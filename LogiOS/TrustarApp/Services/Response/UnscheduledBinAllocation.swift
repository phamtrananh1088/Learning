//
//  UnscheduledBinAllocation.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class UnscheduledBinAllocation: Codable {
    var AllocationNo: String?
    var PostResponse: StateResult
    
    init(allocationNo: String?, postResponse: StateResult){
        self.AllocationNo = allocationNo
        self.PostResponse = postResponse
    }
    
    func toSingle() -> String {
        if PostResponse.result {
            let a = AllocationNo
            if a == nil {
                return String()
            }
            
            return a!
        } else {
            return String()
        }
    }
}
