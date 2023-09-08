//
//  FullScreenCoverCompat.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/06.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct FullScreenCoverCompat<CoverContent: View>: ViewModifier {
  @Binding var isPresented: Bool
  let content: () -> CoverContent

  func body(content: Content) -> some View {
    GeometryReader { geo in
      ZStack {
        // this color makes sure that its enclosing ZStack
        // (and the GeometryReader) fill the entire screen,
        // allowing to know its full height
        Color.clear
        content
        ZStack {
          // the color is here for the cover to fill
          // the entire screen regardless of its content
          Color.white
          self.content()
        }
        .offset(y: isPresented ? 0 : geo.size.height)
        // feel free to play around with the animation speeds!
        .animation(.spring())
      }
    }
  }
}

extension View {
  func fullScreenCoverCompat<Content: View>(isPresented: Binding<Bool>,
                                            content: @escaping () -> Content) -> some View {
    self.modifier(FullScreenCoverCompat(isPresented: isPresented,
                                        content: content))
  }
}

struct FullScreenCoverCompatTest: View {
  @State private var isPresented = false

  var body: some View {
    Button("Show me") {
      isPresented = true
    }
    .fullScreenCoverCompat(isPresented: $isPresented) {
      ZStack {
        Color.green
        Button("Hide") {
          isPresented = false
        }
        .foregroundColor(.white)
      }
    }
  }
}

struct FullScreenCoverCompat_Previews: PreviewProvider {
    static var previews: some View {
        FullScreenCoverCompatTest()
    }
}
