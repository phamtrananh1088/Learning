//
//  UserDb.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/20.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

class UserDb: BaseDb {
    
    var binDetailDao: BinDetailDao? = nil
    var binHeaderDao: BinHeaderDao? = nil
    var truckDao: TruckDao? = nil
    var noticeDao: NoticeDao? = nil
    var incidentalHeaderDao: IncidentalHeaderDao? = nil
    var incidentalWorkDao: IncidentalWorkDao? = nil
    var incidentalTimeDao: IncidentalTimeDao? = nil
    var delayReasonDao: DelayReasonDao? = nil
    var workDao: WorkDao? = nil
    var shipperDao: ShipperDao? = nil
    var fuelDao: FuelDao? = nil
    var collectionItemDao: CollectionItemDao? = nil
    var collectionResultDao: CollectionResultDao? = nil
    var sensorDetectEventDao: SensorDetectEventDao? = nil
    var deliveryChartDao: DeliveryChartDao? = nil
        
    init(_ userName: String) throws {
        super.init()
        grdb = GRDBTrustar(userName)

        let isDbExisted = grdb!.isDbExisted()
        
        instanceDb = try grdb!.makeDatabaseQueue()

        if !isDbExisted {
            initTable(table: BinHeader())

            initTable(table: Truck())

            initTable(table: WorkStatus())
            
            initTable(table: BinStatus())
            
            initTable(table: Work())
            
            initTable(table: Fuel())
            
            initTable(table: Shipper())
            
            initTable(table: DelayReason())
            
            initTable(table: CollectionGroup())
            
            initTable(table: Notice())
            
            initTable(table: WorkPlace())
            
            initTable(table: BinDetail())
            
            initTable(table: IncidentalHeader())
            
            initTable(table: CollectionResult())

            initTable(table: IncidentalTime())
            
            initTable(table: IncidentalWork())
            
            initTable(table: SensorDetectEvent(), columns: SensorDetectEventColumns.allCases.map({($0.rawValue, $0.name())}))
            
            initTable(table: DeliveryChart())
        }
        
        initDao()
    }

    private func initDao() {
        binDetailDao = BinDetailDao(self)
        binHeaderDao = BinHeaderDao(self)
        truckDao = TruckDao(self)
        noticeDao = NoticeDao(self)
        incidentalHeaderDao = IncidentalHeaderDao(self)
        incidentalWorkDao = IncidentalWorkDao(self)
        incidentalTimeDao = IncidentalTimeDao(self)
        delayReasonDao = DelayReasonDao(self)
        workDao = WorkDao(self)
        shipperDao = ShipperDao(self)
        fuelDao = FuelDao(self)
        collectionItemDao = CollectionItemDao(self)
        collectionResultDao = CollectionResultDao(self)
        sensorDetectEventDao = SensorDetectEventDao(self)
        deliveryChartDao = DeliveryChartDao(self)
    }
}
