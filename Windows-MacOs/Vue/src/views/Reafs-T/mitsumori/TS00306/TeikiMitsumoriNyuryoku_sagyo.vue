<template>
  <div class="Main-contianer">
    <el-form ref="refMitsumoriDetaTorikomiForm" label-width="120px" class="sample-form-class"
      :hide-required-asterisk="true">
      <el-row>
        <!-- １行目 -->
        <el-col :span="12">
          <div style="display: flex">
            <el-form-item label="契約№">
              <reafsinputtext v-model="searchForm.keiyakuNO" class="is-readonly" maxlength="12" disabled="disabled"
                :width="'160px'">
              </reafsinputtext>
            </el-form-item>

            <el-form-item label="依頼№" style="margin-left: 8px;">
              <reafsinputtext v-model="searchForm.iraiNO" :title="searchForm.iraiNO" class="is-readonly" maxlength="60"
                disabled="disabled" :width="'120px'">
              </reafsinputtext>
            </el-form-item>

            <el-form-item label="契約管理番号" style="margin-left: 8px;">
              <reafsinputtext v-model="searchForm.keiyakukanriNO" :title="searchForm.keiyakukanriNO" class="is-readonly" maxlength="60"
                disabled="disabled" :width="'160px'">
              </reafsinputtext>
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <!-- ２行目 -->
        <el-col :span="12">
          <div style="display: flex">
            <el-form-item label="提出先">
              <reafsinputtext v-model="searchForm.teisyutsusaki" class="is-readonly" maxlength="12" disabled="disabled"
                :width="'160px'">
              </reafsinputtext>
            </el-form-item>

            <el-form-item label="依頼者" style="margin-left: 8px;">
              <reafsinputtext v-model="searchForm.iraisya" :title="searchForm.iraisya" class="is-readonly" maxlength="60"
                disabled="disabled" :width="'870px'">
              </reafsinputtext>
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <!-- ３行目 -->
        <el-col :span="24">
          <div style="display: flex">
            <el-form-item label="契約名称">
              <reafsinputtext v-model="searchForm.keiyakumeisyo" class="is-readonly" maxlength="12" disabled="disabled"
                :width="'525px'">
              </reafsinputtext>
            </el-form-item>

            <div style="margin-left: 8px; display: flex;">
              <el-label>見積依頼日：</el-label>
              <el-label><label>{{searchForm.iraibi}}</label></el-label>
            </div>

            <div style="margin-left: 60px; display: flex;">
              <el-label>契約期間：</el-label>
              <el-label><label>{{searchForm.keiyakukikan}}</label></el-label>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <!-- ４行目 -->
        <el-col :span="24">
          <div style="display: flex">
            <el-form-item label="物件">
              <div style="display: flex">
                <div>
                  <reafsinputtext v-model="searchForm.bukkenCD" class="is-readonly" maxlength="6" disabled="disabled"
                    :width="'105px'">
                  </reafsinputtext>
                </div>
                <div class="marginLeft">
                  <reafsinputtext class="is-readonly" :title="searchForm.bukkenmei" v-model="searchForm.bukkenmei" maxlength="20"
                    disabled="disabled" :width="'100%'">
                  </reafsinputtext>
                </div>
              </div>
            </el-form-item>
            <el-form-item label="物件住所" style="margin-left: 8px;">
              <div style="display: flex">
                <div>
                  <reafsinputtext class="is-readonly" v-model="searchForm.postCD" maxlength="8" disabled="disabled"
                    :width="'105px'">
                  </reafsinputtext>
                </div>
                <div class="marginLeft">
                  <reafsinputtext :title="searchForm.juusyo" class="is-readonly" v-model="searchForm.juusyo" maxlength="50"
                    disabled="disabled" :width="'100%'">
                  </reafsinputtext>
                </div>
              </div>
            </el-form-item>
          </div>
        </el-col>
      </el-row>

      <el-row>
        <!-- ５行目 -->
        <el-col :span="18">
          <el-form-item label="●見積情報" class="m-bottom-less">
          </el-form-item> 
        </el-col>
      </el-row>

      <el-row>
        <!-- ６行目 -->
        <el-col :span="24" Class="title">
          <el-label Class="m-bottom-less title-bg heder-width-2" :width="'40px'">№</el-label>
          <el-label Class="m-bottom-less title-bg heder-width-30" :width="'300px'">作業内容</el-label>      
          <el-label Class="m-bottom-less title-bg heder-width-6" :width="'60px'">数量</el-label>
          <el-label Class="m-bottom-less title-bg heder-width-8" :width="'80px'">単位</el-label>
          <el-label Class="m-bottom-less title-bg heder-width-12" :width="'120px'">単価</el-label>
          <el-label Class="m-bottom-less title-bg heder-width-16" :width="'165px'">金額（税抜）</el-label>
        </el-col>
      </el-row>

      <el-row>
        <!-- ７行目 -->
        <el-col :span="24" Class="detail">
          <div style="display: flex">
            <reafsinputnumber floatLength=0 v-model="searchForm.meisaiNO" class="is-readonly" maxlength="3" disabled="disabled" :width="'40px'">
            </reafsinputnumber>
            <reafsinputtext v-model="searchForm.sagyoumeisyo" class="is-readonly" maxlength="3" disabled="disabled" :width="'300px'">
            </reafsinputtext>
            <reafsinputnumber floatLength=0 v-model="searchForm.suryo" class="is-readonly" maxlength="3" disabled="disabled" :width="'60px'">
            </reafsinputnumber>
            <reafsinputtext v-model="searchForm.tani" class="is-readonly" maxlength="3" disabled="disabled" :width="'80px'">
            </reafsinputtext>
            <reafsinputnumber floatLength=0 v-model="searchForm.tanka" class="is-readonly" maxlength="3" disabled="disabled" :width="'120px'">
            </reafsinputnumber>
            <reafsinputnumber floatLength=0 v-model="searchForm.genkakingaku_zeinuki" class="is-readonly" maxlength="3" disabled="disabled" :width="'165px'">
            </reafsinputnumber>
          </div>
        </el-col>
      </el-row>
    </el-form>

    <el-row :gutter="10">
      <!-- ７行目 -->
      <el-col :span="5">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="120px"
        >
          <el-form-item label="間隔" Class="m-bottom-less">
            <reafsinputtext 
              v-model="searchForm.kankaku"
              maxlength="1"
              :width="'40px'"
              @change="txtKankakuChanged"
              :disabled="queryParams.isDisplayMode"
            >
            <template v-slot:append>
              <el-label>年ごと</el-label>
            </template>
            </reafsinputtext>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <el-col :span="24">
        <el-form 
          :model="searchForm"
          ref="searchForm"
          :rules="rules"
          label-width="120px"
        >

          <el-form-item label="" Class="m-bottom-less">
            <el-label class="heder-width-4">４月</el-label>
            <el-label class="heder-width-4">５月</el-label>
            <el-label class="heder-width-4">６月</el-label>
            <el-label class="heder-width-4">７月</el-label>
            <el-label class="heder-width-4">８月</el-label>
            <el-label class="heder-width-4">９月</el-label>
            <el-label class="heder-width-4">１０月</el-label>
            <el-label class="heder-width-4">１１月</el-label>
            <el-label class="heder-width-4">１２月</el-label>
            <el-label class="heder-width-4">１月</el-label>
            <el-label class="heder-width-4">２月</el-label>
            <el-label class="heder-width-4">３月</el-label>
          </el-form-item>  
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ７行目 -->
      <el-col :span="24">
          <el-form 
            :model="searchForm"
            ref="searchForm"
            :rules="rules"
            label-width="120px"
          >
            <el-form-item label="作業回数">
              <div style="display: flex">
                <reafsinputnumber
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu04"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数4"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(4)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model.number="searchForm.kaisu05"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数5"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(5)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu06"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数6"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(6)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu07"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数7"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(7)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu08"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数8"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(8)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu09"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数9"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(9)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu10"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数10"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(10)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu11"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数11"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(11)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu12"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数12"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(12)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu01"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数1"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(1)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu02"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数2"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(2)"
                ></reafsinputnumber>
                <reafsinputnumber 
                  :intLength="1"
                  :floatLength="0"
                  v-model="searchForm.kaisu03"
                  :required="false"
                  :width="'50px'"
                  ref="作業回数3"
                  Class="kaisu"
                  :disabled="queryParams.isDisplayMode"
                  @change="txtKaisuChanged(3)"
                ></reafsinputnumber>
                
                <!-- <el-label Class="row-bg kaisu">{{searchForm.kaisuSum}}</el-label> -->
                <reafsinputnumber floatLength=0 v-model="searchForm.kaisuSum" class="is-readonly kaisu" maxlength="3" disabled="disabled" :width="'50px'"></reafsinputnumber>
                <el-label>回</el-label>
            </div>
            </el-form-item>
          </el-form>
      </el-col>
      
    </el-row>
    
    作業金額
    <div Class="box-sagyokingaku">
    <el-row :gutter="10">
      <!-- ９行目 -->
      
      
        <el-col :span="12">
          <el-form
            ref="searchForm"
            :model="searchForm"
            label-width="150px"
          >
            
              <el-radio-group @change="typeSelectChanged" v-model="searchForm.genkaKingakuType" :disabled="this.queryParams.isDisplayMode">
                <el-radio :label="0">金額固定</el-radio>
                <el-radio :label="1">金額変動</el-radio>
                <el-radio :label="2">単価タイプ</el-radio>
              </el-radio-group>
            
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.genkaKingaku_tanka"
                :required="false"
                :width="'120px'"
                :disabled="tankaDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtGenkaKingakuTankaChanged()"
                >
              </reafsinputnumber>
          </el-form>
        </el-col>
      
    </el-row>

    <el-row :gutter="10">
      <el-col :span="24">
        <el-form 
          :model="searchForm"
          ref="searchForm"
          label-width="100px"
        >
          <el-col Class="title m-bottom-less">
            <el-label Class="heder-width-4 box-nengetsu">４月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">５月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">６月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">７月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">８月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">９月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">１０月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">１１月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">１２月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">１月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">２月</el-label>
            <el-label Class="heder-width-4 box-nengetsu">３月</el-label>
          </el-col>
        </el-form>
      </el-col>
     </el-row>

    <el-row :gutter="10">
      <el-col :span="24">
        <el-form 
          :model="searchForm"
          ref="searchForm"
          label-width="100px"
        >
          <el-col Class="title m-bottom-less">
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku04"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu04 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
             <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku05"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu05 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku06"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu06 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku07"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu07 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku08"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu08 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku09"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu09 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku10"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu10 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku11"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu11 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku12"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu12 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku01"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu01 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku02"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu02 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
              <reafsinputnumber
                :intLength="12"
                :floatLength="0"
                v-model="searchForm.kingaku03"
                :required="false"
                :width="'165px'"
                :disabled="searchForm.kaisu03 === '' || kingakuDisabled || queryParams.isDisplayMode"
                Class="kingaku"
                @change="txtKingakuChanged"
                >
              </reafsinputnumber>
          </el-col>
        </el-form>
      </el-col>
     </el-row>
    </div>

    <el-row :gutter="10">
      <el-col class="align-left" :span=24>
        ※作業がない月でも金額が発生する場合は「作業回数」に0を入力する。
        
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <div Class="box-touroku">
        <el-col class="align-right" style="width:1250px;">
          <el-button @click="OnClick" :disabled="queryParams.isDisplayMode">登録</el-button>
        </el-col>
      </div>
    </el-row>

    <!-- 非表示 -->
    <label v-bind:href="searchForm.rirekiNO" class="hidden">{{$route.params.rirekiNO}}</label>
    <label v-bind:href="searchForm.meisaiNO" class="hidden">{{$route.params.meisaiNO}}</label>
    <label v-bind:href="searchForm.zeiritsu" class="hidden">{{$route.params.zeiritsu}}</label>

  </div>
</template>
<script>

import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafsmodal from '@/components/modal/Modal.vue'
import ReafsInputNumber from '../../../../components/basic/ReafsControl/ReafsInputNumber.vue';

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
    reafsmodal
  },

  props: {
    kingakuDisabled: {
      type: Boolean,
      default: true
    },
    tankaDisabled: {
      type: Boolean,
      default: true
    },
    isCloseConfirm:{
      type: Boolean,
      default: false
    }
  },
  
  data () {
    return {
      queryParams: {
        keiyakuNo: '',
        hansu: '',
        rirekiNo: '',
        meisaiNo: '',
        isDisplayMode: false,
        genkazeiritsu: ''
      },

      searchForm:{
        keiyakuNO:'',
        rirekiNO:'',
        keiyakuNOEdaban:'',
        kaishaCode:'',
        aimitsumoriNO:'',
        keiyakuNendo:'',
        iraiNO:'',
        gyoshaCode:'',
        gyoshaCodeEdaban:'',
        keiyakukanriNO:'',
        syuzenKBNsyo:'',
        iraisyaCD:'',
        iraisya:'',
        iraisyaTEL:'',
        iraisyadairiCD:'',
        iraisyadairi:'',
        iraisyadairiTEL:'',
        uketsukesyaCD:'',
        uketsukesya:'',
        keiyakumeisyo:'',
        syuuzenKBNdai:'',
        bukkenCD:'',
        bukkenmei:'',
        postCD:'',
        juusyo:'',
        mitsumoriyuukoukikan:'',
        irainaiyojoukyo:'',
        torihikisakitanntousyamei:'',
        torihikisakiTEL:'',
        genkabiko_mitsumorisyohihyouji:'',
        genkabiko_mitsumorisyohyouji:'',
        teianbikou_koment:'',
        iraibi:'',
        keiyakuYMS:'',
        keiyakuYME:'',
        meisaiNO:'',
        sagyoumeisyo:'',
        suryo:'',
        kaisuSum: '',
        tani:'',
        tanka:'',
        genkakingaku_zeinuki:'',
        kankaku: 0,
        kaisu04: '',
        kaisu05: '',
        kaisu06: '',
        kaisu07: '',
        kaisu08: '',
        kaisu09: '',
        kaisu10: '',
        kaisu11: '',
        kaisu12: '',
        kaisu01: '',
        kaisu02: '',
        kaisu03: '',
        kingaku04: '',
        kingaku05: '',
        kingaku06: '',
        kingaku07: '',
        kingaku08: '',
        kingaku09: '',
        kingaku10: '',
        kingaku11: '',
        kingaku12: '',
        kingaku01: '',
        kingaku02: '',
        kingaku03: '',
        genkaKingaku_tanka: 0,
        genkaKingakuType: 0,
        genkazeiritsu: 0,
        genkazeiKBN: ''
      },

      putData:{
        keiyakuNO:'',
        rirekiNO:'',
        keiyakuNOEdaban:'',
        kaishaCode:'',
        aimitsumoriNO:'',
        keiyakuNendo:'',
        meisaiNO:'',
        kankaku:'',
        genkaKingakuType: 0,
        genkazeiritsu: 0,
        genkazeiKBN: '',
        tanka: '',
        TS00306_MontherData : []
      },

      changeCheckData:{
        keiyakuNO:'',
        rirekiNO:'',
        keiyakuNOEdaban:'',
        kaishaCode:'',
        aimitsumoriNO:'',
        keiyakuNendo:'',
        iraiNO:'',
        gyoshaCode:'',
        gyoshaCodeEdaban:'',
        keiyakukanriNO:'',
        syuzenKBNsyo:'',
        iraisyaCD:'',
        iraisya:'',
        iraisyaTEL:'',
        iraisyadairiCD:'',
        iraisyadairi:'',
        iraisyadairiTEL:'',
        uketsukesyaCD:'',
        uketsukesya:'',
        keiyakumeisyo:'',
        syuuzenKBNdai:'',
        bukkenCD:'',
        bukkenmei:'',
        postCD:'',
        juusyo:'',
        mitsumoriyuukoukikan:'',
        irainaiyojoukyo:'',
        torihikisakitanntousyamei:'',
        torihikisakiTEL:'',
        genkabiko_mitsumorisyohihyouji:'',
        genkabiko_mitsumorisyohyouji:'',
        teianbikou_koment:'',
        iraibi:'',
        keiyakuYMS:'',
        keiyakuYME:'',
        meisaiNO:'',
        sagyoumeisyo:'',
        suryo:'',
        kaisuSum: '',
        tani:'',
        tanka:'',
        genkakingaku_zeinuki:'',
        kankaku: '',
        kaisu04: '',
        kaisu05: '',
        kaisu06: '',
        kaisu07: '',
        kaisu08: '',
        kaisu09: '',
        kaisu10: '',
        kaisu11: '',
        kaisu12: '',
        kaisu01: '',
        kaisu02: '',
        kaisu03: '',
        kingaku04: 0,
        kingaku05: 0,
        kingaku06: 0,
        kingaku07: 0,
        kingaku08: 0,
        kingaku09: 0,
        kingaku10: 0,
        kingaku11: 0,
        kingaku12: 0,
        kingaku01: 0,
        kingaku02: 0,
        kingaku03: 0,
        genkaKingaku_tanka: 0,
        genkaKingakuType: 0,
        genkazeiritsu: 0,
        genkazeiKBN: ''
      },

      tableData: [],
      
      rules: {
        
      },

      single: false, //SingleSelet
        rowKey: undefined, //Treetable PK
        pagination: { total: 22, size: 10, sortName: "", page:1 },
      
      cfmFlg: false
    }
    
  },
  watch: {
    'cfmFlg' (n, o) {
      window.parent.postMessage(
        {
          call: 'chcfmFlg',
          n: n,
          o: o
        },
        '*'
      )
    }
  },
  mounted() {
    console.log(this.$route.query);
    
    this.queryParams = {
      keiyakuNo: this.$route.query.keiyakuNo,
      hansu: this.$route.query.hansu,
      rirekiNo: this.$route.query.rirekiNo,
      meisaiNo: this.$route.query.meisaiNo,
      isDisplayMode: this.$route.query.isDisplayMode === 'true',
      genkazeiritsu: this.$route.query.genkazeiritsu
    }

    this.cfmFlg = this.$route.query.cfmFlg === 'true'
    this.hideLeftMenu(false);
    this.GetMitsumoriData();
  },

  inject: ['hideLeftMenu'],

  methods: {
    onSubmit(){
      console.log(1);
    },

    initialize(){
      
    },

    checkChangedData() {
      if (this.searchForm.kankaku !== this.changeCheckData.kankaku)
      {
        this.cfmFlg = true
        return
      }

      if (this.searchForm.genkaKingakuType !== this.changeCheckData.genkaKingakuType)
      {
        this.cfmFlg = true
        return        
      }

      if (this.searchForm.kaisu04 !== this.changeCheckData.kaisu04)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu05 !== this.changeCheckData.kaisu05)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu06 !== this.changeCheckData.kaisu06)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu07 !== this.changeCheckData.kaisu07)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu08 !== this.changeCheckData.kaisu08)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu09 !== this.changeCheckData.kaisu09)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu10 !== this.changeCheckData.kaisu10)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu11 !== this.changeCheckData.kaisu11)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu12 !== this.changeCheckData.kaisu12)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu01 !== this.changeCheckData.kaisu01)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu02 !== this.changeCheckData.kaisu02)
      {
        this.cfmFlg = true
        return
      }
      if (this.searchForm.kaisu03 !== this.changeCheckData.kaisu03)
      {
        this.cfmFlg = true
        return
      }

      if (Number(this.searchForm.kingaku04.replaceAll(',', '')) !== this.changeCheckData.kingaku04)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku05.replaceAll(',', '')) !== this.changeCheckData.kingaku05)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku06.replaceAll(',', '')) !== this.changeCheckData.kingaku06)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku07.replaceAll(',', '')) !== this.changeCheckData.kingaku07)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku08.replaceAll(',', '')) !== this.changeCheckData.kingaku08)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku09.replaceAll(',', '')) !== this.changeCheckData.kingaku09)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku10.replaceAll(',', '')) !== this.changeCheckData.kingaku10)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku11.replaceAll(',', '')) !== this.changeCheckData.kingaku11)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku12.replaceAll(',', '')) !== this.changeCheckData.kingaku12)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku01.replaceAll(',', '')) !== this.changeCheckData.kingaku01)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku02.replaceAll(',', '')) !== this.changeCheckData.kingaku02)
      {
        this.cfmFlg = true
        return
      }
      if (Number(this.searchForm.kingaku03.replaceAll(',', '')) !== this.changeCheckData.kingaku03)
      {
        this.cfmFlg = true
        return
      }

      // 変更なし
      this.cfmFlg = false
      return
    },

    txtKankakuChanged() {
      // 変更チェック
      this.checkChangedData()
    },

    txtKingakuChanged() {
      // 変更チェック
      this.checkChangedData()
    },
    
    typeSelectChanged(){
      console.log("typechanged")
      
      let type = this.searchForm.genkaKingakuType
      this.checkChangedData()

      if (type === 2)
      {
        if (this.searchForm.kaisu04 === ''){
          this.searchForm.kingaku04 = 0;
        } else {
          this.searchForm.kingaku04 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu05 === ''){
          this.searchForm.kingaku05 = 0;
        } else {
          this.searchForm.kingaku05 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu06 === ''){
          this.searchForm.kingaku06 = 0;
        } else {
          this.searchForm.kingaku06 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu07 === ''){
          this.searchForm.kingaku07 = 0;
        } else {
          this.searchForm.kingaku07 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu08 === ''){
          this.searchForm.kingaku08 = 0;
        } else {
          this.searchForm.kingaku08 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu09 === ''){
          this.searchForm.kingaku09 = 0;
        } else {
          this.searchForm.kingaku09 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu10 === ''){
          this.searchForm.kingaku10 = 0;
        } else {
          this.searchForm.kingaku10 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu11 === ''){
          this.searchForm.kingaku11 = 0;
        } else {
          this.searchForm.kingaku11 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu12 === ''){
          this.searchForm.kingaku12 = 0;
        } else {
          this.searchForm.kingaku12 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu01 === ''){
          this.searchForm.kingaku01 = 0;
        } else {
          this.searchForm.kingaku01 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu02 === ''){
          this.searchForm.kingaku02 = 0;
        } else {
          this.searchForm.kingaku02 = this.searchForm.genkaKingaku_tanka;
        }
        if (this.searchForm.kaisu03 === ''){
          this.searchForm.kingaku03 = 0;
        } else {
          this.searchForm.kingaku03 = this.searchForm.genkaKingaku_tanka;
        }

        this.kingakuDisabled = true
        this.tankaDisabled = false
      } 
      else if(type === 0 || type ===1)
      {
        this.searchForm.genkaKingaku_tanka = 0
        this.kingakuDisabled = false
        this.tankaDisabled = true
      }
    },

    txtKaisuChanged(month){
      let sumKaisu = 0
      let exception = false;
      console.log("回数変わったよ");
      this.checkChangedData()

      if (month == 4){
        if (5 < Number(this.searchForm.kaisu04)){
          exception = true;
        }
        if (this.searchForm.kaisu04 === '' || isFinite(this.searchForm.kaisu04) == false){
          this.searchForm.kingaku04 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku04 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 5){
        if (5 < Number(this.searchForm.kaisu05)){
          exception = true;
        }
        if (this.searchForm.kaisu05 === '' || isFinite(this.searchForm.kaisu05) == false){
          this.searchForm.kingaku05 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku05 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 6){
        if (5 < Number(this.searchForm.kaisu06)){
          exception = true;
        }
        if (this.searchForm.kaisu06 === '' || isFinite(this.searchForm.kaisu06) == false){
          this.searchForm.kingaku06 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku06 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 7){
        if (5 < Number(this.searchForm.kaisu07)){
          exception = true;
        }
        if (this.searchForm.kaisu07 === '' || isFinite(this.searchForm.kaisu07) == false){
          this.searchForm.kingaku07 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku07 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 8){
        if (5 < Number(this.searchForm.kaisu08)){
          exception = true;
        }
        if (this.searchForm.kaisu08 === '' || isFinite(this.searchForm.kaisu08) == false){
          this.searchForm.kingaku08 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku08 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 9){
        if (5 < Number(this.searchForm.kaisu09)){
          exception = true;
        }
        if (this.searchForm.kaisu09 === '' || isFinite(this.searchForm.kaisu09) == false){
          this.searchForm.kingaku09 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku09 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 10){
        if (5 < Number(this.searchForm.kaisu10)){
          exception = true;
        }
        if (this.searchForm.kaisu10 === '' || isFinite(this.searchForm.kaisu10) == false){
          this.searchForm.kingaku10 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku10 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 11){
        if (5 < Number(this.searchForm.kaisu11)){
          exception = true;
        }
        if (this.searchForm.kaisu11 === '' || isFinite(this.searchForm.kaisu11) == false){
          this.searchForm.kingaku11 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku11 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 12){
        if (5 < Number(this.searchForm.kaisu12)){
          exception = true;
        }
        if (this.searchForm.kaisu12 === '' || isFinite(this.searchForm.kaisu12) == false){
          this.searchForm.kingaku12 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku12 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 1){
        if (5 < Number(this.searchForm.kaisu01)){
          exception = true;
        }
        if (this.searchForm.kaisu01 === '' || isFinite(this.searchForm.kaisu01) == false){
          this.searchForm.kingaku01 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku01 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 2){
        if (5 < Number(this.searchForm.kaisu02)){
          exception = true;
        }
        if (this.searchForm.kaisu02 === '' || isFinite(this.searchForm.kaisu02) == false){
          this.searchForm.kingaku02 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku02 = this.searchForm.genkaKingaku_tanka;
        }
      } else if (month == 3){
        if (5 < Number(this.searchForm.kaisu03)){
          exception = true;
        }
        if (this.searchForm.kaisu03 === '' || isFinite(this.searchForm.kaisu03) == false){
          this.searchForm.kingaku03 = 0;
        } else if(this.searchForm.genkaKingakuType == 2){
          this.searchForm.kingaku03 = this.searchForm.genkaKingaku_tanka;
        }
      }

      if (exception == true){
        this.searchForm.kaisuSum = '';
          this.MsgInfo({
            title: "見積依頼(作業予定)",
            message: "回数は0～5の範囲で入力してください。"
          });
          this.$refs['作業回数' + month].focus();

          return false;
      }
      
      sumKaisu += isFinite(this.searchForm.kaisu04) ? Number(this.searchForm.kaisu04) : 0
      sumKaisu += isFinite(this.searchForm.kaisu05) ? Number(this.searchForm.kaisu05) : 0
      sumKaisu += isFinite(this.searchForm.kaisu06) ? Number(this.searchForm.kaisu06) : 0
      sumKaisu += isFinite(this.searchForm.kaisu07) ? Number(this.searchForm.kaisu07) : 0
      sumKaisu += isFinite(this.searchForm.kaisu08) ? Number(this.searchForm.kaisu08) : 0
      sumKaisu += isFinite(this.searchForm.kaisu09) ? Number(this.searchForm.kaisu09) : 0
      sumKaisu += isFinite(this.searchForm.kaisu10) ? Number(this.searchForm.kaisu10) : 0
      sumKaisu += isFinite(this.searchForm.kaisu11) ? Number(this.searchForm.kaisu11) : 0
      sumKaisu += isFinite(this.searchForm.kaisu12) ? Number(this.searchForm.kaisu12) : 0
      sumKaisu += isFinite(this.searchForm.kaisu01) ? Number(this.searchForm.kaisu01) : 0
      sumKaisu += isFinite(this.searchForm.kaisu02) ? Number(this.searchForm.kaisu02) : 0
      sumKaisu += isFinite(this.searchForm.kaisu03) ? Number(this.searchForm.kaisu03) : 0

      this.searchForm.kaisuSum = sumKaisu;
      this.searchForm.suryo = sumKaisu;

      this.isCloseConfirm = true

    },

    txtKaisuBlur(month){
      let exception = false;

      if (month == 4){
        if (this.searchForm.kaisu04 !== ''){
          if(!Number.isFinite(this.searchForm.kaisu04)){
            this.searchForm.kaisu04 = ''
            exception = true
          }
        }
      }

      if (exception == true){
        this.searchForm.kaisuSum = '';
          this.MsgInfo({
            title: "見積依頼(作業予定)",
            message: "回数に数値でない値が含まれていたため、クリアしました。"
          });
          this.$refs['作業回数' + month].focus();

          return false;
      }
    },

    txtGenkaKingakuTankaChanged(){
      this.checkChangedData()

      // 作業回数が空でない月に入力した単価を反映する
      if (this.searchForm.kaisu04 != '') {
        this.searchForm.kingaku04 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu05 != '') {
        this.searchForm.kingaku05 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu06 != '') {
        this.searchForm.kingaku06 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu07 != '') {
        this.searchForm.kingaku07 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu08 != '') {
        this.searchForm.kingaku08 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu09 != '') {
        this.searchForm.kingaku09 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu10 != '') {
        this.searchForm.kingaku10 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu11 != '') {
        this.searchForm.kingaku11 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu12 != '') {
        this.searchForm.kingaku12 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu01 != '') {
        this.searchForm.kingaku01 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu02 != '') {
        this.searchForm.kingaku02 = this.searchForm.genkaKingaku_tanka
      }
      if (this.searchForm.kaisu03 != '') {
        this.searchForm.kingaku03 = this.searchForm.genkaKingaku_tanka
      }
    },

    async GetMitsumoriData(){
      await this.search();
      await this.search2();
    },

    async search() {
      let _this = this

      _this.http
        .post("/api/Reafs_T/TeikiMitsumoriSyosaiNyuryoku/search", _this.queryParams, "検索しています....")
        .then((result) => {
          if (!result.status) {
            return _this.$message.error(result.message);
          }

          // 1行目
          this.searchForm.keiyakuNO = result.data[0].keiyakuNO
          this.searchForm.iraiNO = result.data[0].iraiNO
          this.searchForm.keiyakukanriNO = result.data[0].keiyakukanriNO
          // 2行目
          this.searchForm.teisyutsusaki = result.data[0].syuzenKBNsyo
          this.searchForm.iraisya = result.data[0].iraisya
          // 3行目
          this.searchForm.keiyakumeisyo = result.data[0].keiyakumeisyo
          this.searchForm.iraibi = result.data[0].iraibi
          this.searchForm.keiyakukikan = result.data[0].keiyakuYMS + "~" +result.data[0].keiyakuYME
          // 4行目
          this.searchForm.bukkenCD = result.data[0].bukkenCD
          this.searchForm.bukkenmei = result.data[0].bukkenmei
          this.searchForm.postCD = result.data[0].postCD
          this.searchForm.juusyo = result.data[0].juusyo
          // 5行目
          this.searchForm.meisaiNO = result.data[0].meisaiNO
          this.searchForm.sagyoumeisyo = result.data[0].sagyoumeisyo
          this.searchForm.suryo = result.data[0].suryo
          this.searchForm.tani = result.data[0].tani
          this.searchForm.tanka = result.data[0].tanka
          this.searchForm.genkakingaku_zeinuki = result.data[0].genkakingaku_zeinuki
          //6行目
          this.searchForm.kankaku = result.data[0].kankaku
          this.searchForm.kaisuSum = result.data[0].kaisuSum

          this.searchForm.genkaKingakuType = parseInt(result.data[0].genkaKingakuType)
          this.searchForm.genkaKingaku_tanka = result.data[0].genkaKingaku_tanka

          if (this.searchForm.genkaKingakuType === 2)
          {
            this.kingakuDisabled = true
            this.tankaDisabled = false
          }
          else
          {
            this.kingakuDisabled = false
            this.tankaDisabled = true
          }

          this.searchForm.kaishaCode = result.data[0].kaishaCode
          this.searchForm.keiyakuNOEdaban = result.data[0].keiyakuNOEdaban
          this.searchForm.meisaiNO = result.data[0].meisaiNO
          this.searchForm.rirekiNO = result.data[0].rirekiNO
          this.searchForm.aimitsumoriNO = result.data[0].aimitsumoriNO
          this.searchForm.genkazeiKBN = result.data[0].genkazeiKBN
          this.searchForm.genkazeiritsu = result.data[0].genkazeiritsu

          this.changeCheckData.kankaku = result.data[0].kankaku
          this.changeCheckData.genkaKingakuType = parseInt(result.data[0].genkaKingakuType)
          this.changeCheckData.genkaKingaku_tanka = result.data[0].genkaKingaku_tanka
        })
        
    },

    async search2() {
      let _this = this

      _this.http
        .post("/api/Reafs_T/TeikiMitsumoriSyosaiNyuryoku/search2", _this.queryParams,"検索しています....")
        .then((result) => {
          if (!result.status) {
            return _this.$message.error(result.message);
          }

          if (result.data.length === 0) {
              // 処理無し
          }else{
            // 作業回数
            this.searchForm.kaisu04 = result.data[3].kaisu
            this.searchForm.kaisu05 = result.data[4].kaisu
            this.searchForm.kaisu06 = result.data[5].kaisu
            this.searchForm.kaisu07 = result.data[6].kaisu
            this.searchForm.kaisu08 = result.data[7].kaisu
            this.searchForm.kaisu09 = result.data[8].kaisu
            this.searchForm.kaisu10 = result.data[9].kaisu
            this.searchForm.kaisu11 = result.data[10].kaisu
            this.searchForm.kaisu12 = result.data[11].kaisu
            this.searchForm.kaisu01 = result.data[0].kaisu
            this.searchForm.kaisu02 = result.data[1].kaisu
            this.searchForm.kaisu03 = result.data[2].kaisu

            this.changeCheckData.kaisu04 = result.data[3].kaisu
            this.changeCheckData.kaisu05 = result.data[4].kaisu
            this.changeCheckData.kaisu06 = result.data[5].kaisu
            this.changeCheckData.kaisu07 = result.data[6].kaisu
            this.changeCheckData.kaisu08 = result.data[7].kaisu
            this.changeCheckData.kaisu09 = result.data[8].kaisu
            this.changeCheckData.kaisu10 = result.data[9].kaisu
            this.changeCheckData.kaisu11 = result.data[10].kaisu
            this.changeCheckData.kaisu12 = result.data[11].kaisu
            this.changeCheckData.kaisu01 = result.data[0].kaisu
            this.changeCheckData.kaisu02 = result.data[1].kaisu
            this.changeCheckData.kaisu03 = result.data[2].kaisu

            // 作業金額
            this.searchForm.kingaku04 = result.data[3].kingaku
            this.searchForm.kingaku05 = result.data[4].kingaku
            this.searchForm.kingaku06 = result.data[5].kingaku
            this.searchForm.kingaku07 = result.data[6].kingaku
            this.searchForm.kingaku08 = result.data[7].kingaku
            this.searchForm.kingaku09 = result.data[8].kingaku
            this.searchForm.kingaku10 = result.data[9].kingaku
            this.searchForm.kingaku11 = result.data[10].kingaku
            this.searchForm.kingaku12 = result.data[11].kingaku
            this.searchForm.kingaku01 = result.data[0].kingaku
            this.searchForm.kingaku02 = result.data[1].kingaku
            this.searchForm.kingaku03 = result.data[2].kingaku

            this.changeCheckData.kingaku04 = result.data[3].kingaku
            this.changeCheckData.kingaku05 = result.data[4].kingaku
            this.changeCheckData.kingaku06 = result.data[5].kingaku
            this.changeCheckData.kingaku07 = result.data[6].kingaku
            this.changeCheckData.kingaku08 = result.data[7].kingaku
            this.changeCheckData.kingaku09 = result.data[8].kingaku
            this.changeCheckData.kingaku10 = result.data[9].kingaku
            this.changeCheckData.kingaku11 = result.data[10].kingaku
            this.changeCheckData.kingaku12 = result.data[11].kingaku
            this.changeCheckData.kingaku01 = result.data[0].kingaku
            this.changeCheckData.kingaku02 = result.data[1].kingaku
            this.changeCheckData.kingaku03 = result.data[2].kingaku
          }
        })
        
    },

    OnClick(){
      //入力チェック
      if (parseInt(this.searchForm.genkaKingakuType) == 2 && this.searchForm.genkaKingaku_tanka === '')
      {
        this.MsgErr({
          title: "見積依頼（作業予定）",
          message: "原価金額単価が未入力です。"
        });
        return;
      }

      if (
          (this.searchForm.kaisu04 !== '' && this.searchForm.kingaku04 === '') ||
          (this.searchForm.kaisu05 !== '' && this.searchForm.kingaku05 === '') ||
          (this.searchForm.kaisu06 !== '' && this.searchForm.kingaku06 === '') ||
          (this.searchForm.kaisu07 !== '' && this.searchForm.kingaku07 === '') ||
          (this.searchForm.kaisu08 !== '' && this.searchForm.kingaku08 === '') ||
          (this.searchForm.kaisu09 !== '' && this.searchForm.kingaku09 === '') ||
          (this.searchForm.kaisu10 !== '' && this.searchForm.kingaku10 === '') ||
          (this.searchForm.kaisu11 !== '' && this.searchForm.kingaku11 === '') ||
          (this.searchForm.kaisu12 !== '' && this.searchForm.kingaku12 === '') ||
          (this.searchForm.kaisu01 !== '' && this.searchForm.kingaku01 === '') ||
          (this.searchForm.kaisu02 !== '' && this.searchForm.kingaku02 === '') ||
          (this.searchForm.kaisu03 !== '' && this.searchForm.kingaku03 === '')
          )
      {
        this.MsgErr({
          title: "見積依頼（作業予定）",
          message: "作業回数を入力している場合は、金額を設定してください。"
        });
        return;
      }

      // 各月の合計が12桁を超えるか
      if (
          (
              Number(this.searchForm.kingaku04.replaceAll(',', '')) + (Number(this.searchForm.kingaku04.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku05.replaceAll(',', '')) + (Number(this.searchForm.kingaku05.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku06.replaceAll(',', '')) + (Number(this.searchForm.kingaku06.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku07.replaceAll(',', '')) + (Number(this.searchForm.kingaku07.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku08.replaceAll(',', '')) + (Number(this.searchForm.kingaku08.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku09.replaceAll(',', '')) + (Number(this.searchForm.kingaku09.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku10.replaceAll(',', '')) + (Number(this.searchForm.kingaku10.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku11.replaceAll(',', '')) + (Number(this.searchForm.kingaku11.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku12.replaceAll(',', '')) + (Number(this.searchForm.kingaku12.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku01.replaceAll(',', '')) + (Number(this.searchForm.kingaku01.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku02.replaceAll(',', '')) + (Number(this.searchForm.kingaku02.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
            + Number(this.searchForm.kingaku03.replaceAll(',', '')) + (Number(this.searchForm.kingaku03.replaceAll(',', '')) * this.queryParams.genkazeiritsu)
          )
          > 999999999999
        )
      {
        this.fnc_void_showErrorMsg("E040205");
        return;
      }

      let montherData = [];
      
      montherData.push({kaisu:this.searchForm.kaisu04, kingaku:this.searchForm.kingaku04.replaceAll(',', ''), month:'04'});
      montherData.push({kaisu:this.searchForm.kaisu05, kingaku:this.searchForm.kingaku05.replaceAll(',', ''), month:'05'});
      montherData.push({kaisu:this.searchForm.kaisu06, kingaku:this.searchForm.kingaku06.replaceAll(',', ''), month:'06'});
      montherData.push({kaisu:this.searchForm.kaisu07, kingaku:this.searchForm.kingaku07.replaceAll(',', ''), month:'07'});
      montherData.push({kaisu:this.searchForm.kaisu08, kingaku:this.searchForm.kingaku08.replaceAll(',', ''), month:'08'});
      montherData.push({kaisu:this.searchForm.kaisu09, kingaku:this.searchForm.kingaku09.replaceAll(',', ''), month:'09'});
      montherData.push({kaisu:this.searchForm.kaisu10, kingaku:this.searchForm.kingaku10.replaceAll(',', ''), month:'10'});
      montherData.push({kaisu:this.searchForm.kaisu11, kingaku:this.searchForm.kingaku11.replaceAll(',', ''), month:'11'});
      montherData.push({kaisu:this.searchForm.kaisu12, kingaku:this.searchForm.kingaku12.replaceAll(',', ''), month:'12'});
      montherData.push({kaisu:this.searchForm.kaisu01, kingaku:this.searchForm.kingaku01.replaceAll(',', ''), month:'01'});
      montherData.push({kaisu:this.searchForm.kaisu02, kingaku:this.searchForm.kingaku02.replaceAll(',', ''), month:'02'});
      montherData.push({kaisu:this.searchForm.kaisu03, kingaku:this.searchForm.kingaku03.replaceAll(',', ''), month:'03'});

      this.putData.TS00306_MontherData = montherData;
      this.putData.kankaku = this.searchForm.kankaku;
      this.putData.genkaKingakuType = parseInt(this.searchForm.genkaKingakuType);
      this.putData.tanka = this.searchForm.tanka;
      this.putData.genkazeiritsu = this.searchForm.genkazeiritsu;
      this.putData.genkazeiKBN = this.searchForm.genkazeiKBN;
      this.putData.genkaKingaku_tanka = String(this.searchForm.genkaKingaku_tanka).replaceAll(',', '');

      this.putData.kaishaCode = this.searchForm.kaishaCode;
      this.putData.keiyakuNO = this.searchForm.keiyakuNO;
      this.putData.rirekiNO = this.searchForm.rirekiNO;
      this.putData.keiyakuNOEdaban = this.searchForm.keiyakuNOEdaban;
      this.putData.meisaiNO = this.searchForm.meisaiNO;
      this.putData.aimitsumoriNO = this.searchForm.aimitsumoriNO;

      this.fnc_void_showTourokuMsg();

    },

    fnc_void_showTourokuMsg(){
      this.fnc_rtn_getMsg('K000004').then(response => {
          this.MsgConf(
            {
              title: "見積依頼(作業予定)",
              message: response.data
            },
            () => {
              // OKが選択された場合
              //登録結果取得
              this.fnc_void_Update();
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

    async fnc_void_Update(){
       //登録データ渡し
      let letval_touroku = new Object();
      let _this = this;
      letval_touroku = _this.putData;

      console.log(letval_touroku);

      await _this.http
        .post(
          "/api/Reafs_T/TeikiMitsumoriSyosaiNyuryoku/Update",
          letval_touroku,
          "登録しています...."
        )
        .then(function(response) {
          if (response.status = true) {
            _this.fnc_void_showInfoMsg("M000008");
            _this.cfmFlg = false;
            _this.GetMitsumoriData();
          }
        });
    },

    fnc_void_showInfoMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgInfo({
          title: "見積依頼(作業予定)",
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
    // エラーメッセージ出力
    ///////////////////////////////////////////////
    fnc_void_showErrorMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgErr({
          title: "見積依頼（作業予定）",
          message: response.data
        });
      });
    },

    replaceAll (val) {
      return ((val || '') + '').replaceAll(',', '')
    }
  }
};
</script>
<style lang="less" scoped>
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
    
  }
  .row-bg {
    padding: 1x 0;
    background-color:lightgray;
    height: 30px;
  }
  
.Main-contianer{
  padding: 10px;
  width:1556px;
}
.date-picker{
  width: 250px;
}

.el-input-len-5 {
    width:60px;
 }
 .el-input-serach-len-5 {
    width:130px;
 }
 .el-input-len-10 {
   width:120px;
 }
 .el-input-date-len-10 {
   width:140px;
 }
 .el-input-len-20 {
   width:100px;
 }
 .el-input-len-60 {
   width:300px;
 }
 .el-input-len-200 {
   width:300px;
 }
 .multi-label{
   height:300px;
 }

 .modal_button {
    float: right;
  }

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

 .br{
  display:block;
  content:"";
  height:35px;
 }

 .footer{
   position:absolute;
   bottom:0;
 }

 .btn_updown{
   width:30px;
   padding:0px;
   box-sizing:border-box;
   border:1px;
   cursor:pointer;
 }

 .kaisu{
   display:inline-block;
   font-size:16px;
   width:50px;
   margin-right: 5px;
 }
 .kingaku{
   display:inline-block;
   floot:left;
   font-size:16px;
   width:165px;
 }

.title-bg{
  padding: 1x 0;
  background:#9BC2E6;
  height:25px;
}
.heder-width-2{
  display:inline-block;
  floot:left;
  font-size:16px;
  text-align:center;
  width:40px
}
.heder-width-4{
  display:inline-block;
  floot:left;
  font-size:16px;
  text-align:center;
  width:50px
}

.heder-width-6{
  display:inline-block;
  floot:left;
  font-size:16px;
  text-align: center;
  width:60px
}
.heder-width-8{
  display:inline-block;
  floot:left;
  font-size:16px;
  text-align: center;
  width:80px
}
.heder-width-12{
  display:inline-block;
  floot:left;
  font-size:16px;
  text-align: center;
  width:120px
}
.heder-width-16{
  display:inline-block;
  floot:left;
  font-size:16px;
  text-align: center;
  width:165px
}
.heder-width-30{
  display:inline-block;
  floot:left;
  font-size:16px;
  padding-left: 15px;
  width:300px
}

.m-bottom-less {
 margin-bottom:0px;
}
.space{
  margin-right:10px;
}

.title{
  font-size:0;
}
.detail{
  font-size:0;
  margin-bottom:20px;
}

.box-sagyokingaku{
  border:1px solid;
  width:2000px;
  height:100px;
}

.box-touroku{
  width:1470px;
  height:100px;
}

.box-nengetsu{
  border:1px solid;
  width:165px;
  height:30px;
}

.align-right{
  text-align:right;
}

label-color{background-color:red}

</style>


