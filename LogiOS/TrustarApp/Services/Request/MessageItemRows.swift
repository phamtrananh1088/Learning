//
//  MessageItemRows.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class MessageItemRows: Codable {
    var messageRowIds: [String]
    var userInfo: UserInfo.Resutl
    
    init(messageRowIds: [String], userInfo: UserInfo) {
        self.messageRowIds = messageRowIds
        self.userInfo = userInfo.result()
    }
    
    func result() -> MessageItem {
        var msgItems: [MessageRow] = []
        
        for it in messageRowIds {
            msgItems.append(MessageRow(messageRowId: it))
        }
        
        return MessageItem(messageItems: msgItems, userInfo: userInfo)
    }
    
    class MessageItem: Codable {
        var messageItems: [MessageRow]
        var userInfo: UserInfo.Resutl
        init(messageItems: [MessageRow], userInfo: UserInfo.Resutl) {
            self.messageItems = messageItems
            self.userInfo = userInfo
        }
    }
    
    class MessageRow: Codable {
        var messageRowId: String
        init(messageRowId: String) {
            self.messageRowId = messageRowId
        }
    }
}
