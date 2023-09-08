//
//  OperationView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 04/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct OperationView: View {
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var operationVm: OperationViewModel
    
    @State var style = ModularGridStyle(.vertical, columns: 2, rows: .fixed(50), spacing: 15)
    
    var body: some View {
        ZStack {
            VStack (spacing: 0) {
                // Region: Header
                HomeHeaderView()
                
                ScrollView(style.axes) {
                    VStack(spacing: 0) {
                        // placeGroup
                        HStack {
                            VStack (alignment: .leading) {
                                // place1
                                if operationVm.place1Show {
                                    Text(operationVm.place1)
                                        .foregroundColor(.black)
                                        .font(.system(size: 16, weight: .bold))
                                        .padding(.vertical, 4)
                                        .if (!operationVm.placeGroup) {
                                            $0.padding(15)
                                                .padding(.bottom, 100)
                                        }
                                }
                                
                                if operationVm.placeGroup {
                                    // place2
                                    Text(operationVm.place2)
                                        .foregroundColor(.black)
                                        .font(.system(size: 16, weight: .bold))
                                        .padding(.vertical, 4)
                                    // address
                                    Text(operationVm.address)
                                        .foregroundColor(.black)
                                        .font(.system(size: 12))
                                        .padding(.vertical, 4)
                                }
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                            
                            if operationVm.placeGroup {
                                // clickInfo
                                Image(systemName: "info.circle.fill")
                                    .foregroundColor(operationVm.clickInfoColor)
                                    .font(.system(size: 24))
                                    .onTapGesture {
                                        if let b = operationVm.binDetail {
                                            homeVm.binDetailDialogVm = BinDetailDialogModel(binDetail: b)
                                            operationVm.isShowBinDetailDialog = true
                                        }
                                    }
                            }
                        }
                        .if (operationVm.placeGroup) {
                            $0.padding(16)
                        }
                        
                        // workingGroup
                        if operationVm.workingGroup {
                            HStack {
                                // workStatus
                                Text(operationVm.workStatus)
                                    .foregroundColor(Color(Resources.accentOrange))
                                    .font(.system(size: 40))
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .frame(maxHeight: .infinity, alignment: .center)
                                
                                // timer
                                CircleTimer(basedSystemTimeMillis: operationVm.workStartTime!,
                                            colorLine: operationVm.delayRankColor,
                                            belowText: operationVm.workStateText)
                            }
                            .padding()
                        }
                        
                        HStack {
                            // timeGroup
                            if operationVm.timeGroup {
                                VStack(alignment: .leading) {
                                    //
                                    Text(operationVm.timeRange)
                                        .foregroundColor(.black)
                                        .font(.system(size: 16))
                                    Text(Resources.work_notice_of_time)
                                        .foregroundColor(Color(Resources.accentOrange))
                                        .font(.system(size: 12))
                                }
                            }
                            
                            VStack {
                                if !operationVm.placeGroup && operationVm.binDetail != nil {
                                    // clickInfo
                                    Image(systemName: "info.circle.fill")
                                        .foregroundColor(Color(Resources.colorPrimary))
                                        .font(.system(size: 30))
                                        .frame(maxWidth: .infinity, alignment: .trailing)
                                        .onTapGesture {
                                            if let b = operationVm.binDetail {
                                                homeVm.binDetailDialogVm = BinDetailDialogModel(binDetail: b)
                                                operationVm.isShowBinDetailDialog = true
                                            }
                                        }
                                }
                                // nimachi
                                if operationVm.nimachiShow {
                                    Button(action: {
                                        if let b = operationVm.binDetail {
                                            homeVm.sheetListVm.setBinDetail(binDetail: b)
                                            withAnimation {
                                                homeVm.isShowSheetList = true
                                            }
                                        }
                                    }, label: {
                                        Text(Resources.incidental_sheets)
                                    })
                                    .buttonStyle(PrimaryButtonStyle())
                                    .padding(.vertical, 6)
                                    .frame(maxWidth: .infinity, alignment: .trailing)
                                }
                            }
                        }
                        .padding(EdgeInsets(top: 0, leading: 14, bottom: 10, trailing: 14))
                        
                        VStack(spacing: 15) {
                            if operationVm.lstFlex.count > 0 {
                                if let matchedItem = operationVm.lstFlex.first(where: { $0.matched }) {
                                    Button(action: {
                                        matchedItem.callback!(matchedItem.work)
                                    }, label: {
                                        Text(matchedItem.text)
                                            .font(.system(size: 24))
                                            .fontWeight(.bold)
                                            .foregroundColor(.white)
                                            .frame(maxWidth: .infinity)
                                            .frame(height: 100)
                                            .background(matchedItem.background)
                                            .cornerRadius(10)
                                    })
                                        .disabled(matchedItem.isDisable)
                                }
                                
                                Grid(0...operationVm.lstFlex.count - 1, id: \.self) { idx in
                                    let item = operationVm.lstFlex[idx]
                                    if !item.matched {
                                        Button(action: {
                                            item.callback!(item.work)
                                        }, label: {
                                            Text(item.text)
                                                .font(.system(size: 16))
                                                .fontWeight(.bold)
                                                .foregroundColor(.white)
                                                .frame(maxWidth: .infinity, maxHeight: .infinity)
                                                .background(item.background)
                                                .cornerRadius(10)
                                        })
                                            .disabled(item.isDisable)
                                    }
                                }.gridStyle(self.style)
                            }
                        }
                        .padding(15)
                    }
                    .frame(maxWidth: .infinity)
                }
            }
        }
        .edgesIgnoringSafeArea(.top)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.white)
        .sheet(isPresented: $operationVm.isShowBinDetailDialog,
               onDismiss: nil,
               content: { BinDetailDialog(homeVm: homeVm,
                                          binDetailDialogVm: homeVm.binDetailDialogVm,
                                          onDismiss: { isShow in
                   operationVm.isShowBinDetailDialog = isShow
               })}
        )
        .onAppear(perform: {
            homeVm.operationVm.getData()
        })
    }
}

//struct OperationView_Previews: PreviewProvider {
//    static var previews: some View {
//        OperationView()
//    }
//}
