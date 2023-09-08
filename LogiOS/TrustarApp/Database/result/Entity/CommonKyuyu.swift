//
//  CommonKyuyu.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 13/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB
import CoreLocation

enum CommonKyuyuColumns: String, ColumnExpression {
    case id, companyCd, userId, allocationNo, truckCd
    case inputDatetime, inputUserId, fuelClassCd, refuelingVol, refuelingPayment
    case latitude, longitude, accuracy
}

class CommonKyuyu: BaseEntity, Codable {
    var id: Int?                     = nil
    var companyCd: String            = Resources.strEmpty
    var userId: String               = Resources.strEmpty
    var allocationNo: String         = Resources.strEmpty
    var truckCd: String              = Resources.strEmpty
    
    var inputDatetime: Int64         = 0
    var inputUserId: String          = Resources.strEmpty
    var fuelClassCd: String          = Resources.strEmpty
    var refuelingVol: Double         = 0
    var refuelingPayment: Double     = 0
    
    var latitude: Double             = 0
    var longitude: Double            = 0
    var accuracy: Double             = 0

    override init() {
        super.init()
        recordChanged()
    }

    override class var databaseTableName: String { "CommonKyuyu" }
    
    override class var primaryKey: String { "id" }

    required init(row: Row) {
        id = row[CommonKyuyuColumns.id]
        companyCd = row[CommonKyuyuColumns.companyCd]
        userId = row[CommonKyuyuColumns.userId]
        allocationNo = row[CommonKyuyuColumns.allocationNo]
        truckCd = row[CommonKyuyuColumns.truckCd]
        
        inputDatetime = row[CommonKyuyuColumns.inputDatetime]
        inputUserId = row[CommonKyuyuColumns.inputUserId]
        fuelClassCd = row[CommonKyuyuColumns.fuelClassCd]
        refuelingVol = row[CommonKyuyuColumns.refuelingVol]
        refuelingPayment = row[CommonKyuyuColumns.refuelingPayment]
        
        latitude = row[CommonKyuyuColumns.latitude]
        longitude = row[CommonKyuyuColumns.longitude]
        accuracy = row[CommonKyuyuColumns.accuracy]

        super.init(row: row)
    }
    
    init(userInfo: UserInfo, refueled: Refueled, location: CLLocation?) {
        id = nil
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        allocationNo = refueled.binHeader.allocationNo
        truckCd = refueled.binHeader.truckCd
        
        inputDatetime = currentTimeMillis()
        inputUserId = userInfo.userId
        fuelClassCd = refueled.fuel.fuelCd
        refuelingVol = refueled.quantity
        refuelingPayment = refueled.paid
        
        latitude = location?.coordinate.latitude ?? 0.0
        longitude = location?.coordinate.longitude ?? 0.0
        accuracy = location?.horizontalAccuracy ?? 0

        super.init()
        self.recordChanged()
    }

    override func encode(to container: inout PersistenceContainer) {
        container[CommonKyuyuColumns.id] = id
        container[CommonKyuyuColumns.companyCd] = companyCd
        container[CommonKyuyuColumns.userId] = userId
        container[CommonKyuyuColumns.allocationNo] = allocationNo
        container[CommonKyuyuColumns.truckCd] = truckCd
        
        container[CommonKyuyuColumns.inputDatetime] = inputDatetime
        container[CommonKyuyuColumns.inputUserId] = inputUserId
        container[CommonKyuyuColumns.fuelClassCd] = fuelClassCd
        container[CommonKyuyuColumns.refuelingVol] = refuelingVol
        container[CommonKyuyuColumns.refuelingPayment] = refuelingPayment
        
        container[CommonKyuyuColumns.latitude] = latitude
        container[CommonKyuyuColumns.longitude] = longitude
        container[CommonKyuyuColumns.accuracy] = accuracy
        
        super.encode(to: &container)
    }
}
