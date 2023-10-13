<template>
  <div class="home-container CalendarSetteiSite" >
    <el-form :rules="rules" label-width="120px" class="sample-form-class"
      :hide-required-asterisk="true">
      <el-container class="home-el-container">
        <el-main class="home-el-main">
          <el-row class="border-bot" style="margin-bottom: 20px">
            <el-col :span="24">
              <el-form-item label="対象" class="is-required">
                <reafsinputtext required disabled :width="'110px'" v-model="pageData.対象">
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <div>&emsp;</div>
            </el-col>
            <el-col :span="2">
              <span class="color-note break-day">&emsp;</span>
              <span>休日</span>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="14">
              <el-form-item label="年月" prop="年月" class="date-time-search">
                <div style="display: flex">
                  <el-date-picker maxlength="7" type="month" placeholder="" v-model="pageData.年月" style="width: 124px"
                    format="yyyy/MM" value-format="yyyy/MM" ref="ref年月"></el-date-picker>
                  <reafsinputtext v-model="M020Set" :width="'82px'" class="is-readonly" disabled="disabled">
                  </reafsinputtext>
                </div>
              </el-form-item>
              <el-calendar class="CalendarSettei" v-model="calendarDay" :key="CalendarSetteiKey" ref="refCalendar" @input="inputCalendar">
                <template slot="dateCell" slot-scope="{date, data}">
                  <p class="day-cell" :class="getDayCellClass(data)" @click="setDayBreak(data)">
                    {{ Number(data.day.split('-')[2]) }}
                  </p>
                </template>
              </el-calendar>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row style="margin-bottom: 20px">
            <el-col :span="3">
              <el-button @click="onSearch" :loading="loading.btnSearch">検索</el-button>
            </el-col>
            <el-col :span="5">
              <el-button @click="onRegister()" :loading="loading.btnRegister">登録</el-button>
            </el-col>
            <el-col :span="6">
              <el-button @click="onClickBack" style="float: right" >戻る</el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
export default {
  components: {
    reafsinputtext
  },
  data () {
    return {
      CalendarSetteiKey: 0,
      pageData: {
        対象: 'SGW',
        カレンダ区分: '2',
        年月: '',
        M020: []
      },
      calendarDay: '',
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      rules: {
        年月: [
          { validator: (rule, value, callback) => {
            if (!this.pageData.年月) {
              this.getMsg('E010333').then((response) => {
                callback(new Error(response.data))
              })
            }
          },
          trigger: 'change' }
        ]
      },
      loading: {
        btnRegister: false,
        btnSearch: false
      },
      searched年月: '',
      isM020Set: false
    }
  },
  watch: {
    // 'pageData.年月' (n, o) {
    //   if (n) {
    //     this.calendarDay = n
    //     // this.onSearch(false)
    //   }
    // }
  },
  computed: {
    M020Set () {
      if (this.searched年月) {
        return this.isM020Set ? '設定済' : '未設定'
      } else {
        return ''
      }
    },
    年月Formated () {
      if (!this.pageData.年月) {
        return ''
      }
      var d = new Date(this.pageData.年月)
      let month = '' + (d.getMonth() + 1)
      let day = '' + d.getDate()
      let year = d.getFullYear()

      if (month.length < 2) { month = '0' + month }
      if (day.length < 2) { day = '0' + day }

      return [year, month].join('/')
    }
  },
  created () {
    let userLogIn = this.$store.getters.getUserInfo()
    this.userInfo.ユーザーＩＤ = userLogIn.userId
    this.userInfo.会社コード = userLogIn.会社コード
  },
  mounted () {
    this.http
      .get('/api/Reafs_W/Master/WM00100/GetStartInfo',
        { 会社コード: this.userInfo.会社コード }, 'アクセスしています....')
      .then((response) => {
        if (response.data) {
          this.pageData.年月 = response.data.経理年月
          // ハノイ側修正2022/08/17　STEP2_W　課題管理表№42：「2022/08/17依頼分」Ver1.01 初期表示時に検索を行うよう変更
          if (this.pageData.年月) {
            this.onSearch(false)
          }
        }
      })
  },
  methods: {
    inputCalendar (e) {
      if (!this.$refs.ref年月.displayValue) {
        this.getMsg('E010333 ').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref年月.focus()
          }})
        })
        this.calendarDay = this.$refs.refCalendar.curMonthDatePrefix
        return
      }
      var d = new Date(e)
      let month = '' + (d.getMonth() + 1)
      let day = '' + d.getDate()
      let year = d.getFullYear()

      if (month.length < 2) { month = '0' + month }
      if (day.length < 2) { day = '0' + day }
      if (this.$refs.ref年月.displayValue === [year, month].join('/')) {
        return
      }
      this.pageData.年月 = [year, month].join('/')
      this.onSearch(false)
    },
    forceRerender () {
      this.CalendarSetteiKey += 1
    },
    setDayBreak (data) {
      if (!this.pageData.M020) {
        return
      }
      const m020 = this.pageData.M020.find(x => x.日付 === data.day.split('-').join('/'))
      if (m020) {
        m020.休日区分 = m020.休日区分 === '1' ? 0 : '1'
      } else {
        this.pageData.M020.push({
          日付: data.day.split('-').join('/'),
          休日区分: '1'
        })
      }
      this.forceRerender()
    },
    getDayCellClass (data) {
      const m020 = this.pageData.M020.find(x => x.日付 === data.day.split('-').join('/'))
      if (m020 && m020.休日区分 === '1' && data.day.split('-').slice(0, -1).join('/') === this.searched年月) {
        return 'break-day'
      } else {
        return ''
      }
    },

    async onSearch (isCheck = true) {
      if (isCheck && !this.$refs.ref年月.displayValue) {
        this.getMsg('E010333 ').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref年月.focus()
          }})
        })

        return false
      }
      this.isM020Set = false
      this.searched年月 = ''
      this.loading.btnSearch = true
      let result = {}
      const 年月 = this.年月Formated || this.$refs.refCalendar.formatedToday.split('-').slice(0, -1).join('/')
      await this.http
        .get('/api/Reafs_W/Master/WM00100/GetPageData', {カレンダ区分: this.pageData.カレンダ区分, 年月}, 'アクセスしています....')
        .then(res => {
          result = res
        }).finally(() => {
          this.hideLoading()
        })
      if (result.status && result.data) {
        this.pageData.M020 = result.data
        this.isM020Set = this.pageData.M020 && this.pageData.M020.length > 0
        this.searched年月 = this.年月Formated
        this.calendarDay = this.年月Formated
        this.forceRerender()
      }
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof option.callback === 'function') {
            option.callback()
          }
        }
      })
    },
    regisValidate () {
      if (!this.$refs.ref年月.displayValue) {
        this.getMsg('E010333 ').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref年月.focus()
          }})
        })

        return false
      }

      if (this.年月Formated !== this.searched年月) {
        this.getMsg('E000012').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref年月.focus()
          }})
        })

        return false
      }
      return true
    },
    async onRegister (mode) {
      if (!this.pageData.M020) {
        return
      }
      if (!this.regisValidate()) {
        return false
      }

      this.loading.btnRegister = true
      const 年月 = this.年月Formated

      let msg = (await this.getMsg('K000004')).data
      if (!(await this.confirm(msg))) {
        this.loading.btnRegister = false
        return false
      }
      const data = { M020: this.pageData.M020, カレンダ区分: this.pageData.カレンダ区分, 年月 }
      let res = {}
      await this.http
        .post(
          '/api/Reafs_W/Master/WM00100/Register',
          data,
          'アクセスしています....'
        )
        .then(response => {
          res = response
        })
        .catch(ex => {
          // this.showError(ex)
        })
        .finally(() => {
          this.loading.btnRegister = false
        })
      if (res.status) {
        this.showSuccess(res.message)
        this.onSearch()
        this.loading.btnRegister = false
        return true
      } else {
        this.loading.btnRegister = false
        this.showError(res.message)
        return false
      }
    },
    async onBack () {
    },
    showSuccess (message, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgInfo({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading()
        }
      })
    },
    hideLoading () {
      // eslint-disable-next-line no-return-assign
      Object.keys(this.loading).forEach(v => (this.loading[v] = false))
    },
    confirm (message, title = this.$store.getters.getPageTitle()) {
      return new Promise((resolve, reject) => {
        this.MsgConf({
          title,
          message,
          callback: function (a, b) {
            if (a === 'confirm') {
              resolve(true)
            } else {
              resolve(false)
            }
          }
        })
      })
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    // 戻る
    onClickBack() {
      this.$router.back()
    },
  }
}
</script>
<style lang="less" scoped>
.border-bot {
  border-bottom: 1px solid #dcdfe6;
}

.break-day {
  background-color: #ffc0cb;
}

.home-container {
  padding: 10px;
  min-width: 1380px;
  height: 100%;
}
.sample-form-class {
  height: 100%;
}
.home-el-container {
  height: 100%;
}
.color-note {
  display: inline-block;
  width: 50px;
  border: 1px solid #dcdfe6;
}

.day-cell {
  margin: 0;
  width: 100%;
  height: 100%;
  text-align: center;
  line-height: 66px;
  display: table-cell;
}

.date-time-search {
  width: 324px;
  position: absolute;
  top: 12px;
  left: 204px;
}
</style>
<style>
.el-scrollbar__view {
    height: 100%;
}
.CalendarSettei .el-calendar-table .el-calendar-day {
  height: 70px !important;
  display: flex;
  padding: 0;
}

.CalendarSettei .el-calendar__title {
  display: none;
}

.CalendarSettei .el-calendar__body {
  padding: 8px;
}

.CalendarSettei .el-calendar__button-group,
.CalendarSettei .el-button-group {
  width: 100% !important;
}

.CalendarSettei .el-button-group button:nth-child(1) {
  text-indent: -9999px;
  line-height: 0;
  padding: 5px !important;
}

.CalendarSettei .el-button-group button:nth-child(1):after {
  content: "<<前月";
  text-indent: 0;
  display: block;
  line-height: initial;
  font-size: 14px;
  font-family: Meiryo;
}

.CalendarSettei .el-button-group button:nth-child(3) {
  text-indent: -9999px;
  line-height: 0;
  float: right;
    padding: 5px !important;
}

.CalendarSettei .el-button-group button:nth-child(3):after {
  content: "翌月>>";
  text-indent: 0;
  display: block;
  line-height: initial;
  font-size: 14px;
  font-family: Meiryo;
}

.CalendarSettei .el-button-group button:nth-child(2) {
  display: none;
}

.CalendarSetteiSite .el-scrollbar__wrap {
  background-color: #fff;
}

.CalendarSetteiSite .el-calendar__header {
  border-bottom: none !important;
}
.CalendarSetteiSite .el-calendar__body thead{
  background-color: #9bc2e6
}
.CalendarSetteiSite .el-calendar__body thead th{
  background-color: #9bc2e6
}
.CalendarSetteiSite .el-calendar-table tr:first-child td, .el-calendar-table {
    border-top: 1px solid #000;
}
.CalendarSetteiSite .el-calendar-table tr td:first-child, .el-calendar-table thead th:first-child {
    border-left: 1px solid #000;
}
.CalendarSetteiSite .el-calendar-table thead th {
  border-right: 1px solid #000;
}
.CalendarSetteiSite .el-calendar-table td {
    border-bottom: 1px solid #000;
    border-right: 1px solid #000;
}
.CalendarSetteiSite .el-calendar-table__row .prev .el-calendar-day, .el-calendar-table__row .next .el-calendar-day{
  display: none;
}

.CalendarSetteiSite .el-calendar-table__row .prev, .el-calendar-table__row .next{
  height: 72px;
  pointer-events:none
}
</style>
