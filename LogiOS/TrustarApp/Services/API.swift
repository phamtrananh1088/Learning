//
//  API.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/17.
//

import Foundation
import Combine
import UIKit

enum MethodEnum {
    case GET
    case POST
}

class API: NSObject
{
    static public let shared = API()
    
    private let decoder: JSONDecoder
    private let encoder: JSONEncoder
    
    override init()
    {
        let decoder = JSONDecoder()
        decoder.keyDecodingStrategy = .convertFromSnakeCase
        self.decoder = decoder
        
        let encoder = JSONEncoder()
        self.encoder = encoder
        
    }
    
    private var observation: NSKeyValueObservation?

    deinit {
      observation?.invalidate()
    }
    
    func callAPI<T: Encodable, R: Codable>(_ endPoint: Endpoint ,_ body: T, _ apiKey: String) -> AnyPublisher<R, NetworkError>
    {
        let json = Helper.Shared.convertObjectToJson(body: body)
        var request = URLRequest(url: URL(string: Resources.HOST_URL + Resources.API_URL + endPoint.path() + "?api_key=" + apiKey)!)

        request.httpMethod = "POST"
        request.httpBody = json!.data(using: String.Encoding.utf8)
        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.timeoutInterval = 30
        URLSession.shared.configuration.waitsForConnectivity = true
        URLSession.shared.configuration.timeoutIntervalForResource = 30
        
        return URLSession.shared.dataTaskPublisher(for: request)
            .tryMap{ data, response in
                guard let httpResponse = response as? HTTPURLResponse else
                {
                    throw NetworkError.unknow
                }
                if (httpResponse.statusCode == 404)
                {
                    throw NetworkError.message(reason: "Not found")
                }
                if (httpResponse.statusCode == 401)
                {
                    Current.Shared.logout()
                    NotificationCenter.default.post(name: Notification.Name(BroadcastEnum.Authorized.rawValue), object: nil)
                    throw NetworkError.actionHttp401
                }

                #if DEBUG
//                if endPoint == .postDeliveryChart {
//                    debugPrint(String(decoding: data, as: UTF8.self))
//                }
                #endif
                
                return data
            }
            .decode(type: R.self, decoder: JSONDecoder())
            .mapError{NetworkError.parseError(reason: $0)}
            .eraseToAnyPublisher()
    }
    
    func callChatAPI<T: Encodable, R: Codable>(_ endPoint: ChatEndPoint,
                                               _ types: [TypeEnum],
                                               _ body: T? = nil,
                                               _ apiKey: String,
                                               _ method: MethodEnum,
                                               _ customQuery: [(String, Any)] = [],
                                               _ customBodyString: String? = nil) -> AnyPublisher<R, NetworkError> {

        let user = Current.Shared.loggedUser?.userInfo
        let baseUrl = Resources.HOST_URL + Resources.MESSAGE_API_URL + endPoint.path()
        var urlComps = URLComponents(string: baseUrl)!
        if urlComps.queryItems == nil { urlComps.queryItems = [] }
        var request = URLRequest(url: URL(string: baseUrl)!)
        
        if method == .GET {
            request.httpMethod = "GET"
        } else {
            request.httpMethod = "POST"
            
            if let b = body {
                let json = Helper.Shared.convertObjectToJson(body: b)
                request.httpBody = json!.data(using: String.Encoding.utf8)
            }
            
            if let cB = customBodyString {
                request.httpBody = cB.data(using: String.Encoding.utf8)
            }
        }

        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        for type in types {
            switch(type) {
                
            case .ApiKeyAndTokenHeader:
                request.addValue(apiKey, forHTTPHeaderField: "token")
                request.addValue(Resources.MESSAGE_API_KEY, forHTTPHeaderField: "apiKey")
            case .CompanyCdHeader:
                request.addValue(user?.companyCd ?? "", forHTTPHeaderField: "companyCd")
            case .CompanyCdQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "companyCd", value: user?.companyCd ?? ""))
            case .ApiKeyQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "api_key", value: apiKey))
            case .PostUserInfo:
                request.httpBody = Helper.Shared.convertObjectToJson(body: user!)!.data(using: String.Encoding.utf8)
            }
        }
        
        if !customQuery.isEmpty {
            for cQ in customQuery {
                urlComps.queryItems?.append(URLQueryItem(name: cQ.0, value: String(describing: cQ.1)))
            }
        }
        
        request.url = urlComps.url
        
        request.timeoutInterval = 30
        URLSession.shared.configuration.waitsForConnectivity = true
        URLSession.shared.configuration.timeoutIntervalForResource = 30
        
        return URLSession.shared.dataTaskPublisher(for: request)
            .tryMap{ data, response in
                guard let httpResponse = response as? HTTPURLResponse else
                {
                    throw NetworkError.unknow
                }
                if (httpResponse.statusCode == 404)
                {
                    throw NetworkError.message(reason: "Not found")
                }
                if (httpResponse.statusCode == 401)
                {
                    Current.Shared.logout()
                    NotificationCenter.default.post(name: Notification.Name(BroadcastEnum.Authorized.rawValue), object: nil)
                    throw NetworkError.actionHttp401
                }

                #if DEBUG
                if endPoint == .deleteMessage {
                    //debugPrint(String(decoding: data, as: UTF8.self))
                }
                #endif
                
                return data
            }
            .decode(type: R.self, decoder: JSONDecoder())
            .mapError{NetworkError.parseError(reason: $0)}
            .eraseToAnyPublisher()
    }
    
    func callChatDownloadFileAPI(_ endPoint: ChatFileEndPoint,
                         _ types: [TypeEnum],
                         _ apiKey: String,
                         _ customQuery: [(String, Any)] = []) -> AnyPublisher<(Result<Data>, Int), NetworkError> {
        let user = Current.Shared.loggedUser?.userInfo
        let baseUrl = Resources.HOST_URL + Resources.MESSAGE_API_URL + endPoint.path()
        var urlComps = URLComponents(string: baseUrl)!
        if urlComps.queryItems == nil { urlComps.queryItems = [] }
        var request = URLRequest(url: URL(string: baseUrl)!)
        
        request.httpMethod = "GET"

        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        for type in types {
            switch(type) {
                
            case .ApiKeyAndTokenHeader:
                request.addValue(apiKey, forHTTPHeaderField: "token")
                request.addValue(Resources.MESSAGE_API_KEY, forHTTPHeaderField: "apiKey")
            case .CompanyCdHeader:
                request.addValue(user?.companyCd ?? "", forHTTPHeaderField: "companyCd")
            case .CompanyCdQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "companyCd", value: user?.companyCd ?? ""))
            case .ApiKeyQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "api_key", value: apiKey))
            case .PostUserInfo:
                request.httpBody = Helper.Shared.convertObjectToJson(body: user!)!.data(using: String.Encoding.utf8)
            }
        }
        
        if !customQuery.isEmpty {
            for cQ in customQuery {
                urlComps.queryItems?.append(URLQueryItem(name: cQ.0, value: String(describing: cQ.1)))
            }
        }
        
        request.url = urlComps.url
        URLSession.shared.configuration.waitsForConnectivity = true
        
        let subject = PassthroughSubject<(Result<Data>, Int), NetworkError>()
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            if data == nil || error != nil {
                subject.send(((.Error(error: .error(error: error!))), 0))
            }
            
            subject.send((.Value(value: data), 100))
        }
        
        self.observation = task.progress.observe(\.fractionCompleted) { progress, _ in
            subject.send((.Loading, Int(progress.fractionCompleted * 100)))
        }
        
        task.resume()
        return subject.eraseToAnyPublisher()
    }
    
    func callChatFileCachedAPI(url: URL) -> AnyPublisher<Data, NetworkError> {
        let user = Current.Shared.loggedUser?.userInfo
        var request = URLRequest(url: URL(string: url.absoluteString)!)
        
        request.httpMethod = "GET"

        request.addValue("application/json", forHTTPHeaderField: "Content-type")
        request.addValue(Current.Shared.loggedUser?.token ?? "", forHTTPHeaderField: "token")
        request.addValue(Resources.MESSAGE_API_KEY, forHTTPHeaderField: "apiKey")
        request.addValue(user?.companyCd ?? "", forHTTPHeaderField: "companyCd")
        request.cachePolicy = .returnCacheDataElseLoad
        
        if let cached = Current.Shared.getUrlCache().cachedResponse(for: request) {
            return Just(cached.data)
                .setFailureType(to: NetworkError.self)
                .eraseToAnyPublisher()
        }
        
        return Current.Shared.getSessionCached().dataTaskPublisher(for: request)
            .mapError({ error in
                NetworkError.error(error: error)
            })
            .flatMap({ data, response -> AnyPublisher<Data, NetworkError> in
                Current.Shared.getUrlCache().storeCachedResponse(CachedURLResponse(response: response, data: data), for: request)
                
                return Just(data)
                    .setFailureType(to: NetworkError.self)
                    .eraseToAnyPublisher()
            })
            .eraseToAnyPublisher()
    }
    
    var callBackUpload: ((Double) -> Void)? = nil
    func callChatUploadFileAPI(_ endPoint: ChatFileEndPoint,
                                               _ types: [TypeEnum],
                                               _ apiKey: String,
                                               urlFile: URL,
                                               fileName: String,
                                               callBackProgress: ((Double) -> Void)? = nil) -> AnyPublisher<Result<Data>, NetworkError> {

        self.callBackUpload = callBackProgress
        let user = Current.Shared.loggedUser?.userInfo
        let baseUrl = Resources.HOST_URL + Resources.MESSAGE_API_URL + endPoint.path()
        var urlComps = URLComponents(string: baseUrl)!
        if urlComps.queryItems == nil { urlComps.queryItems = [] }
        
        let boundary = UUID().uuidString
        var request = URLRequest(url: URL(string: baseUrl)!)
        request.httpMethod = "POST"
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        var data = Data()
        data.append("\r\n--\(boundary)\r\n".data(using: .utf8)!)
        data.append("Content-Disposition: form-data; name=\"buffer\"; filename=\"\(fileName)\"\r\n".data(using: .utf8)!)
        data.append("Content-Type: \(mimeTypeForPath(path: urlFile.path))\r\n\r\n".data(using: .utf8)!)
        data.append((try? Data(contentsOf: urlFile)) ?? Data())
        data.append("\r\n--\(boundary)--\r\n".data(using: .utf8)!)

        for type in types {
            switch(type) {

            case .ApiKeyAndTokenHeader:
                request.addValue(apiKey, forHTTPHeaderField: "token")
                request.addValue(Resources.MESSAGE_API_KEY, forHTTPHeaderField: "apiKey")
            case .CompanyCdHeader:
                request.addValue(user?.companyCd ?? "", forHTTPHeaderField: "companyCd")
            case .CompanyCdQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "companyCd", value: user?.companyCd ?? ""))
            case .ApiKeyQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "api_key", value: apiKey))
            case .PostUserInfo:
                break
            }
        }

        request.url = urlComps.url
        URLSession.shared.configuration.waitsForConnectivity = true

        return Future<Result<Data>, NetworkError> { promise in
            let session = URLSession(configuration: URLSessionConfiguration.default, delegate: self, delegateQueue: OperationQueue.main)
            
            session.configuration.waitsForConnectivity = true
            session.configuration.timeoutIntervalForRequest = 60
            session.configuration.timeoutIntervalForResource = 60
            
            session.uploadTask(with: request, from: data) { data, response, error in
                
                if data == nil || error != nil {
                    promise(.success(.Error(error: .error(error: error!))))
                }
                
                promise(.success(.Value(value: data)))
                
                if let httpResponse = response as? HTTPURLResponse {
                    debugPrint(httpResponse.statusCode)
                    if httpResponse.statusCode == 500 {
                        debugPrint(String(decoding: data!, as: UTF8.self))
                    }
                }
                
            }.resume()
        }.eraseToAnyPublisher()
    }
    
    func callUploadCsvAPI(_ endPoint: Endpoint,
                          _ types: [TypeEnum],
                          _ csv: Data,
                          _ fileName: String,
                          _ apiKey: String) -> AnyPublisher<Result<Data>, NetworkError>
    {
        let user = Current.Shared.loggedUser?.userInfo
        let baseUrl = Resources.HOST_URL + Resources.API_URL + endPoint.path()
        var urlComps = URLComponents(string: baseUrl)!
        if urlComps.queryItems == nil { urlComps.queryItems = [] }
        
        let boundary = UUID().uuidString
        var request = URLRequest(url: URL(string: baseUrl)!)
        request.httpMethod = "POST"
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        
        var data = Data()
        data.append("\r\n--\(boundary)\r\n".data(using: .utf8)!)
        data.append("Content-Disposition: form-data; name=\"sensor\"; filename=\"\(fileName)\"\r\n".data(using: .utf8)!)
        data.append("Content-Type: application/octet-stream\r\n\r\n".data(using: .utf8)!)
        data.append(csv)
        data.append("\r\n--\(boundary)--\r\n".data(using: .utf8)!)

        for type in types {
            switch(type) {

            case .ApiKeyAndTokenHeader:
                request.addValue(apiKey, forHTTPHeaderField: "token")
                request.addValue(Resources.MESSAGE_API_KEY, forHTTPHeaderField: "apiKey")
            case .CompanyCdHeader:
                request.addValue(user?.companyCd ?? "", forHTTPHeaderField: "companyCd")
            case .CompanyCdQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "companyCd", value: user?.companyCd ?? ""))
            case .ApiKeyQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "api_key", value: apiKey))
            case .PostUserInfo:
                break
            }
        }

        request.url = urlComps.url
        URLSession.shared.configuration.waitsForConnectivity = true

        return Future<Result<Data>, NetworkError> { promise in
            let session = URLSession(configuration: URLSessionConfiguration.default, delegate: self, delegateQueue: OperationQueue.main)
            
            session.configuration.waitsForConnectivity = true
            session.configuration.timeoutIntervalForRequest = 60
            session.configuration.timeoutIntervalForResource = 60
            
            session.uploadTask(with: request, from: data) { data, response, error in
                
                if data == nil || error != nil {
                    promise(.failure(.parseError(reason: error!)))
                }
                
                promise(.success(.Value(value: data)))
                
                if let httpResponse = response as? HTTPURLResponse {
                    debugPrint(httpResponse.statusCode)
                    if httpResponse.statusCode == 500 {
                        debugPrint(String(decoding: data!, as: UTF8.self))
                    }
                }
                
            }.resume()
        }.eraseToAnyPublisher()
    }
    
    func callPostDeliveryChartAPI(_ endPoint: Endpoint,
                          _ types: [TypeEnum],
                                  _ dataJson: DeliveryChartPost.Result,
                                  _ apiKey: String) -> AnyPublisher<Data, NetworkError>
    {
        let user = Current.Shared.loggedUser?.userInfo
        let baseUrl = Resources.HOST_URL + Resources.API_URL + endPoint.path()
        var urlComps = URLComponents(string: baseUrl)!
        if urlComps.queryItems == nil { urlComps.queryItems = [] }
        
        var multiPart = MultipartRequest()
        
        let boundary = UUID().uuidString
        var request = URLRequest(url: URL(string: baseUrl)!)
        request.httpMethod = "POST"
        request.setValue(multiPart.httpContentTypeHeadeValue, forHTTPHeaderField: "Content-Type")
        
        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        let jsonData = try! encoder.encode(dataJson.chartJson)
        multiPart.add(key: "chartJson", fileName: "", fileMimeType: "application/json", fileData: jsonData)
       
        for img in dataJson.images {
            let mimeType = mimeTypeForPath(path: img.fileName)
            multiPart.add(key: "images", fileName: img.fileName, fileMimeType: mimeType, fileData: img.body)
        }
                
        var data = multiPart.httpBody
        
        for type in types {
            switch(type) {

            case .ApiKeyAndTokenHeader:
                request.addValue(apiKey, forHTTPHeaderField: "token")
                request.addValue(Resources.MESSAGE_API_KEY, forHTTPHeaderField: "apiKey")
            case .CompanyCdHeader:
                request.addValue(user?.companyCd ?? "", forHTTPHeaderField: "companyCd")
            case .CompanyCdQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "companyCd", value: user?.companyCd ?? ""))
            case .ApiKeyQuery:
                urlComps.queryItems?.append(URLQueryItem(name: "api_key", value: apiKey))
            case .PostUserInfo:
                break
            }
        }
        
        request.url =  urlComps.url //URL(from: "https://httpbin.org/post");
        URLSession.shared.configuration.waitsForConnectivity = true

        return Future<Data, NetworkError> { promise in
            let session = URLSession(configuration: URLSessionConfiguration.default, delegate: self, delegateQueue: OperationQueue.main)
            
            session.configuration.waitsForConnectivity = true
            session.configuration.timeoutIntervalForRequest = 60
            session.configuration.timeoutIntervalForResource = 60
            
            session.uploadTask(with: request, from: data) { data, response, error in
                
                if data == nil || error != nil {
                    debugPrint(error as Any)
                    promise(.failure(.parseError(reason: error!)))
                }
                
                promise(.success(data ?? Data()))
                
                if let httpResponse = response as? HTTPURLResponse {
                    debugPrint(httpResponse.statusCode)
                    if httpResponse.statusCode == 500 {
                        debugPrint(String(decoding: data!, as: UTF8.self))
                    }
                    
                }                 
            }.resume()
        }.eraseToAnyPublisher()
    }
}

extension API: URLSessionTaskDelegate {
    func urlSession(_ session: URLSession, task: URLSessionTask, didSendBodyData bytesSent: Int64, totalBytesSent: Int64, totalBytesExpectedToSend: Int64)
    {
        let uploadProgress: Double = Double(totalBytesSent) / Double(totalBytesExpectedToSend)
        
        if let callBackUpload = callBackUpload {
            callBackUpload(uploadProgress)
        }
    }
}


public struct MultipartRequest {
    
    public let boundary: String
    
    private let separator: String = "\r\n"
    private var data: Data

    public init(boundary: String = UUID().uuidString) {
        self.boundary = boundary
        self.data = Data()//.init()
    }
    
    private mutating func appendBoundarySeparator() {
        data.append("--\(boundary)\(separator)".data(using: .utf8)!)
    }
    
    private mutating func appendSeparator() {
        data.append(separator.data(using: .utf8)!)
    }

    private func disposition(_ key: String) -> String {
        "Content-Disposition: form-data; name=\"\(key)\""
    }

    public mutating func add(
        key: String,
        value: String
    ) {
        appendBoundarySeparator()
        data.append((disposition(key) + separator).data(using: .utf8)!)
        appendSeparator()
        data.append((value + separator).data(using: .utf8)!)
    }

    public mutating func add(
        key: String,
        fileName: String,
        fileMimeType: String,
        fileData: Data
    ) {
        appendBoundarySeparator()
        data.append((disposition(key) + "; filename=\"\(fileName)\"" + separator).data(using: .utf8)!)
        data.append(("Content-Type: \(fileMimeType)" + separator + separator).data(using: .utf8)!)
        data.append(fileData)
        appendSeparator()
    }

    public var httpContentTypeHeadeValue: String {
        "multipart/form-data; boundary=\(boundary)"
    }

    public var httpBody: Data {
        var bodyData = data
        bodyData.append("--\(boundary)--".data(using: .utf8)!)
        return bodyData
    }
}
