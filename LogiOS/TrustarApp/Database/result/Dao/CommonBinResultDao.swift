//
//  CommonBinResultDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/03.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonBinResultSql: SqlProtocol {
    case setPending, getPendingWithWorkResult

    func makeQuery() -> String {
        switch self {
        case .setPending:
            return "update commonBinResult set sync = 1 where sync = 0"
        case .getPendingWithWorkResult:
            return """
            select
                 b.*
                ,w.*
          from commonBinResult b
          left outer join commonWorkResult w
           on b.allocationNo = w.allocationNo
            and b.companyCd = w.companyCd
            and b.userId = w.userId
            and w.sync = 1
          where b.sync = 1 or w.sync = 1
        """
        }
    }
}

class CommonBinResultDao: BaseDbDao<ResultDb> {
    
    func setPending() {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: CommonBinResultSql.setPending.makeQuery())
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func getPendingWithWorkResult() -> [BinResultWithWorkResult]? {
        do {
            let rows: [Row]? = try executeDb.instanceDb?.read { db in
                let commonBinResultWidth = try db.columns(in: CommonBinResult.databaseTableName).count
                let commonBinResultAdapter = RangeRowAdapter(0 ..< commonBinResultWidth)
                
                let commonWorkResultWidth = try db.columns(in: CommonWorkResult.databaseTableName).count
                let commonWorkResultAdapter = RangeRowAdapter(commonBinResultWidth ..< (commonBinResultWidth + commonWorkResultWidth))
                
                let adapter = ScopeAdapter([
                    CommonBinResult.databaseTableName: commonBinResultAdapter,
                    CommonWorkResult.databaseTableName: commonWorkResultAdapter,
                ])
                
                return try Row.fetchAll(db, sql: CommonBinResultSql.getPendingWithWorkResult.makeQuery(), arguments: [], adapter: adapter)
            }
            
            if rows != nil {
                var listReturn: [BinResultWithWorkResult] = []
                for r in rows! {
                    listReturn.append(BinResultWithWorkResult(binResults: r[CommonBinResult.databaseTableName], workResults: r[CommonWorkResult.databaseTableName]))
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
}
