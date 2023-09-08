//
//  LoginResponse.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/12.
//

import Foundation

struct LoginResponse: Codable
{
    var loginResult: LoginResult = LoginResult()
    var userInfo: UserInfo?
}
