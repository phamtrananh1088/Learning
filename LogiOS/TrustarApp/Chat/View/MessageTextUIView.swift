//
//  MessageTextUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine
import UIKit

class MessageTextUIViewModel: AbstractMessageUIViewModel, ObservableObject {
    
    /// Current user: True
    /// Another user: False
    var rtl: Bool
    
    var text: String
    
    @available(iOS 15.0, *)
    fileprivate var textMarkDown: AttributedString {
        var markDown: AttributedString = AttributedString(text)
        var tempTxt = text
        
        let detector = try? NSDataDetector(types: NSTextCheckingResult.CheckingType.allTypes.rawValue)
        let matches = detector?.matches(in: text, range: NSRange(text.startIndex..., in: text))

        var lstMatches: [String] = []
        if matches != nil {
            for match in matches! {
                if match.resultType == .phoneNumber, let number = match.phoneNumber {
                    tempTxt = tempTxt.replacingOccurrences(of: number, with: "[\(number)](tel:\(number))")
                    lstMatches.append(number)
                }
                
                if match.resultType == .link, let link = match.url {
                    let str = tempTxt[match.range.lowerBound ..< match.range.upperBound]
                    if link.absoluteString.contains("http") && !str.contains("http") {
                        tempTxt = tempTxt.replacingOccurrences(of: str, with: "[\(str)](https://\(str))")
                    }

                    lstMatches.append(str)
                }
            }
        }
        
        if let md = try? AttributedString(markdown: tempTxt, options: AttributedString.MarkdownParsingOptions(interpretedSyntax: .inlineOnlyPreservingWhitespace)) {
            markDown = md

            for it in lstMatches {
                if let r = markDown.range(of: it) {
                    markDown[r].underlineStyle = .single
                }
            }
        }
        
        return markDown
    }
    
    init(rtl: Bool, bound: MessageItemChat) {
        self.rtl = rtl
        
        if !(bound is TextProtocol) {
            self.text = ""
        } else {
            self.text = (bound as! TextProtocol).text.orEmpty()
        }
        
        super.init(bound: bound)
    }
}

struct MessageTextUIView: View {
    @ObservedObject var vm: MessageTextUIViewModel
    var onTapGesture: () -> Void
    var onShowHideReaders: (GeometryProxy, MessageItemChat.Sent) -> Void
    
    private var gd: Color {
        if vm.rtl {
            return Color(Resources.colorPrimary)
        }
        
        return Color(Resources.grayBackground)
    }

    var body: some View {
        
        if vm.rtl {
            VStack {
                HStack {
                    MessageSelfStatusView(vm: vm, onShowHideReaders: onShowHideReaders)
                        .modify {
                            if vm.bound.status == nil || vm.bound.status!.isEmpty {
                                $0.frame(width: 35)
                            } else if let sent = vm.bound as? MessageItemChat.Sent {
                                if !sent.readers.isEmpty {
                                    $0.frame(width: 80)
                                }
                            } else if vm.bound is MessageItemChat.Pending {
                                if (vm.bound as! MessageItemChat.Pending).status != Resources.msg_tap_to_retry {
                                    $0.frame(width: 80)
                                } else {
                                    $0.frame(maxWidth: .infinity)
                                }
                            }
                        }
                    
                    if #available(iOS 15.0, *) {
                        Text(vm.textMarkDown)
                            .font(.system(size: 16))
                            .foregroundColor(.white)
                            .padding(10)
                            .background(gd)
                            .multilineTextAlignment(.leading)
                            .tint(.white)
                            .if(vm.bound.hasPrev) {
                                $0.cornerRadius(4, corners: [.topLeft, .topRight])
                            }
                            .if(!vm.bound.hasPrev) {
                                $0.cornerRadius(8, corners: [.topLeft, .topRight])
                            }
                            .if(vm.bound.hasNext) {
                                $0.cornerRadius(4, corners: [.bottomLeft, .bottomRight])
                            }
                            .if(!vm.bound.hasNext) {
                                $0.cornerRadius(8, corners: [.bottomLeft, .bottomRight])
                            }
                            .onTapGesture {
                                onTapGesture()
                            }
                    } else {
                        Text(vm.text)
                            .font(.system(size: 16))
                            .foregroundColor(.white)
                            .padding(10)
                            .background(gd)
                            .multilineTextAlignment(.leading)
                            .if(vm.bound.hasPrev) {
                                $0.cornerRadius(4, corners: [.topLeft, .topRight])
                            }
                            .if(!vm.bound.hasPrev) {
                                $0.cornerRadius(8, corners: [.topLeft, .topRight])
                            }
                            .if(vm.bound.hasNext) {
                                $0.cornerRadius(4, corners: [.bottomLeft, .bottomRight])
                            }
                            .if(!vm.bound.hasNext) {
                                $0.cornerRadius(8, corners: [.bottomLeft, .bottomRight])
                            }
                            .onTapGesture {
                                onTapGesture()
                            }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .trailing)
                
            }.frame(alignment: .leading)
            .frame(maxWidth: .infinity, alignment: .topTrailing)
            .padding(.horizontal)
            .if (!vm.bound.hasNext) {
                $0.padding(.bottom, 5)
            }
        } else {
            HStack(spacing: 5) {
                if (vm.isShowAvatar) {
                    Image(vm.avatar)
                        .resizable()
                        .scaledToFill()
                        .frame(width: 48, height: 48)
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
                        if #available(iOS 15.0, *) {
                            Text(vm.textMarkDown)
                                .font(.system(size: 16))
                                .foregroundColor(Color(Resources.textColor))
                                .padding(10)
                                .background(gd)
                                .multilineTextAlignment(.leading)
                                .tint(Color(Resources.textColor))
                                .if(vm.bound.hasPrev) {
                                    $0.cornerRadius(4, corners: [.topLeft, .topRight])
                                }
                                .if(!vm.bound.hasPrev) {
                                    $0.cornerRadius(8, corners: [.topLeft, .topRight])
                                }
                                .if(vm.bound.hasNext) {
                                    $0.cornerRadius(4, corners: [.bottomLeft, .bottomRight])
                                }
                                .if(!vm.bound.hasNext) {
                                    $0.cornerRadius(8, corners: [.bottomLeft, .bottomRight])
                                }
                                .onTapGesture {
                                    onTapGesture()
                                }
                        } else {
                            Text(vm.text)
                                .font(.system(size: 16))
                                .foregroundColor(Color(Resources.textColor))
                                .padding(10)
                                .background(gd)
                                .multilineTextAlignment(.leading)
                                .if(vm.bound.hasPrev) {
                                    $0.cornerRadius(4, corners: [.topLeft, .topRight])
                                }
                                .if(!vm.bound.hasPrev) {
                                    $0.cornerRadius(8, corners: [.topLeft, .topRight])
                                }
                                .if(vm.bound.hasNext) {
                                    $0.cornerRadius(4, corners: [.bottomLeft, .bottomRight])
                                }
                                .if(!vm.bound.hasNext) {
                                    $0.cornerRadius(8, corners: [.bottomLeft, .bottomRight])
                                }
                                .onTapGesture {
                                    onTapGesture()
                                }
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

//struct MessageTextUIView_Previews: PreviewProvider {
//    static var previews: some View {
//        ScrollView {
//            VStack(spacing: 5) {
//                MessageTextUIView(vm: MessageTextUIViewModel(rtl: false, bound: MessageItemChat.Sent.TextSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "CuongId2", text: "Contest msg", createdDate: 132434, messageRow: "1", type: MessageTypeEnum.TYPE_TEXT.rawValue), user: ChatUser(id: "1", name: "CuongTest_02", mail: "cuongnv@gmail.com", avatar: ""), readers: nil, hasPrev: false, hasNext: true)))
//                
//                MessageTextUIView(vm: MessageTextUIViewModel(rtl: false, bound: MessageItemChat.Sent.TextSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "CuongId2", text: "Content msgContent msgContent msgContent msgContent msgContent msgContent", createdDate: 132434, messageRow: "1", type: MessageTypeEnum.TYPE_TEXT.rawValue), user: ChatUser(id: "1", name: "CuongTest_02", mail: "cuongnv@gmail.com", avatar: ""), readers: nil, hasPrev: false, hasNext: true)))
//                
//                MessageTextUIView(vm: MessageTextUIViewModel(rtl: false, bound: MessageItemChat.Sent.TextSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "CuongId", text: "Content", createdDate: 132434, messageRow: "1", type: MessageTypeEnum.TYPE_TEXT.rawValue), user: ChatUser(id: "1", name: "CuongTest_01", mail: "cuongnv@gmail.com", avatar: ""), readers: nil, hasPrev: false, hasNext: false)))
//                
//                MessageTextUIView(vm: MessageTextUIViewModel(rtl: false, bound: MessageItemChat.Sent.TextSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "CuongId2", text: "Contest msg", createdDate: 132434, messageRow: "1", type: MessageTypeEnum.TYPE_TEXT.rawValue), user: ChatUser(id: "1", name: "CuongTest_03", mail: "cuongnv@gmail.com", avatar: ""), readers: nil, hasPrev: false, hasNext: false)))
//                
//                MessageTextUIView(vm: MessageTextUIViewModel(rtl: false, bound: MessageItemChat.Sent.TextSent(msg: ChatMessage(id: "1", roomId: "1234", userId: "CuongId2", text: "Contest msg", createdDate: 132434, messageRow: "1", type: MessageTypeEnum.TYPE_TEXT.rawValue), user: ChatUser(id: "1", name: "CuongTest_03", mail: "cuongnv@gmail.com", avatar: ""), readers: nil, hasPrev: true, hasNext: false)))
//                
//                MessageTextUIView(vm: MessageTextUIViewModel(rtl: true, bound: MessageItemChat.Pending.TextPending(pending: ChatMessagePending(id: 1, createdDate: 123243243, status: 1, roomId: "1234", userId: "232", text: "aglw awegl ageaw awegaw awgaw laweg laweg gawelgajw awgalwkj awegawe lawegalweja w awegawegj awegaweg lawegaweglja lweg awlegjawlkeg alkwejga weg alwjeg ", targetId: nil, type: MessageTypeEnum.TYPE_TEXT.rawValue, ext: nil), hasPrev: false, hasNext: true)))
//                
//                MessageTextUIView(vm: MessageTextUIViewModel(rtl: true, bound: MessageItemChat.Pending.TextPending(pending: ChatMessagePending(id: 1, createdDate: 123243243, status: 1, roomId: "1234", userId: "232", text: "aglw awegl ageaw awegaw awgaw laweg laweg gawelgajw awgalwkj awegawe lawegalweja w awegawegj awegaweg lawegaweglja lweg awlegjawlkeg alkwejga weg alwjeg ", targetId: nil, type: MessageTypeEnum.TYPE_TEXT.rawValue, ext: nil), hasPrev: true, hasNext: false)))
//                
//            }
//        }
//        
//    }
//}
