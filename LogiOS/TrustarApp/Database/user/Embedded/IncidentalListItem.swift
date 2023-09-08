//
//  IncidentalListItem.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/17.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class IncidentalListItem: Equatable, Hashable {
    static func == (lhs: IncidentalListItem, rhs: IncidentalListItem) -> Bool {
        return lhs.shipper == rhs.shipper && lhs.sheet == rhs.sheet
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(shipper)
        hasher.combine(sheet)
    }
    
    var shipper: Shipper?
    var sheet: IncidentalHeader
    
    init(shipper: Shipper?, sheet: IncidentalHeader) {
        self.shipper = shipper
        self.sheet = sheet
    }
    
    var shipperNm: String? {
        return shipper?.shipperNm1
    }
    
    var signStatus: Int {
        return sheet.status
    }
}
