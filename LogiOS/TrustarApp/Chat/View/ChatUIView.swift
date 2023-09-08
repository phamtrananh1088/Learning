//
//  ChatUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine
import AVKit
import AVFoundation
import Foundation
import UIKit
import Photos
import MobileCoreServices

struct ChatUIView: View {
    @ObservedObject var vm: ChatVM
    @ObservedObject var homeVm: HomeViewModel
    @Binding var isShowChatRoom: Bool
    @EnvironmentObject private var navigationStack: NavigationStack
    @State private var isPlayAudio: Bool = false
    @State private var isPlayVideo: Bool = false
    @State private var isShowPopupCopied: Bool = false
    @State private var isShowShareSheet: Bool = false
    
    var onBack: () -> Void
    let timer = Timer.publish(every: 60, on: .main, in: .common).autoconnect()
    
    private var title: String {
        if let r = vm.roomData {
            if let name = r.room.name {
                return name
            }
        }
        
        return String()
    }
    
    private var isLoading: Bool {
        switch vm.chatLiveState {
        case .Loading:
            return true
        default:
            return false
        }
    }
    
    private func stopPlayAudio() {
        isPlayAudio = false
        vm.playerAudio.player?.pause()
    }
    
    /// ToolTips
    @State private var frame: CGRect = CGRect()
    @State private var contentWidth: CGFloat = 10
    @State private var contentHeight: CGFloat = 10
    @State private var readers: [String] = []
    private let arrowWidth: CGFloat = 20
    private let arrowHeight: CGFloat = 9
    
    private var x: CGFloat {
        let tempX = frame.maxX - UIScreen().bounds.size.width - contentWidth - offsetX
        return tempX > 16 ? tempX : 16
        
    }
    private var y: CGFloat {
        return frame.maxY - offsetY - contentHeight / 2
    }
    
    private let offsetX: CGFloat = 35
    private var offsetY: CGFloat {
        if UIScreen.main.bounds.width > UIScreen.main.bounds.height {
            return 0
        }
        
        if UIDevice.current.hasNotch {
            return 60
        }
        
        return 25
    }
    
    private var arrowView: some View {
        
        return AnyView(ArrowShape()
            .rotation(Angle(radians: .pi / 2))
            .stroke(Color(Resources.grayBackground))
            .foregroundColor(Color(Resources.grayBackground))
            .background(ArrowShape()
                .offset(x: 0, y: 1)
                .rotation(Angle(radians: .pi / 2))
                .frame(width: arrowWidth + 2, height: arrowHeight + 1)
                .foregroundColor(Color(Resources.grayBackground))
            )
                .offset(x: x + contentWidth / 2 - 5, y: y)
                .frame(width: arrowWidth, height: arrowHeight)
        )
    }
    
    func onShowHideReaders(geo: GeometryProxy, item: MessageItemChat.Sent) {
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.1, execute: {
            vm.isShowReaders = true
            readers = item.readers.map({ $0.name.orEmpty() })
            frame = geo.frame(in: CoordinateSpace.global)
        })
    }
    
    var body: some View {
        ZStack {
            ZStack {
                VStack(spacing: 0) {
                    // Region: Header
                    HomeHeaderView(isShowContent: false)
                    
                    HStack {
                        Button(action: {
                            onBack()
                        }, label: {
                            Image(systemName: "xmark")
                                .foregroundColor(.white)
                                .font(.system(size: 18))
                                .frame(alignment: .topLeading)
                                .padding(5)
                        })
                        
                        Text(title)
                            .foregroundColor(.white)
                            .bold()
                            .lineLimit(1)
                            .font(.system(size: 18))
                            .frame(maxWidth: .infinity, alignment: .leading)
                        
                        Button(
                            action: {
                                self.endTextEditing()
                                
                                homeVm.chatOptionVm.loadRoom(roomId: vm.roomSource)
                                withAnimation {
                                    homeVm.isShowChatOption = true
                                }
                            },
                            label: {
                                Image(systemName: "ellipsis")
                                    .foregroundColor(Color.white)
                                    .font(.system(size: 15, weight: .bold))
                                    .rotationEffect(Angle.degrees(90))
                                    .frame(width: 30, height: 30)
                            })
                        .contextMenu(menuItems: {
                            Text(Resources.talk_option)
                        })
                    }
                    .padding(.vertical, 10)
                    .padding(.horizontal, 8)
                    .background(Color(Resources.colorPrimary))
                    
                    MessageListView(
                        vm: vm,
                        isPlayAudio: $isPlayAudio,
                        isPlayVideo: $isPlayVideo,
                        onShowMenu: { item in
                            let feedbackGenerator = UINotificationFeedbackGenerator()
                            feedbackGenerator.notificationOccurred(.success)
                            vm.longClickItem = item
                            
                            withAnimation(.linear(duration: 0.2)) {
                                vm.isShowMenu.toggle()
                            }
                        },
                        onShowHideReaders: onShowHideReaders
                    )
                    .overlay(
                        ZStack {
                            Circle()
                                .stroke(
                                    style: StrokeStyle(
                                        lineWidth: 0.3,
                                        lineCap: .round,
                                        lineJoin: .round))
                                .foregroundColor(.black)
                                .background(Color.white)
                                .clipShape(Circle())
                                .frame(width: 40, height: 40)
                            Spin(color: .blue, size: 15)
                        }
                        .opacity(isLoading ? 1 : 0)
                        .padding(.top), alignment: .top)

                    ChatControlView(chatContent: $vm.chatContent) { action in
                        
                        switch action {
                        case .Audio:
                            withAnimation {
                                Task {
                                    let _ = await vm.microphone.checkAuthorization()
                                }
                                
                                vm.isShowVoiceRecorder.toggle()
                            }
                        case .Image:
                            withAnimation {
                                PhotoLibrary.checkAuthorization({ isAuthorized in
                                    if isAuthorized {
                                        DispatchQueue.main.async {
                                            vm.isShowImagePicker.toggle()
                                        }
                                    } else {
                                        homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.app_permission_settings_image, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok, rightBtnClick: {
                                            UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
                                        }))
                                    }
                                })
                            }
                        case .Camera:
                            withAnimation {
                                if !vm.camera.hasFlash() {
                                    vm.flashMode = .NotAvailable
                                } else {
                                    vm.flashMode = .Off
                                }
                                
                                Task {
                                    let isAuthorized = await vm.camera.checkAuthorization()

                                    if isAuthorized {
                                        vm.isShowCamera.toggle()
                                    } else {
                                        homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.app_permission_settings_camera, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok, rightBtnClick: {
                                            UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
                                        }))
                                    }
                                }
                            }

                        case .File:
                            vm.isShowDocumentPicker = true
                            break
                        case .Send:
                            if !vm.chatContent.isEmpty {
                                vm.insertMessage(roomId: vm.roomSource, text: vm.chatContent)
                                vm.chatContent = ""
                            }
                        }
                        
                        self.endTextEditing()
                    }
                }
            }
            .edgesIgnoringSafeArea(.top)
            .background(Color(Resources.defaultBackground))
            .onReceive(timer, perform: {(_) in
                _ = vm.current.chatRepository?.loadBottomMessage(roomId: vm.roomSource, pageSize: 90)
                    .subscribe(on: DispatchQueue.main)
                    .sink(receiveCompletion: { completion in },
                          receiveValue: { s in
                        DispatchQueue.main.async {
                            self.vm.onAppend()
                        }
                    })
            })
            .blur(radius: vm.isShowMenu ? CGFloat(10) : CGFloat.zero)
            .disabled(vm.isShowMenu || isPlayAudio || isPlayVideo)
            .transition(.move(edge: .trailing))
            .onSwipeToBack(isPresented: $isShowChatRoom, action: {
                onBack()
                
                vm.isShowReaders = false

                if isPlayAudio {
                    stopPlayAudio()
                }

                if isPlayVideo {
                    isPlayVideo = false
                }

                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.5, execute: {
                    if vm.isShowCamera {
                        vm.onHideCamera()
                    }

                    if vm.isShowVoiceRecorder {
                        vm.onHideVoiceRecorder(deleteFile: true)
                    }

                    if vm.isShowImagePicker {
                        vm.isShowImagePicker = false
                    }
                })
            })
            
            /// Display over mainview
            // Camera
            if vm.isShowCamera {
                CameraUIView(
                    controller: vm.camera,
                    cameraMode: $vm.cameraMode,
                    flashMode: $vm.flashMode,
                    onHide: vm.onHideCamera,
                    onClickFlash: {
                        withAnimation {
                            switch vm.flashMode {
                            case .On:
                                vm.flashMode = .Off
                            case .Off:
                                vm.flashMode = .Auto
                            case .Auto:
                                vm.flashMode = .On
                            case .NotAvailable:
                                break
                            }
                        }
                    },
                    onSwitchMode: { cMode in
                        withAnimation {
                            if cMode == .VideoNonRecording {
                                Task {
                                    _ = await vm.microphone.checkAuthorization()
                                }
                            }
                            vm.cameraMode = cMode
                        }
                    },
                    onCapture: {
                        withAnimation {
                            switch vm.cameraMode {
                            case .Photo:
                                vm.camera.takePhoto(flashMode: vm.flashMode)
                            case .VideoNonRecording:
                                vm.cameraMode = .VideoRecording
                                vm.camera.startVideoRecorder(url: vm.pending(ext: ".mp4"))
                            case .VideoRecording:
                                vm.cameraMode = .VideoNonRecording
                                vm.camera.stopVideoRecorder()
                            }
                        }
                    }
                )
            }
            
            // Microphone
            if vm.isShowVoiceRecorder {
                RecorderUIView(
                    recordingMode: $vm.recordingMode,
                    basedSystemTimeMillis: $vm.timeStartRecord,
                    onMicrophone: {
                        Task {
                            let isAuthorized = await vm.microphone.checkAuthorization()
                            
                            if isAuthorized {
                                switch vm.recordingMode {
                                case .Recording:
                                    vm.recordingMode = .FinishedRecording
                                    vm.microphone.stopRecording()
                                default:
                                    _ = vm.currentRecordedURL?.delete()
                                    vm.recordingMode = .Recording
                                    vm.timeStartRecord = currentTimeMillis()
                                    vm.microphone.startRecording(url: vm.pending(ext: ".m4a"))
                                }
                            } else {
                                homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.app_permission_settings_audio_record, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok, rightBtnClick: {
                                    UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
                                }))
                            }
                        }
                    },
                    onHide: {
                        vm.onHideVoiceRecorder(deleteFile: true)
                        stopPlayAudio()
                    },
                    onSend: {
                        if let uri = vm.currentRecordedURL {
                            vm.sendAudio(roomId: vm.roomSource, uri: uri, ext: ".m4a")
                            vm.onHideVoiceRecorder(deleteFile: false)
                        }
                        
                        stopPlayAudio()
                    },
                    onPlay: {
                        if let uri = vm.currentRecordedURL {
                            vm.playerAudio.player = AVPlayer(url: uri)
                            isPlayAudio = true
                            vm.playerAudio.player?.play()
                        }
                    })
            }
            
            // Menu Item
            if vm.isShowMenu && vm.longClickItem?.longClickMenu != nil {
                VStack {
                    ForEach(vm.longClickItem!.longClickMenu, id: \.self) { item in
                        Button(action: {
                            switch item {
                            case .Copy:
                                UIPasteboard.general.setValue((vm.longClickItem!.bound as! TextProtocol).text.orEmpty(),
                                            forPasteboardType: kUTTypePlainText as String)
                                
                                isShowPopupCopied = true
                                
                            case .Delete:
                                homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: String(), message: Resources.alert_delete_message_msg, isShowLeftBtn: true, leftBtnText: Resources.cancel, isShowRightBtn: true, rightBtnText: Resources.delete, rightBtnClick: {
                                    vm.deleteMessage(vm.longClickItem!.bound)
                                }))
                            case .Detail:
                                homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.msg_details, message: Resources.msg_options_sent_time.replacingOccurrences(of: "%s", with: Config.Shared.dateFormatterForChat.format(date: vm.longClickItem!.bound.sentDate)), isShowLeftBtn: false, leftBtnText: Resources.strEmpty, isShowRightBtn: false, rightBtnText: Resources.strEmpty))
                            case .Share:
                                isShowShareSheet = true
                            }
                            
                            withAnimation(.linear(duration: 0.2)) {
                                vm.isShowMenu = false
                            }
                        }, label: {
                            switch item {
                            case .Copy:
                                Text(Resources.msg_options_copy)
                                    .foregroundColor(.black)
                                    .frame(height: 30)
                                    .background(Color.white)
                                    .frame(width: 150, alignment: .leading)
                            case .Delete:
                                Text(Resources.delete)
                                    .foregroundColor(.black)
                                    .frame(height: 30)
                                    .background(Color.white)
                                    .frame(width: 150, alignment: .leading)
                            case .Detail:
                                Text(Resources.msg_options_details)
                                    .foregroundColor(.black)
                                    .frame(height: 30)
                                    .background(Color.white)
                                    .frame(width: 150, alignment: .leading)
                            case .Share:
                                Text(Resources.share)
                                    .foregroundColor(.black)
                                    .frame(height: 30)
                                    .background(Color.white)
                                    .frame(width: 150, alignment: .leading)
                            }
                        })
                    }
                }
                .padding()
                .background(Color.white)
                .cornerRadius(CGFloat(8))
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.white.opacity(0.01))
                .onTapGesture {
                    withAnimation(.linear(duration: 0.2)) {
                        vm.isShowMenu = false
                    }
                }
                .transition(.move(edge: .bottom))
            }
            
            // Full Image
            ScrollViewWithMaxHeight(isPresented: $vm.isShowImageFull, image: vm.imageFull)
            
            // Image Picker
            if vm.isShowImagePicker {
                GalleryUIView(onFinish: { imgs in
                    for img in imgs {
                        img.getURL(completionHandler: { url in
                            if let url = url {
                                debugPrint(url.startAccessingSecurityScopedResource())
                                if FileManager.default.fileExists(atPath: url.path) {
                                    vm.sendImage(roomId: vm.roomSource, uris: [url])
                                }
                            }
                        })
                    }
                    
                    withAnimation {
                        vm.isShowImagePicker.toggle()
                    }
                }, onCancel: {
                    withAnimation {
                        vm.isShowImagePicker.toggle()
                    }
                })
            }
        }
        .popup(isPresented: $isPlayAudio, position: .bottom, closeOnTap: false, dismissCallback: {
            vm.playerAudio.player?.pause()
        },view: {
            VideoPlayerView(player: vm.playerAudio, onEnd: {})
                .cornerRadius(CGFloat(8))
                .frame(maxHeight: 145)
                .padding(.horizontal)
                .overlay(
                    Image(systemName: "xmark")
                        .font(.system(size: 22))
                        .foregroundColor(.white)
                        .padding(10)
                        .onTapGesture {
                            stopPlayAudio()
                        }
                        .frame(width: 100, alignment: .leading)
                        .background(Color.black)
                        .cornerRadius(CGFloat(8))
                        .padding(.horizontal, 16),
                    alignment: .topLeading)
        })
        .popup(isPresented: $isPlayVideo, position: .bottom, closeOnTap: false, dismissCallback: {
            vm.playerVideo.player?.pause()
        },view: {
            VideoPlayerView(player: vm.playerVideo, onEnd: { isPlayVideo = false })
                .cornerRadius(CGFloat(8))
        })
        .popup(isPresented: $isShowPopupCopied, type: .toast, position: .bottom, animation: .easeInOut, autohideIn: 2, closeOnTap: true, closeOnTapOutside: true, view: {
             VStack {
                 Text(Resources.msg_options_copied)
                    .foregroundColor(.white)
                    .font(.system(size: 15))
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    .padding(.leading, 10)
                    .background(Color(Resources.textDark))
                    .cornerRadius(5)
                    .padding(EdgeInsets(top: 0, leading: 5, bottom: 5, trailing: 5))
            }
             .frame(maxWidth: .infinity, maxHeight: 55)
        })
        .sheet(isPresented: $vm.isShowDocumentPicker) {
            DocumentPicker(isPresented: $vm.isShowDocumentPicker) { sel in
                debugPrint(sel.startAccessingSecurityScopedResource())
                vm.sendFile(roomId: vm.roomSource, uri: sel)
            }
        }
        .sheet(isPresented: $isShowShareSheet) {
            if let it = vm.longClickItem {
                if let dl = it.bound as? DlProtocol {
                    if let uri = dl.attachmentExt()?.contentUri() {
                        ShareSheet(activityItems: [uri])
                    }
                }
            }
        }
        .overlay(
            GeometryReader { geo in
                if vm.isShowReaders {
                    ScrollView {
                        VStack(alignment: .leading, spacing: 16) {
                            ForEach(readers, id: \.self) { reader in
                                Text(reader)
                                    .foregroundColor(Color(Resources.textColor))
                                    .font(.system(size: 14))
                            }
                        }
                        .padding(16)
                        .background(GeometryReader { bgGeo in
                            Color.clear.onAppear() {
                                contentWidth = bgGeo.size.width
                                contentHeight = bgGeo.size.height
                            }
                        })
                    }
                    .background(Color(Resources.grayBackground))
                    .cornerRadius(CGFloat(6))
                    .shadow(color: .gray, radius: 0.15)
                    .frame(height: contentHeight)
                    .offset(x: x - arrowHeight, y: y)
                    .overlay(arrowView)
                }
            }
        )
    }
}

struct ShareSheet: UIViewControllerRepresentable {
    typealias Callback = (_ activityType: UIActivity.ActivityType?, _ completed: Bool, _ returnedItems: [Any]?, _ error: Error?) -> Void
    
    let activityItems: [Any]
    let applicationActivities: [UIActivity]? = nil
    let excludedActivityTypes: [UIActivity.ActivityType]? = nil
    let callback: Callback? = nil
    
    func makeUIViewController(context: Context) -> UIActivityViewController {
        let controller = UIActivityViewController(
            activityItems: activityItems,
            applicationActivities: applicationActivities)
        controller.excludedActivityTypes = excludedActivityTypes
        controller.completionWithItemsHandler = callback
        return controller
    }
    
    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {
        // nothing to do here
    }
}

