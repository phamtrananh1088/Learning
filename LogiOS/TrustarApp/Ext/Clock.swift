//
//  Clock.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/17.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class Clock {
    private var millisOfTheDay: Int
    
    init(millis: Int64) {
        millisOfTheDay = Int((millis % 86_400_000))
    }
    
    init(date: Date) {
        millisOfTheDay = Int(date.timeIntervalSince1970)
    }
    
    init(h: Int, m: Int, s: Int, millis: Int) {
        millisOfTheDay = (((h * 60) + m) * 60 + s) * 1000 + millis
    }
    
    var totalMillis: Int {
        return millisOfTheDay % 86_400_000
    }
    
    var totalSeconds: Int {
        return totalMillis / 1000
    }
    
    var totalMinutes: Int {
        return totalSeconds / 60
    }
    
    var hours: Int {
        return totalMinutes / 60
    }
    
    var minutes: Int {
        return totalMinutes - hours * 60
    }
    
    var seconds: Int {
        return totalSeconds - totalMinutes * 60
    }
    
    var millis: Int {
        return totalMillis - totalSeconds * 1000
    }
    
    var mmss: String {
        return String(format: "%02d:%02d", totalMinutes, seconds)
    }

    var hhmm: String {
        return String(format: "%02d:%02d", hours, minutes)
    }
    
    var hhmmss: String {
        return hhmm + String(format: ":%02d", seconds)
    }

    var fullString: String {
        return hhmmss + String(format: ".%03d", millis)
    }

    func toString() -> String {
        return fullString
    }
}
