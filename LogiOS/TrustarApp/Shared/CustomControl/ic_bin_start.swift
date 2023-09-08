//
//  ic_bin_start.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/16.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct ic_bin_start: View {
    var body: some View {
        ZStack{
            Image(systemName: "line.diagonal")
                .font(.system(size: 20))
                .rotationEffect(.degrees(45))
                .offset(x: 0, y: -10)
                
            Image(systemName: "arrow.down")
                .font(.system(size: 20))
        }
    }
}

struct ic_bin_start_Previews: PreviewProvider {
    static var previews: some View {
        ic_bin_start()
    }
}
