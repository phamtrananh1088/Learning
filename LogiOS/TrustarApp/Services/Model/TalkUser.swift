//
//  TalkUser.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class TalkUser: Member, Codable {
    var mailAddress: String?
    var avatarImageUrl: String?
    
    init(userId: String,
         companyCd: String,
         userName: String,
         mailAddress: String?,
         avatarImageUrl: String?
    ) {
        self.mailAddress = mailAddress
        self.avatarImageUrl = avatarImageUrl
        super.init(userName: userName,
                   avatarUri: avatarImageUrl == nil ? nil : ImageURI(uri: URL(string: avatarImageUrl!)!, handle: true),
                   userId: userId,
                   companyCd: companyCd)
    }
    
    required init(from decoder: Decoder) throws {
        let tu = try TalkUserDecoding(from: decoder)
        super.init(userName: tu.userName, avatarUri: tu.avatarImageUrl == nil ? nil : ImageURI(uri: URL(string: tu.avatarImageUrl!)!, handle: true), userId: tu.userId, companyCd: tu.companyCd)
    }
}

struct TalkUserDecoding {
    var userId: String
    var userName: String
    var companyCd: String
    var avatarImageUrl: String?
    
    enum CodingKeys: String, CodingKey {
        case userId
        case userName
        case companyCd
        case avatarImageUrl
    }
    
    init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        userId = try values.decode(String.self, forKey: .userId)
        userName = try values.decode(String.self, forKey: .userName)
        companyCd = try values.decode(String.self, forKey: .companyCd)
        avatarImageUrl = try values.decode(String?.self, forKey: .avatarImageUrl)
    }
}
