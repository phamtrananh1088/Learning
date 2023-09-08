//
//  BinDetail.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB
import CoreLocation

enum BinDetailColumns: String, ColumnExpression {
    case allocationNo, allocationRowNo, allocationPlanFlag, workCd, workNm, serviceOrder, workPlanDate, appointedDateFrom, appointedDateTo, placeLatitude, placeLongitude, placeCd, placeNm1, placeNm2, placeAddr, placeZip, placeTel1, placeMail1, placeTel2, placeMail2, noteNoticeClass, placeNote1, placeNote2, placeNote3, status, operationOrder, delayCheckFlag, delayToleranceFrom, delayToleranceTo, delayStatus, delayReasonCd, delayRank, origAllocationRowNo, misdeliveryCheckFlag, misdeliveryMeterTo, misdeliveryStatus, updatedDate, chartCd, temperature, experiencePlaceNote1, stayTime
    case startLatitude, startLongitude, startAccuracy, startDate
    case endLatitude, endLongitude, endAccuracy, endDate
    case autoModeFlag
}

// 運行情報明細
class BinDetail: BaseEntity, BinProtocol , Codable {
    var allocationNo:         String  = Resources.strEmpty
    var allocationRowNo:      Int     = Resources.zeroNumber
    var allocationPlanFlag:   Int     = Resources.zeroNumber
    var workCd:               String? = nil { didSet { recordChanged() } }
    var workNm:               String? = nil { didSet { recordChanged() } }
    var serviceOrder:         Int?    = nil
    var workPlanDate:         String? = nil
    var appointedDateFrom:    Int64?  = nil
    var appointedDateTo:      Int64?  = nil
    var placeLatitude:        Double? = nil { didSet { recordChanged() } }
    var placeLongitude:       Double? = nil { didSet { recordChanged() } }
    var placeCd:              String? = nil 
    var placeNm1:             String? = nil
    var placeNm2:             String? = nil
    var placeAddr:            String? = nil
    var placeZip:             String? = nil
    var placeTel1:            String? = nil
    var placeMail1:           String? = nil
    var placeTel2:            String? = nil
    var placeMail2:           String? = nil
    var noteNoticeClass:      String? = Resources.strEmpty
    var placeNote1:           String? = nil
    var placeNote2:           String? = nil
    var placeNote3:           String? = nil
    var status:               Int     = Resources.zeroNumber { didSet { recordChanged() } }
    var operationOrder:       Int?    = nil
    
    var delayCheckFlag:       String = Resources.strEmpty
    var delayToleranceFrom:   String = Resources.strEmpty
    var delayToleranceTo:     String = Resources.strEmpty
    var delayStatus:          Int    = Resources.zeroNumber { didSet { recordChanged() } }
    var delayReasonCd:        String? = nil { didSet { recordChanged() } }
   
    var delayRank:            String? = nil
    var origAllocationRowNo:  Int? = nil
    var misdeliveryCheckFlag: String = Resources.strEmpty
    var misdeliveryMeterTo:   String = Resources.strEmpty
    var misdeliveryStatus:    String = Resources.strEmpty { didSet { recordChanged() } }
    var updatedDate:          Int64? = nil
    var chartCd:              String? = Resources.strEmpty
    var temperature:          String? = nil { didSet { recordChanged() } }
    var experiencePlaceNote1: String? = nil { didSet { recordChanged() } }
    var stayTime:             Int = Resources.zeroNumber
    
    var startLatitude: Double?        = nil { didSet { recordChanged() } }
    var startLongitude: Double?       = nil { didSet { recordChanged() } }
    var startAccuracy: Double?        = nil { didSet { recordChanged() } }
    var startDate: Int64?             = nil { didSet { recordChanged() } }

    var endLatitude: Double?          = nil { didSet { recordChanged() } }
    var endLongitude: Double?         = nil { didSet { recordChanged() } }
    var endAccuracy: Double?          = nil { didSet { recordChanged() } }
    var endDate: Int64?               = nil { didSet { recordChanged() } }
    
    var autoModeFlag: Bool            = false

    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"BinDetail"}
    
    override class var primaryKey: String {"allocationNo,allocationRowNo"}
    
    required init(row: Row) {
        allocationNo = row[BinDetailColumns.allocationNo]
        allocationRowNo = row[BinDetailColumns.allocationRowNo]
        allocationPlanFlag = row[BinDetailColumns.allocationPlanFlag]
        workCd = row[BinDetailColumns.workCd]
        workNm = row[BinDetailColumns.workNm]
        serviceOrder = row[BinDetailColumns.serviceOrder]
        workPlanDate = row[BinDetailColumns.workPlanDate]
        appointedDateFrom = row[BinDetailColumns.appointedDateFrom]
        appointedDateTo = row[BinDetailColumns.appointedDateTo]
        placeLatitude = row[BinDetailColumns.placeLatitude]
        placeLongitude = row[BinDetailColumns.placeLongitude]
        placeCd = row[BinDetailColumns.placeCd]
        placeNm1 = row[BinDetailColumns.placeNm1]
        placeNm2 = row[BinDetailColumns.placeNm2]
        placeAddr = row[BinDetailColumns.placeAddr]
        placeZip = row[BinDetailColumns.placeZip]
        placeTel1 = row[BinDetailColumns.placeTel1]
        placeMail1 = row[BinDetailColumns.placeMail1]
        placeTel2 = row[BinDetailColumns.placeTel2]
        placeMail2 = row[BinDetailColumns.placeMail2]
        noteNoticeClass = row[BinDetailColumns.noteNoticeClass]
        placeNote1 = row[BinDetailColumns.placeNote1]
        placeNote2 = row[BinDetailColumns.placeNote2]
        placeNote3 = row[BinDetailColumns.placeNote3]
        status = row[BinDetailColumns.status]
        operationOrder = row[BinDetailColumns.operationOrder]
        delayCheckFlag = row[BinDetailColumns.delayCheckFlag]
        delayToleranceFrom = row[BinDetailColumns.delayToleranceFrom]
        delayToleranceTo = row[BinDetailColumns.delayToleranceTo]
        delayStatus = row[BinDetailColumns.delayStatus]
        delayReasonCd = row[BinDetailColumns.delayReasonCd]
        delayRank = row[BinDetailColumns.delayRank]
        origAllocationRowNo = row[BinDetailColumns.origAllocationRowNo]
        misdeliveryCheckFlag = row[BinDetailColumns.misdeliveryCheckFlag]
        misdeliveryMeterTo = row[BinDetailColumns.misdeliveryMeterTo]
        misdeliveryStatus = row[BinDetailColumns.misdeliveryStatus]
        updatedDate = row[BinDetailColumns.updatedDate]
        chartCd = row[BinDetailColumns.chartCd]
        temperature = row[BinDetailColumns.temperature]
        experiencePlaceNote1 = row[BinDetailColumns.experiencePlaceNote1]
        stayTime = row[BinDetailColumns.stayTime]
        
        startLatitude = row[BinDetailColumns.startLatitude]
        startLongitude = row[BinDetailColumns.startLongitude]
        startAccuracy = row[BinDetailColumns.startAccuracy]
        startDate = row[BinDetailColumns.startDate]
        
        endLatitude = row[BinDetailColumns.endLatitude]
        endLongitude = row[BinDetailColumns.endLongitude]
        endAccuracy = row[BinDetailColumns.endAccuracy]
        endDate = row[BinDetailColumns.endDate]
        
        autoModeFlag = row[BinDetailColumns.autoModeFlag]
        
        super.init(row: row)
    }
    
    init(allocationNo: String, allocationRowNo: Int, allocationPlanFlag: Int, workCd: String?, workNm: String?, serviceOrder: Int?, appointedDateFrom: Int64?, appointedDateTo: Int64?, placeLatitude: Double?, placeLongitude: Double?, placeCd: String?, placeNm1: String?, placeNm2: String?, placeAddr: String?, placeZip: String?, placeTel1: String?, placeMail1: String?, placeTel2: String?, placeMail2: String?, noteNoticeClass: String?, placeNote1: String?, placeNote2: String?, placeNote3: String?, status: Int, delayCheckFlag: String, delayToleranceFrom: String, delayToleranceTo: String, delayStatus: Int, misdeliveryCheckFlag: String, misdeliveryMeterTo: String, stayTime: Int, misdeliveryStatus: String, operationOrder: Int?, delayReasonCd: String?, delayRank: String?, origAllocationRowNo: Int?, startLatitude: Double?, startLongitude: Double?, startAccuracy: Double?, startDate: Int64?, endLatitude: Double?, endLongitude: Double?, endAccuracy: Double?, endDate: Int64?, temperature: String?, experiencePlaceNote1: String?, updatedDate: Int64?, chartCd: String?) {
        
        self.allocationNo = allocationNo
        self.allocationRowNo = allocationRowNo
        self.allocationPlanFlag = allocationPlanFlag
        self.workCd = workCd
        self.workNm = workNm
        self.serviceOrder = serviceOrder
        self.appointedDateFrom = appointedDateFrom
        self.appointedDateTo = appointedDateTo
        self.placeLatitude = placeLatitude
        self.placeLongitude = placeLongitude
        self.placeNm1 = placeNm1
        self.placeNm2 = placeNm2
        self.placeAddr = placeAddr
        self.placeZip = placeZip
        self.placeTel1 = placeTel1
        self.placeMail1 = placeMail1
        self.placeTel2 = placeTel2
        self.placeMail2 = placeMail2
        self.noteNoticeClass = noteNoticeClass
        self.placeNote1 = placeNote1
        self.placeNote2 = placeNote2
        self.placeNote3 = placeNote3
        self.status = status
        self.delayCheckFlag = delayCheckFlag
        self.delayToleranceFrom = delayToleranceFrom
        self.delayToleranceTo = delayToleranceTo
        self.delayStatus = delayStatus
        self.misdeliveryCheckFlag = misdeliveryCheckFlag
        self.misdeliveryMeterTo = misdeliveryMeterTo
        self.stayTime = stayTime
        self.misdeliveryStatus = misdeliveryStatus
        self.operationOrder = operationOrder
        self.delayReasonCd = delayReasonCd
        self.delayRank = delayRank
        self.origAllocationRowNo = origAllocationRowNo
        self.startLatitude = startLatitude
        self.startLongitude = startLongitude
        self.startAccuracy = startAccuracy
        self.startDate = startDate
        self.endLatitude = endLatitude
        self.endLongitude = endLongitude
        self.endAccuracy = endAccuracy
        self.endDate = endDate
        self.temperature = temperature
        self.experiencePlaceNote1 = experiencePlaceNote1
        self.chartCd = chartCd
        super.init()
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[BinDetailColumns.allocationNo] = allocationNo
        container[BinDetailColumns.allocationRowNo] = allocationRowNo
        container[BinDetailColumns.allocationPlanFlag] = allocationPlanFlag
        container[BinDetailColumns.workCd] = workCd
        container[BinDetailColumns.workNm] = workNm
        container[BinDetailColumns.serviceOrder] = serviceOrder
        container[BinDetailColumns.workPlanDate] = workPlanDate
        container[BinDetailColumns.appointedDateFrom] = appointedDateFrom
        container[BinDetailColumns.appointedDateTo] = appointedDateTo
        container[BinDetailColumns.placeLatitude] = placeLatitude
        container[BinDetailColumns.placeLongitude] = placeLongitude
        container[BinDetailColumns.placeCd] = placeCd
        container[BinDetailColumns.placeNm1] = placeNm1
        container[BinDetailColumns.placeNm2] = placeNm2
        container[BinDetailColumns.placeAddr] = placeAddr
        container[BinDetailColumns.placeZip] = placeZip
        container[BinDetailColumns.placeTel1] = placeTel1
        container[BinDetailColumns.placeMail1] = placeMail1
        container[BinDetailColumns.placeTel2] = placeTel2
        container[BinDetailColumns.placeMail2] = placeMail2
        container[BinDetailColumns.noteNoticeClass] = noteNoticeClass
        container[BinDetailColumns.placeNote1] = placeNote1
        container[BinDetailColumns.placeNote2] = placeNote2
        container[BinDetailColumns.placeNote3] = placeNote3
        container[BinDetailColumns.status] = status
        container[BinDetailColumns.operationOrder] = operationOrder
        container[BinDetailColumns.delayCheckFlag] = delayCheckFlag
        container[BinDetailColumns.delayToleranceFrom] = delayToleranceFrom
        container[BinDetailColumns.delayToleranceTo] = delayToleranceTo
        container[BinDetailColumns.delayStatus] = delayStatus
        container[BinDetailColumns.delayReasonCd] = delayReasonCd
        container[BinDetailColumns.delayRank] = delayRank
        container[BinDetailColumns.origAllocationRowNo] = origAllocationRowNo
        container[BinDetailColumns.misdeliveryCheckFlag] = misdeliveryCheckFlag
        container[BinDetailColumns.misdeliveryMeterTo] = misdeliveryMeterTo
        container[BinDetailColumns.misdeliveryStatus] = misdeliveryStatus
        container[BinDetailColumns.updatedDate] = updatedDate
        container[BinDetailColumns.chartCd] = chartCd
        container[BinDetailColumns.temperature] = temperature
        container[BinDetailColumns.experiencePlaceNote1] = experiencePlaceNote1
        container[BinDetailColumns.stayTime] = stayTime
        
        container[BinDetailColumns.startLatitude] = startLatitude
        container[BinDetailColumns.startLongitude] = startLongitude
        container[BinDetailColumns.startAccuracy] = startAccuracy
        container[BinDetailColumns.startDate] = startDate

        container[BinDetailColumns.endLatitude] = endLatitude
        container[BinDetailColumns.endLongitude] = endLongitude
        container[BinDetailColumns.endAccuracy] = endAccuracy
        container[BinDetailColumns.endDate] = endDate
        
        container[BinDetailColumns.autoModeFlag] = autoModeFlag
        
        super.encode(to: &container)
    }
    
    /* 予定外作業なら運行情報明細を登録する */
    init(binDetail: BinDetail, allocationRowNo: Int, operationOrder: Int, workCd: String?, workNm: String?) {
        self.allocationNo = binDetail.allocationNo
        self.allocationRowNo = allocationRowNo
        self.allocationPlanFlag = 0
        self.workCd = workCd
        self.workNm = workNm
        self.serviceOrder = nil
        self.appointedDateFrom = binDetail.appointedDateFrom
        self.appointedDateTo = binDetail.appointedDateTo
        self.placeLatitude = binDetail.placeLatitude
        self.placeLongitude = binDetail.placeLongitude
        self.placeCd = binDetail.placeCd
        self.placeNm1 = binDetail.placeNm1
        self.placeNm2 = binDetail.placeNm2
        self.placeAddr = binDetail.placeAddr
        self.placeZip = binDetail.placeZip
        self.placeTel1 = binDetail.placeTel1
        self.placeMail1 = binDetail.placeMail1
        self.placeTel2 = binDetail.placeTel2
        self.placeMail2 = binDetail.placeMail2
        self.noteNoticeClass = "0"
        self.placeNote1 = binDetail.placeNote1
        self.placeNote2 = binDetail.placeNote2
        self.placeNote3 = binDetail.placeNote3
        self.status = 0
        self.delayCheckFlag = "0"
        self.delayToleranceFrom = binDetail.delayToleranceFrom
        self.delayToleranceTo = binDetail.delayToleranceTo
        self.delayStatus = 0
        self.misdeliveryCheckFlag = binDetail.misdeliveryCheckFlag
        self.misdeliveryMeterTo = binDetail.misdeliveryMeterTo
        self.stayTime = binDetail.stayTime
        self.misdeliveryStatus = "0"
        self.operationOrder = operationOrder
        self.delayReasonCd = nil
        self.delayRank = nil
        self.origAllocationRowNo = binDetail.allocationRowNo
        self.startLatitude = nil
        self.startLongitude = nil
        self.startAccuracy = nil
        self.startDate = nil
        self.endLatitude = nil
        self.endLongitude = nil
        self.endAccuracy = nil
        self.endDate = nil
        self.temperature = nil
        self.experiencePlaceNote1 = nil
        self.updatedDate = currentTimeMillis()
        self.chartCd = nil
        super.init()
        
        recordChanged()
    }
    
    static func stayTimeDelayinmin(binDetail: BinDetail) -> Int {
        if (binDetail.unplaned()) {
            return Current.Shared.loggedUser!.userInfo.stayTime
        } else {
            return binDetail.stayTime
        }
    }
    
    /** 配送計画一覧からの追加作業 */
    static func newbindetail(allocationNo: String,
                      allocationRowNo: Int,
                      operationOrder: Int,
                      work: Work,
                      placeNm1: String?, placeNm2: String?, placeAddr: String?
    ) -> BinDetail {
        let b =
        BinDetail(allocationNo: allocationNo,
                  allocationRowNo: allocationRowNo,
                  allocationPlanFlag: 0,
                  workCd: work.workCd,
                  workNm: work.workNm,
                  serviceOrder: nil,
                  appointedDateFrom: nil,
                  appointedDateTo: nil,
                  placeLatitude: nil,
                  placeLongitude: nil,
                  placeCd: nil, placeNm1: placeNm1, placeNm2: placeNm2, placeAddr: placeAddr,
                  placeZip: nil, placeTel1: nil, placeMail1: nil, placeTel2: nil, placeMail2: nil,
                  noteNoticeClass: "0",
                  placeNote1: nil, placeNote2: nil, placeNote3: nil,
                  status: 0,
                  delayCheckFlag: "0",
                  delayToleranceFrom: "0",
                  delayToleranceTo: "0",
                  delayStatus: 0,
                  misdeliveryCheckFlag: "0",
                  misdeliveryMeterTo: "0",
                  stayTime: 0,
                  misdeliveryStatus: "0",
                  operationOrder: operationOrder,
                  delayReasonCd: nil,
                  delayRank: nil,
                  origAllocationRowNo: 0,
                  startLatitude: nil, startLongitude: nil, startAccuracy: nil, startDate: nil,
                  endLatitude: nil, endLongitude: nil, endAccuracy: nil, endDate: nil,
                  temperature: nil,
                  experiencePlaceNote1: nil,
                  updatedDate: currentTimeMillis(),
                  chartCd: nil)
        b.recordChanged()
        return b
    }
    
    func unplaned() -> Bool {
        return allocationPlanFlag == 0
    }
    
    func hasNotice() -> Bool {
        return noteNoticeClass != "0"
    }
    
    func displayPlanTime() -> String? {
        if appointedDateFrom == nil || appointedDateTo == nil {
            return nil
        } else {
            return displayHHmmRange(from: appointedDateFrom!, to: appointedDateTo)
        }
    }
    
    func displayWorkTime() -> String! {
        if let from = startDate {
            return displayHHmmRange(from: from, to: endDate)
        }
        
        return nil
    }
    
    func placeNameFull() -> String {
        return "\(placeNm1.orEmpty()) \(placeNm2.orEmpty())"
    }
    
    /*
    * 距離と範囲内
    */
    func checkDeliveryDeviation(compare: CLLocation) -> (Int, Bool)? {
        
        if placeLatitude == nil && placeLongitude == nil {
             return nil
        }
        
        let pl = CLLocation(latitude: placeLatitude!, longitude: placeLongitude!)
        let d = Int(compare.distance(from: pl).rounded())
        let m = unplaned() ? Current.Shared.loggedUser!.userInfo.misdeliveryMeterTo : misdeliveryMeterTo
        
        return (d, d < Int(m)!)
    }
    
    /**
     * 早配
     */
    func checkIfEarlyDelivery(byDate: Int64) -> Bool {
        let ap = appointedDateFrom
        if ap == nil || unplaned() || delayCheckFlag == "0" {
            return false
        }
        
        let d = byDate - ap!
        return d / 60_000 < Int(delayToleranceFrom)!
    }
    
    /**
     * 遅配
     */
    func checkIfDelayedDelivery(byDate: Int64) -> Bool {
        let ap = appointedDateFrom
        if ap == nil || unplaned() || delayCheckFlag == "0" {
            return false
        }
        
        let d = byDate - ap!
        return d / 60_000 > Int(delayToleranceTo)!
    }
    
    /**
    * 誤配
    */
    func checkIfMisDelivered(compare: CLLocation) -> Bool {
        if origAllocationRowNo == 0 || misdeliveryCheckFlag == "0" {
            return false
        }
        
        if let check = checkDeliveryDeviation(compare: compare) {
            return !check.1
        } else {
            return false
        }
    }
    
    func getStatus() -> BinDetailEnum {
        return BinDetailEnum.init(rawValue: status)!
    }
    
    func setWorkStart(location: CLLocation?, time: Int64) {
        if location != nil {
            startLatitude = location!.coordinate.latitude
            startLongitude = location!.coordinate.longitude
            startAccuracy = location!.horizontalAccuracy
        }
        
        startDate = time
    }
    
    func isDelayed() -> Bool {
        return delayStatus != 0
    }
    
    func setWorkEnd(location: CLLocation?, time: Int64) {
        if location != nil {
            endLatitude = location!.coordinate.latitude
            endLongitude = location!.coordinate.longitude
            endAccuracy = location!.horizontalAccuracy
        }
        
        endDate = time
    }
    
    func operatedInAutoMode() {
        autoModeFlag = true
        recordChanged()
    }
}
