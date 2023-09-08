//
//  NotificationHelper.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/01.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import UserNotifications

class NotificationConfig {
    var Title: String
    var Text: String
    var Icon: String
    var talkRoomId: String
    
    init(Title: String, Text: String, Icon: String, talkRoomId: String) {
        self.Title = Title
        self.Text = Text
        self.Icon = Icon
        self.talkRoomId = talkRoomId
    }
    
    init(userInfo: [AnyHashable: Any]) {
        if userInfo[FirebaseMessageEnum.title.rawValue] == nil
            || userInfo[FirebaseMessageEnum.body.rawValue] == nil
            || userInfo[FirebaseMessageEnum.talkRoomId.rawValue] == nil {
            
            self.Title = String()
            self.Text = String()
            self.Icon = String()
            self.talkRoomId = String()
        } else {
            self.Title = userInfo[FirebaseMessageEnum.title.rawValue] as! String
            self.Text = userInfo[FirebaseMessageEnum.body.rawValue] as! String
            self.Icon = String()
            self.talkRoomId = userInfo[FirebaseMessageEnum.talkRoomId.rawValue] as! String
        }
    }
    
    func isNil() -> Bool {
        if Title.isEmpty || Text.isEmpty { return true }
        
        return false
    }
}

class NotificationHelper {
    static var Shared = NotificationHelper()
    
    init() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound])  { success, error in
            if success {
            } else if let _ = error { return }
        }
    }
    
    func sendNotification(_ config: NotificationConfig) {
        
        if config.isNil() { return }
        
        let content = UNMutableNotificationContent()
        content.title = config.Title
        content.body = config.Text
        content.sound = UNNotificationSound.default
        
//        let imageName = "applelogo"
//        guard let imageURL = Bundle.main.url(forResource: imageName, withExtension: "png") else { return }
//        let attachment = try! UNNotificationAttachment(identifier: imageName, url: imageURL, options: .none)
//        content.attachments = [attachment]
            
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 1, repeats: false)
        let request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)
        
        UNUserNotificationCenter.current().add(request)
    }
}
