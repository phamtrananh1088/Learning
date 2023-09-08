//
//  BinDetailDialogModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 01/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class EditModel: ObservableObject {
    @Published var input: String = String()
    
    init(input: String?) {
        if input != nil {
            self.input = input!
        }
    }
}

class BinDelayReasonChangeViewModel {
    var delayReasons: [DelayReason]
    var reasonSelected: DelayReason?
    
    init(delayReasons: [DelayReason], reasonSelected: DelayReason?) {
        self.delayReasons = delayReasons
        self.reasonSelected = reasonSelected
    }
}

class BinDetailDialogModel: BaseViewModel, ObservableObject {
    private var binDetailRepo = Current.Shared.userRepository?.binDetailRepo
    private var workRepo = Current.Shared.userRepository?.workRepo
    private var repo = CollectionRepo(Current.Shared.userDatabase!)
    
    @Published var binDetailRx: BinDetail
    
    var binLiveData: BinDetailData {
        return binDetailRepo!.findBinDetailWithDelayReason(allocationNo: binDetailRx.allocationNo, allocationRowNo: binDetailRx.allocationRowNo)
    }
    
    @Published var binDelayReason: String = Resources.strEmpty
    var disableBinDelayReasonChange: Bool = false
    var binServiceOrder: String = Resources.strEmpty
    var binOperationOrder: String = Resources.strEmpty
    var binPlaceName: String = Resources.strEmpty
    var binPlanTime: String = Resources.strEmpty
    var binActualTime: String = Resources.strEmpty
    var binWorkName: String = Resources.strEmpty
    var binZipCode: String = Resources.strEmpty
    var binPlaceAddress: String = Resources.strEmpty
    var binPlaceLatitude: String = Resources.strEmpty
    var binPlaceLongtitude: String = Resources.strEmpty
    var binPlaceTel1: String = Resources.strEmpty
    var binPlaceMail1: String = Resources.strEmpty
    var binPlaceTel2: String = Resources.strEmpty
    var binPlaceMail2: String = Resources.strEmpty
    var binPlaceNote1: String = Resources.strEmpty
    var binPlaceNote2: String = Resources.strEmpty
    var binPlaceNote3: String = Resources.strEmpty
    var binIncidental: String = Resources.strEmpty
    var binSignature: String = Resources.strEmpty
    var binIncidentalGroup: Bool = false
    var binSignGroup: Bool = false
    var binTemperature: String = Resources.strEmpty
    var binMemo: String = Resources.strEmpty
    @Published var binCollect: String = Resources.strEmpty
    //var binCollectGroup
    
    @Published var temperatureModel: EditModel = EditModel(input: nil)
    @Published var expeiencePlaceNote1Model: EditModel = EditModel(input: nil)
    @Published var isShowSheetList = false
    
    @Published var binDelayReasonChangeVm: BinDelayReasonChangeViewModel = BinDelayReasonChangeViewModel(delayReasons: [], reasonSelected: nil)
    @Published var binDeliveryChartVm: DeliveryChartViewModel = DeliveryChartViewModel(binDetail: nil)
    var collectVm: CollectViewModel = CollectViewModel()
    
    init(binDetail: BinDetail?) {
        
        binDetailRx = binDetail ?? BinDetail()
        
        super.init()
                
        self.getData()
        
        if binDetail != nil {
            current.syncIncidental() {(isOk, Error) in }
        }
        
        $binDetailRx
            .filter({ !$0.allocationNo.isEmpty})
            .setFailureType(to: Error.self)
            .map({ bin in
                return self.repo
                    .collectionResults(allocationNo: bin.allocationNo,
                                       allocationRowNo: bin.allocationRowNo)
            })
            .switchToLatest()
            .map({ result in
                let rows = result?.rows()
                if rows != nil && rows!.contains(where: { $0.actualQuantity > 0 }) {
                    return Resources.bin_registered
                } else {
                    return Resources.strEmpty
                }
            })
            .removeDuplicates()
            .subscribe(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { result in
                self.binCollect = result
            }).store(in: &bag)

    }
    
    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Incidental.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Bin.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Authorized.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {

        switch notification.name.rawValue {
        case BroadcastEnum.Incidental.rawValue:
            getData()
        case BroadcastEnum.Authorized.rawValue:
            showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: Resources.strEmpty, message: Resources.invalid_account_alert_msg, isShowLeftBtn: false, leftBtnText: Resources.strEmpty, isShowRightBtn: true, rightBtnText: Resources.ok, leftBtnClick: {self.current.logout()}, rightBtnClick: {self.current.logout()}))
        default:
            break
        }
    }
    
    private func getData() {
        let b = binLiveData
        
        let karaPlaceholder = ""
        let binDetail = b.binDetail
        
        binDelayReason = b.delayReason?.reasonText ?? karaPlaceholder
        if binDetail.isDelayed() {
            disableBinDelayReasonChange = false
        } else {
            disableBinDelayReasonChange = true
        }
        
        binServiceOrder = binDetail.serviceOrder.takeIf { binDetail.serviceOrder != nil && binDetail.serviceOrder! > 0 }.toString() ?? karaPlaceholder
        
        
        binOperationOrder = binDetail.operationOrder.takeIf { binDetail.operationOrder != nil && binDetail.operationOrder! > 0 }.toString() ?? karaPlaceholder
        
        binPlaceName = binDetail.placeNameFull()
        binPlanTime = binDetail.displayPlanTime() ?? karaPlaceholder
        binActualTime = binDetail.displayWorkTime() ?? karaPlaceholder
        binWorkName = binDetail.workNm.orEmpty()
        
        binZipCode = binDetail.placeZip ?? karaPlaceholder
        binPlaceAddress = binDetail.placeAddr ?? karaPlaceholder
        binPlaceLatitude = binDetail.placeLatitude.toString() ?? karaPlaceholder
        binPlaceLongtitude = binDetail.placeLongitude.toString() ?? karaPlaceholder
        
        binPlaceTel1 = binDetail.placeTel1 ?? karaPlaceholder
        binPlaceMail1 = binDetail.placeMail1 ?? karaPlaceholder
        binPlaceTel2 = binDetail.placeTel2 ?? karaPlaceholder
        binPlaceMail2 = binDetail.placeMail2 ?? karaPlaceholder

        binPlaceNote1 = binDetail.placeNote1 ?? karaPlaceholder
        binPlaceNote2 = binDetail.placeNote2 ?? karaPlaceholder
        binPlaceNote3 = binDetail.placeNote3 ?? karaPlaceholder
        
        if let it = binDetail.temperature {
            let binTemperatureTemp = "\(String(format: "%.1f", Float(it)!)) ℃"
            if binTemperature != binTemperatureTemp {
                binTemperature = binTemperatureTemp
                temperatureModel = EditModel(input: String(it))
            }
        } else {
            binTemperature = karaPlaceholder
        }
        
        let binMemoTemp = binDetail.experiencePlaceNote1 ?? karaPlaceholder
        if binMemo != binMemoTemp {
            binMemo = binMemoTemp
            expeiencePlaceNote1Model = EditModel(input: binMemo)
        }
        
        binIncidentalGroup = current.loggedUser?.userInfo.incidentalEnable() == true
        binSignGroup = current.loggedUser?.userInfo.incidentalEnable() == true

        if (binLiveData.incidentalHeaderCount > 0) {
            binIncidental = Resources.bin_registered
        } else {
            binIncidental = karaPlaceholder
        }
        
        if (binLiveData.signedIncidentalHeaderCount > 0) {
            binSignature = Resources.bin_registered
        } else {
            binSignature = karaPlaceholder
        }
    }
    
    func reasonChange(delayReason: DelayReason) {
        workRepo?.setDelayReason(binDetail: binDetailRx, delayReason: delayReason)
        getData()
        current.syncBin(callBack: {(_,_) in })
    }
    
    func setTemperature(temperature: String) {
        workRepo?.setTemperature(binDetail: binDetailRx, tem: temperature)
        getData()
        current.syncBin() {(_,_) in }
    }
    
    func setMemo(memo: String) {
        workRepo?.setMemo(binDetail: binDetailRx, memo: memo)
        getData()
    }
}
