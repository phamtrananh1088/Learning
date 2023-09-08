//
//  Item.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class Item {
    class Group: Item {
        var group: CollectionGroup
        init(group: CollectionGroup) {
            self.group = group
        }
    }
    
    class Row: Item {
        var row: CollectViewModel.Row
        init(row: CollectViewModel.Row) {
            self.row = row
        }
    }
}
