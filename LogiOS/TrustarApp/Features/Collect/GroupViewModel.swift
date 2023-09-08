//
//  GroupViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class GroupViewModel {
 
    var group: CollectionGroup
    var callBack: (CollectionGroup) -> Void
    
    init(group: CollectionGroup, callBack: @escaping (CollectionGroup) -> Void) {
        self.group = group
        self.callBack = callBack
    }
}
