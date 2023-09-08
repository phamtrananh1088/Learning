//
//  NoticeListViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI
import Combine

class NoticeListViewModel: BaseViewModel, ObservableObject {
    @Published var noticeList: [NoticeItemViewModel] = []
    
    private var tag: String = Resources.strEmpty
    var currentSel: NoticeItemViewModel? = nil
    var rankNotice: Int

    init(typeNotice: Int?) {
        self.rankNotice = typeNotice ?? 1
        super.init()
        getNotice()
    }

    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Notice.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {
        getNotice()
    }
    
    func getNotice() {
        var noLst: [Notice]? = nil
        
        if current.getBeforeScreen() == .Login {
            noLst = noticeRepo.unreadImportant()
        } else {
            noLst = noticeRepo.allByRank(rank: rankNotice)
        }
        
        var nListTemp: [NoticeItemViewModel] = []
        
        if noLst != nil {
            for no in noLst! {
                nListTemp.append(NoticeItemViewModel(notice: no))
            }
        }
        
        // compare current item with new item
        var isDiff: Bool = false
        if noticeList.count != nListTemp.count {
            isDiff = true
        } else {
            if noticeList.count > 0 {
                for i in 0...noticeList.count - 1 {
                    if !noticeList[i].sameItem(nListTemp[i]) {
                        isDiff = true
                    }
                }
            }
        }

        if isDiff {
            noticeList = nListTemp
        }
    }

    var noticeRepo = NoticeRepo(Config.Shared.userDb!)
    
    func onClickNotice(notice: Notice) {
        let showDialogFlag =
        !notice.noticeTitle.isEmpty
        && notice.unreadFlag == 1
        && notice.eventRank == 1
        && notice.noticeTitle.contains(Config.Shared.clientInfo.terminalId)

        let dataDelFlag = showDialogFlag && !notice.noticeText.isEmpty && notice.noticeText.contains(Resources.initialize)

        if showDialogFlag {
            if dataDelFlag {
                tag = Resources.logout
            } else {
                tag = Resources.restart
            }

            showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.notice_send_data_alert_title, message: Resources.notice_send_data_alert_msg, isShowLeftBtn: true, leftBtnText: Resources.cancel, isShowRightBtn: true, rightBtnText: Resources.yes, leftBtnClick: nil, rightBtnClick: self.sendDataToAdmin))
        }
    }
    
    private func sendDataToAdmin() {
        // commit to common to wait for sync
        Current.Shared.loggedUser?.commitCommonDb()
        
        if tag == Resources.logout {
            // logout action
            current.logout()
            current.changeScreenTo(screenName: .Login)
        } else {
            // restart
            current.changeScreenTo(screenName: .DashBoard)
        }
    }
}
