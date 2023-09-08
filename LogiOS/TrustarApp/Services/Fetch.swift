//
//  FetchAPI.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/13.
//

import Foundation
import SwiftUI
import Combine

struct Fetch
{
    func preLogin(loginRq: LoginRequest, apiKey: String) -> AnyPublisher<LoginResponse, NetworkError> {
        return API.shared.callAPI(.prelogin, loginRq, apiKey)
    }
    
    func login(loginRq: LoginRequest, apiKey: String) -> AnyPublisher<LoginResponse, NetworkError> {
        return API.shared.callAPI(.login, loginRq, apiKey)
    }
    
    func master(loginInfo: LoggerUser) -> AnyPublisher<MasterResponse, NetworkError> {
        return API.shared.callAPI(.masterData, loginInfo.userInfo, loginInfo.token)
    }
    
    func notice(loginInfo: LoggerUser) -> AnyPublisher<NoticeResponse, NetworkError> {
        return API.shared.callAPI(.noticeData, loginInfo.userInfo, loginInfo.token)
    }
    
    func binData(loginInfo: LoggerUser) -> AnyPublisher<BinResponse, NetworkError> {
        return API.shared.callAPI(.binData, loginInfo.userInfo, loginInfo.token)
    }
    
    func incidentalData(loginInfo: LoggerUser) -> AnyPublisher<IncidentalResponse, NetworkError> {
        return API.shared.callAPI(.incidentalData, loginInfo.userInfo, loginInfo.token)
    }
    
    func startUnscheduledBin(loginInfo: LoggerUser, unscheduledBin: UnscheduledBin) -> AnyPublisher<UnscheduledBinAllocation, NetworkError> {
        return API.shared.callAPI(.startUnscheduledBin, unscheduledBin, loginInfo.token)
    }
    
    func getToken(loginInfo: LoggerUser) -> AnyPublisher<String, NetworkError> {
        return API.shared.callAPI(.getToken, loginInfo.userInfo, loginInfo.token)
    }
    
    func deliveryChartData(loginInfo: LoggerUser) -> AnyPublisher<RawChart, NetworkError> {
        return API.shared.callAPI(.deliveryChartData, loginInfo.userInfo, loginInfo.token)
    }
}
