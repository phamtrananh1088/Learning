//
//  CommonIncidentalHeaderResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/07.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonIncidentalHeaderResultColumns: String, ColumnExpression {
    case companyCd, userId, uuid, sheetNo, allocationNo, allocationRowNo, shipperCd, workList
    case picId, deleted
}

class CommonIncidentalHeaderResult: BaseEntity, Codable {
    
    var companyCd: String           = Resources.strEmpty
    var userId: String              = Resources.strEmpty
    var uuid: String                = Resources.strEmpty
    var sheetNo: String?            = nil
    var allocationNo: String        = Resources.strEmpty
    var allocationRowNo: Int        = Resources.zeroNumber
    var shipperCd: String           = Resources.strEmpty

    // workList is List, split by "{,}"
    var workList: String            = Resources.strEmpty
    var picId: String?              = nil
    var deleted: Bool               = false
    
    override init() {
        super.init()
        recordChanged()
    }

    override class var databaseTableName: String { "CommonIncidentalHeaderResult" }
    
    override class var primaryKey: String { "companyCd,userId,uuid,allocationNo,allocationRowNo" }

    required init(row: Row) {
        companyCd = row[CommonIncidentalHeaderResultColumns.companyCd]
        userId = row[CommonIncidentalHeaderResultColumns.userId]
        uuid = row[CommonIncidentalHeaderResultColumns.uuid]
        sheetNo = row[CommonIncidentalHeaderResultColumns.sheetNo]
        allocationNo = row[CommonIncidentalHeaderResultColumns.allocationNo]
        allocationRowNo = row[CommonIncidentalHeaderResultColumns.allocationRowNo]
        shipperCd = row[CommonIncidentalHeaderResultColumns.shipperCd]
        workList = row[CommonIncidentalHeaderResultColumns.workList]
        picId = row[CommonIncidentalHeaderResultColumns.picId]
        deleted = row[CommonIncidentalHeaderResultColumns.deleted]
        
        super.init(row: row)
    }
    
    init(userInfo: UserInfo, header: IncidentalHeader) {
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        uuid = header.uuid
        sheetNo = header.sheetNo
        allocationNo = header.allocationNo
        allocationRowNo = header.allocationRowNo
        shipperCd = header.shipperCd
        workList = header.workList
        picId = header.picId
        deleted = header.deleted
        
        super.init()
        self.recordChanged()
    }

    override func encode(to container: inout PersistenceContainer) {
        container[CommonIncidentalHeaderResultColumns.companyCd] = companyCd
        container[CommonIncidentalHeaderResultColumns.userId] = userId
        container[CommonIncidentalHeaderResultColumns.uuid] = uuid
        container[CommonIncidentalHeaderResultColumns.sheetNo] = sheetNo
        container[CommonIncidentalHeaderResultColumns.allocationNo] = allocationNo
        container[CommonIncidentalHeaderResultColumns.allocationRowNo] = allocationRowNo
        container[CommonIncidentalHeaderResultColumns.shipperCd] = shipperCd
        container[CommonIncidentalHeaderResultColumns.workList] = workList
        container[CommonIncidentalHeaderResultColumns.picId] = picId
        container[CommonIncidentalHeaderResultColumns.deleted] = deleted
        
        super.encode(to: &container)
    }
}
