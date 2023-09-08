//
//  IncidentalItemData.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class IncidentalItemData: ObservableObject {
    @Published var shipper: Shipper?
    @Published var works: [IncidentalWork?]?
    @Published var times: [TimeItem]?
    @Published var editTarget: EditTarget?
    
    var shipperNm: String? {
        return shipper?.shipperNm1
    }
    
    var joinedWorkName: String? {
        let arrayWork = works?.compactMap({
            $0?.workNm ?? "???"
        })
        return arrayWork?.joined(separator: "／")
    }
    
    init (shipper: Shipper?, works: [IncidentalWork?]?, times: [TimeItem]?, editTarget: EditTarget?) {
        self.shipper = shipper
        self.works = works
        self.times = times
        self.editTarget = editTarget
    }
}

class IncidentalItemDataDB: IncidentalItemData {
    var item: IncidentalListItem
    
    init (item: IncidentalListItem, works: [IncidentalWork?]?, times: [IncidentalTime]) {
        self.item = item
        super.init(shipper: item.shipper,
                   works: works,
                   times: times.map({TimeItemDB(time: $0)}).sorted(by: {(l: TimeItemDB, r: TimeItemDB) -> Bool in
                        guard l.begin != nil && l.end != nil && r.begin != nil && r.end != nil
                        else { return true }
            
                        if l.begin?.date != r.begin?.date {
                            return l.begin!.date < r.begin!.date
                        } else {
                            return l.end!.date < r.end!.date
                        }
                    }),
                   editTarget: EditTarget.Sheet(uuid: item.sheet.uuid))
    }
    
}
