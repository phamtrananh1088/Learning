<template>
  <!-- simple -->
  <!-- <reafsCalendar
          @backData="reafsBack_ReafsCalendar"   選択したのデータを設定するのcallback
          :showFullWeekName="true"              true:月曜日     false:月
          :calendarCellAlign="'right'"          cellの中に日の表示Align left center right
          :calendarPadZero="false"              cellの中に日の表示枚数　2022/01/02  →　true:02   false:2
          :isLinkButton="true"  　　　　         linkButton使用フラグ  ★isLinkButtonはtrue時、 prevMonthStrとnextMonthStrが有効 
          :prevMonthStr="'AAA'" 　　　　         LINK表示文字
          :nextMonthStr="'BBB'" 　　　　         LINK表示文字
          :input="'設定する'"   　　　　          month設定文字 
          :calendarWidth="'600px'"              default 100%
        > </reafsCalendar-->
  <div ref="calendarMain">
    <div class="date-title">
      <el-date-picker
        type="month"
        format="yyyy/MM"
        v-model="calendarDay"
      ></el-date-picker>
      <span>{{ input }}</span>
    </div>
    <el-calendar v-model="calendarDay">
      <template slot="dateCell" slot-scope="{ date, data }">
        <div
          @backData="dataBack"
          @dblclick="calendar_dblclick(date, data)"
          :class="setClass(date, data)"
          style="width: 100%; height: 100%"
        >
          {{
            calendarPadZero
              ? data.day.split("-")[2]
              : parseInt(data.day.split("-")[2])
          }}
        </div>
      </template>
    </el-calendar>
  </div>
</template>  
<script>
export default {
  data() {
    return {
      calendarDay: new Date(),
      // 選択したのDate
      chooseDates: [],
      DefaultPrevMonthStr: "前月",
      DefaultNextMonthStr: "翌月",
    };
  },

  name: "reafsCalendar",
  props: {
    calendarWidth: {
      type: String,
      default: "100%",
    },
    //月 →月曜日のフラグ
    showFullWeekName: {
      type: Boolean,
      default: true,
    },
    weekName: {
      type: String,
      default: "曜日",
    },

    //left center right
    calendarCellAlign: {
      type: String,
      default: "right",
    },
    // 2022/01/02  true:02  false :2
    calendarPadZero: {
      type: Boolean,
      default: false,
    },

    calendarSelectVal: {
      type: String,
    },

    calendarInitDay: {
      type: Date,
      default: () => {
        return new Date();
      },
    },

    input: {
      type: String,
      default: "未設定",
    },
    // linkbutton → button
    isLinkButton: {
      type: Boolean,
      default: true,
    },
    // isLinkButtonはtrue時、 prevMonthStrとnextMonthStrが有効
    // :prevMonthStr="'前月'"
    // :nextMonthStr="'翌月'"
    prevMonthStr: {
      type: String,
      default: "前月",
    },
    nextMonthStr: {
      type: String,
      default: "翌月",
    },
  },
  created() {},
  mounted() {
    this.$refs.calendarMain.style.width = this.calendarWidth;
    this.calendarDay = this.calendarInitDay ? this.calendarInitDay : new Date();
    //月 →月曜日のフラグ
    if (this.showFullWeekName) {
      this.$nextTick(function () {
        let tableTitle = document.getElementsByClassName("el-calendar-table");
        for (let itemTemp of Array.from(tableTitle[0].children[0].childNodes)) {
          itemTemp.innerHTML = itemTemp.innerHTML + this.weekName;
        }
      });
    }
    //button →link
    this.isLinkButton = true;
    if (this.isLinkButton) {
      let buttonStr = "";
      let beforeAfterMonth = 0;

      this.$nextTick(function () {
        let calendarButtons = document.querySelector(
          ".el-calendar__button-group .el-button-group"
        ).childNodes;
        for (let btn of Array.from(calendarButtons)) {
          if (this.isLinkButton) {
            btn.style.display = "none";
            buttonStr = btn.innerHTML
              .replace("<!----><!----><span>", "")
              .replace("</span>", "")
              .replace(/\s/g, "");

            if (
              buttonStr == this.DefaultPrevMonthStr ||
              buttonStr == this.DefaultNextMonthStr
            ) {
              let linkButton = document.createElement("el-link");
              if (buttonStr == this.DefaultPrevMonthStr) {
                buttonStr = this.prevMonthStr;
              } else if (buttonStr == this.DefaultNextMonthStr) {
                buttonStr = this.nextMonthStr;
              }
              linkButton.className = "el-link el-link--primary is-underline";
              linkButton.innerHTML = `
                                  <!---->
                                  <span class="el-link--inner">${buttonStr}</span>
                                  <!---->`;
              linkButton.addEventListener("click", (obj) => {
                if (obj.currentTarget.innerText == this.prevMonthStr) {
                  beforeAfterMonth = -1;
                } else if (obj.currentTarget.innerText == this.nextMonthStr) {
                  beforeAfterMonth = 1;
                }
                this.calendarDay = new Date(
                  this.calendarDay.setMonth(
                    this.calendarDay.getMonth() + beforeAfterMonth
                  )
                );
              });

              btn.parentNode.appendChild(linkButton);
            }
          }
        }
      });
    }
  },
  methods: {
    setClass: function (slotDate, slotData) {
      let returnClass = "";
      returnClass += this.isChoose(slotDate, slotData) ? "is-choose" : "";
      if (this.calendarCellAlign && this.calendarCellAlign == "left") {
        returnClass += " align-left";
      } else if (this.calendarCellAlign && this.calendarCellAlign == "center") {
        returnClass += " align-center";
      } else {
        returnClass += " align-right";
      }

      return returnClass;
    },
    dataBack(obj) {
      this.$emit("backData", obj);
    },
    // --カレンダー
    isChoose: function (slotDate, slotData) {
      return this.chooseDates.includes(slotData.day);
    },

    calendar_dblclick(slotDate, slotData) {
      if (slotData.type != "current-month") {
        return;
      }
      if (!this.chooseDates.includes(slotData.day)) {
        this.chooseDates.push(slotData.day);
      } else {
        for (let i = 0; this.chooseDates.length; i++) {
          if (this.chooseDates[i] == slotData.day) {
            this.chooseDates.splice(i, 1);
            break;
          }
        }
      }
      this.dataBack(this.chooseDates);
    },
  },
};
</script>

<style scoped >
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
/* .el-calendar
  >>> .el-button-group
  > .el-link:not(:first-child):not(:last-child) {
  display: none;
} */
.date-title {
  text-align: center;
  transform: translateY(40px);
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

.el-calendar >>> .el-calendar-table .align-right {
  text-align: right;
}
.el-calendar >>> .el-calendar-table .align-left {
  text-align: left;
}
.el-calendar >>> .el-calendar-table .align-center {
  text-align: center;
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
