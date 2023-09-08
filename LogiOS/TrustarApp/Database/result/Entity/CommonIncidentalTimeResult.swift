//
//  CommonIncidentalTimeResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/07.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonIncidentalTimeResultColumns: String, ColumnExpression {
    case companyCd, userId, uuid, headerUUID, sheetNo, sheetRowNo, type, beginDatetime, endDatetime
    case deleted
}

class CommonIncidentalTimeResult: BaseEntity, Codable {
    
    var companyCd: String           = Resources.strEmpty
    var userId: String              = Resources.strEmpty
    var uuid: String                = Resources.strEmpty
    var headerUUID: String          = Resources.strEmpty
    var sheetNo: String?            = nil
    var sheetRowNo: Int?            = nil
    var type: Int                   = Resources.zeroNumber
    var beginDatetime: Int64?       = nil
    var endDatetime: Int64?         = nil
    var deleted: Bool               = false
    
    override init() {
        super.init()
        recordChanged()
    }

    override class var databaseTableName: String { "CommonIncidentalTimeResult" }
    
    override class var primaryKey: String { "companyCd,userId,uuid" }

    required init(row: Row) {
        companyCd = row[CommonIncidentalTimeResultColumns.companyCd]
        userId = row[CommonIncidentalTimeResultColumns.userId]
        uuid = row[CommonIncidentalTimeResultColumns.uuid]
        headerUUID = row[CommonIncidentalTimeResultColumns.headerUUID]
        sheetNo = row[CommonIncidentalTimeResultColumns.sheetNo]
        sheetRowNo = row[CommonIncidentalTimeResultColumns.sheetRowNo]
        type = row[CommonIncidentalTimeResultColumns.type]
        beginDatetime = row[CommonIncidentalTimeResultColumns.beginDatetime]
        endDatetime = row[CommonIncidentalTimeResultColumns.endDatetime]
        deleted = row[CommonIncidentalTimeResultColumns.deleted]
        
        super.init(row: row)
    }
    
    init(userInfo: UserInfo, time: IncidentalTime) {
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        uuid = time.uuid
        headerUUID = time.headerUUID
        sheetNo = time.sheetNo
        sheetRowNo = time.sheetRowNo
        type = time.type
        beginDatetime = time.beginDatetime
        endDatetime = time.endDatetime
        deleted = time.deleted
        
        super.init()
        self.recordChanged()
    }

    override func encode(to container: inout PersistenceContainer) {
        container[CommonIncidentalTimeResultColumns.companyCd] = companyCd
        container[CommonIncidentalTimeResultColumns.userId] = userId
        container[CommonIncidentalTimeResultColumns.uuid] = uuid
        container[CommonIncidentalTimeResultColumns.headerUUID] = headerUUID
        container[CommonIncidentalTimeResultColumns.sheetNo] = sheetNo
        container[CommonIncidentalTimeResultColumns.sheetRowNo] = sheetRowNo
        container[CommonIncidentalTimeResultColumns.type] = type
        container[CommonIncidentalTimeResultColumns.beginDatetime] = beginDatetime
        container[CommonIncidentalTimeResultColumns.endDatetime] = endDatetime
        container[CommonIncidentalTimeResultColumns.deleted] = deleted
        
        super.encode(to: &container)
    }
}
