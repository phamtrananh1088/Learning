//
//  Notice.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum NoticeColumns: String, ColumnExpression {
    case recordId, grpRecordId, noticeCd, eventRank, publicationDateFrom, publicationDateTo, noticeTitle
    case noticeText, unreadFlag, readDatetime, companyCd, userId
}

// お知らせ
class Notice: BaseEntity, Codable {
    var recordId: String = Resources.strEmpty
    var grpRecordId: String = Resources.strEmpty
    var noticeCd: String = Resources.strEmpty
    var eventRank: Int = Resources.zeroNumber
    var publicationDateFrom: String = Resources.strEmpty
    var publicationDateTo: String = Resources.strEmpty
    var noticeTitle: String = Resources.strEmpty
    var noticeText: String = Resources.strEmpty
    var unreadFlag: Int = Resources.zeroNumber
    var readDatetime: Int64 = 0
    var companyCd: String? = nil
    var userId: String? = nil
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"Notice"}
    
    override class var primaryKey: String {"recordId"}
    
    required init(row: Row) {
        recordId = row[NoticeColumns.recordId]
        grpRecordId = row[NoticeColumns.grpRecordId]
        noticeCd = row[NoticeColumns.noticeCd]
        eventRank = row[NoticeColumns.eventRank]
        publicationDateFrom = row[NoticeColumns.publicationDateFrom]
        publicationDateTo = row[NoticeColumns.publicationDateTo]
        noticeTitle = row[NoticeColumns.noticeTitle]
        noticeText = row[NoticeColumns.noticeText]
        unreadFlag = row[NoticeColumns.unreadFlag]
        readDatetime = row[NoticeColumns.readDatetime]
        companyCd = row[NoticeColumns.companyCd]
        userId = row[NoticeColumns.userId]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[NoticeColumns.recordId] = recordId
        container[NoticeColumns.grpRecordId] = grpRecordId
        container[NoticeColumns.noticeCd] = noticeCd
        container[NoticeColumns.eventRank] = eventRank
        container[NoticeColumns.publicationDateFrom] = publicationDateFrom
        container[NoticeColumns.publicationDateTo] = publicationDateTo
        container[NoticeColumns.noticeTitle] = noticeTitle
        container[NoticeColumns.noticeText] = noticeText
        container[NoticeColumns.unreadFlag] = unreadFlag
        container[NoticeColumns.readDatetime] = readDatetime
        container[NoticeColumns.companyCd] = companyCd
        container[NoticeColumns.userId] = userId
        
        super.encode(to: &container)
    }
}
