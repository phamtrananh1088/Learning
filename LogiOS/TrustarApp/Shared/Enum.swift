//
//  Enum.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/20.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

enum TypeColumn {
    case integer, text, real, numeric, blob, json
    
    func text() -> String {
        switch self {
        case .integer:
            return "INTEGER"
        case .text:
            return "TEXT"
        case .real:
            return "REAL"
        case .numeric:
            return "NUMERIC"
        case .blob:
            return "BLOB"
        case .json:
            return "JSON"
        }
    }
}

enum LoginFlag {
    case preFlg, loginFlg, syncFlg, noticeFlg, finishFLg, doneFlg, cancelFlg
}

enum TypeScreen {
    case FullView
    case TabView
    case None
}

public enum ScreenDisplay {
    case Login, DashBoard, Notice, Weather, Setting, Deliver, Operation, Refuel, Chat
    case None
    func typeScreen() -> TypeScreen {
        switch self {
        case .Login, .Notice, .Weather, .Setting, .Chat:
            return TypeScreen.FullView
        case .DashBoard, .Deliver, .Operation, .Refuel:
            return TypeScreen.TabView
        case .None:
            return TypeScreen.None
        }
    }
}

enum TabDisplay {
    case None
}

enum WeatherEnum: Int, CaseIterable {
    case Hare = 1, Kumori, Ame, Yuki, HareKumori, KumoriHare
    
    func getWeather() -> Image {
        switch self {
        case .Hare:
            return Image("hare_48dp")
        case .Kumori:
            return Image("kumori_48dp")
        case .Ame:
            return Image("ame_48dp")
        case .Yuki:
            return Image("yuki_48dp")
        case .HareKumori:
            return Image("hare_kumori_48dp")
        case .KumoriHare:
            return Image("kumori_hare_48dp")
        }
    }
    
    func getTextWeather() -> String {
        switch self {
        case .Hare:
            return "晴れ"
        case .Kumori:
            return "曇り"
        case .Ame:
            return "雨"
        case .Yuki:
            return "雪"
        case .HareKumori:
            return "晴れ時々曇り"
        case .KumoriHare:
            return "曇り時々晴れ"
        }
    }
}

enum HomeEnum {
    case dashboard, deliver, operation, operationWithBadge, refuel, settings, chat
    
    func getImage() -> Image {
        switch self {
        case .dashboard:
            return Image("dashboard_24dp")
        case .deliver:
            return Image("document_24dp")
        case .operation:
            return Image("toolbox_24dp")
        case .operationWithBadge:
            return Image("toolbox_badge_24dp")
        case .refuel:
            return Image("fill_tank_24dp")
        case .settings:
            return Image("settings_24dp")
        case .chat:
            return Image("round_chat_24")
        }
    }
}

enum BinStatusEnum: String {
    case Ready = "0"
    case Working = "1"
    case Finished = "2"
    
    func started() -> Bool {
        return self == .Working || self == .Finished
    }
}

enum WorkStatusEnum: String {
    case Ready = "0"
    case Moving = "1"
    case Working = "2"
    case Finished = "3"
    
    func isWorkingOrMoving() -> Bool {
        return self == .Moving || self == .Working
    }
}

enum BinDetailEnum: Int {
    case Ready = 0
    case Moving = 1
    case Working = 2
    case Finished = 3
}

enum LoadingState {
    case Loading, None
}

enum BroadcastEnum: String {
    case Notice, Bin, Authorized, Location, Incidental, Collection, DeliveryChart
    case Fuel, Active, Chat
    case RemoveObserver
}

enum MessageTypeEnum: Int {
    case TYPE_TEXT
    case TYPE_IMG
    case TYPE_AUDIO
    case TYPE_VIDEO
    case TYPE_FILE
}

enum MessagePendingStateEnum: Int {
    case STATE_NORMAL = 0
    case STATE_SENDING = 1
    case STATE_ERR = 2
    case STATE_SENT = 3
}

enum MessagePendingTypeEnum: Int {
    case TYPE_TEXT = 0
    case TYPE_IMG_SEND = 1
    case TYPE_IMG = 2
    case TYPE_AUDIO_SEND = 3
    case TYPE_AUDIO = 4
    case TYPE_VIDEO_SEND = 5
    case TYPE_VIDEO = 6
    case TYPE_FILE_SEND = 7
    case TYPE_FILE = 8
}

enum Result<T: Any> {
    case Loading
    case Value(value: T?)
    case Error(error: NetworkError)
}

enum Event<T: Any> {
    case Value(value: T?)
    case Error(error: NetworkError)
}

enum PayLoadEnum: Int {
    case PAYLOAD_CONTENT = 0b1
    case PAYLOAD_STATUS = 0b10
    case PAYLOAD_DATE = 0b100
    
    func hasPayload(payload: Int) -> Bool {
        return self.rawValue & payload == self.rawValue
    }
}

enum FirebaseMessageEnum: String {
    case title = "title"
    case talkRoomId = "talkRoomId"
    case body = "body"
}

enum CameraModeEnum {
    case Photo
    case VideoNonRecording
    case VideoRecording
}

enum CameraFlashModeEnum {
    case On
    case Off
    case Auto
    case NotAvailable
}

enum RecordingModeEnum {
    case Recording
    case NonRecording
    case FinishedRecording
}

enum ScaleLevelEnum {
    case defLvl
    case firstLvl
    case secondLvl
}

enum ActionChatControlEnum {
    case Audio
    case Image
    case Camera
    case File
    case Send
}

enum BooleanEnum {
    case True
    case False
    case Nil
}

enum LoadingStateEnum {
    case Ok
    case Error
    case Loading
}

enum LoadTypeEnum {
    case REFRESH(key: ChatVM.KEY?)
    case PREPEND(key: ChatVM.KEY)
    case APPEND(key: ChatVM.KEY)
}

enum MenuItemEnum {
    case Copy
    case Delete
    case Detail
    case Share
}

class StartUnscheduledBinState {
    var detectHasWorking: Bool
    init(detectHasWorking: Bool) {
        self.detectHasWorking = detectHasWorking
    }
    
    class TokenLoading: StartUnscheduledBinState {
        init() {
            super.init(detectHasWorking: true)
        }
    }
    class GetTokenError: StartUnscheduledBinState {
        var err: Error
        init(err: Error) {
            self.err = err
            super.init(detectHasWorking: true)
        }
    }
    class WorkingBinExists: StartUnscheduledBinState {
        init() {
            super.init(detectHasWorking: true)
        }
    }
    class Ready: StartUnscheduledBinState {
        var token: String
        init(token: String) {
            self.token = token
            super.init(detectHasWorking: true)
        }
    }
    class Sending: StartUnscheduledBinState {
        init() {
            super.init(detectHasWorking: false)
        }
    }
    class AddFailed: StartUnscheduledBinState {
        init() {
            super.init(detectHasWorking: false)
        }
    }
    class Added: StartUnscheduledBinState {
        var allocationNo: String
        init(allocationNo: String) {
            self.allocationNo = allocationNo
            super.init(detectHasWorking: false)
        }
    }
    class AddedButReloadFailed: StartUnscheduledBinState {
        var allocationNo: String
        init(allocationNo: String) {
            self.allocationNo = allocationNo
            super.init(detectHasWorking: false)
        }
    }
}
