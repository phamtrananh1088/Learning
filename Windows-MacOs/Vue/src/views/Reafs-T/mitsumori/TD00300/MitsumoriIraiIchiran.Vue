<template>
  <div class="home-container MitsumoriIraiIchiran">
    <el-form
      :model="mitsumoriIraiIchiran"
      ref="refmitsumoriIraiIchiran"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk = "true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row>
            <el-col :span="7">
              <el-form-item label="区分" prop="区分" class="is-required">
                <el-select
                  required
                  tabindex="0"
                  v-model="mitsumoriIraiIchiran.区分"
                  ref="ref区分"
                >
                  <el-option
                    v-for="item in 区分value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="物件" prop="bukken">
                <div style="display: flex">
                  <!-- <reafsinputtext
                    tabindex="3"
                    v-model="mitsumoriIraiIchiran.物件コード"
                    maxlength="8"
                    :width="'95px'"
                    @dbClick="ondbClickBukken"
                    @blur="reafsmodalBackBukkenCd"
                    >
                  </reafsinputtext>
                  <reafsmodalSearchBukken :dialogVisible.sync="showDialogSearchBukken" @backData="reafsmodalBack_searchBukken"></reafsmodalSearchBukken>
                  <reafsinputtext
                    v-model="mitsumoriIraiIchiran.物件名称"
                    maxlength="50"
                    disabled
                    :width="'100%'"
                    class="is-readonly"
                    :title="mitsumoriIraiIchiran.物件名称">
                  </reafsinputtext> -->
                  <opensearch
                    tabindex="4"
                    maxlength="8"
                    v-model="mitsumoriIraiIchiran.物件コード"
                    :width="'95px'"
                    :appendlabelText="mitsumoriIraiIchiran.物件名称"
                    appendlabelWidth="182px"
                    ref="ref物件コード"
                    :path="'reafsmodalSearchBukken'" @backData="reafsmodalBack_searchBukken"
                    >
                  </opensearch>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="8" style="padding-left: 20px;">
              <el-form-item label="状態" prop="状態" class="is-required">
                <el-select
                  required
                  tabindex="1"
                  v-model="mitsumoriIraiIchiran.状態"
                  ref="ref状態"
                  style="width: 293.52px"
                >
                  <el-option
                    v-for="item in 状態value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="工事予定日" prop="kojiYoteiBi">
                <div style="display: flex">
                  <el-date-picker
                    tabindex="4"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="mitsumoriIraiIchiran.工事予定日_開始"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref工事予定日_開始"
                  ></el-date-picker>
                  &nbsp;～&nbsp;
                  <el-date-picker
                    tabindex="5"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="mitsumoriIraiIchiran.工事予定日_終了"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref工事予定日_終了"
                  ></el-date-picker>
                </div>
              </el-form-item>
              <!-- <el-form-item label="工事予定日" prop="kojiYoteiBi">
                <el-date-picker
                  v-model="mitsumoriIraiIchiran.工事予定日"
                  maxlength="10"
                  type="daterange"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  class="date-picker"
                  range-separator="~"
                  start-placeholder="開始日"
                  end-placeholder="終了日"
                  ref="ref工事予定日"
                  >
                </el-date-picker>
              </el-form-item> -->
              <el-form-item label="依頼日" prop="iraiBi">
                <div style="display: flex">
                  <el-date-picker
                    tabindex="8"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="mitsumoriIraiIchiran.依頼日_開始"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref依頼日_開始"
                  ></el-date-picker>
                  &nbsp;～&nbsp;
                  <el-date-picker
                    tabindex="9"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="mitsumoriIraiIchiran.依頼日_終了"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref依頼日_終了"
                  ></el-date-picker>
                </div>
              </el-form-item>
              <!-- <el-form-item label="依頼日" prop="iraiBi">
                <el-date-picker
                  v-model="mitsumoriIraiIchiran.依頼日"
                  maxlength="10"
                  type="daterange"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  class="date-picker"
                  range-separator="~"
                  start-placeholder="開始日"
                  end-placeholder="終了日"
                  ref="ref依頼日"
                  >
                </el-date-picker>
              </el-form-item> -->
            </el-col>
            <el-col :span="8">
              <el-form-item label="負担区分" prop="負担区分">
                <el-select
                  tabindex="2"
                  v-model="mitsumoriIraiIchiran.負担区分"
                  ref="ref負担区分"
                >
                  <el-option
                    v-for="item in 負担区分value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="表示区分" prop="表示区分">
                <el-radio tabindex="6" v-model="mitsumoriIraiIchiran.表示区分" label="1" style="padding-left: 15px;">依頼・契約№別</el-radio>
                <el-radio tabindex="7" v-model="mitsumoriIraiIchiran.表示区分" label="2">定期契約№別</el-radio>
              </el-form-item>
              <el-form-item label="作成日" prop="sakuseiBi">
                <div style="display: flex">
                  <el-date-picker
                    tabindex="10"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="mitsumoriIraiIchiran.作成日_開始"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref作成日_開始"
                  ></el-date-picker>
                  &nbsp;～&nbsp;
                  <el-date-picker
                    tabindex="11"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="mitsumoriIraiIchiran.作成日_終了"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref作成日_終了"
                  ></el-date-picker>
                </div>
              </el-form-item>
              <!-- <el-form-item label="作成日" prop="sakuseiBi">
                <el-date-picker
                  v-model="mitsumoriIraiIchiran.作成日"
                  maxlength="10"
                  type="daterange"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  class="date-picker"
                  range-separator="~"
                  start-placeholder="開始日"
                  end-placeholder="終了日"
                  ref="ref作成日"
                  >
                </el-date-picker>
              </el-form-item> -->
            </el-col>
            <el-col :span="1">
              <br />
              <br />
              <p />
              <el-button tabindex="12" type="primary" size="mini"
                @click="onSearch" :loading="loading">検索</el-button>
            </el-col>
          </el-row>
        </el-header>
        <el-main class="home-el-main">
          <div style="height: 18px;float: right;padding-right: 190px;">{{ rowCount }}</div>
          <reafstable
            ref="table"
            :tableStyle="'width:100%;'"
            :tableData="tableData"
            :scrollWidth="1735"
            :maxHeight="582"
            :height="582"
            :virtualScroll="true"
            :columns="columns"
            :cellClickEvent="onMeisaiKomoku_RowClickEvent"
            :cellStyleOption="cellStyleOption"
            :rowStyleOption="rowStyleOption"
            :rowKeyFieldName="rowKeyFieldName"
            :editOption="editOption"
          ></reafstable>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="22">
              <p />
            </el-col>
            <el-col :span="2">
              <el-button @click="onClickBack" :loading="loadingBack" style="float: right">戻る </el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafstable from '@/components/table/Grid.vue'
import moment from 'moment'
// import reafsmodalSearchBukken from '@/components/modal/Modal_SearchBukken.vue'
import opensearch from '@/components/basic/openSearch.vue'

var $select区分value
var $select状態value
var $select負担区分value

export default {
  components: {
    reafsinputtext,
    reafstable,
    moment,
    opensearch
    // reafsmodalSearchBukken
  },
  data () {
    return {
      loading: false,
      loadingBack: false,
      userInfo: {
        業者コード: '',
        親ＩＤ: '',
        ユーザーＩＤ: '',
        ユーザー種別: '',
        会社コード: ''
      },
      queryParams: {
        StartupMode: ''
      },
      mitsumoriIraiIchiran: {
        区分: '',
        状態: '',
        負担区分: '',
        物件コード: '',
        物件名称: '',
        工事予定日_開始: '',
        工事予定日_終了: '',
        // 工事予定日: [],
        表示区分: '1',
        依頼日_開始: '',
        依頼日_終了: '',
        // 依頼日: [],
        作成日_開始: '',
        作成日_終了: ''
        // 作成日: []
      },
      区分value: [],
      状態value: [],
      負担区分value: [],
      showDialogSearchBukken: false,
      rowCount: '',
      rowKeyFieldName: 'rowKey',
      columns: [
        { field: '',
          key: '1',
          title: '№',
          width: 40,
          fixed: 'left',
          renderBodyCell: ({ row, column, rowIndex }, h) => {
            return ++rowIndex
          }
        },
        { field: '依頼No', key: '2', title: '依頼№', width: 90 },
        { field: '契約No', key: '3', title: '契約№', width: 105 },
        { field: '契約書管理No', key: '4', title: '契約書管理№', width: 90 },
        { field: '物件名',
          key: '5',
          title: '物件名',
          align: 'left',
          width: 120,
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '依頼日', key: '6', title: '依頼日', width: 61 },
        { field: '依頼者',
          key: '7',
          title: '依頼者',
          align: 'left',
          width: 136,
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '依頼_契約件名',
          key: '8',
          title: '依頼・契約件名',
          align: 'left',
          width: 136,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['依頼_契約件名']
            /*if (row.依頼_契約件名 !== '' && row.修繕定期区分 === 1) {
              let url = `/Reafs_T/SyuzenMitsumoriNyuryoku?工事依頼No=${row.工事依頼NO}`
              url += `&工事依頼No枝番=${row.工事依頼NO枝番}&業者コード=${row.業者コード}&業者コード枝番=${row.業者コード枝番}`
              return (
                <div title={text} style="display: block">
                  <a class="ellipsis-text" href={url} >
                    {text}
                  </a></div>
              )
            } else if (row.依頼_契約件名 !== '' && row.修繕定期区分 === 2) {
              let url = `/Reafs_T/TeikiMitsumoriNyuryoku?keiyakuNO=${row.keiyakuNO}`
              url += `&hansu=${row.hansu}&rirekiNO=${row.rirekiNO}`
              return (
                <div title={text} style="display: block">
                  <a class="ellipsis-text" href={url} >
                    {text}
                  </a></div>
              )
            }*/
            return (
                <div title={text} style="display: block">
                  <a class="ellipsis-text" style="color: blue" >
                    {text}
                  </a></div>
              )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '工事区分',
          key: '9',
          title: '工事区分',
          align: 'center',
          width: 60,
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { key: '10',
          title: '工期予定・契約期間',
          children: [
            {
              key: '11',
              title: '開始',
              align: 'center',
              width: 61,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block">
                    <span >
                    </span><span >
                      {row.工期予定_契約期間_開始}
                    </span></div>
                )
              }
            },
            {
              key: '11mid',
              title: '~',
              width: 18
            },
            {
              key: '12',
              title: '終了',
              align: 'center',
              width: 61,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block">
                    <span >
                    </span><span >
                      {row.工期予定_契約期間_終了}
                    </span></div>
                )
              }
            }
          ]
        },
        { field: '作成日', key: '13', title: '作成日', width: 61 },
        { field: '状態', key: '14', title: '状態', width: 85 },
        { field: '金額', key: '15', title: '金額', align: 'right', width: 135 },
        { field: '提出先', key: '16', title: '提出先', width: 85 }
      ],
      tableData: [],
      cellStyleOption: {
        headerCellClass: ({ column, rowIndex }) => {
          if (column.key === '11') {
            return 'table-header-cell-custom-11'
          }
          if (column.key === '11mid') {
            return 'table-header-cell-custom-11mid'
          }
          if (column.key === '13') {
            return 'table-header-cell-custom-12'
          }
        },
        bodyCellClass: ({ row, column, rowIndex }) => {
          if (rowIndex === 6) {
            return 'highlight-row'
          }
        }
      },
      // https://happy-coding-clans.github.io/vue-easytable/#/en/doc/table/cell-edit
      editOption: {
        // cell value change
        cellValueChange: ({ row, column }) => {
        }
      },
      rowStyleOption: {
        clickHighlight: true
      }
    }
  },
  computed: {
    getDataGrid () {
      return {
        s業者コード: this.userInfo.業者コード,
        s親ID: this.userInfo.親ＩＤ,
        sユーザーID: this.userInfo.ユーザーＩＤ,
        sユーザー種別: this.userInfo.ユーザー種別,
        s区分: this.mitsumoriIraiIchiran.区分,
        s状態: this.mitsumoriIraiIchiran.状態,
        s負担区分: this.mitsumoriIraiIchiran.負担区分,
        s物件コード: this.mitsumoriIraiIchiran.物件コード,
        s工事予定日_開始: this.mitsumoriIraiIchiran.工事予定日_開始,
        s工事予定日_終了: this.mitsumoriIraiIchiran.工事予定日_終了,
        s依頼日_開始: this.mitsumoriIraiIchiran.依頼日_開始,
        s依頼日_終了: this.mitsumoriIraiIchiran.依頼日_終了,
        s作成日_開始: this.mitsumoriIraiIchiran.作成日_開始,
        s作成日_終了: this.mitsumoriIraiIchiran.作成日_終了,
        s表示区分: this.mitsumoriIraiIchiran.表示区分
      }
    }
  },

  created () {
    this.queryParams = {
      StartupMode: this.$route.params.StartupMode
    }
    $select区分value = this
    $select状態value = this
    $select負担区分value = this
    this.init()
  },
  mounted () {

  },
  inject: ['setPageTitle'],

  watch: {
    $route(to, from) {
      if (to.name == "TD00300") {
        this.queryParams = {
          StartupMode: this.$route.params.StartupMode
        }
        $select区分value = this
        $select状態value = this
        $select負担区分value = this

        //20221028　rep 作業完了報告画面から戻って来た時、条件を維持したまま再検索を行うように修正。
        //this.init()
        if (this.queryParams.StartupMode != null){
          this.init()
        }else{
          this.onSearch()
        }
        //20221028　rep
      }
    },
  },
  methods: {
    init () {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.業者コード = userLogIn.業者コード
      this.userInfo.親ＩＤ = userLogIn.親ＩＤ
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.ユーザー種別 = userLogIn.ユーザー種別
      this.getPageTitleByMenu()

      // Get Dropdown区分
      $select区分value.区分value = [
        { 名称コード: '', 名称コード_名称: '全て' },
        { 名称コード: '1', 名称コード_名称: '緊急' },
        { 名称コード: '2', 名称コード_名称: '修繕' },
        { 名称コード: '3', 名称コード_名称: '定期委託' }
      ]
        this.mitsumoriIraiIchiran.区分 = ''
      // Get Dropdown状態
      $select状態value.状態value = [
        { 名称コード: '', 名称コード_名称: '全て' },
        { 名称コード: '1', 名称コード_名称: '緊急未受付' },
        { 名称コード: '2', 名称コード_名称: '見積未提出（差戻、一時保存を含む）' },
        { 名称コード: '3', 名称コード_名称: '一時保存' },
        { 名称コード: '4', 名称コード_名称: '見積提出' },
        { 名称コード: '5', 名称コード_名称: '実施・契約' },
        { 名称コード: '6', 名称コード_名称: '見積不採用' }
      ]
      
      this.http
        .get('/api/Reafs_T/Mitsumori/TD00300/GetStartInfo', 'アクセスしています....')
        .then((response) => {
          // Get Dropdown依頼状況
          $select負担区分value.負担区分value = response.data.名称取得方法01
          this.mitsumoriIraiIchiran.負担区分 = ''
        })

      this.$nextTick(() => {
        this.$refs.ref区分.focus()
      })
      this.rowCount = ''

      if (this.queryParams.StartupMode != null){
        if(this.queryParams.StartupMode === '1'){
          this.mitsumoriIraiIchiran.状態 = '1'
        }else if(this.queryParams.StartupMode === '2'){
          this.mitsumoriIraiIchiran.状態 = '2'
        }
        this.onSearch()
      }else{
        this.mitsumoriIraiIchiran.状態 = ''
      }
    },
    getPageTitleByMenu () {
      this.setPageTitle('見積依頼・契約一覧');
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({ title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof (option.callback) === 'function') {
            option.callback()
          }
        }})
    },
    showSuccess (message, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgInfo({ title,
        message,
        callback: function (a, b) {
          self.hideLoading()
        }})
    },
    hideLoading () {
      this.loading = false
      this.loadingBack = false
    },
    // ondbClickBukken () {
    //   this.showDialogSearchBukken = true
    // },
    onMeisaiKomoku_RowClickEvent (rowData, column) {
      if (column.field !== '依頼_契約件名') {

      } else {
        if (column.field === '依頼_契約件名') {
          if (rowData.依頼_契約件名 !== '' && rowData.修繕定期区分 === 1) {
            this.$router.push({ name: 'TD00301', params: { 業者コード: rowData.業者コード, 業者コード枝番: rowData.業者コード枝番, 工事依頼No: rowData.工事依頼NO, 工事依頼No枝番: rowData.工事依頼NO枝番, 見積書枝番: rowData.見積書枝番 } })
          } else if (rowData.依頼_契約件名 !== '' && rowData.修繕定期区分 === 2) {
            this.$router.push({ name: 'TD00302', params: { keiyakuNO: rowData.keiyakuNO, hansu: rowData.hansu, rirekiNO: rowData.rirekiNO } })
          } 
        }
      }
    },
    reafsmodalBack_searchBukken (obj) {
      this.mitsumoriIraiIchiran.物件コード = obj.txtCd
      this.mitsumoriIraiIchiran.物件名称 = obj.lblNm
    },
    reafsmodalBackBukkenCd () {
      if (this.mitsumoriIraiIchiran.物件コード == '') {
        this.mitsumoriIraiIchiran.物件名称 = ''
      } else {
        this.http
          .get('/api/Reafs_T/Mitsumori/TD00300/Get物件コードInfo', { 物件コード: this.mitsumoriIraiIchiran.物件コード })
          .then((response) => {
            if (response.data.length > 0) {
              this.mitsumoriIraiIchiran.物件名称 = response.data[0].物件名
            } else {
              this.getMsg('E040010').then((response) => {
                this.showError(response.data)
              })
              this.mitsumoriIraiIchiran.物件名称 = ''
            }
          })
      }
    },
    async onSearch () {
      this.loading = true

      // if(this.mitsumoriIraiIchiran.工事予定日.length > 0 && moment(this.mitsumoriIraiIchiran.工事予定日[0]).isValid() === true){
      if (this.mitsumoriIraiIchiran.工事予定日_開始 === '' && this.mitsumoriIraiIchiran.工事予定日_開始 === undefined &&
        moment(this.mitsumoriIraiIchiran.工事予定日_開始).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref工事予定日_開始.focus()
        return false
      }

      // if(this.mitsumoriIraiIchiran.工事予定日.length > 0 && moment(this.mitsumoriIraiIchiran.工事予定日[1]).isValid() === false){
      if (this.mitsumoriIraiIchiran.工事予定日_終了 === '' && this.mitsumoriIraiIchiran.工事予定日_終了 === undefined &&
        moment(this.mitsumoriIraiIchiran.工事予定日_終了).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref工事予定日_終了.focus()
        return false
      }

      // if(this.mitsumoriIraiIchiran.依頼日.length > 0 && moment(this.mitsumoriIraiIchiran.依頼日[0]).isValid() === false){
      if (this.mitsumoriIraiIchiran.依頼日_開始 === '' && this.mitsumoriIraiIchiran.依頼日_開始 === undefined &&
        moment(this.mitsumoriIraiIchiran.依頼日_開始).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼日_開始.focus()
        return false
      }

      // if(this.mitsumoriIraiIchiran.依頼日.length > 0 && moment(this.mitsumoriIraiIchiran.依頼日[1]).isValid() === false){
      if (this.mitsumoriIraiIchiran.依頼日_終了 === '' && this.mitsumoriIraiIchiran.依頼日_終了 === undefined &&
        moment(this.mitsumoriIraiIchiran.依頼日_終了).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼日_終了.focus()
        return false
      }

      // if(this.mitsumoriIraiIchiran.作成日.length > 0 && moment(this.mitsumoriIraiIchiran.作成日[0]).isValid() === false){
      if (this.mitsumoriIraiIchiran.作成日_開始 === '' && this.mitsumoriIraiIchiran.作成日_開始 === undefined &&
        moment(this.mitsumoriIraiIchiran.作成日_開始).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作成日_開始.focus()
        return false
      }

      // if(this.mitsumoriIraiIchiran.作成日.length > 0 && moment(this.mitsumoriIraiIchiran.作成日[1]).isValid() === false){
      if (this.mitsumoriIraiIchiran.作成日_終了 === '' && this.mitsumoriIraiIchiran.作成日_終了 === undefined &&
        moment(this.mitsumoriIraiIchiran.作成日_終了).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作成日_終了.focus()
        return false
      }

      this.http
        .get('/api/Reafs_T/Mitsumori/TD00300/GetPageData', this.getDataGrid, 'アクセスしています....')
        .then((response) => {
          this.loading = false
          if (response.status) {
            this.tableData = response.data
            if (this.tableData.length > 0) {
              this.rowCount = this.base.formatCommaNumeric(this.tableData.length.toString()) + ' 件'
            } else {
              this.rowCount = ''
            }
          } else {
            return false
          }
        })
    },
    onClickBack () {
      // this.loadingBack = true
      // this.$router.push({ name: "TD00001" });
      // this.loadingBack = false
      //window.location.href = '/Reafs_T/home'
      this.$router.back()
    },

    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  }
}
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
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
.home-el-header {
  padding: 0 0 0 0;
}
.home-el-main {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
}
.home-el-footer{
  padding: 0 0 0 0;
}
.el-button.is-search {
    padding: 0 12px 0 12px;
}
.date-picker{
  width: 250px !important;
}
</style>

<style>
.el-scrollbar__view {
    height: 100%;
}
.ve-table-body-tr, .ve-table-header-tr{
  height: 30px !important;
}
.ve-table-header-th{
  text-align: center !important;
}
.MitsumoriIraiIchiran .ve-table .ve-table-container .ve-table-border-y th.table-header-cell-custom-11 {
  border-right: none !important;
}
.MitsumoriIraiIchiran .ve-table .ve-table-container .ve-table-border-y th.table-header-cell-custom-11mid {
  border-left: none !important;
  border-right: none !important;
}
.MitsumoriIraiIchiran .ve-table .ve-table-container .ve-table-border-y th.table-header-cell-custom-12 {
  border-left: none !important;
}
/* .ve-table{
  overflow-y:scroll;
} */
/* .ve-table .ve-table-container{
  width: calc(100% - 19px);;
} */
</style>
