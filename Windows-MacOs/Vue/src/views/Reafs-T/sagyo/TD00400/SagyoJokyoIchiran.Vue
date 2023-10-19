<template @open="onOpen()">
  <div class="home-contianer SagyoJokyoIchiran" @open="onOpen()">
    <!-- 検索部 -->
    <el-row>
      <!-- １行目 -->
      <el-row>
        <el-form :model="KensakuJoken" label-width="100px">
          <!--1行 1列目-->
          <el-col :span="8">
            <div class="nengetu">
              <el-form-item label="対象月指定">
                <el-date-picker
                  :required="true"
                  type="month"
                  format="yyyy/MM"
                  value-format="yyyy/MM"
                  :clearable="false"
                  :width="'120px'"
                  v-model="KensakuJoken.dtp対象月_from"
                  :class="{ pinkcolor_month: isErrPinkSagyotsuki }"
                  index="1"
                  ref="ref作業月from"
                  @blur="fnc_tub(0)"
                  tabindex="1"
                ></el-date-picker>
                ~
                <el-date-picker
                  :required="true"
                  type="month"
                  format="yyyy/MM"
                  value-format="yyyy/MM"
                  :clearable="false"
                  :width="'120px'"
                  v-model="KensakuJoken.dtp対象月_to"
                  :class="{ pinkcolor_month: isErrPinkSagyotsuki }"
                  @blur="fnc_tub(1)"
                  ref="ref作業月to"
                  tabindex="2"
                ></el-date-picker>
              </el-form-item>
            </div>
          </el-col>
          <!-- <el-col :span="1"></el-col> -->
          <el-col :span="14">
            <el-radio-group v-model="KensakuJoken.rgb年月">
              <el-radio
                class="cls-chk-paddnig-left"
                label="作業月"
                ref="rgb作業月_月"
                tabindex="3"
              ></el-radio>
              <el-radio
                class="cls-chk-paddnig-left"
                label="作業予定日"
                ref="rgb作業予定日_月"
                tabindex="4"
              ></el-radio>
              <el-radio
                class="cls-kakutei-margin-left"
                label="作業完了日"
                tabindex="5"
              ></el-radio>
              <el-radio
                class="cls-chk-paddnig-left"
                label="作業結果承認日"
                tabindex="6"
              ></el-radio>
            </el-radio-group>
          </el-col>
          <el-col :span="2">
            <el-button
              @click="fnc_change_gamen_flg()"
              tabindex="29"
              @keydown.native="down2first"
            >
              &nbsp;&nbsp;検索&nbsp;&nbsp;&nbsp;
            </el-button>
          </el-col>
        </el-form>
      </el-row>
      <el-row>
        <el-form :model="KensakuJoken" label-width="100px">
          <!--1行 1列目-->
          <el-col :span="8">
            <div class="nengetu">
              <el-form-item label="対象日指定">
                <el-date-picker
                  :required="true"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  :clearable="false"
                  :width="'160px'"
                  v-model="KensakuJoken.dtp対象日_from"
                  :class="{ pinkcolor_date: isErrPinkSagyodate }"
                  ref="ref対象日from"
                  tabindex="7"
                  class="row_date"
                ></el-date-picker>
                ~
                <el-date-picker
                  :required="true"
                  type="date"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  :clearable="false"
                  :width="'160px'"
                  v-model="KensakuJoken.dtp対象日_to"
                  :class="{ pinkcolor_date: isErrPinkSagyodate }"
                  tabindex="8"
                  class="row_date"
                ></el-date-picker>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="1"><label>&nbsp;</label></el-col>
          <!--3行 1列目-->
          <el-col :span="7">
            <el-radio-group v-model="KensakuJoken.rgb年月日">
              <el-radio
                class="cls-chk-paddnig-left"
                label="作業予定日"
                tabindex="9"
              ></el-radio>
              <el-radio
                class="cls-kakutei-margin-left"
                label="作業完了日"
                tabindex="10"
              ></el-radio>
              <el-radio
                class="cls-chk-paddnig-left"
                label="作業結果承認日"
                tabindex="11"
              ></el-radio>
            </el-radio-group>
          </el-col>
          <el-col :span="2"><label>ステータス指定：</label></el-col>
          <el-col :span="2">
            <el-button @click="fnc_click_Zyouken(0)" tabindex="27"
              >予定入力</el-button
            >
          </el-col>
          <el-col :span="2">
            <el-button @click="fnc_click_Zyouken(1)" tabindex="28"
              >完了入力</el-button
            >
          </el-col>
        </el-form>
      </el-row>
      <el-row>
        <el-form :model="KensakuJoken" label-width="100px">
          <el-col :span="22">
            <!--3行 1列目-->
            <el-form-item label="ステータス" class="checkbox-left">
              <div>
                <el-checkbox
                  class="cls-chk-paddnig-left"
                  label="作業予定未入力/作業予定差戻"
                  v-model="KensakuJoken.chk作業予定未入力"
                  ref="ref作業予定未入力"
                  tabindex="14"
                  style="width: 210px;margin: 0 10px;"
                ></el-checkbox>
                <el-checkbox
                  class="cls-kakutei-margin-left checkbox-align"
                  label="作業予定打診中"
                  v-model="KensakuJoken.chk作業予定打診中"
                  tabindex="15"
                  style="width: 110px;margin: 0 10px;"
                ></el-checkbox>
                <el-checkbox
                  class="cls-chk-paddnig-left"
                  label="作業未完了/作業完了差戻"
                  v-model="KensakuJoken.chk作業未完了"
                  tabindex="16"
                  style="width: 175px;margin: 0 10px;"
                ></el-checkbox>
                <el-checkbox
                  class="cls-chk-paddnig-left"
                  label="作業完了確認打診中"
                  v-model="KensakuJoken.chk作業完了確認打診中"
                  tabindex="17"
                  style="width: 140px;margin: 0 10px;"
                ></el-checkbox>
                <el-checkbox
                  class="cls-chk-paddnig-left"
                  label="作業完了確認済"
                  v-model="KensakuJoken.chk作業完了確認済"
                  tabindex="18"
                  style="width: 115px;margin: 0 10px;"
                ></el-checkbox>
                <el-checkbox
                  class="cls-chk-paddnig-left"
                  label="作業中止"
                  v-model="KensakuJoken.chk作業中止"
                  tabindex="19"
                  style="width: 80px;margin: 0 10px;"
                ></el-checkbox>
                <el-checkbox
                  class="cls-chk-paddnig-left"
                  label="添付修正要"
                  v-model="KensakuJoken.chk添付修正要"
                  tabindex="20"
                  style="width: 80px;margin: 0 10px;"
                ></el-checkbox>
              </div>
            </el-form-item>
          </el-col>
     

          <el-col :span="1">
            <el-button @click="fnc_click_Zyouken(2)" tabindex="26"
              >検索条件<br />リセット</el-button
            >
          </el-col>
        </el-form>
      </el-row>
      <!--2行目-->
      <el-row>
        <el-form ref="KensakuJoken2" :model="KensakuJoken" label-width="150px">
          <!--2行 1列目-->
          <el-col :span="9">
            <el-form-item label="物件">
              <opensearch
                v-model="KensakuJoken.txt物件CD"
                maxlength="8"
                :disabled="false"
                :appendlabelText="bukkenNm"
                :append="'append'"
                :path="'reafsmodalSearchBukken'"
                :class="{ pinkcolor_bukken: isErrPinkBukken }"
                @backData="reafsmodalBack_searchBukken"
                @blur="onblur_bukken()"
                :width="'135px'"
                ref="ref物件"
                tabindex="21"
              >
              </opensearch>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="物件住所">
              <div style="display: flex">
                <reafsinputtext
                  v-model="Bukkenjyusyo.物件住所CD"
                  maxlength="8"
                  disabled
                  :width="'110px'"
                >
                  :class="[appendBackground ? 'append-background' : '']"
                </reafsinputtext>
                <reafsinputtext
                  v-model="Bukkenjyusyo.物件住所NM"
                  maxlength="30"
                  disabled
                  :width="'400px'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <el-row>
        <el-form
          :model="KensakuJoken"
          label-width="150px"
          @submit.native.prevent
        >
          <el-col :span="9">
            <div style="display: flex">
              <el-form-item label="営業店" prop="eigyoten">
                <opensearch
                  v-model="KensakuJoken.txt営業店CD"
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
        </el-form>
      </el-row>

      <!-- 3行目 -->
      <el-row>
        <el-form label-width="150px">
          <!--3行 1列目-->
          <el-col :span="5">
            <el-form-item label="契約No./契約管理番号">
              <reafsinputtext
                :width="'135px'"
                type="text"
                maxlength="20"
                v-model="KensakuJoken.txt契約NO"
                tabindex="23"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
          <!--3行 2列目-->
          <el-col :span="8">
            <el-form-item label="契約名・作業名">
              <reafsinputtext
                :width="'300px'"
                maxlength="30"
                v-model="KensakuJoken.txt契約名"
                tabindex="24"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="作業予定/備考">
              <reafsinputtext
                :width="'300px'"
                maxlength="30"
                v-model="KensakuJoken.txt作業予定"
                tabindex="25"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </el-row>
    <!-- 明細部 -->
    <!-- 検索ヒット件数 -->
    <div class="cls-hitkensu_align_right">{{ hitKensu }}</div>

    <!-- 明細テーブル -->
    <div class="grid_height">
      <el-row :gutter="20">
        <div class="grid-content bg-purple-light">
          <!-- <reafstable
            :single="single"
            :rowKey="rowKey"
            :tableData="tblData"
            :columns="tblColumns"
            :max-height="572"
            :height="572"
            :columnIndex="false"
            :columnChkbox="false"
            :linkView="fnc_clickLink"
            :key="Math.random()"
            :row-class-name="tableRowClassName"
            :rowClassFunction="this.rowClassFunction"
            tabindex="30"
          >
          </reafstable> -->
          <reafstable
            ref="table"
            :tableData="tblData"
            :columns="tblColumns"
            :maxHeight="572"
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
    </div>
    <el-footer class="home-el-footer" height="auto">
      <el-row>
        <div>
          <el-col :span="9">
            <el-button tabindex="27" @click="onClickBack">戻る </el-button>
          </el-col>
        </div>
      </el-row>
    </el-footer>

    <!-- 定期作業予定入力モーダル -->
    <modalTS00401
      :dialogVisible.sync="showTS00401"
      :TS00401_tourokuJoken.sync="KeyDataToTS00401"
      :touroku1.sync="touroku_b1"
      :koumoku1.sync="koumoku_t1"
      :改版チェック.sync="改版チェック"
      @closePopupEvent="fnc_TS00401Close"
    ></modalTS00401>

    <!-- 定期作業報告入力モーダル -->
    <modalTS00402
      :dialogVisible.sync="showTS00402"
      :TS00402_tourokuJoken.sync="KeyDataToTS00402"
      :左側.sync="左側"
      :右側金額.sync="右側金額"
      :削除ボタン.sync="削除ボタン"
      :登録ボタン.sync="登録ボタン"
      :touroku.sync="touroku"
      :改版チェック.sync="改版チェック"
      @closePopupEvent="fnc_TS00402Close"
    ></modalTS00402>
  </div>
</template>

<script>
import opensearch from "@/components/basic/openSearch.vue";
import reafstable from "@/components/table/Grid.vue";
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnaturalnumber from "@/components/basic/ReafsControl/ReafsInputNaturalNumber.vue";
import reafsmodal from "@/components/modal/Modal.vue";
import eigyoten from "@/components/basic/EigyoTen.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import modalTS00401 from "@/views/Reafs-T/sagyo/TS00401/TeikiSagyoYoteiNyuryoku.vue";
import modalTS00402 from "@/views/Reafs-T/sagyo/TS00402/TeikiSagyoHokokuNyuryoku.vue";

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
    modalTS00401,
    modalTS00402,
    reafstable,
    reafsinputtext,
    reafsinputnaturalnumber,
    reafsmodal,
    eigyoten,
    nengetsu
  },
  data() {
    return {
      ///////////////////////////////////////////////
      // タブ指定
      ///////////////////////////////////////////////
      TAB_IDX_SAGYOTSUKI: "1",


      ///////////////////////////////////////////////
      // TD00400 -> TS00401,TS00402共通 データ受け渡しインターフェイス
      ///////////////////////////////////////////////
      改版チェック: false,

      ///////////////////////////////////////////////
      // TD00400 -> TS00401 データ受け渡しインターフェイス
      ///////////////////////////////////////////////
      showTS00401: false,
      touroku_b1: false,
      koumoku_t1: false,
      KeyDataToTS00401: {
        //表示
        keiyakukanri_no: "", //契約管理番号
        bukken_name: "", //物件名
        eigyou_name: "", //営業店
        keiyaku_name: "", //契約名称
        sagyou_c_dai: "", //作業カテゴリー大
        sagyou_c_tyuu: "", //作業カテゴリー中
        sagyou_c_syou: "", //作業カテゴリー小
        sagyou_name: "", //作業名称
        keiyaku_nengetu: "", //契約年月
        nengetu_meisai: "", //年月明細
        sagyou_tuki_kaisuu: "", //作業月/回数
        sagyou_yotei_bikou: "", //作業予定/備考
        sasimodori_ziyuu: "", //差戻事由
        yoteikaisi_date: "", //作業予定開始日
        yoteisyuuryou_date: "", //作業予定終了日
        sagyoyotei_day: "", //作業予定日
        sagyoyotei_kaisi_time: "", //作業予定開始時間
        sagyoyotei_syuuryou_time: "", //作業予定終了時間
        sagyou_tantou_name: "", //作業担当者名
        sagyoutantou_tel: "", //担当者連絡先
        TS00401_grid: [], //TS00401グリッド
        check_version: "", //比較用

        //バックデータ
        keiyaku_no: "", //契約NO
        rireki_no: 0, //履歴NO
        meisai_no: 0, //明細NO
        kouziirai_no: "", //工事依頼NO
        kaisya_cd: "", //会社コード
        host: "", //ホスト
        user_id: "", //ユーザーID
        pg: "TS00401", //PG
        sintyoku_CD: 0, //進捗コード
        sagyo_date_hikaku: "", //実施予定年月
        kaihan_Flg: "",
        kakutei_flag: "", //経理年月
        date: "",
        sendMailJimusyoCode: "",
        sendMailEigyosyoCode: "",
        sendMailBuCode: "",
        sendMailKaCode: "",
        sendMailKakariCode: "",

        //閉じるチェック用
        check_yoteikaisi_date: "", //作業予定開始日
        check_yoteisyuuryou_date: "", //作業予定終了日
        check_sagyoyotei_kaisi_time: "", //作業予定開始時間
        check_sagyoyotei_syuuryou_time: "", //作業予定終了時間
        check_sagyou_yotei_bikou: "", //作業予定/備考
        check_sagyoutantou_tel: "", //担当者連絡先
        check_sagyou_tantou_name: "", //作業担当者名
        check_grid_date: []
      },
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
        改版チェック: "",

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
      // 検索部
      /////////////////////////////////////////////
      KensakuJoken: {
        dtp対象月_from: "",
        dtp対象月_to: "",
        rgb年月: "作業月",
        dtp対象日_from: "",
        dtp対象日_to: "",
        rgb年月日: "作業予定日",
        chk作業予定未入力: false,
        chk作業予定打診中: false,
        chk作業未完了: false,
        chk作業完了確認打診中: false,
        chk作業完了確認済: false,
        chk作業中止: false,
        chk添付修正要: false,
        txt物件CD: "",
        txt契約NO: "",
        txt契約名: "",
        txt作業予定: "",
        txt営業店CD: "",
        loginID: "",
        ユーザー名: "",
        gamen_flg: 0
      },
      Bukkenjyusyo: {
        物件住所CD: "",
        物件住所NM: ""
      },
      bukkenNm: "", // 物件名
      eigyouNm: "", //営業店

      ///////////////////////////////////////////////
      // 明細部
      ///////////////////////////////////////////////
      hitKensu: "0  件",
      cellStyleOption: {
        headerCellClass: ({ row, column, rowIndex }) => {},
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = "";

          const textLeft = [
            "tblBukkenmei",
            "tblEigyouten",
            "tblKeiyakuMeisho",
            "tblKeiyakuNO",
            "tblKeiyakuKanriBango",
            "tblSagyomei",
            "tblSagyotsuki_kaime",
            "dgvStatus",
            "tblSagyoYotei_Biko"
          ];
          if (textLeft.includes(column.key)) {
            css += " text-left";
          }

          let sintyoku = row.sintyoku_CD;
          if (sintyoku > "510" && sintyoku < "520") {
            css += " tableRowClassName";
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
          width: 65,
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
          key: "tblBukkenmei",
          title: "物件名",
          field: "tblBukkenmei",
          width: 90,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          fixed: "left",
        },
        
        {
          key: "tblEigyouten",
          title: "営業店",
          field: "tblEigyouten",
          width: 110,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          fixed: "left",
        },

        {
          key: "tblKeiyakuMeisho",
          title: "契約名称",
          field: "tblKeiyakuMeisho",
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
                  title={row["tblKeiyakuMeisho"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKeiyakuMeisho"]) this.fnc_lnkKeiyakuMeishoOnClick(row);
                  }}
                  href="javascript:;"
                >
                  {row["tblKeiyakuMeisho"]}
                </a>
              </div>
            );
          },
          sortBy: "",
          fixed: "left",
        },

        {
          key: "tblKeiyakuNO",
          title: "契約No",
          field: "tblKeiyakuNO",
          width: 130,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
          fixed: "left",
        },

        {
          key: "tblKeiyakuKanriBango",
          title: "契約管理番号",
          field: "tblKeiyakuKanriBango",
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblSagyomei",
          title: "作業名",
          field: "tblSagyomei",
          width: 300,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblSagyotsuki_kaime",
          title: "作業月/回目",
          field: "tblSagyotsuki_kaime",
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "dgvStatus",
          title: "ステータス",
          field: "dgvStatus",
          width: 150,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblSagyoYoteibiStartDate",
          title: "作業予定日(自)",
          field: "tblSagyoYoteibiStartDate",
          width: 160,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblSagyoYoteibiEndDate",
          title: "作業予定日(至)",
          field: "tblSagyoYoteibiEndDate",
          width: 160,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblYoteiShinsei",
          title: "予定申請",
          field: "tblYoteiShinsei",
          width: 110,
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
                  title={row["tblYoteiShinsei"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblYoteiShinsei"]) this.fnc_lnkYoteiShinseiOnClick(row);
                  }}
                  href="javascript:;"
                >
                  {row["tblYoteiShinsei"]}
                </a>
              </div>
            );
          },
          sortBy: "",
        },

        {
          key: "tblSagyoYotei_Biko",
          title: "作業予定/備考",
          field: "tblSagyoYotei_Biko",
          width: 300,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblSagyoKanryobiStartDate",
          title: "作業完了日(自)",
          field: "tblSagyoKanryobiStartDate",
          width: 150,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblSagyoKanryobiEndDate",
          title: "作業完了日(至)",
          field: "tblSagyoKanryobiEndDate",
          width: 150,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },

        {
          key: "tblKanryoHokoku",
          title: "完了報告",
          field: "tblKanryoHokoku",
          width: 150,
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
                  title={row["tblKanryoHokoku"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKanryoHokoku"]) this.fnc_lnkKanryoHokokuOnClick(row);
                  }}
                  href="javascript:;"
                >
                  {row["tblKanryoHokoku"]}
                </a>
              </div>
            );
          },
          sortBy: "",
        },

        {
          key: "tblTenpuShusei",
          title: "添付修正",
          field: "tblTenpuShusei",
          width: 150,
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
                  title={row["tblTenpuShusei"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblTenpuShusei"]) this.fnc_lnkKanryoHokokuOnClick(row);
                  }}
                  href="javascript:;"
                >
                  {row["tblTenpuShusei"]}
                </a>
              </div>
            );
          },
          sortBy: "",
        },

        {
          key: "tblSagyoKekkaShoninbi",
          title: "作業結果承認日",
          field: "tblSagyoKekkaShoninbi",
          width: 150,
          ellipsis: {
            showTitle: true
          },
          sortBy: "",
        },
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
      ///////////////////////////////////////////////
      // エラーフラグ DATEPICKERをピンクにする為に使用
      ///////////////////////////////////////////////
      isErrPinkSagyotsuki: false,
      isErrPinkSagyodate: false,
      isErrPinkBukken: false,
      isErrPinkEigyou: false
    };
  },
  created() {
    let userLogIn = this.$store.getters.getUserInfo();
    this.KensakuJoken.ユーザー名 = userLogIn.ユーザー名;
    if (userLogIn.親ＩＤ === "") {
      this.KensakuJoken.loginID = userLogIn.userId;
    } else {
      this.KensakuJoken.loginID = userLogIn.親ＩＤ;
    }
  },

  mounted() {
    console.log("mounted_TD00400");
    if (this.KensakuJoken.gamen_flg == 0) {
      this.fnc_click_Zyouken(2);
    }
  },
  watch: {
    $route(to, from) {
      let userLogIn = this.$store.getters.getUserInfo();
      this.KensakuJoken.ユーザー名 = userLogIn.ユーザー名;
      if (userLogIn.親ＩＤ === "") {
        this.KensakuJoken.loginID = userLogIn.userId;
      } else {
        this.KensakuJoken.loginID = userLogIn.親ＩＤ;
      }
    }
  },

  methods: {
    onOpen() {},
    initialize() {
      let userLogIn = this.$store.getters.getUserInfo();
      this.KensakuJoken.ユーザー名 = userLogIn.ユーザー名;
      if (userLogIn.親ＩＤ === "") {
        this.KensakuJoken.loginID = userLogIn.userId;
      } else {
        this.KensakuJoken.loginID = userLogIn.親ＩＤ;
      }

      console.log("initialize_TD00400");
      let hikisuu = true;
      hikisuu = this.$route.params.hikisuu;
      if (hikisuu == false) {
        this.KeyDataToTS00402.keiyaku_no = this.$route.params.keiyaku_no; //契約NO
        this.KeyDataToTS00402.rireki_no = this.$route.params.履歴NO; //履歴NO
        this.KeyDataToTS00402.meisai_no = this.$route.params.明細NO; //明細NO
        this.KeyDataToTS00402.keiyaku_nengetu = this.$route.params.契約年月; //契約年月
        this.KeyDataToTS00402.nengetu_meisai = this.$route.params.keiyakumeisai; //契約明細NO
        this.KeyDataToTS00402.check_gamen = 1;
        this.showModalPrefectureWindow402(this.KeyDataToTS00402);
        this.showTS00402 = true;
      } else {
        this.$nextTick(function() {
          this.$refs.ref作業月from.focus();
        });
      }

      let StartupMode = this.$route.params.StartupMode;

      if (StartupMode == "1") {
        // 作業予定未入力
        //今月用 当年度4月をセット
        let now_before;
        now_before = new Date();
        now_before.setMonth(now_before.getMonth());

        let now_before_year;

        //西暦から年度へ
        if (now_before.getMonth <= 3)
        {
          // 現在の月が3月以前なら前年をセット
          now_before_year = now_before.getFullYear() - 1
        } else {
          // そうでない場合は通常の年をセット
          now_before_year = now_before.getFullYear()
        }

        now_before_year = now_before_year.toString();
      
        this.KensakuJoken.dtp対象月_from = now_before_year + "/04";

        //翌月用 システム日付の翌月をセット
        let now_after = new Date();
        now_after.setFullYear(now_after.getFullYear());
        now_after.setMonth(now_after.getMonth() + 2);

        let now_after_year = now_after.getFullYear();
        now_after_year = now_after_year.toString();
        var now_after_mounth = now_after.getMonth();
        now_after_mounth = ("00" + now_after_mounth).slice(-2);

        this.KensakuJoken.dtp対象月_to = now_after_year + "/" + now_after_mounth;

        this.KensakuJoken.gamen_flg = 1;

        // ステータス指定
        this.KensakuJoken.chk作業予定未入力 = true;
        this.KensakuJoken.chk作業予定打診中 = false;
        this.KensakuJoken.chk作業未完了 = false;
        this.KensakuJoken.chk作業完了確認打診中 = false;
        this.KensakuJoken.chk作業完了確認済 = false;
        this.KensakuJoken.chk作業中止 = false;
        this.KensakuJoken.chk添付修正要 = false;

        this.fnc_void_clickBtnKensaku();
      } else if (StartupMode == "2") {
        // 作業完了未入力
        //今月用
        let now_before;
        now_before = new Date();
        now_before.setMonth(now_before.getMonth());

        let now_before_year = now_before.getFullYear();
        now_before_year = now_before_year.toString();
        var now_before_mounth = now_before.getMonth() + 1;
        now_before_mounth = ("00" + now_before_mounth).slice(-2);
        this.KensakuJoken.dtp対象月_from =
          now_before_year + "/" + now_before_mounth;

        //翌月用
        let now_after = new Date();
        now_after.setFullYear(now_after.getFullYear() + 1);
        now_after.setMonth(now_after.getMonth() + 1);

        let now_after_year = now_after.getFullYear();
        now_after_year = now_after_year.toString();
        var now_after_mounth = now_after.getMonth();
        now_after_mounth = ("00" + now_after_mounth).slice(-2);
        this.KensakuJoken.dtp対象月_to =
          now_after_year + "/" + now_after_mounth;

        this.KensakuJoken.gamen_flg = 2;

        // ステータス指定
        this.KensakuJoken.chk作業予定未入力 = false;
        this.KensakuJoken.chk作業予定打診中 = false;
        this.KensakuJoken.chk作業未完了 = true;
        this.KensakuJoken.chk作業完了確認打診中 = false;
        this.KensakuJoken.chk作業完了確認済 = false;
        this.KensakuJoken.chk作業中止 = false;
        this.KensakuJoken.chk添付修正要 = false;

        this.fnc_void_clickBtnKensaku();
      } else if (StartupMode == "3") {
        // 本書類未提出
        //今月用
        let now_before;
        now_before = new Date();
        now_before.setMonth(now_before.getMonth());

        let now_before_year = now_before.getFullYear();
        now_before_year = now_before_year.toString();
        var now_before_mounth = now_before.getMonth() + 1;
        now_before_mounth = ("00" + now_before_mounth).slice(-2);
        this.KensakuJoken.dtp対象月_from =
          now_before_year + "/" + now_before_mounth;

        //翌月用
        let now_after = new Date();
        now_after.setMonth(now_after.getMonth() + 2);

        let now_after_year = now_after.getFullYear();
        now_after_year = now_after_year.toString();
        var now_after_mounth = now_after.getMonth();
        now_after_mounth = ("00" + now_after_mounth).slice(-2);
        this.KensakuJoken.dtp対象月_to =
          now_after_year + "/" + now_after_mounth;

        this.KensakuJoken.gamen_flg = 3;

        this.KensakuJoken.chk作業予定未入力 = false;
        this.KensakuJoken.chk作業予定打診中 = false;
        this.KensakuJoken.chk作業未完了 = false;
        this.KensakuJoken.chk作業完了確認打診中 = false;
        this.KensakuJoken.chk作業完了確認済 = false;
        this.KensakuJoken.chk作業中止 = false;
        this.KensakuJoken.chk添付修正要 = true;

        this.fnc_void_clickBtnKensaku();
      } else if (StartupMode == "4") {
        // メール起動による検索
        let 契約No = this.$route.params.keiyaku_no;
        let 履歴No = this.$route.params.履歴NO;
        let 明細No = this.$route.params.明細NO;
        let 契約年月 = this.$route.params.契約年月;
        let 契約年月明細No = this.$route.params.keiyakumeisai;

        // メール起動固有の検索
        this.fnc_void_search_MailData(
          契約No,
          履歴No,
          明細No,
          契約年月,
          契約年月明細No
        );
      }
    },

    ///////////////////////////////////////////////
    // 検索条件いじったらフラグを戻す
    ///////////////////////////////////////////////
    fnc_change_gamen_flg() {
      this.KensakuJoken.gamen_flg = 0;
      this.fnc_void_clickBtnKensaku();
    },

    ///////////////////////////////////////////////
    // 戻るボタン処理
    ///////////////////////////////////////////////
    onClickBack() {
      this.$router.back();
    },

    ///////////////////////////////////////////////
    // 検索ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnKensaku() {
      let 検索条件コンバート = new Object();
      if (
        this.KensakuJoken.gamen_flg == 0 ||
        this.KensakuJoken.gamen_flg == ""
      ) {
        //検索前エラーチェック
        let letJdg;
        letJdg = this.fnc_obj_kensakumaeErrorChk(
          this.KensakuJoken,
          this.Bukkenjyusyo
        );
        if (letJdg === false) {
          return;
        }
       }// else {
      //   this.KensakuJoken = {
      //     dtp対象月_from: this.KensakuJoken.dtp対象月_from,
      //     dtp対象月_to: this.KensakuJoken.dtp対象月_to,
      //     rgb年月: "作業月",
      //     dtp対象日_from: "",
      //     dtp対象日_to: "",
      //     rgb年月日: "作業予定日",
      //     chk作業予定未入力: 0,
      //     chk作業予定打診中: 0,
      //     chk作業未完了: 0,
      //     chk作業完了確認打診中: 0,
      //     chk作業完了確認済: 0,
      //     chk作業中止: 0,
      //     chk添付修正要: 0,
      //     txt物件CD: "",
      //     txt営業店CD: "",
      //     txt契約NO: "",
      //     txt契約名: "",
      //     txt作業予定: "",
      //     loginID: this.KensakuJoken.loginID,
      //     gamen_flg: this.KensakuJoken.gamen_flg
      //   };
      // }
      // 前準備。検索条件をバックに渡す用にコンバート
      検索条件コンバート = this.fnc_obj_KensakuJokenConvert(this.KensakuJoken);

      // ピンク色等データ初期状態にクリア
      this.fnc_void_clearData();

      //検索結果取得
      this.fnc_obj_getKensakuKekka(検索条件コンバート);
    },

    ///////////////////////////////////////////////
    // 検索条件コンバート
    // バックに渡すパラメータ用にコンバートする
    ///////////////////////////////////////////////
    fnc_obj_KensakuJokenConvert(KensakuJokenConvertDataBefore) {
      let コンバート結果 = new Object();

      // dtp対象月コンバート
      if (
        this.KensakuJoken.dtp対象月_from != "" &&
        this.KensakuJoken.dtp対象月_from != null
      ) {
        if (this.KensakuJoken.dtp対象月_from.constructor == Date) {
          コンバート結果.dtp対象月_from = this.fnc_obj_NengetsuConvert(
            KensakuJokenConvertDataBefore.dtp対象月_from
          );
        } else {
          コンバート結果.dtp対象月_from =
            KensakuJokenConvertDataBefore.dtp対象月_from;
        }
        if (this.KensakuJoken.dtp対象月_to.constructor == Date) {
          コンバート結果.dtp対象月_to = this.fnc_obj_NengetsuConvert(
            KensakuJokenConvertDataBefore.dtp対象月_to
          );
        } else {
          コンバート結果.dtp対象月_to =
            KensakuJokenConvertDataBefore.dtp対象月_to;
        }
      }
      // dtp対象日コンバート
      if (
        this.KensakuJoken.dtp対象日_from != "" &&
        this.KensakuJoken.dtp対象日_from != null
      ) {
        if (this.KensakuJoken.dtp対象日_from.constructor == Date) {
          コンバート結果.dtp対象日_from = this.fnc_obj_NengetsuConvert(
            KensakuJokenConvertDataBefore.dtp対象日_from
          );
        } else {
          コンバート結果.dtp対象日_from =
            KensakuJokenConvertDataBefore.dtp対象日_from;
        }
        if (this.KensakuJoken.dtp対象日_to.constructor == Date) {
          コンバート結果.dtp対象日_to = this.fnc_obj_NengetsuConvert(
            KensakuJokenConvertDataBefore.dtp対象日_to
          );
        } else {
          コンバート結果.dtp対象日_to =
            KensakuJokenConvertDataBefore.dtp対象日_to;
        }
      }

      //チェックボックスコンバート
      コンバート結果.chk作業予定未入力 = this.fnc_obj_CheckBoxConvert(
        KensakuJokenConvertDataBefore.chk作業予定未入力
      );
      コンバート結果.chk作業予定打診中 = this.fnc_obj_CheckBoxConvert(
        KensakuJokenConvertDataBefore.chk作業予定打診中
      );
      コンバート結果.chk作業未完了 = this.fnc_obj_CheckBoxConvert(
        KensakuJokenConvertDataBefore.chk作業未完了
      );
      コンバート結果.chk作業完了確認打診中 = this.fnc_obj_CheckBoxConvert(
        KensakuJokenConvertDataBefore.chk作業完了確認打診中
      );
      コンバート結果.chk作業完了確認済 = this.fnc_obj_CheckBoxConvert(
        KensakuJokenConvertDataBefore.chk作業完了確認済
      );
      コンバート結果.chk作業中止 = this.fnc_obj_CheckBoxConvert(
        KensakuJokenConvertDataBefore.chk作業中止
      );
      コンバート結果.chk添付修正要 = this.fnc_obj_CheckBoxConvert(
        KensakuJokenConvertDataBefore.chk添付修正要
      );

      //ラジオボタン値渡し
      コンバート結果.rgb年月 = KensakuJokenConvertDataBefore.rgb年月;
      コンバート結果.rgb年月日 = KensakuJokenConvertDataBefore.rgb年月日;

      //ログインID
      コンバート結果.loginID = this.KensakuJoken.loginID;
      //その他諸々入れ直し
      コンバート結果.txt物件CD = this.KensakuJoken.txt物件CD;
      コンバート結果.txt契約NO = this.KensakuJoken.txt契約NO;
      コンバート結果.txt契約名 = this.KensakuJoken.txt契約名;
      コンバート結果.txt作業予定 = this.KensakuJoken.txt作業予定;
      コンバート結果.txt営業店CD = this.KensakuJoken.txt営業店CD;

      //フラグ戻し
      // if (
      //   KensakuJokenConvertDataBefore.chk作業予定未入力 == true ||
      //   KensakuJokenConvertDataBefore.chk作業予定打診中 == true ||
      //   KensakuJokenConvertDataBefore.chk作業未完了 == true ||
      //   KensakuJokenConvertDataBefore.chk作業完了確認打診中 == true ||
      //   KensakuJokenConvertDataBefore.chk作業完了確認済 == true ||
      //   KensakuJokenConvertDataBefore.chk作業中止 == true ||
      //   KensakuJokenConvertDataBefore.chk添付修正要 == true
      // ) {
      //   コンバート結果.gamen_flg = "0";
      // }

      //フラグ入れ直し
      コンバート結果.gamen_flg = this.KensakuJoken.gamen_flg;

      return コンバート結果;
    },

    ///////////////////////////////////////////////
    // 日付コンバート
    // DATE型を文字型のYYYY/MMにコンバートする
    ///////////////////////////////////////////////
    fnc_obj_NengetsuConvert(NengetsuConvertBefore) {
      if (NengetsuConvertBefore.length > 0) {
        let letYear = new Object();
        let letMonth = new Object();
        let letNengetsu = new Object();

        letYear = NengetsuConvertBefore.getFullYear();
        letMonth = NengetsuConvertBefore.getMonth() + 1;
        letMonth = ("00" + letMonth).slice(-2);
        letNengetsu = letYear + "/" + letMonth;
      } else {
      }

      return letNengetsu;
    },

    ///////////////////////////////////////////////
    // チェックボックスコンバート
    // Bool型を0, 1にコンバートする
    ///////////////////////////////////////////////
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

    ///////////////////////////////////////////////
    // 検索前ピンク色等データクリア
    ///////////////////////////////////////////////
    fnc_void_clearData() {
      let _this = this;
      _this.tblData = [];
      _this.hitKensu = "0  件";
      _this.isErrPinkSagyotsuki = false;
      _this.isErrPinkSagyodate = false;
      _this.isErrPinkBukken = false;
      _this.isErrPinkEigyou = false;
    },

    ///////////////////////////////////////////////
    // 検索結果取得
    ///////////////////////////////////////////////
    async fnc_obj_getKensakuKekka(KensakuJokenConvertAfter) {
      let kekka = new Object();

      await this.http
        .post(
          "/api/Reafs_T/Sagyo/TD00400_GET_KensakuKekka",
          KensakuJokenConvertAfter,
          "検索しています...."
        )
        .then(function(response) {
          kekka = response;
        });

      if (!kekka.status) {
        this.MsgErr({
          title: "定期作業一覧",
          message: kekka.message
        });
      } else if (kekka.data.length === 0) {
        this.fnc_void_showErrorMsg("E010224");
      } else {
        this.tblData = kekka.data;
        this.hitKensu = kekka.data.length.toLocaleString() + "  件";

        if(this.KensakuJoken.gamen_flg == 2 || this.KensakuJoken.gamen_flg == 3) 
        {
          this.KensakuJoken.dtp対象月_from = kekka.data[0].経理年月
          this.KensakuJoken.dtp対象月_to = kekka.data[0].経理年月
        }
      }
    },

    ///////////////////////////////////////////////
    // リンククリック時の画面遷移またはモーダル表示処理
    ///////////////////////////////////////////////
    fnc_lnkKeiyakuMeishoOnClick(row) {
      // 定期見積一覧へ遷移
      this.$router.push({
        name: "TD00302",
        params: {
          keiyakuNO: row.tblKeiyakuNO,
          rirekiNO: row.rireki_no,
          hansu: row.hansu
        }
      });
    },

    fnc_lnkYoteiShinseiOnClick(row) {
      // 定期作業予定入力を表示
      this.KeyDataToTS00401.keiyaku_no = row.tblKeiyakuNO; //契約NO
      this.KeyDataToTS00401.rireki_no = row.rireki_no; //履歴NO
      this.KeyDataToTS00401.meisai_no = row.meisai_no; //明細NO
      this.KeyDataToTS00401.keiyaku_nengetu = row.keiyaku_nengetu; //契約年月
      this.KeyDataToTS00401.nengetu_meisai = row.nengetu_meisai; //契約明細NO
      this.KeyDataToTS00401.kaisya_cd = row.kaisya_cd;
      this.KeyDataToTS00401.sagyo_date_hikaku = row.tblSagyotsuki; //実施予定年月
      this.KeyDataToTS00401.kaihan_Flg = row.改版チェック; // 改版チェック

      this.showModalPrefectureWindow401(this.KeyDataToTS00401);
      this.showTS00401 = true; //ダイアログ表示
    },

    fnc_lnkKanryoHokokuOnClick(row) {
      // 定期作業完了入力を表示
      this.KeyDataToTS00402.keiyaku_no = row.tblKeiyakuNO; //契約NO
      this.KeyDataToTS00402.rireki_no = row.rireki_no; //履歴NO
      this.KeyDataToTS00402.meisai_no = row.meisai_no; //明細NO
      this.KeyDataToTS00402.keiyaku_nengetu = row.keiyaku_nengetu; //契約年月
      this.KeyDataToTS00402.nengetu_meisai = row.nengetu_meisai; //契約明細NO
      this.KeyDataToTS00402.sagyo_date_hikaku = row.tblSagyotsuki; //実施予定年月
      this.KeyDataToTS00402.改版チェック = row.改版チェック

      this.showModalPrefectureWindow402(this.KeyDataToTS00402);
      this.showTS00402 = true; //ダイアログ表示
    },

    // fnc_clickLink(row, column, rowIndex) {
    //   let hikisuu = new Object();

    //   switch (column.field) {
    //     //契約名称 -> 定期見積入力へ遷移
    //     case "tblKeiyakuMeisho":
    //       this.hikisuu = row;
    //       this.$router.push({
    //         name: "TD00302",
    //         params: {
    //           keiyakuNO: this.hikisuu.tblKeiyakuNO,
    //           rirekiNO: this.hikisuu.rireki_no,
    //           hansu: this.hikisuu.hansu
    //         }
    //       });
    //       break;

    //     //予約申請 -> 定期作業報告入力をポップアップ
    //     case "tblYoteiShinsei":
    //       this.hikisuu = row;
    //       this.KeyDataToTS00401.keiyaku_no = this.hikisuu.tblKeiyakuNO; //契約NO
    //       this.KeyDataToTS00401.rireki_no = this.hikisuu.rireki_no; //履歴NO
    //       this.KeyDataToTS00401.meisai_no = this.hikisuu.meisai_no; //明細NO
    //       this.KeyDataToTS00401.keiyaku_nengetu = this.hikisuu.keiyaku_nengetu; //契約年月
    //       this.KeyDataToTS00401.nengetu_meisai = this.hikisuu.nengetu_meisai; //契約明細NO
    //       this.KeyDataToTS00401.kaisya_cd = this.hikisuu.kaisya_cd;
    //       this.KeyDataToTS00401.sagyo_date_hikaku = this.hikisuu.tblSagyotsuki; //実施予定年月

    //       this.showModalPrefectureWindow401(this.KeyDataToTS00401);
    //       this.showTS00401 = true; //ダイアログ表示
    //       break;

    //     // 完了報告 -> 定期作業報告入力をポップアップ
    //     case "tblKanryoHokoku":
    //       this.hikisuu = row;

    //       this.KeyDataToTS00402.keiyaku_no = this.hikisuu.tblKeiyakuNO; //契約NO
    //       this.KeyDataToTS00402.rireki_no = this.hikisuu.rireki_no; //履歴NO
    //       this.KeyDataToTS00402.meisai_no = this.hikisuu.meisai_no; //明細NO
    //       this.KeyDataToTS00402.keiyaku_nengetu = this.hikisuu.keiyaku_nengetu; //契約年月
    //       this.KeyDataToTS00402.nengetu_meisai = this.hikisuu.nengetu_meisai; //契約明細NO
    //       this.KeyDataToTS00402.sagyo_date_hikaku = this.hikisuu.tblSagyotsuki; //実施予定年月

    //       this.showModalPrefectureWindow402(this.KeyDataToTS00402);
    //       this.showTS00402 = true; //ダイアログ表示
    //       break;

    //     // 添付修正 -> 定期作業報告入力をポップアップ
    //     case "tblTenpuShusei":
    //       this.hikisuu = row;

    //       this.KeyDataToTS00402.keiyaku_no = this.hikisuu.tblKeiyakuNO; //契約NO
    //       this.KeyDataToTS00402.rireki_no = this.hikisuu.rireki_no; //履歴NO
    //       this.KeyDataToTS00402.meisai_no = this.hikisuu.meisai_no; //明細NO
    //       this.KeyDataToTS00402.keiyaku_nengetu = this.hikisuu.keiyaku_nengetu; //契約年月
    //       this.KeyDataToTS00402.nengetu_meisai = this.hikisuu.nengetu_meisai; //契約明細NO
    //       this.KeyDataToTS00402.sagyo_date_hikaku = this.hikisuu.tblSagyotsuki; //実施予定年月

    //       this.showModalPrefectureWindow402(this.KeyDataToTS00402);
    //       this.showTS00402 = true;
    //       break;

    //     default:
    //   }
    // },

    ///////////////////////////////////////////////
    // 検索前エラーチェック
    ///////////////////////////////////////////////
    fnc_obj_kensakumaeErrorChk(KensakuJoken, Bukkenjyusyo) {
      //作業月が空の場合
      if (
        (KensakuJoken.dtp対象月_from == "" ||
          KensakuJoken.dtp対象月_from == null ||
          KensakuJoken.dtp対象月_to == "" ||
          KensakuJoken.dtp対象月_to == null) &&
        (KensakuJoken.dtp対象日_from == "" ||
          KensakuJoken.dtp対象日_from == null ||
          KensakuJoken.dtp対象日_to == "" ||
          KensakuJoken.dtp対象日_to == null)
      ) {
        this.fnc_void_showErrorMsg("E040766"); // 請求書月分を入力して下さい。
        this.isErrPinkSagyotsuki = true; //背景色ピンク
        this.isErrPinkSagyodate = true; //背景色ピンク
        this.$refs.ref作業月from.focus();
        return false;
      } else {
        let tuki_from = KensakuJoken.dtp対象月_from;
        let tuki_to = KensakuJoken.dtp対象月_to;
        let day_from = KensakuJoken.dtp対象日_from;
        let day_after = KensakuJoken.dtp対象日_to;
        if (tuki_from > tuki_to) {
          this.fnc_void_showErrorMsg("E040279"); // 請求書月分が逆転しています。
          this.isErrPinkSagyotsuki = true; //背景色ピンク
          this.$refs.ref作業月from.focus();
          return false;
        } else if (day_from > day_after) {
          this.fnc_void_showErrorMsg("E040279"); // 請求書月分が逆転しています。
          this.isErrPinkSagyodate = true; //背景色ピンク
          this.$refs.ref対象日from.focus();
          return false;
        } else if (
          KensakuJoken.chk作業予定未入力 == false &&
          KensakuJoken.chk作業予定打診中 == false &&
          KensakuJoken.chk作業未完了 == false &&
          KensakuJoken.chk作業完了確認打診中 == false &&
          KensakuJoken.chk作業完了確認済 == false &&
          KensakuJoken.chk作業中止 == false &&
          KensakuJoken.chk添付修正要 == false
        ) {
          this.fnc_void_showErrorMsg("E040472"); // チェックなし
          this.$refs.ref作業予定未入力.focus();
          return false;
        } else if (KensakuJoken.txt物件CD !== "") {
          if (this.Bukkenjyusyo.物件住所NM === "") {
            this.fnc_void_showErrorMsg("E040481"); // 物件が存在しません。
            this.isErrPinkBukken = true; //背景色ピンク
            this.$refs.ref物件.focus();
            return false;
          }
        } else if (KensakuJoken.txt営業店CD !== "") {
          if (this.eigyouNm === "") {
            this.fnc_void_showErrorMsg("E040009"); // 入力された営業店が存在しません。
            this.isErrPinkEigyou = true; //背景色ピンク
            this.$refs.ref営業店.focus();
            return false;
          }
          return true;
        }
      }
    },

    ///////////////////////////////////////////////
    // グリッド背景色変化
    ///////////////////////////////////////////////
    rowClassFunction(data) {
      try {
        if (data && data.hasOwnProperty("row")) {
          let sintyoku = new Object();
          sintyoku = data.row.sintyoku_CD;
          if (sintyoku > "510" && sintyoku < "520") {
            //判断条件
            return "tableRowClassName";
          }
        } else {
          return "";
        }
      } catch (error) {
        return "";
      }
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
          title: "定期作業一覧",
          message: response.data
        });
      });
    },

    ///////////////////////////////////////////////
    // 物件名取得
    ///////////////////////////////////////////////
    reafsmodalBack_searchBukken(obj) {
      console.log(obj.txtCd);
      if (obj.lblNm != "") {
        this.KensakuJoken.txt物件CD = obj.txtCd;
        this.isErrPinkBukken = false;
        this.bukkenNm = obj.lblNm;
        this.Bukkenjyusyo.物件住所CD =
          obj.row.郵便番号.substr(0, 3) + "-" + obj.row.郵便番号.substr(3);
        this.Bukkenjyusyo.物件住所NM = obj.row.住所;
      } else if (this.KensakuJoken.txt物件CD != "") {
        this.KensakuJoken.txt物件CD = this.KensakuJoken.txt物件CD;
        this.isErrPinkBukken = true;
        this.bukkenNm = "";
        this.Bukkenjyusyo.物件住所CD = "";
        this.Bukkenjyusyo.物件住所NM = "";
        this.$refs.ref物件.focus();
      } else {
        this.isErrPinkBukken = false;
      }
    },

    ///////////////////////////////////////////////
    // 物件名フォーカスアウト時
    ///////////////////////////////////////////////
    onblur_bukken() {
      if (this.KensakuJoken.txt物件CD == "") {
        this.isErrPinkBukken = false;
        this.bukkenNm = "";
        this.Bukkenjyusyo.物件住所CD = "";
        this.Bukkenjyusyo.物件住所NM = "";
      } else if (this.bukkenNm == "") {
        this.Bukkenjyusyo.物件住所CD = "";
        this.Bukkenjyusyo.物件住所NM = "";
      }
    },

    ///////////////////////////////////////////////
    // 営業店フォーカスアウト時
    ///////////////////////////////////////////////
    onblur_eigyou() {
      if (this.KensakuJoken.txt営業店CD == "") {
        this.isErrPinkEigyou = false;
        this.eigyouNm = "";
      }
    },

    ///////////////////////////////////////////////
    // 営業店データ取得
    ///////////////////////////////////////////////
    reafsmodalBack(obj) {
      if (obj.lblNm != "" && obj.lblNm != null) {
        this.KensakuJoken.txt営業店CD = obj.txtCd;
        this.isErrPinkEigyou = false;
        this.eigyouNm = obj.lblNm;
      } else if (this.KensakuJoken.txt営業店CD != "") {
        this.KensakuJoken.txt営業店CD = this.KensakuJoken.txt営業店CD;
        this.isErrPinkEigyou = true;
        this.eigyouNm = "";
        this.$refs.ref営業店.focus();
      } else {
        this.KensakuJoken.txt営業店CD = obj.txtCd;
        this.isErrPinkEigyou = false;
      }
    },

    ///////////////////////////////////////////////
    // 検索条件ボタン
    //int = 0:予定入力ボタン
    //int = 1:完了入力ボタン
    //int = 2:条件リセットボタン & 初期値
    ///////////////////////////////////////////////
    fnc_click_Zyouken(int) {
      if (int == 0) {
        this.KensakuJoken.chk作業予定未入力 = true;
        this.KensakuJoken.chk作業予定打診中 = false;
        this.KensakuJoken.chk作業未完了 = false;
        this.KensakuJoken.chk作業未完了 = false;
        this.KensakuJoken.chk作業完了確認打診中 = false;
        this.KensakuJoken.chk作業完了確認済 = false;
        this.KensakuJoken.chk作業中止 = false;
        this.KensakuJoken.chk添付修正要 = false;
      } else if (int == 1) {
        this.KensakuJoken.chk作業予定未入力 = false;
        this.KensakuJoken.chk作業予定打診中 = false;
        this.KensakuJoken.chk作業未完了 = true;
        this.KensakuJoken.chk作業完了確認打診中 = false;
        this.KensakuJoken.chk作業完了確認済 = false;
        this.KensakuJoken.chk作業中止 = false;
        this.KensakuJoken.chk添付修正要 = false;
      } else if (int == 2) {
        console.log("初期化！");
        //今月用
        let now_before;
        now_before = new Date();
        now_before.setMonth(now_before.getMonth());

        let now_before_year = now_before.getFullYear();
        now_before_year = now_before_year.toString();
        var now_before_mounth = now_before.getMonth() + 1;
        now_before_mounth = ("00" + now_before_mounth).slice(-2);
        this.KensakuJoken.dtp対象月_from =
          now_before_year + "/" + now_before_mounth;

        //翌月用
        let now_after = new Date();
        now_after.setMonth(now_after.getMonth() + 2);

        let now_after_year = now_after.getFullYear();
        now_after_year = now_after_year.toString();
        var now_after_mounth = now_after.getMonth();
        now_after_mounth = ("00" + now_after_mounth).slice(-2);
        this.KensakuJoken.dtp対象月_to =
          now_after_year + "/" + now_after_mounth;

        this.KensakuJoken.gamen_flg = 0;

        this.KensakuJoken.dtp対象日_from = "";
        this.KensakuJoken.dtp対象日_to = "";

        this.KensakuJoken.rgb年月 = "作業月";
        this.KensakuJoken.rgb年月日 = "作業予定日";

        this.KensakuJoken.chk作業予定未入力 = true;
        this.KensakuJoken.chk作業予定打診中 = true;
        this.KensakuJoken.chk作業未完了 = true;
        this.KensakuJoken.chk作業完了確認打診中 = true;
        this.KensakuJoken.chk作業完了確認済 = true;
        this.KensakuJoken.chk作業中止 = true;
        this.KensakuJoken.chk添付修正要 = true;

        this.KensakuJoken.txt物件CD = "";
        this.KensakuJoken.txt営業店CD = "";
        this.KensakuJoken.txt契約NO = "";
        this.KensakuJoken.txt契約名 = "";
        this.KensakuJoken.txt作業予定 = "";

        this.isErrPinkSagyotsuki = false;
        this.isErrPinkSagyodate = false;
        this.isErrPinkBukken = false;
        this.isErrPinkEigyou = false;
      }
    },

    ///////////////////////////////////////////////
    // ダイアログ出力時処理(401)
    ///////////////////////////////////////////////
    async showModalPrefectureWindow401(letval) {
      let _this = this;

      _this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00401_GET_KensakuKekka",
          letval,
          "表示しています...."
        )
        .then(function(response1) {
          console.log("OK000110");
          if (!response1.status) {
            _this.MsgErr({
              title: "作業予定入力",
              message: response1.message
            });
          } else {
            _this.KeyDataToTS00401.keiyakukanri_no =
              response1.data[0].keiyakukanri_no; //契約管理番号
            _this.KeyDataToTS00401.bukken_name = response1.data[0].bukken_name; //物件名
            _this.KeyDataToTS00401.eigyou_name = response1.data[0].eigyou_name; //営業店
            _this.KeyDataToTS00401.keiyaku_name =
              response1.data[0].keiyaku_name; //契約名称
            _this.KeyDataToTS00401.sagyou_c_dai =
              response1.data[0].sagyou_c_dai; //大区分
            _this.KeyDataToTS00401.sagyou_c_tyuu =
              response1.data[0].sagyou_c_tyuu; //中区分
            _this.KeyDataToTS00401.sagyou_c_syou =
              response1.data[0].sagyou_c_syou; //小区分
            _this.KeyDataToTS00401.sagyou_name = response1.data[0].sagyou_name; //作業名称
            _this.KeyDataToTS00401.sagyou_tuki_kaisuu =
              response1.data[0].sagyo_date +
              "　" +
              response1.data[0].sagyou_tuki_kaisuu +
              "回目報告"; //作業回数
            //日付・時間がNullの場合は無表記になるようにする。
            if (response1.data[0].sagyou_kaisi_date == null) {
              response1.data[0].sagyou_kaisi_date = "";
            }
            if (response1.data[0].sagyou_kaisi_time == null) {
              response1.data[0].sagyou_kaisi_time = "";
            }
            if (response1.data[0].sagyou_syuuryou_date == null) {
              response1.data[0].sagyou_syuuryou_date = "";
            }
            if (response1.data[0].sagyou_syuuryou_time == null) {
              response1.data[0].sagyou_syuuryou_time = "";
            }
            _this.KeyDataToTS00401.yoteikaisi_date =
              response1.data[0].sagyou_kaisi_date; //作業開始日
            _this.KeyDataToTS00401.sagyoyotei_kaisi_time =
              response1.data[0].sagyou_kaisi_time; //作業開始時間
            _this.KeyDataToTS00401.yoteisyuuryou_date =
              response1.data[0].sagyou_syuuryou_date; //作業終了日
            _this.KeyDataToTS00401.sagyoyotei_syuuryou_time =
              response1.data[0].sagyou_syuuryou_time; //作業終了時間
            _this.KeyDataToTS00401.sagyou_tantou_name =
              response1.data[0].sagyou_tantou_name; //作業担当者
            _this.KeyDataToTS00401.sagyoutantou_tel =
              response1.data[0].sagyoutantou_tel; //作業担当者連絡先
            _this.KeyDataToTS00401.sagyou_yotei_bikou =
              response1.data[0].sagyou_yotei_bikou; //作業予定内容
            _this.KeyDataToTS00401.sasimodori_ziyuu =
              response1.data[0].sasimodori_ziyuu; //差戻事由
            _this.KeyDataToTS00401.check_version =
              response1.data[0].check_version; //バージョン　比較用
            _this.KeyDataToTS00401.sagyo_date_hikaku =
              response1.data[0].sagyo_date_hikaku; //実施予定年月
            _this.KeyDataToTS00401.kakutei_flag =
              response1.data[0].kakutei_flag; //経理年月
            _this.KeyDataToTS00401.sintyoku_CD = response1.data[0].sintyoku_CD; //進捗コード
            _this.KeyDataToTS00401.kouziirai_no =
              response1.data[0].kouziirai_no; //工事依頼NO
            
            // 画面活性
            if (_this.KeyDataToTS00401.sintyoku_CD < "530") {
              _this.touroku_b1 = false;
              _this.koumoku_t1 = false;
            } else if (_this.KeyDataToTS00401.sintyoku_CD >= "530") {
              _this.touroku_b1 = true;
              _this.koumoku_t1 = true;
            }

            // 改版中の場合、何も触れないように
            if(_this.KeyDataToTS00401.kaihan_Flg == "True")
            {
              console.log("改版中！_TS00401");
              _this.touroku_b1 = true;
              _this.koumoku_t1 = true;
              _this.改版チェック = true;
            } else {
              _this.改版チェック = false;
            }

            _this.KeyDataToTS00401.sendMailJimusyoCode =
              response1.data[0].sendMailJimusyoCode;
            _this.KeyDataToTS00401.sendMailEigyosyoCode =
              response1.data[0].sendMailEigyosyoCode;
            _this.KeyDataToTS00401.sendMailBuCode =
              response1.data[0].sendMailBuCode;
            _this.KeyDataToTS00401.sendMailKaCode =
              response1.data[0].sendMailKaCode;
            _this.KeyDataToTS00401.sendMailKakariCode =
              response1.data[0].sendMailKakariCode;
            //チェック用
            _this.KeyDataToTS00401.check_yoteikaisi_date =
              response1.data[0].sagyou_kaisi_date; //作業開始日
            _this.KeyDataToTS00401.check_sagyoyotei_kaisi_time =
              response1.data[0].sagyou_kaisi_time; //作業開始時間
            _this.KeyDataToTS00401.check_yoteisyuuryou_date =
              response1.data[0].sagyou_syuuryou_date; //作業終了日
            _this.KeyDataToTS00401.check_sagyoyotei_syuuryou_time =
              response1.data[0].sagyou_syuuryou_time; //作業終了時間
            _this.KeyDataToTS00401.check_sagyou_tantou_name =
              response1.data[0].sagyou_tantou_name; //作業担当者
            _this.KeyDataToTS00401.check_sagyoutantou_tel =
              response1.data[0].sagyoutantou_tel; //作業担当者連絡先
            _this.KeyDataToTS00401.check_sagyou_yotei_bikou =
              response1.data[0].sagyou_yotei_bikou; //作業予定内容
          }
        });

      await _this.http
        .post(
          "/api/Reafs_T/Sagyo/TS00401_GET_KensakuKekka_g",
          letval,
          "表示しています...."
        )
        .then(function(response_grid) {
          if (!response_grid.status) {
            _this.MsgErr({
              title: "作業予定入力",
              message: response_grid.message
            });
          } else {
            let kariire = response_grid.data;
            _this.KeyDataToTS00401.TS00401_grid = [];
            for (let nakami in kariire) {
              if (
                kariire[nakami].taisyougai == 1 &&
                _this.touroku_b1 == false &&
                _this.koumoku_t1 == false
              ) {
                kariire[nakami].taisyougai = true;
                kariire[nakami].readonly = 0;
              } else if (
                kariire[nakami].taisyougai == 1 &&
                _this.touroku_b1 == true &&
                _this.koumoku_t1 == true
              ) {
                kariire[nakami].taisyougai = true;
                kariire[nakami].readonly = 1;
              } else if (kariire[nakami].taisyougai == 0) {
                kariire[nakami].taisyougai = false;
                kariire[nakami].readonly = 1;
              }
              _this.KeyDataToTS00401.TS00401_grid.push(kariire[nakami]);
            }
            //チェック用
            _this.KeyDataToTS00401.check_grid_date = response_grid.data;
          }
        });
    },

    ///////////////////////////////////////////////
    // ダイアログ出力時処理(402)
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

          // 改版中の場合、何も触れないように
          if (_this.KeyDataToTS00402.改版チェック == "True")
          {
            console.log("改版中！_TS00402");
            _this.左側 = true;
            _this.右側金額 = true;
            _this.削除ボタン = true;
            _this.登録ボタン = true;
            _this.touroku = true;
            _this.改版チェック = true;
          } else {
            _this.改版チェック = false;
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
    // ダイアログ閉じる時処理
    ///////////////////////////////////////////////
    fnc_TS00401Close(e) {
      console.log("TS00401を閉じました");
      this.fnc_void_clickBtnKensaku();
    },

    fnc_TS00402Close(e) {
      console.log("TS00402を閉じました");
      this.fnc_void_clickBtnKensaku();
    },

    async fnc_void_search_MailData(
      契約No,
      履歴No,
      明細No,
      契約年月,
      契約年月明細No
    ) {
      console.log("メール起動検索処理実行");

      let kekka = new Object();
      await this.http
        .post(
          "/api/Reafs_T/Sagyo/TD00400_GET_MailData",
          {
            契約No: 契約No,
            履歴No: 履歴No,
            明細No: 明細No,
            契約年月: 契約年月,
            契約年月明細No: 契約年月明細No
          },
          "検索しています...."
        )
        .then(function(response) {
          kekka = response;
        });

      if (!kekka.status) {
        this.MsgErr({
          title: "定期作業一覧",
          message: kekka.message
        });
      } else if (kekka.data.length === 0) {
        this.fnc_void_showErrorMsg("E010224");
      } else {
        this.tblData = kekka.data;
        this.hitKensu = kekka.data.length.toLocaleString() + "  件";
      }
    },

    down2first(event) {
      if (event.keyCode === 9) {
        this.$nextTick(function() {
          this.$refs.ref作業月from.focus();
          event.preventDefault();
        });
      }
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
.home-el-footer {
  margin: 7% 10% 0 90%;
  float: left;
}
.checkbox_left {
  float: left;
}
</style>

<style>
.SagyoJokyoIchiran .text-left {
  text-align: left !important;
}
.SagyoJokyoIchiran
  .ve-table
  .ve-table-container
  table.ve-table-content
  thead.ve-table-header
  .ve-table-header-tr
  .ve-table-header-th.ve-table-fixed-left {
  z-index: 10 !important;
}
.SagyoJokyoIchiran .tableRowClassName {
  background-color: #ff9999 !important;
}
</style>

<style scoped>
.nengetu >>> .el-date-editor {
  height: 30px;
  width: 135px;
  margin: 0;
  line-height: 30px;
  border-radius: 1px;
  padding-right: 5px;
}
/* 作業月エラーピンク */
.pinkcolor_month >>> .el-input__inner {
  background-color: lightpink !important;
}
/* 作業日エラーピンク */
.pinkcolor_date >>> .el-input__inner {
  background-color: lightpink !important;
}
.pinkcolor_bukken >>> .el-input__inner {
  background-color: lightpink !important;
}
.pinkcolor_eigyou >>> .el-input__inner {
  background-color: lightpink !important;
}
.append-background {
  display: inline-flex;
  background-color: rgb(211, 211, 211);
  /*width: 500px;*/
}
.grid_height {
  height: 500px;
}
.grid-content {
  height: 500px;
}
</style>
