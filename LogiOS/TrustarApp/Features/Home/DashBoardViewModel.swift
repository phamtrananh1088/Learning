//
//  DashBoardViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/10.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class DashBoardViewModel: BaseViewModel, ObservableObject {
    var fullName: String = Resources.strEmpty
    
    @Published var unreadImportant: Int = 0
    @Published var unreadNormal: Int = 0
    @Published var operationAmount = 0
    @Published var operationRemain = 0
    @Published var showAll = true
    @Published var listBinHead: [BinHeadViewModel] = []
    @Published var isAlertAddUnscheduled = false
    
    var lstControlToolbar: [(String, () -> Void)] =
    [(Resources.settings, { Current.Shared.changeScreenTo(screenName: .Setting)})]

    init(callBack: (() -> ())!) {
        super.init()

        if callBack != nil {
            if current.loggedUser?.token != nil {
                current.syncBin() { (isOk, error) in
                    callBack!()
                }
            }
            
            getData()
        }
    }
    
    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Bin.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Notice.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {

        switch notification.name.rawValue {
        case BroadcastEnum.Bin.rawValue:
            getList()
        case BroadcastEnum.Notice.rawValue:
            getNotice()
        default:
            break
        }
    }
    
    private func getData() {
        // Get fullName
        if current.loggedUser?.userInfo.userNm != nil {
            fullName = current.loggedUser!.userInfo.userNm
        }
        
        // Get Notice
        getNotice()
        
        // Get List
        getList()
    }
    
    // Get Notice
    private var noticeRepo = NoticeRepo(Config.Shared.userDb!)
    private func getNotice() {
        unreadImportant = noticeRepo.countUnreadImportant()
        if let normal = Config.Shared.userDb?.noticeDao?.countUnreadByRank(rank: 2) {
            unreadNormal = normal
        }
    }
    
    private var headerRepo = Current.Shared.userRepository?.binHeaderRepo
    private func getList() {
        let list: [BinHeaderAndStatus]? = headerRepo?.list()
        
        var listBinHeadTemp: [BinHeadViewModel] = []
        
        if list != nil {
            for it in list! {
                listBinHeadTemp.append(BinHeadViewModel(it.header, it.status))
            }
        }
        
        // Diff
        var isDiff: Bool = false
        if listBinHead.count != listBinHeadTemp.count {
            isDiff = true
        } else {
            if listBinHead.count > 0 {
                for i in 0...listBinHead.count - 1 {
                    if !listBinHead[i].sameItem(listBinHeadTemp[i]) {
                        isDiff = true
                    }
                }
            }
        }

        if isDiff {
            listBinHead = listBinHeadTemp
            
            operationAmount = listBinHead.count
            
            operationRemain = list?.filter { $0.status.binStatusCd != "2" }.count ?? 0
        }
    }
}
