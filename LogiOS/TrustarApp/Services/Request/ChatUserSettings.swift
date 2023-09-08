//
//  ChatUserSettings.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ChatUserSettings: Codable {
    var talkRoomId: String
    var userId: String
    var notificationFlag: Bool?
    var leavingFlag: Bool?
    var userInfo: UserInfo.Resutl
    
    init(talkRoomId: String,
         userId: String,
         notificationFlag: Bool?,
         leavingFlag:Bool?,
         userInfo: UserInfo) {
        self.talkRoomId = talkRoomId
        self.userId = userId
        self.notificationFlag = notificationFlag
        self.leavingFlag = leavingFlag
        self.userInfo = userInfo.result()
    }
}
