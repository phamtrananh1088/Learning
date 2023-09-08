//
//  ChatMessage.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum ChatMessageColumns: String, ColumnExpression, CaseIterable {
    case id = "id", roomId = "room_id", userId = "user_id", text = "text"
    case createdDate = "created_date", messageRow = "message_row", type = "type"
    
    func name() -> String {
        return String(describing: self)
    }
}

class ChatMessage: BaseEntity, Codable {
    
    var id: String = Resources.strEmpty
    var roomId: String = Resources.strEmpty
    var userId: String = Resources.strEmpty
    var text: String? = nil
    var createdDate: Int64 = 0
    var messageRow: String = Resources.strEmpty
    var type: Int = Resources.zeroNumber //0:text, 1:img, 2:audio, 3:video, 4:file
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"chat_message"}
    
    override class var primaryKey: String {"id"}
    override class var parentTable: String { ChatRoom.databaseTableName}
    override class var parentColumns: String {"id"}
    override class var childColumns: String {"room_id"}
    
    required init(row: Row) {
        id = row[ChatMessageColumns.id]
        roomId = row[ChatMessageColumns.roomId]
        userId = row[ChatMessageColumns.userId]
        text = row[ChatMessageColumns.text]
        createdDate = row[ChatMessageColumns.createdDate]
        messageRow = row[ChatMessageColumns.messageRow]
        type = row[ChatMessageColumns.type]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[ChatMessageColumns.id] = id
        container[ChatMessageColumns.roomId] = roomId
        container[ChatMessageColumns.userId] = userId
        container[ChatMessageColumns.text] = text
        container[ChatMessageColumns.createdDate] = createdDate
        container[ChatMessageColumns.messageRow] = messageRow
        container[ChatMessageColumns.type] = type
        super.encode(to: &container)
    }
    
    init(id: String,
         roomId: String,
         userId: String,
         text: String?,
         createdDate: Int64,
         messageRow: String,
         type: Int
    ) {
        self.id = id
        self.roomId = roomId
        self.userId = userId
        self.text = text
        self.createdDate = createdDate
        self.messageRow = messageRow
        self.type = type
        
        super.init()
    }
    
    func isSelf() -> Bool {
        return userId == Current.Shared.userId()
    }
    
    func textAsAttachment(message: ChatMessage) -> Attachment? {
        if message.text == nil { return nil }
        let data = message.text!.data(using: .utf8)
        if data == nil { return nil }
        return Helper.Shared.convertJsonToObject(json: data!)
    }

    func textAsImg(message: ChatMessage) -> Img? {
        if message.text == nil { return nil }
        let data = message.text!.data(using: .utf8)
        if data == nil { return nil }
        return Helper.Shared.convertJsonToObject(json: data!)
    }

    func attachment(message: ChatMessage) -> Attachment? {
        if let msgType = MessageTypeEnum(rawValue: message.type) {
            switch(msgType) {
            case .TYPE_IMG:
                return textAsImg(message: message)?.attachment
            case .TYPE_AUDIO, .TYPE_VIDEO, .TYPE_FILE:
                return textAsAttachment(message: message)
                
            default:
                return nil
            }
        }
        
        return nil
    }

    func attachmentFile(message: ChatMessage) -> AttachmentExt? {
        if let att = attachment(message: message) {
            return AttachmentExt(file: Current.Shared.userInfo().cacheByKey(fileKey: att.key, isFile: message.isImg()).file,
                                 isCacheFile: true,
                                 key: att.key, name: att.name, size: att.size)
        }
        
        return nil
    }
    
    func isImg() -> Bool {
        return type == 1
    }
}
