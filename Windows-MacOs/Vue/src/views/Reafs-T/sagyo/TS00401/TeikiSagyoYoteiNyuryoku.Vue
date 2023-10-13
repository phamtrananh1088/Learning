<template>
  <el-dialog
    width="1500px"
    :visible="dialogVisible"
    :close-on-click-modal="false"
    :show-close="false"
    @close="onClose_click()"
    @open="onOpen()"
    class="SagyoYoteiNyuryoku"
  >
    <table class="body_table">
      <tr tyle="width: 50%">
        <td>
          <!--作業予定入力-->
          <table class="cls-tbl" cellspacing="0px" width="700px">
            <tr>
              <td colspan="2">
                <span
                  style="font-size : x-large"
                  class="cls-span-sagyoyoteinyuryoku row"
                  >作業予定入力
                </span>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item
                    label="契約管理番号"
                    label-width="150px"
                    class="row"
                  >
                    <reafsinputtext
                      class="is-readonly readonly_text"
                      v-model="TS00401_tourokuJoken.keiyakukanri_no"
                      maxlength="8"
                      disabled="disabled"
                      :width="'490px'"
                    >
                    </reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item label="物件名" label-width="150px" class="row">
                    <reafsinputtext
                      class="is-readonly readonly_text"
                      v-model="TS00401_tourokuJoken.bukken_name"
                      maxlength="40"
                      disabled="disabled"
                      :width="'490px'"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item label="営業店" label-width="150px" class="row">
                    <reafsinputtext
                      class="is-readonly readonly_text"
                      v-model="TS00401_tourokuJoken.eigyou_name"
                      maxlength="40"
                      disabled="disabled"
                      :width="'490px'"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>

            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item label="契約名" label-width="150px" class="row">
                    <reafsinputtext
                      class="is-readonly readonly_text"
                      v-model="TS00401_tourokuJoken.keiyaku_name"
                      maxlength="40"
                      disabled="disabled"
                      :width="'490px'"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td rowspan="3" width="85px">
                <div class="row">
                  <p class="p-sagyocategory " align="center">作業カテゴリ</p>
                </div>
              </td>
              <td>
                <el-form label-width="60px">
                  <el-form-item label="大区分">
                    <reafsinputtext
                      class="is-readonly"
                      v-model="TS00401_tourokuJoken.sagyou_c_dai"
                      maxlength="40"
                      disabled="disabled"
                      :width="'490px'"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td>
                <el-form label-width="60px">
                  <el-form-item label="中区分">
                    <reafsinputtext
                      class="is-readonly"
                      v-model="TS00401_tourokuJoken.sagyou_c_tyuu"
                      maxlength="40"
                      disabled="disabled"
                      :width="'490px'"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td>
                <el-form label-width="60px">
                  <el-form-item label="小区分">
                    <reafsinputtext
                      class="is-readonly"
                      v-model="TS00401_tourokuJoken.sagyou_c_syou"
                      maxlength="40"
                      disabled="disabled"
                      :width="'490px'"
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
                      class="is-readonly readonly_text"
                      v-model="TS00401_tourokuJoken.sagyou_name"
                      maxlength="40"
                      disabled="disabled"
                      :width="'490px'"
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
                      class="is-readonly readonly_text"
                      v-model="TS00401_tourokuJoken.sagyou_tuki_kaisuu"
                      maxlength="15"
                      disabled="disabled"
                      :width="'490px'"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form>
                  <el-form-item
                    label="作業予定日"
                    label-width="150px"
                    class="row row2"
                  >
                    <el-date-picker
                      width="130px"
                      :required="true"
                      class="row_x"
                      type="date"
                      :disabled="koumoku1"
                      v-model="TS00401_tourokuJoken.yoteikaisi_date"
                      @change="change_count(0)"
                      format="yyyy/MM/dd"
                      @blur="onblur()"
                      :clearable="false"
                      :class="{ pinkcolor: isErrPinkSagyotsuki }"
                      ref="ref予定日"
                      tabindex="1"
                    ></el-date-picker>
                    <el-time-picker
                      width="120px"
                      :required="false"
                      class="row_time"
                      :disabled="koumoku1"
                      v-model="TS00401_tourokuJoken.sagyoyotei_kaisi_time"
                      @change="change_count(0)"
                      format="HH:mm"
                      value-format="HH:mm"
                      @blur="onblur()"
                      :clearable="false"
                      tabindex="1"
                    ></el-time-picker>
                    ~
                    <el-date-picker
                      :required="true"
                      type="date"
                      format="yyyy/MM/dd"
                      :clearable="false"
                      width="130px"
                      class="row_x"
                      :disabled="koumoku1"
                      v-model="TS00401_tourokuJoken.yoteisyuuryou_date"
                      :class="{ pinkcolor: isErrPinkSagyotsuki }"
                      @change="change_count(0)"
                      @blur="onblur()"
                      tabindex="1"
                    ></el-date-picker>
                    <el-time-picker
                      width="120px"
                      format="HH:mm"
                      value-format="HH:mm"
                      :required="false"
                      class="row_time"
                      v-model="TS00401_tourokuJoken.sagyoyotei_syuuryou_time"
                      @change="change_count(0)"
                      :disabled="koumoku1"
                      :clearable="false"
                      @blur="onblur()"
                      tabindex="1"
                    ></el-time-picker>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form label-width="150px">
                  <el-form-item
                    label="作業予定/備考"
                    label-width="150px"
                    class="multi_text row"
                  >
                    <div>
                      <reafsinputtext
                        type="textarea"
                        show-word-limit
                        :width="'490px'"
                        maxlength="100"
                        v-model="TS00401_tourokuJoken.sagyou_yotei_bikou"
                        :disabled="koumoku1"
                        :class="{ pinkcolor_text: isErrPinkSagyotsuki_text }"
                        @change="change_count(0)"
                        ref="sagyoubikou"
                        class="grid_time"
                        tabindex="1"
                      ></reafsinputtext>
                    </div>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2" class="tyuuizikou">
                <div class="row">
                  ※作業予定が指定期日外の場合は「作業予定/備考」を入力してください
                </div>
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
                      :disabled="disabledable_text_nomal_true"
                      type="textarea"
                      :width="'490px'"
                      maxlength="100"
                      v-model="TS00401_tourokuJoken.sasimodori_ziyuu"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form label-width="150px" class="row" @submit.native.prevent>
                  <el-form-item
                    label="作業担当者"
                    prop="nyuryokuerea1"
                    label-width="150px"
                  >
                    <reafsinputtext
                      maxlength="20"
                      :width="'490px'"
                      :required="true"
                      v-model="TS00401_tourokuJoken.sagyou_tantou_name"
                      :class="{ pinkcolor_text: isErrPinkSagyotsuki_text }"
                      :disabled="koumoku1"
                      @change="change_count(0)"
                      ref="tantousya_name"
                      tabindex="1"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-form label-width="150px" class="row" @submit.native.prevent>
                  <el-form-item
                    label="担当者連絡先"
                    prop="nyuryokuerea1"
                    label-width="150px"
                  >
                    <reafsinputtext
                      type="tel"
                      maxlength="14"
                      :width="'490px'"
                      :required="true"
                      v-model="TS00401_tourokuJoken.sagyoutantou_tel"
                      :class="{ pinkcolor_text: isErrPinkSagyotsuki_text }"
                      :disabled="koumoku1"
                      @change="change_count(0)"
                      ref="tantousya_tel"
                      tabindex="1"
                    ></reafsinputtext>
                  </el-form-item>
                </el-form>
              </td>
            </tr>
            <tr>
              <td colspan="2">
                <el-button
                  @click="onClose_click()"
                  class="right_flow row"
                  tabindex="1"
                >
                  閉じる</el-button
                >
                <el-button
                  type="primary"
                  @click="fnc_void_clickBtnTouroku()"
                  class="right_flow row"
                  :disabled="touroku1"
                  tabindex="1"
                  >登録
                </el-button>
              </td>
            </tr>
          </table>
        </td>

        <td>
          <!--詳細予定入力-->
          <table width="700px">
            <thead>
              <span
                style="font-size : x-large"
                class="cls-span-sagyoyoteinyuryoku"
                >詳細予定入力
              </span>
            </thead>
            <tbody>
              <tr>
                <div class="grid">
                  <reafstable
                    :single="single"
                    :rowKey="rowKey"
                    :tableData="TS00401_tourokuJoken.TS00401_grid"
                    :columns="columns2"
                    :height="500"
                    :width="600"
                    :columnIndex="false"
                    :columnChkbox="false"
                    :linkView="fnc_clickLink"
                    :key="Math.random()"
                    :row-class-name="tableRowClassName"
                    @change="change_count(0)"
                    @keydown.native="down2first"
                    tabindex="1"
                  >
                  </reafstable>
                </div>
                <div>
                  <p class="tyuuizikou">
                    ※日程を詳細に指定する際に入力してください
                  </p>
                </div>
              </tr>
            </tbody>
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
import timepicker from "@/components/basic/TimePicker.vue";

export default {
  components: {
    reafsinputtext,
    nengetsuhirange,
    nengetsu,
    reafstable,
    timepicker
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
    // TS00401にて明細情報検索に使用
    TS00401_tourokuJoken: {
      type: Object,
      default: null // 初期値nullだがTD00400が上書く
    },
    touroku1: {
      type: Boolean,
      default: false // 初期値falseだがTD00400がtrueに上書く
    },
    koumoku1: {
      type: Boolean,
      default: false // 初期値falseだがTD00400がtrueに上書く
    }
  },
  data() {
    return {
      tabindex: "",
      ///////////////////////////////////////////////
      // 閉じる処理
      ///////////////////////////////////////////////
      change_time: 0,
      ///////////////////////////////////////////////
      // ダイアログ
      ///////////////////////////////////////////////
      dialogVisible: false,
      single: false, //SingleSelet
      rowKey: undefined, //Treetable PK
      pagination: { total: 22, size: 10, sortName: "", page: 1 },
      multipleSelection: [],
      TS00401_change: {
        change: 0
      },
      ///////////////////////////////////////////////
      // グリッド
      ///////////////////////////////////////////////
      tableData2: [],
      期間外フラグ: 0,

      //１　単行表示　ページング機能使用　サンプル :columns="columns"　テーブル列の設定
      //field: Cellのデータフィールドを指定　「tableData」の項目名称（例：tableData.torihikisakicd）を使用してください
      //title：Cellのヘッダタイトルを指定
      //type：Cellの表示形を指定
      //                      string：文字列
      //                      date：日付
      //
      //formatter：Cellのデータ表示形を指定
      //width：Cellの幅を指定　数値
      //align：Cellの整列を指定　left/center/right デフォルト：left
      //link：Cellをリンク形で指定　true/false デフォルト：true
      //fixed：列を固定　true/false/left/right
      //複数行ヘッダ、複数行明細
      columns2: [
        //セルカストマイズ方法１
        {
          field: "grid_date",
          title: "作業日付",
          width: 80,
          type: "string",
          align: "center"
        },
        {
          title: "作業実施",
          width: 40,
          align: "center",
          render: (h, { row, column, index }) => {
            var self = this;
            return (
              <div>
                <el-checkbox
                  value={row.taisyougai}
                  on-input={val => {
                    row["taisyougai"] = val;
                    this.change_count(0);
                  }}
                  disabled={row["readonly"] == 1}
                ></el-checkbox>
              </div>
            );
          }
        },
        {
          title: "開始時間",
          width: 130,
          align: "left",
          render: (h, { row, column, index }) => {
            var self = this;
            return (
              <div>
                <el-time-picker
                  value={row.sagyoyotei_kaisi_time_g}
                  picker-options="{
                      selectableRange: '00:00- 23:59',
                    }"
                  format="HH:mm"
                  value-format="HH:mm"
                  on-input={val => {
                    row["sagyoyotei_kaisi_time_g"] = val;
                    this.change_count(0);
                  }}
                  on-change={val => {
                    if (row["sagyoyotei_kaisi_time_g"] == null) {
                      row["sagyoyotei_kaisi_time_g"] = "";
                    } else {
                      row["sagyoyotei_kaisi_time_g"] = val;
                    }
                    this.change_count(0);
                  }}
                  disabled={row["readonly"] == 1}
                ></el-time-picker>
              </div>
            );
          }
        },
        {
          title: "終了時間",
          width: 130,
          align: "left",
          render: (h, { row, column, index }) => {
            var self = this;
            return (
              <div>
                <el-time-picker
                  value={row.sagyoyotei_syuuryou_time_g}
                  picker-options="{
                      selectableRange: '00:00- 23:59',
                    }"
                  format="HH:mm"
                  value-format="HH:mm"
                  on-input={val => {
                    row["sagyoyotei_syuuryou_time_g"] = val;
                    row["sagyoyotei_syuuryou_Hour"] = (
                      "0" + val.getHours().toString()
                    ).slice(-2);
                    row["sagyoyotei_syuuryou_Min"] = (
                      "0" + val.getMinutes().toString()
                    ).slice(-2);
                    this.change_count(0);
                  }}
                  on-change={val => {
                    if (row["sagyoyotei_syuuryou_time_g"] == null) {
                      row["sagyoyotei_syuuryou_time_g"] = "";
                    } else {
                      row["sagyoyotei_syuuryou_time_g"] = val;
                    }
                    row["sagyoyotei_syuuryou_Hour"] = (
                      "0" + val.getHours().toString()
                    ).slice(-2);
                    row["sagyoyotei_syuuryou_Min"] = (
                      "0" + val.getMinutes().toString()
                    ).slice(-2);
                    this.change_count(0);
                  }}
                  disabled={row["readonly"] == 1}
                ></el-time-picker>
              </div>
            );
          }
        }
      ],

      ///////////////////////////////////////////////
      // エラーフラグ DATEPICKERをピンクにする為に使用
      ///////////////////////////////////////////////
      isErrPinkSagyotsuki: false,
      isErrPinkSagyotsuki_text: false,

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

      if (this.TS00401_tourokuJoken.sintyoku_CD < "530") {
        console.log("OK000");
        this.disabledable_button = false;
      } else if (this.TS00401_tourokuJoken.sintyoku_CD >= "530") {
        console.log("OK001");
        this.disabledable_button = true;
      }
    });
  },
  mounted() {
    console.log("TS00401");
  },
  updated() {
    document.querySelector(".v-modal").style.zIndex = "3000";
    document.querySelector(".SagyoYoteiNyuryoku").style.zIndex = "3001";
  },
  methods: {
    onOpen() {
      //サブ画面を起動するタイミングで、1つ目のコントロールに設定する
      // this.$nextTick(function() {
      //   this.$refs.ref予定日.focus();
      // });
    },

    //   window: (onload = function() {
    //     alert("click_ServerAccessChk");
    //   }),
    ///////////////////////////////////////////////
    // 作業予定日
    ///////////////////////////////////////////////

    onblur() {
      var 予定開始日;
      var 予定終了日;
      var 予定開始時間;
      var 予定終了時間;

      if (this.TS00401_tourokuJoken.yoteikaisi_date != null) {
        if (this.TS00401_tourokuJoken.yoteikaisi_date.constructor == Date) {
          予定開始日 = this.fnc_obj_NengetsuConvert(
            this.TS00401_tourokuJoken.yoteikaisi_date
          );
        } else {
          予定開始日 = this.TS00401_tourokuJoken.yoteikaisi_date;
        }
      } else {
        予定開始日 = "";
      }
      if (this.TS00401_tourokuJoken.yoteisyuuryou_date != null) {
        if (this.TS00401_tourokuJoken.yoteisyuuryou_date.constructor == Date) {
          予定終了日 = this.fnc_obj_NengetsuConvert(
            this.TS00401_tourokuJoken.yoteisyuuryou_date
          );
        } else {
          予定終了日 = this.TS00401_tourokuJoken.yoteisyuuryou_date;
        }
      } else {
        予定終了日 = "";
      }
      if (this.TS00401_tourokuJoken.sagyoyotei_kaisi_time != null) {
        if (
          this.TS00401_tourokuJoken.sagyoyotei_kaisi_time.constructor == Date
        ) {
          予定開始時間 = this.fnc_obj_TimeConvert(
            this.TS00401_tourokuJoken.sagyoyotei_kaisi_time
          );
        } else if (
          this.TS00401_tourokuJoken.sagyoyotei_kaisi_time.length == 4
        ) {
          let 時間 = this.TS00401_tourokuJoken.sagyoyotei_kaisi_time.substr(2);
          let 分 = this.TS00401_tourokuJoken.sagyoyotei_kaisi_time.substr(3, 2);
          this.TS00401_tourokuJoken.sagyoyotei_kaisi_time = 時間 + ":" + 分;
        } else {
          予定開始時間 = this.TS00401_tourokuJoken.sagyoyotei_kaisi_time;
        }
      } else {
        予定開始時間 = "";
      }
      if (this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time != null) {
        if (
          this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time.constructor == Date
        ) {
          予定終了時間 = this.fnc_obj_TimeConvert(
            this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time
          );
        } else if (
          this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time.length == 4
        ) {
          let 時間 = this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time.substr(
            2
          );
          let 分 = this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time.substr(
            3,
            2
          );
          this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time = 時間 + ":" + 分;
        } else {
          予定終了時間 = this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time;
        }
      } else {
        予定終了時間 = "";
      }

      if (
        //予定日開始あり・予定日終了なし
        (this.TS00401_tourokuJoken.yoteikaisi_date != "" ||
          this.TS00401_tourokuJoken.yoteikaisi_date != null) &&
        (this.TS00401_tourokuJoken.yoteisyuuryou_date == "" ||
          this.TS00401_tourokuJoken.yoteisyuuryou_date == null)
      ) {
        this.TS00401_tourokuJoken.yoteisyuuryou_date = this.TS00401_tourokuJoken.yoteikaisi_date;
      } else if (
        //予定日開始なし・予定日終了あり
        (this.TS00401_tourokuJoken.yoteikaisi_date == "" ||
          this.TS00401_tourokuJoken.yoteikaisi_date == null) &&
        (this.TS00401_tourokuJoken.yoteisyuuryou_date != "" ||
          this.TS00401_tourokuJoken.yoteisyuuryou_date != null)
      ) {
        this.TS00401_tourokuJoken.yoteikaisi_date = this.TS00401_tourokuJoken.yoteisyuuryou_date;
      }
      //カレンダー表示処理
      if (予定開始日 != "" && 予定終了日 != "") {
        let kariire = this.TS00401_tourokuJoken.TS00401_grid;

        for (let nakami in kariire) {
          if (kariire[nakami].hikaku == 予定開始日) {
            kariire[nakami].taisyougai = true;
            kariire[nakami].readonly = 0;
          } else if (kariire[nakami].hikaku == 予定終了日) {
            kariire[nakami].taisyougai = true;
            kariire[nakami].readonly = 0;
          } else if (
            予定開始日 <= kariire[nakami].hikaku &&
            kariire[nakami].hikaku <= 予定終了日
          ) {
            kariire[nakami].taisyougai = true;
            kariire[nakami].readonly = 0;
          } else {
            kariire[nakami].taisyougai = false;
            kariire[nakami].readonly = 1;
          }
        }
      }
    },

    ///////////////////////////////////////////////
    // ×ボタンまたは閉じるボタンクリック時処理
    ///////////////////////////////////////////////
    //変更あり
    change_count(no) {
      if (no == 1) {
      } else {
        this.change_time += 1;
      }
    },

    onClose_click() {
      if (this.change_time === 0) {
        this.fnc_void_clearData();

        this.fnc_void_closeWindow();
      } else {
        if (
          ((!this.TS00401_tourokuJoken.check_yoteikaisi_date &&
            !this.TS00401_tourokuJoken.yoteikaisi_date) ||
            this.TS00401_tourokuJoken.check_yoteikaisi_date ===
              this.TS00401_tourokuJoken.yoteikaisi_date) &&
          ((!this.TS00401_tourokuJoken.check_sagyoyotei_kaisi_time &&
            !this.TS00401_tourokuJoken.sagyoyotei_kaisi_time) ||
            this.TS00401_tourokuJoken.check_sagyoyotei_kaisi_time ===
              this.TS00401_tourokuJoken.sagyoyotei_kaisi_time) &&
          ((!this.TS00401_tourokuJoken.check_yoteisyuuryou_date &&
            !this.TS00401_tourokuJoken.yoteisyuuryou_date) ||
            this.TS00401_tourokuJoken.check_yoteisyuuryou_date ===
              this.TS00401_tourokuJoken.yoteisyuuryou_date) &&
          ((!this.TS00401_tourokuJoken.check_sagyoyotei_syuuryou_time &&
            !this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time) ||
            this.TS00401_tourokuJoken.check_sagyoyotei_syuuryou_time ===
              this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time) &&
          ((!this.TS00401_tourokuJoken.check_sagyou_tantou_name &&
            !this.TS00401_tourokuJoken.sagyou_tantou_name) ||
            this.TS00401_tourokuJoken.check_sagyou_tantou_name ===
              this.TS00401_tourokuJoken.sagyou_tantou_name) &&
          ((!this.TS00401_tourokuJoken.check_sagyoutantou_tel &&
            !this.TS00401_tourokuJoken.sagyoutantou_tel) ||
            this.TS00401_tourokuJoken.check_sagyoutantou_tel ===
              this.TS00401_tourokuJoken.sagyoutantou_tel) &&
          ((!this.TS00401_tourokuJoken.check_sagyou_yotei_bikou &&
            !this.TS00401_tourokuJoken.sagyou_yotei_bikou) ||
            this.TS00401_tourokuJoken.check_sagyou_yotei_bikou ===
              this.TS00401_tourokuJoken.sagyou_yotei_bikou) &&
          this.TS00401_tourokuJoken.check_grid_date === this.TS00401_grid
        ) {
          this.fnc_void_clearData();

          this.fnc_void_closeWindow();

          return true;
        } else {
          this.fnc_void_showErrorMsg_close("K000001"); // 確認メッセージ出力
        }
      }
    },

    ///////////////////////////////////////////////
    // 登録前処理
    ///////////////////////////////////////////////
    async fnc_void_clickBtnTouroku() {
      var strs = this.TS00401_tourokuJoken.yoteikaisi_date;
      var stre = this.TS00401_tourokuJoken.yoteisyuuryou_date;
      var strs_t = this.TS00401_tourokuJoken.sagyoyotei_kaisi_time;
      var stre_t = this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time;
      //日付チェック・コンバート
      if (
        this.TS00401_tourokuJoken.yoteikaisi_date === "" ||
        this.TS00401_tourokuJoken.yoteikaisi_date === null ||
        this.TS00401_tourokuJoken.yoteisyuuryou_date === "" ||
        this.TS00401_tourokuJoken.yoteisyuuryou_date === null
      ) {
        console.log("エラーチェック");
        this.isErrPinkSagyotsuki = true; //背景色ピンク
        this.fnc_void_showErrorMsg("E040701"); // 請求書月分を入力して下さい。
        this.$nextTick(() => this.$refs.nengetu_from.focus());
        exit;
      } else {
      }
      //予定開始日
      if (this.TS00401_tourokuJoken.yoteikaisi_date.constructor == Date) {
        strs = this.fnc_obj_NengetsuConvert(strs);
        this.TS00401_tourokuJoken.yoteikaisi_date = strs;
      } else {
      }

      //予定終了日
      if (this.TS00401_tourokuJoken.yoteisyuuryou_date.constructor == Date) {
        stre = this.fnc_obj_NengetsuConvert(stre);
        this.TS00401_tourokuJoken.yoteisyuuryou_date = stre;
      } else {
      }
      //予定開始時間
      if (this.TS00401_tourokuJoken.sagyoyotei_kaisi_time != null) {
        if (
          this.TS00401_tourokuJoken.sagyoyotei_kaisi_time.constructor == Date
        ) {
          strs_t = this.fnc_obj_TimeConvert(strs_t);
          this.TS00401_tourokuJoken.sagyoyotei_kaisi_time = strs_t;
        } else if (
          this.TS00401_tourokuJoken.sagyoyotei_kaisi_time.length >= 10
        ) {
          let strs_t1 = strs_t.substr(11, 5);
          // let strs_t2 = strs_t1.substr(5);
          this.TS00401_tourokuJoken.sagyoyotei_kaisi_time = strs_t1;
        } else {
        }
      } else {
        this.TS00401_tourokuJoken.sagyoyotei_kaisi_time = "";
      }

      //予定終了時間
      if (this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time != null) {
        if (
          this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time.constructor == Date
        ) {
          stre_t = this.fnc_obj_TimeConvert(stre_t);
          this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time = stre_t;
        } else if (
          this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time.length >= 10
        ) {
          let stre_t1 = stre_t.substr(11, 5);
          // let stre_t2 = stre_t1.substr(5);
          this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time = stre_t1;
        } else {
        }
      } else {
        this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time = "";
      }

      //PG入力
      let userLogIn = this.$store.getters.getUserInfo();
      this.TS00401_tourokuJoken.user_id = userLogIn.userId;

      //グリッド期間外チェック
      this.fnv_check_grit(0);

      //登録前エラーチェック
      let letJdg;
      letJdg = await this.fnc_obj_TourokumaeErrorChk();
      if (letJdg === false) {
        exit;
      }

      // // 前準備。検索条件をバックに渡す用にコンバート
      // let ConvertDataAfter = this.TS00401_tourokuJoken;
      // this.fnc_obj_KensakuJokenConvert(ConvertDataAfter);

      //排他チェック
      if (await this.fnc_check_haita()) {
        // 処理事前確認
        this.fnc_void_showTourokuMsg("K000004");
      } else {
        this.fnc_void_showErrorMsg("E040098"); // 他の端末で更新が発生しました。再度読込んで下さい。
      }
    },

    //排他チェック
    fnc_check_haita() {
      let _this = this;
      let letval_check = new Object();
      letval_check = this.TS00401_tourokuJoken;
      let kekka = "";
      return new Promise(resolve => {
        _this.http
          .post(
            "/api/Reafs_T/Sagyo/TS00401402_GET_haitacheck",
            letval_check,
            "確認しています...."
          )
          .then(function(response) {
            if (response.status) {
              if (
                response.data[0].new_check_version == letval_check.check_version
              ) {
                resolve(true);
              } else {
                resolve(false);
              }
            } else {
              _this.MsgErr({
                title: "作業予定入力",
                message: response.message
              });
              exit;
            }
          });
      });
    },

    // 登録前エラーチェック
    async fnc_obj_TourokumaeErrorChk() {
      var strs = this.TS00401_tourokuJoken.yoteikaisi_date;
      var stre = this.TS00401_tourokuJoken.yoteisyuuryou_date;
      var time_from = this.TS00401_tourokuJoken.sagyoyotei_kaisi_time;
      var time_to = this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time;
      var tantoutel_bit = this.fnc_obj_getByteLength(
        this.TS00401_tourokuJoken.sagyoutantou_tel
      );
      if (
        //予定日開始・終了あり　時間逆転チェック
        strs == stre
      ) {
        if (time_from != "" && time_to != "")
          if (time_from > time_to) {
            this.fnc_void_showErrorMsg("E040409"); //予定日時に逆転があります。
            this.fnc_void_pinkData(0); //背景色ピンク
            this.$nextTick(() => this.$refs.nengetu_from.focus());
            return false;
          } else {
          }
        else {
        }
      } else if (
        // 日付逆転チェック
        strs > stre
      ) {
        this.fnc_void_showErrorMsg("E040409"); // 予定日時に逆転があります。
        this.fnc_void_pinkData(0); //背景色ピンク
        this.$nextTick(() => this.$refs.nengetu_from.focus());
        return false;
      }
      // if (strs.substr(0, 7) !== stre.substr(0, 7)) {
      //   //作業予定月が同月ではない
      //   this.fnc_void_showErrorMsg("E040702"); // 請求書月分を入力して下さい。
      //   this.fnc_void_pinkData(0); //背景色ピンク
      //   this.$nextTick(() => this.$refs.nengetu_from.focus());
      //   return false;
      // } else
      if (
        ((this.TS00401_tourokuJoken.sagyo_date_hikaku == strs.substr(0, 7) &&
          strs.substr(8, 2) > "20") ||
          (this.TS00401_tourokuJoken.sagyo_date_hikaku == stre.substr(0, 7) &&
            stre.substr(8, 2) > "20")) &&
        (this.TS00401_tourokuJoken.sagyou_yotei_bikou == "" ||
          this.TS00401_tourokuJoken.sagyou_yotei_bikou == null)
      ) {
        //作業予定日が20日以上でかつ作業予定がない
        this.fnc_void_showErrorMsg("E040703"); // 請求書月分を入力して下さい。
        this.fnc_void_pinkData(0); //背景色ピンク
        this.$nextTick(() => this.$refs.nengetu_from.focus());
        return false;
      } else if (
        this.TS00401_tourokuJoken.kakutei_flag > strs.substr(0, 7) ||
        this.TS00401_tourokuJoken.kakutei_flag > stre.substr(0, 7)
      ) {
        //作業予定日が経理年月以前の場合
        this.fnc_void_showErrorMsg("E040418"); // 請求書月分を入力して下さい。
        this.fnc_void_pinkData(0); //背景色ピンク
        this.$nextTick(() => this.$refs.nengetu_from.focus());
        return false;
      } else if (
        this.TS00401_tourokuJoken.sagyou_tantou_name === "" ||
        this.TS00401_tourokuJoken.sagyou_tantou_name === null
      ) {
        //作業担当が空の場合
        this.fnc_void_showErrorMsg("E040704"); // 請求書月分を入力して下さい。
        this.fnc_void_pinkData(1); //背景色ピンク
        this.$nextTick(() => this.$refs.tantousya_name.focus());
        return false;
      } else if (
        this.TS00401_tourokuJoken.sagyoutantou_tel === "" ||
        this.TS00401_tourokuJoken.sagyoutantou_tel === null
      ) {
        //作業担当連絡先が空の場合
        this.fnc_void_showErrorMsg("E040704"); // 請求書月分を入力して下さい。
        this.fnc_void_pinkData(1); //背景色ピンク
        this.$nextTick(() => this.$refs.tantousya_tel.focus());
        return false;
      } else if (tantoutel_bit > 20) {
        //作業担当者連絡先のバイト数を超えた場合
        this.fnc_void_showErrorMsg("E040764"); // 請求書月分を入力して下さい。
        this.fnc_void_pinkData(1); //背景色ピンク
        this.$nextTick(() => this.$refs.tantousya_tel.focus());
        return false;
      } else if (this.期間外フラグ >= 1) {
        let msg = (await this.fnc_rtn_getMsg("K040071")).data;
        if (!(await this.confirm(msg, "作業予定入力"))) {
          this.期間外フラグ = 0;
          return false;
        } else {
          this.期間外フラグ = 0;
          return true;
        }
      } else {
        return true;
      }
    },
    //グリッド期間外チェック
    //int=0 ⇒期間外フラグ用
    //int=1 ⇒期間外データ削除用
    fnv_check_grit(int) {
      let グリッド = this.TS00401_tourokuJoken.TS00401_grid;
      if (int == 0) {
        for (let n in グリッド) {
          if (
            (グリッド[n].sagyoyotei_kaisi_time_g == "" &&
              グリッド[n].sagyoyotei_kaisi_time_g == null) ||
            グリッド[n].sagyoyotei_syuuryou_time_g == "" ||
            グリッド[n].sagyoyotei_syuuryou_time_g == null
          ) {
          } else {
            if (グリッド[n].readonly == 1) {
              this.期間外フラグ += 1;
            } else {
            }
          }
        }
      } else if (int == 1) {
        for (let n in グリッド) {
          if (
            (グリッド[n].sagyoyotei_kaisi_time_g == "" &&
              グリッド[n].sagyoyotei_kaisi_time_g == null) ||
            グリッド[n].sagyoyotei_syuuryou_time_g == "" ||
            グリッド[n].sagyoyotei_syuuryou_time_g == null
          ) {
          } else {
            if (グリッド[n].readonly == 1) {
              グリッド[n].sagyoyotei_kaisi_time_g = "";
              グリッド[n].sagyoyotei_syuuryou_time_g = "";
            } else {
            }
          }
        }
      }
    },
    // 登録前ピンク色等データ
    fnc_void_pinkData(int) {
      let _this = this;
      if (int == 0) {
        _this.isErrPinkSagyotsuki = true;
      } else if (int == 1) {
        _this.isErrPinkSagyotsuki_text = true;
      }
    },

    // 登録前ピンク色等データクリア
    fnc_void_clearData() {
      let _this = this;
      _this.isErrPinkSagyotsuki = false;
      _this.isErrPinkSagyotsuki_text = false;
      _this.change_time = 0;
      _this.TS00401_tourokuJoken.keiyakukanri_no = "";
      _this.TS00401_tourokuJoken.bukken_name = "";
      _this.TS00401_tourokuJoken.keiyaku_name = "";
      _this.TS00401_tourokuJoken.sagyou_c_dai = "";
      _this.TS00401_tourokuJoken.sagyou_c_tyuu = "";
      _this.TS00401_tourokuJoken.sagyou_c_syou = "";
      _this.TS00401_tourokuJoken.sagyou_name = "";
      _this.TS00401_tourokuJoken.sagyou_tuki_kaisuu = "";
      _this.TS00401_tourokuJoken.yoteikaisi_date = "";
      _this.TS00401_tourokuJoken.yoteisyuuryou_date = "";
      _this.TS00401_tourokuJoken.sagyoyotei_kaisi_time = "";
      _this.TS00401_tourokuJoken.sagyoyotei_syuuryou_time = "";
      _this.TS00401_tourokuJoken.sagyou_yotei_bikou = "";
      _this.TS00401_tourokuJoken.sasimodori_ziyuu = "";
      _this.TS00401_tourokuJoken.sagyou_tantou_name = "";
      _this.TS00401_tourokuJoken.sagyoutantou_tel = "";
      _this.TS00401_grid = [];
    },

    ///////////////////////////////////////////////
    // 登録処理
    ///////////////////////////////////////////////
    async fnc_void_Touroku() {
      let _this = this;

      // ピンク色等データ初期状態にクリア
      //this.fnc_void_clearData(0);

      //登録データ渡し
      let letval_touroku = new Object();
      //期間外のデータを削除
      this.fnv_check_grit(1);

      letval_touroku = _this.TS00401_tourokuJoken;

      console.log(letval_touroku);

      await _this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00401_PUT_TourokuKekka",
          letval_touroku,
          "登録しています...."
        )
        .then(function(response) {
          if (response.status) {
            _this.fnc_void_clearData();
            _this.fnc_void_showInfoMsg("M000008");
            _this.fnc_void_sendMail();
            _this.fnc_void_closeWindow();
          } else {
            console.log(response.message);
            _this.MsgErr({
              title: "作業予定入力",
              message: response.message
            });
          }
        });
    },
    fnc_void_sendMail() {
      let resultMail = "";

      let param = {
        msg_type: 0,
        pg_id: "TS00401",
        st_kbn: 2,
        mail_pattern_no: 15,
        工事依頼NO: this.TS00401_tourokuJoken.kouziirai_no,
        契約NO: this.TS00401_tourokuJoken.keiyaku_no,
        履歴NO: this.TS00401_tourokuJoken.rireki_no,
        明細NO: this.TS00401_tourokuJoken.meisai_no,
        契約年月: this.TS00401_tourokuJoken.keiyaku_nengetu,
        宛先事務所コード: [this.TS00401_tourokuJoken.sendMailJimusyoCode],
        宛先営業所コード: [this.TS00401_tourokuJoken.sendMailEigyosyoCode],
        宛先部コード: [this.TS00401_tourokuJoken.sendMailBuCode],
        宛先課コード: [this.TS00401_tourokuJoken.sendMailKaCode],
        宛先係コード: [this.TS00401_tourokuJoken.sendMailKakariCode]
      };

      resultMail = this.commonFunctionUI.CM00110SendMail(param);

      if (resultMail.result_id !== 0) {
        console.log("メールエラー！ " + resultMail.err_msg);
        // メール送信時のエラー
      }
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

      lethour = ("0" + sagyoudate.getHours().toString()).slice(-2);
      letminues = ("0" + sagyoudate.getMinutes().toString()).slice(-2);
      letTime = lethour + ":" + letminues;

      return letTime;
    },
    ///////////////////////////////////////////////
    // バイトチェック
    ///////////////////////////////////////////////
    fnc_obj_getByteLength(str) {
      str = str == null ? "" : str;
      return encodeURI(str).replace(/%../g, "*").length;
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
          title: "作業予定入力",
          message: response.data
        });
      });
    },
    // エラーメッセージ
    fnc_void_showErrorMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "作業予定入力",
          message: response.data
        });
      });
    },
    confirm(message, title) {
      return new Promise((resolve, reject) => {
        this.MsgConf({
          title,
          message,
          callback: function(a, b) {
            if (a === "confirm") {
              resolve(true);
            } else {
              resolve(false);
            }
          }
        });
      });
    },

    //登録ボタン処理
    async fnc_void_showTourokuMsg(MsgCode) {
      if (
        this.TS00401_tourokuJoken.yoteisyuuryou_date.substr(0, 7) !==
        this.TS00401_tourokuJoken.sagyo_date_hikaku.substr(0, 7)
      ) {
        MsgCode = "K040076";
      } else {
      }
      await this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgConf(
          {
            title: "作業予定入力",
            message: response.data
          },
          () => {
            // OKが選択された場合
            //登録結果取得
            this.fnc_void_Touroku();
            return true;
          },
          () => {
            // Cancelが選択された場合
            // this.$alert('Cancelが選択された場合')
            return false;
          }
        );
      });
    },

    //閉じるボタン処理
    fnc_void_showErrorMsg_close(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgConf(
          {
            title: "作業予定入力",
            message: response.data
          },
          () => {
            // OKが選択された場合
            this.fnc_void_clearData();

            this.fnc_void_closeWindow();
            return true;
          },
          () => {
            // Cancelが選択された場合
            // this.$alert('Cancelが選択された場合')
            return false;
          }
        );
      });
    },
    selectionChange(selection) {
      if (this.single && selection.length == 1) {
        this.$emit("rowChange", selection[1]); //
      }
      if (this.single && selection.length > 1) {
        this.$refs.table.toggleRowSelection(selection[1]);
      }
    },
    down2first(event) {
      if (event.keyCode === 9) {
        this.$nextTick(function() {
          this.$refs.ref予定日.focus();
          event.preventDefault();
        });
      }
    },
    fnc_void_closeWindow() {
      console.log("close_TS00401");

      // dialogVisibleにfalseをセットすることでダイアログを閉じる
      this.$emit("update:dialogVisible", false);
      this.$emit("closePopupEvent", this);
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
  margin-top: 6vh !important;
  overflow: scroll;
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
.body_table {
  width: 1400px;
  height: 90%;
  min-width: 700px;
  min-width: 700px;
  overflow: scroll;
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
.left_flow {
  float: left;
  margin: 30px;
}
.right_flow {
  float: right;
  margin: 10px;
}

.reafs-input .el-input__inner {
  height: 30px;
  width: 200px;
  line-height: 30px;
  border-radius: 1px;
}

.reafs-input-required .el-input__inner {
  height: 30px;
  line-height: 30px;
  border-radius: 1px;
  background: lightgoldenrodyellow;
}
.reafs-input-required .el-input__inner .textarea {
  background: lightgoldenrodyellow;
}
.el-button {
  padding-top: 7px;
  padding-bottom: 7px;
  margin-left: -4px;
  border-start-start-radius: 1px;
  border-end-start-radius: 1px;
}
.row_x {
  display: inline;
  margin: 0;
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
  width: 700px;
}
.row {
  margin: 0 5px;
}
.row_time {
  display: inline;
  margin: -4px;
}
</style>
<style scoped>
.row_x >>> .el-input__inner {
  width: 140px;
}
.row_time >>> .el-input__inner {
  width: 105px;
}
.grid_time {
  width: 200px;
}
.el-form .multi >>> .el-form-item__label {
  height: 94px;
}
.el-form .multi_text >>> .el-form-item__label {
  height: 52px;
}
.nengetsu >>> .el-input__inner {
  height: 30px;
  width: 130px;
  margin: 0;
  line-height: 30px;
  border-radius: 1px;
  padding-right: 5px;
}
.nengetsu-required >>> .el-input__inner {
  height: 30px;
  width: 130px;
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
.hissutext >>> .el-textarea__inner {
  background: lightgoldenrodyellow;
}

.hissutext >>> .el-input__inner {
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
.is-readonly {
}
.readonly_text {
  padding-left: 2px;
  padding: 0.5px 0;
  margin: 0 0 1px 0;
}
.pinkcolor >>> .el-input__inner {
  background-color: lightpink !important;
}
.pinkcolor_text >>> .el-input__inner {
  background-color: lightpink !important;
}
</style>
