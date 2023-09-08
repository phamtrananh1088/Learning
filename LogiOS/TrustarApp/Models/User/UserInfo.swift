//
//  UserInfo.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/12.
//

import Foundation

// ユーザー情報
class UserInfo: Codable {
    var companyCd: String = Resources.strEmpty
    var userId: String = Resources.strEmpty
    var userNm: String = Resources.strEmpty
    var appAuthority: Int = Resources.zeroNumber
    var branchTel: String = Resources.strEmpty
    var positionSendInterval: Int = Resources.zeroNumber
    var positionGetInterval: Int = Resources.zeroNumber
    var decimationRange: Int? = nil
    var backgroundInterval: Int? = nil
    var meterInputFlag: Int? = nil
    var stayTime: Int = Resources.zeroNumber
    var misdeliveryMeterTo: String = Resources.strEmpty
    var incidentalUseFlag: Int? = nil
    var geofenceUseFlag: Int? = nil
    var homeDir: URL?
    var homeCacheDirFile: URL?
    var userLocalFileDir: URL?
    //detectStartTimingInMin スマホ使用検知の開始タイミング（分）
    var detectStartTiming: String = Resources.strEmpty
    //restDistanceMeter
    var restDistance: Int = Resources.zeroNumber
    //restTimeInMin
    var restTime: Int = Resources.zeroNumber
    
    init() {}
    
    init(userInfo: UserInfo) {
        self.companyCd = userInfo.companyCd
        self.userId = userInfo.userId
        self.userNm = userInfo.userNm
        self.appAuthority = userInfo.appAuthority
        self.branchTel = userInfo.branchTel
        self.positionSendInterval = userInfo.positionSendInterval
        self.positionGetInterval = userInfo.positionGetInterval
        self.decimationRange = userInfo.decimationRange
        self.backgroundInterval = userInfo.backgroundInterval
        self.meterInputFlag = userInfo.meterInputFlag
        self.stayTime = userInfo.stayTime
        self.misdeliveryMeterTo = userInfo.misdeliveryMeterTo
        self.incidentalUseFlag = userInfo.incidentalUseFlag
        self.geofenceUseFlag = userInfo.geofenceUseFlag
        self.homeDir = pathOfUserHome(companyCd: companyCd, userId: userId)
        
        let relativePath = "\(companyCd)/\(userId)/"
        self.homeCacheDirFile = Config.Shared.userDirInCache.createSubDirectory(name: relativePath)
        
        self.userLocalFileDir = homeDir?.createSubDirectory(name: "files")
        
        self.detectStartTiming = userInfo.detectStartTiming
        self.restDistance = userInfo.restDistance
        self.restTime = userInfo.restTime
    }
    
    func cacheByKey(fileKey: String, isFile: Bool) -> FileByKey {
        if let f = homeCacheDirFile {
            let path = isFile
            ? f.createSubDirectory(name: "file_by_key").createFile(name: fileKey)
            : f.createSubDirectory(name: "file_by_key").createSubDirectory(name: fileKey)

            return FileByKey(fileKey: fileKey, file: path)
        }
        
        return FileByKey(fileKey: "", file: URL(fileURLWithPath: ""))
    }
    
    func pathToCacheFile (fileKey: String) -> URL?  {
        if let f = homeCacheDirFile {
            let path = f.appendingPathComponent("file_by_key").appendingPathComponent(fileKey)
            return path
        }
        
        return nil
    }
    
    class FileByKey {
        var fileKey: String
        var file: URL
        init(fileKey: String, file: URL) {
            self.fileKey = fileKey
            self.file = file
        }
    }
    
    func isGeofenceUseFlag() -> Bool {
        return geofenceUseFlag == 1
    }
    
    func incidentalEnable() -> Bool {
        return incidentalUseFlag == 1
    }
    
    func meterInputEnable() -> Bool {
        return meterInputFlag == 1
    }
    
    func result() -> Resutl {
        return Resutl(user: User(userId: userId, companyCd: companyCd))
    }
    
    class Resutl: Codable {
        var user: User
        init(user: User) {
            self.user = user
        }
    }
    
    class User: Codable {
        var userId: String
        var companyCd: String
        init(userId: String, companyCd: String) {
            self.userId = userId
            self.companyCd = companyCd
        }
    }
}
