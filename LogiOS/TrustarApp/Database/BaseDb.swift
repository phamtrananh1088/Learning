//
//  BaseDb.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

class BaseDb {
    var instanceDb: DatabaseQueue?
    internal var migrator = DatabaseMigrator()
    internal var grdb: GRDBTrustar? = nil

    private let lstPropOfBaseEntity = Helper.Shared.allPropertiesOf(object: BaseEntity())
    
    internal func initTable<T: BaseEntity>(table: T) {
        grdb!.createTableIfNotExist(table: table,
                                    lstProp: Helper.Shared.appendDictionary(dic1: Helper.Shared.allPropertiesOf(object: table), dic2: lstPropOfBaseEntity))
    }
    
    internal func initTable<T: BaseEntity>(table: T, columns: [(String,String)]) {
        grdb!.createTableIfNotExist(table: table,
                                    lstProp: Helper.Shared.appendDictionary(dic1: Helper.Shared.allPropertiesOf(object: table, columns: columns), dic2: lstPropOfBaseEntity))
    }
}
