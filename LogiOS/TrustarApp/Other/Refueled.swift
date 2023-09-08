//
//  Refueled.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 13/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
class Refueled {
    var binHeader: BinHeader
    var fuel: Fuel
    var quantity: Double
    var paid: Double
    
    init(binHeader: BinHeader, fuel: Fuel, quantity: Double, paid: Double) {
        self.binHeader = binHeader
        self.fuel = fuel
        self.quantity = quantity
        self.paid = paid
    }
}
