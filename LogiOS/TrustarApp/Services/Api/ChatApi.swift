//
//  ChatApi.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

struct ChatApi {
    func getData(apiKey: String) -> AnyPublisher<[TalkRoom], NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.getData, [.ApiKeyAndTokenHeader, .CompanyCdQuery], msg, apiKey, .GET)
    }

    func getTalkRooms(apiKey: String) -> AnyPublisher<[TalkRoom], NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.getTalkRooms, [.ApiKeyAndTokenHeader, .CompanyCdQuery], msg, apiKey, .GET)
    }
    
    func getHistories(apiKey: String) -> AnyPublisher<[TalkUser], NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.getHistories, [.ApiKeyAndTokenHeader, .CompanyCdQuery], msg, apiKey, .GET)
    }
    
    func getAllUsers(includeInvalid: Bool, apiKey: String) -> AnyPublisher<[TalkUser], NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.getAllUsers, [.ApiKeyAndTokenHeader, .CompanyCdQuery], msg, apiKey, .GET, [("includeInvalid", includeInvalid)])
    }
    
    func addRoom(body: RoomCreate, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callChatAPI(.addRoom, [.ApiKeyAndTokenHeader], body.result(), apiKey, .POST)
    }
    
    func addMember(body: RoomAddMember, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callChatAPI(.addMember, [.ApiKeyAndTokenHeader], body.result(), apiKey, .POST)
    }
    
    func sendMessage(body: ChatMessagePending, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.sendMessage, [.ApiKeyAndTokenHeader], msg, apiKey, .POST, [], body.jsonBody())
    }
    
    func getMessagePrevious(roomId: String, messageId: String, pageSize: Int, apiKey: String) -> AnyPublisher<[MessageItem], NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.getMessagePrevious, [.ApiKeyAndTokenHeader, .CompanyCdQuery], msg, apiKey, .GET, [("talkRoomId", roomId), ("messageId", messageId), ("pageSize", pageSize)])
    }
    
    func getMessageNext(roomId: String, messageId: String, pageSize: Int, apiKey: String) -> AnyPublisher<[MessageItem], NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.getMessageNext, [.ApiKeyAndTokenHeader, .CompanyCdQuery], msg, apiKey, .GET, [("talkRoomId", roomId), ("messageId", messageId), ("pageSize", pageSize)])
    }
    
    func getMessageItems(roomId: String, pageSize: Int, apiKey: String) -> AnyPublisher<[MessageItem], NetworkError> {
        let msg: ChatMessage? = nil
        return API.shared.callChatAPI(.getMessageItems, [.ApiKeyAndTokenHeader, .CompanyCdQuery], msg, apiKey, .GET, [("talkRoomId", roomId), ("pageSize", pageSize)])
    }
    
    func updateUserSetting(body: ChatUserSettings, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callChatAPI(.updateUserSetting, [.ApiKeyAndTokenHeader], body, apiKey, .POST)
    }
    
    func updateRead(body: MessageItemRows, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callChatAPI(.updateRead, [.ApiKeyAndTokenHeader], body.result(), apiKey, .POST)
    }
    
    func updateDelete(body: MessageItemRows, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callChatAPI(.updateDelete, [.ApiKeyAndTokenHeader], body.result(), apiKey, .POST)
    }
    
    func registerFirebaseToken(body: DefaultPostContent.Token, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callChatAPI(.registerFirebaseToken, [.ApiKeyAndTokenHeader, .CompanyCdQuery], body, apiKey, .POST)
    }
    
    func deleteMessage(body: DefaultPostContent.Message, apiKey: String) -> AnyPublisher<StateResult, NetworkError> {
        return API.shared.callChatAPI(.deleteMessage, [.ApiKeyAndTokenHeader], body, apiKey, .POST)
    }
}
