//
//  HomeHeaderViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/10.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class HomeHeaderViewModel: BaseViewModel, ObservableObject {
    
    @Published var date: String = Resources.strEmpty
    
    override init() {
        super.init()
        
        date = getCurrentDate()
    }
    
    override func registerNotificationCenter() {
        
    }
    
    func getCurrentDate() -> String {
        let currentDate = Date()
        var calendar = Calendar.current
        calendar.timeZone = TimeZone(abbreviation: "JST")!
        let month = calendar.component(.month, from: currentDate)
        let day = calendar.component(.day, from: currentDate)
        let dayOfWeek = calendar.component(.weekday, from: currentDate)

        return Resources.title_m_d_w
            .replacingOccurrences(of: "{0}", with: String(format: "%02d", month))
            .replacingOccurrences(of: "{1}", with: String(format: "%02d", day))
            .replacingOccurrences(of: "{2}", with: Helper.Shared.getDayOfWeek(dayOfWeek: dayOfWeek, isLongDisplay: false))
    }
}
