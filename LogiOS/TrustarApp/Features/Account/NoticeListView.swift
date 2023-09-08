//
//  NoticeListView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/26.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct NoticeListView: View {
    @ObservedObject var noticeListVm: NoticeListViewModel
    @EnvironmentObject private var navigationStack: NavigationStack
    
    private var lstNotice: [NoticeItemViewModel] {
        return noticeListVm.noticeList
    }
    
    var body: some View {
        ZStack {
            VStack (spacing: 0) {
                HomeHeaderView(isShowContent: false)
                
                Text("")
                ScrollView(.vertical) {
                        VStack {
                            if lstNotice.count == 0 {
                                Text(Resources.empty_placeholder)
                                    .foregroundColor(.white)
                                    .font(.system(size: 18))
                            } else {
                                ForEach(lstNotice) { notice in
                                    NoticeItemView(noticeItem: notice).onTapGesture {
                                        noticeListVm.onClickNotice(notice: notice.notice)
                                    }
                            }
                          }

                            Button(Resources.notice_mark_read) {

                                if Current.Shared.getBeforeScreen() == .Login {
                                    Current.Shared.changeScreenTo(screenName: .Weather)
                                } else {
                                    self.navigationStack.pop()
                                }
                                
                                noticeListVm.noticeRepo.markRead(rank: noticeListVm.rankNotice)
                                Current.Shared.syncNotice() { (_,_) in }
                                
                            }.buttonStyle(WhiteButtonStyle())
                                .padding(EdgeInsets(top: 56, leading: 0, bottom: 10, trailing: 0))

                       }
                        .frame(maxWidth: .infinity)
                        .padding()
                }
            }
            
            PopUpWindow(popupVm: noticeListVm.popupVm, show: $noticeListVm.isShowingAlert)
        }.frame(maxWidth:.infinity, maxHeight: .infinity, alignment: .center)
         .background(LinearGradient(gradient: Gradient(colors: [Color(Resources.colorPrimary),Color(Resources.colorPrimaryDark)]), startPoint: .top, endPoint: .bottom))
         .edgesIgnoringSafeArea(.vertical)
    }
}

//struct NoticeListView_Previews: PreviewProvider {
//    static var previews: some View {
//        NoticeListView()
//    }
//}
