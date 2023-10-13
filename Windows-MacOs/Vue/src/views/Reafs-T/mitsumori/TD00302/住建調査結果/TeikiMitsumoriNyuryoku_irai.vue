<template>
  <div class="Main-contianer">
    <el-row :gutter="10">
      <!-- １行目 -->
      <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="契約№">
            <!--<reafsinputtext 
              v-model="searchForm.keiyakuNo"
              maxlength="11"
              :width="'150px'"
            >
            </reafsinputtext>-->
            <el-col
            Class="row-bg"><label v-model="Main.keiyakuNo">{{$route.params.keiyakuNO}}</label></el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>

     <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
        <el-form-item label="依頼№" prop="iraiNo">
          <el-col Class="row-bg">
            <label>{{$route.params.iraiNO}}</label>
            
          </el-col>
        </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="契約管理番号">
            <el-col Class="row-bg">
              <label>{{$route.params.keiyakukanriNO}}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
      
    <el-row :gutter="10">
      <!-- ２行目 -->
      <el-col :span="3">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="提出先">
            <!--<reafsinputtext 
              v-model="Main.keiyakuNo"
              maxlength="11"
              :width="'150px'"
            >
            </reafsinputtext>-->
            <el-col Class="row-bg">
              <label>{{$route.params.teisyutsusaki}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>

      <el-col :span="10">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
        <el-form-item label="依頼者">
          <el-col Class="row-bg">
            <label>{{$route.params.iraisya}}</label>
          </el-col>
        </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col>
            <el-label>見積依頼日：</el-label>
            <el-label>2022/04/05</el-label>
          </el-col>
          
        </el-form>
      </el-col>

      <el-col :span="5">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="200px"
        >
          <el-col>
            <el-label>契約期間：</el-label>
            <el-label>2022/04/05～9999/99/99</el-label>
          </el-col>
          
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ３行目 -->
      <el-col :span="10">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="契約名称">
            <!--<reafsinputtext 
              v-model="Main.keiyakuNo"
              maxlength="11"
              :width="'150px'"
            >
            </reafsinputtext>-->
            <el-col Class="row-bg">
              <label>{{$route.params.keiyakumeisyo}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>

    </el-row>

    <el-row :gutter="10">
      <!-- ４行目 -->
      <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="修繕区分">
            <!--<reafsinputtext 
              v-model="Main.keiyakuNo"
              maxlength="11"
              :width="'150px'"
            >
            </reafsinputtext>-->
            <el-col Class="row-bg">
              <label>{{$route.params.syuuzenKBNdai}}</label>
            </el-col>
            
          </el-form-item>
          
        </el-form>
      </el-col>

    </el-row>

    <el-row :gutter="10">
      <!-- ５行目 -->
      <el-col :span="18">
        <el-form 
          :model="Main"
          :rules="rules"
          
          ref="Main"
          label-width="100px"
          class="multi-label"
        >
          <el-form-item label="依頼内容／不具合状況等" class="multi">
            <!--<reafsinputtext 
              v-model="Main.keiyakuNo"
              maxlength="11"
              :width="'150px'"
              :height="'300px'"
            >
            </reafsinputtext>-->
            <el-col Class="row-bg"  style="height:70px" >
              <label>{{$route.params.irainaiyojoukyo}}</label>
            </el-col>
          </el-form-item>
          
        </el-form>
      </el-col>

    </el-row>

    <el-row :gutter="10">
      <!-- ７行目 -->
      <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="依頼者" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{$route.params.iraisyaCD}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>

      <el-col :span="12">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col Class="row-bg">
            <label>{{$route.params.iraisya}}</label>
          </el-col>  
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="連絡先" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{$route.params.iraisyaTEL}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>

    </el-row>

    <el-row :gutter="10">
      <!-- ８行目 -->
      <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="依頼代理者" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{$route.params.iraisyadairiCD}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>

      <el-col :span="12">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col Class="row-bg">
            <label>{{$route.params.iraisyadairi}}</label>
          </el-col>  
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="連絡先" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{$route.params.iraisyadairiTEL}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ９行目 -->
      <el-col :span="4">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="ＳＧＲ担当者" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{$route.params.uketsukesyaCD}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>

      <el-col :span="12">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col Class="row-bg">
            <label>{{$route.params.uketsukesya}}</label>
          </el-col>  
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-form 
          :model="Main"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="連絡先" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{$route.params.uketsukesyaTEL}}</label>
            </el-col>  
          </el-form-item>
          
        </el-form>
      </el-col>
    </el-row>
    <div><p>{{"param1:"+ this.$route.query.param1 + "; param2:"+ this.$route.query.param2}}</p></div>
    <reafsmodal :dialogVisible.sync="showDialog" @backData="reafsmodalBack"></reafsmodal>

  </div>
</template>
<script>

import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafsmodal from '@/components/modal/Modal.vue'

export default {
  beforeRouteEnter(to, form, next){
    next(vm =>{
      vm.initialize();
      next();
    });
  },

  components: {
    reafsinputtext,
    reafsinputnumber,
    reafsmodal,
  },
  
  data() {
    return {
      Main:{
        keiyakuNo: ''
      },
      tableData: [],
      rules: {
        
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
    window.onload = ()=>{
      initialize();
    }
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
          
          this.$message.error(result.data[0].契約名称);
          _this.searchform.keiyakumeisyo = result.data[0].契約名称
          
          for (let i = 0; i < result.data.length; i++) {
                
            }

          
          // result.data.shozokuBushoName = this.searchForm.shozokuBushoName;
          // this.$store.commit("setUserInfo", result.data);
          // this.$router.push({ path: "/" });
        })

        
    },

    GetMitsumoriData(){
      this.search()
    },

    initialize(){
    },
    Onclick(){
      this.$message.error('123');
      this.Main.iraiNo = '234'
    },
    //サブ画面から戻すデータ
    reafsmodalBack(obj){
    //   this.sampleForm.nyuryokuerea5 = obj
    }
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
  
.Main-contianer{
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
//  .multi-label{
//    height:100px;
//  }

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

.m-bottom-less {
 margin-bottom:0px;
}
</style>

<style scoped>
.el-form .multi >>> .el-form-item__label {
  height: 70px;
}
</style>