//
//  EditTarget.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Combine

class EditTarget {
    class Sheet: EditTarget {
        var uuid: String
        
        init(uuid: String) {
            self.uuid = uuid
        }
    }
    
    class Add: EditTarget {
        var binDetail: BinDetail
        
        init(binDetail: BinDetail) {
            self.binDetail = binDetail
        }
    }
}
