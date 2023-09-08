//
//  UserCheckItemUIView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/11/29.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine

class UserCheckItemUIViewModel<T: Member>: ObservableObject, Equatable, Hashable {

    static func == (lhs: UserCheckItemUIViewModel<T>, rhs: UserCheckItemUIViewModel<T>) -> Bool {
        return lhs.userCheck.user.userId == rhs.userCheck.user.userId
        && lhs.userCheck.user.userName == rhs.userCheck.user.userName
        && lhs.checkBox == rhs.checkBox
        && lhs.userCheck.keepState == rhs.userCheck.keepState
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(uuid)
    }
    
    var uuid: String
    var userCheck: UserSelectionVM<T>.UserCheck
    var avatarImageView: String
    var nameTextView: String
    @Published var checkBox: Bool
    var isCheckBoxEnable: Bool
    var onClick: (String, Bool) -> Void
    
    init(bound: UserSelectionVM<T>.UserCheck, onClick: @escaping (String, Bool) -> Void) {
        let u = bound.user
        self.userCheck = bound
        self.nameTextView = u.userName.orEmpty()
        self.avatarImageView = bound.user.avatarUri?.uri.path ?? Resources.ic_avatar
        self.checkBox = bound.checked
        self.isCheckBoxEnable = !bound.keepState
        self.onClick = onClick
        self.uuid = UUID().uuidString
    }
    
//    init(value: UserSelectionVM.UserCheck<Member>, position: Int, payLoads: [Any]) {
//        
//    }

    func areContentsTheSame(newItem: UserCheckItemUIViewModel<T>) -> Bool {
        let n = newItem.userCheck
        return userCheck.user.userName == n.user.userName && checkBox == newItem.checkBox && userCheck.keepState == newItem.userCheck.keepState
    }

    func getChangePayload(
        newItem: UserSelectionVM<T>.UserCheck
    ) -> String? {
        let o = userCheck.user
        let n = newItem.user
        return o.userName == n.userName ? "checked" : nil
    }
}

struct UserCheckItemUIView<T: Member>: View {
    @ObservedObject var vm: UserCheckItemUIViewModel<T>
    var body: some View {
        Button(action: {
            vm.checkBox.toggle()
            vm.onClick(vm.userCheck.user.userId, vm.checkBox)
        }, label: {
            HStack {
                Text("")
                    .frame(width: 0)
                
                CheckBox(checked: $vm.checkBox)
                
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
            .frame(maxWidth: .infinity, alignment: .leading)
        })
    }
}

//struct UserCheckItemUIView_Previews: PreviewProvider {
//    static var previews: some View {
//        UserCheckItemUIView(vm: UserCheckItemUIViewModel(bound: UserSelectionVM<Member>.UserCheck(user: Member(userName: "Cuong 1", avatarUri: nil, userId: "1", companyCd: "123"), checked: false, keepState: true)))
//    }
//}
