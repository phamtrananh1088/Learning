<template>
  <div class="home-container IchiranKensaku">
    <el-form :model="pageData" :rules="rules" ref="refForm" label-width="105px" class="sample-form-class"
      :hide-required-asterisk="true">
      <el-container class="home-el-container">
        <!-- <el-header height="auto" class="home-el-header">
        </el-header> -->
        <el-main class="home-el-main">
          <el-row>
            <el-col :span="layoutSpan.one" class="one">&nbsp;</el-col>
            <el-col :span="layoutSpan.two" class="two">&nbsp;</el-col>
            <el-col :span="layoutSpan.three + layoutSpan.four">
              <span style="margin-left: -10px">
                ↓ 含まないとして検索したい場合✓（✓いれて項目空白なら空値以外を検索の意味）
              </span>
            </el-col>
          </el-row>
          <!-- 1 -->
          <el-row>
            <el-col :span="layoutSpan.one" class="one">
              <div>
                <el-form-item label="依頼№/契約№" prop="依頼No_契約No">
                  <reafsinputtext maxlength="11" v-model="pageData.依頼No_契約No" ref="ref依頼No_契約No" :width="'130px'">
                  </reafsinputtext>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="layoutSpan.two" class="two">
              <el-form-item label="進捗（本体）" prop="進捗_本体">
                <div style="display: flex" class="is-readonly">
                  <reafsinputtext v-model="進捗_本体Display" maxlenth="10" disabled :width="'206px'" class="text-ellipsis">
                  </reafsinputtext>
                  <el-button size="mini" @click="showShoriGaiyo進捗_本体 = !showShoriGaiyo進捗_本体">選択</el-button>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.three" class="three">
              <div style="display: flex; position: relative;">
                <el-checkbox v-model="pageData.check工事_契約件名Empty" style="position: absolute; top: 6px; left: -18px;">
                </el-checkbox>
                <!-- <el-form-item label="依頼内容/工事・契約件名" title="依頼内容/工事・契約件名" prop="工事_契約件名" style="width:100%">
                  <reafsinputtext maxlength="60" v-model="pageData.工事_契約件名" ref="ref工事_契約件名" width="92.3%">
                  </reafsinputtext>
                </el-form-item> -->
                <div class="el-form-item is-no-asterisk" title="依頼内容/工事・契約件名" style="width: 100%;">
                  <label for="工事_契約件名" class="el-form-item__label" style="width: 105px;font-size: 11px;position: relative;line-height: 16px;">依頼内容/</label>
                  <label for="工事_契約件名" class="el-form-item__label" style="width: 105px;font-size: 11px;position: relative;top: 10px;left: -28%;line-height: 20px;height: 0;">工事・契約件名</label>
                  <div class="el-form-item__content" style="margin-left: 105px;">
                    <reafsinputtext maxlength="60" v-model="pageData.工事_契約件名" ref="ref工事_契約件名" width="92.3%">
                   </reafsinputtext>
                  </div>
                </div>
              </div>
            </el-col>
            <el-col :span="layoutSpan.four" class="four">
              <el-form-item label="見積書" prop="見積書">
                <el-select v-model="pageData.見積書" ref="ref見積書" style="width:100%">
                  <el-option v-for="(item, index) in ddl書" :key="index + '見積書'" :label="item.text"
                    :value="item.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 2 -->
          <el-row>
            <el-col :span="layoutSpan.one" class="one">
              <el-form-item label="工事依頼日" prop="工事依頼日">
                <div style="display: flex">
                  <el-date-picker v-model="pageData.工事依頼日_開始日" type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                    class="date-picker date-range" />
                  <span style="margin: 0 2px">～</span>
                  <el-date-picker v-model="pageData.工事依頼日_終了日" type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                    class="date-picker date-range" />
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.two" class="two">
              <el-form-item label="進捗（発注）" prop="進捗_発注">
                <div style="display: flex" class="is-readonly">
                  <reafsinputtext v-model="進捗_発注Display" maxlenth="10" disabled :width="'206px'" class="text-ellipsis">
                  </reafsinputtext>
                  <el-button size="mini" @click="showShoriGaiyo進捗_発注 = !showShoriGaiyo進捗_発注">選択</el-button>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.three" class="three">
              <div style="display: flex; position: relative;">
                <el-checkbox v-model="pageData.check取引先Empty" style="position: absolute; top: 6px; left: -18px;">
                </el-checkbox>
                <el-form-item label="取引先" prop="取引先">
                  <div style="display: flex">
                    <reafsinputtext maxlength="39" v-model="pageData.取引先" ref="ref取引先" width="100%"
                      class="input-multi-code-3" @keydown.native="keydownTextbox($event, pageData.取引先)">
                    </reafsinputtext>
                    <opensearch :class="this.pageData.取引先.split(',').length >= 5 ? 'is-disabled' : ''"
                      :append="'append'" :path="'reafsmodalSearchTokuisaki'"
                      :subParamsObj="subParamsObj"
                      @backData="backDataTorihikisaki"
                      inputClass="button-multi-code">
                    </opensearch>
                  </div>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="layoutSpan.four" class="four">
              <el-form-item label="稟議書" prop="稟議書">
                <el-select v-model="pageData.稟議書" ref="ref稟議書" style="width:100%">
                  <el-option v-for="(item, index) in ddl書" :key="index + '稟議書'" :label="item.text"
                    :value="item.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 3 -->
          <el-row>
            <el-col :span="layoutSpan.one" class="one">
              <div>
                <el-form-item label="依頼者" prop="依頼者">
                  <opensearch
                  v-model="pageData.依頼者"
                  :appendlabelText="pageData.依頼者名"
                  appendlabelWidth="175px"
                  :append="'append'"
                  :maxlength="'7'"
                  :path="'reafsmodalSearchShain'"
                  @backData="iraishaBackData"
                  ref="ref依頼者コード"
                  :customValidate="2"
                >
                </opensearch>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="layoutSpan.two" class="two">
              <el-form-item label="負担区分" prop="負担区分">
                <el-select v-model="pageData.負担区分" ref="ref負担区分" style="width:90%">
                  <el-option v-for="(item, index) in 負担区分List" :key="index + '負担区分'" :label="item.名称"
                    :value="item.名称コード">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.three" class="three">
              <el-form-item label="金額" prop="金額">
                <div style="display: flex">
                  <reafsinputnumber :intLength="15" :floatLength="0" v-model="pageData.金額_抜_最小値" ref="ref金額_抜_最小値"
                    width="115px">
                  </reafsinputnumber>
                  <span style="margin: 0 2px">～</span>
                  <reafsinputnumber :intLength="15" :floatLength="0" v-model="pageData.金額_抜_最大値" ref="ref金額_抜_最大値"
                    width="115px">
                  </reafsinputnumber>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.four" class="four">
              <el-form-item label="報告書" prop="報告書">
                <el-select v-model="pageData.報告書" ref="ref報告書" style="width:100%">
                  <el-option v-for="(item, index) in ddl書" :key="index + '報告書'" :label="item.text"
                    :value="item.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 4 -->
          <el-row>
            <el-col :span="layoutSpan.one" class="one">
              <div>
                <el-form-item label="月分" prop="月分">
                  <div style="display: flex">
                    <el-date-picker v-model="pageData.月分_開始日" type="month" format="yyyy/MM" value-format="yyyy/MM"
                      class="date-picker" />
                    <span style="margin: 0 2px">～</span>
                    <el-date-picker v-model="pageData.月分_終了日" type="month" format="yyyy/MM" value-format="yyyy/MM"
                      class="date-picker" />
                  </div>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="layoutSpan.two" class="two">
              <el-form-item label="営業店" prop="営業店">
                <div style="display: flex">
                  <reafsinputtext maxlength="34" v-model="pageData.営業店" ref="ref営業店" width="100%"
                    class="input-multi-code" @keydown.native="keydownTextbox($event, pageData.営業店)">
                  </reafsinputtext>
                  <eigyoten :class="this.pageData.営業店.split(',').length >= 5 ? 'is-disabled' : ''"
                    @backData="backDataEigyoten" ref=ref営業店 inputClass="button-multi-code" />
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.three" class="three">
              <el-form-item label="支払予定日" prop="支払予定日">
                <div style="display: flex">
                  <el-date-picker v-model="pageData.支払予定日_開始日" type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                    class="date-picker" />
                  <span style="margin: 0 2px">～</span>
                  <el-date-picker v-model="pageData.支払予定日_終了日" type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                    class="date-picker" />
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.four" class="four">
              <el-form-item label="請求書" prop="請求書">
                <el-select v-model="pageData.請求書" ref="ref請求書" style="width:100%">
                  <el-option v-for="(item, index) in ddl書" :key="index + '請求書'" :label="item.text"
                    :value="item.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 5 -->
          <el-row>
            <el-col :span="layoutSpan.one" class="one">
              <el-form-item label="工事区分" prop="工事区分">
                <el-select v-model="pageData.工事区分" ref="ref工事区分" style="width:94%">
                  <el-option v-for="(item, index) in 工事区分List" :key="index + '工事区分'" :label="item.名称"
                    :value="item.名称コード">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.two" class="two">
              <el-form-item label="支店" prop="支店">
                <el-select v-model="pageData.支店" ref="ref支店" style="width:90%">
                  <el-option v-for="(item, index) in 支店List" :key="index + '支店'" :label="item.名称" :value="item.名称コード">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.three" class="three">
              <el-form-item label="修繕区分(大)" prop="修繕区分(大)">
                <el-select v-model="pageData.修繕区分大" ref="ref修繕区分大" style="width:92.3%">
                  <el-option v-for="(item, index) in 修繕区分大List" :key="index + '修繕区分大'" :label="item.名称"
                    :value="item.名称コード">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.four" class="four">
              <el-form-item label="不備チェック1" prop="check不備チェック1">
                <el-checkbox v-model="pageData.check不備チェック1"></el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- 6 -->
          <el-row>
            <el-col :span="layoutSpan.one" class="one">
              <div>
                <el-form-item label="大区分" prop="大区分">
                  <el-select v-model="pageData.大区分" ref="ref大区分" style="width:94%">
                    <el-option v-for="(item, index) in 大区分List" :key="index + '大区分'" :label="item.名称"
                      :value="item.名称コード">
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="layoutSpan.two" class="two">
              <el-form-item label="物件" prop="物件">
                <div style="display: flex">
                  <reafsinputtext maxlength="44" v-model="pageData.物件" ref="ref物件" width="100%" class="input-multi-code"
                    @keydown.native="keydownTextbox($event, pageData.物件)">
                  </reafsinputtext>
                  <opensearch :class="this.pageData.物件.split(',').length >= 5 ? 'is-disabled' : ''" :append="'append'"
                    :path="'reafsmodalSearchBukken'" @backData="backDataBukken" inputClass="button-multi-code">
                  </opensearch>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.three" class="three">
              <el-form-item label="修繕区分(小)" prop="修繕区分(小)">
                <el-select v-model="pageData.修繕区分小" ref="ref修繕区分小" style="width:92.3%">
                  <el-option v-for="(item, index) in 修繕区分小List" :key="index + '修繕区分小'" :label="item.名称"
                    :value="item.名称コード">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="layoutSpan.four" class="four">
              <el-form-item label="不備チェック2" prop="check不備チェック2">
                <el-checkbox v-model="pageData.check不備チェック2"></el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="layoutSpan.one + layoutSpan.two">
              <!-- 7 -->
              <el-row>
                <el-col :span="12" class="one">
                  <el-form-item label="中区分" prop="中区分">
                    <el-select v-model="pageData.中区分" ref="ref中区分" style="width:94%">
                      <el-option v-for="(item, index) in 中区分List" :key="index + '中区分'" :label="item.名称"
                        :value="item.名称コード">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12" class="two">
                  <el-form-item label="物件区分" prop="物件区分">
                    <el-select v-model="pageData.物件区分" ref="ref物件区分" style="width:90%">
                      <el-option v-for="(item, index) in 物件区分List" :key="index + '物件区分'" :label="item.名称"
                        :value="item.名称コード">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- 8 -->
              <el-row>
                <el-col :span="12" class="one">
                  <el-form-item label="小区分" prop="小区分">
                    <el-select v-model="pageData.小区分" ref="ref小区分" style="width:94%">
                      <el-option v-for="(item, index) in 小区分List" :key="index + '小区分'" :label="item.名称"
                        :value="item.名称コード">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12" class="two">
                  <el-form-item label="予定工期(開始日)" prop="予定工期_開始日">
                    <div style="display: flex">
                      <el-date-picker v-model="pageData.予定工期_開始日_開始日" type="date" format="yyyy/MM/dd"
                        value-format="yyyy/MM/dd" class="date-picker" />
                      <span style="margin: 0 2px">～</span>
                      <el-date-picker v-model="pageData.予定工期_開始日_終了日" type="date" format="yyyy/MM/dd"
                        value-format="yyyy/MM/dd" class="date-picker" />
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- 9 -->
              <el-row>
                <el-col :span="12" class="one">
                  <div>
                    <el-form-item label="進捗状況" prop="進捗状況" class="is-required">
                      <el-checkbox-group v-model="pageData.進捗状況">
                        <el-checkbox class="chk-shinchoku-joukyou" v-for="(item, index) in 進捗状況List" :key="index + '進捗状況'" :label="item.名称コード">{{
                            item.名称
                        }}</el-checkbox>
                      </el-checkbox-group>
                    </el-form-item>
                  </div>
                </el-col>
                <el-col :span="12" class="two">
                  <el-form-item label="予定工期(終了日)" prop="予定工期_終了日">
                    <div style="display: flex">
                      <el-date-picker v-model="pageData.予定工期_終了日_開始日" type="date" format="yyyy/MM/dd"
                        value-format="yyyy/MM/dd" class="date-picker" />
                      <span style="margin: 0 2px">～</span>
                      <el-date-picker v-model="pageData.予定工期_終了日_終了日" type="date" format="yyyy/MM/dd"
                        value-format="yyyy/MM/dd" class="date-picker" />
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <!-- 10 -->
              <el-row>
                <el-col :span="12" class="one">
                  <el-form-item label="稟議№" prop="稟議No">
                    <div style="display: flex">
                      <reafsinputtext maxlength="15" v-model="pageData.稟議No" ref="ref稟議No" width="154px" @blur="onRingiNoBlur">
                      </reafsinputtext>
                      <el-select :disabled="disable.稟議No_条件検索" v-model="pageData.稟議No_条件検索" ref="ref稟議No_条件検索"
                        style="width:41.4%">
                        <el-option label="---" value="0" />
                        <el-option label="稟議No.未入力を検索" value="スペースを含む" />
                        <el-option label="稟議No.入力済みを検索" value="スペースを含まない" />
                      </el-select>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="12" class="two">
                  <el-form-item label="完了日" prop="完了日">
                    <div style="display: flex">
                      <el-date-picker v-model="pageData.完了日_開始日" type="date" format="yyyy/MM/dd"
                        value-format="yyyy/MM/dd" class="date-picker" />
                      <span style="margin: 0 2px">～</span>
                      <el-date-picker v-model="pageData.完了日_終了日" type="date" format="yyyy/MM/dd"
                        value-format="yyyy/MM/dd" class="date-picker" />
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-col>
            <el-col :span="layoutSpan.three + layoutSpan.four">
              <el-row>
                <el-col :span="12" class="three" style="width: 100% !important">
                  <el-form-item label="解消日" prop="解消日">
                    <div style="display: flex">
                      <el-date-picker v-model="pageData.解消日_開始日" type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                        class="date-picker" />
                      <span style="margin: 0 2px">～</span>
                      <el-date-picker v-model="pageData.解消日_終了日" type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                        class="date-picker" />
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="16" style="padding-left: 10px;">
                  <el-form-item label="到着コメント" prop="到着コメント">
                    <reafsinputtext maxlength="50" v-model="pageData.到着コメント" ref="ref到着コメント" width="94.3%">
                    </reafsinputtext>
                  </el-form-item>
                </el-col>
                <el-col :span="4">
                  <el-button type="primary" size="mini" @click="onSearch" :loading="loading.btn検索" style="margin-left: 6px">検索
                  </el-button>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="17">&nbsp;</el-col>
            <el-col :span="3">{{ pageData.tableData.length !== 0 ? base.formatCommaNumeric(pageData.tableData.length +
                '') + '件' : '&nbsp;'
            }}</el-col>
            <el-col :span="4">合計金額 {{ sumMoney }}円</el-col>
          </el-row>
          <el-row>
            <reafstable ref="table" :tableData="pageData.tableData" :columns="columns" :maxHeight="645"
              :cellStyleOption="cellStyleOption" rowKeyFieldName="key" :virtualScrollOption="virtualScrollOption" :HiddenColumnKeys="hiddenColumnIchiranKensaku"></reafstable>
          </el-row>
        </el-main>
        <el-footer class="home-el-footer" height="auto">
          <el-row>
            <el-col :span="20">
              <div class="grid-content">&nbsp;</div>
            </el-col>
            <el-col :span="2">
              <el-button @click="onBack" :loading="loading.btn戻る" style="float: right">戻る
              </el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
    <shoriGaiyo :dialogVisible.sync="showShoriGaiyo進捗_本体" :data="進捗_本体List" :selection.sync="pageData.進捗_本体">
    </shoriGaiyo>
    <shoriGaiyo :dialogVisible.sync="showShoriGaiyo進捗_発注" :data="進捗_発注List" :selection.sync="pageData.進捗_発注">
    </shoriGaiyo>
  </div>
</template>
<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafstable from '@/components/table/Grid.vue'
import shoriGaiyo from '@/components/modal/ShoriGaiyo.vue'
import eigyoten from '@/components/basic/EigyoTen.vue'
import opensearch from '@/components/basic/openSearch.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'

export default {
  async activated () {
    if (localStorage.getItem('KeepAliveFlag') == 0 && this.isMounted == false) {
      localStorage.setItem('KeepAliveFlag', 1)
      await this.initOnMounted()
      document.querySelector('.ve-table-container').scrollTop = this.scrollTop
    }
    this.isMounted = false
  },
  components: {
    reafsinputtext,
    reafstable,
    shoriGaiyo,
    eigyoten,
    opensearch,
    reafsinputnumber
  },
  data () {
    return {
      isMounted: false,
      scrollTop: 0,
      layoutSpan: {
        one: 7,
        two: 7,
        three: 7,
        four: 3
      },
      showShoriGaiyo進捗_本体: false,
      showShoriGaiyo進捗_発注: false,
      ddl書: [ // 「-」 OR 「〇」 OR 「×」 OR 「空値」
        { text: 'ー', value: 'ー' },
        { text: ' ', value: ' ' },
        { text: '〇', value: '〇' },
        { text: '×', value: '×' }
      ],
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      rules: {
        営業店コード: [
          {
            validator: (rule, value, callback) => {
              if (this.iraiForm.店コード === '' || this.iraiForm.店コード枝番 === '') {
                this.getMsg('E040005').then((response) => {
                  callback(new Error(response.data))
                })
              }
            },
            trigger: 'blur'
          }
        ]
      },
      m001Info: {},
      pageData: {
        No: '',
        依頼No_契約No: '',
        進捗_本体: [],
        工事_契約件名: '',
        check工事_契約件名Empty: false,
        見積書: 'ー',
        工事依頼日_開始日: '',
        工事依頼日_終了日: '',
        進捗_発注: [],
        取引先: '',
        check取引先Empty: false,
        稟議書: 'ー',
        依頼者: '',
        依頼者名: '',
        負担区分: 'ー',
        金額_抜_最小値: '',
        金額_抜_最大値: '',
        報告書: 'ー',
        月分_開始日: '',
        月分_終了日: '',
        営業店: '',
        支払予定日_開始日: '',
        支払予定日_終了日: '',
        請求書: 'ー',
        工事区分: 'ー',
        支店: 'ー',
        解消日_開始日: '',
        解消日_終了日: '',
        check不備チェック1: false,
        大区分: 'ー',
        物件: '',
        到着コメント: '',
        check不備チェック2: false,
        中区分: 'ー',
        物件区分: 'ー',
        小区分: 'ー',
        予定工期_開始日_開始日: '',
        予定工期_開始日_終了日: '',
        予定工期_終了日_開始日: '',
        予定工期_終了日_終了日: '',
        進捗状況: [],
        稟議No: '',
        稟議No_条件検索: '---',
        完了日_開始日: '',
        完了日_終了日: '',
        修繕区分大: 'ー',
        修繕区分小: 'ー',
        tableData: []
      },
      負担区分List: [],
      工事区分List: [],
      支店List: [],
      大区分List: [],
      中区分List: [],
      物件区分List: [],
      小区分List: [],
      進捗状況List: [],
      進捗_本体List: [],
      進捗_発注List: [],
      修繕区分大List: [],
      修繕区分小List: [],
      cellStyleOption: {
        headerCellClass: ({ row, column, rowIndex }) => {
        },
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = ''
          if (column.key === '1') {
            css += ' text-center'
          }
          if (['2', '3', '4', '5'].includes(column.key)) {
            css += ' text-left'
          }
          if (['5', '9', '13', '17', '20', '23', '27', '30', '33'].includes(column.key)) {
            css += ' position-relative'
          }

          return css
        }
      },
      columns: [
        {
          field: 'No',
          key: '1',
          title: '№',
          align: 'middle',
          vAlign: 'top',
          width: 50
        },
        {
          key: '2',
          title: '依頼№/契約№',
          children: [
            {
              key: '3',
              title: '工事依頼日',
              children: [
                {
                  key: '4',
                  title: '依頼者',
                  children: [
                    {
                      key: '5',
                      title: '月分',
                      renderBodyCell: ({ row }) => {
                        return (
                          <div style="display: grid" class="cell-body-custome cell-1">
                            <div>{row.依頼No || row.依頼No}</div>
                            <div class="text-center">{row.工事依頼日}</div>
                            <div class="ellipsis" title={row.依頼者}>{row.依頼者}</div>
                            <div class="text-center">{row.月分}</div>
                          </div>
                        )
                      },
                      width: 135
                    }]
                }]
            }]
        },
        {
          key: '6',
          title: '工事区分',
          children: [
            {
              key: '7',
              title: '大区分',
              children: [
                {
                  key: '8',
                  title: '中区分',
                  children: [
                    {
                      key: '9',
                      title: '小区分',
                      renderBodyCell: ({ row }) => {
                        return (
                          <div style="display: grid; text-align: left;" class="cell-body-custome cell-1">
                            <div class="ellipsis" title={row.工事区分}>{row.工事区分}</div>
                            <div class="ellipsis" title={row.大区分名}>{row.大区分名}</div>
                            <div class="ellipsis" title={row.中区分名}>{row.中区分名}</div>
                            <div class="ellipsis" title={row.小区分名}>{row.小区分名}</div>
                          </div>
                        )
                      },
                      width: 135
                    }]
                }]
            }]
        },
        {
          key: '10',
          title: '進捗状況',
          children: [
            {
              key: '11',
              title: '進捗（本体）',
              children: [
                {
                  key: '12',
                  title: '進捗（発注）',
                  children: [
                    {
                      key: '13',
                      title: '負担区分',
                      renderBodyCell: ({ row }) => {
                        return (
                          <div style="display: grid; text-align: left;" class="cell-body-custome cell-1">
                            <div class="ellipsis" title={row.進捗状況}>{row.進捗状況}</div>
                            <div class="ellipsis" title={row.進捗_本体}>{row.進捗_本体}</div>
                            <div class="ellipsis" title={row.進捗_発注}>{row.進捗_発注}</div>
                            <div class="ellipsis" title={row.負担区分}>{row.負担区分}</div>
                          </div>
                        )
                      },
                      width: 135
                    }]
                }]
            }]
        },
        {
          key: '14',
          title: '営業店',
          children: [
            {
              key: '15',
              title: '物区',
              renderHeaderCell: ({ column, row }) => {
                return (
                  <div class="custome-cell-header">
                    <span style="width: 40%;height: 100%;line-height: 26px;" class="border-right">物区</span>
                    <span style="width: 60%;height: 100%;line-height: 26px;">物件名</span>
                  </div>
                )
              },
              children: [
                {
                  key: '16',
                  title: '予定工期',
                  children: [
                    {
                      key: '17',
                      align: 'left',
                      width: 240,
                      renderHeaderCell: ({ column, row }) => {
                        return (
                          <div class="custome-cell-header">
                            <span style="width: 61.4%;height: 100%;line-height: 26px;" class="border-right">稟議№</span>
                            <span style="width: 38.6%;height: 100%;line-height: 26px;">完了日</span>
                          </div>
                        )
                      },
                      renderBodyCell: ({ row }) => {
                        return (
                          <div style="display: grid" class="cell-body-custome cell-1">
                            <div class="ellipsis" title={row.営業店}>{row.営業店}</div>
                            <div style="padding-left: 0px;">
                              <span title={row.物件区分} style="width: 96px;height: 26px; display: inline-block;padding-left: 5px" class="border-right ellipsis">{row.物件区分}</span>
                              <span title={row.物件名} style="width: 143px;height: 26px; display: inline-block; padding-left: 5px;" class="ellipsis">{row.物件名}</span>
                            </div>
                            <div class="ellipsis" style="text-align: center" title={row.予定工期}>{row.予定工期}</div>
                            <div style="padding-left: 0px;">
                              <span style="width: 147px;height: 26px; display: inline-block;padding-left: 5px" class="border-right ellipsis" title={row['稟議No.']}>{row['稟議No.']}</span>
                              <span style="width: 92px;height: 26px; display: inline-block;text-align: center; position: absolute;" title={row.完了日}>{row.完了日}</span>
                            </div>
                          </div>
                        )
                      }
                    }]
                }
              ]
            }
          ]
        },
        {
          key: '18',
          title: '依頼内容 / 工事・契約件名',
          renderHeaderCell: ({ column }) => {
            return (
              <div>
                <span>依頼内容/</span><br/><span>工事・契約件名</span>
              </div>
            )
          },
          rowspan: 2,
          children: [
            {
              key: '19',
              rowspan: 0,
              children: [
                {
                  key: '20',
                  title: '取引先名',
                  renderBodyCell: ({ row }) => {
                    return (
                      <div style="display: grid" class="cell-body-custome cell-2">
                        <div style="display: flex;" class="cell-body-2"><a title={row['依頼内容/工事・契約件名']} class="ellipsis2" on-click={() => this.handleLinkClick(row)} href="javascript:;">{row['依頼内容/工事・契約件名']}</a></div>
                        <div style="display: flex;text-align: left;" class="cell-body-2"><span class="ellipsis2" title={row.取引先名}>{row.取引先名}</span></div>
                      </div>
                    )
                  }
                }]
            }]
        },
        {
          key: '21',
          title: '金額(抜)',
          rowspan: 1,
          children: [
            {
              key: '22',
              rowspan: 1,
              title: '支払予定日',
              children: [
                {
                  key: '22_2',
                  rowspan: 1,
                  title: '修繕区分(大)',
                  children: [
                    {
                      key: '23',
                      title: '修繕区分(小)',
                      renderBodyCell: ({ row }) => {
                        return (
                          <div style="display: grid" class="cell-body-custome cell-1">
                            <div style="display: flex; align-items: center; padding-right: 5px;" class="cell-body-2"><span style="margin-left: auto;">{row['原価金額(抜)']}</span></div>
                            <div style="display: flex; align-items: center;"><span style="margin: 0 auto;line-height: 36px;" class="cell-body-2">{row.支払予定日}</span></div>
                            <div style="display: flex; align-items: center; padding-right: 5px;" class="cell-body-2"><span>{row['修繕区分大']}</span></div>
                            <div style="display: flex; align-items: center; padding-right: 5px;" class="cell-body-2"><span>{row['修繕区分小']}</span></div>
                          </div>
                        )
                      },
                      width: 140
                    }]
                }]
            }]
        },
        {
          key: '24',
          title: '見積書',
          children: [
            {
              key: '25',
              title: '稟議書',
              children: [
                {
                  key: '26',
                  title: '完了報告書',
                  children: [
                    {
                      key: '27',
                      title: '請求書',
                      renderBodyCell: ({ row }) => {
                        return (
                          <div style="display: grid" class="cell-body-custome cell-1 clsSeikyu">
                            <div style="position: relative">
                              <span class="border-right">{row.見積書不備}</span>
                              <span>{row.見積書不備解消日}</span>
                            </div>
                            <div style="position: relative">
                              <span class="border-right">{row.稟議書不備}</span>
                              <span>{row.稟議書不備解消日}</span>
                            </div>
                            <div style="position: relative">
                              <span class="border-right">{row.完了報告書不備}</span>
                              <span>{row.完了報告書不備解消日}</span>
                            </div>
                            <div style="position: relative">
                              <span class="border-right">{row.請求書不備}</span>
                              <span>{row.請求書不備解消日}</span>
                            </div>
                          </div>
                        )
                      },
                      width: 135
                    }]
                }]
            }]
        },
        {
          key: '28',
          title: 'チェック1',
          rowspan: 2,
          children: [
            {
              key: '29',
              rowspan: 0,
              children: [
                {
                  key: '30',
                  title: 'チェック2',
                  width: 65,
                  renderBodyCell: ({ row }) => {
                    return (
                      <div style="display: grid" class="cell-body-custome cell-2">
                        <div style="display: flex; align-items: center;" class="cell-body-2"><span style="margin: 0 auto;">{row.不備チェック1=='1'?'✓':''}</span></div>
                        <div style="display: flex; align-items: center;" class="cell-body-2"><span style="margin: 0 auto;">{row.不備チェック2=='1'?'✓':''}</span></div>
                      </div>
                    )
                  }
                }]
            }]
        },
        {
          key: '31',
          title: '到着コメント',
          rowspan: 2,
          children: [
            {
              key: '32',
              rowspan: 0,
              children: [
                {
                  key: '33',
                  title: '請求書備考',
                  renderBodyCell: ({ row }) => {
                    return (
                      <div style="display: grid" class="cell-body-custome cell-2">
                        <div style="display: flex;" class="cell-body-2"><span class="ellipsis2" style="text-align: left" title={row.到着コメント}>{row.到着コメント}</span></div>
                        <div style="display: flex;" class="cell-body-2"><span class="ellipsis2" style="text-align: left" title={row.備考}>{row.備考}</span></div>
                      </div>
                    )
                  }
                }]
            }]
        },
        // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」Start
        {
          key: 'f001工事依頼No',
          title: 'f001工事依頼No',
          field: 'f001工事依頼No',
          width: 0,
          ellipsis: {
            showTitle: true
          }
        }
      ],
      hiddenColumnIchiranKensaku: [
        'f001工事依頼No'
      ],
      // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」End
      loading: {
        btn検索: false,
        btn戻る: false
      },
      disable: {
        稟議No_条件検索: false
      },
      virtualScrollOption: {
        enable: true,
        minRowHeight: 105
      },
      subParamsObj: {
        taisyou: '', // 承認種類　 取引先検索：対象 //ハノイ側修正2022/08/26　STEP2_W　課題管理表№59：設計書「修正履歴」シートの「2022/08/24仕様変更分」「取引先検索起動時にパラメータ仕様を変更」
        jigyousyoCd: '110', // 事業所コード
        torType: '' // null or 0:取引先 1:得意先
      },
      // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」Start
      sessionPageData: null
      // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」End
    }
  },
  watch: {
    'pageData.大区分' (n, o) {
      if (n !== 'ー') {
        this.getDropDown中区分()
      } else {
        this.pageData.中区分 = 'ー'
        this.中区分List = []
      }
    },
    'pageData.中区分' (n, o) {
      this.getDropDown小区分()
    },
    'pageData.修繕区分大' (n, o) {
      if (n !== 'ー') {
        this.getDropDown修繕区分小()
      } else {
        this.pageData.修繕区分小 = 'ー'
        this.修繕区分小List = []
      }
    }
  },
  computed: {
    sumMoney () {
      this.loading.btn検索
      let sum = 0
      for (const mn of this.pageData.tableData) {
        sum += (mn['原価金額(抜)'] || '0').replaceAll(',', '') * 1
      }
      return this.base.formatCommaNumeric(sum + '')
    },
    進捗_本体Display () {
      // const listSelect = this.進捗_本体List.filter(x => this.pageData.進捗_本体.find(g => g == x.value))
      // return listSelect.map(x => x.name).join(',')
      return this.pageData.進捗_本体.length > 0 ? '指定あり' : ''
    },
    進捗_発注Display () {
      // const listSelect = this.進捗_発注List.filter(x => this.pageData.進捗_発注.find(g => g == x.value))
      // return listSelect.map(x => x.name).join(',')
      return this.pageData.進捗_発注.length > 0 ? '指定あり' : ''
    }
  },
  created () {
    this.isMounted = true
  },
  async mounted () {
    await this.GetStartInfo()
    this.initOnMounted()
  },
  methods: {
    async initOnMounted () {
      if (localStorage.getItem('RouterBackFlag') == 1) {
        localStorage.setItem('RouterBackFlag', 0)
        // ハノイ側修正2022/12/08　STEP2_W　課題管理表№240：設計書「修正履歴」シートの「2022/12/07仕様変更分」「他画面から戻ってきた際の再検索処理が常に実行されてしまう。」Start
        if (this.pageData.tableData.length > 0) {
          await this.onSearch()
        }
        // ハノイ側修正2022/12/08　STEP2_W　課題管理表№240：設計書「修正履歴」シートの「2022/12/07仕様変更分」「他画面から戻ってきた際の再検索処理が常に実行されてしまう。」End
      }
    },
    onRingiNoBlur () {
      if (this.pageData.稟議No) {
        this.disable.稟議No_条件検索 = true
        this.pageData.稟議No_条件検索 = '---'
      } else {
        this.disable.稟議No_条件検索 = false
      }
    },
    backDataTorihikisaki (obj) {
      const lst取引先 = this.pageData.取引先.split(',')
      if (lst取引先.length <= 5) {
        if (this.pageData.取引先 && lst取引先.length >= 1) {
          this.pageData.取引先 += ','
        }
        this.pageData.取引先 += obj.txtCd
      }
    },
    backDataBukken (obj) {
      const lst物件 = this.pageData.物件.split(',')
      if (lst物件.length <= 5) {
        if (this.pageData.物件 && lst物件.length >= 1) {
          this.pageData.物件 += ','
        }
        this.pageData.物件 += obj.txtCd
      }
    },
    keydownTextbox (e, val) {
      // console.log(e)
      // if (val && val.split(',').length > 5 &&
      // e.keyCode !== 46 &&
      //   e.keyCode !== 8) {
      //   e.preventDefault()
      // }
      if (val && val.split(',').length >= 5 && e.keyCode === 188) {
        e.preventDefault()
      }
    },
    backDataEigyoten (obj) {
      const lst営業店 = this.pageData.営業店.split(',')
      if (lst営業店.length <= 5) {
        if (this.pageData.営業店 && lst営業店.length >= 1) {
          this.pageData.営業店 += ','
        }
        this.pageData.営業店 += obj.eigyotenCd + obj.eigyotenBango
      }
    },
    getDropDown小区分 () {
      if (this.pageData.大区分 !== 'ー' && this.pageData.中区分 !== 'ー') {
        this.http
          .get('/api/Reafs_W/Irai/WD00210/GetDataN005', { 大区分: this.pageData.大区分, 中区分: this.pageData.中区分 })
          .then((res) => {
            this.小区分List = res.data
            this.pageData.小区分 = 'ー'
          })
      } else {
        this.小区分List = []
        this.pageData.小区分 = 'ー'
      }
    },
    getDropDown修繕区分小 () {
      this.http
        .get('/api/Reafs_W/Irai/WD00210/GetDataN016', { 修繕区分大: this.pageData.修繕区分大 })
        .then((res) => {
          this.修繕区分小List = res.data
          this.pageData.修繕区分小 = 'ー'
        })
    },
    getDropDown中区分 () {
      this.http
        .get('/api/Reafs_W/Irai/WD00210/GetDataN004', { 大区分: this.pageData.大区分 })
        .then((res) => {
          this.中区分List = res.data
          this.pageData.中区分 = 'ー'
        })
    },
    iraishaBackData (e) {
      if (!e.txtCd) {
        this.pageData.依頼者 = ''
        this.pageData.依頼者名 = ''
      } else if (e.lblNm) {
        this.pageData.依頼者 = e.txtCd
        this.pageData.依頼者名 = e.lblNm
      } else {
        this.http
          .get('/api/Reafs_W/Irai/WD00210/GetDataN001', { 依頼者: e.txtCd })
          .then((res) => {
            if (res.data !== null && res.data.名称取得方法01 != null) {
              this.pageData.依頼者名 = res.data.名称取得方法01.社員名
            } else {
              // this.pageData.依頼者 = ''
              this.pageData.依頼者名 = ''
              this.getMsg('E040020').then((response) => {
                this.showError(response.data, {
                  callback: () => {
                    this.$refs.ref依頼者.focus()
                  }
                })
              })
            }
          })
      }
    },
    async GetStartInfo () {
      // Get Dropdown
      await this.http
        .get('/api/Reafs_W/Irai/WD00210/GetStartInfo', 'アクセスしています....')
        .then((response) => {
          // Get Dropdown工事区分
          this.工事区分List = response.data.名称取得方法02
          // Get Dropdown大区分
          this.大区分List = response.data.名称取得方法03

          this.進捗状況List = response.data.名称取得方法06

          this.負担区分List = response.data.名称取得方法09

          this.支店List = response.data.名称取得方法11

          this.物件区分List = response.data.名称取得方法13

          this.進捗_本体List = response.data.名称取得方法07

          this.進捗_発注List = response.data.名称取得方法08

          this.修繕区分大List = response.data.名称取得方法15
        })
    },
    handleLinkClick (row) {
      if (!row.工事区分コード) {
        return
      }
      // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」Start
      // create session and send
      // const sessionPageData = {pageData: JSON.parse(JSON.stringify(this.pageData)), scrollTop: document.querySelector('.ve-table-container').scrollTop}
      // delete sessionPageData.pageData.tableData
      // localStorage.setItem('WD00210_IchiranKensaku', JSON.stringify(sessionPageData))

      localStorage.setItem('KeepAliveFlag', 0)
      this.scrollTop = document.querySelector('.ve-table-container').scrollTop
      if (row.工事区分.includes('一般') || row.工事区分.includes('緊急') || row.工事区分.includes('スポット')) {
        if (row.承認フラグ == 0) {
          // F100.承認フラグ=0の場合、依頼入力画面に遷移する
          this.$router.push({ name: 'WD00110', query: { 工事依頼No: row.依頼No, page: 'WD00210' } })

          // if(row.依頼入力フラグ == 1){
          //   // 依頼入力済・差戻の場合依頼入力画面に遷移
          //   this.$router.push({ name: 'WD00110', query: { 工事依頼No: row.依頼No, page: 'WD00210' } })
          // }else {
          //   this.$router.push({ name: 'WD00111', query: {工事依頼No: row.f001工事依頼No ? row.f001工事依頼No : row.依頼No, page: 'WD00210'} })
          //   // window.location.href = `/Reafs_W/SyuzenIraiSansyoShonin?工事依頼No=${row.f001工事依頼No ? row.f001工事依頼No : row.依頼No}&page=WD00210`
          // }
        } else if (row.承認フラグ == 1) {
          this.$router.push({ name: 'WD00520',
            query: {工事依頼No: row.f001工事依頼No ? row.f001工事依頼No : row.依頼No,
              page: 'WD00210'} })
          // window.location.href = `/Reafs_W/WD00520/SyuzenIraiSansyoShonin?工事依頼No=${row.f001工事依頼No ? row.f001工事依頼No : row.依頼No}&page=WD00210`
        }
        // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」End
      } else if (row.工事区分.includes('定期')) {
        // ハノイ側修正2022/09/14　STEP2_W　課題管理表№103：設計書「修正履歴」シートの「2022/09/08仕様変更分」「画面遷移仕様を変更(処理概要)」START
        // this.$router.push({ name: 'WD00530' })
        if (row.承認フラグ == 0) {
           // F100.承認フラグ=0の場合、依頼入力画面に遷移する
          this.$router.push({ name: 'WD00110', query: { 工事依頼No: (row.f001工事依頼No ? row.f001工事依頼No : row.依頼No), page: 'WD00210' } })

          // this.$router.push({ name: 'WD00530',
          //   query: {
          //     依頼No: (row.f001工事依頼No ? row.f001工事依頼No : row.依頼No),
          //     page: 'WD00210'
          //   }})
          // window.location.href = `/Reafs_W/TeikiIraiSansyoShonin?依頼No=${row.f001工事依頼No ? row.f001工事依頼No : row.依頼No}&page=WD00210`
        } else if (row.承認フラグ == 1) {
          let checkExistF011 = false
          let checkExistF021 = false
          if (row.f011契約No) {
            checkExistF011 = true
          }
          // F011とF021(F050)を「契約NO、履歴NO」で結合
          if (row.f050契約No && row.f050履歴No && (row.f011契約No == row.f050契約No) && (row.f050履歴No == row.f011履歴No)) {
            checkExistF021 = true
          }
          
          if (checkExistF011 === false) {
            // F011が存在しない場合、WD00311に遷移する
            this.$router.push({ name: 'WD00311',
              query: {
                依頼No: (row.f001工事依頼No ? row.f001工事依頼No : row.依頼No),
                page: 'WD00210'
              }})
            // this.$router.push({ name: 'WD00530',
            //   query: {
            //     依頼No: (row.f001工事依頼No ? row.f001工事依頼No : row.依頼No),
            //     page: 'WD00210'
            //   }})
            // window.location.href = `/Reafs_W/TeikiIraiSansyoShonin?依頼No=${row.f001工事依頼No ? row.f001工事依頼No : row.依頼No}&page=WD00210`
          } else {
            if (checkExistF021 === false) {
              // 2022/11/17 条件追加
              if(row.定期申請状況フラグ==1){
                this.$router.push({ name: 'WD00530',
                  query: {
                    契約NO: row.f011契約No,
                    履歴NO: row.f011履歴No,
                    契約年度: row.f011契約年度,
                    page: 'WD00210'
                  }})
              }else{
                this.$router.push({ name: 'WD00311',
                  query: {
                    依頼No: (row.f001工事依頼No ? row.f001工事依頼No : row.依頼No),
                    page: 'WD00210'
                  }})
              }
              // window.location.href = `/Reafs_W/WD00530/TeikiIraiSansyoShonin?契約NO=${row.f011契約No}&履歴NO=${row.f011履歴No}&契約年度=${row.f011契約年度}&page=WD00210`
            } else {
              this.$router.push({ name: 'WD00530',
                query: {
                  契約NO: row.f050契約No,
                  履歴NO: row.f050履歴No,
                  明細NO: row.f050明細No,
                  契約年月: row.f050契約年月,
                  契約年度: row.f050契約年度,
                  page: 'WD00210'
                }})
              // window.location.href = `/Reafs_W/WD00530/TeikiIraiSansyoShonin?契約NO=${row.f050契約No}&履歴NO=${row.f050履歴No}&明細NO=${row.f050明細No}&契約年月=${row.f050契約年月}&契約年度=${row.f050契約年度}&page=WD00210`
            }
          }
        }
        // ハノイ側修正2022/09/14　STEP2_W　課題管理表№103：設計書「修正履歴」シートの「2022/09/08仕様変更分」「画面遷移仕様を変更(処理概要)」END
      }
    },
    async onSearch () {
      this.loading.btn検索 = true
      const data = JSON.parse(JSON.stringify(this.pageData))
      delete data.tableData
      data.進捗状況 = data.進捗状況.join(',')
      data.進捗_本体 = data.進捗_本体.join(',')
      data.進捗_発注 = data.進捗_発注.join(',')
      data.工事区分 = data.工事区分.replace('ー', '')
      data.修繕区分大 = data.修繕区分大.replace('ー', '')
      data.修繕区分小 = data.修繕区分小.replace('ー', '')
      data.金額_抜_最大値 = data.金額_抜_最大値.replaceAll(',', '')
      data.金額_抜_最小値 = data.金額_抜_最小値.replaceAll(',', '')
      data.営業店 = data.営業店.split(',').filter(x => !!x).join(',')
      data.物件 = data.物件.split(',').filter(x => !!x).join(',')
      data.取引先 = data.取引先.split(',').filter(x => !!x).join(',')

      for (const property in data) {
        if (data[property] === 'ー') {
          data[property] = ''
        }
      }
      let result
      await this.http
        .post('/api/Reafs_W/Irai/WD00210/GetPageData', data, 'アクセスしています....')
        .then((res) => {
          result = res
        }).finally(() => {
          this.loading.btn検索 = false
        })

      if (result.status) {
        if (result.message !== null) {
          console.log('result.message1' + result.message)
          this.showError(result.message)
        }
        this.pageData.tableData = result.data
        this.pageData.tableData.map((item, index) => {
          item.No = index + 1
          item['原価金額(抜)'] = item['原価金額(抜)'] ? this.base.formatCommaNumeric(item['原価金額(抜)'] + '') : ''
        })
        this.loading.btn検索 = false
        // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」Start
        // this.$nextTick(() => {
        //   if (this.sessionPageData) {
        //     document.querySelector('.ve-table-container').scrollTop = this.sessionPageData.scrollTop || 0
        //     this.sessionPageData = null
        //   }
        // })
        // ハノイ側修正2022/09/09　STEP2_W　課題管理表№81：設計書「修正履歴」シートの「2022/09/06仕様変更分」「画面遷移時のパラメータを追加」End
      } else {
        console.log('result.message2' + result.message)
        this.showError(result.message)
      }
    },
    onBack () {
      // this.$router.push({ name: 'Whome' })
      this.$router.push({ name: 'WD00010' })// home画面のname指定が異なるため
    },
    showError (message, option = null, title = this.$store.getters.getPageTitle()) {
      var self = this
      this.MsgErr({
        title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof (option.callback) === 'function') {
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
      Object.keys(this.loading).forEach(v => this.loading[v] = false)
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
    getDataCheck営業店 () {
      return new Promise((resolve) => {
        this.http
          .get('/api/Reafs_W/Irai/WD00111/Check営業店', { 店コード: this.iraiForm.店コード, 店コード枝番: this.iraiForm.店コード枝番 })
          .then((response) => {
            resolve(response.data)
          })
      })
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    formatNumeric (n) {
      return this.base.formatCommaNumeric(n)
    }
  }
}

</script>
<style lang="less" scoped>
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.el-form-item {
  margin-bottom: 0px;
}

.home-container {
  padding: 10px;
  width: 1380px;
  // height: 100%;
}

// .sample-form-class {
//   height: 100%;
// }

// .home-el-container {
//   height: 100%;
// }

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

.border-top {
  border-top: 1px solid #DCDFE6;
}

.custome-cell-header {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  align-items: center;
  display: flex;
  text-align: center;
}

.border-right {
  border-right: 1px solid #000 !important;
}

.cell-body-custome {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.cell-body-custome div:not(:last-child) {
  border-bottom: solid 1px #000;
}

.cell-body-custome div {
  padding-left: 5px;
}

.cell-1 div {
  height: 26px;
  line-height: 22px;
}

.cell-2 div {
  height: 52px;
}

.is-disabled {
  pointer-events: none;
  opacity: 0.6;
}
/* ハノイ側修正2022/12/09　STEP2_W　課題管理表№234：「2022/12/01依頼分」テキストエリアが非活性になっている場合、スクロールバーが動作しない。START*/
.el-textarea.is-disabled {
  pointer-events: all;
}
/* ハノイ側修正2022/12/09　STEP2_W　課題管理表№234：「2022/12/01依頼分」テキストエリアが非活性になっている場合、スクロールバーが動作しない。END*/
.ellipsis {
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.ellipsis2 {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  height: 44px;
  line-height: 22px;
}

.cell-body-2 {
  height: 42px;
}

.clsSeikyu div {
  position: relative;
}

.clsSeikyu div span:first-child{
  height: 27px;
  display: inline-block;
  position: absolute;
  top: -1px;
  left: 0;
  width: 30%;
}

.clsSeikyu div span:last-child{
  height: 27px;
  display: inline-block;
  padding-left: 5px;
  position: absolute;
  top: -1px;
  right: 0;
  width: 70%;
}
</style>

<style>
.bukken .append-background {
  width: 143px !important;
}

.IchiranKensaku .input-multi-code {
  width: 226px;
}

.IchiranKensaku .input-multi-code-3 {
  width: 211px;
}

.IchiranKensaku .button-multi-code {
  height: 31px;
  display: inline !important;
}

.IchiranKensaku .button-multi-code .reafs-input,
.IchiranKensaku .button-multi-code .append-label {
  display: none !important;
}

.IchiranKensaku .button-multi-code .append-background {
  width: 36px !important;
  background-color: white !important;
  height: 29px;
}

.IchiranKensaku .button-multi-code button {
  padding: 0 12px 0 12px !important;
  margin-left: -4px;
  border-start-start-radius: 1px;
  border-end-start-radius: 1px;
}

.iraisha .append-background {
  width: 62% !important;
}

/* .el-scrollbar__view {
  height: 100%;
} */

.IchiranKensaku .ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr {
  height: 30px;
}

.IchiranKensaku .ve-table-container {
  min-height: 122px !important;
}

.text-center {
  text-align: center !important;
  vertical-align: middle !important;
}

.text-left {
  text-align: left !important;
}

.position-relative {
  position: relative;
  height: 83px !important;
}

.IchiranKensaku .el-checkbox-group {
  height: 30px !important;
}

.IchiranKensaku .el-checkbox-group .el-checkbox:first-child,
.IchiranKensaku .four .el-checkbox {
  margin-left: 10px !important;
}

.IchiranKensaku .el-input__prefix {
  display: none;
}

.IchiranKensaku .el-date-editor .el-input__inner {
  padding-left: 4px !important;
  padding-right: 4px !important;
}

.IchiranKensaku .el-date-editor.el-input,
.IchiranKensaku .el-date-editor.el-input__inner {
  width: 115px;
}

/* .one .el-form-item__label,
.three .el-form-item__label,
.four .el-form-item__label {
  width: 105px !important;
} */

/* .one .el-form-item__content,
.three .el-form-item__content,
.four .el-form-item__content {
  margin-left: 105px !important;
} */

.three {
  width: 28.16667% !important;
  padding-left: 10px;
}

.four {
  width: 13.5% !important;
}

.text-ellipsis input {
  text-overflow: ellipsis;
  overflow: hidden;
}

.IchiranKensaku .ve-table-content .ve-table-body tr:not([style*="height: 0px"]) {
  height: 105px !important;
}

.chk-shinchoku-joukyou > span {
  padding-left: 0;
  font-size: 11px;
}
</style>
