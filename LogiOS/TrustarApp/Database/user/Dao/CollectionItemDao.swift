//
//  CollectionItemDao.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CollectionItemSql: SqlProtocol {
    case selectAll, deleteAll
    
    func makeQuery() -> String {
        switch self {
        case .selectAll:
            return "select * from collectionGroup order by displayOrder"
        case .deleteAll:
            return "delete from collectionGroup"
        }
    }
}

class CollectionItemDao: BaseDbDao<UserDb> {
    func selectAll() -> [CollectionGroup] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try CollectionGroup.fetchAll(
                    db,
                    sql: CollectionItemSql.selectAll.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func deleteAll() {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: CollectionItemSql.deleteAll.makeQuery())
        }
    }
}
