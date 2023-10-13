<template>
  <div class="bg">
    <div class="content">
      <div class="login">
        <div class="login-contianer">
          <div class="login-form">
            <img v-bind:src="logo" />
            <div class="login-form-under">
              <el-form label-width="90px" :model="userInfo" :rules="rules" ref="userInfo" @keypress="loginPress">
                <el-form-item label="ログインID" prop="userName">
                  <el-input
                    size="medium"
                    v-model="userInfo.userName"
                    v-focus
                    @blur="GetBushoList"
                    autocomplete="off">
                  </el-input>
                </el-form-item>
                <el-form-item label="パスワード" prop="passWord">
                  <el-input
                    type="password"
                    size="medium"
                    v-model="userInfo.passWord"
                    autocomplete="off"
                    show-password>
                  </el-input>
                </el-form-item>
                <el-form-item label="所属部署">
                  <el-select
                    v-model="userInfo.shozokuBusho"
                    @change="selectShozokuBusho"
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
let imgUrl = require("@/assets/imgs/logo2.png"); // 2022.02.18 Reafsロゴに変更
var $selectvalue;
export default {
  data() {
    var validateuserName= (rule, value, callback) => {
      if (value === '') {
        callback(new Error('担当者コードを入力してください'));
      } 
    };
    var validatepassword= (rule, value, callback) => {
        if (value === '') {
          callback(new Error('パスワードを入力してください'));
        }
    };

    return {
      loading: false,
      logo: imgUrl,
      codeImgSrc: "",
      userInfo: {
        userName: "",
        passWord: "",
        shozokuBushoCd: '',   //コード（ドロップダウンリストのキー）
        shozokuBushoName: '', //部門名（メニュー表示用）
        shozokuBusho: '',     //コードおよび名称（ドロップダウンリストのラベル）
      },  
      rules: {
        userName: [
          { validator: validateuserName, trigger: 'blur' }
        ],
        passWord: [
          { validator: validatepassword, trigger: 'blur' }
        ],
      },
      shozokuBushovalue: [],
      
    };
  },
  directives: {
    focus: {
      inserted: function (el) {
        el.focus();
      },
    },
  },
  created() {
    $selectvalue = this;
  },
  methods: {
    loginPress(e) {
      if (e.keyCode == 13) {
        this.login();
      }
    },
    login() {
      if (this.userInfo.userName == "" || this.userInfo.userName.trim() == "")
        return this.$message.error("担当者コードを入力してください。");
      if (this.userInfo.passWord == "" || this.userInfo.passWord.trim() == "")
        return this.$message.error("パスワードを入力してください。");

      this.loading = true;
      this.http
        .post("/api/user/login", this.userInfo, "アクセスしています....")
        .then((result) => {
          if (!result.status) {
            this.loading = false;
            return this.$message.error(result.message);
          }
          //this.$message.success("アクセス成功、ホームページへ遷移しています…");
          //ログイン担当者情報をVuexに保存する
          result.data.shozokuBushoName = this.userInfo.shozokuBushoName;
          this.$store.commit("setUserInfo", result.data);
          //アクセス成功後、インデックス画面へ遷移する
          this.$router.push({ path: "/" });
        });
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
    selectShozokuBusho(val){
      var obj = {};
      obj = $selectvalue.shozokuBushovalue.find(item =>{
        return item.コード === val;
      })
      this.userInfo.shozokuBushoName = obj.部門名
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  },
};
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
    width: 400px;
    min-height: 340px;
    background: white;
    height: 400px;
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
</style>