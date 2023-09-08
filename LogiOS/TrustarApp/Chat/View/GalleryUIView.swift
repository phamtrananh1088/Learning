//
//  GalleryUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import UIKit
import BSImagePicker
import Photos

struct GalleryUIView: UIViewControllerRepresentable  {
    var onFinish: ([PHAsset]) -> Void
    var onCancel: () -> Void
    
    typealias UIViewControllerType = ImagePickerController

    func makeUIViewController(context: Context) -> ImagePickerController {
        let picker = ImagePickerController()
        picker.imagePickerDelegate = context.coordinator
        picker.doneButtonTitle = Resources.determine2
        picker.cancelButton = UIBarButtonItem(barButtonSystemItem: .close, target: nil, action: nil)
        return picker
    }

    func updateUIViewController(_ uiViewController: ImagePickerController, context: Context) {
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(self, onFinish: onFinish, onCancel: onCancel)
    }
    
    final class Coordinator: NSObject, ImagePickerControllerDelegate {
        func imagePicker(_ imagePicker: ImagePickerController, didSelectAsset asset: PHAsset) {
            debugPrint("didSelectAsset")
        }
        
        func imagePicker(_ imagePicker: ImagePickerController, didDeselectAsset asset: PHAsset) {
            debugPrint("didDeselectAsset")
        }
        
        func imagePicker(_ imagePicker: ImagePickerController, didFinishWithAssets assets: [PHAsset]) {
            onFinish(assets)
        }
        
        func imagePicker(_ imagePicker: ImagePickerController, didCancelWithAssets assets: [PHAsset]) {
            onCancel()
        }
        
        func imagePicker(_ imagePicker: ImagePickerController, didReachSelectionLimit count: Int) {
            debugPrint("didReachSelectionLimit")
        }
        
        let control: GalleryUIView
        var onFinish: ([PHAsset]) -> Void
        var onCancel: () -> Void
        init(_ control: GalleryUIView,
             onFinish: @escaping ([PHAsset]) -> Void,
             onCancel: @escaping () -> Void) {
            self.control = control
            self.onFinish = onFinish
            self.onCancel = onCancel
        }
    }
}
