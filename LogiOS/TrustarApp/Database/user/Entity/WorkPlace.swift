//
//  WorkPlace.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum WorkPlaceColumns: String, ColumnExpression {
    case companyCd, placeCd, placeNm, placeImage1, placeImage2, placeImage3
}

// 発着地
class WorkPlace: BaseEntity, Codable {
    var companyCd: String = Resources.strEmpty
    var placeCd: String = Resources.strEmpty
    var placeNm: String = Resources.strEmpty
    var placeImage1: UInt8? = nil
    var placeImage2: UInt8? = nil
    var placeImage3: UInt8? = nil
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"WorkPlace"}
    override class var primaryKey: String {"placeCd"}
    
    required init(row: Row) {
        companyCd = row[WorkPlaceColumns.companyCd]
        placeCd = row[WorkPlaceColumns.placeCd]
        placeNm = row[WorkPlaceColumns.placeNm]
        placeImage1 = row[WorkPlaceColumns.placeImage1]
        placeImage2 = row[WorkPlaceColumns.placeImage2]
        placeImage3 = row[WorkPlaceColumns.placeImage3]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[WorkPlaceColumns.companyCd] = companyCd
        container[WorkPlaceColumns.placeCd] = placeCd
        container[WorkPlaceColumns.placeNm] = placeNm
        container[WorkPlaceColumns.placeImage1] = placeImage1
        container[WorkPlaceColumns.placeImage2] = placeImage2
        container[WorkPlaceColumns.placeImage3] = placeImage3
        
        super.encode(to: &container)
    }
}
