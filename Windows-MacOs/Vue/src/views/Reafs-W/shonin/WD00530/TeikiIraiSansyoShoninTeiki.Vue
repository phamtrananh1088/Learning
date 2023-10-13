<template>
  <div class="home-container TeikiIraiSansyoShonin">
    <el-form
      :model="iraiForm"
      :rules="rules"
      ref="refIraiForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk="true"
    >
      <el-container class="home-el-container">
        <el-header height="auto" class="home-el-header">
          <el-row>
            <el-col :span="8">
              <div style="display: flex">
                <el-form-item
                  label="営業店"
                  prop="営業店コード"
                  class="is-readonly"
                >
                  <eigyoten
                    tabindex="2"
                    :eigyotenCd="iraiForm.店コード"
                    @update:eigyotenCd="
                      (v) => {
                        this.iraiForm.店コード = v;
                      }
                    "
                    :eigyotenBango="iraiForm.店コード枝番"
                    @update:eigyotenBango="
                      (v) => {
                        this.iraiForm.店コード枝番 = v;
                      }
                    "
                    :appendlabelText="iraiForm.営業店名"
                    disabled
                    @backData="reafsmodalBackEigyoten"
                    ref="ref営業店"
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
            </el-form>
            <el-col :span="5">
              <el-form-item
                label="契約№"
                prop="KeiyakuNanber"
                class="is-required"
              >
                <div style="display: flex">
                  <reafsinputtext
                    tabindex="45"
                    maxlength="8"
                    v-model="searchForm.KeiyakuNanber"
                    :disabled="画面入力制御disabled.契約番号"
                    :width="'100px'"
                    ref="ref契約番号"
                    @blur="onBlurKeiyakuNanberEdaban"
                  ></reafsinputtext>
                  <reafsinputtext
                    tabindex="46"
                    v-model="searchForm.KeiyakuNanberEdaban"
                    maxlength="3"
                    :width="'60px'"
                    :disabled="画面入力制御disabled.契約番号枝番"
                    class="is-readonly"
                    @blur="onBlurKeiyakuNanberEdaban"
                  ></reafsinputtext>
                </div>
              </el-form-item>
            </el-col>

            <el-col :span="4">
              <el-form-item label="契約年度" prop="KeiyakuNendo">
                <div style="display: flex">
                  <reafsinputtext
                    tabindex="47"
                    maxlength="4"
                    v-model="searchForm.KeiyakuNendo"
                    :width="'80px'"
                    @blur="onBlurKeiyakuNendo"
                  >
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>

            <el-col :span="5">
              <el-form-item label="依頼№" prop="iraiNo" class="is-readonly">
                <div style="display: flex">
                  <reafsinputtext
                    tabindex="1"
                    maxlength="7"
                    v-model="iraiForm.iraiNo"
                    :width="'120px'"
                    disabled
                    ref="iraiNo"
                  >
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="2" style="padding-left: 20px; padding-top: 2px">
              <el-button
                tabindex="1"
                type="primary"
                size="mini"
                @click="onSearch"
                :loading="loading"
                >検索</el-button
              >
            </el-col>

            <el-col :span="8">
              <el-form-item label="物件名" prop="bukken" class="is-readonly">
                <div style="display: flex">
                  <reafsinputtext
                    v-model="iraiForm.物件コード"
                    maxlength="50"
                    disabled
                    :width="'120px'"
                  >
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件名"
                    maxlength="50"
                    disabled
                    :width="'100%'"
                  >
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
                  maxlength="40"
                  disabled
                  :width="'120px'"
                >
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
                    maxlength="7"
                    disabled
                    :width="'110px'"
                  >
                  </reafsinputtext>
                  <reafsinputtext
                    v-model="iraiForm.物件住所Nm"
                    maxlength="30"
                    disabled
                    :width="'370px'"
                  >
                  </reafsinputtext>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
        </el-header>

        <el-menu
          :default-active="activeIndex"
          class="el-menu-class"
          mode="horizontal"
          @select="handleSelect"
        >
          <el-menu-item index="1">依頼</el-menu-item>
          <el-menu-item index="2">見積</el-menu-item>
          <el-menu-item index="3">契約内容</el-menu-item>
          <el-menu-item index="4">作業結果／支払</el-menu-item>
        </el-menu>
        <div style="width: 1px; height: 20px"></div>

        <!-- 依頼 -->
        <el-row :gutter="20" v-show="selectIndex == '1'">
          <el-col :span="8">
            <el-form-item label="工事区分" prop="工事区分" class="is-readonly">
              <el-select
                tabindex="10"
                v-model="iraiForm.工事区分"
                disabled
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
              <el-button tabindex="10" class="btnHelp" @click="onHelp"
                >？</el-button
              >
              <kojikubunsetsumeiichiran
                :dialogVisible.sync="showDialogKojikubunsetsumeiichiran"
              ></kojikubunsetsumeiichiran>
            </el-form-item>
            <el-form-item label="大区分" prop="大区分" class="is-readonly">
              <el-select
                tabindex="11"
                v-model="iraiForm.大区分"
                disabled
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
            <el-form-item label="中区分" prop="中区分" class="is-readonly">
              <el-select
                tabindex="12"
                v-model="iraiForm.中区分"
                disabled
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
            <el-form-item label="小区分" prop="小区分" class="is-readonly">
              <el-select
                tabindex="13"
                v-model="iraiForm.小区分"
                disabled
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
            <el-form-item label="依頼状況" prop="依頼状況" class="is-readonly">
              <el-select
                tabindex="28"
                v-model="iraiForm.依頼状況"
                disabled
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
            <!-- 位置調整用DIV -->
            <div style="width: 1px; height: 140px"></div>
          </el-col>
        </el-row>

        <el-row v-show="selectIndex == '1'">
          <el-col :span="8">
            <el-form-item
              label="依頼者"
              prop="依頼者コード"
              class="is-readonly"
            >
              <div class="依頼者">
                <iraisha
                  tabindex="14"
                  maxlength="7"
                  v-model="iraiForm.依頼者コード"
                  :appendlabelText="iraiForm.依頼者名"
                  disabled
                  @backData="reafsmodalBackIraiSha"
                  ref="ref依頼者コード"
                >
                </iraisha>
              </div>
            </el-form-item>
            <el-form-item
              label="依頼者代理"
              prop="依頼者代理コード"
              class="is-readonly"
            >
              <div class="依頼者代理">
                <iraisha
                  tabindex="19"
                  maxlength="7"
                  v-model="iraiForm.依頼者代理コード"
                  :appendlabelText="iraiForm.依頼者代理名"
                  disabled
                  @backData="reafsmodalBackIraiShadairi"
                  ref="ref依頼者代理コード"
                >
                </iraisha>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label-width="0" class="is-readonly">
              <reafsinputtext
                v-model="iraiForm.依頼者所属部署名"
                readonly
                :width="'320px'"
              >
              </reafsinputtext>
            </el-form-item>
            <el-form-item label-width="0" class="is-readonly">
              <reafsinputtext
                v-model="iraiForm.依頼者代理所属部署名"
                readonly
                :width="'320px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-form-item label-width="0" class="is-readonly">
              <reafsinputtext
                v-model="iraiForm.依頼者役職名"
                readonly
                :width="'150px'"
              >
              </reafsinputtext>
            </el-form-item>
            <el-form-item label-width="0" class="is-readonly">
              <reafsinputtext
                v-model="iraiForm.依頼者代理役職名"
                readonly
                :width="'150px'"
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
                tabindex="18"
                type="tel"
                maxlength="20"
                v-model="iraiForm.依頼者連絡先"
                disabled
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
                tabindex="23"
                type="tel"
                maxlength="20"
                v-model="iraiForm.依頼者代理連絡先"
                disabled
                :width="'150px'"
                ref="ref依頼者代理連絡先"
              ></reafsinputtext>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-show="selectIndex == '1'">
          <el-col :span="14">
            <el-form-item label="依頼コメント" class="is-readonly">
              <reafsinputtext
                tabindex="24"
                maxlength="50"
                v-model="iraiForm.依頼コメント"
                disabled
                :width="'663px'"
              ></reafsinputtext>
            </el-form-item>
            <el-form-item label="対象箇所" prop="対象箇所" class="is-readonly">
              <reafsinputtext
                tabindex="25"
                maxlength="50"
                v-model="iraiForm.対象箇所"
                disabled
                :width="'663px'"
                ref="ref対象箇所"
              ></reafsinputtext>
            </el-form-item>
            <el-form-item label="メーカー名" class="is-readonly">
              <reafsinputtext
                tabindex="26"
                maxlength="50"
                v-model="iraiForm.メーカー名"
                disabled
                :width="'663px'"
              ></reafsinputtext>
            </el-form-item>
            <el-form-item
              class="is-readonly"
              prop="依頼内容_状況"
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
                  disabled
                  ref="iraiNaiyo_fuguaiJokyo"
                >
                </el-input>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6" class="comment">
            <!-- 位置調整用DIV -->
            <div style="width: 1px; height: 90px"></div>
            <el-form-item label="添付一覧" label-width="325px"> </el-form-item>
            <div style="padding-bottom: 20px">
              <div class="commentContent">
                <label class="item_label">1 {{ iraiForm.部位コメント1 }}</label>
              </div>
              <div>
                <label class="item_label">2 {{ iraiForm.部位コメント2 }}</label>
              </div>
              <div>
                <label class="item_label">3 {{ iraiForm.部位コメント3 }}</label>
              </div>
              <div>
                <label class="item_label">4 {{ iraiForm.部位コメント4 }}</label>
              </div>
            </div>
          </el-col>
          <el-col span="2">
            <!-- 位置調整用DIV -->
            <div style="width: 1px; height: 170px"></div>
            <el-button
              tabindex="38"
              @click="onViewAttachRegis"
              style="float: right"
              :disabled="画面入力制御disabled.btn添付登録参照"
              >添付<br />参照</el-button
            >
            <screenPopup
              src="/Reafs_W/IraiTenpuTouroku"
              :params="paramsIraiTenpuTouroku"
              :dialogVisible.sync="showDialogIraiTenpuTouroku"
              @closePopupEvent="IraiTenpuTourokuClose"
            ></screenPopup>
          </el-col>
        </el-row>

        <el-row v-show="selectIndex == '1'">
          <el-col :span="7">
            <el-form-item label="中止取下げ区分" class="is-readonly">
              <el-select
                tabindex="30"
                v-model="iraiForm.中止取下げ区分"
                disabled
                @change="selectChushiHoryuKubun"
                ref="ref中止取下げ区分"
              >
                <el-option
                  v-for="item in 中止取下げ区分value"
                  :key="item.名称コード"
                  :label="item.名称"
                  :value="item.名称コード"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              label="中止取下げ年月日"
              prop="中止取下げ年月日"
              class="is-readonly"
            >
              <el-date-picker
                tabindex="31"
                maxlength="10"
                type="date"
                placeholder=""
                v-model="iraiForm.中止取下げ年月日"
                disabled
                style="width: 135px"
                format="yyyy/MM/dd"
                value-format="yyyy/MM/dd"
                ref="ref中止取下げ年月日"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item
              label="中止取下げ事由"
              prop="中止取下げ事由"
              class="is-readonly"
            >
              <reafsinputtext
                tabindex="32"
                maxlength="200"
                v-model="iraiForm.中止取下げ事由"
                disabled
                :width="'100%'"
                ref="ref中止取下げ事由"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item
              label="否認・差戻事由"
              prop="HininSashimodoshi"
              class="is-required"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="47"
                  maxlength="100"
                  v-model="iraiForm.否認差戻事由"
                  :disabled="画面入力制御disabled.否認差戻事由"
                  :width="'680px'"
                  ref="ref否認差戻事由"
                  :class="{ pinkcolor: isErrPinkSashimodoshiJiyu }"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 見積 -->
        <el-row :gutter="20" v-show="selectIndex == '2'">
          <el-col :span="5">
            <el-form-item label="見積作成日" class="is-readonly">
              <el-date-picker
                tabindex="41"
                maxlength="10"
                type="date"
                placeholder=""
                v-model="iraiForm.見積作成日"
                disabled
                style="width: 135px"
                format="yyyy/MM/dd"
                value-format="yyyy/MM/dd"
                ref="ref見積作成日"
              ></el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="11">
            <el-form-item
              label="見積№"
              prop="MitumoriNanber"
              class="is-readonly"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="42"
                  maxlength="11"
                  v-model="iraiForm.見積番号"
                  disabled
                  class="is-readonly"
                  :width="'120px'"
                  ref="ref見積番号"
                ></reafsinputtext>
                <label>-</label>
                <reafsinputtext
                  v-model="iraiForm.見積番号枝番"
                  maxlength="3"
                  :width="'60px'"
                  disabled
                  class="is-readonly"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <!-- 見積一覧 -->
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <reafstable
                :single="single"
                :rowKey="rowKey"
                :tableData="tblMitumoriData"
                :columns="tblMitumoriColumns"
                :height="50"
                :max-height="800"
                :columnIndex="true"
                :columnChkbox="false"
                :linkView="fnc_clickLink"
                :key="Math.random()"
              >
              </reafstable>
            </div>
          </el-col>

          <div style="height: 400px"></div>

          <el-col :span="3">
            <div style="height: 1px"></div>
          </el-col>

          <el-col :span="6">
            <el-form-item
              label="金額(税抜)計"
              prop="KingakuZeiNuki"
              class="is-readonly"
            >
              <div style="display: flex">
                <reafsinputnumber
                  tabindex="43"
                  maxlength="12"
                  v-model="iraiForm.金額税抜計"
                  disabled
                  :width="'160px'"
                  :floatLength="0"
                  :intLength="12"
                  ref="ref金額税抜計"
                  autocomplete="off"
                ></reafsinputnumber>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item
              label="金額(税込)計"
              prop="KingakuZeiKomi"
              class="is-readonly"
            >
              <div style="display: flex">
                <reafsinputnumber
                  tabindex="44"
                  maxlength="12"
                  align="right"
                  v-model="iraiForm.金額税込計"
                  disabled
                  :width="'160px'"
                  :floatLength="0"
                  :intLength="12"
                  ref="ref金額税込計"
                  autocomplete="off"
                ></reafsinputnumber>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item
              label="消費税額"
              prop="ShouhiZeigaku"
              class="is-readonly"
            >
              <div style="display: flex">
                <reafsinputnumber
                  tabindex="45"
                  maxlength="12"
                  v-model="iraiForm.消費税額"
                  disabled
                  :width="'160px'"
                  :floatLength="0"
                  :intLength="12"
                  ref="ref消費税額"
                  autocomplete="off"
                ></reafsinputnumber>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="3">
            <div style="height: 1px"></div>
          </el-col>

          <el-col :span="12">
            <el-form-item class="is-readonly" prop="見積条件" label-width="0px">
              <div style="display: flex">
                <label class="el-form-item__label item_label_MitumoriJoken"
                  >見積<br />条件</label
                >
                <el-input
                  tabindex="46"
                  class="item_input_MitumoriJoken"
                  maxlength="12"
                  type="textarea"
                  resize="none"
                  :rows="2"
                  v-model="iraiForm.見積条件"
                  disabled
                  ref="MitumoriJoken"
                >
                </el-input>
              </div>
            </el-form-item>
          </el-col>

          <div style="height: 120px"></div>

          <el-col :span="3">
            <div style="height: 1px"></div>
          </el-col>

          <el-col :span="6">
            <el-form-item
              label="否認・差戻事由"
              prop="HininSashimodoshi_mitumori"
              class="is-required"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="47"
                  maxlength="100"
                  v-model="iraiForm.否認差戻事由"
                  :disabled="画面入力制御disabled.否認差戻事由"
                  :width="'680px'"
                  ref="ref否認差戻事由_見積"
                  :class="{ pinkcolor: isErrPinkSashimodoshiJiyu }"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <div style="height: 105px"></div>
        </el-row>

        <!-- 契約内容 -->
        <el-row :gutter="20" v-show="selectIndex == '3'">
          <el-col :span="5">
            <el-form-item
              label="ＳＧＷ契約書番号"
              prop="SGW_KeiyakuBango"
              class="is-required"
            >
              <reafsinputtext
                tabindex="51"
                v-model="iraiForm.ＳＧＷ契約書番号"
                :disabled="画面入力制御disabled.ＳＧＷ契約書番号"
                maxlength="8"
                :width="'110px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="2">
            <el-button
              @click="onUpdateKeiyakuBango"
              style="float: right"
              :disabled="画面入力制御disabled.btn契約書番号更新"
              >契約書番号<br />更新
            </el-button>
          </el-col>

          <div style="height: 80px"></div>

          <el-col :span="8">
            <el-form-item
              label="初回契約開始年月"
              prop="ShokaiKeiyakuKaishiNengetu"
              class="is-readonly"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="53"
                  maxlength="4"
                  v-model="iraiForm.初回契約開始年月_年"
                  disabled
                  :width="'80px'"
                  ref="ref初回契約開始年月"
                ></reafsinputtext>
                <reafsinputtext
                  v-model="iraiForm.初回契約開始年月_月"
                  maxlength="2"
                  :width="'60px'"
                  disabled
                  class="is-readonly"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item
              label="変更契約開始年月"
              prop="HenkouKeiyakuKaishiNengetu"
              class="is-readonly"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="54"
                  maxlength="4"
                  v-model="iraiForm.変更契約開始年月_年"
                  disabled
                  :width="'80px'"
                  ref="ref変更契約開始年月"
                ></reafsinputtext>
                <reafsinputtext
                  v-model="iraiForm.変更契約開始年月_月"
                  maxlength="2"
                  :width="'60px'"
                  disabled
                  class="is-readonly"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <div style="height: 50px"></div>

          <el-col :span="8">
            <el-form-item
              label="契約終了年月"
              prop="KeiyakuShuryouNengetu"
              class="is-readonly"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="55"
                  maxlength="4"
                  v-model="iraiForm.契約終了年月_年"
                  disabled
                  :width="'80px'"
                  ref="ref契約終了年月"
                ></reafsinputtext>
                <reafsinputtext
                  v-model="iraiForm.契約終了年月_月"
                  maxlength="2"
                  :width="'60px'"
                  disabled
                  class="is-readonly"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <div style="height: 70px"></div>

          <div>
            <el-col :span="4">
              <label>【表示金額：税抜】</label>
            </el-col>
          </div>

          <!-- 表示金額税金(作業項目) -->
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <reafstable
                :single="single"
                :rowKey="rowKey"
                :tableData="tblKeiyakuData"
                :columns="tblKeiyakuColumns"
                :height="78"
                :max-height="800"
                :columnIndex="false"
                :columnChkbox="false"
                :linkView="fnc_clickLink"
                :key="Math.random()"
              >
              </reafstable>
            </div>
          </el-col>

          <el-col :span="3">
            <div style="height: 110px"></div>
          </el-col>

          <!-- 表示金額税金(合計) -->
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <reafstable
                :single="single"
                :rowKey="rowKey"
                :tableData="tblKeiyakuDataGoukei"
                :columns="tblKeiyakuColumns"
                :max-height="800"
                :columnIndex="false"
                :columnChkbox="false"
                :linkView="fnc_clickLink"
                :key="Math.random()"
              >
              </reafstable>
            </div>
          </el-col>

          <el-col :span="4">
            <div style="height: 1px"></div>
          </el-col>

          <el-col :span="6">
            <div style="height: 20px"></div>
            <el-form-item
              label="否認・差戻事由"
              prop="HininSashimodoshi_keiyaku"
              class="is-required"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="56"
                  maxlength="100"
                  v-model="iraiForm.否認差戻事由"
                  :disabled="画面入力制御disabled.否認差戻事由"
                  :width="'680px'"
                  ref="ref否認差戻事由_契約"
                  :class="{ pinkcolor: isErrPinkSashimodoshiJiyu }"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 依頼 -->

        <el-row :gutter="20" v-show="selectIndex == '4'">
          <!-- <el-col :span="12">
          <el-form-item
            label="実施月"
            prop="実施月"
            class="is-required"
          >
            <reafsinputtext
              :title="shiharaiForm.実施月"
              v-model="shiharaiForm.実施月"
              maxlength="10"
              :disabled="True"
              :width="'30%'">
            </reafsinputtext>
          </el-form-item>
        </el-col> -->

          <el-col :span="12">
            <div class="nengetu">
              <el-form-item label="実施月" prop="実施月">
                <el-date-picker
                  :required="true"
                  type="month"
                  format="yyyy/MM"
                  :clearable="false"
                  :width="'80px'"
                  v-model="shiharaiForm.実施月"
                  :class="{ pinkcolor: isErrPinkSagyotsuki }"
                  index="1"
                  ref="ref実施月"
                ></el-date-picker>
              </el-form-item>
            </div>
          </el-col>
          <el-col :span="12" style="padding-left: 20px; padding-top: 2px">
            <el-button
              tabindex="1"
              type="primary"
              size="mini"
              @click="onSearchShiharai"
              :loading="loading"
              >検索</el-button
            >
          </el-col>
          <el-col :span="24">
            <el-form-item label="作業名称" prop="作業名称" class="is-required">
              <el-select
                tabindex="11"
                v-model="shiharaiForm.作業名称"
                disabled
                :width="'150%'"
                ref="ref作業名称"
              >
                <el-option
                  v-for="item in 作業名称value"
                  :key="item.作業名称"
                  :label="item.作業名称"
                  :value="item.明細No"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状態" prop="状態" class="is-readonly">
              <reafsinputtext
                :title="shiharaiForm.状態"
                v-model="shiharaiForm.状態"
                maxlength="10"
                disabled
                :width="'30%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col style="width: 22%" :span="6">
            <el-form-item
              label="進捗（本体）"
              prop="shinchokuHontai"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.進捗_本体"
                v-model="shiharaiForm.進捗_本体"
                disabled
                :width="'172px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="padding-left: 10px">
            <el-form-item
              label="進捗（発注）"
              prop="shinchokuHatchu"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.進捗_発注"
                v-model="shiharaiForm.進捗_発注"
                disabled
                :width="'172px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item
              label="取引先"
              prop="torihikisaki"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.取引先"
                v-model="shiharaiForm.取引先"
                disabled
                :width="'30%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item
              label="作業担当者"
              prop="sagyotantosya"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.作業担当者"
                v-model="shiharaiForm.作業担当者"
                disabled
                :width="'40%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item
              label="担当者連絡先"
              prop="tantosyarenrakusaki"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.担当者連絡先"
                v-model="shiharaiForm.担当者連絡先"
                disabled
                :width="'20%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="11">
            <el-form-item
              label="作業実施日"
              prop="作業実施日"
              class="is-readonly date"
            >
              <div style="display: flex">
                <reafsinputtext
                  v-model="shiharaiForm.作業完了日_FROM"
                  maxlength="10"
                  disabled
                  :width="'120px'"
                >
                </reafsinputtext>

                &nbsp;～&nbsp;
                <reafsinputtext
                  v-model="shiharaiForm.作業完了日_TO"
                  maxlength="10"
                  disabled
                  :width="'120px'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="18">
            <el-form-item
              class="is-readonly is-area"
              prop="完了報告"
              label-width="0px"
            >
              <div style="display: flex">
                <label class="item_label_sekoNaiyo_Tokukijikoto"
                  >完了報告</label
                >
                <el-input
                  class="item_input_sekoNaiyo_Tokukijikoto"
                  maxlength="200"
                  type="textarea"
                  resize="none"
                  :rows="3"
                  :title="shiharaiForm.完了報告"
                  v-model="shiharaiForm.完了報告"
                  disabled
                  ref="sekoNaiyo_Tokukijikoto"
                >
                </el-input>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-form-item
              label="費用負担部門"
              prop="費用負担部門"
              class="is-readonly date"
            >
              <div style="display: flex">
                <reafsinputtext
                  v-model="shiharaiForm.費用負担部門コード"
                  maxlength="10"
                  disabled
                  :width="'100px'"
                >
                </reafsinputtext>
                <reafsinputtext
                  class="none-border-left"
                  v-model="shiharaiForm.費用負担部門名"
                  maxlength="5"
                  disabled
                  :width="'100%'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="9">
            <el-form-item
              label="事業領域"
              prop="事業領域"
              class="is-readonly date"
            >
              <div style="display: flex">
                <reafsinputtext
                  v-model="shiharaiForm.事業領域コード"
                  maxlength="10"
                  disabled
                  :width="'100px'"
                >
                </reafsinputtext>
                <reafsinputtext
                  class="none-border-left"
                  v-model="shiharaiForm.事業領域名"
                  maxlength="5"
                  disabled
                  :width="'100%'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item
              label="外内販区分"
              prop="外内販区分"
              class="is-readonly date"
            >
              <div style="display: flex">
                <reafsinputtext
                  v-model="shiharaiForm.外内販区分コード"
                  maxlength="10"
                  disabled
                  :width="'100px'"
                >
                </reafsinputtext>
                <reafsinputtext
                  class="none-border-left"
                  v-model="shiharaiForm.外内販区分名"
                  maxlength="5"
                  disabled
                  :width="'100%'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="税率" prop="税率" class="is-readonly">
              <reafsinputtext
                :title="shiharaiForm.税率"
                v-model="shiharaiForm.税率"
                disabled
                :width="'100%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="5">
            <el-form-item
              label="税込支払金額"
              prop="税込支払金額"
              class="is-readonly"
            >
              <reafsinputnumber
                :title="shiharaiForm.税込支払金額"
                v-model="shiharaiForm.税込支払金額"
                disabled
                :width="'100%'"
              >
              </reafsinputnumber>
            </el-form-item>
          </el-col>

          <el-col :span="14">
            <el-form-item
              label="支払予定日"
              prop="支払予定日"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.支払予定日"
                v-model="shiharaiForm.支払予定日"
                disabled
                :width="'20%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="●テナント" prop="テナント" class="is-readonly">
            </el-form-item>
          </el-col>

          <el-col :span="5">
            <el-form-item
              label="完了確認日"
              prop="完了確認日"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.完了確認日"
                v-model="shiharaiForm.完了確認日"
                disabled
                :width="'100%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="19">
            <el-form-item
              label="完了確認者"
              prop="完了確認者"
              class="is-readonly"
            >
              <reafsinputtext
                :title="shiharaiForm.完了確認者"
                v-model="shiharaiForm.完了確認者"
                disabled
                :width="'60%'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>

          <el-col :span="3">
            <el-form-item label="書類不備" prop="書類不備" class="is-readonly">
            </el-form-item>
          </el-col>

          <el-col :span="21">
            <el-form-item label="解消日" prop="解消日" class="is-readonly">
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item
              label="点検書類待ち"
              prop="点検書類待ち"
              class="is-readonly date"
            >
              <div style="display: flex">
                <reafsinputtext
                  v-model="shiharaiForm.点検書類待ち"
                  maxlength="10"
                  disabled
                  :width="'50px'"
                >
                </reafsinputtext>
                <reafsinputtext
                  class="none-border-left"
                  v-model="shiharaiForm.点検書類待ち_解消日"
                  maxlength="5"
                  disabled
                  :width="'125px'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item
              label="完了報告書"
              prop="完了報告書"
              class="is-readonly date"
            >
              <div style="display: flex">
                <reafsinputtext
                  v-model="shiharaiForm.完了報告書"
                  maxlength="10"
                  disabled
                  :width="'50px'"
                >
                </reafsinputtext>
                <reafsinputtext
                  class="none-border-left"
                  v-model="shiharaiForm.完了報告書_解消日"
                  maxlength="5"
                  disabled
                  :width="'125px'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="請求書" prop="請求書" class="is-readonly date">
              <div style="display: flex">
                <reafsinputtext
                  v-model="shiharaiForm.請求書"
                  maxlength="10"
                  disabled
                  :width="'50px'"
                >
                </reafsinputtext>
                <reafsinputtext
                  class="none-border-left"
                  v-model="shiharaiForm.請求書_解消日"
                  maxlength="5"
                  disabled
                  :width="'125px'"
                >
                </reafsinputtext>
              </div>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <div style="height: 1px"></div>
          </el-col>
          <el-col :span="6">
            <div style="height: 20px"></div>
            <el-form-item
              label="否認・差戻事由"
              prop="HininSashimodoshi_shiharai"
              class="is-required"
            >
              <div style="display: flex">
                <reafsinputtext
                  tabindex="56"
                  maxlength="100"
                  v-model="shiharaiForm.否認差戻事由_支払"
                  :disabled="画面入力制御disabled.否認差戻事由_支払"
                  :width="'680px'"
                  ref="ref否認差戻事由_支払"
                ></reafsinputtext>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="2">
              <div style="width: 40px; height: 1px"></div>
            </el-col>
            <el-col :span="2">
              <el-button
                tabindex="30"
                @click="onSeeAttachment"
                :disabled="画面入力制御disabled.btn帳票一覧"
                >帳票一覧</el-button
              >
              <screenPopup
                src="/Reafs_W/TyohyoIchiran"
                :params="paramsTyohyoIchiran"
                :dialogVisible.sync="showDialogTyohyoIchiran"
              ></screenPopup>
            </el-col>
            <el-col :span="13">
              <div style="width: 40px; height: 1px"></div>
            </el-col>
            <el-col :span="2">
              <el-button
                :loading="loading_否認"
                tabindex="35"
                :disabled="画面入力制御disabled.btn依頼否認"
                @click="onIraiHinin()"
                >否認</el-button
              >
            </el-col>
            <el-col :span="2">
              <el-button
                :loading="loading_差戻"
                tabindex="36"
                :disabled="画面入力制御disabled.btn依頼差戻"
                @click="onIraiSashimodoshi"
                >差戻</el-button
              >
            </el-col>
            <el-col :span="2">
              <el-button
                :loading="loading_承認"
                tabindex="37"
                :disabled="画面入力制御disabled.btn依頼承認"
                @click="onIraiShonin"
                >承認</el-button
              >
            </el-col>
            <el-col :span="1">
              <el-button
                tabindex="39"
                @click="onBack"
                :loading="loadingBack"
                style="float: right"
                >戻る</el-button
              >
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>
<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnumber from "@/components/basic/ReafsControl/ReafsInputNumber.vue";
import reafsmodal from "@/components/modal/Modal.vue";
import eigyoten from "@/components/basic/EigyoTen.vue";
import iraisha from "@/components/basic/Iraisha.vue";
import kojikubunsetsumeiichiran from "@/components/modal/KojiKubunSetsumeiIchiran.vue";
import opensearch from "@/components/basic/openSearch.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";
import reafstable from "@/components/table/Table.vue";
import screenPopup from "../../common/ScreenPopup/ScreenPopup.vue";

var $select工事区分value;
var $select大区分value;
var $select中区分value;
var $select小区分value;
var $select依頼状況value;
var $select中止取下げ区分value;
var $thisValue;

export default {
  activated() {
    if (localStorage.getItem("KeepAliveFlag") == 0 && this.isMounted == false) {
      localStorage.setItem("KeepAliveFlag", 1);
      this.initialize();
    }
    this.isMounted = false;
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.initialize(); // 初期化処理
      next();
    });
  },

  components: {
    reafsinputtext,
    reafsinputnumber,
    reafsmodal,
    eigyoten,
    iraisha,
    kojikubunsetsumeiichiran,
    opensearch,
    reafstable,
    nengetsu,
    screenPopup,
  },
  props: {
    showNo: {
      type: Boolean,
      default: true,
    },
    showCheck: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    var validate営業店コード = (rule, value, callback) => {
      if (this.iraiForm.店コード === "" || this.iraiForm.店コード枝番 === "") {
        this.getMsg("E040005").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validate工事区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.工事区分) {
        if (this.isInit === false) {
          if (value === "-") {
            this.getMsg("E040015").then((response) => {
              callback(new Error(response.data));
            });
          }
        }
      }
    };
    var validate大区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.大区分) {
        if (this.isInit === false) {
          if (value === "-") {
            this.getMsg("E040016").then((response) => {
              callback(new Error(response.data));
            });
          }
        }
      }
    };
    var validate中区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.中区分) {
        if (this.isInit === false) {
          if (value === "-") {
            this.getMsg("E040017").then((response) => {
              callback(new Error(response.data));
            });
          }
        }
      }
    };
    var validate小区分 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.小区分) {
        if (this.isInit === false) {
          if (value === "-") {
            this.getMsg("E040018").then((response) => {
              callback(new Error(response.data));
            });
          }
        }
      }
    };
    var validate依頼者コード = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E040019").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validate依頼者連絡先 = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E040669").then((response) => {
          callback(new Error(response.data));
        });
      } else if (!this.base.isSimpleByte(value)) {
        this.getMsg("E040460").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validate依頼者代理連絡先 = (rule, value, callback) => {
      if (value !== "" && !this.base.isSimpleByte(value)) {
        this.getMsg("E040460").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validate対象箇所 = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E040661").then((response) => {
          callback(new Error(response.data));
        });
      }
    };
    var validate依頼内容_状況 = (rule, value, callback) => {
      if (value === "") {
        this.getMsg("E040034").then((response) => {
          callback(new Error("                              " + response.data));
        });
      }
    };
    var validate依頼状況 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.依頼状況) {
        if (this.isInit === false) {
          if (
            value === "-" &&
            (this.iraiForm.工事区分 || "").charAt(0) !== "3"
          ) {
            this.getMsg("E040035").then((response) => {
              callback(new Error(response.data));
            });
          }
        }
      }
    };
    var validate中止取下げ年月日 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.中止取下げ年月日) {
        if (this.isInit === false) {
          if (
            (value || "") === "" &&
            (this.iraiForm.中止取下げ区分 === "1" ||
              this.iraiForm.中止取下げ区分 === "2")
          ) {
            this.getMsg("E040003").then((response) => {
              callback(new Error(response.data));
            });
          }
        }
      }
    };
    var validate中止取下げ事由 = (rule, value, callback) => {
      if (!this.画面入力制御disabled.中止取下げ事由) {
        if (
          value === "" &&
          (this.iraiForm.中止取下げ区分 === "1" ||
            this.iraiForm.中止取下げ区分 === "2")
        ) {
          this.getMsg("E040358").then((response) => {
            callback(new Error(response.data));
          });
        }
      }
    };
    return {
      isMounted: false,
      isInit: true,
      loading: false,
      loading_否認: false,
      loading_差戻: false,
      loading_承認: false,
      loadingBack: false,
      最終承認フラグ: "",
      修繕区分小: "",
      SGR担当事務所コード:"",
      SGR担当営業所コード:"",
      SGR担当部コード:"",
      SGR担当課コード:"",
      SGR担当係コード:"",
      userInfo: {
        ユーザーＩＤ: "",
        会社コード: "",
        UPDATE_HOST: "",
      },
      menuID: "B",
      subMenuID: "WD00530",
      menuOptions: {},
      form: {
        name: "",
        nameSearch: "",
        region: "",
        date1: "",
        date2: "",
        delivery: false,
        type: [],
        resource: "",
        desc: "",
      },
      searchForm: {
        KeiyakuNanber: "",
        KeiyakuNanberEdaban: "",
        KeiyakuNendo: "",
        keiaykuNoEdaban: "",
      },
      rules: {
        営業店コード: [{ validator: validate営業店コード, trigger: "blur" }],
        工事区分: [{ validator: validate工事区分, trigger: "change" }],
        大区分: [{ validator: validate大区分, trigger: "change" }],
        中区分: [{ validator: validate中区分, trigger: "change" }],
        小区分: [{ validator: validate小区分, trigger: "change" }],
        依頼者コード: [{ validator: validate依頼者コード, trigger: "blur" }],
        依頼者連絡先: [{ validator: validate依頼者連絡先, trigger: "blur" }],
        依頼者代理連絡先: [
          { validator: validate依頼者代理連絡先, trigger: "blur" },
        ],
        対象箇所: [{ validator: validate対象箇所, trigger: "blur" }],
        依頼内容_状況: [{ validator: validate依頼内容_状況, trigger: "blur" }],
        依頼状況: [{ validator: validate依頼状況, trigger: "change" }],
        中止取下げ年月日: [
          { validator: validate中止取下げ年月日, trigger: "change" },
        ],
        中止取下げ事由: [
          { validator: validate中止取下げ事由, trigger: "blur" },
        ],
        iraiNo: [
          {
            type: "number",
            message: "数値を入力してください",
            trigger: "blur",
          },
        ],
      },
      画面入力制御rules: {
        進捗: "",
        制御data: [
          [
            "    ",
            "No",
            "           ",
            "依頼入力済",
            "依頼入力済※1",
            "依頼取下",
            "依頼差戻",
            "-",
            "中止・取下げ時",
          ],
          [false, "1 ", "営業店コード", "〇", "×", "〇", "〇", "×", "×"],
          [false, "3 ", "依頼№", "〇", "x", "〇", "〇", "x", "x"],
          [false, "9 ", "工事区分", "〇", "×", "〇", "〇", "×", "×"],
          [false, "10", "大区分", "〇", "×", "〇", "〇", "×", "×"],
          [false, "11", "中区分", "〇", "×", "〇", "〇", "×", "×"],
          [false, "12", "小区分", "〇", "×", "〇", "〇", "×", "×"],
          [false, "14", "依頼者コード", "〇", "×", "〇", "〇", "×", "×"],
          [false, "18", "依頼者連絡先", "〇", "×", "〇", "〇", "×", "×"],
          [false, "19", "依頼者代理コード", "〇", "×", "〇", "〇", "×", "×"],
          [false, "23", "依頼者代理連絡先", "〇", "×", "〇", "〇", "×", "×"],
          [false, "24", "依頼コメント", "〇", "×", "〇", "〇", "×", "×"],
          [false, "25", "対象箇所", "〇", "×", "〇", "〇", "×", "×"],
          [false, "26", "メーカー名", "〇", "×", "〇", "〇", "×", "×"],
          [
            false,
            "27",
            "依頼内容／不具合状況",
            "〇",
            "×",
            "〇",
            "〇",
            "×",
            "×",
          ],
          [false, "28", "依頼状況", "〇", "×", "〇", "〇", "×", "×"],
          [
            false,
            "30",
            "中止取下げ区分",
            "〇",
            "×",
            "〇",
            "〇",
            "×",
            "△（※2）",
          ],
          [false, "31", "中止取下げ年月日", "×", "×", "×", "×", "×", "△（※2）"],
          [false, "32", "中止取下げ事由", "×", "×", "×", "×", "×", "△（※2）"],
        ],
        metaData: {
          colEnabled: 0,
          colItem: 2,
        },
        制御enabled: {},
      },
      画面入力制御disabled: {
        営業店コード: false,
        依頼番号: false,
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
        "依頼内容／不具合状況": false,
        依頼状況: false,
        中止取下げ区分: false,
        中止取下げ年月日: true,
        中止取下げ事由: true,
        見積作成日: false,
        見積番号: false,
        見積番号枝番: false,
        金額税抜計: false,
        金額税込計: false,
        消費税額: false,
        ＳＧＷ契約書番号: false,
        金額税抜計: false,
        金額税込計: false,
        消費税額: false,
        否認差戻事由: false,
        btn依頼否認: false,
        btn依頼差戻: false,
        btn依頼承認: false,
        btn添付登録参照: false,
        btn支払否認: false,
        btn支払差戻: false,
        btn支払承認: false,
        btn帳票一覧: false,
        btn契約書番号更新: false,
      },
      画面入力制御disabledOrg: {},
      showDialog: false,
      showDialogKojikubunsetsumeiichiran: false,
      activeIndex: "1",
      selectIndex: "1",
      oldData: {},
      iraiForm: {
        会社コード: "",
        営業店コード: "",
        店コード: "",
        店コード枝番: "",
        営業店名: "",
        依頼番号: "",
        契約番号: "",
        履歴no: 0,
        契約年度: "",
        物件コード: "",
        物件名: "",
        物件区分: "",
        物件住所Cd: "",
        物件住所Nm: "",
        工事区分: "",
        大区分: "",
        中区分: "",
        小区分: "",
        依頼者コード: "",
        依頼者名: "",
        依頼者所属部署名: "",
        依頼者役職名: "",
        依頼者連絡先: "",
        依頼者代理コード: "",
        依頼者代理名: "",
        依頼者代理所属部署名: "",
        依頼者代理連絡先: "",
        依頼コメント: "",
        対象箇所: "",
        メーカー名: "",
        依頼内容_状況: "",
        依頼状況: "",
        中止取下げ区分: "",
        中止取下げ年月日: "",
        中止取下げ事由: "",
        部位コメント1: "",
        部位コメント2: "",
        部位コメント3: "",
        部位コメント4: "",
        進捗コード_本体: "",
        "依頼入力済※1": "",
        依頼承認チェック: "",
        定期見積ヘッダ: "",
        依頼No: "",
        見積作成日: "",
        見積番号: "",
        見積番号枝番: "",
        ＳＧＷ契約書番号: "",
        金額税抜計: "",
        金額税込計: "",
        消費税額: "",
        否認差戻事由: "",
        最終承認フラグ: "",
      },
      shiharaiForm: {
        実施月: "",
        実施月_元: "",
        明細no: 0,
        作業名称: "",
        状態: "",
        進捗_本体: "",
        進捗_発注: "",
        取引先: "",
        作業担当者: "",
        担当者連絡先: "",
        作業完了日_FROM: "",
        作業完了日_TO: "",
        完了報告: "",
        費用負担部門コード: "",
        費用負担部門名: "",
        事業領域コード: "",
        事業領域名: "",
        外内販区分コード: "",
        外内販区分名: "",
        税率: "",
        税込支払金額: 0,
        支払予定日: "",
        完了確認日: "",
        完了確認者: "",
        点検書類待ち: "",
        点検書類待ち_解消日: "",
        完了報告書: "",
        完了報告書_解消日: "",
        請求書: "",
        請求書_解消日: "",
        否認差戻事由_支払: "",
        支払承認チェック: "",
      },
      hikisu: {
        契約NO: "",
        履歴NO: 0,
        明細NO: 0,
        契約年月: "",
        契約年度: "",
        page: "",
      },
      paramsTyohyoIchiran: {
        工事依頼No: "",
        契約No: "",
        契約履歴No: "",
        見積明細No: "",
        契約年月: "",
        予定年月: "",
      },
      paramsIraiTenpuTouroku: {
        添付種類: "901",
        工事依頼No: "",
      },
      showDialogTyohyoIchiran: false,
      showDialogIraiTenpuTouroku: false,
      iraiFormInit: {},
      iraiFormSearched: {},
      工事区分value: [],
      大区分value: [],
      中区分value: [],
      小区分value: [],
      依頼状況value: [],
      中止取下げ区分value: [],
      名称取得方法17: {},

      ///////////////////////////////////////////////
      // 見積テーブル
      ///////////////////////////////////////////////
      tblMitumoriData: [],
      tblMitumoriColumns: [
        {
          field: "tblBunrui",
          title: "分類",
          type: "string",
          width: 80,
          align: "center",
          sort: true,
        },
        {
          field: "tblDaiTyuShoKubun",
          title: "大中小区分",
          type: "string",
          width: 300,
          align: "center",
          sort: true,
        },
        {
          field: "tblSagyoMeisho",
          title: "作業名称",
          type: "string",
          width: 300,
          align: "center",
          sort: true,
        },
        {
          field: "tblTorihikiSaki",
          title: "取引先",
          type: "string",
          width: 250,
          align: "center",
          sort: true,
        },
        {
          field: "tblSuuryo",
          title: "数量",
          type: "string",
          width: 100,
          align: "center",
          sort: true,
        },
        {
          field: "tblTani",
          title: "単位",
          type: "string",
          width: 100,
          align: "center",
          sort: true,
        },
        {
          title: "金額",
          align: "right",
          children: [
            {
              title: "単価",
              width: 150,
              align: "right",
              render: (h, { row, column, index }) => {
                return h("div", { style: {} }, [h("p", row.tblTanka)]);
              },
            },
            {
              title: "金額(税抜)",
              align: "right",
              width: 200,
              render: (h, { row, column, index }) => {
                return h("div", { style: {} }, [h("p", row.tblKingaku)]);
              },
            },
            {
              title: "税区分",
              width: 100,
              align: "center",
              render: (h, { row, column, index }) => {
                return h("div", { style: {} }, [h("p", row.tblZeiKubun)]);
              },
            },
            {
              title: "税率",
              width: 50,
              align: "center",
              render: (h, { row, column, index }) => {
                return h("div", { style: {} }, [h("p", row.tblZeiRitu)]);
              },
            },
          ],
        },
      ],

      ///////////////////////////////////////////////
      // 契約内容テーブル
      ///////////////////////////////////////////////
      tblKeiyakuData: [],
      tblKeiyakuDataGoukei: [],
      tblKeiyakuColumns: [
        {
          field: "tblSgyoKoumoku",
          title: "",
          type: "string",
          width: 120,
          align: "center",
        },
        {
          field: "tblMonth04",
          title: "４月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth05",
          title: "５月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth06",
          title: "６月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth07",
          title: "７月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth08",
          title: "８月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth09",
          title: "９月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth10",
          title: "１０月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth11",
          title: "１１月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth12",
          title: "１２月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth01",
          title: "１月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth02",
          title: "２月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblMonth03",
          title: "３月",
          type: "string",
          width: 120,
          align: "right",
          sort: true,
        },
        {
          field: "tblNenngaku",
          title: "年額",
          type: "Number",
          width: 120,
          align: "right",
          sort: true,
        },
      ],
      ///////////////////////////////////////////////
      // 作業結果/支払テーブル
      ///////////////////////////////////////////////
      tblShiharaiData: [],
      作業名称value: [],

      ///////////////////////////////////////////////
      // エラーフラグ
      ///////////////////////////////////////////////
      isErrPinkKeiyakuNo: false,
      isErrPinkHininJiyu: false,
      isErrPinkSashimodoshiJiyu: false,
      isErrPinkSyonin: false,
      isErrPinkKeiyakusyoBango: false,
      isErrPinkSagyotsuki:false,
    };
  },
  computed: {
    searchData() {
      return {
        ログイン会社コード: this.userInfo.会社コード,
        契約No: this.searchForm.KeiyakuNanber,
        契約No枝番: this.searchForm.KeiyakuNanberEdaban,
        契約年度: this.searchForm.KeiyakuNendo,
      };
    },
    isUpdate() {
      return this.iraiForm.依頼番号.trim();
    },
  },
  inject: ["setPageTitle"],
  created() {
    this.init();
    $select工事区分value = this;
    $select大区分value = this;
    $select中区分value = this;
    $select小区分value = this;
    $select依頼状況value = this;
    $select中止取下げ区分value = this;
  },
  mounted() {
    this.setPageTitle(this.menuOptions.SubMenuName);
    this.fnc_void_clearData();
  },

  methods: {
    initialize() {
      this.isMounted = true;
      this.getPageTitleByMenu();
      this.hikisu.契約NO = this.$route.query.契約NO;
      this.hikisu.履歴NO = this.$route.query.履歴NO;
      this.hikisu.明細NO = this.$route.query.明細NO;
      this.hikisu.契約年月 = this.$route.query.契約年月;
      this.hikisu.契約年度 = this.$route.query.契約年度;
      this.page = this.$route.query.page;

      // 検索処理起動
      if (this.hikisu.契約NO != "") {
        this.searchForm.KeiyakuNanber = this.hikisu.契約NO.substring(0, 8);
        this.searchForm.KeiyakuNanberEdaban = this.hikisu.契約NO.substring(8);
        this.searchForm.KeiyakuNendo = this.hikisu.契約年度;
        this.iraiForm.履歴no = this.hikisu.履歴NO;

        this.onSearch();
        //起動時のみの検索※対象データがなくてもメッセージ出さない
        this.shiharaiForm.実施月 = this.$route.query.契約年月;
        this.shiharaiForm.作業名称 = parseInt(this.$route.query.明細NO);
        this.kidouSiharaiSearch();
      }
    },

    getPageTitleByMenu() {
      let menuInfo = this.$store.getters.getMenuInfo();
      const menuOptions = menuInfo.find((item) => {
        return item.MenuId === this.menuID && item.SubMenuId === this.subMenuID;
      });
      Object.assign(this.menuOptions, menuOptions);
      this.menuOptions.SubMenuName = "[定期] 依頼参照/承認";
    },

    afterInit() {
      Object.assign(this.画面入力制御disabledOrg, this.画面入力制御disabled);
      Object.assign(this.iraiFormInit, this.iraiForm);
    },
    reInit() {
      let temp画面入力制御disabled = {};
      let tempiraiForm = {};

      this.isInit = true;
      Object.assign(temp画面入力制御disabled, this.画面入力制御disabledOrg);
      this.画面入力制御disabled = temp画面入力制御disabled;

      Object.assign(tempiraiForm, this.iraiFormInit);
      this.iraiForm = tempiraiForm;
    },
    init() {
      let userLogIn = this.$store.getters.getUserInfo();
      this.userInfo.ユーザーＩＤ = userLogIn.userId;
      this.userInfo.会社コード = userLogIn.会社コード;

      // Get Dropdown
      this.http
        .get(
          "/api/Reafs_W/Shonin/WD00530/GetStartInfo",
          "アクセスしています...."
        )
        .then((response) => {
          // Get Dropdown工事区分
          $select工事区分value.工事区分value = response.data.名称取得方法07;
          this.iraiForm.工事区分 = "-";
          // Get Dropdown大区分
          $select大区分value.大区分value = response.data.名称取得方法08;
          this.iraiForm.大区分 = "-";
          this.iraiForm.中区分 = "-";
          this.iraiForm.小区分 = "-";
          // Get Dropdown依頼状況
          $select依頼状況value.依頼状況value = response.data.名称取得方法13;
          this.iraiForm.依頼状況 = "-";
          // Get Dropdown中止区分
          $select中止取下げ区分value.中止取下げ区分value =
            response.data.名称取得方法15;
          this.名称取得方法17 = response.data.名称取得方法17;
          this.iraiForm.中止取下げ区分 = "0";
          this.$nextTick(() => {
            this.isInit = false;
          });
          this.afterInit();
          const pageData = this.$store.getters.getPageData();
          if (
            pageData &&
            pageData.searchForm &&
            pageData.searchForm.KeiyakuNanber &&
            pageData.契約No &&
            pageData.searchForm.KeiyakuNanberEdaban &&
            pageData.契約No枝番 &&
            pageData.searchForm.KeiyakuNendo &&
            pageData.契約年度
          ) {
            this.searchForm.KeiyakuNanber = pageData.searchForm.KeiyakuNanber;
            this.searchForm.KeiyakuNanberEdaban =
              pageData.searchForm.KeiyakuNanberEdaban;
            this.searchForm.KeiyakuNendo = pageData.searchForm.KeiyakuNendo;
            this.onSearch();
          }
        });

      this.画面入力制御disabled.btn依頼否認 = true;
      this.画面入力制御disabled.btn依頼差戻 = true;
      this.画面入力制御disabled.btn依頼承認 = true;
      this.画面入力制御disabled.btn添付登録参照 = true;
      this.画面入力制御disabled.btn帳票一覧 = true;
    },
    showSuccess(message, title = this.menuOptions.SubMenuName) {
      var self = this;
      this.MsgInfo({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading();
        },
      });
    },
    showError(
      message,
      option = null,
      title = this.$store.getters.getPageTitle()
    ) {
      var self = this;
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading();
          if (option && typeof option.callback === "function") {
            option.callback();
          }
        },
      });
      // this.$message.error({
      //   message,
      //   onClose: this.hideLoading
      // })
    },

    hideLoading() {
      this.loading = false;
      this.loading_差戻 = false;
      this.loading_承認 = false;
      this.loading_否認 = false;
      this.loadingBack = false;
    },

    confirm(message, title = this.menuOptions.SubMenuName) {
      return new Promise((resolve, reject) => {
        this.MsgConf({
          title,
          message,
          callback: function (a, b) {
            if (a === "confirm") {
              resolve(true);
            } else {
              resolve(false);
            }
          },
        });
      });
    },
    set画面入力制御(isScreenDisplayTiming = true) {
      const 進捗コード_本体 = this.iraiForm.進捗コード_本体;
      const 中止取下げ区分 = this.iraiForm.中止取下げ区分;
      const s進捗内容 = this.get進捗(中止取下げ区分, 進捗コード_本体);
      this.画面入力制御rules.進捗 = s進捗内容;
      const col = this.画面入力制御rules.制御data[0].findIndex(
        (e) => e === s進捗内容
      );
      for (
        let index = 1;
        index < this.画面入力制御rules.制御data.length;
        index++
      ) {
        const element = this.画面入力制御rules.制御data[index];
        if (element[col] === "△（※2）") {
          element[0] = this.get進捗Condition2(isScreenDisplayTiming);
        } else {
          element[0] = element[col] !== "×";
        }
        this.画面入力制御rules.制御enabled[
          element[this.画面入力制御rules.metaData.colItem]
        ] = element[this.画面入力制御rules.metaData.colEnabled];
      }

      for (const item in this.画面入力制御disabled) {
        if (this.画面入力制御rules.制御enabled.hasOwnProperty(item)) {
          // mapping enabled to disabled
          this.画面入力制御disabled[item] =
            !this.画面入力制御rules.制御enabled[item];
        }
      }
      if (isScreenDisplayTiming) {
        for (const item in this.画面入力制御disabledOrg) {
          if (this.画面入力制御rules.制御enabled.hasOwnProperty(item)) {
            // mapping enabled to disabled
            this.画面入力制御disabledOrg[item] =
              !this.画面入力制御rules.制御enabled[item];
          }
        }
      }

      if (this.iraiForm.工事区分 && this.iraiForm.工事区分 !== "-") {
        this.selectKojiKubun(this.iraiForm.工事区分);
      }
    },
    get進捗(中止取下げ区分, 進捗コード_本体, 進捗コード_発注) {
      let s進捗内容 = "";
      if (中止取下げ区分 === "1" || 中止取下げ区分 === "2") {
        s進捗内容 = "中止・取下げ時";
      } else if (進捗コード_本体 === "010") {
        s進捗内容 = "依頼入力済";
        const condition1 = this.iraiForm["依頼入力済※1"];
        if (condition1 === true) {
          s進捗内容 = "依頼入力済※1";
        }
      } else if (進捗コード_本体 === "017") {
        s進捗内容 = "依頼取下";
      } else if (進捗コード_本体 === "018") {
        s進捗内容 = "依頼差戻";
      } else if (進捗コード_本体 >= "020") {
        s進捗内容 = "－";
      }

      return s進捗内容;
    },
    get進捗Condition2(isScreenDisplayTiming) {
      return !(isScreenDisplayTiming && this.iraiForm.中止取下げ区分 === "2");
    },
    checkDspbtn() {
      //支払承認のデータが取得されている場合は戻る
      if (this.tblShiharaiData !== null) {
        return;
      }

      const checkDspbtn承認 = this.iraiForm.依頼承認チェック;
      if (checkDspbtn承認 === true) {
        this.画面入力制御disabled.btn依頼否認 = false;
        this.画面入力制御disabled.btn依頼差戻 = false;
        this.画面入力制御disabled.btn依頼承認 = false;
      } else {
        this.画面入力制御disabled.btn依頼否認 = true;
        this.画面入力制御disabled.btn依頼差戻 = true;
        this.画面入力制御disabled.btn依頼承認 = true;
      }

      this.画面入力制御disabled.btn添付登録参照 = false;
      this.画面入力制御disabled.btn帳票一覧 = false;
    },
    checkDspbtn_支払() {
      if (this.tblShiharaiData === null) {
        return;
      }

      const checkDspbtn承認 = this.shiharaiForm.支払承認チェック;
      if (checkDspbtn承認 === true) {
        this.画面入力制御disabled.btn依頼否認 = false;
        this.画面入力制御disabled.btn依頼差戻 = false;
        this.画面入力制御disabled.btn依頼承認 = false;
      } else {
        this.画面入力制御disabled.btn依頼否認 = true;
        this.画面入力制御disabled.btn依頼差戻 = true;
        this.画面入力制御disabled.btn依頼承認 = true;
      }

      this.画面入力制御disabled.btn添付登録参照 = false;
      this.画面入力制御disabled.btn帳票一覧 = false;

      // if (this.selectIndex === 4){
      //   this.activeIndex = '4'
      // }{
      //   this.activeIndex = this.selectIndex
      // }
    },

    async onSearch() {
      console.log("検索実行");
      this.loading = true;
      this.savePageSearchData();

      this.selectIndex = 1;
      this.activeIndex = 1;

      if (this.checkKeiyakuNanber() === false) {
        this.$refs.ref契約番号.focus();
        this.isErrPinkKeiyakuNo = true;
        return false;
      }

      let checkExist = false;
      await this.http
        .get("/api/Reafs_W/Shonin/WD00530/CheckF011Exist", {
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
        })
        .then((response) => {
          checkExist = response.data.isF011Exist;
        });

      if (!checkExist) {
        this.getMsg("E000014").then((response) => {

          this.showError(response.data);
        });
        this.$refs.KeiyakuNanber.focus();
        return false;
      }

      var getArrayValue = (arr, index, key) => {
        if (!arr || !arr[index] || !arr[index][key]) {
          return "";
        }
        return arr[index][key];
      };
      var getObjectValue = (obj, index) => {
        index = index || 0;
        return obj ? obj[Object.keys(obj)[index]] : "";
      };

      await this.http
        .get(
          "/api/Reafs_W/Shonin/WD00530/GetPageData",
          {
            ログイン会社コード: this.userInfo.会社コード,
            契約No: this.searchForm.KeiyakuNanber,
            契約No枝番: this.searchForm.KeiyakuNanberEdaban,
            契約年度: this.searchForm.KeiyakuNendo,
            履歴no: this.hikisu.履歴NO,
            依頼no: this.iraiForm.依頼No,
          },
          "アクセスしています...."
        )
        .then((response) => {
          this.isInit = true;
          this.loading = false;

          this.最終承認フラグ = response.data.最終承認フラグ;

          // Get Dropdown工事区分
          $select工事区分value.工事区分value = response.data.名称取得方法07;
          // Get Dropdown大区分
          $select大区分value.大区分value = response.data.名称取得方法08;
          // Get Dropdown中区分
          $select中区分value.中区分value = response.data.名称取得方法09;
          // Get Dropdown小区分
          $select小区分value.小区分value = response.data.名称取得方法10;
          // Get Dropdown依頼状況
          $select依頼状況value.依頼状況value = response.data.名称取得方法13;
          // Get Dropdown中止取下げ区分
          $select中止取下げ区分value.中止取下げ区分value =
            response.data.名称取得方法15;

          //メール用
          this.SGR担当事務所コード  = response.data.SGR担当事務所コード;
          this.SGR担当営業所コード  = response.data.SGR担当営業所コード;
          this.SGR担当部コード  = response.data.SGR担当部コード;
          this.SGR担当課コード  = response.data.SGR担当課コード;
          this.SGR担当係コード  = response.data.SGR担当係コード;
          console.log(this.SGR担当事務所コード);
          console.log(this.SGR担当営業所コード);
          console.log(this.SGR担当部コード);
          console.log(this.SGR担当課コード);
          console.log(this.SGR担当係コード);

          this.oldData = JSON.parse(JSON.stringify(response.data));
          response.data.対象箇所 = response.data.不具合場所;
          response.data.中止取下げ区分 = response.data.継続区分;
          response.data.中止取下げ年月日 = response.data.中止年月日;
          if (response.data.工事区分.trim() === "") {
            response.data.工事区分 = "-";
          }
          if (response.data.大区分.trim() === "") {
            response.data.大区分 = "-";
          }
          if (response.data.中区分.trim() === "") {
            response.data.中区分 = "-";
          }
          if (response.data.小区分.trim() === "") {
            response.data.小区分 = "-";
          }
          if (response.data.依頼状況.trim() === "") {
            response.data.依頼状況 = "-";
          }
          if (response.data.中止取下げ区分.trim() === "") {
            response.data.中止取下げ区分 = "0";
          }

          // response.data.否認差戻事由 = response.data.F042_否認差戻事由取得結果;
          this.iraiForm = response.data;

          this.iraiForm.部位コメント1 = getArrayValue(
            response.data.部位コメント,
            0,
            "部位コメント"
          );
          this.iraiForm.部位コメント2 = getArrayValue(
            response.data.部位コメント,
            1,
            "部位コメント"
          );
          this.iraiForm.部位コメント3 = getArrayValue(
            response.data.部位コメント,
            2,
            "部位コメント"
          );
          this.iraiForm.部位コメント4 = getArrayValue(
            response.data.部位コメント,
            3,
            "部位コメント"
          );

          this.iraiForm["依頼入力済※1"] = response.data.入力制御FW042;

          this.iraiForm.iraiNo = response.data.依頼番号;
          this.iraiForm.依頼承認チェック = response.data.依頼承認チェック;

          ///////////////////////////////////////////////
          // 契約書内容
          ///////////////////////////////////////////////
          this.iraiForm.見積作成日 =
            response.data.F017_定期見積ヘッダ_見積作成日;
          this.iraiForm.見積番号 = response.data.F017_定期見積ヘッダ_契約No;
          this.iraiForm.見積番号枝番 =
            response.data.F017_定期見積ヘッダ_契約No枝番;

          this.iraiForm.金額税抜計 =
            response.data.F018_定期見積トレーラ_合計_売上金額税抜;
          this.iraiForm.金額税込計 =
            response.data.F018_定期見積トレーラ_合計_売上金額税込;
          this.iraiForm.消費税額 =
            response.data.F018_定期見積トレーラ_合計_売上消費税額;

          this.iraiForm.否認差戻事由 = response.data.F042_否認差戻事由取得結果;

          ///////////////////////////////////////////////
          // 契約書内容
          ///////////////////////////////////////////////
          this.iraiForm.ＳＧＷ契約書番号 =
            response.data.F011_契約書番号取得結果;
          this.修繕区分小 = response.data.F011_修繕区分小取得結果;
          console.log("契約書内容タブ色々制御");
          if (
            response.data.F011_修繕区分小取得結果 === "08" &&
            response.data.F011_負担区分取得結果 === "2"
          ) {
            this.画面入力制御disabled.ＳＧＷ契約書番号 = false;
            this.画面入力制御disabled.btn契約書番号更新 = false;
          } else {
            this.画面入力制御disabled.ＳＧＷ契約書番号 = true;
            this.画面入力制御disabled.btn契約書番号更新 = true;
          }
          let strBuff = response.data.F011_初回契約開始年月取得結果;
          this.iraiForm.初回契約開始年月_年 = strBuff.substring(0, 4);
          this.iraiForm.初回契約開始年月_月 = strBuff.substring(
            strBuff.length - 2
          );
          strBuff = response.data.F011_契約開始年月取得結果;
          if (
            this.iraiForm.初回契約開始年月_年 !== strBuff.substring(0, 4) ||
            this.iraiForm.初回契約開始年月_月 !==
              strBuff.substring(strBuff.length - 2)
          ) {
            this.iraiForm.変更契約開始年月_年 = strBuff.substring(0, 4);
            this.iraiForm.変更契約開始年月_月 = strBuff.substring(
              strBuff.length - 2
            );
          }
          strBuff = response.data.F011_契約終了年月取得結果;
          this.iraiForm.契約終了年月_年 = strBuff.substring(0, 4);
          this.iraiForm.契約終了年月_月 = strBuff.substring(strBuff.length - 2);
        });

      await this.fnc_void_getKensakuKekka_F018();

      //修繕区分小により取得データを変更？
      if (this.修繕区分小 === "08") {
        //直接
        await this.fnc_void_getKensakuKekka_F013();
        await this.fnc_void_getKensakuKekka_F013合計();
      } else {
        //請負
        await this.fnc_void_getKensakuKekka_F012();
        await this.fnc_void_getKensakuKekka_F012合計();
      }

      await this.fnc_void_getKensakuKekka_支払リスト();

      Object.assign(this.iraiFormSearched, this.iraiForm);

      await this.set画面入力制御(true);
      await this.checkDspbtn();
      await this.$nextTick(() => {
        this.isInit = false;
      });
      await this.$refs.refIraiForm.clearValidate();
      await this.savePageSearchData();

      if (this.selectIndex === 4) {
        this.activeIndex = "4";
      } else {
        this.activeIndex = "1";
      }
    },
    ///////////////////////////////////////////////
    // F018：定期見積トレーラ情報取得
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka_F018() {
      // 検索実行
      let _this = this;

      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F018", {
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          履歴no: this.hikisu.履歴NO,
          修繕区分小: this.修繕区分小,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.tblMitumoriData = response.data;
        });
    },
    ///////////////////////////////////////////////
    // F012：見積請求予定マスタ情報取得
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka_F012() {
      // 検索実行
      let _this = this;
      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F012", {
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          履歴no: this.hikisu.履歴NO,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.tblKeiyakuData = response.data;
        });
    },
    ///////////////////////////////////////////////
    // F012：見積請求予定マスタ情報取得
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka_F012合計() {
      // 検索実行
      let _this = this;
      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F012合計", {
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          履歴no: this.hikisu.履歴NO,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.tblKeiyakuDataGoukei = response.data;
        });
    },
    ///////////////////////////////////////////////
    // F013：見積原価予定マスタ情報取得
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka_F013() {
      // 検索実行
      let _this = this;
      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F013", {
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          履歴no: this.hikisu.履歴NO,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.tblKeiyakuData = response.data;
        });
    },
    ///////////////////////////////////////////////
    // F013：見積原価予定マスタ情報取得
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka_F013合計() {
      // 検索実行
      let _this = this;

      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F013合計", {
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          履歴no: this.hikisu.履歴NO,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.tblKeiyakuDataGoukei = response.data;
        });
    },
    // 戻すデータ
    reafsmodalBackEigyoten(obj) {
      this.iraiForm.eigyotenCd = obj.eigyotenCd;
      this.iraiForm.eigyotenBango = obj.eigyotenBango;
      this.iraiForm.営業店名 = obj.eigyotenNm;
      if (
        this.iraiForm.eigyotenCd == "" ||
        this.iraiForm.eigyotenCd.trim() == "" ||
        this.iraiForm.eigyotenBango == "" ||
        this.iraiForm.eigyotenBango.trim() == ""
      ) {
        this.iraiForm.物件コード = "";
        this.iraiForm.物件名 = "";
        this.iraiForm.物件区分 = "";
        this.iraiForm.物件住所Cd = "";
        this.iraiForm.物件住所Nm = "";
      } else {
        const テナント営業所コード =
          this.iraiForm.eigyotenCd + this.iraiForm.eigyotenBango;
        this.http
          .get("/api/Reafs_W/Shonin/WD00530/Get物件コードInfo", {
            ログイン会社コード: this.userInfo.会社コード,
            テナント営業所コード,
          })
          .then((response) => {
            if (response.data !== null) {
              this.iraiForm.物件コード = response.data.物件コード;
              this.iraiForm.物件名 = response.data.物件名;
              this.iraiForm.物件区分 = response.data.物件区分;
              this.iraiForm.物件住所Cd = response.data.物件住所Cd;
              this.iraiForm.物件住所Nm = response.data.物件住所Nm;
            } else {
              this.iraiForm.物件コード = "";
              this.iraiForm.物件名 = "";
              this.iraiForm.物件区分 = "";
              this.iraiForm.物件住所Cd = "";
              this.iraiForm.物件住所Nm = "";
            }
          });
      }
    },
    ///////////////////////////////////////////////
    // 作業結果/支払タブの情報セット
    ///////////////////////////////////////////////
    async onSearchShiharai() {
      // 検索実行
      let _this = this;
      console.log(this.shiharaiForm.実施月);
      let letYear = new Object();
      let letMonth = new Object();
      let letNengetsu = new Object();

      letYear = this.shiharaiForm.実施月.getFullYear();
      letMonth = this.shiharaiForm.実施月.getMonth() + 1;
      letMonth = ("00" + letMonth).slice(-2);
      letNengetsu = letYear + "/" + letMonth;
      console.log(letNengetsu);

      await this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_支払", {
          会社コード: this.userInfo.会社コード,
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          契約年月: letNengetsu,
          明細NO: this.shiharaiForm.作業名称,
          履歴no: this.hikisu.履歴NO,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.tblShiharaiData = response.data;
          _this.shiharaiForm.支払承認チェック = response.data.支払承認チェック;

          //画面表示※コンボボックス切り替わった際の処理に飛ぶ予定
          _this.selectSagyomeisyo();
        });
      this.checkDspbtn_支払();
    },

    ///////////////////////////////////////////////
    // 作業結果/支払タブの情報セット※画面起動時
    ///////////////////////////////////////////////
    async kidouSiharaiSearch() {
      // 検索実行
      let _this = this;
      await this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_支払", {
          会社コード: this.userInfo.会社コード,
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          契約年月: this.shiharaiForm.実施月,
          明細NO: this.shiharaiForm.作業名称,
          履歴no: this.hikisu.履歴NO,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.tblShiharaiData = response.data;
          _this.shiharaiForm.支払承認チェック = response.data.支払承認チェック;

          //画面表示※コンボボックス切り替わった際の処理に飛ぶ予定
          _this.selectSagyomeisyo();

          //検索結果がある場合は、作業結果/支払タブを初期表示
          _this.selectIndex = 4;
        });
      this.checkDspbtn_支払();
    },
    ///////////////////////////////////////////////
    // 作業結果/支払タブの情報セット
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka_支払リスト() {
      // 検索実行
      let _this = this;

      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_支払リスト", {
          契約No: this.searchForm.KeiyakuNanber,
          契約No枝番: this.searchForm.KeiyakuNanberEdaban,
          契約年度: this.searchForm.KeiyakuNendo,
          履歴no: this.hikisu.履歴NO,
        })
        // 検索実行後処理
        .then(function (response) {
          _this.作業名称value = response.data;
        });
    },

    reafsmodalBackIraiSha(obj) {
      this.iraiForm.依頼者コード = obj.torihikisakicd;
      this.iraiForm.依頼者名 = obj.torihikisakinm;

      if (
        this.iraiForm.依頼者コード == "" ||
        this.iraiForm.依頼者コード.trim() == ""
      ) {
        this.iraiForm.依頼者所属部署名 = "";
        this.iraiForm.依頼者役職名 = "";

        this.iraiForm.依頼者事務所コード = "";
        this.iraiForm.依頼者営業所コード = "";
        this.iraiForm.依頼者部コード = "";
        this.iraiForm.依頼者課コード = "";
        this.iraiForm.依頼者係コード = "";
        this.iraiForm.依頼者役職コード = "";
      } else {
        this.http
          .get("/api/Reafs_W/Shonin/WD00530/GetDataN011_12", {
            社員コード: this.iraiForm.依頼者コード,
          })
          .then((response) => {
            if (response.data !== null) {
              if (response.data.名称取得方法11 !== null) {
                this.iraiForm.依頼者名 = response.data.名称取得方法11.社員名;
              } else {
                this.iraiForm.依頼者名 = "";
                this.iraiForm.依頼者所属部署名 = "";
                this.iraiForm.依頼者役職名 = "";

                this.iraiForm.依頼者事務所コード = "";
                this.iraiForm.依頼者営業所コード = "";
                this.iraiForm.依頼者部コード = "";
                this.iraiForm.依頼者課コード = "";
                this.iraiForm.依頼者係コード = "";
                this.iraiForm.依頼者役職コード = "";
                this.getMsg("E040020").then((response) => {
                  this.showError(response.data, {
                    callback: () => {
                      this.$refs.ref依頼者コード.focus();
                    },
                  });
                });
                return false;
              }
              if (response.data.名称取得方法12 !== null) {
                this.iraiForm.依頼者所属部署名 =
                  response.data.名称取得方法12.組織名;
                this.iraiForm.依頼者役職名 =
                  response.data.名称取得方法12.役職名;

                this.iraiForm.依頼者事務所コード =
                  response.data.名称取得方法12.事務所コード;
                this.iraiForm.依頼者営業所コード =
                  response.data.名称取得方法12.営業所コード;
                this.iraiForm.依頼者部コード =
                  response.data.名称取得方法12.部コード;
                this.iraiForm.依頼者課コード =
                  response.data.名称取得方法12.課コード;
                this.iraiForm.依頼者係コード =
                  response.data.名称取得方法12.係コード;
                this.iraiForm.依頼者役職コード =
                  response.data.名称取得方法12.役職コード;
              } else {
                this.iraiForm.依頼者所属部署名 = "";
                this.iraiForm.依頼者役職名 = "";

                this.iraiForm.依頼者事務所コード = "";
                this.iraiForm.依頼者営業所コード = "";
                this.iraiForm.依頼者部コード = "";
                this.iraiForm.依頼者課コード = "";
                this.iraiForm.依頼者係コード = "";
                this.iraiForm.依頼者役職コード = "";
              }
            } else {
              this.iraiForm.依頼者名 = "";
              this.iraiForm.依頼者所属部署名 = "";
              this.iraiForm.依頼者役職名 = "";

              this.iraiForm.依頼者事務所コード = "";
              this.iraiForm.依頼者営業所コード = "";
              this.iraiForm.依頼者部コード = "";
              this.iraiForm.依頼者課コード = "";
              this.iraiForm.依頼者係コード = "";
              this.iraiForm.依頼者役職コード = "";
            }
          });
      }
    },
    reafsmodalBackIraiShadairi() {
      this.iraiForm.依頼者代理コード = obj.torihikisakicd;
      this.iraiForm.依頼者代理名 = obj.torihikisakinm;

      if (
        this.iraiForm.依頼者代理コード == "" ||
        this.iraiForm.依頼者代理コード.trim() == ""
      ) {
        this.iraiForm.依頼者代理所属部署名 = "";
        this.iraiForm.依頼者代理役職名 = "";

        this.iraiForm.依頼者代理事務所コード = "";
        this.iraiForm.依頼者代理営業所コード = "";
        this.iraiForm.依頼者代理部コード = "";
        this.iraiForm.依頼者代理課コード = "";
        this.iraiForm.依頼者代理係コード = "";
        this.iraiForm.依頼者代理役職コード = "";
      } else {
        this.http
          .get("/api/Reafs_W/Shonin/WD00530/GetDataN011_12", {
            社員コード: this.iraiForm.依頼者コード,
          })
          .then((response) => {
            if (response.data !== null) {
              if (response.data.名称取得方法11 !== null) {
                this.iraiForm.依頼者代理名 =
                  response.data.名称取得方法11.社員名;
              } else {
                this.iraiForm.依頼者代理名 = "";
                this.iraiForm.依頼者代理所属部署名 = "";
                this.iraiForm.依頼者代理役職名 = "";

                this.iraiForm.依頼者代理事務所コード = "";
                this.iraiForm.依頼者代理営業所コード = "";
                this.iraiForm.依頼者代理部コード = "";
                this.iraiForm.依頼者代理課コード = "";
                this.iraiForm.依頼者代理係コード = "";
                this.iraiForm.依頼者代理役職コード = "";
                this.getMsg("E040020").then((response) => {
                  this.showError(response.data, {
                    callback: () => {
                      this.$refs.ref依頼者代理コード.focus();
                    },
                  });
                });
                return false;
              }
              if (response.data.名称取得方法12 !== null) {
                this.iraiForm.依頼者代理所属部署名 =
                  response.data.名称取得方法12.組織名;
                this.iraiForm.依頼者代理役職名 =
                  response.data.名称取得方法12.役職名;

                this.iraiForm.依頼者代理事務所コード =
                  response.data.名称取得方法12.事務所コード;
                this.iraiForm.依頼者代理営業所コード =
                  response.data.名称取得方法12.営業所コード;
                this.iraiForm.依頼者代理部コード =
                  response.data.名称取得方法12.部コード;
                this.iraiForm.依頼者代理課コード =
                  response.data.名称取得方法12.課コード;
                this.iraiForm.依頼者代理係コード =
                  response.data.名称取得方法12.係コード;
                this.iraiForm.依頼者代理役職コード =
                  response.data.名称取得方法12.役職コード;
              } else {
                this.iraiForm.依頼者代理所属部署名 = "";
                this.iraiForm.依頼者代理役職名 = "";

                this.iraiForm.依頼者代理事務所コード = "";
                this.iraiForm.依頼者代理営業所コード = "";
                this.iraiForm.依頼者代理部コード = "";
                this.iraiForm.依頼者代理課コード = "";
                this.iraiForm.依頼者代理係コード = "";
                this.iraiForm.依頼者代理役職コード = "";
              }
            } else {
              this.iraiForm.依頼者代理名 = "";
              this.iraiForm.依頼者代理所属部署名 = "";
              this.iraiForm.依頼者代理役職名 = "";

              this.iraiForm.依頼者代理事務所コード = "";
              this.iraiForm.依頼者代理営業所コード = "";
              this.iraiForm.依頼者代理部コード = "";
              this.iraiForm.依頼者代理課コード = "";
              this.iraiForm.依頼者代理係コード = "";
              this.iraiForm.依頼者代理役職コード = "";
            }
          });
      }
    },
    onBlurKeiyakuNanber() {
      if (
        this.searchForm.KeiyakuNanber === "" ||
        this.searchForm.KeiyakuNanber.trim() === ""
      ) {
        // return true
      } else {
        this.searchForm.KeiyakuNanber = this.searchForm.iraiForm.padStart(
          7,
          "0"
        );
      }
      this.savePageSearchData();
    },
    onBlurKeiyakuNanberEdaban() {
      if (
        this.searchForm.KeiyakuNanberEdaban === "" ||
        this.searchForm.KeiyakuNanberEdaban.trim() === ""
      ) {
        // return true
      } else {
        this.searchForm.KeiyakuNanberEdaban = this.searchForm.iraiForm.padStart(
          7,
          "0"
        );
      }
      this.savePageSearchData();
    },
    onBlurKeiyakuNendo() {
      if (
        this.searchForm.KeiyakuNendo === "" ||
        this.searchForm.KeiyakuNendo.trim() === ""
      ) {
        // return true
      } else {
        this.searchForm.KeiyakuNendo = this.searchForm.iraiForm.padStart(
          7,
          "0"
        );
      }
      this.savePageSearchData();
    },
    savePageSearchData() {
      this.$store.commit("setPageData", {
        searchForm: this.searchForm,
        契約No: this.searchForm.KeiyakuNanber,
        契約No枝番: this.searchForm.KeiyakuNanberEdaban,
        契約年度: this.searchForm.KeiyakuNendo,
      });
    },
    handleSelect(key, keyPath) {
      console.log("btn契約書番号更新_bef: " + this.画面入力制御disabled.btn契約書番号更新);
      this.selectIndex = key;
      console.log("btn契約書番号更新_af: " + this.画面入力制御disabled.btn契約書番号更新);
    },
    selectKojiKubun(val) {
      if (this.iraiForm.工事区分 && this.iraiForm.工事区分.charAt(0) === "4") {
        this.iraiForm.大区分 = "-";
        this.画面入力制御disabled.大区分 = true;
        this.iraiForm.中区分 = "-";
        this.画面入力制御disabled.中区分 = true;
        this.iraiForm.小区分 = "-";
        this.画面入力制御disabled.小区分 = true;
        this.iraiForm.依頼状況 = "-";
        this.画面入力制御disabled.依頼状況 = true;
      } else {
        this.画面入力制御disabled.大区分 = this.画面入力制御disabledOrg.大区分;
        this.画面入力制御disabled.中区分 = this.画面入力制御disabledOrg.中区分;
        this.画面入力制御disabled.小区分 = this.画面入力制御disabledOrg.小区分;
        this.画面入力制御disabled.依頼状況 =
          this.画面入力制御disabledOrg.依頼状況;
      }
    },
    selectDaiKubun(val) {
      // Get Dropdown中区分
      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetDropdown中区分", {
          大区分: this.iraiForm.大区分,
        })
        .then((response) => {
          $select中区分value.中区分value = response.data;
          this.iraiForm.中区分 = "-";
          this.iraiForm.小区分 = "-";
        });
    },
    selectChuKubun(val) {
      // Get Dropdown小区分
      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetDropdown小区分", {
          大区分: this.iraiForm.大区分,
          中区分: this.iraiForm.中区分,
        })
        .then((response) => {
          $select小区分value.小区分value = response.data;
          this.iraiForm.小区分 = "-";
        });
    },
    selectChushiHoryuKubun(val) {
      if (this.isUpdate) {
        if (
          this.iraiForm.中止取下げ区分 === "1" ||
          this.iraiForm.中止取下げ区分 === "2"
        ) {
          this.set画面入力制御(false);
        } else if (this.iraiForm.中止取下げ区分 === "0") {
          this.iraiForm.中止取下げ年月日 = "";
          this.iraiForm.中止取下げ事由 = "";
          this.$refs.refIraiForm.clearValidate([
            "中止取下げ年月日",
            "中止取下げ事由",
          ]);
          this.set画面入力制御(true);
        }
      }
    },
    selectSagyomeisyo() {
      //データセット
      this.shiharaiForm.明細no = this.tblShiharaiData.明細No;
      this.shiharaiForm.実施月_元 = this.tblShiharaiData.契約年月;
      this.shiharaiForm.状態 = this.tblShiharaiData.状態;
      this.shiharaiForm.進捗_本体 = this.tblShiharaiData.進捗_本体;
      this.shiharaiForm.進捗_発注 = this.tblShiharaiData.進捗_発注;
      this.shiharaiForm.取引先 = this.tblShiharaiData.業者名;
      this.shiharaiForm.作業担当者 = this.tblShiharaiData.作業担当者;
      this.shiharaiForm.担当者連絡先 = this.tblShiharaiData.担当者連絡先;
      this.shiharaiForm.作業完了日_FROM = this.tblShiharaiData.実施日開始;
      this.shiharaiForm.作業完了日_TO = this.tblShiharaiData.実施日終了;
      this.shiharaiForm.完了報告 = this.tblShiharaiData.完了報告;
      this.shiharaiForm.費用負担部門コード =
        this.tblShiharaiData.SGW用計上部門コード;
      this.shiharaiForm.費用負担部門名 = this.tblShiharaiData.SGW費用負担部門名;
      this.shiharaiForm.事業領域コード = this.tblShiharaiData.SGW用事業領域;
      this.shiharaiForm.事業領域名 = this.tblShiharaiData.SGW用事業領域名;
      this.shiharaiForm.外内販区分コード = this.tblShiharaiData.SGW用外内販区分;
      this.shiharaiForm.外内販区分名 = this.tblShiharaiData.SGW用外内販区分名;

      this.shiharaiForm.税率 = this.tblShiharaiData.検収発注税率 + "%";
      this.shiharaiForm.税込支払金額 = this.tblShiharaiData.検収発注金額_税込;
      this.shiharaiForm.支払予定日 = this.tblShiharaiData.支払予定日;
      this.shiharaiForm.完了確認日 = this.tblShiharaiData.作業結果承認日;
      this.shiharaiForm.完了確認者 = this.tblShiharaiData.作業結果確認者;

      this.shiharaiForm.点検書類待ち = this.tblShiharaiData.点検書類待ち;
      this.shiharaiForm.点検書類待ち_解消日 =
        this.tblShiharaiData.点検書類待ち解消日;
      this.shiharaiForm.完了報告書 = this.tblShiharaiData.完了報告書不備;
      this.shiharaiForm.完了報告書_解消日 =
        this.tblShiharaiData.完了報告書不備解消日;
      this.shiharaiForm.請求書 = this.tblShiharaiData.請求書不備;
      this.shiharaiForm.請求書_解消日 = this.tblShiharaiData.請求書不備解消日;

      this.shiharaiForm.否認差戻事由_支払 = this.tblShiharaiData.否認差戻事由;
    },

    async GetM018削除区分(区分コード, 名称コード, 連動区分) {
      return new Promise((resolve) => {
        this.http
          .post("/api/Reafs_W/Shonin/WD00530/GetM018削除区分", {
            区分コード: 区分コード,
            名称コード: 名称コード,
            連動区分: 連動区分,
          })
          .then((response) => {
            return resolve(response.data);
          });
      });
    },
    checkVersion() {
      return new Promise((resolve) => {
        this.http
          .get("/api/Reafs_W/Shonin/WD00530/CheckF011Version", {
            会社コード: this.tblMitumoriData[0].会社コード,
            契約No: this.tblMitumoriData[0].契約No,
            履歴No: this.tblMitumoriData[0].履歴No,
          })
          .then((response) => {
            if (
              this.tblMitumoriData[0].VERSION &&
              (this.tblMitumoriData[0].VERSION !== response.data.version ||
                !response.data.version)
            ) {
              resolve(false);
            } else {
              resolve(true);
            }
          });
      });
    },
    checkVersionFW042() {
      //承認の種類を判断する。
      let 種類 = "";
      if (this.tblShiharaiData !== null) {
        種類 = "61";
      } else {
        種類 = "32";
      }
      return new Promise((resolve) => {
        this.http
          .get("/api/Reafs_W/Shonin/WD00530/CheckFW042Version", {
            会社コード: this.tblShiharaiData.会社コード,
            契約No: this.tblShiharaiData.契約No,
            履歴No: this.tblShiharaiData.履歴No,
            明細No: this.tblShiharaiData.明細No,
            契約年月: this.tblShiharaiData.契約年月,
            サブNO: this.tblShiharaiData.サブNO,
            種類: 種類,
          })
          .then((response) => {
            if (
              this.tblShiharaiData.VERSION &&
              (this.tblShiharaiData.VERSION !== response.data.version ||
                !response.data.version)
            ) {
              resolve(false);
            } else {
              resolve(true);
            }
          });
      });
    },
    check参照権限() {
      return new Promise((resolve) => {
        if (
          this.名称取得方法17 &&
          this.名称取得方法17.filter(
            (x) => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7
          ).length > 0
        ) {
          const allPOSITIVE7Length = this.名称取得方法17.filter(
            (x) => x.POSITIVE組織コード && x.POSITIVE組織コード.length === 7
          );
          const allRange = allPOSITIVE7Length.map(
            (x) => x.支社コード + x.支社コード枝番
          );
          if (!allRange || allRange.length <= 0) {
            return resolve(false);
          }
          if (
            allPOSITIVE7Length.find(
              (x) => x.営業所コード && x.営業所コード.substring(0, 4) === "9900"
            )
          ) {
            return resolve(true);
          }
          this.http
            .post("/api/Reafs_W/Shonin/WD00530/get営業店CheckData", {
              lst支社コード: allRange,
              店コード: this.iraiForm.店コード,
              店コード枝番: this.iraiForm.店コード枝番,
            })
            .then((response) => {
              if (
                !response.data.Range名称取得方法18 ||
                !response.data.Range名称取得方法18.find(
                  (x) =>
                    x.店コード === this.iraiForm.店コード &&
                    x.店コード枝番 === this.iraiForm.店コード枝番
                )
              ) {
                if (!response.data.店コード) {
                  this.getMsg("E040662").then((res) => {
                    this.showError(res.data);
                  });
                  this.$refs.ref営業店.eigyotenCdFocus();
                  return resolve(false);
                }
              }
              return resolve(true);
            });
        } else {
          resolve(false);
        }
      });
    },
    getDataCheck営業店() {
      return new Promise((resolve) => {
        this.http
          .get("/api/Reafs_W/Shonin/WD00530/Check営業店", {
            店コード: this.iraiForm.店コード,
            店コード枝番: this.iraiForm.店コード枝番,
          })
          .then((response) => {
            resolve(response.data);
          });
      });
    },
    ddlValue(val) {
      if (val) {
        val = val.replace("-", "");
      }
      return val;
    },

    getDisplayData() {
      return {
        会社コード: this.userInfo.会社コード,
        依頼No: this.iraiForm.依頼番号,
        営業店コード: this.iraiForm.店コード,
        営業店コード枝番: this.iraiForm.店コード枝番,
        物件コード: this.iraiForm.物件コード,
        継続区分: this.isUpdate ? this.iraiForm.中止取下げ区分 : 0,
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
        中止年月日: this.isUpdate ? this.iraiForm.中止取下げ年月日 : "",
        中止取下げ事由: this.isUpdate ? this.iraiForm.中止取下げ事由 : "",
        削除区分: !this.isUpdate
          ? "0"
          : this.iraiForm.中止取下げ区分 === "0"
          ? "0"
          : this.iraiForm.中止取下げ区分 === "1"
          ? "3"
          : "2",
      };
    },

    getDataInsertFW042(依頼日) {
      return {
        会社コード: this.userInfo.会社コード,
        依頼No: this.iraiForm.依頼番号,
        種類: "10",
        履歴NO: "0",
        発注サブNo: "0",
        検収回数: "0",
        明細No: "0",
        契約年月: "",
        区分: "1",
        申請日: this.formatDate(new Date()),
        申請時間: new Date().toLocaleTimeString("en-US", { hour12: false }),
        申請区分: "",
        承認区分: "",
        物件コード: this.iraiForm.物件コード,
        表示日付: 依頼日,
        削除区分: !this.isUpdate
          ? "0"
          : this.iraiForm.中止取下げ区分 === "0"
          ? "0"
          : this.iraiForm.中止取下げ区分 === "1"
          ? "3"
          : "2",
      };
    },
    getDataF028() {
      return {
        会社コード: this.tblMitumoriData[0].会社コード,
        契約No: this.tblMitumoriData[0].契約No,
        依頼No: this.iraiForm.依頼No,
        履歴No: this.tblMitumoriData[0].履歴No,
        明細No: this.shiharaiForm.明細no,
        契約年月: this.shiharaiForm.実施月_元,
      };
    },
    formatDate(date) {
      const d = new Date(date);
      let month = "" + (d.getMonth() + 1);
      let day = "" + d.getDate();
      const year = d.getFullYear();

      if (month.length < 2) {
        month = "0" + month;
      }
      if (day.length < 2) {
        day = "0" + day;
      }

      return [year, month, day].join("/");
    },

    async onIraiHinin(mode) {
      this.loading_否認 = true;

      //ボタン押下時のタブにより、処理分岐
      //if(this.selectIndex == 4){
      if (this.tblShiharaiData !== null) {
        //登録時エラーチェック
        if (!(await this.fnc_obj_ShiharaiSyoninmaeErrorChk(this.iraiForm))) {
          this.loading_否認 = false;
          exit;
        }

        let msg = (await this.getMsg("K040066")).data;

        if (!(await this.confirm(msg))) {
          this.loading_否認 = false;
          return false;
        }

        const data = {
          F050_作業報告マスタ: this.getDataF028(),
          否認差戻事由: this.shiharaiForm.否認差戻事由_支払,
          mode: 1, //否認
          種類: "61",
          修繕区分小: this.tblMitumoriData[0].修繕区分小,
        };
        let res = {};
        await this.http
          .post(
            "/api/Reafs_W/Shonin/WD00530/UpdateTeikiShiharaiShonin",
            data,
            "登録しています...."
          )
          .then(function (response) {
            if (response.status == true) {
              res = response;
            } else {
              // 登録失敗メッセージ表示
              this.fnc_void_showInfoMsg("E040059");
            }
          });
        const msg2 = (await this.getMsg("M040013")).data;
        if (res.status) {
          // 登録完了メッセージ表示
          this.showSuccess(msg2);

          this.onSearchShiharai();
        }

        this.loading_否認 = false;
      } else {
        //登録時エラーチェック
        if (!(await this.fnc_obj_SyoninmaeErrorChk(this.iraiForm))) {
          this.loading_否認 = false;
          exit;
        }
        let msg = (await this.getMsg("K040014")).data;

        if (!(await this.confirm(msg))) {
          this.loading_否認 = false;
          return false;
        }
        this.shiharaiForm.明細no = 0;
        this.shiharaiForm.実施月_元 = "9999/99";

        const data = {
          F050_作業報告マスタ: this.getDataF028(),
          否認差戻事由: this.iraiForm.否認差戻事由,
          mode: 1, //否認
          種類: "32",
          修繕区分小: this.tblMitumoriData[0].修繕区分小,
        };
        let res = {};
        await this.http
          .post(
            "/api/Reafs_W/Shonin/WD00530/UpdateTeikiShiharaiShonin",
            data,
            "登録しています...."
          )
          .then(function (response) {
            if (response.status == true) {
              res = response;
            } else {
              // 登録失敗メッセージ表示
              this.fnc_void_showInfoMsg("E040059");
            }
          });
        const msg2 = (await this.getMsg("M040013")).data;
        if (res.status) {
          // 登録完了メッセージ表示
          this.showSuccess(msg2);

          this.onSearch();
        }
      }

      this.loading_否認 = false;
      return true;
    },

    async onIraiSashimodoshi() {
      console.log("差戻ボタン押下");

      this.loading_差戻 = true;
      //ボタン押下時のタブにより、処理分岐
      //if(this.selectIndex == 4){
      if (this.tblShiharaiData !== null) {
        if (this.shiharaiForm.否認差戻事由_支払 == "") {
          this.fnc_void_showInfoMsg("E040357");
          this.loading_差戻 = false;
          this.isErrPinkSashimodoshiJiyu = true;
          return false;
        }

        //登録時エラーチェック
        if (!(await this.fnc_obj_ShiharaiSyoninmaeErrorChk(this.iraiForm))) {
          this.loading_差戻 = false;
          this.isErrPinkSashimodoshiJiyu = true;
          return false;
        }

        let msg = (await this.getMsg("K040070")).data;

        if (!(await this.confirm(msg))) {
          this.loading_差戻 = false;
          return false;
        }

        const data = {
          F050_作業報告マスタ: this.getDataF028(),
          否認差戻事由: this.shiharaiForm.否認差戻事由_支払,
          mode: 2, //差戻
          種類: "61",
          修繕区分小: this.tblMitumoriData[0].修繕区分小,
        };
        let res = {};
        await this.http
          .post(
            "/api/Reafs_W/Shonin/WD00530/UpdateTeikiShiharaiShonin",
            data,
            "登録しています...."
          )
          .then(function (response) {
            if (response.status == true) {
              res = response;
            } else {
              // 登録失敗メッセージ表示
              this.fnc_void_showInfoMsg("E040059");
            }
          });
        const msg2 = (await this.getMsg("M040014")).data;
        if (res.status) {
          // 登録完了メッセージ表示
          this.showSuccess(msg2);

          this.onSearchShiharai();
        }

        this.loading_差戻 = false;
      } else {
        console.log("支払じゃない差戻");
        if (
          this.iraiForm.否認差戻事由 == "" ||
          this.iraiForm.否認差戻事由 == null
        ) {
          this.fnc_void_showInfoMsg("E040357");
          this.loading_差戻 = false;
          this.isErrPinkSashimodoshiJiyu = true;
          return false;
        }
        //登録時エラーチェック
        if (!(await this.fnc_obj_SyoninmaeErrorChk(this.iraiForm))) {
          this.loading_差戻 = false;
          exit;
        }

        let msg = (await this.getMsg("K040013")).data;

        if (!(await this.confirm(msg))) {
          this.loading_差戻 = false;
          return false;
        }
        this.shiharaiForm.明細no = 0;
        this.shiharaiForm.実施月_元 = "9999/99";

        const data = {
          F050_作業報告マスタ: this.getDataF028(),
          否認差戻事由: this.iraiForm.否認差戻事由,
          mode: 2, //差戻
          種類: "32",
          修繕区分小: this.tblMitumoriData[0].修繕区分小,
        };
        let res = {};
        await this.http
          .post(
            "/api/Reafs_W/Shonin/WD00530/UpdateTeikiShiharaiShonin",
            data,
            "登録しています...."
          )
          .then(function (response) {
            if (response.status == true) {
              res = response;
            } else {
              // 登録失敗メッセージ表示
              this.fnc_void_showInfoMsg("E040059");
            }
          });
        const msg2 = (await this.getMsg("M040014")).data;
        if (res.status) {
          // 登録完了メッセージ表示
          this.showSuccess(msg2);

          this.onSearch();
        }
      }

      this.loading_差戻 = false;
      return true;
    },

    async onIraiShonin() {
      this.loading_承認 = true;
      //ボタン押下時のタブにより、処理分岐
      //if(this.selectIndex == 4){
      if (this.tblShiharaiData !== null) {
        //登録時エラーチェック
        if (!(await this.fnc_obj_ShiharaiSyoninmaeErrorChk(this.iraiForm))) {
          this.loading_承認 = false;
          exit;
        }

        let msg = (await this.getMsg("K040065")).data;

        if (!(await this.confirm(msg))) {
          this.loading_承認 = false;
          return false;
        }

        this.shiharaiForm.明細no = this.tblShiharaiData.明細No;
        this.shiharaiForm.実施月_元 = this.tblShiharaiData.契約年月;

        const data = {
          F050_作業報告マスタ: this.getDataF028(),
          否認差戻事由: "",
          mode: 3, //承認
          種類: "61",
          修繕区分小: this.tblMitumoriData[0].修繕区分小,
        };
        let res = {};
        await this.http
          .post(
            "/api/Reafs_W/Shonin/WD00530/UpdateTeikiShiharaiShonin",
            data,
            "登録しています...."
          )
          .then(function (response) {
            if (response.status == true) {
              res = response;
            } else {
              // 登録失敗メッセージ表示
              this.fnc_void_showInfoMsg("E040059");
            }
          });
        const msg2 = (await this.getMsg("M040010")).data;
        if (res.status) {
          // 登録完了メッセージ表示
          this.showSuccess(msg2);

          this.onSearchShiharai();
        }
      } else {
        //登録時エラーチェック
        if (!(await this.fnc_obj_SyoninmaeErrorChk(this.iraiForm))) {
          this.loading_承認 = false;
          exit;
        }
        let msg = (await this.getMsg("K040015")).data;

        if (!(await this.confirm(msg))) {
          this.loading_承認 = false;
          return false;
        }
        this.shiharaiForm.明細no = 0;
        this.shiharaiForm.実施月_元 = "9999/99";

        const data = {
          F050_作業報告マスタ: this.getDataF028(),
          否認差戻事由: "",
          mode: 3, //承認
          種類: "32",
          修繕区分小: this.tblMitumoriData[0].修繕区分小,
        };
        let res = {};

        console.log("承認メール送信確認");

        await this.http
          .post(
            "/api/Reafs_W/Shonin/WD00530/UpdateTeikiShiharaiShonin",
            data,
            "登録しています...."
          )
          .then(function (response) {
            if (response.status == true) {
              res = response;
            } else {
              // 登録失敗メッセージ表示
              this.fnc_void_showInfoMsg("E040059");
            }
          });

        const msg2 = (await this.getMsg("M040010")).data;
        if (res.status) {
          // 登録完了メッセージ表示
          this.showSuccess(msg2);

          console.log("承認メール送信直前");
          console.log(this.最終承認フラグ);
          //メール送信（最終承認の時だけ）
          if (this.最終承認フラグ === 1){
            console.log("承認メール送信");

            let patternNo = 0;
            if (this.修繕区分小 === "08"){
                patternNo = 12
            }else{
                patternNo = 10
            }

            console.log(this.searchForm.KeiyakuNanber);
            console.log(this.searchForm.KeiyakuNanberEdaban);
            
            let param = {
                          msg_type: 0,
                          pg_id: 'TD00530',
                          st_kbn: 2,
                          mail_pattern_no: patternNo,
                          工事依頼NO: this.iraiForm.依頼No,
                          契約NO: this.searchForm.KeiyakuNanber + this.searchForm.KeiyakuNanberEdaban,
                          履歴NO: this.hikisu.履歴NO,
                          宛先事務所コード: [this.SGR担当事務所コード],
                          宛先営業所コード: [this.SGR担当営業所コード],
                          宛先部コード: [this.SGR担当部コード],
                          宛先課コード: [this.SGR担当課コード],
                          宛先係コード: [this.SGR担当係コード],
                        }

                        resultMail = this.commonFunctionUI.CM00110SendMail(param)
          }



          this.onSearch();
        }

      }
      this.loading_承認 = false;
      return retCode;
    },

    showSuccess(message, title = this.menuOptions.SubMenuName) {
      var self = this;
      this.MsgInfo({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading();
        },
      });
    },

    ///////////////////////////////////////////////
    // 承認前エラーチェック
    ///////////////////////////////////////////////
    async fnc_obj_SyoninmaeErrorChk(valSyoninJoken) {
      //バージョンチェック処理
      if (!(await this.checkVersion())) {
        this.getMsg("E040098").then((response) => {
          this.showError(response.data);
        });
        return false;
      }
      return true;
    },
    async fnc_obj_ShiharaiSyoninmaeErrorChk(valSyoninJoken) {
      //バージョンチェック処理
      if (!(await this.checkVersionFW042())) {
        this.getMsg("E040098").then((response) => {
          this.showError(response.data);
        });
        return false;
      }
      return true;
    },

    async fnc_void_Syonin(valSyoninJoken) {
      await this.http
        .post(
          "/api/Reafs_W/Shonin/WD00530/Jisshi_Syonin_IraiData",
          valSyoninJoken,
          "登録しています...."
        )
        .then(function (response) {
          if (response.status == true) {
            // 登録完了メッセージ表示
            this.fnc_void_showInfoMsg("M040021");
          } else {
            // 登録失敗メッセージ表示
            this.fnc_void_showInfoMsg("E040059");
          }
        });
    },

    ///////////////////////////////////////////////
    // エラーメッセージ出力
    ///////////////////////////////////////////////
    // お知らせメッセージ

    fnc_void_showInfoMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then((response) => {
        this.MsgInfo({
          title: "WD00530",
          message: response.data,
        });
      });
    },
    onSeeAttachment() {
      // this.paramsTyohyoIchiran.工事依頼No = this.iraiForm.依頼No
      this.paramsTyohyoIchiran.契約No = this.tblMitumoriData[0].契約No;
      this.paramsTyohyoIchiran.契約履歴No = this.tblMitumoriData[0].履歴No;

      if (this.tblShiharaiData !== null) {
        this.paramsTyohyoIchiran.見積明細No = this.tblShiharaiData.明細No;
        this.paramsTyohyoIchiran.契約年月 = this.tblShiharaiData.契約年月;
        this.paramsTyohyoIchiran.予定年月 = this.tblShiharaiData.契約年月;
      } else {
        this.paramsTyohyoIchiran.見積明細No = 0;
        this.paramsTyohyoIchiran.契約年月 = "0000/00";
      }

      this.showDialogTyohyoIchiran = true;
    },
    onViewAttachRegis() {
      // this.$router.push({ name: 'WD00120' })
      this.paramsIraiTenpuTouroku.工事依頼No = this.iraiForm.依頼No;
      this.showDialogIraiTenpuTouroku = true;
    },
    IraiTenpuTourokuClose() {
      this.onSearch();
    },
    async onBack() {
      localStorage.setItem("KeepAliveFlag", 0);
      localStorage.setItem("RouterBackFlag", 1);
      this.loadingBack = true;
      let isChange = false;

      if (!this.iraiForm.依頼No) {
        for (const prop in this.iraiFormInit) {
          if (prop !== "大区分" && prop !== "中区分" && prop !== "小区分") {
            if (this.iraiFormInit[prop] !== this.iraiForm[prop]) {
              isChange = true;
              this.loadingBack = false;
              break;
            }
          }
        }
      } else {
        if (this.iraiForm.依頼番号 !== this.iraiForm.依頼No) {
          isChange = true;
          this.loadingBack = false;
        } else {
          for (const prop in this.iraiFormInit) {
            if (prop !== "大区分" && prop !== "中区分" && prop !== "小区分") {
              if (this.iraiFormSearched[prop] !== this.iraiForm[prop]) {
                isChange = true;
                this.loadingBack = false;
                break;
              }
            }
          }
        }
      }
      if (isChange) {
        this.loadingBack = false;
        if (await this.confirm((await this.getMsg("K000001")).data)) {
          // if (this.page === 'WD00510') {
          //   this.$router.push({ name: 'WD00510' })
          // } else if (this.page === 'WD00210') {
          //   this.$router.push({ name: 'WD00210' })
          // }else{
          //   this.$router.push({ name: 'WD00010' })
          // }
          localStorage.setItem("KeepAliveFlag", 0);
          localStorage.setItem("RouterBackFlag", 1);
          this.$router.back();
        } else {
          // this.loadingBack = false
        }
      } else {
        this.loadingBack = false;
        // if (this.page === 'WD00510') {
        //   this.$router.push({ name: 'WD00510' })
        // } else if (this.page === 'WD00210') {
        //   this.$router.push({ name: 'WD00210' })
        // }else{
        //   this.$router.push({ name: 'WD00010' })
        // }
        localStorage.setItem("KeepAliveFlag", 0);
        localStorage.setItem("RouterBackFlag", 1);
        this.$router.back();
      }
    },
    onHelp() {
      this.showDialogKojikubunsetsumeiichiran = true;
    },
    checkKeiyakuNanber() {
      if (this.searchForm.KeiyakuNanber == "") {
        this.getMsg("E040002").then((response) => {
          this.showError(response.data);
        });
        this.$refs.KeiyakuNanber.focus();
        return false;
      }
    },
    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    ///////////////////////////////////////////////
    // 検索ボタンクリック時処理
    ///////////////////////////////////////////////
    fnc_void_clickBtnKensaku(valKensakuJoken) {
      // 検索前処理
      let letKensakuJokenConvertDataAfter = new Object();

      letKensakuJokenConvertDataAfter =
        this.fnc_rtrn_getKensakuBefore(valKensakuJoken);

      //エラーなければ検索実行
      if (letKensakuJokenConvertDataAfter[0] === true) {
        //検索実行
        this.fnc_void_getKensakuKekka(letKensakuJokenConvertDataAfter[1]);
      }
    },

    ///////////////////////////////////////////////
    // 検索実行
    ///////////////////////////////////////////////
    fnc_void_getKensakuKekka(KensakuJokenConvertAfter) {
      // 検索実行
      let _this = this;
      this.http
        .post("/api/Sagyo/TD00400_GET_KensakuKekka", KensakuJokenConvertAfter)
        // 検索実行後処理
        .then(function (response) {
          _this.tblData = response.data;
          _this.hitKensu = response.data.length + "  件";

          // 検索件数 = 0件の場合
          if (response.data.length === 0) {
            _this.fnc_void_showErrorMsg("E010224"); // 対象データが存在しません。
          }
        });
    },
    ///////////////////////////////////////////////
    // 契約書番号更新釦クリック処理
    ///////////////////////////////////////////////
    onUpdateKeiyakuBango() {
      let _this = this;
      // 確認メッセージ
      this.getMsg("K000005").then((response) => {
        this.MsgConf(
          {
            title: "依頼参照・承認",
            message: response.data,
          },
          () => {
            // 「はい」釦が選択された場合
            //　契約書番号更新

            const data = {
              契約no:
                this.searchForm.KeiyakuNanber +
                this.searchForm.KeiyakuNanberEdaban,
              履歴no: this.hikisu.履歴NO,
              契約no枝番: this.iraiForm.見積番号枝番,
              契約管理番号: this.iraiForm.ＳＧＷ契約書番号,
            };

            this.http
              .post(
                "/api/Reafs_W/Shonin/WD00530/UpdatekeiyakusyoNo",
                data,
                "登録しています...."
              )
              .then(function (response) {
                if (response.status == true) {
                  // 登録完了メッセージ表示
                  _this.fnc_void_showInfoMsg("M000012");
                } else {
                  // 登録失敗メッセージ表示
                  _this.fnc_void_showInfoMsg("E040059");
                }
              });

            return true;
          },
          () => {
            // 「いいえ」釦が選択された場合
            return false;
          }
        );
      });
    },
    ///////////////////////////////////////////////
    // 検索前ピンク色等データクリア
    ///////////////////////////////////////////////
    fnc_void_clearData() {
      let _this = this;
      _this.tblData = [];
      _this.hitKensu = "0  件";
    },

    ///////////////////////////////////////////////
    // リンククリック時の画面遷移またはモーダル表示処理
    ///////////////////////////////////////////////
    fnc_clickLink(row, column, rowIndex) {
      fnc_void_clearColor();
      switch (column.field) {
        // 予約申請 -> 定期作業報告入力をポップアップ
        case "tblKeiyakuMeisho":
          break;

        // 完了報告 -> 定期作業報告入力をポップアップ
        case "tblKanryoHokoku":
          break;

        default:
      }
    },

    ///////////////////////////////////////////////
    // keyからS016メッセージマスタにアクセスしメッセージ取得
    ///////////////////////////////////////////////
    fnc_rtn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    fnc_void_clearColor() {
      let _this = this;

      _this.isErrPinkKeiyakuNo = false;
      _this.isErrPinkHininJiyu = false;
      _this.isErrPinkSashimodoshiJiyu = false;
      _this.isErrPinkSyonin = false;
      _this.isErrPinkKeiyakusyoBango = false;
    },
  },
};
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
.comment {
  padding-left: 20px;
}
.commentContent {
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
.item_input_iraiNaiyo_fuguaiJokyo {
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
.home-el-footer {
  padding: 10px 0 0 0;
}
.btnHelp {
  height: 30px;
  width: 52px;
  padding: 0 12px 0 12px;
}
.item_label_MitumoriJoken {
  height: 53px;
  line-height: 20px;
  width: 60px;
  text-align: center;
  margin-top: 1px;
  padding-right: 0;
  vertical-align: middle;
  float: left;
  font-size: 14px;
  padding: 5px 0 0 0;
  box-sizing: border-box;
}
.item_input_MitumoriJoken {
  width: calc(100%);
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
.item_input_sekoNaiyo_Tokukijikoto {
  width: calc(100% - 120px);
}
.item_input_hinin_ashimodoshiJiyu {
  width: calc(100% - 120px);
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
.TeikiIraiSansyoShonin .resizer {
  height: 100% !important;
}
.IraiNyuryoku .依頼者 > div > div > div > button {
  padding: 0 12px 0 12px !important;
}
.IraiNyuryoku .依頼者代理 > div > div > div > button {
  padding: 0 12px 0 12px !important;
}
</style>
<style scoped>
.nengetu >>> .el-date-editor {
  height: 30px;
  width: 131px;
  margin: 0;
  line-height: 30px;
  border-radius: 1px;
  padding-right: 5px;
}
.pinkcolor >>> .el-input__inner {
  background-color: lightpink !important;
}
.pinkcolor_text >>> .el-textarea__inner {
  background-color: lightpink !important;
}
</style>
