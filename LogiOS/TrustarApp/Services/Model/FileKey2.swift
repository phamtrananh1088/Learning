//
//  FileKey2.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class FileKey2: Codable {
    var fileKey: String
    
    init(fileKey: String) {
        self.fileKey = fileKey
    }
    
    func key() -> FileKey {
        return FileKey(fileKey: fileKey)
    }
    
    func realFileKey() -> String {
        return fileKey.components(separatedBy: ",").getOrNull(index: 1) ?? fileKey
    }
}
