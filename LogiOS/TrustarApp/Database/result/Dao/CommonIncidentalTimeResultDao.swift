//
//  CommonIncidentalTimeResultDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/07.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CommonIncidentalTimeResultSql: SqlProtocol {
    case setPending, getPending

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update commonIncidentalTimeResult set sync = 1 where sync = 0"
        case .getPending:
            return "select * from commonIncidentalTimeResult where sync = 1"
        }
    }
}

class CommonIncidentalTimeResultDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonIncidentalTimeResultSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonIncidentalTimeResult]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonIncidentalTimeResult.fetchAll(
                    db,
                    sql: CommonIncidentalTimeResultSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
}

