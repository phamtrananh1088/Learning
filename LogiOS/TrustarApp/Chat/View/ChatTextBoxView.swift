//
//  ChatTextBoxView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/22.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct ChatTextBoxView: View {
    @Binding var chatContent: String
    var body: some View {
        if #available(iOS 14.0, *) {
            TextEditorCustomView(string: $chatContent,
                                 maxLength: 100)
            .lineLimit(4)
            .foregroundColor(.black)
            .cornerRadius(CGFloat(8))
            .padding(.horizontal, 10)
        } else {
            TSUITextField(label: "", text: $chatContent,
                          keyboardType: .default,
                          tag:0,
                          onCommit: {_ in },
                          maxLength: 100,
                          textAlignment: .left)
                .foregroundColor(.black)
                .frame(height: 20)
                .cornerRadius(CGFloat(8))
                .padding(.horizontal, 10)
                .overlay(
                    RoundedRectangle(cornerRadius: 2)
                        .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
                )
        }
    }
}
