//
//  CommonCoordinate.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB
import CoreLocation

enum CommonCoordinateColumns: String, ColumnExpression {
    case id, companyCd, userId, truckCd, allocationNo
    case latitude, longitude, accuracy, getDatetime
    case operatingMode
}

class CommonCoordinate: BaseEntity, Codable {
    var id: Int?                     = nil
    var companyCd: String            = Resources.strEmpty
    var userId: String               = Resources.strEmpty
    var truckCd: String              = Resources.strEmpty
    var allocationNo: String         = Resources.strEmpty
    var latitude: Double             = 0
    var longitude: Double            = 0
    var accuracy: Double             = 0
    var getDatetime: Int64           = 0
    var operatingMode: String        = String()

    override init() {
        super.init()
        recordChanged()
    }

    override class var databaseTableName: String { "CommonCoordinate" }
    
    override class var primaryKey: String { "id" }

    required init(row: Row) {
        id = row[CommonCoordinateColumns.id]
        companyCd = row[CommonCoordinateColumns.companyCd]
        userId = row[CommonCoordinateColumns.userId]
        truckCd = row[CommonCoordinateColumns.truckCd]
        allocationNo = row[CommonCoordinateColumns.allocationNo]
        latitude = row[CommonCoordinateColumns.latitude]
        longitude = row[CommonCoordinateColumns.longitude]
        accuracy = row[CommonCoordinateColumns.accuracy]
        getDatetime = row[CommonCoordinateColumns.getDatetime]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[CommonCoordinateColumns.id] = id
        container[CommonCoordinateColumns.companyCd] = companyCd
        container[CommonCoordinateColumns.userId] = userId
        container[CommonCoordinateColumns.truckCd] = truckCd
        container[CommonCoordinateColumns.allocationNo] = allocationNo
        container[CommonCoordinateColumns.latitude] = latitude
        container[CommonCoordinateColumns.longitude] = longitude
        container[CommonCoordinateColumns.accuracy] = accuracy
        container[CommonCoordinateColumns.getDatetime] = getDatetime
        
        super.encode(to: &container)
    }
    
    init(location: CLLocation,
         binHeader: BinHeader,
         userInfo: UserInfo,
         isAutoMode: Bool
    ) {
        id = nil
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        truckCd = binHeader.truckCd
        allocationNo = binHeader.allocationNo
        latitude = location.coordinate.latitude
        longitude = location.coordinate.longitude
        accuracy = location.horizontalAccuracy
        getDatetime = Int64(location.timestamp.timeIntervalSince1970)
        operatingMode = isAutoMode ? "autoMode" : String()

        super.init()
        self.recordChanged()
    }

    
}
