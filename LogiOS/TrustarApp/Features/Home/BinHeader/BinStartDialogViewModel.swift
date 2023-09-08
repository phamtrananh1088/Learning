//
//  BinStartDialogViewModel.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import Combine

class BinStartDialogViewModel: ObservableObject {
    var doneClick: (String, Truck?, String?) -> ()
    var selected: Truck?
    var outgoingMeter: String?
    var header: BinHeader?
    var token: String = Resources.strEmpty
    @Published var sus: StartUnscheduledBinState? = nil
    private var repo = Current.Shared.userRepository?.binHeaderRepo
    private var storage = [AnyCancellable]()
    
    init() {
        //token, selectedTruck, outgoingMeter
        doneClick = {(tk, t, s) in}
    }
    
    init(doneClick: @escaping (String, Truck?, String?) -> ()) {
        self.doneClick = doneClick
        //gettoken to start unscheduled
        fetchStartUnscheduledToken()
    }
    
    init(bin: BinHeaderAndStatus?, doneClick: @escaping (String, Truck?, String?) -> ()) {
        self.doneClick = doneClick
        self.header = bin?.header
        
        if bin == nil || bin!.status.getStatus() != .Ready {
            doneClick(token, nil, nil)
        } else {
            selected = bin!.truck
            outgoingMeter = bin!.header.outgoingMeter.toString()
        }
    }
    
    func setTruck() {
        Current.Shared.userRepository?.binHeaderRepo.setTruck(binHeader: header!, truck: selected!)
        Current.Shared.syncBin(callBack: {(isOk, Error) in })
    }
    
    //
    private func tokenWhenNotWorking() -> AnyPublisher<StartUnscheduledBinState, NetworkError> {
        let userInfo = Current.Shared.loggedUser!
        
        return Config.Shared.fetch.getToken(loginInfo: userInfo)
            .receive(on: DispatchQueue.main)
            .flatMap({(it) -> AnyPublisher<StartUnscheduledBinState, NetworkError> in
                if self.repo?.anyBinHeader(status: .Working) == nil {
                    return Just(StartUnscheduledBinState.Ready(token: it))
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                } else {
                    return Just(StartUnscheduledBinState.WorkingBinExists())
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                }
            })
            .eraseToAnyPublisher()
    }
    
    func fetchStartUnscheduledToken() {
        tokenWhenNotWorking()
        .sink(receiveCompletion: { err in
            switch(err) {
                case .finished: break
                case .failure(let error):
                    self.sus = StartUnscheduledBinState.GetTokenError(err: error)
            }
            }, receiveValue: { res in
                self.sus = res
                if res is StartUnscheduledBinState.Ready {
                    let resData = res as! StartUnscheduledBinState.Ready
                    self.token = resData.token
                }
            }
        ).store(in: &storage)
    }
}
