<template>
  <div class="listviewform">
    <div>
      <span>Target date</span>
      <el-date-picker v-model="tgtdate" type="date" @change="reload" placeholder="Pick a day" />
    </div>
    <div class="infinite-list-wrapper" style="overflow:auto">
      <div
        class="listview"
        v-infinite-scroll="loadMore"
        infinite-scroll-disabled="disabled">
        <div class="slip-header">
          <div class="slip-val-elem slip-w-mid ">日付</div>
          <div class="slip-val-elem slip-w-mid ">種類</div>
          <div class="slip-val-elem slip-w-mid ">支払方法</div>
          <div class="slip-val-elem slip-w-num-h ">金額</div>
          <div class="slip-val-elem ">メモ</div>
        </div>
        <div v-for="(item, index) in sliplist" class="slip-item" :key="index" @click="slipedit">
          <div class="slip-val-elem slip-w-mid ">{{ item.tgt_date_str }}</div>
          <div class="slip-val-elem slip-w-mid ">{{ item.kind_nm }}</div>
          <div class="slip-val-elem slip-w-mid ">{{ item.method_nm }}</div>
          <div class="slip-val-elem slip-w-num ">{{ item.value_fmt }}</div>
          <div class="slip-val-elem ">{{ item.memo }}</div>
        </div>
      </div>
      <p v-if="loading">
        <span>Loading {{ loaddatecnt }} days before from </span>
        <el-date-picker v-model="wkdate" type="date" :readonly="true" />
      </p>
      <p v-if="noMore">Limit over(100 records or 33 days)</p>
    </div>
  </div>
</template>

<script>
import Vue from 'vue'
import modal from "@/components/modal/Modal.vue";

const ModalDialogConstructor = Vue.extend(modal)
const LOAD_DATE_CNT = 3
const LOAD_LIMIT_DATE = 31
const LOAD_LIMIT_REC = 100

export default {
  name: 'List',
  data () {
    return {
      tgtdate: new Date(),
      sliplist: [],
      loading: false,
      loaddatecnt: LOAD_DATE_CNT,
      loadeddatecnt: 0,
      dlgeditidx: -1,
      wkdate: new Date(),
      inputDlg: undefined
    }
  },
  mounted (e) {
    this.inputDlg = new ModalDialogConstructor({
      propsData: {
        parent: this.$el,
        myutilsprm: this.$myutils,
        masterdateprm: this.$masterdata,
        callbackprm: this.dlgCloseCallback
      }
    })
    this.inputDlg.$mount()
  },
  computed: {
    count () {
      return this.sliplist.length
    },
    noMore () {
      return this.sliplist.length >= LOAD_LIMIT_REC || this.loadeddatecnt >= LOAD_LIMIT_DATE
    },
    disabled () {
      return this.loading || this.noMore
    }
  },
  methods: {
    slipedit: function (e) {
      var elements = e.target.parentNode.childNodes
      const idx = [].slice.call(elements).indexOf(e.target) - 1
      this.dlgeditidx = idx - 1
      this.inputDlg.setSlipData(this.sliplist[this.dlgeditidx])
      this.inputDlg.show()
    },
    dlgCloseCallback: function (mode, newSlipData) {
      console.log('dlgCloseCallback:' + mode + ':' + newSlipData)
      if (mode === 'upd') {
        var wkSlip = newSlipData
        wkSlip['tgt_date_obj'] = this.$myutils.getLocalDate(newSlipData['tgt_date'])
        wkSlip['tgt_date_str'] = this.$myutils.getLocalDateStr(newSlipData['tgt_date'])
        wkSlip['kind_nm'] = this.$masterdata.getKindNm(newSlipData['kind_cd'])
        wkSlip['method_nm'] = this.$masterdata.getMethodNm(newSlipData['method_cd'])
        wkSlip['value_fmt'] = Number(newSlipData['value']).toLocaleString()
        this.sliplist[this.dlgeditidx] = wkSlip
        this.$forceUpdate()
      } else {
        this.sliplist.splice(this.dlgeditidx, 1)
      }
    },
    loadMore: function () {
      if (!this.loading) {
        this.loadsub(this)
      }
    },
    loadsub: function (that) {
      const config = {
        headers: {
          'Content-Type': 'application/json',
          'x-api-key': that.apienv.key,
          'Authorization': 1
        },
        data: {}
      }
      that.loading = true
      const tgttodatestr = that.$myutils.getYYYYMMDDStr(that.wkdate)
      that.wkdate.setDate(that.wkdate.getDate() + 1 - LOAD_DATE_CNT)
      const tgtfromdatestr = that.$myutils.getYYYYMMDDStr(that.wkdate)

      const prmstr = 'tgt_date_to=' + tgttodatestr + '&tgt_date_from=' + tgtfromdatestr
      that.$axios.get(that.apienv.baseendpoint + 'slip?' + prmstr, config).then(
        response => {
          that.finload(that, response)
        }
      )
    },
    finload: function (that, response) {
      for (var resdata of response.data) {
        resdata['tgt_date_obj'] = that.$myutils.getLocalDate(resdata['tgt_date'])
        resdata['tgt_date_str'] = that.$myutils.getLocalDateStr(resdata['tgt_date'])
        resdata['kind_nm'] = that.$masterdata.getKindNm(resdata['kind_cd'])
        resdata['method_nm'] = that.$masterdata.getMethodNm(resdata['method_cd'])
        resdata['value_fmt'] = Number(resdata['value']).toLocaleString()
      }
      Array.prototype.push.apply(that.sliplist, response.data)
      that.wkdate.setDate(that.wkdate.getDate() - 1)
      that.loading = false
      that.loadeddatecnt += that.loaddatecnt
    },
    reload: function () {
      if (!this.loading) {
        this.loading = true
        this.wkdate = new Date(this.tgtdate)
        this.sliplist = []
        this.loadeddatecnt = 0
        this.loading = false
        this.loadMore()
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .listview .slip-item {
    align-items: center;
    margin: 10px;
    text-align: left;
    border-bottom: solid 1px #E0E0E0;
  }
  .listview .slip-header {
    align-items: center;
    margin: 10px;
    text-align: left;
    margin-bottom: 3px;
    font-weight: bold;
    font-color: #1f1f1f;
    border-bottom: solid 1px #202020;
  }
  .listviewform{
    padding : 20px;
  }

  .slip-w-icon {
    width: 40px;
    text-align: center;
  }
  .slip-w-mid {
    width: 120px;
    text-align: center;
  }
  .slip-w-num {
    width: 80px;
    text-align: right;
  }
  .slip-w-num-h {
    width: 80px;
    text-align: center;
  }
  .slip-val-elem {
    display: inline-block;
    vertical-align: top;
    height: 20px;
    margin: 5px;
  }
</style>