<template>
  <div class="home-container TeikiSeikyuSubSub">
    <el-form
      :model="TeikiSeikyuSubSub"
      ref="TeikiSeikyuSubSubForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk="true"
    >
      <el-container class="home-el-container">
        <el-main class="home-el-main">
          <el-row>
            <!-- 明細 -->
            <el-col :span="24">
              <div class="grid-content">
                <reafstable
                  :maxHeight="400"
                  :cellStyleOption="cellStyleOption"
                  ref="tableKouza"
                  :tableData="tblIraiData"
                  :columns="tblIraiColumns"
                  :rowKeyFieldName="rowKeyFieldName"
                  :class="tableClassFilter"
                  :cellClickEvent="onSelectrow"
                  :rowStyleOption="rowStyleOption"
                  :row-class-name="tableRowClassName"
                  :height="400"
                >
                </reafstable>
              </div>
            </el-col>
          </el-row>
          <el-footer>
            <el-row class="footer_row">
              <div class="footer_button">
                <el-col>
                  <el-button @click="onIraiKousin">登録</el-button>
                  <el-button @click="onBack">閉じる</el-button>
                </el-col>
              </div>
            </el-row>
          </el-footer>
        </el-main>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafstable from "@/components/table/Grid.vue";
import opensearch from "@/components/basic/openSearch.vue";

export default {
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.initialize(); // 初期化処理
      next();
    });
  },

  components: {
    reafsinputtext,
    reafstable,
    opensearch
  },
  data() {
    return {
      rowKeyFieldName: "rowKey",

      TourokuParams: {
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
      userInfo: {
        ユーザーＩＤ: "",
        会社コード: ""
      },
      TeikiSeikyuSubSub: {
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
      tblIraiColumns: [
        {
          key: "tbl銀行名",
          title: "銀行名",
          field: "tbl銀行名",
          width: 120
        },
        {
          key: "tbl口座種別",
          title: "口座種別",
          field: "tbl口座種別",
          width: 120
        },
        {
          key: "tbl口座番号",
          title: "口座番号",
          field: "tbl口座番号",
          width: 120
        },
        {
          key: "tbl口座名義人",
          title: "口座名義人",
          field: "tbl口座名義人",
          width: 120
        }
      ],
      tblIraiData: [],

      editOption: {
        // cell value change
        cellValueChange: ({ row, column }) => {}
      },

      rowStyleOption: {
        clickHighlight: true
      }
    };
  },

  editOption: {
    // cell value change
    cellValueChange: ({ row, column }) => {}
  },
  watch: {},

  computed: {},
  created() {
    let userLogIn = this.$store.getters.getUserInfo();
    this.userInfo.ユーザーＩＤ = userLogIn.userId;
    this.userInfo.会社コード = userLogIn.会社コード;
  },
  inject: ["hideLeftMenu", "setBtnBackShow"],
  mounted() {
    this.hideLeftMenu(false);
    this.setBtnBackShow(false);
  },
  methods: {
    ///////////////////////////////////////////////
    // 親画面からデータ引き渡し
    ///////////////////////////////////////////////
    initialize() {
      console.log("請求合意一覧サブ画面");
      this.TeikiSeikyuSubSub.種類 = this.$route.query.種類;
      this.TeikiSeikyuSubSub.会社コード = this.$route.query.会社コード;
      this.TeikiSeikyuSubSub.契約NO = this.$route.query.契約NO;
      this.TeikiSeikyuSubSub.履歴NO = this.$route.query.履歴NO;
      this.TeikiSeikyuSubSub.明細NO = this.$route.query.明細NO;
      this.TeikiSeikyuSubSub.契約年月 = this.$route.query.契約年月;
      this.TeikiSeikyuSubSub.工事依頼NO = this.$route.query.工事依頼NO;
      this.TeikiSeikyuSubSub.工事依頼NO枝番 = this.$route.query.工事依頼NO枝番;
      this.TeikiSeikyuSubSub.発注サブNO = this.$route.query.発注サブNO;
      this.TeikiSeikyuSubSub.検収回数 = this.$route.query.検収回数;
      this.TeikiSeikyuSubSub.回収支払契約番号 = this.$route.query.回収支払契約番号;
      this.TeikiSeikyuSubSub.会計会社コード = this.$route.query.会計会社コード;

      this.getPageData();
    },
    ///////////////////////////////////////////////
    // 画面データ検索
    ///////////////////////////////////////////////
    async getPageData() {
      var 検索条件 = new Object();
      var 検索結果 = new Object();
      検索条件 = this.TeikiSeikyuSubSub;

      await this.http
        .post(
          "api/Reafs_T/SeikyuSub/TS00501_GET_KouzaZyouhou",
          検索条件,
          "表示しています...."
        )
        .then(function(response) {
          検索結果 = response.data;
        });
      this.tblIraiData = 検索結果;
    },
    ///////////////////////////////////////////////
    // 一行選択
    ///////////////////////////////////////////////
    onSelectrow(row) {
      //選択時登録用カラムに登録
      this.TourokuParams.種類 = row.tbl種類;
      this.TourokuParams.会社コード = row.tbl会社コード;
      this.TourokuParams.契約NO = row.tbl契約NO;
      this.TourokuParams.履歴NO = row.tbl履歴NO;
      this.TourokuParams.明細NO = row.tbl明細NO;
      this.TourokuParams.契約年月 = row.tbl契約年月;
      this.TourokuParams.工事依頼NO = row.tbl工事依頼NO;
      this.TourokuParams.工事依頼NO枝番 = row.tbl工事依頼NO枝番;
      this.TourokuParams.発注サブNO = row.tbl発注サブNO;
      this.TourokuParams.検収回数 = row.tbl検収回数;
      this.TourokuParams.回収支払契約番号 = row.tbl回収支払契約番号;
    },

    ///////////////////////////////////////////////
    // 更新ボタン押下
    ///////////////////////////////////////////////
    async onIraiKousin() {
      let 更新結果 = new Object();
      let 更新データ = new Object();

      console.log("データ更新");
      if (this.TourokuParams.回収支払契約番号 != "") {
        更新データ = {
          種類: this.TourokuParams.種類,
          会社コード: this.TourokuParams.会社コード,
          回収支払契約番号: this.TourokuParams.回収支払契約番号,
          工事依頼NO: this.TourokuParams.工事依頼NO,
          工事依頼NO枝番: this.TourokuParams.工事依頼NO枝番,
          検収回数: this.TourokuParams.検収回数,
          発注サブNO: this.TourokuParams.発注サブNO,
          契約NO: this.TourokuParams.契約NO,
          履歴NO: this.TourokuParams.履歴NO,
          明細NO: this.TourokuParams.明細NO,
          契約年月: this.TourokuParams.契約年月,
          UPDATE_PG: "TD00500",
          UPDATE_ID: this.userInfo.ユーザーＩＤ,
          会計会社コード: this.$route.query.会計会社コード
        };
      } else {
        this.fnc_void_showErrorMsg("E040245");
        return false;
      }
      if (更新データ.種類 == "1") {
        //修繕の更新
        await this.http
          .post(
            "api/Reafs_T/SeikyuSub/TS00501_KousinSyuzen",
            更新データ,
            "登録しています...."
          )
          .then(function(response) {
            更新結果 = response;
          });
      } else if (更新データ.種類 == "2") {
        //定期の更新
        await this.http
          .post(
            "api/Reafs_T/SeikyuSub/TS00501_KousinTeiki",
            更新データ,
            "登録しています...."
          )
          .then(function(response) {
            更新結果 = response;
          });
      }
      if (!更新結果.status) {
        this.MsgErr({
          title: "請求合意一覧",
          message: tourokukekka.message
        });
      } else {
        this.onBack();
      }
    },
    ///////////////////////////////////////////////
    // 戻るボタン押下
    ///////////////////////////////////////////////
    async onBack() {
      window.parent.postMessage(
        {
          call: "closePopup"
        },
        "*"
      );
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
    ///////////////////////////////////////////////
    // keyからS016メッセージマスタにアクセスしメッセージ取得
    ///////////////////////////////////////////////
    fnc_rtn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    }
  }
};
</script>

<style lang="less" scoped>
.home-container {
  padding: 5px;
  height: 30%;
  width: 50%;
}
.footer_row {
  margin: 1% 1% 0 1%;
  width: 100%;
}
.footer_button {
  float: right;
  margin: 0 0 0 50%;
}
.tableRowClassName {
  background-color: #ff9999 !important;
}
</style>
