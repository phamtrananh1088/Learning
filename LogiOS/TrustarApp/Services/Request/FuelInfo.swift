//
//  FuelInfo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 16/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class FuelInfo: Codable {
    var Kyuyus: [CommonKyuyu]
    var clientInfo: ClientInfo
    init(Kyuyus: [CommonKyuyu], clientInfo: ClientInfo) {
        self.Kyuyus = Kyuyus
        self.clientInfo = clientInfo
    }
}
