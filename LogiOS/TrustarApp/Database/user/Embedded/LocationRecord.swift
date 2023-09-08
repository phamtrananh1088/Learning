//
//  LocationRecord.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/17.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation

class LocationRecord {
    var latitude: Double?
    var longitude: Double?
    var accuracy: Double?
    var date: Int64
    
    init(location: CLLocation?, date: Int64) {
        self.latitude = location?.coordinate.latitude
        self.longitude = location?.coordinate.longitude
        self.accuracy = location?.horizontalAccuracy
        self.date = date
    }
}
