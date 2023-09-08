//
//  MessageItem.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class MessageItem: Codable {
    var messageId: String
    var talkRoomId: String
    var messageRowId: String
    var userId: String
    var messageText: String?
    var messageClass: String?
    var createdDatetime: Int64?
    var sentDatetime: Int64?
    var files: [F]?
    
    init(messageId: String,
         talkRoomId: String,
         messageRowId: String,
         userId: String,
         messageText: String?,
         messageClass: String?,
         createdDatetime: Int64?,
         sentDatetime: Int64?,
         files: [F]?
    ) {
        self.messageId = messageId
        self.talkRoomId = talkRoomId
        self.messageRowId = messageRowId
        self.userId = userId
        self.messageText = messageText
        self.messageClass = messageClass
        self.createdDatetime = createdDatetime
        self.sentDatetime = sentDatetime
        self.files = files
    }
    
    class F: Codable {
        var fileKey: String
        var fileName: String?
        var fileSize: Int64?
        
        //for image
        var fileUrl: String
        var width: Int
        var height: Int
        
        init(fileKey: String, fileName: String?, fileSize: Int64?, fileUrl: String, width: Int, height: Int) {
            self.fileKey = fileKey
            self.fileName = fileName
            self.fileSize = fileSize
            self.fileUrl = fileUrl
            self.width = width
            self.height = height
        }
        
        func asAttachment() -> Attachment {
            return Attachment(key: fileKey, name: fileName ?? "", size: fileSize ?? 0)
        }
        
        func asImg() -> Img {
            return Img(attachment: asAttachment(), url: fileUrl, width: width, height: height)
        }
    }
    
    func message() -> ChatMessage {
        let file = files?.first
        
        switch (messageClass?.lowercased() ?? "") {
        case "i":
            return ChatMessage(id: messageId,
                               roomId: talkRoomId,
                               userId: userId,
                               text: file == nil ? nil : Helper.Shared.convertObjectToJson(body: file!.asImg()),
                               createdDate: createdDatetime ?? 0,
                               messageRow: messageRowId,
                               type: MessageTypeEnum.TYPE_IMG.rawValue)
        case "v":
            return ChatMessage(id: messageId,
                               roomId: talkRoomId,
                               userId: userId,
                               text: file == nil ? nil : Helper.Shared.convertObjectToJson(body: file!.asAttachment()),
                               createdDate: createdDatetime ?? 0,
                               messageRow: messageRowId,
                               type: MessageTypeEnum.TYPE_VIDEO.rawValue)
        case "a":
            return ChatMessage(id: messageId,
                               roomId: talkRoomId,
                               userId: userId,
                               text: file == nil ? nil : Helper.Shared.convertObjectToJson(body: file!.asAttachment()),
                               createdDate: createdDatetime ?? 0,
                               messageRow: messageRowId,
                               type: MessageTypeEnum.TYPE_AUDIO.rawValue)
        case "f":
            return ChatMessage(id: messageId,
                               roomId: talkRoomId,
                               userId: userId,
                               text: file == nil ? nil : Helper.Shared.convertObjectToJson(body: file!.asAttachment()),
                               createdDate: createdDatetime ?? 0,
                               messageRow: messageRowId,
                               type: MessageTypeEnum.TYPE_FILE.rawValue)
        default:
            return ChatMessage(id: messageId,
                               roomId: talkRoomId,
                               userId: userId,
                               text: messageText,
                               createdDate: createdDatetime ?? 0,
                               messageRow: messageRowId,
                               type: MessageTypeEnum.TYPE_TEXT.rawValue)
        }
    }
}
