//
//  CollectionResultDao.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB
import Combine

enum CollectionResultSql: SqlProtocol {
    case setPending, getPending, getResult, safeToDeleteList
    
    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update collectionResult set sync = 1 where sync = 0"
        case .getPending:
            return "select * from collectionResult where sync = 1"
        case .getResult:
            return "select * from collectionResult where allocationNo = :allocationNo and allocationRowNo = :allocationRowNo"
        case .safeToDeleteList:
            return "select * from collectionResult where sync = -1 or sync = 2"
        }
    }
}

class CollectionResultDao: BaseDbDao<UserDb> {
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CollectionResultSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CollectionResult] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try CollectionResult.fetchAll(db, sql: CollectionResultSql.getPending.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func getResult(allocationNo: String, allocationRowNo: Int) -> CollectionResult? {
        do {
            return try executeDb.instanceDb?.read { db in
                return try CollectionResult.fetchOne(db,
                                                     sql: CollectionResultSql.getResult.makeQuery(),
                                                     arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo],
                                                     adapter: nil)
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func getResult(allocationNo: String, allocationRowNo: Int) -> AnyPublisher<CollectionResult?, Error> {
        return ValueObservation.tracking { db in
            try CollectionResult.fetchOne(
                 db
                ,sql: CollectionResultSql.getResult.makeQuery(),
                 arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo],
                 adapter: nil)
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .map {$0}
        .eraseToAnyPublisher()
    }
    
    func safeToDeleteList() -> [CollectionResult] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try CollectionResult.fetchAll(db, sql: CollectionResultSql.safeToDeleteList.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
}
