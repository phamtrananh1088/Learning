<template>
  <el-dialog title="物件" :visible="dialogVisible" class="dialog-class" @close="onClose()" :close-on-click-modal="false">
    <el-form :model="searchForm" :inline="true" ref="searchForm" label-width="126px">
      <el-row>
        <el-col span="10">
          <el-form-item label="物件名" prop="buNm">
            <reafsinputtext v-model="searchForm.buNm" :width="'110px'"></reafsinputtext>
          </el-form-item>
        </el-col>
        <el-col span="10">
          <el-form-item label="営業所（管理部署）" prop="eigyoBushoNm">
            <reafsinputtext v-model="searchForm.eigyoBushoNm" :width="'110px'"></reafsinputtext>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col span="10">
          <el-form-item label="物件区分" prop="buKbn">
            <el-select width="10px">
              <el-option>物件区分1</el-option>
              <el-option>物件区分2</el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col span="10">
          <el-form-item label="郵便番号" prop="yubinNo">
            <reafsinputtext v-model="searchForm.yubinNo" :width="'110px'"></reafsinputtext>
          </el-form-item>
        </el-col>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" size="mini">検索</el-button>
        </el-form-item>
      </el-row>
    </el-form>
    <el-table
      ref="meisaiTable"
      :data="tableData"
      :header-cell-style="headerCellStyle"
      stripe
      height="400"
      style="width: auto"
      @row-dblclick="backData">
      <el-table-column type="index" width="50" label="No"></el-table-column>
      <el-table-column prop="buCd" label="物件コード" width="180"> </el-table-column>
      <el-table-column prop="buCd" label="営業所（依頼主）" width="140"> </el-table-column>
      <el-table-column prop="buCd" label="営業所（管理部署）" width="120">
        <el-table-column prop="buCd" label="事務所" width="40"> </el-table-column>
        <el-table-column prop="buCd" label="営業所" width="40"> </el-table-column>
        <el-table-column prop="buCd" label="部" width="40"> </el-table-column>
        <el-table-column prop="buCd" label="課" width="40"> </el-table-column>
        <el-table-column prop="buCd" label="係" width="40"> </el-table-column>
      </el-table-column>
      <el-table-column prop="buNm" label="物件名" width="180"> </el-table-column>
      <el-table-column prop="buNm" label="物件区分" width="180"> </el-table-column>
      <el-table-column prop="buNm" label="郵便番号" width="180"> </el-table-column>
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
        buNm: "",
        eigyoBushoNm: "",
      },
      tableData: [
        {
          buCd: "0010-000",
          torihikisakinm: "テスト",
          buNm: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-001",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-002",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-003",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
        {
          torihikisakicd: "0010-000",
          torihikisakinm: "テスト",
          address: "2110041 神奈川県川崎市中原区0-00-00",
        },
      ],
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

.el-form >>> .el-input__inner {
  width: 110px;
}
</style>