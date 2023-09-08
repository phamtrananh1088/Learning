//
//  ChatOptionView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/26.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine

struct ChatOptionView: View {
    @ObservedObject var vm: ChatOptionVM
    @ObservedObject var homeVm: HomeViewModel
    @Binding var isShowChatOption: Bool
    @State private var isShowEditRoom: Bool = false
    @EnvironmentObject private var navigationStack: NavigationStack
    
    var onBack: () -> Void
    
    private var title: String {
        if let r = vm.roomData {
            if let name = r.room.name {
                return name
            }
        }
        
        return String()
    }
    
    var body: some View {
        ZStack {
            ZStack {
                VStack(spacing: 0) {
                    // Region: Header
                    HomeHeaderView(isShowContent: false)
                    
                    HStack {
                        Button(action: {
                            onBack()
                        }, label: {
                            Image(systemName: "xmark")
                                .foregroundColor(.white)
                                .font(.system(size: 18))
                                .frame(alignment: .topLeading)
                                .padding(5)
                        })
                        
                        Text(title)
                            .foregroundColor(.white)
                            .bold()
                            .lineLimit(1)
                            .font(.system(size: 18))
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .padding(.vertical, 10)
                    .padding(.horizontal, 8)
                    .background(Color(Resources.colorPrimary))
                    
                    ScrollView {
                        VStack(spacing: 0) {
                            if let r = vm.roomData {
                                let room = r.room
                                let userCount = room.userCount
                                
                                Text(Resources.members_of_room_other.replacingOccurrences(of: "%d", with: userCount.description))
                                    .foregroundColor(Color(Resources.textColor))
                                    .font(.system(size: 16))
                                    .padding(8)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.vertical, 10)
                                    .background(Color.white)
                                    .padding(.top, 10)

                                Divider().padding(.horizontal, 5)
                                
                                HStack {
                                    Image(Resources.ic_avatar)
                                        .resizable()
                                        .scaledToFill()
                                        .frame(width: 48, height: 48)
                                    
                                    Text(Resources.add_user)
                                        .multilineTextAlignment(.leading)
                                        .lineLimit(nil)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                        .foregroundColor(.black)
                                        .font(.system(size: 16))
                                        .padding(.horizontal, 4)
                                }
                                .padding(.horizontal, 20)
                                .padding(.vertical, 10)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(Color.white)
                                .onTapGesture {
                                    withAnimation {
                                        let ids = vm.roomData?.users.map({ $0.id })
                                        homeVm.roomUserSelectionVM.excludeIds(ids: ids)
                                        isShowEditRoom = true
                                    }
                                }
                                
                                Divider().padding(.horizontal, 5)
                                
                                ForEach(vm.lstUserItem, id: \.self) { item in
                                    UserItemUIView(vm: item)
                                    Divider().padding(.horizontal, 5)
                                }
                                
                                Text(Resources.talk_room_settings)
                                    .foregroundColor(Color(Resources.textColor))
                                    .font(.system(size: 16))
                                    .padding(8)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.vertical, 10)
                                    .background(Color.white)
                                    .padding(.top, 10)
                                
                                Divider().padding(.horizontal, 5)
                                
                                Toggle(isOn: $vm.isNotificationOn.onUpdate {
                                    vm.notificationLiveData = .Loading
                                    
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.5, execute: {
                                        vm.notificationOnOff(roomId: vm.roomId, on: vm.isNotificationOn)
                                    })
                                }, label: {
                                    Text(Resources.allow_notification)
                                        .foregroundColor(Color(Resources.textColor))
                                        .font(.system(size: 18))
                                })
                                .padding(8)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.vertical, 10)
                                .background(Color.white)
                                
                                Divider().padding(.horizontal, 5)
                                
                                Button(Resources.quit_talk_room, action: {
                                    if let r = vm.roomData {
                                        homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.confirm, message: Resources.room_quit_s1_alert_msg.replacingOccurrences(of: "%s", with: r.room.name.orEmpty()), isShowLeftBtn: true, leftBtnText: Resources.cancel, isShowRightBtn: true, rightBtnText: Resources.quit, rightBtnClick: { vm.leaveRoom(roomId: r.room.id) }))
                                    }
                                    
                                })
                                .foregroundColor(Color(Resources.textColor))
                                .font(.system(size: 16))
                                .padding(8)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.vertical, 10)
                                .background(Color.white)
                            }
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    
                    Rectangle()
                        .fill(.clear)
                        .frame(maxWidth: .infinity, maxHeight: 0.1)
                }
                .edgesIgnoringSafeArea(.top)
                .background(Color(Resources.defaultBackground))
                .onReceive(vm.$roomData, perform: { room in
                    if room != nil {
                        homeVm.roomUserSelectionVM.excludeIds(ids: room!.users.map({ $0.id }))
                    }
                })
            }
            .transition(.move(edge: .trailing))
            .onAppear() {
                vm.notiCallback = {
                    isShowChatOption = false
                    homeVm.roomLoadVM.load()
                }
                
                vm.leaveCallback = {
                    isShowChatOption = false
                    homeVm.isShowChatRoom = false
                    homeVm.roomLoadVM.load()
                }
            }
            .onSwipeToBack(isPresented: $isShowChatOption, action: {
                isShowChatOption = false
            })
            
            EditRoomView(isShow: $isShowEditRoom, homeVm: homeVm, roomUserSelectionVm: homeVm.roomUserSelectionVM, editRoomVm: homeVm.editRoomVm, callBack: {
                withAnimation {
                    isShowEditRoom = false
                    isShowChatOption = false
                    Helper.Shared.sendBroadcastNotification(.Chat)
                }
            }).edgesIgnoringSafeArea(.top)
            
            if vm.notiLoading == .Loading || vm.leaveLoading == .Loading {
                ShowProgressDialog(title: Resources.processing, message: "")
            }

            PopUpWindow(popupVm: vm.popupVm, show: $vm.isShowingAlert)
        }
    }
}
//struct ChatOptionView_Previews: PreviewProvider {
//    static var previews: some View {
//        ChatOptionView()
//    }
//}
