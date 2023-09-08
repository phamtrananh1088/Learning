//
//  BinHeaderDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import GRDB


enum BinHeaderSql: SqlProtocol {
    case find, setDestination, selectAllWithStatus, selectBinHeaderWithStatus, countByStatus
    case deleteAll, selectAll, selectOneByStatus, setPending, getPending, setSyncPendingByWorkResult, safeToDeleteList
    
    func makeQuery() -> String {
        switch self {
        case .find:
            return "select * from binHeader where allocationNo = :allocationNo"
        case .setDestination:
            return """
            update binHeader
            set destinationRowNo = :allocationRowNo, sync = 0
            where allocationNo = :allocationNo
            """
        case .selectAllWithStatus:
            return """
            select h.*, s.*, t.* from binHeader h
            join truck t on t.truckCd = h.truckCd
            join binStatus s on h.binStatus = s.binStatusCd
            order by
             case h.binStatus when 1 then 1 when 0 then 2 else 3 end,
             h.startDate
            """
        case .selectBinHeaderWithStatus:
            return """
            select h.*, s.*, t.* from binHeader h
            join truck t on t.truckCd = h.truckCd
            join binStatus s on h.binStatus = s.binStatusCd
            where h.allocationNo = :allocationNo
            """
        case .countByStatus:
            return "select count(*) from binHeader where binStatus = :status"
        case .deleteAll:
            return "delete from binHeader"
        case .selectAll:
            return "select * from binHeader"
        case .selectOneByStatus:
            return "select * from binHeader where binStatus = :status limit 1"
        case .setPending:
            return "update binHeader set sync = 1 where sync = 0"
        case .getPending:
            return "select * from binHeader where sync = 1"
        case .setSyncPendingByWorkResult:
            return """
            update binHeader set sync = 1
            where exists(
             select 1 from binDetail r
             where r.allocationNo = binHeader.allocationNo and r.sync = 1
            )
        """
        case .safeToDeleteList:
            return "select * from binHeader where sync = -1 or sync = 2"
        }
    }
}

class BinHeaderDao: BaseDbDao<UserDb> {
    
    func find(allocationNo: String) -> BinHeader? {
        
        do {
            let row = try executeDb.instanceDb?.read { db in
                return try BinHeader.fetchOne(db, sql: BinHeaderSql.find.makeQuery(), arguments: ["allocationNo": allocationNo], adapter: nil)
            }
            
            return row
            
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func setDestination(allocationNo: String, allocationRowNo: Int) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: BinHeaderSql.setDestination.makeQuery(), arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func selectAllWithStatus() -> [BinHeaderAndStatus]? {
        do {
            let rows: [Row]? = try executeDb.instanceDb?.read { db in
                let binHeaderWidth = try db.columns(in: BinHeader.databaseTableName).count
                let binHeaderAdapter = RangeRowAdapter(0 ..< binHeaderWidth)
                
                let binStatusWidth = try db.columns(in: BinStatus.databaseTableName).count
                let binStatusAdapter = RangeRowAdapter(binHeaderWidth ..< (binHeaderWidth + binStatusWidth))
                
                let truckWidth = try db.columns(in: Truck.databaseTableName).count
                let truckAdapter = RangeRowAdapter((binHeaderWidth + binStatusWidth) ..< (binHeaderWidth + binStatusWidth + truckWidth))
                
                let adapter = ScopeAdapter([
                    BinHeader.databaseTableName: binHeaderAdapter,
                    BinStatus.databaseTableName: binStatusAdapter,
                    Truck.databaseTableName: truckAdapter
                ])
                
                return try Row.fetchAll(db, sql: BinHeaderSql.selectAllWithStatus.makeQuery(), arguments: [], adapter: adapter)
            }
            
            if rows != nil {
                var listReturn: [BinHeaderAndStatus] = []
                for r in rows! {
                    listReturn.append(BinHeaderAndStatus(header: r[BinHeader.databaseTableName], truck: r[Truck.databaseTableName], status: r[BinStatus.databaseTableName]))
                }
                
                return listReturn
            } else {
                return nil
            }
        } catch {
            
            debugPrint(error)
            return nil
        }
    }
    
    func selectAllWithStatusPublisher() -> AnyPublisher<[BinHeaderAndStatus], Never> {
        return ValueObservation.tracking { db -> [Row] in
            
            let binHeaderWidth = try db.columns(in: BinHeader.databaseTableName).count
            let binHeaderAdapter = RangeRowAdapter(0 ..< binHeaderWidth)
            
            let binStatusWidth = try db.columns(in: BinStatus.databaseTableName).count
            let binStatusAdapter = RangeRowAdapter(binHeaderWidth ..< (binHeaderWidth + binStatusWidth))
            
            let truckWidth = try db.columns(in: Truck.databaseTableName).count
            let truckAdapter = RangeRowAdapter((binHeaderWidth + binStatusWidth) ..< (binHeaderWidth + binStatusWidth + truckWidth))
            
            let adapter = ScopeAdapter([
                BinHeader.databaseTableName: binHeaderAdapter,
                BinStatus.databaseTableName: binStatusAdapter,
                Truck.databaseTableName: truckAdapter
            ])
            
            return try Row.fetchAll(db, sql: BinHeaderSql.selectAllWithStatus.makeQuery(), arguments: [], adapter: adapter)
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .map { rows -> [BinHeaderAndStatus] in
            var listReturn: [BinHeaderAndStatus] = []
            for r in rows {
                listReturn.append(BinHeaderAndStatus(header: r[BinHeader.databaseTableName], truck: r[Truck.databaseTableName], status: r[BinStatus.databaseTableName]))
            }
            return listReturn
        }
        .catch({ _ in
            Empty<[BinHeaderAndStatus],Never>()
        })
        .eraseToAnyPublisher()
    }
        
    func selectBinHeaderWithStatus(allocationNo: String) -> BinHeaderAndStatus? {
        do {
            let record: Row? = try executeDb.instanceDb?.read { db in
                let binHeaderWidth = try db.columns(in: BinHeader.databaseTableName).count
                let binHeaderAdapter = RangeRowAdapter(0 ..< binHeaderWidth)
                
                let binStatusWidth = try db.columns(in: BinStatus.databaseTableName).count
                let binStatusAdapter = RangeRowAdapter(binHeaderWidth ..< (binHeaderWidth + binStatusWidth))
                
                let truckWidth = try db.columns(in: Truck.databaseTableName).count
                let truckAdapter = RangeRowAdapter((binHeaderWidth + binStatusWidth) ..< (binHeaderWidth + binStatusWidth + truckWidth))
                
                let adapter = ScopeAdapter([
                    BinHeader.databaseTableName: binHeaderAdapter,
                    BinStatus.databaseTableName: binStatusAdapter,
                    Truck.databaseTableName: truckAdapter
                ])
                
                return try Row.fetchOne(
                    db,
                    sql:
                        BinHeaderSql.selectBinHeaderWithStatus.makeQuery(),
                    arguments: ["allocationNo": allocationNo],
                    adapter: adapter)
            }
            
            if let row = record {
                return BinHeaderAndStatus(header: row[BinHeader.databaseTableName], truck: row[Truck.databaseTableName], status: row[BinStatus.databaseTableName])
            } else {
                return nil
            }
        } catch {
            
            debugPrint(error)
            return nil
        }
    }
    
    func countByStatus(status: String) -> Int{
        do {
            let count = try executeDb.instanceDb?.read { db in
                try Int.fetchOne(db, sql: BinHeaderSql.countByStatus.makeQuery(), arguments: ["status": status], adapter: nil)
            }

            return count!
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return 0
        }
    }
    
    func deleteAll() {
        
    }
    
    func selectAll() -> [BinHeader]? {
        let rows = try? executeDb.instanceDb?.inDatabase { db in
            try BinHeader.fetchAll(db, sql: BinHeaderSql.selectAll.makeQuery())
        }

        return rows
    }
    
    func selectOneByStatus(status: String) -> BinHeader? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try BinHeader.fetchOne(db,
                                              sql: BinHeaderSql.selectOneByStatus.makeQuery(),
                                              arguments: ["status": status])
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: BinHeaderSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [BinHeader]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try BinHeader.fetchAll(
                    db,
                    sql: BinHeaderSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func setSyncPendingByWorkResult() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: BinHeaderSql.setSyncPendingByWorkResult.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func safeToDeleteList() -> [BinHeader] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try BinHeader.fetchAll(db, sql: BinHeaderSql.safeToDeleteList.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
}
