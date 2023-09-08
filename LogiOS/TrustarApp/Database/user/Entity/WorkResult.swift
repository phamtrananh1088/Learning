//
//  WorkResult.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum WorkResultColumns: String, ColumnExpression {
    case allocationNo, allocationRowNo, workCd, serviceOrder, status, workDatetimeFrom, workDatetimeTo, latitudeFrom
    case longitudeFrom, latitudeTo, longitudeTo, delayStatus, delayReasonCd, misdeliveryStatus, orgAllocationRowNo
    case updatedDate, companyCd, UserId, temperature, experiencePlaceNote1, operatingMode
}

// 作業実績
class WorkResult: BaseEntity, Codable {
    var allocationNo:         String = Resources.strEmpty
    var allocationRowNo:      Int = Resources.zeroNumber
    var workCd:               String = Resources.strEmpty
    var serviceOrder:         Int = Resources.zeroNumber
    var status:               Int = Resources.zeroNumber
    var workDatetimeFrom:     String = Resources.strEmpty
    var workDatetimeTo:       String = Resources.strEmpty
    var latitudeFrom:         String = Resources.strEmpty
    var longitudeFrom:        String = Resources.strEmpty
    var latitudeTo:           String = Resources.strEmpty
    var longitudeTo:          String = Resources.strEmpty
    var delayStatus:          Int = Resources.zeroNumber
    var delayReasonCd:        String = Resources.strEmpty
    var misdeliveryStatus:    Int = Resources.zeroNumber
    var orgAllocationRowNo:   Int = Resources.zeroNumber
    var updatedDate:          String = Resources.strEmpty
    var companyCd:            String = Resources.strEmpty
    var UserId:               String = Resources.strEmpty
    var temperature:          String = Resources.strEmpty
    var experiencePlaceNote1: String = Resources.strEmpty
    var operatingMode:        String = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"WorkResult"}
    
    required init(row: Row) {
        allocationNo = row[WorkResultColumns.allocationNo]
        allocationRowNo = row[WorkResultColumns.allocationRowNo]
        workCd = row[WorkResultColumns.workCd]
        serviceOrder = row[WorkResultColumns.serviceOrder]
        status = row[WorkResultColumns.status]
        workDatetimeFrom = row[WorkResultColumns.workDatetimeFrom]
        workDatetimeTo = row[WorkResultColumns.workDatetimeTo]
        latitudeFrom = row[WorkResultColumns.latitudeFrom]
        longitudeFrom = row[WorkResultColumns.longitudeFrom]
        latitudeTo = row[WorkResultColumns.latitudeTo]
        longitudeTo = row[WorkResultColumns.longitudeTo]
        delayStatus = row[WorkResultColumns.delayStatus]
        delayReasonCd = row[WorkResultColumns.delayReasonCd]
        misdeliveryStatus = row[WorkResultColumns.misdeliveryStatus]
        orgAllocationRowNo = row[WorkResultColumns.orgAllocationRowNo]
        updatedDate = row[WorkResultColumns.updatedDate]
        companyCd = row[WorkResultColumns.companyCd]
        UserId = row[WorkResultColumns.UserId]
        temperature = row[WorkResultColumns.temperature]
        experiencePlaceNote1 = row[WorkResultColumns.experiencePlaceNote1]
        operatingMode = row[WorkResultColumns.operatingMode]
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[WorkResultColumns.allocationNo] = allocationNo
        container[WorkResultColumns.allocationRowNo] = allocationRowNo
        container[WorkResultColumns.workCd] = workCd
        container[WorkResultColumns.serviceOrder] = serviceOrder
        container[WorkResultColumns.status] = status
        container[WorkResultColumns.workDatetimeFrom] = workDatetimeFrom
        container[WorkResultColumns.workDatetimeTo] = workDatetimeTo
        container[WorkResultColumns.latitudeFrom] = latitudeFrom
        container[WorkResultColumns.longitudeFrom] = longitudeFrom
        container[WorkResultColumns.latitudeTo] = latitudeTo
        container[WorkResultColumns.longitudeTo] = longitudeTo
        container[WorkResultColumns.delayStatus] = delayStatus
        container[WorkResultColumns.delayReasonCd] = delayReasonCd
        container[WorkResultColumns.misdeliveryStatus] = misdeliveryStatus
        container[WorkResultColumns.orgAllocationRowNo] = orgAllocationRowNo
        container[WorkResultColumns.updatedDate] = updatedDate
        container[WorkResultColumns.companyCd] = companyCd
        container[WorkResultColumns.UserId] = UserId
        container[WorkResultColumns.temperature] = temperature
        container[WorkResultColumns.experiencePlaceNote1] = experiencePlaceNote1
        container[WorkResultColumns.operatingMode] = operatingMode
        
        super.encode(to: &container)
    }
}
