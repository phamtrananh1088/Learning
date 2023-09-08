//
//  BinHeadViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/11.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

struct BinHeadViewModel: Hashable {
    
    var allocationNm: String
    var status: String
    var textColor: String
    var bgColor: String
    var hasFinished: Bool
    var allocationNo: String

    init(_ binHeader: BinHeader, _ binStatus: BinStatus) {
        self.allocationNm = binHeader.allocationNm
        self.status = binStatus.binStatusNm
        self.textColor = binStatus.binFontColor
        self.bgColor = binStatus.binBackColor
        self.hasFinished = binStatus.getStatus() == .Finished
        self.allocationNo = binHeader.allocationNo
    }
    
    func sameItem(_ b: BinHeadViewModel) -> Bool {
        return allocationNm == b.allocationNm
        && status == b.status
        && textColor == b.textColor
        && bgColor == b.bgColor
    }
}
