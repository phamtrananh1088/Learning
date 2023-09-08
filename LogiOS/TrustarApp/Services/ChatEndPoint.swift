//
//  ChatEndPoint.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum ChatEndPoint {
   
    case getData
    case getTalkRooms
    case getHistories
    case getAllUsers
    case addRoom
    case addMember
    case sendMessage
    case getMessagePrevious
    case getMessageNext
    case getMessageItems
    case updateUserSetting
    case updateRead
    case updateDelete
    case registerFirebaseToken
    case deleteMessage
    
    func path() -> String {
        switch self {
        case .getData:
            return "Talk/GetData?isSelectAll=false"
        case .getTalkRooms:
            return "Talk/GetTalkRooms"
        case .getHistories:
            return "Talk/GetHistories"
        case .getAllUsers:
            return "Account/GetAllUsers"
        case .addRoom:
            return "Talk/AddTalkRoom"
        case .addMember:
            return "Talk/AddMember"
        case .sendMessage:
            return "Talk/SendMessage"
        case .getMessagePrevious:
            return "Talk/GetPrevious"
        case .getMessageNext:
            return "Talk/GetNext"
        case .getMessageItems:
            return "Talk/GetMessageItems"
        case .updateUserSetting:
            return "Talk/UpdateUserSetting"
        case .updateRead:
            return "Talk/UpdateIsRead"
        case .updateDelete:
            return "Talk/UpdateIsDeleted"
        case .registerFirebaseToken:
            return "Talk/RegisterFirebaseToken"
        case .deleteMessage:
            return "Talk/DeleteMessage"
        }
    }
}
