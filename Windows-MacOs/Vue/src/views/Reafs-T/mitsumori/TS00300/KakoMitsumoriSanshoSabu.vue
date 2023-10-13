<template>
  <div class="KakoMitsumoriSanshoSabu home-container">
    <el-form
      :model="KakoMitsumoriSanshoSabuForm"
      :rules="rules"
      ref="refKakoMitsumoriSanshoSabu"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk="true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row class="rForm">
            <el-col style="width: 32%;">
              <el-form-item label="区分" prop="区分" class="is-required" ref="refParent区分">
                  <el-select
                    required
                    tabindex="1"
                    v-model="KakoMitsumoriSanshoSabuForm.区分"
                    ref="ref区分"
                    style="width: 300px;"
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
              <el-form-item label="物件" prop="物件">
                <div style="display: flex">
                  <!-- <reafsinputtext
                    tabindex="4"
                    maxlength="8"
                    v-model="KakoMitsumoriSanshoSabuForm.物件コード"
                    :width="'95px'"
                    @blur="onBlurBukkenKodo"
                    ref="ref物件コード"
                    >
                  </reafsinputtext>
                  <reafsmodalSearchBukken :dialogVisible.sync="showDialogSearchBukken" @backData="reafsmodalBack_searchBukken"></reafsmodalSearchBukken> -->
                  <opensearch
                    tabindex="4"
                    maxlength="8"
                    v-model="KakoMitsumoriSanshoSabuForm.物件コード"
                    :width="'95px'"
                    :appendlabelText="KakoMitsumoriSanshoSabuForm.物件名"
                    appendlabelWidth="205px"
                    ref="ref物件コード"
                    :path="'reafsmodalSearchBukken'" @backData="reafsmodalBack_searchBukken"
                    >
                  </opensearch>
                  <!-- <reafsinputtext
                    maxlength="20"
                    v-model="KakoMitsumoriSanshoSabuForm.物件名"
                    :width="'205px'"
                    class="is-readonly"
                    disabled="disabled"
                  >
                  </reafsinputtext> -->
                </div>
              </el-form-item>
              <!--<el-form-item style="width: 100%"></el-form-item> -->
              <el-form-item label="件名・明細名称" prop="依頼_契約件名" style="margin-bottom: 0px !important;">
                <reafsinputtext
                  maxlength="20"
                  tabindex="10"
                  v-model="KakoMitsumoriSanshoSabuForm.依頼_契約件名"
                  :width="'300px'"
                  ref="ref依頼_契約件名"
                >
                </reafsinputtext>
              </el-form-item>
              <el-form-item>
                  <span>※部分一致</span>
              </el-form-item>
            </el-col>
            <el-col style="width: 32%; margin-left: 6px;">
              <el-form-item label="状態" prop="状態" class="is-required">
                  <el-select
                    required
                    tabindex="2"
                    v-model="KakoMitsumoriSanshoSabuForm.状態"
                    ref="ref状態"
                    style="width: 294px;"
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
              <el-form-item label="工事予定日" prop="工事予定日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="5"
                  v-model="KakoMitsumoriSanshoSabuForm.工事予定日開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref工事予定日開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="6"
                  v-model="KakoMitsumoriSanshoSabuForm.工事予定日終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref工事予定日終了"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="依頼日" prop="依頼日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="11"
                  v-model="KakoMitsumoriSanshoSabuForm.依頼日開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref依頼日開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="12"
                  v-model="KakoMitsumoriSanshoSabuForm.依頼日終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref依頼日終了"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="作成日" prop="作成日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="13"
                  v-model="KakoMitsumoriSanshoSabuForm.作成日開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作成日開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="14"
                  v-model="KakoMitsumoriSanshoSabuForm.作成日終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作成日終了"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col style="width: 31%;">
              <el-form-item label="負担区分" prop="負担区分" class="is-required">
                  <el-select
                    required
                    tabindex="3"
                    v-model="KakoMitsumoriSanshoSabuForm.負担区分"
                    ref="ref負担区分"
                    style="width: 294px;"
                  >
                    <el-option
                      v-for="item in 負担区分value"
                      :key="item.名称コード"
                      :label="item.名称"
                      :value="item.名称コード"
                    >
                    </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="見積金額" prop="見積金額">
                <div style="display: flex">
                  <reafsinputnumber
                    tabindex="7"
                    :intLength="11"
                    :floatLength="0"
                    v-model="KakoMitsumoriSanshoSabuForm.見積金額下限"
                    width="135px"
                    ref="ref見積金額下限"
                    >
                  </reafsinputnumber>
                  <label style="padding-left: 5px; padding-right: 5px;">〜</label>
                  <reafsinputnumber
                    tabindex="8"
                    :intLength="11"
                    :floatLength="0"
                    v-model="KakoMitsumoriSanshoSabuForm.見積金額上限"
                    width="135px"
                    ref="ref見積金額上限"
                  >
                  </reafsinputnumber>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="1" style="padding-top: 2px;">
              <el-form-item style="margin-bottom: 46px">
              </el-form-item>
              <el-button
                tabindex="9"
                type="primary"
                size="mini"
                @click="onSearch">検索</el-button>
            </el-col>
          </el-row>
        </el-header>
        <el-main class="home-el-main">
          <el-row>
            <el-col :span="22">
              <span style="float: right; margin-right: 29px;">{{件数 | formatnumber}}&nbsp;件</span>
            </el-col>
            <el-col :span="24" style="height: 480px;">
              <reafstable
                ref="dtMeisaiShuzenDeta"
                tabindex="15"
                :tableData="dtMeisaiShuzenDeta"
                :columns="columnsMeisaiShuzenDeta"
                :maxHeight="449"
                :height="460"
                :scrollWidth="1600"
                :tableStyle="'width:100%;'"
                :cellStyleOption="cellStyleOptionMeisaiShuzenDeta"
                :rowStyleOption="rowStyleOption"
                :rowKeyFieldName="rowKeyFieldNameMeisaiShuzenDeta"
                :editOption="editOption"
                :cellClickEvent = "onMeisaiShuzenDeta_RowClickEvent"
                :HiddenColumnKeys = "hiddenColumnsMeisaiShuzenDeta"
              ></reafstable>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer border-top" height="auto">
          <!-- <el-row>
            <el-col :span="22"><div class="grid-content">&nbsp;</div></el-col>
            <el-col :span="1" style="margin-left: 20px;">
              <el-button tabindex="16" @click="onClickBack">戻る </el-button>
            </el-col>
          </el-row> -->
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import opensearch from '@/components/basic/openSearch.vue'
// import reafsmodalSearchBukken from '@/components/modal/Modal_SearchBukken.vue'
import moment from 'moment'
// import screenPopup from '../../common/TScreenPopup/TScreenPopup.vue'
import reafstable from '@/components/table/Grid.vue'

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    moment,
    opensearch,
    // reafsmodalSearchBukken,
    // screenPopup,
    reafstable
  },
  data () {
    return {
      loading: false,
      // MeisaiShuzenDetaStyle: 'width: 1630px;',
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: '',
        業者コード: '',
        ユーザー種別: '',
        親ＩＤ: ''
      },
      queryParams: {
        修繕定期区分: '',
        工事依頼No: '',
        工事依頼No枝番: '',
        業者コード: '',
        業者コード枝番: ''
      },
      responseParams: {
        見積No: '',
        過去見積工事依頼NO枝番: ''
      },
      KakoMitsumoriSanshoSabuForm: {
        区分: '',
        状態: '',
        負担区分: '',
        物件コード: '',
        物件名: '',
        件名_明細名称: '',
        工事予定日開始: '',
        工事予定日終了: '',
        依頼日開始: '',
        依頼日終了: '',
        作成日開始: '',
        作成日終了: '',
        見積金額下限: '',
        見積金額上限: '',
        依頼_契約件名: ''
      },
      区分value: [
        {名称コード: '', 名称コード_名称: '全て'},
        {名称コード: '1', 名称コード_名称: '緊急'},
        {名称コード: '2', 名称コード_名称: '修繕'},
        {名称コード: '3', 名称コード_名称: '定期委託'}
      ],
      状態value: [
        {名称コード: '', 名称コード_名称: '全て'},
        {名称コード: '1', 名称コード_名称: '緊急未受付'},
        {名称コード: '2', 名称コード_名称: '見積未提出（差戻、一時保存を含む）'},
        {名称コード: '3', 名称コード_名称: '一時保存'},
        {名称コード: '4', 名称コード_名称: '見積提出'},
        {名称コード: '5', 名称コード_名称: '実施・契約'},
        {名称コード: '6', 名称コード_名称: '見積不採用'}
      ],
      負担区分value: [],
      showDialogSearchBukken: false,
      件数: 0,
      rowKeyFieldNameMeisaiShuzenDeta: 'rowKeyMeisaiShuzenDeta',
      dtMeisaiShuzenDeta: [],
      columnsMeisaiShuzenDeta: [
        { key: 'No',
          title: '№',
          align: 'center',
          vAlign: 'center',
          width: 75,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return ++rowIndex
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '依頼No',
          key: '依頼No',
          title: '依頼№',
          align: 'center',
          vAlign: 'center',
          width: 90,
          renderBodyCell: ({ row }) => {
            const text = row['依頼No']
            return (
              <div title={text} style="display: block" class="align-left">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '契約No',
          key: '契約No',
          title: '契約№',
          align: 'center',
          vAlign: 'center',
          width: 120,
          renderBodyCell: ({ row }) => {
            const text = row['契約No']
            return (
              <div title={text} style="display: block">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '契約管理No',
          key: '契約管理No',
          title: '契約管理№',
          align: 'center',
          vAlign: 'center',
          width: 90,
          renderBodyCell: ({ row }) => {
            const text = row['契約管理No']
            return (
              <div title={text} style="display: block" class="align-left">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '見積No',
          key: '見積No',
          title: '見積№',
          align: 'center',
          vAlign: 'center',
          width: 135,
          renderBodyCell: ({ row }) => {
            const text = row['見積No']
            return (
              <div title={text} style="display: block" class="align-left">
                <a style="color: blue">
                  {text}
                </a>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '物件名',
          key: '物件名',
          title: '物件名',
          align: 'center',
          vAlign: 'center',
          renderBodyCell: ({ row }) => {
            const text = row['物件名']
            return (
              <div title={text} style="display: block" class="align-left">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '状態',
          key: '状態',
          title: '状態',
          align: 'center',
          vAlign: 'center',
          width: 85,
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '依頼日',
          key: '依頼日',
          title: '依頼日',
          align: 'center',
          vAlign: 'center',
          width: 85,
          renderBodyCell: ({ row, column, rowIndex }) => {
            let cellData依頼日 = row[column.field] // row['依頼日']
            let dsp依頼日 = cellData依頼日 !== '' && cellData依頼日 !== null ? cellData依頼日.substr(2) : ''
            return (
              <div title={dsp依頼日} style="display: block">
                <span style="text-align: center">
                  {dsp依頼日}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '依頼者',
          key: '依頼者',
          title: '依頼者',
          align: 'center',
          vAlign: 'center',
          width: 300,
          renderBodyCell: ({ row }) => {
            const text = row['依頼者']
            return (
              <div title={text} style="display: block" class="align-left">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '依頼_契約件名',
          key: '依頼_契約件名',
          title: '依頼・契約件名',
          align: 'center',
          vAlign: 'center',
          width: 300,
          renderBodyCell: ({ row }) => {
            const text = row['依頼_契約件名']
            return (
              <div title={text} style="display: block" class="align-left">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '見積金額',
          key: '見積金額',
          title: '見積金額',
          align: 'center',
          vAlign: 'center',
          width: 140,
          renderBodyCell: ({ row }) => {
            const text = row['見積金額']
            return (
              <div title={text} style="display: block; text-align: right">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '修繕定期区分',
          key: '修繕定期区分'
        },
        { field: '工事依頼NO',
          key: '工事依頼NO'
        },
        { field: '工事依頼NO枝番',
          key: '工事依頼NO枝番'
        }
      ],
      hiddenColumnsMeisaiShuzenDeta: ['修繕定期区分', '工事依頼NO', '工事依頼NO枝番'],
      cellStyleOptionMeisaiShuzenDeta: {
        bodyCellClass: ({ row, column, rowIndex }) => {
        }
      },
      editOption: {
        // cell value change
        cellValueChange: ({ row, column }) => {
        }
      },
      rowStyleOption: {
        hoverHighlight: true,
        clickHighlight: false
      },
      rules: {}
    }
  },
  inject: ['hideLeftMenu'],

  filters: {
    formatnumber: function (iFormatNumber) {
      return iFormatNumber.toFixed().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  created () {
    this.init(true)
  },
  mounted () {
    this.queryParams = {
      修繕定期区分: this.$route.query.修繕定期区分
      // 工事依頼No: this.$route.query.工事依頼No,
      // 工事依頼No枝番: this.$route.query.工事依頼No枝番,
      // 業者コード: this.$route.query.業者コード,
      // 業者コード枝番: this.$route.query.業者コード枝番
    }
    this.hideLeftMenu(false)
    this.$refs.ref区分.focus()
  },
  methods: {
    init (firstLoad = true) {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.会社コード = userLogIn.会社コード
      this.userInfo.業者コード = userLogIn.業者コード
      this.userInfo.ユーザー種別 = userLogIn.ユーザー種別
      this.userInfo.親ＩＤ = userLogIn.親ＩＤ
      this.http
        .get('/api/Reafs_T/KakoMitsumoriSanshoSabu/GetDropdown負担区分', { 区分コード: '011' }, 'アクセスしています...')
        .then((response) => {
          if (response.data !== null) {
            this.負担区分value = response.data
          }
        })
      this.KakoMitsumoriSanshoSabuForm.区分 = ''
      this.KakoMitsumoriSanshoSabuForm.状態 = ''
      this.KakoMitsumoriSanshoSabuForm.負担区分 = ''
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
    confirm (message, title = this.$store.getters.getPageTitle()) {
      return new Promise((resolve, reject) => {
        this.MsgConf({ title,
          message,
          callback: function (a, b) {
            if (a === 'confirm') {
              resolve(true)
            } else {
              resolve(false)
            }
          }})
      })
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    hideLoading () {
      this.loading = false
    },
    // ondbClickBukken () {
    //   this.showDialogSearchBukken = true
    // },
    reafsmodalBack_searchBukken (obj) {
      this.KakoMitsumoriSanshoSabuForm.物件コード = obj.txtCd
      this.KakoMitsumoriSanshoSabuForm.物件名 = obj.lblNm
    },
    onClickBack () {
      // this.$router.push({ name: "TD00001" });
      window.location.href = this.http.resolve('/Reafs_T/MainMenu')
    },

    async onSearch () {
      this.loading = true
      if (this.KakoMitsumoriSanshoSabuForm.工事予定日開始 === '' && this.KakoMitsumoriSanshoSabuForm.工事予定日開始 === undefined &&
        moment(this.KakoMitsumoriSanshoSabuForm.工事予定日開始).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref工事予定日開始.focus()
        return false
      }
      if (this.KakoMitsumoriSanshoSabuForm.工事予定日終了 === '' && this.KakoMitsumoriSanshoSabuForm.工事予定日終了 === undefined &&
        moment(this.KakoMitsumoriSanshoSabuForm.工事予定日終了).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref工事予定日終了.focus()
        return false
      }
      if (this.KakoMitsumoriSanshoSabuForm.依頼日開始 === '' && this.KakoMitsumoriSanshoSabuForm.依頼日開始 === undefined &&
        moment(this.KakoMitsumoriSanshoSabuForm.依頼日開始).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼日開始.focus()
        return false
      }
      if (this.KakoMitsumoriSanshoSabuForm.依頼日終了 === '' && this.KakoMitsumoriSanshoSabuForm.依頼日終了 === undefined &&
        moment(this.KakoMitsumoriSanshoSabuForm.依頼日終了).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼日終了.focus()
        return false
      }
      if (this.KakoMitsumoriSanshoSabuForm.作成日開始 === '' && this.KakoMitsumoriSanshoSabuForm.作成日開始 === undefined &&
        moment(this.KakoMitsumoriSanshoSabuForm.作成日開始).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作成日開始.focus()
        return false
      }
      if (this.KakoMitsumoriSanshoSabuForm.作成日終了 === '' && this.KakoMitsumoriSanshoSabuForm.作成日終了 === undefined &&
        moment(this.KakoMitsumoriSanshoSabuForm.作成日終了).isValid() === true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作成日終了.focus()
        return false
      }
      if (this.KakoMitsumoriSanshoSabuForm.見積金額下限 === '' && this.KakoMitsumoriSanshoSabuForm.見積金額下限 === undefined) {
        this.getMsg('E040374').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref見積金額下限.focus()
        return false
      }
      if (this.KakoMitsumoriSanshoSabuForm.見積金額上限 === '' && this.KakoMitsumoriSanshoSabuForm.見積金額上限 === undefined) {
        this.getMsg('E040374').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref見積金額上限.focus()
        return false
      }
      let data = {
        ユーザー種別: this.userInfo.ユーザー種別,
        業者コード: this.userInfo.業者コード,
        ユーザーID: this.userInfo.ユーザーＩＤ,
        親ＩＤ: this.userInfo.親ＩＤ,
        表示区分: '1',
        区分: this.KakoMitsumoriSanshoSabuForm.区分,
        状態: this.KakoMitsumoriSanshoSabuForm.状態,
        負担区分: this.KakoMitsumoriSanshoSabuForm.負担区分,
        物件コード: this.KakoMitsumoriSanshoSabuForm.物件コード,
        工事予定日開始: this.KakoMitsumoriSanshoSabuForm.工事予定日開始,
        工事予定日終了: this.KakoMitsumoriSanshoSabuForm.工事予定日終了,
        依頼日開始: this.KakoMitsumoriSanshoSabuForm.依頼日開始,
        依頼日終了: this.KakoMitsumoriSanshoSabuForm.依頼日終了,
        作成日開始: this.KakoMitsumoriSanshoSabuForm.作成日開始,
        作成日終了: this.KakoMitsumoriSanshoSabuForm.作成日終了,
        見積金額下限: this.KakoMitsumoriSanshoSabuForm.見積金額下限.replace(',', ''),
        見積金額上限: this.KakoMitsumoriSanshoSabuForm.見積金額上限.replace(',', ''),
        依頼_契約件名: this.KakoMitsumoriSanshoSabuForm.依頼_契約件名
      }
      await this.http
        .post('/api/Reafs_T/KakoMitsumoriSanshoSabu/Search', data, 'アクセスしています...')
        .then((response) => {
          this.loading = false
          if (response.status) {
            this.件数 = response.data.length
            this.dtMeisaiShuzenDeta = response.data
          } else {
            this.件数 = 0
            this.dtMeisaiShuzenDeta = []
          }
        })
    },

    onMeisaiShuzenDeta_RowClickEvent (rowData, column) {
      if (column.field !== '見積No') {
      } else {
        if (this.queryParams.修繕定期区分 !== rowData.修繕定期区分) {
          if (this.queryParams.修繕定期区分 === '1') {
            this.getMsg('E040694').then((response) => {
              this.showError(response.data)
            })
            return false
          } else if (this.queryParams.修繕定期区分 === '2') {
            this.getMsg('E040693').then((response) => {
              this.showError(response.data)
            })
            return false
          }
        } else {
          this.responseParams.見積No = rowData.見積No
          this.responseParams.過去見積工事依頼NO枝番 = rowData.工事依頼NO枝番
          this.onBack()
        }
      }
    },

    onBlurBukkenKodo () {
      if (this.KakoMitsumoriSanshoSabuForm.物件コード === '' || this.KakoMitsumoriSanshoSabuForm.物件コード.trim() === '') {
        this.KakoMitsumoriSanshoSabuForm.物件名 = ''
      } else {
        this.http
          .get('/api/Reafs_T/KakoMitsumoriSanshoSabu/Get物件名', { 物件コード: this.KakoMitsumoriSanshoSabuForm.物件コード }, 'アクセスしています...')
          .then((response) => {
            if (response.data !== null) {
              if (response.data !== null) {
                this.KakoMitsumoriSanshoSabuForm.物件名 = response.data.物件名
              } else {
                this.KakoMitsumoriSanshoSabuForm.物件名 = ''
                this.getMsg('E040010').then((response) => {
                  this.showError(response.data.物件名, {callback: () => {
                    this.$refs.ref物件コード.focus()
                  }})
                })
                return false
              }
            } else {
              this.KakoMitsumoriSanshoSabuForm.物件名 = ''
            }
          })
      }
    },

    async onBack () {
      window.parent.postMessage({
        'call': 'closePopup',
        見積No: this.responseParams.見積No,
        過去見積工事依頼NO枝番: this.responseParams.過去見積工事依頼NO枝番
      }, '*')
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
  padding: 10px 0 0 0;
}
.border-top {
    border-top: 1px solid #f3f3f3;
}
.rForm {

}
</style>

<style>
.KakoMitsumoriSanshoSabu .ve-table .ve-table-container table.ve-table-content {
  top: -1px !important;
}
</style>
