//
//  Point3.swift
//  TrustarApp
//
//  Created by DionSoftware on 27/03/2023.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation

class Point3<T> {
    var x: Double
    var y: Double
    var z: Double
    var obj: T
    
    init(x: Double, y: Double, z: Double, obj: T) {
        self.x = x
        self.y = y
        self.z = z
        self.obj = obj
    }
}


extension CLLocation {
    func toPoint3<T>(obj: T) -> Point3<T> {
        let lat = self.coordinate.latitude * Double.pi / 180.0
        let lon = self.coordinate.longitude * Double.pi / 180.0
        let cla = cos(lat)
        let clo = cos(lon)
        let sla = sin(lat)
        let slo = sin(lon)
        let r = 6378137.0
        let a = r * cla
        return Point3(
            x: a * clo,
            y: a * slo,
            z: r * sla,
            obj: obj
        )
    }
}


//
//fun Collection<Point3<*>>.miniBall(): Miniball {
//    val points = ArrayPointSet(3, size)
//    forEachIndexed { i, p ->
//        points.set(i, 0, p.x)
//        points.set(i, 1, p.y)
//        points.set(i, 2, p.z)
//    }
//    return Miniball(points)
//}
