//
//  WorkBin.swift
//  TrustarApp
//
//  Created by CuongNguyen on 04/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum WorkBinEnum: Hashable {
    case New, Finished, Bin
}

class WorkBin {
    public private(set) var bin: AnyObject? = nil
    public private(set) var binType: WorkBinEnum? = nil
    
    func onNext<T: BinProtocol>(_ wBinEnum: WorkBinEnum, _ model: T) {
        binType = wBinEnum
        
        switch wBinEnum {
        case .New:
            onMapBinHeader(binHeader: model as! BinHeader)
        case .Finished:
            onMapBinDetail(binDetail: model as! BinDetail)
        case .Bin:
            onMapBinDetail(binDetail: model as! BinDetail)
        }
    }
    
    func clear() {
        bin = nil
        binType = nil
    }
    
    private func onMapBinDetail(binDetail: BinDetail) {
        let b = bin as? BinDetail
        if b == nil {
            bin = binDetail
        } else if b != nil {
            if b!.allocationNo != binDetail.allocationNo || b!.allocationRowNo != binDetail.allocationRowNo {
                bin = binDetail
            }
        }
    }
    
    private func onMapBinHeader(binHeader: BinHeader) {
        let b = bin as? BinHeader
        if b == nil {
            bin = binHeader
        } else if b != nil {
            if b!.allocationNo != binHeader.allocationNo {
                bin = binHeader
            }
        }
    }
}
