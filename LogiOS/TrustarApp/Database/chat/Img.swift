//
//  Img.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class Img: Codable {
    var attachment: Attachment
    var url: String
    var width: Int
    var height: Int
    
    func ratio() -> Float? {
        return width > 0 && height > 0 ? Float(width) / Float(height) : nil
    }
    
    init(attachment: Attachment,
         url: String,
         width: Int,
         height: Int
    ) {
        self.attachment = attachment
        self.url = url
        self.width = width
        self.height = height
    }
}
