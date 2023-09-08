//
//  StateResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/28.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class StateResult: Codable {
    var result: Bool
    var message: String?
    
    init(result: Bool, message: String?) {
        self.result = result
        self.message = message
    }
}
