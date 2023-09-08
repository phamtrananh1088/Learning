//
//  Helper.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/18.
//

import Foundation
import SwiftUI
import var CommonCrypto.CC_MD5_DIGEST_LENGTH
import func CommonCrypto.CC_MD5
import typealias CommonCrypto.CC_LONG

public class Helper {
    static var Shared = Helper()
    
    private let decoder: JSONDecoder
    private let encoder: JSONEncoder
    
    // Store localdata
    enum KeyStoreData: String, CaseIterable {
        case userId, token, companyId, userName, branchTel, userInfo, keyLastWeather
        case notAskAutoMode, dbVersion
        
        func getKey () -> String {
            return self.rawValue
        }
    }
    
    private let localData: UserDefaults
    
    init(localData: UserDefaults = .standard) {
        self.localData = localData
        
        let decoder = JSONDecoder()
        self.decoder = decoder
        
        let encoder = JSONEncoder()
        self.encoder = encoder
    }
    
    func saveValue(key: KeyStoreData, value: String) {
        localData.set(value, forKey: key.getKey())
    }
    
    func readValue<T>(key: KeyStoreData) -> T? {
       return localData.value(forKey: key.getKey()) as? T
    }
    
    func removeValue(key: KeyStoreData) {
        localData.removeObject(forKey: key.getKey())
    }
    
    // JSON Handle
    func convertObjectToJson<T: Encodable>(body: T) -> String? {
        
        let jsonData = try? encoder.encode(body)
        return String(data: jsonData!, encoding: String.Encoding.utf8)
    }
    
    func convertJsonToObject<T: Decodable>(json: Data) -> T {
        return try! decoder.decode(T.self, from: json)
    }
    
    func tryConvertJsonToObject<T: Decodable>(json: Data) -> T? {
        return try? decoder.decode(T.self, from: json)
    }
    
    // read/write file
    func writeDataToFile(path: URL, content: String) {
        try? content.data(using: .utf8)?.write(to: path)
    }
    
    func readDataFromFile<T: Decodable> (path: URL) -> T {
        let data: Data
        
        do {
            data = try Data(contentsOf: path)
        } catch {
            fatalError("Couldn't load.")
        }
        
        do {
          let decoder = JSONDecoder()
            return try decoder.decode(T.self, from: data)
        } catch {
            fatalError("Couldn't parse")
        }
    }
    
    func allPropertiesOf(object: Any, columns: [(String, String)] = []) -> [String: String] {
        var result: [String: String] = [:]
        
        let mirror = Mirror(reflecting: object)
        
        if columns.isEmpty {
            for prop in mirror.children {
                result[prop.label!] = String(reflecting: type(of: prop.value))
            }
        } else {
            for c in columns {
                let child = mirror.children.filter({ $0.label == c.1}).first!
                result[c.0] = String(reflecting: type(of: child.value))
            }
        }

        return result
    }
    
    func appendDictionary<V, K>(dic1: [V : K], dic2: [V : K]) -> [V : K]{
        var combineDic: [V: K] = dic1
        for item in dic2 {
            combineDic[item.key] = item.value
        }

        return combineDic
    }
    
    // device
    func getModelName() -> String {
        var systemInfo = utsname()
                uname(&systemInfo)
                let machineMirror = Mirror(reflecting: systemInfo.machine)
                let identifier = machineMirror.children.reduce("") { identifier, element in
                    guard let value = element.value as? Int8, value != 0 else { return identifier }
                    return identifier + String(UnicodeScalar(UInt8(value)))
                }
        
        return identifier
    }
    
    func getVersionApp() -> String {
        let nsObject: String? = Bundle.main.infoDictionary!["CFBundleShortVersionString"] as? String
        
        return nsObject!
    }
    
    // date time
    func getDayOfWeek(dayOfWeek: Int, isLongDisplay: Bool = true) -> String {
        var returnValue: String = Resources.strEmpty
        
        switch dayOfWeek {
        case 2:
            returnValue = "月"
        case 3:
            returnValue = "火"
        case 4:
            returnValue = "水"
        case 5:
            returnValue = "木"
        case 6:
            returnValue = "金"
        case 7:
            returnValue = "土"
        default:
            returnValue = "日"
        }
        
        if returnValue != Resources.strEmpty && isLongDisplay {
            returnValue += "曜日"
        }
        
        return returnValue
    }
    
    func getLastWeather() -> WeatherEnum? {
        let valLastWeather: String! = readValue(key: .keyLastWeather)

        if valLastWeather != nil {
            return WeatherEnum.init(rawValue: Int.init(valLastWeather!)!)
        } else {
            return nil
        }
    }
    
    func setWeather(weather: WeatherEnum) {
        saveValue(key: .keyLastWeather, value: String.init(weather.rawValue))
    }
    
    func sendBroadcastNotification(_ notification: BroadcastEnum) {
        NotificationCenter.default.post(name: Notification.Name(notification.rawValue), object: nil)
    }
    
    func MD5(string: String) -> Data {
        let length = Int(CC_MD5_DIGEST_LENGTH)
        let messageData = string.data(using:.utf8)!
        var digestData = Data(count: length)
 
        _ = digestData.withUnsafeMutableBytes { digestBytes -> UInt8 in
            messageData.withUnsafeBytes { messageBytes -> UInt8 in
                if let messageBytesBaseAddress = messageBytes.baseAddress, let digestBytesBlindMemory = digestBytes.bindMemory(to: UInt8.self).baseAddress {
                    let messageLength = CC_LONG(messageData.count)
                    CC_MD5(messageBytesBaseAddress, messageLength, digestBytesBlindMemory)
                }
                return 0
            }
        }
        return digestData
    }
    
    func hashMd5Hex16Digit(string: String) -> String {
        let md5Data = MD5(string: string)
        let md5Hex = md5Data.map { String(format: "%02hhx", $0) }.joined()
        
        return String(md5Hex.prefix(16))
    }
    
    func lockOrientation(_ orientation: UIInterfaceOrientationMask) {
        if let delegate = UIApplication.shared.delegate as? AppDelegate {
            delegate.orientationLock = orientation
        }
    }

    func lockOrientation(_ orientation: UIInterfaceOrientationMask, andRotateTo rotateOrientation: UIDeviceOrientation) {
        self.lockOrientation(orientation)
        UIDevice.current.setValue(rotateOrientation.rawValue, forKey: "orientation")
        UINavigationController.attemptRotationToDeviceOrientation()
    }
    
    func stopURLSessionByUrl(lstEndpoint: [Endpoint]) {
        URLSession.shared.getAllTasks(completionHandler: { tasks in
            for it in lstEndpoint {
                let lst = tasks.filter({
                    $0.originalRequest?.url?.path.contains(it.path()) == true
                })
                
                for t in lst {
                    DispatchQueue.main.async {
                        t.cancel()
                    }
                }
            }
        })
    }
    
    func checkURLSessionContaintUrl(
        lstEndpoint: [Endpoint],
        onResult: @escaping (Bool) -> Void
    ){
        URLSession.shared.getAllTasks(completionHandler: { tasks in
            var isContain = false
            for it in lstEndpoint {
                if tasks.contains(where: { $0.originalRequest?.url?.path.contains(it.path()) == true }) {
                    isContain = true
                    break
                }
            }
            
            onResult(isContain)
        })
    }
    
    //MARK: 新バージョンがあるかをチェックする。
    func checkAppUpdateAvailability(onSuccess: @escaping (Bool, String) -> Void, onError: @escaping (Bool) -> Void) {
        guard let info = Bundle.main.infoDictionary,
              let bundleId = info["CFBundleIdentifier"] as? String,
              let appVersion = info["CFBundleShortVersionString"] as? String,
              let url = URL(string: Resources.APP_BUNDLE_URL.replacingOccurrences(of: "{0}", with: String.init(bundleId))) else {
                return onError(true)
        }
        
        let request = URLRequest(url: url)
        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
            guard let data = data else {
                onError(true)
                return
            }
            
            do {
                guard let jsonData = try JSONSerialization.jsonObject(with: data, options: [.allowFragments]) as? [String: Any] else {
                    onError(true)
                    return
                }
                if let results = (jsonData["results"] as? [Any])?.first as? [String: Any],
                   let storeVersion = results["version"] as? String {
                    let versionCompare = storeVersion.compare(appVersion, options: .numeric)
                    
                    switch versionCompare {
                    case .orderedDescending :
                        //appVersion < storeVersion
                        let appId = results["trackId"] as? Int64
                        let appIdUrl = Resources.APP_ID_URL.replacingOccurrences(of: "{0}", with: String.init(appId ?? Int64(Resources.zeroNumber)))
                        onSuccess(true, appIdUrl)
                    case.orderedSame, .orderedAscending:
                        //appVersion >= storeVersion
                        onSuccess(false, Resources.strEmpty)
                    }
                } else {
                    onError(true)
                }
            } catch {
                onError(true)
            }
        }
        task.resume()
    }
    //MARK: Appstoreへ移動する。
    func goToAppStore(appUrl: String, onCompletion: @escaping (Bool) -> Void) {
        //open appstore to update the app
        if let url = URL(string: appUrl),
           UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url, options: [:], completionHandler: onCompletion)
        }
    }

}
