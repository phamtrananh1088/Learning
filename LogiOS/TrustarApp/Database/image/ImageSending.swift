//
//  ImageSending.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum ImageSendingColumns: String, ColumnExpression {
    case picId, companyCd, userId, picRaw, content
}

class ImageSending: BaseEntity, Codable {
    var picId: String       = Resources.strEmpty
    var companyCd: String   = Resources.strEmpty
    var userId: String      = Resources.strEmpty
    var picRaw: Data        = Data.init()
    //JSON data
    var content: String     = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    init(picId: String, companyCd: String, userId: String, picRaw: Data, content: String) {
        self.picId = picId
        self.companyCd = companyCd
        self.userId = userId
        self.picRaw = picRaw
        self.content = content
        
        super.init()
    }
    
    convenience init(userInfo: UserInfo, picRaw: Data, content: PicPost.Content) {
        let randomId = Helper.Shared.hashMd5Hex16Digit(string: UUID().uuidString)
        var base64Id = ""
        if let data = randomId.data(using: .utf8) {
            base64Id =  data.base64EncodedString()
        }
        self.init(picId: base64Id,
                  companyCd: userInfo.companyCd,
                  userId: userInfo.userId,
                  picRaw: picRaw,
                  content: content.toJsonString())
    }
    
    override class var databaseTableName: String {"ImageSending"}
    override class var primaryKey: String { "picId" }
    
    required init(row: Row) {
        picId = row[ImageSendingColumns.picId]
        companyCd = row[ImageSendingColumns.companyCd]
        userId = row[ImageSendingColumns.userId]
        picRaw = row[ImageSendingColumns.picRaw]
        content = row[ImageSendingColumns.content]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[ImageSendingColumns.picId] = picId
        container[ImageSendingColumns.companyCd] = companyCd
        container[ImageSendingColumns.userId] = userId
        container[ImageSendingColumns.picRaw] = picRaw
        container[ImageSendingColumns.content] = content
        
        super.encode(to: &container)
    }
}

