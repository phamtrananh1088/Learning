<template>

  <el-dialog width="50%" class="SagyoyoteiMeisai" :visible="dialogVisible" @open="onOpen()" @close="onClose_click()" :close-on-click-modal="false">
    <table width="95%">
      <tr>
        <td>
          <!--作業予定明細-->
          <table class="cls-tbl" cellspacing="0px" width="700px">
            <tr>
              <td colspan="2">
                <span
                  style="font-size : x-large"
                  class="cls-span-sagyoyoteinyuryoku row"
                  >作業予定明細
                </span>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item label="物件名" label-width="150px" class="row">
                    <reafsinputtext
                      :disabled="disabledable_text_nomal_true"
                      maxlength="8"
                      :width="'530px'"
                      class="el-input-len-10 "
                      v-model="WS00411_privateData.bukken_name"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item label="手配先業者" label-width="150px" class="row">
                    <reafsinputtext
                      :disabled="disabledable_text_nomal_true"
                      maxlength="60"
                      :width="'530px'"
                      class="el-input-len-10 "
                      v-model="WS00411_privateData.tehai_name"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item label="大中小区分" label-width="150px" class="row">
                    <reafsinputtext
                      :disabled="disabledable_text_nomal_true"
                      maxlength="60"
                      :width="'530px'"
                      class="el-input-len-10 "
                      v-model="WS00411_privateData.sagyou_KBN"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item
                    label="作業名称"
                    label-width="150px"
                    class="row"
                  >
                    <reafsinputtext
                      :disabled="disabledable_text_nomal_true"
                      maxlength="50"
                      :width="'530px'"
                      class="el-input-len-10 "
                      v-model="WS00411_privateData.sagyou_name"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item
                    label="作業月/回目"
                    label-width="150px"
                    class="row"
                  >
                    <reafsinputtext
                      :disabled="disabledable_text_nomal_true"
                      maxlength="15"
                      :width="'530px'"
                      class="el-input-len-10 "
                      v-model="WS00411_privateData.sagyou_tuki_kaisuu"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form
                  :model="WS00411_privateData"
                  :disabled="true"
                  ref="WS00411_privateData"
                >
                  <el-form-item
                    label="作業予定日"
                    prop="nyuryokuerea6"
                    label-width="150px"
                    class="row"
                  >
                    <nengetsuhirange
                      @change="onchange_text()"
                      v-bind.sync="WS00411_privateData.sagyo_date"
                      :dateStartRequired="false"
                      :dateEndRequired="false"
                      :timeStartRequired="false"
                      :timeEndRequired="false"
                      :clearable="false"
                      :dateStartformat="'yyyy/MM/dd'"
                      :dateEndformat="'yyyy/MM/dd'"
                      :class="{ pinkcolor: isErrPinkSagyotsuki }"
                    ></nengetsuhirange>

                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form label-width="150px">
                  <el-form-item
                    label="備考"
                    label-width="150px"
                    class="multi_text row"
                  >
                    <reafsinputtext
                      type="textarea"
                      show-word-limit
                      :width="'530px'"
                      maxlength="100"
                      v-model="WS00411_privateData.sagyou_yotei_bikou"
                      :disabled="disabledable_text_nomal_true"
                      @change="onchange_text()"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form label-width="150px" class="row">
                  <el-form-item
                    label="差戻事由"
                    label-width="150px"
                    class="multi_text"
                  >
                    <reafsinputtext
                      :disabled="WS00411_privateData.edit_sashimodoshiJiyuDisable"
                      type="textarea"
                      :width="'530px'"
                      maxlength="50"
                      v-model="WS00411_privateData.sasimodori_ziyuu"
                      :class="{ pinkcolor_text: isErrPinkSashimodoshiJiyu }"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2" align="left">
                <el-button
                  type="primary"
                  @click="fnc_void_clickBtnSashimodoshi(WS00411_privateData)"
                  
                  :disabled="WS00411_privateData.btn_sashimodoshiDisable"
                  >差戻
                </el-button>
                <el-button
                  type="primary"
                  @click="fnc_void_clickBtnKakunin(WS00411_privateData)"
                  
                  :disabled="WS00411_privateData.btn_kakuninDisable"
                  >確認
                </el-button>
                <el-button @click="onClose_click()" class="modal_button">
                  閉じる
                </el-button>
              </td>
              
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </el-dialog>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import reafstable from "@/components/table/Table.vue";
import nengetsuhirange from "@/components/basic/NengetsuhiRange.vue";

export default {
  components: {
    reafsinputtext,
    nengetsuhirange,
    nengetsu,
    reafstable
  },

  ///////////////////////////////////////////////
  // TD00400→TS00401へデータ受け渡し
  // ※propsはプロパティ（property）の略で、親コンポーネントから子コンポーネントに値を渡す際に使用
  ///////////////////////////////////////////////
  props: {
    // TS00401表示、非表示を制御
    dialogVisible: {
      type: Boolean,
      default: false // 初期値falseだがTD00400がtrueに上書く
    },
    // 
    WS00411_privateData: {
      type: Object,
      default: null // 初期値nullだがTD00400が上書く
    },

  },
  data() {
    return {
      ///////////////////////////////////////////////
      // 閉じる処理
      ///////////////////////////////////////////////
      change_time: 0,
      ///////////////////////////////////////////////
      // ダイアログ
      ///////////////////////////////////////////////
      dialogVisible: false,
      pagination: { total: 22, size: 10, sortName: "", page: 1 },


      ///////////////////////////////////////////////
      // エラーフラグ DATEPICKERをピンクにする為に使用
      ///////////////////////////////////////////////
      isErrPinkSagyotsuki: false,
      isErrPinkSashimodoshiJiyu: false,

      ///////////////////////////////////////////////
      // テキスト、ボタン制御
      ///////////////////////////////////////////////
      disabledable_text: false,
      disabledable_text_nomal_true: true,
      disabledable_text_nomal_false: false,
      disabledable_button: false
    };
  },
  created() {},
  ///////////////////////////////////////////////
  // グリッド表示
  ///////////////////////////////////////////////
  mounted: function() {
    this.$nextTick(function() {
      //画面レンダー後行う
      document
        .getElementsByClassName("el-table__cell gutter")[1]
        .setAttribute("rowSpan", 8);
    });
  },
  mounted() {
    console.log("111");
  },
  methods: {
    // window: (onload = function() {
    //   alert("click_ServerAccessChk");
    // }),

    pageBack(obj) {
      this.pagination.page = obj;
    },

    ///////////////////////////////////////////////
    // ×ボタンまたは閉じるボタンクリック時処理
    ///////////////////////////////////////////////
    onchange_text() {
      this.change_time += 1;
    },
	onOpen(){
      this.isErrPinkSashimodoshiJiyu = false;
    },
    onClose_click() {
      if (this.change_time >= 1) {
        this.fnc_void_showErrorMsg_close("K000001"); // 確認メッセージ出力

        return true;
      } else {
        this.fnc_void_closeWindow();
      }
    },

    ///////////////////////////////////////////////
    // 登録前ピンク色等データクリア
    ///////////////////////////////////////////////
    fnc_void_clearData() {
      let _this = this;
      _this.isErrPinkSagyotsuki = false;
      _this.isErrPinkSashimodoshiJiyu = false;
    },

    ///////////////////////////////////////////////
    // 確認ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnKakunin(valKakuninJoken) {

      let retCode;

      console.log("確認ボタン押下");

      // エラー色初期化
      this.fnc_void_clearData();

      //日付チェック・コンバート
      if (

        valKakuninJoken.sagyo_date === "" ||
        valKakuninJoken.sagyo_date == undefined

      ) {
        
        this.fnc_void_showErrorMsg("E040705"); // 請求書月分を入力して下さい。
        this.fnc_void_pinkData(); //背景色ピンク
        exit;

      } else if (

        valKakuninJoken.sagyo_date.startDate.constructor == Date ||
        valKakuninJoken.sagyo_date.endDate.constructor == Date

      ) {
        
        valKakuninJoken.sagyoyotei_kaisi_date = this.fnc_obj_NengetsuConvert(
          valKakuninJoken.sagyo_date.startDate
        );
        valKakuninJoken.sagyoyotei_syuuryou_date = this.fnc_obj_NengetsuConvert(
          valKakuninJoken.sagyo_date.endDate
        );

      }else{

        valKakuninJoken.sagyoyotei_kaisi_date = valKakuninJoken.sagyo_date.startDate;
        valKakuninJoken.sagyoyotei_kaisi_time = valKakuninJoken.sagyo_date.startTime;
        valKakuninJoken.sagyoyotei_syuuryou_date = valKakuninJoken.sagyo_date.endDate;
        valKakuninJoken.sagyoyotei_syuuryou_time = valKakuninJoken.sagyo_date.endTime;

      }

      // 登録時エラーチェック
      retCode = this.fnc_obj_KakuninmaeErrorChk(valKakuninJoken);
      if(retCode === false) {
        exit;
      } else {

        this.fnc_rtn_getMsg("K040036").then(response => {
          this.MsgConf(
            {
              title: "作業状況一覧",
              message: response.data
            },
            () => {
              // OKが選択された場合
              this.fnc_void_Kakunin(valKakuninJoken);
              return true;
            },
            () => {
              // Cancelが選択された場合
              return false;
            }
          );
        });

      }

      return retCode;
    },

    ///////////////////////////////////////////////
    // 確認前エラーチェック
    ///////////////////////////////////////////////
    fnc_obj_KakuninmaeErrorChk(valKakuninJoken) {
      // 現状登録時にエラーチェックは必要ないが、繰越判別はここでやる
      var kaisi = valKakuninJoken.sagyoyotei_kaisi_date;
      var syuuryou = valKakuninJoken.sagyoyotei_syuuryou_date;

      console.log("ここ")
      console.log(valKakuninJoken.sagyo_date)
      console.log(kaisi)
      console.log(syuuryou)
      if (
        //作業実施月が表示と異なる場合
        valKakuninJoken.JissiYoteiNengetsu !== kaisi.substr(0, 7) ||
        valKakuninJoken.JissiYoteiNengetsu !== syuuryou.substr(0, 7)
      ) {
        valKakuninJoken.KurikoshiFlg = 1;
        
      } else {
        valKakuninJoken.KurikoshiFlg = 0;
      }

      return true;
    },
    checkVersion (valKakuninJoken) {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/SagyoJyokyoKoshin/CheckF052Version',
            valKakuninJoken)
            // { 会社コード: valKakuninJoken.KaishaCd, 契約No: valKakuninJoken.KeiyakuNo, 履歴No: valKakuninJoken.RirekiNo, 明細No: valKakuninJoken.MeisaiNo, 契約年月: valKakuninJoken.KeiyakuNengetsu})
          .then((response) => {
            if (valKakuninJoken.VERSION && (valKakuninJoken.VERSION !== response.data[0].VERSION || !response.data[0].VERSION)) {
              console.log("確認①");
              resolve(false)
            } else {
              console.log("確認②");
              resolve(true)
            }
          })
      })
    },

    ///////////////////////////////////////////////
    // 確認更新処理
    ///////////////////////////////////////////////
    async fnc_void_Kakunin(valKakuninJoken) {
      // バージョンチェック処理
      if (!await this.checkVersion(valKakuninJoken)) {
        this.fnc_void_showInfoMsg("E040098");
        return false
      }

      console.log("更新処理実行");
      let _this = this;
      await this.http
        .post(
          "/api/Reafs_W/SagyoJyokyoKoshin/WS00411_POST_YoteiData",
          valKakuninJoken,
          "登録しています...."
        )
        .then((response) => {
          // res = response
          if (response.status) {
            // this.showSuccess(msg004 + res.data.依頼No + (await this.getMsg(smesSucess)).data)
            _this.fnc_void_showInfoMsg("M040021");
          }
        })
        
          
        console.log("登録完了")
        // .then(function(response) {
        //   console.log("分岐")
        //   if (response.status == true) {
        //     // 登録完了メッセージ表示
        //     console.log("メッセージ表示")
        //     this.fnc_void_showInfoMsg("M040021");
        //   } else {
        //     // 登録失敗メッセージ表示
        //     console.log("メッセージ表示エラー")
        //     this.fnc_void_showInfoMsg("E040059");
        //   }
          
        // });

    },

    ///////////////////////////////////////////////
    // 差戻ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnSashimodoshi(valSashimodoshiJoken) {

      let retCode;
      
      console.log("差戻ボタン押下");

      // エラー色初期化
      //this.fnc_void_clearData();

      // 必須チェック
      retCode = this.fnc_obj_SashimodoshimaeErrorChk(valSashimodoshiJoken);
      if(retCode === false) {
        this.fnc_void_showInfoMsg("E040698");
        this.fnc_void_pinkData(1); //背景色ピンク
        this.isErrPinkSashimodoshiJiyu = true;
        return false;

      } else {

        this.fnc_rtn_getMsg("K040037").then(response => {
          this.MsgConf(
            {
              title: "作業状況一覧",
              message: response.data
            },
            () => {
              // OKが選択された場合
              this.fnc_void_Sashimodoshi(valSashimodoshiJoken);
              return true;
            },
            () => {
              // Cancelが選択された場合
              return false;
            }
          );
        });

      }

      return retCode;
    },

    ///////////////////////////////////////////////
    // 差戻前エラーチェック
    ///////////////////////////////////////////////
    fnc_obj_SashimodoshimaeErrorChk(valSashimodoshiJoken) {

      let retCode;

      // 差戻事由入力なし
//      if(this.WS00411_tourokuJoken.sasimodori_ziyuu === ""){
      if(valSashimodoshiJoken.sasimodori_ziyuu === undefined || valSashimodoshiJoken.sasimodori_ziyuu === ""){
        retCode = false;
      }else{
        retCode = true;
      }

      return retCode;
    },

    ///////////////////////////////////////////////
    // 差戻更新処理
    ///////////////////////////////////////////////
    async fnc_void_Sashimodoshi(valSashimodoshiJoken) {
      // バージョンチェック処理
      if (!await this.checkVersion(valSashimodoshiJoken)) {
        this.fnc_void_showInfoMsg("E040098");
        return false
      }

      // F050.差戻回数
      // F052.作業予定登録
      // F052.ステータス登録
      // F053.差し戻し履歴登録
      // F103.進捗ステータスorF102.進捗ステータス更新
      console.log("差戻処理実行");
      let _this = this;
      var mail = true;

      await this.http
        .post(
          "/api/Reafs_W/SagyoJyokyoKoshin/WS00411_RETURN_YoteiData",
          valSashimodoshiJoken,
          "差戻しています...."
        )
        .then(function(response) {
          if (response.status) {
            // 登録完了メッセージ表示
            _this.fnc_void_showInfoMsg("M040014");
          } else {
            mail = false;
            // 登録失敗メッセージ表示
            _this.fnc_void_showInfoMsg("E040059");
          }
          
        });

      //メール送信　10/26
      if (mail === true){
        console.log("メール送信");
        let param = {
                msg_type: 0,
                pg_id: 'WD00410',
                st_kbn: 2,
                mail_pattern_no: 17,
                工事依頼NO: valSashimodoshiJoken.KojiiraiNo,
                契約NO: valSashimodoshiJoken.KeiyakuNo,
                履歴NO: valSashimodoshiJoken.RirekiNo,
                明細NO: valSashimodoshiJoken.MeisaiNo,
                契約年月: valSashimodoshiJoken.KeiyakuNengetsu,
                業者コード: valSashimodoshiJoken.tblTehaiGyoshaCode,
                業者コード枝番: valSashimodoshiJoken.tblTehaiGyoshaCodeEdaban,
                担当者コード:[valSashimodoshiJoken.tblTehaiGyoshaCode]
                // msg_type: 0,
                // pg_id: 'WD00410',
                // st_kbn: 2,
                // mail_pattern_no: 17,
                // 工事依頼NO: '12345678',
                // 契約NO: '12345678911',
                // 履歴NO: 0,
                // 明細NO: 1,
                // 契約年月: '2022/10',
                // 業者コード: '123456',
                // 業者コード枝番: '000',
                // 宛先事務所コード: ['000', '177'],
                // 宛先営業所コード: ['990001', '705000'],
                // 宛先部コード: ['031', '999'],
                // 宛先課コード: ['599', '021'],
                // 宛先係コード: ['9999', '9999'],
                // 担当者コード:['1234567']
              }
            let resultMail
            resultMail = this.commonFunctionUI.CM00110SendMail(param)
        }
    },

    // 登録前ピンク色等データ
    fnc_void_pinkData(int) {
      let _this = this;

      if (int == 0) {
        _this.isErrPinkSagyotsuki = true;
      } else if (int == 1) {
        console.log('背景色変更処理実行');
        _this.isErrPinkSashimodoshiJiyu = true;
        console.log('背景色変更処理実行' + _this.isErrPinkSashimodoshiJiyu);
      }
    },

    ///////////////////////////////////////////////
    //変更あり処理
    ///////////////////////////////////////////////
    onchange() {
      change = 1;
    },

    ///////////////////////////////////////////////
    // 日付コンバート
    // DATE型を文字型のYYYY/MM/ddにコンバートする
    ///////////////////////////////////////////////
    fnc_obj_NengetsuConvert(sagyoudate) {
      let letYear = new Object();
      let letMonth = new Object();
      let letday = new Object();
      let letNengetsu = new Object();

      letYear = sagyoudate.getFullYear();
      letMonth = sagyoudate.getMonth() + 1;
      letMonth = ("00" + letMonth).slice(-2);
      letday = sagyoudate.getDate();
      letday = ("00" + letday).slice(-2);
      letNengetsu = letYear + "/" + letMonth + "/" + letday;

      return letNengetsu;
    },
    ///////////////////////////////////////////////
    // 時間コンバート
    ///////////////////////////////////////////////
    fnc_obj_TimeConvert(sagyoudate) {
      let lethour = new Object();
      let letminues = new Object();
      let letTime = new Object();

      lethour = sagyoudate.gethours();
      letminues = sagyoudate.minutes();
      letTime = lethour + ":" + letminues;

      return letTime;
    },
    ///////////////////////////////////////////////
    // keyからS016メッセージマスタにアクセスしメッセージ取得
    ///////////////////////////////////////////////
    fnc_rtn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    ///////////////////////////////////////////////
    // エラーメッセージ出力
    ///////////////////////////////////////////////
    // お知らせメッセージ

    fnc_void_showInfoMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgInfo({
          title: "作業状況一覧",
          message: response.data
        });
      });
    },
    // エラーメッセージ

    fnc_void_showErrorMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "作業状況一覧",
          message: response.data
        });
      });
    },
    //閉じるボタン処理
    fnc_void_showErrorMsg_close(MsgCode) {
      console.log("閉じるボタン");
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgConf(
          {
            title: "作業状況一覧",
            message: response.data
          },
          () => {
            // OKが選択された場合
            this.change_time = 0;
            this.fnc_void_closeWindow();
          },
          () => {
            // Cancelが選択された場合
            // this.$alert('Cancelが選択された場合')
            return false;
          }
        );
      });
    },
    fnc_void_closeWindow() {
      console.log("close_WS00411");

      // dialogVisibleにfalseをセットすることでダイアログを閉じる
      this.$emit("update:dialogVisible", false);
      this.$emit('closePopupEvent', this);
    }
  },
  watch: {
    testkey: function(newval) {
      newval = newval.replace(/[^0-9]/g, "");
      this.testkey = newval;
    }
  }
};
</script>

<style lang="less" scoped>
.home-contianer {
  padding: 5px;
}
.el-dialog {
  margin: 0;
  padding: 0;
  position: top;
}

.dialog {
  position: scroll;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  padding: 0;
  max-width: 100%;
  min-width: 320px;
  height: auto;
  backface-visibility: hidden;
  position: top;
}

.contens {
  margin: 10px;
}
#dialog-left {
  position: scroll;
  display: block;
  float: left;
  width: 50%;
  height: 80%;
  padding: 10px;
  border: solid 1px black;
}

#dialog-right {
  float: right;
  width: 50%;
  height: 80%;
  padding: 10px;
  border: solid 1px black;
}

.reafs-input .el-input__inner {
  height: 30px;
  line-height: 30px;
  border-radius: 1px;
}

.reafs-input-required .el-input__inner {
  height: 30px;
  line-height: 30px;
  border-radius: 1px;
  background: lightgoldenrodyellow;
}

.el-button {
  padding-top: 7px;
  padding-bottom: 7px;
  margin-left: -4px;
  border-start-start-radius: 1px;
  border-end-start-radius: 1px;
}
.el-input-len-10 {
  width: 450px;
}
.reafsinputtext {
  width: 400px;
  margin: 0;
}

.clearfix:after {
  content: "";
  display: block;
  clear: both;
}
.sagyo_kategori_midasi {
  margin: 0;
  height: 100%;
  width: 10%;
  float: left;
}
.sagyo_kategori {
  list-style-type: none;
  margin: 0;
  float: left;
  width: 50%;
}
.el-row {
  margin: 0;
}

.el-input-len-text {
  width: 490px;
}

.modal_button {
  float: right;
}

.el-form-item {
  margin: 1px;
  padding: 0;
}
.dialog-header {
  display: block;
  clear: both;
  height: 10%;
}
.dialog-body {
  margin-top: 10px;
  display: block;
  height: 90%;
}
.dialog-header h1 {
  float: left;
}
.tyuuizikou {
  color: red;
}
.el-input-date-len-10 {
  width: 120px;
  margin: 0;
}
.grid {
  height: 500px;
}
.row {
  margin: 0 5px;
}
</style>

<style scoped>
.el-form .multi >>> .el-form-item__label {
  height: 94px;
}
.el-form .multi_text >>> .el-form-item__label {
  height: 52px;
}
.nengetsu >>> .el-input__inner {
  height: 30px;
  width: 120px;
  margin: 0;
  line-height: 30px;
  border-radius: 1px;
  padding-right: 5px;
}

.nengetsu-required >>> .el-input__inner {
  height: 30px;
  width: 120px;
  margin: 0;
  line-height: 30px;
  border-radius: 1px;
  padding-right: 5px;
  background: lightgoldenrodyellow;
}

.nengetsu-required >>> .el-range-input {
  padding-right: 5px;
  background: lightgoldenrodyellow;
}
.cls-tbl {
  border-spacing: 0px 0px;
  border: "1";
  min-height: 1px;
  border-collapse: collapse;
  cellspacing: "0px";
  border-spacing: 0px 0px;
  padding: 0;
  margin: 0;
}
.p-sagyocategory {
  line-height: $div-height;
  width: 83px;
  height: 95px;
  line-height: 98px;
  background: #9bc2e6;
  padding: 0px;
  margin: 0px;
  color: white;
}
.cls-span-sagyoyoteinyuryoku {
  height: 30px;
  line-height: 30px;
  padding: 5px;
  margin: 0px;
}
table,
td {
  padding: 0px;
  margin: 0;
}

table {
  margin: 5px;
}
.home-contianer {
  padding: 10px;
}
.pinkcolor >>> .el-range-input {
  background-color: lightpink !important;
}
.pinkcolor_text >>>  .el-textarea__inner {
  background-color: lightpink !important;
}
</style>
