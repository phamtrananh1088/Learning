//
//  CollectViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class CollectViewModel: BaseViewModel, ObservableObject {
    private var repo = CollectionRepo(Current.Shared.userDatabase!)
    @Published public private(set) var rx: BinDetail?
    @Published public private(set) var isLoading = false
    
    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Collection.rawValue), object: nil)
        
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Bin.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {

        switch notification.name.rawValue {
        case BroadcastEnum.Collection.rawValue, BroadcastEnum.Bin.rawValue:
            self.setData()
            self.isLoading = false
            break
        default:
            break
        }
    }
    
    func setBinDetail(binDetail: BinDetail) {
        rx = binDetail
        Helper.Shared.checkURLSessionContaintUrl(lstEndpoint: [.postCollection,.binData], onResult: { result in
            self.isLoading = result
        })
        
        setData(isInit: true)
    }
    
    var title: String = String()
    
    private func setData(isInit: Bool = false) {
        let b = rx ?? BinDetail()
        
        title = b.placeNm1.orEmpty()
        
        if !isInit && collectionsLiveData.contains(where: { $0.1.contains(where: { $0.hasChanged })}) { return }
        collectionsLiveData = collections
    }
    
    class Row: Codable {
        var result: CollectionResult.RowCollectionResult
        var nameInit: String
        var actualInit: Double
        var isAddNew: Bool

        init(result: CollectionResult.RowCollectionResult, isAddNew: Bool = false) {
            self.result = result
            self.nameInit = result.collectionNm.orEmpty()
            self.actualInit = result.actualQuantity
            self.isAddNew = isAddNew
        }
        
        var name: String {
            return result.collectionNm.orEmpty()
        }
        
        var expectedQuantity: Double {
            return result.expectedQuantity
        }
        
        var actualQuantity: Double {
            return result.actualQuantity
        }
        
        var hasChanged: Bool {
            return actualQuantity != actualInit || name != nameInit || isAddNew
        }
    }
    
    @Published private var added: [Row] = []
    private var collections: [(CollectionGroup, [Row])] {
        let g = repo.collectionItems()
        let b = rx ?? BinDetail()

        let results = repo.collectionResults(allocationNo: b.allocationNo, allocationRowNo: b.allocationRowNo)?.rows() ?? []
        var t2: [Row] = []
        for it in results {
            t2.append(Row(result: it))
        }
        
        let t = (t2 + added).sorted(by: { $0.result.displayOrder < $1.result.displayOrder })
        let gs = Dictionary(grouping: t, by: { $0.result.collectionClass })
        
        var list: [(CollectionGroup, [Row])] = []
        for it in g {
            let l = gs[it.collectionClass] ?? []
            list.append((it, l))
        }
        
        return list
    }
    
    @Published var collectionsLiveData: [(CollectionGroup, [Row])] = []
    
    func addRow(name: String, groupIdx: Int) {
        let group = collectionsLiveData[groupIdx].0
        let row = Row(result: CollectionResult.RowCollectionResult(collectionNo: nil,
                                                                   collectionCd: nil,
                                                                   collectionNm: name,
                                                                   expectedQuantity: 0.0,
                                                                   actualQuantity: 0.0,
                                                                   collectionClass: group.collectionClass,
                                                                   displayOrder: Int(currentTimeMillis() & Int64(Int.max))), isAddNew: true)
        
        collectionsLiveData[groupIdx].1.insert(row, at: 0)
    }
    
    func editRow(name: String, groupIdx: Int, itemIdx: Int) {
        collectionsLiveData[groupIdx].1[itemIdx].result.collectionNm = name
    }
    
    func save() {
        let b = rx ?? BinDetail()
        let c = collectionsLiveData
       
        var l: [CollectionResult.RowCollectionResult] = []
        for it in c {
            for row in it.1 {
                l.append(row.result.newRow(quantity: row.actualQuantity, name: row.name))
            }
        }
        
        let r = CollectionResult(allocationNo: b.allocationNo,
                                 allocationRowNo: b.allocationRowNo,
                                 rows: l)
        r.recordChanged()
        repo.saveResult(collectionResult: r)

        Current.Shared.syncBin(callBack: {(isOk, Error) in })
    }
}
