//
//  BinMeterInputViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/22.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class BinMeterInputViewModel: ObservableObject {
    var title1: String = String()
    var title2: String = String()

    var cancelText: String = Resources.cancel
    var cancelClick: (() -> ())?
    var confirmText: String = Resources.confirm_input
    var confirmClick: ((String) -> ())?

    @Published var input: String = Resources.strEmpty
    
    init() {}
    
    init(tilte1: String, title2: String, cancelText: String?, cancelClick: @escaping () -> (), confirmText: String?, confirmClick: @escaping (String) -> (), input: String) {
        self.title1 = tilte1
        self.title2 = title2
        if cancelText != nil {
            self.cancelText = cancelText!
        }
        
        self.cancelClick = cancelClick
        
        if confirmText != nil {
            self.confirmText = confirmText!
        }
        
        self.confirmClick = confirmClick
        
        self.input = input
    }
}
