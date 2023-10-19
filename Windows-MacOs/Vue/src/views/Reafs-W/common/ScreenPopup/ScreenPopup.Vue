<template>
  <el-dialog :visible="dialogVisible"
  :show-close="false" :class="dialogClass"
  class="screenPopup dialog-class"
   @close="onClose()"
   @open="onOpen()"
   :before-close="handleClose"
   :close-on-click-modal="false"
   >
     <iframe
      scrolling="no"
      frameborder="0"
      style="border:none;overflow:hidden;"
      ref="iframe"
      ></iframe>
  </el-dialog>
</template>
<script>

export default {
  components: {
  },
  props: {
    params: {
      type: Object,
      default: () => {
        return {}
      }
    },
    dialogVisible: {
      type: Boolean,
      default: false
    },
    src: {
      type: String,
      default: ''
    },
    // 閉じる際にconfirmメッセージを含める場合:true
    isCloseConfirm: {
      type: Boolean,
      default: false
    },
    // confirmメッセージのエラーコード
    messageCodeCloseCfm: {
      type: String,
      default: 'K000009'
    }
  },
  data () {
    return {
      iFrameSrc: '',
      cfmFlg: false 
    }
  },
  watch: {
  },
  computed: {
    dialogClass () {
      if (this.iFrameSrc && this.iFrameSrc.includes('IraiTenpuTouroku')) {
        return 'IraiTenpuTouroku'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('TyohyoIchiran')) {
        return 'TyohyoIchiran'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('HatchuShoUkesho')) {
        return 'HatchuShoUkesho'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('IraiSansyoSub')) {
        return 'IraiSansyoSub'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('TeikiShiharaiiraiSansyosub')) {
        return 'TeikiShiharaiiraiSansyosub'
      }

      return ''
    }
  },
  created () {
    const me = this
    if (window.addEventListener) {
      window.addEventListener('message', onMessage, false)
    } else if (window.attachEvent) {
      window.attachEvent('onmessage', onMessage, false)
    }

    async function onMessage (event) {
      var data = event.data
      if (data.call === 'closePopup') {
        if (me && me.$emit) {
          // 確認メッセージを追加
          // me.$emit('update:dialogVisible', false)
          if (await me.confirmClose()) {
            me.$emit('update:dialogVisible', false)
          }
        }
      } else if((data.call === 'chcfmFlg')) {
         me.cfmFlg = data.n
      }
    }
  },
  mounted () {
  },
  methods: {
    onClose () {
      this.$emit('update:dialogVisible', false)
      this.$emit('closePopupEvent', this)
      this.iFrameSrc = ''
    },
    async confirmClose () {
//      if (this.isCloseConfirm) {
    if (this.cfmFlg) {
        const cfm = await this.confirm((await this.getMsg(this.messageCodeCloseCfm)).data)
        return cfm
      } else {
        return true
      }
    },
    async handleClose (done) {
      if (await this.confirmClose()) {
        done()
      }
    },
    onOpen () {
      let queryParams = ''
      if (this.params) {
        for (var prop in this.params) {
          if (Object.prototype.hasOwnProperty.call(this.params, prop)) {
            queryParams += queryParams.includes('?') ? '&' : '?'
            queryParams += prop + '=' + this.params[prop]
          }
        }
      }
      this.iFrameSrc = this.http.resolve(this.src) + queryParams
      this.$nextTick(function () {
        this.$refs.iframe.contentWindow.location.replace(this.iFrameSrc)
      })
      this.cfmFlg = this.isCloseConfirm
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
    }
  }
}
</script>
<style scoped>
  .screenPopup iframe {
    width: 100%;
    height: 92vh;
  }
</style>
<style>
  .screenPopup .el-dialog__header {
    display: none;
  }
  .screenPopup .el-dialog__body {
    height: 100%;
  }

  .IraiTenpuTouroku .el-dialog{
    width: 85%;
    margin-top: 6vh !important;
    overflow: auto;
  }
  .TyohyoIchiran .el-dialog{
    width: 690px;
    margin-top: 20px !important;
    max-Height: 740px !important;
    Height: 96% !important;
    /* overflow: auto; */
    overflow-y: overlay;
  }
  /* .IraiSansyoSub .el-dialog{
    width: 1600px;
    margin-top: 20px !important;
    max-Height: 740px !important;
    Height: 96% !important;
    overflow-y: overlay;
  } */
  .IraiSansyoSub .el-dialog {
    width: 95%;
    margin-top: 4vh !important;
    height: 95%;
    overflow: auto;
  }
  .TeikiShiharaiiraiSansyosub .el-dialog {
    width: 75%;
    margin-top: 4vh !important;
    height: 95%;
    overflow: auto;
  }
  /* ハノイ側修正2022/11/08　STEP2_W　課題管理表№205：設計書「不具合管理表」シートの「2022/11/07仕様変更分」「レイアウト調整依頼」START */
  .TyohyoIchiran .el-dialog::-webkit-scrollbar {
    background-color: rgba(0, 0, 0, 0.1);
    width: 10px;
    height: 10px;
  }
  .TyohyoIchiran .el-dialog::-webkit-scrollbar-thumb {
    background-color: rgba(82, 81, 81, 0.5);
    width: 10px;
    border-radius: 5px;
  }
  .TyohyoIchiran .el-dialog::-webkit-scrollbar-thumb:hover {
    background-color: rgba(0, 0, 0, 0.5);
  }
   /* ハノイ側修正2022/11/08　STEP2_W　課題管理表№205：設計書「不具合管理表」シートの「2022/11/07仕様変更分」「レイアウト調整依頼」END */
  .TyohyoIchiran iframe {
    width: 100%;
    height: 100% !important;
    min-height: 690px;
    min-width: 630px;
  }
  .TyohyoIchiran {
    overflow: hidden !important;
  }
  .TyohyoIchiran .el-dialog__body {
    padding-top:10px !important;
    height: 740px !important;
  }

  .HatchuShoUkesho .el-dialog{
    width: 37%;
    margin-top: 6vh !important;
    height: 580px;
    /* overflow: none; */
    overflow: auto;
  }

/* ハノイ側修正2022/11/08　STEP2_W　課題管理表№208：設計書「不具合管理表」シートの「2022/11/07仕様変更分」「レイアウト調整依頼」START */
  .HatchuShoUkesho .el-dialog::-webkit-scrollbar {
    background-color: rgba(0, 0, 0, 0.1);
    width: 10px;
    height: 10px;
  }
  .HatchuShoUkesho .el-dialog::-webkit-scrollbar-thumb {
    background-color: rgba(82, 81, 81, 0.5);
    width: 10px;
    border-radius: 5px;
  }
  .HatchuShoUkesho .el-dialog::-webkit-scrollbar-thumb:hover {
    background-color: rgba(0, 0, 0, 0.5);
  }
  .HatchuShoUkesho .el-dialog__body {
    padding-top:10px !important;
    min-width: 585px !important;
  }
/* ハノイ側修正2022/11/08　STEP2_W　課題管理表№208：設計書「不具合管理表」シートの「2022/11/07仕様変更分」「レイアウト調整依頼」START */

  .HatchuShoUkesho iframe {
    width: 100%;
    height: 100% !important;
    /* min-height: 520px;
    min-width: 500px; */
  }

  /* .HatchuShoUkesho .el-dialog__body {
    padding-top:10px !important;
  } */
</style>
