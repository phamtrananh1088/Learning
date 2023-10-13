<template>
  <el-dialog
    :close-on-click-modal="false"
    title="組織検索2"
    :visible="dialogVisible"
    class="dialog-class"
    @close="onClose()"
    @opened="onOpened()"
  >
    <el-form :model="searchForm" ref="searchForm">
      <el-row>
        <el-col :span="20" :xs="24">
          <el-form-item
            label="事務所コード"
            prop="jigyousyoCd"
            label-width="120px"
          >
            <el-select
              ref="ddlJigyousyoCd"
              v-model="searchForm.jigyousyoCd"
              size="10"
              @change="onJigyousyoChange"
              placeholder="ー"
              is-horizontal="false"
            >
              <el-option
                v-for="item in jigyousyoList"
                :key="item.事務所コード"
                :value="item.事務所コード"
                :label="item.事務所名称"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item
            label="営業所コード"
            prop="eigyousyoCd"
            label-width="120px"
          >
            <el-select
              v-model="searchForm.eigyousyoCd"
              size="10"
              placeholder="ー"
            >
              <el-option
                v-for="item in eigyousyoList"
                :key="item.営業所コード"
                :value="item.営業所コード"
                :label="item.名称FULL"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4" :xs="24" >
          <el-form-item>
            <el-button 
              type="primary" 
              size="mini"
              :loading="loading"
              @click="onSubmit" 
              @keydown.native ="down2first"
              >検索</el-button
            >
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="tableDataCnt">
      <span style="position: absolute; right: 95px">件数：</span>
      <span style="margin-right: 16px">{{ dataCnt.toString() }}</span>
      <span style="position: absolute; right: 20px"> 件</span>
    </div>
    <el-table
      ref="tableRef"
      :data="sliceTable"
      v-loading="loading"
      :header-cell-style="headerCellStyle"
      :cell-style="{ padding: '0px' }"
      stripe
      border
      height="668"
      style="margin-top: 10px; scroll: auto"
      @row-dblclick="backData"
      @sort-change="changeTableSort"
    >
      <!-- @row-dblclick="backData" -->
      <!-- @row-click="backData" -->
      <!-- <el-table-column label="No" prop="行NO" width="70px" align="center">
      </el-table-column> -->
      
      <el-table-column
        type="index"
        width="70"
        label="No"
        align="center"
        :index="setIndex"
      ></el-table-column>
      <el-table-column label="部・課・係" width="120" align="left" sortable>
        <template slot-scope="scope">
          <el-link
            @click="backData(scope.row)"
            target="_blank"
            type="primary"
            :underline="false"
            >{{ scope.row ? scope.row.部課係 : "" }}</el-link
          >
        </template>
      </el-table-column>
      <el-table-column prop="組織名" label="組織名" width="510" align="left" sortable>
      </el-table-column>
      <el-table-column prop="部門名" label="部門名" width="auto" align="left" sortable>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";

var $thisValue;
export default {
  components: {
    reafsinputtext,
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    subParamsObj: {
      taisyou: { type: String }, //承認種類　 取引先検索：対象
      jigyousyoCd: { type: String }, //事業所コード
    },
  },
  data() {
    return {
      searchForm: {
        jigyousyoCd: "", //事務所コード
        eigyousyoCd: "", //営業所コード
        taisyou: "", //対象(負担区分)
      },
      jigyousyoList: {}, //事務所下拉项目
      eigyousyoList: {}, //営業所下拉项目
      dataCnt: 0,
      loading: false,
      tableData: [],
      PKey: "行NO",
      paramsList: {
        param1: "",
        param2: "",
        param3: "",
        param4: "",
        param5: "",
      },
      setDisabled: true,
      startindex: 0,
      sliceTable: [],
      vEle: undefined,
      showCount: 26,
      headerHeight: 41,
      rowHeight: 23,
      tableHeight: 0,
    };
  },
  created() {
    var qs = require("qs");
    // 主画面から引数を取得し設定
    this.searchForm.taisyou = parseInt(this.subParamsObj.taisyou);
    this.setDisabled = this.subParamsObj.length > 0; //仮想スクロール

    //仮想スクロール 設定
    this.tableHeight = this.rowHeight * this.showCount + this.headerHeight;
    this.vEle = document.createElement("div");
  },
  methods: {
    setDisabel() {
      return true;
    },
    onClose() {
      this.clearData();
      this.$emit("update:dialogVisible", false);
    },
    onOpened() {
      // 事務所コード 項目取得
      this.ajaxPost(
        "api/Reafs_W/Search/getJimusyoItems",
        "searchForm.taisyou",
        "jigyousyoList",
        ["事務所コード"],
        ["jigyousyoCd"],
        [this.subParamsObj.jigyousyoCd]
      );
      // 営業所コード 項目取得
      this.ajaxPost(
        "api/Reafs_W/Search/getEigyousyoItems",
        [this.subParamsObj.jigyousyoCd],
        "eigyousyoList"
      );

      this.$nextTick(() => {
        this.$refs.ddlJigyousyoCd.focus();
      });
    },
    // 営業所コード 項目取得
    onJigyousyoChange() {
      this.searchForm.eigyousyoCd = "";
      this.ajaxPost(
        "api/Reafs_W/Search/getEigyousyoItems",
        "searchForm.jigyousyoCd",
        "eigyousyoList"
      );
    },
    onSubmit() {
      $thisValue = this;
      this.loading = true;
      $thisValue.tableData = undefined;
      $thisValue.sliceTable = undefined;
      $thisValue.dataCnt = 0;
      //仮想スクロール
      $thisValue.$nextTick(() => {
        this.$refs.tableRef.$el.querySelector(
          ".el-table__body-wrapper"
        ).scrollTop = 0;
      });

      this.paramsList.param1 = this.searchForm.jigyousyoCd; //事務所コード
      this.paramsList.param2 = this.searchForm.eigyousyoCd; //営業所コード
      this.paramsList.param3 = this.subParamsObj.taisyou; //負担区分

      var qs = require("qs");
      this.http
        .post(
          "api/Reafs_W/Search/getSoshikiItems",
          qs.stringify(this.paramsList)
        )
        .then((result) => {
          if (!result.status || result.data == null) {
            this.loading = false;
             return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: result.message,
            });
          }

          $thisValue.tableData = result.data;
          $thisValue.sliceTable = [];
          $thisValue.startindex = 0;
          //画面表示テーブル高さを計算する
          $thisValue.tableHeight =
            $thisValue.rowHeight * $thisValue.showCount +
            $thisValue.headerHeight;

          this.loadData();

          var reg = /\d{1,3}(?=(\d{3})+$)/g;
          $thisValue.dataCnt = ($thisValue.tableData.length + "").replace(
            reg,
            "$&,"
          );
          this.loading = false;
        });
    },
    ajaxPost(url, paramsType, outPer, keyDbNameS, keyNameS, keyValueS) {
      let qs = require("qs");
      if (typeof paramsType == "string") {
        let params = paramsType.split(".");
        this.paramsList.param1 = this[params[0]][params[1]];
      } else {
        this.paramsList.param1 = paramsType[0];
      }
      // コード 選択項目取得
      this.http.post(url, qs.stringify(this.paramsList)).then((result) => {
        if (!result.status || result.data == null) {
          this.loading = false;
           return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: result.message,
            });
        }

        this[outPer] = result.data;
        let returnRow = [];
        // 主画面から引数を取得し設定
        if (
          keyDbNameS &&
          keyNameS &&
          keyValueS &&
          keyDbNameS.length == keyNameS.length &&
          keyValueS.length == keyNameS.length
        ) {
          returnRow = result.data.filter((item, index) => {
            return item.事務所コード == keyValueS[0];
          });
        }
        if (returnRow.length > 0) {
          this.searchForm[keyNameS[0]] = returnRow[0].事務所名称;
        }
      });
    },
    clearData() {
      this.searchForm.jigyousyoCd = this.subParamsObj.jigyousyoCd; //事務所コード
      this.searchForm.eigyousyoCd = ""; //営業所コード
      this.searchForm.taisyou = parseInt(this.subParamsObj.taisyou); //対象
      this.tableData = undefined;
      //仮想スクロール
      this.sliceTable = undefined;
      this.startindex = 0;
      this.dataCnt = 0;
    },

    headerCellStyle() {
      return "text-align: center;background-color:#BDD7EE";
    },
    backData(currRowDate, column) {
      this.$emit("backData", {
        // 戻り値：「事業所コード」・「事業所名」・「営業所コード」・「営業所名」・「部コード」・「課コード」・「係コード」・「組織名」・「部門名」
        row: currRowDate,
      });
      this.clearData();
      this.$emit("update:dialogVisible", false);
    },
    loadData() {
      if (!$thisValue.tableData) {
        return;
      }

      // 仮想スクロール
      $thisValue.sliceTable = $thisValue.tableData.slice(
        $thisValue.startindex,
        $thisValue.startindex + $thisValue.showCount
      );

      $thisValue.$nextTick(() => {
        $thisValue.$refs.tableRef.$el
          .querySelector(".el-table__body-wrapper")
          .addEventListener("scroll", $thisValue.tableScroll, {
            passive: true,
          });

        $thisValue.$refs.tableRef.$el.querySelector(
          ".el-table__body"
        ).style.position = "absolute";

        $thisValue.vEle.style.height =
          $thisValue.tableData.length * $thisValue.rowHeight + "px";

        $thisValue.$refs.tableRef.$el
          .querySelector(".el-table__body-wrapper")
          .appendChild($thisValue.vEle);
      });
    },

    tableScroll() {
      let bodyWarpperEle = this.$refs.tableRef.$el.querySelector(
        ".el-table__body-wrapper"
      );
      let scrollTop = bodyWarpperEle.scrollTop;

      this.startindex = Math.floor(scrollTop / this.rowHeight);

      //スクロールは最後の位置
      if (
        bodyWarpperEle.scrollHeight <=
        scrollTop + bodyWarpperEle.clientHeight
      ) {
        bodyWarpperEle.querySelector(
          ".el-table__body"
        ).style.transform = `translateY(${
          this.startindex * this.rowHeight - 2
        }px)`;
      } else {
        bodyWarpperEle.querySelector(
          ".el-table__body"
        ).style.transform = `translateY(${this.startindex * this.rowHeight}px)`;
      }
      this.loadData();
    },
    //ソート機能
    changeTableSort(val){
      if (val.order == "ascending") {
        $thisValue.tableData = $thisValue.tableData.sort((a,b) => a[val.prop].toUpperCase() > b[val.prop].toUpperCase() ? 1 : -1);
      } else if (val.order == "descending"){
        $thisValue.tableData = $thisValue.tableData.sort((a,b) => b[val.prop].toUpperCase() > a[val.prop].toUpperCase() ? 1 : -1);
      } else {
        $thisValue.tableData = $thisValue.tableData.sort((a,b) => a[val.prop].toUpperCase() > b[val.prop].toUpperCase() ? 1 : -1);
      }
      
      // 仮想スクロール　表示用データ行数+５のみ、テーブルに入る
      $thisValue.sliceTable = $thisValue.tableData.slice(
        this.startindex,
        this.startindex + this.showCount
      )
    },
    down2first(event) {
      if (event.keyCode === 9) {
        this.$nextTick(function () {
          this.$refs.ddlJigyousyoCd.focus();
          event.preventDefault();
        });
      }
    },
  },
  computed: {
    setIndex(){
      return this.startindex + 1
    }
  },
};
</script>
<style scoped>
.el-dialog__wrapper {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: auto;
  margin: 0;
}
.el-dialog__wrapper.dialog-class >>> .el-dialog {
  position: relative;
  margin: 0 auto 50px;
  background: #fff;
  border-radius: 2px;
  -webkit-box-shadow: 0 1px 3px rgb(0 0 0 / 30%);
  box-shadow: 0 1px 3px rgb(0 0 0 / 30%);
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 90%;
}
.dialog-class >>> .el-dialog__header {
  padding-top: 10px;
  padding-bottom: 0;
}

.radio-group-class >>> .el-form-item__content {
  line-height: 28px;
  border: 1px solid #dcdfe6;
  padding-top: 2px !important;
}

.el-table {
  border-style: solid;
  border-width: 1px;
  border-color: rgb(193, 197, 205);
}
.el-table >>> .el-table__cell {
  padding-top: 5px;
  padding-bottom: 5px;
  border-right-style: solid;
  border-right-width: 1px;
  border-right-color: rgb(235, 238, 245);
}

.el-table >>> .el-table__body:hover {
  cursor: pointer;
}
.el-table >>> .el-table__body tr:hover > td{
  background-color: #abdcff !important;
}
.el-table >>> .el-table__body tr.current-row > td{
  background-color: #abdcff !important;
}

.tableDataCnt {
  margin-top: -15px;
  margin-bottom: -15px;
  text-align: right;
}
.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
</style>

