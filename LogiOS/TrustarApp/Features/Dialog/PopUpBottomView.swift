//
//  PopUpWindow.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/18.
//

import SwiftUI

struct PopUpBottomView: View {
    @ObservedObject var popupVm: PopupViewModel
    @EnvironmentObject var modelRotation: ModelRotation

    @Binding var show: Bool
    
    private let textFontSize: CGFloat = 14
    var body: some View {
        GeometryReader { gp in
            ZStack(alignment: .bottom) {
                if show {
                    // PopUp background color
                    Color.black.opacity(show ? 0.8 : 0).edgesIgnoringSafeArea(.all)
                        .onTapGesture {
                            // Dismiss the PopUp
                            withAnimation(.linear(duration: 0.3)) {
                                if popupVm.autoClose {
                                    show = false
                                }
                                if popupVm.leftBtnClick != nil {
                                    popupVm.leftBtnClick!()
                                }
                            }
                        }
                    
                    VStack {
                        ScrollView {
                            VStack(alignment: .trailing, spacing: 10) {
                                if(popupVm.isShowTitle) {
                                    Text(popupVm.title)
                                        .font(Font.system(size: textFontSize))
                                        .foregroundColor(Color(Resources.colorPrimary))
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                        .padding(.horizontal)
                                }
                                
                                Text(popupVm.message)
                                    .font(Font.system(size: textFontSize))
                                    .foregroundColor(Color(Resources.textColor))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.horizontal)
                                
                               
                            }
                            .background(Color(red: 33, green: 33, blue: 33))
                        }
                    }
                    .frame(width: gp.size.width, height: 400)
                    .background(Color(red: 33, green: 33, blue: 33))
                    .offset(y: -50)
                    
                    VStack {
                        Divider()
                        
                        HStack {
                            Button(action: {
                                show = false
                            }, label: {
                                Text(Resources.close)
                                    .frame(width: gp.size.width, height: 35)
                                    .cornerRadius(10)
                                    .background(Color(red: 44, green: 151, blue: 215))
                                    .padding([.leading, .trailing], 20)
                            
                            })
                            .frame(width: gp.size.width, height: 40)
                            .background(Color(red: 48, green: 165, blue: 230))
                            .cornerRadius(10)
                        }
                    }
                    .frame(width: gp.size.width, height: 50)
                     .background(Color(red: 33, green: 33, blue: 33))
                    
                }
            }
        }
        .animation(.linear(duration: 0.3))
        
    }
}

