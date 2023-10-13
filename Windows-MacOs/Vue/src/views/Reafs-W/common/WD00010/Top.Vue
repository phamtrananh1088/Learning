<template>
  <div class="home-contianer">
    <el-card shadow="never">
      <div slot="header" class="clearfix card-header">
        <span class="card-title">ステータス</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="getStatusData"
          >再読込</el-button
        >
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <el-table
                v-loading="statusLoading"
                :data="statusData"
                style="width: 100%"
              >
                <el-table-column prop="ROWTYPE" label="" width="80">
                </el-table-column>
                <el-table-column
                  prop=""
                  label="依頼承認"
                  align="center"
                  header-align="center"
                  width="150"
                >
                  <!-- -->
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '依頼承認')"
                      type='primary'
                      v-if="scope.$index == 0"
                      >{{ scope.row ? scope.row.依頼承認 : "" }}</el-link
                    >
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '依頼承認')"
                      type='primary'
                      v-else
                      >{{ scope.row ? scope.row.依頼承認 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
                <!-- <el-table-column
                  prop=""
                  label="依頼(緊急)"
                  width="160"
                  align="right"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '依頼承認_緊急')"
                      type="primary"
                      >{{ scope.row ? scope.row.依頼承認_緊急 : "" }}</el-link
                    >
                  </template>
                </el-table-column> -->
                <el-table-column
                  prop=""
                  label="　　見積内容確認　　契約内容確認"
                  width="150"
                  align="center"
                  header-align="center"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '見積_契約内容確認')"
                      type='primary'
                      v-if="scope.$index == 0"
                      >{{scope.row ? scope.row.見積_契約内容確認 : ""}}</el-link
                    >
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '見積_契約内容確認')"
                      type='primary'
                      v-else
                      >{{scope.row ? scope.row.見積_契約内容確認 : ""}}</el-link
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  prop=""
                  label="実施判断"
                  width="150"
                  align="center"
                  header-align="center"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '実施判断')"
                      type='primary'
                      v-if="scope.$index == 0"
                      >{{ scope.row ? scope.row.実施判断 : "" }}</el-link
                    >
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '実施判断')"
                      type='primary'
                      v-else
                      >{{ scope.row ? scope.row.実施判断 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  prop=""
                  label="稟議申請"
                  width="150"
                  align="center"
                  header-align="center"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '稟議申請')"
                      type='primary'
                      v-if="scope.$index == 0"
                      >{{ scope.row ? scope.row.稟議申請 : "" }}</el-link
                    >
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '稟議申請')"
                      type='primary'
                      v-else
                      >{{ scope.row ? scope.row.稟議申請 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  prop="発注承認"
                  label="発注承認"
                  width="150"
                  align="center"
                  header-align="center"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '発注承認')"
                      type='primary'
                      v-if="scope.$index == 0"
                      >{{ scope.row ? scope.row.発注承認 : "" }}</el-link
                    >
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '発注承認')"
                      type='primary'
                      v-else
                      >{{ scope.row ? scope.row.発注承認 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  prop=""
                  label="工事完了承認"
                  width="150"
                  align="center"
                  header-align="center"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '工事完了承認')"
                      type='primary'
                      v-if="scope.$index == 0"
                      >{{ scope.row ? scope.row.工事完了承認 : "" }}</el-link
                    >
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '工事完了承認')"
                      type='primary'
                      v-else
                      >{{ scope.row ? scope.row.工事完了承認 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  prop=""
                  label="支払承認"
                  width="150"
                  align="center"
                  header-align="center"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '支払承認')"
                      type='primary'
                      v-if="scope.$index == 0"
                      >{{ scope.row ? scope.row.支払承認 : "" }}</el-link
                    >
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '支払承認')"
                      type='primary'
                      v-else
                      >{{ scope.row ? scope.row.支払承認 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  prop="点検予定確認"
                  label="点検予定確認"
                  width="150"
                  align="center"
                  header-align="center"
                >
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onJumpTo(scope, '点検予定確認')"
                      type="primary"
                      >{{ scope.row ? scope.row.点検予定確認 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <el-card shadow="never">
      <div slot="header" class="clearfix card-header">
        <span class="card-title">お知らせ通知</span>
        <el-button
          v-show="buttons.btnMenu.visible"
          style="float: right; padding: 3px 0"
          type="text"
          @click="getNoticeData"
          >再読込</el-button
        >
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <el-table
                v-loading="noticeLoading"
                :data="noticeData"
                style="width: 100%"
              >
                <el-table-column width="20">
                  <template slot-scope="scope">
                    <label
                      style="color:red"
                      >{{ scope.row ? scope.row.重要フラグ : "" }}</label
                    >
                  </template>
                </el-table-column>
                <el-table-column prop="掲示開始日" label="掲載日" width="120">
                </el-table-column>
                <el-table-column
                  prop="登録者システム"
                  label="登録システム"
                  width="160"
                >
                </el-table-column>
                <el-table-column
                  prop="登録者部署"
                  label="登録者部署"
                  width="300"
                >
                </el-table-column>
                <el-table-column label="件名">
                  <template slot-scope="scope">
                    <el-link
                      target="_blank"
                      @click="onClickKenMei(scope.row)"
                      type="primary"
                      >{{ scope.row ? scope.row.件名 : "" }}</el-link
                    >
                  </template>
                </el-table-column>
              </el-table>
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
var $thisValue;
import reafsmodal from "@/views/Reafs-W/common/WD00010/OshiraseShousai.vue";

export default {
  components: {
    reafsmodal,
  },
  data() {
    return {
      showDialog: false,
      statusData: [],
      noticeData: [],
      searchForm: {
        keijiFlg: 1, //現在掲示期間
      },
      rowData: {}, //お知らせ通知でクリックしたの行
      statusLoading: false,
      noticeLoading: false,
      メニューID: "B",
      サブメニューID: "WD00010",
      buttons: {
        btnMenu: { visible: true },
        btn検索: { disabledOrg: false, disabled: false },
        btnCSV出力: { disabledOrg: false, disabled: false },
      },
    };
  },

  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.getNoticeData();  
      // ステータスデータ取得
      // 2022/11/29 【暫定対応】自動取得は実行しない
      // this.getStatusData();
      this.http
        .get('api/Reafs_W/Common/WD00010/GetAutoCount', 'アクセスしています....')
        .then((response) => {
          if(response.data=="0"){
            this.getStatusData();
          }
        })
        
    },
    onJumpTo(scope, key) {
      // 0件でも遷移はできるようにしておく→指摘があり遷移できないようにする。
      if (Number(scope.row[key].replaceAll(",", "")) <= 0) {
        return false
      }
      localStorage.setItem('KeepAliveFlag', 0)
      console.log('onJumpTo:'+ scope.row.ROWTYPE + ', ' + key)
      let jumpParamWD00510 = {
        name: null,
        query: {
          承認種類: '10',
          対象履歴: scope.row.ROWTYPE,
          修繕定期: '―'
        }
      }

      let jumpParamWD00410 = {
        name: null,
        query: {
          StartuoMode: ''
        }
      }
      switch (key) {
        case "点検予定確認":
          jumpParamWD00410.name = 'WD00410' //作業状況一覧に遷移 sw003 既存データから　:WD00410 sagyoJoukyoIchiran
          jumpParamWD00410.query.StartuoMode = '1' // 点検予定確認件数押下時
          break
        case "依頼承認":
          jumpParamWD00510.name = 'WD00510' //未承認/承認履歴一覧に遷移。sw003 既存データから　:WD00510 mishoninShoninIchiran
          jumpParamWD00510.query.承認種類 = '10'
          jumpParamWD00510.query.修繕定期 = '全て'
          break
        case "見積_契約内容確認":
          jumpParamWD00510.name = 'WD00510' //未承認/承認履歴一覧に遷移。sw003 既存データから　:WD00510 mishoninShoninIchiran
          jumpParamWD00510.query.承認種類 = '32'
          jumpParamWD00510.query.修繕定期 = '定期'
          break
        case "実施判断":
          jumpParamWD00510.name = 'WD00510' //未承認/承認履歴一覧に遷移。sw003 既存データから　:WD00510 mishoninShoninIchiran
          jumpParamWD00510.query.承認種類 = '32'
          jumpParamWD00510.query.修繕定期 = '修繕'
          break
        case "稟議申請":
          jumpParamWD00510.name = 'WD00510' //未承認/承認履歴一覧に遷移。sw003 既存データから　:WD00510 mishoninShoninIchiran
          jumpParamWD00510.query.承認種類 = '42'
          if (scope.row.ROWTYPE == '承認待ち') {
            jumpParamWD00510.query.対象履歴 = '稟議申請待ち'
          } else if (scope.row.ROWTYPE == '差戻') {
            jumpParamWD00510.query.対象履歴 = '履歴/稟議取下／否認'
          }
          jumpParamWD00510.query.修繕定期 = '－'
          break
        case "発注承認":
          jumpParamWD00510.name = 'WD00510' //未承認/承認履歴一覧に遷移。sw003 既存データから　:WD00510 mishoninShoninIchiran
          jumpParamWD00510.query.承認種類 = '41'
          jumpParamWD00510.query.修繕定期 = '－'
          break
        case "工事完了承認":
          jumpParamWD00510.name = 'WD00510' //未承認/承認履歴一覧に遷移。sw003 既存データから　:WD00510 mishoninShoninIchiran
          jumpParamWD00510.query.承認種類 = '52'
           if (scope.row.ROWTYPE == '承認待ち') {
            jumpParamWD00510.query.対象履歴 = '確認待ち'
          }
          jumpParamWD00510.query.修繕定期 = '－'
          break
        case "支払承認":
          jumpParamWD00510.name = 'WD00510' //未承認/承認履歴一覧に遷移。sw003 既存データから　:WD00510 mishoninShoninIchiran
          jumpParamWD00510.query.承認種類 = '61'
          jumpParamWD00510.query.修繕定期 = '－'
          break

        default:
          break
      }
      if (jumpParamWD00510.name) {
        this.$router.push(jumpParamWD00510)
      } else if (jumpParamWD00410.name) {
        this.$router.push(jumpParamWD00410)
      }
    },
    onClickKenMei(row) {
      this.showDialog = true;
      this.rowData = row;
    },
    // サブ画面から戻すデータ
    oshiraseModaldataBack(obj) {
      this.$emit("backData2", obj);
    },
    getStatusData() {
      $thisValue = this;
      $thisValue.statusLoading = true;
      $thisValue.statusData = undefined;
      this.http
        .post("api/Reafs_W/Common/WD00010/getStatusData", this.searchForm)
        .then((result) => {
          if (!result.status) {
            $thisValue.statusLoading = false;
             // TOP画面では対象データ無しのメッセージは不要のためコメントアウト
            // return this.MsgInfo({
            //   title: this.$store.getters.getPageTitle(),
            //   message: result.message,
            // });
            return
          }

          $thisValue.statusData = $thisValue.formatNumber(result.data);
          $thisValue.statusLoading = false;
        });
    },
    getNoticeData() {
      $thisValue = this;
      $thisValue.noticeLoading = true;
      $thisValue.noticeData = undefined;
      // .post("api/Reafs_W/Common/WD00010/getNoticeData", this.searchForm)
      // .post("api/Reafs_W/Common/WD00011/search", this.searchForm)

      this.http
        .post("api/Reafs_W/Common/WD00010/getNoticeData", this.searchForm)
        .then((result) => {
          if (!result.status) {
            $thisValue.noticeLoading = false
            // TOP画面では対象データ無しのメッセージは不要のためコメントアウト
            // return this.MsgInfo({
            //   title: this.$store.getters.getPageTitle(),
            //   message: result.message,
            // });
            return
          }

          $thisValue.noticeData = result.data;
          $thisValue.noticeLoading = false;
        });
    },
    formatNumber(tableDate) {
      if (
        tableDate != null &&
        Array.isArray(tableDate) &&
        tableDate.length > 0
      ) {
        for (let index = 0; index < tableDate.length; index++) {
          const row = tableDate[index];
          for (const key in row) {
            if (Object.hasOwnProperty.call(row, key)) {
              const itemVal = row[key];
              if (!isNaN(itemVal)) {
                tableDate[index][key] = this.numberToCurrencyNo(itemVal);
              }
            }
          }
        }
      }
      return tableDate;
    },

    numberToCurrencyNo(value, nullStr = '') {
      if (value != 0 && !value) return nullStr;
      const intPart = Math.trunc(value);
      const intPartFormat = intPart
        .toString()
        .replace(/(\d)(?=(?:\d{3})+$)/g, "$1,");
      let floatPart = "";
      const valueArray = value.toString().split(".");
      if (valueArray.length === 2) {
        floatPart = valueArray[1].toString();
        return intPartFormat + "." + floatPart;
      }
      return intPartFormat + floatPart;
    }
  },
};
</script>
<style lang="less" scoped>
.home-contianer {
  background: #efefef;
}
.el-table th {
  text-align: center !important;
}

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