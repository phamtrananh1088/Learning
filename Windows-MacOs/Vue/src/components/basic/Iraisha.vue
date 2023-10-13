<template>
  <div>
    <reafsinputtext
      :tabindex="tabindex"
      :width="'88px'"
      :disabled="disabled"
      @blur="blurEvent"
      v-on="$listeners"
      v-bind="$attrs"
      ref="iraishaCd"
    >
      <template v-slot:append>
        <div :class="[appendBackground?'append-background':'']">
          <el-button :tabindex="tabindex" icon="el-icon-search" class="is-search" @click="showDialog = true" :disabled="disabled"></el-button>
          <div class="append-label" :style="{ width: appendlabelWidth }">{{appendlabelText}}</div>
        </div>
      </template>
    </reafsinputtext>

    <reafsmodal :dialogVisible.sync="showDialog" @backData="dataBack"></reafsmodal>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsmodal from '@/components/modal/Modal.vue'
export default {
  components: {
    reafsinputtext,
    reafsmodal
  },
  name: 'iraisha',
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
    appendlabelText: {
      type: String,
      default: ''
    },
    appendlabelWidth: {
      type: String,
      default: ''
    },
    appendBackground: {
      type: Boolean,
      default: true
    },
    tabindex: String
  },
  data () {
    return {
      showDialog: false
    }
  },
  methods: {
    // サブ画面から戻すデータ
    dataBack (obj) {
      this.$emit('backData', obj)
    },
    blurEvent () {
      if (this.$attrs.value == '' || this.$attrs.value.trim() == '') {
        this.$emit('backData', {torihikisakicd: '', torihikisakinm: ''})
      } else {
        this.$emit('backData', {torihikisakicd: this.$attrs.value, torihikisakinm: ''})
      }
    },
    focus () {
      this.$nextTick(() => {
        this.$refs.iraishaCd.$el.children[0].children[0].focus()
      })
    }
  }
}
</script>

<style scoped>
.append-label{
  display: inline-block;
  padding: 0 15px;
  height: 30px;
  color: black;
  overflow-y: hidden;
}
.append-background{
  display: inline-flex;
  background-color: rgb(211, 211, 211);
  width: 218px;
}
.el-button.is-search {
    padding: 0 12px 0 12px;
}
</style>
