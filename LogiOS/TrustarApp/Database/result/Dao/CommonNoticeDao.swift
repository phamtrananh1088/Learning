//
//  CommonNoticeDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CommonNoticeSql: SqlProtocol {
    case setPending, getPending

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update commonnotice set sync = 1 where sync = 0"
        case .getPending:
            return "select * from commonnotice where sync = 1"
        }
    }
}

class CommonNoticeDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonNoticeSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonNotice]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonNotice.fetchAll(
                    db,
                    sql: CommonNoticeSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
}
