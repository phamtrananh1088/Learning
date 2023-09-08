//
//  AddRoomVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class AddRoomVM: BaseViewModel, ObservableObject {
    @Published private var saveState: Event<Result<Void>> = Event.Value(value: nil)

    @Published var stateLiveData: Event<Result<Void>> = Event.Value(value: nil)
    
    override init() {
        super.init()
        $saveState
            .receive(on: DispatchQueue.main)
            .sink(receiveValue: { s in
                self.stateLiveData = s
            }).store(in: &bag)
        
        $stateLiveData
            .receive(on: DispatchQueue.main)
            .sink(receiveValue: { state in
                switch state {
                    
                case .Value(value: _):
                    break
                case .Error(error: _):
                    self.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: "", message: Resources.room_create_err_alert_msg, isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: true, rightBtnText: Resources.ok))
                }
            }).store(in: &bag)
    }

    private var chatRoomRepository = Current.Shared.chatRoomRepository

    func addRoom(users: [UserLite]) {
        saveState = .Value(value: .Loading)
        chatRoomRepository!.addRoom(users: users)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in
                switch completion {
                case .finished:
                    break
                case .failure(let error):
                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 2, execute: {
                        self.saveState = .Error(error: error)
                    })
                }
            },
                  receiveValue: { state in
                self.saveState = Event.Value(value: .Value(value: nil))
            }).store(in: &bag)
    }
}
