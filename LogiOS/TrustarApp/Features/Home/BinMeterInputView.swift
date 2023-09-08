//
//  BinMeterInputView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct BinMeterInputView: View {
    @Binding var isPresented: Bool
    @ObservedObject var binMeterInputVm: BinMeterInputViewModel
    
    private var isEnableConfirm: Bool {
        if let _ = Int(binMeterInputVm.input) {
            if binMeterInputVm.input.count <= 10 {
                return true
            }
            
            return false
        }
        
        return false
    }
    
    var body: some View {
        ZStack {
            if isPresented {
                Color.black.opacity(isPresented ? 0.5 : 0).edgesIgnoringSafeArea(.all)
            }
        }
        .onTapGesture {
            self.endTextEditing()
            withAnimation(.linear(duration: 0.3)) {
                isPresented = false
            }
        }
        .overlay(
            VStack {
                VStack {
                    Text(binMeterInputVm.title1)
                        .foregroundColor(.black)
                        .padding(8)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    Text(binMeterInputVm.title2)
                        .foregroundColor(.black)
                        .padding(8)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    TextField(Resources.strEmpty, text: $binMeterInputVm.input)
                        .textContentType(.telephoneNumber)
                        .keyboardType(.numberPad)
                        .foregroundColor(.black)
                        .accentColor(Color(Resources.colorPrimary))
                        .background(Color.white)
                        .padding(.horizontal, 4)
                        .padding(.vertical, 10)
                        .overlay(
                            RoundedRectangle(cornerRadius: 2)
                                .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
                        )
                        .padding(.horizontal, 8)

                    HStack {
                        Button(action: {
                            self.endTextEditing()
                            binMeterInputVm.cancelClick!()
                        }, label: {
                            Text(binMeterInputVm.cancelText)
                                .frame(maxWidth: 130, alignment: .center)
                        })
                            .buttonStyle(PrimaryButtonStyle())
                            .frame(maxWidth: .infinity, alignment: .center)

                        Button(action: {
                            self.endTextEditing()
                            binMeterInputVm.confirmClick!(binMeterInputVm.input)
                        }, label: {
                            Text(binMeterInputVm.confirmText)
                                .frame(maxWidth: 130, alignment: .center)
                        })
                            .buttonStyle(PrimaryButtonStyle())
                            .frame(maxWidth: .infinity, alignment: .center)
                            .disabled(!isEnableConfirm)
                    }
                    .padding(.top, 32)
                    .padding(.bottom, 16)
                }
                .padding(8)
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .onTapGesture {
                    self.endTextEditing()
                }
            },
            alignment: .bottom)
            .frame(maxHeight: .infinity, alignment: .bottom)
            .adaptsToKeyboard()
            .edgesIgnoringSafeArea(.all)
            .offset(y: self.isPresented ? 0 : UIScreen.main.bounds.height)
            .animation(.default)
            
    }
}
