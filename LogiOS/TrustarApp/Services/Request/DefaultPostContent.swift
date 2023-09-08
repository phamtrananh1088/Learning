//
//  DefaultPostContent.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class DefaultPostContent {
    class Message: DefaultPostContent, Codable {
        var messageId: String
        var userInfo: UserInfo.Resutl
        
        init(messageId: String, userInfo: UserInfo) {
            self.messageId = messageId
            self.userInfo = userInfo.result()
        }
    }
    
    class Token: DefaultPostContent, Codable {
        var token: String
        var userInfo: UserInfo.Resutl
        
        init(token: String, userInfo: UserInfo) {
            self.token = token
            self.userInfo = userInfo.result()
        }
    }
}
