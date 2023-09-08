//
//  BinResultWithWorkResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/28.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class BinResultWithWorkResult {
    var binResults: CommonBinResult
    var workResults: CommonWorkResult?
    
    init(binResults: CommonBinResult, workResults: CommonWorkResult?) {
        self.binResults = binResults
        self.workResults = workResults
    }
}
