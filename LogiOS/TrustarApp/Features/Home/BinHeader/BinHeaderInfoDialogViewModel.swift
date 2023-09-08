//
//  BinHeaderInfoDialogViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 14/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class BinHeaderInfoDialogViewModel: ObservableObject {
    @Published public private(set) var allocationNo: String = String()
    
    public var vehicle: String         = Resources.strEmpty
    public var startScheduled: String  = Resources.strEmpty
    public var endScheduled: String    = Resources.strEmpty
    public var start: String           = Resources.strEmpty
    public var end: String             = Resources.strEmpty
    public var crew: String            = Resources.strEmpty
    public var crew2: String           = Resources.strEmpty
    public var note: String            = Resources.strEmpty

    public private(set) var inputEnable = false
    private var errorValue: Bool = false
    private var oldValueOutgoing: String = Resources.strEmpty
    @Published public var outgoing: String = Resources.strEmpty {
        willSet {
            let nvl = Int(newValue)
            if nvl == nil && newValue.count > 0 {
                errorValue = true
            }
            
            if newValue.count > 7 {
                errorValue = true
            }
        }
        
        didSet {
            if errorValue {
                errorValue = false
                outgoing = String()
            }
        }
    }
    
    private var oldValueIncoming: String = Resources.strEmpty
    @Published public var incoming: String = Resources.strEmpty {
        willSet {
            let nvl = Int(newValue)
            if nvl == nil && newValue.count > 0 {
                errorValue = true
            }
            
            if newValue.count > 7 {
                errorValue = true
            }
        }
        
        didSet {
            if errorValue {
                errorValue = false
                incoming = String()
            }
        }
    }

    public var outgoingMeterGroup: Bool = false
    public var incomingMeterGroup: Bool = false
    
    private var repo = Current.Shared.userRepository!.binHeaderRepo
    var binHeaderLiveData: BinHeaderAndStatus? {
        repo.selectBinHeaderWithStatus(allocationNo: allocationNo)
    }
    
    func onNext(allocationNo: String) {
        self.allocationNo = allocationNo
        applyBinHeaderData(binHeaderAndStatus: binHeaderLiveData)
    }
    
    func applyBinHeaderData(binHeaderAndStatus: BinHeaderAndStatus?) {
        let header = binHeaderAndStatus?.header
        let status = binHeaderAndStatus?.status
        let started = status?.getStatus().started()
        let finished = status?.getStatus() == .Finished
        
        vehicle = (binHeaderAndStatus?.truck.truckNm).orEmpty()
        
        if let it = header?.startDatetime {
            startScheduled = Config.Shared.dateFormatter3.format(date: it)
        }
        
        if let it = header?.endDatetime {
            endScheduled = Config.Shared.dateFormatter3.format(date: it)
        }
        
        if let it = header?.startDate {
            start = Config.Shared.dateFormatter3.format(date: it)
        }
        
        if let it = header?.endDate {
            end = Config.Shared.dateFormatter3.format(date: it)
        }
        
        crew = header?.driverNm ?? String()
        crew2 = header?.subDriverNm ?? String()
        note = header?.allocationNote1 ?? String()
        
        inputEnable = Current.Shared.loggedUser!.userInfo.meterInputEnable()
        outgoing = header?.outgoingMeter.toString() ?? String()
        oldValueOutgoing = outgoing
        incoming = header?.incomingMeter.toString() ?? String()
        oldValueIncoming = incoming
        outgoingMeterGroup = started ?? false
        incomingMeterGroup = finished
    }
    
    func setOutgoing() {
        if oldValueOutgoing != outgoing {
            repo.setOutgoingMeter(binHeader: binHeaderLiveData!.header, kilometer: Int(outgoing))
            Current.Shared.syncBin(callBack: { [self](isOk, Error) in
                onNext(allocationNo: allocationNo)
            })
        }
    }
    
    func setIncoming() {
        if oldValueIncoming != incoming {
            repo.setIncomingMeter(binHeader: binHeaderLiveData!.header, kilometer: Int(incoming))
            Current.Shared.syncBin(callBack: { [self](isOk, Error) in
                onNext(allocationNo: allocationNo)
            })
        }
    }
}
