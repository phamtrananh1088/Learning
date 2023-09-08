//
//  ChatOptionVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class ChatOptionVM: ChatRoomInfoVM, ObservableObject {
    
    @Published private var leaveState: Result<Any> = .Value(value: nil)

    @Published private var notificationState: Result<Any> = .Value(value: nil)

    @Published var leaveLiveData: Result<Any> = .Value(value: nil)

    @Published var notificationLiveData: Result<Any> = .Value(value: nil)
    
    @Published var lstUserItem: [UserItemUIViewModel] = []
    
    @Published var isNotificationOn: Bool = false
    
    @Published var notiLoading: LoadingStateEnum = .Ok
    var notiCallback: (() -> Void)? = nil
    
    @Published var leaveLoading: LoadingStateEnum = .Ok
    var leaveCallback: (() -> Void)? = nil
    
    override init() {
        super.init()
        
        $leaveState
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { e in
                self.leaveLiveData = e
            }).store(in: &bag)
        
        $notificationState
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { e in
                self.notificationLiveData = e
            }).store(in: &bag)
        
        $roomData
            .compactMap({ $0 })
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { c in
                self.lstUserItem = c.users.map({UserItemUIViewModel(bound: $0)})
            }).store(in: &bag)
        
        $notificationLiveData
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { rs in
                switch(rs) {
                case .Loading:
                    self.notiLoading = .Loading
                case .Value(value: let value):
                    if value != nil {
                        self.notiCallback?()
                        self.notiLoading = .Ok
                    }
                case .Error(_):
                    self.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.room_update_settings_err_alert_msg, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok))
                }
            }).store(in: &bag)
        
        $leaveLiveData
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { rs in
                switch(rs) {
                case .Loading:
                    self.leaveLoading = .Loading
                case .Value(value: let value):
                    if value != nil {
                        self.leaveCallback?()
                        self.leaveLoading = .Ok
                    }
                case .Error(_):
                    self.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.room_leave_err_alert_msg, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok))
                }
            }).store(in: &bag)
        
        $roomId
            .setFailureType(to: Error.self)
            .map({ id in
                self.chatRoomRepository!.chatRoomWithUsers(roomId: id)
                    .compactMap({ $0 })
            })
            .switchToLatest()
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { c in
                self.isNotificationOn = c.room.notification
                self.roomData = c
            }).store(in: &self.bag)
    }

    private let chatRoomRepository = Current.Shared.chatRoomRepository

    func leaveRoom(roomId: String) {
        leaveState = .Loading
        chatRoomRepository!
            .leaveRoom(roomId: roomId)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in
                switch completion {
                case .finished:
                    break
                case .failure(let error):
                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1, execute: {
                        self.leaveLoading = .Ok
                        self.leaveState = .Error(error: error)
                    })
                }
            },
                  receiveValue: { s in
                self.leaveState = .Value(value: s.orEmpty() as Any)
            }).store(in: &bag)
    }

    func notificationOnOff(roomId: String, on: Bool) {
        chatRoomRepository!
            .notificationOnOff(roomId: roomId, on: on)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in
                switch completion {
                case .finished:
                    break
                case .failure(let error):
                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1, execute: {
                        self.notiLoading = .Ok
                        self.notificationState = .Error(error: error)
                    })
                }
            },
                  receiveValue: { s in
                self.notificationState = .Value(value: s.orEmpty() as Any)
            }).store(in: &bag)
    }
}
