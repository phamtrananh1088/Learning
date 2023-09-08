import UIKit
import FirebaseCore
import FirebaseMessaging
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    let gcmMessageIDKey = "gcm.message_id"
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        FirebaseApp.configure()

        // [START set_messaging_delegate]
        Messaging.messaging().delegate = self
        // [END set_messaging_delegate]
        // Register for remote notifications. This shows a permission dialog on first run, to
        // show the dialog at a more appropriate time move this registration accordingly.
        // [START register_for_notifications]
        if #available(iOS 10.0, *) {
          // For iOS 10 display notification (sent via APNS)
          UNUserNotificationCenter.current().delegate = self

          let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
          UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: { _, _ in }
          )
        } else {
          let settings: UIUserNotificationSettings =
            UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
          application.registerUserNotificationSettings(settings)
        }

        application.registerForRemoteNotifications()
        
        print(NSHomeDirectory())

        // [END register_for_notifications]
    
        return true
    }
    
    func application(_ application: UIApplication,
                       didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
        // If you are receiving a notification message while your app is in the background,
        // this callback will not be fired till the user taps on the notification launching the application.
        // TODO: Handle data of notification
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
          print("[1] Message ID: \(messageID)")
        }

        // Print full message.
        print(userInfo)
    }
    
    // [START receive_message]
    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable: Any]) async
      -> UIBackgroundFetchResult {
          // If you are receiving a notification message while your app is in the background,
          // this callback will not be fired till the user taps on the notification launching the application.
          // TODO: Handle data of notification
          // With swizzling disabled you must let Messaging know about the message, for Analytics
          // Messaging.messaging().appDidReceiveMessage(userInfo)
          // Print message ID.
          if let messageID = userInfo[gcmMessageIDKey] {
            print("[2] Message ID: \(messageID)")
          }

          // Print full message.
          print("New message: [2] ---------")
          print("title: \(String(describing: userInfo[FirebaseMessageEnum.title.rawValue]))")
          print("body: \(String(describing: userInfo[FirebaseMessageEnum.body.rawValue]))")
          print("talkRoomId: \(String(describing: userInfo[FirebaseMessageEnum.talkRoomId.rawValue]))")
          
          if userInfo[FirebaseMessageEnum.title.rawValue] == nil {
              print(userInfo)
          }
          print("-------------")
          
          NotificationHelper.Shared.sendNotification(NotificationConfig(userInfo: userInfo))
          Helper.Shared.sendBroadcastNotification(.Chat)

          return UIBackgroundFetchResult.newData
    }

    // [END receive_message]
    func application(_ application: UIApplication,
                     didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("Unable to register for remote notifications: \(error.localizedDescription)")
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }
    
    var orientationLock = UIInterfaceOrientationMask.allButUpsideDown

    func application(_ application: UIApplication, supportedInterfaceOrientationsFor window: UIWindow?) -> UIInterfaceOrientationMask {
        return self.orientationLock
    }
}

extension AppDelegate: UNUserNotificationCenterDelegate {
  // Receive displayed notifications for iOS 10 devices.
  func userNotificationCenter(_ center: UNUserNotificationCenter,
                              willPresent notification: UNNotification) async
    -> UNNotificationPresentationOptions {
        let userInfo = notification.request.content.userInfo

        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // [START_EXCLUDE]
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
          print("[3] Message ID: \(messageID)")
        }
        // [END_EXCLUDE]

        // Print full message.
        print("New message: [3] ---------")
        print("title: \(String(describing: userInfo[FirebaseMessageEnum.title.rawValue]))")
        print("body: \(String(describing: userInfo[FirebaseMessageEnum.body.rawValue]))")
        print("talkRoomId: \(String(describing: userInfo[FirebaseMessageEnum.talkRoomId.rawValue]))")
        
        if userInfo[FirebaseMessageEnum.title.rawValue] == nil {
            print(userInfo)
        }
        print("-------------")

        NotificationHelper.Shared.sendNotification(NotificationConfig(userInfo: userInfo))
        Helper.Shared.sendBroadcastNotification(.Chat)
        
        // Change this to your preferred presentation option
        return [[.alert, .sound]]
  }

  func userNotificationCenter(_ center: UNUserNotificationCenter,
                              didReceive response: UNNotificationResponse) async {
      let userInfo = response.notification.request.content.userInfo

      // [START_EXCLUDE]
      // Print message ID.
      if let messageID = userInfo[gcmMessageIDKey] {
        print("[4] Message ID: \(messageID)")
      }
      // [END_EXCLUDE]
      // With swizzling disabled you must let Messaging know about the message, for Analytics
      // Messaging.messaging().appDidReceiveMessage(userInfo)
      // Print full message.
      print(userInfo)
  }
}

// [END ios_10_message_handling]

extension AppDelegate: MessagingDelegate {
  // [START refresh_token]
  func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
      print("Firebase: Token \(String(describing: fcmToken))")
      if (Current.Shared.loggedIn()
          && Current.Shared.updateFirebaseToken().invalid == false
          && fcmToken != nil) {
          print("---------")
          print("Firebase: updateToken \(String(describing: fcmToken))")
          Current.Shared.updateFirebaseToken().updateToken()
          
      }
    
    // TODO: If necessary send token to application server.
    // Note: This callback is fired at each app startup and whenever a new token is generated.
  }

  // [END refresh_token]
}
