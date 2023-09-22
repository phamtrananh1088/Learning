//
//  HomeView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct HomeView: View {
    @ObservedObject var homeVm: HomeViewModel

    @State private var isShowAlert: Bool = false
    @State private var contentAlert: String = Resources.strEmpty
    @State private var isDisableHomeView: Bool = false

    var body: some View {
        NavigationStackView(navigationStack: homeVm.navigationStack) {
            GeometryReader { geometry in
                ZStack(alignment: .bottomLeading) {
                    Text(contentAlert).foregroundColor(.clear)
                    VStack {
                        // TabContent
                        TabView (selection: $homeVm.tabDisplay) {
                            // Tab ダッシュボード
                            DashBoardView(homeVm: homeVm, dashBoardVm: homeVm.dashBroadVm, isShowAlert: $isShowAlert, contentAlert: $contentAlert)
                                .tabItem {
                                    HomeEnum.dashboard.getImage()
                                    
                                    Text(Resources.home_navigation_dashboard)

                                }
                                .tag(ScreenDisplay.DashBoard)

                            // Tab 配送計画
                            DeliverView(homeVm: homeVm, deliverVm: homeVm.deliverVm, isShowAlert: $isShowAlert, contentAlert: $contentAlert, isDisableHomeView: $isDisableHomeView)
                                .tabItem {
                                    HomeEnum.deliver.getImage()
                                    Text(Resources.home_navigation_deliver)
                                }
                                .tag(ScreenDisplay.Deliver)

                            // Tab 作業入力
                            OperationView(homeVm: homeVm, operationVm: homeVm.operationVm)
                                .tabItem {
                                    if homeVm.badge {
                                        HomeEnum.operationWithBadge.getImage()
                                    } else {
                                        HomeEnum.operation.getImage()
                                    }
                                    
                                    Text(Resources.home_navigation_operation).foregroundColor(.red)
                                }
                                .tag(ScreenDisplay.Operation)
                                .onDisappear(perform: {
                                    homeVm.unsetAdd()
                                })

                            // Tab 給油
                            RefuelView(homeVm: homeVm, vm: homeVm.refuelVm)
                                .tabItem {
                                    HomeEnum.refuel.getImage()
                                    Text(Resources.home_navigation_refuel)
                                }
                                .tag(ScreenDisplay.Refuel)
                            
                            // Tab メッセージ
                            NavMessageView(homeVm: homeVm, navMessageVm: homeVm.roomLoadVM, onShowLoadFailed: {
                                self.contentAlert = Resources.load_failed
                                self.isShowAlert = true
                            })
                            .tabItem {
                                HomeEnum.chat.getImage()
                                Text(Resources.navigation_message)
                            }.tag(ScreenDisplay.Chat)
                            .modify {
                                if #available(iOS 15.0, *) {
                                    $0.badge(homeVm.unreadMessageCount)
                                } else {
                                    $0
                                }
                            }
                        }
                        .tabViewStyle(backgroundColor: Color(Resources.home_bnv_bg), itemColor: nil, selectedItemColor: .black, badgeColor: nil)
                    }
                    .edgesIgnoringSafeArea(.bottom)
                    
                    // Badge View
                    if #available(iOS 15.0,*) {
                        
                    } else {
                        if homeVm.unreadMessageCount > 0 {
                            ZStack {
                              Circle()
                                .foregroundColor(.red)

                                Text("\(homeVm.unreadMessageCount <= 999 ? String(homeVm.unreadMessageCount) : "999+")")
                                    .foregroundColor(.white)
                                    .font(Font.system(size: 12))
                            }
                            .frame(width: 18, height: 18)
                            .offset(x: ( ( 2 * 5) - 0.8 ) * ( geometry.size.width / ( 2 * 5 ) ), y: -30)
                            .opacity(34 == 0 ? 0 : 1)
                        }
                    }
                    
                    ChatUIView(vm: homeVm.chatVm, homeVm: homeVm, isShowChatRoom: $homeVm.isShowChatRoom, onBack: {
                        withAnimation {
                            homeVm.chatVm.lastItemRead = String()
                            homeVm.isShowChatRoom = false
                            homeVm.roomLoadVM.load()
                        }
                        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.3, execute: {
                            homeVm.chatVm.loadRoom(roomId: "")
                        })
                    })
                    .edgesIgnoringSafeArea(.horizontal)
                    
                    ChatOptionView(vm: homeVm.chatOptionVm, homeVm: homeVm, isShowChatOption: $homeVm.isShowChatOption, onBack: {
                        withAnimation {
                            homeVm.isShowChatOption = false
                        }
                    })
                    .edgesIgnoringSafeArea(.horizontal)
                    
                    PopUpBottomView(popupVm:homeVm.popupVm, show: $homeVm.isShowingAlert)
                    //PopUpWindow(popupVm: homeVm.popupVm, show: $homeVm.isShowingAlert)
                    
                    if homeVm.isShowBinStartProgress{
                        Color.black.opacity(0.5)
                    }
                }
            }
            
            .popup(isPresented: $homeVm.isShowSheetList, type:.toast, position: .bottom, dragToDismiss: false, closeOnTap: false, closeOnTapOutside: true, backgroundColor: Color.black.opacity(0.5)) {
                SheetListView(isPresented: $homeVm.isShowSheetList,
                                       homeVm: homeVm,
                                       sheetListVm: homeVm.sheetListVm, onDismiss: { isShow in })
            }
            
            .overlay(BinStartDialogView(
                isPresented: $homeVm.isShowUnscheduleBinStart,
                binStartDialogVm: homeVm.binStartDialogVm,
                changeLabel: {
                    Text(Resources.choose_vehicle)
                        .frame(maxWidth: 110)
                },
                doneLabel: {
                    HStack {
                        Text(Resources.operation_start)
                        Image(systemName: "arrow.down")
                            .padding(.leading)
                    }
                    .frame(maxWidth: 110)
                }, heightDialog: $homeVm.heigthBinStartProgress))
            
            // 配送計画一覧・運行開始押下時に起動されるイベント
            .overlay(BinStartDialogView(
                isPresented: $homeVm.isShowBinStart,
                binStartDialogVm: homeVm.binStartDialogVm,
                changeLabel: {
                    Text(Resources.change_vehicle)
                        .frame(maxWidth: 200)
                },
                doneLabel: {
                    Text(Resources.yes)
                        .frame(maxWidth: 200)
                },
                heightDialog: $homeVm.heigthBinStartProgress,
                modeUpdateTruck: true))
            
            .overlay(BinMeterInputView(isPresented: $homeVm.isShowbinMeterInputView, binMeterInputVm: homeVm.binMeterInputVm))
            
            /*給油 給油入力・便選択の押下時に起動されるイベント*/
            .popup(isPresented: $homeVm.isShowBinBottomPicker, type:.toast, position: .bottom, backgroundColor: Color.black.opacity(0.5)) {
                BinBottomPicker(isPresented: $homeVm.isShowBinBottomPicker, vm: homeVm.refuelVm)
            }
            
            .popup(isPresented: $homeVm.isShowFuelBottomPicker, type:.toast, position: .bottom, backgroundColor: Color.black.opacity(0.5)) {
                FuelBottomPicker(isPresented: $homeVm.isShowFuelBottomPicker, vm: homeVm.refuelVm)
            }
            
            .overlay(BinHeaderInfoDialogView(isPresented: $homeVm.isShowBinHeaderInfoDialogView, binHeaderInfoDialogVm: homeVm.binHeaderInfoDialogVm))
            
            .overlay(WorkModeDialogView(isPresented: $homeVm.isShowWorkModeDialogView, workModeDialogVm: homeVm.workModeDialogVm))
            .if (homeVm.isShowBinStartProgress) {
                $0.overlay(
                    VStack {
                        Spin(color: .blue)
                            .frame(maxWidth: .infinity, maxHeight: homeVm.heigthBinStartProgress, alignment: .center)
                            .background(Color.white)
                    }
                    .frame(maxHeight: .infinity, alignment: .bottom))
                    .edgesIgnoringSafeArea(.bottom)
            }
            .popup(isPresented: $isShowAlert, type: .toast, position: .bottom, animation: .easeInOut, autohideIn: 2, closeOnTap: true, closeOnTapOutside: true, view: {
                 VStack {
                     Text(contentAlert)
                        .foregroundColor(.white)
                        .font(.system(size: 15))
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                        .padding(.leading, 10)
                        .background(Color(Resources.textDark))
                        .cornerRadius(5)
                        .padding(EdgeInsets(top: 0, leading: 5, bottom: 55, trailing: 5))
                }
                 .frame(maxWidth: .infinity, maxHeight: 110)
            })
        }
        .onAppear(){
            homeVm.tabDisplay = .Deliver
        }
    }
    
    private func updateContentAlert(content: String) {
        contentAlert = content
    }
}

//struct HomeView_Previews: PreviewProvider {
//    static var previews: some View {
//        HomeView()
//    }
//}
