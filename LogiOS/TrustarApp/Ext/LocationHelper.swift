//
//  LocationHelper.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/23.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import CoreLocation
import UIKit

class LocationHelper : UIViewController {
    
    var onLocationCallBack: ((CLLocation?) -> ())! = nil
    var beforeTime: Int64 = 0
    var timeRefresh: Int = 1
    
    private lazy var locationManager: CLLocationManager = {
      let manager = CLLocationManager()
      manager.desiredAccuracy = kCLLocationAccuracyBest
      manager.delegate = self
      manager.requestAlwaysAuthorization()
      manager.allowsBackgroundLocationUpdates = true
      return manager
    }()
    
    public private(set) var connected = false

    @objc private func intervalRepeat() {
        if UIApplication.shared.applicationState == .active {
            if let callBack = onLocationCallBack {
                callBack(nil)
            }
        }
    }
    
    private var intervalRun: Timer? = nil
    func setLocationRequest(second: Int) {
        DispatchQueue.main.async { [self] in
            self.timeRefresh = second
            locationManager.startUpdatingLocation()
            switch (UIApplication.shared.applicationState) {
            case .active, .inactive:
                stop()
                guard intervalRun == nil else { return }
                intervalRun = Timer.scheduledTimer(timeInterval: TimeInterval(second), target: self, selector: #selector(self.intervalRepeat), userInfo: nil, repeats: true)
            default:
                break
            }
        }
    }
    
    func stop() {
        intervalRun?.invalidate()
        intervalRun = nil
    }
}

extension LocationHelper: CLLocationManagerDelegate {
  
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let _ = locations.last else {
          return
        }
        
        if UIApplication.shared.applicationState == .background {
            let curr = currentTimeMillis()
            if let callBack = onLocationCallBack {
                if curr - beforeTime >= timeRefresh * 1000 {
                    beforeTime = curr
                    if let loc = locations.first {
                        callBack(loc)
                    }
                }
            }
        }
    }
}

class LocationManager: NSObject, CLLocationManagerDelegate {

    static let shared = LocationManager()
    private var locationManager: CLLocationManager = CLLocationManager()
    private var requestLocationAuthorizationCallback: ((CLAuthorizationStatus) -> Void)?

    public func requestLocationAuthorization() {
        self.locationManager.delegate = self
        let currentStatus = CLLocationManager.authorizationStatus()

        // Only ask authorization if it was never asked before
        guard currentStatus == .notDetermined else { return }

        // Starting on iOS 13.4.0, to get .authorizedAlways permission, you need to
        // first ask for WhenInUse permission, then ask for Always permission to
        // get to a second system alert
        if #available(iOS 13.4, *) {
            self.requestLocationAuthorizationCallback = { status in
                if status == .authorizedWhenInUse {
                    self.locationManager.requestAlwaysAuthorization()
                }
            }
            self.locationManager.requestWhenInUseAuthorization()
        } else {
            self.locationManager.requestAlwaysAuthorization()
        }
    }
    // MARK: - CLLocationManagerDelegate
    public func locationManager(_ manager: CLLocationManager,
                                didChangeAuthorization status: CLAuthorizationStatus) {
        self.requestLocationAuthorizationCallback?(status)
    }
}
