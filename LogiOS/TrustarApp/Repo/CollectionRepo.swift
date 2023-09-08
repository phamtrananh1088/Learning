//
//  CollectionRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class CollectionRepo {
    private var userDb: UserDb
    init(_ userDb: UserDb) {
        self.userDb = userDb
    }
    
    func collectionItems() -> [CollectionGroup] {
        return userDb.collectionItemDao!.selectAll()
    }

    func collectionResults(allocationNo: String, allocationRowNo: Int) -> CollectionResult? {
        return userDb.collectionResultDao?.getResult(allocationNo: allocationNo, allocationRowNo: allocationRowNo)
    }
    
    func collectionResults(allocationNo: String, allocationRowNo: Int) -> AnyPublisher<CollectionResult?, Error> {
        return userDb.collectionResultDao!.getResult(allocationNo: allocationNo, allocationRowNo: allocationRowNo)
    }

    func saveResult(collectionResult: CollectionResult) {
        do {
            try userDb.instanceDb?.write {db in
                try collectionResult.save(db)
            }
        } catch {
            debugPrint(error)
        }
    }
}
