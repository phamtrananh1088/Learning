//
//  ChatMessagePendingDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import GRDB

enum ChatMessagePendingSql: SqlProtocol {
    
    case messageByRoom, firstUnsentOrSending
    case unsentOrSendingRoomIds, unsentRoomIdList
    case setStatus
    case resetErr
    case setStatusAndTargetId
    case deleteSent
    case messagePrev, messagePrevIncluded
    case messageNext
    case selectSent
    case selectById
    
    func makeQuery() -> String {
        switch self {
        
        case .messageByRoom:
            return "select * from chat_message_pending where room_id = :roomId order by created_date"
        case .firstUnsentOrSending:
            return "select * from chat_message_pending where room_id= :roomId and status < 2 order by created_date limit 1"
        case .unsentOrSendingRoomIds:
            return "select id from (select min(created_date), room_id id from chat_message_pending where status < 2 group by room_id)"
        case .unsentRoomIdList:
            return "select id from (select min(created_date), room_id id from chat_message_pending where status < 2 group by room_id having status = 0)"
        case .setStatus:
            return "update chat_message_pending set status = :status where id = :id"
        case .resetErr:
            return "update chat_message_pending set status = 0 where status = 2 and room_id = :roomId"
        case .setStatusAndTargetId:
            return "update chat_message_pending set status = :status, target_id = :targetId where id = :id"
        case .deleteSent:
            return "delete from chat_message_pending where status = 3 and room_id = :roomId"
        case .messagePrev:
            return "select * from chat_message_pending where room_id = :roomId and created_date < :createdDate"
        case .messagePrevIncluded:
            return "select * from chat_message_pending where room_id = :roomId and created_date <= :createdDate"
        case .messageNext:
            return "select * from chat_message_pending where room_id = :roomId and created_date > :createdDate"
        case .selectSent:
            return "select * from chat_message_pending where status = 3 and room_id = :roomId"
        case .selectById:
            return "select * from chat_message_pending where id = :id"
        }
    }
}

class ChatMessagePendingDao: BaseDbDao<ChatDb> {
    
    func messageByRoom(roomId: String) -> [ChatMessagePending] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatMessagePending.fetchAll(db,
                                                sql: ChatMessagePendingSql.messageByRoom.makeQuery(),
                                                arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func firstUnsentOrSending(roomId: String) -> ChatMessagePending? {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatMessagePending.fetchOne(db,
                                                sql: ChatMessagePendingSql.firstUnsentOrSending.makeQuery(),
                                                arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func unsentOrSendingRoomIds() -> AnyPublisher<[StringId], Error> {
        return ValueObservation.tracking { db in
            try String.fetchAll(db, sql: ChatMessagePendingSql.unsentOrSendingRoomIds.makeQuery())
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .map { lst -> [StringId] in
            var lstReturn: [StringId] = []
            for it in lst {
                lstReturn.append(StringId(id: it))
            }
            return lstReturn
        }
        .eraseToAnyPublisher()
    }

    func unsentRoomIdList() -> [StringId] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                try String
                    .fetchAll(db, sql: ChatMessagePendingSql.unsentRoomIdList.makeQuery())
                    .map { it in
                        return StringId(id: it)
                    }
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func setStatus(status: MessagePendingStateEnum, id: Int64) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: ChatMessagePendingSql.setStatus.makeQuery(), arguments: ["status": status.rawValue, "id" : id])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func resetErr(roomId: String) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: ChatMessagePendingSql.resetErr.makeQuery(), arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func setStatusAndTargetId(status: Int, targetId: String?, id: Int64) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: ChatMessagePendingSql.setStatusAndTargetId.makeQuery(),
                               arguments: ["status": status, "targetId": targetId, "id": id])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func deleteSent(roomId: String) {
        do {
            try executeDb.instanceDb?.write { db in
                try db.execute(sql: ChatMessagePendingSql.deleteSent.makeQuery(),
                               arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
        }
    }
    
    func messagePrev(roomId: String, createdDate: Int64) -> [ChatMessagePending] {
        do {
            return try executeDb.instanceDb!.inDatabase { db in
                return try ChatMessagePending.fetchAll(db,
                                                       sql: ChatMessagePendingSql.messagePrev.makeQuery(),
                                                       arguments: ["roomId": roomId, "createdDate": createdDate])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func messagePrevIncluded(roomId: String, createdDate: Int64) -> [ChatMessagePending] {
        do {
            return try executeDb.instanceDb!.inDatabase { db in
                return try ChatMessagePending.fetchAll(db,
                                                       sql: ChatMessagePendingSql.messagePrevIncluded.makeQuery(),
                                                       arguments: ["roomId": roomId, "createdDate": createdDate])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func messageNext(roomId: String, createdDate: Int64) -> [ChatMessagePending] {
        do {
            return try executeDb.instanceDb!.inDatabase { db in
                return try ChatMessagePending.fetchAll(db,
                                                       sql: ChatMessagePendingSql.messageNext.makeQuery(),
                                                       arguments: ["roomId": roomId, "createdDate": createdDate])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func selectSent(roomId: String) -> [ChatMessagePending] {
        do {
            return try executeDb.instanceDb!.inDatabase { db in
                return try ChatMessagePending.fetchAll(db,
                                                       sql: ChatMessagePendingSql.selectSent.makeQuery(),
                                                       arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func selectById(id: Int64) -> ChatMessagePending? {
        do {
            return try executeDb.instanceDb!.inDatabase { db in
                return try ChatMessagePending.fetchOne(db,
                                                       sql: ChatMessagePendingSql.selectById.makeQuery(),
                                                       arguments: ["id": id])
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
}
