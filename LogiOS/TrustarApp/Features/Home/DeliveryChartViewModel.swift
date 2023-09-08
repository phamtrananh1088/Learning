//
//  DeliveryChartViewModel.swift
//  TrustarApp
//
//  Created by hoanx on 8/3/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//
import Foundation
import Combine
import UIKit
import SwiftUI

class DeliveryChartViewModel:  BaseViewModel, ObservableObject {
    private var deliveryChartRepo = Current.Shared.userRepository?.deliveryChartRepo
    @Published var binDetail: BinDetail?
    var chartImageStore: CachedPicStore?
    
    init(binDetail: BinDetail?) {
        self.binDetail = binDetail
        self.deliveryChart = DeliveryChart()
        self.Tmemos = []
        self.Timages = []
        super.init()
        deliveryChartRepo = Current.Shared.userRepository?.deliveryChartRepo
    }
    
    @Published var deliveryChart: DeliveryChart
    @Published var Tmemos: [ChartMemos]
    @Published var Timages: [EditImage]
    
    private var innerChart: DeliveryChart? {
        return deliveryChartRepo?.findChart (binDetail: binDetail!)
    }
    var isNewCreate: Bool = false
    public let userStoredChartDir: FilesInDir? =
    Current.Shared.loggedUser?.userChartSyncDir
    
    
    func setBinDetail(binDetail: BinDetail) {
        self.binDetail = binDetail
        getData()
    }
    
    private func getData() {
        let b = binDetail ?? BinDetail()
        if let chart = innerChart  {
            isNewCreate = false
            deliveryChart = chart
            Tmemos = deliveryChart.getChartMemos() ?? []
            Timages = (deliveryChart.getImages() ?? []).map { it in
                EditImage.Exists(imageFile: it, dir: userStoredChartDir!)
            }
        } else {
            isNewCreate = true
            deliveryChart = DeliveryChart()
            deliveryChart.chartCd = UUID().uuidString
            deliveryChart.placeCd = b.placeCd ?? ""
            deliveryChart.lastAllocationRowNo = b.allocationRowNo
            deliveryChart.lastAllocationNo = b.allocationNo
            
            deliveryChart.dest = b.placeNameFull()
            deliveryChart.addr1 = b.placeAddr.orEmpty()
            deliveryChart.addr2 = ""
            deliveryChart.tel = b.placeTel1.orEmpty()
            deliveryChart.carrierTel = b.placeTel2.orEmpty()
            Tmemos = []
            Timages = []
        }
        
    }
    
    func download(fileRef: FileRef.ByKey) -> AnyPublisher<URL?, Never> {
        let filePath = Current.Shared.userInfo().pathToCacheFile(fileKey: fileRef.key)
        if filePath != nil && FileManager.default.fileExists(atPath: filePath!.path) {
            return Just(filePath).eraseToAnyPublisher()
        } else {
            let file = Current.Shared.userInfo().cacheByKey(fileKey: fileRef.key, isFile: true).file
            return downloadToFile(picId: fileRef.key, toPath: file)
        }
    }
    
    func readImageToTmpJPEG(uri: [UIImage]) -> [FileRef.ByAnyFile] {
        return uri.map{ uiImage in
            let data = resizeImage(image: uiImage) //.jpegData(compressionQuality: 0.9)
            let file = File.createTempFileInDir(contents: data)
            return FileRef.ByAnyFile(file: file, dotExt: ".jpeg")
        }
    }
    
    func resizeImage(image: UIImage) -> Data {
        var actualHeight: Float = Float(image.size.height)
        var actualWidth: Float = Float(image.size.width)
        let maxHeight: Float = 1920
        let maxWidth: Float = 1920
        var imgRatio: Float = actualWidth / actualHeight
        let maxRatio: Float = maxWidth / maxHeight
        let compressionQuality: Float = 0.9
        //50 percent compression
        
        if actualHeight > maxHeight || actualWidth > maxWidth {
            if imgRatio < maxRatio {
                //adjust width according to maxHeight
                imgRatio = maxHeight / actualHeight
                actualWidth = imgRatio * actualWidth
                actualHeight = maxHeight
            }
            else if imgRatio > maxRatio {
                //adjust height according to maxWidth
                imgRatio = maxWidth / actualWidth
                actualHeight = imgRatio * actualHeight
                actualWidth = maxWidth
            }
            else {
                actualHeight = maxHeight
                actualWidth = maxWidth
            }
        }
        
        let rect = CGRectMake(0.0, 0.0, CGFloat(actualWidth), CGFloat(actualHeight))
        UIGraphicsBeginImageContext(rect.size)
        image.draw(in: rect)
        let img = UIGraphicsGetImageFromCurrentImageContext()
        let imageData = img!.jpegData(compressionQuality: CGFloat(compressionQuality))
        UIGraphicsEndImageContext()
        return imageData!
    }
    
    func save (
        memos: [ChartMemos],
        images: [EditImage]
    ) -> Bool
    {
        
        func c(editImage: EditImage) -> ChartImageFile {
            if let add = editImage as? EditImage.Add {
                let file = add.fileRef.storeFileByMove(dir: add.saveTo).asDbStoredFile()
                return ChartImageFile(
                    dbStoreFile: file, extra: "")
            } else {
                return (editImage as? EditImage.Exists)!.imageFile
            }
        }
        let imageFiles: [ChartImageFile] =  images.map(c)
        let chart: DeliveryChart = DeliveryChart()
        chart.chartCd = deliveryChart.chartCd
        chart.placeCd = deliveryChart.placeCd
        chart.dest = deliveryChart.dest
        chart.addr1 = deliveryChart.addr1
        chart.addr2 = deliveryChart.addr2
        chart.tel = deliveryChart.tel
        chart.carrier =  deliveryChart.carrier
        chart.carrierTel =  deliveryChart.carrierTel
        chart.lastAllocationRowNo = deliveryChart.lastAllocationRowNo
        chart.lastAllocationNo = deliveryChart.lastAllocationNo
        chart.extra = deliveryChart.extra
        let jsonEn = JSONEncoder()
        chart.memos = try! String(data: jsonEn.encode(memos), encoding: .utf8)!
        chart.images = try! String(data: jsonEn.encode(imageFiles), encoding: .utf8)!
        
        if deliveryChartRepo!.saveDeliveryChart(deliveryChart: chart, isCreateNew: isNewCreate) {
            if isNewCreate {
                binDetail?.chartCd = chart.chartCd
            }
            getData()
            
            Current.Shared.syncChart { b, error in
                if let n = error {
                    debugPrint(n)
                }
            }
            
            return true
        }
        return false
    }
}

enum BinDeliveryChartViewModelError: Error {
    case save
}

public class EditImage: Identifiable {
    public let id = UUID()
    var fileRef: FileRef
    init(fileRef: FileRef) {
        self.fileRef = fileRef
    }
    
    public class Exists: EditImage {
        var imageFile: ChartImageFile
        var dir: FilesInDir
        init(imageFile: ChartImageFile, dir: FilesInDir) {
            self.imageFile = imageFile
            self.dir = dir
            super.init(fileRef: self.imageFile.dbStoreFile!.fileRef(dir: self.dir)!)
        }
    }
    
    public class Add: EditImage {
        var saveTo: FilesInDir
        init(fileRef: FileRef, saveTo: FilesInDir) {
            self.saveTo = saveTo
            super.init(fileRef: fileRef)
        }
    }
}

