//
//  ChatUserWithReadRow.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ChatUserWithReadRow {
    var chatUser: ChatUser
    var lastRow: String?
    
    init(chatUser: ChatUser, lastRow: String?) {
        self.chatUser = chatUser
        self.lastRow = lastRow
    }
}
