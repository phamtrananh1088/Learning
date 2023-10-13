<template>
  <div class="home-container OshiraseHyouji">
    <el-form
      :model="oshiraseHyoji"
      ref="refoshiraseHyoji"
      label-width="120px"
      class="sample-form-class"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header"> </el-header>
        <el-main class="home-el-main">
          <el-col :span="13">
            <el-form-item
              prop="kenmei"
              class="is-readonly oshiraseHyoji_control"
              label-width="0px"
            >
              <div style="display: flex">
                <label class="el-form-item__label item_label_control"
                  >件名</label
                >
                <label
                  class="el-form-item__label item_label_oshiraseHyoji_Kenmei"
                >
                  {{ oshiraseHyoji.件名 }}
                </label>
                <!-- <reafsinputtext
                  v-model="oshiraseHyoji.件名"
                  maxlength="40"
                  disabled
                  :width="'100%'"
                  class="item_input_oshiraseHyoji"
                >
                </reafsinputtext> -->
              </div>
            </el-form-item>
            <el-row class="no-margin-bottom">
            <el-col class="fix-width">
              <el-form-item
                prop="keisaibi"
                class="is-readonly oshiraseHyoji_control dtKeisaibi"
                label-width="0px"
              >
                <div style="display: flex">
                  <label class="el-form-item__label item_label_control"
                    >掲載日</label
                  >
                  <label
                    class="
                      el-form-item__label
                      item_label_oshiraseHyoji_KeisaiBi
                    "
                  >
                    {{ oshiraseHyoji.掲載日 }}
                  </label>
                  <!-- <reafsinputtext
                  v-model="oshiraseHyoji.掲載日"
                  maxlength="10"
                  disabled
                  :width="'150px'"
                  style="text-align: center"
                >
                </reafsinputtext> -->
                </div>
              </el-form-item>
               </el-col>
               <el-col class="remain-width">
              <el-form-item
                prop="torokuSoshiki"
                class="is-readonly oshiraseHyoji_control"
                label-width="0px"
              >
                <div style="display: flex">
                  <label class="el-form-item__label item_label_control"
                    >登録組織</label
                  >
                  <label
                    class="
                      el-form-item__label
                      item_label_oshiraseHyoji_TorokuSoshiki
                    "
                  >
                    {{ oshiraseHyoji.登録組織 }}
                  </label>
                </div>
              </el-form-item>
            </el-col>
            </el-row>
            <el-form-item
              prop="tenpu"
              class="is-readonly oshiraseHyoji_control"
              label-width="0px"
            >
              <div style="display: flex">
                <label class="el-form-item__label item_label_control"
                  >添付</label
                >
                <label class="el-form-item__label item_label_oshiraseHyoji">
                  <a
                    style="padding-left: 15px"
                    ref="privacyPolicy"
                    href="#"
                    @click.prevent="onDownloadFile"
                    >{{ oshiraseHyoji.添付ファイル名 }}</a
                  >
                </label>
              </div>
            </el-form-item>
            <el-form-item class="is-readonly" prop="naiyo" label-width="0px">
              <div style="display: flex">
                <label
                  class="el-form-item__label item_label_oshiraseHyoji_tenpu"
                  >内容</label
                >
                <!-- <el-input
                  tabindex="27"
                  class="item_input_oshiraseHyoji_tenpu"
                  maxlength="400"
                  type="label"
                  resize="none"
                  v-model="oshiraseHyoji.内容"
                  disabled
                  ref="oshiraseHyoji_tenpu"
                >
                </el-input> -->
                <label
                  class="el-form-item__label item_input_oshiraseHyoji_tenpu"
                >
                  {{ oshiraseHyoji.内容 }}
                </label>
              </div>
            </el-form-item>
          </el-col>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="7">
              <p />
            </el-col>
            <el-col :span="7">
              <p />
            </el-col>
            <el-col :span="2">
              <el-button @click="onClickBack">戻る </el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'

export default {
  components: {
    reafsinputtext
  },
  data () {
    return {
      queryParams: {
        掲示ID: ''
      },
      loading: false,
      oshiraseHyoji: {
        件名: '',
        掲載日: '',
        登録組織: '',
        内容: '',
        添付: '',
        添付ファイル名: '',
        掲示ID: ''
      }
    }
  },

  created () {
    this.queryParams = {
      掲示ID: this.$route.params.KEIJI_ID
    }
    this.init()
  },

  mounted () {
  },
  watch: {
    $route(to, from) {
      if (to.name == "TD00002") {
        this.queryParams.掲示ID = to.params.KEIJI_ID
        this.init()
      }
    },
  },

  methods: {
    init () {
      this.oshiraseHyoji.掲示ID = this.queryParams.掲示ID
      this.http
        .get('/api/Reafs_T/OshiraseHyoji/GetNoti', this.oshiraseHyoji)
        .then((response) => {
          this.oshiraseHyoji.件名 = response.data.タイトル
          this.oshiraseHyoji.掲載日 = response.data.掲示開始日
          this.oshiraseHyoji.登録組織 = response.data.部門名
          this.oshiraseHyoji.内容 = response.data.内容
          this.oshiraseHyoji.添付 = response.data.フルパス
          this.oshiraseHyoji.添付ファイル名 = response.data.添付ファイル名
        })
    },
    onDownloadFile () {
      const FileDownloadInfo = {FilePath: this.oshiraseHyoji.添付, FileName: this.oshiraseHyoji.添付ファイル名}
      this.http
        .post('/api/Reafs_T/Common/DownloadFile', FileDownloadInfo, '', {responseType: 'blob'})
        .then(async (blob) => {
          // const url = window.URL.createObjectURL(blob)
          // const link = document.createElement('a')
          // link.href = url
          // link.setAttribute('download', this.oshiraseHyoji.添付ファイル名)
          // document.body.appendChild(link)
          // link.click()
          await this.base.saveFileDownload(blob, this.oshiraseHyoji.添付ファイル名)
        })
        .catch((ex) => console.log(ex))
    },

    onClickBack () {
      this.$router.push({ name: 'TD00001' })
    },

    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  }
}
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
// .el-col {
//   border-radius: 4px;
// }

.home-container {
  padding: 10px;
  min-width: 1380px;
  height: 100%;
}
.item_input_oshiraseHyoji_tenpu {
  width: calc(100% - 120px);
  height: 389px;
  border: 1px solid #b7b5b5;
  padding: 0 15px;
  text-align: left;
  color: black;
  background-color: white !important;
  overflow-wrap: break-word;
}
.item_label_oshiraseHyoji_tenpu {
  height: 389px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 1px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white !important;
  padding: 180px 0 0 0;
  box-sizing: border-box;
  background-color: #808080 !important;
}
.oshiraseHyoji_control {
  margin-bottom: 0px;
}
.item_label_control {
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 1px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white !important;
  box-sizing: border-box;
  background-color: #808080 !important;
}
.item_label_oshiraseHyoji {
  line-height: 30px;
  width: calc(100% - 120px);
  text-align: left;
  padding-right: 0;
  float: left;
  font-size: 14px;
  color: black;
  background-color: white !important;
  border: 1px solid #b7b5b5;
  box-sizing: border-box;
}
.item_input_oshiraseHyoji {
  width: calc(100% - 120px);
}
.item_label_oshiraseHyoji_KeisaiBi {
  line-height: 30px;
  width: 150px;
  text-align: center;
  padding-right: 0;
  float: left;
  font-size: 14px;
  color: black;
  background-color: white !important;
  border: 1px solid #b7b5b5;
  box-sizing: border-box;
}
.item_label_oshiraseHyoji_TorokuSoshiki {
  line-height: 30px;
  width: 100%;
  text-align: left;
  padding-right: 0;
  float: left;
  font-size: 14px;
  color: black;
  padding: 0 15px;
  background-color: white !important;
  border: 1px solid #b7b5b5;
  box-sizing: border-box;
}
.item_label_oshiraseHyoji_Kenmei {
  line-height: 30px;
  width: calc(100% - 120px);
  text-align: left;
  padding-right: 0;
  float: left;
  font-size: 14px;
  color: black;
  padding: 0 15px;
  background-color: white !important;
  border: 1px solid #b7b5b5;
  box-sizing: border-box;
}
</style>

<style>
.OshiraseHyouji .dtKeisaibi input {
  text-align: center;
}
.OshiraseHyouji .el-row.no-margin-bottom {
  margin-bottom: 0px;
}
.OshiraseHyouji .fix-width {
  width: 270px;
}
.OshiraseHyouji .remain-width {
  width: calc(100% - 270px);
}
</style>
