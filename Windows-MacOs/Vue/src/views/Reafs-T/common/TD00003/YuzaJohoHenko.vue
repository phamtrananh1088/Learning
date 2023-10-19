<template>
  <div class="home-container YuzaJohoHenko">
    <el-row :gutter="20">
      <el-col :span="24">
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
          <div class="alertPassword">
            <p v-if="iDispDate < 0">※パスワードの有効期限を超えています</p>
            <p v-else-if="iDispDate >= 0 && iDispDate <= 60">
              ※パスワードの有効期限切れまで{{ iDispDate }}日です
            </p>
          </div>
          <el-form-item label="現在のパスワード" prop="現在のパスワード">
            <el-input
              maxlength="30"
              type="password"
              class="el-input-form-required"
              v-model="userInfo.現在のパスワード"
              show-password
            ></el-input>
            <span class="startDate"
              >前回設定日 : {{ userInfo.パスワード期限開始 }}</span
            >
            <span class="endDate"
              >有効期限 : {{ userInfo.パスワード期限終了 }}</span
            >
          </el-form-item>
          <el-form-item label="新しいパスワード" prop="新しいパスワード">
            <el-input
              maxlength="30"
              type="password"
              class="el-input-form-required"
              v-model="userInfo.新しいパスワード"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item
            class="lastChildFormItem"
            label="新しいパスワード"
            prop="新しいパスワード確認"
          >
            <el-input
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
          <span v-if="!loading">更新</span>
          <span v-else>更新しています...</span>
        </el-button>
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
    var oneInthree = () => {
      if (
        this.userInfo.現在のパスワード.trim() != "" ||
        this.userInfo.新しいパスワード.trim() != "" ||
        this.userInfo.新しいパスワード確認.trim() != ""
      ) {
        return true;
      } else {
        return false;
      }
    };
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
      if (oneInthree()) {
        if (value === "") {
          this.getMsg("E020343").then((response) => {
            callback(new Error(response.data));
          });
          return false;
        }
      }
    };
    var validate新しいパスワード = (rule, value, callback) => {
      if (oneInthree()) {
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
      }
    };
    var validate新しいパスワード確認 = (rule, value, callback) => {
      if (oneInthree()) {
        if (value === "") {
          this.getMsg("E020346").then((response) => {
            callback(new Error(response.data));
          });
        }
      }
    };

    return {
      loading: false,
      iDispDate: 0,
      userInfo: {
        ユーザーＩＤ: "",
        ユーザー名: "",
        メールアドレス: "",
        部署等: "",
        現在のパスワード: "",
        新しいパスワード: "",
        新しいパスワード確認: "",
        パスワード期限開始: "",
        パスワード期限終了: "",
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

  created() {
    this.init();
  },

  mounted() {
  },

  methods: {
    init() {
      let userLogIn = this.$store.getters.getUserInfo();
      this.userInfo.ユーザーＩＤ = userLogIn.userId;
      this.userInfo.会社コード = userLogIn.会社コード;
      this.userInfo.現在のパスワード = "";
      this.userInfo.新しいパスワード = "";
      this.userInfo.新しいパスワード確認 = "";
      this.http
        .post("/api/Reafs_T/YuzaJohoHenko/getm015", this.userInfo)
        .then((response) => {
          this.userInfo.ユーザー名 = response.data.ユーザー名;
          this.userInfo.メールアドレス = response.data.メールアドレス;
          this.userInfo.部署等 = response.data.部署等;
          this.userInfo.パスワード期限開始 = response.data.パスワード期限開始;
          this.userInfo.パスワード期限終了 = response.data.パスワード期限終了;
          this.iDispDate = this.checkDispAlert();
        });
    },

    checkDispAlert() {
      let d期限終了 = new Date(this.userInfo.パスワード期限終了);
      let sCurrDate = this.base.formatCurrDate();
      let dCurrDate = new Date(sCurrDate);
      let iResult = Math.trunc((d期限終了 - dCurrDate) / 1000 / 3600 / 24);
      return iResult;
    },

    showError(message) {
      this.$message.error({
        message,
        onClose: this.hideLoading,
      });
    },

    showSuccess(message) {
      this.$message.success({
        message,
        onClose: this.hideLoading,
      });
    },

    hideLoading() {
      this.loading = false;
    },

    onClickBack() {
      //this.$router.push({ name: "TD00001" });
      //window.location.href = "/Reafs_T/MainMenu";
      this.$router.back()
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
        this.userInfo.現在のパスワード.trim() !== "" ||
        this.userInfo.新しいパスワード.trim() !== "" ||
        this.userInfo.新しいパスワード確認.trim() !== ""
      ) {
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
          "api/Reafs_T/YuzaJohoHenko/checkpassword",
          this.userInfo
        );

        if (!checkPass.status) {
          this.showError(checkPass.message);
          return false;
        }
      }

      this.http
        .put(
          "api/Reafs_T/YuzaJohoHenko/putm015",
          this.userInfo,
          "アクセスしています...."
        )
        .then((result) => {
          if (!result.status) {
            this.showError(result.message);
            return false;
          }
          this.init();
          this.showSuccess(result.message);
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

.alertPassword {
  color: red;
}

.startDate {
  margin-left: 60px;
}

.endDate {
  margin-left: 40px;
}
</style>

<style scoped>
.YuzaJohoHenko .el-input-form-required {
  width: 300px;
}
</style>
