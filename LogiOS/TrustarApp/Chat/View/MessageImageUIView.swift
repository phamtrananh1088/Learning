//
//  MessageImageUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

class MessageImageUIViewModel: AbstractMessageUIViewModel, ObservableObject {
    
    /// Current user: True
    /// Another user: False
    var rtl: Bool
    
    @Published var imageView: Result<Image> = .Loading
    var loadingState: LoadingStateEnum = .Error
    var uiImage: UIImage?
    
    init(rtl: Bool, bound: MessageItemChat) {
        self.rtl = rtl
        
        super.init(bound: bound)
    }
    
    func loadImg(uiImage: UIImage) {
        let maxS = max(uiImage.size.width, uiImage.size.height)
        var scale: CGFloat = 1
        if maxS > 192 {
            scale = 192 / maxS
        }
        
        DispatchQueue.main.async {
            withAnimation {
                self.imageView = .Value(value: Image(uiImage: uiImage.resize(scale: scale)))
                self.uiImage = uiImage
                self.loadingState = .Ok
            }
        }
    }
}

struct MessageImageUIView: View {
    
    @ObservedObject var vm: MessageImageUIViewModel
    var onTapGesture: () -> Void
    var onShowHideReaders: (GeometryProxy, MessageItemChat.Sent) -> Void
    
    private var gd: Color {
        if vm.rtl {
            return Color(Resources.colorPrimary)
        }
        
        return Color(Resources.grayBackground)
    }
    
    var max: CGFloat = 192
    
    private var widthImage: CGFloat {
        if (vm.bound as! ImageProtocol).ratio >= 1 {
            return max
        }
        
        return 0
    }
    
    private var heightImage: CGFloat {
        if (vm.bound as! ImageProtocol).ratio >= 1 {
            return 0
        }
        
        return max
    }
    
    var body: some View {
        if vm.rtl {
            HStack {
                VStack {
                    HStack {
                        MessageSelfStatusView(vm: vm, onShowHideReaders: onShowHideReaders)
                        
                        switch vm.imageView {
                        case .Loading:
                            Image(Resources.round_photo_24)
                                .scaledToFit()
                                .frame(width: max, height: max)
                        case .Value(value: let img):
                            if let image = img {
                                image
                                    .resizable()
                                    .scaledToFit()
                                    .cornerRadius(CGFloat(8))
                                    .fixedSize()
                                    .onTapGesture {
                                        onTapGesture()
                                    }
                            }
                        default:
                            Image(Resources.round_broken_image_24)
                                .scaledToFit()
                                .frame(width: max, height: max)
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .trailing)
                    
                }.frame(alignment: .leading)
            }
            .frame(maxWidth: .infinity, alignment: .topTrailing)
            .padding(.horizontal)
            .if (!vm.bound.hasNext) {
                $0.padding(.bottom, 5)
            }
        } else {
            HStack {
                if (vm.isShowAvatar) {
                    Image(vm.avatar)
                        .resizable()
                        .scaledToFill()
                        .frame(width: 48, height: 48)
                        .frame(maxHeight: .infinity, alignment: .top)
                        .padding(.top, 15)
                }
                
                VStack {
                    if vm.isShowUserName {
                        Text(vm.username)
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(Color(Resources.textColor))
                            .padding(.horizontal, 8)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    
                    HStack {
                        switch vm.imageView {
                        case .Loading:
                            Image(Resources.round_photo_24)
                                .scaledToFit()
                                .frame(width: max, height: max)
                        case .Value(value: let img):
                            if let image = img {
                                image
                                    .resizable()
                                    .scaledToFit()
                                    .cornerRadius(CGFloat(8))
                                    .fixedSize()
                                    .onTapGesture {
                                        onTapGesture()
                                    }
                            }
                        default:
                            Image(Resources.round_broken_image_24)
                                .scaledToFit()
                                .frame(width: max, height: max)
                        }
                        
                        Text(vm.sentTimeTextView)
                            .foregroundColor(.black)
                            .font(.system(size: 12))
                            .frame(maxHeight: .infinity, alignment: .bottomLeading)
                            .padding(2)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    
                }
                .frame(alignment: .leading)
                .if(!vm.isShowAvatar) {
                    $0.padding(.leading, 53)
                }
            }
            .frame(maxWidth: .infinity, alignment: .topLeading)
            .padding(.horizontal)
            .if (!vm.bound.hasNext) {
                $0.padding(.bottom, 5)
            }
        }
    }
}

//struct MessageImageUIView_Previews: PreviewProvider {
//    static var previews: some View {
//        ScrollView {
//            VStack(spacing: 5) {
//                MessageImageUIView(vm: MessageImageUIViewModel(rtl: false, bound: MessageItemChat.Sent.ImgSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "1234", text: nil, createdDate: 1231241, messageRow: "1", type: MessageTypeEnum.TYPE_IMG.rawValue), user: ChatUser(id: "1", name: "cuong nguyen", mail: "", avatar: ""), readers: nil, hasPrev: false, hasNext: false)), onPlay: {_ in})
//
//                MessageImageUIView(vm: MessageImageUIViewModel(rtl: false, bound: MessageItemChat.Sent.ImgSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "1234", text: nil, createdDate: 1231241, messageRow: "1", type: MessageTypeEnum.TYPE_IMG.rawValue), user: ChatUser(id: "1", name: "DionSoft", mail: "", avatar: ""), readers: nil, hasPrev: false, hasNext: false)), onPlay: {_ in})
//
//                MessageImageUIView(vm: MessageImageUIViewModel(rtl: false, bound: MessageItemChat.Sent.ImgSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "1234", text: nil, createdDate: 1231241, messageRow: "1", type: MessageTypeEnum.TYPE_IMG.rawValue), user: ChatUser(id: "1", name: "TokeiTcc", mail: "", avatar: ""), readers: nil, hasPrev: false, hasNext: false)), onPlay: {_ in})
//
//                MessageImageUIView(vm: MessageImageUIViewModel(rtl: true, bound: MessageItemChat.Sent.ImgSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "1234", text: nil, createdDate: 1231241, messageRow: "1", type: MessageTypeEnum.TYPE_IMG.rawValue), user: ChatUser(id: "1", name: "TokeiTcc", mail: "", avatar: ""), readers: nil, hasPrev: false, hasNext: false)), onPlay: {_ in})
//
//            }
//        }
//    }
//}
