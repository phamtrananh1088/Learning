<template>
  <div class="home-contianer SagyojoukyoIchiran">
    <!-- 検索部 -->

    <div>
      <el-row>
        <!--1行目-->
        <el-row>
          <!--1行 1列目-->
          <el-form
            label-width="90px"
            :model="KensakuJoken"
            :rules="rules"
            class="sample-form-class"
          >
            <el-col :span="7">
              <div class="nengetu">
                <el-form-item label="対象月指定" prop="dtpNengetsudo">
                  <el-date-picker
                    :required="true"
                    type="month"
                    format="yyyy/MM"
                    value-format="yyyy/MM"
                    :clearable="false"
                    :width="'80px'"
                    v-model="KensakuJoken.dtpNengetsudo_from"
                    :class="{ pinkcolor_el_range_input: isErrPinkNengetsudo }"
                    index="1"
                    ref="ref年月度"
                  ></el-date-picker>
                  ~
                  <el-date-picker
                    :required="true"
                    type="month"
                    format="yyyy/MM"
                    value-format="yyyy/MM"
                    :clearable="false"
                    :width="'80px'"
                    v-model="KensakuJoken.dtpNengetsudo_to"
                    :class="{ pinkcolor_el_range_input: isErrPinkNengetsudo }"
                    index="1"
                  ></el-date-picker>
                </el-form-item>
              </div>
            </el-col>

            <!--1行 2列目-->
            <el-col :span="8">
              <el-form-item label-width="0px">
                <el-radio-group v-model="KensakuJoken.rdoYearAndMonth">
                  <el-radio label="作業月">作業月</el-radio>
                  <el-radio label="作業予定日">作業予定日</el-radio>
                  <el-radio label="作業完了日">作業完了日</el-radio>
                  <el-radio label="完了確認日">完了確認日</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <!--1行 3列目-->
            <el-col :span="7">
              <el-form-item label="工事区分">
                <el-radio-group v-model="KensakuJoken.rdoKojiKubun">
                  <el-radio label="全て"></el-radio>
                  <el-radio label="定期委託"></el-radio>
                  <el-radio label="スポット"></el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <!--1行 4列目-->
            <el-col :span="2">
              <el-button
                @click="fnc_void_clickBtnKensaku()"
                style="width: 100px"
                >検索</el-button
              >
            </el-col>
          </el-form>
        </el-row>

        <!--2行目-->
        <el-row>
          <!--2行 1列目-->
          <el-form
            label-width="90px"
            :model="KensakuJoken"
            :rules="rules"
            class="sample-form-class"
          >
            <el-col :span="7">
              <div class="nengetu">
                <el-form-item label="対象日指定" prop="dtpNengetsudo">
                  <el-date-picker
                    :required="true"
                    type="date"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    :clearable="false"
                    v-model="KensakuJoken.dtpStartDate"
                    class="date-picker date-range"
                    :class="{ pinkcolor_el_range_input: isErrPinkNengetsudo }"
                  />
                  ~
                  <el-date-picker
                    :required="true"
                    type="date"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    :clearable="false"
                    v-model="KensakuJoken.dtpEndDate"
                    class="date-picker date-range"
                    :class="{ pinkcolor_el_range_input: isErrPinkNengetsudo }"
                  ></el-date-picker>
                </el-form-item>
              </div>
            </el-col>

            <!--1行 2列目-->
            <el-col :span="8">
              <el-form-item label-width="0px">
                <el-radio-group
                  v-model="KensakuJoken.rdoTargetDate"
                  style="margin-left: 86px"
                >
                  <el-radio label="作業予定日">作業予定日</el-radio>
                  <el-radio label="作業完了日">作業完了日</el-radio>
                  <el-radio label="完了確認日">完了確認日</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <!--1行 3列目-->
            <el-col :span="7">
              <span style="font-size: 14px">ステータス指定:</span>
              <el-button
                @click="fnc_void_clickBtnYoteiKakunin()"
                style="width: 100px"
                >予定確認</el-button
              >
              <el-button
                @click="fnc_void_clickBtnKanryoKakunin()"
                style="width: 100px"
                >完了確認</el-button
              >
            </el-col>

            <!--1行 4列目-->
            <el-col :span="2">
              <el-button @click="fnc_void_clickBtnReset(0)" style="width: 100px"
                >検索条件<br />リセット</el-button
              >
            </el-col>
          </el-form>
        </el-row>

        <el-row>
          <el-form
            label-width="90px"
            :model="KensakuJoken"
            :rules="rules"
            :hide-required-asterrisk="true"
            @submit.native.prevent
          >
            <el-col>
              <el-form-item label="ステータス">
                <el-checkbox
                  label="作業予定未入力/作業予定差戻"
                  v-model="KensakuJoken.chkSagyoYoteiMinyuryoku"
                ></el-checkbox>
                <el-checkbox
                  label="作業予定未確認"
                  v-model="KensakuJoken.chkSagyoYoteiMikakunin"
                ></el-checkbox>
                <el-checkbox
                  label="作業未完了/作業完了差戻"
                  v-model="KensakuJoken.chkSagyoMikanryo"
                ></el-checkbox
                ><el-checkbox
                  label="作業完了未確認"
                  v-model="KensakuJoken.chkSagyoKanryoMikakunin"
                ></el-checkbox>
                <el-checkbox
                  label="作業完了確認済"
                  v-model="KensakuJoken.chkSagyoKanryoKakuninzumi"
                ></el-checkbox>
                <el-checkbox
                  label="作業中止"
                  v-model="KensakuJoken.chkSagyoChusi"
                ></el-checkbox>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>

        <el-row>
          <el-form label-width="90px">
            <!--3行 1列目-->
            <el-col :span="8">
              <el-form-item label="物件名">
                <opensearch
                  v-model="KensakuJoken.txtBukkenmeiCode"
                  maxlength="8"
                  :disabled="false"
                  :appendlabelText="KensakuJoken.txtBukkenmei"
                  :append="'append'"
                  :path="'reafsmodalSearchBukken'"
                  @backData="reafsmodalBackBukkenmei"
                  :width="'135px'"
                  :class="{ pinkcolor_bukken: isErrPinkBukken }"
                  ref="ref物件"
                >
                </opensearch>
              </el-form-item>
            </el-col>

            <!--3行 2列目-->
            <el-col :span="5">
              <el-form-item
                label="物件区分"
                prop="bukkenKbn"
                class="is-readonly"
              >
                <reafsinputtext
                  v-model="KensakuJoken.txtBukkenkubun"
                  maxlength="20"
                  disabled
                  :width="'150px'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>

            <!--3行 3列目-->
            <el-col :span="2">
              <el-form-item
                label="物件住所"
                prop="bukkenAddress"
                class="is-readonly"
              >
                <div style="display: flex">
                  <reafsinputtext
                    v-model="KensakuJoken.txtBukkenJushoCd"
                    maxlength="8"
                    disabled
                    :width="'110px'"
                  >
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="KensakuJoken.txtBukkenJushoNm"
                    maxlength="100"
                    disabled
                    :width="'284px'"
                  >
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>

        <!--2行目-->
        <el-row>
          <el-form
            label-width="90px"
            :model="KensakuJoken"
            :rules="rules"
            :hide-required-asterisk="true"
            @submit.native.prevent
          >
            <!--2行 1列目-->
            <el-col :span="8">
              <div style="display: flex">
                <el-form-item label="営業店" prop="eigyoten">
                  <opensearch
                    width="100px"
                    :disabled="false"
                    :path="'reafsmodalSearchMise'"
                    maxlength="7"
                    :inputClass="'moblie-input-append'"
                    :class="{ pinkcolor_eigyou: isErrPinkEigyoten }"
                    :required="false"
                    v-model="KensakuJoken.txtEigyotenCode"
                    :appendlabelText="KensakuJoken.txtEigyoten"
                    @backData="reafsmodalBackEigyoten"
                    ref="ref営業店"
                    :appendlabelWidth="'215px'"
                    :modeTextBox="2"
                  ></opensearch>
                </el-form-item>
              </div>
            </el-col>

            <!--2行 2列目-->
            <el-col :span="15">
              <el-form-item label="手配業者" prop="txtTehaiGyoshaCode">
                <div style="display: flex">
                  <reafsinputtext
                    maxlength="6"
                    v-model="KensakuJoken.txtTehaiGyoshaCode"
                    ref="reftxtTehaiGyoshaCode"
                    width="85px"
                    @blur="getTextBox業者名()"
                  >
                  </reafsinputtext>
                  <reafsinputtext
                    maxlength="3"
                    v-model="KensakuJoken.txtTehaiGyoshaCodeEdaban"
                    ref="ref取引先"
                    width="60px"
                    @blur="getTextBox業者名()"
                  >
                  </reafsinputtext>
                  <opensearch
                    :append="'append'"
                    :appendlabelText="KensakuJoken.txtTehaiGyoshaName"
                    :appendlabelWidth="'533px'"
                    :path="'reafsmodalSearchTokuisaki'"
                    :subParamsObj="subParamsObj"
                    @backData="backDataTorihikisaki"
                    inputClass="button-multi-code"
                  >
                  </opensearch>
                </div>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>

        <!-- 3行目 -->
        <el-row>
          <el-form
            label-width="90px"
            :model="KensakuJoken"
            :rules="rules"
            :hide-required-asterisk="true"
            @submit.native.prevent
          >
            <!--2行 1列目-->
            <el-col :span="8">
              <el-form-item label="大区分" prop="大区分">
                <el-select
                  v-model="KensakuJoken.ddlDaikbn"
                  ref="ref大区分"
                  style="width: 84%"
                >
                  <el-option
                    v-for="(item, index) in 大区分List"
                    :key="index + '大区分'"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <!--2行 2列目-->
            <el-col :span="8">
              <el-form-item label="中区分" prop="中区分">
                <el-select
                  v-model="KensakuJoken.ddlChukbn"
                  ref="ref大区分"
                  style="width: 84%"
                >
                  <el-option
                    v-for="(item, index) in 中区分List"
                    :key="index + '中区分'"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="小区分" prop="小区分">
                <el-select
                  v-model="KensakuJoken.ddlSyokbn"
                  ref="ref大区分"
                  style="width: 84%"
                >
                  <el-option
                    v-for="(item, index) in 小区分List"
                    :key="index + '小区分'"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>

        <el-row>
          <el-form
            label-width="90px"
            :model="KensakuJoken"
            :rules="rules"
            :hide-required-asterisk="true"
            @submit.native.prevent
          >
            <!--2行 1列目-->
            <el-col :span="8">
              <el-form-item label="作業名称" prop="txtSagyoName">
                <reafsinputtext
                  v-model="KensakuJoken.txtSagyoName"
                  maxlength="200"
                  :width="'305px'"
                />
              </el-form-item>
            </el-col>

            <!--2行 2列目-->
            <el-col :span="8">
              <el-form-item label="備考" prop="txtBiko">
                <reafsinputtext
                  v-model="KensakuJoken.txtBiko"
                  maxlength="200"
                  :width="'305px'"
                />
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>
      </el-row>
    </div>

    <!-- 明細部 -->
    <!-- 検索ヒット件数 -->
    <div class="cls-hitkensu_align_right">{{ hitKensu }}</div>

    <!-- 明細テーブル -->
    <div>
      <el-row :gutter="20">
        <el-col :span="24">
          <div class="grid-content bg-purple-light">
            <!-- <reafstable
              :single="false"
              :rowKey="rowKey"
              :tableData="tblData"
              :columns="tblColumns"
              :max-height="650"
              :height="650"
              :columnIndex="false"
              :columnChkbox="false"
              :linkView="fnc_clickLink"
              :key="Math.random()"
              v-bind:tabindex="TAB_IDX_TABLE"
            >
            </reafstable> -->
            <reafstable
                ref="table"
                :tableData="tblData"
                :columns="tblColumns"
                :maxHeight="500"
                :resizeAble="true"
                :cellStyleOption="cellStyleOption"
                rowKeyFieldName="key"
                :sortOption="sortOption"
                :virtualScrollOption="virtualScrollOption"
              >
              </reafstable>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 戻るボタン -->
    <div class="footer">
      <el-row :gutter="20">
        <el-col :span="2" style="float: right">
          <el-button @click="onBack">戻る</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 作業予定明細モーダル -->
    <modalWS00411
      :dialogVisible.sync="showWS00411"
      :WS00411_privateData.sync="KeyDataToWS00411"
      @closePopupEvent="fnc_WS00411Close"
    ></modalWS00411>

    <!-- 作業完了報告モーダル -->
    <modalWS00412
      :dialogVisible.sync="showWS00412"
      :WS00412_privateData.sync="KeyDataToWS00412"
      :tblData2.sync="tblData2"
      @closePopupEvent="fnc_WS00412Close"
    ></modalWS00412>
  </div>
</template>

<script>
import codingVue from "../../../redirect/coding.vue";
//import reafstable from "@/components/table/Table.vue";
import reafstable from "@/components/table/Grid.vue";
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnaturalnumber from "@/components/basic/ReafsControl/ReafsInputNaturalNumber.vue";
import opensearch from "@/components/basic/openSearch.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import modalWS00411 from "@/views/Reafs-W/sagyo/WS00411/SagyoYoteiMeisai.vue";
import modalWS00412 from "@/views/Reafs-W/sagyo/WS00412/SagyoYoteiKanryoHoukoku.vue";
import eigyoten from "@/components/basic/EigyoTen.vue";

export default {
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.initialize(); // 初期化処理
      next();
    });
  },
  watch: {
    "KensakuJoken.ddlDaikbn"(n, o) {
      if (n !== "-") {
        this.getDropDown中区分();
      } else {
        this.KensakuJoken.ddlChukbn = "-";
        this.中区分List = [];
      }
    },
    "KensakuJoken.ddlChukbn"(n, o) {
      this.getDropDown小区分();
    }
    // "KensakuJoken.txtTehaiGyoshaCode"(n, o) {
    //   this.getTextBox業者名();
    // },
    // "KensakuJoken.txtTehaiGyoshaCodeEdaban"(n, o) {
    //   this.getTextBox業者名();
    // }
  },

  ///////////////////////////////////////////////
  // コンポーネントタグ
  ///////////////////////////////////////////////
  components: {
    modalWS00411,
    modalWS00412,
    reafstable,
    reafsinputtext,
    reafsinputnaturalnumber,
    opensearch,
    nengetsu,
    eigyoten
  },
  data() {
    var validate年月度 = (rule, value, callback) => {
      if (value === "") {
        this.fnc_rtrn_getMsg("E040277").then(response => {
          callback(new Error(response.data));
        });
      }
    };
    var validate営業店コード = (rule, value, callback) => {
      //@@@@      if (this.KensakuJoken.txtEigyotenCode === '' || this.KensakuJoken.txtEigyotenCodeEdaban === '') {
      if (this.KensakuJoken.txtEigyotenCode === "") {
        this.fnc_rtrn_getMsg("E040009").then(response => {
          callback(new Error(response.data));
        });
      }
    };

    ///////////////////////////////////////////////
    // Template から参照できる変数
    ///////////////////////////////////////////////
    return {
      rules: {
        dtpNengetsudo: [{ validator: validate年月度, trigger: "blur" }],
        txtEigyoten: [{ validator: validate営業店コード, trigger: "blur" }]
      },

      ///////////////////////////////////////////////
      // タブ指定
      ///////////////////////////////////////////////
      // 年月度 開始（未使用）
      TAB_IDX_TXT_NENGETSUDO_KAISHI: "1",
      // 年月度 終了（未使用）
      TAB_IDX_TXT_NENGETSUDO_SYURYOU: "3",
      // 年月度 契約 チェックボックス（未使用）
      TAB_IDX_CHK_NENGETSUDO_KEIYAKU_NENGETSU: "7",
      // 予定／完了確認 ラジオボタン（未使用）
      TAB_IDX_RADIO_YOTEI_KANRYO_KAKUNIN: "6",
      // 物件名（未使用）
      TAB_IDX_TXT_BUKKEN_MEI: "7",
      // 営業店（未使用）
      TAB_IDX_TXT_EIGYOTEN: "8",
      // 工事区分 ラジオボタン（未使用）
      TAB_IDX_RADIO_KOJIKUBUN: "9",
      // 検索 ボタン（未使用）
      TAB_IDX_BTN_KENSAKU: "10",
      // 明細項目一覧（未使用）
      TAB_IDX_TABLE: "11",

      ///////////////////////////////////////////////
      // WD00410 -> モーダルダイアログ データ受け渡しインターフェイス
      ///////////////////////////////////////////////
      showWS00411: false,
      closingNowWS00411: false,
      KeyDataToWS00411: {
        // 会社コード
        kaisha_cd: "",
        // 工事依頼No
        kojiirai_no: "",
        // 契約No
        keiyaku_no: "",
        // 履歴No
        rireki_no: 0,
        // 明細No
        meisai_no: 0,
        // 契約年月
        keiyaku_nengetu: "",
        // 契約年月明細No
        keiyaku_nengetu_meisai: 0,
        // 発注サブNo
        hachu_sub_no: 0,
        // 検収回数
        kenshu_kaisuu: 0,
        // 差戻回数
        sashimodoshi_kaisuu: 0,
        // 物件名
        bukken_name: "",
        // 作業カテゴリー大
        sagyou_c_dai: "",
        // 作業カテゴリー中
        sagyou_c_tyuu: "",
        // 作業カテゴリー小
        sagyou_c_syou: "",
        // 作業名称
        sagyou_name: "",
        // 作業月/回数
        sagyou_tuki_kaisuu: "",
        // 作業予定/備考
        sagyou_yotei_bikou: "",
        // 差戻事由
        sasimodori_ziyuu: "",
        // 作業担当者名
        sagyou_tantou_name: "",
        // 担当者連絡先
        sagyoutantou_tel: "",
        // 作業日
        sagyo_date: {
          startDate: "",
          startTime: "",
          endDate: "",
          endTime: ""
        },
        sagyoyotei_kaisi_date: "",
        sagyoyotei_kaisi_time: "",
        sagyoyotei_syuuryou_date: "",
        sagyoyotei_syuuryou_time: "",
        sagyokanryo_kaisi_date: "",
        sagyokanryo_syuuryou_date: "",
        sagyou_kanryo_bikou: "",

        // 進捗コード
        sintyoku_CD: 0,
        // 差戻エディットボックス
        edit_sashimodoshiJiyuDisable: false,
        // 差戻ボタン
        btn_sashimodoshiDisable: false,
        // 確認ボタン
        btn_kakuninDisable: false
      },
      showWS00412: false,
      closingNowWS00412: false,
      KeyDataToWS00412: {
        // 会社コード
        kaisha_cd: "",
        // 工事依頼No
        kojiirai_no: "",
        // 契約No
        keiyaku_no: "",
        // 履歴No
        rireki_no: 0,
        // 明細No
        meisai_no: 0,
        // 契約年月
        keiyaku_nengetu: "",
        // 契約年月明細No
        keiyaku_nengetu_meisai: 0,
        // 発注サブNo
        hachu_sub_no: 0,
        // 検収回数
        kenshu_kaisuu: 0,
        // 差戻回数
        sashimodoshi_kaisuu: 0,
        // 物件名
        bukken_name: "",
        // 作業カテゴリー大
        sagyou_c_dai: "",
        // 作業カテゴリー中
        sagyou_c_tyuu: "",
        // 作業カテゴリー小
        sagyou_c_syou: "",
        // 作業カテゴリー大中小
        sagyou_KBN: "",
        // 作業名称
        sagyou_name: "",
        // 作業月/回数
        sagyou_tuki_kaisuu: "",
        // 作業予定/備考
        sagyou_yotei_bikou: "",
        // 差戻事由
        sasimodori_ziyuu: "",
        // 作業担当者名
        sagyou_tantou_name: "",
        // 担当者連絡先
        sagyoutantou_tel: "",
        // 作業日
        sagyo_date: {
          startDate: "",
          startTime: "",
          endDate: "",
          endTime: ""
        },
        sagyoyotei_kaisi_date: "",
        sagyoyotei_kaisi_time: "",
        sagyoyotei_syuuryou_date: "",
        sagyoyotei_syuuryou_time: "",
        sagyokanryo_kaisi_date: "",
        sagyokanryo_syuuryou_date: "",
        sagyou_kanryo_bikou: "",

        // 進捗コード
        sintyoku_CD: 0,
        // 差戻エディットボックス
        edit_sashimodoshiJiyuDisable: false,
        // 差戻ボタン
        btn_sashimodoshiDisable: false,
        // 確認ボタン
        btn_kakuninDisable: false,

        tblData2: [],
        名称取得方法17: {}
      },

      ///////////////////////////////////////////////
      // 検索部
      ///////////////////////////////////////////////
      KensakuJoken: {
        // 対象月指定 開始
        dtpNengetsudo_from: "",
        // 対象月指定 終了
        dtpNengetsudo_to: "",
        // 対象月指定
        rdoYearAndMonth: "作業月",

        // 工事区分
        rdoKojiKubun: "全て",

        // 対象日指定 開始
        dtpStartDate: "",
        // 対象日指定 終了
        dtpEndDate: "",
        // 対象日指定
        rdoTargetDate: "作業予定日",

        // ステータス 作業予定未入力
        chkSagyoYoteiMinyuryoku: false,
        // ステータス 作業予定未確認
        chkSagyoYoteiMikakunin: false,
        // ステータス 作業未完了
        chkSagyoMikanryo: false,
        // ステータス 作業完了未確認
        chkSagyoKanryoMikakunin: false,
        // ステータス 作業完了済
        chkSagyoKanryoKakuninzumi: false,
        // ステータス 作業中止
        chkSagyoChusi: false,

        // 物件名 コード
        txtBukkenmeiCode: "",
        // 物件名 名称
        txtBukkenmei: "",
        // 物件区分
        txtBukkenkubun: "",
        // 物件住所郵便番号
        txtBukkenJushoCd: "",
        // 物件住所
        txtBukkenJushoNm: "",

        // 営業店 コード
        txtEigyotenCode: "",
        // 営業店 コード枝番
        txtEigyotenCodeEdaban: "",
        // 営業店 名称
        txtEigyoten: "",

        // 手配業者 コード
        txtTehaiGyoshaCode: "",
        // 手配業者 コード枝番
        txtTehaiGyoshaCodeEdaban: "",
        // 手配業者 名称
        txtTehaiGyoshaName: "",

        // 大区分
        ddlDaikbn: "-",
        // 中区分
        ddlChukbn: "-",
        // 小区分
        ddlSyokbn: "-",

        // 作業名称
        txtSagyoName: "",

        // 備考
        txtBiko: "",

        //画面遷移判別
        kidou_no: 0
      },
      大区分List: [],
      中区分List: [],
      小区分List: [],
      ///////////////////////////////////////////////
      // 明細部
      ///////////////////////////////////////////////
      hitKensu: "0  件",
      cellStyleOption: {
        headerCellClass: ({ row, column, rowIndex }) => {},
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = "";

          const textLeft = [
            "tblbukken",
            "tblEigyoten",
            "tblTehaiGyosha",
            "tblDaikubun",
            "tblChukubun",
            "tblShokubun",
            "tblSagyoMeisho",
            "tblStatus",
            "tblBiko",
            "tblKanryoHokokusho"
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
          key: "tblbukken",
          title: "物件",
          field: "tblbukken",
          width: 120,
          sortBy: "",
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div
                style="display: grid; padding-left: 5px"
                class="cell-body-custome"
              >
                <a
                  style="margin-top: 4px"
                  title={row["tblbukken"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblbukken"]) this.lnktblbukkenOnClick(row, column, rowIndex);
                  }}
                  href="javascript:;"
                >
                  {row["tblbukken"]}
                </a>
              </div>
            );
          },
        },

        {
          key: "tblEigyoten",
          title: "営業店",
          field: "tblEigyoten",
          width: 120,
          sortBy: "",
        },

        {
          key: "tblTehaiGyosha",
          title: "手配業者",
          field: "tblTehaiGyosha",
          width: 240,
          sortBy: "",
        },

        {
          key: "tblDaikubun",
          title: "大区分",
          field: "tblDaikubun",
          width: 130,
          sortBy: "",
        },
        
        {
          key: "tblChukubun",
          title: "中区分",
          field: "tblChukubun",
          width: 130,
          sortBy: "",
        },

        {
          key: "tblShokubun",
          title: "小区分",
          field: "tblShokubun",
          width: 130,
          sortBy: "",
        },

        {
          key: "tblSagyoMeisho",
          title: "作業名称",
          field: "tblSagyoMeisho",
          width: 240,
          ellipsis: {
            showTitle: true
          },
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div
                style="display: grid; padding-left: 5px"
                class="cell-body-custome"
              >
                <a
                  style="margin-top: 4px"
                  title={row["tblSagyoMeisho"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblSagyoMeisho"]) this.lnktblSagyoMeishoOnClick(row, column, rowIndex);
                  }}
                  href="javascript:;"
                >
                  {row["tblSagyoMeisho"]}
                </a>
              </div>
            );
          },
        },

        {
          key: "tblStatus",
          title: "ステータス",
          field: "tblStatus",
          width: 130,
          sortBy: "",
        },

        {
          key: "JissiYoteiNengetsu",
          title: "作業月",
          field: "JissiYoteiNengetsu",
          width: 130,
          sortBy: ""
        },

        {
          key: "tblSagyoYoteibiStartDate",
          title: "作業予定日(自)",
          field: "tblSagyoYoteibiStartDate",
          width: 141,
          sortBy: ""
        },

        {
          key: "tblSagyoYoteibiEndtDate",
          title: "作業予定日(至)",
          field: "tblSagyoYoteibiEndtDate",
          width: 141,
          sortBy: ""
        },

        {
          key: "tblSagyoYoteibiTime",
          title: "作業予定時刻",
          field: "tblSagyoYoteibiTime",
          width: 130,
          sortBy: ""
        },

        {
          key: "tblBiko",
          title: "備考",
          field: "tblBiko",
          width: 130,
          sortBy: ""
        },

        {
          key: "tblSagyoKanryobi",
          title: "作業完了日",
          field: "tblSagyoKanryobi",
          width: 140,
          sortBy: ""
        },

        {
          key: "tblKanryoKakuninbi",
          title: "完了確認日",
          field: "tblKanryoKakuninbi",
          width: 140,
          sortBy: ""
        },

        //   {
        //     title: "完了報告書",
        //     align: "left",
        //     link: true,
        //     width: 500,
        //     render: (h, { row, column, index }) => {
        //       return (
        //         <div style="display: flex" class="cell-body-custome clsHansu">
        //           <div>
        //             <el-link
        //               on-click={() => {
        //                 this.onDownload(
        //                   row.tblKanryoHokokushoPass1,
        //                   row.tblKanryoHokokusho1
        //                 );
        //               }}
        //               value={row.tblKanryoHokokusho1}
        //               class="link_color"
        //               type="primary"
        //             >
        //               {row.tblKanryoHokokusho1}
        //             </el-link>
        //             <br />
        //             <el-link
        //               on-click={() => {
        //                 this.onDownload(
        //                   row.tblKanryoHokokushoPass2,
        //                   row.tblKanryoHokokusho2
        //                 );
        //               }}
        //               value={row.tblKanryoHokokusho2}
        //               class="link_color"
        //               type="primary"
        //             >
        //               {row.tblKanryoHokokusho2}
        //             </el-link>
        //             <br />
        //             <el-link
        //               on-click={() => {
        //                 this.onDownload(
        //                   row.tblKanryoHokokushoPass3,
        //                   row.tblKanryoHokokusho3
        //                 );
        //               }}
        //               value={row.tblKanryoHokokusho3}
        //               class="link_color"
        //               type="primary"
        //             >
        //               {row.tblKanryoHokokusho3}
        //             </el-link>
        //             <br />
        //             <el-link
        //               on-click={() => {
        //                 this.onDownload(
        //                   row.tblKanryoHokokushoPass4,
        //                   row.tblKanryoHokokusho4
        //                 );
        //               }}
        //               value={row.tblKanryoHokokusho4}
        //               class="link_color"
        //               type="primary"
        //             >
        //               {row.tblKanryoHokokusho4}
        //             </el-link>
        //             <br />
        //             <el-link
        //               on-click={() => {
        //                 this.onDownload(
        //                   row.tblKanryoHokokushoPass5,
        //                   row.tblKanryoHokokusho5
        //                 );
        //               }}
        //               value={row.tblKanryoHokokusho5}
        //               class="link_color"
        //               type="primary"
        //             >
        //               {row.tblKanryoHokokusho5}
        //             </el-link>
        //           </div>
        //         </div>
        //       );
        //     }
        //   }
        {
          key: "tblKanryoHokokusho",
          title: "完了報告書",
          width: 500,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div
                style="display: grid; padding-left: 5px"
                class="cell-body-custome clsHansu"
              >
                <a
                  style="margin-top: 4px"
                  title={row["tblKanryoHokokusho1"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKanryoHokokusho1"]) this.onDownload(row["tblKanryoHokokushoPass1"], row["tblKanryoHokokusho1"]);
                  }}
                  href="javascript:;"
                >
                  {row["tblKanryoHokokusho1"]}
                </a>
                <br />
                <a
                  style="margin-top: 4px"
                  title={row["tblKanryoHokokusho2"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKanryoHokokusho2"]) this.onDownload(row["tblKanryoHokokushoPass2"], row["tblKanryoHokokusho2"]);
                  }}
                  href="javascript:;"
                >
                  {row["tblKanryoHokokusho2"]}
                </a>
                <br />
                <a
                  style="margin-top: 4px"
                  title={row["tblKanryoHokokusho3"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKanryoHokokusho3"]) this.onDownload(row["tblKanryoHokokushoPass3"], row["tblKanryoHokokusho3"]);
                  }}
                  href="javascript:;"
                >
                  {row["tblKanryoHokokusho3"]}
                </a>
                <br />
                <a
                  style="margin-top: 4px"
                  title={row["tblKanryoHokokusho4"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKanryoHokokusho4"]) this.onDownload(row["tblKanryoHokokushoPass4"], row["tblKanryoHokokusho4"]);
                  }}
                  href="javascript:;"
                >
                  {row["tblKanryoHokokusho4"]}
                </a>
                <br />
                <a
                  style="margin-top: 4px"
                  title={row["tblKanryoHokokusho5"]}
                  class="ellipsis"
                  on-click={() => {
                    if (row["tblKanryoHokokusho5"]) this.onDownload(row["tblKanryoHokokushoPass5"], row["tblKanryoHokokusho5"]);
                  }}
                  href="javascript:;"
                >
                  {row["tblKanryoHokokusho5"]}
                </a>
              </div>
            );
          },
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

      virtualScrollOption: {
        enable: true,
        minRowHeight: 103
      },

      ///////////////////////////////////////////////
      // エラーフラグ DATEPICKERをピンクにする為に使用
      ///////////////////////////////////////////////
      isErrPinkNengetsudo: false,
      isErrPinkEigyoten: false,
      isErrPinkBukken: false,

      subParamsObj: {
        taisyou: "", // 承認種類　 取引先検索：対象 //ハノイ側修正2022/08/26　STEP2_W　課題管理表№59：設計書「修正履歴」シートの「2022/08/24仕様変更分」「取引先検索起動時にパラメータ仕様を変更」
        jigyousyoCd: "110", // 事業所コード
        torType: "" // null or 0:取引先 1:得意先
      }
    };
  },

  mounted() {
    this.$nextTick(function() {
      this.$refs.ref年月度.focus();
    });
    let kidouNO = this.KensakuJoken.kidou_no;
    this.fnc_void_clickBtnReset(kidouNO);
  },

  methods: {
    initialize() {
      this.KensakuJoken.kidou_no = this.$route.params.StartuoMode;

      // 検索処理起動
      if (this.KensakuJoken.kidou_no == 1) {
        // カウンターからの遷移による起動
        let kidouNO = this.KensakuJoken.kidou_no;
        //初期値セット
        this.fnc_void_clickBtnReset(kidouNO);

        this.fnc_void_Kensaku();
      } else if (this.KensakuJoken.kidou_no == 2) {
        // メールからの起動
        let 契約No = this.$route.params.契約No;
        let 履歴No = this.$route.params.履歴No;
        let 明細No = this.$route.params.明細No;
        let 契約年月 = this.$route.params.契約年月;
        let 契約年月明細No = this.$route.params.契約年月明細NO;

        // メール起動固有の検索
        this.fnc_void_search_MailData(
          契約No,
          履歴No,
          明細No,
          契約年月,
          契約年月明細No
        );
      } else {
        // 通常起動
        this.KensakuJoken.kidou_no = 0;
      }
    },

    getDropDown中区分() {
      console.log("中区分List取得処理実行");

      this.http
        .get("/api/Reafs_W/Sagyo/GetDropdown中区分", {
          大区分: this.KensakuJoken.ddlDaikbn
        })
        .then(res => {
          this.中区分List = res.data;
          this.KensakuJoken.ddlChukbn = "-";
        });
    },

    getDropDown小区分() {
      console.log("小区分List取得処理実行");

      if (
        this.KensakuJoken.ddlDaikbn !== "-" &&
        this.KensakuJoken.ddlChukbn !== "-"
      ) {
        this.http
          .get("/api/Reafs_W/Sagyo/GetDropdown小区分", {
            大区分: this.KensakuJoken.ddlDaikbn,
            中区分: this.KensakuJoken.ddlChukbn
          })
          .then(res => {
            this.小区分List = res.data;
            this.KensakuJoken.ddlSyokbn = "-";
          });
      } else {
        this.小区分List = [];
        this.KensakuJoken.小区分 = "-";
      }
    },

    getTextBox業者名() {
      console.log("業者名取得処理実行");

      if (
        this.KensakuJoken.txtTehaiGyoshaCode !== "" &&
        this.KensakuJoken.txtTehaiGyoshaCode !== undefined &&
        this.KensakuJoken.txtTehaiGyoshaCodeEdaban !== "" &&
        this.KensakuJoken.txtTehaiGyoshaCodeEdaban
      ) {
        console.log("業者検索実行");

        this.http
          .post("/api/Reafs_W/Sagyo/GetDataM006", {
            業者コード: this.KensakuJoken.txtTehaiGyoshaCode,
            業者コード枝番: this.KensakuJoken.txtTehaiGyoshaCodeEdaban
          })
          .then(res => {
            if (res.data.length !== 0) {
              this.KensakuJoken.txtTehaiGyoshaName = res.data.業者名;
            } else {
              // 該当データ無し
              this.KensakuJoken.txtTehaiGyoshaName = "";
            }
          });
      } else {
        this.KensakuJoken.txtTehaiGyoshaName = "";
      }
    },

    ///////////////////////////////////////////////
    // 検索ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnKensaku() {
      //検索ボタン押下時に引数を０にする。
      this.KensakuJoken.kidou_no = 0;
      this.fnc_void_Kensaku();
    },

    fnc_void_Kensaku() {
      console.log("検索ボタン押下！");
      // 検索前処理
      let letKensakuJokenConvertDataAfter = new Object();
      letKensakuJokenConvertDataAfter = this.fnc_rtrn_getKensakuBefore(
        this.KensakuJoken
      );

      //エラーなければ検索実行
      if (letKensakuJokenConvertDataAfter[0] === true) {
        //検索実行
        this.fnc_void_getKensakuKekka(letKensakuJokenConvertDataAfter[1]);
      }
    },

    fnc_void_search_MailData(契約No, 履歴No, 明細No, 契約年月, 契約年月明細No) {
      let searchTarget = {
        契約No: 契約No,
        履歴No: 履歴No,
        明細No: 明細No,
        契約年月: 契約年月,
        契約年月明細No: 契約年月明細No
      };

      this.fnc_void_getKensakuKekka();
    },

    fnc_WS00411Close(e) {
      if (this.closingNowWS00411 === false) {
        console.log("WS00411を閉じたぞ！");
        this.closingNowWS00411 = true;
        this.fnc_void_Kensaku();
      } else {
        this.closingNowWS00411 = false;
      }
    },

    fnc_WS00412Close(e) {
      if (this.closingNowWS00412 === false) {
        console.log("WS00412を閉じたぞ！");
        this.closingNowWS00412 = true;
        this.fnc_void_Kensaku();
      } else {
        this.closingNowWS00412 = false;
      }
    },

    ///////////////////////////////////////////////
    // 検索実行前処理
    ///////////////////////////////////////////////
    fnc_rtrn_getKensakuBefore(valKensakuJoken) {
      let letJdg;

      // ピンク色等データ初期状態にクリア
      this.fnc_void_clearData();

      //検索前エラーチェック
      letJdg = this.fnc_rtrn_kensakumaeErrorChk(valKensakuJoken);
      if (letJdg === false) {
        return [false, false];
        exit;
      }

      // 前準備。検索条件をバックに渡す用にコンバート
      let letKensakuJokenConvertDataAfter = new Object();
      letKensakuJokenConvertDataAfter = this.fnc_rtrn_KensakuJokenConvert(
        valKensakuJoken
      );

      return [true, letKensakuJokenConvertDataAfter];
    },

    ///////////////////////////////////////////////
    // 検索実行
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka(KensakuJokenConvertAfter) {
      console.log("検索処理実行");
      // 検索実行
      let _this = this;
      let hokokusyo = new Object();

      _this.http
        .post(
          "/api/Reafs_W/Sagyo/WD00410_GET_KensakuKekka",
          KensakuJokenConvertAfter,
          "検索しています...."
        )
        // 検索実行後処理
        .then(function(response) {
          //サーバが落ちた時エラーメッセージ表示
          if (!response.status) {
            _this.MsgErr({
              title: "作業状況一覧",
              message: response.message
            });
          } else if (response.data.length === 0) {
            // 検索件数 = 0件の場合
            // 対象データが存在しません。
            _this.fnc_void_showErrorMsg("E010224");
          } else {
            // 20000件超えメッセージ
            if (response.message !== null) {
              console.log('response.message1' + response.message)
              _this.MsgErr({
                title: "作業状況一覧",
                message: response.message
              });
            }

            _this.tblData = response.data;
            _this.tblData.map((item, index) => {
              item.No = index + 1
            })
            _this.hitKensu = response.data.length + "  件";

            for (let i = 0; i < response.data.length; i++) {
              // 作業時間
              _this.tblData[i].tblSagyoYoteibiTime = 
                _this.fnc_rtrn_getSagyoYoteibiTime(response.data[i]);
            }
          }
        });
    },

    ///////////////////////////////////////////////
    // 検索条件リセットボタン押下時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnReset(e) {
      if (e == 1) {
        let now_before;
        now_before = new Date();
        now_before.setMonth(now_before.getMonth());
        let now_before_year = now_before.getFullYear();
        now_before_year = now_before_year.toString();
        var now_before_mounth = now_before.getMonth() + 1;
        now_before_mounth = ("00" + now_before_mounth).slice(-2);
        this.KensakuJoken.dtpNengetsudo_from =
          now_before_year + "/" + now_before_mounth;

        //翌月用
        let now_after = new Date();
        now_after.setFullYear(now_after.getFullYear() + 1);
        now_after.setMonth(now_after.getMonth() + 1);

        let now_after_year = now_after.getFullYear();
        now_after_year = now_after_year.toString();
        var now_after_mounth = now_after.getMonth();
        now_after_mounth = ("00" + now_after_mounth).slice(-2);
        this.KensakuJoken.dtpNengetsudo_to =
          now_after_year + "/" + now_after_mounth;

        this.KensakuJoken.rdoKojiKubun = "定期委託";
        this.KensakuJoken.rdoYoteiKanryoKakunin = "予定";

        this.KensakuJoken.dtpStartDate = "";
        this.KensakuJoken.dtpEndDate = "";
        this.KensakuJoken.rdoTargetDate = "作業予定日";

        this.KensakuJoken.chkSagyoYoteiMinyuryoku = false;
        this.KensakuJoken.chkSagyoYoteiMikakunin = true;
        this.KensakuJoken.chkSagyoMikanryo = false;
        this.KensakuJoken.chkSagyoKanryoMikakunin = false;
        this.KensakuJoken.chkSagyoKanryoKakuninzumi = false;
        this.KensakuJoken.chkSagyoChusi = false;

        this.KensakuJoken.txtBukkenmeiCode = "";
        this.KensakuJoken.txtBukkenmei = "";
        this.KensakuJoken.txtBukkenkubun = "";
        this.KensakuJoken.txtBukkenJushoCd = "";
        this.KensakuJoken.txtBukkenJushoNm = "";

        this.KensakuJoken.txtEigyotenCode = "";
        this.KensakuJoken.txtEigyotenCodeEdaban = "";
        this.KensakuJoken.txtEigyoten = "";
        this.KensakuJoken.txtTehaiGyoshaCode = "";
        this.KensakuJoken.txtTehaiGyoshaCodeEdaban = "";
        this.KensakuJoken.txtTehaiGyoshaName = "";

        this.KensakuJoken.ddlDaikbn = "-";
        this.KensakuJoken.ddlChukbn = "-";
        this.KensakuJoken.ddlSyokbn = "-";

        this.KensakuJoken.txtSagyoName = "";
        this.KensakuJoken.txtBiko = "";
        this.isErrPinkNengetsudo = false;
        this.isErrPinkEigyoten = false;
        this.isErrPinkBukken = false;
      } else {
        //今月用
        let now_before;
        now_before = new Date();
        now_before.setMonth(now_before.getMonth());

        let now_before_year = now_before.getFullYear();
        now_before_year = now_before_year.toString();
        var now_before_mounth = now_before.getMonth() + 1;
        now_before_mounth = ("00" + now_before_mounth).slice(-2);
        this.KensakuJoken.dtpNengetsudo_from =
          now_before_year + "/" + now_before_mounth;

        //翌月用
        let now_after = new Date();
        now_after.setMonth(now_after.getMonth() + 2);

        let now_after_year = now_after.getFullYear();
        now_after_year = now_after_year.toString();
        var now_after_mounth = now_after.getMonth();
        now_after_mounth = ("00" + now_after_mounth).slice(-2);
        this.KensakuJoken.dtpNengetsudo_to =
          now_after_year + "/" + now_after_mounth;

        this.KensakuJoken.rdoYearAndMonth = "作業月";
        this.KensakuJoken.rdoKojiKubun = "全て";

        this.KensakuJoken.dtpStartDate = "";
        this.KensakuJoken.dtpEndDate = "";
        this.KensakuJoken.rdoTargetDate = "作業予定日";

        this.KensakuJoken.chkSagyoYoteiMinyuryoku = true;
        this.KensakuJoken.chkSagyoYoteiMikakunin = true;
        this.KensakuJoken.chkSagyoMikanryo = true;
        this.KensakuJoken.chkSagyoKanryoMikakunin = true;
        this.KensakuJoken.chkSagyoKanryoKakuninzumi = true;
        this.KensakuJoken.chkSagyoChusi = true;

        this.KensakuJoken.ddlDaikbn = "-";
        this.http
          .get("/api/Reafs_W/Sagyo/GetStartInfo", "アクセスしています....")
          .then(response => {
            this.名称取得方法17 = response.data.名称取得方法17;
            this.大区分List = response.data.名称取得方法08;
          });
        this.KensakuJoken.ddlChukbn = "-";
        this.KensakuJoken.ddlSyokbn = "-";

        this.KensakuJoken.txtBukkenmeiCode = "";
        this.KensakuJoken.txtBukkenmei = "";
        this.KensakuJoken.txtBukkenkubun = "";
        this.KensakuJoken.txtBukkenJushoCd = "";
        this.KensakuJoken.txtBukkenJushoNm = "";

        this.KensakuJoken.txtEigyotenCode = "";
        this.KensakuJoken.txtEigyotenCodeEdaban = "";
        this.KensakuJoken.txtEigyoten = "";
        this.KensakuJoken.txtTehaiGyoshaCode = "";
        this.KensakuJoken.txtTehaiGyoshaCodeEdaban = "";
        this.KensakuJoken.txtTehaiGyoshaName = "";

        this.KensakuJoken.txtSagyoName = "";
        this.KensakuJoken.txtBiko = "";
        this.isErrPinkNengetsudo = false;
        this.isErrPinkEigyoten = false;
        this.isErrPinkBukken = false;
      }
    },

    ///////////////////////////////////////////////
    // ステータス指定_予定確認ボタン押下時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnYoteiKakunin() {
      this.KensakuJoken.chkSagyoYoteiMinyuryoku = false;
      this.KensakuJoken.chkSagyoYoteiMikakunin = true;
      this.KensakuJoken.chkSagyoMikanryo = false;
      this.KensakuJoken.chkSagyoKanryoMikakunin = false;
      this.KensakuJoken.chkSagyoKanryoKakuninzumi = false;
      this.KensakuJoken.chkSagyoChusi = false;
    },

    ///////////////////////////////////////////////
    // ステータス指定_完了確認ボタン押下時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnKanryoKakunin() {
      this.KensakuJoken.chkSagyoYoteiMinyuryoku = false;
      this.KensakuJoken.chkSagyoYoteiMikakunin = false;
      this.KensakuJoken.chkSagyoMikanryo = false;
      this.KensakuJoken.chkSagyoKanryoMikakunin = true;
      this.KensakuJoken.chkSagyoKanryoKakuninzumi = false;
      this.KensakuJoken.chkSagyoChusi = false;
    },

    ///////////////////////////////////////////////
    // 作業時間取得
    ///////////////////////////////////////////////
    fnc_rtrn_getSagyoYoteibiTime(valResponseData) {
      let sagyoYoteibiTime =
        valResponseData.yoteijikanKaishi +
        "～" +
        valResponseData.yoteijikanSyuryo;

      return sagyoYoteibiTime;
    },

    ///////////////////////////////////////////////
    // 検索条件コンバート
    // バックに渡すパラメータ用にコンバートする
    ///////////////////////////////////////////////
    fnc_rtrn_KensakuJokenConvert(KensakuJokenConvertDataBefore) {
      console.log("検索時コンバート開始");

      // 変換後データ
      let letKensakuJokenConvertDataAfter = new Object();

      //チェックボックスコンバート
      let letchkKeiyakuNengetsuConvertAfter = new Object();
      let letchkSagyoYoteiConvertAfter = new Object();
      let letchkSagyoKanryoConvertAfter = new Object();

      // 年月度 契約年月
      letchkKeiyakuNengetsuConvertAfter =
        this.fnc_obj_CheckBoxConvert(KensakuJokenConvertDataBefore.chkKeiyakuNengetsu);

      // 年月度 作業予定
      letchkSagyoYoteiConvertAfter =
        this.fnc_obj_CheckBoxConvert(KensakuJokenConvertDataBefore.chkSagyoYotei);

      // 年月度 作業完了
      letchkSagyoKanryoConvertAfter =
        this.fnc_obj_CheckBoxConvert(KensakuJokenConvertDataBefore.chkSagyoKanryo);

      // 対象月 開始
      letKensakuJokenConvertDataAfter.dtpNengetsudo_from =
        KensakuJokenConvertDataBefore.dtpNengetsudo_from;
      // 対象月 終了
      letKensakuJokenConvertDataAfter.dtpNengetsudo_to =
        KensakuJokenConvertDataBefore.dtpNengetsudo_to;
      // 対象月 指定先
      letKensakuJokenConvertDataAfter.rdoYearAndMonth =
        KensakuJokenConvertDataBefore.rdoYearAndMonth;
      // 工事区分
      letKensakuJokenConvertDataAfter.rdoKojiKubun =
        KensakuJokenConvertDataBefore.rdoKojiKubun;
      // 対象日 開始
      letKensakuJokenConvertDataAfter.dtpStartDate =
        KensakuJokenConvertDataBefore.dtpStartDate;
      // 対象日 終了
      letKensakuJokenConvertDataAfter.dtpEndDate =
        KensakuJokenConvertDataBefore.dtpEndDate;
      // 対象日 指定先
      letKensakuJokenConvertDataAfter.rdoTargetDate =
        KensakuJokenConvertDataBefore.rdoTargetDate;

      // ステータス
      letKensakuJokenConvertDataAfter.chkSagyoYoteiMinyuryoku = this.KensakuJoken.chkSagyoYoteiMinyuryoku;
      letKensakuJokenConvertDataAfter.chkSagyoYoteiMikakunin = this.KensakuJoken.chkSagyoYoteiMikakunin;
      letKensakuJokenConvertDataAfter.chkSagyoMikanryo = this.KensakuJoken.chkSagyoMikanryo;
      letKensakuJokenConvertDataAfter.chkSagyoKanryoMikakunin = this.KensakuJoken.chkSagyoKanryoMikakunin;
      letKensakuJokenConvertDataAfter.chkSagyoKanryoKakuninzumi = this.KensakuJoken.chkSagyoKanryoKakuninzumi;
      letKensakuJokenConvertDataAfter.chkSagyoChusi = this.KensakuJoken.chkSagyoChusi;

      // 物件コード
      letKensakuJokenConvertDataAfter.txtBukkenmeiCode =
        KensakuJokenConvertDataBefore.txtBukkenmeiCode;

      // 営業店コード
      letKensakuJokenConvertDataAfter.txtEigyotenCode = 
        KensakuJokenConvertDataBefore.txtEigyotenCode.substr(0, 4);

      // 営業店コード枝番
      letKensakuJokenConvertDataAfter.txtEigyotenCodeEdaban =
        KensakuJokenConvertDataBefore.txtEigyotenCode.substr(4, 2);

      // 手配業者コード
      letKensakuJokenConvertDataAfter.txtTehaiGyoshaCode =
        KensakuJokenConvertDataBefore.txtTehaiGyoshaCode;
      
      // 手配業者コード枝番
      letKensakuJokenConvertDataAfter.txtTehaiGyoshaCodeEdaban =
        KensakuJokenConvertDataBefore.txtTehaiGyoshaCodeEdaban;

      // 大区分
      letKensakuJokenConvertDataAfter.ddlDaikbn =
        KensakuJokenConvertDataBefore.ddlDaikbn;
      // 中区分
      letKensakuJokenConvertDataAfter.ddlChukbn =
        KensakuJokenConvertDataBefore.ddlChukbn;
      // 小区分
      letKensakuJokenConvertDataAfter.ddlSyokbn =
        KensakuJokenConvertDataBefore.ddlSyokbn;

      // 作業名称_部分一致
      letKensakuJokenConvertDataAfter.txtSagyoName =
        KensakuJokenConvertDataBefore.txtSagyoName;
      // 備考_部分一致
      letKensakuJokenConvertDataAfter.txtBiko =
        KensakuJokenConvertDataBefore.txtBiko;

      //画面遷移判別
      letKensakuJokenConvertDataAfter.kidou_no =
        KensakuJokenConvertDataBefore.kidou_no;

      return letKensakuJokenConvertDataAfter;
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
      _this.isErrPinkNengetsudo = false;
      _this.isErrPinkEigyoten = false;
      _this.isErrPinkBukken = false;
    },

    ///////////////////////////////////////////////
    // リンククリック時の画面遷移またはモーダル表示処理
    ///////////////////////////////////////////////
    // fnc_clickLink(row, column, rowIndex) {
    //   switch (column.field) {
    //     // 物件名
    //     case "tblEigyoten":
    //       this.$router.push({
    //         name: "WD00420",
    //         query: {
    //           kojiKubun: "定期委託", //@@@@ 取り敢えず固定値
    //           txtEigyotenCode:
    //             this.tblData[rowIndex].tblEigyotenCode +
    //             this.tblData[rowIndex].tblEigyotenCodeEdaban,
    //           txtEigyoten: this.tblData[rowIndex].tblEigyoten,
    //           JissiYoteiNengetsu: this.tblData[rowIndex].JissiYoteiNengetsu
    //         }
    //       });
    //       break;

    //     // 作業名称
    //     case "tblSagyoMeisho":
    //       console.log("WD00411表示処理");
    //       this.fnc_void_dispSubDialog(row, column, rowIndex);
    //       break;

    //     default:
    //       break;
    //   }
    // },

    lnktblbukkenOnClick(row, column, rowIndex) {
      console.log("物件リンク押下");

      this.$router.push({
        name: "WD00420",
        query: {
          kojiKubun: "定期委託", //@@@@ 取り敢えず固定値
          txtEigyotenCode:
            row.tblEigyotenCode +
            row.tblEigyotenCodeEdaban,
          txtEigyoten: row.tblEigyoten,
          JissiYoteiNengetsu: row.jissiYoteiNengetsu
        }
      });
    }, 

    lnktblSagyoMeishoOnClick(row, column, rowIndex) {
      console.log("WD00411表示処理");

      this.fnc_void_dispSubDialog(row, column, rowIndex);
    },

    async onDownload(DLPass, FileName) {
      console.log("ファイルダウンロードするよ");

      if (DLPass == null || DLPass.toString().trim().length == 0) {
        return;
      }

      // this.loading["refDl"] = true;
      const FileDownloadInfo = {
        FilePath: DLPass,
        FileName: FileName
      };

      await this.http
        .post("/api/Reafs_W/Common/DownloadFile", FileDownloadInfo, "", {
          responseType: "blob"
        })
        .then(async blob => {
          await this.base.saveFileDownload(blob, FileName);
        })
        .catch(ex => console.log(ex))
        .finally(() => {})
        .catch(ex => console.log(ex));
    },

    ///////////////////////////////////////////////
    // サブ画面表示@@@@
    ///////////////////////////////////////////////
    async fnc_void_dispSubDialog(row, column, rowIndex) {
      let _this = this;
      let valParamToWSSubDialog = _this.fnc_rtn_getSagyoInfo(
        row,
        column,
        rowIndex
      );
      console.log(row.type);

      // 定期委託時
      if (valParamToWSSubDialog.type == "teiki") {
        if (
          "510" <= valParamToWSSubDialog.sintyoku_cd &&
          valParamToWSSubDialog.sintyoku_cd <= "530"
        ) {
          // 作業予定明細画面表示
          _this.KeyDataToWS00411 = valParamToWSSubDialog;
          _this.showWS00411 = true;
        } else if (
          "530" < valParamToWSSubDialog.sintyoku_cd &&
          valParamToWSSubDialog.sintyoku_cd <= "550"
        ) {
          //添付ファイル取得
          await _this.http
            .post("/api/Reafs_W/Sagyo/WS00410_GET_attached", {
              契約no: row.keiyakuNo,
              履歴no: row.rirekiNo,
              明細no: row.meisaiNo,
              契約年月明細NO: row.keiyakuNengetsuMeisai,
              契約年月: row.keiyakuNengetsu,
              実施予定年月: row.jissiYoteiNengetsu
            })
            // 検索実行後処理
            .then(function(response) {
              _this.tblData2 = response.data;
            });
          // 作業完了報告画面表示
          _this.KeyDataToWS00412 = valParamToWSSubDialog;
          _this.showWS00412 = true;
        } else {
          // 処理なし
          console.log(
            "画面表示なし 進捗コード（受注）:" +
              valParamToWSSubDialog.sintyoku_cd
          );
        }
      } else if (valParamToWSSubDialog.type == "spot") {
        // F052.検収回数0より大きいレコード有 かつ 修繕区分大がスポット清掃(03)
        if (
          valParamToWSSubDialog.tblStatus == "作業予定未入力" ||
          valParamToWSSubDialog.tblStatus == "作業予定未確認"
        ) {
          // 作業予定明細画面表示
          _this.KeyDataToWS00411 = valParamToWSSubDialog;
          _this.showWS00411 = true;
        } else {
          // 処理なし
          console.log("画面表示なし");
        }
      } else {
      }
    },

    ///////////////////////////////////////////////
    // 作業情報取得@@@@
    ///////////////////////////////////////////////
    fnc_rtn_getSagyoInfo(row, column, rowIndex) {
      let _this = this;
      let valParamToWSSubDialog = new Object();

      valParamToWSSubDialog = row;

      // 工事区分
      valParamToWSSubDialog.rdoKojiKubun = this.KensakuJoken.rdoKojiKubun;
      // 会社コード
      valParamToWSSubDialog.KaishaCd = row.kaishaCd;
      // 工事依頼No
      valParamToWSSubDialog.KojiiraiNo = row.kojiiraiNo;
      // 契約NO
      valParamToWSSubDialog.KeiyakuNo = row.keiyakuNo;
      // 履歴No
      valParamToWSSubDialog.RirekiNo = row.rirekiNo;
      // 明細NO
      valParamToWSSubDialog.MeisaiNo = row.meisaiNo;
      // 契約年月
      valParamToWSSubDialog.KeiyakuNengetsu = row.keiyakuNengetsu;
      // 契約年月明細NO
      valParamToWSSubDialog.KeiyakuNengetsuMeisai = row.keiyakuNengetsuMeisai;

      //発注サブNO
      valParamToWSSubDialog.発注サブno = row.発注サブno;
      //検収回数
      valParamToWSSubDialog.検収回数 = row.検収回数;

      // 差戻回数
      valParamToWSSubDialog.SashimodoshiKaisuu = row.sashimodoshiKaisuu;
      // 物件名
      valParamToWSSubDialog.bukken_name = row.tblbukken;
      // 営業店
      valParamToWSSubDialog.tblEigyoten = row.tblEigyoten;
      // 手配業者
      valParamToWSSubDialog.tehai_name = row.tblTehaiGyosha;
      // 大区分
      valParamToWSSubDialog.sagyou_c_dai = row.tblDaikubun;
      // 中区分
      valParamToWSSubDialog.sagyou_c_tyuu = row.tblChukubun;
      // 小区分
      valParamToWSSubDialog.sagyou_c_syou = row.tblShokubun;
      // 大中小区分
      valParamToWSSubDialog.sagyou_KBN =
        row.tblDaikubun + " " + row.tblChukubun + " " + row.tblShokubun;
      // 作業名称
      valParamToWSSubDialog.sagyou_name = row.tblSagyoMeisho;
      // 作業月/回目
      valParamToWSSubDialog.sagyou_tuki_kaisuu =
        row.jissiYoteiNengetsu.substr(0, 4) +
        "年" +
        row.jissiYoteiNengetsu.substr(5, 2) +
        "月 " +
        row.kaime +
        "回目報告";
      valParamToWSSubDialog.sagyo_date = new Object();

      // 備考
      valParamToWSSubDialog.sagyou_yotei_bikou = row.tblBiko;
      // 進捗コード（受注）
      valParamToWSSubDialog.sintyoku_cd = row.shintyokuCode;
      // 差戻事由
      valParamToWSSubDialog.sasimodori_ziyuu = 
        _this.fnc_void_getSashimodoshiJiyuInfo(row, column, rowIndex);

      // 定期と修繕で処理が分かれます。
      if (valParamToWSSubDialog.type == "teiki") {
        // 作業月/回目
        valParamToWSSubDialog.sagyou_tuki_kaisuu =
          row.jissiYoteiNengetsu.substr(0, 4) +
          "年" +
          row.jissiYoteiNengetsu.substr(5, 2) +
          "月 " +
          row.kaime +
          "回目報告";
      } else {
        valParamToWSSubDialog.sagyou_tuki_kaisuu =
          row.jissiYoteiNengetsu.substr(0, 4) +
          "年" +
          row.jissiYoteiNengetsu.substr(5, 2) +
          "月 ";
      }

      // 差戻事由有効
      valParamToWSSubDialog.edit_sashimodoshiJiyuDisable = false;
      // 差戻ボタン有効
      valParamToWSSubDialog.btn_sashimodoshiDisable = false;
      // 確認ボタン有効
      valParamToWSSubDialog.btn_kakuninDisable = false;

      if (valParamToWSSubDialog.sintyoku_cd <= "530") {
        // 作業予定日 開始
        valParamToWSSubDialog.sagyo_date.startDate =
          row.tblSagyoYoteibiStartDate;

        // 作業予定時間 開始
        valParamToWSSubDialog.sagyo_date.startTime = row.tblSagyoYoteibiTime.substr(0, 5);
        // 作業予定日 終了
        valParamToWSSubDialog.sagyo_date.endDate = row.tblSagyoYoteibiEndtDate;
        // 作業予定時間 終了
        valParamToWSSubDialog.sagyo_date.endTime = row.tblSagyoYoteibiTime.substr(6, 5);
        // 作業実施日 終了引数用
        valParamToWSSubDialog.sagyoyotei_syuuryou_date = row.tblSagyoYoteibiEndtDate;

        valParamToWSSubDialog.sagyoyotei_kaisi_date =row.tblSagyoYoteibiStartDate;

        valParamToWSSubDialog.sagyoyotei_kaisi_time = row.tblSagyoYoteibiTime.substr(0, 5);
        valParamToWSSubDialog.sagyoyotei_syuuryou_date = row.tblSagyoYoteibiEndtDate;
        valParamToWSSubDialog.sagyoyotei_syuuryou_time = row.tblSagyoYoteibiTime.substr(6, 5);
        valParamToWSSubDialog.sagyokanryo_kaisi_date = row.tblSagyoKanryobiStartDate;
        valParamToWSSubDialog.sagyokanryo_syuuryou_date = row.tblSagyoKanryobi;

        // 定期と修繕で処理が分かれます。
        if (valParamToWSSubDialog.type == "teiki") {
          //530（作業未完了）の場合は、見るだけのため、ボタン使用不可
          if (
            valParamToWSSubDialog.sintyoku_cd >= "520" &&
            valParamToWSSubDialog.sintyoku_cd < "530"
          ) {
            //無効の場合のため処理なし
          } else {
            // 差戻事由有効
            valParamToWSSubDialog.edit_sashimodoshiJiyuDisable = true;
            // 差戻ボタン有効
            valParamToWSSubDialog.btn_sashimodoshiDisable = true;
            // 確認ボタン有効
            valParamToWSSubDialog.btn_kakuninDisable = true;
          }
        } else {
          if (row.btnDisable == 0) {
            // 差戻事由有効
            valParamToWSSubDialog.edit_sashimodoshiJiyuDisable = true;
            // 差戻ボタン有効
            valParamToWSSubDialog.btn_sashimodoshiDisable = true;
            // 確認ボタン有効
            valParamToWSSubDialog.btn_kakuninDisable = true;
          }
        }
      } else if (valParamToWSSubDialog.sintyoku_cd < "550") {
        // 作業実施日 開始
        valParamToWSSubDialog.sagyo_date.startDate =
          row.tblSagyoKanryobiStartDate;
        // 作業実施日 終了
        valParamToWSSubDialog.sagyo_date.endDate =
          row.tblSagyoKanryobi;
        // 作業実施日 終了引数用
        valParamToWSSubDialog.sagyoyotei_syuuryou_date =
          row.tblSagyoKanryobi;
        // 備考
        valParamToWSSubDialog.sagyou_yotei_bikou =
          row.tblSagyoKanryoBiko;

        valParamToWSSubDialog.sagyoyotei_kaisi_date =
          row.tblSagyoYoteibiStartDate;
        valParamToWSSubDialog.sagyoyotei_kaisi_time = row.tblSagyoYoteibiTime.substr(0, 5);
        valParamToWSSubDialog.sagyoyotei_syuuryou_date =
          row.tblSagyoYoteibiEndtDate;
        valParamToWSSubDialog.sagyoyotei_syuuryou_time = row.tblSagyoYoteibiTime.substr(6, 5);
        valParamToWSSubDialog.sagyokanryo_kaisi_date =
          row.tblSagyoKanryobiStartDate;
        valParamToWSSubDialog.sagyokanryo_syuuryou_date =
          row.tblSagyoKanryobi;
      } else {
        // 作業実施日 開始
        valParamToWSSubDialog.sagyo_date.startDate =
          row.tblSagyoKanryobiStartDate;
        // 作業実施日 終了
        valParamToWSSubDialog.sagyo_date.endDate =
          row.tblSagyoKanryobi;
        // 作業実施日 終了引数用
        valParamToWSSubDialog.sagyoyotei_syuuryou_date =
          row.tblSagyoKanryobi;
        // 差戻事由無効
        valParamToWSSubDialog.edit_sashimodoshiJiyuDisable = true;
        // 差戻ボタン無効
        valParamToWSSubDialog.btn_sashimodoshiDisable = true;
        // 確認ボタン無効
        valParamToWSSubDialog.btn_kakuninDisable = true;
        // 備考
        valParamToWSSubDialog.sagyou_yotei_bikou =
          row.tblSagyoKanryoBiko;

        valParamToWSSubDialog.sagyoyotei_kaisi_date =
          row.tblSagyoYoteibiStartDate;
        valParamToWSSubDialog.sagyoyotei_kaisi_time = row.tblSagyoYoteibiTime.substr(0, 5);
        valParamToWSSubDialog.sagyoyotei_syuuryou_date =
          row.tblSagyoYoteibiEndtDate;
        valParamToWSSubDialog.sagyoyotei_syuuryou_time = row.tblSagyoYoteibiTime.substr(6, 5);
        valParamToWSSubDialog.sagyokanryo_kaisi_date =
          row.tblSagyoKanryobiStartDate;
        valParamToWSSubDialog.sagyokanryo_syuuryou_date =
          row.tblSagyoKanryobi;
      }

      // 予定未入力の時予定開始時間に「～」が出るので空白に制御
      // 原因分かり次第処理をなくしていいと思います。
      if (valParamToWSSubDialog.sagyo_date.startTime == "～") {
        valParamToWSSubDialog.sagyo_date.startTime = "";
      }

      return valParamToWSSubDialog;
    },

    ///////////////////////////////////////////////
    // 差戻事由取得@@@@
    ///////////////////////////////////////////////
    fnc_void_getSashimodoshiJiyuInfo(row, column, rowIndex) {
      let _this = this;
      let letVals = new Object();

      if (
        row.shintyokuCode === "516" ||
        row.shintyokuCode === "518"
      ) {
        // 工事区分
        letVals.rdoKojiKubun = this.KensakuJoken.rdoKojiKubun;
        // 契約NO
        letVals.KeiyakuNo = row.keiyakuNo;
        // 履歴No
        letVals.RirekiNo = row.rirekiNo;
        // 明細NO
        letVals.MeisaiNo = row.meisaiNo;
        // 契約年月
        letVals.KeiyakuNengetsu = row.keiyakuNengetsu;
        // 契約年月明細NO
        letVals.KeiyakuNengetsuMeisai = row.keiyakuNengetsuMeisai;

        _this.http
          .post(
            "/api/Reafs_W/SagyoJyokyoKoshin/WS00411_GET_KensakuKekka",
            letVals
          )
          // 検索実行後処理
          .then(function(response) {
            if (response.data.length === 0) {
              _this.KeyDataToWS00411.sasimodori_ziyuu = "";
            } else {
              // 差戻事由
              _this.KeyDataToWS00411.sasimodori_ziyuu =
                response.data[0].SashimodoshiJiyu;
            }
          });

        return this.KeyDataToWS00411.sasimodori_ziyuu;
      } else if (
        row.shintyokuCode === "536" ||
        row.shintyokuCode === "538"
      ) {
        // 工事区分
        letVals.rdoKojiKubun = this.KensakuJoken.rdoKojiKubun;
        // 契約NO
        letVals.KeiyakuNo = row.keiyakuNo;
        // 履歴No
        letVals.RirekiNo = row.rirekiNo;
        // 明細NO
        letVals.MeisaiNo = row.meisaiNo;
        // 契約年月
        letVals.KeiyakuNengetsu = row.keiyakuNengetsu;
        // 契約年月明細NO
        letVals.KeiyakuNengetsuMeisai = row.keiyakuNengetsuMeisai;

        _this.http
          .post(
            "/api/Reafs_W/SagyoJyokyoKanryo/WS00412_GET_KensakuKekka",
            letVals
          )
          // 検索実行後処理
          .then(function(response) {
            if (response.data.length === 0) {
              _this.KeyDataToWS00412.sasimodori_ziyuu = "";
            } else {
              // 差戻事由
              _this.KeyDataToWS00412.sasimodori_ziyuu =
                response.data[0].SashimodoshiJiyu;
            }
          });

        return this.KeyDataToWS00412.sasimodori_ziyuu;
      } else {
        this.KeyDataToWS00411.sasimodori_ziyuu = "";
        this.KeyDataToWS00412.sasimodori_ziyuu = "";

        return "";
      }
    },

    ///////////////////////////////////////////////
    // 検索前エラーチェック
    ///////////////////////////////////////////////
    fnc_rtrn_kensakumaeErrorChk(KensakuJoken) {
      let targetDaysFlg = 0;

      // 対象月および対象日が空の場合
      if (
        this.KensakuJoken.dtpNengetsudo_from === null ||
        this.KensakuJoken.dtpNengetsudo_from === "" ||
        this.KensakuJoken.dtpNengetsudo_to === null ||
        this.KensakuJoken.dtpNengetsudo_to === ""
      ) {
        if (
          this.KensakuJoken.dtpStartDate === null ||
          this.KensakuJoken.dtpStartDate === "" ||
          this.KensakuJoken.dtpEndDate === null ||
          this.KensakuJoken.dtpEndDate === ""
        ) {
          this.fnc_void_showErrorMsg("E010333"); // 請求書月分を入力して下さい。
          this.isErrPinkNengetsudo = true; //背景色ピンク
          return false;
        }
      }

      // 対象月の範囲が逆の場合
      if (
        this.KensakuJoken.dtpNengetsudo_from >
        this.KensakuJoken.dtpNengetsudo_to
      ) {
        this.fnc_void_showErrorMsg("E040279"); // 日付を正しく入力して下さい。（開始≦終了）
        this.isErrPinkNengetsudo = true; //背景色ピンク
        return false;
      }

      if (this.KensakuJoken.dtpStartDate > this.KensakuJoken.dtpEndDate) {
        this.fnc_void_showErrorMsg("E040279"); // 日付を正しく入力して下さい。（開始≦終了）
        this.isErrPinkNengetsudo = true; //背景色ピンク
        return false;
      }

      console.log("ステータスチェック");
      // チェック済チェックボックスが存在しない場合
      if (
        this.KensakuJoken.chkSagyoYoteiMinyuryoku === false &&
        this.KensakuJoken.chkSagyoYoteiMikakunin === false &&
        this.KensakuJoken.chkSagyoMikanryo === false &&
        this.KensakuJoken.chkSagyoKanryoMikakunin === false &&
        this.KensakuJoken.chkSagyoKanryoKakuninzumi === false &&
        this.KensakuJoken.chkSagyoChusi === false
      ) {
        this.fnc_void_showErrorMsg("E040472"); // 検索対象の作業ステータスをチェックしてください。
        return false;
      }

      // 営業店が空の場合
      if (
        KensakuJoken.txtEigyotenCode != "" &&
        KensakuJoken.txtEigyoten === "" &&
        this.KensakuJoken.kidou_no == 0
      ) {
        this.fnc_void_showErrorMsg("E040009"); // 営業店を入力して下さい。
        this.isErrPinkEigyoten = true; //背景色ピンク
        this.$refs.ref営業店.focus();
        return false;
      }
      // 物件が空の場合
      if (
        KensakuJoken.txtBukkenmeiCode != "" &&
        KensakuJoken.txtBukkenmei === "" &&
        this.KensakuJoken.txtBukkenkubun == ""
      ) {
        this.fnc_void_showErrorMsg("E040481"); // 営業店を入力して下さい。
        this.isErrPinkBukken = true; //背景色ピンク
        this.$refs.ref物件.focus();
        return false;
      }

      return true;
    },

    ///////////////////////////////////////////////
    // keyからS016メッセージマスタにアクセスしメッセージ取得
    ///////////////////////////////////////////////
    fnc_rtrn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    ///////////////////////////////////////////////
    // エラーメッセージ出力
    ///////////////////////////////////////////////
    fnc_void_showErrorMsg(MsgCode) {
      this.fnc_rtrn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "作業状況一覧",
          message: response.data
        });
      });
    },

    ///////////////////////////////////////////////
    // 営業店データ取得
    ///////////////////////////////////////////////
    async reafsmodalBackEigyoten(e) {
      var next = true;
      this.KensakuJoken.txtEigyotenCode = e.txtCd;
      this.KensakuJoken.txtEigyoten = e.lblNm;
      if (this.KensakuJoken.txtEigyotenCode) {
        if (!(await this.Check営業店Exist())) {
          next = false;
          this.isErrPinkEigyoten = true;
        }
      } else {
        this.KensakuJoken.txtEigyoten = "";
        next = false;
        this.isErrPinkEigyoten = true;
      }

      if (next) {
        if (
          !this.名称取得方法17.find(
            x => x.営業所コード && x.営業所コード.substring(0, 4) === "9900"
          )
        ) {
          if (
            this.名称取得方法17 &&
            this.名称取得方法17.filter(
              x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7
            ).length > 0
          ) {
            var allPOSITIVE7Length = this.名称取得方法17.filter(
              x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7
            );
            var allRange = allPOSITIVE7Length.map(
              x => x.支社コード + x.支社コード枝番
            );
            this.http
              .post("/api/Reafs_W/Sagyo/get営業店CheckData", {
                lst支社コード: allRange,
                店コード: e.txtCd.substring(0, 4),
                店コード枝番: e.txtCd.substring(4, 7)
              })
              .then(response => {
                if (
                  !response.data.range名称取得方法18 ||
                  !response.data.range名称取得方法18.find(
                    x =>
                      x.店コード === e.txtCd.substring(0, 4) &&
                      x.店コード枝番 === e.txtCd.substring(4, 7)
                  )
                ) {
                  if (!response.data.店コード) {
                    this.KensakuJoken.txtEigyotenCode = "";
                    this.KensakuJoken.txtEigyoten = "";
                    this.fnc_void_showErrorMsg("E040662");
                  }
                }
              });
          } else {
            this.http
              .post("/api/Reafs_W/Sagyo/get営業店CheckData", {
                lst支社コード: [],
                店コード: e.txtCd.substring(0, 4),
                店コード枝番: e.txtCd.substring(4, 7)
              })
              .then(response => {
                if (!response.data.店コード) {
                  this.KensakuJoken.txtEigyotenCode = "";
                  this.KensakuJoken.txtEigyoten = "";

                  this.fnc_void_showErrorMsg("E040662");
                }
              });
          }
        }
      } else {
      }
    },

    Check営業店Exist() {
      return new Promise(resolve => {
        this.http
          .get("/api/Reafs_W/Sagyo/Check営業店Exist", {
            店コード: this.KensakuJoken.txtEigyotenCode
          })
          .then(response => {
            if (!response.data || response.data != 1) {
              // this.fnc_rtrn_getMsg("E040009")
              //   .then(response => {
              //     this.showError(response.data, () => {
              //       this.$refs.ref営業店.focus();
              //     });
              //   })
              //   .finally(() => {});
              return resolve(false);
            } else {
              return resolve(true);
            }
          });
      });
    },

    get営業店CheckData(allRange, 店コード, 店コード枝番) {
      return new Promise(resolve => {
        this.http
          .post("/api/Reafs_W/Sagyo/get営業店CheckData", {
            lst支社コード: allRange,
            店コード: 店コード,
            店コード枝番: 店コード枝番
          })
          .then(response => {
            return resolve(response);
          });
      });
    },

    ///////////////////////////////////////////////
    // 物件名データ取得
    ///////////////////////////////////////////////
    reafsmodalBackBukkenmei(obj) {
      // コード
      this.KensakuJoken.txtBukkenmeiCode = obj.txtCd;
      // 名称
      this.KensakuJoken.txtBukkenmei = obj.lblNm;
      // 区分
      this.KensakuJoken.txtBukkenkubun = obj.row.物件区分;
      // 郵便番号
      this.KensakuJoken.txtBukkenJushoCd = obj.row.郵便番号;
      // 住所
      if (this.KensakuJoken.txtBukkenmei != "") {
        this.KensakuJoken.txtBukkenJushoNm =
          obj.row.住所 + " " + obj.row.番地 + "番地 " + obj.row.号 + "号";
      } else {
        this.isErrPinkBukken = true; //背景色ピンク
        this.KensakuJoken.txtBukkenmeiCode = "";
        this.KensakuJoken.txtBukkenJushoNm = "";
      }
    },

    ///////////////////////////////////////////////
    // 業者名データ取得
    ///////////////////////////////////////////////
    backDataTorihikisaki(obj) {
      console.log("業者名検索サブ画面表示処理");
      var getCode = obj.txtCd.split("-");

      // 業者コード
      this.KensakuJoken.txtTehaiGyoshaCode = getCode[0];
      // 業者コード枝番
      this.KensakuJoken.txtTehaiGyoshaCodeEdaban = getCode[1];
      // 名称
      this.KensakuJoken.txtTehaiGyoshaName = obj.lblNm;
    },

    ///////////////////////////////////////////////
    // 戻るボタン押下
    ///////////////////////////////////////////////
    async onBack() {
      this.$router.push({ name: "WD00010" }); // home画面のname指定が異なるため
    }
  }
};
</script>

<style lang="less" scoped>
.home-contianer {
  padding: 10px;
  min-width: 1380px;
  height: 100%;
  // width: 1920px;
  // height: 1080px;
}

.sample-form-class {
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
.border {
  background-color: lightpink;
}
.el-checkbox {
  margin: 0 5px 0 20px;
}
.el-radio-group {
  margin: 0 0 0 5px;
}
.el-radio {
  margin-right: 15px;
}
.link_color {
  color: blue;
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
.pinkcolor_el_range_input .el-input__inner {
  background-color: lightpink !important;
}
/* 営業店エラーピンク */
.pinkcolor_eigyou >>> .el-input__inner {
  background-color: lightpink !important;
}
/* 物件エラーピンク */
.pinkcolor_bukken >>> .el-input__inner {
  background-color: lightpink !important;
}
.is-search {
  width: 56px;
}
</style>

<style>
.SagyojoukyoIchiran .text-left {
  text-align: left !important;
}
/* 2023/07/06 この設定とvirtualScrollOptionとの相性が悪い。 スクロールしてたら最終的になんも見えなくなる
   virtualScrollOptionはメモリエラーの対策なのでこっちを殺す。 動き、子画面表示など確認したが影響なし。 なんのために入れてた？*/
/* .SagyojoukyoIchiran .ve-table .ve-table-container table.ve-table-content {
  top: -1px !important;
} */

.SagyojoukyoIchiran .button-multi-code .reafs-input {
  display: none !important;
}

/* ハノイ側修正2022/11/08　課題管理表№206：「2022/11/07依頼分」「レイアウト調整依頼」START*/
.SagyoyoteiMeisai .el-dialog {
  overflow: auto;
}

.SagyoyoteiMeisai .el-dialog::-webkit-scrollbar-thumb {
  background-color: #ddd;
  height: 10px;
  border-radius: 5px;
}
/* ハノイ側修正2022/11/08　課題管理表№206：「2022/11/07依頼分」「レイアウト調整依頼」START*/

/* ハノイ側修正2022/11/08　課題管理表№207：「2022/11/07依頼分」「レイアウト調整依頼」START*/
.SagyoyoteiKanryoHoukoku .el-dialog {
  overflow: auto;
}

.SagyoyoteiKanryoHoukoku .el-dialog::-webkit-scrollbar-thumb {
  background-color: #ddd;
  height: 10px;
  border-radius: 5px;
}
/* ハノイ側修正2022/11/08　課題管理表№207：「2022/11/07依頼分」「レイアウト調整依頼」END*/
</style>
