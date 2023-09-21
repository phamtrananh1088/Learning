//
//  DeliveryChartResult.swift
//  TrustarApp
//
//  Created by hoanx on 8/9/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//
import Foundation

// MARK: - Welcome
class RawChart: Codable {
    let binHeaders, binResults, binDetails, binCollections: String?
    let binCharts: BinCharts
    let workResults: String?
    let clientInfo: String?

    init(binHeaders: String?, binResults: String?, binDetails: String?, binCollections: String?, binCharts: BinCharts,
         workResults: String?, clientInfo: String?) {
        self.binHeaders = binHeaders
        self.binResults = binResults
        self.binDetails = binDetails
        self.binCollections = binCollections
        self.binCharts = binCharts
        self.workResults = workResults
        self.clientInfo = clientInfo
    }
    
    func charts() -> [DeliveryChart]? {
        var chartArr:[DeliveryChart] = [];
        guard let chartHeader = self.binCharts.chartHeaders else {
            return chartArr
        }

       for item in chartHeader {
           do {
               var memos: [ChartMemos] = []
               var imgs: [ChartImageFile] = []
               if let chartMemos = self.binCharts.chartMemos?.filter({$0.chartCD == item.chartCD}) {
                   for mm in chartMemos {
                       let extraMM = "{\"MkTimeStamp\":\"\(mm.mkTimeStamp ?? "")\",\"MkUserId\":\"\(mm.mkUserID ?? "")\",\"MkPrgNm\":\"\(mm.mkPrgNm ?? "")\"}"
                       memos.append(ChartMemos(label: mm.title ?? "", note: mm.note ?? "",
                                               higtlight: mm.highLightFlag == 1, extra: extraMM))
                   }
               }

               if let files = self.binCharts.chartFiles?.filter({$0.chartCD == item.chartCD}) {
                   for file in files {
                       let extraImg = "{\"MkTimeStamp\":\"\(file.mkTimeStamp ?? "")\",\"MkUserId\":\"\(file.mkUserID ?? "")\",\"MkPrgNm\":\"\(file.mkPrgNm ?? "")\"}"
                       imgs.append(ChartImageFile(
                        dbStoreFile: StoredFile.ByKey(key: file.fileID ?? "").asDbStoredFile(), extra: extraImg))
                   }
               }

               let jsonEn = JSONEncoder()
               let chart = DeliveryChart()
               chart.chartCd = item.chartCD
               chart.placeCd = item.placeCD ?? ""
               chart.dest = item.chartNm ?? ""
               chart.addr1 = item.chartAddr1 ?? ""
               chart.addr2 = item.chartAddr2 ?? ""
               chart.tel = item.chartTel ?? ""
               chart.carrier = item.chargeNm ?? ""
               chart.carrierTel = item.chargeTel ?? ""
               chart.memos = try String(data: jsonEn.encode(memos), encoding: .utf8)!
               chart.images = try String(data: jsonEn.encode(imgs), encoding: .utf8)!
               let extra = "{\"mkTimeStamp\":\"\(item.mkTimeStamp ?? "")\",\"mkUserId\":\"\(item.mkUserID ?? "")\",\"mkPrgNm\":\"\(item.mkPrgNm ?? "")\"}"
               chart.extra = extra
               chartArr.append(chart)
           } catch {
               print(error)
           }
       }
       return chartArr
   }
}

// MARK: - BinCharts
class BinCharts: Codable {
   let chartHeaders: [ChartHeader]?
   let chartMemos: [ChartMemo]?
   let chartFiles: [ChartFile]?
}

// MARK: - ChartHeader
class ChartHeader: Codable {
    let chartCD: String
    let mkTimeStamp, mkUserID, mkPrgNm, companyCD: String?
    let allocationNo, allocationRowNo, chartNm: String?
    let placeCD, chartZip, chartAddr1, chartAddr2: String?
    let chartTel, chargeNm, chargeTel: String?

    enum CodingKeys: String, CodingKey {
        case mkTimeStamp
        case mkUserID = "mkUserId"
        case mkPrgNm
        case companyCD = "companyCd"
        case allocationNo, allocationRowNo
        case chartCD = "chartCd"
        case chartNm
        case placeCD = "placeCd"
        case chartZip, chartAddr1, chartAddr2, chartTel, chargeNm, chargeTel
    }

    init(mkTimeStamp: String?, mkUserID: String?, mkPrgNm: String?, companyCD: String?, allocationNo: String?, allocationRowNo: String?, chartCD: String, chartNm: String?, placeCD: String?, chartZip: String?, chartAddr1: String?, chartAddr2: String?, chartTel: String?, chargeNm: String?, chargeTel: String?) {
        self.mkTimeStamp = mkTimeStamp
        self.mkUserID = mkUserID
        self.mkPrgNm = mkPrgNm
        self.companyCD = companyCD
        self.allocationNo = allocationNo
        self.allocationRowNo = allocationRowNo
        self.chartCD = chartCD
        self.chartNm = chartNm
        self.placeCD = placeCD
        self.chartZip = chartZip
        self.chartAddr1 = chartAddr1
        self.chartAddr2 = chartAddr2
        self.chartTel = chartTel
        self.chargeNm = chargeNm
        self.chargeTel = chargeTel
    }
}

// MARK: - ChartMemo
class ChartMemo: Codable {
    let chartCD: String
    let mkTimeStamp, mkUserID, mkPrgNm: String?
    let seqNo, highLightFlag: Int?
    let title, note: String?

    enum CodingKeys: String, CodingKey {
        case mkTimeStamp = "MkTimeStamp"
        case mkUserID = "MkUserId"
        case mkPrgNm = "MkPrgNm"
        case chartCD = "ChartCd"
        case seqNo = "SeqNo"
        case highLightFlag = "HighLightFlag"
        case title = "Title"
        case note = "Note"
    }

    init(mkTimeStamp: String?, mkUserID: String?, mkPrgNm: String?, chartCD: String, seqNo: Int?, highLightFlag: Int?, title: String?, note: String?) {
        self.mkTimeStamp = mkTimeStamp
        self.mkUserID = mkUserID
        self.mkPrgNm = mkPrgNm
        self.chartCD = chartCD
        self.seqNo = seqNo
        self.highLightFlag = highLightFlag
        self.title = title
        self.note = note
    }
}

// MARK: - ChartFile
class ChartFile: Codable {
    let chartCD: String
    let mkTimeStamp, mkUserID, mkPrgNm: String?
    let fileID: String?
    let dataClass: Int?
    let attachedFile: String?
    let fileName: String?
    let fileSize, width, height: Int?
    let folderPath, fileExtension: String?

    enum CodingKeys: String, CodingKey {
        case mkTimeStamp = "MkTimeStamp"
        case mkUserID = "MkUserId"
        case mkPrgNm = "MkPrgNm"
        case chartCD = "ChartCd"
        case fileID = "FileId"
        case dataClass = "DataClass"
        case attachedFile = "AttachedFile"
        case fileName = "FileName"
        case fileSize = "FileSize"
        case width = "Width"
        case height = "Height"
        case folderPath = "FolderPath"
        case fileExtension = "FileExtension"
    }
}




