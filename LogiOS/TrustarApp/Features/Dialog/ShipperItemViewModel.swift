//
//  ShipperItemViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class ShipperItemViewModel {
    var shipper: Shipper
    var code: String
    var name: String
    
    init(shipper: Shipper) {
        self.shipper = shipper
        self.code = shipper.shipperCd
        self.name = shipper.shipperNm1
    }
}
