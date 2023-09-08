//
//  WorkItemViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/17.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation
import UIKit
import SwiftUI
import Combine

class WorkItemViewModel: ObservableObject {
    @Published var deviation: String? = nil
    
    private var binDetailAndStatus: BinDetailAndStatus
    
    var detail: BinDetail
    var status: WorkStatus
    var inRange: Bool = false
    var nameAndTime: String = Resources.strEmpty
    var additionalRedText: String? = nil
    
    // View
    var bgColor: UIColor
    
    var target: String
    var address: String
    
    var workStatusText: String
    var workStatusColor: UIColor
    var workStatusBgColor: UIColor
    
    var workL: String {
        if detail.placeLatitude != nil && detail.placeLongitude != nil {
            return deviation ?? Resources.strEmpty
        } else {
            return Resources.location_0
        }
    }
    
    var workLShow: Bool {
        if detail.placeLatitude != nil && detail.placeLongitude != nil {
            return deviation != nil
        } else {
            return true
        }
    }
    
    var workLBgColor: UIColor {
        if detail.placeLatitude != nil && detail.placeLongitude != nil {
            return inRange ? UIColor.red : UIColor.gray
        } else {
            return UIColor.red
        }
    }
    
    var wrong: String
    var wrongShow: Bool
    
    var clickInfoColor: UIColor
    
    init(binDetailAndStatus: BinDetailAndStatus, location: CLLocation?) {
        self.binDetailAndStatus = binDetailAndStatus
        
        self.detail = binDetailAndStatus.detail
        self.status = binDetailAndStatus.status

        self.nameAndTime = "[\(detail.workNm!)] \(detail.displayPlanTime().nullToEmpty())"
        
        let deStatus = detail.delayStatus
        let misdelivered = detail.misdeliveryStatus != "0"
        if deStatus == 0 && !misdelivered {
            self.additionalRedText = nil
        } else {
            var notEmpty = false
            var delayed: String? = nil
            
            if deStatus == 1 {
                delayed = Resources.work_in_advance
            } else if (deStatus == 2) {
                delayed = Resources.work_delayed
            }
            
            if delayed != nil {
                self.additionalRedText = delayed
                notEmpty = true
            }
            
            if misdelivered {
                if self.additionalRedText == nil {
                    self.additionalRedText = String()
                }
                
                if notEmpty {
                    self.additionalRedText?.append(" ")
                }
                
                self.additionalRedText?.append(Resources.work_misdelivered)
            }
        }
        
        // bgColor
        bgColor = UIColor(rgbText: status.plateBackColor)
        // target
        self.target = detail.placeNameFull()
        // address
        self.address = detail.placeAddr.orEmpty()
        // workStatus
        workStatusText = status.workStatusNm
        workStatusColor = UIColor(rgbText: status.workFontColor)
        workStatusBgColor = UIColor(rgbText: status.workBackColor)
        
        // wrong
        if let value = additionalRedText {
            wrong = value
            wrongShow = true
        } else {
            wrong = Resources.strEmpty
            wrongShow = false
        }
        
        // clickInfoColor
        clickInfoColor = detail.hasNotice() ? UIColor.red : Resources.colorPrimary
        
        updateDeviation(location: location)
    }
    
    func updateDeviation(location: CLLocation?) {
        self.inRange = false
        
        if location != nil {
            if let p = detail.checkDeliveryDeviation(compare: location!) {
                if p.0 <= 1000 {
                    self.deviation = "\(p.0)m"
                } else {
                    self.deviation = "\(String(format: "%.1f", Float((p.0 / 100)) / Float(10)))km"
                }
                
                self.inRange = p.1
            }
        }
    }
    
    func sameItem(_ w: WorkItemViewModel) -> Bool {
        return detail.updatedDate == w.detail.updatedDate
        && deviation == w.deviation
    }
}
