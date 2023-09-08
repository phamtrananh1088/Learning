//
//  PopupViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/18.
//

import Foundation
import Combine
import SwiftUI

class PopupViewModel: ObservableObject {
    
    var isShowTitle: Bool
    var title: String
    var message: String
    var isShowLeftBtn: Bool
    var leftBtnText: String
    var isShowRightBtn: Bool
    var rightBtnText: String
    
    var leftBtnClick: (() -> ())?
    var rightBtnClick: (() -> ())?
    
    var isShowCheckBox: Bool = false
    var rightBtnClickWithCheckBox: ((Bool) -> ())?
    var autoClose: Bool
    @Published var isChecked: Bool = false
    
    init(isShowTitle: Bool,
         title: String,
         message: String,
         isShowLeftBtn: Bool,
         leftBtnText: String,
         isShowRightBtn: Bool,
         rightBtnText: String,
         leftBtnClick: (() -> ())? = nil,
         rightBtnClick: (() -> ())? = nil,
         isShowCheckBox: Bool = false,
         rightBtnClickWithCheckBox: ((Bool) -> ())? = nil,
         autoClose: Bool = true
    ) {
        self.isShowTitle = isShowTitle
        self.title = title
        self.message = message
        self.isShowLeftBtn = isShowLeftBtn
        self.leftBtnText = leftBtnText
        self.isShowRightBtn = isShowRightBtn
        self.rightBtnText = rightBtnText
        self.leftBtnClick = leftBtnClick
        self.rightBtnClick = rightBtnClick
        self.isShowCheckBox = isShowCheckBox
        self.rightBtnClickWithCheckBox = rightBtnClickWithCheckBox
        self.autoClose = autoClose
    }
}


