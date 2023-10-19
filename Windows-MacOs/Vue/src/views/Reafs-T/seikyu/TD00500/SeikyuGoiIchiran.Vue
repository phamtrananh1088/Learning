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

          <el-col :span="9">
            <el-form-item label="請求合意" class="button_left">
              <div>
                <el-checkbox
                  class="cmd"
                  label="請求未処理"
                  v-model="KensakuJoken.請求合意_請求未処理"
                  ref="ref請求未処理"
                  tabindex="2"
                  style="width: 110px;margin: 0 5px;"
                ></el-checkbox>
                <el-checkbox
                  class="cmd"
                  label="請求処理済"
                  v-model="KensakuJoken.請求合意_請求処理済"
                  ref="ref請求処理済"
                  tabindex="3"
                  style="width: 110px;margin: 0 5px;"
                ></el-checkbox>
                <el-checkbox
                  class="cmd"
                  label="請求処理対象外"
                  v-model="KensakuJoken.請求合意_請求処理対象外"
                  ref="ref請求処理対象外"
                  style="width: 110px;margin: 0 5px;"
                  tabindex="4"
                ></el-checkbox>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="支払承認" class="button_left">
              <div style="padding-left: 5px;width: 300px;">
                <el-checkbox
                  class="cmd"
                  label="支払未承認"
                  v-model="KensakuJoken.支払承認_未承認"
                  ref="ref支払未承認"
                  tabindex="5"
                  style="width: 110px;margin: 0 5px;"
                ></el-checkbox>
                <el-checkbox
                  class="cmd"
                  label="支払承認済"
                  v-model="KensakuJoken.支払承認_承認済"
                  ref="ref支払承認済"
                  tabindex="6"
                  style="width: 110px;margin: 0 5px;"
                ></el-checkbox>
              </div>
            </el-form-item>
          </el-col>

          <!--1行 5列目-->
          <el-col :span="1" class="button_right">
            <el-button
              @click="fnc_void_clickBtnKensaku(KensakuJoken)"
              tabindex="17"
              style="width: 100.52px"
              >検索
            </el-button>
          </el-col>
        </el-form>
      </el-row>

      <!--2行目-->
      <el-row>
        <el-form label-width="100px">
          <el-col :span="7" style="padding-right: 15px;">
            <el-form-item label="物件名">
              <opensearch
                v-model="KensakuJoken.物件コード"
                maxlength="8"
                :disabled="false"
                :appendlabelText="ラベル名.物件名"
                :append="'append'"
                :path="'reafsmodalSearchBukken'"
                @backData="reafsmodalBackBukkenmei"
                :width="'115px'"
              >
              </opensearch>
            </el-form-item>
          </el-col>

          <el-col :span="3" style="padding-right: 20px;width: 200px;">
            <el-form-item label="番号指定">
              <reafsinputtext
                v-model="KensakuJoken.番号指定"
                maxlength="11"
                :width="'130px'"
              />
            </el-form-item>
          </el-col>

          <el-col :span="9"  style="padding-left: 35px;">
            <el-form-item label-width="0px">
              <el-radio-group v-model="KensakuJoken.番号指定_指定先">
                <el-radio label="指定なし" style="margin: 0 5px;"
                  >指定なし</el-radio
                >
                <el-radio label="依頼No" style="margin: 0 5px;">依頼№</el-radio>
                <el-radio label="契約No" style="margin: 0 5px;">契約№</el-radio>
                <el-radio label="契約管理番号" style="margin: 0 5px;"
                  >契約管理番号</el-radio
                >
                <el-radio label="口座番号" style="margin: 0 5px;"
                  >口座番号</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="2" style="padding-left: 29px;">
            <el-form-item
              label="登録番号"
              class="is-readonly"
              style="width: 200px"
            >
              <reafsinputtext
                v-model="KensakuJoken.txtRecodeNumber"
                maxlength="8"
                disabled
                :width="'170px'"
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
          <el-col :span="6" style="padding-right: 10px;">
            <div>
              <el-form-item label="営業店" prop="eigyoten">
                <opensearch
                  v-model="KensakuJoken.営業店コード表示"
                  width="100px"
                  :disabled="false"
                  :appendlabelText="ラベル名.営業店名"
                  :path="'reafsmodalSearchMise'"
                  maxlength="6"
                  :inputClass="'moblie-input-append'"
                  @backData="reafsmodalBackEigyoten"
                  @blur="onblur_eigyou()"
                  :class="{ pinkcolor_eigyou: isErrPinkEigyou }"
                  ref="ref営業店"
                  tabindex="22"
                >
                </opensearch>
              </el-form-item>
            </div>
          </el-col>

          <el-col :span="4" style="width: 355px;">
            <el-form-item label="金額指定">
              <div style="display: flex;">
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="KensakuJoken.金額指定_開始"
                  :width="'160px'"
                />
                ～
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="KensakuJoken.金額指定_終了"
                  :width="'160px'"
                />
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8" style="padding-left: 75px;">
            <el-form-item label-width="0px">
              <el-radio-group v-model="KensakuJoken.金額指定_指定先">
                <el-radio label="指定なし" style="margin: 0 5px;"
                  >指定なし</el-radio
                >
                <el-radio label="金額(税抜)" style="margin: 0 5px;"
                  >金額(税抜)</el-radio
                >
                <el-radio label="金額(消費税)" style="margin: 0 5px;"
                  >金額(消費税)</el-radio
                >
                <el-radio label="金額(税込)" style="margin: 0 5px;"
                  >金額(税込)</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-form-item label="請求合意開始月" class="is-readonly">
              <reafsinputtext
                v-model="KensakuJoken.txtRecodeNumber"
                maxlength="8"
                disabled
                :width="'100px'"
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
              <el-select
                v-model="KensakuJoken.工事区分"
                ref="ref工事区分"
                placeholder="ー"
              >
                <el-option
                  v-for="item in 工事区分value"
                  :key="item.名称コード"
                  :label="item.名称コード_名称"
                  :value="item.名称コード"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="5" style="padding-right: 30px;width: 320px;">
            <el-form-item label="対象日指定" class="button_left">
              <div style="display: flex">
                <el-date-picker
                  tabindex="1"
                  v-model="KensakuJoken.対象日指定_開始"
                  style="width: 130px;"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  :clearable="false"
                  :class="{
                    pinkcolor_SeikyuuGatubun: isErrPinkSeikyuuGatubun
                  }"
                ></el-date-picker>
                ～
                <el-date-picker
                  tabindex="1"
                  v-model="KensakuJoken.対象日指定_終了"
                  style="width: 130px;"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  :clearable="false"
                  :class="{
                    pinkcolor_SeikyuuGatubun: isErrPinkSeikyuuGatubun
                  }"
                ></el-date-picker>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8" style="padding-left: 60px;">
            <el-form-item label-width="0px">
              <el-radio-group v-model="KensakuJoken.対象日指定_指定先">
                <el-radio label="指定なし" style="margin: 0 5px;"
                  >指定なし</el-radio
                >
                <el-radio label="完了日" style="margin: 0 5px;"
                  >完了日</el-radio
                >
                <el-radio label="検収日" style="margin: 0 5px;"
                  >検収日</el-radio
                >
                <el-radio label="支払予定日" style="margin: 0 5px;"
                  >支払予定日</el-radio
                >
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
              <el-select v-model="KensakuJoken.提出先" placeholder="ー">
                <el-option label="ー" value="ー"></el-option>
                <el-option
                  label="ＳＧリアルティ"
                  value="ＳＧリアルティ"
                ></el-option>
                <el-option label="佐川急便" value="佐川急便"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="件名指定" class="button_left">
              <reafsinputtext
                v-model="KensakuJoken.件名指定"
                maxlength="30"
                :width="'340px'"
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
                class="hyouzikei"
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
                class="hyouzikei"
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
                class="hyouzikei"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </el-row>
    <!-- 明細部 -->
    <!-- 検索ヒット件数 -->
    <div class="home-el-body">
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
                :maxHeight="500"
                :resizeAble="true"
                :cellStyleOption="cellStyleOption"
                rowKeyFieldName="key"
                :class="tableClassFilter"
                :disableSelectedRowKeys="disableSelectedRowKeys"
                :sortOption="sortOption"
                :HiddenColumnKeys="hiddenColumnMishoninShonin"
                :virtualScrollOption="virtualScrollOption"
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
    </div>
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
    <screenPopup
      src="/Reafs_T/TS00501/TeikiSeikyuSubSub"
      :params="TeikiSeikyuSubSubParams"
      :dialogVisible.sync="showTS00501"
      :isCloseConfirm="TeikiSeikyuSubSubParams.cfmFlg"
      messageCodeCloseCfm="K000001"
      @closePopupEvent="fnc_TS00501Close"
    ></screenPopup>

    <!-- 定期作業報告入力モーダル -->
    <modalTS00402
      :dialogVisible.sync="showTS00402"
      :TS00402_tourokuJoken.sync="KeyDataToTS00402"
      :左側.sync="左側"
      :右側金額.sync="右側金額"
      :削除ボタン.sync="削除ボタン"
      :登録ボタン.sync="登録ボタン"
    ></modalTS00402>
  </div>
</template>
<script>
import opensearch from "@/components/basic/openSearch.vue";
import reafstable from "@/components/table/Grid.vue";
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnaturalnumber from "@/components/basic/ReafsControl/ReafsInputNaturalNumber.vue";
import reafsmodal from "@/components/modal/Modal.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import screenPopup from "@/views/Reafs-T/common/ScreenPopup/ScreenPopup.vue";
import reafsinputnumber from "@/components/basic/ReafsControl/ReafsInputNumber.vue";
import modalTS00402 from "@/views/Reafs-T/sagyo/TS00402/TeikiSagyoHokokuNyuryoku.vue";

var $select工事区分value;

export default {
  beforeRouteEnter(to, from, next) {
    next(vm => {
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
    nengetsu,
    screenPopup,
    reafsinputnumber,
    modalTS00402
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
      表示計_税抜: 0,
      表示計_消費税: 0,
      表示計_税込: 0,

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
        番号指定_指定先: "指定なし",
        営業店コード: "",
        営業店コード枝番: "",
        営業店コード表示: "",
        金額指定_開始: "",
        金額指定_終了: "",
        金額指定_指定先: "指定なし",
        工事区分: "",
        対象日指定_開始: "",
        対象日指定_終了: "",
        対象日指定_指定先: "指定なし",
        提出先: "",
        件名指定: "",
        名称コード: "",
        ログインID: "",
        ユーザー名: "",
        画面フラグ: 0 //メニュー遷移：１、画面検索：0
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
      tourokudata: {
        touroku_select: [],
        UPDATE_PG: "TD00500",
        UPDATE_HOST: "",
        UPDATE_ID: ""
      },
      初期値: {
        会社コード: "9945"
      },
      ラベル名: {
        物件名: "",
        営業店名: ""
      },

      工事区分value: [],

      ///////////////////////////////////////////////
      // TD00400 -> TS00402 データ受け渡しインターフェイス
      ///////////////////////////////////////////////
      showTS00402: false,
      左側: false,
      右側金額: false,
      削除ボタン: false,
      KeyDataToTS00402: {
        //表示
        契約管理番号: "", //契約管理番号
        物件名: "", //物件名
        営業店: "", //営業店
        契約名: "", //契約名称
        大区分: "", //作業カテゴリー大
        中区分: "", //作業カテゴリー中
        小区分: "", //作業カテゴリー小
        作業名称: "", //作業名称
        作業月: "", //作業月/回数
        作業実施日_from: "", //作業開始日
        作業実施日_to: "", //作業終了日
        完了報告: "", ///完了報告
        差戻事由: "", //差戻事由
        作業担当者: "", //作業担当者名
        担当者連絡先: "", //担当者連絡先
        請求備考: "", //請求備考
        現地確認あり: false, //現地確認あり
        現地確認ありbefore: false, //現地確認あり_登録前チェック
        tenpu0: "", //添付1
        例月請求額税抜: "", //例月請求額税抜
        作業合計請求税抜: "", //作業合計請求税抜
        実費請求金額税抜: "", //実費請求金額税抜
        申請金額税抜: "", //申請金額税抜
        申請金額消費税: "", //申請金額消費税
        申請金額税込: "",
        touroku_sinseisagyo_zeinuki: "", //申請金額税抜
        touroku_zippi_seikyuugaku: "", //実費請求金額税抜
        touroku_sinseisagyo_syouhizei: "", //申請金額消費税
        touroku_sinseisagyo_zeikomi: "",
        税率: "", //税率
        税区分: "", //税区分
        税区分登録: "", //税区分登録用
        提出先: "",
        webgoui: 0,
        TS00402_grid: [], //TS00402グリッド
        TS00402_grid_kingaku: [],
        check_version: "", //比較用

        //バックデータ
        keiyaku_no: "", //契約NO
        rireki_no: 0, //履歴NO
        meisai_no: 0, //明細NO
        keiyaku_nengetu: "", //契約年月
        nengetu_meisai: "", //年月明細
        工事依頼NO: "", //工事依頼NO
        実施予定年月: "", //作業予定日
        kaisya_CD: "", //会社コード
        host: "", //ホスト
        user_id: "", //ユーザーID
        pg: "TS00402", //PG
        進捗コード: 0, //進捗コード
        完了報告書不備: 0, //完了報告書不備
        完了報告書不備解消日: "", //完了報告書不備解消日
        請求書不備: 0, //請求書不備
        請求書不備解消日: "", //請求書不備解消日
        実施予定年月: "", //実施予定年月
        取引先コード: "", //取引先コード
        取引先コード枝番: "", //取引先コード枝番
        sinsei_hattyuu_kingaku: "", //申請発注金額
        hattyuu_sub: 0, //発注サブ
        kensyu_kaisuu: 0, //検収回数
        新規添付種類: 0,
        添付ファイル名: "",
        フルパス: "",
        sendMailJimusyoCode: "",
        sendMailEigyosyoCode: "",
        sendMailBuCode: "",
        sendMailKaCode: "",
        sendMailKakariCode: "",
        attachfile_sendMailJimusyoCode: "",
        attachfile_sendMailEigyosyoCode: "",
        attachfile_sendMailBuCode: "",
        attachfile_sendMailKaCode: "",
        attachfile_sendMailKakariCode: "",
        予定日開始: "",
        作業回数: "",
        予定売上金額: 0,
        検収売上税率: 0,
        検収売上金額_税抜: 0,
        検収売上金額_税込: 0,
        検収売上金額_消費税: 0,

        //0=月ズレなし,1=月ズレあり
        月ズレフラグ: 0,

        //閉じるチェック用
        check完了報告: "", ///完了報告
        check作業担当者: "", //作業担当者名
        check担当者連絡先: "", //担当者連絡先
        check作業実施日_from: "", //作業実施日_from
        check作業実施日_to: "", //作業実施日_to
        check例月請求額税抜: "", //例月請求額税抜
        check実費請求金額税抜: "", //実費請求金額税抜
        check作業合計請求税抜: "",
        check_gamen: 0,
        check_grid_date: [],
        check請求備考: "", //請求備考
        check現地確認あり: false //現地確認あり
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
      showTS00501: false,
      TeikiSeikyuSubSubParams: {
        cfmFlg: false,
        種類: "",
        会社コード: "",
        契約NO: "",
        履歴NO: 0,
        明細NO: 0,
        契約年月: "",
        工事依頼NO: "",
        工事依頼NO枝番: "",
        発注サブNO: 0,
        検収回数: 0,
        回収支払契約番号: "",
        会計会社コード: ""
      },

      tblData: [],
      tblColumns: [
        {
          field: "No",
          key: "1",
          title: "№",
          fixed: "left",
          width: 40
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
          width: 150,
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
                    if (row["tblKozabango"]) this.lnkKozaBangoOnClick(row);
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
        sortChange: params => {
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
      
      virtualScrollOption: {
        enable: true,
        minRowHeight: 40
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

    $select工事区分value = this;
  },

  mounted() {
    console.log("TD00500 ログインID:" + this.KensakuJoken.ログインID);
    this.KensakuJoken.名称コード = "0";

    let フラグ = this.KensakuJoken.画面フラグ;
    this.fnc_GET_Syokiti(フラグ);
    this.selectKouzikubun();
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
      if (kidou_no == "1") {
        let kousindata = new Object();
        this.KensakuJoken.名称コード = "0";
        // ユーザー名取得
        let userLogIn = this.$store.getters.getUserInfo();
        this.KensakuJoken.ユーザー名 = userLogIn.ユーザー名;
        if (userLogIn.親ＩＤ === "") {
          this.KensakuJoken.ログインID = userLogIn.userId;
        } else {
          this.KensakuJoken.ログインID = userLogIn.親ＩＤ;
        }
        // メニューからの初期値表示
        this.KensakuJoken.画面フラグ = 1;

        kousindata = this.KensakuJoken;
        let フラグ = this.KensakuJoken.画面フラグ;
        this.fnc_GET_Syokiti(フラグ);
        this.fnc_obj_getKensakuKekka(kousindata);
      } else {
      }
    },
    ///////////////////////////////////////////////
    //初期値取得
    //メニューから遷移：1、画面検索：0
    ///////////////////////////////////////////////
    async fnc_GET_Syokiti(e) {
      if (e === 1) {
        // 初期値設定
        this.KensakuJoken.請求月分 = "";
        this.KensakuJoken.請求合意_請求未処理 = true;
        this.KensakuJoken.請求合意_請求処理済 = false;
        this.KensakuJoken.請求合意_請求処理対象外 = false;
        this.KensakuJoken.支払承認_未承認 = true;
        this.KensakuJoken.支払承認_承認済 = true;
        this.KensakuJoken.物件コード = "";
        this.KensakuJoken.名称コード = "";
        this.KensakuJoken.営業店コード = "";
        this.KensakuJoken.営業店コード枝番 = "";
        this.KensakuJoken.営業店コード表示 = "";
        this.KensakuJoken.工事区分 = "";
        this.KensakuJoken.提出先 = "";
        this.KensakuJoken.番号指定 = "";
        this.KensakuJoken.金額指定_開始 = "-999999999999";
        this.KensakuJoken.金額指定_終了 = "999999999999";
        this.KensakuJoken.対象日指定_終了 = "";
        this.KensakuJoken.件名指定 = "";
        this.ラベル名.物件名 = "";
        this.ラベル名.営業店名 = "";
      } else {
        let 引数 = new Object();
        引数 = this.初期値;
        var kekka = "";
        // SGR経理年月取得
        await this.http
          .post(
            "/api/Reafs_T/Seikyu/TD00500_GET_syokiti",
            引数,
            "表示しています...."
          )
          .then(function(response) {
            kekka = response.data[0].経理年月;
          });

        // 初期値設定
        this.KensakuJoken.請求月分 = kekka;
        this.KensakuJoken.請求合意_請求未処理 = true;
        this.KensakuJoken.請求合意_請求処理済 = false;
        this.KensakuJoken.請求合意_請求処理対象外 = false;
        this.KensakuJoken.支払承認_未承認 = true;
        this.KensakuJoken.支払承認_承認済 = false;
        this.KensakuJoken.物件コード = "";
        this.KensakuJoken.名称コード = "";
        this.KensakuJoken.営業店コード = "";
        this.KensakuJoken.営業店コード枝番 = "";
        this.KensakuJoken.営業店コード表示 = "";
        this.KensakuJoken.工事区分 = "";
        this.KensakuJoken.提出先 = "";
        this.KensakuJoken.番号指定 = "";
        this.KensakuJoken.金額指定_開始 = "-999999999999";
        this.KensakuJoken.金額指定_終了 = "999999999999";
        this.KensakuJoken.対象日指定_終了 = "";
        this.KensakuJoken.件名指定 = "";
        this.ラベル名.物件名 = "";
        this.ラベル名.営業店名 = "";
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
        発注サブNo: row.発注サブNO,
        支払予定日: row.kubetu == "true" ? row.tblSiharaiyoteiDate : "" // ハノイ側修正2023/09/26　STEP2_W　課題管理表№51：設計書「修正履歴」シートの「2023/09/26仕様変更分」「処理概要シート　引数に支払予定日を追加。」
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
      this.KensakuJoken.画面フラグ = 0;

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
      this.tblData = []; // 検索前にテーブルのデータを初期化。 これをやらないとチェックボックスの値が残ったままになる。

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
        x.No = i + 1;
      });

      this.表示計_税抜 = _this.tblData
        .map(x => x.tblKingaku_zeinuki)
        .reduce((a, b) => this.sum(a, b), 0)
        .toLocaleString();

      this.表示計_消費税 = _this.tblData
        .map(x => x.tblKingaku_zeigaku)
        .reduce((a, b) => this.sum(a, b), 0)
        .toLocaleString();

      this.表示計_税込 = _this.tblData
        .map(x => x.tblKingaku)
        .reduce((a, b) => this.sum(a, b), 0)
        .toLocaleString();

      _this.hitKensu = kekka.data.length.toLocaleString() + "  件";
    },

    sum(a, b) {
      a = this.getNumber(a);
      b = this.getNumber(b);
      return a * 1 + b * 1;
    },

    getNumber(string) {
      if (!string) return 0;
      return (string + "" || "0").replaceAll(",", "") * 1;
    },

    ///////////////////////////////////////////////
    // 検索前エラーチェック
    ///////////////////////////////////////////////
    fnc_obj_kensakumaeErrorChk(KensakuJoken) {
      console.log("検索前エラーチェック");
      //処理区分が「請求処理済」「支払承認済」であるが、請求月分が空白の場合
      if (KensakuJoken.請求月分 === "" || KensakuJoken.請求月分 === null) {
        this.fnc_void_showErrorMsg("E040500"); // 請求書月分を入力して下さい。
        this.isErrPinkSeikyuuGatubun = true; //背景色ピンク
        return false;
      }

      // 請求合意区分
      if (
        (KensakuJoken.請求合意_請求未処理 === "false" ||
          KensakuJoken.請求合意_請求未処理 === "") &&
        (KensakuJoken.請求合意_請求処理済 === "false" ||
          KensakuJoken.請求合意_請求処理済 === "") &&
        (KensakuJoken.請求合意_請求処理対象外 === "false" ||
          KensakuJoken.請求合意_請求処理対象外 === "")
      ) {
        this.MsgErr({
          title: "請求合意一覧",
          message: "請求合意区分を選択してください。"
        });
        return false;
      }

      // 請求承認区分
      if (
        (KensakuJoken.支払承認_未承認 === "false" ||
          KensakuJoken.支払承認_未承認 === "") &&
        (KensakuJoken.支払承認_承認済 === "false" ||
          KensakuJoken.支払承認_承認済 === "")
      ) {
        this.MsgErr({
          title: "請求合意一覧",
          message: "支払承認区分を選択してください。"
        });
        return false;
      }

      // 金額指定 片方だけ金額未入力ならエラー
      if (
        (KensakuJoken.金額指定_開始 !== "" && KensakuJoken.金額指定_終了 === "" && KensakuJoken.金額指定_指定先 !== "指定なし") || 
        (KensakuJoken.金額指定_開始 === "" && KensakuJoken.金額指定_終了 !== "" && KensakuJoken.金額指定_指定先 !== "指定なし")
      ) {
        this.MsgErr({
          title: "請求合意一覧",
          message: "金額指定を指定する際は開始と終了の両方を設定してください。"
        });
        return false;
      }

      // 対象日指定
      if (
        (KensakuJoken.対象日指定_開始 !== "" && KensakuJoken.対象日指定_終了 === "" && KensakuJoken.対象日指定_指定先 !== "指定なし") || 
        (KensakuJoken.対象日指定_開始 === "" && KensakuJoken.対象日指定_終了 !== "" && KensakuJoken.対象日指定_指定先 !== "指定なし")
      ) {
        this.MsgErr({
          title: "請求合意一覧",
          message: "対象日指定を指定する際は開始と終了の両方を設定してください。"
        });
        return false;
      }

      return true;
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
        let row = dataTbl[i];

        // チェックされた明細に「請求処理済」「請求処理対象外」以外のものをセレクト
        if (row.tblSeikyugoui == "未") {
          //口座番号が登録されているものだけセレクト
          if (row.tblKozabango == "未登録") {
            _this.fnc_void_showErrorMsg("E040784");

            return false;
          } else {
            _this.tourokudata.touroku_select.push(dataTbl[i]);
          }
        } else {
          _this.fnc_void_showErrorMsg("E040785");

          return false;
        }

        // チェックされた明細のペア発注サブNOが1以上かどうか
        // 2023/07/10 ペア発注サブNOが1以上だからと言って必ずしもペアがいるわけではない事に留意
        if (row.ペア発注サブNo >= 1) {
          let pairChkTbl = JSON.parse(JSON.stringify(_this.tblData)) || [];

          // ペア発注サブNOが1以上だった明細の依頼Noを基に、画面に出ている明細全ての中から同一の依頼Noの明細を全て取得する
          // 2023/07/10 ペア発注サブNOが1以上だからと言って0が存在しないわけではない。 ペア発注サブNOを検索条件に追加
          var existPairData1 = pairChkTbl.filter(e => e.tblIraiNo == row.tblIraiNo && e.ペア発注サブNo == row.ペア発注サブNo);
          // 上記の条件にプラスしてチェックされているかどうかを見る
          var existPairData2 = pairChkTbl.filter(e => e.tblIraiNo == row.tblIraiNo && e.ペア発注サブNo == row.ペア発注サブNo && this.$refs.table.getCheckboxSelected().includes(e.key));
          
          if (existPairData1.length != existPairData2.length) {
            // 通常のものとチェックされた明細の数が一致しなかった場合、ペアを全て選択できていないのでエラーとする
            _this.fnc_void_showErrorMsg("E040791");

            return false;
          }
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
        _this.fnc_void_showTourokuMsg("K040086");
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

    ///////////////////////////////////////////////
    // TS00501（口座選択サブ画面）表示
    ///////////////////////////////////////////////
    async lnkKozaBangoOnClick(row) {
      console.log("lnkKozaBangoOnClick");
      
      // 対象データが口座番号変更可能かどうかチェック
      if (row.tblAPKakuninFlg == "1")
      {
        // K002のAP確認区分が1の場合、口座番号は変更出来ない。
        this.fnc_void_showInfoMsg("E040792")

        return false;
      }

      if (row.tblShiharaiSyoninKBN == "1")
      {
        // K002の支払承認区分が1(支払承認済)の場合、口座番号は変更出来ない。
        this.fnc_void_showInfoMsg("E040798")

        return false;
      }

      let kkcode = "";

      if (row.kubetu == 'true')
      {
        // 修繕データ
        if ((row.syuuzenkubun_syou == "01" && row.hutankubun == "2") ||
            (row.syuuzenkubun_syou == "02" && row.hutankubun == "2") ||
            (row.syuuzenkubun_syou == "03" && row.hutankubun == "2") ||
            (row.syuuzenkubun_syou == "04" && row.hutankubun == "2"))
        {
            // 修繕区分小が01(一般)、02(緊急),03(緊急6項目),04(計画)かつ負担区分が2(テナント)
            kkcode = "SGW01";
        }
        else
        {
            // 上記の組み合わせ以外全て
            kkcode = "SGW37";
        }
      }else{
        // 定期データ
        if (row.syuuzenkubun_syou == "08")
        {
            if (row.hutankubun == "1")
            {
                // 直接SGR
                kkcode = "SGW37";
            }
            else if (row.hutankubun == "2")
            {
                // 直接SGW(直接テナント)
                kkcode = "SGW01";
            }
        }
        else if (row.syuuzenkubun_syou == "09")
        {
            if (row.hutankubun == "2")
            {
                // 請負SGW(請負テナント)
                kkcode = "SGW37";
            }
            else if (row.hutankubun == "3")
            {
                // 請負その他
                kkcode = "SGW37";
            }
        }
      }

      // モーダル表示フラグを立てる
      this.TeikiSeikyuSubSubParams.種類 = row.kubetu;
      this.TeikiSeikyuSubSubParams.会社コード = row.会社コード;
      this.TeikiSeikyuSubSubParams.契約NO = row.tblKeiyakuNo;
      this.TeikiSeikyuSubSubParams.履歴NO = row.履歴NO;
      this.TeikiSeikyuSubSubParams.明細NO = row.明細NO;
      this.TeikiSeikyuSubSubParams.契約年月 = row.契約年月;
      this.TeikiSeikyuSubSubParams.工事依頼NO = row.tblIraiNo;
      this.TeikiSeikyuSubSubParams.工事依頼NO枝番 = row.工事依頼NO枝番;
      this.TeikiSeikyuSubSubParams.発注サブNO = row.発注サブNO;
      this.TeikiSeikyuSubSubParams.検収回数 = row.検収回数;
      this.TeikiSeikyuSubSubParams.回収支払契約番号 = row.tblKaisyuukeiyakuNO;
      this.TeikiSeikyuSubSubParams.会計会社コード = kkcode;

      this.showTS00501 = true;
    },

    fnc_TS00501Close(e) {
      console.log("TS00501を閉じました");

      let kousindata = new Object();
      kousindata = this.KensakuJoken;
      this.fnc_obj_getKensakuKekka(kousindata);
    },
    ///////////////////////////////////////////////
    // 物件名データ取得
    ///////////////////////////////////////////////
    reafsmodalBackBukkenmei(obj) {
      // コード
      this.KensakuJoken.物件コード = obj.txtCd;
      // 名称
      this.ラベル名.物件名 = obj.lblNm;
    },
    ///////////////////////////////////////////////
    // 営業店データ取得・制御
    ///////////////////////////////////////////////
    reafsmodalBackEigyoten(obj) {
      if (obj.txtCd.length >= 1) {
        // コード
        this.KensakuJoken.営業店コード = obj.txtCd.substr(0, 4);
        this.KensakuJoken.営業店コード枝番 = obj.txtCd.substr(4, 2);

        this.KensakuJoken.営業店コード表示 = obj.txtCd;
        // // 名称
        this.ラベル名.営業店名 = obj.lblNm;
      } else {
      }
    },
    onblur_eigyou() {
      if (this.KensakuJoken.営業店コード表示 == "") {
        this.ラベル名.営業店名 = "";
        this.KensakuJoken.営業店コード = "";
        this.KensakuJoken.営業店コード枝番 = "";
      }
    },

    ///////////////////////////////////////////////
    // 工事区分取得
    ///////////////////////////////////////////////
    selectKouzikubun(val) {
      this.http
        .get(
          "api/Reafs_T/Seikyu/TD00500_GET_Kouzikubun",
          "アクセスしています...."
        )
        .then(response => {
          $select工事区分value.工事区分value = response.data.名称取得方法07;
          this.KensakuJoken.工事区分 = "-";
        });
    },
    ddlValue(val) {
      if (val) {
        val = val.replace("-", "");
      }
      return val;
    },
    getDisplayData() {
      return {
        工事区分: this.ddlValue(this.KensakuJoken.工事区分)
      };
    },

    ///////////////////////////////////////////////
    // リンククリック時の画面遷移またはモーダル表示処理
    ///////////////////////////////////////////////
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
            業者コード枝番: row.業者コード枝番,
            見積書枝番: row.hansu
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

        this.KeyDataToTS00402.keiyaku_no = kensaku[0].契約NO; //契約NO
        this.KeyDataToTS00402.rireki_no = kensaku[0].履歴No; //履歴NO
        this.KeyDataToTS00402.meisai_no = kensaku[0].明細NO; //明細NO
        this.KeyDataToTS00402.keiyaku_nengetu = kensaku[0].契約年月; //契約年月
        this.KeyDataToTS00402.nengetu_meisai = kensaku[0].契約年月明細NO; //契約明細NO
        this.KeyDataToTS00402.check_gamen = 1;
        this.showModalPrefectureWindow402(this.KeyDataToTS00402);

        this.showTS00402 = true;
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
    // ダイアログ出力時処理(TS00402)
    ///////////////////////////////////////////////
    async showModalPrefectureWindow402(letvalB) {
      let _this = this;

      await _this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00402_GET_KensakuKekka",
          letvalB,
          "表示しています...."
        )
        .then(function(response_kanryou) {
          if (!response_kanryou.status) {
            _this.MsgErr({
              title: "完了報告入力",
              message: response_kanryou.message
            });
          } else {
            console.log(response_kanryou);
            _this.KeyDataToTS00402.契約管理番号 =
              response_kanryou.data[0].契約管理番号; //契約管理番号
            _this.KeyDataToTS00402.物件名 = response_kanryou.data[0].物件名; //物件名
            _this.KeyDataToTS00402.営業店 = response_kanryou.data[0].営業店; //営業店
            _this.KeyDataToTS00402.契約名 = response_kanryou.data[0].契約名; //契約名称
            _this.KeyDataToTS00402.大区分 = response_kanryou.data[0].大区分; //大区分
            _this.KeyDataToTS00402.中区分 = response_kanryou.data[0].中区分; //中区分
            _this.KeyDataToTS00402.小区分 = response_kanryou.data[0].小区分; //小区分
            _this.KeyDataToTS00402.作業名称 = response_kanryou.data[0].作業名称; //作業名称
            _this.KeyDataToTS00402.作業月 =
              response_kanryou.data[0].実施予定年月 +
              "　" +
              response_kanryou.data[0].回目 +
              "回目報告"; //作業回数
            //日付・時間がNullの場合は無表記になるようにする。
            if (response_kanryou.data[0].作業実施日_from == null) {
              response_kanryou.data[0].作業実施日_from = "";
            } else {
              _this.KeyDataToTS00402.作業実施日_from =
                response_kanryou.data[0].作業実施日_from;
            }
            if (response_kanryou.data[0].作業実施日_to == null) {
              response_kanryou.data[0].作業実施日_to = "";
            } else {
              _this.KeyDataToTS00402.作業実施日_to =
                response_kanryou.data[0].作業実施日_to; //作業完了日
            }
            _this.KeyDataToTS00402.作業担当者 =
              response_kanryou.data[0].作業担当者; //作業担当者
            if (response_kanryou.data[0].現地確認あり === "1") {
              _this.KeyDataToTS00402.現地確認あり = true;
            } else {
              _this.KeyDataToTS00402.現地確認あり = false;
            } //現地確認サイン
            _this.KeyDataToTS00402.完了報告 = response_kanryou.data[0].完了報告; //完了報告
            _this.KeyDataToTS00402.差戻事由 = response_kanryou.data[0].差戻事由; //差戻事由
            _this.KeyDataToTS00402.担当者連絡先 =
              response_kanryou.data[0].担当者連絡先; //作業担当者連絡先
            _this.KeyDataToTS00402.check_version =
              response_kanryou.data[0].check_version; //バージョン　比較用
            _this.KeyDataToTS00402.請求備考 = response_kanryou.data[0].請求備考; //請求備考
            _this.KeyDataToTS00402.実費請求金額税抜 = Number(
              response_kanryou.data[0].実費請求金額税抜
            ).toLocaleString();
          } //実費請求金額税抜
          _this.KeyDataToTS00402.申請金額消費税 = Number(
            response_kanryou.data[0].申請金額消費税
          ).toLocaleString();
          _this.KeyDataToTS00402.税率 = response_kanryou.data[0].税率;
          if (response_kanryou.data[0].税区分 == "1") {
            _this.KeyDataToTS00402.税区分 = "0";
          } else if (response_kanryou.data[0].税区分 == "2") {
            _this.KeyDataToTS00402.税区分 = "1";
          } //税区分
          _this.KeyDataToTS00402.例月請求額税抜 = Number(
            response_kanryou.data[0].例月請求額税抜
          ).toLocaleString(); //例月請求額
          _this.KeyDataToTS00402.提出先 = response_kanryou.data[0].提出先;
          _this.KeyDataToTS00402.進捗コード =
            response_kanryou.data[0].進捗コード; //進捗コード
          _this.KeyDataToTS00402.完了報告書不備 =
            response_kanryou.data[0].完了報告書不備; //完了報告書不備
          _this.KeyDataToTS00402.完了報告書不備解消日 =
            response_kanryou.data[0].完了報告書不備解消日; //完了報告書不備解消日
          _this.KeyDataToTS00402.請求書不備 =
            response_kanryou.data[0].請求書不備; //請求書不備
          _this.KeyDataToTS00402.請求書不備解消日 =
            response_kanryou.data[0].請求書不備解消日; //請求書不備解消日
          _this.KeyDataToTS00402.実施予定年月 =
            response_kanryou.data[0].実施予定年月; //実施予定年月
          _this.KeyDataToTS00402.kaisya_CD = response_kanryou.data[0].kaisya_CD; //会社コード
          _this.KeyDataToTS00402.取引先コード =
            response_kanryou.data[0].取引先コード; //取引先コード
          _this.KeyDataToTS00402.取引先コード枝番 =
            response_kanryou.data[0].取引先コード枝番; //取引先コード枝番
          _this.KeyDataToTS00402.工事依頼NO =
            response_kanryou.data[0].工事依頼NO; //工事依頼NO
          _this.KeyDataToTS00402.WEB合意 = response_kanryou.data[0].WEB合意; //web合意　1：WEB 0:ローカル
          _this.KeyDataToTS00402.WEB合意開始月 =
            response_kanryou.data[0].WEB合意開始月; //WEB合意開始月
          _this.KeyDataToTS00402.予定日開始 =
            response_kanryou.data[0].予定日開始; //予定日開始
          _this.KeyDataToTS00402.作業回数 = response_kanryou.data[0].作業回数;
          _this.KeyDataToTS00402.検収売上税率 =
            response_kanryou.data[0].検収売上税率;

          if (_this.KeyDataToTS00402.作業回数 > "0") {
            if (
              _this.KeyDataToTS00402.進捗コード >= "530" &&
              _this.KeyDataToTS00402.進捗コード < "550"
            ) {
              if (
                _this.KeyDataToTS00402.WEB合意 == "1" &&
                _this.KeyDataToTS00402.実施予定年月 >=
                  _this.KeyDataToTS00402.WEB合意開始月
              ) {
                _this.左側 = false;
                _this.右側金額 = false;
                _this.削除ボタン = false;
                _this.登録ボタン = false;
              } else {
                _this.左側 = false;
                _this.右側金額 = true;
                _this.削除ボタン = false;
                _this.登録ボタン = false;
              }
            } else if (
              _this.KeyDataToTS00402.進捗コード >= "550" &&
              (_this.KeyDataToTS00402.完了報告書不備 == 1 ||
                _this.KeyDataToTS00402.請求書不備 == 1) &&
              _this.KeyDataToTS00402.完了報告書不備解消日 == "" &&
              _this.KeyDataToTS00402.請求書不備解消日 == ""
            ) {
              _this.左側 = true;
              _this.右側金額 = true;
              _this.削除ボタン = false;
              _this.登録ボタン = true;
            } else {
              _this.左側 = true;
              _this.右側金額 = true;
              _this.削除ボタン = true;
              _this.登録ボタン = true;
            }
          } else {
            _this.左側 = true;
            _this.右側金額 = false;
            _this.削除ボタン = false;
            _this.登録ボタン = false;
          }
          //メールの引数
          _this.KeyDataToTS00402.sendMailJimusyoCode =
            response_kanryou.data[0].sendMailJimusyoCode;
          _this.KeyDataToTS00402.sendMailEigyosyoCode =
            response_kanryou.data[0].sendMailEigyosyoCode;
          _this.KeyDataToTS00402.sendMailBuCode =
            response_kanryou.data[0].sendMailBuCode;
          _this.KeyDataToTS00402.sendMailKaCode =
            response_kanryou.data[0].sendMailKaCode;
          _this.KeyDataToTS00402.sendMailKakariCode =
            response_kanryou.data[0].sendMailKakariCode;
          _this.KeyDataToTS00402.attachfile_sendMailJimusyoCode = 
            response_kanryou.data[0].attachfile_sendMailData.substring(0, 3);
          _this.KeyDataToTS00402.attachfile_sendMailEigyosyoCode =
            response_kanryou.data[0].attachfile_sendMailData.substring(3, 9);
          _this.KeyDataToTS00402.attachfile_sendMailBuCode =
            response_kanryou.data[0].attachfile_sendMailData.substring(9, 12);
          _this.KeyDataToTS00402.attachfile_sendMailKaCode =
            response_kanryou.data[0].attachfile_sendMailData.substring(12, 15);
          _this.KeyDataToTS00402.attachfile_sendMailKakariCode =
            response_kanryou.data[0].attachfile_sendMailData.substring(15);

          console.log(
            "メール送信パラメータ：" +
              _this.KeyDataToTS00402.attachfile_sendMailJimusyoCode + " " +
              _this.KeyDataToTS00402.attachfile_sendMailEigyosyoCode + " " +
              _this.KeyDataToTS00402.attachfile_sendMailBuCode + " " +
              _this.KeyDataToTS00402.attachfile_sendMailKaCode + " " +
              _this.KeyDataToTS00402.attachfile_sendMailKakariCode
          );

          //チェック用
          _this.KeyDataToTS00402.check作業実施日_from =
            response_kanryou.data[0].作業実施日_from; //作業開始日
          _this.KeyDataToTS00402.check作業実施日_to =
            response_kanryou.data[0].作業実施日_to; //作業終了日
          _this.KeyDataToTS00402.check作業担当者 =
            response_kanryou.data[0].作業担当者; //作業担当者
          if (response_kanryou.data[0].現地確認あり == "1") {
            _this.KeyDataToTS00402.check現地確認あり = true;
          } else {
            _this.KeyDataToTS00402.check現地確認あり = false;
          } //現地確認サイン
          _this.KeyDataToTS00402.check完了報告 =
            response_kanryou.data[0].完了報告; //完了報告
          _this.KeyDataToTS00402.check担当者連絡先 =
            response_kanryou.data[0].担当者連絡先; //作業担当者連絡先
          _this.KeyDataToTS00402.check例月請求額税抜 =
            response_kanryou.data[0].例月請求額税抜; //例月請求額税抜
          _this.KeyDataToTS00402.check実費請求金額税抜 =
            response_kanryou.data[0].実費請求金額税抜; //実費税抜
        });
      await _this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00402_GET_KensakuKekka_g",
          letvalB,
          "表示しています...."
        )
        .then(function(response_grid) {
          if (!response_grid.status) {
            _this.MsgErr({
              title: "完了報告入力",
              message: response_grid.message
            });
          } else {
            _this.KeyDataToTS00402.TS00402_grid = response_grid.data;

            //チェック用
            _this.KeyDataToTS00402.check_grid_date = response_grid.data;
          }
        });
      await _this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00402_GET_KensakuKekka_g_Kinagaku",
          letvalB,
          "表示しています...."
        )
        .then(function(response_grid) {
          if (!response_grid.status) {
            _this.MsgErr({
              title: "完了報告入力",
              message: response_grid.message
            });
          } else {
            _this.KeyDataToTS00402.TS00402_grid_kingaku = response_grid.data;
            let kariire = _this.KeyDataToTS00402.TS00402_grid_kingaku;
            let 作業合計税抜き = 0;
            let 売上合計税抜き = 0;

            // グリッド内のデータを計算
            for (let kaisuu in kariire) {
              if (kariire[kaisuu].click_seikyu == "〇") {
                // 原価計算
                作業合計税抜き += kariire[kaisuu].seikyukingaku;
                // 売価計算
                売上合計税抜き += kariire[kaisuu].uriagekingaku;
              } else {
              }
            }
            // 原価合計額格納
            _this.KeyDataToTS00402.作業合計請求税抜 = 作業合計税抜き.toLocaleString();
            _this.KeyDataToTS00402.check作業合計請求税抜 = 作業合計税抜き.toLocaleString();

            // 売価合計額格納
            _this.KeyDataToTS00402.検収売上金額_税抜 = Number(売上合計税抜き);

            // 原価税計算
            let 申請金税抜き = 0;
            申請金税抜き =
              作業合計税抜き +
              Number(_this.KeyDataToTS00402.実費請求金額税抜.replace(/,/g, ""));
            _this.KeyDataToTS00402.申請金額税抜 = 申請金税抜き.toLocaleString();
            let 消費税 = 0;
            let 税率 = 0;
            税率 = _this.KeyDataToTS00402.税率;
            消費税 = _this.KeyDataToTS00402.申請金額消費税.replace(/,/g, "");
            消費税 = Number(消費税);
            if (
              _this.KeyDataToTS00402.申請金額消費税.length == 0 ||
              _this.KeyDataToTS00402.申請金額消費税 == "0"
            ) {
              if (税率 == 0 || 税率 == "" || 税率 == null) {
                _this.KeyDataToTS00402.申請金額消費税 = "0";
              } else {
                消費税 = Math.floor((Number(申請金税抜き) * 税率) / 100);
                _this.KeyDataToTS00402.申請金額消費税 = 消費税.toLocaleString();
              }
            }
            // 原価税込計算
            _this.KeyDataToTS00402.申請金額税込 =
              Number(消費税) + Number(申請金税抜き);
            _this.KeyDataToTS00402.申請金額税込 = _this.KeyDataToTS00402.申請金額税込.toLocaleString();

            // 売価税計算
            let 原価消費税 = 0;
            let 税率売価 = 0;

            let 消費税売価 = 0;
            税率売価 = _this.KeyDataToTS00402.検収売上税率;
            if (税率売価 == 0 || 税率売価 == "" || 税率売価 == null) {
              _this.KeyDataToTS00402.検収売上金額_消費税 = 0;
            } else {
              消費税売価 = Math.floor(
                (Number(売上合計税抜き) * 税率売価) / 100
              );
              _this.KeyDataToTS00402.検収売上金額_消費税 = Number(消費税売価);
            }
            // 売上税込計算
            _this.KeyDataToTS00402.検収売上金額_税込 =
              Number(消費税売価) + Number(売上合計税抜き);
          }
        });
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
  padding: 15px;
  width: 100%;
  height: 100%;
}

.home-container {
  padding: 15px;
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
.home-el-footer {
  margin: 7% 0 0 0;
}
.home-el-body {
  height: 450px;
}
</style>
<style scoped>
.hyouzikei >>> .el-input__inner {
  text-align: right;
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
