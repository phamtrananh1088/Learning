//
//  SheetListItemView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 09/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct SheetListItemView: View {
    var sheetListItemVm: SheetListItemViewModel
    var body: some View {
        HStack {
            // Shipper
            Text(sheetListItemVm.shipper)
                .foregroundColor(.black)
                .font(.system(size: 18))
                .padding(8)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            // signStatus
            if (!sheetListItemVm.signStatus.isEmpty) {
                Text(sheetListItemVm.signStatus)
                    .foregroundColor(.white)
                    .font(.system(size: 14))
                    .frame(maxWidth: 70)
                    .padding(4)
                    .background(sheetListItemVm.signStatusBackground)
            }
        }
        .padding(8)
        .background(Color.white)
    }
}
