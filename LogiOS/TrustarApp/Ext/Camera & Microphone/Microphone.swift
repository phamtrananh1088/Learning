//
//  Microphone.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/16.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import AVFoundation
import CoreImage
import UIKit

class Microphone: NSObject {
    private let captureSession = AVAudioSession()
    private var audioRecorder: AVAudioRecorder?
    private var sessionQueue: DispatchQueue!
    private let settings = [
        AVFormatIDKey: Int(kAudioFormatMPEG4AAC),
        AVSampleRateKey: 12000,
        AVNumberOfChannelsKey: 1,
        AVEncoderAudioQualityKey: AVAudioQuality.high.rawValue
    ]
    
    override init() {
        sessionQueue = DispatchQueue(label: "session queue microphone")
    }
    
    private var addToAudioStream: ((Bool, AVAudioRecorder) -> Void)?
    lazy var audioStream: AsyncStream<(Bool, AVAudioRecorder)> = {
        AsyncStream { continuation in
            addToAudioStream = { successFlg, audio in
                continuation.yield((successFlg, audio))
            }
        }
    }()
    
    func startRecording(url: URL) {
        sessionQueue.async {
            do {
                self.audioRecorder = try AVAudioRecorder(url: url, settings: self.settings)
                self.audioRecorder?.delegate = self
                self.audioRecorder?.record()
            } catch {
                debugPrint(error)
            }
        }
    }
    
    func stopRecording() {
        sessionQueue.async {
            self.audioRecorder?.stop()
        }
    }
    
    func checkAuthorization() async -> Bool {
        switch AVCaptureDevice.authorizationStatus(for: .audio) {
        case .authorized:
            debugPrint("Microphone access authorized.")
            return true
        case .notDetermined:
            debugPrint("Microphone access not determined.")
            sessionQueue.suspend()
            let status = await AVCaptureDevice.requestAccess(for: .audio)
            sessionQueue.resume()
            return status
        case .denied:
            debugPrint("Microphone access denied.")
            return false
        case .restricted:
            debugPrint("Microphone library access restricted.")
            return false
        @unknown default:
            return false
        }
    }
}

extension Microphone: AVAudioRecorderDelegate {
    func audioRecorderDidFinishRecording(_ recorder: AVAudioRecorder, successfully flag: Bool) {
        addToAudioStream?(flag, recorder)
    }
}
