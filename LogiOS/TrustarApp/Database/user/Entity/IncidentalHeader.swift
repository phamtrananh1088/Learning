//
//  IncidentalHeader.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum IncidentalHeaderColumns: String, ColumnExpression {
    case uuid, workList, deleted
    case allocationNo, allocationRowNo, sheetNo, shipperCd
    case status, picId, terminalId, localSeq
}

class IncidentalHeader: BaseEntity, Codable, Equatable, Hashable {
    static func == (lhs: IncidentalHeader, rhs: IncidentalHeader) -> Bool {
        return lhs.uuid == rhs.uuid &&
        lhs.allocationNo == rhs.allocationNo &&
        lhs.allocationRowNo == rhs.allocationRowNo &&
        lhs.sheetNo == rhs.sheetNo &&
        lhs.shipperCd == rhs.shipperCd
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(uuid)
        hasher.combine(allocationNo)
        hasher.combine(allocationRowNo)
        hasher.combine(sheetNo)
        hasher.combine(shipperCd)
    }
    
    var uuid: String            = Resources.strEmpty
    var allocationNo: String    = Resources.strEmpty
    var allocationRowNo: Int    = Resources.zeroNumber
    var sheetNo: String?        = nil
    var shipperCd: String       = Resources.strEmpty { didSet { recordChanged() } }
    
    var status: Int             = Resources.zeroNumber { didSet { recordChanged() } }
    var picId: String?          = nil { didSet { recordChanged() } }
    var terminalId: String      = Resources.strEmpty
    var localSeq: String        = Resources.strEmpty
    // workList is List, split by "{,}"
    var workList: String        = Resources.strEmpty { didSet { recordChanged() } }
    var deleted: Bool           = false { didSet { recordChanged() } }
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"IncidentalHeader"}
    override class var primaryKey: String {"uuid"}
    
    required init(row: Row) {
        uuid = row[IncidentalHeaderColumns.uuid]
        allocationNo = row[IncidentalHeaderColumns.allocationNo]
        allocationRowNo = row[IncidentalHeaderColumns.allocationRowNo]
        sheetNo = row[IncidentalHeaderColumns.sheetNo]
        shipperCd = row[IncidentalHeaderColumns.shipperCd]
        status = row[IncidentalHeaderColumns.status]
        picId = row[IncidentalHeaderColumns.picId]
        terminalId = row[IncidentalHeaderColumns.terminalId]
        localSeq = row[IncidentalHeaderColumns.localSeq]
        workList = row[IncidentalHeaderColumns.workList]
        deleted = row[IncidentalHeaderColumns.deleted]
        super.init(row: row)
    }
    
    init(uuid: String, sheetNo: String?, allocationNo: String,
         allocationRowNo: Int, shipperCd: String,
         workList: [String], status: Int, picId: String?) {
        self.uuid = uuid
        self.sheetNo = sheetNo
        self.allocationNo = allocationNo
        self.allocationRowNo = allocationRowNo
        self.shipperCd = shipperCd
        self.workList = workList.joined(separator: ",")
        self.status = status
        self.picId = picId
        super.init()
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[IncidentalHeaderColumns.uuid] = uuid
        container[IncidentalHeaderColumns.allocationNo] = allocationNo
        container[IncidentalHeaderColumns.allocationRowNo] = allocationRowNo
        container[IncidentalHeaderColumns.sheetNo] = sheetNo
        container[IncidentalHeaderColumns.shipperCd] = shipperCd
        container[IncidentalHeaderColumns.status] = status
        container[IncidentalHeaderColumns.picId] = picId
        container[IncidentalHeaderColumns.terminalId] = terminalId
        container[IncidentalHeaderColumns.localSeq] = localSeq
        container[IncidentalHeaderColumns.workList] = workList
        container[IncidentalHeaderColumns.deleted] = deleted
        
        super.encode(to: &container)
    }
    
    func localSignatureId() -> String {
            return "incidental:\(uuid):\(picId ?? "")"
    }

    func setSignStatus(isSigned: Bool) {
        self.status = isSigned ? 1 : 0
    }
}
