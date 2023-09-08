//
//  TextEditorCustomView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 31/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine

@available(iOS 14.0, *)
struct TextEditorCustomView: View {
    
    @Binding var string: String
    @State var maxLength: Int? = nil
    @State var textEditorHeight : CGFloat = 20
    
    var body: some View {
        
        ZStack(alignment: .leading) {
            Text(string)
                .font(.system(size: 16))
                .foregroundColor(.clear)
                .padding(10)
                .background(GeometryReader {
                    Color.clear.preference(key: ViewHeightKey.self,
                                           value: $0.frame(in: .local).size.height)
                })
            
            TextEditor(text: $string)
                .if (maxLength != nil) {
                    $0.onReceive(Just(string)) { _ in
                        if string.count > maxLength! {
                            string = String(string.prefix(maxLength!))
                        }
                    }
                }
                .font(.system(size: 16))
//                .font(.system(.body))
                .frame(maxHeight: max(40,textEditorHeight))
                .background(Color.white)
                .cornerRadius(1.0)
                            .shadow(radius: 1.0)
        }.onPreferenceChange(ViewHeightKey.self) { textEditorHeight = $0 }
        .onAppear(perform: {
            UITextView.appearance().backgroundColor = .clear
        })
        .introspectTextView(customize: { textEditor in
            textEditor.becomeFirstResponder()
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.35, execute: {
                if textEditor.text != nil && !textEditor.text!.isEmpty && textEditor.text != " " {
                    textEditor.selectedTextRange = textEditor.textRange(from: textEditor.beginningOfDocument, to: textEditor.endOfDocument)
                }
            })
        })
    }
    
}


struct ViewHeightKey: PreferenceKey {
    static var defaultValue: CGFloat { 0 }
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value = value + nextValue()
    }
}
