//
//  PopUpWindow.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/18.
//

import SwiftUI

struct PopUpWindow: View {
    @ObservedObject var popupVm: PopupViewModel
    @EnvironmentObject var modelRotation: ModelRotation

    @Binding var show: Bool
    
    private let textFontSize: CGFloat = 14
    var body: some View {
        GeometryReader { gp in
            ZStack {
                if show {
                    // PopUp background color
                    Color.black.opacity(show ? 0.5 : 0).edgesIgnoringSafeArea(.all)

                    // PopUp Window
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

                        if popupVm.isShowCheckBox {
                            Button(action: {
                                popupVm.isChecked.toggle()
                            }, label: {
                                HStack {
                                    CheckBox(checked: $popupVm.isChecked)
                                        .frame(width: 20, height: 20)
                                    Text(Resources.tip_checkbox)
                                        .foregroundColor(.black)
                                        .font(.system(size: textFontSize))
                                }
                            })
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.leading, 20)
                        }
                        
                        if popupVm.isShowLeftBtn || popupVm.isShowRightBtn {
                            HStack {
                                // leftBtn
                                if popupVm.isShowLeftBtn
                                {
                                    Button(action: {
                                        // Dismiss the PopUp
                                        withAnimation(.linear(duration: 0.3)) {
                                            if popupVm.autoClose {
                                                show = false
                                            }
                                            if popupVm.leftBtnClick != nil {
                                                popupVm.leftBtnClick!()
                                            }
                                            
                                        }
                                    }, label: {
                                        Text(popupVm.leftBtnText)
                                            .foregroundColor(Color(Resources.colorPrimary))
                                            .font(Font.system(size: textFontSize))
                                    }).buttonStyle(PlainButtonStyle())
                                }
                                
                                // rightBtn
                                if popupVm.isShowRightBtn {
                                    Button(action: {
                                        // Dismiss the PopUp
                                        withAnimation(.linear(duration: 0.3)) {
                                            if popupVm.autoClose {
                                                show = false
                                            }
                                            if popupVm.isShowCheckBox {
                                                if popupVm.rightBtnClickWithCheckBox != nil {
                                                    popupVm.rightBtnClickWithCheckBox!(popupVm.isChecked)
                                                }
                                            } else {
                                                if popupVm.rightBtnClick != nil {
                                                    popupVm.rightBtnClick!()
                                                }
                                            }
                                        }
                                    }, label: {
                                        Text(popupVm.rightBtnText)
                                            .padding(.horizontal)
                                            .foregroundColor(Color(Resources.colorPrimary))
                                            .font(Font.system(size: textFontSize))
                                    }).buttonStyle(PlainButtonStyle())
                                }
                            }
                        }
                    }
                    .padding(.vertical)
                    .frame(maxWidth: .infinity)
                    .background(Color.white)
                    .shadow(radius: 10)
                    .cornerRadius(10)
                    .padding(.horizontal)
                    .padding(.horizontal)
                    //.padding(10)
                    .if(UIDevice.current.orientation.isLandscape) {
                        $0.padding(.horizontal, 100)
                    }
                }
            }
        }.onTapGesture {
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
    }
}

struct PopUpWindow_Previews: PreviewProvider {
    static var previews: some View {
        PopUpWindow(popupVm: PopupViewModel(isShowTitle: true, title: "Error", message: "Opsss!", isShowLeftBtn: true, leftBtnText: "Cancel", isShowRightBtn: true, rightBtnText: "Ok", isShowCheckBox: true), show: .constant(true))
    }
    
    static func doSomething()
    {
        
    }
}
