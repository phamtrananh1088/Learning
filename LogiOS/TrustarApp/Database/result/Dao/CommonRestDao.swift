//
//  CommonRestDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/05/16.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation

enum CommonRestSql: SqlProtocol {
    case setPending, getPending

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update commonRest set sync = 1 where sync = 0"
        case .getPending:
            return "select * from commonRest where sync = 1"
        }
    }
}

class CommonRestDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonRestSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonRest]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonRest.fetchAll(
                    db,
                    sql: CommonRestSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func setThenGetPending() -> [CommonRest]? {
        setPending()
        return getPending()
    }
}
