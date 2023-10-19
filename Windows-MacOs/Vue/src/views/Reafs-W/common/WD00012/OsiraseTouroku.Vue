<template>
  <div class="home-contianer onloadCon OsiraseTouroku" ref="onloadCon">
    <!-- 検索部 -->
    <el-form
      :model="oshiraseHyoji"
      ref="oshiraseHyoji"
      label-width="120px"
      class="search-form-class"
    >
      <el-row>
        <el-col :span="21" :xs="24">
          <!-- layout  xs:<768px sm:>=768px md:>=992px lg:>=1200px xl:>=1920px-->
          <el-col :span="24" :xs="12">
            <el-form-item label="掲示ID" prop="掲示ID">
              <reafsinputtext
                id="txtKeijiId"
                v-model="oshiraseHyoji.掲示ID"
                :width="'120px'"
                maxlength="8"
                @change="txtKeijiId_Changed()"
                :readonly="oshiraseHyoji.readOnlyKbn"
              ></reafsinputtext>
            </el-form-item>

            <el-form-item label="適用日" prop="適用日">
              <div style="display: flex">
                <el-date-picker v-model="oshiraseHyoji.適用日" ref="ref適用日" required type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                  class="date-picker date-range" />

                <el-checkbox
                  v-model="oshiraseHyoji.重要フラグ"
                  style="margin-left: 17px"
                ></el-checkbox>
                <label style="margin-left: -20px"
                  >重要度の高い掲示の場合、チェックを入れて下さい。</label
                >
              </div>
            </el-form-item>

            <el-form-item label="掲示期間" prop="掲示期間">
              <div style="display: flex;">
                <el-date-picker v-model="oshiraseHyoji.掲示開始日" ref="ref掲示開始日" required type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                  class="date-picker date-range" />
                <span style="margin: 0 2px">～</span>
                <el-date-picker v-model="oshiraseHyoji.掲示終了日" ref="ref掲示終了日" required type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                  class="date-picker date-range" />
              </div>
            </el-form-item>
            <el-col :span="16">
              <el-form-item label="件名" prop="件名">
                <reafsinputtext
                  id="txtKenmei"
                  :required="true"
                  v-model="oshiraseHyoji.件名"
                  :width="'100%'"
                  maxlength="50"
                  ref="ref件名"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item
                label="内容"
                prop="内容"
                class="textarea-class"
              >
                <el-input
                  id="txtNaiyo"
                  type="textarea"
                  v-model="oshiraseHyoji.内容"
                  maxlength="400"
                  show-word-limit
                  :autosize="{ minRows: 8 }"
                  ref="ref内容"
                >
                </el-input>
              </el-form-item>
              <el-form-item label="登録組織" prop="登録組織" class="gray">
                <div style="display: flex">
                  <reafsinputtext
                    maxlength="19"
                    v-model="oshiraseHyoji.登録組織コード"
                    :width="'188px'"
                    :readonly="true"
                    ref="ref登録組織コード"
                  >
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="oshiraseHyoji.登録組織名称"
                    :width="'100%'"
                    class="is-readonly"
                    disabled="disabled"
                  >
                  </reafsinputtext>
                </div>
              </el-form-item>

              <el-form-item label="登録者" prop="登録者" class="gray">
                <div style="display: flex">
                  <reafsinputtext
                    maxlength="7"
                    v-model="oshiraseHyoji.登録担当者ID"
                    :width="'100px'"
                    :readonly="true"
                    ref="ref登録組織コード"
                  >
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="oshiraseHyoji.登録者名称"
                    :width="'100%'"
                    class="is-readonly"
                    disabled="disabled"
                  >
                  </reafsinputtext>
                </div>
              </el-form-item>
              <el-form-item label="添付" prop="添付">
                <div
                  style="display: flex; align-content: center; padding-top: 1px"
                >
                  <div class="img-label" style="width: 80%">
                    <label>{{ oshiraseHyoji.添付ファイル名 }}</label>
                    <a
                      style="padding-left: 15px"
                      ref="privacyPolicy"
                      href="#"
                      @click.prevent="onDownload()"
                      >{{ oshiraseHyoji.フルパス }}</a
                    >
                  </div>
                  <div>
                    <el-button
                      style="margin-left: 2px"
                      size="mini"
                      @click="openFileDialog()"
                      :loading="loading['btnUploadFile']"
                      >参照</el-button
                    >
                  </div>
                  <div>
                    <el-button
                      style="margin-left: 2px"
                      size="mini"
                      @click="onDelete()"
                      :loading="loading['btnDelete']"
                      >削除</el-button
                    >
                  </div>
                </div>
                <div v-if="dragArea1" class="transition-box" style="width: 80%">
                  <el-upload
                    drag
                    :multiple="false"
                    action=""
                    :on-change="dragChange"
                    :show-file-list="true"
                    :auto-upload="false"
                    :file-list="fileList"
                  >
                    <i class="el-icon-upload" z-index="-1"></i>
                    <div class="el-upload__text">ここにファイルをドロップ</div>
                  </el-upload>
                </div>
              </el-form-item>
              <el-form-item label="適用範囲" prop="適用範囲">
                <el-checkbox-group v-model="checkList">
                  <el-checkbox label="Reafs-R"></el-checkbox>
                  <el-checkbox label="Reafs-W"></el-checkbox>
                  <el-checkbox label="Reafs-T"></el-checkbox>
                </el-checkbox-group>
              </el-form-item>
              <el-row style="margin-top: 50px">
                <el-button @click="toroku_Click()"
                  >登録</el-button
                >
                <el-button @click="btnSakujo_Click()"
                  >削除</el-button
                >
                <el-button @click="onClickBack" style="float: right">戻る </el-button>
              </el-row>
            </el-col>
          </el-col>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import moment from 'moment'

var $thisValue;
var $userInfo = {
  ユーザーＩＤ: "",
  ユーザー名: "",
  登録組織名称: "",
  登録組織コード: "",
};

export default {
  components: {
    reafsinputtext,
    moment
  },

  data() {
    return {
      oshiraseHyoji: {
        掲示ID: "",
        readOnlyKbn: false,
        適用日: "",
        重要フラグ: false,
        掲示開始日: "",
        掲示終了日: "",
        掲示期間: "",
        件名: "",
        内容: "",
        登録組織コード: "",
        登録組織名称: "",
        登録担当者ID: "",
        登録者名称: "",
        添付ファイル名: "",
        フルパス: "",
        Ｒ参照区分: "",
        Ｔ参照区分: "",
        Ｗ参照区分: "",
      },
      checkList: [],
      loading: {
        btnRegister: false,
        loadingBack: false,
        btnUploadFile: false,
        btnDelete: false,
        refDl: false,
      },
      pageData: {
        ファイルNo: "",
        ファイルパス: "",
        ファイルパス2: "",
        ファイル名: "",
        ルートパス: "",
        添付元ファイル名: "",
        物理ファイル名: "",
        物理ファイル名2: "",
        状況コメント: "",
        部位コメント: "",
        linkImage: "",
        linkDownload: "",
        添付NO: "",
        imgUploadName: "",
      },
      fileList: [],
      dropActive: false,
      dragArea1: false,
      MSC_5MB_LengthByte: 5245329,
      MSC_300MB_LengthByte: 314572800, // 300MBの判定を追加
    };
  },
  created() {
    let userLogIn = this.$store.getters.getUserInfo();
    $userInfo.ユーザー名 = userLogIn.userName;

    this.http
      .get("/api/Reafs_W/Common/OshiraseHyoji/GetSoshikiName")
      .then((response) => {
        $userInfo.ユーザーＩＤ = response.data.ユーザーＩＤ;
        $userInfo.登録組織コード = response.data.登録組織コード;
        $userInfo.登録組織名称 = response.data.登録組織名称;

        if (
          this.$route.query.掲示ID != null &&
          this.$route.query.掲示ID.toString().trim().length > 0
        ) {
          this.GetData(this.$route.query.掲示ID);
        } else {
          this.init();
        }
      });
  },

  mounted() {
    const dropArea1 = this.$refs.onloadCon;
    var lastenter = null;
    dropArea1.addEventListener("dragenter", (e) => {
      lastenter = e.target; // 记录最后进入的元素
      this.dragArea1 = true;
    });
    dropArea1.addEventListener("dragleave", (e) => {
      if (lastenter === e.target) {
        this.dragArea1 = false;
        e.stopPropagation();
        e.preventDefault();
      }
    });
    dropArea1.addEventListener("drop", (e) => {
      this.dragArea1 = false;
    });

    this.$nextTick(function () {
      this.setfocus_KeijiId();
    });
  },

  watch: {
    $route(to, from) {
      if (to.name == "WD00012") {
        let userLogIn = this.$store.getters.getUserInfo();
        $userInfo.ユーザー名 = userLogIn.userName;

        this.http
          .get("/api/Reafs_W/Common/OshiraseHyoji/GetSoshikiName")
          .then((response) => {
            $userInfo.ユーザーＩＤ = response.data.ユーザーＩＤ;
            $userInfo.登録組織コード = response.data.登録組織コード;
            $userInfo.登録組織名称 = response.data.登録組織名称;

            if (
              this.$route.query.掲示ID != null &&
              this.$route.query.掲示ID.toString().trim().length > 0
            ) {
              this.GetData(this.$route.query.掲示ID);
            } else {
              this.init();
              this.oshiraseHyoji.readOnlyKbn = true;
            }
          });
      }
    },
  },

  methods: {
    init() {
      this.oshiraseHyoji.掲示ID = "";
      this.oshiraseHyoji.適用日 = this.base.formatCurrDate();
      this.oshiraseHyoji.掲示開始日 = this.base.formatCurrDate();
      this.oshiraseHyoji.掲示終了日 = this.getDay(this.base.formatCurrDate(), 10);
      this.oshiraseHyoji.重要フラグ = false;
      this.oshiraseHyoji.添付ファイル名 = "";
      this.oshiraseHyoji.フルパス = "";
      this.pageData.添付NO = "";
      this.pageData.添付元ファイル名 = "";
      this.oshiraseHyoji.件名 = "";
      this.oshiraseHyoji.内容 = "";
      this.oshiraseHyoji.登録担当者ID = $userInfo.ユーザーＩＤ;
      this.oshiraseHyoji.登録者名称 = $userInfo.ユーザー名;
      this.oshiraseHyoji.登録組織コード = $userInfo.登録組織コード;
      this.oshiraseHyoji.登録組織名称 = $userInfo.登録組織名称;
      this.checkList = [];
    },
    txtKeijiId_Changed() {
      if (!this.isSimpleNum(this.oshiraseHyoji.掲示ID)) {
        this.MsgErr({
          title: this.$store.getters.getPageTitle(),
          message: "掲示IDを半角数値入力して下さい。",
        });
        return;
      }
      if (
        this.oshiraseHyoji.掲示ID == null ||
        this.oshiraseHyoji.掲示ID.toString().trim() == ""
      ) {
        this.init();
      } else {
        this.GetData(this.oshiraseHyoji.掲示ID.toString().trim());
      }
    },
    async GetSoshikiName() {
      await this.http
        .get("/api/Reafs_W/Common/OshiraseHyoji/GetSoshikiName")
        .then((response) => {
          $userInfo.ユーザーＩＤ = response.data.ユーザーＩＤ;
          $userInfo.登録組織コード = response.data.登録組織コード;
          $userInfo.登録組織名称 = response.data.登録組織名称;
        });
    },
    async GetData(s掲示ID) {
      this.oshiraseHyoji.掲示ID = s掲示ID;
      await this.GetSoshikiName();
      await this.http
        .get("/api/Reafs_W/Common/OshiraseHyoji/GetNoti", this.oshiraseHyoji)
        .then((response) => {
          if (!response.status) {
            this.init();
            this.setfocus_KeijiId();
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }

          this.oshiraseHyoji = response.data;
          if (
            response.data.添付ファイル名 == null ||
            response.data.添付ファイル名.toString().trim().length == 0
          ) {
            this.oshiraseHyoji.フルパス = "";
          }

          if (response.data.重要フラグ == "1") {
            this.oshiraseHyoji.重要フラグ = true;
          }
          let arr1 = [];
          if (response.data.Ｒ参照区分 == "1") {
            arr1.push("Reafs-R");
          }
          if (response.data.Ｔ参照区分 == "1") {
            arr1.push("Reafs-T");
          }
          if (response.data.Ｗ参照区分 == "1") {
            arr1.push("Reafs-W");
          }
          this.checkList = arr1;

          if (
            response.data.掲示開始日 != "" &&
            response.data.掲示開始日 != null
          ) {
            this.oshiraseHyoji.掲示開始日 = response.data.掲示開始日;
          } else {
            this.oshiraseHyoji.掲示開始日 = "";
          }
          if (
            response.data.掲示終了日 != "" &&
            response.data.掲示終了日 != null
          ) {
            this.oshiraseHyoji.掲示終了日 = response.data.掲示終了日;
          } else {
            this.oshiraseHyoji.掲示終了日 = "";
          }

          this.pageData.添付元ファイル名 = response.data.添付ファイル名;
          this.pageData.ファイル名 = response.data.添付ファイル名;
        });
    },
    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },
    toroku_Check() {
      if (this.oshiraseHyoji.掲示ID.toString().trim().length > 0) {
        if (this.oshiraseHyoji.掲示ID != this.$route.query.掲示ID) {
          this.MsgErr({
            title: this.$store.getters.getPageTitle(),
            message: "登録に失敗しました。", //E040059
          });
          return false;
        }
      }

      if (
        this.oshiraseHyoji.適用日 == null ||
        this.oshiraseHyoji.適用日.toString().trim().length == 0
      ) {
        this.showError("適用日を正しく入力して下さい。", {
          callback: () => {
            this.$refs.ref適用日.focus()
          }
        })
        return false;
      }

      if (
        this.oshiraseHyoji.掲示開始日 == null ||
        this.oshiraseHyoji.掲示開始日.toString().trim().length == 0
      ) {
        this.showError("掲示日（ＳＴ）を正しく入力して下さい。", {
          callback: () => {
            this.$refs.ref掲示開始日.focus()
          }
        })
        return false;
      }

      if (
        this.oshiraseHyoji.掲示終了日 == null ||
        this.oshiraseHyoji.掲示終了日.toString().trim().length == 0
      ) {
        this.showError("掲示日（ＥＤ）を正しく入力して下さい。", {
          callback: () => {
            this.$refs.ref掲示終了日.focus()
          }
        })
        return false;
      }

      if (this.oshiraseHyoji.掲示開始日 && this.oshiraseHyoji.掲示終了日 &&
          moment(this.oshiraseHyoji.掲示開始日) > moment(this.oshiraseHyoji.掲示終了日)) {
        this.getMsg('E010352').then((response) => {
          this.showError(response.data, {
            callback: () => {
              this.$refs.ref掲示開始日.focus()
            }
          })
        })
        return false
      }

      if (
        this.oshiraseHyoji.件名 == null ||
        this.oshiraseHyoji.件名.toString().trim().length == 0
      ) {
        this.showError("件名を入力して下さい。", {
          callback: () => {
            this.$refs.ref件名.focus()
          }
        })
        return false;
      }

      if (
        this.oshiraseHyoji.内容 == null ||
        this.oshiraseHyoji.内容.toString().trim().length == 0
      ) {
        this.showError("内容を入力して下さい。", {
          callback: () => {
            this.$refs.ref内容.focus()
          }
        })
        return false;
      }

      if (
        !this.checkList.includes("Reafs-R") &&
        !this.checkList.includes("Reafs-W") &&
        !this.checkList.includes("Reafs-T")
      ) {
        this.MsgErr({
          title: this.$store.getters.getPageTitle(),
          message: "適用範囲を選択して下さい。", //E010365
        });
        return false;
      }
      return true;
    },
    Sakujo_Check() {
      if (
        this.oshiraseHyoji.掲示ID == null ||
        this.oshiraseHyoji.掲示ID.toString().trim().length == 0 ||
        this.oshiraseHyoji.掲示ID != this.$route.query.掲示ID
      ) {
        this.MsgErr({
          title: this.$store.getters.getPageTitle(),
          message: "削除に失敗しました。", //E040012
        });
        return false;
      }

      return true;
    },
    async toroku_Click() {
      $thisValue = this;

      if (!this.toroku_Check()) {
        return;
      }

      let sMsgConf = "登録しますがよろしいですか？"; //(K000004)
      this.getMsg("K000004").then((response) => {
            this.MsgConf(
              {
                title: this.$store.getters.getPageTitle(),
                message: response.data,
              },
              () => {
                // OKが選択された場合
                let url = "";
                if (this.oshiraseHyoji.掲示ID.toString().trim().length > 0) {
                  url = "/api/Reafs_W/Common/OshiraseHyoji/Update";
                } else {
                  url = "/api/Reafs_W/Common/OshiraseHyoji/Insert";
                }

                const param = {
                  掲示ID: this.oshiraseHyoji.掲示ID,
                  適用日: this.oshiraseHyoji.適用日,
                  掲示開始日: this.oshiraseHyoji.掲示開始日,
                  掲示終了日: this.oshiraseHyoji.掲示終了日,
                  重要フラグ: this.oshiraseHyoji.重要フラグ ? 1 : 0,
                  件名: this.oshiraseHyoji.件名,
                  内容: this.oshiraseHyoji.内容,
                  登録組織コード: this.oshiraseHyoji.登録組織コード,
                  添付ファイル名: this.oshiraseHyoji.添付ファイル名,
                  登録担当者ID: this.oshiraseHyoji.登録担当者ID,
                  Ｒ参照区分: this.checkList.includes("Reafs-R") ? 1 : 0,
                  Ｔ参照区分: this.checkList.includes("Reafs-T") ? 1 : 0,
                  Ｗ参照区分: this.checkList.includes("Reafs-W") ? 1 : 0,
                  添付NO: this.pageData.添付NO,
                };

                this.http.post(url, param).then((response) => {
                  if (!response.status) {
                    return this.MsgInfo({
                      title: this.$store.getters.getPageTitle(),
                      message: response.message,
                    });
                  }else{
                    this.onJumpTo();
                    return this.MsgInfo({
                      title: this.$store.getters.getPageTitle(),
                      message: "登録しました。", //M000008
                    });
                  }
                });
                return;
              },
              () => {
                // Cancelが選択された場合
                return;
              }
            );
          });
      // if (await this.confirm(sMsgConf)) {
      //   let url = "";
      //   if (this.oshiraseHyoji.掲示ID.toString().trim().length > 0) {
      //     url = "/api/Reafs_W/Common/OshiraseHyoji/Update";
      //   } else {
      //     url = "/api/Reafs_W/Common/OshiraseHyoji/Insert";
      //   }

      //   const param = {
      //     掲示ID: this.oshiraseHyoji.掲示ID,
      //     適用日: this.oshiraseHyoji.適用日,
      //     掲示開始日: this.oshiraseHyoji.掲示期間[0],
      //     掲示終了日: this.oshiraseHyoji.掲示期間[1],
      //     重要フラグ: this.oshiraseHyoji.重要フラグ ? 1 : 0,
      //     件名: this.oshiraseHyoji.件名,
      //     内容: this.oshiraseHyoji.内容,
      //     登録組織コード: this.oshiraseHyoji.登録組織コード,
      //     添付ファイル名: this.oshiraseHyoji.添付ファイル名,
      //     登録担当者ID: this.oshiraseHyoji.登録担当者ID,
      //     Ｒ参照区分: this.checkList.includes("Reafs-R") ? 1 : 0,
      //     Ｔ参照区分: this.checkList.includes("Reafs-T") ? 1 : 0,
      //     Ｗ参照区分: this.checkList.includes("Reafs-W") ? 1 : 0,
      //     添付NO: this.pageData.添付NO,
      //   };

      //   await this.http.post(url, param).then((response) => {
      //     if (!response.status) {
      //       return this.MsgInfo({
      //         title: this.$store.getters.getPageTitle(),
      //         message: response.message,
      //       });
      //     }
      //   });

      //   this.onJumpTo();
      //   return this.MsgInfo({
      //     title: this.$store.getters.getPageTitle(),
      //     message: "登録しました。", //M000008
      //   });
      // }
    },
    async btnSakujo_Click() {
      if (!this.Sakujo_Check()) {
        return;
      }

      let sMsgConf = "削除しますがよろしいですか？"; //(K000002)
      // 確認メッセージ
      this.getMsg("K000002").then((response) => {
        this.MsgConf(
          {
            title: this.$store.getters.getPageTitle(),
            message: response.data,
          },
          () => {
            // OKが選択された場合
            const param = {
              掲示ID: this.oshiraseHyoji.掲示ID,
              適用日: this.oshiraseHyoji.適用日,
              掲示開始日: this.oshiraseHyoji.掲示開始日,
              掲示終了日: this.oshiraseHyoji.掲示終了日,
              重要フラグ: this.oshiraseHyoji.重要フラグ ? 1 : 0,
              件名: this.oshiraseHyoji.件名,
              内容: this.oshiraseHyoji.内容,
              登録組織コード: this.oshiraseHyoji.登録組織コード,
              添付ファイル名: this.oshiraseHyoji.添付ファイル名,
              登録担当者ID: this.oshiraseHyoji.登録担当者ID,
              Ｒ参照区分: this.checkList.includes("Reafs-R") ? 1 : 0,
              Ｔ参照区分: this.checkList.includes("Reafs-T") ? 1 : 0,
              Ｗ参照区分: this.checkList.includes("Reafs-W") ? 1 : 0,
            };

            this.http
              .post("/api/Reafs_W/Common/OshiraseHyoji/Delete", param)
              .then((response) => {
                if (!response.status) {
                  return this.MsgInfo({
                    title: this.$store.getters.getPageTitle(),
                    message: response.message,
                  });
                }else{
                  this.onJumpTo();
                  return this.MsgInfo({
                    title: this.$store.getters.getPageTitle(),
                    message: "削除しました。", //M000009
                  });
                }
                return;
              });
          },
          () => {
            // Cancelが選択された場合
            return;
          }
        );
      });
      // if (await this.confirm(sMsgConf)) {
      //   const param = {
      //     掲示ID: this.oshiraseHyoji.掲示ID,
      //     適用日: this.oshiraseHyoji.適用日,
      //     掲示開始日: this.oshiraseHyoji.掲示期間[0],
      //     掲示終了日: this.oshiraseHyoji.掲示期間[1],
      //     重要フラグ: this.oshiraseHyoji.重要フラグ ? 1 : 0,
      //     件名: this.oshiraseHyoji.件名,
      //     内容: this.oshiraseHyoji.内容,
      //     登録組織コード: this.oshiraseHyoji.登録組織コード,
      //     添付ファイル名: this.oshiraseHyoji.添付ファイル名,
      //     登録担当者ID: this.oshiraseHyoji.登録担当者ID,
      //     Ｒ参照区分: this.checkList.includes("Reafs-R") ? 1 : 0,
      //     Ｔ参照区分: this.checkList.includes("Reafs-T") ? 1 : 0,
      //     Ｗ参照区分: this.checkList.includes("Reafs-W") ? 1 : 0,
      //   };

      //   await this.http
      //     .post("/api/Reafs_W/Common/OshiraseHyoji/Delete", param)
      //     .then((response) => {
      //       if (!response.status) {
      //         return this.MsgInfo({
      //           title: this.$store.getters.getPageTitle(),
      //           message: response.message,
      //         });
      //       }
      //     });
      //   this.onJumpTo();
      //   return this.MsgInfo({
      //     title: this.$store.getters.getPageTitle(),
      //     message: "削除しました。", //M000009
      //   });
      // }
    },
    setfocus_KeijiId() {
      var txt = document
        .getElementById("txtKeijiId")
        .getElementsByClassName("el-input__inner")[0];
      txt.focus();
    },
    daterangeChange(e) {
      this.$nextTick(() => this.$forceUpdate());
    },
    openFileDialog() {
      const me = this;
      var input = document.createElement("input");
      input.type = "file";
      const pageData = this.pageData;

      input.onchange = (e) => {
        me.loading["btnUploadFile"] = true;
        var file = e.target.files[0];
        if (!file) {
          me.loading["btnUploadFile"] = false;
          return;
        }

        // ファイルアップロードサイズの制限を300MBに変更
        // if (file.size > this.MSC_5MB_LengthByte) {
        if (file.size > this.MSC_300MB_LengthByte) {
          e.preventDefault();
          this.getMsg("E040470").then((response) => {
            this.showError(response.data);
          });
          me.loading["btnUploadFile"] = false;
          return;
        }
        // FileReader support
        if (FileReader && file) {
          var fr = new FileReader();
          fr.onload = function () {
            pageData.linkImage = fr.result;
            pageData.linkDownload = fr.result;
            pageData.ファイル名 = file.name;
            pageData.imgUploadName = file.name;
            me.oshiraseHyoji.添付ファイル名 = file.name;
            me.oshiraseHyoji.フルパス = "";
            me.upLoadImage(fr.result.split(",")[1]);
          };
          fr.readAsDataURL(file);
          input.remove();
        } else {
          // Not supported
          // fallback -- perhaps submit the input to an iframe and temporarily store
          // them on the server until the user's session ends.
          input.remove();
          me.loading["btnUploadFile"] = false;
        }
      };
      input.click();
    },
    dragChange(event) {
      const me = this;
      const pageData = this.pageData;
      me.fileList = [];

      if (!event) {
        return;
      }

      if (event.size > this.MSC_5MB_LengthByte) {
        e.preventDefault();
        this.getMsg("E040470").then((response) => {
          this.showError(response.data);
        });
        return;
      }
      // FileReader support
      if (FileReader && event) {
        var fr = new FileReader();
        fr.onload = function () {
          pageData.linkImage = fr.result;
          pageData.linkDownload = fr.result;
          pageData.ファイル名 = event.name;
          pageData.imgUploadName = event.name;
          me.oshiraseHyoji.添付ファイル名 = event.name;
          me.oshiraseHyoji.フルパス = "";
          me.upLoadImage(fr.result.split(",")[1]);
        };
        fr.readAsDataURL(event.raw);
      }
    },
    async upLoadImage(imgBase64) {
      const data = {
        F093_一時添付ファイル: {
          添付NO: this.pageData.添付NO || 0,
          枝番: 1,
          削除区分: 0,
          添付元ファイル名: this.pageData.ファイル名,
          ファイルデータ: imgBase64,
        },
        FLAG添付NO: this.pageData.添付NO,
      };
      let res = {};
      await this.http
        .post(
          "/api/Reafs_W/Common/OshiraseHyoji/fnc_InsertF093",
          data,
          "アクセスしています...."
        )
        .then((response) => {
          res = response;
        });
      if (res.status) {
        this.pageData.添付NO = res.data;
      } else {
      }
      this.loading["btnUploadFile"] = false;
    },
    async onDownload() {
      if (
        this.oshiraseHyoji.フルパス == null ||
        this.oshiraseHyoji.フルパス.toString().trim().length == 0
      ) {
        return;
      }

      this.loading["refDl"] = true;
      const FileDownloadInfo = {
        FilePath: this.oshiraseHyoji.フルパス,
        FileName: this.oshiraseHyoji.添付ファイル名,
      };
      this.http
        .post("/api/Reafs_T/Common/DownloadFile", FileDownloadInfo, "", {
          responseType: "blob",
        })
        .then(async (blob) => {
          // const url = window.URL.createObjectURL(blob);
          // const link = document.createElement("a");
          // link.href = url;
          // link.setAttribute("download", this.oshiraseHyoji.添付ファイル名);
          // document.body.appendChild(link);
          // link.click();
          await this.base.saveFileDownload(blob, this.oshiraseHyoji.添付ファイル名)
        })
        .catch((ex) => console.log(ex))
        .finally(() => {
          this.loading["refDl"] = false;
        })
        .catch((ex) => console.log(ex));
    },
    async onDelete() {
      const dataDefault = {
        ファイルNo: "",
        ファイルパス: "",
        ファイルパス2: "",
        ファイル名: "",
        ルートパス: "",
        添付元ファイル名: "",
        物理ファイル名: "",
        物理ファイル名2: "",
        状況コメント: "",
        部位コメント: "",
        linkImage: "",
        linkDownload: "",
        添付NO: "",
        imgUploadName: "",
      };

      this.loading["btnDelete"] = true;
      const f093Idnx = this.pageData;
      if (f093Idnx.添付NO !== "") {
        const data = {
          FLAG添付NO: f093Idnx.添付NO,
        };
        let res = {};
        await this.http
          .post(
            "/api/Reafs_W/Common/OshiraseHyoji/fnc_delF093_一時添付ファイル",
            data,
            "アクセスしています...."
          )
          .then((response) => {
            res = response;
          });
      }
      this.oshiraseHyoji.フルパス = "";
      this.oshiraseHyoji.添付ファイル名 = "";
      Object.assign(this.pageData, dataDefault);
      this.loading["btnDelete"] = false;
    },
    getDay(beginDate, days) {
      var beginDate = beginDate.split("/");
      var nDate = new Date(
        beginDate[1] + "/" + beginDate[2] + "/" + beginDate[0]
      );
      var milleconds = Math.abs(nDate) + days * 24 * 60 * 60 * 1000;
      var rDate = new Date(milleconds);
      var year = rDate.getFullYear();
      var month = rDate.getMonth() + 1;
      if (month < 10) month = "0" + month;
      var date = rDate.getDate();
      if (date < 10) date = "0" + date;
      return year + "/" + month + "/" + date;
    },
    confirm(message, title) {
      return new Promise((resolve, reject) => {
        this.MsgConf({
          title,
          message,
          callback: function (a, b) {
            if (a === "confirm") {
              resolve(true);
            } else {
              resolve(false);
            }
          },
        });
      });
    },
    onJumpTo(obj) {
      this.$router.push({
        name: "WD00011",
      });
    },
    onClickBack() {
      this.$router.back()
    },
    isSimpleNum(s) {
      if (this.base.isSimpleByte(s)) {
        if (s.match(/[^0-9]/)) {
          return false;
        } else {
          return true;
        }
      } else {
        return false;
      }
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof (option.callback) === 'function') {
            option.callback()
          }
        }
      })
    },
    hideLoading () {
      // eslint-disable-next-line no-return-assign
      Object.keys(this.loading).forEach(v => this.loading[v] = false)
    }
  },
};
</script>

<style>
.OsiraseTouroku {
  margin-top: 20px;
  padding: 10px;
}
/* .OsiraseTouroku .el-form-item.orange .el-form-item__label {
  background-color: orange;
}
.OsiraseTouroku .textarea-class.orange {
  background-color: orange;
} */
.OsiraseTouroku .el-form-item.gray .el-form-item__label {
  background-color: #d3d3d3;
  color: black;
}
.OsiraseTouroku .el-textarea__inner {
  background: lightgoldenrodyellow;
}
.OsiraseTouroku .el-input__count {
  background: lightgoldenrodyellow !important;
}
.OsiraseTouroku .img-label {
  padding-left: 5px;
  background: #d3d3d3;
  border-color: #b7b5b5;
}
.OsiraseTouroku .el-upload--text,
.el-upload-dragger {
  width: 100%;
}
.OsiraseTouroku .el-date-editor .el-input__inner {
  padding-left: 4px !important;
  padding-right: 4px !important;
}
.OsiraseTouroku .el-date-editor.el-input,
.OsiraseTouroku .el-date-editor.el-input__inner {
  width: 115px;
}
</style>