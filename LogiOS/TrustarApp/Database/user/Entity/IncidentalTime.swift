//
//  IncidentalTime.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/06.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum IncidentalTimeColumns: String, ColumnExpression {
    case uuid, headerUUID, sheetNo, sheetRowNo, type, beginDatetime, endDatetime
    case deleted
}

class IncidentalTime: BaseEntity, Codable {
    var uuid: String          = Resources.strEmpty
    var headerUUID: String    = Resources.strEmpty
    var sheetNo: String?      = nil
    var sheetRowNo: Int?      = nil
    var type: Int             = Resources.zeroNumber
    var beginDatetime: Int64? = nil { didSet { recordChanged() } }
    var endDatetime: Int64?   = nil { didSet { recordChanged() } }
    var deleted: Bool         = false { didSet { recordChanged() } }
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"IncidentalTime"}
    override class var primaryKey: String {"uuid"}
    
    required init(row: Row) {
        uuid = row[IncidentalTimeColumns.uuid]
        headerUUID = row[IncidentalTimeColumns.headerUUID]
        sheetNo = row[IncidentalTimeColumns.sheetNo]
        sheetRowNo = row[IncidentalTimeColumns.sheetRowNo]
        type = row[IncidentalTimeColumns.type]
        beginDatetime = row[IncidentalTimeColumns.beginDatetime]
        endDatetime = row[IncidentalTimeColumns.endDatetime]
        deleted = row[IncidentalTimeColumns.deleted]
        super.init(row: row)
    }
    
    init(uuid: String, headerUUID: String, sheetNo: String?, sheetRowNo: Int?, type: Int, beginDatetime: Int64?, endDatetime: Int64?) {
        self.uuid = uuid
        self.headerUUID = headerUUID
        self.sheetNo = sheetNo
        self.sheetRowNo = sheetRowNo
        self.type = type
        self.beginDatetime = beginDatetime
        self.endDatetime = endDatetime
        super.init()
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[IncidentalTimeColumns.uuid] = uuid
        container[IncidentalTimeColumns.headerUUID] = headerUUID
        container[IncidentalTimeColumns.sheetNo] = sheetNo
        container[IncidentalTimeColumns.sheetRowNo] = sheetRowNo
        container[IncidentalTimeColumns.type] = type
        container[IncidentalTimeColumns.beginDatetime] = beginDatetime
        container[IncidentalTimeColumns.endDatetime] = endDatetime
        container[IncidentalTimeColumns.deleted] = deleted
        
        super.encode(to: &container)
    }
}
