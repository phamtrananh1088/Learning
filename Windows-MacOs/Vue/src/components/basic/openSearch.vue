<template>
  <div>
    <reafsinputtext
      style="display:flex; align-items: center;"
      :class="inputClass"
      ref="input"
      :maxlength="maxlength"
      :width="width"
      :required="required"
      :disabled="disabled"
      :errorMessageCode="errorMessageCode"
      v-on="$listeners"
      v-bind="$attrs"
      @change="blurEvent"
    >
      <template v-slot:append>
        <div :style="{ width: appendlabelWidth }" :class="[appendBackground ? 'append-background' : '']">
          <el-button
            icon="el-icon-search"
            class="is-search"
            @click="showDialog = true"
            v-if="!disabled"
          ></el-button>
          <span :title="appendlabelText" class="append-label">{{appendlabelText}}</span>
        </div>
        <span :title="appendlabelText" class="mobile-append-label">{{appendlabelText}}</span>
      </template>
    </reafsinputtext>

    <component
      :is="currentView"
      :dialogVisible.sync="showDialog"
      :subParamsObj="subParamsObj"
      @backData="dataBack"
    ></component>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsmodal from '@/components/modal/Modal.vue'
import reafsmodalSearchTanto from '@/components/modal/Modal_SearchTanto.vue'
import reafsmodalSearchBukken from '@/views/Reafs-W/search/WS00020/Modal_SearchBukken.vue'
import reafsmodalSearchMise from '@/views/Reafs-W/search/WS00050/Modal_SearchMise.vue'
import reafsmodalSearchShain from '@/views/Reafs-W/search/WS00040/Modal_SearchShain.vue'
import reafsmodalSearchKubun from '@/views/Reafs-W/search/WS00080/Modal_SearchKubun.vue'
import reafsmodalSearchTorihikisaki from '@/views/Reafs-W/search/WS00030/Modal_SearchTorihikisaki.vue'
import reafsmodalSearchTokuisaki from '@/views/Reafs-W/search/WS00030/Modal_SearchTorihikisaki.vue'
import reafsmodalSearchSoshiki2 from '@/views/Reafs-W/search/WS00060/Modal_SearchsSoshiki2.vue'
import reafsmodalSearchShoninSha from '@/views/Reafs-W/search/WS00040/Modal_SearchShain.vue'
import reafsmodalSearchHiyoFutanBumonSabu from '@/views/Reafs-W/search/WS00520/Modal_SearchHiyoFutanBumonSabu.vue'

export default {
  components: {
    reafsinputtext,
    reafsmodal,
    reafsmodalSearchTanto,
    reafsmodalSearchBukken,
    reafsmodalSearchMise,
    reafsmodalSearchShain,
    reafsmodalSearchKubun,
    reafsmodalSearchTorihikisaki,
    reafsmodalSearchTokuisaki,
    reafsmodalSearchSoshiki2,
    reafsmodalSearchShoninSha,
    reafsmodalSearchHiyoFutanBumonSabu
  },
  name: 'openSearch',
  props: {
    inputClass: {
      type: String,
      default: ''
    },
    customValidate: {
      type: Number,
      default: 1
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
      default: '206px'
    },
    path: {
      type: String,
      default: ''
    },
    appendBackground: {
      type: Boolean,
      default: true
    },
    maxlength: {
      type: String,
      default: '4'
    },
    width: {
      type: String,
      default: '100px'
    },
    errorMessageCode: {
      // サブ画面エラーメッセージコード
      type: String,
      default: 'E000014' // '該当データ無し'
    },

    subParamsObj: {
      taisyou: { type: String }, // 承認種類　 　　　　 取引先検索：対象
      jigyousyoCd: { type: String } // 事業所コード
    }
  },
  data () {
    return {
      showDialog: false,
      urlList: [
        // 店検索
        {
          path: 'reafsmodalSearchMise',
          url: 'api/Reafs_W/Search/searchMise',
          dbkeyCd: '店コード_戻る',
          dbKeyNm: '店名称'
        },
        // 取引検索
        {
          path: 'reafsmodalSearchTorihikisaki',
          url: 'api/Reafs_W/Search/searchTorihikisaki',
          dbkeyCd: 'コード',
          dbKeyNm: '名'
        },
        // 得意先検索
        {
          path: 'reafsmodalSearchTokuisaki',
          url: 'api/Reafs_W/Search/searchTorihikisaki',
          dbkeyCd: 'コード',
          dbKeyNm: '名'
        },
        {
          path: 'reafsmodalSearchBukken',
          url: 'api/Reafs_W/Search/searchBukken',
          dbkeyCd: '物件コード',
          dbKeyNm: '物件名'
        },
        {
          path: 'reafsmodalSearchShain',
          url: 'api/Reafs_W/Search/searchShain',
          dbkeyCd: '社員コード',
          dbKeyNm: '社員名'
        },
        {
          path: 'reafsmodalSearchKubun',
          url: 'api/Reafs_W/Search/searchKubun',
          dbkeyCd: '大中小区分コード_戻る',
          dbKeyNm: '大中小区分名'
        },
        // 組織サブ2
        {
          path: 'reafsmodalSearchSoshiki2',
          url: 'api/Reafs_W/Search/getSoshikiItems'
        },
        {
          path: 'reafsmodalSearchShoninSha',
          url: 'api/Reafs_W/Search/searchShain',
          dbkeyCd: '社員コード',
          dbKeyNm: '社員名'
        },
        {
          path: 'reafsmodalSearchHiyoFutanBumonSabu',
          url: 'api/Reafs_W/Search/searchHiyoFutanBumonSabu',
          dbkeyCd: '名称コード',
          dbKeyNm: 'コード長名'
        }
      ],
      searchparam: {
        param1: '',
        param2: '',
        param3: '',
        param4: '',
        param5: '',

        param9: '',
        param10: '' // 小画面を開かずに検索フラグ 1:入力検索、0:サブ画面の検索
      }
    }
  },
  computed: {
    currentView (obj) {
      return obj.path
    }
  },
  mounted () {
    this.blurEvent();
  },
  methods: {
    // サブ画面から戻すデータ
    dataBack (obj) {
      this.$emit('backData', obj)
    },
    blurEvent () {
      if (
        this.$attrs.value == null ||
        this.$attrs.value == '' ||
        this.$attrs.value.trim() == ''
      ) {
        this.$emit('backData', { txtCd: '', lblNm: '', row: {} })
      } else {
        if (this.customValidate != 2) {
          let keyCd = this.$attrs.value
          let keyNm
          let dataFlg = false
          let urlTemp = ''

          let dbkeyCd = ''
          let dbKeyNm = ''

          let currRowDate = {}

          if (
            this.urlList.filter((i) => {
              return i.path == this.path
            }).length > 0 &&
          this.urlList.filter((i) => {
            return i.path == this.path
          })[0].url
          ) {
            let obj = this.urlList.filter((i) => {
              return i.path == this.path
            })[0]
            urlTemp = obj.url
            dbkeyCd = obj.dbkeyCd
            dbKeyNm = obj.dbKeyNm
          } else {
            return
          }
          this.searchparam.param1 = keyCd
          this.searchparam.param2 = ''
          this.searchparam.param3 = ''
          // 負担区分
          this.searchparam.param4 = this.subParamsObj
            ? this.subParamsObj.taisyou
            : ''

          // 小画面を開かずに検索フラグ
          // コード入力検索
          this.searchparam.param10 = '1'

          var qs = require('qs')
          this.http
            .post(urlTemp, qs.stringify(this.searchparam))
            .then((result) => {
              if (!result.status) {
              // return this.$message.error(result.message);
                this.$emit('backData', {
                  txtCd: keyCd,
                  lblNm: '',
                  row: {}
                })

                // return this.MsgErr({
                //   title: this.$store.getters.getPageTitle(),
                //   message: result.message
                // }, () => { this.focus() })
                return this.getSubMsg()
              }

              this.tableData = result.data
              if (this.tableData && this.tableData.length > 0) {
                for (let index = 0; index < this.tableData.length; index++) {
                  if (keyCd == this.tableData[index][dbkeyCd]) {
                    keyNm = this.tableData[index][dbKeyNm]
                    dataFlg = true
                    currRowDate = this.tableData[index]
                    break
                  }
                }
              }

              if (!dataFlg) {
                keyCd = ''
                keyNm = ''
                currRowDate = {}
              // alert("未登録の" + dbkeyCd + "が入力されました。");
              }
              this.$emit('backData', {
                txtCd: keyCd,
                lblNm: keyNm,
                row: currRowDate
              })
            })
        } else {
          this.$emit('backData', {
            txtCd: this.$attrs.value,
            lblNm: '',
            row: {}
          })
        }
      }
    },
    focus () {
      this.$nextTick(() => {
        this.$refs.input.$el.children[0].children[0].focus()
      })
    },
    getSubMsg () {
      this.commonFunctionUI.getMsg(this.errorMessageCode).then((response) => {
        this.MsgInfo({
          title: this.$store.getters.getPageTitle(),
          message: response.data
        })
      })
    }
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
  border: 1px solid #646464;
  border-left: none;
  width: 100%;
}
.el-button.is-search + .append-label{
  margin-left: -3px;
}
.mobile-append-label {
  padding: 0 15px;
  color: black;
  display: none;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 30px;
  line-height: 30px;
}
.append-background {
  display: inline-flex;
  background-color: rgb(211, 211, 211);
  /*width: 500px;*/
}

@media screen and (max-width: 420px) {
  .moblie-input-append {
    display: block !important;
  }

  .moblie-input-append .append-background {
    display: inline-table;
    background-color: unset;
    margin-left: -2px;
  }

  .moblie-input-append .append-label{
    display: none;
  }
  .moblie-input-append .mobile-append-label {
    display: -webkit-box;
    background-color: rgb(211, 211, 211);
  }
}

</style>
