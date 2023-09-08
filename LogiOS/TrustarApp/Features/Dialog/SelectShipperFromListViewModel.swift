//
//  SelectShipperFromListViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class SelectShipperFromListViewModel: ObservableObject {
    var currentSelect: String
    var listShipperItem: [ShipperItemViewModel] = []
    var chooseItemClick: (Shipper) -> ()
    @Published var search: String = String()
    
    init(currentSelect: String, chooseItemClick: @escaping (Shipper) -> ()) {
        self.currentSelect = currentSelect
        self.chooseItemClick = chooseItemClick
        
        listShipperItem = []
        if let lst = try? Current.Shared.userDatabase?.shipperDao?.selectAll() {
            for it in lst {
                listShipperItem.append(ShipperItemViewModel(shipper: it))
            }
        }
    }
}
