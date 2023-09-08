//
//  Coordinate.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum CoordinateColumns: String, ColumnExpression {
    case recordId, getDatetime, companyCd, carrierCd, truckCd, allocationNo
    case latitude, longitude, UserId, operatingMode
}

// 座標
class Coordinate: BaseEntity, Codable {
    var recordId: Int = Resources.zeroNumber
    var getDatetime: String = Resources.strEmpty
    var companyCd: String = Resources.strEmpty
    var carrierCd: String = Resources.strEmpty
    var truckCd: String = Resources.strEmpty
    var allocationNo: String = Resources.strEmpty
    var latitude: String = Resources.strEmpty
    var longitude: String = Resources.strEmpty
    var UserId: String = Resources.strEmpty
    var operatingMode: String = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"CoordinateColumns"}
    
    required init(row: Row) {
        recordId = row[CoordinateColumns.recordId]
        getDatetime = row[CoordinateColumns.getDatetime]
        companyCd = row[CoordinateColumns.companyCd]
        carrierCd = row[CoordinateColumns.carrierCd]
        truckCd = row[CoordinateColumns.truckCd]
        allocationNo = row[CoordinateColumns.allocationNo]
        latitude = row[CoordinateColumns.latitude]
        longitude = row[CoordinateColumns.longitude]
        UserId = row[CoordinateColumns.UserId]
        operatingMode = row[CoordinateColumns.operatingMode]

        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[CoordinateColumns.recordId] = recordId
        container[CoordinateColumns.getDatetime] = getDatetime
        container[CoordinateColumns.companyCd] = companyCd
        container[CoordinateColumns.carrierCd] = carrierCd
        container[CoordinateColumns.truckCd] = truckCd
        container[CoordinateColumns.allocationNo] = allocationNo
        container[CoordinateColumns.latitude] = latitude
        container[CoordinateColumns.longitude] = longitude
        container[CoordinateColumns.UserId] = UserId
        container[CoordinateColumns.operatingMode] = operatingMode
        
        super.encode(to: &container)
    }
}
