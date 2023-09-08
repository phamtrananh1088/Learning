//
//  MessageItemBase.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class MessageItemBase: Codable {
    var messageRowId: String
    var messageId: String
    
    init(messageRowId: String, messageId: String) {
        self.messageRowId = messageRowId
        self.messageId = messageId
    }
}
