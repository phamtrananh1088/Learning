//
//  BinHeadUIItem.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/15.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BinHeadUIItem {
    var binHead: BinHeaderAndStatus
    var allocationNo: String
    var allocationNm: String
    var status: String
    var textColor: String
    var bgColor: String
    var hasFinished: Bool
    
    init(bin: BinHeaderAndStatus) {
        self.binHead = bin
        
        self.allocationNo = bin.header.allocationNo
        self.allocationNm = bin.header.allocationNm
        self.status = bin.status.binStatusNm
        self.textColor = bin.status.binFontColor
        self.bgColor = bin.status.binBackColor
        self.hasFinished = bin.status.getStatus() == .Finished
    }
}
