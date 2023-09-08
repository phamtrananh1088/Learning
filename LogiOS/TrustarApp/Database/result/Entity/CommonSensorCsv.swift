//
//  CommonSensorCsv.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/02/21.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonSensorCsvColumns: String, ColumnExpression, CaseIterable {
    case id = "id"
    case companyCd = "company_cd"
    case userId = "user_id"
    case allocationNo = "allocation_no"
    case csv = "csv_file"
    
    func name() -> String {
        return String(describing: self)
    }
}

class CommonSensorCsv: BaseEntity, Codable {
    var id          : Int?   = Resources.zeroNumber
    var companyCd   : String = Resources.strEmpty
    var userId      : String = Resources.strEmpty
    var allocationNo: String = Resources.strEmpty
    var csv         : Data   = Data()
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"common_sensor_csv"}
    
    override class var primaryKey: String { CommonSensorCsvColumns.id.rawValue }
    
    required init(row: Row) {
        id = row[CommonSensorCsvColumns.id]
        companyCd = row[CommonSensorCsvColumns.companyCd]
        userId = row[CommonSensorCsvColumns.userId]
        allocationNo = row[CommonSensorCsvColumns.allocationNo]
        csv = row[CommonSensorCsvColumns.csv]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[CommonSensorCsvColumns.id] = id
        container[CommonSensorCsvColumns.companyCd] = companyCd
        container[CommonSensorCsvColumns.userId] = userId
        container[CommonSensorCsvColumns.allocationNo] = allocationNo
        container[CommonSensorCsvColumns.csv] = csv
        
        super.encode(to: &container)
    }
    
    init(userInfor: UserInfo,
         allocationNo: String,
         csv: Data) {
        self.companyCd = userInfor.companyCd
        self.userId = userInfor.userId
        self.allocationNo = allocationNo
        self.csv = csv
        super.init()
    }
}
