//
//  DeliverButtonView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct DeliverButtonView: View {
    
    var binStatusSwitchText: String
    var binStatusSwitchColor: UIColor
    var binStatusSwitchDisable: Bool
    var binStatusType: BinStatusEnum
    var workAddEnable: Bool
    var operationAreaBackground: UIColor

    var binStatusSwitchClick: () -> ()
    var workAddClick: () -> ()

    var body: some View {
        HStack {
            // binStatusWitch
            Button(action: {
                binStatusSwitchClick()
            }, label: {
                HStack(spacing: 5) {
                    Text(binStatusSwitchText)
                        .foregroundColor(Color(binStatusSwitchColor))
                    if binStatusType == .Ready {
                        ic_bin_start()
                            .foregroundColor(Color(binStatusSwitchColor))
                            
                    } else {
                        ic_bin_stop()
                            .foregroundColor(Color(binStatusSwitchColor))
                    }
                }
            })
                .buttonStyle(WhiteButtonStyle())
                .disabled(binStatusSwitchDisable)
                .padding(10)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            // workAdd
            Button(action: {
                workAddClick()
            }, label: {
                Text(Resources.work_add)
            })
                .buttonStyle(WhiteButtonSmallStyle())
                .padding(10)
                .disabled(!workAddEnable)
        }
        .frame(maxWidth: .infinity)
        .background(Color(operationAreaBackground))
    }
}
