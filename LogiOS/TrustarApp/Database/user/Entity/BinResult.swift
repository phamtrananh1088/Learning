//
//  BinResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum BinResultColumns: String, ColumnExpression {
    case allocationNo, carrierCd, truckCd, weather, workMode, startLatitude
    case startLongitude, startDatetime, endLatitude, endLongitude, endDatetime, destinationRowNo
    case updatedDate, companyCd, UserId, outgoingMeter, incomingMeter
}

// 運行実績
class BinResult: BaseEntity, Codable {
    var allocationNo: String = Resources.strEmpty
    var carrierCd: String = Resources.strEmpty
    var truckCd: String = Resources.strEmpty
    var weather: String = Resources.strEmpty
    var workMode: String = Resources.strEmpty
    var startLatitude: String = Resources.strEmpty
    var startLongitude: String = Resources.strEmpty
    var startDatetime: String = Resources.strEmpty
    var endLatitude: String = Resources.strEmpty
    var endLongitude: String = Resources.strEmpty
    var endDatetime: String = Resources.strEmpty
    var destinationRowNo: Int = Resources.zeroNumber
    var updatedDate: String = Resources.strEmpty
    var companyCd: String = Resources.strEmpty
    var UserId: String = Resources.strEmpty
    var outgoingMeter: Int = Resources.zeroNumber
    var incomingMeter: Int = Resources.zeroNumber

    override init() {
        super.init()
    }

    override class var databaseTableName: String {"BinResult"}

    required init(row: Row) {
        allocationNo = row[BinResultColumns.allocationNo]
        carrierCd = row[BinResultColumns.carrierCd]
        truckCd = row[BinResultColumns.truckCd]
        weather = row[BinResultColumns.weather]
        workMode = row[BinResultColumns.workMode]
        startLatitude = row[BinResultColumns.startLatitude]
        startLongitude = row[BinResultColumns.startLongitude]
        startDatetime = row[BinResultColumns.startDatetime]
        endLatitude = row[BinResultColumns.endLatitude]
        endLongitude = row[BinResultColumns.endLongitude]
        endDatetime = row[BinResultColumns.endDatetime]
        destinationRowNo = row[BinResultColumns.destinationRowNo]
        updatedDate = row[BinResultColumns.updatedDate]
        companyCd = row[BinResultColumns.companyCd]
        UserId = row[BinResultColumns.UserId]
        outgoingMeter = row[BinResultColumns.outgoingMeter]
        incomingMeter = row[BinResultColumns.incomingMeter]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[BinResultColumns.allocationNo] = allocationNo
        container[BinResultColumns.carrierCd] = carrierCd
        container[BinResultColumns.truckCd] = truckCd
        container[BinResultColumns.weather] = weather
        container[BinResultColumns.workMode] = workMode
        container[BinResultColumns.startLatitude] = startLatitude
        container[BinResultColumns.startLongitude] = startLongitude
        container[BinResultColumns.startDatetime] = startDatetime
        container[BinResultColumns.endLatitude] = endLatitude
        container[BinResultColumns.endLongitude] = endLongitude
        container[BinResultColumns.endDatetime] = endDatetime
        container[BinResultColumns.destinationRowNo] = destinationRowNo
        container[BinResultColumns.updatedDate] = updatedDate
        container[BinResultColumns.companyCd] = companyCd
        container[BinResultColumns.UserId] = UserId
        container[BinResultColumns.outgoingMeter] = outgoingMeter
        container[BinResultColumns.incomingMeter] = incomingMeter
        
        super.encode(to: &container)
    }
}
