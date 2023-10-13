<template>
  <div class="TyohyoIchiran home-container">
    <el-form :model="TyohyoIchiranForm" :rules="rules" ref="refTyohyoIchiran" label-width="130px"
      class="sample-form-class" :hide-required-asterisk="true">
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
              <H3 style="margin-top: 0px;" class="customTitle">帳票一覧</H3>
        </el-header>
        <el-main class="home-el-main">
          <el-row class="rTbl" style="height: 440px; width: 630px;">
              <reafstable
                ref="refDtTyohyoIchiran"
                tabindex="1"
                :tableData="dtTyohyoIchiran"
                :columns="columnsTyohyoIchiran"
                :maxHeight="418"
                :cellStyleOption="cellStyleOptionTyohyoIchiran"
                :rowStyleOption="rowStyleOption"
                :rowKeyFieldName="rowKeyFieldNameTyohyoIchiran"
                :editOption="editOption"
                :hideSelect=true
                :HiddenColumnKeys="hiddenColumnsTyohyoIchiran">
              </reafstable>
          </el-row>
          <el-row class="rForm">
            <el-col :span="24">
              <el-form-item label="帳票種類" prop="帳票種類" class="is-required" style="margin-bottom: 0px !important;">
                <el-select
                  required
                  tabindex="2"
                  v-model="TyohyoIchiranForm.帳票種類"
                  ref="ref帳票種類"
                  style="width: 300px;"
                  placeholder=""
                  @change="selectChohyoShurui">
                  <el-option v-for="item in 帳票種類value" :key="item.帳票種類" :label="item.帳票名称" :value="item.帳票種類">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="その他帳票名" prop="その他帳票名" class="is-required" style="margin-bottom: 0px !important;">
                <reafsinputtext required tabindex="3" maxlength="20" v-model="TyohyoIchiranForm.その他帳票名" :width="'300px'"
                  ref="refその他帳票名" :disabled="画面入力制御disabled.その他帳票名"></reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="添付ファイル" prop="添付ファイル" class="is-required" style="margin-bottom: 0px !important;">
                <reafsinputtext required tabindex="4" maxlength="1000" v-model="TyohyoIchiranForm.添付ファイル" :width="'400px'"
                  :disabled="true" ref="ref添付ファイル">
                  <el-button
                    style="margin-left: -5px; padding-top: 6px; padding-bottom: 8px"
                    slot="append"
                    @click="openFileDialog()"
                    tabindex = "5"
                    :disabled="buttons.btn参照.disabled"
                    ref="ref参照"
                  >参照
                  </el-button>
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="ファイル名" prop="ファイル名" class="is-readonly" style="margin-bottom: 0px !important;">
                <reafsinputtext tabindex="6" maxlength="100" v-model="TyohyoIchiranForm.ファイル名" :width="'400px'"
                  :disabled="true" ref="refファイル名">
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-top: 25px;">
            <el-col :span="9">
              <el-button
                tabindex="7"
                @click="onTenpuToroku"
                style="float:right; margin-right: 15px;"
                :disabled="buttons.btn添付登録.disabled"
              >添付登録
              </el-button>
            </el-col>
            <el-col :span="11"><div class="grid-content">&nbsp;</div></el-col>
            <el-col :span="1" style="margin-left: 10px;">
              <el-button
                tabindex="8"
                @click="onBack">閉じる </el-button>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer border-top" height="auto">   
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafstable from '@/components/table/Grid.vue'

var $select帳票種類value

export default {
  components: {
    reafsinputtext,
    reafstable
  },
  data () {
    return {
      loading: false,
      buttons: {
        'btn添付登録': {disabledOrg: false, disabled: false},
        'btn閉じる': {disabledOrg: false, disabled: false},
        'btn参照': {disabledOrg: false, disabled: false}
      },
      bCheckBorderRight: false,
      MSC_5MB_LengthByte: 5245329,
      MSC_300MB_LengthByte: 314572800, // 300MBの判定を追加
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: '',
        業者コード: '',
        ユーザー種別: '',
        親ＩＤ: ''
      },
      queryParams: {
        帳票区分: '',
        参照パターン: '',
        工事依頼No: '',
        契約No: '',
        契約履歴No: '',
        契約年月: '',
        見積明細No: '',
        予定年月: ''
      },
      画面入力制御disabled: {
        その他帳票名: false
      },
      TyohyoIchiranForm: {
        帳票種類: '',
        その他帳票名: '',
        添付ファイル: '',
        ファイル名: '',
        添付NO: '',
        ドキュメントNO: ''
      },
      帳票種類value: [
        { 帳票種類: '－', 帳票名称: '－' }
      ],
      rowKeyFieldNameTyohyoIchiran: 'rowKeyTyohyoIchiran',
      dtTyohyoIchiran: [],
      columnsTyohyoIchiran: [
        {
          field: '帳票名',
          key: '帳票名',
          title: '帳票名',
          align: 'center',
          vAlign: 'center',
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['帳票名']
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
          field: '発生',
          key: '発生',
          title: '発生',
          align: 'center',
          vAlign: 'center',
          width: 60,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['発生']
            return (
              <div title={text} style="display: block">
                <span>
                  {text}
                </span>
              </div>
            )
          }
        },
        { field: '',
          key: '参照',
          title: '',
          align: 'center',
          vAlign: 'center',
          width: 60,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <span>
                <el-button
                  class='customBtn'
                  on-click={() => this.onDownload(row)}
                >参照</el-button>
              </span>
            )
          }
        },
        { field: '',
          key: '削除',
          title: '',
          align: 'center',
          vAlign: 'center',
          width: 60,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const 削除不可 = row['削除不可']
            if (削除不可 === '1') {
              return (
                <span>
                  <el-button class='customBtn' disabled="disabled">削除</el-button>
                </span>
              )
            } else {
              return (
                <span>
                  <el-button
                    class='customBtn'
                    on-click={() => this.onSakujo(row)}>削除</el-button>
                </span>
              )
            }
          }
        },
        { field: 'ドキュメントNO',
          key: 'ドキュメントNO'
        },
        { field: 'ダウンロードパス',
          key: 'ダウンロードパス'
        },
        { field: '削除不可',
          key: '削除不可'
        },
        { field: '物理ファイル名',
          key: '物理ファイル名'
        },
        { field: '稟議添付',
          key: '稟議添付'
        },
        { field: '発注稟議添付',
          key: '発注稟議添付'
        },
        { field: 'ファイル名',
          key: 'ファイル名'
        },
        { field: '枝番',
          key: '枝番'
        },
        { field: '年月',
          key: '年月'
        }

      ],
      hiddenColumnsTyohyoIchiran: [
        'ドキュメントNO',
        'ダウンロードパス',
        '削除不可',
        '物理ファイル名',
        '稟議添付',
        '発注稟議添付',
        'ファイル名',
        '枝番',
        '年月'
      ],
      cellStyleOptionTyohyoIchiran: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          let sClass = ''
          if (this.bCheckBorderRight == true) {
            if (column.key === '削除') {
              sClass = 'custom-border-right'
            }
          } else {
            sClass = ''
          }
          return sClass
        },
        headerCellClass: ({ column, rowIndex }) => {
          if (column.key === '参照') {
            return 'table-header-cell-custom-3'
          }
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
  inject: ['hideLeftMenu', 'showHeaderBar'],
  created () {
    let userLogIn = this.$store.getters.getUserInfo()
    this.userInfo.ユーザーＩＤ = userLogIn.userId
    this.userInfo.会社コード = userLogIn.会社コード
    this.userInfo.業者コード = userLogIn.業者コード
    this.userInfo.ユーザー種別 = userLogIn.ユーザー種別
    this.userInfo.親ＩＤ = userLogIn.親ＩＤ
    $select帳票種類value = this
  },
  mounted () {
    // 一旦この制御は不可とする
    // this.commonFunctionUI.getControlActiveButton(this.buttons, {MenuId: 'B', SubMenuId: 'WD00130'})
    this.hideLeftMenu(false)
    this.showHeaderBar(false)
    this.queryParams = {
      工事依頼No: this.$route.query.工事依頼No,
      契約No: this.$route.query.契約No,
      契約履歴No: this.$route.query.契約履歴No,
      見積明細No: this.$route.query.見積明細No,
      契約年月: this.$route.query.契約年月,
      予定年月: this.$route.query.予定年月
    }
    if (this.queryParams.工事依頼No != undefined && this.queryParams.工事依頼No != '') {
      this.queryParams.帳票区分 = '1'
      this.queryParams.参照パターン = '1'
    } else {
      if ((this.queryParams.見積明細No != undefined && this.queryParams.見積明細No != '') &&
          (this.queryParams.予定年月 != undefined && this.queryParams.予定年月 != '')) {
        this.queryParams.帳票区分 = '2'
        this.queryParams.参照パターン = '3'
      } else {
        this.queryParams.帳票区分 = '2'
        this.queryParams.参照パターン = '2'
      }
    }
    this.GetDropdown帳票種類()
    this.getPageData()
    this.画面入力制御disabled.その他帳票名 = true
  },
  methods: {
    GetDropdown帳票種類 () {
      this.http
        .get('/api/Reafs_W/Irai/WD00130/GetDropdown帳票種類', { 帳票区分: this.queryParams.帳票区分, 参照パターン: this.queryParams.参照パターン }, 'アクセスしています...')
        .then((response) => {
          if (response.data !== null) {
            $select帳票種類value.帳票種類value = response.data
            this.TyohyoIchiranForm.帳票種類 = '－'
          }
        })
    },
    getPageData () {
      this.http
        .get('/api/Reafs_W/Irai/WD00130/getData', {
          帳票区分: this.queryParams.帳票区分,
          参照パターン: this.queryParams.参照パターン,
          工事依頼No: this.queryParams.工事依頼No,
          契約No: this.queryParams.契約No,
          契約履歴No: this.queryParams.契約履歴No,
          見積明細No: this.queryParams.見積明細No,
          契約年月: this.queryParams.契約年月
        }, 'アクセスしています...')
        .then((response) => {
          if (response.data !== null) {
            this.dtTyohyoIchiran = response.data.明細表示データ
          }
          if (this.dtTyohyoIchiran.length > 10) {
            this.bCheckBorderRight = true
          } else {
            this.bCheckBorderRight = false
          }
        })
    },
    onHasseiShudo (rowIndex) {
      alert('eidt row number')
    },
    onHasseiJido (rowIndex) {
      alert('eidt row number')
    },
    onSansho (rowIndex) {
      alert('eidt row number')
    },
    openFileDialog () {
      const me = this
      // const imageExtension = ['memorybmp', 'bmp', 'emf', 'wmf', 'gif', 'jpeg', 'jpg', 'png', 'tiff', 'exif',
      //   'icon', 'pdf', '.memorybmp', '.bmp', '.emf', '.wmf', '.gif', '.jpeg', '.jpg', '.png', '.tiff', '.exif',
      //   '.pdf', '.icon', 'xlsx', '.xlsx', 'csv', '.csv', 'docx', '.docx']
      var input = document.createElement('input')
      input.type = 'file'
      // input.accept = imageExtension
      input.onchange = e => {
        me.loading = true
        var file = e.target.files[0]
        if (!file) {
          me.loading = false
          return
        }
        // ファイルアップロードサイズの制限を300MBに変更
        // if (file.size > this.MSC_5MB_LengthByte) {
        if (file.size > this.MSC_300MB_LengthByte) {
          e.preventDefault()
          this.getMsg('E040709').then(response => { // ハノイ側修正2022/10/26　STEP2_W　課題管理表№193：設計書「修正履歴」シートの「2022/10/25仕様変更分」「添付可能ファイルサイズ上限を変更」
            this.showError(response.data)
          })
          me.loading = false
          return
        }
        // FileReader support
        if (FileReader && file) {
          var fr = new FileReader()
          fr.onload = function () {
            me.TyohyoIchiranForm.添付ファイル = file.name
            me.TyohyoIchiranForm.ファイル名 = file.name
            me.upLoadFile(fr.result.split(',')[1])
          }
          fr.readAsDataURL(file)
          input.remove()
        } else {
          // Not supported
          // fallback -- perhaps submit the input to an iframe and temporarily store
          // them on the server until the user's session ends.
          input.remove()
          me.loading = false
        }
      }
      input.click()
    },
    async upLoadFile (imgBase64) {
      const data = this.getDataF093(imgBase64)
      let res = {}
      await this.http
        .post(
          '/api/Reafs_W/Common/exInsertF093',
          data,
          'アクセスしています....'
        )
        .then(response => {
          res = response
        })
      if (res.status) {
        this.TyohyoIchiranForm.添付NO = res.data
      } else {
      }
      this.loading = false
    },
    getDataF093 (imgByte) {
      const TyohyoIchiranForm = this.TyohyoIchiranForm
      const F093 = {
        添付NO: TyohyoIchiranForm.添付NO || 0,
        枝番: 1,
        削除区分: 0,
        添付元ファイル名: TyohyoIchiranForm.ファイル名,
        ファイルデータ: imgByte,
        INSERT_PG: 'WD00130'
      }
      return {
        F093_一時添付ファイル: F093,
        FLAG添付NO: TyohyoIchiranForm.添付NO
      }
    },
    getDataF090 () {
      let str契約No = ''
      let str履歴NO = ''
      let str明細NO = ''
      let str契約年月 = ''
      let str予定年月 = ''
      if (this.queryParams.帳票区分 === '2' && (this.TyohyoIchiranForm.帳票種類 !== '213' && this.TyohyoIchiranForm.帳票種類 !== '214' &&
          this.TyohyoIchiranForm.帳票種類 !== '215' && this.TyohyoIchiranForm.帳票種類 !== '216' &&
          this.TyohyoIchiranForm.帳票種類 !== '217' && this.TyohyoIchiranForm.帳票種類 !== '218')) {
        str契約No = this.queryParams.契約No
        str履歴NO = this.queryParams.契約履歴No
        str明細NO = this.queryParams.見積明細No
        str契約年月 = this.queryParams.契約年月
        str予定年月 = this.queryParams.予定年月
      }
      const f090 = {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.queryParams.工事依頼No,
        添付種類: this.TyohyoIchiranForm.帳票種類,
        その他帳票名: this.TyohyoIchiranForm.その他帳票名,
        添付元ファイル名: this.TyohyoIchiranForm.ファイル名,
        契約NO: str契約No,
        履歴NO: str履歴NO,
        明細NO: str明細NO,
        契約年月: str契約年月,
        予定年月: str予定年月,
        Ｗ参照区分: 1,
        INSERT_PG: 'WD00130'
      }
      return {
        F090_ドキュメント管理ファイル: f090,
        添付NO: this.TyohyoIchiranForm.添付NO
      }
    },
    async onDownload (row) {
      this.loading = true
      const fileName = row.物理ファイル名
      const FileDownloadInfo = {
        FilePath: row.ダウンロードパス,
        FileName: fileName
      }
      await this.http
        .post('/api/Reafs_W/Common/DownloadFile', FileDownloadInfo, 'アクセスしています....', {
          responseType: 'blob'
        })
        .then(async blob => {
          // const url = window.URL.createObjectURL(blob)
          // const link = document.createElement('a')
          // link.href = url
          // link.setAttribute('download', fileName)
          // document.body.appendChild(link)
          // link.click()
          await this.base.saveFileDownload(blob, fileName)
          this.getMsg('M040020').then(response => {
            this.showSuccess(response.data)
          })
        })
        .catch(ex => console.log(ex))
        .finally(() => {
          this.loading = false
        })
        .catch(ex => console.log(ex))
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
    async onBack () {
      window.parent.postMessage({
        'call': 'closePopup'
      }, '*')
    },
    async onSakujo (row) {
      let result = ''

      let msg = (await this.getMsg('K000002')).data
      if (!(await this.confirm(msg))) {
        return false
      }
      const data = {
        ドキュメントNO: row.ドキュメントNO,
        UPDATE_PG: 'WD00130'
      }
      await this.http
        .post('/api/Reafs_W/Common/exDeleteF090', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
        })
      if (result) {
        let msgM000013 = (await this.getMsg('M000013')).data
        this.showSuccess(msgM000013)
        this.getPageData()
      } else {
        return false
      }
    },
    async onTenpuToroku () {
      this.loading = true
      if (this.TyohyoIchiranForm.帳票種類 === '－') {
        this.getMsg('E040320').then(response => {
          this.showError(response.data)
        })
        this.$refs.ref帳票種類.focus()
        return false
      }
      if (!this.画面入力制御disabled.その他帳票名) {
        if (this.TyohyoIchiranForm.その他帳票名 === '') {
          this.getMsg('E040475').then(response => {
            this.showError(response.data)
          })
          this.$refs.refその他帳票名.focus()
          return false
        }
      }
      if (this.TyohyoIchiranForm.添付ファイル === '') {
        this.getMsg('E040469').then(response => {
          this.showError(response.data)
        })
        this.$refs.ref参照.$el.focus()
        return false
      }
      let msg = (await this.getMsg('K000004')).data
      if (!(await this.confirm(msg))) {
        this.loading = false
        return false
      }
      const data = this.getDataF090()
      let res = {}
      await this.http
        .post(
          '/api/Reafs_W/Common/exInsertF090WithSaveFile',
          data,
          'アクセスしています....'
        )
        .then(response => {
          this.loading = false
          res = response
        })
        .catch(ex => {
          this.loading = false
          // this.showError(ex)
        })
        .finally(() => {
        })
      if (res.status) {
        this.TyohyoIchiranForm.ドキュメントNO = res.data
        let msgM000008 = (await this.getMsg('M000008')).data
        this.showSuccess(msgM000008)
        this.getPageData()
        this.TyohyoIchiranForm.添付NO = ''
        this.TyohyoIchiranForm.帳票種類 = '－'
        this.TyohyoIchiranForm.その他帳票名 = ''
        this.TyohyoIchiranForm.添付ファイル = ''
        this.TyohyoIchiranForm.ファイル名 = ''
        return true
      } else {
        this.showError(res.message)
        return false
      }
    },
    selectChohyoShurui (val) {
      this.TyohyoIchiranForm.その他帳票名 = ''
      this.TyohyoIchiranForm.添付ファイル = ''
      this.TyohyoIchiranForm.ファイル名 = ''
      let 名称 = $select帳票種類value.帳票種類value.find(x => x.帳票種類 == val).帳票種類

      if (名称 === '199' || 名称 === '299') {
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
    }
  }
}
</script>

<style lang="less" scoped>
.el-row {
  //margin-bottom: 20px;
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
  min-width: 670px;
  height: 97%;
  width: 40%;
}

.sample-form-class {
  height: 100%;
}

.home-el-container {
  height: 100%;
}

.home-el-header {
  padding: 0 0 0 0;
  height: 27px;
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
  border-top: 1px solid #f3f3f3;
}
.customBtn {
  padding: 5px 5px;
}

</style>

<style>
.TyohyoIchiran .ve-table .ve-table-container table.ve-table-content {
  top: -3px !important;
}
.TyohyoIchiran .ve-table .ve-table-container .ve-table-border-y th.table-header-cell-custom-3 {
  border-right: none !important;
}
/* .TyohyoIchiran .ve-table-body-tr, .ve-table-header-tr{
  height: 30px !important;
}
.TyohyoIchiran .ve-table-header-th{
  height: 37px;
} */
.TyohyoIchiran .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr td.custom-border-right {
  border-right: 1px solid #000 !important;
}

.TyohyoIchiran .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr {
  height: 38px !important;
}

.el-scrollbar__view {
  width: 100% !important;
}

.TyohyoIchiran .el-scrollbar__wrap {
  overflow-x: hidden !important;
}

.main-content {
  margin-top: -35px !important;
}
</style>
