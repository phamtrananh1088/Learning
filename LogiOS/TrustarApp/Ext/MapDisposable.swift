//
//  MapDisposable.swift
//  TrustarApp
//
//  Created by DionSoftware on 26/03/2023.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class MapDisposable<K: Hashable>: Cancellable {
    
    private var resources : [K: Cancellable] = [:]
    
    var disposed: Bool = false
    
    func cancel() {
        if disposed { return }
        
        var s: [Cancellable] = []
        
        DispatchQueue.global().asyncAndWait(execute: .init(block: { [self] in
            if disposed { return }
            disposed = true
            s = resources.values.map({ $0 })
            resources = [:]
        }))
        
        dispose(s: s)
    }
    
    func clear() {
        if disposed { return }
        var s: [Cancellable] = []
        
        DispatchQueue.global().asyncAndWait(execute: .init(block: { [self] in
            if disposed { return }
            s = resources.values.map({ $0 })
            resources = [:]
        }))
    
        dispose(s: s)
    }
    
    func put(key: K, d: Cancellable) -> Bool {
        if !disposed {
            DispatchQueue.global().asyncAndWait(execute: .init(block: { [self] in
                if (!disposed) {
                    if let old = resources[key] {
                        old.cancel()
                    }
                    resources[key] = d
                }
            }))
            
            return true
        }
        
        d.cancel()
        return false
    }

    func take(key: K) -> Cancellable? {
        return resources.removeValue(forKey: key)
    }
    
    func isDisposed() -> Bool {
        return disposed
    }
    
    private func dispose(s: [Cancellable]) {
        let set = s.makeIterator()
        set.compactMap({ $0}).forEach({ d in d.cancel()})
    }
}
