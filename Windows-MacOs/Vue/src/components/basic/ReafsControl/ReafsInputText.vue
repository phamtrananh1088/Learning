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
        let maxlength = Number(this.$attrs.maxlength)
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

  .el-button{
    padding-top: 6px;
    padding-bottom: 6px;
    margin-left: -4px;
    border-start-start-radius: 1px;
    border-end-start-radius: 1px;
  }
</style>
