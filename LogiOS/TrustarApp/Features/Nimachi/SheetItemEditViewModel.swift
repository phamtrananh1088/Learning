//
//  SheetItemEditViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/27.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

/**
 * 待機・附帯作業詳細・編集
 *
 * - 作業を選択[SheetWorkSelection]
 * - 荷主変更[ShipperChooseDialogFragment]
 * - 詳細[SheetItem]
 */
class SheetItemEditViewModel: ObservableObject {
    private let repo = Current.Shared.userRepository?.incidentalRepo
    
    //Using for display on view
    @Published var shipperName: String
    @Published var joinedWorkName: String
    
    var shipper: Shipper? {
        didSet {
            shipperName = shipper?.shipperNm1 ?? ""
        }
    }
    
    var works: [IncidentalWork?]? {
        didSet {
            let arrayWork = works?.compactMap({
                $0?.workNm ?? "???"
            })
            joinedWorkName = arrayWork?.joined(separator: "／") ?? ""
        }
    }
    @Published var nimachiTimes: [EditedTime]
    @Published var additionalTimes: [EditedTime]
    
    @Published var itemData: IncidentalItemData? {
        //load data from DB
        didSet {
            shipperName = itemData?.shipperNm ?? ""
            joinedWorkName = itemData?.joinedWorkName ?? ""
            works = itemData?.works
            
            nimachiTimes = itemData?.times?.nimachiTime().map({ it in
                return EditedTime(t: it)
            }) ?? []
            
            additionalTimes = itemData?.times?.additionalTime().map({ it in
                return EditedTime(t: it)
            }) ?? []
        }
    }
    
    //using for internal
    private var addedTimes: [EditedTime] = []
    private var deletedTimes: [EditedTime] = []
    
    //MARK: 新規登録押下 --> Add, 待機・附帯作業詳細・編集押下 --> Sheet
    private var target: EditTarget
    init(target: EditTarget) {
        self.target = target
        
        self.shipperName = ""
        self.joinedWorkName = ""
        
        self.nimachiTimes = []
        self.additionalTimes = []
        
        //load data
        editTarget()
    }
    
    //MARK: Initialize data
    func editTarget() {
        if target is EditTarget.Add {
            //create new sheet item
            self.itemData = IncidentalItemData(shipper: nil, works: nil, times: nil, editTarget: target)
            
        } else if target is EditTarget.Sheet{
            do {
                //edit sheet item
                let sheet = target as! EditTarget.Sheet
                let headerUUID = sheet.uuid
                let itemDataDb = try repo!.sheetDetailsByUUID(uuid: headerUUID)
                
                self.itemData = itemDataDb
            } catch let err {
                print(err)
            }
        }
    }
    
    // MARK: Common method for edit and add
    func deleteTime(time: EditedTime) {
        time.markDeleted = true
        
        //add to deletedTime
        deletedTimes.append(time)
    }
    
    func addTime(time: TimeItem) -> EditedTime {
        let addedTime = EditedTime(t: time, color: 0xEEFFEE) //0x1100FF00
        addedTime.justChanged = true
        
        //add to addedTime
        addedTimes.append(addedTime)
        
        return addedTime
    }
    
    func editTime(time: EditedTime, begin: Int64?, end: Int64?) {
        if let dt = begin {
            time.overrideBeginDate = DateItem(date: dt)
            time.begin = DateItem(date: dt)
        }
        
        if let dt2 = end {
            time.overrideEndDate = DateItem(date: dt2)
            time.end = DateItem(date: dt2)
        }
    }
    
    func setShipper(shipper: Shipper?) {
        self.shipper = shipper
    }
    
    func setWorks(works: [IncidentalWork]?) {
        self.works = works
    }
    
    //MARK: Save data into DB and back to list or view mode
    func save() -> IncidentalHeader? {
        if self.target is EditTarget.Add {
            //add new sheet item
            guard shipper != nil else { return nil }
            let editObj = target as! EditTarget.Add
            let binDetail = editObj.binDetail
            let timeList = nimachiTimes + additionalTimes
            
            guard let header = repo?.addIncidentalHeader(binDetail: binDetail,
                                                         shipper: shipper!,
                                                         workList: works ?? [])
            else { return nil }
            
            repo?.addTimeList(header: header, list: timeList)
            
            return header
        } else if self.target is EditTarget.Sheet {
            //Edit sheet item
            var saveList: [IncidentalTime] = []
            let itemDataDb = itemData as? IncidentalItemDataDB
            if let header = itemDataDb?.item.sheet {
                //Extract Edited times
                let timeList = nimachiTimes + additionalTimes + deletedTimes
                let editedTimes = timeList.filter({ it in
                    it.hasChanged() //&& it is TimeItemDB
                })
                
                editedTimes.forEach { et in
                    if let timeItemDb = et.t as? TimeItemDB {
                       let incidentalTime = timeItemDb.time
                        
                        if et.markDeleted {
                            //delete
                            incidentalTime.deleted = true
                        } else {
                            //change
                            if et.overrideBeginDate != nil {
                                incidentalTime.beginDatetime = et.overrideBeginDate?.date
                            }
                            if et.overrideEndDate != nil {
                                incidentalTime.endDatetime = et.overrideEndDate?.date
                            }
                        }
                        incidentalTime.recordChanged()
                        saveList.append(incidentalTime)
                    }
                }
                
                repo?.saveTimeList(list: saveList)
                repo?.addTimeList(header: header, list: addedTimes)
                
                if shipper != nil {
                    header.shipperCd = shipper!.shipperCd
                }
                
                if works != nil {
                   if let wlist = works?.compactMap({ it in
                        it?.workCd
                   }) {
                       header.workList = wlist.joined(separator: ",")
                   }
                }
                
                if header.sync == 0 {
                    repo?.saveIncidentalHeader(header: header)
                }
                
                //Call sync
                Current.Shared.syncIncidental(callBack: {(isOk, Error) in
                })
                
                return header
            }
        }
        
        return nil
    }
}
