//
//  VideoPlayerView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import UIKit
import Combine
import AVKit
import AVFoundation

struct VideoPlayerView: UIViewControllerRepresentable {
    var player: AVPlayerViewController
    var onEnd: () -> Void
    
    init(player: AVPlayerViewController, onEnd: @escaping () -> Void) {
        self.player = player
        self.onEnd = onEnd
    }
    
    typealias UIViewControllerType = AVPlayerViewController

    func makeUIViewController(context: Context) -> AVPlayerViewController {
        player.delegate = context.coordinator
        return player
    }

    func updateUIViewController(_ uiViewController: AVPlayerViewController, context: Context) {
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(onEnd)
    }
    
    final class Coordinator: NSObject, AVPlayerViewControllerDelegate {
        let onEnd: () -> Void
        init(_ onEnd: @escaping () -> Void) {
            self.onEnd = onEnd
        }
        
        func playerViewController(_ playerViewController: AVPlayerViewController, willEndFullScreenPresentationWithAnimationCoordinator coordinator: UIViewControllerTransitionCoordinator) {
            onEnd()
        }
    }
}
