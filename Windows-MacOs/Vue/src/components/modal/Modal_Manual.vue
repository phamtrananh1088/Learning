<template>
  <el-dialog :title="pageTitle" :visible="dialogVisible" :show-close="false" class="dialog-class" @opened="onOpen()" @close="onClose()" :close-on-click-modal="false">

    <el-table
      ref="meisaiTable"
      :data="tableData"
      :cell-style="cellStyle"
      border
      :span-method="objectSpanMethod"
      style="width: 100%;"
      :show-header="false"
      >
        <el-table-column
          prop="title"
          label="title"
          sortable
          width="100">
        </el-table-column>
        <el-table-column
          prop="fileName"
          label="fileName">
          <el-button type="text" slot-scope="scope" @click="onDownLoadFile(scope.row)">{{ scope.row.fileName }}</el-button>
        </el-table-column>
    </el-table>
    <el-row style="margin-top:20px;">
      <el-col :span="18">
        <p/>
      </el-col>
      <el-col :span="6">
        <el-button @click="onClose" style="float: right">閉じる</el-button>
      </el-col>
  </el-row>
  </el-dialog>
</template>

<script>

const webconfig = require('../../../static/webconfig.json')

export default {
  components: {
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    manualType: {
      type: String,
      default: null
    }
  },
  data () {
    return {
      tableData: null,
      pageTitle: 'マニュアルダウンロード'
    }
  },
  methods: {
    async onOpen () {
      let type = ''
      let title = ''
      if (this.manualType === 'W') {
        // this.tableData = [ {
        //   id: 1,
        //   title: '【Reafs-W】SGW用',
        //   fileName: '【Reafs】フェーズ2_Reafs-W_画面操作説明書Ver1.00.pdf',
        //   filePath: webconfig.manual_W_path1
        // }
        // ]
        type = this.manualType
        title = '【Reafs-W】SGW用'
      } else if (this.manualType === 'T') {
        // this.tableData = [ {
        //   id: 1,
        //   title: '【Reafs-T】業者用',
        //   fileName: '【Reafs】フェーズ2_Reafs-T_画面操作説明書Ver1.00.pdf',
        //   filePath: webconfig.manual_T_path1
        // }
        // ]
        type = this.manualType
        title = '【Reafs-T】業者用'
      } else if (this.manualType === 'R') {
        // this.tableData = [{
        //   id: 1,
        //   title: '【Reafs-R】※社内用',
        //   fileName: '【Reafs】フェーズ2_Reafs-R_画面操作説明書Ver1.00_タブレット.pdf',
        //   filePath: webconfig.manual_R_path1
        // }
        // ]
        type = 'R_Web'
        title = '【Reafs-R】※社内用'
      }
      this.tableData = await this.getFileDownloadManualInfo(type, title)
    },
    async getFileDownloadManualInfo (type, title) {
      const tblData = []
      const res = await this.http
        .get('/api/Common/getFileDownloadManualInfo', { type })
      if (res.status) {
        if (!res.data) res.data = [{}]
        for (let i = 0; i < res.data.length; i++) {
          tblData.push({
            id: i,
            title: title,
            fileName: res.data[i].fileName,
            filePath: res.data[i].filePath
          })
        }
      } else {
        this.showError(res.message)
      }
      return tblData
    },
    onClose () {
      this.$emit('update:dialogVisible', false)
    },
    cellStyle ({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 1) {
        // if (rowIndex === 8 || rowIndex === 9) {
        //   return 'padding-top: 15px;padding-bottom: 15px;'
        // }
      } else if (columnIndex === 0) {
        return 'text-align: center;'
      }
    },
    objectSpanMethod ({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) {
        if (rowIndex === 0) {
          return {
            rowspan: 99,
            colspan: 1
          }
        }
        //  else if (rowIndex === 3) {
        //   return {
        //     rowspan: 3,
        //     colspan: 1
        //   }
        // } else if (rowIndex === 6) {
        //   return {
        //     rowspan: 2,
        //     colspan: 1
        //   }
        // }
         else {
          return {
            rowspan: 0,
            colspan: 0
          }
        }
      }
    },
    onDownLoadFile (obj) {
      let fileFullPath = {
        FilePath: obj.filePath,
        FileName: obj.fileName
      }

      this.http
        .post('api/Reafs_W/Common/DownloadFile', fileFullPath, '', {
          responseType: 'blob'
        })
        .then(async blob => {
          await this.base.saveFileDownload(blob, obj.fileName)
        })
    },
    showError (message, option = null, title = this.pageTitle) {
      this.MsgErr({ title,
        message,
        callback: function (a, b) {
          if (option && typeof (option.callback) === 'function') {
            option.callback()
          }
        }})
    }
  }
}
</script>
<style scoped>
.el-table{
  border-style: solid;
  border-width: 1px;
  border-color: rgb(193, 197, 205);
}

.el-table >>> .el-table__cell{
  padding-top: 5px;
  padding-bottom: 5px;
  border-right-style: solid;
  border-right-width: 1px;
  border-right-color: rgb(235, 238, 245);
}

.el-dialog__wrapper.dialog-class >>> .el-dialog {
  position: relative;
  margin: 0 auto 50px;
  background: #fff;
  border-radius: 2px;
  -webkit-box-shadow: 0 1px 3px rgb(0 0 0 / 30%);
  box-shadow: 0 1px 3px rgb(0 0 0 / 30%);
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 90%;
}

</style>
