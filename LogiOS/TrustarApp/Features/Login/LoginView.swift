//
//  LoginView.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/12.
//

import SwiftUI

struct LoginView: View {
    @ObservedObject var loginVm = LoginViewModel()
    @EnvironmentObject var modelRotation: ModelRotation
    
    @State private var companyId: String = ""
    @State private var userId: String = ""
    @State private var password: String = ""
    @State private var deviceId: String = ""
    @State private var isShowPassword: Bool = false
    
    private var isShowLoading: Bool {
        return loginVm.loginProcess == .preFlg
        || loginVm.loginProcess == .loginFlg
        || loginVm.loginProcess == .syncFlg
        || loginVm.loginProcess == .finishFLg
    }
    
    private var isShowMissingInput: Bool {
        return loginVm.isMissingInput
    }
    
    var LoginForm: some View{
        VStack {
            VStack {
                Text(Resources.app_name)
                    .foregroundColor(.white)
                    .font(.system(size: 26))
                    .fontWeight(.semibold)
                    .padding(.top, 70)

                VStack (alignment: .center) {
                    // 企業ID
                    VStack (alignment: .leading) {
                        FloatingLabelTextField($companyId, placeholder: Resources.company_id, editingChanged: {(isChanged) in
                        }).isShowError(true)
                            .lineColor(setLineColor(isShowError: isShowMissingInput && companyId.isEmpty))
                            .selectedLineColor(setLineColor(isShowError: isShowMissingInput && companyId.isEmpty))
                            .selectedLineHeight(2)
                            .textColor(.white)
                            .titleFont(.system(size: 15))
                            .titleColor(.white)
                            .selectedTextColor(.white)
                            .selectedTitleColor(.white)
                            .placeholderColor(Color(Resources.textHint))
                            .rightView({ // Add right view.
                                if isShowMissingInput && companyId.isEmpty {
                                    Image(systemName: "exclamationmark.circle.fill")
                                        .foregroundColor(Color(Resources.accentOrange))
                                }
                            })
                            .accentColor(Color(Resources.colorAccent))
                            .onReceive(companyId.publisher.collect()) {
                                    self.companyId = String($0.prefix(10))
                                }

                        if isShowMissingInput && companyId.isEmpty {
                            Text(Resources.company_id + Resources.s1_field_required)
                                .font(.system(size: 12))
                                .foregroundColor(Color(Resources.accentOrange))
                        }
                    }
                    .if(modelRotation.landscape) {
                        $0.frame(width: 400, height: 70)
                        
                    }
                    .if(!modelRotation.landscape) {
                        $0.frame(maxHeight: 75)
                    }
                    
                    // ユーザーID
                    VStack (alignment: .leading) {
                        FloatingLabelTextField($userId, placeholder: Resources.user_id, editingChanged: {(isChanged) in
                        }).isShowError(true)
                            .lineColor(setLineColor(isShowError: isShowMissingInput && userId.isEmpty))
                            .selectedLineColor(setLineColor(isShowError: isShowMissingInput && userId.isEmpty))
                            .selectedLineHeight(2)
                            .textColor(.white)
                            .titleFont(.system(size: 15))
                            .titleColor(.white)
                            .selectedTextColor(.white)
                            .selectedTitleColor(.white)
                            .placeholderColor(Color(Resources.textHint))
                            .rightView({ // Add right view.
                                if isShowMissingInput && userId.isEmpty {
                                    Image(systemName: "exclamationmark.circle.fill")
                                        .foregroundColor(Color(Resources.accentOrange))
                                }
                            })
                            .accentColor(Color(Resources.colorAccent))
                            .onReceive(userId.publisher.collect()) {
                                    self.userId = String($0.prefix(10))
                                }

                        if isShowMissingInput && userId.isEmpty {
                            Text(Resources.user_id + Resources.s1_field_required)
                                .font(.system(size: 12))
                                .foregroundColor(Color(Resources.accentOrange))
                        }
                    }
                    .if(modelRotation.landscape) {
                        $0.frame(width: 400, height: 70)
                        
                    }
                    .if(!modelRotation.landscape) {
                        $0.frame(maxHeight: 75)
                    }

                    // パスワード
                    VStack (alignment: .leading) {
                        FloatingLabelTextField($password, placeholder: Resources.password, editingChanged: { (isChanged) in
                                }) {
                                    
                                }
                            .isSecureTextEntry(!self.isShowPassword)
                                        .rightView({ // Add right view.
                                            if isShowMissingInput && password.isEmpty {
                                                Image(systemName: "exclamationmark.circle.fill")
                                                    .foregroundColor(Color(Resources.accentOrange))
                                            } else {
                                                Button(action: {
                                                    withAnimation {
                                                        self.isShowPassword.toggle()
                                                    }
                                                }) {
                                                    if self.isShowPassword {
                                                        Image(systemName: "eye.slash.fill").foregroundColor(.gray)
                                                    } else {
                                                        Image(systemName: "eye.fill").foregroundColor(.gray)
                                                    }
                                                }
                                            }
                                        })
                            .isShowError(true)
                            .lineColor(setLineColor(isShowError: isShowMissingInput && password.isEmpty))
                            .selectedLineColor(setLineColor(isShowError: isShowMissingInput && password.isEmpty))
                            .selectedLineHeight(2)
                            .textColor(.white)
                            .titleFont(.system(size: 15))
                            .titleColor(.white)
                            .selectedTextColor(.white)
                            .selectedTitleColor(.white)
                            .placeholderColor(Color(Resources.textHint))
                            .accentColor(Color(Resources.colorAccent))
                            .onReceive(password.publisher.collect()) {
                                    self.password = String($0.prefix(256))
                                }
                        
                        if isShowMissingInput && password.isEmpty {
                            Text(Resources.password + Resources.s1_field_required)
                                .font(.system(size: 12))
                                .foregroundColor(Color(Resources.accentOrange))
                        }
                    }
                    .if(modelRotation.landscape) {
                        $0.frame(width: 400, height: 70)
                        
                    }
                    .if(!modelRotation.landscape) {
                        $0.frame(maxHeight: 75)
                    }
                    
                }.padding(.horizontal, 30)
                    .padding(.bottom, 30)
                
                HStack {
                    if isShowLoading {
                        Spin()
                    } else {
                        Button(Resources.login) {
                            self.endTextEditing()
                            loginVm.preLoginAction(companyId: companyId, userId: userId, password: password)
                        }
                        .buttonStyle(WhiteButtonStyle())
                    }
                }.frame(height: 35)
                
                Text(Resources.login_footer_info)
                    .font(.system(size: 16))
                    .foregroundColor(.white)
                    .multilineTextAlignment(.center)
                    .frame(alignment: .center)
                    .padding(8)
                    .padding(.top, 20)

                Spacer()

                VStack (alignment: .leading) {
                    Text(Resources.terminal_id_s1 + deviceId)
                        .font(.system(size: 13))
                        .foregroundColor(.white)
                        .padding(.bottom, 30)
                        .frame(maxWidth: .infinity)
                }.frame(maxWidth:.infinity, alignment: .leading)
                
            }
        }
        .onAppear {
            Current.Shared.stopInterval()
        }
    }
    
    var body: some View {
        ZStack {
            if modelRotation.landscape {
                ScrollView {
                    LoginForm
                }
            } else {
                LoginForm
            }
            
            PopUpWindow(popupVm: loginVm.popupVm, show: $loginVm.isShowingAlert)
        }
        .frame(maxWidth:.infinity, alignment: .center)
        .background(LinearGradient(gradient: Gradient(colors: [Color(Resources.colorPrimary), Color(Resources.colorPrimaryDark)]), startPoint: .top, endPoint: .bottom))
        .edgesIgnoringSafeArea(.all)
        .onTapGesture {
            self.endTextEditing()
        }
        .popup(isPresented: $loginVm.isShowError, type: .toast, position: .bottom, animation: .easeInOut, autohideIn: 2, closeOnTap: true, closeOnTapOutside: true, view: {
             VStack {
                 Text(loginVm.errorMsg)
                    .foregroundColor(.white)
                    .font(.system(size: 15))
                    .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .leading)
                    .padding(.leading, 10)
                    .background(Color(Resources.textDark))
                    .cornerRadius(5)
                    .padding(EdgeInsets(top: 0, leading: 5, bottom: 5, trailing: 5))
            }
             .frame(maxWidth: .infinity, maxHeight: 50)
        })
    }
    
    private func setLineColor(isShowError: Bool) -> Color {
        switch isShowError {
        case true:
            return Color(Resources.accentOrange)
        case false:
            return Color(Resources.colorAccent)
        }
    }
    
    init()
    {
        if let companyId: String = Helper.Shared.readValue(key: .companyId) {
            _companyId = State(initialValue: companyId)
        }
        
        _deviceId = State(initialValue: Config.Shared.clientInfo.terminalId)
    }
}

//struct LoginView_Previews: PreviewProvider {
//    static var previews: some View {
//        LoginView()
//    }
//}
