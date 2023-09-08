//
//  ChatRoomDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import GRDB

enum ChatRoomSql: SqlProtocol {
    case selectRoomList
    case selectRoom
    case deleteAll
    case deleteNot
    case deleteByRoomId
    
    func makeQuery() -> String {
        switch self {
            
        case .selectRoomList:
            return "select * from chat_room order by last_updated desc"
        case .selectRoom:
            return "select * from chat_room where id = :roomId"
        case .deleteAll:
            return "delete from chat_room"
        case .deleteNot:
            return "delete from chat_room where version != :version"
        case .deleteByRoomId:
            return "delete from chat_room where id = :roomId"
        }
    }
}

class ChatRoomDao: BaseDbDao<ChatDb>  {
    
    func selectRoomList() -> AnyPublisher<[ChatRoomExt], Error> {
        return ValueObservation.tracking { db in
            try ChatRoom.fetchAll(db, sql: ChatRoomSql.selectRoomList.makeQuery())
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .map { lst -> [ChatRoomExt] in
            var lstReturn: [ChatRoomExt] = []
            for it in lst {
                lstReturn.append(ChatRoomExt(room: it))
            }
            return lstReturn
        }
        .eraseToAnyPublisher()
    }
    
    func selectRoom(roomId: String) -> AnyPublisher<Optional<ChatRoom>, Error> {
        return ValueObservation.tracking { db in
            try ChatRoom.fetchOne(db, sql: ChatRoomSql.selectRoom.makeQuery(), arguments: ["roomId": roomId])
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .map { c in c }
        .eraseToAnyPublisher()
    }
    
    func deleteAll(db: Database) {
        do {
            try ChatRoom.deleteAll(db)
        } catch {
            debugPrint(error)
        }
    }
    
    func deleteNot(db: Database, version: String) {
        do {
            try db.execute(sql: ChatRoomSql.deleteNot.makeQuery(), arguments: ["version": version])
        } catch {
            debugPrint(error)
        }
    }
    
    func deleteByRoomId(roomId: String) {
        do {
            _ = try executeDb.instanceDb!.inDatabase { db in
                try db.execute(sql: ChatRoomSql.deleteByRoomId.makeQuery(), arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
        }
    }
}
