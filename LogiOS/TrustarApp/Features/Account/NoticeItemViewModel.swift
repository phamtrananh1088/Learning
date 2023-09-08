//
//  NoticeItemViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class NoticeItemViewModel: BaseViewModel, ObservableObject, Identifiable {
    var notice: Notice
    
    init(notice: Notice) {
        self.notice = notice
    }
    
    override func registerNotificationCenter() {
        
    }
    
    func sameItem(_ n: NoticeItemViewModel) -> Bool {
        return notice.noticeTitle == n.notice.noticeTitle
        && notice.noticeText == n.notice.noticeText
        && notice.unreadFlag == n.notice.unreadFlag
    }
}
