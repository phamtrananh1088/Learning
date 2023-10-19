<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="24">
        <div>
          <p>
            ようこそ。<br />
            システムにログインするための、新しいパスワードを入力し、登録ボタンをクリックしてください。<br />
            ※内容は、ログイン後に「ユーザー情報変更」画面から変更ができます。<br />
          </p>
        </div>
        <el-form
          ref="userInfo"
          :model="userInfo"
          :rules="rules"
          label-width="130px"
          :hide-required-asterisk="true"
          @keypress="loginPress"
        >
          <el-form-item label="ユーザー名" prop="ユーザー名">
            <reafsinputtext
              maxlength="40"
              v-model="userInfo.ユーザー名"
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
          <el-form-item label="部署等（メモ）" prop="部署等">
            <reafsinputtext
              maxlength="40"
              v-model="userInfo.部署等"
              :width="'300px'"
            ></reafsinputtext>
          </el-form-item>
          <el-divider></el-divider>
          <el-form-item
            label="現在のパスワード"
            prop="現在のパスワード"
            class="is-required"
          >
            <el-input
              required
              maxlength="30"
              type="password"
              class="el-input-form-required"
              v-model="userInfo.現在のパスワード"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item
            label="新しいパスワード"
            prop="新しいパスワード"
            class="is-required"
          >
            <el-input
              required
              maxlength="30"
              type="password"
              class="el-input-form-required"
              v-model="userInfo.新しいパスワード"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item
            class="is-required lastChildFormItem"
            label="新しいパスワード"
            prop="新しいパスワード確認"
          >
            <el-input
              required
              maxlength="30"
              type="password"
              class="el-input-form-required"
              v-model="userInfo.新しいパスワード確認"
              show-password
            ></el-input>
          </el-form-item>
          <label>（確認の為）</label>
          <p>
            パスワードは、英字（大文字・小文字）、数値、記号のうち3種以上を<br />
            混在させた8桁以上で入力してください<br />
            前回を含む過去3回のパスワードは利用できません<br />
          </p>
        </el-form>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="7">
        <p />
      </el-col>
      <el-col :span="11">
        <el-button type="primary" @click="onSubmit" :loading="loading">
          <span v-if="!loading">登録</span>
          <span v-else>登録しています...</span>
        </el-button>
        <p>※登録完了後、メニュー画面を表示します。</p>
      </el-col>
      <el-col :span="2">
        <el-button type="primary" @click="onClickBack">戻る </el-button>
      </el-col>
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
    var validateユーザー名 = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E040663").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validateメールアドレス = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E040172").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validate現在のパスワード = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E020343").then((response) => {
          callback(new Error(response.data));
        });
        return false;
      }
    };
    var validate新しいパスワード = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E020345").then((response) => {
          callback(new Error(response.data));
        });
      } else {
        if (value.length < 8) {
          this.getMsg("E020349").then((response) => {
            callback(new Error(response.data));
          });
          return false;
        } else {
          if (!this.base.checkPass(value, 1)) {
            this.getMsg("E040664").then((response) => {
              callback(new Error(response.data));
            });
            return false;
          }
          if (!this.base.checkPass(value, 2)) {
            this.getMsg("E040665").then((response) => {
              callback(new Error(response.data));
            });
            return false;
          }
        }
      }
    };
    var validate新しいパスワード確認 = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E020346").then((response) => {
          callback(new Error(response.data));
        });
        return false;
      }
    };

    return {
      loading: false,
      userInfo: {
        ユーザーＩＤ: "",
        ユーザー名: "",
        メールアドレス: "",
        部署等: "",
        現在のパスワード: "",
        新しいパスワード: "",
        新しいパスワード確認: "",
        会社コード: "",
      },
      rules: {
        ユーザー名: [{ validator: validateユーザー名, trigger: "blur" }],
        メールアドレス: [
          { validator: validateメールアドレス, trigger: "blur" },
        ],
        現在のパスワード: [
          { validator: validate現在のパスワード, trigger: "blur" },
        ],
        新しいパスワード: [
          { validator: validate新しいパスワード, trigger: "blur" },
        ],
        新しいパスワード確認: [
          { validator: validate新しいパスワード確認, trigger: "blur" },
        ],
      },
    };
  },
  inject: ["hideLeftMenu", "showLogoutButton"],

  created() {
    this.init();
  },

  mounted() {
    this.hideLeftMenu(false);
    this.showLogoutButton(true);
  },

  methods: {
    init() {
      let userLogIn = this.$store.getters.getUserInfo();
      this.userInfo.ユーザーＩＤ = userLogIn.userId;
      this.userInfo.会社コード = userLogIn.会社コード;
      this.http
        .post("/api/Reafs_T/ShokaiTouroku/getm015", this.userInfo)
        .then((response) => {
          this.userInfo.ユーザー名 = response.data.ユーザー名;
          this.userInfo.メールアドレス = response.data.メールアドレス;
          this.userInfo.部署等 = response.data.部署等;
        });
    },

    showError(message) {
      this.$message.error({
        message,
        onClose: this.hideLoading,
      });
    },

    hideLoading() {
      this.loading = false;
    },

    onClickBack() {
      //this.hideLeftMenu(true);
      //this.$router.push({ name: "TD00000" });
      //this.$router.back();
      this.$store.commit('clearUserInfo', '')
      this.$store.commit('clearMenuInfo', '')
      window.location.href = this.http.resolve("/Reafs_T");
    },

    async onSubmit() {
      this.loading = true;
      if (
        this.userInfo.ユーザー名 == "" ||
        this.userInfo.ユーザー名.trim() == ""
      ) {
        this.getMsg("E040663").then((response) => {
          this.showError(response.data);
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
      }
      if (
        this.userInfo.現在のパスワード == "" ||
        this.userInfo.現在のパスワード.trim() == ""
      ) {
        this.getMsg("E020343").then((response) => {
          this.showError(response.data);
        });
        return false;
      }
      if (
        this.userInfo.新しいパスワード == "" ||
        this.userInfo.新しいパスワード.trim() == ""
      ) {
        this.getMsg("E020345").then((response) => {
          this.showError(response.data);
        });
        return false;
      } else {
        if (this.userInfo.新しいパスワード.length < 8) {
          this.getMsg("E020349").then((response) => {
            this.showError(response.data);
          });
          return false;
        } else {
          if (!this.base.checkPass(this.userInfo.新しいパスワード, 1)) {
            this.getMsg("E040664").then((response) => {
              this.showError(response.data);
            });
            return false;
          }
          if (!this.base.checkPass(this.userInfo.新しいパスワード, 2)) {
            this.getMsg("E040665").then((response) => {
              this.showError(response.data);
            });
            return false;
          }
        }
      }
      if (
        this.userInfo.新しいパスワード確認 == "" ||
        this.userInfo.新しいパスワード確認.trim() == ""
      ) {
        this.getMsg("E020346").then((response) => {
          this.showError(response.data);
        });
        return false;
      }
      if (
        this.userInfo.新しいパスワード !== this.userInfo.新しいパスワード確認
      ) {
        this.getMsg("E020347").then((response) => {
          this.showError(response.data);
        });
        return false;
      }
      const checkPass = await this.http.post(
        "api/Reafs_T/ShokaiTouroku/checkpassword",
        this.userInfo
      );

      if (!checkPass.status) {
        this.showError(checkPass.message);
        return false;
      }

      this.http
        .put(
          "api/Reafs_T/ShokaiTouroku/putm015",
          this.userInfo,
          "アクセスしています...."
        )
        .then((result) => {
          this.loading = false;
          if (!result.status) {
            this.showError(result.message);
            return false;
          }
          this.$router.push({ name: "TD00001" });
        });
    },

    loginPress(e) {
      if (e.keyCode == 13) {
        this.onSubmit();
      }
    },

    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },
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
.lastChildFormItem {
  margin-bottom: 5px;
}
.el-col {
  border-radius: 4px;
}

.home-container {
  padding: 10px;
  height: 100%;
}
</style>

<style scoped>
.el-input-form-required {
  width: 300px;
}
</style>
