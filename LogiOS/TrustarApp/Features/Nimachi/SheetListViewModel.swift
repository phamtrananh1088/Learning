//
//  SheetListViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 02/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class SheetListViewModel: ObservableObject {
    @Published public private(set) var bin: BinDetail = BinDetail()
    
    private var repo = Current.Shared.userRepository!.incidentalRepo
    private var listLiveData: [IncidentalListItem] {
        return repo.sortedSheetList(allocationNo: bin.allocationNo, allocationRowNo: bin.allocationRowNo)
    }
    
    var listItem: [SheetListItemViewModel] {
        var lst: [SheetListItemViewModel] = []
        for it in listLiveData {
            lst.append(SheetListItemViewModel(bound: it))
        }
        
        return lst
    }
    
    func setBinDetail(binDetail: BinDetail) {
        self.bin = binDetail
    }
    
    func removeIncidental(item: IncidentalListItem) {
        item.sheet.deleted = true
        repo.saveIncidentalHeader(header: item.sheet)
        setBinDetail(binDetail: bin)
        Current.Shared.syncIncidental() {(_,_) in }
    }
}
