//
//  DeliveryChartPost.swift
//  TrustarApp
//
//  Created by hoanx on 8/9/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import UIKit

class DeliveryChartPost: Codable {
    var userInfo: UserInfo? = Current.Shared.loggedUser?.userInfo
    var chart: CommonDeliveryChart
    
    init(chart: CommonDeliveryChart) {
        self.userInfo = Current.Shared.loggedUser?.userInfo
        self.chart = chart
    }
    
    func result() -> Result {
        let extraHeader = chart.getExtraHeader()
        let chartHeader: ChartHeaderPost = ChartHeaderPost(
            companyCd: chart.companyCd,
            allocationNo: chart.lastAllocationNo,
            allocationRowNo: chart.lastAllocationRowNo,
            chartCd: chart.chartCd,
            placeCd: chart.placeCd,
            chartNm: chart.dest,
            chartAddr1: chart.addr1,
            chartAddr2: chart.addr2,
            chartTel: chart.tel,
            chargeNm: chart.carrier,
            chargeTel: chart.carrierTel,
            mkTimeStamp: extraHeader?.mkTimeStamp,
            mkUserId: extraHeader?.mkUserId,
            mkPrgNm: extraHeader?.mkPrgNm
        )
        
        var chartMemos: [ChartMemoPost] = []
        let memos = chart.getChartMemos()
        if memos != nil {
            for (index, memo) in memos!.enumerated() {
                var extraCm = chart.getExtraCm(extraCm: memo.extra)
                chartMemos.append(ChartMemoPost(
                    chartCd: chart.chartCd,
                    title: memo.label,
                    note: memo.note,
                    highLightFlag: memo.higtlight ? 1 : 0,
                    seqNo: index + 1,
                    mkTimeStamp: extraCm?.MkTimeStamp,
                    mkUserId: extraCm?.MkUserId,
                    mkPrgNm: extraCm?.MkPrgNm
                ))
            }
        }
        
        var chartImages: [ChartFilePost] = []
        var images: [ResultFile] = []
        let imgs = chart.getImages()
        if imgs != nil {
            for (index, img) in imgs!.enumerated() {
                if img.dbStoreFile?.asStoredFile(dir: Config.Shared.commonChartSyncDir) is StoredFile.ByKey {
                    var extraImg = chart.getExtraCm(extraCm: img.extra)
                    chartImages.append(ChartFilePost(
                        chartCd: chart.chartCd,
                        fileId: img.dbStoreFile?.value ?? "",
                        seqNo: index + 1,
                        mkTimeStamp: extraImg?.MkTimeStamp,
                        mkUserId: extraImg?.MkUserId,
                        mkPrgNm: extraImg?.MkPrgNm
                    ))
                } else {
                    images.append(ResultFile(img: img))
                }
            }
        }
        
        let chart: ChartInfo = ChartInfo(chartHeaders: chartHeader, chartMemos: chartMemos, chartFiles: chartImages)
        let info: ResultInfo = ResultInfo(chart: chart)
        
        let rs: Result = Result(chartJson: info, images: images)
        return rs
    }
    
    class Result: Codable {
        var chartJson: ResultInfo
        var images: [ResultFile]
        
        init(chartJson: ResultInfo, images: [ResultFile]) {
            self.chartJson = chartJson
            self.images = images
        }
    }
    
    class ResultFile: Codable {
        var name: String = ""
        var fileName: String
        var body: Data
        
        
        init(img: ChartImageFile) {
            self.fileName = img.dbStoreFile!.value
            self.body = readImageData(fileId: img.dbStoreFile!.value)
        }
    }
    
    class ResultInfo: Codable {
        var userId: String
        var clientInfo: ClientInfo
        var chart: ChartInfo
        
        init(chart: ChartInfo) {
            self.userId = Current.Shared.loggedUser?.userInfo.userId ?? ""
            self.clientInfo = Config.Shared.clientInfo
            self.chart = chart
        }
    }
    
    class ChartInfo: Codable {
        var chartHeaders: [ChartHeaderPost] = []
        var chartMemos: [ChartMemoPost]
        var chartFiles: [ChartFilePost]
        
        init(chartHeaders: ChartHeaderPost, chartMemos: [ChartMemoPost], chartFiles: [ChartFilePost]) {
            self.chartHeaders.append(chartHeaders)
            self.chartMemos = chartMemos
            self.chartFiles = chartFiles
        }
    }
    
    class ChartHeaderPost: Codable {
        var companyCd: String
        var allocationNo: String?
        var allocationRowNo: Int?
        var chartCd: String
        var placeCd: String?
        var chartNm: String?
        var chartAddr1: String?
        var chartAddr2: String?
        var chartTel: String?
        var chargeNm: String?
        var chargeTel: String?
        var mkTimeStamp: String?
        var mkUserId: String?
        var mkPrgNm: String?
        
        init(companyCd: String, allocationNo: String?, allocationRowNo: Int?, chartCd: String, placeCd: String? = nil, chartNm: String? = nil, chartAddr1: String? = nil, chartAddr2: String? = nil, chartTel: String? = nil, chargeNm: String? = nil, chargeTel: String? = nil, mkTimeStamp: String?, mkUserId: String?, mkPrgNm: String?) {
            self.companyCd = companyCd
            self.allocationNo = allocationNo
            self.allocationRowNo = allocationRowNo
            self.chartCd = chartCd
            self.placeCd = placeCd
            self.chartNm = chartNm
            self.chartAddr1 = chartAddr1
            self.chartAddr2 = chartAddr2
            self.chartTel = chartTel
            self.chargeNm = chargeNm
            self.chargeTel = chargeTel
            self.mkTimeStamp = mkTimeStamp
            self.mkUserId = mkUserId
            self.mkPrgNm = mkPrgNm
        }
    }
    
    class ChartMemoPost: Codable {
        var chartCd: String
        var title: String?
        var note: String?
        var highLightFlag: Int?
        var seqNo: Int
        var MkTimeStamp: String?
        var MkUserId: String?
        var MkPrgNm: String?
        
        init(chartCd: String, title: String? = nil, note: String? = nil, highLightFlag: Int? = nil, seqNo: Int, mkTimeStamp: String?, mkUserId: String?, mkPrgNm: String?) {
            self.chartCd = chartCd
            self.title = title
            self.note = note
            self.highLightFlag = highLightFlag
            self.seqNo = seqNo
            self.MkTimeStamp = mkTimeStamp
            self.MkUserId = mkUserId
            self.MkPrgNm = mkPrgNm
        }
    }
    
    class ChartFilePost : Codable {
        var chartCd: String
        var fileId: String
        var seqNo: Int
        var MkTimeStamp: String?
        var MkUserId: String?
        var MkPrgNm: String?
        
        init(chartCd: String, fileId: String, seqNo: Int, mkTimeStamp: String?, mkUserId: String?, mkPrgNm: String?) {
            self.chartCd = chartCd
            self.fileId = fileId
            self.seqNo = seqNo
            self.MkTimeStamp = mkTimeStamp
            self.MkUserId = mkUserId
            self.MkPrgNm = mkPrgNm
        }
    }
}

func readImageData(fileId: String) -> Data {
    let filePath: URL = (Config.Shared.commonChartSyncDir.dir.appendingPathComponent(fileId))
    if !FileManager.default.fileExists(atPath: filePath.path) {
        return Data()
    }
    let image: UIImage? = UIImage(contentsOfFile: filePath.path)
    return image!.pngData()!
//        let fileHandle = FileHandle(forReadingAtPath: filePath.path)
//        let data = fileHandle?.readData(ofLength: 100000000)
//        fileHandle?.closeFile()
//        return data ?? Data()
}
