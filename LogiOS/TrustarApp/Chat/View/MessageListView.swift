//
//  MessageListView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import AVKit

struct MessageListView: View {
    @ObservedObject var vm: ChatVM
    @Binding var isPlayAudio: Bool
    @Binding var isPlayVideo: Bool
    var onShowMenu: (AbstractMessageUIViewModel) -> Void
    var onShowHideReaders: (GeometryProxy, MessageItemChat.Sent) -> Void
    private let coordinateSpaceName = "MessageCoordinateSpace"
    
    private var isError: Bool {
        switch vm.chatLiveState {
        case .Error:
            return true
        default:
            return false
        }
    }
    
    var body: some View {
        List {
            ForEach(vm.chatLiveData, id: \.self) { item in
                ChatItemUIView(
                    vm: vm,
                    item: item,
                    isNewest: item.uuid == vm.chatLiveData.first!.uuid,
                    isOldest:  item.uuid == vm.chatLiveData.last!.uuid,
                    onShowMenu: {
                        onShowMenu(item)
                        self.endTextEditing()
                    },
                    onTapGesture: { onTapGesture(item: item) },
                    onShowHideReaders: onShowHideReaders)
                .listRowBackground(Color.clear)
                .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 5, trailing: 0))
                .modify {
                    if #available(iOS 15.0, *) {
                        $0.listRowSeparator(.hidden)
                    } else {
                        $0.introspectTableView(customize: { tb in
                            tb.separatorStyle = .none
                            tb.separatorColor = .clear
                            tb.tableFooterView = UIView()
                        })
                    }
                }
                .id(item.uuid)
                .flip()
            }
            .background(GeometryReader {
                Color.clear.preference(key: ViewOffsetKey.self,
                    value: -$0.frame(in: .named(coordinateSpaceName)).origin.y)
            })
            .onPreferenceChange(ViewOffsetKey.self) { bound in
                if vm.isShowReaders {
                    vm.isShowReaders = false
                }
            }
            
            if isError {
                Text(Resources.chat_retry)
                    .multilineTextAlignment(.center)
                    .frame(maxWidth: .infinity, alignment: .center)
                    .font(.system(size: 14))
                    .padding(16)
                    .listRowBackground(Color(Resources.defaultBackground))
                    .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 5, trailing: 0))
                    .modify {
                        if #available(iOS 15.0, *) {
                            $0.listRowSeparator(.hidden)
                        } else {
                            $0.introspectTableView(customize: { tb in
                                tb.separatorStyle = .none
                                tb.separatorColor = .clear
                                tb.tableFooterView = UIView()
                            })
                        }
                    }
                    .onTapGesture {
                        switch vm.loadType {
                        case .REFRESH(key: _):
                            vm.onRefresh()
                        case .PREPEND(key: _):
                            vm.onPrepend()
                        case .APPEND(key: _):
                            vm.onAppend()
                        }
                    }
                    .flip()
            }
        }
        .listStyle(PlainListStyle())
        .flip()
        .onAppear() {
            DispatchQueue.main.async {
                if vm.chatLiveData.isEmpty {
                    vm.onPrepend()
                }
            }
            
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.5, execute: {
                Helper.Shared.sendBroadcastNotification(.Chat)
                
                vm.resetAllErrStatus(roomId: vm.roomSource)
            })
        }
        .onTapGesture {
            vm.isShowReaders = false
        }
        .coordinateSpace(name: coordinateSpaceName)
    }
    
    private func onTapGesture(item: AbstractMessageUIViewModel) {
        self.endTextEditing()
        // Audio
        if item is MessageAudioUIViewModel {
            let vm = item as! MessageAudioUIViewModel
            
            let playOrDownLoad = {
                if vm.isReady {
                    play(data: vm.pathFile!, type: .TYPE_AUDIO)
                } else {
                    vm.download()
                }
            }
            
            if vm.bound.selfField {
                if vm.bound is MessageItemChat.Pending {
                    if (vm.bound as! MessageItemChat.Pending).isSending() {
                        play(data: vm.pathFile!, type: .TYPE_AUDIO)
                    } else {
                        retry(msg: vm.bound)
                    }
                } else {
                    playOrDownLoad()
                }
            } else {
                playOrDownLoad()
            }
        }
        
        // File
        if item is MessageFileUIViewModel {
            let vm = item as! MessageFileUIViewModel
            let presentOrDownload = {
                if vm.isReady {
                    vm.isPresented = true
                } else {
                    vm.download()
                }
            }
            
            if vm.bound.selfField {
                if vm.bound is MessageItemChat.Pending {
                    if (vm.bound as! MessageItemChat.Pending).isSending() {
                        vm.isPresented = true
                    } else {
                        retry(msg: vm.bound)
                    }
                } else {
                    presentOrDownload()
                }
            } else {
                presentOrDownload()
            }
        }
        
        // Image
        if item is MessageImageUIViewModel {
            let vm = item as! MessageImageUIViewModel
            if vm.bound.selfField {
                if vm.bound is MessageItemChat.Pending {
                    if (vm.bound as! MessageItemChat.Pending).isSending() {
                        play(data: vm.uiImage!, type: .TYPE_IMG)
                    } else {
                        retry(msg: vm.bound)
                    }
                } else {
                    play(data: vm.uiImage!, type: .TYPE_IMG)
                }
            } else {
                play(data: vm.uiImage!, type: .TYPE_IMG)
            }
        }
        
        // Text
        if item is MessageTextUIViewModel {
            let vm = item as! MessageTextUIViewModel
            if vm.bound.selfField {
                if vm.bound is MessageItemChat.Pending {
                    retry(msg: vm.bound)
                }
            }
        }
        
        // Video
        if item is MessageVideoUIViewModel {
            let vm = item as! MessageVideoUIViewModel
            let playOrDownload = {
                if vm.previewImage != nil {
                    play(data: vm.pathFile!, type: .TYPE_VIDEO)
                } else {
                    vm.download()
                }
            }
            if vm.bound.selfField {
                if vm.bound is MessageItemChat.Pending {
                    if (vm.bound as! MessageItemChat.Pending).isSending() {
                        play(data: vm.pathFile!, type: .TYPE_VIDEO)
                    } else {
                        retry(msg: vm.bound)
                    }
                } else {
                    playOrDownload()
                }
            } else {
                playOrDownload()
            }
        }
    }
    
    private func retry(msg: MessageItemChat) {
        if let pending = (msg as? MessageItemChat.Pending)?.pending {
            vm.resetErrStatus(chatMessagePending: pending)
        }
    }
    
    private func play(data: Any, type: MessageTypeEnum) {
        switch type {
            
        case .TYPE_TEXT:
            break
        case .TYPE_IMG:
            if data is UIImage {
                vm.imageFull = data as? UIImage
                withAnimation {
                    vm.isShowImageFull = true
                }
            }
            
        case .TYPE_AUDIO:
            if data is URL {
                vm.playerAudio.player = AVPlayer(url: data as! URL)
                isPlayAudio = true
                vm.playerAudio.player?.play()
            }
            
        case .TYPE_VIDEO:
            if data is URL {
                vm.playerVideo.player = AVPlayer(url: data as! URL)
                isPlayVideo = true
                vm.playerVideo.player?.play()
            }
            
        case .TYPE_FILE:
            break
        }
    }
}
