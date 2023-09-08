//
//  ShipperItemView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct ShipperItemView: View {
    var shipperItemVm: ShipperItemViewModel
    
    var body: some View {
        HStack {
            Text(shipperItemVm.code)
                .foregroundColor(.black)
                .bold()
                .font(.system(size: 16))
                .padding(8)
            
            Text(shipperItemVm.name)
                .foregroundColor(.black)
                .font(.system(size: 16))
                .padding(8)
                .frame(maxWidth: .infinity, alignment: .leading)
        }.background(Color.white)
    }
}
