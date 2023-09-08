//
//  RoomAddMember.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class RoomAddMember: Codable {
    var roomId: String
    var users: [UserLite]
    var userInfo: UserInfo
    
    init(roomId: String, users: [UserLite], userInfo: UserInfo) {
        self.roomId = roomId
        self.users = users
        self.userInfo = userInfo
    }
    
    func result() -> TalkRoom {
        let room = Room(talkRoomId: roomId, users: users)
        return TalkRoom(talkRoom: room, userInfo: userInfo)
    }
    
    class TalkRoom: Codable {
        var talkRoom: Room
        var userInfo: UserInfo.Resutl
        
        init(talkRoom: Room, userInfo: UserInfo) {
            self.talkRoom = talkRoom
            self.userInfo = userInfo.result()
        }
    }

    class Room: Codable {
        var talkRoomId: String
        var users: [UserLite]
        init(talkRoomId: String, users: [UserLite]) {
            self.talkRoomId = talkRoomId
            self.users = users
        }
    }
}
