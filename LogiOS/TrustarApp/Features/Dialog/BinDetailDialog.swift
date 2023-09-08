//
//  BinDetailDialog.swift
//  TrustarApp
//
//  Created by CuongNguyen on 01/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct BinDetailDialog: View {
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var binDetailDialogVm: BinDetailDialogModel
    var onDismiss: (Bool) -> Void
    
    @EnvironmentObject private var navigationStack: NavigationStack
    @State private var isShowTemperature = false
    @State private var isShowMemo = false
    @State private var isGroupView = false
    @State private var isShowDeliveryChart = false
    @State private var isShowDelayReasonChange = false
    @State private var showToast = false
    
    var body: some View {
        ZStack {
            ScrollView {
                VStack {
                    VStack{
                        // binDeliveryChart
                        ItemView(title: {
                            Text(Resources.delivery_chart)
                            .foregroundColor(.black)},
                                 content: {
                            Button(action: {
                                deliveryChartClick()
                            }, label: {
                                HStack {
                                    Text("").frame(maxWidth: .infinity)
                                    Image(systemName: "chevron.right")
                                        .foregroundColor(Color(Resources.colorPrimary))
                                }
                            })
                        })
                    }

                    VStack {
                        // binDelayReason
                        ItemView(title: {
                            Text(Resources.bin_untimely_delivered_reason)
                            .foregroundColor(.black)},
                                 content: {
                            HStack {
                                Text(binDetailDialogVm.binDelayReason)
                                    .foregroundColor(.black)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                Button(action: {
                                    binDetailDialogVm.binDelayReasonChangeVm = BinDelayReasonChangeViewModel(delayReasons: binDetailDialogVm.binLiveData.delayReasons, reasonSelected: binDetailDialogVm.binLiveData.delayReason)
                                    isShowDelayReasonChange = true
                                }, label: {
                                    Text(Resources.bin_untimely_delivered_reason_change)
                                })
                                .buttonStyle(PrimaryButtonSmallStyle())
                                .disabled(binDetailDialogVm.disableBinDelayReasonChange) }})
                        
                        // binServiceOrder
                        ItemView(title: {
                            Text(Resources.bin_service_order)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binServiceOrder)
                            .foregroundColor(.black)})
                        
                        // bin_operation_order
                        ItemView(title: {
                            Text(Resources.bin_operation_order)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binOperationOrder)
                            .foregroundColor(.black)})
                        
                        // bin_place_name
                        ItemView(title: {
                            Text(Resources.bin_place_name)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binPlaceName)
                                .fixedSize(horizontal: false, vertical: true)
                            .foregroundColor(.black)})
                        
                        // bin_plan_time
                        ItemView(title: {
                            Text(Resources.bin_plan_time)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binPlanTime)
                            .foregroundColor(.black)})
                        
                        // bin_actual_time
                        ItemView(title: {
                            Text(Resources.bin_actual_time)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binActualTime)
                            .foregroundColor(.black)})
                        
                        // bin_work_name
                        ItemView(title: {
                            Text(Resources.bin_work_name)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binWorkName)
                            .foregroundColor(.black)})
                        
                        // bin_zip_code
                        ItemView(title: {
                            Text(Resources.bin_zip_code)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binZipCode)
                            .foregroundColor(.black)})
                        
                        // bin_place_address
                        ItemView(title: {
                            Text(Resources.bin_place_address)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binPlaceAddress)
                                .fixedSize(horizontal: false, vertical: true)
                                .foregroundColor(Color(Resources.colorPrimary))
                                .multilineTextAlignment(.leading)
                                .onTapGesture {
                                    openMap(location: binDetailDialogVm.binPlaceAddress)
                                }
                                .onLongPressGesture {
                                    let pasteboard = UIPasteboard.general
                                    pasteboard.string = binDetailDialogVm.binPlaceAddress
                                    self.showToast = true
                                }
                        })
                        // bin_place_tel1
                        ItemView(title: {
                            HStack {
                                Text(Resources.bin_place_tel1)
                            }
                            .foregroundColor(.black)},
                                 content: {
                            Button(action: {
                                makeCallPhone(phoneNumber: binDetailDialogVm.binPlaceTel1)
                            }) {
                                Text(binDetailDialogVm.binPlaceTel1)
                                .foregroundColor(Color(Resources.colorPrimary)) }})
                    }
                    
                    VStack {
                        // bin_place_mail1
                        ItemView(title: {
                            Text(Resources.bin_place_mail1)
                            .foregroundColor(.black)},
                                 content: {
                            Button(action: {
                                makeSendMail(emailAddress: binDetailDialogVm.binPlaceMail1)
                            }) {
                                Text(binDetailDialogVm.binPlaceMail1)
                                .foregroundColor(Color(Resources.colorPrimary)) }})
                        
                        
                        // bin_place_tel2
                        ItemView(title: {
                            Text(Resources.bin_place_tel2)
                            .foregroundColor(.black)},
                                 content: {
                            Button(action: {
                                makeCallPhone(phoneNumber: binDetailDialogVm.binPlaceTel2)
                            }) {
                                Text(binDetailDialogVm.binPlaceTel2)
                                .foregroundColor(Color(Resources.colorPrimary)) }})
                        
                        // bin_place_mail2
                        ItemView(title: {
                            Text(Resources.bin_place_mail2)
                            .foregroundColor(.black)},
                                 content: {
                            Button(action: {
                                makeSendMail(emailAddress: binDetailDialogVm.binPlaceMail2)
                            }) {
                                Text(binDetailDialogVm.binPlaceMail2)
                                .foregroundColor(Color(Resources.colorPrimary)) }})
                        
                        // bin_place_note1
                        ItemView(title: {
                            Text(Resources.bin_place_note1)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binPlaceNote1)
                                .foregroundColor(.black)
                                .multilineTextAlignment(.leading)
                        })
                        
                        // bin_place_note2
                        ItemView(title: {
                            Text(Resources.bin_place_note2)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binPlaceNote2)
                                .foregroundColor(.black)
                                .multilineTextAlignment(.leading)
                        })
                        
                        // bin_place_note3
                        ItemView(title: {
                            Text(Resources.bin_place_note3)
                            .foregroundColor(.black)},
                                 content: {
                            Text(binDetailDialogVm.binPlaceNote3)
                                .foregroundColor(.black)
                                .multilineTextAlignment(.leading)
                        })
                        
                        // bin_incidental
                        if binDetailDialogVm.binIncidentalGroup {
                            ItemView(title: {
                                Text(Resources.bin_incidental)
                                .foregroundColor(.black)},
                                     content: {
                                Button(action: {
                                    showSheetList()
                                }, label: {
                                    HStack {
                                        Text(binDetailDialogVm.binIncidental)
                                            .foregroundColor(.black)
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                        
                                        Image(systemName: "chevron.right")
                                            .foregroundColor(Color(Resources.colorPrimary))
                                    }})})
                        }
                        
                        // bin_signature
                        if binDetailDialogVm.binSignGroup {
                            ItemView(title: {
                                Text(Resources.bin_signature)
                                .foregroundColor(.black)},
                                     content: {
                                Text(binDetailDialogVm.binSignature)
                                .foregroundColor(.black)})
                        }
                        
                        // bin_temperature
                        ItemView(title: {
                            Text(Resources.bin_temperature)
                            .foregroundColor(.black)},
                                 content: {
                            Button(action: {
                                if let temp = binDetailDialogVm.binLiveData.binDetail.temperature {
                                    binDetailDialogVm.temperatureModel = EditModel(input: "\(String(format: "%.1f", Float(temp)!))")
                                } else {
                                    binDetailDialogVm.temperatureModel = EditModel(input: nil)
                                }
                                
                                isShowTemperature = true
                            }, label: {
                                HStack {
                                    Text(binDetailDialogVm.binTemperature)
                                        .foregroundColor(.black)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                    
                                    Image(systemName: "chevron.right")
                                        .foregroundColor(Color(Resources.colorPrimary))
                                }
                            })
                        })
                        
                        // bin_memo
                        ItemView(title: {
                            Text(Resources.bin_memo)
                            .foregroundColor(.black)},
                                 content: {
                            Button(action: {
                                if let temp = binDetailDialogVm.binLiveData.binDetail.experiencePlaceNote1 {
                                    binDetailDialogVm.expeiencePlaceNote1Model = EditModel(input: String(temp))
                                } else {
                                    binDetailDialogVm.expeiencePlaceNote1Model = EditModel(input: nil)
                                }
                                
                                isShowMemo = true
                            }, label: {
                                HStack {
                                    Text(binDetailDialogVm.binMemo)
                                        .lineLimit(nil)
                                        .foregroundColor(.black)
                                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                                        .multilineTextAlignment(.leading)
                                    
                                    Image(systemName: "chevron.right")
                                        .foregroundColor(Color(Resources.colorPrimary))
                                }
                            })
                        })
                    }
                    
                    VStack {
                        // collections_edit
                        ItemView(title: {
                            Text(Resources.collections_edit)
                            .foregroundColor(.black)},
                                 content: {
                            
                            Button(action: {
                                DispatchQueue.main.async {
                                    binDetailDialogVm.collectVm.setBinDetail(binDetail: binDetailDialogVm.binDetailRx)
                                    
                                    self.navigationStack.push(CollectView(collectVm: binDetailDialogVm.collectVm)
                                        .onAppear(perform: { onDismiss(false) } )
                                        .onDisappear(perform:  { onDismiss(true) })
                                    )
                                }
                            }, label: {
                                HStack {
                                    Text(binDetailDialogVm.binCollect)
                                        .foregroundColor(.black)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                    
                                    Image(systemName: "chevron.right")
                                        .foregroundColor(Color(Resources.colorPrimary))
                                }
                            })
                        },
                                 borderLess: true)
                        .padding(.bottom, 5)
                    }
                }
                .padding(8)
            }
            
            EditTemperature(editTemperatureVm: binDetailDialogVm.temperatureModel, isPresented: $isShowTemperature, okClick: { value in
                binDetailDialogVm.setTemperature(temperature: value)
            }).animation(.default)
            
            EditExperiencePlaceNote1(editVm: binDetailDialogVm.expeiencePlaceNote1Model, isPresented: $isShowMemo, okClick: { value in
                binDetailDialogVm.setMemo(memo: value)
            }).animation(.default)
            
            BinDelayReasonChangeView(isPresented: $isShowDelayReasonChange,
                                     binDelayReasonChangeVm: binDetailDialogVm.binDelayReasonChangeVm,
                                     onClickItem: { delayPicked in
                binDetailDialogVm.reasonChange(delayReason: delayPicked)
            })
            
            PopUpWindow(popupVm: binDetailDialogVm.popupVm, show: $binDetailDialogVm.isShowingAlert)
        }
        .frame(maxWidth: .infinity)
        .popup(isPresented: $binDetailDialogVm.isShowSheetList, type:.toast, position: .bottom, dragToDismiss: false, closeOnTap: false, closeOnTapOutside: true, backgroundColor: Color.black.opacity(0.5)) {
            SheetListView(isPresented: $binDetailDialogVm.isShowSheetList,
                          homeVm: homeVm,
                          sheetListVm: homeVm.sheetListVm,
                          onDismiss: { isShow in
                binDetailDialogVm.isShowSheetList = isShow
                onDismiss(isShow)
            })
        }.sheet(isPresented: $isShowDeliveryChart, content: {
            BinDeliveryChartView(isPresented: $isShowDeliveryChart,
                                 vm: binDetailDialogVm.binDeliveryChartVm,
                                 onDissmiss: { isShow in
                isShowDeliveryChart = isShow
            })
        })
        .onTapGesture {
            self.endTextEditing()
            showSheetList(false)
            isShowTemperature = false
        }
        .background(Color.white)
        .toast(message: Resources.copy_clipboard,
               isShowing: $showToast,
               nonStop: false)
        .onAppear()
    }
    
    private func deliveryChartClick() {
        DispatchQueue.main.async {
            binDetailDialogVm.binDeliveryChartVm.setBinDetail(binDetail: binDetailDialogVm.binDetailRx)
            isShowDeliveryChart = true
        }
    }
    
    private func openMap(location: String) {
        if location.isEmpty {
            return
        }
        
        if let url = URL(string: "maps://?q=\(location.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)") {
            UIApplication.shared.open(url)
        }
    }
    
    private func makeCallPhone(phoneNumber: String) {
        let telephone = "tel://"
        let formattedString = telephone + phoneNumber
        guard let url = URL(string: formattedString) else { return }
        UIApplication.shared.open(url)
    }
    
    private func makeSendMail(emailAddress: String) {
        if let url = URL(string: "mailto:\(emailAddress)") {
            UIApplication.shared.open(url)
        }
    }
    
    private func showSheetList(_ isShow: Bool = true) {
        if isShow {
            homeVm.sheetListVm.setBinDetail(binDetail: binDetailDialogVm.binDetailRx)
            binDetailDialogVm.isShowSheetList = isShow
        } else {
            binDetailDialogVm.isShowSheetList = isShow
        }
    }
}

struct ItemView<Title: View, Content: View>: View {
    @ViewBuilder var title: Title
    @ViewBuilder var content: Content
    var borderLess: Bool = false
    
    var body: some View {
        HStack (spacing: 0) {
            // title:
            title
                .frame(maxWidth: 110 ,alignment: .leading)
                .font(.system(size: 15))
            
            // content
            content
                .frame(maxWidth: .infinity, alignment: .leading)
                .font(.system(size: 15))
        }
        
        if !borderLess {
            Divider().background(Color.black)
        }
    }
}

struct EditTemperature: View {
    @ObservedObject var editTemperatureVm: EditModel
    @Binding var isPresented: Bool
    var okClick: (String) -> ()
    
    var body: some View {
        GeometryReader { gp in
            ZStack {
                if isPresented {
                    // PopUp background color
                    Color.black.opacity(isPresented ? 0.5 : 0).edgesIgnoringSafeArea(.all)
                        .onTapGesture {
                            isPresented = false
                        }
                    
                    VStack {
                        Text(Resources.bin_temperature)
                            .foregroundColor(Color(Resources.colorPrimary))
                            .font(.system(size: 18, weight: .bold))
                            .frame(maxWidth: .infinity, alignment: .leading)
                        
                        TSUITextField(label: "", text: $editTemperatureVm.input,
                                      focusable: true,
                                      keyboardType: .numbersAndPunctuation,
                                      tag:0,
                                      onCommit: {_ in },
                                      regexExp: "^\\-{0,1}[0-9]{0,3}(\\.[0-9]{0,1})?$",
                                      textAlignment: .left)
                        .foregroundColor(.black)
                        .frame(height: 20)
                        .padding(10)
                        .overlay(
                            RoundedRectangle(cornerRadius: 2)
                                .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
                        )
                        
                        HStack {
                            Button(action: {
                                isPresented = false
                            }, label: {
                                Text(Resources.cancel)
                                    .foregroundColor(Color(Resources.colorPrimary))
                            })
                            
                            Button(action: {
                                isPresented = false
                                okClick(editTemperatureVm.input)
                            }, label: {
                                Text(Resources.confirm)
                                    .foregroundColor(Color(Resources.colorPrimary))
                            })
                            .padding(.leading)
                            .padding(.trailing)
                            .foregroundColor(.blue)
                        }
                        .padding(.top)
                        .frame(maxWidth: .infinity, alignment: .trailing)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity)
                    .background(Color.white)
                    .padding()
                }
            }
        }.onTapGesture {
            // Dismiss the PopUp
            withAnimation(.linear(duration: 0.3)) {
                self.endTextEditing()
            }
        }
    }
}

struct EditExperiencePlaceNote1: View {
    @ObservedObject var editVm: EditModel
    @Binding var isPresented: Bool
    var okClick: (String) -> ()
    
    var body: some View {
        GeometryReader { gp in
            ZStack {
                if isPresented {
                    // PopUp background color
                    Color.black.opacity(isPresented ? 0.5 : 0).edgesIgnoringSafeArea(.all)
                        .onTapGesture {
                            isPresented = false
                        }
                    
                    VStack {
                        Text(Resources.bin_memo)
                            .foregroundColor(Color(Resources.colorPrimary))
                            .font(.system(size: 18, weight: .bold))
                            .frame(maxWidth: .infinity, alignment: .leading)
                        
                        if #available(iOS 14.0, *) {
                            TextEditorCustomView(string: $editVm.input,
                                                 maxLength: 100)
                            .foregroundColor(.black)
                            .padding(10)
                        } else {
                            TSUITextField(label: "", text: $editVm.input,
                                          keyboardType: .default,
                                          tag:0,
                                          onCommit: {_ in },
                                          maxLength: 100,
                                          textAlignment: .left)
                            .foregroundColor(.black)
                            .frame(height: 20)
                            .padding(10)
                            .overlay(
                                RoundedRectangle(cornerRadius: 2)
                                    .stroke(Color(UIColor(rgb: 0xffcccccc)), lineWidth:  1)
                            )
                        }
                        
                        HStack {
                            Button(action: {
                                isPresented = false
                            }, label: {
                                Text(Resources.cancel)
                                    .foregroundColor(Color(Resources.colorPrimary))
                            })
                            
                            Button(action: {
                                isPresented = false
                                okClick(editVm.input)
                            }, label: {
                                Text(Resources.confirm)
                                    .foregroundColor(Color(Resources.colorPrimary))
                            })
                            .padding(.leading)
                            .padding(.trailing)
                            .foregroundColor(.blue)
                            .if(editVm.input.count > 100) {
                                $0.disabled(true)
                            }
                            .if(editVm.input.count <= 100) {
                                $0.disabled(false)
                            }
                        }
                        .padding(.top)
                        .frame(maxWidth: .infinity, alignment: .trailing)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity)
                    .background(Color.white)
                    .padding()
                }
            }
        }.onTapGesture {
            // Dismiss the PopUp
            withAnimation(.linear(duration: 0.3)) {
                //self.endTextEditing()
            }
        }
    }
}

struct BinDelayReasonChangeView: View {
    @Binding var isPresented: Bool
    var binDelayReasonChangeVm: BinDelayReasonChangeViewModel
    var onClickItem: (DelayReason) -> Void
    
    var body: some View {
        ZStack {
            if isPresented {
                Color.black.opacity(isPresented ? 0.5 : 0).edgesIgnoringSafeArea(.all)
            }
        }
        .onTapGesture {
            withAnimation(.linear(duration: 0.3)) {
                isPresented = false
            }
        }.overlay(
            VStack {
                VStack(alignment: .leading, spacing: 2) {
                    if binDelayReasonChangeVm.delayReasons.count > 0 {
                        ForEach((0...binDelayReasonChangeVm.delayReasons.count - 1), id: \.self) { idx in
                            let item = binDelayReasonChangeVm.delayReasons[idx]
                            Button(action: {
                                onClickItem(item)
                                isPresented = false
                            }, label: {
                                VStack {
                                    Text(item.reasonText).foregroundColor(.black)
                                        .padding(.vertical, 10)
                                        .padding(.horizontal, 20)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                }.frame(maxWidth: .infinity)
                            })
                        }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.vertical, 32)
                .background(Color(Resources.colorAccent))
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

//struct BinDetailDialog_Previews: PreviewProvider {
//    static var previews: some View {
//        BinDetailDialog()
//    }
//}
