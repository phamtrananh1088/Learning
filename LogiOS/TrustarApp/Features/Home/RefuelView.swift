//
//  RefuelView.swift
//  TrustarApp
//
//  Created by Nguyen Nhon on 13/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine

struct RefuelView: View {
    private let MAX_WIDTH: CGFloat = 512
    /*Required HomeVM to enject show flg and view model*/
    @ObservedObject var homeVm: HomeViewModel
    @ObservedObject var vm: RefuelViewModel
    
    @ObservedObject var textValidator = TextValidator()
    
    @State private var f_liter: String = Resources.f_liter
    @State private var refuelAmount: String = ""
    @State private var paymentAmount: String = ""
    @State private var isShowingToast: Bool = false
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                // Region: Header
                HomeHeaderView()
                
                ScrollView {
                    VStack(alignment: .center, spacing: 20) {
                        /*selectedBin allocation*/
                        Button {
                            // set show
                            self.endTextEditing()
                            if let lst = vm.binHeaderList() {
                                if lst.count > 0 {
                                    withAnimation {
                                        homeVm.isShowBinBottomPicker.toggle()
                                    }
                                }
                            }
                        } label: {
                            HStack{
                                Text(vm.selectedBin != nil ? (vm.selectedBin?.header.allocationNm ?? "") : Resources.unselected)
                                    .frame(minWidth: 0, maxWidth: MAX_WIDTH, alignment: .leading)
                                Image(systemName: "chevron.down")
                            }
                        }
                        .frame(minWidth: 0, maxWidth: MAX_WIDTH, alignment: .leading)
                        .buttonStyle(CapsulePickerStyle())
                        .padding(.horizontal)
                        
                        /*selectedBin vehicle*/
                        Text(vm.selectedBin != nil ? (vm.selectedBin?.truck.truckNm ?? "") : "")
                            .foregroundColor(Color.black)
                            .font(.system(size: 16))
                            .frame(maxWidth: MAX_WIDTH, alignment: .leading)
                            .padding(.horizontal)
                        
                        /*selectedFuel*/
                        HStack {
                            Text(Resources.refuel_amount_of_the_operation)
                                .font(.system(size: 14))
                            Text(String(format: f_liter, vm.currentAmount))
                                .font(.system(size: 18, weight: .bold))
                        }
                        .foregroundColor(Color.black)
                        .frame(maxWidth: MAX_WIDTH, alignment: .leading)
                        .padding(.horizontal)
                        
                        Button {
                            // set show
                            self.endTextEditing()
                            withAnimation { homeVm.isShowFuelBottomPicker.toggle() }
                        } label: {
                            HStack{
                                Text(vm.selectedFuel != nil ? (vm.selectedFuel?.fuelNm ?? "") : Resources.unselected)
                                    .frame(minWidth: 0, maxWidth: MAX_WIDTH, alignment: .leading)
                                Image(systemName: "chevron.down")
                            }
                        }
                        .frame(minWidth: 0, maxWidth: MAX_WIDTH, alignment: .leading)
                        .buttonStyle(CapsulePickerStyle())
                        .padding(.horizontal)
                        
                        /**refuelAmount**/
                        Text(Resources.refuel_amount_in_liter)
                            .foregroundColor(Color.black)
                            .font(.system(size: 14))
                            .frame(maxWidth: MAX_WIDTH, alignment: .leading)
                            .padding(.horizontal)
                        
                        TSUITextField(label: "", text: $refuelAmount,
                                      returnKeyType: .next, keyboardType: .numbersAndPunctuation, tag:0,
                                      regexExp: "^[0-9]{0,4}(\\.[0-9]{0,1})?$")
                            .padding(12)
                            .background(Color.white)
                            .frame(maxWidth: MAX_WIDTH, alignment: .leading)
                            .padding(.horizontal)
                            .keyboardType(.decimalPad)
                            .overlay(Rectangle()
                                        .strokeBorder(Color.gray, lineWidth: 0.3)
                                        .padding(.horizontal)
                            )
                        
                        /**paymentAmount**/
                        Text(Resources.amount_include_tax)
                            .foregroundColor(Color.black)
                            .font(.system(size: 14))
                            .frame(maxWidth: MAX_WIDTH, alignment: .leading)
                            .padding(.horizontal)
                        TSUITextField(label: "", text: $paymentAmount,
                                      returnKeyType: .done, keyboardType: .numberPad, tag:1,
                                      regexExp: "^[0-9]{0,7}?$")
                            .padding(12)
                            .background(Color.white)
                            .frame(maxWidth: MAX_WIDTH, alignment: .leading)
                            .padding(.horizontal)
                            .foregroundColor(Color.black)
                            .keyboardType(.decimalPad)
                            .overlay(Rectangle()
                                        .strokeBorder(Color.gray, lineWidth: 0.3)
                                        .padding(.horizontal)
                            )
                        /*入力を確定するボタン押下イベント*/
                        Button {
                            vm.kakutei(refuelAmount: refuelAmount, paymentAmount: paymentAmount)
                            //reset amount
                            refuelAmount = ""
                            paymentAmount = ""
                            //show message
                            isShowingToast = true
                        } label: {
                            Text(Resources.confirm_input)
                                .foregroundColor(Color(Resources.colorAccent))
                        }
                        .buttonStyle(PrimaryButtonStyle())
                        .padding(.top, 32)
                        .disabled(vm.selectedBin == nil || vm.selectedFuel == nil ||
                                  refuelAmount == "" || paymentAmount == "")
                    }
                    .padding(.top)
                }
            }
        }
        .onTapGesture {
            //hide keyboard when tap on screen
            self.endTextEditing()
        }
        .edgesIgnoringSafeArea(.top)
        .background(Color(Resources.defaultBackground))
        .toast(message: Resources.refuel_success_msg,
               isShowing: $isShowingToast,
               duration: Toast.short)
    }
}

// MARK: 給油入力・便選択の押下時に起動されるイベント
struct BinBottomPicker: View {
    @Binding var isPresented: Bool
    @ObservedObject var vm: RefuelViewModel
    @State var size: CGSize = .zero
    
    var body: some View {
        ScrollView {
            VStack {
                let binHeaderList = vm.binHeaderList()!
                ForEach(binHeaderList.indices, id: \.self) { idx in
                    
                    HStack {
                        Text(binHeaderList[idx].allocationNm)
                            .font(.system(size: 20))
                            .foregroundColor(.black)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(8)
                        
                        HStack {
                            Text(binHeaderList[idx].status)
                                .font(.system(size: 12))
                                .padding(.horizontal)
                                .padding(.vertical, 2)
                                .frame(maxWidth: 70)
                                .foregroundColor(Color(UIColor(rgbText: binHeaderList[idx].textColor)))
                                .background(Color(UIColor(rgbText: binHeaderList[idx].bgColor)))
                                .padding(8)
                        }
                    }
                    .padding(5)
                    .background(Color.white)
                    .onTapGesture {
                        //Call back to set to refuelview
                        vm.saveCallbackSelectedBin(bin: binHeaderList[idx])
                        
                        withAnimation {
                            isPresented = false
                        }
                    }
                }
            }
        }
        .frame(height: 300)
        .padding(10)
        .frame(maxWidth: .infinity)
        .background(Color.white)
        .frame(maxHeight: .infinity, alignment: .bottom)
        .contentShape(Rectangle())
    }
}

// MARK: 給油入力・燃料種別選択の押下時に起動されるイベント
struct FuelBottomPicker: View {
    @Binding var isPresented: Bool
    @ObservedObject var vm: RefuelViewModel
    @State var size: CGSize = .zero
    
    var body: some View {
        VStack {
            VStack {
                let fuelList = vm.fuelList()!
                ForEach(fuelList.indices, id: \.self) { idx in
                    
                    HStack {
                        Text(fuelList[idx].fuelNm)
                            .font(.system(size: 20))
                            .padding(.vertical, 2)
                            .foregroundColor(.black)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(8)
                    }
                    .padding(5)
                    .background(Color.white)
                    .onTapGesture {
                        //Call back to set to refuelview
                        vm.saveCallbackSelectedFuel(fuel: fuelList[idx])
                        withAnimation {
                            isPresented = false
                        }
                    }
                }
            }
        }
        .padding(10)
        .frame(maxWidth: .infinity)
        .background(Color.white)
        .frame(maxHeight: .infinity, alignment: .bottom)
        .contentShape(Rectangle())
    }
}

class TextValidator: ObservableObject {
    @Published var text = ""
}

//struct RefuelView_Previews: PreviewProvider {
//    static var previews: some View {
//        RefuelView(homeVm: HomeViewModel(),
//                   vm: RefuelViewModel())
//    }
//}
