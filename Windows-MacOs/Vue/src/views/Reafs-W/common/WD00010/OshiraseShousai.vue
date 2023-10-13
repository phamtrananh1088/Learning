<template>
  <el-dialog
    title="お知らせ詳細"
    :visible="dialogVisible"
    class="dialog-class"
    @close="onClose()"
    @opened="onOpen()"
    :close-on-click-modal="false"
  >
    <el-form :model="rowData" :inline="false" ref="rowData" label-width="100px">
      <el-form-item label="件名" prop="件名">
        <el-input
          v-model="rowData.件名"
          :readonly="true"
          :autosize="true"
          resize="true"
        ></el-input>
      </el-form-item>
      <el-form-item label="掲載日" prop="掲示開始日">
        <el-input v-model="rowData.掲示開始日" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="登録者" prop="登録者FULL">
        <el-input v-model="rowData.登録者FULL" :readonly="true"></el-input>
      </el-form-item>
      <el-form-item label="添付" prop="FILENAME">
        <el-col class="linkCol">
          <!-- <el-input v-model="rowData.TENPU" readonly="true"></el-input> -->
          <el-link :underline="false" type="primary" @click="onDownLoadFile">{{
            rowData.FILENAME
          }}</el-link>
        </el-col></el-form-item
      >
      <el-form-item label="詳細" prop="内容" class="textarea-class">
        <el-input
          v-model="rowData.内容"
          type="textarea"
          :autosize="{ minRows: 2 }"
        ></el-input>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
    rowData: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {};
  },
  methods: {
    onClose() {
      this.$emit("update:dialogVisible", false);
    },
    onOpen() {
      console.log("onOpen");
      console.log(this.rowData);
    },
    onSubmit() {},
    onDownLoadFile(obj) {
      let $thisValue = this;
      let fileFullPath = {
        FilePath: this.rowData.FILE_PATH,
        FileName: this.rowData.FILENAME,
      };

      this.http
        .post("api/Reafs_W/Common/DownloadFile", fileFullPath, "", {
          responseType: "blob",
        })
        .then(async (blob) => {
          // const url = window.URL.createObjectURL(blob);
          // const link = document.createElement("a");
          // link.href = url;
          // link.setAttribute("download", $thisValue.rowData.FILE_PATH);
          // document.body.appendChild(link);
          // link.click();

          await this.base.saveFileDownload(blob, $thisValue.rowData.FILENAME)

          // document.body.removeChild(link);
          // $thisValue.joukyouList = response.data;
        });
    },
    backData(row, column) {
      // row-dblclick　タブレット無効
      // 代わりに row-click を使用してください
      if (this.touchtime == 0) {
        undefined;
        this.touchtime = new Date().getTime();
      } else {
        undefined;
        if (new Date().getTime() - this.touchtime < 800) {
          undefined;
          this.$emit("backData", row);
          this.$emit("update:dialogVisible", false);
          this.touchtime = 0;
        } else {
          undefined;
          // 2回目のクリックが最初のクリックから0.8秒後の場合、
          // 次に、2回目のクリックは、デフォルトで次のダブルクリックによって判断される最初のクリックになります。
          this.touchtime = new Date().getTime();
        }
      }
    },
  },
};
</script>
<style scoped>
.dialog-class >>> .el-dialog__header {
  padding-top: 10px;
  padding-bottom: 0;
}

.radio-group-class >>> .el-form-item__content {
  line-height: 28px;
  border: 1px solid #dcdfe6;
  padding-top: 2px !important;
}

.el-form >>> .el-form-item__label {
  margin-top: 0px;
}

.el-form-item {
  margin-bottom: 0px;
}

.el-table {
  border-style: solid;
  border-width: 1px;
  border-color: rgb(193, 197, 205);
}
.el-table >>> .el-table__cell {
  padding-top: 5px;
  padding-bottom: 5px;
  border-right-style: solid;
  border-right-width: 1px;
  border-right-color: rgb(235, 238, 245);
}

.el-table >>> .el-table__body:hover {
  cursor: pointer;
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
.linkCol {
  border: 1px solid #dcdfe6;
  height: 29px;
  line-height: 25px;
  border-radius: 1px;
  margin: 1px 0px;
  padding: 0px 15px;
}
</style>
<style >
</style>
