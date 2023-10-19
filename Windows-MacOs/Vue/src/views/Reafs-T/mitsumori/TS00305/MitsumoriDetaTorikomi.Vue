<template>
  <div class="home-container MitsumoriDetaTorikomi">
    <el-form ref="refMitsumoriDetaTorikomiForm" label-width="120px" class="sample-form-class"
      :hide-required-asterisk="true">
      <el-container class="home-el-container" style="margin-bottom: 20px">
        <el-main class="home-el-main">
          <el-row>
            <el-col :span="8">
              <el-form-item label="営業店">
                <div style="display: flex">
                  <div>
                    <reafsinputtext v-model="pageData.営業店コード" class="is-readonly" maxlength="6" disabled="disabled"
                      :width="'95px'">
                    </reafsinputtext>
                  </div>
                  <div class="marginLeft">
                    <reafsinputtext class="is-readonly" :title="pageData.営業店名称" v-model="pageData.営業店名称" maxlength="20"
                      disabled="disabled" :width="'100%'">
                    </reafsinputtext>
                  </div>
                </div>
              </el-form-item>
              <el-form-item label="物件">
                <div style="display: flex">
                  <div>
                    <reafsinputtext class="is-readonly" v-model="pageData.物件コード" maxlength="8" disabled="disabled"
                      :width="'95px'">
                    </reafsinputtext>
                  </div>
                  <div class="marginLeft">
                    <reafsinputtext :title="pageData.物件名" class="is-readonly" v-model="pageData.物件名" maxlength="50"
                      disabled="disabled" :width="'100%'">
                    </reafsinputtext>
                  </div>
                </div>
              </el-form-item>
              <el-form-item label="工事件名">
                <reafsinputtext v-model="pageData.工事件名" :title="pageData.工事件名" class="is-readonly" maxlength="60"
                  disabled="disabled" :width="'276'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="15" class="marginLeft">
              <div style="display: flex">
                <el-form-item label="見積№">
                  <reafsinputtext v-model="pageData.見積No" class="is-readonly" maxlength="11" disabled="disabled"
                    :width="'120px'">
                  </reafsinputtext>
                </el-form-item>
                <div style="width: 212px; margin-left: 15px; display: flex; align-items: center;">
                  <span>見積依頼日: {{ pageData.見積依頼日 }}</span>
                </div>
                <div style="margin-left: 15px; display: flex; align-items: center;">
                  <span>工事予定日: {{ pageData.工事予定日 }}</span>
                </div>
              </div>
              <el-form-item label="物件住所">
                <div style="display: flex">
                  <div>
                    <reafsinputtext v-model="pageData.物件住所郵便番号" class="is-readonly" maxlength="8" disabled="disabled"
                      :width="'120px'">
                    </reafsinputtext>
                  </div>
                  <div class="marginLeft">
                    <reafsinputtext v-model="pageData.物件住所" class="is-readonly" maxlength="30" disabled="disabled"
                      :width="'520px'">
                    </reafsinputtext>
                  </div>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <span>見積明細データCSV</span>
              <el-form-item label="ファイル">
                <div style="display: flex; flex-flow: column;">
                  <div style="display: flex;">
                    <reafsinputtext v-model="pageData.ファイル" :width="'344px'" @click="openFileDialog">
                    </reafsinputtext>
                    <input ref="refFile" type="file" style="display:none;" id="file" name="file" />
                    <el-button @click="openFileDialog" style="margin-left: 5px;" size="mini">参照
                    </el-button>
                  </div>
                  <el-button @click="onToroku" style="width: 65px;margin-left: 130px; margin-top: 2px" size="mini">登録
                  </el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <div>
                <span style="margin-left: 30px">★必須項目</span>
                <span style="margin-left: 140px">割宛項目</span>
              </div>
              <reafstable ref="refWariateKomoku" class="tableWariateKomoku" :tableData="tableDataWariateKomoku" :defaultSelectedRowKeys="['分類']"
                :columns="columnsWariateKomoku" rowKeyFieldName="項目" :cellStyleOption="cellStyleOptionWariateKomoku" :maxHeight="383"
                :disableSelectedRowKeys="['作業カテゴリ大','作業カテゴリ中','作業カテゴリ小']" />
              <div>※金額は（税抜）・（税込）のいずれか指定してください</div>
            </el-col>
            <el-col :span="2" style="    display: flex;flex-flow: column;margin-top: 125px;">
              <el-button @click="onWariate" style="margin: 10px">←割当
              </el-button>
              <el-button @click="onKaijo" style="margin: 10px">解除→
              </el-button>
            </el-col>
            <el-col :span="4">
              <div>＜＜取込みデータリスト＞＞</div>
              <reafstable ref="refTorikomiDetarisuto" class="tableTorikomiDetarisuto" :tableData="tableDataTorikomiDetarisuto"
                :columns="columnsTorikomiDetarisuto" :cellStyleOption="cellStyleOptionTorikomiDetarisuto"
                :maxHeight="383" rowKeyFieldName="項目" />
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto" style="display: flex">
          <div style="margin-left: 30px;">
            <el-button @click="onDownloadTemp" style="margin: 10px">サンプルダウンロード</el-button>
          </div>
          <div style="margin-left: 20px;">
            <el-button @click="onBack" style="margin: 10px">閉じる</el-button>
          </div>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafstable from '@/components/table/Grid.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafstable
  },
  data () {
    return {
      queryParams: {
        会社コード: '',
        工事依頼No: '',
        工事依頼No枝番: '',
        業者コード: '',
        業者コード枝番: ''
      },
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      pageData: {
        営業店コード: '',
        営業店名称: '',
        見積No: '',
        見積依頼日: '',
        工事予定日: '',
        物件コード: '',
        物件名: '',
        物件住所郵便番号: '',
        物件住所: '',
        工事件名: '',
        ファイル: '',
        fileDataBase64: '',
        tempFilePath: ''
      },
      columnsWariateKomoku: [
        {
          field: '',
          key: '1',
          type: 'radio',
          title: '',
          width: 28,
          align: 'center'
        },
        {
          key: '2',
          title: '項目',
          field: '項目',
          width: 150,
          ellipsis: {
            showTitle: true
          }
        },
        {
          key: '3',
          title: '桁数',
          field: '桁数',
          width: 60,
          ellipsis: {
            showTitle: true
          }
        },
        {
          key: '4',
          title: '項目名',
          field: '項目名',
          align: 'left'
        }
      ],
      columnsTorikomiDetarisuto: [
        {
          field: '',
          key: '1',
          type: 'radio',
          title: '',
          width: 28,
          align: 'center'
        },
        {
          key: '2',
          title: '項目',
          field: '項目',
          ellipsis: {
            showTitle: true
          }
        }
      ],
      tableDataWariateKomoku: this.getDefaultWariateKomoku(),
      tableDataTorikomiDetarisuto: [],
      torikomiDetaTable: []
    }
  },
  watch: {

  },
  computed: {
    cellStyleOptionWariateKomoku () {
      return {
        headerCellClass: ({ row, column, rowIndex }) => { },
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = ''
          if (column.key == '2') {
            css += ' text-left'
          }

          if (column.key == '3') {
            css += ' text-right'
          }

          // if (column.key == '4' && ((this.tableDataTorikomiDetarisuto && this.tableDataTorikomiDetarisuto.find(x => row.項目.includes(x.項目))) || row.isHightLight)) {
          //   css += ' hightlight'
          // }
          if (column.key == '4') {
            const rowAdd = this.tableDataTorikomiDetarisuto && this.tableDataTorikomiDetarisuto.find(x => x.項目.includes(row.項目名))
            if (rowAdd && rowAdd.項目.replaceAll(',', '') == row.項目名.replaceAll(',', '') ) {
              if(rowAdd.項目.replaceAll(',', '') != ''){
                css += ' hightlight'
              }
            }
          }
          if (column.key === '2' || column.key === '3') {
            css += ' is-gray'
          }
          return css
        }
      }
    },
    cellStyleOptionTorikomiDetarisuto () {
      return {
        headerCellClass: ({ row, column, rowIndex }) => { },
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = ''
          if (column.key == '2') {
            css += ' text-left'
          }
          return css
        }
      }
    }
  },
  inject: ['hideLeftMenu', 'setBtnBackShow'],
  created () {
    let userLogIn = this.$store.getters.getUserInfo()
    this.userInfo.ユーザーＩＤ = userLogIn.userId
    this.userInfo.会社コード = userLogIn.会社コード
  },
  mounted () {
    this.queryParams = {
      会社コード: this.userInfo.会社コード,
      工事依頼No: this.$route.query.工事依頼No,
      工事依頼No枝番: this.$route.query.工事依頼No枝番,
      業者コード: this.$route.query.業者コード,
      業者コード枝番: this.$route.query.業者コード枝番
    }
    this.hideLeftMenu(false)
    this.setBtnBackShow(false)
    this.getPageData()
  },
  methods: {
    onWariate () {
      const lstSelectWariateKomoku = this.$refs.refWariateKomoku.selectedRowKeys
      const lstSelectTorikomiDetarisuto = this.$refs.refTorikomiDetarisuto.selectedRowKeys
      if (!lstSelectWariateKomoku || lstSelectWariateKomoku.length <= 0 ||
        !lstSelectTorikomiDetarisuto || lstSelectTorikomiDetarisuto.length <= 0) {
        return
      }
      const value = lstSelectTorikomiDetarisuto[0]
      const row = this.tableDataWariateKomoku.find(x => x.項目.includes(lstSelectWariateKomoku[0]))
      row.項目名 = value
      row.isHightLight = true
      //20221028 rep 税抜か税込どちらか一方のみ選択できるように修正
      if(row.項目.includes('金額（税抜）')){
        const kaijoRow = this.tableDataWariateKomoku.find(x => x.項目.includes('金額（税込）'))
        kaijoRow.項目名 = ''
        kaijoRow.isHightLight = false
      }
      if(row.項目.includes('金額（税込）')){
        const kaijoRow = this.tableDataWariateKomoku.find(x => x.項目.includes('金額（税抜）'))
        kaijoRow.項目名 = ''
        kaijoRow.isHightLight = false
      }
          //20221028 rep
    },
    onKaijo () {
      const lstSelectWariateKomoku = this.$refs.refWariateKomoku.selectedRowKeys
      if (!lstSelectWariateKomoku || lstSelectWariateKomoku.length <= 0) {
        return
      }

      const index = this.tableDataWariateKomoku.findIndex(x => x.項目.includes(lstSelectWariateKomoku[0]))
      this.tableDataWariateKomoku[index].項目名 = ''
      this.tableDataWariateKomoku[index].isHightLight = false
    },
    async onDownloadTemp () {
      if (!this.pageData.tempFilePath) {
        return
      }
      const FileDownloadInfo = {
        FilePath: this.pageData.tempFilePath,
        FileName: 'fileName'
      }

      await this.http
        .post('/api/Reafs_W/Common/DownloadFile', FileDownloadInfo, '', {
          responseType: 'blob'
        })
        .then(async blob => {
          // ハノイ側修正2022/10/21　STEP2_W　課題管理表№137：設計書「修正履歴」シートの「2022/10/21仕様変更分」「ダウンロード0KBデータの添付エラー」Start
          // if (blob.size === 0) {
          //   this.getMsg('E040464').then(res => {
          //     this.showError(res.data)
          //   })
          //   return
          // }
          // ハノイ側修正2022/10/21　STEP2_W　課題管理表№137：設計書「修正履歴」シートの「2022/10/21仕様変更分」「ダウンロード0KBデータの添付エラー」End
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: this.pageData.tempFilePath.split('\\')[this.pageData.tempFilePath.split('\\').length - 1]
          // })
          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()

          await this.base.saveFileDownload(blob, this.pageData.tempFilePath.split('\\')[this.pageData.tempFilePath.split('\\').length - 1])
        })
        .catch(ex => console.log(ex))
        .finally(() => {
        })
        .catch(ex => console.log(ex))
    },
    async onSansho () {
      if (!this.pageData.ファイル || !this.pageData.fileDataBase64) {
        this.pageData.ファイル = ''
        return
      }
      const data = {
        base64Data: this.pageData.fileDataBase64
      }
      let res
      await this.http
        .get('/api/Reafs_T/MitsumoriDetaTorikomi/ReadFileCsv', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
          console.log(ex)
        }).finally(() => {
        })
      if (res.status) {
        if (res.data) {
          this.tableDataTorikomiDetarisuto = []
          /*for (let propertyName in res.data) {
            this.tableDataTorikomiDetarisuto.push({項目: propertyName, 項目名: res.data[propertyName]})
          }*/
          for (let propertyName in res.data) {
            this.tableDataTorikomiDetarisuto.push({項目: propertyName, 項目名: propertyName})
          }
        } else {
          this.showError(res.message)
        }
     }
      await this.http
        .get('/api/Reafs_T/MitsumoriDetaTorikomi/ReadFileCsvData', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
          console.log(ex)
        }).finally(() => {
        })
        if (res.status) {
          if (res.data) {
            this.torikomiDetaTable = res.data
          }
        }
    },
    validateSave () {
      const lstValidate = this.tableDataWariateKomoku.filter(x => x.項目.includes('★'))
      for (let row of lstValidate) {
        if (row.項目.includes('金額（税抜）') || row.項目.includes('金額（税込）')) {
          const 金額税込 = this.tableDataWariateKomoku.find(x => x.項目.includes('金額（税込）'))
          const 金額税込Add = this.tableDataTorikomiDetarisuto.find(x => x.項目.includes(金額税込.項目名))

          const 金額税抜 = this.tableDataWariateKomoku.find(x => x.項目.includes('金額（税抜）'))
          const 金額税抜Add = this.tableDataTorikomiDetarisuto.find(x => x.項目.includes(金額税抜.項目名))

          if (!((金額税込Add) || (金額税抜Add))) {
            this.getMsg('E040737').then((response) => {
              this.showError(response.data)
            })
            return false
          }

          continue
        }
        const rowAdd = this.tableDataTorikomiDetarisuto.find(x => x.項目.includes(row.項目名))
        if (!rowAdd) {
          this.getMsg('E040737').then((response) => {
            this.showError(response.data)
          })
          return false
        }
      }
      return true
    },
    async onToroku () {
      if (!this.validateSave()) {
        return false
      }

      let msg = (await this.getMsg('K040072')).data
      if (!await this.confirm(msg)) {
        return false
      }

      /*const 金額税込 = this.getBackData('金額（税込）')
      let 金額税抜 = this.getBackData('金額（税抜）')
      let 金額税込Add = this.getTblData('金額（税込）').replaceAll(',', '')
      // let 金額税抜Add = this.getTblData('金額（税抜）').replaceAll(',', '')
      if ((金額税込 === 金額税込Add) && 金額税込) {
        const 税率 = this.getBackData('税率')
        金額税抜 = Number(金額税込) / (100 + Number(税率 || 0)) * 100
        金額税抜 = this.base.ToRoundDown(金額税抜)
      }*/

      const data = {
        F046_業者見積項目割当マスタ: {
          業者コード: this.queryParams.業者コード,
          業者コード枝番: this.queryParams.業者コード枝番,
          分類: this.getTblData('分類'),
          作業カテゴリ大: this.getTblData('作業カテゴリ大'),
          作業カテゴリ中: this.getTblData('作業カテゴリ中'),
          作業カテゴリ小: this.getTblData('作業カテゴリ小'),
          作業内容: this.getTblData('作業内容★'),
          数量: this.getTblData('数量'),
          単位: this.getTblData('単位'),
          金額_税抜: this.getTblData('金額（税抜）★'),
          課税区分: this.getTblData('課税区分★'),
          税率: this.getTblData('税率★'),
          金額_税込: this.getTblData('金額（税込）★'),
          削除区分: 0
        }
      }
      let res
      await this.http
        .post('/api/Reafs_T/MitsumoriDetaTorikomi/Update', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        /*let 単価 = this.getBackData('単価')
        if (this.getBackData('数量')) {
          単価 = this.base.ToRoundDown(金額税抜 / Number(this.getBackData('数量')))
        }*/

        /*window.parent.postMessage({
          'call': 'closePopup',
          backData: {
            分類: this.getBackData('分類'),
            作業内容: this.getBackData('作業内容'),
            数量: this.getBackData('数量'),
            単位: this.getBackData('単位'),
            単価: 単価,
            金額_税抜: 金額税抜,
            税区: this.getBackData('税区'),
            税率: this.getBackData('税率')
          }
        }, '*')*/
      this.showSuccess("作成開始")
        window.parent.postMessage({
          'call': 'closePopup',
          backData: this.getCsvData()
        }, '*')
      } else {
        this.showError(res.message)
      }
    },
    getCsvData () {
      var data = []
      let 金額税込Add = this.getTblData('金額（税込）')
      for (let row of this.torikomiDetaTable) {
        let 数量 = row[this.getTblData('数量')]
        let 税率 = row[this.getTblData('税率')]
        let 税区 = row[this.getTblData('課税区分')]
        let 分類 = row[this.getTblData('分類')]
        let 作業内容 = row[this.getTblData('作業内容')]
        if(!(税区 == '1' || 税区 == '2')){
          税区 = ''
        }
         //20230517 add 分類の取込時の不具合を修正
        if(!(分類 == 'G' || 分類 == 'T')){
          分類 = ''
        }
        if(分類 == 'G'){
          if(作業内容 == ''){
            作業内容 = '小計'
          }
        }
        if(金額税込Add != ''){
          let 金額税込 = row[this.getTblData('金額（税込）')]
          if (金額税込 == '' ){
            金額税込 = 0
          }
          //20221028 rep 税抜金額の計算式を変更
          //let 金額税抜 = this.base.ToRoundDown(Number(金額税込) / (100 + Number(税率 || 0)) * 100)
          let 消費税相当額 = this.base.ToRoundDown((Number(金額税込) * Number(税率 || 0)) / (100 + Number(税率 || 0)),0)
          let 金額税抜 = Number(金額税込) - 消費税相当額
          //20221028 rep
          let 単価  = this.base.ToRoundDown(金額税抜 / Number(数量))
          data.push({
            分類 : 分類,
            作業内容 : 作業内容,
            数量 : 数量,
            単位 : row[this.getTblData('単位')],
            単価 : 単価,
            金額_税抜 : 金額税抜,
            税区 : 税区,
            税率 : 税率
          })
        }else{
          let 金額税抜 = row[this.getTblData('金額（税抜）') ]
          if (金額税抜 == '' ){
            金額税抜 = 0
          }
          let 単価  = this.base.ToRoundDown(金額税抜 / Number(数量))
          data.push({
            分類 : 分類,
            作業内容 : 作業内容,
            数量 : 数量,
            単位 : row[this.getTblData('単位')],
            単価 : 単価,
            金額_税抜 : 金額税抜,
            税区 : 税区,
            税率 : 税率
          }) 
        }
      }
      return data
    },
    getBackData (項目) {
      const row = this.tableDataTorikomiDetarisuto.find(x => x.項目.includes(項目))
      return row ? row.項目名 : ''
    },
    getTblData (項目) {
      const row = this.tableDataWariateKomoku.find(x => x.項目.includes(項目))
      return row ? row.項目名 : ''
    },
    openFileDialog (index) {
      const me = this
      var input = document.createElement('input')
      input.type = 'file'
      input.accept = '.csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      input.onchange = e => {
        var file = e.target.files[0]
        if (!file) {
          return
        }
        // FileReader support
        if (FileReader && file) {
          me.pageData.ファイル = file.name
          var fr = new FileReader()
          fr.onload = function () {
            me.pageData.fileDataBase64 = fr.result.split(',')[1]
            me.onSansho()
          }
          fr.readAsDataURL(file)
          input.remove()
        } else {
          // Not supported
          // fallback -- perhaps submit the input to an iframe and temporarily store
          // them on the server until the user's session ends.
          input.remove()
        }
      }
      input.click()
    },
    getDefaultWariateKomoku () {
      return [
        {
          項目: '分類',
          桁数: 1,
          項目名: ''
        },
        {
          項目: '作業カテゴリ大',
          桁数: 4,
          項目名: ''
        },
        {
          項目: '作業カテゴリ中',
          桁数: 4,
          項目名: ''
        },
        {
          項目: '作業カテゴリ小',
          桁数: 4,
          項目名: ''
        },
        {
          項目: '作業内容★',
          桁数: 100,
          項目名: ''
        },
        {
          項目: '数量',
          桁数: 4,
          項目名: ''
        },
        {
          項目: '単位',
          桁数: 8,
          項目名: ''
        },
        {
          項目: '金額（税抜）★',
          桁数: 10,
          項目名: ''
        },
        {
          項目: '課税区分★',
          桁数: 1,
          項目名: ''
        },
        {
          項目: '税率★',
          桁数: 3,
          項目名: ''
        },
        {
          項目: '金額（税込）★',
          桁数: 10,
          項目名: ''
        }
      ]
    },
    async getPageData () {
      await this.http
        .get('/api/Reafs_T/MitsumoriDetaTorikomi/GetPageData',
          {
            会社コード: this.userInfo.会社コード,
            工事依頼No: this.queryParams.工事依頼No,
            工事依頼No枝番: this.queryParams.工事依頼No枝番,
            業者コード: this.queryParams.業者コード,
            業者コード枝番: this.queryParams.業者コード枝番
          })
        .then((response) => {
          let 依頼情報 = response.data.依頼情報
          if (依頼情報 && 依頼情報.length > 0) {
            依頼情報 = 依頼情報[0]
            this.pageData.営業店コード = 依頼情報.営業店コード
            this.pageData.営業店名称 = 依頼情報.営業店
            this.pageData.見積No = 依頼情報.見積No
            this.pageData.見積依頼日 = 依頼情報.見積依頼日
            this.pageData.工事予定日 = 依頼情報.工事予定日_開始日 ? 依頼情報.工事予定日_開始日 + '～' + 依頼情報.工事予定日_終了日 : ''
            this.pageData.物件コード = 依頼情報.物件コード
            this.pageData.物件名 = 依頼情報.物件
            this.pageData.物件住所郵便番号 = 依頼情報.物件住所_郵便番号
            this.pageData.物件住所 = 依頼情報.物件住所
            this.pageData.工事件名 = 依頼情報.工事件名
          }
          let 割当項目データ = response.data.割当項目データ
          if (割当項目データ && 割当項目データ.length > 0) {
            割当項目データ = 割当項目データ[0]
            this.tableDataWariateKomoku[0].項目名 = 割当項目データ.分類
            this.tableDataWariateKomoku[1].項目名 = 割当項目データ.作業カテゴリ大
            this.tableDataWariateKomoku[2].項目名 = 割当項目データ.作業カテゴリ中
            this.tableDataWariateKomoku[3].項目名 = 割当項目データ.作業カテゴリ小
            this.tableDataWariateKomoku[4].項目名 = 割当項目データ.作業内容
            this.tableDataWariateKomoku[5].項目名 = 割当項目データ.数量
            this.tableDataWariateKomoku[6].項目名 = 割当項目データ.単位
            this.tableDataWariateKomoku[7].項目名 = 割当項目データ.金額_税抜
            this.tableDataWariateKomoku[8].項目名 = 割当項目データ.課税区分
            this.tableDataWariateKomoku[9].項目名 = 割当項目データ.税率
            this.tableDataWariateKomoku[10].項目名 = 割当項目データ.金額_税込
          }

          this.pageData.tempFilePath = response.data.tempFilePath
        })
    },
    async onBack () {
      window.parent.postMessage({
        'call': 'closePopup'
      }, '*')
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
document.getElementsByName('大区分')
</script>
<style>
.MitsumoriDetaTorikomi .marginLeft {
  margin-left: 15px;
}

.MitsumoriDetaTorikomi .item_label {
  padding-left: 15px;
  max-width: 407px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.MitsumoriDetaTorikomi .right {
  text-align: right;
  margin-bottom: 20px;
}

.el-form .el-form-item__content {
  line-height: 27px;
}

.el-form .el-form-item__label {
  margin-top: 0px !important;
}

.el-textarea.is-disabled .el-textarea__inner {
  color: black !important;
}

.MitsumoriDetaTorikomi .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr,
.MitsumoriDetaTorikomi .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-expand-tr {
  height: 31px;
}

.MitsumoriDetaTorikomi .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr td.ve-table-body-td,
.MitsumoriDetaTorikomi .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-expand-tr td.ve-table-body-td,
.MitsumoriDetaTorikomi .ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.ve-table-header-th {
  padding: 0px 5px !important;
}

.text-left {
  text-align: left !important;
}

.text-right {
  text-align: right !important;
}

.MitsumoriDetaTorikomi .hightlight,
.MitsumoriDetaTorikomi .hightlight input {
  background-color: #EFCFD6 !important;
}

.MitsumoriDetaTorikomi .tableWariateKomoku input {
  border: none !important;
  padding: 0 4px !important;
  font-family: Meiryo UI !important;
  text-align: left !important;
}

.MitsumoriDetaTorikomi .ve-table-container {
  height: 383px !important;
}

.wrap-class .el-scrollbar__view {
  width: 100% !important;
}
.MitsumoriDetaTorikomi .is-gray {
  background-color: #d3d3d3 !important;
}

.ve-loading-parent-relative {
  position: static !important;
}
</style>
