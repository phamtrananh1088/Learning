//
//  SignView.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 20/02/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

/**
 * 署名
 */
struct SignView: View {
    
    public var onSave: (String) -> Void
    public var onCancel: () -> Void
    
    @State private var drawing = DrawingPath()
    @State private var color = Color.black
    @State private var lineWidth: CGFloat = 8
    @State private var maxHeight: CGFloat = 300
    
    @State private var showToast = false
    @State private var initOrientation: UIDeviceOrientation = .unknown
    
    public init(onSave: @escaping (String) -> Void,
                onCancel: @escaping () -> Void) {
        self.onSave = onSave
        self.onCancel = onCancel
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 3.0) {
            Text(Resources.sign_inside_area).padding(.top)
                .foregroundColor(Color.black)
            signatureContent
            HStack {
                Spacer()
                /*署名・クリア押下時に起動されるイベント*/
                Button(Resources.clear, action: clear)
                    .buttonStyle(RoundedRectangleButtonStyle())
                Spacer()
                /*署名・確定押下時に起動されるイベント*/
                Button(Resources.determine, action: saveAsImage)
                    .buttonStyle(RoundedRectangleButtonStyle())
                Spacer()
            }.padding(.top)
        }
        .padding()
        .frame(maxWidth:.infinity, maxHeight: .infinity)
        .background(Color.white)
        .toast(message: "Processing...",
               isShowing: $showToast,
               nonStop: true)
        .onAppear {
            initOrientation = UIDevice.current.orientation == .unknown ? .portrait : UIDevice.current.orientation
            
            if UIDevice.current.orientation == .landscapeRight {
                Helper.Shared.lockOrientation(.landscapeLeft, andRotateTo: .landscapeRight)
            } else {
                Helper.Shared.lockOrientation(.landscapeRight, andRotateTo: .landscapeLeft)
            }
        }
        .onDisappear(perform: {
            DispatchQueue.main.async {
                Helper.Shared.lockOrientation(.allButUpsideDown, andRotateTo: initOrientation)
            }
        })
    }
    
    private var signatureContent: some View {
        return Group {
            DrawingView(drawing: $drawing, color: $color, lineWidth: $lineWidth, maxHeight: $maxHeight)
        }
    }
    
    private func clear() {
        drawing = DrawingPath()
    }
    
    private func saveAsImage() {
        var filePath:String = ""
        
        self.showToast = true
        
        //Clear sign
        if drawing.points.count <= 0 {
            onSave(filePath)
            return
        }
        
        //Convert path to image
        let path = drawing.cgPath
        let maxX = drawing.points.map { $0.x }.max() ?? 0
        let renderer = UIGraphicsImageRenderer(size: CGSize(width: maxX + 10, height: maxHeight))
        
        let uiImage = renderer.image { ctx in
            let rect = CGRect(origin: .zero, size: CGSize.init(width: maxX + 10, height: maxHeight))
            UIColor.white.set()
            ctx.fill(rect)
            ctx.cgContext.setStrokeColor(UIColor(rgb: 0x000000).cgColor)
            ctx.cgContext.setLineCap(.round)
            ctx.cgContext.setLineJoin(.round)
            ctx.cgContext.setLineWidth(lineWidth)
            ctx.cgContext.beginPath()
            ctx.cgContext.addPath(path)
            ctx.cgContext.drawPath(using: .stroke)
        }
        
        if let data = uiImage.pngData() {
            let docsDir = Config.Shared.tmpDir
            let fileName = "Signature-\(Config.Shared.dateFormatter.format(date: Date())).png"
            let fileUrl = docsDir.appendingPathComponent(fileName)
            
            //Checks if file exists, removes it if so.
            if FileManager.default.fileExists(atPath: fileUrl.path) {
                do {
                    try FileManager.default.removeItem(atPath: fileUrl.path)
                    //print("Removed old image")
                } catch let removeError {
                    print("couldn't remove file at path", removeError)
                }
            }
            
            do {
                try data.write(to: fileUrl, options: .atomic)
            } catch let error {
                print("error saving file with error", error)
            }
            
            filePath = fileUrl.path
        }
        
        //call back to return previous screen
        onSave(filePath)
        
        //hide processing
        showToast = false
    }
}

struct SignView_Previews: PreviewProvider {
    static var previews: some View {
        let height = UIScreen.main.bounds.width //toggle width height
        let width = UIScreen.main.bounds.height
        
        SignView { _ in
            
        } onCancel: {
            
        }.previewLayout(PreviewLayout.fixed(width: height, height: width))
    }
}
