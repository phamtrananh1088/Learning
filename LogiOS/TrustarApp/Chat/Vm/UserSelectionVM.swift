//
//  UserSelectionVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class UserSelectionVM<T: Member>: BaseViewModel, ObservableObject {
    
    class UserCheck: ObservableObject, Hashable {
        static func == (lhs: UserCheck, rhs: UserCheck) -> Bool {
            return lhs.uuid == rhs.uuid
        }
        
        func hash(into hasher: inout Hasher) {
            hasher.combine(uuid)
        }
        
        var uuid: String
        var checked: Bool
        var user: T
        var keepState: Bool
        init(user: T, checked: Bool, keepState: Bool) {
            self.user = user
            self.checked = checked
            self.keepState = keepState
            self.uuid = UUID().uuidString
        }
    }
    
    internal func keepState(t: T) -> Bool {
        return false
    }

    @Published private var currentUser: [UserCheck] = []
    

    @Published var fetch: [[T]] = []
    

    @Published private var selectedDirtyPool: Set<String> = []

    @Published private var excludedIds: Set<String> = []

    func excludeIds(ids: [String]?) {
        excludedIds = Set(ids?.map({$0}) ?? [])
    }
    
    @Published private var groups: Result<[[T]]> = Result.Value(value: nil)

    private var lastList: AnyPublisher<[[UserCheck]], NetworkError>  {
                
        return Publishers.CombineLatest4(
            $groups.setFailureType(to: NetworkError.self)
                .flatMap({ gr -> AnyPublisher<[[T]], NetworkError> in
                switch gr {
                case .Loading:
                    return Just([])
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                case .Value(value: let value):
                    return Just(value ?? [])
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                case .Error(error: let error):
                    return Fail(error: error).eraseToAnyPublisher()
                }
            }).eraseToAnyPublisher(),
            $selectedDirtyPool.setFailureType(to: NetworkError.self).eraseToAnyPublisher(),
            $excludedIds.setFailureType(to: NetworkError.self).eraseToAnyPublisher(),
            $currentUser.setFailureType(to: NetworkError.self).eraseToAnyPublisher())
        
        .map({(g, s, e, c) -> [[UserCheck]] in

            return g.map({ l -> [UserCheck] in
                return l.compactMap({ it -> UserCheck? in
                    if !e.contains(it.userId) {
                        let f = c.first(where: { cu in cu.user.userId == it.userId })
                        return UserCheck(user: it, checked: s.contains(it.userId), keepState: f?.keepState ?? self.keepState(t: it))
                    } else {
                        return nil
                    }
                })
            })
        }).eraseToAnyPublisher()
    }
    
    private var merge: AnyPublisher<[UserCheck], NetworkError> {
        return Publishers.CombineLatest(
            $currentUser.setFailureType(to: NetworkError.self).eraseToAnyPublisher(),
            lastList)
            .map({(c, l) in
                var tempLst: [UserCheck] = []
                for current in c {
                    if !tempLst.contains(where: {$0.user.userId == current.user.userId}) {
                        tempLst.append(current)
                    }
                }
                
                for last in l {
                    for lastChild in last {
                        if !tempLst.contains(where: {$0.user.userId == lastChild.user.userId}) {
                            tempLst.append(lastChild)
                        }
                    }
                }
                
                return tempLst
            }).eraseToAnyPublisher()
    }
    
    private var checkedResult: AnyPublisher<[UserCheck], NetworkError> {
        return Publishers.CombineLatest(
            merge,
            $selectedDirtyPool.setFailureType(to: NetworkError.self).eraseToAnyPublisher())
            .map({(l, s) in
                return l.filter({ $0.checked && s.contains($0.user.userId)})
            })
            .eraseToAnyPublisher()
    }

    @Published var loadStateLiveData: Result<[[T]]> = Result.Value(value: [])

    @Published var topList: [UserCheck] = []

    @Published var displayList: [[UserCheck]] = []

    @Published private var keep: [UserCheck] = []

    override init() {
        super.init()
        
        $fetch
            .setFailureType(to: NetworkError.self)
            .flatMap({ it -> AnyPublisher<Result<[[T]]>, NetworkError> in
                return Just(Result.Value(value: it)).setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            })
            .eraseToAnyPublisher()
            .sink(receiveCompletion: { completion in },
                  receiveValue: { rs in
                self.groups = rs
            }).store(in: &bag)
        
        $groups
            .setFailureType(to: NetworkError.self)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                    receiveValue: { result in
                self.loadStateLiveData = result
        }).store(in: &bag)
        
        checkedResult
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { users in
                self.topList = users
            }).store(in: &bag)
        
        lastList
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: {completion in },
                  receiveValue: { g in
                self.displayList = g
            }).store(in: &bag)
        
        merge
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: { u in
                self.keep = u
            }).store(in: &bag)
    }

    private var selectedIds: Set<String> = Set()

    func setCurrentSelected(selection: [T]) {
        let ll: [UserCheck] = selection.map({UserCheck(user: $0, checked: true, keepState: keepState(t: $0))})
        
        for it in ll {
            selectedIds.insert(it.user.userId)
        }
        
        currentUser = ll
        selectedDirtyPool = selectedIds
    }

    func selectId(id: String, select: Bool) {
        
        let f = keep.filter({ $0.user.userId == id}).first
        
            if f != nil && !f!.keepState {
            if select {
                selectedIds.insert(id)
            } else {
                selectedIds.remove(id)
            }
        }
        
        selectedDirtyPool = selectedIds
    }
}
