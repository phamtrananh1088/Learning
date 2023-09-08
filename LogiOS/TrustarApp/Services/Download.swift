//
//  Download.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/08.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

struct Download {
    func download(fileKey: String) -> AnyPublisher<(Result<Data>, Int), NetworkError> {
        return Current.Shared.chatFileApi.download(fileKey: fileKey, apiKey: Current.Shared.loggedUser?.token ?? "")
    }
}
