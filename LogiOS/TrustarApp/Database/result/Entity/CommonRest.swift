//
//  CommonRest.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/05/16.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import GRDB
import CoreLocation

enum CommonRestColumns: String, ColumnExpression, CaseIterable {
    case id = "id"
    case companyCd = "company_cd"
    case userId = "user_id"
    case allocationNo = "allocation_no"
    case startLatitude = "start_latitude"
    case startLongtitude = "start_longtitude"
    case startAccuracy = "start_accuracy"
    case endDate = "end_date"
    case elapsed = "elapsed"
    
    func name() -> String {
        return String(describing: self)
    }
}

class CommonRest: BaseEntity, Codable {
    var id          : Int?   = Resources.zeroNumber
    var companyCd   : String = Resources.strEmpty
    var userId      : String = Resources.strEmpty
    var allocationNo: String = Resources.strEmpty
    var startLatitude    : Double = 0
    var startLongtitude  : Double = 0
    var startAccuracy    : Double = 0
    var endDate     : Int64  = 0
    var elapsed     : Int64  = 0
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"commonRest"}
    
    override class var primaryKey: String { CommonRestColumns.id.rawValue }
    
    required init(row: Row) {
        id = row[CommonRestColumns.id]
        companyCd = row[CommonRestColumns.companyCd]
        userId = row[CommonRestColumns.userId]
        allocationNo = row[CommonRestColumns.allocationNo]
        startLatitude = row[CommonRestColumns.startLatitude]
        startLongtitude = row[CommonRestColumns.startLongtitude]
        startAccuracy = row[CommonRestColumns.startAccuracy]
        endDate = row[CommonRestColumns.endDate]
        elapsed = row[CommonRestColumns.elapsed]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[CommonRestColumns.id] = id
        container[CommonRestColumns.companyCd] = companyCd
        container[CommonRestColumns.userId] = userId
        container[CommonRestColumns.allocationNo] = allocationNo
        container[CommonRestColumns.startLatitude] = startLatitude
        container[CommonRestColumns.startLongtitude] = startLongtitude
        container[CommonRestColumns.startAccuracy] = startAccuracy
        container[CommonRestColumns.endDate] = endDate
        container[CommonRestColumns.elapsed] = elapsed
        
        super.encode(to: &container)
    }
    
    init(userInfor: UserInfo,
         allocationNo: String,
         startLocation: CLLocation,
         endDate: Int64,
         elapsed: Int64
    ) {
        self.companyCd = userInfor.companyCd
        self.userId = userInfor.userId
        self.allocationNo = allocationNo
        self.startLatitude = startLocation.coordinate.latitude
        self.startLongtitude = startLocation.coordinate.longitude
        self.startAccuracy = startLocation.horizontalAccuracy
        self.endDate = endDate
        self.elapsed = elapsed
        
        super.init()
        self.recordChanged()
    }
}

