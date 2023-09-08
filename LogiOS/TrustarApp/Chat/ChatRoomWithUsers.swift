//
//  ChatRoomWithUsers.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/25.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ChatRoomWithUsers {
    var room: ChatRoom
    var users: [ChatUser]
    init(room: ChatRoom, users: [ChatUser]) {
        self.room = room
        self.users = users
    }
}
