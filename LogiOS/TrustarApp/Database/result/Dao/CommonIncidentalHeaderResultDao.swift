//
//  CommonIncidentalHeaderResultDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/07.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CommonIncidentalHeaderResultSql: SqlProtocol {
    case setPending, getPending

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update CommonIncidentalHeaderResult set sync = 1 where sync = 0"
        case .getPending:
            return "select * from CommonIncidentalHeaderResult where sync = 1"
        }
    }
}

class CommonIncidentalHeaderResultDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonIncidentalHeaderResultSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonIncidentalHeaderResult]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonIncidentalHeaderResult.fetchAll(
                    db,
                    sql: CommonIncidentalHeaderResultSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
}
