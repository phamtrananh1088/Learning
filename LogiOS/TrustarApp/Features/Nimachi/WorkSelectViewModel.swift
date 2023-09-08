//
//  SheetWorkSelectVM.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/06.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
class WorkSelectViewModel: BaseViewModel, ObservableObject {
    
    @Published var workList: [WorkItem] = []
    
    //passed incidentalwork from SheetItemEditView
    private var incidentalWork: [IncidentalWork?]?
    private var saveCallback: ([IncidentalWork]?) -> Void
    
    init(incidentalWork: [IncidentalWork?]?, saveCallback: @escaping ([IncidentalWork]?) -> Void) {
        self.incidentalWork = incidentalWork
        self.saveCallback = saveCallback
        
        super.init()
    }
    
    override func registerNotificationCenter() {
        NotificationCenter.default.addObserver(self, selector: #selector(self.methodOfReceivedNotification(notification:)), name: Notification.Name(BroadcastEnum.Incidental.rawValue), object: nil)
    }
    
    @objc func methodOfReceivedNotification(notification: Notification) {

        switch notification.name.rawValue {
        case BroadcastEnum.Incidental.rawValue:
            loadIncidentalWorks()
        default:
            break
        }
    }
    
    func loadIncidentalWorks() {
        let incidentalWorks: [IncidentalWork]? = Current.Shared.userRepository?.incidentalRepo.workList()?.sorted(by: { l, r in
            return l.displayOrder < r.displayOrder
        })
        guard incidentalWorks != nil else { return }
        
        workList = incidentalWorks!.map({ it in
            WorkItem(work: it, selected: incidentalWork?.contains(where: { it2 in
                it2?.workCd == it.workCd
            }) ?? false)
        })
    }
    
    func saveIncidentalWorks() {
        /*let incidentalWorks = workList.compactMap { it in
            return it.work
        }*/
        let incidentalWorks = workList.filter { it in
            it.selected
        }.compactMap { it2 in
            return it2.work
        }
        saveCallback(incidentalWorks)
    }
}
