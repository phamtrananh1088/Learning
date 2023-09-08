//
//  DeliverView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/16.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine

struct DeliverView: View {
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var deliverVm: DeliverViewModel
    
    @EnvironmentObject var modelRotation: ModelRotation
    @Binding var isShowAlert: Bool
    @Binding var contentAlert: String
    @Binding var isDisableHomeView: Bool
    
    @State private var isStickyHeader = false
    @State private var heightOperation = CGFloat.zero
    @State private var inputBinMeter: String = Resources.strEmpty
    
    var content: some View {
        VStack (spacing: 0) {
            // Region: Control
            HStack {
                VStack (spacing: 16) {
                    Text(deliverVm.operation)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .foregroundColor(.black)
                        .font(.system(size: 20))
                        .padding(EdgeInsets(top: 16, leading: 8, bottom: 0, trailing: 0))
                    Text(deliverVm.carModel)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .foregroundColor(.black)
                        .font(.system(size: 20))
                        .padding(EdgeInsets(top: 0, leading: 8, bottom: 16, trailing: 0))
                }

                if deliverVm.binStatusType == .Working && Current.Shared.loggedUser?.userInfo.isGeofenceUseFlag() == true {
                    Toggle(isOn: $homeVm.deliverVm.isAutoMode.onUpdate {
                        homeVm.workModeDialogVm = WorkModeDialogViewModel(allocationNo: homeVm.deliverVm.allocationNo)
                        homeVm.workModeDialogVm.setAutoMode(state: homeVm.deliverVm.isAutoMode)

                        var listNotShowTipAutoMode: String? = Helper.Shared.readValue(key: .notAskAutoMode)
                        let userId = Current.Shared.loggedUser!.userInfo.userId
                        if listNotShowTipAutoMode == nil || !listNotShowTipAutoMode!.components(separatedBy: ",").contains(where: { $0 == userId}) {
                            homeVm.showPopupView(
                                popupVm: PopupViewModel(
                                    isShowTitle: true,
                                    title: homeVm.deliverVm.isAutoMode ? Resources.tip_automatic_title : Resources.tip_manual_title,
                                    message: homeVm.deliverVm.isAutoMode ? Resources.tip_automatic_msg : Resources.tip_manual_msg,
                                    isShowLeftBtn: false, leftBtnText: String(),
                                    isShowRightBtn: true, rightBtnText: Resources.ok,
                                    leftBtnClick: nil,
                                    rightBtnClick: nil,
                                    isShowCheckBox: true,
                                    rightBtnClickWithCheckBox: { value in
                                        if value {
                                            if listNotShowTipAutoMode == nil {
                                                listNotShowTipAutoMode = userId
                                            } else {
                                                listNotShowTipAutoMode?.append(",\(userId)")
                                            }
                                            
                                            Helper.Shared.saveValue(key: .notAskAutoMode, value: listNotShowTipAutoMode!)
                                        }
                                    }))
                        }
                    }) {
                        Text(deliverVm.isAutoMode ? Resources.work_mode_automatic : Resources.work_mode_manual)
                            .foregroundColor(.black)
                            .fontWeight(.bold)
                            .frame(maxWidth: .infinity, alignment: .trailing)
                    }
                    .padding(.trailing, 10)
                    .fixedSize()
                }
            }
            .background(Color(Resources.defaultBackground))
            .if(modelRotation.landscape) {
                $0.overlay(
                    GeometryReader { geo in
                        Color.clear.onAppear {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                heightOperation =  geo.size.height
                            }
                        }
                    })
            }
            .if(!modelRotation.landscape) {
                $0.overlay(
                    GeometryReader { geo in
                        Color.clear.onAppear {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                heightOperation =  geo.size.height
                            }
                        }
                    })
            }
            
            DeliverButtonView(binStatusSwitchText: deliverVm.binStatusSwitchText, binStatusSwitchColor: deliverVm.binStatusSwitchColor, binStatusSwitchDisable: deliverVm.binStatusSwitchDisable, binStatusType: deliverVm.binStatusType, workAddEnable: deliverVm.workAddEnable, operationAreaBackground: deliverVm.operationAreaBackground, binStatusSwitchClick: actionClickBinStatusSwitch, workAddClick: actionWorkAdd)

            VStack (spacing: 1) {
                if deliverVm.binDetailWithStatusList.count > 0 {
                    ForEach((0...deliverVm.binDetailWithStatusList.count - 1), id: \.self) { idx in
                        WorkItemView(workItemVm: deliverVm.binDetailWithStatusList[idx], rowIdx: idx, iconClick: iconClick(idx:), rowClick: rowClick(idx:))
                    }
                }
            }
        }.background(GeometryReader {
            Color.clear.preference(key: ViewOffsetKey.self,
                value: -$0.frame(in: .named("scroll")).origin.y)
        })
            .onPreferenceChange(ViewOffsetKey.self) { if $0 > heightOperation {
                if !isStickyHeader {
                    isStickyHeader = true
                }

            } else {
                if isStickyHeader {
                    isStickyHeader = false
                }
            }}
    }
    
    var body: some View {
        ZStack {
            VStack (spacing: 0) {
                // Region: Header
                HomeHeaderView()
                
                ZStack {
                    ScrollView {
                        content
                    }
                    .refreshableCompat(showsIndicators: true,
                                       loadingViewBackgroundColor: Color(Resources.defaultBackground),
                                       threshold: 68,
                                       onRefresh: { done in
                        deliverVm.doneRefresh = done
                        deliverVm.current.syncBin() { (_, _) in done() }
                        DispatchQueue.main.asyncAfter(deadline: .now() + 30, execute: {
                            done()
                        })
                    }, progress: { state in
                        if state == .waiting {
                            Circle()
                                .trim(from: 1/4, to: 1)
                                .stroke(style: StrokeStyle(lineWidth: 3, lineCap: .round,lineJoin: .round))
                                .foregroundColor(.blue)
                                .frame(width: 32, height: 32)
                        } else if state == .primed {
                            Circle()
                                .trim(from: 1/4, to: 1)
                                .stroke(style: StrokeStyle(lineWidth: 3, lineCap: .round,lineJoin: .round))
                                .foregroundColor(.blue)
                                .frame(width: 32, height: 32)
                        } else {
                            Spin(color: .blue)
                        }
                    })
                    .coordinateSpace(name: "scroll")

                    DeliverButtonView(binStatusSwitchText: deliverVm.binStatusSwitchText, binStatusSwitchColor: deliverVm.binStatusSwitchColor, binStatusSwitchDisable: deliverVm.binStatusSwitchDisable, binStatusType: deliverVm.binStatusType, workAddEnable: deliverVm.workAddEnable, operationAreaBackground: deliverVm.operationAreaBackground, binStatusSwitchClick: actionClickBinStatusSwitch, workAddClick: actionWorkAdd)
                        .frame(maxHeight: .infinity, alignment: .top)
                        .if(!isStickyHeader) {
                            $0.hidden()
                        }
                }
            }
            
            HStack {
                if deliverVm.endState == .Loading {
                    ShowProgressDialog(title: Resources.data_end_data_sending, message: Resources.data_end_data_sending_msg).onAppear(perform: {
                        isDisableHomeView = true
                    })
                } else {
                    Color.clear.onAppear(perform: {
                        isDisableHomeView = false
                    })
                }
            }
        }
        .onTapGesture {
            homeVm.isShowbinMeterInputView = false
            inputBinMeter = Resources.strEmpty
            self.endTextEditing()
        }
        .background(Color(Resources.defaultBackground))
        .edgesIgnoringSafeArea(.top)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .sheet(isPresented: $deliverVm.isShowBinDetailDialog, onDismiss: nil, content: {
            BinDetailDialog(homeVm: homeVm, binDetailDialogVm: homeVm.binDetailDialogVm, onDismiss: { isShow in
                deliverVm.isShowBinDetailDialog = isShow
            })
        })
    }
    
    private func confirmClick(value: String) {
        //endBin
        homeVm.isShowbinMeterInputView = false
        deliverVm.endBin(weather: Helper.Shared.getLastWeather()!, location: deliverVm.current.lastLocation, setIncomingOrNot: Int(value)!)
        Helper.Shared.sendBroadcastNotification(.Bin)
    }
    
    private func actionClickBinStatusSwitch() {
        if deliverVm.binHeaderSelected?.status.binStatusCd == BinStatusEnum.Ready.rawValue {
            if homeVm.countBinHeader(status: .Working) > 0 {
                showAlert(Resources.start_when_started_operation_exists_msg)
            } else {
                /*配送計画一覧・運行開始押下時に起動されるイベント*/
                withAnimation {
                    homeVm.isShowBinStart = true
                }
                
                let bin = Current.Shared.userRepository?.binHeaderRepo.selectBinHeaderWithStatus(allocationNo: deliverVm.binHeaderSelected!.header.allocationNo)
                homeVm.binStartDialogVm = BinStartDialogViewModel(
                    bin: bin,
                    doneClick: { (token, selected, meterInput) in
                        if selected != nil {
                            if Current.Shared.loggedUser?.userInfo.meterInputEnable() == true {
                                homeVm.binMeterInputVm = BinMeterInputViewModel(
                                    tilte1: Resources.outgoing_meter_input_title,
                                    title2: Resources.outgoing_meter_in_km,
                                    cancelText: Resources.input_later,
                                    cancelClick: {
                                        startOrSelectWorkMode()
                                        dismiss()
                                    },
                                    confirmText: nil,
                                    confirmClick: { value in
                                        Current.Shared.userRepository!.binHeaderRepo.setOutgoingMeter(binHeader: bin!.header, kilometer: Int(value))
                                        Current.Shared.syncBin(callBack: {(isOk, Error) in })
                                        startOrSelectWorkMode()
                                        dismiss()
                                    }, input: meterInput ?? String())
                                
                                withAnimation {
                                    homeVm.isShowbinMeterInputView = true
                                }
                            } else {
                                startOrSelectWorkMode()
                                dismiss()
                            }
                        } else {
                            dismiss()
                            return
                        }
                    })
            }
        } else if deliverVm.binHeaderSelected?.status.binStatusCd == BinStatusEnum.Working.rawValue {
            let allocationNo = deliverVm.binHeaderSelected!.header.allocationNo
            // Call endBinHeader
            // parameter: countReade, countWorking
            endBinHeader(countReady: homeVm.countBinDetail(status: .Ready, allocationNo: allocationNo),
                         countWorking: homeVm.countBinDetail(status: .Working, allocationNo: allocationNo))
        }
    }
    
    private func startOrSelectWorkMode() {
        if Current.Shared.loggedUser?.userInfo.isGeofenceUseFlag() == true {
            homeVm.workModeDialogVm = WorkModeDialogViewModel(allocationNo: deliverVm.binHeaderSelected!.header.allocationNo)
            homeVm.isShowWorkModeDialogView = true
        } else {
            homeVm.workModeDialogVm = WorkModeDialogViewModel(allocationNo: deliverVm.binHeaderSelected!.header.allocationNo)
            homeVm.workModeDialogVm.start(workMode: WorkMode(mode: .manual), location: Current.Shared.lastLocation)
        }
    }
    
    private func dismiss() {
        withAnimation {
            homeVm.isShowbinMeterInputView = false
            homeVm.isShowBinStart = false
        }
    }
    
    private func actionWorkAdd() {
        if deliverVm.binHeaderSelected?.header == nil || deliverVm.binHeaderSelected?.status.getStatus() == .Finished {
            // Do not thing
        } else {
            // Navigate 作業入力 mode create
            // parameter: binheader
            navigationWorkAddNew(bin: deliverVm.binHeaderSelected!.header)
        }
    }
    
    private func endBinHeader(countReady: Int, countWorking: Int) {
        if countWorking > 0 {
            showAlert(Resources.end_when_working_exists_msg)
            return
        }
        
        let lastWeather = Helper.Shared.getLastWeather()
        if lastWeather == nil {
            showAlert(Resources.weather_need_to_be_set_msg)
            return
        }
        
        if let header = deliverVm.binHeaderSelected?.header {
            if let incommingMeter = header.incomingMeter {
                inputBinMeter = String(incommingMeter)
            }
            
            if countReady > 0 {
                homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.end_when_ready_exists_alert_title, message: Resources.end_when_ready_exists_alert_msg, isShowLeftBtn: true, leftBtnText: Resources.cancel, isShowRightBtn: true, rightBtnText: Resources.operation_end, leftBtnClick: nil, rightBtnClick: okClickEndBinHeader))
            } else {
                homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.confirm, message: Resources.operation_end_alert_msg, isShowLeftBtn: true, leftBtnText: Resources.cancel, isShowRightBtn: true, rightBtnText: Resources.operation_end, leftBtnClick: nil, rightBtnClick: okClickEndBinHeader))
            }
        }
    }
    
    private func okClickEndBinHeader() {
        if deliverVm.current.loggedUser?.userInfo.meterInputFlag == 1 {
            homeVm.binMeterInputVm = BinMeterInputViewModel(
                tilte1: Resources.incoming_meter_input_title,
                title2: Resources.incoming_meter_in_km,
                cancelText: nil,
                cancelClick: {
                    homeVm.isShowbinMeterInputView = false
                },
                confirmText: nil,
                confirmClick: self.confirmClick(value:),
                input: inputBinMeter
            )
                                                       
            homeVm.isShowbinMeterInputView = true
        } else {
            deliverVm.endBin(weather: Helper.Shared.getLastWeather()!, location: deliverVm.current.lastLocation, setIncomingOrNot: nil)
        }
    }
    
    private func iconClick(idx: Int) {
        let row = deliverVm.binDetailWithStatusList[idx]
        
        homeVm.binDetailDialogVm = BinDetailDialogModel(binDetail: row.detail)
        //deliverVm.binDetailDialogVm = row.detail
        showBinDetailDialog(true)
    }
    
    private func rowClick(idx: Int) {
        let row = deliverVm.binDetailWithStatusList[idx]
        if let binStatus = deliverVm.binHeaderSelected?.status {
            let notAutoMode = !deliverVm.isAutoMode
            switch binStatus.getStatus() {
            case .Finished:
                if notAutoMode {
                    showAlert(Resources.work_after_operation_finished_tip)
                }
            case .Ready:
                if notAutoMode {
                    showAlert(Resources.work_before_operation_start_tip)
                }
            case .Working:
                let binDetail = row.detail
                
                if row.status.getStatus() == .Moving || row.status.getStatus() == .Working {
                    if notAutoMode {
                        navigationBinDetail(binDetail: binDetail)
                    }
                } else if homeVm.countBinDetail(status: .Working, allocationNo: deliverVm.binHeaderSelected!.header.allocationNo) > 0 {
                    if notAutoMode {
                        showAlert(Resources.move_work_when_working_exists_msg)
                    }
                } else if row.status.getStatus() == .Finished {
                    if notAutoMode {
                        navigationBinDetail(binDetail: binDetail)
                    }
                } else if row.status.getStatus() == .Ready {
                    if notAutoMode {
                        homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.work_move_to_destination_title, message: Resources.work_move_to_destination_msg, isShowLeftBtn: true, leftBtnText: Resources.cancel, isShowRightBtn: true, rightBtnText: Resources.work_move, leftBtnClick: nil, rightBtnClick: { navigationBinDetail(binDetail: binDetail) }))
                    } else if binDetail.placeLatitude == nil && binDetail.placeLongitude == nil {
                        if let l = deliverVm.current.lastLocation {
                            homeVm.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: Resources.strEmpty, message: Resources.work_start_without_dest_location_msg, isShowLeftBtn: true, leftBtnText: Resources.no, isShowRightBtn: true, rightBtnText: Resources.yes, leftBtnClick: nil, rightBtnClick: { deliverVm.startInAutoMode(binDetail: binDetail, location: l)
                                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1.2) {
                                    Helper.Shared.sendBroadcastNotification(.Bin)
                                }
                            }))
                        }
                    }
                }
            }
        }
    }
    
    private func navigationWorkAddNew(bin: BinHeader) {
        homeVm.workAdd(bin: bin)
        
    }
    
    private func navigationBinDetail(binDetail: BinDetail) {
        homeVm.navigationBinDetail(binDetail: binDetail, goDefault: false)
    }
    
    private func showAlert(_ content: String) {
        contentAlert = content
        isShowAlert = true
    }
    
    private func showBinDetailDialog(_ isShow: Bool = true) {
        deliverVm.isShowBinDetailDialog = isShow
    }
}

//struct DeliverView_Previews: PreviewProvider {
//    static var previews: some View {
//        DeliverView(deliverVm: DeliverViewModel(allocationNo: ""), tabBar: .init(projectedValue: .constant(UITabBar())))
//    }
//}
