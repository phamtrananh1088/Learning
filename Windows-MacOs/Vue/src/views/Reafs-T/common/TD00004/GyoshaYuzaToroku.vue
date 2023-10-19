<template>
  <div class="GyoshaYuzaToroku home-container">
    <el-form
      :model="gyoshaForm"
      :rules="rules"
      ref="refGyoshaForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk="true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header"></el-header>
        <el-main class="home-el-main">
          <el-row>
            <el-col :span="18">
              <span style="float: right; margin-right: 40px;">{{件数}}件</span>
            </el-col>
            <el-col :span="24" style="height: 376px;">
              <div style="height: 356px;" class="dtShutsuryokuKomoku">
              <reafstable
                class="gyoshatable"
                ref="dtShutsuryokuKomoku"
                tabindex="1"
                :tableData="dtShutsuryokuKomoku"
                :columns="columnsShutsuryo"
                :maxHeight="341"
                :height="341"
                :cellStyleOption="cellStyleOptionShutsuryo"
                :rowStyleOption="rowStyleOption"
                :rowKeyFieldName="rowKeyFieldNameShutsuryo"
                :editOption="editOption"
                :rowSelectEvent= "onTableTantoEigyojoRowSelectEvent"
                :cellClickEvent = "onShutsuryo_RowClickEvent"
                :HiddenColumnKeys = "hiddenColumnsShutsuryo"
              ></reafstable>
              </div>
              <el-divider class= "clsdiv"></el-divider>
            </el-col>
            <el-col :span="7">
                <el-form-item label="ユーザーＩＤ" prop="ユーザーＩＤ">
                  <label class="item_input_gyoshaYuzaToroku">
                    {{ gyoshaForm.ユーザーＩＤ }}
                  </label>
                  <label style="color: red; margin-left: 5px;">{{dspユーザー種類}}</label>
                  <label style="color: red;">{{dsp新規更新}}</label>
                </el-form-item>
                <el-form-item label="ユーザー名" prop="ユーザー名">
                    <reafsinputtext
                    tabindex="5"
                    maxlength="40"
                    v-model="gyoshaForm.ユーザー名"
                    :width="'100%'"
                    :required="true"
                    :disabled="画面入力制御disabled.txtユーザー名"
                    ref="refユーザー名"
                    ></reafsinputtext>
                </el-form-item>
                <el-form-item label="メールアドレス" prop="メールアドレス">
                    <reafsinputtext
                    tabindex="7"
                    maxlength="50"
                    v-model="gyoshaForm.メールアドレス"
                    :width="'100%'"
                    :required="true"
                    :disabled="画面入力制御disabled.txtメールアドレス"
                    ref="refメールアドレス"
                    ></reafsinputtext>
                </el-form-item>
            </el-col>
            <el-col :span="7" style="padding-left: 30px;">
                <el-form-item label="削除">
                    <el-checkbox
                      tabindex="2"
                      v-model="gyoshaForm.削除"
                      style="margin-left: 5px;
                      margin-right: 10px;"
                      :disabled="画面入力制御disabled.chk削除区分"
                      ></el-checkbox>
                    <label style="color: red;">削除</label>
                </el-form-item>
                <el-form-item label="部署等（メモ）" prop="部署等">
                  <reafsinputtext
                    maxlength="40"
                    tabindex="6"
                    v-model="gyoshaForm.部署等"
                    :width="'100%'"
                    :disabled="画面入力制御disabled.txt部署等"
                  ></reafsinputtext>
                </el-form-item>
                <el-form-item label="パスワード期限" prop="パスワード期限">
                  <label class="item_input_PasuwadoKigen">
                    {{ gyoshaForm.パスワード期限 }}
                  </label>
                  <label class="Kigengire" v-if="expirate_date">期限切れ</label>
                </el-form-item>
            </el-col>
            <el-col :span="9" style="margin-left:-40px;">
              <el-form-item label="利用期間" prop="利用期間" class="is-required">
                <el-date-picker
                  required
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="3"
                  v-model="gyoshaForm.利用期間_開始日"
                  style="width: 135px; background-color: lightgoldenrodyellow;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref利用期間_開始日"
                  :disabled="画面入力制御disabled.txt利用期間_開始日"
                ></el-date-picker>
                <label style="padding-left: 5px; padding-right: 5px;">〜</label>
                <el-date-picker
                  required
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="4"
                  v-model="gyoshaForm.利用期間_終了日"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref利用期間_終了日"
                  :disabled="画面入力制御disabled.txt利用期間_終了日"
                  
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="" style="height: 31px;">
              </el-form-item>
              <el-form-item label="" label-width="0px">
                <el-button
                  tabindex="8"
                  type="primary"
                  @click="onPasuwadoHakko"
                  class="PasuwadoHakko"
                  :disabled="画面入力制御disabled.btnパスワード再発行">
                  <span>パスワード再発行</span>
                </el-button>
              </el-form-item>
            </el-col>
            <el-col :span="16" style="height: 340px;" class="dtTantoEigyojo">
            <div style="background-color: #9bc2e6; color: white; text-align: center; width: 150px; margin-left: 1px; line-height: 30px; height: 30px;">担当営業所（枝番）</div>
              <reafstable
                class="tantotable"
                ref="dtTantoEigyojo"
                tabindex="9"
                :tableData="dtTantoEigyojo"
                :columns="columnsTantoEigyojo"
                :maxHeight="301"
                :height="301"
                :cellStyleOption="cellStyleOptionTantoEigyojo"
                :rowStyleOption="rowStyleOption"
                :rowKeyFieldName="rowKeyFieldNameTantoEigyojo"
                :editOption="editOption"
                :hideSelect = true
                :HiddenColumnKeys = "hiddenColumnsTantoEigyojo"
                :checkboxCheckEvent = "TantoEigyojoCheckboxCheckEvent"
              ></reafstable>
            </el-col>
            <el-col :span="10"></el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer border-top" height="auto">
          <el-row>
            <el-col :span="14"><div class="grid-content">&nbsp;</div></el-col>
            <el-col :span="2">
              <el-button
              tabindex="10"
              type="primary"
              @click="onShinki"
              :disabled="画面入力制御disabled.btn新規">
                <span>新規</span>
              </el-button>
            </el-col>
            <el-col :span="2">
              <el-button
              tabindex="11"
              type="primary"
              @click="onGengXin"
              :disabled="画面入力制御disabled.btn更新">
                <span>更新</span>
              </el-button>
            </el-col>
            <el-col :span="1">
              <el-button tabindex="12" @click="onClickBack">戻る </el-button>
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

var ShutsuryoRowSelected = {}
var TantoEigyojoListRowIdSelect = []
export default {
  components: { reafsinputtext, reafstable ,moment,},
  data () {
    // var validateユーザー名 = (rule, value, callback) => {
    //   if (value === '') {
    //     this.getMsg('E040663').then((response) => {
    //       callback(new Error(response.data))
    //     })
    //   }
    // }
    // var validateメールアドレス = (rule, value, callback) => {
    //   if (value === '') {
    //     this.getMsg('E040172').then((response) => {
    //       callback(new Error(response.data))
    //     })
    //   }
    // }
    return {
      loading: false,
      expirate_date: false,
      dspユーザー種類: '',
      dsp新規更新: '',
      menuInfo: {},
      checkboxOptionCustom: {
        hideSelectAll: true,
        selectedRowKeys: [],
        selectedRowChange: ({ row, isSelected, selectedRowKeys }) => {
          let key = row.rowKeyTantoEigyojo
          if (!this.TantoEigyojoListRowIdSelect.includes(key)) {
            this.TantoEigyojoListRowIdSelect.push(key)
          } else {
            this.TantoEigyojoListRowIdSelect = this.TantoEigyojoListRowIdSelect.filter(element => element !== key)
          }
          this.checkboxOptionCustom.selectedRowKeys = this.TantoEigyojoListRowIdSelect
        }
      },
      画面入力制御disabled: {
        btn新規: false,
        btn更新: true,
        btnパスワード再発行: true,
        chk削除区分: false,
        txt利用期間_開始日: false,
        txt利用期間_終了日: false,
        txtユーザー名: false,
        txt部署等: false,
        txtメールアドレス: false
      },
      gyoshaForm: {
        ユーザーＩＤ: '',
        業者コード: '',
        ユーザー種別: '',
        会社コード: '',
        ユーザー名: '',
        メールアドレス: '',
        削除: false,
        部署等: '',
        パスワード期限: '',
        利用期間_開始日: '',
        利用期間_終了日: ''
      },
      件数: 0,
      rowKeyFieldNameShutsuryo: 'rowKeyShutsuryo',
      rowKeyFieldNameTantoEigyojo: 'rowKeyTantoEigyojo',
      dtShutsuryokuKomoku: [],
      columnsShutsuryo: [
        { key: '1',
          title: 'No',
          align: 'center',
          vAlign: 'center',
          width: 50,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return ++rowIndex
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '削除区分',
          key: '2',
          title: '削除',
          align: 'center',
          vAlign: 'center',
          width: 50,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const cellData = row[column.field]
            let iconName = cellData === 0 ? '' : cellData === 1 ? '削除' : ''
            return (
              <div style="display: block">
                <span style="text-align: center">
                  {iconName}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: 'ユーザーＩＤ',
          key: '3',
          title: 'ユーザーID',
          align: 'center',
          vAlign: 'center',
          width: 100,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['ユーザーＩＤ']
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
        { field: 'ユーザー種別',
          key: '4',
          title: 'ユーザー種類',
          align: 'center',
          vAlign: 'center',
          width: 100,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const cellData = row[column.field]
            let iconName = cellData === '0' ? 'マスター' : cellData === '1' ? '親ユーザー' : cellData === '2' ? '子ユーザー' : ''
            return (
              <div title={iconName} style="display: block" class="align-left">
                <span>
                  {iconName}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: 'ユーザー名',
          key: '5',
          title: 'ユーザー名',
          align: 'center',
          vAlign: 'center',
          // width: 230,
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          },
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['ユーザー名']
            return (
              <div title={text} style="display: block" class="align-left">
                <a href="#">
                  {text}
                </a>
              </div>
            )
          }
        },
        { field: '部署等',
          key: '6',
          title: '部署等（メモ）',
          align: 'center',
          vAlign: 'center',
          width: 230,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['部署等']
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
        { field: 'メールアドレス',
          key: '7',
          title: 'メールアドレス',
          align: 'center',
          vAlign: 'center',
          width: 190,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['メールアドレス']
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
        { key: '8',
          title: '利用期間',
          rowspan: 2,
          children: [
            {
              key: '9',
              width: 80,
              rowspan: 0,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData = row['利用期間開始']
                let dsp利用期間開始 = cellData.substr(2)
                return (
                  <div style="display: block">
                    <span style="text-align: center">
                      {dsp利用期間開始}
                    </span>
                  </div>
                )
              },
              ellipsis: {
                showTitle: true,
                lineClamp: 1
              }
            },
            {
              key: '10',
              width: 30,
              rowspan: 0,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block">
                    <span >
                      {row.チルダ}
                    </span>
                  </div>
                )
              },
              ellipsis: {
                showTitle: true,
                lineClamp: 1
              }
            },
            {
              key: '11',
              width: 80,
              rowspan: 0,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData = row['利用期間終了']
                let dsp利用期間終了 = cellData.substr(2)
                return (
                  <div style="display: block">
                    <span style="text-align: center">
                      {dsp利用期間終了}
                    </span>
                  </div>
                )
              },
              ellipsis: {
                showTitle: true,
                lineClamp: 1
              }
            }
          ]
        },
        { key: '12',
          title: '担当営業所（枝番）',
          rowspan: 2,
          children: [
            {
              key: '13',
              width: 210,
              rowspan: 0,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const text = row['業者名']
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
            {
              key: '14',
              width: 30,
              rowspan: 0,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block">
                    <span >
                      {row.担当営業所_枝番他}
                    </span>
                  </div>
                )
              },
              ellipsis: {
                showTitle: true,
                lineClamp: 1
              }
            }
          ]
        },
        { field: 'パスワード期限終了',
          key: '15',
          title: 'パスワード期限終了',
          align: 'center',
          vAlign: 'center'
        },
        { field: '全枝参照区分',
          key: '16',
          title: '全枝参照区分',
          align: 'center',
          vAlign: 'center'
        },
        { field: '業者コード',
          key: '17',
          title: '業者コード',
          align: 'center',
          vAlign: 'center'
        }
      ],
      hiddenColumnsShutsuryo: [
        '15',
        '16',
        '17'
      ],
      dtTantoEigyojo: [],
      columnsTantoEigyojo: [
        {
          field: '選択',
          key: '1',
          title: '選択',
          type: 'checkbox',
          align: 'center',
          vAlign: 'center',
          width: 40
        },
        { field: '業者コード枝番',
          key: '2',
          title: '枝番',
          align: 'center',
          vAlign: 'center',
          width: 50,
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '業者名称',
          key: '3',
          title: '業者名称',
          align: 'center',
          vAlign: 'center',
          // width: 350,
          renderBodyCell: ({ row }) => {
            const text = row['業者名称']
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
        { field: '住所',
          key: '4',
          title: '住所',
          align: 'center',
          vAlign: 'center',
          width: 500,
          renderBodyCell: ({ row }) => {
            const text = row['住所']
            return (
              <div title={text} style="display: block" class="align-left">
                <span>
                  {text}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true
          }
        },
        {
          field: 'ソート順',
          key: '5'
        }
      ],
      hiddenColumnsTantoEigyojo: [
        '5'
      ],
      cellStyleOptionTantoEigyojo: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          let iユーザー種別 = this.gyoshaForm.ユーザー種別
          let check許可 = this.dspユーザー種類
          let sClass = ''
          if (column.field === '選択') {
            if (iユーザー種別 === '0') {
              if (check許可 === '親ユーザー') {
                sClass = 'enable-checkbox'
              } else if (check許可 === '子ユーザー') {
                sClass = 'disable-checkbox'
              } else {
                sClass = 'disable-checkbox'
              }
            } else if (iユーザー種別 === '1') {
              if (check許可 === '子ユーザー') {
                sClass = 'disable-checkbox'
              } else {
                sClass = 'disable-checkbox'
              }
            } else {
              sClass = 'disable-checkbox'
            }
          }
          return sClass
        }
      },
      cellStyleOptionShutsuryo: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          const check削除区分 = row['削除区分']
          const check利用期間終了 = row['利用期間終了']
          const checkパスワード期限終了 = row['パスワード期限終了']
          const check許可 = row['ユーザー種別']
          let iユーザー種別 = this.gyoshaForm.ユーザー種別
          let d利用期間終了 = new Date(check利用期間終了)
          let dパスワード期限終了 = new Date(checkパスワード期限終了)
          let sCurrDate = this.base.formatCurrDate()
          let dCurrDate = new Date(sCurrDate)
          let sClass = ''
          if (column.key !== '1') {
            if (check削除区分 === 1) {
              sClass = 'disable-row'
            } else if (d利用期間終了 < dCurrDate) {
              sClass = 'disable-row'
            } else if (dパスワード期限終了 < dCurrDate) {
              sClass = 'warning-row'
            } else { sClass = '' }
            if ((check許可 === '0' && iユーザー種別 === '0') || (check許可 === '1' && iユーザー種別 === '1')) {
              sClass += ' nolink'
            }
          }
          return sClass
        }
      },
      // https://happy-coding-clans.github.io/vue-easytable/#/en/doc/table/cell-edit
      editOption: {
        // cell value change
        cellValueChange: ({ row, column }) => {
        }
      },
      rowStyleOption: {
        hoverHighlight: true,
        clickHighlight: false
      },
      rules: {
        // ユーザー名: [{ validator: validateユーザー名, trigger: 'blur' }],
        // メールアドレス: [
        //   { validator: validateメールアドレス, trigger: 'blur' }
        // ]
      }
    }
  },

  created () {
    this.init(true)
  },

  mounted () {
    if (this.gyoshaForm.ユーザー種別 === '0') {
      this.dspユーザー種類 = '親ユーザー'
    } else if (this.gyoshaForm.ユーザー種別 === '1') {
      this.dspユーザー種類 = '子ユーザー'
    } else {
      this.dspユーザー種類 = ''
    }
  },
  inject: ['setPageTitle'],

  methods: {
    init (firstLoad = true) {
      let userLogIn = this.$store.getters.getUserInfo()
      // this.gyoshaForm.ユーザーＩＤ = userLogIn.userId
      this.gyoshaForm.会社コード = userLogIn.会社コード
      this.gyoshaForm.ユーザー種別 = userLogIn.ユーザー種別
      this.gyoshaForm.業者コード = userLogIn.業者コード
      const data = {ユーザー種別: userLogIn.ユーザー種別, 業者コード: userLogIn.業者コード, ユーザーＩＤ: userLogIn.userId}
      this.getPageTitleByMenu()
      this.http
        .get('/api/Reafs_T/GyoshaYuzaToroku/getDisplayData', data)
        .then((response) => {
          if (response.status) {
            this.件数 = response.data.length
            this.dtShutsuryokuKomoku = response.data

            let userLogIn = this.$store.getters.getUserInfo()
            this.TantoEigyojoListRowIdSelect = []
            const data = {
              業者コード: userLogIn.業者コード,
              ユーザー種別: '',
              親ユーザーＩＤ: userLogIn.userId,
              ユーザーＩＤ: ''
            }
            if (userLogIn.ユーザー種別 === '1') {
              data.ユーザー種別 = '2'
            } else if (userLogIn.ユーザー種別 === '0') {
              data.ユーザー種別 = '1'
            }
            if (firstLoad === true) {
              this.http
                .get('/api/Reafs_T/GyoshaYuzaToroku/GetSalesOffice', data, 'アクセスしています...')
                .then((response) => {
                  let objData = []
                  if (response.data) {
                    response.data[0].業者コード枝番 = '000'
                    response.data.map((value, index) => {
                      let newItem = {rowKeyTantoEigyojo: index + 1}
                      Object.assign(newItem, value)
                      newItem.選択 = value.選択 === '1'
                      objData.push(newItem)
                      if (newItem.選択 === true) {
                        this.TantoEigyojoListRowIdSelect.push(index + 1)
                      }
                    })
                  }
                  this.dtTantoEigyojo = objData
                  this.$refs.dtTantoEigyojo.setCheckboxSelected(this.TantoEigyojoListRowIdSelect)
                })
            }
          } else {
            this.dtShutsuryokuKomoku = []
          }
        })
    },
    getPageTitleByMenu () {
      Object.assign(this.menuInfo, {SubMenuName: this.$store.getters.getPageTitle()})
      if (this.gyoshaForm.ユーザー種別 === '0') {
        // this.menuInfo.SubMenuName = this.menuInfo.SubMenuName + '（マスターユーザー）'
        this.setPageTitle(this.menuInfo.SubMenuName + '（マスターユーザー）');
      } else if (this.gyoshaForm.ユーザー種別 === '1') {
        // this.menuInfo.SubMenuName = this.menuInfo.SubMenuName + '（親ユーザー）'
        this.setPageTitle(this.menuInfo.SubMenuName + '（親ユーザー）');
      }
    },
    showError (message, option = null, title = this.menuInfo.SubMenuName) {
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
    showSuccess (message, title = this.menuInfo.SubMenuName) {
      var self = this
      this.MsgInfo({ title,
        message,
        callback: function (a, b) {
          self.hideLoading()
        }})
    },
    confirm (message, title = this.menuInfo.SubMenuName) {
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
    formatDate (date) {
      const d = new Date(date)
      let month = '' + (d.getMonth() + 1)
      let day = '' + d.getDate()
      const year = d.getFullYear()

      if (month.length < 2) { month = '0' + month }
      if (day.length < 2) { day = '0' + day }

      return [year, month, day].join('/')
    },
    check選択 () {
      if (this.dspユーザー種類 === '親ユーザー') {
        return this.$refs.dtTantoEigyojo.getCheckboxSelected().length > 0
      } else {
        return true
      }
    },
    TantoEigyojoCheckboxCheckEvent (isSelected, selectedRowKeys) {
      this.TantoEigyojoListRowIdSelect = selectedRowKeys
    },
    showDataShutsuryo (rowData) {
      this.ShutsuryoRowSelected = rowData
      this.gyoshaForm.ユーザーＩＤ = rowData.ユーザーＩＤ
      this.gyoshaForm.ユーザー名 = rowData.ユーザー名
      this.gyoshaForm.メールアドレス = rowData.メールアドレス
      this.gyoshaForm.部署等 = rowData.部署等
      this.gyoshaForm.利用期間_開始日 = rowData.利用期間開始
      this.gyoshaForm.利用期間_終了日 = rowData.利用期間終了
      this.gyoshaForm.パスワード期限 = rowData.パスワード期限終了
      this.gyoshaForm.削除 = rowData.削除区分 === 1 || rowData.削除区分 === '1'
      this.dspユーザー種類 = rowData.ユーザー種別 === '0' ? 'マスター' : rowData.ユーザー種別 === '1' ? '親ユーザー' : rowData.ユーザー種別 === '2' ? '子ユーザー' : ''
      this.dsp新規更新 = ''
      if (rowData.パスワード期限終了) {
        let d期限終了 = new Date(rowData.パスワード期限終了)
        let sCurrDate = this.base.formatCurrDate()
        let dCurrDate = new Date(sCurrDate)
        this.expirate_date = d期限終了 <= dCurrDate
      }
      if (this.gyoshaForm.ユーザー種別 === '0') {
        if (rowData.ユーザー種別 === '1') {
          this.画面入力制御disabled.chk削除区分 = false
          this.画面入力制御disabled.txt利用期間_開始日 = false
          this.画面入力制御disabled.txt利用期間_終了日 = false
          this.画面入力制御disabled.txtユーザー名 = false
          this.画面入力制御disabled.txt部署等 = false
          this.画面入力制御disabled.txtメールアドレス = false
          this.画面入力制御disabled.btnパスワード再発行 = false
          this.画面入力制御disabled.btn更新 = false
          this.画面入力制御disabled.btn新規 = false
        } else if (rowData.ユーザー種別 === '2') {
          this.画面入力制御disabled.chk削除区分 = false
          this.画面入力制御disabled.txt利用期間_開始日 = true
          this.画面入力制御disabled.txt利用期間_終了日 = true
          this.画面入力制御disabled.txtユーザー名 = true
          this.画面入力制御disabled.txt部署等 = true
          this.画面入力制御disabled.txtメールアドレス = true
          this.画面入力制御disabled.btnパスワード再発行 = false
          this.画面入力制御disabled.btn更新 = false
          this.画面入力制御disabled.btn新規 = true
        }
      } else if (this.gyoshaForm.ユーザー種別 === '1') {
        if (rowData.ユーザー種別 === '2') {
          this.画面入力制御disabled.chk削除区分 = false
          this.画面入力制御disabled.txt利用期間_開始日 = false
          this.画面入力制御disabled.txt利用期間_終了日 = false
          this.画面入力制御disabled.txtユーザー名 = false
          this.画面入力制御disabled.txt部署等 = false
          this.画面入力制御disabled.txtメールアドレス = false
          this.画面入力制御disabled.btnパスワード再発行 = false
          this.画面入力制御disabled.btn更新 = false
          this.画面入力制御disabled.btn新規 = false
        }
      } else {
        this.画面入力制御disabled.btn新規 = true
        this.画面入力制御disabled.btn更新 = true
        this.画面入力制御disabled.btnパスワード再発行 = true
        this.画面入力制御disabled.chk削除区分 = true
        this.画面入力制御disabled.txt利用期間_開始日 = true
        this.画面入力制御disabled.txt利用期間_終了日 = true
        this.画面入力制御disabled.txtユーザー名 = true
        this.画面入力制御disabled.txt部署等 = true
        this.画面入力制御disabled.txtメールアドレス = true
      }
    },
    onShutsuryo_RowClickEvent (rowData, column) {
      if (column.field !== 'ユーザー名') return
      if ((rowData.ユーザー種別 === '0' && this.gyoshaForm.ユーザー種別 === '0') || (rowData.ユーザー種別 === '1' && this.gyoshaForm.ユーザー種別 === '1')) return
      this.showDataShutsuryo(rowData)
      let userLogIn = this.$store.getters.getUserInfo()
      this.TantoEigyojoListRowIdSelect = []
      this.dtTantoEigyojo = []
      const data = {
        業者コード: userLogIn.業者コード,
        ユーザー種別: rowData.ユーザー種別,
        親ユーザーＩＤ: rowData.親ユーザーＩＤ,
        ユーザーＩＤ: rowData.ユーザーＩＤ.replace('-', '')
      }
      this.http
        .get('/api/Reafs_T/GyoshaYuzaToroku/GetSalesOffice', data, 'アクセスしています...')
        .then((response) => {
          let objData = []
          if (response.data) {
            response.data[0].業者コード枝番 = '000'
            response.data.map((value, index) => {
              let newItem = {rowKeyTantoEigyojo: index + 1}
              Object.assign(newItem, value)
              newItem.選択 = value.選択 === '1'
              objData.push(newItem)
              if (newItem.選択 === true) {
                this.TantoEigyojoListRowIdSelect.push(index + 1)
              }
            })
          }
          this.dtTantoEigyojo = objData
          this.$refs.dtTantoEigyojo.setCheckboxSelected(this.TantoEigyojoListRowIdSelect)
        })
    },
    onTableTantoEigyojoRowSelectEvent ({row, rowIndex}) {},
    hideLoading () {
      this.loading = false
    },
    onClickBack () {
      // this.$router.push({ name: "TD00001" });
      //window.location.href = '/Reafs_T/MainMenu'
      this.$router.back()
    },
    async onShinki () {
      this.loading = true
      if (
        this.gyoshaForm.ユーザー名 === '' ||
        this.gyoshaForm.ユーザー名.trim() === ''
      ) {
        this.getMsg('E040663').then((response) => {
          this.showError(response.data)
        })
        this.$refs.refユーザー名.focus()
        return false
      }
      if (
        this.gyoshaForm.メールアドレス === '' ||
        this.gyoshaForm.メールアドレス.trim() === ''
      ) {
        this.getMsg('E040172').then((response) => {
          this.showError(response.data)
        })
        this.$refs.refメールアドレス.focus()
        return false
      }
      if (this.gyoshaForm.利用期間_開始日 === '' || this.gyoshaForm.利用期間_開始日 === undefined ||
        moment(this.gyoshaForm.利用期間_開始日).isValid() === false){
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref利用期間_開始日.focus()
        return false
      }
      if (this.gyoshaForm.利用期間_終了日 === '' || this.gyoshaForm.利用期間_終了日 === undefined ||
        moment(this.gyoshaForm.利用期間_終了日).isValid() === false){
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref利用期間_終了日.focus()
        return false
      }
      if (!this.check選択()) {
        this.getMsg('E040700').then((response) => {
          this.showError(response.data)
        })
        return false
      }

      if (this.gyoshaForm.利用期間_開始日 && this.gyoshaForm.利用期間_終了日 &&
          moment(this.gyoshaForm.利用期間_開始日) > moment(this.gyoshaForm.利用期間_終了日)) {
        this.getMsg('E040279').then((response) => {
          this.showError(response.data)
        })
        this.$nextTick(() => {
          this.$refs.ref利用期間_終了日.focus()
        })
        return false
      }

      let msgConfirm = (await this.getMsg('K000005')).data
      if (!await this.confirm(msgConfirm)) {
        return false
      }
      
      let userLogin = this.$store.getters.getUserInfo()

      let lstTantoEigyojo = []
      let lstTantoEigyojoId = this.$refs.dtTantoEigyojo.getCheckboxSelected()
      this.dtTantoEigyojo.map((value, key) => {
        const bcheck = lstTantoEigyojoId.includes(value.rowKeyTantoEigyojo)
        let newItem = {
          Checked: bcheck,
          会社コード: userLogin.会社コード,
          業者コード: userLogin.業者コード,
          親ユーザーＩＤ: this.gyoshaForm.ユーザーＩＤ.replace('-', '')
        }
        Object.assign(newItem, value)
        lstTantoEigyojo.push(newItem)
      })
      let gyoshaYuzaTorokuInfo = {
        会社コード: userLogin.会社コード,
        業者コード: userLogin.業者コード,
        業者コード枝番: userLogin.業者コード枝番,
        ユーザーＩＤ: userLogin.userId,
        ユーザー種別: userLogin.ユーザー種別 === '0' ? '1' : userLogin.ユーザー種別 === '1' ? '2' : '',
        ユーザー名: this.gyoshaForm.ユーザー名,
        部署等: this.gyoshaForm.部署等,
        メールアドレス: this.gyoshaForm.メールアドレス,
        利用期間開始: this.gyoshaForm.利用期間_開始日,
        利用期間終了: this.gyoshaForm.利用期間_終了日,
        削除区分: this.gyoshaForm.削除 === true ? '1' : '0',
        親ユーザーＩＤ: userLogin.ユーザー種別 === '1' ? userLogin.userId : '',
        親ＩＤ: userLogin.ユーザー種別 === '1' ? userLogin.userId : '',
        lstEigyojo: lstTantoEigyojo
      }
      if (this.ShutsuryoRowSelected) {
        gyoshaYuzaTorokuInfo.ユーザーＩＤ = this.ShutsuryoRowSelected.ユーザーＩＤ.replace('-', '')
        gyoshaYuzaTorokuInfo.親ユーザーＩＤ = this.ShutsuryoRowSelected.親ユーザーＩＤ
      }
      this.http.post('api/Reafs_T/GyoshaYuzaToroku/Shinki', gyoshaYuzaTorokuInfo, 'アクセスしています....')
        .then(async response => {
          this.loading = false
          // this.dtTantoEigyojo = []
          this.TantoEigyojoListRowIdSelect = []
          this.init(false)
          this.showDataShutsuryo(response.data)
          if (response.status) {
            if (response.data.ユーザーＩＤ !== '') {
              this.onShutsuryo_RowClickEvent(response.data, {field: 'ユーザー名'})
              this.dsp新規更新 = '新規'
              // show success
              let msgComplete = (await this.getMsg('M000003')).data
              //20221212 add メール送信を追加
              this.putSendMail(response.data.ユーザーＩＤ,"1")
              //20221212 add
              this.showSuccess('ユーザーID：' + response.data.ユーザーＩＤ + msgComplete)
            }
          } else {
            this.showError(response.message)
          }
        })
    },
    async onGengXin () {
      this.loading = true
      if (
        this.gyoshaForm.ユーザー名 === '' ||
        this.gyoshaForm.ユーザー名.trim() === ''
      ) {
        this.getMsg('E040663').then((response) => {
          this.showError(response.data)
        })
        this.$refs.refユーザー名.focus()
        return false
      }
      if (
        this.gyoshaForm.メールアドレス === '' ||
        this.gyoshaForm.メールアドレス.trim() === ''
      ) {
        this.getMsg('E040172').then((response) => {
          this.showError(response.data)
        })
        this.$refs.refメールアドレス.focus()
        return false
      }
      if (this.gyoshaForm.利用期間_開始日 === '' || this.gyoshaForm.利用期間_開始日 === undefined ||
        moment(this.gyoshaForm.利用期間_開始日).isValid() === false){
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref利用期間_開始日.focus()
        return false
      }
      if (this.gyoshaForm.利用期間_終了日 === '' || this.gyoshaForm.利用期間_終了日 === undefined ||
        moment(this.gyoshaForm.利用期間_終了日).isValid() === false){
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref利用期間_終了日.focus()
        return false
      }
      if (!this.check選択()) {
        this.getMsg('E040700').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      if (this.gyoshaForm.利用期間_開始日 && this.gyoshaForm.利用期間_終了日 &&
          moment(this.gyoshaForm.利用期間_開始日) > moment(this.gyoshaForm.利用期間_終了日)) {
        this.getMsg('E040279').then((response) => {
          this.showError(response.data)
        })
        this.$nextTick(() => {
          this.$refs.ref利用期間_終了日.focus()
        })
        return false
      }
      
      let msgConfirm = (await this.getMsg('K000005')).data
      if (!await this.confirm(msgConfirm)) {
        return false
      }


      if (this.ShutsuryoRowSelected) {
        let userLogin = this.$store.getters.getUserInfo()
        let lstTantoEigyojo = []
        let lstTantoEigyojoId = this.$refs.dtTantoEigyojo.getCheckboxSelected()
        this.dtTantoEigyojo.map((value, key) => {
          const bcheck = lstTantoEigyojoId.includes(value.rowKeyTantoEigyojo)
          let newItem = {
            Checked: bcheck,
            会社コード: userLogin.会社コード,
            業者コード: userLogin.業者コード,
            親ユーザーＩＤ: this.gyoshaForm.ユーザーＩＤ.replace('-', '')
          }
          Object.assign(newItem, value)
          lstTantoEigyojo.push(newItem)
        })

        let gyoshaYuzaTorokuInfo = {
          会社コード: userLogin.会社コード,
          業者コード: userLogin.業者コード,
          業者コード枝番: userLogin.業者コード枝番,
          ユーザーＩＤ: this.ShutsuryoRowSelected.ユーザーＩＤ.replace('-', ''),
          ユーザー種別: this.ShutsuryoRowSelected.ユーザー種別,
          ユーザー名: this.gyoshaForm.ユーザー名,
          部署等: this.gyoshaForm.部署等,
          メールアドレス: this.gyoshaForm.メールアドレス,
          利用期間開始: this.gyoshaForm.利用期間_開始日,
          利用期間終了: this.gyoshaForm.利用期間_終了日,
          削除区分: this.gyoshaForm.削除 === true ? '1' : '0',
          lstEigyojo: lstTantoEigyojo
        }
        this.http.post('api/Reafs_T/GyoshaYuzaToroku/Koshin', gyoshaYuzaTorokuInfo, 'アクセスしています....')
          .then(async response => {
            this.loading = false
            if (response.status) {
              let msgComplete = (await this.getMsg('M000012')).data
              this.showSuccess(msgComplete)
              this.dtTantoEigyojo = []
              this.TantoEigyojoListRowIdSelect = []
              this.init(false)
              this.onShutsuryo_RowClickEvent(response.data, {field: 'ユーザー名'})
              this.dsp新規更新 = '更新'
            }
            this.checkboxOptionCustom.selectedRowKeys = this.TantoEigyojoListRowIdSelect
          })
      }
      // window.location.href = '/Reafs_T/MainMenu'
    },
    onPasuwadoHakko () {
      this.loading = true
      let userLogin = this.$store.getters.getUserInfo()
      let pasuwadoRimaindaInfo = {業者コード: userLogin.業者コード, ユーザーＩＤ: this.ShutsuryoRowSelected.ユーザーＩＤ.replace('-', ''), ソルト値: this.ShutsuryoRowSelected.ソルト値}
      this.http.put('api/Reafs_T/GyoshaYuzaToroku/putM015_2', pasuwadoRimaindaInfo)
        .then(res => {
          this.loading = false
          if (res.status === true) {
            //20221212 add メール送信を追加
            this.putSendMail(this.ShutsuryoRowSelected.ユーザーＩＤ,"2")
            //20221212 add
            this.getMsg('M040028').then((response) => {
              this.showSuccess(response.data, 'パスワード再発行')
            })
          // window.location.href = '/Reafs_T/MainMenu'
          }
        })
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    putSendMail (userId,mode) {
    //20221212 add メール送信メソッド追加
      const data = {
        ユーザーＩＤ: userId.replace('-', ''),
        mode
      }
      this.http.post('/api/Reafs_T/GyoshaYuzaToroku/PostSendMail', data)
      //20221212 add
    },
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
  // min-width: 1380px;
  height: 100%;
}
.sample-form-class {
  height: 100%;
}
.home-el-container {
  height: 100%;
}
.home-el-header {
  padding: 0 0 20px 0;
}
.home-el-main {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
}
.home-el-footer {
  padding: 10px 0 0 0;
}
.item_input_gyoshaYuzaToroku {
    width: 100%;
    margin-left: 20px;
}
.item_input_PasuwadoKigen {
  width: 120px;
  margin-left: 10px;
}
.Kigengire {
  width: 120px;
  margin-left: 30px;
  color: red;
}
.PasuwadoHakko {
  //margin-left: 30px;
  // float: right;
  padding-top: 5px;
  padding-bottom: 5px;
}
.clsdiv{
  margin: 10px 0;
  background-color:#303133;
}
</style>
<style>
.GyoshaYuzaToroku .ve-table-header .ve-table-header-tr
{
  height: 20px !important;
}
.GyoshaYuzaToroku .dtTantoEigyojo .ve-table-container {
  height: 303px !important;
}
.GyoshaYuzaToroku .dtShutsuryokuKomoku .ve-table-container {
  height: 341px !important;
}
.GyoshaYuzaToroku .dtShutsuryokuKomoku {
  width: 100%;
}
.GyoshaYuzaToroku .tantotable > .ve-table.ve-table-border-around {
  height: 303px !important;
}
.GyoshaYuzaToroku .gyoshatable > .ve-table.ve-table-border-around {
  height: 343px !important;
}
</style>
