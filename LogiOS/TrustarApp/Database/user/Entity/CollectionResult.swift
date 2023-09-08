//
//  CollectionResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum CollectionResultColumns: String, ColumnExpression {
    case allocationNo, allocationRowNo, json
    case collectionNo, collectionCd, collectionNm, expectedQuantity, actualQuantity, collectionClass, displayOrder
}

class CollectionResult: BaseEntity, Codable {
    var allocationNo: String = Resources.strEmpty
    var allocationRowNo: Int = Resources.zeroNumber
    var json: [RowCollectionResult] = []
    
    override init() {
        super.init()
    }
    
    init(allocationNo: String,
         allocationRowNo: Int,
         rows: [RowCollectionResult]
    ) {
        self.allocationNo = allocationNo
        self.allocationRowNo = allocationRowNo
        self.json = rows
        super.init()
    }
    
    override class var databaseTableName: String {"CollectionResult"}
    override class var primaryKey: String {"allocationNo,allocationRowNo"}
    
    required init(row: Row) {
        allocationNo = row[CollectionResultColumns.allocationNo]
        allocationRowNo = row[CollectionResultColumns.allocationRowNo]
        json = Helper.Shared.convertJsonToObject(json: row[CollectionResultColumns.json])
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[CollectionResultColumns.allocationNo] = allocationNo
        container[CollectionResultColumns.allocationRowNo] = allocationRowNo
        container[CollectionResultColumns.json] = Helper.Shared.convertObjectToJson(body: json)
        super.encode(to: &container)
    }
    
    func rows() -> [RowCollectionResult] {
        var list: [RowCollectionResult] = []
        for j in json {
            list.append(RowCollectionResult(collectionNo: j.collectionNo,
                                            collectionCd: j.collectionCd,
                                            collectionNm: j.collectionNm,
                                            expectedQuantity: j.expectedQuantity,
                                            actualQuantity: j.actualQuantity,
                                            collectionClass: j.collectionClass,
                                            displayOrder: j.displayOrder))
        }
        
        return list
    }
    
    class RowCollectionResult: Codable {
        var collectionNo: String?
        var collectionCd: String?
        var collectionNm: String?
        var expectedQuantity: Double
        var actualQuantity: Double
        var collectionClass: Int
        var displayOrder: Int
        
        init(collectionNo: String?,
             collectionCd: String?,
             collectionNm: String?,
             expectedQuantity: Double,
             actualQuantity: Double,
             collectionClass: Int,
             displayOrder: Int
        ){
            self.collectionNo = collectionNo
            self.collectionCd = collectionCd
            self.collectionNm = collectionNm
            self.expectedQuantity = expectedQuantity
            self.actualQuantity = actualQuantity
            self.collectionClass = collectionClass
            self.displayOrder = displayOrder
        }
        
        func emptyCd() -> Bool {
            return collectionCd == nil || collectionCd!.isEmpty
        }

        func newRow(quantity: Double, name: String) -> RowCollectionResult {
            return RowCollectionResult(collectionNo: collectionNo,
                                       collectionCd: collectionCd,
                                       collectionNm: emptyCd() ? name : collectionNm,
                                       expectedQuantity: expectedQuantity,
                                       actualQuantity: quantity,
                                       collectionClass: collectionClass,
                                       displayOrder: displayOrder)
        }
    }
}
