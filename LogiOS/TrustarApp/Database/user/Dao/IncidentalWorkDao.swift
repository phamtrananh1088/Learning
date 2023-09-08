//
//  IncidentalWorkDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/03.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum IncidentalWorkSql: SqlProtocol {
    case selectAll, deleteAll
    
    func makeQuery() -> String {
        switch self {
        case .selectAll:
            return "select * from IncidentalWork"
        case .deleteAll:
            return "delete from IncidentalWork"
        }
    }
}

class IncidentalWorkDao: BaseDbDao<UserDb> {
    
    func selectAll() throws -> [IncidentalWork]? {
        return try executeDb.instanceDb?.read { db in
            return try IncidentalWork.fetchAll(db,
                                        sql:IncidentalWorkSql.selectAll.makeQuery())
        }
    }
    
    func deleteAll() {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: IncidentalWorkSql.deleteAll.makeQuery())
        }
    }}
