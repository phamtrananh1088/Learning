//
//  DeliveryChartRepo.swift
//  TrustarApp
//
//  Created by hoanx on 8/8/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class DeliveryChartRepo {
    private var userDb: UserDb
    init(_ userDb: UserDb) {
        self.userDb = userDb
    }
        
    func findChart(binDetail: BinDetail) -> DeliveryChart? {
        var chart: DeliveryChart?
        if (!(binDetail.placeCd ?? "").isEmpty) {
            chart = userDb.deliveryChartDao?.selectByPlaceCd(placeCd: binDetail.placeCd ?? "")
        }
        
        if chart == nil && !(binDetail.chartCd ?? "").isEmpty {
            chart = userDb.deliveryChartDao?.selectByChartCd(chartCd: binDetail.chartCd ?? "")
        }
        
        if chart == nil {
            chart = userDb.deliveryChartDao?.selectByAllocationRow(lastAllocationNo: binDetail.allocationNo, lastAllocationRowNo: binDetail.allocationRowNo)
        }
        
        return chart
    }
    
    func saveDeliveryChart(deliveryChart: DeliveryChart, isCreateNew: Bool) -> Bool {
        if isCreateNew {
            if let bin = userDb.binDetailDao?.find(allocationNo: deliveryChart.lastAllocationNo, allocationRowNo: deliveryChart.lastAllocationRowNo!) {
                //bin.chartCd = deliveryChart.chartCd
                do {
                    try userDb.instanceDb?.write { db in
                        //try bin.update(db)
                        try deliveryChart.save(db)
                    }
                    return true
                } catch {
                    debugPrint(error)
                }
            }
        } else {
            do {
                try userDb.instanceDb?.write { db in
                    try deliveryChart.save(db)
                }
                return true
            } catch {
                debugPrint(error)
            }
        }
        return false
    }
}
