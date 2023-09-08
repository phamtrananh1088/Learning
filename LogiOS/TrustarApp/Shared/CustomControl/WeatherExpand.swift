//
//  WeatherExpand.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/02/09.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import SwiftUI

struct WeatherExpand: View {
    
    @Binding var isRotated: Bool
    @Binding var weather: WeatherEnum
    
    var body: some View {
        HStack {
            HStack {
                weather.getWeather()
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .padding(10)
            }
            .frame(width: 65, height: 65, alignment: .center)
            .background(Color.white)
                .clipShape(Circle())
                .onTapGesture {
                    withAnimation(.linear(duration: 0.3)) {
                        self.isRotated.toggle()
                    }
                }

            Image(systemName: "chevron.down")
                .foregroundColor(.white)
                .rotationEffect(Angle.degrees(isRotated ? 180 : 0))
                .animation(Animation.easeOut)
                .onTapGesture {
                    withAnimation(.linear(duration: 0.3)) {
                        self.isRotated.toggle()
                    }
                }
        }
    }
}

struct WeatherExpand_Previews: PreviewProvider {
    static var previews: some View {
        WeatherExpand(isRotated: .constant(false), weather: .constant(WeatherEnum.Hare))
    }
}
