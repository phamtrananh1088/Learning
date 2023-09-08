//
//  BellWithTextView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/10.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct BellWithTextView: View {
    var isImportant: Bool = false
    var totalUnread: Int = 0

    var body: some View {
        Image(systemName: "bell.fill")
            .resizable()
            .scaledToFit()
            .frame(width: 40, height: 40)
            .padding(4)
            .foregroundColor(Color(isImportant ? Resources.bell_important: Resources.bell_normal))
            .if (totalUnread > 0) {
                $0.overlay(
                    Circle()
                        .stroke(Color.white, lineWidth: 3)
                        .background(Color.red)
                        .cornerRadius(25)
                        .frame(width: 16, height: 16)
                        .overlay(
                            Text(String.init(totalUnread))
                                .foregroundColor(.white)
                                .frame(width: 35, height: 35, alignment: .center)
                                .font(.system(size: 8))
                        ).position(x: 30, y: 10)
                )
            }
    }
}

struct BellWithTextView_Previews: PreviewProvider {
    static var previews: some View {
        BellWithTextView()
    }
}
