//
//  WeatherPickerView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct WeatherPickerView: View {
    
    @EnvironmentObject var model: ModelRotation
    @Binding var weatherPicked: WeatherEnum
    @Binding var isRotated: Bool
    var actionAfterPick: (() -> ())? = nil
    
    var body: some View {
        if !model.landscape {
            VStack {
                HStack {
                    ForEach(WeatherEnum.allCases, id: \.self) { weather in
                        if weather.rawValue <= 3 {
                            WeatherItemView(image: weather.getWeather(), text: weather.getTextWeather())
                                .frame(maxWidth: .infinity)
                                .onTapGesture {
                                    onTapGuesture(weather: weather)
                                }
                        }
                    }
                }

                HStack {
                    ForEach(WeatherEnum.allCases, id: \.self) { weather in
                        if weather.rawValue > 3 {
                            WeatherItemView(image: weather.getWeather(), text: weather.getTextWeather())
                                .frame(maxWidth: .infinity)
                                .onTapGesture {
                                    onTapGuesture(weather: weather)
                                }
                        }
                    }
                }
            }
            .edgesIgnoringSafeArea(.all)
            .frame(maxWidth: .infinity)
            .background(Color.white)
        } else {
            HStack {
                ForEach(WeatherEnum.allCases, id: \.self) { weather in
                    WeatherItemView(image: weather.getWeather(), text: weather.getTextWeather())
                        .frame(maxWidth: .infinity)
                        .onTapGesture {
                            onTapGuesture(weather: weather)
                        }
                }
            }
            .frame(maxWidth: .infinity)
            .background(Color.white)
        }
    }
    
    private func onTapGuesture(weather: WeatherEnum) {
        isRotated = false
        
        if weatherPicked != weather {
            weatherPicked = weather
            if actionAfterPick != nil {
                actionAfterPick!()
            }
        }
    }
}

//struct WeatherPickerView_Previews: PreviewProvider {
//    static var previews: some View {
//        WeatherPickerView()
//    }
//}
