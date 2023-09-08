//
//  DocumentPicker.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/22.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import UIKit
import SwiftUI

struct DocumentPicker: UIViewControllerRepresentable {
    
    @Binding var isPresented: Bool
    var onPicked: (URL) -> Void
    
    func makeCoordinator() -> DocumentPicker.Coordinator {
        return DocumentPicker.Coordinator(parent1: self)
    }
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<DocumentPicker>) -> UIDocumentPickerViewController {
        let picker = UIDocumentPickerViewController(documentTypes: ["public.item"], in: .open)
        picker.allowsMultipleSelection = false
        picker.delegate = context.coordinator
        return picker
    }
    
    func updateUIViewController(_ uiViewController: DocumentPicker.UIViewControllerType, context: UIViewControllerRepresentableContext<DocumentPicker>) {
    }
    
    class Coordinator: NSObject, UIDocumentPickerDelegate {
        
        var parent: DocumentPicker
        
        init(parent1: DocumentPicker){
            parent = parent1
        }
        func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
            parent.onPicked(urls[0])
        }
        
        func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
            parent.isPresented = false
        }
    }
}
