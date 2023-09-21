//
//  Current.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/17.
//

import Foundation
import Combine
import GRDB
import CoreLocation
import SwiftUI
import FirebaseMessaging

public class Current: ObservableObject {
    static var Shared = Current()
    
    var loggedUser: LoggerUser?
    
    func userInfo() -> UserInfo {
        return loggedUser?.userInfo ?? UserInfo()
    }
    
    func userId() -> String {
        return userInfo().userId
    }
    
    func loggedIn() -> Bool {
        return loggedUser?.token != nil
    }
    
    var locationHelper: LocationHelper?

    func afterLogin(loginResponse: LoginResponse) {
        self.loggedUser = LoggerUser(userInfo: UserInfo(userInfo: loginResponse.userInfo!), token: loginResponse.loginResult.token!)
        self.setLastUserLogin()
        
        // create db
        initDbAndRepository(dbName: "\(loginResponse.userInfo!.companyCd)_\(loginResponse.userInfo!.userId)")
        
        messageTask = MessageTask()
        updateFirebaseToken().newToken()
    }
    
    private func setLastUserLogin() {
        if(loggedUser != nil) {
            // Save to localData
            // UserId
            Helper.Shared.saveValue(key: .userId, value: self.loggedUser!.userInfo.userId)
            // Token
            Helper.Shared.saveValue(key: .token, value: self.loggedUser!.token)
            // CompanyId
            Helper.Shared.saveValue(key: .companyId, value: self.loggedUser!.userInfo.companyCd)
            // userName
            Helper.Shared.saveValue(key: .userName, value: self.loggedUser!.userInfo.userNm)
            // branchTel
            Helper.Shared.saveValue(key: .branchTel, value: self.loggedUser!.userInfo.branchTel)
            // UserInfo
            Helper.Shared.saveValue(key: .userInfo, value: Helper.Shared.convertObjectToJson(body: loggedUser!.userInfo)!)
        }
    }
    
    func getLastUserLogin()
    {
        // Get data from localData
        // UserId
        let userId: String? = Helper.Shared.readValue(key: .userId)
        // Token
        let token: String? = Helper.Shared.readValue(key: .token)
        // CompanyId
        let companyId: String? = Helper.Shared.readValue(key: .companyId)
        // userName
        let userName: String? = Helper.Shared.readValue(key: .userName)
        // branchTel
        let branchTel: String? = Helper.Shared.readValue(key: .branchTel)
        // UserInfo
        let userInfoJs: String? = Helper.Shared.readValue(key: .userInfo)
        
        if userInfoJs != nil {
            let userInfo: UserInfo? = Helper.Shared.convertJsonToObject(json: userInfoJs!.data(using: .utf8)!)
            if userInfo != nil {
                userInfo?.companyCd = companyId!
                userInfo?.userNm = userName!
                userInfo?.branchTel = branchTel!
                
                self.loggedUser = LoggerUser(userInfo: UserInfo(userInfo: userInfo!), token: token!)
                
                initDbAndRepository(dbName: "\(companyId!)_\(userId!)")
                messageTask = MessageTask()
                
                sync = SyncData()
                try? syncMaster(callBack: {(_,_) in })
                syncNotice(callBack: {(_,_) in })
                
                updateFirebaseToken().updateToken()
            }
        }
    }
    
    private func initDbAndRepository(dbName: String) {
        Config.Shared.userDb = try? UserDb(dbName)
        Config.Shared.resultDb = try? ResultDb(dbName)
        Config.Shared.imageDb = try? ImageDb(dbName)
        chatDb = try? ChatDb(dbName)
        
        userRepository = AioRepository()
        userDatabase = Config.Shared.userDb
        chatRoomRepository = ChatRoomRepository(api: chatApi, chatDB: chatDb!)
        chatRepository = ChatRepository(api: chatApi, chatDB: chatDb!)
        chatFileRepository = ChatFileRepository()
    }
    
    func stopInterval() {
        locationHelper?.stop()
        loggedUser?.binLocationTask.removeObserver()
        loggedUser?.binLocationTask.cancelAutoMode()
    }
    
    func logout() {
        stopInterval()
        sync?.dispose()
        sync = nil
        resetLastUserLogin()
        getLastUserLogin()
        sync?.dispose()
        sync = nil
        messageTask?.dispose()
    }
    
    func resetLastUserLogin() {
        // UserId
        Helper.Shared.removeValue(key: .userId)
        // Token
        Helper.Shared.removeValue(key: .token)
        // CompanyId
        //Helper.Shared.removeValue(key: .companyId)
        // userName
        Helper.Shared.removeValue(key: .userName)
        // branchTel
        Helper.Shared.removeValue(key: .branchTel)
        // UserInfo
        Helper.Shared.removeValue(key: .userInfo)
        
        loggedUser = nil
    }
    
    // sync
    
    private var sync: SyncData? = nil
    
    func syncMaster(callBack: @escaping (Bool, NetworkError?)->()) throws {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncMaster(callBack: callBack)
    }
        
    func syncGeo(callBack: @escaping (Bool, NetworkError?)->()) {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncGeo(callBack: callBack)
    }
    
    func syncNotice(callBack: @escaping (Bool, NetworkError?)->()) {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncNotice(callBack: callBack)
    }
    
    func syncBin(callBack: @escaping (Bool, NetworkError?)->()) {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncBin { [self](isOk, Error) in
            if !isOk {
                callBack(isOk,Error)
            } else {
                if sync != nil {
                    //sync?.syncCollection(callBack: callBack)
                    sync?.syncIncidental(callBack: callBack)
                    sync?.syncDeliveryChart(callBack: callBack)
                }
            }
        }
    }
    
    func syncIncidental(callBack: @escaping (Bool, NetworkError?)->()) {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncIncidental { [self](isOk, Error) in
            if !isOk {
                callBack(isOk, Error)
            } else {
                if sync != nil {
                    sync!.syncImgData(callBack: callBack)
                }
            }
        }
    }
    
    func syncFuel(callBack: @escaping (Bool, NetworkError?)->()) {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncFuel(callBack: callBack)
    }
    
    func syncChart(callBack: @escaping (Bool, NetworkError?)->()) {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncDeliveryChart(callBack: callBack)
    }
    
    func syncSensorCsvUpload() {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncSensorCsvUpload()
    }
    
    func syncRest(callBack: @escaping (Bool, NetworkError?)->()) {
        if loggedUser?.token == nil || loggedUser!.token.isEmpty {
            return
        }
        
        if sync == nil {
            sync = SyncData()
        }
        
        sync!.syncRest(callBack: callBack)
    }
    
    // View display
    @Published public private(set) var screen: ScreenDisplay = .None
    
    private var historyChangeScreen: [ScreenDisplay] = [.None]
    
    func changeScreenTo(screenName: ScreenDisplay) {
        historyChangeScreen.append(screenName)
        withAnimation {
            self.screen = screenName
        }
    }
    
    func getBeforeScreen() -> ScreenDisplay? {
        if historyChangeScreen.count == 1 {
            return nil
        }
        
        return historyChangeScreen[historyChangeScreen.count - 2]
    }
    
    var lastLocation: CLLocation? = nil
    
    var userDatabase: UserDb? = nil
    var userRepository: AioRepository? = nil
    var chatRoomRepository: ChatRoomRepository? = nil
    var chatRepository: ChatRepository? = nil
    var chatFileRepository: ChatFileRepository? = nil
    
    var chatApi: ChatApi = ChatApi()
    var chatFileApi: FileApi = FileApi()
    var chatDb: ChatDb? = nil
    
    var messageTask: MessageTask?
    
    private var updateFirebase: UpdateFirebaseToken? = nil
    func updateFirebaseToken() -> UpdateFirebaseToken {
        if updateFirebase == nil {
            updateFirebase = UpdateFirebaseToken()
        }
        
        return updateFirebase!
    }
    
    var fileApi: FileApi = FileApi()
    private var sessionCached: URLSession?
    private var urlCache: URLCache?
    func getUrlCache() -> URLCache {
        if urlCache == nil {
            let cacheDir: URL = Config.Shared
                .cacheDir
                .createSubDirectory(name: "okhttp_cache")
            
            urlCache = URLCache(memoryCapacity: 0, diskCapacity: 1073741824, directory: cacheDir)
        }
        
        return urlCache!
    }
    
    func getSessionCached() -> URLSession {
        if sessionCached == nil {
            let config = URLSessionConfiguration.default
            config.timeoutIntervalForRequest = 30
            config.timeoutIntervalForResource = 30
            config.requestCachePolicy = .returnCacheDataElseLoad
            config.urlCache = getUrlCache()
            sessionCached = URLSession(configuration: config)
        }
        
        return sessionCached!
    }
}

