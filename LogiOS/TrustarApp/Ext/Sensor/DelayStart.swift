//
//  DelayStart.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/02/22.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class DelayStart {
    private var isStarted: Bool = false

    func tryStart(delaySeconds: Int = 0) {
        if started() { return }
        
        _ = Future<Bool, Never> { promise in
            promise(.success(true))
        }
        .delay(for: .seconds(delaySeconds > 0 ? TimeInterval(delaySeconds) : 0), scheduler: DispatchQueue.main)
        .subscribe(on: DispatchQueue.global())
        .sink(receiveValue: { _ in
            self.start()
        })
    }

    private func start() {
        if isStarted == false {
            isStarted = true
            onStart()
        }
    }

    func cancelAndReset() {
        onStop(isStarted: isStarted)
        
        if isStarted {
            isStarted = false
        }
    }

    func onStart() { }
    func onStop(isStarted: Bool) { }

    func started() -> Bool {
        return isStarted
    }
}
