//
//  BinDetailData.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BinDetailData {
    
    var binDetail: BinDetail
    var delayReasons: [DelayReason]
    var incidentalHeaderCount: Int
    var signedIncidentalHeaderCount: Int
    
    init(binDetail: BinDetail, delayReasons: [DelayReason], incidentalHeaderCount: Int, signedIncidentalHeaderCount: Int) {
        self.binDetail = binDetail
        self.delayReasons = delayReasons
        self.incidentalHeaderCount = incidentalHeaderCount
        self.signedIncidentalHeaderCount = signedIncidentalHeaderCount
    }
    
    var delayReason: DelayReason? {
        if let cd = binDetail.delayReasonCd {
           return delayReasons.first(where: { $0.reasonCd == cd })
        }
        
        return nil
    }
    
    var incidentalEnable: Bool {
        return Current.Shared.loggedUser?.userInfo.incidentalEnable() == true
    }
}
