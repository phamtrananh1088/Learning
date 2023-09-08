//
//  AbstractMessageUIViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class AbstractMessageUIViewModel: BaseViewModel, Equatable, Hashable {
    
    static func == (lhs: AbstractMessageUIViewModel, rhs: AbstractMessageUIViewModel) -> Bool {
        return lhs.uuid == rhs.uuid
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(uuid)
    }
    
    var uuid: String
    var bound: MessageItemChat
    var username: String
    var isShowUserName: Bool
    @Published var sentTimeTextView: String
    @Published var statusTextView: String
    var isShowAvatarAlignment: Bool
    var avatar: String
    var isShowAvatar: Bool
    var longClickMenu: [MenuItemEnum] = []
    
    init(bound: MessageItemChat) {
        let selfField = bound.selfField
        self.uuid = UUID().uuidString
        
        self.bound = bound
        self.sentTimeTextView = bound.dateStr()
        self.statusTextView = bound.status.orEmpty()
        
        let gone = selfField || bound.hasPrev
        if bound is MessageItemChat.Sent && !gone {
            self.isShowUserName = true
            self.username = (bound as! MessageItemChat.Sent).username.orEmpty()
            self.avatar = (bound as! MessageItemChat.Sent).avatar?.uri.path ?? Resources.ic_avatar
        } else {
            self.isShowUserName = false
            self.username = ""
            self.avatar = ""
        }
        
        self.isShowAvatar = !gone
        
        self.isShowAvatarAlignment = !selfField
        
        super.init()
        
        setLongClickMenu(binder: bound)
    }
    
    func update(bound: MessageItemChat) {
        self.bound = bound
        DispatchQueue.main.async {
            self.sentTimeTextView = bound.dateStr()
            self.statusTextView = bound.status.orEmpty()
        }
    }
    
    func setLongClickMenu(binder: MessageItemChat) {
        longClickMenu = []
        if binder is TextProtocol {
            longClickMenu.append(.Copy)
        }
        
        if (binder is MessageItemChat.Sent && binder.selfField) ||
            (binder is MessageItemChat.Pending
             && ((binder as! MessageItemChat.Pending).pending.status == MessagePendingStateEnum.STATE_ERR.rawValue
                 || (binder as! MessageItemChat.Pending).pending.status == MessagePendingStateEnum.STATE_NORMAL.rawValue)) {
            longClickMenu.append(.Delete)
        }
        
        longClickMenu.append(.Detail)
        
        if let binder = binder as? DlProtocol {
            if let att = binder.attachmentExt() {
                if att
                    .contentUri()
                    .canRead() {
                    longClickMenu.append(.Share)
                }
            }
        }
    }
}
