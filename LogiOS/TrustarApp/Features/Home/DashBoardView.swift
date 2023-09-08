//
//  DashBoardView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/20.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct DashBoardView: View {
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var dashBoardVm: DashBoardViewModel
    
    @State private var isFirst: Bool = true
    @State var isShowPopover: Bool = false
    
    @Binding var isShowAlert: Bool
    @Binding var contentAlert: String
    @EnvironmentObject private var navigationStack: NavigationStack
    
    private var listDisplay: [BinHeadViewModel] {
        if dashBoardVm.showAll {
            return dashBoardVm.listBinHead
        } else {
            return dashBoardVm.listBinHead.filter { !$0.hasFinished }
        }
    }
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                // Region: Header
                HomeHeaderView(isShowToolbar: true, lstControlToolbars: dashBoardVm.lstControlToolbar)

                // Region: UserName, Notification
                HStack(alignment: .top) {
                    Text(dashBoardVm.fullName)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .font(.system(size: 20))
                        .padding()
                        .foregroundColor(.black)
                    
                    VStack(spacing: 0) {
                        BellWithTextView(isImportant: true, totalUnread: dashBoardVm.unreadImportant)

                        Text(Resources.dashboard_important)
                            .font(.system(size: 15))
                            .foregroundColor(.black)
                    }
                    .onTapGesture {
                        self.navigationStack.push(NoticeListView(noticeListVm: NoticeListViewModel(typeNotice: 1)))
                    }
                    .padding(EdgeInsets(top: 5, leading: 0, bottom: 5, trailing: 0))
                    
                    VStack(spacing: 0) {
                        BellWithTextView(isImportant: false, totalUnread: dashBoardVm.unreadNormal)
                        
                        Text(Resources.dashboard_normal)
                            .font(.system(size: 15))
                            .foregroundColor(.black)
                    }
                    .onTapGesture {
                        self.navigationStack.push(NoticeListView(noticeListVm: NoticeListViewModel(typeNotice: 2)))
                        
                    }
                    .padding(EdgeInsets(top: 5, leading: 0, bottom: 5, trailing: 0))
                }
                .frame(maxWidth: .infinity)
                .background(Color(Resources.defaultBackground))
                
                // Region: count work
                HStack {
                    VStack {
                        Text(Resources.today_operation_amount_d.replacingOccurrences(of: "{0}", with: String.init(dashBoardVm.operationAmount)))
                            .font(.system(size: 16, weight: .medium))
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundColor(.white)
                            .padding(EdgeInsets(top: 8, leading: 8, bottom: 0, trailing: 8))
                        Text(Resources.today_operation_remain_d.replacingOccurrences(of: "{0}", with: String.init(dashBoardVm.operationRemain)))
                            .font(.system(size: 16, weight: .medium))
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .foregroundColor(.white)
                            .padding(EdgeInsets(top: 0, leading: 8, bottom: 8, trailing: 8))
                    }

                    Toggle(isOn: $dashBoardVm.showAll) {
                        Text(Resources.show_all)
                            .foregroundColor(.white)
                            .font(.system(size: 16, weight: .medium))
                            .frame(maxWidth: .infinity, alignment: .trailing)
                    }.padding(.trailing)
                }
                .background(Color(Resources.colorPrimary))
                
                // List
                ScrollView {
                    VStack(spacing: 2) {
                        ForEach(listDisplay, id: \.self) { bin in
                            BinHeadView(binHeadVm: bin, onTapRow: onTapRow(allocationNo:), onTapInfo: onTapInfo(allocationNo:))
                        }
                    }
                    
                    Button (action: {
                        let totalWorking = homeVm.countBinHeader(status: .Working)
                        
                        if totalWorking > 0 {
                            raiseAlertError()
                        } else {
                            withAnimation {
                                homeVm.binStartDialogVm = BinStartDialogViewModel(
                                    doneClick: { (token, selected, meterInput) in
                                        if Current.Shared.loggedUser!.userInfo.meterInputEnable() {
                                            homeVm.binMeterInputVm = BinMeterInputViewModel(
                                                tilte1: Resources.outgoing_meter_input_title,
                                                title2: Resources.outgoing_meter_in_km,
                                                cancelText: Resources.input_later,
                                                cancelClick: {
                                                    homeVm.startUnscheduledBin(
                                                        token: token,
                                                        truck: selected!,
                                                        outgoingMeter: nil,
                                                        raiseAlertError: raiseAlertError,
                                                        raisePopupError: raisePopupError)
                                                    homeVm.isShowbinMeterInputView = false
                                                },
                                                confirmText: nil,
                                                confirmClick: { value in
                                                    if !homeVm.binMeterInputVm.input.isEmpty {
                                                        homeVm.startUnscheduledBin(
                                                            token: token,
                                                            truck: selected!,
                                                            outgoingMeter: Int(homeVm.binMeterInputVm.input),
                                                            raiseAlertError: raiseAlertError,
                                                            raisePopupError: raisePopupError)
                                                    }
                                                }, input: String())
                                            
                                            homeVm.isShowbinMeterInputView = true
                                        } else {
                                            homeVm.startUnscheduledBin(
                                                token: token,
                                                truck: selected!,
                                                outgoingMeter: nil,
                                                raiseAlertError: raiseAlertError,
                                                raisePopupError: raisePopupError)
                                        }
                                    }
                                )
                                homeVm.isShowUnscheduleBinStart = true
                            }
                        }
                    }, label: {
                        HStack {
                            Image(systemName: "plus.circle.fill")
                                .font(.system(size: 20))
                                .foregroundColor(Color(Resources.colorPrimary))
                                .padding(12)
                            Text(Resources.operation_add_unscheduled)
                                .font(.system(size: 15))
                                .foregroundColor(.black)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                    })
                }
                .background(Color(Resources.defaultBackground))
                .frame(maxWidth: .infinity)
                Spacer()
            }
        }
        .edgesIgnoringSafeArea(.top)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(Resources.defaultBackground))
    }
    
    private func raiseAlertError() {
        contentAlert = Resources.start_when_started_operation_exists_msg
        isShowAlert = true
    }
    
    private func raisePopupError() {
        homeVm.isShowbinMeterInputView = false
        homeVm.isShowUnscheduleBinStart = false
        homeVm.isShowBinStartProgress = false
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.5, execute: {
            homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.server_connection_err, message: Resources.start_bin_connection_err_alert_msg, isShowLeftBtn: false, leftBtnText: String(), isShowRightBtn: true, rightBtnText: Resources.ok, leftBtnClick: nil, rightBtnClick: nil))
        })
    }
    
    private func onTapRow(allocationNo: String) {
        homeVm.navigationBinHeader(allocationNo: allocationNo, goDefault: false)
        homeVm.tabDisplay = .Deliver
    }
    
    private func onTapInfo(allocationNo: String) {
        homeVm.binHeaderInfoDialogVm.onNext(allocationNo: allocationNo)
        homeVm.isShowBinHeaderInfoDialogView = true
    }
}
