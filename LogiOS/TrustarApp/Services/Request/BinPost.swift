//
//  BinPost.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/28.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BinPost: Codable {
    var binResults: [CommonBinResult]
    var workResults: [CommonWorkResult]
    var clientInfo: ClientInfo
    
    init(bin: [CommonBinResult], work: [CommonWorkResult], client: ClientInfo) {
        self.binResults = bin
        self.workResults = work
        self.clientInfo = client
    }
    
    func result() -> BinPost {
        var lstBin: [CommonBinResult] = []
        var lstWork: [CommonWorkResult] = []
        
        for it in binResults {
            let cb: CommonBinResult = CommonBinResult()
            lstBin.append(cb)
            cb.companyCd = it.companyCd
            cb.userId = it.userId
            cb.allocationNo = it.allocationNo
            cb.truckCd = it.truckCd
            cb.weatherCd = it.weatherCd
            cb.destinationRowNo = it.destinationRowNo
            
            if !it.startWorkIsNull() {
                cb.startLatitude = it.startLatitude ?? 0.0
                cb.startLongitude = it.startLongitude ?? 0.0
                cb.startAccuracy = it.startAccuracy ?? 0
                cb.startDatetime = it.startDatetime
            }
            
            if !it.endWorkIsNull() {
                cb.endLatitude = it.endLatitude ?? 0.0
                cb.endLongitude = it.endLongitude ?? 0.0
                cb.endAccuracy = it.endAccuracy ?? 0
                cb.endDatetime = it.endDatetime
            }
            
            cb.outgoingMeter = it.outgoingMeter
            cb.incomingMeter = it.incomingMeter
            cb.updatedDate = it.updatedDate
        }
        
        for it in workResults {
            let wr: CommonWorkResult = CommonWorkResult()
            lstWork.append(wr)
            wr.companyCd = it.companyCd
            wr.userId = it.userId
            wr.allocationNo = it.allocationNo
            wr.allocationRowNo = it.allocationRowNo
            
            wr.workCd = it.workCd
            wr.status = it.status
            wr.delayStatus = it.delayStatus == 0 ? 0 : 1
            wr.delayReasonCd = it.delayReasonCd
            wr.misdeliveryStatus = it.misdeliveryStatus
            wr.orgAllocationRowNo = it.orgAllocationRowNo
            
            if !it.startWorkIsNull() {
                wr.latitudeFrom = it.latitudeFrom ?? 0.0
                wr.longitudeFrom = it.longitudeFrom ?? 0.0
                wr.accuracyFrom = it.accuracyFrom ?? 0
                wr.workDatetimeFrom = it.workDatetimeFrom
            }
            
            if !it.endWorkIsNull() {
                wr.latitudeTo = it.latitudeTo ?? 0.0
                wr.longitudeTo = it.longitudeTo ?? 0.0
                wr.accuracyTo = it.accuracyTo ?? 0
                wr.workDatetimeTo = it.workDatetimeTo
            }
            
            wr.temperature = it.temperature
            wr.experiencePlaceNote1 = it.experiencePlaceNote1
            wr.operatingMode = it.operatingMode
        }
        
        return BinPost(bin: lstBin, work: lstWork, client: clientInfo)
    }
}
