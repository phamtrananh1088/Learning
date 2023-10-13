<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <div v-if="defaultScreen" class="mainScreen">
          <h1>パスワードをお忘れの方</h1>
          <p>
            登録しているログインＩＤとメールアドレスを入力してください。<br />
            登録しているメールアドレスに仮パスワードを送付します。<br />
          </p>
          <el-form
            ref="userInfo"
            :model="userInfo"
            :rules="rules"
            label-width="130px"
            :hide-required-asterisk="true"
            @keypress="loginPress"
          >
            <el-form-item label="ログインID" prop="ユーザーＩＤ">
              <reafsinputtext
                maxlength="9"
                v-model="userInfo.ユーザーＩＤ"
                :width="'300px'"
                :required="true"
              ></reafsinputtext>
            </el-form-item>
            <el-form-item label="メールアドレス" prop="メールアドレス">
              <reafsinputtext
                maxlength="50"
                v-model="userInfo.メールアドレス"
                :width="'300px'"
                :required="true"
              ></reafsinputtext>
            </el-form-item>
          </el-form>
          <div class="note">
            <p>
              【お読みください】<br />
              ※迷惑メール対策をしている場合は、下記の受信設定をお願いいたします。<br />
              　・「xxxxxxxxx@xxx.co.jp」（※メールアドレス指定）<br />
              　・「@xxx.co.jp」（※ドメイン指定）<br />
              ※メールが届かない場合は、入力情報に誤りがある可能性があります。<br />
            </p>
          </div>
          <el-button
            style="margin-left: 10px; margin-bottom: 10px"
            type="primary"
            @click="onSubmit"
            :loading="loading"
          >
            <span v-if="!loading">仮パスワード発行</span>
            <span v-else>仮パスワード発行しています...</span>
          </el-button>
          <br />
          <!-- <router-link style="margin-left: 20px" :to="{ name: 'TD00000' }"
            >ログイン画面へ戻る</router-link
          > -->
            <el-link type="primary" @click="onBack" :loading="loading.loadingBack" >ログイン画面へ戻る</el-link>
        </div>
        <div v-else class="mainScreen">
          <div class="reLogin">
            <h1>パスワードをお忘れの方</h1>
            <p>
              登録しているメールアドレスに仮パスワードを送信しました。<br />
            </p>
            <!-- <router-link :to="{ name: 'TD00000' }"
              >ログイン画面へ戻る</router-link
            > -->
            <el-link type="primary" @click="onBack" :loading="loading.loadingBack" >ログイン画面へ戻る</el-link>
          </div>
        </div>
      </el-col>
      <el-col :span="12"> </el-col>
    </el-row>
  </div>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";

export default {
  components: {
    reafsinputtext,
  },
  data() {
    var validateユーザーID = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E000001").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validateメールアドレス = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E040172").then((response) => {
          callback(new Error(response.data));
        });
      } else {
        if (!this.base.isMail(value)) {
          this.getMsg("E020352").then((response) => {
            callback(new Error(response.data));
          });
        }
      }
    };

    return {
      loading: false,
      defaultScreen: true,
      userInfo: {
        ユーザーＩＤ: "",
        メールアドレス: "",
        UPDATE_HOST: this.base.getUpdateHost(),
      },
      rules: {
        ユーザーＩＤ: [{ validator: validateユーザーID, trigger: "blur" }],
        メールアドレス: [
          { validator: validateメールアドレス, trigger: "blur" },
        ],
      },
    };
  },

  mounted() {

  },

  created() {

  },

  methods: {

    showError(message) {
      this.$message.error({
        message,
        onClose: this.hideLoading,
      });
    },

    hideLoading() {
      this.loading = false;
    },

    /*     onClickLogout() {
      this.$store.commit("clearUserInfo", "");
      this.$store.commit('clearMenuInfo', '')
      this.$router.push({ name: "TD00000" });
    }, */

    async onSubmit() {
      this.loading = true;
      if (
        this.userInfo.ユーザーＩＤ == "" ||
        this.userInfo.ユーザーＩＤ.trim() == ""
      ) {
        this.getMsg("E000001").then((response) => {
          this.showError(response.data);
           this.MsgErr({
          title: "画面名を取得して表示",
          message: response.data
        });
        });
       
        return false;
      }
      if (
        this.userInfo.メールアドレス == "" ||
        this.userInfo.メールアドレス.trim() == ""
      ) {
        this.getMsg("E040172").then((response) => {
          this.showError(response.data);
        });
        return false;
      } else {
        if (!this.base.isMail(this.userInfo.メールアドレス)) {
          this.getMsg("E020352").then((response) => {
            this.showError(response.data);
          });
          return false;
        }
      }
      let res = {}
      await this.http
        .put("api/Reafs_T/PasuwadoRimainda/putM015", this.userInfo, 'アクセスしています....')
        .then((result) => {
          res = result;
          this.loading = false; 
          this.defaultScreen = false;
        })
      if (res.status) {
        //20221212 rep 待機メッセージを削除
         /*this.http
          .post('/api/Reafs_T/PasuwadoRimainda/メール送信', this.userInfo, 'アクセスしています....')
          .then((result) => {
        }) */
        this.http.post('/api/Reafs_T/PasuwadoRimainda/メール送信', this.userInfo)
        //20221212 rep
      }
    },

    loginPress(e) {
      if (e.keyCode == 13) {
        this.onSubmit();
      }
    },

    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },
    async onBack () {
      window.parent.postMessage({
        'call': 'closePopup'
      }, '*')
    }
  },
};
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 30px;
  &:last-child {
    margin-bottom: 0;
  }
}

.el-col {
  border-radius: 4px;
}

.home-container {
  padding: 10px;
  height: 100%;
}

.mainScreen {
  line-height: 38px;
  padding-left: 10px;
}

.note {
  line-height: 34px;
  border-radius: 15px;
  background-color: lightsalmon;
  padding: 5px;
  padding-left: 20px;
  margin-top: 20px;
  margin-bottom: 20px;
  width: 570px;
}
</style>
