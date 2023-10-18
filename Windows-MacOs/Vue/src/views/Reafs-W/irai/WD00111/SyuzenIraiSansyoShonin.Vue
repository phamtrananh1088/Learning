<template>
  <div class="home-container SyuzenIraiSansyoShonin">
    <el-form
      :model="iraiForm"
      :rules="rules"
      ref="refIraiForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk = "true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row>
            <el-col :span="8">
              <div style="display: flex">
                <el-form-item label="営業店" prop="営業店コード" class="is-required 営業店">
                  <eigyoten
                    required
                    :eigyotenCd="iraiForm.店コード"
                    @update:eigyotenCd="(v)=> { this.iraiForm.店コード = v }"
                    :eigyotenBango="iraiForm.店コード枝番"
                    @update:eigyotenBango="(v)=> { this.iraiForm.店コード枝番 = v }"
                    :appendlabelText="iraiForm.営業店名"
                    :disabled="画面入力制御disabled.営業店コード"
                    @backData="reafsmodalBackEigyoten"
                    ref=ref営業店
                  >
                  </eigyoten>
                </el-form-item>
              </div>
            </el-col>
            <el-form
              :model="searchForm"
              ref="searchForm"
              label-width="120px"
              class="search-form-class"
              :hide-required-asterisk="true"
            >
              <el-col :span="4">
                <el-form-item label="工事依頼№" prop="koujiiraiNo">
                  <div style="display: flex">
                    <reafsinputtext
                      maxlength="2"
                      v-model="searchForm.koujiiraiNo"
                      :width="'48px'"
                      ref="koujiiraiNo"
                      @blur="onBlurKoujiiraiNoNendo"
                    >
                    </reafsinputtext>
                    <reafsinputtext
                      maxlength="5"
                      v-model="searchForm.koujiiraiNo1"
                      :width="'72px'"
                      ref="koujiiraiNo1"
                      @blur="onBlurKoujiiraiNoRenban"
                    >
                    </reafsinputtext>
                  </div>
                </el-form-item>
              </el-col>
              <el-col style="width: 6%; padding-left: 20px; padding-top: 2px;">
                <el-button type="primary" size="mini"
                 :disabled="!isKojiIraiInput"
                 @click="onSearch" :loading="loading">検索</el-button>
              </el-col>
            </el-form>
            <el-col style="width: 22%;">
              <el-form-item
                label="進捗（本体）"
                prop="shinchokuHontai"
                class="is-readonly"
              >
                <reafsinputtext
                  v-model="iraiForm.進捗_本体"
                  maxlenth="10"
                  disabled
                  :width="'172px'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="進捗（発注）"
                prop="shinchokuHatchu"
                class="is-readonly"
              >
                <reafsinputtext
                  v-model="iraiForm.進捗_発注"
                  maxlenth="10"
                  disabled
                  :width="'172px'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="物件名" prop="bukken" class="is-readonly">
                <div style="display: flex">
                  <reafsinputtext
                    v-model="iraiForm.物件コード"
                    maxlength="8"
                    disabled
                    :width="'120px'">
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件名"
                    maxlength="50"
                    disabled
                    :width="'100%'">
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
            <el-col style="width: 22.7%;">
              <el-form-item
                label="物件区分"
                prop="bukkenKbn"
                class="is-readonly"
              >
                <reafsinputtext
                  v-model="iraiForm.物件区分"
                  maxlength="20"
                  disabled
                  :width="'120px'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item
                label="物件住所"
                prop="bukkenAddress"
                class="is-readonly"
              >
                <div style="display: flex">
                  <reafsinputtext
                    v-model="iraiForm.物件住所Cd"
                    maxlength="8"
                    disabled
                    :width="'110px'">
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件住所Nm"
                    maxlength="100"
                    disabled
                    :width="'361px'">
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-header>
        <el-main class="home-el-main">
          <!-- <el-row :gutter="20"> -->
          <el-row>
            <!-- 共通コントロール　サンプル -->
            <el-col :span="8">
              <el-form-item label="工事区分" prop="工事区分" class="is-required">
                <el-select
                  required
                  v-model="iraiForm.工事区分"
                  :disabled="画面入力制御disabled.工事区分"
                  @change="selectKojiKubun"
                  ref="ref工事区分"
                  style="width: 80%;"
                >
                  <el-option
                    v-for="item in 工事区分value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
                <el-button class="btnHelp" @click="onHelp">？</el-button>
                <kojikubunsetsumeiichiran :dialogVisible.sync="showDialogKojikubunsetsumeiichiran"></kojikubunsetsumeiichiran>
              </el-form-item>
              <el-form-item label="大区分" prop="大区分" class="is-required">
                <el-select
                  required
                  v-model="iraiForm.大区分"
                  :disabled="画面入力制御disabled.大区分"
                  @change="selectDaiKubun"
                  ref="ref大区分"
                  style="width: 80%;"
                >
                  <el-option
                    v-for="item in 大区分value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="中区分" prop="中区分" class="is-required">
                <el-select
                  required
                  v-model="iraiForm.中区分"
                  :disabled="画面入力制御disabled.中区分"
                  @change="selectChuKubun"
                  ref="ref中区分"
                  style="width: 80%;"
                >
                  <el-option
                    v-for="item in 中区分value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="小区分" prop="小区分" class="is-required">
                <el-select
                  required
                  v-model="iraiForm.小区分"
                  :disabled="画面入力制御disabled.小区分"
                  ref="ref小区分"
                  style="width: 80%;"
                >
                  <el-option
                    v-for="item in 小区分value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="依頼状況" prop="依頼状況" class="is-required">
                <el-select
                  required
                  v-model="iraiForm.依頼状況"
                  :disabled="画面入力制御disabled.依頼状況"
                  style="width: 100%"
                  ref="ref依頼状況"
                >
                  <el-option
                    v-for="item in 依頼状況value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="SGR担当部署" prop="SGR担当部署" class="is-required">
                <el-select
                  required
                  v-model="iraiForm.SGR担当部署"
                  :disabled="画面入力制御disabled.SGR担当部署"
                  style="width: 75%"
                  ref="refSGR担当部署"
                >
                  <el-option
                    v-for="item in SGR担当部署value"
                    :key="item.名称コード"
                    :label="item.担当部署"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <div style="width: 1px; height: 92px"></div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="依頼者" prop="依頼者コード" class="is-required">
                <div class="依頼者">
                  <opensearch
                    required
                    v-model="iraiForm.依頼者コード"
                    :disabled="画面入力制御disabled.依頼者コード"
                    :appendlabelText="iraiForm.依頼者名"
                    :append="'append'"
                    :maxlength="'7'"
                    :path="'reafsmodalSearchShain'"
                    :customValidate= 2
                    @backData="reafsmodalBackIraiSha"
                    ref="ref依頼者コード"
                  >
                  </opensearch>
                </div>
              </el-form-item>
              <!-- ハノイ側修正2022/08/30　STEP2_W　課題管理表№69：設計書「修正履歴」シートの「2022/08/30仕様変更分」「依頼者代理用の検索画面機能を追加してください。」Start -->
              <el-form-item label="依頼者代理" prop="iraishaDairi">
                <div class="依頼者代理">
                  <opensearch
                  v-model="iraiForm.依頼者代理コード"
                  :disabled="画面入力制御disabled.依頼者代理コード"
                  :appendlabelText="iraiForm.依頼者代理名"
                  :append="'append'"
                  :maxlength="'7'"
                  :path="'reafsmodalSearchShain'"
                  :customValidate= 2
                  @backData="reafsmodalBackIraiShadairi"
                  ref="ref依頼者代理コード"
                  >
                  </opensearch>
                </div>
              </el-form-item>
              <!-- ハノイ側修正2022/08/30　STEP2_W　課題管理表№69：設計書「修正履歴」シートの「2022/08/30仕様変更分」「依頼者代理用の検索画面機能を追加してください。」End -->
            </el-col>
            <el-col :span="6">
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者所属部署名"
                  :width="'320px'"
                  disabled="disabled"
                >
                </reafsinputtext>
              </el-form-item>
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者代理所属部署名"
                  :width="'320px'"
                  disabled="disabled"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者役職名"
                  :width="'150px'"
                  disabled="disabled"
                >
                </reafsinputtext>
              </el-form-item>
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者代理役職名"
                  :width="'150px'"
                  disabled="disabled"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="連絡先"
                prop="依頼者連絡先"
                class="is-required"
              >
                <reafsinputtext
                  required
                  type="tel"
                  maxlength="20"
                  v-model="iraiForm.依頼者連絡先"
                  :disabled="画面入力制御disabled.依頼者連絡先"
                  :width="'150px'"
                  ref="ref依頼者連絡先"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item
                label="連絡先"
                prop="依頼者代理連絡先"
              >
                <reafsinputtext
                  type="tel"
                  maxlength="20"
                  v-model="iraiForm.依頼者代理連絡先"
                  :disabled="画面入力制御disabled.依頼者代理連絡先"
                  :width="'150px'"
                  ref="ref依頼者代理連絡先"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="14">
              <el-form-item label="依頼コメント">
                <reafsinputtext
                  maxlength="50"
                  v-model="iraiForm.依頼コメント"
                  :disabled="画面入力制御disabled.依頼コメント"
                  :width="'100%'"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item
                label="対象箇所"
                prop="対象箇所"
                class="is-required"
              >
                <reafsinputtext
                  required
                  maxlength="50"
                  v-model="iraiForm.対象箇所"
                  :disabled="画面入力制御disabled.対象箇所"
                  :width="'100%'"
                  ref="ref対象箇所"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item label="メーカー名">
                <reafsinputtext
                  maxlength="50"
                  v-model="iraiForm.メーカー名"
                  :disabled="画面入力制御disabled.メーカー名"
                  :width="'100%'"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item class="is-required is-area" prop="依頼内容_状況"
               label-width="0px"
               >
                <div style="display: flex">
                  <label
                    class="el-form-item__label item_label_iraiNaiyo_fuguaiJokyo"
                    >依頼内容／<br />不具合状況</label
                  >
                  <el-input
                    required
                    class="is-required item_input_iraiNaiyo_fuguaiJokyo"
                    maxlength="200"
                    type="textarea"
                    resize="none"
                    :rows="3"
                    v-model="iraiForm.依頼内容_状況"
                    :disabled="画面入力制御disabled['依頼内容／不具合状況']"
                    ref="iraiNaiyo_fuguaiJokyo"
                  >
                  </el-input>
                </div>
              </el-form-item>
            </el-col>
            <el-col class="comment" style="width: 31%;">
              <br />
              <br />
              <br />
              <br />
              <br />
              <el-form-item label="添付一覧" label-width="100%">
              </el-form-item>
              <div style="padding-bottom: 20px">
                <div class="commentContent">
                  <label class="item_label">1　　{{ iraiForm.部位コメント1 }}</label>
                </div>
                <div>
                  <label class="item_label">2　　{{ iraiForm.部位コメント2 }}</label>
                </div>
                <div>
                  <label class="item_label">3　　{{ iraiForm.部位コメント3 }}</label>
                </div>
                <div>
                  <label class="item_label">4　　{{ iraiForm.部位コメント4 }}</label>
                </div>
              </div>
              <br />
              <br />
            </el-col>
            <el-col style="width: 10%;">
              <br />
              <br />
              <br />
              <br />
              <br /><br />
              <br />
              <br /><br />
              <el-button
                @click="onViewAttachRegis"
                style="float: right"
                :disabled="画面入力制御disabled.btn添付登録参照">添付登録参照</el-button>
                 <screenPopup
                  src="/Reafs_W/IraiTenpuTouroku"
                  :params="paramsIraiTenpuTouroku"
                  :dialogVisible.sync="showDialogIraiTenpuTouroku"
                  @closePopupEvent="IraiTenpuTourokuClose"></screenPopup>
            </el-col>
            <el-col :span="7">
              <el-form-item label="中止保留区分">
                <el-select
                  v-model="iraiForm.中止保留区分"
                  :disabled="画面入力制御disabled.中止保留区分"
                  @change="selectChushiHoryuKubun"
                  ref="ref中止保留区分"
                >
                  <el-option
                    v-for="item in 中止保留区分value"
                    :key="item.名称コード"
                    :label="item.名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="中止保留年月日" prop="中止保留年月日">
                <el-date-picker
                  :required="iraiForm.中止保留区分 === '1' || iraiForm.中止保留区分 === '2' ? true:false"
                  maxlength="10"
                  type="date"
                  placeholder=""
                  v-model="iraiForm.中止保留年月日"
                  :disabled="画面入力制御disabled.中止保留年月日"
                  style="width: 135px"
                  format="yyyy/MM/dd"
                  value-format="yyyy/MM/dd"
                  ref="ref中止保留年月日"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="中止保留事由" prop="中止保留事由">
                <reafsinputtext
                  :required="iraiForm.中止保留区分 === '1' || iraiForm.中止保留区分 === '2' ? true:false"
                  maxlength="200"
                  v-model="iraiForm.中止保留事由"
                  :disabled="画面入力制御disabled.中止保留事由"
                  :width="'100%'"
                  ref="ref中止保留事由"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
             <el-col :span="24">
               <el-form-item label="変更コメント">
                 <reafsinputtext
                  maxlength="50"
                  v-model="iraiForm.変更コメント"
                  :disabled="画面入力制御disabled.変更コメント"
                  :width="'45%'"
                  ref="ref変更コメント"
                />
              </el-form-item>
             </el-col>
             <el-col :span="24">
               <el-form-item label="否認・差戻事由">
                 <reafsinputtext
                  maxlength="50"
                  v-model="iraiForm.否認差戻事由"
                  :disabled="画面入力制御disabled.否認差戻事由"
                  :width="'45%'"
                  ref="ref否認_差戻事由"
                />
              </el-form-item>
             </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="15" style="width: 60%"><div class="grid-content">&nbsp;</div></el-col>
            <el-col :span="2" style="width: 10%">
              <el-button :loading="btnLoading.依頼否認"
              :disabled="画面入力制御disabled.btn依頼否認"
              @click="onRegister(mode.依頼否認)">依頼否認</el-button>
            </el-col>
            <el-col :span="2" style="width: 10%">
              <el-button
                @click="onRegister(mode.依頼差戻)"
                :disabled="画面入力制御disabled.btn依頼差戻"
                 :loading="btnLoading.依頼差戻">依頼差戻</el-button>
            </el-col>
            <el-col :span="2" style="width: 10%">
              <el-button
                id="ref依頼承認"
                @click="onRegister(mode.依頼承認)"
                :disabled="画面入力制御disabled.btn依頼承認"
                 :loading="btnLoading.依頼承認">依頼承認</el-button>
            </el-col>
            <el-col :span="2">
              <el-button @click="onBack" :loading="loadingBack" style="float: right">戻る</el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafsmodal from '@/components/modal/Modal.vue'
import eigyoten from '@/components/basic/EigyoTen.vue'
import opensearch from '@/components/basic/openSearch.vue'
import kojikubunsetsumeiichiran from '@/components/modal/KojiKubunSetsumeiIchiran.vue'
import screenPopup from '../../common/ScreenPopup/ScreenPopup.vue'
import pick from 'lodash/pick'
import isEqual from 'lodash/isEqual'

var $select工事区分value
var $select大区分value
var $select中区分value
var $select小区分value
var $select依頼状況value
var $selectSGR担当部署value
var $select中止保留区分value

export default {
  activated () {
    if (this.$store.getters.getKeepAliveFlag() == 0 && this.isMounted == false) {
      this.$store.commit('setKeepAliveFlag', 1)
      this.initOnMounted()
    }
    this.isMounted = false
  },
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafsmodal,
    eigyoten,
    opensearch,
    kojikubunsetsumeiichiran,
    screenPopup
  },
  data () {
    // ハノイ側修正2022/05/17　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」Start
    var validate営業店コード = (rule, value, callback) => {
      if (this.iraiForm.店コード === '' || this.iraiForm.店コード枝番 === '') {
        this.getMsg('E040005').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    // ハノイ側修正2022/05/17　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」End

    return {
      isMounted: false,
      isInit: true,
      loading: false,
      loadingBack: false,
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: '',
        UPDATE_HOST: ''
      },
      form: {
        name: '',
        nameSearch: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      searchForm: {
        koujiiraiNo: '',
        koujiiraiNo1: ''
      },
      rules: {
        営業店コード: [
          { validator: validate営業店コード, trigger: 'blur' }
        ],
        工事区分: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.工事区分) {
              if (this.isInit === false) {
                if (value === 'ー') {
                  this.getMsg('E040015').then((response) => {
                    callback(new Error(response.data))
                  })
                }
              }
            }
          },
          trigger: 'change' }
        ],
        大区分: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.大区分) {
              if (this.isInit === false) {
                if (value === 'ー') {
                  this.getMsg('E040016').then((response) => {
                    callback(new Error(response.data))
                  })
                }
              }
            }
          },
          trigger: 'change' }
        ],
        中区分: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.中区分) {
              if (this.isInit === false) {
                if (value === 'ー') {
                  this.getMsg('E040017').then((response) => {
                    callback(new Error(response.data))
                  })
                }
              }
            }
          },
          trigger: 'change' }
        ],
        小区分: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.小区分) {
              if (this.isInit === false) {
                if (value === 'ー') {
                  this.getMsg('E040018').then((response) => {
                    callback(new Error(response.data))
                  })
                }
              }
            }
          },
          trigger: 'change' }
        ],
        依頼者コード: [
          { validator: async (rule, value, callback) => {
            if (value === '') {
              this.getMsg('E040019').then((response) => {
                callback(new Error(response.data))
              })
            }
            await this.check依頼者コードCallBack(callback)
          },
          trigger: 'blur' }
        ],
        依頼者連絡先: [
          { validator: (rule, value, callback) => {
            if (value === '') {
              this.getMsg('E040669').then((response) => {
                callback(new Error(response.data))
              })
            } else if (!this.base.isSimpleByte(value)) {
              this.getMsg('E040460').then((response) => {
                callback(new Error(response.data))
              })
            }
          },
          trigger: 'blur' }
        ],
        依頼者代理連絡先: [
          { validator: (rule, value, callback) => {
            if (value !== '' && !this.base.isSimpleByte(value)) {
              this.getMsg('E040460').then((response) => {
                callback(new Error(response.data))
              })
            }
          },
          trigger: 'blur' }
        ],
        対象箇所: [
          { validator: (rule, value, callback) => {
            if (value === '') {
              this.getMsg('E040661').then((response) => {
                callback(new Error(response.data))
              })
            }
          },
          trigger: 'blur' }
        ],
        依頼内容_状況: [
          { validator: (rule, value, callback) => {
            if (value === '') {
              this.getMsg('E040034').then((response) => {
                callback(new Error('　　　　　　　　　　　　　　　' + response.data))
              })
            }
          },
          trigger: 'blur' }
        ],
        依頼状況: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.依頼状況) {
              if (this.isInit === false) {
                if (value === 'ー' && (this.iraiForm.工事区分 || '').charAt(0) !== '3') {
                  this.getMsg('E040035').then((response) => {
                    callback(new Error(response.data))
                  })
                }
              }
            }
          },
          trigger: 'change' }
        ],
        SGR担当部署: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.SGR担当部署) {
              if (this.isInit === false) {
                if (value === 'ー') {
                  this.getMsg('E040670').then((response) => {
                    callback(new Error(response.data))
                  })
                }
              }
            }
          },
          trigger: 'change' }
        ],
        中止保留年月日: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.中止保留年月日) {
              if (this.isInit === false) {
                if ((value || '') === '' && (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2')) {
                  this.getMsg('E040003').then((response) => {
                    callback(new Error(response.data))
                  })
                }
              }
            }
          },
          trigger: 'change' }
        ],
        中止保留事由: [
          { validator: (rule, value, callback) => {
            if (!this.画面入力制御disabled.中止保留事由) {
              if (value === '' && (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2')) {
                this.getMsg('E040358').then((response) => {
                  callback(new Error(response.data))
                })
              }
            }
          },
          trigger: 'blur' }
        ],
        koujiiraiNo: [
          {
            type: 'number',
            message: '数値を入力してください',
            trigger: 'blur'
          }
        ],
        koujiiraiNo1: [
          {
            type: 'number',
            message: '数値を入力してください',
            trigger: 'blur'
          }
        ]
      },
      m001Info: {},
      画面入力制御disabled: {
        営業店コード: false,
        工事区分: false,
        大区分: false,
        中区分: false,
        小区分: false,
        依頼者コード: false,
        依頼者連絡先: false,
        依頼者代理コード: false,
        依頼者代理連絡先: false,
        依頼コメント: false,
        対象箇所: false,
        メーカー名: false,
        '依頼内容／不具合状況': false,
        依頼状況: false,
        SGR担当部署: false,
        中止保留区分: true,
        中止保留年月日: true,
        中止保留事由: true,
        変更コメント: false,
        否認差戻事由: false,
        btn添付登録参照: false,
        btn依頼否認: true,
        btn依頼差戻: true,
        btn依頼承認: true
      },
      画面入力制御disabledInit: {},
      画面入力制御disabledOrg: {},
      画面入力制御: {},
      showDialog: false,
      showDialogKojikubunsetsumeiichiran: false,
      activeIndex: '1',
      selectIndex: '1',
      iraiForm: {
        営業店コード: '',
        店コード: '',
        店コード枝番: '',
        営業店名: '',
        '工事依頼№': '',
        '工事依頼№2': '',
        進捗_本体: '',
        進捗_発注: '',
        物件コード: '',
        物件名: '',
        物件区分: '',
        物件住所Cd: '',
        物件住所Nm: '',
        工事区分: '',
        大区分: '',
        中区分: '',
        小区分: '',
        依頼者コード: '',
        依頼者名: '',
        依頼者所属部署名: '',
        依頼者役職名: '',
        依頼者連絡先: '',
        依頼者代理コード: '',
        依頼者代理名: '',
        依頼者代理所属部署名: '',
        依頼者代理連絡先: '',
        依頼コメント: '',
        対象箇所: '',
        メーカー名: '',
        依頼内容_状況: '',
        依頼状況: '',
        SGR担当部署: '',
        中止保留区分: '',
        中止保留年月日: '',
        中止保留事由: '',
        部位コメント1: '',
        部位コメント2: '',
        部位コメント3: '',
        部位コメント4: '',
        進捗コード_本体: '',
        '依頼入力済※1': '',
        依頼承認チェック: '',
        依頼No: '',
        変更コメント: '',
        否認差戻事由: ''
      },
      iraiFormInit: {},
      iraiFormSearched: {},
      工事区分value: [],
      大区分value: [],
      中区分value: [],
      小区分value: [],
      依頼状況value: [],
      SGR担当部署value: [],
      中止保留区分value: [],
      名称取得方法17: [],
      paramsIraiTenpuTouroku: {
        添付種類: '901',
        工事依頼No: ''
      },
      showDialogIraiTenpuTouroku: false,
      mode: Object.freeze({'依頼否認': 1, '依頼差戻': 2, '依頼承認': 3}),
      btnLoading: {
        依頼否認: false,
        依頼差戻: false,
        依頼承認: false
      },
      // ハノイ側修正2022/05/27　STEP2_W　課題管理表№4：設計書「修正履歴」シートの「2022/05/20仕様変更分」「依頼承認ボタン押下時の工事依頼書の処理を追加」START
      paramsKojiIraiSho: {
        工事依頼No: '',
        page: ''
      },
      // ハノイ側修正2022/05/27　STEP2_W　課題管理表№4：設計書「修正履歴」シートの「2022/05/20仕様変更分」「依頼承認ボタン押下時の工事依頼書の処理を追加」END
      sessionPageDataBack: null
    }
  },
  watch: {
    'iraiForm.中止保留年月日' (n, o) {
    }
  },
  computed: {
    searchData () {
      return {
        ログイン会社コード: this.userInfo.会社コード,
        工事依頼No: this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1
      }
    },
    isKojiIraiInput () {
      return (this.searchForm.koujiiraiNo.trim() + this.searchForm.koujiiraiNo1.trim()) !== ''
    }
  },
  created () {
    $select工事区分value = this
    $select大区分value = this
    $select中区分value = this
    $select小区分value = this
    $select依頼状況value = this
    $selectSGR担当部署value = this
    $select中止保留区分value = this
    this.init()
  },
  mounted () {
    this.isMounted = true
    this.initOnMounted()
  },
  methods: {
    initOnMounted () {
      // ハノイ側修正2022/07/28　STEP2_W　課題管理表№14：設計書「修正履歴」シートの「2022/07/14仕様変更分」「№15,16,18修正  №27追加」Start
      this.paramsKojiIraiSho = {
        工事依頼No: this.$route.query.工事依頼No,
        page: this.$route.query.page
      }

      if (this.$route.query && this.$route.query.page) {
        this.searchForm.koujiiraiNo = this.$route.query.工事依頼No.substring(0, 2)
        this.searchForm.koujiiraiNo1 = this.$route.query.工事依頼No.substring(2)
        this.onSearch()
      }
    // ハノイ側修正2022/07/28　STEP2_W　課題管理表№14：設計書「修正履歴」シートの「2022/07/14仕様変更分」「№15,16,18修正  №27追加」End
    },
    async print () {
      const data = {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1,
        店コード: this.iraiForm.店コード,
        店コード枝番: this.iraiForm.店コード枝番,
        添付種類: '101' // ハノイ側修正2022/12/12　STEP2_W　課題管理表№245：設計書「修正履歴」シートの「2022/12/12仕様変更分」「遷移元にWD00311を追加。その場合の帳票コードも追記。」
      }
      await this.http
        .post('/api/Reafs_W/KojiIraiShoService/ExportReport', data, 'アクセスしています....', {
          responseType: 'blob'
        })
        .then(async blob => {
          if (blob.size === 0) {
            this.getMsg('E010224').then(res => {
              this.showError(res.data)
            })
          }
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: '工事依頼書.pdf'
          // })

          // const fileStream = await fileHandle.createWritable()
          // await fileStream.write(blob)
          // await fileStream.close()

          // 2022/10/05 ファイルダウンロード処理は不要のためコメント
          // await this.base.saveFileDownload(blob, '工事依頼書.pdf')

          // 2022/10/01 TCC修正 工事依頼書のPDFダウンロードは不要の為コメント
          // this.getMsg('M040020').then(response => {
          //   this.showSuccess(response.data)
          // })
        })
        .catch(ex => console.log(ex))
        .finally(() => {
        })
        .catch(ex => console.log(ex))
    },
    afterInit () {
      Object.assign(this.画面入力制御disabledOrg, this.画面入力制御disabled)
      Object.assign(this.画面入力制御disabledInit, this.画面入力制御disabled)
      Object.assign(this.iraiFormInit, this.iraiForm)
    },
    reInit () {
      let temp画面入力制御disabled = {}
      let tempiraiForm = {}

      this.isInit = true
      Object.assign(temp画面入力制御disabled, this.画面入力制御disabledInit)
      this.画面入力制御disabled = temp画面入力制御disabled

      Object.assign(tempiraiForm, this.iraiFormInit)
      this.iraiForm = tempiraiForm
    },
    init () {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.会社コード = userLogIn.会社コード

      // Get Dropdown
      this.http
        .get('/api/Reafs_W/Irai/WD00111/GetStartInfo', 'アクセスしています....')
        .then((response) => {
          this.m001Info = response.data.m001Info
          // Get Dropdown工事区分
          $select工事区分value.工事区分value = response.data.名称取得方法07
          this.iraiForm.工事区分 = 'ー'
          // Get Dropdown大区分
          $select大区分value.大区分value = response.data.名称取得方法08
          this.iraiForm.大区分 = 'ー'
          this.iraiForm.中区分 = 'ー'
          this.iraiForm.小区分 = 'ー'
          // Get Dropdown依頼状況
          $select依頼状況value.依頼状況value = response.data.名称取得方法13
          this.iraiForm.依頼状況 = 'ー'
          // Get DropdownSGR担当部署
          $selectSGR担当部署value.SGR担当部署value = null
          this.iraiForm.SGR担当部署 = '244-994500-999-999-9999'
          // Get Dropdown中止保留区分
          $select中止保留区分value.中止保留区分value = response.data.名称取得方法15
          this.名称取得方法17 = response.data.名称取得方法17
          this.iraiForm.中止保留区分 = '0'
          this.$nextTick(() => {
            this.isInit = false
          })
          this.afterInit()
          const pageData = this.$store.getters.getPageData()
          if (pageData && pageData.keepSearchForm) {
            this.searchForm.koujiiraiNo = pageData.SyuzenIraiSansyoShonin.koujiiraiNo
            this.searchForm.koujiiraiNo1 = pageData.SyuzenIraiSansyoShonin.koujiiraiNo1
          } else if (pageData &&
          pageData.SyuzenIraiSansyoShonin &&
          pageData.SyuzenIraiSansyoShonin.koujiiraiNo &&
          pageData.SyuzenIraiSansyoShonin.koujiiraiNo1 &&
          pageData.依頼No) {
            this.searchForm.koujiiraiNo = pageData.SyuzenIraiSansyoShonin.koujiiraiNo
            this.searchForm.koujiiraiNo1 = pageData.SyuzenIraiSansyoShonin.koujiiraiNo1
            this.onSearch()
          }
        })

      this.画面入力制御disabled.btn添付登録参照 = true
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({ title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof (option.callback) === 'function') {
            option.callback()
          }
        }})
      // this.$message.error({
      //   message,
      //   onClose: this.hideLoading
      // })
    },
    showSuccess (message, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgInfo({ title,
        message,
        callback: function (a, b) {
          self.hideLoading()
        }})
    },
    hideLoading () {
      this.loading = false
      // eslint-disable-next-line no-return-assign
      Object.keys(this.btnLoading).forEach(v => this.btnLoading[v] = false)
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
    set画面入力制御 (isScreenDisplayTiming = true) {
      const 進捗コード_本体 = this.画面入力制御 ? this.画面入力制御.進捗コード_本体 : ''
      if ((進捗コード_本体 || '').substring(0, 2) === '01' || this.iraiForm.中止保留区分 === '0') {
        // eslint-disable-next-line no-return-assign
        Object.keys(this.画面入力制御disabled).forEach((v) => {
          if (!v.includes('btn')) {
            this.画面入力制御disabled[v] = false
          }
        })
      } else {
        // eslint-disable-next-line no-return-assign
        Object.keys(this.画面入力制御disabled).forEach((v) => {
          if (!v.includes('btn')) {
            this.画面入力制御disabled[v] = true
          }
        })
      }

      if (this.iraiForm.工事区分 && this.iraiForm.工事区分 !== 'ー' && this.iraiForm.工事区分.substring(0, 1) === '4') {
        this.selectKojiKubun(this.iraiForm.工事区分)
      }
    },
    get進捗 (中止保留区分, 進捗コード_本体, 進捗コード_発注) {
      let s進捗内容 = ''
      if (中止保留区分 === '1' || 中止保留区分 === '2') {
        s進捗内容 = '中止・保留時'
      } else if (進捗コード_本体 === '010') {
        s進捗内容 = '依頼入力済'
        const condition1 = this.iraiForm['依頼入力済※1']
        if (condition1 === true) {
          s進捗内容 = '依頼入力済※1'
        }
      } else if (進捗コード_本体 === '017') {
        s進捗内容 = '依頼取下'
      } else if (進捗コード_本体 === '018') {
        s進捗内容 = '依頼差戻'
      } else if (進捗コード_本体 >= '020') {
        s進捗内容 = '－'
      }

      return s進捗内容
    },
    get進捗Condition2 (isScreenDisplayTiming) {
      return !((isScreenDisplayTiming && this.iraiForm.中止保留区分 === '2'))
    },
    checkDspbtn () {
      if (this.iraiForm.依頼承認チェック === true) {
        this.画面入力制御disabled.btn依頼否認 = false
        this.画面入力制御disabled.btn依頼差戻 = false
        this.画面入力制御disabled.btn依頼承認 = false
      } else {
        this.画面入力制御disabled.btn依頼否認 = true
        this.画面入力制御disabled.btn依頼差戻 = true
        this.画面入力制御disabled.btn依頼承認 = true
      }

      this.画面入力制御disabled.btn添付登録参照 = false
    },
    async checkKojiExist () {
      return new Promise(async (resolve) => {
        let checkExist = false
        await this.http
          .get('/api/Reafs_W/Irai/WD00111/CheckF100Exist', { 工事依頼No: this.searchData.工事依頼No })
          .then((response) => {
            checkExist = response.data.isF100Exist
          })

        if (!checkExist) {
          this.getMsg('E000014').then((response) => {
            this.showError(response.data, {callback: () => {
              resolve(false)
            }})
          })
          this.$refs.koujiiraiNo.focus()
        } else {
          resolve(true)
        }
      })
    },
    async onSearch () {
      this.loading = true
      this.savePageSearchData()
      if (this.checkKoji() === false) {
        return false
      }

      if (!await this.checkKojiExist()) {
        this.savePageSearchData(true)
        return false
      }

      var getArrayValue = (arr, index, key) => {
        if (!arr || !arr[index] || !arr[index][key]) {
          return ''
        }
        return arr[index][key]
      }

      this.http
        .get('/api/Reafs_W/Irai/WD00111/GetPageData', this.searchData, 'アクセスしています....')
        .then(async (response) => {
          this.isInit = true
          this.loading = false
          // ハノイ側修正2022/09/26　STEP2_W　課題管理表№117：設計書「修正履歴」シートの「2022/09/20仕様変更分」参照権限チェック追加 Start
          if (!await this.check営業店OnSearch(response.data)) {
            return false
          }
          // ハノイ側修正2022/09/26　STEP2_W　課題管理表№117：設計書「修正履歴」シートの「2022/09/20仕様変更分」参照権限チェック追加 End

          // Get Dropdown工事区分
          $select工事区分value.工事区分value = response.data.名称取得方法07
          // Get Dropdown大区分
          $select大区分value.大区分value = response.data.名称取得方法08
          // Get Dropdown中区分
          $select中区分value.中区分value = response.data.名称取得方法09
          // Get Dropdown小区分
          $select小区分value.小区分value = response.data.名称取得方法10
          // Get Dropdown依頼状況
          $select依頼状況value.依頼状況value = response.data.名称取得方法13
          // Get DropdownSGR担当部署
          $selectSGR担当部署value.SGR担当部署value = response.data.ddlSGR担当部署
          // Get Dropdown中止保留区分
          $select中止保留区分value.中止保留区分value = response.data.名称取得方法15

          response.data.対象箇所 = response.data.不具合場所
          response.data.中止保留区分 = response.data.継続区分
          response.data.中止保留年月日 = response.data.中止年月日
          response.data.SGR担当部署 = !response.data.SGR担当事務所コード ? 'ー' : response.data.SGR担当事務所コード +
          '-' + response.data.SGR担当営業所コード +
          '-' + response.data.SGR担当部コード +
          '-' + response.data.SGR担当課コード +
          '-' + response.data.SGR担当係コード

          if (response.data.工事区分.trim() === '') {
            response.data.工事区分 = 'ー'
          }
          if (response.data.大区分.trim() === '') {
            response.data.大区分 = 'ー'
          }
          if (response.data.中区分.trim() === '') {
            response.data.中区分 = 'ー'
          }
          if (response.data.小区分.trim() === '') {
            response.data.小区分 = 'ー'
          }
          if (response.data.依頼状況.trim() === '') {
            response.data.依頼状況 = 'ー'
          }
          if (response.data.中止保留区分.trim() === '') {
            response.data.中止保留区分 = '0'
          }

          this.iraiForm = response.data
          // this.iraiForm.対象箇所 = response.data.不具合場所
          // this.iraiForm.中止保留区分 = response.data.継続区分
          // this.iraiForm.中止保留年月日 = response.data.中止年月日

          // ハノイ側修正2022/09/05　STEP2_W　課題管理表№76：設計書「修正履歴」シートの「2022/09/01仕様変更分」添付一覧のNoはF091の登録が無い場合でも、1～4を常に表示してください。 Start
          var lst部位コメント = response.data.部位コメント.values()
          for (let item of lst部位コメント) {
            if (item.ファイルNo === 1) {
              this.iraiForm.部位コメント1 = item.部位コメント
            } else if (item.ファイルNo === 2) {
              this.iraiForm.部位コメント2 = item.部位コメント
            } else if (item.ファイルNo === 3) {
              this.iraiForm.部位コメント3 = item.部位コメント
            } else if (item.ファイルNo === 4) {
              this.iraiForm.部位コメント4 = item.部位コメント
            }
          }
          // this.iraiForm.部位コメント1 = getArrayValue(response.data.部位コメント, 0, '部位コメント')
          // this.iraiForm.部位コメント2 = getArrayValue(response.data.部位コメント, 1, '部位コメント')
          // this.iraiForm.部位コメント3 = getArrayValue(response.data.部位コメント, 2, '部位コメント')
          // this.iraiForm.部位コメント4 = getArrayValue(response.data.部位コメント, 3, '部位コメント')
          // ハノイ側修正2022/09/05　STEP2_W　課題管理表№76：設計書「修正履歴」シートの「2022/09/01仕様変更分」添付一覧のNoはF091の登録が無い場合でも、1～4を常に表示してください。 End

          this.iraiForm['依頼入力済※1'] = response.data.入力制御FW042

          this.iraiForm.依頼承認チェック = response.data.依頼承認チェック

          this.画面入力制御 = response.data.画面入力制御

          Object.assign(this.iraiFormSearched, this.iraiForm)

          this.set画面入力制御(true)
          this.checkDspbtn()
          this.$nextTick(() => {
            this.isInit = false
          })
          this.$refs.refIraiForm.clearValidate()
          this.savePageSearchData()
        })
    },
    async check営業店OnSearch (data) {
      if (!data) {
        return true
      }
      const dataCheck営業店 = await this.getDataCheck営業店(data.店コード, data.店コード枝番)
      if (!dataCheck営業店 || dataCheck営業店.削除区分 != 0) {
        this.getMsg('E040254').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo.focus()
        return false
      }

      if (!await this.check参照権限(data.店コード, data.店コード枝番, true)) {
        this.$refs.koujiiraiNo.focus()
        return false
      }
      if (!data.物件コード &&
              !data.物件名 &&
              !data.物件区分 &&
              !data.物件住所Cd &&
              !data.物件住所Nm) {
        this.getMsg('E040254').then((ress) => {
          this.showError(ress.data)
        })
        this.$refs.koujiiraiNo.focus()
        return false
      }

      return true
    },
    // 戻すデータ
    reafsmodalBackEigyoten (obj) {
      this.iraiForm.店コード = obj.eigyotenCd
      this.iraiForm.店コード枝番 = obj.eigyotenBango
      this.iraiForm.営業店名 = obj.eigyotenNm

      if ((this.iraiForm.店コード == '' || this.iraiForm.店コード.trim() == '') || (this.iraiForm.店コード枝番 == '' || this.iraiForm.店コード枝番.trim() == '')) {
        this.isInit = true
        this.iraiForm.物件コード = ''
        this.iraiForm.物件名 = ''
        this.iraiForm.物件区分 = ''
        this.iraiForm.物件住所Cd = ''
        this.iraiForm.物件住所Nm = ''
        $selectSGR担当部署value.SGR担当部署value = null
        this.iraiForm.SGR担当部署 = 'ー'
        this.$nextTick(() => {
          this.isInit = false
        })
      } else {
        const テナント営業所コード = this.iraiForm.店コード + this.iraiForm.店コード枝番
        this.http
          .get('/api/Reafs_W/Irai/WD00111/Get物件コードInfo', { ログイン会社コード: this.userInfo.会社コード, テナント営業所コード })
          .then((response) => {
            if (response.data !== null) {
              this.iraiForm.物件コード = response.data.物件コード
              this.iraiForm.物件名 = response.data.物件名
              this.iraiForm.物件区分 = response.data.物件区分
              this.iraiForm.物件住所Cd = response.data.物件住所Cd
              this.iraiForm.物件住所Nm = response.data.物件住所Nm

              // Get DropdownSGR担当部署
              this.http
                .get('/api/Reafs_W/Irai/WD00111/GetDropdownSGR担当部署', { ログイン会社コード: this.userInfo.会社コード, 物件コード: this.iraiForm.物件コード })
                .then((response) => {
                  this.isInit = true
                  $selectSGR担当部署value.SGR担当部署value = response.data
                  this.iraiForm.SGR担当部署 = 'ー'
                  this.$nextTick(() => {
                    this.isInit = false
                  })
                })
            } else {
              this.iraiForm.物件コード = ''
              this.iraiForm.物件名 = ''
              this.iraiForm.物件区分 = ''
              this.iraiForm.物件住所Cd = ''
              this.iraiForm.物件住所Nm = ''
            }
          })

        if (obj.event === obj.eigyotenBango) {
          this.http
            .get('/api/Reafs_W/Irai/WD00111/Check営業店', { 店コード: this.iraiForm.店コード, 店コード枝番: this.iraiForm.店コード枝番 })
            .then((response) => {
              var next = true
              if (response.data && response.data.削除区分 === 1) {
                next = false
                this.getMsg('E040351').then((response) => {
                  this.showError(response.data)
                })
              } else {
                if (!response.data || response.data.削除区分 !== 0) {
                  next = false
                  this.getMsg('E040009').then((response) => {
                    this.showError(response.data)
                  })
                }
              }
              if (next) {
                if (!this.名称取得方法17.find(x => x.営業所コード && x.営業所コード.substring(0, 4) === '9900')) {
                  if (this.名称取得方法17 && this.名称取得方法17.filter(x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7).length > 0) {
                    var allPOSITIVE7Length = this.名称取得方法17.filter(x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7)
                    var allRange = allPOSITIVE7Length.map(x => x.支社コード + x.支社コード枝番)
                    this.http
                      .post('/api/Reafs_W/Irai/WD00111/get営業店CheckData',
                        { lst支社コード: allRange, 店コード: this.iraiForm.店コード, 店コード枝番: this.iraiForm.店コード枝番 })
                      .then((response) => {
                        if (!response.data.range名称取得方法18 ||
                    !response.data.range名称取得方法18.find(x => x.店コード === this.iraiForm.店コード && x.店コード枝番 === this.iraiForm.店コード枝番)) {
                          if (!response.data.店コード) {
                            this.getMsg('E040662').then((ress) => {
                              this.showError(ress.data)
                            })
                          }
                        }
                      })
                  } else {
                    this.http
                      .post('/api/Reafs_W/Irai/WD00111/get営業店CheckData',
                        { lst支社コード: [], 店コード: this.iraiForm.店コード, 店コード枝番: this.iraiForm.店コード枝番 })
                      .then((response) => {
                        if (!response.data.店コード) {
                          this.getMsg('E040662').then((ress) => {
                            this.showError(ress.data)
                          })
                        }
                      })
                  }
                }
              }
            })
        }
      }
    },
    reafsmodalBackIraiSha (obj) {
      this.iraiForm.依頼者コード = obj.txtCd
      this.iraiForm.依頼者名 = obj.lblNm

      if (this.iraiForm.依頼者コード == '' || this.iraiForm.依頼者コード.trim() == '') {
        this.iraiForm.依頼者所属部署名 = ''
        this.iraiForm.依頼者役職名 = ''

        this.iraiForm.依頼者事務所コード = ''
        this.iraiForm.依頼者営業所コード = ''
        this.iraiForm.依頼者部コード = ''
        this.iraiForm.依頼者課コード = ''
        this.iraiForm.依頼者係コード = ''
        this.iraiForm.依頼者役職コード = ''
      } else {
        this.http
          .get('/api/Reafs_W/Irai/WD00111/GetDataN011_12', { 社員コード: this.iraiForm.依頼者コード })
          .then((response) => {
            if (response.data !== null) {
              if (response.data.名称取得方法11 !== null) {
                this.iraiForm.依頼者名 = response.data.名称取得方法11.社員名
              } else {
                this.iraiForm.依頼者名 = ''
                this.iraiForm.依頼者所属部署名 = ''
                this.iraiForm.依頼者役職名 = ''

                this.iraiForm.依頼者事務所コード = ''
                this.iraiForm.依頼者営業所コード = ''
                this.iraiForm.依頼者部コード = ''
                this.iraiForm.依頼者課コード = ''
                this.iraiForm.依頼者係コード = ''
                this.iraiForm.依頼者役職コード = ''
                this.getMsg('E040020').then((response) => {
                  this.showError(response.data, {callback: () => {
                    this.$refs.ref依頼者コード.focus()
                  }})
                })
                return false
              }
              if (response.data.名称取得方法12 !== null) {
                this.iraiForm.依頼者所属部署名 = response.data.名称取得方法12.組織名
                this.iraiForm.依頼者役職名 = response.data.名称取得方法12.役職名

                this.iraiForm.依頼者事務所コード = response.data.名称取得方法12.事務所コード
                this.iraiForm.依頼者営業所コード = response.data.名称取得方法12.営業所コード
                this.iraiForm.依頼者部コード = response.data.名称取得方法12.部コード
                this.iraiForm.依頼者課コード = response.data.名称取得方法12.課コード
                this.iraiForm.依頼者係コード = response.data.名称取得方法12.係コード
                this.iraiForm.依頼者役職コード = response.data.名称取得方法12.役職コード
              } else {
                this.iraiForm.依頼者所属部署名 = ''
                this.iraiForm.依頼者役職名 = ''

                this.iraiForm.依頼者事務所コード = ''
                this.iraiForm.依頼者営業所コード = ''
                this.iraiForm.依頼者部コード = ''
                this.iraiForm.依頼者課コード = ''
                this.iraiForm.依頼者係コード = ''
                this.iraiForm.依頼者役職コード = ''
              }
            } else {
              this.iraiForm.依頼者名 = ''
              this.iraiForm.依頼者所属部署名 = ''
              this.iraiForm.依頼者役職名 = ''

              this.iraiForm.依頼者事務所コード = ''
              this.iraiForm.依頼者営業所コード = ''
              this.iraiForm.依頼者部コード = ''
              this.iraiForm.依頼者課コード = ''
              this.iraiForm.依頼者係コード = ''
              this.iraiForm.依頼者役職コード = ''
            }
          })
      }
    },
    reafsmodalBackIraiShadairi (obj) {
      // ハノイ側修正2022/08/30　STEP2_W　課題管理表№69：設計書「修正履歴」シートの「2022/08/30仕様変更分」「依頼者代理用の検索画面機能を追加してください。」Start
      this.iraiForm.依頼者代理コード = obj.txtCd
      this.iraiForm.依頼者代理名 = obj.lblNm
      // ハノイ側修正2022/08/30　STEP2_W　課題管理表№69：設計書「修正履歴」シートの「2022/08/30仕様変更分」「依頼者代理用の検索画面機能を追加してください。」End
      if (this.iraiForm.依頼者代理コード == '' || this.iraiForm.依頼者代理コード.trim() == '') {
        this.iraiForm.依頼者代理名 = ''
        this.iraiForm.依頼者代理所属部署名 = ''
        this.iraiForm.依頼者代理役職名 = ''

        this.iraiForm.依頼者代理事務所コード = ''
        this.iraiForm.依頼者代理営業所コード = ''
        this.iraiForm.依頼者代理部コード = ''
        this.iraiForm.依頼者代理課コード = ''
        this.iraiForm.依頼者代理係コード = ''
        this.iraiForm.依頼者代理役職コード = ''
      } else {
        this.http
          .get('/api/Reafs_W/Irai/WD00111/GetDataN011_12', { 社員コード: this.iraiForm.依頼者代理コード })
          .then((response) => {
            if (response.data !== null) {
              if (response.data.名称取得方法11 !== null) {
                this.iraiForm.依頼者代理名 = response.data.名称取得方法11.社員名
              } else {
                this.iraiForm.依頼者代理名 = ''
                this.iraiForm.依頼者代理所属部署名 = ''
                this.iraiForm.依頼者代理役職名 = ''

                this.iraiForm.依頼者代理事務所コード = ''
                this.iraiForm.依頼者代理営業所コード = ''
                this.iraiForm.依頼者代理部コード = ''
                this.iraiForm.依頼者代理課コード = ''
                this.iraiForm.依頼者代理係コード = ''
                this.iraiForm.依頼者代理役職コード = ''
                this.getMsg('E040457').then((response) => {
                  this.showError(response.data, {callback: () => {
                    this.$refs.ref依頼者代理コード.focus()
                  }})
                })
                return false
              }
              if (response.data.名称取得方法12 !== null) {
                this.iraiForm.依頼者代理所属部署名 = response.data.名称取得方法12.組織名
                this.iraiForm.依頼者代理役職名 = response.data.名称取得方法12.役職名

                this.iraiForm.依頼者代理事務所コード = response.data.名称取得方法12.事務所コード
                this.iraiForm.依頼者代理営業所コード = response.data.名称取得方法12.営業所コード
                this.iraiForm.依頼者代理部コード = response.data.名称取得方法12.部コード
                this.iraiForm.依頼者代理課コード = response.data.名称取得方法12.課コード
                this.iraiForm.依頼者代理係コード = response.data.名称取得方法12.係コード
                this.iraiForm.依頼者代理役職コード = response.data.名称取得方法12.役職コード
              } else {
                this.iraiForm.依頼者代理所属部署名 = ''
                this.iraiForm.依頼者代理役職名 = ''

                this.iraiForm.依頼者代理事務所コード = ''
                this.iraiForm.依頼者代理営業所コード = ''
                this.iraiForm.依頼者代理部コード = ''
                this.iraiForm.依頼者代理課コード = ''
                this.iraiForm.依頼者代理係コード = ''
                this.iraiForm.依頼者代理役職コード = ''
              }
            } else {
              this.iraiForm.依頼者代理名 = ''
              this.iraiForm.依頼者代理所属部署名 = ''
              this.iraiForm.依頼者代理役職名 = ''

              this.iraiForm.依頼者代理事務所コード = ''
              this.iraiForm.依頼者代理営業所コード = ''
              this.iraiForm.依頼者代理部コード = ''
              this.iraiForm.依頼者代理課コード = ''
              this.iraiForm.依頼者代理係コード = ''
              this.iraiForm.依頼者代理役職コード = ''
            }
          })
      }
    },
    onBlurKoujiiraiNoNendo () {
      if (this.searchForm.koujiiraiNo === '' || this.searchForm.koujiiraiNo.trim() === '') {
        // return true
      } else {
        this.searchForm.koujiiraiNo = this.searchForm.koujiiraiNo.padStart(2, '0')
      }
      this.savePageSearchData()
    },
    onBlurKoujiiraiNoRenban () {
      if (this.searchForm.koujiiraiNo1 === '' || this.searchForm.koujiiraiNo1.trim() === '') {
        // return true
      } else {
        this.searchForm.koujiiraiNo1 = this.searchForm.koujiiraiNo1.padStart(5, '0')
      }
      this.savePageSearchData()
    },
    savePageSearchData (keepSearchForm) {
      this.$store.commit('setPageData', {
        SyuzenIraiSansyoShonin: this.searchForm,
        依頼No: this.iraiForm.依頼No,
        keepSearchForm
      })
    },
    handleSelect (key, keyPath) {
      this.selectIndex = key
    },
    async selectKojiKubun (val) {
      // ハノイ側修正2022/05/16　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」Start
      if (this.iraiForm.工事区分 === '13' || this.iraiForm.工事区分 === '23') {
        const today = new Date()
        const timeNow = ('0' + today.getHours().toString()).slice(-2) + ':' + ('0' + today.getMinutes().toString()).slice(-2)
        if (this.isInit === false) {
          if (!await this.checkM020Exists()) {
            return false
          }
        }
      }
      // ハノイ側修正2022/05/16　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」End
      if (this.iraiForm.工事区分 && this.iraiForm.工事区分.charAt(0) === '4') {
        this.isInit = true
        this.iraiForm.大区分 = 'ー'
        this.画面入力制御disabled.大区分 = true
        this.iraiForm.中区分 = 'ー'
        this.画面入力制御disabled.中区分 = true
        this.iraiForm.小区分 = 'ー'
        this.画面入力制御disabled.小区分 = true
        this.iraiForm.依頼状況 = 'ー'
        this.画面入力制御disabled.依頼状況 = true
        this.iraiForm.SGR担当部署 = 'ー'
        this.画面入力制御disabled.SGR担当部署 = true
        this.$nextTick(() => {
          this.isInit = false
        })
      } else {
        this.画面入力制御disabled.大区分 = this.画面入力制御disabledOrg.大区分
        this.画面入力制御disabled.中区分 = this.画面入力制御disabledOrg.中区分
        this.画面入力制御disabled.小区分 = this.画面入力制御disabledOrg.小区分
        this.画面入力制御disabled.依頼状況 = this.画面入力制御disabledOrg.依頼状況
        this.画面入力制御disabled.SGR担当部署 = this.画面入力制御disabledOrg.SGR担当部署
      }
    },
    selectDaiKubun (val) {
      // ハノイ側修正2022/10/17　STEP2_W　課題管理表№169：設計書「修正履歴」シートの「2022/10/14仕様変更分」「WD00110_1.16の修正分を追記」Start
      var selected大区分 = this.iraiForm.大区分
      var current大区分 = $select大区分value.大区分value.filter(function (item) { return item.名称コード === selected大区分 }).pop()
      if (current大区分.備考1 !== '1' && (this.iraiForm.工事区分 === '30' || this.iraiForm.工事区分 === '31')) {
        this.getMsg('E040534').then((response) => {
          this.showError(response.data)
        })
      }
      // ハノイ側修正2022/10/17　STEP2_W　課題管理表№169：設計書「修正履歴」シートの「2022/10/14仕様変更分」「WD00110_1.16の修正分を追記」End
      // Get Dropdown中区分
      this.http
        .get('/api/Reafs_W/Irai/WD00111/GetDropdown中区分', { 大区分: this.iraiForm.大区分 })
        .then((response) => {
          $select中区分value.中区分value = response.data
          this.iraiForm.中区分 = 'ー'
          this.iraiForm.小区分 = 'ー'
        })
    },
    selectChuKubun (val) {
      // Get Dropdown小区分
      this.http
        .get('/api/Reafs_W/Irai/WD00111/GetDropdown小区分', { 大区分: this.iraiForm.大区分, 中区分: this.iraiForm.中区分 })
        .then((response) => {
          $select小区分value.小区分value = response.data
          this.iraiForm.小区分 = 'ー'
        })
    },
    selectChushiHoryuKubun (val) {
      // if (this.isUpdate) {
      if (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2') {
        // this.set画面入力制御(false)
      } else if (this.iraiForm.中止保留区分 === '0') {
        this.iraiForm.中止保留年月日 = ''
        this.iraiForm.中止保留事由 = ''
        this.$refs.refIraiForm.clearValidate(['中止保留年月日', '中止保留事由'])
        // this.set画面入力制御(true)
      }
      // }
    },
    async regisValidate (mode) {
      if (mode === this.mode.依頼承認) {
        // 営業店
        if (!this.iraiForm.店コード || !this.iraiForm.店コード枝番) {
          this.getMsg('E040005').then((response) => {
            this.showError(response.data, {callback: () => {
              if (!this.iraiForm.店コード) {
                this.$refs.ref営業店.focus()
              } else if (!this.iraiForm.店コード枝番) {
                this.$refs.ref営業店.eigyotenBangoFocus()
              }
            }})
          })
          return false
        }
        const dataCheck営業店 = await this.getDataCheck営業店(this.iraiForm.店コード, this.iraiForm.店コード枝番)
        if (dataCheck営業店 && dataCheck営業店.削除区分 === 1) {
          this.getMsg('E040351').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref営業店.focus()
            }})
          })

          return false
        }
        if (!dataCheck営業店 || dataCheck営業店.削除区分 !== 0) {
          this.getMsg('E040009').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref営業店.focus()
            }})
          })

          return false
        }
        // ハノイ側修正2022/09/26　STEP2_W　課題管理表№117：設計書「修正履歴」シートの「2022/09/20仕様変更分」参照権限チェック追加 Start
        // if (!await this.check参照権限(this.iraiForm.店コード, this.iraiForm.店コード枝番)) {
        //   return false
        // }
        // ハノイ側修正2022/09/26　STEP2_W　課題管理表№117：設計書「修正履歴」シートの「2022/09/20仕様変更分」参照権限チェック追加 End
      }
      // 工事依頼№
      // if (this.checkKoji() === false) {
      //   return false
      // }
      // if (!await this.checkKojiExist()) {
      //   return false
      // }

      if (this.iraiFormSearched.依頼No !== (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1)) {
        this.getMsg('E000012').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo.focus()
        return false
      }
      // ハノイ側修正2022/09/26　STEP2_W　課題管理表№117：設計書「修正履歴」シートの「2022/09/20仕様変更分」参照権限チェック追加 Start
      if (!await this.check営業店OnSearch(this.iraiForm)) {
        return false
      }
      // ハノイ側修正2022/09/26　STEP2_W　課題管理表№117：設計書「修正履歴」シートの「2022/09/20仕様変更分」参照権限チェック追加 End

      if (mode === this.mode.依頼承認) {
        // 工事区分
        // 1
        if (!this.画面入力制御disabled.工事区分 && (!this.iraiForm.工事区分 || this.iraiForm.工事区分 === 'ー')) {
          this.getMsg('E040015').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref工事区分.focus()
          return false
        }
        // 2
        const 名称取得方法07Selected = await this.GetM018削除区分('003', this.iraiForm.工事区分)
        if (名称取得方法07Selected && 名称取得方法07Selected.削除区分 === 1) {
          this.getMsg('E040351').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref工事区分.focus()
          return false
        }
        // 3
        if (this.iraiForm.工事区分 === '13' || this.iraiForm.工事区分 === '23') {
          if (!await this.checkM020Exists()) {
            return false
          }
        }
        // 大区分
        // 1
        if (!this.画面入力制御disabled.大区分 && (this.iraiForm.工事区分 || '').charAt(0) !== '4' && (!this.iraiForm.大区分 || this.iraiForm.大区分 === 'ー')) {
          this.getMsg('E040016').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref大区分.focus()
          return false
        }
        // 2
        const 大区分Selected = await this.GetM018削除区分('004', this.iraiForm.大区分)
        if (大区分Selected && 大区分Selected.削除区分 === 1) {
          this.getMsg('E040351').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref大区分.focus()
          return false
        }
        // ハノイ側修正2022/10/17　STEP2_W　課題管理表№169：設計書「修正履歴」シートの「2022/10/14仕様変更分」「WD00110_1.16の修正分を追記」Start
        // 3
        var selected大区分 = this.iraiForm.大区分
        var current大区分 = $select大区分value.大区分value.filter(function (item) { return item.名称コード === selected大区分 }).pop()
        if (current大区分.備考1 !== '1' && (this.iraiForm.工事区分 === '30' || this.iraiForm.工事区分 === '31')) {
          this.getMsg('E040534').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref大区分.focus()
          return false
        }
        // ハノイ側修正2022/10/17　STEP2_W　課題管理表№169：設計書「修正履歴」シートの「2022/10/14仕様変更分」「WD00110_1.16の修正分を追記」End
        // 中区分
        // 1
        if (!this.画面入力制御disabled.中区分 && (this.iraiForm.工事区分 || '').charAt(0) !== '4' && (!this.iraiForm.中区分 || this.iraiForm.中区分 === 'ー')) {
          this.getMsg('E040017').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref中区分.focus()
          return false
        }
        // 2
        const 中区分Selected = await this.GetM018削除区分('005', this.iraiForm.中区分, this.iraiForm.大区分)
        if (中区分Selected && 中区分Selected.削除区分 === 1) {
          this.getMsg('E040351').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref中区分.focus()
          return false
        }
        // 小区分
        // 1
        if (!this.画面入力制御disabled.小区分 && (this.iraiForm.工事区分 || '').charAt(0) !== '4' && (!this.iraiForm.小区分 || this.iraiForm.小区分 === 'ー')) {
          this.getMsg('E040018').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref小区分.focus()
          return false
        }
        // 2
        const 小区分Selected = await this.GetM018削除区分('006', this.iraiForm.小区分, this.iraiForm.大区分 + this.iraiForm.中区分)
        if (小区分Selected && 小区分Selected.削除区分 === 1) {
          this.getMsg('E040351').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref小区分.focus()
          return false
        }
        // 依頼者コード
        if (!this.画面入力制御disabled.依頼者コード && !this.iraiForm.依頼者コード) {
          this.getMsg('E040019').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref依頼者コード.focus()
          return false
        }
        if (!await this.check依頼者コード()) {
          return false
        }
      }
      // 依頼者連絡先
      if (mode === this.mode.依頼承認) {
        if (!this.画面入力制御disabled.依頼者連絡先 && !this.iraiForm.依頼者連絡先) {
          this.getMsg('E040669').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref依頼者連絡先.focus()
          return false
        }
      }
      if (!this.画面入力制御disabled.依頼者連絡先 && !this.base.isSimpleByte(this.iraiForm.依頼者連絡先)) {
        this.getMsg('E040460').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼者連絡先.focus()
        return false
      }
      // 依頼者代理連絡先
      if (!this.画面入力制御disabled.依頼者代理連絡先 && !this.base.isSimpleByte(this.iraiForm.依頼者代理連絡先)) {
        this.getMsg('E040460').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼者代理連絡先.focus()
        return false
      }
      if (mode === this.mode.依頼承認) {
        // 対象箇所
        if (!this.画面入力制御disabled.対象箇所 && !this.iraiForm.対象箇所) {
          this.getMsg('E040661').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref対象箇所.focus()
          return false
        }
        // 依頼内容／不具合状況
        if (!this.画面入力制御disabled['依頼内容／不具合状況'] && !this.iraiForm.依頼内容_状況) {
          this.getMsg('E040034').then((response) => {
            this.showError(response.data)
          })
          this.$refs.iraiNaiyo_fuguaiJokyo.focus()
          return false
        }
        // 依頼状況
        if (!this.画面入力制御disabled.依頼状況 && (!this.iraiForm.依頼状況 || this.iraiForm.依頼状況 === 'ー') && (this.iraiForm.工事区分 || '').charAt(0) !== '3') {
          this.getMsg('E040035').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref依頼状況.focus()
          return false
        }
        // SGR担当部署
        if (!this.画面入力制御disabled.SGR担当部署 && (!this.iraiForm.SGR担当部署 || this.iraiForm.SGR担当部署 === 'ー') && (this.iraiForm.工事区分 || '').charAt(0) !== '4') {
          this.getMsg('E040670').then((response) => {
            this.showError(response.data)
          })
          this.$refs.refSGR担当部署.focus()
          return false
        }
      }
      // 中止保留年月日
      if (this.iraiForm.中止保留区分 !== '0' && !this.iraiForm.中止保留年月日) {
        this.getMsg('E040003').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref中止保留年月日.focus()
        return false
      }

      if (this.iraiForm.中止保留区分 !== '0' && !this.iraiForm.中止保留年月日) {
        this.getMsg('E040004').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref中止保留年月日.focus()
        return false
      }
      if (this.iraiForm.中止保留区分 === '0' && this.iraiForm.中止保留年月日) {
        this.getMsg('E040213').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref中止保留区分.focus()
        return false
      }
      // 中止保留事由
      if (this.iraiForm.中止保留年月日 && !this.iraiForm.中止保留事由) {
        this.getMsg('E040358').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref中止保留事由.focus()
        return false
      }

      // ハノイ側修正2022/09/27　STEP2_W　課題管理表№121：設計書「修正履歴」シートの「2022/09/22仕様変更分」変更コメント追加 Start
      if (mode === this.mode.依頼承認) {
        var iraiFormCheck = this.iraiForm
        var iraiFormSearchedCheck = this.iraiFormSearched
        if (!isEqual(
          pick(iraiFormCheck, ['店コード', '店コード枝番', '工事依頼№', '工事区分', '大区分', '中区分', '小区分', '依頼者コード', '依頼者連絡先', '依頼者代理コード', '依頼者代理連絡先', '依頼コメント', '対象箇所', 'メーカー名', '依頼内容_状況']),
          pick(iraiFormSearchedCheck, ['店コード', '店コード枝番', '工事依頼№', '工事区分', '大区分', '中区分', '小区分', '依頼者コード', '依頼者連絡先', '依頼者代理コード', '依頼者代理連絡先', '依頼コメント', '対象箇所', 'メーカー名', '依頼内容_状況'])
        ) && (!this.iraiForm.変更コメント || (this.iraiForm.変更コメント && this.iraiForm.変更コメント === this.iraiFormSearched.変更コメント))) { // 2022/09/30 M.Ootsuka 依頼状況とSGR担当部署は変更チェック対象外に修正
          this.getMsg('E040765').then((response) => { // 2022/09/30 M.Ootsuka コードが重複していたので新しく発番したコードを参照するよう修正
            this.showError(response.data)
          })
          this.$refs.ref変更コメント.focus()
          return false
        }
      }
      // ハノイ側修正2022/09/27　STEP2_W　課題管理表№121：設計書「修正履歴」シートの「2022/09/22仕様変更分」変更コメント追加 End

      if ((mode === this.mode.依頼否認 || mode === this.mode.依頼差戻) && !this.iraiForm.否認差戻事由 && !this.画面入力制御disabled.否認差戻事由) {
        this.getMsg('E040357').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref否認_差戻事由.focus()
        return false
      }
      if (!await this.checkVersion()) {
        this.getMsg('E040098').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      // ハノイ側修正2022/05/27　STEP2_W　課題管理表№5：設計書「修正履歴」シートの「2022/05/26仕様変更分」「更新時チェックを追加」START
      if (mode === this.mode.依頼承認) {
        if (!await this.checkM032Exists()) {
          this.getMsg('E040717').then((response) => {
            this.showError(response.data)
          })
          return false
        }

        if (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2') {
          this.getMsg('E040746').then((response) => {
            this.showError(response.data)
          })
          return false
        }
      }
      // ハノイ側修正2022/05/27　STEP2_W　課題管理表№5：設計書「修正履歴」シートの「2022/05/26仕様変更分」「更新時チェックを追加」END
      return true
    },
    async check依頼者コード () {
      return new Promise(async (resolve) => {
        let res = {}
        await this.http
          .get('/api/Reafs_W/Irai/WD00111/GetDataN011_12', { 社員コード: this.iraiForm.依頼者コード })
          .then((response) => {
            res = response
          })
        if (res.data !== null) {
          if (res.data.名称取得方法11 === null) {
            this.getMsg('E040020').then((response) => {
              this.showError(response.data, {callback: () => {
                this.$refs.ref依頼者コード.focus()
              }})
            })
            resolve(false)
          }
        }
        resolve(true)
      })
    },
    async check依頼者コードCallBack (callback) {
      return new Promise(async (resolve) => {
        let res = {}
        await this.http
          .get('/api/Reafs_W/Irai/WD00111/GetDataN011_12', { 社員コード: this.iraiForm.依頼者コード })
          .then((response) => {
            res = response
          })
        if (res.data !== null) {
          if (res.data.名称取得方法11 === null) {
            this.getMsg('E040020').then((response) => {
              callback(new Error(response.data))
            })
            return false
          }
        }
        return true
      })
    },
    async GetM018削除区分 (区分コード, 名称コード, 連動区分) {
      return new Promise((resolve) => {
        this.http
          .post('/api/Reafs_W/Irai/WD00111/GetM018削除区分',
            { 区分コード: 区分コード, 名称コード: 名称コード, 連動区分: 連動区分 })
          .then((response) => {
            return resolve(response.data)
          })
      })
    },
    checkVersion () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00111/CheckF100Version',
            { 会社コード: this.iraiForm.会社コード, 依頼No: (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1) })
          .then((response) => {
            if (this.iraiForm.VERSION && (this.iraiForm.VERSION !== response.data.version || !response.data.version)) {
              resolve(false)
            } else {
              resolve(true)
            }
          })
      })
    },
    // ハノイ側修正2022/05/27　STEP2_W　課題管理表№5：設計書「修正履歴」シートの「2022/05/26仕様変更分」「更新時チェックを追加」START
    checkM032Exists () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00111/checkM032Exists',
            { 会社コード: this.userInfo.会社コード,
              物件コード: this.iraiForm.物件コード,
              連動区分: (this.ddlValue(this.iraiForm.大区分) + this.ddlValue(this.iraiForm.中区分)),
              名称コード: this.ddlValue(this.iraiForm.小区分) })
          .then((response) => {
            if (!response.data.isM032Exist) {
              resolve(false)
            } else {
              resolve(true)
            }
          })
      })
    },
    // ハノイ側修正2022/05/27　STEP2_W　課題管理表№5：設計書「修正履歴」シートの「2022/05/26仕様変更分」「更新時チェックを追加」END
    get営業店CheckData (allRange, 店コード, 店コード枝番) {
      return new Promise((resolve) => {
        this.http
          .post('/api/Reafs_W/Irai/WD00111/get営業店CheckData',
            { lst支社コード: allRange, 店コード: 店コード, 店コード枝番: 店コード枝番 })
          .then((response) => {
            return resolve(response)
          })
      })
    },
    check参照権限 (店コード, 店コード枝番, isCheckFromSearch) {
      return new Promise(async (resolve) => {
        if (this.名称取得方法17.find(x => x.営業所コード && x.営業所コード.substring(0, 4) === '9900')) {
          return resolve(true)
        }
        if (this.名称取得方法17 && this.名称取得方法17.filter(x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7).length > 0) {
          const allPOSITIVE7Length = this.名称取得方法17.filter(x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7)
          const allRange = allPOSITIVE7Length.map(x => x.支社コード + x.支社コード枝番)
          const res = await this.get営業店CheckData(allRange, 店コード, 店コード枝番)
          if (!res.data.range名称取得方法18 ||
              !res.data.range名称取得方法18.find(x => x.店コード === 店コード && x.店コード枝番 === 店コード枝番)) {
            if (!res.data.店コード) {
              this.getMsg(!isCheckFromSearch ? 'E040662' : 'E040254').then((ress) => {
                this.showError(ress.data)
              })
              if (!isCheckFromSearch) {
                this.$refs.ref営業店.eigyotenCdFocus()
              }
              return resolve(false)
            } else {
              return resolve(true)
            }
          } else {
            return resolve(true)
          }
        } else {
          const res = await this.get営業店CheckData([], 店コード, 店コード枝番)
          if (!res.data.店コード) {
            this.getMsg(!isCheckFromSearch ? 'E040662' : 'E040254').then((ress) => {
              this.showError(ress.data)
            })
            return resolve(false)
          } else {
            return resolve(true)
          }
        }
      })
    },
    getDataCheck営業店 (店コード, 店コード枝番) {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00111/Check営業店', { 店コード: 店コード, 店コード枝番: 店コード枝番 })
          .then((response) => {
            resolve(response.data)
          })
      })
    },
    async checkM020Exists () {
      return new Promise(async (resolve) => {
        let checkExist = false
        await this.http
          .get('/api/Reafs_W/Irai/WD00111/checkM020Exists')
          .then((response) => {
            checkExist = response.data.isM020Exist
          })

        const today = new Date()
        const timeNow = ('0' + today.getHours().toString()).slice(-2) + ':' + ('0' + today.getMinutes().toString()).slice(-2)
        if ((timeNow >= this.m001Info.急便始業時間 && timeNow <= this.m001Info.急便終業時間) === false && checkExist === false) {
          this.getMsg('E040671').then((response) => {
            this.showError(response.data, {callback: () => {
              resolve(false)
            }})
          })
          this.$refs.ref工事区分.focus()
        } else {
          resolve(true)
        }
      })
    },
    async onRegister (mode) {
      let smesConfirm = ''
      let smesSucess = ''
      if (mode === this.mode.依頼否認) {
        smesConfirm = 'K040068'
        smesSucess = 'M040013'
        this.btnLoading.依頼否認 = true
      } else if (mode === this.mode.依頼差戻) {
        smesConfirm = 'K040067'
        smesSucess = 'M040014'
        this.btnLoading.依頼差戻 = true
      } else if (mode === this.mode.依頼承認) {
        smesConfirm = 'K040069'
        smesSucess = 'M040010'
        this.btnLoading.依頼承認 = true
      }
      if (!await this.regisValidate(mode)) {
        this.hideLoading()
        return false
      }
      let msg = (await this.getMsg(smesConfirm)).data
      if (!await this.confirm(msg)) {
        this.hideLoading()
        return false
      }

      const data = {
        F100_依頼マスタ: this.getDataF100(mode),
        FW042_申請承認ファイル: this.getDataFW042(mode),
        F042_申請承認ファイル: this.getDataF042(mode),
        F044_業者別見積マスタ: this.getDataF044(mode),
        mode
      }
      const url = '/api/Reafs_W/Irai/WD00111/UpdateSyuzenIraiSansyoShonin'
      let res = {}
      await this.http
        .post(url, data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
          this.hideLoading()
        })
      const msg004 = (await this.getMsg('M040004')).data
      if (res.status) {
        if (mode) {
          this.showSuccess(msg004 + res.data.依頼No + (await this.getMsg(smesSucess)).data)
          // ハノイ側修正2022/10/24　STEP2_W　課題管理表№188：設計書「修正履歴」シートの「2022/10/21仕様変更分」「◆メール送信について追加」START
          if (mode === this.mode.依頼差戻 || (mode === this.mode.依頼承認 && res.data.最終承認フラグ === 1)) {
            await this.call_sendMail(mode === this.mode.依頼差戻 ? '依頼差戻' : '依頼承認', res.data.申請者, res.data.f042, res.data.f044)
          }
          // ハノイ側修正2022/10/24　STEP2_W　課題管理表№188：設計書「修正履歴」シートの「2022/10/21仕様変更分」「◆メール送信について追加」END
          // ハノイ側修正2022/10/25　STEP2_W　課題管理表№191：設計書「修正履歴」シートの「2022/10/24仕様変更分」「◆架電通知について追加」Start
          if (mode === this.mode.依頼承認 && res.data.最終承認フラグ === 1) {
            await this.twilioCall(res.data.f044)
          }
          // ハノイ側修正2022/10/25　STEP2_W　課題管理表№191：設計書「修正履歴」シートの「2022/10/24仕様変更分」「◆架電通知について追加」End
        }
        // ハノイ側修正2022/05/27　STEP2_W　課題管理表№4：設計書「修正履歴」シートの「2022/05/20仕様変更分」「依頼承認ボタン押下時の工事依頼書の処理を追加」START
        if (res.data.最終承認フラグ === 1) {
          this.paramsKojiIraiSho.工事依頼No = this.searchData.工事依頼No
          // this.$router.push({ name: 'WP00010' })
          if (mode === this.mode.依頼承認) {
            this.print()
          }
        }
        // ハノイ側修正2022/05/27　STEP2_W　課題管理表№4：設計書「修正履歴」シートの「2022/05/20仕様変更分」「依頼承認ボタン押下時の工事依頼書の処理を追加」END

        this.onSearch()
        this.hideLoading()
        return true
      } else {
        this.hideLoading()
        this.showError(res.message, {callback: async () => {
          if (mode === this.mode.依頼承認) {
            document.getElementById('ref依頼承認').focus()
          }
        }})
        return false
      }
    },
    // ハノイ側修正2022/11/17　STEP2_W　課題管理表№216：設計書「修正履歴」シートの「2022/11/17仕様変更分」「mail_pattern_no：63を追加」Start
    async call_sendMail (btnName, 申請者, f042, f044) {
      let res
      const f100 = this.getDataF100()
      var 工事依頼NO = f100.依頼No
      var 宛先事務所コード = []
      var 宛先営業所コード = []
      var 宛先部コード = []
      var 宛先課コード = []
      var 宛先係コード = []
      var 担当者コード = []

      var 業者コード = ''
      var 業者コード枝番 = ''

      if (btnName == '依頼差戻') {
        const mailPatNo = f100.工事区分 == '11' ? '2'
          : f100.工事区分 == '12' ? '37'
            : f100.工事区分 == '13' ? '60'
              : f100.工事区分 == '31' ? '81' : ''
        if (!mailPatNo) {
          return
        }
        宛先事務所コード = f100.工事区分 == '11' ? [f100.事務所コード_依頼者] : []
        宛先営業所コード = f100.工事区分 == '11' ? [f100.営業所コード_依頼者] : []
        宛先部コード = f100.工事区分 == '11' ? [f100.部コード_依頼者] : []
        宛先課コード = f100.工事区分 == '11' ? [f100.課コード_依頼者] : []
        宛先係コード = f100.工事区分 == '11' ? [f100.係コード_依頼者] : []
        担当者コード = [申請者]

        res = await this.sendMail(mailPatNo, 工事依頼NO, 宛先事務所コード, 宛先営業所コード, 宛先部コード, 宛先課コード, 宛先係コード, 担当者コード, 業者コード, 業者コード枝番)
      }
      if (btnName == '依頼承認') {
        const mailPatNo = f100.工事区分 == '11' ? '3'
          : f100.工事区分 == '12' ? '38'
            : f100.工事区分 == '13' ? '62'
              : f100.工事区分 == '31' ? '82' : ''
        if (!mailPatNo) {
          return
        }

        宛先事務所コード = f100.工事区分 == '12' ? [f042.承認事務所コード] : [f100.SGR担当事務所コード]
        宛先営業所コード = f100.工事区分 == '12' ? [f042.承認営業所コード] : [f100.SGR担当営業所コード]
        宛先部コード = f100.工事区分 == '12' ? [f042.承認部コード] : [f100.SGR担当部コード]
        宛先課コード = f100.工事区分 == '12' ? [f042.承認課コード] : [f100.SGR担当課コード]
        宛先係コード = f100.工事区分 == '12' ? [f042.承認係コード] : [f100.SGR担当係コード]

        res = await this.sendMail(mailPatNo, 工事依頼NO, 宛先事務所コード, 宛先営業所コード, 宛先部コード, 宛先課コード, 宛先係コード, 担当者コード, 業者コード, 業者コード枝番)

        if (f100.工事区分 == '13' && this.ddlValue(this.iraiForm.依頼状況) == '01') {
          const mailPatNo = '63'

          宛先事務所コード = []
          宛先営業所コード = []
          宛先部コード = []
          宛先課コード = []
          宛先係コード = []

          業者コード = f044.業者コード
          業者コード枝番 = f044.業者コード枝番

          res = await this.sendMail(mailPatNo, 工事依頼NO, 宛先事務所コード, 宛先営業所コード, 宛先部コード, 宛先課コード, 宛先係コード, 担当者コード, 業者コード, 業者コード枝番)
        }
      }
      return res
    },
    async sendMail (mailPatNo, 工事依頼NO, 宛先事務所コード, 宛先営業所コード, 宛先部コード, 宛先課コード, 宛先係コード, 担当者コード, 業者コード, 業者コード枝番) {
      const param = {
        msg_type: 0,
        mail_to: '',
        msg_head: '',
        msg_body: '',
        pg_id: 'WD00111',
        st_kbn: 1,
        mail_pattern_no: mailPatNo,
        工事依頼NO: 工事依頼NO,
        // 工事依頼NO枝番: 工事依頼No枝番,
        宛先事務所コード: 宛先事務所コード,
        宛先営業所コード: 宛先営業所コード,
        宛先部コード: 宛先部コード,
        宛先課コード: 宛先課コード,
        宛先係コード: 宛先係コード,
        業者コード,
        業者コード枝番,
        担当者コード: 担当者コード
      }

      const res = await this.commonFunctionUI.CM00110SendMail(param)
      return res
    },
    // ハノイ側修正2022/11/17　STEP2_W　課題管理表№216：設計書「修正履歴」シートの「2022/11/17仕様変更分」「mail_pattern_no：63を追加」End
    // ハノイ側修正2022/10/25　STEP2_W　課題管理表№191：設計書「修正履歴」シートの「2022/10/24仕様変更分」「◆架電通知について追加」Start
    async twilioCall (f044) {
      const f100 = this.getDataF100()
      let param = {}
      if ((f100.工事区分 === '13' || f100.工事区分 === '23') && this.ddlValue(this.iraiForm.依頼状況) === '01') {
        param = {
          param工事依頼No: f044.工事依頼NO,
          param工事依頼NO枝番: f044.工事依頼NO枝番,
          param契約NO: '',
          param契約年月: '',
          param予定年月: '',
          param業者コード: f044.業者コード,
          param業者コード枝番: f044.業者コード枝番,
          param架電区分: 3,
          paramINSERT_PG: 'WD00111'
        }
        const res = await this.commonFunctionUI.twilio_Call(param)
        return res
      }
    },
    // ハノイ側修正2022/10/25　STEP2_W　課題管理表№191：設計書「修正履歴」シートの「2022/10/24仕様変更分」「◆架電通知について追加」End
    ddlValue (val) {
      if (val) {
        val = val.replace('ー', '')
      }
      return val
    },
    getDataF100 (mode) {
      const SGR担当部署Selected = this.SGR担当部署value ? this.SGR担当部署value.find(x => x.名称コード === this.iraiForm.SGR担当部署) : null
      return {
        会社コード: this.userInfo.会社コード,
        依頼No: (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1),
        営業店コード: this.iraiForm.店コード,
        営業店コード枝番: this.iraiForm.店コード枝番,
        物件コード: this.iraiForm.物件コード,
        継続区分: mode !== this.mode.依頼否認 ? this.iraiForm.中止保留区分 : 2,
        工事区分: this.ddlValue(this.iraiForm.工事区分),
        大区分: this.ddlValue(this.iraiForm.大区分),
        中区分: this.ddlValue(this.iraiForm.中区分),
        小区分: this.ddlValue(this.iraiForm.小区分),
        依頼日: this.formatDate(new Date()),
        依頼者コード: this.iraiForm.依頼者コード,
        事務所コード_依頼者: this.iraiForm.依頼者事務所コード,
        営業所コード_依頼者: this.iraiForm.依頼者営業所コード,
        部コード_依頼者: this.iraiForm.依頼者部コード,
        課コード_依頼者: this.iraiForm.依頼者課コード,
        係コード_依頼者: this.iraiForm.依頼者係コード,
        役職コード_依頼者: this.iraiForm.依頼者役職コード,
        依頼者連絡先: this.iraiForm.依頼者連絡先,
        依頼者代理コード: this.iraiForm.依頼者代理コード,
        事務所コード_依頼者代理: this.iraiForm.依頼者代理事務所コード,
        営業所コード_依頼者代理: this.iraiForm.依頼者代理営業所コード,
        部コード_依頼者代理: this.iraiForm.依頼者代理部コード,
        課コード_依頼者代理: this.iraiForm.依頼者代理課コード,
        係コード_依頼者代理: this.iraiForm.依頼者代理係コード,
        役職コード_依頼者代理: this.iraiForm.依頼者代理役職コード,
        依頼者代理連絡先: this.iraiForm.依頼者代理連絡先,
        依頼状況: this.ddlValue(this.iraiForm.依頼状況),
        依頼コメント: this.iraiForm.依頼コメント,
        不具合場所: this.iraiForm.対象箇所,
        メーカー名: this.iraiForm.メーカー名,
        依頼内容_状況: this.iraiForm.依頼内容_状況,
        中止年月日: mode !== this.mode.依頼否認 ? this.iraiForm.中止保留年月日 : this.formatDate(new Date()),
        中止保留事由: mode !== this.mode.依頼否認 ? this.iraiForm.中止保留事由 : this.iraiForm.否認差戻事由,
        削除区分: mode === this.mode.依頼否認 ? 2 : this.iraiForm.中止保留区分 === '0' ? '0'
          : this.iraiForm.中止保留区分 === '1' ? '3' : '2',
        SGR担当事務所コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.事務所コード) : '',
        SGR担当営業所コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.営業所コード) : '',
        SGR担当部コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.部コード) : '',
        SGR担当課コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.課コード) : '',
        SGR担当係コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.係コード) : ''
      }
    },
    getDataFW042 (mode) {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼No: (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1),
        工事依頼No枝番: '',
        種類: '10',
        履歴NO: '0',
        発注サブNo: '0',
        検収回数: '0',
        明細No: '0',
        契約年月: '',
        区分: '2',
        // 申請日: this.formatDate(new Date()),
        // 申請時間: (new Date()).toLocaleTimeString('en-US', { hour12: false }),
        申請区分: '',
        承認日: this.formatDate(new Date()),
        承認時間: (new Date()).toLocaleTimeString('en-US', { hour12: false }),
        承認区分: '',
        否認差戻日: this.formatDate(new Date()),
        否認差戻時間: (new Date()).toLocaleTimeString('en-US', { hour12: false }),
        否認差戻事由: this.iraiForm.否認差戻事由,
        変更コメント: this.iraiForm.変更コメント,
        // ハノイ側修正2022/12/16　STEP2_W　課題管理表№246：設計書「修正履歴」シートの「2022/12/12仕様変更分」「中止時の削除区分更新削除」Start
        削除区分: this.iraiForm.中止保留区分 === '0' ? '0'
          : this.iraiForm.中止保留区分 === '1' ? '3' : '2'
        // ハノイ側修正2022/12/16　STEP2_W　課題管理表№246：設計書「修正履歴」シートの「2022/12/12仕様変更分」「中止時の削除区分更新削除」End
      }
    },
    getDataF042 (mode) {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼No: (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1),
        工事依頼No枝番: '',
        種類: '11',
        履歴No: '0',
        発注サブNO: '0',
        検収回数: '0',
        明細NO: '0',
        契約年月: '',
        区分: '1',
        申請日: this.formatDate(new Date()),
        申請時間: (new Date()).toLocaleTimeString('en-US', { hour12: false }),
        申請区分: '',
        承認日: this.formatDate(new Date()),
        承認時間: (new Date()).toLocaleTimeString('en-US', { hour12: false }),
        // 承認区分: '',
        // 否認差戻日: this.formatDate(new Date()),
        // 否認差戻時間: (new Date()).toLocaleTimeString('en-US', { hour12: false }),
        // 否認差戻事由: this.iraiForm.否認差戻事由,
        // 変更コメント: this.iraiForm.変更コメント,
        削除区分: '0'
      }
    },
    getDataF044 (mode) {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼No: (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1),
        工事依頼No枝番: '11',
        依頼フラグ: '1',
        依頼日: this.formatDate(new Date()),
        進捗コード: '020',
        削除区分: '0'
      }
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
    onViewAttachRegis () {
      this.paramsIraiTenpuTouroku.工事依頼No = this.iraiForm.依頼No
      this.showDialogIraiTenpuTouroku = true
    },
    IraiTenpuTourokuClose () {
      this.onSearch()
    },
    async onBack () {
      this.$store.commit('setKeepAliveFlag', 0)
      this.$store.commit('setRouterBackFlag', 1)

      this.loadingBack = true
      let isChange = false
      if (!this.iraiForm.依頼No) {
        for (const prop in this.iraiFormInit) {
          if (this.iraiFormInit[prop] !== this.iraiForm[prop]) {
            isChange = true
            break
          }
        }
      } else {
        if ((this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1) !== this.iraiForm.依頼No) {
          isChange = true
        } else {
          for (const prop in this.iraiFormInit) {
            if (this.iraiFormSearched[prop] !== this.iraiForm[prop]) {
              isChange = true
              break
            }
          }
        }
      }
      if (isChange) {
        if (await this.confirm((await this.getMsg('K000001')).data)) {
          this.loadingBack = false
        } else {
          this.loadingBack = false
          return
        }
      } else {
        this.loadingBack = false
      }
      this.$router.back()
    },
    onHelp () {
      this.showDialogKojikubunsetsumeiichiran = true
    },
    checkKoji () {
      if (this.searchForm.koujiiraiNo == '') {
        this.getMsg('E040001').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo.focus()
        return false
      }
      if (this.searchForm.koujiiraiNo1 == '') {
        this.getMsg('E040001').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo1.focus()
        return false
      }
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    }
  }
}
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 0px;
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
.item_label_iraiNaiyo_fuguaiJokyo {
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
  padding: 5px 0 0 0;
  box-sizing: border-box;
}
.item_input_iraiNaiyo_fuguaiJokyo{
  width: calc(100% - 120px);
}
.home-el-header {
  padding: 0 0 20px 0;
}
.home-el-main {
  padding: 0;
  flex-shrink: 1;
  flex-grow: 1;
}
.home-el-footer{
  padding: 10px 0 0 0;
}
.btnHelp{
  height: 30px;
  width: 52px;
  padding: 0 12px 0 12px;
}

.border-top {
    border-top: 1px solid #DCDFE6;
}
</style>

<style>
  .el-scrollbar__view {
      height: 100%;
  }
  .SyuzenIraiSansyoShonin .依頼者 > div > div > div >button {
    padding: 0 12px 0 12px !important;
  }
  .SyuzenIraiSansyoShonin .依頼者 .append-background {
    width: 212px !important;
  }
  .SyuzenIraiSansyoShonin .依頼者代理 > div > div > div >button {
    padding: 0 12px 0 12px !important;
  }
  .SyuzenIraiSansyoShonin .依頼者代理 .append-background {
    width: 212px !important;
  }
  .SyuzenIraiSansyoShonin .営業店 .append-background {
    width: 202px !important;
  }
</style>
