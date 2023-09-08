//
//  ChatRoomExt.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ChatRoomExt {
    var room: ChatRoom
    init(room: ChatRoom) {
        self.room = room
    }
    
    func imageUri() -> ImageURI? {
        if let img = room.image {
            if let url = URL(string: img) {
                return ImageURI(uri: url, handle: true)
            }
        }
        
        return nil
    }

    func title() -> String {
        return "\(room.name ?? "")(\(room.userCount))"
    }

    func lastUpdateHHmm() -> String {
        return Config.Shared.dateFormatterHHmm.format(date: Date(timeIntervalSince1970: TimeInterval(room.lastUpdated / 1000)))
    }

    func lastUpdateMMdd() -> String {
        return Config.Shared.dateFormatterMMdd.format(date: Date(timeIntervalSince1970: TimeInterval(room.lastUpdated / 1000)))
    }

    func sameDayToLastUpdated(time: Int64) -> Bool {
        return daysBetween(from: room.lastUpdated, to: time) == 0
    }
}
