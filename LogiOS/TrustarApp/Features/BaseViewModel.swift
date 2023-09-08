//
//  BaseViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/18.
//

import Foundation
import SwiftUI
import Combine

class BaseViewModel {
    internal var bag = Set<AnyCancellable>()
    internal var current = Current.Shared
    
    init() {
        registerNotificationCenter()
    }

    var popupVm = PopupViewModel(isShowTitle: false, title: "", message: "", isShowLeftBtn: false, leftBtnText: "", isShowRightBtn: false, rightBtnText: "")
    @Published var isShowingAlert = false
    
    internal func showPopupView(popupVm: PopupViewModel) {
        self.popupVm = popupVm
        self.isShowingAlert = true
    }
    
    internal func registerNotificationCenter() {

    }
    
    internal func removeObserver() {
        NotificationCenter.default.removeObserver(self)
    }
}

class ModelRotation: ObservableObject {
    @Published var landscape: Bool = false
        
    init(isLandscape: Bool) {
        self.landscape = isLandscape // Initial value
        NotificationCenter.default.addObserver(self, selector: #selector(onViewWillTransition(notification:)), name: .my_onViewWillTransition, object: nil)
    }

    @objc func onViewWillTransition(notification: Notification) {
        guard let size = notification.userInfo?["size"] as? CGSize else { return }

        landscape = size.width > size.height
    }
}
