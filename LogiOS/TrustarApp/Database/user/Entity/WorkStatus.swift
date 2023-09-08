//
//  WorkStatus.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum WorkStatusColumns: String, ColumnExpression {
    case workStatusCd, workStatusNm, workBackColor, workFontColor, plateBackColor, displayOrder
}

// 作業ステータス
class WorkStatus: BaseEntity, Codable {
    var workStatusCd: String = Resources.strEmpty
    var workStatusNm: String = Resources.strEmpty
    var workBackColor: String = Resources.strEmpty
    var workFontColor: String = Resources.strEmpty
    var plateBackColor: String = Resources.strEmpty
    var displayOrder: String = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"WorkStatus"}
    override class var primaryKey: String {"workStatusCd"}
    
    required init(row: Row) {
        workStatusCd = row[WorkStatusColumns.workStatusCd]
        workStatusNm = row[WorkStatusColumns.workStatusNm]
        workBackColor = row[WorkStatusColumns.workBackColor]
        workFontColor = row[WorkStatusColumns.workFontColor]
        plateBackColor = row[WorkStatusColumns.plateBackColor]
        displayOrder = row[WorkStatusColumns.displayOrder]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[WorkStatusColumns.workStatusCd] = workStatusCd
        container[WorkStatusColumns.workStatusNm] = workStatusNm
        container[WorkStatusColumns.workBackColor] = workBackColor
        container[WorkStatusColumns.workFontColor] = workFontColor
        container[WorkStatusColumns.plateBackColor] = plateBackColor
        container[WorkStatusColumns.displayOrder] = displayOrder
        
        super.encode(to: &container)
    }
    
    func getStatus() -> WorkStatusEnum {
        return WorkStatusEnum.init(rawValue: workStatusCd)!
    }
}
