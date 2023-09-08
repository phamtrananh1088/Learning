//
//  ChatRoomInfoVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class ChatRoomInfoVM: BaseViewModel {
    @Published var roomId: String = String()
    @Published private var roomSource: Optional<ChatRoomWithUsers> = nil
    
    internal var chatRoomWithUsers: AnyPublisher<Optional<ChatRoomWithUsers>, Error> {
        return $roomSource
            .setFailureType(to: Error.self)
            .map({ it in
                Just(it).setFailureType(to: Error.self)
            })
            .switchToLatest()
            .eraseToAnyPublisher()
    }

    @Published var roomData: Optional<ChatRoomWithUsers> = nil

    private let chatRoomRepository = Current.Shared.chatRoomRepository

    func loadRoom(roomId: String) {
        self.roomId = roomId
    }
    
    override init() {
        super.init()
        
        $roomId
            .setFailureType(to: Error.self)
            .map({ id -> AnyPublisher<ChatRoomWithUsers?, Error> in
                if id.isEmpty { return Just(nil)
                    .setFailureType(to: Error.self)
                    .eraseToAnyPublisher()
                }
                
                return Future<ChatRoomWithUsers?, Error> { promise in
                    self.chatRoomRepository!
                        .chatRoomWithUsers(roomId: id)
                        .receive(on: DispatchQueue.main)
                        .sink(receiveCompletion: { completion in },
                              receiveValue: { c in
                            promise(.success(c))
                        }).store(in: &self.bag)
                }.eraseToAnyPublisher()
            })
            .switchToLatest()
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { chatRoom in
                if chatRoom == nil { return }
                self.roomSource = chatRoom
            }).store(in: &bag)
        
        chatRoomWithUsers
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { c in
                if c == nil { return }
                self.roomData = c
            }).store(in: &bag)
    }
}
