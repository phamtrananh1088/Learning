//
//  RoomUserSelectionVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class RoomUserSelectionVM: UserSelectionVM<TalkUser> {
    
    private var chatRoomRepository = Current.Shared.chatRoomRepository
    var onTapRow: (() -> Void)? = nil
    
    func loadUsers() {
        self.loadStateLiveData = .Loading
        Publishers.CombineLatest(
            chatRoomRepository!.getHistoryUsers(),
            chatRoomRepository!.getAllUsers())
        .flatMap({(h, a) -> AnyPublisher<[[TalkUser]], NetworkError> in
            return Just([h, a])
                .setFailureType(to: NetworkError.self)
                .eraseToAnyPublisher()
        })
        .eraseToAnyPublisher()
        .receive(on: DispatchQueue.main)
        .sink(receiveCompletion: {completion in
            switch completion {
            case .finished:
                break
            case .failure(let error):
                withAnimation {
                    self.loadStateLiveData = .Error(error: error)
                }
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 2, execute: {
                    withAnimation {
                        self.loadStateLiveData = Result.Value(value: [])
                    }
                })
            }
        },
              receiveValue: { u in
            self.fetch = u
        }).store(in: &bag)
    }

    @Published var selectedLiveData: [UserCheck] = []
    
    @Published var firstListLiveData: [UserCheckItemUIViewModel<TalkUser>] = []
    @Published var secondListLiveData: [UserCheckItemUIViewModel<TalkUser>] = []
    
    override init() {
        super.init()
        
        $topList
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: {completion in },
                  receiveValue: { u in
                self.selectedLiveData = u
            }).store(in: &bag)

        $displayList
            .receive(on: DispatchQueue.main)
            .sink(receiveValue: { [self] group in
                if let firsts = group.getOrNull(index: 0) {
                    let firstLst = getVm(lst: firsts)
                    if firstListLiveData.count != firstLst.count || firstListLiveData.isEmpty {
                        firstListLiveData = firstLst
                    } else {
                        for i in 0...firstListLiveData.count - 1 {
                            if !firstListLiveData[i].areContentsTheSame(newItem: firstLst[i]) {
                                firstListLiveData[i] = firstLst[i]
                            }
                        }
                    }
                }
                
                if let seconds = group.getOrNull(index: 1) {
                    let secondLst = getVm(lst: seconds)
                    if secondListLiveData.count != secondLst.count || secondListLiveData.isEmpty {
                        secondListLiveData = secondLst
                    } else {
                        for i in 0...secondListLiveData.count - 1 {
                            if !secondListLiveData[i].areContentsTheSame(newItem: secondLst[i]) {
                                secondListLiveData[i].checkBox = secondLst[i].checkBox
                            }
                        }
                    }
                }
            }).store(in: &bag)
    }
    
    private func getVm(lst: [UserSelectionVM<TalkUser>.UserCheck]) -> [UserCheckItemUIViewModel<TalkUser>] {
        var lstRs: [UserCheckItemUIViewModel<TalkUser>] = []
        for item in lst {
            lstRs.append(
                UserCheckItemUIViewModel(
                    bound: item,
                    onClick: { (id, isChecked) in
                        self.selectId(id: id, select: isChecked)
                        self.onTapRow?()
                    }
                )
            )
        }
        
        return lstRs
    }
}
