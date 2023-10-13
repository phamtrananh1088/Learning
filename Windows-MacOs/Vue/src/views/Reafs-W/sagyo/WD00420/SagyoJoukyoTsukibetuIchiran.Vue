<template>
  <div class="home-contianer">
    <!-- 検索部 -->
    <el-header>
      <el-row>
        <el-col :span="5">
          <!--1行目-->
          <el-form @submit.native.prevent>
            <el-form-item>
              <div class="hiduke">
                <el-date-picker
                  tabindex="1"
                  v-model="calendarDay"
                  type="month"
                  format="yyyy年MM月"
                  :width="'300px'"
                  @change="fnc_change_count()"
                  ref="ref日付"
                ></el-date-picker>
              </div>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="9">
          <el-form label-width="150px" @submit.native.prevent>
            <el-form-item label="営業店">
              <opensearch
                :class="{ pinkcolor: isErrPinkEigyouten }"
                v-model="KensakuJoken.txtEigyotenCode"
                width="100px"
                :disabled="false"
                :appendlabelText="KensakuJoken.txtEigyoten"
                :append="'append'"
                :path="'reafsmodalSearchMise'"
                maxlength="6"
                @backData="reafsmodalBackEigyoten"
                ref="ref営業店"
                @change="fnc_click_eigyouten()"
              >
              </opensearch>
            </el-form-item>
          </el-form>

          <div class="tyuuigaki">
            <ul>
              <li class="color_blue">青字：予定</li>
              <li class="color_red">赤字：遅延</li>
              <li class="color_green">緑字：完了</li>
              <li class="spot">★：スポット</li>
            </ul>
          </div>
        </el-col>
        <el-col :span="2">
          <div class="kensaku">
            <!--検索ボタン-->
            <el-button tabindex="3" @click="onSearch">検索</el-button>
          </div>
        </el-col>
      </el-row>
    </el-header>

    <div class="body">
      <!-- カレンダー -->
      <div class="calendar">
        <el-calendar
          v-model="calendarDay"
          first-day-of-week="7"
          :showFullWeekName="true"
          :calendarPadZero="false"
          :key="CalendarSetteiKey"
          ref="refCalendar"
          class="CalendarSettei"
          readonly="cakendar_dis"
        >
          <template slot="dateCell" slot-scope="{ date, data }">
            <div
              class="sagyou"
              :class="getDayCellClass(data)"
              @dblclick="ChooseChanged(date, data)"
              style="width:100%;height:100%"
            >
              {{ Number(data.day.split("-")[2]) }}<br />
              <label :class="syuuzen_color(data)">{{
                data.修繕工事 ? "修繕工事" : ""
              }}</label
              ><br />
              <label :class="spsyuzen_color(data)">{{
                data.スポット修繕 ? "スポット修繕" : ""
              }}</label
              ><br />
              <label :class="spseisou_color(data)">{{
                data.スポット清掃 ? "スポット清掃" : ""
              }}</label
              ><br />
              <label :class="zesei_color(data)">{{
                data.点検是正 ? "点検是正" : ""
              }}</label
              ><br />
              <label :class="teiki_color(data)">{{
                data.定期委託 ? "定期委託" : ""
              }}</label>
            </div>
          </template>
        </el-calendar>
      </div>
      <div class="syousai">
        <div class="syousai_fiduke_div">
          <p class="syousai_fiduke_p">{{ select_day }}</p>
        </div>
        <reafstable
          :single="single"
          :rowKey="rowKey"
          :tableData="syousai_row"
          :columns="columns"
          :height="700"
          :width="400"
          :columnIndex="false"
          :columnChkbox="false"
          :key="Math.random()"
          :row-class-name="tableRowClassName"
          @keydown.native="down2first"
        >
        </reafstable>
      </div>
    </div>
    <el-footer>
      <div class="modoru">
        <!--戻るボタン-->
        <el-button tabindex="3" @click="onBack" :loading="loadingBack"
          >戻る</el-button
        >
      </div>
    </el-footer>
  </div>
</template>

<script>
import reafsmodal from "@/components/modal/Modal.vue";
import opensearch from "@/components/basic/openSearch.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import reafstable from "@/components/table/Table.vue";

export default {
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.initialize(); // 初期化処理
      next();
    });
  },
  ///////////////////////////////////////////////
  // コンポーネントタグ
  ///////////////////////////////////////////////
  components: {
    reafstable,
    reafsmodal,
    opensearch,
    nengetsu
  },
  data() {
    return {
      userInfo: {
        ユーザーＩＤ: "",
        会社コード: ""
      },
      queryParams: {
        kojiKubun: "",
        eigyotenCode: "",
        eigyoten: ""
      },

      loadingBack: false,

      ///////////////////////////////////////////////
      // 検索部
      ///////////////////////////////////////////////
      KensakuJoken: {
        // 工事区分
        kojiKubun: "",
        // 営業店 コード
        txtEigyotenCode: "",
        // 営業店 コード枝番
        txtEigyotenCodeEdaban: "",
        // 営業店 名称
        txtEigyoten: ""
      },
      uketori: {
        eigyou_cd: "",
        nengetu: ""
      },
      isErrPinkEigyouten: false,
      名称取得方法17: {},

      ///////////////////////////////////////////////
      // カレンダー
      ///////////////////////////////////////////////
      calendarDay: "",
      calendar_val: [],
      cakendar_dis: true,

      searchForm: {
        calendarSelectVal: ""
      },
      calendarItems: [],
      input: "未設定",

      // date: new Date(),
      chooseDates: [],

      CalendarSetteiKey: 0,
      change_carender_date: "",

      ///////////////////////////////////////////////
      // 詳細表示
      ///////////////////////////////////////////////
      select_day: "",
      syousai_row: [],
      count_change: 0,
      columns: [
        {
          title: "作業予定時間",
          width: 22,
          align: "left",
          render: (h, { row, column, index }) => {
            return (
              <div>
                <label>{row.予定時間}</label>
              </div>
            );
          }
        },
        {
          title: "作業名称/備考",
          width: 50,
          align: "left",
          render: (h, { row, column, index }) => {
            return (
              <div>
                <label>{row.作業名称}</label>
                <br />
                <label>{row.予定内容}</label>
              </div>
            );
          }
        }
      ],

      ///////////////////////////////////////////////
      // エラーフラグ DATEPICKERをピンクにする為に使用
      ///////////////////////////////////////////////
      isErrPinkSagyotsuki: false
    };
  },
  props: {
    showFullWeekName: {
      type: Boolean,
      default: true
    },
    weekName: {
      type: String,
      default: "曜日"
    },
    // 2022/01/02  true:02  false :2
    calendarPadZero: {
      type: Boolean,
      default: false
    },
    calendarInitDay: {
      type: Date,
      default: () => {
        return new Date();
      }
    }
  },
  computed: {},
  created() {},
  mounted() {
    console.log("mounted_WD00420");

    //月 →月曜日のフラグ
    if (this.showFullWeekName) {
      this.$nextTick(function() {
        let tableTitle = document.getElementsByClassName("el-calendar-table");
        for (let itemTemp of Array.from(tableTitle[0].children[0].childNodes)) {
          itemTemp.innerHTML = itemTemp.innerHTML + this.weekName;
        }
      });
    }
  },

  methods: {
    initialize() {
      var now = new Date();

      // 工事区分
      this.KensakuJoken.kojiKubun = this.$route.query.kojiKubun;
      // コード uketori.eigyou_cd
      this.KensakuJoken.txtEigyotenCode = this.$route.query.txtEigyotenCode;
      // 名称
      this.KensakuJoken.txtEigyoten = this.$route.query.txtEigyoten;
      //年月 uketori.nengetu
      this.uketori.nengetu = this.$route.query.JissiYoteiNengetsu;

      console.log("init_WD00420");

      this.calendarDay = this.uketori.nengetu;
      let karioki = this.uketori.nengetu;
      if (karioki.constructor.name == "Date") {
        karioki = this.convert_date(karioki);
      } else {
      }
      this.change_carender_date = karioki;

      console.log(
        "initialize_onSearchStartingNow " + this.change_carender_date
      );
      this.onSearch();
    },

    hideLoading() {
      this.loading = false;
      this.loading削除 = false;
      this.loading_申請 = false;
      this.loading_取下 = false;
      this.loadingBack = false;
      this.loading_登録_更新 = false;
    },

    ///////////////////////////////////////////////
    // エラーメッセージ出力
    ///////////////////////////////////////////////
    fnc_void_showErrorMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "WD00420",
          message: response.data
        });
      });
    },

    ///////////////////////////////////////////////
    // 営業店データ取得
    ///////////////////////////////////////////////
    reafsmodalBackEigyoten(obj) {
      if (obj.txtCd.length >= 1) {
        // コード
        this.KensakuJoken.txtEigyotenCode = obj.txtCd;
        // // 名称
        this.KensakuJoken.txtEigyoten = obj.lblNm;
      } else {
      }
    },

    // 年月コンバート
    convert_date(date) {
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      month = ("00" + month).slice(-2);
      let fulldate = year + "/" + month;

      return fulldate;
    },

    ///////////////////////////////////////////////
    // カレンダー
    ///////////////////////////////////////////////
    async onSearch() {
      console.log("onSearchStart");

      //詳細画面初期化
      this.select_day = "";
      this.syousai_row = [];
      this.calendar_val = [];

      if (!this.KensakuJoken.txtEigyotenCode) {
        this.fnc_void_showInfoMsg("E040005");
        this.isErrPinkEigyouten = true;
        this.$refs.ref営業店.focus();
        return false;
      } else if (!this.calendarDay) {
        this.fnc_void_showInfoMsg("E010333");
        this.$refs.ref日付.focus();
        return false;
      } else {
        let kekka = await this.fnc_click_eigyouten();
        if (kekka) {
          this.count_change = 0;
          this.isErrPinkEigyouten = false;
          let karioki = this.calendarDay;
          if (karioki.constructor.name == "Date") {
            karioki = this.convert_date(karioki);
          } else {
          }
          this.change_carender_date = karioki;

          await this.fnc_表示用検索();
          this.getDayCellClass();
        } else {
          return false;
        }
      }
    },
    // 月選択，日付クリック
    // 表示している日付分イベントが発生する
    getDayCellClass(slotData) {
      let data_list = new Object();
      data_list = this.calendar_val;

      const kekka = data_list.find(x => x.hiduke === slotData.day);
      if (kekka) {
        slotData.修繕工事 = kekka.修繕工事;
        slotData.スポット修繕 = kekka.スポット修繕;
        slotData.スポット清掃 = kekka.スポット清掃;
        slotData.点検是正 = kekka.点検是正;
        slotData.定期委託 = kekka.定期委託;
      }
    },
    syuuzen_color(data) {
      let data_list = new Object();
      data_list = this.calendar_val;

      const kekka = data_list.find(x => x.hiduke === data.day);
      if (kekka) {
        let color_class = kekka.修繕工事_色;
        return color_class;
      }
    },
    spsyuzen_color(data) {
      let data_list = new Object();
      data_list = this.calendar_val;

      const kekka = data_list.find(x => x.hiduke === data.day);
      if (kekka) {
        var color_class = kekka.スポット修繕_色;
        return color_class;
      }
    },
    spseisou_color(data) {
      let data_list = new Object();
      data_list = this.calendar_val;

      const kekka = data_list.find(x => x.hiduke === data.day);
      if (kekka) {
        var color_class = kekka.スポット清掃_色;
        return color_class;
      }
    },
    zesei_color(data) {
      let data_list = new Object();
      data_list = this.calendar_val;

      const kekka = data_list.find(x => x.hiduke === data.day);
      if (kekka) {
        var color_class = kekka.点検是正_色;
        return color_class;
      }
    },
    teiki_color(data) {
      let data_list = new Object();
      data_list = this.calendar_val;

      const kekka = data_list.find(x => x.hiduke === data.day);
      if (kekka) {
        var color_class = kekka.定期委託_色;
        return color_class;
      }
    },

    // 日付ダブルクリック
    async ChooseChanged(slotDate, slotData) {
      if (slotData.type != "current-month") {
        return;
      }

      let letval = new Object();
      let kensakudate = this.calendarDay;
      let syousai_kekka = new Object();
      //年月・営業店取得
      if (kensakudate.constructor.name == "Date") {
        letval.select_year = kensakudate.getFullYear();
        letval.select_month = kensakudate.getMonth() + 1;
        letval.select_month = ("00" + letval.select_month).slice(-2);
      } else {
        letval.select_year = kensakudate.substr(0, 4);
        letval.select_month = slokensakudatetDate.substr(5, 2);
      }
      letval.select_nengetu =
        letval.select_year.toString() + "/" + letval.select_month.toString();
      letval.eigyou_CD = this.KensakuJoken.txtEigyotenCode.substr(0, 4);
      letval.eigyou_CD_eda = this.KensakuJoken.txtEigyotenCode.substr(4, 2);
      letval.hiduke = slotData.day;

      this.fnc_日付取得(letval);

      let karioki = this.calendarDay;
      karioki = this.convert_date(karioki);

      if (this.count_change == 0 && this.change_carender_date == karioki) {
        await this.http
          .post(
            "/api/Reafs_W/SagyoJyokyoCalendar/WD00420_GET_Syousai",
            letval,
            "表示しています...."
          )
          .then(res => {
            if (res.status) {
              this.syousai_row = res.data;
              this.count_change = 0;
            } else {
              console.log(res.message);
              this.MsgErr({
                title: "作業状況カレンダー",
                message: res.message
              });
            }
          })
          .finally(() => {});
      } else {
        this.fnc_void_showInfoMsg("E020331");
      }
    },
    async fnc_表示用検索() {
      let letval = new Object();
      let slotDate = this.calendarDay;

      //年月・営業店取得
      if (slotDate.constructor.name == "Date") {
        letval.select_year = slotDate.getFullYear();
        letval.select_month = slotDate.getMonth() + 1;
        letval.select_month = ("00" + letval.select_month).slice(-2);
      } else {
        letval.select_year = slotDate.substr(0, 4);
        letval.select_month = slotDate.substr(5, 2);
      }
      letval.select_nengetu =
        letval.select_year.toString() + "/" + letval.select_month.toString();
      letval.eigyou_CD = this.KensakuJoken.txtEigyotenCode.substr(0, 4);
      letval.eigyou_CD_eda = this.KensakuJoken.txtEigyotenCode.substr(4, 2);
      letval.hiduke = "";

      await this.http
        .post(
          "/api/Reafs_W/SagyoJyokyoCalendar/WD00420_GET_KensakuKekka",
          letval,
          "表示しています...."
        )
        .then(res => {
          if (res.status) {
            this.calendar_val = res.data;
          } else {
            console.log(res.message);
            this.MsgErr({
              title: "作業状況カレンダー",
              message: res.message
            });
          }
        })
        .finally(() => {});
    },

    async fnc_日付取得(letval) {
      await this.http
        .post(
          "/api/Reafs_W/SagyoJyokyoCalendar/WD00420_GET_hidukesyutoku",
          letval,
          "表示しています...."
        )
        .then(res => {
          if (res.status) {
            this.select_day = res.data[0].hiduke_title;
          } else {
            console.log(res.message);
            this.MsgErr({
              title: "作業状況カレンダー",
              message: res.message
            });
          }
        })
        .finally(() => {});
    },
    //変更回数カウント
    fnc_change_count() {
      this.count_change += 1;
    },
    //営業店参照チェック
    async fnc_click_eigyouten() {
      this.fnc_change_count();

      let letval = new Object();

      letval.select_year = "";
      letval.select_month = "";
      letval.select_nengetu = "";
      letval.eigyou_CD = "";
      letval.eigyou_CD_eda = "";
      letval.hiduke = "";

      await this.http
        .post(
          "/api/Reafs_W/SagyoJyokyoCalendar/WD00420_GET_loginkengen",
          letval,
          "表示しています...."
        )
        .then(res => {
          if (res.status) {
            this.名称取得方法17 = res.data;
            console.log(this.名称取得方法17);
          } else {
            console.log(res.message);
            this.MsgErr({
              title: "作業状況カレンダー",
              message: res.message
            });
          }
        })
        .finally(() => {});
      var 営業店 = this.KensakuJoken.txtEigyotenCode;
      if (営業店 != "") {
        let syori = this.check参照権限();
        return syori;
      } else {
        return false;
      }
    },
    check参照権限() {
      var 営業店 = this.KensakuJoken.txtEigyotenCode.substr(0, 4);
      var 営業店枝番 = this.KensakuJoken.txtEigyotenCode.substr(4, 2);
      return new Promise(async resolve => {
        if (
          this.名称取得方法17.find(
            x => x.営業所コード && x.営業所コード.substring(0, 4) === "9900"
          )
        ) {
          return resolve(true);
        }
        if (
          this.名称取得方法17 &&
          this.名称取得方法17.filter(
            x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7
          ).length > 0
        ) {
          const allPOSITIVE7Length = this.名称取得方法17.filter(
            x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7
          );
          const allRange = allPOSITIVE7Length.map(
            x => x.支社コード + x.支社コード枝番
          );
          const res = await this.get営業店CheckData(allRange);
          if (
            !res.data.range名称取得方法18 ||
            !res.data.range名称取得方法18.find(
              x => x.店コード === 営業店 && x.店コード枝番 === 営業店枝番
            )
          ) {
            if (!res.data.店コード) {
              this.fnc_void_showInfoMsg("E040662");
              this.KensakuJoken.txtEigyotenCode = "";
              this.KensakuJoken.txtEigyoten = "";
              this.isErrPinkEigyouten = true;
              this.$refs.ref営業店.focus();
              return resolve(false);
            } else {
              return resolve(true);
            }
          } else {
            return resolve(true);
          }
        } else {
          const res = await this.get営業店CheckData([]);
          if (!res.data.店コード) {
            this.fnc_void_showInfoMsg("E040662");
            this.KensakuJoken.txtEigyotenCode = "";
            this.KensakuJoken.txtEigyoten = "";
            this.isErrPinkEigyouten = true;
            this.$refs.ref営業店.focus();
            return resolve(false);
          } else {
            return resolve(true);
          }
        }
      });
    },
    get営業店CheckData(allRange) {
      var 営業店CD = this.KensakuJoken.txtEigyotenCode.substr(0, 4);
      var 営業店CD枝番 = this.KensakuJoken.txtEigyotenCode.substr(4, 2);
      return new Promise(resolve => {
        this.http
          .post("/api/Reafs_W/SagyoJyokyoCalendar/get営業店CheckData", {
            lst支社コード: allRange,
            店コード: 営業店CD,
            店コード枝番: 営業店CD枝番
          })
          .then(response => {
            if (response.status) {
            } else {
              console.log(response.message);
              this.MsgErr({
                title: "作業状況カレンダー",
                message: response.message
              });
            }
            console.log(response);
            return resolve(response);
          });
      });
    },

    ///////////////////////////////////////////////
    // 戻るボタン押下
    ///////////////////////////////////////////////
    onBack() {
      this.loadingBack = true;
      let isChange = false;
      isChange = true;
      if (isChange) {
        this.loadingBack = false;
        // this.$router.push({ name: 'Whome' })
        this.$router.back();
      }
    },
    ///////////////////////////////////////////////
    // keyからS016メッセージマスタにアクセスしメッセージ取得
    ///////////////////////////////////////////////
    fnc_rtn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },
    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    ///////////////////////////////////////////////
    // エラーメッセージ出力
    ///////////////////////////////////////////////
    // お知らせメッセージ

    fnc_void_showInfoMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "作業状況カレンダー",
          message: response.data
        });
      });
    }
  },

  watch: {
    testkey: function(newval) {
      newval = newval.replace(/[^0-9]/g, "");
      this.testkey = newval;
    }
  }
};
</script>

<style lang="less" scoped>
.home-contianer {
  padding: 10px;
  min-width: 100%;
  height: 100%;
}
// 検索部の各行の高さ幅を狭める
.el-form-item {
  margin-bottom: 1px;
}
// ラベル⇔チェックボックス間に余白を作る
.cls-chk-paddnig-left {
  padding-left: 5px;
}
// 確定チェックボックスを左に詰める
.cls-kakutei-margin-left {
  margin-left: -25px;
}

// ボタンの高さをラベルに合わせる
.el-button {
  padding-top: 7px;
  padding-bottom: 7px;
}

// 検索ヒット件数右寄せ
.cls-hitkensu_align_right {
  text-align: right;
}
.tyuuigaki {
  position: absolute;
  margin: 0 auto;
}
.tyuuigaki li {
  float: left;
  margin: 0 10px;
  list-style: none;
}
.calendar {
  float: left;
  width: 900px;
  margin-right: 1px;
}
.syousai {
  float: left;
  width: 329px;
  height: 100%;
}
.modoru {
  float: right;
  margin: 5px 62px 0 0;
}
.body {
  width: 1316px;
  height: 100%;
}
.body:after {
  content: "";
  display: block;
  clear: both;
}
.syousai_fiduke_div {
  padding-left: 5px;
  background: transparent;
  border: solid lightgrey 1px;
  display: flex;
  align-items: center;
  overflow: hidden;
  width: 330px;
  height: 30px;
}
.syousai_fiduke_p {
  padding: 5px;
  margin: 0 auto;
  align-items: center;
  text-align: center;
}
.color_blue {
  color: blue;
}
.color_red {
  color: red;
}
.color_green {
  color: green;
}
</style>

<style scoped>
/* 作業月エラーピンク */
.pinkcolor >>> .el-input__inner {
  background-color: lightpink !important;
}
</style>

<!--カレンダー-->
<style lang="less" scoped>
.button-row {
  display: flex;
  justify-content: space-between;
}
.sagyou {
  white-space: ore-line;
  display: inline-block;
}
</style>

<style scoped>
/* ? */
.el-form >>> .orange .el-form-item__label {
  background: orange;
}

/* 年月日表示 */
.el-calendar >>> .el-calendar__title {
  display: none;
}

/* 前月，翌月ボタン配置設定 */
.el-calendar >>> .el-button-group {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

/* 前月，翌月ボタン配置設定 */
.el-calendar >>> .el-calendar__button-group {
  width: 100%;
  text-align: center;
}

/* 前月，翌月ボタン配置設定 */
.el-calendar >>> .el-button-group::after,
.el-calendar >>> .el-button-group::before {
  display: table;
  content: none;
}

/* 今日ボタン非表示 */
.el-calendar
  >>> .el-button-group
  > .el-button:not(:first-child):not(:last-child) {
  display: none;
}

/* ? */
.date-title {
  text-align: center;
  transform: translateY(55px);
}

/* 曜日の枠線 */
.el-calendar >>> .el-calendar-table {
  border-collapse: collapse;
}

/* 曜日の枠線 */
.el-calendar >>> .el-calendar-table thead th {
  background: #bdd7ee;
  border: 0.1px solid;
}

/* 当月の土日の色 */
.el-calendar >>> .el-calendar-table .current:nth-child(1) {
  background-color: lightpink;
}
.el-calendar >>> .el-calendar-table .current:nth-child(7) {
  background-color: lightpink;
}

/* ? */
.el-calendar >>> .el-calendar-table .current :hover .is-choose {
  background-color: pink;
}

/* 当月の土日の選択時の色 */
.el-calendar >>> .el-calendar-table .current:nth-last-child(1) :hover {
  background-color: pink;
  widows: 100%;
}
.el-calendar >>> .el-calendar-table .current:nth-last-child(7) :hover {
  background-color: pink;
}

/* 当月以外の土日の色 */
.el-calendar >>> .el-calendar-table .prev:nth-child(1),
.el-calendar >>> .el-calendar-table .next:nth-child(1) {
  background-color: pink;
}
.el-calendar >>> .el-calendar-table .prev:nth-child(7),
.el-calendar >>> .el-calendar-table .next:nth-child(7) {
  background-color: pink;
}

/* 次月の土日の選択時の色 */
.el-calendar >>> .el-calendar-table .next:nth-last-child(1) :hover {
  background-color: rgb(247, 213, 219);
}
.el-calendar >>> .el-calendar-table .next:nth-last-child(7) :hover {
  background-color: rgb(247, 213, 219);
}

/* 前月の土日の選択時の色 */
.el-calendar >>> .el-calendar-table .prev:nth-last-child(1) :hover {
  background-color: rgb(247, 213, 219);
}
.el-calendar >>> .el-calendar-table .prev:nth-last-child(7) :hover {
  background-color: rgb(247, 213, 219);
}

/* 日付 箱のサイズ */
.el-calendar >>> .el-calendar-table .el-calendar-day {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding: 0px;
  height: 120px;
  width: 122px;
}
.hiduke >>> .el-input__inner {
  height: 50px;
  line-height: 40px;
  border-radius: 1px;
  padding-right: 5px;
  font-size: xx-large;
  width: 220px;
}
.CalendarSettei .el-button-group button:nth-child(1):after {
  content: "<<前月";
  text-indent: 0;
  display: block;
  line-height: initial;
  font-size: 14px;
  font-family: Meiryo;
}
.CalendarSettei .el-button-group button:nth-child(3):after {
  content: "翌月>>";
  text-indent: 0;
  display: block;
  line-height: initial;
  font-size: 14px;
  font-family: Meiryo;
}
</style>
