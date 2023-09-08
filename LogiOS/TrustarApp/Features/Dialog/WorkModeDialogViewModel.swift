//
//  WorkModeDialogViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 22/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import CoreLocation

class WorkModeDialogViewModel: ObservableObject {
    var workMode: [WorkMode] = [.init(mode: .manual), .init(mode: .automatic)]
    var isShowWarning = false
    @Published var selected: WorkMode?
    var binHeaderLiveData: BinHeaderAndStatus?
    
    init(allocationNo: String) {
        let binHeaderLiveData = Current.Shared.userRepository?.binHeaderRepo.selectBinHeaderWithStatus(allocationNo: allocationNo)
        
        if let binList = Current.Shared.userRepository?.binDetailRepo.binDetailWithStatusList(allocationNo: allocationNo) {
            if binList.contains(where: { $0.detail.placeLatitude == nil && $0.detail.placeLongitude == nil}) {
                isShowWarning = true
            } else {
                isShowWarning = false
            }
        }
        
        self.binHeaderLiveData = binHeaderLiveData
    }
    
    func start(workMode: WorkMode, location: CLLocation?) {
        if binHeaderLiveData != nil {
            
            Current.Shared.userRepository?.binHeaderRepo.startBin(binHeader: binHeaderLiveData!.header, startLocation: location)
            
            Helper.Shared.sendBroadcastNotification(.Bin)
            setAutoMode(state: workMode.mode == .automatic)
            Helper.Shared.sendBroadcastNotification(.Bin)
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1) {
                Current.Shared.syncBin(callBack: {(isOk, Error) in })
            }
        }
    }
    
    func setAutoMode(state: Bool) {
        let task = Current.Shared.loggedUser!.binLocationTask
        if state {
            task.setInAutoMode(binHeader: binHeaderLiveData!.header)
        } else {
            task.cancelAutoMode()
        }
    }
}
