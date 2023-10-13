<template>
  <el-dialog
    width="1500px"
    :visible="dialogVisible"
    @close="onClose_click()"
    :close-on-click-modal="false"
    ref="onloadCon"
    class="SagyoHoukokuNyuryoku onloadCon"
    :show-close="false"
  >
    <div width="100%" height="100%" overflow="auto">
      <div width="1400px">
        <tr>
          <td class="align_top">
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
                    <el-form-item
                      label="契約管理番号"
                      label-width="150px"
                      class="row"
                    >
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.契約管理番号"
                        class="is-readonly readonly_text"
                        maxlength="8"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                    <el-form-item
                      label="物件名"
                      label-width="150px"
                      class="row"
                    >
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.物件名"
                        class="is-readonly readonly_text"
                        maxlength="40"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                    <el-form-item
                      label="営業店"
                      label-width="150px"
                      class="row"
                    >
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.営業店"
                        class="is-readonly readonly_text"
                        maxlength="40"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                    <el-form-item
                      label="契約名"
                      label-width="150px"
                      class="row"
                    >
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.契約名"
                        class="is-readonly readonly_text"
                        maxlength="40"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                  </el-form>
                </td>
              </tr>
              <tr>
                <td rowspan="3" width="84px">
                  <div class="row">
                    <p class="p-sagyocategory " align="center">作業カテゴリ</p>
                  </div>
                </td>
                <td>
                  <el-form label-width="60px">
                    <el-form-item label="大区分">
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.大区分"
                        class="is-readonly"
                        maxlength="40"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                  </el-form>
                </td>
              </tr>
              <tr>
                <td>
                  <el-form label-width="60px">
                    <el-form-item label="中区分">
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.中区分"
                        class="is-readonly"
                        maxlength="40"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                  </el-form>
                </td>
              </tr>
              <tr>
                <td>
                  <el-form label-width="60px">
                    <el-form-item label="小区分">
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.小区分"
                        class="is-readonly"
                        maxlength="40"
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
                    <el-form-item
                      label="作業名称"
                      label-width="150px"
                      class="row"
                    >
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.作業名称"
                        class="is-readonly readonly_text"
                        maxlength="40"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                    <el-form-item
                      label="作業月/回目"
                      label-width="150px"
                      class="row"
                    >
                      <reafsinputtext
                        v-model="TS00402_tourokuJoken.作業月"
                        class="is-readonly readonly_text"
                        maxlength="15"
                        disabled="disabled"
                        :width="'490px'"
                      >
                      </reafsinputtext>
                    </el-form-item>
                    <el-form-item
                      label="現地確認あり"
                      prop="nyuryokuerea6"
                      label-width="150px"
                      class="row"
                    >
                      <el-checkbox
                        class="cls-chk-paddnig-left tyuuizikou"
                        label="報告書等に現地での確認印等が得られている"
                        v-model="TS00402_tourokuJoken.現地確認あり"
                        @change="change_count(1)"
                        :disabled="左側"
                        ></el-checkbox>
                      <input
                        type="checkbox"
                        class="dairi_checkbox"
                        ref="ref現地確認"
                        @change="fnc_checkbox()"
                        v-model="TS00402_tourokuJoken.現地確認あり"
                        tabindex="1"
                      ></input>
                    </el-form-item>
                    <div class="nengetsu">
                      <el-form-item
                        label="作業実施日"
                        label-width="150px"
                        class="row"
                      >
                        <el-date-picker
                          :required="true"
                          type="date"
                          format="yyyy/MM/dd"
                          :clearable="false"
                          width="260px"
                          range-separator="~"
                          :class="{ pinkcolor: isErrPinkSagyotsuki }"
                          v-model="TS00402_tourokuJoken.作業実施日_from"
                          :disabled="左側"
                          @change="change_count(0)"
                          @blur="onblur()"
                          ref="ref予定日"
                          tabindex="2"
                        ></el-date-picker>
                        ~
                        <el-date-picker
                          :required="true"
                          type="date"
                          format="yyyy/MM/dd"
                          :clearable="false"
                          width="260px"
                          range-separator="~"
                          :class="{ pinkcolor: isErrPinkSagyotsuki }"
                          v-model="TS00402_tourokuJoken.作業実施日_to"
                          :disabled="左側"
                          @change="change_count(0)"
                          @blur="onblur()"
                          tabindex="3"
                        ></el-date-picker>
                      </el-form-item>
                    </div>
                    <el-form-item
                      label="完了報告"
                      label-width="150px"
                      class="multi row"
                    >
                      <div class="hissutext">
                        <reafsinputtext
                          type="textarea"
                          show-word-limit
                          :width="'490px'"
                          maxlength="100"
                          v-model="TS00402_tourokuJoken.完了報告"
                          :disabled="左側"
                          @change="change_count(4)"
                          class="bikou_hight"
                          ref="ref完了報告"
                          tabindex="4"
                        ></reafsinputtext>
                      </div>
                    </el-form-item>
                  </el-form>
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <el-form
                    label-width="150px"
                    class="row"
                    @submit.native.prevent
                  >
                    <el-form-item
                      label="差戻事由"
                      label-width="150px"
                      class="multi"
                    >
                      <reafsinputtext
                        :disabled="disabledable_text_nomal_true"
                        class="is-readonly bikou_hight"
                        type="textarea"
                        :width="'490px'"
                        maxlength="100"
                        v-model="TS00402_tourokuJoken.差戻事由"
                      ></reafsinputtext>
                    </el-form-item>
                    <el-form-item
                      label="作業担当者"
                      prop="nyuryokuerea1"
                      label-width="150px"
                    >
                      <reafsinputtext
                        maxlength="20"
                        :width="'490px'"
                        :required="true"
                        :class="{
                          pinkcolor_text: isErrPinkSagyotsuki_text
                        }"
                        v-model="TS00402_tourokuJoken.作業担当者"
                        :disabled="左側"
                        @change="change_count(4)"
                        ref="ref担当者"
                        tabindex="6"
                      ></reafsinputtext>
                    </el-form-item>
                    <el-form-item
                      label="担当者連絡先"
                      prop="nyuryokuerea1"
                      label-width="150px"
                    >
                      <reafsinputtext
                        maxlength="14"
                        :width="'490px'"
                        :required="true"
                        :class="{
                          pinkcolor_text: isErrPinkSagyotsuki_text
                        }"
                        v-model="TS00402_tourokuJoken.担当者連絡先"
                        :disabled="左側"
                        @change="change_count(4)"
                        ref="tantousya_tel"
                        tabindex="7"
                      ></reafsinputtext>
                    </el-form-item>
                  </el-form>
                </td>
              </tr>
            </table>

            <table class="table_button">
              <tr class="right_flow">
                <td colspan="2">
                  <el-button class="right_flow row" @click="onClose_click()" tabindex="8">
                    閉じる</el-button
                  >
                  <el-button
                    type="primary"
                    @click="fnc_void_clickBtnTouroku(TS00402_tourokuJoken)"
                    class="right_flow row"
                    :disabled="登録ボタン"
                    tabindex="9"
                    >登録
                  </el-button>
                </td>
              </tr>
            </table>
          </td>

          <div class="align_top">
            <!--報告書等添付-->
            <div class="cls-tbl" cellspacing="0px">
              <span
                style="font-size : x-large"
                class="cls-span-sagyoyoteinyuryoku row"
                >新規添付</span
              >
              <div>
                <el-form
                  :model="TS00402_tourokuJoken"
                  ref="TS00402_tourokuJoken"
                  label-width="120px"
                  class="search-form-class"
                  :rules="rules"
                >
                  <el-form-item label="添付ファイルパス" prop="添付">
                    <div
                      style="display: flex; align-content: center; padding-top: 1px"
                    >
                      <div>
                        <div class="transition-box">
                          <el-upload
                            drag
                            :multiple="false"
                            action=""
                            :on-change="dragChange"
                            :show-file-list="true"
                            :auto-upload="false"
                            :file-list="fileList"
                            tabindex="10"
                          >
                            <div
                              class="img-label"
                              style="padding-left: 15px"
                              ref="privacyPolicy"
                            >
                              <label maxlength="100">{{
                                zumenForm.添付ファイル_パス
                              }}</label>
                            </div>
                          </el-upload>
                        </div>
                      </div>
                      <div>
                        <el-button
                          style="margin-left: 2px"
                          size="mini"
                          @click="openFileDialog()"
                          :loading="loading['btnUploadFile']"
                          tabindex="11"
                          >参照</el-button
                        >
                      </div>
                    </div>
                  </el-form-item>
                </el-form>
              </div>
            </div>
            <div>
              <el-form>
                <el-form-item class="left_flow" label-width="0px">
                  <el-select
                    v-model="TS00402_tourokuJoken.新規添付種類"
                    placeholder="Select"
                    tabindex="12"
                  >
                    <el-option
                      v-for="item in 種類名称"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                  <el-button
                    type="primary"
                    @click="onRegister()"
                    :disabled="touroku"
                    class="row"
                    tabindex="13"
                    >登録
                  </el-button>
                </el-form-item>
              </el-form>
            </div>
            <table class="cls-tbl" cellspacing="0px">
              <!--報告書等添付-->
              <tr>
                <td>
                  <span
                    style="font-size : x-large"
                    class="cls-span-sagyoyoteinyuryoku row"
                    >添付書類</span
                  >
                </td>
              </tr>
              <tr>
                <td>
                  <div class="grid-content">
                    <reafstable
                      ref="table"
                      :tableData="TS00402_tourokuJoken.TS00402_grid"
                      :columns="tblColumns_file"
                      :maxHeight="130"
                      :resizeAble="true"
                      :cellStyleOption="cellStyleOption"
                      rowKeyFieldName="key"
                      :class="tableClassFilter"
                      :disableSelectedRowKeys="disableSelectedRowKeys"
                      :sortOption="sortOption"
                      :HiddenColumnKeys="hiddenColumnMishoninShonin"
                      tabindex="14"
                    >
                    </reafstable>
                  </div>
                </td>
              </tr>
            </table>

            <table class="cls-tbl" cellspacing="0px">
              <!-- 月末請求申請欄（作業毎月毎に同じ内容が表示されます） -->
              <tr>
                <td colspan="3">
                  <span
                    style="font-size : x-large"
                    class="cls-span-sagyoyoteinyuryoku row"
                    >月末請求申請欄（作業毎月毎に同じ内容が表示されます）
                  </span>
                </td>
              </tr>
              <tr class="table_kingaku">
                <td>
                  <div class="row_y">
                    <el-form @submit.native.prevent>
                      <el-form-item label="例月請求額税抜" label-width="135px">
                        <reafsinputtext
                          v-model="TS00402_tourokuJoken.例月請求額税抜"
                          class="is-readonly kingaku"
                          maxlength="12"
                          disabled="disabled"
                          :width="'120px'"
                        >
                        </reafsinputtext>
                      </el-form-item>
                      <el-form-item
                        label="作業合計請求税抜"
                        label-width="135px"
                      >
                        <reafsinputtext
                          v-model="TS00402_tourokuJoken.作業合計請求税抜"
                          class="is-readonly kingaku"
                          maxlength="12"
                          disabled="disabled"
                          :width="'120px'"
                        >
                        </reafsinputtext>
                      </el-form-item>
                    </el-form>
                  </div>
                </td>
                <td>
                  <div class="grid-content_kingaku">
                    <reafstable
                      ref="table"
                      :tableData="TS00402_tourokuJoken.TS00402_grid_kingaku"
                      :columns="tblColumns_kingaku"
                      :maxHeight="140"
                      :resizeAble="true"
                      :cellStyleOption="cellStyleOption"
                      rowKeyFieldName="key"
                      :class="tableClassFilter"
                      :disableSelectedRowKeys="disableSelectedRowKeys"
                      :sortOption="sortOption"
                      :HiddenColumnKeys="hiddenColumnMishoninShonin"
                      tabindex="15"
                    >
                    </reafstable>
                  </div>
                </td>
              </tr>

              <tr class="table_kingaku">
                <td>
                  <div class="row_y">
                    <el-form @submit.native.prevent>
                      <el-form-item
                        label="実費請求金額税抜"
                        label-width="135px"
                      >
                        <reafsinputtext
                          class="kingaku"
                          :disabled="右側金額"
                          maxlength="12"
                          :required="true"
                          :width="'120px'"
                          :class="{
                            pinkcolor_money_zippi: isErrPinkSagyotsuki_money_zippi
                          }"
                          v-model="TS00402_tourokuJoken.実費請求金額税抜"
                          @change="change_count(2)"
                          ref="ref実費金額"
                          tabindex="16"
                        ></reafsinputtext>
                      </el-form-item>
                    </el-form>
                  </div>
                </td>

                <td colspan="2">
                  <div class="row_y">
                    <el-form>
                      <el-form-item
                        label="請求備考"
                        label-width="135px"
                        class="multi_text"
                      >
                        <div class="hissutext">
                          <reafsinputtext
                            index="25"
                            type="textarea"
                            show-word-limit
                            :width="'300px'"
                            maxlength="100"
                            :required="true"
                            :class="{
                              pinkcolor_money_zippi: isErrPinkSagyotsuki_money_zippi
                            }"
                            v-model="TS00402_tourokuJoken.請求備考"
                            :disabled="右側金額"
                            @change="change_count(1)"
                            tabindex="17"
                          ></reafsinputtext>
                        </div>
                      </el-form-item>
                    </el-form>
                    <p class="tyuuizikou row" style="width: 400px;">
                      ※実費精算をする場合、資料添付必須。
                    </p>
                  </div>
                </td>
              </tr>
              <tr class="table_kingaku">
                <td>
                  <div class="row_y">
                    <el-form @submit.native.prevent>
                      <el-form-item label="申請金額税抜" label-width="135px">
                        <reafsinputtext
                          class="is-readonly kingaku"
                          v-model="TS00402_tourokuJoken.申請金額税抜"
                          index="22"
                          :disabled="disabledable_text_nomal_true"
                          maxlength="12"
                          :width="'120px'"
                          :required="true"
                          :class="{
                            pinkcolor_money_sinsei: isErrPinkSagyotsuki_money_sinsei
                          }"
                          @change="change_count(2)"
                        ></reafsinputtext>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div class="row_y">
                    <el-form @submit.native.prevent>
                      <el-form-item label="申請金額消費税" label-width="135px">
                        <reafsinputtext
                          class="kingaku"
                          v-model="TS00402_tourokuJoken.申請金額消費税"
                          index="22"
                          :disabled="右側金額"
                          maxlength="12"
                          :width="'120px'"
                          :required="true"
                          :class="{
                            pinkcolor_money_sinsei: isErrPinkSagyotsuki_money_sinsei
                          }"
                          @change="change_count(3)"
                          tabindex="18"
                          @keydown.native="down2first"
                        ></reafsinputtext>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div class="row_y">
                    <el-form @submit.native.prevent>
                      <el-form-item label="税区分" label-width="135px">
                        <el-select
                          placeholder="Select"
                          v-model="TS00402_tourokuJoken.税区分"
                          class="is-readonly opption"
                          :disabled="disabledable_text_nomal_true"
                        >
                          <el-option
                            v-for="item in 税区分名称"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                          >
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div class="row_y">
                    <el-form @submit.native.prevent>
                      <el-form-item label="税率" label-width="135px">
                        <reafsinputtext
                          :disabled="disabledable_text_nomal_true"
                          maxlength="2"
                          :required="true"
                          :width="'120px'"
                          v-model="TS00402_tourokuJoken.税率"
                          @change="change_count(1)"
                          class="is-readonly kingaku"
                        ></reafsinputtext>
                      </el-form-item>
                    </el-form>
                  </div>
                </td>

                <td>
                  <div class="row">
                    <el-form @submit.native.prevent>
                      <el-form-item label="提出先" label-width="135px">
                        <reafsinputtext
                          v-model="TS00402_tourokuJoken.提出先"
                          class="is-readonly"
                          maxlength="12"
                          disabled="disabled"
                          :width="'120px'"
                        >
                        </reafsinputtext>
                      </el-form-item>
                    </el-form>
                  </div>
                  <div class="row_y">
                    <el-form @submit.native.prevent>
                      <el-form-item label="申請金額税込" label-width="135px">
                        <reafsinputtext
                          class="is-readonly kingaku"
                          :disabled="disabledable_text_nomal_true"
                          maxlength="12"
                          :width="'120px'"
                          :required="true"
                          :class="{
                            pinkcolor_money_sinsei: isErrPinkSagyotsuki_money_sinsei
                          }"
                          v-model="TS00402_tourokuJoken.申請金額税込"
                          @change="change_count(2)"
                        ></reafsinputtext>
                      </el-form-item>
                    </el-form>
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </tr>
      </div>
    </div>

    <!-- :loading="loading['btnUploadFile' + index]" -->
  </el-dialog>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import nengetsuhirange from "@/components/basic/NengetsuhiRange.vue";
import reafstable from "@/components/table/Grid.vue";

export default {
  beforeRouteEnter(to, from, next) {
    next(vm => {
      console.log("OK");
      this.click_ServerAccessChk(); // 初期化処理
      next();
    });
  },

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
    // TS00401にて明細情報検索に使用
    TS00402_tourokuJoken: {
      type: Object,
      default: null
    },
    //作業完了未確認：有効　不備有：無効　左記以外：無効
    左側: {
      type: Boolean,
      default: false // 初期値falseだがTD00400がtrueに上書く
    },
    //作業完了未確認：有効（WEB合意の場合）　不備有：無効　左記以外：無効
    右側金額: {
      type: Boolean,
      default: false // 初期値falseだがTD00400がtrueに上書く
    },
    //作業完了未確認：有効　不備有：有効　左記以外：無効
    削除ボタン: {
      type: Boolean,
      default: false // 初期値falseだがTD00400がtrueに上書く
    },
    登録ボタン: {
      type: Boolean,
      default: false // 初期値falseだがTD00400がtrueに上書く
    }
  },
  data() {
    return {
      ///////////////////////////////////////////////
      // 閉じる処理
      ///////////////////////////////////////////////
      change_time: 0,
      ///////////////////////////////////////////////
      // エラーフラグ DATEPICKERをピンクにする為に使用
      ///////////////////////////////////////////////
      isErrPinkSagyotsuki: false,
      isErrPinkSagyotsuki_text: false,
      isErrPinkSagyotsuki_money_sinsei: false,
      isErrPinkSagyotsuki_money_zippi: false,
      ///////////////////////////////////////////////
      // テキスト、ボタン制御
      ///////////////////////////////////////////////
      disabledable_text: false,
      disabledable_text_nomal_false: false,
      disabledable_button: false,
      disabledable_text_nomal_true: true,
      ///////////////////////////////////////////////
      // PDF取得・制御
      ///////////////////////////////////////////////
      MSC_5MB_LengthByte: 5245329,
      MSC_300MB_LengthByte: 314572800, // 300MBの判定を追加
      種類名称: [
        {
          value: "0",
          label: "完了報告書"
        },
        {
          value: "1",
          label: "法定点検書類等"
        },
        {
          value: "2",
          label: "実費精算資料"
        }
      ],
      tblColumns_file: [
        {
          title: "帳票種類",
          key: "1",
          field: "tblLabel",
          type: "string",
          width: 40,
          align: "center",
          maxlength: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },
        {
          title: "ファイル名",
          key: "2",
          type: "string",
          width: 100,
          align: "left",
          link: true,
          maxlength: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          renderBodyCell: ({ row, rowIndex }) => {
            const tblPDFName = row["tblPDFName"];
            return (
              <div title={tblPDFName} style="display: block">
                <a
                  class="ellipsis-text"
                  style="color: blue"
                  on-click={() => {
                    this.onDownload(row);
                  }}
                >
                  {tblPDFName}
                </a>
              </div>
            );
          }
        },
        {
          title: "削除",
          key: "3",
          width: 30,
          align: "center",
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onDelete(row, rowIndex);
                  }}
                  size="mini"
                  style="background: #d3d3d3; border-color: #b7b5b5"
                  disabled={this.削除ボタン == true}
                >
                  削除
                </el-button>
              </div>
            );
          }
        }
      ],

      tblColumns_kingaku: [
        {
          title: "請求",
          width: 10,
          key: "1",
          ellipsis: {
            showTitle: true
          },
          field: "click_seikyu"
        },
        {
          title: "作業月/回目",
          key: "2",
          field: "sagyoutuki_kaime",
          type: "string",
          width: 20,
          align: "left",
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },
        {
          title: "予定月",
          key: "2",
          field: "keiyakunengetu",
          type: "string",
          width: 20,
          align: "center",
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },
        {
          title: "請求金額（税抜き）",
          key: "2",
          type: "string",
          width: 20,
          align: "left",
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          renderBodyCell: ({ row, rowIndex }) => {
            var self = this;
            let 仮置き = 0;
            return (
              <div>
                <reafsinputtext
                  width="100%"
                  maxlength="12"
                  value={row.seikyukingaku.toLocaleString()}
                  on-input={val => {
                    val = val.toLocaleString();
                    仮置き = parseInt(val.replace(/,/g, ""));
                    if (val == "") {
                      row["seikyukingaku"] = "0";
                    } else {
                      row["seikyukingaku"] = 仮置き.toLocaleString();
                    }
                    this.fnc_change_kingaku(row);
                  }}
                  disabled={this.右側金額 == true}
                ></reafsinputtext>
              </div>
            );
          }
        }
      ],
      loading: {
        btnRegister: false,
        loadingBack: false,
        btnUploadFile: false,
        btnDelete: false,
        refDl: false
      },
      zumenForm: {
        帳票種類: "",
        その他帳票名: "",
        ファイル名: "",
        添付ファイル_パス: "ここにファイルをドロップ",
        ファイルNo: "",
        ファイルパス: "",
        ファイルパス2: "",
        ルートパス: "",
        添付元ファイル名: "",
        物理ファイル名: "",
        物理ファイル名2: "",
        状況コメント: "",
        部位コメント: "",
        linkImage: "",
        linkDownload: "",
        添付NO: "",
        imgUploadName: ""
      },
      fileList: [],

      ///////////////////////////////////////////////
      // 月末申請欄
      ///////////////////////////////////////////////
      税区分名称: [
        {
          value: "0",
          label: "課税"
        },
        {
          value: "1",
          label: "非課税"
        }
      ],
      zeikubun: "",
      dragArea1: false,
      排他フラグ: 1
    };
  },
  created() {},
  ///////////////////////////////////////////////
  // グリッド表示
  ///////////////////////////////////////////////
  // mounted: function() {
  //   this.$nextTick(function() {
  //     //画面レンダー後行う
  //     document
  //       .getElementsByClassName("el-table__cell gutter")[1]
  //       .setAttribute("rowSpan", 8);
  //   });
  // },
  mounted() {
    console.log("TS00402");

    this.TS00402_tourokuJoken.新規添付種類 = "0";

    this.$nextTick(function() {
      this.$refs.ref現地確認.focus();
    });
  },
  updated() {
    document.querySelector(".v-modal").style.zIndex = "3000";
    document.querySelector(".SagyoHoukokuNyuryoku").style.zIndex = "3001";
  },

  methods: {
    // window: (onload = function() {
    //   alert("click_ServerAccessChk");
    // }),
    click_ServerAccessChk() {
      const dropArea1 = this.$refs.onloadCon;
      var lastenter = null;
      dropArea1.addEventListener("dragenter", e => {
        lastenter = e.target; // 记录最后进入的元素
        this.dragArea1 = true;
      });
      dropArea1.addEventListener("dragleave", e => {
        if (lastenter === e.target) {
          this.dragArea1 = false;
          e.stopPropagation();
          e.preventDefault();
        }
      });
      dropArea1.addEventListener("drop", e => {
        this.dragArea1 = false;
      });
    },

    dataDefault() {
      return {
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
        imgUploadName: ""
      };
    },
    pageBack(obj) {
      this.pagination.page = obj;
    },

    ///////////////////////////////////////////////
    // ×ボタンまたは閉じるボタンクリック時処理
    ///////////////////////////////////////////////
    async change_count(int) {
      let respons_int;
      let 作業合計 = Number(
        this.TS00402_tourokuJoken.作業合計請求税抜.replace(/,/g, "")
      );
      let 実費 = Number(
        this.TS00402_tourokuJoken.実費請求金額税抜.replace(/,/g, "")
      );
      let 税率 = Number(this.TS00402_tourokuJoken.税率);
      let 消費税 = Number(
        this.TS00402_tourokuJoken.申請金額消費税.replace(/,/g, "")
      );
      let 税込 = Number(
        this.TS00402_tourokuJoken.申請金額税込.replace(/,/g, "")
      );
      let 税抜き = Number(
        this.TS00402_tourokuJoken.申請金額税抜.replace(/,/g, "")
      );

      if (int == 0) {
        respons_int = this.date_hosoku();
        if (respons_int == 1) {
          this.gyakuten_tyekku();
        } else {
        }
        this.change_time += 1;
      } else if ((await int) == 2) {
        //金額変更時再計算
        //申請金額税抜
        let 税抜き = 0;
        税抜き = 作業合計 + 実費;
        //申請金額消費税・申請金額税込
        if (税率 >= 3 && 税抜き != "0") {
          消費税 = Math.floor((税抜き * 税率) / 100);
          税込 = 消費税 + 税抜き;
        } else if (税率 >= 3 && 税抜き == 0) {
          税込 = "0";
          消費税 = "0";
          税抜き = "0";
        } else if (税率 == 0) {
          消費税 = 0;
          税込 = 消費税 + 税抜き;
        }
        this.TS00402_tourokuJoken.申請金額税抜 = 税抜き.toLocaleString();
        this.TS00402_tourokuJoken.申請金額消費税 = 消費税.toLocaleString();
        this.TS00402_tourokuJoken.申請金額税込 = 税込.toLocaleString();
        this.TS00402_tourokuJoken.実費請求金額税抜 = 実費.toLocaleString();
        this.change_time += 1;
      } else if ((await int) == 3) {
        //金額変更時再計算（消費税の時）
        if (税率 >= 3 && 税抜き != "0") {
          税込 = 消費税 + 税抜き;
        } else if (税率 >= 3 && 税抜き == 0) {
          税込 = "0";
          消費税 = "0";
          税抜き = "0";
        } else if (税率 == 0) {
          消費税 = 0;
          税込 = 消費税 + 税抜き;
        }
        this.TS00402_tourokuJoken.申請金額税抜 = 税抜き.toLocaleString();
        this.TS00402_tourokuJoken.申請金額消費税 = 消費税.toLocaleString();
        this.TS00402_tourokuJoken.申請金額税込 = 税込.toLocaleString();
        this.TS00402_tourokuJoken.実費請求金額税抜 = 実費.toLocaleString();
        this.change_time += 1;
      } else {
        this.change_time += 1;
        console.log(111);
      }
    },
    onClose_click() {
      if (this.change_time === 0) {
        this.fnc_void_clearData(1);
        this.change_time = 0;
        this.fnc_void_closeWindow();

        if (this.TS00402_tourokuJoken.check_gamen == 1) {
          this.TS00402_tourokuJoken.check_gamen = 0;
          this.$router.push({ name: "TD00500" });
        } else {
        }
      } else {
        if (
          this.TS00402_tourokuJoken.作業実施日_from == "" ||
          (this.TS00402_tourokuJoken.作業実施日_from == null) |
            (this.TS00402_tourokuJoken.作業実施日_to == "") ||
          this.TS00402_tourokuJoken.作業実施日_to == null
        ) {
        } else {
          if (this.TS00402_tourokuJoken.作業実施日_from.constructor == Date) {
            this.TS00402_tourokuJoken.作業実施日_from = this.fnc_obj_NengetsuConvert(
              this.TS00402_tourokuJoken.作業実施日_from
            );
          }
          if (this.TS00402_tourokuJoken.作業実施日_to.constructor == Date) {
            this.TS00402_tourokuJoken.作業実施日_to = this.fnc_obj_NengetsuConvert(
              this.TS00402_tourokuJoken.作業実施日_to
            );
          }
        }
        if (
          this.TS00402_tourokuJoken.check完了報告 ==
            this.TS00402_tourokuJoken.完了報告 &&
          this.TS00402_tourokuJoken.check作業担当者 ==
            this.TS00402_tourokuJoken.作業担当者 &&
          this.TS00402_tourokuJoken.check担当者連絡先 ==
            this.TS00402_tourokuJoken.担当者連絡先 &&
          this.TS00402_tourokuJoken.check作業実施日_from ==
            this.TS00402_tourokuJoken.作業実施日_from &&
          this.TS00402_tourokuJoken.check作業実施日_to ==
            this.TS00402_tourokuJoken.作業実施日_to &&
          this.TS00402_tourokuJoken.check例月請求額税抜 ==
            this.TS00402_tourokuJoken.例月請求額税抜 &&
          this.TS00402_tourokuJoken.check実費請求金額税抜 ==
            this.TS00402_tourokuJoken.実費請求金額税抜 &&
          this.TS00402_tourokuJoken.check請求備考 ==
            this.TS00402_tourokuJoken.請求備考 &&
          this.TS00402_tourokuJoken.check現地確認あり ==
            this.TS00402_tourokuJoken.現地確認あり &&
          this.TS00402_tourokuJoken.check作業合計請求税抜 ==
            this.TS00402_tourokuJoken.作業合計請求税抜
        ) {
          this.fnc_void_clearData(1);
          this.fnc_void_closeWindow();

          if (this.TS00402_tourokuJoken.check_gamen == 1) {
            this.TS00402_tourokuJoken.check_gamen = 0;
            this.$router.push({ name: "TD00500" });
          } else {
          }

          return true;
        } else {
          this.fnc_void_showErrorMsg_close("K000001"); // 確認メッセージ出力
        }
      }
    },
    onblur() {
      if (
        (this.TS00402_tourokuJoken.作業実施日_from != "" ||
          this.TS00402_tourokuJoken.作業実施日_from != null) &&
        (this.TS00402_tourokuJoken.作業実施日_to == "" ||
          this.TS00402_tourokuJoken.作業実施日_to == null)
      ) {
        this.TS00402_tourokuJoken.作業実施日_from = this.TS00402_tourokuJoken.作業実施日_to;
      } else if (
        (this.TS00402_tourokuJoken.作業実施日_from == "" ||
          this.TS00402_tourokuJoken.作業実施日_from == null) &&
        (this.TS00402_tourokuJoken.作業実施日_to != "" ||
          this.TS00402_tourokuJoken.作業実施日_to != null)
      ) {
        this.TS00402_tourokuJoken.作業実施日_to = this.TS00402_tourokuJoken.作業実施日_from;
      }
    },
    ///////////////////////////////////////////////
    // 登録前処理
    ///////////////////////////////////////////////
    async fnc_void_clickBtnTouroku(valTourokuJoken) {
      //左側が活性化されている場合はチェック
      if (this.左側 == false) {
        //日付チェック・コンバート
        if (
          valTourokuJoken.作業実施日_from == "" ||
          valTourokuJoken.作業実施日_from == null ||
          valTourokuJoken.作業実施日_to == "" ||
          valTourokuJoken.作業実施日_to == null
        ) {
          this.fnc_void_showErrorMsg("E040705"); // 請求書月分を入力して下さい。
          this.fnc_void_pinkData(0); //背景色ピンク
          exit;
        } else {
        }
        if (valTourokuJoken.作業実施日_from.constructor == Date) {
          valTourokuJoken.作業実施日_from = this.fnc_obj_NengetsuConvert(
            valTourokuJoken.作業実施日_from
          );
        } else {
          valTourokuJoken.作業実施日_from = valTourokuJoken.作業実施日_from;
        }

        if (valTourokuJoken.作業実施日_to.constructor == Date) {
          valTourokuJoken.作業実施日_to = this.fnc_obj_NengetsuConvert(
            valTourokuJoken.作業実施日_to
          );
        } else {
          valTourokuJoken.作業実施日_to = valTourokuJoken.作業実施日_to;
        }
      } else {
      }

      //PG入力
      let userLogIn = this.$store.getters.getUserInfo();
      valTourokuJoken.user_id = userLogIn.userId;

      //検索前エラーチェック
      let letJdg;
      letJdg = await this.fnc_obj_TourokumaeErrorChk(valTourokuJoken);
      if (letJdg === false) {
        exit;
      }

      // チェックボックス入れ直し
      valTourokuJoken.現地確認ありbefore = this.fnc_obj_CheckBoxConvert(
        valTourokuJoken.現地確認あり
      );

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
      letval_check = this.TS00402_tourokuJoken;
      let kekka = new Object();
      return new Promise(resolve => {
        _this.http
          .post(
            "/api/Reafs_T/Sagyo/TS00401402_GET_haitacheck",
            letval_check,
            "確認しています...."
          )
          .then(function(response) {
            kekka = response;
            if (kekka.status) {
              if (
                kekka.data[0].new_check_version == letval_check.check_version
              ) {
                resolve(true);
              } else {
                resolve(false);
              }
            } else {
              _this.MsgErr({
                title: "完了報告入力",
                message: kekka.message
              });
            }
          });
      });
    },

    //エラーチェック
    async fnc_obj_TourokumaeErrorChk(valTourokuJoken) {
      var kaisi = valTourokuJoken.作業実施日_from;
      var syuuryou = valTourokuJoken.作業実施日_to;
      var tantoutel_bit = this.fnc_obj_getByteLength(
        valTourokuJoken.担当者連絡先
      );
      var zippi_count = 0;
      var kariire = [];

      kariire = this.TS00402_tourokuJoken.TS00402_grid;

      for (let nakami in kariire) {
        if (kariire[nakami].tblLabel == "実費精算資料") {
          zippi_count += 1;
        } else {
        }
      }
      //左側が活性化されている場合はチェック
      if (this.左側 == false) {
        if (
          // 日付逆転チェック
          kaisi > syuuryou
        ) {
          this.fnc_void_showErrorMsg("E040485"); // 実施日時に逆転があります。
          this.fnc_void_pinkData(0); //背景色ピンク
          this.$nextTick(() => this.$refs.ref予定日.focus());
          return false;
        }
        if (
          //作業担当が空の場合
          valTourokuJoken.作業担当者 === "" ||
          valTourokuJoken.作業担当者 == null
        ) {
          this.fnc_void_showErrorMsg("E040704"); // 請求書月分を入力して下さい。
          this.fnc_void_pinkData(1); //背景色ピンク
          return false;
        } else if (
          //作業実施日（開始）」が「作業予定日（開始）」の年月と異なる場合
          valTourokuJoken.予定日開始.substr(0, 7) !== kaisi.substr(0, 7)
        ) {
          this.fnc_void_showErrorMsg("E040768"); // 請求書月分を入力して下さい。
          this.fnc_void_pinkData(0); //背景色ピンク
          return false;
        } else if (tantoutel_bit > 20) {
          //作業担当者連絡先のバイト数を超えた場合
          this.fnc_void_showErrorMsg("E040764"); // 請求書月分を入力して下さい。
          this.fnc_void_pinkData(1); //背景色ピンク
          this.$nextTick(() => this.$refs.tantousya_tel.focus());
          return false;
        }
      } else {
        if (
          //実費請求金額が空の場合
          Number(valTourokuJoken.実費請求金額税抜) >= 1 &&
          zippi_count == 0
        ) {
          this.fnc_void_showErrorMsg("E040707"); // 請求書月分を入力して下さい。
          this.fnc_void_pinkData(3); //背景色ピンク
          return false;
        } else if (
          //金額グリッドの額を変えている時の確認
          valTourokuJoken.作業合計請求税抜 !=
          valTourokuJoken.check作業合計請求税抜
        ) {
          let msg = (await this.fnc_rtn_getMsg("K040077")).data;
          if (!(await this.confirm(msg, "完了報告書"))) {
            return false;
          } else {
            return true;
          }
        }
      }
    },

    //エラー箇所ピンク化
    fnc_void_pinkData(int) {
      let _this = this;
      if (int == 0) {
        _this.isErrPinkSagyotsuki = true;
      } else if (int == 1) {
        _this.isErrPinkSagyotsuki_text = true;
      } else if (int == 2) {
        _this.isErrPinkSagyotsuki_money_sinsei = true;
      } else if (int == 3) {
        _this.isErrPinkSagyotsuki_money_zippi = true;
      }
    },

    // 登録前ピンク色等データクリア
    fnc_void_clearData() {
      let _this = this;
      _this.isErrPinkSagyotsuki = false;
      _this.isErrPinkSagyotsuki_text = false;
      _this.isErrPinkSagyotsuki_money_sinsei = false;
      _this.isErrPinkSagyotsuki_money_zippi = false;
      _this.change_time = 0;
      _this.TS00402_tourokuJoken.契約管理番号 = "";
      _this.TS00402_tourokuJoken.物件名 = "";
      _this.TS00402_tourokuJoken.営業店 = "";
      _this.TS00402_tourokuJoken.契約名 = "";
      _this.TS00402_tourokuJoken.大区分 = "";
      _this.TS00402_tourokuJoken.中区分 = "";
      _this.TS00402_tourokuJoken.小区分 = "";
      _this.TS00402_tourokuJoken.作業名称 = "";
      _this.TS00402_tourokuJoken.作業月 = "";
      _this.TS00402_tourokuJoken.現地確認あり = 0;
      _this.TS00402_tourokuJoken.作業実施日_from = "";
      _this.TS00402_tourokuJoken.作業実施日_to = "";
      _this.TS00402_tourokuJoken.完了報告 = "";
      _this.TS00402_tourokuJoken.差戻事由 = "";
      _this.TS00402_tourokuJoken.作業担当者 = "";
      _this.TS00402_tourokuJoken.担当者連絡先 = "";
      _this.zumenForm.添付ファイル_パス = "ここにファイルをドロップ";
      _this.TS00402_tourokuJoken.例月請求額税抜 = "";
      _this.TS00402_tourokuJoken.作業合計請求税抜 = "";
      _this.TS00402_tourokuJoken.実費請求金額税抜 = "";
      _this.TS00402_tourokuJoken.請求備考 = "";
      _this.TS00402_tourokuJoken.申請金額税抜 = "";
      _this.TS00402_tourokuJoken.申請金額消費税 = "";
      _this.TS00402_tourokuJoken.税区分 = "";
      _this.TS00402_tourokuJoken.税率 = "";
      _this.TS00402_tourokuJoken.提出先 = "";
      _this.TS00402_tourokuJoken.kaisya_CD = "";
      _this.TS00402_tourokuJoken.回目 = "";
      _this.TS00402_tourokuJoken.進捗コード = "";
      _this.TS00402_tourokuJoken.工事依頼NO = "";
      _this.TS00402_tourokuJoken.実施予定年月 = "";
      _this.TS00402_tourokuJoken.完了報告書不備 = "";
      _this.TS00402_tourokuJoken.完了報告書不備解消日 = "";
      _this.TS00402_tourokuJoken.請求書不備 = "";
      _this.TS00402_tourokuJoken.請求書不備解消日 = "";
      _this.TS00402_tourokuJoken.取引先コード = "";
      _this.TS00402_tourokuJoken.取引先コード枝番 = "";
      _this.TS00402_tourokuJoken.WEB合意 = "";
      _this.TS00402_tourokuJoken.WEB合意開始月 = "";
      _this.TS00402_tourokuJoken.新規添付種類 = "0";
      _this.TS00402_tourokuJoken.TS00402_grid = [];
      _this.TS00402_tourokuJoken.TS00402_grid_kingaku = [];
    },

    //コンバート
    // 日付コンバート
    // DATE型を文字型のYYYY/MM/ddにコンバートする
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

    // チェックボックスコンバート
    // Bool型を0, 1にコンバートする
    fnc_obj_CheckBoxConvert(ChkBoxConvertBefore) {
      let letChkConvertAfter = new Object();
      if (ChkBoxConvertBefore == true) {
        letChkConvertAfter = "1";
      } else {
        letChkConvertAfter = "0";
      }
      console.log(letChkConvertAfter);
      return letChkConvertAfter;
    },

    // チェックボックスコンバート
    // 0, 1をBool型にコンバートする
    fnc_obj_CheckBoxConvertB(ChkBoxConvertBefore) {
      let letChkConvertAfter = new Object();
      if (ChkBoxConvertBefore == 1) {
        this.TS00402_tourokuJoken.genti_kakunin_before = true;
      } else {
        this.TS00402_tourokuJoken.genti_kakunin_before = false;
      }
      console.log(letChkConvertAfter);
      return letChkConvertAfter;
    },
    // 金額コンバート
    fnc_obj_KingakuConvert() {
      if (this.TS00402_tourokuJoken.申請金額税抜 == "") {
        this.TS00402_tourokuJoken.申請金額税抜 = 0;
      }
      if (this.TS00402_tourokuJoken.申請金額消費税 == "") {
        this.TS00402_tourokuJoken.申請金額消費税 = 0;
      }
      if (this.TS00402_tourokuJoken.申請金額税込 == "") {
        this.TS00402_tourokuJoken.申請金額税込 = 0;
      }
      if (this.TS00402_tourokuJoken.実費請求金額税抜 == "") {
        this.TS00402_tourokuJoken.実費請求金額税抜 = 0;
      }
      this.TS00402_tourokuJoken.touroku_sinseisagyo_zeinuki = ""; //申請作業金額
      this.TS00402_tourokuJoken.touroku_zippi_seikyuugaku = ""; //実費請求額
      this.TS00402_tourokuJoken.touroku_reigetu_seikyu = ""; //例月請求額
      this.TS00402_tourokuJoken.touroku_sinseisagyo_syouhizei = ""; //申請消費税
      this.TS00402_tourokuJoken.touroku_sinseisagyo_zeikomi = ""; //申請税込

      this.TS00402_tourokuJoken.touroku_sinseisagyo_zeinuki = this.TS00402_tourokuJoken.申請金額税抜.replace(
        /,/g,
        ""
      );
      this.TS00402_tourokuJoken.touroku_zippi_seikyuugaku = this.TS00402_tourokuJoken.実費請求金額税抜.replace(
        /,/g,
        ""
      ); //実費請求額
      this.TS00402_tourokuJoken.touroku_sinseisagyo_syouhizei = this.TS00402_tourokuJoken.申請金額消費税.replace(
        /,/g,
        ""
      ); //申請消費税
      this.TS00402_tourokuJoken.touroku_sinseisagyo_zeikomi = this.TS00402_tourokuJoken.申請金額税込.replace(
        /,/g,
        ""
      ); //申請税込
    },

    //実績開始日～実績終了日　日時の逆転チェック
    date_hosoku() {
      let kaisi_date = this.TS00402_tourokuJoken.作業実施日_from;
      let syuuryou_date = this.TS00402_tourokuJoken.作業実施日_to;
      if (kaisi_date != "" && (syuuryou_date == "" || syuuryou_date == null)) {
        this.TS00402_tourokuJoken.作業実施日_to = kaisi_date;
        return 1;
      } else if (
        (kaisi_date == "" || kaisi_date == null) &&
        syuuryou_date != ""
      ) {
        this.TS00402_tourokuJoken.作業実施日_from = syuuryou_date;
        return 1;
      }
      return 0;
    },
    ///////////////////////////////////////////////
    // バイトチェック
    ///////////////////////////////////////////////
    fnc_obj_getByteLength(str) {
      str = str == null ? "" : str;
      return encodeURI(str).replace(/%../g, "*").length;
    },
    ///////////////////////////////////////////////
    //日付逆転チェック
    ///////////////////////////////////////////////
    gyakuten_tyekku() {
      let kakunin_kaisi_date = this.TS00402_tourokuJoken.作業実施日_from;
      let kakunin_syuuryou_date = this.TS00402_tourokuJoken.作業実施日_to;

      if (kakunin_kaisi_date != "" && kakunin_syuuryou_date != "") {
        if (kakunin_kaisi_date < kakunin_syuuryou_date) {
          this.fnc_void_showErrorMsg("E040485"); // 日付逆転
        }
      }
    },

    ///////////////////////////////////////////////
    // 登録処理
    ///////////////////////////////////////////////
    async fnc_void_Touroku() {
      let _this = this;
      let num_sinsei_hattyuu_kingaku = new Object();

      // ピンク色等データ初期状態にクリア
      // this.fnc_void_clearData();

      //登録データ渡し
      let letval = new Object();

      //金額チェック
      if (_this.TS00402_tourokuJoken.税区分 == "0") {
        _this.TS00402_tourokuJoken.税区分登録 = "1";
      } else if (_this.TS00402_tourokuJoken.税区分 == "1") {
        _this.TS00402_tourokuJoken.税区分登録 = "2";
      }
      _this.fnc_obj_KingakuConvert();

      letval = _this.TS00402_tourokuJoken;
      await this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00402_PUT_TourokuKekka",
          letval,
          "登録しています...."
        )
        .then(function(response) {
          if (response.status == true) {
            if (
              _this.TS00402_tourokuJoken.進捗コード >= "530" &&
              _this.TS00402_tourokuJoken.進捗コード < "540" && 
              _this.TS00402_tourokuJoken.作業回数 > 0
            ) {
              _this.print(letval);
              //帳票できたら送信
              _this.fnc_void_sendMail(1);
            } else {
            }
            _this.fnc_void_clearData();
            _this.fnc_void_showInfoMsg("M000008");
            _this.fnc_void_closeWindow();
          } else {
            _this.MsgErr({
              title: "完了報告入力",
              message: response.message
            });
          }
        });
    },

    async upLoadImage(imgBase64) {
      const data = this.getDataF093(imgBase64);
      let res = {};
      await this.http
        .post(
          "/api/Reafs_T/sagyo/fnc_InsertF093",
          data,
          "アクセスしています...."
        )
        .then(response => {
          res = response;
        });
      if (res.status) {
        this.zumenForm.添付NO = res.data;
      } else {
        this.MsgErr({
          title: "完了報告入力",
          message: res.message
        });
      }
    },
    getDataF093(imgByte) {
      const zumenForm = this.zumenForm;
      const F093 = {
        添付NO: zumenForm.添付NO || 0,
        枝番: 1,
        削除区分: 0,
        添付元ファイル名: zumenForm.ファイル名,
        ファイルデータ: imgByte
      };
      return {
        F093_一時添付ファイル: F093,
        FLAG添付NO: zumenForm.添付NO
      };
    },

    async print() {
      let letval = new Object();
      letval = {
        kaisya_CD: this.TS00402_tourokuJoken.kaisya_CD,
        keiyaku_no: this.TS00402_tourokuJoken.keiyaku_no,
        rireki_no: this.TS00402_tourokuJoken.rireki_no,
        meisai_no: this.TS00402_tourokuJoken.meisai_no,
        keiyaku_nengetu: this.TS00402_tourokuJoken.keiyaku_nengetu,
        nengetu_meisai: this.TS00402_tourokuJoken.nengetu_meisai,
        zissiyoteinengetu: this.TS00402_tourokuJoken.実施予定年月,
        zissibi_syuuryou: this.TS00402_tourokuJoken.作業実施日_to
      };

      await this.http
        .post(
          "/api/Reafs_T/SagyouKanryouHoukokuShoService/ExportReport",
          letval,
          "アクセスしています....",
          {
            responseType: "blob"
          }
        )
        .then(async blob => {
          if (blob.size === 0) {
            this.fnc_void_showErrorMsg("E010224");
            return;
          }

          // await this.base.saveFileDownload(blob, "作業報告書.pdf");
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: "作業報告書.pdf"
          // });

          // const fileStream = await fileHandle.createWritable();
          // await fileStream.write(blob);
          // await fileStream.close();
          // this.fnc_void_showInfoMsg("M000008");
        })
        .catch(ex => console.log(ex))
        .finally(() => {});
    },

    ///////////////////////////////////////////////
    // PDF取得
    ///////////////////////////////////////////////
    openFileDialog() {
      const me = this;
      var input = document.createElement("input");
      input.type = "file";
      const zumenForm = this.zumenForm;
      // ファイルアップロードサイズの制限を300MBに変更
      // let サイズ = this.MSC_5MB_LengthByte;
      let サイズ = this.MSC_300MB_LengthByte;

      input.onchange = e => {
        var file = e.target.files[0];
        if (!file) {
          return;
        } else {
        }

        if (file.size >= サイズ) {
          e.preventDefault();
          this.fnc_void_showErrorMsg("E040470");
          return;
        }
        // FileReader support
        if (FileReader && file) {
          var fr = new FileReader();
          fr.onload = function() {
            zumenForm.添付ファイル_パス = file.name; // fr.result
            zumenForm.ファイル名 = file.name;
            me.upLoadImage(fr.result.split(",")[1]);
          };
          fr.readAsDataURL(file);
          input.remove();
        } else {
          // Not supported
          // fallback -- perhaps submit the input to an iframe and temporarily store
          // them on the server until the user's session ends.
          input.remove();
        }
      };
      input.click();
    },

    dragChange(event) {
      const me = this;
      // const pageData = this.pageData;
      me.fileList = [];
      const zumenForm = this.zumenForm;
      if (!event) {
        return;
      }
      if (event.size > me.MSC_300MB_LengthByte) {
        e.preventDefault();
        this.fnc_void_showErrorMsg("E040470");
        return;
      }
      // // FileReader support
      // if (FileReader && event) {
      //   var fr = new FileReader();
      //   fr.onload = function() {
      //     zumenForm.添付ファイル_パス = file.name; // fr.result
      //     zumenForm.ファイル名 = file.name;
      //     me.upLoadImage(fr.result.split(",")[1]);
      //   };
      //   fr.readAsDataURL(file);
      //   input.remove();
      // } else {
      //   // Not supported
      //   // fallback -- perhaps submit the input to an iframe and temporarily store
      //   // them on the server until the user's session ends.
      //   input.remove();
      // }

      // FileReader support
      if (FileReader && event) {
        var fr = new FileReader();
        fr.onload = function() {
          zumenForm.添付ファイル_パス = event.name;
          zumenForm.ファイル名 = event.name;
          me.upLoadImage(fr.result.split(",")[1]);
        };
        fr.readAsDataURL(event.raw);
        input.remove();
        zumenForm.添付ファイル_パス;
      } else {
        input.remove();
      }

      // if (FileReader && file) {
      //   var fr = new FileReader();
      //   fr.onload = function() {
      //     zumenForm.添付ファイル_パス = file.name; // fr.result
      //     zumenForm.ファイル名 = file.name;
      //     me.upLoadImage(file.uid);
      //   };
      //   fr.readAsDataURL(file);
      // }
    },

    getDataF090() {
      const data = [];
      const zumenForm = this.zumenForm;
      const TS00402_tourokuJoken = this.TS00402_tourokuJoken;
      let userLogIn = this.$store.getters.getUserInfo();

      var 業者コード = TS00402_tourokuJoken.取引先コード;
      var 業者コード枝番 = TS00402_tourokuJoken.取引先コード枝番;

      const f090 = {
        会社コード: userLogIn.会社コード,
        工事依頼No: "",
        工事依頼No枝番: "",
        添付元: "",
        添付種類: zumenForm.帳票種類,
        その他帳票名: zumenForm.その他帳票名,
        ファイルパス: "",
        物理ファイル名: "",
        添付元ファイル名: zumenForm.ファイル名,
        契約NO: "",
        履歴NO: 0,
        明細NO: 0,
        契約年月: "",
        契約年月明細NO: 0,
        予定年月: "",
        契約書管理NO: "",
        請求書管理NO: "",
        枝番: "",
        取引先コード: "",
        取引先コード枝番: "",
        検収回数: 0,
        請求書NO: "",
        日付: "",
        備考1: "",
        備考2: "",
        Ｗ参照区分: 0,
        Ｔ参照区分: 0,
        発注稟議添付: 0,
        稟議申請ファイル名: "",
        削除区分: 0,
        INSERT_PG: "TS00402",
        INSERT_HOST: "",
        INSERT_ID: "",
        物件コード: ""
      };
      data.push({
        F090_ドキュメント管理ファイル: f090,
        添付NO: zumenForm.添付NO,
        帳票種類: zumenForm.帳票種類,
        業者コード: 業者コード,
        業者コード枝番: 業者コード枝番,
        契約No: TS00402_tourokuJoken.keiyaku_no,
        契約履歴No: TS00402_tourokuJoken.rireki_no,
        契約年月: TS00402_tourokuJoken.keiyaku_nengetu,
        見積明細No: TS00402_tourokuJoken.meisai_no,
        予定年月: TS00402_tourokuJoken.keiyaku_nengetu,
        契約年月明細No: TS00402_tourokuJoken.nengetu_meisai
      });
      return data;
    },
    async onRegister() {
      if (this.TS00402_tourokuJoken.新規添付種類 == "0") {
        this.zumenForm.帳票種類 = "221";
      } else if (this.TS00402_tourokuJoken.新規添付種類 == "1") {
        this.zumenForm.帳票種類 = "222";
      } else if ((this.TS00402_tourokuJoken.新規添付種類 = "2")) {
        this.zumenForm.帳票種類 = "223";
      } else {
      }

      if (!this.regisValidate()) {
        return false;
      } else {
      }

      let msg = (await this.fnc_rtn_getMsg("K000004")).data;
      if (!(await this.confirm(msg, "完了報告書"))) {
        return false;
      }
      const data = this.getDataF090();
      let res = {};
      await this.http
        .post(
          "/api/Reafs_T/Common/fnc_InsertF090",
          data,
          "アクセスしています...."
        )
        .then(response => {
          res = response;
          if (res.code == "308") {
            res = response;
          }
        })
        .catch(ex => {
          // this.showError(ex)
        })
        .finally(() => {});

      if (res.status) {
        this.fnc_void_showInfoMsg("M000008");
        this.getDataTable();
        // 今添付したものが法定点検書類等ならメールを飛ばす
        if (this.zumenForm.帳票種類 === "222") {
          // メール処理
          this.fnc_void_sendMail(2);
        }
        this.zumenForm.添付NO = "";
        this.zumenForm.帳票種類 = "";
        this.zumenForm.その他帳票名 = "";
        this.zumenForm.添付ファイル_パス = "ここにファイルをドロップ";
        this.zumenForm.ファイル名 = "";
        return true;
      } else {
        this.showError(res.message);
        return false;
      }
    },
    regisValidate() {
      if (this.zumenForm.帳票種類 === "") {
        this.fnc_void_showErrorMsg("E040320");
        // this.$refs.ref帳票種類.focus();
        return false;
      }

      if (
        this.zumenForm.添付ファイル_パス === "" ||
        this.zumenForm.添付ファイル_パス === "ここにファイルをドロップ"
      ) {
        this.fnc_void_showErrorMsg("E040469");
        // this.$refs.ref参照.$el.focus();
        return false;
      }

      return true;
    },
    // メール送信関数
    fnc_void_sendMail(mode) {
      let resultMail = "";
      let patternNo = 0;
      let jimusyoCode = "";
      let eigyosyoCode = "";
      let buCode = "";
      let kaCode = "";
      let kakariCode = "";

      if (mode === 1) {
        // 通常登録時メール送信
        patternNo = 20;
        jimusyoCode = this.TS00402_tourokuJoken.sendMailJimusyoCode;
        eigyosyoCode = this.TS00402_tourokuJoken.sendMailEigyosyoCode;
        buCode = this.TS00402_tourokuJoken.sendMailBuCode;
        kaCode = this.TS00402_tourokuJoken.sendMailKaCode;
        kakariCode = this.TS00402_tourokuJoken.sendMailKakariCode;
      } else if (mode === 2) {
        // 添付ファイル登録時メール送信
        patternNo = 24;
        jimusyoCode = this.TS00402_tourokuJoken.attachfile_sendMailJimusyoCode;
        eigyosyoCode = this.TS00402_tourokuJoken
          .attachfile_sendMailEigyosyoCode;
        buCode = this.TS00402_tourokuJoken.attachfile_sendMailBuCode;
        kaCode = this.TS00402_tourokuJoken.attachfile_sendMailKaCode;
        kakariCode = this.TS00402_tourokuJoken.attachfile_sendMailKakariCode;
      }
      let param = {
        msg_type: 0,
        pg_id: "TS00402",
        st_kbn: 2,
        mail_pattern_no: patternNo,
        工事依頼NO: this.TS00402_tourokuJoken.工事依頼NO,
        契約NO: this.TS00402_tourokuJoken.keiyaku_no,
        履歴NO: this.TS00402_tourokuJoken.rireki_no,
        明細NO: this.TS00402_tourokuJoken.meisai_no,
        契約年月: this.TS00402_tourokuJoken.keiyaku_nengetu,
        宛先事務所コード: [jimusyoCode],
        宛先営業所コード: [eigyosyoCode],
        宛先部コード: [buCode],
        宛先課コード: [kaCode],
        宛先係コード: [kakariCode]
      };

      resultMail = this.commonFunctionUI.CM00110SendMail(param);

      if (resultMail.result_id !== 0) {
        console.log("メールエラー！ " + resultMail.err_msg);
        // メール送信時のエラー
      }
    },
    onJumpTo(obj) {
      this.$router.push({
        name: "TS00402"
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
    ///////////////////////////////////////////////
    //ダウンロード処理
    ///////////////////////////////////////////////

    async onDownload(row) {
      console.log(row);
      const fileName = row.物理ファイル名;

      const FileDownloadInfo = {
        FilePath: row.ダウンロードパス,
        FileName: fileName
      };
      await this.http
        .post("/api/Reafs_T/Common/DownloadFile", FileDownloadInfo, "", {
          responseType: "blob"
        })
        .then(async blob => {
          // if (blob.size === 0) {
          //   this.fnc_void_showErrorMsg("E040464");
          //   return;
          // }
          await this.base.saveFileDownload(blob, fileName);
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: fileName
          // });
          // const fileStream = await fileHandle.createWritable();
          // await fileStream.write(blob);
          // await fileStream.close();
          // this.fnc_void_showInfoMsg("M040020");
          // this.$nextTick(() => {
          //   window.open(URL.createObjectURL(blob), '_blank');
          // })
        })
        .catch(ex => console.log(ex))
        .finally(() => {})
        .catch(ex => console.log(ex));
    },

    fnc_clickLink(row, column, rowIndex) {
      let hikisuu = new Object();
      hikisuu = row;
      switch (column.field) {
        //契約名称 -> 定期見積入力へ遷移
        case "tblPDFName":
          this.onDownload(hikisuu);
          break;
        default:
      }
    },

    ///////////////////////////////////////////////
    // PDF削除
    ///////////////////////////////////////////////
    onDelete(row, index) {
      this.fnc_void_showsakuzyoMsg("K000002", row);
    },

    async fnc_void_Sakuzyo(row) {
      let tenpu_path;
      let data = new Object();
      let userLogIn = this.$store.getters.getUserInfo();
      let obj = new Object();

      data = {
        documentNO: row.documentNO,
        host: "", //ホスト
        user_id: userLogIn.userId,
        pg: "TS00402" //PG
      };
      await this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00402_PUT_PDFsakuzyo",
          data,
          "アクセスしています...."
        )
        .then(response => {
          if (response.status) {
            this.fnc_void_showInfoMsg("M000013");
          } else {
            this.MsgErr({
              title: "完了報告入力",
              message: response.message
            });
          }
        });

      this.getDataTable();
    },

    ///////////////////////////////////////////////
    // グリッド再検索
    ///////////////////////////////////////////////
    async getDataTable() {
      let obj = new Object();
      let row = this.TS00402_tourokuJoken;

      this.TS00402_tourokuJoken.TS00402_grid = [];

      await this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00402_GET_KensakuKekka_g",
          row,
          "表示しています...."
        )
        .then(function(response_grid) {
          if (response_grid.data.length === 0) {
            obj = "";
          } else {
            obj = response_grid.data;
          }
        });

      this.TS00402_tourokuJoken.TS00402_grid = obj;
      this.change_count(4);
    },
    ///////////////////////////////////////////////
    //金額グリッド内ボタン押下時金額変更
    ///////////////////////////////////////////////
    async fnc_change_kingaku(row) {
      let グリッド内 = new Object();
      グリッド内 = this.TS00402_tourokuJoken.TS00402_grid_kingaku;
      let 合計 = 0;
      for (let 順番 in グリッド内) {
        if (グリッド内[順番].click_seikyu == "〇") {
          let 詳細 = グリッド内[順番].seikyukingaku.toLocaleString();
          if (詳細 != "0") {
            合計 += Number(詳細.replace(/,/g, ""));
          } else {
            合計 += 0;
          }
        } else {
        }
      }
      this.TS00402_tourokuJoken.作業合計請求税抜 = "";
      this.TS00402_tourokuJoken.作業合計請求税抜 = 合計.toLocaleString();

      await this.change_count(2);
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
          title: "完了報告入力",
          message: response.data
        });
      });
      return 1;
    },
    // エラーメッセージ
    async fnc_void_showErrorMsg(MsgCode) {
      await this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "完了報告入力",
          message: response.data
        });
      });
    },

    //登録ボタン処理
    async fnc_void_showTourokuMsg(MsgCode) {
      if (
        this.左側 == false &&
        this.TS00402_tourokuJoken.作業実施日_to.substr(0, 7) !==
          this.TS00402_tourokuJoken.実施予定年月.substr(0, 7)
      ) {
        MsgCode = "K040076";
        this.TS00402_tourokuJoken.月ズレフラグ = 1;
      } else {
        this.TS00402_tourokuJoken.月ズレフラグ = 0;
      }

      await this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgConf(
          {
            title: "完了報告入力",
            message: response.data
          },
          () => {
            // OKが選択された場合
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
    //削除ボタン処理
    fnc_void_showsakuzyoMsg(MsgCode, row) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgConf(
          {
            title: "完了報告入力",
            message: response.data
          },
          () => {
            // OKが選択された場合
            this.fnc_void_Sakuzyo(row);
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
      console.log("閉じるボタン");
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgConf(
          {
            title: "完了報告入力",
            message: response.data
          },
          () => {
            // OKが選択された場合
            this.fnc_void_clearData(1);

            this.fnc_void_closeWindow();

            if (this.TS00402_tourokuJoken.check_gamen == 1) {
              this.TS00402_tourokuJoken.check_gamen = 0;
              this.$router.push({ name: "TD00500" });
            } else {
            }
          },
          () => {
            // Cancelが選択された場合
            // this.$alert('Cancelが選択された場合')
            return false;
          }
        );
      });
    },
    down2first(event) {
      if (event.keyCode === 9) {
        this.$nextTick(function() {
          this.$refs.ref現地確認.focus();
          event.preventDefault();
        });
      }
    },

    fnc_void_closeWindow() {
      console.log("close_TS00402");

      // dialogVisibleにfalseをセットすることでダイアログを閉じる
      this.$emit("update:dialogVisible", false);
      this.$emit("closePopupEvent", this);
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
.left_flow {
  float: left;
  margin: 10px;
}
.right_flow {
  float: right;
  margin: 10px;
}
.align_top {
  vertical-align: top;
}
.cls-tbl td {
  vertical-align: top;
}
.table_button {
  margin-top: 1%;
  vertical-align: bottom;
  float: left;
  width: 100%;
}
.el-button {
  padding-top: 7px;
  padding-bottom: 7px;
  margin-left: -4px;
  border-start-start-radius: 1px;
  border-end-start-radius: 1px;
}
// .table_kingaku td {
//   margin: 20px 0;
// }
.el-form-item {
  margin: 1px;
  padding: 0;
}
.tyuuizikou {
  color: red;
}
.grid-content {
  height: 160px;
  width: 730px;
}
.grid-content_kingaku {
  height: 140px;
  width: 460px;
  margin: 0 0 5px 3px;
}
.row {
  margin: 1px 5px;
}
.row_y {
  margin: 0 5px;
  width: 250px;
}
.dairi_checkbox {
  width: 1px;
  height: 1px;
}

.opption {
  width: 120px;
}
// ラベル⇔チェックボックス間に余白を作る
.cls-chk-paddnig-left {
  margin-left: 5px;
}
.img-label {
  padding-left: 5px;
  background: #f7f7f7;
  border-color: #fcfcfc;
  display: flex;
  align-items: center;
  overflow: hidden;
}
.readonly_text {
  padding-left: 2px;
  padding: 0.5px 0;
}
</style>

<style scoped>
.kingaku >>> .el-input__inner {
  text-align: right;
}
.el-form .multi >>> .el-form-item__label {
  height: 94px;
}
.el-form .multi_text >>> .el-form-item__label {
  height: 52px;
}
.nengetsu >>> .el-date-editor {
  height: 30px;
  width: 160px;
  margin: 0;
  line-height: 30px;
  border-radius: 1px;
  padding-right: 5px;
}
.hissutext >>> .el-input__inner {
  background: lightgoldenrodyellow;
}

.pinkcolor >>> .el-input__inner {
  background-color: lightpink !important;
}
.transition-box >>> .el-upload-dragger {
  height: 30px;
  vertical-align: top;
  align-items: left;
  width: 467px;
  background-color: #f7f7f7;
  margin: 0;
}
.pinkcolor_text >>> .el-input__inner {
  background-color: lightpink !important;
}
.pinkcolor_money_sinsei >>> .el-input__inner {
  background-color: lightpink !important;
}
.pinkcolor_money_zippi >>> .el-input__inner {
  background-color: lightpink !important;
}
.hissutext >>> .el-textarea__inner {
  background: lightgoldenrodyellow;
}
.bikou_hight >>> .el-textarea__inner {
  height: 94px;
  margin: 1px;
}
cellClassFunction {
  background: #b0c4de;
  color: azure;
}
.cls-tbl {
  border-spacing: 0px 0px;
  min-height: 1px;
  border-collapse: collapse;
  cellspacing: "0px";
  border-spacing: 0px 0px;
  padding: 0;
  margin: 10px 0;
  width: 700px;
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
</style>
