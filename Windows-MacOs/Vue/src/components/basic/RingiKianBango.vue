<template>
  <div>
    <el-input
      :style="{ width }"
      :required="required"
      :class="[required ? 'reafs-input-required' : 'reafs-input']"
      @blur="bluring"
      v-on="$listeners"
      v-bind="$attrs"
      @keypress.native="onKeypress($event)"
      @input="onInput"
      @focus="onFocus"
      :maxlength="maxlength"
      ref="inputNumber"
      :disabled="disabled"
    />
    <slot name="append"></slot>
  </div>
</template>
<script>
export default {
  name: 'reafsinputnumber',
  props: {
    width: {
      type: String,
      default: '180px'
    },
    required: {
      type: Boolean,
      default: false
    },
    // 整数の長さ
    intLength: {
      type: Number,
      default: 4
    },
    // 小数の長さ
    floatLength: {
      type: Number,
      default: 2
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
    }
  },
  mounted () {
  },
  computed: {
    maxlength () {
      return this.intLength + this.floatLength + (this.floatLength > 0 ? 1 : 0)
    }
  },
  watch: {
    '$attrs.value': function (oldVal, newVal) {
      if (document.activeElement != this.$refs.inputNumber.$el.children[0]) {
        this.bluring()
      }
    }
  },
  methods: {
    onInput (e) {
      this.$emit('input', e)
    },
    onFocus () {
      if (this.$attrs.value) {
        this.$emit('input', (this.$attrs.value + '').replaceAll('-', ''))
      }
    },
    bluring (evn) {
      // カーソルが離れたら数値をフォーマットする
      const valFormat = this.number_format(
        this.$attrs.value,
        this.floatLength,
        '.',
        ','
      )
      this.$emit('input', valFormat)
    },
    onKeypress (evt) {
      var theEvent = evt || window.event
      var key = theEvent.keyCode || theEvent.which
      key = String.fromCharCode(key)
      var regex = /[-\d\.]/ // dowolna liczba (+- ,.) :)
      var objRegex = /^-?\d*[\.]?\d*$/
      var val = this.$attrs.value
      if (!regex.test(key) || !objRegex.test(val + key) ||
            !theEvent.keyCode == 46 || !theEvent.keyCode == 8) {
        theEvent.returnValue = false
        if (theEvent.preventDefault) theEvent.preventDefault()
      };
    },
    // フォーマット化処理:[,]追加、フォーマット再処理
    number_format (number, decimals, dec_point, thousands_sep) {
      number = (number + '').replace(/[^0-9+-.]|,/g, '')
      if (number == '') {
        return number
      }
      if (number.length - number.replace(/\./g, '').length > 1) {
        let sTemp = number.split('.')
        number =
          sTemp[0] +
          '.' +
          number.substring(number.indexOf('.') + 1).replace(/\./g, '')
      }
      if (number.length - number.replace(/\-/g, '').length > 1) {
        number = number.substring(0).replace(/\-/g, '')
      }

      var s = number
      if (number.length == 9) {
        s = number.slice(0, 2) + "-" + number.slice(2, 4) + "-" + number.slice(4, 9)
      }
      if (number.length == 15) {
        s = number.slice(0, 3) + "-" + number.slice(3, 7) + "-" + number.slice(7, 9) + "-" + number.slice(9)
      }
      return s
    },
    focus () {
      this.$refs.inputNumber.focus()
    }
  }
}
</script>

<style scoped>
.reafs-input >>> .el-input__inner {
  height: 30px;
  line-height: 30px;
  border-radius: 1px;
  text-align: left;
}

.reafs-input-required >>> .el-input__inner {
  height: 30px;
  line-height: 30px;
  border-radius: 1px;
  background: lightgoldenrodyellow;
  text-align: left;
}
.el-button {
  padding-top: 7px;
  padding-bottom: 7px;
  margin-left: -4px;
  border-start-start-radius: 1px;
  border-end-start-radius: 1px;
}
</style>
