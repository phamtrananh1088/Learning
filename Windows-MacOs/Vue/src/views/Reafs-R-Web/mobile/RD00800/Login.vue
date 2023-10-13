<template>
  <div class="bg">
    <div class="content">
      <div class="login">
        <div class="login-contianer">
          <div>
            <el-row :gutter="20">
              <el-col :span="6" :offset="9"><div class="grid-content"><p style="color:#F00">開発環境</p></div></el-col>
              <el-col :span="6" :offset="2"><div class="grid-content"><p >Ver.{{version}}</p></div></el-col>
            </el-row>
          </div>
          <div class="login-form">
            <el-image v-bind:src="logo" />
            <div class="login-form-under">
              <el-form label-width="90px" :model="userInfo" :rules="rules" ref="userInfo" @keypress="loginPress">
                <el-form-item label="ログインID" prop="userName">
                  <el-input
                    size="medium"
                    v-model="userInfo.userName"
                    v-focus
                    @blur="GetBushoList"
                    ref="userName"
                    autocomplete="off">
                  </el-input>
                </el-form-item>
                <el-form-item label="パスワード" prop="passWord">
                  <el-input
                    type="password"
                    size="medium"
                    v-model="userInfo.passWord"
                    autocomplete="off"
                    ref="passWord"
                    show-password>
                  </el-input>
                </el-form-item>
                <el-form-item label="所属部署">
                  <el-select
                    v-model="userInfo.shozokuBusho"
                    @change="selectShozokuBusho"
                    ref="shozokuBusho"
                    >
                    <el-option
                      v-for="item in shozokuBushovalue"
                      :key="item.コード"
                      :label="item.コードおよび名称"
                      :value="item.コード">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-button
                  id="btnLogin"
                  type="primary"
                  size="medium"
                  :loading="loading"
                  @click="login">
                  <span v-if="!loading">ログイン</span>
                  <span v-else>ログインしています...</span>
                </el-button>
              </el-form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import config from  '../../../../../package.json'
import "../../../../assets/css/commonR.less";
let imgUrl = require('@/assets/imgs/logo2.png') // 2022.02.18 Reafsロゴに変更
var $selectvalue
export default {
  data () {
    var validateuserName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('担当者コードを入力してください'))
      }
    }
    var validatepassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('パスワードを入力してください'))
      }
    }

    return {
      loading: false,
      logo: imgUrl,
      codeImgSrc: '',
      userInfo: {
        userName: '',
        passWord: '',
        shozokuBushoCd: '',   //コード（ドロップダウンリストのキー）
        shozokuBushoName: '', //部門名（メニュー表示用）
        shozokuBusho: '',     //コードおよび名称（ドロップダウンリストのラベル）
        reafsWRTFlg: 'RD00800',
      },
      rules: {
        userName: [
          { validator: validateuserName, trigger: 'blur' }
        ],
        passWord: [
          { validator: validatepassword, trigger: 'blur' }
        ]
      },
      shozokuBushovalue: [],
      version: config.version,
      工事依頼NO: "", //工事依頼№
      工事依頼NO枝番: "", //工事依頼NO枝番
      承認種類: "" //承認種類
    }
  },
  directives: {
    focus: {
      inserted: function (el) {
        el.focus()
      }
    }
  },
  created () {
    $selectvalue = this;

    this.guid = this.$route.query.guid;
    this.path = this.$route.query.redirect;

    if(this.guid && this.$store.getters.isLogin()) {
      this.switchScreen()
    } else {
      // // ユーザー情報を削除
      this.$store.commit('clearUserInfo', '')
      this.$store.commit('clearMenuInfo', '')
    }
  },
  mounted () {
    this.$refs.userName.focus()
  },
  methods: {
    async switchScreen() {
      // クエリパラメータのguidより、登録したパラメータを取得する
      let params = await this.commonFunctionUI.getQueryParameter(this.$route.query.guid)

      // 起動時URLより遷移先の画面情報を取得する
      const router = this.$router.resolve({ path: this.path })
      // this.$router.push({ name: router.route.name, params: this.パラメータ })
        // URL指定起動の場合、遷移先画面を判定してパラメータを設定する

      if(router.route.name==='RD00803'){
        let shoninparam = { 工事依頼NO: (!params.data)?'':params.data.パラメータ1
                      , 工事依頼NO枝番: (!params.data)?'':params.data.パラメータ2
                      , 種類: (!params.data)?'':params.data.パラメータ3
                    }
        this.$router.push({ name: router.route.name, params: shoninparam })
      }
    },
    loginPress (e) {
      if (e.keyCode == 13) {
        this.login()
      }
    },
    async login () {
      // ログインID未入力
      if (this.userInfo.userName == '' || this.userInfo.userName.trim() == '') {
        this.getMsg('E000001').then((response) => {
          this.MsgErr({
            title: this.$store.getters.getPageTitle(),
            message: response.data
          })
          this.$refs.userName.focus()
        })
        return
      }
      // パスワード未入力
      if (this.userInfo.passWord == '' || this.userInfo.passWord.trim() == '') {
        this.getMsg('E000002').then((response) => {
          this.MsgErr({
            title: this.$store.getters.getPageTitle(),
            message: response.data
          })
          this.$refs.passWord.focus()
        })
        return
      }

      this.loading = true;
      this.http
        .post('/api/user/login', this.userInfo, 'アクセスしています....')
        .then(async result => {
          if (!result.status) {
            this.loading = false
            // return this.$message.error(result.message)
            // this.MsgErr({
            //   title: this.$store.getters.getPageTitle(),
            //   message: result.message
            // })

            // ログインエラー
            // ログインIDまたは、パスワードが誤っています。
            await this.getMsg('E000003').then((response) => {
              this.MsgErr({
                title: this.$store.getters.getPageTitle(),
                message: response.data
              })
              this.$refs.userName.focus()
            })
            this.loading = false
            return
          }
          else if (result.data.siyoKbn !== 1 || result.data.delKbn !== 0) {
            // ログインエラー
            // 既に削除されているユーザです。ログイン出来ません。
            await this.getMsg('E000004').then((response) => {
              this.MsgErr({
                title: this.$store.getters.getPageTitle(),
                message: response.data
              })
            })
            this.loading = false
            return
          }
          // this.$message.success('アクセス成功、ホームページへ遷移しています…')

          result.data.shozokuBushoName = this.userInfo.shozokuBushoName;
          this.$store.commit('setUserInfo', result.data)
          // this.$router.push({ path: "/Reafs_R_Web" });
          
          //メールリンクからアクセスした後、業務画面へ遷移する
          if (this.guid) {
            this.switchScreen()
          } else if (this.path) {
            const router = this.$router.resolve({ path: this.path })
            if (router.route.name == '404ERROR') {
              this.$router.push({ name: 'RD00801' })
            } else {
              this.$router.push({ path: this.path })
            }
          } else {
            this.$router.push({ name: 'RD00801' })
          }
        })
    },
    async GetBushoList () {
      if (this.userInfo.userName === '') {
          $selectvalue.userInfo.shozokuBusho = ''
          $selectvalue.userInfo.shozokuBushoName = ''
          $selectvalue.userInfo.shozokuBushoCd = ''
        return
      }
      this.http
        .post('/api/user/getDepartment', this.userInfo)
        .then(async response => {
          $selectvalue.shozokuBushovalue = response.data
          if (response.data[0] === undefined) {
            // 該当データ無し。
            await this.getMsg('E000014').then((response) => {
              this.MsgErr({
                title: this.$store.getters.getPageTitle(),
                message: response.data
              })
            })
            $selectvalue.userInfo.shozokuBusho = ''
            $selectvalue.userInfo.shozokuBushoName = ''
            $selectvalue.userInfo.shozokuBushoCd = ''
            return
          }
          if (response.data[0]) {
            $selectvalue.userInfo.shozokuBusho = response.data[0].コードおよび名称
            $selectvalue.userInfo.shozokuBushoName = response.data[0].部門名
            $selectvalue.userInfo.shozokuBushoCd = response.data[0].コード
          }
        })
    },
    selectShozokuBusho (val) {
      var obj = {}
      obj = $selectvalue.shozokuBushovalue.find(item => {
        return item.コード === val
      })
      this.userInfo.shozokuBushoName = obj.部門名
      this.userInfo.shozokuBushoCd = obj.コード
    },

    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
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
    width: 100%;
    min-height: 340px;
    background: white;
    height: 400px;
    margin-left: 20px;
  }
  .login-form-under{
    padding-top: 20px;
  }
}

#btnLogin{
  width: 100%;
}

@media screen and (max-width: 600px) {
  .bg {
    background-image: none;
  }
  .login-form {
    box-shadow: none !important;
    width: 90% !important;
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
}
.el-form >>> .el-form-item__error{
  padding-top: 0;
}
.el-form-item{
  margin-bottom: 15px;
}
.el-form >>> .el-input__inner{
  height: 40px;
  line-height: 40px;
  border-radius: 1px;
}
.el-form >>> .el-input__icon{
  line-height: 32px;
}

.el-form >>> .el-form-item__label {
  color: #303133;
}
</style>
