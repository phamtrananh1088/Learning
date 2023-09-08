//
//  ItemCollectViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 16/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class ItemCollectViewModel: ObservableObject {
    var content: String
    var exp: String
    @Published var act: String
    var onChangeValue: (String) -> ()
    var onEditMode: () -> ()
    
    init(_ bound: CollectViewModel.Row,
         onChangeValue: @escaping (String) -> (),
         onEditMode: @escaping () -> ()) {
        self.content = bound.name
        self.exp = bound.expectedQuantity.roundToStringDecimal1()
        self.act = bound.actualQuantity.roundToStringDecimal1()
        self.onChangeValue = onChangeValue
        self.onEditMode = onEditMode
    }
    
    enum action {
        case decrement, increment
    }
    
    private var step = 0
    private let max = 999.9
    private var delay = 300
    private var onLongClickRun = false
    private func reset(action: action) {
        switch action {
        case .decrement:
            step = -1
        case .increment:
            step = 1
        }
    }
    
    func onClick(action: action) {
        reset(action: action)
        setValue()
    }
    
    func onLongClick(action: action) {
        reset(action: action)
        delay = 300
        onLongClickRun = true
        run()
    }
    
    func onStopLongClick() {
        onLongClickRun = false
        onChangeValue(act)
    }
    
    private func run() {
        if onLongClickRun {
            DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(delay)) { [self] in
                setValue()
                delay = Swift.max(50, delay - 50)
                run()
            }
        }
    }
    
    private func setValue() {
        let i = Double(act) ?? 0.0
        let value = i + Double(step)
        let v1 = value > max ? max : value
        act = v1 > 0 ? v1.roundToStringDecimal1() : "0"
    }
}
