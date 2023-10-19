<template>
  <div class="home-container IraiNyuryoku">
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
                <el-form-item label="営業所" prop="営業店コード" class="is-required 営業店">
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
                      :disabled="画面入力制御disabled['工事依頼№']"
                      ref="koujiiraiNo"
                      @blur="onBlurKoujiiraiNoNendo"
                    >
                    </reafsinputtext>
                    <reafsinputtext
                      maxlength="5"
                      v-model="searchForm.koujiiraiNo1"
                      :width="'72px'"
                      :disabled="画面入力制御disabled['工事依頼№']"
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
                 @click="onSearch">検索</el-button>
              </el-col>
            </el-form>
            <el-col style="width: 22%;">
              <el-form-item
                label="進捗（本体）"
                prop="shinchokuHontai"
                class="is-readonly"
              >
              <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
                <reafsinputtext
                  v-model="iraiForm.進捗_本体"
                  maxlenth="10"
                  readonly
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
                  readonly
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
                    readonly
                    :width="'120px'">
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件名"
                    :title="iraiForm.物件名"
                    maxlength="50"
                    readonly
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
                  readonly
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
                <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
                <div style="display: flex">
                  <reafsinputtext
                    v-model="iraiForm.物件住所Cd"
                    maxlength="8"
                    readonly
                    :width="'125px'">
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件住所Nm"
                    maxlength="100"
                    readonly
                    :width="'346px'">
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="5" style="margin-top: -9px; width: 25% !important;">
              <div style="margin-left: 10px; color: red; font-weight: bold; font-size: 15px;">営業所で選択した場所に協力会社が向かいます。</div>
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
                  popper-class = "el-popper-required"
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
                  popper-class = "el-popper-required"
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
                  popper-class = "el-popper-required"
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
                  popper-class = "el-popper-required"
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
                  popper-class = "el-popper-required"
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
              <el-form-item style ="height: 31px; margin-left: -125px;">
                <div style="margin: -5px 0 0 -45px; font-weight: bold; font-size: 13px; color: black;">【工事区分について】</div>
                <div style="margin: -10px 0 0 -55px; font-weight: bold; font-size: 13px; color: black;">修繕工事一般・・・・ 見積先行工事。通常はこちらを使います。</div>
                <div style="margin: -10px 0 0 -55px; font-weight: bold; font-size: 13px; color: black;">修繕工事緊急6項目・・すぐに工事対応が必要な案件です。詳細は「？」マークを押下して参照してください。</div>
                <div style="margin: -10px 0 0 -55px; font-weight: bold; font-size: 13px; color: black;">修繕工事緊急・・・・ 上記修繕工事緊急6項目以外。（案件によっては一般に切り替える場合があります。）</div>
              </el-form-item>
              <div style="width: 1px; height: 92px"></div>
            </el-col>
            <el-col :span="5">
              <div style="margin-left: 20px; margin-bottom: 4px; color: red; font-weight: bold; font-size: 15px;">【依頼状況について】</div>
              <div style="margin-left: 10px; margin-bottom: 4px; color: red; font-weight: bold; line-height: 25px; font-size: 15px;">先行してコールセンターに電話で手配した場合は、必ず02の「緊急を要したためSGRコールセンターに電話で依頼済みです」を選択してください。</div>
              <div style="margin-left: 10px; color: red; font-weight: bold; font-size: 15px;">（二重手配を避けるため）</div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="依頼者" prop="依頼者コード" class="is-required">
                <div class="依頼者">
                  <!-- ハノイ側修正2022/09/05　STEP2_W　課題管理表№72：設計書「修正履歴」シートの「2022/09/01仕様変更分」タブキーによるフォーカス移動時の挙動がおかしい。 Start -->
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
                  <!-- ハノイ側修正2022/09/05　STEP2_W　課題管理表№72：設計書「修正履歴」シートの「2022/09/01仕様変更分」タブキーによるフォーカス移動時の挙動がおかしい。 End -->
                </div>
              </el-form-item>
              <el-form-item label="依頼者代理" prop="iraishaDairi">
                <div class="依頼者代理">
                  <!-- ハノイ側修正2022/09/05　STEP2_W　課題管理表№72：設計書「修正履歴」シートの「2022/09/01仕様変更分」タブキーによるフォーカス移動時の挙動がおかしい。 Start -->
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
                  <!-- ハノイ側修正2022/09/05　STEP2_W　課題管理表№72：設計書「修正履歴」シートの「2022/09/01仕様変更分」タブキーによるフォーカス移動時の挙動がおかしい。 End -->
                </div>
                  <!-- <reafsinputtext
                    maxlength="7"
                    v-model="iraiForm.依頼者代理コード"
                    :disabled="画面入力制御disabled.依頼者代理コード"
                    :width="'88px'"
                    @blur="reafsmodalBackIraiShadairi"
                    ref="ref依頼者代理コード"
                    >
                  </reafsinputtext> -->
                  <!-- <reafsinputtext
                    v-model="iraiForm.依頼者代理名"
                    :width="'100%'"
                    class="is-readonly"
                    readonly
                  >
                  </reafsinputtext> -->
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者所属部署名"
                  :title="iraiForm.依頼者所属部署名"
                  :width="'320px'"
                  readonly
                >
                </reafsinputtext>
              </el-form-item>
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者代理所属部署名"
                  :title="iraiForm.依頼者代理所属部署名"
                  :width="'320px'"
                  readonly
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者役職名"
                  :title="iraiForm.依頼者役職名"
                  :width="'150px'"
                  readonly
                >
                </reafsinputtext>
              </el-form-item>
              <el-form-item label-width="0" class="is-readonly">
                <reafsinputtext
                  v-model="iraiForm.依頼者代理役職名"
                  :title="iraiForm.依頼者代理役職名"
                  :width="'150px'"
                  readonly
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
                <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
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
              <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
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
                <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
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
                  <!--  ハノイ側修正2023/05/10　課題管理表№337：「改行コードをWindows用のコードに変換した上で文字数チェックを行う入力コントロール開発依頼」Start -->
                  <reafsinputtext
                  required
                  maxlength="200"
                  class="is-required item_input_iraiNaiyo_fuguaiJokyo"
                  v-model="iraiForm.依頼内容_状況"
                  @update:replaceValue="value=>iraiForm.依頼内容_状況 = value"
                  :disabled="画面入力制御disabled['依頼内容／不具合状況']"
                  :width="'100%'"
                  type="textarea"
                  resize="none"
                  :rows="3"
                  ref="iraiNaiyo_fuguaiJokyo"
                ></reafsinputtext>
                  <!-- <el-input
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
                  </el-input> -->
                  <!--  ハノイ側修正2023/05/10　課題管理表№337：「改行コードをWindows用のコードに変換した上で文字数チェックを行う入力コントロール開発依頼」End -->
                </div>
              </el-form-item>
            </el-col>
            <el-col class="comment" style="width: 32%;">
              <br />
              <br />
              <div style="font-family: Meiryo; margin-top: 2px; margin-left: -10px; color: red; font-weight: 900; font-size: 14pt; line-height: 20px;">【注意】</div>
              <div style="font-family: Meiryo; margin-left: 10px; font-weight: 900;"><span style="font-size: 14pt; color: black;">添付できる写真は</span><span style="font-size: 16pt; color: red;">1ファイル5MB未満</span><span style="font-size: 14pt; color: black;">です。</span></div>
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
            <el-col style="width: 9%; margin-left: 8px;">
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
                ref="ref添付登録参照"
                >添付登録参照</el-button>
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
                  :title="iraiForm.中止保留事由"
                  ref="ref中止保留事由"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="変更コメント" prop="変更コメント">
                <reafsinputtext
                  id="変更コメント"
                  v-model="iraiForm.変更コメント"
                  :title="iraiForm.変更コメント"
                  :width="'100%'"
                  ref="ref変更コメント"
                  readonly
                  class="is-readonly"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="否認・差戻事由" prop="否認差戻事由">
                <reafsinputtext
                  id="否認差戻事由"
                  v-model="iraiForm.否認差戻事由"
                  :title="iraiForm.否認差戻事由"
                  :width="'100%'"
                  ref="ref否認差戻事由"
                  readonly
                  class="is-readonly"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              &nbsp;
            </el-col>
            <el-col :span="10" style="margin-top: 2px;">
              <div style="font-family: Meiryo; margin-left: 40px; color: red; font-weight: bold; font-size: 14pt;">【注意】</div>
              <div style="font-family: Meiryo; margin-left: 60px;"><span style="font-size: 14pt; color: black;"> 必ず「登録」後、</span> <span style="font-size: 16pt; font-weight: 900; color: red;">「申請」ボタンを押してください。</span></div>
              <div style="font-family: Meiryo; margin-left: 60px; font-size: 14pt; color: black;">※登録だけでは依頼はされません。</div>
            </el-col>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="14">
              <el-button
                @click="onDelete"
                :disabled="画面入力制御disabled.btn削除"
                 :loading="loading削除">削除</el-button>
            </el-col>
            <el-col :span="3" style="margin-left: 2.6%;">
              <el-button v-if="isUpdate" :loading="loading_登録_更新" @click="onRegister()" :disabled="画面入力制御disabled.btn登録_更新">更新</el-button>
              <el-button v-else :loading="loading_登録_更新" @click="onRegister()" :disabled="画面入力制御disabled.btn登録_更新">登録</el-button>
            </el-col>
            <el-col :span="2">
              <el-button
                @click="onApply"
                :disabled="画面入力制御disabled.btn申請"
                 :loading="loading_申請">申請</el-button>
            </el-col>
            <el-col :span="2">
              <el-button
                @click="onWithdrawal"
                :disabled="画面入力制御disabled.btn取下"
                 :loading="loading_取下">取下</el-button>
            </el-col>
            <el-col :span="2" style="margin-left: 0.7%;">
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

var $select工事区分value
var $select大区分value
var $select中区分value
var $select小区分value
var $select依頼状況value
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
        this.getMsg('E040019').then((response) => {
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
      isMounted: false,
      isInit: true,
      loading削除: false,
      loading_登録_更新: false,
      loading_申請: false,
      loading_取下: false,
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
          [false, '1 ', '営業店コード', '〇', '×', '〇', '〇', '×', '×'],
          [false, '3 ', '工事依頼№', '〇', 'x', '〇', '〇', 'x', 'x'],
          [false, '9 ', '工事区分', '〇', '×', '〇', '〇', '×', '×'],
          [false, '10', '大区分', '〇', '×', '〇', '〇', '×', '×'],
          [false, '11', '中区分', '〇', '×', '〇', '〇', '×', '×'],
          [false, '12', '小区分', '〇', '×', '〇', '〇', '×', '×'],
          [false, '14',	'依頼者コード', '〇', '×', '〇', '〇', '×', '×'],
          [false, '18',	'依頼者連絡先',	'〇', '×', '〇', '〇', '×', '×'],
          [false, '19',	'依頼者代理コード', '〇', '×', '〇', '〇', '×', '×'],
          [false, '23',	'依頼者代理連絡先', '〇', '×', '〇', '〇', '×', '×'],
          [false, '24',	'依頼コメント', '〇', '×', '〇', '〇', '×', '×'],
          [false, '25',	'対象箇所', '〇', '×', '〇', '〇', '×', '×'],
          [false, '26',	'メーカー名', '〇', '×', '〇', '〇', '×', '×'],
          [false, '27',	'依頼内容／不具合状況',	'〇', '×', '〇', '〇', '×', '×'],
          [false, '28',	'依頼状況', '〇', '×', '〇', '〇', '×', '×'],
          [false, '29',	'中止保留区分', '〇', '×', '〇', '〇', '×', '△（※2）'],
          [false, '30', '中止保留年月日', '×', '×', '×', '×', '×', '△（※2）'],
          [false, '31', '中止保留事由', '×', '×', '×', '×', '×', '△（※2）']
        ],
        metaData: {
          colEnabled: 0,
          colItem: 2
        },
        制御enabled: {}
      },
      m001Info: {},
      画面入力制御disabled: {
        営業店コード: false,
        '工事依頼№': false,
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
        中止保留区分: true,
        中止保留年月日: true,
        中止保留事由: true,
        btn削除: false,
        btn申請: false,
        btn取下: false,
        btn登録_更新: false // ハノイ側修正2022/11/22　STEP2_W　課題管理表№221：設計書「修正履歴」シートの「2022/11/21仕様変更分」「登録／更新ボタン押下条件追加。」
      },
      画面入力制御disabledInit: {},
      画面入力制御disabledOrg: {},
      showDialog: false,
      showDialogKojikubunsetsumeiichiran: false,
      showDialogIraiTenpuTouroku: false,
      activeIndex: '1',
      selectIndex: '1',
      oldData: {},
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
        依頼申請取下チェック: '',
        依頼No: '',
        ログイン者事務所コード: '',
        ログイン者営業所コード: '',
        ログイン者部コード: '',
        ログイン者課コード: '',
        ログイン者係コード: ''
      },
      iraiFormInit: {},
      iraiFormSearched: {},
      工事区分value: [],
      大区分value: [],
      中区分value: [],
      小区分value: [],
      依頼状況value: [],
      中止保留区分value: [],
      名称取得方法17: {},
      paramsIraiTenpuTouroku: {
        添付種類: '901',
        工事依頼No: ''
      },
      paramsKojiIraiSho: {
        工事依頼No: '',
        page: ''
      },
      StartupMode: '' // ハノイ側修正2023/07/17　STEP2_W　課題管理表№368：「WD00110：依頼入力画面にて、営業店と物件コードが紐づかない状態で、F100に登録されているデータがあるようです。」
    }
  },
  watch: {
    'iraiForm.中止保留年月日' (n, o) {

    },
    'searchForm.koujiiraiNo' (v) {
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
    },
    isUpdate () {
      return (this.searchForm.koujiiraiNo.trim() && this.searchForm.koujiiraiNo1.trim())
    }
  },
  created () {
    $select工事区分value = this
    $select大区分value = this
    $select中区分value = this
    $select小区分value = this
    $select依頼状況value = this
    $select中止保留区分value = this
    // ハノイ側修正2023/07/17　STEP2_W　課題管理表№368：「WD00110：依頼入力画面にて、営業店と物件コードが紐づかない状態で、F100に登録されているデータがあるようです。」START
    if (this.$route.params && this.$route.params.工事依頼No) {
      this.StartupMode = '1'
    } else {
      this.StartupMode = '0'
    }
    // ハノイ側修正2023/07/17　STEP2_W　課題管理表№368：「WD00110：依頼入力画面にて、営業店と物件コードが紐づかない状態で、F100に登録されているデータがあるようです。」END
    this.init()
  },
  mounted () {
    this.isMounted = true
    this.initOnMounted()
    this.$store.commit('setIraiTenpuTourokuPageData', '')// ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… Start
  },
  methods: {
    initOnMounted () {
      // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 Start
      this.paramsKojiIraiSho = {
        工事依頼No: this.$route.params.工事依頼No,
        page: this.$route.params.page
      }

      if (this.$route.params && this.$route.params.工事依頼No) {
        this.searchForm.koujiiraiNo = this.$route.params.工事依頼No.substring(0, 2)
        this.searchForm.koujiiraiNo1 = this.$route.params.工事依頼No.substring(2)
        this.onSearch()
      }
    // ハノイ側修正2022/09/16　STEP2_W　課題管理表№97：設計書「修正履歴」シートの「2022/09/07仕様変更分」承認種類＝「10：依頼承認」の場合 工事件名／依頼内容／不具合状況の画面遷移先修正 End
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
        .get('/api/Reafs_W/Irai/WD00110/GetStartInfo', 'アクセスしています....')
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
          // Get Dropdown中止保留区分
          $select中止保留区分value.中止保留区分value = response.data.名称取得方法15
          this.名称取得方法17 = response.data.名称取得方法17
          this.iraiForm.中止保留区分 = '0'
          // ハノイ側修正2023/07/17　STEP2_W　課題管理表№368：「WD00110：依頼入力画面にて、営業店と物件コードが紐づかない状態で、F100に登録されているデータがあるようです。」START
          if (this.StartupMode === '0') {
            this.iraiForm.店コード = response.data.店コード
            this.iraiForm.店コード枝番 = response.data.店コード枝番
          }
          this.$nextTick(() => {
            this.isInit = false
            if (this.StartupMode === '0') {
              this.$refs.ref営業店.changeEvent(response.data.店コード枝番)
            }
          })
          // ハノイ側修正2023/07/17　STEP2_W　課題管理表№368：「WD00110：依頼入力画面にて、営業店と物件コードが紐づかない状態で、F100に登録されているデータがあるようです。」END
          this.afterInit()
          // ハノイ側修正2022/09/05　STEP2_W　課題管理表№71：設計書「修正履歴」シートの「2022/08/31仕様変更分」データを呼び出した状態でメニューから画面を開くと、再度同じデータが呼び出されてしまう。 Start
          // const pageData = this.$store.getters.getPageData()
          // if (pageData &&
          // pageData.IraiNyuryoku &&
          // pageData.IraiNyuryoku.koujiiraiNo &&
          // pageData.IraiNyuryoku.koujiiraiNo1 &&
          // pageData.依頼No) {
          //   this.searchForm.koujiiraiNo = pageData.IraiNyuryoku.koujiiraiNo
          //   this.searchForm.koujiiraiNo1 = pageData.IraiNyuryoku.koujiiraiNo1
          //   this.onSearch()
          // }
          // ハノイ側修正2022/09/05　STEP2_W　課題管理表№71：設計書「修正履歴」シートの「2022/08/31仕様変更分」データを呼び出した状態でメニューから画面を開くと、再度同じデータが呼び出されてしまう。End
        })

      this.画面入力制御disabled.btn削除 = true
      this.画面入力制御disabled.btn申請 = true
      this.画面入力制御disabled.btn取下 = true
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
      // this.$message.success({
      //   message,
      //   onClose: this.hideLoading
      // })
    },
    hideLoading () {
      this.loading削除 = false
      this.loading_申請 = false
      this.loading_取下 = false
      this.loadingBack = false
      this.loading_登録_更新 = false
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
        // this.$confirm(message, title || '依頼入力', {
        //   cancelButtonText: 'いいえ(N)',
        //   confirmButtonText: 'はい(Y)',
        //   type: 'Confirm'
        // })
        //   .then(() => {
        //     resolve(true)
        //   })
        //   .catch(() => {
        //     resolve(false)
        //   })
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

      for (const item in this.画面入力制御disabledOrg) {
        if (this.画面入力制御rules.制御enabled.hasOwnProperty(item)) {
          // mapping enabled to disabled
          this.画面入力制御disabledOrg[item] =
            !this.画面入力制御rules.制御enabled[item]
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
    checkDspbtn () {
      const checkDspBtn削除_申請 = this.iraiForm.依頼承認チェック
      // ハノイ側修正2023/08/07　STEP2_W　課題管理表№377：設計書「修正履歴」シートの「2023/08/07仕様変更分」「削除ボタン、登録／更新ボタン、申請ボタン押下条件追加。」 
      const checkDspBtn削除_申請2 = this.iraiForm.更新不可チェック 
      if (checkDspBtn削除_申請 === true || checkDspBtn削除_申請2 === true) { 
        this.画面入力制御disabled.btn削除 = true
        this.画面入力制御disabled.btn申請 = true
        this.画面入力制御disabled.btn登録_更新 = true // ハノイ側修正2022/11/22　STEP2_W　課題管理表№221：設計書「修正履歴」シートの「2022/11/21仕様変更分」「登録／更新ボタン押下条件追加。」
      } else {
        this.画面入力制御disabled.btn削除 = false
        this.画面入力制御disabled.btn申請 = false
        this.画面入力制御disabled.btn登録_更新 = false // ハノイ側修正2022/11/22　STEP2_W　課題管理表№221：設計書「修正履歴」シートの「2022/11/21仕様変更分」「登録／更新ボタン押下条件追加。」
      }
      // ハノイ側修正2023/08/07　STEP2_W　課題管理表№377：設計書「修正履歴」シートの「2023/08/07仕様変更分」「削除ボタン、登録／更新ボタン、申請ボタン押下条件追加。」

      const checkDspBtn取下 = this.iraiForm.依頼申請取下チェック
      if (checkDspBtn取下 === true) {
        this.画面入力制御disabled.btn取下 = false
      } else {
        this.画面入力制御disabled.btn取下 = true
      }
    },
    async onSearch () {
      this.savePageSearchData()
      if (this.checkKoji() === false) {
        return false
      }

      let checkExist = false
      await this.http
        .get('/api/Reafs_W/Irai/WD00110/CheckF100Exist', { 工事依頼No: this.searchData.工事依頼No })
        .then((response) => {
          checkExist = response.data.isF100Exist
        })

      if (!checkExist) {
        this.getMsg('E000014').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo.focus()
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
        .get('/api/Reafs_W/Irai/WD00110/GetPageData', this.searchData, 'アクセスしています....')
        .then(async (response) => {
          this.isInit = true
          // ハノイ側修正2022/09/15　STEP2_W　課題管理表№106：工事依頼№の検索ボタン押下時、営業店チェックを追加。START
          if (!await this.check営業店OnSearch(response.data)) {
            return false
          }
          // ハノイ側修正2022/09/15　STEP2_W　課題管理表№106：工事依頼№の検索ボタン押下時、営業店チェックを追加。END
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
          // Get Dropdown中止保留区分
          $select中止保留区分value.中止保留区分value = response.data.名称取得方法15

          this.oldData = JSON.parse(JSON.stringify(response.data))
          response.data.対象箇所 = response.data.不具合場所
          response.data.中止保留区分 = response.data.継続区分
          response.data.中止保留年月日 = response.data.中止年月日

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
          // ハノイ側修正2022/09/05　STEP2_W　課題管理表№75：設計書「修正履歴」シートの「2022/09/01仕様変更分」依頼承認チェックに条件追加 Start
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
          // ハノイ側修正2022/09/05　STEP2_W　課題管理表№75：設計書「修正履歴」シートの「2022/09/01仕様変更分」依頼承認チェックに条件追加 End

          this.iraiForm['依頼入力済※1'] = response.data.入力制御FW042

          this.iraiForm.依頼承認チェック = response.data.依頼承認チェック
          this.iraiForm.依頼申請取下チェック = response.data.依頼申請取下チェック

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
      this.iraiForm.店コード = obj.eigyotenCd
      this.iraiForm.店コード枝番 = obj.eigyotenBango
      this.iraiForm.営業店名 = obj.eigyotenNm
      this.iraiForm.物件コード = ''
      this.iraiForm.物件名 = ''
      this.iraiForm.物件区分 = ''
      this.iraiForm.物件住所Cd = ''
      this.iraiForm.物件住所Nm = ''

      if ((this.iraiForm.店コード == '' || this.iraiForm.店コード.trim() == '') || (this.iraiForm.店コード枝番 == '' || this.iraiForm.店コード枝番.trim() == '')) {
        this.isInit = true
        this.iraiForm.物件コード = ''
        this.iraiForm.物件名 = ''
        this.iraiForm.物件区分 = ''
        this.iraiForm.物件住所Cd = ''
        this.iraiForm.物件住所Nm = ''
        this.$nextTick(() => {
          this.isInit = false
        })
      } else {
        const テナント営業所コード = this.iraiForm.店コード + this.iraiForm.店コード枝番
        if (obj.event === obj.eigyotenBango || (obj.event === obj.eigyotenCd && obj.eigyotenBango !== '')) { // ハノイ側修正2022/08/17　課題管理表№40：「2022/08/03依頼分」「選択可能な営業所に変更したのに、物件名、物件区分、物件住所が表示されない。」
          this.http
            .get('/api/Reafs_W/Irai/WD00110/Check営業店', { 店コード: this.iraiForm.店コード, 店コード枝番: this.iraiForm.店コード枝番 })
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
                      .post('/api/Reafs_W/Irai/WD00110/get営業店CheckData',
                        { lst支社コード: allRange, 店コード: this.iraiForm.店コード, 店コード枝番: this.iraiForm.店コード枝番 })
                      .then((response) => {
                        if (!response.data.range名称取得方法18 ||
                      !response.data.range名称取得方法18.find(x => x.店コード === this.iraiForm.店コード && x.店コード枝番 === this.iraiForm.店コード枝番)) {
                          if (!response.data.店コード) {
                            this.getMsg('E040662').then((ress) => {
                              this.showError(ress.data)
                            })
                          } else {
                            // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 Start
                            this.http
                              .get('/api/Reafs_W/Irai/WD00110/Get物件コードInfo', { ログイン会社コード: this.userInfo.会社コード, テナント営業所コード })
                              .then((response) => {
                                if (response.data !== null) {
                                  this.iraiForm.物件コード = response.data.物件コード
                                  this.iraiForm.物件名 = response.data.物件名
                                  this.iraiForm.物件区分 = response.data.物件区分
                                  this.iraiForm.物件住所Cd = response.data.物件住所Cd
                                  this.iraiForm.物件住所Nm = response.data.物件住所Nm
                                } else {
                                  this.getMsg('E040662').then((ress) => {
                                    this.showError(ress.data)
                                  })
                                }
                              })
                              // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 End
                          }
                        } else {
                          // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 Start
                          this.http
                            .get('/api/Reafs_W/Irai/WD00110/Get物件コードInfo', { ログイン会社コード: this.userInfo.会社コード, テナント営業所コード })
                            .then((response) => {
                              if (response.data !== null) {
                                this.iraiForm.物件コード = response.data.物件コード
                                this.iraiForm.物件名 = response.data.物件名
                                this.iraiForm.物件区分 = response.data.物件区分
                                this.iraiForm.物件住所Cd = response.data.物件住所Cd
                                this.iraiForm.物件住所Nm = response.data.物件住所Nm
                              } else {
                                this.getMsg('E040662').then((ress) => {
                                  this.showError(ress.data)
                                })
                              }
                            })
                          // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 End
                        }
                      })
                  } else {
                    this.http
                      .post('/api/Reafs_W/Irai/WD00110/get営業店CheckData',
                        { lst支社コード: [], 店コード: this.iraiForm.店コード, 店コード枝番: this.iraiForm.店コード枝番 })
                      .then((response) => {
                        if (!response.data.店コード) {
                          this.getMsg('E040662').then((ress) => {
                            this.showError(ress.data)
                          })
                        } else {
                          // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 Start
                          this.http
                            .get('/api/Reafs_W/Irai/WD00110/Get物件コードInfo', { ログイン会社コード: this.userInfo.会社コード, テナント営業所コード })
                            .then((response) => {
                              if (response.data !== null) {
                                this.iraiForm.物件コード = response.data.物件コード
                                this.iraiForm.物件名 = response.data.物件名
                                this.iraiForm.物件区分 = response.data.物件区分
                                this.iraiForm.物件住所Cd = response.data.物件住所Cd
                                this.iraiForm.物件住所Nm = response.data.物件住所Nm
                              } else {
                                this.getMsg('E040662').then((ress) => {
                                  this.showError(ress.data)
                                })
                              }
                            })
                            // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 End
                        }
                      })
                  }
                } else {
                  // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 Start
                  this.http
                    .get('/api/Reafs_W/Irai/WD00110/Get物件コードInfo', { ログイン会社コード: this.userInfo.会社コード, テナント営業所コード })
                    .then((response) => {
                      if (response.data !== null) {
                        this.iraiForm.物件コード = response.data.物件コード
                        this.iraiForm.物件名 = response.data.物件名
                        this.iraiForm.物件区分 = response.data.物件区分
                        this.iraiForm.物件住所Cd = response.data.物件住所Cd
                        this.iraiForm.物件住所Nm = response.data.物件住所Nm
                      } else {
                        this.getMsg('E040662').then((ress) => {
                          this.showError(ress.data)
                        })
                      }
                    })
                    // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 End
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
          .get('/api/Reafs_W/Irai/WD00110/GetDataN011_12', { 社員コード: this.iraiForm.依頼者コード })
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
      this.iraiForm.依頼者代理コード = obj.txtCd
      this.iraiForm.依頼者代理名 = obj.lblNm
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
          .get('/api/Reafs_W/Irai/WD00110/GetDataN011_12', { 社員コード: this.iraiForm.依頼者代理コード })
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
    savePageSearchData () {
      this.$store.commit('setPageData', {
        IraiNyuryoku: this.searchForm,
        依頼No: this.iraiForm.依頼No
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
        // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」Start
        const dataCheckM020 = await this.getDataCheckM020()
        if (this.isInit === false) {
          // 2023/02/13 M.Ootsuka 緊急6項目は時間条件と日付条件を両方満たしている場合のみ選択可能に修正 「2023/02/13仕様変更分」
          if (!(timeNow >= this.m001Info.急便始業時間 && timeNow <= this.m001Info.急便終業時間) || !dataCheckM020) {
            this.getMsg('E040671').then((response) => {
              this.showError(response.data)
            })
            this.$refs.ref工事区分.focus()
            return false
          }
        }
        // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」End
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
        this.$nextTick(() => {
          this.isInit = false
        })
      } else {
        this.画面入力制御disabled.大区分 = this.画面入力制御disabledOrg.大区分
        this.画面入力制御disabled.中区分 = this.画面入力制御disabledOrg.中区分
        this.画面入力制御disabled.小区分 = this.画面入力制御disabledOrg.小区分
        this.画面入力制御disabled.依頼状況 = this.画面入力制御disabledOrg.依頼状況
      }
    },
    selectDaiKubun (val) {
      // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 Start
      var selected大区分 = this.iraiForm.大区分
      var current大区分 = $select大区分value.大区分value.filter(function (item) { return item.名称コード === selected大区分 }).pop()
      if (current大区分.備考1 !== '1' && (this.iraiForm.工事区分 === '30' || this.iraiForm.工事区分 === '31')) {
        this.getMsg('E040534').then((response) => {
          this.showError(response.data)
        })
      }
      // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 End
      // Get Dropdown中区分
      this.http
        .get('/api/Reafs_W/Irai/WD00110/GetDropdown中区分', { 大区分: this.iraiForm.大区分 })
        .then((response) => {
          $select中区分value.中区分value = response.data
          this.iraiForm.中区分 = 'ー'
          this.iraiForm.小区分 = 'ー'
        })
    },
    selectChuKubun (val) {
      // Get Dropdown小区分
      this.http
        .get('/api/Reafs_W/Irai/WD00110/GetDropdown小区分', { 大区分: this.iraiForm.大区分, 中区分: this.iraiForm.中区分 })
        .then((response) => {
          $select小区分value.小区分value = response.data
          this.iraiForm.小区分 = 'ー'
        })
    },
    selectChushiHoryuKubun (val) {
      if (this.isUpdate) {
        if (this.iraiForm.中止保留区分 === '1' || this.iraiForm.中止保留区分 === '2') {
          this.set画面入力制御(false)
        } else if (this.iraiForm.中止保留区分 === '0') {
          this.iraiForm.中止保留年月日 = ''
          this.iraiForm.中止保留事由 = ''
          this.$refs.refIraiForm.clearValidate(['中止保留年月日', '中止保留事由'])
          this.set画面入力制御(true)
        }
      }
    },
    async onDelete () {
      this.loading削除 = true
      let result = ''

      if (this.checkKoji() === false) {
        return false
      }

      if (this.isUpdate && this.oldData.依頼No !== (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1)) {
        this.getMsg('E000012').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo.focus()
        return false
      }

      let msg = (await this.getMsg('K000002')).data
      if (await this.confirm(msg)) {
        await this.http
          .post('/api/Reafs_W/Irai/WD00110/Detele', this.searchData, 'アクセスしています....')
          .then((response) => {
            result = response.status
            this.loading削除 = false
          })
        if (result) {
          let msgM040004 = (await this.getMsg('M040004')).data
          let msgM000005 = (await this.getMsg('M000005')).data
          this.showSuccess(msgM040004 + this.searchData.工事依頼No + msgM000005)
          this.searchForm.koujiiraiNo = ''
          this.searchForm.koujiiraiNo1 = ''
          this.savePageSearchData()
          this.reInit()
          this.init()
        } else {
          return false
        }
      } else {
        this.loading削除 = false
      }
      return false
    },
    async regisValidate (mode) {
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
      if (!await this.check参照権限(this.iraiForm.店コード, this.iraiForm.店コード枝番)) {
        return false
      }
      // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 Start
      if (this.iraiForm.店コード && this.iraiForm.店コード枝番) {
        const dataCheck物件コード = await this.getDataCheck物件コード()
        if (!dataCheck物件コード) {
          this.getMsg('E040662').then((ress) => {
            this.showError(ress.data, {callback: () => {
              this.$refs.ref営業店.focus()
            }})
          })
          return false
        }
      }
      // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 End
      // 工事依頼№
      if (this.isUpdate && this.oldData.依頼No !== (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1)) {
        this.getMsg('E000012').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo.focus()
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
        // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」Start
        const dataCheckM020 = await this.getDataCheckM020()
        if (!(timeNow >= this.m001Info.急便始業時間 && timeNow <= this.m001Info.急便終業時間) && !dataCheckM020) {
          this.getMsg('E040671').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref工事区分.focus()
          return false
        }
        // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」End
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
      // ハノイ側修正2022/09/05　STEP2_W　課題管理表№70：設計書「修正履歴」シートの「2022/08/31仕様変更分」存在しない依頼者を登録出来てしまう。 Start
      if (this.iraiForm.依頼者コード) {
        const check = await this.GetDataNo11_12(this.iraiForm.依頼者コード)
        if (!check) {
          this.getMsg('E040020').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref依頼者コード.focus()
            }})
          })
          return false
        }
      }
      // 依頼者代理コード
      if (this.iraiForm.依頼者代理コード) {
        const check = await this.GetDataNo11_12(this.iraiForm.依頼者代理コード)
        if (!check) {
          this.getMsg('E040457').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.ref依頼者代理コード.focus()
            }})
          })
          return false
        }
      }
      // ハノイ側修正2022/09/05　STEP2_W　課題管理表№70：設計書「修正履歴」シートの「2022/08/31仕様変更分」存在しない依頼者を登録出来てしまう。 End
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
      // 依頼者代理連絡先
      if (!this.画面入力制御disabled.依頼者代理連絡先 && !this.base.isSimpleByte(this.iraiForm.依頼者代理連絡先)) {
        this.getMsg('E040460').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref依頼者代理連絡先.focus()
        return false
      }
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
      // 中止保留年月日
      if (this.isUpdate && this.iraiForm.中止保留区分 !== '0' && !this.iraiForm.中止保留年月日) {
        this.getMsg('E040003').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref中止保留年月日.focus()
        return false
      }

      if (this.isUpdate && this.iraiForm.中止保留区分 !== '0' && !this.iraiForm.中止保留年月日) {
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
      // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… Start
      if (mode !== '申請') {
        if (this.iraiForm.工事区分 && this.iraiForm.工事区分.charAt(0) != '4' &&
      !this.iraiForm.部位コメント1 && !this.iraiForm.部位コメント2 &&
      !this.iraiForm.部位コメント3 && !this.iraiForm.部位コメント4) {
          this.getMsg('E040789').then((response) => {
            this.showError(response.data)
          })
          this.$refs.ref添付登録参照.$el.focus()
          return false
        }
      }
      // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… End
      if (this.isUpdate && !await this.checkVersion()) {
        this.getMsg('E040098').then((response) => {
          this.showError(response.data)
        })
        return false
      }
      return true
    },
    async GetM018削除区分 (区分コード, 名称コード, 連動区分) {
      return new Promise((resolve) => {
        this.http
          .post('/api/Reafs_W/Irai/WD00110/GetM018削除区分',
            { 区分コード: 区分コード, 名称コード: 名称コード, 連動区分: 連動区分 })
          .then((response) => {
            return resolve(response.data)
          })
      })
    },
    // ハノイ側修正2022/09/05　STEP2_W　課題管理表№70：設計書「修正履歴」シートの「2022/08/31仕様変更分」存在しない依頼者を登録出来てしまう。 Start
    async GetDataNo11_12 (依頼者コード) {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00110/GetDataN011_12', { 社員コード: 依頼者コード })
          .then((response) => {
            if (response.data.名称取得方法11 !== null) {
              return resolve(true)
            } else {
              return resolve(false)
            }
          })
      })
    },
    // ハノイ側修正2022/09/05　STEP2_W　課題管理表№70：設計書「修正履歴」シートの「2022/08/31仕様変更分」存在しない依頼者を登録出来てしまう。 End
    checkVersion () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00110/CheckF100Version',
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
    get営業店CheckData (allRange, 店コード, 店コード枝番) {
      return new Promise((resolve) => {
        this.http
          .post('/api/Reafs_W/Irai/WD00110/get営業店CheckData',
            { lst支社コード: allRange, 店コード: 店コード, 店コード枝番: 店コード枝番 })
          .then((response) => {
            return resolve(response)
          })
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
            if (!isCheckFromSearch) {
              this.$refs.ref営業店.eigyotenCdFocus()
            }
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
          .get('/api/Reafs_W/Irai/WD00110/Check営業店', { 店コード: 店コード, 店コード枝番: 店コード枝番 })
          .then((response) => {
            resolve(response.data)
          })
      })
    },
    // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」Start
    async getDataCheckM020 () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00110/CheckM020Data')
          .then((response) => {
            if (response.data) {
              resolve(true)
            } else {
              resolve(false)
            }
          })
      })
    },
    // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」End
    // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 Start
    getDataCheck物件コード () {
      return new Promise((resolve) => {
        const テナント営業所コード = this.iraiForm.店コード + this.iraiForm.店コード枝番
        this.http
          .get('/api/Reafs_W/Irai/WD00110/Get物件コードInfo', { ログイン会社コード: this.userInfo.会社コード, テナント営業所コード })
          .then((response) => {
            resolve(response.data)
          })
      })
    },
    // ハノイ側修正2022/08/04　STEP2_W　課題管理表№23：設計書「修正履歴」シートの「2022/08/03仕様変更分」営業店チェック追加 End
    async onRegister (mode) {
      if (mode === '申請') {
        this.loading_登録_更新 = false
      } else {
        this.loading_登録_更新 = true
      }
      let type = 1
      if (!await this.regisValidate(mode)) {
        this.loading_登録_更新 = false
        return false
      }
      if (mode === '申請') {
        let msg = (await this.getMsg('K040059')).data
        if (!await this.confirm(msg)) {
          return false
        }
      } else {
        // insert
        if (!this.isUpdate) {
          let msg = (await this.getMsg('K000004')).data
          if (!await this.confirm(msg)) {
            this.loading_登録_更新 = false
            return false
          }
          type = 1
          // ハノイ側修正2022/05/16　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」Start
          if (this.iraiForm.工事区分 === '13' || this.iraiForm.工事区分 === '23') {
            const today = new Date()
            const timeNow = ('0' + today.getHours().toString()).slice(-2) + ':' + ('0' + today.getMinutes().toString()).slice(-2)
            // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」Start
            const dataCheckM020 = await this.getDataCheckM020()
            if (!(timeNow >= this.m001Info.急便始業時間 && timeNow <= this.m001Info.急便終業時間) && !dataCheckM020) {
              this.getMsg('E040671').then((response) => {
                this.showError(response.data)
              })
              this.$refs.ref工事区分.focus()
              return false
            }
            // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」End
          }
          // ハノイ側修正2022/05/16　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」End
          // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 Start
          var selected大区分 = this.iraiForm.大区分
          var current大区分 = $select大区分value.大区分value.filter(function (item) { return item.名称コード === selected大区分 }).pop()
          if (current大区分.備考1 !== '1' && (this.iraiForm.工事区分 === '30' || this.iraiForm.工事区分 === '31')) {
            this.getMsg('E040534').then((response) => {
              this.showError(response.data)
            })
            this.$refs.ref大区分.focus()
            return false
          }
          // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 End
        }
        // update
        if (this.isUpdate) {
          let msg = ''
          if (this.iraiForm.中止保留区分 === '0') {
            msg = (await this.getMsg('K000005')).data
            type = 2
          } else if (this.iraiForm.中止保留区分 === '1') {
            msg = (await this.getMsg('K040010')).data
            type = 3
          } else if (this.iraiForm.中止保留区分 === '2') {
            msg = (await this.getMsg('K040009')).data
            type = 4
          }
          // ハノイ側修正2022/05/16　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」Start
          if (this.iraiForm.工事区分 === '13' || this.iraiForm.工事区分 === '23') {
            const today = new Date()
            const timeNow = ('0' + today.getHours().toString()).slice(-2) + ':' + ('0' + today.getMinutes().toString()).slice(-2)
            // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」Start
            const dataCheckM020 = await this.getDataCheckM020()
            if (!(timeNow >= this.m001Info.急便始業時間 && timeNow <= this.m001Info.急便終業時間) && !dataCheckM020) {
              this.getMsg('E040671').then((response) => {
                this.showError(response.data)
              })
              this.$refs.ref工事区分.focus()
              return false
            }
            // ハノイ側修正2022/12/12　STEP2_W　課題管理表№241：設計書「修正履歴」シートの「2022/07/14仕様変更分」「変更コメント、否認・差戻事由追加, 工事区分の緊急6項目のチェック条件修正」End
          }
          // ハノイ側修正2022/05/16　課題管理表№3：「2022/5/13依頼分」「営業所、工事区分のチェックタイミングを追加」End
          // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 Start
          var selected大区分 = this.iraiForm.大区分
          var current大区分 = $select大区分value.大区分value.filter(function (item) { return item.名称コード === selected大区分 }).pop()
          if (current大区分.備考1 !== '1' && (this.iraiForm.工事区分 === '30' || this.iraiForm.工事区分 === '31')) {
            this.getMsg('E040534').then((response) => {
              this.showError(response.data)
            })
            this.$refs.ref大区分.focus()
            return false
          }
          // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 End
          if (!await this.confirm(msg)) {
            this.loading_登録_更新 = false
            return false
          }
        }
      }
      const data = {
        F100_依頼マスタ: this.getDataF100()
      }
      // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… Start
      if (!this.isUpdate) {
        const sessionStr = this.$store.getters.getIraiTenpuTourokuPageData()
        if (sessionStr) {
          data.F091Data = sessionStr.f091Data
        }
        data.old依頼No = this.oldData.依頼No
      }
      // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… End
      const url = this.isUpdate ? '/api/Reafs_W/Irai/WD00110/Update' : '/api/Reafs_W/Irai/WD00110/Insert'
      let res = {}
      await this.http
        .post(url, data, 'アクセスしています....')
        .then((response) => {
          res = response
        })
      const msg004 = (await this.getMsg('M040004')).data
      if (res.status) {
        if (mode !== '申請') {
          if (type === 1) {
            this.showSuccess(msg004 + res.data + (await this.getMsg('M040001')).data)
            this.searchForm.koujiiraiNo = res.data.substr(0, 2)
            this.searchForm.koujiiraiNo1 = res.data.substr(2)
          } else if (type === 2) {
            this.showSuccess(msg004 + res.data + (await this.getMsg('M040002')).data)
          } else if (type === 3) {
            this.showSuccess(msg004 + res.data + (await this.getMsg('M040012')).data)
          } else if (type === 4) {
            this.showSuccess(msg004 + res.data + (await this.getMsg('M040011')).data)
          }
          this.onSearch()
        }
        return true
      } else {
        return false
      }
    },
    ddlValue (val) {
      if (val) {
        val = val.replace('ー', '')
      }
      return val
    },
    getDataF100 () {
      return {
        会社コード: this.userInfo.会社コード,
        依頼No: (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1),
        営業店コード: this.iraiForm.店コード,
        営業店コード枝番: this.iraiForm.店コード枝番,
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
        削除区分: !this.isUpdate ? '0'
          : this.iraiForm.中止保留区分 === '0' ? '0'
            : this.iraiForm.中止保留区分 === '1' ? '3' : '2',
        SGR担当事務所コード: '',
        SGR担当営業所コード: '',
        SGR担当部コード: '',
        SGR担当課コード: '',
        SGR担当係コード: ''
      }
    },
    getDataInsertFW042 (依頼日) {
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
        区分: '1',
        申請日: this.formatDate(new Date()),
        申請時間: (new Date()).toLocaleTimeString('en-US', { hour12: false }),
        申請区分: '',
        承認区分: '',
        物件コード: this.iraiForm.物件コード,
        表示日付: 依頼日,
        削除区分: !this.isUpdate ? '0'
          : this.iraiForm.中止保留区分 === '0' ? '0'
            : this.iraiForm.中止保留区分 === '1' ? '3' : '2'
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
    async onApply () {
      this.loading_申請 = true
      let result = ''
      let msgErr = ''

      if (this.checkKoji() === false) {
        return false
      }

      // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 Start
      var selected大区分 = this.iraiForm.大区分
      var current大区分 = $select大区分value.大区分value.filter(function (item) { return item.名称コード === selected大区分 }).pop()
      if (current大区分.備考1 !== '1' && (this.iraiForm.工事区分 === '30' || this.iraiForm.工事区分 === '31')) {
        this.getMsg('E040534').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref大区分.focus()
        return false
      }
      // ハノイ側修正2022/08/23　STEP2_W　課題管理表№53：設計書「修正履歴」シートの「2022/08/22仕様変更分」工事区分「スポット清掃」の時、大区分は「定期」とするチェック追加 End

      if (!await this.onRegister('申請')) {
        this.loading_申請 = false
        return false
      }

      const data = {
        ログイン会社コード: this.userInfo.会社コード,
        工事依頼No: this.searchData.工事依頼No,
        中止保留区分: this.iraiForm.中止保留区分,
        物件コード: this.iraiForm.物件コード,
        表示日付: this.formatDate(new Date())
      }
      await this.http
        .post('/api/Reafs_W/Irai/WD00110/UpdIns_申請', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
          this.loading_申請 = false
        })
      if (result) {
        let msgM040004 = (await this.getMsg('M040004')).data
        let msgM040007 = (await this.getMsg('M040007')).data
        this.showSuccess(msgM040004 + this.searchData.工事依頼No + msgM040007)
        await this.sendMail() // ハノイ側修正2022/10/24　STEP2_W　課題管理表№187：設計書「修正履歴」シートの「2022/10/21仕様変更分」◆メール送信について追加
        this.onSearch()
      } else {
        this.showError(msgErr)
        return false
      }
      this.loading_申請 = false
      return false
    },
    async onWithdrawal () {
      this.loading_取下 = true
      let result = ''
      let msgErr = ''

      if (this.checkKoji() === false) {
        return false
      }

      if (this.isUpdate && this.oldData.依頼No !== (this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1)) {
        this.getMsg('E000012').then((response) => {
          this.showError(response.data)
        })
        this.$refs.koujiiraiNo.focus()
        return false
      }

      let msg = (await this.getMsg('K040060')).data
      if (await this.confirm(msg)) {
        const data = {
          ログイン会社コード: this.userInfo.会社コード,
          工事依頼No: this.searchData.工事依頼No,
          中止保留区分: this.iraiForm.中止保留区分
        }
        await this.http
          .post('/api/Reafs_W/Irai/WD00110/Update_取下', data, 'アクセスしています....')
          .then((response) => {
            result = response.status
            msgErr = response.message
            this.loading_取下 = false
          })
        if (result) {
          let msgM040004 = (await this.getMsg('M040004')).data
          let msgM040008 = (await this.getMsg('M040008')).data
          this.showSuccess(msgM040004 + this.searchData.工事依頼No + msgM040008)
          this.onSearch()
        } else {
          this.showError(msgErr)
          return false
        }
      } else {
        this.loading_取下 = false
      }
      return false
    },
    onViewAttachRegis () {
      this.paramsIraiTenpuTouroku.工事依頼No = this.iraiForm.依頼No
      this.paramsIraiTenpuTouroku.isDisableUpdate = !this.searchForm.koujiiraiNo || !this.searchForm.koujiiraiNo1 ? 1 : 0// ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定…
      this.showDialogIraiTenpuTouroku = true
    },
    // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… Start
    IraiTenpuTourokuClose () {
      if (this.oldData.依頼No) {
        this.http
          .get('/api/Reafs_W/Irai/WD00110/Get名称取得方法16',
            { 会社コード: this.iraiForm.会社コード, 工事依頼No: this.oldData.依頼No })
          .then((response) => {
            if (response.status) {
              this.iraiForm.部位コメント1 = ''
              this.iraiForm.部位コメント2 = ''
              this.iraiForm.部位コメント3 = ''
              this.iraiForm.部位コメント4 = ''

              if (response.data.length > 0) {
                var lst部位コメント = response.data.values()
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
              }
              this.$forceUpdate()
            }
          })
      } else {
        // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… Start
        const sessionStr = this.$store.getters.getIraiTenpuTourokuPageData()
        if (sessionStr) {
          const pageData = sessionStr.pageData
          for (let i = 0; i < pageData.length; i++) {
            const item = pageData[i]
            if (i == 0) {
              this.iraiForm.部位コメント1 = item.ファイル名
            } else if (i == 1) {
              this.iraiForm.部位コメント2 = item.ファイル名
            } else if (i == 2) {
              this.iraiForm.部位コメント3 = item.ファイル名
            } else if (i == 3) {
              this.iraiForm.部位コメント4 = item.ファイル名
            }
          }
        }
        // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… End
      }
    },
    // ハノイ側修正2023/03/03　STEP2_W　課題管理表№300：設計書「修正履歴」シートの「2023/03/01仕様変更分」№16修正 添付元ファイル名に変更 営業店コード初期値設定… End
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
    async sendMail () {
      const f100 = this.getDataF100()
      const mailPatternNo = f100.工事区分 == '11' || f100.工事区分 == '41' || f100.工事区分 == '42' ? '1' // ハノイ側修正2022/10/27　STEP2_W　課題管理表№195：設計書「修正履歴」シートの「2022/10/27仕様変更分」「定期分のパラメータ追加」
        : f100.工事区分 == '12' ? '36'
          : f100.工事区分 == '13' ? '59'
            : f100.工事区分 == '31' ? '80' : ''
      // eslint-disable-next-line camelcase
      if (!mailPatternNo) {
        return
      }
      const Reafs_W_BaseURL = this.commonFunctionUI.getBaseURL('W')
      const st_kbn = f100.工事区分 == '41' || f100.工事区分 == '42' ? 2 : 1
      const url = []
      let linkUrl = ''
      if (st_kbn == 1) {
        linkUrl = Reafs_W_BaseURL + 'SyuzenIraiSansyoShonin'
      } else {
        linkUrl = Reafs_W_BaseURL + 'TeikiIraiSansyoShonin'
      }
      let paramSet = {
        パラメータ1: f100.依頼No,
        PG: 'WD00110'
      }
      const resParam = await this.commonFunctionUI.setQueryParameter(paramSet)
      linkUrl = linkUrl + '?guid=' + resParam.data.guid
      linkUrl = encodeURI(linkUrl)
      url.push(linkUrl)
      const param = {
        msg_type: 0,
        mail_to: '',
        msg_head: '',
        msg_body: '',
        pg_id: 'WD00110',
        st_kbn: st_kbn, // ハノイ側修正2022/10/27　STEP2_W　課題管理表№195：設計書「修正履歴」シートの「2022/10/27仕様変更分」「定期分のパラメータ追加」
        mail_pattern_no: mailPatternNo,
        工事依頼NO: f100.依頼No,
        // 工事依頼NO枝番:
        // 契約NO
        // 履歴NO
        // 明細NO
        // 契約年月
        宛先事務所コード: [this.iraiForm.ログイン者事務所コード],
        宛先営業所コード: [this.iraiForm.ログイン者営業所コード],
        宛先部コード: [this.iraiForm.ログイン者部コード],
        宛先課コード: [this.iraiForm.ログイン者課コード],
        宛先係コード: [this.iraiForm.ログイン者係コード],
        // 業者コード
        // 業者コード枝番
        担当者コード: [],
        url: url
      }
      const res = await this.commonFunctionUI.CM00110SendMail(param)
      return res
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
  padding-left: 15px;
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
  padding: 0 0 11px 0;
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
  .IraiNyuryoku .依頼者 > div > div > div >button {
    padding: 0 12px 0 12px !important;
  }
  .IraiNyuryoku .依頼者代理 > div > div > div > button {
    padding: 0 12px 0 12px !important;
  }
  .IraiNyuryoku .append-background {
    width: 212px !important;
  }

    .KojiKubunSetsumeiIchiran .el-dialog {
    margin-top: 97px !important;
    margin-left: 255px !important;
  }

  .IraiNyuryoku .営業店 .append-background {
    width: 202px !important;
  }
</style>
