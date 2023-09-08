//
//  DeliveryChartDao.swift
//  TrustarApp
//
//  Created by hoanx on 8/8/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation

enum DeliveryChartSql: SqlProtocol {
    case selectByChartCd, selectByPlaceCd, selectByAllocationRow, count, setPending, getPending, deleteSynced
    
    func makeQuery() -> String {
        switch self {
        case .selectByChartCd:
            return "select * from DeliveryChart where chartCd = :chartCd limit 1"
        case .selectByPlaceCd:
            return "select * from DeliveryChart where placeCd = :placeCd limit 1"
        case .selectByAllocationRow:
            return "select * from DeliveryChart where lastAllocationNo = :lastAllocationNo and lastAllocationRowNo = :lastAllocationRowNo limit 1"
        case .count:
            return "select count(*) from DeliveryChart"
        case .setPending:
            return "update DeliveryChart set sync = 1 where sync = 0"
        case .getPending:
            return "select * from DeliveryChart where sync = 1"
        case .deleteSynced:
            return "delete from DeliveryChart where sync = 2"
        }
    }
}

class DeliveryChartDao: BaseDbDao<UserDb> {
    func selectByChartCd(chartCd: String) -> DeliveryChart? {
        return try? executeDb.instanceDb?.read { db in
            return try? DeliveryChart.fetchOne(
                db,
                sql: DeliveryChartSql.selectByChartCd.makeQuery(),
                arguments: ["chartCd": chartCd])
        }
    }
    
    func selectByPlaceCd(placeCd: String) -> DeliveryChart? {
        return try? executeDb.instanceDb?.read { db in
            return try? DeliveryChart.fetchOne(
                db,
                sql: DeliveryChartSql.selectByPlaceCd.makeQuery(),
                arguments: ["placeCd": placeCd])
        }
    }
    
    func selectByAllocationRow(lastAllocationNo : String, lastAllocationRowNo: Int ) -> DeliveryChart? {
        do {
            return try executeDb.instanceDb?.read { db in
                return try DeliveryChart.fetchOne(
                    db,
                    sql: DeliveryChartSql.selectByAllocationRow.makeQuery(),
                    arguments: ["lastAllocationNo": lastAllocationNo, "lastAllocationRowNo" : lastAllocationRowNo])
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func count() -> Int {
        do {
            let count = try executeDb.instanceDb?.read { db in
                try Int.fetchOne(db, sql: DeliveryChartSql.count.makeQuery(), arguments: [], adapter: nil)
            }

            return count!
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return 0
        }
    }
        
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: DeliveryChartSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [DeliveryChart?] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try DeliveryChart.fetchAll(db, sql: DeliveryChartSql.getPending.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func deleteSynced() {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: DeliveryChartSql.deleteSynced.makeQuery())
        }
    }
}

