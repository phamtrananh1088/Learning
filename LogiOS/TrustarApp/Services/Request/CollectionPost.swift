//
//  CollectionPost.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class CollectionPost {
    var userInfo: UserInfo?
    var collections: [CommonCollectionResult]
    
    init(userInfo: UserInfo?,
         collections: [CommonCollectionResult]) {
        self.userInfo = userInfo
        self.collections = collections
    }
    
    class ResultClass: Codable {
        var collectionResults: [CollectionResult] = []
    }
    
    class CollectionResult: Codable {
        var companyCd: String = ""
        var userId: String = ""
        var addCollection: [CommonCollectionResult.CommonRow] = []
    }
    
    func result() -> ResultClass {
        let rs: ResultClass = ResultClass()
        let collectionResult = CollectionResult()
        collectionResult.companyCd = userInfo!.companyCd
        collectionResult.userId = userInfo!.userId
        
        collectionResult.addCollection = []
        for it in collections {
            for r in it.postJson {
                collectionResult.addCollection.append(r)
            }
        }
        
        rs.collectionResults.append(collectionResult)
        return rs
    }
}
