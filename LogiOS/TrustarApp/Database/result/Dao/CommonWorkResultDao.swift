//
//  CommonWorkResultDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/31.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CommonWorkResultSql: SqlProtocol {
    case setPending

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update commonWorkResult set sync = 1 where sync = 0"
        }
    }
}

class CommonWorkResultDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonWorkResultSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
}
