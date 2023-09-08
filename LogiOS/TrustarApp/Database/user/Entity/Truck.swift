//
//  Truck.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum TruckColumns: String, ColumnExpression {
    case companyCd, truckCd, truckNm, carrierCd, usageStartDate, usageEndDate
}
// 車両
class Truck: BaseEntity, Codable {
    var companyCd: String = Resources.strEmpty
    var truckCd: String = Resources.strEmpty
    var truckNm: String = Resources.strEmpty
    var carrierCd: String? = nil
    var usageStartDate: String? = nil
    var usageEndDate: String? = nil
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"Truck"}
    
    override class var primaryKey: String { "truckCd" }
    
    required init(row: Row) {
        companyCd = row[TruckColumns.companyCd]
        truckCd = row[TruckColumns.truckCd]
        truckNm = row[TruckColumns.truckNm]
        carrierCd = row[TruckColumns.carrierCd]
        usageStartDate = row[TruckColumns.usageStartDate]
        usageEndDate = row[TruckColumns.usageEndDate]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[TruckColumns.companyCd] = companyCd
        container[TruckColumns.truckCd] = truckCd
        container[TruckColumns.truckNm] = truckNm
        container[TruckColumns.carrierCd] = carrierCd
        container[TruckColumns.usageStartDate] = usageStartDate
        container[TruckColumns.usageEndDate] = usageEndDate
        super.encode(to: &container)
    }
}
