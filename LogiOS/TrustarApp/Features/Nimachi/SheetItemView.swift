//
//  SheetItemView.swift
//  TrustarApp
//
//  Created by DION COMPANY on 2022/02/18.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

/*待機・附帯作業明細*/
struct SheetItemView: View {
    private let nimachiListHeight:CGFloat = 30
    private let additionalListHeight:CGFloat = 30
    private let maxHeightSign: CGFloat = 120
    private let bigFontSize: CGFloat = 24
    
    @EnvironmentObject private var navigationStack: NavigationStack
    @ObservedObject var vm: SheetItemViewModel
    
    @State private var shipperName: String = Resources.strEmpty
    @State private var joinedWorkName: String = Resources.strEmpty
    @State private var workTimes: [TimeItem] = []
    @State private var additionalTimes: [TimeItem] = []
    //move to sign view
    @State private var isSelectedSignView = false
    
    //Get number of worktimes
    private var workTimesCnt: Int {
        let cnt:Int = workTimes.count
        return cnt < 1 ? 1: cnt
    }
    
    //Get number of addtionaltimes
    private var additionalTimesCnt: Int {
        let cnt:Int = additionalTimes.count
        return cnt < 1 ? 1: cnt
    }
    
    init(vm: SheetItemViewModel) {
        //pass viewmodel from parent view
        self.vm = vm

        // To remove all separators including the actual ones:
        UITableView.appearance().separatorStyle = .none
        UITableView.appearance().separatorColor = .clear
        //UITableView.appearance().isScrollEnabled = false
        // To remove only extra separators below the list:
        UITableView.appearance().tableFooterView = UIView()
    }
    
    var body: some View {
        ZStack{
            VStack {
                HomeHeaderView(isShowContent: false)
                ScrollView (.vertical) {
                    VStack {
                            /*荷主*/
                            HStack {
                                Text(shipperName)
                                    .foregroundColor(Color(Resources.colorPrimary))
                                    .font(.system(size: 18))
                                Spacer()
                                Button(action: {
                                    DispatchQueue.main.async {
                                        self.navigationStack.push(
                                            SheetItemEditView(vm: SheetItemEditViewModel(
                                                target: EditTarget.Sheet(
                                                    uuid: vm.getHeaderUUID()
                                                )
                                            ))
                                        )
                                    }
                                }, label: {
                                    Text(Resources.edit)
                                        .padding(.horizontal)
                                })
                                .buttonStyle(PrimaryButtonStyle())
                            }
                            .padding(.horizontal)
                            
                            /*附帯作業内容*/
                            Text(Resources.incidental_sheet_addition_work_)
                                .foregroundColor(Color.black)
                                .font(.system(size: 16))
                                .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                                .frame(maxWidth: .infinity, alignment: .leading)
                            
                            Text(joinedWorkName)
                                .foregroundColor(Color.black)
                                .font(.system(size: 16))
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.horizontal)
                                .multilineTextAlignment(TextAlignment.leading)

                        VStack(alignment: .leading, spacing: 0) {
                            /*荷待作業*/
                            Text(Resources.incidental_sheet_await)
                                .foregroundColor(Color(Resources.colorAccent))
                                .font(.system(size: 16, weight: .semibold))
                                .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(Color(Resources.colorPrimary))
                            
                            if workTimes.count <= 0 {
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
                                    ForEach(workTimes.indices) { idx in
                                        HStack {
                                            Text("\(idx+1)")
                                                .fontWeight(.semibold)
                                                .frame(width:20, alignment: .leading)
                                            HStack {
                                                Text("\(workTimes[idx].beginDateString ?? "")")
                                                    .frame(width: 120, alignment: .leading)
                                                Text("〜")
                                                Text("\(workTimes[idx].endDateString ?? "")")
                                                    .frame(width: 120, alignment: .leading)
                                                    .padding(.leading, 5)
                                            }
                                            .fixedSize()
                                            .frame(maxWidth: .infinity, alignment: .center)
                                        }
                                        .minimumScaleFactor(0.1)
                                        .font(.system(size: 16))
                                        .foregroundColor(Color.black)
                                        .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
                                        .frame(height: nimachiListHeight)
                                        .padding(.horizontal)
                                        .background(Color.white)
                                    }
                                }
                                .listStyle(PlainListStyle())
                                .frame(width: .infinity, height: CGFloat(workTimesCnt) * nimachiListHeight)
                                .environment(\.defaultMinListRowHeight, nimachiListHeight) //minimum row height
                            }

                            /*附帯作業*/
                            Text(Resources.incidental_sheet_addition_work)
                                .foregroundColor(Color(Resources.colorAccent))
                                .font(.system(size: 16, weight: .semibold))
                                .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(Color(Resources.colorPrimary))
                            
                            if additionalTimes.count <= 0 {
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
                                    ForEach(additionalTimes.indices) { idx in
                                        HStack{
                                            Text("\(idx+1)")
                                                .fontWeight(.semibold)
                                                .frame(width:20, alignment: .leading)
                                            HStack {
                                                Text("\(additionalTimes[idx].beginDateString ?? "")")
                                                    .frame(width: 120, alignment: .leading)
                                                Text("〜")
                                                Text("\(additionalTimes[idx].endDateString ?? "")")
                                                    .frame(width: 120, alignment: .leading)
                                                    .padding(.leading, 5)
                                            }
                                            .fixedSize()
                                            .frame(maxWidth: .infinity, alignment: .center)
                                        }
                                        .minimumScaleFactor(0.1)
                                        .font(.system(size: 16))
                                        .foregroundColor(Color.black)
                                        .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
                                        .frame(height: additionalListHeight)
                                        .padding(.horizontal)
                                        .background(Color.white)
                                    }
                                }
                                .listStyle(PlainListStyle())
                                .frame(width: .infinity, height: CGFloat(additionalTimesCnt) * additionalListHeight)
                                .environment(\.defaultMinListRowHeight, additionalListHeight) //minimum row height
                            }
                            
                            /*署名画像*/
                            Text(Resources.incidental_sheet_signature)
                                .foregroundColor(Color(Resources.colorAccent))
                                .font(.system(size: 16, weight: .semibold))
                                .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(Color(Resources.colorPrimary))
                            PushView(destination: SignView(onSave: { path in
                                            /*署名・署名データの保存処理*/
                                            vm.commitPendingSign(signPath: path)
                                            navigationStack.pop()
                                            //self.isSelectedSignView = false
                                                                    },
                                                            onCancel: {}),
                                        isActive: $isSelectedSignView,
                                        label: {
                                            ZStack {
                                                Color.white
                                                if vm.signImagePath.isEmpty {
                                                    Text(Resources.tap_to_edit)
                                                        .foregroundColor(.gray)
                                                        .font(.system(size: bigFontSize))
                                                } else {
                                                    if vm.signImagePath != "" {
                                                        let imageUrl = URL(fileURLWithPath: vm.signImagePath)
                                                        let uiImage = UIImage(contentsOfFile: imageUrl.path)
                                                        if uiImage != nil {
                                                            let image: Image? = Image(uiImage: uiImage!)
                                                            image? .resizable()
                                                                    .scaledToFit()
                                                        } else {
                                                            Text(Resources.tap_to_edit)
                                                                .foregroundColor(.gray)
                                                                .font(.system(size: bigFontSize))
                                                        }
                                                    }
                                                }
                                            }
                                            .frame(height: maxHeightSign)
                                            .overlay(RoundedRectangle(cornerRadius: 10)
                                                        .stroke(Color.gray, lineWidth: 1))
                                            .padding()
                                            .onTapGesture {
                                                isSelectedSignView = true
                                            }
                            })
                        }
                        }
                    .padding(.top)
                }
                
                //Spacer()
                /*待機・附帯作業明細一覧に戻る押下時に起動されるイベント*/
                Button(Resources.incidental_back_sheet_list) {
                    navigationStack.pop(to: .root)
                }
                .buttonStyle(PrimaryButtonStyle())
                .padding(.bottom)
                //.frame(height: 50)
            }
            .onAppear {
                //vm.loadIncidentalItemData()
                self.shipperName = vm.itemData?.shipperNm ?? ""
                self.joinedWorkName = vm.itemData?.joinedWorkName ?? Resources.unselected
                self.workTimes = vm.itemData?.times?.nimachiTime() ?? []
                self.additionalTimes = vm.itemData?.times?.additionalTime() ?? []
            }
        }
        .edgesIgnoringSafeArea(.top)
        .background(Color.white)
    }
}

//struct SheetItemView_Previews: PreviewProvider {
//    static var previews: some View {
//        let bin: IncidentalHeader = IncidentalHeader()
//        SheetItemView(vm: SheetItemViewModel(header: bin))
//    }
//}
