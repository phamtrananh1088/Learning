<template>
  <el-dialog :visible="dialogVisible" :show-close="false" :class="dialogClass" class="screenPopup dialog-class"
    @close="onClose()" @open="onOpen()" :before-close="handleClose" :close-on-click-modal="false">
    <iframe scrolling="no" frameborder="0" style="border:none;overflow:hidden;" ref="iframe"></iframe>
  </el-dialog>
</template>
<script>

export default {
  components: {
  },
  props: {
    params: {
      type: Object,
      default: {}
    },
    dialogVisible: {
      type: Boolean,
      default: false
    },
    src: {
      type: String,
      default: ''
    },
    isCloseConfirm: {
      type: Boolean,
      default: false
    },
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
      if (this.iFrameSrc && this.iFrameSrc.includes('IraiNaiyoSabu')) {
        return 'IraiNaiyoSabu'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('KakoMitsumoriSanshoSabu')) {
        return 'KakoMitsumoriSanshoSabu'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('BukkenDokyumentoSanshoSabu')) {
        return 'BukkenDokyumentoSanshoSabu'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('ZumenShiryoTenpuSabu')) {
        return 'ZumenShiryoTenpuSabu'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('ShashinTenpuSabu')) {
        return 'ShashinTenpuSabu'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('MitsumoriDetaTorikomi')) {
        return 'MitsumoriDetaTorikomi'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('TeikiMitsumoriSyosaiNyuryoku')) {
        return 'TeikiMitsumoriSyosaiNyuryoku'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('TeikiMitsumoriIraiSansho')) {
        return 'TeikiMitsumoriIraiSansho'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('PasuwadoRimainda')) {
        return 'PasuwadoRimainda'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('TeikiMitsumoriDataTorikomi')) {
        return 'TeikiMitsumoriDataTorikomi'
      }
      if (this.iFrameSrc && this.iFrameSrc.includes('TeikiSeikyuSubSub')) {
        return 'TeikiSeikyuSubSub'
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
      if (me.dialogVisible === true) {
        var data = event.data
        if (data.call === 'logout') {
          window.alreadyLogout = true
          document.getElementById('btnLogout').click()
        }

        if (data.call === 'closePopup') {
          if (me && me.$emit) {
            if (await me.confirmClose()) {
              me.$emit('onBackData', event.data)
              me.$emit('update:dialogVisible', false)
            }
          }
        } else if ((data.call === 'chcfmFlg')) {
          me.cfmFlg = data.n
        }
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
      console.log('閉じますかー？')
      if (this.cfmFlg) {
        //       if (this.isCloseConfirm) {
        const cfm = await this.confirm((await this.getMsg(this.messageCodeCloseCfm)).data)
        return cfm
      } else {
        return true
      }
    },
    async handleClose (done) {
      console.log('閉じるよー')
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
.PasuwadoRimainda iframe {
  height: 100%;
}
</style>
<style>
.screenPopup .el-dialog__header {
  display: none;
}

.screenPopup .el-dialog__body {
  height: 100%;
}

.IraiNaiyoSabu .el-dialog {
  width: 89%;
  margin-top: 4vh !important;
  height: 90%;
  overflow: auto;
}

.MitsumoriDetaTorikomi .el-dialog {
  width: 80%;
  margin-top: 4vh !important;
  height: 95%;
  overflow: auto;
}

.KakoMitsumoriSanshoSabu .el-dialog {
  width: 92%;
  margin-top: 4vh !important;
  height: 95%;
  overflow: auto;
}

.BukkenDokyumentoSanshoSabu .el-dialog {
  width: 75%;
  margin-top: 6vh !important;
  height: 694px;
  overflow: auto;
}

.MitsumoriDetaTorikomi iframe, .BukkenDokyumentoSanshoSabu iframe {
  width: 100%;
  height: 100% !important;
}

.BukkenDokyumentoSanshoSabu .el-dialog__body {
  padding-bottom: 5px !important;
}

.ZumenShiryoTenpuSabu .el-dialog {
  width: 85%;
  min-width: 1080px;
  margin-top: 2vh !important;
  overflow: auto;
  height: 97%;
}

.ShashinTenpuSabu .el-dialog {
  width: 89%;
  margin-top: 2vh !important;
  height: 97%;
  overflow: auto;
}

.TeikiMitsumoriSyosaiNyuryoku .el-dialog {
  width: 89%;
  margin-top: 2vh !important;
  height: 96%;
  overflow: auto;
}

.TeikiMitsumoriIraiSansho .el-dialog {
  width: 89%;
  margin-top: 6vh !important;
  height: 60%;
  overflow: hidden;
}

.PasuwadoRimainda .el-dialog {
  width: 650px;
  margin-top: 2vh !important;
  height: 690px;
  overflow: auto;
}

.TeikiMitsumoriDataTorikomi .el-dialog {
  width: 80%;
  margin-top: 4vh !important;
  height: 95%;
  overflow: auto;
}
.TeikiSeikyuSubSub .el-dialog {
  width: 60%;
  margin-top: 5% !important;
  height: 80%;
  overflow: auto;
}

</style>
