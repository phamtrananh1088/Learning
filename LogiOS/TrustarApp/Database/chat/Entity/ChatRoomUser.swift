//
//  ChatRoomUser.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum ChatRoomUserColumns: String, ColumnExpression, CaseIterable {
    case roomId = "room_id", userId = "user_id", readMessageRowId = "read_message_row"
    
    func name() -> String {
        return String(describing: self)
    }
}

class ChatRoomUser: BaseEntity, Codable {
    var roomId: String = Resources.strEmpty
    var userId: String = Resources.strEmpty
    var readMessageRowId: String? = nil
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"chat_room_user"}
    
    override class var primaryKey: String {"room_id,user_id"}
    override class var parentTable: String { ChatRoom.databaseTableName}
    override class var parentColumns: String {"id"}
    override class var childColumns: String {"room_id"}
    
    required init(row: Row) {
        roomId = row[ChatRoomUserColumns.roomId]
        userId = row[ChatRoomUserColumns.userId]
        readMessageRowId = row[ChatRoomUserColumns.readMessageRowId]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[ChatRoomUserColumns.roomId] = roomId
        container[ChatRoomUserColumns.userId] = userId
        container[ChatRoomUserColumns.readMessageRowId] = readMessageRowId
        
        super.encode(to: &container)
    }
    
    init(roomId: String,
         userId: String,
         readMessageRowId: String?
    ) {
        self.roomId = roomId
        self.userId = userId
        self.readMessageRowId = readMessageRowId
        super.init()
    }
}

