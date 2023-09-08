//
//  ChatRoomRepository.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/25.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import GRDB

class ChatRoomRepository {
    private var api: ChatApi
    private var chatDB: ChatDb
    init(api: ChatApi, chatDB: ChatDb) {
        self.api = api
        self.chatDB = chatDB
    }
    
    func chatRoomWithUsers(roomId: String) -> AnyPublisher<ChatRoomWithUsers?, Error> {
        return chatDB.chatRoomDao!.selectRoom(roomId: roomId)
            .map { [self] r -> AnyPublisher<ChatRoomWithUsers?, Error> in
                if r == nil { return Just(nil).setFailureType(to: Error.self).eraseToAnyPublisher()}
                return chatDB.chatRoomUserDao!.userListByRoom(roomId: r!.id)
                    .map { u in
                        ChatRoomWithUsers(room: r!, users: u)
                    }
                .eraseToAnyPublisher()
            }
            .switchToLatest()
            .eraseToAnyPublisher()
    }
    
    func chatRoomWithUsers2(roomId: String)-> AnyPublisher<ChatRoomWithUsers2?, Error> {
        return chatDB.chatRoomDao!.selectRoom(roomId: roomId)
            .map { [self] r -> AnyPublisher<ChatRoomWithUsers2?, Error> in
                if r == nil { return Just(nil).setFailureType(to: Error.self).eraseToAnyPublisher()}
                return chatDB.chatRoomUserDao!.userListByRoomWithReadRow(roomId: r!.id)
                    .map { u in
                        ChatRoomWithUsers2(room: r!, users: u)
                    }
                .eraseToAnyPublisher()
            }
            .switchToLatest()
            .eraseToAnyPublisher()
    }
    
    func reloadRooms() -> AnyPublisher<StateResult, NetworkError> {
        return api.getData(apiKey: Current.Shared.loggedUser?.token ?? "")
            .map {[self] rooms -> AnyPublisher<StateResult, NetworkError> in
                var delRows: [String] = []
                
                do {
                    var rs: [ChatRoom] = []
                    var us: [ChatUser] = []
                    var cs: [ChatRoomUser] = []
                    let version = UUID().uuidString
                    
                    for r in rooms {
                        rs.append(ChatRoom(id: r.talkRoomId,
                                           name: r.talkRoomName,
                                           image: r.talkRoomImageUrl,
                                           unread: r.unreadCount ?? 0,
                                           userCount: r.userCount ?? 0,
                                           lastUpdated: r.lastUpdateDatetime ?? 0,
                                           notification: r.notificationFlag ?? false,
                                           version: version))
                        
                        if let users = r.users {
                            for u in users {
                                cs.append(ChatRoomUser(roomId: r.talkRoomId, userId: u.userId, readMessageRowId: r.readRowsByUser()[u.userId]))
                                us.append(ChatUser(id: u.userId, name: u.userName, mail: u.mailAddress, avatar: u.avatarImageUrl))
                            }
                        }
                    }
                    
                    try chatDB.instanceDb?.write {db in
                        for r in rs {
                            try r.save(db)
                        }
                        chatDB.chatRoomDao?.deleteNot(db: db, version: version)
                        
                        chatDB.chatUserDao?.deleteAll(db: db)
                        for u in us {
                            try u.save(db)
                        }

                        chatDB.chatRoomUserDao?.deleteAll(db: db)
                        for c in cs {
                            try c.save(db)
                        }
                        
                        var ids: [String] = []
                        for r in rooms {
                            if let lr = r.deletedMessageItems {
                                for m in lr {
                                    ids.append(m.messageId)
                                    delRows.append(m.messageRowId)
                                }
                            }
                        }
                        
                        if !delRows.isEmpty {
                            chatDB.chatMessageDao?.deleteById(db: db, ids: ids)
                        }
                    }
                } catch {
                    debugPrint(error)
                }
                
                if delRows.isEmpty {
                    return Just(StateResult(result: true, message: "")).setFailureType(to: NetworkError.self).eraseToAnyPublisher()
                } else {
                    return api.updateDelete(body: MessageItemRows(messageRowIds: delRows, userInfo: Current.Shared.userInfo()), apiKey: Current.Shared.loggedUser?.token ?? "")
                }
            }
            .switchToLatest()
            .replaceError(with: StateResult(result: false, message: "ReloadRooms: Error!"))
            .setFailureType(to: NetworkError.self)
            .eraseToAnyPublisher()
    }
    
    func sortedRoomList() -> AnyPublisher<[ChatRoomExt], Error> {
        return chatDB.chatRoomDao!.selectRoomList()
            .map { lst in
                lst.sorted(by: { t1, t2 in
                    t1.room.lastUpdated > t2.room.lastUpdated
                })
            }.eraseToAnyPublisher()
    }
    
    func leaveRoom(roomId: String) -> AnyPublisher<String?, NetworkError> {
        let setting = ChatUserSettings(talkRoomId: roomId, userId: Current.Shared.userId(), notificationFlag: nil, leavingFlag: true, userInfo: Current.Shared.userInfo())
        return api.updateUserSetting(body: setting, apiKey: Current.Shared.loggedUser?.token ?? "")
            .map({ stt -> AnyPublisher<String?, NetworkError> in
                if stt.result {
                    self.chatDB.chatRoomDao?.deleteByRoomId(roomId: roomId)
                }
                
                return Just(stt.message).setFailureType(to: NetworkError.self).eraseToAnyPublisher()})
            .switchToLatest()
            .eraseToAnyPublisher()
    }
    
    func notificationOnOff(roomId: String, on: Bool) -> AnyPublisher<String?, NetworkError> {
        let setting = ChatUserSettings(talkRoomId: roomId, userId: Current.Shared.userId(), notificationFlag: on, leavingFlag: nil, userInfo: Current.Shared.userInfo())
        
        return api.updateUserSetting(body: setting, apiKey: Current.Shared.loggedUser?.token ?? "")
            .map({ $0.message }).eraseToAnyPublisher()
    }
    
    func getHistoryUsers() -> AnyPublisher<[TalkUser], NetworkError> {
        return api.getHistories(apiKey: Current.Shared.loggedUser?.token ?? "")
    }
    
    func getAllUsers() -> AnyPublisher<[TalkUser], NetworkError> {
        return api.getAllUsers(includeInvalid: false, apiKey: Current.Shared.loggedUser?.token ?? "")
    }
    
    func unreadCount() -> AnyPublisher<Int, Error> {
        chatDB.chatRoomDao!.selectRoomList()
            .map({ l in
                l.map( {$0.room.unread}).reduce(0, +)
            }).eraseToAnyPublisher()
    }
    
    func addRoom(users: [UserLite]) -> AnyPublisher<StateResult, NetworkError> {
        return api.addRoom(body: RoomCreate(user: users, userInfo: Current.Shared.userInfo()), apiKey: Current.Shared.loggedUser?.token ?? "")
    }
    
    func editRoom(roomId: String, users: [UserLite]) -> AnyPublisher<StateResult, NetworkError> {
        return api.addMember(body: RoomAddMember(roomId: roomId, users: users, userInfo: Current.Shared.userInfo()), apiKey: Current.Shared.loggedUser?.token ?? "")
    }
}
