//
//  Kyuyu.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum KyuyuColumns: String, ColumnExpression {
    case recordId, companyCd, carrierCd, truckCd, inputDatetime, inputUserId, fuelClassCd
    case refuelingVol, refuelingPayment, latitude, longitude, allocationNo
}

// 給油
class Kyuyu: BaseEntity, Codable {
    var recordId: Int = Resources.zeroNumber
    var companyCd: String = Resources.strEmpty
    var carrierCd: String = Resources.strEmpty
    var truckCd: String = Resources.strEmpty
    var inputDatetime: String = Resources.strEmpty
    var inputUserId: String = Resources.strEmpty
    var fuelClassCd: String = Resources.strEmpty
    var refuelingVol: Double = 0
    var refuelingPayment: Int = Resources.zeroNumber
    var latitude: String = Resources.strEmpty
    var longitude: String = Resources.strEmpty
    var allocationNo: String = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"Kyuyu"}
    
    required init(row: Row) {
        recordId = row[KyuyuColumns.recordId]
        companyCd = row[KyuyuColumns.companyCd]
        carrierCd = row[KyuyuColumns.carrierCd]
        truckCd = row[KyuyuColumns.truckCd]
        inputDatetime = row[KyuyuColumns.inputDatetime]
        inputUserId = row[KyuyuColumns.inputUserId]
        fuelClassCd = row[KyuyuColumns.fuelClassCd]
        refuelingVol = row[KyuyuColumns.refuelingVol]
        refuelingPayment = row[KyuyuColumns.refuelingPayment]
        latitude = row[KyuyuColumns.latitude]
        longitude = row[KyuyuColumns.longitude]
        allocationNo = row[KyuyuColumns.allocationNo]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[KyuyuColumns.recordId] = recordId
        container[KyuyuColumns.companyCd] = companyCd
        container[KyuyuColumns.carrierCd] = carrierCd
        container[KyuyuColumns.truckCd] = truckCd
        container[KyuyuColumns.inputDatetime] = inputDatetime
        container[KyuyuColumns.inputUserId] = inputUserId
        container[KyuyuColumns.fuelClassCd] = fuelClassCd
        container[KyuyuColumns.refuelingVol] = refuelingVol
        container[KyuyuColumns.refuelingPayment] = refuelingPayment
        container[KyuyuColumns.latitude] = latitude
        container[KyuyuColumns.longitude] = longitude
        container[KyuyuColumns.allocationNo] = allocationNo
        
        super.encode(to: &container)
    }
}
