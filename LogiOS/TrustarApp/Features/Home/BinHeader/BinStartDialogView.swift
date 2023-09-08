//
//  BinStartDialogView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 10/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct BinStartDialogView<Change: View, Done: View>: View {
    @Binding var isPresented: Bool
    var binStartDialogVm: BinStartDialogViewModel
    @ViewBuilder var changeLabel: Change
    @ViewBuilder var doneLabel: Done
    @Binding var heightDialog: CGFloat
    var modeUpdateTruck = false
    private var isDisableDone: Bool {
        return binStartDialogVm.selected == nil
    }
    @EnvironmentObject private var navigationStack: NavigationStack
    
    var body: some View {
        ZStack {
            if isPresented {
                Color.black.opacity(isPresented ? 0.5 : 0).edgesIgnoringSafeArea(.all)
            }
        }
        .onTapGesture {
            self.endTextEditing()
            withAnimation(.linear(duration: 0.3)) {
                isPresented = false
            }
        }.overlay(
            VStack {
                VStack(alignment: .leading) {
                    Text(Resources.current_vehicle)
                        .foregroundColor(.black)
                        .padding(.horizontal, 16)
                    Text(binStartDialogVm.selected?.truckNm ?? String())
                        .foregroundColor(.black)
                        .padding(16)
                        .padding(.leading, 32)
                    
                    Text(Resources.operation_start_by_this_vehicle)
                        .foregroundColor(.black)
                        .padding(16)
                    
                    HStack {
                        Button(action: {
                            DispatchQueue.main.async {
                                self.navigationStack.push(
                                    SelectTruckFromListView(truckVm: SelectTruckFromListViewModel(currentSelect: binStartDialogVm.selected?.truckNm ?? String(), chooseItemClick: { truck in
                                        binStartDialogVm.selected = truck
                                        if modeUpdateTruck {
                                            binStartDialogVm.setTruck()
                                        }
                                    }))
                                )
                            }
                        }, label: {
                            changeLabel
                        }).buttonStyle(PrimaryButtonStyle())
                            .frame(maxWidth: .infinity)
                        
                        Button(action: {
                            binStartDialogVm.doneClick(binStartDialogVm.token, binStartDialogVm.selected, nil)
                        }, label: {
                            doneLabel
                        }).buttonStyle(PrimaryButtonStyle())
                            .frame(maxWidth: .infinity)
                            .disabled(isDisableDone)
                    }
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding(.top, 32)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.vertical, 32)
                .background(Color(Resources.colorAccent))
                .overlay(
                    GeometryReader { geo in
                        Color.clear.onAppear(perform: {
                            heightDialog = geo.size.height
                        })
                    }
                )
            }
                .frame(maxHeight: .infinity, alignment: .bottom)
                .adaptsToKeyboard()
                .edgesIgnoringSafeArea(.all)
                .offset(y: self.isPresented ? 0 : UIScreen.main.bounds.height)
                .animation(.default)
            ,alignment: .bottom
        )
    }
}

//struct BinStartDialogView_Previews: PreviewProvider {
//    static var previews: some View {
//        BinStartDialogView(isPresented: .constant(true), changeLabel: {}, doneLabel: {})
//    }
//}
