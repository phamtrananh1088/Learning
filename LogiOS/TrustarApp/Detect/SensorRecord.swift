//
//  SensorRecord.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/03/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
class SensorRecord {
    class DropRecent : SensorRecord {
        var allocationNo: String
        var from: Int64
        var to: Int64
        var isFinished: Bool
        init(allocationNo: String, from: Int64, to: Int64, isFinished: Bool) {
            self.allocationNo = allocationNo
            self.from = from
            self.to = to
            self.isFinished = isFinished
        }
    }
    
    class Record : SensorRecord {
        var event: SensorDetectEvent
        init(event: SensorDetectEvent) {
            self.event = event
        }
    }
}
