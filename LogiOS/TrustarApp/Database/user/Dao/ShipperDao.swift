//
//  ShipperDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/03.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum ShipperSql: SqlProtocol {
    case selectAll
    case deleteAll
    case findByCd
    
    func makeQuery() -> String {
        switch self {
        case .selectAll:
            return "select * from shipper"
        case .deleteAll:
            return "delete from shipper"
        case .findByCd:
            return "select * from shipper where shipper_cd = :shipperCd"
        }
    }
}

class ShipperDao: BaseDbDao<UserDb> {
    
    func selectAll() throws -> [Shipper]? {
        return try executeDb.instanceDb?.read { db in
            return try Shipper.fetchAll(db,
                                        sql: ShipperSql.selectAll.makeQuery())
        }
    }
    
    func deleteAll() throws {
        try executeDb.instanceDb?.write { db in
           try db.execute(sql: ShipperSql.deleteAll.makeQuery())
        }
    }
    
    func findByCd(shipperCd: String) throws -> Shipper? {
        return try executeDb.instanceDb?.read { db in
            return try Shipper.fetchOne(db,
                                        sql: ShipperSql.findByCd.makeQuery(),
                                        arguments: ["shipperCd": shipperCd],
                                        adapter: nil)
        }
    }
}
