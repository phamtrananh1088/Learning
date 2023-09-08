//
//  BinStatus.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum BinStatusColumns: String, ColumnExpression {
    case binStatusCd, binStatusNm, binBackColor,binFontColor
}

// 運行状況ステータス
class BinStatus: BaseEntity, Codable {
    var binStatusCd: String = Resources.strEmpty
    var binStatusNm: String = Resources.strEmpty
    var binBackColor: String = Resources.strEmpty
    var binFontColor: String = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"BinStatus"}
    
    override class var primaryKey: String {"binStatusCd"}
    
    required init(row: Row) {
        binStatusCd = row[BinStatusColumns.binStatusCd]
        binStatusNm = row[BinStatusColumns.binStatusNm]
        binBackColor = row[BinStatusColumns.binBackColor]
        binFontColor = row[BinStatusColumns.binFontColor]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[BinStatusColumns.binStatusCd] = binStatusCd
        container[BinStatusColumns.binStatusNm] = binStatusNm
        container[BinStatusColumns.binBackColor] = binBackColor
        container[BinStatusColumns.binFontColor] = binFontColor
        
        super.encode(to: &container)
    }
    
    func getStatus() -> BinStatusEnum {
        return BinStatusEnum.init(rawValue: binStatusCd)!
    }
}
