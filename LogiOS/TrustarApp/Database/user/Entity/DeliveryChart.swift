//
//  DeliveryChart.swift
//  TrustarApp
//
//  Created by hoanx on 8/6/23.
//  Copyright c 2023 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

enum DeliveryChartColumns: String, ColumnExpression {
    case chartCd, placeCd, dest, addr1, addr2, tel, carrier, carrierTel, memos, images, lastAllocationNo, lastAllocationRowNo, extra
}

class DeliveryChart: BaseEntity, Codable {
    var chartCd: String = Resources.strEmpty { didSet { recordChanged() } }
    var placeCd: String = Resources.strEmpty { didSet { recordChanged() } }
    
    var dest: String = Resources.strEmpty { didSet { recordChanged() } }
    var addr1: String = Resources.strEmpty { didSet { recordChanged() } }
    var addr2: String = Resources.strEmpty { didSet { recordChanged() } }
    var tel: String = Resources.strEmpty { didSet { recordChanged() } }
    var carrier: String = Resources.strEmpty { didSet { recordChanged() } }
    var carrierTel: String = Resources.strEmpty { didSet { recordChanged() } }
    var memos: String = Resources.strEmpty { didSet { recordChanged() } }
    var images: String = Resources.strEmpty { didSet { recordChanged() } }
    //var last_allocation_row = Allocationrow
    var lastAllocationNo: String = Resources.strEmpty { didSet { recordChanged() } }
    var lastAllocationRowNo: Int? = Resources.zeroNumber { didSet { recordChanged() } }
    var extra: String? = Resources.strEmpty
    
    override init() {
        super.init()
    }
    

    override class var databaseTableName: String {"DeliveryChart"}
    override class var primaryKey: String {"chartCd"}
    
    required init(row: Row) {
        chartCd = row[DeliveryChartColumns.chartCd]
        placeCd = row[DeliveryChartColumns.placeCd]
        
        dest = row[DeliveryChartColumns.dest]
        addr1 = row[DeliveryChartColumns.addr1]
        addr2 = row[DeliveryChartColumns.addr2]
        tel = row[DeliveryChartColumns.tel]
        carrier = row[DeliveryChartColumns.carrier]
        carrierTel = row[DeliveryChartColumns.carrierTel]
        memos = row[DeliveryChartColumns.memos]
        images = row[DeliveryChartColumns.images]
        lastAllocationNo = row[DeliveryChartColumns.lastAllocationNo]
        lastAllocationRowNo = row[DeliveryChartColumns.lastAllocationRowNo]
        extra = row[DeliveryChartColumns.extra]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[DeliveryChartColumns.chartCd] = chartCd
        container[DeliveryChartColumns.placeCd] = placeCd
        container[DeliveryChartColumns.dest] = dest
        container[DeliveryChartColumns.addr1] = addr1
        container[DeliveryChartColumns.addr2] = addr2
        container[DeliveryChartColumns.tel] = tel
        container[DeliveryChartColumns.carrier] = carrier
        container[DeliveryChartColumns.carrierTel] = carrierTel
        container[DeliveryChartColumns.memos] = memos
        container[DeliveryChartColumns.images] = images
        container[DeliveryChartColumns.lastAllocationNo] = lastAllocationNo
        container[DeliveryChartColumns.lastAllocationRowNo] = lastAllocationRowNo
        container[DeliveryChartColumns.extra] = extra
        
        super.encode(to: &container)
    }
    
    func address() -> String {
        return "\(addr1) \(addr2)".trimmingCharacters(in: .whitespacesAndNewlines)
    }
    
    func getImages () -> [ChartImageFile]? {
        do {
            if images.isEmpty {
                return nil
            }
            let decoder = JSONDecoder()
            return try decoder.decode([ChartImageFile].self, from: images.data(using: .utf8)!)
        } catch {
            return nil
        }
    }
    
    func getChartMemos () -> [ChartMemos]? {
        do {
            if memos.isEmpty {
                return nil
            }
            let decoder = JSONDecoder()
            return try decoder.decode([ChartMemos].self, from: memos.data(using: .utf8)!)
        } catch {
            return nil
        }
    }
}

class ChartImageFile : Decodable, Encodable {
    enum CodingKeys : CodingKey {
        case dbStoreFile
        case extra
    }
    var dbStoreFile: DbFileRecord? = nil
    var extra: String?
    
    init() {

    }
    
    init(dbStoreFile: DbFileRecord?, extra: String?) {
        self.dbStoreFile = dbStoreFile
        self.extra = extra
    }
    
    required init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        if let v = try? values.decode(DbFileRecord.self, forKey: .dbStoreFile) {
            dbStoreFile = v
        }
        
        if let v = try? values.decode(String.self, forKey: .extra) {
            extra = v
        }
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(dbStoreFile, forKey: .dbStoreFile)
        try container.encode(extra, forKey: .extra)
    }
}

class ChartMemos : Decodable, Encodable, Identifiable {
    public let id = UUID()
    enum CodingKeys : CodingKey {
        case label
        case note
        case higtlight
        case extra
    }
    var label: String = Resources.strEmpty
    var note: String = Resources.strEmpty
    var higtlight: Bool = false
    var extra: String?
    
    init() {
    }
    
    init(label: String, note: String, higtlight: Bool, extra: String?) {
        self.label = label
        self.note = note
        self.higtlight = higtlight
        self.extra = extra
    }
    
    required init(from decoder: Decoder) throws {
        let values = try decoder.container(keyedBy: CodingKeys.self)
        if let v = try? values.decode(String.self, forKey: .label) {
            label = v
        }
        if let v = try? values.decode(String.self, forKey: .note) {
            note = v
        }
        if let v = try? values.decode(Bool.self, forKey: .higtlight) {
            higtlight = v
        }
        if let v = try? values.decode(String.self, forKey: .extra) {
            extra = v
        }
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(label, forKey: .label)
        try container.encode(note, forKey: .note)
        try container.encode(higtlight, forKey: .higtlight)
        try container.encode(extra, forKey: .extra)
    }
}

class ExtraHeader: Codable {
    var mkTimeStamp: String?
    var mkUserId: String?
    var mkPrgNm: String?

    init(mkTimeStamp: String?, mkUserId: String?, mkPrgNm: String?) {
        self.mkTimeStamp = mkTimeStamp
        self.mkUserId = mkUserId
        self.mkPrgNm = mkPrgNm
    }
}

class ExtraCm: Codable {
    var MkTimeStamp: String?
    var MkUserId: String?
    var MkPrgNm: String?

    init(MkTimeStamp: String?, MkUserId: String?, MkPrgNm: String?) {
        self.MkTimeStamp = MkTimeStamp
        self.MkUserId = MkUserId
        self.MkPrgNm = MkPrgNm
    }
}


