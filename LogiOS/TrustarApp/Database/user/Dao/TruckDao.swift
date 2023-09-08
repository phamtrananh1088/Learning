//
//  TruckDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum TruckSql: SqlProtocol {
    case selectAll
    case deleteAll
    
    func makeQuery() -> String {
        switch self {
        case .selectAll:
            return "select * from truck"
        case .deleteAll:
            return "delete from truck"
        }
    }
}

class TruckDao: BaseDbDao<UserDb> {
    
    func selectAll() throws -> [Truck]! {
        try executeDb.instanceDb?.inDatabase {db in
            return try Truck.fetchAll(db, sql: TruckSql.selectAll.makeQuery())
        }
    }
    
    func deleteAll() throws {
        try executeDb.instanceDb?.write { db in
           try db.execute(sql: TruckSql.deleteAll.makeQuery())
        }
    }
}
