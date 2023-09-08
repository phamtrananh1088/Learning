//
//  DateAndTimePickerView.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 21/02/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct DateAndTimePickerView: View {
    let dateFormatter: DateFormatter = {
            let f = DateFormatter()
            f.dateFormat = "MM月dd日 HH:mm"
            //f.timeStyle = .short
            //f.dateStyle = .shor
            f.locale = Locale(identifier: "ja_JP")
            return f
        }()
    
    @Binding var dateValue: Date
    @Binding var sideOfDateType: Int
    var callbackAction: () -> Void
    
    var body: some View {
        VStack{
            DatePicker("DateTime", selection: $dateValue, displayedComponents: [.date, .hourAndMinute])
                .datePickerStyle(WheelDatePickerStyle())
                .labelsHidden()
                .colorMultiply(Color.black)
                .environment(\.locale, Config.Shared.locale)
            HStack {
                Spacer()
                Button {
                    if sideOfDateType == 0 {
                        //choose begin datetime then goto end datetime
                        sideOfDateType = 1
                    } else {
                        callbackAction()
                    }
                    ////self.presentation.wrappedValue.dismiss()
                    //strDateTime = Config.Shared.dateFormatterMMddHHmm.format(date: date)
                } label: {
                    Text(Resources.determine)
                        .foregroundColor(Color(Resources.colorPrimary))
                }
                .padding()
            }
        }
        .environment(\.locale, Config.Shared.locale)
    }
}
