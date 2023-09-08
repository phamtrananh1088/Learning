//
//  CircleTimer.swift
//  TrustarApp
//
//  Created by CuongNguyen on 07/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct CircleTimer: View {
    var basedSystemTimeMillis: Int64
    var colorLine: Color
    var belowText: String = String()
    var totalMillis: Int = 3600_000
    
    let timer = Timer.publish(every: 0.2, on: .main, in: .common).autoconnect()
    
    private var timeDisplay: String {
        return String(format: "%02d:%02d", basedRealTime / 3600, (basedRealTime / 60) % 60)
    }
    
    @State private var basedRealTime: Int64 = 0
    var body: some View {
        ZStack {
            ProgressBar(basedRealTime: $basedRealTime, colorLine: colorLine)
            Text(timeDisplay)
                .onReceive(timer, perform: {(_) in
                    self.basedRealTime = (currentTimeMillis() - basedSystemTimeMillis) / 1000
                })
                .foregroundColor(colorLine)
                .font(.system(size: 39))
                .lineLimit(1)
                .minimumScaleFactor(0.01)
            
            Text(belowText)
                .font(.system(size: 14))
                .foregroundColor(.black)
                .offset(x: 0, y: 30)
        }
        .frame(width: 120.0, height: 120.0)
        .background(Color.white)
    }
}

struct ProgressBar: View {
    
    @Binding var basedRealTime: Int64
    var colorLine: Color
    
    private let offsetDegree: Double = 270.0
    
    var body: some View {
        ZStack {
            Circle()
                .stroke(lineWidth: 3)
                .opacity(0.3)
                .foregroundColor(colorLine)
            
            // line
            Circle()
                .trim(from: 0.0, to: (Double(basedRealTime) / Double(60) * 6) / Double(360))
                .stroke(
                    style: StrokeStyle(
                        lineWidth: 3,
                        lineCap: .round,
                        lineJoin: .round))
                .foregroundColor(colorLine)
                .rotationEffect(Angle(degrees: offsetDegree))

            // point
            Circle()
                .trim(from: 0.0, to: 0.001)
                .stroke(
                    style: StrokeStyle(
                        lineWidth: 10,
                        lineCap: .round,
                        lineJoin: .round))
                .foregroundColor(colorLine)
                .rotationEffect(Angle(degrees: offsetDegree + (Double(basedRealTime) / Double(60)) * 6))
        }
    }
}
//
//struct CircleTimer_Previews: PreviewProvider {
//    static var previews: some View {
//        CircleTimer(basedSystemTimeMillis: 1646989313306)
//    }
//}
