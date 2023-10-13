<template>
  <div class="home-container SeikyuGouiIchiran">
    <!-- 検索部 -->
    <el-row>
      <!-- １行目 -->
      <el-row>
        <el-form ref="KensakuJoken" :model="KensakuJoken" label-width="100px">
          <el-col :span="7">
            <el-form-item label="請求月分" prop="searchForm">
              <el-date-picker
                tabindex="1"
                v-model="KensakuJoken.請求月分"
                style="width: 130px;"
                type="month"
                format="yyyy/MM"
                value-format="yyyy/MM"
                :clearable="false"
                :width="'110px'"
                :class="{ pinkcolor_SeikyuuGatubun: isErrPinkSeikyuuGatubun }"
              ></el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="請求合意" class="button_left">
              <div style="padding-left: 5px;">
                <el-checkbox
                  class="cmd"
                  label="請求未処理"
                  v-model="KensakuJoken.請求合意_請求未処理"
                  ref="ref請求未処理"
                  tabindex="2"
                ></el-checkbox>
                <el-checkbox
                  class="cmd"
                  label="請求処理済"
                  v-model="KensakuJoken.請求合意_請求処理済"
                  ref="ref請求処理済"
                  tabindex="3"
                ></el-checkbox>
                <el-checkbox
                  class="cmd"
                  label="請求処理対象外"
                  v-model="KensakuJoken.請求合意_請求処理対象外"
                  ref="ref請求処理対象外"
                  tabindex="4"
                ></el-checkbox>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="支払承認" class="button_left">
              <div style="padding-left: 5px;">
                <el-checkbox
                  class="cmd"
                  label="支払未承認"
                  v-model="KensakuJoken.支払承認_未承認"
                  ref="ref支払未承認"
                  tabindex="5"
                ></el-checkbox>
                <el-checkbox
                  class="cmd"
                  label="支払承認済"
                  v-model="KensakuJoken.支払承認_承認済"
                  ref="ref支払承認済"
                  tabindex="6"
                ></el-checkbox>
              </div>
            </el-form-item>
          </el-col>

          <!--1行 5列目-->
          <el-col :span="1" class="button_right">
            <el-button
              @click="fnc_void_clickBtnKensaku(KensakuJoken)"
              tabindex="17"
              >検索
            </el-button>
          </el-col>
        </el-form>
      </el-row>

      <!--2行目-->
      <el-row>
        <el-form label-width="100px">
          <el-col :span="7">
            <el-form-item label="物件名">
              <opensearch
                v-model="KensakuJoken.物件コード"
                maxlength="8"
                :disabled="false"
                :appendlabelText="KensakuJoken.物件名"
                :append="'append'"
                :path="'reafsmodalSearchBukken'"
                @backData="reafsmodalBackBukkenmei"
              >
              </opensearch>
            </el-form-item>
          </el-col>

          <el-col :span="5">
            <el-form-item label="番号指定" class="button_left">
              <reafsinputtext
                v-model="KensakuJoken.txtTargetNumber"
                maxlength="20"
                :width="'110px'"
              />
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-form-item label-width="0px">
              <el-radio-group v-model="KensakuJoken.番号指定_指定先">
                <el-radio label="指定なし">指定なし</el-radio>
                <el-radio label="依頼No">依頼№</el-radio>
                <el-radio label="契約No">契約№</el-radio>
                <el-radio label="契約管理番号">契約管理番号</el-radio>
                <el-radio label="口座番号">口座番号</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-form-item label="登録番号" class="is-readonly">
              <reafsinputtext
                v-model="KensakuJoken.txtRecodeNumber"
                maxlength="8"
                disabled
                :width="'110px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <!--3行目-->
      <el-row>
        <el-form
          :model="KensakuJoken"
          label-width="100px"
          @submit.native.prevent
        >
          <el-col :span="7">
            <div style="display: flex">
              <el-form-item label="営業店" prop="eigyoten">
                <opensearch
                  v-model="KensakuJoken.営業店コード"
                  width="100px"
                  :disabled="false"
                  :appendlabelText="eigyouNm"
                  :path="'reafsmodalSearchMise'"
                  maxlength="7"
                  :inputClass="'moblie-input-append'"
                  @backData="reafsmodalBack"
                  @blur="onblur_eigyou()"
                  :class="{ pinkcolor_eigyou: isErrPinkEigyou }"
                  ref="ref営業店"
                  tabindex="22"
                >
                </opensearch>
              </el-form-item>
            </div>
          </el-col>

          <el-col :span="6">
            <el-form-item label="金額指定" class="button_left">
              <div style="display: flex">
                <reafsinputtext
                  v-model="KensakuJoken.金額指定_開始"
                  maxlength="20"
                  :width="'130px'"
                />
                ～
                <reafsinputtext
                  v-model="KensakuJoken.金額指定_終了"
                  maxlength="20"
                  :width="'130px'"
                />
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label-width="0px">
              <el-radio-group v-model="KensakuJoken.金額指定_指定先">
                <el-radio label="指定なし">指定なし</el-radio>
                <el-radio label="金額(税抜)">金額(税抜)</el-radio>
                <el-radio label="金額(消費税)">金額(消費税)</el-radio>
                <el-radio label="金額(税込)">金額(税込)</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-form-item label="請求合意開始月" class="is-readonly">
              <reafsinputtext
                v-model="KensakuJoken.txtRecodeNumber"
                maxlength="8"
                disabled
                :width="'110px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <!--4行目-->
      <el-row>
        <el-form
          :model="KensakuJoken"
          label-width="100px"
          @submit.native.prevent
        >
          <el-col :span="7">
            <el-form-item label="工事区分" prop="工事区分">
              <el-select v-model="KensakuJoken.工事区分" ref="ref工事区分" placeholder="-">
                <el-option label="-" value="-"></el-option>
                <el-option label="直接契約" value="直接契約"></el-option>
                <el-option label="請負" value="請負"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="対象日指定" class="button_left">
              <div style="display: flex">
                <el-date-picker
                  tabindex="1"
                  v-model="KensakuJoken.対象日開始"
                  style="width: 130px;"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  :clearable="false"
                  :class="{ pinkcolor_SeikyuuGatubun: isErrPinkSeikyuuGatubun }"
                ></el-date-picker>
                ～
                <el-date-picker
                  tabindex="1"
                  v-model="KensakuJoken.対象日終了"
                  style="width: 130px;"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  :clearable="false"
                  :class="{ pinkcolor_SeikyuuGatubun: isErrPinkSeikyuuGatubun }"
                ></el-date-picker>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label-width="0px">
              <el-radio-group v-model="KensakuJoken.対象日指定_指定先">
                <el-radio label="指定なし">指定なし</el-radio>
                <el-radio label="完了日">完了日</el-radio>
                <el-radio label="検収日">検収日</el-radio>
                <el-radio label="支払予定日">支払予定日</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <!--5行目-->
      <el-row>
        <el-form
          :model="KensakuJoken"
          label-width="100px"
          @submit.native.prevent
        >
          <el-col :span="7">
            <el-form-item label="提出先" prop="提出先">
              <el-select v-model="KensakuJoken.提出先" placeholder="-">
                <el-option label="-" value="-"></el-option>
                <el-option label="ＳＧリアルティ" value="ＳＧリアルティ"></el-option>
                <el-option label="佐川急便" value="佐川急便"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="件名指定" class="button_left">
              <reafsinputtext
                v-model="KensakuJoken.件名指定"
                maxlength="20"
                :width="'250px'"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>

      <!--6行目-->
      <el-row>
        <el-form
          :model="KensakuJoken"
          label-width="120px"
          @submit.native.prevent
        >
          <el-col :span="6">
            <el-form-item label="表示計（税抜）" class="is-readonly">
              <reafsinputtext
                v-model="表示計_税抜"
                maxlength="8"
                disabled
                :width="'200px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="表示計（消費税）" class="is-readonly">
              <reafsinputtext
                v-model="表示計_消費税"
                maxlength="8"
                disabled
                :width="'200px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="表示計（税込）" class="is-readonly">
              <reafsinputtext
                v-model="表示計_税込"
                maxlength="8"
                disabled
                :width="'200px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </el-row>
    <!-- 明細部 -->
    <!-- 検索ヒット件数 -->
    <el-body style="width: 100%">
      <div class="cls-hitkensu_align_right">{{ hitKensu }}</div>

      <!-- 明細テーブル -->
      <div>
        <el-row :gutter="20">
          <div class="grid-content bg-purple-light">
            <reafstable
              ref="table"
              :tableData="tblData"
              :columns="tblColumns"
              :maxHeight="551"
              :resizeAble="true"
              :cellStyleOption="cellStyleOption"
              rowKeyFieldName="key"
              :class="tableClassFilter"
              :disableSelectedRowKeys="disableSelectedRowKeys"
              :sortOption="sortOption"
              :HiddenColumnKeys="hiddenColumnMishoninShonin"
            >
            </reafstable>
          </div>
        </el-row>
        <div>
          ※当システムの請求書を使用する場合は、
          請求書を紙で印刷し押印の上、提出をして下さい。
        </div>
      </div>
    </el-body>
    <el-footer class="home-el-footer" height="auto" style="width: 100%">
      <el-row style="margin-bottom: 20px">
        <el-form label-width="100px">
          <el-col :span="1" class="button_left">
            <el-button :disabled="seikyu" @click="fnc_void_clickBtnTouroku()"
              >請求処理</el-button
            >
          </el-col>

          <el-col :span="1" class="button_right">
            <el-button @click="onClickBack()">戻る</el-button>
          </el-col>
        </el-form>
      </el-row>
    </el-footer>
  </div>
</template>
<script>
import opensearch from "@/components/basic/openSearch.vue";
import reafstable from "@/components/table/Grid.vue";
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnaturalnumber from "@/components/basic/ReafsControl/ReafsInputNaturalNumber.vue";
import reafsmodal from "@/components/modal/Modal.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";

export default {
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.initialize(); // 初期化処理
      next();
    });
  },

  ///////////////////////////////////////////////
  // コンポーネントタグ
  ///////////////////////////////////////////////
  components: {
    opensearch,
    reafstable,
    reafsinputtext,
    reafsinputnaturalnumber,
    reafsmodal,
    nengetsu
  },
  data() {
    ///////////////////////////////////////////////
    // Template から参照できる変数
    ///////////////////////////////////////////////
    return {
      ///////////////////////////////////////////////
      // タブ指定
      ///////////////////////////////////////////////
      TAB_IDX_SAGYOTSUKI: "1",

      処理区分名称: [
        {
          value: "0",
          label: "請求未処理"
        },
        {
          value: "1",
          label: "請求処理済"
        },
        {
          value: "2",
          label: "支払承認済"
        }
      ],
      名称コード: "",
      表示計_税抜 : 0,
      表示計_消費税 : 0,
      表示計_税込 : 0,

      ///////////////////////////////////////////////
      // 検索部
      /////////////////////////////////////////////
      KensakuJoken: {
        請求月分: "",
        請求合意_請求未処理: "",
        請求合意_請求処理済: "",
        請求合意_請求処理対象外: "",
        支払承認_未承認: "",
        支払承認_承認済: "",
        物件コード: "",
        番号指定: "",
        番号指定_指定先: "",
        営業店コード: "",
        営業店コード枝番: "",
        金額指定_開始: "",
        金額指定_終了: "",
        金額指定_指定先: "",
        工事区分: "",
        対象日指定_開始: "",
        対象日指定_終了: "",
        対象日指定_指定先: "",
        提出先: "",
        件名指定: "",
        名称コード: "",
        ログインID: "",
        ユーザー名: ""
      },
      tourokudata: {
        touroku_select: [],
        UPDATE_PG: "TD00500",
        UPDATE_HOST: "",
        UPDATE_ID: ""
      },
      tuutisyo: {
        tblKeiyakuNo: "",
        履歴NO: 0,
        明細NO: 0,
        実施予定年月: "",
        添付種類: "",
        業者コード: "",
        検収回数: "",
        tblIraiNo: "",
        工事依頼NO枝番: "",
        フラグ: 0
      },
      seikyuusyo: {
        tblKeiyakuNo: "",
        履歴NO: 0,
        明細NO: 0,
        実施予定年月: "",
        添付種類: "",
        業者コード: "",
        検収回数: "",
        tblIraiNo: "",
        工事依頼NO枝番: "",
        フラグ: 0
      },

      ///////////////////////////////////////////////
      // 明細部
      ///////////////////////////////////////////////
      hitKensu: "0  件",
      cellStyleOption: {
        headerCellClass: ({ row, column, rowIndex }) => {},
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = "";

          const textLeft = [
            "tblIraiNo",
            "tblKeiyakuNo",
            "tblKeiyakukanri",
            "tblBukkenName",
            "tblKouziName",
            "tblEigyouten",
            "tblKouziKubun"
          ];
          if (textLeft.includes(column.key)) {
            css += " text-left";
          }

          return css;
        }
      },
      tblData: [],
      tblColumns: [
        {
          field: "No",
          key: "No",
          title: "№",
          width: 40,
          fixed: "left",
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex" class="cell-body-custome">
                <span style="height: 100%;width: 100%;line-height: 26px;">
                  {rowIndex + 1}
                </span>
              </div>
            );
          }
        },

        {
          field: "taisyou",
          key: "taisyou",
          title: "",
          type: "checkbox",
          align: "center",
          vAlign: "center",
          width: 45,
          fixed: "left"
        },

        {
          key: "tblIraiNo",
          title: "依頼№",
          field: "tblIraiNo",
          width: 120,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          fixed: "left"
        },

        {
          key: "tblKeiyakuNo",
          title: "契約№",
          field: "tblKeiyakuNo",
          width: 120,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblKeiyakukanri",
          title: "契約管理番号",
          field: "tblKeiyakukanri",
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblBukkenName",
          title: "物件名",
          field: "tblBukkenName",
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblEigyouten",
          title: "営業店",
          field: "tblEigyouten",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblKouziKubun",
          title: "工事区分",
          field: "tblKouziKubun",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblKouziName",
          title: "工事件名/作業名",
          field: "tblKouziName",
          width: 300,
          ellipsis: {
            showTitle: true
          },
          renderBodyCell: ({ row }) => {
            return (
              <div
                style="display: grid; padding-left: 5px"
                class="cell-body-custome"
              >
                <a
                  style="margin-top: 4px"
                  title={row["tblKouziName"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKouziName"]) this.lnkKouziNameOnClick(row);
                  }}
                  href="javascript:;"
                >
                  {row["tblKouziName"]}
                </a>
              </div>
            );
          },
          sortBy: ""
        },

        {
          key: "tblTeisyutusaki",
          title: "提出先",
          field: "tblTeisyutusaki",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblKingaku_zeinuki",
          title: "金額(税抜)",
          field: "tblKingaku_zeinuki",
          width: 150,
          ellipsis: {
            showTitle: true
          },
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex;" class="cell-body-custome">
                <label style="text-align: right; width: 100%;">
                  {row.tblKingaku_zeinuki.toLocaleString()}
                </label>
              </div>
            );
          },
          sortBy: ""
        },

        {
          key: "tblKingaku_zeigaku",
          title: "金額(消費税)",
          field: "tblKingaku_zeigaku",
          width: 150,
          ellipsis: {
            showTitle: true
          },
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex;" class="cell-body-custome">
                <label style="text-align: right; width: 100%;">
                  {row.tblKingaku_zeigaku.toLocaleString()}
                </label>
              </div>
            );
          },
          sortBy: ""
        },

        {
          key: "tblKingaku",
          title: "金額(税込)",
          field: "tblKingaku",
          width: 150,
          ellipsis: {
            showTitle: true
          },
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex;" class="cell-body-custome">
                <label style="text-align: right; width: 100%;">
                  {row.tblKingaku.toLocaleString()}
                </label>
              </div>
            );
          },
          sortBy: ""
        },

        {
          key: "tblKozabango",
          title: "口座番号",
          field: "tblKozabango",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          renderBodyCell: ({ row }) => {
            return (
              <div
                style="display: grid; padding-left: 5px"
                class="cell-body-custome"
              >
                <a
                  style="margin-top: 4px"
                  title={row["tblKozabango"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKozabango"]) this.lnkKouziNameOnClick(row);
                  }}
                  href="javascript:;"
                >
                  {row["tblKozabango"]}
                </a>
              </div>
            );
          },
          sortBy: ""
        },

        {
          key: "tblSeikyugoui",
          title: "請求合意(Web)",
          field: "tblSeikyugoui",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblShiharai",
          title: "支払承認",
          field: "tblShiharai",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblKanryouDate",
          title: "完了日",
          field: "tblKanryouDate",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          renderBodyCell: ({ row }) => {
            return (
              <div
                style="display: grid; padding-left: 5px"
                class="cell-body-custome"
              >
                <a
                  style="margin-top: 4px"
                  title={row["tblKanryouDate"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKanryouDate"]) this.lnkKanryoDateOnClick(row);
                  }}
                  href="javascript:;"
                >
                  {row["tblKanryouDate"]}
                </a>
              </div>
            );
          },
          sortBy: ""
        },

        {
          key: "tblKensyuuDate",
          title: "検収日",
          field: "tblKensyuuDate",
          width: 110,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "tblSiharaiyoteiDate",
          title: "支払予定日",
          field: "tblSiharaiyoteiDate",
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: ""
        },

        {
          key: "通知書",
          title: "通知書",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onClickBtn_tuutisyo(row);
                  }}
                  disabled={row.添付種類 != "240" && row.添付種類 != "140"}
                >
                  {"通知書"}
                </el-button>
              </div>
            );
          }
        },

        {
          key: "請求書",
          title: "請求書",
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onClickBtn_seikyusyo(row);
                  }}
                  disabled={row.kensyuukubun != "1" && row.tblKensyuuDate == ""}
                >
                  {"請求書"}
                </el-button>
              </div>
            );
          }
        }
      ],
      sortOption: {
        sortAlways: true,
        sortChange: (params) => {
          if (!this.tblData || this.tblData.length <= 0) {
            return;
          }
          let sortProp = "";
          let sortDirect = "";
          for (let prop in params) {
            if (params[prop]) {
              sortProp = prop;
              sortDirect = params[prop];
            }
          }

          this.tblData.sort((a, b) => {
            let aClone = JSON.parse(JSON.stringify(a[sortProp]));
            let bClone = JSON.parse(JSON.stringify(b[sortProp]));

            if (sortDirect === "asc") {
              if (!a) {
                return 1;
              }
              if (!b) {
                return 1;
              }
              return aClone > bClone ? 1 : -1;
            } else if (sortDirect === "desc") {
              if (!a) {
                return 1;
              }
              if (!b) {
                return 1;
              }
              return bClone > aClone ? 1 : -1;
            } else {
              return 0;
            }
          });
        }
      },
      ///////////////////////////////////////////////
      // エラーフラグ DATEPICKERをピンクにする為に使用
      ///////////////////////////////////////////////
      isErrPinkSeikyuuGatubun: false,
      seikyu: false
    };
  },
  created() {
    let userLogIn = this.$store.getters.getUserInfo();
    this.KensakuJoken.ユーザー名 = userLogIn.ユーザー名;
    if (userLogIn.親ＩＤ === "") {
      this.KensakuJoken.ログインID = userLogIn.userId;
    } else {
      this.KensakuJoken.ログインID = userLogIn.親ＩＤ;
    }
  },

  mounted() {
    console.log("TD00500 ログインID:" + this.KensakuJoken.ログインID);
    this.KensakuJoken.名称コード = "0";
  },
  watch: {
    $route(to, from) {
      let userLogIn = this.$store.getters.getUserInfo();
      this.KensakuJoken.ユーザー名 = userLogIn.ユーザー名;
      this.KensakuJoken.ログインID = userLogIn.userId;
    }
  },

  methods: {
    initialize() {
      let kidou_no = this.$route.params.StartupMode;
      // 検索処理起動
      if (kidou_no == 1) {
        let kousindata = new Object();
        this.KensakuJoken.名称コード = "0";
        let userLogIn = this.$store.getters.getUserInfo();
        this.KensakuJoken.ユーザー名 = userLogIn.ユーザー名;
        if (userLogIn.親ＩＤ === "") {
          this.KensakuJoken.ログインID = userLogIn.userId;
        } else {
          this.KensakuJoken.ログインID = userLogIn.親ＩＤ;
        }

        kousindata = this.KensakuJoken;

        this.fnc_obj_getKensakuKekka(kousindata);
      } else {
      }
    },
    ///////////////////////////////////////////////
    // 戻るボタン
    ///////////////////////////////////////////////
    onClickBack() {
      //window.location.href = "/Reafs_T/MainMenu";
      // 遷移元画面へ戻る
      this.$router.back();
    },
    ///////////////////////////////////////////////
    // 通知書・請求書ボタン
    ///////////////////////////////////////////////
    //通知書
    async onClickBtn_tuutisyo(row) {
      let _this = this;
      let kensaku = new Object();
      let kekka = new Object();
      _this.tuutisyo = row;

      if (_this.tuutisyo.kubetu == "true") {
        _this.tuutisyo.フラグ = 0;
        _this.tuutisyo.添付種類 = "140";
      } else if (_this.tuutisyo.kubetu == "false") {
        _this.tuutisyo.フラグ = 1;
        _this.tuutisyo.添付種類 = "240";
      }

      kensaku = _this.tuutisyo;
      await _this.http
        .post(
          "/api/Reafs_T/Seikyu/TD00500_GET_tuutisyo",
          kensaku,
          "検索しています...."
        )
        .then(function(response) {
          if (response.status) {
            kekka = response.data;
            if (kekka.length === 0) {
              _this.fnc_void_showErrorMsg("E000014");
            } else {
              _this.onDownload(kekka);
            }
          } else {
            _this.MsgErr({
              title: "請求合意一覧",
              message: response.message
            });
            return;
          }
        });
    },
    //請求書
    async onClickBtn_seikyusyo(row) {
      console.log("start_onClickBtn_seikyusyo");
      const seikyusyo = {
        会社コード: row.会社コード,
        出力区分: "3",
        帳票区分: row.kubetu == "true" ? "1" : "2",
        業者コード: row.業者コード,
        業者コード枝番: row.業者コード枝番,
        工事依頼No: row.tblIraiNo,
        工事依頼No枝番: row.工事依頼NO枝番,
        検収回数: row.検収回数,
        契約No: row.tblKeiyakuNo,
        明細No: row.明細NO,
        履歴No: row.履歴NO,
        契約年月: row.契約年月,
        発注サブNo: row.発注サブNO
      };
      console.log(seikyusyo);
      this.http
        .post("/api/Reafs_T/TP00501/ExportReport", seikyusyo, "", {
          responseType: "blob"
        })
        .then(async blob => {
          console.log(blob);
          // if (blob.size === 0) {
          //   return;
          // }
          await this.base.saveFileDownload(blob, "請求書.pdf");
        });
    },
    ///////////////////////////////////////////////
    //ダウンロード処理
    ///////////////////////////////////////////////

    async onDownload(kekka) {
      const fileName = kekka[0].物理ファイル名;

      const FileDownloadInfo = {
        FilePath: kekka[0].ダウンロードパス,
        FileName: fileName
      };
      await this.http
        .post("/api/Reafs_T/Common/DownloadFile", FileDownloadInfo, "", {
          responseType: "blob"
        })
        .then(async blob => {
          if (blob.size === 0) {
            this.fnc_void_showErrorMsg("E040464");
            return;
          }
          await this.base.saveFileDownload(blob, fileName);
        })
        .catch(ex => console.log(ex))
        .finally(() => {})
        .catch(ex => console.log(ex));
    },

    ///////////////////////////////////////////////
    // 検索ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnKensaku(valKensakuJoken) {
      //検索前エラーチェック
      let letJdg;
      letJdg = this.fnc_obj_kensakumaeErrorChk(valKensakuJoken);
      if (letJdg === false) {
        return false;
      }

      // 前準備。検索条件をバックに渡す用にコンバート
      //valKensakuJoken = this.fnc_obj_KensakuJokenConvert(valKensakuJoken);
      console.log(valKensakuJoken);

      // ピンク色等データ初期状態にクリア
      this.fnc_void_clearData();

      //検索結果取得
      this.fnc_obj_getKensakuKekka(valKensakuJoken);
    },

    ///////////////////////////////////////////////
    // 検索条件コンバート
    // バックに渡すパラメータ用にコンバートする
    ///////////////////////////////////////////////
    fnc_obj_KensakuJokenConvert(KensakuJokenConvert) {
      let let請求月分convert;
      let let名称コードconvert;
      //年月コンバート(KensakuJokenConvert.請求月分 == null)
      if (KensakuJokenConvert.請求月分 == null) {
        KensakuJokenConvert.請求月分 = "";
      } else if (
        KensakuJokenConvert.請求月分 != "" &&
        KensakuJokenConvert.請求月分.constructor == Date
      ) {
        let請求月分convert = this.fnc_obj_NengetsuConvert(
          KensakuJokenConvert.請求月分
        );
        KensakuJokenConvert.請求月分 = let請求月分convert;
      }

      return KensakuJokenConvert;
    },

    ///////////////////////////////////////////////
    // 日付コンバート
    // DATE型を文字型のYYYY/MMにコンバートする
    ///////////////////////////////////////////////
    fnc_obj_NengetsuConvert(NengetsuConvertBefore) {
      let letYear = new Object();
      let letMonth = new Object();
      let letNengetsu = new Object();

      letYear = NengetsuConvertBefore.getFullYear();
      letMonth = NengetsuConvertBefore.getMonth() + 1;
      letMonth = ("00" + letMonth).slice(-2);
      letNengetsu = letYear + "/" + letMonth;

      return letNengetsu;
    },

    ///////////////////////////////////////////////
    // 検索前ピンク色等データクリア
    ///////////////////////////////////////////////
    fnc_void_clearData() {
      let _this = this;
      _this.tblData = [];
      _this.hitKensu = "0  件";
      _this.isErrPinkSeikyuuGatubun = false;
    },

    ///////////////////////////////////////////////
    // 検索結果取得
    ///////////////////////////////////////////////
    async fnc_obj_getKensakuKekka(KensakuJokenConvert, refreshFlg = 0) {
      let _this = this;
      this.KensakuJoken = KensakuJokenConvert;
      let kekka = new Object();

      console.log(KensakuJokenConvert);
      await _this.http
        .post(
          "/api/Reafs_T/Seikyu/TD00500_GET_KensakuKekka",
          KensakuJokenConvert,
          "検索しています...."
        )
        .then(function(response) {
          kekka = response;
        });
      if (!kekka.status) {
        _this.MsgErr({
          title: "請求合意一覧",
          message: kekka.message
        });
      } else {
        console.log("0件出すか出さないか " + refreshFlg);
        if (kekka.data.length === 0 && refreshFlg === 0) {
          await _this.fnc_void_showErrorMsg("E000014");
        }
      }
      _this.tblData = kekka.data;
      _this.tblData.map((x, i) => {
        x.key = i + 1;
        return x;
      });

      this.表示計_税抜 = _this.tblData
        .map((x) => x.tblKingaku_zeinuki)
        .reduce((a, b) => this.sum(a, b), 0).toLocaleString();

      this.表示計_消費税 = _this.tblData
        .map((x) => x.tblKingaku_zeigaku)
        .reduce((a, b) => this.sum(a, b), 0).toLocaleString();

      this.表示計_税込 = _this.tblData
        .map((x) => x.tblKingaku)
        .reduce((a, b) => this.sum(a, b), 0).toLocaleString();

      _this.hitKensu = kekka.data.length.toLocaleString() + "  件";
    },

    sum (a, b) {
      a = this.getNumber(a)
      b = this.getNumber(b)
      return a * 1 + b * 1
    },

    getNumber (string) {
      if (!string) return 0
      return (string + '' || '0').replaceAll(',', '') * 1
    },

    ///////////////////////////////////////////////
    // 検索前エラーチェック
    ///////////////////////////////////////////////
    fnc_obj_kensakumaeErrorChk(KensakuJoken) {
      console.log("検索前エラーチェック");
      //処理区分が「請求処理済」「支払承認済」であるが、請求月分が空白の場合
      if (
        (KensakuJoken.請求月分 === "" || KensakuJoken.請求月分 === null)
      ) {
        this.fnc_void_showErrorMsg("E040500"); // 請求書月分を入力して下さい。
        this.isErrPinkSeikyuuGatubun = true; //背景色ピンク
        return false;
      }

      if (
        (KensakuJoken.請求合意_請求未処理 === "false" || KensakuJoken.請求合意_請求未処理 === "") &&
        (KensakuJoken.請求合意_請求処理済 === "false" || KensakuJoken.請求合意_請求処理済 === "") &&
        (KensakuJoken.請求合意_請求処理対象外 === "false" || KensakuJoken.請求合意_請求処理対象外 === "")
      ) {
        this.MsgErr({
          title: "請求合意一覧",
          message: "請求合意区分を選択してください。"
        });
        return false;
      }

      if (
        (KensakuJoken.支払承認_未承認 === "false" || KensakuJoken.支払承認_未承認 === "") &&
        (KensakuJoken.支払承認_承認済 === "false" || KensakuJoken.支払承認_承認済 === "")
      ) {
        this.MsgErr({
          title: "請求合意一覧",
          message: "支払承認区分を選択してください。"
        });
        return false;
      }

      return true;
    },
    fnc_clickLink(row, column, rowIndex) {
      let hikisuu = new Object();
      this.hikisuu = row.column(1);
    },
    ///////////////////////////////////////////////
    // 登録ボタンクリック時処理
    ///////////////////////////////////////////////
    async fnc_void_clickBtnTouroku() {
      var _this = this;
      var kariire = [];
      _this.tourokudata.touroku_select = [];
      kariire = _this.tblData;

      console.log("start_fnc_void_clickBtnTouroku");
      //PG入力
      _this.tourokudata.UPDATE_ID = _this.KensakuJoken.ログインID;
      const resData = [];
      let dataTbl = JSON.parse(JSON.stringify(_this.tblData)) || [];
      dataTbl = dataTbl.filter(x =>
        this.$refs.table.getCheckboxSelected().includes(x.key)
      );
      
      for (let i = 0; i < dataTbl.length; i++) {
        const row = dataTbl[i];
        if (row.tblSeikyugoui == '未')
        {
          _this.tourokudata.touroku_select.push(dataTbl[i]);
        }
        else
        {
          this.MsgErr({
            title: "請求合意一覧",
            message: "請求未処理以外のデータが選択されています。"
          });
          return false;
        }
      }

      if (_this.tourokudata.touroku_select.length == 0) {
        _this.fnc_void_showErrorMsg("E040245");
        return false;
      }
      
      let haita_check1 = "";
      let haita_check2 = "";
      let haita_check_select = [];
      haita_check_select = _this.tourokudata.touroku_select;
      haita_check2 = _this.fnc_check_haita2(haita_check_select);
      if (await haita_check2) {
        _this.fnc_void_showTourokuMsg("K040031");
      } else {
        return false;
      }
    },

    //排他チェック
    async fnc_check_haita2(haita_check_select) {
      let _this = this;
      let sumcount;
      let data = haita_check_select;
      let flg = 0;
      flg = await this.haita_kensaku1(haita_check_select);

      return new Promise(resolve => {
        if (flg == 0) {
          resolve(true);
        } else if (flg == 2) {
          this.fnc_void_showErrorMsg("E040198"); // 承認ルートがありません。
          resolve(false);
        } else if (flg == 3) {
          this.fnc_void_showErrorMsg("E020332"); //他の端末で更新されました。再度検索をして下さい。
          resolve(false);
        } else {
          resolve(false);
        }
      });
    },

    async haita_kensaku1(haita_check_select) {
      let data = haita_check_select;
      let flg = 0;
      for (let n in data) {
        let kensaku = "";
        kensaku = data[n];
        flg = await this.haita_kensaku2(kensaku);
        if (flg == 2) {
          break;
        } else if (flg == 3) {
          break;
        } else {
        }
      }
      return flg;
    },
    
    async haita_kensaku2(kensaku) {
      let _this = this;
      let flg = 0;
      await _this.http
        .post(
          "/api/Reafs_T/Seikyu/TD00500_GET_haitacheck",
          kensaku,
          "確認しています...."
        )
        .then(function(response) {
          if (!response.status) {
            _this.MsgErr({
              title: "請求合意一覧",
              message: response.message
            });
            flg = 4;
          } else if (response.data.length === 0) {
            console.log("排他チェック失敗");
            flg = 4;
          } else {
            let F00VERSION = response.data[0].F00VERSION;
            let F01VERSION = response.data[0].F01VERSION;
            let 承認ルート = response.data[0].承認ルート;
            if (
              F00VERSION == kensaku.F00VERSION &&
              F01VERSION == kensaku.F01VERSION
            ) {
              if (承認ルート != "") {
              } else {
                flg = 2;
              }
            } else {
              flg = 3;
            }
          }
        });
      return flg;
    },
    ///////////////////////////////////////////////
    // 登録メッセージ
    ///////////////////////////////////////////////
    //登録ボタン処理
    async fnc_void_showTourokuMsg(MsgCode) {
      await this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgConf(
          {
            title: "請求合意一覧",
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

    ///////////////////////////////////////////////
    // 登録処理
    ///////////////////////////////////////////////
    async fnc_void_Touroku() {
      var _this = this;
      var tourokudata = new Object();
      let tourokukekka = new Object();
      let kensaku = _this.KensakuJoken;
      let flg = new Object();

      tourokudata = _this.tourokudata;
      await _this.http
        .post(
          "/api/Reafs_T/Seikyu/TD00500_Put_Touroku",
          tourokudata,
          "更新しています...."
        )
        .then(async function(response) {
          tourokukekka = response;
        });

      if (!tourokukekka.status) {
        _this.MsgErr({
          title: "請求合意一覧",
          message: tourokukekka.message
        });
      }

      if (await _this.fnc_void_showInfoMsg("M000012")) {
        await _this.fnc_obj_getKensakuKekka(kensaku, 1);
      } else {
        // 処理無し
      }
    },
    ///////////////////////////////////////////////
    // コンボボックスセレクト時
    ///////////////////////////////////////////////
    fnc_void_SelectComb() {
      let check = this.KensakuJoken.名称コード;
      if (check == "0") {
        this.seikyu = false;
      } else if (check == "1" || check == "2") {
        this.seikyu = true;
      }
    },
    async lnkKouziNameOnClick(row) {
      //修繕：TD00301・工事：TD00302へ遷移
      console.log(
        "パラメータ：" +
          row.tblKeiyakuNo +
          " / " +
          row.履歴NO +
          " / " +
          row.hansu
      );
      if (row.kubetu == "true") {
        this.$router.push({
          name: "TD00301",
          params: {
            工事依頼No: row.tblIraiNo,
            工事依頼No枝番: row.工事依頼NO枝番,
            業者コード: row.業者コード,
            業者コード枝番: row.業者コード枝番
          }
        });
      } else if (row.kubetu == "false") {
        if (row.hansu == null) {
          row.hansu = "11";
        }
        this.$router.push({
          name: "TD00302",
          params: {
            keiyakuNO: row.tblKeiyakuNo,
            rirekiNO: row.履歴NO,
            hansu: row.hansu
          }
        });
      }
    },
    async lnkKanryoDateOnClick(row) {
      if (row.kubetu == "true") {
        this.$router.push({
          name: "TD00101",
          params: {
            工事依頼No: row.tblIraiNo,
            工事依頼No枝番: row.工事依頼NO枝番,
            業者コード: row.業者コード,
            業者コード枝番: row.業者コード枝番,
            発注サブNo: row.発注サブNO,
            検収回数: row.検収回数

          }
        });
      } else if (row.kubetu == "false") {
        let kensaku = new Object();
        kensaku = await this.fnc_get_TS00402(row);

        this.$router.push({
          name: "TD00400",
          params: {
            keiyaku_no: kensaku[0].契約NO,
            履歴NO: kensaku[0].履歴No,
            明細NO: kensaku[0].明細NO,
            契約年月: kensaku[0].契約年月,
            keiyakumeisai: kensaku[0].契約年月明細NO,
            hikisuu: false
          }
        });
      }
    },
    ///////////////////////////////////////////////
    // リンククリック時の画面遷移またはモーダル表示処理
    ///////////////////////////////////////////////
    async fnc_clickLink(row, column, rowIndex) {
      let hikisuu = new Object();

      switch (column.field) {
        //修繕：TD00301・工事：TD00302へ遷移
        case "tblKouziName":
          this.hikisuu = row;
          if (this.hikisuu.kubetu == "true") {
            this.$router.push({
              name: "TD00301",
              query: {
                工事依頼No: this.hikisuu.tblIraiNo,
                工事依頼No枝番: this.hikisuu.工事依頼NO枝番,
                業者コード: this.hikisuu.業者コード,
                業者コード枝番: this.hikisuu.業者コード枝番
              }
            });
          } else if (this.hikisuu.kubetu == "false") {
            if (this.hikisuu.hansu == null) {
              this.hikisuu.hansu = "11";
            }
            this.$router.push({
              name: "TD00302",
              query: {
                keiyakuNO: this.hikisuu.tblKeiyakuNo,
                rirekiNO: this.hikisuu.履歴NO,
                hansu: this.hikisuu.hansu
              }
            });
          }
          break;

        //修繕：TD00101・定期：TS00402へ遷移
        case "tblKanryouDate":
          this.hikisuu = row;
          if (this.hikisuu.kubetu == "true") {
            this.$router.push({
              name: "TD00101",
              query: {
                工事依頼No: this.hikisuu.tblIraiNo,
                工事依頼No枝番: this.hikisuu.工事依頼NO枝番,
                業者コード: this.hikisuu.業者コード
              }
            });
          } else if (this.hikisuu.kubetu == "false") {
            let kensaku = new Object();
            kensaku = await this.fnc_get_TS00402(row);

            this.$router.push({
              name: "TD00400",
              query: {
                tblKeiyakuNo: kensaku[0].契約NO,
                履歴NO: kensaku[0].履歴No,
                明細NO: kensaku[0].明細NO,
                契約年月: kensaku[0].契約年月,
                keiyakumeisai: kensaku[0].keiyakumeisai,
                hikisuu: false
              }
            });
          }
          break;
        default:
      }
    },

    async fnc_get_TS00402(row) {
      let _this = this;
      let data = new Object();
      await _this.http
        .post(
          "/api/Reafs_T/Seikyu/TD00500_GET_yoteikanryou",
          row,
          "表示しています...."
        )
        .then(function(response) {
          if (!response.status) {
            _this.MsgErr({
              title: "請求合意一覧",
              message: response.message
            });
          } else if (response.data.length === 0) {
            _this.fnc_void_showErrorMsg("E010224");
          } else {
            data = response.data;
          }
        });
      return data;
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
    fnc_void_showErrorMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "請求合意一覧",
          message: response.data
        });
      });
      return true;
    },
    // お知らせメッセージ
    fnc_void_showInfoMsg(MsgCode) {
      return new Promise((resolve, reject) => {
        this.fnc_rtn_getMsg(MsgCode).then(response => {
          this.MsgInfo({
            title: "請求合意一覧",
            message: response.data,
            callback: function(a, b) {
              resolve(true);
            }
          });
        });
      });
    },

    fnc_backcolor(index) {
      if (rowIndex === 6) {
        return "highlight-row";
      }
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
  padding: 10px;
  width: 100%;
  height: 100%;
}

.home-container {
  padding-left: 10px;
  width: 100%;
  height: 100%;
}

// 検索部の各行の高さ幅を狭める
.el-form-item {
  margin-bottom: 1px;
}

// ラベル⇔チェックボックス間に余白を作る
.cls-chk-paddnig-left {
  padding-left: 5px;
}

// 確定チェックボックスを左に詰める
.cls-kakutei-margin-left {
  margin-left: -25px;
}

// ボタンの高さをラベルに合わせる
.el-button {
  padding-top: 7px;
  padding-bottom: 7px;
}

.cls-intput-num-only {
  width: 150px;
}

// 検索ヒット件数右寄せ
.cls-hitkensu_align_right {
  text-align: right;
}

/* 作業月エラーピンク */
.append-background {
  display: inline-flex;
  background-color: rgb(211, 211, 211);
  /*width: 500px;*/
}
.button_left {
  float: left;
}
.button_right {
  float: right;
  margin: 0 20px 0 0;
}
</style>
<style>
.SeikyuGouiIchiran .text-left {
  text-align: left !important;
}
.SeikyuGouiIchiran
  .ve-table
  .ve-table-container
  table.ve-table-content
  thead.ve-table-header
  .ve-table-header-tr
  .ve-table-header-th.ve-table-fixed-left {
  z-index: 10 !important;
}
<style scoped > .pinkcolor_SeikyuuGatubun >>> .el-input__inner {
  background-color: lightpink !important;
}
</style>
