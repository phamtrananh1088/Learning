//
//  SensorRecorder.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/02/22.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import Foundation
import Combine
import CoreMotion

class SensorRecorder {
    var sensorManager: CMMotionManager
    var listenerCallback: (Double, Double, Double, Double) -> Void
    private var sendToSubjectFlg: Bool = true
    
    init(sensorManager: CMMotionManager,
         listenerCallback: @escaping (Double, Double, Double, Double) -> Void) {
        self.sensorManager = sensorManager
        self.listenerCallback = listenerCallback
    }
    
    let queue = OperationQueue()
    /// https://developer.apple.com/documentation/coremotion/getting_raw_accelerometer_events
    let G_9_8: Double = 9.8
    
    func stop() {
        self.sensorManager.stopDeviceMotionUpdates()
    }
    
    func start() {
        self.sensorManager.startDeviceMotionUpdates(to: self.queue) { (data: CMDeviceMotion?, error: Error?) in
            guard let data = data else {
                //print("Error: \(error!)")
                return
            }
            
            if(self.sendToSubjectFlg) {
                DispatchQueue.main.async { [self] in
                    let X = data.gravity.x * G_9_8
                    let Y = data.gravity.y * G_9_8
                    let Z = data.gravity.z * G_9_8
                
                    listenerCallback(X, Y, Z, data.timestamp)
                }
            }
        }
    }
    
    func setSendToSubjectFlg(allowFlag: Bool) {
        self.sendToSubjectFlg = allowFlag
    }
}

extension CMMotionManager {
    func rxAccelerometerDetection(sampling: Int, // MILLISECONDS
                                  size: Int,
                                  limit: Int,   // MILLISECONDS
                                  threshold: Double) -> AnyPublisher<EventXYZT, Never> {
        let subject = PassthroughSubject<EventXYZT, Never>()
        let s = SensorRecorder(sensorManager: self) { x, y, z, t in
            subject.send(EventXYZT(x: x, y: y, z: z, t: t))
            //debugPrint("x: \(x), y: \(y), z: \(z)")
        }
        
        s.start()
        
        return subject
            .filter({ $0.t > 0})
            .collect(.byTime(RunLoop.main, .milliseconds(sampling)))
            .flatMap({ lst -> AnyPublisher<EventXYZT, Never> in
                return Just(
                    lst.reduce(EventXYZT(x: 0, y: 0, z: 0, t: 0)) { (t1, t2) in
                    let ax = (t1.x + t2.x) / 2
                    let ay = (t1.y + t2.y) / 2
                    let az = (t1.z + t2.z) / 2
                    let at = (t1.t + t2.t) / 2
                    return EventXYZT(x: ax, y: ay, z: az, t: at)
                }).eraseToAnyPublisher()
            })
            .collect(size)
            .filter({ l in
                let xMax = l.max(by: { $0.x <= $1.x })!.x
                let xMin = l.min(by: { $0.x <= $1.x })!.x
                let yMax = l.max(by: { $0.y <= $1.y })!.y
                let yMin = l.min(by: { $0.y <= $1.y })!.y
                let zMax = l.max(by: { $0.z <= $1.z })!.z
                let zMin = l.min(by: { $0.z <= $1.z })!.z

                return xMax - xMin > threshold && yMax - yMin > threshold && zMax - zMin > threshold
            })
            .flatMap({ lst -> AnyPublisher<EventXYZT?, Never> in
                let data = lst.first
                //doesn't send data to subject
                s.setSendToSubjectFlg(allowFlag: false)
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + .milliseconds(limit)) {
                    s.setSendToSubjectFlg(allowFlag: true)
                }
                return Just(data).eraseToAnyPublisher()
            })
            .compactMap({ $0})
            .eraseToAnyPublisher()
    }
    
    func rxAccelerometerDetectionMillis(sampling: Int, // MILLISECONDS
                                        size: Int,
                                        limit: Int,   // MILLISECONDS
                                        threshold: Double) -> AnyPublisher<Double, Never> {
        return rxAccelerometerDetection(sampling: sampling, size: size, limit: limit, threshold: threshold)
            .map { e -> AnyPublisher<Double, Never> in
                let passed = (ProcessInfo.processInfo.systemUptime - e.t) // / 1_000_000
                return Just(NSDate().timeIntervalSince1970 - passed).eraseToAnyPublisher()
            }
            .switchToLatest()
            .share()
            .eraseToAnyPublisher()
    }
}
