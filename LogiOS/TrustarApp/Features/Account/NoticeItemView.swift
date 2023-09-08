//
//  NoticeItemView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct NoticeItemView: View {
    
    @ObservedObject var noticeItem: NoticeItemViewModel
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                HStack {
                    // Title
                    Text(noticeItem.notice.noticeTitle)
                        .padding(4)
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundColor(Color(Resources.accentOrange))
                    // Is new
                    if noticeItem.notice.unreadFlag == 1 {
                        Text(Resources.new)
                            .foregroundColor(.red)
                            .fontWeight(.heavy)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                // Content
                Text(noticeItem.notice.noticeText)
                    .padding(4)
                    .font(.system(size: 16))
                    .foregroundColor(Color(Resources.textDark))
                    .frame(maxWidth: .infinity, alignment: .leading)

            }
            .background(Color.white)
            .frame(maxWidth: .infinity)
            .cornerRadius(4)
            .padding(EdgeInsets(top: 1, leading: 0, bottom: 1, trailing: 0))
            .edgesIgnoringSafeArea(.all)
        }
    }
}

struct NoticeItemView_Previews: PreviewProvider {
    static var previews: some View {
        NoticeItemView(noticeItem: NoticeItemViewModel(notice: Notice()))
    }
}
