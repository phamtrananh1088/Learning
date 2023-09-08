//
//  WorkModeDialogView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 22/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct WorkModeDialogView: View {
    @Binding var isPresented: Bool
    @ObservedObject var workModeDialogVm: WorkModeDialogViewModel
    
    var body: some View {
        
        ZStack {
            if isPresented {
                Color.black.opacity(isPresented ? 0.5 : 0).edgesIgnoringSafeArea(.all)
            }
        }
        .onAppear(perform: {
            UITableView.appearance().separatorColor = .clear
        })
        .onTapGesture {
            withAnimation(.linear(duration: 0.3)) {
                isPresented = false
            }
        }.overlay(
            VStack {
                VStack {
                    Text(Resources.work_mode_select_title)
                        .font(.system(size: 17))
                        .foregroundColor(.black)
                        .padding(16)
                    
                    if workModeDialogVm.isShowWarning {
                        Text(Resources.work_mode_select_msg)
                            .font(.system(size: 17))
                            .foregroundColor(.red)
                            .padding(.horizontal, 16)
                            .padding(.vertical, 4)
                            .multilineTextAlignment(.center)
                    }
                    
                    HStack {
                        ForEach(0..<workModeDialogVm.workMode.count, id: \.self) { idx in
                            let work = workModeDialogVm.workMode[idx]
                            let isSelected = work.string == workModeDialogVm.selected?.string
                            ZStack {
                                Text(work.string)
                                    .font(.system(size: 18))
                                    .bold()
                                    .foregroundColor(isSelected ? Color.white : Color(Resources.colorPrimary))
                                    .padding(.vertical, 16)
                                    .padding(.horizontal, 32)
                            }
                            .background(isSelected ? Color(Resources.colorPrimary) : Color.white)
                            .cornerRadius(8)
                            .overlay(RoundedRectangle(cornerRadius: 8)
                                        .strokeBorder(Color(Resources.colorPrimary), lineWidth: 2))
                            .onTapGesture {
                                workModeDialogVm.selected = work
                            }
                        }
                    }
                    
                    Button(action: {
                        if let mode = workModeDialogVm.selected {
                            workModeDialogVm.start(workMode: mode, location: Current.Shared.lastLocation)
                            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now() + 1.2) {
                                Helper.Shared.sendBroadcastNotification(.Bin)
                            }
                            isPresented = false
                        }
                    }, label: {
                        HStack {
                            Text(Resources.operation_start)
                            Image(systemName: "arrow.down")
                        }
                            .font(.system(size: 14))
                    })
                        .buttonStyle(PrimaryButtonStyle())
                        .padding(.top, 32)
                        .disabled(workModeDialogVm.selected == nil)
                    
                }
                .padding(.vertical, 32)
                .frame(maxWidth: .infinity)
                .background(Color.white)
            },alignment: .bottom)
        .frame(maxHeight: .infinity, alignment: .bottom)
        .adaptsToKeyboard()
        .edgesIgnoringSafeArea(.all)
        .offset(y: self.isPresented ? 0 : UIScreen.main.bounds.height)
        .animation(.default)
    }
}

//struct WorkModeDialogView_Previews: PreviewProvider {
//    static var previews: some View {
//        WorkModeDialogView()
//    }
//}
