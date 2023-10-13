<template>
  <div class="Serch-contianer">
    <el-row :gutter="10">
      <!-- 検索部 -->
      <!-- １行目 -->
      <el-col :span="8">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="契約№">
            <el-col
            Class="row-bg"></el-col>
          </el-form-item>

          <el-form-item label="提出先">
            <el-col Class="row-bg"></el-col>
          </el-form-item>

          <el-form-item label="契約名称">
            <el-input
              v-model="searchForm.keiyakumeisyo"
              maxlength="200"
              class="el-input-len-200 required"
            ></el-input>
          </el-form-item>

          <el-form-item label="表示区分">
            <el-select
              v-model="searchForm.region"
            >
              <el-option
                label="Reafs-W"
                value="1"
              ></el-option>
              <el-option
                label="Reafs-T"
                value="2"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="物件">
            <el-input
              v-model="searchForm.bukkennCD"
              maxlength="20"
              class="el-input-len-20 required"
            ></el-input>
          </el-form-item>

          <el-form-item label="見積有効期間" prop="nyuryokuerea6">
            <el-date-picker
              type="date"
              placeholder=""
              v-model="searchForm.nyuryokuerea6"
            ></el-date-picker>
          </el-form-item>

          <el-form-item label="見積金額">
            <el-input
              v-model="searchForm.mittsumorikingaku"
              maxlength="20"
              class="el-input-len-20 required"
            ></el-input>
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
              maxlength="20"
              :width="'140px'"
            >
            <template v-slot:append>
              <el-button>◀</el-button>
              <el-button>▶</el-button>
              <el-label><span>最新</span>／履歴</el-label>
              <el-button>検索</el-button>
            </template>
            </reafsinputtext>
          </el-form-item>

          <el-form-item label="依頼者">
            <el-input
              v-model="searchForm.keiyakumeisyo"
              maxlength="200"
              class="el-input required"
            ></el-input>
          </el-form-item>

          <br class="br">

          <el-form-item>
            
            <el-button>検索</el-button>
          </el-form-item>
          

          <el-form-item label="物件住所">
              <el-input
                v-model="searchForm.bukkenyubin"
                maxlength="20"
                class="el-input-len-20 required"
              ></el-input>
              <el-input
                v-model="searchForm.bukkennjuusyo"
                maxlength="200"
                class="el-input-len-200 required"
              ></el-input>            
          </el-form-item>

          <el-form-item label="見積担当者" prop="mitsumoritantousya">
            <reafsinputtext 
              maxlength="20"
              v-model="searchForm.mitsumoritantousya"
              :width="'300px'"
              :required="true"
            ></reafsinputtext>
          </el-form-item>

          <el-form-item label="担当者連絡先" prop="tantousyarenrakusaki">
            <reafsinputtext 
              maxlength="20"
              v-model="searchForm.tantousyarenrakusaki"
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
          <el-form-item label="契約管理番号">
            <el-input
              v-model="searchForm.keiyakukanriNo"
              maxlength="200"
              class="el-input-len-200 required"
            ></el-input>
          </el-form-item>
        </el-form>

        <el-button>依頼内容表示</el-button>

        <br class="br">

        <el-label>見積依頼日：9999/99/99</el-label>
        <el-label>　　　　　　　　　状態：NNNNNNNNNNN</el-label>
        <br>
        <el-label>契約期間：2020/05/01～9999/99/99</el-label>
        
        <el-form-item label="SGリアルティからの連絡事項">
          <el-input
            type="textarea"
            v-model="searchForm.desc"
            maxlength="300"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <template>
        <reafstable 
              ref="table"
              :single="single"
              :rowKey="rowKey"
              :tableData="tableData.slice((pagination.page - 1) * pagination.size, pagination.page * pagination.size)"
              :columns="columns"
              :pagination="pagination"
              :pagination-hide="false"
              :height= 200
              @backData="pageBack"
              ></reafstable>
      </template>
    </el-row>
    
    <!-- ２行目 -->
    <!-- <el-row :gutter="20">
      <el-col :span="10">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="提出先">
            <el-col
            label-width="100px"
            Class="row-bg"></el-col>
          </el-form-item>
          

        </el-form>
      </el-col>
      
      <el-col :span="10">
        <el-form 
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="150px"
        >
          <el-form-item label="依頼者">
            <el-col
            label-width="10px"
            Class="row-bg"></el-col>
          </el-form-item>
        </el-form>

        <button>依頼内容表示</button> 
      </el-col>
      
    </el-row>-->
    <reafsmodal :dialogVisible.sync="showDialog" @backData="reafsmodalBack"></reafsmodal>

  </div>
</template>
<script>

import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafstable from '@/components/table/Table.vue'
import reafsmodal from '@/components/modal/Modal.vue'
import eigyoten from '@/components/basic/EigyoTen.vue'

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
        eigyoten:'',
      },
      rules: {
        eigyoten: [
        //   { required: true, message: '入力してください', trigger: 'blur' },
        ],
      },
      single: false, //SingleSelet
        rowKey: undefined, //Treetable PK
        pagination: { total: 22, size: 10, sortName: "", page:1 },
        
        tableData: [{
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, {
            torihikisakicd: '0010-001',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, {
            torihikisakicd: '0010-002',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, {
            torihikisakicd: '0010-003',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト1',
            address: '2110041 神奈川県川崎市中原区0-00-00'
        }, 
        ],
        columns: [{field:'torihikisakicd',title:'取引先コード',type:'string',link:true,width:90,readonly:true,align:'center',sort:true},
            {field:'torihikisakinm',title:'取引先名',type:'string',width:90,align:'left',sort:true},
            {field:'address',title:'住所',type:'string',width:140,align:'left'},
            ],

      showDialog: false,
    };
  },
  mounted() {
  },
  methods: {
    onSubmit(){
      console.log(1);
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
    background-color: #f9fafc;
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
   width:120px;
 }
 .el-input-date-len-10 {
   width:140px;
 }
 .el-input-len-20 {
   width:100px;
 }
 .el-input-len-200 {
   width:300px;
 }

 .br{
  display:block;
  content:"";
  height:35px;
 }
span{color:red}

</style>


