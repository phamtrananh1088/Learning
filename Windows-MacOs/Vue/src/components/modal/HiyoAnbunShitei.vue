<template>
<div>
  <el-dialog
    :visible="dialogVisible"
    @open="onOpen()"
    @close="onClose()"
    :close-on-click-modal="false"
    class="HiyoAnbunShitei dialog-class"
    :show-close="false"
  >
    <el-row class="img-content">
      <span
        style="font-size : medium"
        class="cls-span-hiyoAnbunShitei row"
        >取引先： {{Torihikisaki}}
      </span>
    </el-row>
    <reafstable
      ref="tableHiyoAnbunShitei"
      :tableData="dataForm.dt明細部"
      v-loading="loading"
      :maxHeight="379"
      class="dt-detail-hiyo-anbun-shitei"
      :columns="columns"
      :cellStyleOption="cellStyleOption"
      :footer-data="footerData費用按分指定"
      :rowKeyFieldName="rowKey"
    ></reafstable>
    <el-row style="margin-top:10px;">
      <el-col :span="18">
        <p/>
      </el-col>
      <el-col :span="4">
        <el-button :disabled="isDisabledHiyoAnbun" @click="onRegister" style="float: right">更新</el-button>
      </el-col>
      <el-col :span="2">
        <el-button @click="onClose" style="float: right">閉じる</el-button>
      </el-col>
    </el-row>
  </el-dialog>
  <reafsmodalSearchHiyoFutanBumonSabu
      :dialogVisible.sync="showDialog"
      :subParamsObj="subParamsObj"
      @backData="dataBack">
  </reafsmodalSearchHiyoFutanBumonSabu>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafstable from '@/components/table/Grid.vue'
import opensearch from '@/components/basic/openSearch.vue'
import reafsmodalSearchHiyoFutanBumonSabu from '@/views/Reafs-W/search/WS00520/Modal_SearchHiyoFutanBumonSabu.vue'

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafstable,
    opensearch,
    reafsmodalSearchHiyoFutanBumonSabu
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    HiyoAnbunShitei_key: {
      type: Object,
      default: null
    }
  },
  data () {
    return {
      loading: false,
      Torihikisaki: '',
      dataForm: {
        費用負担部門コード: '',
        費用負担部門名: '',
        税率: '',
        税抜按分金額: '',
        税抜按分消費税: '',
        税込按分金額: '',
        dt明細部: this.defaultTableData()
      },
      columns: [
        {
          field: 'No',
          key: 'No',
          title: '',
          width: 3,
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
          key: '1',
          title: '費用負担部門',
          field: '費用負担部門名',
          width: 63,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="hiyoFutanBumon" style="display: inline-flex;">
                <div>
                  <reafsinputtext
                    style="display:flex;"
                    maxlength="8"
                    value={row.費用負担部門コード}
                    ref={'ref費用負担部門' + rowIndex}
                    width="110px"
                    disabled={this.isDisabledHiyoAnbun}
                    on-input={(val) => {
                      row.費用負担部門コード = val
                    }}
                    on-change={(value) => { this.onblurEvent(value, rowIndex) }}
                  >
                  </reafsinputtext>
                </div>
                <div>
                  <el-button
                    class="btnHiyoFutanBumon"
                    icon="el-icon-search"
                    disabled={this.isDisabledHiyoAnbun}
                    on-click={() => { this.onShowDialog(rowIndex) }}
                  ></el-button>
                </div>
                <div>
                  <span style="line-height: 30px;">
                    {row.費用負担部門名}
                  </span>
                </div>
              </div>
            )
          }
        },
        {
          key: '2',
          title: '税率',
          field: '税率',
          width: 6
        },
        {
          key: '3',
          title: '税抜按分金額',
          field: '税抜按分金額',
          width: 17,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.税抜按分金額}
                  ref={'ref税抜按分金額' + rowIndex}
                  width="100%"
                  disabled={this.isDisabledHiyoAnbun}
                  on-input={(val) => {
                    row['税抜按分金額'] = val
                  }}
                  on-change={(value) => { this.onMeisaiChange(value, rowIndex, '税抜按分金額') }}
                ></reafsinputnumber>
              </div>
            )
          }
        },
        {
          key: '4',
          title: '税抜按分消費税',
          field: '税抜按分消費税',
          width: 17,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.税抜按分消費税}
                  ref={'ref税抜按分消費税' + rowIndex}
                  width="100%"
                  disabled={this.isDisabledHiyoAnbun}
                  on-input={(val) => {
                    row['税抜按分消費税'] = val
                  }}
                  on-change={(value) => { this.onMeisaiChange(value, rowIndex, '税抜按分消費税') }}
                ></reafsinputnumber>
              </div>
            )
          }
        },
        {
          key: '5',
          title: '税込按分金額',
          field: '税込按分金額',
          width: 17,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.税込按分金額}
                  ref={'ref税込按分金額' + rowIndex}
                  width="100%"
                  disabled={this.isDisabledHiyoAnbun}
                  on-input={(val) => {
                    row['税込按分金額'] = val
                  }}
                  on-change={(value) => { this.onMeisaiChange(value, rowIndex, '税込按分金額') }}
                ></reafsinputnumber>
              </div>
            )
          }
        }
      ],
      cellStyleOption: {
        headerCellClass: ({ column, rowIndex }) => {

        },
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = 'table-body-cell-class position-relative'
          return css
        },
        footerCellClass: ({ row, column, rowIndex }) => {
          let css = ''
          if (column.key === '2') {
            css += 'text-center'
          }
          return css
        }
      },
      rowKey: 'rowKey',
      rowIndex: 0,
      showDialog: false,
      subParamsObj: {
        taisyou: '', // 承認種類　 　　　　 取引先検索：対象
        jigyousyoCd: '', // 事業所コード
        torType: ''
      },
      税抜按分金額_入力合計: '',
      税抜按分消費税_入力合計: '',
      税込按分金額_入力合計: '',
      isDisabledHiyoAnbun: false
    }
  },
  created () {

  },
  mounted () {

  },
  computed: {
    footerData費用按分指定 () {
      return [
        {
          rowKey: 8,
          費用負担部門コード: '',
          費用負担部門名: '入力合計額',
          税率: this.HiyoAnbunShitei_key.税率,
          税抜按分金額: this.税抜按分金額_入力合計,
          税抜按分消費税: this.税抜按分消費税_入力合計,
          税込按分金額: this.税込按分金額_入力合計
        },
        {
          rowKey: 9,
          費用負担部門コード: '',
          費用負担部門名: '按分元金額',
          税率: this.HiyoAnbunShitei_key.税率,
          税抜按分金額: this.HiyoAnbunShitei_key.率別売上金額税抜,
          税抜按分消費税: this.HiyoAnbunShitei_key.率別売上金額消費税,
          税込按分金額: this.HiyoAnbunShitei_key.率別売上金額税込
        }
      ]
    }
  },
  methods: {
    onblurEvent (value, index) {
      this.rowIndex = index
      const rowIndex = this.rowIndex
      if (value == '') {
        this.dataForm.dt明細部[rowIndex].費用負担部門名 = ''
        this.dataForm.dt明細部[rowIndex].税率 = ''
        this.onMeisaiChange(this.dataForm.dt明細部[rowIndex].税込按分金額, index, '税込按分金額')
      } else {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: value })
          .then((response) => {
            if (response.data !== null) {
              this.dataForm.dt明細部[rowIndex].費用負担部門名 = response.data.費用負担部門
              this.dataForm.dt明細部[rowIndex].税率 = this.HiyoAnbunShitei_key.税率
              this.onMeisaiChange(this.dataForm.dt明細部[rowIndex].税込按分金額, index, '税込按分金額')
            } else {
              this.getMsg('E040747').then((response) => {
                this.showError(response.data, () => {
                  this.$refs[`ref費用負担部門${index}`].focus()
                })
              }).finally(() => {
              })
              this.dataForm.dt明細部[rowIndex].費用負担部門名 = ''
              this.dataForm.dt明細部[rowIndex].税率 = ''
              this.onMeisaiChange(this.dataForm.dt明細部[rowIndex].税込按分金額, index, '税込按分金額')
            }
          })
      }
    },
    onShowDialog (index) {
      this.rowIndex = index
      this.showDialog = true
    },
    dataBack (obj) {
      const rowIndex = this.rowIndex
      this.dataForm.dt明細部[rowIndex].費用負担部門コード = obj.txtCd
      this.dataForm.dt明細部[rowIndex].費用負担部門名 = obj.lblNm
      this.dataForm.dt明細部[rowIndex].税率 = this.HiyoAnbunShitei_key.税率
      this.onMeisaiChange(this.dataForm.dt明細部[rowIndex].税込按分金額, rowIndex, '税込按分金額')
    },
    regisValidate () {
      // １）
      if (this.税抜按分金額_入力合計 !== this.HiyoAnbunShitei_key.率別売上金額税抜 || this.税抜按分消費税_入力合計 !== this.HiyoAnbunShitei_key.率別売上金額消費税 ||
          this.税込按分金額_入力合計 !== this.HiyoAnbunShitei_key.率別売上金額税込) {
        this.getMsg('E040776').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref税込按分金額0.focus()
        return false
      }

      let isDuplicate = false
      let isExceed1円税込按分金額 = false
      let isExceed1円税抜按分消費税 = false
      let keepIndex = 0
      let isNotExist費用負担部門コード = false
      let isExist費用負担部門コード = false
      let isNumberNegative = false
      for (let i = 0; i < this.dataForm.dt明細部.length; i++) {
        keepIndex = i
        // ２）
        for (let j = 0; j < this.dataForm.dt明細部.length; j++) {
          if (i !== j && this.dataForm.dt明細部[i].費用負担部門コード !== '' && this.dataForm.dt明細部[j].費用負担部門コード !== '') {
            if (this.dataForm.dt明細部[i].費用負担部門コード === this.dataForm.dt明細部[j].費用負担部門コード) {
              keepIndex = i
              isDuplicate = true
              break
            }
          }
        }

        if (isDuplicate) {
          break
        }

        if (!this.dataForm.dt明細部[i].費用負担部門コード &&
          (this.dataForm.dt明細部[i].税込按分金額 || this.dataForm.dt明細部[i].税抜按分消費税 || this.dataForm.dt明細部[i].税抜按分金額)) {
          isNotExist費用負担部門コード = true
          break
        }

        if (this.dataForm.dt明細部[i].費用負担部門コード &&
          this.getNumber(this.dataForm.dt明細部[i].税込按分金額) == 0 &&
          this.getNumber(this.dataForm.dt明細部[i].税抜按分消費税) == 0 &&
          this.getNumber(this.dataForm.dt明細部[i].税抜按分金額) == 0) {
          isExist費用負担部門コード = true
          break
        }

        if (this.getNumber(this.dataForm.dt明細部[i].税込按分金額) < 0 ||
          this.getNumber(this.dataForm.dt明細部[i].税抜按分消費税) < 0 ||
          this.getNumber(this.dataForm.dt明細部[i].税抜按分金額) < 0) {
          isNumberNegative = true
          break
        }

        const 税込按分金額 = this.dataForm.dt明細部[i].税込按分金額
        const i税抜按分金額 = this.base.Get_ZeiNuki(this.getNumber(税込按分金額), this.getNumber(this.dataForm.dt明細部[i].税率), '0')
        const i税抜按分消費税 = this.base.ToRoundDown(i税抜按分金額 * this.getNumber(this.dataForm.dt明細部[i].税率) / 100, 0)
        const dis税抜按分金額 = this.getNumber(this.dataForm.dt明細部[i].税抜按分金額)
        if ((dis税抜按分金額 > i税抜按分金額 + 1) || (dis税抜按分金額 < i税抜按分金額 - 1)) {
          isExceed1円税込按分金額 = true
          break
        }
        const dis税抜按分消費税 = this.getNumber(this.dataForm.dt明細部[i].税抜按分消費税)
        if ((dis税抜按分消費税 > i税抜按分消費税 + 1) || (dis税抜按分消費税 < i税抜按分消費税 - 1)) {
          isExceed1円税抜按分消費税 = true
          break
        }
      }

      if (isDuplicate) {
        this.getMsg('E040778').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs[`ref費用負担部門${keepIndex}`].focus()
          }})
        })
        return false
      }

      // ３）
      if (!this.dataForm.dt明細部.find(x => !!x.費用負担部門コード) || isNotExist費用負担部門コード) {
        this.getMsg('E040373').then((response) => {
          this.showError(response.data, {callback: () => {
            const index = !this.dataForm.dt明細部.find(x => !!x.費用負担部門コード) ? 0 : keepIndex
            this.$refs[`ref費用負担部門${index}`].focus()
          }})
        })
        return false
      }

      // ４）
      if (isExist費用負担部門コード) {
        this.getMsg('E040780').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs[`ref税込按分金額${keepIndex}`].focus()
          }})
        })
        return false
      }

      if (isNumberNegative) {
        this.getMsg('E010162').then((response) => {
          this.showError(response.data, {callback: () => {
            if (this.getNumber(this.dataForm.dt明細部[keepIndex].税込按分金額) < 0) {
              this.$refs[`ref税込按分金額${keepIndex}`].focus()
            } else if (this.getNumber(this.dataForm.dt明細部[keepIndex].税抜按分消費税) < 0) {
              this.$refs[`ref税抜按分消費税${keepIndex}`].focus()
            } else if (this.getNumber(this.dataForm.dt明細部[keepIndex].税抜按分金額) < 0) {
              this.$refs[`ref税抜按分金額${keepIndex}`].focus()
            }
          }
          })
        })
        return false
      }

      if (isExceed1円税込按分金額 || isExceed1円税抜按分消費税) {
        this.getMsg('E040779').then((response) => {
          this.showError(response.data, {callback: () => {
            if (isExceed1円税込按分金額) {
              this.$refs[`ref税抜按分金額${keepIndex}`].focus()
            } else {
              this.$refs[`ref税抜按分消費税${keepIndex}`].focus()
            }
          }})
        })
        return false
      }

      return true
    },
    async onRegister () {
      if (!await this.regisValidate()) {
        return false
      }

      let msg = (await this.getMsg('K000005')).data
      if (!await this.confirm(msg)) {
        return false
      }
      this.loading = true
      const data = {
        会社コード: this.HiyoAnbunShitei_key.会社コード,
        工事依頼NO: this.HiyoAnbunShitei_key.工事依頼No,
        工事依頼NO枝番: this.HiyoAnbunShitei_key.工事依頼No枝番,
        業者コード: this.HiyoAnbunShitei_key.業者コード,
        業者コード枝番: this.HiyoAnbunShitei_key.業者コード枝番,
        検収回数: this.HiyoAnbunShitei_key.検収回数,
        税率: this.HiyoAnbunShitei_key.税率,
        入力区分: this.HiyoAnbunShitei_key.入力区分,
        dt修繕部門按分設定ファイル: this.getData費用按分指定(),
        tabmode: this.HiyoAnbunShitei_key.tabmode,
        費用按分: this.HiyoAnbunShitei_key.費用按分
      }
      let res = {}
      await this.http
        .post(
          '/api/Reafs_W/Shonin/WD00520/Update費用按分指定',
          data,
          'アクセスしています....'
        )
        .then(response => {
          this.loading = false
          res = response
        })
        .catch(ex => {
          // this.showError(ex)
        })
        .finally(() => {
          this.loading = false
        })
      if (res.status) {
        this.getMsg('M000012').then((response) => {
          this.showSuccess(response.data)
        })
        this.$emit('UpdateSuccess')
        return true
      }
    },
    getData費用按分指定 () {
      const arrData = []
      for (let i = 0; i < this.dataForm.dt明細部.length; i++) {
        const row = this.dataForm.dt明細部[i]
        if (row.費用負担部門コード !== '' && row.税抜按分金額 !== '' && row.税抜按分消費税 !== '' && row.税込按分金額 !== '') {
          const data = {
            会社コード: this.HiyoAnbunShitei_key.会社コード,
            工事依頼NO: this.HiyoAnbunShitei_key.工事依頼No,
            工事依頼NO枝番: this.HiyoAnbunShitei_key.工事依頼No枝番,
            業者コード: this.HiyoAnbunShitei_key.業者コード,
            業者コード枝番: this.HiyoAnbunShitei_key.業者コード枝番,
            検収回数: this.HiyoAnbunShitei_key.検収回数,
            税率: row.税率,
            按分サブNO: i + 1,
            入力区分: this.HiyoAnbunShitei_key.入力区分,
            負担部門コード: row.費用負担部門コード,
            税抜按分金額: row.税抜按分金額,
            按分消費税: row.税抜按分消費税,
            税込按分金額: row.税込按分金額,
            発注サブNO: this.HiyoAnbunShitei_key.発注サブNO,
            削除区分: 0
          }
          arrData.push(data)
        }
      }
      return arrData
    },
    async getDataAnbunNyuryokuSumiMesseji () {
      let res
      await this.http
        .get('/api/Reafs_W/Shonin/WD00520/GetData費用按分指定',
          {
            会社コード: this.HiyoAnbunShitei_key.会社コード,
            工事依頼No: this.HiyoAnbunShitei_key.工事依頼No,
            工事依頼No枝番: this.HiyoAnbunShitei_key.工事依頼No枝番,
            業者コード: this.HiyoAnbunShitei_key.業者コード,
            業者コード枝番: this.HiyoAnbunShitei_key.業者コード枝番,
            税率: this.HiyoAnbunShitei_key.税率,
            検収回数: this.HiyoAnbunShitei_key.検収回数,
            tabmode: this.HiyoAnbunShitei_key.tabmode
          }).then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        const data = res.data.dt費用按分指定
        while (data.length < 9) {
          data.push({
            費用負担部門コード: '',
            費用負担部門名: '',
            税率: '',
            税抜按分金額: '',
            税抜按分消費税: '',
            税込按分金額: ''
          })
        }

        if (this.HiyoAnbunShitei_key.未入力mode === '未入力') {
          data[0].費用負担部門コード = this.HiyoAnbunShitei_key.費用負担部門コード
          await this.http
            .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: data[0].費用負担部門コード })
            .then((response) => {
              if (response.data !== null) {
                data[0].費用負担部門名 = response.data.費用負担部門
                data[0].税率 = this.HiyoAnbunShitei_key.税率
              } else {
                this.getMsg('E040747').then((response) => {
                  this.showError(response.data, () => {
                    this.$refs.ref費用負担部門0.focus()
                  })
                }).finally(() => {
                })
                data[0].費用負担部門名 = ''
              }
            })
          data[0].税抜按分金額 = this.HiyoAnbunShitei_key.率別売上金額税抜
          data[0].税抜按分消費税 = this.HiyoAnbunShitei_key.率別売上金額消費税
          data[0].税込按分金額 = this.HiyoAnbunShitei_key.率別売上金額税込
        }
        this.dataForm.dt明細部 = data
        this.isDisabledHiyoAnbun = res.data.iEnableHiyoAnbun != '1'// ハノイ側修正2023/01/16　STEP2_W　課題管理表№271：設計書「修正履歴」シートの「2022/01/11仕様変更分」「費用按分指定画面明細部の入力不可条件追記、按分入力ボタン押下条件追記」
      }
      this.sumHiyoAnbunShitei()
    },
    async onOpen () {
      this.loading = true
      this.dataForm.dt明細部 = this.defaultTableData()
      this.Torihikisaki = this.HiyoAnbunShitei_key.取引先コード + '　' + this.HiyoAnbunShitei_key.取引先名
      await this.getDataAnbunNyuryokuSumiMesseji()
      this.loading = false
    },
    onClose () {
      this.$emit('update:dialogVisible', false)
    },
    onMeisaiChange (value, index, column) {
      this.rowIndex = index
      const rowIndex = this.rowIndex
      if (column === '税込按分金額') {
        if (this.getNumber(value) < 0) {
          this.getMsg('E010162').then((response) => {
            this.showError(response.data)
          })
        }
        if (value !== '') {
          const i税抜按分金額 = this.base.Get_ZeiNuki(this.getNumber(value), this.getNumber(this.dataForm.dt明細部[rowIndex].税率), '0')
          const i税抜按分消費税 = this.base.ToRoundDown(i税抜按分金額 * this.getNumber(this.dataForm.dt明細部[rowIndex].税率) / 100, 0)
          this.dataForm.dt明細部[rowIndex].税抜按分消費税 = i税抜按分消費税
          this.dataForm.dt明細部[rowIndex].税抜按分金額 = i税抜按分金額
        } else {
          this.dataForm.dt明細部[rowIndex].税抜按分消費税 = ''
          this.dataForm.dt明細部[rowIndex].税抜按分金額 = ''
        }
      } else if (column === '税抜按分金額') {
        if (this.getNumber(value) < 0) {
          this.getMsg('E010162').then((response) => {
            this.showError(response.data)
          })
        }

        const 税込按分金額 = this.dataForm.dt明細部[rowIndex].税込按分金額
        if (税込按分金額) {
          const i税抜按分金額 = this.base.Get_ZeiNuki(this.getNumber(税込按分金額), this.getNumber(this.dataForm.dt明細部[rowIndex].税率), '0')
          if ((this.getNumber(value) > i税抜按分金額 + 1) || (this.getNumber(value) < i税抜按分金額 - 1)) {
            this.getMsg('E040779').then((response) => {
              this.showError(response.data, {callback: () => {
                this.$refs[`ref税抜按分金額${rowIndex}`].focus()
              }})
            })
          }
        }
      } else if (column === '税抜按分消費税') {
        if (this.getNumber(value) < 0) {
          this.getMsg('E010162').then((response) => {
            this.showError(response.data)
          })
        }

        const 税込按分金額 = this.dataForm.dt明細部[rowIndex].税込按分金額
        if (税込按分金額) {
          const i税抜按分金額 = this.base.Get_ZeiNuki(this.getNumber(税込按分金額), this.getNumber(this.dataForm.dt明細部[rowIndex].税率), '0')
          const i税抜按分消費税 = this.base.ToRoundDown(i税抜按分金額 * this.getNumber(this.dataForm.dt明細部[rowIndex].税率) / 100, 0)
          if ((this.getNumber(value) > i税抜按分消費税 + 1) || (this.getNumber(value) < i税抜按分消費税 - 1)) {
            this.getMsg('E040779').then((response) => {
              this.showError(response.data, {callback: () => {
                this.$refs[`ref税抜按分消費税${rowIndex}`].focus()
              }})
            })
          }
        }
      }
      setTimeout(() => {
        this.sumHiyoAnbunShitei()
      }, 200)
    },
    getNumber (string) {
      if (!string) return 0
      return (string + '' || '0').replaceAll(',', '') * 1
    },
    defaultTableData () {
      return [
        {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }, {
          費用負担部門コード: '',
          費用負担部門名: '',
          税率: '',
          税抜按分金額: '',
          税抜按分消費税: '',
          税込按分金額: ''
        }
      ]
    },
    sumHiyoAnbunShitei () {
      let sum税抜按分金額 = 0
      let sum税抜按分消費税 = 0
      let sum税込按分金額 = 0

      for (let i = 0; i < this.dataForm.dt明細部.length; i++) {
        const row = this.dataForm.dt明細部[i]
        sum税抜按分金額 += this.getNumber(row.税抜按分金額)
        sum税抜按分消費税 += this.getNumber(row.税抜按分消費税)
        sum税込按分金額 += this.getNumber(row.税込按分金額)
      }

      this.税抜按分金額_入力合計 = this.base.formatCommaNumeric(sum税抜按分金額)
      this.税抜按分消費税_入力合計 = this.base.formatCommaNumeric(sum税抜按分消費税)
      this.税込按分金額_入力合計 = this.base.formatCommaNumeric(sum税込按分金額)
    },
    hideLoading () {
      this.loading = false
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    confirm (message, title = '依頼参照・承認（発注申請）') {
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
    showError (message, option = null, title = '依頼参照・承認（発注申請）') {
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
    showSuccess (message, title = '依頼参照・承認（発注申請）') {
      return new Promise((resolve, reject) => {
        var self = this
        this.MsgInfo({ title,
          message,
          callback: function (a, b) {
            self.hideLoading()
            resolve(true)
          }})
      })
    }
  }
}
</script>

<style lang="less" scoped>
.home-contianer {
  padding: 5px;
}
.el-dialog {
  margin: 0;
  padding: 0;
  position: top;
}

.dialog {
  position: scroll;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  padding: 0;
  max-width: 100%;
  min-width: 320px;
  height: auto;
  backface-visibility: hidden;
  position: top;
}
.cls-tbl td {
  vertical-align: top;
}
.el-form-item {
  margin: 1px;
  padding: 0;
}
.row {
  margin: 1px 5px;
}
.readonly_text {
  padding-left: 2px;
  padding: 0.5px 0;
}

</style>

<style scoped>
.el-dialog__wrapper.dialog-class >>> .el-dialog {
  width: 1150px;
  /* height: 740px; */
  overflow: overlay;
  max-width: -webkit-fill-available;
  max-height: -webkit-fill-available;
}
.HiyoAnbunShitei.dialog-class >>> .el-dialog__header {
  padding: 0px 20px 10px;
}
.HiyoAnbunShitei.dialog-class >>> .el-dialog__body {
  padding: 0px 10px 30px;
  width: 1150px;
  /* height: 750px; */
}

.el-form .multi >>> .el-form-item__label {
  height: 94px;
}
.el-form .multi_text >>> .el-form-item__label {
  height: 52px;
}
.cls-tbl {
  border-spacing: 0px 0px;
  min-height: 1px;
  border-collapse: collapse;
  cellspacing: "0px";
  border-spacing: 0px 0px;
  padding: 0;
  margin: 10px 0;
  width: 700px;
}
.p-sagyocategory {
  line-height: $div-height;
  width: 83px;
  height: 95px;
  line-height: 98px;
  background: #9bc2e6;
  padding: 0px;
  margin: 0px;
  color: white;
}
.cls-span-hiyoAnbunShitei {
  height: 30px;
  line-height: 30px;
  padding: 5px;
  margin: 0px;
}
table,
td {
  padding: 0px;
  margin: 0;
}

table {
  margin: 5px;
}

.btnHiyoFutanBumon {
  width: 40px;
  height: 30px;
  padding: 0 12px 0 12px !important;
  margin-left: -4px;
}

.HiyoAnbunShitei .border-left {
  border-left: none !important;
}

.HiyoAnbunShitei .hiyoFutanBumon {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.HiyoAnbunShitei .hiyoFutanBumon > div:nth-child(1) {
  /* padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 5px; */
}
.HiyoAnbunShitei .hiyoFutanBumon > div:nth-child(2) {
  border-right: solid 1px #000;
  /* padding-top: 5px;
  padding-bottom: 5px;
  padding-right: 5px; */
}
.HiyoAnbunShitei .hiyoFutanBumon > div:nth-child(3) {
  padding-left: 5px;
}
</style>

<style>
  .dt-detail-hiyo-anbun-shitei td.position-relative {
    position: relative;
  }

  .dt-detail-hiyo-anbun-shitei td.text-center {
    text-align: center !important;
  }
  .SyuzenIraiSansyoShonin .ve-table .ve-table-container table.ve-table-content tfoot.ve-table-footer tr.ve-table-footer-tr td.ve-table-footer-td.text-center {
    text-align: center !important;
  }
  .HiyoAnbunShitei .dt-detail-hiyo-anbun-shitei .ve-table .ve-table-container {
    height: 470px !important;
  }
  .HiyoAnbunShitei .dt-detail-hiyo-anbun-shitei .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr td.ve-table-body-td {
    padding: 0px;
  }

 .HiyoAnbunShitei table tfoot tr:nth-child(1) td {
  border-bottom: none !important;
 }

.HiyoAnbunShitei table tfoot tr:nth-child(1)  td:nth-child(1){
  border-bottom: none !important;
 }
</style>
