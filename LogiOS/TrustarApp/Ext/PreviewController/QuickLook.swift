//
//  QuickLook.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import UIKit
import QuickLook
import SwiftUI

final class QuickLookController: UIViewController, QLPreviewControllerDataSource, QLPreviewControllerDelegate {
    var url: URL
    @Binding var isPresented: Bool
    init(url: URL, isPresented: Binding<Bool>) {
        self.url = url
        self._isPresented = isPresented
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func numberOfPreviewItems(in controller: QLPreviewController) -> Int {
        return 1
    }
    
    func previewController(_ controller: QLPreviewController, previewItemAt index: Int) -> QLPreviewItem {
        return url as QLPreviewItem
    }
    
    func previewController(_ controller: QLPreviewController, editingModeFor previewItem: QLPreviewItem) -> QLPreviewItemEditingMode {
        
        return .createCopy
    }
    
    func previewController(_ controller: QLPreviewController, didUpdateContentsOf previewItem: QLPreviewItem) {
    }
    
    func previewController(_ controller: QLPreviewController, didSaveEditedCopyOf previewItem: QLPreviewItem, at modifiedContentsURL: URL) {
        
        let documentsDirectoryURL =  FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        
        let currentTimeStamp = String(Int(NSDate().timeIntervalSince1970))
        
        let destinationUrl = documentsDirectoryURL.appendingPathComponent("newFile\(currentTimeStamp).\(modifiedContentsURL.pathExtension)")
        
        if FileManager.default.fileExists(atPath: destinationUrl.path) {
            debugPrint("The file already exists at path")
        }
        else{
            do {
                try FileManager.default.moveItem(at: modifiedContentsURL, to: destinationUrl)
                print("File moved to documents folder")
            } catch let error as NSError {
                print(error.localizedDescription)
                
            }
        }
    }
    
    func previewControllerWillDismiss(_ controller: QLPreviewController) {
        self.isPresented = false
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        DispatchQueue.main.async {
            let previewController = QLPreviewController()
            previewController.dataSource = self
            previewController.delegate = self
            previewController.setEditing(true, animated: true)
            self.present(previewController, animated: true, completion: nil)
        }
    }
}

struct QuickLookView: UIViewControllerRepresentable {
    typealias UIViewControllerType = QuickLookController
    var url: URL
    @Binding var isPresented: Bool
    
    func makeUIViewController(context: Context) -> QuickLookController {
        return QuickLookController(url: url, isPresented: $isPresented)
    }

    func updateUIViewController(_ uiViewController: QuickLookController, context: Context) {
    }
}
