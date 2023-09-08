//
//  CommonDeliveryChart.swift
//  TrustarApp
//
//  Created by hoanx on 8/9/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import GRDB
import CoreLocation

enum CommonDeliveryChartColumns: String, ColumnExpression, CaseIterable {
    case companyCd, userId, chartCd, placeCd, dest, addr1, addr2, tel, carrier, carrierTel, memos, images, lastAllocationNo, lastAllocationRowNo, extra
    
    func name() -> String {
        return String(describing: self)
    }
}

class CommonDeliveryChart: BaseEntity, Codable {
    var companyCd: String = Resources.strEmpty
    var userId: String = Resources.strEmpty
    var chartCd: String = Resources.strEmpty
    var placeCd: String? = Resources.strEmpty
    
    var dest: String? = Resources.strEmpty
    var addr1: String? = Resources.strEmpty
    var addr2: String? = Resources.strEmpty
    var tel: String? = Resources.strEmpty
    var carrier: String? = Resources.strEmpty
    var carrierTel: String? = Resources.strEmpty
    var memos: String = Resources.strEmpty
    var images: String = Resources.strEmpty
    var lastAllocationNo: String? = Resources.strEmpty
    var lastAllocationRowNo: Int? = Resources.zeroNumber
    var extra: String? = Resources.strEmpty
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"CommonDeliveryChart"}
    override class var primaryKey: String { "companyCd,userId,chartCd" }
    
    required init(row: Row) {
        companyCd = row[CommonDeliveryChartColumns.companyCd]
        userId = row[CommonDeliveryChartColumns.userId]
        chartCd = row[CommonDeliveryChartColumns.chartCd]
        placeCd = row[CommonDeliveryChartColumns.placeCd]
        
        dest = row[CommonDeliveryChartColumns.dest]
        addr1 = row[CommonDeliveryChartColumns.addr1]
        addr2 = row[CommonDeliveryChartColumns.addr2]
        tel = row[CommonDeliveryChartColumns.tel]
        carrier = row[CommonDeliveryChartColumns.carrier]
        carrierTel = row[CommonDeliveryChartColumns.carrierTel]
        memos = row[CommonDeliveryChartColumns.memos]
        images = row[CommonDeliveryChartColumns.images]
        lastAllocationNo = row[CommonDeliveryChartColumns.lastAllocationNo]
        lastAllocationRowNo = row[CommonDeliveryChartColumns.lastAllocationRowNo]
        extra = row[CommonDeliveryChartColumns.extra]
        
        super.init(row: row)
    }
    
    init(userInfo: UserInfo, result: DeliveryChart, images: String) {
        companyCd = userInfo.companyCd
        userId = userInfo.userId
        chartCd = result.chartCd
        placeCd = result.placeCd
        
        dest = result.dest
        addr1 = result.addr1
        addr2 = result.addr2

        tel = result.tel
        carrier = result.carrier
        lastAllocationNo = result.lastAllocationNo
        lastAllocationRowNo = result.lastAllocationRowNo
        memos = result.memos
        self.images = images
        carrierTel = result.carrierTel
        extra = result.extra
        super.init()
        self.recordChanged()
    }

    
    override func encode(to container: inout PersistenceContainer) {
        container[CommonDeliveryChartColumns.companyCd] = companyCd
        container[CommonDeliveryChartColumns.userId] = userId
        container[CommonDeliveryChartColumns.chartCd] = chartCd
        container[CommonDeliveryChartColumns.placeCd] = placeCd
        container[CommonDeliveryChartColumns.dest] = dest
        container[CommonDeliveryChartColumns.addr1] = addr1
        container[CommonDeliveryChartColumns.addr2] = addr2
        container[CommonDeliveryChartColumns.tel] = tel
        container[CommonDeliveryChartColumns.carrier] = carrier
        container[CommonDeliveryChartColumns.carrierTel] = carrierTel
        container[CommonDeliveryChartColumns.memos] = memos
        container[CommonDeliveryChartColumns.images] = images
        container[CommonDeliveryChartColumns.lastAllocationNo] = lastAllocationNo
        container[CommonDeliveryChartColumns.lastAllocationRowNo] = lastAllocationRowNo
        container[CommonDeliveryChartColumns.extra] = extra
        
        super.encode(to: &container)
    }
       
    func getImages () -> [ChartImageFile]? {
        do {
            if images.isEmpty {
                return nil
            }
            let decoder = JSONDecoder()
            return try decoder.decode([ChartImageFile].self, from: images.data(using: .utf8)!)
        } catch {
            debugPrint(error)
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
            debugPrint(error)
            return nil
        }
    }
    
    func getExtraHeader () -> ExtraHeader? {
        do {
            if extra == nil || extra!.isEmpty {
                return nil
            }
            let decoder = JSONDecoder()
            return try decoder.decode(ExtraHeader.self, from: extra!.data(using: .utf8)!)
        } catch {
            debugPrint(error)
            return nil
        }
    }
    
    func getExtraCm (extraCm: String?) -> ExtraCm? {
        do {
            if extraCm == nil || extraCm!.isEmpty {
                return nil
            }
            let decoder = JSONDecoder()
            return try decoder.decode(ExtraCm.self, from: extraCm!.data(using: .utf8)!)
        } catch {
            debugPrint(error)
            return nil
        }
    }
}
