<template>
  <div class="home-contianer">
    <el-menu
      :default-active="activeIndex"
      class="el-menu-class"
      mode="horizontal"
      @select="handleSelect"
    >
      <el-menu-item index="1">サンプルフォーム</el-menu-item>
      <el-menu-item index="2">nengetsu</el-menu-item>
      <el-menu-item index="3">要望依頼2</el-menu-item>
      <el-menu-item index="4">カレンダー</el-menu-item>
      <el-menu-item index="5">カレンダー2</el-menu-item>
    </el-menu>
    <!-- サンプルフォーム -->
    <el-row :gutter="20" v-if="selectIndex == '1'">
      <el-col :span="12">
        <el-form ref="form" :model="form" label-width="150px">
          <el-form-item label="入力フォーム">
            <el-input
              v-model="form.name"
              maxlength="10"
              class="el-input-len-10 required"
            ></el-input>
          </el-form-item>
          <el-form-item label="入力フォーム(検索)">
            <el-input
              placeholder=""
              v-model="form.nameSearch"
              maxlength="5"
              class="input-with-select el-input-serach-len-5"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="showDialog = true"
              ></el-button>
            </el-input>
          </el-form-item>
          <el-form-item label="ドロップダウン">
            <el-select v-model="form.region" placeholder="---">
              <el-option label="Reafs-W" value="1"></el-option>
              <el-option label="Reafs-T" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日付選択">
            <el-date-picker
              type="date"
              placeholder=""
              v-model="form.date1"
              class="el-input-date-len-10"
            ></el-date-picker>
            <el-time-picker
              placeholder=""
              v-model="form.date2"
              class="el-input-date-len-10"
            ></el-time-picker>
          </el-form-item>
          <el-form-item label="トグルボタン">
            <el-switch v-model="form.delivery"></el-switch>
          </el-form-item>
          <el-form-item label="チェックボックス">
            <el-checkbox-group v-model="form.type">
              <el-checkbox label="選択肢１" name="type"></el-checkbox>
              <el-checkbox label="選択肢２" name="type"></el-checkbox>
              <el-checkbox label="選択肢３" name="type"></el-checkbox>
              <el-checkbox label="選択肢４" name="type"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="ラジオボタン">
            <el-radio-group v-model="form.resource">
              <el-radio label="選択肢A"></el-radio>
              <el-radio label="選択肢B"></el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="テキストエリア" class="textarea-class">
            <el-input
              type="textarea"
              v-model="form.desc"
              maxlength="300"
              show-word-limit
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit">登録</el-button>
            <el-button>キャンセル</el-button>
            <el-button type="primary" @click="onSubmitInfo">info</el-button>
            <el-button type="primary" @click="onSubmitErr">error</el-button>
            <el-button type="primary" @click="onSubmitConfirm"
              >confirm</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>

      <!-- 共通コントロール　サンプル -->
      <el-col :span="12">
        <el-form
          :model="sampleForm"
          :rules="rules"
          ref="sampleForm"
          label-width="150px"
          class="sample-form-class"
        >
          <el-form-item label="入力エリア(必須)" prop="nyuryokuerea1">
            <reafsinputtext
              maxlength="10"
              v-model="sampleForm.nyuryokuerea1"
              :width="'150px'"
              :required="true"
            ></reafsinputtext>
          </el-form-item>
          <el-form-item label="入力エリア" prop="nyuryokuerea2">
            <reafsinputtext
              v-model="sampleForm.nyuryokuerea2"
              :width="'150px'"
            ></reafsinputtext>
          </el-form-item>
          <el-form-item label="入力数値エリア" prop="nyuryokuerea3">
            <reafsinputnumber
              v-model="sampleForm.nyuryokuerea3"
              :value="sampleForm.nyuryokuerea3"
              :width="'150px'"
              :required="true"
              autocomplete="off"
            ></reafsinputnumber>
          </el-form-item>
          <el-form-item label="日付選択" prop="nyuryokuerea6">
            <el-date-picker
              type="date"
              placeholder=""
              format="yyyy/MM/dd"
              v-model="sampleForm.nyuryokuerea6"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="入力エリア(日付)" prop="nyuryokuerea4">
            <el-date-picker
              v-model="sampleForm.nyuryokuerea4"
              type="daterange"
              format="yyyy/MM/dd"
              class="date-picker"
              range-separator="~"
              start-placeholder="開始日"
              end-placeholder="終了日"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="入力エリア(検索)" prop="nyuryokuerea5">
            <reafsinputtext
              maxlength="10"
              v-model="sampleForm.nyuryokuerea5"
              :width="'150px'"
              :required="true"
            >
              <template v-slot:append>
                <el-button
                  icon="el-icon-search"
                  @click="showDialog = true"
                ></el-button>
                <el-button
                  icon="el-icon-search"
                  @click="showDialog = true"
                ></el-button>
              </template>
            </reafsinputtext>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <reafsmodal
      :dialogVisible.sync="showDialog"
      @backData="reafsmodalBack"
    ></reafsmodal>

    <!-- nengetsu -->
    <el-row :gutter="20" v-if="selectIndex == '2'">
      <el-col :span="8">
        <el-form
          :model="iraiForm1"
          ref="iraiForm1"
          label-width="120px"
          class="sample-form-class"
        >
          <el-form-item label="年月日" prop="nyuryokuerea1">
            <nengetsu
              v-model="iraiForm1.nyuryokuerea1"
              :required="true"
              type="date"
              format="yyyy/MM/dd"
              :clearable="false"
              :width="'120px'"
            ></nengetsu>
          </el-form-item>
          <el-form-item label="年月" prop="nyuryokuerea2">
            <nengetsu
              v-model="iraiForm1.nyuryokuerea2"
              :required="true"
              type="month"
              format="yyyy/MM"
              :clearable="false"
              :width="'100px'"
            ></nengetsu>
          </el-form-item>
          <el-form-item label="年月日範囲" prop="nyuryokuerea3">
            <nengetsu
              v-model="iraiForm1.nyuryokuerea3"
              :required="true"
              type="daterange"
              format="yyyy/MM/dd"
              :clearable="false"
              :width="'260px'"
              range-separator="~"
              :class="{ pinkcolor: isActive }"
            ></nengetsu>
          </el-form-item>
          <el-button @click="colorChange">colorChange</el-button>
          <el-form-item label="年月範囲" prop="nyuryokuerea4">
            <nengetsu
              v-model="iraiForm1.nyuryokuerea4"
              :required="true"
              type="monthrange"
              format="yyyy/MM"
              :clearable="false"
              :width="'260px'"
              range-separator="~"
            ></nengetsu>
          </el-form-item>
          <el-form-item label="時間" prop="nyuryokuerea5">
            <timepicker
              v-model="iraiForm1.nyuryokuerea5"
              :required="true"
              :width="'120px'"
            ></timepicker>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="16">
        <el-form
          :model="iraiForm1"
          ref="iraiForm1"
          label-width="120px"
          class="sample-form-class"
        >
          <el-form-item label="年月日時間範囲" prop="nyuryokuerea6">
            <nengetsuhirange
              v-bind.sync="iraiForm1.nyuryokuerea6"
              :required="true"
              :clearable="false"
            ></nengetsuhirange>
          </el-form-item>
          <el-form-item label="日付だけ必須" prop="nyuryokuerea6">
            <nengetsuhirange
              v-bind.sync="iraiForm1.nyuryokuerea6"
              :dateStartRequired="true"
              :dateEndRequired="true"
              :timeStartRequired="false"
              :timeEndRequired="false"
              :clearable="false"
              :dateStartformat="'yyyyMMdd'"
              :dateEndformat="'yyyy/MM/dd'"
            ></nengetsuhirange>
          </el-form-item>
          <el-form-item label="時間だけ必須" prop="nyuryokuerea6">
            <nengetsuhirange
              v-bind.sync="iraiForm1.nyuryokuerea6"
              :dateStartRequired="false"
              :dateEndRequired="false"
              :timeStartRequired="true"
              :timeEndRequired="true"
              :clearable="false"
            ></nengetsuhirange>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <!-- ドロップダウンリスト -->
    <el-row :gutter="20" v-if="selectIndex == '3'">
      <el-col :span="8">
        <el-form
          :model="sampleForm"
          :rules="rules"
          ref="sampleForm"
          label-width="150px"
          class="sample-form-class"
        >
          <el-form-item label="入力エリア(必須)" prop="nyuryokuerea1">
            <reafsinputtext
              maxlength="10"
              v-model="sampleForm.nyuryokuerea1"
              :width="'150px'"
              :required="true"
            ></reafsinputtext>
          </el-form-item>
        </el-form>
        <reafsdropdown></reafsdropdown>
      </el-col>
    </el-row>
    <!-- カレンダー -->
    <el-row :gutter="20" v-if="selectIndex == '4'">
      <el-form
        :model="searchForm"
        :rules="rules"
        ref="searchForm"
        label-width="120px"
        class="search-form-class"
      >
        <el-row>
          <el-col :span="20" :xs="24">
            <el-col :span="15" :xs="12">
              <el-form-item label="対象" class="orange">
                <el-radio-group v-model="searchForm.calendarSelectVal">
                  <!-- <el-radio
                    v-for="item in searchForm.calendarItems"
                    :key="item.value"
                    :label="item.value"
                  >
                    {{ item.text }}
                  </el-radio> -->
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="15" :xs="12">
            <hr />
            <div style="display: flex; justify-content: center">
              <div
                style="
                  border: 1px;
                  width: 60px;
                  height: 25px;
                  background-color: #ff7171;
                  display: inline-block;
                "
              ></div>
              <label>休日</label>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="15" :xs="12">
          <div class="date-title">
            <el-form-item>
              <el-date-picker
                type="month"
                format="yyyy/MM"
                v-model="searchForm.date"
              ></el-date-picker>
              <span>{{ input }}</span>
            </el-form-item>
          </div>
          <el-calendar  v-model="searchForm.date" :first-day-of-week=7>
            <template slot="dateCell" slot-scope="{ date,data }">
              <div
              @dblclick="ChooseChanged(date,data)"
              :class="isChoose(date, data) ? 'is-choose':''"
              style="width:100%;height:100%"
              >
                  {{ data.day.split("-")[2] }}
                  <!-- {{ data.isSelected ? "✔️" : "" }} -->
                </div>
              </template>
            </el-calendar>
          </el-col>
        </el-row>
      </el-form>
    </el-row>
    <el-row :gutter="20" v-if="selectIndex == '5'">
      <reafsCalendar
        @backData="reafsBack_ReafsCalendar"
        :showFullWeekName="true"
        :calendarCellAlign="'right'"
        :calendarPadZero="false"
        :isLinkButton="true"
        :prevMonthStr="'AAA'"
        :nextMonthStr="'BBB'"
        :input="'設定する'"
        :calendarWidth="'600px'"
      >
        <!-- -->
      </reafsCalendar>
    </el-row>
  </div>
</template>
<script>
import countTo from "vue-count-to";
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnumber from "@/components/basic/ReafsControl/ReafsInputNumber.vue";
import reafsmodal from "@/components/modal/Modal.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import timepicker from "@/components/basic/TimePicker.vue";
import nengetsuhirange from "@/components/basic/NengetsuhiRange.vue";
import reafsdropdown from "@/components/basic/ReafsControl/ReafsDropdown.vue";

import reafsCalendar from "@/components/basic/ReafsControl/ReafsCalendar";

var $thisValue;

export default {
  components: {
    "count-to": countTo,
    reafsinputtext,
    reafsinputnumber,
    reafsmodal,
    nengetsu,
    timepicker,
    nengetsuhirange,
    reafsdropdown,
    reafsCalendar,
  },
  data() {
    // var validatenumber= (rule, value, callback) => {
    //     if (value > 99999) {
    //       callback(new Error('6桁以内に入力してください'));
    //     }
    // };
    return {
      activeIndex: "1",
      selectIndex: "1",

      // --- サンプルフォーム
      showDialog: false,
      form: {
        name: "",
        nameSearch: "",
        region: "",
        date1: "",
        date2: "",
        delivery: false,
        type: [],
        resource: "",
        desc: "",
      },
      sampleForm: {
        nyuryokuerea1: "",
        nyuryokuerea2: "",
        nyuryokuerea3: "",
        nyuryokuerea4: "",
        nyuryokuerea5: "",
        nyuryokuerea6: "",
      },
      rules: {
        nyuryokuerea1: [
          { required: true, message: "入力してください", trigger: "blur" },
        ],
        nyuryokuerea3: [
          { required: true, message: "入力してください", trigger: "blur" },
        ],
      },

      // --- 要望依頼1-3
      iraiForm1: {
        nyuryokuerea1: "",
        nyuryokuerea2: "",
        nyuryokuerea3: "",
        nyuryokuerea4: "",
        nyuryokuerea5: "",
        nyuryokuerea6: {
          startDate: "",
          startTime: "",
          endDate: "",
          endTime: "",
        },
      },
      // --- 要望依頼4
      iraiForm4: {},

      // --カレンダー
      searchForm: {
        calendarSelectVal: "",
      },
      calendarItems: [],
      input: "未設定",

      date: new Date(),
      chooseDates: [],
      isActive: false,
    }
  },
  created() {
    // --カレンダー    $thisValue = this;
    // 対象 選択項目取得
    // this.http.ajax({
    //   url: "api/calendarKbn",
    //   json: true,
    //   success: function (result) {
    //     $thisValue.searchForm.calendarItems = result.data;
    //   },
    //   type: "get",
    //   async: false,
    // });
  },
  mounted() {},
  methods: {
    reafsBack_ReafsCalendar(obj) {
      this.MsgInfo({
        title: this.$store.getters.getPageTitle(),
        message: "222",
      });
    },
    onSubmit() {
      // this.MsgInfo({
      //   title: "123",
      //   message: "456",
      //   dangerouslyUseHTMLString: true,
      // });

      // this.MsgErr({
      //   title: "123",
      //   message: "456",
      //   dangerouslyUseHTMLString: true,
      // });

      // this.MsgConf({
      //   title: this.$store.getters.getPageTitle(),
      //   message: "456",
      //   dangerouslyUseHTMLString: true,
      // },()=>{
      //   console.log("はい")
      // },()=>{
      //   console.log("いいえ")
      // });

      this.MsgConf({
        title: this.$store.getters.getPageTitle(),
        message: "456",
        dangerouslyUseHTMLString: true,
      });
    },
    // お知らせメッセージのサンプル
    onSubmitInfo() {
      this.getMsg("M000012").then((response) => {
        this.MsgInfo({
          title: this.$store.getters.getPageTitle(),
          message: response.data,
        });
      });
    },
    // エラーメッセージのサンプル
    onSubmitErr() {
      this.getMsg("E040012").then((response) => {
        this.MsgErr({
          title: this.$store.getters.getPageTitle(),
          message: response.data,
        });
      });
    },
    // Conformのサンプル
    onSubmitConfirm() {
      this.getMsg("K000006").then((response) => {
        this.MsgConf(
          {
            title: this.$store.getters.getPageTitle(),
            message: response.data,
          },
          () => {
            // OKが選択された場合
            this.getMsg("M040022").then((response) => {
              this.MsgInfo({
                title: this.$store.getters.getPageTitle(),
                message: response.data,
              });
            });
          },
          () => {
            // Cancelが選択された場合
            // this.$alert('Cancelが選択された場合')
          }
        );
      });
    },
    handleSelect(key, keyPath) {
      this.selectIndex = key;
    },
    // サブ画面から戻すデータ
    reafsmodalBack(obj) {
      this.sampleForm.nyuryokuerea5 = obj;
    },

    // --カレンダー
    isChoose: function (slotDate, slotData) {
      return this.chooseDates.includes(slotData.day);
    },

    ChooseChanged(slotDate, slotData) {
      if (slotData.type != "current-month") {
        return;
      }
      if (!this.chooseDates.includes(slotData.day)) {
        this.chooseDates.push(slotData.day);
        console.log(this.chooseDates);
      } else {
        for (let i = 0; this.chooseDates.length; i++) {
          console.log(this.chooseDates[i]);
          if (this.chooseDates[i] == slotData.day) {
            this.chooseDates.splice(i, 1);
            break;
          }
        }
        console.log(this.chooseDates);
      }
    },

    colorChange() {
      this.isActive = true;
    },

    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },
  },
};
</script>
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
// .bg-purple-light {
//   background: #f3f3f3;
// }
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
// .el-form-item {
//   background-color: rgb(236, 236, 236);
// }

.home-contianer {
  padding: 10px;
}
.el-input-len-5 {
  width: 60px;
}
.el-input-serach-len-5 {
  width: 130px;
}
.el-input-len-10 {
  width: 120px;
}
.el-input-date-len-10 {
  width: 140px;
}
.date-picker {
  width: 250px;
}
</style>

<style lang="less" scoped>
// カレンダー
.button-row {
  display: flex;
  justify-content: space-between;
}
</style>
<style scoped>
.el-form >>> .orange .el-form-item__label {
  background: orange;
}

.el-calendar >>> .el-calendar__title {
  display: none;
}

.el-calendar >>> .el-button-group {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.el-calendar >>> .el-calendar__button-group {
  width: 100%;
  text-align: center;
}

.el-calendar >>> .el-button-group::after,
.el-calendar >>> .el-button-group::before {
  display: table;
  content: none;
}

.el-calendar
  >>> .el-button-group
  > .el-button:not(:first-child):not(:last-child) {
  display: none;
}
.date-title {
  text-align: center;
  transform: translateY(55px);
}
.el-calendar >>> .el-calendar-table {
  border-collapse: collapse;
}
.el-calendar >>> .el-calendar-table thead th {
  background: #bdd7ee;
  border: 0.1px solid;
}

.el-calendar >>> .is-choose,
.el-calendar >>> .el-calendar-table .current:last-child {
  background-color: lightpink;
}
.el-calendar >>> .el-calendar-table .current :hover .is-choose {
  background-color: pink;
}
.el-calendar >>> .el-calendar-table .current:last-child :hover {
  background-color: pink;
}

.el-calendar >>> .el-calendar-table .prev:last-child,
.el-calendar >>> .el-calendar-table .next:last-child {
  background-color: pink;
}
.el-calendar >>> .el-calendar-table .next:last-child :hover {
  background-color: rgb(247, 213, 219);
}
.el-calendar >>> .el-calendar-table .prev:last-child :hover {
  background-color: rgb(247, 213, 219);
}


.el-calendar >>> .is-choose,
.el-calendar >>> .el-calendar-table .current:first-child {
  background-color: lightpink;
}
.el-calendar >>> .el-calendar-table .current:first-child :hover {
  background-color: pink;
}

.el-calendar >>> .el-calendar-table .prev:first-child,
.el-calendar >>> .el-calendar-table .next:first-child {
  background-color: pink;
}
.el-calendar >>> .el-calendar-table .next:first-child :hover {
  background-color: rgb(247, 213, 219);
}
.el-calendar >>> .el-calendar-table .prev:first-child :hover {
  background-color: rgb(247, 213, 219);
}


.el-calendar >>> .el-calendar-table .el-calendar-day {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding: 0px;
  height: 85px;
}
</style>
<style>
.pinkcolor .el-range-input {
  background-color: pink !important;
}
</style>
