//
//  UnscheduledBin.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class UnscheduledBin: Codable {
    var companyCd: String
    var userId: String
    var truckCd: String
    var latitude: Double
    var longitude: Double
    var accuracy: Double
    var executeDate: Int64
    var outgoingMeter: Int? = nil
    var allocationToken: AllocationToken
    var clientInfo: ClientInfo
    
    init(token: String,
         truck: Truck,
         locationRecord: LocationRecord,
         clientInfo: ClientInfo,
         userInfo: UserInfo,
         outgoingMeter: Int?
    ) {
        self.companyCd = userInfo.companyCd
        self.userId = userInfo.userId
        self.allocationToken = AllocationToken(token: token)
        self.truckCd = truck.truckCd
        self.latitude = locationRecord.latitude ?? 0.0
        self.longitude = locationRecord.longitude ?? 0.0
        self.accuracy = locationRecord.accuracy ?? 0.0
        self.executeDate = locationRecord.date
        self.outgoingMeter = outgoingMeter
        self.clientInfo = clientInfo
    }
}

class AllocationToken: Codable {
    var token: String
    init(token: String) {
        self.token = token
    }
}
