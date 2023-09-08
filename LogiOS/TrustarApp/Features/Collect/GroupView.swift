//
//  GroupView.swift
//  TrustarApp
//
//  Created by CuongNguyen on 03/03/2022.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct GroupView: View {
    var groupVm: GroupViewModel
    @EnvironmentObject private var navigationStack: NavigationStack
    
    var body: some View {
        ZStack {
            VStack {
                HStack {
                    // Title
                    Text(groupVm.group.collectionClassName.orEmpty())
                        .foregroundColor(Color(Resources.textDark))
                        .font(.system(size: 16))
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    Button(action: {
                        groupVm.callBack(groupVm.group)
                    }, label: {
                        Image(systemName: "plus")
                            .foregroundColor(.black)
                    })
                }
                .frame(maxWidth: .infinity)
                
                HStack {
                    HStack {
                        Text("")
                            .foregroundColor(.clear)
                            .frame(maxWidth: .infinity, alignment: .leading)
                        HStack {
                            Text(Resources.expected_quantity)
                                .foregroundColor(.black)
                                .bold()
                                .font(.system(size: 16))
                                .frame(width: 70)
                        }
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                    
                    HStack(spacing: 0) {
                        Text("").foregroundColor(.clear)
                            .frame(width: 40)
                        Text(Resources.actual_quantity).foregroundColor(.black)
                            .font(.system(size: 16))
                            .bold()
                            .frame(width: 70)
                        Text("").foregroundColor(.clear)
                            .frame(width: 40)
                    }
                }
            }
        }
        .padding()
        .background(Color.white)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

//struct GroupView_Previews: PreviewProvider {
//    static var previews: some View {
//        GroupView()
//    }
//}
