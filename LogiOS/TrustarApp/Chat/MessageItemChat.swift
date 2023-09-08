//
//  MessageItemChat.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/28.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

protocol TextProtocol {
    var text: String? { get set }
}

protocol DlProtocol {
    func attachmentExt() -> AttachmentExt?
}

protocol ImageProtocol {
    var imageUri: ImageURI? { get set }
    var ratio: Float { get set }
}

protocol VideoProtocol : DlProtocol {

}

protocol AudioProtocol : DlProtocol {

}

protocol FProtocol : DlProtocol {

}

class MessageItemChat: Equatable, Hashable {
    let uuid: String
    static func == (lhs: MessageItemChat, rhs: MessageItemChat) -> Bool {
        return lhs.uuid == rhs.uuid
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(uuid)
    }
    
    var listId: String?
    var dbText: String
    var selfField: Bool
    var sentDate: Int64

    var status: String?
    var hasPrev: Bool
    var hasNext: Bool
    
    init(listId: String?, dbText: String, selfField: Bool, sentDate: Int64, status: String?, hasPrev: Bool, hasNext: Bool) {
        self.listId = listId
        self.dbText = dbText
        self.selfField = selfField
        self.sentDate = sentDate
        self.status = status
        self.hasPrev = hasPrev
        self.hasNext = hasNext
        
        self.uuid = UUID().uuidString
    }
    
    func dateStr() -> String {
        if (daysBetween(from: currentTimeMillis(), to: sentDate) == 0) {
            return Config.Shared.dateFormatterHHmm.format(date: Date(miliseconds: sentDate))
        } else {
            return Config.Shared.dateFormatterMMdd.format(date: Date(miliseconds: sentDate))
        }
    }
    
    /** @see [DiffUtil.ItemCallback.areContentsTheSame] */
    func sameContent(other: MessageItemChat) -> Bool {
        return other.dbText == dbText &&
        other.hasPrev == hasPrev &&
        other.hasNext == hasNext &&
        other.status == status &&
        other.selfField == selfField &&
        other.dateStr() == dateStr()
    }
    
    /** @see [DiffUtil.ItemCallback.getChangePayload] */
    func getPayload(other: MessageItemChat) -> Int? {
        var l = 0
        if (other.dbText != dbText) {
            l = l | PayLoadEnum.PAYLOAD_CONTENT.rawValue
        }
        if (other.status != status) {
            l = l | PayLoadEnum.PAYLOAD_STATUS.rawValue
        }
        if (other.dateStr() != dateStr()) {
            l = l | PayLoadEnum.PAYLOAD_DATE.rawValue
        }
        return l == 0 ? nil : l
    }
    
    
    
    class Pending : MessageItemChat {
        var pending: ChatMessagePending
        
        init(pending: ChatMessagePending, hasPrev: Bool, hasNext: Bool) {
            self.pending = pending
            
            var status = ""
            switch MessagePendingStateEnum.init(rawValue: pending.status)! {
            case .STATE_SENDING:
                status = Resources.msg_sending
            case .STATE_ERR:
                status = Resources.msg_tap_to_retry
            default:
                break
            }
            
            super.init(listId: pending.targetId,
                       dbText: pending.text,
                       selfField: true,
                       sentDate: pending.createdDate,
                       status: status,
                       hasPrev: hasPrev,
                       hasNext: hasNext)
        }
        
        func isSending() -> Bool {
            return pending.status == MessagePendingStateEnum.STATE_SENDING.rawValue
        }
        
        override func sameContent(other: MessageItemChat) -> Bool {
            return other is Pending && super.sameContent(other: other)
        }
        
        func attachmentExt() -> AttachmentExt? {
            return pending.attachmentExt(message: pending)
        }
        
        class ImgPending: Pending, ImageProtocol {
            var imageUri: ImageURI?
            var ratio: Float = Float(1)
            
            override init(pending: ChatMessagePending, hasPrev: Bool, hasNext: Bool) {
                super.init(pending: pending, hasPrev: hasPrev, hasNext: hasNext)
                
                let attEx = attachmentExt()
                if let file = attEx?.file {
                    self.imageUri = ImageURI(uri: file, handle: false)
                }
                
                self.ratio = pending.textAsImg(message: pending)?.ratio() ?? Float(1)
            }
        }
        
        class AudioPending: Pending, AudioProtocol {
            override init(pending: ChatMessagePending, hasPrev: Bool, hasNext: Bool) {
                super.init(pending: pending, hasPrev: hasPrev, hasNext: hasNext)
            }
        }
        
        class TextPending: Pending, TextProtocol {
            var text: String?
            
            override init(pending: ChatMessagePending, hasPrev: Bool, hasNext: Bool) {
                self.text = pending.text
                super.init(pending: pending, hasPrev: hasPrev, hasNext: hasNext)
            }
        }

        class VideoPending : Pending, VideoProtocol {
            override init(pending: ChatMessagePending, hasPrev: Bool, hasNext: Bool) {
                super.init(pending: pending, hasPrev: hasPrev, hasNext: hasNext)
            }
        }

        class FilePending: Pending, FProtocol {
            override init(pending: ChatMessagePending, hasPrev: Bool, hasNext: Bool) {
                super.init(pending: pending, hasPrev: hasPrev, hasNext: hasNext)
            }
        }
    }
    
    class Sent: MessageItemChat {
        var msg: ChatMessage
        
        var avatar: ImageURI?
        
        var username: String?
        
        var readers: [ChatUser]
        
        internal var attachmentExt: AttachmentExt?

        override func sameContent(other: MessageItemChat) -> Bool {
            return other is Sent && super.sameContent(other: other)
        }
        
        init(msg: ChatMessage, user: ChatUser?, readers: [ChatUser?], hasPrev: Bool, hasNext: Bool) {
            self.msg = msg
            
            self.avatar = user?.avatarUri()
            self.username = user?.name
            self.readers = readers.compactMap({$0})
            self.attachmentExt = msg.attachmentFile(message: msg)
            
            var status : String?
            if readers.isEmpty {
                status = nil
            } else {
                let c = self.readers.count
                if c == 0 {
                    status = nil
                } else {
                    status = Resources.msg_been_read
                    if c != readers.count {
                        status!.append(String(c))
                    }
                }
            }
            
            super.init(listId: msg.id,
                       dbText: msg.text ?? Resources.strEmpty,
                       selfField: msg.isSelf(),
                       sentDate: msg.createdDate,
                       status: status,
                       hasPrev: hasPrev,
                       hasNext: hasNext)
        }
        
        class ImgSent: Sent, ImageProtocol {
            var imageUri: ImageURI?
            var ratio: Float
            
            override init(msg: ChatMessage, user: ChatUser?, readers: [ChatUser?], hasPrev: Bool, hasNext: Bool) {
                let img = msg.textAsImg(message: msg)
                self.imageUri = img.map({ ImageURI(uri: URL(string: $0.url)!, handle: true)})
                self.ratio = img?.ratio() ?? Float(1)
                
                super.init(msg: msg, user: user, readers: readers, hasPrev: hasPrev, hasNext: hasNext)
            }
        }
        
        class VideoSent: Sent, VideoProtocol {
            func attachmentExt() -> AttachmentExt? {
                return attachmentExt
            }
            
            override init(msg: ChatMessage, user: ChatUser?, readers: [ChatUser?], hasPrev: Bool, hasNext: Bool) {
                super.init(msg: msg, user: user, readers: readers, hasPrev: hasPrev, hasNext: hasNext)
            }
        }
        
        class AudioSent: Sent, AudioProtocol {
            func attachmentExt() -> AttachmentExt? {
                return attachmentExt
            }
            
            override init(msg: ChatMessage, user: ChatUser?, readers: [ChatUser?], hasPrev: Bool, hasNext: Bool) {
                super.init(msg: msg, user: user, readers: readers, hasPrev: hasPrev, hasNext: hasNext)
            }
        }
        
        class TextSent: Sent, TextProtocol {
            var text: String?
            
            override init(msg: ChatMessage, user: ChatUser?, readers: [ChatUser?], hasPrev: Bool, hasNext: Bool) {
                
                self.text = msg.text
                super.init(msg: msg, user: user, readers: readers, hasPrev: hasPrev, hasNext: hasNext)
            }
        }

        class FileSent: Sent, FProtocol {
            func attachmentExt() -> AttachmentExt? {
                attachmentExt
            }
            
            override init(msg: ChatMessage, user: ChatUser?, readers: [ChatUser?], hasPrev: Bool, hasNext: Bool) {
                super.init(msg: msg, user: user, readers: readers, hasPrev: hasPrev, hasNext: hasNext)
            }
        }
    }
}
