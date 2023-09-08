//
//  SensorDetectEventDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/02/21.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum SensorDetectEventSql: SqlProtocol {
    case findBy, deleteBy, allocationList, latestEventEachGroup, deleteByTimeRange, latestEvent
    
    func makeQuery() -> String {
        switch self {
        case .findBy:
            return "select * from sensor_detect where allocation_no = :allocationNo"
        case .deleteBy:
            return "delete from sensor_detect where allocation_no = :allocationNo"
        case .allocationList:
            return "select distinct allocation_no from sensor_detect"
        case .latestEventEachGroup:
            return "select *, max(event_date) from sensor_detect group by allocation_no"
        case .deleteByTimeRange:
            return "delete from sensor_detect where allocation_no = :allocationNo and event_date between :from and :to"
        case .latestEvent:
            return "select * from sensor_detect where allocation_no = :allocationNo order by event_date desc limit 1"
        }
    }
}

class SensorDetectEventDao: BaseDbDao<UserDb> {
    func findBy(allocationNo: String) -> [SensorDetectEvent] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try SensorDetectEvent.fetchAll(
                    db,
                    sql: SensorDetectEventSql.findBy.makeQuery(),
                    arguments: ["allocationNo": allocationNo])
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func deleteBy(allocationNo: String) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(
                    sql: SensorDetectEventSql.deleteBy.makeQuery(),
                    arguments: ["allocationNo": allocationNo])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func allocationList() -> [String] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try String.fetchAll(db, sql: SensorDetectEventSql.allocationList.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func latestEventEachGroup() -> [SensorDetectEvent] {
        do {
            return try executeDb.instanceDb?.read { db in
                return try SensorDetectEvent.fetchAll(db, sql: SensorDetectEventSql.latestEventEachGroup.makeQuery())
            } ?? []
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func deleteByTimeRange(allocationNo: String, from: Int64, to: Int64) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(
                    sql: SensorDetectEventSql.deleteByTimeRange.makeQuery(),
                    arguments: ["allocationNo": allocationNo, "from": from, "to": to])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func latestEvent(allocationNo: String) -> SensorDetectEvent? {
        do {
            return try executeDb.instanceDb?.read { db in
                return try SensorDetectEvent.fetchOne(db,
                                                      sql: SensorDetectEventSql.latestEvent.makeQuery(),
                                                      arguments: ["allocationNo": allocationNo])
            } ?? nil
        } catch {
            debugPrint(error)
            return nil
        }
    }
}
