//
//  NetworkError.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/17.
//

import Foundation

enum NetworkError : Error {
    case unknow
    case error(error: Error)
    case actionHttp401
    case message(reason: String), parseError(reason: Error), networkError(reason: String)
}
