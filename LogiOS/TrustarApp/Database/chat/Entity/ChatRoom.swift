//
//  ChatRoom.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum ChatRoomColumns: String, ColumnExpression, CaseIterable {
    case id = "id"
    case name = "name"
    case image = "room_image"
    case unread = "unread"
    case userCount = "user_count"
    case lastUpdated = "last_updated"
    case notification = "notification"
    case version = "version"
    
    func name() -> String {
        return String(describing: self)
    }
}

class ChatRoom: BaseEntity, Codable {
    var id: String = Resources.strEmpty
    var name: String? = nil
    var image: String? = nil
    var unread: Int = Resources.zeroNumber
    var userCount: Int = Resources.zeroNumber
    var lastUpdated: Int64 = 0
    var notification: Bool = false
    var version: String = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"chat_room"}
    
    override class var primaryKey: String { ChatRoomColumns.id.rawValue }
    
    required init(row: Row) {
        id = row[ChatRoomColumns.id]
        name = row[ChatRoomColumns.name]
        image = row[ChatRoomColumns.image]
        unread = row[ChatRoomColumns.unread]
        userCount = row[ChatRoomColumns.userCount]
        lastUpdated = row[ChatRoomColumns.lastUpdated]
        notification = row[ChatRoomColumns.notification]
        version = row[ChatRoomColumns.version]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[ChatRoomColumns.id] = id
        container[ChatRoomColumns.name] = name
        container[ChatRoomColumns.image] = image
        container[ChatRoomColumns.unread] = unread
        container[ChatRoomColumns.userCount] = userCount
        container[ChatRoomColumns.lastUpdated] = lastUpdated
        container[ChatRoomColumns.notification] = notification
        container[ChatRoomColumns.version] = version
        super.encode(to: &container)
    }
    
    init(id: String,
         name: String?,
         image: String?,
         unread: Int,
         userCount: Int,
         lastUpdated: Int64,
         notification: Bool,
         version: String
    ) {
        self.id = id
        self.name = name
        self.image = image
        self.unread = unread
        self.userCount = userCount
        self.lastUpdated = lastUpdated
        self.notification = notification
        self.version = version
        super.init()
    }
}
