//
//  Work.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum WorkColumns: String, ColumnExpression {
    case workCd, workNm, displayOrder, displayFlag
}

// 作業
class Work: BaseEntity, Codable {
    var workCd: String = Resources.strEmpty
    var workNm: String = Resources.strEmpty
    var displayOrder: Int = Resources.zeroNumber
    var displayFlag: Bool = false
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"Work"}
    override class var primaryKey: String {"workCd"}
    
    required init(row: Row) {
        workCd = row[WorkColumns.workCd]
        workNm = row[WorkColumns.workNm]
        displayOrder = row[WorkColumns.displayOrder]
        displayFlag = row[WorkColumns.displayFlag]
        
        super.init(row: row)
    }
    
    init(workCd: String, workNm: String, displayOrder: Int, displayFlag: Bool) {
        self.workCd = workCd
        self.workNm = workNm
        self.displayOrder = displayOrder
        self.displayFlag = displayFlag
        
        super.init()
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[WorkColumns.workCd] = workCd
        container[WorkColumns.workNm] = workNm
        container[WorkColumns.displayOrder] = displayOrder
        container[WorkColumns.displayFlag] = displayFlag
        
        super.encode(to: &container)
    }
}
