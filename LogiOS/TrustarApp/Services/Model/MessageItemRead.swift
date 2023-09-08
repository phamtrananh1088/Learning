//
//  MessageItemRead.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class MessageItemRead: Codable {
    var messageRowId: String
    var readers: [U]
    
    init(messageRowId: String, readers: [U]) {
        self.messageRowId = messageRowId
        self.readers = readers
    }
    
    class U: Codable {
        var userId: String
        init(userId: String) {
            self.userId = userId
        }
    }
}
