//
//  NoticeDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/25.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import UIKit

enum NoticeSql: SqlProtocol {
    case markAllReadByRank, setPending, getPending, countUnreadByRank, unreadByRank, selectByRank, deleteAll
    func makeQuery() -> String {
        switch self {
        case .markAllReadByRank:
            return """
            update notice
            set unreadFlag = 0, readDatetime = {readDate}, sync = 0
            where eventRank = {rank} and unreadFlag = 1
            """
        case .setPending:
            return "update notice set sync = 1 where sync = 0 and unreadFlag = 0"
        case .getPending:
            return "select * from notice where sync = 1"
        case .countUnreadByRank:
            return "select * from notice where eventRank = {rank} and unreadFlag = 1"
        case .unreadByRank:
            return "select * from notice where eventRank = {rank} and unreadFlag = 1 order by publicationDateFrom desc"
        case .selectByRank:
            return "select * from notice where eventRank = {rank} order by publicationDateFrom desc"
        case .deleteAll:
            return "delete from notice"
        }
    }
}

class NoticeDao: BaseDbDao<UserDb> {
    
    func markAllReadByRank(rank: Int) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: NoticeSql.markAllReadByRank.makeQuery()
                                .replacingOccurrences(of: "{readDate}", with: String.init(currentTimeMillis()))
                                .replacingOccurrences(of: "{rank}", with: String.init(rank)))
            }
        } catch {
            debugPrint(error)
        }
    }

    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: NoticeSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }

    func getPending() -> [Notice]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try Notice.fetchAll(
                    db,
                    sql: NoticeSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }

    func countUnreadByRank(rank: Int) -> Int {
        do {
            let allRecord = try executeDb.instanceDb?.inDatabase { db in
                return try Notice.fetchAll(
                     db
                    ,sql: NoticeSql.countUnreadByRank.makeQuery()
                        .replacingOccurrences(of: "{rank}", with: String.init(rank)))
            }

            return allRecord!.count
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return 0
        }
    }

    func unreadByRank(rank: Int) -> [Notice]? {
        var noticeLst: [Notice]? = nil
        do {
            noticeLst = try executeDb.instanceDb?.inDatabase { db in
                return try Notice.fetchAll(
                     db
                    ,sql: NoticeSql.unreadByRank.makeQuery()
                        .replacingOccurrences(of: "{rank}", with: String.init(rank)))
            }

            return noticeLst
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return noticeLst
        }
    }

    func selectByRank(rank: Int) -> [Notice]? {
        var noticeLst: [Notice]? = nil
        do {
            noticeLst = try executeDb.instanceDb?.inDatabase { db in
                return try Notice.fetchAll(
                     db
                    ,sql: NoticeSql.selectByRank.makeQuery()
                        .replacingOccurrences(of: "{rank}", with: String.init(rank)))
            }

            return noticeLst
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return noticeLst
        }
    }

    func deleteAll() {
        
    }
}
