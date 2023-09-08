//
//  ChatUser.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum ChatUserColumns: String, ColumnExpression, CaseIterable {
    case id = "id"
    case name = "name"
    case mail = "mail"
    case avatar = "avatar"
    
    func name() -> String {
        return String(describing: self)
    }
}

class ChatUser: BaseEntity, Hashable, Codable {
    static func == (lhs: ChatUser, rhs: ChatUser) -> Bool {
        return lhs.id == rhs.id
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    var id: String = Resources.strEmpty
    var name: String? = nil
    var mail: String? = nil
    var avatar: String? = nil
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"chat_user"}
    
    override class var primaryKey: String {"id"}
    
    required init(row: Row) {
        id = row[ChatUserColumns.id]
        name = row[ChatUserColumns.name]
        mail = row[ChatUserColumns.mail]
        avatar = row[ChatUserColumns.avatar]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[ChatUserColumns.id] = id
        container[ChatUserColumns.name] = name
        container[ChatUserColumns.mail] = mail
        container[ChatUserColumns.avatar] = avatar
        super.encode(to: &container)
    }
    
    init(id: String,
         name: String?,
         mail: String?,
         avatar: String?
    ) {
        self.id = id
        self.name = name
        self.mail = mail
        self.avatar = avatar
        super.init()
    }
    
    func avatarUri() -> ImageURI? {
        if avatar == nil || URL(string: avatar!) == nil { return nil }
        return ImageURI(uri: URL(string: avatar!)!, handle: true)
    }
    
    func sameItem(other: ChatUser) -> Bool {
        return id == other.id
    }
}
