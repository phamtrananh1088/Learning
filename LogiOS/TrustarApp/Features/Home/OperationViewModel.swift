//
//  OperationViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 04/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import SwiftUI

class FlexboxLayoutModel {
    var order: Int
    var text: String
    var isDisable: Bool
    var background: Color
    var matched: Bool
    var work: Work
    var callback: ((Work) -> Void)?

    init(order: Int, text: String, isDisable: Bool, background: Color, matched: Bool, work: Work, callback: ((Work) -> Void)?) {
        self.order = order
        self.text = text
        self.isDisable = isDisable
        self.background = background
        self.matched = matched
        self.work = work
        self.callback = callback
    }
}

class OperationViewModel: BaseViewModel, ObservableObject {
    var homeVm: HomeViewModel?
    @Published var isShowBinDetailDialog: Bool = false
    
    init(homeVm: HomeViewModel?) {
        self.homeVm = homeVm
        super.init()
        getData()
    }
    
    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Bin.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {
        switch notification.name.rawValue {
        case BroadcastEnum.Bin.rawValue:
            getData()
        default:
            break
        }
    }
    
    @Published var workingGroup: Bool = false
    @Published var placeGroup: Bool = false
    @Published var timeGroup: Bool = false
    @Published var timeRange: String = String()
    
    @Published var place1Show: Bool = false
    @Published var place1: String = String()
    
    @Published var nimachiShow: Bool = false
    @Published var nimachi: String = String()
    
    @Published var place2: String = String()
    @Published var address: String = String()
    
    @Published var clickInfoColor: Color = Color.red
    @Published var workStatus: String = String()
    @Published var workStartTime: Int64? = nil
    @Published var workStateText: String = String()
    @Published var delayRankColor: Color = Color.clear
    @Published var lstFlex: [FlexboxLayoutModel] = []
    @Published var binDetail: BinDetail?
    
    func getData() {
        if let home = homeVm {
            if let it = home.currentUnfinishedWork {
                let w = it.0
                let array = it.1
                
                switch w.binType {
                case .New:
                    let name = Resources.work_add
                    setData(works: array,
                            item: nil,
                            placeNm1: name, placeNm2: nil, placeAddr: nil,
                            isWorkingStyle: false) { [self] work in
                        startNewWork(binHeader: w.bin as! BinHeader,
                                     work: work,
                                     placeNm1: name, placeNm2: nil, placeAddr: nil)
                    }

                    self.binDetail = nil
                case .Finished:
                    let binDetail = w.bin as! BinDetail
                    setData(works: array,
                            item: BinDetailForWork(binDetail: binDetail),
                            isWorkingStyle: false) { [self] work in
                        startWork(binDetail: binDetail, work: work)
                    }
                    
                    self.binDetail = binDetail
                case .Bin:
                    let binDetail = w.bin as! BinDetail
                    let isWorking = binDetail.getStatus() == .Working
                    setData(works: array,
                            item: BinDetailForWork(binDetail: binDetail),
                            isWorkingStyle: isWorking) { [self] work in
                        if isWorking {
                            homeVm?.endWork(binDetail: binDetail)
                        } else {
                            startWork(binDetail: binDetail, work: work)
                        }
                    }
                    
                    self.binDetail = binDetail
                case .none:
                    setData(works: [], item: nil, isWorkingStyle: false, callback: nil)
                }
            }
        }
    }
    
    private func setData(works: [Work],
                         item: BinDetailForWork?,
                         isWorkingStyle: Bool,
                         callback: ((Work) -> Void)?
    ) {
        setData(works: works,
                item: item,
                placeNm1: item?.detail.placeNm1, placeNm2: item?.detail.placeNm2, placeAddr: item?.detail.placeAddr,
                isWorkingStyle: isWorkingStyle,
                callback: callback)
    }
    
    private func setData(works: [Work],
                         item: BinDetailForWork?,
                         placeNm1: String?, placeNm2: String?, placeAddr: String?,
                         isWorkingStyle: Bool,
                         callback: ((Work) -> Void)?
    ) {
        let gray = Color(Resources.gray_out)
        
        if item != nil {
            if isWorkingStyle {
                workingGroup = true
                placeGroup = false
            } else {
                workingGroup = false
                placeGroup = true
            }
        }
        
        if let it = item?.detail.displayPlanTime() {
            timeRange = it
            timeGroup = true
        } else {
            timeGroup = false
        }
        
        if isWorkingStyle {
            place1Show = false
        } else {
            place1Show = true
        }
        
        nimachiShow = item != nil && item!.incidentalEnabled
        
        place1 = placeNm1.nullToEmpty()
        place2 = placeNm2.nullToEmpty()
        address = placeAddr.nullToEmpty()
        
        let color = isWorkingStyle ? Color(Resources.accentOrange) : Color(Resources.colorPrimary)
        
        clickInfoColor = item?.detail.hasNotice() == true ? Color.red : color
        
        // timer
        workStartTime = item?.workStartTime
        workStateText = item?.workStateText ?? String()
        delayRankColor = item?.delayRankColor ?? Color(UIColor(rgb: 0xff92d050))
        
        let needMatchCd =
                (item?.detail.getStatus() == .Moving || item?.detail.getStatus() == .Working)
                ? item?.detail.workCd
                : nil
        
        lstFlex = []
        for it in works {
            let matched = needMatchCd != nil && needMatchCd == it.workCd
            
            if matched {
                workStatus = Resources.work_status_s1_ing.replacingOccurrences(of: "%s", with: it.workNm)
            }
            
            let order = matched ? -1 : 0
            let text = isWorkingStyle && matched
                ? Resources.work_status_s1_end.replacingOccurrences(of: "%s", with: it.workNm)
                : it.workNm
            let isDisable = isWorkingStyle && !matched ? true : false
            let background = isWorkingStyle && !matched ? gray : color
            let cb = isWorkingStyle && !matched ? nil : callback
            lstFlex.append(FlexboxLayoutModel(
                order: order,
                text: text,
                isDisable: isDisable,
                background: background,
                matched: matched,
                work: it,
                callback: cb))
        }
    }
    
    private func startNewWork(binHeader: BinHeader,
                              work: Work,
                              placeNm1: String?, placeNm2: String?, placeAddr: String?
    ) {
        startWorkDialog(work: work) { [self] in
            homeVm?.startNewAddWork(binHeader: binHeader,
                                    work: work,
                                    placeNm1: placeNm1, placeNm2: placeNm2, placeAddr: placeAddr,
                                    location: current.lastLocation)
            getData()
        }
    }
    
    private func startWork(binDetail: BinDetail,
                           work: Work
    ) {
        startWorkDialog(work: work) { [self] in
            let location = current.lastLocation
            if location != nil && binDetail.checkIfMisDelivered(compare: location!) {
                homeVm?.showPopupView(
                    popupVm: PopupViewModel(isShowTitle: true,
                                            title: Resources.work_misdelivered_alert_title,
                                            message: Resources.work_misdelivered_alert_msg,
                                            isShowLeftBtn: true,
                                            leftBtnText: Resources.cancel,
                                            isShowRightBtn: true,
                                            rightBtnText: Resources.start,
                                            leftBtnClick: nil,
                                            rightBtnClick:
                                                { [self] in
                                                    homeVm?.startWork(binDetail: binDetail, work: work, location: location)
                                                    getData()
                                                }
                                           )
                )
            } else {
                homeVm?.startWork(binDetail: binDetail, work: work, location: location)
                getData()
            }
        }
    }
    
    private func startWorkDialog(work: Work, callBack: @escaping () -> ()) {
        homeVm?.showPopupView(popupVm: PopupViewModel(isShowTitle: true,
                                                      title: Resources.work_start_by_s1_alert_title.replacingOccurrences(of: "%s", with: work.workNm),
                                                      message: Resources.work_start_alert_msg,
                                                      isShowLeftBtn: true,
                                                      leftBtnText: Resources.cancel,
                                                      isShowRightBtn: true,
                                                      rightBtnText: Resources.start,
                                                      leftBtnClick: nil,
                                                      rightBtnClick: callBack))
    }
}
