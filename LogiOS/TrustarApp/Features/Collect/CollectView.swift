//
//  CollectView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine

struct CollectView: View {
    @ObservedObject var collectVm: CollectViewModel
    @EnvironmentObject private var navigationStack: NavigationStack
    @EnvironmentObject var modelRotation: ModelRotation
    @State private var isShowDialog = false
    @State var dialogVm: DialogViewModel?
    @State var groupSelected: Int?
    @State var itemSelected: Int?
    @State var heightScrollView: CGFloat?
    
    private var list: [(CollectionGroup, [CollectViewModel.Row])] {
        return collectVm.collectionsLiveData
    }
    
    var body: some View {
        ZStack {
            ZStack {
                VStack(spacing: 0) {
                    HomeHeaderView(isShowContent: false)
                    
                    Text(collectVm.title)
                        .foregroundColor(.black)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding()
                    
                    Divider()
                        .background(Color.gray)
                    GeometryReader { geo in
                        ScrollView {
                            if list.count > 0 {
                                ForEach((0...list.count - 1), id: \.self) { idx in
                                    let group = list[idx].0
                                    let items = list[idx].1
                                    // GroupUI
                                    GroupView(groupVm: GroupViewModel(group: group) { group in
                                        groupSelected = idx
                                        itemSelected = nil
                                        dialogVm = DialogViewModel()
                                        isShowDialog = true
                                    })
                                    
                                    // Item
                                    if items.count > 0 {
                                        ForEach((0...items.count - 1), id: \.self) { idxItem in
                                            let item = items[idxItem]
                                            // Item
                                            ItemCollectView(
                                                itemCollectVm: ItemCollectViewModel(
                                                    item,
                                                    onChangeValue: { [self] value in
                                                        var rows = list[idx].1
                                                        let updatedItem  = rows[idxItem]
                                                        updatedItem.result.actualQuantity = Double(value) ?? 0.0
                                                        rows[idxItem] = updatedItem
                                                        collectVm.collectionsLiveData[idx].1 = rows
                                                    },
                                                    onEditMode: {
                                                        if item.result.collectionCd == nil {
                                                            groupSelected = idx
                                                            itemSelected = idxItem
                                                            let input = list[groupSelected!].1[itemSelected!].result.collectionNm
                                                            dialogVm = DialogViewModel(input: input.orEmpty())
                                                            isShowDialog = true
                                                        }
                                                    }))
                                        }
                                    }
                                    
                                    if idx != list.count - 1 {
                                        Divider().background(Color.gray)
                                    }
                                }
                            }
                        }
                        .onAppear {
                            self.heightScrollView = geo.size.height
                        }
                        .if(modelRotation.landscape) {
                            $0.overlay(
                                GeometryReader { geo in
                                    Color.clear.onAppear {
                                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                            self.heightScrollView = geo.size.height - 100
                                        }
                                    }
                                })
                        }
                    }
                    
                    Spacer()
                    Divider().background(Color.gray)
                }
                .padding(.bottom, 70)
                
                VStack (spacing: 0) {
                    Spacer()
                    Color.clear.frame(height: self.heightScrollView)
                    HStack {
                        Button(action: {
                            self.navigationStack.pop()
                        }, label: {
                            Text(Resources.cancel)
                        }).buttonStyle(MaterialButtonUnelevatedStyle())
                        
                        Button(action: {
                            self.endTextEditing()
                            collectVm.save()
                            self.navigationStack.pop()
                        }, label: {
                            Text(Resources.update)
                        }).buttonStyle(MaterialButtonUnelevatedStyle())
                    }
                    .frame(maxWidth: .infinity, alignment: .trailing)
                    .padding()
                    .background(Color.white)
                }
            }
            
            if isShowDialog {
                if dialogVm != nil {
                    DialogView(isPresented: $isShowDialog, dialogVm: dialogVm!, okClick: { value in
                        // add
                        if itemSelected == nil {
                            collectVm.addRow(name: value, groupIdx: groupSelected!)
                        } else {
                            // edit
                            collectVm.editRow(name: value, groupIdx: groupSelected!, itemIdx: itemSelected!)
                        }
                    })
                }
            }
            
            if collectVm.isLoading {
                Spin(color: .blue)
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
                    .background(Color.black.opacity(0.5))
            }
        }
        .edgesIgnoringSafeArea(.top)
        .background(Color.white)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .onTapGesture {
            self.endTextEditing()
        }
    }
    
    class DialogViewModel: ObservableObject {
        @Published var input: String
        init() {
            self.input = String()
        }
        
        init(input: String) {
            self.input = input
        }
    }
    
    struct DialogView: View {
        @Binding var isPresented: Bool
        @ObservedObject var dialogVm: DialogViewModel
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

                        VStack(alignment: .leading) {
                            Text(Resources.collections)
                                .foregroundColor(Color(Resources.colorPrimary))
                            
                            if #available(iOS 14.0, *) {
                                TextEditorCustomView(string: $dialogVm.input,
                                                     maxLength: 20)
                                    .foregroundColor(.black)
                                    .padding(10)
                            } else {
                                TSUITextField(label: "", text: $dialogVm.input,
                                              keyboardType: .default,
                                              tag:0,
                                              onCommit: {_ in },
                                              maxLength: 20,
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
                                    okClick(dialogVm.input)
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
                        .frame(maxWidth: .infinity, alignment: .leading)
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
}

//struct CollectView_Previews: PreviewProvider {
//    static var previews: some View {
//        CollectView()
//    }
//}
