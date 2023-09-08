//
//  BinDetailAndStatus.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/17.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BinDetailAndStatus {
    var detail: BinDetail
    var status: WorkStatus
    
    init(detail: BinDetail, status: WorkStatus) {
        self.detail = detail
        self.status = status
    }
    
    func sameItem(_ b: BinDetailAndStatus) -> Bool {
        return status.workStatusCd == b.status.workStatusCd
        && detail.allocationNo == b.detail.allocationNo
        && detail.allocationRowNo == b.detail.allocationRowNo
    }
}
