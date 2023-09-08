//
//  UserItemUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

class UserItemUIViewModel: Hashable {
    static func == (lhs: UserItemUIViewModel, rhs: UserItemUIViewModel) -> Bool {
        return lhs.id == rhs.id
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
    var avatarImageView: String
    var nameTextView: String
    var id: String
    
    init(bound: ChatUser) {
        self.id = bound.id
        self.nameTextView = bound.name.orEmpty()
        self.avatarImageView = bound.avatarUri()?.uri.path ?? Resources.ic_avatar
    }
}

struct UserItemUIView: View {
    var vm: UserItemUIViewModel
    
    var body: some View {
        HStack {
            Image(vm.avatarImageView)
                .resizable()
                .scaledToFill()
                .frame(width: 48, height: 48)
            
            Text(vm.nameTextView)
                .multilineTextAlignment(.leading)
                .lineLimit(nil)
                .frame(maxWidth: .infinity, alignment: .leading)
                .foregroundColor(.black)
                .font(.system(size: 16))
                .padding(.horizontal, 4)
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 10)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color.white)
    }
}

//struct UserItemUIView_Previews: PreviewProvider {
//    static var previews: some View {
//        UserItemUIView()
//    }
//}
