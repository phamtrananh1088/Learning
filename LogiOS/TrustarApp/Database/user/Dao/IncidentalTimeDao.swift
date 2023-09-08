//
//  IncidentalTimeDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/06.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum IncidentalTimeSql: SqlProtocol {
    case selectAll, deleteAll
    case findByHeaderUUID, setPending, getPending, deleteSynced
    
    func makeQuery() -> String {
        switch self {
        case .selectAll:
            return "select * from IncidentalTime"
        case .deleteAll:
            return "delete from IncidentalTime"
        case .findByHeaderUUID:
            return "select * from IncidentalTime where deleted = 0 and headerUUID = :headerUUID"
        case .setPending:
            return "update IncidentalTime set sync = 1 where sync = 0"
        case .getPending:
            return "select * from IncidentalTime where sync = 1"
        case .deleteSynced:
            return "delete from IncidentalTime where sync = -1 or sync = 2"
        }
    }
}

class IncidentalTimeDao: BaseDbDao<UserDb> {
    
    func selectAll() throws -> [IncidentalTime]? {
        return try executeDb.instanceDb?.read { db in
            return try IncidentalTime.fetchAll(db,
                                        sql: IncidentalTimeSql.selectAll.makeQuery())
        }
    }
    
    func deleteAll() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: IncidentalTimeSql.deleteAll.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func findByHeaderUUID(headerUUID: String) throws -> [IncidentalTime]? {
        return try executeDb.instanceDb?.read { db in
            return try IncidentalTime.fetchAll(db,
                                               sql: IncidentalTimeSql.findByHeaderUUID.makeQuery(),
                                               arguments: ["headerUUID": headerUUID],
                                               adapter: nil)
        }
    }
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: IncidentalTimeSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() throws -> [IncidentalTime]? {
        return try executeDb.instanceDb?.inDatabase { db in
            return try IncidentalTime.fetchAll(db,
                                               sql: IncidentalTimeSql.getPending.makeQuery())
        }
    }
    
    func deleteSynced() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: IncidentalTimeSql.deleteSynced.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func insertOrReplace(list: [IncidentalTime]) {
        do {
            try executeDb.instanceDb?.write { db in
                for item in list {
                    try item.save(db)
                }
            }
        } catch {
            debugPrint(error)
        }
    }
}
