//
//  BinDetailRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class BinDetailRepo {
    private var userDb: UserDb
    init(_ userDb: UserDb) {
        self.userDb = userDb
    }
    
    private var incidentalHeaderDao: IncidentalHeaderDao? { return userDb.incidentalHeaderDao }
    private var binDetailDao: BinDetailDao? { return userDb.binDetailDao }
    private var deliveryChartDao: DeliveryChartDao? { return userDb.deliveryChartDao }
    private var delayReasonDao: DelayReasonDao? { return userDb.delayReasonDao }
    
    func binDetailWithStatusList(allocationNo: String) -> [BinDetailAndStatus]? {
        return binDetailDao?.binDetailAndStatusListByAllocationNo(allocationNo: allocationNo)
    }

    func findBinDetail(allocationNo: String, allocationRowNo: Int) -> BinDetail? {
        return binDetailDao?.selectBinDetail(allocationNo: allocationNo, allocationRowNo: allocationRowNo)
    }

    func countByStatus(status: WorkStatusEnum, allocationNo: String) -> Int {
        return binDetailDao!.countByStatus(status: status.rawValue, allocationNo: allocationNo)
    }
    
    func findBinDetailWithDelayReason(allocationNo: String, allocationRowNo: Int) -> BinDetailData {
        // findBinDetail
        let it = Config.Shared.userDb?.binDetailDao?.selectBinDetail(allocationNo: allocationNo, allocationRowNo: allocationRowNo) ?? BinDetail()
        
        //delayReasonDao.selectAll()
        let a = Config.Shared.userDb?.delayReasonDao?.selectAll()
        // incidentalHeaderDao.countSheetList(it.allocationNo, it.allocationRowNo),
        let c = (try? Config.Shared.userDb?.incidentalHeaderDao?.countSheetList(allocationNo: allocationNo, allocationRowNo: allocationRowNo)) ?? 0
        // incidentalHeaderDao.countSignedSheetList(it.allocationNo, it.allocationRowNo),
        let sc = (try? Config.Shared.userDb?.incidentalHeaderDao?.countSignedSheetList(allocationNo: allocationNo, allocationRowNo: allocationRowNo)) ?? 0
        
        return BinDetailData(binDetail: it, delayReasons: a!, incidentalHeaderCount: c, signedIncidentalHeaderCount: sc)
    }
    
    func countByStatus(allocationNo: String, status: Int) -> AnyPublisher<Int, Never> {
        return binDetailDao!.countByStatusPublisher(allocationNo: allocationNo, status: status)
    }
    
    func countWorkingStatus(allocationNo: String) -> AnyPublisher<Bool, Never> {
        //status(0:new, 1: working, 2: finished)
        return countByStatus(allocationNo: allocationNo, status: 2)
            .map { cnt in
                return cnt == 0
            }
            .removeDuplicates()
            .eraseToAnyPublisher()
    }
}
