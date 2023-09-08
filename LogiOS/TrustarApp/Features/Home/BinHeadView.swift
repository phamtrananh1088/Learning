//
//  BinHeadView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/11.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct BinHeadView: View {

    var binHeadVm: BinHeadViewModel
    
    var onTapRow: ((String) -> ())? = nil
    var onTapInfo: ((String) -> ())? = nil
    
    var body: some View {
        HStack {
            Text(binHeadVm.allocationNm)
                .font(.system(size: 18))
                .foregroundColor(.black)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(8)
            
            HStack {
                Text(binHeadVm.status)
                    .font(.system(size: 12))
                    .padding(.horizontal)
                    .padding(.vertical, 2)
                    .frame(maxWidth: 70)
                    .foregroundColor(Color(UIColor(rgbText: binHeadVm.textColor)))
                    .background(Color(UIColor(rgbText: binHeadVm.bgColor)))
                    .padding(8)

                Image(systemName: "info.circle.fill")
                    .font(.system(size: 22))
                    .foregroundColor(Color(Resources.colorPrimary))
                    .padding(5)
                    .onTapGesture {
                        if onTapInfo != nil {
                            onTapInfo!(binHeadVm.allocationNo)
                        }
                    }
            }
        }
        .padding(5)
        .background(Color(Resources.defaultBackground))
        .onTapGesture {
            if onTapRow != nil {
                onTapRow!(binHeadVm.allocationNo)
            }
        }
    }
}
