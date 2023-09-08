//
//  SheetListItemViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 09/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

class SheetListItemViewModel: Hashable {
    static func == (lhs: SheetListItemViewModel, rhs: SheetListItemViewModel) -> Bool {
        return lhs.shipper == rhs.shipper &&
        lhs.signStatus == rhs.signStatus &&
        lhs.signStatusBackground == rhs.signStatusBackground &&
        lhs.incidentalHeader == rhs.incidentalHeader &&
        lhs.incidentalListItem == rhs.incidentalListItem
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(shipper)
        hasher.combine(signStatus)
        hasher.combine(signStatusBackground)
        hasher.combine(incidentalHeader)
        hasher.combine(incidentalListItem)
    }
    
    var shipper: String
    var signStatus: String
    var signStatusBackground: Color
    var incidentalHeader: IncidentalHeader
    var incidentalListItem: IncidentalListItem
    
    init(bound: IncidentalListItem) {
        self.shipper = bound.shipperNm.nullToEmpty()
        self.incidentalHeader = bound.sheet
        
        switch bound.signStatus {
        case 0:
            self.signStatus = Resources.unsigned
            self.signStatusBackground = Color(UIColor(rgb: 0xff58d489))
        case 1:
            self.signStatus = Resources.signed
            self.signStatusBackground = Color(UIColor(rgb: 0xffdfcdbc))
        default:
            self.signStatus = String()
            self.signStatusBackground = Color.clear
        }
        
        self.incidentalListItem = bound
    }
}
