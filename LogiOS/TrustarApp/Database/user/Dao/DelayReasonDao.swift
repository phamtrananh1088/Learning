//
//  DelayReasonDao.swift
//  TrustarApp
//
//  Created by CuongNguyen on 01/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum DelayReasonSql: SqlProtocol {
    case selectAll, deleteAll
    
    func makeQuery() -> String {
        switch self {
        case .selectAll:
            return "select * from delayReason order by displayOrder"
        case .deleteAll:
            return "delete from delayReason"
        }
    }
}

class DelayReasonDao: BaseDbDao<UserDb> {
    func selectAll() -> [DelayReason]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try DelayReason.fetchAll(
                    db,
                    sql: DelayReasonSql.selectAll.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func deleteAll() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: DelayReasonSql.deleteAll.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
}
