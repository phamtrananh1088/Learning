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
                            }
                        }
                    
                    VStack {
                        ScrollView {
                            VStack(alignment: .trailing, spacing: 10) {

                                Text(popupVm.message)
                                    .font(Font.system(size: textFontSize))
                                    .foregroundColor(Color(Resources.textColor))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.horizontal)
                                
                               
                            }
                            //.background(Color(red: 33, green: 33, blue: 33))
                        }
                    }
                    .background(Color(Resources.grayBackground))
                    .frame(width: gp.size.width, height: 400)
                    
                    .offset(y: -50)
                    
                    VStack(alignment: .center) {
                        Divider()
                        
                     
                            Button(action: {
                                show = false
                            }, label: {
                                Text(Resources.close).foregroundColor(Color(Resources.lightText))
                                    .frame(width: gp.size.width - 40, height: 35)
                                    .cornerRadius(10)
                                    .background(Color(Resources.colorPrimaryDark))
                            
                            })
                            .frame(width: gp.size.width - 40, height: 40)
                            .background(Color(Resources.colorPrimary))
                            .cornerRadius(10)
                       
                    }
                    .frame(width: gp.size.width, height: 50)
                    
                }
            }
        }
        .animation(.linear(duration: 0.3))
        
    }
}

