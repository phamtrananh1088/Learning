//
//  UserLite.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class UserLite: Codable {
    var userId: String
    var companyCd: String
    
    init(userId: String,
         companyCd: String) {
        self.userId = userId
        self.companyCd = companyCd
    }
}
