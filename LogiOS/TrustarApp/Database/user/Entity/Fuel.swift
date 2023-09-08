//
//  Fuel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum FuelColumns: String, ColumnExpression {
    case fuelCd, fuelNm, displayOrder
}

// 燃料
class Fuel: BaseEntity, Codable {
    var fuelCd:         String = Resources.strEmpty
    var fuelNm:         String = Resources.strEmpty
    var displayOrder:   Int = Resources.zeroNumber
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"Fuel"}
    override class var primaryKey: String {"fuelCd"}
    
    required init(row: Row) {
        fuelCd = row[FuelColumns.fuelCd]
        fuelNm = row[FuelColumns.fuelNm]
        displayOrder = row[FuelColumns.displayOrder]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[FuelColumns.fuelCd] = fuelCd
        container[FuelColumns.fuelNm] = fuelNm
        container[FuelColumns.displayOrder] = displayOrder
        
        super.encode(to: &container)
    }
}
