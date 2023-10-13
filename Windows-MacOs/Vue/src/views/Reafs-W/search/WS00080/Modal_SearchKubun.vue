<template>
  <el-dialog
    title="区分検索"
    :visible="dialogVisible"
    class="dialog-class"
    @open="openSearch()"
    @close="onClose()"
    :close-on-click-modal="false"
  >
    <el-form
      :model="searchForm"
      ref="searchForm"
    >
      <el-row>
        <el-col :span="20" :xs="24">
          <el-form-item label="大区分">
            <el-select
              ref="ddlDKbn"
              v-model="searchForm.dKbnValue"
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
          <el-form-item label="中区分">
            <el-select
              v-model="searchForm.cKbnValue"
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
          <el-form-item label="小区分">
            <el-select
              v-model="searchForm.sKbnValue"
            >
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
        </el-col>
        <el-col :span="4" :xs="24">
          <el-button 
            type="primary" 
            size="mini"
            :loading="loading"
            @click="onSearch" 
            @keydown.native ="down2first"
            >検索</el-button
          >
        </el-col>
      </el-row>
    </el-form>
    <div class="tableDataCnt">
      <span style="position: absolute; right: 95px">件数：</span>
      <span style="margin-right: 16px">{{ dataCnt.toString() }}</span>
      <span style="position: absolute; right: 20px"> 件</span>
    </div>
    <el-table
      ref="meisaiTable"
      :data="tableData"
      v-loading="loading"
      :header-cell-style="headerCellStyle"
      :cell-style="{ padding: '0px' }"
      stripe
      border
      height="570"
      style="margin-top: 10px; scroll: auto"
      @row-click="backData"
    >
      <el-table-column
        type="index"
        width="50"
        label="No"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="大中小区分コード"
        label="大中小区分コード"
        width="180"
        align="center"
        sortable
      >
      </el-table-column>
      <el-table-column prop="大区分名" label="大区分名" width="400" sortable>
      </el-table-column>
      <el-table-column prop="中区分名" label="中区分名" sortable> </el-table-column>
      <el-table-column prop="小区分名" label="小区分名" width="400" sortable>
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
  data() {
    return {
      searchForm: {
        dKbnValue: "", //大区分コード
        cKbnValue: "", //中区分コード
        sKbnValue: "", //小区分コード
      },
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

      kbnInfo: {
        strParam1: "",
        strParam2: "",
        strParam3: "",
        intParam1: 0,
      },

      dataCnt: 0,
      loading: false,
      tableData: [],
      searchparam: {
        param1: "",
        param2: "",
        param3: "",
        param4: "",
        param5: "",
      },
    };
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
  },
  created() {},

  methods: {
    headerCellStyle() {
      return "text-align: center;background-color:#BDD7EE";
    },

    openSearch() {
      //サブ画面を起動するタイミングで、1つ目のコントロールに設定する
      this.$nextTick(function () {
        this.$refs.ddlDKbn.focus()
      });

      this.dKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.cKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.sKbnSelectItems = [{ 名称コード: "", 名称: "－" }];

      this.getdKbnSelect();
    },

    dKbnSelectChanged() {
      this.cKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.sKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.searchForm.cKbnValue = "";
      this.searchForm.sKbnValue = "";

      this.getcKbnSelect();
    },

    cKbnSelectChanged() {
      this.sKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.searchForm.sKbnValue = "";

      this.getsKbnSelect();
    },

    getdKbnSelect() {
      this.kbnInfo.strParam1 = "004"; //大区分の場合
      this.kbnInfo.intParam1 = 0;
      this.http
        .post("api/Reafs_W/Master/selectM018", this.kbnInfo)
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: result.message,
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
      if (this.searchForm.dKbnValue.length > 0) {
        this.kbnInfo.strParam1 = "005"; //中区分の場合
        this.kbnInfo.strParam2 = this.searchForm.dKbnValue; //大区分コード
        this.kbnInfo.intParam1 = 0; //削除フラグ

        this.http
          .post("api/Reafs_W/Master/selectM018", this.kbnInfo)
          .then((response) => {
            if (!response.status) {
              return this.MsgInfo({
                title: this.$store.getters.getPageTitle(),
                message: this.message,
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
        this.searchForm.dKbnValue.length > 0 &&
        this.searchForm.cKbnValue.length > 0
      ) {
        this.kbnInfo.strParam1 = "006"; //小区分の場合
        this.kbnInfo.strParam2 = this.searchForm.dKbnValue; //大区分コード
        this.kbnInfo.strParam3 = this.searchForm.cKbnValue; //中区分コード
        this.kbnInfo.intParam1 = 0; //削除フラグ

        this.http
          .post("api/Reafs_W/Master/selectM018", this.kbnInfo)
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

    clearData() {
      this.dKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.cKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.sKbnSelectItems = [{ 名称コード: "", 名称: "－" }];
      this.searchForm.dKbnValue = "";
      this.searchForm.cKbnValue = "";
      this.searchForm.sKbnValue = "";
      this.tableData = undefined;
      this.dataCnt = 0;
    },

    onClose() {
      this.clearData();
      this.$emit("update:dialogVisible", false);
    },

    onSearch() {
      $thisValue = this;
      this.loading = true;
      this.tableData = undefined;
      this.dataCnt = 0;

      this.searchparam.param1 = "";
      this.searchparam.param2 = this.searchForm.dKbnValue;
      this.searchparam.param3 = this.searchForm.cKbnValue;
      this.searchparam.param4 = this.searchForm.sKbnValue;

      var qs = require("qs");
      this.http
        .post("api/Reafs_W/Search/searchKubun", qs.stringify(this.searchparam))
        .then((result) => {
          if (!result.status || result.data == null) {
            this.loading = false;
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: result.message,
            });
          }

          $thisValue.tableData = result.data;
          var reg = /\d{1,3}(?=(\d{3})+$)/g;
          $thisValue.dataCnt = ($thisValue.tableData.length + "").replace(
            reg,
            "$&,"
          );
          this.loading = false;
        });
    },
    backData(row, column) {
      // row-dblclick　タブレット無効
      // 代わりに row-click を使用してください
      if (0 == this.touchtime) {
        this.touchtime = new Date().getTime();
      } else {
        if (new Date().getTime() - this.touchtime < 800) {
          this.$emit("backData", {
            txtCd: row.大区分コード + row.中区分コード + row.小区分コード,
            lblNm: row.大中小区分名,
            row: row,
          });
          this.clearData();
          this.$emit("update:dialogVisible", false);
          this.touchtime = 0;
        } else {
          //2回目のクリックが最初のクリックから0.8秒後の場合、
          //次に、2回目のクリックは、デフォルトで次のダブルクリックによって判断される最初のクリックになります。
          this.touchtime = new Date().getTime();
        }
      }
    },
    down2first(event) {
      if (event.keyCode === 9) {
        this.$nextTick(function () {
          this.$refs.ddlDKbn.focus();
          event.preventDefault();
        });
      }
    },
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
.el-table >>> th.el-table__cell .cell {
  white-space: pre-wrap;
}

.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
</style>