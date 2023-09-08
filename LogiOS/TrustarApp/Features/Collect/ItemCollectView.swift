//
//  ItemCollectView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 15/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI
import Combine

struct ItemCollectView: View {
    @ObservedObject var itemCollectVm: ItemCollectViewModel
    @State private var buffValue: String = String()
    
    let gray = Color(Resources.defaultBackground)
    let textColor = Color(Resources.textDark)
    
    var body: some View {
        HStack {
            HStack {
                Button(action: {
                    itemCollectVm.onEditMode()
                }, label: {
                    Text(itemCollectVm.content)
                        .foregroundColor(textColor)
                        .multilineTextAlignment(.leading)
                        .font(.system(size: 16))
                        .fixedSize(horizontal: false, vertical: true)
                        .frame(maxWidth: .infinity, alignment: .leading)
                })
                
                HStack {
                    Text(itemCollectVm.exp)
                        .foregroundColor(textColor)
                        .frame(height: 20)
                        .padding(.vertical)
                        .frame(width: 70)
                }
                .background(gray)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            
            HStack(spacing: 0) {
                Button(action: {},
                       label: {
                    Image(systemName: "minus")
                        .foregroundColor(textColor)
                        .frame(width: 40)
                        .padding(.vertical)
                        .frame(height: 20)
                }).simultaneousGesture(LongPressGesture(minimumDuration: 0.2).onEnded { _ in
                    itemCollectVm.onLongClick(action: .decrement)
                })
                .simultaneousGesture(
                    DragGesture(minimumDistance: 0)
                        .onEnded{ _ in
                            itemCollectVm.act = buffValue
                            itemCollectVm.onClick(action: .decrement)
                            itemCollectVm.onStopLongClick()
                            buffValue = itemCollectVm.act
                        }
                    )
                
                TSUITextField(label: "", text: $itemCollectVm.act,
                              isSelectAllWhenFocus: true,
                              keyboardType: .numbersAndPunctuation,
                              tag:0,
                              onCommit: { val in itemCollectVm.onChangeValue(val)},
                              onChange: { val in buffValue = val },
                              regexExp: "^[0-9]{0,5}(\\.[0-9]{0,1})?$",
                              textAlignment: .center)
                    .foregroundColor(textColor)
                    .multilineTextAlignment(.center)
                    .frame(height: 20)
                    .padding(.vertical).frame(width: 70)
                    .onReceive(Just(itemCollectVm.act), perform: {_ in
                        self.buffValue = itemCollectVm.act
                    })
              
                Button(action: {},
                       label: {
                    Image(systemName: "plus")
                        .foregroundColor(textColor)
                        .frame(width: 40)
                        .padding(.vertical)
                        .frame(height: 20)
                }).simultaneousGesture(LongPressGesture(minimumDuration: 0.2).onEnded { _ in
                    itemCollectVm.onLongClick(action: .increment)
                })
                .simultaneousGesture(
                    DragGesture(minimumDistance: 0)
                        .onEnded{ _ in
                            itemCollectVm.act = buffValue
                            itemCollectVm.onClick(action: .increment)
                            itemCollectVm.onStopLongClick()
                            buffValue = itemCollectVm.act
                        }
                    )
            }
            .background(gray)
        }
        .minimumScaleFactor(1)
        .padding(.horizontal)
        .padding(.vertical, 5)
        .onAppear(perform: {
            self.buffValue = itemCollectVm.act
        })
    }
}

//struct ItemCollectView_Previews: PreviewProvider {
//    static var previews: some View {
//        ItemCollectView(itemCollectVm: ItemCollectViewModel())
//    }
//}
