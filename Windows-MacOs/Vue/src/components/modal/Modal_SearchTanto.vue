<template>
  <el-dialog
    title="担当者検索"
    :visible="dialogVisible"
    class="dialog-class"
    @close="onClose()"
    :close-on-click-modal="false"
  >
    <el-form
      :model="searchForm"
      :inline="true"
      ref="searchForm"
      label-width="100px"
    >
      <el-form-item label="担当者コード" prop="torihikisakiCd">
        <reafsinputtext
          v-model="searchForm.tantoCd"
          :width="'120px'"
        ></reafsinputtext>
      </el-form-item>
      <el-form-item label="担当者名" prop="torihikisakiNm">
        <reafsinputtext
          v-model="searchForm.tantoNm"
          :width="'150px'"
        ></reafsinputtext>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSubmit" size="mini">検索</el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="meisaiTable"
      :data="tableData"
      :header-cell-style="headerCellStyle"
      stripe
      height="400"
      style="width: 100%"
      @row-dblclick="backData"
    >
      <el-table-column type="index" width="40"> </el-table-column>
      <el-table-column
        prop="tantoCd"
        label="担当者コード"
        sortable
        width="120"
      >
      </el-table-column>
      <el-table-column prop="tantoNm" label="担当者名" width="180">
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
var $thisValue;
export default {
  components: {
    reafsinputtext,
  },
  data() {
    return {
      searchForm: {
        tantoCd: "",
        tantoNm: "",
      },
      tableData: [],
    };
  },

  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    },
  },

  methods: {
    onClose() {
      this.$emit("update:dialogVisible", false);
    },
    onSubmit() {
      $thisValue = this;
      this.http.ajax({
        url: "api/searchTanto",
        json: true,
        success: function (result) {
          $thisValue.tableData = result.data;
        },
        type: "get",
        async: false,
      });
    },
    headerCellStyle() {
      return "text-align: center;";
    },
    backData(row, column) {
      this.$emit("backData", row);
      this.$emit("update:dialogVisible", false);
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
</style>