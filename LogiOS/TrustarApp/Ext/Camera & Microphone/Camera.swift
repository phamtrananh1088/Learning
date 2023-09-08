//
//  Camera.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/15.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import AVFoundation
import CoreImage
import UIKit
import SwiftUI

final class CameraViewController : UIViewController,
                                   AVCaptureVideoDataOutputSampleBufferDelegate,
                                   AVCapturePhotoCaptureDelegate,
                                   AVCaptureFileOutputRecordingDelegate {
    
    let captureSession = AVCaptureSession()
    private var isCaptureSessionConfigured = false
    private var deviceInput: AVCaptureDeviceInput?
    private var photoOutput: AVCapturePhotoOutput?
    private var videoOutput: AVCaptureVideoDataOutput?
    private var videoFileOutput: AVCaptureMovieFileOutput?
    private var sessionQueue: DispatchQueue!
    var previewLayer: AVCaptureVideoPreviewLayer!
    
    private var addToPhotoStream: ((AVCapturePhoto) -> Void)?
    
    private var addToMovieStream: ((URL) -> Void)?
    
    private var addToPreviewStream: ((CIImage) -> Void)?
    
    var isPreviewPaused = false
    
    lazy var previewStream: AsyncStream<CIImage> = {
        AsyncStream { continuation in
            addToPreviewStream = { ciImage in
                if !self.isPreviewPaused {
                    continuation.yield(ciImage)
                }
            }
        }
    }()
    
    lazy var photoStream: AsyncStream<AVCapturePhoto> = {
        AsyncStream { continuation in
            addToPhotoStream = { photo in
                continuation.yield(photo)
            }
        }
    }()
    
    lazy var movieStream: AsyncStream<URL> = {
        AsyncStream { continuation in
            addToMovieStream = { movie in
                continuation.yield(movie)
            }
        }
    }()
    
    private var deviceOrientation: UIDeviceOrientation {
        var orientation = UIDevice.current.orientation
        if orientation == UIDeviceOrientation.unknown {
            orientation = UIScreen.main.orientation
        }
        return orientation
    }
    
    private func videoOrientationFor(_ deviceOrientation: UIDeviceOrientation) -> AVCaptureVideoOrientation? {
        switch deviceOrientation {
        case .portrait: return AVCaptureVideoOrientation.portrait
        case .portraitUpsideDown: return AVCaptureVideoOrientation.portraitUpsideDown
        case .landscapeLeft: return AVCaptureVideoOrientation.landscapeRight
        case .landscapeRight: return AVCaptureVideoOrientation.landscapeLeft
        default: return nil
        }
    }
    
    private var captureDevice: AVCaptureDevice? {
        didSet {
            guard let captureDevice = captureDevice else { return }
            debugPrint("Using capture device: \(captureDevice.localizedName)")
            sessionQueue.async {
                self.updateSessionForCaptureDevice(captureDevice)
            }
        }
    }
    
    private func updateSessionForCaptureDevice(_ captureDevice: AVCaptureDevice) {
        guard isCaptureSessionConfigured else { return }
        
        captureSession.beginConfiguration()
        defer { captureSession.commitConfiguration() }

        for input in captureSession.inputs {
            if let deviceInput = input as? AVCaptureDeviceInput {
                captureSession.removeInput(deviceInput)
            }
        }
        
        if let deviceInput = deviceInputFor(device: captureDevice) {
            if !captureSession.inputs.contains(deviceInput), captureSession.canAddInput(deviceInput) {
                captureSession.addInput(deviceInput)
            }
        }
        
        updateVideoOutputConnection()
    }
    
    private func updateVideoOutputConnection() {
        if let videoOutput = videoOutput, let videoOutputConnection = videoOutput.connection(with: .video) {
            if videoOutputConnection.isVideoMirroringSupported {
                videoOutputConnection.isVideoMirrored = isUsingFrontCaptureDevice
            }
        }
    }
    
    var isUsingFrontCaptureDevice: Bool {
        guard let captureDevice = captureDevice else { return false }
        return frontCaptureDevices.contains(captureDevice)
    }
    
    private var frontCaptureDevices: [AVCaptureDevice] {
        allCaptureDevices
            .filter { $0.position == .front }
    }
    
    private var allCaptureDevices: [AVCaptureDevice] {
        AVCaptureDevice.DiscoverySession(deviceTypes: [.builtInTrueDepthCamera, .builtInDualCamera, .builtInDualWideCamera, .builtInWideAngleCamera, .builtInDualWideCamera], mediaType: .video, position: .unspecified).devices
    }
    
    private var microphone: AVCaptureDevice? {
        AVCaptureDevice.DiscoverySession(deviceTypes: [.builtInMicrophone], mediaType: .audio, position: .unspecified).devices.first
    }
    
    private func deviceInputFor(device: AVCaptureDevice?) -> AVCaptureDeviceInput? {
        guard let validDevice = device else { return nil }
        do {
            return try AVCaptureDeviceInput(device: validDevice)
        } catch let error {
            debugPrint("Error getting capture device input: \(error.localizedDescription)")
            return nil
        }
    }
    
    func checkAuthorization() async -> Bool {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            debugPrint("Camera access authorized.")
            return true
        case .notDetermined:
            debugPrint("Camera access not determined.")
            sessionQueue.suspend()
            let status = await AVCaptureDevice.requestAccess(for: .video)
            sessionQueue.resume()
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
    
    init() {
        super.init(nibName: nil, bundle: nil)
        
        sessionQueue = DispatchQueue(label: "session queue")
                
        captureDevice = availableCaptureDevices.first ?? AVCaptureDevice.default(for: .video)
        
        UIDevice.current.beginGeneratingDeviceOrientationNotifications()

        UIDevice.current.beginGeneratingDeviceOrientationNotifications()
        NotificationCenter.default.addObserver(self, selector: #selector(updateForDeviceOrientation), name: UIDevice.orientationDidChangeNotification, object: nil)
    }
    
    @objc
    func updateForDeviceOrientation() {
        //TODO: Figure out if we need this for anything.
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    private var backCaptureDevices: [AVCaptureDevice] {
        allCaptureDevices
            .filter { $0.position == .back }
    }

    private var captureDevices: [AVCaptureDevice] {
        var devices = [AVCaptureDevice]()
        #if os(macOS) || (os(iOS) && targetEnvironment(macCatalyst))
        devices += allCaptureDevices
        #else
        if let backDevice = backCaptureDevices.first {
            devices += [backDevice]
        }
        if let frontDevice = frontCaptureDevices.first {
            devices += [frontDevice]
        }
        #endif
        return devices
    }

    private var availableCaptureDevices: [AVCaptureDevice] {
        captureDevices
            .filter( { $0.isConnected } )
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.configureCaptureSession(completionHandler: {_ in
            previewLayer = AVCaptureVideoPreviewLayer(session: captureSession)
            previewLayer.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.width * 4 / 3)
            previewLayer.connection?.videoOrientation = .portrait
            previewLayer.videoGravity = .resizeAspectFill
            view.layer.insertSublayer(previewLayer, at: 0)
        })
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if (captureSession.isRunning == false) {
            sessionQueue.async { [self] in
                captureSession.startRunning()
            }
        }
    }
    
    private func configureCaptureSession(completionHandler: (_ success: Bool) -> Void) {
        
        var success = false
        
        self.captureSession.beginConfiguration()
        
        defer {
            self.captureSession.commitConfiguration()
            completionHandler(success)
        }
        
        captureDevice = AVCaptureDevice.default(for: .video)
        
        guard
            let captureDevice = captureDevice,
            let deviceInput = try? AVCaptureDeviceInput(device: captureDevice)
        else {
            debugPrint("Failed to obtain video input.")
            return
        }
        
        var microphoneInput: AVCaptureDeviceInput? = nil
        if let microphone = microphone {
            microphoneInput = try? AVCaptureDeviceInput(device: microphone)
        }
        
        let photoOutput = AVCapturePhotoOutput()
                        
        captureSession.sessionPreset = AVCaptureSession.Preset.photo

        let videoOutput = AVCaptureVideoDataOutput()
        videoOutput.setSampleBufferDelegate(self, queue: DispatchQueue(label: "VideoDataOutputQueue"))
  
        let videoFileOutput = AVCaptureMovieFileOutput()
        
        guard captureSession.canAddInput(deviceInput) else {
            debugPrint("Unable to add device input to capture session.")
            return
        }
        
        var canAddMicrophoneInput: Bool = false
        if let microphoneInput = microphoneInput {
            canAddMicrophoneInput = captureSession.canAddInput(microphoneInput)
        }
        
        guard captureSession.canAddOutput(photoOutput) else {
            debugPrint("Unable to add photo output to capture session.")
            return
        }
        guard captureSession.canAddOutput(videoOutput) else {
            debugPrint("Unable to add video output to capture session.")
            return
        }
        
        guard captureSession.canAddOutput(videoFileOutput) else {
            debugPrint("Unable to add movie output to capture session.")
            return
        }
        
        if canAddMicrophoneInput {
            captureSession.addInput(microphoneInput!)
        }
        
        captureSession.addInput(deviceInput)
        captureSession.addOutput(photoOutput)
        captureSession.addOutput(videoOutput)
        captureSession.addOutput(videoFileOutput)
        
        self.deviceInput = deviceInput
        self.photoOutput = photoOutput
        self.videoOutput = videoOutput
        self.videoFileOutput = videoFileOutput
        
        photoOutput.isHighResolutionCaptureEnabled = true
        photoOutput.maxPhotoQualityPrioritization = .quality
        
        updateVideoOutputConnection()
        
        isCaptureSessionConfigured = true
        
        success = true
    }
    
    func hasFlash() -> Bool {
        if let d = self.captureDevice {
            return d.hasFlash
        }
        
        return false
    }
    
    func stop() {
        guard isCaptureSessionConfigured else { return }
        
        if captureSession.isRunning {
            sessionQueue.async {
                self.captureSession.stopRunning()
            }
        }
    }
    
    func takePhoto(flashMode: CameraFlashModeEnum) {
        guard let photoOutput = self.photoOutput else { return }
        
        sessionQueue.async {
        
            var photoSettings = AVCapturePhotoSettings()

            if photoOutput.availablePhotoCodecTypes.contains(.hevc) {
                photoSettings = AVCapturePhotoSettings(format: [AVVideoCodecKey: AVVideoCodecType.hevc])
            }
            
            let isFlashAvailable = self.deviceInput?.device.isFlashAvailable ?? false
            photoSettings.flashMode = .off
            if flashMode == .On || flashMode == .Auto {
                if isFlashAvailable {
                    if flashMode == .On {
                        photoSettings.flashMode = .on
                    } else {
                        photoSettings.flashMode = .auto
                    }
                }
            }
            
            photoSettings.isHighResolutionPhotoEnabled = true
            if let previewPhotoPixelFormatType = photoSettings.availablePreviewPhotoPixelFormatTypes.first {
                photoSettings.previewPhotoFormat = [kCVPixelBufferPixelFormatTypeKey as String: previewPhotoPixelFormatType]
            }
            photoSettings.photoQualityPrioritization = .balanced
            
            if let photoOutputVideoConnection = photoOutput.connection(with: .video) {
                if photoOutputVideoConnection.isVideoOrientationSupported,
                    let videoOrientation = self.videoOrientationFor(self.deviceOrientation) {
                    photoOutputVideoConnection.videoOrientation = videoOrientation
                }
            }
            
            photoOutput.capturePhoto(with: photoSettings, delegate: self)
        }
    }
    
    func startVideoRecorder(url: URL) {
        guard let videoFileOutput = self.videoFileOutput else { return }
        
        sessionQueue.async {
            videoFileOutput.startRecording(to: url, recordingDelegate: self)
        }
    }
    
    func stopVideoRecorder() {
        guard let videoFileOutput = self.videoFileOutput else { return }
        
        sessionQueue.async {
            videoFileOutput.stopRecording()
        }
    }
    
    func captureOutput(_ output: AVCaptureOutput, didOutput sampleBuffer: CMSampleBuffer, from connection: AVCaptureConnection) {
        guard let pixelBuffer = sampleBuffer.imageBuffer else { return }
        
        if connection.isVideoOrientationSupported,
           let videoOrientation = videoOrientationFor(deviceOrientation) {
            connection.videoOrientation = videoOrientation
        }

        addToPreviewStream?(CIImage(cvPixelBuffer: pixelBuffer))
    }
    
    func photoOutput(_ output: AVCapturePhotoOutput, didFinishProcessingPhoto photo: AVCapturePhoto, error: Error?) {
        
        if let error = error {
            debugPrint("Error capturing photo: \(error.localizedDescription)")
            return
        }
        
        addToPhotoStream?(photo)
    }
    
    func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {
        
        if let error = error {
            debugPrint("Error recording photo: \(error.localizedDescription)")
            return
        }
        
        addToMovieStream?(outputFileURL)
    }

    func failed() {
//        captureSession = nil
    }
    
    private func checkPermissions() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { granted in
              if !granted {
                // showPermissionsAlert
              }
            }
        case .denied, .restricted:
            //showPermissionsAlert
            break
        default:
            return
        }
    }
}

struct CameraView: UIViewControllerRepresentable {
    typealias UIViewControllerType = CameraViewController
    var controller: CameraViewController
    
    func makeUIViewController(context: Context) -> CameraViewController {
        return controller
    }

    func updateUIViewController(_ uiViewController: CameraViewController, context: Context) {
    }
}


fileprivate extension UIScreen {

    var orientation: UIDeviceOrientation {
        let point = coordinateSpace.convert(CGPoint.zero, to: fixedCoordinateSpace)
        if point == CGPoint.zero {
            return .portrait
        } else if point.x != 0 && point.y != 0 {
            return .portraitUpsideDown
        } else if point.x == 0 && point.y != 0 {
            return .landscapeRight //.landscapeLeft
        } else if point.x != 0 && point.y == 0 {
            return .landscapeLeft //.landscapeRight
        } else {
            return .unknown
        }
    }
}
