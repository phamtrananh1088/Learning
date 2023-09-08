//
//  ChatDb.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

class ChatDb: BaseDb {
    
    var chatRoomDao: ChatRoomDao? = nil
    var chatUserDao: ChatUserDao? = nil
    var chatRoomUserDao: ChatRoomUserDao? = nil
    var chatMessageDao: ChatMessageDao? = nil
    var chatMessagePendingDao: ChatMessagePendingDao? = nil
        
    init(_ userName: String) throws {
        super.init()
        grdb = GRDBTrustar("chatDb_" + userName)

        let isDbExisted = grdb!.isDbExisted()
        
        instanceDb = try grdb!.makeDatabaseQueue()
        
        if !isDbExisted {
            initTable(table: ChatRoom(), columns: ChatRoomColumns.allCases.map({($0.rawValue, $0.name())}))
            
            initTable(table: ChatUser(), columns: ChatUserColumns.allCases.map({($0.rawValue, $0.name())}))
            
            initTable(table: ChatRoomUser(), columns: ChatRoomUserColumns.allCases.map({($0.rawValue, $0.name())}))
            
            initTable(table: ChatMessage(), columns: ChatMessageColumns.allCases.map({($0.rawValue, $0.name())}))
            
            initTable(table: ChatMessagePending(), columns: ChatMessagePendingColumns.allCases.map({($0.rawValue, $0.name())}))
        }
        
        initDao()
    }

    private func initDao() {
        chatRoomDao = ChatRoomDao(self)
        chatUserDao = ChatUserDao(self)
        chatRoomUserDao = ChatRoomUserDao(self)
        chatMessageDao = ChatMessageDao(self)
        chatMessagePendingDao = ChatMessagePendingDao(self)
    }
}
