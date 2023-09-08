//
//  DoubleDateAndTimeView.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 21/02/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

extension View {
    func eraseToAnyView() -> AnyView {
        AnyView(self)
    }
}
// 1. PreferenceKey for a subview to notify superview of its size
struct SizePreferenceKey: PreferenceKey {
    typealias Value = CGSize
    static var defaultValue: CGSize = .zero
    static func reduce(value: inout CGSize, nextValue: () -> CGSize) {
        value = nextValue()
    }
}
// 2. A view that will serve as the background of one of our text elements (the subview)
struct BackgroundGeometryReader: View {
    var body: some View {
        GeometryReader { geometry in
            return Color
                    .clear
                    .preference(key: SizePreferenceKey.self, value: geometry.size)
        }
    }
}
// 3. Define a helper ViewModifier to attach to the subview that encapsulates the PreferenceKey
struct SizeAwareViewModifier: ViewModifier {

    @Binding private var viewSize: CGSize

    init(viewSize: Binding<CGSize>) {
        self._viewSize = viewSize
    }

    func body(content: Content) -> some View {
        content
            .background(BackgroundGeometryReader())
            .onPreferenceChange(SizePreferenceKey.self, perform: { if self.viewSize != $0 { self.viewSize = $0 }})
    }
}
// 4. Segment container and Items
struct SegmentedPicker: View {
    private static let ActiveSegmentColor: Color = Color(Resources.colorPrimary)
    private static let BackgroundColor: Color = Color(Resources.colorPrimary) //Color(.secondarySystemBackground)
    private static let ShadowColor: Color = Color.black.opacity(0.2)
    private static let TextColor: Color = Color(Resources.colorAccent)
    private static let SelectedTextColor: Color = Color(Resources.colorPrimary)

    private static let TextFont: Font = .system(size: 12)
    
    private static let SegmentCornerRadius: CGFloat = 12
    private static let ShadowRadius: CGFloat = 4
    private static let SegmentXPadding: CGFloat = 16
    private static let SegmentYPadding: CGFloat = 16
    private static let PickerPadding: CGFloat = 4
    
    private static let AnimationDuration: Double = 0.1
    
    // Stores the size of a segment, used to create the active segment rect
    @State private var segmentSize: CGSize = .zero
    
    // Rounded rectangle to denote active segment
    private var activeSegmentView: AnyView {
        // Don't show the active segment until we have initialized the view
        // This is required for `.animation()` to display properly, otherwise the animation will fire on init
        let isInitialized: Bool = segmentSize != .zero
        if !isInitialized { return EmptyView().eraseToAnyView() }
        return
            Rectangle()
            .foregroundColor(SegmentedPicker.ActiveSegmentColor)
            .shadow(color: SegmentedPicker.ShadowColor, radius: SegmentedPicker.ShadowRadius)
            .frame(width: self.segmentSize.width, height: self.segmentSize.height)
            .offset(x: self.computeActiveSegmentHorizontalOffset(), y: 0)
            .animation(Animation.linear(duration: SegmentedPicker.AnimationDuration))
            .eraseToAnyView()
            /*RoundedRectangle(cornerRadius: SegmentedPicker.SegmentCornerRadius)
                .foregroundColor(SegmentedPicker.ActiveSegmentColor)
                .shadow(color: SegmentedPicker.ShadowColor, radius: SegmentedPicker.ShadowRadius)
                .frame(width: self.segmentSize.width, height: self.segmentSize.height)
                .offset(x: self.computeActiveSegmentHorizontalOffset(), y: 0)
                .animation(Animation.linear(duration: SegmentedPicker.AnimationDuration))
                .eraseToAnyView()*/
    }
    
    @Binding private var selection: Int
    private let items: [String]
    
    init(items: [String], selection: Binding<Int>) {
        self._selection = selection
        self.items = items
    }
    
    var body: some View {
        // Align the ZStack to the leading edge to make calculating offset on activeSegmentView easier
        ZStack(alignment: .leading) {
            // activeSegmentView indicates the current selection
            self.activeSegmentView
            //HStack {
            HStack(alignment: .center, spacing: 0) {
                ForEach(0..<self.items.count, id: \.self) { index in
                    self.getSegmentView(for: index)
                }
            }
        }
        //.padding(SegmentedPicker.PickerPadding)
        //.background(SegmentedPicker.BackgroundColor)
        //.clipShape(RoundedRectangle(cornerRadius: SegmentedPicker.SegmentCornerRadius))
    }

    // Helper method to compute the offset based on the selected index
    private func computeActiveSegmentHorizontalOffset() -> CGFloat {
        //CGFloat(self.selection) * (self.segmentSize.width + SegmentedPicker.SegmentXPadding / 2)
        CGFloat(self.selection) * (self.segmentSize.width)
    }

    // Gets text view for the segment
    private func getSegmentView(for index: Int) -> some View {
        guard index < self.items.count else {
            return EmptyView().eraseToAnyView()
        }
        let isSelected = self.selection == index
        return
            Text(self.items[index])
                // Dark test for selected segment
                .foregroundColor(isSelected ? SegmentedPicker.SelectedTextColor: SegmentedPicker.TextColor)
                .lineLimit(1)
                .padding(.vertical, SegmentedPicker.SegmentYPadding)
                .padding(.horizontal, SegmentedPicker.SegmentXPadding)
                .frame(minWidth: 0, maxWidth: .infinity)
                .contentShape(Rectangle())
                .background(isSelected ? .white : SegmentedPicker.BackgroundColor)
                // Watch for the size of the
                .modifier(SizeAwareViewModifier(viewSize: self.$segmentSize))
                .onTapGesture { self.onItemTap(index: index) }
                .eraseToAnyView()
    }

    // On tap to change the selection
    private func onItemTap(index: Int) {
        guard index < self.items.count else {
            return
        }
        self.selection = index
    }
    
}

struct DoubleDateAndTimeView: View {
    private let items: [String] = [Resources.pick_start_time, Resources.pick_end_time]
    @State var selection: Int = 0
    @Binding var beginDateTime: Date
    @Binding var endDateTime: Date
    var callbackAction: () -> Void
        
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            SegmentedPicker(items: self.items, selection: self.$selection)
                //.padding(.horizontal)
            switch selection {
                case 0:
                    DateAndTimePickerView(dateValue: $beginDateTime, sideOfDateType: $selection, callbackAction: callbackAction)
                    .background(Color(Resources.colorAccent))
                case 1:
                    DateAndTimePickerView(dateValue: $endDateTime, sideOfDateType: $selection, callbackAction: callbackAction)
                    .background(Color(Resources.colorAccent))
                default:
                    EmptyView()
            }
        }
    }
}

/*enum SideOfDatTime: String, CaseIterable {
    case beginDT = "BeginDateTime"
    case endDT = "EndDateTime"
}

struct ChosenDateTimeView: View {
    @Binding var selectedSide: SideOfDatTime
    @Binding var beginDateTime: Date
    @Binding var endDateTime: Date
    
    var body: some View {
        switch selectedSide {
        case .beginDT:
            DateAndTimePickerView(dateValue: $beginDateTime, sideOfDateType: $selectedSide)
        case .endDT:
            DateAndTimePickerView(dateValue: $endDateTime, sideOfDateType: $selectedSide)
        }
    }
}

struct DoubleDateAndTimeView: View {
    
    init() {
        UISegmentedControl.appearance().selectedSegmentTintColor = Resources.colorPrimary
        UISegmentedControl.appearance().setTitleTextAttributes([.foregroundColor: UIColor.black], for: .selected)
        
        UISegmentedControl.appearance().backgroundColor = .white
        
    }
    
    @State private var selectedSide: SideOfDatTime = .beginDT
    @State var beginDateTime: Date = Date()
    @State var endDateTime: Date = Date()
        
    var body: some View {
        NavigationView {
            VStack {
                Picker("", selection: $selectedSide) {
                    ForEach(SideOfDatTime.allCases, id: \.self) {
                        //Text($0.rawValue).hidden()
                        if $0 == .beginDT {
                            Text(Resources.pick_start_time)
                                .background(Rectangle())
                        } else {
                            Text(Resources.pick_end_time)
                        }
                    }
                }
                .pickerStyle(SegmentedPickerStyle())
                .padding(.horizontal)
                
                //Spacer()
                ChosenDateTimeView(selectedSide: $selectedSide, beginDateTime: $beginDateTime, endDateTime: $endDateTime)
                Spacer()
            }
        }
    }
}*/
