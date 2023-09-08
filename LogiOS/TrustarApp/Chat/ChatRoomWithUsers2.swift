//
//  ChatRoomWithUsers2.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/25.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ChatRoomWithUsers2 {
    var room: ChatRoom
    var users: [ChatUserWithReadRow]
    init(room: ChatRoom, users: [ChatUserWithReadRow]) {
        self.room = room
        self.users = users
    }
}
