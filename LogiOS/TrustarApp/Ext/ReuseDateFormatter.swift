//
//  ReuseDateFormatter.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/16.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
class ReuseDateFormatter {
    private var pattern: String = "yyyy-MM-DD  HH:mm:ss"
    private var locale: Locale = Locale(identifier: "ja_JP")
    private var timeZone: TimeZone? = TimeZone(identifier: "Asia/Tokyo")
    private var dateFormatter: DateFormatter  = DateFormatter()
    
    init (pattern: String, locale: Locale, timeZone: TimeZone?) {
        self.pattern = pattern
        self.locale = locale
        self.timeZone = timeZone

        self.dateFormatter.dateFormat = self.pattern
        self.dateFormatter.timeZone = self.timeZone
        self.dateFormatter.locale = self.locale
    }
    
    func format(date: Date) -> String {
        return dateFormatter.string(from: date)
    }
    
    func format(date: Int64) -> String {
        let dateVal = Date(miliseconds:  date)
        return dateFormatter.string(from: dateVal)
    }
    
    func parse(text: String) -> Date? {
        return dateFormatter.date(from: text)
    }
}
