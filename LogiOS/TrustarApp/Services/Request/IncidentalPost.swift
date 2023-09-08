//
//  IncidentalPost.swift
//  TrustarApp
//
//  Created by CuongNguyen on 08/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class IncidentalPost {
    private var clientInfo: ClientInfo
    private var headerList: [CommonIncidentalHeaderResult]
    private var timeList: [CommonIncidentalTimeResult]
    
    init(clientInfo: ClientInfo,
         headerList: [CommonIncidentalHeaderResult],
         timeList: [CommonIncidentalTimeResult]
    ) {
        self.clientInfo = clientInfo
        self.headerList = headerList
        self.timeList = timeList
    }
    
    private class R {
        var header: [CommonIncidentalHeaderResult] = []
        var time: [CommonIncidentalTimeResult]     = []
    }
        
    private var r: [String : [String : R]] = [String : [String : R]]()
    
    class IncidentalResult: Codable {
        var companyCd: String                 = String()
        var userId: String                    = String()
        var removeIncidental: [String]        = []
        var editIncidental: [EditIncidental]  = []
        var addIncidental: [AddIncidental]    = []
        var editTime: [EditTime]              = []
        var removeTime: [RemoveTime]          = []
        var addTime: [AddTime]                = []
    }
    
    class EditIncidental: Codable {
        var sheetNo: String = String()
        var shipperCd: String = String()
        var workCd: [String] = []
        var picId: String? = nil
    }
    
    class AddIncidental: Codable {
        var guid: String = String()
        var allocationNo: String = String()
        var allocationRowNo: Int = 0
        var shipperCd: String = String()
        var workCd: [String] = []
        var picId: String? = nil
        var time: [Time] = []
        
        class Time: Codable {
            var guid: String = String()
            var cls: Int = 0
            var beginTime: Int64?
            var endTime: Int64?
        }
    }
    
    class EditTime: Codable {
        var sheetNo: String = String()
        var sheetRowNo: Int = 0
        var beginTime: Int64?
        var endTime: Int64?
    }
    
    class RemoveTime: Codable {
        var sheetNo: String = String()
        var sheetRowNo: Int = 0
    }
    
    class AddTime: Codable {
        var guid: String = String()
        var sheetNo: String = String()
        var cls: Int = 0
        var beginTime: Int64?
        var endTime: Int64?
    }
    
    class ResultModel: Codable {
        var incidentalResults: [IncidentalResult]
        var clientInfo: ClientInfo
        
        init(incidentalResult: [IncidentalResult], clientInfo: ClientInfo) {
            self.incidentalResults = incidentalResult
            self.clientInfo = clientInfo
        }
    }
    
    func result() -> ResultModel {
        r = [String : [String : R]]()
        
        // create dictionary:
        // If contain -> append
        // if not contain -> add new
        for it in headerList {
            if var valByCompany = r[it.companyCd] {
                if let valByUserId = valByCompany[it.userId] {
                    valByUserId.header.append(it)
                } else {
                    let obj = R()
                    obj.header.append(it)
                    valByCompany[it.userId] = obj
                }
            } else {
                var dicObj = [String : R]()
                let obj = R()
                obj.header.append(it)
                dicObj[it.userId] = obj
                r[it.companyCd] = dicObj
            }
        }
        
        for it in timeList {
            if var valByCompany = r[it.companyCd] {
                if let valByUserId = valByCompany[it.userId] {
                    valByUserId.time.append(it)
                } else {
                    let obj = R()
                    obj.time.append(it)
                    valByCompany[it.userId] = obj
                }
            } else {
                var dicObj = [String : R]()
                let obj = R()
                obj.time.append(it)
                dicObj[it.userId] = obj
                r[it.companyCd] = dicObj
            }
        }
        
        var result: [IncidentalResult] = []
        
        for l in r {
            var removedIncidental : [String]           = []
            var addedIncidental   : [AddIncidental]    = []
            var editedIncidental  : [EditIncidental]   = []
            var removedTime       : [RemoveTime]       = []
            var addedTime         : [AddTime]          = []
            var editedTime        : [EditTime]         = []
            
            let item: IncidentalResult = IncidentalResult()
            
            item.companyCd = l.key
            
            for r in l.value {
                item.userId = r.key
                var map = Dictionary(grouping: r.value.time, by: { $0.headerUUID })
                let hg = Dictionary(grouping: r.value.header, by: { $0.sheetNo == nil })
                
                if hg[true] != nil {
                    for h in hg[true]! {
                        let times = map.removeValue(forKey: h.uuid)
                        if !h.deleted {
                            let ai: AddIncidental = AddIncidental()
                            addedIncidental.append(ai)
                            ai.guid = h.uuid
                            ai.allocationNo = h.allocationNo
                            ai.allocationRowNo = h.allocationRowNo
                            ai.shipperCd = h.shipperCd
                            ai.workCd = h.workList.components(separatedBy: ",")
                            ai.picId = h.picId
                            
                            if times != nil {
                                for t in times! {
                                    if !t.deleted {
                                        let at: AddIncidental.Time = AddIncidental.Time()
                                        ai.time.append(at)
                                        at.guid = t.uuid
                                        at.cls = t.type
                                        at.beginTime = t.beginDatetime
                                        at.endTime = t.endDatetime
                                    }
                                }
                            }
                        }
                    }
                }
                
                if hg[false] != nil {
                    for h in hg[false]! {
                        if let s = h.sheetNo {
                            if h.deleted {
                                map.removeValue(forKey: h.uuid)
                                removedIncidental.append(s)
                            } else {
                                let ei: EditIncidental = EditIncidental()
                                editedIncidental.append(ei)
                                ei.sheetNo = s
                                ei.shipperCd = h.shipperCd
                                ei.workCd = h.workList.components(separatedBy: ",")
                                ei.picId = h.picId
                            }
                        }
                    }
                }
                
                for value in map.values {
                    for t in value {
                        let s = t.sheetNo
                        let n = t.sheetRowNo
                        if s != nil && n != nil {
                            if t.deleted {
                                let rt: RemoveTime = RemoveTime()
                                removedTime.append(rt)
                                rt.sheetNo = s!
                                rt.sheetRowNo = n!
                            } else {
                                 let et: EditTime = EditTime()
                                 editedTime.append(et)
                                 et.sheetNo = s!
                                 et.sheetRowNo = n!
                                 et.beginTime = t.beginDatetime
                                 et.endTime = t.endDatetime
                            }
                        } else {
                            let at: AddTime = AddTime()
                            addedTime.append(at)
                            at.guid = t.uuid
                            at.sheetNo = s!
                            at.cls = t.type
                            at.beginTime = t.beginDatetime
                            at.endTime = t.endDatetime
                        }
                    }
                }
            }
            
            item.removeIncidental = removedIncidental
            item.addIncidental = addedIncidental
            item.editIncidental = editedIncidental
            item.removeTime = removedTime
            item.addTime = addedTime
            item.editTime = editedTime
            
            result.append(item)
        }
        
        return ResultModel(incidentalResult: result, clientInfo: clientInfo)
    }
}
