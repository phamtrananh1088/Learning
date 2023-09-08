//
//  CommonWorkResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/31.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum CommonWorkResultColumns: String, ColumnExpression {
    case companyCd, userId, allocationNo, allocationRowNo, workCd, status, delayStatus, delayReasonCd, misdeliveryStatus, orgAllocationRowNo
    case latitudeFrom, longitudeFrom, accuracyFrom, workDatetimeFrom
    case latitudeTo, longitudeTo, accuracyTo, workDatetimeTo
    case temperature, experiencePlaceNote1
    case operatingMode
}

class CommonWorkResult: BaseEntity, Codable {
    var companyCd: String             = Resources.strEmpty
    var userId: String                = Resources.strEmpty
    var allocationNo: String          = Resources.strEmpty
    var allocationRowNo: Int          = Resources.zeroNumber
    var workCd: String?               = nil
    var status: Int                   = Resources.zeroNumber
    var delayStatus: Int              = Resources.zeroNumber
    var delayReasonCd: String?        = nil
    var misdeliveryStatus: String     = Resources.strEmpty
    var orgAllocationRowNo: Int?      = Resources.zeroNumber

    var latitudeFrom: Double?        = nil
    var longitudeFrom: Double?       = nil
    var accuracyFrom: Double?        = nil
    var workDatetimeFrom: Int64?     = nil

    var latitudeTo: Double?          = nil
    var longitudeTo: Double?         = nil
    var accuracyTo: Double?          = nil
    var workDatetimeTo: Int64?       = nil

    var temperature: String?          = nil
    var experiencePlaceNote1: String? = nil
    var operatingMode: String         = String()

    override init() {
        super.init()
        recordChanged()
    }

    override class var databaseTableName: String { "CommonWorkResult" }
    
    override class var primaryKey: String { "companyCd,userId,allocationNo,allocationRowNo" }

    required init(row: Row) {
        companyCd = row[CommonWorkResultColumns.companyCd]
        userId = row[CommonWorkResultColumns.userId]
        allocationNo = row[CommonWorkResultColumns.allocationNo]
        allocationRowNo = row[CommonWorkResultColumns.allocationRowNo]
        workCd = row[CommonWorkResultColumns.workCd]
        status = row[CommonWorkResultColumns.status]
        delayStatus = row[CommonWorkResultColumns.delayStatus]
        delayReasonCd = row[CommonWorkResultColumns.delayReasonCd]
        misdeliveryStatus = row[CommonWorkResultColumns.misdeliveryStatus]
        orgAllocationRowNo = row[CommonWorkResultColumns.orgAllocationRowNo]
        
        latitudeFrom = row[CommonWorkResultColumns.latitudeFrom]
        longitudeFrom = row[CommonWorkResultColumns.longitudeFrom]
        accuracyFrom = row[CommonWorkResultColumns.accuracyFrom]
        workDatetimeFrom = row[CommonWorkResultColumns.workDatetimeFrom]
        
        latitudeTo = row[CommonWorkResultColumns.latitudeTo]
        longitudeTo = row[CommonWorkResultColumns.longitudeTo]
        accuracyTo = row[CommonWorkResultColumns.accuracyTo]
        workDatetimeTo = row[CommonWorkResultColumns.workDatetimeTo]
        
        temperature = row[CommonWorkResultColumns.temperature]
        experiencePlaceNote1 = row[CommonWorkResultColumns.experiencePlaceNote1]
        super.init(row: row)
    }
    
    init(userInfo: UserInfo, binDetail: BinDetail) {
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        allocationNo = binDetail.allocationNo
        allocationRowNo = binDetail.allocationRowNo
        workCd = binDetail.workCd
        status = binDetail.status
        delayStatus = binDetail.delayStatus
        delayReasonCd = binDetail.delayReasonCd
        misdeliveryStatus = binDetail.misdeliveryStatus
        
        orgAllocationRowNo = binDetail.origAllocationRowNo
        
        latitudeFrom = binDetail.startLatitude
        longitudeFrom = binDetail.startLongitude
        accuracyFrom = binDetail.startAccuracy
        workDatetimeFrom = binDetail.startDate

        latitudeTo = binDetail.endLatitude
        longitudeTo = binDetail.endLongitude
        accuracyTo = binDetail.endAccuracy
        workDatetimeTo = binDetail.endDate

        temperature = binDetail.temperature
        experiencePlaceNote1 = binDetail.experiencePlaceNote1
        
        operatingMode = binDetail.autoModeFlag ? "autoMode" : String()

        super.init()
        self.recordChanged()
    }

    override func encode(to container: inout PersistenceContainer) {
        container[CommonWorkResultColumns.companyCd] = companyCd
        container[CommonWorkResultColumns.userId] = userId
        container[CommonWorkResultColumns.allocationNo] = allocationNo
        container[CommonWorkResultColumns.allocationRowNo] = allocationRowNo
        container[CommonWorkResultColumns.workCd] = workCd
        container[CommonWorkResultColumns.status] = status
        container[CommonWorkResultColumns.delayStatus] = delayStatus
        container[CommonWorkResultColumns.delayReasonCd] = delayReasonCd
        container[CommonWorkResultColumns.misdeliveryStatus] = misdeliveryStatus
        container[CommonWorkResultColumns.orgAllocationRowNo] = orgAllocationRowNo

        container[CommonWorkResultColumns.latitudeFrom] = latitudeFrom
        container[CommonWorkResultColumns.longitudeFrom] = longitudeFrom
        container[CommonWorkResultColumns.accuracyFrom] = accuracyFrom
        container[CommonWorkResultColumns.workDatetimeFrom] = workDatetimeFrom

        container[CommonWorkResultColumns.latitudeTo] = latitudeTo
        container[CommonWorkResultColumns.longitudeTo] = longitudeTo
        container[CommonWorkResultColumns.accuracyTo] = accuracyTo
        container[CommonWorkResultColumns.workDatetimeTo] = workDatetimeTo

        container[CommonWorkResultColumns.temperature] = temperature
        container[CommonWorkResultColumns.experiencePlaceNote1] = experiencePlaceNote1
        
        super.encode(to: &container)
    }
    
    func startWorkIsNull() -> Bool {
        return latitudeFrom == nil && longitudeFrom == nil
        && accuracyFrom == nil && workDatetimeFrom == nil
    }
    
    func endWorkIsNull() -> Bool {
        return latitudeTo == nil && longitudeTo == nil
        && accuracyTo == nil && workDatetimeTo == nil
    }
}
