<template>
  <div class="home-container ShashinTenpuSabu">
    <el-form
      :rules="rules"
      ref="refShashinForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk = "true"
    >
      <el-container class="home-el-container">
        <el-main class="home-el-main">
          <div
            v-for="(data, index) in pageData"
            :key="index"
            class="img-row border"
          >
            <el-row class="img-header border-bot">
              <el-col :span="21" style="align-items: center;display: flex;">
                No.{{ data.ファイルNo ? +data.ファイルNo : index + 1 }}
              </el-col>
              <el-col
                class="img-content-col"
                :span="3"
                style="max-height: 26px"
              >
                <el-button
                  class="btnDL"
                  @click="onDownload(index)"
                  :loading="loading['refDl' + index]"
                  >DL</el-button
                >
              </el-col>
            </el-row>
            <el-row class="img-content">
              <el-col class="img-content-col" :span="16">
                  <el-row
                    class="img-show border-bot border-right"
                    ref="refImgContainer"
                  >
                    <img
                      v-if="data.linkImage"
                      :src="data.linkImage"
                      :ref="'refImg' + index"
                    />
                  </el-row>
                <el-row
                  class="img-content-footer border-right"
                  style="display: flex; align-items: center; align-items: stretch; padding: 0"
                >
                  <!--<el-col :span="24" class="img-label">{{data.ファイル名}}</el-col> -->
                  <el-col :span="24" class="img-label">{{
                    data.imgUploadName
                  }}</el-col>
                  <el-col style="width: fit-content; padding: 1px"
                    ><el-button
                      @click="openFileDialog(index)"
                      :loading="loading['btnUploadFile' + index]"
                      :disabled="data.削除不可フラグ=='1'"
                      style="margin-left: 2px;"
                      :ref="'btnUploadFile' + index"
                      >参照</el-button
                    ></el-col
                  >
                  <el-col style="width: fit-content; padding: 1px"
                    ><el-button
                      @click="onDelete(index)"
                      :loading="loading['btnDelete' + index]"
                      :disabled="data.削除不可フラグ=='1'"
                      style="margin-left: 2px;"
                      >削除</el-button
                    ></el-col
                  >
                </el-row>
              </el-col>
              <el-col class="img-content-text" :span="8">
                <el-row style="height: 100%;">
                  <el-col
                    class="img-content-text-col"
                    :span="24"
                    style="height: 100%;"
                  >
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content label-content"
                        style="padding-left: 5px;"
                      >
                        部位
                      </el-col>
                    </el-row>
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content"
                        style="padding: 0 2px"
                      >
                        <el-input
                          class="txtPartComment"
                          maxlength="30"
                          type="textarea"
                          resize="none"
                          :rows="2"
                          v-model="data.部位コメント"
                          ref="ref部位コメント"
                        />
                      </el-col>
                    </el-row>
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content label-content"
                        style="padding-left: 5px;"
                      >
                        状況等
                      </el-col>
                    </el-row>
                    <!--<el-row
                      class="row-situation"
                      style="height: calc(100% - 132px);"
                    >-->
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content"
                        style="height: 100%;padding: 0 2px"
                      >
                        <el-input
                          maxlength="100"
                          type="textarea"
                          resize="none"
                          v-model="data.状況コメント"
                          ref="ref状況コメント"
                          class="txtSituation"
                        />
                      </el-col>
                    </el-row>
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content label-content"
                        style="padding-left: 5px;"
                      >
                        撮影日時
                      </el-col>
                    </el-row>
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content"
                        style="padding: 0 2px"
                      >
                        <el-input
                          class="txtPartCommentSatsuen"
                          maxlength="20"
                          type="textarea"
                          resize="none"
                          v-model="data.撮影日時"
                          ref="ref撮影日時"
                        />
                      </el-col>
                    </el-row>
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content label-content"
                        style="padding-left: 5px;"
                      >
                        撮影場所
                      </el-col>
                    </el-row>
                    <el-row class="img-content-text-row">
                      <el-col
                        :span="24"
                        class="grid-content"
                        style="padding: 0 2px"
                      >
                        <el-input
                          class="txtPartCommentSatsuen"
                          maxlength="20"
                          type="textarea"
                          resize="none"
                          v-model="data.撮影場所"
                          ref="ref撮影場所"
                        />
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </div>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <div style="margin-left: auto;">
            <el-button @click="onRegister()" :loading="loading.btnRegister"
              >更新</el-button
            >
          </div>
          <div style="margin-left: 30px;">
            <el-button id="backbtn" @click="onBack" :loading="loading.loadingBack" 
              >戻る</el-button
            >
          </div>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafstable from '@/components/table/Grid.vue'
export default {
  components: {
    reafsinputtext,
    reafstable
  },
  data () {
    return {
      pageData: [],
      pageData901: [
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault()
      ],
      pageData902: [
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault(),
        this.dataDefault()
      ],
      queryParams: {
        添付種類: '',
        工事依頼No: ''
      },
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      rules: {},
      MSC_5MB_LengthByte: 5245329,
      MSC_300MB_LengthByte: 314572800, // 300MBの判定を追加
      loading: {
        btnRegister: false,
        loadingBack: false,
        btnUploadFile0: false,
        btnUploadFile1: false,
        btnUploadFile2: false,
        btnUploadFile3: false,
        btnDelete0: false,
        btnDelete1: false,
        btnDelete2: false,
        btnDelet34: false,
        refDl0: false,
        refDl1: false,
        refDl2: false,
        refDl3: false
      },
    }
  },
  watch: {
    'koujiiraiNo' (v) {
    }
  },
  computed: {
    isUpdate () {
      return true
    }
  },
  inject: ['hideLeftMenu', 'showLogoutButton','setBtnBackShow'],
  created () {
    let userLogIn = this.$store.getters.getUserInfo()
    this.userInfo.ユーザーＩＤ = userLogIn.userId
    this.userInfo.会社コード = userLogIn.会社コード
  },
  mounted () {
    this.queryParams = {
      添付種類: this.$route.query.添付種類,
      工事依頼No: this.$route.query.工事依頼No
    }
    this.getPageData()
    // document.getElementById('btnLogout').onclick = function (e) {
    //   e.preventDefault()
    //   alert(1)
    // }
    if(this.$route.query.添付種類 == '901'){
      this.setPageTitle('現状写真　添付登録');
    }else if(this.$route.query.添付種類 == '902'){
      this.setPageTitle('完了写真　添付登録');
    }
    if(window == window.parent){
      document.getElementById("backbtn").innerText = '戻る'
      this.showLogoutButton(true)
    }else{
      document.getElementById("backbtn").innerText = '閉じる'
      this.hideLeftMenu(false)
      this.setBtnBackShow(false)
    }
  },
  inject: ['setPageTitle', 'showLogoutButton', 'hideLeftMenu', 'setBtnBackShow'],

  methods: {
    dataDefault () {
      return {
        ファイルNo: '',
        ファイルパス: '',
        ファイルパス2: '',
        ファイル名: '',
        ルートパス: '',
        添付元ファイル名: '',
        物理ファイル名: '',
        物理ファイル名2: '',
        状況コメント: '',
        部位コメント: '',
        撮影日時: '',
        撮影場所: '',
        linkImage: '',
        ダウンロードパス: '',
        添付NO: '',
        imgUploadName: ''
      }
    },
    async getPageData () {
      let result = {}
      await this.http
        .get('/api/Reafs_T/ShashinTenpuSabu/GetPageData', {
          工事依頼No: this.queryParams.工事依頼No,
          添付種類: this.queryParams.添付種類
        })
        .then(res => {
          result = res
        })
      if ((result.status && result.data && result.data.f091Data)) {
        result.data.f091Data.map((x) => {
          x.linkImage = this.http.ipAddress + x.linkImage
          x.添付NO = ''
          x.imgUploadName = ''
        })
        if (this.$route.query.添付種類 == '901') {
          this.pageData901.map((x, i) => {
            const data = result.data.f091Data.find(x => x.ファイルNo === (i + 1))
            if (data) {
              Object.assign(x, data)
            }
            return x
          })
          this.pageData = this.pageData901
        }
        if (this.$route.query.添付種類 == '902') {
          this.pageData902.map((x, i) => {
            const data = result.data.f091Data.find(x => x.ファイルNo === (i + 1))
            if (data) {
              Object.assign(x, data)
            }
            return x
          })
          this.pageData = this.pageData902
        }
      }
    },
    async onDownload (index) {
      this.loading['refDl' + index] = true
      const fileName =
        this.pageData[index].添付元ファイル名 ||
        this.pageData[index].ファイル名

      if (fileName == '' || fileName.toString().trim().length == 0) {
        this.loading['refDl' + index] = false
        return
      }

      if (this.pageData[index].添付NO !== '') {
        let imgdata = this.pageData[index].linkImage
        if (imgdata) {
          let imgtype = imgdata.split(';')[0].split(':')[1]
          let imgbase64 = imgdata.split(',')[1]
          await this.base.saveFileDownload(imgbase64, fileName, imgtype)
          this.getMsg('M040020').then(response => {
            this.showSuccess(response.data)
          })
        }
        this.loading['refDl' + index] = false
        return
      }

      const FileDownloadInfo = {
        FilePath: this.pageData[index].ダウンロードパス,
        FileName: fileName
      }

      await this.http
        .post('/api/Reafs_T/Common/DownloadFile', FileDownloadInfo, '', {
          responseType: 'blob'
        })
        .then(async blob => {
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: fileName
          // })
          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()

          await this.base.saveFileDownload(blob, fileName)

          this.getMsg('M040020').then(response => {
            this.showSuccess(response.data)
          })
        })
        .catch(ex => console.log(ex))
        .finally(() => {
          this.loading['refDl' + index] = false
        })
        .catch(ex => console.log(ex))
    },
    async onDelete (index) {
      const dataDefault = this.dataDefault()
      this.loading['btnDelete' + index] = true
      const f093Idnx = this.pageData[index]
      if (f093Idnx.添付NO !== '') {
        const data = {
          FLAG添付NO: f093Idnx.添付NO
        }
        let res = {}
        await this.http
          .post(
            'api/Reafs_T/ShashinTenpuSabu/fnc_delF093_一時添付ファイル',
            data,
            'アクセスしています....'
          )
          .then(response => {
            res = response
          })
      }
      Object.assign(this.pageData[index], dataDefault)
      this.loading['btnDelete' + index] = false
    },
    openFileDialog (index) {
      const me = this
      const imageExtension = ['memorybmp', 'bmp', 'emf', 'wmf', 'gif', 'jpeg', 'jpg', 'png', 'tiff', 'exif',
        'icon', '.memorybmp', '.bmp', '.emf', '.wmf', '.gif', '.jpeg', '.jpg', '.png', '.tiff', '.exif', '.icon']
      var input = document.createElement('input')
      input.type = 'file'
      input.accept = imageExtension
      const pageData = this.pageData

      input.onchange = e => {
        me.loading['btnUploadFile' + index] = true
        var file = e.target.files[0]
        if (!file) {
          me.loading['btnUploadFile' + index] = false
          return
        }

        if (imageExtension.includes(file['type'].split('/')[1]) === false) {
          e.preventDefault()
          this.getMsg('E040466').then(response => {
            this.showError(response.data)
          })
          me.loading['btnUploadFile' + index] = false
          return
        }
        // ファイルアップロードサイズの制限を300MBに変更
        // if (file.size > this.MSC_5MB_LengthByte) {
        if (file.size > this.MSC_300MB_LengthByte) {
          e.preventDefault()
          this.getMsg('E040470').then(response => {
            this.showError(response.data)
          })
          me.loading['btnUploadFile' + index] = false
          return
        }
        // FileReader support
        if (FileReader && file) {
          var fr = new FileReader()
          fr.onload = function () {
            pageData[index].linkImage = fr.result
            pageData[index].ダウンロードパス = fr.result
            pageData[index].ファイル名 = file.name
            pageData[index].imgUploadName = file.name
            me.upLoadImage(index, fr.result.split(',')[1])
          }
          fr.readAsDataURL(file)
          input.remove()
        } else {
          // Not supported
          // fallback -- perhaps submit the input to an iframe and temporarily store
          // them on the server until the user's session ends.
          input.remove()
          me.loading['btnUploadFile' + index] = false
        }
      }
      input.click()
    },
    async upLoadImage (index, imgBase64) {
      const data = this.getDataF093(index, imgBase64)
      let res = {}
      await this.http
        .post(
          '/api/Reafs_T/ShashinTenpuSabu/fnc_InsertF093',
          data,
          'アクセスしています....'
        )
        .then(response => {
          res = response
        })
      if (res.status) {
        this.pageData[index].添付NO = res.data
      } else {
      }
      this.loading['btnUploadFile' + index] = false
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof option.callback === 'function') {
            option.callback()
          }
        }
      })
    },
    showSuccess (message, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgInfo({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading()
        }
      })
    },
    hideLoading () {
      // eslint-disable-next-line no-return-assign
      Object.keys(this.loading).forEach(v => (this.loading[v] = false))
    },
    confirm (message, title = this.$store.getters.getPageTitle()) {
      return new Promise((resolve, reject) => {
        this.MsgConf({
          title,
          message,
          callback: function (a, b) {
            if (a === 'confirm') {
              resolve(true)
            } else {
              resolve(false)
            }
          }
        })
      })
    },
    regisValidate () {
      const lstNotNull = this.pageData.filter(
        x => x.部位コメント || x.状況コメント || x.撮影日時 || x.撮影場所
      )
      if (lstNotNull && lstNotNull.find(x => !x.linkImage)) {
        this.getMsg('E040465').then(response => {
          this.showError(response.data)
        })
        const index = this.pageData.indexOf(lstNotNull.find(x => !x.linkImage))
        this.$refs['btnUploadFile' + index][0].$el.focus()
        return false
      }

      if(this.queryParams.添付種類 == '902'){
        const linkNotNull = this.pageData.filter(
          x => x.linkImage
        )
        if (linkNotNull && linkNotNull.find(x => !x.撮影日時)) {
          this.getMsg('E040713').then((response) => {
            this.showError(response.data)
          })
          const index1 = this.pageData.indexOf(linkNotNull.find(x => !x.撮影日時))
          this.$refs.ref撮影日時[index1].focus()
          return false
        }

        const linkNotNull2 = this.pageData.filter(
          x => x.linkImage
        )
        if (linkNotNull2 && linkNotNull2.find(x => !x.撮影場所)) {
          this.getMsg('E040714').then((response) => {
            this.showError(response.data)
          })
          const index2 = this.pageData.indexOf(linkNotNull.find(x => !x.撮影場所))
          this.$refs.ref撮影場所[index2].focus()
          return false
        }
      }

      const nitijiNotNull = this.pageData.filter(
        x =>  x.撮影日時
      )
      if(nitijiNotNull && nitijiNotNull.find(x => encodeURI(x.撮影日時).replace(/%../g, "*").length > 30)){
        this.getMsg('E040759').then((response) => {
          this.showError(response.data)
        })
        const index3 = this.pageData.indexOf(nitijiNotNull.find(x => encodeURI(x.撮影日時).replace(/%../g, "*").length > 30))
        this.$refs['btnUploadFile' + index3][0].$el.focus()
        this.$refs.ref撮影日時[index3].focus()
        return false
      }
      return true
    },
    async onRegister (mode) {
      this.loading.btnRegister = true
      if (!this.regisValidate()) {
        return false
      }

      let msg = (await this.getMsg('K000004')).data
      if (!(await this.confirm(msg))) {
        this.loading.btnRegister = false
        return false
      }
      const data = this.getDataF091()
      let res = {}
      await this.http
        .post(
          '/api/Reafs_T/ShashinTenpuSabu/fnc_登録',
          data,
          'アクセスしています....'
        )
        .then(response => {
          res = response
        })
        .catch(ex => {
          // this.showError(ex)
        })
        .finally(() => {
          this.loading.btnRegister = false
        })
      if (res.status) {
        this.showSuccess(res.message)
        this.getPageData()
        this.loading.btnRegister = false
        return true
      } else {
        this.loading.btnRegister = false
        this.showError(res.message)
        return false
      }
    },
    getDataF091 () {
      const data = []
      for (let i = 0; i <= this.pageData.length - 1; i++) {
        const imgBoxData = this.pageData[i]
        if (!imgBoxData.linkImage) {
          data.push({
            F091_画像管理ファイル: {
              工事依頼No: this.queryParams.工事依頼No,
              添付種類: this.queryParams.添付種類,
              ファイルNo: imgBoxData.ファイルNo || i + 1
            },
            添付NO: imgBoxData.添付NO
          })
          continue
        }
        const f091 = {
          会社コード: this.userInfo.会社コード,
          工事依頼No: this.queryParams.工事依頼No,
          添付種類: this.queryParams.添付種類,
          ファイルNo: imgBoxData.ファイルNo || i + 1,
          ファイルパス: imgBoxData.ファイルパス,
          物理ファイル名: imgBoxData.物理ファイル名,
          添付元ファイル名: imgBoxData.添付元ファイル名,
          ファイルパス2: imgBoxData.ファイルパス2,
          物理ファイル名2: imgBoxData.物理ファイル名2,
          削除不可フラグ: 0,
          概要書使用フラグ: 0,
          完了報告書使用フラグ: (this.queryParams.添付種類 == '902' ? 1 : 0),
          部位コメント: imgBoxData.部位コメント,
          状況コメント: imgBoxData.状況コメント,
          撮影日時: imgBoxData.撮影日時,
          撮影場所: imgBoxData.撮影場所
        }
        data.push({
          F091_画像管理ファイル: f091,
          添付NO: imgBoxData.添付NO
        })
      }
      return data
    },
    getDataF093 (index, imgByte) {
      const f093Idnx = this.pageData[index]
      const F093 = {
        添付NO: f093Idnx.添付NO || 0,
        枝番: 1,
        削除区分: 0,
        添付元ファイル名: f093Idnx.ファイル名,
        ファイルデータ: imgByte
      }
      return {
        F093_一時添付ファイル: F093,
        FLAG添付NO: f093Idnx.添付NO
      }
    },
    async onBack () {
      if(window == window.parent){
        this.$router.back()
      }else{
        window.parent.postMessage({
          'call': 'closePopup'
        }, '*')
      }
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  }
}
</script>
<style lang="less" scoped>
main {
  display: grid;
  grid-template-columns: 50% 50%;
  grid-auto-rows: minmax(50%, auto) !important;;
}
.main-container {
  left: 0 !important;
  background: white !important;
}
.img-content {
  display: flex;
  flex-direction: row;
  height: calc(100% - 36px);
}
.img-content-footer {
  padding: 2px;
}
.img-content-footer button {
  padding: 9px 15px;
}
.border {
  border: 1px solid #dcdfe6;
}
.border-top {
  border-top: 1px solid #dcdfe6;
}
.border-bot {
  border-bottom: 1px solid #dcdfe6;
}
.border-left {
  border-left: 1px solid #dcdfe6;
}
.border-right {
  border-right: 1px solid #dcdfe6;
}
.img-row{
  height: 100% !important;
}
.img-row:nth-child(2) {
  border-left: none;
}
.img-row:nth-child(3) {
  border-top: none;
}
.img-row:nth-child(4) {
  border-top: none;
  border-left: none;
}
.grid-content {
  min-height: 25px;
}
.img-show {
  position: relative;
  width: 100%;
  height: calc(100% - 37px);
  img {
    position: absolute;
    top: 5px;
    left: 5px;
    bottom: 5px;
    right: 5px;
    max-width: 97.6%;
    max-height: 95.6%;
    object-fit: cover;
    margin: auto;
    background-color: white;
  }
  background-color: #f3f3f3;
}
.img-header {
  display: flex;
  align-item: center;
  width: 100%;
  padding: 5px;
}
.btnDL {
  padding: 5px 10px;
}
.btnDL.is-loading {
  padding-top: 5px;
  padding-bottom: 5px;
}
.home-container {
  width: 100%;
  height: 760px !important;
  font-size: 9pt;
  margin-bottom: 50px;
}
.home-container form {
  height: 100%;
}
.home-el-container {
  height: 100%;
}
.home-el-header {
  padding: 0 0 20px 0;
}
.home-el-main,
.home-container {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
}
.home-el-footer {
  padding: 10px 0 5px 0;
}

.label-content {
  background: #d3d3d3;
  border-color: #b7b5b5;
  color: black;
  display: flex;
  align-items: center;
}

.el-dialog__body {
  padding: 12px 5px;
}
.img-content-text-row:not(last-child) {
  margin-bottom: 2px;
}
.img-label {
  padding-left: 5px;
  background: #d3d3d3;
  border-color: #b7b5b5;
  display: flex;
  align-items: center;
  overflow: hidden;
}
.el-scrollbar__wrap {
  padding-right: none;
}
footer {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  height: auto;
  background: white;
  display: flex;
}
</style>
<style>
.ShashinTenpuSabu .row-situation {
  height: 100%;
}
.ShashinTenpuSabu .txtSituation,
.txtSituation textarea {
  height: 85pt;
  font-size: 9pt;
}
.ShashinTenpuSabu .el-textarea__inner{
  padding: 5px 4px;
}
.ShashinTenpuSabu .txtPartComment textarea {
  padding: 5px 4px;
  height: 50px;
  font-size: 9pt;
}
.ShashinTenpuSabu .txtPartCommentSatsuen textarea {
  padding: 5px 4px;
  height: 25px;
  font-size: 9pt;
}
.ShashinTenpuSabu .header-collapse {
  display: none !important;
}
.ShashinTenpuSabu .main-content {
  margin-bottom: 53px !important;
  background: white !important;
}

.ShashinTenpuSabu .wrap-class .el-scrollbar__view {
  width: 100% !important;
}

.ShashinTenpuSabu .el-main .home-el-main{
  width: 89% !important;
}

.ShashinTenpuSabu .el-scrollbar .el-scrollbar__bar.is-vertical{
  opacity: 1 !important;
  -webkit-transition: opacity 340ms ease-out;
  transition: opacity 340ms ease-out;
}
</style>
