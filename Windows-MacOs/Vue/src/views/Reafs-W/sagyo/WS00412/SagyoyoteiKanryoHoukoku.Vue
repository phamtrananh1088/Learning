<template>
  <!-- <el-dialog title="TS00401" :visible="dialogVisible" @close="onClose()">
    <p>契約Noは「{{ keyData.keiyakukanri_no }}」です。</p>
    <p>作業月は「{{ keyData.keiyakukanri_no }}」です。</p>
    <p>回目は「{{ keyData.keiyakukanri_no }}」です。</p>
  </el-dialog> -->

  <el-dialog width="100%" class="SagyoyoteiKanryoHoukoku" :visible="dialogVisible" @open="onOpen()" @close="onClose_click()" :close-on-click-modal="false">
    <table width="95%">
      <tr>
        <td>
          <!--作業完了報告-->
          <table class="cls-tbl" cellspacing="0px" width="700px">
            <tr>
              <td colspan="2">
                <span
                  style="font-size : x-large"
                  class="cls-span-sagyoyoteinyuryoku row"
                  >作業完了報告
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
                      :width="'490px'"
                      class="el-input-len-10 "
                      v-model="WS00412_privateData.bukken_name"
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
                      :width="'490px'"
                      class="el-input-len-10 "
                      v-model="WS00412_privateData.tehai_name"
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
                      :width="'490px'"
                      class="el-input-len-10 "
                      v-model="WS00412_privateData.sagyou_KBN"
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
                      :width="'490px'"
                      class="el-input-len-10 "
                      v-model="WS00412_privateData.sagyou_name"
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
                      :width="'490px'"
                      class="el-input-len-10 "
                      v-model="WS00412_privateData.sagyou_tuki_kaisuu"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form
                  :model="WS00412_privateData"
                  :disabled="false"
                  ref="WS00412_privateData"
                >
                  <div class="nengetu">
                    <el-form-item label="作業実施日" prop="作業実施日" class="row">
                      <el-date-picker
                        :required="true"
                        type="date"
                        format="yyyy/MM/dd"
                        :clearable="false"
                        :width="'120px'"
                        v-model="WS00412_privateData.sagyokanryo_kaisi_date"
                        :class="{ pinkcolor: isErrPinkSagyotsuki }"
                        index="1"
                        disabled: true
                        @change="onchange_text()"
                        @blur="onblur()"
                        ref="ref作業実施日開始"
                      ></el-date-picker>
                      ~
                      <el-date-picker
                        :required="true"
                        type="date"
                        format="yyyy/MM/dd"
                        :clearable="false"
                        :width="'120px'"
                        v-model="WS00412_privateData.sagyokanryo_syuuryou_date"
                        :class="{ pinkcolor: isErrPinkSagyotsuki }"
                        index="1"
                        @change="onchange_text()"
                        @blur="onblur()"
                        ref="ref作業実施日終了"
                      ></el-date-picker>
                    </el-form-item>
                  </div>
                  
                </el-form>

                
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form label-width="150px">
                  <el-form-item
                    label="完了報告"
                    label-width="150px"
                    class="multi_text row"
                  >
                    <reafsinputtext
                      type="textarea"
                      show-word-limit
                      :width="'490px'"
                      maxlength="100"
                      v-model="WS00412_privateData.sagyou_yotei_bikou"
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
                      :disabled="WS00412_privateData.edit_sashimodoshiJiyuDisable"
                      type="textarea"
                      :width="'490px'"
                      maxlength="50"
                      v-model="WS00412_privateData.sasimodori_ziyuu"
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
                  @click="fnc_void_clickBtnSashimodoshi(WS00412_privateData)"
                  :disabled="WS00412_privateData.btn_sashimodoshiDisable"
                  >差戻
                </el-button>
                <el-button
                  type="primary"
                  @click="fnc_void_clickBtnKakunin(WS00412_privateData)"
                  :disabled="WS00412_privateData.btn_kakuninDisable"
                  >確認
                </el-button>
                <el-button @click="onClose_click()" class="modal_button">
                  閉じる
                </el-button>
              </td>
            </tr>
          </table>
        </td>

        <td>
          <table class="cls-tbl" cellspacing="0px" width="700px">
            <tr>
              <td colspan="2">
                <span
                  style="font-size : x-large"
                  class="cls-span-sagyoyoteinyuryoku row"
                  >添付書類
                </span>
              </td>
            </tr>
            <el-row :gutter="20">
              <el-col :span="24">
                <div class="grid-content bg-purple-light">
                  <reafstable
                    :single="false"
                    :rowKey="rowKey"
                    :tableData="tblData2"
                    :columns="tblColumns"
                    :max-height="650"
                    :height="650"
                    :columnIndex="false"
                    :columnChkbox="false"
                    :linkView="fnc_clickLink"
                    :key="Math.random()"
                    v-bind:tabindex="TAB_IDX_TABLE"
                  >
                  </reafstable>
                </div>
              </el-col>
            </el-row>
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
    WS00412_privateData: {
      type: Object,
      default: null // 初期値nullだがTD00400が上書く
    },
    tblData2: [],
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
      disabledable_button: false,

      tblColumns: [
        {
          field: "帳票名称",
          title: "帳票種類",
          type: "string",
          width: 120,
          align: "center",
          sort: false,
          link: false
        },
        {
          field: "添付元ファイル名",
          title: "ファイル名",
          type: "string",
          width: 240,
          align: "left",
          sort: false,
          link: true
        }
    ],
    };
    
  },
  created() {},
  ///////////////////////////////////////////////
  // グリッド表示
  ///////////////////////////////////////////////
  mounted: function() {
    console.log("222");
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
    // 日付入力イベント
    ///////////////////////////////////////////////
    onblur() {
      var 予定開始日;
      var 予定終了日;
      var 予定開始時間;
      var 予定終了時間;
      if (this.WS00412_privateData.sagyokanryo_kaisi_date != null) {
        if (this.WS00412_privateData.sagyokanryo_kaisi_date.constructor == Date) {
          予定開始日 = this.fnc_obj_NengetsuConvert(
            this.WS00412_privateData.sagyokanryo_kaisi_date
          );
          this.WS00412_privateData.sagyokanryo_kaisi_date = 予定開始日;
        } else {
          予定開始日 = this.WS00412_privateData.sagyokanryo_kaisi_date;
        }
      } else {
        予定開始日 = "";
      }

      if (this.WS00412_privateData.sagyokanryo_syuuryou_date != null) {
        if (this.WS00412_privateData.sagyokanryo_syuuryou_date.constructor == Date) {
          予定終了日 = this.fnc_obj_NengetsuConvert(
            this.WS00412_privateData.sagyokanryo_syuuryou_date
          );
          this.WS00412_privateData.sagyokanryo_syuuryou_date = 予定終了日;
        } else {
          予定終了日 = this.WS00412_privateData.sagyokanryo_syuuryou_date;
        }
      } else {
        予定終了日 = "";
      }
      
      if (
        //予定日開始あり・予定日終了なし
        (this.WS00412_privateData.sagyokanryo_kaisi_date != "" ||
          this.WS00412_privateData.sagyokanryo_kaisi_date != null) &&
        (this.WS00412_privateData.sagyokanryo_syuuryou_date == "" ||
          this.WS00412_privateData.sagyokanryo_syuuryou_date == null)
      ) {
        this.WS00412_privateData.sagyokanryo_syuuryou_date = this.WS00412_privateData.sagyokanryo_kaisi_date;
      } else if (
        //予定日開始なし・予定日終了あり
        (this.WS00412_privateData.sagyokanryo_syuuryou_date == "" ||
          this.WS00412_privateData.sagyokanryo_syuuryou_date == null) &&
        (this.WS00412_privateData.sagyokanryo_kaisi_date != "" ||
          this.WS00412_privateData.sagyokanryo_kaisi_date != null)
      ) {
        this.WS00412_privateData.sagyokanryo_syuuryou_date = this.WS00412_privateData.sagyokanryo_kaisi_date;
      } else if (
        //予定日開始・終了あり　時間逆転チェック
        予定開始日 === 予定終了日
      ) {
        //nothing
      } else if (
        // 日付逆転チェック
        予定開始日 >= 予定終了日
      ) {
        // var kaisi = "";
        // var owari = "";
        // kaisi = this.WS00412_privateData.sagyokanryo_kaisi_date;
        // owari = this.WS00412_privateData.sagyokanryo_syuuryou_date;
        // this.WS00412_privateData.sagyokanryo_kaisi_date = owari;
        // this.WS00412_privateData.sagyokanryo_syuuryou_date = kaisi;
      }
    },

    ///////////////////////////////////////////////
    // ×ボタンまたは閉じるボタンクリック時処理
    ///////////////////////////////////////////////
    onchange_text() {
      this.change_time += 1;
    },
    onOpen(){
      this.fnc_void_clearData();
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
    // 差戻ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnSashimodoshi(valSashimodoshiJoken) {

      let retCode;

      // 必須チェック
      retCode = this.fnc_obj_SashimodoshimaeErrorChk(valSashimodoshiJoken);
      if(retCode === false) {

        // エラーメッセージ
        this.fnc_void_showInfoMsg("E040699");
        // 差戻事由エラー色表示
        this.isErrPinkSashimodoshiJiyu = true;

        return false;

      } else {

        this.fnc_rtn_getMsg("K040039").then(response => {
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
//      if(this.WS00412_tourokuJoken.sasimodori_ziyuu === ""){
      if(valSashimodoshiJoken.sasimodori_ziyuu === ""){
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

      let _this = this;
      var mail = true;
      // F050.差戻回数
      // F052.作業予定登録
      // F052.ステータス登録
      // F053.差し戻し履歴登録
      // F103.進捗ステータス更新
      console.log("差戻処理実行");

      await this.http
        .post(
          "/api/Reafs_W/SagyoJyokyoKanryo/WS00412_RETURN_YoteiData",
          valSashimodoshiJoken,
          "差戻しています...."
        )
        .then(function(response) {
          console.log("メール開始");
          if (response.status == true) {
            // 登録完了メッセージ表示
            _this.fnc_void_showInfoMsg("M040014");
            _this.fnc_void_closeWindow();
          } else {
            mail = false;
            // 登録失敗メッセージ表示
            _this.fnc_void_showInfoMsg("E040059");
          }
          
        });

      //メール送信　10/25
      if (mail === true){
        console.log("メール送信");
        let param = {
                msg_type: 0,
                pg_id: 'WD00410',
                st_kbn: 2,
                mail_pattern_no: 21,
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

    ///////////////////////////////////////////////
    // 確認ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnKakunin(valKakuninJoken) {
      let retCode;

      retCode = this.fnc_obj_KakuninmaeErrorChk(valKakuninJoken);
      if(retCode === false) {
        // 年月度エラー色表示
        this.isErrPinkSagyotsuki = true;

        return false;
      } else {

        this.fnc_rtn_getMsg("K040038").then(response => {
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
      //日付の逆転チェック
      if (valKakuninJoken.sagyokanryo_kaisi_date === null || valKakuninJoken.sagyokanryo_syuuryou_date === null
          || valKakuninJoken.sagyokanryo_kaisi_date === '' || valKakuninJoken.sagyokanryo_syuuryou_date === ''
        ){
        this.fnc_void_showInfoMsg("E040705");
        
        // if (valKakuninJoken.sagyokanryo_kaisi_date === null || valKakuninJoken.sagyokanryo_kaisi_date === ''){
        //   this.$refs.ref作業実施日開始.focus()
        // }else{
        //   this.$refs.ref作業実施日終了.focus()
        // }        
        return false;
      }else if(valKakuninJoken.sagyokanryo_kaisi_date > valKakuninJoken.sagyokanryo_syuuryou_date){
        console.log("2");
        this.fnc_void_showInfoMsg("E040485");
        return false;
      }
      return true;
    },
    checkVersion (valKakuninJoken) {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/SagyoJyokyoKanryo/CheckF052Version',
            valKakuninJoken)
            // { 会社コード: valKakuninJoken.KaishaCd, 契約No: valKakuninJoken.KeiyakuNo, 履歴No: valKakuninJoken.RirekiNo, 明細No: valKakuninJoken.MeisaiNo, 契約年月: valKakuninJoken.KeiyakuNengetsu})
          .then((response) => {
            if (valKakuninJoken.VERSION && (valKakuninJoken.VERSION !== response.data[0].VERSION || !response.data[0].VERSION)) {
              resolve(false)
            } else {
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
        // this.getMsg('E040098').then((response) => {
        //   this.showError(response.data)
        // })
        this.fnc_void_showInfoMsg("E040098");
        return false
      }

      // 進捗ステータス更新
      // 回目データ更新
      console.log("更新処理実行");
      let _this = this;
      var mail = true;

      await this.http
        .post(
          "/api/Reafs_W/SagyoJyokyoKanryo/WS00412_POST_YoteiData",
          valKakuninJoken,
          "登録しています...."
        )
        .then(function(response) {
          if (response.status == true) {
            // 登録完了メッセージ表示
            _this.fnc_void_showInfoMsg("M040021");
            _this.fnc_void_closeWindow();
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
                mail_pattern_no: 23,
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


    ///////////////////////////////////////////////
    //変更あり処理
    ///////////////////////////////////////////////
    onchange() {
      console.log("変わるわよ");

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
    fnc_clickLink(row, column, rowIndex) {
      switch (column.field) {
        // 物件名
        case "添付元ファイル名":
          this.onDownload(this.tblData2[rowIndex].ファイルパス,this.tblData2[rowIndex].添付元ファイル名)
          break;

        default:
          break;
      }
    },

    async onDownload(DLPass,FileName) {
      if (
        DLPass == null ||
        DLPass.toString().trim().length == 0
      ) {
        return;
      }
      // this.loading["refDl"] = true;
      const FileDownloadInfo = {
        FilePath: DLPass,
        FileName: FileName
      };
      this.http
        .post("/api/Reafs_W/Common/DownloadFile", FileDownloadInfo, "", {
          responseType: "blob"
        })
        .then(async blob => {
          // const url = window.URL.createObjectURL(blob);
          // const link = document.createElement("a");
          // link.href = url;
          // link.setAttribute("download", FileName);
          // document.body.appendChild(link);
          // link.click();
            await this.base.saveFileDownload(blob, FileName)
        })
        .catch(ex => console.log(ex))
        .finally(() => {
          // this.loading["refDl"] = false;
        })
        .catch(ex => console.log(ex));
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
.nengetu >>> .el-date-editor{
  height: 30px;
  width: 150px;
  margin: 0;
  line-height: 30px;
  border-radius: 1px;
  padding-right: 5px;
}
.pinkcolor >>> .el-input__inner {
  background-color: lightpink !important;
}
.pinkcolor_text >>> .el-textarea__inner {
  background-color: lightpink !important;
}
</style>
