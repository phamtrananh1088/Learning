<template>
  <div>
    <div style="display:inline-flex; align-items: center;" :class="inputClass">
      <reafsinputtext
        :required="required"
        :tabindex="tabindex"
        :width="modeTextBox == 2 ? '100px' : '75px'"
        :disabled="disabled"
        :value="eigyotenCd"
        :maxlength="modeTextBox == 2 ? 7 : 4"
        @change="changeEvent"
        @input="$emit('update:eigyotenCd', $event)"
        ref="eigyotenCd"
      >
      </reafsinputtext>
      <reafsinputtext
      v-if="modeTextBox != 2"
        :required="required"
        :tabindex="tabindex"
        :width="'55px'"
        :disabled="disabled"
        :value="eigyotenBango"
        maxlength="2"
        @change="changeEvent"
        @input="$emit('update:eigyotenBango', $event)"
        ref="eigyotenBango"
      >
      </reafsinputtext>
      <div :style="{ width: appendlabelWidth }" :class="[appendBackground?'append-background':'']">
        <el-button :tabindex="tabindex" icon="el-icon-search" class="is-search" @click="showDialog = true" v-if="!disabled"></el-button>
        <span :title="appendlabelText" class="append-label">
          {{appendlabelText}}
        </span>
      </div>
    </div>
    <reafsmodal :dialogVisible.sync="showDialog" @backData="dataBack"></reafsmodal>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsmodal from '@/views/Reafs-W/search/WS00050/Modal_SearchMise.vue'
export default {
  components: {
    reafsinputtext,
    reafsmodal
  },
  name: 'eigyoten',
  props: {
    modeTextBox: {
      type: Number,
      default: 1
    },
    inputClass: {
      type: String,
      default: ''
    },
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
      default: '196px'
    },
    appendBackground: {
      type: Boolean,
      default: true
    },
    modelValue: String,
    title: {
      type: String
    },
    eigyotenCd: String,
    eigyotenBango: String,
    tabindex: String
  },
  $emit: ['update:eigyotenCd', 'update:eigyotenBango'],
  data () {
    return {
      showDialog: false
    }
  },
  methods: {
    // サブ画面から戻すデータ
    dataBack (obj) {
      if (this.modeTextBox == 2) {
        obj.eigyotenCd = obj.row.店コード + obj.row.店枝番
        obj.eigyotenNm = obj.lblNm
      } else {
        obj.eigyotenCd = obj.row.店コード
        obj.eigyotenBango = obj.row.店枝番
        obj.eigyotenNm = obj.lblNm
      }
      obj.event = obj.row.店枝番
      this.$emit('backData', obj)
    },

    changeEvent (event) {
      if (
        !this.$props.eigyotenCd ||
        (!this.$props.eigyotenBango && this.modeTextBox != 2)
      ) {
        this.$emit('backData', {
          eigyotenCd:
            this.$props.eigyotenCd == null ||
            this.$props.eigyotenCd == '' ||
            this.$props.eigyotenCd.trim() == ''
              ? ''
              : this.$props.eigyotenCd.trim(),
          eigyotenBango:
            this.$props.eigyotenBango == null ||
            this.$props.eigyotenBango == '' ||
            this.$props.eigyotenBango.trim() == ''
              ? ''
              : this.$props.eigyotenBango.trim(),
          eigyotenNm: '',
          event
        })
      } else {
        if (this.modeTextBox != 2) {
          this.http
            .get('/api/Reafs_W/Irai/WD00110/Get営業店名', {
              店コード: this.$props.eigyotenCd,
              店コード枝番: this.$props.eigyotenBango
            })
            .then((response) => {
              this.$emit('backData', {
                eigyotenCd: this.$props.eigyotenCd.trim(),
                eigyotenBango: this.$props.eigyotenBango.trim(),
                eigyotenNm: response.data,
                event
              })
            })
        } else {
          this.http
            .get('/api/Reafs_W/Shonin/WD00510/GetDataN002', {
              店コード: this.$props.eigyotenCd
            })
            .then((response) => {
              this.$emit('backData', {
                eigyotenCd: this.$props.eigyotenCd.trim(),
                eigyotenNm: response.data,
                event
              })
            })
        }
      }
    },
    focus () {
      this.eigyotenCdFocus()
    },
    eigyotenCdEl () {
      return this.$refs.eigyotenCd.$el.children[0].children[0]
    },
    eigyotenBangoEl () {
      return this.$refs.eigyotenBango.$el.children[0].children[0]
    },
    eigyotenCdFocus () {
      this.$nextTick(() => {
        this.$refs.eigyotenCd.$el.children[0].children[0].focus()
      })
    },
    eigyotenBangoFocus () {
      this.$nextTick(() => {
        this.$refs.eigyotenBango.$el.children[0].children[0].focus()
      })
    }
  }
}
</script>

<style scoped>
.append-label{
  padding: 0 15px;
  color: black;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 30px;
  line-height: 30px;
  border: 1px solid #646464;
  border-left: none;
  width: 100%
}
.el-button.is-search + .append-label{
  margin-left: -3px;
}
.append-background{
  display: inline-flex;
  background-color: rgb(211, 211, 211);
  /* width: 206px; */
}
.append-background button {
  margin-left: -4px;
}
.el-button.is-search {
    padding: 0 12px 0 12px;
}
</style>
