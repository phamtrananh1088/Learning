import SwiftUI
import GRDB

struct ContentView: View {
    @ObservedObject var current = Current.Shared
    @State var isShowPopup = false
    @State private var isUpdateAvailable: Bool = false
    @State private var appStoreUrl: String = ""
    
    var body: some View {
        ZStack {
            if current.screen.typeScreen() == .FullView {
                
                if current.screen == .Login {
                    LoginView().transition(.slide)
                }
                
                if current.screen == .Notice {
                    NoticeListView(noticeListVm: NoticeListViewModel(typeNotice: nil)).transition(.slide)
                }
                
                if current.screen == .Weather {
                    WeatherView()
                }
                
                if current.screen == .Setting {
                    SettingView().transition(.slide)
                }
            } else if current.screen.typeScreen() == .TabView {
                HomeView(homeVm: HomeViewModel() {
                    if current.screen != .Login {
                        isShowPopup = true
                    }
                })
            }
            
            if current.screen != .Login {
                if isShowPopup {
                    PopUpWindow(popupVm: PopupViewModel(isShowTitle: false, title: Resources.strEmpty, message: Resources.invalid_account_alert_msg, isShowLeftBtn: false, leftBtnText: Resources.strEmpty, isShowRightBtn: true, rightBtnText: Resources.ok, leftBtnClick: {
                        isShowPopup = false
                        current.changeScreenTo(screenName: .Login)
                    }, rightBtnClick: {
                        isShowPopup = false
                        current.changeScreenTo(screenName: .Login)
                    }), show: $isShowPopup)
                }
            }
        }
        .onAppear(perform: checkAppVersion)
        .onReceive(NotificationCenter.default.publisher(for: UIApplication.willEnterForegroundNotification)) { _ in
            checkAppVersion()
        }
        .alert(isPresented: $isUpdateAvailable) {
            Alert(title: Text(Resources.update_version_title),
                  message: Text(Resources.update_version_content),
                  dismissButton: Alert.Button.default(Text("OK"), action: {
                    Helper.Shared.goToAppStore(appUrl: appStoreUrl) { rs in
                        
                    }
                  })
            )
        }
    }
    func checkAppVersion() {
        Helper.Shared.checkAppUpdateAvailability(onSuccess: { result, url in
            isUpdateAvailable = result
            appStoreUrl = url
        }, onError: { result in
            
        })
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
