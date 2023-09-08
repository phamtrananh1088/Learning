//
//  ChatVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI
import AVKit
import AVFoundation
import GRDB

class ChatVM: BaseViewModel, ObservableObject {
    var playerAudio = AVPlayerViewController()
    var playerVideo = AVPlayerViewController()
    @Published var chatContent: String = String()
    
    @Published var isShowImageFull: Bool = false
    @Published var imageFull: UIImage? = nil
    
    @Published var isShowMenu: Bool = false
    @Published var longClickItem: AbstractMessageUIViewModel? = nil
    
    @Published var isShowReaders: Bool = false
    
    /// DocumentPicker
    @Published var isShowDocumentPicker: Bool = false
    
    // ImagePicker
    @Published var isShowImagePicker: Bool = false
    
    /// Start: Camera
    @Published var isShowCamera: Bool = false
    @Published var cameraMode: CameraModeEnum = .Photo
    @Published var flashMode: CameraFlashModeEnum = .Off
    
    let camera: CameraViewController = CameraViewController()
    
    func handleCameraPhotos() async {
        let unpackedPhotoStream = await camera.photoStream
            .compactMap { self.unpackPhoto($0) }
        
        for await photoData in unpackedPhotoStream {
            do {
                let ext: String = ".jpg"
                let url = pending(ext: ext)
                try photoData.imageData.write(to: url)
                sendImage(roomId: roomSource, uris: [url], ext: ext)
            } catch {
                debugPrint(error)
            }
            
            DispatchQueue.main.async {
                self.onHideCamera()
            }
        }
    }
    
    func handleCameraMovies() async {
        let movieStream = await camera.movieStream
            .compactMap({ $0 })
        
        for await movieData in movieStream {
            if cameraMode == .VideoNonRecording {
                sendVideo(roomId: roomSource, uri: movieData, ext: ".mp4")
                DispatchQueue.main.async {
                    self.onHideCamera()
                }
            }
        }
    }
    
    private func unpackPhoto(_ photo: AVCapturePhoto) -> PhotoData? {
        guard let imageData = photo.fileDataRepresentation() else { return nil }

        guard let previewCGImage = photo.previewCGImageRepresentation(),
           let metadataOrientation = photo.metadata[String(kCGImagePropertyOrientation)] as? UInt32,
              let cgImageOrientation = CGImagePropertyOrientation(rawValue: metadataOrientation) else { return nil }
        let imageOrientation = Image.Orientation(cgImageOrientation)
        let thumbnailImage = Image(decorative: previewCGImage, scale: 1, orientation: imageOrientation)
        
        let photoDimensions = photo.resolvedSettings.photoDimensions
        let imageSize = (width: Int(photoDimensions.width), height: Int(photoDimensions.height))
        let previewDimensions = photo.resolvedSettings.previewDimensions
        let thumbnailSize = (width: Int(previewDimensions.width), height: Int(previewDimensions.height))
        
        return PhotoData(thumbnailImage: thumbnailImage, thumbnailSize: thumbnailSize, imageData: imageData, imageSize: imageSize)
    }

    func onHideCamera() {
        withAnimation {
            isShowCamera.toggle()
        }
        
        camera.stop()
        cameraMode = .Photo
    }
    
    /// End: Camera
    
    /// Start: Microphone
    let microphone = Microphone()
    var currentRecordedURL: URL? = nil
    @Published var isShowVoiceRecorder: Bool = false
    @Published var recordingMode: RecordingModeEnum = .NonRecording
    @Published var timeStartRecord: Int64 = 0
    
    func onHideVoiceRecorder(deleteFile: Bool = false) {
        withAnimation {
            isShowVoiceRecorder.toggle()
        }
        
        if deleteFile {
            _ = currentRecordedURL?.delete()
        }
        
        microphone.stopRecording()
        recordingMode = .NonRecording
    }
    
    func handleAudios() async {
        let audioStream = microphone.audioStream
            .compactMap({ $0 })
        
        for await (isSuccess, audioData) in audioStream {
            if recordingMode == .FinishedRecording && isSuccess {
                currentRecordedURL = audioData.url
            } else {
                debugPrint(audioData.url.delete())
            }
        }
    }
    
    /// End: Microphone
    
    var _scrollViewProxy: Any? = nil
    @available(iOS 14.0, *)
    fileprivate var scrollViewProxy: ScrollViewProxy? {
        return _scrollViewProxy as? ScrollViewProxy
    }

    class KEY {
        class State: KEY {
            var first: MessageItemChat
            var last: MessageItemChat
            
            init(first: MessageItemChat, last: MessageItemChat) {
                self.first = first
                self.last = last
            }
        }
        
        class Sent: KEY {
            var m: MessageItemChat.Sent
            init(m: MessageItemChat.Sent) {
                self.m = m
            }
        }
        
        class Pending: KEY {
            var m: MessageItemChat.Pending
            init(m: MessageItemChat.Pending) {
                self.m = m
            }
        }
    }
    
    @Published var roomSource: String = ""
    
    @Published var loadType: LoadTypeEnum = .REFRESH(key: nil)
    var prev: KEY? {
        let it = chatLiveData.last?.bound
        if it is MessageItemChat.Sent {
            return KEY.Sent(m: it as! MessageItemChat.Sent)
        } else if it is MessageItemChat.Pending {
            return KEY.Pending(m: it as! MessageItemChat.Pending)
        }
        
        return nil
    }
    
    var next: KEY? {
        let it = chatLiveData.first?.bound
        if it is MessageItemChat.Sent {
            return KEY.Sent(m: it as! MessageItemChat.Sent)
        } else if it is MessageItemChat.Pending {
            return KEY.Pending(m: it as! MessageItemChat.Pending)
        }
        
        return nil
    }
    
    var refresh: KEY? {
        let first = chatLiveData.last?.bound
        let last = chatLiveData.first?.bound
        if first == nil || last == nil {
            return nil
        }
        
        return KEY.State(first: first!, last: last!)
    }

    private var chatRoomWithUsers: AnyPublisher<ChatRoomWithUsers2?, Error> {
        return $roomSource
            .setFailureType(to: Error.self)
            .map({ it in
                return self.chatRoomRepository!.chatRoomWithUsers2(roomId: it)
            })
            .switchToLatest()
            .eraseToAnyPublisher()
    }
    
    private var observationChatMessage = ValueObservation.tracking { db in
        try ChatMessage.fetchAll(db)
    }
    
    private var observationChatMessagePending = ValueObservation.tracking { db in
        try ChatMessagePending.fetchAll(db)
    }

    @Published var roomData: ChatRoomWithUsers2?

    private let pageSize = 40
    func loadRoom(roomId: String) {
        self.chatLiveData = []
        
        if roomId.isEmpty { return }
        roomSource = roomId
        onRefresh()
    }

    private var chatRoomRepository = Current.Shared.chatRoomRepository
    private var chatRepository = Current.Shared.chatRepository
    
    @Published var chatLiveData: [AbstractMessageUIViewModel] = []
    @Published var chatLiveState: LoadingStateEnum = .Ok

    func insertMessage(roomId: String, text: String) {
        DispatchQueue.global().asyncAfter(deadline: DispatchTime.now() + 0.2, execute: {
            do {
                try self.current.chatDb!.instanceDb?.write { db in
                    try ChatMessagePending()
                        .textMessage(roomId: roomId, text: text)
                        .insert(db)
                }
            } catch {
                print(error)
            }
        })
    }

    func resetErrStatus(chatMessagePending: ChatMessagePending) {
        if (chatMessagePending.status == MessagePendingStateEnum.STATE_ERR.rawValue) {
            DispatchQueue.global().async {
                self.current.chatDb?.chatMessagePendingDao?.setStatus(status: MessagePendingStateEnum(rawValue: 0)!, id: chatMessagePending.id!)
            }
        }
    }

    func resetAllErrStatus(roomId: String) {
        DispatchQueue.global().async {
            self.current.chatDb?.chatMessagePendingDao?.resetErr(roomId: roomId)
        }
    }

    @Published private var markRead: String = ""
    @Published var lastItemRead: String = ""
    
    private var chatRoomWithUsersAndLoadType: AnyPublisher<(LoadTypeEnum, Event<Any?>), NetworkError> {
        return Publishers.CombineLatest(
            chatRoomWithUsers
                .mapError({ NetworkError.error(error: $0)})
                .removeDuplicates(by: { (t1, t2) in
                    t1 != nil
                    && t2 != nil
                    && t1!.room.id == t2!.room.id
                })
                .eraseToAnyPublisher(),
            $loadType.setFailureType(to: NetworkError.self).eraseToAnyPublisher())
        .map({ [self] (opt, type) -> AnyPublisher<(LoadTypeEnum, Event<Any?>), NetworkError> in
            DispatchQueue.main.async {
                withAnimation {
                    self.chatLiveState = .Loading
                }
            }
            
            if opt?.room.id == nil {
                return Just((LoadTypeEnum.REFRESH(key: nil), Event.Value(value: nil)))
                    .setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            }

            let roomId = opt?.room.id ?? ""

            switch type {

            case .REFRESH(let key):
                return Just((LoadTypeEnum.REFRESH(key: key), Event.Value(value: nil)))
                    .setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            case .PREPEND(let key):
                return chatRepository!.loadTopMessage(roomId: roomId, pageSize: pageSize)
                    .flatMap({ e -> AnyPublisher<(LoadTypeEnum, Event<Any?>), NetworkError> in
                        switch e {
                        case .Value(value: _):
                            return Just((LoadTypeEnum.PREPEND(key: key), Event.Value(value: nil)))
                                .setFailureType(to: NetworkError.self)
                                .eraseToAnyPublisher()
                        case .Error(error: _):
                            return Just((LoadTypeEnum.PREPEND(key: key), Event.Error(error: NetworkError.unknow)))
                                .setFailureType(to: NetworkError.self)
                                .eraseToAnyPublisher()
                        }
                    }).eraseToAnyPublisher()
            case .APPEND(let key):
                return chatRepository!.loadBottomMessage(roomId: roomId, pageSize: pageSize)
                    .flatMap({ e -> AnyPublisher<(LoadTypeEnum, Event<Any?>), NetworkError> in
                        switch e {
                        case .Value(value: _):
                            return Just((LoadTypeEnum.APPEND(key: key), Event.Value(value: nil)))
                                .setFailureType(to: NetworkError.self)
                                .eraseToAnyPublisher()
                        case .Error(error: _):
                            return Just((LoadTypeEnum.APPEND(key: key), Event.Error(error: NetworkError.unknow)))
                                .setFailureType(to: NetworkError.self)
                                .eraseToAnyPublisher()
                        }
                    })
                    .eraseToAnyPublisher()
            }
        })
        .switchToLatest()
        .eraseToAnyPublisher()
    }
    
    private var chatRoomMessages: AnyPublisher<(LoadTypeEnum, Event<[MessageItemChat]>), NetworkError> {
        return chatRoomWithUsersAndLoadType
            .map({ [self] (loadType, e) -> AnyPublisher<(LoadTypeEnum, Event<[MessageItemChat]>), NetworkError> in

                var tempLst: [MessageItemChat] = []
                let pd = allPending()

                switch loadType {
                case .REFRESH(let key):
                    if let k = key {
                        if k is KEY.State {
                            let f = (k as! KEY.State).first
                            if f is MessageItemChat.Pending {
                                tempLst.append(contentsOf: pd)
                                tempLst.append(contentsOf: latestSent(size: pageSize))
                            } else if f is MessageItemChat.Sent {
                                let l = (k as! KEY.State).last
                                if l is MessageItemChat.Pending {
                                    let s = chatRepository!
                                        .localMessagesBetweenAround(
                                            roomId: roomSource,
                                            startR: (f as! MessageItemChat.Sent).msg.messageRow,
                                            endR: (f as! MessageItemChat.Sent).msg.messageRow,
                                            prepend: pageSize,
                                            append: Int.max)

                                    tempLst.append(contentsOf: pd)
                                    tempLst.append(contentsOf: sortedByRow(s))
                                } else if l is MessageItemChat.Sent {
                                    let s = chatRepository!
                                        .localMessagesBetweenAround(
                                            roomId: roomSource,
                                            startR: (f as! MessageItemChat.Sent).msg.messageRow,
                                            endR: (l as! MessageItemChat.Sent).msg.messageRow,
                                            prepend: pageSize,
                                            append: pageSize)

                                    if s.isEmpty {
                                        tempLst.append(contentsOf: pd)
                                    } else {
                                        tempLst.append(contentsOf: pd)
                                        tempLst.append(contentsOf: sortedByRow(s))
                                    }
                                }
                            }
                        }
                    } else {
                        tempLst.append(contentsOf: pd)
                        tempLst.append(contentsOf: latestSent(size: pageSize))
                    }

                case .APPEND(let key):
                    let k = key
                    if k is KEY.Sent {
                        let msgRow  = (k as! KEY.Sent).m.msg.messageRow
                        let s = sortedByRow(chatRepository!.localMessagesNext(roomId: roomSource, pageSize: pageSize, startR: msgRow))

                        if s.isEmpty {
                            tempLst.append(contentsOf: pd)
                        } else {
                            tempLst.append(contentsOf: s)
                        }

                    } else if k is KEY.Pending {
                        let crd = (k as! KEY.Pending).m.pending.createdDate
                        let s = nextPending(createdDate: crd)
                        tempLst.append(contentsOf: s)
                    } else {
                        // Do not thing
                    }

                    break
                case .PREPEND(let key):
                    let k = key
                    if k is KEY.Sent {
                        let msgRow = (k as! KEY.Sent).m.msg.messageRow
                        let s = chatRepository!.localMessagesPrev(roomId: roomSource, pageSize: pageSize, startR: msgRow)
                        tempLst.append(contentsOf: sortedByRow(s))
                    } else if k is KEY.Pending {
                        let crd = (k as! KEY.Pending).m.pending.createdDate
                        let s = prevPending(createdDate: crd)
                        if s.isEmpty {
                            tempLst.append(contentsOf: latestSent(size: pageSize))
                        } else {
                            tempLst.append(contentsOf: s)
                        }

                    } else {
                        // Do not thing
                    }
                }

                switch e {

                case .Value(value: _):
                    return Just((loadType, Event.Value(value: tempLst)))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                case .Error(error: _):
                    return Just((loadType, Event.Error(error: NetworkError.unknow)))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                }
            })
            .switchToLatest()
            .eraseToAnyPublisher()
    }

    override init() {
        tmpDir = Config.Shared.tmpDir.createSubDirectory(name: "tmp")
        super.init()
        
        try? AVAudioSession
            .sharedInstance()
            .setCategory(.playAndRecord, mode: .videoRecording)
        
        self.playerAudio.setValue(false, forKey: "canHidePlaybackControls")
        self.playerAudio.contentOverlayView?.backgroundColor = .black
        
        Task {
            await handleCameraPhotos()
        }
        
        Task {
            await handleCameraMovies()
        }
        
        Task {
            await handleAudios()
        }
        
        chatRoomWithUsers
            .map({ it in
                return Just(it)
                    .setFailureType(to: Error.self)
            })
            .switchToLatest()
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { c in
                self.roomData = c
            }).store(in: &bag)
        
        Publishers.CombineLatest(
            observationChatMessage
                .publisher(in: current.chatDb!.instanceDb!, scheduling: .immediate)
                .eraseToAnyPublisher(),
            observationChatMessagePending
                .publisher(in: current.chatDb!.instanceDb!, scheduling: .immediate)
                .eraseToAnyPublisher())
        .map({ ob1, ob2 -> AnyPublisher<([ChatMessage], [ChatMessagePending]), Error> in
            return Just((ob1, ob2))
                .setFailureType(to: Error.self)
                .eraseToAnyPublisher()
        })
        .switchToLatest()
        .eraseToAnyPublisher()
        .sink(receiveCompletion: { completion in },
              receiveValue: { ob1, ob2 in
          self.onRefresh()
        }).store(in: &bag)
        
        $lastItemRead
            .removeDuplicates(by: {(t1, t2) in
                return !t1.isEmpty && !t2.isEmpty && t2 <= t1
            })
            .map({ row in
                if row.isEmpty {
                    return Just(StateResult(result: true, message: nil)).eraseToAnyPublisher()
                }
                
                return self.chatRepository!.updateRead(messageRow: row)
                    .retry(3)
                    .replaceError(with: StateResult(result: false, message: nil))
                    .eraseToAnyPublisher()
            })
            .switchToLatest()
            .eraseToAnyPublisher()
            .sink(receiveValue: { state in
            }).store(in: &bag)
        
        chatRoomMessages
            .receive(on: DispatchQueue(label: "chatRoomMessages"))
            .sink(receiveCompletion: { completion in },
                  receiveValue: { (type, event) in
                switch event {
                case .Value(value: let value):
                    self.appendToChatLiveData(type, value!)
                    
                    DispatchQueue.main.async {
                        withAnimation {
                            self.chatLiveState = .Ok
                        }
                    }
                case .Error(error: _):
                    DispatchQueue.main.async {
                        withAnimation {
                            self.chatLiveState = .Error
                        }
                    }
                }
            }).store(in: &bag)
    }
    
    private func appendToChatLiveData(_ type: LoadTypeEnum, _ lst: [MessageItemChat]) {
        DispatchQueue(label: "appendToChatLiveData").async { [self] in
            // Diff
            var isDiff: Bool = false
            if chatLiveData.count != lst.count {
                isDiff = true
            } else {
                if chatLiveData.count > 0 {
                    for i in 0...chatLiveData.count - 1 {
                        if !chatLiveData[i].bound.sameContent(other: lst[i]) {
                            isDiff = true
                            break
                        }
                    }
                }
            }
            
            switch type {
                
            case .REFRESH(key: _):
                if isDiff {
                    let newLst = maptoVm(lst)
                   
                    if newLst.count == chatLiveData.count {
                        for i in 0...newLst.count - 1 {
                            if !chatLiveData[i].bound.sameContent(other: newLst[i].bound) {
                                DispatchQueue.main.async {
                                    self.chatLiveData[i] = newLst[i]
                                }
                            }
                        }
                    } else {
                        DispatchQueue.main.async {
                            self.chatLiveData = newLst
                        }
                    }
                }
            case .PREPEND(key:_):
                if !lst.isEmpty {
                    DispatchQueue.main.async {
                        self.chatLiveData.append(contentsOf: self.maptoVm(lst))
                    }
                }
            case .APPEND(key:_):
                if !lst.isEmpty {
//                    self.chatLiveData.insert(contentsOf: maptoVm(lst), at: 0)
                }
            }
            
            for item in self.chatLiveData {
                if let msg = item.bound as? MessageItemChat.Sent {
                    if !msg.msg.isSelf() {
                        DispatchQueue.main.async {
                            self.lastItemRead = msg.msg.messageRow
                        }
                        return
                    }
                }
            }
        }
    }
    
    private func maptoVm(_ lst: [MessageItemChat]) -> [AbstractMessageUIViewModel] {
        return lst.map({ item in
            if let existed = self.chatLiveData.first(where: { $0.bound is MessageItemChat.Sent && $0.bound.listId == item.listId }) {
                existed.update(bound: item)
                
                return existed
            }
            
            if item is TextProtocol {
                if item is MessageItemChat.Sent.TextSent {
                    return MessageTextUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Sent.TextSent)
                } else {
                    return MessageTextUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Pending.TextPending)
                }
            } else  if item is ImageProtocol {
                if item is MessageItemChat.Sent.ImgSent {
                    return MessageImageUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Sent.ImgSent)
                } else {
                    return MessageImageUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Pending.ImgPending)
                }
            } else if item is VideoProtocol {
                if item is MessageItemChat.Sent.VideoSent {
                    return MessageVideoUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Sent.VideoSent)
                } else {
                    return MessageVideoUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Pending.VideoPending)
                }
            } else if item is AudioProtocol {
                if item is MessageItemChat.Sent.AudioSent {
                    return MessageAudioUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Sent.AudioSent)
                } else {
                    return MessageAudioUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Pending.AudioPending)
                }
            } else if item is FProtocol {
                if item is MessageItemChat.Sent.FileSent {
                    return MessageFileUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Sent.FileSent)
                } else {
                    return MessageFileUIViewModel(rtl: item.selfField, bound: item as! MessageItemChat.Pending.FilePending)
                }
            }
            else {
                return AbstractMessageUIViewModel(bound: item)
            }
        })
    }

    func markRead(messageRow: String) {
        markRead = messageRow
    }
    
    func onRefresh(_ isInit: Bool = false) {
        DispatchQueue.main.async {
            if isInit {
                self.loadType = .REFRESH(key: nil)
            } else {
                self.loadType = .REFRESH(key: self.refresh)
            }
        }
    }
    
    func onAppend() {
        if let item = next {
            DispatchQueue.main.async {
                self.loadType = .APPEND(key: item)
            }
        } else {
            withAnimation {
                chatLiveState = .Loading
            }
            
            chatRepository?.loadBottomMessage(roomId: roomSource, pageSize: pageSize)
                .receive(on: DispatchQueue.global())
                .sink(receiveCompletion: { completion in },
                      receiveValue: { _ in
                    self.onRefresh(true)
                }).store(in: &bag)
        }
    }
    
    func onPrepend() {
        if let item = prev {
            DispatchQueue.main.async {
                self.loadType = .PREPEND(key: item)
            }
        } else {
            withAnimation {
                chatLiveState = .Loading
            }
            
            chatRepository?.loadTopMessage(roomId: roomSource, pageSize: pageSize)
                .receive(on: DispatchQueue.global())
                .sink(receiveCompletion: { completion in },
                      receiveValue: { _ in
                    self.onRefresh(true)
                }).store(in: &bag)
        }
    }
    
    var tmpDir: URL
    func pending(ext: String) -> URL {
        return tmpDir
            .appendingPathComponent("\(currentTimeMillis())\(ext)", isDirectory: false)
    }
    
    //20 seconds limit.
    func sendImage(roomId: String, uris: [URL], ext: String = String()) {
        DispatchQueue(label: "imageTask").async {
            let tmpFiles: [URL] = !ext.isEmpty
            ? uris
            : uris.compactMap({ url -> URL? in
                debugPrint(url.startAccessingSecurityScopedResource())
                return readToTmpFile(uri: url, dir: self.tmpDir)
            }).compactMap({ $0 })
            
            let maxSize: CGFloat = 1080
            for temp in tmpFiles {
                if let uiImg: UIImage = UIImage(contentsOfFile: temp.path) {
                    let s = maxSize / max(uiImg.size.width, uiImg.size.height)
                    let newImg = s >= 1 ? uiImg.jpegData(compressionQuality: 1) : uiImg.jpegData(compressionQuality: s)
                    
                    let t2: URL = temp.deletingPathExtension().appendingPathExtension(".jpg")
                    
                    do {
                        _ = temp.delete()
                        try newImg?.write(to: t2)
                    } catch {
                        debugPrint(error)
                    }

                    let name = !ext.isEmpty ? "\(timeString())\(ext)" : t2.lastPathComponent
                    
                    if let att = self.saveLocalAttachment(tmp: t2, name: name) {
                        let file = att.file
                        let size = file.decodeImageSize()
                        let img = Img(attachment: att, url: att.file.path, width: Int(size.width), height: Int(size.height))
                        let message = ChatMessagePending().imageMessage(roomId: roomId, localImg: img)
                        
                        do {
                            try self.current.chatDb?.instanceDb?.write { db in
                                try message.insert(db)
                            }
                        } catch {
                            debugPrint(error)
                        }
                    }
                }
            }
        }
    }

    private func saveLocalAttachment(tmp: URL, name: String) -> AttachmentExt? {
        let keyName = UUID().uuidString
        let file = current
            .userInfo()
            .userLocalFileDir!
            .createSubDirectory(name: keyName)
            .appendingPathComponent(name, isDirectory: false)
        
        if !tmp.moveOrCopy(to: file) {
            debugPrint("saveLocalAttachment: exception")
            return nil
        }
        
        if let attributes = try? FileManager.default.attributesOfItem(atPath: file.path) {
            let a = Attachment(key: keyName, name: name, size: attributes[.size] as! Int64)
            
            return AttachmentExt(file: file, isCacheFile: false, key: a.key, name: a.name, size: a.size)
        }
        
        return nil
    }

    //20 seconds limit.
    func sendAudio(roomId: String, uri: URL, ext: String) {
        let name = "\(timeString())\(ext)"
        if let att = saveLocalAttachment(tmp: uri, name: name) {
            let message = ChatMessagePending().audioMessage(roomId: roomId, localFile: att)
            do {
                try current.chatDb?.instanceDb?.write { db in
                    try message.insert(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }

    func sendVideo(roomId: String, uri: URL, ext: String) {
        let name = "\(timeString())\(ext)"
        if let att = saveLocalAttachment(tmp: uri, name: name) {
            let message = ChatMessagePending().videoMessage(roomId: roomId, localFile: att)
            
            do {
                try current.chatDb?.instanceDb?.write { db in
                    try message.insert(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }

    func sendFile(roomId: String, uri: URL) {
        let name = uri.lastPathComponent
        let tmp = readToTmpFile(uri: uri, dir: tmpDir)
        
        if tmp == nil { return }
        
        if let att = saveLocalAttachment(tmp: tmp!, name: name) {
            let message = ChatMessagePending().fileMessage(roomId: roomId, localFile: att)
            
            do {
                try current.chatDb?.instanceDb?.write { db in
                    try message.insert(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }
    
    func deleteMessage(_ msg: MessageItemChat) {
        if msg is MessageItemChat.Pending {
            chatRepository?.deletePendingMessage(pending: (msg as! MessageItemChat.Pending).pending)
        } else if msg is MessageItemChat.Sent {
            chatRepository?.deleteMessage(msg: (msg as! MessageItemChat.Sent).msg)
                .receive(on: DispatchQueue.global())
                .sink(receiveCompletion: { completion in },
                      receiveValue: { state in
                    if state.result {
                        do {
                            _ = try Current.Shared.chatDb!.instanceDb?.write { db in
                                try (msg as! MessageItemChat.Sent).msg.delete(db)
                            }
                        } catch {
                            debugPrint(error)
                        }
                    }
                }).store(in: &bag)
        }
    }
    
    private func allPending() -> [MessageItemChat.Pending] {
        return sortedByTime(chatRepository!.localPendingMessages(roomId: roomSource))
    }

    private func nextPending(createdDate: Int64) -> [MessageItemChat.Pending] {
        return sortedByTime(chatRepository!.localPendingMessagesNext(roomId: roomSource, createdDate: createdDate))
    }

    private func prevPending(
        createdDate: Int64,
        includeSelf: Bool = false
    ) -> [MessageItemChat.Pending] {
        return sortedByTime(chatRepository!.localPendingMessagesPrev(roomId: roomSource, createdDate: createdDate, includeSelf: includeSelf))
    }

    private func latestSent(size: Int) -> [MessageItemChat.Sent] {
        return sortedByRow(chatRepository!.localLatestSentMessage(roomId: roomSource, pageSize: size))
    }
    
    private func sortedByRow(_ lst: [ChatMessage]) -> [MessageItemChat.Sent] {
        let list = lst.sorted(by: {(t1, t2) in
            t1.messageRow >= t2.messageRow
        })
        
        var target: [MessageItemChat.Sent] = []
        
        forEachPrevNext(list) { (prev, it, next) in
            let uid = it.userId
            let n = prev?.userId == uid
            let p = next?.userId == uid
            
            switch MessageTypeEnum.init(rawValue: it.type)! {
                
            case .TYPE_VIDEO:
                target.append(MessageItemChat.Sent.VideoSent(msg: it,
                                                   user: self.userGetter(u: it.userId),
                                                   readers: self.readStatus(m: it),
                                                   hasPrev: p, hasNext: n))
                
            case .TYPE_AUDIO:
                target.append(MessageItemChat.Sent.AudioSent(msg: it,
                                                             user: self.userGetter(u: it.userId),
                                                             readers: self.readStatus(m: it),
                                                             hasPrev: p,
                                                             hasNext: n))
                
            case .TYPE_IMG:
                target.append(MessageItemChat.Sent.ImgSent(msg: it,
                                                             user: self.userGetter(u: it.userId),
                                                             readers: self.readStatus(m: it),
                                                             hasPrev: p,
                                                             hasNext: n))
                
            case .TYPE_FILE:
                target.append(MessageItemChat.Sent.FileSent(msg: it,
                                                             user: self.userGetter(u: it.userId),
                                                             readers: self.readStatus(m: it),
                                                             hasPrev: p,
                                                             hasNext: n))
                
            default:
                target.append(MessageItemChat.Sent.TextSent(msg: it,
                                                             user: self.userGetter(u: it.userId),
                                                             readers: self.readStatus(m: it),
                                                             hasPrev: p,
                                                             hasNext: n))
            }
        }
        
        return target
    }
    
    private func sortedByTime(_ lst: [ChatMessagePending]) -> [MessageItemChat.Pending] {
        let list = lst.sorted(by: {(t1, t2) in
            t1.createdDate >= t2.createdDate
        })
        
        var target: [MessageItemChat.Pending] = []
        forEachPrevNext(list, { (prev, it, next) in
            let uid = it.userId
            let n = prev?.userId == uid
            let p = next?.userId == uid
            
            switch MessagePendingTypeEnum.init(rawValue: it.type)! {
            case .TYPE_IMG_SEND, .TYPE_IMG:
                target.append(MessageItemChat.Pending.ImgPending(pending: it, hasPrev: p, hasNext: n))
            case .TYPE_AUDIO_SEND, .TYPE_AUDIO:
                target.append(MessageItemChat.Pending.AudioPending(pending: it, hasPrev: p, hasNext: n))
            case .TYPE_VIDEO_SEND, .TYPE_VIDEO:
                target.append(MessageItemChat.Pending.VideoPending(pending: it, hasPrev: p, hasNext: n))
            case .TYPE_FILE_SEND, .TYPE_FILE:
                target.append(MessageItemChat.Pending.FilePending(pending: it, hasPrev: p, hasNext: n))
            default:
                target.append(MessageItemChat.Pending.TextPending(pending: it, hasPrev: p, hasNext: n))
            }
        })
        
        return target
    }
    
    func userGetter(u: String) -> ChatUser? {
        return roomData?.users.first(where: { $0.chatUser.id == u })?.chatUser
    }
    
    func readStatus(m: ChatMessage) -> [ChatUser?] {
        return roomData?.users.map({ r in
            if r.lastRow != nil && r.lastRow! > m.messageRow {
                return r.chatUser
            }
            
            return nil
        }) ?? []
    }
    
    func forEachPrevNext<T>(_ list: [T], _ block: @escaping (_ prev: T?, _ it: T, _ next: T?) -> Void) {
        var it = list.makeIterator()
        
        let f = it.next()
        if f == nil { return }
        let s = it.next()
        
        block(nil, f!, s)
        
        if s == nil { return}
        
        var p = f!
        var e = s!
        while let n = it.next() {
            block(p, e, n)
            p = e
            e = n
        }
        
        block(p, e, nil)
    }
}

fileprivate struct PhotoData {
    var thumbnailImage: Image
    var thumbnailSize: (width: Int, height: Int)
    var imageData: Data
    var imageSize: (width: Int, height: Int)
}

fileprivate extension CIImage {
    var image: Image? {
        let ciContext = CIContext()
        guard let cgImage = ciContext.createCGImage(self, from: self.extent) else { return nil }
        return Image(decorative: cgImage, scale: 1, orientation: .up)
    }
}

fileprivate extension Image.Orientation {

    init(_ cgImageOrientation: CGImagePropertyOrientation) {
        switch cgImageOrientation {
        case .up: self = .up
        case .upMirrored: self = .upMirrored
        case .down: self = .down
        case .downMirrored: self = .downMirrored
        case .left: self = .left
        case .leftMirrored: self = .leftMirrored
        case .right: self = .right
        case .rightMirrored: self = .rightMirrored
        }
    }
}
