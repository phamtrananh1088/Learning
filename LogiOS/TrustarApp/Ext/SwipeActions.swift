//
//  SwipeToDeleteRow.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 20/02/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

// Button in swipe action, renders text or image and can have background color
struct SwipeActionButton: View, Identifiable {
    static let width: CGFloat = 70
    
    let id = UUID()
    let text: Text?
    let icon: Image?
    let action: () -> Void
    let tint: Color?
    
    init(text: Text? = nil,
         icon: Image? = nil,
         action: @escaping () -> Void,
         tint: Color? = nil) {
        self.text = text
        self.icon = icon
        self.action = action
        self.tint = tint ?? .gray
    }
    
    var body: some View {
        ZStack {
            tint
            VStack {
                icon?
                    .foregroundColor(.white)
                if icon == nil {
                    text?
                        .foregroundColor(.white)
                }
            }
            .frame(width: SwipeActionButton.width)
        }
    }
}

// Adds custom swipe actions to a given view
@available(iOS 13.0, *)
struct SwipeActionView: ViewModifier {
  // How much does the user have to swipe at least to reveal buttons on either side
    private static let minSwipeableWidth = SwipeActionButton.width * 0.8
    
  // Buttons at the leading (left-hand) side
    let leading: [SwipeActionButton]
  // Can you full swipe the leading side
    let allowsFullSwipeLeading: Bool
  // Buttons at the trailing (right-hand) side
    let trailing: [SwipeActionButton]
  // Can you full swipe the trailing side
    let allowsFullSwipeTrailing: Bool
    
    private let totalLeadingWidth: CGFloat!
    private let totalTrailingWidth: CGFloat!
    
    @State private var offset: CGFloat = 0
    @State private var prevOffset: CGFloat = 0
    
    init(leading: [SwipeActionButton] = [],
         allowsFullSwipeLeading: Bool = false,
         trailing: [SwipeActionButton] = [],
         allowsFullSwipeTrailing: Bool = false) {
        self.leading = leading
        self.allowsFullSwipeLeading = allowsFullSwipeLeading && !leading.isEmpty
        self.trailing = trailing
        self.allowsFullSwipeTrailing = allowsFullSwipeTrailing && !trailing.isEmpty
        //Trustar back to origin offset
        totalLeadingWidth = 0 //SwipeActionButton.width * CGFloat(leading.count)
        totalTrailingWidth = 0 //SwipeActionButton.width * CGFloat(trailing.count)
    }
    
    func body(content: Content) -> some View {
      // Use a GeometryReader to get the size of the view on which we're adding
      // the custom swipe actions.
        GeometryReader { geo in
            // Place leading buttons, the wrapped content and trailing buttons
            // in an HStack with no spacing.
            HStack(spacing: 0) {
              
                // If any swiping on the left-hand side has occurred, reveal
                // leading buttons. This also resolves button flickering.
                if offset > 0 {
                    // If the user has swiped enough for it to qualify as a full swipe,
                    // render just the first button across the entire swipe length.
                    if fullSwipeEnabled(edge: .leading, width: geo.size.width) {
                        button(for: leading.first)
                            .frame(width: offset, height: geo.size.height)
                    } else {
                      // If we aren't in a full swipe, render all buttons with widths
                      // proportional to the swipe length.
                        ForEach(leading) { actionView in
                            button(for: actionView)
                                .frame(width: individualButtonWidth(edge: .leading),
                                       height: geo.size.height)
                        }
                    }
                }
              
              // This is the list row itself
                content
                    // Add horizontal padding as we removed it to allow the
                    // swipe buttons to occupy full row height.
                    .padding(.horizontal, 16)
                    .frame(width: geo.size.width, height: geo.size.height, alignment: .leading)
                    .offset(x: (offset > 0) ? 0 : offset)
                
                // If any swiping on the right-hand side has occurred, reveal
                // trailing buttons. This also resolves button flickering.
                if offset < 0 {
                    Group {
                      // If the user has swiped enough for it to qualify as a full swipe,
                      // render just the last button across the entire swipe length.
                        if fullSwipeEnabled(edge: .trailing, width: geo.size.width) {
                            button(for: trailing.last)
                                .frame(width: -offset, height: geo.size.height)
                        } else {
                           // If we aren't in a full swipe, render all buttons with widths
                           // proportional to the swipe length.
                            ForEach(trailing) { actionView in
                                button(for: actionView)
                                    .frame(width: individualButtonWidth(edge: .trailing),
                                           height: geo.size.height)
                            }
                        }
                    }
                    // The leading buttons need to move to the left as the swipe progresses.
                    .offset(x: offset)
                }
            }
            // animate the view as `offset` changes
            .animation(.spring(), value: offset)
            // allows the DragGesture to work even if there are now interactable
            // views in the row
            .contentShape(Rectangle())
            // The DragGesture distates the swipe. The minimumDistance is there to
            // prevent the gesture from interfering with List vertical scrolling.
            .gesture(DragGesture(minimumDistance: 20,
                                 coordinateSpace: .local)
                        .onChanged { gesture in
                                    // Compute the total swipe based on the gesture values.
                                    var total = gesture.translation.width + prevOffset
                                    if !allowsFullSwipeLeading {
                                        total = min(total, totalLeadingWidth)
                                    }
                                    if !allowsFullSwipeTrailing {
                                        total = max(total, -totalLeadingWidth)
                                    }
                                    offset = total
                                   }
                        .onEnded { _ in
                                    // Adjust the offset based on if the user has swiped enough to reveal
                                    // all the buttons or not. Also handles full swipe logic.
                                    if offset > SwipeActionView.minSwipeableWidth && !leading.isEmpty {
                                        if !checkAndHandleFullSwipe(for: leading, edge: .leading, width: geo.size.width) {
                                            offset = totalLeadingWidth
                                        }
                                    } else if offset < -SwipeActionView.minSwipeableWidth && !trailing.isEmpty {
                                        if !checkAndHandleFullSwipe(for: trailing, edge: .trailing, width: -geo.size.width) {
                                            offset = -totalTrailingWidth
                                        }
                                    } else {
                                        offset = 0
                                    }
                                    prevOffset = offset
                                }
                   )
        }
        // Remove internal row padding to allow the buttons to occupy full row height
        .listRowInsets(EdgeInsets())
    }
    
    // Checks if full swipe is supported and currently active for the given edge.
    // The current threshold is at half of the row width.
    private func fullSwipeEnabled(edge: Edge, width: CGFloat) -> Bool {
        let threshold = abs(width) / 2
        switch (edge) {
        case .leading:
            return allowsFullSwipeLeading && offset > threshold
        case .trailing:
            return allowsFullSwipeTrailing && -offset > threshold
        }
    }
    
    // Creates the view for each SwipeActionButton. Also assigns it
    // a tap gesture to handle the click and reset the offset.
    private func button(for button: SwipeActionButton?) -> some View {
        button?
            .onTapGesture {
                button?.action()
                offset = 0
                prevOffset = 0
            }
    }
    
   // Calculates width for each button, proportional to the swipe.
    private func individualButtonWidth(edge: Edge) -> CGFloat {
        switch edge {
        case .leading:
            return (offset > 0) ? (offset / CGFloat(leading.count)) : 0
        case .trailing:
            return (offset < 0) ? (abs(offset) / CGFloat(trailing.count)) : 0
        }
    }
    
  // Checks if the view is in full swipe. If so, trigger the action on the
  // correct button (left- or right-most one), make it full the entire row
  // and schedule everything to be reset after a while.
    private func checkAndHandleFullSwipe(for collection: [SwipeActionButton],
                                         edge: Edge,
                                         width: CGFloat) -> Bool {
        if fullSwipeEnabled(edge: edge, width: width) {
            offset = width * CGFloat(collection.count) * 1.2
            ((edge == .leading) ? collection.first : collection.last)?.action()
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                offset = 0
                prevOffset = 0
            }
            return true
        } else {
            return false
        }
    }
    
    private enum Edge {
        case leading, trailing
    }
}

extension View {
    func swipeActions(leading: [SwipeActionButton] = [],
                      allowsFullSwipeLeading: Bool = false,
                      trailing: [SwipeActionButton] = [],
                      allowsFullSwipeTrailing: Bool = false) -> some View {
        modifier(SwipeActionView(leading: leading,
                                 allowsFullSwipeLeading: allowsFullSwipeLeading,
                                 trailing: trailing,
                                 allowsFullSwipeTrailing: allowsFullSwipeTrailing))
    }
}

struct SwipeActionsTest: View {
      var body: some View {
        NavigationView {
            List(1..<20) {
              Text("List view item at row \($0)")
                .frame(alignment: .leading)
                .swipeActions(leading: [
                  SwipeActionButton(text: Text("Text"), action: {
                    print("Text")
                  }),
                  SwipeActionButton(icon: Image(systemName: "flag"), action: {
                  print("Flag")
                  }, tint: .green)
                ],
                allowsFullSwipeLeading: true,
                trailing: [
                  SwipeActionButton(text: Text("Read"),
                                    icon: Image(systemName: "envelope.open"),
                                    action: {
                    print("Read")
                  }, tint: .blue),
                  SwipeActionButton(icon: Image(systemName: "trash"), action: {
                    print("Trash")
                  }, tint: .red)
                ],
                allowsFullSwipeTrailing: true)
            }
        }
      }
}

struct SwipeActionsTest_Previews: PreviewProvider {
    static var previews: some View {
        SwipeActionsTest()
    }
}

/// Mimics the delete functionality in a list since a LazyVStack in iOS 14 does not support that.
/// Wraps a View with a gesture that will show a delete button when swiped vertically. deleteAction is invoked on button tap.
/// Alternatively, if the user swipes > 50% of the screen, deleteAction will also be invoked.
/// Example usage: .modifier(SwipeToDeleteRow(deleteAction: { print("delete") }))
/*struct SwipeToDeleteRowView: ViewModifier {
    init(deleteAction: @escaping () -> Void) {
        self.deleteAction = deleteAction
    }
    // action to perform on delete
    var deleteAction: () -> Void
    // width of the delete button. height == height of content
    private let width : CGFloat = 50
    // offset of content
    @State private var offset : CGFloat = 0
    // true if the delete button will stay visible when the user stops dragging
    @State private var isSwiped : Bool = false

    public func body(content: Content) -> some View {
        ZStack(alignment: .trailing) {
            content
            
            // This is the "delete" button. You could put something else here as long as you use
            // contentShape, onTapGesture, and offset extensions
            /*HStack {
                Image(systemName: "trash")
                    .resizable()
                    .frame(width: 18, height: 24, alignment: .center)
                    .foregroundColor(Color.white)
            }
            .frame(minWidth: width, maxWidth: width, minHeight: 0, maxHeight: .infinity, alignment: .center)
            .background(Color.red)
            .contentShape(Rectangle())
            .onTapGesture(perform: deleteAction)
            .offset(x: width)*/
        }
        .offset(x: offset)
        .contentShape(Rectangle())
        .gesture(DragGesture().onChanged(onChanged(value:)).onEnded(onEnd(value:)))
    }
    
    func onChanged(value: DragGesture.Value){
        if value.translation.width < 0 {
            if isSwiped {
                // show the full width of the button when "swiped"
                offset = value.translation.width - width
            } else {
                // offset the content to match the drag translation
                offset = value.translation.width
            }
        }
    }
    
    func onEnd(value: DragGesture.Value){
        withAnimation(.easeOut) {
            if value.translation.width < 0 {
                if -value.translation.width > UIScreen.main.bounds.width / 2 {
                    // user dragged > 50% of the screen delete
                    deleteAction()
                }
                else if -offset > width {
                    // user dragged enough to show the button, show it when they stop dragging
                    isSwiped = true
                    offset = -width
                }
                else {
                    // user did not drag enough, pop the button off the screen
                    isSwiped = false
                    offset = 0
                }
            }
            else{
                // user swiped right, pop the button off the screen
                isSwiped = false
                offset = 0
            }
        }
    }
}

extension View {
  func SwipeToDeleteRow(deleteAction: @escaping () -> Void) -> some View {
    modifier(SwipeToDeleteRowView(deleteAction: deleteAction))
  }
}*/
