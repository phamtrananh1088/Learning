//
//  ClientInfo.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/12.
//

import Foundation
import GRDB
// クライアント情報
class ClientInfo: BaseEntity, Codable {
    var terminalId: String
    var terminalName: String
    var osVersion: String
    var appVersion: String
    
    init(terminalId: String,terminalName: String,osVersion: String,appVersion: String) {
        self.terminalId = terminalId
        self.terminalName = terminalName
        self.osVersion = osVersion
        self.appVersion = appVersion
        
        super.init()
    }
    
    required init(row: Row) {
        fatalError("init(row:) has not been implemented")
    }
    
    required init(from decoder: Decoder) throws {
        fatalError("init(from:) has not been implemented")
    }
}
