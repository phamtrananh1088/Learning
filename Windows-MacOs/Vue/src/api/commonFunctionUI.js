
import http from '@/../src/api/http.js'
import store from '../store/index'
import webconfig from '../../static/webconfig.json'
// let $vue = null;
let commonFunctionUI = {
  // init(vue) {
  //     $vue = vue;
  // },
  getMsg (key) {
    const userInfo = store.getters.getUserInfo()
    if (userInfo && userInfo.listMessage && userInfo.listMessage[key]) {
      return new Promise((resolve, reject) => {
        resolve({data: userInfo.listMessage[key]})
      })
    }
    const msgInfo = {
      メッセージコード: key
    }
    return http
      .post('/api/Reafs_T/Common/getMsg', msgInfo)
  },
  /*
    権限情報を取得
    */
  getControlActiveButton (hash, menuInfo) {
    http.post('/api/Reafs_W/Common/getControlActiveButton', menuInfo).then(response => {
      for (const b in hash) {
        const cf = response.data.find((m) => m.コントロールID === b)
        if (cf) {
          if (hash[b].hasOwnProperty('visible')) {
            hash[b].visible = (cf.利用可否 === 1)
          } else if (hash[b].hasOwnProperty('disabledOrg')) {
            hash[b].disabledOrg = (cf.利用可否 !== 1)
            hash[b].disabled = hash[b].disabledOrg
          }
        } else {
          if (hash[b].hasOwnProperty('visible')) {
            hash[b].visible = false
          } else if (hash[b].hasOwnProperty('disabledOrg')) {
            hash[b].disabledOrg = true
            hash[b].disabled = hash[b].disabledOrg
          }
        }
      }
    })
  },
  /*
    msg_type        (number)
    mail_to         (string)
    msg_head        (string)
    msg_body        (string)
    pg_id           (string)
    st_kbn          (number)
    mail_pattern_no (number)
    宛先事務所コード       (string array)
    宛先営業所コード       (string array)
    宛先部コード       (string array)
    宛先課コード       (string array)
    宛先係コード       (string array)
    業者コード       (string)
    業者コード枝番       (string)
    担当者コード       (string array)
    工事依頼NO    (string)
    工事依頼NO枝番 (string)
    契約NO        (string)
    履歴NO        (number)
    明細NO        (number)
    契約年月       (string)
  */
  async CM00110SendMail (param) {
    return new Promise((resolve) => {
      return http
        .post('/api/Common/CM00110SendMail', param) //ハノイ側修正2022/10/14　課題管理表№161：「2022/10/13依頼分」「Reafs-W、Reafs-T用のメール送信機能（共通機能）の作成依頼」
        .then((res) => {
          resolve(res)
        })
        .catch((error) => {
          resolve({
            status: false,
            message: error
          })
        })
    })
  },
  /*
    paramINSERT_PG     (string)
    param工事依頼No     (string)
    param工事依頼NO枝番 (string)
    param契約NO        (string)
    param履歴NO        (number)
    param明細NO        (number)
    param契約年月       (string)
    param予定年月       (string)
    param業者コード     (string)
    param業者コード枝番  (string)
    param架電区分       (number)
  */
  async twilio_Call (param) {
    return new Promise((resolve) => {
      return http
        .post('/api/Common/twilio_Call', param)
        .then((res) => {
          resolve(res)
        })
        .catch((error) => {
          resolve({
            status: false,
            message: error
          })
        })
    })
  },
  async getQueryParameter (guid) {
    return new Promise((resolve) => {
      return http
      .get('/api/Common/getQueryParameter', { guid })
        .then((res) => {
          resolve(res)
        })
        .catch((error) => {
          resolve({
            status: false,
            message: error
          })
        })
    })
  },
  async setQueryParameter (param) {
    return new Promise((resolve) => {
      return http
        .post('/api/Common/setQueryParameter', param)
        .then((res) => {
          resolve(res)
        })
        .catch((error) => {
          resolve({
            status: false,
            message: error
          })
        })
    })
  },
  /*
    @param {*} type : W, T, R
  */
  getBaseURL(type) {
    if (type == 'W') {
      return webconfig.Reafs_W_BaseURL
    } else if (type == 'T') {
      return webconfig.Reafs_T_BaseURL
    } else if (type == 'R') {
      return webconfig.Reafs_R_BaseURL
    }
  }
}

export default commonFunctionUI
