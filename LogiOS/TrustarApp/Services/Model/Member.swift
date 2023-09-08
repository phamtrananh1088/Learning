//
//  Member.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ImageURI: Codable {
    var uri: URL
    var handle: Bool
    
    init(uri: URL, handle: Bool) {
        self.uri = uri
        self.handle = handle
    }
}

class UserMemberLite {
    var userId: String
    var companyCd: String
    
    init(userId: String,
         companyCd: String) {
        self.userId = userId
        self.companyCd = companyCd
    }
}

class Member: UserMemberLite {
    var userName: String?
    var avatarUri: ImageURI?
    
    init(userName: String?, avatarUri: ImageURI?, userId: String, companyCd: String) {
        self.userName = userName
        self.avatarUri = avatarUri
        
        super.init(userId: userId, companyCd: companyCd)
    }
}
