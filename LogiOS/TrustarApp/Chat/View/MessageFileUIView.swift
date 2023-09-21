//
//  MessageFileUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine
import AVKit
import AVFoundation

class MessageFileUIViewModel: AbstractMessageUIViewModel, ObservableObject {
    
    /// Current user: True
    /// Another user: False
    var rtl: Bool
    
    var boundFileItem: FProtocol?
    @Published var isReady: Bool = false
    @Published private var startDownload: Bool = false
    @Published var statusDownLoad: Int = 0
    @Published var isPresented: Bool = false
    var pathFile: URL? {
        if let att = boundFileItem?.attachmentExt() {
            return att.contentUri()
        }
        
        return nil
    }
    
    init(rtl: Bool, bound: MessageItemChat) {
        self.rtl = rtl
        super.init(bound: bound)
        
        self.boundFileItem = bound as? FProtocol
        if pathFile != nil {
            if FileManager.default.fileExists(atPath: pathFile!.path) {
                self.isReady = true
            }
        }
        
        $startDownload
            .setFailureType(to: NetworkError.self)
            .map({ d -> AnyPublisher<(Optional<Data>, Int), NetworkError> in
                if !d {
                    return Just((Optional(nil), 0)).setFailureType(to: NetworkError.self).eraseToAnyPublisher()
                }
                
                return Download().download(fileKey: self.boundFileItem!.attachmentExt()!.key)
                    .map({ (result, progress) in
                        switch result {
                        case .Loading:
                            return Just((Optional<Data>(nil), progress))
                                .setFailureType(to: NetworkError.self)
                        case .Value(value: let value):
                            return Just((Optional(value!), 100))
                                .setFailureType(to: NetworkError.self)
                        case .Error(error: _):
                            return Just((Optional(nil), 0))
                                .setFailureType(to: NetworkError.self)
                        }
                    })
                    .switchToLatest()
                    .eraseToAnyPublisher()
            })
            .switchToLatest()
            .eraseToAnyPublisher()
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { completion in },
                  receiveValue: {[self] (resultData, progress) in
                statusDownLoad = progress
                    
                if let data = resultData {
                    if let path = pathFile {
                        do {
                            if FileManager.default.fileExists(atPath: path.path) {
                                try FileManager.default.removeItem(atPath: path.path)
                            }
                            
                            try data.write(to: path)
                            
                            self.setLongClickMenu(binder: bound)
                            
                            isReady = true
                        } catch {
                            print("SaveFileError: \(error.localizedDescription)")
                        }
                    }
                }
            }).store(in: &bag)
    }
    
    func download() {
        if (boundFileItem?.attachmentExt()?.key) != nil {
            if statusDownLoad != 0 {
                return
            }
            
            startDownload = true
        }
    }
}

struct MessageFileUIView: View {
    
    @ObservedObject var vm: MessageFileUIViewModel
    var onTapGesture: () -> Void
    var onShowHideReaders: (GeometryProxy, MessageItemChat.Sent) -> Void
    
    private var gd: Color {
        if vm.rtl {
            return Color(Resources.colorPrimary)
        }
        
        return Color(Resources.grayBackground)
    }
    
    var max: CGFloat = 192
    var body: some View {
        ZStack {
            if vm.rtl {
                VStack {
                    HStack {
                        MessageSelfStatusView(vm: vm, onShowHideReaders: onShowHideReaders)
                        
                        VStack(spacing: 0) {
                            ZStack {
                                Image(systemName: "doc.fill")
                                    .foregroundColor(Color(Resources.lightText))
                                    .font(.system(size: 24))
                                    .padding(8)
                                
                                if vm.statusDownLoad > 0 && !vm.isReady {
                                    Circle()
                                        .trim(from: 0.0, to: (Double(vm.statusDownLoad) * 3.6) / Double(360))
                                        .stroke(
                                            style: StrokeStyle(
                                                lineWidth: 3,
                                                lineCap: .round,
                                                lineJoin: .round))
                                        .foregroundColor(.white)
                                        .rotationEffect(Angle(degrees: 270))
                                        .frame(width: 25, height: 25)
                                }
                            }
                            
                            Text(vm.boundFileItem?.attachmentExt()?.name ?? "")
                                .foregroundColor(Color(Resources.lightText))
                                .font(.system(size: 20, weight: .bold))
                                .padding(10)
                            
                            Text(humanReadableSize(value: vm.boundFileItem?.attachmentExt()?.size ?? 0))
                                .foregroundColor(Color(Resources.lightText))
                                .font(.system(size: 14))
                                .padding(10)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            
                            Rectangle()
                                .fill(.white)
                                .frame(height: 0.5)
                                .edgesIgnoringSafeArea(.horizontal)
                                .padding(.horizontal, 10)
                            
                            Text(vm.isReady ? Resources.preview : Resources.download)
                                .foregroundColor(Color(Resources.lightText))
                                .font(.system(size: 16))
                                .padding(8)
                        }
                        .frame(maxWidth: max)
                        .background(gd)
                        .cornerRadius(CGFloat(8))
                        .onTapGesture {
                            onTapGesture()
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .trailing)
                    
                }.frame(alignment: .leading)
                .frame(maxWidth: .infinity, alignment: .topTrailing)
            } else {
                HStack(spacing: 5) {
                    if (vm.isShowAvatar) {
                        Image(vm.avatar)
                            .resizable()
                            .scaledToFill()
                            .frame(width: 48, height: 48)
                            .frame(maxHeight: .infinity, alignment: .top)
                            .padding(.top, 15)
                    }
                    
                    VStack {
                        if vm.isShowUserName {
                            Text(vm.username)
                                .font(.system(size: 14, weight: .bold))
                                .foregroundColor(Color(Resources.textColor))
                                .padding(.horizontal, 8)
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        
                        HStack {
                            VStack(spacing: 0) {
                                ZStack {
                                    Image(systemName: "doc.fill")
                                        .foregroundColor(Color(Resources.textColor))
                                        .font(.system(size: 24))
                                        .padding(8)
                                    
                                    if vm.statusDownLoad > 0 && !vm.isReady {
                                        Circle()
                                            .trim(from: 0.0, to: (Double(vm.statusDownLoad) * 3.6) / Double(360))
                                            .stroke(
                                                style: StrokeStyle(
                                                    lineWidth: 3,
                                                    lineCap: .round,
                                                    lineJoin: .round))
                                            .foregroundColor(.blue)
                                            .rotationEffect(Angle(degrees: 270))
                                            .frame(width: 25, height: 25)
                                    }
                                }
                                
                                Text(vm.boundFileItem?.attachmentExt()?.name ?? "")
                                    .foregroundColor(Color(Resources.textColor))
                                    .font(.system(size: 20, weight: .bold))
                                    .padding(10)
                                
                                Text(humanReadableSize(value: vm.boundFileItem?.attachmentExt()?.size ?? 0))
                                    .foregroundColor(Color(Resources.textColor))
                                    .font(.system(size: 14))
                                    .padding(10)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                
                                Rectangle()
                                    .fill(.white)
                                    .frame(height: 0.5)
                                    .edgesIgnoringSafeArea(.horizontal)
                                    .padding(.horizontal, 10)
                                
                                Text(vm.isReady ? Resources.preview : Resources.download)
                                    .foregroundColor(Color(Resources.textColor))
                                    .font(.system(size: 16))
                                    .padding(8)
                            }
                            .frame(maxWidth: max)
                            .background(gd)
                            .cornerRadius(CGFloat(8))
                            .onTapGesture {
                                onTapGesture()
                            }
                            
                            Text(vm.sentTimeTextView)
                                .foregroundColor(.black)
                                .font(.system(size: 12))
                                .frame(maxHeight: .infinity, alignment: .bottomLeading)
                                .padding(2)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .frame(alignment: .leading)
                    .if(!vm.isShowAvatar) {
                        $0.padding(.leading, 53)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .topLeading)
            }
            
            if vm.isPresented {
                QuickLookView(url: vm.pathFile!, isPresented: $vm.isPresented)
            }
        }
        .padding(.horizontal)
        .if (!vm.bound.hasNext) {
            $0.padding(.bottom, 5)
        }
    }
}