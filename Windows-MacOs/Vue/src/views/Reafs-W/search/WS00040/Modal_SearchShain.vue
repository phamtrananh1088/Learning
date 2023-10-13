<template>
  <el-dialog
    title="社員検索"
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
          <el-form-item label="社員名">
            <reafsinputtext
              ref="txtShainNm"
              v-model="searchForm.shainNm"
              :width="'300px'"
              maxlength="20"
            ></reafsinputtext>
          </el-form-item>
          <el-form-item label="事務所コード">
            <el-select
              style="width: 300px"
              v-model="searchForm.jimushoSelectValue"
              @change="jimushoSelectChanged()"
            >
              <el-option
                v-for="item in jimushoSelectItems"
                :key="item.名称コード"
                :value="item.名称コード"
                :label="item.名称コード + ' ' + item.名称"
              >
                <span>{{ item.名称コード }} {{ item.名称 }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="営業所コード">
            <el-select
              style="width: 300px"
              v-model="searchForm.eigyoSelectValue"
              @change="eigyoSelectChanged()"
            >
              <el-option
                v-for="item in eigyoSelectItems"
                :key="item.営業所コード"
                :value="item.営業所コード"
                :label="item.営業所名称"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="部・課・係">
            <el-select
              style="width: 300px"
              v-model="searchForm.bukakaraiSelectValue"
            >
              <el-option
                v-for="item in bukakaraiSelectItems"
                :key="item.部コード + item.課コード + item.係コード"
                :value="item.部コード + item.課コード + item.係コード"
                :label="
                  item.部コード +
                  item.課コード +
                  item.係コード +
                  ' ' +
                  item.部門名
                "
              >
                <span
                  >{{ item.部コード }}{{ item.課コード }}{{ item.係コード }}
                  {{ item.部門名 }}</span
                >
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
        prop="社員コード"
        label="社員コード"
        width="120"
        align="center"
        sortable
      >
      </el-table-column>
      <el-table-column prop="社員名" label="社員名" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="部門名" label="部門名" min-width="200" sortable> </el-table-column>
      <el-table-column prop="役職名" label="役職名" min-width="120" sortable>
      </el-table-column>
    </el-table>

    <!-- <reafstable
      ref="meisaiTableMobile"
      :tableData="tableData"
      :columns="columns2"
      :pagination-hide="true"
      :columnIndex="false"
      :columnChkbox="false"
      :spanColumns="spanColumns2"
      :spanHeaderColumns="spanHeaderColumns2"
      :key="Math.random()"
      :multirow="2"
      :height="570"
    >
    </reafstable> -->
  </el-dialog>
</template>

<script>
import reafstable from "@/components/table/Table.vue";
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
var $thisValue;
export default {
  components: {
    reafstable,
    reafsinputtext,
  },
  data() {
    return {
      searchForm: {
        shainNm: "",
        jimushoSelectValue: "",
        eigyoSelectValue: "",
        bukakaraiSelectValue: "",
      },
      jimushoSelectItems: [{ 名称コード: "", 名称: "－" }],
      eigyoSelectItems: [
        {
          営業所コード: "",
          営業所名称: " －",
        },
      ],
      bukakaraiSelectItems: [
        {
          部コード: "",
          課コード: "",
          係コード: "",
          部門名: "－",
        },
      ],

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
        param2: "",
        param3: "",
        param4: "",
        param5: "",
      },
      
      //
      columns2: [
      {
        title: "社員名",
        type: "string",
        width: 100,
        children: [
          {
            field: "社員名",
            title: "部門名", 
            type: "string",
            width: 100,
          },
        ],
      },
      {
        title: "役職名",
        type: "string",
        children: [
          {
            field: "役職名",
            type: "string",
          },
        ],
      },
      {
        field: "部門名",
        children: [
          {
            field: "部門名",
            title: "部門名",
            type: "string",
            width: "100%",
          },
        ],
      },
      ],
      spanHeaderColumns2 : [
        // //ヘッダ結合を指定
        { rowIndex: 0, columnIndex: 0, rowspan: 1, colspan: 1 }, //掲載日
        { rowIndex: 0, columnIndex: 1, rowspan: 1, colspan: 2 }, //会社名

        { rowIndex: 1, columnIndex: 0, rowspan: 0, colspan: 3 }, //掲載日
        { rowIndex: 1, columnIndex: 1, rowspan: 0, colspan: 0 }, //会社名
        { rowIndex: 1, columnIndex: 2, rowspan: 1, colspan: 0 }, //件名
      ],
      spanColumns2 : [
        { rowIndex: 0, columnIndex: 0, rowspan: 1, colspan: 1 }, //掲載日.
        { rowIndex: 0, columnIndex: 1, rowspan: 1, colspan: 2 }, //会社名row1.
        { rowIndex: 0, columnIndex: 2, rowspan: 0, colspan: 0 }, //件名.

        { rowIndex: 1, columnIndex: 0, rowspan: 0, colspan: 0 }, //掲載日row1.
        { rowIndex: 1, columnIndex: 1, rowspan: 0, colspan: 0 }, //会社名row1.
        { rowIndex: 1, columnIndex: 2, rowspan: 1, colspan: 3 }, //件名row1.
      ]
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
      return "text-align: center;background-color:#BDD7EE !important;";
    },
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      if (rowIndex % 2 === 0) {
        if (columnIndex === 0) {
          return [1, 1]
        } else if (columnIndex === 1) {
          return [0, 0]
        }
      }
    },
    openSearch() {
      //サブ画面を起動するタイミングで、1つ目のコントロールに設定する
      this.$nextTick(function () {
        this.$refs.txtShainNm.focus()
      });

      this.kbnInfo.strParam1 = "031";
      this.kbnInfo.intParam1 = 0;
      this.loading = true
      this.http
        .post("api/Reafs_W/Master/selectM018", this.kbnInfo)
        .then((response) => {
          if (!response.status) {
            this.loading = false
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          this.jimushoSelectItems = response.data;
          this.jimushoSelectItems.unshift({
            名称コード: "",
            名称: "－",
          });
          this.loading = false
        });
    },

    jimushoSelectChanged() {
      this.eigyoSelectItems = [
        {
          営業所コード: "",
          営業所名称: " －",
        },
      ];
      this.bukakaraiSelectItems = [
        {
          部コード: "",
          課コード: "",
          係コード: "",
          部門名: "－",
        },
      ];
      this.searchForm.eigyoSelectValue = "";
      this.searchForm.bukakaraiSelectValue = "";

      if (this.searchForm.jimushoSelectValue.length > 0) {
        this.kbnInfo.intParam1 = 0; //削除フラグ
        this.kbnInfo.strParam1 = this.searchForm.jimushoSelectValue; //事務所コード

        this.http
          .post("api/Reafs_W/Master/selectM009", this.kbnInfo)
          .then((response) => {
            if (!response.status) {
              return this.MsgInfo({
                title: this.$store.getters.getPageTitle(),
                message: response.message,
              });
            }
            this.eigyoSelectItems = response.data;
            this.eigyoSelectItems.unshift({
              営業所コード: "",
              営業所名称: " －",
            });
          });
      }
    },

    eigyoSelectChanged() {
      this.bukakaraiSelectItems = [
        {
          部コード: "",
          課コード: "",
          係コード: "",
          部門名: "－",
        },
      ];
      this.searchForm.bukakaraiSelectValue = "";

      if (
        this.searchForm.jimushoSelectValue.length > 0 &&
        this.searchForm.eigyoSelectValue.length > 0
      ) {
        this.kbnInfo.intParam1 = 0; //削除フラグ
        this.kbnInfo.strParam1 = this.searchForm.jimushoSelectValue; //事務所コード
        this.kbnInfo.strParam2 = this.searchForm.eigyoSelectValue; //営業所コード

        this.http
          .post("api/Reafs_W/Master/selectM002", this.kbnInfo)
          .then((response) => {
            if (!response.status) {
              return this.MsgInfo({
                title: this.$store.getters.getPageTitle(),
                message: response.message,
              });
            }
            this.bukakaraiSelectItems = response.data;
            this.bukakaraiSelectItems.unshift({
              部コード: "",
              課コード: "",
              係コード: "",
              部門名: "－",
            });
          });
      }
    },

    clearData() {
      this.jimushoSelectItems = [
        {
          名称コード: "",
          名称: " －",
        },
      ];
      this.eigyoSelectItems = [
        {
          営業所コード: "",
          営業所名称: " －",
        },
      ];
      this.bukakaraiSelectItems = [
        {
          部コード: "",
          課コード: "",
          係コード: "",
          部門名: "－",
        },
      ];

      this.searchForm.shainNm = "";
      this.searchForm.jimushoSelectValue = "";
      this.searchForm.eigyoSelectValue = "";
      this.searchForm.bukakaraiSelectValue = "";

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
      this.searchparam.param2 = this.searchForm.shainNm;
      this.searchparam.param3 = this.searchForm.jimushoSelectValue;
      this.searchparam.param4 = this.searchForm.eigyoSelectValue;
      this.searchparam.param5 = this.searchForm.bukakaraiSelectValue;

      var qs = require("qs");
      this.http
        .post("api/Reafs_W/Search/searchShain", qs.stringify(this.searchparam))
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
            txtCd: row.社員コード,
            lblNm: row.社員名,
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