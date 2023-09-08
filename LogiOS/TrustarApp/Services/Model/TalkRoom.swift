//
//  TalkRoom.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class TalkRoom: Codable {
    var talkRoomId: String
    var talkRoomName: String?
    var talkRoomImageUrl: String?
    var notificationFlag: Bool?
    var users: [TalkUser]?
    var readMessageItems: [MessageItemRead]?
    var deletedMessageItems: [MessageItemBase]?
    var userCount: Int?
    var unreadCount: Int?
    var lastUpdateDatetime: Int64?
    
    init(talkRoomId: String, talkRoomName: String? = nil, talkRoomImageUrl: String? = nil, notificationFlag: Bool? = nil, users: [TalkUser]? = nil, readMessageItems: [MessageItemRead]? = nil, deletedMessageItems: [MessageItemBase]? = nil, userCount: Int? = nil, unreadCount: Int? = nil, lastUpdateDatetime: Int64? = nil) {
        self.talkRoomId = talkRoomId
        self.talkRoomName = talkRoomName
        self.talkRoomImageUrl = talkRoomImageUrl
        self.notificationFlag = notificationFlag
        self.users = users
        self.readMessageItems = readMessageItems
        self.deletedMessageItems = deletedMessageItems
        self.userCount = userCount
        self.unreadCount = unreadCount
        self.lastUpdateDatetime = lastUpdateDatetime
    }
    
    func readRowsByUser() -> [String: String] {
        var results: [String:String] = [:]
        if let rmi = readMessageItems {
            for m in rmi {
                for u in m.readers {
                    if results[u.userId] == nil {
                        results[u.userId] = m.messageRowId
                    } else {
                        if results[u.userId]! < m.messageRowId {
                            results[u.userId] = m.messageRowId
                        }
                    }
                }
            }
        }
        
        return results
    }
}
