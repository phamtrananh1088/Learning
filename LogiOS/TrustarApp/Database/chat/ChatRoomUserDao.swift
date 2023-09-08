//
//  ChatRoomUserDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import GRDB

enum ChatRoomUserSql: SqlProtocol {
    case userListByRoom
    case userListByRoomWithReadRow
    case queryByRoom
    case deleteAll
    
    
    func makeQuery() -> String {
        switch self {
            
        case .userListByRoom:
            return "select u.* from chat_user u where exists (select 1 from chat_room_user where room_id = :roomId and user_id = u.id)"
        case .userListByRoomWithReadRow:
            return "select u.*, r.read_message_row as last_row from chat_user u inner join chat_room_user r on r.user_id == u.id where r.room_id == :roomId"
        case .queryByRoom:
            return "select * from chat_room_user where room_id = :roomId"
        case .deleteAll:
            return "delete from chat_room_user"
        }
    }
}

class ChatRoomUserDao: BaseDbDao<ChatDb>  {
    func userListByRoom(roomId: String) -> AnyPublisher<[ChatUser], Error> {
        return ValueObservation.tracking { db in
            try ChatUser.fetchAll(db, sql: ChatRoomUserSql.userListByRoom.makeQuery(), arguments: ["roomId": roomId])
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .map { l in l }
        .eraseToAnyPublisher()
    }
    
    func userListByRoomWithReadRow(roomId: String) -> AnyPublisher<[ChatUserWithReadRow], Error> {
        return ValueObservation.tracking { db -> [Row] in
            let chatUserWidth = try db.columns(in: ChatUser.databaseTableName).count
            let chatUserAdapter = RangeRowAdapter(0 ..< chatUserWidth)
            
            let lastRowWidth = 1
            let lastRowAdapter = RangeRowAdapter(chatUserWidth ..< (lastRowWidth + chatUserWidth))
            
            let adapter = ScopeAdapter([
                ChatUser.databaseTableName: chatUserAdapter,
                "last_row": lastRowAdapter
            ])
            
            return try Row.fetchAll(db,
                                    sql: ChatRoomUserSql.userListByRoomWithReadRow.makeQuery(),
                                    arguments: ["roomId": roomId],
                                    adapter: adapter)
        }
        .publisher(in: executeDb.instanceDb!, scheduling: .immediate)
        .map { rows -> [ChatUserWithReadRow] in
            var listReturn: [ChatUserWithReadRow] = []
            for r in rows {
                listReturn.append(ChatUserWithReadRow(chatUser: r[ChatUser.databaseTableName], lastRow: r["last_row"]))
            }
            
            return listReturn
        }.eraseToAnyPublisher()
    }
    
    func queryByRoom(roomId: String) -> [ChatRoomUser] {
        do {
            return try executeDb.instanceDb!.inDatabase {db in
                return try ChatRoomUser.fetchAll(db,
                                             sql: ChatRoomUserSql.queryByRoom.makeQuery(),
                                             arguments: ["roomId": roomId])
            }
        } catch {
            debugPrint(error)
            return []
        }
    }
    
    func deleteAll(db: Database) {
        do {
            try ChatRoomUser.deleteAll(db)
        } catch {
            debugPrint(error)
        }
    }
}
