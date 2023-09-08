//
//  BinDetailDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import GRDB

enum BinDetailSql: SqlProtocol {
    case find
    case binDetailAndStatusListByAllocationNo
    case setWorkStatus
    case unsetMoving
    case countByAllocationNo
    case countFinished
    case selectBinDetail
    case countByStatus
    case setPending
    case getPending
    case safeToDeleteList
    case findByMaxEndTime
    case findNextBinDetailForWork
    case countByStatus2
    
    func makeQuery() -> String {
        switch self {
        case .find:
            return "select * from binDetail where allocationNo = :allocationNo and allocationRowNo = :allocationRowNo"
        case .binDetailAndStatusListByAllocationNo:
            return """
                select ws.*, d.*
                from binDetail d
                join workStatus ws on ws.workStatusCd = d.status
                where d.allocationNo = :allocationNo
                and exists (
                 select 1 from binHeader h where h.allocationNo = d.allocationNo
                )
                order by
                    case d.status when 1 then 1 when 2 then 2 when 0 then 3 when 3 then 4 end,
                    case when d.status = 3 then 1 else d.serviceOrder end,
                    case when d.status = 3 then d.startDate else d.appointedDateFrom end
            """
        case .setWorkStatus:
            return "update binDetail set status = :workStatus where allocationNo = :allocationNo and allocationRowNo = :allocationRowNo"
        case .unsetMoving:
            return "update binDetail set status = 0 where status = 1 and allocationNo = {allocationNo}"
        case .countByAllocationNo:
            return "select count(*) from binDetail where allocationNo = :allocationNo"
        case .countFinished:
            return "select count(*) from binDetail where allocationNo = :allocationNo and status = 3"
        case .selectBinDetail:
            return "select * from binDetail where allocationNo = :allocationNo and allocationRowNo = :allocationRowNo"
        case .countByStatus:
            return """
                select count(*) from binDetail
                where status = :status and allocationNo = :allocationNo
            """
        case .setPending:
            return "update binDetail set sync = 1 where sync = 0"
        case .getPending:
            return "select * from binDetail where sync = 1"
        case .safeToDeleteList:
            return "select * from binDetail where sync = -1 or sync = 2"
        case .findByMaxEndTime:
            return """
                select *, max(endDate) from binDetail d
                where exists (select 1 from binHeader r where r.allocationNo = d.allocationNo)
                    and allocationNo = :allocationNo
                    and endDate is not null
                    and serviceOrder is not null
                    and status = 3
                group by allocationNo
            """
        case .findNextBinDetailForWork:
            return """
                select *, min(serviceOrder) from binDetail d
                where exists (select 1 from binHeader r where r.allocationNo = d.allocationNo)
                    and allocationNo = :allocationNo
                    and (status = 0 or status == 1)
                    and (:fromServiceOrder is null or serviceOrder >= :fromServiceOrder)
                group by allocationNo
            """
        case .countByStatus2:
            return """
                select count(*) from binDetail
                where status = :status and allocationNo = :allocationNo
            """
        }
    }
}

class BinDetailDao: BaseDbDao<UserDb> {
    
    func find(allocationNo: String, allocationRowNo: Int) -> BinDetail? {
        do {
            return try executeDb.instanceDb?.read { db in
                return try BinDetail.fetchOne(db, sql: BinDetailSql.find.makeQuery(), arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo], adapter: nil)
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func binDetailAndStatusListByAllocationNo(allocationNo: String) -> [BinDetailAndStatus]! {
        do {
            let rows: [Row]? = try executeDb.instanceDb?.read { db in
                let workStatusWidth = try db.columns(in: WorkStatus.databaseTableName).count
                let workStatusAdapter = RangeRowAdapter(0 ..< workStatusWidth)
                
                let binDetailWidth = try db.columns(in: BinDetail.databaseTableName).count
                let binDetailAdapter = RangeRowAdapter(workStatusWidth ..< (workStatusWidth + binDetailWidth))
                
                let adapter = ScopeAdapter([
                    WorkStatus.databaseTableName: workStatusAdapter,
                    BinDetail.databaseTableName: binDetailAdapter
                ])
                
                return try Row.fetchAll(db,
                                        sql:
                                            BinDetailSql.binDetailAndStatusListByAllocationNo.makeQuery(),
                                        arguments: ["allocationNo": allocationNo],
                                        adapter: adapter)
            }
            
            if rows != nil {
                var listReturn: [BinDetailAndStatus] = []
                for r in rows! {
                    listReturn.append(BinDetailAndStatus(detail: r[BinDetail.databaseTableName], status: r[WorkStatus.databaseTableName]))
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
    
    func setWorkStatus(workStatus: String, allocationNo: String, allocationRowNo: Int) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: BinDetailSql.setWorkStatus.makeQuery(), arguments: ["workStatus":workStatus, "allocationNo": allocationNo, "allocationRowNo": allocationRowNo])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func unsetMoving(allocationNo: String) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: BinDetailSql.unsetMoving.makeQuery().replacingOccurrences(of: "{allocationNo}", with: allocationNo))
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func countByAllocationNo(allocationNo: String) -> Int {
        do {
            let count = try executeDb.instanceDb?.read { db in
                try Int.fetchOne(db, sql: BinDetailSql.countByAllocationNo.makeQuery(), arguments: ["allocationNo": allocationNo], adapter: nil)
            }

            return count!
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return 0
        }
    }
    
    func countFinished(allocationNo: String) -> Int{
        do {
            let count = try executeDb.instanceDb?.read { db in
                try Int.fetchOne(db, sql: BinDetailSql.countFinished.makeQuery(), arguments: ["allocationNo": allocationNo], adapter: nil)
            }

            return count!
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return 0
        }
    }
    
    func selectBinDetail(allocationNo: String, allocationRowNo: Int) -> BinDetail? {
        do {
            return try executeDb.instanceDb?.read { db in
                try BinDetail.fetchOne(db, sql: BinDetailSql.selectBinDetail.makeQuery(), arguments: ["allocationNo": allocationNo, "allocationRowNo": allocationRowNo], adapter: nil)
            }
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return nil
        }
    }
    
    func countByStatus(status: String, allocationNo: String) -> Int {
        do {
            let count = try executeDb.instanceDb?.read { db in
                try Int.fetchOne(db, sql: BinDetailSql.countByStatus.makeQuery(), arguments: ["status": status, "allocationNo": allocationNo], adapter: nil)
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
                try db.execute(sql: BinDetailSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPending() -> [BinDetail]? {
        do {
            return try executeDb.instanceDb?.inDatabase { db in
                return try BinDetail.fetchAll(
                    db,
                    sql: BinDetailSql.getPending.makeQuery())
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func safeToDeleteList() -> [BinDetail] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try BinDetail.fetchAll(db, sql: BinDetailSql.safeToDeleteList.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func findByMaxEndTime(allocationNo: String) -> BinDetail? {
        do {
            return try executeDb.instanceDb?.read { db in
                return try BinDetail.fetchOne(db,
                                              sql: BinDetailSql.findByMaxEndTime.makeQuery(),
                                              arguments: ["allocationNo": allocationNo])
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func findNextBinDetailForWork(allocationNo: String, serviceOrder: Int?) -> BinDetail? {
        do {
            return try executeDb.instanceDb?.inDatabase {db in
                return try BinDetail.fetchOne(db, sql: BinDetailSql.findNextBinDetailForWork.makeQuery(), arguments: ["allocationNo": allocationNo, "fromServiceOrder": serviceOrder], adapter: nil)
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func countByStatus(allocationNo: String, status: Int) -> Int {
        do {
            let count = try executeDb.instanceDb?.read { db in
                try Int.fetchOne(db, sql: BinDetailSql.countByStatus2.makeQuery(), arguments: ["allocationNo": allocationNo, "status": status], adapter: nil)
            }

            return count!
        } catch {
            #if DEBUG
            debugPrint(error)
            #endif
            
            return 0
        }
    }
    
    func countByStatusPublisher(allocationNo: String, status: Int) -> AnyPublisher<Int, Never> {
        return ValueObservation.tracking { db in
            try Int.fetchOne(db, sql: BinDetailSql.countByStatus2.makeQuery(), arguments: ["allocationNo": allocationNo, "status": status], adapter: nil)!
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .catch({ _ in
            Empty<Int,Never>()
        })
        .eraseToAnyPublisher()
    }
}
