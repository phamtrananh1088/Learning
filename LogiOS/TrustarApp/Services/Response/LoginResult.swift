//
//  LoginResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/17.
//

import Foundation

// 認証結果
class LoginResult: Codable {
    var isLoggedIn: Bool = false
    var messageCode: String = ""
    var token: String? = nil
    var expires: String? = nil
}
