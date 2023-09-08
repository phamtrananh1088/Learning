//
//  WorkItem.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//
import Foundation

class WorkItem: Identifiable, Equatable, ObservableObject {
    public let id: UUID = UUID()
    
    static func == (lhs: WorkItem, rhs: WorkItem) -> Bool {
        return lhs.selected == rhs.selected &&
               lhs.work == rhs.work
    }
    
    @Published var work: IncidentalWork
    @Published var selected: Bool
    
    init(work: IncidentalWork, selected: Bool) {
        self.work = work
        self.selected = selected
    }
    
    var workNm: String? {
        return work.workNm
    }
    
    func toggle() {
        selected.toggle()
    }
}
