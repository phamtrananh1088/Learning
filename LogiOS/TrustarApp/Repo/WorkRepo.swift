//
//  WorkRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation

class WorkRepo {
    private var userDb: UserDb
    init(_ userDb: UserDb) {
        self.userDb = userDb
    }
    
    /**
     * 移動開始
     */
    func moveWork(binDetail: BinDetail) {
        userDb.binDetailDao?.unsetMoving(allocationNo: binDetail.allocationNo)
        userDb.binDetailDao?.setWorkStatus(workStatus: WorkStatusEnum.Moving.rawValue, allocationNo: binDetail.allocationNo, allocationRowNo: binDetail.allocationRowNo)
        userDb.binHeaderDao?.setDestination(allocationNo: binDetail.allocationNo, allocationRowNo: binDetail.allocationRowNo)
    }
    
    func autoMoveBinDetail(allocationNo: String,
                           ignoreHasWorking: Bool = false
    ) {
        if (ignoreHasWorking ||
            userDb.binDetailDao?.countByStatus(status: WorkStatusEnum.Working.rawValue,
                                               allocationNo: allocationNo) == 0) {
            let order = userDb.binDetailDao?.findByMaxEndTime(allocationNo: allocationNo)?.serviceOrder
            if let it = userDb.binDetailDao?.findNextBinDetailForWork(allocationNo: allocationNo, serviceOrder: order) {
                moveWork(binDetail: it)
            }
        }
    }

    /**
     * 作業開始、"追加作業"から
     * @return [BinDetail]
     */
    func startWork(
            binHeader: BinHeader,
            placeNm1: String?, placeNm2: String?, placeAddr: String?,
            work: Work,
            fromLocation: CLLocation?
    ) -> BinDetail {
        let allocationNo = binHeader.allocationNo
        let binHeaderDao = userDb.binHeaderDao!
        let binDetailDao = userDb.binDetailDao!
        
        binHeaderDao.setDestination(allocationNo: allocationNo, allocationRowNo: 0)
        
        let countByAllocationNo = binDetailDao.countByAllocationNo(allocationNo: allocationNo)
        let countFinished = binDetailDao.countFinished(allocationNo: allocationNo)
        
        let newBin = BinDetail.newbindetail(allocationNo: allocationNo,
                                            allocationRowNo: countByAllocationNo + 1,
                                            operationOrder: countFinished + 1,
                                            work: work,
                                            placeNm1: placeNm1, placeNm2: placeNm2, placeAddr: placeAddr)
        
        binDetailDao.unsetMoving(allocationNo: allocationNo)
        newBin.setWorkStart(location: fromLocation, time: currentTimeMillis())
        newBin.status = BinDetailEnum.Working.rawValue
        
        do {
            try userDb.instanceDb?.write { db in
                try newBin.save(db)
            }
        } catch {
            debugPrint(error)
        }
        
        return newBin
    }

    /**
     * 作業開始
     * @return [BinDetail]
     */
    func startWork(
            binDetail: BinDetail,
            work: Work,
            fromLocation: CLLocation?,
            addIfFinished: Bool,
            isAutoMode: Bool = false
    ) -> BinDetail? {
        let allocationNo = binDetail.allocationNo
        let allocationRowNo = binDetail.allocationRowNo
        if let bin: BinDetail = userDb.binDetailDao?.find(allocationNo: allocationNo, allocationRowNo: allocationRowNo) {
            userDb.binHeaderDao?.setDestination(allocationNo: allocationNo, allocationRowNo: 0)
            let plan = bin.workCd == work.workCd
            let now = currentTimeMillis()
            let finished = bin.getStatus() == .Finished
            let newAdd = addIfFinished && finished
            
            var target: BinDetail = BinDetail()
            if plan && !newAdd {
                target = bin
            } else {
                if !finished {
                    bin.status = BinDetailEnum.Ready.rawValue
                    do {
                        try userDb.instanceDb?.write { db in
                            try bin.save(db)
                        }
                    } catch {
                        debugPrint(error)
                    }
                }
                
                let countByAllocationNo = userDb.binDetailDao?.countByAllocationNo(allocationNo: allocationNo)
                let countFinished = userDb.binDetailDao?.countFinished(allocationNo: allocationNo)
                target = BinDetail(binDetail: bin, allocationRowNo: countByAllocationNo! + 1, operationOrder: countFinished! + 1, workCd: work.workCd, workNm: work.workNm)
                
                do {
                    try userDb.instanceDb?.write { db in
                       try target.save(db)
                    }
                } catch {
                    debugPrint(error)
                }
            }
            
            userDb.binDetailDao?.unsetMoving(allocationNo: allocationNo)
            target.setWorkStart(location: fromLocation, time: now)
            target.status = BinDetailEnum.Working.rawValue
            
            if plan {
                if target.checkIfEarlyDelivery(byDate: now) {
                    target.delayStatus = 1
                } else if target.checkIfDelayedDelivery(byDate: now) {
                    target.delayStatus = 2
                }
            }
        
            if isAutoMode {
                target.operatedInAutoMode()
            } else {
                if fromLocation != nil && target.checkIfMisDelivered(compare: fromLocation!) {
                    target.misdeliveryStatus = "1"
                }
            }
            
            do {
                try userDb.instanceDb?.write { db in
                    try target.update(db)
                }
            } catch {
                debugPrint(error)
            }
            
            return target
        }
        
        return nil
    }

    /**
     * 作業終了
     * @return next [BinDetail]
     */
    func endWork(
            binDetail: BinDetail,
            endLocation: CLLocation?,
            moveNext: Bool = false,
            isAutoMode: Bool = false
    ) {
        let allocationNo =  binDetail.allocationNo
        if let b = userDb.binDetailDao?.find(allocationNo: allocationNo, allocationRowNo: binDetail.allocationRowNo) {
            b.status = BinDetailEnum.Finished.rawValue
            b.setWorkEnd(location: endLocation, time: currentTimeMillis())
            if isAutoMode {
                b.operatedInAutoMode()
            }
            
            do {
                try userDb.instanceDb?.write { db in
                    try b.update(db)
                }
            } catch {
                    debugPrint(error)
            }
            
            if moveNext {
                let fromOrder = b.serviceOrder
                if fromOrder != nil {
                    let bReturn: BinDetail? = userDb.binDetailDao?.findNextBinDetailForWork(allocationNo: allocationNo, serviceOrder: fromOrder!)
                    
                    if bReturn != nil {
                        moveWork(binDetail: bReturn!)
                    }
                } else {
                    autoMoveBinDetail(allocationNo: allocationNo)
                }
            }
        }
    }
        
    func setDelayReason(binDetail: BinDetail, delayReason: DelayReason) {
        if let bin = userDb.binDetailDao?.find(allocationNo: binDetail.allocationNo, allocationRowNo: binDetail.allocationRowNo) {
            bin.delayReasonCd = delayReason.reasonCd
            do {
                try userDb.instanceDb?.write { db in
                    try bin.update(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }

    /**
     * 温度
     */
    func setTemperature(binDetail: BinDetail, tem: String) {
        if let bin = userDb.binDetailDao?.find(allocationNo: binDetail.allocationNo, allocationRowNo: binDetail.allocationRowNo) {
            bin.temperature = tem
            
            do {
                try userDb.instanceDb?.write { db in
                    try bin.update(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }
    

    /**
     * memo
     */
    func setMemo(binDetail: BinDetail, memo: String?) {
        if let bin = userDb.binDetailDao?.find(allocationNo: binDetail.allocationNo, allocationRowNo: binDetail.allocationRowNo) {
            bin.experiencePlaceNote1 = memo
            do {
                try Config.Shared.userDb?.instanceDb?.write { db in
                    try bin.update(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }

    func setLocation(binDetail: BinDetail, location: CLLocation) {
        if let bin = userDb.binDetailDao?.find(allocationNo: binDetail.allocationNo, allocationRowNo: binDetail.allocationRowNo) {
            bin.placeLatitude = location.coordinate.latitude
            bin.placeLongitude = location.coordinate.longitude
            do {
                try userDb.instanceDb?.write { db in
                    try bin.update(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }

    func workList() -> [Work]? {
        return userDb.workDao?.displayList()
    }
}
