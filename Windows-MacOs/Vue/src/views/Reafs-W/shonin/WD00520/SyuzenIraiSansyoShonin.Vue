<template>
  <div class="home-container SyuzenIraiSansyoShonin">
    <el-form
      :model="syuzenForm"
      ref="refSyuzenForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk="true">
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row>
            <el-col style="width: 31%;">
              <el-form-item label="営業所" prop="営業店コード" class="is-readonly">
                <div style="display: flex">
                  <reafsinputtext
                    v-model="syuzenForm.営業店コード"
                    :width="'75px'"
                    disabled
                    >
                  </reafsinputtext>
                  <reafsinputtext
                    class="none-border-left"
                    v-model="syuzenForm.営業店コード枝番"
                    :width="'50px'"
                    disabled
                    >
                  </reafsinputtext>
                  <reafsinputtext
                    class="none-border-left"
                    :title="syuzenForm.営業店名"
                    v-model="syuzenForm.営業店名"
                    :width="'94%'"
                    disabled
                  >
                  </reafsinputtext>
                </div>
              </el-form-item>
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
                      tabindex="1"
                      maxlength="2"
                      v-model="searchForm.koujiiraiNo"
                      :width="'48px'"
                      ref="koujiiraiNo"
                      @blur="onBlurKoujiiraiNoNendo"
                    >
                    </reafsinputtext>
                    <reafsinputtext
                      class="none-border-left"
                      tabindex="2"
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
              <el-col style="width: 6.95%; padding-left: 25px; padding-top: 2px;">
                <el-button tabindex="3" type="primary" size="mini"
                 :disabled="!isKojiIraiInput"
                 @click="onSearch(true)">検索</el-button>
              </el-col>
            </el-form>
            <el-col style="width: 22%;">
              <el-form-item
                label="進捗（本体）"
                prop="shinchokuHontai"
                class="is-readonly"
              >
                <reafsinputtext
                  :title="syuzenForm.進捗_本体"
                  v-model="syuzenForm.進捗_本体"
                  disabled
                  :width="'100%'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="5" style="padding-left: 10px; width: 23%">
              <el-form-item
                label="進捗（発注）"
                prop="shinchokuHatchu"
                class="is-readonly"
              >
                <reafsinputtext
                  :title="syuzenForm.進捗_発注"
                  v-model="syuzenForm.進捗_発注"
                  disabled
                  :width="'100%'"
                >
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col style="width: 31%;">
              <el-form-item label="物件名" prop="bukken" class="is-readonly">
                <div style="display: flex">
                  <reafsinputtext
                    v-model="syuzenForm.物件コード"
                    maxlength="8"
                    disabled
                    :width="'112px'">
                  </reafsinputtext>
                  <reafsinputtext
                    class="none-border-left"
                    :title="syuzenForm.物件名"
                    v-model="syuzenForm.物件名"
                    maxlength="50"
                    disabled
                    :width="'94%'">
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
            <el-col style="width: 23.6%;">
              <el-form-item
                label="物件区分"
                prop="bukkenKbn"
                class="is-readonly"
              >
                <reafsinputtext
                  :title="syuzenForm.物件区分"
                  v-model="syuzenForm.物件区分"
                  maxlength="20"
                  disabled
                  :width="'120px'">
                </reafsinputtext>
              </el-form-item>
            </el-col>
            <el-col :span="10" style="width: 45%;">
              <el-form-item
                label="物件住所"
                prop="bukkenAddress"
                class="is-readonly"
                :width="'100%'"
              >
                <div style="display: flex; width: 100%">
                  <reafsinputtext
                    v-model="syuzenForm.物件住所Cd"
                    maxlength="8"
                    disabled
                    :width="'110px'">
                  </reafsinputtext>
                  <reafsinputtext
                    class="none-border-left"
                    style="width: 100%"
                    :title="syuzenForm.物件住所Nm"
                    v-model="syuzenForm.物件住所Nm"
                    maxlength="100"
                    disabled
                    :width="'100%'">
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-header>
        <el-main class="home-el-main">
          <el-tabs type="card" v-model="activeName" :before-leave="beforeTabLeave">
            <el-tab-pane label="依頼" name="irai">
              <el-row>
                <el-col :span="8">
                  <el-form-item label="工事区分" prop="工事区分" class="is-readonly">
                    <el-select
                      v-model="iraiForm.工事区分"
                      :disabled="true"
                      ref="ref工事区分"
                      style="width: 80%;"
                      placeholder=""
                    >
                      <!-- <el-option
                        v-for="item in 工事区分value"
                        :key="item.名称コード"
                        :label="item.名称コード_名称"
                        :value="item.名称コード"
                      >
                      </el-option> -->
                    </el-select>
                    <el-button :disabled="true" class="btnHelp">？</el-button>
                  </el-form-item>
                  <el-form-item label="大区分" prop="大区分" class="is-readonly">
                    <el-select
                      v-model="iraiForm.大区分"
                      :disabled="true"
                      ref="ref大区分"
                      style="width: 80%;"
                      placeholder=""
                    >
                      <!-- <el-option
                        v-for="item in 大区分value"
                        :key="item.名称コード"
                        :label="item.名称コード_名称"
                        :value="item.名称コード"
                      >
                      </el-option> -->
                    </el-select>
                  </el-form-item>
                  <el-form-item label="中区分" prop="中区分" class="is-readonly">
                    <el-select
                      v-model="iraiForm.中区分"
                      :disabled="true"
                      ref="ref中区分"
                      style="width: 80%;"
                      placeholder=""
                    >
                      <!-- <el-option
                        v-for="item in 中区分value"
                        :key="item.名称コード"
                        :label="item.名称コード_名称"
                        :value="item.名称コード"
                      >
                      </el-option> -->
                    </el-select>
                  </el-form-item>
                  <el-form-item label="小区分" prop="小区分" class="is-readonly">
                    <el-select
                      v-model="iraiForm.小区分"
                      :disabled="true"
                      ref="ref小区分"
                      style="width: 80%;"
                      placeholder=""
                    >
                      <!-- <el-option
                        v-for="item in 小区分value"
                        :key="item.名称コード"
                        :label="item.名称コード_名称"
                        :value="item.名称コード"
                      >
                      </el-option> -->
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="13" style="padding-left: 7px">
                  <el-form-item label="依頼状況" prop="依頼状況" class="is-readonly">
                    <el-select
                      v-model="iraiForm.依頼状況"
                      :disabled="true"
                      style="width: 80%"
                      ref="ref依頼状況"
                      placeholder=""
                    >
                      <!-- <el-option
                        v-for="item in 依頼状況value"
                        :key="item.名称コード"
                        :label="item.名称コード_名称"
                        :value="item.名称コード"
                      >
                      </el-option> -->
                    </el-select>
                  </el-form-item>
                  <el-form-item label="SGR担当部署" prop="SGR担当部署" class="is-readonly">
                    <el-select
                      v-model="iraiForm.sgr担当部署"
                      :disabled="true"
                      style="width: 55%"
                      ref="refSGR担当部署"
                      placeholder=""
                    >
                      <!-- <el-option
                        v-for="item in SGR担当部署value"
                        :key="item.名称コード"
                        :label="item.担当部署"
                        :value="item.名称コード"
                      >
                      </el-option> -->
                    </el-select>
                  </el-form-item>
                  <el-form-item label="修繕区分大" prop="修繕区分大" class="is-readonly">
                    <reafsinputtext
                      v-model="iraiForm.修繕区分大"
                      :width="'200px'"
                      disabled="disabled"
                    >
                    </reafsinputtext>
                  </el-form-item>
                  <el-col style="width: 50%" >
                    <el-form-item label="修繕区分小" prop="修繕区分小" class="is-readonly">
                      <reafsinputtext
                        v-model="iraiForm.修繕区分小"
                        :width="'200px'"
                        disabled="disabled"
                      >
                      </reafsinputtext>
                    </el-form-item>
                  </el-col>
                  <el-col style="width: 40%">
                    <el-form-item label="負担区分" prop="負担区分" class="is-readonly">
                      <reafsinputtext
                        v-model="iraiForm.負担区分"
                        :width="'200px'"
                        disabled="disabled"
                      >
                      </reafsinputtext>
                    </el-form-item>
                  </el-col>
                  <!-- <div style="width: 1px; height: 92px"></div>                  -->
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="依頼者" prop="依頼者コード" class="is-readonly">
                    <div style="display: flex">
                      <reafsinputtext
                        maxlength="7"
                        v-model="iraiForm.依頼者コード"
                        :disabled="true"
                        :width="'112px'"
                        ref="ref依頼者コード"
                      >
                      </reafsinputtext>
                      <reafsinputtext
                        :title="iraiForm.依頼者名"
                        v-model="iraiForm.依頼者名"
                        :width="'97%'"
                        class="is-readonly none-border-left"
                        disabled="disabled"
                      >
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                  <el-form-item label="依頼者代理" prop="依頼者代理コード" class="is-readonly">
                    <div style="display: flex">
                      <reafsinputtext
                        maxlength="7"
                        :title="iraiForm.依頼者代理コード"
                        v-model="iraiForm.依頼者代理コード"
                        :disabled="true"
                        :width="'112px'"
                        ref="ref依頼者代理コード"
                        >
                      </reafsinputtext>
                      <reafsinputtext
                        :title="iraiForm.依頼者代理名"
                        v-model="iraiForm.依頼者代理名"
                        :width="'97%'"
                        class="is-readonly none-border-left"
                        disabled="disabled"
                      >
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6" style="padding-left: 7px;">
                  <el-form-item label-width="0" class="is-readonly">
                    <reafsinputtext
                      :title="iraiForm.依頼者所属部署名"
                      v-model="iraiForm.依頼者所属部署名"
                      :width="'320px'"
                      disabled="disabled"
                    >
                    </reafsinputtext>
                  </el-form-item>
                  <el-form-item label-width="0" class="is-readonly">
                    <reafsinputtext
                      :title="iraiForm.依頼者代理所属部署名"
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
                      :title="iraiForm.依頼者代理役職名"
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
                    class="is-readonly"
                  >
                    <reafsinputtext
                      type="tel"
                      maxlength="20"
                      :title="iraiForm.依頼者連絡先"
                      v-model="iraiForm.依頼者連絡先"
                      :disabled="true"
                      :width="'150px'"
                      ref="ref依頼者連絡先"
                    ></reafsinputtext>
                  </el-form-item>
                  <el-form-item
                    label="連絡先"
                    prop="依頼者代理連絡先"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      type="tel"
                      maxlength="20"
                      :title="iraiForm.依頼者代理連絡先"
                      v-model="iraiForm.依頼者代理連絡先"
                      :disabled="true"
                      :width="'150px'"
                      ref="ref依頼者代理連絡先"
                    ></reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="14">
                  <el-form-item label="依頼コメント" class="is-readonly">
                    <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
                    <reafsinputtext
                      maxlength="50"
                      :title="iraiForm.依頼コメント"
                      v-model="iraiForm.依頼コメント"
                      :disabled="true"
                      :width="'100%'"
                    ></reafsinputtext>
                  </el-form-item>
                  <el-form-item
                    label="対象箇所"
                    prop="対象箇所"
                    class="is-readonly"
                  >
                  <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
                    <reafsinputtext
                      maxlength="50"
                      v-model="iraiForm.対象箇所"
                      :disabled="true"
                      :width="'100%'"
                      ref="ref対象箇所"
                    ></reafsinputtext>
                  </el-form-item>
                  <el-form-item label="メーカー名" class="is-readonly">
                    <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№45：「2022/08/17依頼分」レイアウト修正依頼 -->
                    <reafsinputtext
                      maxlength="50"
                      :title="iraiForm.メーカー名"
                      v-model="iraiForm.メーカー名"
                      :disabled="true"
                      :width="'100%'"
                    ></reafsinputtext>
                  </el-form-item>
                  <el-form-item class="is-readonly is-area" prop="依頼内容_不具合状況" label="依頼内容／ 不具合状況" id="依頼内容_不具合状況"
                  >
                      <el-input
                        class="item_input_iraiNaiyo_fuguaiJokyo"
                        maxlength="200"
                        type="textarea"
                        resize="none"
                        :rows="3"
                        v-model="iraiForm.依頼内容_不具合状況"
                        :disabled="true"
                        ref="iraiNaiyo_fuguaiJokyo"
                      >
                      </el-input>
                  </el-form-item>
                  <el-form-item label="変更コメント" class="is-readonly">
                    <reafsinputtext
                      maxlength="50"
                      :title="iraiForm.変更コメント"
                      v-model="iraiForm.変更コメント"
                      :disabled="true"
                      :width="'100%'"
                    ></reafsinputtext>
                  </el-form-item>
                  <el-form-item label="依頼変更理由" class="is-readonly">
                    <reafsinputtext
                      maxlength="200"
                      :title="iraiForm.依頼内容変更理由"
                      v-model="iraiForm.依頼内容変更理由"
                      :disabled="true"
                      :width="'100%'"
                    ></reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="2"><br /><br /><br /><br /><br /><br /><br /><br /></el-col>
                <el-col :span="8" class="comment">
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <br />
                  <el-form-item label="添付一覧" label-width="325px">
                  </el-form-item>
                  <div style="padding-bottom: 20px">
                    <div class="commentContent">
                      <label class="item_label" :title="iraiForm.部位コメント1">1　　{{ iraiForm.部位コメント1 }}</label>
                    </div>
                    <div>
                      <label class="item_label" :title="iraiForm.部位コメント2">2　　{{ iraiForm.部位コメント2 }}</label>
                    </div>
                    <div>
                      <label class="item_label" :title="iraiForm.部位コメント3">3　　{{ iraiForm.部位コメント3 }}</label>
                    </div>
                    <div>
                      <label class="item_label" :title="iraiForm.部位コメント4">4　　{{ iraiForm.部位コメント4 }}</label>
                    </div>
                  </div>
                  <br />
                  <br />
                </el-col>
                <el-col :span="7">
                  <el-form-item label="中止保留区分" class="is-readonly">
                    <el-select
                      v-model="iraiForm.中止保留区分"
                      :disabled="true"
                      ref="ref中止保留区分"
                      placeholder=""
                    >
                      <!-- <el-option
                        v-for="item in 中止保留区分value"
                        :key="item.名称コード"
                        :label="item.名称"
                        :value="item.名称コード"
                      >
                      </el-option> -->
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="中止保留年月日" prop="中止保留年月日" class="is-readonly">
                    <reafsinputtext
                      maxlength="10"
                      v-model="iraiForm.中止保留年月日"
                      :disabled="true"
                      :width="'75%'"
                      ref="ref中止保留年月日"
                    ></reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <el-form-item label="中止保留事由" prop="中止保留事由" class="is-readonly">
                    <reafsinputtext
                      maxlength="200"
                      v-model="iraiForm.中止保留事由"
                      :disabled="true"
                      :width="'100%'"
                      :title="iraiForm.中止保留事由"
                      ref="ref中止保留事由"
                    >
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="見積" name="mitsu">
              <el-row>
                <el-col :span="5">
                  <el-form-item
                    label="見積作成日"
                    prop="mitsumoriSakuseiBi"
                    class="is-readonly"
                  >
                    <label class="el-form-item__label item_input_mitsumori_sakuseibi">
                      {{ mitsuForm.見積作成日 }}
                    </label>
                  </el-form-item>
                </el-col>
                <el-col :span="6" style="padding-left: 6px;">
                  <el-form-item label="見積№" prop="mitsumoriNo" class="is-readonly">
                    <div style="display: flex">
                      <reafsinputtext
                        v-model="mitsuForm.見積No"
                        maxlength="8"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                      -
                      <reafsinputtext
                        v-model="mitsuForm.見積No枝番"
                        maxlength="2"
                        disabled
                        :width="'50px'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item
                    label="見積有効期限"
                    prop="mitsumoriYukokigen"
                    class="is-readonly"
                  >
                    <label class="el-form-item__label item_input_mitsumori_yukokigen">
                      {{ mitsuForm.見積有効期限 }}
                    </label>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- ハノイ側修正2022/08/24　STEP2_W　課題管理表№55：設計書「修正履歴」シートの「2022/08/23仕様変更分」明細の各項目の横幅を変更可能としたい。  -->
              <reafstable
                ref="table"
                :tableData="mitsuForm.dt明細部"
                :maxHeight="375"
                :class="'dtMitsu'"
                :columns="columns"
                :cellStyleOption="cellStyleOption"
                :rowKeyFieldName="rowKey"
                :resizeAble="true"
              ></reafstable>
              <el-row style="padding-top: 10px;">
                <el-col :span="9">
                  <el-form-item class="is-readonly is-area" prop="見積条件"
                  style="margin-bottom: 0px;"
                  label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="item_label_mitsumoriJoken"
                        >見積<br />条件</label
                      >
                      <el-input
                        :title="mitsuForm.見積条件"
                        tabindex="4"
                        class="item_input_mitsumoriJoken"
                        maxlength="200"
                        type="textarea"
                        resize="none"
                        :rows="2"
                        v-model="mitsuForm.見積条件"
                        disabled
                        ref="mitsumoriJoken"
                      >
                      </el-input>
                    </div>
                  </el-form-item>
                </el-col>
                <reafstable
                  ref="tableMitsu税率別金額合計欄"
                  :tableData="mitsuForm.dt税率別金額合計欄"
                  :tableStyle="'width:770px;'"
                  :maxHeight="183"
                  :class="'dtMitsu'"
                  :columns="columnsZeiritsuBetsuKeiranMitsu"
                  :cellStyleOption="cellStyleOption税率別金額合計欄Mitsu"
                  :footer-data="footerData税率別金額合計欄Mitsu"
                  :rowKeyFieldName="rowKey税率別金額合計欄Mitsu"
                ></reafstable>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="概要書" name="gaiyoSho">
              <el-row>
                <el-col :span="13" style="width:54.6%">
                  <el-form-item
                    label="工事名称"
                    prop="工事名称"
                    class="is-readonly"
                  >
					<!-- ハノイ側修正2022/08/24　STEP2_W　課題管理表№56：設計書「修正履歴」シートの「2022/08/23仕様変更分」レイアウト修正依頼 -->
                    <reafsinputtext
                      :title="gaiyoShoForm.工事名称"
                      v-model="gaiyoShoForm.工事名称"
                      maxlength="50"
                      disabled
                      :width="'100%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item
                    label="稟議№"
                    prop="稟議No"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="gaiyoShoForm.稟議No"
                      v-model="gaiyoShoForm.稟議No"
                      maxlength="20"
                      disabled
                      :width="'135px'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="13" style="width: 54.6%">
                  <el-form-item id="申請内容" class="is-readonly is-area" label="申請内容" prop="申請内容"
                  >
                      <reafsinputtext
                        maxlength="200"
                        type="textarea"
                        resize="none"
                        :rows="3"
                        :title="gaiyoShoForm.申請内容"
                        v-model="gaiyoShoForm.申請内容"
                        disabled
                        ref="shinseiNaiyo"
                        :width="'100%'">
                      </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="予算額"
                    prop="予算額"
                    class="is-readonly noMarginBottom"
                  >
                      <reafsinputnumber
                        :title="gaiyoShoForm.予算額"
                        v-model="gaiyoShoForm.予算額"
                        disabled
                        :intLength="12"
                        :floatLength="0"
                        :width="'71%'"
                      >
                      </reafsinputnumber>
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="width:29.6%">
                  <el-form-item label="予算区分" prop="予算区分" class="label_Bottom is-readonly">
                    <el-select
                      tabindex="10"
                      :title="gaiyoShoForm.予算区分"
                      v-model="gaiyoShoForm.予算区分"
                      :disabled="true"
                      ref="ref予算区分"
                      style="width: 70%;"
                      placeholder=""
                    >
                    </el-select>
                  </el-form-item>
                </el-col>
                <!-- ハノイ側修正2022/08/24　STEP2_W　課題管理表№56：設計書「修正履歴」シートの「2022/08/23仕様変更分」レイアウト修正依頼 -->
                <el-col :span="10" style="width: 45%">
                  <el-form-item
                    label="計画№/件名"
                    prop="計画No_件名"
                    class="label_Bottom is-readonly"
                  >
                    <div style="display: flex; width: 100%;">
                      <reafsinputtext
                        :title="gaiyoShoForm.計画No"
                        v-model="gaiyoShoForm.計画No"
                        maxlength="12"
                        disabled
                        :width="'135px'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        :title="gaiyoShoForm.件名"
                        v-model="gaiyoShoForm.件名"
                        maxlength="20"
                        disabled
                        style="width: 100%"
                        :width="'100%'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="申請額（税抜）"
                    prop="申請額_税抜"
                    class="is-readonly noMarginBottom"
                  >
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.申請額_税抜"
                        v-model="gaiyoShoForm.申請額_税抜"
                        maxlength="12"
                        disabled
                        :width="'71%'">
                      </reafsinputtext> -->
                      <reafsinputnumber
                        :title="gaiyoShoForm.申請額_税抜"
                        v-model="gaiyoShoForm.申請額_税抜"
                        disabled
                        :intLength="12"
                        :floatLength="0"
                        :width="'71%'"
                      >
                      </reafsinputnumber>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item
                    label="積算期間"
                    prop="積算期間"
                    class="label_Bottom is-readonly"
                  >
                    <div style="display: flex">
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.積算期間"
                        v-model="gaiyoShoForm.積算期間"
                        maxlength="12"
                        disabled
                        :width="'100%'">
                      </reafsinputtext> -->
                      <label class="item_input_sekisan_kikan">
                      {{ gaiyoShoForm.積算期間 }}
                      </label>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <div style="display: flex; padding-top: 7px;">
                    <label style="padding-left: 2px;">年</label>
                    <label style="padding-left: 12px;">※ランニング費用計算時の年数</label>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="計画"
                    prop="計画"
                    class="is-readonly noMarginBottom"
                  >
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.計画"
                        v-model="gaiyoShoForm.計画"
                        maxlength="12"
                        disabled
                        :width="'71%'">
                      </reafsinputtext> -->
                      <reafsinputnumber
                        :title="gaiyoShoForm.計画"
                        v-model="gaiyoShoForm.計画"
                        disabled
                        :intLength="12"
                        :floatLength="0"
                        :width="'71%'"
                      >
                      </reafsinputnumber>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="計画外"
                    prop="計画外"
                    class="is-readonly noMarginBottom"
                  >
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.計画外"
                        v-model="gaiyoShoForm.計画外"
                        maxlength="12"
                        disabled
                        :width="'71%'">
                      </reafsinputtext> -->
                      <reafsinputnumber
                        :title="gaiyoShoForm.計画外"
                        v-model="gaiyoShoForm.計画外"
                        disabled
                        :intLength="12"
                        :floatLength="0"
                        :width="'71%'"
                      >
                      </reafsinputnumber>
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="width:29.6%">
                  <el-form-item
                    label="計画外区分"
                    prop="計画外区分"
                    class="label_Bottom is-readonly"
                  >
                    <reafsinputtext
                      :title="gaiyoShoForm.計画外区分"
                      v-model="gaiyoShoForm.計画外区分"
                      disabled
                      :width="'58%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item
                    label="ＣＣ受付№"
                    prop="ＣＣ受付No"
                    class="label_Bottom is-readonly"
                  >
                    <reafsinputtext
                      :title="gaiyoShoForm.ＣＣ受付No"
                      v-model="gaiyoShoForm.ＣＣ受付No"
                      disabled
                      :width="'92%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="計画外(賃料工事)"
                    prop="計画外_賃料工事"
                    class="is-readonly noMarginBottom"
                  >
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.計画外_賃料工事"
                        v-model="gaiyoShoForm.計画外_賃料工事"
                        maxlength="12"
                        disabled
                        :width="'71%'">
                      </reafsinputtext> -->
                      <reafsinputnumber
                        :title="gaiyoShoForm.計画外_賃料工事"
                        v-model="gaiyoShoForm.計画外_賃料工事"
                        disabled
                        :intLength="12"
                        :floatLength="0"
                        :width="'71%'"
                      >
                      </reafsinputnumber>
                  </el-form-item>
                </el-col>
                <div class="el-col-7" style="width: 29.6%">
                <el-col>
                  <el-form-item
                    label="追加賃料額"
                    prop="追加賃料額"
                    class="is-readonly noMarginBottom"
                  >
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.追加賃料額"
                        v-model="gaiyoShoForm.追加賃料額"
                        disabled
                        :width="'100%'">
                      </reafsinputtext> -->
                      <reafsinputnumber
                        :title="gaiyoShoForm.追加賃料額"
                        v-model="gaiyoShoForm.追加賃料額"
                        disabled
                        :intLength="12"
                        :floatLength="0"
                        style="width: 58%; display: inline-block;"
                        :width="'100%'"
                      >
                      </reafsinputnumber>
                      <label>／　月</label>
                  </el-form-item>
                </el-col>
                <!-- <el-col :span="2" style="padding-left: 2px; padding-top: 6px; width:8.8%">
                  <label>／　月</label>
                </el-col> -->
              </div>
                <el-col :span="5">
                  <el-form-item
                    label="賃料発生月"
                    prop="賃料発生月"
                    class="is-readonly noMarginBottom"
                  >
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.賃料発生月"
                        v-model="gaiyoShoForm.賃料発生月"
                        disabled
                        :width="'93%'">
                      </reafsinputtext> -->
                      <reafsinputtext
                      :title="gaiyoShoForm.賃料発生月"
                      v-model="gaiyoShoForm.賃料発生月"
                      disabled
                      :width="'92%'">
                      </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="固定資産除却"
                    prop="固定資産除却"
                    class="is-readonly noMarginBottom"
                  >
                      <!-- <reafsinputtext
                        :title="gaiyoShoForm.固定資産除却"
                        v-model="gaiyoShoForm.固定資産除却"
                        maxlength="12"
                        disabled
                        :width="'71%'">
                      </reafsinputtext> -->
                      <reafsinputnumber
                        :title="gaiyoShoForm.固定資産除却"
                        v-model="gaiyoShoForm.固定資産除却"
                        disabled
                        :intLength="12"
                        :floatLength="0"
                        :width="'71%'"
                      >
                      </reafsinputnumber>
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="width:29.6%">
                  <el-form-item
                    class="label_Bottom is-readonly is-area"
                    prop="固定資産No"
                    label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="item_label_koteiShisanNo"
                        ></label
                      >
                      <reafsinputtext
                        :title="gaiyoShoForm.固定資産No1"
                        v-model="gaiyoShoForm.固定資産No1"
                        disabled
                        :width="'97.5%'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label-width="0"
                  >
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="width:29.6%">
                  <el-form-item class="label_Bottom is-readonly is-area" prop="固定資産No2"
                  label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="item_label_koteiShisanNo"
                        >固定資産№</label
                      >
                      <reafsinputtext
                        :title="gaiyoShoForm.固定資産No2"
                        v-model="gaiyoShoForm.固定資産No2"
                        disabled
                        :width="'97.5%'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="予算差異・残額"
                    prop="予算差異_残額"
                    class="is-readonly"
                  >
                    <!-- <reafsinputtext
                      :title="gaiyoShoForm.予算差異_残額"
                      v-model="gaiyoShoForm.予算差異_残額"
                      maxlength="12"
                      disabled
                      :width="'70%'">
                    </reafsinputtext> -->
                    <reafsinputnumber
                      :title="gaiyoShoForm.予算差異_残額"
                      v-model="gaiyoShoForm.予算差異_残額"
                      disabled
                      :intLength="12"
                      :floatLength="0"
                      :width="'71%'"
                    >
                    </reafsinputnumber>
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="width:29.6%">
                  <el-form-item class="label_Bottom is-readonly is-area" prop="固定資産No3"
                  label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="item_label_koteiShisanNo"
                        ></label
                      >
                      <reafsinputtext
                        :title="gaiyoShoForm.固定資産No3"
                        v-model="gaiyoShoForm.固定資産No3"
                        disabled
                        :width="'97.5%'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="13" style="width:54.6%">
                  <el-form-item
                    label="取引先・支払先"
                    prop="取引先_支払先"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="gaiyoShoForm.取引先_支払先"
                      v-model="gaiyoShoForm.取引先_支払先"
                      disabled
                      :width="'91.3%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
                <!-- ハノイ側修正2022/08/24　STEP2_W　課題管理表№56：設計書「修正履歴」シートの「2022/08/23仕様変更分」レイアウト修正依頼 -->
                <el-col :span="7">
                  <el-form-item
                    label="支払条件"
                    prop="支払条件"
                    class="label_Bottom is-readonly"
                  >
                    <reafsinputtext
                      :title="gaiyoShoForm.支払条件"
                      v-model="gaiyoShoForm.支払条件"
                      disabled
                      :width="'100%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="13">
                  <el-form-item
                    label="工事期間"
                    prop="工事期間"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="gaiyoShoForm.工事期間"
                      v-model="gaiyoShoForm.工事期間"
                      disabled
                      :width="'92.1%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="13">
                  <el-form-item
                    label="申請者(担当者)"
                    prop="申請者_担当者"
                    class="is-readonly"
                  >
                    <div style="display: flex; width: 92.1%;">
                      <reafsinputtext
                        :title="gaiyoShoForm.申請者_担当者_組織名"
                        v-model="gaiyoShoForm.申請者_担当者_組織名"
                        disabled
                        style="width: 50%;"
                        :width="'100%'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        :title="gaiyoShoForm.申請者_担当者_役職名"
                        v-model="gaiyoShoForm.申請者_担当者_役職名"
                        disabled
                        style="width: 25%;"
                        :width="'100%'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        :title="gaiyoShoForm.申請者_担当者_名"
                        v-model="gaiyoShoForm.申請者_担当者_名"
                        disabled
                        style="width: 25%;"
                        :width="'100%'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="7">
                  <el-form-item
                    label="竣工年月"
                    prop="予算竣工年月日額"
                    class="is-readonly"
                  >
                    <!-- <reafsinputtext
                      :title="gaiyoShoForm.竣工年月日"
                      v-model="gaiyoShoForm.竣工年月日"
                      maxlength="12"
                      disabled
                      :width="'56%'">
                    </reafsinputtext> -->
                    <label class="item_input_jungong_nian_yeuri">
                      {{ gaiyoShoForm.竣工年月日 }}
                    </label>
                  </el-form-item>
                </el-col>
                <el-col :span="6" style="width:25.5%">
                  <el-form-item
                    label="築年数"
                    prop="築年数"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="gaiyoShoForm.築年数"
                      v-model="gaiyoShoForm.築年数"
                      disabled
                      :width="'76%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <el-form-item
                    label="残契約期間"
                    prop="残契約期間"
                    class="is-readonly"
                  >
                    <!-- <reafsinputtext
                      :title="gaiyoShoForm.残契約期間"
                      v-model="gaiyoShoForm.残契約期間"
                      disabled
                      :width="'54%'">
                    </reafsinputtext> -->
                    <label class="item_input_zan_keiyaku_kikan">
                      {{ gaiyoShoForm.残契約期間 }}
                    </label>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- <el-row>
                <el-col :span="18">
                  <el-form-item class="is-readonly is-area" prop="備考"
                  label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="item_label_biko"
                        >備考</label
                      >
                      <el-input
                        class="item_input_biko"
                        maxlength="200"
                        type="textarea"
                        resize="none"
                        :rows="3"
                        :title="gaiyoShoForm.備考"
                        v-model="gaiyoShoForm.備考"
                        disabled
                        ref="biko"
                      >
                      </el-input>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row> -->
              <el-row>
                <el-col :span="13" style="width: 54.6%">
                  <el-form-item id="備考" class="is-readonly is-area" label="備考" prop="備考"
                  >
                      <reafsinputtext
                        maxlength="200"
                        type="textarea"
                        resize="none"
                        :rows="3"
                        :title="gaiyoShoForm.備考"
                        v-model="gaiyoShoForm.備考"
                        disabled
                        ref="biko"
                        :width="'100%'">
                      </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="発注申請" name="hatchu">
              <el-row>
                <el-col :span="5" style="height: 32px">
                  <el-form-item
                    label="見積作成日"
                    prop="mitsumoriSakuseiBi"
                    class="is-readonly"
                  >
                    <label class="el-form-item__label item_input_mitsumori_sakuseibi">
                      {{ hatchuForm.見積作成日 }}
                    </label>
                  </el-form-item>
                </el-col>
                <el-col :span="6" style="padding-left: 6px; height: 32px">
                  <el-form-item label="見積№" prop="mitsumoriNo" class="is-readonly">
                    <div style="display: flex">
                      <reafsinputtext
                        v-model="hatchuForm.見積No"
                        maxlength="8"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                      -
                      <reafsinputtext
                        v-model="hatchuForm.見積No枝番"
                        maxlength="2"
                        disabled
                        :width="'50px'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="5" style="height: 32px">
                  <el-form-item
                    label="見積有効期限"
                    prop="mitsumoriYukokigen"
                    class="is-readonly"
                  >
                    <label class="el-form-item__label item_input_mitsumori_yukokigen">
                      {{ hatchuForm.見積有効期限 }}
                    </label>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <el-form-item
                    prop="依頼営業店稟議申請許可"
                    class="label_Bottom is-readonly"
                    label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="el-form-item__label irai_eigyoten_ringi_shinsei_kyoka"
                        >依頼営業店稟議申請許可</label
                      >
                      <!-- ハノイ側修正2022/08/18　STEP2_W　課題管理表№39：設計書「修正履歴」シートの「2022/08/16仕様変更分」依頼営業店稟議申請許可制御追記 -->
                      <el-radio-group
                       v-model="hatchuForm.依頼営業店稟議申請許可"
                       style="display:contents !important;"
                       :disabled="画面入力制御disabled.btn稟議申請"
                      >
                        <el-radio :label="0" style="margin-right: 25px; padding-left:5px; align-self: center;">許可しない</el-radio>
                        <el-radio :label="1" style="align-self: center;">許可する</el-radio>
                      </el-radio-group>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="8" style="height: 32px">
                  <el-form-item label="費用負担部門" prop="費用負担部門" class="is-required">
                    <div class="費用負担部門">
                      <opensearch
                        required
                        v-model="hatchuForm.費用負担部門コード"
                        :disabled="画面入力制御disabled.btn更新"
                        :appendlabelText="hatchuForm.費用負担部門名"
                        :append="'append'"
                        :maxlength="'8'"
                        :width="'110px'"
                        :customValidate= 2
                        :path="'reafsmodalSearchHiyoFutanBumonSabu'"
                        @backData="reafsmodalBackHatchuAnbunSettei"
                        ref="refHatchu費用負担部門">
                      </opensearch>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="8" style="height: 32px; margin-left:10px">
                  <el-form-item label="事業領域" prop="事業領域" class="is-required">
                    <el-select
                      tabindex="10"
                      required
                      v-model="hatchuForm.事業領域コード"
                      :disabled="画面入力制御disabled.btn更新"
                      @change="selectHatchuSho_JigyoRyoiki"
                      ref="refHatchu事業領域"
                      style="width: 100%;"
                    >
                      <el-option
                        v-for="item in 事業領域value"
                        :key="item.事業領域"
                        :label="item.dsp事業領域"
                        :value="item.事業領域">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="height: 32px; margin-left:10px">
                  <el-form-item label="外内販区分" prop="外内販区分" class="is-required">
                    <el-select
                      tabindex="10"
                      required
                      v-model="hatchuForm.外内販コード"
                      :disabled="画面入力制御disabled.btn更新"
                      @change="selectHatchuSho_SotoNai"
                      ref="refHatchu外内販区分"
                      style="width: 100%;"
                    >
                      <el-option
                        v-for="item in 外内販value"
                        :key="item.外内販"
                        :label="item.dsp外内販区分"
                        :value="item.外内販">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="稟議起案番号" prop="稟議起案番号">
                    <ringiKianBango
                      tabindex="7"
                      :intLength="15"
                      :floatLength="0"
                      v-model="hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号"
                      @blur="onBlurRingiKianBango"
                      width="90%"
                      ref="ref稟議起案番号"
                      :disabled="画面入力制御disabled.btn稟議申請"
                      >
                    </ringiKianBango>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item label="稟議可決番号" prop="稟議可決番号">
                    <ringiKianBango
                      tabindex="8"
                      :intLength="9"
                      :floatLength="0"
                      v-model="hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号"
                      @blur="onBlurRingiKaketsuBango"
                      width="90%"
                      ref="ref稟議可決番号"
                      :disabled="画面入力制御disabled.btn稟議申請"
                    >
                    </ringiKianBango>
                  </el-form-item>
                </el-col>
                <el-col :span="5">
                  <el-form-item label="費用按分" prop="費用按分" class="is-readonly">
                      <el-radio-group
                       v-model="hatchuForm.費用按分"
                       @change="changeHiyo_AnbunHatchu"
                      >
                        <el-radio :disabled="isHiyoAnbun" label="0" style="margin-right: 25px; padding-left:5px; align-self: center;">なし</el-radio>
                        <el-radio :disabled="isHiyoAnbun" label="1" style="align-self: center;">あり</el-radio>
                      </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="5" style="margin: 7px 0 0 0;">
                  <label style="color: red;">
                    <span>{{ hatchuForm.msg費用按分 }}</span>
                  </label>
                </el-col>
              </el-row>
              <!-- ハノイ側修正2022/08/24　STEP2_W　課題管理表№55：設計書「修正履歴」シートの「2022/08/23仕様変更分」明細の各項目の横幅を変更可能としたい。  -->
              <reafstable
                ref="table"
                :tableData="hatchuForm.dtHatchu"
                :maxHeight="285"
                :class="'dtHatchu'"
                :columns="columns"
                :cellStyleOption="cellStyleOption"
                :rowKeyFieldName="rowKey"
                :resizeAble="true"
              ></reafstable>
              <el-row style="padding-top: 10px; display: flex;">
                <el-col :span="9">
                  <el-form-item class="is-readonly is-area" prop="見積条件"
                  label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="item_label_mitsumoriJoken"
                        >見積<br />条件</label
                      >
                      <el-input
                        tabindex="9"
                        :title="hatchuForm.見積条件"
                        class="item_input_mitsumoriJoken"
                        maxlength="200"
                        type="textarea"
                        resize="none"
                        :rows="2"
                        v-model="hatchuForm.見積条件"
                        disabled
                        ref="mitsumoriJoken"
                      >
                      </el-input>
                    </div>
                  </el-form-item>
                </el-col>
                <reafstable
                  ref="tableHatchu税率別金額合計欄"
                  :tableData="hatchuForm.dt税率別金額合計欄"
                  :tableStyle="colWidthTblZeiritsuBetsuKingakuGokeiran"
                  :maxHeight="183"
                  :class="'dtHatchu'"
                  :columns="columnsZeiritsuBetsuKeiranHatchu"
                  :cellStyleOption="cellStyleOption税率別金額合計欄Hatchu"
                  :footer-data="footerData税率別金額合計欄Hatchu"
                  :rowKeyFieldName="rowKey税率別金額合計欄Hatchu"
                ></reafstable>
              </el-row>
              <el-row style="padding-top: 10px;">
                <el-col style="width: 27.5%;">
                  <el-form-item label="発注書・請書" prop="発注書_請書">
                    <el-select
                      tabindex="10"
                      v-model="hatchuForm.発注書_請書"
                      :disabled="画面入力制御disabled.発注書_請書"
                      @change="selectHatchuSho_Ukesho"
                      ref="ref発注書_請書"
                      style="width: 100%;"
                    >
                      <el-option
                        v-for="item in 発注書_請書value"
                        :key="item.取引先"
                        :label="item.取引先名"
                        :value="item.取引先"
                      >
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="3" style="margin-left: 10px">
                  <el-button
                    tabindex="11"
                    @click="onHatchuShoUkesho()"
                    :disabled="画面入力制御disabled.btn発注書請書">発注書・請書
                  </el-button>
                  <screenPopup src="/Reafs_W/HatchuShoUkesho" :params="paramsHatchuShoUkesho"
                    :dialogVisible.sync="showDialogHatchuShoUkesho"></screenPopup>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane label="作業結果／支払" name="sagyo">
              <el-row>
                <el-col :span="8">
                  <el-form-item prop="状態" label-width="0px">
                    <div style="display: flex">
                      <label
                        class="el-form-item__label item_label_col30"
                        >状態</label
                      >
                      <el-select
                        tabindex="11"
                        v-model="sagyoForm.状態"
                        :disabled="画面入力制御disabled.状態"
                        @change="selectJotai"
                        placeholder=""
                        ref="ref状態"
                        style="width: 255px;"
                      >
                        <el-option
                          v-for="item in 状態value"
                          :key="item.状態"
                          :label="item.状態"
                          :value="item.状態"
                        >
                        </el-option>
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="padding-left: 7px;">
                  <el-form-item
                    label="業者状態"
                    prop="業者状態"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="sagyoForm.業者状態"
                      v-model="sagyoForm.業者状態"
                      maxlength="10"
                      disabled
                      :width="'255px;'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="7" style="padding-left: 7px;">
                  <el-form-item
                    label="部門確認"
                    prop="部門確認"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="sagyoForm.部門確認"
                      v-model="sagyoForm.部門確認"
                      maxlength="10"
                      disabled
                      :width="'255px;'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item prop="取引先" label-width="0px">
                    <div style="display: flex">
                      <label
                        class="el-form-item__label item_label_col30"
                        >取引先</label
                      >
                      <el-select
                        tabindex="12"
                        v-model="sagyo取引先"
                        :disabled="画面入力制御disabled.取引先"
                        @change="selectTorihikisaki"
                        placeholder=""
                        ref="ref取引先"
                        style="width: 255px;"
                      >
                        <el-option
                          v-for="item in 取引先value"
                          :key="item.取引先"
                          :label="item.取引先名"
                          :value="item.取引先"
                        >
                        </el-option>
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="5" style="padding-left: 7px;">
                  <el-form-item prop="検収回数" class="label_Bottom" label-width="0px">
                    <div style="display: flex">
                      <label
                        class="el-form-item__label item_label_col30"
                        >検収回数</label
                      >
                      <el-select
                        tabindex="13"
                        v-model="sagyo検収回数"
                        :disabled="画面入力制御disabled.検収回数"
                        @change="selectKenshuKaisu"
                        placeholder=""
                        ref="ref検収回数"
                        style="width: 70px;"
                      >
                        <el-option
                          v-for="item in 検収回数value"
                          :key="item.サブNo_回数"
                          :label="item.サブNo_回数"
                          :value="item.サブNo_回数"
                        >
                        </el-option>
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="15">
                  <el-form-item
                    label="工事名称"
                    prop="工事名称"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="sagyoForm.工事名称"
                      v-model="sagyoForm.工事名称"
                      maxlength="50"
                      disabled
                      :width="'100%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item
                    label="作業担当者"
                    prop="作業担当者"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="sagyoForm.作業担当者"
                      v-model="sagyoForm.作業担当者"
                      maxlength="40"
                      disabled
                      :width="'93%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="担当者連絡先"
                    prop="担当者連絡先"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="sagyoForm.担当者連絡先"
                      v-model="sagyoForm.担当者連絡先"
                      maxlength="14"
                      disabled
                      :width="'100%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="11">
                  <el-form-item
                    label="作業完了日・時間"
                    prop="作業完了日"
                    class="is-readonly date"
                  >
                    <div style="display: flex">
                      <reafsinputtext
                        v-model="sagyoForm.作業完了日_FROM"
                        maxlength="10"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        v-model="sagyoForm.作業完了時間_FROM"
                        maxlength="5"
                        disabled
                        :width="'85px'">
                      </reafsinputtext>
                      &nbsp;～&nbsp;
                      <reafsinputtext
                        v-model="sagyoForm.作業完了日_TO"
                        maxlength="10"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                      <reafsinputtext
                        v-model="sagyoForm.作業完了時間_TO"
                        maxlength="5"
                        disabled
                        :width="'85px'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="18">
                  <el-form-item class="is-readonly is-area" prop="施工内容_特記事項等"
                  label-width="0px"
                  >
                    <div style="display: flex">
                      <label
                        class="item_label_sekoNaiyo_Tokukijikoto"
                        >施工内容／<br />特記事項等</label
                      >
                      <el-input
                        class="item_input_sekoNaiyo_Tokukijikoto"
                        maxlength="200"
                        type="textarea"
                        resize="none"
                        :rows="3"
                        :title="sagyoForm.施工内容_特記事項等"
                        v-model="sagyoForm.施工内容_特記事項等"
                        disabled
                        ref="sekoNaiyo_Tokukijikoto"
                      >
                      </el-input>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row style="display: flex">
                <!-- ハノイ側修正2022/08/31　STEP2_W　課題管理表№68：設計書「修正履歴」シートの「2022/08/29仕様変更分」「検収回数に戻す、費用負担部門・事業領域・外内販項目の条件修正、添付参照ボタン追加、添付参照ボタン→帳票一覧ボタンに変更 確認登録ボタン条件修正」 -->
                <el-col v-if="!isShiharai" :span="8">
                  <el-form-item label="費用負担部門" prop="費用負担部門" class="is-readonly">
                    <div style="display: flex">
                      <reafsinputtext
                        maxlength="8"
                        v-model="sagyoForm.費用負担部門コード"
                        :disabled="!isBumonKakunin"
                        @blur="reafsmodalBack_searchHiyoFutanBumonSagyoForm"
                        :width="'100px'"
                        disabled="disabled"
                        ref="ref費用負担部門"
                      >
                      </reafsinputtext>
                      <reafsinputtext
                        :title="sagyoForm.費用負担部門名"
                        v-model="sagyoForm.費用負担部門名"
                        :disabled="!isBumonKakunin"
                        :width="'95%'"
                        class="is-readonly none-border-left"
                        disabled="disabled"
                      >
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col v-else style="width: 34.1%;">
                  <el-form-item label="費用負担部門" prop="費用負担部門" style="margin-bottom: 0px;">
                    <div class="費用負担部門">
                      <opensearch
                        required
                        v-model="sagyoForm.費用負担部門コード"
                        :disabled="!isBumonKakunin"
                        :appendlabelText="sagyoForm.費用負担部門名"
                        :append="'append'"
                        :maxlength="'8'"
                        :width="'110px'"
                        :customValidate= 2
                        :path="'reafsmodalSearchHiyoFutanBumonSabu'"
                        @backData="reafsmodalBackSagyoAnbunSettei"
                        ref="refSagyo費用負担部門">
                      </opensearch>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item v-if="!isShiharai" label="事業領域" prop="事業領域" class="is-readonly">
                    <div style="display: flex">
                      <reafsinputtext
                        maxlength="8"
                        v-model="sagyoForm.事業領域コード"
                        :disabled="!isBumonKakunin"
                        :width="'100px'"
                        disabled="disabled"
                        ref="ref事業領域"
                      >
                      </reafsinputtext>
                      <reafsinputtext
                        :title="sagyoForm.事業領域名"
                        v-model="sagyoForm.事業領域名"
                        :disabled="!isBumonKakunin"
                        :width="'95%'"
                        class="is-readonly none-border-left"
                        disabled="disabled"
                      >
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                  <el-form-item v-else label="事業領域" prop="事業領域" class="is-required" style="margin-bottom: 0px;">
                    <el-select
                      tabindex="10"
                      required
                      v-model="sagyoForm.事業領域"
                      :disabled="!isBumonKakunin"
                      @change="selectSagyo_JigyoRyoiki"
                      ref="refSagyo事業領域"
                      style="width: 98%;"
                    >
                      <el-option
                        v-for="item in 事業領域value"
                        :key="item.事業領域"
                        :label="item.dsp事業領域"
                        :value="item.事業領域"
                      >
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="7">
                  <el-form-item v-if="!isShiharai" label="外内販区分" prop="外内販区分" class="is-readonly">
                    <div style="display: flex">
                      <reafsinputtext
                        maxlength="5"
                        v-model="sagyoForm.外内販コード"
                        :disabled="!isBumonKakunin"
                        :width="'90px'"
                        disabled="disabled"
                        ref="ref外内販区分"
                      >
                      </reafsinputtext>
                      <reafsinputtext
                        :title="sagyoForm.外内販名"
                        v-model="sagyoForm.外内販名"
                        :disabled="!isBumonKakunin"
                        :width="'70%'"
                        class="is-readonly none-border-left"
                        disabled="disabled"
                      >
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                  <el-form-item v-else label="外内販区分" prop="外内販区分" class="is-required" style="margin-bottom: 0px;">
                    <el-select
                      tabindex="10"
                      required
                      v-model="sagyoForm.外内販"
                      :disabled="!isBumonKakunin"
                      @change="selectSagyo_SotoNai"
                      ref="refSagyo外内販"
                      style="width: 100%;"
                    >
                      <el-option
                        v-for="item in 外内販value"
                        :key="item.外内販"
                        :label="item.dsp外内販区分"
                        :value="item.外内販"
                      >
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="5">
                  <el-form-item label="費用按分" prop="費用按分" class="is-readonly">
                      <el-radio-group
                       v-model="sagyoForm.費用按分"
                       @change="changeHiyo_AnbunSagyo"
                      >
                        <el-radio :disabled="!(isBumonKakunin && ((sagyoForm.状態 == '支払承認待ち' && sagyoForm.支払承認可能チェックCheck) || sagyoForm.状態 != '支払承認待ち'))" label="0" style="margin-right: 25px; padding-left:5px; align-self: center;">なし</el-radio>
                        <el-radio :disabled="!(isBumonKakunin && ((sagyoForm.状態 == '支払承認待ち' && sagyoForm.支払承認可能チェックCheck) || sagyoForm.状態 != '支払承認待ち'))" label="1" style="align-self: center;">あり</el-radio>
                      </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="5" style="margin: 7px 0 0 0;">
                  <label style="color: red;">
                    <span>{{ sagyoForm.msg費用按分 }}</span>
                  </label>
                </el-col>
              </el-row>
              <reafstable
                ref="tableSagyo税率別金額合計欄"
                :tableData="sagyoForm.dt税率別金額合計欄"
                :tableStyle="tableSagyo税率別金額合計欄Style"
                :maxHeight="202"
                :class="'dtSagyo'"
                :columns="columnsZeiritsuBetsuKeiranSagyo"
                :cellStyleOption="cellStyleOption税率別金額合計欄Sagyo"
                :footer-data="footerData税率別金額合計欄Sagyo"
                :rowKeyFieldName="rowKey税率別金額合計欄Sagyo"
              ></reafstable>
              <el-row style="padding-top: 10px;">
                <el-col :span="8">
                  <label
                    class="el-form-item__label item_label_Title"
                    >● {{sagyoForm.負担区分名称}}</label
                  >
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="5">
                  <el-form-item label="完了確認日" prop="完了確認日" class="is-readonly date">
                    <reafsinputtext
                      v-model="sagyoForm.完了確認日"
                      maxlength="10"
                      disabled
                      :width="'80%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="13">
                  <el-form-item
                    label="完了確認者"
                    prop="完了確認者"
                    class="is-readonly"
                  >
                    <reafsinputtext
                      :title="sagyoForm.完了確認者"
                      v-model="sagyoForm.完了確認者"
                      maxlength="100"
                      disabled
                      :width="'100%'">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="1.5">
                  <el-form-item
                    label="書類不備"
                    prop="書類不備"
                    class="label_Bottom is-readonly"
                  >
                  </el-form-item>
                </el-col>
                <el-col :span="3" style="padding-left: 50px;">
                  <el-form-item
                    label="解消日"
                    prop="解消日"
                    class="label_Bottom is-readonly"
                    style="margin-left: 1px;"
                  >
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="見積書"
                    prop="見積書"
                    class="label_Bottom is-readonly date"
                  >
                    <div style="display: flex">
                      <reafsinputtext
                        v-model="sagyoForm.見積書_不備有無欄"
                        maxlength="1"
                        disabled
                        :width="'51px'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        v-model="sagyoForm.見積書_解消日"
                        maxlength="10"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="稟議書"
                    prop="稟議書"
                    class="label_Bottom is-readonly date"
                  >
                    <div style="display: flex">
                      <reafsinputtext
                        v-model="sagyoForm.稟議書_不備有無欄"
                        maxlength="1"
                        disabled
                        :width="'51px'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        v-model="sagyoForm.稟議書_解消日"
                        maxlength="10"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col v-if="isTenpuSansho" :span="3" style="margin-top:-31px">
                  <el-button v-show="endableBtnKakuninToroku"
                    tabindex="16"
                    @click="onTenpuSansho"
                    style="width: 100px; padding-top: 8px; padding-bottom: 8px;">添付<br/><br/>参照</el-button>
                    <screenPopup
                    src="/Reafs_W/IraiTenpuTouroku"
                    :params="paramsIraiTenpuTouroku"
                    :dialogVisible.sync="showDialogIraiTenpuTouroku"
                    @closePopupEvent="IraiTenpuTourokuClose"></screenPopup>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="完了報告"
                    prop="完了報告"
                    class="label_Bottom is-readonly date"
                  >
                    <div style="display: flex">
                      <reafsinputtext
                        v-model="sagyoForm.完了報告_不備有無欄"
                        maxlength="1"
                        disabled
                        :width="'51px'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        v-model="sagyoForm.完了報告_解消日"
                        maxlength="10"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="請求書"
                    prop="請求書"
                    class="label_Bottom is-readonly date"
                  >
                    <div style="display: flex">
                      <reafsinputtext
                        v-model="sagyoForm.請求書_不備有無欄"
                        maxlength="1"
                        disabled
                        :width="'51px'">
                      </reafsinputtext>
                      <reafsinputtext
                        class="none-border-left"
                        v-model="sagyoForm.請求書_解消日"
                        maxlength="10"
                        disabled
                        :width="'120px'">
                      </reafsinputtext>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col v-if="isShiharai" :span="3" style="margin-top:-10px">
                  <el-button
                    :disabled="!(isBumonKakunin && sagyoForm.支払承認可能チェックCheck)"
                    tabindex="13"
                    @click="onBumonKakunin">部門確認</el-button>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="チェック1"
                    prop="チェック1"
                    class="label_Bottom is-readonly"
                  >
                    <div style="display: flex">
                      <reafsinputtext
                        maxlength="1"
                        disabled
                        :width="'51px'">
                      </reafsinputtext>
                      <div :class="{ disabled_checkbox: !endableBtnKakuninToroku }" class="checkbox">
                        <el-checkbox
                          tabindex="14"
                          v-model="sagyoForm.チェック1"
                          :disabled="画面入力制御disabled.チェック1 || !isShiharai"
                          >
                        </el-checkbox>
                      </div>
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="6">
                  <el-form-item
                    label="チェック2"
                    prop="チェック2"
                    class="label_Bottom is-readonly"
                  >
                    <div style="display: flex">
                      <reafsinputtext
                        maxlength="1"
                        disabled
                        :width="'51px'">
                      </reafsinputtext>
                      <div :class="{ disabled_checkbox: !endableBtnKakuninToroku }" class="checkbox">
                        <el-checkbox
                          tabindex="15"
                          v-model="sagyoForm.チェック2"
                          :disabled="画面入力制御disabled.チェック2 || !isShiharai"
                          >
                        </el-checkbox>
                      </div>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col v-if="isTenpuSansho" :span="3" style="margin-top:-8px">
                  <el-button v-show="endableBtnKakuninToroku"
                    tabindex="16"
                    @click="onKakuninToroku">ﾁｪｯｸ登録</el-button>
                </el-col>
              </el-row>
            </el-tab-pane>
          </el-tabs>
        </el-main>
        <el-footer class="home-el-footer border-top" height="auto">
          <el-row>
            <el-col :span="2">
              <el-button
                tabindex="30"
                @click="onSeeAttachment"
                :disabled="画面入力制御disabled.btn帳票一覧">帳票一覧</el-button>
                <screenPopup
                  src="/Reafs_W/TyohyoIchiran"
                  :params="paramsTyohyoIchiran"
                  :dialogVisible.sync="showDialogTyohyoIchiran"></screenPopup>
            </el-col>
            <el-col v-if="is発注申請Tab" style="width: 6.2%;">
              <el-button
                tabindex="31"
                @click="onUpdate"
                :disabled="画面入力制御disabled.btn更新">更新</el-button>
            </el-col>
            <el-col :span="2">
              <el-button
                tabindex="31"
                @click="onApprovalApplication"
                :disabled="画面入力制御disabled.btn稟議申請">稟議申請</el-button>
            </el-col>
            <el-col :span="2">
              <el-button
                tabindex="32"
                @click="onWithdrawal"
                :disabled="画面入力制御disabled.btn取下">取下</el-button>
            </el-col>
            <el-col :span="9">
              <el-form-item
                :class="画面入力制御disabled.否認_差戻事由 === true ? 'is-readonly is-area' : 'is-area'"
                prop="否認_差戻事由"
                label-width="0px">
                <div style="display: flex">
                  <label
                    class="el-form-item__label item_label_hinin_ashimodoshiJiyu"
                    >否認・差戻事由</label
                  >
                  <el-input
                    tabindex="33"
                    class="item_input_hinin_ashimodoshiJiyu"
                    maxlength="200"
                    type="textarea"
                    resize="none"
                    :rows="2"
                    :disabled="画面入力制御disabled.否認_差戻事由"
                    v-model="否認_差戻事由"
                    :title="否認_差戻事由"
                    ref="ref否認_差戻事由"
                  >
                  </el-input>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="2" style="padding-left: 15px; width: 7.5%;">
              <el-button
                tabindex="34"
                @click="onRemand"
                :disabled="画面入力制御disabled.btn差戻">差戻</el-button>
            </el-col>
            <el-col :span="2" style="padding-left: 15px; width: 7.5%;">
              <el-button
                tabindex="35"
                @click="onDenial"
                :disabled="画面入力制御disabled.btn否認">否認</el-button>
            </el-col>
            <el-col :span="2" style="padding-left: 15px; width: 7.5%;">
              <el-button
                tabindex="36"
                @click="onApproval"
                :disabled="画面入力制御disabled.btn承認">承認</el-button>
            </el-col>
            <el-col :span="2">
              <el-button tabindex="37" @click="onBack" style="float: right">戻る</el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
    <modalHiyoAnbunShitei
      :dialogVisible.sync="showHiyoAnbunShitei"
      :HiyoAnbunShitei_key.sync="KeyDataToHiyoAnbunShitei"
      @closePopupEvent="fnc_HiyoAnbunShiteiClose"
      @UpdateSuccess="fnc_HiyoAnbunShiteiUpdateSuccess"
    ></modalHiyoAnbunShitei>
  </div>
</template>

<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import ringiKianBango from '@/components/basic/RingiKianBango.vue'
import reafstable from '@/components/table/Grid.vue'
import screenPopup from '../../common/ScreenPopup/ScreenPopup.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import opensearch from '@/components/basic/openSearch.vue'
import reafsmodalSearchHiyoFutanBumonSabu from '@/views/Reafs-W/search/WS00520/Modal_SearchHiyoFutanBumonSabu.vue'
import modalHiyoAnbunShitei from '@/components/modal/HiyoAnbunShitei.vue'

var $select事業領域value
var $select外内販value
var $select発注書_請書value
var $select取引先value
var $select検収回数value
var $select状態value

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
    ringiKianBango,
    reafstable,
    screenPopup,
    reafsinputnumber,
    opensearch,
    reafsmodalSearchHiyoFutanBumonSabu,
    modalHiyoAnbunShitei
  },
  data () {
    return {
      isMounted: false,
      menuID: 'D',
      subMenuID: 'WD00520',
      menuOptions: {},
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: '',
        業者コード: '',
        ユーザー種別: '',
        親ＩＤ: ''
      },
      searchForm: {
        koujiiraiNo: '',
        koujiiraiNo1: ''
      },
      syuzenForm: {
        営業店コード: '',
        営業店コード枝番: '',
        営業店名: '',
        進捗_本体: '',
        進捗_発注: '',
        物件コード: '',
        物件名: '',
        物件区分: '',
        物件住所Cd: '',
        物件住所Nm: '',
        工事依頼No: '',
        工事依頼No枝番: '',
        修繕区分小: '',
        w稟議申請対象フラグ: '',
        sgw費用按分FLG: ''
      },
      iraiForm: {
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
        依頼者代理役職名: '',
        依頼者代理連絡先: '',
        依頼コメント: '',
        対象箇所: '',
        メーカー名: '',
        依頼内容_不具合状況: '',
        依頼状況: '',
        sgr担当部署: '',
        中止保留区分: '',
        中止保留年月日: '',
        中止保留事由: '',
        部位コメント: [],
        部位コメント1: '',
        部位コメント2: '',
        部位コメント3: '',
        部位コメント4: '',
        否認_差戻事由: '',
        // ハノイ側修正2022/10/03　STEP2_W　課題管理表№125：設計書「修正履歴」シートの「2022/09/26仕様変更分」「修繕区分大、修繕区分小、負担区分、変更コメント、依頼内容変更理由追加」Start
        変更コメント: '',
        依頼内容変更理由: '',
        修繕区分大: '',
        修繕区分小: '',
        負担区分: ''
        // ハノイ側修正2022/10/03　STEP2_W　課題管理表№125：設計書「修正履歴」シートの「2022/09/26仕様変更分」「修繕区分大、修繕区分小、負担区分、変更コメント、依頼内容変更理由追加」End
      },
      mitsuForm: {
        見積作成日: '',
        見積No: '',
        見積No枝番: '',
        見積有効期限: '',
        見積条件: '',
        金額_税抜_合計: '',
        消費税額_合計: '',
        金額_税補正_合計: '',
        金額_税込_合計: '',
        否認_差戻事由: '',
        修繕区分小: '',
        dt明細部: [],
        dt税率別金額合計欄: []
      },
      rowKey: 'rowKey',
      columns: [
        { field: 'No',
          key: '1',
          title: '№',
          width: 30,
          fixed: 'center'
          // renderBodyCell: ({ row, column, rowIndex }, h) => {
          //   return ++rowIndex
          // }
        },
        { field: '分類', key: '2', title: '分類', width: 20 },
        { field: '大中小区分',
          key: '3',
          title: '大中小区分',
          width: '18.5%',
          renderBodyCell: ({ row }) => {
            const text = row['大中小区分']
            return (
              <div title={text} style="display: block; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                {text}
              </div>
            )
          }
        },
        { field: '作業名称',
          key: '4',
          title: '作業名称',
          width: '20%',
          renderBodyCell: ({ row }) => {
            const text = row['作業名称']
            return (
              <div title={text} style="display: block; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                {text}
              </div>
            )
          }
        },
        { field: '取引先',
          key: '5',
          title: '取引先',
          width: '13%',
          renderBodyCell: ({ row }) => {
            const text = row['取引先']
            return (
              <div title={text} style="display: block; text-align: left; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;">
                {text}
              </div>
            )
          }
        },
        { field: '数量',
          key: '6',
          title: '数量',
          width: 65,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.数量}</span>
              </div>
            )
          }
        },
        { field: '単位',
          key: '7',
          title: '単位',
          width: 56,
          renderBodyCell: ({ row, column, rowIndex }) => {
            const text = row['単位']
            return (
              <div title={text} style="display: block; text-align: left;">
                <span >{row.単位}</span>
              </div>
            )
          }
        },
        { key: '8',
          title: '金額',
          children: [
            {
              key: '9',
              title: '単価',
              align: 'center',
              width: 87,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block; text-align: right;">
                    <span >
                    </span><span >
                      {row.単価}
                    </span></div>
                )
              }
            },
            {
              key: '10',
              title: '金額（税抜）',
              align: 'center',
              width: 87,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block; text-align: right;">
                    <span >
                    </span><span >
                      {row.金額_税抜}
                    </span></div>
                )
              }
            },
            {
              key: '11',
              title: '税区分',
              align: 'center',
              width: 40,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block; text-align: left;">
                    <span >
                    </span><span >
                      {row.税区分}
                    </span></div>
                )
              }
            },
            {
              key: '12',
              title: '税率',
              align: 'center',
              width: 30,
              renderBodyCell: ({ row, column, rowIndex }) => {
                return (
                  <div style="display: block">
                    <span >
                    </span><span >
                      {row.税率}
                    </span></div>
                )
              }
            }
          ]
        }
      ],
      cellStyleOption: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          return 'table-body-cell-class'
        }
      },
      rowKey税率別金額合計欄Mitsu: 'rowKey',
      columnsZeiritsuBetsuKeiranMitsu: [
        { field: '取引先名',
          key: '1',
          title: '取引先名',
          width: 120,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: left;">
                <span >{row.取引先名}</span>
              </div>
            )
          }
        },
        { field: '税率',
          key: '2',
          title: '税率',
          width: 32,
          fixed: 'center'
        },
        { field: '原価金額税抜',
          key: '3',
          title: '金額（税抜）',
          width: 65,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.率別売上金額税抜}</span>
              </div>
            )
          }
        },
        { field: '原価金額消費税',
          key: '4',
          title: '消費税額',
          width: 65,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.率別売上金額消費税}</span>
              </div>
            )
          }
        },
        { field: '税補正',
          key: '5',
          title: '税補正',
          width: 32,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.税補正}</span>
              </div>
            )
          }
        },
        { field: '原価金額税込',
          key: '6',
          title: '金額（税込）',
          width: 65,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.率別売上金額税込}</span>
              </div>
            )
          }
        }
      ],
      cellStyleOption税率別金額合計欄Mitsu: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          return 'table-body-cell-class'
        },
        footerCellClass: ({ row, column, rowIndex }) => {
          if (column.field === '取引先名') {
            return 'table-footer-cell-total'
          }
        }
      },
      gaiyoShoForm: {
        工事名称: '',
        稟議No: '',
        申請内容: '',
        予算額: '',
        申請額_税抜: '',
        計画: '',
        計画外: '',
        計画外_賃料工事: '',
        固定資産除却: '',
        予算差異_残額: '',
        予算区分: '',
        計画No: '',
        件名: '',
        積算期間: '',
        計画外区分: '',
        ＣＣ受付No: '',
        追加賃料額: '',
        賃料発生月: '',
        固定資産No1: '',
        固定資産No2: '',
        固定資産No3: '',
        取引先_支払先: '',
        支払条件: '',
        工事期間: '',
        申請者_担当者_組織名: '',
        申請者_担当者_役職名: '',
        申請者_担当者_名: '',
        竣工年月日: '',
        築年数: '',
        残契約期間: '',
        備考: '',
        否認_差戻事由: ''
      },
      hatchuForm: {
        負担区分: '',
        修繕区分小: '',
        見積作成日: '',
        見積No: '',
        見積No枝番: '',
        見積有効期限: '',
        見積条件: '',
        ｸﾞﾙｰﾌﾟ稟議起案番号: '',
        ｸﾞﾙｰﾌﾟ稟議可決番号: '',
        発注書_請書: '',
        否認_差戻事由: '',
        依頼営業店稟議申請許可: 1,
        費用負担部門コード: '',
        費用負担部門名: '',
        事業領域コード: '',
        事業領域名: '',
        外内販コード: '',
        外内販名: '',
        dtHatchu: [],
        費用按分: '0',
        金額_税抜_合計: '',
        消費税額_合計: '',
        金額_税補正_合計: '',
        金額_税込_合計: '',
        dt税率別金額合計欄: [],
        msg費用按分: '',
        w費用負担部門按分フラグ: 0,
        dt按分入力済メッセージ: [],
        isDisabledHiyoAnbun: false
      },
      rowKey税率別金額合計欄Hatchu: 'rowKey',
      columnsZeiritsuBetsuKeiranHatchu: [
        { field: '業者コード',
          key: '業者コード',
          title: '',
          width: 0,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: none;">
                <span >{row.業者コード}</span>
              </div>
            )
          }
        },
        { field: '業者コード枝番',
          key: '業者コード枝番',
          title: '',
          width: 0,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: none;">
                <span >{row.業者コード枝番}</span>
              </div>
            )
          }
        },
        { field: '取引先名',
          key: '1',
          title: '取引先名',
          width: 120,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: left;">
                <span >{row.取引先名}</span>
              </div>
            )
          }
        },
        { field: '税率',
          key: '2',
          title: '税率',
          width: 32,
          fixed: 'center'
        },
        { field: '原価金額税抜',
          key: '3',
          title: '金額（税抜）',
          width: 65,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.率別売上金額税抜}</span>
              </div>
            )
          }
        },
        { field: '原価金額消費税',
          key: '4',
          title: '消費税額',
          width: 65,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.率別売上金額消費税}</span>
              </div>
            )
          }
        },
        { field: '税補正',
          key: '5',
          title: '税補正',
          width: 32,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.税補正}</span>
              </div>
            )
          }
        },
        { field: '原価金額税込',
          key: '6',
          title: '金額（税込）',
          width: 65,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.率別売上金額税込}</span>
              </div>
            )
          }
        },
        { field: '',
          key: '7',
          title: '',
          width: 110,
          renderBodyCell: ({ row, column, rowIndex }) => {
            if (!row.率別売上金額税込) {
              return (<div style="height: 24px;"/>)
            } else {
              return (
                <div style="display: block; text-align: left;">
                  <el-button
                    style= "height: 24px; padding-top: 3px;"
                    disabled={!(this.isDisplayHatchuAnbunNyuryoku)}
                    on-click={() => {
                      this.onAnbunNyuryoku(row, rowIndex, 'hatchu')
                    }}
                    size="medium"
                    id={'refHatchuAnbunNyuryoku' + rowIndex}
                  >
                    按分入力
                  </el-button>
                  <span style={'margin-left: 5px;' + (row.按分入力済メッセージ === '未入力' ? ' color: red;' : ' font-weight: bold;')}>{ row.按分入力済メッセージ }</span>
                </div>
              )
            }
          }
        }
      ],
      cellStyleOption税率別金額合計欄Hatchu: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          return 'table-body-cell-class'
        },
        footerCellClass: ({ row, column, rowIndex }) => {
          if (column.field === '取引先名') {
            return 'table-footer-cell-total'
          }
        }
      },
      事業領域value: [],
      外内販value: [],
      発注書_請書value: [],
      paramsHatchuShoUkesho: {
        工事依頼No: '',
        工事依頼No枝番: '',
        取引先コード: '',
        取引先コード枝番: '',
        取引先名: ''
      },
      showDialogHatchuShoUkesho: false,
      取引先コード: '',
      取引先コード枝番: '',
      取引先名: '',
      状態value: [],
      取引先value: [],
      検収回数value: [],
      sagyoForm: {
        状態: '',
        業者状態: '',
        部門確認: '',
        工事名称: '',
        作業担当者: '',
        担当者連絡先: '',
        作業完了日_FROM: '',
        作業完了時間_FROM: '',
        作業完了日_TO: '',
        作業完了時間_TO: '',
        施工内容_特記事項等: '',
        費用負担部門コード: '',
        費用負担部門名: '',
        事業領域: '',
        事業領域コード: '',
        事業領域名: '',
        外内販: '',
        外内販コード: '',
        外内販名: '',
        税込支払金額: '',
        負担区分名称: '',
        完了確認日: '',
        完了確認者: '',
        見積書_不備有無欄: '',
        見積書_解消日: '',
        稟議書_不備有無欄: '',
        稟議書_解消日: '',
        完了報告_不備有無欄: '',
        完了報告_解消日: '',
        請求書_不備有無欄: '',
        請求書_解消日: '',
        チェック1: false,
        チェック2: false,
        作業結果No: '',
        w作業結果確認フラグ: '',
        sagyo取引先: '',
        sagyo検収回数: '',
        否認_差戻事由: '',
        種類: false,
        費用按分: '0',
        按分設定: '',
        税込発注金額_税込支払金額_合計: '',
        dt税率別金額合計欄: [],
        負担区分: '',
        発注サブNo: '',
        msg費用按分: '',
        w費用負担部門按分フラグ: 0,
        dt按分入力済メッセージ: [],
        no32Data: {},
        発注サブNo_F052: '',
        支払承認可能チェックCheck: false// ハノイ側修正2023/2/23　STEP2_W　課題管理表№297：設計書「修正履歴」シートの「2023/2/22仕様変更分」「作業結果／支払タブ内の費用按分、按分入力ボタン、部門確認ボタン押下条件に支払承認可能チェックを追加」
      },
      rowKey税率別金額合計欄Sagyo: 'rowKey',
      tableSagyo税率別金額合計欄Style: '',
      columnsZeiritsuBetsuKeiranSagyoOrg: [
        { field: '税率',
          key: '1',
          title: '税率',
          width: 32,
          fixed: 'center'
        },
        { field: '税込発注金額_税込支払金額',
          key: '2',
          title: '税込発注金額',
          width: 70,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: block; text-align: right;">
                <span >{row.税込発注金額_税込支払金額}</span>
              </div>
            )
          }
        },
        { field: '支払予定日',
          key: '3',
          title: '支払予定日',
          width: 56,
          fixed: 'center'
        },
        { field: '',
          key: '4',
          title: '',
          width: 105,
          renderBodyCell: ({ row, column, rowIndex }) => {
            if (!row.税込発注金額_税込支払金額) {
              return (<div style="height: 24px;"/>)
            } else {
              return (
                <div style="display: block; text-align: left;">
                  <el-button
                    style= "height: 24px; padding-top: 3px;"
                    disabled={!(this.isDisplaySagyoAnbunNyuryoku &&
                      (this.sagyoForm.状態 !== '工事完了確認待ち' && this.sagyoForm.状態 !== '工事完了差戻' && this.sagyoForm.状態 !== '工事完了確認済') &&
                      ((this.sagyoForm.状態 == '支払承認待ち' && this.sagyoForm.支払承認可能チェックCheck) || this.sagyoForm.状態 != '支払承認待ち'))}
                    on-click={() => {
                      this.onAnbunNyuryoku(row, rowIndex, 'sagyo')
                    }}
                    size="medium"
                    id={'refSagyoAnbunNyuryoku' + rowIndex}
                  >
                      按分入力
                  </el-button>
                  <span style={'margin-left: 5px;' + (row.按分入力済メッセージ === '未入力' ? ' color: red;' : ' font-weight: bold;')}>{ row.按分入力済メッセージ }</span>
                </div>
              )
            }
          }
        }
      ],
      cellStyleOption税率別金額合計欄Sagyo: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          return 'table-body-cell-class'
        },
        footerCellClass: ({ row, column, rowIndex }) => {
          if (column.field === '税率') {
            return 'table-footer-cell-totalSagyoTab'
          }
        }
      },
      sagyo取引先: '',
      sagyo検収回数: '',
      sagyo発注サブNo: '',
      dispBtnExistF005: true,
      endableBtnKakuninToroku: true,
      dispBtnBy請求合意区分F005: true,
      否認_差戻事由: '',
      画面入力制御disabled: {
        btn発注書請書: true,
        btn帳票一覧: true,
        btn稟議申請: true,
        btn取下: true,
        btn差戻: true,
        btn否認: true,
        btn承認: true,
        状態: false,
        取引先: false,
        検収回数: false,
        否認_差戻事由: false,
        チェック1: false,
        チェック2: false,
        費用負担部門: false,
        事業領域: false,
        外内販区分: false,
        btn更新: true,
        発注書_請書: false
      },
      iraiFormInit: {},
      mitsuFormInit: {},
      gaiyoShoFormInit: {},
      hatchuFormInit: {},
      hatchuFormSearched: {},
      sagyoFormInit: {},
      sagyoFormSearched: {},
      activeName: 'irai',
      doActiveName: '',
      oldSelectedTab: 'irai',
      showDialogTyohyoIchiran: false,
      paramsTyohyoIchiran: {
        工事依頼No: ''
      },
      f001_version: '',
      fw042_version: '',
      queryParams: {
        工事依頼No: '',
        発注サブNo: '',
        検収回数: '',
        tabFocus: '',
        承認種類: ''
      },
      sessionPageDataBack: null,
      fw042_最終承認フラグ: '',
      税率別金額合計欄_取引先名: '取引先名',
      税率別金額合計欄_税率: '税率',
      税率別金額合計欄_金額_税抜: '金額（税抜）',
      税率別金額合計欄_消費税額: '消費税額',
      税率別金額合計欄_税補正: '税補正',
      税率別金額合計欄_金額_税込: '金額（税込）',
      税率別金額合計欄_合計: '合計',
      is発注申請Tab: false,
      作業結果_支払_税率: '税率',
      paramsIraiTenpuTouroku: {
        添付種類: '901',
        工事依頼No: ''
      },
      showDialogIraiTenpuTouroku: false,
      showDialogHiyoFutanBumonSabu_Sagyo: false,
      名称取得方法17: {},
      kengen: false,
      showHiyoAnbunShitei: false,
      KeyDataToHiyoAnbunShitei: {
        会社コード: '',
        工事依頼No: '',
        工事依頼No枝番: '',
        取引先コード: '',
        取引先名: '',
        業者コード: '',
        業者コード枝番: '',
        税率: '',
        率別売上金額税抜: '',
        率別売上金額消費税: '',
        率別売上金額税込: '',
        費用負担部門コード: '',
        検収回数: 0,
        入力区分: '',
        発注サブNO: 0,
        費用按分: '',
        未入力mode: '',
        tabmode: ''
      },
      isDisplayHatchuAnbunNyuryoku: false,
      isDisplaySagyoAnbunNyuryoku: false
    }
  },
  computed: {
    searchData () {
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1
      }
    },
    isKojiIraiInput () {
      return (this.searchForm.koujiiraiNo.trim() + this.searchForm.koujiiraiNo1.trim()) !== ''
    },
    isUpdate () {
      return (this.searchForm.koujiiraiNo.trim() && this.searchForm.koujiiraiNo1.trim())
    },
    isTenpuSansho () {
      return ((this.sagyoForm.状態 !== '工事完了確認待ち' && this.sagyoForm.状態 !== '工事完了確認済' && this.sagyoForm.状態 !== '工事完了差戻') && this.dispBtnExistF005)
    },
    isShiharai () {
      return (this.sagyoForm.状態 !== '工事完了確認待ち' && this.sagyoForm.状態 !== '工事完了確認済' && this.sagyoForm.状態 !== '工事完了差戻')
    },
    isBumonKakunin () {
      return ((this.sagyoForm.状態 !== '工事完了確認待ち' && this.sagyoForm.状態 !== '工事完了差戻' && this.sagyoForm.状態 !== '工事完了確認済') && this.sagyoForm.部門確認 === '部門確認待ち')
    },
    isHiyoAnbun () {
      return (this.画面入力制御disabled.btn更新 || this.hatchuForm.isDisabledHiyoAnbun)
    },
    columnsZeiritsuBetsuKeiranSagyo () {
      if (!this.columnsZeiritsuBetsuKeiranSagyoOrg) {
        return []
      }
      // ハノイ側修正2022/1/11　STEP2_W　課題管理表№268：設計書「修正履歴」シートの「2022/1/6仕様変更分」「負担区分≠2の場合、税率・金額は非表示」 START
      const cols = this.columnsZeiritsuBetsuKeiranSagyoOrg
        .filter(x => this.sagyoForm.負担区分 != '2' ? x.field != '税率' && x.field != '税込発注金額_税込支払金額' : true)
      const tblFullWidth = 510 // TODO
      if (cols.find(x => x.field == '税率')) {
        this.tableSagyo税率別金額合計欄Style = `width:${tblFullWidth}px;`
      } else {
        this.tableSagyo税率別金額合計欄Style = `width:${tblFullWidth - (70 + 142)}px;`
      }
      // ハノイ側修正2022/1/11　STEP2_W　課題管理表№268：設計書「修正履歴」シートの「2022/1/6仕様変更分」「負担区分≠2の場合、税率・金額は非表示」 END
      const col2 = cols.find(x => x.field == '税込発注金額_税込支払金額')
      if (col2) {
        if (this.dispBtnExistF005) {
          col2.title = '税込支払金額'
        } else {
          col2.title = '税込発注金額'
        }
      }
      return cols
    },
    colWidthTblZeiritsuBetsuKingakuGokeiran () {
      if (!this.hatchuForm.dt税率別金額合計欄) {
        return 'width:920px;'
      }
      if (this.hatchuForm.dt税率別金額合計欄.length <= 4) {
        return 'width:910px;'
      } else {
        return 'width:920px;'
      }
    },
    footerData税率別金額合計欄Mitsu () {
      return [
        {
          rowKey: 0,
          取引先名: '合計',
          税率: '',
          原価金額税抜: this.mitsuForm.金額_税抜_合計,
          原価金額消費税: this.mitsuForm.消費税額_合計,
          税補正: this.mitsuForm.金額_税補正_合計,
          原価金額税込: this.mitsuForm.金額_税込_合計
        }
      ]
    },
    footerData税率別金額合計欄Hatchu () {
      return [
        {
          rowKey: 0,
          取引先名: '合計',
          税率: '',
          原価金額税抜: this.hatchuForm.金額_税抜_合計,
          原価金額消費税: this.hatchuForm.消費税額_合計,
          税補正: this.hatchuForm.金額_税補正_合計,
          原価金額税込: this.hatchuForm.金額_税込_合計
        }
      ]
    },
    footerData税率別金額合計欄Sagyo () {
      if (this.columnsZeiritsuBetsuKeiranSagyo && this.columnsZeiritsuBetsuKeiranSagyo.find(x => x.field == '税率')) {
        return [
          {
            rowKey: 0,
            税率: '合計',
            税込発注金額_税込支払金額: this.sagyoForm.税込発注金額_税込支払金額_合計,
            支払予定日: ''
          }
        ]
      } else {
        return []
      }
    }
  },
  inject: ['setPageTitle'],
  created () {
    this.init()

    $select事業領域value = this
    $select外内販value = this
    $select発注書_請書value = this
    $select状態value = this
    $select取引先value = this
    $select検収回数value = this
  },
  mounted () {
    this.isMounted = true
    this.initOnMounted()
  },
  methods: {
    initOnMounted () {
      // ハノイ側修正2022/07/28　STEP2_W　課題管理表№14：設計書「修正履歴」シートの「2022/07/14仕様変更分」「№15,16,18修正  №27追加」Start
      this.queryParams = {
        工事依頼No: this.$route.params.工事依頼No,
        発注サブNo: this.$route.params.発注サブNo,
        検収回数: this.$route.params.検収回数,
        tabFocus: this.$route.params.tabFocus,
        承認種類: this.$route.params.承認種類
      }

      if (this.queryParams.工事依頼No && this.queryParams.tabFocus === 'sagyo') {
        this.doActiveName = 'sagyo'
        this.oldSelectedTab = 'sagyo'
        this.syuzenForm.工事依頼No = this.queryParams.工事依頼No
        this.searchForm.koujiiraiNo = this.queryParams.工事依頼No.substring(0, 2)
        this.searchForm.koujiiraiNo1 = this.queryParams.工事依頼No.substring(2)
        this.sagyo発注サブNo = this.queryParams.発注サブNo
        this.sagyo検収回数 = this.queryParams.検収回数
      } else if (this.queryParams.工事依頼No && this.queryParams.tabFocus === 'mitsu') {
        this.doActiveName = 'mitsu'
        this.oldSelectedTab = 'mitsu'
        this.syuzenForm.工事依頼No = this.queryParams.工事依頼No
        this.searchForm.koujiiraiNo = this.queryParams.工事依頼No.substring(0, 2)
        this.searchForm.koujiiraiNo1 = this.queryParams.工事依頼No.substring(2)
      } else if (this.queryParams.工事依頼No && this.queryParams.tabFocus === 'hatchu') {
        this.doActiveName = 'hatchu'
        this.oldSelectedTab = 'hatchu'
        this.syuzenForm.工事依頼No = this.queryParams.工事依頼No
        this.searchForm.koujiiraiNo = this.queryParams.工事依頼No.substring(0, 2)
        this.searchForm.koujiiraiNo1 = this.queryParams.工事依頼No.substring(2)
      } else {
        this.doActiveName = 'irai'
        this.oldSelectedTab = 'irai'
        if (this.queryParams.工事依頼No) {
          this.syuzenForm.工事依頼No = this.queryParams.工事依頼No
          this.searchForm.koujiiraiNo = this.queryParams.工事依頼No.substring(0, 2)
          this.searchForm.koujiiraiNo1 = this.queryParams.工事依頼No.substring(2)
        }
      }

      this.onSearch()

      // ハノイ側修正2022/07/28　STEP2_W　課題管理表№14：設計書「修正履歴」シートの「2022/07/14仕様変更分」「№15,16,18修正  №27追加」END
      this.getPageTitleByMenu() // ハノイ側修正2023/03/14　課題管理表№350：「2023/03/10依頼分」「画面タイトルが正しく表示されないケースがある。」
      this.$store.commit('setpageTitle', this.menuOptions.SubMenuName) // ハノイ側修正2023/03/14　課題管理表№350：「2023/03/10依頼分」「画面タイトルが正しく表示されないケースがある。」
      this.setPageTitle(this.menuOptions.SubMenuName)
    },
    init () {
      let userLogIn = this.$store.getters.getUserInfo()
      this.userInfo.ユーザーＩＤ = userLogIn.userId
      this.userInfo.会社コード = userLogIn.会社コード

      this.getPageTitleByMenu()
      this.iraiForm.工事区分 = ''
      this.iraiForm.大区分 = ''
      this.iraiForm.中区分 = ''
      this.iraiForm.小区分 = ''
      this.iraiForm.依頼状況 = ''
      this.iraiForm.sgr担当部署 = ''
      this.iraiForm.中止保留区分 = ''
      this.画面入力制御disabled.取引先 = true
      this.画面入力制御disabled.検収回数 = true
      this.画面入力制御disabled.否認_差戻事由 = true
      this.hatchuForm.事業領域コード = '0'
      this.hatchuForm.外内販コード = '0'
      this.sagyoForm.事業領域 = '0'
      this.sagyoForm.外内販 = '0'
      this.http
        .get('/api/Reafs_W/Shonin/WD00520/GetDataN017')
        .then((response) => {
          this.名称取得方法17 = response.data
        })
    },
    getPageTitleByMenu () {
      let menuInfo = this.$store.getters.getMenuInfo()
      const menuOptions = menuInfo.find((item) => {
        return item.MenuId === this.menuID && item.SubMenuId === this.subMenuID
      })
      Object.assign(this.menuOptions, menuOptions)
      if (this.activeName === 'irai') {
        this.menuOptions.SubMenuName = '依頼参照・承認（見積確認）'
      } else if (this.activeName === 'mitsu') {
        this.menuOptions.SubMenuName = '依頼参照・承認（見積確認）'
      } else if (this.activeName === 'gaiyoSho') {
        this.menuOptions.SubMenuName = '依頼参照・承認（見積確認/実施判断）'
      } else if (this.activeName === 'hatchu') {
        this.menuOptions.SubMenuName = '依頼参照・承認（発注申請）'
      } else if (this.activeName === 'sagyo') {
        this.menuOptions.SubMenuName = '依頼参照・承認（完了確認）'
      }
      // this.setPageTitle(this.menuOptions.SubMenuName)
    },
    onBlurKoujiiraiNoNendo () {
      if (this.searchForm.koujiiraiNo === '' || this.searchForm.koujiiraiNo.trim() === '') {
        // return true
      } else {
        this.searchForm.koujiiraiNo = this.searchForm.koujiiraiNo.padStart(2, '0')
      }
      // this.savePageSearchData()
    },
    onBlurKoujiiraiNoRenban () {
      if (this.searchForm.koujiiraiNo1 === '' || this.searchForm.koujiiraiNo1.trim() === '') {
        // return true
      } else {
        this.searchForm.koujiiraiNo1 = this.searchForm.koujiiraiNo1.padStart(5, '0')
      }
      // this.savePageSearchData()
    },
    async onSearch (isGetInitialDisplayTab = false) {
      this.kengen = false
      this.loading = true
      this.iraiFormInit = {}
      this.mitsuFormInit = {}
      this.gaiyoShoFormInit = {}
      this.hatchuFormInit = {}
      this.hatchuFormSearched = {}
      this.sagyoFormInit = {}
      this.sagyoFormSearched = {}

      this.resetCommon()
      this.resetSyuzenForm()
      this.resetIraiForm()
      this.resetMitsuForm()
      this.resetGaiyoShoForm()
      this.resetHatchuForm()
      this.resetSagyoForm()
      this.resetButtonState()
      if (this.checkKoji() === false) {
        return false
      }
      let checkExist = false
      let hasData = true
      await this.http
        .get('/api/Reafs_W/Shonin/WD00520/CheckF100Exist', { 工事依頼No: this.searchData.工事依頼No })
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

      // ハノイ側修正2022/09/23　STEP2_W　課題管理表№119：設計書「修正履歴」シートの「2022/09/20仕様変更分」「参照権限チェック追加」Start
      if (await this.onHeaderSearch()) {
        this.kengen = true
        this.syuzenForm.工事依頼No = this.searchData.工事依頼No
        if (this.doActiveName) {
          this.activeName = this.doActiveName
          this.oldSelectedTab = this.activeName
          // reset flag
          this.doActiveName = ''
        } else {
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」START
          if (isGetInitialDisplayTab) {
            await this.http
              .get('/api/Reafs_W/Shonin/WD00520/GetInitialDisplayTab',
                {会社コード: this.searchData.会社コード, 工事依頼No: this.searchData.工事依頼No}, 'アクセスしています....')
              .then((response) => {
                this.loading = false
                if (response.data.b見積Tab) {
                  this.activeName = 'mitsu'
                  this.oldSelectedTab = 'mitsu'
                } else if (response.data.b発注申請Tab) {
                  this.activeName = 'hatchu'
                  this.oldSelectedTab = 'hatchu'
                } else if (response.data.b作業結果Tab) {
                  this.activeName = 'sagyo'
                  this.oldSelectedTab = 'sagyo'
                } else {
                  this.activeName = 'irai'
                  this.oldSelectedTab = 'irai'
                }
              })
          }
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」END
        }

        if (this.activeName === 'irai') {
          await this.getDataIrai()// ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」
        }
        if (this.activeName === 'mitsu') {
          this.画面入力制御disabled.否認_差戻事由 = false
          this.is発注申請Tab = false
          await this.http
            .get('/api/Reafs_W/Shonin/WD00520/GetData見積Tab',
              {会社コード: this.searchData.会社コード, 工事依頼No: this.searchData.工事依頼No}, 'アクセスしています....')
            .then((response) => {
              if (response.data.data !== null) {
                this.mitsuForm = response.data.data || {}
                // this.dt明細部 = response.data.明細部
                this.f001_version = response.data.f001_version
                this.fw042_version = response.data.fw042_version
                this.mitsuForm.修繕区分小 = response.data.data.修繕区分小
                this.mitsuForm.dt税率別金額合計欄 = response.data.data.dt税率別金額合計欄
                this.否認_差戻事由 = response.data.data.否認_差戻事由
                Object.assign(this.mitsuFormInit, this.mitsuForm)
              } else {
                this.resetMitsuForm()
                hasData = false// ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」
              }
            })
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」START
          if (hasData) {
            await this.fnc_btn制御()
          }
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」END
        }
        if (this.activeName === 'gaiyoSho') {
          this.画面入力制御disabled.否認_差戻事由 = false
          this.is発注申請Tab = false
          await this.http.get('api/Reafs_W/Shonin/WD00520/GetData概要書Tab',
            {会社コード: this.searchData.会社コード, 工事依頼No: this.searchData.工事依頼No}, 'アクセスしています....')
            .then(async response => {
              if (response.status) {
                if (response.data.data !== null) {
                  Object.assign(this.gaiyoShoForm, response.data.data)
                  this.gaiyoShoForm.工事期間 = response.data.data.工事期間_期間表示部分 + '                    ' +
                response.data.data.工事期間_内_延べ + '                         ' + response.data.data.工事期間_日数 + ' 日間'
                  this.f001_version = response.data.f001_version
                  this.fw042_version = response.data.fw042_version
                  this.否認_差戻事由 = response.data.data.否認_差戻事由
                  Object.assign(this.gaiyoShoFormInit, this.gaiyoShoForm)
                } else {
                  this.resetGaiyoShoForm()
                  hasData = false// ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」
                }
              } else {
                this.showError(response.message)
              }
            })
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」START
          if (hasData) {
            await this.fnc_btn制御()
          }
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」END
        }
        if (this.activeName === 'hatchu') {
          this.画面入力制御disabled.否認_差戻事由 = false
          this.is発注申請Tab = true
          await this.http
            .get('/api/Reafs_W/Shonin/WD00520/GetData発注申請Tab',
              {会社コード: this.searchData.会社コード, 工事依頼No: this.searchData.工事依頼No}, 'アクセスしています....')
            .then((response) => {
              if (response.data.data !== null) {
                Object.assign(this.hatchuForm, response.data.data)
                $select発注書_請書value.発注書_請書value = response.data.data.発注書_請書
                // ハノイ側修正2022/10/07　STEP2_W　課題管理表№151：設計書「修正履歴」シートの「2022/10/06仕様変更分」「発注申請タブ内の発注書・請書のドロップダウンリスト内の条件修正」START
                if (response.data.data.発注書_請書) {
                  if (response.data.data.発注書_請書.length === 1) {
                    this.hatchuForm.発注書_請書 = response.data.data.発注書_請書[0].取引先
                    this.selectHatchuSho_Ukesho(response.data.data.発注書_請書[0].取引先)
                    this.画面入力制御disabled.発注書_請書 = true
                  } else {
                    if (response.data.data.発注書_請書[0].業者Default) {
                      this.hatchuForm.発注書_請書 = response.data.data.発注書_請書[0].業者Default
                      this.selectHatchuSho_Ukesho(response.data.data.発注書_請書[0].業者Default)
                    } else {
                      this.hatchuForm.発注書_請書 = response.data.data.発注書_請書[0].取引先
                      this.selectHatchuSho_Ukesho(response.data.data.発注書_請書[0].取引先)
                    }
                    this.画面入力制御disabled.発注書_請書 = false
                  }
                }
                // ハノイ側修正2022/10/07　STEP2_W　課題管理表№151：設計書「修正履歴」シートの「2022/10/06仕様変更分」「発注申請タブ内の発注書・請書のドロップダウンリスト内の条件修正」END
                this.f001_version = response.data.f001_version
                this.fw042_version = response.data.fw042_version
                this.hatchuForm.修繕区分小 = response.data.data.修繕区分小
                this.hatchuForm.dt税率別金額合計欄 = response.data.data.dt税率別金額合計欄
                this.否認_差戻事由 = response.data.data.否認_差戻事由
                $select事業領域value.事業領域value = response.data.data.data名称取得方法No35
                $select外内販value.外内販value = response.data.data.data名称取得方法No36
                this.hatchuForm.w費用負担部門按分フラグ = response.data.data.w費用負担部門按分フラグ
                this.hatchuForm.isDisabledHiyoAnbun = response.data.data.iEnableHiyoAnbun != '1'
                Object.assign(this.hatchuFormInit, this.hatchuForm)
              } else {
                this.resetHatchuForm()
                hasData = false// ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」
              }
            })
          if (this.hatchuForm.w費用負担部門按分フラグ === 2) {
            this.isDisplayHatchuAnbunNyuryoku = false
            this.hatchuForm.費用按分 = '0'
            this.hatchuForm.msg費用按分 = ''
          } else {
            this.isDisplayHatchuAnbunNyuryoku = true
            this.hatchuForm.費用按分 = '1'

            await this.getDataAnbunNyuryokuSumiMesseji('hatchu')
            this.hatchuForm.dt税率別金額合計欄.map((item, index) => {
              if (this.hatchuForm.dt按分入力済メッセージ[index].rowEmpty === '1') {
                if (this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額 === '') {
                  item['按分入力済メッセージ'] = '未入力'
                } else if (this.getNumber(this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額) === this.getNumber(item['率別売上金額税込'])) {
                  item['按分入力済メッセージ'] = '入力済'
                } else {
                  item['按分入力済メッセージ'] = '出来高入力済'
                }
              } else {
                item['按分入力済メッセージ'] = ''
              }
            })

            let isExist未入力 = true
            for (let i = 0; i < this.hatchuForm.dt税率別金額合計欄.length; i++) {
              if (this.hatchuForm.dt税率別金額合計欄[i].按分入力済メッセージ === '未入力') {
                isExist未入力 = false
                break
              }
            }
            if (!isExist未入力) {
              this.hatchuForm.msg費用按分 = '部門按分未入力があります。'
            } else {
              this.hatchuForm.msg費用按分 = ''
            }
          }
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」START
          if (hasData) {
            await this.fnc_btn制御()
          }
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」END
        }
        if (this.activeName === 'sagyo') {
          this.is発注申請Tab = false
          await this.http
            .get('/api/Reafs_W/Shonin/WD00520/GetDataSource取引先_検収回数',
              {会社コード: this.searchData.会社コード, 工事依頼No: this.searchData.工事依頼No}, 'アクセスしています....')
            .then((response) => {
              if (response.data !== null) {
                if (response.data.取引先) {
                  $select取引先value.取引先value = response.data.取引先.filter((a, i) => response.data.取引先.findIndex((s) => a.取引先 === s.取引先) === i)
                }
                $select検収回数value.検収回数value = response.data.検収回数
                let 優先順位 = 0
                let 検収回数 = 0
                if (response.data.取引先.length === 0) {
                  this.sagyo取引先 = ''
                  this.画面入力制御disabled.取引先 = true
                } else if (response.data.取引先.length === 1) {
                  優先順位 = response.data.取引先[0].優先順位
                  検収回数 = response.data.取引先[0].検収回数
                  this.sagyo取引先 = response.data.取引先[0].取引先
                  this.画面入力制御disabled.取引先 = true
                } else {
                  優先順位 = response.data.取引先[0].優先順位
                  検収回数 = response.data.取引先[0].検収回数
                  this.sagyo取引先 = response.data.取引先[0].取引先
                  this.画面入力制御disabled.取引先 = false
                }

                if (response.data.検収回数.length === 0) {
                  this.sagyo検収回数 = ''
                  this.画面入力制御disabled.検収回数 = true
                } else if (response.data.検収回数.length === 1) {
                  this.sagyo検収回数 = response.data.検収回数[0].サブNo_回数
                  this.画面入力制御disabled.検収回数 = true
                } else {
                  if (優先順位 === 2) {
                    this.sagyo検収回数 = response.data.検収回数[response.data.検収回数.length - 1].サブNo_回数
                  } else {
                    this.sagyo検収回数 = 検収回数// response.data.検収回数[0].サブNo_回数
                  }
                  this.画面入力制御disabled.検収回数 = false
                }
              } else {
                this.sagyo取引先 = ''
                this.画面入力制御disabled.取引先 = true
                this.sagyo検収回数 = ''
                this.画面入力制御disabled.検収回数 = true
                $select取引先value.取引先value = []
                $select検収回数value.検収回数value = []
                this.sagyoForm.費用負担部門コード = ''
                this.sagyoForm.費用負担部門名 = ''
                this.sagyoForm.事業領域コード = ''
                this.sagyoForm.事業領域名 = ''
                this.sagyoForm.外内販コード = ''
                this.sagyoForm.外内販名 = ''
              }
            })
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」START
          if (await this.fnc_getData作業結果Tab()) {
            await this.fnc_btn制御()
          } else {
            hasData = false
          }
          // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」END
        }
      } else {
        // this.activeName = 'irai'
        // this.oldSelectedTab = 'irai'
        hasData = false// ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」
      }
      // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」START
      if (!hasData) {
        this.activeName = 'irai'
        this.oldSelectedTab = 'irai'
        await this.getDataIrai()
      }
      // ハノイ側修正2022/11/09　STEP2_W　課題管理表№165：設計書「修正履歴」シートの「2022/10/14仕様変更分」「見積承認後の再表示が実行された後の表示がおかしい。」END
      // ハノイ側修正2022/09/23　STEP2_W　課題管理表№119：設計書「修正履歴」シートの「2022/09/20仕様変更分」「参照権限チェック追加」End
    },
    async getDataAnbunNyuryokuSumiMesseji (tab) {
      const data = {
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        tabmode: tab,
        dt税率別金額合計欄: this.getData税率別金額合計欄(tab)
      }
      let res
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/按分入力済メッセージ', data, 'アクセスしています....')
        .then((response) => {
          res = response
        }).catch((ex) => {
        }).finally(() => {
        })

      if (res.status) {
        if (tab === 'hatchu') {
          this.hatchuForm.dt按分入力済メッセージ = res.data.dt按分入力済メッセージ
        } else if (tab === 'sagyo') {
          this.sagyoForm.dt按分入力済メッセージ = res.data.dt按分入力済メッセージ
        }
      }
    },
    getData税率別金額合計欄 (tab) {
      const arrData = []
      if (tab === 'hatchu') {
        for (let i = 0; i < this.hatchuForm.dt税率別金額合計欄.length; i++) {
          const row = this.hatchuForm.dt税率別金額合計欄[i]
          const data = {
            業者コード: row.業者コード,
            業者コード枝番: row.業者コード枝番,
            税率: row.税率
          }
          arrData.push(data)
        }
      } else if (tab === 'sagyo') {
        let 業者コード = ''
        let 業者コード枝番 = ''
        if (this.syuzenForm.修繕区分小 === '05') {
          業者コード = this.sagyoForm.no32Data.業者コード
          業者コード枝番 = this.sagyoForm.no32Data.業者コード枝番
        } else {
          業者コード = this.sagyo取引先.split('_')[0]
          業者コード枝番 = this.sagyo取引先.split('_')[1]
        }
        for (let i = 0; i < this.sagyoForm.dt税率別金額合計欄.length; i++) {
          const row = this.sagyoForm.dt税率別金額合計欄[i]
          const data = {
            業者コード,
            業者コード枝番,
            税率: row.税率,
            検収回数: this.sagyo検収回数
          }
          arrData.push(data)
        }
      }
      return arrData
    },
    getNumber (string) {
      if (!string) return 0
      return (string + '' || '0').replaceAll(',', '') * 1
    },
    async getDataIrai () {
      this.画面入力制御disabled.否認_差戻事由 = true
      this.is発注申請Tab = false
      await this.fnc_getData依頼Tab()
      await this.fnc_btn制御()
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
    async beforeTabLeave (activeName, oldActiveName) {
      if (this.kengen === false) {
        if (activeName === 'irai') {
          this.activeName = 'irai'
          this.oldSelectedTab = 'irai'
          this.getPageTitleByMenu()
          this.$store.commit('setpageTitle', this.menuOptions.SubMenuName) // ハノイ側修正2023/03/14　課題管理表№350：「2023/03/10依頼分」「画面タイトルが正しく表示されないケースがある。」
          this.setPageTitle(this.menuOptions.SubMenuName)
          return Promise.resolve(true)
        }
        return Promise.reject(new Error('cancel change tab' + oldActiveName))
      }
      // this.画面入力制御disabled.否認_差戻事由 = false
      return new Promise(async (resolve, reject) => {
        let next = true
        // check data of next tab
        if (activeName === 'irai') {
          this.is発注申請Tab = false
          if (Object.keys(this.iraiFormInit).length === 0) {
            await this.http.get('api/Reafs_W/Shonin/WD00520/GetData依頼Tab',
              {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No},
              'アクセスしています....')
              .then(async response => {
                if (response.status) {
                  if (response.data !== null) {
                    Object.assign(this.iraiForm, response.data)
                    this.否認_差戻事由 = response.data.否認_差戻事由
                    if (this.iraiForm.部位コメント.length > 0) {
                      for (let item of this.iraiForm.部位コメント) {
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
                      /*  if (this.iraiForm.部位コメント.length == 1) {
                          this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                          this.iraiForm.部位コメント2 = ''
                          this.iraiForm.部位コメント3 = ''
                          this.iraiForm.部位コメント4 = ''
                          } else if (this.iraiForm.部位コメント.length == 2) {
                          this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                          this.iraiForm.部位コメント2 = this.iraiForm.部位コメント[1].部位コメント
                          this.iraiForm.部位コメント3 = ''
                          this.iraiForm.部位コメント4 = ''
                          } else if (this.iraiForm.部位コメント.length == 3) {
                          this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                          this.iraiForm.部位コメント2 = this.iraiForm.部位コメント[1].部位コメント
                          this.iraiForm.部位コメント3 = this.iraiForm.部位コメント[2].部位コメント
                          this.iraiForm.部位コメント4 = ''
                          } else if (this.iraiForm.部位コメント.length == 4) {
                          this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                          this.iraiForm.部位コメント2 = this.iraiForm.部位コメント[1].部位コメント
                          this.iraiForm.部位コメント3 = this.iraiForm.部位コメント[2].部位コメント
                          this.iraiForm.部位コメント4 = this.iraiForm.部位コメント[3].部位コメント
                      }  */
                    } else {
                      this.iraiForm.部位コメント1 = ''
                      this.iraiForm.部位コメント2 = ''
                      this.iraiForm.部位コメント3 = ''
                      this.iraiForm.部位コメント4 = ''
                    }
                    Object.assign(this.iraiFormInit, this.iraiForm)
                    this.画面入力制御disabled.否認_差戻事由 = true
                    next = true
                  } else {
                    this.resetIraiForm()
                    next = false
                  }
                } else {
                  this.showError(response.message)
                  next = false
                }
              })
          } else {
            this.否認_差戻事由 = this.iraiForm.否認_差戻事由
            this.画面入力制御disabled.否認_差戻事由 = true
            next = true
          }
        } else if (activeName === 'mitsu') {
          this.is発注申請Tab = false
          if (Object.keys(this.mitsuFormInit).length === 0) {
            await this.http
              .get('/api/Reafs_W/Shonin/WD00520/GetData見積Tab',
                {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No}, 'アクセスしています....')
              .then((response) => {
                if (response.data.data !== null) {
                  this.mitsuForm = response.data.data || {}
                  this.mitsuForm.修繕区分小 = response.data.data.修繕区分小
                  this.mitsuForm.dt税率別金額合計欄 = response.data.data.dt税率別金額合計欄
                  this.否認_差戻事由 = response.data.data.否認_差戻事由
                  Object.assign(this.mitsuFormInit, this.mitsuForm)
                  this.画面入力制御disabled.否認_差戻事由 = false
                  next = true
                } else {
                  this.resetMitsuForm()
                  next = false
                }
              })
          } else {
            this.否認_差戻事由 = this.mitsuForm.否認_差戻事由
            this.画面入力制御disabled.否認_差戻事由 = false
            next = true
          }
        } else if (activeName === 'gaiyoSho') {
          this.is発注申請Tab = false
          if (Object.keys(this.gaiyoShoFormInit).length === 0) {
            await this.http.get('api/Reafs_W/Shonin/WD00520/GetData概要書Tab',
              {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No}, 'アクセスしています....')
              .then((response) => {
                if (response.status) {
                  if (response.data.data !== null) {
                    Object.assign(this.gaiyoShoForm, response.data.data)
                    this.gaiyoShoForm.工事期間 = response.data.data.工事期間_期間表示部分 + '                    ' +
                      response.data.data.工事期間_内_延べ + '                         ' + response.data.data.工事期間_日数 + ' 日間'
                    this.f001_version = response.data.f001_version
                    this.fw042_version = response.data.fw042_version
                    this.否認_差戻事由 = response.data.data.否認_差戻事由
                    Object.assign(this.gaiyoShoFormInit, this.gaiyoShoForm)
                    this.画面入力制御disabled.否認_差戻事由 = false
                    next = true
                  } else {
                    next = false
                    this.resetGaiyoShoForm()
                  }
                } else {
                  next = false
                  this.showError(response.message)
                }
              })
          } else {
            this.否認_差戻事由 = this.gaiyoShoForm.否認_差戻事由
            this.画面入力制御disabled.否認_差戻事由 = false
            next = true
          }
        } else if (activeName === 'hatchu') {
          // ハノイ側修正2022/09/30　STEP2_W　課題管理表№131：設計書「修正履歴」シートの「2022/09/30仕様変更分」「タブ切替できない時はフッター部ボタンが切り替えてしまいます。」Start
          this.is発注申請Tab = false
          // ハノイ側修正2022/09/30　STEP2_W　課題管理表№131：設計書「修正履歴」シートの「2022/09/30仕様変更分」「タブ切替できない時はフッター部ボタンが切り替えてしまいます。」End
          if (Object.keys(this.hatchuFormInit).length === 0) {
            await this.http
              .get('/api/Reafs_W/Shonin/WD00520/GetData発注申請Tab',
                {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No}, 'アクセスしています....')
              .then((response) => {
                if (response.data.data !== null) {
                  // ハノイ側修正2022/09/30　STEP2_W　課題管理表№131：設計書「修正履歴」シートの「2022/09/30仕様変更分」「タブ切替できない時はフッター部ボタンが切り替えてしまいます。」Start
                  this.is発注申請Tab = true
                  // ハノイ側修正2022/09/30　STEP2_W　課題管理表№131：設計書「修正履歴」シートの「2022/09/30仕様変更分」「タブ切替できない時はフッター部ボタンが切り替えてしまいます。」End
                  Object.assign(this.hatchuForm, response.data.data)
                  this.hatchuForm.修繕区分小 = response.data.data.修繕区分小
                  this.hatchuForm.dt税率別金額合計欄 = response.data.data.dt税率別金額合計欄
                  $select発注書_請書value.発注書_請書value = response.data.data.発注書_請書
                  // ハノイ側修正2022/10/07　STEP2_W　課題管理表№151：設計書「修正履歴」シートの「2022/10/06仕様変更分」「発注申請タブ内の発注書・請書のドロップダウンリスト内の条件修正」START
                  if (response.data.data.発注書_請書) {
                    if (response.data.data.発注書_請書.length === 1) {
                      this.hatchuForm.発注書_請書 = response.data.data.発注書_請書[0].取引先
                      this.selectHatchuSho_Ukesho(response.data.data.発注書_請書[0].取引先)
                      this.画面入力制御disabled.発注書_請書 = true
                    } else {
                      if (response.data.data.発注書_請書[0].業者Default) {
                        this.hatchuForm.発注書_請書 = response.data.data.発注書_請書[0].業者Default
                        this.selectHatchuSho_Ukesho(response.data.data.発注書_請書[0].業者Default)
                      } else {
                        this.hatchuForm.発注書_請書 = response.data.data.発注書_請書[0].取引先
                        this.selectHatchuSho_Ukesho(response.data.data.発注書_請書[0].取引先)
                      }
                      this.画面入力制御disabled.発注書_請書 = false
                    }
                  }
                  // ハノイ側修正2022/10/07　STEP2_W　課題管理表№151：設計書「修正履歴」シートの「2022/10/06仕様変更分」「発注申請タブ内の発注書・請書のドロップダウンリスト内の条件修正」END
                  this.f001_version = response.data.f001_version
                  this.fw042_version = response.data.fw042_version
                  this.否認_差戻事由 = response.data.data.否認_差戻事由
                  $select事業領域value.事業領域value = response.data.data.data名称取得方法No35
                  $select外内販value.外内販value = response.data.data.data名称取得方法No36
                  this.hatchuForm.w費用負担部門按分フラグ = response.data.data.w費用負担部門按分フラグ
                  this.hatchuForm.isDisabledHiyoAnbun = response.data.data.iEnableHiyoAnbun != '1'
                  Object.assign(this.hatchuFormInit, this.hatchuForm)
                  this.画面入力制御disabled.否認_差戻事由 = false
                  next = true
                } else {
                  next = false
                  this.resetHatchuForm()
                }
              })
            if (this.hatchuForm.w費用負担部門按分フラグ === 2) {
              this.isDisplayHatchuAnbunNyuryoku = false
              this.hatchuForm.費用按分 = '0'
              this.hatchuForm.msg費用按分 = ''
            } else {
              this.isDisplayHatchuAnbunNyuryoku = true
              this.hatchuForm.費用按分 = '1'
            }
          } else {
            // ハノイ側修正2022/09/30　STEP2_W　課題管理表№131：設計書「修正履歴」シートの「2022/09/30仕様変更分」「タブ切替できない時はフッター部ボタンが切り替えてしまいます。」Start
            this.is発注申請Tab = true
            // ハノイ側修正2022/09/30　STEP2_W　課題管理表№131：設計書「修正履歴」シートの「2022/09/30仕様変更分」「タブ切替できない時はフッター部ボタンが切り替えてしまいます。」End
            this.否認_差戻事由 = this.hatchuForm.否認_差戻事由
            this.画面入力制御disabled.否認_差戻事由 = false
            next = true
          }

          if (this.hatchuForm.費用按分 == '1') {
            await this.getDataAnbunNyuryokuSumiMesseji('hatchu')
            this.hatchuForm.dt税率別金額合計欄.map((item, index) => {
              if (this.hatchuForm.dt按分入力済メッセージ[index].rowEmpty === '1') {
                if (this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額 === '') {
                  item['按分入力済メッセージ'] = '未入力'
                } else if (this.getNumber(this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額) === this.getNumber(item['率別売上金額税込'])) {
                  item['按分入力済メッセージ'] = '入力済'
                } else {
                  item['按分入力済メッセージ'] = '出来高入力済'
                }
              } else {
                item['按分入力済メッセージ'] = ''
              }
            })

            let isExist未入力 = true
            for (let i = 0; i < this.hatchuForm.dt税率別金額合計欄.length; i++) {
              if (this.hatchuForm.dt税率別金額合計欄[i].按分入力済メッセージ === '未入力') {
                isExist未入力 = false
                break
              }
            }
            if (!isExist未入力) {
              this.hatchuForm.msg費用按分 = '部門按分未入力があります。'
            } else {
              this.hatchuForm.msg費用按分 = ''
            }
          }
        } else if (activeName === 'sagyo') {
          this.is発注申請Tab = false
          if (Object.keys(this.sagyoFormInit).length === 0) {
            await this.http
              .get('/api/Reafs_W/Shonin/WD00520/GetDataSource取引先_検収回数',
                {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No}, 'アクセスしています....')
              .then((response) => {
                if (response.data !== null) {
                  if (response.data.取引先) {
                    $select取引先value.取引先value = response.data.取引先.filter((a, i) => response.data.取引先.findIndex((s) => a.取引先 === s.取引先) === i)
                  }
                  $select検収回数value.検収回数value = response.data.検収回数
                  let 優先順位 = 0
                  let 検収回数 = 0
                  if (response.data.取引先.length === 0) {
                    this.sagyo取引先 = ''
                    this.画面入力制御disabled.取引先 = true
                  } else if (response.data.取引先.length === 1) {
                    優先順位 = response.data.取引先[0].優先順位
                    検収回数 = response.data.取引先[0].検収回数
                    this.sagyo取引先 = response.data.取引先[0].取引先
                    this.画面入力制御disabled.取引先 = true
                  } else {
                    優先順位 = response.data.取引先[0].優先順位
                    検収回数 = response.data.取引先[0].検収回数
                    this.sagyo取引先 = response.data.取引先[0].取引先
                    this.画面入力制御disabled.取引先 = false
                  }
                  if (response.data.検収回数.length === 0) {
                    this.sagyo検収回数 = ''
                    this.画面入力制御disabled.検収回数 = true
                  } else if (response.data.検収回数.length === 1) {
                    this.sagyo検収回数 = response.data.検収回数[0].サブNo_回数
                    this.画面入力制御disabled.検収回数 = true
                  } else {
                    if (優先順位 === 2) {
                      this.sagyo検収回数 = response.data.検収回数[response.data.検収回数.length - 1].サブNo_回数
                    } else {
                      this.sagyo検収回数 = 検収回数// response.data.検収回数[0].サブNo_回数
                    }
                    this.画面入力制御disabled.検収回数 = false
                  }
                } else {
                  this.sagyo取引先 = ''
                  this.画面入力制御disabled.取引先 = true
                  this.sagyo検収回数 = ''
                  this.画面入力制御disabled.検収回数 = true
                  $select取引先value.取引先value = []
                  $select検収回数value.検収回数value = []
                  this.sagyoForm.費用負担部門コード = ''
                  this.sagyoForm.費用負担部門名 = ''
                  this.sagyoForm.事業領域コード = ''
                  this.sagyoForm.事業領域名 = ''
                  this.sagyoForm.外内販コード = ''
                  this.sagyoForm.外内販名 = ''
                }
              })
            let bHasData = await this.fnc_getData作業結果Tab()
            if (bHasData) {
              this.画面入力制御disabled.否認_差戻事由 = false
              next = true
            } else {
              // ハノイ側修正2022/10/10　STEP2_W　課題管理表№143：設計書「修正履歴」シートの「2022/10/04仕様変更分」「依頼タブから作業結果支払タブ切り替えた時にデータがない場合はフッター部の「否認・差戻事由」項目はLableからtextBoxに変更されてしまいます。」Start
              this.画面入力制御disabled.否認_差戻事由 = true
              // ハノイ側修正2022/10/10　STEP2_W　課題管理表№143：設計書「修正履歴」シートの「2022/10/04仕様変更分」「依頼タブから作業結果支払タブ切り替えた時にデータがない場合はフッター部の「否認・差戻事由」項目はLableからtextBoxに変更されてしまいます。」End
              next = false
            }
          } else {
            this.否認_差戻事由 = this.sagyoForm.否認_差戻事由
            this.画面入力制御disabled.否認_差戻事由 = false
            next = true
          }
        }
        // if (next === true) {
        //   if (oldActiveName === 'hatchu') {
        //     if (this.checkChangeTabHatchu()) {
        //       if (!await this.confirm((await this.getMsg('K000001')).data)) {
        //         next = false
        //       } else {
        //         next = true
        //         Object.assign(this.hatchuFormSearched, this.hatchuForm)
        //         Object.assign(this.hatchuFormInit, this.hatchuForm)
        //       }
        //     }
        //   } else if (oldActiveName === 'sagyo') {
        //     if (this.checkChangeTabSagyo()) {
        //       if (!await this.confirm((await this.getMsg('K000001')).data)) {
        //         next = false
        //       } else {
        //         next = true
        //         Object.assign(this.sagyoFormInit, this.sagyoForm)
        //         Object.assign(this.sagyoFormSearched, this.sagyoForm)
        //       }
        //     }
        //   }
        // } else {
        //   reject(new Error('cancel change tab' + oldActiveName))
        // }
        if (next === true) {
          this.activeName = activeName
          this.oldSelectedTab = oldActiveName
          this.getPageTitleByMenu()
          this.$store.commit('setpageTitle', this.menuOptions.SubMenuName) // ハノイ側修正2023/03/14　課題管理表№350：「2023/03/10依頼分」「画面タイトルが正しく表示されないケースがある。」
          this.setPageTitle(this.menuOptions.SubMenuName)
          await this.fnc_btn制御()
          resolve(true)
        } else {
          reject(new Error('cancel change tab' + oldActiveName))
        }
      })
    },
    async fnc_getData依頼Tab () {
      await this.http.get('api/Reafs_W/Shonin/WD00520/GetData依頼Tab',
        {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No}, 'アクセスしています....')
        .then((response) => {
          if (response.status) {
            if (response.data !== null) {
              Object.assign(this.iraiForm, response.data)
              this.否認_差戻事由 = response.data.否認_差戻事由
              if (this.iraiForm.部位コメント.length > 0) {
                for (let item of this.iraiForm.部位コメント) {
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
                /*  if (this.iraiForm.部位コメント.length == 1) {
                  this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                  this.iraiForm.部位コメント2 = ''
                  this.iraiForm.部位コメント3 = ''
                  this.iraiForm.部位コメント4 = ''
                } else if (this.iraiForm.部位コメント.length == 2) {
                  this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                  this.iraiForm.部位コメント2 = this.iraiForm.部位コメント[1].部位コメント
                  this.iraiForm.部位コメント3 = ''
                  this.iraiForm.部位コメント4 = ''
                } else if (this.iraiForm.部位コメント.length == 3) {
                  this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                  this.iraiForm.部位コメント2 = this.iraiForm.部位コメント[1].部位コメント
                  this.iraiForm.部位コメント3 = this.iraiForm.部位コメント[2].部位コメント
                  this.iraiForm.部位コメント4 = ''
                } else if (this.iraiForm.部位コメント.length == 4) {
                  this.iraiForm.部位コメント1 = this.iraiForm.部位コメント[0].部位コメント
                  this.iraiForm.部位コメント2 = this.iraiForm.部位コメント[1].部位コメント
                  this.iraiForm.部位コメント3 = this.iraiForm.部位コメント[2].部位コメント
                  this.iraiForm.部位コメント4 = this.iraiForm.部位コメント[3].部位コメント
                }  */
              } else {
                this.iraiForm.部位コメント1 = ''
                this.iraiForm.部位コメント2 = ''
                this.iraiForm.部位コメント3 = ''
                this.iraiForm.部位コメント4 = ''
              }
              Object.assign(this.iraiFormInit, this.iraiForm)
            } else {
              this.resetIraiForm()
            }
          } else {
            this.showError(response.message)
          }
        })
    },
    async fnc_getData作業結果Tab () {
      let res = false
      const 取引先 = this.sagyo取引先.split('_')
      const 業者コード = 取引先[0]
      const 業者コード枝番 = 取引先[1]
      const 検収回数 = this.sagyo検収回数
      // ハノイ側修正2022/10/10　STEP2_W　課題管理表№143：設計書「修正履歴」シートの「2022/10/04仕様変更分」「依頼タブから作業結果支払タブ切り替えた時にデータがない場合はフッター部の「否認・差戻事由」項目はLableからtextBoxに変更されてしまいます。」Start
      // this.画面入力制御disabled.否認_差戻事由 = false
      // ハノイ側修正2022/10/10　STEP2_W　課題管理表№143：設計書「修正履歴」シートの「2022/10/04仕様変更分」「依頼タブから作業結果支払タブ切り替えた時にデータがない場合はフッター部の「否認・差戻事由」項目はLableからtextBoxに変更されてしまいます。」End
      await this.http
        .get('/api/Reafs_W/Shonin/WD00520/GetData作業結果Tab', { 会社コード: this.searchData.会社コード,
          工事依頼No: this.syuzenForm.工事依頼No,
          業者コード: 業者コード,
          業者コード枝番: 業者コード枝番,
          検収回数: 検収回数,
          事業領域コード: this.sagyoForm.事業領域コード }
          , 'アクセスしています....')
        .then((response) => {
          if (response.data !== null) {
            this.否認_差戻事由 = '' // ハノイ側修正2023/06/19　課題管理表№354：「2023/06/16依頼分」「1. タブ切り替えた時に「否認・差戻事由」内容が消えます。2.「検収回数」を変更した時に「否認・差戻事由」内容は再表示していません。」
            res = true
            this.sagyoForm.税込発注金額_税込支払金額_合計 = response.data.税込発注金額_税込支払金額_合計
            this.sagyoForm.dt税率別金額合計欄 = response.data.dt税率別金額合計欄
            Object.assign(this.sagyoForm, response.data.dataRet)
            $select状態value.状態value = response.data.状態
            if (this.queryParams.承認種類) {
              if (response.data.状態.length === 0) {
                this.sagyoForm.状態 = ''
                this.画面入力制御disabled.状態 = true
              } else {
                if (this.queryParams.承認種類 === '工事完了確認') {
                  const 工事完了確認待ち = $select状態value.状態value.find(x => x.状態 == '工事完了確認待ち')
                  if (工事完了確認待ち) {
                    this.sagyoForm.状態 = '工事完了確認待ち'
                    this.否認_差戻事由 = 工事完了確認待ち.否認差戻事由
                    this.fw042_version = 工事完了確認待ち.fw042_version
                  }
                  const 工事完了確認済 = $select状態value.状態value.find(x => x.状態 == '工事完了確認済')
                  if (工事完了確認済) {
                    this.sagyoForm.状態 = '工事完了確認済'
                    this.否認_差戻事由 = 工事完了確認済.否認差戻事由
                    this.fw042_version = 工事完了確認済.fw042_version
                  }
                  const 工事完了差戻 = $select状態value.状態value.find(x => x.状態 == '工事完了差戻')
                  if (工事完了差戻) {
                    this.sagyoForm.状態 = '工事完了差戻'
                    this.否認_差戻事由 = 工事完了差戻.否認差戻事由
                    this.fw042_version = 工事完了差戻.fw042_version
                  }
                } else if (this.queryParams.承認種類 === '支払承認') {
                  const 支払承認待ち = $select状態value.状態value.find(x => x.状態 == '支払承認待ち')
                  if (支払承認待ち) {
                    this.sagyoForm.状態 = '支払承認待ち'
                    this.否認_差戻事由 = 支払承認待ち.否認差戻事由
                    this.fw042_version = 支払承認待ち.fw042_version
                  }
                  const 支払承認済 = $select状態value.状態value.find(x => x.状態 == '支払承認済')
                  if (支払承認済) {
                    this.sagyoForm.状態 = '支払承認済'
                    this.否認_差戻事由 = 支払承認済.否認差戻事由
                    this.fw042_version = 支払承認済.fw042_version
                  }
                  const 支払差戻 = $select状態value.状態value.find(x => x.状態 == '支払差戻')
                  if (支払差戻) {
                    this.sagyoForm.状態 = '支払差戻'
                    this.否認_差戻事由 = 支払差戻.否認差戻事由
                    this.fw042_version = 支払差戻.fw042_version
                  }
                  const 支払否認 = $select状態value.状態value.find(x => x.状態 == '支払否認')
                  if (支払否認) {
                    this.sagyoForm.状態 = '支払否認'
                    this.否認_差戻事由 = 支払否認.否認差戻事由
                    this.fw042_version = 支払否認.fw042_version
                  }
                }
                if (response.data.状態.length === 1) {
                  this.画面入力制御disabled.状態 = true
                } else {
                  this.画面入力制御disabled.状態 = false
                }
              }
            } else {
              if (response.data.状態.length === 0) {
                this.sagyoForm.状態 = ''
                this.否認_差戻事由 = ''
                this.画面入力制御disabled.状態 = true
              } else if (response.data.状態.length === 1) {
                this.sagyoForm.状態 = response.data.状態[0].状態
                this.否認_差戻事由 = response.data.状態[0].否認差戻事由
                this.fw042_version = response.data.状態[0].fw042_version
                this.画面入力制御disabled.状態 = true
              } else {
                this.sagyoForm.状態 = response.data.状態[0].状態
                this.否認_差戻事由 = response.data.状態[0].否認差戻事由
                this.fw042_version = response.data.状態[0].fw042_version
                this.画面入力制御disabled.状態 = false
              }
            }
            this.sagyoForm.否認_差戻事由 = this.否認_差戻事由 // ハノイ側修正2023/06/19　課題管理表№354：「2023/06/16依頼分」「1. タブ切り替えた時に「否認・差戻事由」内容が消えます。2.「検収回数」を変更した時に「否認・差戻事由」内容は再表示していません。」
            // if (response.data.チェック1 === '1') {
            //   this.sagyoForm.チェック1 = true
            // } else {
            //   this.sagyoForm.チェック1 = false
            // }

            // if (response.data.チェック2 === '1') {
            //   this.sagyoForm.チェック2 = true
            // } else {
            //   this.sagyoForm.チェック2 = false
            // }
            // ハノイ側修正2023/08/09　STEP2_W　課題管理表№385：設計書「修正履歴」シートの「2023/08/09仕様変更分」「業者状態の表示条件を修正」Start
            if (response.data.dataRet.existF005 === '' || response.data.dataRet.existF005 === null) {
              this.sagyoForm.業者状態 = '検収待ち'
            } else {
              if (response.data.dataRet.修繕区分小 == '05') {
                this.sagyoForm.業者状態 = '請求合意済'
              } else {
                if (response.data.dataRet.請求合意区分 === ' ') {
                  this.sagyoForm.業者状態 = '請求合意不要'
                } else if (response.data.dataRet.請求合意区分 === '1') {
                  this.sagyoForm.業者状態 = '請求合意済'
                } else if (response.data.dataRet.請求合意区分 === '0') {
                  this.sagyoForm.業者状態 = '請求合意待ち'
                }
              }
            }
            // ハノイ側修正2023/08/09　STEP2_W　課題管理表№385：設計書「修正履歴」シートの「2023/08/09仕様変更分」「業者状態の表示条件を修正」End
            if (response.data.dataRet.existF005 === '' || response.data.dataRet.existF005 === null) {
              this.sagyoForm.部門確認 = '部門確認前'
              this.dispBtnExistF005 = false
            }
            if (this.sagyoForm.チェック1 === true && this.sagyoForm.チェック2 === true) {
              this.endableBtnKakuninToroku = true
            } else {
              this.endableBtnKakuninToroku = false
            }
            if (response.data.dataRet.請求合意区分 === '0') {
              this.dispBtnBy請求合意区分F005 = false
            } else {
              this.dispBtnBy請求合意区分F005 = true
            }
            if (response.data.dataRet.部門確認フラグ === 0) {
              this.sagyoForm.部門確認 = '部門確認待ち'
            } else if (response.data.dataRet.部門確認フラグ === 1) {
              this.sagyoForm.部門確認 = '部門確認済'
            }
            if (response.data.data名称取得方法No32 !== undefined) {
              this.sagyoForm.no32Data = response.data.data名称取得方法No32
            }
            if (response.data.data名称取得方法No33.length > 0) {
              this.sagyoForm.費用負担部門コード = response.data.data名称取得方法No33[0].計上部門コード
              this.sagyoForm.費用負担部門名 = response.data.data名称取得方法No33[0].費用負担部門
              this.sagyoForm.事業領域コード = response.data.data名称取得方法No33[0].事業領域
              this.sagyoForm.事業領域 = response.data.data名称取得方法No33[0].事業領域 ? response.data.data名称取得方法No33[0].事業領域 : '0' // ハノイ側修正2023/03/23　STEP2_W　課題管理表№315：設計書「修正履歴」シートの「2022/08/29仕様変更分」「画面表示時の読込がない場合、初期値は選択なしの「ー」固定。」
              this.sagyoForm.事業領域名 = response.data.data名称取得方法No33[0].事業領域名
              this.sagyoForm.外内販コード = response.data.data名称取得方法No33[0].外内販
              this.sagyoForm.外内販 = response.data.data名称取得方法No33[0].外内販 ? response.data.data名称取得方法No33[0].外内販 : '0' // ハノイ側修正2023/03/23　STEP2_W　課題管理表№315：設計書「修正履歴」シートの「2022/08/29仕様変更分」「画面表示時の読込がない場合、初期値は選択なしの「ー」固定。」
              this.sagyoForm.外内販名 = response.data.data名称取得方法No33[0].外内販区分
            } else {
              this.sagyoForm.費用負担部門コード = ''
              this.sagyoForm.費用負担部門名 = ''
              this.sagyoForm.事業領域コード = ''
              this.sagyoForm.事業領域 = ''
              this.sagyoForm.事業領域名 = ''
              this.sagyoForm.外内販コード = ''
              this.sagyoForm.外内販 = ''
              this.sagyoForm.外内販名 = ''
            }
            this.f001_version = response.data.f001_version
            this.sagyoForm.sagyo取引先 = this.sagyo取引先
            this.sagyoForm.sagyo検収回数 = this.sagyo検収回数
            $select事業領域value.事業領域value = response.data.data名称取得方法No35
            $select外内販value.外内販value = response.data.data名称取得方法No36
            this.sagyoForm.w費用負担部門按分フラグ = response.data.dataRet.W費用負担部門按分フラグ
            Object.assign(this.sagyoFormInit, this.sagyoForm)
          } else {
            res = false
            this.resetSagyoForm()
          }
          if (this.画面入力制御disabled.チェック1 === false && this.画面入力制御disabled.チェック2 === false) {
            this.endableBtnKakuninToroku = true
          } else {
            this.endableBtnKakuninToroku = false
          }
        })

      if (this.sagyoForm.w費用負担部門按分フラグ === 2) {
        this.isDisplaySagyoAnbunNyuryoku = false
        this.sagyoForm.費用按分 = '0'
        this.sagyoForm.msg費用按分 = ''
      } else {
        this.isDisplaySagyoAnbunNyuryoku = true
        this.sagyoForm.費用按分 = '1'

        await this.getDataAnbunNyuryokuSumiMesseji('sagyo')
        this.sagyoForm.dt税率別金額合計欄.map((item, index) => {
          if (this.sagyoForm.dt按分入力済メッセージ[index].rowEmpty === '1') {
            if (this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額 === '' ||
            this.getNumber(this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額) !== this.getNumber(item['税込発注金額_税込支払金額'])) { // ハノイ側修正2023/01/16　STEP2_W　課題管理表№271：設計書「修正履歴」シートの「2022/01/11仕様変更分」「按分入力ボタン押下条件追記	」
              item['按分入力済メッセージ'] = '未入力'
            } else if (this.getNumber(this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額) === this.getNumber(item['税込発注金額_税込支払金額'])) {
              item['按分入力済メッセージ'] = '入力済'
            } else {
              // item['按分入力済メッセージ'] = '出来高入力済'// ハノイ側修正2023/01/16　STEP2_W　課題管理表№271：設計書「修正履歴」シートの「2022/01/11仕様変更分」「按分入力ボタン押下条件追記	」
            }
          } else {
            item['按分入力済メッセージ'] = ''
          }
        })

        let isExist未入力 = true
        for (let i = 0; i < this.sagyoForm.dt税率別金額合計欄.length; i++) {
          if (this.sagyoForm.dt税率別金額合計欄[i].按分入力済メッセージ === '未入力') {
            isExist未入力 = false
            break
          }
        }
        if (!isExist未入力) {
          this.sagyoForm.msg費用按分 = '部門按分未入力があります。'
        } else {
          this.sagyoForm.msg費用按分 = ''
        }
      }

      return res
    },
    async selectHatchuSho_Ukesho (val) {
      var obj = {}
      obj = $select発注書_請書value.発注書_請書value.find(item => {
        return item.取引先 === val
      })
      if (!obj) {
        this.取引先コード = ''
        this.取引先コード枝番 = ''
        this.取引先名 = ''
        return
      }
      this.取引先コード = obj.取引先コード
      this.取引先コード枝番 = obj.取引先コード枝番
      this.取引先名 = obj.取引先名

      let b発注書請書Check = false
      await this.http.get('api/Reafs_W/Shonin/WD00520/発注承認済チェック',
        {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No}, 'アクセスしています....')
        .then((response) => {
          let bhasData = false
          if (Object.getPrototypeOf(response.data) == Object.prototype) {
            bhasData = Object.keys(response.data).length > 0
          } else {
            bhasData = response.data.length > 0
          }
          if (bhasData === true) {
            b発注書請書Check = true
          }
        })
      if (b発注書請書Check) {
        if (this.hatchuForm.修繕区分小 === '05') {
          this.画面入力制御disabled.btn発注書請書 = true
        } else {
          if (await this.checkWeb合意() === true) {
            this.画面入力制御disabled.btn発注書請書 = true
          } else {
            this.画面入力制御disabled.btn発注書請書 = false
          }
        }
      }
    },
    async selectJotai (val) {
      let 否認_差戻事由 = ''
      let fw042_version = ''
      let itemSelect = $select状態value.状態value.find((item) => {
        return item.状態 === val
      })
      if (itemSelect) {
        否認_差戻事由 = itemSelect.否認差戻事由
        fw042_version = itemSelect.fw042_version
      }
      this.否認_差戻事由 = 否認_差戻事由
      this.fw042_version = fw042_version
      await this.fnc_btn制御()
    },
    async selectTorihikisaki (val) {
      const 取引先 = val.split('_')
      const 業者コード = 取引先[0]
      const 業者コード枝番 = 取引先[1]
      await this.http
        .get('/api/Reafs_W/Shonin/WD00520/GetDataSource検収回数', { 会社コード: this.searchData.会社コード,
          工事依頼No: this.syuzenForm.工事依頼No,
          業者コード,
          業者コード枝番 })
        .then((response) => {
          let 優先順位 = 0
          $select検収回数value.検収回数value = response.data
          let itemSelect = $select取引先value.取引先value.find((item) => {
            return item.取引先 === val
          })
          if (itemSelect) {
            優先順位 = itemSelect.優先順位
          }
          if (response.data.length === 0) {
            this.sagyo検収回数 = ''
            this.画面入力制御disabled.検収回数 = true
          } else if (response.data.length === 1) {
            this.sagyo検収回数 = response.data[0].サブNo_回数
            this.画面入力制御disabled.検収回数 = true
          } else {
            if (優先順位 === 2) {
              this.sagyo検収回数 = response.data[response.data.length - 1].サブNo_回数
            } else {
              this.sagyo検収回数 = response.data[0].サブNo_回数
            }
            this.画面入力制御disabled.検収回数 = false
          }
        })
      await this.fnc_getData作業結果Tab()
      await this.fnc_btn制御()
    },
    async selectKenshuKaisu (val) {
      // this.sagyo検収回数 = val
      await this.fnc_getData作業結果Tab()
      await this.fnc_btn制御()
    },
    async onSeeAttachment () {
      this.paramsTyohyoIchiran.工事依頼No = this.syuzenForm.工事依頼No
      this.showDialogTyohyoIchiran = true
    },
    onBlurRingiKianBango () {
      if (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 !== '' && this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号.length !== 18) {
        this.getMsg('E040121').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref稟議起案番号.focus()
          }})
        })
        return false
      }
    },
    async onBlurRingiKaketsuBango () {
      await this.fnc_btn制御()
      if (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 !== '' && this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号.length !== 11) {
        this.getMsg('E040121').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref稟議可決番号.focus()
          }})
        })
        return false
      }
    },
    async checkVersion (tabName) {
      return new Promise((resolve) => {
        let 種類 = ''
        let 検収回数 = ''
        const para検収回数 = this.sagyo検収回数
        switch (tabName) {
          case 'gaiyoSho':
            種類 = '32'
            break
          case 'mitsu':
            種類 = '32'
            break
          case 'hatchu':
            種類 = '41'
            break
          case 'sagyo':
            if (this.sagyoForm.状態 === '支払承認待ち') {
              種類 = '61'
            }
            if (this.sagyoForm.状態 === '工事完了確認待ち') {
              種類 = '52'
            }
            検収回数 = para検収回数
            break
        }
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/fnc_getVersion',
            { 会社コード: this.searchData.会社コード, 依頼No: this.syuzenForm.工事依頼No, 種類, 検収回数 })
          .then((response) => {
            if ((this.f001_version && (this.f001_version !== response.data.f001_version || !response.data.f001_version)) ||
            (this.fw042_version && (this.fw042_version !== response.data.fw042_version || !response.data.fw042_version))) {
              resolve(false)
            } else {
              resolve(true)
            }
          })
      })
    },
    async regisValidate (tabName) {
      if (tabName !== '') {
        if (this.isUpdate && !await this.checkVersion(tabName)) {
          this.getMsg('E040098').then((response) => {
            this.showError(response.data)
          })
          return false
        }
      }
      return true
    },
    async fnc_不備チェック () { // ハノイ側修正2022/08/01　STEP2_W　課題管理表№376：設計書「修正履歴」シートの「2023/07/26仕様変更分」「支払承認時のボタン押下不可条件に不備のチェックを追加」
      return new Promise((resolve) => {
        this.http
          .post('/api/Reafs_W/Shonin/WD00520/不備チェック',
            { 会社コード: this.searchData.会社コード,
              工事依頼No: this.syuzenForm.工事依頼No,
              工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
              検収回数: this.sagyo検収回数,
              業者コード: this.sagyo取引先.split('_')[0],
              業者コード枝番: this.sagyo取引先.split('_')[1]
              // 修繕区分小: this.syuzenForm.修繕区分小 // ハノイ側修正2022/08/09　STEP2_W　課題管理表№384：設計書「修正履歴」シートの「2023/08/08仕様変更分」「不備チェックの条件除外処理を削除」
            })
          .then((response) => {
            resolve(response.data)
          })
      })
    },
    async fnc_承認ルートチェック (btnName) {
      return new Promise((resolve) => {
        let 表示金額 = 0
        let 検収回数 = 0
        let 状態 = ''
        if (this.activeName === 'mitsu') {
          表示金額 = this.mitsuForm.金額_税抜_合計
        } else if (this.activeName === 'hatchu') {
          表示金額 = this.hatchuForm.金額_税抜_合計 // ハノイ側修正2022/10/19　STEP2_W　課題管理表№179：設計書「修正履歴」シートの「2022/10/18仕様変更分」「発注の申請データ作成時に表示金額としてセットされる値がおかしい。」
        } else if (this.activeName === 'sagyo') {
          const para検収回数 = this.sagyo検収回数
          状態 = this.sagyoForm.状態
          検収回数 = para検収回数
          if (this.sagyoForm.状態 === '支払承認待ち') {
            表示金額 = this.sagyoForm.税込支払金額
          }
        }
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/承認ルートチェック',
            { 会社コード: this.searchData.会社コード,
              工事依頼No: this.syuzenForm.工事依頼No,
              工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
              状態: 状態,
              表示金額: 表示金額,
              検収回数: 検収回数,
              取引先コード: this.sagyo取引先.split('_')[0], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
              取引先コード枝番: this.sagyo取引先.split('_')[1], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
              修繕区分小: this.syuzenForm.修繕区分小,
              tabName: this.activeName,
              btnName: btnName})
          .then((response) => {
            if (btnName === 'btn承認') {
              this.fw042_最終承認フラグ = response.data.最終承認フラグ
              if (response.data.最終承認フラグ !== 0) {
                resolve(true)
              } else {
                if (response.data.data.length > 0) {
                  resolve(true)
                } else {
                  resolve(false)
                }
              }
            } else {
              if (response.data.data.length > 0) {
                resolve(true)
              } else {
                resolve(false)
              }
            }
          })
      })
    },
    async onUpdate (e) {
      if (!await this.updateValidate('hatchu')) {
        return false
      }
      let msg = (await this.getMsg('K000005')).data
      if (!await this.confirm(msg)) {
        return false
      }
      this.loading = true
      let result = ''
      let msgErr = ''
      const data = {
        DisplayTab: this.activeName,
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        計上部門コード: this.hatchuForm.費用負担部門コード,
        事業領域: this.hatchuForm.事業領域コード,
        外内販: this.hatchuForm.外内販コード,
        F001_受注マスタ: {
          会社コード: this.searchData.会社コード,
          工事依頼No: this.syuzenForm.工事依頼No,
          // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「UPD①条件を否認ボタン全てに変更、UPD④・⑤条件及び更新項目編集内容修正」START
          稟議起案番号: this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号,
          稟議可決番号: this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号,
          // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「UPD①条件を否認ボタン全てに変更、UPD④・⑤条件及び更新項目編集内容修正」END
          修繕区分小: this.hatchuForm.修繕区分小,
          依頼営業店稟議申請許可フラグ: this.hatchuForm.依頼営業店稟議申請許可,
          W費用負担部門按分フラグ: this.hatchuForm.費用按分 == '0' ? '2' : '0' // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「UPD①条件を否認ボタン全てに変更、UPD④・⑤条件及び更新項目編集内容修正」
        }
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/更新ボタン', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
          this.loading = false
        })
      if (result) {
        let msgM040004 = (await this.getMsg('M040004')).data
        let msgM040002 = (await this.getMsg('M040002')).data
        if (await this.showSuccess(msgM040004 + this.syuzenForm.工事依頼No + msgM040002)) {
          this.onSearch()
        }
      } else {
        this.showError(msgErr)
        return false
      }
    },
    async updateValidate (tab) {
      if (tab === 'hatchu') {
        if (!this.hatchuForm.費用負担部門コード) {
          this.getMsg('E040373').then((res) => {
            this.showError(res.data)
          })
          this.$refs.refHatchu費用負担部門.focus()
          return false
        }

        if (this.hatchuForm.事業領域コード === '0') {
          this.getMsg('E040570').then((res) => {
            this.showError(res.data)
          })
          this.$refs.refHatchu事業領域.focus()
          return false
        }
        if (this.hatchuForm.外内販コード === '0') {
          this.getMsg('E040572').then((res) => {
            this.showError(res.data)
          })
          this.$refs.refHatchu外内販区分.focus()
          return false
        }
        if (this.hatchuForm.msg費用按分 !== '') {
          this.getMsg('E040777').then((response) => {
            this.showError(response.data, {callback: () => {
              document.getElementById('refHatchuAnbunNyuryoku0').focus()
            }})
          })
          return false
        }
      } else if (tab === 'sagyo' && this.isShiharai) {
        if (!this.sagyoForm.費用負担部門コード) {
          this.getMsg('E040373').then((res) => {
            this.showError(res.data)
          })
          this.$refs.refSagyo費用負担部門.focus()
          return false
        }

        if (this.sagyoForm.事業領域 === '0') {
          this.getMsg('E040570').then((res) => {
            this.showError(res.data)
          })
          this.$refs.refSagyo事業領域.focus()
          return false
        }
        if (this.sagyoForm.外内販 === '0') {
          this.getMsg('E040572').then((res) => {
            this.showError(res.data)
          })
          this.$refs.refSagyo外内販.focus()
          return false
        }
      }

      return true
    },
    get名称取得方法No34 () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: this.hatchuForm.費用負担部門コード })
          .then((response) => {
            resolve(response.data)
          })
      })
    },
    async onApprovalApplication (e) {
      this.loading = true
      let result = ''
      let msgErr = ''
      // ハノイ側修正2022/10/18　STEP2_W　課題管理表№178：設計書「修正履歴」シートの「2022/10/17仕様変更分」「稟議起案番号や稟議可決番号を入力済みなのに、発注稟議申請処理が実行されてしまう。」Start
      let check稟議申請パラメータ = false
      let cur取引先 = []
      // ハノイ側修正2022/10/18　STEP2_W　課題管理表№178：設計書「修正履歴」シートの「2022/10/17仕様変更分」「稟議起案番号や稟議可決番号を入力済みなのに、発注稟議申請処理が実行されてしまう。」End
      if (this.hatchuForm.費用負担部門コード) {
        const dataCheck名称取得方法No34 = await this.get名称取得方法No34()
        if (dataCheck名称取得方法No34 === null) {
          this.getMsg('E040747').then((response) => {
            this.showError(response.data, {callback: () => {
              this.$refs.refHatchu費用負担部門.focus()
            }})
          })
          return false
        }
      }
      if (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 !== '' && this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 === '' &&
      (!(this.hatchuForm.負担区分 === '2' && (this.hatchuForm.修繕区分小 === '02' || this.hatchuForm.修繕区分小 === '03')))) {
        this.getMsg('E040677').then((response) => {
          this.showError(response.data, {callback: () => {
            this.$refs.ref稟議可決番号.focus()
          }})
        })
        return false
      }
      // ハノイ側修正2022/08/30　STEP2_W　課題管理表№67：設計書「修正履歴」シートの「2022/08/24仕様変更分」「費用負担部門、事業領域、外内販の入力チェック追加」Start
      if (!await this.updateValidate(this.activeName)) {
        return false
      }
      // ハノイ側修正2022/08/30　STEP2_W　課題管理表№67：設計書「修正履歴」シートの「2022/08/24仕様変更分」「費用負担部門、事業領域、外内販の入力チェック追加」End
      if (!await this.fnc_承認ルートチェック('btn稟議申請')) {
        this.getMsg('E040198').then((response) => {
          this.showError(response.data)
        })
        e.target.parentNode
        return false
      }
      // ハノイ側修正2023/04/07　STEP2_W　課題管理表№321：設計書「修正履歴」シートの「2023/04/06仕様変更分」「事前発注稟議申請時の申請額チェック追加」Start
      const data = {
        DisplayTab: this.activeName,
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        計上部門コード: this.hatchuForm.費用負担部門コード,
        事業領域: this.hatchuForm.事業領域コード,
        外内販: this.hatchuForm.外内販コード,
        否認_差戻事由: this.否認_差戻事由,
        表示金額: this.hatchuForm.金額_税抜_合計, // ハノイ側修正2022/10/19　STEP2_W　課題管理表№179：設計書「修正履歴」シートの「2022/10/18仕様変更分」「発注の申請データ作成時に表示金額としてセットされる値がおかしい。」
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        営業店名: this.syuzenForm.営業店名, // ハノイ側修正2022/09/16　課題管理表№98：設計書「修正履歴」シートの「2022/09/07仕様変更分」「各パラメータセット内容追記・修正」
        依頼者名: this.iraiForm.依頼者名, // ハノイ側修正2022/09/16　課題管理表№98：設計書「修正履歴」シートの「2022/09/07仕様変更分」「各パラメータセット内容追記・修正」
        F001_受注マスタ: {
          会社コード: this.searchData.会社コード,
          工事依頼No: this.syuzenForm.工事依頼No,
          稟議起案番号: this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号,
          稟議可決番号: this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号,
          修繕区分小: this.hatchuForm.修繕区分小,
          // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「UPD①条件を否認ボタン全てに変更、UPD④・⑤条件及び更新項目編集内容修正」START
          依頼営業店稟議申請許可フラグ: this.hatchuForm.依頼営業店稟議申請許可,
          W費用負担部門按分フラグ: this.hatchuForm.費用按分 == '0' ? '2' : '0'
          // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「UPD①条件を否認ボタン全てに変更、UPD④・⑤条件及び更新項目編集内容修正」END
        },
        原価金額税抜: this.hatchuForm.金額_税抜_合計
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/申請額チェック', data)
        .then((response) => {
          result = response.status
          msgErr = response.message
          this.loading = false
        })
      if (!result) {
        this.showError(msgErr)
        return false
      }
      // ハノイ側修正2023/04/07　STEP2_W　課題管理表№321：設計書「修正履歴」シートの「2023/04/06仕様変更分」「事前発注稟議申請時の申請額チェック追加」End
      if (this.activeName === 'hatchu') {
        let msgConfirm = (await this.getMsg('K040078')).data// ハノイ側修正2022/11/03　課題管理表№203：設計書「修正履歴」シートの「2022/11/02仕様変更分」「稟議申請ボタン押下時のメッセージ変更」
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }

      if (!await this.regisValidate(this.activeName)) {
        return false
      }

      // ハノイ側修正2022/10/18　STEP2_W　課題管理表№178：設計書「修正履歴」シートの「2022/10/17仕様変更分」「稟議起案番号や稟議可決番号を入力済みなのに、発注稟議申請処理が実行されてしまう。」Start
      if (this.activeName === 'hatchu' && !this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 && !this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号) {
        check稟議申請パラメータ = true
        cur取引先 = (this.hatchuForm.dtHatchu || []).map(x => x.取引先)
          .filter((value, index, self) => self.indexOf(value) === index).join('・')
      }
      // ハノイ側修正2022/10/18　STEP2_W　課題管理表№178：設計書「修正履歴」シートの「2022/10/17仕様変更分」「稟議起案番号や稟議可決番号を入力済みなのに、発注稟議申請処理が実行されてしまう。」End
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/稟議申請ボタン', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
          this.loading = false
        })
      if (result) {
        let msgM040004 = (await this.getMsg('M040004')).data
        let msgM040007 = (await this.getMsg('M040007')).data
        if (await this.showSuccess(msgM040004 + this.syuzenForm.工事依頼No + msgM040007)) {
          await this.onSearch()
        }
      } else {
        this.showError(msgErr)
        return false
      }
      // ハノイ側修正2022/08/11　STEP2_W　課題管理表№30：依頼参照・承認（発注申請）シートの「2022/08/09仕様変更分」◆発注申請タブ内の稟議申請ボタン押下後の処理追加 START
      if (check稟議申請パラメータ) {
        data.lst取引先 = cur取引先
        let res
        await this.http
          .post('/api/Reafs_W/Shonin/WD00520/稟議申請パラメータ', data, 'アクセスしています....')
          .then((response) => {
            res = response
            msgErr = response.message
            this.loading = false
          })
        if (res.status) {
          var newwindow = window.open() // ハノイ側修正2022/09/14　課題管理表№93：「2022/09/07依頼分」「稟議申請時の処理の見直し」
          newwindow.document.write(res.data) // ハノイ側修正2022/09/14　課題管理表№93：「2022/09/07依頼分」「稟議申請時の処理の見直し」
          newwindow.document.close() // ハノイ側修正2022/10/06 「javascriptが実行されない件ですが、以下の一文を追加するだけで解決するかもしれませんので共有させていただきます。」
        } else {
          if (msgErr) {
            this.showError(msgErr)
          }
          return false
        }
      }
      // ハノイ側修正2022/08/11　STEP2_W　課題管理表№30：依頼参照・承認（発注申請）シートの「2022/08/09仕様変更分」◆発注申請タブ内の稟議申請ボタン押下後の処理追加 END
    },
    async onWithdrawal () {
      this.loading = true
      let result = ''
      let msgErr = ''
      if (this.activeName === 'hatchu') {
        let msgConfirm = (await this.getMsg('K040025')).data
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }
      if (!await this.regisValidate(this.activeName)) {
        return false
      }
      const data = {
        // DisplayTab: this.activeName,
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        修繕区分小: this.hatchuForm.修繕区分小
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/取下ボタン', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
          this.loading = false
        })
      if (result) {
        let msgM040004 = (await this.getMsg('M040004')).data
        let msgM040008 = (await this.getMsg('M040008')).data
        if (await this.showSuccess(msgM040004 + this.syuzenForm.工事依頼No + msgM040008)) {
          await this.onSearch()
        }
      } else {
        this.showError(msgErr)
        return false
      }
    },
    async onRemand () {
      this.loading = true
      let result = ''
      let msgErr = ''
      let resData = {}
      if (this.否認_差戻事由 === '' || this.否認_差戻事由 === null) {
        this.getMsg('E040357').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref否認_差戻事由.focus()
        return false
      }
      if (this.activeName === 'gaiyoSho' || this.activeName === 'mitsu') {
        let msgConfirm = (await this.getMsg('K040013')).data
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }
      if (this.activeName === 'hatchu') {
        let msgConfirm = (await this.getMsg('K040026')).data
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }
      if (this.activeName === 'sagyo') {
        if (this.sagyoForm.状態 === '工事完了確認待ち') {
          let msgConfirm = (await this.getMsg('K040039')).data
          if (!await this.confirm(msgConfirm)) {
            this.loading = false
            return false
          }
        } else if (this.sagyoForm.状態 === '支払承認待ち') {
          let msgConfirm = (await this.getMsg('K040070')).data
          if (!await this.confirm(msgConfirm)) {
            this.loading = false
            return false
          }
        }
      }
      // if (!await this.regisValidate(this.activeName)) {
      //   return false
      // }
      const 検収回数 = this.sagyo検収回数
      const 取引先 = this.sagyo取引先.split('_')
      const 取引先コード = 取引先[0]
      const 取引先コード枝番 = 取引先[1] // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
      const data = {
        DisplayTab: this.activeName,
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        修繕区分小: this.syuzenForm.修繕区分小,
        否認_差戻事由: this.否認_差戻事由,
        検収回数: 検収回数,
        lstサブNo_回数: this.検収回数value.map(x => `'${x.サブNo_回数}'`).join(','),
        取引先コード,
        取引先コード枝番, // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
        状態: this.sagyoForm.状態
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/差戻ボタン', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
          resData = response.data
          this.loading = false
        })
      if (result) {
        let msgM040004 = (await this.getMsg('M040004')).data
        let msgM040014 = (await this.getMsg('M040014')).data
        if (await this.showSuccess(msgM040004 + this.syuzenForm.工事依頼No + msgM040014)) {
          await this.call_sendMail('差戻', resData.f001, resData.状態, resData.lstF042, resData.lstF004, 取引先コード, 取引先コード枝番)// ハノイ側修正2022/10/26　STEP2_W　課題管理表№190：設計書「修正履歴」シートの「2022/10/21仕様変更分」「◆メール送信について追加」
          await this.onSearch()
        }
      } else {
        this.showError(msgErr)
        return false
      }
    },
    async onDenial () {
      this.loading = true
      let result = ''
      let msgErr = ''
      if (this.否認_差戻事由 === '' || this.否認_差戻事由 === null) {
        this.getMsg('E040357').then((response) => {
          this.showError(response.data)
        })
        this.$refs.ref否認_差戻事由.focus()
        return false
      }

      if (this.activeName === 'gaiyoSho' || this.activeName === 'mitsu') {
        let msgConfirm = (await this.getMsg('K040014')).data
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }
      if (this.activeName === 'hatchu') {
        let msgConfirm = (await this.getMsg('K040027')).data
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }
      if (this.activeName === 'sagyo') {
        // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「工事完了確認否認メッセージチェック」START
        if (this.sagyoForm.状態 === '工事完了確認待ち') {
          let msgConfirm = (await this.getMsg('K040084')).data
          if (!await this.confirm(msgConfirm)) {
            this.loading = false
            return false
          }
        }
        // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「工事完了確認否認メッセージチェック」END
        else if (this.sagyoForm.状態 === '支払承認待ち') {
          let msgConfirm = (await this.getMsg('K040066')).data
          if (!await this.confirm(msgConfirm)) {
            this.loading = false
            return false
          }
        }
      }
      if (!await this.regisValidate(this.activeName)) {
        return false
      }
      const 検収回数 = this.sagyo検収回数
      const 取引先 = this.sagyo取引先.split('_')
      const 取引先コード = 取引先[0]
      const 取引先コード枝番 = 取引先[1] // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
      const data = {
        DisplayTab: this.activeName,
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        修繕区分小: this.syuzenForm.修繕区分小,
        否認_差戻事由: this.否認_差戻事由,
        中止保留事由: this.iraiForm.中止保留事由,
        検収回数: 検収回数,
        取引先コード,
        取引先コード枝番, // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
        状態: this.sagyoForm.状態
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/否認ボタン', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
          this.loading = false
        })
      if (result) {
        let msgM040004 = (await this.getMsg('M040004')).data
        let msgM040013 = (await this.getMsg('M040013')).data
        if (await this.showSuccess(msgM040004 + this.syuzenForm.工事依頼No + msgM040013)) {
          await this.onSearch()
        }
      } else {
        this.showError(msgErr)
      }
    },
    async onApproval (e) {
      this.loading = true
      let result = ''
      let msgErr = ''
      let resData = {}
      let msgM04001021 = ''

      if (!await this.fnc_承認ルートチェック('btn承認')) {
        this.getMsg('E040198').then((response) => {
          this.showError(response.data)
        })
        e.target.parentNode
        return false
      }
      if (this.activeName === 'gaiyoSho' || this.activeName === 'mitsu') {
        let msgConfirm = (await this.getMsg('K040015')).data
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }
      if (this.activeName === 'hatchu') {
        let msgConfirm = (await this.getMsg('K040028')).data
        if (!await this.confirm(msgConfirm)) {
          this.loading = false
          return false
        }
      }
      if (this.activeName === 'sagyo') {
        if (!await this.updateValidate('sagyo')) {
          return false
        }
        if (this.sagyoForm.状態 === '工事完了確認待ち') {
          let msgConfirm = (await this.getMsg('K040038')).data
          if (!await this.confirm(msgConfirm)) {
            this.loading = false
            return false
          }
        } else if (this.sagyoForm.状態 === '支払承認待ち') {
          let msgConfirm = (await this.getMsg('K040065')).data
          if (!await this.confirm(msgConfirm)) {
            this.loading = false
            return false
          }
        }
      }
      if (!await this.regisValidate(this.activeName)) {
        return false
      }
      const 検収回数 = this.sagyo検収回数
      const 取引先 = this.sagyo取引先.split('_')
      const 取引先コード = 取引先[0]
      const 取引先コード枝番 = 取引先[1] // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
      const data = {
        DisplayTab: this.activeName,
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        修繕区分小: this.syuzenForm.修繕区分小,
        否認_差戻事由: this.否認_差戻事由,
        検収回数: 検収回数,
        原価金額_税抜: this.mitsuForm.金額_税抜_合計,
        表示金額: this.hatchuForm.金額_税抜_合計, // ハノイ側修正2022/10/19　STEP2_W　課題管理表№179：設計書「修正履歴」シートの「2022/10/18仕様変更分」「発注の申請データ作成時に表示金額としてセットされる値がおかしい。」
        税込支払金額: this.sagyoForm.税込支払金額,
        計上部門コード: this.activeName === 'sagyo' ? this.sagyoForm.費用負担部門コード : this.hatchuForm.費用負担部門コード,
        事業領域: this.activeName === 'sagyo' ? this.sagyoForm.事業領域 : this.hatchuForm.事業領域コード,
        外内販: this.activeName === 'sagyo' ? this.sagyoForm.外内販 : this.hatchuForm.外内販コード,
        lstサブNo_回数: this.検収回数value.map(x => `'${x.サブNo_回数}'`).join(','),
        最終承認フラグ: this.fw042_最終承認フラグ,
        取引先コード,
        取引先コード枝番, // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
        F005_発注支払マスタ: this.getDataF005(),
        チェック1: this.sagyoForm.チェック1,
        チェック2: this.sagyoForm.チェック2,
        // 取引先コード枝番 // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
        sgw費用按分FLG: this.syuzenForm.sgw費用按分FLG,
        状態: this.sagyoForm.状態
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/承認ボタン', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
          resData = response.data
          this.loading = false
        })
      if (result) {
        let msgM040004 = (await this.getMsg('M040004')).data
        if (this.activeName === 'sagyo') {
          if (this.sagyoForm.状態 === '工事完了確認待ち') {
            msgM04001021 = (await this.getMsg('M040021')).data
          } else if (this.sagyoForm.状態 === '支払承認待ち') {
            msgM04001021 = (await this.getMsg('M040010')).data
          }
        } else {
          msgM04001021 = (await this.getMsg('M040010')).data
        }
        if (await this.showSuccess(msgM040004 + this.syuzenForm.工事依頼No + msgM04001021)) {
          if (this.activeName === 'hatchu') {
            if (this.fw042_最終承認フラグ === 1) {
              this.ExportReport発注書() // ハノイ側修正2022/12/29　STEP2_W　課題管理表№261：設計書「修正履歴」シートの「2022/12/27仕様変更分」「発注申請タブ：承認ボタン押下時の発注書自動作成処理は、サーバ側での独立処理とし、承認処理とは別で処理されるようにする。」
            }
          }
          this.call_sendMail('承認', resData.f001, resData.状態, resData.lstF042, resData.lstF004, 取引先コード, 取引先コード枝番, resData.lstFT040) // ハノイ側修正2022/10/26　STEP2_W　課題管理表№190：設計書「修正履歴」シートの「2022/10/21仕様変更分」「◆メール送信について追加」
          this.onSearch()
        }
      } else {
        this.showError(msgErr)
        return false
      }
    },
    async call_sendMail (btnName, f001, 状態, lstF042, listF004, 取引先コード, 取引先コード枝番, lstFT040) {
      if (listF004) {
        listF004 = listF004.reduce((prev, el) => {
          if (prev.some(o => o.取引先コード == el.取引先コード && o.取引先コード枝番 == el.取引先コード枝番)) { return prev }
          return [...prev, {取引先コード: el.取引先コード, 取引先コード枝番: el.取引先コード枝番}]
        }, [])
      }
      if (!listF004) {
        listF004 = [{}]
      }
      let res
      // ハノイ側修正2023/02/15　STEP2_W　課題管理表№286：設計書「修正履歴」シートの「2023/02/15仕様変更分」「メール送信機能呼び出し時の、引数「本文印字用業者コード」と引数「本文印字用業者コード枝番」の設定を追加。」Start
      // 13
      if (btnName == '差戻' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      (f001.修繕区分小 == '01' || f001.修繕区分小 == '04') && (f001.負担区分 == '1' || f001.負担区分 == '3')) {
        res = await this.sendMail(13, f001.工事依頼No, f001.工事依頼No枝番, 取引先コード, 取引先コード枝番, null, '', '', '')
        return res
        // 14
      } else if (btnName == '承認' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      (f001.修繕区分小 == '01' || f001.修繕区分小 == '04') && (f001.負担区分 == '1' || f001.負担区分 == '3') && this.fw042_最終承認フラグ == '1') {
        res = await this.sendMail(14, f001.工事依頼No, f001.工事依頼No枝番, '', '', f001.工事監理者 || f001.受付担当者, 取引先コード, 取引先コード枝番, '')
        return res
        // 22
      } else if (btnName == '差戻' && (this.activeName == 'mitsu' || this.activeName == 'gaiyoSho') &&
        (f001.修繕区分小 == '01' || f001.修繕区分小 == '04') && f001.負担区分 == '2') {
        const 申請者 = lstF042 ? (lstF042.find(x => x.種類 == 20) || {}).申請者 : ''
        res = await this.sendMail(22, f001.工事依頼No, f001.工事依頼No枝番, '', '', 申請者, '', '', '')
        return res
        // 23
      } else if (btnName == '差戻' && this.activeName == 'hatchu' &&
        (f001.修繕区分小 == '01' || f001.修繕区分小 == '04') && f001.負担区分 == '2') {
        const 申請者 = lstF042 ? (lstF042.find(x => x.種類 == 20) || {}).申請者 : ''
        res = await this.sendMail(23, f001.工事依頼No, f001.工事依頼No枝番, '', '', 申請者, '', '', '')
        return res
        // 24
      } else if (btnName == '承認' && this.activeName == 'hatchu' &&
        (f001.修繕区分小 == '01' || f001.修繕区分小 == '04') && f001.負担区分 == '2' && this.fw042_最終承認フラグ == '1') {
        for (let i = 0; i < listF004.length; i++) {
          // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」Start
          let lstFT040Filter = lstFT040 ? lstFT040.filter(
            x => x.工事依頼No == f001.工事依頼No && x.工事依頼No枝番 == f001.工事依頼No枝番 &&
              x.業者コード == listF004[i].取引先コード && x.業者コード枝番 == listF004[i].取引先コード枝番) : []
          if (!lstFT040Filter) {
            lstFT040Filter = []
          }
          let 工事件名 = lstFT040Filter[0] && lstFT040Filter[0].工事件名 != '' ? lstFT040Filter[0].工事件名 : f001.工事件名
          res = await this.sendMail(24, f001.工事依頼No, f001.工事依頼No枝番, listF004[i].取引先コード, listF004[i].取引先コード枝番, null, '', '', 工事件名)
          // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」End
        }
        return res
        // 27
      } else if (btnName == '差戻' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      (f001.修繕区分小 == '01' || f001.修繕区分小 == '04') && f001.負担区分 == '2') {
        res = await this.sendMail(27, f001.工事依頼No, f001.工事依頼No枝番, 取引先コード, 取引先コード枝番, null, '', '', '')
        return res
        // 28
      } else if (btnName == '承認' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      (f001.修繕区分小 == '01' || f001.修繕区分小 == '04') && f001.負担区分 == '2' && this.fw042_最終承認フラグ == '1') {
        res = await this.sendMail(28, f001.工事依頼No, f001.工事依頼No枝番, '', '', f001.工事監理者 || f001.受付担当者, 取引先コード, 取引先コード枝番, '')
        return res
      // 47
      } else if (btnName == '差戻' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '02' && (f001.負担区分 == '1' || f001.負担区分 == '3')) {
        res = await this.sendMail(47, f001.工事依頼No, f001.工事依頼No枝番, 取引先コード, 取引先コード枝番, null, '', '', '')
        return res
        // 48
      } else if (btnName == '承認' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '02' && (f001.負担区分 == '1' || f001.負担区分 == '3') && this.fw042_最終承認フラグ == '1') {
        res = await this.sendMail(48, f001.工事依頼No, f001.工事依頼No枝番, '', '', f001.工事監理者 || f001.受付担当者, 取引先コード, 取引先コード枝番, '')
        return res
        // 54
      } else if (btnName == '承認' && this.activeName == 'hatchu' &&
        f001.修繕区分小 == '02' && f001.負担区分 == '2' && this.fw042_最終承認フラグ == '1') {
        for (let i = 0; i < listF004.length; i++) {
          // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」Start
          let lstFT040Filter = lstFT040 ? lstFT040.filter(
            x => x.工事依頼No == f001.工事依頼No && x.工事依頼No枝番 == f001.工事依頼No枝番 &&
              x.業者コード == listF004[i].取引先コード && x.業者コード枝番 == listF004[i].取引先コード枝番) : []
          if (!lstFT040Filter) {
            lstFT040Filter = []
          }
          let 工事件名 = lstFT040Filter[0] && lstFT040Filter[0].工事件名 != '' ? lstFT040Filter[0].工事件名 : f001.工事件名
          res = await this.sendMail(54, f001.工事依頼No, f001.工事依頼No枝番, listF004[i].取引先コード, listF004[i].取引先コード枝番, null, '', '', 工事件名)
          // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」End
        }
        return res
        // 56
      } else if (btnName == '差戻' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '02' && f001.負担区分 == '2') {
        res = await this.sendMail(56, f001.工事依頼No, f001.工事依頼No枝番, 取引先コード, 取引先コード枝番, null, '', '', '')
        return res
        // 57
      } else if (btnName == '承認' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '02' && f001.負担区分 == '2' && this.fw042_最終承認フラグ == '1') {
        res = await this.sendMail(57, f001.工事依頼No, f001.工事依頼No枝番, '', '', f001.工事監理者 || f001.受付担当者, 取引先コード, 取引先コード枝番)
        return res
        // 69
      } else if (btnName == '差戻' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '03' && (f001.負担区分 == '1' || f001.負担区分 == '3')) {
        res = await this.sendMail(69, f001.工事依頼No, f001.工事依頼No枝番, 取引先コード, 取引先コード枝番, null, '', '', '')
        return res
        // 70
      } else if (btnName == '承認' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '03' && (f001.負担区分 == '1' || f001.負担区分 == '3') && this.fw042_最終承認フラグ == '1') {
        res = await this.sendMail(70, f001.工事依頼No, f001.工事依頼No枝番, '', '', f001.工事監理者 || f001.受付担当者, 取引先コード, 取引先コード枝番, '')
        return res
        // 75
      } else if (btnName == '承認' && this.activeName == 'hatchu' &&
        f001.修繕区分小 == '03' && f001.負担区分 == '2' && this.fw042_最終承認フラグ == '1') {
        for (let i = 0; i < listF004.length; i++) {
          // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」Start
          let lstFT040Filter = lstFT040 ? lstFT040.filter(
            x => x.工事依頼No == f001.工事依頼No && x.工事依頼No枝番 == f001.工事依頼No枝番 &&
              x.業者コード == listF004[i].取引先コード && x.業者コード枝番 == listF004[i].取引先コード枝番) : []
          if (!lstFT040Filter) {
            lstFT040Filter = []
          }
          let 工事件名 = lstFT040Filter[0] && lstFT040Filter[0].工事件名 != '' ? lstFT040Filter[0].工事件名 : f001.工事件名
          res = await this.sendMail(75, f001.工事依頼No, f001.工事依頼No枝番, listF004[i].取引先コード, listF004[i].取引先コード枝番, null, '', '', 工事件名)
          // ハノイ側修正2023/02/17　STEP2_W　課題管理表№275：設計書「275_230215追加」End
        }
        return res
        // 77
      } else if (btnName == '差戻' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '03' && f001.負担区分 == '2') {
        res = await this.sendMail(77, f001.工事依頼No, f001.工事依頼No枝番, 取引先コード, 取引先コード枝番, null, '', '', '')
        return res
        // 78
      } else if (btnName == '承認' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '03' && f001.負担区分 == '2' && this.fw042_最終承認フラグ == '1') {
        res = await this.sendMail(78, f001.工事依頼No, f001.工事依頼No枝番, '', '', f001.工事監理者 || f001.受付担当者, 取引先コード, 取引先コード枝番, '')
        return res
        // 89
      } else if (btnName == '差戻' && (this.activeName == 'mitsu' || this.activeName == 'gaiyoSho') &&
        f001.修繕区分小 == '05' && f001.負担区分 == '2') {
        const 申請者 = lstF042 ? (lstF042.find(x => x.種類 == 30) || {}).申請者 : ''
        res = await this.sendMail(89, f001.工事依頼No, f001.工事依頼No枝番, '', '', 申請者, '', '', '')
        return res
        // 90
      } else if (btnName == '差戻' && this.activeName == 'hatchu' &&
        f001.修繕区分小 == '05' && f001.負担区分 == '2') {
        const 申請者 = lstF042 ? (lstF042.find(x => x.種類 == 30) || {}).申請者 : ''
        res = await this.sendMail(90, f001.工事依頼No, f001.工事依頼No枝番, '', '', 申請者, '', '', '')
        return res
        // 94
      } else if (btnName == '差戻' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '05' && f001.負担区分 == '2') {
        res = await this.sendMail(94, f001.工事依頼No, f001.工事依頼No枝番, 取引先コード, 取引先コード枝番, null, '', '', '')
        return res
        // 95
      } else if (btnName == '承認' && this.activeName == 'sagyo' && 状態 == '工事完了確認待ち' &&
      f001.修繕区分小 == '05' && f001.負担区分 == '2' && this.fw042_最終承認フラグ == '1') {
        res = await this.sendMail(95, f001.工事依頼No, f001.工事依頼No枝番, '', '', f001.工事監理者 || f001.受付担当者, 取引先コード, 取引先コード枝番, '')
        return res
      }
      // ハノイ側修正2023/02/15　STEP2_W　課題管理表№286：設計書「修正履歴」シートの「2023/02/15仕様変更分」「メール送信機能呼び出し時の、引数「本文印字用業者コード」と引数「本文印字用業者コード枝番」の設定を追加。」End
    },
    async sendMail (mailPatternNo, 工事依頼No, 工事依頼No枝番, 業者コード, 業者コード枝番, 担当者コード, 本文印字用業者コード, 本文印字用業者コード枝番, 工事件名) { // ハノイ側修正2023/02/15　STEP2_W　課題管理表№286：設計書「修正履歴」シートの「2023/02/15仕様変更分」「メール送信機能呼び出し時の、引数「本文印字用業者コード」と引数「本文印字用業者コード枝番」の設定を追加。」
      const url = []
      if ([13, 23, 24, 27, 47, 54, 56, 69, 75, 77, 90, 94].includes(mailPatternNo)) {
        let Reafs_W_BaseURL = ''
        let Reafs_T_BaseURL = ''
        let linkUrl = ''
        let paramSet = {}
        if (mailPatternNo == 13 || mailPatternNo == 27 || mailPatternNo == 47 || mailPatternNo == 56 ||
            mailPatternNo == 69 || mailPatternNo == 77 || mailPatternNo == 94) {
          Reafs_T_BaseURL = this.commonFunctionUI.getBaseURL('T')
          linkUrl = Reafs_T_BaseURL + 'SagyoHokokuNyuryoku'
          paramSet = {
            パラメータ1: 工事依頼No,
            パラメータ2: 工事依頼No枝番,
            パラメータ3: 業者コード,
            パラメータ4: 業者コード枝番,
            パラメータ5: this.sagyoForm.発注サブNo_F052,
            パラメータ6: this.sagyo検収回数,
            PG: 'WD00520'
          }
        } else if (mailPatternNo == 23 || mailPatternNo == 90) {
          Reafs_W_BaseURL = this.commonFunctionUI.getBaseURL('W')
          linkUrl = Reafs_W_BaseURL + 'WD00520/SyuzenIraiSansyoShonin'
          paramSet = {
            パラメータ1: 工事依頼No,
            パラメータ2: '',
            パラメータ3: '',
            パラメータ4: 'hatchu',
            PG: 'WD00520'
          }
        } else if (mailPatternNo == 24 || mailPatternNo == 54 || mailPatternNo == 75) {
          Reafs_T_BaseURL = this.commonFunctionUI.getBaseURL('T')
          linkUrl = Reafs_T_BaseURL + 'KojiIchiran'
          paramSet = {
            パラメータ1: '5',
            パラメータ2: 工事件名,
            PG: 'WD00520'
          }
        }

        const resParam = await this.commonFunctionUI.setQueryParameter(paramSet)
        linkUrl = linkUrl + '?guid=' + resParam.data.guid
        linkUrl = encodeURI(linkUrl)
        url.push(linkUrl)
      }
      const param = {
        msg_type: 0,
        mail_to: '',
        msg_head: '',
        msg_body: '',
        pg_id: 'WD00520',
        st_kbn: 1,
        mail_pattern_no: mailPatternNo,
        工事依頼NO: 工事依頼No,
        工事依頼NO枝番: 工事依頼No枝番,
        宛先事務所コード: [],
        宛先営業所コード: [],
        宛先部コード: [],
        宛先課コード: [],
        宛先係コード: [],
        業者コード,
        業者コード枝番,
        担当者コード: 担当者コード ? [担当者コード] : [],
        // ハノイ側修正2023/02/15　STEP2_W　課題管理表№286：設計書「修正履歴」シートの「2023/02/15仕様変更分」「メール送信機能呼び出し時の、引数「本文印字用業者コード」と引数「本文印字用業者コード枝番」の設定を追加。」Start
        本文印字用業者コード: 本文印字用業者コード,
        本文印字用業者コード枝番: 本文印字用業者コード枝番,
        // ハノイ側修正2023/02/15　STEP2_W　課題管理表№286：設計書「修正履歴」シートの「2023/02/15仕様変更分」「メール送信機能呼び出し時の、引数「本文印字用業者コード」と引数「本文印字用業者コード枝番」の設定を追加。」End
        url: url
      }
      const res = await this.commonFunctionUI.CM00110SendMail(param)
      return res
    },
    async onHatchuShoUkesho () {
      // WP00020 発注書・請書
      this.paramsHatchuShoUkesho.工事依頼No = this.syuzenForm.工事依頼No
      this.paramsHatchuShoUkesho.工事依頼No枝番 = this.syuzenForm.工事依頼No枝番
      this.paramsHatchuShoUkesho.取引先コード = this.取引先コード
      this.paramsHatchuShoUkesho.取引先コード枝番 = this.取引先コード枝番
      this.paramsHatchuShoUkesho.取引先名 = this.取引先名
      this.showDialogHatchuShoUkesho = true
    },
    onAnbunNyuryoku (row, rowIndex, tab) {
      let 検収回数 = 0
      let 入力区分 = ''
      let 発注サブNO = 0
      this.KeyDataToHiyoAnbunShitei.会社コード = this.searchData.会社コード
      this.KeyDataToHiyoAnbunShitei.工事依頼No = this.syuzenForm.工事依頼No
      this.KeyDataToHiyoAnbunShitei.工事依頼No枝番 = this.syuzenForm.工事依頼No枝番
      this.KeyDataToHiyoAnbunShitei.修繕区分小 = this.syuzenForm.修繕区分小
      if (tab === 'hatchu') {
        this.KeyDataToHiyoAnbunShitei.取引先コード = row.業者コード + '-' + row.業者コード枝番
        this.KeyDataToHiyoAnbunShitei.取引先名 = row.取引先名
        this.KeyDataToHiyoAnbunShitei.業者コード = row.業者コード
        this.KeyDataToHiyoAnbunShitei.業者コード枝番 = row.業者コード枝番
        this.KeyDataToHiyoAnbunShitei.税率 = row.税率
        this.KeyDataToHiyoAnbunShitei.率別売上金額税抜 = row.率別売上金額税抜
        this.KeyDataToHiyoAnbunShitei.率別売上金額消費税 = row.率別売上金額消費税
        this.KeyDataToHiyoAnbunShitei.率別売上金額税込 = row.率別売上金額税込
        this.KeyDataToHiyoAnbunShitei.費用負担部門コード = this.hatchuForm.費用負担部門コード
        this.KeyDataToHiyoAnbunShitei.未入力mode = this.hatchuForm.dt税率別金額合計欄[rowIndex].按分入力済メッセージ
        this.KeyDataToHiyoAnbunShitei.発注サブNO = 発注サブNO
      } else if (tab === 'sagyo') {
        if (this.syuzenForm.修繕区分小 === '05') {
          this.KeyDataToHiyoAnbunShitei.取引先コード = this.sagyoForm.no32Data.業者コード + '-' + this.sagyoForm.no32Data.業者コード枝番
          this.KeyDataToHiyoAnbunShitei.取引先名 = this.sagyoForm.no32Data.業者名
          this.KeyDataToHiyoAnbunShitei.業者コード = this.sagyoForm.no32Data.業者コード
          this.KeyDataToHiyoAnbunShitei.業者コード枝番 = this.sagyoForm.no32Data.業者コード枝番
        } else {
          this.KeyDataToHiyoAnbunShitei.取引先コード = this.sagyo取引先.split('_')[0] + '-' + this.sagyo取引先.split('_')[1]
          let 取引先名 = ''
          let itemSelect = $select取引先value.取引先value.find((item) => {
            return item.取引先 === this.sagyo取引先
          })
          if (itemSelect) {
            取引先名 = itemSelect.取引先名
          }
          this.KeyDataToHiyoAnbunShitei.取引先名 = 取引先名
          this.KeyDataToHiyoAnbunShitei.業者コード = this.sagyo取引先.split('_')[0]
          this.KeyDataToHiyoAnbunShitei.業者コード枝番 = this.sagyo取引先.split('_')[1]
        }
        this.KeyDataToHiyoAnbunShitei.税率 = row.税率
        this.KeyDataToHiyoAnbunShitei.率別売上金額税抜 = row.税抜発注金額_税抜支払金額
        this.KeyDataToHiyoAnbunShitei.率別売上金額消費税 = row.消費税発注金額_消費税支払金額
        this.KeyDataToHiyoAnbunShitei.率別売上金額税込 = row.税込発注金額_税込支払金額
        this.KeyDataToHiyoAnbunShitei.費用負担部門コード = this.sagyoForm.費用負担部門コード
        this.KeyDataToHiyoAnbunShitei.未入力mode = this.sagyoForm.dt税率別金額合計欄[rowIndex].按分入力済メッセージ
        if (this.syuzenForm.修繕区分小 !== '05') {
          発注サブNO = row.発注サブNo
        }
        this.KeyDataToHiyoAnbunShitei.発注サブNO = 発注サブNO
        this.KeyDataToHiyoAnbunShitei.費用按分 = this.sagyoForm.費用按分
      }

      if (this.activeName === 'hatchu') {
        検収回数 = '0'
        入力区分 = '1'
      } else if (this.activeName === 'sagyo') {
        検収回数 = this.sagyo検収回数
        入力区分 = '2'
      }

      this.KeyDataToHiyoAnbunShitei.検収回数 = 検収回数
      this.KeyDataToHiyoAnbunShitei.入力区分 = 入力区分
      this.KeyDataToHiyoAnbunShitei.tabmode = tab
      this.showHiyoAnbunShitei = true
    },
    fnc_HiyoAnbunShiteiClose (e) {
    },
    async fnc_HiyoAnbunShiteiUpdateSuccess (e) {
      if (this.activeName === 'hatchu') {
        await this.getDataAnbunNyuryokuSumiMesseji('hatchu')
        this.hatchuForm.dt税率別金額合計欄.map((item, index) => {
          if (this.hatchuForm.dt按分入力済メッセージ[index].rowEmpty === '1') {
            if (this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額 === '') {
              item['按分入力済メッセージ'] = '未入力'
            } else if (this.getNumber(this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額) === this.getNumber(item['率別売上金額税込'])) {
              item['按分入力済メッセージ'] = '入力済'
            } else {
              item['按分入力済メッセージ'] = '出来高入力済'
            }
          } else {
            item['按分入力済メッセージ'] = ''
          }
        })
        let isExist未入力 = true
        for (let i = 0; i < this.hatchuForm.dt税率別金額合計欄.length; i++) {
          if (this.hatchuForm.dt税率別金額合計欄[i].按分入力済メッセージ === '未入力') {
            isExist未入力 = false
            break
          }
        }
        if (!isExist未入力) {
          this.hatchuForm.msg費用按分 = '部門按分未入力があります。'
        } else {
          this.hatchuForm.msg費用按分 = ''
        }
      } else if (this.activeName === 'sagyo') {
        await this.getVersion('sagyo')
        await this.getDataAnbunNyuryokuSumiMesseji('sagyo')
        this.sagyoForm.dt税率別金額合計欄.map((item, index) => {
          if (this.sagyoForm.dt按分入力済メッセージ[index].rowEmpty === '1') {
            if (this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額 === '' ||
            this.getNumber(this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額) !== this.getNumber(item['税込発注金額_税込支払金額'])) { // ハノイ側修正2023/01/16　STEP2_W　課題管理表№271：設計書「修正履歴」シートの「2022/01/11仕様変更分」「按分入力ボタン押下条件追記	」
              item['按分入力済メッセージ'] = '未入力'
            } else if (this.getNumber(this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額) === this.getNumber(item['税込発注金額_税込支払金額'])) {
              item['按分入力済メッセージ'] = '入力済'
            } else {
              // item['按分入力済メッセージ'] = '出来高入力済'// ハノイ側修正2023/01/16　STEP2_W　課題管理表№271：設計書「修正履歴」シートの「2022/01/11仕様変更分」「按分入力ボタン押下条件追記	」
            }
          } else {
            item['按分入力済メッセージ'] = ''
          }
        })

        let isExist未入力 = true
        for (let i = 0; i < this.sagyoForm.dt税率別金額合計欄.length; i++) {
          if (this.sagyoForm.dt税率別金額合計欄[i].按分入力済メッセージ === '未入力') {
            isExist未入力 = false
            break
          }
        }
        if (!isExist未入力) {
          this.sagyoForm.msg費用按分 = '部門按分未入力があります。'
        } else {
          this.sagyoForm.msg費用按分 = ''
        }
      }
    },
    async getVersion (tabName) {
      let 種類 = ''
      let 検収回数 = ''
      const para検収回数 = this.sagyo検収回数
      switch (tabName) {
        case 'gaiyoSho':
          種類 = '32'
          break
        case 'mitsu':
          種類 = '32'
          break
        case 'hatchu':
          種類 = '41'
          break
        case 'sagyo':
          if (this.sagyoForm.状態 === '支払承認待ち') {
            種類 = '61'
          }
          if (this.sagyoForm.状態 === '工事完了確認待ち') {
            種類 = '52'
          }
          検収回数 = para検収回数
          break
      }
      this.http
        .get('/api/Reafs_W/Shonin/WD00520/fnc_getVersion',
          { 会社コード: this.searchData.会社コード, 依頼No: this.syuzenForm.工事依頼No, 種類, 検収回数 })
        .then((response) => {
          if (response.data !== null) {
            this.f001_version = response.data.f001_version
          }
        })
    },
    async onBack () {
      this.$store.commit('setKeepAliveFlag', 0)
      this.$store.commit('setRouterBackFlag', 1)

      if (this.activeName === 'hatchu') {
        if (this.checkChangeTabHatchu() || this.checkChangeFooter()) {
          if (await this.confirm((await this.getMsg('K000001')).data)) {
            this.$router.back()
          }
        } else {
          this.$router.back()
        }
      } else if (this.activeName === 'sagyo') {
        if (this.checkChangeTabSagyo() || this.checkChangeFooter()) {
          if (await this.confirm((await this.getMsg('K000001')).data)) {
            this.$router.back()
          }
        } else {
          this.$router.back()
        }
      } else if (this.activeName === 'mitsu') {
        this.$router.back()
      } else if (this.checkChangeFooter()) {
        if (await this.confirm((await this.getMsg('K000001')).data)) {
          this.$router.back()
        }
      } else {
        this.$router.back()
      }
    },
    checkChangeTabHatchu () {
      let isChange = false
      if (!this.syuzenForm.工事依頼No) {
        if (this.hatchuFormInit.ｸﾞﾙｰﾌﾟ稟議起案番号 !== this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 ||
            this.hatchuFormInit.ｸﾞﾙｰﾌﾟ稟議可決番号 !== this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 ||
            this.hatchuFormInit.費用負担部門コード !== this.hatchuForm.費用負担部門コード ||
            this.hatchuFormInit.事業領域コード !== this.hatchuForm.事業領域コード ||
            this.hatchuFormInit.外内販コード !== this.hatchuForm.外内販コード ||
            this.hatchuFormInit.依頼営業店稟議申請許可 !== this.hatchuForm.依頼営業店稟議申請許可 ||
            this.hatchuFormInit.発注書_請書 !== this.hatchuForm.発注書_請書) {
          isChange = true
        }
      } else {
        if ((this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1) !== this.syuzenForm.工事依頼No) {
          isChange = true
        } else {
          if (this.hatchuFormInit.ｸﾞﾙｰﾌﾟ稟議起案番号 !== this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 ||
            this.hatchuFormInit.ｸﾞﾙｰﾌﾟ稟議可決番号 !== this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 ||
            this.hatchuFormInit.費用負担部門コード !== this.hatchuForm.費用負担部門コード ||
            this.hatchuFormInit.事業領域コード !== this.hatchuForm.事業領域コード ||
            this.hatchuFormInit.外内販コード !== this.hatchuForm.外内販コード ||
            this.hatchuFormInit.依頼営業店稟議申請許可 !== this.hatchuForm.依頼営業店稟議申請許可 ||
            this.hatchuFormInit.発注書_請書 !== this.hatchuForm.発注書_請書) {
            isChange = true
          }
        }
      }
      return isChange
    },
    checkChangeTabSagyo () {
      let isChange = false
      if (!this.syuzenForm.工事依頼No) {
        if (this.sagyo取引先 !== this.sagyoForm.sagyo取引先 ||
            this.sagyo検収回数 !== this.sagyoForm.sagyo検収回数 ||
            this.sagyoFormInit.チェック1 !== this.sagyoForm.チェック1 ||
            this.sagyoFormInit.チェック2 !== this.sagyoForm.チェック2) {
          isChange = true
        }
      } else {
        if ((this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1) !== this.syuzenForm.工事依頼No) {
          isChange = true
        } else {
          if (this.sagyo取引先 !== this.sagyoForm.sagyo取引先 ||
            this.sagyo検収回数 !== this.sagyoForm.sagyo検収回数 ||
            this.sagyoFormInit.チェック1 !== this.sagyoForm.チェック1 ||
            this.sagyoFormInit.チェック2 !== this.sagyoForm.チェック2) {
            isChange = true
          }
        }
      }
      return isChange
    },
    checkChangeFooter () {
      let isChange = false
      if (!this.syuzenForm.工事依頼No) {
        if (this.activeName === 'mitsu') {
          if (this.否認_差戻事由 !== this.mitsuForm.否認_差戻事由) {
            isChange = true
          }
        } else if (this.activeName === 'gaiyoSho') {
          if (
            this.否認_差戻事由 !== this.gaiyoShoForm.否認_差戻事由) {
            isChange = true
          }
        } else if (this.activeName === 'hatchu') {
          if (this.否認_差戻事由 !== this.hatchuForm.否認_差戻事由) {
            isChange = true
          }
        } else if (this.activeName === 'sagyo') {
          if (this.否認_差戻事由 !== this.sagyoForm.否認_差戻事由) {
            isChange = true
          }
        }
      } else {
        if ((this.searchForm.koujiiraiNo + this.searchForm.koujiiraiNo1) !== this.syuzenForm.工事依頼No) {
          isChange = true
        } else {
          if (this.activeName === 'mitsu') {
            if (this.否認_差戻事由 !== this.mitsuForm.否認_差戻事由) {
              isChange = true
            }
          } else if (this.activeName === 'gaiyoSho') {
            if (
              this.否認_差戻事由 !== this.gaiyoShoForm.否認_差戻事由) {
              isChange = true
            }
          } else if (this.activeName === 'hatchu') {
            if (this.否認_差戻事由 !== this.hatchuForm.否認_差戻事由) {
              isChange = true
            }
          } else if (this.activeName === 'sagyo') {
            if (this.否認_差戻事由 !== this.sagyoForm.否認_差戻事由) {
              isChange = true
            }
          }
        }
      }
      return isChange
    },
    async onHeaderSearch () {
      const res = await this.http.get('api/Reafs_W/Shonin/WD00520/GetDataCommon',
        {会社コード: this.searchData.会社コード, 工事依頼No: this.searchData.工事依頼No},
        'アクセスしています....')
        .then(async response => {
          if (response.status) {
            // ハノイ側修正2022/09/23　STEP2_W　課題管理表№119：設計書「修正履歴」シートの「2022/09/20仕様変更分」「参照権限チェック追加」Start
            if (!await this.check参照権限(response.data.営業店コード, response.data.営業店コード枝番, true)) {
              this.$refs.koujiiraiNo.focus()
              return false
            }
            // ハノイ側修正2022/09/23　STEP2_W　課題管理表№119：設計書「修正履歴」シートの「2022/09/20仕様変更分」「参照権限チェック追加」End
            Object.assign(this.syuzenForm, response.data)
            return true
          } else {
            this.showError(response.message)
            return false
          }
        })
      return res
    },
    async reafsmodalBack_searchHiyoFutanBumon () {
      // this.hatchuForm.費用負担部門コード = obj.txtCd
      // this.hatchuForm.費用負担部門名 = obj.lblNm
      // if (this.hatchuForm.費用負担部門コード) {
      //   this.http
      //     .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: this.hatchuForm.費用負担部門コード })
      //     .then(async (response) => {
      //       if (response.data && response.data.費用負担部門) {
      //         this.hatchuForm.費用負担部門名 = response.data.費用負担部門
      //       } else {
      //         this.hatchuForm.費用負担部門名 = ''
      //       }
      //     })
      // }
      if (this.hatchuForm.費用負担部門コード === '') {
        this.hatchuForm.費用負担部門名 = ''
      } else {
        // ハノイ側修正2022/08/30　STEP2_W　課題管理表№67：設計書「修正履歴」シートの「2022/08/24仕様変更分」「費用負担部門、事業領域、外内販の入力チェック追加」
        await this.Check費用負担部門Exist()
      }
    },
    // ハノイ側修正2022/08/30　STEP2_W　課題管理表№67：設計書「修正履歴」シートの「2022/08/24仕様変更分」「費用負担部門、事業領域、外内販の入力チェック追加」Start
    Check費用負担部門Exist () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: this.hatchuForm.費用負担部門コード })
          .then((response) => {
            if (!response.data) {
              this.getMsg('E040747').then((response) => {
                this.showError(response.data, () => { this.$refs.refHatchu費用負担部門.focus() })
              }).finally(() => {
              })
              this.hatchuForm.費用負担部門名 = ''
              return resolve(false)
            } else {
              this.hatchuForm.費用負担部門名 = response.data.費用負担部門
              return resolve(true)
            }
          })
      })
    },
    // ハノイ側修正2022/08/30　STEP2_W　課題管理表№67：設計書「修正履歴」シートの「2022/08/24仕様変更分」「費用負担部門、事業領域、外内販の入力チェック追加」End
    reafsmodalBack_searchJigyoRyoiki () {
      // this.hatchuForm.事業領域コード = obj.txtCd
      // this.hatchuForm.事業領域名 = obj.lblNm
      // if (this.hatchuForm.事業領域コード) {
      //   this.http
      //     .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No35', { 事業領域コード: this.hatchuForm.事業領域コード })
      //     .then(async (response) => {
      //       if (response.data && response.data.名称取得方法03) {
      //         this.hatchuForm.事業領域名 = response.data.名称取得方法03
      //       } else {
      //         this.hatchuForm.事業領域名 = ''
      //       }
      //     })
      // }
      if (this.hatchuForm.事業領域コード === '') {
        this.hatchuForm.事業領域名 = ''
      } else {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No35', { 事業領域コード: this.hatchuForm.事業領域コード })
          .then((response) => {
            if (response.data !== null) {
              this.hatchuForm.事業領域名 = response.data.事業領域
            } else {
              this.hatchuForm.事業領域名 = ''
            }
          })
      }
    },
    reafsmodalBack_searchSotoNai () {
      // this.hatchuForm.外内販コード = obj.txtCd
      // this.hatchuForm.外内販名 = obj.lblNm
      // if (this.hatchuForm.外内販コード) {
      //   this.http
      //     .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No36', { 外内販コード: this.hatchuForm.外内販コード })
      //     .then(async (response) => {
      //       if (response.data && response.data.名称取得方法03) {
      //         this.hatchuForm.外内販名 = response.data.名称取得方法03
      //       } else {
      //         this.hatchuForm.外内販名 = ''
      //       }
      //     })
      // }
      if (this.hatchuForm.外内販コード === '') {
        this.hatchuForm.外内販名 = ''
      } else {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No36', { 外内販コード: this.hatchuForm.外内販コード })
          .then((response) => {
            if (response.data !== null) {
              this.hatchuForm.外内販名 = response.data.外内販区分
            } else {
              this.hatchuForm.外内販名 = ''
            }
          })
      }
    },
    async reafsmodalBack_searchHiyoFutanBumonSagyoForm () {
      if (this.sagyoForm.費用負担部門コード === '') {
        this.sagyoForm.費用負担部門名 = ''
      } else {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: this.sagyoForm.費用負担部門コード })
          .then((response) => {
            if (response.data !== null) {
              this.sagyoForm.外内販名 = response.data.外内販区分
            } else {
              this.sagyoForm.外内販名 = ''
            }
          })
      }
    },
    async fnc_btn制御 () {
      let bolBtn発注書請書 = true
      let bolBtn帳票一覧 = true
      let bolBtn稟議申請 = true
      let bolBtn取下 = true
      let bolBtn差戻 = true
      let bolBtn否認 = true
      let bolBtn承認 = true

      if (this.activeName === 'irai') {
        bolBtn帳票一覧 = false
      }
      if (this.activeName === 'mitsu') {
        bolBtn帳票一覧 = false
        await this.http.get('api/Reafs_W/Shonin/WD00520/見積承認可能チェック',
          {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No},
          'アクセスしています....')
          .then((response) => {
            if (response.status) {
              let bhasData = false
              if (Object.getPrototypeOf(response.data) == Object.prototype) {
                bhasData = Object.keys(response.data).length > 0
              } else {
                bhasData = response.data.length > 0
              }
              if (bhasData === true) {
                if (response.data.見積承認可能チェック1.length > 0) {
                  bolBtn差戻 = false
                  bolBtn否認 = false
                  bolBtn承認 = false
                }
                if (response.data.見積承認可能チェック2.length > 0) {
                  if (response.data.m018_見積承認可能チェック2.length > 0) {
                    bolBtn差戻 = false
                    bolBtn否認 = false
                    bolBtn承認 = false
                  }
                }
              }
            } else {
              this.showError(response.message)
            }
          })
      }
      if (this.activeName === 'gaiyoSho') {
        bolBtn帳票一覧 = false
        await this.http.get('api/Reafs_W/Shonin/WD00520/見積承認可能チェック',
          {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No},
          'アクセスしています....')
          .then((response) => {
            let bhasData = false
            if (Object.getPrototypeOf(response.data) == Object.prototype) {
              bhasData = Object.keys(response.data).length > 0
            } else {
              bhasData = response.data.length > 0
            }
            if (bhasData === true) {
              if (response.data.見積承認可能チェック1.length > 0) {
                bolBtn差戻 = false
                bolBtn否認 = false
                bolBtn承認 = false
              }
              if (response.data.見積承認可能チェック2.length > 0) {
                if (response.data.m018_見積承認可能チェック2.length > 0) {
                  bolBtn差戻 = false
                  bolBtn否認 = false
                  bolBtn承認 = false
                }
              }
            }
          })
      }
      if (this.activeName === 'hatchu') {
        bolBtn帳票一覧 = false
        let b発注書請書Check = false
        await this.http.get('api/Reafs_W/Shonin/WD00520/発注承認済チェック',
          {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No}, 'アクセスしています....')
          .then((response) => {
            let bhasData = false
            if (Object.getPrototypeOf(response.data) == Object.prototype) {
              bhasData = Object.keys(response.data).length > 0
            } else {
              bhasData = response.data.length > 0
            }
            if (bhasData === true) {
              bolBtn発注書請書 = false
              b発注書請書Check = true
            }
          })
        if (b発注書請書Check) {
          if (this.hatchuForm.修繕区分小 === '05') {
            bolBtn発注書請書 = true
          } else {
            if (await this.checkWeb合意() === true) {
              bolBtn発注書請書 = true
            }
          }
        }
        await this.http.get('api/Reafs_W/Shonin/WD00520/発注申請チェック',
          {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No},
          'アクセスしています....')
          .then((response) => {
            let bhasData = false
            if (Object.getPrototypeOf(response.data) == Object.prototype) {
              bhasData = Object.keys(response.data).length > 0
            } else {
              bhasData = response.data.length > 0
            }
            if (bhasData == true) {
              bolBtn稟議申請 = true
            } else {
              bolBtn稟議申請 = false
            }
          })
        await this.http.get('api/Reafs_W/Shonin/WD00520/発注承認可能チェック',
          {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No, 修繕区分小: this.syuzenForm.修繕区分小},
          'アクセスしています....')
          .then((response) => {
            let bhasData = false
            if (Object.getPrototypeOf(response.data) == Object.prototype) {
              bhasData = Object.keys(response.data).length > 0
            } else {
              bhasData = response.data.length > 0
            }
            if (bhasData == true) {
              if (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 !== '' && (response.data.発注承認可能チェック1.length > 0 ||
                (response.data.発注承認可能チェック2.length > 0 && response.data.m018_発注承認可能チェック2.length > 0)) &&
                  response.data.発注承認可能チェック3.length > 0 && (this.syuzenForm.修繕区分小 === '02' || this.syuzenForm.修繕区分小 === '03')) {
                bolBtn否認 = false
                bolBtn承認 = false
              } else if (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 !== '' && (response.data.発注承認可能チェック1.length > 0 ||
                (response.data.発注承認可能チェック2.length > 0 && response.data.m018_発注承認可能チェック2.length > 0)) &&
                  response.data.発注承認可能チェック3.length > 0) {
                bolBtn否認 = false
                bolBtn承認 = false
              }
            }
          })
        await this.http.get('api/Reafs_W/Shonin/WD00520/稟議差戻チェック',
          {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No, 修繕区分小: this.syuzenForm.修繕区分小, 負担区分: this.hatchuForm.負担区分},
          'アクセスしています....')
          .then((response) => {
            let bhasData = false
            let bCondition1 = false
            let bCondition2 = false
            let bCondition3 = false
            let bCondition4 = false
            let bCondition5 = false
            if (Object.getPrototypeOf(response.data) == Object.prototype) {
              bhasData = Object.keys(response.data).length > 0
            } else {
              bhasData = response.data.length > 0
            }
            if (bhasData == true) {
              // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「「工事完了確認待ち」時の差戻ボタン押下条件追加」START
              if (response.data.稟議差戻チェック1.length > 0 || response.data.稟議差戻チェック2.length > 0) {
                bCondition1 = true
              }
              if (response.data.稟議差戻チェック3.length === 0) {
                bCondition2 = true
              }
              if (response.data.稟議差戻チェック4.length > 0) {
                bCondition3 = true
              }
              if ((this.syuzenForm.修繕区分小 === '02' || this.syuzenForm.修繕区分小 === '03') && this.hatchuForm.負担区分 === '2') {
                if (response.data.稟議差戻チェック5.length === 0) {
                  bCondition4 = true
                }
              } else {
                bCondition4 = true
              }

              if (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 === '') {
                bCondition5 = true
              }

              if ((bCondition1 || bCondition2) && bCondition3 && bCondition4 && bCondition5) {
                bolBtn差戻 = false
              }
              // ハノイ側修正2023/02/15　STEP2_W　課題管理表№285：設計書「修正履歴」シートの「2023/02/14仕様変更分」「「工事完了確認待ち」時の差戻ボタン押下条件追加」END
            }
          })
        let bol発注申請取下チェック = false
        await this.http.get('api/Reafs_W/Shonin/WD00520/発注申請取下チェック',
          {会社コード: this.searchData.会社コード, 工事依頼No: this.syuzenForm.工事依頼No},
          'アクセスしています....')
          .then((response) => {
            let bhasData = false
            if (Object.getPrototypeOf(response.data) == Object.prototype) {
              bhasData = Object.keys(response.data).length > 0
            } else {
              bhasData = response.data.length > 0
            }
            if (bhasData === true) {
              bol発注申請取下チェック = true
            }
          })
        if (this.syuzenForm.w稟議申請対象フラグ === '2' &&
            (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 == '' || this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 == null) &&
            bol発注申請取下チェック) {
          bolBtn取下 = false
        } else if (this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 !== '' && bol発注申請取下チェック) {
          bolBtn取下 = false
        }

        if (this.syuzenForm.w稟議申請対象フラグ === '2') {
          bolBtn否認 = true
        }
      }
      if (this.activeName === 'sagyo') {
        await this.支払承認可能チェック(this.sagyo検収回数)// ハノイ側修正2023/2/23　STEP2_W　課題管理表№297：設計書「修正履歴」シートの「2023/2/22仕様変更分」「作業結果／支払タブ内の費用按分、按分入力ボタン、部門確認ボタン押下条件に支払承認可能チェックを追加」
        bolBtn帳票一覧 = false
        if (this.sagyoForm.状態 === '工事完了確認待ち') {
          const 検収回数 = this.sagyo検収回数
          await this.http.get('api/Reafs_W/Shonin/WD00520/工事完了確認可能チェック',
            {会社コード: this.searchData.会社コード,
              工事依頼No: this.syuzenForm.工事依頼No,
              工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
              発注サブNO: this.sagyo発注サブNo,
              検収回数: 検収回数,
              取引先コード: this.sagyo取引先.split('_')[0], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
              取引先コード枝番: this.sagyo取引先.split('_')[1], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
              修繕区分小: this.syuzenForm.修繕区分小},
            'アクセスしています....')
            .then((response) => {
              let bhasData = false
              if (Object.getPrototypeOf(response.data) == Object.prototype) {
                bhasData = (Object.keys(response.data.工事完了確認可能チェック).length > 0 || Object.keys(response.data.工事完了確認可能チェック2).length > 0)
              } else {
                bhasData = (response.data.工事完了確認可能チェック.length > 0 || response.data.工事完了確認可能チェック2.length > 0)
              }
              if (bhasData === true) {
                bolBtn差戻 = false
                bolBtn否認 = false
                bolBtn承認 = false
              }
            })
          // ハノイ側修正2023/2/23　STEP2_W　課題管理表№297：設計書「修正履歴」シートの「2023/2/22仕様変更分」「作業結果／支払タブ内の費用按分、按分入力ボタン、部門確認ボタン押下条件に支払承認可能チェックを追加」 Start
          if (this.sagyoForm.支払承認可能チェックCheck) {
            bolBtn差戻 = true
          }
          // await this.http.get('api/Reafs_W/Shonin/WD00520/支払承認可能チェック',
          //   {会社コード: this.searchData.会社コード,
          //     工事依頼No: this.syuzenForm.工事依頼No,
          //     工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
          //     検収回数: 検収回数,
          //     取引先コード: this.sagyo取引先.split('_')[0], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
          //     取引先コード枝番: this.sagyo取引先.split('_')[1], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
          //     修繕区分小: this.syuzenForm.修繕区分小},
          //   'アクセスしています....')
          //   .then((response) => {
          //     let bhasData = false
          //     if (Object.getPrototypeOf(response.data) == Object.prototype) {
          //       bhasData = Object.keys(response.data).length > 0
          //     } else {
          //       bhasData = response.data.length > 0
          //     }
          //     if (bhasData === true) {
          //       bolBtn差戻 = true
          //     }
          //   })
          // ハノイ側修正2023/2/23　STEP2_W　課題管理表№297：設計書「修正履歴」シートの「2023/2/22仕様変更分」「作業結果／支払タブ内の費用按分、按分入力ボタン、部門確認ボタン押下条件に支払承認可能チェックを追加」 End
        }
        if (this.sagyoForm.状態 === '支払承認待ち') {
          // ハノイ側修正2023/2/23　STEP2_W　課題管理表№297：設計書「修正履歴」シートの「2023/2/22仕様変更分」「作業結果／支払タブ内の費用按分、按分入力ボタン、部門確認ボタン押下条件に支払承認可能チェックを追加」Start
          if (this.sagyoForm.支払承認可能チェックCheck) {
            bolBtn差戻 = false
            bolBtn否認 = false
            if (this.sagyoForm.部門確認 === '部門確認済') {
              bolBtn承認 = false
            }
          }
          // await this.http.get('api/Reafs_W/Shonin/WD00520/支払承認可能チェック',
          //   {会社コード: this.searchData.会社コード,
          //     工事依頼No: this.syuzenForm.工事依頼No,
          //     工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
          //     検収回数: 検収回数,
          //     取引先コード: this.sagyo取引先.split('_')[0], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
          //     取引先コード枝番: this.sagyo取引先.split('_')[1], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
          //     修繕区分小: this.syuzenForm.修繕区分小},
          //   'アクセスしています....')
          //   .then((response) => {
          //     let bhasData = false
          //     if (Object.getPrototypeOf(response.data) == Object.prototype) {
          //       bhasData = Object.keys(response.data).length > 0
          //     } else {
          //       bhasData = response.data.length > 0
          //     }
          //     if (bhasData === true) {
          //       bolBtn差戻 = false
          //       bolBtn否認 = false
          //       if (this.sagyoForm.部門確認 === '部門確認済') {
          //         bolBtn承認 = false
          //       }
          //     }
          //   })
          // ハノイ側修正2023/2/23　STEP2_W　課題管理表№297：設計書「修正履歴」シートの「2023/2/22仕様変更分」「作業結果／支払タブ内の費用按分、按分入力ボタン、部門確認ボタン押下条件に支払承認可能チェックを追加」 End
          if (this.syuzenForm.修繕区分小 !== '05' && !this.dispBtnBy請求合意区分F005) { // ハノイ側修正2023/03/07　STEP2_W　課題管理表№306：設計書「修正履歴」シートの「2023/03/07仕様変更分」「支払承認時のボタン押下不可条件に条件追加」
            bolBtn承認 = true
          }

          // ハノイ側修正2022/08/01　STEP2_W　課題管理表№376：設計書「修正履歴」シートの「2023/07/26仕様変更分」「支払承認時のボタン押下不可条件に不備のチェックを追加」Start
          if (this.syuzenForm.修繕区分小 !== '05' && !await this.fnc_不備チェック()) { // ハノイ側修正2022/08/09　STEP2_W　課題管理表№384：設計書「修正履歴」シートの「2023/08/08仕様変更分」「F001_受注マスタ.修繕区分小='05'の場合は不備チェック不要とするよう修正。」
            bolBtn承認 = true
          }
          // ハノイ側修正2022/08/01　STEP2_W　課題管理表№376：設計書「修正履歴」シートの「2023/07/26仕様変更分」「支払承認時のボタン押下不可条件に不備のチェックを追加」End
        }
      }
      this.画面入力制御disabled.btn発注書請書 = bolBtn発注書請書
      this.画面入力制御disabled.btn帳票一覧 = bolBtn帳票一覧
      this.画面入力制御disabled.btn稟議申請 = bolBtn稟議申請
      this.画面入力制御disabled.btn取下 = bolBtn取下
      this.画面入力制御disabled.btn差戻 = bolBtn差戻
      this.画面入力制御disabled.btn否認 = bolBtn否認
      this.画面入力制御disabled.btn承認 = bolBtn承認

      if (bolBtn稟議申請 === false || bolBtn取下 === false || bolBtn差戻 === false || bolBtn否認 === false || bolBtn承認 === false) {
        this.画面入力制御disabled.btn更新 = false
      } else {
        this.画面入力制御disabled.btn更新 = true
      }
    },
    async 支払承認可能チェック (検収回数) { // ハノイ側修正2023/2/23　STEP2_W　課題管理表№297：設計書「修正履歴」シートの「2023/2/22仕様変更分」「作業結果／支払タブ内の費用按分、按分入力ボタン、部門確認ボタン押下条件に支払承認可能チェックを追加」
      let bhasData = false
      await this.http.get('api/Reafs_W/Shonin/WD00520/支払承認可能チェック',
        {会社コード: this.searchData.会社コード,
          工事依頼No: this.syuzenForm.工事依頼No,
          工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
          検収回数: 検収回数,
          取引先コード: this.sagyo取引先.split('_')[0], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
          取引先コード枝番: this.sagyo取引先.split('_')[1], // ハノイ側修正2022/09/29　課題管理表№128：設計書「修正履歴」シートの「2022/09/28仕様変更分」「工事完了確認可能チェック、支払承認可能チェック条件修正」
          修繕区分小: this.syuzenForm.修繕区分小},
        'アクセスしています....')
        .then((response) => {
          if (Object.getPrototypeOf(response.data) == Object.prototype) {
            bhasData = Object.keys(response.data).length > 0
          } else {
            bhasData = response.data.length > 0
          }
        })
      this.sagyoForm.支払承認可能チェックCheck = bhasData
      return bhasData
    },
    async checkWeb合意 () {
      let checkWeb合意 = false
      await this.http
        .get('/api/Reafs_W/Shonin/WD00520/fnc_getWeb合意',
          { 会社コード: this.searchData.会社コード,
            工事依頼No: this.syuzenForm.工事依頼No,
            工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
            取引先コード: this.取引先コード,
            取引先コード枝番: this.取引先コード枝番
          })
        .then((response) => {
          if (response.data.length > 0) {
            if (response.data[0].Web合意 === '1') {
              checkWeb合意 = true
            } else {
              checkWeb合意 = false
            }
          }
        })
      return checkWeb合意
    },
    resetGaiyoShoForm () {
      this.gaiyoShoForm.工事名称 = ''
      this.gaiyoShoForm.稟議No = ''
      this.gaiyoShoForm.申請内容 = ''
      this.gaiyoShoForm.予算額 = ''
      this.gaiyoShoForm.申請額_税抜 = ''
      this.gaiyoShoForm.計画 = ''
      this.gaiyoShoForm.計画外 = ''
      this.gaiyoShoForm.計画外_賃料工事 = ''
      this.gaiyoShoForm.固定資産除却 = ''
      this.gaiyoShoForm.予算差異_残額 = ''
      this.gaiyoShoForm.予算区分 = ''
      this.gaiyoShoForm.計画No = ''
      this.gaiyoShoForm.件名 = ''
      this.gaiyoShoForm.積算期間 = ''
      this.gaiyoShoForm.計画外区分 = ''
      this.gaiyoShoForm.ＣＣ受付No = ''
      this.gaiyoShoForm.追加賃料額 = ''
      this.gaiyoShoForm.賃料発生月 = ''
      this.gaiyoShoForm.固定資産No1 = ''
      this.gaiyoShoForm.固定資産No2 = ''
      this.gaiyoShoForm.固定資産No3 = ''
      this.gaiyoShoForm.取引先_支払先 = ''
      this.gaiyoShoForm.支払条件 = ''
      this.gaiyoShoForm.工事期間 = ''
      this.gaiyoShoForm.申請者_担当者_組織名 = ''
      this.gaiyoShoForm.申請者_担当者_役職名 = ''
      this.gaiyoShoForm.申請者_担当者_名 = ''
      this.gaiyoShoForm.竣工年月日 = ''
      this.gaiyoShoForm.築年数 = ''
      this.gaiyoShoForm.残契約期間 = ''
      this.gaiyoShoForm.備考 = ''
      this.gaiyoShoFormInit = {}
      // this.否認_差戻事由 = ''
      this.gaiyoShoForm.否認_差戻事由 = ''
    },
    resetButtonState () {
      this.画面入力制御disabled = {
        btn発注書請書: true,
        btn帳票一覧: true,
        btn稟議申請: true,
        btn取下: true,
        btn差戻: true,
        btn否認: true,
        btn承認: true,
        取引先: false,
        検収回数: false,
        否認_差戻事由: false,
        チェック1: false,
        チェック2: false,
        費用負担部門: false,
        事業領域: false,
        外内販区分: false,
        btn更新: true,
        発注書_請書: false
      }
    },
    resetCommon () {
      this.sagyo取引先 = ''
      this.sagyo検収回数 = ''
      this.sagyo発注サブNo = ''
      this.dispBtnExistF005 = true
      this.endableBtnKakuninToroku = true
      this.dispBtnBy請求合意区分F005 = true
      this.否認_差戻事由 = ''
      this.画面入力制御disabled = {
        btn発注書請書: true,
        btn帳票一覧: true,
        btn稟議申請: true,
        btn取下: true,
        btn差戻: true,
        btn否認: true,
        btn承認: true,
        取引先: false,
        検収回数: false,
        否認_差戻事由: false,
        チェック1: false,
        チェック2: false,
        費用負担部門: false,
        事業領域: false,
        外内販区分: false,
        btn更新: true,
        発注書_請書: false
      }
      this.showHiyoAnbunShitei = false
      this.KeyDataToHiyoAnbunShitei = {
        会社コード: '',
        工事依頼No: '',
        工事依頼No枝番: '',
        取引先コード: '',
        取引先名: '',
        業者コード: '',
        業者コード枝番: '',
        税率: '',
        率別売上金額税抜: '',
        率別売上金額消費税: '',
        率別売上金額税込: '',
        費用負担部門コード: '',
        検収回数: 0,
        入力区分: '',
        発注サブNO: 0,
        費用按分: '',
        未入力mode: '',
        tabmode: ''
      }
      this.isDisplayHatchuAnbunNyuryoku = false
      this.isDisplaySagyoAnbunNyuryoku = false
    },
    resetSyuzenForm  () {
      this.syuzenForm = {
        営業店コード: '',
        営業店コード枝番: '',
        営業店名: '',
        進捗_本体: '',
        進捗_発注: '',
        物件コード: '',
        物件名: '',
        物件区分: '',
        物件住所Cd: '',
        物件住所Nm: '',
        工事依頼No: '',
        工事依頼No枝番: '',
        修繕区分小: '',
        w稟議申請対象フラグ: '',
        sgw費用按分FLG: ''
      }
    },
    resetIraiForm () {
      this.iraiForm.工事区分 = ''
      this.iraiForm.大区分 = ''
      this.iraiForm.中区分 = ''
      this.iraiForm.小区分 = ''
      this.iraiForm.依頼者コード = ''
      this.iraiForm.依頼者名 = ''
      this.iraiForm.依頼者所属部署名 = ''
      this.iraiForm.依頼者役職名 = ''
      this.iraiForm.依頼者連絡先 = ''
      this.iraiForm.依頼者代理コード = ''
      this.iraiForm.依頼者代理名 = ''
      this.iraiForm.依頼者代理所属部署名 = ''
      this.iraiForm.依頼者代理役職名 = ''
      this.iraiForm.依頼者代理連絡先 = ''
      this.iraiForm.依頼コメント = ''
      this.iraiForm.対象箇所 = ''
      this.iraiForm.メーカー名 = ''
      this.iraiForm.依頼内容_不具合状況 = ''
      this.iraiForm.依頼状況 = ''
      this.iraiForm.sgr担当部署 = ''
      this.iraiForm.中止保留区分 = ''
      this.iraiForm.中止保留年月日 = ''
      this.iraiForm.中止保留事由 = ''
      this.iraiForm.部位コメント = []
      this.iraiForm.部位コメント1 = ''
      this.iraiForm.部位コメント2 = ''
      this.iraiForm.部位コメント3 = ''
      this.iraiForm.部位コメント4 = ''
      this.iraiForm.否認_差戻事由 = ''
      this.iraiFormInit = {}
      // this.否認_差戻事由 = ''
    },
    resetMitsuForm () {
      this.mitsuForm.見積作成日 = ''
      this.mitsuForm.見積No = ''
      this.mitsuForm.見積No枝番 = ''
      this.mitsuForm.見積有効期限 = ''
      this.mitsuForm.見積条件 = ''
      this.hatchuForm.取引先名1 = ''
      this.hatchuForm.取引先名2 = ''
      this.hatchuForm.取引先名3 = ''
      this.hatchuForm.取引先名4 = ''
      this.mitsuForm.税率1 = ''
      this.mitsuForm.税率2 = ''
      this.mitsuForm.税率3 = ''
      this.mitsuForm.税率4 = ''
      this.mitsuForm.金額_税抜1 = ''
      this.mitsuForm.金額_税抜2 = ''
      this.mitsuForm.金額_税抜3 = ''
      this.mitsuForm.金額_税抜4 = ''
      this.mitsuForm.金額_税抜_合計 = ''
      this.mitsuForm.消費税額1 = ''
      this.mitsuForm.消費税額2 = ''
      this.mitsuForm.消費税額3 = ''
      this.mitsuForm.消費税額4 = ''
      this.mitsuForm.消費税額_合計 = ''
      this.mitsuForm.税補正1 = ''
      this.mitsuForm.税補正2 = ''
      this.mitsuForm.税補正3 = ''
      this.mitsuForm.税補正4 = ''
      this.mitsuForm.金額_税補正_合計 = ''
      this.mitsuForm.金額_税込1 = ''
      this.mitsuForm.金額_税込2 = ''
      this.mitsuForm.金額_税込3 = ''
      this.mitsuForm.金額_税込4 = ''
      this.mitsuForm.金額_税込_合計 = ''
      this.mitsuForm.dt明細部 = []
      this.mitsuFormInit = {}
      // ハノイ側修正2023/06/19　課題管理表№354：「2023/06/16依頼分」「1. タブ切り替えた時に「否認・差戻事由」内容が消えます。2.「検収回数」を変更した時に「否認・差戻事由」内容は再表示していません。」START
      // this.否認_差戻事由 = ''
      this.mitsuForm.否認_差戻事由 = ''
      // ハノイ側修正2023/06/19　課題管理表№354：「2023/06/16依頼分」「1. タブ切り替えた時に「否認・差戻事由」内容が消えます。2.「検収回数」を変更した時に「否認・差戻事由」内容は再表示していません。」END
      this.mitsuForm.修繕区分小 = ''
      this.mitsuForm.dt税率別金額合計欄 = []
    },
    resetHatchuForm () {
      this.hatchuForm.見積作成日 = ''
      this.hatchuForm.見積No = ''
      this.hatchuForm.見積No枝番 = ''
      this.hatchuForm.見積有効期限 = ''
      this.hatchuForm.見積条件 = ''
      this.hatchuForm.取引先名1 = ''
      this.hatchuForm.取引先名2 = ''
      this.hatchuForm.取引先名3 = ''
      this.hatchuForm.取引先名4 = ''
      this.hatchuForm.税率1 = ''
      this.hatchuForm.税率2 = ''
      this.hatchuForm.税率3 = ''
      this.hatchuForm.税率4 = ''
      this.hatchuForm.金額_税抜1 = ''
      this.hatchuForm.金額_税抜2 = ''
      this.hatchuForm.金額_税抜3 = ''
      this.hatchuForm.金額_税抜4 = ''
      this.hatchuForm.金額_税抜_合計 = ''
      this.hatchuForm.消費税額1 = ''
      this.hatchuForm.消費税額2 = ''
      this.hatchuForm.消費税額3 = ''
      this.hatchuForm.消費税額4 = ''
      this.hatchuForm.消費税額_合計 = ''
      this.hatchuForm.税補正1 = ''
      this.hatchuForm.税補正2 = ''
      this.hatchuForm.税補正3 = ''
      this.hatchuForm.税補正4 = ''
      this.hatchuForm.金額_税補正_合計 = ''
      this.hatchuForm.金額_税込1 = ''
      this.hatchuForm.金額_税込2 = ''
      this.hatchuForm.金額_税込3 = ''
      this.hatchuForm.金額_税込4 = ''
      this.hatchuForm.金額_税込_合計 = ''
      this.hatchuForm.dtHatchu = []
      this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議起案番号 = ''
      this.hatchuForm.稟議起案番号1 = ''
      this.hatchuForm.ｸﾞﾙｰﾌﾟ稟議可決番号 = ''
      this.hatchuForm.発注書_請書 = '  '
      this.hatchuForm.工事依頼No = ''
      this.hatchuFormInit = {}
      // this.否認_差戻事由 = ''
      this.hatchuForm.否認_差戻事由 = ''
      $select発注書_請書value.発注書_請書value = []
      this.依頼営業店稟議申請許可 = 1
      this.費用負担部門コード = ''
      this.費用負担部門名 = ''
      this.事業領域コード = ''
      this.事業領域名 = ''
      this.外内販コード = ''
      this.外内販名 = ''
      $select事業領域value.事業領域value = []
      $select外内販value.外内販value = []
      this.hatchuForm.修繕区分小 = ''
      this.hatchuForm.dt税率別金額合計欄 = []
      this.hatchuForm.msg費用按分 = ''
      this.hatchuForm.w費用負担部門按分フラグ = 0
      this.hatchuForm.dt按分入力済メッセージ = []
      this.hatchuForm.費用按分 = '0'
      this.hatchuForm.isDisabledHiyoAnbun = false
    },
    resetSagyoForm () {
      this.sagyoFormInit = {}
      this.sagyoForm.工事名称 = ''
      this.sagyoForm.作業担当者 = ''
      this.sagyoForm.担当者連絡先 = ''
      this.sagyoForm.作業完了日_FROM = ''
      this.sagyoForm.作業完了時間_FROM = ''
      this.sagyoForm.作業完了日_TO = ''
      this.sagyoForm.作業完了時間_TO = ''
      this.sagyoForm.施工内容_特記事項等 = ''
      this.sagyoForm.完了確認日 = ''
      this.sagyoForm.完了確認者 = ''
      this.sagyoForm.負担区分名称 = ''
      this.sagyoForm.見積書_不備有無欄 = ''
      this.sagyoForm.見積書_解消日 = ''
      this.sagyoForm.稟議書_不備有無欄 = ''
      this.sagyoForm.稟議書_解消日 = ''
      this.sagyoForm.完了報告_不備有無欄 = ''
      this.sagyoForm.完了報告_解消日 = ''
      this.sagyoForm.請求書_不備有無欄 = ''
      this.sagyoForm.請求書_解消日 = ''
      this.sagyoForm.チェック1 = false
      this.sagyoForm.チェック2 = false
      this.sagyoForm.作業結果No = ''
      this.sagyoForm.w作業結果確認フラグ = ''
      this.sagyoForm.sagyo取引先 = ''
      this.sagyoForm.sagyo検収回数 = ''
      this.sagyoForm.状態 = ''
      this.sagyoForm.費用負担部門コード = ''
      this.sagyoForm.費用負担部門名 = ''
      this.sagyoForm.事業領域コード = ''
      this.sagyoForm.事業領域名 = ''
      this.sagyoForm.外内販コード = ''
      this.sagyoForm.外内販名 = ''
      this.sagyoForm.税込支払金額 = ''
      // this.否認_差戻事由 = ''
      this.sagyoForm.否認_差戻事由 = ''
      this.sagyoForm.税込発注金額_税込支払金額_合計 = ''
      this.sagyoForm.dt税率別金額合計欄 = []
      this.sagyoForm.発注サブNo = ''
      this.sagyoForm.業者状態 = ''
      this.sagyoForm.負担区分 = ''
      this.sagyoForm.部門確認 = ''
      this.sagyoForm.msg費用按分 = ''
      this.sagyoForm.w費用負担部門按分フラグ = 0
      this.sagyoForm.dt按分入力済メッセージ = []
      this.sagyoForm.費用按分 = '0'
      this.sagyoForm.no32Data = {}
      this.sagyoForm.発注サブNo_F052 = ''
    },
    getDataF005 () {
      const 検収回数 = this.sagyo検収回数
      return {
        会社コード: this.userInfo.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        検収回数: 検収回数,
        発注サブNo: this.sagyoForm.発注サブNo,
        取引先コード: this.sagyo取引先.split('_')[0],
        取引先コード枝番: this.sagyo取引先.split('_')[1]
      }
    },
    async onKakuninToroku () {
      if (!await this.updateValidate('sagyo')) {
        return false
      }

      let result = ''
      let msgErr = ''
      const 検収回数 = this.sagyo検収回数
      const 取引先 = this.sagyo取引先.split('_')
      const 取引先コード = 取引先[0]
      const data = {
        DisplayTab: this.activeName,
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No,
        工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
        修繕区分小: this.syuzenForm.修繕区分小,
	      // ハノイ側修正2023/05/04　STEP2_W　課題管理表№335：設計書「修正履歴」シートの「2023/05/02仕様変更分」「計上部門コード、事業領域、内外販区分更新元追記」Start
        計上部門コード: this.sagyoForm.費用負担部門コード,
        事業領域: this.sagyoForm.事業領域,
        外内販: this.sagyoForm.外内販,
        // ハノイ側修正2023/05/04　STEP2_W　課題管理表№335：設計書「修正履歴」シートの「2023/05/02仕様変更分」「計上部門コード、事業領域、内外販区分更新元追記」End
        検収回数: 検収回数,
        F005_発注支払マスタ: this.getDataF005(),
        チェック1: this.sagyoForm.チェック1,
        チェック2: this.sagyoForm.チェック2,
        取引先コード,
        費用按分: this.sagyoForm.費用按分,
        状態: this.sagyoForm.状態
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/確認登録ボタン', data, 'アクセスしています....')
        .then((response) => {
          result = response.status
          msgErr = response.message
        })
      if (result) {
        await this.getVersion('sagyo')
        await this.showSuccess(msgErr)
      } else {
        this.showError(msgErr)
      }
    },
    validateBumonKakunin () {
      if (this.sagyoForm.msg費用按分) {
        this.getMsg('E040777').then((response) => {
          this.showError(response.data, {callback: () => {
            document.getElementById('refSagyoAnbunNyuryoku0').focus()
          }})
        })
        return false
      }
      return true
    },
    async onBumonKakunin () {
      if (this.sagyoForm.状態 === '支払承認待ち') {
        if (!await this.validateBumonKakunin()) {
          return false
        }

        let msg = (await this.getMsg('K040083')).data
        if (!await this.confirm(msg)) {
          return false
        }

        let result = ''
        let msgErr = ''
        const data = {
          会社コード: this.searchData.会社コード,
          工事依頼No: this.syuzenForm.工事依頼No,
          工事依頼No枝番: this.syuzenForm.工事依頼No枝番,
          修繕区分小: this.syuzenForm.修繕区分小,
          // ハノイ側修正2023/05/04　STEP2_W　課題管理表№335：設計書「修正履歴」シートの「2023/05/02仕様変更分」「計上部門コード、事業領域、内外販区分更新元追記」START
          計上部門コード: this.sagyoForm.費用負担部門コード,
          事業領域: this.sagyoForm.事業領域,
          外内販: this.sagyoForm.外内販,
          // ハノイ側修正2023/05/04　STEP2_W　課題管理表№335：設計書「修正履歴」シートの「2023/05/02仕様変更分」「計上部門コード、事業領域、内外販区分更新元追記」END
          検収回数: this.sagyo検収回数,
          取引先コード: this.sagyo取引先.split('_')[0],
          取引先コード枝番: this.sagyo取引先.split('_')[1],
          費用按分: this.sagyoForm.費用按分,
          DisplayTab: this.activeName,
          チェック1: this.sagyoForm.チェック1,
          チェック2: this.sagyoForm.チェック2,
          F005_発注支払マスタ: this.getDataF005(),
          状態: this.sagyoForm.状態
        }
        await this.http
          .post('/api/Reafs_W/Shonin/WD00520/BumonKakunin', data, 'アクセスしています....')
          .then((response) => {
            result = response.status
            msgErr = response.message
          })
        if (result) {
          const msg004 = (await this.getMsg('M040004')).data
          const msg021 = (await this.getMsg('M040021')).data
          if (await this.showSuccess(msg004 + this.syuzenForm.工事依頼No + msg021)) {
            this.onSearch()
          }
        } else {
          this.showError(msgErr)
        }
      }
    },
    async onTenpuSansho () {
      this.paramsIraiTenpuTouroku.工事依頼No = this.syuzenForm.工事依頼No// this.queryParams.工事依頼No
      this.showDialogIraiTenpuTouroku = true
    },
    IraiTenpuTourokuClose () {
      // this.onSearch()
    },
    reafsmodalBackHatchuAnbunSettei (obj) {
      this.hatchuForm.費用負担部門コード = obj.txtCd
      this.hatchuForm.費用負担部門名 = obj.lblNm
      if (this.hatchuForm.費用負担部門コード == '' || this.hatchuForm.費用負担部門コード.trim() == '') {
        this.hatchuForm.費用負担部門名 = ''
      } else {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: this.hatchuForm.費用負担部門コード })
          .then((response) => {
            if (response.data !== null) {
              this.hatchuForm.費用負担部門名 = response.data.費用負担部門
            } else {
              this.getMsg('E040747').then((response) => {
                this.showError(response.data, () => { this.$refs.refHatchu費用負担部門.focus() })
              }).finally(() => {
              })
              this.hatchuForm.費用負担部門名 = ''
            }
          })
      }
    },
    reafsmodalBackSagyoAnbunSettei (obj) {
      this.sagyoForm.費用負担部門コード = obj.txtCd
      this.sagyoForm.費用負担部門名 = obj.lblNm
      if (this.sagyoForm.費用負担部門コード == '' || this.sagyoForm.費用負担部門コード.trim() == '') {
        this.sagyoForm.費用負担部門名 = ''
      } else {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: this.sagyoForm.費用負担部門コード })
          .then((response) => {
            if (response.data !== null) {
              this.sagyoForm.費用負担部門名 = response.data.費用負担部門
            } else {
              this.getMsg('E040747').then((response) => {
                this.showError(response.data, () => { this.$refs.refSagyo費用負担部門.focus() })
              }).finally(() => {
              })
              this.sagyoForm.費用負担部門名 = ''
            }
          })
      }
    },
    // dataBackHatchuAnbunSettei (obj) {
    //   this.hatchuForm.按分設定 = obj.lblNm
    // },
    // dataBackSagyoAnbunSettei (obj) {
    //   this.sagyoForm.按分設定 = obj.lblNm
    // },
    selectHatchuSho_JigyoRyoiki (val) {
      this.hatchuForm.事業領域コード = val
    },
    selectHatchuSho_SotoNai (val) {
      this.hatchuForm.外内販コード = val
    },
    selectSagyo_JigyoRyoiki (val) {
      this.sagyoForm.事業領域コード = val
    },
    selectSagyo_SotoNai (val) {
      this.sagyoForm.外内販コード = val
    },
    async changeHiyo_AnbunHatchu (val) {
      if (val === '1') {
        this.isDisplayHatchuAnbunNyuryoku = true
        await this.getDataAnbunNyuryokuSumiMesseji('hatchu')
        this.hatchuForm.dt税率別金額合計欄.map((item, index) => {
          if (this.hatchuForm.dt按分入力済メッセージ[index].rowEmpty === '1') {
            if (this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額 === '') {
              item['按分入力済メッセージ'] = '未入力'
            } else if (this.getNumber(this.hatchuForm.dt按分入力済メッセージ[index].税込按分金額) === this.getNumber(item['率別売上金額税込'])) {
              item['按分入力済メッセージ'] = '入力済'
            } else {
              item['按分入力済メッセージ'] = '出来高入力済'
            }
          } else {
            item['按分入力済メッセージ'] = ''
          }
        })

        let isExist未入力 = true
        for (let i = 0; i < this.hatchuForm.dt税率別金額合計欄.length; i++) {
          if (this.hatchuForm.dt税率別金額合計欄[i].按分入力済メッセージ === '未入力') {
            isExist未入力 = false
            break
          }
        }
        if (!isExist未入力) {
          this.hatchuForm.msg費用按分 = '部門按分未入力があります。'
        } else {
          this.hatchuForm.msg費用按分 = ''
        }
      } else {
        this.isDisplayHatchuAnbunNyuryoku = false
        this.hatchuForm.dt税率別金額合計欄.map((item, index) => {
          item['按分入力済メッセージ'] = ''
        })
        this.hatchuForm.msg費用按分 = ''
      }
    },
    async changeHiyo_AnbunSagyo (val) {
      if (val === '1') {
        this.isDisplaySagyoAnbunNyuryoku = true
        await this.getDataAnbunNyuryokuSumiMesseji('sagyo')
        this.sagyoForm.dt税率別金額合計欄.map((item, index) => {
          if (this.sagyoForm.dt按分入力済メッセージ[index].rowEmpty === '1') {
            if (this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額 === '' ||
            this.getNumber(this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額) !== this.getNumber(item['税込発注金額_税込支払金額'])) { // ハノイ側修正2023/01/16　STEP2_W　課題管理表№271：設計書「修正履歴」シートの「2022/01/11仕様変更分」「按分入力ボタン押下条件追記	」
              item['按分入力済メッセージ'] = '未入力'
            } else if (this.getNumber(this.sagyoForm.dt按分入力済メッセージ[index].税込按分金額) === this.getNumber(item['税込発注金額_税込支払金額'])) {
              item['按分入力済メッセージ'] = '入力済'
            } else {
              // item['按分入力済メッセージ'] = '出来高入力済'// ハノイ側修正2023/01/16　STEP2_W　課題管理表№271：設計書「修正履歴」シートの「2022/01/11仕様変更分」「按分入力ボタン押下条件追記	」
            }
          } else {
            item['按分入力済メッセージ'] = ''
          }
        })

        let isExist未入力 = true
        for (let i = 0; i < this.sagyoForm.dt税率別金額合計欄.length; i++) {
          if (this.sagyoForm.dt税率別金額合計欄[i].按分入力済メッセージ === '未入力') {
            isExist未入力 = false
            break
          }
        }
        if (!isExist未入力) {
          this.sagyoForm.msg費用按分 = '部門按分未入力があります。'
        } else {
          this.sagyoForm.msg費用按分 = ''
        }
      } else {
        this.isDisplaySagyoAnbunNyuryoku = false
        this.sagyoForm.dt税率別金額合計欄.map((item, index) => {
          item['按分入力済メッセージ'] = ''
        })
        this.sagyoForm.msg費用按分 = ''
      }
    },
    showError (message, option = null, title = this.menuOptions.SubMenuName) {
      var self = this
      this.MsgErr({ title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof (option.callback) === 'function') {
            option.callback()
          }
        }})
    },
    showSuccess (message, title = this.menuOptions.SubMenuName) {
      return new Promise((resolve, reject) => {
        var self = this
        this.MsgInfo({ title,
          message,
          callback: function (a, b) {
            self.hideLoading()
            resolve(true)
          }})
      })
    },
    confirm (message, title = this.menuOptions.SubMenuName) {
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
    hideLoading () {
      this.loading = false
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    // ハノイ側修正2022/09/23　STEP2_W　課題管理表№119：設計書「修正履歴」シートの「2022/09/20仕様変更分」「参照権限チェック追加」Start
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
              this.getMsg('E040254').then((ress) => {
                this.showError(ress.data)
              })
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
            this.getMsg('E040254').then((ress) => {
              this.showError(ress.data)
            })
            return resolve(false)
          } else {
            return resolve(true)
          }
        }
      })
    },
    get営業店CheckData (allRange, 店コード, 店コード枝番) {
      return new Promise((resolve) => {
        this.http
          .post('/api/Reafs_W/Shonin/WD00520/get営業店CheckData',
            { lst支社コード: allRange, 店コード: 店コード, 店コード枝番: 店コード枝番 })
          .then((response) => {
            return resolve(response)
          })
      })
    },
    // ハノイ側修正2022/09/23　STEP2_W　課題管理表№119：設計書「修正履歴」シートの「2022/09/20仕様変更分」「参照権限チェック追加」End
    // ハノイ側修正2022/12/29　STEP2_W　課題管理表№261：設計書「修正履歴」シートの「2022/12/27仕様変更分」「発注申請タブ：承認ボタン押下時の発注書自動作成処理は、サーバ側での独立処理とし、承認処理とは別で処理されるようにする。」Start
    async ExportReport発注書 () {
      const data = {
        会社コード: this.searchData.会社コード,
        工事依頼No: this.syuzenForm.工事依頼No
      }
      await this.http
        .post('/api/Reafs_W/Shonin/WD00520/ExportReport発注書', data)
        .then((response) => {
        })
        .finally(() => {
        })
    }
    // ハノイ側修正2022/12/29　STEP2_W　課題管理表№261：設計書「修正履歴」シートの「2022/12/27仕様変更分」「発注申請タブ：承認ボタン押下時の発注書自動作成処理は、サーバ側での独立処理とし、承認処理とは別で処理されるようにする。」End
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
  padding: 10px;
  // min-width: 1380px;
  // height: 100%;
}

.disabled_checkbox {
  background: #d3d3d3;
}

.item_input_mitsumori_sakuseibi {
  height: 30px;
  line-height: 29px;
  width: 120px;
  text-align: center;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black !important;
  // padding: 180px 0 0 0;
  text-overflow: ellipsis;
  box-sizing: border-box;
  background: #d3d3d3;
  border: 1px solid #b7b5b5;
  border-radius: 1px;
}

.item_input_mitsumori_yukokigen {
  height: 30px;
  line-height: 29px;
  width: 120px;
  text-align: center;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black !important;
  // padding: 180px 0 0 0;
  text-overflow: ellipsis;
  box-sizing: border-box;
  background: #d3d3d3;
  border: 1px solid #b7b5b5;
  border-radius: 1px;
}

.item_input_chinryo_hassei_tsuki {
  height: 30px;
  line-height: 29px;
  width: 51.5%;
  text-align: center;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black !important;
  // padding: 180px 0 0 0;
  text-overflow: ellipsis;
  box-sizing: border-box;
  background: #d3d3d3;
  border: 1px solid #b7b5b5;
  border-radius: 1px;
}

.item_input_sekisan_kikan {
  height: 30px;
  line-height: 29px;
  width: 75%;
  text-align: right;
  padding-right: 13px;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black !important;
  // padding: 180px 0 0 0;
  text-overflow: ellipsis;
  box-sizing: border-box;
  background: #d3d3d3;
  border: 1px solid #b7b5b5;
  border-radius: 1px;
}

.item_input_jungong_nian_yeuri {
  height: 30px;
  line-height: 29px;
  width: 56%;
  text-align: center;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black !important;
  // padding: 180px 0 0 0;
  text-overflow: ellipsis;
  box-sizing: border-box;
  background: #d3d3d3;
  border: 1px solid #b7b5b5;
  border-radius: 1px;
}

.item_input_chikunensu {
  height: 30px;
  line-height: 29px;
  width: 74.6%;
  text-align: right;
  padding-right: 13px;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black !important;
  // padding: 180px 0 0 0;
  text-overflow: ellipsis;
  box-sizing: border-box;
  background: #d3d3d3;
  border: 1px solid #b7b5b5;
  border-radius: 1px;
}

.item_input_zan_keiyaku_kikan {
  height: 30px;
  line-height: 29px;
  width: 54%;
  text-align: center;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black !important;
  // padding: 180px 0 0 0;
  text-overflow: ellipsis;
  box-sizing: border-box;
  background: #d3d3d3;
  border: 1px solid #b7b5b5;
  border-radius: 1px;
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
// ハノイ側修正2022/09/23　STEP2_W　課題管理表№104：設計書「修正履歴」シートの「2022/09/12仕様変更分」「コントロールが存在するのにスクロールバーが表示されない」START
.home-el-main {
  overflow-y: auto;
}
.home-el-main::-webkit-scrollbar {
  background-color: rgba(0, 0, 0, 0.1);
  width: 10px;
  height: 10px;
}
.home-el-main::-webkit-scrollbar-thumb {
  background-color: rgba(82, 81, 81, 0.5);
  width: 10px;
  border-radius: 5px;
}
.home-el-main::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.5);
}
// ハノイ側修正2022/09/23　STEP2_W　課題管理表№104：設計書「修正履歴」シートの「2022/09/12仕様変更分」「コントロールが存在するのにスクロールバーが表示されない」END
.home-el-footer {
  padding: 10px 0 0 0;
}
.comment{
  padding-left: 20px;
}
.commentContent{
  padding: 1px 0 1px 0;
}
.item_label_mitsumoriJoken {
  height: 54px;
  line-height: 22px;
  width: 120px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 5px 0 0 0;
  box-sizing: border-box;
}
.item_input_mitsumoriJoken{
  width: calc(100% - 120px);
}
.border-top {
  border-top: 1px dotted black;
  margin-top: 10px;
  // padding-bottom: 20px;
}
.item_label_hinin_ashimodoshiJiyu {
  height: 54px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white;
  padding: 10px 0 0 0;
  box-sizing: border-box;
}
.item_input_hinin_ashimodoshiJiyu{
  width: calc(100% - 120px);
}
.item_label_sekoNaiyo_Tokukijikoto {
  height: 75px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 6px 0 0 0;
  box-sizing: border-box;
}
.item_input_sekoNaiyo_Tokukijikoto{
  width: calc(100% - 120px);
}
.item_label_shinseiNaiyo {
  height: 75px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 20px 0 0 0;
  box-sizing: border-box;
}
.item_input_shinseiNaiyo{
  width: 100%;
}
.item_label_koteiShisanNo {
  height: 31px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 0 0 0 0;
  box-sizing: border-box;
}
.item_label_biko {
  height: 75px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 20px 0 0 0;
  box-sizing: border-box;
}
.item_input_biko{
  width: calc(75% - 141px);
}
.item_label_col5 {
  height: 31px;
  line-height: 30px;
  width: 162px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 0 0 0 0;
  box-sizing: border-box;
}
.item_label_col6 {
  height: 31px;
  line-height: 30px;
  width: 130px;
  text-align: center;
  margin-top: 0px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 0 0 0 0;
  box-sizing: border-box;
}
.irai_eigyoten_ringi_shinsei_kyoka {
  height: 30px;
  line-height: 30px;
  width: 180px;
  text-align: left;
  margin-top: 0px;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: white;
  padding: 0 0 0 10px;
  box-sizing: border-box;
  margin-bottom: 2px;
}
.btnHelp{
  height: 30px;
  width: 52px;
  padding: 0 12px 0 12px;
}
.item_label_iraiNaiyo_fuguaiJokyo {
  height: 75px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  color: black;
  background: #d3d3d3;
  padding: 5px 0 0 0;
  box-sizing: border-box;
}
.item_input_iraiNaiyo_fuguaiJokyo {
  width: 100%;
}
.item_label_col30 {
  height: 30px;
  line-height: 30px;
  width: 120px;
  text-align: center;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  // color: white;
  background: #d3d3d3;
  color: black;
  padding: 0 0 0 0;
  box-sizing: border-box;
}
.item_input_opensearch {
  height: 30px;
  line-height: 27px;
  width: 150px;
  text-align: left;

  vertical-align: middle;
  float: left;
  font-size: 14px;
  // color: white;
  border: 1px solid darkgray;
  background: #d3d3d3;
  color: black;
  padding: 0 0 0 3px;
  box-sizing: border-box;
  text-overflow: ellipsis;
}
.item_label_Title {
  background: #d3d3d3 !important;
  padding-left: 5px;
  color: black !important;
  line-height: 30px;
  width: 120px;
  margin-bottom: 15px !important;
  text-align: left !important;
}
</style>

<style>
.SyuzenIraiSansyoShonin #tab-sagyo.el-tabs__item.is-top {
  border-right: 1px solid darkgray;
}
.SyuzenIraiSansyoShonin .el-tabs__item {
  height: 25px;
  line-height: 23px;
  width: 136px;
  text-align: center;
}
.SyuzenIraiSansyoShonin .el-tabs__item.is-top {
  background: #d3d3d3;
  border-color: #b7b5b5;
  color: black;
}
.SyuzenIraiSansyoShonin .el-tabs--card>.el-tabs__header .el-tabs__item.is-active {
  background: #FFFFFF;
  border-color: #b7b5b5;
  color: black;
  border-bottom: 2px solid #00AFF5;
}
.SyuzenIraiSansyoShonin .ve-table-body-tr, .ve-table-header-tr{
  height: 30px !important;
}
.SyuzenIraiSansyoShonin .ve-table-header-th{
  height: 37px;
}
.SyuzenIraiSansyoShonin .table-body-cell-class {
  background-color: #d3d3d3 !important;
}
.SyuzenIraiSansyoShonin .marginBottom{
  margin-bottom: 0px;
}
.SyuzenIraiSansyoShonin .el-textarea.is-disabled .el-textarea__inner {
  background: #d3d3d3;
  border-color: #b7b5b5;
  color: black;
  border-radius: 0;
}
.SyuzenIraiSansyoShonin .label_Bottom {
  margin-bottom: 0 !important;
}
.SyuzenIraiSansyoShonin .checkbox {
  width: 120px;
  height: 30px;
  text-align: center;
  background-color: white;
  border-right: 1px solid #b7b5b5 !important;
  border-bottom: 1px solid #b7b5b5 !important;
  border-top: 1px solid #b7b5b5 !important;
}
.SyuzenIraiSansyoShonin .lblKoteiShisanNo.el-form-item__label {
  margin-top: 0px !important;
  height: 31px !important;
}
.SyuzenIraiSansyoShonin {
  height: 100%;
}
.SyuzenIraiSansyoShonin .sample-form-class {
  height: 100%;
}
.SyuzenIraiSansyoShonin .home-el-container {
  height: 100%;
}
.main-content:has(.SyuzenIraiSansyoShonin) > .el-scrollbar > .el-scrollbar__wrap > .el-scrollbar__view {
  height: 100%;
}
.SyuzenIraiSansyoShonin .is-readonly .el-input.is-disabled .el-input__inner {
  height: 30px !important;
}
.SyuzenIraiSansyoShonin .el-input.is-disabled .el-input__inner {
  height: 30px !important;
  background: #d3d3d3;
  border-color: #b7b5b5;
  color: black;
  text-overflow: ellipsis;
}
.SyuzenIraiSansyoShonin .el-form .el-input__inner {
  height: 30px !important;
}
.SyuzenIraiSansyoShonin .is-readonly.date .el-input.is-disabled .el-input__inner {
  text-align: center;
}
.SyuzenIraiSansyoShonin .el-tabs__content {
  overflow: overlay;
}
.SyuzenIraiSansyoShonin .ve-table .ve-table-container table.ve-table-content {
  /* top: -2px !important; */
}
.SyuzenIraiSansyoShonin .dtMitsu .ve-table .ve-table-container {
  height: 375px !important;
  min-height: 152px;
}
.SyuzenIraiSansyoShonin .dtHatchu .ve-table .ve-table-container {
  height: 375px !important;
  min-height: 152px;
  max-height: 202px !important;
}
.SyuzenIraiSansyoShonin .dtSagyo .ve-table .ve-table-container {
  /* min-height: 65px !important; */
  overflow-y: hidden;
}
.SyuzenIraiSansyoShonin .dtSagyo .ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.ve-table-header-th {
  background-color: #d3d3d3 !important;
  color: black !important;
  height: auto !important;
}
.SyuzenIraiSansyoShonin .dtHatchu .ve-table {
  padding-left: 10px;
}
.SyuzenIraiSansyoShonin .dtHatchu .ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.ve-table-header-th {
  background-color: #d3d3d3 !important;
  color: black !important;
  height: auto !important;
}
.SyuzenIraiSansyoShonin .dtMitsu .ve-table {
  padding-left: 10px;
}
.SyuzenIraiSansyoShonin .dtMitsu .ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.ve-table-header-th {
  background-color: #d3d3d3 !important;
  color: black !important;
  height: auto !important;
}
/* ハノイ側修正2022/08/24　STEP2_W　課題管理表№55：設計書「修正履歴」シートの「2022/08/23仕様変更分」明細の各項目の横幅を変更可能としたい。 */
.SyuzenIraiSansyoShonin .resizer {
  height: 100% !important;
}
/* ハノイ側修正2022/08/29　STEP2_W　課題管理表№54：設計書「修正履歴」シートの「2022/08/23仕様変更分」「フッターボタン配置位置調整依頼」 */
.ve-loading-parent-relative:has(.SyuzenIraiSansyoShonin) {
  position: static !important;
}
.noMarginBottom{
  margin-bottom: 0px !important;
}
#申請内容 label{
  height: 75px !important;
  padding: 20px !important;
}
#備考 label{
  height: 75px !important;
  padding: 20px !important;
}
#依頼内容_不具合状況 label{
  width: 120px;
  height: 75px;
  text-indent: 12px;
  padding: 10px;
}
.SyuzenIraiSansyoShonin .label_Bottom.Zeiritsu .el-form-item__label {
  text-align: left;
  padding-left: 6px;
  width: 100% !important;
  border: 1px solid #000 !important;
  border-top: none !important;
}
.SyuzenIraiSansyoShonin .label_Bottom.ZeikomiHatchuKingaku .el-form-item__label {
  width: 163px !important;
  padding-left: 5px;
}
.SyuzenIraiSansyoShonin .label_Bottom.ShiharaiYoteiBi .el-form-item__label {
  width: 121px !important;
}
.SyuzenIraiSansyoShonin .txtMargin .el-input.is-disabled .el-input__inner {
  /* margin-left: -1px; */
  border-left: 0;
}
.SyuzenIraiSansyoShonin .txtMarginBorder .el-input.is-disabled .el-input__inner {
  margin-left: -1px;
  padding: 5px;
  border: 1px solid #000 !important;
  border-top: none !important;
  border-right: none !important;
}
.SyuzenIraiSansyoShonin .txtMarginBorderColLast .el-input.is-disabled .el-input__inner {
  margin-left: -1px;
  padding: 5px;
  border: 1px solid #000 !important;
  border-top: none !important;
}
.SyuzenIraiSansyoShonin .費用負担部門 > div > div > div > button {
  padding: 0 12px 0 12px !important;
}
.SyuzenIraiSansyoShonin .ve-table .ve-table-container table.ve-table-content tfoot.ve-table-footer tr.ve-table-footer-tr td.ve-table-footer-td {
  text-align: right !important;
  padding: 5px !important;
  background-color: #d3d3d3 !important;
}
.SyuzenIraiSansyoShonin .ve-table .ve-table-container table.ve-table-content tfoot.ve-table-footer tr.ve-table-footer-tr td.ve-table-footer-td.table-footer-cell-total {
  text-align: left !important;
  padding: 5px !important;
  background-color: #d3d3d3 !important;
}
.SyuzenIraiSansyoShonin .ve-table .ve-table-container table.ve-table-content tfoot.ve-table-footer tr.ve-table-footer-tr td.ve-table-footer-td.table-footer-cell-totalSagyoTab {
  text-align: center !important;
  padding: 5px !important;
  background-color: #d3d3d3 !important;
}
.SyuzenIraiSansyoShonin .ve-table .ve-table-container table.ve-table-content tfoot.ve-table-footer tr.ve-table-footer-tr {
  height: 31px !important;
}
.item_label {
  max-width: 333px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
