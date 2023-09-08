//
//  UserCheckedUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

class UserCheckedUIViewModel<T: Member> {
    var userCheck: UserSelectionVM<T>.UserCheck
    var avatarImageView: String
    var nameTextView: String
    var onRemove: (String) -> Void
    
    init(bound: UserSelectionVM<T>.UserCheck, onRemove: @escaping (String) -> Void) {
        let user = bound.user
        self.userCheck = bound
        self.nameTextView = user.userName ?? ""
        self.avatarImageView = user.avatarUri?.uri.path ?? Resources.ic_avatar
        self.onRemove = onRemove
    }
}

struct UserCheckedUIView<T: Member>: View {
    var vm: UserCheckedUIViewModel<T>
    var body: some View {
        VStack {
            Image(vm.avatarImageView)
                .resizable()
                .scaledToFill()
                .frame(width: 48, height: 48)
                .overlay(
                    Image(systemName: "x.circle.fill")
                        .foregroundColor(Color(Resources.colorPrimary))
                        .position(x: 45, y: 0)
                )
            
            Text(vm.nameTextView)
                .multilineTextAlignment(.leading)
                .lineLimit(nil)
                .frame(maxWidth: .infinity, alignment: .center)
                .foregroundColor(.black)
                .font(.system(size: 12))
                .padding(.horizontal, 4)
        }
        .onTapGesture {
            self.vm.onRemove(vm.userCheck.user.userId)
            self.endTextEditing()
        }
    }
}

//struct UserCheckedUIView_Previews: PreviewProvider {
//    static var previews: some View {
//        UserCheckedUIView(vm: UserCheckedUIViewModel<Member>(bound: UserSelectionVM<Member>.UserCheck(user: Member(userName: "Cuong 1", avatarUri: nil, userId: "1223", companyCd: "12"), checked: true, keepState: true), onRemove: {_ in }))
//    }
//}
