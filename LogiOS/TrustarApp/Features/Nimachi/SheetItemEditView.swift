//
//  SheetItemEditView.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 20/02/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

/*待機・附帯作業詳細・編集*/
struct SheetItemEditView: View {
    // Row height for List
    private let nimachiListHeight:CGFloat = 30
    private let aditionalListHeight:CGFloat = 30
    
    //1: nimachitime button click, 2: nimachitime row click
    //3: addtionaltime button click, 4: additionaltime row click
    @State private var clickedFlg: Int = 0
    //trakcing selected row index
    @State private var clickedRowIdx: Int = 0
    
    //datetimepicker
    @State var beginDateTime: Date = Date()
    @State var endDateTime: Date = Date()
    @State private var showDatetimeDialog = false
    @State private var invalidTimeRange = false
    
    //viewmodel for manipulating data
    @ObservedObject var vm: SheetItemEditViewModel
    @EnvironmentObject private var navigationStack: NavigationStack
    
    //Get number of nimachiTimes for set height of list
    private var nimachiTimesCnt: Int {
        let cnt:Int = vm.nimachiTimes.count
        return cnt < 1 ? 1: cnt;
    }
    //Get number of addtionaltimes for set height of list
    private var additionalTimesCnt: Int {
        let cnt:Int = vm.additionalTimes.count
        return cnt < 1 ? 1: cnt;
    }
    
    init(vm: SheetItemEditViewModel) {
        self.vm = vm
        // To remove all separators including the actual ones:
        UITableView.appearance().separatorStyle = .none
        UITableView.appearance().separatorColor = .clear
        // To remove only extra separators below the list:
        UITableView.appearance().tableFooterView = UIView()
        //UITableView.appearance().backgroundColor = .clear
    }
    
    var body: some View {
        ZStack {
            VStack {
                HomeHeaderView(isShowContent: false)
                
                ScrollView (.vertical) {
                    VStack {
                            /*荷主変更*/
                            HStack {
                                Button(action: {
                                    navigationStack.pop()
                                }, label: {
                                    Image(systemName: "chevron.left")
                                        .foregroundColor(Color(Resources.colorPrimary))
                                        .font(.system(size: 18, weight: .bold))
                                        .padding(.horizontal, 10)
                                })
                                
                                Text(vm.shipperName)
                                    .foregroundColor(Color(Resources.colorPrimary))
                                    .font(.system(size: 18))
                                Spacer()
                                Button {
                                    DispatchQueue.main.async {
                                        self.navigationStack.push(
                                            SelectShipperFromListView(shipperVm: SelectShipperFromListViewModel(currentSelect: vm.shipperName, chooseItemClick: { it in
                                                        vm.shipper = it
                                                                 }
                                                )
                                            )
                                        )
                                    }
                                } label: {
                                    Text(Resources.change_shipper)
                                }
                                .buttonStyle(PrimaryButtonStyle())
                            }
                            .padding(.trailing)
                            
                            /*附帯作業内容*/
                            Text(Resources.incidental_sheet_addition_work_)
                                .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .foregroundColor(Color.black)
                                .font(.system(size: 16))
                            
                            Text(vm.joinedWorkName == "" ? Resources.unselected : vm.joinedWorkName)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.horizontal)
                                .multilineTextAlignment(TextAlignment.leading)
                                .foregroundColor(Color.black)
                                .font(.system(size: 16))
                            
                            /*作業を選択*/
                            HStack {
                                Spacer()
                                Button(Resources.select_work) {
                                    DispatchQueue.main.async {
                                        self.navigationStack.push(
                                            SheetWorkSelectionView(
                                                vm: WorkSelectViewModel(incidentalWork: vm.works,
                                                                        saveCallback: { incidentalWorkItems in
                                                                                vm.works = incidentalWorkItems
                                                                      })
                                            )
                                        )
                                    }
                                }
                                .buttonStyle(PrimaryButtonStyle())
                                
                            }.padding(.horizontal)
                        
                            VStack(alignment: .leading, spacing: 0) {
                                /*荷待待機*/
                                Text(Resources.incidental_sheet_await)
                                    .foregroundColor(Color(Resources.colorAccent))
                                    .font(.system(size: 16, weight: .semibold))
                                    .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .background(Color(Resources.colorPrimary))
                                
                                if vm.nimachiTimes.count <= 0 {
                                    HStack {
                                        Spacer()
                                        Text(Resources.unregistered_placeholder)
                                            .foregroundColor(Color.black)
                                            .font(.system(size: 16))
                                        Spacer()
                                    }
                                    .foregroundColor(Color.black)
                                    .listRowBackground(Color.white)
                                } else {
                                    List {
                                        ForEach(Array(zip(vm.nimachiTimes.indices, vm.nimachiTimes)), id: \.1) { (idx, time) in
                                            HStack{
                                                Text("\(idx+1)")
                                                    .fontWeight(.semibold)
                                                    .frame(width:20, alignment: .leading)
                                                HStack {
                                                    Text("\(vm.nimachiTimes[idx].beginDateString ?? "")")
                                                        .frame(width: 120, alignment: .leading)
                                                    Text("〜")
                                                    Text("\(vm.nimachiTimes[idx].endDateString ?? "")")
                                                        .frame(width: 120, alignment: .leading)
                                                        .padding(.leading, 5)
                                                }
                                                .fixedSize()
                                                .frame(maxWidth: .infinity, alignment: .center)
                                            }
                                            .minimumScaleFactor(0.1)
                                            .font(.system(size: 16))
                                            .foregroundColor(Color.black)
                                            .onTapGesture {
                                                clickedFlg = 2
                                                showDatetimeDialog = true
                                                clickedRowIdx = idx
                                                beginDateTime = Date(miliseconds: vm.nimachiTimes[idx].begin?.date ?? 0)
                                                endDateTime = Date(miliseconds: vm.nimachiTimes[idx].end?.date ?? 0)
                                            }
                                            .onSwipe(action: {
                                                removeNimachiItem(idx: idx)
                                            })
                                            .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
                                            .frame(height: nimachiListHeight)
                                            .background(vm.nimachiTimes[idx].hasChanged() ?
                                                        Color(UIColor(rgb: vm.nimachiTimes[idx].color)) : Color.white)
                                        }
                                    }
                                    .listStyle(PlainListStyle())
                                    .frame(width: .infinity, height: CGFloat(nimachiTimesCnt) * nimachiListHeight)
                                    .background(Color.white)
                                    .environment(\.defaultMinListRowHeight, nimachiListHeight) //minimum row height
                                    .environment(\.locale, Config.Shared.locale)
                                }
                                
                                Button {
                                    /*待機・附帯作業時間設定・追加を押下時に起動されるイベント*/
                                    clickedFlg = 1
                                    showDatetimeDialog = true
                                    
                                    beginDateTime = Date()
                                    endDateTime = beginDateTime
                                } label: {
                                    Image(systemName: "plus.circle.fill")
                                        .font(.system(size: 24))
                                }.frame(height: 40)
                                    .padding(.horizontal)
                                
                                /*附帯作業*/
                                Text(Resources.incidental_sheet_addition_work)
                                    .foregroundColor(Color(Resources.colorAccent))
                                    .font(.system(size: 16, weight: .semibold))
                                    .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .background(Color(Resources.colorPrimary))
                                
                                if vm.additionalTimes.count <= 0 {
                                    HStack {
                                        Spacer()
                                        Text(Resources.unregistered_placeholder)
                                            .foregroundColor(Color.black)
                                            .font(.system(size: 16))
                                        Spacer()
                                    }
                                    .listRowBackground(Color.white)
                                } else {
                                    List {
                                        ForEach(Array(zip(vm.additionalTimes.indices, vm.additionalTimes)), id: \.1) { (idx, time) in
                                            HStack{
                                                Text("\(idx+1)")
                                                    .fontWeight(.semibold)
                                                    .frame(width:20, alignment: .leading)
                                                HStack {
                                                    Text("\(vm.additionalTimes[idx].beginDateString ?? "")")
                                                        .frame(width: 120, alignment: .leading)
                                                    Text("〜")
                                                    Text("\(vm.additionalTimes[idx].endDateString ?? "")")
                                                        .frame(width: 120, alignment: .leading)
                                                        .padding(.leading, 5)
                                                }
                                                .fixedSize()
                                                .frame(maxWidth: .infinity, alignment: .center)
                                            }
                                            .minimumScaleFactor(0.1)
                                            .font(.system(size: 16))
                                            .foregroundColor(Color.black)
                                            .onTapGesture {
                                                /*待機・附帯作業時間設定・追加を押下時に起動されるイベント*/
                                                clickedFlg = 4
                                                showDatetimeDialog = true
                                                clickedRowIdx = idx
                                                beginDateTime = Date(miliseconds: vm.additionalTimes[idx].begin?.date ?? 0)
                                                endDateTime = Date(miliseconds: vm.additionalTimes[idx].end?.date ?? 0)
                                            }
                                            .onSwipe(action: {
                                                removeAdditionalItem(idx: idx)
                                            })
                                            .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
                                            .frame(height: aditionalListHeight)
                                            .background(vm.additionalTimes[idx].hasChanged() ?
                                                        Color(UIColor(rgb: vm.additionalTimes[idx].color)) : Color.white)
                                        }
                                    }
                                    .listStyle(PlainListStyle())
                                    .frame(width: .infinity, height: CGFloat(additionalTimesCnt) * aditionalListHeight)
                                    .environment(\.defaultMinListRowHeight, aditionalListHeight) //minimum row height
                                    .environment(\.locale, Config.Shared.locale)
                                }
                                
                                Button {
                                    clickedFlg = 3
                                    showDatetimeDialog = true
                                    
                                    beginDateTime = Date()
                                    endDateTime = beginDateTime
                                } label: {
                                    Image(systemName: "plus.circle.fill")
                                        .font(.system(size: 24))
                                }.frame(height: 40)
                                    .padding(.horizontal)
                            }
                        }
                    .padding(.top)
                }
                
                Spacer()
                
                Button(Resources.update_content) {
                    let header = vm.save()
                    DispatchQueue.main.async {
                        navigationStack.push(SheetItemView(vm: SheetItemViewModel(header: header)))
                    }
                }
                .buttonStyle(PrimaryButtonStyle())
                .padding(.bottom)
                .disabled(vm.shipperName == "")
            }
            .showCustomDialog(isShowing: $showDatetimeDialog) {
                /*待機・附帯作業時間設定・データ更新処理*/
                DoubleDateAndTimeView(beginDateTime: $beginDateTime,
                                      endDateTime: $endDateTime,
                                      callbackAction: self.updateShowDialog)
            }
            PopUpWindow(popupVm: PopupViewModel(isShowTitle: false,
                                                title: Resources.strEmpty,
                                                message: Resources.invalid_time_range,
                                                isShowLeftBtn: false,
                                                leftBtnText: Resources.strEmpty,
                                                isShowRightBtn: true,
                                                rightBtnText: Resources.ok,
                                                leftBtnClick: nil,
                                                rightBtnClick: nil),
                        show: $invalidTimeRange)
        }
        .edgesIgnoringSafeArea(.top)
        .background(Color.white)
        .onAppear {
        }
    }
    
    //MARK: 時間入力・確定ボタン押下時に起動されるイベント
    func updateShowDialog() -> Void {
        if beginDateTime >= endDateTime {
            invalidTimeRange.toggle()
            return
        }
        showDatetimeDialog.toggle()
        
        //Convert selected datetime to miliseconds
        let beginDateAsNum = beginDateTime.milisecondsSince1970
        let endDateAsNum = endDateTime.milisecondsSince1970
        
        //Add or update selected datetime
        if clickedFlg == 1 {
            /*待機・附帯作業時間設定・データ追加処理*/
            let addedTime = TimeItem(begin: beginDateAsNum, end: endDateAsNum, type: 0)
            vm.nimachiTimes.append(vm.addTime(time: addedTime))
            
        } else if clickedFlg == 3 {
            /*待機・附帯作業時間設定・データ追加処理*/
            let addedTime = TimeItem(begin: beginDateAsNum, end: endDateAsNum, type: 1)
            vm.additionalTimes.append(vm.addTime(time: addedTime))
            
        } else if clickedFlg == 2 {
            //edit
            let editedTime = vm.nimachiTimes[clickedRowIdx]
            vm.editTime(time: editedTime, begin: beginDateAsNum, end: endDateAsNum)
            
            //reset to trigger view
            vm.nimachiTimes[clickedRowIdx] = editedTime
            //TimeItem(begin: beginDateAsNum, end: endDateAsNum, type: 0)
        } else if clickedFlg == 4 {
            //edit
            let editedTime = vm.additionalTimes[clickedRowIdx]
            vm.editTime(time: editedTime, begin: beginDateAsNum, end: endDateAsNum)
            //reset to trigger view
            vm.additionalTimes[clickedRowIdx] = editedTime
            //TimeItem(begin: beginDateAsNum, end: endDateAsNum, type: 1)
        }
    }
    
    //MARK: 待機・附帯作業時間設定・データ削除処理
    func deleteNimachiTime(at offsets: IndexSet) {
        vm.nimachiTimes.remove(atOffsets: offsets)
    }
    
    func deleteAdditionalTime(at offsets: IndexSet) {
        vm.additionalTimes.remove(atOffsets: offsets)
    }
    
    func removeNimachiItem(idx: Int) {
        let delItem = vm.nimachiTimes[idx]
        if delItem.justChanged {
            // delete added item then remove
            vm.nimachiTimes.remove(at: idx)
        } else {
            // delete existing item then mark del flag
            vm.deleteTime(time: delItem)
            vm.nimachiTimes.remove(at: idx)
            //delItem.markDeleted = true
        }
    }
    
    func removeAdditionalItem(idx: Int) {
        let delItem = vm.additionalTimes[idx]
        if delItem.justChanged {
            // delete added item then remove
            vm.additionalTimes.remove(at: idx)
        } else {
            // delete existing item then mark del flag
            vm.deleteTime(time: delItem)
            vm.additionalTimes.remove(at: idx)
            //delItem.markDeleted = true
        }
    }
}


//struct SheetItemEditView_Previews: PreviewProvider {
//    static var previews: some View {
//        SheetItemEditView(vm: SheetItemEditViewModel())
//            .preferredColorScheme(.dark)
//    }
//}
