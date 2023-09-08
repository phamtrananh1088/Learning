//
//  CommonCoordinateDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CommonCoordinateSql: SqlProtocol {
    case setPending, getPending

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update CommonCoordinate set sync = 1 where sync = 0"
        case .getPending:
            return "select * from CommonCoordinate where sync = 1"
        }
    }
}

class CommonCoordinateDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonCoordinateSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonCoordinate]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonCoordinate.fetchAll(
                    db,
                    sql: CommonCoordinateSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
}
