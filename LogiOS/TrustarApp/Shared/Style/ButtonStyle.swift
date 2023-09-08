//
//  ButtonStyle.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/24.
//  Copyright © 2022 DionSoftware. All rights reserved.
//

import Foundation
import SwiftUI

struct WhiteButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        MyButton(configuration: configuration)
    }
    
    struct MyButton: View {
        let configuration: ButtonStyle.Configuration
        @Environment(\.isEnabled) private var isEnable: Bool
        
        var body: some View {
            configuration.label
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(Color(Resources.colorPrimary))
                .padding(.vertical, 12)
                .padding(.horizontal, 32)
                .frame(alignment: .center)
                .background(LinearGradient(gradient: Gradient(colors: [.init(white: 0x66)]), startPoint: .top, endPoint: .bottom))
                .cornerRadius(50)
                .if(!isEnable) {
                    $0.opacity(0.8)
                }
        }
    }
}

struct WhiteButtonSmallStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        MyButton(configuration: configuration)
    }
    
    struct MyButton: View {
        let configuration: ButtonStyle.Configuration
        @Environment(\.isEnabled) private var isEnable: Bool
        
        var body: some View {
            configuration.label
                .font(.system(size: 12, weight: .bold))
                .foregroundColor(Color(Resources.colorPrimary))
                .padding(.vertical, 4)
                .padding(.horizontal, 16)
                .frame(alignment: .center)
                .background(LinearGradient(gradient: Gradient(colors: [.init(white: 0x66)]), startPoint: .top, endPoint: .bottom))
                .cornerRadius(50)
                .if(!isEnable) {
                    $0.opacity(0.5)
                }
        }
    }
}

struct CapsuleButtonStyle: ButtonStyle {
    private var disabled: Bool
    private var normalColor: Color
    private var disabledColor: Color
    private var pressedColor: Color
    
    init(_ disabled: Bool = false) {
        self.normalColor = Color(Resources.colorPrimary)
        self.disabledColor =  Color(Resources.colorPrimary).opacity(0.5)
        self.pressedColor =  Color(Resources.colorPrimary).opacity(0.75)
        self.disabled = disabled
    }
    
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .font(.system(size: 16, weight: .semibold))
            .foregroundColor(Color(Resources.colorAccent))
            .padding(EdgeInsets(top: 8.0, leading: 16.0, bottom: 8.0, trailing: 16.0))
            .padding(.horizontal)
            .background(Capsule()
                            .foregroundColor( disabled ? disabledColor :
                                (configuration.isPressed ? pressedColor : normalColor)))
            .contentShape(Capsule())
    }
}

struct CapsulePickerStyle: ButtonStyle {
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .font(.system(size: 14, weight: .bold))
            .foregroundColor(Color(Resources.colorPrimary))
            .padding(EdgeInsets(top: 15.0, leading: 10.0, bottom: 15.0, trailing: 10.0))
            .padding(.horizontal)
            .background(RoundedRectangle(cornerRadius: 50, style: .continuous).fill(Color.white))
            .overlay(RoundedRectangle(cornerRadius: 50, style: .continuous).stroke(Color(Resources.colorPrimary), lineWidth: 2))
    }
}

struct RoundedRectangleButtonStyle: ButtonStyle {
    func makeBody(configuration: Self.Configuration) -> some View {
        configuration.label
            .font(.system(size: 16, weight: .semibold))
            .foregroundColor(Color(Resources.colorAccent))
            .padding(EdgeInsets(top: 5.0, leading: 10.0, bottom: 5.0, trailing: 10.0))
            .padding(.horizontal)
            .background(RoundedRectangle(cornerRadius: 5.0)
                            .foregroundColor(configuration.isPressed ? Color(Resources.colorPrimary).opacity(0.75) : Color(Resources.colorPrimary)))
    }
}

struct PrimaryButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        MyButton(configuration: configuration)
    }
    
    struct MyButton: View {
        let configuration: ButtonStyle.Configuration
        @Environment(\.isEnabled) private var isEnable: Bool
        
        var body: some View {
            configuration.label
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(Color.white)
                .padding(.vertical, 12)
                .padding(.horizontal)
                .frame(alignment: .center)
                .background(Color(Resources.colorPrimary))
                .cornerRadius(50)
                .if(!isEnable) {
                    $0.opacity(0.5)
                }
        }
    }
}

struct PrimaryButtonSmallStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        MyButton(configuration: configuration)
    }
    
    struct MyButton: View {
        let configuration: ButtonStyle.Configuration
        @Environment(\.isEnabled) private var isEnable: Bool
        
        var body: some View {
            configuration.label
                .font(.system(size: 12, weight: .bold))
                .foregroundColor(.white)
                .padding(.vertical, 4)
                .padding(.horizontal, 16)
                .frame(alignment: .center)
                .background(Color(Resources.colorPrimary))
                .cornerRadius(50)
                .if(!isEnable) {
                    $0.opacity(0.5)
                }
        }
    }
}

struct MaterialButtonUnelevatedStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        MyButton(configuration: configuration)
    }
    
    struct MyButton: View {
        let configuration: ButtonStyle.Configuration
        @Environment(\.isEnabled) private var isEnable: Bool
        
        var body: some View {
            configuration.label
                .foregroundColor(.white)
                .padding(.vertical, 5)
                .padding(.horizontal)
                .frame(alignment: .center)
                .background(Color(Resources.colorPrimary))
                .cornerRadius(5)
                .if(!isEnable) {
                    $0.opacity(0.5)
                }
        }
    }
}
