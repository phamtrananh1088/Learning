//
//  AttachmentExt.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class AttachmentExt: Attachment {
    var file: URL
    var isCacheFile: Bool
    
    init(file: URL, isCacheFile: Bool, key: String, name: String, size: Int64) {
        self.file = file
        self.isCacheFile = isCacheFile
        super.init(key: key, name: name, size: size)
    }
    
    required init(from decoder: Decoder) throws {
        fatalError("init(from:) has not been implemented")
    }
    
    func contentUri() -> URL {
        return file.appendingPathComponent(name, isDirectory: false)
    }
}
