<template>
  <el-dialog :visible="dialogVisible"
  :show-close="false" :class="dialogClass"
  class="screenPopup dialog-class"
   @close="onClose()"
   @open="onOpen()"
   :close-on-click-modal="false"
   >
     <iframe
      :src="iFrameSrc"
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
      default: {}
    },
    dialogVisible: {
      type: Boolean,
      default: false
    },
    src: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      iFrameSrc: ''
    }
  },
  watch: {
  },
  computed: {
    dialogClass () {
      if (this.iFrameSrc && this.iFrameSrc.includes('IraiTenpuTouroku')) {
        return 'IraiTenpuTouroku'
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

    function onMessage (event) {
      var data = event.data
      if (data.call === 'closePopup') {
        if (me && me.$emit) {
          me.$emit('update:dialogVisible', false)
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
</style>
