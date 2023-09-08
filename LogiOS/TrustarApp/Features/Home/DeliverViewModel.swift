//
//  DeliverViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/16.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import UIKit
import CoreLocation

class DeliverViewModel: BaseViewModel, ObservableObject {
    
    private var homeVm: HomeViewModel?
    @Published var allocationNo: String
    @Published var operation: String = Resources.strEmpty
    @Published var carModel: String = Resources.strEmpty
    @Published var isAutoMode: Bool = false
    @Published var binStatusType: BinStatusEnum = .Ready
    @Published var binHeaderSelected: BinHeaderAndStatus? = nil
    
    var workAddEnable: Bool {
        var enableToAddWorkOfSelectedBinHeader: BinHeaderAndStatus? = nil
        
         if let h = binHeaderSelected {
             if h.status.getStatus() == .Working {
                 var hasWorking: Bool = false
                 for l in binDetailWithStatusList {
                     if l.status.getStatus() == .Working {
                         hasWorking = true
                     }
                 }
                 
                 if !hasWorking {
                     enableToAddWorkOfSelectedBinHeader = h
                 }
             }
        }
        
        return enableToAddWorkOfSelectedBinHeader != nil && isAutoMode != true
    }
    
    // Button binStatusSwitch
    @Published var binStatusSwitchDisable = true
    @Published var binStatusSwitchText = Resources.operation_start
    @Published var binStatusSwitchColor = Resources.colorPrimary
    
    // Area contain binStatusSwitch and workAdd
    @Published var operationAreaBackground = Resources.colorPrimary
    
    @Published var binDetailWithStatusList: [WorkItemViewModel] = []
    
    @Published var endState: LoadingState = .None
    
    @Published var isShowBinDetailDialog = false
    var doneRefresh: (() -> Void)!
    
    init(homeVm: HomeViewModel?, allocationNo: String) {
        
        self.homeVm = homeVm
        self.allocationNo = allocationNo
                
        super.init()
        
        self.isAutoMode = current.loggedUser?.binLocationTask.inAutoModeBin == allocationNo
        
        if !allocationNo.isEmpty {
            self.getDataHeader()
            self.getList()
            DispatchQueue.main.async {
                homeVm?.updateBadge()
            }
        }
    }
    
    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Bin.rawValue), object: nil)

        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Location.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {

        switch notification.name.rawValue {
        case BroadcastEnum.Bin.rawValue:
            getDataHeader()
            getList()
            homeVm?.updateBadge()
            doneRefresh?()
        case BroadcastEnum.Location.rawValue:
            onUpdateLocation()
        default:
            break
        }
    }
    
    private func onUpdateLocation() {
        if let loc = current.loggedUser?.binLocationTask.throttleLatestRecord()?.location {
            for item in binDetailWithStatusList {
                item.updateDeviation(location: loc)
            }
        }
    }
    private func getDataHeader() {
        binHeaderSelected = repo?.selectBinHeaderWithStatus(allocationNo: allocationNo)
        
        if let data = binHeaderSelected {
            operation = data.header.allocationNm
            carModel = data.truck.truckNm
            binStatusType = data.status.getStatus()
            isAutoMode = data.header.allocationNo == current.loggedUser?.binLocationTask.inAutoModeBin

            setSwitchAreaColor()
        }
    }
    
    private func setSwitchAreaColor() {
        binStatusSwitchDisable = true
        binStatusSwitchText = Resources.strEmpty
        
        var tempColor: UIColor
        
        // Color
        if binStatusType == .Finished {
            tempColor = Resources.orange_out
        } else {
            tempColor = Resources.colorPrimary
        }
        
        // Set state button
        if binStatusType == .Ready {
            operationAreaBackground = tempColor
            binStatusSwitchColor = tempColor
            binStatusSwitchDisable = false
            binStatusSwitchText = Resources.operation_start
        }
        
        if binStatusType == .Working {
            operationAreaBackground = tempColor
            binStatusSwitchColor = tempColor
            binStatusSwitchDisable = false
            binStatusSwitchText = Resources.operation_end
        }
        
        if binStatusType == .Finished {
            operationAreaBackground = tempColor
            binStatusSwitchColor = tempColor
            binStatusSwitchText = Resources.operation_end
        }
    }
    
    private func getList() {
        var binDetailWithStatusListTemp: [WorkItemViewModel] = []
        
        if let list = repo2?.binDetailWithStatusList(allocationNo: allocationNo) {
            for item in list {
                binDetailWithStatusListTemp.append(
                    WorkItemViewModel(binDetailAndStatus: item,
                                      location: current.loggedUser?.binLocationTask.throttleLatestRecord()?.location))
            }
        }
        
        // compare current item with new item
        var isDiff: Bool = false
        if binDetailWithStatusList.count != binDetailWithStatusListTemp.count {
            isDiff = true
        } else {
            if binDetailWithStatusList.count > 0 {
                for i in 0...binDetailWithStatusList.count - 1 {
                    if !binDetailWithStatusList[i].sameItem(binDetailWithStatusListTemp[i])
                        || binDetailWithStatusList[i].detail.updatedDate == nil {
                        isDiff = true
                    }
                }
            }
        }

        if isDiff {
            binDetailWithStatusList = binDetailWithStatusListTemp
        }
    }
    
    private var workRepo = Current.Shared.userRepository?.workRepo
    private var repo = Current.Shared.userRepository?.binHeaderRepo
    private var repo2 = Current.Shared.userRepository?.binDetailRepo
    
    func startInAutoMode(binDetail: BinDetail, location: CLLocation) {
        if binDetail.workCd != nil {
            workRepo?.setLocation(binDetail: binDetail, location: location)
            let work: Work = Work(workCd: binDetail.workCd!, workNm: binDetail.workNm!, displayOrder: 0, displayFlag: false)
            let _ = workRepo?.startWork(binDetail: binDetail, work: work, fromLocation: location, addIfFinished: true)
            current.syncBin() { (isOk, Error) in }
        }
    }

    func endBin(weather: WeatherEnum, location: CLLocation?, setIncomingOrNot: Int?) {
        if setIncomingOrNot != nil {
            repo?.setIncomingMeter(binHeader: binHeaderSelected!.header, kilometer: setIncomingOrNot)
        }
        
        repo?.endBin(binHeader: binHeaderSelected!.header, weather: weather, endLocation: location)
        sync()
    }
    
    func sync() {
        endState = .Loading
        current.loggedUser?.binLocationTask.cancelAutoMode()
        Helper.Shared.sendBroadcastNotification(.Bin)
        current.syncBin() { [self] (isOK, Error) in
            if !isOK {
                endState = .None
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: { [self] in
                    homeVm!.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.server_connection_err, message: Resources.sync_connection_err_alert_msg, isShowLeftBtn: true, leftBtnText: Resources.cancel, isShowRightBtn: true, rightBtnText: Resources.data_resend, leftBtnClick: nil, rightBtnClick: {
                        self.sync()
                    }))
                })
            } else {
                self.endState = .None
            }
        }
    }
}
