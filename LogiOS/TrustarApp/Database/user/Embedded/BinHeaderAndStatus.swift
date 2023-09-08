//
//  BinHeaderAndStatus.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/17.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BinHeaderAndStatus {
    var header: BinHeader
    var truck: Truck
    var status: BinStatus
    
    init(header: BinHeader, truck: Truck, status: BinStatus) {
        self.header = header
        self.truck = truck
        self.status = status
    }
}
