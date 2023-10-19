<template>
  <div class="KojiIchiran home-container">
    <el-form
      :model="kojiIchiranForm"
      :rules="rules"
      ref="refkojiIchiran"
      label-width="105px"
      class="sample-form-class"
      :hide-required-asterisk="true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row class="rForm">
            <el-col style="width: 32%;">
              <el-form-item label="状態" prop="状態" class="is-required">
                  <el-select
                    required
                    tabindex="1"
                    v-model="kojiIchiranForm.状態"
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
              <el-form-item label="物件" prop="物件" style="margin-bottom: 0px !important;">
                <div style="display: flex">
                  <!--<reafsinputtext
                    tabindex="4"
                    maxlength="8"
                    v-model="kojiIchiranForm.物件コード"
                    :width="'95px'"
                    @blur="onBlurBukkenKodo"
                    ref="ref物件コード"
                    >
                  </reafsinputtext>
                  <reafsinputtext
                    maxlength="20"
                    v-model="kojiIchiranForm.物件名"
                    :width="'199px'"
                    class="is-readonly"
                    disabled="disabled"
                  >
                  </reafsinputtext>-->
                  <opensearch
                    tabindex="4"
                    maxlength="8"
                    v-model="kojiIchiranForm.物件コード"
                    :width="'95px'"
                    :appendlabelText="kojiIchiranForm.物件名"
                    appendlabelWidth="198px"
                    ref="ref物件コード"
                    :path="'reafsmodalSearchBukken'" 
                    @backData="reafsmodalBack_searchBukken"
                    >
                  </opensearch>
                </div>
              </el-form-item>
              <el-form-item label="依頼日" prop="依頼日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="10"
                  v-model="kojiIchiranForm.依頼日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref依頼日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="11"
                  v-model="kojiIchiranForm.依頼日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref依頼日_終了"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="見積提出日" prop="見積提出日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="16"
                  v-model="kojiIchiranForm.見積提出日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref見積提出日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="17"
                  v-model="kojiIchiranForm.見積提出日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref見積提出日_終了"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col style="width: 32%;">
              <el-form-item label="応諾" prop="応諾" class="is-required">
                  <el-select
                    required
                    tabindex="2"
                    v-model="kojiIchiranForm.応諾"
                    ref="ref応諾"
                    style="width: 294px;"
                  >
                    <el-option
                      v-for="item in 応諾value"
                      :key="item.名称コード"
                      :label="item.名称コード_名称"
                      :value="item.名称コード"
                    >
                    </el-option>
                  </el-select>
                </el-form-item>
              <el-form-item label="発注日" prop="発注日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="5"
                  v-model="kojiIchiranForm.発注日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref発注日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="6"
                  v-model="kojiIchiranForm.発注日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref発注日_終了"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="請日" prop="請日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="12"
                  v-model="kojiIchiranForm.請日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref請日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="13"
                  v-model="kojiIchiranForm.請日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref請日_終了"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="作業予定日" prop="作業予定日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="18"
                  v-model="kojiIchiranForm.作業予定日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作業予定日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="19"
                  v-model="kojiIchiranForm.作業予定日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作業予定日_終了"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col style="width: 30%;">
              <el-form-item label="作業報告" prop="作業報告" class="is-required">
                  <el-select
                    required
                    tabindex="3"
                    v-model="kojiIchiranForm.作業報告"
                    ref="ref作業報告"
                    style="width: 294px;"
                  >
                    <el-option
                      v-for="item in 作業報告value"
                      :key="item.名称コード"
                      :label="item.名称コード_名称"
                      :value="item.名称コード"
                    >
                    </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="作業実績" prop="作業実績" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="7"
                  v-model="kojiIchiranForm.作業実績日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作業実績日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="8"
                  v-model="kojiIchiranForm.作業実績日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作業実績日_終了"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="作業報告日" prop="作業報告日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="14"
                  v-model="kojiIchiranForm.作業報告日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作業報告日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="15"
                  v-model="kojiIchiranForm.作業報告日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref作業報告日_終了"
                ></el-date-picker>
              </el-form-item>
              <el-form-item label="請求日" prop="請求日" style="margin-bottom: 0px !important;">
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="20"
                  v-model="kojiIchiranForm.請求日_開始"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref請求日_開始"
                ></el-date-picker>
                <label>〜</label>
                <el-date-picker
                  maxlength="10"
                  type="date"
                  placeholder=""
                  tabindex="21"
                  v-model="kojiIchiranForm.請求日_終了"
                  style="width: 135px;"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref請求日_終了"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="1" style="padding-top: 2px; margin-left: 10px;">
            <el-form-item style="width: 100%; height: 31px"></el-form-item>
              <el-button
                tabindex="9"
                type="primary"
                size="mini"
                @click="onSearch">検索</el-button>
            </el-col>
            <el-col style="width: 100%;">
              <el-form-item label="工事名称" prop="工事名称">
                <reafsinputtext
                  maxlength="40"
                  tabindex="22"
                  v-model="kojiIchiranForm.工事名称"
                  :width="'569px'"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
        </el-header>
        <el-main class="home-el-main">
          <el-row>
            <el-col :span="18">
              <span style="float: right; margin-right: 19px;">{{件数 | formatNumber}}&nbsp;件</span>
            </el-col>
            <el-col :span="24" style="height: 525px;">
              <reafstable
                ref="dtMeisaiKomoku"
                tabindex="23"
                :tableData="dtMeisaiKomoku"
                :columns="columnsMeisaiKomoku"
                :maxHeight="499"
                :height="501"
                :scrollWidth="1700"
                :tableStyle="'width:100%;'"
                :cellStyleOption="cellStyleOptionMeisaiKomoku"
                :rowStyleOption="rowStyleOption"
                :rowKeyFieldName="rowKeyFieldNameMeisaiKomoku"
                :editOption="editOption"
                :hideSelect = true
                :cellClickEvent = "onMeisaiKomoku_RowClickEvent"
                :HiddenColumnKeys = "hiddenColumnsMeisaiKomoku"
                :checkboxCheckEvent = "MeisaiKomokuCheckboxCheckEvent"
                :customStyle = "MeisaiKomokuStyle"
              ></reafstable>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer border-top" height="auto">
          <el-row>
            <el-col :span="2"><div class="grid-content">&nbsp;</div></el-col>
            <el-col :span="3">
              <el-button
                tabindex="24"
                type="primary"
                @click="onHatchuSho">
                <span>発注書印刷</span>
              </el-button>
            </el-col>
            <el-col :span="2">
              <el-button
                tabindex="25"
                type="primary"
                @click="onDianziYingnuo">
                <span>電子応諾</span>
              </el-button>
            </el-col>
            <el-col :span="10"><div class="grid-content">&nbsp;</div></el-col>
            <el-col :span="3">
              <el-button
                tabindex="26"
                type="primary"
                @click="onSagyoYoteiKoshin">
                <span>作業予定更新</span>
              </el-button>
            </el-col>
            <el-col :span="1" style="margin-left: 20px;">
              <el-button tabindex="27" @click="onClickBack">戻る </el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafstable from '@/components/table/Grid.vue'
import opensearch from '@/components/basic/openSearch.vue'
import moment from 'moment'

var MeisaiKomokuListRowIdSelect = []
var MeisaiKomokuListSearch = []

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafstable,
    opensearch,
    moment
  },
  data () {
    return {
      loading: false,
      MeisaiKomokuStyle: 'width: 1670px;',
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: '',
        業者コード: '',
        業者コード枝番: '',
        ユーザー種別: '',
        親ＩＤ: ''
      },
      kojiIchiranForm: {
        状態: '',
        応諾: '',
        作業報告: '',
        物件コード: '',
        物件名: '',
        依頼日_開始: '',
        依頼日_終了: '',
        見積提出日_開始: '',
        見積提出日_終了: '',
        工事名称: '',
        発注日_開始: '',
        発注日_終了: '',
        請日_開始: '',
        請日_終了: '',
        作業予定日_開始: '',
        作業予定日_終了: '',
        作業実績日_開始: '',
        作業実績日_終了: '',
        作業報告日_開始: '',
        作業報告日_終了: '',
        請求日_開始: '',
        請求日_終了: ''
      },
      queryParams: {
        StartupMode: ''
      },
      状態value: [
        {名称コード: '1', 名称コード_名称: '未完成'},
        {名称コード: '2', 名称コード_名称: '完成'},
        {名称コード: '3', 名称コード_名称: '支払'},
        {名称コード: '4', 名称コード_名称: '保留'},
        {名称コード: '5', 名称コード_名称: '中止'}
      ],
      応諾value: [
        {名称コード: '', 名称コード_名称: '全て'},
        {名称コード: '1', 名称コード_名称: '応諾'},
        {名称コード: '0', 名称コード_名称: '未応諾'},
        {名称コード: 'K', 名称コード_名称: '未応諾（高額）'}
      ],
      作業報告value: [
        {名称コード: '', 名称コード_名称: '全て'},
        {名称コード: '1', 名称コード_名称: '予定未入力'},
        {名称コード: '2', 名称コード_名称: '報告未入力'},
        {名称コード: '3', 名称コード_名称: '予定未確認'},
        {名称コード: '4', 名称コード_名称: '完了'}
      ],
      checkboxOptionCustom: {
        hideSelectAll: true,
        selectedRowKeys: [],
        selectedRowChange: ({ row, isSelected, selectedRowKeys }) => {
          let key = row.rowKeyMeisaiKomoku
          if (!this.MeisaiKomokuListRowIdSelect.includes(key)) {
            this.MeisaiKomokuListRowIdSelect.push(key)
          } else {
            this.MeisaiKomokuListRowIdSelect = this.MeisaiKomokuListRowIdSelect.filter(element => element !== key)
          }
          this.checkboxOptionCustom.selectedRowKeys = this.MeisaiKomokuListRowIdSelect
        }
      },
      件数: 0,
      rowKeyFieldNameMeisaiKomoku: 'rowKeyMeisaiKomoku',
      dtMeisaiKomoku: [],
      henkonFlag: [],
      columnsMeisaiKomoku: [
        { key: 'No',
          title: 'No',
          align: 'center',
          vAlign: 'center',
          width: 70,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return ++rowIndex
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        {
          field: '指示',
          key: '指示',
          title: '指示',
          type: 'checkbox',
          align: 'center',
          vAlign: 'center',
          width: 40
        },
        { field: '応諾',
          key: '応諾',
          title: '応諾',
          align: 'center',
          vAlign: 'center',
          width: 40,
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '工事区分',
          key: '工事区分',
          title: '工事区分',
          align: 'center',
          vAlign: 'center',
          width: 90,
          renderBodyCell: ({ row }) => {
            const text = row['工事区分']
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
        { field: '工事依頼No',
          key: '工事依頼No',
          title: '工事依頼No',
          align: 'center',
          vAlign: 'center',
          width: 90,
          renderBodyCell: ({ row }) => {
            const text = row['工事依頼No']
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
        { field: '物件名',
          key: '物件名',
          title: '物件名',
          align: 'center',
          vAlign: 'center',
          width: 170,
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
        { field: '工事名称',
          key: '工事名称',
          title: '工事名称',
          align: 'center',
          vAlign: 'center',
          width: 250,
          renderBodyCell: ({ row }) => {
            const text = row['工事名称']
           /* let dsp工事依頼No = ''
            if (row.工事依頼No !== undefined && row.工事依頼No !== '') {
              dsp工事依頼No = row.工事依頼No.replace('-', '')
            }
            let url = `/Reafs_T/SyuzenMitsumoriNyuryoku?工事依頼No=${dsp工事依頼No}`
            url += `&工事依頼No枝番=${row.工事依頼No枝番}&業者コード=${row.業者コード}&業者コード枝番=${row.業者コード枝番}`
            return (
              <div title={text} style="display: block" class="align-left">
                <a class="ellipsis-text" href={url} >
                  {text}
                </a></div>
            )*/
            return (
              <div title={text} style="display: block" class="align-left">
                <a class="ellipsis-text" style="color: blue" >
                  {text}
                </a></div>
            )
          }
        //   ellipsis: {
        //     showTitle: true,
        //     lineClamp: 1
        //   }
        },
        { field: '金額',
          key: '金額',
          title: '金額',
          align: 'center',
          vAlign: 'center',
          width: 140,
          renderBodyCell: ({ row }) => {
            const text = row['金額']
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
        { field: '状態',
          key: '状態',
          title: '状態',
          align: 'center',
          vAlign: 'center',
          width: 120,
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
        { field: '見積提出日',
          key: '見積提出日',
          title: '見積提出日',
          align: 'center',
          vAlign: 'center',
          width: 85,
          renderBodyCell: ({ row, column, rowIndex }) => {
            let cellData見積提出日 = row[column.field] // row['見積提出日']
            let dsp見積提出日 = cellData見積提出日 !== '' && cellData見積提出日 !== null ? cellData見積提出日.substr(2) : ''
            return (
              <div title={dsp見積提出日} style="display: block">
                <span style="text-align: center">
                  {dsp見積提出日}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '発注日',
          key: '発注日',
          title: '発注日',
          align: 'center',
          vAlign: 'center',
          width: 85,
          renderBodyCell: ({ row, column, rowIndex }) => {
            let cellData発注日 = row[column.field] // row['発注日']
            let dsp発注日 = cellData発注日 !== '' && cellData発注日 !== null ? cellData発注日.substr(2) : ''
            return (
              <div title={dsp発注日} style="display: block">
                <span style="text-align: center">
                  {dsp発注日}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { title: '発注工期',
          children: [
            { field: '発注工期ＳＴ',
              key: '発注工期ＳＴ',
              title: '開始日',
              width: 80,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData発注工期ST = row['発注工期ＳＴ']
                let dsp発注工期ST = cellData発注工期ST !== '' && cellData発注工期ST !== null ? cellData発注工期ST.substr(2) : ''
                return (
                  <div title={dsp発注工期ST} style="display: block">
                    <span>{dsp発注工期ST}</span>
                  </div>
                )
              },
              ellipsis: {
                showTitle: true,
                lineClamp: 1
              }
            },
            { field: '発注工期ＥＤ',
              key: '発注工期ＥＤ',
              title: '終了日',
              width: 80,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData発注工期ED = row['発注工期ＥＤ']
                let dsp発注工期ED = cellData発注工期ED !== '' && cellData発注工期ED !== null ? cellData発注工期ED.substr(2) : ''
                return (
                  <div title={dsp発注工期ED} style="display: block">
                    <span>{dsp発注工期ED}</span>
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
        { field: '請日',
          key: '請日',
          title: '請日',
          align: 'center',
          vAlign: 'center',
          width: 85,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const cellData請日 = row[column.field] // row['請日']
            let dsp請日 = cellData請日 !== '' && cellData請日 !== null ? cellData請日.substr(2) : ''
            return (
              <div title={dsp請日} style="display: block">
                <span style="text-align: center">
                  {dsp請日}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { title: '作業予定',
          children: [
            { field: '作業予定開始日',
              key: '作業予定開始日',
              title: '開始日',
              width: 140,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData作業予定開始日 = row['作業予定開始日']
                const cellData完了報告 = row['完了報告']
                const cellData継続区分= row['継続区分']
                return (
                  <div title="" style="display: block">
                    <el-date-picker
                      required
                      size="small"
                      value={cellData作業予定開始日}
                      disabled={cellData完了報告 == '済' || cellData継続区分 == '1' || cellData継続区分 == '2'}
                      on-input={(val) => {
                        row['作業予定開始日'] = val
                        this.cellDataChange(row, column, val)
                      }}
                      on-blur={(val) => {
                        this.onBlurDatePicker(row, column, val)
                      }}
                      style="width: 129px;"
                      type="date"
                      value-format="yyyy/MM/dd"
                      maxlength="10"
                      format="yyyy/MM/dd"
                      ref={'refTbl作業予定開始日' + rowIndex}
                    ></el-date-picker>
                  </div>
                )
              },
              ellipsis: {
                showTitle: true,
                lineClamp: 1
              }
            },
            { field: '作業予定終了日',
              key: '作業予定終了日',
              title: '終了日',
              width: 140,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData作業予定終了日 = row['作業予定終了日']
                const cellData完了報告 = row['完了報告']
                const cellData継続区分= row['継続区分']
                return (
                  <div title="" style="display: block">
                    <el-date-picker
                      required
                      size="small"
                      value={cellData作業予定終了日}
                      disabled={cellData完了報告 == '済' || cellData継続区分 == '1' || cellData継続区分 == '2'}
                      on-input={(val) => {
                        row['作業予定終了日'] = val
                        this.cellDataChange(row, column, val)
                      }}
                      style="width: 129px;"
                      type="date"
                      value-format="yyyy/MM/dd"
                      maxlength="10"
                      ref={'refTbl作業予定終了日' + rowIndex}
                      format="yyyy/MM/dd"
                    ></el-date-picker>
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
        { title: '作業実績',
          children: [
            { field: '作業実績開始日',
              key: '作業実績開始日',
              title: '開始日',
              width: 80,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData作業実績開始日 = row['作業実績開始日']
                let dsp作業実績開始日 = cellData作業実績開始日 !== '' && cellData作業実績開始日 !== null ? cellData作業実績開始日.substr(2) : ''
                return (
                  <div title={dsp作業実績開始日} style="display: block">
                    <span>{dsp作業実績開始日}</span>
                  </div>
                )
              },
              ellipsis: {
                showTitle: true,
                lineClamp: 1
              }
            },
            { field: '作業実績終了日',
              key: '作業実績終了日',
              title: '終了日',
              width: 80,
              renderBodyCell: ({ row, column, rowIndex }) => {
                const cellData作業実績終了日 = row['作業実績終了日']
                let dsp作業実績終了日 = cellData作業実績終了日 !== '' && cellData作業実績終了日 !== null ? cellData作業実績終了日.substr(2) : ''
                return (
                  <div title={dsp作業実績終了日} style="display: block">
                    <span>{dsp作業実績終了日}</span>
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
        { field: '完了報告',
          key: '完了報告',
          title: '完了報告',
          align: 'center',
          vAlign: 'center',
          width: 50,
          renderBodyCell: ({ row }) => {
            const text = row['完了報告']
            return (
              <div title={text} style="display: block">
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
        { field: '作業報告日',
          key: '作業報告日',
          title: '作業報告日',
          align: 'center',
          vAlign: 'center',
          width: 85,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const cellData作業報告日 = row['作業報告日']
            let dsp作業報告日 = cellData作業報告日 !== '' && cellData作業報告日 !== null ? cellData作業報告日.substr(2) : ''
            return (
              <div title={dsp作業報告日} style="display: block">
                <span style="text-align: center">
                  {dsp作業報告日}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '作業結果確認日',
          key: '作業結果確認日',
          title: '作業結果確認日',
          align: 'center',
          vAlign: 'center',
          width: 80,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const cellData作業結果確認日 = row['作業結果確認日']
            let dsp作業結果確認日 = cellData作業結果確認日 !== '' && cellData作業結果確認日 !== null ? cellData作業結果確認日.substr(2) : ''
            return (
              <div title={dsp作業結果確認日} style="display: block">
                <span style="text-align: center">
                  {dsp作業結果確認日}
                </span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '請求日',
          key: '請求日',
          title: '請求日',
          align: 'center',
          vAlign: 'center',
          width: 80,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const cellData請求日 = row['請求日']
            let dsp請求日 = cellData請求日 !== '' && cellData請求日 !== null ? cellData請求日.substr(2) : ''
            return (
              <div title={dsp請求日} style="display: block">
                <span style="text-align: center">
                  {dsp請求日}
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
          field: '会社コード',
          key: '会社コード'
        },
        {
          field: '発注サブNo',
          key: '発注サブNo'
        },
        {
          field: '工事依頼No枝番',
          key: '工事依頼No枝番'
        },
        {
          field: '業者コード',
          key: '業者コード'
        },
        {
          field: '業者コード枝番',
          key: '業者コード枝番'
        },
        {
          field: '検収回数',
          key: '検収回数'
        }
      ],
      hiddenColumnsMeisaiKomoku: ['会社コード', '発注サブNo', '工事依頼No枝番', '業者コード', '業者コード枝番', '検収回数','F044_VRSION','F052_VRSION'],
      cellStyleOptionMeisaiKomoku: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          let sClass = ''
          let check作業予定開始日 = row['作業予定開始日']
          let check作業予定終了日 = row['作業予定終了日']
          let check変更前作業予定開始日 = row['変更前作業予定開始日']
          let check変更前作業予定終了日 = row['変更前作業予定終了日']
          if (check作業予定終了日 === '' || check作業予定終了日 === null || check作業予定開始日 === '' || check作業予定開始日 === null ||
              check変更前作業予定終了日 === '' || check変更前作業予定終了日 === null || check変更前作業予定開始日 === '' || check変更前作業予定開始日 === null) {
            if (column.key == '完了報告') {
              sClass += ' nolink'
            }
          }
          let check発注確定フラグ = row['発注確定フラグ']
          let check継続区分 = row['継続区分']
          if (column.key == '指示') {
            if(check発注確定フラグ === 1 && check継続区分 !== '1' && check継続区分 !== '2'){
              sClass = 'enable-checkbox'
            }else{
              sClass = 'disable-checkbox'
            }
          }

          return sClass
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

  filters: {
    formatNumber: function (iNumber) {
      return iNumber.toFixed().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },

  created () {
    this.queryParams = {
      StartupMode: this.$route.params.StartupMode,
      工事名称: this.$route.params.工事名称
    }
    this.init(true)
  },
  mounted () {
    this.$refs.ref状態.focus()
  },
  watch: {
    $route(to, from) {
      if (to.name == "TD00100") {
        this.queryParams = {
          StartupMode: this.$route.params.StartupMode,
          工事名称: this.$route.params.工事名称
        }
        //20221028　rep 作業完了報告画面から戻って来た時、条件を維持したまま再検索を行うように修正。
        //this.init(true)
        if (this.queryParams.StartupMode != null){
          this.init(true)
        }else{
          this.onSearch()
        }
        //20221028　rep
      }
    },
  },
  methods: {
    init (firstLoad = true) {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.会社コード = userLogIn.会社コード
      this.userInfo.業者コード = userLogIn.業者コード
      this.userInfo.業者コード枝番 = userLogIn.業者コード枝番
      this.userInfo.ユーザー種別 = userLogIn.ユーザー種別
      this.userInfo.親ＩＤ = userLogIn.親ＩＤ
      //this.kojiIchiranForm.状態 = '1'
      //this.kojiIchiranForm.応諾 = ''
      //this.kojiIchiranForm.作業報告 = ''
      
      if (this.queryParams.StartupMode != null){
        if(this.queryParams.StartupMode === '1'){
          this.kojiIchiranForm.状態 = '1'
          this.kojiIchiranForm.応諾 = ''
          this.kojiIchiranForm.作業報告 = ''
        }else if(this.queryParams.StartupMode === '2'){
          this.kojiIchiranForm.状態 = '1'
          this.kojiIchiranForm.応諾 = ''
          this.kojiIchiranForm.作業報告 = '1'
        }else if(this.queryParams.StartupMode === '3'){
          this.kojiIchiranForm.状態 = '1'
          this.kojiIchiranForm.応諾 = ''
          this.kojiIchiranForm.作業報告 = '2'
        }else if(this.queryParams.StartupMode === '4'){
          this.kojiIchiranForm.状態 = '1'
          this.kojiIchiranForm.応諾 = 'K'
          this.kojiIchiranForm.作業報告 = ''
        }else if(this.queryParams.StartupMode === '5'){
          this.kojiIchiranForm.状態 = '1'
          this.kojiIchiranForm.応諾 = '0'
          this.kojiIchiranForm.作業報告 = ''
          this.kojiIchiranForm.工事名称 = this.queryParams.工事名称
        }
        this.onSearch()
      }else{
        this.kojiIchiranForm.状態 = '1'
        this.kojiIchiranForm.応諾 = ''
        this.kojiIchiranForm.作業報告 = ''
      }
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
    hideLoading () {
      this.loading = false
    },
    reafsmodalBack_searchBukken (obj) {
      this.kojiIchiranForm.物件コード = obj.txtCd
      this.kojiIchiranForm.物件名 = obj.lblNm
    },
    async onMeisaiKomoku_RowClickEvent (rowData, column) {
      if (column.field !== '完了報告') {

      } else {
        if (column.field === '完了報告') {
          if (rowData.作業予定開始日 !== '' && rowData.作業予定開始日 !== null && rowData.作業予定終了日 !== '' && rowData.作業予定終了日 !== null &&
              rowData.変更前作業予定開始日 !== '' && rowData.変更前作業予定開始日 !== null && rowData.変更前作業予定終了日 !== '' && rowData.変更前作業予定終了日 !== null) {
            //window.location.href = `/Reafs_T/SagyoHokokuNyuryoku?業者コード=${this.userInfo.業者コード}&業者コード枝番=${this.userInfo.業者コード枝番}&工事依頼No=${rowData.工事依頼No.replace('-', '')}&工事依頼No枝番=${rowData.工事依頼No枝番}`
            //window.location.href = `/Reafs_T/SagyoHokokuNyuryoku?業者コード=${rowData.業者コード}&業者コード枝番=${rowData.業者コード枝番}&工事依頼No=${rowData.工事依頼No.replace('-', '')}&工事依頼No枝番=${rowData.工事依頼No枝番}`
            //window.location.href = `/Reafs_T/SagyoHokokuNyuryoku?業者コード=${rowData.業者コード}&業者コード枝番=${rowData.業者コード枝番}&工事依頼No=${rowData.工事依頼No.replace('-', '')}&工事依頼No枝番=${rowData.工事依頼No枝番}&発注サブNo=${rowData.発注サブNo}&検収回数=${rowData.検収回数}`
            if(rowData.完了報告 == '未') {
              let checkExist = false
              await this.http
                .get('/api/Reafs_T/Koji/TD00100/CheckF052Exist', { 会社コード: rowData.会社コード , 工事依頼No: rowData.工事依頼No.replace('-', ''), 発注サブNo: rowData.発注サブNo, 検収回数: rowData.検収回数}, 'アクセスしています...')
                .then((response) => {
                  checkExist = response.data.isF052Exist
                })
              if (checkExist == false) {
                this.getMsg('E040098').then((response) => {
                  this.showError(response.data)
                })
                this.$refs.koujiiraiNo.focus()
                return false
          }
        }
            this.$router.push({ name: 'TD00101', params: { 業者コード: rowData.業者コード, 業者コード枝番: rowData.業者コード枝番, 工事依頼No: rowData.工事依頼No.replace('-', ''), 工事依頼No枝番: rowData.工事依頼No枝番, 発注サブNo: rowData.発注サブNo, 検収回数: rowData.検収回数 } })
          }
        }
      }
      if (column.field !== '工事名称') {

      } else {
        if (column.field === '工事名称') {
          if (rowData.工事依頼No !== undefined && rowData.工事依頼No !== '') {
            this.$router.push({ name: 'TD00301', params: { 業者コード: rowData.業者コード, 業者コード枝番: rowData.業者コード枝番, 工事依頼No: rowData.工事依頼No.replace('-', ''), 工事依頼No枝番: rowData.工事依頼No枝番, 見積書枝番: rowData.見積書枝番 } })
          }
        }
      }
    },
    MeisaiKomokuCheckboxCheckEvent (isSelected, selectedRowKeys) {
      this.MeisaiKomokuListRowIdSelect = selectedRowKeys
    },

    onClickBack () {
      // this.$router.push({ name: "TD00001" });
      //window.location.href = '/Reafs_T/MainMenu'
      this.$router.back()
    },
    async onSearch () {
      this.loading = true
      let data = {
        ユーザー種別: this.userInfo.ユーザー種別,
        業者コード: this.userInfo.業者コード,
        ユーザーID: this.userInfo.ユーザーＩＤ,
        親ＩＤ: this.userInfo.親ＩＤ,
        状態: this.kojiIchiranForm.状態,
        // 状態: '',
        応諾: this.kojiIchiranForm.応諾,
        作業報告: this.kojiIchiranForm.作業報告,
        物件コード: this.kojiIchiranForm.物件コード,
        発注日_開始: this.kojiIchiranForm.発注日_開始,
        発注日_終了: this.kojiIchiranForm.発注日_終了,
        作業実績日_開始: this.kojiIchiranForm.作業実績日_開始,
        作業実績日_終了: this.kojiIchiranForm.作業実績日_終了,
        依頼日_開始: this.kojiIchiranForm.依頼日_開始,
        依頼日_終了: this.kojiIchiranForm.依頼日_終了,
        請日_開始: this.kojiIchiranForm.請日_開始,
        請日_終了: this.kojiIchiranForm.請日_終了,
        作業報告日_開始: this.kojiIchiranForm.作業報告日_開始,
        作業報告日_終了: this.kojiIchiranForm.作業報告日_終了,
        見積提出日_開始: this.kojiIchiranForm.見積提出日_開始,
        見積提出日_終了: this.kojiIchiranForm.見積提出日_終了,
        作業予定日_開始: this.kojiIchiranForm.作業予定日_開始,
        作業予定日_終了: this.kojiIchiranForm.作業予定日_終了,
        請求日_開始: this.kojiIchiranForm.請求日_開始,
        請求日_終了: this.kojiIchiranForm.請求日_終了,
        工事名称: this.kojiIchiranForm.工事名称
      }
      await this.http
        .post('/api/Reafs_T/Koji/TD00100/Search', data, 'アクセスしています...')
        .then((response) => {
          this.loading = false
          let objData = []
          if (response.status) {
            this.件数 = response.data.length
            response.data.map((value, index) => {
              let newItem = {rowKeyMeisaiKomoku: index + 1}
              Object.assign(newItem, value)
              newItem.指示 = false
              objData.push(newItem)
            })
            this.dtMeisaiKomoku = objData
            this.$refs.dtMeisaiKomoku.setCheckboxSelected([])
            MeisaiKomokuListSearch = []
            objData.forEach((val) => {
              MeisaiKomokuListSearch.push(Object.assign({}, val))
            })
          } else {
            this.dtMeisaiKomoku = []
          }
          // this.$refs.ref作業予定日_開始.focus()
        })
    },
    async onHatchuSho () {
      this.loading = true
      let lstTantoEigyojoId = this.$refs.dtMeisaiKomoku.getCheckboxSelected()
      let dtPrint = this.dtMeisaiKomoku.filter((val, idx, arr) => {
        return lstTantoEigyojoId.includes(idx + 1)
      }).map(rowData => {
        rowData.指示 = true
        return rowData
      })
      if (dtPrint === undefined || dtPrint === null || dtPrint.length === 0) {
        this.getMsg('E040245').then(res => {
              this.showError(res.data)
        })
        return false
      } else {
        await this.print(dtPrint)
      }
      this.loading = false
    },
    async print (dtPrint) {
      let lstSelect = []
      for (let row of dtPrint) {
        lstSelect.push({
          会社コード: this.userInfo.会社コード,
          工事依頼No: row.工事依頼No,
          工事依頼No枝番: row.工事依頼No枝番,
          業者コード: row.業者コード,
          業者コード枝番: row.業者コード枝番,
          発注サブNo: row.発注サブNo
        })
      }
      lstSelect = lstSelect.filter(function (a) {
        var key = a.会社コード + '|' + a.工事依頼No + '|' + a.工事依頼No枝番 + '|' + a.発注サブNo
        if (!this[key]) {
          this[key] = true
          return true
        }
      }, Object.create(null))
      const data = {
        lstSelect
      }
      await this.http
        //.post('api/Reafs_T/Common/TP00101/ExportReport', data, 'アクセスしています....', {
        .post('api/Reafs_T/Koji/TD00100/JoinPdfFile', data, 'アクセスしています....', {
          responseType: 'blob'
        })
        .then(async blob => {
          if (blob.size === 0) {
            this.getMsg('E010224').then(res => {
              this.showError(res.data)
            })
            return
          }
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: '発注書・請書.pdf'
          // })

          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()
          var now = new Date()
          var year = now.getFullYear()
          var month = now.getMonth() + 1
          var date = now.getDate()
          var time = now.getHours()
          var minuts = now.getMinutes()
          var sec = now.getSeconds() 
          var milSec = now.getMilliseconds()
          var fileName = '発注書・請書_' + year + month + date + time + minuts + sec + milSec + '.pdf'
          await this.base.saveFileDownload(blob, fileName)

          this.getMsg('M040020').then(response => {
            this.showSuccess(response.data)
          })
        })
        .catch(ex => console.log(ex))
        .finally(() => {
        })
        .catch(ex => console.log(ex))
    },
    async onDianziYingnuo () {
      this.loading = true
      let lstTantoEigyojoId = this.$refs.dtMeisaiKomoku.getCheckboxSelected()
      let dtPrint = this.dtMeisaiKomoku.filter((val, idx, arr) => {
        return lstTantoEigyojoId.includes(idx + 1)
      }).map(rowData => {
        rowData.指示 = true
        return rowData
      })
      if (dtPrint === undefined || dtPrint === null || dtPrint.length === 0) {
        this.getMsg('E040245').then(res => {
              this.showError(res.data)
        })
        return false
      } else {
        for (let i = 0; i < dtPrint.length; i++) {
          if (dtPrint[i].応諾 === '●' || dtPrint[i].発注確定フラグ != 1) {
            this.getMsg('E040718').then((response) => {
              this.showError(response.data)
            })
            return false
          }
        }
        let msgConfirm = (await this.getMsg('K040082')).data
        if (!await this.confirm(msgConfirm)) {
          return false
        }
        // ハノイ側修正2022/08/24 STEP2_T 課題管理表№8：工事一覧「2022/08/19仕様変更分」処理概要：電子応諾ボタン押下時の処理を修正...START
        let lstSelect = []
        for (let row of dtPrint) {
          lstSelect.push({
            会社コード: this.userInfo.会社コード,
            工事依頼No: row.工事依頼No,
            工事依頼No枝番: row.工事依頼No枝番,
            業者コード: row.業者コード,
            業者コード枝番: row.業者コード枝番,
            発注サブNo: row.発注サブNo,
            F044_VERSION: row.F044_VERSION
          })
        }
        lstSelect = lstSelect.filter(function (a) {
          var key = a.会社コード + '|' + a.工事依頼No + '|' + a.工事依頼No枝番 + '|' + a.発注サブNo
          if (!this[key]) {
            this[key] = true
            return true
          }
        }, Object.create(null))
        this.http
          .put('/api/Reafs_T/Koji/TD00100/Update電子応諾', lstSelect, 'アクセスしています...')
          .then(async response => {
            this.loading = false
            // if (response.status) {
            //   await this.print(lstSelect)
            // }
            if(!response.status)
            {
              this.showError(response.message)
            }else{
              let msgComplete = (await this.getMsg('M000012')).data
              this.showSuccess(msgComplete)
            }
            
            this.onSearch()
          })
      }
      // ハノイ側修正2022/08/24 STEP2_T 課題管理表№8：工事一覧「2022/08/19仕様変更分」処理概要：電子応諾ボタン押下時の処理を修正...END
      this.loading = false
    },
    async onSagyoYoteiKoshin () {
      this.loading = true
      for (let i = 0; i < this.dtMeisaiKomoku.length; i++) {
        this.dtMeisaiKomoku[i].作業予定開始日 = this.dtMeisaiKomoku[i].作業予定開始日 == null ? '':this.dtMeisaiKomoku[i].作業予定開始日
        this.dtMeisaiKomoku[i].変更前作業予定開始日 = this.dtMeisaiKomoku[i].変更前作業予定開始日 == null ? '':this.dtMeisaiKomoku[i].変更前作業予定開始日
        this.dtMeisaiKomoku[i].作業予定終了日 = this.dtMeisaiKomoku[i].作業予定終了日 == null ? '':this.dtMeisaiKomoku[i].作業予定終了日
        this.dtMeisaiKomoku[i].変更前作業予定終了日 = this.dtMeisaiKomoku[i].変更前作業予定終了日 == null ? '':this.dtMeisaiKomoku[i].変更前作業予定終了日

        if(this.dtMeisaiKomoku[i].作業予定開始日 != this.dtMeisaiKomoku[i].変更前作業予定開始日 || this.dtMeisaiKomoku[i].作業予定終了日 != this.dtMeisaiKomoku[i].変更前作業予定終了日){
            this.henkonFlag[i] = 1
        }else{
            this.henkonFlag[i] = 0
        }

        if(this.henkonFlag[i] === 1 ){
          if (this.dtMeisaiKomoku[i].作業予定開始日 === '' || this.dtMeisaiKomoku[i].作業予定開始日 === null ||
          moment(this.dtMeisaiKomoku[i].作業予定開始日).isValid() === false) {
            this.getMsg('E040278').then((response) => {
              this.showError(response.data)
              this.$refs['refTbl作業予定開始日' + i].focus()
            })
            return false
          }
          if (this.dtMeisaiKomoku[i].作業予定終了日 === '' || this.dtMeisaiKomoku[i].作業予定終了日 === null ||
          moment(this.dtMeisaiKomoku[i].作業予定終了日).isValid() === false) {
            this.getMsg('E040278').then((response) => {
              this.showError(response.data)
              this.$refs['refTbl作業予定終了日' + i].focus()
            })
            return false
          }
          if (this.dtMeisaiKomoku[i].作業予定開始日 > this.dtMeisaiKomoku[i].作業予定終了日) {
            this.getMsg('E040279').then((response) => {
              this.showError(response.data)
              this.$refs['refTbl作業予定終了日' + i].focus()
            })
            return false
          }
        }else{

        }
        
      }
      let arrData = []
      let msgConfirm = (await this.getMsg('K000005')).data
      if (!await this.confirm(msgConfirm)) {
        return false
      }
      let userLogIn = this.$store.getters.getUserInfo()
      for (let i = 0; i < this.dtMeisaiKomoku.length; i++) {
        let valCheck = this.dtMeisaiKomoku[i]
        let dataFilter = MeisaiKomokuListSearch.filter(function (val, idx, arr) {
          return valCheck.rowKeyMeisaiKomoku === val.rowKeyMeisaiKomoku
        })
        Object.keys(valCheck).forEach(function (key) {
          if (valCheck[key] !== dataFilter[0][key]) {
            if (!arrData.some((val) => val.rowKeyMeisaiKomoku === valCheck.rowKeyMeisaiKomoku)) {
              let itemUpdate = {
                rowKeyMeisaiKomoku: valCheck.rowKeyMeisaiKomoku,
                会社コード: userLogIn.会社コード,
                工事依頼NO: valCheck.工事依頼No.replace('-', ''),
                工事依頼No枝番: valCheck.工事依頼No枝番,
                業者コード: valCheck.業者コード,
                業者コード枝番: valCheck.業者コード枝番,
                発注サブNo: valCheck.発注サブNo,
                作業予定開始日: valCheck.作業予定開始日,
                作業予定終了日: valCheck.作業予定終了日,
                発注工期ＳＴ: valCheck.発注工期ＳＴ,
                発注工期ＥＤ: valCheck.発注工期ＥＤ,
                検収回数: valCheck.検収回数,
                UPDATE_HOST: '',
                UPDATE_ID: '',
                F044_VERSION: valCheck.F044_VERSION,
                F052_VERSION: valCheck.F052_VERSION,
                発注確定フラグ: valCheck.発注確定フラグ
              }
              arrData.push(itemUpdate)
            }
          }
        })
      }
      let kojiIchiranInfo = {
        lstSagyoHokokuMeisaiMasuta: arrData
      }
      this.http
        .put('/api/Reafs_T/Koji/TD00100/Update', kojiIchiranInfo, 'アクセスしています...')
        .then(async response => {
          this.loading = false
          if (response.status) {

            for(let i = 0; i < response.data.length; i++){
                let resultMail
                resultMail = this.commonFunctionUI.CM00110SendMail(response.data[i])
            }

            let msgComplete = (await this.getMsg('M000012')).data
            this.showSuccess(msgComplete)
            this.onSearch()
          }else {
            this.showError(response.message)
          }
        })
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    cellDataChange (row, column, val) {
      this.$refs.dtMeisaiKomoku.cellDataChange(row, column, val)
    },
    onBlurDatePicker (row, column, val) {
      if (row.作業予定開始日 !== null && row.作業予定開始日 !== '') {
        if (row.作業予定終了日 === '' || row.作業予定終了日 === null) {
          row.作業予定終了日 = row.作業予定開始日
        }
      }
    },
    onBlurBukkenKodo () {
      if (this.kojiIchiranForm.物件コード === '' || this.kojiIchiranForm.物件コード.trim() === '') {
        this.kojiIchiranForm.物件名 = ''
      } else {
        this.http
          .get('/api/Reafs_T/Koji/TD00100/Get物件名', { 物件コード: this.kojiIchiranForm.物件コード }, 'アクセスしています...')
          .then((response) => {
            if (response.data !== null) {
              if (response.data !== null) {
                this.kojiIchiranForm.物件名 = response.data.物件名
              } else {
                this.kojiIchiranForm.物件名 = ''
                this.getMsg('E040010').then((response) => {
                  this.showError(response.data.物件名, {callback: () => {
                    this.$refs.ref物件コード.focus()
                  }})
                })
                return false
              }
            } else {
              this.kojiIchiranForm.物件名 = ''
            }
          })
      }
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
</style>
<style>
.KojiIchiran .ve-table .ve-table-container table.ve-table-content {
  top: -1px !important;
}
.ve-table-header-tr{
  height: 30px !important;
}
</style>
