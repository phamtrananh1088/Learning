<template>
  <div>
    <div style="display:inline-flex; align-items: center;" :class="inputClass">
      <reafsinputtext :tabindex="tabindex" :width="'56px'" :disabled="disabled" :value="事務所コード" maxlength="3"
        @blur="blurEvent" @input="$emit('update:事務所コード', $event)" ref="事務所コード">
      </reafsinputtext>
      <reafsinputtext :tabindex="tabindex" :width="'80px'" :disabled="disabled" :value="営業所コード" maxlength="6"
        @blur="blurEvent" @input="$emit('update:営業所コード', $event)" ref="営業所コード"> </reafsinputtext>
      <reafsinputtext :tabindex="tabindex" :width="'56px'" :disabled="disabled" :value="部コード" maxlength="3"
        @blur="blurEvent" @input="$emit('update:部コード', $event)" ref="部コード"> </reafsinputtext>
      <reafsinputtext :tabindex="tabindex" :width="'56px'" :disabled="disabled" :value="課コード" maxlength="3"
        @blur="blurEvent" @input="$emit('update:課コード', $event)" ref="課コード"> </reafsinputtext>
      <reafsinputtext :tabindex="tabindex" :width="'64px'" :disabled="disabled" :value="係コード" maxlength="4"
        @blur="blurEvent" @input="$emit('update:係コード', $event)" ref="係コード">
      </reafsinputtext>
      <div :class="[appendBackground ? 'append-background' : '']">
        <el-button :tabindex="tabindex" icon="el-icon-search" class="is-search" @click="showDialog = true"
          v-if="!disabled"></el-button>
      </div>
    </div>
    <div :class="[appendBackground ? 'append-background' : '']" :style="{ width: appendlabelWidth }"
      style="display: block"><span :title="appendlabelText" class="append-label">
        {{ appendlabelText }}
      </span></div>
    <reafsmodal :dialogVisible.sync="showDialog" @backData="dataBack" :subParamsObj="subParamsObj"></reafsmodal>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsmodal from '@/views/Reafs-W/search/WS00060/Modal_SearchsSoshiki2.vue'
export default {
  components: {
    reafsinputtext,
    reafsmodal
  },
  name: 'shinseiBusho',
  props: {
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
    appendBackground: {
      type: Boolean,
      default: true
    },
    modelValue: String,
    title: {
      type: String
    },
    事務所コード: String,
    営業所コード: String,
    部コード: String,
    課コード: String,
    係コード: String,
    tabindex: String,
    subParamsObj: {
      type: Object,
      default () {
        return {
          taisyou: '1', // 承認種類取引先検索：対象
          jigyousyoCd: '' // 事業所コード
        }
      }
    },
    appendlabelWidth: {
      type: String,
      default: '314px'
    }
  },
  $emit: ['update:事務所コード', 'update:営業所コード', 'update:部コード', 'update:課コード', 'update:係コード'],
  data () {
    return {
      showDialog: false
    }
  },
  methods: {
    // サブ画面から戻すデータ
    dataBack (obj) {
      obj.事務所コード = obj.row.事務所コード
      obj.営業所コード = obj.row.営業所コード
      obj.部コード = obj.row.部コード
      obj.課コード = obj.row.課コード
      obj.係コード = obj.row.係コード
      obj.部署名 = obj.row.部門名
      this.$emit('backData', obj)
    },
    blurEvent (event) {
      if (event.relatedTarget == this.$refs.事務所コード.$el.children[0].children[0] ||
      event.relatedTarget == this.$refs.営業所コード.$el.children[0].children[0] ||
      event.relatedTarget == this.$refs.部コード.$el.children[0].children[0] ||
      event.relatedTarget == this.$refs.課コード.$el.children[0].children[0] ||
      event.relatedTarget == this.$refs.係コード.$el.children[0].children[0]) {
        return
      }
      if (
        !this.$props.事務所コード ||
        !this.$props.営業所コード ||
        !this.$props.部コード ||
        !this.$props.課コード ||
        !this.$props.係コード
      ) {
        this.$emit('backData', {
          事務所コード: !this.$props.事務所コード ? '' : this.$props.事務所コード.trim(),
          営業所コード: !this.$props.営業所コード == null ? '' : this.$props.営業所コード.trim(),
          部コード: !this.$props.部コード == null ? '' : this.$props.部コード.trim(),
          課コード: !this.$props.課コード == null ? '' : this.$props.課コード.trim(),
          係コード: !this.$props.係コード == null ? '' : this.$props.係コード.trim(),
          部署名: '',
          event
        })
      } else {
        this.http
          .get('/api/Reafs_W/Shonin/WD00510/GetDataN004', {
            申請部署コード: this.$props.事務所コード,
            申請部署コード2: this.$props.営業所コード,
            申請部署コード3: this.$props.部コード,
            申請部署コード4: this.$props.課コード,
            申請部署コード5: this.$props.係コード
          })
          .then((response) => {
            this.$emit('backData', {
              事務所コード: this.$props.事務所コード,
              営業所コード: this.$props.営業所コード,
              部コード: this.$props.部コード,
              課コード: this.$props.課コード,
              係コード: this.$props.係コード,
              部署名: response.data,
              event
            })
          })
      }
    },
    focus () {
      this.eigyotenCdFocus()
    },
    eigyotenCdFocus () {
      this.$nextTick(() => {
        this.$refs.事務所コード.$el.children[0].children[0].focus()
      })
    }
    // onKeypress (evt) {
    //   var theEvent = evt || window.event
    //   var key = theEvent.keyCode || theEvent.which
    //   key = String.fromCharCode(key)
    //   var regex = /[-\d\.]/ // dowolna liczba (+- ,.) :)
    //   var objRegex = /^-?\d*[\.]?\d*$/
    //   var val = this.$refs.事務所コード.$attrs.value
    //   if (!regex.test(key) || !objRegex.test(val + key) ||
    //         !theEvent.keyCode == 46 || !theEvent.keyCode == 8) {
    //     theEvent.returnValue = false
    //     if (theEvent.preventDefault) theEvent.preventDefault()
    //   };
    //   const textbox = this.$refs.事務所コード.$children[0].$el.children[0]
    //   if ((textbox.selectionEnd - textbox.selectionStart) == 0 &&
    //   (val + key).length <= 3 &&
    //   (Number(val + key) < 0 || Number(val + key) > 199)) {
    //     theEvent.returnValue = false
    //     if (theEvent.preventDefault) theEvent.preventDefault()
    //   }
    // }
  }
}
</script>

<style scoped>
.append-label {
  padding: 0 15px;
  color: black;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 30px;
  line-height: 30px;
  width: 100% !important;
}

.append-label span {
  width: 100% !important;
}

.append-background {
  display: inline-flex;
  background-color: rgb(211, 211, 211);
  /* width: 206px; */
}

.el-button.is-search {
  padding: 0 12px 0 12px;
  height: 29px;
}
</style>
