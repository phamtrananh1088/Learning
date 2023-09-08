//
//  BaseDbExecute.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BaseDbDao<T> {
    internal var executeDb: T
    
    init(_ executeDb: T) {
        self.executeDb = executeDb
    }
}
