//
//  CommonBinResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/03.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonBinResultColumns: String, ColumnExpression {
    case companyCd, userId, allocationNo, truckCd, weatherCd, destinationRowNo
    
    case startLatitude, startLongitude, startAccuracy, startDatetime
    case endLatitude, endLongitude, endAccuracy, endDatetime
    
    case outgoingMeter, incomingMeter, updatedDate
}

class CommonBinResult: BaseEntity, Codable {
    var companyCd: String             = Resources.strEmpty
    var userId: String                = Resources.strEmpty
    var allocationNo: String          = Resources.strEmpty

    var truckCd: String               = Resources.strEmpty
    var weatherCd: Int?               = nil
    var destinationRowNo: Int         = Resources.zeroNumber

    var startLatitude: Double?        = nil
    var startLongitude: Double?       = nil
    var startAccuracy: Double?        = nil
    var startDatetime: Int64?         = nil

    var endLatitude: Double?          = nil
    var endLongitude: Double?         = nil
    var endAccuracy: Double?          = nil
    var endDatetime: Int64?           = nil

    var outgoingMeter: Int?           = nil
    var incomingMeter: Int?           = nil

    var updatedDate: Int64            = 0
    
    override init() {
        super.init()
        recordChanged()
    }

    override class var databaseTableName: String { "CommonBinResult" }
    
    override class var primaryKey: String { "companyCd,userId,allocationNo" }

    required init(row: Row) {
        companyCd = row[CommonBinResultColumns.companyCd]
        userId = row[CommonBinResultColumns.userId]
        allocationNo = row[CommonBinResultColumns.allocationNo]
        
        truckCd = row[CommonBinResultColumns.truckCd]
        weatherCd = row[CommonBinResultColumns.weatherCd]
        destinationRowNo = row[CommonBinResultColumns.destinationRowNo]
       
        startLatitude = row[CommonBinResultColumns.startLatitude]
        startLongitude = row[CommonBinResultColumns.startLongitude]
        startAccuracy = row[CommonBinResultColumns.startAccuracy]
        startDatetime = row[CommonBinResultColumns.startDatetime]
        
        endLatitude = row[CommonBinResultColumns.endLatitude]
        endLongitude = row[CommonBinResultColumns.endLongitude]
        endAccuracy = row[CommonBinResultColumns.endAccuracy]
        endDatetime = row[CommonBinResultColumns.endDatetime]
        
        outgoingMeter = row[CommonBinResultColumns.outgoingMeter]
        incomingMeter = row[CommonBinResultColumns.incomingMeter]
        updatedDate = row[CommonBinResultColumns.updatedDate]
        
        super.init(row: row)
    }
    
    init(userInfo: UserInfo, binHeader: BinHeader) {
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        allocationNo = binHeader.allocationNo
        
        truckCd = binHeader.truckCd
        weatherCd = binHeader.weatherCd
        destinationRowNo = binHeader.destinationRowNo ?? 0
       
        startLatitude = binHeader.startLatitude
        startLongitude = binHeader.startLongitude
        startAccuracy = binHeader.startAccuracy
        startDatetime = binHeader.startDate

        endLatitude = binHeader.endLatitude
        endLongitude = binHeader.endLongitude
        endAccuracy = binHeader.endAccuracy
        endDatetime = binHeader.endDate

        outgoingMeter = binHeader.outgoingMeter
        incomingMeter = binHeader.incomingMeter
        updatedDate = binHeader.updatedDate
        super.init()
        self.recordChanged()
    }

    override func encode(to container: inout PersistenceContainer) {
        container[CommonBinResultColumns.companyCd] = companyCd
        container[CommonBinResultColumns.userId] = userId
        container[CommonBinResultColumns.allocationNo] = allocationNo
        
        container[CommonBinResultColumns.truckCd] = truckCd
        container[CommonBinResultColumns.weatherCd] = weatherCd
        container[CommonBinResultColumns.destinationRowNo] = destinationRowNo

        container[CommonBinResultColumns.startLatitude] = startLatitude
        container[CommonBinResultColumns.startLongitude] = startLongitude
        container[CommonBinResultColumns.startAccuracy] = startAccuracy
        container[CommonBinResultColumns.startDatetime] = startDatetime

        container[CommonBinResultColumns.endLatitude] = endLatitude
        container[CommonBinResultColumns.endLongitude] = endLongitude
        container[CommonBinResultColumns.endAccuracy] = endAccuracy
        container[CommonBinResultColumns.endDatetime] = endDatetime
        
        container[CommonBinResultColumns.outgoingMeter] = outgoingMeter
        container[CommonBinResultColumns.incomingMeter] = incomingMeter
        container[CommonBinResultColumns.updatedDate] = updatedDate

        super.encode(to: &container)
    }
    
    func startWorkIsNull() -> Bool {
        return startLatitude == nil && startLongitude == nil
        && startAccuracy == nil && startDatetime == nil
    }
    
    func endWorkIsNull() -> Bool {
        return endLatitude == nil && endLongitude == nil
        && endAccuracy == nil && endDatetime == nil
    }
}
