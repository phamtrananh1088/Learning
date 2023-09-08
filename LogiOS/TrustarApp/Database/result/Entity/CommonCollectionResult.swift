//
//  CommonCollectionResult.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonCollectionResultColumns: String, ColumnExpression {
    case companyCd, userId, allocationNo, allocationRowNo, postJson
}

class CommonCollectionResult: BaseEntity, Codable {
    var companyCd: String = Resources.strEmpty
    var userId: String = Resources.strEmpty
    var allocationNo: String = Resources.strEmpty
    var allocationRowNo: Int = Resources.zeroNumber
    var postJson: [CommonRow] = []
    
    override init() {
        super.init()
    }

    override class var databaseTableName: String { "CommonCollectionResult" }
    
    override class var primaryKey: String { "companyCd,userId,allocationNo,allocationRowNo" }

    required init(row: Row) {
        companyCd = row[CommonCollectionResultColumns.companyCd]
        userId = row[CommonCollectionResultColumns.userId]
        allocationNo = row[CommonCollectionResultColumns.allocationNo]
        allocationRowNo = row[CommonCollectionResultColumns.allocationRowNo]
        postJson = Helper.Shared.convertJsonToObject(json: row[CommonCollectionResultColumns.postJson])
        super.init(row: row)
    }
    
    init(userInfo: UserInfo, result: CollectionResult) {
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        allocationNo = result.allocationNo
        allocationRowNo = result.allocationRowNo
        
        let r = result.rows()
        for it in r {
            postJson.append(CommonRow(allocationNo: result.allocationNo,
                                      allocationRowNo: result.allocationRowNo,
                                      collectionNo: it.collectionNo,
                                      collectionCd: it.collectionCd,
                                      collectionNm: it.collectionNm,
                                      actualQuantity: it.actualQuantity,
                                      collectionClass: it.collectionClass))
        }
        
        super.init()
        self.recordChanged()
    }

    override func encode(to container: inout PersistenceContainer) {
        container[CommonCollectionResultColumns.companyCd] = companyCd
        container[CommonCollectionResultColumns.userId] = userId
        container[CommonCollectionResultColumns.allocationNo] = allocationNo
        container[CommonCollectionResultColumns.allocationRowNo] = allocationRowNo
        container[CommonCollectionResultColumns.postJson] = Helper.Shared.convertObjectToJson(body: postJson)
        super.encode(to: &container)
    }
    
    class CommonRow: Codable {
        var allocationNo: String
        var allocationRowNo: Int
        var collectionNo: String?
        var collectionCd: String?
        var collectionNm: String?
        var collectionCountRes: Double
        var collectionClass: Int
        
        init(allocationNo: String,
             allocationRowNo: Int,
             collectionNo: String?,
             collectionCd: String?,
             collectionNm: String?,
             actualQuantity: Double,
             collectionClass: Int
        ){
            self.allocationNo = allocationNo
            self.allocationRowNo = allocationRowNo
            self.collectionNo = collectionNo
            self.collectionCd = collectionCd
            self.collectionNm = collectionNm
            self.collectionCountRes = actualQuantity
            self.collectionClass = collectionClass
        }
    }
}
