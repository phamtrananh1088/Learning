//
//  IncidentalHeaderDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/06.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum IncidentalHeaderSql: SqlProtocol {

    case selectAll, deleteAll, findByUUID, selectSheetList, countSheetList, countSignedSheetList
    case setPending, getPending, deleteSynced
    
    func makeQuery() -> String {
        switch self {
            
        case .selectAll:
            return "select * from IncidentalHeader"
        case .deleteAll:
            return "delete from IncidentalHeader"
        case .findByUUID:
            return """
            select * from IncidentalHeader h
            left join shipper p on p.shipperCd = h.shipperCd
            where deleted = 0 and h.uuid = :uuid
            """
        case .selectSheetList:
            return """
            select * from IncidentalHeader h
            left join shipper p on p.shipperCd = h.shipperCd
            where deleted = 0
                and h.allocationNo = :allocationNo
                and h.allocationRowNo = :allocationRowNo
            """
        case .countSheetList:
            return """
            select count(*) from IncidentalHeader h
            where deleted = 0
                and h.allocationNo = :allocationNo
                and h.allocationRowNo = :allocationRowNo
            """
        case .countSignedSheetList:
            return """
            select count(*) from IncidentalHeader h
            where deleted = 0
                and status = 1
                and h.allocationNo = :allocationNo
                and h.allocationRowNo = :allocationRowNo
            """
        case .setPending:
            return "update IncidentalHeader set sync = 1 where sync = 0"
        case .getPending:
            return "select * from IncidentalHeader where sync = 1"
        case .deleteSynced:
            return "delete from IncidentalHeader where sync = -1 or sync = 2"
        }
    }
}

class IncidentalHeaderDao: BaseDbDao<UserDb> {

    func selectAll() throws -> [IncidentalHeader]? {
        return try executeDb.instanceDb?.read { db in
            return try IncidentalHeader.fetchAll(db,
                                        sql: IncidentalHeaderSql.selectAll.makeQuery())
        }
    }
    
    func deleteAll() {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: IncidentalHeaderSql.deleteAll.makeQuery())
        }
    }
    
    func findByUUID(uuid: String) throws -> IncidentalListItem? {
//        if let incidentalHeader =  try executeDb.instanceDb?.read({ db in
//            return try IncidentalHeader.fetchOne(db,
//                                                 sql: IncidentalHeaderSql.findByUUID.makeQuery(),
//                                                 arguments: ["uuid": uuid],
//                                                 adapter: nil)
//        })
//        {
//            return IncidentalListItem(shipper: nil, sheet: incidentalHeader)
//        }
//
//        return nil
        
        let row: Row? = try executeDb.instanceDb?.read { db in
            let incidentalHeaderWidth = try db.columns(in: IncidentalHeader.databaseTableName).count
            let incidentalHeaderAdapter = RangeRowAdapter(0 ..< incidentalHeaderWidth)
            
            let shipperWidth = try db.columns(in: Shipper.databaseTableName).count
            let shipperAdapter = RangeRowAdapter(incidentalHeaderWidth ..< incidentalHeaderWidth + shipperWidth)
            
            let adapter = ScopeAdapter([
                Shipper.databaseTableName: shipperAdapter,
                IncidentalHeader.databaseTableName: incidentalHeaderAdapter
            ])
            
            return try Row.fetchOne(db,
                                    sql: IncidentalHeaderSql.findByUUID.makeQuery(),
                                    arguments: ["uuid": uuid],
                                    adapter: adapter)
        }
        
        if row != nil {
            return IncidentalListItem(shipper: row![Shipper.databaseTableName], sheet: row![IncidentalHeader.databaseTableName])
        } else {
            return nil
        }
    }
    
    func selectSheetList(allocationNo: String, allocationRowNo: Int) throws -> [IncidentalListItem]? {
        let rows: [Row]? = try executeDb.instanceDb?.read { db in
            let incidentalHeaderWidth = try db.columns(in: IncidentalHeader.databaseTableName).count
            let incidentalHeaderAdapter = RangeRowAdapter(0 ..< incidentalHeaderWidth)
            
            let shipperWidth = try db.columns(in: Shipper.databaseTableName).count
            let shipperAdapter = RangeRowAdapter(incidentalHeaderWidth ..< incidentalHeaderWidth + shipperWidth)
            
            let adapter = ScopeAdapter([
                Shipper.databaseTableName: shipperAdapter,
                IncidentalHeader.databaseTableName: incidentalHeaderAdapter
            ])
            
            return try Row.fetchAll(db,
                                    sql: IncidentalHeaderSql.selectSheetList.makeQuery(),
                                    arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo],
                                    adapter: adapter)
        }
        
        if rows != nil {
            var listReturn: [IncidentalListItem] = []
            for r in rows! {
                listReturn.append(IncidentalListItem(shipper: r[Shipper.databaseTableName], sheet: r[IncidentalHeader.databaseTableName]))
            }
            
            return listReturn
        } else {
            return nil
        }
    }
    
    func countSheetList(allocationNo: String, allocationRowNo: Int) throws -> Int {
        let count = try executeDb.instanceDb?.read { db in
            try Int.fetchOne(db,
                             sql: IncidentalHeaderSql.countSheetList.makeQuery(),
                             arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo],
                             adapter: nil)
        }

        return count ?? 0
    }
    
    func countSignedSheetList(allocationNo: String, allocationRowNo: Int) throws -> Int {
        let count = try executeDb.instanceDb?.read { db in
            try Int.fetchOne(db,
                             sql: IncidentalHeaderSql.countSignedSheetList.makeQuery(),
                             arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo],
                             adapter: nil)
        }

        return count ?? 0
    }
    
    func setPending() {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: IncidentalHeaderSql.setPending.makeQuery())
        }
    }
    
    func getPending() throws -> [IncidentalHeader]? {
        return try executeDb.instanceDb?.inDatabase { db in
            return try IncidentalHeader.fetchAll(
                db,
                sql: IncidentalHeaderSql.getPending.makeQuery())
        }
    }
    
    func deleteSynced() {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: IncidentalHeaderSql.deleteSynced.makeQuery())
        }
    }
    
    func insertOrReplace(header: IncidentalHeader) {
        try? executeDb.instanceDb?.write { db in
            try? header.save(db)
        }
    }
}
