//
//  BinHeaderInfoDialogView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 14/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct BinHeaderInfoDialogView: View {
    @Binding var isPresented: Bool
    @ObservedObject var binHeaderInfoDialogVm: BinHeaderInfoDialogViewModel
    @State var isShowTextInput: Bool = false
    
    let fmt: NumberFormatter = {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.maximumIntegerDigits = 7
        formatter.maximumFractionDigits = 0
        return formatter
    }()
    
    private func formmatterString(value: String) -> String {
        var valueReturn = Resources.strEmpty
        if let it = Int(value) {
            let number = NSNumber(value: it)
            if let formatterdValue = fmt.string(from: number) {
                valueReturn = formatterdValue + " km"
            }
        }
        
        return valueReturn
    }
    
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
        }
        .overlay(
            VStack {
                VStack (spacing: 10) {
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_vehicle).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.vehicle).foregroundColor(.black)
                    })
                    
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_start_scheduled_time).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.startScheduled).foregroundColor(.black)
                    })
                    
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_end_scheduled_time).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.endScheduled).foregroundColor(.black)
                    })
                    
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_start_time).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.start).foregroundColor(.black)
                    })
                    
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_end_time).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.end).foregroundColor(.black)
                    })
                    
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_crew).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.crew).foregroundColor(.black)
                    })
                    
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_crew2).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.crew2).foregroundColor(.black)
                    })
                    
                    ItemViewBinHeaderInfor(title: {
                        Text(Resources.bin_note_1).foregroundColor(.black)
                    }, content: {
                        Text(binHeaderInfoDialogVm.note).foregroundColor(.black)
                    })
                    
                    if binHeaderInfoDialogVm.outgoingMeterGroup {
                        ItemViewBinHeaderInfor(title: {
                            Text(Resources.bin_outgoing_meter).foregroundColor(.black)
                        }, content: {
                            ZStack {
                                Text("\(formmatterString(value: binHeaderInfoDialogVm.outgoing))").foregroundColor(.black)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                TextField(Resources.strEmpty, text: $binHeaderInfoDialogVm.outgoing, onEditingChanged: { editingChanged in
                                    if !editingChanged {
                                        binHeaderInfoDialogVm.setOutgoing()
                                    }
                                })
                                    .foregroundColor(.clear)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .keyboardType(.numberPad)
                                    .disabled(!binHeaderInfoDialogVm.inputEnable)
                            }
                        })
                    }
                    
                    if binHeaderInfoDialogVm.incomingMeterGroup {
                        ItemViewBinHeaderInfor(title: {
                            Text(Resources.bin_incoming_meter).foregroundColor(.black)
                        }, content: {
                            ZStack {
                                Text("\(formmatterString(value: binHeaderInfoDialogVm.incoming))").foregroundColor(.black)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                TextField(Resources.strEmpty, text: $binHeaderInfoDialogVm.incoming, onEditingChanged: { editingChanged in
                                    if !editingChanged {
                                        binHeaderInfoDialogVm.setIncoming()
                                    }
                                })
                                    .foregroundColor(.clear)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .keyboardType(.numberPad)
                                    .disabled(!binHeaderInfoDialogVm.inputEnable)
                            }
                        })
                    }
                }
                .padding(EdgeInsets(top: 8, leading: 8, bottom: 0, trailing: 8))
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .onTapGesture {
                    self.endTextEditing()
                }
            }, alignment: .bottom)
            .frame(maxHeight: .infinity, alignment: .bottom)
            .adaptsToKeyboard()
            .edgesIgnoringSafeArea(.all)
            .offset(y: self.isPresented ? 0 : UIScreen.main.bounds.height)
            .animation(.default)
    }
}

struct ItemViewBinHeaderInfor<Title: View, Content: View>: View {
    @ViewBuilder var title: Title
    @ViewBuilder var content: Content

    var body: some View {
        HStack (spacing: 0) {
            // title:
            title
                .font(.system(size: 15))
                .frame(maxWidth: 170 ,alignment: .leading)

            // content
            content
                .font(.system(size: 15))
                .frame(maxWidth: .infinity, alignment: .leading)
        }

        Divider().background(Color.black)
    }
}
//struct BinHeaderInfoDialogView_Previews: PreviewProvider {
//    static var previews: some View {
//        BinHeaderInfoDialogView()
//    }
//}
