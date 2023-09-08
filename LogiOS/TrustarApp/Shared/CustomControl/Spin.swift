//
//  Spin.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/20.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct Spin: View {
    
    @State private var spin = false
    var color: Color = Color(#colorLiteral(red: 0.6588235294,green: 0.6588235294, blue: 0.6745098039, alpha: 1))
    var size: CGFloat = 32
    
    var body: some View {
        
        Circle()
            .trim(from: 1/4, to: 1)
            .stroke(style: StrokeStyle(lineWidth: 3, lineCap: .round,lineJoin: .round))
            .foregroundColor(color)
            .frame(width: size, height: size)
            .rotationEffect(.degrees(spin ? 360 : 0))
            .animation(Animation.linear(duration: 1)
                        .repeatForever(autoreverses: false), value: spin)
            .onAppear() {
                self.spin = true
            }
    }
}

struct Spin_Previews: PreviewProvider {
    static var previews: some View {
        Spin()
    }
}
