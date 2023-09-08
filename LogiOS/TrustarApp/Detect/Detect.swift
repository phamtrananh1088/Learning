//
//  Detect.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/03/23.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import CoreMotion
import CoreLocation

func restEvent(
    cancelableBags: inout Set<AnyCancellable>,
    working: AnyPublisher<[BinHeader], Never>,
    location: AnyPublisher<CLLocation, Never>,
    user: UserInfo
) -> AnyPublisher<(String, DetectRestEvent), Never> {
    let restDistanceMeter = user.restDistance
    let restTimeInMillis: Int64 = Int64(user.restTime * 60_000) //miniute
    
    return working.eachWorkingBin(
        cancelableBags: &cancelableBags,
        internalCancelEmit: { _, p in
            let r = p?.1
            if (r is DetectRestEvent.RestDetected) {
                //let now = ProcessInfo.processInfo.systemUptime * 1000 //miliseconds
                let now = Date().milisecondsSince1970
                let rDetected = r as! DetectRestEvent.RestDetected
                let d = DetectRestEvent.MoveDetected(rest: rDetected, endRealtimeNaos: Int64(now))
                let over = d.elapsedRestMillis() > restTimeInMillis
                if (over) {
                    return (p!.0, d)
                } else {
                    return nil
                }
            } else {
                return nil
            }
        },
        flowable: { b in
            //let allocationNo = b.allocationNo
            DetectRestEvent.Reset(elapsedMillis: nil)
                .detectAfter(locationFlow: location,
                             restDistance: restDistanceMeter,
                             restSpeedMpMs: Double(restDistanceMeter) / 15_000,
                             restTimeInMillis: restTimeInMillis,
                             speedDetectSample: 10_000)
                .map { restEvent in
                    return (b.allocationNo, restEvent)
                }.eraseToAnyPublisher()
        }).map({ $0.1 }).eraseToAnyPublisher()
}

func sensorDetect(
    cancelableBags: inout Set<AnyCancellable>,
    working: AnyPublisher<[BinHeader], Never>,
    binHeaderDelayInMillis: Int64,
    workDelayInMillis: Int64,
    sensorManager : CMMotionManager,
    detectControl: @escaping (String) -> AnyPublisher<Bool, Never>
) -> AnyPublisher<SensorRecord, Never> {
    
    let sensorEvent = sensorManager
        .rxAccelerometerDetectionMillis(sampling: 100, size: 10, limit: 10_000, threshold: 4.3)
        .subscribe(on: DispatchQueue.global())
        .share()
    
    return working.eachWorkingBin(
        cancelableBags: &cancelableBags,
        internalCancelEmit: { b, _ in
            let now = Date().milisecondsSince1970
            let from = now - binHeaderDelayInMillis
            return SensorRecord.DropRecent(allocationNo: b.allocationNo, from: from, to: now, isFinished: true)
        },
        flowable: { b in
            detectControl(b.allocationNo)
                .removeDuplicates()
                .map({ it -> AnyPublisher<SensorRecord, Never> in
                    if it {
                        return sensorEvent.map({ eventDate -> SensorRecord in
                            return SensorRecord.Record(event: SensorDetectEvent(binHeader: b, location: Current.Shared.lastLocation, eventTime: eventDate))
                        })
                        .delaySubscription(for: .milliseconds(Int(workDelayInMillis)), scheduler: RunLoop.main)
                        .eraseToAnyPublisher()
                    } else {
                        let now = Date().milisecondsSince1970
                        let from = now - workDelayInMillis
                        return Just(SensorRecord.DropRecent(allocationNo: b.allocationNo, from: from, to: now, isFinished: false))
                            .eraseToAnyPublisher()
                    }
                })
                .switchToLatest()
                .delaySubscription(for: .milliseconds(Int(binHeaderDelayInMillis - max(0, -1*(b.startDate ?? 0)))), scheduler: RunLoop.main)
                .eraseToAnyPublisher()
        }).map({ $0.1 }).eraseToAnyPublisher()
}

extension AnyPublisher<[BinHeader], Never> {
    func eachWorkingBin<T: Any>(
        cancelableBags: inout Set<AnyCancellable>,
        internalCancelEmit: ((_ bin: BinHeader, _ lastElement: T?) -> T?)? = nil,
        flowable: @escaping (_ bin: BinHeader) -> AnyPublisher<T, Never>
    ) -> AnyPublisher<(BinHeader, T), Never> {
        let emitter = PassthroughSubject<(BinHeader, T), Never>()
        var onCancelEmit = internalCancelEmit
        let mapDisposable: MapDisposable<String> = MapDisposable()

        let d = self
        .receive(on: RunLoop.main)
        .sink(receiveValue: { ls in
            var m: [String : Cancellable] = [:]
            ls.filter({ $0.binStatus == "1"}).forEach({ b in
                let k = b.allocationNo
                let d = mapDisposable.take(key: k)
                
                var x: Cancellable? = nil
                if (d == nil || mapDisposable.isDisposed()) {
                    let consumer = { it in
                        emitter.send((b, it))
                    }
                    
                    x = flowable(b).withLastElementOnCancel(onCancel: { it in
                        if let rs = onCancelEmit?(b, it) {
                            consumer(rs)
                        }
                    }).sink(receiveValue: consumer)
                } else {
                    x = d
                }
                
                if let temp = m[k] {
                    temp.cancel()
                }
                m[k] = x
            })
            
            mapDisposable.clear()
            m.forEach({ (key, value) in
                _ = mapDisposable.put(key: key, d: value)
            })
        })
        
        cancelableBags.insert(d)
        
        emitter.handleEvents(receiveCancel:
                                 {
                                    onCancelEmit = nil
                                    d.cancel()
                                    mapDisposable.cancel()
                                }
                             )
        return emitter
            .eraseToAnyPublisher()
    }
}
