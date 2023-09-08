//
//  SwipeToBack.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

public struct SwipeToBack: ViewModifier, Animatable {
    @Binding var isPresented: Bool
    var action: () -> Void
    @State var offset: CGSize = CGSize.zero
    
    public func body(content: Content) -> some View {
        GeometryReader { geo in
            if isPresented {
                content
                    .offset(self.offset)
                    .gesture(
                        DragGesture()
                            .onChanged { value in
                                if value.startLocation.x > 30 { return }
                                let amount = value.translation.width
                                if amount < 20 {
                                    withAnimation {
                                        offset.width = 0
                                    }
                                    return
                                }
                                
                                let distance = amount - offset.width
                                if distance > 30 {
                                    withAnimation {
                                        action()
                                        offset.width = 0
                                    }
                                    
                                    return
                                }

                                
                                offset.width = amount
                            }.onEnded { value in
                                if value.startLocation.x > 30 { return }
                                if abs(offset.width) > 200 {
                                    withAnimation {
                                        offset.width = 1.2 * (offset.width > 0 ? geo.size.width : -geo.size.width)
                                        action()
                                        offset.width = 0
                                    }
                                }
                                else {
                                    withAnimation {
                                        offset.width = 0
                                    }
                                }
                            }
                    )
            }
        }
    }
}

