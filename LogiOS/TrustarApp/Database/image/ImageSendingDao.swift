//
//  ImageSendingDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum ImageSendingExecuteSql: SqlProtocol {
    case selectAny, deleteByPicId

    func makeQuery() -> String {
        switch self {
        case .selectAny:
            return "select * from ImageSending limit 1"
        case .deleteByPicId:
            return "delete from ImageSending where picId = :picId"
        }
    }
}

class ImageSendingDao: BaseDbDao<ImageDb> {
    
    func selectAny() -> ImageSending? {
        return try? executeDb.instanceDb?.read({ db in
            return try? ImageSending.fetchOne(db,
                                             sql: ImageSendingExecuteSql.selectAny.makeQuery())
        })
    }
    
    func deleteByPicId(picId: String) {
        try? executeDb.instanceDb?.write { db in
            try? db.execute(sql: ImageSendingExecuteSql.deleteByPicId.makeQuery(),
                            arguments: ["picId": picId])
        }
    }
    
    func insert(imgSending: ImageSending) {
        try? executeDb.instanceDb?.write { db in
            try? imgSending.insert(db)
        }
    }
}
