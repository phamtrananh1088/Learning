//
//  Weather.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/19.
//

import Foundation
import GRDB

enum WeatherColumns: String, ColumnExpression {
    case weatherCd, weatherNm, displayOrder
}

// 天候
class Weather: BaseEntity, Codable {
    var weatherCd:      String = Resources.strEmpty
    var weatherNm:      String = Resources.strEmpty
    var displayOrder:   Int = Resources.zeroNumber
    
    override init() {
        super.init()
    }
    
    override class var databaseTableName: String {"Weathers"}
    
    required init(row: Row) {
        weatherCd = row[WeatherColumns.weatherCd]
        weatherNm = row[WeatherColumns.weatherNm]
        displayOrder = row[WeatherColumns.displayOrder]
        
        super.init(row: row)
    }
    
    override func encode(to container: inout PersistenceContainer) {
        container[WeatherColumns.weatherCd] = weatherCd
        container[WeatherColumns.weatherNm] = weatherNm
        container[WeatherColumns.displayOrder] = displayOrder
        
        super.encode(to: &container)
    }
}
