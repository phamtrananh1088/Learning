<template>
  <div>
    <el-input
      :tabindex="tabindex"
      :style="{width}"
      :required="required"
      :disabled="disabled"
      :class="[required ? 'reafs-input-required' : 'reafs-input']"
      ref="elinput"
      @blur="handleBlur"
      v-on="$listeners"
      v-bind="$attrs"
      :maxlength="cusMaxlength"
      :type="type"
      :rows="rows"
      :title="type == 'textarea' ? '' : undefined"
      @keypress.native="onKeypress($event)"
      >
    </el-input>
    <slot name="append"></slot>
  </div>
</template>
<script>
export default {
  name: 'reafsinputtext',
  props: {
    width: {
      type: String,
      default: '180px'
    },
    required: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: 'text'
    },
    rows: {
      type: Number,
      default: 0
    },
    rules: Object,
    tabindex: String
  },
  computed: {
    inDbClick () {
      if (this.clickTime === '') {
        return false
      }
      const millis = Date.now() - this.clickTime
      return millis < 260
    },
    // ハノイ側修正2023/05/10　課題管理表№337：「改行コードをWindows用のコードに変換した上で文字数チェックを行う入力コントロール開発依頼」
    cusMaxlength () {
      if (this.$attrs.value &&
      this.type == 'textarea' &&
      (this.$attrs.value.match(/\n/g) || []).length > 0) {
        const maxLen = this.$attrs.maxlength - (this.$attrs.value.match(/\n/g) || []).length
        return maxLen
      } else {
        return Number(this.$attrs.maxlength)
      }
    }
  },
  data () {
    return {
      validvalue: '',
      clickTime: '',
      preventDbClick: false
    }
  },
  mounted () {
    this.$refs.elinput.$el.children[0].addEventListener('click', this.onclick)
  },
  beforeUnmount () {
    this.$refs.elinput.$el.children[0].removeEventListener('click', this.onclick)
  },
  methods: {
    setPreventDbClick () {
      this.preventDbClick = true
    },
    onKeypress (evt) {
      var theEvent = evt || window.event

      const idx = theEvent.target.selectionStart
      const idxEnd = theEvent.target.selectionEnd
      let valueAfterAdd = theEvent.target.value
      if (idx != idxEnd) {
        valueAfterAdd = valueAfterAdd.substring(0, idx) + valueAfterAdd.substring(idxEnd, valueAfterAdd.length)
      }
      valueAfterAdd = valueAfterAdd.slice(0, idx) + valueAfterAdd.slice(idx + Math.abs(0))
      if (theEvent.keyCode == 13 && valueAfterAdd.length >= this.cusMaxlength - 1) {
        if (theEvent.preventDefault) theEvent.preventDefault()
      };
    },
    onclick (event) {
      if (this.clickTime === '') {
        this.clickTime = Date.now()
        this.$emit('click', event)
      } else {
        if (this.inDbClick) {
          this.clickTime = ''
          if (this.preventDbClick) {
            this.preventDbClick = false
            return
          }
          this.$emit('dbClick', event)
        } else {
          this.clickTime = Date.now()
          this.$emit('click', event)
        }
      }
    },
    handleBlur (event) {
      if (typeof this.rules === 'object') {
        const m = this.$attrs.value
        let pattern = ''
        if (typeof this.rules.type === 'string') {
          switch (this.rules.type) {
            case '半角英数字':
              pattern = /^[a-zA-Z1-9]*$/
              break
            default:
              break
          }
        } else {
          pattern = this.rules.pattern
        }
        if (pattern instanceof RegExp) {
          if (pattern.test(m)) {
            this.validvalue = m
          } else {
            this.rules.validator(this.validvalue)
          }
        }
      } else {
        var m = this.$attrs.value || ''
        let v = ''
        let maxlength = Number(this.cusMaxlength)
        if (isNaN(maxlength)) {
          maxlength = 0
        }
        if (maxlength > 0) {
          if (m.length > maxlength) {
            v = m.substring(0, maxlength)
            this.$emit('update:modelValue', v)
          }
        }
      }

      if (this.type == 'textarea' &&
      (this.$attrs.value.match(/\r/g) || []).length <= 0) {
        let maxlength = Number(this.$attrs.maxlength)
        var val = (this.$attrs.value || '').replace(/\n/g, '\r\n')
        if (val.length > maxlength) {
          val = val.substring(0, maxlength)
        }
        this.$emit('update:replaceValue', val)
      }
    },
    focus () {
      this.$refs.elinput.$el.children[0].focus()
    }
  }
}
</script>

<style scoped>
  .reafs-input >>> .el-input__inner {
    height: 30px;
    line-height: 30px;
    border-radius: 1px;
  }

  .reafs-input-required >>> .el-input__inner {
    height: 30px;
    line-height: 30px;
    border-radius: 1px;
    background: lightgoldenrodyellow;
  }

  .reafs-input-required >>> .el-textarea__inner {
    border-radius: 1px;
    background: lightgoldenrodyellow;
  }

  .el-button{
    padding-top: 6px;
    padding-bottom: 6px;
    margin-left: -4px;
    border-start-start-radius: 1px;
    border-end-start-radius: 1px;
  }

  .is-error .reafs-input-required >>> .el-textarea__inner {
    border-color: #F56C6C !important;
  }
</style>
