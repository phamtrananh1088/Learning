//
//  SelectTruckFromListView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct SelectTruckFromListView: View {
    @ObservedObject var truckVm: SelectTruckFromListViewModel
    @EnvironmentObject private var navigationStack: NavigationStack
    
    private var listDisplay: [TruckItemViewModel] {
        if !truckVm.search.isEmpty {
            return truckVm.listTruckItem.filter({ $0.code.contains(truckVm.search) || $0.name.contains(truckVm.search) })
        }
        
        return truckVm.listTruckItem
    }
    
    var body: some View {
        VStack(spacing: 0) {
            HomeHeaderView(isShowContent: false)
            
            HStack {
                Button(action: {
                    navigationStack.pop()
                }, label: {
                    Image(systemName: "chevron.left")
                        .foregroundColor(.black)
                        .font(.system(size: 18, weight: .bold))
                        .padding(.horizontal, 10)
                })
                
                Text(Resources.current_vehicle)
                    .foregroundColor(.black)
                    .font(.system(size: 16))
                
                Text(truckVm.currentSelect)
                    .foregroundColor(.black)
                    .bold()
                    .font(.system(size: 17))
                    .padding(8)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(.vertical, 4)
            .padding(.horizontal, 8)
            
            Divider().background(Color(Resources.gray_out))
            
            FloatingLabelTextField($truckVm.search,
                                   placeholder: Resources.vehicle_search_hint,
                                   editingChanged: {(isChanged) in debugPrint(isChanged)})
                .titleColor(.clear)
                .selectedTitleColor(.clear)
                .lineHeight(0)
                .selectedLineHeight(0)
                .textColor(.black)
                .selectedTextColor(.black)
                .frame(height: 35)
                .background(Color.white)
                .padding(.horizontal, 8)
                .overlay(
                    RoundedRectangle(cornerRadius: 0)
                        .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
                )
                .padding(2)
                .padding(.horizontal, 5)
            
            Divider().background(Color(Resources.gray_out))
                        
            VStack {
                Text(Resources.choose_target_vehicle)
                    .foregroundColor(.white)
                    .bold()
                    .font(.system(size: 14))
                    .padding(.vertical, 4)
                    .padding(.horizontal, 8)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color(Resources.colorPrimary))
            .padding(.top, 2)
            
            ScrollView {
                VStack(spacing: 2) {
                    if listDisplay.count > 0 {
                        ForEach((0...listDisplay.count - 1), id: \.self) { idx in
                            let item = listDisplay[idx]
                            TruckItemView(truckItemVm: item)
                                .onTapGesture {
                                    navigationStack.pop()
                                    truckVm.chooseItemClick(item.truck)
                                }
                        }
                    }
                }
            }
        }
        .edgesIgnoringSafeArea(.top)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.white)
        .onTapGesture(perform: {
            self.endTextEditing()
        })
    }
}

//struct SelectTruckFromListView_Previews: PreviewProvider {
//    static var previews: some View {
//        SelectTruckFromListView()
//    }
//}
