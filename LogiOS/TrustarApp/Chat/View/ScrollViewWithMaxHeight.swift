//
//  ScrollViewWithMaxHeight.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

struct ScrollViewWithMaxHeight: View {
    
    @State var scale: CGFloat = 1
    @State var scaleAnchor: UnitPoint = .center
    @State var lastScale: CGFloat = 1
    @State var offset: CGSize = .zero
    @State var lastOffset: CGSize = .zero
    @State var debug = ""
    @State var opacity: Double = 1
    @State var scaleLevel: ScaleLevelEnum = .defLvl {
        didSet {
            switch scaleLevel {
            case .defLvl:
                scale = 1
            case .firstLvl:
                scale = 2
            case .secondLvl:
                scale = 4
            }
        }
    }
    @Binding var isPresented: Bool
    
    let image: UIImage?
    
    var body: some View {
        GeometryReader { geometry in
            if isPresented {
                let magnificationGesture = MagnificationGesture()
                    .onChanged{ gesture in
                        scaleAnchor = .center
                        scale = lastScale * gesture
                    }
                    .onEnded { _ in
                        fixOffsetAndScale(geometry: geometry)
                    }
                
                let dragGesture = DragGesture()
                    .onChanged { gesture in
                        var newOffset = lastOffset
                        let w = gesture.translation.width
                        let h = gesture.translation.height
                        
                        newOffset.width += w
                        newOffset.height += h
                        offset = newOffset
                        
                        if scale == 1 {
                            opacity = 0.01 * (100 - Double((abs(h) + abs(w)) / Double(2)))
                        }
                    }
                    .onEnded { gesture in
                        let w = gesture.translation.width
                        let h = gesture.translation.height
                        
                        if scale == 1 {
                            if (abs(h) > 100 || abs(w) > 100) {
                                withAnimation {
                                    isPresented = false
                                }
                            }
                        }
                        
                        fixOffsetAndScale(geometry: geometry)
                        withAnimation {
                            opacity = 1
                        }
                    }
                
                if image != nil {
                    Image(uiImage: image!)
                        .resizable()
                        .scaledToFit()
                        .position(x: geometry.size.width / 2,
                                  y: geometry.size.height / 2)
                        .scaleEffect(scale, anchor: scaleAnchor)
                        .offset(offset)
                        .gesture(dragGesture)
                        .gesture(magnificationGesture)
                        .gesture(TapGesture(count: 2)
                            .onEnded {
                                switch scaleLevel {
                                case .defLvl:
                                    scaleLevel = .firstLvl
                                case .firstLvl:
                                    scaleLevel = .secondLvl
                                case .secondLvl:
                                    scaleLevel = .defLvl
                                }
                                
                                scaleAnchor = .center
                                fixOffsetAndScale(geometry: geometry)
                        })
                        .background(Color.black.opacity(opacity))
                }
            }
        }
        .edgesIgnoringSafeArea(.all)
    }
    
    private func fixOffsetAndScale(geometry: GeometryProxy) {
        guard let image = image else { return }
        
        let newScale: CGFloat = .minimum(.maximum(scale, 1), 4)
        let screenSize = geometry.size
        
        let originalScale = image.size.width / image.size.height >= screenSize.width / screenSize.height ?
            geometry.size.width / image.size.width :
            geometry.size.height / image.size.height
        
        let imageWidth = (image.size.width * originalScale) * newScale
        
        var width: CGFloat = .zero
        if imageWidth > screenSize.width {
            let widthLimit: CGFloat = imageWidth > screenSize.width ?
                (imageWidth - screenSize.width) / 2
                : 0

            width = offset.width > 0 ?
                .minimum(widthLimit, offset.width) :
                .maximum(-widthLimit, offset.width)
        }
        
        let imageHeight = (image.size.height * originalScale) * newScale
        var height: CGFloat = .zero
        if imageHeight > screenSize.height {
            let heightLimit: CGFloat = imageHeight > screenSize.height ?
                (imageHeight - screenSize.height) / 2
                : 0

            height = offset.height > 0 ?
                .minimum(heightLimit, offset.height) :
                .maximum(-heightLimit, offset.height)
        }
        
        let newOffset = CGSize(width: width, height: height)
        lastScale = newScale
        lastOffset = newOffset
        withAnimation() {
            offset = newOffset
            scale = newScale
        }
    }
}
