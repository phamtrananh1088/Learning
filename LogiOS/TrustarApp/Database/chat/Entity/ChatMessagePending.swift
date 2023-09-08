//
//  ChatMessagePending.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum ChatMessagePendingColumns: String, ColumnExpression, CaseIterable {
    case id = "id", createdDate = "created_date", status = "status", roomId = "room_id"
    case userId = "user_id", text = "text", targetId = "target_id", type = "type", ext = "ext"
    
    func name() -> String {
        return String(describing: self)
    }
}

class ChatMessagePending: BaseEntity, Codable {
    var id: Int64? = nil
    var createdDate: Int64 = 0
    var status: Int = Resources.zeroNumber
    var roomId: String = Resources.strEmpty
    var userId: String = Resources.strEmpty
    var text: String = Resources.strEmpty
    var targetId: String? = nil
    var type: Int = Resources.zeroNumber //0:text, 1:send_img, 2:img, 3:send_audio, 4:audio, 5: send_video, 6: video
    var ext: String? = nil
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String { "chat_message_pending" }
    
    override class var primaryKey: String {"id"}
    override class var parentTable: String { ChatRoom.databaseTableName}
    override class var parentColumns: String {"id"}
    override class var childColumns: String {"room_id"}
    
    required init(row: Row) {
        id = row[ChatMessagePendingColumns.id]
        createdDate = row[ChatMessagePendingColumns.createdDate]
        status = row[ChatMessagePendingColumns.status]
        roomId = row[ChatMessagePendingColumns.roomId]
        userId = row[ChatMessagePendingColumns.userId]
        text = row[ChatMessagePendingColumns.text]
        targetId = row[ChatMessagePendingColumns.targetId]
        type = row[ChatMessagePendingColumns.type]
        ext = row[ChatMessagePendingColumns.ext]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[ChatMessagePendingColumns.id] = id
        container[ChatMessagePendingColumns.createdDate] = createdDate
        container[ChatMessagePendingColumns.status] = status
        container[ChatMessagePendingColumns.roomId] = roomId
        container[ChatMessagePendingColumns.userId] = userId
        container[ChatMessagePendingColumns.text] = text
        container[ChatMessagePendingColumns.targetId] = targetId
        container[ChatMessagePendingColumns.type] = type
        container[ChatMessagePendingColumns.ext] = ext
        super.encode(to: &container)
    }
    
    init(id: Int64?,
         createdDate: Int64,
         status: Int,
         roomId: String,
         userId: String,
         text: String,
         targetId: String?,
         type: Int,
         ext: String?
    ) {
        self.id = id
        self.createdDate = createdDate
        self.status = status
        self.roomId = roomId
        self.userId = userId
        self.text = text
        self.targetId = targetId
        self.type = type
        self.ext = ext
        
        super.init()
    }
    
    func jsonBody() -> String {
        
        if let msgType = MessagePendingTypeEnum(rawValue: type) {
            switch(msgType) {
            
            case .TYPE_IMG_SEND, .TYPE_AUDIO_SEND, .TYPE_VIDEO_SEND, .TYPE_FILE_SEND:
                return ""
                
            case .TYPE_IMG:
                let media = Media(fileKey: ext,
                                  messageItem: MessageItem(identity: id!,
                                                           talkRoomId: roomId,
                                                           userId: userId,
                                                           messageClass: "I",
                                                           createdDatetime: createdDate))
                return Helper.Shared.convertObjectToJson(body: media) ?? ""
            case .TYPE_AUDIO:
                let media = Media(fileKey: ext,
                                  messageItem: MessageItem(identity: id!,
                                                           talkRoomId: roomId,
                                                           userId: userId,
                                                           messageClass: "A",
                                                           createdDatetime: createdDate))
                return Helper.Shared.convertObjectToJson(body: media) ?? ""
                
            case .TYPE_VIDEO:
                let media = Media(fileKey: ext,
                                  messageItem: MessageItem(identity: id!,
                                                           talkRoomId: roomId,
                                                           userId: userId,
                                                           messageClass: "V",
                                                           createdDatetime: createdDate))
                return Helper.Shared.convertObjectToJson(body: media) ?? ""
                
            case .TYPE_FILE:
                let media = Media(fileKey: ext,
                                  messageItem: MessageItem(identity: id!,
                                                           talkRoomId: roomId,
                                                           userId: userId,
                                                           messageClass: "F",
                                                           createdDatetime: createdDate))
                return Helper.Shared.convertObjectToJson(body: media) ?? ""
                
            case .TYPE_TEXT:
                let media = Media(fileKey: nil,
                                  messageItem: MessageItem(identity: id!,
                                                           talkRoomId: roomId,
                                                           userId: userId,
                                                           messageClass: "T",
                                                           createdDatetime: createdDate,
                                                           messageText: text))
                return Helper.Shared.convertObjectToJson(body: media) ?? ""
            }
        }
        
        return ""
    }
    
    class Media: Codable {
        var fileKey: String?
        var messageItem: MessageItem
        var userInfo: UserInfo.Resutl
        
        init(fileKey: String?, messageItem: MessageItem) {
            self.fileKey = fileKey
            self.messageItem = messageItem
            self.userInfo = Current.Shared.userInfo().result()
        }
    }
    
    class MessageItem: Codable {
        var identity: Int64
        var talkRoomId: String
        var userId: String
        var messageText: String?
        var messageClass: String
        var createdDatetime: Int64
        
        init(identity: Int64,
             talkRoomId: String,
             userId: String,
             messageClass: String,
             createdDatetime: Int64,
             messageText: String? = nil
        ) {
            self.identity = identity
            self.talkRoomId = talkRoomId
            self.userId = userId
            self.messageClass = messageClass
            self.createdDatetime = createdDatetime
            self.messageText = messageText
        }
    }
    
    func textAsAttachment(message: ChatMessagePending) -> Attachment? {
        let data = message.text.data(using: .utf8)
        if data == nil { return nil }
        return Helper.Shared.tryConvertJsonToObject(json: data!)
    }

    func textAsImg(message: ChatMessagePending) -> Img? {
        let data = message.text.data(using: .utf8)
        if data == nil { return nil }
        return Helper.Shared.tryConvertJsonToObject(json: data!)
    }

    func attachment(message: ChatMessagePending) -> Attachment? {
        if let msgType = MessagePendingTypeEnum(rawValue: type) {
            switch (msgType) {
            case .TYPE_IMG_SEND, .TYPE_IMG:
                return textAsImg(message: message)?.attachment
            default: break
            }
        }
        
        return textAsAttachment(message: message)
    }

    func attachmentExt(message: ChatMessagePending) -> AttachmentExt? {
        if let att = attachment(message: message) {
            if let msgType = MessagePendingTypeEnum(rawValue: message.type) {
                switch msgType {
                case .TYPE_IMG_SEND, .TYPE_AUDIO_SEND, .TYPE_VIDEO_SEND, .TYPE_FILE_SEND:
                    let relativePath = "/\(att.key)/"
                    let f = Current.Shared.userInfo().userLocalFileDir!.appendingPathComponent(relativePath, isDirectory: true)
                    return AttachmentExt(file: f, isCacheFile: false, key: att.key, name: att.name, size: att.size)
                    
                case .TYPE_AUDIO, .TYPE_VIDEO, .TYPE_FILE:
                    let f = Current.Shared.userInfo().cacheByKey(fileKey: att.key, isFile: false).file
                    return AttachmentExt(file: f, isCacheFile: true, key: att.key, name: att.name, size: att.size)
                case .TYPE_IMG:
                    let f = Current.Shared.userInfo().cacheByKey(fileKey: att.key, isFile: true).file
                    return AttachmentExt(file: f, isCacheFile: true, key: att.key, name: att.name, size: att.size)
                default: break
                }
            }
        }
        
        return nil
    }

    private func createMessage(roomId: String, type: Int, text: String) -> ChatMessagePending {
        return ChatMessagePending(
            id: nil,
            createdDate: currentTimeMillis(),
            status: MessagePendingStateEnum.STATE_NORMAL.rawValue,
            roomId: roomId,
            userId: Current.Shared.userId(),
            text: text,
            targetId: nil,
            type: type,
            ext: nil
        )
    }

    func textMessage(roomId: String, text: String) -> ChatMessagePending {
        return createMessage(roomId: roomId, type: MessagePendingTypeEnum.TYPE_TEXT.rawValue, text: text)
    }

    func imageMessage(roomId: String, localImg: Img) -> ChatMessagePending {
        return createMessage(roomId: roomId, type: MessagePendingTypeEnum.TYPE_IMG_SEND.rawValue, text: Helper.Shared.convertObjectToJson(body: localImg)!)
    }

    func audioMessage(roomId: String, localFile: Attachment) -> ChatMessagePending {
        return createMessage(roomId: roomId, type: MessagePendingTypeEnum.TYPE_AUDIO_SEND.rawValue, text: Helper.Shared.convertObjectToJson(body: localFile)!)
    }

    func videoMessage(roomId: String, localFile: Attachment) -> ChatMessagePending {
        return createMessage(roomId: roomId, type: MessagePendingTypeEnum.TYPE_VIDEO_SEND.rawValue, text: Helper.Shared.convertObjectToJson(body: localFile)!)
    }

    func fileMessage(roomId: String, localFile: Attachment) -> ChatMessagePending {
        return createMessage(roomId: roomId, type: MessagePendingTypeEnum.TYPE_FILE_SEND.rawValue, text: Helper.Shared.convertObjectToJson(body: localFile)!)
    }

    private func keyMessage(
        update: ChatMessagePending,
        type: Int,
        newText: String,
        key: String
    ) -> ChatMessagePending {
        return ChatMessagePending(
            id: update.id!,
            createdDate: update.createdDate,
            status: MessagePendingStateEnum.STATE_NORMAL.rawValue,
            roomId: update.roomId,
            userId: update.userId,
            text: newText,
            targetId: nil,
            type: type,
            ext: key
        )
    }

    func imageKeyMessage(m: ChatMessagePending, key: String) -> ChatMessagePending {
        return keyMessage(update: m, type: MessagePendingTypeEnum.TYPE_IMG.rawValue, newText: m.text, key: key)
    }

    func audioKeyMessage(m: ChatMessagePending, key: String) -> ChatMessagePending {
        return keyMessage(update: m, type: MessagePendingTypeEnum.TYPE_AUDIO.rawValue, newText: m.text, key: key)
    }

    func videoKeyMessage(
        m: ChatMessagePending,
        cacheFile: Attachment,
        key: String
    ) -> ChatMessagePending {
        return keyMessage(update: m, type: MessagePendingTypeEnum.TYPE_VIDEO.rawValue, newText: Helper.Shared.convertObjectToJson(body: cacheFile)!, key: key)
    }

    func fileKeyMessage(
        m: ChatMessagePending,
        cacheFile: Attachment,
        key: String
    ) -> ChatMessagePending {
        return keyMessage(update: m, type: MessagePendingTypeEnum.TYPE_FILE.rawValue, newText: Helper.Shared.convertObjectToJson(body: cacheFile)!, key: key)
    }
    
    func getType() -> MessagePendingTypeEnum {
        return MessagePendingTypeEnum.init(rawValue: type)!
    }
}
