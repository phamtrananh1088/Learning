//
//  CachedPicStore.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class CachedPicStore {
    //private let cache = NSCache<NSURL, UIImage>()
    
    private var directory: URL
    
    init(directory: URL) {
        self.directory = directory
    }
    
    private func fileName(identity: String) -> String? {
        if let data = identity.data(using: .utf8) {
            return data.base64EncodedString()
        }
        return nil
    }
    
    func getOrDownload(identity: String, picId: String) -> URL? {
        if let fN = fileName(identity: identity) {
            let filePath: URL = directory.appendingPathComponent(fN)
            if FileManager.default.fileExists(atPath: filePath.path) {
                return filePath
            }
            getOrDownload(identity: identity, picId: picId) { data in
                if data != nil && !data!.isEmpty {
                    try? data?.write(to: filePath)
                }
            }
            return filePath
        }
        
        return nil
    }
    
    private func getOrDownload(identity: String, picId: String, onCompletion: @escaping (Data?) -> Void) {
        if Current.Shared.loggedUser == nil { return }
        let json = Helper.Shared.convertObjectToJson(body: PicQuery(picId: picId,
                                                                    userInfo: Current.Shared.loggedUser!.userInfo))
        
        var request = URLRequest(url: URL(string: Resources.HOST_URL + Resources.API_URL + Endpoint.getImgData.path() + "?api_key=" + Current.Shared.loggedUser!.token)!)
        
        request.httpMethod = "POST"
        request.httpBody = json!.data(using: String.Encoding.utf8)
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        
        URLSession.shared.dataTask(with: request, completionHandler: { data, response, error in
            guard let _ = data, error == nil else { return }
            
            onCompletion(data)
        }).resume()
    }
    
    func removeFile(identity: String) {
        if let fN = fileName(identity: identity) {
            let fileUrl: URL = directory.appendingPathComponent(fN)
            
            //Checks if file exists, removes it if so.
            if FileManager.default.fileExists(atPath: fileUrl.path) {
                do {
                    try FileManager.default.removeItem(atPath: fileUrl.path)
                    //print("Removed old image")
                } catch let removeError {
                    print("couldn't remove file at path", removeError)
                }
            }
        }
    }
    
    func storeFile(identity: String, signPath: String) throws -> String {
        let imageUrl = URL(fileURLWithPath: signPath)
        let imageNewUrl: URL = directory.appendingPathComponent(fileName(identity: identity)!)
        try FileManager.default.moveItem(at: imageUrl, to: imageNewUrl)
        return imageNewUrl.path
    }
}
