<template>
  <el-dialog
    title="費用負担部門"
    :visible="dialogVisible"
    class="dialog-class HiyoFutanBumonSabu"
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
          <el-form-item label="コード" id="コード">
            <reafsinputtext
              ref="txtKodo"
              v-model="searchForm.kodo"
              :width="'110px'"
              maxlength="20"
            ></reafsinputtext>
          </el-form-item>
          <el-form-item label="名称" id="名称">
            <reafsinputtext
              ref="txtMeisho"
              v-model="searchForm.meisho"
              :width="'68%'"
              maxlength="20"
            ></reafsinputtext>
          </el-form-item>
        </el-col>
        <el-col :span="4" :xs="24" style="margin-top: 29px;">
          <el-button type="primary" @click="onSearch" size="mini"
              @keydown.native ="down2first"
            >検索</el-button
          >
        </el-col>
      </el-row>
    </el-form>
    <el-table
      ref="meisaiTable"
      :data="tableData"
      v-loading="loading"
      :header-cell-style="headerCellStyle"
      :cell-style="{ padding: '0px' }"
      stripe
      border
      height="288"
      style="margin-top: 10px; scroll: auto"
      @row-click="backData"
    >
      <el-table-column
        prop="名称コード"
        label="コード"
        width="120"
        align="left"
      >
      </el-table-column>
      <el-table-column prop="コード長名" label="名称" min-width="120">
      </el-table-column>
    </el-table>
    <el-button @click="onClose()" style="float: right; margin-top: 20px;">閉じる</el-button>
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
        kodo: "",
        meisho: "",
      },

      kbnInfo: {
        strParam1: "",
        strParam2: "",
        intParam1: 0,
      },

      dataCnt: 0,
      loading: false,
      tableData: [],
      searchparam: {
        param1: "",
        param2: ""
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
        this.$refs.txtKodo.focus()
      });

      this.onSearch()
    },

    clearData() {
      this.searchForm.kodo = "";
      this.searchForm.meisho = "";

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

      this.searchparam.param1 = this.searchForm.kodo;
      this.searchparam.param2 = this.searchForm.meisho;

      var qs = require("qs");
      this.http
        .post("api/Reafs_W/Search/searchHiyoFutanBumonSabu", qs.stringify(this.searchparam))
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
            txtCd: row.名称コード,
            lblNm: row.コード長名,
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
          this.$refs.txtShainNm.focus();
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
  width: 40%;
  height: 520px;
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

.el-table >>> th.el-table__cell .cell {
  white-space: pre-wrap;
}

.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
.HiyoFutanBumonSabu .el-dialog .el-form-item {
    margin-bottom: 0px;
}
</style>
<style>
  #コード label {
    width: 120px !important;
    margin-bottom: 5px;
  }
  #名称 label {
    width: 120px !important
  }
</style>