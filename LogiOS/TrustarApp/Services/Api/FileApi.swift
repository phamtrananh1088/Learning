//
//  FileApi.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/08.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

struct FileApi {
    func download(fileKey: String,
                  apiKey: String)
    -> AnyPublisher<(Result<Data>, Int), NetworkError> {
        let qry: [(String, Any)]  = [("fileKey", fileKey)]
        return API.shared.callChatDownloadFileAPI(.download, [.ApiKeyAndTokenHeader, .CompanyCdHeader], apiKey, qry)
    }
    
    func imageCached(url: URL) -> AnyPublisher<Data, NetworkError> {
        return API.shared.callChatFileCachedAPI(url: url)
    }

    func uploadFile(body: URL,
                    fileName: String,
                    apiKey: String,
                    _ callBackProgress: ((Double)-> Void)? = nil)
    -> AnyPublisher<FileKey2, NetworkError> {
        return API.shared.callChatUploadFileAPI(.uploadFile, [.ApiKeyAndTokenHeader, .CompanyCdHeader], apiKey, urlFile: body, fileName: fileName, callBackProgress: callBackProgress)
            .map({ rs in
                switch rs {
                case .Loading:
                    return Just(FileKey2(fileKey: ""))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                case .Value(value: let value):
                    do {
                        let obj = try JSONDecoder().decode(FileKey2.self, from: value!)
                        return Just(obj)
                            .setFailureType(to: NetworkError.self)
                            .eraseToAnyPublisher()
                    } catch {
                        return Fail(error: NetworkError.error(error: error)).eraseToAnyPublisher()
                    }
                    
                case .Error(error: let error):
                    return Fail(error: error).eraseToAnyPublisher()
                }
            })
            .switchToLatest()
            .eraseToAnyPublisher()
    }

    func uploadImage(body: URL,
                     fileName: String,
                     apiKey: String,
                     _ callBackProgress: ((Double)-> Void)? = nil)
    -> AnyPublisher<FileKey, NetworkError> {
        return API.shared.callChatUploadFileAPI(.uploadImage, [.ApiKeyAndTokenHeader, .CompanyCdHeader], apiKey, urlFile: body, fileName: fileName, callBackProgress: callBackProgress)
            .map({ rs in
                switch rs {
                case .Loading:
                    return Just(FileKey(fileKey: ""))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                case .Value(value: let value):
                    do {
                        let obj = try JSONDecoder().decode(FileKey.self, from: value!)
                        return Just(obj)
                            .setFailureType(to: NetworkError.self)
                            .eraseToAnyPublisher()
                    } catch {
                        return Fail(error: NetworkError.error(error: error)).eraseToAnyPublisher()
                    }
                    
                case .Error(error: let error):
                    return Fail(error: error).eraseToAnyPublisher()
                }
            })
            .switchToLatest()
            .eraseToAnyPublisher()
    }

    func uploadSensorCSV(fileName: String, csv: Data, apiKey: String) -> AnyPublisher<Result<Data>, NetworkError> {
        return API.shared.callUploadCsvAPI(
            .uploadSensorCSV,
            [.ApiKeyAndTokenHeader, .CompanyCdHeader],
            csv,
            fileName,
            apiKey
        )
    }
}
