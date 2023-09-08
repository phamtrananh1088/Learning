//
//  FileKey.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class FileKey: Codable {
    var fileKey: String
    
    init(fileKey: String) {
        self.fileKey = fileKey
    }
}
