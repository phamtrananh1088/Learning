//
//  RoomCreate.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class RoomCreate: Codable {
    var users: [UserLite]
    var userInfo: UserInfo
    
    init(user: [UserLite], userInfo: UserInfo) {
        self.users = user
        self.userInfo = userInfo
    }
    
    func result() -> Result {
        var userIds = users.map { $0 }
        let isContain = userIds.contains(where: { $0.companyCd == userInfo.companyCd && $0.userId == userInfo.userId })
        if !isContain {
            userIds.append(UserLite(userId: userInfo.userId, companyCd: userInfo.companyCd))
        }
        
        let talkRoom = TalkRoom(users: userIds)
        
        return Result(talkRoom: talkRoom, userInfo: userInfo)
    }
    
    class Result: Codable {
        var talkRoom: TalkRoom
        var userInfo: UserInfo.Resutl
        init(talkRoom: TalkRoom, userInfo: UserInfo) {
            self.talkRoom = talkRoom
            self.userInfo = userInfo.result()
        }
    }
    
    class TalkRoom: Codable {
        var users: [UserLite]
        init(users: [UserLite]) {
            self.users = users
        }
    }
}
