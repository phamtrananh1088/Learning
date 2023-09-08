//
//  RecorderUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct RecorderUIView: View {
    @Binding var recordingMode: RecordingModeEnum
    @Binding var basedSystemTimeMillis: Int64
    var onMicrophone: () -> Void
    var onHide: () -> Void
    var onSend: () -> Void
    var onPlay: () -> Void
    private let sizeCircle: CGFloat = 180
    private let sizeMicro: CGFloat = 50
    
    @State private var basedRealTime: Int64 = 0
    private let timer = Timer.publish(every: 0.2, on: .main, in: .common).autoconnect()
    private var timeDisplay: String {
        return String(format: "%02d:%02d:%02d", basedRealTime / 3600, (basedRealTime / 60) % 60, basedRealTime)
    }

    var body: some View {
        ZStack {
            
            Button(action: {
                onMicrophone()
                basedRealTime = 0
            }, label: {
                Circle()
                    .stroke(
                        style: StrokeStyle(
                            lineWidth: 0.5,
                            lineCap: .round,
                            lineJoin: .round))
                    .foregroundColor(.white)
                    .frame(width: sizeCircle, height: sizeCircle)
                    .overlay(
                        Image(systemName: recordingMode == .Recording ? "pause.fill" : "mic.fill")
                            .font(.system(size: sizeMicro))
                            .foregroundColor(.white)
                            .animation(.default))
            })
            
            if recordingMode == .Recording {
                Text(timeDisplay)
                    .font(.system(size: 25))
                    .foregroundColor(.white)
                    .padding(.top, 300)
                    .onReceive(timer, perform: {(_) in
                        self.basedRealTime = (currentTimeMillis() - basedSystemTimeMillis) / 1000
                    })
            } else if recordingMode == .FinishedRecording {
                Image(systemName: "play.circle.fill")
                    .font(.system(size: 35))
                    .foregroundColor(.white)
                    .padding(.top, 300)
                    .onTapGesture {
                        onPlay()
                    }
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.black)
        .edgesIgnoringSafeArea(.all)
        .overlay(
            // Header
            VStack(spacing: 0) {
                HomeHeaderView(isShowContent: false)
                HStack {
                    Button(action: {
                        onHide()
                    }, label: {
                        Image(systemName: Resources.x_mark)
                            .foregroundColor(Color(Resources.lightText))
                            .font(.system(size: 18))
                            .padding(5)
                    }).frame(maxWidth: .infinity, alignment: .leading)
                    
                    if recordingMode == .FinishedRecording {
                        Button(action: {
                            onSend()
                        }, label: {
                            Image(systemName: Resources.checkmark)
                                .foregroundColor(Color(Resources.lightText))
                                .font(.system(size: 18))
                                .padding(5)
                        }).frame(maxWidth: .infinity, alignment: .trailing)
                    }
                }.frame(maxWidth: .infinity, maxHeight: 50)
                 .background(Color(Resources.colorPrimary))
            }
                .edgesIgnoringSafeArea(.top)
            ,alignment: .top)
    }
}

struct RecorderUIView_Previews: PreviewProvider {
    @State static var recordingMode: RecordingModeEnum = .FinishedRecording
    @State static var time: Int64 = 1278978798
    static var previews: some View {
        RecorderUIView(
            recordingMode: $recordingMode,
            basedSystemTimeMillis: $time,
            onMicrophone: {},
            onHide: {},
            onSend: {},
            onPlay: {}
        )
    }
}
