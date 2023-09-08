//
//  CommonNotice.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonNoticeColumns: String, ColumnExpression {
    case companyCd, userId, recordId, grpRecordId, noticeCd, readDatetime
}

class CommonNotice: BaseEntity, Codable {
    var companyCd: String = Resources.strEmpty
    var userId: String = Resources.strEmpty
    var recordId: String = Resources.strEmpty
    var grpRecordId: String = Resources.strEmpty
    var noticeCd: String? = nil
    var readDatetime: Int64? = 0

    override init() {
        super.init()
    }

    override class var databaseTableName: String { "CommonNotice" }
    
    override class var primaryKey: String { "companyCd,userId,recordId" }

    required init(row: Row) {
        companyCd = row[CommonNoticeColumns.companyCd]
        userId = row[CommonNoticeColumns.userId]
        recordId = row[CommonNoticeColumns.recordId]
        grpRecordId = row[CommonNoticeColumns.grpRecordId]
        noticeCd = row[CommonNoticeColumns.noticeCd]
        readDatetime = row[CommonNoticeColumns.readDatetime]
        super.init(row: row)
    }
    
    init(userInfo: UserInfo, notice: Notice) {
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        recordId = notice.recordId
        grpRecordId = notice.grpRecordId
        noticeCd = notice.noticeCd
        readDatetime = notice.readDatetime

        super.init()
        self.recordChanged()
    }

    override func encode(to container: inout PersistenceContainer) {
        container[CommonNoticeColumns.companyCd] = companyCd
        container[CommonNoticeColumns.userId] = userId
        container[CommonNoticeColumns.recordId] = recordId
        container[CommonNoticeColumns.grpRecordId] = grpRecordId
        container[CommonNoticeColumns.noticeCd] = noticeCd
        container[CommonNoticeColumns.readDatetime] = readDatetime
        
        super.encode(to: &container)
    }
}
