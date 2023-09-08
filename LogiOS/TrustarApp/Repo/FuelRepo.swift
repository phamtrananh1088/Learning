//
//  FuelRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class FuelRepo {
    private var userDb: UserDb
    
    init(_ userDb: UserDb) {
        self.userDb = userDb
    }
    
    func fuelList() -> [Fuel]? {
        return userDb.fuelDao?.list()
    }
}
