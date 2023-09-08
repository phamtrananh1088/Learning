//
//  DateAndTimePickerDialog.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 21/02/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct DateAndTimePickerDialog: View {
    //@State var beginDateTime: String = ""
    //@State var endDateTime: String = ""
    @State var beginDateTime: Date = Date()
    @State var endDateTime: Date = Date()
    @State private var showDialog = true
    @State private var invalidTimeRange = false

    var body: some View {
        ZStack {
            VStack {
                Text(" ")
                Spacer()
                Text(" ")
            }
            .showCustomDialog(isShowing: $showDialog) {
                DoubleDateAndTimeView(beginDateTime: $beginDateTime,
                                      endDateTime: $endDateTime,
                                      callbackAction: self.updateShowDialog)
            }
            //show alert
            PopUpWindow(popupVm: PopupViewModel(isShowTitle: false,
                                                title: Resources.strEmpty,
                                                message: "Error",
                                                isShowLeftBtn: false,
                                                leftBtnText: Resources.strEmpty,
                                                isShowRightBtn: true,
                                                rightBtnText: Resources.ok,
                                                leftBtnClick: nil,
                                                rightBtnClick: nil),
                        show: $invalidTimeRange)
        }
        
    }
    
    func updateShowDialog() -> Void {
        if beginDateTime >= endDateTime {
            invalidTimeRange.toggle()
            return
        }
        showDialog.toggle()
    }
}

struct DateAndTimePickerView_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            DateAndTimePickerDialog()
        }
    }
}
