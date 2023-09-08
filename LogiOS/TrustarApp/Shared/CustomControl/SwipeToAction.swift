//
//  SwipeToAction.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/28/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

public struct SwipeToAction: ViewModifier, Animatable {
    
    @State var offset: CGSize = CGSize.zero
    private var action: () -> Void
    
    public init(action: @escaping () -> Void) {
        self.action = action
    }
    
    public func body(content: Content) -> some View {
        
        GeometryReader { geo in
            HStack {
                content
                    .padding(.horizontal, 16)
                    .frame(width: geo.size.width, height: geo.size.height, alignment: .leading)
                    .offset(self.offset)
            }
            .gesture(
                DragGesture()
                    .onChanged { value in
                        let amount = value.translation.width
                        withAnimation {
                            offset.width = amount
                        }
                    }.onEnded { value in
                        if abs(offset.width) > 150 {
                            withAnimation {
                                offset.width = 1.2 * (offset.width > 0 ? geo.size.width : -geo.size.width)
                            }
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                                withAnimation {
                                    action()
                                }
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
