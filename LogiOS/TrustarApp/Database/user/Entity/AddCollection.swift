//
//  AddCollection.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum AddCollectionColumns: String, ColumnExpression {
    case AllocationNo, AllocationRowNo, collectionNo, collectionClass, collectionCd, collectionNm, collectionCount, collectionCountRes
}

class AddCollection: BaseEntity, Codable {
    var AllocationNo:       String = Resources.strEmpty
    var AllocationRowNo:    String = Resources.strEmpty
    var collectionNo:       String = Resources.strEmpty
    var collectionClass:    Int = Resources.zeroNumber
    var collectionCd:       String = Resources.strEmpty
    var collectionNm:       String = Resources.strEmpty
    var collectionCount:    Decimal = 0
    var collectionCountRes: Decimal = 0
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"AddCollection"}
    
    required init(row: Row) {
        AllocationNo = row[AddCollectionColumns.AllocationNo]
        AllocationRowNo = row[AddCollectionColumns.AllocationRowNo]
        collectionNo = row[AddCollectionColumns.collectionNo]
        collectionClass = row[AddCollectionColumns.collectionClass]
        collectionCd = row[AddCollectionColumns.collectionCd]
        collectionNm = row[AddCollectionColumns.collectionNm]
        collectionCount = row[AddCollectionColumns.collectionCount]
        collectionCountRes = row[AddCollectionColumns.collectionCountRes]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[AddCollectionColumns.AllocationNo] = AllocationNo
        container[AddCollectionColumns.AllocationRowNo] = AllocationRowNo
        container[AddCollectionColumns.collectionNo] = collectionNo
        container[AddCollectionColumns.collectionClass] = collectionClass
        container[AddCollectionColumns.collectionCd] = collectionCd
        container[AddCollectionColumns.collectionNm] = collectionNm
        container[AddCollectionColumns.collectionCount] = collectionCount
        container[AddCollectionColumns.collectionCountRes] = collectionCountRes
        
        super.encode(to: &container)
    }
}
