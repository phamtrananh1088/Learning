//
//  SettingView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct SettingView: View {
    @State private var userTel: String = ""
    @State private var deviceId: String = ""
    @State private var isWeatherView = false
    @EnvironmentObject var modelRotaion: ModelRotation
    @EnvironmentObject private var navigationStack: NavigationStack

    init()
    {
        _userTel = State(initialValue: Current.Shared.loggedUser?.userInfo.branchTel ?? "")
        _deviceId = State(initialValue: Config.Shared.clientInfo.terminalId)
    }
    
    var body: some View {
        NavigationStackView {
            ZStack {
                VStack(spacing: 0) {
                    // Region: Header
                    //HomeHeaderView()
                    HomeHeaderView(isShowContent: false)
                    
                    HStack {
                        Button(action: {
                            Current.Shared.changeScreenTo(screenName: .DashBoard)
                        }, label: {
                            Image(systemName: "chevron.left")
                                .foregroundColor(.white)
                                .font(.system(size: 18, weight: .bold))
                                .padding(.horizontal, 10)
                        })
                        
                        Text(Resources.settings)
                            .foregroundColor(.white)
                            .bold()
                            .font(.system(size: 18))
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .padding(.vertical, 10)
                    .padding(.horizontal, 8)
                    .background(Color(Resources.colorPrimary))
                    
                    if modelRotaion.landscape {
                        ScrollView {
                            content
                        }
                    } else {
                        content
                        Spacer()
                    }
                }
            }
        }
        
        .edgesIgnoringSafeArea(.top)
        .background(Color(Resources.defaultBackground))
    }
    
    var content: some View {
        VStack(alignment: .center, spacing: 30) {
            /* システムの問い合わせ */
            HStack {
                Spacer()
                VStack {
                    Text(Resources.ask_for_support)
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundColor(Color.black)
                    
                    /* tel */
                    Button {
                        /*設定・電話ボタン押下時に起動されるイベント*/
                        let phone = "tel://"
                        let phoneNumberformatted = phone + userTel
                        guard let url = URL(string: phoneNumberformatted) else { return }
                        UIApplication.shared.open(url)
                    } label: {
                        HStack {
                            Image(systemName: "phone.fill")
                                .imageScale(.large)
                            Text(userTel)
                        }.frame(width: 170)
                    }.buttonStyle(PrimaryButtonStyle())
                    .padding(5)
                }
                Spacer()
            }
            
            Divider()
            HStack {
                PushView(destination: WeatherView(),
                         isActive: $isWeatherView,
                         label: {
                            /*設定・天候ボタン押下時に起動されるイベント*/
                            Button {
                                isWeatherView = true
                            } label: {
                                Text(Resources.weather_settings)
                                    .frame(width: 170)
                            }.buttonStyle(PrimaryButtonStyle())
                        }
                )
            }
            
            Divider()
            HStack{
                Button(action: {
                    /*設定・ユーザー切替押下時に起動されるイベント*/
                    Current.Shared.changeScreenTo(screenName: .Login)
                    Helper.Shared.sendBroadcastNotification(.RemoveObserver)
                }, label: {
                    Text(Resources.switch_user)
                        .frame(width: 170)
                }).buttonStyle(PrimaryButtonStyle())
            }
            
            Divider()
            HStack(alignment: .center) {
                Text(Resources.terminal_id_s1 + deviceId)
                    .font(.system(size: 14))
                    .foregroundColor(.black)
                    .frame(maxWidth: .infinity)
            }.frame(maxWidth:.infinity, alignment: .center)
                .padding(.bottom, 50)
        }
        .padding(.top, 50)
    }
}

struct SettingView_Previews: PreviewProvider {
    static var previews: some View {
        SettingView()
    }
}
