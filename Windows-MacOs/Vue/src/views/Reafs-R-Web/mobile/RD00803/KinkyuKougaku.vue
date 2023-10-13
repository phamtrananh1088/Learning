<template>
  <div class="home-contianer">
    <el-form
      :model="dataForm"
      ref="dataForm"
      class="search-form-class"
      label-width="120px"
      :label-position="isMobileSized ? 'top' : 'left'"
    >
      <el-row>
        <el-col :span="21" :xs="24">
          <!-- layout  xs:<768px sm:>=768px md:>=992px lg:>=1200px xl:>=1920px-->
          <el-col :span="24" :xs="24">
            <!-- 工事依頼NO -->
            <el-form-item label="工事依頼No." >
              <label id="lblIraiNo">{{ dataForm.工事依頼No }}</label>
            </el-form-item>
            <!-- 依頼日時 -->
            <el-form-item label="依頼日時" >
              <label id="lblIraiDateTime">{{ dataForm.依頼日時 }}</label>
            </el-form-item>
            <!-- 営業店 -->
            <el-form-item label="営業店">
              <label id="lblEigyoTen">{{ dataForm.営業店 }}</label>
            </el-form-item>
            <!-- 依頼内容 -->
            <el-form-item label="依頼内容/状況" class="textarea-class">
              <!-- <label id="lblIraiNaiyo" >{{ dataForm.依頼内容_状況 }}</label> -->
              <el-input type="textarea" v-model="dataForm.依頼内容_状況"  :autosize="{ minRows: 3 }"></el-input>
            </el-form-item>
            <!-- 不具合場所 -->
            <el-form-item label="不具合場所" class="textarea-class">
              <!-- <label id="lblFuguaiBasho">{{ dataForm.不具合場所 }}</label> -->
               <el-input type="textarea" v-model="dataForm.不具合場所" ></el-input>
            </el-form-item>
            <!-- メーカー名 -->
            <el-form-item label="メーカー名" class="textarea-class">
              <!-- <label id="lblMakerNm">{{ dataForm.メーカー名 }}</label> -->
              <el-input type="textarea" v-model="dataForm.メーカー名" ></el-input>
            </el-form-item>
            <!-- 依頼状況 -->
            <el-form-item label="依頼状況" class="textarea-class" :readonly="true">
              <!-- <label id="lblIraiStatus">{{ dataForm.依頼状況 }}</label> -->
              <el-input type="textarea" v-model="dataForm.依頼状況" ></el-input>
            </el-form-item>
            <!-- 工事区分 -->
            <el-form-item label="工事区分">
              <el-select
                style="width: 320px"
                v-model="dataForm.工事区分コード"
              >
                <el-option
                  v-for="item in koujiKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 大区分 -->
            <el-form-item label="大区分">
              <el-select
                style="width: 320px"
                v-model="dataForm.大区分コード"
                @change="dKbnSelectChanged()"
              >
                <el-option
                  v-for="item in dKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 中区分 -->
            <el-form-item label="中区分">
              <el-select
                style="width: 320px"
                v-model="dataForm.中区分コード"
                @change="cKbnSelectChanged()"
              >
                <el-option
                  v-for="item in cKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 小区分 -->
            <el-form-item label="小区分">
              <el-select style="width: 320px" v-model="dataForm.小区分コード" @change="sKbnSelectChanged()">
                <el-option
                  v-for="item in sKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>

            <!-- 工事名称 -->
            <el-form-item label="工事名称"  class="textarea-class">
              <!-- <label id="lblKoujiNm">{{ dataForm.工事名称 }}</label> -->
              <el-input type="textarea" v-model="dataForm.工事名称" ></el-input>
            </el-form-item>
            <!-- 依頼者 -->
            <el-form-item label="依頼者">
              <label id="lblIraiShaNm">{{ dataForm.依頼者 }}</label>
            </el-form-item>
            <!-- 連絡先 -->
            <el-form-item label="連絡先">
              <label id="lblRenraKusen">{{ dataForm.連絡先 }}</label>
            </el-form-item>
            <!-- 依頼者（代行） -->
            <el-form-item label="依頼者（代行）">
              <label id="lblIraiShaDaiNm">{{ dataForm.依頼者_代行 }}</label>
            </el-form-item>
            <!-- 連絡先（代行） -->
            <el-form-item label="連絡先（代行）">
              <label id="lblRenraKusenDai">{{ dataForm.連絡先_代行 }}</label>
            </el-form-item>
            <!-- 修繕区分大 -->
            <el-form-item label="修繕区分大">
              <el-select
                style="width: 320px"
                v-model="dataForm.修繕区分大コード"
                @change="sDaiKbnSelectChanged()"
              >
                <el-option
                  v-for="item in sDaiKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 修繕区分小 -->
            <el-form-item label="修繕区分小">
              <el-select
                style="width: 320px"
                v-model="dataForm.修繕区分小コード"
              >
                <el-option
                  v-for="item in sShouKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 負担区分 -->
            <!-- <el-form-item label="負担区分">
              <label id="lblFutanKbn">{{ dataForm.負担区分 }}</label>
            </el-form-item> -->
            <el-form-item label="負担区分">
              <el-select
                style="width: 320px"
                v-model="dataForm.負担区分コード"
                @change="fKbnSelectChanged()"
              >
                <el-option
                  v-for="item in fKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 管理区分 -->
            <el-form-item label="管理区分">
              <el-select
                style="width: 320px"
                v-model="dataForm.管理区分コード"
                @change="kanriKbnSelectChanged()"
              >
                <el-option
                  v-for="item in kanriKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 支払区分 -->
            <el-form-item label="支払区分">
              <el-select
                style="width: 320px"
                v-model="dataForm.支払区分コード"
                @change="shiKbnSelectChanged()"
              >
                <el-option
                  v-for="item in shiKbnSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 支払条件 -->
            <el-form-item label="支払条件">
              <el-select
                style="width: 320px"
                v-model="dataForm.支払条件コード"
                @change="shiJoukenSelectChanged()"
              >
                <el-option
                  v-for="item in shiJoukenSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 事業計画 -->
            <el-form-item label="事業計画">
              <el-select
                style="width: 320px"
                v-model="dataForm.事業計画コード"
                @change="jigyouSelectChanged()"
              >
                <el-option
                  v-for="item in jigyouSelectItems"
                  :key="item.名称コード"
                  :value="item.名称コード"
                  :label="item.名称コード + ' ' + item.名称"
                >
                  <span>{{ item.名称コード }} {{ item.名称 }}</span>
                </el-option>
              </el-select>
            </el-form-item>
            <!-- 選定取引先1 -->
            <el-form-item label="選定取引先1" prop="取引先コード1" class="textarea-class">
              <opensearch
                v-model="dataForm.取引先コード1"
                :appendlabelText="dataForm.取引先名称1"
                :maxlength="'10'"
                :append="'append'"
                :width="'120px'"
                :appendlabelWidth="isMobileSized ? '70px' : ''"
                :path="'reafsmodalSearchTorihikisaki'"
                :subParamsObj="this.SetsubParamsObjTorihikisaki1()"
                :inputClass = "'moblie-input-append'"
                @backData="reafsmodalBack_Torihikisaki1"
                :disabled="(ShoninInfo.syouninSyuruiSelectVal == '12' )"
              >
              </opensearch>
            </el-form-item>
            <!-- 選定取引先2 -->
            <el-form-item label="選定取引先2" prop="取引先コード2" class="textarea-class">
              <opensearch
                v-model="dataForm.取引先コード2"
                :appendlabelText="dataForm.取引先名称2"
                :maxlength="'10'"
                :append="'append'"
                :width="'120px'"
                :appendlabelWidth="isMobileSized ? '70px' : ''"
                :path="'reafsmodalSearchTorihikisaki'"
                :subParamsObj="this.SetsubParamsObjTorihikisaki2()"
                :inputClass = "'moblie-input-append'"
                @backData="reafsmodalBack_Torihikisaki2"
                :disabled="(ShoninInfo.syouninSyuruiSelectVal == '12' )"
              >
              </opensearch>
            </el-form-item>
            <!-- 選定取引先3 -->
            <el-form-item label="選定取引先3" prop="取引先コード3" class="textarea-class">
              <opensearch
                v-model="dataForm.取引先コード3"
                :appendlabelText="dataForm.取引先名称3"
                :maxlength="'10'"
                :append="'append'"
                :width="'120px'"
                :appendlabelWidth="isMobileSized ? '70px' : ''"
                :path="'reafsmodalSearchTorihikisaki'"
                :subParamsObj="this.SetsubParamsObjTorihikisaki3()"
                :inputClass = "'moblie-input-append'"
                @backData="reafsmodalBack_Torihikisaki3"
                :disabled="(ShoninInfo.syouninSyuruiSelectVal == '12' )"
              >
              </opensearch>
            </el-form-item>
            <!-- 概算金額 -->
            <el-form-item label="概算金額">
              <label id="lblGaisaningaku" v-if="dataForm.概算金額 != 0">{{ dataForm.概算金額 }}</label>
            </el-form-item>
            <el-form-item label="見積金額ﾒｰﾙ通知">
              <el-radio-group
                v-model="dataForm.見積金額メール通知"
                :disabled="((dataForm.MailChangeFlg != '0')
                          ||!(dataForm.修繕区分小コード == '02' && (dataForm.負担区分コード =='2')))"
              >
                <el-radio :label="'1'">高額承認時</el-radio>
                <!-- <el-radio :label="'2'">緊急承認時</el-radio> -->
                <el-radio :label="'2'">購買申請時</el-radio>
              </el-radio-group>
            </el-form-item>
            <!-- 得意先 -->
            <el-form-item label="得意先" prop="得意先コード" class="textarea-class">
              <opensearch
                v-model="dataForm.得意先コード"
                :appendlabelText="dataForm.得意先名称"
                :maxlength="'10'"
                :append="'append'"
                :width="'120px'"
                :appendlabelWidth="isMobileSized ? '70px' : ''"
                :path="'reafsmodalSearchTorihikisaki'"
                :subParamsObj="this.SetsubParamsObjTokuisaki()"
                :inputClass = "'moblie-input-append'"
                @backData="reafsmodalBack_Tokuisaki"
              >
              </opensearch>
            </el-form-item>
          </el-col>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="21" :xs="24">
          <el-col :span="18" :xs="24">
            <el-form-item label-width="100%"
              label="写真"
            >            </el-form-item>
              <el-image
                :src="linkImage1"
                fit="contain"
              >
                <div slot="error" class="image-slot">
                  <span>写真なし</span>
                </div>
              </el-image>
              <el-input
                placeholder="コメント"
                :readonly="true"
                style="margin-bottom: 10px"
                v-model="dataForm.状況コメント1"
              ></el-input>
              <el-image
                :src="linkImage2"
                fit="contain"
              >
                <div slot="error" class="image-slot">
                  <span>写真なし</span>
                </div>
              </el-image>
              <el-input
                placeholder="コメント"
                :readonly="true"
                style="margin-bottom: 10px"
                v-model="dataForm.状況コメント2"
              >
              </el-input>
              <el-image
                :src="linkImage3"
                fit="contain"
              >
                <div slot="error" class="image-slot">
                  <span>写真なし</span>
                </div>
              </el-image>
              <el-input
                placeholder="コメント"
                :readonly="true"
                style="margin-bottom: 10px"
                v-model="dataForm.状況コメント3"
              >
              </el-input>
              <el-image
                :src="linkImage4"
                fit="contain"
              >
                <div slot="error" class="image-slot">
                  <span>写真なし</span>
                </div>
              </el-image>
              <el-input
                placeholder="コメント"
                :readonly="true"
                style="margin-bottom: 10px"
                v-model="dataForm.状況コメント4"
              ></el-input>

          </el-col>
        </el-col>
      </el-row>
      <el-row style="margin-top: 50px">
        <el-button type="primary" size="mini" @click="sashiModoshi_Click()"
         :disabled="dataForm.承認区分 != ' '"
          >差戻</el-button
        >
        <el-button
          v-if="(ShoninInfo.syouninSyuruiSelectVal == '11' )"
          type="primary"
          size="mini"
          @click="hinin_Click()"
          :disabled="dataForm.承認区分 != ' '"
          >否認</el-button
        >
        <el-button type="primary" size="mini" @click="shonin_Click()"
          :disabled="dataForm.承認区分 != ' '"
          >承認</el-button
        >
        <el-button type="primary" size="mini" style="float:right" @click="pageback_Click()"
          >戻る</el-button
        >
      </el-row>
    </el-form>

    <el-dialog
      :visible.sync="dialogVisible"
      width="95%"
      :modal="true"
      :close-on-click-modal="false"
      @close="hideModal"
    >
      <span>否認、差戻事由</span>
      <reafsinputtext
        v-model="dataForm.否認差戻事由"
        :width="'100%'"
        maxlength="200'"
      ></reafsinputtext>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" size="mini" @click="toroku_Click()"
          >登 録</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import opensearch from "@/components/basic/openSearch.vue";
var $thisValue;
const collapseWidth = 768;

export default {
  components: {
    reafsinputtext,
    opensearch,
  },

  data() {
    return {
      menuInfo: {},
      dataForm: {
        依頼日時: "",
        依頼日: "",
        営業店: "",
        依頼内容_状況: "",
        不具合場所: "",
        メーカー名: "",
        依頼状況: "",
        工事区分コード: "",
        大区分コード: "",
        中区分コード: "",
        小区分コード: "",
        依頼者: "",
        連絡先: "",
        依頼者_代行: "",
        連絡先_代行: "",
        修繕区分大コード: "",
        修繕区分小コード: "",
        負担区分コード: "",
        管理区分コード: "",
        支払区分コード: "",
        支払条件コード: "",
        事業計画コード: "",
        // 取引先コード: "",
        // 取引先コード枝番: "",
        // 取引先名称: "",
        取引先コード_高額: "",
        取引先コード枝番_高額: "",
        取引先名称_高額: "",
        選定取引先FLG: "",
        取引先コード1: "",
        取引先コード枝番1: "",
        取引先名称1: "",
        取引先名称1: "",
        REAFST使用1: "",
        WEB合意1 : "",
        取引先コード2: "",
        取引先コード枝番2: "",
        取引先名称2: "",
        REAFST使用2: "",
        WEB合意2 : "",
        取引先コード3: "",
        取引先コード枝番3: "",
        取引先名称3: "",
        REAFST使用3: "",
        WEB合意3 : "",
        概算金額: "",
        得意先コード: "",
        得意先コード枝番: "",
        得意先名称: "",
        状況コメント1: "",
        状況コメント2: "",
        状況コメント3: "",
        状況コメント4: "",
        否認差戻事由: "",
        負担区分: "",
        見積金額メール通知: "",
        MailChangeFlg: "", //見積金額メール通知 0:選択可能 1:選択不可
        最終承認フラグ: "",
        承認区分: "",
        継続区分: "",
        修繕区分大: "",
        修繕区分小: "",
        物件コード: "",
        表示件名: "",
        工事名称: "",
        会社コード: "",
        工事依頼No: "", // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」
        工事依頼No枝番: "",
        F100VERSION: "",
        見積依頼フラグ: ""
      },
      linkImage1: "",
      linkImage2: "",
      linkImage3: "",
      linkImage4: "",
      
      koujiKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      dKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      cKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      sKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      sDaiKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      sShouKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      fKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      kanriKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      shiKbnSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      shiJoukenSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      jigyouSelectItems: [
        {
          名称コード: "",
          名称: "－",
        },
      ],
      subParamsObj: {
        taisyou: "1", //承認種類　 取引先検索：対象
        jigyousyoCd: "110", //事業所コード
      },
      koujiKbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      dkbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      ckbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      skbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      sDaikbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      sShoukbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      fkbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      kanrikbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      shikbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      shiJoukenInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      jigyouInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },
      ShoninInfo: {
        kojiiraiNo: "", //工事依頼№
        kojiiraiNoEdaban: "", //工事依頼NO枝番
        syouninSyuruiSelectVal: "", //承認種類
      },

      dialogVisible: false,
      btnKbn: "", //0:差戻ボタン押下 1:否認ボタン押下  2:承認ボタン押下

      isMobileSized: false,
      宛先事務所コード: [],
      宛先営業所コード: [],
      宛先部コード: [],
      宛先課コード: [],
      宛先係コード: [],
      // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」Start
      paramF044: {
        工事依頼No: '',
        工事依頼NO枝番: '',
        業者コード: '',
        業者コード枝番: ''
      }
      // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」End
    };
  },
  inject: ['setPageTitle'],
  watch: {
    $route(to, from) {
      if (to.name == "RD00803") {
        this.init();
      }
    }
  },

  created() {
    this.init()
  },

  methods: {
    init() {
      $thisValue = this
      // 大区分リスト作成
      this.getdKbnSelect()
      // 工事区分リスト作成
      this.getkoujiKbnSelect()
      // 修繕区分大リスト作成
      this.getsDaiKbnSelect()
      // 負担区分リスト作成
      this.getfKbnSelect()
      // 管理区分リスト作成
      this.getkanriKbnSelect()
      // 支払区分リスト作成
      this.getshiKbnSelect()
      // 支払条件リスト作成
      this.getshiJoukenSelect()
      // 事業計画リスト作成
      this.getjigyouSelect()
      
      // データ取得
      this.getData()
    },
    //初期データ取得
    async getData() {
      // 直接起動の場合
      this.ShoninInfo.kojiiraiNo = this.$route.params.工事依頼NO
      this.ShoninInfo.kojiiraiNoEdaban = this.$route.params.工事依頼NO枝番
      this.ShoninInfo.syouninSyuruiSelectVal = this.$route.params.種類
      let qs = require("qs")

      await this.http
        .post(
          "api/Reafs_R/Mobile/Shonin/RD00803/getMeisaiRD00803",
          qs.stringify(this.ShoninInfo)
        )
        .then((response) => {
          if (!response.status || response.data.data == null) {
            this.dataClear();
            this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            })
            this.$router.push({
              name: "RD00802",
            })
            return
          }
          this.dataForm = response.data.data
          let ipAddress = $thisValue.http.ipAddress
          this.subParamsObj.taisyou = response.data.data.負担区分コード
          this.linkImage1 = ipAddress + response.data.data.linkImage1
          this.linkImage2 = ipAddress + response.data.data.linkImage2
          this.linkImage3 = ipAddress + response.data.data.linkImage3
          this.linkImage4 = ipAddress + response.data.data.linkImage4

          let lst名称取得方法No64 = response.data.data名称取得方法No64
          if (lst名称取得方法No64.length > 0) {
            this.resetParam()
            for (let i = 0; i < lst名称取得方法No64.length; i++) {
              this.宛先事務所コード.push(lst名称取得方法No64[i].事務所コード)
              this.宛先営業所コード.push(lst名称取得方法No64[i].営業所コード)
              this.宛先部コード.push(lst名称取得方法No64[i].部コード)
              this.宛先課コード.push(lst名称取得方法No64[i].課コード)
              this.宛先係コード.push(lst名称取得方法No64[i].係コード)
            }
          } else {
            this.resetParam()
          }
          this.getcKbnSelect()
          this.getsKbnSelect()
          this.getsShouKbnSelect()
          
          this.getSenteiTorihikisaki()

          // 画面タイトル変更
          Object.assign(this.menuInfo, {SubMenuName: this.$store.getters.getPageTitle()})
          if (this.ShoninInfo.syouninSyuruiSelectVal == '11') {
            this.setPageTitle('緊急承認')
          } else if (this.ShoninInfo.syouninSyuruiSelectVal == '12') {
            this.setPageTitle('高額承認')
          }
        })
    },
    resetParam () {
      this.宛先事務所コード = []
      this.宛先営業所コード = []
      this.宛先部コード = []
      this.宛先課コード = []
      this.宛先係コード = []
    },
    // // 取引先　戻すデータ
    // reafsmodalBack_Torihikisaki (obj) {
    //   // 戻り値：「取引先コード」[取引先コード枝番]「取引先名」「郵便番号」「住所」
    //   //  cd: row.取引先コード,
    //   //       nm: row.取引先名,
    //   //       yuubin: row.郵便番号,
    //   //       juusyo: row.住所,
    //   this.dataForm.取引先コード = obj.txtCd;
    //   this.dataForm.取引先名称 = obj.lblNm;
    //   this.dataForm.取引先コード枝番 = obj.row.枝番;
    // },
    // SetsubParamsObjTorihikisaki(obj) {
    //   let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj))
    //   tempSubParamsObj.torType = ""
    //   tempSubParamsObj.taisyou = ""
    //   return tempSubParamsObj
    // },
    // 取引先1　戻すデータ
    reafsmodalBack_Torihikisaki1 (obj) {
      // 戻り値：「取引先コード」[取引先コード枝番]「取引先名」「郵便番号」「住所」
      //  cd: row.取引先コード,
      //       nm: row.取引先名,
      //       yuubin: row.郵便番号,
      //       juusyo: row.住所,
      this.dataForm.取引先コード1 = obj.txtCd;
      this.dataForm.取引先名称1 = obj.lblNm;
      this.dataForm.取引先コード枝番1 = obj.row.枝番;
    },
    SetsubParamsObjTorihikisaki1(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj))
      tempSubParamsObj.torType = ""
      tempSubParamsObj.taisyou = ""
      return tempSubParamsObj
    },
    // 取引先2　戻すデータ
    reafsmodalBack_Torihikisaki2 (obj) {
      // 戻り値：「取引先コード」[取引先コード枝番]「取引先名」「郵便番号」「住所」
      //  cd: row.取引先コード,
      //       nm: row.取引先名,
      //       yuubin: row.郵便番号,
      //       juusyo: row.住所,
      this.dataForm.取引先コード2 = obj.txtCd;
      this.dataForm.取引先名称2 = obj.lblNm;
      this.dataForm.取引先コード枝番2 = obj.row.枝番;
    },
    SetsubParamsObjTorihikisaki2(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj))
      tempSubParamsObj.torType = ""
      tempSubParamsObj.taisyou = ""
      return tempSubParamsObj
    },
    // 取引先3　戻すデータ
    reafsmodalBack_Torihikisaki3 (obj) {
      // 戻り値：「取引先コード」[取引先コード枝番]「取引先名」「郵便番号」「住所」
      //  cd: row.取引先コード,
      //       nm: row.取引先名,
      //       yuubin: row.郵便番号,
      //       juusyo: row.住所,
      this.dataForm.取引先コード3 = obj.txtCd;
      this.dataForm.取引先名称3 = obj.lblNm;
      this.dataForm.取引先コード枝番3 = obj.row.枝番;
    },
    SetsubParamsObjTorihikisaki3(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj))
      tempSubParamsObj.torType = ""
      tempSubParamsObj.taisyou = ""
      return tempSubParamsObj
    },

    SetsubParamsObjTokuisaki(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj))
      tempSubParamsObj.torType = "1"
      tempSubParamsObj.taisyou = ""
      return tempSubParamsObj
    },
    // 得意先　戻すデータ
    reafsmodalBack_Tokuisaki (obj) {
      // 戻り値：「得意先コード」[得意先コード枝番]「得意先名」「郵便番号」「住所」
      //  cd: row.得意先コード,
      //       nm: row.得意先名,
      //       yuubin: row.郵便番号,
      //       juusyo: row.住所,
      this.dataForm.得意先コード = obj.txtCd;
      this.dataForm.得意先名称 = obj.lblNm;
      this.dataForm.得意先コード枝番 = obj.row.枝番;
    },

    dKbnSelectChanged() {
      this.cKbnSelectItems = [{ 名称コード: "", 名称: "－" }]
      this.sKbnSelectItems = [{ 名称コード: "", 名称: "－" }]
      this.dataForm.中区分コード = ""
      this.dataForm.小区分コード = ""

      this.getcKbnSelect()
    },
    // 中区分変更時イベント
    cKbnSelectChanged() {
      this.sKbnSelectItems = [{ 名称コード: "", 名称: "－" }]
      this.dataForm.小区分コード = ""

      this.getsKbnSelect()
    },
    // 小区分変更時イベント
    sKbnSelectChanged() {
      // 選定取引先取得
      this.getSenteiTorihikisaki()
    },

    sDaiKbnSelectChanged() {
      this.sShouKbnSelectItems = [{ 名称コード: "", 名称: "－" }]
      this.dataForm.修繕区分小コード = ""
      this.getsShouKbnSelect()
    },
    getSenteiTorihikisaki() {
      // 選定取引先が未入力の場合設定する
      // ただしF044が存在する場合は処理しない
      console.log('選定取引先FLG:'+this.dataForm.選定取引先FLG)
      if ( this.dataForm.選定取引先FLG == '0') {

      this.dataForm.取引先コード1=''
      this.dataForm.取引先コード2=''
      this.dataForm.取引先コード3=''
      this.dataForm.業者コード枝番1=''
      this.dataForm.業者コード枝番2=''
      this.dataForm.業者コード枝番3=''
      this.dataForm.取引先名称1 = ''
      this.dataForm.取引先名称2 = ''
      this.dataForm.取引先名称3 = ''
      this.dataForm.REAFST使用1=''
      this.dataForm.REAFST使用2=''
      this.dataForm.REAFST使用3=''
      this.dataForm.WEB合意1=''
      this.dataForm.WEB合意2=''
      this.dataForm.WEB合意3=''      

      const param = {
        会社コード: this.dataForm.会社コード,
        物件コード: this.dataForm.物件コード,
        大区分コード: this.dataForm.大区分コード,
        中区分コード: this.dataForm.中区分コード,
        小区分コード: this.dataForm.小区分コード
      };
      this.http
        .post("api/Reafs_R/Mobile/Shonin/RD00803/GetSenteiTorihikisakiRD00803", param)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          if((response.data.業者コード1 != null && response.data.業者コード1 != '')){
            this.dataForm.取引先コード1 = response.data.業者コード1+'-'+response.data.業者コード枝番1
            this.dataForm.取引先コード枝番1 = response.data.業者コード枝番1
            this.dataForm.取引先名称1 = response.data.業者名1
            this.dataForm.REAFST使用1 = response.data.REAFST使用1
            this.dataForm.WEB合意1 = response.data.WEB合意1
          }
          if((response.data.業者コード2 !=null && response.data.業者コード2 != '')){
            this.dataForm.取引先コード2 = response.data.業者コード2+'-'+response.data.業者コード枝番2
            this.dataForm.取引先コード枝番2 = response.data.業者コード枝番2
            this.dataForm.取引先名称2 = response.data.業者名2
            this.dataForm.REAFST使用2 = response.data.REAFST使用2
            this.dataForm.WEB合意2 = response.data.WEB合意2
          }
          if((response.data.業者コード3 !=null && response.data.業者コード3 != '')){
            this.dataForm.取引先コード3 = response.data.業者コード3+'-'+response.data.業者コード枝番3
            this.dataForm.取引先コード枝番3 = response.data.業者コード枝番3
            this.dataForm.取引先名称3 = response.data.業者名3
            this.dataForm.REAFST使用3 = response.data.REAFST使用3
            this.dataForm.WEB合意3 = response.data.WEB合意3
          }
        });
      }
    },
    getkoujiKbnSelect() {
      this.koujiKbnInfo.strParam1 = "003"; //工事区分の場合
      this.koujiKbnInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.koujiKbnInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }          
          this.koujiKbnSelectItems = response.data;
          
          this.koujiKbnSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },

    getdKbnSelect() {
      this.dkbnInfo.strParam1 = "004"; //大区分の場合
      this.dkbnInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.dkbnInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.dKbnSelectItems = response.data;
          this.dKbnSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },

    getcKbnSelect() {
      if (this.dataForm.大区分コード.length > 0) {
        this.ckbnInfo.strParam1 = "005"; //中区分の場合
        this.ckbnInfo.strParam2 = this.dataForm.大区分コード; //大区分コード
        this.ckbnInfo.intParam1 = 0; //削除フラグ

        this.http
          .post("/api/Reafs_W/Master/selectM018", this.ckbnInfo)
          .then((response) => {
            if (!response.status) {
              return this.MsgInfo({
                title: this.$store.getters.getPageTitle(),
                message: response.message,
              });
            }
            this.cKbnSelectItems = response.data;
            this.cKbnSelectItems.unshift({
              名称コード: "",
              名称: "－",
            });
          });
      }
    },

    getsKbnSelect() {
      if (
        this.dataForm.大区分コード.length > 0 &&
        this.dataForm.中区分コード.length > 0
      ) {
        this.skbnInfo.strParam1 = "006"; //小区分の場合
        this.skbnInfo.strParam2 = this.dataForm.大区分コード; //大区分コード
        this.skbnInfo.strParam3 = this.dataForm.中区分コード; //中区分コード
        this.skbnInfo.intParam1 = 0; //削除フラグ

        this.http
          .post("/api/Reafs_W/Master/selectM018", this.skbnInfo)
          .then((response) => {
            if (!response.status) {
              return this.MsgInfo({
                title: this.$store.getters.getPageTitle(),
                message: response.message,
              });
            }
            this.sKbnSelectItems = response.data;
            this.sKbnSelectItems.unshift({
              名称コード: "",
              名称: "－",
            });
          });
      }
    },

    getsDaiKbnSelect() {
      this.sDaikbnInfo.strParam1 = "009"; //修繕区分大の場合
      this.sDaikbnInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.sDaikbnInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.sDaiKbnSelectItems = response.data;
          this.sDaiKbnSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },

    getsShouKbnSelect() {
      if(this.dataForm.修繕区分大コード==null){return}
      if (this.dataForm.修繕区分大コード.length > 0) {
        console.log('getsShouKbnSelect:'+this.dataForm.修繕区分大コード)
        this.sShoukbnInfo.strParam1 = "010"; //修繕区分小の場合
        this.sShoukbnInfo.strParam2 = this.dataForm.修繕区分大コード; //修繕区分大コード
        this.sShoukbnInfo.intParam1 = 0; //削除フラグ

        this.http
          .post("/api/Reafs_W/Master/selectM018", this.sShoukbnInfo)
          .then((response) => {
            if (!response.status) {
              return this.MsgInfo({
                title: this.$store.getters.getPageTitle(),
                message: response.message,
              });
            }
            this.sShouKbnSelectItems = response.data;
            this.sShouKbnSelectItems.unshift({
              名称コード: "",
              名称: "－",
            });
          });
      }
    },

    getfKbnSelect() {
      this.fkbnInfo.strParam1 = "011"; //負担区分の場合
      this.fkbnInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.fkbnInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.fKbnSelectItems = response.data;
          this.fKbnSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },

    getkanriKbnSelect() {
      this.kanrikbnInfo.strParam1 = "012"; //管理区分の場合
      this.kanrikbnInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.kanrikbnInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.kanriKbnSelectItems = response.data;
          this.kanriKbnSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },

    getshiKbnSelect() {
      this.shikbnInfo.strParam1 = "013"; //支払区分の場合
      this.shikbnInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.shikbnInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.shiKbnSelectItems = response.data;
          this.shiKbnSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },

    getshiJoukenSelect() {
      this.shiJoukenInfo.strParam1 = "014"; //支払条件の場合
      this.shiJoukenInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.shiJoukenInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.shiJoukenSelectItems = response.data;
          this.shiJoukenSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },

    getjigyouSelect() {
      this.jigyouInfo.strParam1 = "016"; //事業計画の場合
      this.jigyouInfo.intParam1 = 0;

      this.http
        .post("/api/Reafs_W/Master/selectM018", this.jigyouInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.jigyouSelectItems = response.data;
          this.jigyouSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },
    checkVersion () {
      return new Promise((resolve) => {
      const param = {
        会社コード: this.dataForm.会社コード,
        工事依頼No: this.ShoninInfo.kojiiraiNo
      }
        this.http
          .post('/api/Reafs_R/Mobile/Shonin/RD00803/CheckF100Version', param)
          .then((response) => {
            if (this.dataForm.F100VERSION&& (this.dataForm.F100VERSION !== response.data.version || !response.data.version)) {
              resolve(false)
            } else {
              resolve(true)
            }
          })
      })
    },
    //差戻ボタン押下時処理
    sashiModoshi_Click() {
      this.btnKbn = "0"; //0:差戻ボタン押下 1:否認ボタン押下  2:承認ボタン押下
      this.dialogVisible = true;
    },

    //否認ボタン押下時処理
    hinin_Click() {
      this.btnKbn = "1"; //0:差戻ボタン押下 1:否認ボタン押下  2:承認ボタン押下
      this.dialogVisible = true;
    },

    //承認ボタン押下時処理
    shonin_Click() {
      this.btnKbn = "2"; //0:差戻ボタン押下 1:否認ボタン押下  2:承認ボタン押下
      this.toroku_Click();
    },

    //登録ボタン押下時処理
    async toroku_Click() {
      let s種類 = this.ShoninInfo.syouninSyuruiSelectVal; //12: 高額承認時 11: 緊急承認時
      this.resetParamF044()
      // 否認差戻事由チェック
      if (this.btnKbn != "2") {
        if (!this.HininSashimodoshi_Check(s種類)) {
          this.dialogVisible = true;
          return;
        }
      }
      // Version チェック
      if (!await this.checkVersion()) {
         this.MsgErr({
          title: this.$store.getters.getPageTitle(),
          message: '他の端末で更新が発生しました。再度読込んで下さい。',
        });
        this.dialogVisible = false;
        return;
      }

      // //緊急承認の場合Confirm
      // let skojiiraiNo = this.ShoninInfo.kojiiraiNo; //工事依頼№
      // let sMsgConf;
      // let sMsg1 = "工事依頼№＝"; //M040004
      // let sMsg2;

      // if (this.btnKbn == "0") {
      //   sMsgConf = "緊急依頼を差し戻しますがよろしいですか？"; //K040016
      //   sMsg2 = "差戻しました。"; //M040014
      // } else if (this.btnKbn == "1") {
      //   sMsgConf = "緊急依頼を否認しますがよろしいですか？"; //K040017
      //   sMsg2 = "否認しました。"; //M040013
      // } else if (this.btnKbn == "2") {
      //   sMsgConf = "緊急依頼を承認しますがよろしいですか？"; //K040018
      //   sMsg2 = "承認しました。"; //M040010
      // }

      if (s種類 == "11") {
        //緊急承認の場合Confirm
        let skojiiraiNo = this.ShoninInfo.kojiiraiNo; //工事依頼№
        let sMsgConf;
        let sMsgConf2;
        let sMsg1 = "工事依頼№＝"; //M040004
        let sMsg2;
        let sMsg3;

        if (this.btnKbn == "0") {
          sMsgConf = "緊急依頼を差し戻しますがよろしいですか？"; //K040016
          sMsg2 = "差戻しました。"; //M040014
        } else if (this.btnKbn == "1") {
          sMsgConf = "緊急依頼を否認しますがよろしいですか？"; //K040017
          sMsg2 = "否認しました。"; //M040013
        } else if (this.btnKbn == "2") {
          sMsgConf = "緊急依頼を承認しますがよろしいですか？"; //K040018
          sMsg2 = "承認しました。"; //M040010

          sMsgConf2 = "見積依頼しますがよろしいですか？"; //K040050
          sMsg3 = "依頼しました。"; //M040027
        }
        if (await this.confirm(sMsgConf,this.$store.getters.getPageTitle())) {
          if(await this.UpdateData(sMsg1, sMsg2)) {
            if (this.btnKbn == "0") {
              await this.call_sendMail('差戻', this.ShoninInfo.kojiiraiNo)
            }
            // 見積依頼
            if (this.btnKbn == "2") {
              if (await this.confirm(sMsgConf2,this.$store.getters.getPageTitle())) {
                this.dataForm.見積依頼フラグ = '1'
                if(await this.UpdateData(sMsg1, sMsg3)) {
                  await this.call_sendMail('緊急承認', this.ShoninInfo.kojiiraiNo)
                  await this.twilioCall() // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」
                  this.getData()
                }else{
                  this.resetParamF044()
                  this.getData()
                }
              }else{
                  this.getData()
              }
            }else{
              this.getData()
            }
          }
        } 
      }      

      if (s種類 == "12") {
        //高額承認の場合Confirm
        let skojiiraiNo = this.ShoninInfo.kojiiraiNo; //工事依頼№
        let sMsgConf;
        let sMsg1 = "工事依頼№＝"; //M040004
        let sMsg2;

        if (this.btnKbn == "0") {
          sMsgConf = "高額差戻しますがよろしいですか？"; //K040074
          sMsg2 = "差戻しました。"; //M040014
        } else if (this.btnKbn == "1") {
          // sMsgConf = "高額否認しますがよろしいですか？"; //K040017
          // sMsg2 = "否認しました。"; //M040013
          return
        } else if (this.btnKbn == "2") {
          sMsgConf = "高額承認しますがよろしいですか？"; //K040075
          sMsg2 = "承認しました。"; //M040010
        }
        if (await this.confirm(sMsgConf,this.$store.getters.getPageTitle())) {
          if (await this.UpdateData(sMsg1, sMsg2)) {
            if (this.btnKbn == "0") {
              await this.call_sendMail('差戻', this.ShoninInfo.kojiiraiNo)
            }
            else if (this.btnKbn == "2") {
              await this.call_sendMail('高額承認', this.ShoninInfo.kojiiraiNo)
            }
            await this.getData()
          }
        }
      }
    },
    async call_sendMail (btnName, 工事依頼No) {
      let res
      // アクション№40
      if (btnName == '差戻') {
        res = await this.sendMail(40, 工事依頼No, '', this.宛先事務所コード, this.宛先営業所コード,
                                  this.宛先部コード, this.宛先課コード, this.宛先係コード, '', '')
      }
      // アクション№41
      if (btnName == '緊急承認') {

        res = await this.sendMail(41, 工事依頼No, '', [], [], [], [], [], this.paramF044.業者コード, this.paramF044.業者コード枝番)
      }
      // アクション№43
      if (btnName == '高額承認') {
        if (this.dataForm.負担区分コード == '1' || this.dataForm.負担区分コード == '3') {
          res = await this.sendMail(43, 工事依頼No, '', [], [], [], [], [], this.paramF044.業者コード, this.paramF044.業者コード枝番)
        }
        else if (this.dataForm.負担区分コード == '2') {
          // アクション№50
          res = await this.sendMail(50, 工事依頼No, '', [], [], [], [], [], this.paramF044.業者コード, this.paramF044.業者コード枝番)
          if (this.dataForm.見積金額メール通知 == '1') {
            // アクション№51
            res = await this.sendMail(51, 工事依頼No, '', this.宛先事務所コード, this.宛先営業所コード,
                                  this.宛先部コード, this.宛先課コード, this.宛先係コード, '', '')
          }
        }
      }
      return res
    },
    async sendMail (mailPatternNo, 工事依頼No, 工事依頼No枝番, 宛先事務所コード, 宛先営業所コード, 宛先部コード, 宛先課コード,
                    宛先係コード, 業者コード, 業者コード枝番) {
      const param = {
        msg_type: 0,
        mail_to: '',
        msg_head: '',
        msg_body: '',
        pg_id: 'RD00803',
        st_kbn: 1,
        mail_pattern_no: mailPatternNo,
        工事依頼NO: 工事依頼No,
        工事依頼NO枝番: 工事依頼No枝番,
        宛先事務所コード: 宛先事務所コード,
        宛先営業所コード: 宛先営業所コード,
        宛先部コード: 宛先部コード,
        宛先課コード: 宛先課コード,
        宛先係コード: 宛先係コード,
        業者コード,
        業者コード枝番,
        担当者コード: []
      }
      console.log(param)
      const res = await this.commonFunctionUI.CM00110SendMail(param)
      return res
    },
    getDataUpdate () {
      return {
        工事依頼No: this.ShoninInfo.kojiiraiNo, //工事依頼№
        工事依頼No枝番: this.ShoninInfo.kojiiraiNoEdaban,
        種類: this.ShoninInfo.syouninSyuruiSelectVal, //11: 緊急承認 12: 高額承認
        処理区分: this.btnKbn, //0:差戻ボタン押下 1:否認ボタン押下  2:承認ボタン押下
        会社コード: this.dataForm.会社コード,
        否認差戻事由: this.dataForm.否認差戻事由,
        最終承認フラグ: this.dataForm.最終承認フラグ,
        工事区分: this.dataForm.工事区分コード,
        大区分: this.dataForm.大区分コード,
        中区分: this.dataForm.中区分コード,
        小区分: this.dataForm.小区分コード,
        依頼内容状況: this.dataForm.依頼内容_状況,
        不具合場所: this.dataForm.不具合場所,
        メーカー名: this.dataForm.メーカー名,
        依頼状況: this.dataForm.依頼状況,
        承認区分: this.dataForm.承認区分,
        継続区分: this.dataForm.継続区分,
        管理区分: this.dataForm.管理区分コード,
        支払区分: this.dataForm.支払区分コード,
        支払条件: this.dataForm.支払条件コード,
        事業計画: this.dataForm.事業計画コード,
        負担区分: this.dataForm.負担区分コード,
        修繕区分大: this.dataForm.修繕区分大,
        修繕区分小: this.dataForm.修繕区分小,
        物件コード: this.dataForm.物件コード,
        依頼日: this.dataForm.依頼日,
        表示件名: this.dataForm.表示件名,
        工事件名: this.dataForm.工事名称,
        // 取引先コード: this.dataForm.取引先コード,
        // 取引先コード枝番: this.dataForm.取引先コード枝番,
        取引先コード_高額: (!this.dataForm.取引先コード_高額)?'':this.dataForm.取引先コード_高額,
        取引先コード枝番_高額: this.dataForm.取引先コード枝番_高額,

        取引先コード1: (!this.dataForm.取引先コード1)?'':this.dataForm.取引先コード1.split('-')[0],
        取引先コード枝番1: this.dataForm.取引先コード枝番1,
        REAFST使用1: (!this.dataForm.REAFST使用1)?'':this.dataForm.REAFST使用1,
        WEB合意1: this.dataForm.WEB合意1,
        取引先コード2: (!this.dataForm.取引先コード2)?'':this.dataForm.取引先コード2.split('-')[0],
        取引先コード枝番2: this.dataForm.取引先コード枝番2,
        REAFST使用2: (!this.dataForm.REAFST使用2)?'':this.dataForm.REAFST使用2,
        WEB合意2: this.dataForm.WEB合意2,
        取引先コード3: (!this.dataForm.取引先コード3)?'':this.dataForm.取引先コード3.split('-')[0],
        取引先コード枝番3: this.dataForm.取引先コード枝番3,
        REAFST使用3: (!this.dataForm.REAFST使用3)?'':this.dataForm.REAFST使用3,
        WEB合意3: this.dataForm.WEB合意3,

        概算金額: this.dataForm.概算金額,
        見積金額メール通知: (!this.dataForm.見積金額メール通知)?'0':this.dataForm.見積金額メール通知,
        得意先コード: this.dataForm.得意先コード,
        得意先コード枝番: this.dataForm.得意先コード枝番,
        見積依頼フラグ:  (!this.dataForm.見積依頼フラグ)?'0':this.dataForm.見積依頼フラグ//'1': 緊急承認時見積依頼あり
      }
    },
    async UpdateData(sMsg1, sMsg2, s種類) {
      return new Promise((resolve) => {
      $thisValue = this;
      let skojiiraiNo = this.ShoninInfo.kojiiraiNo; //工事依頼№

      const param = this.getDataUpdate()
      
      this.http
        .post("api/Reafs_R/Mobile/Shonin/RD00803/Update_緊急高額承認", param)
        .then((response) => {
          if (!response.status) {
            this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
            resolve(false)
          }
          // this.getData();
          if(this.showSuccess(sMsg1 + skojiiraiNo + sMsg2)){
            this.dialogVisible = false;
            // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」Start
            if (response.data.f044) {
              this.paramF044.工事依頼No = response.data.f044.工事依頼No
              this.paramF044.工事依頼NO枝番 = response.data.f044.工事依頼NO枝番
              this.paramF044.業者コード = response.data.f044.業者コード
              this.paramF044.業者コード枝番 = response.data.f044.業者コード枝番
            }
            // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」End
            resolve(true)
          }
        })
      })
    },

    HininSashimodoshi_Check(s種類) {
      // 否認差戻事由チェック
      // 承認種類:11(緊急)　 差戻・否認ボタン押下時
      // 承認種類:12(高額)　 差戻ボタン押下時
      if (this.btnKbn == "0" || this.btnKbn == "1") {
        if (s種類 == "11" || (s種類 == "12" && this.btnKbn == "0")) {
          if (
            this.dataForm.否認差戻事由 == null ||
            this.dataForm.否認差戻事由 == ""
          ) {
            $thisValue.MsgErr({
              title: "",
              message: "否認差戻事由を入力してください。", //E040357
            });
            return false;
          }
        }
      }
      return true;
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

    showSuccess(message, title) {
      return new Promise((resolve, reject) => {
        var self = this;
        this.MsgInfo({
          title,
          message,
          callback: function (a, b) {
            // self.hideLoading();
            resolve(true);
          },
        });
      });
    },

    dataClear() {
      this.dataForm.依頼日時 = "";
      this.dataForm.依頼日 = "";
      this.dataForm.営業店 = "";
      this.dataForm.依頼内容_状況 = "";
      this.dataForm.不具合場所 = "";
      this.dataForm.メーカー名 = "";
      this.dataForm.依頼状況 = "";
      this.dataForm.工事区分コード = "";
      this.dataForm.大区分コード = "";
      this.dataForm.中区分コード = "";
      this.dataForm.小区分コード = "";
      this.dataForm.依頼者 = "";
      this.dataForm.連絡先 = "";
      this.dataForm.依頼者_代行 = "";
      this.dataForm.連絡先_代行 = "";
      this.dataForm.修繕区分大コード = "";
      this.dataForm.修繕区分小コード = "";
      this.dataForm.負担区分コード = "";
      this.dataForm.管理区分コード= "";
      this.dataForm.支払区分コード = "";
      this.dataForm.支払条件コード = "";
      this.dataForm.事業計画コード = "";
      // this.dataForm.取引先コード = "";
      // this.dataForm.取引先コード枝番 = "";
      // this.dataForm.取引先名称 = "";
      this.dataForm.取引先コード_高額 = "";
      this.dataForm.取引先コード枝番_高額 = "";
      this.dataForm.取引先名称_高額 = "";
      this.dataForm.選定取引先FLG = "";
      this.dataForm.取引先コード1 = "";
      this.dataForm.取引先コード枝番1 = "";
      this.dataForm.取引先名称1 = "";
      this.dataForm.取引先コード2 = "";
      this.dataForm.取引先コード枝番2 = "";
      this.dataForm.取引先名称2 = "";
      this.dataForm.取引先コード3 = "";
      this.dataForm.取引先コード枝番3 = "";
      this.dataForm.取引先名称3 = "";
      this.dataForm.概算金額 = "";
      this.dataForm.得意先コード = "";
      this.dataForm.得意先コード枝番 = "";
      this.dataForm.得意先名称 = "";
      this.dataForm.状況コメント1 = "";
      this.dataForm.状況コメント2 = "";
      this.dataForm.状況コメント3 = "";
      this.dataForm.状況コメント4 = "";
      this.dataForm.否認差戻事由 = "";
      this.dataForm.修繕区分小 = "";
      this.dataForm.負担区分 = "";
      this.dataForm.見積金額メール通知 = "";
      this.dataForm.MailChangeFlg = "";
      this.dataForm.最終承認フラグ = "";
      this.dataForm.承認区分 = "";
      this.dataForm.継続区分 = "";
      this.dataForm.修繕区分大 = "";
      this.dataForm.修繕区分小 = "";
      this.dataForm.物件コード = "";
      this.dataForm.表示件名 = "";
      this.dataForm.工事名称 = "";
      this.dataForm.会社コード = "";
      this.dataForm.工事依頼No枝番 = "";

      this.linkImage1 = "";
      this.linkImage2 = "";
      this.linkImage3 = "";
      this.linkImage4 = "";
    },

    isMobile() {
      let winWidth = window.innerWidth;
      if (winWidth < collapseWidth) {
        this.isMobileSized = true;
      } else {
        this.isMobileSized = false;
      }
    },
    hideModal() {
      this.dataForm.否認差戻事由 = ""
    },
    pageback_Click(){
        this.$router.push({
          name: "RD00802",
        });
    },
    // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」Start
    async twilioCall () {
      let param = {}
      if (this.paramF044.工事依頼No) {
        param = {
          param工事依頼No: this.paramF044.工事依頼No,
          param工事依頼NO枝番: this.paramF044.工事依頼NO枝番,
          param業者コード: this.paramF044.業者コード,
          param業者コード枝番: this.paramF044.業者コード枝番,
          param架電区分: 2,
          paramINSERT_PG: 'RD00803'
        }
        const res = await this.commonFunctionUI.twilio_Call(param)
        return res 
      }           
    },

    resetParamF044() {
      this.paramF044.工事依頼No = null,
      this.paramF044.工事依頼NO枝番 = null,
      this.paramF044.業者コード = null,
      this.paramF044.業者コード枝番 = null
    }
    // ハノイ側修正2022/11/15　STEP2_Web_R　課題管理表№1：設計書「修正履歴」シートの「2022/11/14仕様変更分」「架電通知・メール通知機能を追加」End
  },
};
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 20px;
}

.home-contianer {
  padding: 10px;
}

.el-form-item {
  margin-bottom: 0px;
}

.image-class {
  background: linear-gradient(to right, #9bc2e6 120px, #f3f3f3 0);
}

.dialog-footer {
  display: flex;
  justify-content: center;
}
</style>

<style scoped>
.el-form-item.image-class >>> .el-form-item__label {
  padding-top: 32%;
}

.el-image >>> .image-slot {
  display: flex;
  -ms-flex-pack: center;
  -webkit-box-pack: center;
  justify-content: center;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  font-size: 14px;
  color: #c0c4cc;
  vertical-align: middle;
  background: #f5f7fa;
  width: 100%;
  height: 100%;
}

.el-form >>> .el-form-item__content {
  min-height: 30px;
}
</style>