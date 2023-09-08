//
//  CommonKyuyuDao.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 14/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum CommonKyuyuSql: SqlProtocol {
    case setPending, getPending, getTotalAmount, deleteSyncedBeforeDate

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update CommonKyuyu set sync = 1 where sync = 0"
        case .getPending:
            return "select * from CommonKyuyu where sync = 1"
        case .getTotalAmount:
            return "select sum(k.refuelingVol) from CommonKyuyu k where k.companyCd = :companyCd and k.allocationNo = :allocationNo and k.fuelClassCd = :fuelClassCd"
        case .deleteSyncedBeforeDate:
            return "delete from CommonKyuyu where sync = 2 and inputDatetime < :date"
        }
    }
}

class CommonKyuyuDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonKyuyuSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [CommonKyuyu]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try CommonKyuyu.fetchAll(
                    db,
                    sql: CommonKyuyuSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func getTotalAmount(companyCd: String, allocationNo: String, fuelClassCd: String) -> Double {
        do {
            let total = try executeDb.instanceDb?.read { db in
                try Double.fetchOne(db, sql: CommonKyuyuSql.getTotalAmount.makeQuery(),
                                    arguments: ["companyCd": companyCd,
                                                "allocationNo": allocationNo,
                                                "fuelClassCd": fuelClassCd])
            }

            return total ?? 0
        } catch {
            debugPrint(error)
            return 0
        }
    }
    
    func deleteSyncedBeforeDate(date: Int64) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonKyuyuSql.deleteSyncedBeforeDate.makeQuery(),
                               arguments: ["date": date])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func insert(commonKyuyu: CommonKyuyu) {
        do {
            try executeDb.instanceDb?.write { db in
                try commonKyuyu.insert(db)
            }
        } catch {
            debugPrint(error)
        }
    }
}
