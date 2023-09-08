//
//  IncidentalResponse.swift
//  TrustarApp
//
//  Created by CuongNguyen on 02/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class IncidentalResponse: Codable {
    var incidentalHeaders: [RawIncidentalHeader]?
    var incidentalWorks: [RawIncidentalWorkRelated]?
    var incidentals: [RawIncidentalWork]?
    var incidentalTimes: [RawIncidentalTime]?
    
    func incidentalHeaderList() -> [IncidentalHeader]? {
        if incidentalHeaders != nil {
            var lstIncidentalHeader: [IncidentalHeader] = []
            for it in incidentalHeaders! {
                
                var lstWorkCd: [String] = []
                if incidentalWorks != nil {
                    for iw in incidentalWorks! {
                        if iw.sheetNo == it.sheetNo {
                            lstWorkCd.append(iw.incidentalCd)
                        }
                    }
                }
                
                lstIncidentalHeader.append(IncidentalHeader(uuid: it.uuid, sheetNo: it.sheetNo, allocationNo: it.allocationNo, allocationRowNo: it.allocationRowNo, shipperCd: it.shipperCd, workList: lstWorkCd, status: it.stt, picId: it.picId))
            }
            
            return lstIncidentalHeader
        }
        
        return nil
    }
    
    func incidentalWorkList() -> [IncidentalWork]? {
        if incidentals != nil {
            var lst: [IncidentalWork] = []
            for it in incidentals! {
                lst.append(it.item)
            }
            
            return lst
        }
        return nil
    }
    
    func incidentalTimeList() -> [IncidentalTime]? {
        if incidentalTimes != nil {
            var lst: [IncidentalTime] = []
            for it in incidentalTimes! {
                if let h = incidentalHeaders?.first(where: { $0.sheetNo == it.sheetNo }) {
                    lst.append(IncidentalTime(uuid: it.guid, headerUUID: h.uuid, sheetNo: it.sheetNo, sheetRowNo: it.sheetRowNo, type: Int(it.incidentalCls)!, beginDatetime: it.bDateTime, endDatetime: it.eDateTime))
                }
            }
            
            return lst
        }
        
        return nil
    }
    
    class RawIncidentalWorkRelated: Codable {
        var sheetNo: String = ""
        var incidentalCd: String = ""
    }
    
    class RawIncidentalHeader: Codable {
        var localSeq: String? // uuid
        var sheetNo: String = ""
        var allocationNo: String = ""
        var allocationRowNo: Int = 0
        var shipperCd: String = ""
        var status: String?
        var picId: String?
        
        var uuid: String {
            if let uuid = localSeq {
                return uuid
            }
            
            return Helper.Shared.hashMd5Hex16Digit(string: UUID().uuidString)
        }
        
        // status
        var stt: Int {
            switch status?.lowercased() {
            case "x":
                return -1  //署名無しで完了
            case "z":
                return 1   //署名済
            default:
                return 0   //未署名
            }
        }
    }


    class RawIncidentalWork: Codable {
        var incidentalCd: String = ""
        var incidentalNm: String?
        var displayOrder: Int = 0
        
        var item: IncidentalWork {
            return IncidentalWork(workCd: incidentalCd, workNm: incidentalNm?.trimming(spaces: .leadingAndTrailing), displayOrder: displayOrder)
        }
    }

    class RawIncidentalTime: Codable {
        var guid: String = "" // uuid
        var sheetNo: String = ""
        var sheetRowNo: Int = 0
        var incidentalCls: String = ""
        var beginDatetime: String?
        var endDatetime: String?
        
        var eDateTime: Int64? {
            return endDatetime.dateFromString2()!.milisecondsSince1970
        }
        
        var bDateTime: Int64? {
            return beginDatetime.dateFromString2()!.milisecondsSince1970
        }
    }
}



