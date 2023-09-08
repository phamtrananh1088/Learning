//
//  FileRepository.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/02/22.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import MobileCoreServices

class FileRepository {
    func uploadSensorCsv(filename: String, body: Data) -> AnyPublisher<Result<Data>, NetworkError> {
        return Current.Shared.fileApi.uploadSensorCSV(fileName: filename, csv: body, apiKey: Current.Shared.loggedUser?.token ?? "")
    }
}
