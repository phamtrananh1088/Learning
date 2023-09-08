//
//  MessageTask.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class MessageTask: ObservableObject {
    private var bag = Set<AnyCancellable>()
    private var fileRepository = Current.Shared.chatFileRepository
    private var repo = Current.Shared.chatRepository
    private var sessionQueue: DispatchQueue!

    private var tasks: [String] = []
    @Published private var roomId: (ChatDb, String)?

    init() {
        let db = Current.Shared.chatDb
        
        sessionQueue = DispatchQueue(label: "session MessageTask")
        
        db?.chatMessagePendingDao?.unsentOrSendingRoomIds()
            .delay(for: .seconds(0.5), scheduler: sessionQueue)
            .receive(on: sessionQueue)
            .sink(receiveCompletion: { completion in },
                  receiveValue: {[self] ids in
                for id in ids {
                    if db != nil {
                        if tasks.contains(where: { $0 == id.id }) { return }
                        tasks.append(id.id)
                        roomId = (db!, id.id)
                    }
                }
            }).store(in: &bag)
        
        $roomId
            .compactMap({ $0 })
            .setFailureType(to: Never.self)
            .flatMap({ [self] (db, roomId) -> AnyPublisher<(String, StateResult), Never> in
                let f = db.chatMessagePendingDao?.firstUnsentOrSending(roomId: roomId)
                if f == nil {
                    return Just((roomId, StateResult(result: true, message: ""))).eraseToAnyPublisher()
                }
                
                let dao = db.chatMessagePendingDao
                
                let pending = f!
                let id = pending.id!
                dao?.setStatus(status: .STATE_SENDING, id: id)
                
                switch pending.getType() {
                    
                case .TYPE_IMG_SEND:
                    let m = dao?.selectById(id: id)
                    let f: AttachmentExt? = m.map({ $0.attachmentExt(message: $0) }) ?? nil
                    if m == nil || f == nil || !f!.file.canRead() {
                        repo?.deletePendingMessage(pending: pending)
                        return Just((roomId, StateResult(result: true, message: "")))
                            .eraseToAnyPublisher()
                    }
                    
                    return fileRepository!.uploadImage(filename: f!.name, body: f!.contentUri())
                        .replaceError(with: FileKey(fileKey: ""))
                        .eraseToAnyPublisher()
                        .flatMap({ file -> AnyPublisher<(String, StateResult), Never> in
                            if file.fileKey.isEmpty {
                                dao?.setStatus(status: .STATE_ERR, id: id)
                                return Just((roomId, StateResult(result: false, message: "")))
                                    .eraseToAnyPublisher()
                            }
                            
                            let n = ChatMessagePending().imageKeyMessage(m: m!, key: file.fileKey)
                            do {
                                try db.instanceDb?.inDatabase({ db in
                                    try n.update(db)
                                })
                            } catch {
                                debugPrint(error)
                            }
                            
                            _ = f!.file.delete()
                            
                            return Just((roomId, StateResult(result: true, message: "")))
                                .eraseToAnyPublisher()
                        })
                        .eraseToAnyPublisher()
                    
                case .TYPE_AUDIO_SEND:
                    let m = dao?.selectById(id: id)
                    let f: AttachmentExt? = m.map({ $0.attachmentExt(message: $0) }) ?? nil
                    if m == nil || f == nil || !f!.file.canRead() {
                        repo?.deletePendingMessage(pending: pending)
                        return Just((roomId, StateResult(result: true, message: "")))
                            .eraseToAnyPublisher()
                    }
                    
                    return fileRepository!.uploadFile(filename: f!.name, body: f!.contentUri())
                        .replaceError(with: FileKey(fileKey: ""))
                        .eraseToAnyPublisher()
                        .flatMap({ file -> AnyPublisher<(String, StateResult), Never> in
                            if file.fileKey.isEmpty {
                                dao?.setStatus(status: .STATE_ERR, id: id)
                                return Just((roomId, StateResult(result: false, message: "")))
                                    .eraseToAnyPublisher()
                            }
                            
                            let n = ChatMessagePending().audioKeyMessage(m: m!, key: file.fileKey)
                            do {
                                try db.instanceDb?.inDatabase({ db in
                                    try n.update(db)
                                })
                            } catch {
                                debugPrint(error)
                            }
                            
                            _ = f!.file.delete()
                            
                            return Just((roomId, StateResult(result: true, message: "")))
                                .eraseToAnyPublisher()
                        })
                        .eraseToAnyPublisher()

                case .TYPE_VIDEO_SEND:
                    let m = dao?.selectById(id: id)
                    let f: AttachmentExt? = m.map({ $0.attachmentExt(message: $0) }) ?? nil
                    if m == nil || f == nil || !f!.file.canRead() {
                        repo?.deletePendingMessage(pending: pending)
                        return Just((roomId, StateResult(result: true, message: "")))
                            .eraseToAnyPublisher()
                    }
                    
                    return fileRepository!.uploadVideo(filename: f!.name, body: f!.contentUri())
                        .replaceError(with: FileKey2(fileKey: ""))
                        .eraseToAnyPublisher()
                        .flatMap({ file -> AnyPublisher<(String, StateResult), Never> in
                            if file.fileKey.isEmpty {
                                dao?.setStatus(status: .STATE_ERR, id: id)
                                return Just((roomId, StateResult(result: false, message: "")))
                                    .eraseToAnyPublisher()
                            }
                            
                            let old = m!
                            let uploaded = file
                            let oldAtt = f!
                            let newAtt = Attachment(key: uploaded.realFileKey(),
                                                    name: oldAtt.name,
                                                    size: oldAtt.size)
                            let n = ChatMessagePending()
                                .videoKeyMessage(m: old,
                                                cacheFile: newAtt,
                                                key: uploaded.key().fileKey)
                            do {
                                try db.instanceDb?.inDatabase({ db in
                                    try n.update(db)
                                })
                            } catch {
                                debugPrint(error)
                            }
                            
                            let c = Current.Shared.userInfo().cacheByKey(fileKey: newAtt.key, isFile: false)
                            _ = oldAtt.contentUri().moveOrCopy(to: c.file.appendingPathComponent(oldAtt.name, isDirectory: false))
                            _ = oldAtt.file.delete()
                            
                            return Just((roomId, StateResult(result: true, message: "")))
                                .eraseToAnyPublisher()
                        })
                        .eraseToAnyPublisher()
                case .TYPE_FILE_SEND:
                    let m = dao?.selectById(id: id)
                    let f: AttachmentExt? = m.map({ $0.attachmentExt(message: $0) }) ?? nil
                    if m == nil || f == nil || !f!.file.canRead() {
                        repo?.deletePendingMessage(pending: pending)
                        return Just((roomId, StateResult(result: true, message: "")))
                            .eraseToAnyPublisher()
                    }
                    
                    return fileRepository!.uploadFile2(filename: f!.name, body: f!.contentUri())
                        .replaceError(with: FileKey2(fileKey: ""))
                        .eraseToAnyPublisher()
                        .flatMap({ file -> AnyPublisher<(String, StateResult), Never> in
                            if file.fileKey.isEmpty {
                                dao?.setStatus(status: .STATE_ERR, id: id)
                                return Just((roomId, StateResult(result: false, message: "")))
                                    .eraseToAnyPublisher()
                            }
                            
                            let old = m!
                            let uploaded = file
                            let oldAtt = f!
                            let newAtt = Attachment(key: uploaded.realFileKey(),
                                                    name: oldAtt.name,
                                                    size: oldAtt.size)
                            let n = ChatMessagePending()
                                .fileKeyMessage(m: old,
                                                cacheFile: newAtt,
                                                key: uploaded.key().fileKey)
                            do {
                                try db.instanceDb?.inDatabase({ db in
                                    try n.update(db)
                                })
                            } catch {
                                debugPrint(error)
                            }
                            
                            let c = Current.Shared.userInfo().cacheByKey(fileKey: newAtt.key, isFile: false)
                            _ = oldAtt.contentUri().moveOrCopy(to: c.file.appendingPathComponent(oldAtt.name, isDirectory: false))
                            _ = oldAtt.file.delete()
                            
                            return Just((roomId, StateResult(result: true, message: "")))
                                .eraseToAnyPublisher()
                        })
                        .eraseToAnyPublisher()
                    
                default:
                    // Text
                    let m = dao?.selectById(id: id)
                    if  m == nil {
                        repo?.deletePendingMessage(pending: pending)
                        return Just((roomId, StateResult(result: true, message: "")))
                            .eraseToAnyPublisher()
                    }
                    
                    return Current.Shared.chatApi.sendMessage(body: m!, apiKey: Current.Shared.loggedUser?.token ?? "")
                        .replaceError(with: StateResult(result: false, message: ""))
                        .flatMap({ rs -> AnyPublisher<(String, StateResult), Never> in
                            if rs.result {
                                dao?.setStatus(status: .STATE_SENT, id: id)
                            } else {
                                dao?.setStatus(status: .STATE_ERR, id: id)
                            }
                            
                            return Just((roomId, rs))
                                .eraseToAnyPublisher()
                        }).eraseToAnyPublisher()
                }
            })
            .flatMap({ [self] id, state -> AnyPublisher<(String, StateResult), Never> in
                if repo == nil {
                    return Just(("", StateResult(result: true, message: "")))
                        .eraseToAnyPublisher()
                }
                
                return repo!.loadBottomMessage(roomId: id, pageSize: 80)
                    .replaceError(with: .Error(error: .unknow))
                    .map({ e -> AnyPublisher<(String, StateResult), Never> in
                        return Just((id, StateResult(result: true, message: "")))
                            .eraseToAnyPublisher()
                    })
                    .switchToLatest()
                    .eraseToAnyPublisher()
            })
            .receive(on: sessionQueue)
            .sink(receiveValue: { id, state in
                if state.result {
                    self.tasks.removeAll(where: { $0 == id })
                }
            }).store(in: &bag)
    }
    
    func dispose() {
        bag.forEach { $0.cancel()}
    }
}
