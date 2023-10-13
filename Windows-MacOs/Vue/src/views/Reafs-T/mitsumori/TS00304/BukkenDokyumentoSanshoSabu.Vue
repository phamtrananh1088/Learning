<template>
  <div class="home-container BukkenDokyumentoSanshoSabuins">
    <el-form
      ref="refIraiForm"
      class="sample-form-class">
        <el-container class="home-el-container">
          <el-main class="home-el-main">
              <reafstable
                ref="table"
                :tableData="tableData"
                :maxHeight="550"
                :virtualScroll="virtualScroll"
                :columns="columns"
                :rowKeyFieldName="rowKeyFieldName"
              ></reafstable>
          </el-main>
          <!-- <el-footer></el-footer> -->
        </el-container>
    </el-form>
  </div>
</template>

<script>
import reafstable from '@/components/table/Grid.vue'

export default {
  components: {
    reafstable
  },
  data () {
    return {
      queryParams: {
        物件コード: ''
      },
      rowCount: '',
      rowKeyFieldName: 'rowKey',
      columns: [
        { field: 'ファイル名',
          key: '1',
          title: 'ファイル名',
          align: 'center',
          width: 50,
          renderBodyCell: ({ row, column, rowIndex }) => {
            let text = row['ファイル名']
            return (
              <div style="text-align: left">
                <span>{text}</span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '備考',
          key: '2',
          title: '備考',
          align: 'center',
          width: 50,
          renderBodyCell: ({ row, column, rowIndex }) => {
            let text = row['備考']
            return (
              <div style="text-align: left">
                <span>{text}</span>
              </div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 1
          }
        },
        { field: '登録日',
          key: '3',
          title: '登録日',
          align: 'center',
          width: 15
        },
        {
          name: 'ダウンロード1',
          field: '',
          key: '4',
          title: '',
          width: 13.5,
          align: 'left',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block">
                <el-button
                  on-click={() => this.onDownload(row)}
                >ダウンロード</el-button>
              </div>
            )
          }
        }
      ],
      tableData: [],
      cellStyleOption: {
        headerCellClass: ({ column, rowIndex }) => {
        },
        bodyCellClass: ({ row, column, rowIndex }) => {
          if (column.key === '1') {
            return 'table-body-cell-custom-1'
          }
          if (column.key === '2') {
            return 'table-body-cell-custom-2'
          }
          if (column.key === '5') {
            return 'table-body-cell-custom-5'
          }
        }
      },
      virtualScroll: {
        enable: true,
        minRowHeight: 50
      }
    }
  },
  watch: {

  },
  computed: {

  },
  inject: ['hideLeftMenu'],
  created () {
  },
  mounted () {
    this.queryParams = {
      物件コード: this.$route.query.物件コード
    }
    this.hideLeftMenu(false)
    this.getPageData()
  },
  methods: {
    async getPageData () {
      await this.http
        .get('/api/Reafs_T/BukkenDokyumentoSanshoSabu/GetPageData', { 物件コード: this.queryParams.物件コード })
        .then((response) => {
          this.loading = false
          if (response.status) {
            this.tableData = response.data
          } else {
            return false
          }
        })
    },
    async onDownload (row) {
      const FileDownloadInfo = {
        FilePath: row.DownloadLink,
        FileName: row.ファイル名
      }
      await this.http
        .post('/api/Reafs_T/Common/DownloadFile', FileDownloadInfo, '', {
          responseType: 'blob'
        })
        .then(async blob => {
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: row.ファイル名
          // })
          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()

          await this.base.saveFileDownload(blob, row.ファイル名)

          this.getMsg('M040020').then(response => {
            this.showSuccess(response.data)
          })
        })
        .catch(ex => console.log(ex))
        .finally(() => {
        })
        .catch(ex => console.log(ex))
    },
    async onBack () {
      window.parent.postMessage({
        'call': 'closePopup'
      }, '*')
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading()
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
          self.hideLoading()
        }
      })
    },
    hideLoading () {
      // eslint-disable-next-line no-return-assign
      Object.keys(this.loading).forEach(v => (this.loading[v] = false))
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  }
}
</script>
<style scoped>
  /* .home-container {
    padding: 10px;
    min-width: 800px;
  } */
</style>
<style>
  .BukkenDokyumentoSanshoSabuins .ve-table .ve-table-container table.ve-table-content
  thead.ve-table-header tr.ve-table-header-tr th.table-header-cell-custom-1 {
    text-align: center !important;
  }
  .BukkenDokyumentoSanshoSabuins .ve-table .ve-table-container table.ve-table-content
  thead.ve-table-header tr.ve-table-header-tr th.table-header-cell-custom-2 {
    text-align: center !important;
  }
  .BukkenDokyumentoSanshoSabuins .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body
  tr.ve-table-body-tr td.ve-table-body-td, .ve-table .ve-table-container table.ve-table-content
  tbody.ve-table-body tr.ve-table-expand-tr td.ve-table-body-td, .ve-table .ve-table-container
  table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.table-body-cell-custom-1 {
    margin-left: 3px !important;
  }
  .BukkenDokyumentoSanshoSabuins .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body
  tr.ve-table-body-tr td.ve-table-body-td, .ve-table .ve-table-container table.ve-table-content
  tbody.ve-table-body tr.ve-table-expand-tr td.ve-table-body-td, .ve-table .ve-table-container
  table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.table-body-cell-custom-2 {
    margin-left: 3px !important;
  }
  .BukkenDokyumentoSanshoSabuins .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body
  tr.ve-table-body-tr td.ve-table-body-td, .ve-table .ve-table-container table.ve-table-content
  tbody.ve-table-body tr.ve-table-expand-tr td.ve-table-body-td, .ve-table .ve-table-container
  table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.table-body-cell-custom-5 {
    padding: 5px !important;
  }
  .header-collapse {
        display: none !important;
  }
  .header-logo {
    display: none !important;
  }
  .header-title[data-v-4929537c]{
    text-align: left !important;
  }

  .BukkenDokyumentoSanshoSabuins .ve-table-container {
    height: 550px !important;
  }

  html, body, #app {
    min-width: 0 !important ;
  }

  .el-scrollbar__wrap, .el-scrollbar, .main-content{
    height: fit-content !important;
  }

  .el-scrollbar__view {
    width: 1146px !important;
  }
  .BukkenDokyumentoSanshoSabuins .home-el-main {
    padding: 10px !important;
  }
</style>
