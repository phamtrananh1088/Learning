<template>
  <el-dialog :title="this.gameName + '検索'" :visible="dialogVisible" class="dialog-class" @close="onClose()"
    @opened="onOpened()" :close-on-click-modal="false">
    <el-form :model="searchForm" ref="searchForm">
      <el-row>
        <el-col :span="20">
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item :label="this.gameName + 'コード'" prop="cd">
              <div style="display: inline-block;">
                <div style="display: flex;">
                  <reafsinputtext v-model="searchForm.cd" :width="'120px'" maxlength="6"
                    ref="cd"></reafsinputtext>
                  <reafsinputtext v-model="searchForm.eda" :width="'80px'" maxlength="3"></reafsinputtext>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item :label="this.gameName + '名'" prop="nm">
              <reafsinputtext v-model="searchForm.nm" :width="'300px'" maxlength="20"></reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="24" :md="24" :lg="12">
            <el-form-item label="対象" prop="taisyou">
              <el-radio-group v-model="searchForm.taisyou" :disabled="this.setDisabled">
                <el-radio :label="0">佐川急便</el-radio>
                <el-radio :label="1">SGR</el-radio>
                <el-radio :label="2">その他</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-col>
        <el-col :span="4" :xs="24">
          <el-form-item>
            <el-button 
              type="primary" 
              size="mini"
              :loading="loading"
              @click="onSubmit" 
              @keydown.native ="down2first"
              >検索</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div class="tableDataCnt">
      <span style="position: absolute; right: 95px">件数：</span>
      <span style="margin-right: 16px">{{ dataCnt.toString() }}</span>
      <span style="position: absolute; right: 20px"> 件</span>
    </div>
    <el-table ref="tableRef" :data="sliceTable" v-loading="loading" :header-cell-style="headerCellStyle"
      :cell-style="{ padding: '0px' }" stripe border height="668" style="margin-top: 10px; scroll: auto"
      @row-click="backData" @sort-change="changeTableSort">
      <!-- @row-dblclick="backData" -->
      <!-- @row-click="backData" -->
      <!-- <el-table-column label="No" prop="行NO" width="70px" align="center">
      </el-table-column> -->
      <el-table-column type="index" width="70" label="No" align="center" :index="setIndex"></el-table-column>
      <el-table-column prop="コード" :label="this.gameName + 'コード'" width="140" align="center" sortable>
      </el-table-column>
      <el-table-column prop="名" :label="this.gameName + '名'" width="300" align="left" sortable>
      </el-table-column>
      <el-table-column prop="住所" label="住所" width="auto" align="left" sortable>
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
    },
    // subParamsObj: {
    //   type: String,
    //   default: "",
    // },
    subParamsObj: {
      taisyou: { type: String }, // 承認種類　 取引先検索：対象
      jigyousyoCd: { type: String }, // 事業所コード
      torType: { type: String } // null or 0:取引先 1:得意先
    }
  },
  data () {
    return {
      searchForm: {
        cd: '', // コード
        nm: '', // 名
        eda: '', // コード枝番
        taisyou: -1 // 対象
      },
      dataCnt: 0,
      loading: false,
      tableData: [],
      paramsList: {
        param1: '',
        param2: '',
        param3: '',
        param4: '',
        param5: ''
      },
      setDisabled: true,
      startindex: 0,
      sliceTable: [],
      vEle: undefined,
      showCount: 30,
      headerHeight: 41,
      rowHeight: 23,
      tableHeight: 0,
      gameName: ''
    }
  },
  created () {
    // 主画面から引数を取得し設定
    this.searchForm.taisyou = parseInt(this.subParamsObj.taisyou)
    this.setDisabled = this.subParamsObj.taisyou.length > 0 // 仮想スクロール
    // 仮想スクロール 設定
    this.tableHeight = this.rowHeight * this.showCount + this.headerHeight
    this.vEle = document.createElement('div')
  },
  methods: {
    setDisabel () {
      return true
    },
    onClose () {
      this.clearData()
      this.$emit('update:dialogVisible', false)
    },
    onOpened () {
      if (this.subParamsObj && this.subParamsObj.torType == '1') {
        this.gameName = '得意先'
      } else {
        this.gameName = '取引先'
      }
      
      //サブ画面を起動するタイミングで、1つ目のコントロールに設定する
      this.$nextTick(function () {
        this.$refs.cd.focus()
      });
    },
    clearData () {
      this.searchForm.cd = '' // コード
      this.searchForm.nm = '' // 名
      this.searchForm.eda = '' // コード枝番
      this.searchForm.taisyou = parseInt(this.subParamsObj.taisyou) // 対象
      this.tableData = undefined
      // 仮想スクロール
      this.sliceTable = undefined
      this.startindex = 0
      this.dataCnt = 0
    },
    onSubmit () {
      $thisValue = this
      this.loading = true
      $thisValue.tableData = undefined
      $thisValue.sliceTable = undefined
      $thisValue.dataCnt = 0
      // 仮想スクロール スクロール位置を初期化
      $thisValue.$nextTick(() => {
        this.$refs.tableRef.$el.querySelector(
          '.el-table__body-wrapper'
        ).scrollTop = 0
      })

      this.paramsList.param1 = this.searchForm.cd // コード
      this.paramsList.param2 = this.searchForm.nm // 名
      this.paramsList.param3 = this.searchForm.eda // コード枝番
      this.paramsList.param4 = this.searchForm.taisyou // 対象
      this.paramsList.param5 = '' // 対象

      // 区分: 0 OR NULL 取引先  1:得意先
      this.paramsList.param9 = this.subParamsObj
        ? this.subParamsObj.torType
        : ''

      var qs = require('qs')
      this.http
        .post(
          'api/Reafs_W/Search/searchTorihikisaki',
          qs.stringify(this.paramsList)
        )
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
    headerCellStyle () {
      return 'text-align: center;background-color:#BDD7EE'
    },
    backData (row, column) {
      // row-dblclick　タブレット無効
      // 代わりに row-click を使用してください
      if (this.touchtime == 0) {
        this.touchtime = new Date().getTime()
      } else {
        if (new Date().getTime() - this.touchtime < 800) {
          this.$emit('backData', {
            // 戻り値：「コード」「取引先名」「郵便番号」「住所」
            txtCd: row.コード,
            lblNm: row.名,
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
        $thisValue.$refs.tableRef.$el
          .querySelector('.el-table__body-wrapper')
          .addEventListener('scroll', $thisValue.tableScroll, {
            passive: true
          })

        $thisValue.$refs.tableRef.$el.querySelector(
          '.el-table__body'
        ).style.position = 'absolute'

        $thisValue.vEle.style.height =
          $thisValue.tableData.length * $thisValue.rowHeight + 'px'

        $thisValue.$refs.tableRef.$el
          .querySelector('.el-table__body-wrapper')
          .appendChild($thisValue.vEle)
      })
    },

    // テーブルはスクロール位置より移動する
    tableScroll () {
      let bodyWarpperEle = this.$refs.tableRef.$el.querySelector(
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
          this.$refs.cd.focus();
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

.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
</style>
