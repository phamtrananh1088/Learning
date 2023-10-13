<template>
  <div class="home-contianer">
    <el-row>
      <!-- 検索部 -->
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="searchForm"
          label-width="120px"
          class="search-form-class"
        >
          <!-- layout  xs:<768px sm:>=768px md:>=992px lg:>=1200px xl:>=1920px-->
          <el-col :span="9" :xs="24" >
            <!-- disabled 使用可否 true/false-->
            <!-- required 必須 true/false -->
            <!-- appendlabelText 番号から戻す名称文字列 -->
            <!-- append 名称文字列を使用時 'append'固定 -->
            <!-- backData  番号からデータ戻すmethod-->
            <el-form-item label="営業店" prop="eigyoten">
              <eigyoten 
                v-model="searchForm.eigyotenCd"
                :disabled="false"
                :appendlabelText="eigyotenNm"
                :append="'append'"
                @backData="reafsmodalBack"
              >
              </eigyoten>
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24">
            <el-form-item label="工事依頼№" prop="koujiiraiNo">
              <reafsinputtext 
                maxlength="7"
                v-model.number="searchForm.koujiiraiNo"
                :width="'90px'"
                :required="true"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="9" :xs="24">
            <el-form-item label-width="0px">
              <el-button type="primary" size="mini" @click="onSearchSubmit">検索</el-button>
            </el-form-item> 
          </el-col>
          <el-col :span="9" :xs="24">
            <el-form-item label="物件名" prop="bukken">
              <eigyoten 
                v-model="searchForm.bukken"
                :disabled="false"
                :appendlabelText="labelText"
                :append="'append'"
                @backData="reafsmodalBack"
              >
              </eigyoten>
            </el-form-item>
          </el-col>
          <el-col :span="6" :xs="24">
            <el-form-item label="物件区分" prop="bukkenKbn">
              <reafsinputtext 
                maxlength="7"
                v-model.number="searchForm.bukkenKbn"
                :width="'90px'"
              >
              </reafsinputtext>
            </el-form-item>
          </el-col>
          <el-col :span="9" :xs="24">
            <el-form-item label="物件住所" prop="bukkenAddress">
              <eigyoten 
                v-model="searchForm.bukkenAddress"
                :disabled="false"
                :appendlabelText="labelText"
                :append="'append'"
                @backData="reafsmodalBack"
              >
              </eigyoten>
            </el-form-item>
          </el-col>
        </el-form>
    </el-row>
    <el-menu :default-active="activeIndex" class="el-menu-class" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">依頼</el-menu-item>
      <el-menu-item index="2">見積</el-menu-item>
      <el-menu-item index="3">概要書</el-menu-item>
      <el-menu-item index="4">作業結果</el-menu-item>
    </el-menu>
    
    <el-row :gutter="20" v-if="selectIndex =='1'">
      <!-- 共通コントロール　サンプル -->
      <el-col :span="12" :xs="24">
        <el-form 
          :model="iraiForm"
          :rules="rules"
          ref="sampleForm"
          label-width="150px"
          class="sample-form-class"
        >
          <el-form-item label="入力エリア(必須)" prop="nyuryokuerea1">
            <reafsinputtext 
              maxlength="10"
              v-model="iraiForm.nyuryokuerea1"
              :width="'150px'"
              :required="true"
            ></reafsinputtext>
          </el-form-item>
          <el-form-item label="入力エリア" prop="nyuryokuerea2">
            <reafsinputtext 
              v-model="iraiForm.nyuryokuerea2"
              :width="'150px'"
            ></reafsinputtext>
          </el-form-item>  
          <el-form-item label="入力数値エリア" prop="nyuryokuerea3">
            <reafsinputnumber 
              v-model="iraiForm.nyuryokuerea3"
              :value="iraiForm.nyuryokuerea3"
              :width="'150px'"
              :required="true"
              autocomplete="off"
            ></reafsinputnumber>
          </el-form-item>
          <el-form-item label="日付選択" prop="nyuryokuerea6">
              <el-date-picker
                type="date"
                placeholder=""
                v-model="iraiForm.nyuryokuerea6"
              ></el-date-picker>
          </el-form-item>
          <el-form-item label="入力エリア(日付)" prop="nyuryokuerea4">
              <el-date-picker
                v-model="iraiForm.nyuryokuerea4"
                type="daterange"
                format="yyyy/MM/dd"
                class="date-picker"
                range-separator="~"
                start-placeholder="開始日"
                end-placeholder="終了日">
              </el-date-picker>
          </el-form-item> 
          <el-form-item label="入力エリア(検索)" prop="nyuryokuerea5">
            <reafsinputtext 
              maxlength="10"
              v-model="iraiForm.nyuryokuerea5"
              :width="'150px'"
              :required="true"
              :appendBackground="false"
            >
              <template v-slot:append>
                <el-button icon="el-icon-search" @click="showDialog = true"></el-button>
                <el-button icon="el-icon-search" @click="showDialog = true"></el-button>
              </template>
            </reafsinputtext>
          </el-form-item> 

        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>

import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafsmodal from '@/components/modal/Modal.vue'
import eigyoten from '@/components/basic/EigyoTen.vue'

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafsmodal,
    eigyoten,
  },
  data() {
    return {
      searchForm:{
        eigyotenCd:'',
        koujiiraiNo:'',
        bukken:'',
        bukkenKbn:'',
        bukkenAddress:'',
      },
      rules: {
        eigyotenCd: [
           //{ required: true, message: '入力してください', trigger: 'blur' },
        ],
        koujiiraiNo: [
           { type: 'number', message: '数値を入力してください', trigger: 'blur'}
        ],
      },
      showDialog: false,
      eigyotenNm:'',
      labelText:'',
      activeIndex: '1',
      selectIndex: '1',
      iraiForm:{
        nyuryokuerea1:'',
        nyuryokuerea2:'',
        nyuryokuerea3:'',
        nyuryokuerea4:'',
        nyuryokuerea5:'',
        nyuryokuerea6:'',
      },
    };
  },
  mounted() {
  },
  methods: {
    onSearchSubmit(){
      console.log(this.iraiForm);
    },

    //戻すデータ
    reafsmodalBack(obj){
      this.searchForm.eigyotenCd = obj.torihikisakicd
      this.eigyotenNm = obj.torihikisakinm
    }, 

    handleSelect(key, keyPath) {
        console.log(key, keyPath);
        this.selectIndex = key;
      }
  }
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
  .home-contianer{
    padding: 10px;
  }
  .el-menu-class{
    margin-bottom: 10px;

    li{
      height: 30px;
      line-height: 30px;
    }
  }
</style>