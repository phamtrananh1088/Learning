//
//  CommonDeliveryChart.swift
//  TrustarApp
//
//  Created by hoanx on 8/9/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation

enum CommonDeliveryChartDaoSql: SqlProtocol {
    case setPending, getPending, getAnyPending, deleteSynced

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update CommonDeliveryChart set sync = 1 where sync = 0"
        case .getPending:
            return "select * from CommonDeliveryChart where sync = 1"
        case .getAnyPending:
            return "elect * from CommonDeliveryChart where sync = 1 limit 1"
        case .deleteSynced:
            return "delete from CommonDeliveryChart where sync = 2"
        }
    }
}

class CommonDeliveryChartDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonDeliveryChartDaoSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonDeliveryChart]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonDeliveryChart.fetchAll(
                    db,
                    sql: CommonDeliveryChartDaoSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func getAnyPending() -> CommonDeliveryChart? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonDeliveryChart.fetchOne(
                    db,
                    sql: CommonDeliveryChartDaoSql.getAnyPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func deleteSynced() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonDeliveryChartDaoSql.deleteSynced.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
}
