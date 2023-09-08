//
//  LoginModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/12.
//

import Foundation
import SwiftUI

class LoggerUser {
    var userInfo: UserInfo
    var token: String
    lazy var binLocationTask: BinLocationTask = BinLocationTask(userInfo: userInfo)
    var imageStore: CachedPicStore
    
    init(userInfo: UserInfo, token: String) {
        self.userInfo = userInfo
        self.token = token
        
        //self.binLocationTask = BinLocationTask(userInfo: userInfo)
        self.imageStore = CachedPicStore(directory: userInfo.homeDir!.createSubDirectory(name: "images"))
    }
    
    func commitCommonDb() {
        try? commitNoticeToCommon()
        try? commitBinToCommon()
        try? commitIncidentalToCommon()
    }
    
    func commitNoticeToCommon() throws {
        Config.Shared.userDb?.noticeDao?.setPending()
        let lstPending = Config.Shared.userDb?.noticeDao?.getPending()

        if lstPending != nil {
            for pending in lstPending! {
                pending.setSyncFinished()

                try Config.Shared.resultDb?.instanceDb?.write { db in
                    do {
                        let commonNotice = CommonNotice(userInfo: self.userInfo, notice: pending)
                        try commonNotice.insert(db)
                    } catch {
                        debugPrint(error)
                    }
                }
                
                try Config.Shared.userDb?.instanceDb?.write { db in
                    do {
                        try pending.update(db)
                    } catch {
                        debugPrint(error)
                    }
                }
            }
        }
    }
    
    func commitBinToCommon() throws {
        
        let wr = Config.Shared.userDb!.binDetailDao!
        let br = Config.Shared.userDb!.binHeaderDao!
        
        // set pending bindetail
        wr.setPending()
        // set pending binheader
        br.setSyncPendingByWorkResult()
        br.setPending()
        
        let pw = wr.getPending()
        
        if pw != nil {
            for pending in pw! {
                pending.setSyncFinished()
                
                try Config.Shared.resultDb?.instanceDb?.write { db in
                    do {
                        let commonWr = CommonWorkResult(userInfo: self.userInfo, binDetail: pending)
                        try commonWr.save(db)
                    } catch {
                        debugPrint(error)
                    }
                }
                
                try Config.Shared.userDb?.instanceDb?.write { db in
                    do {
                        try pending.update(db)
                    } catch {
                        debugPrint(error)
                    }
                }
            }
        }
        
        let pb = br.getPending()
        
        if pb != nil {
            for pending in pb! {
                pending.setSyncFinished()
                
                try Config.Shared.resultDb?.instanceDb?.write { db in
                    do {
                        let commonBr = CommonBinResult(userInfo: self.userInfo, binHeader: pending)
                        try commonBr.save(db)
                    } catch {
                        debugPrint(error)
                    }
                }
                
                try Config.Shared.userDb?.instanceDb?.write { db in
                    do {
                        try pending.update(db)
                    } catch {
                        debugPrint(error)
                    }
                }
            }
        }
    }
    
    func commitIncidentalToCommon() throws {
        try Config.Shared.resultDb?.instanceDb?.write { db in
            
            let h = Config.Shared.userDb?.incidentalHeaderDao
            let t = Config.Shared.userDb?.incidentalTimeDao
            
            h?.setPending()
            let hp = try h?.getPending()
            
            if hp != nil {
                for pending in hp! {
                    do {
                        pending.setSyncFinished()
                        let commonIh = CommonIncidentalHeaderResult(userInfo: self.userInfo, header: pending)
                        try commonIh.save(db)
                    } catch {
                        debugPrint(error)
                    }
                    
                    try Config.Shared.userDb?.instanceDb?.write { db in
                        do {
                            try pending.save(db)
                        } catch {
                            debugPrint(error)
                        }
                    }
                }
            }
            
            t?.setPending()
            let tp = try? t?.getPending()
            if tp != nil {
                for pending in tp! {
                    do {
                        pending.setSyncFinished()
                        let commonIt = CommonIncidentalTimeResult(userInfo: self.userInfo, time: pending)
                        try commonIt.save(db)
                    } catch {
                        debugPrint(error)
                    }
                    
                    try Config.Shared.userDb?.instanceDb?.write { db in
                        do {
                            try pending.update(db)
                        } catch {
                            debugPrint(error)
                        }
                    }
                }
            }
        }
    }
    
    func commitCollectionResultToCommon() {
        let userDb = Config.Shared.userDb!
        let d = userDb.collectionResultDao
        
        d?.setPending()
        let ls = d?.getPending()
        
        let resultDb = Config.Shared.resultDb!
        
        if ls != nil {
            do {
                try resultDb.instanceDb?.write { db in
                    for it in ls! {
                        it.setSyncFinished()
                        let cc = CommonCollectionResult(userInfo: self.userInfo, result: it)
                        try cc.save(db)
                        
                        try userDb.instanceDb?.write { db in
                            try it.update(db)
                        }
                    }
                }
            } catch {
                debugPrint(error)
            }
        }
    }
    
    func commitdeliveryChartToCommon() {
        let userDb = Config.Shared.userDb!
        let d = userDb.deliveryChartDao
        
        d?.setPending()
        let ls = d?.getPending()
        let resultDb = Config.Shared.resultDb!
        if ls != nil {
            do {
                try resultDb.instanceDb?.write { db in
                    for it in ls! {
                        if it == nil {continue}
                        it!.setSyncFinished()
                        
                        let images = it?.getImages()
                        var lstImgStorefile: [ChartImageFile] = []
                        for img in images! {
                            let file = img.dbStoreFile?.copyToNewDir(fromDir: userChartSyncDir, toDir: Config.Shared.commonChartSyncDir)
                            lstImgStorefile.append(ChartImageFile(dbStoreFile: file!, extra: img.extra))
                        }
                        let jsEncode = JSONEncoder()
                        var sImg: String = String(data: try! jsEncode.encode(lstImgStorefile), encoding: .utf8)!
                        let cc = CommonDeliveryChart(userInfo: self.userInfo, result: it!, images: sImg)
                        try cc.save(db)
                        
                        try userDb.instanceDb?.write { db in
                            try it!.update(db)
                        }
                    }
                }
            } catch {
                debugPrint(error)
            }
        }
    }
    
    var userChartSyncDir : FilesInDir { return FilesInDir(dir: self.userInfo.homeDir!.createSubDirectory(name: "chartImages")) }
}
