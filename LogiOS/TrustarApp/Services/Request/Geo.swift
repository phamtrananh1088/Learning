//
//  Geo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 18/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class Geo: Codable {
    var coordinates: [CommonCoordinate]
    var clientInfo: ClientInfo
    init(coordinates: [CommonCoordinate], clientInfo: ClientInfo) {
        var coordinateTemp: [CommonCoordinate] = []
        for co in coordinates {
            if !coordinateTemp.contains(where: {
                $0.latitude == co.latitude
                && $0.longitude == co.longitude
                && $0.accuracy == co.accuracy
            }) {
                coordinateTemp.append(co)
            }
        }
        
        self.coordinates = coordinateTemp
        self.clientInfo = clientInfo
    }
}
