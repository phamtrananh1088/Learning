//
//  NoticeRequest.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/28.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class NoticePost: Codable {
    var notices: [CommonNotice]
    var clientInfo: ClientInfo
    
    init(notices: [CommonNotice], clientInfo: ClientInfo) {
        self.notices = notices
        self.clientInfo = clientInfo
    }
}
