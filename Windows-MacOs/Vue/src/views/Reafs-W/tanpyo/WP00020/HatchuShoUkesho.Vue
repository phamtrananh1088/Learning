<template>
  <div class="home-container">
    <el-form
      ref="refHatchuShoUkeshoForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk = "true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row class="border-bot" style="margin-top: 20px">
          </el-row>
        </el-header>
        <el-main class="home-el-main">
          <label id="lblNoti">{{ dataForm.Noti }}</label>
          <el-row>
            <el-col :span="15">
              <el-form-item label-width="0px">
                <el-radio-group v-model="dataForm.固定文" style="width: 100%;">
                  <el-radio
                   label="1"
                   tabindex="0"
                   :disabled="画面入力制御disabled.固定文1"
                   style="width: 54%;">発注書・発注請書を御中で出力</el-radio>
                  <el-radio
                   label="2"
                   tabindex="1"
                   :disabled="画面入力制御disabled.固定文2"
                  >発注書・発注請書を様で出力</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label-width="0px" prop="発注書">
                <el-checkbox
                  tabindex="2"
                  label="発注書"
                  checked
                  v-model="dataForm.発注書"
                  :disabled="画面入力制御disabled.発注書"
                  ref="ref発注書"
                  style="margin-right: 10px;">
                </el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="発注日" prop="発注日">
                <el-date-picker
                  required
                  tabindex="3"
                  maxlength="10"
                  type="date"
                  placeholder=""
                  v-model="dataForm.発注日"
                  :disabled="画面入力制御disabled.発注日"
                  style="width: 135px"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref発注日"
                ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label-width="0px" prop="発注請書">
                <el-checkbox
                  tabindex="4"
                  label="発注請書"
                  checked
                  v-model="dataForm.発注請書"
                  :disabled="画面入力制御disabled.発注請書"
                  style="margin-right: 10px;">
                </el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label-width="0px" prop="契約約款印字">
                <el-checkbox
                  tabindex="5"
                  label="契約約款印字"
                  checked
                  v-model="dataForm.契約約款印字"
                  :disabled="画面入力制御disabled.契約約款印字"
                  style="margin-right: 10px;">
                </el-checkbox>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label-width="0px" prop="契約約款印字">
                <el-select
                  :required="dataForm.契約約款印字 ? true : false"
                  tabindex="6"
                  v-model="dataForm.契約約款印字_リスト"
                  :disabled="画面入力制御disabled.契約約款印字_リスト"
                  @change="selectKeiyakuYakkanInjiRisuto"
                  ref="ref契約約款印字"
                  style="width: 100%;"
                >
                  <el-option
                    v-for="item in 契約約款印字value"
                    :key="item.名称コード"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="工事監理者" prop="工事監理者">
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label-width="0px" prop="工事監理者">
                <el-select
                  required
                  tabindex="7"
                  v-model="dataForm.工事監理者"
                  :disabled="画面入力制御disabled.工事監理者"
                  @change="selectKojiKanriSha"
                  ref="ref工事監理者"
                  style="width: 100%;"
                >
                  <el-option
                    v-for="item in 工事監理者value"
                    :key="item.名称コード"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="提出先" prop="提出先">
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label-width="0px" prop="提出先">
                <el-select
                  required
                  tabindex="8"
                  v-model="dataForm.提出先"
                  :disabled="画面入力制御disabled.提出先"
                  @change="selectTeishutsuSaki"
                  ref="ref提出先"
                  style="width: 100%;"
                >
                  <el-option
                    v-for="item in 提出先value"
                    :key="item.名称コード"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label-width="0px" prop="作成済の発注書_発注請書を出力">
                <el-checkbox
                  tabindex="9"
                  label="作成済の発注書・発注請書を出力"
                  v-model="dataForm.作成済の発注書_発注請書を出力"
                  :disabled="画面入力制御disabled.作成済の発注書_発注請書を出力"
                  @change="checkedChange"
                  style="margin-right: 10px;">
                </el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label-width="0px" prop="サーバーに保存する">
                <el-checkbox
                  tabindex="10"
                  label="サーバーに保存する"
                  v-model="dataForm.サーバーに保存する"
                  :disabled="画面入力制御disabled.サーバーに保存する"
                  style="margin-right: 10px;">
                </el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="10"><div class="grid-content">&nbsp;</div></el-col>
            <el-col :span="4">
              <el-button
              tabindex="30"
              @click="onExport">出力</el-button>
            </el-col>
            <el-col :span="2">
              <el-button tabindex="31" @click="onBack" style="float: right">閉じる</el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafstable from '@/components/table/Grid.vue'
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'

var $select契約約款印字value
var $select工事監理者value
var $select提出先value

export default {
  components: {
    reafstable,
    reafsinputtext
  },
  data () {
    return {
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      dataForm: {
        Noti: '',
        固定文: '1',
        発注書: false,
        発注日: '',
        発注請書: false,
        契約約款印字: false,
        契約約款印字_リスト: '',
        工事監理者: '',
        提出先: '',
        作成済の発注書_発注請書を出力: false,
        サーバーに保存する: false
      },
      保存済ファイル: [],
      queryParams: {
        工事依頼No: '',
        工事依頼No枝番: '',
        発注書: '',
        発注請書: '',
        取引先コード: '',
        取引先コード枝番: '',
        取引先名: ''
      },
      契約約款印字value: [],
      工事監理者value: [],
      提出先value: [],
      画面入力制御disabled: {
        固定文1: false,
        固定文2: false,
        発注書: false,
        発注日: false,
        発注請書: false,
        契約約款印字: false,
        契約約款印字_リスト: false,
        工事監理者: false,
        提出先: false,
        作成済の発注書_発注請書を出力: false,
        サーバーに保存する: false
      },
      契約約款_印字内容: '',
      工事監理者_名称: '',
      工事監理者_備考1: '',
      工事監理者_備考2: '',
      工事監理者_備考3: '',
      提出先_名称: '',
      提出先_備考1: ''
    }
  },
  watch: {
  },
  computed: {
  },
  inject: ['hideLeftMenu', 'showHeaderBar'],
  created () {
    $select契約約款印字value = this
    $select工事監理者value = this
    $select提出先value = this
  },
  mounted () {
    this.queryParams = {
      工事依頼No: this.$route.query.工事依頼No,
      工事依頼No枝番: this.$route.query.工事依頼No枝番,
      取引先コード: this.$route.query.取引先コード,
      取引先コード枝番: this.$route.query.取引先コード枝番,
      取引先名: this.$route.query.取引先名
    }

    this.hideLeftMenu(false)
    this.showHeaderBar(false)
    this.init()
  },
  methods: {
    async init () {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.会社コード = userLogIn.会社コード

      await this.http
        .get('/api/Reafs_W/Tanpyo/WP00020/GetStartInfo',
          { 会社コード: this.userInfo.会社コード,
            工事依頼No: this.queryParams.工事依頼No,
            工事依頼No枝番: this.queryParams.工事依頼No枝番,
            取引先コード: this.queryParams.取引先コード,
            取引先コード枝番: this.queryParams.取引先コード枝番 }
          , 'アクセスしています....')
        .then((response) => {
          // 契約約款印字リスト
          if (response.data.契約約款印字リスト !== null && response.data.契約約款印字リスト.length > 0) {
            $select契約約款印字value.契約約款印字value = response.data.契約約款印字リスト
            this.dataForm.契約約款印字_リスト = response.data.契約約款印字リスト[0].名称コード
            this.契約約款_印字内容 = response.data.契約約款印字リスト[0].印字内容
          }
          if (response.data.工事監理者_提出先 !== null && response.data.工事監理者_提出先.length > 0) {
            // 工事監理者
            $select工事監理者value.工事監理者value = response.data.工事監理者_提出先
            // 提出先
            $select提出先value.提出先value = response.data.工事監理者_提出先
            // 初期値のセット
            // ハノイ側修正2022/12/28　STEP2_W　課題管理表№262：設計書「修正履歴」シートの「2022/12/27仕様変更分」「初期値セット方法を変更」START
            if (response.data.defaultInfo) {
              // 工事監理者
              const obj工事監理者 = $select工事監理者value.工事監理者value.find(item => {
                return item.名称コード === response.data.defaultInfo.工事監理者
              })

              if (obj工事監理者 !== undefined) {
                this.工事監理者_名称 = obj工事監理者.名称
                this.工事監理者_備考1 = obj工事監理者.備考1
                this.工事監理者_備考2 = obj工事監理者.備考2
                this.工事監理者_備考3 = obj工事監理者.備考3
                this.dataForm.工事監理者 = obj工事監理者.名称コード
              } else {
                this.工事監理者_名称 = response.data.工事監理者_提出先[0].名称
                this.工事監理者_備考1 = response.data.工事監理者_提出先[0].備考1
                this.工事監理者_備考2 = response.data.工事監理者_提出先[0].備考2
                this.工事監理者_備考3 = response.data.工事監理者_提出先[0].備考3
              }

              // 提出先
              const obj提出先 = $select工事監理者value.工事監理者value.find(item => {
                return item.名称コード === response.data.defaultInfo.提出先
              })
              if (obj提出先 !== undefined) {
                this.提出先_名称 = obj提出先.名称
                this.提出先_備考1 = obj提出先.備考1
                this.dataForm.提出先 = obj提出先.名称コード
              } else {
                this.提出先_名称 = response.data.工事監理者_提出先[0].名称
                this.提出先_備考1 = response.data.工事監理者_提出先[0].備考1
              }

              // 発注書・発注請書を御中で出力
              this.dataForm.固定文 = response.data.defaultInfo.帳票用敬称区分 + ''
              // 発注日
              this.dataForm.発注日 = response.data.defaultInfo.帳票用発注日
              // リスト
              this.dataForm.契約約款印字_リスト = response.data.defaultInfo.約款印字パターン
            } else {
              this.dataForm.工事監理者 = response.data.工事監理者_提出先[0].名称コード
              this.dataForm.提出先 = response.data.工事監理者_提出先[0].名称コード

              this.工事監理者_名称 = response.data.工事監理者_提出先[0].名称
              this.工事監理者_備考1 = response.data.工事監理者_提出先[0].備考1
              this.工事監理者_備考2 = response.data.工事監理者_提出先[0].備考2
              this.工事監理者_備考3 = response.data.工事監理者_提出先[0].備考3
              this.提出先_名称 = response.data.工事監理者_提出先[0].名称
              this.提出先_備考1 = response.data.工事監理者_提出先[0].備考1
            }
          }
          // 契約約款印字
          if (!response.data.defaultInfo.約款印字パターン) {
            this.dataForm.契約約款印字 = false
          }
          // ハノイ側修正2022/12/28　STEP2_W　課題管理表№262：設計書「修正履歴」シートの「2022/12/27仕様変更分」「初期値セット方法を変更」END
          this.保存済ファイル = response.data.保存済ファイル
          if (response.data.保存済ファイル !== null && response.data.保存済ファイル.length > 0) {
            this.dataForm.Noti = '保存された発注書・請書があります'
            this.showHideItem(true)
            this.dataForm.作成済の発注書_発注請書を出力 = true
            this.dataForm.サーバーに保存する = false
            // ハノイ側修正2022/12/16　STEP2_W　課題管理表№249：設計書「修正履歴」シートの「2022/12/13仕様変更分」「F090が存在する場合、入力項目をF004_発注明細ファイルから表示する様修正」Start
            // if (response.data.f004) {
            //   this.dataForm.発注日 = response.data.f004.帳票用発注日
            //   this.dataForm.契約約款印字_リスト = response.data.f004.約款印字パターン
            //   this.dataForm.工事監理者 = response.data.f004.工事監理者
            //   this.dataForm.提出先 = response.data.f004.提出先
            // }
            // ハノイ側修正2022/12/16　STEP2_W　課題管理表№249：設計書「修正履歴」シートの「2022/12/13仕様変更分」「F090が存在する場合、入力項目をF004_発注明細ファイルから表示する様修正」End
          } else {
            this.dataForm.Noti = ''
            this.showHideItem(false)
            this.dataForm.作成済の発注書_発注請書を出力 = false
            this.dataForm.サーバーに保存する = true
          }
        })
    },
    showHideItem (bEnable) {
      this.画面入力制御disabled.固定文1 = bEnable
      this.画面入力制御disabled.固定文2 = bEnable
      this.画面入力制御disabled.発注日 = bEnable
      this.画面入力制御disabled.契約約款印字 = bEnable
      this.画面入力制御disabled.契約約款印字_リスト = bEnable
      this.画面入力制御disabled.工事監理者 = bEnable
      this.画面入力制御disabled.提出先 = bEnable
      this.画面入力制御disabled.作成済の発注書_発注請書を出力 = true
      this.画面入力制御disabled.サーバーに保存する = bEnable
    },
    selectKeiyakuYakkanInjiRisuto (val) {
      var obj = {}
      obj = $select契約約款印字value.契約約款印字value.find(item => {
        return item.名称コード === val
      })
      this.契約約款_印字内容 = obj.印字内容
    },
    selectKojiKanriSha (val) {
      var obj = {}
      obj = $select工事監理者value.工事監理者value.find(item => {
        return item.名称コード === val
      })
      this.工事監理者_名称 = obj.名称
      this.工事監理者_備考1 = obj.備考1
      this.工事監理者_備考2 = obj.備考2
      this.工事監理者_備考3 = obj.備考3
    },
    selectTeishutsuSaki (val) {
      var obj = {}
      obj = $select工事監理者value.工事監理者value.find(item => {
        return item.名称コード === val
      })
      this.提出先_名称 = obj.名称
      this.提出先_備考1 = obj.備考1
    },
    checkedChange (val) {
      if (val) {
        this.dataForm.サーバーに保存する = false
        this.画面入力制御disabled.サーバーに保存する = true
      } else {
        this.dataForm.サーバーに保存する = true
        this.画面入力制御disabled.サーバーに保存する = false
      }
    },
    async exportValidate () {
      if (!this.dataForm.発注書 && !this.dataForm.発注請書) {
        this.getMsg('E040320').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref発注書.focus()
          }})
        })
        return false
      }

      if (!this.画面入力制御disabled.発注日) {
        if (this.dataForm.発注日 === null || this.dataForm.発注日 === '') {
          this.getMsg('E040277').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref発注日.focus()
            }})
          })
          return false
        }
      }

      if (!this.画面入力制御disabled.契約約款印字_リスト) {
        if (this.dataForm.契約約款印字 && this.dataForm.契約約款印字_リスト === '') {
          this.getMsg('E040536').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref契約約款印字.focus()
            }})
          })
          return false
        }
      }

      if (!this.画面入力制御disabled.工事監理者) {
        if (this.dataForm.工事監理者 === '') {
          this.getMsg('E040537').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref工事監理者.focus()
            }})
          })
          return false
        }
      }

      if (!this.画面入力制御disabled.提出先) {
        if (this.dataForm.提出先 === '') {
          this.getMsg('E040538').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref提出先.focus()
            }})
          })
          return false
        }
      }

      if (this.dataForm.サーバーに保存する && (!this.dataForm.発注書 || !this.dataForm.発注請書)) {
        this.getMsg('E040580').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref発注書.focus()
          }})
        })
        return false
      }

      return true
    },
    async onExport () {
      if (!await this.exportValidate()) {
        return false
      }

      if (this.dataForm.作成済の発注書_発注請書を出力) {
        if (this.保存済ファイル !== null && this.保存済ファイル.length > 0) {
          if (this.保存済ファイル.length > 0 && this.dataForm.発注書) {
            const data = this.保存済ファイル.filter(x => x.添付種類 == '109')
            for (let i = 0; i < data.length; i++) {
              const fileName = data[i].物理ファイル名
              const filePath = data[i].ダウンロードパス
              const 添付種類 = data[i].添付種類
              const FileDownloadInfo = {FilePath: filePath, FileName: fileName}
              await this.http
                .post('/api/Reafs_W/Common/DownloadFile', FileDownloadInfo, 'アクセスしています....',
                  {responseType: 'blob'})
                .then(async blob => {
                  if (blob.size === 0) {
                    this.getMsg('E010224').then(res => {
                      this.showError(res.data)
                    })
                    return
                  }

                  // const fileHandle = await window.showSaveFilePicker({
                  //   suggestedName: 添付種類 === '109' ? '発注書.pdf' : '発注請書.pdf'
                  // })

                  // const fileStream = await fileHandle.createWritable()
                  // await fileStream.write(blob)
                  // await fileStream.close()

                  await this.base.saveFileDownload(blob, 添付種類 === '109' ? '発注書.pdf' : '発注請書.pdf')

                  const resMessage = await this.getMsg('M040020')
                  await this.showSuccess(resMessage.data)
                })
                .catch((ex) => console.log(ex))
            }
          }

          if (this.保存済ファイル.length > 1 && this.dataForm.発注請書) {
            const data = this.保存済ファイル.filter(x => x.添付種類 == '110')
            for (let i = 0; i < data.length; i++) {
              const fileName = data[i].物理ファイル名
              const filePath = data[i].ダウンロードパス
              const 添付種類 = data[i].添付種類
              const FileDownloadInfo = {FilePath: filePath, FileName: fileName}
              await this.http
                .post('/api/Reafs_W/Common/DownloadFile', FileDownloadInfo, 'アクセスしています....',
                  {responseType: 'blob'})
                .then(async blob => {
                  if (blob.size === 0) {
                    this.getMsg('E010224').then(res => {
                      this.showError(res.data)
                    })
                    return
                  }

                  // const fileHandle = await window.showSaveFilePicker({
                  //   suggestedName: 添付種類 == '109' ? '発注書.pdf' : '発注請書.pdf'
                  // })

                  // const fileStream = await fileHandle.createWritable()
                  // await fileStream.write(blob)
                  // await fileStream.close()

                  await this.base.saveFileDownload(blob, 添付種類 == '109' ? '発注書.pdf' : '発注請書.pdf')

                  const resMessage = await this.getMsg('M040020')
                  await this.showSuccess(resMessage.data)
                })
                .catch((ex) => console.log(ex))
            }
          }
        } else {
          this.getMsg('E040476').then((response) => {
            this.showError(response.data, {callback: () => {
            }})
          })
        }
      } else {
        // preview report
        this.print()
      }
    },
    async print () {
      const data = {
        ログイン会社コード: this.userInfo.会社コード,
        工事依頼No: this.queryParams.工事依頼No,
        工事依頼No枝番: this.queryParams.工事依頼No枝番,
        固定文: this.dataForm.固定文,
        発注書chk: this.dataForm.発注書,
        発注請書chk: this.dataForm.発注請書,
        取引先コード: this.queryParams.取引先コード,
        取引先コード枝番: this.queryParams.取引先コード枝番,
        取引先名: this.queryParams.取引先名,
        発注日: this.dataForm.発注日,
        約款印字パターン: this.dataForm.契約約款印字_リスト,
        工事監理者: this.dataForm.工事監理者,
        提出先: this.dataForm.提出先,
        契約約款_印字内容: this.契約約款_印字内容,
        工事監理者_名称: this.工事監理者_名称,
        工事監理者_備考1: this.工事監理者_備考1,
        工事監理者_備考2: this.工事監理者_備考2,
        工事監理者_備考3: this.工事監理者_備考3,
        提出先_名称: this.提出先_名称,
        提出先_備考1: this.提出先_備考1,
        契約約款: this.dataForm.契約約款印字,
        サーバーに保存する: this.dataForm.サーバーに保存する
      }
      await this.http
        .post('/api/Reafs_W/Tanpyo/WP00020/ExportReport', data, 'アクセスしています....', {
          responseType: 'blob'
        })
        .then(async blob => {
          if (blob.size === 0) {
            this.getMsg('E010224').then(res => {
              this.showError(res.data)
            })
            return
          }

          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: '発注書・請書.pdf'
          // })

          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()

          await this.base.saveFileDownload(blob, '発注書・請書.pdf')

          this.getMsg('M040020').then(response => {
            this.showSuccess(response.data)
          })
          const dataFormSave = JSON.parse(JSON.stringify(this.dataForm))
          if (this.dataForm.サーバーに保存する) {
            await this.init()
            this.dataForm.固定文 = dataFormSave.固定文
            this.dataForm.発注書 = dataFormSave.発注書
            this.dataForm.発注日 = dataFormSave.発注日
            this.dataForm.発注請書 = dataFormSave.発注請書
            this.dataForm.契約約款印字 = dataFormSave.契約約款印字
            this.dataForm.契約約款印字_リスト = dataFormSave.契約約款印字_リスト
            this.dataForm.工事監理者 = dataFormSave.工事監理者
            this.dataForm.提出先 = dataFormSave.提出先
          }
        })
    },
    async onBack () {
      window.parent.postMessage(
        {
          call: 'closePopup'
        },
        '*'
      )
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          if (option && typeof option.callback === 'function') {
            option.callback()
          }
        }
      })
    },
    showSuccess (message, title = this.$store.getters.getPageTitle()) {
      var self = this
      return new Promise((resolve, reject) => {
        this.MsgInfo({
          title,
          message,
          callback: function (a, b) {
            resolve(true)
          }
        })
      })
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
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  }
}
</script>
<style lang="less" scoped>
.el-row {
  //margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.home-container {
  padding: 0 10px 0 10px;
  min-width: 480px;
  height: 97%;
  // width: 50%;
}

.sample-form-class {
  height: 100%;
}

.home-el-container {
  height: 100%;
}

.home-el-header {
  padding: 0 0 0 0;
}

.home-el-main {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
}

.home-el-footer {
  padding: 10px 0 0 0;
}

</style>
<style>
.el-scrollbar__view {
  width: 100% !important;
}
</style>
