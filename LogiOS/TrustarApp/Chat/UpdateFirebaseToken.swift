//
//  UpdateFirebaseToken.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/28.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import FirebaseMessaging

class UpdateFirebaseToken {
    var invalid: Bool? = nil
    private func getToken(invalid: Bool) -> AnyPublisher<Result<String>, NetworkError> {
        return Future<Result<String>, NetworkError> { promise in
            let firebase = FirebaseMessaging.Messaging.messaging()
            self.invalid = invalid
            if invalid {
                firebase.deleteToken { _ in }
            }
            
            firebase.token { (token, error) in
                if error != nil {
                    promise(.success(Result.Error(error: NetworkError.error(error: error!))))
                } else {
                    promise(.success(.Value(value: token)))
                }
            }
        }.map {$0}.eraseToAnyPublisher()
    }

    private func invalidToken() -> AnyPublisher<Result<String>, NetworkError> {
        return getToken(invalid: true)
    }

    private func sendToken(delayedSendSec: Int) -> AnyPublisher<Result<StateResult>, NetworkError> {
        return getToken(invalid: false)
            .map({ token in
                Just(token)
                    .delay(for: .seconds(delayedSendSec), scheduler: DispatchQueue.main)
                    .setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            })
            .switchToLatest()
            .flatMap { token -> AnyPublisher<Result<StateResult>, NetworkError> in
                switch token {
                case .Value(value: let value):
                    return Current.Shared.chatApi.registerFirebaseToken(body: DefaultPostContent.Token(token: value!, userInfo: Current.Shared.userInfo()), apiKey: Current.Shared.loggedUser?.token ?? "")
                        .map({ Result.Value(value: $0)})
                        .replaceError(with: Result.Error(error: .message(reason: "RegisterFirebaseToken error")))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                case .Error(error: let error):
                    return Just(Result.Error(error: error))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                default:
                    return Just(Result.Value(value: nil))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                }
            }.eraseToAnyPublisher()
    }
    
    @Published private var request: BooleanEnum = .Nil
    
    private var tokenGenerated: BooleanEnum = .False

    @Published var requestState: Result<Void> = .Value(value: nil)
     
    private var bag = Set<AnyCancellable>()
    init() {

        $request
            .setFailureType(to: NetworkError.self)
            .map { [self] it -> AnyPublisher<Result<StateResult>, NetworkError> in
                requestState = .Loading
                
                if it != .Nil {
                    if it == .False || tokenGenerated == .False {
                        return invalidToken()
                            .flatMap { [self] _ -> AnyPublisher<Result<StateResult>, NetworkError> in
                                tokenGenerated = .True
                                return sendToken(delayedSendSec: 2)
                            }.eraseToAnyPublisher()
                    } else {
                        return sendToken(delayedSendSec: 0)
                    }
                } else {
                    return Just(Result.Value(value: nil))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                }
            }
            .switchToLatest()
            .sink(receiveCompletion: { completion in
                print(completion)
            },
                  receiveValue: { w in
                switch w {
                    
                case .Loading:
                    break
                case .Value(value: let value):
                    print("[Value] UpdateFirebaseToken: \(value?.message ?? "")")
                case .Error(error: let error):
                    print("[Error] UpdateFirebaseToken: \(error)")
                }
            }).store(in: &bag)
    }

    func updateToken() {
        request = .False
    }

    func newToken() {
        tokenGenerated = .False
        request = .True
    }
}
