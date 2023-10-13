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
                    <reafsinputtext v-model="pageData.EigyotenCode" class="is-readonly" maxlength="6" disabled="disabled"
                      :width="'95px'">
                    </reafsinputtext>
                  </div>
                  <div class="marginLeft">
                    <reafsinputtext class="is-readonly" :title="pageData.EigyotenName" v-model="pageData.EigyotenName" maxlength="20"
                      disabled="disabled" :width="'100%'">
                    </reafsinputtext>
                  </div>
                </div>
              </el-form-item>
              <el-form-item label="物件">
                <div style="display: flex">
                  <div>
                    <reafsinputtext class="is-readonly" v-model="pageData.BukkenCode" maxlength="8" disabled="disabled"
                      :width="'95px'">
                    </reafsinputtext>
                  </div>
                  <div class="marginLeft">
                    <reafsinputtext :title="pageData.BukkenName" class="is-readonly" v-model="pageData.BukkenName" maxlength="50"
                      disabled="disabled" :width="'100%'">
                    </reafsinputtext>
                  </div>
                </div>
              </el-form-item>
              <el-form-item label="契約名称">
                <reafsinputtext v-model="pageData.KeiyakuName" :title="pageData.KeiyakuName" class="is-readonly" maxlength="60"
                  disabled="disabled" :width="'276'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="15" class="marginLeft">
              <div style="display: flex">
                <el-form-item label="見積№">
                  <reafsinputtext v-model="pageData.MitsumoriNo" class="is-readonly" maxlength="11" disabled="disabled"
                    :width="'120px'">
                  </reafsinputtext>
                </el-form-item>
                <div style="width: 212px; margin-left: 15px; display: flex; align-items: center;">
                  <span>見積依頼日: {{ pageData.Iraibi }}</span>
                </div>
                <div style="margin-left: 15px; display: flex; align-items: center;">
                  <span>工事予定日: {{ pageData.KeiyakuYMS }}</span>
                </div>
              </div>
              <el-form-item label="物件住所">
                <div style="display: flex">
                  <div>
                    <reafsinputtext v-model="pageData.PostCD" class="is-readonly" maxlength="8" disabled="disabled"
                      :width="'120px'">
                    </reafsinputtext>
                  </div>
                  <div class="marginLeft">
                    <reafsinputtext v-model="pageData.Juusyo" class="is-readonly" maxlength="30" disabled="disabled"
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
                    <reafsinputtext v-model="pageData.ReadFile" :width="'344px'" @click="openFileDialog">
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
                :disableSelectedRowKeys="['作業カテゴリ大','作業カテゴリ中','作業カテゴリ小','金額（税抜）','金額（税込）']" />
              <!-- <div>※金額は（税抜）・（税込）のいずれか指定してください</div> -->
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
                :maxHeight="383" rowKeyFieldName="項目" :disableSelectedRowKeys="['作業カテゴリ大','作業カテゴリ中','作業カテゴリ小']"/>
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
        keiyakuNo: '',
        hansu: '',
        rirekiNO: ''
      },
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      pageData: {
        KeiyakuNo: '',
        KeiyakuNoEdaban: '',
        rirekiNo: '',
        EigyotenCode: '',
        EigyotenName: '',
        MitsumoriNo: '',
        BukkenCode: '',
        BukkenName: '',
        PostCD: '',
        Juusyo: '',
        KeiyakuName: '',
        Iraibi: '',
        KeiyakuYMS: '',
        KeiyakuYME: '',
        ReadFile: '',
        FileDataBase64: '',
        TempFilePath: ''
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
          renderBodyCell: ({ row, column, rowIndex }) => {
            let input
            input = <reafsinputtext
                maxlength={Number(row.桁数)}
                value={row.項目名}
                width="100%"
                on-input={(val) => {
                  row['項目名'] = val
                }}
              ></reafsinputtext>
            return (
              <div class="cell-body-custome">
                {input}
              </div>
            )
          }
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
      torikomiDataList: [],
      returnDataList: []
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

          if (column.key == '4') {
            const rowAdd = this.tableDataTorikomiDetarisuto && this.tableDataTorikomiDetarisuto.find(x => row.項目.includes(x.項目))
            if (rowAdd && rowAdd.項目名.replaceAll(',', '') == row.項目名.replaceAll(',', '')) {
              css += ' hightlight'
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

    console.log("定期見積データ取込処理開始");

    this.queryParams = {
      keiyakuNo: this.$route.query.keiyakuNo,
      hansu: this.$route.query.hansu,
      rirekiNo: this.$route.query.rirekiNo
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
        this.MsgInfo({
          title: "見積取込（定期委託）",
          message: "割り当て項目または取込データが未選択です。"
        });
        return
      }
      const existRow = this.tableDataWariateKomoku.find(x => x.項目名.includes(lstSelectTorikomiDetarisuto[0]))
      if (existRow){
        this.MsgInfo({
          title: "見積取込（定期委託）",
          message: "既に割り当て済みの項目です。"
        });
        return
      }

      const value = this.tableDataTorikomiDetarisuto[lstSelectTorikomiDetarisuto]
      const row = this.tableDataWariateKomoku.find(x => x.項目.includes(lstSelectWariateKomoku[0]))
      row.項目名 = lstSelectTorikomiDetarisuto[0]
      console.log(this.tableDataWariateKomoku)
      // row.isHightLight = true
    },
    onKaijo () {
      const lstSelectWariateKomoku = this.$refs.refWariateKomoku.selectedRowKeys
      if (!lstSelectWariateKomoku || lstSelectWariateKomoku.length <= 0) {
        return
      }

      const index = this.tableDataWariateKomoku.findIndex(x => x.項目.includes(lstSelectWariateKomoku[0]))
      this.tableDataWariateKomoku[index].項目名 = ''
    },
    async onDownloadTemp () {
      if (!this.pageData.TempFilePath) {
        return
      }
      const FileDownloadInfo = {
        FilePath: this.pageData.TempFilePath,
        FileName: 'fileName'
      }

      await this.http
        .post('/api/Reafs_W/Common/DownloadFile', FileDownloadInfo, '', {
          responseType: 'blob'
        })
        .then(async blob => {
          if (blob.size === 0) {
            this.getMsg('E040464').then(res => {
              this.showError(res.data)
            })
            return
          }
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: this.pageData.TempFilePath.split('\\')[this.pageData.TempFilePath.split('\\').length - 1]
          // })
          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()

          await this.base.saveFileDownload(blob, this.pageData.TempFilePath.split('\\')[this.pageData.TempFilePath.split('\\').length - 1])
        })
        .catch(ex => console.log(ex))
        .finally(() => {
        })
        .catch(ex => console.log(ex))
    },
    async onSansho () {
      console.log("CSVファイル読込！")
      if (!this.pageData.ReadFile || !this.pageData.FileDataBase64) {
        this.pageData.ReadFile = ''
        return
      }
      const data = {
        base64Data: this.pageData.FileDataBase64
      }
      let res
      await this.http
        .get('/api/Reafs_T/TeikiMitsumoriNyuryoku/ReadFileCSV', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
          console.log(ex)
        }).finally(() => {
        })
      if (res.status) {
        if (res.data) {
          this.torikomiDataList = res.data
          this.tableDataTorikomiDetarisuto = []
          for (let propertyName in Object.keys(res.data[0])) {
            this.tableDataTorikomiDetarisuto.push({項目: Object.keys(res.data[0])[propertyName]})
          }
          console.log(this.torikomiDataList);
        }
      } else {
        this.showError(res.message)
      }
    },
    validateSave () {
      const lstValidate = this.tableDataWariateKomoku.filter(x => x.項目.includes('★'))
      for (let row of lstValidate) {
        const rowAdd = this.tableDataWariateKomoku.find(x => row.項目.includes(x.項目))
        if (!rowAdd || rowAdd.項目名.replaceAll(',', '') != row.項目名.replaceAll(',', '')) {
          this.getMsg('E040737').then((response) => {
            this.showError(response.data)
          })
          return false
        }
      }
      return true
    },
    async onToroku () {
      // 登録前チェック
      if (!this.validateSave()) {
        return false
      }

      // 確認MSG
      // let msg = (await this.getMsg('K000072')).data
      // if (!await this.confirm(msg)) {
      //   return false
      // }

      let bunruiKey = this.tableDataWariateKomoku[0].項目名
      let sagyonaiyoKey = this.tableDataWariateKomoku[4].項目名
      let suryoKey = this.tableDataWariateKomoku[5].項目名
      let tanniKey = this.tableDataWariateKomoku[6].項目名
      let tankaKey = this.tableDataWariateKomoku[7].項目名
      let kazeiKBNKey = this.tableDataWariateKomoku[9].項目名
      let zeiritsuKey = this.tableDataWariateKomoku[10].項目名

      for (var torikomiRow in this.torikomiDataList)
      {
        let bunrui = ''
        if (bunruiKey !== '')
        {
          bunrui = this.torikomiDataList[torikomiRow][sagyonaiyoKey]
        }

        let sagyounaiyou = this.torikomiDataList[torikomiRow][sagyonaiyoKey]
        let suryo = this.torikomiDataList[torikomiRow][sagyonaiyoKey]
        let tanni = this.torikomiDataList[torikomiRow][sagyonaiyoKey]
        let tanka = this.torikomiDataList[torikomiRow][sagyonaiyoKey]
        let kazeiKBN = this.torikomiDataList[torikomiRow][sagyonaiyoKey]
        let zeiritsu = this.torikomiDataList[torikomiRow][sagyonaiyoKey]

        this.returnDataList.push
        ({
            bunruiCode:bunrui
          , sagyomeisyo:sagyounaiyou
          , suryo:suryo
          , tani:tanni
          , tanka:tanka
          , genkazeiKBN:kazeiKBN
          , genkazeiritsu:zeiritsu
        })
      }

      window.parent.postMessage({
          'call': 'closePopup',
          backData: {
            取込データ: this.returnDataList
          }
        }, '*')
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
          me.pageData.ReadFile = file.name
          var fr = new FileReader()
          fr.onload = function () {
            me.pageData.FileDataBase64 = fr.result.split(',')[1]
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
          桁数: 20,
          項目名: ''
        },
        {
          項目: '数量★',
          桁数: 4,
          項目名: ''
        },
        {
          項目: '単位★',
          桁数: 8,
          項目名: ''
        },
        {
          項目: '単価★',
          桁数: 8,
          項目名: ''
        },
        {
          項目: '金額（税抜）',
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
          項目: '金額（税込）',
          桁数: 10,
          項目名: ''
        }
      ]
    },
    async getPageData () {
      console.log(this.queryParams)

      const data = {
        keiyakuNo: this.queryParams.keiyakuNo,
        rirekiNO: this.queryParams.rirekiNo,
        hansu: this.queryParams.hansu,
        kaisha_code: ""
      }

      await this.http
        .post('/api/Reafs_T/TeikiMitsumoriNyuryoku/SearchCSV', data, "検索しています....")
        .then((response) => {
          let 依頼情報 = response.data.依頼情報
          if (依頼情報 && 依頼情報.length > 0) {
            console.log("取得成功");
            依頼情報 = 依頼情報[0]
            this.pageData.EigyotenCode = 依頼情報.EigyotenCode
            this.pageData.EigyotenName = 依頼情報.EigyotenName
            this.pageData.MitsumoriNo = 依頼情報.MitsumoriNo
            this.pageData.Iraibi = 依頼情報.Iraibi
            this.pageData.KeiyakuYMS = 依頼情報.KeiyakuYMS ? 依頼情報.KeiyakuYMS + '～' + 依頼情報.KeiyakuYME : ''
            this.pageData.BukkenCode = 依頼情報.BukkenCode
            this.pageData.BukkenName = 依頼情報.BukkenName
            this.pageData.PostCD = 依頼情報.PostCD
            this.pageData.Juusyo = 依頼情報.Juusyo
            this.pageData.KeiyakuName = 依頼情報.KeiyakuName
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

          this.pageData.TempFilePath = response.data.TempFilePath
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
</style>
