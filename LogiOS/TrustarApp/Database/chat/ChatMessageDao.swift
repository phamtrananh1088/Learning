//
//  ChatMessageDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum ChatMessageSql: SqlProtocol {
    
    case messageOldest, messageLatest
    case messagePrev, messageNext
    case deleteAll
    case messageBetween
        
    func makeQuery() -> String {
        switch self {
        case .messageOldest:
            return "select * from chat_message where room_id = :roomId order by message_row limit :size"
        case .messageLatest:
            return "select * from chat_message where room_id = :roomId order by message_row desc limit :size"
        case .messagePrev:
            return "select * from chat_message where room_id = :roomId and message_row < :messageRow order by message_row desc limit :size"
        case .messageNext:
            return "select * from chat_message where room_id = :roomId and message_row > :messageRow order by message_row limit :size"
        case .deleteAll:
            return "delete from chat_message where room_id = :roomId "
        case .messageBetween:
            return "select * from chat_message where room_id = :roomId and message_row between :startR and :endR"
        }
    }
}

class ChatMessageDao: BaseDbDao<ChatDb> {
    
    func messageOldest(roomId: String, size: Int) -> [ChatMessage] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatMessage.fetchAll(db,
                                                sql: ChatMessageSql.messageOldest.makeQuery(),
                                                arguments: ["roomId": roomId, "size": size])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func messageLatest(roomId: String, size: Int) -> [ChatMessage] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatMessage.fetchAll(db,
                                                sql: ChatMessageSql.messageLatest.makeQuery(),
                                                arguments: ["roomId": roomId, "size": size])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func messagePrev(roomId: String, size: Int, messageRow: String) -> [ChatMessage] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatMessage.fetchAll(db,
                                                sql: ChatMessageSql.messagePrev.makeQuery(),
                                                arguments: ["roomId": roomId, "messageRow": messageRow, "size": size])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func messageNext(roomId: String, size: Int, messageRow: String) -> [ChatMessage] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatMessage.fetchAll(db,
                                                sql: ChatMessageSql.messageNext.makeQuery(),
                                                arguments: ["roomId": roomId, "messageRow": messageRow, "size": size])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func deleteById(db: Database, ids: [String]) {
        do {
            try ChatMessage.deleteAll(db, keys: ids)
        } catch {
            debugPrint(error)
        }
    }
    
    func deleteAll(roomId: String) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: ChatMessageSql.deleteAll.makeQuery(), arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func messageBetween(roomId: String, startR: String, endR: String) -> [ChatMessage] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatMessage.fetchAll(db,
                                                sql: ChatMessageSql.messageBetween.makeQuery(),
                                                arguments: ["roomId": roomId, "startR": startR, "endR": endR])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
}


