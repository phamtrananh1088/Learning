import Vue from 'vue'
import Vuex from 'vuex'
import data from './data.js'
const webconfig = require('../../static/webconfig.json')

Vue.use(Vuex)

function getUserInfo (state) {
  if (state.userInfo) return state.userInfo
  let userInfo = localStorage.getItem(keys.USER)
  if (userInfo) {
    state.userInfo = JSON.parse(userInfo)
  }
  return state.userInfo
}
function defKey (key) {
  return key + appSubfix
}
const appSubfix = webconfig.spa_folder === '/Reafs_W/' ? 'W' : webconfig.spa_folder === '/Reafs_R_Web/' ? 'R_Web' : ''
const keys = { USER: defKey('user') }
const appSetting = {
  PAGEDATA: defKey('pageData'),
  OLDPATH: defKey('oldPath'),
  ISBACKTOLOGIN: defKey('isBackToLogin'),
  MAIN_THEME: defKey('main_theme'),
  KEEPALIVEFLAG: defKey('KeepAliveFlag'),
  ROUTERBACKFLAG: defKey('RouterBackFlag'),
  ROUTERPARAMS: defKey('routerParams'),
  IRAITENPUTOUROKUPAGEDATA: defKey('IraiTenpuTourokuPageData')
}
function getMenuInfo (state) {
  if (state.menuInfo) return state.menuInfo
  let menuInfo = localStorage.getItem(menus.MENU)
  if (menuInfo) {
    state.menuInfo = JSON.parse(menuInfo)
  }
  return state.menuInfo
}
function getPageTitle (state) {
  if (state.pagetitle) return state.pagetitle
  let pageTitle = localStorage.getItem(menus.PAPGETITLE)
  if (pageTitle) {
    state.pagetitle = pageTitle
  }
  return state.pagetitle
}
function getIsBackToLogin (state) {
  if (state.isBackToLogin) return state.isBackToLogin
  let data = localStorage.getItem(appSetting.ISBACKTOLOGIN)
  if (data) {
    state.isBackToLogin = data
  }
  return state.isBackToLogin
}
function getMainTheme (state) {
  if (state.main_theme) return state.main_theme
  let data = localStorage.getItem(appSetting.main_theme)
  if (data) {
    state.main_theme = data
  }
  return state.isBackToLogin
}
function getKeepAliveFlag (state) {
  if (state.KeepAliveFlag) return state.KeepAliveFlag
  let data = localStorage.getItem(appSetting.KEEPALIVEFLAG)
  if (data) {
    state.KeepAliveFlag = data
  }
  return state.KeepAliveFlag
}
function getRouterBackFlag (state) {
  if (state.RouterBackFlag) return state.RouterBackFlag
  let data = localStorage.getItem(appSetting.ROUTERBACKFLAG)
  if (data) {
    state.RouterBackFlag = data
  }
  return state.RouterBackFlag
}
function getRouterParams (state) {
  if (state.routerParams) return state.routerParams
  let data = localStorage.getItem(appSetting.ROUTERPARAMS)
  if (data) {
    state.routerParams = JSON.parse(data)
  }
  return state.routerParams
}
function getIraiTenpuTourokuPageData (state) {
  if (state.IraiTenpuTourokuPageData) return state.IraiTenpuTourokuPageData
  let data = localStorage.getItem(appSetting.IRAITENPUTOUROKUPAGEDATA)
  if (data) {
    state.IraiTenpuTourokuPageData = JSON.parse(data)
  }
  return state.IraiTenpuTourokuPageData
}
const menus = { MENU: defKey('menu'), PAPGETITLE: defKey('pagetitle') }
// this.$store.system(systemは名称)
const system = {
  state: {
    isLoading: false,
    userInfo: null,
    menuInfo: null,
    IpAddress: '',
    pageData: {},
    oldPath: '',
    isBackToLogin: false,
    main_theme: 'orange',
    KeepAliveFlag: 0,
    RouterBackFlag: 0,
    routerParams: null,
    IraiTenpuTourokuPageData: null
  },
  mutations: {
    setUserInfo (state, data) {
      state.userInfo = data
      localStorage.setItem(keys.USER, JSON.stringify(data))
    },
    setMenuInfo (state, data) {
      state.menuInfo = data
      localStorage.setItem(menus.MENU, JSON.stringify(data))
    },
    clearUserInfo (state) {
      state.userInfo = null
      localStorage.removeItem(keys.USER)
    },
    clearMenuInfo (state) {
      state.menuInfo = null
      localStorage.removeItem(menus.MENU)
    },
    updateLoadingState (state, flag) {
      state.isLoading = flag
    },
    setpageTitle (state, data) {
      state.pagetitle = data
      localStorage.setItem(menus.PAPGETITLE, JSON.stringify(data))
    },
    setIpAddress (state, data) { // this.$store.commit('setIpAddress', data)
      state.IpAddress = data
    },
    setPageData (state, data) {
      window.sessionStorage.setItem(appSetting.PAGEDATA, JSON.stringify(data))
      // state.pageData = data
    },
    setOldPath (state, data) {
      window.sessionStorage.setItem(appSetting.OLDPATH, JSON.stringify(data))
      // state.oldPath = data
    },
    setIsBackToLogin (state, data) {
      state.isBackToLogin = data
      localStorage.setItem(appSetting.ISBACKTOLOGIN, JSON.stringify(data))
    },
    setMainTheme (state, data) {
      state.main_theme = data
      localStorage.setItem(appSetting.MAIN_THEME, JSON.stringify(data))
    },
    setKeepAliveFlag (state, data) {
      state.KeepAliveFlag = data
      localStorage.setItem(appSetting.KEEPALIVEFLAG, JSON.stringify(data))
    },
    setRouterBackFlag (state, data) {
      state.RouterBackFlag = data
      localStorage.setItem(appSetting.ROUTERBACKFLAG, JSON.stringify(data))
    },
    setRouterParams (state, data) {
      state.routerParams = data
      localStorage.setItem(appSetting.ROUTERPARAMS, JSON.stringify(data))
    },
    setIraiTenpuTourokuPageData (state, data) {
      state.IraiTenpuTourokuPageData = data
      localStorage.setItem(appSetting.IRAITENPUTOUROKUPAGEDATA, JSON.stringify(data))
    }
  },
  getters: {
    getUserInfo: (state) => () => {
      getUserInfo(state)
      return state.userInfo
    },
    getUserName: (state) => () => {
      getUserInfo(state)
      if (state.userInfo) {
        return state.userInfo.userName
      }
      return 'アクセス情報は取得できませんでした。'
    },
    getToken: (state) => () => {
      getUserInfo(state)
      if (state.userInfo) {
        return 'Bearer ' + state.userInfo.token
      }
      return ''
    },
    isLogin: (state) => () => {
      if (getUserInfo(state)) {
        return true
      }
      return false
    },
    getMenuInfo: (state) => () => {
      getMenuInfo(state)
      return state.menuInfo
    },
    isLoading: (state) => () => {
      return state.isLoading
    },
    getPageTitle: (state) => () => {
      getPageTitle(state)
      if (state.userInfo) {
        return state.pagetitle
      }
    },
    getIpAddress: (state) => () => {
      return state.IpAddress
    },
    getPageData: (state) => () => {
      return JSON.parse(window.sessionStorage.getItem(appSetting.PAGEDATA))
    },
    getOldPath: (state) => () => {
      return JSON.parse(window.sessionStorage.getItem(appSetting.OLDPATH))
      // return state.oldPath
    },
    getIsBackToLogin: (state) => () => {
      return getIsBackToLogin(state)
    },
    getMainTheme: (state) => () => {
      return getMainTheme(state)
    },
    getKeepAliveFlag: (state) => () => {
      return getKeepAliveFlag(state)
    },
    getRouterBackFlag: (state) => () => {
      return getRouterBackFlag(state)
    },
    getRouterParams: (state) => () => {
      return getRouterParams(state)
    },
    getIraiTenpuTourokuPageData: (state) => () => {
      return getIraiTenpuTourokuPageData(state)
    }
  },
  actions: {
    toDo (context) {
      return context.Store.m
    },
    onLoading (context, flag) {
      context.commit('updateLoadingState', flag)
    }
  }
}
const store = new Vuex.Store({
  modules: {
    system, // this.$store.state.system
    data
  }
})
export default store
