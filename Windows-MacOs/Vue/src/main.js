// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'//reafs デスクトップ用UI
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/ja' // 日本語にロケール変換
// import router from './router' // 適用router：開発用
// import router from './router/reafs_t' // 適用router：Reafs-Tの場合
// import router from './router/reafs_w' // 適用router：Reafs-Wの場合
import router from './router/reafs_r_web' // 適用router：Reafs-Rの場合
import http from './api/http'
import commonFunctionUI from './api/commonFunctionUI'
import store from './store/index'
import base from './utilities/common'

//reafs_r_webのみ vantスマホ用UIコントロールを別々インポートする
import { DatetimePicker } from 'vant';
import 'vant/lib/datetime-picker/style';
import { Popup } from 'vant';
import 'vant/lib/popup/style';
import { Locale } from 'vant';
import jaJP from 'vant/es/locale/lang/ja-JP';
Locale.use('ja-JP', jaJP);

//element-ui version 2.15.9 の「DatePicker」はBUGがあります
//①　fallback to 2.6.3　 https://github.com/ElemeFE/element/issues/21965
//②　fallback to 2.15.6　https://github.com/ElemeFE/element/issues/21905
//element-guiのコンポーネントをインポートする
import { DatePicker } from 'element-gui';
import elementguijaJP from 'element-gui/lib/locale/lang/ja'
import elementguilocale from 'element-gui/lib/locale'
elementguilocale.use(elementguijaJP)

Vue.config.productionTip = false
Vue.use(ElementUI, {
  locale
}) // 日本語にロケール変換
Vue.use(Popup);
Vue.use(DatetimePicker);
Vue.use(DatePicker)
// require('./mock')

// 本番環境用にrouterを分けて管理
// if (process.env.NODE_ENV == 'production') {
//   var vue = new Vue({
//     el: '#app',
//     store,
//     productionrouter,
//     components: {
//       App
//     },
//     template: '<App/>'
//   })
// } else {
var vue = new Vue({
  el: '#app',
  store,
  router,
  components: {
    App
  },
  template: '<App/>'
})
// }

Vue.prototype.http = http
Vue.prototype.http.init(vue)
Vue.prototype.commonFunctionUI = commonFunctionUI
Vue.prototype.base = base
Vue.prototype.$tabs = {}
Vue.prototype.$ELEMENT = {
  zIndex: 3000
}

Vue.prototype.MsgInfo = function (options, callback) {
  options.type = 'info'
  base.openMsg(Vue.prototype, options, callback)
  
  setTimeout(() => {
    document.querySelector('body > div.el-message-box__wrapper > div > div.el-message-box__btns > button.el-button--primary').focus();
  }, 200)
}
Vue.prototype.MsgErr = function (options, callback) {
  options.type = 'error'
  // fix multi alert
  const cancelBtn = document.querySelector('body > div.el-message-box__wrapper > div > div.el-message-box__btns > button')
  if (document.activeElement === cancelBtn) {
    return
  }
  base.openMsg(Vue.prototype, options, callback)
  
  setTimeout(() => {
    document.querySelector('body > div.el-message-box__wrapper > div > div.el-message-box__btns > button.el-button--primary').focus();
  }, 200)
}
Vue.prototype.MsgConf = function (options, thenfunc, catchfunc) {
  options.type = 'warning'
  base.openConfirmMsg(Vue.prototype, options, thenfunc, catchfunc)

  setTimeout(() => {
    document.querySelector('body > div.el-message-box__wrapper > div > div.el-message-box__btns > button.el-button--primary').focus();
  }, 200)
}
