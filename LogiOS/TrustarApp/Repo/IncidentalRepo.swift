//
//  IncidentalRepo.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation

class IncidentalRepo {
    private var userDb: UserDb
    private var imageDb: ImageDb
    private var user: LoggerUser
    public var imageStore: CachedPicStore
    
    init(_ userDb: UserDb, _ imageDb: ImageDb,_ user: LoggerUser) {
        self.userDb = userDb
        self.imageDb = imageDb
        self.user = user
        self.imageStore = user.imageStore
    }
    
    private var headerDao: IncidentalHeaderDao? { return userDb.incidentalHeaderDao }
    private var workDao: IncidentalWorkDao? { return userDb.incidentalWorkDao }
    private var timeDao: IncidentalTimeDao? { return userDb.incidentalTimeDao }

    func sortedSheetList(allocationNo: String, allocationRowNo: Int) -> [IncidentalListItem] {
        guard headerDao != nil else { return [] }
        do {
            return try headerDao!.selectSheetList(allocationNo: allocationNo, allocationRowNo: allocationRowNo)!
                    .sorted(by: { (l, r) in
                        return (l.sheet.sheetNo ?? "") < (r.sheet.sheetNo ?? "")
                    })
        } catch {
            return []
        }
    }

    func sheetDetailsByUUID(uuid: String) throws -> IncidentalItemDataDB {
        let item = try headerDao?.findByUUID(uuid: uuid)
        let ls = item!.sheet
        let works = ls.workList
        let incidentalTimes = try timeDao?.findByHeaderUUID(headerUUID: ls.uuid)
        let incidentalWorks = workList()?.filter({ it in
                works.contains(it.workCd)
            })

        return IncidentalItemDataDB(item: item!, works: incidentalWorks, times: incidentalTimes!)
    }

    func workList() -> [IncidentalWork]? {
        return try? workDao?.selectAll()
    }
    
    func saveIncidentalHeader(header: IncidentalHeader) {
        headerDao?.insertOrReplace(header: header)
        if (header.deleted) {
            if let picId = header.picId {
                imageDb.imageSendingDao?.deleteByPicId(picId: picId)
            }
        }
    }

    func saveIncidentalHeaderWithPic(header: IncidentalHeader, pic: Data?) -> IncidentalHeader {
        
        let img = ImageSending(userInfo: user.userInfo,
                               picRaw: pic ?? Data.init(),
                               content: PicPost.Content.IncidentalSign(header: header))
        
        if header.picId != nil {
            imageDb.imageSendingDao?.deleteByPicId(picId: header.picId!)
        }
        
        if pic != nil {
            imageDb.imageSendingDao?.insert(imgSending: img)
        }
        
        if pic != nil {
            header.picId = img.picId
        } else {
            header.picId = nil
        }
        
        header.setSignStatus(isSigned: pic != nil)
        
        saveIncidentalHeader(header: header)
        return header
    }

    func saveTimeList(list: [IncidentalTime]) {
        timeDao?.insertOrReplace(list: list)
    }

    func addIncidentalHeader(
            binDetail: BinDetail,
            shipper: Shipper,
            workList: [IncidentalWork?]
    ) -> IncidentalHeader {
        let header = IncidentalHeader(uuid: UUID().uuidString,
                                      sheetNo: nil,
                                      allocationNo: binDetail.allocationNo,
                                      allocationRowNo: binDetail.allocationRowNo,
                                      shipperCd: shipper.shipperCd,
                                      workList: workList.compactMap({ it in
                                                    return it?.workCd
                                                }),
                                      status: 0,
                                      picId: nil)
        header.recordChanged()
        saveIncidentalHeader(header: header)
        return header
    }

    func addTimeList(header: IncidentalHeader, list: [EditedTime]) {
        
        let added:[IncidentalTime] = list.map { it in
            let iT = IncidentalTime(uuid: UUID().uuidString,
                                  headerUUID: header.uuid,
                                  sheetNo: header.sheetNo,
                                  sheetRowNo: nil,
                                  type: it.type,
                                  beginDatetime: it.targetBeginDate()?.date,
                                  endDatetime: it.targetEndDate()?.date)
            iT.recordChanged()
            return iT
        }

        if (!added.isEmpty) {
            saveTimeList(list: added)
        }
    }
}
