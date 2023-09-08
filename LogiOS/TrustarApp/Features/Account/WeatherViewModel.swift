//
//  WeatherViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/08.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class WeatherViewModel: BaseViewModel, ObservableObject {
    
    @Published var date: String = Resources.strEmpty
    @Published var buttonText: String = Resources.strEmpty
    @Published var weather: WeatherEnum = .Hare
    
    override init() {
        super.init()
        
        if let lastWeather = Helper.Shared.getLastWeather() {
            weather = lastWeather
        }
        
        if current.getBeforeScreen() == .Login {
            // ダッシュボードへ
            buttonText = Resources.goto_home_dashboard
        } else {
            // 天気を設定する
            buttonText = Resources.weather_setup_close
        }
        
        getCurrentDate()
    }
    
    override func registerNotificationCenter() {
        
    }
    
    func getCurrentDate() {
        let currentDate = Date()
        var calendar = Calendar.current
        calendar.timeZone = TimeZone(abbreviation: "JST")!
        let month = calendar.component(.month, from: currentDate)
        let day = calendar.component(.day, from: currentDate)
        let dayOfWeek = calendar.component(.weekday, from: currentDate)

        date = Resources.weather_m_d_ww
            .replacingOccurrences(of: "{0}", with: String.init(month))
            .replacingOccurrences(of: "{1}", with: String.init(day))
            .replacingOccurrences(of: "{2}", with: Helper.Shared.getDayOfWeek(dayOfWeek: dayOfWeek))
    }
    
    func actionAfterPick() {
        Helper.Shared.setWeather(weather: weather)
    }
}
