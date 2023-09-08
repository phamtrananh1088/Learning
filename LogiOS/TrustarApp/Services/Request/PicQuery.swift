//
//  PicQuery.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class PicQuery: Codable {
    var companyCd: String
    var picId: String
    
    init(picId: String, userInfo: UserInfo) {
        self.companyCd = userInfo.companyCd
        self.picId = picId
    }
}
