//
//  fuelDao.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 14/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
enum FuelSql: SqlProtocol {
    case list, deleteAll
    
    func makeQuery() -> String {
        switch self {
        case .list:
            return "select * from fuel"
        case .deleteAll:
            return "delete from fuel"
        }
    }
}

class FuelDao: BaseDbDao<UserDb> {
    func list() -> [Fuel]? {
        do {
            return try executeDb.instanceDb?.read { db in
                return try Fuel.fetchAll(
                    db,
                    sql: FuelSql.list.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
        
    }
    
    func deleteAll() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: FuelSql.deleteAll.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
}
