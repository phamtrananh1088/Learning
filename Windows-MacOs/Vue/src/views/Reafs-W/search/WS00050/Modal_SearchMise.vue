<template>
  <el-dialog 
    title="店検索" 
    :visible="dialogVisible" 
    class="dialog-class" 
    @open="openSearch()"
    @close="onClose()"
    :close-on-click-modal="false">
    <el-form :model="searchForm" ref="searchForm">
      <el-row>
        <el-col :span="20" :xs="24">
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="店名称" prop="miseNm">
              <reafsinputtext v-model="searchForm.miseNm" :width="'300px'" maxlength="20"
                ref="txtMiseNm"></reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="店コード" prop="miseCd">
              <div style="display: inline-block;">
                <div style="display: flex;">
                  <reafsinputtext v-model="searchForm.miseCd" :width="'100px'" maxlength="4"></reafsinputtext>
                  <reafsinputtext v-model="searchForm.miseEdaban" :width="'50px'" maxlength="2"></reafsinputtext>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="支社コード" prop="shiShaCd">
              <reafsinputtext v-model="searchForm.shiShaCd" :width="'100px'" maxlength="4"></reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="JIS地区11桁" prop="jisCd">
              <reafsinputtext v-model="searchForm.jisCd" :width="'150px'" maxlength="11"></reafsinputtext>
            </el-form-item>
          </el-col>
        </el-col>
        <el-col :span="4" :xs="24">
          <el-button 
            type="primary" 
            size="mini"
            :loading="loading"
            @click="onSubmit" 
            @keydown.native ="down2first"
            >検索</el-button
          >
        </el-col>
      </el-row>
    </el-form>
    <div class="tableDataCnt">
      <span style="position: absolute; right: 95px">件数：</span>
      <span style="margin-right: 16px">{{ dataCnt.toString() }}</span>
      <span style="position: absolute; right: 20px"> 件</span>
    </div>
    <el-table ref="meisaiTable" :data="sliceTable" v-loading="loading" :header-cell-style="headerCellStyle"
      :cell-style="{ padding: '0px' }" stripe border height="668" style="margin-top: 10px; scroll: auto"
      @row-click="backData" @sort-change="changeTableSort">
      <!-- @row-dblclick="backData" -->
      <!-- <el-table-column prop="行NO" label="No" width="auto" align="center">
      </el-table-column> -->
      <el-table-column type="index" width="50" label="No" align="center" :index="setIndex"></el-table-column>
      <el-table-column prop="店コード" label="店コード" width="100" align="center" sortable>
      </el-table-column>
      <el-table-column prop="店枝番" label="店枝番" width="90" align="center" sortable>
      </el-table-column>
      <el-table-column prop="店名称" label="店名称" min-width="90" sortable>
      </el-table-column>
      <el-table-column prop="支社コード" label="支社コード" width="110" align="center" sortable>
      </el-table-column>
      <el-table-column prop="JIS地区11桁" label="JIS地区11桁" width="140" align="center" sortable>
      </el-table-column>
      <el-table-column prop="郵便番号" label="郵便番号" width="120" align="center" sortable>
      </el-table-column>
      <el-table-column prop="都道府県" label="都道府県" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="市区町村" label="市区町村" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="区画番地" label="区画番地" min-width="120" sortable>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'

var $thisValue
export default {
  components: {
    reafsinputtext
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      searchForm: {
        miseNm: '',
        miseCd: '',
        miseEdaban: '',
        shiShaCd: '',
        jisCd: ''
      },
      dataCnt: 0,
      loading: false,
      tableData: [],
      searchparam: {
        param1: '',
        param2: '',
        param3: '',
        param4: '',
        param5: '',
      },

      startindex: 0,
      sliceTable: [],
      vEle: undefined,
      showCount: 30,
      headerHeight: 41,
      rowHeight: 23,
      tableHeight: 0
    }
  },
  created () {
    // 仮想スクロール 設定
    this.tableHeight = this.rowHeight * this.showCount + this.headerHeight
    this.vEle = document.createElement('div')
  },
  methods: {
    openSearch() {
      //サブ画面を起動するタイミングで、1つ目のコントロールに設定する
      this.$nextTick(function () {
        this.$refs.txtMiseNm.focus()
      });
    },
    onClose () {
      this.clearData()
      this.$emit('update:dialogVisible', false)
    },
    onSubmit () {
      $thisValue = this
      $thisValue.loading = true
      $thisValue.tableData = undefined
      $thisValue.sliceTable = undefined
      $thisValue.dataCnt = 0
      // 仮想スクロール スクロール位置を初期化
      $thisValue.$nextTick(() => {
        this.$refs.meisaiTable.$el.querySelector(
          '.el-table__body-wrapper'
        ).scrollTop = 0
      })

      this.searchparam.param1 = this.searchForm.miseCd // 店コード
      this.searchparam.param2 = this.searchForm.miseNm // 店名称
      this.searchparam.param3 = this.searchForm.miseEdaban // 店コード枝番
      this.searchparam.param4 = this.searchForm.shiShaCd // 支社コード
      this.searchparam.param5 = this.searchForm.jisCd // JIS地区11桁

      var qs = require('qs')
      this.http
        .post('api/Reafs_W/Search/searchMise', qs.stringify(this.searchparam))
        .then((result) => {
          if (!result.status || result.data == null) {
            this.loading = false
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: result.message
            })
          }

          $thisValue.tableData = result.data
          $thisValue.sliceTable = []
          $thisValue.startindex = 0
          // 画面表示テーブル高さを計算する
          $thisValue.tableHeight =
            $thisValue.rowHeight * $thisValue.showCount +
            $thisValue.headerHeight

          this.loadData()

          $thisValue.tableData = result.data
          var reg = /\d{1,3}(?=(\d{3})+$)/g
          $thisValue.dataCnt = ($thisValue.tableData.length + '').replace(
            reg,
            '$&,'
          )
          this.loading = false
        })
    },

    clearData () {
      this.searchForm.miseNm = ''
      this.searchForm.miseCd = ''
      this.searchForm.miseEdaban = ''
      this.searchForm.shiShaCd = ''
      this.searchForm.jisCd = ''
      this.tableData = undefined
      // 仮想スクロール
      this.sliceTable = undefined
      this.startindex = 0
      this.dataCnt = 0
    },

    headerCellStyle () {
      return 'text-align: center;background-color:#BDD7EE'
    },
    backData (row, column) {
      // this.$emit("backData", row);
      // this.$emit("update:dialogVisible", false);

      // row-dblclick　タブレット無効
      // 代わりに row-click を使用してください
      if (this.touchtime == 0) {
        this.touchtime = new Date().getTime()
      } else {
        if (new Date().getTime() - this.touchtime < 800) {
          this.$emit('backData', {
            txtCd: row.店コード + row.店枝番,
            lblNm: row.SGW用店正式名,
            row: row
          })
          this.clearData()
          this.$emit('update:dialogVisible', false)
          this.touchtime = 0
        } else {
          // 2回目のクリックが最初のクリックから0.8秒後の場合、
          // 次に、2回目のクリックは、デフォルトで次のダブルクリックによって判断される最初のクリックになります。
          this.touchtime = new Date().getTime()
        }
      }
    },
    loadData () {
      if (!$thisValue.tableData) {
        return
      }

      // 仮想スクロール　表示用データ行数+５のみ、テーブルに入る
      $thisValue.sliceTable = $thisValue.tableData.slice(
        $thisValue.startindex,
        $thisValue.startindex + $thisValue.showCount
      )

      // テーブルは「position:absolute」に設定する
      $thisValue.$nextTick(() => {
        $thisValue.$refs.meisaiTable.$el
          .querySelector('.el-table__body-wrapper')
          .addEventListener('scroll', $thisValue.tableScroll, {
            passive: true
          })

        $thisValue.$refs.meisaiTable.$el.querySelector(
          '.el-table__body'
        ).style.position = 'absolute'

        $thisValue.vEle.style.height =
          $thisValue.tableData.length * $thisValue.rowHeight + 'px'

        $thisValue.$refs.meisaiTable.$el
          .querySelector('.el-table__body-wrapper')
          .appendChild($thisValue.vEle)
      })
    },

    // テーブルはスクロール位置より移動する
    tableScroll () {
      let bodyWarpperEle = this.$refs.meisaiTable.$el.querySelector(
        '.el-table__body-wrapper'
      )
      let scrollTop = bodyWarpperEle.scrollTop

      this.startindex = Math.floor(scrollTop / this.rowHeight)

      // スクロールは最後の位置
      if (
        bodyWarpperEle.scrollHeight <=
        scrollTop + bodyWarpperEle.clientHeight
      ) {
        bodyWarpperEle.querySelector(
          '.el-table__body'
        ).style.transform = `translateY(${
          this.startindex * this.rowHeight - 2
        }px)`
      } else {
        bodyWarpperEle.querySelector(
          '.el-table__body'
        ).style.transform = `translateY(${this.startindex * this.rowHeight}px)`
      }

      this.loadData()
    },
    
    //ソート機能
    changeTableSort(val){
      if (val.order == "ascending") {
        $thisValue.tableData = $thisValue.tableData.sort((a,b) => a[val.prop].toUpperCase() > b[val.prop].toUpperCase() ? 1 : -1);
      } else if (val.order == "descending"){
        $thisValue.tableData = $thisValue.tableData.sort((a,b) => b[val.prop].toUpperCase() > a[val.prop].toUpperCase() ? 1 : -1);
      } else {
        $thisValue.tableData = $thisValue.tableData.sort((a,b) => a[val.prop].toUpperCase() > b[val.prop].toUpperCase() ? 1 : -1);
      }
      
      // 仮想スクロール　表示用データ行数+５のみ、テーブルに入る
      $thisValue.sliceTable = $thisValue.tableData.slice(
        this.startindex,
        this.startindex + this.showCount
      )
    },
    
    down2first(event) {
      if (event.keyCode === 9) {
        this.$nextTick(function () {
          this.$refs.txtMiseNm.focus();
          event.preventDefault();
        });
      }
    },
  },
  computed: {
    setIndex(){
      return this.startindex + 1
    }
  },
}
</script>
<style scoped>
.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
.el-dialog__wrapper {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: auto;
  margin: 0;
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
.dialog-class >>> .el-dialog__header {
  padding-top: 10px;
  padding-bottom: 0;
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
.el-table >>> .el-table__body tr:hover > td{
  background-color: #abdcff !important;
}
.el-table >>> .el-table__body tr.current-row > td{
  background-color: #abdcff !important;
}

.tableDataCnt {
  margin-top: -15px;
  margin-bottom: -15px;
  text-align: right;
}
</style>
