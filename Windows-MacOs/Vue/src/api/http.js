import axios from 'axios'
import store from '../store/index'
import webconfig from '../../static/webconfig.json'

import { Loading, MessageBox } from 'element-ui'
axios.defaults.timeout = 500000
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'

//UIのLoading制御変数
let loadingInstance
let loadingStatus = false
let isHandleLoading = false
//UIのMessageBox制御変数
let messageBoxFlag = 0
let subsite = '/'
if (process.env.NODE_ENV == 'development') {
  // axios.defaults.baseURL = 'http://172.16.2.198:3000/'
  // axios.defaults.baseURL = 'http://localhost:3000/'
  // axios.defaults.baseURL = 'http://10.251.38.62:3000/';
  //   axios.defaults.baseURL = 'http://localhost:3000/';
  axios.defaults.baseURL = webconfig.baseURL_development;
  subsite = webconfig.subsite_development
} else if (process.env.NODE_ENV == 'debug') {
  // axios.defaults.baseURL = 'http://localhost:8888/'
  // axios.defaults.baseURL = 'http://10.251.38.62:3000/';
//   axios.defaults.baseURL = 'http://localhost:3000/';
  axios.defaults.baseURL = webconfig.baseURL_debug;
  subsite = webconfig.subsite_debug
} else if (process.env.NODE_ENV == 'production') {
  // axios.defaults.baseURL = 'http://localhost:3000/'
  axios.defaults.baseURL = webconfig.baseURL_production;
  subsite = webconfig.subsite_production
}

let ipAddress = axios.defaults.baseURL
// リクエスト　インターセプト
axios.interceptors.request.use((config) => {
  // リクエスト前の処理をここに追記
  return config
}, (error) => {
  // リクエストのエラー処理
  return Promise.reject(error)
})
// レスポンス　インターセプト
axios.interceptors.response.use((res) => {
  // レスポンス前の処理をここに追記
  if (!isHandleLoading) {
    closeLoading()
  }
  isHandleLoading = false
  checkResponse(res)

  return Promise.resolve(res)
}, (error) => {
  // レスポンスのエラー処理
  closeLoading()
  let httpMessage = ''
  if (error.response) {
    if (error.response.data && error.response.data.message) {
      httpMessage = error.response.data.message
    } else if (error.response.status == '404') {
      httpMessage = 'リクエストアドレスが無効です。'
    } else if (error.response.status == '401') {
      httpMessage = '認証期限切れです。'
    }
  } else {
    httpMessage = 'サーバー処理で問題が発生しました。'
  }
  
  redirect(error.response || {}, httpMessage)
  return Promise.reject(error)
})

// レスポンスインターセプトのチェック
function checkResponse (res) {
  // tokenリフレッシュ
  if (!res.headers) {
    // レスポンスのヘッダに「reafs_exp」検査
    // サーバー側「ApiAuthorizeFilter」からToken時限チェック
    if (res.getResponseHeader('reafs_exp') == '1') {
      replaceToken()
    }
  } else if (res.headers.reafs_exp == '1') {
    replaceToken()
  }
}
let _vue = null
const _Authorization = 'Authorization'
function init (vue) {
  _vue = vue
}

//Loading制御
function showLoading (loading) {
  if (!loading || loadingStatus) {
    return
  }
  loadingInstance = Loading.service({
    target: '#loading-container',
    customClass: 'el-loading',
    text: typeof loading === 'string' ? loading : '処理中.....',
    spinner: 'el-icon-loading',
    background: 'rgba(58, 61, 63, 0.32)'
  })
}
function closeLoading () {
  isHandleLoading = false
  if (loadingInstance) {
    loadingInstance.close()
  }
  if (loadingStatus) {
    loadingStatus = false
    if (loadingInstance) {
      loadingInstance.close()
    }
  }
}

function getToken () {
  return store.getters.getToken()
}

// UPDATE
function put (url, params, loading, config) {
  showLoading(loading)
  axios.defaults.headers[_Authorization] = getToken()
  return new Promise((resolve, reject) => {
    axios.put(url, params, config)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err && err.data && err.data.message ? err.data.message : 'サーバー処理で問題が発生しました。')
      })
      .catch((error) => {
        reject(error)
      })
  })
}

// INSERT
function post (url, params, loading, config) {
  showLoading(loading)
  axios.defaults.headers[_Authorization] = getToken()
  if (config && config.isHandleLoading) {
    isHandleLoading = true
  }
  return new Promise((resolve, reject) => {
    axios.post(url, params, config)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err && err.data && err.data.message ? err.data.message : 'サーバー処理で問題が発生しました。')
      })
      .catch((error) => {
        reject(error)
      }).finally(() => {
      })
  })
}

function get (url, param, loading, config) {
  showLoading(loading)
  axios.defaults.headers[_Authorization] = getToken()
  if (config && config.isHandleLoading) {
    isHandleLoading = true
  }
  return new Promise((resolve, reject) => {
    axios.get(url, { params: param }, config)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

function createXHR () {
  if (XMLHttpRequest) {
    return new XMLHttpRequest()
  }
  if (ActiveXObject) {
    if (typeof arguments.callee.activeXString !== 'string') {
      var versions = [
        'MSXML2.XMLHttp.6.0',
        'MSXML2.XMLHttp',
        'MSXML2.XMLHttp.3.0'
      ]
      for (var i = 0; i < versions.length; i++) {
        try {
          new ActiveXObject(versions[i])
          arguments.callee.activeXString = versions[i]
          break
        } catch (e) {
          console.log(e)
        }
      }
    }
    return new ActiveXObject(arguments.callee.activeXString)
  }
}

function redirect(responseText, message) {
  try {
    let responseData = typeof responseText === 'string' && responseText !== "" ? JSON.parse(responseText) : responseText
    if ((responseData.hasOwnProperty('status') && responseData.status == 401) ||
      (responseData.status && responseData.status.code == 401) ||
      responseData.status == 401 ) {
      closeLoading()

      if (messageBoxFlag === 0) {
        messageBoxFlag = 1 //制御変数を変更
        MessageBox.confirm('セッション情報がありません。タイムアウトしました。ログイン画面に戻ります。', 'システム', {
          confirmButtonText: 'はい(Y)',
          showCancelButton: false,
        }).then(() => {
          messageBoxFlag = 0
          toLogin()
          // ハノイ側修正2022/11/17　STEP2_W「タイムアウトの状態でサブ画面が開くの不具合」Start
          if (window.parent && window.parent === top) {
            window.parent.postMessage({
              'call': 'closePopup'
            }, '*')
            if (_vue.$router.options.routes[0].name == 'Index_R') {
              window.top.location.replace(resolve('/Reafs_R_Web/login'))
            } else if (_vue.$router.options.routes[0].name == 'Index_T') {
              window.top.location.replace(resolve('/Reafs_T/login'))
            } else if (_vue.$router.options.routes[0].name == 'Index_W') {
              window.top.location.replace(resolve('/Reafs_W/login'))
            }
          }
          // ハノイ側修正2022/11/17　STEP2_W「タイムアウトの状態でサブ画面が開くの不具合」End
        })
      }
    } else {
      if (messageBoxFlag === 0) {
        messageBoxFlag = 1 //制御変数を変更
        MessageBox.confirm(message, 'システム', {
          confirmButtonText: 'はい(Y)',
          showCancelButton: false,
        }).then(() => {
          messageBoxFlag = 0
        })
      }
    }
  } catch (error) {
    if (messageBoxFlag === 0) {
      messageBoxFlag = 1 //制御変数を変更
      MessageBox.confirm(error, 'システム', {
        confirmButtonText: 'はい(Y)',
        showCancelButton: false,
      }).then(() => {
        messageBoxFlag = 0
      })
    }
  }
}

function toLogin () {
  _vue.$store.commit('clearUserInfo', '')
  _vue.$store.commit('clearMenuInfo', '')
  if (_vue.$router.options.routes[0].name == 'Index_R') {
    _vue.$router.push({ path: resolve('/Reafs_R_Web/login'), params: { r: Math.random() } })
  } else if (_vue.$router.options.routes[0].name == 'Index_T') {
    _vue.$router.push({ path: resolve('/Reafs_T/login'), params: { r: Math.random() } })
  } else if (_vue.$router.options.routes[0].name == 'Index_W') {
    _vue.$router.push({ path: resolve('/Reafs_W/login'), params: { r: Math.random() } })
  }
}
// tokenリフレッシュ
function replaceToken () {
  //ハノイ側修正2022/12/01　課題管理表№35：「2022/12/01依頼分」「タイムアウト設定時間リセット機能を有効にしてください。」START
  var url = ''
  if (_vue.$router.options.routes[0].name == 'Index_R') {
    url = '/api/User/replaceToken'
  } else if (_vue.$router.options.routes[0].name == 'Index_T') {
    url = '/api/Reafs_T/replaceToken'
  } else if (_vue.$router.options.routes[0].name == 'Index_W') {
    url = '/api/User/replaceToken'
  }
  //ハノイ側修正2022/12/01　課題管理表№35：「2022/12/01依頼分」「タイムアウト設定時間リセット機能を有効にしてください。」END
  ajax({
    url: url,
    param: {},
    json: true,
    success: function (x) {
      if (x.status) {
        let userInfo = _vue.$store.getters.getUserInfo()
        userInfo.token = x.data
        _vue.$store.commit('setUserInfo', userInfo)
      } else {
        toLogin()
      }
    },
    errror: function (ex) {
      console.log(ex)
      toLogin()
    },
    type: 'post',
    async: false
  })
}

function ajax (param) {
  let httpParam =
        Object.assign({
          url: '',
          headers: {},
          param: {},
          json: true,
          success: function () { },
          error: function () { },
          type: 'post',
          async: true
        }, param)

  httpParam.url = axios.defaults.baseURL + httpParam.url.replace(/\/?/, '')
  httpParam.headers[_Authorization] = getToken()
  var xhr = createXHR()
  xhr.onreadystatechange = function () {
    if (xhr.status == 401) {
      redirect(xhr)
      return
    }
    if (xhr.status == 403) {
      redirect(xhr.responseText)
      return
    }
    checkResponse(xhr)
    if (xhr.readyState == 4 && xhr.status == 200) {
      httpParam.success(httpParam.json ? JSON.parse(xhr.responseText) : xhr.responseText)
      return
    }
    if (xhr.status != 0 && xhr.readyState != 1) {
      httpParam.error(xhr)
    }
  }
  // リクエスト初期化
  xhr.open(
    httpParam.type,
    httpParam.url,
    httpParam.async
  )
  xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded')
  // xhr.setRequestHeader('abc', { name: 'aaaa', value: 123456 }) // todo 共通パラメータのため
  for (const key in httpParam.headers) {
    xhr.setRequestHeader(key, httpParam.headers[key])
  }
  let dataStr = ''
  for (const key in httpParam.param) {
    //パラメータ形「param1 = 1&param2 = 2」に修正
    dataStr += key + "=" + httpParam.param[key] + "&";
  }
  try {
    xhr.send(dataStr)
  } catch (error) {
    toLogin()
  }
}
/**
 * get full url
 * @param {*} url : url
 * @returns full url contain subsite
 */
function resolve (url) {
  if (subsite === '/') {
    return url
  } else {
    return subsite + url.substring(1)
  }
}
ajax.post = function (url, param, success, errror) {
  ajax({ url: url, param: param, success: success, error: errror, type: 'post' })
}
ajax.get = function (url, param, success, errror) {
  ajax({ url: url, param: param, success: success, error: errror, type: 'get' })
}
export default { put, post, get, ajax, init, ipAddress, closeLoading, subsite, resolve }
