//
//  ResultDb.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/18.
//

import Foundation
import GRDB

class ResultDb: BaseDb {

    var noticeDao: CommonNoticeDao? = nil
    var workResutlDao: CommonWorkResultDao? = nil
    var incidentalHeaderResultDao : CommonIncidentalHeaderResultDao? = nil
    var incidentalTimeResultDao : CommonIncidentalTimeResultDao? = nil
    var coordinateDao : CommonCoordinateDao? = nil
    var binResultDao: CommonBinResultDao? = nil
    var commonKyuyuDao: CommonKyuyuDao? = nil
    var collectionResultDao: CommonCollectionResultDao? = nil
    var commonSensorCsvDao: CommonSensorCsvDao? = nil
    var commonRestDao: CommonRestDao? = nil
    var commonDeliveryChartDao: CommonDeliveryChartDao? = nil
    
    init(_ userName: String) throws {
        super.init()
        
        grdb = GRDBTrustar("resultDb_" + userName)

        let isDbExisted = grdb!.isDbExisted()
        
        instanceDb = try grdb!.makeDatabaseQueue()
        
        if !isDbExisted {
            initTable(table: CommonNotice())
            
            initTable(table: CommonWorkResult())
            
            initTable(table: CommonIncidentalHeaderResult())
            
            initTable(table: CommonIncidentalTimeResult())
            
            initTable(table: CommonCoordinate())
            
            initTable(table: CommonBinResult())
            
            initTable(table: CommonKyuyu())
            
            initTable(table: CommonCollectionResult())
            
            initTable(table: CommonSensorCsv(), columns: CommonSensorCsvColumns.allCases.map({($0.rawValue, $0.name())}))
            
            initTable(table: CommonRest(), columns: CommonRestColumns.allCases.map({($0.rawValue, $0.name())}))
            
            initTable(table: CommonDeliveryChart())
        }
        
        initDao()
    }

    private func initDao() {
        noticeDao = CommonNoticeDao(self)
        workResutlDao = CommonWorkResultDao(self)
        incidentalHeaderResultDao = CommonIncidentalHeaderResultDao(self)
        incidentalTimeResultDao = CommonIncidentalTimeResultDao(self)
        coordinateDao = CommonCoordinateDao(self)
        binResultDao = CommonBinResultDao(self)
        commonKyuyuDao = CommonKyuyuDao(self)
        collectionResultDao = CommonCollectionResultDao(self)
        commonSensorCsvDao = CommonSensorCsvDao(self)
        commonRestDao = CommonRestDao(self)
        commonDeliveryChartDao = CommonDeliveryChartDao(self)
    }
}
