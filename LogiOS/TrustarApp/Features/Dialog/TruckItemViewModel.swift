//
//  TruckItemViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class TruckItemViewModel {
    var truck: Truck
    var code: String
    var name: String
    
    init(truck: Truck) {
        self.truck = truck
        self.code = truck.truckCd
        self.name = truck.truckNm
    }
}
