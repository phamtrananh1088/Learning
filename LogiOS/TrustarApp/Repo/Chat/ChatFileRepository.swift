//
//  ChatFileRepository.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/25.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import MobileCoreServices

func mimeTypeForPath(path: String) -> String {
    let url = NSURL(fileURLWithPath: path)
    let pathExtension = url.pathExtension

    if let uti = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, pathExtension! as NSString, nil)?.takeRetainedValue() {
        if let mimetype = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)?.takeRetainedValue() {
            return mimetype as String
        }
    }
    return "application/octet-stream"
}

class ChatFileRepository {
    func uploadFile(filename: String, body: URL) -> AnyPublisher<FileKey, NetworkError> {
        return uploadFile2(filename: filename, body: body)
            .flatMap({ f2 in
                return Just(f2.key())
                    .setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            })
            .eraseToAnyPublisher()
    }
    
    func uploadFile2(filename: String, body: URL) -> AnyPublisher<FileKey2, NetworkError> {
        return Current.Shared.chatFileApi.uploadFile(body: body, fileName: filename, apiKey: Current.Shared.loggedUser?.token ?? "")
    }
    
    func uploadImage(filename: String, body: URL) -> AnyPublisher<FileKey, NetworkError> {
        return Current.Shared.chatFileApi.uploadImage(body: body, fileName: filename, apiKey: Current.Shared.loggedUser?.token ?? "")
    }
    
    func uploadVideo(filename: String, body: URL) -> AnyPublisher<FileKey2, NetworkError> {
        return uploadFile2(filename: filename, body: body)
    }
}
