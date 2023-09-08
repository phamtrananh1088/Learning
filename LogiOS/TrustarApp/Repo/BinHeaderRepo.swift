//
//  BinHeaderRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation
import Combine

class BinHeaderRepo {
    private var userDb: UserDb
    init(_ userDb: UserDb) {
        self.userDb = userDb
    }
    
    /**
     * ダッシュボードのデータ
     */
    func list() -> [BinHeaderAndStatus]? {
        return userDb.binHeaderDao?.selectAllWithStatus()
    }
    
    func anyBinHeader(status: BinStatusEnum) -> BinHeader? {
        return userDb.binHeaderDao?.selectOneByStatus(status: status.rawValue)
    }
    
    func listPublisher() -> AnyPublisher<[BinHeaderAndStatus], Never> {
        return userDb.binHeaderDao!.selectAllWithStatusPublisher()
    }

    /**
     * 運行開始
     */
    func startBin(
            binHeader: BinHeader,
            startLocation: CLLocation?
    ) {
        if let bin = userDb.binHeaderDao?.find(allocationNo: binHeader.allocationNo) {
            bin.startBin(location: startLocation)
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
     * 運行終了
     */
    func endBin(
            binHeader: BinHeader,
            weather: WeatherEnum,
            endLocation: CLLocation?
    ) {
        userDb.binDetailDao?.unsetMoving(allocationNo: binHeader.allocationNo)
        
        if let bHeader = userDb.binHeaderDao?.find(allocationNo: binHeader.allocationNo) {
            bHeader.endBin(weather: weather, location: endLocation)
            
            do {
                try Config.Shared.userDb?.instanceDb?.write { db in
                    try bHeader.update(db)
                }
            } catch {
                debugPrint(error)
            }
            
        }
    }

    /**
     * 車両設定
     */
    func setTruck(
            binHeader: BinHeader,
            truck: Truck
    ) {
        if let bin = userDb.binHeaderDao?.find(allocationNo: binHeader.allocationNo) {
            bin.truckCd = truck.truckCd
            do {
                try userDb.instanceDb?.write { db in
                    try bin.update(db)
                }
            } catch {
                debugPrint(error)
            }
        }
    }

    func selectBinHeaderWithStatus(allocationNo: String) -> BinHeaderAndStatus? {
        return userDb.binHeaderDao?.selectBinHeaderWithStatus(allocationNo: allocationNo)
    }

    func countByStatus(status: BinStatusEnum) -> Int {
        return userDb.binHeaderDao!.countByStatus(status: status.rawValue)
    }

    /**
     * 出庫メーター
     */
    func setOutgoingMeter(
            binHeader: BinHeader,
            kilometer: Int?
    ) {
        let bh = userDb.binHeaderDao?.find(allocationNo: binHeader.allocationNo)
        bh?.outgoingMeter = kilometer
        
        try? userDb.instanceDb?.write { db in
            try? bh?.update(db)
        }
    }

    /**
     * 入庫メーター
     */
    func setIncomingMeter(
            binHeader: BinHeader,
            kilometer: Int?
    ) {
        if let binHeader = Config.Shared.userDb?.binHeaderDao?.find(allocationNo: binHeader.allocationNo) {
            binHeader.incomingMeter = kilometer
            
            try? Config.Shared.userDb?.instanceDb?.write { db in
                do {
                    try binHeader.update(db)
                } catch {
                    debugPrint(error)
                }
            }
        }
    }
}
