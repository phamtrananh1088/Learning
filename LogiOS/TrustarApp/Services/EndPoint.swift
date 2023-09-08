//
//  EndPoint.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/17.
//

import Foundation

enum Endpoint {
    // login
    case prelogin
    case login
    // fetch
    case masterData
    case binData
    case incidentalData
    case noticeData
    case getImgData
    case startUnscheduledBin
    case getToken
    // post
    case postGeo
    case postBin
    case postIncidental
    case postNotice
    case postFuel
    case postFailedData
    case uploadImgData
    case postLogFile
    case postCollection
    case rest
    case postDeliveryChart
    case deliveryChartData
    case getChartImage
    
    // Upload
    case uploadSensorCSV
    
    func path() -> String {
        switch self {
        // login
        case .prelogin:
            return "account/prelogin"
        case .login:
            return "account/login"
            
        // fetch
        case .masterData:
            return "master/getdata"
        case .binData:
            return "operation/getdata"
        case .incidentalData:
            return "operation/incidentalGetdata"
        case .noticeData:
            return "notice/getdata"
        case .getImgData:
            return "operation/getImg"
        //onlineapi
        case .startUnscheduledBin:
            return "unplannedAllocation/post"
        case .getToken:
            return "unplannedAllocation/getToken"
            
        // post
        case .postGeo:
            return "coordinate/post"
        case .postBin:
            return "operation/post"
        case .postIncidental:
            return "operation/postIncidental"
        case .postNotice:
            return "notice/post"
        case .postFuel:
            return "fuel/post"
        case .postFailedData:
            return "localdata/post"
        case .uploadImgData:
            return "operation/postImg"
        case .postLogFile:
            return "appLog/postLogFile"
        case .postCollection:
            return "operation/postCollection"
        case .rest:
            return "rest/post"
            
        // Upload
        case .uploadSensorCSV:
            return "Sensor/Upload"
        case .postDeliveryChart:
            return "operation/postChart"
        case .deliveryChartData:
            return "operation/getChartData"
        case .getChartImage:
            return "Operation/GetChartImg"
        }
    }
}
