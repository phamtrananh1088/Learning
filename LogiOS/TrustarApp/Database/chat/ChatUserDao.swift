//
//  ChatUserDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

class ChatUserDao: BaseDbDao<ChatDb> {
    func deleteAll(db: Database) {
        do {
            try ChatUser.deleteAll(db)
        } catch {
            debugPrint(error)
        }
    }
}
