//
//  DelayReason.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum DelayReasonColumns: String, ColumnExpression {
    case reasonCd, reasonText, displayOrder
}

// 遅配理由
class DelayReason: BaseEntity, Codable {
    var reasonCd:       String = Resources.strEmpty
    var reasonText:     String = Resources.strEmpty
    var displayOrder:   Int = Resources.zeroNumber
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"DelayReason"}
    override class var primaryKey: String {"reasonCd"}
    
    required init(row: Row) {
        reasonCd = row[DelayReasonColumns.reasonCd]
        reasonText = row[DelayReasonColumns.reasonText]
        displayOrder = row[DelayReasonColumns.displayOrder]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[DelayReasonColumns.reasonCd] = reasonCd
        container[DelayReasonColumns.reasonText] = reasonText
        container[DelayReasonColumns.displayOrder] = displayOrder
        
        super.encode(to: &container)
    }
}
