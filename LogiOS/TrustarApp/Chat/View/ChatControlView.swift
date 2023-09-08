//
//  ChatControlView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/22.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct ChatControlView: View {
    @Binding var chatContent: String
    var onAction: (ActionChatControlEnum) -> Void
    var body: some View {
        HStack(spacing: 10) {
            ChatTextBoxView(chatContent: $chatContent)
                .frame(maxWidth: .infinity)
            
            if chatContent.isEmpty {
                Button(action: {
                    onAction(.Audio)
                }, label: {
                    Image(systemName: "mic.fill")
                        .font(.system(size: 24))
                        .foregroundColor(Color(Resources.textColor))
                        .padding(.horizontal, 2)
                })
                
                Button(action: {
                    onAction(.Image)
                }, label: {
                    Image(systemName: "photo.fill")
                        .font(.system(size: 24))
                        .foregroundColor(Color(Resources.textColor))
                        .padding(.horizontal, 2)
                })
                
                Button(action: {
                    onAction(.Camera)
                }, label: {
                    Image(systemName: "camera.fill")
                        .font(.system(size: 24))
                        .foregroundColor(Color(Resources.textColor))
                        .padding(.horizontal, 2)
                })
                
                Button(action: {
                    onAction(.File)
                }, label: {
                    Image(systemName: "arrow.up.doc.fill")
                        .font(.system(size: 24))
                        .foregroundColor(Color(Resources.textColor))
                        .padding(.horizontal, 2)
                })
            } else {
                Button(action: {
                    onAction(.Send)
                    self.chatContent = ""
                }, label: {
                    Image(systemName: "paperplane.circle.fill")
                        .font(.system(size: 30))
                        .foregroundColor(Color(Resources.colorPrimary))
                        .padding(.horizontal, 2)
                })
            }
        }
        .padding(10)
    }
}
