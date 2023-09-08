//
//  SensorDetectEvent.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/02/21.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import GRDB
import CoreLocation

enum SensorDetectEventColumns: String, ColumnExpression, CaseIterable {
    case id = "id"
    case allocationNo = "allocation_no"
    
    case eventRecordLatitude = "event_latitude"
    case eventRecordLongitude = "event_longitude"
    case eventRecordAccuracy = "event_accuracy"
    case eventRecordDate = "event_date"
    
    case locationTimestamp = "location_timestamp"
    
    func name() -> String {
        return String(describing: self)
    }
}

class SensorDetectEvent: BaseEntity, Codable {
    var id                   : Int?    = nil
    var allocationNo         : String  = Resources.strEmpty
    
    var eventRecordLatitude  : Double? = nil
    var eventRecordLongitude : Double? = nil
    var eventRecordAccuracy  : Double? = nil
    var eventRecordDate      : Double   = 0
    
    var locationTimestamp    : Int64?  = nil
    
    init(id: Int?,
         allocationNo: String,
         eventRecordLatitude: Double?,
         eventRecordLongitude: Double?,
         eventRecordAccuracy: Double?,
         eventRecordDate: Double,
         locationTimestamp: Int64?
    ) {
        self.id = id
        self.allocationNo = allocationNo
        self.eventRecordLatitude = eventRecordLatitude
        self.eventRecordLongitude = eventRecordLongitude
        self.eventRecordAccuracy = eventRecordAccuracy
        self.eventRecordDate  = eventRecordDate
        self.locationTimestamp = locationTimestamp
        
        super.init()
    }
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"sensor_detect"}
    
    override class var primaryKey: String { SensorDetectEventColumns.id.rawValue }
    
    required init(row: Row) {
        id = row[SensorDetectEventColumns.id]
        allocationNo = row[SensorDetectEventColumns.allocationNo]
        
        eventRecordLatitude = row[SensorDetectEventColumns.eventRecordLatitude]
        eventRecordLongitude = row[SensorDetectEventColumns.eventRecordLongitude]
        eventRecordAccuracy = row[SensorDetectEventColumns.eventRecordAccuracy]
        eventRecordDate = row[SensorDetectEventColumns.eventRecordDate]
        
        locationTimestamp = row[SensorDetectEventColumns.locationTimestamp]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[SensorDetectEventColumns.id] = id
        container[SensorDetectEventColumns.allocationNo] = allocationNo
        container[SensorDetectEventColumns.eventRecordLatitude] = eventRecordLatitude
        container[SensorDetectEventColumns.eventRecordLongitude] = eventRecordLongitude
        container[SensorDetectEventColumns.eventRecordAccuracy] = eventRecordAccuracy
        container[SensorDetectEventColumns.eventRecordDate] = eventRecordDate
        container[SensorDetectEventColumns.locationTimestamp] = locationTimestamp
        
        super.encode(to: &container)
    }
    
    init(binHeader: BinHeader,
         location: CLLocation?,
         eventTime: Double
    ) {
        self.allocationNo = binHeader.allocationNo
        self.eventRecordLatitude = location?.coordinate.latitude
        self.eventRecordLongitude = location?.coordinate.longitude
        self.eventRecordAccuracy = location?.horizontalAccuracy
        self.eventRecordDate = eventTime
        self.locationTimestamp = location?.timestamp.milisecondsSince1970
        
        super.init()
    }
    
    func csvRow(formatter: ReuseDateFormatter) -> String {
        let dateFmt: String = formatter.format(date: Date(timeIntervalSince1970: TimeInterval(eventRecordDate)))
        let coordinate: String = "\(eventRecordLongitude ?? 0.0),\(eventRecordLatitude ?? 0.0),\(eventRecordAccuracy ?? 0)"
        return "\(dateFmt),\(coordinate)"
    }

    func copyOf(newLocation: CLLocation) -> SensorDetectEvent {
        return SensorDetectEvent(id: id,
                                 allocationNo: allocationNo,
                                 eventRecordLatitude: newLocation.coordinate.latitude,
                                 eventRecordLongitude: newLocation.coordinate.longitude,
                                 eventRecordAccuracy: newLocation.horizontalAccuracy,
                                 eventRecordDate: eventRecordDate,
                                 locationTimestamp: newLocation.timestamp.milisecondsSince1970)
    }
    
    func inRange(location: CLLocation, range: Int) -> Bool {
        let l = CLLocation(latitude: eventRecordLatitude ?? 0.0, longitude: eventRecordLongitude ?? 0.0)
        return location.distance(from: l) < Double(range)
    }
    
    func distanceTo(location: CLLocation) -> Double? {
        let l = CLLocation(latitude: eventRecordLatitude ?? 0.0, longitude: eventRecordLongitude ?? 0.0)
        return l.distance(from: location)
    }
}
