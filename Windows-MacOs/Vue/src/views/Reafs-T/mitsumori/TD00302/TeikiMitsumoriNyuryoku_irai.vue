<template>
  <div class="Main-contianer BukkenDokyumentoSanshoSabuins">
    <el-row :gutter="10">
      <!-- １行目 -->
      <el-col :span="4">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="契約№">
            <el-col Class="row-bg"
              ><label>{{ this.searchForm.keiyakuNo }}</label></el-col
            >
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="依頼№" prop="iraiNO">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.iraiNO }}</label>
            </el-col>
          </el-form-item>

          <!-- <el-button @click="toIrai()">依頼内容表示</el-button> -->
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="契約管理番号">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.keiyakukanriNO }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ２行目 -->
      <el-col :span="3">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="提出先">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.syuzenKBNsyo }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="10">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
          v-show="hiddenFLG == 8"
        >
          <el-form-item label="依頼者">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.iraisya }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="4">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col>
            <el-label>見積依頼日：{{ this.searchForm.iraibi }}</el-label>
          </el-col>
        </el-form>
      </el-col>

      <el-col :span="5">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="200px"
        >
          <el-col>
            <el-label>契約期間：{{ this.searchForm.keiyakukikan }}</el-label>
          </el-col>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ３行目 -->
      <el-col :span="10">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="契約名称">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.keiyakumeisyo }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ４行目 -->
      <el-col :span="4">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="修繕区分">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.syuuzenKBNdai }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ５行目 -->
      <el-col :span="18">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
          
        >
          <el-form-item label="依頼内容／不具合状況等" class="multi">
            <el-col style="height: 70px">
              <!-- <label>{{ this.searchForm.irainaiyojoukyo }}</label> -->
              <el-input
                type="textarea"
                v-model="searchForm.irainaiyojoukyo"
                height="500"
                disabled="disabled"
                class="is-gray-label"
                style="padding: 0px"
                resize="none"
              ></el-input>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ７行目 -->
      <el-col :span="4">
        <el-form
          v-show="hiddenFLG == 8"
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="依頼者" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.iraisyaCD }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="12">
        <el-form
          v-show="hiddenFLG == 8"
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col Class="row-bg">
            <label>{{ this.searchForm.iraisya }}</label>
          </el-col>
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-form
          v-show="hiddenFLG == 8"
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="連絡先" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.iraisyaTEL }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ８行目 -->
      <el-col :span="4">
        <el-form
          v-show="hiddenFLG == 8"
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="依頼代理者" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.iraisyadairiCD }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="12">
        <el-form
          v-show="hiddenFLG == 8"
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col Class="row-bg">
            <label>{{ this.searchForm.iraisyadairi }}</label>
          </el-col>
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-form
          v-show="hiddenFLG == 8"
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="連絡先" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.iraisyadairiTEL }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="10">
      <!-- ９行目 -->
      <el-col :span="4">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="ＳＧＲ担当者" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.uketsukesyaCD }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :span="12">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-col Class="row-bg">
            <label>{{ this.searchForm.uketsukesya }}</label>
          </el-col>
        </el-form>
      </el-col>

      <el-col :span="6">
        <el-form
          :model="searchForm"
          :rules="rules"
          ref="Main"
          label-width="100px"
        >
          <el-form-item label="連絡先" class="m-bottom-less">
            <el-col Class="row-bg">
              <label>{{ this.searchForm.uketsukesyaTEL }}</label>
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnumber from "@/components/basic/ReafsControl/ReafsInputNumber.vue";
import reafsmodal from "@/components/modal/Modal.vue";

export default {
  beforeRouteEnter(to, form, next) {
    next((vm) => {
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
      Main: {
        keiyakuNo: "",
      },

      queryParams: {
        keiyakuNo: "",
        hansu: "",
        rirekiNo: "",
      },

      searchForm: {
        keiyakuNo: "",
        hansu: "",
        rirekiNO: "",
        iraiNO: "",
        keiyakukanriNO: "",
        syuzenKBNsyo: "",
        iraisya: "",
        keiyakumeisyo: "",
        keiyakukikan: "",
        syuuzenKBNdai: "",
        irainaiyojoukyo: "",

        iraisyaCD: "",
        iraisyaTEL: "",
        iraisyadairiCD: "",
        iraisyadairi: "",
        iraisyadairiTEL: "",
        uketsukesyaCD: "",
        uketsukesya: "",
        uketsukesyaTEL: "",
      },

      tableData: [],
      hiddenFLG: 0,
      rules: {},

      showDialog: false,
    };
  },
  mounted() {
    // window.onload = ()=>{
    //   initialize();
    // }
    console.log("定期見積依頼内容表示処理開始");

    this.queryParams = {
      keiyakuNo: this.$route.query.keiyakuNo,
      hansu: this.$route.query.hansu,
      rirekiNo: this.$route.query.rirekiNo,
    };

    this.hideLeftMenu(false);
    this.GetMitsumoriData();
  },

  inject: ["hideLeftMenu"],

  methods: {
    onSubmit() {
      console.log(1);
    },
    search() {
      this.http
        .post(
          "/api/Reafs_T/TeikiMitsumoriNyuryoku/searchIrai",
          this.queryParams,
          "検索しています...."
        )
        .then((result) => {
          if (!result.status) {
            return this.$message.error(result.message);
          }

          this.searchForm.keiyakuNo = result.data[0].keiyakuNO;
          this.searchForm.rirekiNO = result.data[0].rirekiNO;
          this.searchForm.iraiNO = result.data[0].iraiNO;
          this.searchForm.keiyakukanriNO = result.data[0].keiyakukanriNO;
          this.searchForm.syuzenKBNsyo = result.data[0].syuzenKBNsyo;
          this.searchForm.iraisya = result.data[0].iraisya;
          this.searchForm.keiyakumeisyo = result.data[0].keiyakumeisyo;
          this.searchForm.iraibi = result.data[0].iraibi;
          this.searchForm.keiyakukikan =
            result.data[0].keiyakuYMS + "~" + result.data[0].keiyakuYME;
          this.searchForm.syuuzenKBNdai = result.data[0].syuuzenKBNdai;
          this.searchForm.irainaiyojoukyo = result.data[0].irainaiyojoukyo;

          this.searchForm.iraisyaCD = result.data[0].iraisyaCD;
          this.searchForm.iraisya = result.data[0].iraisya;
          this.searchForm.iraisyaTEL = result.data[0].iraisyaTEL;
          this.searchForm.iraisyadairiCD = result.data[0].iraisyadairiCD;
          this.searchForm.iraisyadairi = result.data[0].iraisyadairi;
          this.searchForm.iraisyadairiTEL = result.data[0].iraisyadairiTEL;
          this.searchForm.uketsukesyaCD = result.data[0].uketsukesyaCD;
          this.searchForm.uketsukesya = result.data[0].uketsukesya;
          this.searchForm.uketsukesyaTEL = result.data[0].uketsukesyaTEL;

          this.hiddenFLG = result.data[0].koubaisinseijoukyo;
        });
    },

    GetMitsumoriData() {
      this.search();
    },

    initialize() {},
    Onclick() {
      this.$message.error("123");
      this.Main.iraiNO = "234";
    },

    //サブ画面から戻すデータ
    reafsmodalBack(obj) {
      //   this.sampleForm.nyuryokuerea5 = obj
    },
  },
};
</script>
<style lang="less" scoped>
.el-col {
  border-radius: 4px;
  padding-left: 0px !important;
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
  background-color: lightgray;
  height: 30px;
}

.Main-contianer {
  height: fit-content !important;
  padding: 10px;
}

html,
body,
#app {
  min-width: 0 !important ;
}

.el-scrollbar__wrap,
.el-scrollbar,
.main-content {
  height: fit-content !important;
}

.BukkenDokyumentoSanshoSabuins .ve-table-container {
  height: 550px !important;
}

.date-picker {
  width: 250px;
}

.el-input-len-5 {
  width: 60px;
}
.el-input-serach-len-5 {
  width: 130px;
}
.el-input-len-10 {
  width: 120px;
}
.el-input-date-len-10 {
  width: 140px;
}
.el-input-len-20 {
  width: 100px;
}
.el-input-len-60 {
  width: 300px;
}
.el-input-len-200 {
  width: 300px;
}
// .multi-label{
//   height:300px;
// }

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.br {
  display: block;
  content: "";
  height: 35px;
}

.footer {
  position: absolute;
  bottom: 0;
}

.btn_updown {
  width: 30px;
  padding: 0px;
  box-sizing: border-box;
  border: 1px;
  cursor: pointer;
}
.m-bottom-less {
  margin-bottom: 0px;
}

span {
  color: red;
}
</style>
<style scoped>
.el-form .multi >>> .el-form-item__label {
  height: 70px;
}

.is-gray-label >>> .el-textarea__inner {
  background-color: #d3d3d3 !important;
  color: black !important;
  height: 70px;
}
</style>


