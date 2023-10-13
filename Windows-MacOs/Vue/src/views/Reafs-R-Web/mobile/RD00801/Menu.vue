<template>
  <div class="home-contianer">
    <el-card shadow="never">
      <div slot="header" class="clearfix card-header">
        <span class="card-title">ステータス</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="GetShoninList"
          >再表示</el-button
        >
      </div>
      <!-- <div>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <el-table
                v-loading="shoninLoading"
                :data="tableData"
                style="width: 100%"
              >
                <el-table-column prop="TITLE" label="" width="120">
                </el-table-column>
                <el-table-column
                  prop="KINKYU_CNT"
                  label="依頼承認(緊急)"
                  width="120"
                  align="right"
                >
                  <template v-slot:default="tableData">
                    <router-link :to="{name: 'RD00802', params: { id: tableData.row.KINKYU_CNT }}" style="color:blue">
                       <p>{{ tableData.row.KINKYU_CNT }}</p>
                    </router-link>
                  </template>
                </el-table-column>
                <el-table-column prop="KOUGAKU_CNT" label="依頼承認(高額)"  width="120" align="right">
                  <template v-slot:default="tableData">
                    <router-link :to="{name: 'RD00802', params: { id: tableData.row.KOUGAKU_CNT }}" style="color:blue">
                       <p>{{ tableData.row.KOUGAKU_CNT }}</p>
                    </router-link>
                  </template>
                </el-table-column>
                <el-table-column  label="" width="auto">
                </el-table-column>
              </el-table>
            </div>
          </el-col>
        </el-row>
      </div> -->
      <div>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <reafstable
                ref="meisaiData"
                :single="single"
                :rowKey="rowKey"
                :tableData="meisaiData"
                :columns="columns"
                :pagination="pagination"
                :pagination-hide="true"
                :height="78"
                :columnIndex="false"
                :columnChkbox="false"
                @backData="pageBack"
                :multirow="1"
                :linkView="linkViewShonin"
                :key="Math.random()"
                :cellStyleFunction="this.cellStyleFunction"
                :cellClassFunction="this.cellClassFunction"
              >
              </reafstable>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <el-card shadow="never">
      <div slot="header" class="clearfix card-header">
        <span class="card-title">お知らせ通知</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="GetOshiraseList"
          >再表示</el-button
        >
      </div>
      <div>
        <el-row :gutter="20">
          <!-- <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <el-table
                v-loading="oshiraseLoading"
                :data="oshiraseData"
                style="width: 100%"
              >
               <el-table-column prop="JYUYO_FLG" label="" width="20">
                </el-table-column>
                <el-table-column prop="KEIJI_S_YMD" label="掲載日" width="120">
                </el-table-column>
                <el-table-column prop="TOUROKU_KBN" label="登録システム" width="150">
                </el-table-column>
                <el-table-column prop="TITLE" label="件名"> </el-table-column>
              </el-table>
            </div>
            <div>
          </el-col> -->
          <el-col :span="24">
            <!-- :single="single" -->
            <div class="grid-content bg-purple-light">
              <reafstable
                v-loading="oshiraseLoading"
                ref="oshiraseData2"
                
                :rowKey="rowKey"
                :tableData="oshiraseData2"
                :columns="oshiraseColumns"
                :pagination="oshirasePagination"
                :pagination-hide="true"
                :columnIndex="false"
                :columnChkbox="false"
                :spanColumns="spanColumns"
                :spanHeaderColumns="spanHeaderColumns"
                @backData1="oshirasePageBack"
                :linkView="linkViewOshirase"
                :key="Math.random()"
                :multirow="2"
                :cellClassFunction="this.cellClassFunctionList"
              >
              </reafstable>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <reafsmodal
      :dialogVisible.sync="showDialog"
      @backData2="oshiraseModaldataBack"
      :rowData="rowData"
    ></reafsmodal>
  </div>
</template>
<script>
import reafstable from "@/components/table/Table.vue";
import reafsmodal from "@/views/Reafs-R-Web/mobile/RD00801/Modal_OshiraseShousai.vue";
var $thisValue;

export default {
  components: {
    reafstable,
    reafsmodal,
  },
  data() {
    return {
      value1: "1",
      showDialog: false,
      tableData: [],
      meisaiData: [],
      // oshiraseData: [],
      oshiraseData2: [],
      shoninLoading: false,
      oshiraseLoading: false,

      single: false, // SingleSelet
      rowKey: undefined, // Treetable PK
      pagination: { total: 10, size: 10, sortName: "", page: 1 },

      columns: [
        {
          field: "TITLE",
          title: "",
          type: "string",
          width: "20%",
          readonly: true,
          align: "center",
        },
        {
          field: "KINKYU_CNT",
          title: "依頼承認(緊急)",
          type: "string",
          width: "40%",
          readonly: false,
          detailAlign: "right",
          align: "center",
          link: true, // リンクを有効に
        },
        {
          field: "KOUGAKU_CNT",
          title: "依頼承認(高額)",
          type: "string",
          width: "40%",
          readonly: false,
          detailAlign: "right",
          align: "center",
          link: true, // リンクを有効に
        },
      ],

      // お知らせ
      oshirasePagination: { total: 0, size: 0, sortName: "", page: 1 },
      oshiraseColumns: [
        {
          field: "JYUYO_FLG",
          title: "",
          type: "string",
          width: 4,
          readonly: false,
          align: "center",
        },
        {
          // field: "KEIJI_S_YMD",
          title: "掲載日",
          type: "string",
          width: 100,
          readonly: false,
          align: "center",
          children: [
            {
              field: "KEIJI_S_YMD",
              title: "件名", //「タイトル：件名、データ：掲載日」
              type: "string",
              width: 100,
              readonly: false,
              align: "center",
            },
          ],
        },
        {
          // field: "TOUROKU_KBN",
          title: "会社名",
          type: "string",
          readonly: false,
          align: "center",
          children: [
            {
              field: "TOUROKU_KBN",
              title: "会社名",
              type: "string",
              readonly: false,
              align: "left",
            },
          ],
        },
        {
          field: "TITLE",
          // title: "件名",
          // type: "string",
          // width: "100%",
          // readonly: false,
          // align: "center",
          link: true, // リンクを有効に
          children: [
            {
              field: "TITLE",
              title: "件名",
              type: "string",
              width: "100%",
              readonly: false,
              align: "left",
              link: true, // リンクを有効に
            },
          ],
        },
      ],
      rowData: {},
      rowDatas: [],
      spanColumns: [],
      spanHeaderColumns: [],
    };
  },
  methods: {
    init() {
      // 承認件数取得
      this.GetShoninList();
      // お知らせ件数取得
      this.GetOshiraseList();

      let offsetColumn =
        (this.columnIndex ? 1 : 0) + (this.columnChkbox ? 1 : 0) + 1;
      this.spanHeaderColumns = [
        // //ヘッダ結合を指定
        { rowIndex: 0, columnIndex: 0, rowspan: 2, colspan: 1 }, //!.
        { rowIndex: 0, columnIndex: 1, rowspan: 1, colspan: 1 }, //掲載日
        { rowIndex: 0, columnIndex: 2, rowspan: 1, colspan: 2 }, //会社名
        // // { rowIndex: 0, columnIndex: 3, rowspan: 0, colspan: 0 }, //件名

        { rowIndex: 1, columnIndex: 0, rowspan: 0, colspan: 3 }, //掲載日
        { rowIndex: 1, columnIndex: 1, rowspan: 0, colspan: 0 }, //会社名
        { rowIndex: 1, columnIndex: 2, rowspan: 1, colspan: 0 }, //件名
      ];
      this.spanColumns = [
        // // // //データ結合を指定
        { rowIndex: 0, columnIndex: 0, rowspan: 2, colspan: 1 }, //!.
        { rowIndex: 0, columnIndex: 1, rowspan: 1, colspan: 1 }, //掲載日.
        { rowIndex: 0, columnIndex: 2, rowspan: 1, colspan: 2 }, //会社名row1.
        { rowIndex: 0, columnIndex: 3, rowspan: 0, colspan: 0 }, //件名.

        { rowIndex: 1, columnIndex: 0, rowspan: 0, colspan: 0 }, //!.
        { rowIndex: 1, columnIndex: 1, rowspan: 0, colspan: 0 }, //掲載日row1.
        { rowIndex: 1, columnIndex: 2, rowspan: 0, colspan: 0 }, //会社名row1.
        { rowIndex: 1, columnIndex: 3, rowspan: 1, colspan: 3 }, //件名row1.
      ];
    },

    cellClassFunction(data) {
      let returnStr = "";
      let row = data.row;

      // 件数内訳に申請日から1日以上経過したデータが含まれる場合は赤文字に変更。
      if (data.columnIndex == "1" && row.IS_ALERT_KINKYU == "1") {
        returnStr += " textRed";
      }

      if (data.columnIndex == "2" && data.row.IS_ALERT_KOUGAKU == "1") {
        returnStr += " textRed";
      }

      return returnStr;
    },
    cellClassFunctionList(data) {
      let returnStr = "";
      let row = data.row;

      if (data.columnIndex == "0") {
        returnStr += " noPadding textRed";
      }

      return returnStr;
    },
    cellStyleFunction(data) {
      let returnStr = "";
      let col = this.columns[(data.rowIndex, data.columnIndex)];

      if (col.detailAlign) {
        // cell align設定
        returnStr += " text-align: " + col.detailAlign + ";";
      }

      return returnStr;
    },
    GetShoninList() {
      let _this = this;
      var data = [];
      _this.shoninLoading = true;

      $thisValue.meisaiData = [];

      this.http
        .post("/api/Reafs_R/Mobile/getShoninList", { user: "info" })
        .then(function (response) {
          for (let i = 0; i < response.data.length; i++) {
            var obj = {};
            obj.TITLE = "承認待ち";
            obj.KINKYU_CNT = response.data[i].KINKYU_CNT;
            obj.KOUGAKU_CNT = response.data[i].KOUGAKU_CNT;
            data[i] = obj;
          }
          _this.tableData = data;
          _this.shoninLoading = false;

          $thisValue.meisaiData.push(...response.data);
        });
    },
    GetOshiraseList() {
      let _this = this;
      var data = [];
      _this.oshiraseLoading = true;

      // $thisValue.oshiraseData = [];
      $thisValue.oshiraseData2 = [];

      this.http
        .post("/api/Reafs_R/Mobile/getOshiraseList", { user: "info" })
        .then(function (response) {
          for (let i = 0; i < response.data.length; i++) {
            var obj = {};
            obj.JYUYO_FLG = response.data[i].JYUYO_FLG;
            obj.KEIJI_S_YMD = response.data[i].KEIJI_S_YMD;
            obj.TOUROKU_KBN = response.data[i].TOUROKU_KBN;
            obj.TITLE = response.data[i].TITLE;
            obj.KEIJI_ID = response.data[i].KEIJI_ID;
            obj.TOUROKU_USER = response.data[i].TOUROKU_USER;
            obj.TENPU = response.data[i].TENPU;
            obj.TENPU_PATH = response.data[i].TENPU_PATH;
            obj.DETAIL = response.data[i].DETAIL;
            data[i] = obj;
          }
          // データが重複して取得されてしまうためコメント
          // _this.oshiraseData2 = data;

          $thisValue.oshiraseData2.push(...response.data);
          $thisValue.oshirasePagination.total = response.data.length;
          $thisValue.oshirasePagination.size = response.data.length;
          _this.oshiraseLoading = false;

          // _this.showDialog = true
        });
    },
    oshirasePageBack(obj) {
      this.oshirasePagination.page = obj;
    },
    // サブ画面から戻すデータ
    oshiraseModaldataBack(obj) {
      this.$emit("backData2", obj);
    },
    linkViewShonin(row, column, rowIndex) {
      // 承認リンク押下時の処理
      // 0始まりのため行と対応させる場合+1する必要あり
      if (column.field === "KINKYU_CNT") {
        // 0件の場合遷移できないようにする。
        if (Number(row.KINKYU_CNT) <= 0) {
          return false
        }
        // KINKYU_CNT
        row.linkType = "11"
        this.$router.push({ name: "RD00802", params: row });
        this.rowData = row
      } else if (column.field === "KOUGAKU_CNT") {
        // 0件の場合遷移できないようにする。
        if (Number(row.KOUGAKU_CNT) <= 0) {
          return false
        }
        row.linkType = "12"
        // KOUGAKU_CNT
        this.$router.push({ name: "RD00802", params: row });
        this.rowData = row
      }
    },
    linkViewOshirase(row, column, rowIndex) {
      // お知らせリンク押下時の処理
      // console.log(row);
      // console.log(rowIndex); // 0始まりのため行と対応させる場合+1する必要あり
      // console.log(column);

      if (column.field === "TITLE") {
        // TITLE列のリンクの場合
        this.showDialog = true;
        this.rowData = row;
      }
      // その他の列のリンクをクリックした場合条件を追加
    },

    pageBack(obj) {
      this.pagination.page = obj;
    },
  },
  created() {
    $thisValue = this;

    this.init();
  },
  mounted() {
    // window.onload = () => {
    //   GetShoninList()
    // }
  },
};
</script>
<style lang="less" scoped>
.home-contianer {
  background: #efefef;
  width: 100%;
}
.home-app {
  display: inline-block;
  padding: 15px;
  padding-top: 5px;
}
.home-app > div {
  float: left;
  width: 33.33333%;
  padding-left: 8px;
  padding-right: 8px;
}
.ivu-card-body {
  text-align: center;
  padding: 25px 13px;
  padding-left: 80px;
}
.demo-color-name {
  color: #fff;
  font-size: 16px;
}
.demo-color-desc {
  color: #fff;
  opacity: 0.7;
}
.ivu-card {
  position: relative;
}
.ivu-card .icon-left {
  border-right: 1px solid;
  padding: 10px 24px;
  height: 100%;
  position: absolute;
  font-size: 50px;
  color: white;
}
.ivu-row {
  display: flex;
  border-bottom: 2px dotted #eee;
  padding: 0px 5px;
  .ivu-col {
    border-radius: 4px;
    background: white;
    flex: 1;
    margin: 13px 8px;
    padding: 20px 20px;
    padding-bottom: 15px;
  }
  .total {
    word-break: break-all;
    color: #282727;
    font-size: 30px;
    padding-bottom: 12px;
    font-family: -apple-system, BlinkMacSystemFont, Segoe UI, PingFang SC,
      Hiragino Sans GB, Microsoft YaHei, Helvetica Neue, Helvetica, Arial,
      sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol;
  }
  .item-name {
    position: relative;
    .icon {
      position: absolute;
      right: 0;
      top: -1px;
    }
  }
  .item-name,
  .rate,
  .bottom {
    color: #5f5f5f;
    font-size: 14px;
  }
  .rate {
    border-bottom: 1px solid #e6e6e6;
    padding-bottom: 10px;
  }
  .bottom {
    padding-top: 8px;
  }
  .down {
    color: #10d310;
  }
  .up {
    color: red;
  }
}

.h5-desc {
  padding-top: 10px;
}
</style>
<style lang="less">
.charts-line {
  background: white;
  padding-top: 10px;
}
.charts {
  margin: 25px 13px;
  display: inline-block;
  width: 100%;
  .left {
    padding: 25px;
    background: white;
    height: 360px;
    width: 49%;
    float: left;
    margin-right: 1%;
    background: white;
  }
  .right {
    padding: 25px 45px;
    background: white;
    height: 360px;
    width: 49%;
    float: left;
    margin-left: 1%;
    .badge-count {
      padding: 3px 7px;
      position: relative;
      border: 1px solid #eee;
      border-radius: 50%;

      margin-right: 11px;
    }
    .badge {
      background: #e2e2e2;
      color: #3a3535;
    }
    .top3 {
      background: #2db7f5;
      color: white;
    }
    .cell {
      position: relative;
      display: flex;
      padding: 10px 0;
      border-bottom: 1px dotted #eee;
    }
    .primary {
      flex: 1;
    }
    .title {
      font-size: 16px;
      padding-bottom: 6px;
      border-bottom: 1px solid #eee;
      margin-bottom: 11px;
    }
    .name {
      font-size: 15px;
      position: relative;
      top: 5px;
      color: #303133;
      left: 12px;
    }
    .desc {
      margin-left: 27px;
      font-size: 12px;
      color: #b3b3b3;
      position: relative;
      top: 5px;
    }
  }
}
</style>
<style lang="less" scoped>
.numbers {
  margin: 0 12px 12px 12px;
  border-radius: 5px;
  border: 1px solid #eaeaea;
  background: white;
  display: flex;

  padding: 20px 0px;
  .item {
    flex: 1;
    text-align: center;
    border-right: 1px solid #e5e5e5;
  }
  .item > 　div:first-child {
    word-break: break-all;
    color: #282727;
    font-size: 30px;
  }
  .item > 　div:last-child {
    font-size: 13px;
    color: #777;
  }
  .item:last-child {
    border-right: none;
  }
  .number {
    cursor: pointer;
    transition: transform 0.8s;
  }
  .number:hover {
    transform: scale(1.2);
    color: #03c10b !important;
  }
}
</style>
<style lang="less" scoped>
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.el-card__header {
  padding: 0 0;
  border: 0 0;
  background: #777777;
  color: #ececec;
}

.card-title {
  font-size: 1.4em;
}
</style>
