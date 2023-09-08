//
//  PhotoItemView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/19.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Photos

struct PhotoItemView: View {
    var asset: PhotoAsset
    var cache: CachedImageManager?
    var imageSize: CGSize
    
    @State private var image: Image?
    @State private var imageRequestID: PHImageRequestID?

    var body: some View {
        
        Group {
            if let image = image {
                image
                    .resizable()
                    .scaledToFill()
            } else {
                Text("loading")
            }
        }
        .onAppear() {
            Task {
                guard image == nil, let cache = cache else { return }
                imageRequestID = await cache.requestImage(for: asset, targetSize: imageSize) { result in
                    Task {
                        if let result = result {
                            self.image = result.image
                        }
                    }
                }
            }
        }
    }
}
