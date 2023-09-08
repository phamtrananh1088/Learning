//
//  PicPost.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/05.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class PicPost: Codable {
    private var clientInfo: ClientInfo
    private var imageSending: ImageSending
    private var picId = Resources.strEmpty
    
    init(clientInfo: ClientInfo, imageSending: ImageSending) {
        self.clientInfo = clientInfo
        self.imageSending = imageSending
        self.picId = imageSending.picId
    }
    
    func result() -> Result {
        let rs = Result()
        rs.companyCd = imageSending.companyCd
        rs.userId = imageSending.userId
        rs.clientInfo = clientInfo
        
        rs.picId = picId
        rs.picByteBase64String = imageSending.picRaw.base64EncodedString()
        
        if let parsedData = try? JSONSerialization.jsonObject(with: imageSending.content.data(using: .utf8)!, options: []) as? [String:Any] {
            rs.picCls = parsedData["picCls"] as? String ?? ""
            rs.note1 = parsedData["note1"] as? String
            rs.note2 = parsedData["note2"] as? String
            rs.note3 = parsedData["note3"] as? String
            rs.note4 = parsedData["note4"] as? String
            rs.note5 = parsedData["note5"] as? String
        }
        
        return rs
    }
    
    class Result: Codable {
        var companyCd: String = ""
        var userId: String = ""
        var clientInfo: ClientInfo?
        
        var picId: String = ""
        var picByteBase64String: String = ""
        var picCls: String = ""
        var note1: String?
        var note2: String?
        var note3: String?
        var note4: String?
        var note5: String?
    }
    
    class Content {
        var picCls: String
        var note1: String?
        var note2: String?
        var note3: String?
        var note4: String?
        var note5: String?
        
        init(picCls: String,
             note1: String?,
             note2: String?,
             note3: String?,
             note4: String?,
             note5: String?) {
            self.picCls = picCls
            self.note1 = note1
            self.note2 = note2
            self.note3 = note3
            self.note4 = note4
            self.note5 = note5
        }
        
        func toJsonString() -> String {
            let jsonObj: [String: Any] = [
                "picCls": picCls,
                "note1": note1 ?? "",
                "note2": note2 ?? "",
                "note3": note3 ?? "",
                "note4": note4 ?? "",
                "note5": note5 ?? ""
            ]
            guard let theJSONData = try? JSONSerialization.data(withJSONObject: jsonObj,
                                                                options: .prettyPrinted) else {
                return ""
            }
            
            return String(data: theJSONData, encoding: .utf8) ?? ""
            
        }
        
        class IncidentalSign : Content {
            init (header: IncidentalHeader) {
                super.init (picCls: "SIGN",
                            note1: header.allocationNo,
                            note2: String(header.allocationRowNo),
                            note3: header.uuid,
                            note4: "Incidental",
                            note5: nil)
            }
        }
    }
}
