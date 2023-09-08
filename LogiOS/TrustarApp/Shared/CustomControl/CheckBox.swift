//
//  CheckBox.swift
//  TrustarApp
//
//  Created by CuongNguyen on 23/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct CheckBox: View {
    @Binding var checked: Bool
    var body: some View {
        if !checked {
            Image(systemName: "square")
                .foregroundColor(.gray)
                .font(.system(size: 22))
        } else {
            Image(systemName: "checkmark.square.fill")
                .foregroundColor(Color(Resources.colorPrimary))
                .font(.system(size: 22))
        }
    }
}
