<template>
  <el-dialog title="取引先" :visible="dialogVisible" class="dialog-class" @close="onClose()" :close-on-click-modal="false">
    <el-form 
      :model="searchForm"
      :inline="true"
      ref="searchForm"
      label-width="100px"
    >
      <el-form-item label="取引先コード" prop="torihikisakiCd">
        <reafsinputtext 
          v-model="searchForm.torihikisakiCd"
          :width="'120px'"
        ></reafsinputtext>
      </el-form-item> 
      <el-form-item label="取引先名" prop="torihikisakiNm">
        <reafsinputtext 
          v-model="searchForm.torihikisakiNm"
          :width="'150px'"
        ></reafsinputtext>
      </el-form-item> 
      <el-form-item label="対象" class="radio-group-class">
        <el-radio-group v-model="searchForm.taisyo" >
          <el-radio label="佐川急便" checked></el-radio>
          <el-radio label="SGR"></el-radio>
          <el-radio label="その他"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="表示対象" class="radio-group-class">
        <el-radio-group v-model="searchForm.taisyoHyouji">
          <el-radio label="利用可能"></el-radio>
          <el-radio label="利用停止"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="onSubmit"
          size="mini"
        >検索</el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="meisaiTable"
      :data="tableData"
      :header-cell-style="headerCellStyle"
      stripe
      height="400"
      style="width: 100%;"
      @row-click="backData"
    >
     <!-- @row-dblclick="backData" -->
        <el-table-column
          type="index"
          width="40">
        </el-table-column>
        <el-table-column
          prop="torihikisakicd"
          label="取引先ｺｰﾄﾞ"
          sortable
          width="120">
        </el-table-column>
        <el-table-column
          prop="torihikisakinm"
          label="取引先名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="address"
          label="住所">
        </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
export default {
  components: {
    reafsinputtext,
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false,
    }
  },
  data(){
    return{
      searchForm:{
        torihikisakiCd:'',
        torihikisakiNm:'',
        taisyo:'佐川急便',
        taisyoHyouji:'利用可能',
      },
      tableData: [{
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, {
        torihikisakicd: '0010-001',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, {
        torihikisakicd: '0010-002',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, {
        torihikisakicd: '0010-003',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      },  {
        torihikisakicd: '0010-000',
        torihikisakinm: 'テスト',
        address: '2110041 神奈川県川崎市中原区0-00-00'
      }, 
      ],
      }
  },
  methods: {
    onClose() {
      this.$emit('update:dialogVisible', false)
    },
    onSubmit(){
      console.log(1);
    },
    headerCellStyle(){
      return 'text-align: center;'
    },
    backData(row, column) {        
      // this.$emit("backData", row);
      // this.$emit("update:dialogVisible", false);

      // row-dblclick　タブレット無効 
      // 代わりに row-click を使用してください
      if (0 == this.touchtime) {
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
          //2回目のクリックが最初のクリックから0.8秒後の場合、
          //次に、2回目のクリックは、デフォルトで次のダブルクリックによって判断される最初のクリックになります。
          this.touchtime = new Date().getTime();
        }
      }
    },
  },
};
</script>
<style scoped>
.dialog-class >>> .el-dialog__header{
  padding-top: 10px;
  padding-bottom: 0;
}

.radio-group-class >>> .el-form-item__content{
  line-height: 28px;
  border: 1px solid #DCDFE6;
  padding-top: 2px !important;
}

.el-form >>> .el-form-item__label{
  margin-top: 0px;
}

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

.el-table >>> .el-table__body:hover {
  cursor: pointer;
}

</style>