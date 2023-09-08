//
//  PhotoLibrary.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/15.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Photos

class PhotoLibrary {

    static func checkAuthorization(_ onCallBack: @escaping (Bool) -> Void) {
        let authorStatus = PHPhotoLibrary.authorizationStatus()
        
        switch authorStatus {
        case .authorized:
            debugPrint("Photo library access authorized.")
            onCallBack(true)
        case .notDetermined:
            debugPrint("Photo library access not determined.")
            PHPhotoLibrary.requestAuthorization({ auth in
                onCallBack(auth == .authorized)
            })
        case .denied:
            debugPrint("Photo library access denied.")
            onCallBack(false)
        case .limited:
            debugPrint("Photo library access limited.")
            onCallBack(false)
        case .restricted:
            debugPrint("Photo library access restricted.")
            onCallBack(false)
        @unknown default:
            onCallBack(true)
        }
    }
    
    static func checkAuthorizationCamera() async -> Bool {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            debugPrint("Camera access authorized.")
            return true
        case .notDetermined:
            debugPrint("Camera access not determined.")
            //sessionQueue.suspend()
            let status = await AVCaptureDevice.requestAccess(for: .video)
            //sessionQueue.resume()
            return status
        case .denied:
            debugPrint("Camera access denied.")
            return false
        case .restricted:
            debugPrint("Camera library access restricted.")
            return false
        @unknown default:
            return false
        }
    }
}

