//
//  ShowProgressDialog.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/21.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct ShowProgressDialog: View {
    var title: String
    var message: String
    
    var body: some View {
        ZStack {
            Color.black.opacity(0.5).edgesIgnoringSafeArea(.all)
            VStack {
                Text(title)
                    .fontWeight(.bold)
                    .foregroundColor(.black)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                HStack {
                    Spin(color: .blue)
                    Text(message)
                        .foregroundColor(.black)
                        .padding(.leading)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding()
            .background(Color.white)
            .padding()
        }
    }
}
