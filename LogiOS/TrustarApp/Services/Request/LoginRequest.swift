//
//  LoginRequest.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/12.
//

import Foundation

class LoginRequest: Codable {
    var userId: String = Resources.strEmpty
    var companyCd: String = Resources.strEmpty
    var passWord: String = Resources.strEmpty
    var clientInfo: ClientInfo?;
}
