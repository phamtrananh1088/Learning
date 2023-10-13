<template>
  <div class="bg">
    <screenPopup src="/Reafs_T/PasuwadoRimainda" :dialogVisible.sync="showPasswordReminder" />
    <div class="content">
      <div class="login">
        <div class="login-contianer">
          <div class="login-form">
            <img v-bind:src="logo" />
            <div class="login-form-under">
              <el-form label-width="90px" :model="userInfo" :rules="rules" ref="userInfo" @keypress="loginPress">
                <el-form-item label="ログインID" prop="userName">
                  <el-input
                    maxlength="9"
                    size="medium"
                    v-model="userInfo.userName"
                    v-focus
                    ref="userName"
                    autocomplete="off">
                  </el-input>
                </el-form-item>
                <el-form-item label="パスワード" prop="passWord">
                  <el-input
                  maxlength="30"
                    type="password"
                    size="medium"
                    v-model="userInfo.passWord"
                    autocomplete="off"
                    show-password>
                  </el-input>
                </el-form-item>
                <p class="errorMessage">{{ errorMessage }}</p><br/>
                <el-button
                  id="btnLogin"
                  type="primary"
                  size="medium"
                  :loading="loading"
                  @click="login">
                  <span>ログイン</span>
                </el-button>
                <br/>
                <div class="repass">
                  <!-- <router-link :to="{ name: 'TD00006' }">パスワードを忘れた方はこちら</router-link> -->
                  <el-link type="primary" @click="showPasswordReminder = !showPasswordReminder">パスワードを忘れた方はこちら</el-link>
                </div>
              </el-form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="main-bottom">
      <a ref = "privacyPolicy" href="http://www.sg-realty.co.jp/personalinfo" target="_blank">個人情報保護方針</a>
      <label style="margin-left: 29%;">©2020 ＳＧリアルティ株式会社 All Rights Reserved.</label>
     </div>
  </div>
</template>
<script>
import screenPopup from '../../common/ScreenPopup/ScreenPopup.vue'
let imgUrl = require('@/assets/imgs/logo2.png') // 2022.02.18 Reafsロゴに変更
var $selectvalue
export default {
  components: {
    screenPopup
  },
  data () {
    var validateuserName = (rule, value, callback) => {
      if (value === '') {
        this.getMsg('E000001').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    var validatepassword = (rule, value, callback) => {
      if (value === '') {
        this.getMsg('E000002').then((response) => {
          callback(new Error(response.data))
        })
      }
    }

    return {
      loading: false,
      logo: imgUrl,
      codeImgSrc: '',
      userInfo: {
        userName: '',
        passWord: '',
        shozokuBusho: '',
        shozokuBushoName: '',
        reafsWRTFlg: 'TD00000',
      },
      rules: {
        userName: [
          { validator: validateuserName, trigger: 'blur' }
        ],
        passWord: [
          { validator: validatepassword, trigger: 'blur' }
        ]
      },
      errorMessage: '',
      errorCnt: 0,
      oldID: '',
      showPasswordReminder: false
    }
  },
  directives: {
    focus: {
      inserted: function (el) {
        el.focus()
        if (el.children && el.children[0]) {
          el.children[0].focus()
        }
      }
    }
  },
  created () {
    $selectvalue = this
    
    this.guid = this.$route.query.guid;
    this.path = this.$route.query.redirect;

    if(this.guid && this.$store.getters.isLogin()) {
      this.switchScreen()
    } else {
      // ユーザー情報を削除
      this.$store.commit('clearUserInfo', '')
      this.$store.commit('clearMenuInfo', '')
    }
  },
  mounted () {
    this.$refs.userName.focus()
  },
  methods: {
    async switchScreen() {
      let params = await this.commonFunctionUI.getQueryParameter(this.$route.query.guid)

      const router = this.$router.resolve({ path: this.path })
      
      // URL指定起動の場合、遷移先画面を判定してパラメータを設定する
      
      // 画面毎に処理を分岐
      // Wですが、サンプルここから　※コメント解除して確認してください
      // if(router.route.name==='WD00111'){
      //   // パラメータ1とパラメータ2を利用(最大10まで)
      //   let paramWD00111 = { 工事依頼No: (!params.data)?'':params.data.パラメータ1
      //                 , page: (!params.data)?'':params.data.パラメータ2 }
      //   // 指定したパラメータを引数にして遷移する
      //   this.$router.push({ name: router.route.name, query: paramWD00111 })
      // }
      // サンプルここまで
    },
    loginPress (e) {
      if (e.keyCode == 13) {
        this.login()
      }
    },
    login () {
      if (this.userInfo.userName == '' || this.userInfo.userName.trim() == '') {
        this.getMsg('E000001').then((response) => {
          this.errorMessage = response.data
        })
        return false
      }
      if (this.userInfo.passWord == '' || this.userInfo.passWord.trim() == '') {
        this.getMsg('E000002').then((response) => {
          this.errorMessage = response.data
        })
        return false
      }
      if(this.oldID != this.userInfo.userName){
        this.errorCnt = 0
      }

      // if (this.userInfo.passWord.length >= 8){
      //   if(!this.base.checkPass(this.userInfo.passWord)){
      //     this.getMsg("E040665").then((response) => {
      //       this.errorMessage = response.data
      //     })
      //     return false
      //   }
      // } else{
      //   this.getMsg("E020349").then((response) => {
      //     this.errorMessage = response.data
      //   })
      //   return false
      // }

      this.loading = true
      this.http
        .post('/api/Reafs_T/login', this.userInfo, 'アクセスしています....')
        .then((result) => {
          //認証OK以外の場合
          if (result.code != 1) {
            this.loading = false
            this.errorMessage = result.message
            //ID、PW不一致の場合
            if(result.code == -1){
                this.errorCnt++
            }else{
              this.errorCnt = 0
            }
            if (this.errorCnt >= 5){
              this.updM015()
            }
            this.oldID = this.userInfo.userName
            return false
          }
          result.data.userId = this.userInfo.userName
          this.$store.commit('setUserInfo', result.data)
          if (result.data.新規区分 == 1) {
            this.$router.push({ name: 'TD00005' })
          } else if (this.guid) { //メールリンクからアクセスした後、業務画面へ遷移する
            this.switchScreen()
          } else if (this.path) {
            const router = this.$router.resolve({ path: this.path })
            if (router.route.name == '404ERROR') {
              this.$router.push({ name: 'TD00001' })
            } else {
              this.$router.push({ path: this.path })
            }
          } else {
            this.$router.push({ name: 'TD00001' })
          }
          // else {
          //   this.$router.push({ path: '/' })
          // }
        })
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    updM015(){
      this.http
        .post('/api/Reafs_T/updM015', this.userInfo)
        .then((result) => {
          this.errorMessage = result.message
        });
    }
  }
}
</script>

<style lang="less" scoped>
.bg {
  display: flex;
  overflow: hidden;
  position: relative;
  height: 100%;
  width: 100%;
  background-color: rgb(235, 235, 235);
  font-size: 12px;
}

.login {
  flex: 1;
}

.content {
  display: flex;
  z-index: 99;
  position: relative;
  left: 0;
  right: 0;
  margin: 0 auto;
  transform: translateY(-50%);
  top: 50%;
  height: 400px;
}

.login-contianer {
  .login-form {
    border-radius: 5px;
    padding: 10px 30px 40px 30px;
    width: 400px;
    min-height: 340px;
    background: white;
    height: 400px;
  }
  .login-form-under{
    padding-top: 20px;
  }
}

.el-button--medium{
  font-size: 12px;
}

#btnLogin{
  width: 90px;
  height: 33px;
  margin-left: 18px;
  background-color: rgb(183,198,51,255);
  border-color: rgb(183,198,51,255);
}

#btnLogin:hover, #btnLogin:focus {
  background-color: rgb(197,209,90,1);
  border-color: rgb(197,209,90,1);
}

@media screen and (max-width: 600px) {
  .bg {
    background-image: none;
  }
  .login-form {
    box-shadow: none !important;
    width: 100% !important;
  }
}
</style>
<style scoped>
.el-form >>> .el-form-item__content{
  line-height: 40px;
}
.el-form >>> .el-form-item__label{
  height: 40px;
  line-height: 40px;
  background: transparent ;
  color: #606266;
}
.el-form >>> .el-input__inner{
  height: 40px;
  line-height: 40px;
  border-radius: 1px;
}
.errorMessage {
  color: red;
  font-size: 12px;
  margin-left: 18px;
}
.main-bottom {
  height: 30px;
  position: fixed;
  width: 100%;
  bottom: 0;
  margin-left: 20px;
}
.repass {
  margin-top: 20px;
  margin-left: 18px;
}
</style>
