//
//  TimeItem.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/16.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Combine
import Foundation
class TimeItem: Hashable, Equatable, Identifiable {
    let ID = UUID()
    
    static func == (lhs: TimeItem, rhs: TimeItem) -> Bool {
        return lhs.begin == rhs.begin &&
               lhs.end == rhs.end
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(Unmanaged.passUnretained(self).toOpaque())
    }
    
    var begin: DateItem?
    var end: DateItem?
    //0: 荷待待機　1: 付帯業務
    var type: Int
    
    var beginDateString: String? {
        return begin?.localDateString
    }
    
    var endDateString: String? {
        return end?.localDateString
    }
    
    init(begin: DateItem?, end: DateItem?, type: Int) {
        self.begin = begin
        self.end = end
        self.type = type
    }
    
    init(begin: Int64?, end: Int64?, type: Int) {
        if begin != nil {
            self.begin = DateItem(date: begin!)
        }
        if end != nil {
            self.end = DateItem(date: end!)
        }
        self.type = type
    }
}

extension Array where Element: TimeItem {
    func nimachiTime() -> [Element] {
        return self.filter { item in
            item.type == 0
        }
    }
    
    func additionalTime() -> [Element] {
        return self.filter { item in
            item.type == 1
        }
    }
}

class TimeItemDB: TimeItem {
    var time: IncidentalTime
    
    init(time: IncidentalTime) {
        self.time = time
        super.init(begin: time.beginDatetime, end: time.endDatetime, type: time.type)
    }
}
