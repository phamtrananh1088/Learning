//
//  CommonSensorCsvDao.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/02/21.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import GRDB

class CommonSensorCsvDao: BaseDbDao<ResultDb> {
    
    func selectAny() -> CommonSensorCsv? {
        do {
            return try executeDb.instanceDb?.read { db in
                try CommonSensorCsv.fetchOne(db)
            }
        } catch {
            debugPrint(error)
            return nil
        }
    }
}

