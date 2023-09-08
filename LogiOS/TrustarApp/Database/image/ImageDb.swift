//
//  ImageDb.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/20.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ImageDb: BaseDb {

    var imageSendingDao: ImageSendingDao? = nil
    
    init(_ userName: String) throws {
        super.init()
        
        grdb = GRDBTrustar("imageSendingDb_" + userName)

        let isDbExisted = grdb!.isDbExisted()
        
        instanceDb = try grdb!.makeDatabaseQueue()
        
        if !isDbExisted {
            initTable(table: ImageSending())
        }
        
        initDao()
    }

    private func initDao() {
        imageSendingDao = ImageSendingDao(self)
    }
}
