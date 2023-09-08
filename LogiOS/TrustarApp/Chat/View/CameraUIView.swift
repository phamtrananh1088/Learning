//
//  CameraUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct CameraUIView: View {
    var controller: CameraViewController
    @Binding var cameraMode: CameraModeEnum
    @Binding var flashMode: CameraFlashModeEnum
    @State private var initOrientation: UIDeviceOrientation = .unknown
    
    var onHide: () -> Void
    var onClickFlash: () -> Void
    var onSwitchMode: (CameraModeEnum) -> Void
    var onCapture: () -> Void
    
    private static let barHeightFactor = 0.15
    private static let lineWidth: CGFloat = 120
    private var FlashModeImage: Image {
        switch flashMode {
        case .On:
            return Image(systemName: "bolt.fill")
        case .Off:
            return Image(systemName: "bolt.slash.fill")
        case .Auto:
            return Image(systemName: "bolt.badge.a.fill")
        case .NotAvailable:
            return Image(systemName: "bolt.slash.fill")
        }
    }
    
    var body: some View {
        GeometryReader { geometry in
            VStack(spacing: 0) {
                // Header
                Button(action: {
                    onHide()
                }, label: {
                    Image(systemName: Resources.x_mark)
                        .foregroundColor(Color(Resources.lightText))
                        .shadow(color: .black, radius: 1)
                        .font(.system(size: 18))
                        .padding(5)
                })
                .frame(maxWidth:.infinity, alignment: .leading)
                .padding(.bottom, 10)
                
                CameraView(controller: controller)
                    .overlay(
                        VStack(spacing: 0) {
                            ZStack {
                                Button(action: {
                                    onClickFlash()
                                }, label: {
                                    FlashModeImage
                                        .foregroundColor((cameraMode == .Photo && flashMode != .NotAvailable) ? Color(Resources.lightText) : Color.clear)
                                        .shadow(color: .black, radius: 1)
                                        .font(.system(size: 24))
                                        .padding(5)
                                })
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.leading)
                                
                                HStack(spacing: 20) {
                                    Button(action: {
                                        onSwitchMode(.Photo)
                                    }, label: {
                                        Image(systemName: "camera.fill")
                                            .foregroundColor(Color(Resources.lightText))
                                            .shadow(color: .black, radius: 1)
                                            .font(.system(size: 24))
                                            .padding(5)
                                    })
                                    
                                    Button(action: {
                                        if cameraMode != .VideoRecording {
                                            onSwitchMode(.VideoNonRecording)
                                        }
                                        
                                    }, label: {
                                        Image(systemName: "video.fill")
                                            .foregroundColor(Color(Resources.lightText))
                                            .shadow(color: .black, radius: 1)
                                            .font(.system(size: 24))
                                            .padding(5)
                                    })
                                }
                            }
                            .padding(.vertical, 10)
                            
                            VStack(spacing: 0) {
                                Rectangle()
                                    .fill(Color(Resources.lightText))
                                    .frame(width: Self.lineWidth / 2, height: 3)
                                    .frame(maxWidth: .infinity, alignment: cameraMode == .Photo ? .leading : .trailing)
                                    .animation(.default)
                                Rectangle()
                                    .fill(Color(Resources.lightText))
                                    .frame(width: Self.lineWidth, height: 1)
                            }
                            .frame(width: Self.lineWidth)
                        }
                            .padding(.bottom, 20)
                        ,alignment: .bottom
                    )
                    
                    Spacer()
                    
                    // Footer
                    ZStack {
                        Image(systemName: "circle")
                            .foregroundColor(Color(Resources.lightText))
                            .shadow(color: .black, radius: 1)
                            .font(.system(size: 80))
                        
                        if cameraMode == .VideoRecording {
                            Image(systemName: "square.fill")
                                .foregroundColor(.red)
                                .shadow(color: .black, radius: 1)
                                .font(.system(size: 25))
                        } else {
                            Image(systemName: "circle.fill")
                                .foregroundColor(Color(Resources.lightText))
                                .shadow(color: .black, radius: 1)
                                .font(.system(size: 65))
                        }
                    }
                    .onTapGesture {
                        onCapture()
                    }
                    .padding(.bottom, 10)
                }
                .background(Color.black)
                .edgesIgnoringSafeArea(.bottom)
        }
        .onAppear {
            initOrientation = UIDevice.current.orientation == .unknown ? .portrait : UIDevice.current.orientation
            
            Helper.Shared.lockOrientation(.portrait, andRotateTo: .portrait)
        }
        .onDisappear(perform: {
            DispatchQueue.main.async {
                Helper.Shared.lockOrientation(.allButUpsideDown, andRotateTo: initOrientation)
            }
        })
    }
}

//struct CameraUIView_Previews: PreviewProvider {
//    @State static var mode: CameraModeEnum = .Photo
//    @State static var flashMode: CameraFlashModeEnum = .Auto
//    static var previews: some View {
//        CameraUIView(
//            textInit: .constant("abc"),
//            image: .constant(Image(systemName: "")),
//            cameraMode: $mode,
//            flashMode: $flashMode,
//            onHide: {
//            },
//            onClickFlash: {},
//            onSwitchMode: { _ in },
//            onCapture: {}
//        )
//    }
//}
