//
//  ChatFileEndPoint.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/08.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum ChatFileEndPoint {
   
    case download
    case uploadFile
    case uploadImage
    
    func path() -> String {
        switch self {
        case .download:
            return "File/Download"
        case .uploadFile:
            return "File/Upload"
        case .uploadImage:
            return "Image/Upload"
        }
    }
}
