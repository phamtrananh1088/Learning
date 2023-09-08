//
//  WeatherView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/08.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct WeatherView: View {
    @ObservedObject var weatherVm: WeatherViewModel = WeatherViewModel()
    @EnvironmentObject private var navigationStack: NavigationStack
    @EnvironmentObject private var modelRotation: ModelRotation
    @State var isShowHideWeather: Bool = false

    var body: some View {
        NavigationStackView {
            ZStack {
                VStack {
                    Text(weatherVm.date)
                        .foregroundColor(.white)
                        .font(.system(size: 22))
                        .padding(56)
                    
                    Spacer()
                    Text(Resources.tf_weather_today)
                        .foregroundColor(.white)
                        .font(.system(size: 14))
                        .padding(4)
                    
                    WeatherExpand(isRotated: $isShowHideWeather, weather: $weatherVm.weather)
                    
                    Spacer()
                    
                    Button(weatherVm.buttonText) {
                        weatherVm.actionAfterPick()
                        if weatherVm.current.getBeforeScreen() == .Login || weatherVm.current.getBeforeScreen() == .Notice {
                            weatherVm.current.changeScreenTo(screenName: .DashBoard)
                            
                        } else {
                            self.navigationStack.pop()

                        }
                    }.buttonStyle(WhiteButtonStyle())
                        .padding(56)
                    
                }.frame(maxWidth:.infinity, maxHeight: .infinity, alignment: .center)
                    .animation(.default)
                    .edgesIgnoringSafeArea(.all)
                
                if isShowHideWeather {
                    Color.black.opacity(0.5)
                }
            }
            .background(LinearGradient(gradient: Gradient(colors: [Color(Resources.colorPrimary),Color(Resources.colorPrimaryDark)]), startPoint: .top, endPoint: .bottom))
                .edgesIgnoringSafeArea(.all)
                .overlay(WeatherPickerView(weatherPicked: $weatherVm.weather, isRotated: $isShowHideWeather, actionAfterPick: weatherVm.actionAfterPick)
                            .frame(maxHeight: .infinity, alignment: .bottom)
                            .edgesIgnoringSafeArea(.all)
                            .offset(y: self.isShowHideWeather ? 0 : UIScreen.main.bounds.height)
                            .animation(.default)
                            .if(modelRotation.landscape) {
                    $0.padding(.bottom)
                }
                         ,alignment: .bottom)
                .onTapGesture {
                    self.isShowHideWeather = false
                }
        }
        
    }
}

//struct WeatherView_Previews: PreviewProvider {
//    static var previews: some View {
//        WeatherView(weatherVm: WeatherViewModel(), isShowHideWeather: .init(true), tabBar: .constant(UITabBar()))
//    }
//}
