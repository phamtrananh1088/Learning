//
//  SignDrawingView.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 20/02/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

private let placeHolderText = Resources.tap_to_edit
private let bigFontSize: CGFloat = 44

struct FramePreferenceKey: PreferenceKey {
    static var defaultValue: CGRect = .zero
    
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value = nextValue()
    }
}

struct DrawingView: View {
    @Binding var drawing: DrawingPath
    @Binding var color: Color
    @Binding var lineWidth: CGFloat
    @Binding var maxHeight: CGFloat
    
    @State private var drawingBounds: CGRect = .zero
    
    var body: some View {
        ZStack {
            //Drawing background
            Color.white
                .background(GeometryReader { geometry in
                    Color.clear.onAppear(perform: {
                        maxHeight = geometry.size.height
                    })
                    Color.clear.preference(key: FramePreferenceKey.self
                                           , value: geometry.frame(in: .local))
                })
                .onPreferenceChange(FramePreferenceKey.self) { bounds in
                    drawingBounds = bounds
                }
            
            if drawing.isEmpty {
                /*Text(placeHolderText)
                    .foregroundColor(.gray)
                    .font(.system(size: bigFontSize))*/
            } else {
                DrawingShape(drawingPath: drawing)
                    .stroke(style: StrokeStyle(lineWidth: lineWidth, lineCap: .round, lineJoin: .round, miterLimit: 0
                                               , dash: [], dashPhase: 0))
                    .foregroundColor(color)
            }
        }
        //.frame(height: maxHeight == 0 ? .infinity : maxHeight)
        .gesture(DragGesture(minimumDistance: 0)
                    .onChanged({ value in
                        if drawingBounds.contains(value.location) {
                            if value.location == value.startLocation {
                                drawing.addPoint(value.location)
                                drawing.addPoint(CGPoint(x: value.location.x + 1, y: value.location.y))
                            } else {
                                drawing.addPoint(value.location)
                            }
                        } else {
                            drawing.addBreak()
                        }
                    })
                    .onEnded({ value in
                        drawing.addBreak()
                    })
        )
        .overlay(RoundedRectangle(cornerRadius: 5)
                    .stroke(Color.gray))
    }
}
