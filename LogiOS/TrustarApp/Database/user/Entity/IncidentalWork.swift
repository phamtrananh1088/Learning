//
//  IncidentalWork.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum IncidentalWorkColumns: String, ColumnExpression {
    case workCd, workNm, displayOrder
    case deleted
}

class IncidentalWork: BaseEntity, Codable, Equatable {
    static func == (lhs: IncidentalWork, rhs: IncidentalWork) -> Bool {
        return lhs.workCd == rhs.workCd
    }
    
    var workCd: String        = Resources.strEmpty
    var workNm: String?       = nil
    var displayOrder: Int     = Resources.zeroNumber
    var deleted: Bool         = false { didSet { recordChanged() } }
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"IncidentalWork"}
    override class var primaryKey: String {"workCd"}
    
    required init(row: Row) {
        workCd = row[IncidentalWorkColumns.workCd]
        workNm = row[IncidentalWorkColumns.workNm]
        displayOrder = row[IncidentalWorkColumns.displayOrder]
        deleted = row[IncidentalTimeColumns.deleted]
        super.init(row: row)
    }
    
    init(workCd: String, workNm: String?, displayOrder: Int) {
        self.workCd = workCd
        self.workNm = workNm
        self.displayOrder = displayOrder
        super.init()
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[IncidentalWorkColumns.workCd] = workCd
        container[IncidentalWorkColumns.workNm] = workNm
        container[IncidentalWorkColumns.displayOrder] = displayOrder
        container[IncidentalWorkColumns.deleted] = deleted
        
        super.encode(to: &container)
    }
}
