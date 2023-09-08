//
//  SelectTruckFromListViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class SelectTruckFromListViewModel: ObservableObject {
    var currentSelect: String
    var listTruckItem: [TruckItemViewModel] = []
    var chooseItemClick: (Truck) -> ()
    @Published var search: String = String()
    
    init(currentSelect: String, chooseItemClick: @escaping (Truck) -> ()) {
        self.currentSelect = currentSelect
        self.chooseItemClick = chooseItemClick
        
        listTruckItem = []
        if let lst = try? Current.Shared.userDatabase?.truckDao?.selectAll() {
            for it in lst {
                listTruckItem.append(TruckItemViewModel(truck: it))
            }
        }
    }
}
