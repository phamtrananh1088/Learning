//
//  TruckItemView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct TruckItemView: View {
    var truckItemVm: TruckItemViewModel
    
    var body: some View {
        HStack {
            Text(truckItemVm.code)
                .foregroundColor(.black)
                .bold()
                .font(.system(size: 16))
                .padding(8)
            
            Text(truckItemVm.name)
                .foregroundColor(.black)
                .font(.system(size: 16))
                .padding(8)
                .frame(maxWidth: .infinity, alignment: .leading)
        }.background(Color.white)
    }
}

//struct TruckItemView_Previews: PreviewProvider {
//    static var previews: some View {
//        TruckItemView()
//    }
//}
