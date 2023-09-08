//
//  RoomLoadVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class RoomLoadVM: BaseViewModel, ObservableObject {
    @Published private var loadRoom: Bool
    @Published private var lastTimeMillis: Int64

    @Published var loadStateLiveData: Result<Void> = Result.Value(value: nil)

    private var chatRoomRepository = Current.Shared.chatRoomRepository

    var onShowLoadFailed: (() -> Void)?
    
    override init() {
        self.loadRoom = false
        self.lastTimeMillis = currentTimeMillis()
        super.init()
        
        $loadRoom
            .setFailureType(to: NetworkError.self)
            .map({ [self] it -> AnyPublisher<StateResult, NetworkError> in
                withAnimation {
                    loadStateLiveData = .Loading
                }
                
                if chatRoomRepository == nil {
                    return Just(StateResult(result: true, message: nil))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                } else {
                    return chatRoomRepository!.reloadRooms()
                }
            })
            .switchToLatest()
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in
                
            }, receiveValue: { st in

                withAnimation {
                    if !st.result {
                        self.loadStateLiveData = .Error(error: .message(reason: st.message ?? ""))
                    } else {
                        self.loadStateLiveData = .Value(value: nil)
                    }
                }
            }).store(in: &bag)
        
        Publishers
            .CombineLatest(chatRoomRepository!.sortedRoomList(), $lastTimeMillis.setFailureType(to: Error.self))
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { (rooms, time) in
                self.listLiveData = rooms.map({ RoomItemUIViewModel(bound: $0)})
            }).store(in: &bag)
        
        $loadStateLiveData
            .receive(on: DispatchQueue.main)
            .sink(receiveValue: { state in
                switch state {
                case .Error(error: _):
                    if let f = self.onShowLoadFailed {
                        f()
                    }
                    break
                default: break
                }
            }).store(in: &bag)
    }
    
    func load() {
        loadRoom = false
    }

    @Published var listLiveData: [RoomItemUIViewModel] = []

    func updateDate(timeMillis: Int64) {
        lastTimeMillis = timeMillis
    }
}
