//
//  CollectionGroup.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CollectionGroupColumns: String, ColumnExpression {
    case collectionClass, collectionClassName, displayOrder
}

class CollectionGroup: BaseEntity, Codable {
    var collectionClass: Int = Resources.zeroNumber
    var collectionClassName: String?
    var displayOrder: Int = Resources.zeroNumber
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"CollectionGroup"}
    override class var primaryKey: String {"collectionClass"}
    
    required init(row: Row) {
        collectionClass = row[CollectionGroupColumns.collectionClass]
        collectionClassName = row[CollectionGroupColumns.collectionClassName]
        displayOrder = row[CollectionGroupColumns.displayOrder]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[CollectionGroupColumns.collectionClass] = collectionClass
        container[CollectionGroupColumns.collectionClassName] = collectionClassName
        container[CollectionGroupColumns.displayOrder] = displayOrder
        
        super.encode(to: &container)
    }
}
