//
//  NoticeRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class NoticeRepo {
    private var userDb: UserDb
    init(_ userDb: UserDb) {
        self.userDb = userDb
    }
    
    func countUnreadImportant() -> Int {
        return userDb.noticeDao!.countUnreadByRank(rank: 1)
    }

    func unreadImportant() -> [Notice] {
        return unreadBy(rank: 1)
    }

    func unreadNormal() -> [Notice] {
        return unreadBy(rank: 2)
    }

    /**
     * @param rank 1：重要、2：通常
     */
    func unreadBy(rank: Int) -> [Notice] {
        return userDb.noticeDao?.unreadByRank(rank: rank) ?? []
    }

    func allByRank(rank: Int) -> [Notice] {
        return userDb.noticeDao?.selectByRank(rank: rank) ?? []
    }

    func markRead(rank: Int) {
        userDb.noticeDao?.markAllReadByRank(rank: rank)
    }
}
