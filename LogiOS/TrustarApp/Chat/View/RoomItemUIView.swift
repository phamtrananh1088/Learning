//
//  RoomItemUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

class RoomItemUIViewModel {
    var avatarImageView: String
    var contentTextView: String
    var timeTextView: String
    var unreadTextView: String
    var roomId: String
    
    init(bound: ChatRoomExt) {
        self.avatarImageView = bound.imageUri()?.uri.path ?? Resources.ic_avatar
        self.contentTextView = bound.title()
        if bound.sameDayToLastUpdated(time: currentTimeMillis()) {
            self.timeTextView =  bound.lastUpdateHHmm()
        } else {
            self.timeTextView =  bound.lastUpdateMMdd()
        }
        
        self.unreadTextView = bound.room.unread > 99 ? "99+" : String(bound.room.unread)
        
        self.roomId = bound.room.id
    }
}

struct RoomItemUIView: View {
    var roomItemVm: RoomItemUIViewModel
    var body: some View {
        HStack {
            Image(roomItemVm.avatarImageView)
                .resizable()
                .scaledToFill()
                .frame(width: 54, height: 54)
                .padding(.vertical, 10)
            
            Text(roomItemVm.contentTextView)
                .multilineTextAlignment(.leading)
                .lineLimit(nil)
                .frame(maxWidth: .infinity, alignment: .leading)
                .foregroundColor(.black)
                .font(.system(size: 18))
            
            VStack(alignment: .center) {
                Text(roomItemVm.timeTextView)
                    .foregroundColor(.black)
                    .frame(maxHeight: .infinity)
                    .font(.system(size: 14))
                
                Text(roomItemVm.unreadTextView)
                    .foregroundColor(.white)
                    .font(.system(size: 12))
                    .bold()
                    .if(roomItemVm.unreadTextView.length == 1) {
                        $0.frame(width: 22, height: 22)
                    }
                    .if(roomItemVm.unreadTextView.length == 2) {
                        $0.frame(width: 30, height: 22)
                    }
                    .if(roomItemVm.unreadTextView.length == 3) {
                        $0.frame(width: 37, height: 22)
                    }
                    .background(Color(Resources.colorPrimary))
                    .cornerRadius(CGFloat(45))
                    .if(roomItemVm.unreadTextView == "0") {
                        $0.hidden()
                    }
            }
            .frame(height: 40)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
        .padding(.horizontal)
    }
}

struct RoomItemUIView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 5) {
            RoomItemUIView(roomItemVm: RoomItemUIViewModel(bound: ChatRoomExt(room: ChatRoom(id: "1", name: "cuong 1", image: "", unread: 2, userCount: 3, lastUpdated: 1213123232, notification: true, version: "1"))))
            
            RoomItemUIView(roomItemVm: RoomItemUIViewModel(bound: ChatRoomExt(room: ChatRoom(id: "1", name: "Cuong 2", image: "", unread: 999, userCount: 4, lastUpdated: 1213123232, notification: true, version: "1"))))
        }
        .padding()
    }
}
