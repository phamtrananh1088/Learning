//
//  BinLocationTask.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation
import Combine
import CoreMotion
import GRDB

class BinLocationTask {
    
    class BinState {
        var running: Bool
        var location: CLLocation? = nil
        
        init(running: Bool, location: CLLocation? = nil) {
            self.running = running
            self.location = location
        }
    }
    
    @Published private var workingBinHeaders =
    Current.Shared.userRepository!.binHeaderRepo.listPublisher()
        .map({ lst in
            return lst.filter { item in
                item.status.getStatus() == .Working
            }
            .map { it in
                return it.header
            }
        })
        .removeDuplicates()
        .eraseToAnyPublisher()
        .shareReplay(1)
    
    private var locationPublisher = PassthroughSubject<CLLocation, Never>()
    private var bag = Set<AnyCancellable>()
    private var userInfo: UserInfo
    
    init(userInfo: UserInfo) {
        self.userInfo = userInfo
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Location.rawValue), object: nil)

        _ = $autoStart
            .subscribe(on: DispatchQueue.main)
            .scan(Optional<BinDetailAndStatus>(BinDetailAndStatus(detail: BinDetail(), status: WorkStatus()))) { [self] (acc, e) in
                let inRange = e?.filter({!($0.status.getStatus() == .Working || $0.inRange != true)})
                let b = acc != nil
                ? inRange?.first(where: { $0.binDetailAndStatus.sameItem(acc!) })
                : inRange?
                    .sorted(by: { getServiceOrder(binDetailAndStatus: $0.binDetailAndStatus) < getServiceOrder(binDetailAndStatus: $1.binDetailAndStatus)})
                    .sorted(by: { getWorkStatusOrder(wStatus: $0.status.getStatus()) < getWorkStatusOrder(wStatus: $1.status.getStatus()) })
                    .first

                return b?.binDetailAndStatus
            }
            .removeDuplicates(by: {b1, b2 -> Bool in
                let r = (b1 != nil && b2 != nil && b1!.sameItem(b2!)) || (b1 == nil && b2 == nil)
                return r
            })
            .map { data -> AnyPublisher<BinDetail?, Never> in
                let detail = data?.detail
                if detail == nil {
                    return Just(nil)
                        .eraseToAnyPublisher()
                        
                } else {
                    let delayInSecond = Double(BinDetail.stayTimeDelayinmin(binDetail: detail!)) * 60
                    return Just(detail)
                        .delay(for: .seconds(delayInSecond), scheduler: DispatchQueue.main)
                        .eraseToAnyPublisher()
                }
            }
            .switchToLatest()
            .subscribe(on: DispatchQueue.main)
            .retry(Int.max)
            .sink(receiveValue: { detail in
                let w = detail?.workCd
                if w != nil {
                    let workRepo = Current.Shared.userRepository?.workRepo
                    let _ = workRepo?.startWork(binDetail: detail!,
                                        work: Work(workCd: detail!.workCd!,
                                                   workNm: detail!.workNm ?? String(),
                                                   displayOrder: 0,
                                                   displayFlag: false),
                                        fromLocation: Current.Shared.lastLocation,
                                        addIfFinished: true,
                                        isAutoMode: true)
                    Helper.Shared.sendBroadcastNotification(.Bin)
                    Current.Shared.syncBin() {(_,_) in }
                }
            })
        
        let user = self.userInfo
        //休憩判定
        restEvent(cancelableBags: &bag,
                  working: workingBinHeaders,
                  location: locationPublisher.eraseToAnyPublisher(),
                  user: user)
            .sink { (allocationNo, r) in
                if (r is DetectRestEvent.MoveDetected) {
                    let r2 = (r as! DetectRestEvent.MoveDetected)
                    let c = CommonRest(userInfor: user,
                                       allocationNo: allocationNo,
                                       startLocation: r2.rest.startRestLocation,
                                       endDate: r2.restEndTimeBasedOsDate(),
                                       elapsed: r2.elapsedRestMillis())
                    do {
                        try Config.Shared.resultDb?.instanceDb?.write { db in
                            try c.insert(db)
                        }
                    } catch {
                        debugPrint(error)
                    }
                    Current.Shared.syncRest() {(_,_) in }
                }
            }
            .store(in: &bag)
        
        let u = self.userInfo
        let d = u.decimationRange
        let detectStartTimingInMils = Int64(userInfo.detectStartTiming)! * 60_000
        let dao = Config.Shared.userDb?.sensorDetectEventDao
        
        //センサー検知
        sensorDetect(cancelableBags: &bag,
                     working: workingBinHeaders,
                     binHeaderDelayInMillis: detectStartTimingInMils,
                     workDelayInMillis: 5000,
                     sensorManager: sensorManager,
                     detectControl: Current.Shared.userRepository!.binDetailRepo.countWorkingStatus
        ).sink(receiveValue: { item in
            if item is SensorRecord.DropRecent {
                let item2 = (item as! SensorRecord.DropRecent)
                let no = item2.allocationNo
                dao!.deleteByTimeRange(allocationNo: no, from: item2.from, to: item2.to)
                if item2.isFinished {
                    let ls = dao!.findBy(allocationNo: no)
                    if !ls.isEmpty {
                        let dt = ls.map { $0.csvRow(formatter: Config.Shared.dateFormatterSensorCsv)}
                            .joined(separator: "\n")
                        
                        let csv = CommonSensorCsv(userInfor: userInfo, allocationNo: no, csv: Data(dt.utf8))
                        
                        do {
                            try Config.Shared.resultDb?.instanceDb?.write { db in
                                try csv.insert(db)
                            }
                            
                            dao?.deleteBy(allocationNo: no)
                            
                        } catch {
                            debugPrint(error)
                        }
                    }
                }
            }
            if item is SensorRecord.Record {
                let item2 = (item as! SensorRecord.Record)
                let n = item2.event
                let l = dao?.latestEvent(allocationNo: n.allocationNo)?.distanceTo(location: CLLocation(latitude: n.eventRecordLatitude!, longitude: n.eventRecordLongitude!))
                if l == nil || d == nil || l! > Double(d!) {
                    do {
                        try Config.Shared.userDb?.instanceDb?.write { db in
                            try n.insert(db)
                        }
                    } catch {
                        debugPrint(error)
                    }
                    
                }
            }
        })
        .store(in: &bag)

        //センサー検知レコード位置更新
        locationPublisher.sink { location in
            dao?.latestEventEachGroup()
                .map({ sde -> SensorDetectEvent? in
                    let prevLocationTime: Int64 = sde.locationTimestamp ?? 0
                    let currLocationTime = location.timestamp.milisecondsSince1970
                    let eventTime = Int64(sde.eventRecordDate)
                    let td1 = abs(eventTime - prevLocationTime)
                    let td2 = abs(eventTime - currLocationTime)
                    if td1 > td2 {
                        return sde.copyOf(newLocation: location)
                    } else {
                        return nil
                    }
                })
                .compactMap{$0}
                .forEach({ it in
                    do {
                        try Config.Shared.userDb?.instanceDb?.write { db in
                            try it.save(db)
                        }
                    } catch {
                        debugPrint(error)
                    }
                })
        }
        .store(in: &bag)

        let skipRange = user.decimationRange ?? 50
        let skipInterval = user.positionGetInterval
        
        //位置レコード
        workingBinHeaders
            .eachWorkingBin(cancelableBags: &bag) { b in
            return self.locationPublisher.filterWithLast { upStreamLast, downStreamLast, current in
                !(shouldSkipLocation(range: skipRange, current: current, previous: upStreamLast, lastRecorded: downStreamLast) ||
                  shouldSkipCoordinateRecord(timeSampleInSec: skipInterval , current: current, lastRecorded: downStreamLast))
            }
            .map { location in
                return CommonCoordinate(location: location, binHeader: b, userInfo: user, isAutoMode: b.allocationNo == self.inAutoMode)
            }
            .eraseToAnyPublisher()
        }
        .sink(receiveValue: { coordinate in
            let value2 = coordinate.1
            if value2 != nil {
                try? Config.Shared.resultDb?.instanceDb?.write { db in
                    do {
                        try value2.save(db)
                    } catch {
                        debugPrint(error)
                    }
                }
            }
        })
        .store(in: &bag)
        /*
        //運行中ステータス
        workingBinHeaders
            .map({ bin -> Bool in
                return bin != nil
            })
            .removeDuplicates()
            .map({ w in
                if w {
                    self.locationPublisher.map({
                        BinState(running: true, location: $0)
                    })
                    .prepend(BinState(running: true))
                } else {
                    Just(BinState(running: false))
                }
            })
            .sink(receiveValue: {
                
            })
            .store(in: &bag)
        */
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {

        switch notification.name.rawValue {
        case BroadcastEnum.Location.rawValue:
            accept()
            onLocationResult()
        default:
            break
        }
    }
    
    func removeObserver() {
        NotificationCenter.default.removeObserver(self)
    }
    
    private var bin: [BinHeader]? = nil
    private var recordProcessor: BinState? = nil
    
    private var runningBinHeaders: [BinHeaderAndStatus]? {
        return Current.Shared.userRepository?.binHeaderRepo.list()
    }
    /*@Published private var workingBinHeaders =
    Just(Current.Shared.userRepository?.binHeaderRepo.list()?
        .compactMap({ $0 })
        .filter({ item in
            item.status.getStatus() == .Working
        })
        .compactMap({ it -> BinHeader in
            return it.header
        }) ?? []
    )
    .shareReplay(1)*/
    
    private func accept() {
        if let _ = Current.Shared.loggedUser?.userInfo {
            
            if let l = runningBinHeaders {
                var hasItem = false
                for item in l {
                    if item.header.allocationNo == inAutoMode {
                        hasItem = true
                    }
                }
                
                if !hasItem {
                    cancelAutoMode()
                }
                
                var tempBin: [BinHeader] = []
                for item in l {
                    if item.status.getStatus() == .Working {
                        tempBin.append(item.header)
                    }
                }
                
                bin = tempBin
            }
        }
    }
    
    private func tryConnect() {
        if bin == nil || bin!.isEmpty {
            recordProcessor = nil
        }
    }
    
    private func onLocationResult() {
        if let _ = Current.Shared.loggedUser?.userInfo {
            if let c = Current.Shared.lastLocation {
                locationPublisher.send(c)
                if let b = bin {
                    recordProcessor = BinState(running: true, location: c)
                    
                    for header in b {
                        let k = header.allocationNo
                        
                        if k == inAutoMode {
                            autoModeProcess(allocationNo: k, location: c)
                        }
                    }
                }
            }
        }
    }
    
    class M {
        var binDetailAndStatus: BinDetailAndStatus
        var check: (Int, Bool)?
        init(binDetailAndStatus: BinDetailAndStatus, check: (Int, Bool)?){
            self.binDetailAndStatus = binDetailAndStatus
            self.check = check
        }
        
        var detail: BinDetail {
            return binDetailAndStatus.detail
        }
        
        var status: WorkStatus {
            return binDetailAndStatus.status
        }
        
        var inRange: Bool? {
            return check?.1
        }
    }
    
    @Published private var autoStart: [M]? = nil
    private func autoModeProcess(allocationNo: String, location: CLLocation) {
        let detailRepo = Current.Shared.userRepository?.binDetailRepo
        let workRepo = Current.Shared.userRepository?.workRepo

        let detailList = detailRepo!.binDetailWithStatusList(allocationNo: allocationNo)!
            .map { M(binDetailAndStatus: $0, check: $0.detail.checkDeliveryDeviation(compare: location))}
        
        let group = Dictionary.init(grouping: detailList, by: { $0.status.getStatus() })
        if let working = group[WorkStatusEnum.Working]?.first {
            let inRange = working.inRange
            if inRange != false {
                return
            }
            else {
                workRepo?.endWork(binDetail: working.detail, endLocation: location, moveNext: true, isAutoMode: true)
                Helper.Shared.sendBroadcastNotification(.Bin)
                Current.Shared.syncBin() {(isOk, Error) in}
            }
        } else {
            let moving = group[WorkStatusEnum.Moving]?.first(where: { $0.inRange ?? false })
            if moving == nil {
                workRepo?.autoMoveBinDetail(allocationNo: allocationNo)
            }
        }
        
        autoStart = detailList
    }
    
    private func getServiceOrder(binDetailAndStatus: BinDetailAndStatus) -> Int {
        let s = binDetailAndStatus.detail.serviceOrder
        if (s == nil) {
            return Int.max
        } else {
            switch binDetailAndStatus.status.getStatus() {
            case .Finished:
                return ~s!
            default:
                return s!
            }
        }
    }
    
    private func getWorkStatusOrder(wStatus: WorkStatusEnum) -> Int {
        switch wStatus {
        case .Moving:
            return 1
        case .Ready:
            return 2
        case .Finished:
            return 3
        default:
            return 4
        }
    }

    func throttleLatestRecord() -> BinState? {
        return recordProcessor
    }
    
    private var userLocationRequest: Int {
        return Current.Shared.loggedUser?.userInfo.positionGetInterval ?? 5
    }
    private var autoModeLocationRequest = 1
    
    private func ensureLocationRequestMode() {
        Current.Shared.locationHelper?.setLocationRequest(second: inAutoMode.isEmpty ? userLocationRequest : autoModeLocationRequest)
    }
    
    private var inAutoMode: String = Resources.strEmpty
    
    var inAutoModeBin: String {
        return inAutoMode
    }
    
    func setInAutoMode(binHeader: BinHeader) {
        inAutoMode = binHeader.allocationNo
        ensureLocationRequestMode()
        Current.Shared.locationHelper?.onLocationCallBack(nil)
    }
    
    func cancelAutoMode() {
        inAutoMode = Resources.strEmpty
        autoStart = nil
        ensureLocationRequestMode()
    }
    
    private var sensorDetectorDelayStart: SensorDetectorDelayStart? = nil
    private var sensorManager: CMMotionManager = CMMotionManager()
    private class SensorDetectorDelayStart : DelayStart {
        
        private var sensorManager : CMMotionManager
        private var callBack: (Double) -> Void
        private var disposable: Cancellable? = nil
        private var lastTime: Double = 0
        private let limitTime: Int = 10_000
        
        init(sensorManager: CMMotionManager, callBack: @escaping (Double) -> Void) {
            self.sensorManager = sensorManager
            self.callBack = callBack
        }
        
        override func onStart() {
            disposable = sensorManager
                .rxAccelerometerDetectionMillis(sampling: 100, size: 10, limit: self.limitTime, threshold: 4.3)
                .subscribe(on: DispatchQueue.global())
                .sink(receiveValue: { ctime in
                    let dst = ctime - self.lastTime
                    if dst > Double(self.limitTime/1000) {
                        self.lastTime = ctime
                        self.callBack(ctime)
                    }
                })
        }
        
        override func onStop(isStarted: Bool) {
            disposable?.cancel()
            sensorManager.stopDeviceMotionUpdates()
            if (isStarted) {
                disposable = nil
            }
        }
    }
}
