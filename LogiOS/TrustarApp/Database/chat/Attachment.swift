//
//  Attachment.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class Attachment: Codable {
    var key: String
    var name: String
    var size: Int64
    
    init(key: String, name: String, size: Int64) {
        self.key = key
        self.name = name
        self.size = size
    }
}
