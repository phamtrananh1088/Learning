//
//  RefuelViewModel.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 13/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

/**
 * 給油
 */
class RefuelViewModel: ObservableObject {
    @Published var selectedBin: BinHeaderAndStatus? {
        didSet {
            currentAmount = self.totalAmountOfCompany(bin: selectedBin!.header) ?? 0
        }
    }
    @Published var selectedFuel: Fuel? {
        didSet {
            if selectedBin != nil {
                currentAmount = self.totalAmountOfCompany(bin: selectedBin!.header) ?? 0
            } else {
                currentAmount = 0
            }
        }
    }
    
    @Published var currentAmount: Double
    @Published var refuelAmount: Double?
    @Published var paymentAmount: Double?
    
    init() {
        currentAmount = 0
    }
    
    func totalAmountOfCompany(bin: BinHeader) -> Double? {
        if selectedBin?.header.allocationNo == nil || selectedFuel?.fuelCd == nil {
            return nil
        } else {
            return Current.Shared.userRepository?.kyuyuRepo.totalAmountOfCompany(allocationNo: selectedBin!.header.allocationNo, fuelClass: selectedFuel!.fuelCd )
        }
    }

    //MARK: 給油入力・入力を確定する押下時に起動されるイベント
    func kakutei(refuelAmount: String, paymentAmount: String) {
        self.refuelAmount = Double(refuelAmount)
        self.paymentAmount = Double(paymentAmount)
        
        let  refueled = Refueled(binHeader: selectedBin!.header,
                                   fuel: selectedFuel!,
                                   quantity: self.refuelAmount!,
                                   paid: self.paymentAmount!)
        self.refuel(refueled: refueled)
        self.refuelAmount = nil
        self.paymentAmount = nil
        //notify update current amount
        self.selectedBin = self.selectedBin
    }
    
    //MARK: 給油入力・データの更新処理
    func refuel(refueled: Refueled) {
        Current.Shared.userRepository?.kyuyuRepo.refuel(refueled: refueled)
        
        Current.Shared.syncFuel(callBack: { (isOk, Error) in
            
        })
        
    }
    
    //Data for bottom picker
    func fuelList() -> [Fuel]? {
        return Current.Shared.userRepository?.fuelRepo.fuelList()
    }

    func binHeaderList() -> [BinHeadUIItem]? {
        let rs = Current.Shared.userRepository?.binHeaderRepo.list()
        return rs?.map({ it in
            BinHeadUIItem(bin: it)
        })
    }
    
    //Call back from bottom picker
    func saveCallbackSelectedBin(bin: BinHeadUIItem?) -> Void {
        self.selectedBin = bin?.binHead
    }
    
    func saveCallbackSelectedFuel (fuel: Fuel?) -> Void {
        self.selectedFuel = fuel
    }
}
