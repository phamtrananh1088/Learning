//
//  NavMessageView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct NavMessageView: View {
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var navMessageVm: RoomLoadVM
    @EnvironmentObject private var navigationStack: NavigationStack
    let timer = Timer.publish(every: 60, on: .main, in: .common).autoconnect()
    var onShowLoadFailed: () -> Void
    private var isLoading: Bool {
        switch navMessageVm.loadStateLiveData {
        case .Loading:
            return true
        default:
            return false
        }
    }
    
    var body: some View {
        VStack(spacing: 0) {
            // Region: Header
            HomeHeaderView(isShowToolbar: true,
                           lstControlToolbars: [(Resources.create_talk_room, {
                homeVm.roomUserSelectionVM.excludeIds(ids: [Current.Shared.userId()])
                self.navigationStack.push(UserSelectionUIView(homeVm: homeVm, roomUserSelectionVm: homeVm.roomUserSelectionVM, addRoomVm: homeVm.addRoomVm))
            })], isChatView: true)

            ScrollView {
                VStack (spacing: 10) {
                    if navMessageVm.listLiveData.count > 0 {
                        ForEach((0...navMessageVm.listLiveData.count - 1), id: \.self) { idx in
                            let item = navMessageVm.listLiveData[idx]
                            Button(action: {
                                homeVm.chatVm.loadRoom(roomId: item.roomId)
                                withAnimation {
                                    homeVm.isShowChatRoom = true
                                }
                            }, label: {
                                RoomItemUIView(roomItemVm: item)
                            })
                            
                            Divider().padding(.horizontal, 2)
                        }
                    }
                }
            }
            .refreshableCompat(showsIndicators: true,
                               loadingViewBackgroundColor: Color(Resources.defaultBackground),
                               threshold: 68,
                               onRefresh: { done in
                withAnimation {
                    done()
                }

                navMessageVm.load()
            }, progress: { state in })
            .overlay(
                ZStack {
                    if isLoading {
                        ZStack {
                            Circle()
                                .stroke(
                                    style: StrokeStyle(
                                        lineWidth: 0.3,
                                        lineCap: .round,
                                        lineJoin: .round))
                                .foregroundColor(.black)
                                .background(Color.white)
                                .clipShape(Circle())
                                .frame(width: 40, height: 40)
                            Spin(color: .black, size: 15)
                        }
                        .padding(.top)
                    }
                }, alignment: .top)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(Resources.defaultBackground))
        .edgesIgnoringSafeArea(.top)
        .onAppear() {
            navMessageVm.onShowLoadFailed = onShowLoadFailed
            navMessageVm.load()
        }
        .onReceive(timer, perform: {(_) in
            navMessageVm.load()
        })
    }
}

//struct NavMessageView_Previews: PreviewProvider {
//    static var previews: some View {
//        NavMessageView()
//    }
//}
