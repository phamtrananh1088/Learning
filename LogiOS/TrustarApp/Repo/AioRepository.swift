//
//  AioRepository.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class AioRepository {
    var binHeaderRepo = BinHeaderRepo(Config.Shared.userDb!)
    var binDetailRepo = BinDetailRepo(Config.Shared.userDb!)
    var workRepo = WorkRepo(Config.Shared.userDb!)
    var fuelRepo = FuelRepo(Config.Shared.userDb!)
    var kyuyuRepo = KyuyuRepo(Config.Shared.resultDb!, Current.Shared.loggedUser!)
    var incidentalRepo = IncidentalRepo(Config.Shared.userDb!, Config.Shared.imageDb!, Current.Shared.loggedUser!)
    var deliveryChartRepo = DeliveryChartRepo(Config.Shared.userDb!)
}
