//
//  KyuyuRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class KyuyuRepo {
    private var resultDb: ResultDb
    private var user: LoggerUser
    
    init(_ resultDb: ResultDb, _ user: LoggerUser) {
        self.resultDb = resultDb
        self.user = user
    }
    
    func refuel(refueled: Refueled) {
        let k = CommonKyuyu(userInfo: user.userInfo, refueled: refueled, location: Current.Shared.lastLocation)
        resultDb.commonKyuyuDao?.insert(commonKyuyu: k)
    }

    func totalAmountOfCompany(allocationNo: String, fuelClass: String) -> Double {
        let userInfo = user.userInfo
        return resultDb.commonKyuyuDao?
                .getTotalAmount(companyCd: userInfo.companyCd, allocationNo: allocationNo, fuelClassCd: fuelClass) ?? 0
    }
}
