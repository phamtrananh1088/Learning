//
//  ChatRepository.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/25.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class ChatRepository {
    private var api: ChatApi
    private var chatDB: ChatDb
    init(api: ChatApi, chatDB: ChatDb) {
        self.api = api
        self.chatDB = chatDB
    }
    
    func updateRead(messageRow: String) -> AnyPublisher<StateResult, NetworkError> {
        return api.updateRead(body: MessageItemRows(messageRowIds: [messageRow], userInfo: Current.Shared.userInfo()), apiKey: Current.Shared.loggedUser?.token ?? "")
    }
    
    func loadBottomMessage(roomId: String, pageSize: Int) -> AnyPublisher<Event<[MessageItem]>, NetworkError> {
        let oldest = localLatestSentMessage(roomId: roomId, pageSize: 1).first?.id
        let task: AnyPublisher<[MessageItem], NetworkError>
        if oldest != nil {
            task = api.getMessageNext(roomId: roomId, messageId: oldest!, pageSize: pageSize, apiKey: Current.Shared.loggedUser?.token ?? "")
        } else {
            task = api.getMessageItems(roomId: roomId, pageSize: pageSize, apiKey: Current.Shared.loggedUser?.token ?? "")
        }
        
        return insertMessagesConsumer(task: task)
    }
    
    func loadTopMessage(roomId: String, pageSize: Int)-> AnyPublisher<Event<[MessageItem]>, NetworkError> {
        let oldest = chatDB.chatMessageDao?.messageOldest(roomId: roomId, size: 1).first?.id
        let task: AnyPublisher<[MessageItem], NetworkError>
        if oldest != nil {
            task = api.getMessagePrevious(roomId: roomId, messageId: oldest!, pageSize: pageSize, apiKey: Current.Shared.loggedUser?.token ?? "")
        } else {
            task = api.getMessageItems(roomId: roomId, pageSize: pageSize, apiKey: Current.Shared.loggedUser?.token ?? "")
        }
        
        return insertMessagesConsumer(task: task)
    }

    private func insertMessagesConsumer(task: AnyPublisher<[MessageItem], NetworkError>) -> AnyPublisher<Event<[MessageItem]>, NetworkError> {
        return task
            .eraseToAnyPublisher()
            .map({ [self] msgs -> AnyPublisher<Event<[MessageItem]>, NetworkError> in
                insertMessages(iterable: msgs.map({ $0.message() }))
                return Just(Event.Value(value: msgs)).setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            })
            .switchToLatest()
            .replaceError(with: .Error(error: .unknow))
            .setFailureType(to: NetworkError.self)
            .eraseToAnyPublisher()
    }

    private func insertMessages(iterable: [ChatMessage]) {
        let gs = Dictionary.init(grouping: iterable, by: { $0.roomId })
        var deleted: [ChatMessagePending] = []
        
        do {
            for item in gs {
                let t = item.key
                let u = item.value
                try chatDB.instanceDb?.write { db in for c in u { try c.save(db) }}
                deleted += chatDB.chatMessagePendingDao!.selectSent(roomId: t)
                chatDB.chatMessagePendingDao?.deleteSent(roomId: t)
            }
            
            for d in deleted {
                let att = d.attachmentExt(message: d)
                if att?.isCacheFile == false {
                    if FileManager.default.fileExists(atPath: att!.file.path) {
                        do {
                            try FileManager.default.removeItem(atPath: att!.file.path)
                        } catch let removeError {
                            print("couldn't remove file at path", removeError)
                        }
                    }
                }
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func deletePendingMessage(pending: ChatMessagePending) {
        deletePendingMessage(pending: [pending])
    }
    
    func deleteMessage(msg: ChatMessage) -> AnyPublisher<StateResult, NetworkError> {
        let task = api.deleteMessage(body: DefaultPostContent.Message(messageId: msg.id, userInfo: Current.Shared.userInfo()), apiKey: Current.Shared.loggedUser?.token ?? "")
        
        return Future<StateResult, NetworkError> { promise in
            _ = task
                .subscribe(on: DispatchQueue.global())
                .sink(receiveCompletion: { completion in
                    
                }, receiveValue: { rs in
                    
                    if let path = msg.attachmentFile(message: msg)?.file.path {
                        if FileManager.default.fileExists(atPath: path) {
                            try? FileManager.default.removeItem(atPath: path)
                        }
                    }
                    
                    promise(.success(rs))
                })
        }.map {$0}.eraseToAnyPublisher()
    }
    
    private func deletePendingMessage(pending: [ChatMessagePending]) {
        do {
            try chatDB.instanceDb?.write { db in
                for p in pending {
                    try p.delete(db)
                    
                    if let path = p.attachmentExt(message: p)?.file.path {
                        if FileManager.default.fileExists(atPath: path) {
                            try FileManager.default.removeItem(atPath: path)
                        }
                    }
                }
            }
            
        } catch {
            debugPrint(error)
        }
    }
    
    func localPendingMessages(roomId: String) -> [ChatMessagePending] {
        return chatDB.chatMessagePendingDao!.messageByRoom(roomId: roomId)
    }
    
    func localPendingMessagesPrev(
        roomId: String,
        createdDate: Int64,
        includeSelf: Bool
    ) -> [ChatMessagePending] {
        return includeSelf
        ? chatDB.chatMessagePendingDao!.messagePrevIncluded(roomId: roomId, createdDate: createdDate)
        : chatDB.chatMessagePendingDao!.messagePrev(roomId: roomId, createdDate: createdDate)
    }
    
    func localPendingMessagesNext(roomId: String, createdDate: Int64) -> [ChatMessagePending] {
        return chatDB.chatMessagePendingDao!.messageNext(roomId: roomId, createdDate: createdDate)
    }
    
    func localLatestSentMessage(roomId: String, pageSize: Int) -> [ChatMessage] {
        return chatDB.chatMessageDao!.messageLatest(roomId: roomId, size: pageSize)
    }
    
    func localMessagesPrev(roomId: String, pageSize: Int, startR: String) -> [ChatMessage] {
        return chatDB.chatMessageDao!.messagePrev(roomId: roomId, size: pageSize, messageRow: startR)
    }
    
    func localMessagesNext(roomId: String, pageSize: Int, startR: String) -> [ChatMessage] {
        return chatDB.chatMessageDao!.messageNext(roomId: roomId, size: pageSize, messageRow: startR)
    }
    
    func localMessagesBetween(roomId: String, startR: String, endR: String) -> [ChatMessage] {
        return chatDB.chatMessageDao!.messageBetween(roomId: roomId, startR: startR, endR: endR)
    }
    
    func localMessagesBetweenAround(
        roomId: String,
        startR: String,
        endR: String,
        prepend: Int,
        append: Int
    ) -> [ChatMessage] {
//        let p = localMessagesPrev(roomId: roomId, pageSize: prepend, startR: startR)
        let b = localMessagesBetween(roomId: roomId, startR: startR, endR: endR)
        let n = localMessagesNext(roomId: roomId, pageSize: append, startR: endR)
        
        return b + n
    }
}
