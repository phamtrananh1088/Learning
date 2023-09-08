//
//  ArrowShape.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/01/10.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import SwiftUI

public struct ArrowShape: Shape {
    public func path(in rect: CGRect) -> Path {
        var path = Path()
        path.addLines([
            CGPoint(x: 0, y: rect.height),
            CGPoint(x: rect.width / 2, y: 0),
            CGPoint(x: rect.width, y: rect.height),
        ])
        return path
    }
}
