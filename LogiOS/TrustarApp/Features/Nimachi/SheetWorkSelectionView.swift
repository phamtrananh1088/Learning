//
//  SheetWorkSelection.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/03/06.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct MenuButton: View {
    @ObservedObject var item: WorkItem
    
    var body: some View {
        ZStack(alignment: .init(horizontal: .center, vertical: .center)) {
            Rectangle()
                .foregroundColor(item.selected ? Color(Resources.colorPrimary) : Color(Resources.colorAccent))
            Text("\(item.workNm ?? "")")
                .font(.headline)
                .foregroundColor(item.selected ? Color(Resources.colorAccent) : Color(Resources.colorPrimary))
                .padding(.horizontal)
                .multilineTextAlignment(.center)
                //.lineLimit(2)
        }
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .strokeBorder(Color(Resources.colorPrimary), lineWidth: 2)
        )
        .cornerRadius(8)
        .onTapGesture {
            /*附帯作業内容設定・複数ある作業ボタンを押下時に起動されるイベント*/
            item.toggle()
        }
    }
}

struct SheetWorkSelectionView: View {
    
    @ObservedObject var vm: WorkSelectViewModel
    @State var style = ModularGridStyle(.vertical,
                                        columns: .min(160),
                                        rows: .fixed(80),
                                        spacing: 20)
    @EnvironmentObject private var navigationStack: NavigationStack
    
    var body: some View {
        VStack {
            HomeHeaderView(isShowContent: false)
            
            /*附帯作業内容*/
            Text(Resources.incidental_sheet_works_selection)
                .foregroundColor(Color.black)
                .padding(EdgeInsets(top: 5, leading: 10, bottom: 5, trailing: 0))
                .frame(maxWidth: .infinity, alignment: .leading)
            
            ScrollView(style.axes) {
                Grid(vm.workList) { item in
                    MenuButton(item: item)
                }
                .padding(8)
            }
            .gridStyle(self.style)
            
            /*附帯作業内容設定・内容を確定する押下時に起動されるイベント*/
            Button(Resources.confirm_content) {
                navigationStack.pop()
                vm.saveIncidentalWorks()
            }
            .buttonStyle(PrimaryButtonStyle())
            .padding(.vertical, 6)
        }
        .background(Color.white)
        .edgesIgnoringSafeArea(.top)
        .onAppear {
            vm.loadIncidentalWorks()
        }
    }
    
    // from Scrumdinger sample app
    private func binding(for item: WorkItem) -> Binding<WorkItem> {
        guard let scrumIndex = vm.workList.firstIndex(where: { $0.id == item.id }) else {
            fatalError("Can't find scrum in array")
        }
        return $vm.workList[scrumIndex]
    }
}

struct SheetWorkSelection_Previews: PreviewProvider {
    static var previews: some View {
        SheetWorkSelectionView(vm: WorkSelectViewModel(incidentalWork: nil, saveCallback: { it in
            
        }))
    }
}
