//
//  DetectRestEvent.swift
//  TrustarApp
//
//  Created by DionSoftware on 26/03/2023.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation
import Combine

class DetectRestEvent {
    class MoveDetected: DetectRestEvent {
        var rest: RestDetected
        var endRealtimeNaos: Int64  //miliseconds
        
        init(rest: RestDetected, endRealtimeNaos: Int64) {
            self.rest = rest
            self.endRealtimeNaos = endRealtimeNaos
        }
        
        func elapsedRestMillis() -> Int64 {
            return (endRealtimeNaos - rest.startRestLocation.timestamp.milisecondsSince1970)
        }
        
        func restEndTimeBasedOsDate() -> Int64 {
            return dateBasedOnCurrentDate(realtimeNanos: self.endRealtimeNaos)
         }
    }
    
    class RestDetected: DetectRestEvent {
        var startRestLocation: CLLocation
        var speed: Double
        
        init(startRestLocation: CLLocation, speed: Double) {
            self.startRestLocation = startRestLocation
            self.speed = speed
        }
    }
    
    class Reset: DetectRestEvent {
        var elapsedMillis: Int64? = nil
        
        init (elapsedMillis: Int64?) {
            self.elapsedMillis = elapsedMillis
        }
    }
    
    init() {
        
    }
}

extension DetectRestEvent {
    func detectAfter(
        locationFlow: AnyPublisher<CLLocation, Never>,
        restDistance: Int,
        restSpeedMpMs: Double,
        restTimeInMillis: Int64,
        speedDetectSample: Int64
    ) -> AnyPublisher<DetectRestEvent, Never> {
        let behaviorProcessor = CurrentValueSubject<Optional<DetectRestEvent>, Never>(self)

        return behaviorProcessor
            .map({ e -> AnyPublisher<DetectRestEvent, Never> in
                if e is DetectRestEvent.RestDetected {
                    return locationFlow
                        .withPrevious()
                        .map({ (item) -> AnyPublisher<Optional<DetectRestEvent>, Never> in
                            let secondToLast = item.previous
                            let last = item.current
                            let eRD = e as! DetectRestEvent.RestDetected
                            if secondToLast != nil && last.distance(from: eRD.startRestLocation) > Double(restDistance) {
                                let t = last.elapsedMillis(eRD.startRestLocation)
                                if (t > restTimeInMillis) {
                                    
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.1) {
                                        behaviorProcessor.send(MoveDetected(rest: eRD, endRealtimeNaos: secondToLast!.timestamp.milisecondsSince1970))
                                    }
                                    
                                    return Just(MoveDetected(rest: eRD, endRealtimeNaos: secondToLast!.timestamp.milisecondsSince1970)).eraseToAnyPublisher()
                                } else {
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.1) {
                                        behaviorProcessor.send(Reset(elapsedMillis: t))
                                    }
                                    return Just(Reset(elapsedMillis: t)).eraseToAnyPublisher()
                                }
                            }
                            else {
                                return Just(nil).eraseToAnyPublisher()
                            }
                        })
                        .switchToLatest()
                        .compactMap({ $0 })
                        .eraseToAnyPublisher()
                } else {
                    return locationFlow
                    .locationSample(speedDetectSample: speedDetectSample)
                    .map( { (lastDropped, list)  -> AnyPublisher<Optional<DetectRestEvent>, Never> in
                            let last = list.last
                            if (lastDropped != nil && last != nil) {
                                let t = last!.elapsedMillis(lastDropped!)
                                let distance = last!.distance(from: lastDropped!)
                                let speed = Double(distance) / Double(t)
                                if (speed < restSpeedMpMs) {
                                    let secondToLast = list.secondToLast() ?? lastDropped
                                    
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 0.1) {
                                        behaviorProcessor.send(RestDetected(startRestLocation: secondToLast!, speed: speed))
                                    }
                                    
                                    return Just(RestDetected(startRestLocation: secondToLast!, speed: speed)).eraseToAnyPublisher()
                                } else {
                                    return Just(nil).eraseToAnyPublisher()
                                }
                            } else {
                                return Just(nil).eraseToAnyPublisher()
                            }
                        }
                    )
                    .switchToLatest()
                    .compactMap({ $0 })
                    .eraseToAnyPublisher()
                }
            })
            .switchToLatest()
            .eraseToAnyPublisher()
    }
}

extension AnyPublisher<CLLocation, Never> {
    func locationSample(speedDetectSample: Int64) -> AnyPublisher<(CLLocation?, [CLLocation]), Never> {
        return keepHistoryUntil { current, item in
            current.elapsedMillis(item) >= speedDetectSample
        }.map { (drop, list) in
            return (drop.last, list)
        }.scan((nil,[])) { pre, cur in
            let lastDropped = cur.0 ?? pre.0
            return (lastDropped, cur.1)
        }
        .eraseToAnyPublisher()
    }
}
