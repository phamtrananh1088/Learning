//
//  HomeHeaderView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/10.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct HomeHeaderView: View {
    var isShowContent = true
    var isShowToolbar = false
    var lstControlToolbars: [(String, () -> Void)] = []
    var isChatView = false
    @ObservedObject var homeHeaderVm = HomeHeaderViewModel()
    @EnvironmentObject var modelRotation: ModelRotation
    
    var hasTopNotch: Bool {
        return UIDevice().hasNotch
    }
    
    var body: some View {
        if isShowContent {
            HStack {
                Text(isChatView ? Resources.talk : Resources.trustar)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .foregroundColor(.white)
                    .font(.system(size: 14, weight: .bold))
                    .padding()
                
                Text(homeHeaderVm.date)
                    .frame(maxWidth: .infinity, alignment: .trailing)
                    .foregroundColor(.black)
                    .font(.system(size: 14))
                    .padding(.leading)
                
                if isShowToolbar {
                    if isChatView {
                        Button(action: {
                            if lstControlToolbars.count > 0 {
                                lstControlToolbars[0].1()
                            }
                        }, label: {
                            Image(systemName: "plus")
                                .foregroundColor(Color.white)
                                .font(.system(size: 15, weight: .bold))
                                .frame(width: 30, height: 30)
                        })
                        .padding(.trailing, 10)
                        .contextMenu(menuItems: {
                            Button(lstControlToolbars[0].0, action: {
                                lstControlToolbars[0].1()
                            })
                            .foregroundColor(.black)
                            .frame(height: 30)
                            .background(Color.white)
                        })

                    } else {
                        if #available(iOS 14.0, *) {
                            Menu(content: {
                                ForEach(0..<lstControlToolbars.count, id: \.self) { idx in
                                    Button(lstControlToolbars[idx].0, action: {
                                        lstControlToolbars[idx].1()
                                    })
                                    .foregroundColor(.black)
                                    .frame(height: 30)
                                    .background(Color.white)
                                }
                            }, label: {
                                Image(systemName: "ellipsis")
                                    .foregroundColor(Color.white)
                                    .font(.system(size: 15, weight: .bold))
                                    .frame(width: 30, height: 30)
                                    .offset(x: 0, y: -10)
                            })
                            .rotationEffect(Angle.degrees(90))
                            .padding(.trailing, 10)
                        } else {
                            Button(action: {}, label: {
                                Image(systemName: "ellipsis")
                                    .foregroundColor(Color.white)
                                    .font(.system(size: 15, weight: .bold))
                                    .rotationEffect(Angle.degrees(90))
                                    .frame(width: 30, height: 30)
                            })
                            .frame(width: 30, height: 30)
                            .contextMenu(menuItems: {
                                ForEach(0..<lstControlToolbars.count, id: \.self) { idx in
                                    Button(lstControlToolbars[idx].0, action: {
                                        lstControlToolbars[idx].1()
                                    })
                                    .foregroundColor(.black)
                                    .frame(height: 30)
                                    .background(Color.white)
                                }
                            })
                        }
                    }
                }
                
            }.frame(maxWidth: .infinity)
                .if(!modelRotation.landscape) {
                    $0.if(hasTopNotch) {
                        $0.padding(.top)
                    }
                        .padding(.top, 15)
                }
                .background(Color(Resources.colorPrimary))
        } else {
            if !modelRotation.landscape {
                Text(String())
                    .frame(maxWidth: .infinity)
                    .if(hasTopNotch) {
                        $0.padding(.top, 35)
                    }
                    .if(!hasTopNotch) {
                        $0.padding(.top, 10)
                    }
                    .background(Color(Resources.colorPrimary))
            }
        }
    }
}

struct HomeHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        HomeHeaderView()
    }
}
