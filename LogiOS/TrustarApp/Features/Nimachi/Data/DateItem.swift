//
//  DateTime.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/16.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Combine

struct DateItem: Equatable {
    static func == (lhs: DateItem, rhs: DateItem) -> Bool {
        return lhs.date == rhs.date
    }
    
    //Date in miliseconds
    var date: Int64
    
    init (date: Int64) {
        self.date = date
    }
    
    public var localDateString: String {
        return Config.Shared.dateFormatterMMddHHmm.format(date: self.date)
    }
}
