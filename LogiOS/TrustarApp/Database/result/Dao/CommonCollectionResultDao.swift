//
//  CommonCollectionResultDao.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CommonCollectionResultSql: SqlProtocol {
    case setPending, getPending

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update CommonCollectionResult set sync = 1 where sync = 0"
        case .getPending:
            return "select * from CommonCollectionResult where sync = 1"
        }
    }
}

class CommonCollectionResultDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonCollectionResultSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonCollectionResult]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonCollectionResult.fetchAll(
                    db,
                    sql: CommonCollectionResultSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
}
