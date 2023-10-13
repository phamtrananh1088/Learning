<template>
  <div class="home-container ZumenShiryoTenpuSabu">
    <el-form
      ref="refZumenShiryoTenpuSabuForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk = "true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row class="border-bot" style="margin-top: 20px">
          </el-row>
        </el-header>
        <el-main class="home-el-main">
          <reafstable
            ref="table"
            :tableData="dataTable1"
            :maxHeight="437"
            :columns="columns"
            :cellStyleOption="cellStyleOption"
            :rowKeyFieldName="rowKeyFieldName"
            :virtualScrollOption="virtualScrollOption"
          ></reafstable>
          <el-col >
          </el-col>
          <reafstable
            ref="table"
            :tableData="dataTable2"
            :maxHeight="437"
            :columns="columns1"
            :cellStyleOption="cellStyleOption"
            :rowKeyFieldName="rowKeyFieldName"
            :virtualScrollOption="virtualScrollOption"
          ></reafstable>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row class="border-top">
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="帳票種類" prop="帳票種類" class="is-required">
                <el-select
                  required
                  tabindex="11"
                  v-model="zumenForm.帳票種類"
                  @change="selectChohyoShurui"
                  ref="ref帳票種類"
                  style="width: 250px;"
                >
                  <el-option
                    v-for="item in 帳票種類value"
                    :key="item.名称コード"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <div style="display: flex">
                <el-form-item
                  label="添付ファイル"
                  prop="添付ファイル"
                  class="is-required"
                  style="width: 88%;"
                >
                  <reafsinputtext
                    required
                    v-model="zumenForm.添付ファイル_パス"
                    maxlenth="100"
                    disabled
                    :width="'100%'"
                    ref="ref添付ファイル_パス"
                  >
                  </reafsinputtext>
                </el-form-item>
                <el-button
                  class="btnSansho"
                  @click="openFileDialog()"
                  style="margin-left: 2px;"
                  ref="ref参照"
                  >参照
                </el-button>
              </div>
            </el-col>
            <el-col :span="10">
              <el-form-item
                label="その他帳票名"
                prop="その他帳票名"
                class="is-required"
                style="width: 88%;"
              >
                <reafsinputtext
                  required
                  tabindex="18"
                  maxlength="20"
                  v-model="zumenForm.その他帳票名"
                  :disabled="画面入力制御disabled.その他帳票名"
                  :width="'100%'"
                  ref="refその他帳票名"
                ></reafsinputtext>
              </el-form-item>
              <div style="display: flex">
                <el-form-item
                  label="ファイル名"
                  prop="ファイル名"
                  class="is-readonly"
                  style="width: 88%;"
                >
                  <reafsinputtext
                    v-model="zumenForm.ファイル名"
                    maxlenth="100"
                    disabled
                    :width="'100%'"
                  >
                  </reafsinputtext>
                </el-form-item>
                <el-button
                  class="btnTorikeshi"
                  @click="onDeleteFile()"
                  style="margin-left: 2px;"
                  >取消
                </el-button>
              </div>
            </el-col>
            <el-col :span="3" style="padding-left: 20px; padding-top: 3px;">
              </br></br>
              <el-button @click="onRegister()"
                >添付登録</el-button
              >
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafstable from '@/components/table/Grid.vue'
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'

var $select帳票種類value

export default {
  components: {
    reafstable,
    reafsinputtext
  },
  data () {
    return {
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      MSC_5MB_LengthByte: 5245329,
      MSC_300MB_LengthByte: 314572800, // 300MBの判定を追加
      zumenForm: {
        帳票種類: '',
        その他帳票名: '',
        ファイル名: '',
        添付ファイル_パス: '',
        ファイルNo: '',
        ファイルパス: '',
        ファイルパス2: '',
        ルートパス: '',
        添付元ファイル名: '',
        物理ファイル名: '',
        物理ファイル名2: '',
        状況コメント: '',
        部位コメント: '',
        linkImage: '',
        linkDownload: '',
        添付NO: '',
        imgUploadName: ''
      },
      queryParams: {
        業者コード: '',
        業者コード枝番: '',
        工事依頼No: '',
        枝番: '',
        契約No: '',
        契約履歴No: '',
        契約年月: '',
        見積明細No: '',
        予定年月: '',
        契約年月明細No: '',
        帳票区分: '',
        参照パターン: ''
      },
      帳票種類value: [],
      画面入力制御disabled: {
        その他帳票名: false
      },
      delete制御disabled: false,
      rowKeyFieldName: 'rowKey',
      columns: [
        { field: '帳票名',
          key: '1',
          title: '帳票名',
          align: 'center',
          width: 120,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['帳票名']
            return (
              <div title={text} style="display: block; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                <a class="ellipsis-text" href="#" on-click={() => this.onDownload(row)} >
                  {text}
                </a></div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: 'ファイル名',
          key: '2',
          title: 'ファイル名',
          align: 'center',
          width: 250,
          renderBodyCell: ({ row }) => {
            const text = row['物理ファイル名']
            return (
              <div title={text} style="display: block; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                {text}
              </div>
            )
          }
        },
        { field: '',
          key: '3',
          title: '',
          align: 'center',
          width: 35,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
                <el-button
                  class={row['削除不可'] === true ? 'btnDelete is-disabled' : 'btnDelete'}
                  on-click={() => this.onDeleteRow(row)}
                >
                  削除
                </el-button>
            )
          }
        }
      ],
      columns1: [
        { field: '帳票名',
          key: '1',
          title: '帳票名',
          align: 'center',
          width: 120,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['帳票名']
            return (
              <div title={text} style="display: block; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                <a class="ellipsis-text" href="#" on-click={() => this.onDownload(row)} >
                  {text}
                </a></div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: 'ファイル名',
          key: '2',
          title: 'ファイル名',
          align: 'center',
          width: 250,
          renderBodyCell: ({ row }) => {
            const text = row['添付元ファイル名']
            return (
              <div title={text} style="display: block; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                {text}
              </div>
            )
          }
        },
        { field: '',
          key: '3',
          title: '',
          align: 'center',
          width: 35,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
                <el-button
                  class={row['削除不可'] === true ? 'btnDelete is-disabled' : 'btnDelete'}
                  on-click={() => this.onDeleteRow(row)}
                >
                  削除
                </el-button>
            )
          }
        }
      ],
      dataTable1: [],
      dataTable2: [],
      cellStyleOption: {
        headerCellClass: ({ column, rowIndex }) => {
          if (column.key === '2') {
            return 'table-header-cell-custom-2'
          }
        }
      },
      virtualScrollOption: {
        enable: true,
        minRowHeight: 30
      }
    }
  },
  watch: {
  },
  computed: {
  },
  inject: ['hideLeftMenu'],
  created () {
    $select帳票種類value = this
  },
  mounted () {
    this.queryParams = {
      業者コード: this.$route.query.業者コード,
      業者コード枝番: this.$route.query.業者コード枝番,
      工事依頼No: this.$route.query.工事依頼No,
      枝番: this.$route.query.枝番,
      契約No: this.$route.query.契約No,
      契約履歴No: this.$route.query.契約履歴No,
      契約年月: this.$route.query.契約年月,
      見積明細No: this.$route.query.見積明細No,
      予定年月: this.$route.query.予定年月,
      契約年月明細No: this.$route.query.契約年月明細No
    }

    if (this.queryParams.工事依頼No != undefined && this.queryParams.工事依頼No != '') {
      this.queryParams.帳票区分 = '1'
      this.queryParams.参照パターン = '1'
    } else {
      if (this.queryParams.見積明細No !== undefined && this.queryParams.見積明細No != '') {
        this.queryParams.帳票区分 = '2'
        this.queryParams.参照パターン = '3'
      } else {
        this.queryParams.帳票区分 = '2'
        this.queryParams.参照パターン = '2'
      }
    }

    this.hideLeftMenu(false)
    this.init()
  },
  methods: {
    init () {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.会社コード = userLogIn.会社コード

      this.http
        .get('/api/Reafs_T/Mitsumori/TS00302/GetStartInfo', { 帳票区分: this.queryParams.帳票区分, 参照パターン: this.queryParams.参照パターン }
        , 'アクセスしています....')
        .then((response) => {
          // Get Dropdown帳票種類
          $select帳票種類value.帳票種類value = response.data
          this.zumenForm.帳票種類 = ''
        })

      this.getDataTable()
      this.画面入力制御disabled.その他帳票名 = true
    },
    async getDataTable () {
      this.http
        .get('/api/Reafs_T/Mitsumori/TS00302/fnc_getDataTable', { 参照パターン: this.queryParams.参照パターン,
        業者コード: this.queryParams.業者コード, 業者コード枝番: this.queryParams.業者コード枝番, 工事依頼No: this.queryParams.工事依頼No,
        契約No: this.queryParams.契約No, 契約履歴No: this.queryParams.契約履歴No || 0,
        契約年月: this.queryParams.契約年月, 見積明細No: this.queryParams.見積明細No || 0 }
        , 'アクセスしています....')
        .then((response) => {
          var data1 = []
          var data2 = []
          if (response.data.dataTable.length > 0) {
            for (let i = 0; i < response.data.dataTable.length; i++) {
              let 添付種類 = response.data.dataTable[i]['添付種類'];
              if (添付種類 === '199' || 添付種類 === '299') {
                data2.push(response.data.dataTable[i])
              } else {
                data1.push(response.data.dataTable[i])
              }
            }
          }
          this.dataTable1 = data1
          this.dataTable2 = data2
        })
    },
    async onDownload (row) {
      const fileName = row.物理ファイル名

      const FileDownloadInfo = {
        FilePath: row.ダウンロードパス,
        FileName: fileName
      }

      await this.http
        .post('/api/Reafs_T/Common/DownloadFile', FileDownloadInfo, '', {
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
          //   suggestedName: fileName
          // })
          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()

          await this.base.saveFileDownload(blob, fileName)

          this.getMsg('M040020').then(response => {
            this.showSuccess(response.data)
          })

          // this.$nextTick(() => {
          //   window.open(URL.createObjectURL(blob), '_blank');
          // })
        })
        .catch(ex => console.log(ex))
        .finally(() => {
        })
        .catch(ex => console.log(ex))
    },
    async onDeleteRow (row) {
      let result = ''
      
      let msg = (await this.getMsg('K000002')).data
      if (!(await this.confirm(msg))) {
        return false
      }

      const data = {
        ドキュメントNO: row.ドキュメントNO
      }
      await this.http
        .post('/api/Reafs_T/Mitsumori/TS00302/fnc_delF090_ドキュメント管理ファイル', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
        })
      if (result) {
        let msgM000013 = (await this.getMsg('M000013')).data
        this.showSuccess(msgM000013)
        this.getDataTable()
      } else {
        return false
      }
    },
    selectChohyoShurui (val) {
      this.zumenForm.その他帳票名 = ''
      this.zumenForm.添付ファイル_パス = ''
      this.zumenForm.ファイル名 = ''
      let 名称 = $select帳票種類value.帳票種類value.find(x => x.名称コード == val).名称

      if (名称 === 'その他') {
        // その他
        this.画面入力制御disabled.その他帳票名 = false
      } else {
        this.画面入力制御disabled.その他帳票名 = true
      }

      if (!this.画面入力制御disabled.その他帳票名) {
        this.$nextTick(() => {
          this.$refs.refその他帳票名.focus()
        })
      } else {
        this.$nextTick(() => {
          this.$refs.ref参照.$el.focus()
        })
      }
    },
    openFileDialog () {
      const me = this
      var input = document.createElement('input')
      input.type = 'file'
      const zumenForm = this.zumenForm

      input.onchange = e => {
        var file = e.target.files[0]
        if (!file) {
          return
        }

        // ファイルアップロードサイズの制限を300MBに変更
        // if (file.size > this.MSC_5MB_LengthByte) {
        if (file.size > this.MSC_300MB_LengthByte) {
          e.preventDefault()
          this.getMsg('E040470').then(response => {
            this.showError(response.data)
          })
          return
        }
        // FileReader support
        if (FileReader && file) {
          var fr = new FileReader()
          fr.onload = function () {
            zumenForm.添付ファイル_パス = file.name // fr.result
            zumenForm.ファイル名 = file.name
            me.upLoadImage(fr.result.split(',')[1])
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
    async onDeleteFile () {
      this.zumenForm.添付ファイル_パス = ''
      this.zumenForm.ファイル名 = ''
    },
    async upLoadImage (imgBase64) {
      const data = this.getDataF093(imgBase64)
      let res = {}
      await this.http
        .post(
          '/api/Reafs_T/Mitsumori/TS00302/fnc_InsertF093',
          data,
          'アクセスしています....'
        )
        .then(response => {
          res = response
        })
      if (res.status) {
        this.zumenForm.添付NO = res.data
      } else {
      }
    },
    getDataF093 (imgByte) {
      const zumenForm = this.zumenForm
      const F093 = {
        添付NO: zumenForm.添付NO || 0,
        枝番: 1,
        削除区分: 0,
        添付元ファイル名: zumenForm.ファイル名,
        ファイルデータ: imgByte
      }
      return {
        F093_一時添付ファイル: F093,
        FLAG添付NO: zumenForm.添付NO
      }
    },
    getDataF090 () {
      const data = []
      const f090 = {
        会社コード: this.userInfo.会社コード,
        工事依頼No: '',
        工事依頼No枝番: '',
        添付元: '',
        添付種類: this.zumenForm.帳票種類,
        その他帳票名: this.zumenForm.その他帳票名,
        ファイルパス: '',
        物理ファイル名: '',
        添付元ファイル名: this.zumenForm.ファイル名,
        契約NO: '',
        履歴NO: 0,
        明細NO: 0,
        契約年月: '',
        契約年月明細NO: 0,
        予定年月: '',
        契約書管理NO: '',
        請求書管理NO: '',
        枝番: '',
        取引先コード: '',
        取引先コード枝番: '',
        検収回数: 0,
        請求書NO: '',
        日付: '',
        備考1: '',
        備考2: '',
        Ｗ参照区分: 0,
        Ｔ参照区分: 0,
        発注稟議添付: 0,
        稟議申請ファイル名: '',
        削除区分: 0,
        INSERT_PG: 'TS00302',
        INSERT_HOST: '',
        INSERT_ID: '',
        物件コード: ''
      }
      data.push({
        F090_ドキュメント管理ファイル: f090,
        添付NO: this.zumenForm.添付NO,
        帳票種類: this.zumenForm.帳票種類,
        業者コード: this.queryParams.業者コード,
        業者コード枝番: this.queryParams.業者コード枝番,
        工事依頼No: this.queryParams.工事依頼No,
        枝番: this.queryParams.枝番,
        契約No: this.queryParams.契約No,
        契約履歴No: this.queryParams.契約履歴No,
        契約年月: this.queryParams.契約年月,
        見積明細No: this.queryParams.見積明細No,
        予定年月: this.queryParams.予定年月,
        契約年月明細No: this.queryParams.契約年月明細No
      })
      return data
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
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
      var self = this
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
    regisValidate () {
      if (this.zumenForm.帳票種類 === '') {
        this.getMsg('E040320').then(response => {
          this.showError(response.data)
        })
        this.$refs.ref帳票種類.focus()
        return false
      }

      if (!this.画面入力制御disabled.その他帳票名) {
        if (this.zumenForm.その他帳票名 === '') {
          this.getMsg('E040475').then(response => {
            this.showError(response.data)
          })
          this.$refs.refその他帳票名.focus()
          return false
        }
      }

      if (this.zumenForm.添付ファイル_パス === '') {
        this.getMsg('E040469').then(response => {
          this.showError(response.data)
        })
        this.$refs.ref参照.$el.focus()
        return false
      }

      return true
    },
    async onRegister () {
      if (!this.regisValidate()) {
        return false
      }

      let msg = (await this.getMsg('K000004')).data
      if (!(await this.confirm(msg))) {
        return false
      }
      const data = this.getDataF090()
      let res = {}
      await this.http
        .post(
          '/api/Reafs_T/Common/fnc_InsertF090',
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
        })
      if (res.status) {
        let msgM000013 = (await this.getMsg('M000008')).data
        this.showSuccess(msgM000013)
        this.getDataTable()
        this.zumenForm.添付NO = ''
        this.zumenForm.帳票種類 = ''
        this.zumenForm.その他帳票名 = ''
        this.画面入力制御disabled.その他帳票名 = true
        this.zumenForm.添付ファイル_パス = ''
        this.zumenForm.ファイル名 = ''
        return true
      } else {
        this.showError(res.message)
        return false
      }
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  }
}
</script>
<style lang="less" scoped>
main {
  display: grid;
  grid-template-columns: 49.5% 1% 49.5%;
}
.main-container {
  left: 0 !important;
  background: white !important;
}
.el-dialog__body {
  padding: 0 10px !important;
}
.border-top {
  border-top: 1px dotted black;
  padding-bottom: 20px;
}
.border-bot {
  border-bottom: 1px dotted black;
}
.home-container {
  width: 100%;
  height: 100%;
}
.home-container form {
  height: 100%;
}
.home-el-container {
  height: 100%;
}
.home-el-header {
  padding: 0 0 20px 0;
  background: white;
}
.home-el-main,
.home-container {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
  background: white;
}
.home-el-footer {
  padding: 10px 0 0 0;
}
footer {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: auto;
  background: white;
}
.btnTorikeshi, .btnSansho {
  max-height: 32px;
  padding: 5px 10px;
}
.btnDelete {
  padding: 5px 7px;
}
.is-disabled {
  pointer-events: none;
  opacity: 0.6;
}
</style>
<style>
.ZumenShiryoTenpuSabu .ve-table-body-tr, .ve-table-header-tr{
  height: 30px !important;
}
.ZumenShiryoTenpuSabu .ve-table-header-th{
  height: 37px;
}
.ZumenShiryoTenpuSabu .ve-table .ve-table-container .ve-table-border-y th.table-header-cell-custom-2 {
  border-right: none !important;
}
.ZumenShiryoTenpuSabu .wrap-class .el-scrollbar__view {
  width: 100% !important;
}
</style>
