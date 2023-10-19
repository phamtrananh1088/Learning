<template>
  <div class="home-container MitsumoriNyuryokuShuzen">
    <el-form :model="pageData" ref="refForm" label-width="120px" class="sample-form-class MitsumoriNyuryokuShuzenForm"
      :hide-required-asterisk="true">
      <el-container class="home-el-container">
        <!-- <el-header height="auto" class="home-el-header">
        </el-header> -->
        <el-main class="home-el-main">
          <!-- 1 -->
          <el-row style="display: flex; align-items: center">
            <el-col :span="5">
              <div>
                <el-form-item label="見積№" prop="表示見積No" class="is-readonly">
                  <reafsinputtext disabled v-model="pageData.表示見積No" ref="ref表示見積No" :width="'140px'" class="text-ellipsis">
                  </reafsinputtext>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="4">
              <el-form-item label="版数" prop="版数" class="is-required">
                <div style="display: flex" class="clsHansu">
                  <reafsinputtext required v-model="pageData.版数" maxlength="1" width="48px"
                    @keypress.native="keypressTextbox($event)" @blur="onHansuBlur" @focus="onHansuFocus" ref="ref版数">
                  </reafsinputtext>
                  <el-button :disabled="(pageData.版数 || '0') <= 1" size="mini" @click="changeHansu(true)"><i
                      class="el-icon-caret-left" />
                  </el-button>
                  <el-button :disabled="(pageData.版数 || '0') >= 9" size="mini" @click="changeHansu()"><i
                      class="el-icon-caret-right" />
                  </el-button>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="1">
              <div v-if="pageData.提出フラグ == '1'" style="display: flex; justify-content: center">
                <span v-if="pageData.確定フラグ == '1'" class="is-red">最新</span>
                <span v-else-if="pageData.確定フラグ != '1'">履歴</span>
              </div>
              <div v-else>&nbsp;</div>
            </el-col>
            <el-col :span="1">
              <el-button type="primary" size="mini" @click="onSearch">検索</el-button>
            </el-col>
            <el-col :span="11">
              <el-form-item label="工事№" prop="工事No" class="is-readonly">
                <reafsinputtext disabled v-model="pageData.工事No" ref="ref工事No" :width="'115px'" class="text-ellipsis">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="2">
              <reafsinputtext style="display: none;" v-model="pageData.見積No" :width="'115'">
              </reafsinputtext>
          </el-col>
          </el-row>
          <!-- 2 -->
          <el-row>
            <el-col :span="5">
              <el-form-item label="提出先" prop="提出先" class="is-readonly">
                <reafsinputtext disabled :title="pageData.提出先" v-model="pageData.提出先" ref="ref提出先" :width="'140px'"
                  class="text-ellipsis">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="依頼者" class="is-readonly">
                <reafsinputtext disabled :title="pageData.依頼者" v-model="pageData.依頼者" ref="ref依頼者" :width="'89%'"
                  class="text-ellipsis">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <div style="display: flex; flex-flow: column">
                <div>
                  <span>見積依頼日:</span>
                  <span>{{ pageData.見積依頼日 }}</span>
                </div>
                <div>
                  <span>工事予定日:</span>
                  <span>{{ pageData.工事予定日_開始日 }}{{ pageData.工事予定日_開始日 ? '～' : '' }}{{
                      pageData.工事予定日_終了日
                  }}</span>
                </div>
              </div>
            </el-col>
            <el-col :span="3">
              <div style="display: flex; position: relative">
                <span>状態:</span>
                <span>{{ pageData.状態 }}</span>
              </div>
            </el-col>
          </el-row>
          <!-- 3 -->
          <el-row style="display: flex; align-items: center">
            <el-col :span="5">
              <div>
                <el-form-item label="物件" prop="物件" class="is-readonly">
                  <reafsinputtext disabled v-model="pageData.物件_コード" ref="ref物件_コード" :width="'140px'"
                    class="text-ellipsis">
                  </reafsinputtext>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="5" class="is-readonly">
              <reafsinputtext :title="pageData.物件_名称" disabled v-model="pageData.物件_名称" ref="ref物件_名称" :width="'280px'"
                class="text-ellipsis">
              </reafsinputtext>
            </el-col>
            <el-col :span="5">
              <el-form-item label="物件住所" prop="物件住所" class="is-readonly">
                <reafsinputtext disabled v-model="pageData.物件住所_郵便番号" ref="ref物件住所_郵便番号" :width="'115px'"
                  class="text-ellipsis">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="9" class="is-readonly">
              <reafsinputtext disabled :title="pageData.物件住所" v-model="pageData.物件住所" ref="ref物件住所" :width="'480px'"
                class="text-ellipsis">
              </reafsinputtext>
            </el-col>
          </el-row>
          <!-- 4 -->
          <el-row>
            <el-col :span="12">
              <el-form-item label="工事件名" prop="工事件名">
                <reafsinputtext :disabled="isDisplayModeKoji" maxlength="60" v-model="pageData.工事件名" ref="ref工事件名"
                  width="90%">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="見積時作業予定日" prop="見積時作業予定日">
                <div style="display: flex">
                  <el-date-picker :disabled="isDisplayMode" v-model="pageData.見積時作業予定日_開始日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker" ref="ref見積時作業予定日_開始日" />
                  <span style="margin: 0 2px">～</span>
                  <el-date-picker :disabled="isDisplayMode" v-model="pageData.見積時作業予定日_終了日" type="date"
                    format="yyyy/MM/dd" value-format="yyyy/MM/dd" class="date-picker" ref="ref見見積時作業予定日_終了日" />
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 5 -->
          <el-row>
            <el-col :span="10">
              <div v-if="pageData.緊急フラグ == '1' && pageData.受付フラグ == '0'">
                <el-form-item label="見積有効期間" prop="見積有効期間">
                  <el-date-picker :disabled="isDisplayMode" v-model="pageData.見積有効期間" type="date" format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd" class="date-picker" ref="ref見積有効期間" />
                </el-form-item>
              </div>
              <div v-else>
                <el-form-item label="見積有効期間" prop="見積有効期間" class="is-required">
                  <el-date-picker required :disabled="isDisplayMode" v-model="pageData.見積有効期間" type="date" format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd" class="date-picker" ref="ref見積有効期間" />
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="10">
              <el-form-item label="見積担当者" prop="見積担当者" class="is-required">
                <reafsinputtext required :disabled="isDisplayMode" maxlength="20" v-model="pageData.見積担当者" ref="ref見積担当者"
                  width="89%">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button class="color-blue" @click="onIraiNaiyoHyojiClick">依頼内容表示
              </el-button>
              <screenPopup src="/Reafs_T/IraiNaiyoSabu" :params="params" :dialogVisible.sync="showIraiNaiyoSabu"
                @closePopupEvent="IraiNaiyoSabuClose"></screenPopup>
            </el-col>
          </el-row>
          <!-- 6 -->
          <el-row style="display: flex; align-items: end;">
            <el-col :span="5">
              <div>
                <el-form-item label="見積金額" prop="見積金額" class="is-readonly input-font-20">
                  <reafsinputtext disabled v-model="pageData.見積金額" ref="ref見積金額"
                    width="196px">
                  </reafsinputtext>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="5">
              <span style="font-size: 19px;margin-left: 28px;">（税込）</span>
            </el-col>
            <el-col :span="10">
              <el-form-item label="担当者連絡先" prop="担当者連絡先" class="is-required">
                <reafsinputtext required :disabled="isDisplayMode" maxlength="14" v-model="pageData.担当者連絡先" ref="ref担当者連絡先"
                  width="225px">
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24" style="display: flex">
              <span class="is-red">{{ mitsumori }}</span>
              <div v-if="pageData.緊急フラグ == '1' && pageData.受付フラグ == '1'">
                <span class="is-red">　/　【緊急依頼】本見積時は概算金額を上書きしてください。</span>
              </div>
              <div v-else-if="pageData.緊急フラグ == '1' && pageData.受付フラグ == '0'">
                <span class="is-red">　/　【緊急依頼】
                  <span v-if="pageData.依頼状況 == '02'" style="color: #0000FF;">コールセンター受付済です。</span>
                  10万円以上の作業は概算金額を入力してください。10万円未満の作業は依頼受付をクリックしてください。</span>
              </div>
            </el-col>
          </el-row>
          <el-row style="margin: 0; display: flex; align-items: end">
            <el-col :span="2">&nbsp;</el-col>
            <el-col :span="8">
              <el-button :disabled="isDisplayMode" size="mini" @click="onAddRowMeisai">5行追加
              </el-button>
            </el-col>
            <el-col :span="6">
              <el-form-item label="過去見積参照" prop="過去見積参照">
                <div style="display: flex; align-items: center">
                  <reafsinputtext :maxlength="12" :disabled="isDisplayMode" v-model="pageData.過去見積参照" ref="ref過去見積参照"
                    width="150px">
                  </reafsinputtext>
                  <el-button :disabled="isDisplayMode" icon="el-icon-search" size="mini" @click="showTS00300 = !showTS00300"></el-button>
                </div>
              </el-form-item>
              <screenPopup src="/Reafs_T/KakoMitsumoriSanshoSabu" :params="{ 修繕定期区分: '1' }"
                :dialogVisible.sync="showTS00300" @onBackData="KakoMitsumoriSanshoSabuonBackData"></screenPopup>
            </el-col>
            <el-col :span="8">
              <el-button size="mini" @click="onReference" :disabled="isDisplayMode">参照検索
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <reafstable ref="table" :tableData="tableDataMeisai" :columns="columnsMeisai" :maxHeight="340"
              :cellStyleOption="cellStyleOptionMeisai" rowKeyFieldName="key" class="tblMeisai">
            </reafstable>
          </el-row>
          <el-row>
            <el-button :disabled="isDisplayMode" @click="onRitsuUpdateClick" style="display: none; float: right">更新
            </el-button>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-row style="margin-bottom: 29px;">
                <el-col :span="24">
                  <el-form-item label="備考" prop="備考" class="clsTextArena">
                    <reafsinputtext :disabled="isDisplayMode" maxlength="200" v-model="pageData.備考" ref="ref備考"
                      width="90%" type="textarea">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="margin-bottom: 29px;" >
                <el-col :span="24">
                  <el-form-item label="ＳＧリアルティへの連絡事項" prop="ＳＧリアルティへの連絡事項" class="clsTextArena">
                    <reafsinputtext :disabled="isDisplayMode" maxlength="200" :title="pageData.ＳＧリアルティへの連絡事項" v-model="pageData.ＳＧリアルティへの連絡事項" ref="refＳＧリアルティへの連絡事項"
                      width="90%" type="textarea" class="text-ellipsis" :autosize="false">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="margin: 0">
                <el-col :span="24">
                  <el-form-item label="ＳＧリアルティより連絡事項" prop="ＳＧリアルティより連絡事項" class="clsTextArena is-readonly resize-none">
                    <reafsinputtext disabled maxlength="200" :title="pageData.ＳＧリアルティより連絡事項" v-model="pageData.ＳＧリアルティより連絡事項" ref="refＳＧリアルティより連絡事項"
                      width="90%" type="textarea" class="text-ellipsis" :autosize="false">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-col>
            <el-col :span="12">
              <div>
                <reafstable class="tableRitsu" :tableData="tableDataRitsu" :columns="columnsRitsu" :maxHeight="192"
                  rowKeyFieldName="key" :footerData="footerDataRitsu">
                </reafstable>
              </div>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="12" style="display: flex; justify-content: space-evenly;">
              <screenPopup src="/Reafs_T/BukkenDokyumentoSanshoSabu" :params="BukkenDokyumentoSanshoSabuParam"
                :dialogVisible.sync="showTS00304"></screenPopup> </screenPopup>
              <el-button class="color-blue" @click="showTS00304 = !showTS00304" style="float: right">物件資料参照
              </el-button>
               <screenPopup src="/Reafs_T/MitsumoriDetaTorikomi" :params="MitsumoriDetaTorikomiParam"
                :dialogVisible.sync="showTS00305" @onBackData="MitsumoriDetaTorikomiOnBackData"></screenPopup>
              <el-button class="color-blue" :disabled="isDisplayMode" @click="onMitsumoriDetaTorikomiClick"
                style="float: right">見積データ取込
              </el-button>
              <el-button class="color-blue" @click="showTS00303 = !showTS00303" style="float: right">
                写真添付
              </el-button>
               <screenPopup src="/Reafs_T/ShashinTenpuSabu" :params="ShashinTenpuSabuParams"
                :dialogVisible.sync="showTS00303"></screenPopup>
              <el-button class="color-blue" @click="showTS00302 = !showTS00302" style="float: right">図面・資料添付
              </el-button>
                <screenPopup src="/Reafs_T/ZumenShiryoTenpuSabu" :params="ZumenShiryoTenpuSabuParams"
                :dialogVisible.sync="showTS00302"></screenPopup>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>
            <el-col :span="4" style="display: flex; justify-content: space-around;">
              <el-button v-if="pageData.緊急フラグ == '1' && pageData.受付フラグ == '0'" :class="isDisplayModeColor"
                :disabled="isDisplayMode" @click="onSaveTemp(mode.依頼受付)" style="float: right">依頼受付</el-button>
              <el-button v-else :class="isDisplayModeColor" :disabled="isDisplayMode" @click="onSaveTemp(mode.一時保存)" style="float: right">
                一時保存
              </el-button>
              <el-button :class="isDisplayModeTeishutuColor" :disabled="isDisplayModeTeishutu" @click="onSubmission" style="float: right">提出
              </el-button>
            </el-col>
            <el-col :span="1">&nbsp;</el-col>
            <el-col :span="6">
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
import opensearch from '@/components/basic/openSearch.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafsinputnumber2 from '@/components/basic/ReafsControl/ReafsInputNumber2.vue'
import screenPopup from '../../common/ScreenPopup/ScreenPopup.vue'
import moment from 'moment'

export default {
  components: {
    reafsinputtext,
    reafstable,
    eigyoten,
    opensearch,
    reafsinputnumber,
    reafsinputnumber2,
    screenPopup,
    moment
  },
  data () {
    return {
      params: {
        工事依頼No: '',
        工事依頼No枝番: '',
        業者コード: '',
        業者コード枝番: ''
      },
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      pageData: this.defaultPagedata(),
      tableDataMeisai: [],
      oldData: [],
      tableDataRitsu: [
        this.defaultDataRitsu(),
        this.defaultDataRitsu(),
        this.defaultDataRitsu(),
        this.defaultDataRitsu()
      ],
      分類List: [
        { name: '', value: '' },
        { name: '見出', value: 'T' },
        { name: '小計', value: 'G' }
      ],
      税区List: [],
      cellStyleOptionMeisai: {
        headerCellClass: ({ row, column, rowIndex }) => { },
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = ''
          if (row.分類 != 'T' && row.分類 != 'G' && column.key * 1 >= 9 &&
            this.isNotEmptyRow(row)) {
            css += ' is-yl'
          } else if (row.分類 == 'T' || row.分類 == 'G') {
            // ハノイ側修正2022/09/08 STEP2_T 課題管理表№13：見積入力(修繕) シートの「2022/09/01仕様変更分」 処理概要シート：＜提出ボタン押下＞のDB更新処理で緊急案件時の分岐処理を追加 START
            // if (column.key == '9') {
            //   css += ' is-yl'
            // } else if (column.key * 1 >= 6) {
            //   css += ' is-disabled'
            // }
            // ハノイ側修正2022/09/08 STEP2_T 課題管理表№13：見積入力(修繕) シートの「2022/09/01仕様変更分」 処理概要シート：＜提出ボタン押下＞のDB更新処理で緊急案件時の分岐処理を追加 END
          }
          // ハノイ側修正2022/09/08 STEP2_T 課題管理表№13：見積入力(修繕) シートの「2022/09/01仕様変更分」 処理概要シート：＜提出ボタン押下＞のDB更新処理で緊急案件時の分岐処理を追加。START
          if (row.分類 != 'T' && row.分類 != 'G' && column.key === '13' && this.getNumber(row.単価)) {
            css += ' is-disabled'
          }
          // ハノイ側修正2022/09/08 STEP2_T 課題管理表№13：見積入力(修繕) シートの「2022/09/01仕様変更分」 処理概要シート：＜提出ボタン押下＞のDB更新処理で緊急案件時の分岐処理を追加。 END
          if (
            ['1', '2', '3', '4', '5', '9', '10', '11', '12', '13', '14', '15'].includes(column.key)
          ) {
            css += ' position-relative'
          }
          if (
            ['6', '7', '8'].includes(column.key)
          ) {
            css += ' is-label'
          }
          // if (this.isDisplayMode) {
          //   css += ' is-disabled'
          // }

          if (column.key == '6' || column.key == '7' || column.key == '8') {
            css += ' text-left'
          }

          if (column.key == '5' || column.key == '14') {
            css += ' border-right-none'
          }

          return css
        }
      },
      columnsMeisai: [
        {
          field: 'No',
          key: '1',
          title: '№',
          width: 40,
          // fixed: 'left',
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
          key: '2',
          title: '順番',
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex" class="cell-body-custome clsHansu">
                <div>
                  <el-button
                    on-click={() => {
                      this.onNextBackMeisai(row, rowIndex, true)
                    }}
                    disabled={this.isDisplayMode}
                    size="mini"
                  >
                    <i class="el-icon-caret-top" />
                  </el-button>
                </div>
                <div>
                  <el-button
                    on-click={() => {
                      this.onNextBackMeisai(row, rowIndex)
                    }}
                    disabled={this.isDisplayMode}
                    size="mini"
                  >
                    <i class="el-icon-caret-bottom" />
                  </el-button>
                </div>
              </div>
            )
          },
          width: 62
          // fixed: 'left'
        },
        {
          key: '3',
          title: 'クリア',
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onDeleteMeisai(row, rowIndex)
                  }}
                  disabled={this.isDisplayMode}
                  size="mini"
                  style="background: #d3d3d3; border-color: #b7b5b5"
                >
                  クリア
                </el-button>
              </div>
            )
          },
          width: 58
          // fixed: 'left'
        },
        {
          key: '4',
          title: 'コピー',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onCopyMeisai(row, rowIndex)
                  }}
                  disabled={this.isDisplayMode}
                  size="mini"
                >
                  &#x21B3;
                </el-button>
              </div>
            )
          },
          width: 45
          // fixed: 'left'
        },
        {
          key: '5',
          title: '分類',
          renderBodyCell: ({ row, column, rowIndex }, h) => {
            return (
              <div class="cell-body-custome">
                <el-select
                  value={row['分類']}
                  placeholder=""
                  style="width: 100%;"
                  disabled={this.isDisplayMode}
                  on-change={(val) => {
                    row['分類'] = val
                    this.onMeisai分類Change(row, column, rowIndex)
                  }}
                >
                  {this.分類List.map(function (obj, i) {
                    return (
                      <el-option label={obj.name} value={obj.value} key={i} />
                    )
                  })}
                </el-select>
              </div>
            )
          },
          width: 75
          // fixed: 'left'
        },
        {
          key: '6',
          title: '作業カテゴリ大区分',
          field: '作業カテゴリ大',
          width: 125,
          // fixed: 'left',
          ellipsis: {
            showTitle: true
          }
        },
        {
          key: '7',
          title: '作業カテゴリ中区分',
          field: '作業カテゴリ中',
          width: 125,
          // fixed: 'left',
          ellipsis: {
            showTitle: true
          }
        },
        {
          key: '8',
          title: '作業カテゴリ小区分',
          field: '作業カテゴリ小',
          width: 125,
          // fixed: 'left',
          ellipsis: {
            showTitle: true
          }
        },
        {
          key: '9',
          title: '作業内容',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputtext
                  maxlength="100"
                  value={row.作業内容}
                  ref={'ref作業内容' + rowIndex}
                  width="100%"
                  disabled={this.isDisplayMode}
                  on-input={(val) => {
                    row['作業内容'] = val
                    // this.cellDataChange(row, column, val)
                  }}
                ></reafsinputtext>
              </div>
            )
          }
        },
        {
          key: '10',
          title: '数量',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber2
                  intLength={6}
                  floatLength={2}
                  value={row.数量}
                  ref={'ref数量' + rowIndex}
                  width="100%"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-input={(val) => {
                    row['数量'] = val
                  }}
                  on-change={() => {
                    this.onMeisai数量Change(row, column, rowIndex)
                  }}
                ></reafsinputnumber2>
              </div>
            )
          },
          width: 95
        },
        {
          key: '11',
          title: '単位',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputtext
                  maxlength={10}
                  value={row.単位}
                  ref={'ref単位' + rowIndex}
                  width="100%"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-input={(val) => {
                    row['単位'] = val
                    // this.cellDataChange(row, column, val)
                  }}
                  on-change={() => {
                    this.onMeisai単位Change(row, column, rowIndex)
                  }}
                ></reafsinputtext>
              </div>
            )
          },
          width: 90
        },
        {
          key: '12',
          title: '単価',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.単価}
                  ref={'ref単価' + rowIndex}
                  width="100%"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-input={(val) => {
                    row['単価'] = val
                    // this.cellDataChange(row, column, val)
                  }}
                  on-change={() => {
                    this.onMeisai単価Change(row, column, rowIndex)
                  }}
                ></reafsinputnumber>
              </div>
            )
          },
          width: 125
        },
        {
          key: '13',
          title: '金額（税抜）',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.金額_税抜}
                  ref={'ref金額_税抜' + rowIndex}
                  width="100%"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-input={(val) => {
                    row['金額_税抜'] = val
                    // this.cellDataChange(row, column, val)
                  }}
                  on-change={() => {
                    this.onMeisai金額_税抜Change(row, column, rowIndex)
                  }}
                ></reafsinputnumber>
              </div>
            )
          },
          width: 125
        },
        {
          key: '14',
          title: '税区',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-select
                  value={row['税区']}
                  placeholder=""
                  style="width: 100%;"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-change={(val) => {
                    row['税区'] = val
                    this.onMeisai税区Change(row, column, rowIndex)
                  }}
                >
                  {this.税区List.map(function (object, i) {
                    return (
                      <el-option
                        label={object.名称}
                        value={object.名称コード}
                        key={i}
                      />
                    )
                  })}
                </el-select>
              </div>
            )
          },
          width: 90
        },
        {
          key: '15',
          title: '税率',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: flex;" class="cell-body-custome">
                <div>
                  <reafsinputnumber
                    intLength={2}
                    floatLength={0}
                    value={row.税率}
                    ref={'ref税率' + rowIndex}
                    disabled={row.TGDisabled || this.isDisplayMode || row['税区'] == '2'}
                    width="100%"
                    on-input={(val) => {
                      row['税率'] = val
                      // this.cellDataChange(row, column, val)
                    }}
                    on-change={() => {
                      this.onMeisai税率Change(row, column, rowIndex)
                    }}
                  ></reafsinputnumber>
                </div>
                <div style="display: flex">
                  <span style="padding: 0 5px;line-height: 25px; width: 25px">{row.分類 != 'G' && row.分類 != 'T' ? '%' : ''}</span>
                </div>
              </div>
            )
          },
          width: 75
        }
      ],
      columnsRitsu: [
        {
          field: '税率',
          key: '1',
          title: '税率',
          width: 50
        },
        {
          field: '合計金額_税抜',
          key: '2',
          title: '合計金額（税抜）',
          align: 'right',
          width: 120
        },
        {
          field: '税額',
          key: '3',
          title: '税額',
          align: 'right',
          width: 120
        },
        {
          field: '合計金額_税込',
          key: '4',
          title: '合計金額（税込）',
          align: 'right',
          vAlign: 'top',
          width: 120
        }
      ],
      maxRowMeisai: 30,
      showIraiNaiyoSabu: false,
      showTS00300: false,
      showTS00304: false,
      showTS00302: false,
      showTS00303: false,
      showTS00305: false,
      mode: Object.freeze({'一時保存': 1, '依頼受付': 2})
    }
  },
  watch: {
    recalMeisai (newValue) {
      this.handleDisplayMeisai()
      this.sumAllMeisai()
      this.sumForRitsu()
    },
    $route (to, from) {
      if (to.name == 'TD00301') {
        this.params = {
          工事依頼No: this.$route.params.工事依頼No,
          工事依頼No枝番: this.$route.params.工事依頼No枝番,
          業者コード: this.$route.params.業者コード,
          業者コード枝番: this.$route.params.業者コード枝番,
          見積書枝番: this.$route.params.見積書枝番
        }
        if (this.params.見積書枝番) {
          this.pageData.版数 = (
            this.params.見積書枝番.substring(2) * 1
          ).toLocaleString('en-US', {
            minimumIntegerDigits: 2,
            useGrouping: false
          })
        }
        let userLogIn = this.$store.getters.getUserInfo()
        this.userInfo.ユーザーＩＤ = userLogIn.userId
        this.userInfo.会社コード = userLogIn.会社コード

        this.tableDataMeisai = []
        for (let i = 0; i < 10; i++) {
          this.tableDataMeisai.push(this.defaultDataMeisai())
        }
        this.GetStartInfo()
        this.onSearch()
      }
    }
  },
  computed: {
    BukkenDokyumentoSanshoSabuParam () {
      if (this.params) {
        return { 物件コード: this.pageData.物件_コード }
      } else return {}
    },
    MitsumoriDetaTorikomiParam () {
      if (this.params) {
        return {
          工事依頼No: this.params.工事依頼No,
          工事依頼No枝番: this.pageData && this.pageData.工事依頼NO枝番 ? this.pageData.工事依頼NO枝番 : this.params.工事依頼NO枝番,
          業者コード: this.params.業者コード,
          業者コード枝番: this.params.業者コード枝番
        }
      }
    },
    ZumenShiryoTenpuSabuParams () {
      if (this.params) {
        return {
          業者コード: this.params.業者コード,
          業者コード枝番: this.params.業者コード枝番,
          工事依頼No: this.params.工事依頼No,
          枝番: this.pageData ? this.pageData.工事依頼NO枝番 : '',
          契約No: '',
          契約履歴No: '',
          契約年月: '',
          見積明細No: '',
          予定年月: '',
          契約年月明細No: ''
        }
      }
    },
    ShashinTenpuSabuParams () {
      return {
        添付種類: '904',
        工事依頼No: this.params.工事依頼No,
        工事依頼No枝番: this.params.工事依頼No枝番,
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番
      }
    },
    recalMeisai () {
      return `${this.tableDataMeisai.map(form => form.分類)}
      |${this.tableDataMeisai.map(form => form.数量)}
      |${this.tableDataMeisai.map(form => form.単価)}
      |${this.tableDataMeisai.map(form => form.金額_税抜)}
      |${this.tableDataMeisai.map(form => form.税区)}
      |${this.tableDataMeisai.map(form => form.税率)}`
    },
    footerDataRitsu () {
      const sum合計金額税抜 = this.tableDataRitsu
        .map((x) => x.合計金額_税抜)
        .reduce((a, b) => this.sum(a, b), 0)
      const sum税額 = this.tableDataRitsu
        .map((x) => x.税額)
        .reduce((a, b) => this.sum(a, b), 0)
      const sum合計金額税込 = this.tableDataRitsu
        .map((x) => x.合計金額_税込)
        .reduce((a, b) => this.sum(a, b), 0)
      this.pageData.見積金額 = '￥' + this.base.formatCommaNumeric(sum合計金額税込 + '')
      return [
        {
          rowKey: 0,
          税率: '合計',
          合計金額_税抜: this.base.formatCommaNumeric(sum合計金額税抜 + ''),
          税額: this.base.formatCommaNumeric(sum税額 + ''),
          合計金額_税込: this.base.formatCommaNumeric(sum合計金額税込 + '')
        }
      ]
    },
    isDisplayMode () {
      return this.pageData.提出フラグ == '1' || this.pageData.継続区分 == '1' || this.pageData.継続区分 == '2'
    },
    isDisplayModeColor () {
      if(this.isDisplayMode){
        return ""
      }
      return "color-orange"
    },
    isDisplayModeKoji () {
      if (this.isDisplayMode) {
        return true
      } else if (this.oldData.工事件名 != '' && this.oldData.工事件名 != null) {
        return true
      }
      return false
    },
    isDisplayModeTeishutu () {
      return this.pageData.緊急フラグ == '1' && this.pageData.受付フラグ == '0' || this.isDisplayMode
    },
    isDisplayModeTeishutuColor () {
      if(this.isDisplayModeTeishutu){
        return ""
      }
      return "color-orange"
    },
    mitsumori () {
      const no = this.pageData.工事依頼NO枝番
      if (no == '11') {
        return '見積（新規）'
      } else if (no == '21' || no == '31') {
        return '見積（相見積）'
      } else {
        return '見積（修正依頼）'
      }
    }
  },
  created () {
    // this.params = this.$route.params
    this.params = {
      工事依頼No: this.$route.params.工事依頼No,
      工事依頼No枝番: this.$route.params.工事依頼No枝番,
      業者コード: this.$route.params.業者コード,
      業者コード枝番: this.$route.params.業者コード枝番,
      見積書枝番: this.$route.params.見積書枝番
    }
    if (this.params.見積書枝番) {
      this.pageData.版数 = (
        this.params.見積書枝番.substring(2) * 1
      ).toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false
      })
    }
    let userLogIn = this.$store.getters.getUserInfo()
    this.userInfo.ユーザーＩＤ = userLogIn.userId
    this.userInfo.会社コード = userLogIn.会社コード

    this.tableDataMeisai = []
    for (let i = 0; i < 10; i++) {
      this.tableDataMeisai.push(this.defaultDataMeisai())
    }
    this.GetStartInfo()
    this.onSearch()
  },
  mounted () {
  },
  methods: {
    CheckF044Version () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_T/Mitsumori/TD00301/CheckF044Version',
            {
              会社コード: this.userInfo.会社コード,
              工事依頼No: this.params.工事依頼No,
              工事依頼No枝番: this.params.工事依頼No枝番.substring(0, 1) + this.pageData.版数.substring(1),
              業者コード: this.params.業者コード,
              業者コード枝番: this.params.業者コード枝番
            })
          .then((response) => {
            if (!response.data || !response.data.version || this.pageData.VERSION !== response.data.version) {
              resolve(false)
            } else {
              resolve(true)
            }
          })
      })
    },
    isNotEmptyRow (row) {
      return row.作業内容 || row.数量 || row.単位 || row.単価 || row.金額_税抜 || row.税区 || row.税率
    },
    async validateSaveTemp (mode) {
      let count = 0
      for (let i = 0; i < this.tableDataMeisai.length; i++) {
        const gyoshu = this.tableDataMeisai[i]
        if (gyoshu.分類 !== 'T' && gyoshu.分類 !== 'G' && this.isNotEmptyRow(gyoshu)) {
          count++;
        }
      }
      if (this.pageData.版数 != this.oldData.版数) {
        this.getMsg('E000012').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref版数.focus()
        return false
      }
      if (this.pageData.見積時作業予定日_開始日 && moment(this.pageData.見積時作業予定日_開始日).isValid() !== true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref見積時作業予定日_開始日.focus()
        return false
      }
      if (this.pageData.見積時作業予定日_終了日 && moment(this.pageData.見積時作業予定日_終了日).isValid() !== true) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref見積時作業予定日_終了日.focus()
        return false
      }
      if (this.pageData.見積時作業予定日_開始日 && this.pageData.見積時作業予定日_終了日 &&
        moment(this.pageData.見積時作業予定日_開始日) > moment(this.pageData.見積時作業予定日_終了日)) {
        this.getMsg('E040279').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref見積時作業予定日_開始日.focus()
        return false
      }
      if (!(this.pageData.緊急フラグ == '1' && this.pageData.受付フラグ == '0')){
        if (!this.pageData.見積有効期間) {
          this.getMsg('E040685').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref見積有効期間.focus()
          return false
        }
      }
      if (!this.pageData.見積担当者) {
        this.getMsg('E040049').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref見積担当者.focus()
        return false
      }
      if (!this.pageData.緊急フラグ == '1' && this.pageData.受付フラグ == '0') {
        if (moment(this.pageData.見積有効期間).isValid() !== true) {
          this.getMsg('E040278').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref見積有効期間.focus()
          return false
        }
      }
      if (!this.pageData.担当者連絡先) {
        this.getMsg('E040686').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref担当者連絡先.focus()
        return false
      }
      // ハノイ側修正2022/09/08 STEP2_T 課題管理表№13：見積入力(修繕) シートの「2022/09/01仕様変更分」 処理概要シート：＜提出ボタン押下＞のDB更新処理で緊急案件時の分岐処理を追加。START
      if (!this.base.isSimpleByte(this.pageData.担当者連絡先)) {
        this.getMsg('E040460').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref担当者連絡先.focus()
        return false
      }
      // ハノイ側修正2022/09/08 STEP2_T 課題管理表№13：見積入力(修繕) シートの「2022/09/01仕様変更分」 処理概要シート：＜提出ボタン押下＞のDB更新処理で緊急案件時の分岐処理を追加。END
      if(mode != 2){
        if (this.getNumber(this.pageData.見積金額.replaceAll('￥', '')) == 0) {
        this.getMsg('E010309').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref見積金額.focus()
        return false
        }
      }else{
        if(count != 0){
          if (this.getNumber(this.pageData.見積金額.replaceAll('￥', '')) == 0) {
          this.getMsg('E010309').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref見積金額.focus()
          return false
          }
        }
      }
      // table
      for (let i = 0; i < this.tableDataMeisai.length; i++) {
        const row = this.tableDataMeisai[i]
        // if (row.分類 == 'T' || row.分類 == 'G') {
        //   if (!row.作業内容) {
        //     this.getMsg('E040687').then((response) => {
        //       this.showError(response.data)
        //     })
        //     this.$refs['ref作業内容' + i].focus()
        //     return false
        //   }
        //   continue
        // }
        if (row.分類 !== 'T' && row.分類 !== 'G' && this.isNotEmptyRow(row)) {
          if (!row.作業内容) {
            this.getMsg('E040687').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref作業内容' + i].focus()
            return false
          }
          // 数量
          if (!row.数量) {
            this.getMsg('E040065').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref数量' + i].focus()
            return false
          }
          if (isNaN(String(row.数量).replaceAll(',', ''))) {
            this.getMsg('E040071').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref数量' + i].focus()
            return false
          }
          // 20221118 del マイナスチェックを削除
          /* if (this.getNumber(row.数量) < 0) {
            this.getMsg('E040239').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref数量' + i].focus()
            return false
          } */
          // 20221118 del
          // 単位
          if (!row.単位) {
            this.getMsg('E040609').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref単位' + i].focus()
            return false
          }
          // ハノイ側修正2022/09/08 STEP2_T 課題管理表№13：見積入力(修繕) シートの「2022/09/01仕様変更分」 処理概要シート：＜提出ボタン押下＞のDB更新処理で緊急案件時の分岐処理を追加。
          // 単価
          // if (!row.単価) {
          //   this.getMsg('E040066').then((response) => {
          //     this.showError(response.data)
          //   })
          //   this.$refs['ref単価' + i].focus()
          //   return false
          // }
          if (isNaN(String(row.単価).replaceAll(',', ''))) {
            this.getMsg('E040072').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref単価' + i].focus()
            return false
          }
          // 20221118 del マイナスチェックを削除
          /* if (this.getNumber(row.単価) < 0) {
            this.getMsg('E040239').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref単価' + i].focus()
            return false
          } */
          // 20221118 del
          // 金額(税抜)
          if (!row.金額_税抜) {
            this.getMsg('E040689').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref金額_税抜' + i].focus()
            return false
          }
          if (isNaN(String(row.金額_税抜).replaceAll(',', ''))) {
            this.getMsg('E040690').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref金額_税抜' + i].focus()
            return false
          }
          // 20221118 del マイナスチェックを削除
          /* if (this.getNumber(row.金額_税抜) < 0) {
            this.getMsg('E040239').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref金額_税抜' + i].focus()
            return false
          } */
          // 20221118 del
          // 税区
          if (!row.税区) {
            this.getMsg('E040688').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref税区' + i].focus()
            return false
          }
          // 税率
          if (row.税率 != 0 && !row.税率) {
            this.getMsg('E010310').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref税率' + i].focus()
            return false
          }
          if (isNaN(String(row.税率).replaceAll(',', ''))) {
            this.getMsg('E040073').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref税率' + i].focus()
            return false
          }
          if (this.getNumber(row.税率) < 0) {
            this.getMsg('E040239').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref税率' + i].focus()
            return false
          }
          if ((row.税区 == '1' && row.税率 == 0) || (row.税区 == '2' && row.税率 != 0)) {
            this.getMsg('E040560').then((response) => {
              this.showError(response.data)
            })
            this.$refs['ref税率' + i].focus()
            return false
          }
          // 20221222 add 税率の使用可否チェックを追加。
          if (row.税区 == '1') {
            const チェック税率 = await this.checkzeiritu(row.税率)
            if (チェック税率 == '') {
              this.getMsg('E040598').then((response) => {
                this.showError(response.data)
              })
              this.$refs['ref税率' + i].focus()
              return false
            }
          }
          // 20221222 add
        }
      }
      // 20221118 add 率別テーブルのマイナスチェックを追加。
      if (this.getNumber(this.tableDataRitsu[0].合計金額_税込) < 0) {
        this.getMsg('E040775').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      if (this.getNumber(this.tableDataRitsu[1].合計金額_税込) < 0) {
        this.getMsg('E040775').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      if (this.getNumber(this.tableDataRitsu[2].合計金額_税込) < 0) {
        this.getMsg('E040775').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      if (this.getNumber(this.tableDataRitsu[3].合計金額_税込) < 0) {
        this.getMsg('E040775').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      // 20221118 add
      return true
    },
    // 20221222 add 税率の使用可否チェックを追加。
    async checkzeiritu (zeiritu) {
      const checkzei = {
        会計会社コード: this.pageData.会計会社コード,
        税率: zeiritu,
        判定日: this.pageData.見積有効期間
      }
      let res
      await this.http
        .post('/api/Reafs_T/Mitsumori/TD00301/Check税率', checkzei, 'アクセスしています....')
        .then(async (response) => {
          res = response
        })
      if (res.status) {
        return res.data && res.data[0] ? res.data[0].税率 : ''
      }
    },
    // 20221222 add
    async onSubmission () {
      if (!await this.validateSaveTemp(0)) {
        return false
      }
      let msg = (await this.getMsg('K040064')).data
      if (!await this.confirm(msg)) {
        return false
      }
      if (!await this.CheckF044Version()) {
        this.getMsg('E040098').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      const data = {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.params.工事依頼No,
        工事依頼No枝番: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番,
        見積金額: this.getNumber(this.footerDataRitsu[0].合計金額_税抜),
        緊急フラグ: this.pageData.緊急フラグ,
        F044_業者別見積マスタ: this.getDataF044_2(),
        FT040_業者見積ヘッダマスタ: this.getDataFT040(),
        FT041_業者見積トレーラマスタ: this.getDataFT041()
      }
      let res
      await this.http
        .post('/api/Reafs_T/Mitsumori/TD00301/提出', data, 'アクセスしています....')
        .then(async (response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        let res2
        this.http
          .post('/api/Reafs_T/Mitsumori/TD00301/見積書作成', data, 'アクセスしています....')
          .then((result) => {
            res2 = result
          })
        let baseURL = this.commonFunctionUI.getBaseURL('W')
        baseURL = baseURL + 'WD00520/SyuzenIraiSansyoShonin'
        for (let i = 0; i < res.data.mailData.length; i++) {
          if (res.data.mailData[i].mail_pattern_no == '73') {
            let paramSet = {
              パラメータ1: this.params.工事依頼No,
              パラメータ2: '',
              パラメータ3: '',
              パラメータ4: '',
              PG: 'TD00301'
            }
            const resParam = await this.commonFunctionUI.setQueryParameter(paramSet)
            let linkUrl = baseURL + '?guid=' + resParam.data.guid
            linkUrl = encodeURI(linkUrl)
            res.data.mailData[i].url = [linkUrl]
          }
          let resultMail = this.commonFunctionUI.CM00110SendMail(res.data.mailData[i])
        }

        for (let i = 0; i < res.data.tellData.length; i++) {
          const resultTell = this.commonFunctionUI.twilio_Call(res.data.tellData[i])
        }

        // ハノイ側修正2022/08/24 STEP2_T 課題管理表№7：見積入力(修繕) シートの「2022/08/19仕様変更分」処理概要シート：＜提出ボタン押下＞のDB更新処理を修正...
        // 2022/10/25 ファイルダウンロード処理は不要のためコメント
        // await this.downloadReport(res.data, 'application/pdf', 'Reafs-T_見積書.pdf')
        this.getMsg('M000012').then((response) => {
          this.showSuccess(response.data)
        })
        this.onSearch()
      } else {
        this.showError(res.message)
      }
    },
    async downloadReport (base64Data, contentType, fileName) {
      // const linkSource = `data:${contentType};base64,${base64Data}`
      // const downloadLink = document.createElement('a')
      // downloadLink.href = linkSource
      // downloadLink.download = fileName
      // downloadLink.click()
      await this.base.saveFileDownload(base64Data, fileName, contentType)
    },
    async onSaveTemp (mode) {
      if (!await this.validateSaveTemp(mode)) {
        return false
      }

      let msg = (await this.getMsg('K040063')).data
      if (mode == 2) {
        msg = (await this.getMsg('K040080')).data
      }
      if (!await this.confirm(msg)) {
        return false
      }
      if (!await this.CheckF044Version()) {
        this.getMsg('E040098').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      const data = {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.params.工事依頼No,
        工事依頼No枝番: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番,
        見積金額: this.getNumber(this.footerDataRitsu[0].合計金額_税抜),
        mode,
        F044_業者別見積マスタ: this.getDataF044_1(),
        FT040_業者見積ヘッダマスタ: this.getDataFT040(),
        FT041_業者見積トレーラマスタ: this.getDataFT041()

      }
      let res
      await this.http
        .post('/api/Reafs_T/Mitsumori/TD00301/Post一時保存', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        if (mode == 2) {
          let baseURL = this.commonFunctionUI.getBaseURL('R')
          baseURL = baseURL + 'mobile/KinkyuKougaku'
          for (let i = 0; i < res.data.mailData.length; i++) {
            if (res.data.mailData[i].mail_pattern_no == '42' && res.data.i高額申請処理 == 1) {
              let paramSet = {
                パラメータ1: this.params.工事依頼No,
                パラメータ2: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
                パラメータ3: res.data.種類,
                PG: 'TD00301'
              }
              const resParam = await this.commonFunctionUI.setQueryParameter(paramSet)
              let linkUrl = baseURL + '?guid=' + resParam.data.guid
              linkUrl = encodeURI(linkUrl)
              res.data.mailData[i].url = [linkUrl]
            } else if (res.data.mailData[i].mail_pattern_no == '42') { // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230216追加」Start
              res.data.mailData[i].url = ['']
            }
            // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230216追加」End
            let resultMail = this.commonFunctionUI.CM00110SendMail(res.data.mailData[i])
          }
        }

        this.getMsg('M000012').then((response) => {
          this.showSuccess(response.data)
        })
        this.onSearch()
        return true
      } else {
        this.showError(res.message)
        return false
      }
    },
    getDataF044_1 () {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼NO: this.params.工事依頼No,
        工事依頼NO枝番: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番,
        受付フラグ: 1,
        受付日: '',
        差戻フラグ: 0,
        進捗コード_T: 111
      }
    },
    getDataF044_2 () {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼NO: this.params.工事依頼No,
        工事依頼NO枝番: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番,
        提出フラグ: 1,
        提出日: moment(new Date()).format('yyyy/MM/dd'),
        差戻フラグ: 0,
        進捗コード_T: 121
      }
    },
    getDataF044_3 () {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼NO: this.params.工事依頼No,
        // 工事依頼NO枝番: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
        // 業者コード: this.params.業者コード,
        // 業者コード枝番: this.params.業者コード枝番,
        依頼フラグ: 1
      }
    },
    getDataFT040 () {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.params.工事依頼No,
        工事依頼No枝番: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番,
        工事件名: this.pageData.工事件名,
        見積作成日: moment(new Date()).format('yyyy/MM/dd'),
        見積時作業予定日開始: this.pageData.見積時作業予定日_開始日,
        見積時作業予定日終了: this.pageData.見積時作業予定日_終了日,
        見積有効期限: this.pageData.見積有効期間,
        見積担当者名: this.pageData.見積担当者,
        担当者連絡先: this.pageData.担当者連絡先,
        見積金額税込: this.replaceAll(this.footerDataRitsu[0].合計金額_税込),
        見積金額税抜: this.replaceAll(this.footerDataRitsu[0].合計金額_税抜),
        見積消費税額: this.replaceAll(this.footerDataRitsu[0].税額),
        見積金額: this.replaceAll(this.pageData.見積金額).replaceAll('￥', ''),
        原価備考: this.pageData.備考,
        連絡事項: this.pageData.ＳＧリアルティへの連絡事項,
        率別税率1: this.tableDataRitsu[0].税率,
        率別原価金額税込1: this.replaceAll(this.tableDataRitsu[0].合計金額_税込),
        率別原価金額税抜1: this.replaceAll(this.tableDataRitsu[0].合計金額_税抜),
        率別原価金額消費税1: this.replaceAll(this.tableDataRitsu[0].税額),
        率別税率2: this.replaceAll(this.tableDataRitsu[1].税率),
        率別原価金額税込2: this.replaceAll(this.tableDataRitsu[1].合計金額_税込),
        率別原価金額税抜2: this.replaceAll(this.tableDataRitsu[1].合計金額_税抜),
        率別原価金額消費税2: this.replaceAll(this.tableDataRitsu[1].税額),
        率別税率3: this.replaceAll(this.tableDataRitsu[2].税率),
        率別原価金額税込3: this.replaceAll(this.tableDataRitsu[2].合計金額_税込),
        率別原価金額税抜3: this.replaceAll(this.tableDataRitsu[2].合計金額_税抜),
        率別原価金額消費税3: this.replaceAll(this.tableDataRitsu[2].税額),
        率別税率4: this.replaceAll(this.tableDataRitsu[3].税率),
        率別原価金額税込4: this.replaceAll(this.tableDataRitsu[3].合計金額_税込),
        率別原価金額税抜4: this.replaceAll(this.tableDataRitsu[3].合計金額_税抜),
        率別原価金額消費税4: this.replaceAll(this.tableDataRitsu[3].税額),
        削除区分: 0
      }
    },
    getDataFT041 () {
      const ft041 = []
      let 明細ソートNo = 1
      for (let i = 0; i < this.tableDataMeisai.length; i++) {
        const row = this.tableDataMeisai[i]
        if (row.分類 == 'T' || row.分類 == 'G' || this.isNotEmptyRow(row)) {
          const data = {
            会社コード: this.userInfo.会社コード,
            工事依頼No: this.params.工事依頼No,
            工事依頼No枝番: this.pageData.見積No ? this.pageData.見積No.substring(this.pageData.見積No.length - 2) : '',
            業者コード: this.params.業者コード,
            業者コード枝番: this.params.業者コード枝番,
            明細No: row.明細No || -1,
            明細ソートNo: 明細ソートNo,
            画面ソートNo: 0,
            明細登録順No: 0,
            分類コード: row.分類 || ' ',
            作業名称: row.作業内容,
            数量: this.replaceAll(row.数量),
            単位: this.replaceAll(row.単位),
            原価単価: this.replaceAll(row.単価),
            原価金額税抜: this.replaceAll(row.金額_税抜),
            原価税区分: row.税区,
            原価税率: row.税率,
            削除区分: 0
          }
          // 原価金額税抜 × 原価税率 ÷ 100 ※端数切捨て
          data.原価消費税額 = this.base.ToRoundDown(this.getNumber(data.原価金額税抜) * this.getNumber(row.税率) / 100, 0)
          data.原価金額税込 = this.getNumber(data.原価金額税抜) + data.原価消費税額
          ft041.push(data)
          明細ソートNo++
        }
      }
      return ft041
    },
    getDataF102 () {
      return {
        依頼NO: this.params.工事依頼No,
        進捗コード_本体: ''
      }
    },
    replaceAll (val) {
      return ((val || '') + '').replaceAll(',', '')
    },
    async onMitsumoriDetaTorikomiClick () {
      this.showTS00305 = true
    },
    onIraiNaiyoHyojiClick () {
      this.showIraiNaiyoSabu = true
    },
    IraiNaiyoSabuClose (e) {
    },
    KakoMitsumoriSanshoSabuonBackData (e) {
      if (e.見積No) {
        this.pageData.過去見積参照 = e.見積No
      }
      if (e.過去見積工事依頼NO枝番) {
        this.pageData.過去見積工事依頼NO枝番 = e.過去見積工事依頼NO枝番
      }
    },
    MitsumoriDetaTorikomiOnBackData (e) {
      if (e.backData) {
        this.tableDataMeisai = []
        const row = this.defaultDataMeisai()
        Object.assign(row, e.backData)
        this.tableDataMeisai = e.backData
        // this.tableDataMeisai = e.backData
        /* for (let i = 0; i < 9; i++) {
          this.tableDataMeisai.push(this.defaultDataMeisai())
        } */
      }
    },
    BukkenDokyumentoSanshoSabuOnBackData () {

    },
    onMeisai分類Change (row, column, index) {
      // this.sumAllMeisai()
      // this.sumForRitsu()
      if (row.分類 != 'T' && row.分類 != 'G') {
        row.作業内容 = ''
        row.金額_税抜 = ''
      }else if(row.分類 == 'G'){
        row.作業内容 = '小計'
      }
    },
    onMeisai数量Change (row, column, index) {
      this.sumRowMeisai(row, index)
      // this.sumAllMeisai()
      // this.sumForRitsu()
    },
    onMeisai単位Change (row, column, index) {
      // this.sumRowMeisai(row, index)
      // this.sumAllMeisai()
      // this.sumForRitsu()
    },
    onMeisai単価Change (row, column, index) {
      this.sumRowMeisai(row, index)
      // this.sumAllMeisai()
      // this.sumForRitsu()
    },
    onMeisai金額_税抜Change (row, column, index) {
      this.sumAllMeisai()
      // this.sumForRitsu()
    },
    async onMeisai税区Change (row, column, index) {
      if (row.税区 == '2') {
        row.税率 = '0'
      } else if (row.税区 == '1' && this.pageData.見積依頼日) {
        const tax = await this.getS008()
        row.税率 = tax
      }
      // this.sumForRitsu()
    },
    onMeisai税率Change (row, column, index) {
      // this.sumForRitsu()
    },
    async getS008 () {
      let result
      await this.http
        .get(
          '/api/Reafs_T/Mitsumori/TD00301/Get税率',
          {有効期間: this.pageData.見積依頼日},
          'アクセスしています....'
        )
        .then((res) => {
          result = res
        })
        .finally(() => {
        })
      if (result.status) {
        return result.data && result.data[0] ? result.data[0].税率 : ''
      }
    },
    validateMeisai () {
      // let iGStartIndex = -1
      // for (let i = this.tableDataMeisai.length - 1; i >= 0; i--) {
      //   const row = this.tableDataMeisai[i]
      //   if (iGStartIndex != -1 && row.分類 == 'G') {
      //     break
      //   }
      //   if (iGStartIndex != -1 && row.分類 == 'T') {
      //     iGStartIndex = -1
      //   }
      //   if (row.分類 == 'G') {
      //     iGStartIndex = i
      //   }
      // }
      // if (iGStartIndex != -1) {
      //   this.tableDataMeisai[iGStartIndex].分類 = ''
      //   this.tableDataMeisai[iGStartIndex].作業内容 = ''
      //   this.tableDataMeisai[iGStartIndex].金額_税抜 = ''
      // }
    },
    handleDisplayMeisai () {
      this.validateMeisai()
      const data = this.tableDataMeisai
      if (data.length <= 0) {
        return
      }
      for (let i = 0; i < data.length; i++) {
        let row = data[i]
        if (row.分類 != 'T' && row.分類 != 'G') {
          row.TGDisabled = false
        } else {
          row.TGDisabled = true
          for (const props in row) {
            if (!(props == '分類' || props == '作業内容' || (props == '金額_税抜' && row.分類 == 'G') ||
             props == '明細No' || props == '明細登録順No' || props.includes('Disabled'))) {
              row[props] = ''
            }
          }
        }
      }
    },
    getNumber (string) {
      if (!string) return 0
      return (string + '' || '0').replaceAll(',', '') * 1
    },
    sumRowMeisai (row, rowIndex) {
      if (!row || !this.getNumber(row.単価)) {
        return
      }
      const 数量 = this.getNumber(row.数量)
      // const 単位 = this.getNumber(row.単位)
      const 単価 = this.getNumber(row.単価)
      row.金額_税抜 = this.base.formatCommaNumeric(数量 * 単価 + '')
    },
    sumAllMeisai () {
      let sum金額税抜 = 0
      if (!this.tableDataMeisai.find(x => x.分類 == 'G')) {

      } else {
        for (let i = 0; i < this.tableDataMeisai.length; i++) {
          const row = this.tableDataMeisai[i]
          if (row.分類 == 'T') {
            continue
          }
          if (row.分類 != 'G' && row.分類 != 'T') {
            sum金額税抜 += this.getNumber(row.金額_税抜)
          } else if (row.分類 == 'G') {
            const valFormat = this.base.formatCommaNumeric(sum金額税抜 + '')
            this.tableDataMeisai[i].金額_税抜 = valFormat
            sum金額税抜 = 0
            // break
          }
        }
      }
    },
    sumForRitsu () {
      this.tableDataRitsu = [this.defaultDataRitsu(), this.defaultDataRitsu(), this.defaultDataRitsu(), this.defaultDataRitsu()]
      const data = JSON.parse(JSON.stringify(this.tableDataMeisai))
      const group = this.groupByForRitsu(data, '税率')
      for (let i = 0; i < group.length && i < 4; i++) {
        // this.tableDataRitsu[i].税率 = group[i].key
        this.tableDataRitsu[i].税率 = group[i].key || ''
        if (group[i].key == '0') {
          this.tableDataRitsu[i].税率 = '0'
        }
        const 合計金額税抜 = group[i].value.map((x) => x.金額_税抜)
          .reduce((a, b) => this.sum(a, b), 0)
        // this.tableDataRitsu[i].合計金額_税抜 = this.base.formatCommaNumeric(合計金額税抜 + '')
        this.tableDataRitsu[i].合計金額_税抜 = this.base.formatCommaNumeric(合計金額税抜 || '')
        // const 税額 = this.base.ToHalfAjust(合計金額税抜 * group[i].key / 100, 0)
        const 税額 = this.base.ToRoundDown(合計金額税抜 * group[i].key / 100, 0)
        // this.tableDataRitsu[i].税額 = this.base.formatCommaNumeric(税額 + '')
        // this.tableDataRitsu[i].合計金額_税込 = this.base.formatCommaNumeric(合計金額税抜 + 税額 + '')
        this.tableDataRitsu[i].税額 = this.base.formatCommaNumeric(税額 || '')
        this.tableDataRitsu[i].合計金額_税込 = this.base.formatCommaNumeric(合計金額税抜 + 税額 || '')
      }
    },
    groupByForRitsu (xs, key) {
      const grp = xs.filter(x => x.分類 != 'T' && x.分類 != 'G').reduce(function (rv, x) {
        (rv[x[key]] = rv[x[key]] || []).push(x)
        return rv
      }, {})
      const arr = []
      for (const prop in grp) {
        if (prop) {
          arr.push({ key: prop * 1, value: grp[prop] })
        }
      }
      arr.sort((a, b) => (a.key > b.key) ? 1 : ((b.key > a.key) ? -1 : 0))
      return arr
    },
    onDeleteMeisai (row, rowIndex) {
      for (const prop in this.tableDataMeisai[rowIndex]) {
        if (prop != '明細No' && prop != '明細登録順No') {
          this.tableDataMeisai[rowIndex][prop] = ''
        }
      }
    },
    onNextBackMeisai (row, rowIndex, isBack) {
      const newIndex = isBack ? rowIndex - 1 : rowIndex + 1
      this.moveArrayItemToNewIndex(this.tableDataMeisai, rowIndex, newIndex)
    },
    moveArrayItemToNewIndex (arr, oldIndex, newIndex) {
      if (newIndex < 0 || newIndex >= arr.length) {
        return
      }
      arr.splice(newIndex, 0, arr.splice(oldIndex, 1)[0])
      return arr
    },
    onCopyMeisai (row, rowIndex) {
      // if (row.分類 == 'G') {
      //   return
      // }
      if (this.tableDataMeisai.length >= this.maxRowMeisai) {
        for (let i = this.tableDataMeisai.length - 1; i > 0; i--) {
          const checkRow = this.tableDataMeisai[i]
          if (!(this.isNotEmptyRow(checkRow))) { // || checkRow.明細No
            this.tableDataMeisai.splice(i, 1)
            break
          }
        }
        if (this.tableDataMeisai.length >= this.maxRowMeisai) {
          return
        }
      }

      const newRow = JSON.parse(JSON.stringify(row))
      newRow.明細No = ''
      newRow.明細登録順No = ''
      this.tableDataMeisai.splice(rowIndex + 1, 0, newRow)
    },
    onAddRowMeisai () {
      const iInsertRow =
        this.tableDataMeisai.length < 10 ? 10 - this.tableDataMeisai.length
          : this.tableDataMeisai.length + 5 <= this.maxRowMeisai
            ? 5
            : this.maxRowMeisai - this.tableDataMeisai.length
      for (let i = 0; i < iInsertRow; i++) {
        this.tableDataMeisai.push(this.defaultDataMeisai())
      }
    },
    async onReference () {
      if (!this.pageData.過去見積参照) {
        this.getMsg('E040691').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref過去見積参照.focus()
        return
      }
      let pattern = /[\d]{2}-[\d]{5}-[\d]{2}/
      if (!pattern.test(this.pageData.過去見積参照)) {
        this.getMsg('E040692').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref過去見積参照.focus()
        return
      }
      let result
      const data = {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.pageData.過去見積参照.split('-')[0] + this.pageData.過去見積参照.split('-')[1],
        工事依頼No枝番: this.pageData.過去見積工事依頼NO枝番,
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番
      }
      await this.http
        .get(
          '/api/Reafs_T/Mitsumori/TD00301/参照検索',
          data,
          'アクセスしています....'
        )
        .then((res) => {
          result = res
        })
        .finally(() => {
        })
      if (result.status) {
        if (!result.data || result.data.length <= 0) {
          this.getMsg('E010224').then((response) => {
            this.showError(response.data)
          })
        } else {
          this.tableDataMeisai = result.data
          if (this.tableDataMeisai.length < 10) {
            for (let i = this.tableDataMeisai.length; i < 10; i++) {
              this.tableDataMeisai.push(this.defaultDataMeisai())
            }
          }
        }
      } else {
        this.showError(result.message)
      }
    },
    async onRitsuUpdateClick () {
      this.sumForRitsu()
    },
    sum (a, b) {
      a = this.getNumber(a)
      b = this.getNumber(b)
      return a * 1 + b * 1
    },
    defaultDataRitsu () {
      return {
        税率: '',
        合計金額_税抜: '',
        税額: '',
        合計金額_税込: ''
      }
    },
    defaultDataMeisai () {
      return {
        作業カテゴリ大: '',
        作業カテゴリ中: '',
        作業カテゴリ小: '',
        作業内容: '',
        数量: '',
        単位: '',
        単価: '',
        金額_税抜: '',
        分類: '',
        税率: '',
        税区: ''
      }
    },
    defaultPagedata () {
      return {
        VERSION: '',
        依頼状況: '',
        依頼者: '',
        備考: '',
        受付フラグ: '',
        工事No: '',
        工事予定日_終了日: '',
        工事予定日_開始日: '',
        工事件名: '',
        工事依頼NO枝番: '',
        差戻フラグ: '',
        担当者連絡先: '',
        提出フラグ: '',
        提出先: '',
        版数: '',
        物件_コード: '',
        物件_名称: '',
        物件住所: '',
        物件住所_郵便番号: '',
        状態: '',
        確定フラグ: '',
        緊急フラグ: '',
        見積No: '',
        表示見積No: '',
        見積依頼日: '',
        見積担当者: '',
        見積時作業予定日_終了日: '',
        見積時作業予定日_開始日: '',
        見積有効期間: '',
        見積金額: '',
        過去見積参照: '',
        過去見積工事依頼NO枝番: '',
        ＳＧリアルティより連絡事項: '',
        ＳＧリアルティへの連絡事項: ''
      }
    },
    changeHansu (isLeft) {
      let num = Number(this.pageData.版数 || 0)
      num = isLeft ? (num > 1 ? num - 1 : num) : num < 9 ? num + 1 : num
      this.pageData.版数 = num.toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false
      })
    },
    onHansuFocus () {
      // this.pageData.版数 = this.pageData.版数 ? this.pageData.版数.substring(1) : this.pageData.版数
    },
    onHansuBlur () {
      const num = this.pageData.版数 * 1
      if (Number.isInteger(num)) {
        this.pageData.版数 = num.toLocaleString('en-US', {
          minimumIntegerDigits: 2,
          useGrouping: false
        })
      }
    },
    keypressTextbox (evt) {
      var theEvent = evt || window.event
      var key = theEvent.keyCode || theEvent.which
      key = String.fromCharCode(key)
      var regex = /[-\d\.]/ // dowolna liczba (+- ,.) :)
      var objRegex = /^-?\d*[\.]?\d*$/
      var val = this.pageData.版数
      if (!regex.test(key) || !objRegex.test(val + key) ||
        !theEvent.keyCode == 46 || !theEvent.keyCode == 8) {
        theEvent.returnValue = false
        if (theEvent.preventDefault) {
          theEvent.preventDefault()
        }
      };
    },
    GetStartInfo () {
      // Get Dropdown
      this.http
        .get('/api/Reafs_T/Mitsumori/TD00301/Get税区DataSource', 'アクセスしています....')
        .then((response) => {
          this.税区List = response.data
        })
    },
    searchValidate () {
      const num = Number(this.pageData.版数 || 0)
      if (num < 1 || num > 9) {
        this.getMsg('E040719').then((response) => {
          this.showError(response.data)
        })
        return false
      }

      return true
    },
    async onSearch () {
      if (!this.searchValidate()) {
        return
      }
      const data = {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.params.工事依頼No,
        工事依頼No枝番: this.params.工事依頼No枝番.substring(0, 1) + this.pageData.版数.substring(1),
        業者コード: this.params.業者コード,
        業者コード枝番: this.params.業者コード枝番,
        見積書枝番: this.params.見積書枝番
      }
      let result
      await this.http
        .get(
          '/api/Reafs_T/Mitsumori/TD00301/Search',
          data,
          'アクセスしています....'
        )
        .then((res) => {
          result = res
        })
        .finally(() => {
        })
      if (result.status) {
        const dataF040 = result.data.dataF040[0] || {}
        const 依頼情報 = result.data.依頼情報 || {}
        if (!依頼情報.工事No) {
          this.getMsg('E000014').then((response) => {
            this.showSuccess(response.data)
          })
        }
        let 明細データ = result.data.明細データ
        const 見積ヘッダ = result.data.見積ヘッダ[0] || {}
        const df = this.defaultPagedata()
        for (const prop in df) {
          if (prop != '版数') {
            this.pageData[prop] = df[prop]
          }
        }
        for (const prop in 依頼情報) {
          if (prop != '版数') {
            this.pageData[prop] = 依頼情報[prop] || 依頼情報[prop] == 0 ? 依頼情報[prop] : ''
          }
        }
        if (this.pageData.工事No) {
          this.pageData.工事No = `${this.pageData.工事No.substring(0, 2)}-${this.pageData.工事No.substring(2)}`
        }
        // this.pageData.工事件名 = this.pageData.工事依頼NO枝番 == '11' ? this.pageData.工事件名 : 見積ヘッダ.工事件名
        this.pageData.工事件名 = 依頼情報.工事件名
        if (this.pageData.工事件名 == '' || 依頼情報.提出フラグ == '1') {
          this.pageData.工事件名 = 見積ヘッダ.工事件名
        }
        this.pageData.工事予定日_開始日 = dataF040.工事予定日_開始日 || ''
        this.pageData.工事予定日_終了日 = dataF040.工事予定日_終了日 || ''
        this.pageData.過去見積参照 = ''
        this.pageData.過去見積工事依頼NO枝番 = ''
        this.pageData.ＳＧリアルティより連絡事項 = dataF040.ＳＧリアルティより連絡事項 || ''
        this.pageData.見積時作業予定日_開始日 = 見積ヘッダ.見積時作業予定日開始 || ''
        this.pageData.見積時作業予定日_終了日 = 見積ヘッダ.見積時作業予定日終了 || ''
        this.pageData.見積有効期間 = 見積ヘッダ.見積有効期限 || ''
        this.pageData.見積担当者 = 見積ヘッダ.見積担当者名 || ''
        this.pageData.見積金額 = '￥' + this.base.formatCommaNumeric(見積ヘッダ.見積金額税込)
        this.pageData.担当者連絡先 = 見積ヘッダ.担当者連絡先 || ''
        this.pageData.備考 = 見積ヘッダ.原価備考 || ''
        this.pageData.ＳＧリアルティへの連絡事項 = 見積ヘッダ.連絡事項 || ''
        this.oldData = JSON.parse(JSON.stringify(this.pageData))
        // tableDataMeisai
        if (明細データ) {
          明細データ.map((x) => {
            x.分類 = (x.分類 || '').trim()
            return x
          })
        }
        this.tableDataMeisai = 明細データ || []
        if (this.tableDataMeisai.length < 10 && !this.isDisplayMode) {
          for (let i = this.tableDataMeisai.length; i < 10; i++) {
            this.tableDataMeisai.push(this.defaultDataMeisai())
          }
        }
        // tableDataRitsu
        this.tableDataRitsu = [this.defaultDataRitsu(), this.defaultDataRitsu(), this.defaultDataRitsu(), this.defaultDataRitsu()]
        // 1
        this.tableDataRitsu[0].税率 = 見積ヘッダ.率別税率1 || ''
        this.tableDataRitsu[0].合計金額_税抜 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税抜1 || '')
        this.tableDataRitsu[0].税額 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額消費税1 || '')
        this.tableDataRitsu[0].合計金額_税込 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税込1 || '')
        // 2
        this.tableDataRitsu[1].税率 = 見積ヘッダ.率別税率2 || ''
        this.tableDataRitsu[1].合計金額_税抜 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税抜2 || '')
        this.tableDataRitsu[1].税額 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額消費税2 || '')
        this.tableDataRitsu[1].合計金額_税込 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税込2 || '')
        // 3
        this.tableDataRitsu[2].税率 = 見積ヘッダ.率別税率3 || ''
        this.tableDataRitsu[2].合計金額_税抜 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税抜3 || '')
        this.tableDataRitsu[2].税額 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額消費税3 || '')
        this.tableDataRitsu[2].合計金額_税込 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税込3 || '')
        // 4
        this.tableDataRitsu[3].税率 = 見積ヘッダ.率別税率4 || ''
        this.tableDataRitsu[3].合計金額_税抜 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税抜4 || '')
        this.tableDataRitsu[3].税額 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額消費税4 || '')
        this.tableDataRitsu[3].合計金額_税込 = this.base.formatCommaNumeric(見積ヘッダ.率別原価金額税込4 || '')
      } else {
        this.showError(result.message)
      }
    },
    onBack () {
      // this.$router.push({ name: 'Thome' })
      this.$router.back()
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          if (option && typeof option.callback === 'function') {
            option.callback()
          }
        }
      })
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
    }
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
  min-width: 1380px;
  // height: 100%;
}

.MitsumoriNyuryokuShuzen .MitsumoriNyuryokuShuzenForm {
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
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.ellipsis2 {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 44px;
  line-height: 22px;
}

.cell-body-2 {
  height: 42px;
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

.tblMeisai button {
  height: 96%;
  width: 96%;
}

.color-blue {
  color: #305496;
}

.color-orange {
  color: orange;
}

</style>

<style>
.MitsumoriNyuryokuShuzen .is-disabled,
.MitsumoriNyuryokuShuzen .is-disabled * {
  pointer-events: none;
  opacity: 0.9;
}

.MitsumoriNyuryokuShuzen .is-yl,
.MitsumoriNyuryokuShuzen .is-yl * {
  background-color: lightgoldenrodyellow !important;
}

.MitsumoriNyuryokuShuzen .is-yl *[disabled*="disabled"] {
  opacity: 0.8;
}

.MitsumoriNyuryokuShuzen .is-label,
.is-label * {
  background-color: #d3d3d3 !important;
}

.MitsumoriNyuryokuShuzen .cell-body-custome {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.MitsumoriNyuryokuShuzen .cell-body-custome div:not(:last-child) {
  border-right: solid 1px #000;
}

.MitsumoriNyuryokuShuzen .clsTextArena {
  display: flex;
  flex-flow: column;
}

.MitsumoriNyuryokuShuzen .clsTextArena label {
  width: 90% !important;
}

.MitsumoriNyuryokuShuzen .clsTextArena .el-form-item__content {
  margin-left: 0 !important;
}

.MitsumoriNyuryokuShuzen .resize-none textarea {
  resize: none;
  background: #d3d3d3 !important;
  border-color: #b7b5b5 !important;
  color: black !important;
  text-overflow: ellipsis;
      min-height: 32px;
    height: 48px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}

.MitsumoriNyuryokuShuzen .el-row {
  margin-bottom: 14px;
}

.MitsumoriNyuryokuShuzen .text-center {
  text-align: center !important;
  vertical-align: middle !important;
}

.MitsumoriNyuryokuShuzen .text-left {
  text-align: left !important;
}

.MitsumoriNyuryokuShuzen .position-relative {
  position: relative;
}

.MitsumoriNyuryokuShuzen .el-checkbox-group .el-checkbox:first-child,
.MitsumoriNyuryokuShuzen .four .el-checkbox {
  margin-left: 10px !important;
}

.MitsumoriNyuryokuShuzen .el-date-editor.el-input,
.MitsumoriNyuryokuShuzen .el-date-editor.el-input__inner {
  width: 135px;
}

.MitsumoriNyuryokuShuzen .text-ellipsis input {
  text-overflow: ellipsis;
  overflow: hidden;
}

.MitsumoriNyuryokuShuzen .ve-table-content .ve-table-body tr:not([style*="height: 0px"]),
.MitsumoriNyuryokuShuzen .ve-table-footer-tr {
  height: 30px !important;
}
.MitsumoriNyuryokuShuzen .tblMeisai .ve-table-body .el-input__inner {
  height: 29px !important;
  padding: 0 5px;
}
.MitsumoriNyuryokuShuzen .ve-table-footer-tr td {
  padding: 3px !important;
}

.MitsumoriNyuryokuShuzen .tableRitsu .ve-table-container {
  overflow: hidden;
  height: 200px !important;
}

.MitsumoriNyuryokuShuzen .tableRitsu .ve-table-fixed-header .ve-table-header-tr th {
  text-align: center !important;
}

.MitsumoriNyuryokuShuzen .tblMeisai .ve-table-container {
  min-height: 340px;
}

.MitsumoriNyuryokuShuzen .input-font-20 input {
  font-size: 19px;
  height: 40px !important;
  text-align: right;
}

.MitsumoriNyuryokuShuzen .input-font-20 .el-form-item__label {
  margin-top: 10px !important;
}

.MitsumoriNyuryokuShuzen .border-right-none .cell-body-custome .el-input--suffix{
  border-right: none !important;
}

.MitsumoriNyuryokuShuzen .tblMeisai input{
  color: #000 !important
}
</style>
