<template>
  <div class="home-container MitsumoriNyuryokuTeiki">
    <el-row :gutter="10">
      <label v-bind:href="searchForm.update_host" class="hidden"></label>
      <label v-bind:href="searchForm.update_id" class="hidden"></label>
      <!-- 検索部 -->
      <!-- １行目 -->
      <el-col :span="5">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="契約№">
            <el-col Class="row-bg">
              <label v-bind:value="searchForm.keiyakuNo" v-on:input="searchForm.keiyakuNo = $event.target.value">{{searchForm.keiyakuNo}}</label>
            </el-col> 
            <label v-bind:href="searchForm.rirekiNO" class="hidden">{{$route.params.rirekiNO}}</label>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="8">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="版数">
            <reafsinputtext 
              v-model="searchForm.hansu"
              maxlength="2"
              :width="'60px'"
            >
            <template v-slot:append>
              <el-button class="btn_updown" @click="changeHansu(true)">◀</el-button>
              <el-button class="btn_updown" @click="changeHansu(false)">▶</el-button>
              <el-label>
                <span class="is-red">最新</span>
                ／
                <span >履歴</span>
              </el-label>
              <el-button @click="GetMitsumoriData">検索</el-button>
            </template>
            </reafsinputtext>
          </el-form-item>

        </el-form>
      </el-col>

      <el-col :span="5">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
        <el-form-item label="依頼№">
          <el-col Class="row-bg">
            <label v-bind:href="searchForm.iraiNO">{{searchForm.iraiNO}}</label>
          </el-col>
        </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="契約管理番号">
            <el-col Class="row-bg">
              <label v-bind:href="searchForm.keiyakukanriNO">{{searchForm.keiyakukanriNO}}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
      
    <el-row :gutter="10">
      <!-- ２行目 -->
      <el-col :span="5">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="提出先">
            <el-col Class="row-bg">
              <label v-bind:href="searchForm.teisyutsusaki">{{searchForm.teisyutsusaki}}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="16">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="依頼者">
            <el-col Class="row-bg">
              <label v-bind:href="searchForm.iraisya">{{searchForm.iraisya}}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="3">
          <el-button @click="onIraiNaiyoHyojiClick">依頼内容表示</el-button>
          <screenPopup src="/Reafs_T/TeikiMitsumoriIraiSansho" :params="iraiNaiyoParams" :dialogVisible.sync="showIraiNaiyoSabu">
          </screenPopup>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- 検索部 -->
      <!-- ３行目 -->
      <el-col :span="18">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
          @submit.native.prevent
        >
          <el-form-item label="契約名称" prop="keiyakumeisyo">
            <reafsinputtext 
              maxlength="200"
              v-model="searchForm.keiyakumeisyo"
              :width="'1080px'"
              :required="false"
              @change="checkChangedData('契約名称')"
            ></reafsinputtext>
          </el-form-item>
        </el-form>      
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- 検索部 -->
      <!-- ４行目 -->
      <el-col :span="8">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
        <el-form-item label="表示区分">
          <el-select
            v-model="searchForm.hyoujiKBN"
            @change="checkChangedData('表示区分')"
          >
            <el-option
              label="依頼分のみ"
              value="1"
            ></el-option>
            <el-option
              label="契約"
              value="2"
            ></el-option>
          </el-select>
        </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-button @click="GetMitsumoriData">検索</el-button>
      </el-col>

      <el-col :span="6">
        <el-label v-bind:href="searchForm.mitsumoriiraibi">見積依頼日：{{searchForm.mitsumoriiraibi}}</el-label>
        <br>
        <el-label v-bind:href="searchForm.keiyakukikan">契約期間：{{searchForm.keiyakukikan}}</el-label>
      </el-col>
      <el-col :span="6">
        <el-label v-bind:href="searchForm.joutai">状態：{{searchForm.joutai}}</el-label>
      </el-col>
    </el-row>

    <el-row :gutter="10" style="align-items: center">
      <!-- 検索部 -->
      <!-- ５行目 -->
      <el-col :span="5">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px">
          <el-form-item label="物件">
            <el-col Class="row-bg el-input-date-len-10">
              <label style="" v-bind:href="searchForm.bukkenCD">{{searchForm.bukkenCD}}</label>
            </el-col>
          </el-form-item>  
        </el-form>
      </el-col>

      <el-col :span="5">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <!-- <el-col Class="row-bg el-input-date-len-20">
            <label v-bind:href="searchForm.bukkenmei">{{searchForm.bukkenmei}}</label>
          </el-col>  -->
          <reafsinputtext v-model="searchForm.bukkenmei" class="is-readonly" disabled="disabled" :width="'200px'">
          </reafsinputtext>
        </el-form>
      </el-col>

      <el-col :span="5">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
        <el-form-item label="物件住所">
            <el-col Class="row-bg el-input-date-len-10">
              <label v-bind:href="searchForm.postCD">{{searchForm.postCD}}</label>
            </el-col>
          </el-form-item>  
        </el-form>
      </el-col>
      <el-col :span="9">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <!-- <el-col Class="row-bg el-input-date-len-200">
            <label v-bind:href="searchForm.juusyo">{{searchForm.juusyo}}</label>
          </el-col>  -->
          <reafsinputtext v-model="searchForm.juusyo" class="is-readonly" disabled="disabled" :width="'400px'">
          </reafsinputtext>
        </el-form>
      </el-col>
    </el-row> 

    <el-row :gutter="10">
      <!-- 検索部 -->
      <!-- ５行目 -->
      <el-col :span="8">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
        <el-form-item label="見積有効期間">
          <nengetsu
              v-model="searchForm.mitsumoriyuukoukikan"
              :required="true"
              type="date"
              format="yyyy/MM/dd"
              :clearable="false"
              :width="'130px'"
              :disabled="this.isDisplayMode"
              @change="checkChangedData('見積有効期間')"
            ></nengetsu>
        </el-form-item>

        </el-form>

        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="見積金額" class="input-font-20">
            <div style="display: flex">
              <reafsinputtext floatLength=0 v-model="searchForm.mitsumoriKingaku" class="is-readonly input-font-20" disabled="disabled"
                :width="'200px'">
              </reafsinputtext>
              <el-label class="inline-block" style="margin-top: 10px">（税込）―</el-label>
            </div>
          </el-form-item>
          
        </el-form>
      </el-col>

      <el-col :span="8">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
          class="el-input-len-20"
          @submit.native.prevent>
        
          <el-form-item label="見積担当者">
            <reafsinputtext 
              maxlength="20"
              v-model="searchForm.mitsumoritantousya"
              :width="'300px'"
              :required="true"
              :disabled="this.isDisplayMode"
              @change="checkChangedData('見積担当者')"
            ></reafsinputtext>
          </el-form-item>
          
        </el-form>

        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
          @submit.native.prevent>
        
          <el-form-item label="担当者連絡先" prop="tantousyaTEL">
            <reafsinputtext 
              maxlength="14"
              v-model="searchForm.tantousyaTEL"
              :width="'150px'"
              :required="true"
              :disabled="this.isDisplayMode"
              @change="checkChangedData('担当者連絡先')"
            ></reafsinputtext>
          </el-form-item>
          
        </el-form>
      </el-col>

      <el-col :span="8">
        
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="50px"
        >
        
        <el-form-item label="" prop="SGRrenrakujiko">
          <el-label>リアルティからの連絡事項</el-label>
          <el-input
            type="textarea"
            v-model="searchForm.SGRrenrakujiko"
            maxlength="200"
            show-word-limit
            height="500"
            disabled="disabled"
          ></el-input>
        </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    

    <el-row :gutter="10">
      <!-- 検索部 -->
      <!-- ７行目 -->
      <el-col :span="8">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
        <el-label><span class="is-red">見積（新規／相見積／修正依頼／契約変更）</span></el-label>
        <br>
        <div style="">
          <div style="width: 45px; height: 1px; float:left"></div>
          <el-button @click="onAddRowMeisai" :disabled="this.isDisplayMode">５行追加</el-button>
        </div>
        </el-form>
      </el-col>
      <el-col :span="7">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
          @submit.native.prevent>
        
          <el-form-item label="過去見積参照">
            <div style="display: flex; align-items: center">
              <reafsinputtext 
                maxlength="14"
                v-model="searchForm.kakomitsumorisansyo"
                :width="'160px'"
                :required="false"
                key="reftxtKakoSansho"
              >
              </reafsinputtext>
              <el-button :disabled="isDisplayMode" icon="el-icon-search" size="mini" @click="showTS00300 = !showTS00300"></el-button>
            </div>
          </el-form-item>
          <screenPopup src="/Reafs_T/KakoMitsumoriSanshoSabu" :params="{ 修繕定期区分: '2' }"
            :dialogVisible.sync="showTS00300" @onBackData="KakoMitsumoriSanshoSabuonBackData"></screenPopup>
        
        </el-form>
      </el-col>

      <el-col :span="2">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px">
        
          <el-button :disabled="this.isDisplayMode" @click="onReference">参照検索</el-button>    
        </el-form>
      </el-col>
    </el-row>

    <el-row>
      <reafstable ref="table" :tableData="tableDataMeisai" :columns="columnskensakukekka" :maxHeight="354"
        :cellStyleOption="cellStyleOptionMeisai" rowKeyFieldName="key" class="tblMeisai">
      </reafstable>
    </el-row>

    <el-row :gutter="10">
      <!-- 下段 -->
      <el-col :span="8">
        <el-label>見積書表示備考</el-label>
        <el-input
            type="textarea"
            v-model="searchForm.mitsumorisyohyoujibiko"
            maxlength="200"
            show-word-limit
            class="textarea-h200"
            :disabled="this.isDisplayMode"
            @change="checkChangedData('見積書表示備考')"
          ></el-input>
        <el-label>提案備考・コメント</el-label>
        <el-input
            type="textarea"
            v-model="searchForm.teianbikou_koment"
            maxlength="200"
            show-word-limit
            :disabled="this.isDisplayMode"
            @change="checkChangedData('提案備考・コメント')"
          ></el-input>
      </el-col>

      <el-col :span="12">
        <!-- 検索結果 -->
      <template>
        <reafstable ref="tblSubTotal" :tableData="tableDataSubTotal" :columns="columnsSubTotal" :Height="195" :maxHeight="195"
          rowKeyFieldName="key" :footer-data="footerDataSubTotal">
        </reafstable>
        
      </template>

      </el-col>
      <!-- <el-col :span="2">
        <el-button @click="onRenewalData" size="mini">更新</el-button>
      </el-col> -->
      
    </el-row>

    
    <!-- <el-row :gutter="5" class="footer"> -->
    <el-row :gutter="5">
      <!-- 最下段 -->
      <!-- <el-col :span="17"> -->
        <el-button @click="OnClick(1)">物件資料参照</el-button>
        <screenPopup src="/Reafs_T/BukkenDokyumentoSanshoSabu" :params="bukkenShiryoSansyoParams" :dialogVisible.sync="showTS00304">
        </screenPopup>
        
        <el-button @click="OnClick(2)" :disabled="this.isDisplayMode">見積データ取込</el-button>    
        <screenPopup src="/Reafs_T/TeikiMitsumoriDataTorikomi" :params="mitsumoriTorikomiParams" :dialogVisible.sync="showTS00305" @onBackData="MitsumoriDataTorikomiOnBackData">
        </screenPopup>

        <el-button @click="OnClick(3)">写真添付</el-button>    
        <screenPopup src="/Reafs_T/ShashinTenpuSabu" :params="genzyoShashinTenpuParams" :dialogVisible.sync="showTS00303">
        </screenPopup>

        <el-button @click="OnClick(4)">図面・資料添付</el-button>   
        <screenPopup src="/Reafs_T/ZumenShiryoTenpuSabu" :params="ZumenShiryoTenpuSabuParams" :dialogVisible.sync="showTS00302">
        </screenPopup>

      <!-- </el-col>
      <el-col :span="7"> -->
        <el-button @click="OnClick(0)" :disabled="this.isDisplayMode">一時保存</el-button>
        <el-button @click="OnClick(5)" :disabled="this.isDisplayMode || this.isFirstMode">提出</el-button>    
        <el-button @click="onBack">戻る</el-button>
      <!-- </el-col> -->
    </el-row>
    
    <screenPopup
      src="/Reafs_T/TeikiMitsumoriSyosaiNyuryoku" 
      :params="MitsumoriSyosaiNyuryokuParams"
      :dialogVisible.sync="showTS00306"
      :isCloseConfirm="MitsumoriSyosaiNyuryokuParams.cfmFlg"
      messageCodeCloseCfm="K000001"
      @closePopupEvent="MitsumoriSyosaiNyuryokuClose"></screenPopup>

    <!-- 非表示 -->
    <label v-bind:href="searchForm.koubaisinseijoukyo" class="hidden">{{searchForm.koubaisinseijoukyo}}</label>
  </div>
</template>
<script>

import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafsinputnumber2 from '@/components/basic/ReafsControl/ReafsInputNumber2.vue'
import reafstable from '@/components/table/Grid.vue'
import reafsmodal from '@/components/modal/Modal.vue'
// import eigyoten from '@/components/basic/Eigyoten.vue'
import nengetsu from '@/components/basic/Nengetsu.vue'
import screenPopup from '@/views/Reafs-T/common/ScreenPopup/ScreenPopup.vue';
import moment from 'moment'
import opensearch from '@/components/basic/openSearch.vue'

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
    reafsinputnumber2,
    reafstable,
    reafsmodal,
    nengetsu,
    screenPopup,
    moment,
    opensearch
  },

  props: {
    mitsumoriSyosaiChangedFlg: {
      type: Boolean,
      default: false
    }
  },
  
  data() {
    return {
      searchForm:{
        keiyakuNo:'',
        hansu:'',
        iraiNo:'',
        aimitsumoriNO:'',
        iraisya:'',
        keiyakumeisyo:'',
        rirekiNO:'',
        update_host:'',
        update_id:'',
        mitsumoritantousya:'',
        tantousyaTEL:'',
        mitsumoriyuukoukikan:'',
        koubaisinseijoukyo:'',
        sagyomeisyo:'',
        suryo:'',
        tani:'',
        tanka:0,
        keiyakuNoBukkenmei:'',
        SGRrenrakujiko:'',
        mitsumorisyohyoujibiko:'',
        teianbikou_koment:'',
        hyoujiKBN: '1',
        shinchokuCode: '',
        kakomitsumorisansyo: '',
        defaultTax: 0,
        TD00302_GridData: [{
          sagyomeisyo: '',
          suryo: 0,
          tani: '',
          tanka: 0,
          bunruiCode: '',
          genkakingaku_zeinuki: 0,
          genkakingaku_zeikomi: 0,
          genkakingaku_zeigaku: 0,
          genkazeiKBN: -1,
          genkazeiritsu: 0
        }],
        sendMailJimusyoCode : '',
        sendMailEigyosyoCode : '',
        sendMailBuCode : '',
        sendMailKaCode : '',
        sendMailKakariCode : '',
        keizokuKBN: ''
      },

      KeyDataToTD00302: {
        kaishaCode: '',
        keiyakuNo: '',
        keiyakuNoEdaban: '',
        iraiNo: '',
        rirekiNo: '',
        aimitsumoriNo: '',
        gyoshaCode: '',
        gyoshaCodeEdaban: '',
        keiyakuNendo: '',
        keiyakuMeisho: '',
        mitsumoriTantosha: '',
        mitsumoriTantoRenrakusaki: '',
        mitsumoriYukoKikan: '',
        genkaBiko: '',
        comment: '',
        TD00302_GridData: [{
          sagyomeisyo: '',
          suryo: 0,
          tani: '',
          tanka: 0,
          bunruiCode: '',
          genkakingaku_zeinuki: 0,
          genkakingaku_zeikomi: 0,
          genkakingaku_zeigaku: 0,
          genkazeiKBN: -1,
          genkazeiritsu: 0,
          sakugyoKBN: 0,
          TGDisabled: false
        }],
        TD00302_DeleteData: [],
      },
      
      iraiNaiyoParams: {
        keiyakuNo:'',
        hansu:'',
        rirekiNo:'',
        koubaisinseijoukyo:''
      },

      MitsumoriSyosaiNyuryokuParams:{
        keiyakuNo:'',
        hansu:'',
        rirekiNo:'',
        meisaiNo:'',
        isDisplayMode:false,
        genkazeiritsu:'',
        genkazeiKBN:'',
        cfmFlg:false
      },

      zumenShiryoTenpuParams:{
        業者コード: '',
        業者コード枝番: '',
        工事依頼No: '',
        枝番: '',

        契約No: '',
        契約履歴No: '',
        契約年月: '',
        見積明細No: '',
        予定年月: '',
        契約年月明細No: ''
      },

      genzyoShashinTenpuParams:{
        添付種類: '901',
        工事依頼No: ''
      },

      mitsumoriTorikomiParams:{
        keiyakuNo: '',
        hansu: '',
        rirekiNo: '',
        gyoshaCode: '',
        gyoshaCodeEdaban: ''
      },

      ZumenShiryoTenpuSabuParams:{
        業者コード: '',
        業者コード枝番: '',
        工事依頼No: '',
        枝番: '',
        契約No: '',
        契約履歴No: '',
        契約年月: '',
        見積明細No: '',
        予定年月: '',
        契約年月明細No: ''
      },

      kakoMitsumoriSansyoParams:{
        修繕定期区分:''
      },

      bukkenShiryoSansyoParams:{
        物件コード: ''
      },

      syuuzenKBNdai:'',
      irainaiyojoukyo:'',
      iraisyaCD:'',
      iraisyaTEL:'',
      iraisyadairiCD:'',
      iraisyadairi:'',
      iraisyadairiTEL:'',
      uketsukesyaCD:'',
      uketsukesya:'',
      uketsukesyaTEL:'',

      //mitsumoriSyosaiChangedFlg:'',

      data: [],
      data2: [],
      tableDataMeisai: [],
      tableDataSubTotal:[],
      tableDataDeleteMeisai: [],
      //tableData_zei:[],

      //明細NOの最大値
      MAXmeisaiNO:0,

      isFirstMode: true,
      isDisplayMode: true,
      maxRowMeisai: 30,
      showIraiNaiyoSabu: false,
      showTS00300: false,
      showTS00304: false,
      showTS00302: false,
      showTS00303: false,
      showTS00305: false,
      showTS00306: false,
      changeDataFlg: false,

      rules: {
        eigyoten: [
        //   { required: true, message: '入力してください', trigger: 'blur' },
        ],
      },

      single: false, //SingleSelet
        rowKey: undefined, //Treetable PK
        pagination: { total: 30, size: 10, sortName: "", page:1 },
      
      分類List: [
        { name: '', value: '' },
        { name: '見出', value: 'T' },
        { name: '小計', value: 'G' }
      ],

      税区List: [],

      cellStyleOptionMeisai: {
        headerCellClass: ({ row, column, rowIndex }) => { },
        bodyCellClass: ({ row, column, rowIndex }) => {
          let css = ''
          if (row.bunruiCode != 'T' && row.bunruiCode != 'G' && row.sagyomeisyo != "" &&
            column.key * 1 >= 10 && column.key * 1 != 11 && column.key * 1 != 18 &&
            this.isNotEmptyRow(row)) {
              css += ' is-yl'
          } 

          // No,順番,クリア,コピー,分類,契約No,作業内容,数量,単位,単価,金額（税抜）,税区,税率,作業
          if (
            ['1', '2', '3', '4', '5', '6', '10', '12', '13', '14', '15', '16', '17', '18'].includes(column.key)
          ) {
            css += ' position-relative'
          }

          // 大中小区分
          if (column.key == '7' || column.key == '8' || column.key == '9') {
            css += ' is-gray-label'
            css += ' text-left'
          }

          // 分類,税区
          if (column.key == '5' || column.key == '16') {
            css += ' border-right-none'
          }

          return css
        }
      },

      isNotEmptyRow (row) {
        return row.sagyomeisyo || row.suryo || row.tani || row.tanka || row.genkakingaku_zeinuki || row.genkazeiKBN || row.genkazeiritsu || row.sagyoYotei
      },

      columnskensakukekka: [
        {
          field: 'No',
          key: '1',
          title: '№',
          width: 40,
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex" class="cell-body-custome">
                <span style="height: 100%;width: 100%;line-height: 26px;">
                  {rowIndex + 1}
                </span>
              </div>
            )
          }
        },
        {
          key: '2',
          title: '順番',
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div style="display: flex" class="cell-body-custome clsHansu">
                <div>
                  <el-button
                    on-click={() => {
                      this.onNextBackMeisai(row, rowIndex, true)
                    }}
                    disabled={this.isDisplayMode}
                    size="mini"
                  >
                    <i class="el-icon-caret-top" />
                  </el-button>
                </div>
                <div>
                  <el-button
                    on-click={() => {
                      this.onNextBackMeisai(row, rowIndex)
                    }}
                    disabled={this.isDisplayMode}
                    size="mini"
                  >
                    <i class="el-icon-caret-bottom" />
                  </el-button>
                </div>
              </div>
            )
          },
          width: 53
          // fixed: 'left'
        },
        {
          key: '3',
          title: 'クリア',
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onDeleteMeisai(row, rowIndex)
                  }}
                  disabled={row['addMeisaiFLG'] == '1' || this.isDisplayMode}
                  size="mini"
                  style="background: #d3d3d3; border-color: #b7b5b5"
                >
                  クリア
                </el-button>
              </div>
            )
          },
          width: 58
          // fixed: 'left'
        },
        {
          key: '4',
          title: 'コピー',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onCopyMeisai(row, rowIndex)
                  }}
                  disabled={this.isDisplayMode}
                  size="mini"
                >
                  &#x21B3;
                </el-button>
              </div>
            )
          },
          width: 45
          // fixed: 'left'
        },
        {
          key: '5',
          title: '分類',
          renderBodyCell: ({ row, column, rowIndex }, h) => {
            return (
              <div class="cell-body-custome">
                <el-select
                  value={row['bunruiCode']}
                  placeholder=""
                  style="width: 100%;"
                  disabled={this.isDisplayMode}
                  on-change={(val) => {
                    row['bunruiCode'] = val
                    this.checkChangedData('グリッド_分類')
                    this.onMeisaiBunruiChange(row, column, rowIndex)
                  }}
                >
                  {this.分類List.map(function (obj, i) {
                    return (
                      <el-option label={obj.name} value={obj.value} key={i} />
                    )
                  })}
                </el-select>
              </div>
            )
          },
          width: 100
          // fixed: 'left'
        },
        {
          key: '6',
          title: '契約No・物件',
          field: 'keiyakuNoBukkenmei',
          width: 200
        },
        {
          key: '7',
          title: '作業カテゴリ大区分',
          field: 'daiKBN',
          width: 125,
          // fixed: 'left',
          ellipsis: {
            showTitle: false
          }
        },
        {
          key: '8',
          title: '作業カテゴリ中区分',
          field: 'tyuKBN',
          width: 125,
          display: false,
          // fixed: 'left',
          ellipsis: {
            showTitle: true
          }
        },
        {
          key: '9',
          title: '作業カテゴリ小区分',
          field: 'syoKBN',
          width: 125,
          // fixed: 'left',
          ellipsis: {
            showTitle: true
          }
        },
        {
          key: '10',
          title: '作業内容',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputtext
                  maxlength={50}
                  value={row.sagyomeisyo}
                  ref={'ref作業内容' + rowIndex}
                  width="100%"
                  disabled={this.isDisplayMode}
                  on-input={(val) => {
                    this.checkChangedData('グリッド_作業内容')
                    row['sagyomeisyo'] = val
                  }}
                ></reafsinputtext>
              </div>
            )
          },
          width: 200
        },
        {
          key: '11',
          title: '作業',
          renderBodyCell: ({ row, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-button
                  on-click={() => {
                    this.onTeikiMitsumoriSyosaiNyuryokuClick(row, rowIndex)
                  }}
                  // disabled={this.isDisplayMode}
                  size="mini"
                  style="border-color: #b7b5b5"
                >
                  入力
                </el-button>
              </div>
            )
          },
          width: 65
        },
        {
          key: '12',
          title: '数量',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber2
                  intLength={6}
                  floatLength={2}
                  value={row.suryo}
                  ref={'ref数量' + rowIndex}
                  width="100%"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-input={(val) => {
                    this.checkChangedData('グリッド_数量')
                    row['suryo'] = val
                  }}
                  on-change={(val) => {
                    this.txtSuryoChanged(row, val)
                  }}
                ></reafsinputnumber2>
              </div>
            )
          },
          width: 100
        },
        {
          key: '13',
          title: '単位',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputtext
                  maxlength={10}
                  value={row.tani}
                  ref={'ref単位' + rowIndex}
                  width="100%"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-input={(val) => {
                    row['tani'] = val
                    // this.cellDataChange(row, column, val)
                  }}
                ></reafsinputtext>
              </div>
            )
          },
          width: 100
        },
        {
          key: '14',
          title: '単価',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tanka.toLocaleString()}
                  ref={'ref単価' + rowIndex}
                  width="100%"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-input={(val) => {
                    this.checkChangedData('グリッド_単価')
                    row['tanka'] = val
                  }}
                ></reafsinputnumber>
              </div>
            )
          },
          width: 165
        },
        {
          key: '15',
          title: '金額（税抜）',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.genkakingaku_zeinuki.toLocaleString()}
                  ref={'ref金額_税抜' + rowIndex}
                  width="100%"
                  disabled="true"
                  on-input={(val) => {
                    row['genkakingaku_zeinuki'] = val
                    // this.cellDataChange(row, column, val)
                  }}
                ></reafsinputnumber>
              </div>
            )
          },
          width: 165
        },
        {
          key: '16',
          title: '税区',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <el-select
                  value={row['genkazeiKBN']}
                  placeholder=""
                  style="width: 100%;"
                  disabled={row.TGDisabled || this.isDisplayMode}
                  on-change={(val) => {
                    row['genkazeiKBN'] = val
                    this.checkChangedData('グリッド_税区')
                    this.onMeisaiZeiKBNChange(row, column, rowIndex)
                  }}
                >
                  {this.税区List.map(function (object, i) {
                    return (
                      <el-option
                        label={object.名称}
                        value={object.名称コード}
                        key={i}
                      />
                    )
                  })}
                </el-select>
              </div>
            )
          },
          width: 105
        },
        {
          key: '17',
          title: '税率',
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div style="display: flex;" class="cell-body-custome">
                <div>
                  <reafsinputnumber
                    intLength={2}
                    floatLength={0}
                    value={row.genkazeiritsu}
                    ref={'ref税率' + rowIndex}
                    disabled={row.TGDisabled || this.isDisplayMode || row['genkazeiKBN'] == '2'}
                    width="100%"
                    on-input={(val) => {
                      row['genkazeiritsu'] = val
                      this.checkChangedData('グリッド_税率')
                      // this.cellDataChange(row, column, val)
                    }}
                    on-change={val => {
                      this.txtgenkazeiritsuChanged(row, val);
                    }}
                  ></reafsinputnumber>
                </div>
                <div style="display: flex">
                  <span style="padding: 0 5px;line-height: 25px; width: 25px color:black">{row.bunruiCode != 'G' && row.bunruiCode != 'T' ? '%' : ''}</span>
                </div>
              </div>
            )
          },
          width: 95
        },
        {
          key: '18',
          title: '作業予定',
          field: 'sagyoYotei',
          width: 75
        }
      ],

      columnsSubTotal: [
        {
          field: 'GenkaZeiritsu',
          key: '1',
          title: '税率',
          width: 50
        },
        {
          field: 'KingakuZeinuki',
          key: '2',
          title: '合計金額（税抜）',
          align: 'right',
          width: 120
        },
        {
          field: 'KingakuZeigaku',
          key: '3',
          title: '税額',
          align: 'right',
          width: 120
        },
        {
          field: 'KingakuZeikomi',
          key: '4',
          title: '合計金額（税込）',
          align: 'right',
          width: 120
        }
      ],
          
      showDialog: false,
    };

  },
  created() {
    
  },
  
  mounted() {
    
  },

  methods: {
    pageBack(obj){
      this.pagination.page = obj
    },

    onSubmit(){
      console.log(1);
    },

    initialize(){
      this.tableDataMeisai = []
      this.searchForm.keiyakumeisyo = ""
      this.searchForm.iraiNO = ""
      this.searchForm.keiyakukanriNO = ""
      this.searchForm.teisyutsusaki = ""
      this.searchForm.iraisya = ""
      this.searchForm.mitsumoriiraibi = ""
      this.searchForm.keiyakukikan = "　~　"
      this.searchForm.joutai = ""
      
      this.searchForm.bukkenCD = ""
      this.searchForm.bukkenmei = ""
      this.searchForm.postCD = ""
      this.searchForm.juusyo = ""
      this.searchForm.mitsumoriKingaku = ""
      this.searchForm.mitsumoriyuukoukikan = ""
      this.searchForm.mitsumoritantousya = ""
      this.searchForm.tantousyaTEL = ""
      this.searchForm.SGRrenrakujiko = ""
      this.searchForm.mitsumorisyohyoujibiko = ""
      this.searchForm.teianbikou_koment = ""
      this.syuuzenKBNdai = ""
      this.irainaiyojoukyo = ""
      this.iraisyaCD = ""
      this.iraisyaTEL = ""
      this.iraisyadairiCD = ""
      this.iraisyadairi = ""
      this.iraisyadairiTEL = ""
      this.uketsukesyaCD = ""
      this.uketsukesya = ""
      this.uketsukesyaTEL = ""

      this.searchForm.keiyakuNo = this.$route.params.keiyakuNO;
      this.searchForm.hansu = this.$route.params.hansu;    
      this.searchForm.rirekiNO = this.$route.params.rirekiNO; 
      console.log(this.$route.params.keiyakuNO);
      
      this.http
        .get('/api/Reafs_T/TeikiMitsumoriNyuryoku/Get税区DataSource', 'アクセスしています....')
        .then((response) => {
          this.税区List = response.data
        });

      // 検索処理起動
      // テストデータは削除、あると何かと勘違いを引き起こす。
      this.GetMitsumoriData();
      
    },

    footerDataSubTotal () {
      console.log("footerDatasubTotalStart")
      let sum合計金額税抜 = 0
      let sum税額 = 0
      let sum合計金額税込 = 0
      let obj = this.defaultDataSubTotal()

      for(let i = 0; i < this.tableDataSubTotal.length; i++) {
        if (this.tableDataSubTotal[i].GenkaZeiritsu !== '合計') {
          sum合計金額税抜 += Number(this.tableDataSubTotal[i].KingakuZeinuki.replaceAll(',', ''))
          sum税額 += Number(this.tableDataSubTotal[i].KingakuZeigaku.replaceAll(',', ''))
          sum合計金額税込 += Number(this.tableDataSubTotal[i].KingakuZeikomi.replaceAll(',', ''))
        }
      }

      console.log("zeikomi" + sum合計金額税込 + " zeigaku" + sum税額 + " zeinuki" + sum合計金額税抜)
      this.searchForm.mitsumoriKingaku = '￥' + this.base.formatCommaNumeric(sum合計金額税込)

      obj.GenkaZeiritsu = '合計'
      obj.KingakuZeinuki = this.base.formatCommaNumeric(sum合計金額税抜)
      obj.KingakuZeigaku = this.base.formatCommaNumeric(sum税額)
      obj.KingakuZeikomi = this.base.formatCommaNumeric(sum合計金額税込)

      this.tableDataSubTotal.push(obj)
    },

    async MitsumoriSyosaiNyuryokuClose(e){
      
      console.log("作業予定サブ画面を閉じました！")

      // 詳細入力が終わったら画面再描画
      await this.GetMitsumoriData()
      
      // 明細内小計行再計算
      await this.checkBunruiCodeData()

      this.changeDataFlg = false;
    },
    
    async search() {
      if (this.searchForm.hansu == "" || this.searchForm.hansu.trim() == "")
      {
        return this.$message.error("版数を入力してください。");
      }

      let _this = this
      //var data = []
      await this.http
        .post("/api/Reafs_T/TeikiMitsumoriNyuryoku/search", this.searchForm,"検索しています....")
        .then((result) => {
          if (!result.status) {
            return this.$message.error(result.message);
          }

          if (result.data.length == 0) {
            _this.searchForm.keiyakumeisyo = ""
            _this.searchForm.iraiNO = ""
            _this.searchForm.keiyakukanriNO = ""
            _this.searchForm.teisyutsusaki = ""
            _this.searchForm.iraisya = ""
            _this.searchForm.mitsumoriiraibi = ""
            _this.searchForm.keiyakukikan = "　~　"
            _this.searchForm.joutai = ""
            
            _this.searchForm.bukkenCD = ""
            _this.searchForm.bukkenmei = ""
            _this.searchForm.postCD = ""
            _this.searchForm.juusyo = ""
            _this.searchForm.mitsumoriKingaku = ""
            _this.searchForm.mitsumoriyuukoukikan = ""
            _this.searchForm.mitsumoritantousya = ""
            _this.searchForm.tantousyaTEL = ""
            _this.searchForm.SGRrenrakujiko = ""
            _this.searchForm.mitsumorisyohyoujibiko = ""
            _this.searchForm.teianbikou_koment = ""
            _this.syuuzenKBNdai = ""
            _this.irainaiyojoukyo = ""
            _this.iraisyaCD = ""
            _this.iraisyaTEL = ""
            _this.iraisyadairiCD = ""
            _this.iraisyadairi = ""
            _this.iraisyadairiTEL = ""
            _this.uketsukesyaCD = ""
            _this.uketsukesya = ""
            _this.uketsukesyaTEL = ""

            //非表示
            _this.koubaisinseijoukyo = ""

            // メール送信用
            _this.searchForm.sendMailJimusyoCode = ""
            _this.searchForm.sendMailEigyosyoCode = ""
            _this.searchForm.sendMailBuCode = ""
            _this.searchForm.sendMailKaCode = ""
            _this.searchForm.sendMailKakariCode = ""

            // 一時保存時に必要なデータ群
            _this.KeyDataToTD00302.kaishaCode = ""
            _this.KeyDataToTD00302.keiyakuNo = ""
            _this.KeyDataToTD00302.keiyakuNoEdaban = ""
            _this.KeyDataToTD00302.rirekiNo = ""
            _this.KeyDataToTD00302.aimitsumoriNo = ""
            _this.KeyDataToTD00302.iraiNo = ""
            _this.KeyDataToTD00302.gyoshaCode = ""
            _this.KeyDataToTD00302.gyoshaCodeEdaban = ""
            _this.KeyDataToTD00302.keiyakuNendo = ""

            _this.fnc_void_showInfoMsg("E000014");
            return;
          }
          
          _this.searchForm.keiyakumeisyo = result.data[0].keiyakumeisyo
          _this.searchForm.iraiNO = result.data[0].iraiNO
          _this.searchForm.keiyakukanriNO = result.data[0].keiyakukanriNO
          _this.searchForm.teisyutsusaki = result.data[0].syuzenKBNsyo
          _this.searchForm.iraisya = result.data[0].iraisya
          _this.searchForm.mitsumoriiraibi = result.data[0].iraibi
          _this.searchForm.keiyakukikan = result.data[0].keiyakuYMS + "~" +result.data[0].keiyakuYME
          _this.searchForm.joutai = result.data[0].joutai

          // 継続区分が保留(1)データは「保留」と表示する
          if (result.data[0].keizokuKBN == "1") {
            _this.searchForm.joutai = "保留"
          }
          // 継続区分が中止(2)データは「中止」と表示する
          if (result.data[0].keizokuKBN == "2") {
            _this.searchForm.joutai = "中止"
          }

          _this.searchForm.shinchokuCode = result.data[0].shinchokuCode
          
          _this.searchForm.bukkenCD = result.data[0].bukkenCD
          _this.searchForm.bukkenmei = result.data[0].bukkenmei
          _this.searchForm.postCD = result.data[0].postCD
          _this.searchForm.juusyo = result.data[0].juusyo
          //_this.searchForm.mitsumoriKingaku = '￥' + result.data[0].mitsumoriKingaku
          _this.searchForm.mitsumoriyuukoukikan = result.data[0].mitsumoriyuukoukikan
          _this.searchForm.mitsumoritantousya = result.data[0].torihikisakitanntousyamei
          _this.searchForm.tantousyaTEL = result.data[0].torihikisakiTEL
          _this.searchForm.SGRrenrakujiko = result.data[0].genkabiko_mitsumorisyohihyouji
          _this.searchForm.mitsumorisyohyoujibiko = result.data[0].genkabiko_mitsumorisyohyouji
          _this.searchForm.teianbikou_koment = result.data[0].teianbikou_koment
          _this.syuuzenKBNdai = result.data[0].syuuzenKBNdai
          _this.irainaiyojoukyo = result.data[0].irainaiyojoukyo
          _this.iraisyaCD = result.data[0].iraisyaCD
          _this.iraisyaTEL = result.data[0].iraisyaTEL
          _this.iraisyadairiCD = result.data[0].iraisyadairiCD
          _this.iraisyadairi = result.data[0].iraisyadairi
          _this.iraisyadairiTEL = result.data[0].iraisyadairiTEL
          _this.uketsukesyaCD = result.data[0].uketsukesyaCD
          _this.uketsukesya = result.data[0].uketsukesya
          _this.uketsukesyaTEL = result.data[0].uketsukesyaTEL

          _this.searchForm.kakomitsumorisansyo = ''
          _this.searchForm.defaultTax = result.data[0].defaultTax

          //非表示
          _this.koubaisinseijoukyo = result.data[0].koubaisinseijoukyo
          
          // メール送信用
          _this.searchForm.sendMailJimusyoCode = result.data[0].sendMailJimusyoCode
          _this.searchForm.sendMailEigyosyoCode = result.data[0].sendMailEigyosyoCode
          _this.searchForm.sendMailBuCode = result.data[0].sendMailBuCode
          _this.searchForm.sendMailKaCode = result.data[0].sendMailKaCode
          _this.searchForm.sendMailKakariCode = result.data[0].sendMailKakariCode

          console.log("メール送信用パラメータ：" + _this.searchForm.sendMailJimusyoCode + " " + _this.searchForm.sendMailEigyosyoCode
            + " " + _this.searchForm.sendMailBuCode + " " + _this.searchForm.sendMailKaCode + " " + _this.searchForm.sendMailKakariCode);

          _this.searchForm.keizokuKBN = result.data[0].keizokuKBN

          // 一時保存時に必要なデータ群
          _this.KeyDataToTD00302.kaishaCode = result.data[0].kaishaCode
          _this.KeyDataToTD00302.keiyakuNo = _this.searchForm.keiyakuNo 
          _this.KeyDataToTD00302.keiyakuNoEdaban = _this.searchForm.hansu
          _this.KeyDataToTD00302.rirekiNo = _this.searchForm.rirekiNO
          _this.KeyDataToTD00302.aimitsumoriNo = result.data[0].aimitsumoriNO
          _this.KeyDataToTD00302.iraiNo = result.data[0].iraiNO
          _this.KeyDataToTD00302.gyoshaCode = result.data[0].gyoshaCode
          _this.KeyDataToTD00302.gyoshaCodeEdaban = result.data[0].gyoshaCodeEdaban
          _this.KeyDataToTD00302.keiyakuNendo = result.data[0].keiyakuNendo

          if (_this.searchForm.shinchokuCode == '110')
          {
            // 提出ボタンが押せない
            _this.isFirstMode = true
            _this.isDisplayMode = false
          }
          else if(_this.searchForm.shinchokuCode >= '200')
          {
            // 各テキストボックス、一時保存ボタン、提出ボタンが押せない
            _this.isFirstMode = false
            _this.isDisplayMode = true
          }
          else
          {
            // 全てのボタンが押下可能
            _this.isFirstMode = false
            _this.isDisplayMode = false
          }

          // 継続区分が保留(1)または、中止(2)のデータは全ての更新が行なえない
          if (_this.searchForm.keizokuKBN == "1" || _this.searchForm.keizokuKBN == "2") 
          {
            // 各テキストボックス、一時保存ボタン、提出ボタンが押せない
            _this.isFirstMode = false
            _this.isDisplayMode = true
          }

          _this.changeDataFlg = false
        })
        
    },
    
    async search2() {
      let i = 0
      let maxrow = 30
      console.log("search2_start");
      await this.http
        .post("/api/Reafs_T/TeikiMitsumoriNyuryoku/search2", this.searchForm,"検索しています....")
        .then((result2) => {
          if (!result2.status)
          {
            return this.$message.error(result.message);
          }

          this.tableDataMeisai = result2.data
          this.KeyDataToTD00302.TD00302_GridData = result2.data
          this.searchForm.TD00302_GridData = result2.data
          console.log(this.tableDataMeisai);

          for (let row in this.tableDataMeisai)
          {
            if(this.tableDataMeisai[row].bunruiCode == 'T' || this.tableDataMeisai[row].bunruiCode == 'G')
            {
              this.tableDataMeisai[row].TGDisabled = true
              console.log(row + ' ' + this.tableDataMeisai[row].genkakingaku_zeikomi);
            } else
            {
              this.tableDataMeisai[row].TGDisabled = false
            }
          }

          if (this.tableDataMeisai.length < 10 && !this.isDisplayMode)
          {
            for (let i = this.tableDataMeisai.length; i < 10; i++) {
              this.tableDataMeisai.push(this.defaultDataMeisai())
            }
          }

        })

    },

    async search3() {
      let i = 0

      console.log("Search3_Start");

      await this.http
        .post("/api/Reafs_T/TeikiMitsumoriNyuryoku/search3", this.searchForm,"検索しています....")
        .then((result3) => {
          if (!result3.status) {
            return this.$message.error(result3.message);
          }

          this.tableDataSubTotal = []
          for(let i = 0; i < result3.data.length; i++) {
            const obj = this.defaultDataSubTotal()

            obj.GenkaZeiritsu = result3.data[i].GenkaZeiritsu
            obj.KingakuZeinuki = result3.data[i].KingakuZeinuki.toLocaleString()
            obj.KingakuZeigaku = result3.data[i].KingakuZeigaku.toLocaleString()
            obj.KingakuZeikomi = result3.data[i].KingakuZeikomi.toLocaleString()

            this.tableDataSubTotal.push(obj)
          }

          if (this.tableDataSubTotal.length < 4) {
            for (let i = this.tableDataSubTotal.length; i < 4; i++) {
              this.tableDataSubTotal.push(this.defaultDataSubTotal())
            }
          }

          this.footerDataSubTotal();
          
        })
        
    },

    async onReference () {
      console.log("過去見積参照開始");

      if (this.searchForm.kakomitsumorisansyo === '') {
        // 過去参照見積NOが空だったらエラー
        this.MsgErr({
          title: "見積入力（定期委託）",
          message: "過去見積NOが未入力です。"
        });
        return
      }

      let pattern = /[\d]{11}-[\d]{2}/
      if (!pattern.test(this.searchForm.kakomitsumorisansyo)) {
        // 見積NOが契約NO(11桁)+"-"+契約NO枝番(2桁)の構成でなければエラーとする。
        this.MsgErr({
          title: "見積入力（定期委託）",
          message: "過去見積NOが不適切です。"
        });

        this.$refs.reftxtKakoSansho.focus()
        return
      }

      let result
      const data = {
        keiyakuNo: this.searchForm.keiyakuNo,
        hansu: this.searchForm.hansu,
        rirekiNO: this.searchForm.rirekiNO,
        aimitsumoriNo: this.searchForm.aimitsumoriNO,
        keiyakuNendo:  this.KeyDataToTD00302.keiyakuNendo,
        kaishaCode: this.KeyDataToTD00302.kaishaCode,
        refKeiyakuNo: this.searchForm.kakomitsumorisansyo.split('-')[0],
        refHansu: this.searchForm.kakomitsumorisansyo.split('-')[1],
        refRirekiNo: this.searchForm.kakomitsumorisansyo.split('-')[2],
      }

      await this.http
        .post("/api/Reafs_T/TeikiMitsumoriNyuryoku/Reference", data,"検索しています....")
        .then((res) => {
          result = res
        })
        .finally(() => {
        })

      if (result.status) {
        if (!result.data || result.data.length <= 0) {
          this.getMsg('E010224').then((response) => {
            this.showError(response.data)
          })
        } else {
          //this.GetMitsumoriData()
          this.tableDataMeisai = result.data
          if (this.tableDataMeisai.length < 10) {
            for (let i = this.tableDataMeisai.length; i < 10; i++) {
              this.tableDataMeisai.push(this.defaultDataMeisai())
            }
          }
        }
      } else {
        this.showError(result.message)
      }
    },

    async GetMitsumoriData(){
      await this.search();
      await this.search2();
      await this.search3();
      this.changeDataFlg = false;
      //this.$message.error(this.iraisyaCD);
    },

    defaultDataMeisai () {
      return {
        meisaiNO: '',
        meisaiSORT: '',
        bunruiCode: '',
        keiyakuNoBukkenmei: '',
        daiKBN: '',
        tyuKBN: '',
        syoKBN: '',
        sagyomeisyo: '',
        suryo: '',
        tani: '',
        tanka: 0,
        genkakingaku_zeinuki: 0,
        genkakingaku_zeikomi: 0,
        genkakingaku_zeigaku: 0,
        genkazeiKBN: -1,
        genkazeiritsu: 0,
        syosaiFLG: 0,
        addMeisaiFLG: 0,
        sagyoKaisu: 0,
        genkaKingakuType: 0,
        genkaKingakuTanka: 0,
        deleteFLG: 0
      }
    },

    defaultDataSubTotal () {
      return {
        GenkaZeiritsu: '',
        KingakuZeinuki: '',
        KingakuZeigaku: '',
        KingakuZeikomi: ''
      }
    },

    onBack () {
      console.log("戻るよー")
      
      if (this.changeDataFlg === true) {
        this.fnc_rtn_getMsg('K000001').then(response => {
          this.MsgConf(
            {
              title: "見積入力(定期委託)",
              message: response.data
            },
            () => {
              // 遷移元画面へ戻る
              this.$router.back()
            },
            () => {
              return false;
            }
          );
        });
      } else {
        // 遷移元画面へ戻る
        this.$router.back()
      }
    },

    OnClick(KBN){
      if (KBN==0){
        console.log("一時保存実行")
        let convertNengetsu = ''
        var gridTableData = [];
        var tantouTEL_bit = this.fnc_obj_getByteLength(
          this.searchForm.tantousyaTEL
        );

        if (this.searchForm.mitsumoriyuukoukikan == ''){
          // 見積有効期間未入力
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "見積有効期間が未入力です。"
          });
          return;
        }else if(this.searchForm.mitsumoritantousya == ''){
          // 見積担当者未入力
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "見積担当者が未入力です。"
          });
          return;
        }else if(this.searchForm.tantousyaTEL == ''){
          // 見積担当者連絡先未入力
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "見積担当者連絡先が未入力です。"
          });
          return ;
        }else if(tantouTEL_bit > 15){
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "見積担当者連絡先は最大半角15文字で入力してください。"
          });
        }

        var torokuGridDate = [];
        var gridTableData = [];
        var tmpSortNO = 1;
        gridTableData = this.tableDataMeisai

        // 今ある明細NOの最大値を取得する。
        const gridTableDataMeisaiNo = gridTableData.map((x) => x.meisaiNO)
        var tmpMeisaiNO = Math.max.apply(null, gridTableDataMeisaiNo);

        // DB登録対象かどうかの判別を行なう
        for (let row in gridTableData)
        {
          console.log(row);
          
          if (gridTableData[row].sagyomeisyo != "" || gridTableData[row].bunruiCode != '') {  
            // 明細表示順については登録毎に番号振り直し
            gridTableData[row].meisaiSORT = tmpSortNO;

            if(gridTableData[row].meisaiNO === ''){
              // 新規行には最大値+1の明細NOをセット
              tmpMeisaiNO += 1;
              gridTableData[row].meisaiNO = tmpMeisaiNO;
            }

            if(gridTableData[row].bunruiCode == '')
            {
              if(gridTableData[row].sagyomeisyo === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "作業名称が未入力です。"
                });
                return;
              }
              if(gridTableData[row].suryo === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "数量が未入力です。"
                });
                return;
              }
              if(gridTableData[row].tani === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "単位が未入力です。"
                });
                return;
              }
              if(gridTableData[row].tanka === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "単価が未入力です。"
                });
                return;
              }
              if(gridTableData[row].genkazeiKBN !== 1 && gridTableData[row].genkazeiKBN !== 2)
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "税区分が未設定です。"
                });
                return;
              }
              if(gridTableData[row].genkazeiritsu === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "税率が未設定です。"
                });
                return;
              }
            } else if (gridTableData[row].bunruiCode == 'T')
            {
              if(gridTableData[row].sagyomeisyo === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "作業名称が未入力です。"
                });
                return;
              }
            }

            torokuGridDate.push(gridTableData[row]);
            tmpSortNO += 1;
          }
        }

        //gridTableData = this.KeyDataToTD00302.TD00302_GridData

        //一時保存
        this.KeyDataToTD00302.keiyakuMeisho = this.searchForm.keiyakumeisyo;
        this.KeyDataToTD00302.mitsumoriTantosha = this.searchForm.mitsumoritantousya;
        this.KeyDataToTD00302.mitsumoriTantoRenrakusaki = this.searchForm.tantousyaTEL;
        convertNengetsu = this.fnc_obj_NengetsuConvert(this.searchForm.mitsumoriyuukoukikan);
        this.KeyDataToTD00302.mitsumoriYukoKikan = convertNengetsu;
        this.KeyDataToTD00302.genkaBiko = this.searchForm.mitsumorisyohyoujibiko;
        this.KeyDataToTD00302.comment = this.searchForm.teianbikou_koment;

        console.log(torokuGridDate)

        this.fnc_void_showTourokuMsg(torokuGridDate);

      }else if(KBN==1){
        console.log("物件資料参照ボタン押下");

        this.bukkenShiryoSansyoParams.物件コード = this.searchForm.bukkenCD

        this.showTS00304 = true;
        return;
      }else if(KBN==2){
        //見積データ取込
        console.log("見積データ取込ボタン押下");

        this.mitsumoriTorikomiParams.keiyakuNo = this.searchForm.keiyakuNo;
        this.mitsumoriTorikomiParams.hansu = this.searchForm.hansu;
        this.mitsumoriTorikomiParams.rirekiNo = this.searchForm.rirekiNO;
        this.mitsumoriTorikomiParams.gyoshaCode = this.KeyDataToTD00302.gyoshaCode;
        this.mitsumoriTorikomiParams.gyoshaCodeEdaban = this.KeyDataToTD00302.gyoshaCodeEdaban;
        
        this.showTS00305 = true;
        return;
      }else if(KBN==3){
        //写真添付
        console.log("写真添付ボタン押下");
        console.log("iraiNo: " + this.searchForm.iraiNO);
        
        this.genzyoShashinTenpuParams.工事依頼No = this.searchForm.iraiNO

        this.showTS00303 = true;
        return;
      }else if(KBN==4){
        //図面・資料添付
        console.log("図面資料添付ボタン押下");

        this.ZumenShiryoTenpuSabuParams.業者コード = this.KeyDataToTD00302.gyoshaCode;
        this.ZumenShiryoTenpuSabuParams.業者コード枝番 = this.KeyDataToTD00302.gyoshaCodeEdaban;
        this.ZumenShiryoTenpuSabuParams.契約No = this.searchForm.keiyakuNo;
        this.ZumenShiryoTenpuSabuParams.契約履歴No = this.searchForm.rirekiNO;
        this.ZumenShiryoTenpuSabuParams.枝番 = this.searchForm.hansu;
        this.ZumenShiryoTenpuSabuParams.契約見積NO = this.searchForm.hansu;
        this.ZumenShiryoTenpuSabuParams.見積明細No = this.searchForm.hansu;
        //this.ZumenShiryoTenpuSabuParams.契約年月 = this.searchForm.;
        console.log(this.ZumenShiryoTenpuSabuParams);

        this.showTS00302 = true;
        return;
      }else if(KBN==5){
        //提出
        console.log("提出処理実行")

        var gridTableData = [];

        if (this.searchForm.mitsumoriyuukoukikan == ''){
          // 見積有効期間未入力
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "見積有効期間が未入力です。"
          });
          return;
        }else if(this.searchForm.mitsumoritantousya == ''){
          // 見積担当者未入力
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "見積担当者が未入力です。"
          });
          return;
        }else if(this.searchForm.tantousyaTEL == ''){
          // 見積担当者連絡先未入力
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "見積担当者連絡先が未入力です。"
          });
          return ;
        }

        var torokuGridDate = [];
        var gridTableData = [];
        var tmpSortNO = 1;
        gridTableData = this.tableDataMeisai

        // 今ある明細NOの最大値を取得する。
        const gridTableDataMeisaiNo = gridTableData.map((x) => x.meisaiNO)
        var tmpMeisaiNO = Math.max.apply(null, gridTableDataMeisaiNo);

        // DB登録対象かどうかの判別を行なう
        for (let row in gridTableData)
        {
          console.log(row);
          
          if (gridTableData[row].sagyomeisyo != "" || gridTableData[row].bunruiCode != '') {  
            // 明細表示順については登録毎に番号振り直し
            gridTableData[row].meisaiSORT = tmpSortNO;

            if(gridTableData[row].meisaiNO === ''){
              // 新規行には最大値+1の明細NOをセット
              tmpMeisaiNO += 1;
              gridTableData[row].meisaiNO = tmpMeisaiNO;
            }

            if(gridTableData[row].bunruiCode == '')
            {
              if(gridTableData[row].sagyomeisyo === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "作業名称が未入力です。"
                });
                return;
              }
              if(gridTableData[row].suryo === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "数量が未入力です。"
                });
                return;
              }
              if(gridTableData[row].tani === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "単位が未入力です。"
                });
                return;
              }
              if(gridTableData[row].tanka === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "単価が未入力です。"
                });
                return;
              }
              if(gridTableData[row].genkazeiKBN === -1)
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "税区分が未設定です。"
                });
                return;
              }
              if(gridTableData[row].genkazeiritsu === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "税率が未設定です。"
                });
                return;
              }
              if(gridTableData[row].syosaiFLG === 0)
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "詳細が登録されていない明細があるため提出できません。"
                });
                return;
              }
            } else if (gridTableData[row].bunruiCode == 'T')
            {
              if(gridTableData[row].sagyomeisyo === '')
              {
                this.MsgErr({
                  title: "見積入力（定期委託）",
                  message: "作業名称が未入力です。"
                });
                return;
              }
            }

            torokuGridDate.push(gridTableData[row]);
            tmpSortNO += 1;
          }
        }

        if (torokuGridDate.length == 0){
          // 見積担当者連絡先未入力
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "登録明細が0件です。"
          });
          return ;
        }
        console.log(torokuGridDate);

        this.KeyDataToTD00302.keiyakuMeisho = this.searchForm.keiyakumeisyo;
        this.KeyDataToTD00302.mitsumoriTantosha = this.searchForm.mitsumoritantousya;
        this.KeyDataToTD00302.mitsumoriTantoRenrakusaki = this.searchForm.tantousyaTEL;
        this.KeyDataToTD00302.mitsumoriYukoKikan = this.searchForm.mitsumoriyuukoukikan;
        this.KeyDataToTD00302.genkaBiko = this.searchForm.mitsumorisyohyoujibiko;
        this.KeyDataToTD00302.comment = this.searchForm.teianbikou_koment;

        this.fnc_void_showUpdateMsg(torokuGridDate);

      }else{
        
      }
      
    },

    MitsumoriDataTorikomiOnBackData (e) {

      if (e.backData) {
        
        // 作業初期化
        this.tableDataMeisai = []

        console.log(e.backData.取込データ)

        for (var torikomiRow in e.backData.取込データ){

          var row = this.defaultDataMeisai()

          row.bunruiCode = e.backData.取込データ[torikomiRow].bunruiCode
          row.sagyomeisyo = e.backData.取込データ[torikomiRow].sagyomeisyo
          row.suryo = e.backData.取込データ[torikomiRow].suryo
          row.tani = e.backData.取込データ[torikomiRow].tani
          row.tanka = e.backData.取込データ[torikomiRow].tanka
          row.genkazeiKBN = Number(e.backData.取込データ[torikomiRow].genkazeiKBN)
          row.genkazeiritsu = e.backData.取込データ[torikomiRow].genkazeiritsu

          this.tableDataMeisai.push(row)
        }

        if (this.tableDataMeisai.length < 10 && !this.isDisplayMode)
        {
          for (let i = this.tableDataMeisai.length; i < 10; i++) {
            this.tableDataMeisai.push(this.defaultDataMeisai())
          }
        }

      }
    },

    fnc_void_showTourokuMsg (torokuGridDate) {
      this.fnc_rtn_getMsg('K040063').then(response => {
          this.MsgConf(
            {
              title: "見積入力(定期委託)",
              message: response.data
            },
            () => {
              // OKが選択された場合
              //登録結果取得
              this.fnc_void_TmpSave(torokuGridDate);
              return true;
            },
            () => {
              // Cancelが選択された場合
              // this.$alert('Cancelが選択された場合')
              return false;
            }
          );
        });
    },

    async fnc_void_TmpSave (torokuGridDate) {
      let _this = this;

      this.KeyDataToTD00302.TD00302_GridData = torokuGridDate

      //登録データ渡し
      let letval_touroku = new Object();

      letval_touroku = this.KeyDataToTD00302;

      console.log(letval_touroku);

      await this.http
        .post(
          "/api/Reafs_T/TeikiMitsumoriNyuryoku/TmpSave",
          letval_touroku,
          "登録しています...."
        )
        .then(function(response) {
          if (response.status) {
            _this.fnc_void_showInfoMsg("M000008")
            _this.GetMitsumoriData()
          }
        });
    },

    fnc_void_showUpdateMsg (torokuGridDate)
    {
      if (this.changeDataFlg === true) {
        this.fnc_rtn_getMsg('K000001').then(response => {
          this.MsgConf(
            {
              title: "見積入力(定期委託)",
              message: response.data
            },
            () => {
              this.fnc_void_showSubmissionMsg()
            },
            () => {
              return false;
            }
          );
        });
      } else {
        this.fnc_void_showSubmissionMsg()
      }
    },

    fnc_void_showSubmissionMsg()
    {
      this.fnc_rtn_getMsg('K040064').then(response => {
          this.MsgConf(
            {
              title: "見積入力(定期委託)",
              message: response.data
            },
            () => {
              // OKが選択された場合
              //this.fnc_void_TmpSave(torokuGridDate);
              this.fnc_void_Submission();
              return true;
            },
            () => {
              // Cancelが選択された場合
              // this.$alert('Cancelが選択された場合')
              return false;
            }
          );
        });
    },

    async fnc_void_Submission ()
    {
      let _this = this;
      var torokuGridDate = [];
      var gridTableData = [];

      gridTableData = _this.KeyDataToTD00302.TD00302_GridData

      for (let row in gridTableData)
      {
        console.log(row);

        if (gridTableData[row].sagyomeisyo != "") {
          torokuGridDate.push(gridTableData[row]);
        }
      }

      _this.KeyDataToTD00302.TD00302_GridData = torokuGridDate

      //登録データ渡し
      let letval_touroku = new Object();

      letval_touroku = _this.KeyDataToTD00302;

      console.log(letval_touroku);

      await _this.http
        .post(
          "/api/Reafs_T/TeikiMitsumoriNyuryoku/Submission",
          letval_touroku,
          "登録しています...."
        )
        .then(function(response) {
          if (response.status) {
            _this.print();
            _this.fnc_void_showInfoMsg("M000008");
            _this.fnc_void_sendMail();
            _this.GetMitsumoriData();
          }
        });
    },

    fnc_void_sendMail() {
      let resultMail = "";
      let patternNo = 0;
      if (this.KeyDataToTD00302.aimitsumoriNo == 1) {
        patternNo = 6 // 見積完了通知
      }else{
        patternNo = 8 // 相見積完了通知
      }

      console.log("メール送信用パラメータ：" + this.searchForm.sendMailJimusyoCode + " " + this.searchForm.sendMailEigyosyoCode
            + " " + this.searchForm.sendMailBuCode + " " + this.searchForm.sendMailKaCode + " " + this.searchForm.sendMailKakariCode);

      let param = {
        msg_type: 0,
        pg_id: 'TD00302',
        st_kbn: 2,
        mail_pattern_no: patternNo,
        工事依頼NO: this.searchForm.iraiNO,
        契約NO: this.searchForm.keiyakuNo,
        契約No枝番: this.searchForm.hansu,
        履歴NO: this.searchForm.rirekiNO,
        宛先事務所コード: [this.searchForm.sendMailJimusyoCode],
        宛先営業所コード: [this.searchForm.sendMailEigyosyoCode],
        宛先部コード: [this.searchForm.sendMailBuCode],
        宛先課コード: [this.searchForm.sendMailKaCode],
        宛先係コード: [this.searchForm.sendMailKakariCode],
      }

      resultMail = this.commonFunctionUI.CM00110SendMail(param)

      if (resultMail.result_id !== 0){
        console.log("メールエラー！ " + resultMail.err_msg)
        // メール送信時のエラー
      }
    },

    KakoMitsumoriSanshoSabuonBackData (e) {
      if (e.見積No) {
        this.searchForm.kakomitsumorisansyo = e.見積No
      }
    },

    async print() {
      // 見積書出力に必要なデータを取ってくるための検索項目
      const data = {
        keiyakuNo: this.searchForm.keiyakuNo,
        hansu: this.searchForm.hansu,
        rirekiNO: this.searchForm.rirekiNO,
        gyoshaCode: this.KeyDataToTD00302.gyoshaCode,
        gyoshaCodeEdaban: this.KeyDataToTD00302.gyoshaCodeEdaban
      }

      await this.http
        .post(
          "/api/Reafs_T/TeikiMitsumoriNyuryoku/OUTPUT",
          data,
          "見積書作成中...",
          {
            responseType: "blob"
          }
        )
        .then(async blob => {
          if (blob.size === 0) {
            this.fnc_void_showErrorMsg("E010224");
            return;
          }
          // const fileHandle = await window.showSaveFilePicker({
          //   suggestedName: "見積書.pdf"
          // });

          // const fileStream = await fileHandle.createWritable();
          // await fileStream.write(blob);
          // await fileStream.close();
          // this.fnc_void_showInfoMsg("M000008");

          //await this.base.saveFileDownload(blob, '見積書.pdf')
        })
        .finally(() => {})
        .catch(ex => console.log(ex));
    },

    changeHansu (isLeft) {
      //console.log("changeHansu");

      // 2桁の版数を10の位と1の位にそれぞれ分離
      let num1 = this.searchForm.hansu.slice(-2, 1);
      let num2 = Number(this.searchForm.hansu.slice(-1) || 0)
      let result;

      // 最小値1、最大値9の範囲で1の位を増減させる
      num2 = isLeft ? (num2 > 1 ? num2 - 1 : num2) : num2 < 9 ? num2 + 1 : num2
      
      // 分離させたそれぞれの版数を文字連結
      result = num1 + String(num2)

      // 結果をテキストボックスに格納
      this.searchForm.hansu = result.toLocaleString('en-US', {
        minimumIntegerDigits: 2,
        useGrouping: false
      })
    },

    onIraiNaiyoHyojiClick(){
      
      this.iraiNaiyoParams.keiyakuNo = this.searchForm.keiyakuNo;
      this.iraiNaiyoParams.hansu = this.searchForm.hansu;
      this.iraiNaiyoParams.rirekiNo = this.searchForm.rirekiNO;
      
      this.showIraiNaiyoSabu = true;
    },

    onNextBackMeisai (row, rowIndex, isBack) {
      const newIndex = isBack ? rowIndex - 1 : rowIndex + 1
      this.moveArrayItemToNewIndex(this.tableDataMeisai, rowIndex, newIndex)

      this.checkBunruiCodeData()
    },

    checkBunruiCodeData()
    {
      console.log("再計算！")

      var sum = 0
      var sumRowIndex = 0
      var titleFind = true

      for (var i = this.tableDataMeisai.length - 1 ; i >= 0; i--)
      {
        if (this.tableDataMeisai[i].bunruiCode != 'T' && this.tableDataMeisai[i].bunruiCode != 'G') {
          if(!titleFind)
          {
            // タイトル行検索中なら加算
            sum += Number(String(this.tableDataMeisai[i].genkakingaku_zeinuki).replaceAll(',', ''))
          }          
          
        } else if (this.tableDataMeisai[i].bunruiCode == 'T') {
          if(!titleFind)
          {
            // タイトル行発見、それまでの合計を合計行にセット
            titleFind = true
            this.tableDataMeisai[sumRowIndex].genkakingaku_zeinuki = sum.toLocaleString()
            sum = 0;
          }
          else
          {
            // いきなりタイトル行が出てきたパターン、何もしなくていいはず
          }
          
          continue

        } else if (this.tableDataMeisai[i].bunruiCode == 'G') {
          if (!titleFind)
          {
            //タイトル行のない小計行
            this.tableDataMeisai[sumRowIndex].bunruiCode = ''
            this.tableDataMeisai[sumRowIndex].sagyomeisyo = ''
            this.tableDataMeisai[sumRowIndex].suryo = ''
            this.tableDataMeisai[sumRowIndex].tani = ''
            this.tableDataMeisai[sumRowIndex].tanka = 0
            this.tableDataMeisai[sumRowIndex].genkakingaku_zeinuki = 0
            this.tableDataMeisai[sumRowIndex].genkazeiritsu = 0
            this.tableDataMeisai[sumRowIndex].genkazeiKBN = -1
            this.tableDataMeisai[sumRowIndex].sagyoYotei = ''
            sumRowIndex = i
            sum = 0;
            continue
          }
          else
          {
            // タイトル行検索開始
            titleFind = false
            sumRowIndex = i
          }

        }
      }

      if(!titleFind)
      {
        //タイトル行のない小計行
        this.tableDataMeisai[sumRowIndex].bunruiCode = ''
        this.tableDataMeisai[sumRowIndex].sagyomeisyo = ''
        this.tableDataMeisai[sumRowIndex].suryo = ''
        this.tableDataMeisai[sumRowIndex].tani = ''
        this.tableDataMeisai[sumRowIndex].tanka = 0
        this.tableDataMeisai[sumRowIndex].genkakingaku_zeinuki = 0
        this.tableDataMeisai[sumRowIndex].genkazeiritsu = 0
        this.tableDataMeisai[sumRowIndex].genkazeiKBN = -1
        this.tableDataMeisai[sumRowIndex].sagyoYotei = ''
        sumRowIndex = i
        sum = 0;
      }
    },

    moveArrayItemToNewIndex (arr, oldIndex, newIndex) {
      if (newIndex < 0 || newIndex >= arr.length) {
        return
      }
      arr.splice(newIndex, 0, arr.splice(oldIndex, 1)[0])
      return arr
    },

    onDeleteMeisai (row, rowIndex) {
      
      console.log("グリッド内クリアボタン押下");
      
      if (row.meisaiNO !== ''){
        this.tableDataDeleteMeisai.push(this.tableDataMeisai[rowIndex])
      }
      this.tableDataMeisai.splice(rowIndex, 1)
      this.tableDataMeisai.push(this.defaultDataMeisai())

      this.checkBunruiCodeData();
    },

    onCopyMeisai (row, rowIndex) {

      console.log("グリッド内コピーボタン押下");

      let meisaiCount = 0;
      for (let i in this.tableDataMeisai)
      {
        if (this.tableDataMeisai[i].meisaiNO != '')
        {
          meisaiCount += 1;
        }
      }

      if (this.tableDataMeisai.length == meisaiCount) 
      {
        return;
      }

      const newRow = JSON.parse(JSON.stringify(row));
      newRow.meisaiNO = '';
      newRow.meisaiSORT = '';
      newRow.syosaiFLG = 0;
      newRow.sagyoYotei = '未予定';
      
      this.tableDataMeisai.splice(rowIndex + 1, 0, newRow);
      console.log(this.tableDataMeisai);

      this.checkBunruiCodeData();
    },

    onTeikiMitsumoriSyosaiNyuryokuClick(row, rowIndex) {

      let _this = this;


      console.log("グリッド内入力ボタン押下");

      // 分類コードがTまたはGの場合、作業ボタンは押せない。
      if (row.bunruiCode == 'T' || row.bunruiCode == 'G') {
        return;
      }

      // 新規行の場合、作業ボタンは押せない
      if (row.meisaiNO == '') {
        return;
      }
      if (_this.changeDataFlg === true) {
        this.fnc_rtn_getMsg('K000001').then(response => {
          this.MsgConf(
            {
              title: "見積入力(定期委託)",
              message: response.data
            },
            () => {
              this.MitsumoriSyosaiNyuryokuParams.keiyakuNo = this.searchForm.keiyakuNo;
              this.MitsumoriSyosaiNyuryokuParams.hansu = this.searchForm.hansu;
              this.MitsumoriSyosaiNyuryokuParams.rirekiNo = this.searchForm.rirekiNO;
              this.MitsumoriSyosaiNyuryokuParams.meisaiNo = row.meisaiNO;
              this.MitsumoriSyosaiNyuryokuParams.isDisplayMode = this.isDisplayMode;
              this.MitsumoriSyosaiNyuryokuParams.genkazeiritsu = row.genkazeiritsu;
              this.MitsumoriSyosaiNyuryokuParams.genkazeiritsu = row.genkazeiritsu;
              this.MitsumoriSyosaiNyuryokuParams.mitsumoriSyosaiChangedFlg = this.mitsumoriSyosaiChangedFlg

              this.showTS00306 = true;
            },
            () => {
              return false;
            }
          );
        });
      } else {
        // 分類コードがTまたはGの場合、作業ボタンは押せない。
        if (row.bunruiCode == 'T' || row.bunruiCode == 'G') {
          return;
        }

        // 新規行の場合、作業ボタンは押せない
        if (row.meisaiNO == '') {
          return;
        }

        this.MitsumoriSyosaiNyuryokuParams.keiyakuNo = this.searchForm.keiyakuNo;
        this.MitsumoriSyosaiNyuryokuParams.hansu = this.searchForm.hansu;
        this.MitsumoriSyosaiNyuryokuParams.rirekiNo = this.searchForm.rirekiNO;
        this.MitsumoriSyosaiNyuryokuParams.meisaiNo = row.meisaiNO;
        this.MitsumoriSyosaiNyuryokuParams.isDisplayMode = this.isDisplayMode;
        this.MitsumoriSyosaiNyuryokuParams.genkazeiritsu = row.genkazeiritsu;
        this.MitsumoriSyosaiNyuryokuParams.genkazeiritsu = row.genkazeiritsu;
        this.MitsumoriSyosaiNyuryokuParams.mitsumoriSyosaiChangedFlg = this.mitsumoriSyosaiChangedFlg

        this.showTS00306 = true;
      }
    },

    onMeisaiBunruiChange (row, column, index) {
      
      console.log("分類が変わりましたよ")
      var genkakingaku = row.genkakingaku_zeinuki
      console.log(genkakingaku);

      row.sagyomeisyo = ''

      // 各非活性要素のchangeイベントを無理やり発火させて再評価させてる。
      row.suryo = 1
      row.suryo = 0
      row.tani = ' '
      row.tani = ''
      row.tanka = 1
      row.tanka = 0
      row.genkakingaku_zeinuki = 1
      // 原価金額税抜は行分類が変わったらクリアする
      row.genkakingaku_zeinuki = 0
      row.genkazeiritsu = 1
      row.genkazeiritsu = 0
      row.genkazeiKBN = 1
      row.genkazeiKBN = -1

      if (row.bunruiCode != 'T' && row.bunruiCode != 'G') {
        row.TGDisabled = false
        this.checkBunruiCodeData()
      } else if (row.bunruiCode == 'T') {
        row.TGDisabled = true
        this.checkBunruiCodeData()
      } else if (row.bunruiCode == 'G') {
        console.log("計算開始");
        row.TGDisabled = true

        let sum = 0
        let titleFind = false

        for (var i = index; i >= 0; i--)
        {
          if (this.tableDataMeisai[i].bunruiCode == 'T')
          {
            titleFind = true
            break;
          }

          sum += Number(String(this.tableDataMeisai[i].genkakingaku_zeinuki).replaceAll(',', ''))
        }

        if (titleFind)
        {
          row.genkakingaku_zeinuki = sum.toLocaleString()
        }
        else
        {
          // 見出し行が見つからなかった
          row.bunruiCode = ''
          row.TGDisabled = false
          this.MsgErr({
            title: "見積入力（定期委託）",
            message: "集計行を設定する際は対応する見出行を作成してください。"
          });
        }
      }
    },

    onMeisaiZeiKBNChange(row, column, index) {

      console.log("税区分を変えましたわ～！");

      if (row.genkazeiKBN == 1)
      {
        row.genkazeiritsu = this.searchForm.defaultTax
      }
       else if (row.genkazeiKBN == 2)
      {
        row.genkazeiritsu = 0
        row.genkakingaku_zeigaku = 0
      }

      this.checkBunruiCodeData();
    },

    onAddRowMeisai () {
      console.log("行追加するよー");

      if (this.tableDataMeisai.length == this.maxRowMeisai){
        // 30行以上あったら追加しない
        this.fnc_void_showInfoMsg("E010277");
      }

      const iInsertRow =
        this.tableDataMeisai.length < 10 ? 10 - this.tableDataMeisai.length
          : this.tableDataMeisai.length + 5 <= this.maxRowMeisai
            ? 5
            : this.maxRowMeisai - this.tableDataMeisai.length

      for (let i = 0; i < iInsertRow; i++) {
        this.tableDataMeisai.push(this.defaultDataMeisai())
      }
    },

    onRenewalData () {
      this.GetMitsumoriData();
    },

    IraiNaiyoSabuClose (e) {
    },

    fnc_void_showInfoMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgInfo({
          title: "見積入力(定期委託)",
          message: response.data
        });
      });
      return 1;
    },

    ///////////////////////////////////////////////
    // keyからS016メッセージマスタにアクセスしメッセージ取得
    ///////////////////////////////////////////////
    fnc_rtn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    ///////////////////////////////////////////////
    // 日付コンバート
    // DATE型を文字型のYYYY/MM/ddにコンバートする
    ///////////////////////////////////////////////
    fnc_obj_NengetsuConvert(sagyoudate) {
      console.log("日付コンバート");

      if (sagyoudate.length == 10){
        return sagyoudate;
      }

      let letYear = new Object();
      let letMonth = new Object();
      let letday = new Object();
      let letNengetsu = new Object();

      letYear = sagyoudate.getFullYear();
      letMonth = sagyoudate.getMonth() + 1;
      letMonth = ("00" + letMonth).slice(-2);
      letday = sagyoudate.getDate();
      letday = ("00" + letday).slice(-2);
      letNengetsu = letYear + "/" + letMonth + "/" + letday;

      return letNengetsu;
    },

    checkChangedData (ctrl) {
      console.log("変更フラグがたったよ " + ctrl);
      this.changeDataFlg = true
    },

    txtSuryoChanged(row, val){
      var targetPosition = val.toString().indexOf('-')

      if (targetPosition > -1) {
        row.suryo = ''
      }
    },

    replaceAll (val) {
      return ((val || '') + '').replaceAll(',', '')
    },
    
    async txtgenkazeiritsuChanged (row, val) {
      await this.http
        .get('/api/Reafs_T/TeikiMitsumoriNyuryoku/check税期間', { genkazeiritsu: val })
        .then((response) => {
          if (response.data !== null) 
          {
            if (response.data.isTaxExist == false)
            {
              this.MsgErr({
                title: "見積入力（定期委託）",
                message: "入力された税率は有効ではありません。"
              });
              // 入力された税率は存在しないのでデフォルトの税率をセット
              row.genkazeiritsu = this.searchForm.defaultTax
            }
          }
          else 
          {
            this.MsgErr({
                title: "見積入力（定期委託）",
                message: "入力された税率は有効ではありません。"
              });
              // 入力された税率は存在しないのでデフォルトの税率をセット
              row.genkazeiritsu = this.searchForm.defaultTax
          }
        });
    },

    async txtgenkazeiritsuChanged (row, val) {
      await this.http
        .get('/api/Reafs_T/TeikiMitsumoriNyuryoku/check税期間', { genkazeiritsu: val })
        .then((response) => {
          if (response.data !== null) 
          {
            if (response.data.isTaxExist == false)
            {
              this.MsgErr({
                title: "見積入力（定期委託）",
                message: "入力された税率は有効ではありません。"
              });
              // 入力された税率は存在しないのでデフォルトの税率をセット
              row.genkazeiritsu = this.searchForm.defaultTax
            }
          }
          else 
          {
            this.MsgErr({
                title: "見積入力（定期委託）",
                message: "入力された税率は有効ではありません。"
              });
              // 入力された税率は存在しないのでデフォルトの税率をセット
              row.genkazeiritsu = this.searchForm.defaultTax
          }
        });
    },

    ///////////////////////////////////////////////
    // バイトチェック
    ///////////////////////////////////////////////
    fnc_obj_getByteLength(str) {
      str = str == null ? "" : str;
      return encodeURI(str).replace(/%../g, "*").length;
    }
  }
};
</script>
<style lang="less">


.MitsumoriNyuryokuTeiki .el-col {
  border-radius: 4px;
}

.MitsumoriNyuryokuTeiki .bg-purple-dark {
  background: #99a9bf;
}
.MitsumoriNyuryokuTeiki .bg-purple {
  background: #d3dce6;
}
.MitsumoriNyuryokuTeiki .grid-content {
  border-radius: 4px;
  min-height: 36px;
  
}
.MitsumoriNyuryokuTeiki .row-bg {
  padding: 1x 0;
  background-color:lightgray;
  height: 30px;
}

.MitsumoriNyuryokuTeiki .is-yl,
.MitsumoriNyuryokuTeiki .is-yl * {
  background-color: lightgoldenrodyellow !important;
}

.MitsumoriNyuryokuTeiki .is-gray-label,
.is-gray-label * {
  background-color: #d3d3d3 !important;
}
  
.MitsumoriNyuryokuTeiki .Serch-contianer{
  padding: 10px;
  width:1560px;
}
.MitsumoriNyuryokuTeiki .date-picker{
  width: 250px;
}
.MitsumoriNyuryokuTeiki .el-button--mini, .MitsumoriNyuryokuTeiki .el-button--mini.is-round{
  padding: 5px 6px;
}
.MitsumoriNyuryokuTeiki .MitsumoriNyuryokuTeiki .el-button--mini {
  padding: 5px 6px;
}

.MitsumoriNyuryokuTeiki .el-input-len-5 {
    width:60px;
 }
 .MitsumoriNyuryokuTeiki .el-input-serach-len-5 {
    width:130px;
 }
 .MitsumoriNyuryokuTeiki .el-input-len-10 {
   width:140px;
 }
 .MitsumoriNyuryokuTeiki .el-input-date-len-10 {
   width:100px;
 }
 .MitsumoriNyuryokuTeiki .el-input-date-len-20 {
   width:200px;
 }
 .MitsumoriNyuryokuTeiki .el-input-len-20 {
   width:100px;
 }
 .MitsumoriNyuryokuTeiki .el-input-len-60 {
   width:300px;
 }
 .MitsumoriNyuryokuTeiki .el-input-len-200 {
   width:300px;
 }
 .MitsumoriNyuryokuTeiki .el-input-date-len-200{
   width:480px;
 }
.MitsumoriNyuryokuTeiki .textarea-h200{
  resize:none;
  height:140px;
  max-height:200px;
}

.MitsumoriNyuryokuTeiki .grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.MitsumoriNyuryokuTeiki .hidden{
  display:none;
}

 .MitsumoriNyuryokuTeiki .br{
  display:block;
  content:"";
  height:35px;
 }

.MitsumoriNyuryokuTeiki .inline-block{
  display:inline-block;
  floot:left;
}

.MitsumoriNyuryokuTeiki .footer{
  position:absolute;
  bottom:0;
}

.MitsumoriNyuryokuTeiki .btn_updown{
  width:30px;
  padding:0px;
  box-sizing:border-box;
  border:1px;
  cursor:pointer;
}

.MitsumoriNyuryokuTeiki .tblMeisai button {
height: 96%;
width: 96%;
}

.MitsumoriNyuryokuTeiki .tblMeisai .ve-table-body .el-input__inner {
  height: 29px !important;
  padding: 0 5px;
}

.MitsumoriNyuryokuTeiki .tblMeisai .ve-table-container {
  min-height: 340px;
}

.MitsumoriNyuryokuTeiki .tblMeisai input{
  color: #000 !important
}

.MitsumoriNyuryokuTeiki .el-row {
  margin-bottom: 14px;
}

.MitsumoriNyuryokuTeiki .text-center {
  text-align: center !important;
  vertical-align: middle !important;
}

.MitsumoriNyuryokuTeiki .text-left {
  text-align: left !important;
}

.MitsumoriNyuryokuTeiki .input-font-20 input {
  font-size: 19px;
  height: 40px !important;
  text-align: right;
}

.MitsumoriNyuryokuTeiki .input-font-20 .el-form-item__label {
  margin-top: 10px !important;
}

.MitsumoriNyuryokuTeiki .position-relative {
  position: relative;
}

.MitsumoriNyuryokuTeiki .is-red {
  color: red;
}

.MitsumoriNyuryokuTeiki .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr td.ve-table-body-td, .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-expand-tr td.ve-table-body-td,
.MitsumoriNyuryokuTeiki .ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.ve-table-header-th {
  padding: 0px;
}

.MitsumoriNyuryokuTeiki .ve-table-content .ve-table-body tr:not([style*="height: 0px"]),
.MitsumoriNyuryokuTeiki .ve-table-footer-tr {
  height: 30px !important;
}
.MitsumoriNyuryokuTeiki .tblMeisai .ve-table-body .el-input__inner {
  height: 29px !important;
  padding: 0 5px;
}

</style>
