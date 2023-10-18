<template>
  <div class="home-container">
    <el-form
      :model="iraiForm"
      :rules="rules"
      ref="refIraiForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk = "false"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row>
            <el-col :span="8">
              <div style="display: flex">
                <el-form-item label="営業店" prop="営業店">
                  <opensearch
                    tabindex="2"
                    v-model="iraiForm.営業店"
                    width="90px"
                    :disabled="画面入力制御disabled.営業店"
                    :appendlabelText="iraiForm.営業店名"
                    :append="'append'"
                    :path="'reafsmodalSearchMise'"
                    maxlength="6"
                    @backData="reafsmodalBackEigyoten"
                    ref=ref営業店
                  >
                  </opensearch>
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
                <el-form-item label="依頼№" prop="iraiNo">
                  <div style="display: flex">
                    <reafsinputtext
                      tabindex="1"
                      maxlength="7"
                      v-model="searchForm.iraiNo"
                      :width="'100px'"
                      :disabled="画面入力制御disabled['依頼№']"
                      ref="iraiNo"
                      @blur="onBluriraiNoNendo"
                    >
                    </reafsinputtext>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="2" style="padding-left: 20px; padding-top: 2px;">
                <el-button tabindex="1" type="primary" size="mini"
                 @click="onSearch" :loading="loading">検索</el-button>
              </el-col>
            </el-form>
            <el-col :span="5">
              <el-form-item
                label="進捗（本体）"
                prop="shinchokuHontai"
                class="is-readonly"
              >
                <reafsinputtext
                  v-model="iraiForm.進捗_本体"
                  :disabled="画面入力制御disabled.進捗_本体"
                  maxlenth="10"
                  :width="'140px'"
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
                  :disabled="画面入力制御disabled.進捗_発注"
                  maxlenth="10"
                  :width="'140px'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="物件名" prop="bukken" class="is-readonly">
                <div style="display: flex">
                  <reafsinputtext
                    v-model="iraiForm.物件コード"
                    :disabled="画面入力制御disabled.物件コード"
                    maxlength="8"
                    :width="'120px'">
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件名"
                    :disabled="画面入力制御disabled.物件名"
                    maxlength="50"
                    :width="'100%'">
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item
                label="物件区分"
                prop="bukkenKbn"
                class="is-readonly"
              >
                <reafsinputtext
                  v-model="iraiForm.物件区分"
                  :disabled="画面入力制御disabled.物件区分"
                  maxlength="40"
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
                    :disabled="画面入力制御disabled.物件住所Cd"
                    maxlength="7"
                    :width="'110px'">
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件住所Nm"
                    :disabled="画面入力制御disabled.物件住所Nm"
                    maxlength="30"
                    :width="'370px'">
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
              <el-form-item label="工事区分" prop="工事区分">
                <el-select
                  tabindex="10"
                  v-model="iraiForm.工事区分"
                  :disabled="画面入力制御disabled.工事区分"
                  :required="true"
                  @change="selectKojiKubun"
                  ref="ref工事区分"
                >
                  <el-option
                    v-for="item in 工事区分value"
                    :key="item.名称コード"
                    :label="item.名称コード_名称"
                    :value="item.名称コード"
                  >
                  </el-option>
                </el-select>
                <el-button tabindex="10" class="btnHelp" @click="onHelp">？</el-button>
                <kojikubunsetsumeiichiran :dialogVisible.sync="showDialogKojikubunsetsumeiichiran"></kojikubunsetsumeiichiran>
              </el-form-item>
              <el-form-item label="大区分" prop="大区分">
                <el-select
                  tabindex="11"
                  v-model="iraiForm.大区分"
                  :disabled="画面入力制御disabled.大区分"
                  @change="selectDaiKubun"
                  ref="ref大区分"
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
              <el-form-item label="中区分" prop="中区分">
                <el-select
                  tabindex="12"
                  v-model="iraiForm.中区分"
                  :disabled="画面入力制御disabled.中区分"
                  @change="selectChuKubun"
                  ref="ref中区分"
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
              <el-form-item label="小区分" prop="小区分">
                <el-select
                  tabindex="13"
                  v-model="iraiForm.小区分"
                  :disabled="画面入力制御disabled.小区分"
                  ref="ref小区分"
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
              <el-form-item label="依頼状況" prop="依頼状況">
                <el-select
                  tabindex="28"
                  v-model="iraiForm.依頼状況"
                  :disabled="画面入力制御disabled.依頼状況"
                  :required="false"
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
              <el-form-item label="SGR担当部署" prop="SGR担当部署">
                <el-select
                  required
                  tabindex="29"
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
              <div style="width: 1px; height: 95px"></div>
            </el-col>
            <el-col :span="9">
              <el-form-item label="依頼者" prop="依頼者コード">
                <opensearch
                  tabindex="14"
                  v-model="iraiForm.依頼者コード"
                  :disabled="画面入力制御disabled.依頼者コード"
                  :appendlabelText="iraiForm.依頼者名"
                  :append="'append'"
                  :maxlength="'7'"
                  :path="'reafsmodalSearchShain'"
                  @backData="reafsmodalBackIraiSha"
                  ref="ref依頼者コード"
                >
                </opensearch>
              </el-form-item>
              <el-form-item label="依頼者代理" prop="依頼者代理コード">
                <opensearch
                  tabindex="19"
                  v-model="iraiForm.依頼者代理コード"
                  :disabled="画面入力制御disabled.依頼者代理コード"
                  :appendlabelText="iraiForm.依頼者代理名"
                  :append="'append'"
                  :maxlength="'7'"
                  :path="'reafsmodalSearchShain'"
                  @backData="reafsmodalBackIraiShadairi"
                  ref="ref依頼者代理コード"
                >
                </opensearch>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属部署・役職" prop="所属部署役職">
                <reafsinputtext
                  v-model="iraiForm.依頼者所属部署名"
                  :disabled="画面入力制御disabled.依頼者所属部署名"
                  :width="'200px'"
                >
                </reafsinputtext>
              </el-form-item>
              <el-form-item label="所属部署・役職" prop="所属部署役職">
                <reafsinputtext
                  v-model="iraiForm.依頼者代理所属部署名"
                  :disabled="画面入力制御disabled.依頼者代理所属部署名"
                  :width="'200px'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者役職名"
                  :disabled="画面入力制御disabled.依頼者役職名"
                  :width="'150px'"
                >
                </reafsinputtext>
              </el-form-item>
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者代理役職名"
                  :disabled="画面入力制御disabled.依頼者代理役職名"
                  :width="'150px'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="連絡先"
                prop="依頼者連絡先"
              >
                <reafsinputtext
                  tabindex="18"
                  type="tel"
                  maxlength="14"
                  v-model="iraiForm.依頼者連絡先"
                  :disabled="画面入力制御disabled.依頼者連絡先"
                  :width="'150px'"
                  :required="true"
                  ref="ref依頼者連絡先"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item
                label="連絡先"
                prop="依頼者代理連絡先"
              >
                <reafsinputtext
                  tabindex="23"
                  type="tel"
                  maxlength="14"
                  v-model="iraiForm.依頼者代理連絡先"
                  :disabled="画面入力制御disabled.依頼者代理連絡先"
                  :width="'150px'"
                  ref="ref依頼者代理連絡先"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="14">
              <el-form-item label="依頼コメント">
                <reafsinputtext
                  tabindex="24"
                  maxlength="50"
                  v-model="iraiForm.依頼コメント"
                  :disabled="画面入力制御disabled.依頼コメント"
                  :width="'673px'"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item
                label="対象箇所"
                prop="対象箇所"
              >
                <reafsinputtext
                  tabindex="25"
                  maxlength="50"
                  v-model="iraiForm.対象箇所"
                  :disabled="画面入力制御disabled.対象箇所"
                  :width="'673px'"
                  :required="true"
                  ref="ref対象箇所"
                ></reafsinputtext>
              </el-form-item>
              <el-form-item label="メーカー名">
                <reafsinputtext
                  tabindex="26"
                  maxlength="50"
                  v-model="iraiForm.メーカー名"
                  :disabled="画面入力制御disabled.メーカー名"
                  :width="'673px'"
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
                    tabindex="27"
                    class="item_input_iraiNaiyo_fuguaiJokyo"
                    maxlength="200"
                    type="textarea"
                    resize="none"
                    :rows="5"
                    v-model="iraiForm.依頼内容_状況"
                    :disabled="画面入力制御disabled['依頼内容／不具合状況']"
                    :required="true"
                    ref="iraiNaiyo_fuguaiJokyo"
                  >
                  </el-input>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="8" class="comment">
              <br />
              <br />
              <br />
              <br />
              <br />
              <el-form-item label="添付一覧" label-width="325px">
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
            <el-col :span="2">
              <br />
              <br />
              <br />
              <br />
              <br /><br />
              <br />
              <br /><br />
              <el-button
                tabindex="38"
                @click="onViewAttachRegis"
                style="float: right"
                :disabled="画面入力制御disabled.btn添付登録参照">添付<br />参照</el-button>
                <screenPopup
                  src="/Reafs_W/IraiTenpuTouroku"
                  :params="paramsIraiTenpuTouroku"
                  :dialogVisible.sync="showDialogIraiTenpuTouroku"
                  @closePopupEvent="IraiTenpuTourokuClose"></screenPopup>
            </el-col>
            <el-col :span="7">
              <el-form-item label="中止保留区分">
                <el-select
                  tabindex="30"
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
              <el-form-item label="中止保留年月日" prop="中止保留年月日" :class="[iraiForm.中止保留区分 === '1' || iraiForm.中止保留区分 === '2' ? 'is-required':'']">
                <el-date-picker
                  tabindex="31"
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
              <el-form-item label="中止保留事由" prop="中止保留事由" :class="[iraiForm.中止保留区分 === '1' || iraiForm.中止保留区分 === '2' ? 'is-required':'']">
                <reafsinputtext
                  tabindex="32"
                  maxlength="200"
                  v-model="iraiForm.中止保留事由"
                  :disabled="画面入力制御disabled.中止保留事由"
                  :width="'100%'"
                  ref="ref中止保留事由"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="変更コメント">
                <reafsinputtext
                  tabindex="33"
                  maxlength="50"
                  v-model="iraiForm.変更コメント"
                  :disabled="画面入力制御disabled.変更コメント"
                  :width="'450px'"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="14">
              <el-form-item label="否認・差戻事由">
                <reafsinputtext
                  tabindex="34"
                  maxlength="50"
                  v-model="iraiForm.否認差戻事由"
                  :disabled="画面入力制御disabled.否認差戻事由"
                  :width="'450px'"
                  ref="ref否認_差戻事由"
                ></reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="16">
              <div style="width: 40px; height: 1px"></div>
            </el-col>
            <el-col :span="2">
              <el-button :loading="loading_依頼否認" tabindex="35" @click="onRegister(mode.依頼否認)" :disabled="画面入力制御disabled.btn依頼否認">依頼<br />否認</el-button>
            </el-col>
            <el-col :span="2">
              <el-button :loading="loading_依頼差戻" tabindex="36" @click="onRegister(mode.依頼差戻)" :disabled="画面入力制御disabled.btn依頼差戻">依頼<br />差戻</el-button>
            </el-col>
            <el-col :span="2">
              <el-button :loading="loading_依頼承認" tabindex="37" @click="onRegister(mode.依頼承認)" :disabled="画面入力制御disabled.btn依頼承認">依頼<br />承認</el-button>
            </el-col>
            <el-col :span="1">
              <el-button tabindex="39" @click="onBack" :loading="loadingBack" style="float: right; height: 54px;">戻る</el-button>
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
import iraisha from '@/components/basic/Iraisha.vue'
import kojikubunsetsumeiichiran from '@/components/modal/KojiKubunSetsumeiIchiran.vue'
import opensearch from "@/components/basic/openSearch.vue";
import screenPopup from '../../common/ScreenPopup/ScreenPopup.vue'

var $select工事区分value
var $select大区分value
var $select中区分value
var $select小区分value
var $select依頼状況value
var $selectSGR担当部署value
var $select中止保留区分value

export default {
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.initialize(); // 初期化処理
      next();
    });
  },
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafsmodal,
    eigyoten,
    opensearch,
    iraisha,
    kojikubunsetsumeiichiran,
    screenPopup
  },
  data () {
    var validate営業店 = (rule, value, callback) => {
      if (this.iraiForm.営業店 === '') {
        this.getMsg('E010008').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    var validate工事区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.工事区分) {
        if (this.isInit === false) {
          if (value === 'ー') {
            this.getMsg('E040015').then((response) => {
              callback(new Error(response.data))
            })
          }
        }
      }
    }
    var validate大区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.大区分) {
        if (this.isInit === false) {
          if (value === 'ー') {
            this.getMsg('E040016').then((response) => {
              callback(new Error(response.data))
            })
          }
        }
      }
    }
    var validate中区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.中区分) {
        if (this.isInit === false) {
          if (value === 'ー') {
            this.getMsg('E040017').then((response) => {
              callback(new Error(response.data))
            })
          }
        }
      }
    }
    var validate小区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.小区分) {
        if (this.isInit === false) {
          if (value === 'ー') {
            this.getMsg('E040018').then((response) => {
              callback(new Error(response.data))
            })
          }
        }
      }
    }
    var validate依頼者コード = (rule, value, callback) => {
      if (value === '') {
        this.getMsg('E010008').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    var validate依頼者代理コード = (rule, value, callback) => {
      if (value === '') {
        this.getMsg('E010008').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    var validate依頼者連絡先 = (rule, value, callback) => {
      if (value === '') {
        this.getMsg('E040669').then((response) => {
          callback(new Error(response.data))
        })
      } else if (!this.base.isSimpleByte(value)) {
        this.getMsg('E040460').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    var validate依頼者代理連絡先 = (rule, value, callback) => {
      if (value !== '' && !this.base.isSimpleByte(value)) {
        this.getMsg('E040460').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    var validate対象箇所 = (rule, value, callback) => {
      if (value === '') {
        this.getMsg('E040661').then((response) => {
          callback(new Error(response.data))
        })
      }
    }
    var validate依頼内容_状況 = (rule, value, callback) => {
      if (value === '') {
        this.getMsg('E040034').then((response) => {
          callback(new Error('　　　　　　　　　　　　　　　' + response.data))
        })
      }
    }
    var validate依頼状況 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.依頼状況) {
        if (this.isInit === false) {
          if (value === 'ー' && (this.iraiForm.工事区分 || '').charAt(0) !== '3') {
            this.getMsg('E040035').then((response) => {
              callback(new Error(response.data))
            })
          }
        }
      }
    }
    var validate中止保留年月日 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.中止保留年月日) {
        if (this.isInit === false) {
          if ((value || '') === '' && (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2')) {
            this.getMsg('E040003').then((response) => {
              callback(new Error(response.data))
            })
          }
        }
      }
    }
    var validate中止保留事由 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.中止保留事由) {
        if (value === '' && (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2')) {
          this.getMsg('E040358').then((response) => {
            callback(new Error(response.data))
          })
        }
      }
    }
    return {
      isInit: true,
      loading: false,
      loading_依頼否認: false,
      loading_依頼差戻: false,
      loading_依頼承認: false,
      loadingBack: false,
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: '',
        UPDATE_HOST: ''
      },
      menuID: 'B',
      subMenuID: 'WD00311',
      menuOptions: {},
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
        iraiNo: ''
      },
      rules: {
        営業店: [
          { validator: validate営業店, trigger: 'blur' }
        ],
        工事区分: [
          { validator: validate工事区分, trigger: 'change' }
        ],
        大区分: [
          { validator: validate大区分, trigger: 'change' }
        ],
        中区分: [
          { validator: validate中区分, trigger: 'change' }
        ],
        小区分: [
          { validator: validate小区分, trigger: 'change' }
        ],
        依頼者コード: [
          { validator: validate依頼者コード, trigger: 'blur' }
        ],
        依頼者連絡先: [
          { validator: validate依頼者連絡先, trigger: 'blur' }
        ],
        依頼者代理コード: [
          { validator: validate依頼者代理コード, trigger: 'blur' }
        ],
        依頼者代理連絡先: [
          { validator: validate依頼者代理連絡先, trigger: 'blur' }
        ],
        対象箇所: [
          { validator: validate対象箇所, trigger: 'blur' }
        ],
        依頼内容_状況: [
          { validator: validate依頼内容_状況, trigger: 'blur' }
        ],
        依頼状況: [
          { validator: validate依頼状況, trigger: 'change' }
        ],
        中止保留年月日: [
          { validator: validate中止保留年月日, trigger: 'change' }
        ],
        中止保留事由: [
          { validator: validate中止保留事由, trigger: 'blur' }
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
        iraiNo: [
          {
            type: 'number',
            message: '数値を入力してください',
            trigger: 'blur'
          }
        ],
      },
      画面入力制御rules: {
        進捗: '',
        制御data: [
          [
            '    ',
            'No',
            '           ',
            '依頼入力済',
            '依頼入力済※1',
            '依頼取下',
            '依頼差戻',
            '－',
            '中止・保留時'
          ],
          [false, '1 ', '営業店', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '3 ', '依頼№', '〇', '〇', '〇', '〇', 'x', 'x'],
          [false, '9 ', '工事区分', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '10', '大区分', '〇', '×', '〇', '〇', '×', '×'],
          [false, '11', '中区分', '〇', '×', '〇', '〇', '×', '×'],
          [false, '12', '小区分', '〇', '×', '〇', '〇', '×', '×'],
          [false, '13',	'SGR担当部署', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '14',	'依頼者コード', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '18',	'依頼者連絡先',	'〇', '〇', '〇', '〇', '×', '×'],
          [false, '19',	'依頼者代理コード', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '23',	'依頼者代理連絡先', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '24',	'依頼コメント', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '25',	'対象箇所', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '26',	'メーカー名', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '27',	'依頼内容／不具合状況',	'〇', '〇', '〇', '〇', '×', '×'],
          [false, '28',	'依頼状況', '〇', '×', '〇', '〇', '×', '×'],
          [false, '30',	'中止保留区分', '〇', '〇', '〇', '〇', '×', '△（※2）'],
          [false, '31', '中止保留年月日', '×', '×', '×', '×', '×', '△（※2）'],
          [false, '32', '中止保留事由', '×', '×', '×', '×', '×', '△（※2）'],
          [false, '33', '変更コメント', '〇', '〇', '〇', '〇', '×', '×'],
          [false, '34', '否認差戻事由', '〇', '〇', '〇', '〇', '×', '×']
        ],
        metaData: {
          colEnabled: 0,
          colItem: 2
        },
        制御enabled: {}
      },
      画面入力制御disabled: {
        営業店: false,
        '依頼№': false,
        進捗_本体: true,
        進捗_発注: true,
        物件コード: true,
        物件名: true,
        物件区分: true,
        物件住所Cd: true,
        物件住所Nm: true,
        工事区分: false,
        大区分: false,
        中区分: false,
        小区分: false,
        依頼者コード: false,
        依頼者所属部署名: true,
        依頼者役職名: true,
        依頼者連絡先: false,
        依頼者代理コード: false,
        依頼者代理所属部署名: true,
        依頼者代理役職名: true,
        依頼者代理連絡先: false,
        依頼コメント: false,
        対象箇所: false,
        メーカー名: false,
        '依頼内容／不具合状況': false,
        依頼状況: false,
        SGR担当部署: false,
        中止保留区分: false,
        中止保留年月日: true,
        中止保留事由: true,
        変更コメント: false,
        否認差戻事由: false,
        btn依頼否認: false,
        btn依頼差戻: false,
        btn依頼承認: false,
        btn添付登録参照: false
      },
      画面入力制御disabledOrg: {},
      showDialog: false,
      showDialogKojikubunsetsumeiichiran: false,
      activeIndex: '1',
      selectIndex: '1',
      oldData: {},
      iraiForm: {
        営業店: '',
        営業店名: '',
        '依頼№': '',
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
        変更コメント: '',
        否認差戻事由: '',
        部位コメント1: '',
        部位コメント2: '',
        部位コメント3: '',
        部位コメント4: '',
        進捗コード_本体: '',
        '依頼入力済※1': '',
        依頼承認チェック: '',
        依頼No: ''
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
      名称取得方法17: {},
      mode: Object.freeze({'依頼否認': 1, '依頼差戻': 2, '依頼承認': 3}),
      btnLoading: {
        依頼否認: false,
        依頼差戻: false,
        依頼承認: false
      },
      paramsKojiIraiSho: {
        工事依頼No: '',
        page: ''
      },
      showDialogIraiTenpuTouroku: false,
      paramsIraiTenpuTouroku:{
        添付種類: '901',
        工事依頼No: ''
      },
      shoninfile: {
        申請者: '',
        申請者事務所コード: '',
        申請者営業所コード: '',
        申請者部コード: '',
        申請者課コード: '',
        申請者係コード: ''
      },
    }
  },
  watch: {
    'iraiForm.中止保留年月日' (n, o) {

    },
    'searchForm.iraiNo' (v) {
    }
  },
  computed: {
    searchData () {
      return {
        ログイン会社コード: this.userInfo.会社コード,
        依頼No: this.searchForm.iraiNo
      }
    },
    isUpdate () {
      return (this.searchForm.iraiNo.trim())
    }
  },
  inject: ["setPageTitle"],
  created () {
    this.init()
    $select工事区分value = this
    $select大区分value = this
    $select中区分value = this
    $select小区分value = this
    $select依頼状況value = this
    $selectSGR担当部署value = this
    $select中止保留区分value = this
  },
  mounted () {
    this.setPageTitle(this.menuOptions.SubMenuName);
  },
  methods: {
    initialize() {
      this.paramsKojiIraiSho = {
        依頼No: this.$route.query.依頼No,
        page: this.$route.query.page
      }
      if (this.$route.params && this.$route.query.依頼No) {
        this.searchForm.iraiNo = this.$route.query.依頼No
        this.onSearch()
      }
    },
    
    afterInit () {
      Object.assign(this.画面入力制御disabledOrg, this.画面入力制御disabled)
      Object.assign(this.iraiFormInit, this.iraiForm)
    },
    reInit () {
      let temp画面入力制御disabled = {}
      let tempiraiForm = {}

      this.isInit = true
      Object.assign(temp画面入力制御disabled, this.画面入力制御disabledOrg)
      this.画面入力制御disabled = temp画面入力制御disabled

      Object.assign(tempiraiForm, this.iraiFormInit)
      this.iraiForm = tempiraiForm
    },
    getPageTitleByMenu () {
      let menuInfo = this.$store.getters.getMenuInfo()
      const menuOptions = menuInfo.find((item) => {
        return item.MenuId === this.menuID && item.SubMenuId === this.subMenuID
      })
      Object.assign(this.menuOptions, menuOptions)
      this.menuOptions.SubMenuName = '[定期] 依頼参照/承認'
    },

    init () {
      this.getPageTitleByMenu()
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.会社コード = userLogIn.会社コード
      
      // Get Dropdown
      this.http
        .get('/api/Reafs_W/Irai/WD00311/GetStartInfo', 'アクセスしています....')
        .then((response) => {
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
          if (pageData &&
          pageData.searchForm &&
          pageData.searchForm.iraiNo &&
          pageData.依頼No) {
            this.searchForm.iraiNo = pageData.searchForm.iraiNo
            this.onSearch()
          } 
        })

      this.画面入力制御disabled.btn依頼否認 = true
      this.画面入力制御disabled.btn依頼差戻 = true
      this.画面入力制御disabled.btn依頼承認 = true
      this.画面入力制御disabled.btn添付登録参照 = true
    },

    showError (message, option = null, title = this.menuOptions.SubMenuName) {
      var self = this
      this.MsgErr({ title, message, callback: function (a, b) {
        self.hideLoading()
        if(option && typeof(option.callback) === 'function') {
          option.callback()
        }
      }})
    },
    showSuccess (message, title = this.menuOptions.SubMenuName) {
      var self = this
      this.MsgInfo({ title, message, callback: function (a, b) {
        self.hideLoading()
      }})
    },
    hideLoading () {
      this.loading = false
      // eslint-disable-next-line no-return-assign
      Object.keys(this.btnLoading).forEach(v => this.btnLoading[v] = false)
    },

    confirm (message, title = this.menuOptions.SubMenuName) {
      return new Promise((resolve, reject) => {
        this.MsgConf({ title, message, callback: function (a, b) {
          if (a === 'confirm') {
            resolve(true)
          } else {
            resolve(false)
          }
        }})
      })
    },
    set画面入力制御 (isScreenDisplayTiming = true) {
      const 進捗コード_本体 = this.iraiForm.進捗コード_本体
      const 中止保留区分 = this.iraiForm.中止保留区分
      const s進捗内容 = this.get進捗(中止保留区分, 進捗コード_本体)
      this.画面入力制御rules.進捗 = s進捗内容
      const col = this.画面入力制御rules.制御data[0].findIndex(
        (e) => e === s進捗内容
      )
      for (
        let index = 1;
        index < this.画面入力制御rules.制御data.length;
        index++
      ) {
        const element = this.画面入力制御rules.制御data[index]
        if (element[col] === '△（※2）') {
          element[0] = this.get進捗Condition2(isScreenDisplayTiming)
        } else {
          element[0] = element[col] !== '×'
        }
        this.画面入力制御rules.制御enabled[
          element[this.画面入力制御rules.metaData.colItem]
        ] = element[this.画面入力制御rules.metaData.colEnabled]
      }

      for (const item in this.画面入力制御disabled) {
        if (this.画面入力制御rules.制御enabled.hasOwnProperty(item)) {
          // mapping enabled to disabled
          this.画面入力制御disabled[item] =
            !this.画面入力制御rules.制御enabled[item]
        }
      }
      if (isScreenDisplayTiming) {
        for (const item in this.画面入力制御disabledOrg) {
          if (this.画面入力制御rules.制御enabled.hasOwnProperty(item)) {
            // mapping enabled to disabled
            this.画面入力制御disabledOrg[item] =
              !this.画面入力制御rules.制御enabled[item]
          }
        }
      }

      if (this.iraiForm.工事区分 && this.iraiForm.工事区分 !== 'ー') {
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
    checkDspbtn(){
      if (this.iraiForm.依頼承認チェック === true) {
        this.画面入力制御disabled.btn依頼否認 = false
        this.画面入力制御disabled.btn依頼差戻 = false
        this.画面入力制御disabled.btn依頼承認 = false
      } else {
        this.画面入力制御disabled.btn依頼否認 = true
        this.画面入力制御disabled.btn依頼差戻 = true
        this.画面入力制御disabled.btn依頼承認 = true
       }

       this.画面入力制御disabled.工事区分 = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.SGR担当部署 = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.依頼者コード = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.依頼者連絡先 = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.依頼者代理コード = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.依頼者代理連絡先 = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.依頼コメント = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.対象箇所 = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.メーカー名 = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled['依頼内容／不具合状況'] = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.変更コメント = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.中止保留区分 = this.画面入力制御disabled.btn依頼承認
       this.画面入力制御disabled.否認差戻事由 = this.画面入力制御disabled.btn依頼承認

      this.画面入力制御disabled.btn添付登録参照 = false
    },
    async onSearch () {
      this.loading = true
      this.savePageSearchData()
      if (this.checkIraiNo() === false) {
        this.$refs.iraiNo.focus()
        return false
      }

      let checkExist = false
      await this.http
        .get('/api/Reafs_W/Irai/WD00311/CheckF100Exist', { 依頼No: this.searchData.依頼No })
        .then((response) => {
          checkExist = response.data.isF100Exist
        })
      
      if (!checkExist) {
        this.getMsg('E000014').then((response) => {
          this.showError(response.data)
        })
        this.$refs.iraiNo.focus()
        return false
      }
      var getArrayValue = (arr, index, key) => {
        if (!arr || !arr[index] || !arr[index][key]) {
          return ''
        }
        return arr[index][key]
      }
      var getObjectValue = (obj, index) => {
        index = index || 0
        return obj ? obj[Object.keys(obj)[index]] : ''
      }

      this.http
        .get('/api/Reafs_W/Irai/WD00311/GetPageData', { ログイン会社コード: this.userInfo.会社コード, 依頼No: this.searchData.依頼No }, 'アクセスしています....')
        .then((response) => {
          //取得件数が0件の場合エラーメッセージを出して、表示されている情報をクリア
          if (!response.status) {
            this.getMsg('E000014').then((response) => {
              this.showError(response.data)
            })
            this.$refs.iraiNo.focus()
            return false
          }
          

          this.isInit = true
          this.loading = false
          
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

          //承認ファイル
          this.shoninfile = response.data.申請承認ファイル;

          this.oldData = JSON.parse(JSON.stringify(response.data))
          response.data.対象箇所 = response.data.不具合場所
          response.data.中止保留区分 = response.data.継続区分
          response.data.中止保留年月日 = response.data.中止年月日
          response.data.SGR担当部署 = !response.data.SGR担当事務所コード ? 'ー' : response.data.SGR担当事務所コード +
          '-' + response.data.SGR担当営業所コード +
          '-' + response.data.SGR担当部コード +
          '-' + response.data.SGR担当課コード +
          '-' + response.data.SGR担当係コード

          if(response.data.工事区分.trim() === ''){
            response.data.工事区分 = 'ー'
          }
          if(response.data.大区分.trim() === ''){
            response.data.大区分 = 'ー'
          }
          if(response.data.中区分.trim() === ''){
            response.data.中区分 = 'ー'
          }
          if(response.data.小区分.trim() === ''){
            response.data.小区分 = 'ー'
          }
          if(response.data.依頼状況.trim() === ''){
            response.data.依頼状況 = 'ー'
          }
          if(response.data.中止保留区分.trim() === ''){
            response.data.中止保留区分 = '0'
          }

          this.iraiForm = response.data

          console.log("F100");
          console.log(this.iraiForm);

          this.iraiForm.部位コメント1 = getArrayValue(response.data.部位コメント, 0, '部位コメント')
          this.iraiForm.部位コメント2 = getArrayValue(response.data.部位コメント, 1, '部位コメント')
          this.iraiForm.部位コメント3 = getArrayValue(response.data.部位コメント, 2, '部位コメント')
          this.iraiForm.部位コメント4 = getArrayValue(response.data.部位コメント, 3, '部位コメント')

          this.iraiForm.営業店 = response.data.店コード + response.data.店コード枝番
          this.iraiForm.依頼者コード = response.data.依頼者コード
          this.iraiForm.依頼者代理コード = response.data.依頼者代理コード
          this.iraiForm['依頼入力済※1'] = response.data.入力制御FW042

          this.iraiForm.依頼承認チェック = response.data.依頼承認チェック

          this.iraiForm.変更コメント = response.data.名称取得方法30
          this.iraiForm.否認差戻事由 = response.data.否認差戻事由
         
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
    // 戻すデータ
    reafsmodalBackEigyoten (obj) {
      this.iraiForm.営業店 = obj.営業店
      this.iraiForm.営業店名 = obj.営業店名

      if ((this.iraiForm.営業店 == '' || this.iraiForm.営業店.trim() == '') ) {
        this.iraiForm.営業店名 = ''
        this.iraiForm.進捗_本体 = ''
        this.iraiForm.進捗_発注 = ''
        this.iraiForm.物件名 = ''
        this.iraiForm.物件区分 = ''
        this.iraiForm.物件住所Cd = ''
        this.iraiForm.物件住所Nm = ''
         $selectSGR担当部署value.SGR担当部署value = null
        this.iraiForm.SGR担当部署 = 'ー'
      } else {
        const テナント営業所コード = this.iraiForm.営業店
        this.http
          .get('/api/Reafs_W/Irai/WD00311/Get物件コードInfo', { ログイン会社コード: this.userInfo.会社コード, テナント営業所コード })
          .then((response) => {
            if (response.data !== null) {
              // this.iraiForm.進捗_本体 = response.data.進捗_本体
              // this.iraiForm.進捗_発注 = response.data.進捗_発注
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
              this.iraiForm.進捗_本体 = ''
              this.iraiForm.進捗_発注 = ''
              this.iraiForm.物件コード = ''
              this.iraiForm.物件名 = ''
              this.iraiForm.物件区分 = ''
              this.iraiForm.物件住所Cd = ''
              this.iraiForm.物件住所Nm = ''
            }
          })
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
          .get('/api/Reafs_W/Irai/WD00311/GetDataN011_12', { 社員コード: this.iraiForm.依頼者コード })
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
                  this.showError(response.data, {callback: ()=>{
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
      this.iraiForm.依頼者代理コード = obj.txtCd
      this.iraiForm.依頼者代理名 = obj.lblNm

      if (this.iraiForm.依頼者代理コード == '' || this.iraiForm.依頼者代理コード.trim() == '') {
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
          .get('/api/Reafs_W/Irai/WD00311/GetDataN011_12', { 社員コード: this.iraiForm.依頼者代理コード })
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
                this.getMsg('E040020').then((response) => {
                  this.showError(response.data, {callback: ()=>{
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
    onBluriraiNoNendo () {
      if (this.searchForm.iraiNo === '' || this.searchForm.iraiNo.trim() === '') {
        // return true
      } else {
        this.searchForm.iraiNo = this.searchForm.iraiNo.padStart(7, '0')
      }
      this.savePageSearchData()
    },
    savePageSearchData() {
      this.$store.commit("setPageData", {
        searchForm: this.searchForm,
        依頼No: this.iraiForm.依頼No
      })
    },
    handleSelect (key, keyPath) {
      this.selectIndex = key
    },
    selectKojiKubun (val) {
      if (this.iraiForm.工事区分 && this.iraiForm.工事区分.charAt(0) === '4') {
        this.iraiForm.大区分 = 'ー'
        this.画面入力制御disabled.大区分 = true
        this.iraiForm.中区分 = 'ー'
        this.画面入力制御disabled.中区分 = true
        this.iraiForm.小区分 = 'ー'
        this.画面入力制御disabled.小区分 = true
        this.iraiForm.依頼状況 = 'ー'
        this.画面入力制御disabled.依頼状況 = true
      } else {
        this.画面入力制御disabled.大区分 = this.画面入力制御disabledOrg.大区分
        this.画面入力制御disabled.中区分 = this.画面入力制御disabledOrg.中区分
        this.画面入力制御disabled.小区分 = this.画面入力制御disabledOrg.小区分
        this.画面入力制御disabled.依頼状況 = this.画面入力制御disabledOrg.依頼状況
      }
    },
    selectDaiKubun (val) {
      // Get Dropdown中区分
      this.http
        .get('/api/Reafs_W/Irai/WD00311/GetDropdown中区分', { 大区分: this.iraiForm.大区分 })
        .then((response) => {
          $select中区分value.中区分value = response.data
          this.iraiForm.中区分 = 'ー'
          this.iraiForm.小区分 = 'ー'
        })
    },
    selectChuKubun (val) {
      // Get Dropdown小区分
      this.http
        .get('/api/Reafs_W/Irai/WD00311/GetDropdown小区分', { 大区分: this.iraiForm.大区分, 中区分: this.iraiForm.中区分 })
        .then((response) => {
          $select小区分value.小区分value = response.data
          this.iraiForm.小区分 = 'ー'
        })
    },
    selectChushiHoryuKubun (val) {
      console.log("中止保留区分")
      if(this.isUpdate){
        if (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2') {
          this.set画面入力制御(false)
        } else if (this.iraiForm.中止保留区分 === '0') {
          this.iraiForm.中止保留年月日 = ''
          this.iraiForm.中止保留事由 = ''
          this.$refs.refIraiForm.clearValidate(['中止保留年月日','中止保留事由'])
          this.set画面入力制御(true)
        }
      }
    },
    async regisValidate (mode) {
      // 営業店
      if (!this.iraiForm.営業店) {
        this.getMsg('E040005').then((response) => {
          this.showError(response.data)
        })
        if (!this.iraiForm.営業店) {
          this.$refs.ref営業店.focus()
        }
        return false
      }
      const dataCheck営業店 = await this.getDataCheck営業店()
      if (dataCheck営業店 && dataCheck営業店.削除区分 === 1) {
        this.getMsg('E040351').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref営業店.focus()
        return false
      }
      if (!dataCheck営業店 || dataCheck営業店.削除区分 !== 0) {
        this.getMsg('E040009').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref営業店.focus()
        return false
      }
      
      // if (!await this.check参照権限()) {
      //   return false
      // }
      
      // 依頼№
      if (this.isUpdate && this.oldData.依頼No !== (this.searchForm.iraiNo)) {
        this.getMsg('E000012').then((response) => {
          this.showError(response.data)
        })
        this.$refs.iraiNo.focus()
        return false
      }
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
        const today = new Date()
        const timeNow = ('0' + today.getHours().toString()).slice(-2) + ':' + ('0' + today.getMinutes().toString()).slice(-2)
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
      // 依頼者連絡先
      if (!this.画面入力制御disabled.依頼者連絡先 && !this.iraiForm.依頼者連絡先) {
        this.getMsg('E040669').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼者連絡先.focus()
        return false
      }
      if (!this.画面入力制御disabled.依頼者連絡先 && !this.base.isSimpleByte(this.iraiForm.依頼者連絡先)) {
        this.getMsg('E040460').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼者連絡先.focus()
        return false
      }
      // // 依頼者代理コード
      // if (!this.画面入力制御disabled.依頼者代理コード && !this.iraiForm.依頼者代理コード) {
      //   this.getMsg('E040019').then((response) => {
      //     this.showError(response.data)
      //   })
      //   this.$refs.ref依頼者代理コード.focus()
      //   return false
      // }
      // // 依頼者代理連絡先
      // if (!this.画面入力制御disabled.依頼者代理連絡先 && !this.base.isSimpleByte(this.iraiForm.依頼者代理連絡先)) {
      //   this.getMsg('E040460').then((response) => {
      //     this.showError(response.data)
      //   })
      //   this.$refs.ref依頼者代理連絡先.focus()
      //   return false
      // }
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
      if (!this.画面入力制御disabled.SGR担当部署 && (!this.iraiForm.SGR担当部署 || this.iraiForm.SGR担当部署 === 'ー') && (this.iraiForm.工事区分 || '').charAt(0) == '4') {
        this.getMsg('E040670').then((response) => {
          this.showError(response.data)
        })
        this.$refs.refSGR担当部署.focus()
        return false
      }

      //承認の場合に継続区分が継続以外
      console.log(this.iraiForm.中止保留区分)
      if (this.isUpdate && mode === this.mode.依頼承認 && this.iraiForm.中止保留区分 !=='0') {
        
        this.getMsg('E040746').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref中止保留事由.focus()
        return false
      }

      // 中止保留年月日
      if (this.isUpdate && this.iraiForm.中止保留区分 !== '0' && !this.iraiForm.中止保留年月日) {
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
      if (this.isUpdate && this.iraiForm.中止保留年月日 && !this.iraiForm.中止保留事由) {
        this.getMsg('E040358').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref中止保留事由.focus()
        return false
      }

      // 否認差戻事由
      if ((mode === this.mode.依頼否認 || mode === this.mode.依頼差戻) && !this.iraiForm.否認差戻事由 && !this.画面入力制御disabled.否認差戻事由) {
        this.getMsg('E040357').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref否認_差戻事由.focus()
        return false
      }

      if (this.isUpdate && !await this.checkVersion()) {
        this.getMsg('E040098').then((response) => {
          this.showError(response.data)
        })
        return false
      }


      return true
    },
    async print () {
      const data = {
        会社コード: this.iraiForm.会社コード,
        工事依頼No: this.iraiForm.依頼No,
        店コード: this.iraiForm.店コード,
        店コード枝番: this.iraiForm.店コード枝番,
        添付種類: '201' // ハノイ側修正2022/12/12　STEP2_W　課題管理表№245：設計書「修正履歴」シートの「2022/12/12仕様変更分」「遷移元にWD00311を追加。その場合の帳票コードも追記。」
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
        })
        .catch(ex => console.log(ex))
        .finally(() => {
        })
        .catch(ex => console.log(ex))
    },
    async GetM018削除区分 (区分コード, 名称コード, 連動区分) {
      return new Promise((resolve) => {
        this.http
          .post('/api/Reafs_W/Irai/WD00311/GetM018削除区分',
            { 区分コード: 区分コード, 名称コード: 名称コード, 連動区分: 連動区分 })
          .then((response) => {
            return resolve(response.data)
          })
      })
    },
    checkVersion () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00311/CheckF100Version',
            { 会社コード: this.iraiForm.会社コード, 依頼No: (this.searchForm.iraiNo) })
          .then((response) => {
            if (this.iraiForm.VERSION && (this.iraiForm.VERSION !== response.data.version || !response.data.version)) {
              resolve(false)
            } else {
              resolve(true)
            }
          })
      })
    },
    check参照権限 () {
      return new Promise((resolve) => {
        if (this.名称取得方法17 && this.名称取得方法17.filter(x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7).length > 0) {
          const allPOSITIVE7Length = this.名称取得方法17.filter(x => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7)
          const allRange = allPOSITIVE7Length.map(x => x.支社コード + x.支社コード枝番)
          if (!allRange || allRange.length <= 0) {
            return resolve(false)
          }
          if (allPOSITIVE7Length.find(x => x.営業所コード && x.営業所コード.substring(0, 4) === '9900')) {
            return resolve(true)
          }
          this.http
            .post('/api/Reafs_W/Irai/WD00311/get営業店CheckData',
              { lst支社コード: allRange, 営業店: this.iraiForm.営業店 })
            .then((response) => {
              if (!response.data.Range名称取得方法18 ||
              !response.data.Range名称取得方法18.find(x => x.営業店 === this.iraiForm.営業店)) {
                if (!response.data.営業店) {
                  this.getMsg('E040662').then((res) => {
                    this.showError(res.data)
                  })
                  this.$refs.ref営業店.Focus()
                  return resolve(false)
                }
              }
              return resolve(true)
            })
        } else {
          resolve(false)
        }
      })
    },
    getDataCheck営業店 () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00311/Check営業店', { 営業店: this.iraiForm.営業店 })
          .then((response) => {
            resolve(response.data)
          })
      })
    },
    ddlValue (val) {
      if (val) {
        val = val.replace('ー', '')
      }
      return val
    },
    getDataF100 () {
      const SGR担当部署Selected = this.SGR担当部署value.find(x => x.名称コード === this.iraiForm.SGR担当部署)
      return {
        会社コード: this.userInfo.会社コード,
        依頼No: (this.searchForm.iraiNo),
        営業店: this.iraiForm.営業店,
        物件コード: this.iraiForm.物件コード,
        継続区分: this.isUpdate ? this.iraiForm.中止保留区分 : 0,
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
        中止年月日: this.isUpdate ? this.iraiForm.中止保留年月日 : '',
        中止保留事由: this.isUpdate ? this.iraiForm.中止保留事由 : '',
        変更コメント: this.iraiForm.変更コメント,
        削除区分: !this.isUpdate ? '0'
          : this.iraiForm.中止保留区分 === '0' ? '0'
            : this.iraiForm.中止保留区分 === '1' ? '3' : '2',
        SGR担当事務所コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.事務所コード) : '',
        SGR担当営業所コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.営業所コード) : '',
        SGR担当部コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.部コード) : '',
        SGR担当課コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.課コード) : '',
        SGR担当係コード: SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.係コード) : ''
      }
    },
      getDataFW042 () {
        return {
          会社コード: this.userInfo.会社コード,
          工事依頼No: (this.searchForm.iraiNo),
          申請者事務所コード: '',
          申請者営業所コード: '',
          申請者部コード: '',
          申請者課コード: '',
          申請者係コード: '',
          申請者役職コード: '',
          承認事務所コード: '',
          承認営業所コード: '',
          承認部コード: '',
          承認課コード: '',
          承認係コード: '',
          承認役職コード: '',
          承認役職コード_上限: '',
          否認差戻事由: this.iraiForm.否認差戻事由,
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
      // this.$router.push({ name: 'WD00120' })
      this.paramsIraiTenpuTouroku.工事依頼No = this.iraiForm.依頼No
      this.showDialogIraiTenpuTouroku = true
    },
    IraiTenpuTourokuClose () {
      this.onSearch()
    },
    async onBack () {
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
        if ((this.searchForm.iraiNo) !== this.iraiForm.依頼No) {
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
          // if (this.paramsKojiIraiSho.page === 'WD00510') {
          //   this.$router.push({ name: 'WD00510' })
          // } else {
          //   this.$router.push({ name: 'WD00010' })
          // }
          this.$store.commit('setKeepAliveFlag', 0)
          this.$store.commit('setRouterBackFlag', 1)
          this.$router.back()
        } else {
          this.loadingBack = false
        }
      } else {
        this.loadingBack = false
        // if (this.paramsKojiIraiSho.page === 'WD00510') {
        //   this.$router.push({ name: 'WD00510' })
        // } else {
        //   this.$router.push({ name: 'WD00010' })
        // }
        this.$store.commit('setKeepAliveFlag', 0)
        this.$store.commit('setRouterBackFlag', 1)
        this.$router.back()
        //WD00510からも呼ばれるはずなのでそちらに戻る処理も必要
      }
    },

    async onRegister (mode) {
      console.log("メール開始準備");
      console.log(this.searchForm.iraiNo);
      console.log(this.iraiform);

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
        mode
      }
      const url = '/api/Reafs_W/Irai/WD00311/UpdateTeikiIraiSansyoShonin'
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
        }

        if (mode === this.mode.依頼差戻){
          let param = {
                msg_type: 0,
                pg_id: 'WD00311',
                st_kbn: 2,
                mail_pattern_no: 2,
                工事依頼NO: this.searchForm.iraiNo,
                宛先事務所コード: [this.shoninfile.申請者事務所コード],
                宛先営業所コード: [this.shoninfile.申請者営業所コード],
                宛先部コード: [this.shoninfile.申請者部コード],
                宛先課コード: [this.shoninfile.申請者課コード],
                宛先係コード: [this.shoninfile.申請者係コード],
                担当者コード:[this.shoninfile.申請者]
              }
            let resultMail
            resultMail = this.commonFunctionUI.CM00110SendMail(param)
        }else if (mode === this.mode.依頼承認){
          console.log("メール送信");
          console.log(this.searchForm.iraiNo);
          console.log(this.shoninfile.最終承認フラグ);

          if (this.shoninfile.最終承認フラグ === 1){
            this.print()
            const SGR担当部署Selected = this.SGR担当部署value.find(x => x.名称コード === this.iraiForm.SGR担当部署)
         
            let param = {
                  msg_type: 0,
                  pg_id: 'WD00311',
                  st_kbn: 2,
                  mail_pattern_no: 3,
                  工事依頼NO: this.searchForm.iraiNo,
                  宛先事務所コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.事務所コード) : ''],
                  宛先営業所コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.営業所コード) : ''],
                  宛先部コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.部コード) : ''],
                  宛先課コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.課コード) : ''],
                  宛先係コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.係コード) : '']
                }
              let resultMail
              resultMail = this.commonFunctionUI.CM00110SendMail(param)
            console.log("メール送信2");
            let param2 = {
                  msg_type: 0,
                  pg_id: 'WD00311',
                  st_kbn: 2,
                  mail_pattern_no: 4,
                  工事依頼NO: this.searchForm.iraiNo,
                  宛先事務所コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.事務所コード) : ''],
                  宛先営業所コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.営業所コード) : ''],
                  宛先部コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.部コード) : ''],
                  宛先課コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.課コード) : ''],
                  宛先係コード: [SGR担当部署Selected ? this.ddlValue(SGR担当部署Selected.係コード) : '']
                }
              let resultMail2
              resultMail2 = this.commonFunctionUI.CM00110SendMail(param2)
              console.log("メール送信完了");
          }
          
        }

        this.onSearch()
        this.hideLoading()
        return true
      } else {
        this.hideLoading()
        this.showError(res.message)
        return false
      }
    },

    onHelp () {
      this.showDialogKojikubunsetsumeiichiran = true
    },
    checkIraiNo () {
      if (this.searchForm.iraiNo == '') {
        this.getMsg('E040002').then((response) => {
          this.showError(response.data)
        })
        this.$refs.iraiNo.focus()
        return false
      }
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    diffcheckIraiNo () {
      let isChange = false
      if ((this.searchForm.iraiNo) !== this.iraiForm.依頼No) {
          isChange = true
        }

      if (isChange) {
          this.getMsg('E000012').then((response) => {
          this.showError(response.data)
        }) 
        this.$refs.iraiNo.focus()
        return false
      } else {
        return true
      }
    },
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
  height: 116px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 1px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  padding: 25px 0 0 0;
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
</style>
