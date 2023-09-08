//
//  BaseEntity.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum BaseEntityColumns: String, ColumnExpression {
    case sync
}

class BaseEntity: Record {
    
    /**
         *  # -1: ignored. untouched, nothing to do.
         *  # 0: ready. this record has changed.
         *  # 1: pending. this record is going to sync.
         *  # 2: synced. synced successful.
         */
        var sync: Int = -1
    
        func recordChanged() {
            sync = 0
        }

        func setSyncFinished() {
            sync = 2
        }
    
    class var primaryKey: String {""}
    class var parentTable: String{""}
    class var parentColumns: String{""}
    class var childColumns: String{""}
    
    override init() {
        super.init()
    }
    
    required init(row: Row) {
        sync = row[BaseEntityColumns.sync]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[BaseEntityColumns.sync] = sync
    }
}
