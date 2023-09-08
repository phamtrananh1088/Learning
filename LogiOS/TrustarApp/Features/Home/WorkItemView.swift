//
//  WorkItemView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/17.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct WorkItemView: View {
    @ObservedObject var workItemVm: WorkItemViewModel
    var rowIdx: Int = 0
    var iconClick: (Int) -> ()
    var rowClick: (Int) -> ()
    
    var body: some View {
        HStack {
            VStack (spacing: 0) {
                HStack(spacing: 2) {
                    // nameAndTime
                    Text(workItemVm.nameAndTime)
                        .foregroundColor(.black)
                        .font(.system(size: 15))
                        .fontWeight(.bold)
                        .fixedSize(horizontal: false, vertical: true)
                        .frame(maxWidth: .infinity, alignment: .leading)

                    // workL
                    Text(workItemVm.workL)
                        .foregroundColor(.white)
                        .font(.system(size: 12))
                        .padding(2)
                        .frame(maxWidth: .infinity, alignment: .trailing)
                        .background(Color(workItemVm.workLBgColor))
                        .fixedSize()
                        .if(!workItemVm.workLShow) {
                            $0.hidden()
                        }
                    
                    // workStatus
                    Text(workItemVm.workStatusText)
                        .foregroundColor(Color(workItemVm.workStatusColor))
                        .font(.system(size: 12))
                        .padding(.vertical, 2)
                        .frame(maxWidth: 60, alignment: .center)
                        .background(Color(workItemVm.workStatusBgColor))
                    
                    // wrong
                    Text(workItemVm.wrong)
                        .padding(2)
                        .foregroundColor(.red)
                        .font(.system(size: 10))
                        .frame(maxWidth: 50, alignment: .leading)
                        .if(!workItemVm.wrongShow) {
                            $0.hidden()
                        }
                }
                
                HStack {
                    // target
                    Text(workItemVm.target)
                        .font(.system(size: 17))
                        .foregroundColor(.black)
                        .fontWeight(.bold)
                        .multilineTextAlignment(.leading)
                        .lineLimit(nil)
                        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    
                    // clickInfo
                    Image(systemName: "info.circle.fill")
                        .foregroundColor(Color(workItemVm.clickInfoColor))
                        .font(.system(size: 22))
                        .onTapGesture {
                            iconClick(rowIdx)
                        }
                        .fixedSize()
                }
                
                // address
                Text(workItemVm.address)
                    .font(.system(size: 16))
                    .foregroundColor(.black)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.vertical, 5)
            }
            .frame(maxWidth: .infinity)
        }
        .padding(8)
        .background(Color(workItemVm.bgColor))
        .onTapGesture {
            rowClick(rowIdx)
        }
    }
}

//struct WorkItemView_Previews: PreviewProvider {
//    static var previews: some View {
//        WorkItemView()
//    }
//}
