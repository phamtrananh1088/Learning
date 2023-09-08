//
//  WorkMode.swift
//  TrustarApp
//
//  Created by CuongNguyen on 22/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

enum WorkModeEnum {
    case manual
    case automatic
}

class WorkMode {
    var string: String
    var mode: WorkModeEnum
    init(mode: WorkModeEnum) {
        self.mode = mode
        switch mode {
        case .manual:
            self.string = Resources.work_mode_manual
        case .automatic:
            self.string = Resources.work_mode_automatic
        }
    }
}
