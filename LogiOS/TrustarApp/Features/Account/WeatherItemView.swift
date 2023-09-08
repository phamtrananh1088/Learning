//
//  WeatherItemView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct WeatherItemView: View {
    @State var image: Image
    @State var text: String
    
    var body: some View {
        VStack {
            image
                .resizable()
                .aspectRatio(contentMode: .fit)
                .padding(12)
                .frame(width: 72, height: 72)
            
            Text(text)
                .foregroundColor(Color.black)
                .padding(.top, 8)
                .padding(.bottom, 8)
                .lineLimit(1)
                .font(.system(size: 11))
        }
    }
}

struct WeatherItemView_Previews: PreviewProvider {
    static var previews: some View {
        WeatherItemView(image: WeatherEnum.Hare.getWeather(), text: WeatherEnum.Hare.getTextWeather())
    }
}
