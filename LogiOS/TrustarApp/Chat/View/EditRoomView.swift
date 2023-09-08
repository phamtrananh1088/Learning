//
//  EditRoomView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/12/26.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct EditRoomView: View {
    @Binding var isShow: Bool
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var roomUserSelectionVm: RoomUserSelectionVM
    @ObservedObject var editRoomVm: EditRoomVM
    var callBack: () -> Void
    
    @State private var queryText: String = ""
    private var firstList: [UserCheckItemUIViewModel<TalkUser>] {
        if queryText.isEmpty {
            return roomUserSelectionVm.firstListLiveData
        }
        
        return roomUserSelectionVm.firstListLiveData.filter({ $0.userCheck.user.userName != nil && $0.userCheck.user.userName!.lowercased().contains(queryText.lowercased())})
    }
    
    private var secondList: [UserCheckItemUIViewModel<TalkUser>] {
        if queryText.isEmpty {
            return roomUserSelectionVm.secondListLiveData
        }
        
        return roomUserSelectionVm.secondListLiveData.filter({ $0.userCheck.user.userName != nil && $0.userCheck.user.userName!.lowercased().contains(queryText.lowercased())})
    }
    
    private var listChecked: [RoomUserSelectionVM.UserCheck] {
        return roomUserSelectionVm.selectedLiveData
    }
    
    private var title: String {
        return Resources.user_selected_other.replacingOccurrences(of: "%d", with: String(listChecked.count))
    }
    
    private var isLoading: Bool {
        
        switch roomUserSelectionVm.loadStateLiveData {
        case .Loading:
            return true
        default:
            return false
        }
    }
    
    private var loadStateLiveDataError: String {
        switch roomUserSelectionVm.loadStateLiveData {
            
        case .Error(error: let error):
            switch error {
            case .unknow:
                return "unknow"
            case .error(error: let error):
                return error.localizedDescription
            case .actionHttp401:
                return ""
            case .message(reason: let reason):
                return reason
            case .parseError(reason: let reason):
                return reason.localizedDescription
            case .networkError(reason: let reason):
                return reason
            }
        default:
            return ""
        }
    }
    
    private var creatRoomState: LoadingStateEnum {
        switch editRoomVm.stateLiveData {
        case .Value(value: let value):
            if value == nil { return .Ok }
            else {
                switch value! {
                    
                case .Loading:
                    return .Loading
                case .Error(error: _):
                    return .Error
                case .Value(value: _):
                    return .Ok
                }
            }
        case .Error(error: _):
            return .Error
        }
    }
    var body: some View {
        GeometryReader { geo in
            if isShow {
                ZStack {
                    VStack(spacing: 0) {
                        // Region: Header
                        HomeHeaderView(isShowContent: false)
                        
                        HStack {
                            Button(action: {
                                withAnimation {
                                    isShow = false
                                }
                            }, label: {
                                Image(systemName: "xmark")
                                    .foregroundColor(.white)
                                    .font(.system(size: 18))
                                    .frame(alignment: .topLeading)
                                    .padding(5)
                            })
                            
                            Text(title)
                                .foregroundColor(.white)
                                .bold()
                                .font(.system(size: 18))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            
                            if listChecked.count > 0 {
                                Button(
                                    action: {
                                        homeVm.editRoomVm.editRoom(roomId: homeVm.chatOptionVm.roomId, users: listChecked.map({ UserLite(userId: $0.user.userId, companyCd: $0.user.companyCd)}))
                                    },
                                    label: {
                                        Image(systemName: "checkmark")
                                            .foregroundColor(.white)
                                            .font(.system(size: 18, weight: .bold))
                                            .frame(maxWidth: .infinity, alignment: .trailing)
                                    })
                            }
                        }
                        .padding(.vertical, 10)
                        .padding(.horizontal, 8)
                        .background(Color(Resources.colorPrimary))
                        
                        // search
                        HStack {
                            ZStack {
                                if queryText.isEmpty {
                                    HStack {
                                        Image(systemName: "magnifyingglass")
                                            .font(.system(size: 15))
                                            .foregroundColor(.black)
                                        
                                        Text(Resources.search_by_name)
                                            .font(.system(size: 15))
                                            .foregroundColor(.gray)
                                            .opacity(0.5)
                                    }
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .padding(.leading, 12)
                                }
                                
                                TextField("", text: $queryText.animation(), onCommit: {})
                                    .font(.system(size: 15))
                                    .foregroundColor(.gray)
                                    .padding(.horizontal, 10)
                            }
                            
                            if !queryText.isEmpty {
                                Image(systemName: "xmark")
                                    .foregroundColor(.gray)
                                    .font(.system(size: 15))
                                    .onTapGesture {
                                        withAnimation {
                                            queryText.removeAll()
                                        }
                                    }
                                    .padding(.trailing, 10)
                            }
                        }
                        .frame(height: 40)
                        .background(Color.white)
                        .padding(.horizontal, 15)
                        .padding(.vertical, 10)
                        
                        if self.listChecked.count > 0 {
                            ScrollView(Axis.Set.horizontal) {
                                HStack {
                                    ForEach(listChecked, id: \.self) { item in
                                        UserCheckedUIView(vm: UserCheckedUIViewModel<TalkUser>(bound: item, onRemove: { id in
                                            roomUserSelectionVm.selectId(id: id, select: false)
                                        }))
                                    }
                                }
                                .padding(.vertical, 10)
                            }
                        }
                        
                        List {
                            Section(header: Text(Resources.talk_history)) {
                                ForEach(firstList, id: \.self) { row in
                                    UserCheckItemUIView<TalkUser>(vm: row)
                                        .listRowInsets(EdgeInsets(top: 10, leading: 5, bottom: 10, trailing: 5))
                                }
                            }
                            
                            Section(header: Text(Resources.logged_in_user)) {
                                ForEach(secondList, id: \.self) { row in
                                    UserCheckItemUIView<TalkUser>(vm: row)
                                        .listRowInsets(EdgeInsets(top: 10, leading: 5, bottom: 10, trailing: 5))
                                }
                            }
                        }
                        .id(firstList.isEmpty && secondList.isEmpty ? "1" : "2")
                        .animation(.default)
                        .modify {
                            if #available(iOS 15.0, *) {
                                $0.listStyle(SidebarListStyle())
                                    .refreshable {
                                        roomUserSelectionVm.loadUsers()
                                    }
                            } else {
                                $0
                            }
                        }
                    }
                    
                    if isLoading {
                        Spin(color: .blue)
                    }
                    
                    if creatRoomState == .Loading {
                        ShowProgressDialog(title: Resources.updating, message: "")
                            .onDisappear() {
                                if self.creatRoomState == .Ok {
                                    callBack()
                                }
                            }
                    }
                    
                    if !loadStateLiveDataError.isEmpty {
                        Spacer()
                        Text(loadStateLiveDataError)
                           .foregroundColor(.white)
                           .font(.system(size: 15))
                           .frame(maxWidth: .infinity, alignment: .leading)
                           .padding(10)
                           .background(Color(Resources.textDark))
                           .cornerRadius(5)
                           .frame(maxHeight: .infinity, alignment: .bottom)
                           .padding(5)
                           .transition(.move(edge: .bottom))
                    }
                    
                    PopUpWindow(popupVm: editRoomVm.popupVm, show: $editRoomVm.isShowingAlert)
                }
                .transition(.move(edge: .trailing))
                .background(Color(Resources.defaultBackground))
                .onAppear() {
                    roomUserSelectionVm.loadUsers()
                    roomUserSelectionVm.onTapRow = {
                        self.endTextEditing()
                    }
                }
                .onDisappear() {
                    homeVm.roomUserSelectionVM = RoomUserSelectionVM()
                }
            }
        }
        .edgesIgnoringSafeArea(.top)
    }
}
