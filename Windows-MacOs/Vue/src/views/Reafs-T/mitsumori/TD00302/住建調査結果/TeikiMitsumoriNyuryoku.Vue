<template>
  <div class="Serch-contianer">
    <el-row :gutter="10">
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
            <!-- <reafsinputtext 
              v-model="searchForm.keiyakuNo"
              maxlength="11"
              :width="'150px'"
            >
            </reafsinputtext> -->
              
            <el-col Class="row-bg">
              <label v-bind:href="searchForm.keiyakuNo">{{$route.params.keiyakuNO}}</label>
            </el-col>
              
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
              <el-button class="btn_updown">◀</el-button>
              <el-button class="btn_updown">▶</el-button>
              <el-label><span>最新</span>／履歴</el-label>
              <el-button v-on:click="OnClick">検索</el-button>
            </template>
            </reafsinputtext>
          </el-form-item>

        </el-form>
      </el-col>

      <el-col :span="4">
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
      <!-- 検索部 -->
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

      <el-col :span="13">
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

      <el-col :span="6">
        <!--<el-button v-on:click="irai">依頼内容表示</el-button>-->
        <router-link :to="{name:'TeikiMitsumoriNyuryoku_irai'
                            ,params:{keiyakuNO:this.searchForm.keiyakuNo
                                    ,iraiNO:this.searchForm.iraiNO
                                    ,keiyakukanriNO:this.searchForm.keiyakukanriNO
                                    ,teisyutsusaki:this.searchForm.teisyutsusaki
                                    ,iraisya:this.searchForm.iraisya
                                    ,keiyakumeisyo:this.searchForm.keiyakumeisyo
                                    ,syuuzenKBNdai:this.syuuzenKBNdai
                                    ,irainaiyojoukyo:this.irainaiyojoukyo
                                    ,iraisyaCD:this.iraisyaCD
                                    ,iraisya:this.searchForm.iraisya
                                    ,iraisyaTEL:this.iraisyaTEL
                                    ,iraisyadairiCD:this.iraisyadairiCD
                                    ,iraisyadairi:this.iraisyadairi
                                    ,iraisyadairiTEL:this.iraisyadairiTEL
                                    ,uketsukesyaCD:this.uketsukesyaCD
                                    ,uketsukesya:this.uketsukesya
                                    ,uketsukesyaTEL:this.uketsukesyaTEL
                                    }}"><el-button>依頼内容表示</el-button></router-link>
                                    <el-button @click="toIrai()">依頼内容表示サンプル</el-button>
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
        >
          <el-form-item label="契約名称" prop="keiyakumeisyo">
            <reafsinputtext 
              maxlength="200"
              v-model="searchForm.keiyakumeisyo"
              :width="'1080px'"
              :required="false"
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
            placeholder="---"
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
        <el-label>見積依頼日：9999/99/99</el-label>
        <label v-bind:href="searchForm.mitsumoriiraibi">{{searchForm.mitsumoriiraibi}}</label>
        <br>
        <el-label>契約期間：9999/99/99～9999/99/99</el-label>
      </el-col>
      <el-col :span="6">
        <el-label>状態：NNNNNNNNNNNNNNNN</el-label>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- 検索部 -->
      <!-- ５行目 -->
      <el-col :span="4">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="物件">
            <el-col Class="row-bg el-input-date-len-10">
              <label v-bind:href="searchForm.bukkenCD">{{searchForm.bukkenCD}}</label>
            </el-col>
          </el-form-item>  
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-col Class="row-bg el-input-date-len-20">
            <label v-bind:href="searchForm.bukkenmei">{{searchForm.bukkenmei}}</label>
          </el-col> 
        </el-form>
      </el-col>

      <el-col :span="4">
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
      <el-col :span="12">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-col Class="row-bg el-input-date-len-200">
            <label v-bind:href="searchForm.juusyo">{{searchForm.juusyo}}</label>
          </el-col> 
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
        <el-form-item label="見積有効期間" prop="mitsumoriyuukoukikan">
          <el-date-picker
            type="date"
            format="yyyy/MM/dd"
            placeholder=""
            v-model="searchForm.mitsumoriyuukoukikan"
            :rezuired="true"
          ></el-date-picker>
        </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="8">
        <el-form 
          :model="sampleForm"
          :rules="rules"
          ref="sampleForm"
          label-width="150px"
          class="el-input-len-20">
        
          <el-form-item label="見積担当者" prop="mitsumoritantousya">
            <reafsinputtext 
              maxlength="20"
              v-model="searchForm.mitsumoritantousya"
              :width="'300px'"
              :required="true"
            ></reafsinputtext>
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
        
        <el-form-item label="">
          <el-label>SGリアルティからの連絡事項</el-label>
          <el-input
            type="textarea"
            v-model="searchForm.SGRrenrakujiko"
            maxlength="200"
            show-word-limit
            height="500"
          ></el-input>
        </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- 検索部 -->
      <!-- ６行目 -->
      <el-col :span="8">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="見積金額" class="row-bg">
            <el-label >\999,999,999,999</el-label>
            <el-label>（税込）ー</el-label>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="8">
        <el-form 
          :model="sampleForm"
          :rules="rules"
          ref="sampleForm"
          label-width="150px"
          class="sample-form-class">
        
          <el-form-item label="担当者連絡先" prop="tantousyaTEL">
            <reafsinputtext 
              maxlength="14"
              v-model="searchForm.tantousyaTEL"
              :width="'150px'"
              :required="true"
            ></reafsinputtext>
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
        <el-label><span>見積（新規／相見積／修正依頼／契約変更）</span></el-label>
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-form 
          :model="sampleForm"
          :rules="rules"
          ref="sampleForm"
          label-width="150px"
          class="sample-form-class">
        
          <el-form-item label="過去見積参照" prop="tantousyaTEL">
            
            <reafsinputtext 
              maxlength="11"
              v-model="searchForm.kakomitsumorisansyo"
              :width="'150px'"
              :required="false"
            >
          </reafsinputtext>
          </el-form-item>
          
        
        </el-form>
      </el-col>
      <el-col :span="2">
        <el-form 
          :model="sampleForm"
          :rules="rules"
          ref="sampleForm"
          label-width="150px"
          class="sample-form-class">
        
          <el-button>参照検索</el-button>    
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- 検索結果 -->
      <el-col :span="24">
        <div class="grid-content bg-purple-light overflow-auto">
          <el-table
            v-loading="loading"
            :data="tableData"
            style="width: 100; height: 100%"
          >
            <el-table-column
              prop="NO"
              label="№"
              width="90"
            ></el-table-column>
            <el-table-column @click="onClickUP"
              prop="UP"
              label="順番"
              width="40"
            ></el-table-column>
            <el-table-column
              prop="DOWN"
              label=""
              width="40"
            ></el-table-column>
            <el-table-column
              prop="Clear"
              label="クリア"
              width="60"
            ></el-table-column>
            <el-table-column
              prop="Copy"
              label="コピー"
              width="60"
            ></el-table-column>
            <el-table-column
              prop="keiyakuNO"
              label="契約№・物件"
              width="140"  
            ></el-table-column>
            <el-table-column
              prop="bukken"
              label=""
              width="140"
            ></el-table-column>
            <el-table-column
              prop="daiKBN"
              label="作業ｶﾃｺﾞﾘ大区分"
              with="280">
            </el-table-column>
            <el-table-column
              prop="tyuKBN"
              label="作業ｶﾃｺﾞﾘ中区分"
              with="280">
            </el-table-column>
            <el-table-column
              prop="syoKBN"
              label="作業ｶﾃｺﾞﾘ小区分"
              with="280">
            </el-table-column>
            <el-table-column
              prop="sagyomeisyo"
              label="作業名称"
              with="140">
            </el-table-column>
            <el-table-column
              prop="sagyo_HELP"
              label="作業"
              with="60">
            </el-table-column>
            <el-table-column
              prop="suryo"
              label="数量"
              with="160">
            </el-table-column>
            <el-table-column
              prop="tani"
              label="単位"
              with="100">
            </el-table-column>
            <el-table-column
              prop="tanka"
              label="単価"
              with="140">
            </el-table-column>
            <el-table-column
              prop="kingaku_zeinuki"
              label="金額（税抜）"
              with="140">
            </el-table-column>
            <el-table-column
              prop="zeiKBN"
              label="税区分"
              with="80">
            </el-table-column>
            <el-table-column
              prop="zeiritsu"
              label="税率"
              with="80">
            </el-table-column>
            <el-table-column
              prop="percent"
              label=""
              with="40">
            </el-table-column>
            <el-table-column
              prop="sagyo_yotei"
              label="作業予定"
              with="80">
            </el-table-column>
          </el-table>
        </div>
      </el-col>
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
            class="overflow-y-auto textarea-h200"
          ></el-input>
      </el-col>

      <el-col :span="12">
        <!-- 検索結果 -->
      <template>
        <td rowspan="2">
          <reafstable 
          ref="table"
          :single="single"
          :rowKey="rowKey"
          :tableData="tableData2"
          :columns="columnszeibetsuSUM"
          :height= "200"
          @backData="pageBack"
          ></reafstable>
        </td>
      </template>
      </el-col>

      
    </el-row>

    <el-row :gutter="10">
      <!-- 下段 -->
      <el-col :span="8">
        <el-label>提案備考・コメント</el-label>
        <el-input
            type="textarea"
            v-model="searchForm.teianbikou_koment"
            maxlength="200"
            show-word-limit
          ></el-input>
       
      </el-col>
    </el-row>

    <el-row :gutter="5" class="footer">
      <!-- 最下段 -->
      <el-col :span="17">
        <el-button @click="OnClick(1)">物件資料参照</el-button>
        <el-button @click="OnClick(2)">見積データ取込</el-button>    
        <el-button @click="OnClick(3)">写真添付</el-button>    
        <el-button @click="OnClick(4)">図面・資料添付</el-button>   
       
      </el-col>
      <el-col :span="7">
        <el-button @click="OnClick(0)">一時保存</el-button>
        <el-button @click="OnClick(5)">提出</el-button>    
        <el-button @click="OnClick(1)">戻る</el-button>    
      </el-col>
    </el-row>
    <reafsmodal :dialogVisible.sync="showDialog" @backData="reafsmodalBack"></reafsmodal>

  </div>
</template>
<script>

import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafstable from '@/components/table/Table.vue'
import reafsmodal from '@/components/modal/Modal.vue'
import eigyoten from '@/components/basic/Eigyoten.vue'

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafstable,
    reafsmodal,
    eigyoten,
  },
  
  data() {
    return {
      searchForm:{
        keiyakuNo:'00001000015',
        hansu:'11',
        iraiNo:'',
        iraisya:'',
        keiyakumeisyo:'',
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


      tableData: [],
      rules: {
        eigyoten: [
        //   { required: true, message: '入力してください', trigger: 'blur' },
        ],
      },
      single: false, //SingleSelet
        rowKey: undefined, //Treetable PK
        pagination: { total: 22, size: 10, sortName: "", page:1 },
        
        
        tableData2: [{
            zeiritsu:"10",
            zeinukiSUM:"-999,999,999,999",
            zeigaku:"-999,999,999,999",
            zeikomiSUM:"-999,999,999,999"
        }
        ],
        columnskensakukekka: [
          {field:'NO',title:'№',type:'integer',width:90,readonly:true,align:'center'},
          {field:'UP',title:'▲',type:'button',button:true,width:40,align:'center'},
          {field:'DOWN',title:'▼',type:'string',width:40,align:'center'},
          {field:'Clear',title:'クリア',type:'button',width:60,align:'center'},
          {field:'Copy',title:'コピー',type:'string',width:60,align:'center'},
          {field:'keiyakuNo',title:'契約№・物件',type:'string',width:140,align:'left'},
          {field:'daiKBN',title:'作業ｶﾃｺﾞﾘ大区分',type:'string',width:140,align:'left'},
          {field:'tyuKBN',title:'作業ｶﾃｺﾞﾘ中区分',type:'string',width:140,align:'left'},
          {field:'syoKBN',title:'作業ｶﾃｺﾞﾘ小区分',type:'string',width:140,align:'left'},
          {field:'sagyo_name',title:'作業名称',type:'input',width:140,align:'left'},
          {field:'sagyo_HELP',title:'作業',type:'string',width:60,align:'center'},
          {field:'suryo',title:'数量',type:'bumber',width:160,align:'right'},
          {field:'tani',title:'単位',type:'string',width:100,align:'riget'},
          {field:'tanka',title:'単価',type:'string',width:140,align:'right'},
          {field:'kingaku_zeinuki',title:'金額（税抜）',type:'string',width:140,align:'right'},
          {field:'zeiKBN',title:'税区分',type:'string',width:80,align:'left'},
          {field:'zeiritsu',title:'税率',type:'string',width:60,align:'right'},
          {field:'sagyo_yotei',title:'作業予定',type:'string',width:80,align:'center'},
        ],
        columnszeibetsuSUM: [
          {field:'zeiritsu',title:'税率',type:'integer',width:40,readonly:true,align:'center'},
          {field:'zeinukiSUM',title:'合計金額(税抜)',type:'decimal',width:180,align:'right'},
          {field:'zeigaku',title:'税額',type:'decimal',width:180,align:'right'},
          {field:'zeikomiSUM',title:'合計金額(税込み)',type:'decimal',width:180,align:'right'},
        ],

      showDialog: false,
    };
  },
  mounted() {
    this.searchForm.keiyakuNo = '00001000015';
  },
  methods: {
    onSubmit(){
      console.log(1);
    },
    search() {
      if (this.searchForm.hansu == "" || this.searchForm.hansu.trim() == "")
        return this.$message.error("版数を入力してください。");

      let _this = this
      var data = []

      this.http
        .post("/api/TD00302/search", this.searchForm)
        .then((result) => {
          if (!result.status) {
            return this.$message.error(result.message);
          }
          
          _this.searchForm.keiyakumeisyo = result.data[0].keiyakumeisyo
          _this.searchForm.iraiNO = result.data[0].iraiNO
          _this.searchForm.keiyakukanriNO = result.data[0].keiyakukanriNO
          _this.searchForm.teisyutsusaki = result.data[0].syuzenKBNsyo
          _this.searchForm.iraisya = result.data[0].iraisya
          _this.searchForm.bukkenCD = result.data[0].bukkenCD
          _this.searchForm.bukkenmei = result.data[0].bukkenmei
          _this.searchForm.postCD = result.data[0].postCD
          _this.searchForm.juusyo = result.data[0].juusyo
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

          // result.data.shozokuBushoName = this.searchForm.shozokuBushoName;
          // this.$store.commit("setUserInfo", result.data);
          // this.$router.push({ path: "/" });
        })

        
    },
    search2() {
      let _this = this

      var data = []

      this.http
        .post("/api/TD00302/search2", this.searchForm)
        .then((result2) => {
          if (!result2.status) {
            return this.$message.error(result.message);
          }
          
          for (let i = 0; i < result2.data.length; i++) {
                var obj = {}
                obj.NO = i + 1
                obj.keiyakuNO = result2.data[i].keiyakuNO
                obj.bukken = result2.data[i].bukkennmei
                obj.UP = "▲"
                obj.DOWN = "▼"
                obj.Clear = "ｸﾘｱ"
                obj.Copy = "©"
                obj.daiKBN = result2.data[i].daiKBN
                obj.tyuKBN = result2.data[i].tyuKBN
                obj.syoKBN = result2.data[i].syoKBN
                obj.sagyomeisyo = result2.data[i].sagyomeisyo
                obj.sagyo_HELP = "入力"
                obj.suryo = result2.data[i].suryo
                obj.tani = result2.data[i].tani
                obj.tanka = result2.data[i].tanka
                obj.kingaku_zeinuki = result2.data[i].genkakingaku_zeinuki
                obj.zeiKBN = result2.data[i].zeiKBN
                obj.zeiritsu = result2.data[i].genkazeiritsu
                obj.percent = "%"
                if (result2.data[i].syosaiFLG == 1)
                  {obj.sagyo_yotei = "入力済"}
                  else{obj.sagyo_yotei = "未入力"}
                
                data[i] = obj
            }

          
          _this.tableData = data
        })
    },

    GetMitsumoriData(){
      this.search()
      this.search2()
    },

    
    irai(){
      this.$router.push('TeikiMitsumoriNyuryoku_irai')
    },

    OnClick(KBN){
      if (KBN==0){
        //一時保存
        this.http
          .post("/api/TD00302/UPD", this.searchForm)
          .then((UPDresult) => {
            if (!UPDresult.status) {
              return this.$message.error("rollback");
            }
          })
      }else if(KBN==1){
        //物件資料参照
        return this.$message.error("修繕実装待ち");
      }else if(KBN==2){
        //見積データ取込
        return this.$message.error("修繕実装待ち");
      }else if(KBN==3){
        //写真添付
        return this.$message.error("修繕実装待ち");
      }else if(KBN==4){
        //図面・飼料添付
        return this.$message.error("修繕実装待ち");
      }else if(KBN==5){
        //提出
        return this.$message.error("修繕実装待ち");
      }else{
        this.searchForm.keiyakumeisyo = 'test'
        this.searchForm.teisyutsusaki = 'テスト'
        this.searchForm.bukkenn = 'てすと'
        this.searchForm.iraiNo = 'テスト'
      }
      
    },

    //サブ画面から戻すデータ
    reafsmodalBack(obj){
    //   this.sampleForm.nyuryokuerea5 = obj
    },
    // 依頼入力に遷移 add nishimura
    toIrai(){
      this.$router.push({
          name: 'TeikiMitsumoriNyuryokuirai', // ←src\router\index.jsにあわせてください
          query: {
            param1: this.searchForm.keiyakumeisyo,
            param2: 'パラメータ２'
          }
        })
    },
  }
};
</script>
<style lang="less" scoped>
 /* .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  } */
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
  
.Serch-contianer{
  padding: 10px;
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
   width:140px;
 }
 .el-input-date-len-10 {
   width:100px;
 }
 .el-input-date-len-20 {
   width:200px;
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
 .el-input-date-len-200{
   width:750px;
 }
.textarea-h200{
  height:200px;
  max-height:200px;
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
span{color:red}

</style>


