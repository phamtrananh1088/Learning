//
//  ChatItemUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/22.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct ChatItemUIView: View {
    var vm: ChatVM
    var item: AbstractMessageUIViewModel
    var isNewest: Bool = false
    var isOldest: Bool = false
    var onShowMenu: () -> Void
    var onTapGesture: () -> Void
    var onShowHideReaders: (GeometryProxy, MessageItemChat.Sent) -> Void
    var body: some View {
        HStack {
            if item is MessageTextUIViewModel {
                MessageTextUIView(vm: item as! MessageTextUIViewModel, onTapGesture: onTapGesture, onShowHideReaders: onShowHideReaders)
            } else if item is MessageImageUIViewModel {
                MessageImageUIView(vm: item as! MessageImageUIViewModel, onTapGesture: onTapGesture, onShowHideReaders: onShowHideReaders)
            } else if item is MessageVideoUIViewModel {
                MessageVideoUIView(vm: item as! MessageVideoUIViewModel, onTapGesture: onTapGesture, onShowHideReaders: onShowHideReaders)
            } else if item is MessageAudioUIViewModel {
                MessageAudioUIView(vm: item as! MessageAudioUIViewModel, onTapGesture: onTapGesture, onShowHideReaders: onShowHideReaders)
            } else if item is MessageFileUIViewModel {
                MessageFileUIView(vm: item as! MessageFileUIViewModel, onTapGesture: onTapGesture, onShowHideReaders: onShowHideReaders)
            }
        }
        .onAppear() {
            if item is MessageImageUIViewModel {
                if (item as! MessageImageUIViewModel).loadingState != .Error { return }
                
                (item as! MessageImageUIViewModel).loadingState = .Loading
                
                if let url = (item.bound as! ImageProtocol).imageUri?.uri {
                    if item.bound is MessageItemChat.Pending {
                        if let urlImgLocal = (item.bound as! MessageItemChat.Pending).attachmentExt()?.contentUri() {
                            if let uiImage = UIImage(contentsOfFile: urlImgLocal.path) {
                                (item as! MessageImageUIViewModel).loadImg(uiImage: uiImage)
                            }
                        }
                    } else {
                        Current.Shared.chatFileApi.imageCached(url: url)
                            .retry(2)
                            .receive(on: DispatchQueue.main)
                            .sink(receiveCompletion: { completion in
                                switch completion {
                                    
                                case .finished:
                                    break
                                case .failure(_):
                                    (item as! MessageImageUIViewModel).loadingState = .Error
                                }
                            },
                                  receiveValue: { data in
                                DispatchQueue.main.async {
                                    if let uiImage = UIImage(data: data) {
                                        (item as! MessageImageUIViewModel).loadImg(uiImage: uiImage)
                                    } else {
                                        (item as! MessageImageUIViewModel).imageView = .Error(error: NetworkError.networkError(reason: ""))
                                    }
                                }
                            }).store(in: &item.bag)
                    }
                }
            }
        }
        .if(isOldest) {
            $0.onAppear() {
                DispatchQueue.global().async {
                    vm.onPrepend()
                }
            }
        }
        .onTapGesture { }
        .onLongPressGesture(minimumDuration: 0.3, maximumDistance: 0.3) {
            vm.isShowReaders = false
            onShowMenu()
        }
    }
}
