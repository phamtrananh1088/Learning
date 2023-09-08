//
//  SheetListView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 02/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct SheetListView: View {
    @Binding var isPresented: Bool
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var sheetListVm: SheetListViewModel
    var onDismiss: (Bool) -> Void

    init(isPresented: Binding<Bool>,
         homeVm: HomeViewModel,
         sheetListVm: SheetListViewModel,
         onDismiss: @escaping (Bool) -> Void
    ) {
        self._isPresented = isPresented
        self.homeVm = homeVm
        self.sheetListVm = sheetListVm
        self.onDismiss = onDismiss
        UITableView.appearance().separatorStyle = .none
        UITableView.appearance().separatorColor = .clear
        // To remove only extra separators below the list:
        UITableView.appearance().tableFooterView = UIView()
    }
    @State private var itemHeight: CGFloat = .zero
    @State private var offset: CGFloat = .zero
    private var contentHeight: CGFloat {
        let total = sheetListVm.listItem.count
        if total == 0 {
            return 40
        }
        
        let height = CGFloat(sheetListVm.listItem.count * 55)
        if height >= 200 {
            return 200
        }
        
        return CGFloat(sheetListVm.listItem.count * 55)
    }
    
    var body: some View {
        VStack(spacing: 0) {
            // Create Button
            Button(action: {
                DispatchQueue.main.async {
                    homeVm.navigationStack.push(SheetItemEditView(vm: SheetItemEditViewModel(target: EditTarget.Add(binDetail: sheetListVm.bin)))
                                                .onAppear(perform: { onDismiss(false) } )
                                                .onDisappear(perform:  { onDismiss(true) })
                    )
                }

            }, label: {
                Text(Resources.incidental_sheet_new_create)
            }).buttonStyle(PrimaryButtonStyle()).padding(8)
            
            // Incidental_create
            Text(Resources.incidental_created)
                .bold()
                .foregroundColor(.white)
                .font(.system(size: 14))
                .padding(.vertical, 4)
                .padding(.horizontal, 8)
                .frame(maxWidth: .infinity, alignment: .leading)
                .background(Color(Resources.colorPrimary))
                
            // List
            if sheetListVm.listItem.count > 0 {
                List {
                    ForEach(Array(zip(sheetListVm.listItem.indices, sheetListVm.listItem)), id: \.1) { (idx, sheetItem) in
                        let item = sheetListVm.listItem[idx]
                        SheetListItemView(sheetListItemVm: item)
                        .onTapGesture(perform: {
                            homeVm.navigationStack.push(SheetItemView(vm: SheetItemViewModel(header: item.incidentalHeader))
                                                            .onAppear(perform: { onDismiss(false) })
                                                            .onDisappear(perform: { onDismiss(true) }))
                        })
                        .onSwipe(action: {
                            sheetListVm.removeIncidental(item: item.incidentalListItem)
                        })
                        .listRowBackground(Color.white)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .listRowInsets(EdgeInsets())
                        .background(Color.white)
                    }
                }
                .listStyle(PlainListStyle())
                .frame(maxWidth: .infinity, maxHeight: contentHeight)
                .background(Color.white)
            } else {
                Text(Resources.unregistered_placeholder).foregroundColor(.black)
                    .listRowBackground(Color.white)
                    .listRowInsets(EdgeInsets(top: 0, leading: 0, bottom: 0, trailing: 0))
                    .frame(maxWidth: .infinity, maxHeight: contentHeight)
            }
        }
        .frame(maxWidth: .infinity)
        .background(Color.white)
        .frame(maxHeight: .infinity, alignment: .bottom)
        .onAppear(perform: {
            URLSession.shared.getAllTasks { lstTask in
                if !lstTask.contains(
                    where: {
                        $0.originalRequest?.url?.path.contains(Endpoint.incidentalData.path()) ?? false
                        || $0.originalRequest?.url?.path.contains(Endpoint.postIncidental.path()) ?? false }) {
                    Current.Shared.syncIncidental() {(isOk, Error) in }
                }
            }
        })
    }
}

//struct SheetListView_Previews: PreviewProvider {
//    static var previews: some View {
//        SheetListView(isPresented: .constant(true))
//    }
//}
