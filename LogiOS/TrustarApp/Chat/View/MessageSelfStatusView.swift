//
//  MessageSelfStatusView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2023/01/10.
//  Copyright © 2023 DionSoftware. All rights reserved.
//

import SwiftUI

struct MessageSelfStatusView: View {
    var vm: AbstractMessageUIViewModel
    var onShowHideReaders: (GeometryProxy, MessageItemChat.Sent) -> Void
    
    var body: some View {
        VStack(alignment: .trailing, spacing: 8) {
            GeometryReader { geo in
                VStack(spacing: 0) {
                    Spacer()
                    HStack {
                        Spacer()
                        Text(vm.statusTextView)
                            .foregroundColor((vm.statusTextView == Resources.msg_tap_to_retry) ? .red : .black)
                            .font(.system(size: 12))
                            .onTapGesture {
                                if let sent = vm.bound as? MessageItemChat.Sent {
                                    if !sent.readers.isEmpty {
                                        onShowHideReaders(geo, sent)
                                    }
                                }
                            }
                    }
                }
            }
            
            Text(vm.sentTimeTextView)
                .foregroundColor(.black)
                .font(.system(size: 12))
        }
    }
}
