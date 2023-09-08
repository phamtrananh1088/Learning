//
//  BinResponse.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/11.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BinResponse: Codable {
    var binHeaders: [RawBinHeaders]
    var binDetails: [RawBinDetail]?
    var binCollections: [RawCollectionItem]?
    
    func binHeaderList() -> [BinHeader] {
        var lst: [BinHeader] = []
        for bh in binHeaders {
            lst.append(bh.binHeader)
        }
        
        return lst
    }

    func binDetailList() -> [BinDetail] {
        var lst: [BinDetail] = []
        if binDetails != nil {
            for bd in binDetails! {
                lst.append(bd.binDetail)
            }
        }
        
        return lst
    }
    
    func collectionResults() -> [CollectionResult] {
        var list: [CollectionResult] = []
        if let bCollections = binCollections {
            var tuple: [((String, Int), [RawCollectionItem])] = []
            for it in bCollections {
                if let idx = tuple.firstIndex(where: {
                    // allocationNo
                    $0.0.0 == it.allocationNo &&
                    // allocationRowNo
                    $0.0.1 == it.allocationRowNo
                }) {
                    tuple[idx].1.append(it)
                } else {
                    tuple.append(((it.allocationNo, it.allocationRowNo), [it]))
                }
            }
            
            for t in tuple {
                var lstRowCollectionResult: [CollectionResult.RowCollectionResult] = []
                for r in t.1 {
                    lstRowCollectionResult.append(
                        CollectionResult.RowCollectionResult(
                            collectionNo: r.collectionNo,
                            collectionCd: r.collectionCd,
                            collectionNm: r.collectionNm,
                            expectedQuantity: r.collectionCount ?? 0.0,
                            actualQuantity: r.collectionCountRes ?? 0.0,
                            collectionClass: r.collectionClass,
                            displayOrder: Int.max))
                }
                list.append(CollectionResult(allocationNo: t.0.0,
                                             allocationRowNo: t.0.1,
                                             rows: lstRowCollectionResult))
            }
        }
        
        return list
    }
    
    class RawBinHeaders: Codable {
        var allocationNo: String = ""
        var allocationNm: String = ""
        var binStatus: String = ""
        var truckCd: String = ""
        var updatedDate: String?
        var unplannedFlag: Int?

        var startDatetime: String?
        var endDatetime: String?
        var drvStartDatetime: String?
        var drvEndDatetime: String?

        var driverNm: String?
        var subDriverNm: String?
        var allocationNote1: String?

        var outgoingMeter: Int?
        var incomingMeter: Int?
        
        var binHeader: BinHeader {
            return BinHeader(allocationNo: allocationNo,
                             allocationNm: allocationNm,
                             binStatus: binStatus,
                             truckCd: truckCd,
                             updatedDate: updatedDate,
                             unplannedFlag: unplannedFlag,
                             planStartDatetime: startDatetime,
                             planEndDatetime: endDatetime,
                             drvStartDatetime: drvStartDatetime,
                             drvEndDateTime: drvEndDatetime,
                             driverNm: driverNm,
                             subDriverNm: subDriverNm,
                             allocationNote1: allocationNote1,
                             outgoingMeter: outgoingMeter,
                             incomingMeter: incomingMeter)
        }
    }
    
    class RawBinDetail: Codable {
        var allocationNo: String = ""
        var allocationRowNo: Int = 0
        var allocationPlanFlag: Int = 0
        var workCd: String? = nil
        var workNm: String? = nil
        var serviceOrder: Int? = nil
        var workPlanDate: String? = nil
        var appointedTimeFrom: String? = nil
        var appointedTimeTo: String? = nil
        var placeLatitude: Double? = nil
        var placeLongitude: Double? = nil
        var placeCd: String? = nil
        var placeNm1: String? = nil
        var placeNm2: String? = nil
        var placeAddr: String? = nil
        var placeZip: String? = nil
        var placeTel1: String? = nil
        var placeMail1: String? = nil
        var placeTel2: String? = nil
        var placeMail2: String? = nil
        var noteNoticeClass: String? = nil
        var placeNote1: String? = nil
        var placeNote2: String? = nil
        var placeNote3: String? = nil
        var status: String = ""
        var chartCd: String? = nil
        var delayCheckFlag: String = ""
        var delayToleranceFrom: String? = nil
        var delayToleranceTo: String? = nil
        var delayStatus: String = ""
        var misdeliveryCheckFlag: String = ""
        var misdeliveryMeterTo: String? = nil
        var stayTime: Int = 0
        var misdeliveryStatus: String = ""

        var operationOrder: Int? = nil
        var workDatetimeFrom: String? = nil
        var workDatetimeTo: String? = nil
        var delayReasonCd: String? = nil
        var delayRankC: Int? = nil
        var delayRankB: Int? = nil
        var delayRankA: Int? = nil
        var temperature: String? = nil
        var experiencePlaceNote1: String? = nil
        var updatedDate: String? = nil
        
        var binDetail: BinDetail {
            var atf: Int64?
            var att: Int64?
            
            if let wd = workPlanDate.dateFromStringT() {
                if let min = appointedTimeFrom?.hms60From4dig() {
                    atf = Int64(wd.timeIntervalSince1970 * 1000) + Int64(Clock(h: 0, m: min, s: 0, millis: 0).totalMillis)
                }
                
                if let min = appointedTimeTo?.hms60From4dig() {
                    att = Int64(wd.timeIntervalSince1970 * 1000) + Int64(Clock(h: 0, m: min, s: 0, millis: 0).totalMillis)
                }
            }
            
            // workStart
            var workStartDate: Int64?
            if let tm = workDatetimeFrom.dateFromStringT() {
                workStartDate = Int64(tm.timeIntervalSince1970 * 1000)
            }
            
            // workEnd
            var workEndDate: Int64?
            if let tm = workDatetimeTo.dateFromStringT() {
                workEndDate = Int64(tm.timeIntervalSince1970 * 1000)
            }
            
            // placeLocation
            if placeLatitude == 0 || placeLongitude == 0 {
                placeLatitude = nil
                placeLongitude = nil
            }
            
            // delayStatus
            var dlStatus: Int = 1
            if delayStatus != "1" || atf == nil {
                dlStatus = Int(delayStatus)!
            } else if workStartDate == nil || workStartDate! > atf! {
                dlStatus = 2
            }
            
            // delayRank
            var dlRank: String = Resources.strEmpty
            dlRank.append(String(delayRankC ?? -1))
            dlRank.append(",")
            dlRank.append(String(delayRankB ?? -1))
            dlRank.append(",")
            dlRank.append(String(delayRankA ?? -1))
            
            // updateDate
            var updateDate: Int64? = nil
            if let uDate = updatedDate.dateFromString2()?.timeIntervalSince1970 {
                updateDate = Int64(uDate) * 1000
            }
            
            return BinDetail(
                allocationNo: allocationNo,
                allocationRowNo: allocationRowNo,
                allocationPlanFlag: allocationPlanFlag,
                workCd: workCd,
                workNm: workNm,
                serviceOrder: serviceOrder,
                appointedDateFrom: atf,
                appointedDateTo: att,
                placeLatitude: placeLatitude,
                placeLongitude: placeLongitude,
                placeCd: placeCd,
                placeNm1: placeNm1,
                placeNm2: placeNm2,
                placeAddr: placeAddr,
                placeZip: placeZip,
                placeTel1: placeTel1,
                placeMail1: placeMail1,
                placeTel2: placeTel2,
                placeMail2: placeMail2,
                noteNoticeClass: noteNoticeClass,
                placeNote1: placeNote1,
                placeNote2: placeNote2,
                placeNote3: placeNote3,
                status: Int(status) ?? 0,
                delayCheckFlag: delayCheckFlag,
                delayToleranceFrom: delayToleranceFrom ?? "0",
                delayToleranceTo: delayToleranceTo ?? "0",
                delayStatus: dlStatus,
                misdeliveryCheckFlag: misdeliveryCheckFlag,
                misdeliveryMeterTo: misdeliveryMeterTo ?? "0",
                stayTime: stayTime,
                misdeliveryStatus: misdeliveryStatus,
                operationOrder: operationOrder,
                delayReasonCd: delayReasonCd,
                delayRank: dlRank,
                origAllocationRowNo: nil,
                startLatitude: nil,
                startLongitude: nil,
                startAccuracy: nil,
                startDate: workStartDate,
                endLatitude: nil,
                endLongitude: nil,
                endAccuracy: nil,
                endDate: workEndDate,
                temperature: temperature,
                experiencePlaceNote1: experiencePlaceNote1,
                updatedDate: updateDate,
                chartCd: chartCd)
        }
    }
    
    class RawCollectionItem: Codable {
        var allocationNo: String = ""
        var allocationRowNo: Int = 0
        var collectionNo: String? = nil
        var collectionClass: Int = 0
        var collectionCd: String? = nil
        var collectionNm: String = ""
        var collectionCount: Double? = nil
        var collectionCountRes: Double? = nil
    }
}
