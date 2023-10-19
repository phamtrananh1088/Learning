<template>
  <div class="home-container MishoninShoninIchiran">
    <el-form :model="pageData" ref="refForm" label-width="100px" class="sample-form-class MishoninShoninIchiranForm"
      :hide-required-asterisk="true">
      <el-container class="home-el-container">
        <!-- <el-header height="auto" class="home-el-header">
        </el-header> -->
        <el-main class="home-el-main">
          <!-- 1 -->
          <el-row style="display: flex; align-items: center; margin-bottom: 14px">
            <el-col :span="8">
                <el-form-item label="承認種類" prop="承認種類" class="is-required">
                <el-select
                  required
                  v-model="pageData.承認種類"
                  ref="ref承認種類"
                  @change="onChange承認種類()"
                >
                  <el-option
                    v-for="item in 承認種類List"
                    :key="item.名称コード"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-row style="display: flex; align-items: center;">
                <el-col :span="13">
                  <el-form-item label="対象" prop="対象" class="is-required">
                    <el-radio-group v-model="pageData.対象">
                      <el-radio
                      v-for="item in 対象List"
                      :key="item.name"
                      :label="item.value"
                      >
                      {{ item.name }}
                      </el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="10" style="width: 39%;">
                   <el-radio-group v-model="pageData.対象_履歴">
                    <label v-if="対象_履歴List && 対象_履歴List.length > 0" style="margin: 0px 5px 0px 14px;" role="radio" class="el-radio">
                      <span class="el-radio__label">履歴/</span>
                    </label>
                    <el-radio
                      v-for="item in 対象_履歴List"
                      :key="item.name"
                      :label="item.value"
                    >
                      {{ item.name }}
                    </el-radio>
                  </el-radio-group>
                </el-col>
                <el-col :span="1">
                  <el-button type="primary" size="mini" ref="ref検索"
                 @click="onSearch(false)">検索</el-button>
                 </el-col>
                 </el-row>
                </el-col>
          </el-row>
          <!-- 2 -->
          <el-row>
            <el-col :span="8">
              <el-form-item v-if="visible.営業店" label="営業所" prop="営業店コード">
                  <eigyoten
                    :eigyotenCd="pageData.店コード"
                    @update:eigyotenCd="(v)=> { this.pageData.店コード = v }"
                    :appendlabelText="pageData.営業店名"
                    @backData="reafsmodalBackEigyoten"
                    ref=ref営業店
                    :appendlabelWidth="'198px'"
                    :modeTextBox="2"
                    inputClass="button-multi-code"
                  />
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item v-if="visible.申請日1" label="申請日" prop="申請日">
                <div style="display: flex">
                  <el-date-picker v-model="pageData.申請日_開始日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker" ref="ref申請日_開始日" />
                  <span style="margin: 0 2px">～</span>
                  <el-date-picker v-model="pageData.申請日_終了日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker" @change="onChangeShinseiBi" ref="ref申請日_終了日" />
                </div>
              </el-form-item>
              <el-form-item label="申請者" prop="申請者" v-if="visible.申請者">
                 <opensearch
                  v-model="pageData.申請者コード"
                  :appendlabelText="pageData.申請者名"
                  :append="'append'"
                  :maxlength="'7'"
                  :path="'reafsmodalSearchShain'"
                  @backData="reafsmodalSearchShainBack"
                  ref="ref申請者"
                  :appendlabelWidth="'214px'"
                  class="btn-icon"
                  :customValidate= 2
                />
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="工事区分" prop="工事区分" v-if="visible.工事区分">
                <el-select
                  v-model="pageData.工事区分"
                  ref="ref工事区分"
                >
                  <el-option
                    v-for="item in 工事区分List"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
                </el-form-item>
            </el-col>
          </el-row>
          <!-- 3 -->
          <el-row>
            <el-col :span="8">
              <el-form-item label="物件名" prop="物件名" v-if="visible.物件名">
                 <reafsinputtext maxlength="40" v-model="pageData.物件名" ref="ref物件名"
                  width="306px">
                </reafsinputtext>
              </el-form-item>
              <el-form-item :label="pageData.承認種類 == ApprovalType.支払承認 ? '支払先' : '手配先'" prop="手配先" v-if="visible.手配先">
                <opensearch
                  v-model="pageData.手配先"
                  :appendlabelText="pageData.手配先名"
                  :append="'append'"
                  :maxlength="'9'"
                  :errorMessageCode="pageData.承認種類 == ApprovalType.支払承認 ? 'E040695' : 'E040756'"
                  :path="'reafsmodalSearchTorihikisaki'"
                  :subParamsObj="subParamsObj"
                  @backData="reafsmodalSearchTorihikisakiBack"
                  ref="ref手配先"
                  class="openseach-txt"
                  :customValidate= 2
                >
                </opensearch>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="工事件名" prop="工事件名" v-if="visible.工事件名">
                <reafsinputtext maxlength="40" v-model="pageData.工事件名" ref="ref工事件名"
                  width="306px">
                </reafsinputtext>
              </el-form-item>
              <el-form-item v-if="visible.申請日2" label="申請日" prop="申請日">
                <div style="display: flex">
                  <el-date-picker v-model="pageData.申請日_開始日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker" ref="ref申請日_開始日" />
                  <span style="margin: 0 2px">～</span>
                  <el-date-picker v-model="pageData.申請日_終了日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker" @change="onChangeShinseiBi" ref="ref申請日_終了日" />
                </div>
              </el-form-item>
               <el-form-item label="依頼内容" prop="依頼内容" v-if="visible.依頼内容">
                <reafsinputtext maxlength="40" v-model="pageData.依頼内容" ref="ref依頼内容"
                  width="306px">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="物件区分" prop="物件区分" v-if="visible.物件区分">
                  <el-select
                    v-model="pageData.物件区分"
                    ref="ref物件区分"
                  >
                    <el-option
                      v-for="item in 物件区分List"
                      :key="item.名称コード"
                      :label="item.名称コード_名称"
                      :value="item.名称コード"
                    >
                    </el-option>
                  </el-select>
              </el-form-item>
              <el-form-item v-if="visible.工事完了日" label="工事完了日" prop="工事完了日">
                <div style="display: flex">
                  <el-date-picker v-model="pageData.工事完了日_開始日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker custom-date-picker" ref="ref工事完了日_開始日" />
                  <span style="margin: 0 2px">～</span>
                  <el-date-picker v-model="pageData.工事完了日_終了日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker custom-date-picker" @change="onChangeKojiKanryoBiShuryoBi" ref="ref工事完了日_終了日" />
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label=" 請求内容" prop="請求内容" v-if="visible.請求内容">
                <reafsinputtext maxlength="40" v-model="pageData.請求内容" ref="ref依頼内容"
                  width="306px">
                </reafsinputtext>
              </el-form-item>
              <el-form-item label="承認者" prop="承認者" v-if="visible.承認者">
                 <opensearch
                  v-model="pageData.承認者コード"
                  :appendlabelText="pageData.承認者名"
                  :append="'append'"
                  :maxlength="'7'"
                  :path="'reafsmodalSearchShoninSha'"
                  @backData="reafsmodalSearchShoninShaBack"
                  ref="ref承認者"
                  :appendlabelWidth="'214px'"
                  class="openseach-txt"
                  :customValidate= 2
                />
              </el-form-item>
            </el-col>
            <el-col :span="9">
             <el-form-item label="申請部署" prop="申請部署" v-if="visible.申請部署">
                <shinseiBusho
                    :事務所コード="pageData.事務所コード"
                    @update:事務所コード="(v)=> { this.pageData.事務所コード = v }"
                    :営業所コード="pageData.営業所コード"
                    @update:営業所コード="(v)=> { this.pageData.営業所コード = v }"
                    :部コード="pageData.部コード"
                    @update:部コード="(v)=> { this.pageData.部コード = v }"
                    :課コード="pageData.課コード"
                    @update:課コード="(v)=> { this.pageData.課コード = v }"
                    :係コード="pageData.係コード"
                    @update:係コード="(v)=> { this.pageData.係コード = v }"
                    :appendlabelText="pageData.申請部署名"
                    @backData="reafsmodalBackShinseiBusho"
                    ref=ref申請部署
                  />
              </el-form-item>
              <el-form-item label="修繕／定期" prop="修繕_定期" v-if="visible.修繕_定期">
                <el-radio-group v-model="pageData.修繕_定期" style="display:contents !important;">
                  <el-radio label="全て" style="margin-right: 25px; padding-left:5px; align-self: center;">全て</el-radio>
                  <el-radio label="修繕" style="align-self: center;">修繕</el-radio>
                  <el-radio label="定期" style="align-self: center;">定期</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="7">
             <el-form-item label="不備" prop="不備" v-if="visible.不備">
                 <el-checkbox
                  label="有り"
                  v-model="pageData.有り"
                  style="margin-left: 5px"
                ></el-checkbox>
                 <el-checkbox
                  label="無し"
                  v-model="pageData.無し"
                ></el-checkbox>
              </el-form-item>
              <el-form-item label="（工事）依頼№" prop="工事依頼No">
                <span slot="label">{{dsp工事依頼No}}</span>
                <reafsinputtext maxlength="7" v-model="pageData.工事依頼No" ref="ref工事依頼No"
                  width="108px">
                </reafsinputtext>
              </el-form-item>
             </el-col>
          </el-row>
          <el-row  style="margin-bottom: 14px">
            <el-col :span="8">
             <el-form-item label="部門確認" prop="部門確認" v-if="visible.部門確認">
                 <el-checkbox
                  label="確認済"
                  v-model="pageData.確認済"
                  style="margin-left: 5px"
                ></el-checkbox>
                 <el-checkbox
                  label="未確認"
                  v-model="pageData.未確認"
                ></el-checkbox>
              </el-form-item>
             </el-col>
            <el-col :span="9">
             <el-form-item label="按分" prop="按分" v-if="visible.按分">
                 <el-checkbox
                  label="有り"
                  v-model="pageData.按分有り"
                  style="margin-left: 5px"
                ></el-checkbox>
                 <el-checkbox
                  label="無し"
                  v-model="pageData.按分無し"
                ></el-checkbox>
              </el-form-item>
             </el-col>
             <el-col :span="7">
             <el-form-item id ="業者状態" label="業者状態" prop="業者状態" v-if="visible.業者状態">
                 <el-checkbox
                  label="請求合意待ち"
                  v-model="pageData.請求合意待ち"
                ></el-checkbox>
                 <el-checkbox
                  label="請求合意済"
                  v-model="pageData.請求合意済"
                ></el-checkbox>
                <el-checkbox
                  label="請求合意不要"
                  v-model="pageData.請求合意不要"
                ></el-checkbox>
              </el-form-item>
             </el-col>
          </el-row>
          <el-row>
            <reafstable ref="table" :tableData="tableData" :columns="columnsFilter" :maxHeight="581" :resizeAble="true"
              :cellStyleOption="cellStyleOption" rowKeyFieldName="key" :class="tableClassFilter" :sortOption="sortOption" :HiddenColumnKeys="hiddenColumnMishoninShonin">
            </reafstable>
            <screenPopup
                  src="/Reafs_W/TyohyoIchiran"
                  :params="paramsTyohyoIchiran"
                  :dialogVisible.sync="showDialogTyohyoIchiran"></screenPopup>
          </el-row>
          <el-row style="padding-top: 10px;">
            <el-col :span="12" style="width:57.59%" v-show="isShowButton">
              <el-form-item class="is-area hininSashimodoshiJiyu" prop="否認差戻事由"
               label-width="0px"
               >
                <div style="display: flex">
                  <label
                    class="el-form-item__label item_label_HininSashimodoshiJiyu"
                    >否認・差戻事由</label
                  >
                  <el-input
                    class="item_input_HininSashimodoshiJiyu"
                    maxlength="100"
                    type="textarea"
                    resize="none"
                    :rows="2"
                    v-model="否認差戻事由"
                    ref="ref否認差戻事由"
                  >
                  </el-input>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="5">
             <el-form-item label="確認金額計" prop="確認金額計" v-if="visible.確認金額計">
              <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="確認金額計"
                    :width="'133px'"
                    class="is-readonly"
                    disabled="disabled"
                  />
             </el-form-item>
            </el-col>
            <el-col :span="5">
              <div>&nbsp;</div>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="24" style="display: flex; justify-content: end; gap: 30px;">
              <el-button v-show="isShowButtonBumonKakunin" :disabled="disable部門確認" @click="onBumonKakunin" style="float: right">部門確認</el-button>
              <el-button v-show="isShowButton" :disabled="disable差戻" @click="onSashimodoshi" style="float: right">差戻</el-button>
              <el-button v-show="isShowButton" :disabled="disable否認" @click="onHinin" style="float: right">否認</el-button>
              <el-button v-show="isShowButton" :disabled="disable承認" @click="onShonin" style="float: right">承認</el-button>
              <el-button @click="onBack">戻る
              </el-button>
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
import eigyoten from '@/components/basic/EigyoTen.vue'
import shinseiBusho from '@/components/basic/ShinseiBusho.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import opensearch from '@/components/basic/openSearch.vue'
import moment from 'moment'
import screenPopup from '../../common/ScreenPopup/ScreenPopup.vue'

export default {
  async activated () {
    if (this.$store.getters.getKeepAliveFlag() == 0 && this.isMounted == false) {
      this.$store.commit('setKeepAliveFlag', 1)
      await this.initOnMounted()
      document.querySelector('.ve-table-container').scrollTop = this.scrollTop
    }
    this.isMounted = false
  },
  components: {
    reafsinputtext,
    reafstable,
    eigyoten,
    opensearch,
    moment,
    shinseiBusho,
    reafsinputnumber,
    screenPopup
  },
  data () {
    return {
      isMounted: false,
      scrollTop: 0,
      isScreenTop: false, // ハノイ側修正2023/07/31　STEP2_W　課題管理表№373：設計書「修正履歴」シートの「2023/07/28仕様変更分」「別画面から遷移時専用の処理を追記」
      params: {
        承認種類: ''
      },
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      dsp工事依頼No: '',
      pageData: this.defaultPagedata(),
      否認差戻事由: '',
      確認金額計: '0',
      searchedParam: this.defaultPagedata(),
      tableData: [],
      selectedHatchuShoninRowKeys: [],
      selectedBumonKakuninRowKeys: [],
      selectedShiharaiShoninRowKeys: [],
      承認種類List: [],
      工事区分List: [],
      物件区分List: [],
      税区List: [],
      showDialogTyohyoIchiran: false,
      paramsTyohyoIchiran: {
        工事依頼No: '',
        // ハノイ側修正2023/05/23　STEP2_W　課題管理表№340：設計書「修正履歴」シートの「2023/05/22仕様変更分」「工事区分の頭1桁='4'（定期案件）の場合の、「WD00130 帳票一覧」画面起動引数を修正。」 START
        契約No: '',
        契約履歴No: '',
        見積明細No: '',
        契約年月: '',
        予定年月: ''
        // ハノイ側修正2023/05/23　STEP2_W　課題管理表№340：設計書「修正履歴」シートの「2023/05/22仕様変更分」「工事区分の頭1桁='4'（定期案件）の場合の、「WD00130 帳票一覧」画面起動引数を修正。」 END
      },
      ApprovalType: Object.freeze({
        '依頼承認': 10,
        // '依頼承認_定期': 13,
        '見積承認_実施判断': 32,
        '稟議申請': 42,
        '発注承認': 41,
        '工事完了確認': 52,
        '支払承認': 61
      }),
      cellStyleOption: {
        headerCellClass: ({ row, column, rowIndex }) => {
        },
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = ''
          if (
            ['№', '帳票', '工事件名', '依頼内容／不具合状況', '請求内容', '工事件名／依頼内容／不具合状況'].includes(column.key)
          ) {
            css += ' position-relative'
          }

          const textLeft = ['工事依頼№', '工事区分', '営業店', '物件名', '物件区分',
            '工事件名', '依頼内容／不具合状況', '工事件名／依頼内容／不具合状況', '承認者',
            '手配先', '申請部署', '費用負担部門', '事業領域', '外内販区分', '支払先', '申請者']
          if (textLeft.includes(column.key)) {
            css += ' text-left'
          }

          const textRight = ['見積金額', '発注金額', '金額']
          if (textRight.includes(column.key)) {
            css += ' text-right'
          }

          return css
        }
      },
      columns: [
        {
          field: '№',
          key: '№',
          title: '№',
          width: 50,
          fixed: '',
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex" class="cell-body-custome">
                <span style="height: 100%;width: 100%;line-height: 26px;">
                  {rowIndex + 1}
                </span>
              </div>
            )
          }
        },
        {
          field: '',
          key: '選択欄',
          title: '選択欄',
          // type: 'checkbox',
          align: 'center',
          vAlign: 'center',
          width: 40,
          fixed: '',
          renderHeaderCell: ({ column }) => {
            return (
              <el-checkbox
                label=""
                indeterminate= {(this.tableData.length - this.countDisableSelectedHatchuShonin) > this.selectedHatchuShoninRowKeys.length && this.selectedHatchuShoninRowKeys.length > 0 }
                value={ this.tableData.length > 0 && (this.tableData.length - this.countDisableSelectedHatchuShonin) == this.selectedHatchuShoninRowKeys.length && this.selectedHatchuShoninRowKeys.length > 0 }
                on-change = {(checked) => { this.handleCheckAllHatchuShonin(checked) }}
              ></el-checkbox>
            )
          },
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <el-checkbox
                label=""
                disabled={!this.checkBoxHatchuShoninState(row)}
                value={this.selectedHatchuShoninRowKeys.includes(row.key)}
                on-change = {(checked) => { this.handleCheckHatchuShonin(checked, row) }}
                class={this.checkBoxHatchuShoninState(row) ? '' : 'checkBoxDisabled'}
              ></el-checkbox>
            )
          }
        },
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 Start
        {
          field: '',
          key: '部門確認CB',
          title: '部門確認',
          // type: 'checkbox',
          align: 'center',
          vAlign: 'center',
          width: 40,
          fixed: '',
          renderHeaderCell: ({ column }) => {
            return (
              <div>
                部門確認
              <br/>
              <el-checkbox
                label=""
                indeterminate= {(this.tableData.length - this.countDisableSelectedBumonKakunin) > this.selectedBumonKakuninRowKeys.length && this.selectedBumonKakuninRowKeys.length > 0 }
                value={ this.tableData.length > 0 && (this.tableData.length - this.countDisableSelectedBumonKakunin) == this.selectedBumonKakuninRowKeys.length && this.selectedBumonKakuninRowKeys.length > 0 }
                on-change = {(checked) => { this.handleCheckAllBumonKakunin(checked) }}
              ></el-checkbox>
              </div>
            )
          },
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <el-checkbox
                label=""
                disabled={!this.checkBoxBumonKakuninState(row)}
                value={this.selectedBumonKakuninRowKeys.includes(row.key)}
                on-change = {(checked) => { this.handleCheckBumonKakunin(checked, row) }}
                class={this.checkBoxBumonKakuninState(row) ? '' : 'checkBoxDisabled'}
              ></el-checkbox>
            )
          }
        },
        {
          field: '',
          key: '支払承認',
          title: '支払承認',
          // type: 'checkbox',
          align: 'center',
          vAlign: 'center',
          width: 40,
          fixed: '',
          renderHeaderCell: ({ column }) => {
            return (
              <div>
                支払承認
              <br/>
              <el-checkbox
                label=""
                indeterminate= {(this.tableData.length - this.countDisableSelectedShiharaiShonin) > this.selectedShiharaiShoninRowKeys.length && this.selectedShiharaiShoninRowKeys.length > 0 }
                value={ this.tableData.length > 0 && (this.tableData.length - this.countDisableSelectedShiharaiShonin) == this.selectedShiharaiShoninRowKeys.length && this.selectedShiharaiShoninRowKeys.length > 0 }
                on-change = {(checked) => { this.handleCheckAllShiharaiShonin(checked) }}
              ></el-checkbox>
              </div>
            )
          },
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <el-checkbox
                label=""
                disabled={!this.checkBoxShiharaiShoninState(row)}
                value={this.selectedShiharaiShoninRowKeys.includes(row.key)}
                on-change = {(checked) => { this.handleCheckShiharaiShonin(checked, row) }}
                class={this.checkBoxShiharaiShoninState(row) ? '' : 'checkBoxDisabled'}
              ></el-checkbox>
            )
          }
        },
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 End
        {
          key: '帳票',
          title: '帳票',
          field: '帳票',
          align: 'center',
          vAlign: 'center',
          width: 42,
          fixed: '',
          renderBodyCell: ({ row }) => {
            return (
              <div style="display: grid; padding-left: 6px" class="cell-body-custome">
                <a style="margin-top: 4px" title={row['帳票']} class="ellipsis" on-click={() => { if (row['帳票']) this.handleLinkClick(row) }} href="javascript:;">{row['帳票']}</a>
              </div>
            )
          }
        },
        {
          key: '工事依頼№',
          title: '工事依頼№',
          field: '工事依頼NO',
          width: 110,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '申請日',
          title: '申請日',
          field: '申請日',
          width: 90,
          align: 'center',
          sortBy: ''
        },
        {
          key: '工事区分',
          title: '工事区分',
          field: '工事区分',
          width: 90,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '営業店',
          title: '営業所',
          field: '営業店',
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '物件名',
          title: '物件名',
          field: '物件名',
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '物件区分',
          title: '物件区分',
          field: '物件区分',
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '工事件名',
          title: '工事件名',
          field: '工事件名',
          renderBodyCell: ({ row }) => {
            return (
              <div style="display: grid; padding-left: 5px" class="cell-body-custome">
                <a style="margin-top: 4px" title={row['工事件名']} class="ellipsis" on-click={() => { if (row['工事件名']) this.handleLinkClick(row, '1') }} href="javascript:;">{row['工事件名']}</a>
              </div>
            )
          },
          sortBy: ''
        },
        {
          key: '発注金額',
          title: '発注金額',
          field: '発注金額',
          width: 130,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '金額',
          title: '金額',
          field: '金額',
          width: 130,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '依頼内容／不具合状況',
          title: '依頼内容／不具合状況',
          field: '依頼内容_不具合状況',
          renderBodyCell: ({ row }) => {
            return (
              <div style="display: grid; padding-left: 5px" class="cell-body-custome">
                <a style="margin-top: 4px" title={row['依頼内容_不具合状況']} class="ellipsis" on-click={() => { if (row['依頼内容_不具合状況']) this.handleLinkClick(row) }} href="javascript:;">{row['依頼内容_不具合状況']}</a>
              </div>
            )
          },
          sortBy: ''
        },
        {
          key: '工事件名／依頼内容／不具合状況',
          title: '工事件名／依頼内容／不具合状況',
          field: '工事件名_依頼内容_不具合状況',
          renderBodyCell: ({ row }) => {
            return (
              <div style="display: grid; padding-left: 5px" class="cell-body-custome">
                <a style="margin-top: 4px" title={row['工事件名_依頼内容_不具合状況']} class="ellipsis" on-click={() => { if (row['工事件名_依頼内容_不具合状況']) this.handleLinkClick(row) }} href="javascript:;">{row['工事件名_依頼内容_不具合状況']}</a>
              </div>
            )
          },
          sortBy: ''
        },
        {
          key: '見積金額',
          title: '見積金額',
          field: '見積金額',
          width: 130,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '手配先',
          title: '手配先',
          field: '手配先',
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '工事完了日',
          title: '工事完了日',
          field: '工事完了日',
          width: 110,
          align: 'center',
          sortBy: ''
        },
        {
          key: 'ステータス',
          title: 'ステータス',
          field: 'ステータス',
          width: 120,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 Start
        {
          key: '部門確認',
          title: '部門確認',
          field: '部門確認',
          width: 120,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 End
        {
          key: '業者状態',
          title: '業者状態',
          field: '業者状態',
          width: 120,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        // {
        //   key: '経理状態',
        //   title: '経理状態',
        //   field: '経理状態',
        //   width: 120,
        //   ellipsis: {
        //     showTitle: true
        //   },
        //   sortBy: ''
        // },
        {
          key: '日付',
          title: '日付',
          field: '日付',
          width: 90,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '承認者',
          title: '承認者',
          field: '承認者',
          width: 90,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '請求内容',
          title: '請求内容',
          field: '請求内容',
          width: 210,
          renderBodyCell: ({ row }) => {
            return (
              <div style="display: grid; padding-left: 5px" class="cell-body-custome">
                <a style="margin-top: 4px" title={row['請求内容']} class="ellipsis" on-click={() => { if (row['請求内容']) this.handleLinkClick(row, '1') }} href="javascript:;">{row['請求内容']}</a>
              </div>
            )
          },
          sortBy: ''
        },
        {
          key: '支払先',
          title: '支払先',
          field: '支払先',
          width: 140,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '申請者',
          title: '申請者',
          field: '申請者',
          width: 120,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '申請部署',
          title: '申請部署',
          field: '申請部署',
          width: 190,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '費用負担部門',
          title: '費用負担部門',
          field: '費用負担部門',
          width: 190,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '事業領域',
          title: '事業領域',
          field: '事業領域',
          width: 100,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '外内販区分',
          title: '外内販区分',
          field: '外内販区分',
          width: 106,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: '按分',
          title: '按分',
          field: '按分',
          width: 66,
          // ellipsis: {
          //   showTitle: true
          // },
          renderBodyCell: ({ row }) => {
            return (
              <div class={row['按分'] === '有' ? 'red' : 'black'}>
                <span title={row['按分']} >{row['按分']}</span>
              </div>
            )
          },
          sortBy: ''
        },
        {
          key: '不備有無',
          title: '不備有無',
          field: '不備有無',
          width: 95,
          ellipsis: {
            showTitle: true
          },
          sortBy: ''
        },
        {
          key: 'FW042承認事務所コード',
          title: 'FW042承認事務所コード',
          field: 'FW042承認事務所コード',
          sortBy: ''
        },
        {
          key: 'FW042承認営業所コード',
          title: 'FW042承認営業所コード',
          field: 'FW042承認営業所コード',
          sortBy: ''
        },
        {
          key: 'FW042承認部コード',
          title: 'FW042承認部コード',
          field: 'FW042承認部コード',
          sortBy: ''
        },
        {
          key: 'FW042承認課コード',
          title: 'FW042承認課コード',
          field: 'FW042承認課コード',
          sortBy: ''
        },
        {
          key: 'FW042承認係コード',
          title: 'FW042承認係コード',
          field: 'FW042承認係コード',
          sortBy: ''
        },
        {
          key: 'FW042申請者事務所コード',
          title: 'FW042申請者事務所コード',
          field: 'FW042申請者事務所コード',
          sortBy: ''
        },
        {
          key: 'FW042申請者営業所コード',
          title: 'FW042申請者営業所コード',
          field: 'FW042申請者営業所コード',
          sortBy: ''
        },
        {
          key: 'FW042申請者部コード',
          title: 'FW042申請者部コード',
          field: 'FW042申請者部コード',
          sortBy: ''
        },
        {
          key: 'FW042申請者課コード',
          title: 'FW042申請者課コード',
          field: 'FW042申請者課コード',
          sortBy: ''
        },
        {
          key: 'FW042申請者係コード',
          title: 'FW042申請者係コード',
          field: 'FW042申請者係コード',
          sortBy: ''
        }
      ],
      visible: {
        営業店: false,
        申請日1: false,
        申請日2: false,
        工事区分: false,
        物件区分: false,
        物件名: false,
        工事件名: false,
        依頼内容: false,
        請求内容: false,
        手配先: false,
        支払先名: false,
        工事完了日: false,
        申請部署: false,
        不備: false,
        申請者: false,
        承認者: false,
        修繕_定期: false,
        // ハノイ側修正2022/11/23　STEP2_W　課題管理表№223：設計書「修正履歴」シートの「2022/11/21仕様変更分」「検索項目に「按分」「業者状態」追加, 検索項目指定時の条件追加、修正」Start
        按分: false,
        業者状態: false,
        // ハノイ側修正2022/11/23　STEP2_W　課題管理表№223：設計書「修正履歴」シートの「2022/11/21仕様変更分」「検索項目に「按分」「業者状態」追加, 検索項目指定時の条件追加、修正」End
        部門確認: false // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」
      },
      sortOption: {
        sortAlways: true,
        sortChange: (params) => {
          if (!this.tableData || this.tableData.length <= 0) {
            return
          }
          let sortProp = ''
          let sortDirect = ''
          for (let prop in params) {
            if (params[prop]) {
              sortProp = prop
              sortDirect = params[prop]
            }
          }

          this.tableData.sort((a, b) => {
            let aClone = JSON.parse(JSON.stringify(a[sortProp]))
            let bClone = JSON.parse(JSON.stringify(b[sortProp]))
            if (['見積金額', '発注金額', '金額'].includes(sortProp)) {
              aClone = Number(this.getNumber(aClone) || '0')
              bClone = Number(this.getNumber(bClone) || '0')
            }
            if (sortDirect === 'asc') {
              if (!a) {
                return 1
              }
              if (!b) {
                return 1
              }
              return aClone > bClone ? 1 : -1
            } else if (sortDirect === 'desc') {
              if (!a) {
                return 1
              }
              if (!b) {
                return 1
              }
              return bClone > aClone ? 1 : -1
            } else {
              return 0
            }
          })
        }
      },
      hiddenColumnMishoninShonin: [
        'FW042承認事務所コード',
        'FW042承認営業所コード',
        'FW042承認部コード',
        'FW042承認課コード',
        'FW042承認係コード',
        'FW042申請者事務所コード',
        'FW042申請者営業所コード',
        'FW042申請者部コード',
        'FW042申請者課コード',
        'FW042申請者係コード'
      ],
      sessionPageData: null,
      onResearch: false, // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」
      // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」Start
      disable差戻: true,
      disable部門確認: true,
      disable承認: true,
      disable否認: true
      // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」End
    }
  },
  watch: {
    'pageData.承認種類' (newValue) {
      this.showHideItem()
    },
    'pageData.対象' (val) {
      if (val) {
        this.pageData.対象_履歴 = ''
      }
    },
    'pageData.対象_履歴' (val) {
      if (val) {
        this.pageData.対象 = ''
      }
    },
    '対象List' (val) {
      this.setDefault対象()
    },
    '対象_履歴List' (val) {
      this.setDefault対象()
    }
  },
  computed: {
    subParamsObj () {
      return {
        taisyou: '', // 承認種類 取引先検索：対象
        jigyousyoCd: '', // 事業所コード
        torType: this.pageData && this.pageData.承認種類 == this.ApprovalType.支払承認 ? '2' : ''
      }
    },
    // disableSelectedRowKeys () {
    //   if (!this.tableData || this.tableData.length <= 0) {
    //     return []
    //   }
    //   let lstKeyDisabled = []
    //   if (this.searchedParam.承認種類 == this.ApprovalType.支払承認) {
    //     lstKeyDisabled = this.tableData.filter(x => x.ステータス != '支払承認待ち' ||
    //     x.部門確認 != '確認済' ||
    //     (x.業者状態 != '請求合意済' && x.業者状態 != '請求合意不要') ||
    //     !!x.不備有無).map(x => x.key)
    //     // ハノイ側修正2022/10/27　STEP2_W　課題管理表№194：設計書「修正履歴」シートの「2022/10/26仕様変更分」「差戻ボタン押下時の条件追加, 対象「承認待ち」かつ「41:発注承認」の時の条件追加...」START
    //   } else {
    //     const ステータス = this.searchedParam.承認種類 == this.ApprovalType.発注承認
    //       ? '発注承認待ち' : this.searchedParam.承認種類 == this.ApprovalType.支払承認 ? '' : ''
    //     if (!ステータス) {
    //       return []
    //     }
    //     lstKeyDisabled = this.tableData.filter(x => x.ステータス != ステータス).map(x => x.key)
    //   }
    //   return lstKeyDisabled
    // },
    countDisableSelectedHatchuShonin () {
      let cnt = 0
      if (!this.tableData || this.tableData.length <= 0) {
        return cnt
      }
      cnt = this.tableData.filter(x => !this.checkBoxHatchuShoninState(x)).length
      return cnt
    },
    countDisableSelectedBumonKakunin () {
      let cnt = 0
      if (!this.tableData || this.tableData.length <= 0) {
        return cnt
      }
      cnt = this.tableData.filter(x => !this.checkBoxBumonKakuninState(x)).length
      return cnt
    },
    countDisableSelectedShiharaiShonin () {
      let cnt = 0
      if (!this.tableData || this.tableData.length <= 0) {
        return cnt
      }
      cnt = this.tableData.filter(x => !this.checkBoxShiharaiShoninState(x)).length
      return cnt
    },
    tableClassFilter () {
      let tableRes = 'tblDetail'
      if (this.searchedParam.承認種類 == this.ApprovalType.依頼承認 || !this.searchedParam.承認種類) {
        tableRes += ''
      } else if (this.searchedParam.承認種類 == this.ApprovalType.見積承認_実施判断) {
        tableRes += ''
      } else if (this.searchedParam.承認種類 == this.ApprovalType.稟議申請) {
        tableRes += ''
      } else if (this.searchedParam.承認種類 == this.ApprovalType.発注承認) {
        tableRes += ' hatchuShonin'
      } else if (this.searchedParam.承認種類 == this.ApprovalType.工事完了確認) {
        tableRes += ''
      } else if (this.searchedParam.承認種類 == this.ApprovalType.支払承認) {
        tableRes += ' shiharaiShonin'
      }
      return tableRes
    },
    columnsFilter () {
      if (!this.columns) {
        return []
      }
      const columnRes = []
      var colNo = this.findColumn('№')
      if (this.searchedParam.承認種類 == this.ApprovalType.支払承認) {
        colNo.fixed = 'left'
      } else {
        colNo.fixed = ''
      }
      columnRes.push(colNo)
      if (this.searchedParam.承認種類 == this.ApprovalType.依頼承認 || !this.searchedParam.承認種類) {
        columnRes.push(this.findColumn('工事依頼№'))
        columnRes.push(this.findColumn('申請日'))
        columnRes.push(this.findColumn('工事区分'))
        columnRes.push(this.findColumn('営業店'))
        columnRes.push(this.findColumn('物件名'))
        columnRes.push(this.findColumn('物件区分'))
        columnRes.push(this.findColumn('工事件名／依頼内容／不具合状況'))
        columnRes.push(this.findColumn('ステータス'))
        columnRes.push(this.findColumn('日付'))
        columnRes.push(this.findColumn('承認者'))
      // } else if (this.searchedParam.承認種類 == this.ApprovalType.依頼承認_定期) {
      //   columnRes.push(this.findColumn('工事依頼№'))
      //   columnRes.push(this.findColumn('申請日'))
      //   columnRes.push(this.findColumn('工事区分'))
      //   columnRes.push(this.findColumn('営業店'))
      //   columnRes.push(this.findColumn('物件名'))
      //   columnRes.push(this.findColumn('物件区分'))
      //   columnRes.push(this.findColumn('依頼内容／不具合状況'))
      //   columnRes.push(this.findColumn('ステータス'))
      //   columnRes.push(this.findColumn('日付'))
      //   columnRes.push(this.findColumn('承認者'))
      } else if (this.searchedParam.承認種類 == this.ApprovalType.見積承認_実施判断) {
        columnRes.push(this.findColumn('工事依頼№'))
        columnRes.push(this.findColumn('申請日'))
        columnRes.push(this.findColumn('工事区分'))
        columnRes.push(this.findColumn('営業店'))
        columnRes.push(this.findColumn('物件区分'))
        columnRes.push(this.findColumn('工事件名'))
        columnRes.push(this.findColumn('見積金額'))
        columnRes.push(this.findColumn('手配先'))
        columnRes.push(this.findColumn('ステータス'))
        columnRes.push(this.findColumn('日付'))
        columnRes.push(this.findColumn('承認者'))
      } else if (this.searchedParam.承認種類 == this.ApprovalType.稟議申請) {
        columnRes.push(this.findColumn('工事依頼№'))
        columnRes.push(this.findColumn('申請日'))
        columnRes.push(this.findColumn('工事区分'))
        columnRes.push(this.findColumn('営業店'))
        columnRes.push(this.findColumn('物件区分'))
        columnRes.push(this.findColumn('工事件名'))
        columnRes.push(this.findColumn('見積金額'))
        columnRes.push(this.findColumn('手配先'))
        columnRes.push(this.findColumn('ステータス'))
        columnRes.push(this.findColumn('日付'))
      } else if (this.searchedParam.承認種類 == this.ApprovalType.発注承認) {
        var col選択欄 = this.findColumn('選択欄')
        col選択欄.fixed = ''
        columnRes.push(col選択欄)
        var col帳票 = this.findColumn('帳票')
        col帳票.fixed = ''
        columnRes.push(col帳票)
        columnRes.push(this.findColumn('工事依頼№'))
        columnRes.push(this.findColumn('申請日'))
        columnRes.push(this.findColumn('工事区分'))
        columnRes.push(this.findColumn('営業店'))
        columnRes.push(this.findColumn('工事件名'))
        columnRes.push(this.findColumn('発注金額'))
        columnRes.push(this.findColumn('手配先'))
        columnRes.push(this.findColumn('ステータス'))
        columnRes.push(this.findColumn('日付'))
        columnRes.push(this.findColumn('承認者'))
      } else if (this.searchedParam.承認種類 == this.ApprovalType.工事完了確認) {
        columnRes.push(this.findColumn('工事依頼№'))
        columnRes.push(this.findColumn('申請日'))
        columnRes.push(this.findColumn('工事区分'))
        columnRes.push(this.findColumn('営業店'))
        columnRes.push(this.findColumn('工事件名'))
        columnRes.push(this.findColumn('金額'))
        columnRes.push(this.findColumn('手配先'))
        columnRes.push(this.findColumn('工事完了日'))
        columnRes.push(this.findColumn('ステータス'))
        columnRes.push(this.findColumn('日付'))
        columnRes.push(this.findColumn('承認者'))
      } else if (this.searchedParam.承認種類 == this.ApprovalType.支払承認) {
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 Start
        var col部門確認 = this.findColumn('部門確認CB')
        col部門確認.fixed = 'left'
        columnRes.push(col部門確認)
        var col支払承認 = this.findColumn('支払承認')
        col支払承認.fixed = 'left'
        columnRes.push(col支払承認)
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 Start
        col帳票 = this.findColumn('帳票')
        col帳票.fixed = 'left'
        columnRes.push(col帳票)
        columnRes.push(this.findColumn('工事依頼№'))
        columnRes.push(this.findColumn('申請日'))
        columnRes.push(this.findColumn('工事区分'))
        columnRes.push(this.findColumn('営業店'))
        columnRes.push(this.findColumn('物件区分'))
        columnRes.push(this.findColumn('請求内容'))
        columnRes.push(this.findColumn('金額'))
        columnRes.push(this.findColumn('支払先'))
        columnRes.push(this.findColumn('申請者'))
        columnRes.push(this.findColumn('申請部署'))
        columnRes.push(this.findColumn('費用負担部門'))
        columnRes.push(this.findColumn('事業領域'))
        columnRes.push(this.findColumn('外内販区分'))
        columnRes.push(this.findColumn('按分'))
        columnRes.push(this.findColumn('不備有無'))
        columnRes.push(this.findColumn('ステータス'))
        columnRes.push(this.findColumn('部門確認'))
        columnRes.push(this.findColumn('業者状態'))
        // columnRes.push(this.findColumn('経理状態'))
        columnRes.push(this.findColumn('日付'))
        columnRes.push(this.findColumn('承認者'))
        this.visible.確認金額計 = true
      }
      return columnRes
    },
    isShowButton () {
      return (this.searchedParam.承認種類 == this.ApprovalType.発注承認 || this.searchedParam.承認種類 == this.ApprovalType.支払承認) && this.searchedParam.対象 === '承認待ち'
    },
    isShowButtonBumonKakunin () {
      return this.searchedParam.承認種類 == this.ApprovalType.支払承認 && this.searchedParam.対象 === '承認待ち'
    },
    対象List () {
      if (!this.pageData.承認種類 ||
      this.pageData.承認種類 == this.ApprovalType.依頼承認 ||
      // this.pageData.承認種類 == this.ApprovalType.依頼承認_定期 ||
      // this.pageData.承認種類 == this.ApprovalType.見積承認_実施判断 ||
      this.pageData.承認種類 == this.ApprovalType.発注承認) {
        return [
          {name: '承認待ち', value: '承認待ち'},
          {name: '申請中', value: '申請中'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.見積承認_実施判断) {
        return [
          {name: '承認待ち', value: '承認待ち'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
        return [
          {name: '承認待ち', value: '承認待ち'},
          {name: '申請中', value: '申請中'},
          {name: '差戻', value: '差戻'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.稟議申請) {
        return [
          {name: '稟議申請待ち', value: '稟議申請待ち'},
          {name: '稟議取下／否認', value: '稟議取下／否認'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.工事完了確認) {
        return [{name: '確認待ち', value: '確認待ち'}]
      }
    },
    対象_履歴List () {
      if (!this.pageData.承認種類 ||
      this.pageData.承認種類 == this.ApprovalType.依頼承認 ||
      // this.pageData.承認種類 == this.ApprovalType.依頼承認_定期 ||
      // this.pageData.承認種類 == this.ApprovalType.見積承認_実施判断 ||
      // this.pageData.承認種類 == this.ApprovalType.支払承認 ||
      this.pageData.承認種類 == this.ApprovalType.発注承認) {
        return [
          {name: '承認済', value: '承認済'},
          {name: '否認', value: '否認'},
          {name: '差戻', value: '差戻'},
          {name: '取下', value: '取下'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.見積承認_実施判断) {
        return [
          {name: '承認済', value: '承認済'},
          {name: '否認', value: '否認'},
          {name: '差戻', value: '差戻'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.稟議申請) {
        return [
          {name: '承認済', value: '承認済'},
          {name: '稟議取下／否認', value: '履歴/稟議取下／否認'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.工事完了確認) {
        return [
          {name: '確認済', value: '確認済'},
          {name: '差戻', value: '差戻'}
        ]
      } else if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
        return [
          {name: '承認済', value: '承認済'},
          {name: '差戻', value: '差戻'},
          {name: '取下', value: '取下'},
          {name: '否認', value: '否認'}
        ]
      }
    }
  },
  created () {
    this.showHideItem()
    let userLogIn = this.$store.getters.getUserInfo()
    this.userInfo.ユーザーＩＤ = userLogIn.userId
    this.userInfo.会社コード = userLogIn.会社コード
    this.pageData.承認種類 = this.$route.query.承認種類 ? this.$route.query.承認種類 : this.pageData.承認種類
    this.isMounted = true
  },
  async mounted () {
    await this.GetStartInfo()
    this.initOnMounted()
  },
  methods: {
    async initOnMounted () {
      if (this.$store.getters.getRouterBackFlag() == 1) {
        this.$store.commit('setRouterBackFlag', 0)
        // ハノイ側修正2022/12/08　STEP2_W　課題管理表№240：設計書「修正履歴」シートの「2022/12/07仕様変更分」「他画面から戻ってきた際の再検索処理が常に実行されてしまう。」Start
        if (this.pageData.承認種類) {
          await this.onSearch(this.isScreenTop)
        }
        // ハノイ側修正2022/12/08　STEP2_W　課題管理表№240：設計書「修正履歴」シートの「2022/12/07仕様変更分」「他画面から戻ってきた際の再検索処理が常に実行されてしまう。」End
        return
      }
      this.setDefault対象()
      this.pageData.修繕_定期 = '全て'
      this.dsp工事依頼No = '（工事）依頼№'

      if (this.$route.query.承認種類) {
      // ハノイ側修正2022/08/05　STEP2_W　課題管理表№24：設計書「修正履歴」シートの「2022/08/03仕様変更分」◆TOPページ（WD00010）の件数カウントからの画面遷移時の初期表示条件について　追加 Start
        this.pageData.承認種類 = this.$route.query.承認種類

        let is対象List = this.対象List.find(x => x.value == this.$route.query.対象履歴)
        let is対象履歴List = this.対象_履歴List.find(x => x.value == this.$route.query.対象履歴)
        if (is対象List) {
          this.pageData.対象 = this.$route.query.対象履歴
        } else if (is対象履歴List) {
          this.pageData.対象_履歴 = this.$route.query.対象履歴
        }

        this.pageData.修繕_定期 = this.$route.query.修繕定期
        this.onSearch(true)
      }
    // ハノイ側修正2022/08/05　STEP2_W　課題管理表№24：設計書「修正履歴」シートの「2022/08/03仕様変更分」◆TOPページ（WD00010）の件数カウントからの画面遷移時の初期表示条件について　追加 End
    },
    // selectedRowChange ({ row, isSelected, selectedRowKeys }) {
    //   if (!this.tableData || this.tableData.length <= 0) {
    //     this.確認金額計 = '0'
    //   } else {
    //     const dataTblAll = JSON.parse(JSON.stringify(this.tableData)) || []
    //     // let dataTbl = dataTblAll.filter(x => this.$refs.table.getCheckboxSelected().includes(x.key))
    //     // ハノイ側修正2022/09/28　STEP2_W　課題管理表№127：設計書「修正履歴」シートの「2022/09/28仕様変更分」ペア発注サブ№、ペア検収回数追加 Start
    //     if (row && this.pageData.承認種類 == this.ApprovalType.支払承認) {
    //       const selectedRow = row
    //       const 工事依頼NO = selectedRow.工事依頼NO
    //       const ペア発注サブNO = selectedRow.ペア発注サブNO || 0
    //       if (ペア発注サブNO != 0) {
    //         const lstSelectedRows = dataTblAll.filter(x => x.工事依頼NO == 工事依頼NO && x.ペア発注サブNO == ペア発注サブNO && x.ペア検収回数 == row.ペア検収回数)
    //         for (let index = 0; index < lstSelectedRows.length; index++) {
    //           const element = lstSelectedRows[index]
    //           const rowKeyIndex = selectedRowKeys.indexOf(element.key)
    //           if (rowKeyIndex > -1) {
    //             if (!isSelected) {
    //               // remove selected
    //               selectedRowKeys.splice(rowKeyIndex, 1)
    //             }
    //           } else {
    //             if (isSelected) {
    //               // add selected
    //               selectedRowKeys.push(element.key)
    //             }

    //             if (element.業者状態 == '請求合意待ち') {
    //               selectedRowKeys.splice(rowKeyIndex, 1)
    //             }
    //           }
    //         }
    //         this.$refs.table.setCheckboxSelected(selectedRowKeys)
    //       }
    //     }
    //     // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「№18、№30 不備の判定に解消日を追加」Start
    //     // dataTbl = dataTblAll.filter(x => this.$refs.table.getCheckboxSelected().includes(x.key))
    //     // // ハノイ側修正2022/09/28　STEP2_W　課題管理表№127：設計書「修正履歴」シートの「2022/09/28仕様変更分」ペア発注サブ№、ペア検収回数追加 End
    //     // const sum = dataTbl.map((x) => x.金額)
    //     //   .reduce((a, b) => this.sum(a, b), 0)
    //     // this.確認金額計 = this.base.formatCommaNumeric(sum + '')
    //     // // ハノイ側修正2022/10/27　STEP2_W　課題管理表№194：設計書「修正履歴」シートの「2022/10/26仕様変更分」「差戻ボタン押下時の条件追加, 対象「承認待ち」かつ「41:発注承認」の時の条件追加...」START
    //     this.sum確認金額計()
    //     // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「№18、№30 不備の判定に解消日を追加」End
    //     // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」Start
    //     // if (selectedRowKeys && selectedRowKeys.length == 1) {
    //     //   const rowsl = dataTblAll.find(x => this.$refs.table.getCheckboxSelected().includes(x.key))
    //     //   if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
    //     //     if (rowsl && rowsl.ｸﾞﾙｰﾌﾟ稟議可決番号) {
    //     //       this.disable差戻 = true
    //     //     } else {
    //     //       this.disable差戻 = false
    //     //     }
    //     //   }
    //     //   if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
    //     //     this.disable差戻 = false
    //     //   }
    //     //   this.disable承認 = false
    //     //   this.disable否認 = false
    //     // }
    //     if (this.$refs.table.getCheckboxSelected().length > 0) {
    //       if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
    //         const rowsl = dataTblAll.filter(x => this.$refs.table.getCheckboxSelected().includes(x.key)).some(x => x.ｸﾞﾙｰﾌﾟ稟議可決番号 == '')
    //         if (rowsl) {
    //           this.disable差戻 = false
    //         } else {
    //           this.disable差戻 = true
    //         }
    //       }
    //       if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
    //         this.disable差戻 = false
    //       }
    //       this.disable承認 = false
    //       this.disable否認 = false
    //     } else {
    //       this.disable差戻 = true
    //       this.disable承認 = true
    //       this.disable否認 = true
    //     }
    //     // ハノイ側修正2022/10/27　STEP2_W　課題管理表№194：設計書「修正履歴」シートの「2022/10/26仕様変更分」「差戻ボタン押下時の条件追加, 対象「承認待ち」かつ「41:発注承認」の時の条件追加...」END
    //     // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」End
    //   }
    // },
    sum確認金額計 (checked) {
      if (!this.tableData || this.tableData.length <= 0) {
        this.確認金額計 = '0'
      } else {
        let dataTbl = this.tableData.filter(x => this.selectedShiharaiShoninRowKeys.includes(x.key))
        const sum = dataTbl.map((x) => x.金額)
          .reduce((a, b) => this.sum(a, b), 0)
        this.確認金額計 = this.base.formatCommaNumeric(sum + '')
      }
    },
    handleCheckAllHatchuShonin (checked) {
      if (!checked) {
        this.selectedHatchuShoninRowKeys = []
        this.disable差戻 = true
        this.disable承認 = true
        this.disable否認 = true
      } else {
        const selectedRowKeys1 = []
        this.tableData.map((row) => {
          if (this.checkBoxHatchuShoninState(row)) {
            selectedRowKeys1.push(row.key)
          }
        })
        this.selectedHatchuShoninRowKeys = selectedRowKeys1
        if (this.selectedHatchuShoninRowKeys.length > 0) {
          const rowsl = this.tableData.filter(x => this.selectedHatchuShoninRowKeys.includes(x.key)).some(x => x.ｸﾞﾙｰﾌﾟ稟議可決番号 == '')
          if (rowsl) {
            this.disable差戻 = false
          } else {
            this.disable差戻 = true
          }
          this.disable承認 = false
          this.disable否認 = false
        }
      }
    },
    handleCheckHatchuShonin (checked, row) {
      if (!this.tableData || this.tableData.length <= 0) {
        this.確認金額計 = '0'
      } else {
        const dataTblAll = JSON.parse(JSON.stringify(this.tableData)) || []
        const rowKeyIndex = this.selectedHatchuShoninRowKeys.indexOf(row.key)
        const lstKey = this.tableData.map(x => x.key)
        if (!checked) {
          // remove selected
          this.selectedHatchuShoninRowKeys.splice(rowKeyIndex, 1)
        } else if (checked && this.checkBoxHatchuShoninState(row)) {
          // add selected
          this.selectedHatchuShoninRowKeys.push(row.key)
        }
        this.selectedHatchuShoninRowKeys = this.selectedHatchuShoninRowKeys.filter((v, i, a) => a.indexOf(v) === i)

        if (this.selectedHatchuShoninRowKeys.length > 0) {
          // ハノイ側修正2023/08/08　STEP2_W　課題管理表№383：設計書「修正履歴」シートの「2023/08/08仕様変更分」「承認種類＝「41：発注確認」の場合の差戻ボタン押下可能条件を修正。」START
          const rowsl = dataTblAll.filter(x => this.selectedHatchuShoninRowKeys.includes(x.key)).some(x => x.ｸﾞﾙｰﾌﾟ稟議可決番号 == ''
            && (((x.修繕区分小 == '02' || x.修繕区分小 == '03') && x.負担区分 == '2' && x.発注差戻可能チェック == 0) || ((x.修繕区分小 != '02' && x.修繕区分小 != '03') || x.負担区分 != '2')))
          if (rowsl) {
            this.disable差戻 = false
          } else {
            this.disable差戻 = true
          }
          // ハノイ側修正2023/08/08　STEP2_W　課題管理表№383：設計書「修正履歴」シートの「2023/08/08仕様変更分」「承認種類＝「41：発注確認」の場合の差戻ボタン押下可能条件を修正。」END
          this.disable承認 = false
          this.disable否認 = false
        } else {
          this.disable差戻 = true
          this.disable承認 = true
          this.disable否認 = true
        }
      }
    },
    checkBoxHatchuShoninState (row) {
      return row.ステータス == '発注承認待ち'
    },
    handleCheckAllBumonKakunin (checked) {
      if (!checked) {
        this.selectedBumonKakuninRowKeys = []
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」Start
        this.disable部門確認 = true
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」End
      } else {
        const selectedRowKeys1 = []
        this.tableData.map((row) => {
          if (this.checkBoxBumonKakuninState(row)) {
            selectedRowKeys1.push(row.key)
          }
        })
        this.selectedBumonKakuninRowKeys = selectedRowKeys1
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」Start
        if (this.selectedBumonKakuninRowKeys.length > 0) {
          this.disable部門確認 = false
        }
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」End
      }
    },
    handleCheckBumonKakunin (checked, row) {
      const rowKeyIndex = this.selectedBumonKakuninRowKeys.indexOf(row.key)
      const lstKey = row.ペア発注サブNO != 0 ? this.tableData.filter(x => x.工事依頼NO == row.工事依頼NO &&
      x.ペア発注サブNO == row.ペア発注サブNO &&
      x.ペア検収回数 == row.ペア検収回数 &&
      this.checkBoxBumonKakuninState(x) &&
      x.key != row.key).map(x => x.key) : []
      if (!checked) {
        // remove selected
        this.selectedBumonKakuninRowKeys.splice(rowKeyIndex, 1)
        this.selectedBumonKakuninRowKeys = this.selectedBumonKakuninRowKeys.filter(x => !lstKey.includes(x))
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」Start
        if (this.selectedBumonKakuninRowKeys.length === 0) {
          this.disable部門確認 = true
        }
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」End
      } else if (checked && this.checkBoxBumonKakuninState(row)) {
        // add selected
        this.selectedBumonKakuninRowKeys.push(row.key)
        this.selectedBumonKakuninRowKeys = this.selectedBumonKakuninRowKeys.concat(lstKey)
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」Start
        this.disable部門確認 = false
        // ハノイ側修正2023/02/23　STEP2_W　課題管理表№296：設計書「修正履歴」シートの「2023/02/22仕様変更分」「各種ボタンは明細欄の該当チェックボックスにチェックがある時のみボタン押下可能とする。」End
      }
      this.selectedBumonKakuninRowKeys = this.selectedBumonKakuninRowKeys.filter((v, i, a) => a.indexOf(v) === i)
    },
    checkBoxBumonKakuninState (row) {
      // ハノイ側修正2022/03/28　STEP2_W　課題管理表№318：設計書「修正履歴」シートの「2022/03/28仕様変更分」承認種類＝「61：支払承認」の場合の部門確認チェック不可条件に定期分の条件追加 Start
      const stateF100 = row.F100工事区分 && row.F100工事区分.substring(0, 1) == '4'
        ? row.按分_NO35 == '1' && row.表示金額 != row.税込按分金額
        : row.W費用負担部門按分フラグ == '0'
      const state = row.部門確認 == '未確認' && !stateF100
      // ハノイ側修正2022/03/28　STEP2_W　課題管理表№318：設計書「修正履歴」シートの「2022/03/28仕様変更分」承認種類＝「61：支払承認」の場合の部門確認チェック不可条件に定期分の条件追加 Start

      return state
    },
    // ハノイ側修正2022/09/28　STEP2_W　課題管理表№127：設計書「修正履歴」シートの「2022/09/28仕様変更分」ペア発注サブ№、ペア検収回数追加
    // ハノイ側修正2023/08/08　STEP2_W　課題管理表№381：設計書「修正履歴」シートの「2023/08/08仕様変更分」「承認種類：発注承認（41）のデータを1行しか選択していない場合でも、否認や差戻が出来ない。」Start
    checkPair () {
      if (!this.tableData || this.tableData.length <= 0) {
        return false
      } else {
        const dataTblAll = JSON.parse(JSON.stringify(this.tableData)) || []

        if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
          let dataTbl = dataTblAll.filter(x => this.selectedHatchuShoninRowKeys.includes(x.key)) || []
          if (dataTbl.length > 1) {
            return false
          } else {
            return true
          }
        } else if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
          let dataTbl = dataTblAll.filter(x => this.selectedShiharaiShoninRowKeys.includes(x.key))
          dataTbl = dataTbl.map((x) => {
            x.ペア発注サブNO = x.ペア発注サブNO || 0
            return x
          })
          var groupBy = (xs, key, key1) => {
            const grp = xs.reduce(function (rv, x) {
              (rv[x[key] + '_' + x[key1]] = rv[x[key] + '_' + x[key1]] || []).push(x)
              return rv
            }, {})
            const arr = []
            for (const prop in grp) {
              if (prop) {
                arr.push({ key: prop, value: grp[prop] })
              }
            }
            arr.sort((a, b) => (a.key > b.key) ? 1 : ((b.key > a.key) ? -1 : 0))
            return arr
          }
          const res = groupBy(dataTbl, '工事依頼NO', 'ペア発注サブNO')
          if (res.length > 1) {
            return false
          } else if (res[0].value.length > 1 && res[0].value[0].ペア発注サブNO == 0) {
            return false
          }
          return true
        } else {
          return true
        }
      }
    },
    // ハノイ側修正2023/08/08　STEP2_W　課題管理表№381：設計書「修正履歴」シートの「2023/08/08仕様変更分」「承認種類：発注承認（41）のデータを1行しか選択していない場合でも、否認や差戻が出来ない。」End
    handleCheckAllShiharaiShonin (checked) {
      if (!checked) {
        this.selectedShiharaiShoninRowKeys = []
        this.disable差戻 = true
        this.disable承認 = true
        this.disable否認 = true
      } else {
        const selectedRowKeys1 = []
        this.tableData.map((row) => {
          if (this.checkBoxShiharaiShoninState(row)) {
            selectedRowKeys1.push(row.key)
          }
        })
        this.selectedShiharaiShoninRowKeys = selectedRowKeys1
        if (this.selectedShiharaiShoninRowKeys.length > 0) {
          this.disable差戻 = false
          this.disable承認 = false
          this.disable否認 = false
        }
      }
      this.sum確認金額計(checked)
    },
    handleCheckShiharaiShonin (checked, row) {
      if (!this.tableData || this.tableData.length <= 0) {
        this.確認金額計 = '0'
      } else {
        const dataTblAll = JSON.parse(JSON.stringify(this.tableData)) || []
        const rowKeyIndex = this.selectedShiharaiShoninRowKeys.indexOf(row.key)
        const lstKey = row.ペア発注サブNO != 0 ? dataTblAll.filter(x => x.工事依頼NO == row.工事依頼NO &&
        x.ペア発注サブNO == row.ペア発注サブNO &&
        x.ペア検収回数 == row.ペア検収回数 &&
        this.checkBoxShiharaiShoninState(x) &&
        x.key != row.key).map(x => x.key) : []
        if (!checked) {
          // remove selected
          this.selectedShiharaiShoninRowKeys.splice(rowKeyIndex, 1)
          this.selectedShiharaiShoninRowKeys = this.selectedShiharaiShoninRowKeys.filter(x => !lstKey.includes(x))
        } else if (checked && this.checkBoxShiharaiShoninState(row)) {
          // add selected
          this.selectedShiharaiShoninRowKeys.push(row.key)
          this.selectedShiharaiShoninRowKeys = this.selectedShiharaiShoninRowKeys.concat(lstKey)
        }
        this.selectedShiharaiShoninRowKeys = this.selectedShiharaiShoninRowKeys.filter((v, i, a) => a.indexOf(v) === i)

        this.sum確認金額計(checked)

        if (this.selectedShiharaiShoninRowKeys.length > 0) {
          this.disable差戻 = false
          this.disable承認 = false
          this.disable否認 = false
        } else {
          this.disable差戻 = true
          this.disable承認 = true
          this.disable否認 = true
        }
      }
    },
    checkBoxShiharaiShoninState (row) {
      const ステータス = row.ステータス != '支払承認待ち' ||
        row.部門確認 != '確認済' ||
        (row.業者状態 != '請求合意済' && row.業者状態 != '請求合意不要') ||
        !!row.不備有無

      return !ステータス
    },
    sum (a, b) {
      a = this.getNumber(a)
      b = this.getNumber(b)
      return a * 1 + b * 1
    },
    getNumber (string) {
      if (!string) return 0
      return (string + '' || '0').replaceAll(',', '') * 1
    },
    setDefault対象 () {
      if (this.pageData.対象 && !this.対象List.find(x => x.value == this.pageData.対象)) {
        this.pageData.対象 = ''
      }

      if (this.pageData.対象_履歴 && !this.対象_履歴List.find(x => x.value == this.pageData.対象_履歴)) {
        this.pageData.対象_履歴 = ''
      }

      if (!this.pageData.対象 && !this.pageData.対象_履歴) {
        // ハノイ側修正2023/05/25　STEP2_W　課題管理表№342：設計書「修正履歴」シートの「2023/05/24仕様変更分」「メニューの別のリンク押下後、稟議申請の差戻件数リンク押下時、稟議申請待ちの条件で検索されてしまう。」Start
        if (this.pageData.承認種類 == this.$route.query.承認種類 && this.$route.query.対象履歴 && this.対象List.find(x => x.value == this.$route.query.対象履歴)) {
          this.pageData.対象 = this.$route.query.対象履歴
        } else {
          this.pageData.対象 = this.対象List[0].value
        }
        // ハノイ側修正2023/05/25　STEP2_W　課題管理表№342：設計書「修正履歴」シートの「2023/05/24仕様変更分」「メニューの別のリンク押下後、稟議申請の差戻件数リンク押下時、稟議申請待ちの条件で検索されてしまう。」End
      }
    },
    findColumn (key) {
      const column = this.columns.find(x => x.key == key)
      if (column) {
        return column
      } else {
        console.error('Column invalid!')
        return {}
      }
    },
    clearPageData () {
      if (this.sessionPageData) {
        return
      }
      for (const prop in this.pageData) {
        if (prop != '承認種類') {
          this.pageData[prop] = ''
        }
      }
    },
    showHideItem () {
      for (let item in this.visible) {
        this.visible[item] = false
      }
      if (this.pageData.承認種類 == this.ApprovalType.依頼承認 ||
      !this.pageData.承認種類) {
        this.visible.営業店 = true
        this.visible.申請日1 = true
        this.visible.工事区分 = true
        this.visible.物件区分 = true
        this.visible.物件名 = true
        this.visible.工事件名 = true
        this.visible.修繕_定期 = true
        this.visible.承認者 = true
        // } else if (this.pageData.承認種類 == this.ApprovalType.依頼承認_定期) {
        //   this.visible.営業店 = true
        //   this.visible.申請日1 = true
        //   this.visible.工事区分 = true
        //   this.visible.物件区分 = true
        //   this.visible.物件名 = true
        //   this.visible.依頼内容 = true
        this.dsp工事依頼No = '（工事）依頼№'
      } else if (this.pageData.承認種類 == this.ApprovalType.見積承認_実施判断 ||
      this.pageData.承認種類 == this.ApprovalType.稟議申請) {
        this.visible.営業店 = true
        this.visible.申請日1 = true
        this.visible.工事区分 = true
        this.visible.物件区分 = true
        this.visible.手配先 = true
        this.visible.工事件名 = true
        if (this.pageData.承認種類 == this.ApprovalType.見積承認_実施判断) {
          this.visible.修繕_定期 = true
          this.visible.承認者 = true
          this.dsp工事依頼No = '（工事）依頼№'
        } else {
          this.dsp工事依頼No = '工事依頼№'
        }
      } else if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
        this.visible.営業店 = true
        this.visible.申請日1 = true
        this.visible.工事区分 = true
        this.visible.手配先 = true
        this.visible.工事件名 = true
        this.visible.承認者 = true
        this.pageData.dsp工事依頼No = '工事依頼№'
      } else if (this.pageData.承認種類 == this.ApprovalType.工事完了確認) {
        this.visible.営業店 = true
        this.visible.申請日1 = true
        this.visible.工事区分 = true
        this.visible.工事完了日 = true
        this.visible.手配先 = true
        this.visible.工事件名 = true
        this.visible.承認者 = true
        this.pageData.dsp工事依頼No = '工事依頼№'
      } else if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
        this.visible.営業店 = true
        this.visible.申請日2 = true
        this.visible.工事区分 = true
        this.visible.物件区分 = true
        // this.visible.工事完了日 = true
        this.visible.手配先 = true
        this.visible.工事件名 = false
        this.visible.請求内容 = true
        this.visible.申請部署 = true
        this.visible.不備 = true
        this.visible.申請者 = true
        // this.visible.確認金額計 = true
        this.visible.承認者 = true
        this.dsp工事依頼No = '（工事）依頼№'
        // ハノイ側修正2022/11/23　STEP2_W　課題管理表№223：設計書「修正履歴」シートの「2022/11/21仕様変更分」「検索項目に「按分」「業者状態」追加, 検索項目指定時の条件追加、修正」Start
        this.visible.按分 = true
        this.visible.業者状態 = true
        // ハノイ側修正2022/11/23　STEP2_W　課題管理表№223：設計書「修正履歴」シートの「2022/11/21仕様変更分」「検索項目に「按分」「業者状態」追加, 検索項目指定時の条件追加、修正」End
        this.visible.部門確認 = true // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」
      }
      this.clearPageData()
      // ハノイ側修正2022/08/05　STEP2_W　課題管理表№24：設計書「修正履歴」シートの「2022/08/03仕様変更分」◆TOPページ（WD00010）の件数カウントからの画面遷移時の初期表示条件について　追加 Start
      if (this.$route.query.修繕定期) {
        this.pageData.修繕_定期 = this.$route.query.修繕定期
      } else {
        this.pageData.修繕_定期 = '全て'
      }
      if (this.$route.query.対象履歴) {
        if (this.$route.query.対象履歴 === '承認待ち' || this.$route.query.対象履歴 === '申請中') {
          this.pageData.対象 = this.$route.query.対象履歴
        } else {
          this.pageData.対象_履歴 = this.$route.query.対象履歴
        }
      }
      // ハノイ側修正2022/08/05　STEP2_W　課題管理表№24：設計書「修正履歴」シートの「2022/08/03仕様変更分」◆TOPページ（WD00010）の件数カウントからの画面遷移時の初期表示条件について　追加 End
    },
    async validateSave () {
      // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」Start
      let dataTbl = JSON.parse(JSON.stringify(this.tableData)) || []
      if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
        dataTbl = dataTbl.filter(x => this.selectedHatchuShoninRowKeys.includes(x.key))
      }
      if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
        dataTbl = dataTbl.filter(x => this.selectedShiharaiShoninRowKeys.includes(x.key))
      }
      if (dataTbl.length === 0) {
        return false
      }
      // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」End
      for (let prop in this.pageData) {
        if (this.pageData[prop] != this.searchedParam[prop]) {
          this.getMsg('E000012').then((response) => {
            this.showError(response.data, () => { })
          })
          return false
        }
      }
      return true
    },
    async validateSashiAndHinin () {
      if (!this.checkPair()) {
      // let dataTbl = JSON.parse(JSON.stringify(this.tableData)) || []
      // dataTbl = dataTbl.filter(x => this.$refs.table.getCheckboxSelected().includes(x.key))
      // if (dataTbl.length > 1) {
        this.getMsg('E040724').then((response) => {
          this.showError(response.data, () => { })
        })
        // this.$refs.table.focus()
        return false
      }

      if (!this.否認差戻事由) {
        this.getMsg('E040357').then((response) => {
          this.showError(response.data, () => { })
        })
        this.$refs.ref否認差戻事由.focus()
        return false
      }

      return true
    },
    async onSashimodoshi () {
      // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」Start
      let dataTbl = JSON.parse(JSON.stringify(this.tableData)) || []
      if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
        dataTbl = dataTbl.filter(x => this.selectedHatchuShoninRowKeys.includes(x.key))
      }
      if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
        dataTbl = dataTbl.filter(x => this.selectedShiharaiShoninRowKeys.includes(x.key))
      }
      if (dataTbl.length === 0) {
        return false
      }
      // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」End
      if (!await this.validateSashiAndHinin()) {
        return false
      }
      const msgCode = this.searchedParam.承認種類 == this.ApprovalType.発注承認 ? 'K040026' : 'K040070'
      let msg = (await this.getMsg(msgCode)).data
      if (!await this.confirm(msg)) {
        return false
      }

      const data = {
        FW042_申請承認ファイル: this.getDataFW042(),
        会社コード: this.userInfo.会社コード,
        承認種類: this.searchedParam.承認種類,
        mode: '差戻',
        否認差戻事由: this.否認差戻事由
      }
      let res
      await this.http
        .post('/api/Reafs_W/Shonin/WD00510/Sashimodoshi', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        this.getMsg('M040014').then((response) => {
          this.showSuccess(response.data)
        })
        this.onResearch = true // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」
        await this.sendMail('差戻', res.data.lstF001, res.data.申請者)// ハノイ側修正2022/10/25　STEP2_W　課題管理表№189：設計書「修正履歴」シートの「2022/10/21仕様変更分」「◆メール送信について追加」
        this.onSearch(false)
      } else {
        this.showError(res.message)
      }
    },
    async onHinin () {
      if (!await this.validateSave()) {
        return false
      }
      if (!await this.validateSashiAndHinin()) {
        return false
      }
      const msgCode = this.searchedParam.承認種類 == this.ApprovalType.発注承認 ? 'K040027' : 'K040066'
      let msg = (await this.getMsg(msgCode)).data
      if (!await this.confirm(msg)) {
        return false
      }

      const data = {
        FW042_申請承認ファイル: this.getDataFW042(),
        会社コード: this.userInfo.会社コード,
        承認種類: this.searchedParam.承認種類,
        mode: '否認',
        否認差戻事由: this.否認差戻事由
      }
      let res
      await this.http
        .post('/api/Reafs_W/Shonin/WD00510/Hinin', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        this.getMsg('M040013').then((response) => {
          this.showSuccess(response.data)
        })
        this.onResearch = true // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」
        this.onSearch(false)
      } else {
        this.showError(res.message)
      }
    },
    async onShonin () {
      if (!await this.validateSave()) {
        return false
      }
      const msgCode = this.searchedParam.承認種類 == this.ApprovalType.発注承認 ? 'K040028' : 'K040065'
      let msg = (await this.getMsg(msgCode)).data
      if (!await this.confirm(msg)) {
        return false
      }

      const data = {
        FW042_申請承認ファイル: this.getDataFW042(),
        会社コード: this.userInfo.会社コード,
        承認種類: this.searchedParam.承認種類
      }
      let res
      await this.http
        .post('/api/Reafs_W/Shonin/WD00510/Shonin', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        this.getMsg('M040010').then((response) => {
          this.showSuccess(response.data)
        })
        this.onResearch = true // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」
        // ハノイ側修正2022/10/25　STEP2_W　課題管理表№189：設計書「修正履歴」シートの「2022/10/21仕様変更分」「◆メール送信について追加」START
        await this.sendMail('承認', res.data.lstF001, res.data.申請者, res.data.lstF004Res, res.data.lstFT040) // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」
        // ハノイ側修正2022/10/25　STEP2_W　課題管理表№189：設計書「修正履歴」シートの「2022/10/21仕様変更分」「◆メール送信について追加」END
        this.onSearch(false)
        return true
      } else {
        this.showError(res.message)
        return false
      }
    },
    async onBumonKakunin () {
      if (this.searchedParam.承認種類 != this.ApprovalType.支払承認) {
        return
      }

      let msg = (await this.getMsg('K040083')).data
      if (!await this.confirm(msg)) {
        return false
      }

      const data = {
        FW042_申請承認ファイル: this.getDataFW042(true),
        会社コード: this.userInfo.会社コード,
        承認種類: this.searchedParam.承認種類
      }
      let res
      await this.http
        .post('/api/Reafs_W/Shonin/WD00510/BumonKakunin', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        this.getMsg('M040021').then((response) => {
          this.showSuccess(response.data)
        })
        this.onResearch = true
        this.onSearch(false)
      } else {
        this.showError(res.message)
      }
    },
    async sendMail (btnName, lstF001, 申請者, lstF004Res, lstFT040) { // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」
      if (!lstF001) {
        return
      }
      lstF001 = lstF001.reduce((prev, el) => {
        if (prev.some(o => o.工事依頼No == el.工事依頼No && o.工事依頼No枝番 == el.工事依頼No枝番)) { return prev }
        return [...prev, {工事依頼No: el.工事依頼No, 工事依頼No枝番: el.工事依頼No枝番, 修繕区分小: el.修繕区分小, 最終承認フラグ: el.最終承認フラグ, 工事件名: el.工事件名}]
      }, [])
      const lstF004Key = []
      for (let i = 0; i < lstF001.length; i++) {
        const f001 = lstF001[i]
        let param = []
        if (btnName == '差戻') {
          const mailPatternNo = f001.修繕区分小 == '01' || f001.修繕区分小 == '04' ? '23'
            : f001.修繕区分小 == '05' ? '90' : ''
          // eslint-disable-next-line camelcase
          const Reafs_W_BaseURL = this.commonFunctionUI.getBaseURL('W')
          // eslint-disable-next-line camelcase
          let linkUrl = Reafs_W_BaseURL + 'WD00520/SyuzenIraiSansyoShonin'
          let commonParam = {
            パラメータ1: f001.工事依頼No,
            パラメータ2: f001.発注サブNo,
            パラメータ3: f001.検収回数,
            パラメータ4: 'hatchu',
            PG: 'WD00510'
          }
          const resParam = await this.commonFunctionUI.setQueryParameter(commonParam)
          linkUrl = linkUrl + '?guid=' + resParam.data.guid
          linkUrl = encodeURI(linkUrl)
          if (!mailPatternNo) {
            continue
          }

          // ハノイ側修正2022/11/08　STEP2_W　課題管理表№209：設計書「修正履歴」シートの「2022/11/08仕様変更分」「メール送信条件②（承認種類）を追記」Start
          if (this.pageData.承認種類 != '41') {
            continue
          }
          // ハノイ側修正2022/11/08　STEP2_W　課題管理表№209：設計書「修正履歴」シートの「2022/11/08仕様変更分」「メール送信条件②（承認種類）を追記」End
          param = {
            msg_type: 0,
            mail_to: '',
            msg_head: '',
            msg_body: '',
            pg_id: 'WD00510',
            st_kbn: 1,
            mail_pattern_no: mailPatternNo,
            工事依頼NO: f001.工事依頼No,
            工事依頼NO枝番: f001.工事依頼No枝番,
            // 契約NO
            // 履歴NO
            // 明細NO
            // 契約年月
            宛先事務所コード: [],
            宛先営業所コード: [],
            宛先部コード: [],
            宛先課コード: [],
            宛先係コード: [],
            // 業者コード
            // 業者コード枝番
            担当者コード: 申請者,
            url: [linkUrl]
          }
          const res = await this.commonFunctionUI.CM00110SendMail(param)
        } else if (btnName == '承認') {
          if (f001.最終承認フラグ != '1') {
            continue
          }
          let lsF004 = lstF004Res ? lstF004Res.filter(x => x.工事依頼No == f001.工事依頼No) : [{}]
          if (!lsF004) {
            lsF004 = [{}]
          }
          const mailPatternNo = f001.修繕区分小 == '01' || f001.修繕区分小 == '04' ? '24'
            : f001.修繕区分小 == '02' ? '54'
              : f001.修繕区分小 == '03' ? '75' : ''
          // eslint-disable-next-line camelcase
          const Reafs_T_BaseURL = this.commonFunctionUI.getBaseURL('T')
          // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」Start
          if (!mailPatternNo) {
            continue
          }

          // ハノイ側修正2022/11/08　STEP2_W　課題管理表№209：設計書「修正履歴」シートの「2022/11/08仕様変更分」「メール送信条件②（承認種類）を追記」Start
          if (this.pageData.承認種類 != '41') {
            continue
          }
          // ハノイ側修正2022/11/08　STEP2_W　課題管理表№209：設計書「修正履歴」シートの「2022/11/08仕様変更分」「メール送信条件②（承認種類）を追記」End
          for (let j = 0; j < lsF004.length; j++) {
            if (lstF004Key.find(x => x.工事依頼No == f001.工事依頼No &&
             x.取引先コード == lsF004[j].取引先コード && x.取引先コード枝番 == lsF004[j].取引先コード枝番)) {
              continue
            }
            lstF004Key.push({工事依頼No: f001.工事依頼No,
              取引先コード: lsF004[j].取引先コード,
              取引先コード枝番: lsF004[j].取引先コード枝番})

            let lstFT040Filter = lstFT040 ? lstFT040.filter(
              x => x.工事依頼No == f001.工事依頼No && x.工事依頼No枝番 == f001.工事依頼No枝番 &&
              x.業者コード == lsF004[j].取引先コード && x.業者コード枝番 == lsF004[j].取引先コード枝番) : []
            if (!lstFT040Filter) {
              lstFT040Filter = []
            }
            // eslint-disable-next-line camelcase
            let linkUrl = Reafs_T_BaseURL + 'KojiIchiran'
            let startupMode = 5
            let commonParam = {
              パラメータ1: startupMode,
              パラメータ2: lstFT040Filter[0] && lstFT040Filter[0].工事件名 != '' ? lstFT040Filter[0].工事件名 : f001.工事件名,
              PG: 'WD00510'
            }
            const resParam = await this.commonFunctionUI.setQueryParameter(commonParam)
            linkUrl = linkUrl + '?guid=' + resParam.data.guid
            linkUrl = encodeURI(linkUrl)
            // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」End
            param = {
              msg_type: 0,
              mail_to: '',
              msg_head: '',
              msg_body: '',
              pg_id: 'WD00510',
              st_kbn: 1,
              mail_pattern_no: mailPatternNo,
              工事依頼NO: f001.工事依頼No,
              工事依頼NO枝番: f001.工事依頼No枝番,
              // 契約NO
              // 履歴NO
              // 明細NO
              // 契約年月
              宛先事務所コード: [],
              宛先営業所コード: [],
              宛先部コード: [],
              宛先課コード: [],
              宛先係コード: [],
              業者コード: lsF004[j].取引先コード,
              業者コード枝番: lsF004[j].取引先コード枝番,
              担当者コード: [],
              url: [linkUrl]
            }
            const res = await this.commonFunctionUI.CM00110SendMail(param)
          }
        }
      }
    },
    async handleLinkClick (row, flg = '') {
      if (row.帳票 && flg === '') {
        if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
          this.paramsTyohyoIchiran.工事依頼No = row.工事依頼NO
          this.paramsTyohyoIchiran.契約No = ''
          this.paramsTyohyoIchiran.契約履歴No = ''
          this.paramsTyohyoIchiran.見積明細No = ''
          this.paramsTyohyoIchiran.契約年月 = ''
          this.paramsTyohyoIchiran.予定年月 = ''

          this.showDialogTyohyoIchiran = true
        } else if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
        // ハノイ側修正2023/05/23　STEP2_W　課題管理表№340：設計書「修正履歴」シートの「2023/05/22仕様変更分」「工事区分の頭1桁='4'（定期案件）の場合の、「WD00130 帳票一覧」画面起動引数を修正。」 START
          if (row.F100工事区分.substring(0, 1) != '4') {
            this.paramsTyohyoIchiran.工事依頼No = row.工事依頼NO
            this.paramsTyohyoIchiran.契約No = ''
            this.paramsTyohyoIchiran.契約履歴No = ''
            this.paramsTyohyoIchiran.見積明細No = ''
            this.paramsTyohyoIchiran.契約年月 = ''
            this.paramsTyohyoIchiran.予定年月 = ''
          } else if (row.F100工事区分.substring(0, 1) === '4') {
            this.paramsTyohyoIchiran.工事依頼No = ''
            this.paramsTyohyoIchiran.契約No = row.契約NO
            this.paramsTyohyoIchiran.契約履歴No = row.履歴No
            this.paramsTyohyoIchiran.見積明細No = row.明細NO
            this.paramsTyohyoIchiran.契約年月 = row.契約年月
            this.paramsTyohyoIchiran.予定年月 = row.契約年月
          }
          this.showDialogTyohyoIchiran = true
          // this.$router.push({ name: 'WD00130', params: { 工事依頼No: row.工事依頼NO } })
        }
        // ハノイ側修正2023/05/23　STEP2_W　課題管理表№340：設計書「修正履歴」シートの「2023/05/22仕様変更分」「工事区分の頭1桁='4'（定期案件）の場合の、「WD00130 帳票一覧」画面起動引数を修正。」 END
      } else {
        this.$store.commit('setKeepAliveFlag', 0)
        this.scrollTop = document.querySelector('.ve-table-container').scrollTop

        if (this.pageData.承認種類 == this.ApprovalType.依頼承認) {
        // ハノイ側修正2022/07/28　STEP2_W　課題管理表№14：設計書「修正履歴」シートの「2022/07/14仕様変更分」「№15,16,18修正  №27追加」Start
        // ハノイ側修正2022/08/17　STEP2_W　課題管理表№38：設計書「修正履歴」シートの「2022/08/16仕様変更分」承認種類＝「10：依頼承認」のリンククリック時の遷移先を修正 Start
        // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
          if (row.F100工事区分.substring(0, 1) != '4') {
            if (this.searchedParam.対象) {
              if (this.searchedParam.対象 === '承認待ち') {
                // window.location.href = `/Reafs_W/SyuzenIraiSansyoShonin?工事依頼No=${row.工事依頼NO}&page=WD00510`
                this.$router.push({ name: 'WD00111', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
              } else if (this.searchedParam.対象 === '申請中') {
                // window.location.href = `/Reafs_W/IraiNyuryoku?工事依頼No=${row.工事依頼NO}&page=WD00510`
                this.$router.push({ name: 'WD00110', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
              }
            }
            if (this.searchedParam.対象_履歴) {
              if (this.searchedParam.対象_履歴 === '承認済') {
                // window.location.href = `/Reafs_W/SyuzenIraiSansyoShonin?工事依頼No=${row.工事依頼NO}&page=WD00510`
                this.$router.push({ name: 'WD00111', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
              } else if (this.searchedParam.対象_履歴 === '否認' || this.searchedParam.対象_履歴 === '差戻' || this.searchedParam.対象_履歴 === '取下') {
                if (await this.getCheckログイン情報(row.FW042承認事務所コード, row.FW042承認営業所コード, row.FW042承認部コード, row.FW042承認課コード, row.FW042承認係コード)) {
                  // window.location.href = `/Reafs_W/SyuzenIraiSansyoShonin?工事依頼No=${row.工事依頼NO}&page=WD00510`
                  this.$router.push({ name: 'WD00111', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
                } else if (await this.getCheckログイン情報(row.FW042申請者事務所コード, row.FW042申請者営業所コード, row.FW042申請者部コード, row.FW042申請者課コード, row.FW042申請者係コード)) {
                  // window.location.href = `/Reafs_W/IraiNyuryoku?工事依頼No=${row.工事依頼NO}&page=WD00510`
                  this.$router.push({ name: 'WD00110', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
                }
              }
            }
          } else {
            if (this.searchedParam.対象) {
              if (this.searchedParam.対象 === '承認待ち') {
                // window.location.href = `/Reafs_W/TeikiIraiSansyoShonin?依頼No=${row.工事依頼NO}&page=WD00510`
                this.$router.push({ name: 'WD00311', params: { 依頼No: row.工事依頼NO, page: 'WD00510' } })
              } else if (this.searchedParam.対象 === '申請中') {
                // window.location.href = `/Reafs_W/IraiNyuryoku?工事依頼No=${row.工事依頼NO}&page=WD00510`
                this.$router.push({ name: 'WD00110', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
              }
            }
            if (this.searchedParam.対象_履歴) {
              if (this.searchedParam.対象_履歴 === '承認済') {
                // window.location.href = `/Reafs_W/TeikiIraiSansyoShonin?依頼No=${row.工事依頼NO}&page=WD00510`
                this.$router.push({ name: 'WD00311', params: { 依頼No: row.工事依頼NO, page: 'WD00510' } })
              } else if (this.searchedParam.対象_履歴 === '否認' || this.searchedParam.対象_履歴 === '差戻' || this.searchedParam.対象_履歴 === '取下') {
                if (await this.getCheckログイン情報(row.FW042承認事務所コード, row.FW042承認営業所コード, row.FW042承認部コード, row.FW042承認課コード, row.FW042承認係コード)) {
                  // window.location.href = `/Reafs_W/TeikiIraiSansyoShonin?依頼No=${row.工事依頼NO}&page=WD00510`
                  this.$router.push({ name: 'WD00311', params: { 依頼No: row.工事依頼NO, page: 'WD00510' } })
                } else if (await this.getCheckログイン情報(row.FW042申請者事務所コード, row.FW042申請者営業所コード, row.FW042申請者部コード, row.FW042申請者課コード, row.FW042申請者係コード)) {
                  // window.location.href = `/Reafs_W/IraiNyuryoku?工事依頼No=${row.工事依頼NO}&page=WD00510`
                  this.$router.push({ name: 'WD00110', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
                }
              }
            }
          }
        // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End
          // ハノイ側修正2022/08/17　STEP2_W　課題管理表№38：設計書「修正履歴」シートの「2022/08/16仕様変更分」承認種類＝「10：依頼承認」のリンククリック時の遷移先を修正 Start
          // this.$router.push({ name: 'WD00111', params: { 工事依頼No: row.工事依頼NO, page: 'WD00510' } })
        } else if (this.pageData.承認種類 == this.ApprovalType.見積承認_実施判断) {
          if (row.F100工事区分.substring(0, 1) != '4') {
            // window.location.href = `/Reafs_W/WD00520/SyuzenIraiSansyoShonin?工事依頼No=${row.工事依頼NO}&tabFocus=mitsu`
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
            this.$router.push({ name: 'WD00520', params: { 工事依頼No: row.工事依頼NO, tabFocus: 'mitsu' } })
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End
          } else {
            // ハノイ側修正2022/09/08　STEP2_W　課題管理表№86：設計書「修正履歴」シートの「2022/09/06仕様変更分」「WD00530起動時引数追加」Start
            // window.location.href = `/Reafs_W/WD00530/TeikiIraiSansyoShonin?契約NO=${row.契約NO}&履歴NO=${row.履歴No}&明細NO=${row.明細NO}&契約年月=${row.契約年月}&契約年度=${row.契約年度}&page=WD00510&tabFocus=mitsu`
            // ハノイ側修正2022/09/08　STEP2_W　課題管理表№86：設計書「修正履歴」シートの「2022/09/06仕様変更分」「WD00530起動時引数追加」End
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
            this.$router.push({ name: 'WD00530', params: { 契約NO: row.契約NO, 履歴NO: row.履歴No, 明細NO: row.明細NO, 契約年月: row.契約年月, 契約年度: row.契約年度, page: 'WD00510', tabFocus: 'mitsu' } })
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End
          }
        } else if (this.pageData.承認種類 == this.ApprovalType.稟議申請 ||
                    this.pageData.承認種類 == this.ApprovalType.発注承認) {
          // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
          this.$router.push({ name: 'WD00520', params: { 工事依頼No: row.工事依頼NO, tabFocus: 'hatchu' } })
          // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End
          // window.location.href = `/Reafs_W/WD00520/SyuzenIraiSansyoShonin?工事依頼No=${row.工事依頼NO}&tabFocus=hatchu`
        // this.$router.push({ name: 'WD00520', params: { 工事依頼No: row.工事依頼NO, tabFocus: 'hatchu' } })
        // const link = '/Reafs_W/SyuzenIraiSansyoShonin?工事依頼No=' + row.工事依頼NO
        // window.location.href = link
        } else if (this.pageData.承認種類 == this.ApprovalType.工事完了確認) {
        // const link = '/Reafs_W/SyuzenIraiSansyoShonin?工事依頼No=' + row.工事依頼NO
        // window.location.href = link
          // window.location.href = `/Reafs_W/WD00520/SyuzenIraiSansyoShonin?工事依頼No=${row.工事依頼NO}&発注サブNo=${row.発注サブNo}&検収回数=${row.検収回数}&tabFocus=sagyo`
          // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
          this.$router.push({ name: 'WD00520', params: { 工事依頼No: row.工事依頼NO, 発注サブNo: row.発注サブNo, 検収回数: row.検収回数, tabFocus: 'sagyo', 承認種類: '工事完了確認' } })
          // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End

          // this.$router.push({ name: 'WD00520', params: { 工事依頼No: row.工事依頼NO, 発注サブNo: row.発注サブNo, 検収回数: row.検収回数, tabFocus: 'sagyo' } })
        // ハノイ側修正2022/07/28　STEP2_W　課題管理表№14：設計書「修正履歴」シートの「2022/07/14仕様変更分」「№15,16,18修正  №27追加」END
          // ハノイ側修正2022/08/01　STEP2_W　課題管理表№16：設計書「修正履歴」シートの「2022/07/21仕様変更分」承認種類＝「32：見積承認（実施判断）」「61：支払承認」のリンククリック時の画面遷移先を修正 Start
        } else if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
          if (row.F100工事区分.substring(0, 1) != '4') {
            // window.location.href = `/Reafs_W/WD00520/SyuzenIraiSansyoShonin?工事依頼No=${row.工事依頼NO}&発注サブNo=${row.発注サブNo}&検収回数=${row.検収回数}&tabFocus=sagyo`
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
            this.$router.push({ name: 'WD00520', params: { 工事依頼No: row.工事依頼NO, 発注サブNo: row.発注サブNo, 検収回数: row.検収回数, tabFocus: 'sagyo', 承認種類: '支払承認' } })
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End
          } else {
            // ハノイ側修正2022/09/08　STEP2_W　課題管理表№86：設計書「修正履歴」シートの「2022/09/06仕様変更分」「WD00530起動時引数追加」Start
            // window.location.href = `/Reafs_W/WD00530/TeikiIraiSansyoShonin?契約NO=${row.契約NO}&履歴NO=${row.履歴No}&明細NO=${row.明細NO}&契約年月=${row.契約年月}&契約年度=${row.契約年度}&page=WD00510&tabFocus=sagyo`
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
            this.$router.push({ name: 'WD00530', params: { 契約NO: row.契約NO, 履歴NO: row.履歴No, 明細NO: row.明細NO, 契約年月: row.契約年月, 契約年度: row.契約年度, page: 'WD00510', tabFocus: 'sagyo' } })
            // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End
            // ハノイ側修正2022/09/08　STEP2_W　課題管理表№86：設計書「修正履歴」シートの「2022/09/06仕様変更分」「WD00530起動時引数追加」End
          }
        }
        // ハノイ側修正2022/08/01　STEP2_W　課題管理表№16：設計書「修正履歴」シートの「2022/07/21仕様変更分」承認種類＝「32：見積承認（実施判断）」「61：支払承認」のリンククリック時の画面遷移先を修正 End
      }
    },
    getDataFW042 (isGet部門確認) {
      const resData = []
      let dataTbl
      if (isGet部門確認) {
        dataTbl = this.tableData.filter(x => this.selectedBumonKakuninRowKeys.includes(x.key))
      } else if (this.pageData.承認種類 == this.ApprovalType.発注承認) {
        dataTbl = this.tableData.filter(x => this.selectedHatchuShoninRowKeys.includes(x.key))
      } else if (this.pageData.承認種類 == this.ApprovalType.支払承認) {
        dataTbl = this.tableData.filter(x => this.selectedShiharaiShoninRowKeys.includes(x.key))
      }

      for (let i = 0; i < dataTbl.length; i++) {
        const row = dataTbl[i]
        resData.push({
          会社コード: this.userInfo.会社コード,
          工事依頼No: row.工事依頼NO,
          工事依頼No枝番: row.工事依頼NO枝番,
          サブNo: row.サブNo,
          種類: row.種類,
          履歴No: row.履歴No,
          発注サブNo: row.発注サブNo,
          検収回数: row.検収回数,
          明細No: row.明細No,
          契約年月: row.契約年月,
          契約NO: row.契約NO,
          VERSION: row.FW042VERSION // ハノイ側修正2023/08/01　STEP2_W　課題管理表№333：設計書「修正履歴」シートの「2023/04/24仕様変更分」「更新時チェックを追加。」
        })
      }
      return resData
    },
    defaultPagedata () {
      return {
        承認種類: '',
        対象: '',
        対象_履歴: '',
        店コード: '',
        営業店名: '',
        申請日_開始日: '',
        申請日_終了日: '',
        申請者コード: '',
        申請者名: '',
        工事区分: '',
        物件名: '',
        手配先: '',
        手配先名: '',
        工事件名: '',
        物件区分: '',
        工事完了日_開始日: '',
        工事完了日_終了日: '',
        依頼内容: '',
        申請部署名: '',
        事務所コード: '',
        営業所コード: '',
        部コード: '',
        課コード: '',
        係コード: '',
        有り: '',
        無し: '',
        請求内容: '',
        承認者コード: '',
        承認者名: '',
        修繕_定期: '全て',
        工事依頼No: '',
        // ハノイ側修正2022/11/23　STEP2_W　課題管理表№223：設計書「修正履歴」シートの「2022/11/21仕様変更分」「検索項目に「按分」「業者状態」追加, 検索項目指定時の条件追加、修正」Start
        按分有り: '',
        按分無し: '',
        請求合意待ち: '',
        請求合意済: '',
        // ハノイ側修正2022/11/23　STEP2_W　課題管理表№223：設計書「修正履歴」シートの「2022/11/21仕様変更分」「検索項目に「按分」「業者状態」追加, 検索項目指定時の条件追加、修正」End
        請求合意不要: '', // ハノイ側修正2022/12/27　STEP2_W　課題管理表№258：設計書「修正履歴」シートの「2022/12/26仕様変更分」「検索項目「業者状態」に請求合意不要追加、承認種類＝「61：支払確認」の場合　業者状態表示条件追記」
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 Start
        確認済: '',
        未確認: ''
        // ハノイ側修正2022/12/30　STEP2_W　課題管理表№254：設計書「修正履歴」シートの「2022/12/15仕様変更分」「承認種類＝「61：支払承認」	部門確認追加」 End
      }
    },
    defaultTableData () {
      return {
        key: '',
        工事依頼NO: '',
        申請日: '',
        工事区分: '',
        営業店: '',
        物件名: '',
        物件区分: '',
        工事件名: '',
        ステータス: '',
        日付: '',
        承認者: '',
        依頼内容_不具合状況: '',
        見積金額: '',
        手配先: '',
        発注金額: '',
        金額: '',
        請求内容: '',
        支払先: '',
        申請者: '',
        申請部署: '',
        不備有無: ''
      }
    },
    reafsmodalSearchTorihikisakiBack (e) {
      this.pageData.手配先 = e.txtCd // + '-' + e.row.枝番
      this.pageData.手配先名 = e.lblNm
      if (this.pageData.手配先) {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/GetDataN003', { 手配先コード: this.pageData.手配先 })
          .then(async (response) => {
            if (response.data && response.data.名称取得方法03) {
              this.pageData.手配先名 = response.data.名称取得方法03

              // if (e.event && e.event.target === this.$refs.ref営業店.eigyotenBangoEl()) {
              //   if (!await this.Check営業店Exist()) {
              //   }
              // }
            } else {
              this.pageData.手配先名 = ''
              let messCode = this.pageData.承認種類 == this.ApprovalType.支払承認 ? 'E040695' : 'E040756'
              this.getMsg(messCode).then((response) => {
                this.showError(response.data, () => { this.$refs.ref手配先.focus() })
              })
            }
          })
      }
    },
    reafsmodalSearchShainBack (e) {
      this.pageData.申請者コード = e.txtCd
      this.pageData.申請者名 = e.lblNm

      if (this.pageData.申請者コード) {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/GetDataN021', { 申請者コード_承認者コード: this.pageData.申請者コード })
          .then(async (response) => {
            if (response.data && response.data.名称取得方法21) {
              this.pageData.申請者名 = response.data.名称取得方法21
            } else {
              this.pageData.手配先名 = ''
              this.getMsg('E010353').then((response) => {
                this.showError(response.data, () => { this.$refs.ref申請者.focus() })
              })
            }
          })
      } else {
        this.pageData.申請者コード = ''
        this.pageData.申請者名 = ''
      }
    },
    async reafsmodalBackShinseiBusho (e) {
      this.pageData.事務所コード = e.事務所コード
      this.pageData.営業所コード = e.営業所コード
      this.pageData.部コード = e.部コード
      this.pageData.課コード = e.課コード
      this.pageData.係コード = e.係コード
      this.pageData.申請部署名 = e.部署名

      if (this.pageData.事務所コード &&
      this.pageData.営業所コード &&
      this.pageData.部コード &&
      this.pageData.課コード &&
      this.pageData.係コード &&
      !await this.Check申請部署Exist()) {
      }
    },
    async reafsmodalBackEigyoten (e) {
      this.pageData.店コード = e.eigyotenCd
      this.pageData.営業店名 = e.eigyotenNm
      if (this.pageData.店コード) {
        await this.Check営業店Exist()
      } else {
        this.pageData.営業店名 = ''
      }
    },
    reafsmodalSearchShoninShaBack (e) {
      this.pageData.承認者コード = e.txtCd
      this.pageData.承認者名 = e.lblNm

      if (this.pageData.承認者コード) {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/GetDataN021', { 申請者コード_承認者コード: this.pageData.承認者コード })
          .then(async (response) => {
            if (response.data && response.data.名称取得方法21) {
              this.pageData.承認者名 = response.data.名称取得方法21
            } else {
              this.pageData.承認者名 = ''
              this.getMsg('E040757').then((response) => {
                this.showError(response.data, () => { this.$refs.ref承認者.focus() })
              })
            }
          })
      } else {
        this.pageData.承認者コード = ''
        this.pageData.承認者名 = ''
      }
    },
    Check営業店Exist () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/Check営業店Exist', { 店コード: this.pageData.店コード })
          .then((response) => {
            if (!response.data || response.data != 1) {
              this.getMsg('E040009').then((response) => {
                this.showError(response.data, () => { this.$refs.ref営業店.focus() })
              }).finally(() => {
              })
              return resolve(false)
            } else {
              return resolve(true)
            }
          })
      })
    },
    Check承認者Exist () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/Check承認者Exist', { 承認者コード: this.pageData.承認者コード })
          .then((response) => {
            if (!response.data || response.data != 1) {
              this.getMsg('E040757').then((response) => {
                this.showError(response.data, () => { this.$refs.ref承認者.focus() })
              }).finally(() => {
              })
              return resolve(false)
            } else {
              return resolve(true)
            }
          })
      })
    },
    Check申請部署Exist () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/Check申請部署Exist', {
            申請部署コード: this.pageData.事務所コード,
            申請部署コード2: this.pageData.営業所コード,
            申請部署コード3: this.pageData.部コード,
            申請部署コード4: this.pageData.課コード,
            申請部署コード5: this.pageData.係コード
          })
          .then((response) => {
            if (!response.data || response.data != 1) {
              this.getMsg('E040463').then((response) => {
                this.showError(response.data, () => { this.$refs.ref申請部署.focus() })
              })
              return resolve(false)
            } else {
              return resolve(true)
            }
          })
      })
    },
    Check支払先Exist () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/Check支払先Exist', {
            手配先コード: this.pageData.手配先
          })
          .then((response) => {
            if (!response.data || response.data != 1) {
              const messCode = this.pageData.承認種類 == this.ApprovalType.支払承認 ? 'E040695' : 'E040756'
              this.getMsg(messCode).then((response) => {
                this.showError(response.data, () => { this.$refs.ref手配先.focus() })
              })
              return resolve(false)
            } else {
              return resolve(true)
            }
          })
      })
    },
    Check申請者Exist () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/Check申請者Exist', {
            申請者: this.pageData.申請者コード
          })
          .then((response) => {
            if (!response.data || response.data != 1) {
              const messCode = 'E010353'
              this.getMsg(messCode).then((response) => {
                this.showError(response.data, () => { this.$refs.ref申請者.focus() })
              })
              return resolve(false)
            } else {
              return resolve(true)
            }
          })
      })
    },
    async GetStartInfo () {
      // Get Dropdown
      await this.http
        .get('/api/Reafs_W/Shonin/WD00510/GetStartInfo', 'アクセスしています....')
        .then((response) => {
          this.承認種類List = response.data.名称取得方法01
          this.工事区分List = response.data.名称取得方法22
          this.物件区分List = response.data.名称取得方法23
        })
    },

    async searchValidate () {
      // 承認種類
      if (!this.pageData.承認種類) {
        this.getMsg('E040387').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref承認種類.focus()
        return false
      }

      // 営業店
      if (!!this.pageData.店コード && !await this.Check営業店Exist()) {
        return false
      }

      // 承認者コード
      if (!!this.pageData.承認者コード && !await this.Check承認者Exist()) {
        return false
      }

      // 取引先コード 支払先コード
      if (this.pageData.手配先 && !await this.Check支払先Exist()) {
        return false
      }

      // 申請者コード
      if (this.pageData.申請者コード && !await this.Check申請者Exist()) {
        return false
      }

      // 申請部署
      if (this.pageData.事務所コード &&
      this.pageData.営業所コード &&
      this.pageData.部コード &&
      this.pageData.課コード &&
      this.pageData.係コード) {
        if (!await this.Check申請部署Exist()) {
          return false
        }
      } else if (this.pageData.事務所コード ||
      this.pageData.営業所コード ||
      this.pageData.部コード ||
      this.pageData.課コード ||
      this.pageData.係コード) {
        this.getMsg('E040463').then((response) => {
          this.showError(response.data, () => { this.$refs.ref申請部署.focus() })
        })
        return false
      }

      // 申請日
      if (this.pageData.申請日_開始日 && moment(this.pageData.申請日_開始日).isValid() !== true) {
        this.getMsg('E040696').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref申請日_開始日.focus()
        return false
      }
      if (this.pageData.申請日_終了日 && moment(this.pageData.申請日_終了日).isValid() !== true) {
        this.getMsg('E040697').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref申請日_終了日.focus()
        return false
      }
      if (this.pageData.申請日_開始日 && this.pageData.申請日_終了日 &&
        this.pageData.申請日_開始日 > this.pageData.申請日_終了日) {
        this.getMsg('E040723').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref申請日_終了日.focus()
        return false
      }

      if (this.pageData.承認種類 == this.ApprovalType.工事完了確認) {
        // 完了日
        if (this.pageData.工事完了日_開始日 && moment(this.pageData.工事完了日_開始日).isValid() !== true) {
          this.getMsg('E040268').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref工事完了日_開始日.focus()
          return false
        }
        if (this.pageData.工事完了日_終了日 && moment(this.pageData.工事完了日_終了日).isValid() !== true) {
          this.getMsg('E040269').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref工事完了日_終了日.focus()
          return false
        }
        if (this.pageData.工事完了日_開始日 && this.pageData.工事完了日_終了日 &&
            this.pageData.工事完了日_開始日 > this.pageData.工事完了日_終了日) {
          this.getMsg('E040270').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref工事完了日_終了日.focus()
          return false
        }
      }

      return true
    },
    async onSearch (isScreenTop) {
      if (!await this.searchValidate()) {
        return
      }
      this.disable部門確認 = true
      this.disable差戻 = true
      this.disable否認 = true
      this.disable承認 = true
      this.確認金額計 = ''
      this.tableData = []
      this.selectedHatchuShoninRowKeys = []
      this.selectedBumonKakuninRowKeys = []
      this.selectedShiharaiShoninRowKeys = []
      const data = JSON.parse(JSON.stringify(this.pageData))
      // ハノイ側修正2022/12/07　STEP2_W　課題管理表№236：設計書「修正履歴」シートの「2022/12/05仕様変更分」「検索条件の「申請日」、「工事完了日」の指定方を変更する。」Start
      data.申請日_開始日 = data.申請日_開始日
      data.申請日_終了日 = data.申請日_終了日

      data.工事完了日_開始日 = data.工事完了日_開始日
      data.工事完了日_終了日 = data.工事完了日_終了日
      // ハノイ側修正2022/12/07　STEP2_W　課題管理表№236：設計書「修正履歴」シートの「2022/12/05仕様変更分」「検索条件の「申請日」、「工事完了日」の指定方を変更する。」End

      // ハノイ側修正2023/07/31　STEP2_W　課題管理表№373：設計書「修正履歴」シートの「2023/07/28仕様変更分」「別画面から遷移時専用の処理を追記」START
      this.isScreenTop = isScreenTop
      if (this.isScreenTop) {
        data.isScreenTop = this.$route.query.承認種類
      } else {
        data.isScreenTop = ''
      }
      // ハノイ側修正2023/07/31　STEP2_W　課題管理表№373：設計書「修正履歴」シートの「2023/07/28仕様変更分」「別画面から遷移時専用の処理を追記」END

      data.ログイン会社コード = this.userInfo.会社コード
      let result
      await this.http
        .post(
          '/api/Reafs_W/Shonin/WD00510/GetPageData',
          data,
          'アクセスしています....'
        )
        .then((res) => {
          result = res
        })
        .finally(() => {
        })
      if (result.status) {
        if ((!result.data || result.data.length <= 0) && !this.onResearch) { // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」
          this.getMsg('E000014').then((response) => {
            this.showError(response.data)
          })
          this.searchedParam = JSON.parse(JSON.stringify(this.pageData))
          this.$refs.ref検索.$el.focus()
        } else {
          this.onResearch = false // ハノイ側修正2022/10/21　STEP2_W　課題管理表№184：設計書「修正履歴」シートの「2022/10/20仕様変更分」「処理後の再読込時におけるメッセージ表示仕様を変更」
          this.searchedParam = JSON.parse(JSON.stringify(this.pageData))
          result.data.map((x, i) => {
            x.key = i + 1
            return x
          })
          this.tableData = result.data

          // this.$nextTick(() => {
          //   if (this.sessionPageData) {
          //     document.querySelector('.ve-table-container').scrollTop = this.sessionPageData.scrollTop || 0
          //     this.sessionPageData = null
          //   }
          // })
        }
        this.否認差戻事由 = ''
      } else {
        this.showError(result.message)
      }
    },
    onChangeKojiKanryoBiShuryoBi () {
      if (this.pageData.工事完了日_開始日 && this.pageData.工事完了日_終了日 &&
          moment(this.pageData.工事完了日_開始日) > moment(this.pageData.工事完了日_終了日)) {
        this.getMsg('E040270').then((response) => {
          this.showError(response.data)
        })
        this.$nextTick(() => {
          this.$refs.ref工事完了日_終了日.focus()
        })
        return false
      }
    },
    onChangeShinseiBi () {
      if (this.pageData.申請日_開始日 && this.pageData.申請日_終了日 &&
          moment(this.pageData.申請日_開始日) > moment(this.pageData.申請日_終了日)) {
        this.getMsg('E040723').then((response) => {
          this.showError(response.data)
        })
        this.$nextTick(() => {
          this.$refs.ref申請日_終了日.focus()
        })
        return false
      }
    },
    onBack () {
      // this.$router.push({ name: 'Whome' })
      this.$router.push({ name: 'WD00010' })// home画面のname指定が異なるため
    },
    showError (message, callback, title = this.$store.getters.getPageTitle()) {
      this.MsgErr({
        title,
        message
      }, callback)
    },
    showSuccess (message, title = this.$store.getters.getPageTitle()) {
      this.MsgInfo({
        title,
        message,
        callback: function (a, b) {
        }
      })
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
    getCheckログイン情報 (事務所コード, 営業所コード, 部コード, 課コード, 係コード) {
      return new Promise((resolve) => {
        this.http.get('/api/Reafs_W/Shonin/WD00510/GetCheckログイン情報', 'アクセスしています....')
          .then((response) => {
            if (response.data.事務所コード === 事務所コード && response.data.営業所コード === 営業所コード &&
            response.data.部コード === 部コード && response.data.課コード == 課コード && response.data.係コード === 係コード) {
              return resolve(true)
            } else {
              return resolve(false)
            }
          })
      })
    },
    // ハノイ側修正2023/05/25　STEP2_W　課題管理表№342：設計書「修正履歴」シートの「2023/05/24仕様変更分」「メニューの別のリンク押下後、稟議申請の差戻件数リンク押下時、稟議申請待ちの条件で検索されてしまう。」Start
    onChange承認種類 () {
      this.$route.query.承認種類 = this.pageData.承認種類
      this.$route.query.対象履歴 = this.対象List[0].value
    }
    // ハノイ側修正2023/05/25　STEP2_W　課題管理表№342：設計書「修正履歴」シートの「2023/05/24仕様変更分」「メニューの別のリンク押下後、稟議申請の差戻件数リンク押下時、稟議申請待ちの条件で検索されてしまう。」End
  }
}
</script>
<style lang="less" scoped>
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.el-form-item {
  margin-bottom: 0px;
}

.home-container {
  padding-left: 10px;
}

.MishoninShoninIchiran .MishoninShoninIchiranForm {
  width: 100%;
}

.clsHansu button {
  padding: 1px 4px;
  font-size: 20px !important;
  margin-top: 1px;
  margin-left: 0px;
}

.is-red {
  color: red;
}

.commentContent {
  padding: 1px 0 1px 0;
}

.item_label {
  padding-left: 15px;
  max-width: 407px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.home-el-header {
  padding: 0 0 20px 0;
}

.home-el-main {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
  margin-top: 10px;
}

.home-el-footer {
  padding: 10px 0 0 0;
}

.border-top {
  border-top: 1px solid #dcdfe6;
}

.custome-cell-header {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  align-items: center;
  display: flex;
  text-align: center;
}

.border-right {
  border-right: 1px solid #000 !important;
}

.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: left;
}

.clsSeikyu div {
  position: relative;
}

.clsSeikyu div span:first-child {
  height: 27px;
  display: inline-block;
  position: absolute;
  top: -1px;
  left: 0;
  width: 30%;
}

.clsSeikyu div span:last-child {
  height: 27px;
  display: inline-block;
  padding-left: 5px;
  position: absolute;
  top: -1px;
  right: 0;
  width: 70%;
}

.tblDetail button {
  height: 96%;
  width: 96%;
}

.item_label_HininSashimodoshiJiyu {
  height: 53px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 1px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white;
  padding: 10px 0 0 0;
  box-sizing: border-box;
}
.item_input_HininSashimodoshiJiyu {
  width: calc(100% - 200px);
}
.red {
  color: red;
  font-weight: bold;
}
.black {
  color: black;
}
#業者状態 .el-checkbox {
  margin-right: 7px !important;
}
</style>

<style>
.MishoninShoninIchiran #業者状態 .el-form-item__content {
  margin-left: 105px !important;
}
.MishoninShoninIchiran .is-disabled,
.is-disabled * {
  pointer-events: none;
  opacity: 0.9;
}
/* ハノイ側修正2022/12/09　STEP2_W　課題管理表№234：「2022/12/01依頼分」テキストエリアが非活性になっている場合、スクロールバーが動作しない。START*/
.el-textarea.is-disabled * {
  pointer-events: all;
}
/* ハノイ側修正2022/12/09　STEP2_W　課題管理表№234：「2022/12/01依頼分」テキストエリアが非活性になっている場合、スクロールバーが動作しない。END*/

.MishoninShoninIchiran .cell-body-custome {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.MishoninShoninIchiran .cell-body-custome div:not(:last-child) {
  border-right: solid 1px #000;
}

.MishoninShoninIchiran .text-center {
  text-align: center !important;
  vertical-align: middle !important;
}

.MishoninShoninIchiran .text-left {
  text-align: left !important;
}

.MishoninShoninIchiran .text-right {
  text-align: right !important;
}

.MishoninShoninIchiran .position-relative {
  position: relative;
}

.MishoninShoninIchiran .el-checkbox-group .el-checkbox:first-child,
.MishoninShoninIchiran .four .el-checkbox {
  margin-left: 10px !important;
}

.MishoninShoninIchiran .el-date-editor.el-input,
.MishoninShoninIchiran .el-date-editor.el-input__inner {
  width: 135px;
}

.MishoninShoninIchiran .text-ellipsis input {
  text-overflow: ellipsis;
  overflow: hidden;
}

.MishoninShoninIchiran .ve-table-content .ve-table-body tr:not([style*="height: 0px"]),
.MishoninShoninIchiran .ve-table-footer-tr {
  height: 30px !important;
}
.MishoninShoninIchiran .ve-table-content colgroup col:not([style*="width"]) {
  min-width: 200px;
}

.MishoninShoninIchiran .tblDetail .ve-table-body .el-input__inner {
  height: 29px !important;
  padding: 0 5px;
}
.MishoninShoninIchiran .ve-table-footer-tr td {
  padding: 3px !important;
}

.MishoninShoninIchiran .tableRitsu .ve-table-container {
  overflow: hidden;
  height: 200px !important;
}

.MishoninShoninIchiran .tableRitsu .ve-table-fixed-header .ve-table-header-tr th {
  text-align: center !important;
}

.MishoninShoninIchiran .tblDetail .ve-table-container {
  min-height: 581px;
  max-height:581px !important;
}
.MishoninShoninIchiran .tblDetail.shiharaiShonin .ve-table-container {
  min-height: 505px;
  max-height:505px !important;
}
.MishoninShoninIchiran .tblDetail.hatchuShonin .ve-table-container {
  min-height: 494px;
  max-height:494px !important;
}

.MishoninShoninIchiran .tblDetail input{
  color: #000 !important
}

/* .el-form-item .el-form-item__label {
  width: 110px !important;
}
.el-form-item .el-form-item__content {
  margin-left: 110px !important;
} */

.MishoninShoninIchiran .el-radio-group .el-radio:first-child {
  margin-left: 5px;
}
.MishoninShoninIchiran .el-radio-group .is-disabled .el-radio__inner {
  border-color: #7f8083;
}
.MishoninShoninIchiran .el-radio-group .is-disabled .el-radio__label {
  color: black !important;
}
.MishoninShoninIchiran .el-radio__label {
  padding-left: 2px !important;
}
.MishoninShoninIchiran .el-radio {
  margin-right: 16px !important;
}
.MishoninShoninIchiran .openseach-txt .reafs-input{
  width: 108px !important;
}
.MishoninShoninIchiran .openseach-txt button, .btn-icon button {
  padding-left: 12px;
  padding-right: 12px;
}
.MishoninShoninIchiran .openseach-txt .append-background {
  width: 198px !important;
}
.MishoninShoninIchiran .el-form-item.hininSashimodoshiJiyu .el-form-item__content {
  margin-left: 0px !important;
}
.MishoninShoninIchiran .custom-date-picker input {
  padding-right: 26px !important;
}
.MishoninShoninIchiran .button-multi-code .reafs-input{
  width: 108px !important;
}
.MishoninShoninIchiran .ve-table .ve-table-container table.ve-table-content thead.ve-table-header .ve-table-header-tr .ve-table-header-th.ve-table-fixed-left{
 z-index: 10 !important;
}

.MishoninShoninIchiran .hatchuShonin table .el-checkbox__inner,
.MishoninShoninIchiran .shiharaiShonin table .el-checkbox__inner {
  width: 16px !important;
  height: 16px !important;
}

.MishoninShoninIchiran .hatchuShonin table .checkBoxDisabled .el-checkbox__inner,
.MishoninShoninIchiran .shiharaiShonin table .checkBoxDisabled .el-checkbox__inner {
  border: 1px solid #DCDFE6;
  background-color: #f7f7f7;
  cursor: not-allowed;
}

.MishoninShoninIchiran .hatchuShonin table td .el-checkbox:not(.checkBoxDisabled):not(.is-checked) .el-checkbox__inner,
.MishoninShoninIchiran .shiharaiShonin table td .el-checkbox:not(.checkBoxDisabled):not(.is-checked) .el-checkbox__inner {
  border: 1px solid #abbacc;
}

.MishoninShoninIchiran .hatchuShonin table td .is-checked .el-checkbox__inner::after,
.MishoninShoninIchiran .hatchuShonin table th .is-checked .el-checkbox__input:not(.is-indeterminate) .el-checkbox__inner::after,
.MishoninShoninIchiran .shiharaiShonin table td .is-checked .el-checkbox__inner::after,
.MishoninShoninIchiran .shiharaiShonin table th .is-checked .el-checkbox__input:not(.is-indeterminate) .el-checkbox__inner::after {
  -webkit-transform: rotate(45deg) scale(1);
  transform: rotate(45deg) scale(1);
  position: absolute;
  display: table;
  border: 2px solid #fff;
  border-top: 0;
  border-left: 0;
  content: " ";
  -webkit-transition: all 0.2s cubic-bezier(0.12, 0.4, 0.29, 1.46) 0.1s;
  transition: all 0.2s cubic-bezier(0.12, 0.4, 0.29, 1.46) 0.1s;
  width: 6px;
  height: 10px;
}

.MishoninShoninIchiran .hatchuShonin table th .is-indeterminate .el-checkbox__inner,
.MishoninShoninIchiran .shiharaiShonin table th .is-indeterminate .el-checkbox__inner {
  background-color: white;
  border-color: white;
}

.MishoninShoninIchiran .hatchuShonin table th .is-checked .el-checkbox__inner,
.MishoninShoninIchiran .shiharaiShonin table th .is-checked .el-checkbox__inner {
  background-color: #108ee9;
  border-color: #108ee9;
}

.MishoninShoninIchiran .hatchuShonin table th .is-indeterminate .el-checkbox__inner::before,
.MishoninShoninIchiran .shiharaiShonin table th .is-indeterminate .el-checkbox__inner::before {
  height: 16px;
  width: 16px;
  top: -1px;
  left: -1px;
  background-color: #108ee9;
  border: none;
}

</style>
