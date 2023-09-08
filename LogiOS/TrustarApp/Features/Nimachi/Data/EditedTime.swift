//
//  EditedTime.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

class EditedTime: TimeItem {
    var t: TimeItem
    var color: Int
    
    var justChanged: Bool = false
    var markDeleted: Bool = false
    var overrideBeginDate: DateItem? = nil
    var overrideEndDate: DateItem? = nil
    
    //0x11ff0000
    init(t: TimeItem, color: Int = 0xffeeee) {
        self.t = t
        self.color = color
        super.init(begin: t.begin, end: t.end, type: t.type)
    }
    
    func targetBeginDate() -> DateItem? {
        return overrideBeginDate ?? begin
    }
    
    func targetEndDate() -> DateItem? {
        return overrideEndDate ?? end
    }
    
    override var beginDateString: String? {
        return targetBeginDate()?.localDateString
    }
    
    override var endDateString: String? {
        return targetEndDate()?.localDateString
    }
    
    func hasChanged() -> Bool {
        return justChanged || markDeleted ||
               overrideBeginDate != nil || overrideEndDate != nil
    }
}
