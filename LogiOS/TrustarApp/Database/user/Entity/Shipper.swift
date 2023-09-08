//
//  Shipper.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum ShipperColumns: String, ColumnExpression {
    case shipperCd, shipperNm1, shipperNm2,shipperShortNm
}

// 荷主
class Shipper: BaseEntity, Codable, Equatable, Hashable {
    static func == (lhs: Shipper, rhs: Shipper) -> Bool {
        return lhs.shipperCd == rhs.shipperCd
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(shipperCd)
    }
    
    var shipperCd:      String = Resources.strEmpty
    var shipperNm1:     String = Resources.strEmpty
    var shipperNm2:     String? = nil
    var shipperShortNm: String = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"Shipper"}
    override class var primaryKey: String {"shipperCd"}
    
    required init(row: Row) {
        shipperCd = row[ShipperColumns.shipperCd]
        shipperNm1 = row[ShipperColumns.shipperNm1]
        shipperNm2 = row[ShipperColumns.shipperNm2]
        shipperShortNm = row[ShipperColumns.shipperShortNm]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[ShipperColumns.shipperCd] = shipperCd
        container[ShipperColumns.shipperNm1] = shipperNm1
        container[ShipperColumns.shipperNm2] = shipperNm2
        container[ShipperColumns.shipperShortNm] = shipperShortNm
        
        super.encode(to: &container)
    }
}
