//
//  LoginViewModel.swift
//  TrustarApp
//
//  Created by DionSoftware on 2022/01/12.
//

import Foundation
import SwiftUI
import Combine

class LoginViewModel: BaseViewModel, ObservableObject {

    var loginResponse: LoginResponse = LoginResponse()
    var loginRq: LoginRequest = LoginRequest()
    var masterRs: MasterResponse? = nil
    
    @Published var isShowError: Bool = false
    var errorMsg: String = String()

    @Published var isMissingInput = false
    @Published var loginProcess: LoginFlag? {
        didSet {
            switch loginProcess! {
                
            case .preFlg:
                break
            case .loginFlg:
                loginAction()
                break
            case .syncFlg:
                syncMaster()
            case .noticeFlg:
                syncNotice()
            case .finishFLg:
                finishLogin()
            case .doneFlg:
                break
            case .cancelFlg:
                break
            }
        }
    }

    override init() {
        super.init()
        
        if Current.Shared.loggedUser != nil {
            loginRq.companyCd = (Current.Shared.loggedUser?.userInfo.companyCd)!
        }
    }
    
    override func registerNotificationCenter() {
        
    }
    
    private func handleErr(error: NetworkError) {
        switch(error) {
        case .parseError(let reason):
            isShowError = true
            errorMsg = reason.localizedDescription
        case .message(reason: let reason):
            isShowError = true
            errorMsg = reason
        case .networkError(reason: let reason):
            isShowError = true
            errorMsg = reason
        default: break
        }
        self.loginProcess = .cancelFlg
    }

    func preLoginAction(companyId: String, userId: String, password: String)
    {
        if companyId.isEmpty || userId.isEmpty || password.isEmpty {
            isMissingInput = true
            cancelLogin()
            return
        }
        
        self.loginProcess = .preFlg
        
        loginRq.companyCd = companyId
        loginRq.userId = userId
        loginRq.passWord = password

        loginRq.clientInfo = Config.Shared.clientInfo

        Config.Shared.fetch.preLogin(loginRq: loginRq, apiKey: Resources.API_KEY)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { err in
                switch(err) {
                case .finished: break
                case .failure(let error):
                    self.handleErr(error: error)
                }

            }, receiveValue: {
                res in

                self.loginResponse = res

                // Check message code
                if !self.validateMessageCode(messsageCode: self.loginResponse.loginResult.messageCode) {
                    return
                }

                if(res.loginResult.isLoggedIn)
                {
                    // Message: 既にログインされています。現在のログイン情報は破棄されますがよろしいですか？
                    self.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: Resources.strEmpty, message: Resources.login_sso_alert_msg, isShowLeftBtn: true, leftBtnText: Resources.no, isShowRightBtn: true, rightBtnText: Resources.yes, leftBtnClick: self.cancelLogin, rightBtnClick: self.loginAction))
                } else {
                    self.loginProcess = .loginFlg
                }

            }).store(in: &bag)
    }
    
    private func cancelLogin() {
        self.loginProcess = .cancelFlg
    }

    private func loginAction()
    {
        Config.Shared.fetch.login(loginRq: loginRq, apiKey: Resources.API_KEY)
            .receive(on: DispatchQueue.main)
            .sink(receiveCompletion: { err in
                switch(err) {
                case .finished: break
                case .failure(let error):
                    self.handleErr(error: error)
                }
            }, receiveValue: { res in
                self.loginResponse = res
                
                // Check message code
                if !self.validateMessageCode(messsageCode: self.loginResponse.loginResult.messageCode) {
                    return
                }

                // check userInfo.appAuthority
                if self.loginResponse.userInfo?.appAuthority == 0 {
                    self.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: Resources.strEmpty, message: Resources.login_disabled, isShowLeftBtn: false, leftBtnText: Resources.strEmpty, isShowRightBtn: true, rightBtnText: Resources.ok, leftBtnClick: self.cancelLogin, rightBtnClick: self.cancelLogin))
                    
                    return
                }

                Current.Shared.afterLogin(loginResponse: res)
                
                self.loginProcess = .syncFlg
                self.loginProcess = .noticeFlg

            }).store(in: &bag)
    }
    
    private func syncMaster() {
        try? Current.Shared.syncMaster() { (isOk, error) in
            if !isOk {
                self.cancelLogin()
            }
        }
    }
    
    private func syncNotice() {
        Current.Shared.syncNotice() { (isOk, error) in
            if !isOk {
                debugPrint(error!)
                self.cancelLogin()
            } else {
                self.loginProcess = .finishFLg
            }
        }
    }
    
    private func finishLogin() {
        let countUnreadImportant = Config.Shared.userDb?.noticeDao?.countUnreadByRank(rank: 1)
        
        if countUnreadImportant! > 0 {
            Current.Shared.changeScreenTo(screenName: .Notice)
        } else {
            Current.Shared.changeScreenTo(screenName: .Weather)
        }
    }
    
    private func weatherSetup() {
        
    }

    private func validateMessageCode(messsageCode: String) -> Bool {
        switch(messsageCode) {
        case "401.1", "401.2":
            // Title: 認証エラー
            // Message: IDまたはパスワードが間違っています。
            self.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.login_auth_err, message: Resources.login_auth_wrong_msg, isShowLeftBtn: false, leftBtnText: Resources.strEmpty, isShowRightBtn: true, rightBtnText: Resources.ok, leftBtnClick: self.cancelLogin, rightBtnClick: self.cancelLogin))
            return false

        case "401.3":
            // Title: Nil
            // Message: パスワードの有効期限が切れています。
            self.showPopupView(popupVm: PopupViewModel(isShowTitle: false, title: Resources.strEmpty, message: Resources.login_auth_password_expired_msg, isShowLeftBtn: false, leftBtnText: Resources.strEmpty, isShowRightBtn: true, rightBtnText: Resources.ok, leftBtnClick: self.cancelLogin, rightBtnClick: self.cancelLogin))
            return false

        case "401.5":
            // Title: 認証エラー
            // Message: アカウントが失効しています。
            self.showPopupView(popupVm: PopupViewModel(isShowTitle: true, title: Resources.login_auth_err, message: Resources.login_auth_invalid_account_msg, isShowLeftBtn: false, leftBtnText: Resources.strEmpty, isShowRightBtn: true, rightBtnText: Resources.ok, leftBtnClick: self.cancelLogin, rightBtnClick: self.cancelLogin))

            return false

        default:
            return true
        }
    }
}
