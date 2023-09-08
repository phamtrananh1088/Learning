//
//  BinHeader.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB
import CoreLocation

enum BinHeaderColumns: String, ColumnExpression {
    case allocationNo, allocationNm, binStatus, truckCd, updatedDate, unplannedFlag
    case destinationRowNo
    
    case startDatetime, endDatetime
    case driverNm, subDriverNm, allocationNote1
    
    case outgoingMeter, incomingMeter
    
    case startLatitude, startLongitude, startAccuracy, startDate
    case endLatitude, endLongitude, endAccuracy, endDate
    case weatherCd
}

// 運行情報ヘッダ

class BinHeader: BaseEntity, BinProtocol, Codable, Equatable, Hashable {
    
    static func == (lhs: BinHeader, rhs: BinHeader) -> Bool {
        return lhs.allocationNo == rhs.allocationNo
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(allocationNo)
    }
    
    var allocationNo:       String = Resources.strEmpty
    var allocationNm:       String = Resources.strEmpty
    var binStatus:          String = Resources.strEmpty { didSet { recordChanged() } }
    var truckCd:            String = Resources.strEmpty { didSet { recordChanged() } }
    var updatedDate:        Int64 = 0
    var unplannedFlag:      Bool = false
    var destinationRowNo:   Int? = 0 { didSet { recordChanged() } }
    
    var startDatetime:      Int64? = nil
    var endDatetime:        Int64? = nil
    
    var driverNm:           String? = nil
    var subDriverNm:        String? = nil
    var allocationNote1:    String? = nil
    
    var outgoingMeter:      Int? = nil { didSet { recordChanged() } }
    var incomingMeter:      Int? = nil { didSet { recordChanged() } }
    
    var startLatitude: Double?   = nil { didSet { recordChanged() } }
    var startLongitude: Double?  = nil { didSet { recordChanged() } }
    var startAccuracy: Double?   = nil { didSet { recordChanged() } }
    var startDate: Int64?        = nil { didSet { recordChanged() } }

    var endLatitude: Double?     = nil { didSet { recordChanged() } }
    var endLongitude: Double?    = nil { didSet { recordChanged() } }
    var endAccuracy: Double?     = nil { didSet { recordChanged() } }
    var endDate: Int64?          = nil { didSet { recordChanged() } }
    
    var weatherCd: Int?          = nil { didSet { recordChanged() } }
    
    override init() {
        super.init()
    }
    
    init(allocationNo: String,
         allocationNm: String,
         binStatus: String,
         truckCd: String,
         updatedDate: String?,
         unplannedFlag: Int?,
         
         planStartDatetime: String?,
         planEndDatetime: String?,
         drvStartDatetime: String?,
         drvEndDateTime: String?,
         
         driverNm: String?,
         subDriverNm: String?,
         allocationNote1: String?,
         outgoingMeter: Int?,
         incomingMeter: Int?
    ) {
        self.allocationNo = allocationNo
        self.allocationNm = allocationNm
        self.binStatus = binStatus
        self.truckCd = truckCd
        self.updatedDate = Int64((updatedDate.dateFromStringT()?.timeIntervalSince1970 ?? 0) * 1000)
        self.unplannedFlag = unplannedFlag == 1
        
        if let stDatetime = planStartDatetime.dateFromStringT() {
            self.startDatetime = stDatetime.milisecondsSince1970
        }
        
        if let eDatetime = planEndDatetime.dateFromStringT() {
            self.endDatetime = eDatetime.milisecondsSince1970
        }
        
        if let drvSDatetime = drvStartDatetime.dateFromStringT() {
            self.startDate = drvSDatetime.milisecondsSince1970
        }
        
        if let drvETime = drvEndDateTime.dateFromStringT() {
            self.endDate = drvETime.milisecondsSince1970
        }
        
        self.driverNm = driverNm
        self.subDriverNm = subDriverNm
        self.allocationNote1 = allocationNote1
        
        self.outgoingMeter = outgoingMeter
        self.incomingMeter = incomingMeter

        super.init()
    }

    override class var databaseTableName: String {"BinHeader"}
    
    override class var primaryKey: String {"allocationNo"}

    required init(row: Row) {
        allocationNo  = row[BinHeaderColumns.allocationNo]
        allocationNm  = row[BinHeaderColumns.allocationNm]
        binStatus  = row[BinHeaderColumns.binStatus]
        truckCd  = row[BinHeaderColumns.truckCd]
        updatedDate  = row[BinHeaderColumns.updatedDate]
        unplannedFlag  = row[BinHeaderColumns.unplannedFlag]
        destinationRowNo = row[BinHeaderColumns.destinationRowNo]
        
        startDatetime = row[BinHeaderColumns.startDatetime]
        endDatetime = row[BinHeaderColumns.endDatetime]
        
        driverNm = row[BinHeaderColumns.driverNm]
        subDriverNm = row[BinHeaderColumns.subDriverNm]
        allocationNote1 = row[BinHeaderColumns.allocationNote1]
        
        outgoingMeter  = row[BinHeaderColumns.outgoingMeter]
        incomingMeter  = row[BinHeaderColumns.incomingMeter]
        
        startLatitude = row[BinHeaderColumns.startLatitude]
        startLongitude = row[BinHeaderColumns.startLongitude]
        startAccuracy = row[BinHeaderColumns.startAccuracy]
        startDate = row[BinHeaderColumns.startDate]
        
        endLatitude = row[BinHeaderColumns.endLatitude]
        endLongitude = row[BinHeaderColumns.endLongitude]
        endAccuracy = row[BinHeaderColumns.endAccuracy]
        endDate = row[BinHeaderColumns.endDate]
        
        weatherCd = row[BinHeaderColumns.weatherCd]
        super.init(row: row)
    }

    override func encode(to container: inout PersistenceContainer) {
        container[BinHeaderColumns.allocationNo] = allocationNo
        container[BinHeaderColumns.allocationNm] = allocationNm
        container[BinHeaderColumns.binStatus] = binStatus
        container[BinHeaderColumns.truckCd] = truckCd
        container[BinHeaderColumns.updatedDate] = updatedDate
        container[BinHeaderColumns.unplannedFlag] = unplannedFlag
        container[BinHeaderColumns.destinationRowNo] = destinationRowNo
        
        container[BinHeaderColumns.startDatetime] = startDatetime
        container[BinHeaderColumns.endDatetime] = endDatetime
        
        container[BinHeaderColumns.driverNm] = driverNm
        container[BinHeaderColumns.subDriverNm] = subDriverNm
        container[BinHeaderColumns.allocationNote1] = allocationNote1
        
        container[BinHeaderColumns.outgoingMeter] = outgoingMeter
        container[BinHeaderColumns.incomingMeter] = incomingMeter
        
        container[BinHeaderColumns.startLatitude] = startLatitude
        container[BinHeaderColumns.startLongitude] = startLongitude
        container[BinHeaderColumns.startAccuracy] = startAccuracy
        container[BinDetailColumns.startDate] = startDate

        container[BinHeaderColumns.endLatitude] = endLatitude
        container[BinHeaderColumns.endLongitude] = endLongitude
        container[BinHeaderColumns.endAccuracy] = endAccuracy
        container[BinHeaderColumns.endDate] = endDate
        
        container[BinHeaderColumns.weatherCd] = weatherCd
        super.encode(to: &container)
    }
    
    func startBin(location: CLLocation?) {
        binStatus = BinStatusEnum.Working.rawValue
        startLatitude = location?.coordinate.latitude
        startLongitude = location?.coordinate.longitude
        startAccuracy = location?.horizontalAccuracy
        startDate = currentTimeMillis()
    }
    
    /**
     * 運行終了から
     */
    func endBin(weather: WeatherEnum, location: CLLocation?) {
        binStatus = BinStatusEnum.Finished.rawValue
        weatherCd = weather.rawValue
        endLatitude = location?.coordinate.latitude
        endLongitude = location?.coordinate.longitude
        endAccuracy = location?.horizontalAccuracy
        endDate = currentTimeMillis()
    }
}
