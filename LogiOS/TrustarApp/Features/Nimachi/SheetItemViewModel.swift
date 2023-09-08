//
//  SheetItemViewModel.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class SheetItemViewModel: ObservableObject {
    
    private var header: IncidentalHeader?
    private var repo: IncidentalRepo?
    private var picStore: CachedPicStore?
    
    @Published var itemData: IncidentalItemDataDB?
    @Published var signImagePath: String
    
    init(header: IncidentalHeader?) {
        self.header = header
        self.itemData = nil
        self.signImagePath = ""
        
        do {
            /*荷待作業・附帯作業のデータ取得*/
            repo  = Current.Shared.userRepository?.incidentalRepo
            itemData = try repo!.sheetDetailsByUUID(uuid: header!.uuid)
            
            /*待機・附帯作業詳細・署名画像の取得処理*/
            picStore = repo?.imageStore
            signImagePath = ""
            
            if header?.status != nil && header?.status != 0 {
                if let url = picStore?.getOrDownload(identity: itemData?.item.sheet.localSignatureId() ?? "",
                                                     picId: itemData?.item.sheet.picId ?? "") {
                    signImagePath = url.path
                }
            }
        } catch let err {
            print("Cannot get image file", err)
        }
    }
    
    func getHeaderUUID() -> String {
        return header?.uuid ?? ""
    }
    /*署名・附帯作業のデータ更新処理*/
    func commitPendingSign(signPath: String) {
        let oldId = header?.localSignatureId()
        if signPath.isEmpty {
            //clear sign
            let _ = repo?.saveIncidentalHeaderWithPic(header: header!, pic: nil)
            
            //trigger view update
            signImagePath = signPath
            
            //delete old file if exists
            if oldId != nil {
                picStore?.removeFile(identity: oldId!)
            }
            return
        }
        
        //delete old file if exists
        if oldId != nil {
            picStore?.removeFile(identity: oldId!)
        }
        
        do {
            //read file
            let imageUrl = URL(fileURLWithPath: signPath)
            let imageData = try Data(contentsOf: imageUrl)
            let newHeader = repo?.saveIncidentalHeaderWithPic(header: header!, pic: imageData)
            
            let newId = newHeader?.localSignatureId()
            let newSignPath = try picStore?.storeFile(identity: newId!, signPath: signPath)
            
            //trigger view update
            signImagePath = newSignPath!
        } catch let err {
            print("Cannot save image file", err)
        }
    }
}
