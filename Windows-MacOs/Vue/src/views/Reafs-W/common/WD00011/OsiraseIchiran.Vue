<template>
  <div class="home-contianer">
    <!-- 検索部 -->
    <!-- :rules="rules" -->
    <el-form
      :model="searchForm"
      ref="searchForm"
      label-width="120px"
      class="search-form-class"
      :rules="rules"
    >
      <el-row>
        <!-- left -->
        <el-col :span="12">
          <el-col :span="16"
            ><el-form-item label="適用日" prop="tekiyouDate">
              <nengetsu
                v-model="searchForm.tekiyouDate"
                ref="selectDate"
                :required="true"
                type="daterange"
                format="yyyy/MM/dd"
                :clearable="false"
                :width="'260px'"
                range-separator="~"
                @input="onInput"
              ></nengetsu> </el-form-item
          ></el-col>
          <el-col :span="1">
            <el-form-item prop="keijiFlg" label-width="0">
              <el-checkbox v-model="searchForm.keijiFlg"
                >現在掲示期間のみ表示</el-checkbox
              >
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-col :span="18">
              <el-form-item label="件名" prop="kenmei">
                <reafsinputtext
                  v-model="searchForm.kenmei"
                  :width="'350px'"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
          </el-col>
          <el-col
            ><el-form-item label="登録者" prop="tourokuTantoiCd">
              <opensearch
                v-model="searchForm.tourokuTantoiCd"
                :disabled="false"
                :appendlabelText="tourokuTantoiNm"
                :append="'append'"
                maxlength="7"
                :path="'reafsmodalSearchShain'"
                @backData="reafsmodalBack_searchSyouninTantoi"
              >
              </opensearch> </el-form-item
          ></el-col>
        </el-col>
        <!-- left end-->
        <!-- right-->
        <el-col :span="4">
          <el-form-item label-width="0px">
            <el-button type="primary" size="mini" @click="onSearchSubmit">
              &nbsp;検索&nbsp;
            </el-button>
          </el-form-item>
        </el-col>
        <!-- right end -->
      </el-row>
    </el-form>
    <!-- 明細部 -->
    <el-table
      v-loading="loading"
      ref="table"
      :data="tableData"
      :cell-style="{ padding: '0px' }"
      stripe
      height="540"
      style="margin-top: 10px; scroll: auto"
      :header-cell-style="headerCellStyle"
      class="el-row"
    >
      <!-- No -->
      <el-table-column
        type="index"
        width="60"
        label="No"
        align="center"
      ></el-table-column>
      <!-- 適用日 -->
      <el-table-column prop="適用日" label="適用日" align="center" width="110">
      </el-table-column>
      <el-table-column
        prop="掲示開始日"
        label="掲示開始日"
        align="center"
        width="110"
      >
      </el-table-column>
      <el-table-column
        prop="掲示終了日"
        label="掲示終了日"
        align="center"
        width="110"
      ></el-table-column>
      <el-table-column label="件名">
        <template slot-scope="scope">
          <el-link
            @click="onJumpTo(scope.row)"
            target="_blank"
            type="primary"
            :underline="false"
            >{{ scope.row ? scope.row.件名 : "" }}</el-link
          >
        </template>
      </el-table-column>
      <el-table-column
        prop="登録日"
        label="登録日"
        align="center"
        width="110"
      ></el-table-column>
      <el-table-column prop="登録組織" label="登録組織"> </el-table-column>
      <el-table-column
        prop="登録者NM"
        label="登録担当者名"
        align="left"
        width="120"
      >
      </el-table-column>
      <el-table-column label="添付" align="left" width="60">
        <!-- <el-link  >{{this.tableData.添付}}</el-link> -->
        <template slot-scope="scope">
          <el-link
            @click="onDownLoadFile(scope.row)"
            target="_blank"
            type="primary"
            :underline="false"
            >{{ scope.row ? scope.row.添付 : "" }}</el-link
          >
        </template>
        >
      </el-table-column>
    </el-table>
    <el-button @click="onSakusei" >新規作成</el-button>
    <el-button @click="onClickBack" :loading="loadingBack" style="float: right" >戻る</el-button>
  </div>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import opensearch from "@/components/basic/openSearch.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";

var $thisValue;

export default {
  components: {
    reafsinputtext,
    opensearch,
    nengetsu,
  },
  props: {
    showNo: {
      type: Boolean,
      default: true,
    },
  },

  data() {
    const validaTetekiyouDate = (rule, value, callback) => {
      if (!value) {
        this.MsgInfo({
          title: this.$store.getters.getPageTitle(),
          message: rule.popMessage,
        });
        callback(new Error(" "));
      }
    };
    // var validateuserName = (rule, value, callback) => {
    //   if (value === "") {
    //     callback(new Error("担当者コードを入力してください"));
    //   }
    // };
    // var validatepassword = (rule, value, callback) => {
    //   if (value === "") {
    //     callback(new Error("パスワードを入力してください"));
    //   }
    // };
    return {
      searchForm: {
        tekiyouDate: "", // 適用日
        tekiyouDateStart: "", //掲示開始日
        tekiyouDateEnd: "", //掲示終了日
        keijiFlg: "", //現在掲示期間のみ表示
        kenmei: "", //件名
        tourokuTantoiCd: "", // 登録者CD
      },
      tourokuTantoiNm: "", // 登録者NM
      total: 0, // 明細CNT
      tableData: [], //明細データ
      loading: false,
      rules: {
        tekiyouDate: {
          validator: validaTetekiyouDate,
          popMessage: "適用日（ＳＴ）を正しく入力して下さい。",
          trigger: "blur",
        },
        tekiyouDateStart: {
          validator: validaTetekiyouDate,
          popMessage: "適用日（ＳＴ）を正しく入力して下さい。",
        },
        tekiyouDateEnd: {
          validator: validaTetekiyouDate,
          popMessage: "適用日（ＥＤ）を正しく入力して下さい。",
        },
      },
    };
  },
  created() {
    this.init();
  },
  mounted: function () {
    this.$nextTick(function () {
      // 画面レンダー後行う
      // document
      //   .getElementsByClassName("el-table__cell gutter")[1]
      //   .setAttribute("rowSpan", 2);
    });
  },
  watch: {
    $route(to, from) {
      if (to.name == "WD00011") {
        this.init(from);
      }
    },
  },

  methods: {
    init(from) {
      $thisValue = this;
      let qs = require("qs");

      if (from && from.name == "WD00012") {
        if ($thisValue.searchForm.tekiyouDate) {
          this.onSearchSubmit();
        }
      } else {
        this.total = 0;
        this.tableData = undefined;
      }
    },
    //新規作成
    onSakusei(obj) {
      this.$router.push({
        name: "WD00012",
      });
    },
    // 戻る
    onClickBack() {
      this.$router.back()
    },
    onJumpTo(obj) {
      this.$router.push({
        name: "WD00012",
        query: {
          掲示ID: obj.掲示ID,
        },
      });
    },
    // 検索
    onSearchSubmit() {
      $thisValue = this;

      // Form CHECK
      let formCheckErrFlg = false;
      $thisValue.$refs["searchForm"].validate((valid) => {
        if (valid) {
        } else {
          formCheckErrFlg = true;
        }
      });
      if (formCheckErrFlg) {
        return false;
      }

      $thisValue.loading = true;
      $thisValue.total = 0;
      $thisValue.tableData = undefined;

      $thisValue.searchForm.tekiyouDateStart = $thisValue.formateDate(
        $thisValue.searchForm.tekiyouDate
          ? $thisValue.searchForm.tekiyouDate[0]
          : ""
      );
      $thisValue.searchForm.tekiyouDateEnd = $thisValue.formateDate(
         $thisValue.searchForm.tekiyouDate
          ? $thisValue.searchForm.tekiyouDate[1]
          : ""
      );
      this.http
        .post("api/Reafs_W/Common/WD00011/search", this.searchForm)
        .then((result) => {
          if (!result.status) {
            $thisValue.loading = false;
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: result.message,
            });
          }

          $thisValue.tableData = result.data;
          if (result.data) {
            $thisValue.total = result.data.length;
          }
          $thisValue.loading = false;
        });
    },
    onInput(...args) {
      let $thisValue = this;
      $thisValue.searchForm.tekiyouDateStart = $thisValue.formateDate(
        $thisValue.searchForm.tekiyouDate
          ? $thisValue.searchForm.tekiyouDate[0]
          : ""
      );
      $thisValue.searchForm.tekiyouDateEnd = $thisValue.formateDate(
         $thisValue.searchForm.tekiyouDate
          ? $thisValue.searchForm.tekiyouDate[1]
          : ""
      );
    },
    onDownLoadFile(row) {
      let $thisValue = this;
      let $thisRow = row;
      let fileFullPath = {
        FilePath: row.FILE_PATH,
        FileName: row.FILENAME,
      };
      this.http
        .post("api/Reafs_T/Common/DownloadFile", fileFullPath, "", {
          responseType: "blob",
        })
        .then(async (blob) => {
          // const url = window.URL.createObjectURL(blob);
          // const link = document.createElement("a");
          // link.href = url;
          // link.setAttribute("download", $thisRow.FILENAME);
          // document.body.appendChild(link);
          // link.click();
          // document.body.removeChild(link);
          await this.base.saveFileDownload(blob, $thisRow.FILENAME)
        });
    },

    headerCellStyle() {
      return "text-align: center;background-color:#BDD7EE";
    },
    formateDate(dDate) {
      if (!dDate) {
        return "";
      }
      let strTemp = dDate.toLocaleDateString().split("/");
      let mm = ("0" + strTemp[1])
        .split("")
        .reverse()
        .join("")
        .substr(0, 2)
        .split("")
        .reverse()
        .join("");
      let dd = ("0" + strTemp[2])
        .split("")
        .reverse()
        .join("")
        .substr(0, 2)
        .split("")
        .reverse()
        .join("");
      return strTemp[0] + "/" + mm + "/" + dd;
    },
    SetsubParamsObj(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj));
      tempSubParamsObj.torType = "1";
      return tempSubParamsObj;
    },
    pageBack(obj) {
      this.pagination.page = obj;
    },

    // 登録者
    reafsmodalBack_searchSyouninTantoi(obj) {
      this.searchForm.tourokuTantoiCd = obj.txtCd;
      this.tourokuTantoiNm = obj.lblNm;
    },
  },
};
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 20px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.home-contianer {
  margin-top: 20px;
  padding: 10px;
  // width: 1024px;
}
.home-contianer .el-form-item {
  margin-bottom: 20px;
}

.el-form-item {
  margin-bottom: 0px;
}

.el-button--text {
  color: black;
}
.labelVCenter {
  height: 30px;
  line-height: 30px;
}
</style>
<style scoped>
.el-form >>> .orange .el-form-item__label {
  background: orange !important;
}
.margin_right_60 {
  margin-right: 60px;
}
.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
.el-table >>> .el-table__cell {
  padding-top: 5px;
  padding-bottom: 5px;
  border-right-style: solid;
  border-right-width: 1px;
  border-right-color: rgb(235, 238, 245);
}
</style>