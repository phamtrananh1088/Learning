<template>
  <el-dialog
    title="物件検索"
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
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="物件名" prop="buNm">
            <reafsinputtext
              ref="txtBuNm"
              v-model="searchForm.buNm"
              :width="'300px'"
              maxlength="20"
            ></reafsinputtext>
          </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="住所" prop="address">
              <reafsinputtext
                v-model="searchForm.address"
                :width="'300px'"
                maxlength="20"
              ></reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="物件区分">
              <el-select
                width="10px"
                v-model="kbnValue"
              >
                <el-option
                  v-for="item in kbnSelectValue"
                  :key="item.名称コード"
                  :label="item.名称"
                  :value="item.名称コード"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="営業所名" prop="eigyoNm">
              <reafsinputtext
                v-model="searchForm.eigyoNm"
                :width="'300px'"
                maxlength="20"
              ></reafsinputtext>
            </el-form-item>
          </el-col>
        </el-col>
        
        <el-col :span="4" :xs="24">
          <el-button 
            type="primary" 
            size="mini"
            :loading="loading"
            @click="onSubmit" 
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
      height="680"
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
        prop="物件コード"
        label="物件コード"
        width="120"
        align="center"
        sortable
      >
      </el-table-column>
      <el-table-column label="営業所（管理部署）">
        <el-table-column
          prop="事務所_管理部署"
          label="事務所"
          width="90"
          align="center"
          sortable
        >
        </el-table-column>
        <el-table-column
          prop="営業所_管理部署"
          label="営業所"
          width="90"
          align="center"
          sortable
        >
        </el-table-column>
        <el-table-column
          prop="部_管理部署"
          label="部"
          width="70"
          align="center"
          sortable
        >
        </el-table-column>
        <el-table-column
          prop="課_管理部署"
          label="課"
          width="70"
          align="center"
          sortable
        >
        </el-table-column>
        <el-table-column
          prop="係_管理部署"
          label="係"
          width="70"
          align="center"
          sortable
        >
        </el-table-column>
      </el-table-column>
      <el-table-column prop="物件名" label="物件名" min-width="200" sortable>
      </el-table-column>
      <el-table-column
        prop="物件区分"
        label="物件区分"
        width="120"
        align="center"
        sortable
      >
      </el-table-column>
      <el-table-column label="住所">
        <el-table-column prop="住所" label="住所" width="305" sortable>
        </el-table-column>
        <el-table-column prop="番地" label="番地" width="80" align="center" sortable>
        </el-table-column>
        <el-table-column prop="号" label="号" width="70" align="center" sortable>
        </el-table-column>
      </el-table-column>
      <el-table-column
        prop="営業所_依頼主"
        :label="labelFn()"
        width="120"
        align="center"
        sortable
      >
      </el-table-column>
      <el-table-column prop="郵便番号" label="郵便番号" v-if="false">
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
        buNm: "",
        address: "",
        eigyoNm: "",
      },

      kbnInfo: {
        strParam1: "",
        intParam1: 0,
      },
      kbnValue: "",
      kbnSelectValue: [{ 名称コード: "", 名称: "" }],

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
    labelFn() {
      return `営業所\t\n（依頼主）`;
    },
    headerCellStyle() {
      return "text-align: center;background-color:#BDD7EE";
    },

    openSearch() {
      
      //サブ画面を起動するタイミングで、1つ目のコントロールに設定する
      this.$nextTick(function () {
        this.$refs.txtBuNm.focus()
      });

      this.kbnInfo.strParam1 = "025";
      this.kbnInfo.intParam1 = 0;
      this.http
        .post("api/Reafs_W/Master/selectM018", this.kbnInfo)
        .then((response) => {
          if (!response.status) {
            // return this.$message.error(response.message);
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.kbnSelectValue = response.data;
          this.kbnSelectValue.unshift({ 名称コード: "", 名称: "" });
        });
    },

    clearData() {
      this.kbnSelectValue = undefined;
      this.kbnValue = "";
      this.searchForm.buNm = "";
      this.searchForm.address = "";
      this.searchForm.eigyoNm = "";
      this.tableData = undefined;
      this.dataCnt = 0;
    },

    onClose() {
      this.clearData();
      this.$emit("update:dialogVisible", false);
    },
    onSubmit() {
      $thisValue = this;
      this.loading = true;
      this.tableData = undefined;
      this.dataCnt = 0;

      this.searchparam.param1 = "";
      this.searchparam.param2 = this.searchForm.buNm;
      this.searchparam.param3 = this.searchForm.address;
      this.searchparam.param4 = this.kbnValue;
      this.searchparam.param5 = this.searchForm.eigyoNm;

      var qs = require("qs");
      this.http
        .post("api/Reafs_W/Search/searchBukken", qs.stringify(this.searchparam))
        .then((result) => {
          if (!result.status || result.data == null) {
            this.loading = false;
            // return this.$message.error(result.message);
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
            txtCd: row.物件コード,
            lblNm: row.物件名,
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
          this.$refs.txtBuNm.focus();
          event.preventDefault();
        });
      }
    },
  },
};
</script>
<style scoped>
.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
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

.el-form--inline .el-form-item {
  display: inline-block;
  margin-right: 0px;
  vertical-align: top;
}

.tableDataCnt {
  margin-top: -15px;
  margin-bottom: -15px;
  text-align: right;
}
.el-table >>> th.el-table__cell .cell {
  white-space: pre-wrap;
}
</style>