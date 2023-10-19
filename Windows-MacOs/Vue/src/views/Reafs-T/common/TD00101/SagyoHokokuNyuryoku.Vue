<template>
  <div class="home-container SagyoHokokuNyuryoku">
    <el-form
      :model="sagyoForm"
      ref="refsagyoForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk = "true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
        </el-header>
        <el-main class="home-el-main">
          <!-- <el-row :gutter="20"> -->
          <el-row>
            <el-col :span="5">
              <el-form-item label="工事依頼№" prop="koujiiraiNo">
                <reafsinputtext
                  tabindex="1"
                  maxlength="11"
                  disabled
                  v-model="sagyoForm.工事依頼NoDsp"
                  :width="'80%'"
                  class="is-readonly"
                  ref="koujiiraiNo"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="13" id="keizoku">
              &nbsp;
            </el-col>
            <el-col :span="6" style="padding-left: 25px;">
              <el-form-item label="工事予定日" prop="koujiYoteiBi">
                <reafsinputtext
                  v-if="is工事予定日"
                  v-model="sagyoForm.工事予定日_開始日 + '～' + sagyoForm.工事予定日_終了日"
                  :width="'100%'"
                  disabled
                  class="is-readonly"
                >
                </reafsinputtext>
                <reafsinputtext
                  v-else-if="sagyoForm.工事予定日_開始日"
                  v-model="sagyoForm.工事予定日_開始日 + '～'"
                  :width="'100%'"
                  disabled
                  class="is-readonly"
                >
                </reafsinputtext>
                <reafsinputtext
                  v-else-if="sagyoForm.工事予定日_終了日"
                  v-model="'～' + sagyoForm.工事予定日_終了日"
                  :width="'100%'"
                  disabled
                  class="is-readonly"
                >
                </reafsinputtext>
                <reafsinputtext
                  v-else
                  :width="'88%'"
                  disabled
                  class="is-readonly"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item
                label="物件コード"
                prop="bukkenCd"
              >
                <reafsinputtext
                  tabindex="2"
                  v-model="sagyoForm.物件コード"
                  maxlength="8"
                  disabled
                  class="is-readonly"
                  :width="'80%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label-width="0">
                <reafsinputtext
                  tabindex="3"
                  :title="sagyoForm.物件名"
                  v-model="sagyoForm.物件名"
                  :width="'88%'"
                  disabled
                  class="is-readonly"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="物件住所"
                prop="bukkenJusho"
              >
                <reafsinputtext
                  tabindex="4"
                  v-model="sagyoForm.物件住所郵便番号"
                  maxlength="8"
                  disabled
                  class="is-readonly"
                  :width="'80%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label-width="0">
                <reafsinputtext
                  tabindex="5"
                  :title="sagyoForm.物件住所"
                  v-model="sagyoForm.物件住所"
                  :width="'100%'"
                  disabled
                  class="is-readonly"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="15">
              <el-form-item
                label="工事件名"
                prop="kojiKenmei"
              >
                <reafsinputtext
                  tabindex="6"
                  :title="sagyoForm.工事件名"
                  v-model="sagyoForm.工事件名"
                  maxlength="8"
                  disabled
                  class="is-readonly"
                  :width="'95.5%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="15">
              <el-form-item
                label="依頼者"
                prop="依頼者"
              >
                <reafsinputtext
                  tabindex="7"
                  :title="sagyoForm.依頼者"
                  v-model="sagyoForm.依頼者"
                  maxlength="8"
                  disabled
                  class="is-readonly"
                  :width="'95.5%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <br />
          <el-row>
            <el-col :span="8">
              <el-form-item
                label="作業担当者"
                prop="作業担当者"
                class="narrow is-required"
              >
                <reafsinputtext
                  required
                  :disabled=this.displayMode
                  tabindex="8"
                  maxlength="20"
                  v-model="sagyoForm.作業担当者"
                  :width="'100%'"
                  ref="ref作業担当者"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="7">
              <el-form-item
                label="担当者連絡先"
                prop="担当者連絡先"
                class="narrow is-required"
              >
                <reafsinputtext
                  required
                  :disabled=this.displayMode
                  tabindex="9"
                  maxlength="14"
                  v-model="sagyoForm.担当者連絡先"
                  :width="'85%'"
                  ref="ref担当者連絡先"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="報告日" prop="報告日" class="is-required">
                <!--<el-date-picker
                    required
                    tabindex="10"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="sagyoForm.報告日"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref報告日"
                    @change="onChangeHokokuBi"
                  ></el-date-picker>-->
                  <el-date-picker
                    required
                    :disabled=this.displayMode
                    tabindex="10"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="sagyoForm.報告日"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref報告日"
                  ></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="作業完了日" prop="sagyoKanryoBi" class="narrow1 is-required">
                <div style="display: flex">
                  <!--<el-date-picker
                    required
                    tabindex="11"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="sagyoForm.作業完了日開始日"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref作業完了日開始日"
                    @change="onChangeSagyoKanryoBi_Start"
                  ></el-date-picker>-->
                  <el-date-picker
                    required
                    :disabled=this.displayMode
                    tabindex="11"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="sagyoForm.作業完了日開始日"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref作業完了日開始日"
                  ></el-date-picker>
                  &nbsp;～&nbsp;
                  <!--<el-date-picker
                    required
                    tabindex="12"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="sagyoForm.作業完了日終了日"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref作業完了日終了日"
                    @change="onChangeSagyoKanryoBi_Last"
                  ></el-date-picker>-->
                  <el-date-picker
                    required
                    :disabled=this.displayMode
                    tabindex="12"
                    maxlength="10"
                    type="date"
                    placeholder=""
                    v-model="sagyoForm.作業完了日終了日"
                    style="width: 135px"
                    format="yyyy/MM/dd"
                    value-format="yyyy/MM/dd"
                    ref="ref作業完了日終了日"
                  ></el-date-picker>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="テナント確認" prop="テナント確認" class="narrow1">
                <div style="padding-left: 10px;">
                  <el-checkbox
                  　:disabled=this.displayMode
                    tabindex="13"
                    v-model="sagyoForm.テナント確認"
                    style="margin-right: 10px;">
                  </el-checkbox>
                  <label style="color: red;">テナント現地の完了報告書が添付されている場合チェックしてください</label>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item class="narrow is-required is-area" prop="sekoNaiyo_Tokukijikoto"
               label-width="0px"
               >
                <div style="display: flex">
                  <label
                    class="el-form-item__label item_label_SekoNaiyoTokukijikoTo"
                    >施工内容／<br />特記事項等</label
                  >
                  <el-input
                    required
                    :disabled=this.displayMode
                    tabindex="14"
                    class="is-required item_input_SekoNaiyoTokukijikoTo"
                    maxlength="200"
                    type="textarea"
                    resize="none"
                    :rows="3"
                    v-model="sagyoForm.施工内容"
                    ref="ref施工内容_特記事項等"
                  >
                  </el-input>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item class="is-area" prop="sashimodoshiJiyu"
               label-width="0px"
               >
                <div style="display: flex">
                  <label
                    class="el-form-item__label item_label_SashimodoshiJiyu"
                    >差戻事由</label
                  >
                  <el-input
                    tabindex="15"
                    class="is-readonly item_input_SashimodoshiJiyu"
                    maxlength="200"
                    disabled
                    type="textarea"
                    resize="none"
                    :rows="3"
                    :title="sagyoForm.差戻事由"
                    v-model="sagyoForm.差戻事由"
                  >
                  </el-input>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <br />
          <el-row>
            <el-col :span="8">
              <label
                class="el-form-item__label item_label_Title"
                >●テナント</label
              >
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item label="完了確認日" prop="完了確認日">
                <reafsinputtext
                  tabindex="16"
                  v-model="sagyoForm.テナント完了確認日"
                  maxlength="10"
                  disabled
                  class="is-readonly"
                  :width="'80%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="13">
              <el-form-item
                label="完了確認者"
                prop="完了確認者"
              >
                <reafsinputtext
                  tabindex="17"
                  :title="sagyoForm.テナント完了確認者"
                  v-model="sagyoForm.テナント完了確認者"
                  maxlength="8"
                  disabled
                  class="is-readonly"
                  :width="'100%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <label
                class="el-form-item__label item_label_Title"
                >●ＳＧＲ</label
              >
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item label="完了確認日" prop="完了確認日">
                <reafsinputtext
                  tabindex="18"
                  v-model="sagyoForm.SGR完了確認日"
                  maxlength="10"
                  disabled
                  class="is-readonly"
                  :width="'80%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="13">
              <el-form-item
                label="完了確認者"
                prop="完了確認者"
              >
                <reafsinputtext
                  tabindex="19"
                  :title="sagyoForm.SGR完了確認者"
                  v-model="sagyoForm.SGR完了確認者"
                  maxlength="8"
                  disabled
                  class="is-readonly"
                  :width="'100%'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="18">
              <el-form-item class="is-area" prop="適用"
               label-width="0px"
               >
                <div style="display: flex">
                  <label
                    class="el-form-item__label item_label_Tekiyo"
                    >適用</label
                  >
                  <el-input
                    tabindex="20"
                    class="is-readonly item_input_Tekiyo"
                    maxlength="200"
                    disabled
                    type="textarea"
                    resize="none"
                    :rows="3"
                    :title="sagyoForm.適用"
                    v-model="sagyoForm.適用"
                  >
                  </el-input>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="3">
              <el-button tabindex="30" @click="onCompletionPhoto()">完了写真</el-button>
              <screenPopup
                  src="/Reafs_T/ShashinTenpuSabu"
                  :params="paramsShashinTenpuSabu"
                  :dialogVisible.sync="showDialogShashinTenpuSabu"></screenPopup>
            </el-col>
            <el-col :span="8">
              <el-button tabindex="31" @click="onAttachedDocument()">添付書類</el-button>
              <screenPopup
                  src="/Reafs_T/ZumenShiryoTenpuSabu"
                  :params="paramsZumenShiryoTenpuSabu"
                  :dialogVisible.sync="showDialogZumenShiryoTenpuSabu"></screenPopup>
            </el-col>
            <el-col :span="6">
              <el-form-item
              ><reafsinputtext
                  required
                  tabindex="9"
                  maxlength="20"
                  v-model="sagyoForm.報告書作成日"
                  :width="'85%'"
                  ref="ref担当者連絡先"
                  style="display: none"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="2">
              <el-button tabindex="32" style="display: none" @click="onReportCreate(mode.再発行)">再発行</el-button>
            </el-col>
            <el-col :span="2">
              <el-button tabindex="32" :disabled=this.displayMode @click="onTemporarilySaved(mode.一時保存)">一時保存</el-button>
            </el-col>
            <el-col :span="2">
              <el-button tabindex="33" :disabled=this.displayMode @click="onFinalReport(mode.完了報告)">完了報告</el-button>
            </el-col>
            <el-col :span="1">
              <el-button tabindex="34" @click="onBack" style="float: right">戻る</el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import screenPopup from '../../common/ScreenPopup/ScreenPopup.vue'
import moment from 'moment'

export default {
  components: {
    reafsinputtext,
    screenPopup,
    moment
  },
  data () {
    return {
      userInfo: {
        会社コード: '',
        業者コード: '',
        業者コード枝番: ''
      },
      queryParams: {
        工事依頼No: '',
        工事依頼No枝番: '',
        業者コード: '',
        業者コード枝番: '',
        発注サブNo: '',
        検収回数: ''
      },
      sagyoForm: this.dataDefault(),
      paramsShashinTenpuSabu: {
        添付種類: '905',
        工事依頼No: '',
        工事依頼No枝番: '',
        業者コード: '',
        業者コード枝番: ''
      },
      showDialogShashinTenpuSabu: false,
      paramsZumenShiryoTenpuSabu: {
        業者コード: '',
        業者コード枝番: '',
        工事依頼No: '',
        枝番: ''
      },
      showDialogZumenShiryoTenpuSabu: false,
      mode: Object.freeze({'一時保存': 1, '完了報告': 2, '再発行': 3}),
      displayMode: false
    }
  },
  computed: {
    is工事予定日 () {
      return (this.sagyoForm.工事予定日_開始日 && this.sagyoForm.工事予定日_終了日)
    }
  },
  created () {

  },
  mounted () {
    this.queryParams = {
      工事依頼No: this.$route.params.工事依頼No,
      工事依頼No枝番: this.$route.params.工事依頼No枝番,
      業者コード: this.$route.params.業者コード,
      業者コード枝番: this.$route.params.業者コード枝番,
      発注サブNo: this.$route.params.発注サブNo,
      検収回数: this.$route.params.検収回数
    }

    this.init()
  },
  watch: {
    $route (to, from) {
      this.queryParams = {
        工事依頼No: this.$route.params.工事依頼No,
        工事依頼No枝番: this.$route.params.工事依頼No枝番,
        業者コード: this.$route.params.業者コード,
        業者コード枝番: this.$route.params.業者コード枝番,
        発注サブNo: this.$route.params.発注サブNo,
        検収回数: this.$route.params.検収回数
      }
      this.init()
    }
  },
  methods: {
    dataDefault () {
      return {
        作業担当者: '',
        担当者連絡先: '',
        報告日: '',
        作業完了日開始日: '',
        作業完了日終了日: '',
        施工内容: '',
        テナント確認: true,
        工事依頼No: '',
        工事依頼NoDsp: '',
        工事予定日_開始日: '',
        工事予定日_終了日: '',
        物件コード: '',
        物件名: '',
        物件住所郵便番号: '',
        物件住所: '',
        工事件名: '',
        依頼者: '',
        差戻事由: '',
        テナント完了確認日: '',
        テナント完了確認者: '',
        SGR完了確認日: '',
        SGR完了確認者: '',
        適用: '',
        発注サブNo: 0,
        検収回数: 0,
        報告フラグ: '',
        契約No: 0,
        契約年月: ''
      }
    },
    // ハノイ側修正2022/08/30　STEP2_T　課題管理表№10：作業報告入力「2022/08/29仕様変更分」処理概要の＜一時保存＞＜完了報告＞の内容を追加修正 START
    // async print (mode) {
    //   const data = {
    //     会社コード: this.userInfo.会社コード,
    //     業者コード: this.queryParams.業者コード,
    //     業者コード枝番: this.userInfo.業者コード枝番,
    //     工事依頼No: this.queryParams.工事依頼No,
    //     工事依頼No枝番: this.queryParams.工事依頼No枝番,
    //     発注サブNo: this.sagyoForm.発注サブNo ? this.sagyoForm.発注サブNo : 0,
    //     検収回数: this.sagyoForm.検収回数 ? this.sagyoForm.検収回数 : 0,
    //     完了日: this.sagyoForm.報告日,
    //     出力フラグ: '0'
    //   }
    //   await this.http
    //     .post('/api/Reafs_T/KanryoHokokuShoService/ExportReport', data, 'アクセスしています....', {
    //       responseType: 'blob'
    //     })
    //     .then(async blob => {
    //       if (blob.size === 0) {
    //         this.getMsg('E010224').then(res => {
    //           this.showError(res.data)
    //         })
    //         return
    //       }
    //       // const fileHandle = await window.showSaveFilePicker({
    //       //   suggestedName: '完了報告書.pdf'
    //       // })

    //       // const fileStream = await fileHandle.createWritable()
    //       // await fileStream.write(blob)
    //       // await fileStream.close()

    //       const url = window.URL.createObjectURL(blob)
    //       const link = document.createElement('a')
    //       link.href = url
    //       link.setAttribute('download', '完了報告書.pdf')
    //       document.body.appendChild(link)
    //       link.click()

    //       this.getMsg('M040020').then(response => {
    //         this.showSuccess(response.data)
    //       })
    //     })
    //     .catch(ex => console.log(ex))
    //     .finally(() => {
    //     })
    //     .catch(ex => console.log(ex))
    // },
    // ハノイ側修正2022/08/30　STEP2_T　課題管理表№10：作業報告入力「2022/08/29仕様変更分」処理概要の＜一時保存＞＜完了報告＞の内容を追加修正 END
    init () {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.会社コード = userLogIn.会社コード
      this.userInfo.業者コード = userLogIn.業者コード
      this.userInfo.業者コード枝番 = userLogIn.業者コード枝番

      this.sagyoForm = this.dataDefault()
      this.http
        .get('/api/Reafs_T/SagyoHokokuNyuryoku/GetPageData', {
          業者コード: this.queryParams.業者コード,
          業者コード枝番: this.queryParams.業者コード枝番,
          工事依頼No: this.queryParams.工事依頼No,
          工事依頼No枝番: this.queryParams.工事依頼No枝番,
          発注サブNo: this.queryParams.発注サブNo,
          検収回数: this.queryParams.検収回数
        }
          , 'アクセスしています....')
        .then((response) => {
          if (response.status === true) {
            this.sagyoForm = response.data || {}
            if (this.sagyoForm.テナント確認 === '1') {
              this.sagyoForm.テナント確認 = true
            } else {
              this.sagyoForm.テナント確認 = false
            }
          }
          if (this.sagyoForm.報告フラグ == '1' || this.sagyoForm.継続区分 == '1' || this.sagyoForm.継続区分 == '2') {
            this.displayMode = true
          } else {
            this.displayMode = false
          }
          if(this.sagyoForm.継続区分 == '1' || this.sagyoForm.継続区分 == '2'){
            document.getElementById("keizoku").innerHTML = '<label>' + this.sagyoForm.継続区分名 + '</label>'
          }else{
            document.getElementById("keizoku").innerHTML = '<label>&nbsp;</label>'
          }
        })

      this.$nextTick(() => {
        this.$refs.ref作業担当者.focus()
      })
    },
    onChangeHokokuBi (type) {
      let sCurrDate = this.base.formatCurrDate()
      let dCurrDate = new Date(sCurrDate)
      let d報告日 = new Date(this.sagyoForm.報告日)

      if (d報告日 > dCurrDate) {
        this.getMsg('E010019').then(response => {
          this.showError(response.data)
        })
        if (type !== 1) {
          this.$nextTick(() => {
            this.$refs.ref報告日.focus()
          })
        }
        return false
      } else {
        return true
      }
    },
    onChangeSagyoKanryoBi_Start (type) {
      let sCurrDate = this.base.formatCurrDate()
      let dCurrDate = new Date(sCurrDate)
      let d作業完了日開始日 = new Date(this.sagyoForm.作業完了日開始日)

      if (d作業完了日開始日 > dCurrDate) {
        this.getMsg('E010019').then(response => {
          this.showError(response.data)
        })
        if (type !== 1) {
          this.$nextTick(() => {
            this.$refs.ref作業完了日開始日.focus()
          })
        }
        return false
      } else {
        return true
      }
    },
    onChangeSagyoKanryoBi_Last (type) {
      let sCurrDate = this.base.formatCurrDate()
      let dCurrDate = new Date(sCurrDate)
      let d作業完了日終了日 = new Date(this.sagyoForm.作業完了日終了日)

      if (d作業完了日終了日 > dCurrDate) {
        this.getMsg('E010019').then(response => {
          this.showError(response.data)
        })
        if (type !== 1) {
          this.$nextTick(() => {
            this.$refs.ref作業完了日終了日.focus()
          })
        }
        return false
      } else {
        return true
      }
    },
    async onCompletionPhoto () {
      this.paramsShashinTenpuSabu.工事依頼No = this.queryParams.工事依頼No
      this.paramsShashinTenpuSabu.工事依頼No枝番 = this.queryParams.工事依頼No枝番
      this.paramsShashinTenpuSabu.業者コード = this.queryParams.業者コード
      this.paramsShashinTenpuSabu.業者コード枝番 = this.queryParams.業者コード枝番
      this.showDialogShashinTenpuSabu = true
    },
    async onAttachedDocument () {
      // this.paramsZumenShiryoTenpuSabu.業者コード = this.userInfo.業者コード
      // this.paramsZumenShiryoTenpuSabu.業者コード枝番 = this.userInfo.業者コード枝番
      this.paramsZumenShiryoTenpuSabu.業者コード = this.queryParams.業者コード
      this.paramsZumenShiryoTenpuSabu.業者コード枝番 = this.queryParams.業者コード枝番
      this.paramsZumenShiryoTenpuSabu.工事依頼No = this.queryParams.工事依頼No
      this.paramsZumenShiryoTenpuSabu.枝番 = this.queryParams.工事依頼No枝番
      this.showDialogZumenShiryoTenpuSabu = true
    },
    async regisValidate () {
      if (!this.sagyoForm.作業担当者) {
        this.getMsg('E040749').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作業担当者.focus()
        return false
      }
      if (!this.sagyoForm.担当者連絡先) {
        this.getMsg('E040686').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref担当者連絡先.focus()
        return false
      }
      if (!this.sagyoForm.報告日) {
        this.getMsg('E040750').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref報告日.focus()
        return false
      }
      if (moment(this.sagyoForm.報告日).isValid() === false) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref報告日.focus()
        return false
      }
      if (!this.onChangeHokokuBi(1)) {
        this.$refs.ref報告日.focus()
        return false
      }
      if (!this.sagyoForm.作業完了日開始日) {
        this.getMsg('E040751').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作業完了日開始日.focus()
        return false
      }
      if (moment(this.sagyoForm.作業完了日開始日).isValid() === false) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作業完了日開始日.focus()
        return false
      }
      if (!this.onChangeSagyoKanryoBi_Start(1)) {
        this.$refs.ref作業完了日開始日.focus()
        return false
      }
      if (!this.sagyoForm.作業完了日終了日) {
        this.getMsg('E040752').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作業完了日終了日.focus()
        return false
      }
      if (moment(this.sagyoForm.作業完了日終了日).isValid() === false) {
        this.getMsg('E040278').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作業完了日終了日.focus()
        return false
      }
      if (!this.onChangeSagyoKanryoBi_Last(1)) {
        this.$refs.ref作業完了日終了日.focus()
        return false
      }
      if (moment(this.sagyoForm.作業完了日開始日) > moment(this.sagyoForm.作業完了日終了日)) {
        this.getMsg('E040279').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref作業完了日開始日.focus()
        return false
      }
      if (!this.sagyoForm.施工内容) {
        this.getMsg('E040753').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref施工内容_特記事項等.focus()
        return false
      }
      // 20220920 add 半角が入力できないように修正。
      if (!this.base.isSimpleByte(this.sagyoForm.担当者連絡先)) {
        this.getMsg('E040460').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref担当者連絡先.focus()
        return false
      }
      // 20220920 add
      return true
    },
    getDataUpdate () {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼NO: this.queryParams.工事依頼No,
        // 発注サブNo: this.sagyoForm.発注サブNo ? this.sagyoForm.発注サブNo : 0,
        発注サブNo: this.queryParams.発注サブNo ? this.queryParams.発注サブNo : 0,
        // 検収回数: this.sagyoForm.検収回数 ? this.sagyoForm.検収回数 : 0,
        検収回数: this.queryParams.検収回数 ? this.queryParams.検収回数 : 0,
        実施日開始: this.sagyoForm.作業完了日開始日,
        実施時間開始: '',
        実施日終了: this.sagyoForm.作業完了日終了日,
        実施時間終了: '',
        実施内容: this.sagyoForm.施工内容,
        現地確認ｻｲﾝ: this.sagyoForm.テナント確認 ? '1' : '0',
        作業担当者: this.sagyoForm.作業担当者,
        担当者連絡先: this.sagyoForm.担当者連絡先,
        T作業報告日: this.sagyoForm.報告日,
        T完了報告フラグ: '1',
        業者コード枝番: this.userInfo.業者コード枝番,
        契約No: this.sagyoForm.契約No,
        契約年月: this.sagyoForm.契約年月
      }
    },
    // ハノイ側修正2022/08/30　STEP2_T　課題管理表№10：作業報告入力「2022/08/29仕様変更分」処理概要の＜一時保存＞＜完了報告＞の内容を追加修正 START
    async onTemporarilySaved (mode) {
      if (!await this.regisValidate()) {
        return false
      }
      let msg = (await this.getMsg('K040063')).data
      if (!await this.confirm(msg)) {
        return false
      }
      const data = {
        F052_作業報告明細マスタ: this.getDataUpdate(),
        mode,
        工事依頼No枝番: this.queryParams.工事依頼No枝番,
        業者コード: this.queryParams.業者コード,
        業者コード枝番: this.queryParams.業者コード枝番,
        F044_VERSION: this.sagyoForm.F044_VERSION,
        F052_VERSION: this.sagyoForm.F052_VERSION,
        負担区分: this.sagyoForm.負担区分,
        検収金額: this.sagyoForm.検収金額,
        依頼者事務所コード: this.sagyoForm.依頼者事務所コード
      }
      let res = {}
      await this.http
        .post('/api/Reafs_T/SagyoHokokuNyuryoku/Update', data, 'アクセスしています....')
        .then((response) => {
          res = response
        })
      if (res.status) {
        this.showSuccess(res.message)
        this.init()
        return true
      } else {
        this.showError(res.message)
        return false
      }
    },
    async onFinalReport (mode) {
      if (!await this.regisValidate()) {
        return false
      }
      let msg = (await this.getMsg('K040081')).data
      if (!await this.confirm(msg)) {
        return false
      }
      const data = {
        F052_作業報告明細マスタ: this.getDataUpdate(),
        mode,
        工事依頼No枝番: this.queryParams.工事依頼No枝番,
        業者コード: this.queryParams.業者コード,
        業者コード枝番: this.queryParams.業者コード枝番,
        F044_VERSION: this.sagyoForm.F044_VERSION,
        F052_VERSION: this.sagyoForm.F052_VERSION,
        負担区分: this.sagyoForm.負担区分,
        検収金額: this.sagyoForm.検収金額,
        依頼者事務所コード: this.sagyoForm.依頼者事務所コード,
        発注確定フラグ: this.sagyoForm.発注確定フラグ
      }
      let res = {}
      await this.http
        .post('/api/Reafs_T/SagyoHokokuNyuryoku/Update', data, 'アクセスしています....')
        .then((response) => {
          res = response
        })
      if (res.status) {
        let res2 = {}
        this.http
          .post('/api/Reafs_T/SagyoHokokuNyuryoku/完了報告書作成', data, 'アクセスしています....')
          .then((result) => {
            res2 = result
          })
        let baseURL = this.commonFunctionUI.getBaseURL('W')
        baseURL = baseURL + 'WD00520/SyuzenIraiSansyoShonin'
        for (let i = 0; i < res.data.length; i++) {
          let paramSet = {
            パラメータ1: this.queryParams.工事依頼No,
            パラメータ2: this.queryParams.発注サブNo,
            パラメータ3: this.queryParams.検収回数,
            パラメータ4: 'sagyo',
            PG: 'TD00101'
          }
          const resParam = await this.commonFunctionUI.setQueryParameter(paramSet)
          let linkUrl = baseURL + '?guid=' + resParam.data.guid
          linkUrl = encodeURI(linkUrl)
          res.data[i].url = [linkUrl]
          const resultMail = this.commonFunctionUI.CM00110SendMail(res.data[i])
        }

        this.showSuccess(res.message)
        this.init()
        // 2022/10/25 ファイルダウンロード処理は不要のためコメント
        // await this.print(res.data)
        return true
      } else {
        this.showError(res.message)
        return false
      }
    },
    async print (pdf) {
      if (!pdf) {
        this.getMsg('E010224').then(res => {
          this.showError(res.data)
        })
        return
      }
      // const linkSource = `data:${'application/pdf'};base64,${pdf}`
      // const downloadLink = document.createElement('a')
      // downloadLink.href = linkSource
      // downloadLink.download = 'Reafs-T_完了報告書.pdf'
      // downloadLink.click()
      await this.base.saveFileDownload(pdf, 'Reafs-T_完了報告書.pdf', 'application/pdf')
    },
    // ハノイ側修正2022/08/30　STEP2_T　課題管理表№10：作業報告入力「2022/08/29仕様変更分」処理概要の＜一時保存＞＜完了報告＞の内容を追加修正 END
    async onBack () {
      // this.$router.push({ name: 'TD00100' })
      this.$router.back()
    },
    async onReportCreate (mode) {
      if (!await this.regisValidate()) {
        return false
      }
      const data = {
        F052_作業報告明細マスタ: this.getDataUpdate(),
        mode,
        工事依頼No枝番: this.queryParams.工事依頼No枝番,
        業者コード: this.queryParams.業者コード,
        業者コード枝番: this.queryParams.業者コード枝番,
        F044_VERSION: this.sagyoForm.F044_VERSION,
        F052_VERSION: this.sagyoForm.F052_VERSION,
        負担区分: this.sagyoForm.負担区分,
        検収金額: this.sagyoForm.検収金額,
        依頼者事務所コード: this.sagyoForm.依頼者事務所コード,
        発注確定フラグ: this.sagyoForm.発注確定フラグ,
        報告書作成日: this.sagyoForm.報告書作成日
      }
        this.http
          .post('/api/Reafs_T/SagyoHokokuNyuryoku/完了報告書作成', data, 'アクセスしています....')
          .then((result) => {
            res2 = result
          })
        this.showSuccess(res2.message)
        this.init()
        // 2022/10/25 ファイルダウンロード処理は不要のためコメント
        // await this.print(res.data)
        return true
    },
    formatDate (date) {
      const d = new Date(date)
      let month = '' + (d.getMonth() + 1)
      let day = '' + d.getDate()
      const year = d.getFullYear()

      if (month.length < 2) { month = '0' + month }
      if (day.length < 2) { day = '0' + day }

      return [year, month, day].join('/')
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({ title,
        message,
        callback: function (a, b) {
        }})
    },
    showSuccess (message, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgInfo({ title,
        message,
        callback: function (a, b) {
        }})
    },
    confirm (message, title = this.$store.getters.getPageTitle()) {
      return new Promise((resolve, reject) => {
        this.MsgConf({ title,
          message,
          callback: function (a, b) {
            if (a === 'confirm') {
              resolve(true)
            } else {
              resolve(false)
            }
          }})
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
  // margin-bottom: 20px;
  &:last-child {
    margin-bottom: 0;
  }
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.home-container {
  padding: 10px;
  min-width: 1380px;
  height: 100%;
}
.sample-form-class {
  height: 100%;
}
.home-el-container {
  height: 100%;
}
.label {
  margin-bottom: 17px;
}
.comment{
  padding-left: 20px;
}
.commentContent{
  padding: 1px 0 1px 0;
}
.item_label {
  padding-left: 15px;
  max-width: 407px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.item_label_SekoNaiyoTokukijikoTo {
  height: 74px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 0px !important;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white;
  padding: 5px 0 0 0;
  box-sizing: border-box;
}
.item_input_SekoNaiyoTokukijikoTo {
  width: calc(100% - 120px);
}
.item_label_SashimodoshiJiyu {
  height: 74px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 0px !important;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white;
  padding: 20px 0 0 0;
  box-sizing: border-box;
}
.item_input_SashimodoshiJiyu {
  width: calc(100% - 120px);
}
.item_label_Tekiyo {
  height: 74px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 1px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white;
  padding: 20px 0 0 0;
  box-sizing: border-box;
}
.item_input_Tekiyo {
  width: calc(100% - 120px);
}
.home-el-header {
  // padding: 0 0 20px 0;
}
.home-el-main {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
}
.home-el-footer{
  padding: 10px 0 0 0;
}
.border-top {
    border-top: 1px solid #DCDFE6;
}
</style>
<style>
  .el-scrollbar__view {
      height: 100%;
  }
  .SagyoHokokuNyuryoku .el-textarea.is-disabled .el-textarea__inner {
    background-color: #d3d3d3;
    color: black;
  }
  .SagyoHokokuNyuryoku .el-form-item.narrow {
    margin-bottom: 0px !important;
  }
  .SagyoHokokuNyuryoku .el-form-item.narrow1 {
    margin-bottom: 1px !important;
  }
  .SagyoHokokuNyuryoku .item_label_Title {
    line-height: 30px;
    width: 120px;
    margin-bottom: 0px !important;
    text-align: left !important;
  }
</style>
