//
//  HomeViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import CoreLocation
import SwiftUI

class HomeViewModel: BaseViewModel, ObservableObject {

    private var locManager = CLLocationManager()
    @Published var dashBroadVm: DashBoardViewModel = DashBoardViewModel(callBack: nil)
    @Published var deliverVm: DeliverViewModel = DeliverViewModel(homeVm: nil, allocationNo: "")
    @Published var operationVm: OperationViewModel = OperationViewModel(homeVm: nil)
    @Published var roomLoadVM: RoomLoadVM = RoomLoadVM()
    @Published var roomUserSelectionVM: RoomUserSelectionVM = RoomUserSelectionVM()
    @Published var addRoomVm: AddRoomVM = AddRoomVM()
    @Published var editRoomVm: EditRoomVM = EditRoomVM()
    @Published var chatVm: ChatVM = ChatVM()
    @Published var isShowChatRoom: Bool = false
    @Published var chatOptionVm: ChatOptionVM = ChatOptionVM()
    @Published var isShowChatOption: Bool = false
    @Published var isShowSheetList = false
    @Published var isShowBinStart = false
    @Published var isShowUnscheduleBinStart = false
    @Published var isShowBinStartProgress = false
    @Published var heigthBinStartProgress = CGFloat.zero
    var binStartDialogVm: BinStartDialogViewModel = BinStartDialogViewModel()
    @Published var isShowbinMeterInputView = false
    var binMeterInputVm: BinMeterInputViewModel = BinMeterInputViewModel()
    /**給油*/
    @Published var isShowBinBottomPicker = false
    @Published var isShowFuelBottomPicker = false
    @Published var refuelVm: RefuelViewModel = RefuelViewModel()
    @Published var isShowBinHeaderInfoDialogView = false
    var binHeaderInfoDialogVm: BinHeaderInfoDialogViewModel = BinHeaderInfoDialogViewModel()
    @Published var isShowWorkModeDialogView = false
    var workModeDialogVm: WorkModeDialogViewModel = WorkModeDialogViewModel(allocationNo: String())
    var binDetailDialogVm: BinDetailDialogModel = BinDetailDialogModel(binDetail: nil)
    
    var navigationStack: NavigationStack = NavigationStack()
    
    @Published var badge: Bool = false
    private var disableChangeToDesignationView: Bool = true
    @Published var tabDisplay: ScreenDisplay = .None {
        willSet {
            if tabDisplay == .DashBoard || tabDisplay == .Deliver || tabDisplay == .Operation {
                current.syncBin(callBack: {(_,_) in})
            }
            
            if newValue == .Operation {
                let working = goDefault ? startedBinDetailListOfSelectedBinHeader.first : nil
                let inAutoMode = deliverVm.binHeaderSelected?.header.allocationNo == current.loggedUser?.binLocationTask.inAutoModeBin
                
                if !inAutoMode {
                    if working != nil || currentUnfinishedWork != nil {
                        disableChangeToDesignationView = false
                    } else {
                        disableChangeToDesignationView = true
                    }
                } else {
                    disableChangeToDesignationView = true
                }
                
                if !disableChangeToDesignationView {
                    if goDefault {
                        if working != nil {
                            navigationBinDetail(binDetail: working!.detail, goDefault: false)
                        }
                    }
                    
                    goDefault = true
                    operationVm = OperationViewModel(homeVm: self)
                }
            }
            
            if newValue == .Deliver {
                let working = goDefault ? binHeaderRepo?.anyBinHeader(status: .Working) : nil
                
                if working != nil || deliverVm.binHeaderSelected != nil {
                    disableChangeToDesignationView = false
                } else {
                    disableChangeToDesignationView = true
                }
                
                if !disableChangeToDesignationView {
                    if goDefault {
                        if working != nil {
                            navigationBinHeader(allocationNo: working!.allocationNo, goDefault: false)
                        } else {
                            navigationBinHeader(allocationNo: deliverVm.binHeaderSelected!.header.allocationNo, goDefault: false)
                        }
                    }
                    
                    goDefault = true
                }
            }
        }
        
        didSet {
            if disableChangeToDesignationView {
                if tabDisplay == .Operation {
                    if oldValue == .Deliver {
                        navigationBinHeader(allocationNo: deliverVm.allocationNo, goDefault: false)
                    }
                    
                    tabDisplay = oldValue
                } else if tabDisplay == .Deliver {
                    tabDisplay = oldValue
                }
            }
        }
    }
        
    private var binDetailRepo = Current.Shared.userRepository?.binDetailRepo
    private var binDetailListOfSelected: [BinDetailAndStatus]? {
        if let allocationNo = deliverVm.binHeaderSelected?.header.allocationNo {
            return binDetailRepo?.binDetailWithStatusList(allocationNo: allocationNo)
        }
        
        return nil
    }
    
    private var startedBinDetailListOfSelected: [BinDetailAndStatus] {
        var b: [BinDetailAndStatus] = []
        if binDetailListOfSelected != nil {
            for it in binDetailListOfSelected! {
                if it.detail.getStatus() == .Working || it.detail.getStatus() == .Moving {
                    b.append(it)
                }
            }
        }
        
        return b
    }
    
    var startedBinDetailListOfSelectedBinHeader: [BinDetailAndStatus] {
        return startedBinDetailListOfSelected
    }
    
    private var works = Current.Shared.userRepository?.workRepo.workList()
    private var workList: [Work]? {
        return works
    }
    private var selectWorkBin: WorkBin = WorkBin()
    private var currentWorkBin: WorkBin {
        var wBin: WorkBin = WorkBin()
        if selectWorkBin.bin != nil {
            if selectWorkBin.binType == .Bin {
                let b = selectWorkBin.bin as! BinDetail
                if let bin = binDetailRepo?.findBinDetail(allocationNo: b.allocationNo, allocationRowNo: b.allocationRowNo) {
                    wBin.onNext(.Bin, bin)
                }
            } else {
                wBin = selectWorkBin
            }
        }
        
        return wBin
    }
    
    var currentWork: WorkBin {
        return currentWorkBin
    }
    
    var currentUnfinishedWork: (WorkBin, [Work])? {
        if currentWork.bin == nil || workList == nil || workList!.isEmpty {
            return nil
        }
        
        return (currentWork, workList!)
    }
    
    private var workRepo = Current.Shared.userRepository?.workRepo
    private var binHeaderRepo = Current.Shared.userRepository?.binHeaderRepo
    private var chatRepo = Current.Shared.chatRoomRepository

    @Published var unreadMessageCount: Int = 0
    
    private var binHeaders: [BinHeaderAndStatus]? {
        return binHeaderRepo?.list()
    }
    
    var sheetListVm: SheetListViewModel = SheetListViewModel()
    private var callBack: () -> Void
    init(callBack: @escaping () -> Void) {
        self.callBack = callBack
        super.init()

        dashBroadVm = DashBoardViewModel(callBack: onWaitDashBroadSyncDone)
        
        // Location
        LocationManager.shared.requestLocationAuthorization()
        current.locationHelper = LocationHelper()
        current.locationHelper?.onLocationCallBack = onLocationResult
        current.locationHelper?.setLocationRequest(second: 3)
        onLocationResult(nil)
        checkLocationPermission()
        
        chatRepo?.unreadCount()
            .sink(receiveCompletion: { completion in },
                  receiveValue: { unread in
                self.unreadMessageCount = unread
            }).store(in: &bag)
    }
    
    private func checkLocationPermission() {
        let status = CLLocationManager.authorizationStatus()
        if status == .denied || status == .notDetermined {
            showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.permission_request_msg_location, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok, rightBtnClick: {
                UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
            },autoClose: false))
        } else if status != .authorizedAlways  {
            showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.app_settings_location_background_permission_msg, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok, rightBtnClick: {
                UIApplication.shared.open(URL(string: UIApplication.openSettingsURLString)!)
            },autoClose: false))
        } else {
            isShowingAlert = false
            onLocationResult(nil)
        }
    }
    
    private func onWaitDashBroadSyncDone() {
        if let first = dashBroadVm.listBinHead.first(where: { $0.status == BinStatusEnum.Working.rawValue }) {
            if deliverVm.binHeaderSelected == nil {
                deliverVm = DeliverViewModel(homeVm: self, allocationNo: first.allocationNo)
            }
        }
        
        operationVm = OperationViewModel(homeVm: self)
        
        updateBadge()
    }
    
    func updateBadge() {
        if !startedBinDetailListOfSelectedBinHeader.isEmpty {
            badge = true
        } else {
           badge = false
        }
    }
    
    private func onLocationResult(_ locResult: CLLocation?) {
        if CLLocationManager.authorizationStatus() == .authorizedWhenInUse || CLLocationManager.authorizationStatus() == .authorizedAlways {
            
            if let loc = locResult {
                if loc.horizontalAccuracy >= 0
                    && loc.coordinate.latitude != 0
                    && loc.coordinate.longitude != 0 {
                    current.lastLocation = loc
                }
            } else if let loc = locManager.location {
                if loc.horizontalAccuracy >= 0
                    && loc.coordinate.latitude != 0
                    && loc.coordinate.longitude != 0 {
                    current.lastLocation = loc
                }
            }
        }
        
        Helper.Shared.sendBroadcastNotification(.Location)
    }
    
    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Authorized.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Active.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Chat.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.RemoveObserver.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {

        switch notification.name.rawValue {
        case BroadcastEnum.Authorized.rawValue:
            callBack()
        case BroadcastEnum.Active.rawValue:
            checkLocationPermission()
        case BroadcastEnum.RemoveObserver.rawValue:
            dashBroadVm.removeObserver()
            deliverVm.removeObserver()
            operationVm.removeObserver()
            self.removeObserver()
            
        case BroadcastEnum.Chat.rawValue:
            roomLoadVM.load()
            
            if !chatVm.roomSource.isEmpty {
                chatVm.onAppend()
            }
        default:
            break
        }
    }
    
    /**
     * 追加作業、作業済から
     */
    func workAdd(finished: BinDetail) {
        selectWorkBin.onNext(.Finished, finished)
    }
    
    /**
     * 追加作業
     */
    func workAdd(bin: BinHeader) {
        goDefault = false
        selectWorkBin.onNext(.New, bin)
        tabDisplay = .Operation
    }
    
    /**
     * BinDetailを選択
     */
    func selectBinDetail(binDetail: BinDetail?) {
        if (binDetail == nil) { unsetAdd() }
        else { selectWorkBin.onNext(.Bin, binDetail!) }
    }
    
    /**
     * 追加しなかった
     */
    func unsetAdd() {
        selectWorkBin.clear()
    }
    
    /**
     * 作業終了
     */
    func endWork(binDetail: BinDetail) {
        workRepo!.endWork(binDetail: binDetail, endLocation: current.lastLocation, moveNext: false)
        
        let h = binHeaderRepo!.selectBinHeaderWithStatus(allocationNo: binDetail.allocationNo)?.header
        if h != nil && h!.unplannedFlag {
            workAdd(bin: h!)
        } else {
            selectBinDetail(binDetail: nil)
            tabDisplay = .Deliver
        }
        
        current.syncBin() {(isOk, Error) in}
        updateBadge()
    }
    
    /**
     * ダッシュボードBinHeaderリスト、すべて
     */
    var binHeaderList: [BinHeaderAndStatus]? {
        return binHeaders
    }
    
    func startWork(binDetail: BinDetail, work: Work, location: CLLocation?) {
        let wr = workRepo?.startWork(binDetail: binDetail, work: work, fromLocation: location, addIfFinished: true)
        selectBinDetail(binDetail: wr)
        current.syncBin(callBack: {(isOk, Error) in })
        updateBadge()
    }
    
    func startNewAddWork(binHeader: BinHeader,
                         work: Work,
                         placeNm1: String?, placeNm2: String?, placeAddr: String?,
                         location: CLLocation?
    ) {
        let wr = workRepo!.startWork(binHeader: binHeader,
                                     placeNm1: placeNm1, placeNm2: placeNm2, placeAddr: placeAddr,
                                     work: work,
                                     fromLocation: location)
        selectBinDetail(binDetail: wr)
        current.syncBin() { (isOk, Error) in }
        updateBadge()
    }
    
    private var goDefault = true
    func navigationBinDetail(binDetail: BinDetail, goDefault: Bool = true) {
        self.goDefault = goDefault
        
        if binDetail.getStatus() == .Finished {
            workAdd(finished: binDetail)
        } else {
            if binDetail.getStatus() == .Ready {
                current.userRepository?.workRepo.moveWork(binDetail: binDetail)
                current.syncBin() { (isOk, Error) in }
            }
            
            selectBinDetail(binDetail: binDetail)
        }
        
        tabDisplay = .Operation
    }
    
    private func handleErr(err: Subscribers.Completion<NetworkError>, callBack: @escaping (Bool, NetworkError?)->()) {
        switch(err) {
        case .finished:
            break
        case .failure(let error):
            callBack(false, error)
        }
    }
    
    func startUnscheduledBin(token: String,
                             truck: Truck,
                             outgoingMeter: Int? = nil,
                             raiseAlertError: @escaping () -> Void,
                             raisePopupError: @escaping () -> Void) {
        isShowBinStartProgress = true
        let user = Current.Shared.loggedUser!
        current.syncBin(callBack: { [self] (isOk, error) in
            if !isOk {
                raisePopupError()
            } else {
                let totalWorking = self.countBinHeader(status: .Working)
                if totalWorking > 0 {
                    isShowbinMeterInputView = false
                    isShowUnscheduleBinStart = false
                    isShowBinStartProgress = false
                    
                    raiseAlertError()
                } else {
                    Config.Shared.fetch.startUnscheduledBin(
                        loginInfo: user,
                        unscheduledBin:
                            UnscheduledBin(token: token,
                                           truck: truck,
                                           locationRecord: LocationRecord(location: self.current.lastLocation, date: currentTimeMillis()),
                                           clientInfo: Config.Shared.clientInfo,
                                           userInfo: user.userInfo,
                                           outgoingMeter: outgoingMeter))
                        .receive(on: DispatchQueue.main)
                        .sink(receiveCompletion: { err in
                            self.handleErr(err: err, callBack: {(isOk, Error) in
                                if !isOk {
                                    raisePopupError()
                                }
                            })
                        },
                              receiveValue: { res in
                      self.current.syncBin() { [self] (isOk, Error) in
                                isShowbinMeterInputView = false
                                isShowUnscheduleBinStart = false
                                isShowBinStartProgress = false
                                
                                lazyNavigationByAllocationNo(allocationNo: res.AllocationNo!)
                            }
                        }).store(in: &self.bag)
                }
            }
        })
    }
    
    func countBinDetail(status: WorkStatusEnum, allocationNo: String) -> Int {
        return binDetailRepo!.countByStatus(status: status, allocationNo: allocationNo)
    }
    
    func countBinHeader(status: BinStatusEnum) -> Int {
        return binHeaderRepo!.countByStatus(status: status)
    }
    
    func lazyNavigationByAllocationNo(allocationNo: String) {
        let o = binHeaderRepo!.selectBinHeaderWithStatus(allocationNo: allocationNo)
        if o != nil {
            navigationBinHeader(allocationNo: o!.header.allocationNo, goDefault: false)
            tabDisplay = .Deliver
        }
    }
    
    func navigationBinHeader(allocationNo: String, goDefault: Bool = true) {
        self.goDefault = goDefault
        deliverVm = DeliverViewModel(homeVm: self, allocationNo: allocationNo)
    }
}
