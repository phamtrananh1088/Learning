//
//  BinDetailForWork.swift
//  TrustarApp
//
//  Created by CuongNguyen on 07/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

class BinDetailForWork {
    var detail: BinDetail
    
    init(binDetail: BinDetail) {
        self.detail = binDetail
    }
    
    var workStartTime: Int64? {
        return detail.startDate
    }
    
    private var workStartOffsetInMin: Int? {
        let appointedFrom = detail.appointedDateFrom
        if workStartTime != nil && appointedFrom != nil && detail.isDelayed() {
            return Int(workStartTime! - appointedFrom!) / 60_000
        }
        
        return nil
    }
    
    var workStateText: String? {
        if workStartOffsetInMin == nil {
            return nil
        } else if workStartOffsetInMin! < 0 {
            return "\(fastHHMMString(minutes: -workStartOffsetInMin!)) \(Resources.work_in_advance)"
        } else {
            return "\(fastHHMMString(minutes: workStartOffsetInMin!)) \(Resources.work_delayed)"
        }
    }
    
    var delayRankColor: Color? {
        if let absN = workStartOffsetInMin {
            var zip: [(Int, Color)] = []
            if let lstDelay = detail.delayRank?
                .components(separatedBy: ",")
                .map({ Int($0) ?? -1 }) {
                var pos = 0
                for it in lstDelay {
                    zip.append((it, Config.Shared.delayColorRank[pos]))
                    pos = pos + 1
                }
                
                zip = zip
                    .filter({(first, second) in first > 0})
                    .filter({(first, second) in abs(absN) > first})
                
                return zip.first?.1
            }
                
            return nil
        }
        
        return nil
    }
    
    var incidentalEnabled: Bool = Current.Shared.loggedUser?.userInfo.incidentalEnable() == true
}
