//
//  MasterResponse.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation

class MasterResponse: Codable {
    var binStatuses: [BinStatus]?
    var workStatuses: [WorkStatus]?
    var fuels: [Fuel]?
    var weathers: [Weather]?
    var works: [RawWork]?
    var trucks: [Truck]?
    var delayReason: [DelayReason]?
    var shippers: [Shipper]?
    var collections: [CollectionGroup]?
    
    func getWorks() -> [Work] {
        var lstWork: [Work] = []
        if works != nil {
            for it in works! {
                lstWork.append(it.work)
            }
        }
        
        return lstWork
    }
    
    
    class RawWork: Codable {
        var workCd: String = Resources.strEmpty
        var workNm: String = Resources.strEmpty
        var displayOrder: Int = Resources.zeroNumber
        var appDisplayFlag: Int? = nil
    
        var work: Work {
            return Work(workCd: workCd,
                        workNm: workNm,
                        displayOrder: displayOrder,
                        displayFlag: appDisplayFlag != 0)
        }
    }
}
