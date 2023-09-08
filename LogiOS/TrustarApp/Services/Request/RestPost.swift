//
//  RestPost.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/05/31.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation

class RestPost: Codable {
    var rests: [CommonRest]
    var clientInfo: ClientInfo
    
    init(rests: [CommonRest], clientInfo: ClientInfo) {
        self.rests = rests
        self.clientInfo = clientInfo
    }
}
