//
//  Config.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB
import UIKit
import SwiftUI

public class Config {
    static let Shared = Config()
    
    let fetch = Fetch()

    let post = Post()
    
    var userDb: UserDb? = nil

    var resultDb: ResultDb? = nil

    var imageDb: ImageDb? = nil
        
    var clientInfo: ClientInfo = ClientInfo(terminalId: Helper.Shared.hashMd5Hex16Digit(string: UIDevice.current.identifierForVendor!.uuidString),
                                                   terminalName: Helper.Shared.getModelName(),
                                                   osVersion: UIDevice.current.systemVersion,
                                                   appVersion: Helper.Shared.getVersionApp())

    var locale: Locale = Locale(identifier: "ja_JP")
    var GMT9: TimeZone = TimeZone(abbreviation: "GMT+9")!
    var timeZone: TimeZone? = TimeZone(identifier: "Asia/Tokyo")
    
    var delayColorRank: [Color] = [Color.red,
                                   Color(UIColor(red: 255, green: 176, blue: 97)),
                                   Color(UIColor(red: 255, green: 176, blue: 97))]
    
    var dateFormatter: ReuseDateFormatter = ReuseDateFormatter(pattern: "yyyy-MM-dd'T'HH:mm:ss",
                                                               locale: Locale(identifier: "ja_JP"),
                                                               timeZone: TimeZone(identifier: "Asia/Tokyo"))
    var dateFormatter2: ReuseDateFormatter = ReuseDateFormatter(pattern: "yyyy/MM/dd HH:mm:ss",
                                                               locale: Locale(identifier: "ja_JP"),
                                                               timeZone: TimeZone(identifier: "Asia/Tokyo"))
    var dateFormatter3: ReuseDateFormatter = ReuseDateFormatter(pattern: "yyyy/MM/dd HH:mm",
                                                               locale: Locale(identifier: "ja_JP"),
                                                               timeZone: TimeZone(identifier: "Asia/Tokyo"))
    var dateFormatterMMddHHmm: ReuseDateFormatter = ReuseDateFormatter(pattern: "MM月dd日 HH:mm",
                                                               locale: Locale(identifier: "ja_JP"),
                                                               timeZone: TimeZone(identifier: "Asia/Tokyo"))
    var dateFormatterHHmm: ReuseDateFormatter = ReuseDateFormatter(pattern: "HH:mm",
                                                                   locale: Locale(identifier: "ja_JP"),
                                                                   timeZone: TimeZone(identifier: "Asia/Tokyo"))
    
    var dateFormatterMMdd: ReuseDateFormatter = ReuseDateFormatter(pattern: "MM/dd",
                                                                   locale: Locale(identifier: "ja_JP"),
                                                                   timeZone: TimeZone(identifier: "Asia/Tokyo"))
    
    var dateFormatterForChat: ReuseDateFormatter = ReuseDateFormatter(pattern: "yyyy年M月d日 E曜日 HH:mm",
                                                                   locale: Locale(identifier: "ja_JP"),
                                                                   timeZone: TimeZone(identifier: "Asia/Tokyo"))
    
    var timeFormatter: ReuseDateFormatter = ReuseDateFormatter(pattern: "yyyyMMddHHmmss", locale: Locale(identifier: "ja_JP"), timeZone: TimeZone(identifier: "Asia/Tokyo"))
    
    var dateFormatterSensorCsv: ReuseDateFormatter = ReuseDateFormatter(pattern: "yyyy-MM-dd HH:mm:ss.SSS",
                                                                        locale: Locale(identifier: "ja_JP"),
                                                                        timeZone: TimeZone(identifier: "Asia/Tokyo"))
    
    lazy var fileDir: URL = getDocumentDirectoryPath()
    
    lazy var userDir: URL = getDocumentDirectoryPath().createSubDirectory(name: "user")
    
    lazy var userDirInCache: URL = getCacheDirectoryPath().createSubDirectory(name: "user")
    
    lazy var cacheDir: URL = getCacheDirectoryPath()
    
    lazy var errorLogDir: URL = getCacheDirectoryPath().createSubDirectory(name: "error_log")
    
    lazy var tmpDir: URL = getCacheDirectoryPath().createSubDirectory(name: "temp", removeBeforeCreate: false)
    
    var commonChartSyncDir:  FilesInDir { return FilesInDir(dir: fileDir.createSubDirectory(name: "chartImages")) }
}
