//
//  WorkDao.swift
//  TrustarApp
//
//  Created by CuongNguyen on 07/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum WorkSql: SqlProtocol {
    case displayList, deleteAll, findByCd
    
    func makeQuery() -> String {
        switch self {
        case .displayList:
            return "select * from work where displayFlag = 1 order by displayOrder"
        case .deleteAll:
            return "delete from work"
        case .findByCd:
            return "select * from work where workCd = :workCd"
        }
    }
}

class WorkDao: BaseDbDao<UserDb> {
    func displayList() -> [Work]? {
        do {
            return try executeDb.instanceDb?.read { db in
                return try Work.fetchAll(
                    db,
                    sql: WorkSql.displayList.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
        
    }
    
    func deleteAll() {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: WorkSql.deleteAll.makeQuery())
        }
    }
    
    func findByCd(workCd: String) -> Work? {
        return try? executeDb.instanceDb?.read { db in
            return try? Work.fetchOne(
                db,
                sql: WorkSql.findByCd.makeQuery(),
                arguments: ["workCd": workCd])
        }
    }
}
