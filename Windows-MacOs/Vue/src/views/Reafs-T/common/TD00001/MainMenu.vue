<template>
  <div class="home-contianer">
    <el-card shadow="never">
      <div class="clearfix card-header">
        <span class="card-title">ステータス</span>
        <!-- <el-button
          style="float: right; padding: 3px 0; color: #000000"
          type="reload"
          @click="getCnt">再読込</el-button> -->

      </div>
      <div class ="grid-content bg-purple-light">
        <div class="cnt-col" >
          <div class="koujimiuketsukekinkyu cnt-row">
            <div>修繕</div>
            <div>工事未受付（緊急）</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00300'}" id="cnt1">
              </router-link>-->
              <el-link @click="statuslink('cnt1')" id="cnt1"></el-link>
              <span>件</span>
            </div>
          </div>
          <div class="koujimioutakukugaku cnt-row">
            <div>修繕</div>
            <div>工事未応諾（高額）</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00300'}" id="cnt2"></router-link>-->
              <el-link @click="statuslink('cnt2')" id="cnt2"></el-link>
              <span>件</span>
            </div>
          </div>
            <div class="koujimioutaku cnt-row">
            <div>修繕</div>
            <div>工事未応諾</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00100'}" id="cnt3"></router-link>-->
              <el-link @click="statuslink('cnt3')" id="cnt3"></el-link>
              <span>件</span>
            </div>
          </div>
        </div>
        <div class="cnt-col">
          <div class="mitsumorimiteisyutsu cnt-row">
            <div>修繕・定期委託</div>
            <div>見積未提出</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00300'}" id="cnt4"></router-link>-->
              <el-link @click="statuslink('cnt4')" id="cnt4"></el-link>
              <span>件</span>
            </div>
          </div>
        </div>
        <div class="cnt-col">
          <div class="juntyuzumimikanryo cnt-row">
            <div>修繕</div>
            <div>受注済未完了</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00100'}" id="cnt5"></router-link>-->
              <el-link @click="statuslink('cnt5')" id="cnt5"></el-link>
              <span>件</span>
            </div>
          </div>
        </div>
        <div class="cnt-col">
          <div class="sagyoyoteiminyuuryokusyuzenn cnt-row">
            <div>修繕</div>
            <div>作業予定未入力</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00100'}" id="cnt6"></router-link>-->
              <el-link @click="statuslink('cnt6')" id="cnt6"></el-link>
              <span>件</span>
            </div>
          </div>
          <div class="sagyoyoteiminyuuryokuteiki cnt-row">
            <div>定期委託</div>
            <div>作業予定未入力</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00400'}" id="cnt7"></router-link>-->
              <el-link @click="statuslink('cnt7')" id="cnt7"></el-link>
              <span>件</span>
            </div>
          </div>
        </div>
        <div class="cnt-col">
          <div class="sagyokanryouminyuuryokusyuzenn cnt-row">
            <div>修繕</div>
            <div>作業完了未入力</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00100'}" id="cnt8"></router-link>-->
              <el-link @click="statuslink('cnt8')" id="cnt8"></el-link>
              <span>件</span>
            </div>
          </div>
          <div class="sagyokanryouminyuuryokuteiki cnt-row">
            <div>定期委託</div>
            <div>作業完了未入力</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00400'}" id="cnt9"></router-link>-->
              <el-link @click="statuslink('cnt9')" id="cnt9"></el-link>
              <span>件</span>
            </div>
          </div>
            <div class="honsyoruimisakusei cnt-row">
            <div>定期委託</div>
            <div>本書類未提出</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00400'}" id="cnt10"></router-link>-->
              <el-link @click="statuslink('cnt10')" id="cnt10"></el-link>
              <span>件</span>
            </div>
          </div>
        </div>
        <div class="cnt-col">
          <div class="seikyumisyori cnt-row">
            <div><br></div> 
            <div>請求未処理</div>
            <div class ="cnt-link">
              <!--<router-link :to="{name:'TD00500'}" id="cnt11"></router-link>-->
              <el-link @click="statuslink('cnt11')" id="cnt11"></el-link>
              <span>件</span>
            </div>
          </div>
        </div>
      </div>
    </el-card>
    <el-card shadow="never">
      <div class="clearfix card-header">
        <span class="card-title">お知らせ通知</span>
      <!-- <el-button style="float: right; padding: 3px 0; color: #000000" type="reload"
        @click="getNews">再読込</el-button> -->
      </div>
      <div>
        <div class="grid-content bg-purple-light">
          <reafstable
                height="415"
                v-loading="loading"
                ref="newsData2"
                :single="single"
                :rowKey="rowKey"
                :tableData="newsData2"
                :columns="oshiraseColumns"
                :pagination="oshirasePagination"
                :pagination-hide="true"
                :columnIndex="false"
                :columnChkbox="false"
                @backData1="oshirasePageBack"
                :linkView="linkViewOshirase"
                :key="Math.random()"
                :cellClassFunction="this.cellClassFunctionList"
              >
              </reafstable>
          <!--<el-table
            v-loading="loading"
            :data="newsData"
            height="480"
            style="width: 100%">
                
            <el-table-column prop="KEIJI_S_YMD" label="掲載日" width="120">
            </el-table-column>
            <el-table-column prop="TOUROKU_KBN" label="登録システム" width="150">
            </el-table-column>
            <el-table-column prop="TOUROKU_BUSHO" label="登録者部署" width="300">
            </el-table-column>
            <el-table-column prop="TITLE" label="件名"> </el-table-column>
          </el-table>-->
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import reafstable from "@/components/table/Table.vue";

var $thisValue;

export default {
  components: {
    reafstable,
  },
  data() {
    return {
      loading: false,
      single: false,
      rowKey: undefined,
      loginInfo: {
        userName: '',
        passWord: '',
        shozokuBusho: '',
        shozokuBushoName: ''
      },
      value1: "1",
      tableData: [],
      newsData: [],
      newsData2: [],


      // お知らせ
      oshirasePagination: { total: 0, size: 0, sortName: "", page: 1 },
      oshiraseColumns: [
        {
          field: "KEIJI_S_YMD",
          title: "掲載日",
          type: "string",
          width: "15",
          readonly: false,
          align: "left",
          link: true,
        },
        {
          field: "TOUROKU_KBN",
          title: "登録システム",
          type: "string",
          width: "18",
          readonly: false,
          align: "left",
          link: true,
        },
        {
          field: "TOUROKU_BUSHO",
          title: "登録者部署",
          type: "string",
          width: "30",
          readonly: false,
          align: "left",
          link: true,
        },
        {
          field: "TITLE",
          title: "件名",
          type: "string",
          //width: "20",
          readonly: false,
          align: "left",
          link: true, // リンクを有効に
        },
      ],
    };
  },

  inject: ["hideLeftMenu"],

  created() {
    $thisValue = this;
    this.hideLeftMenu(true);
    this.getNews();
    this.init();
  },

  methods: {
    init() {
      let userLogIn = this.$store.getters.getUserInfo();
      this.loginInfo.userName = userLogIn.userId;
      this.getCnt();
    },

    statuslink(item) {
      var statusID = ''
      if(Number(document.getElementById(item).innerText.replaceAll(",", "")) <= 0){
        return false
      }
      switch (item) {
        case "cnt1":
          //工事未受付（緊急）
          statusID = 'TD00300'
          this.$router.push({ name: statusID , params: {StartupMode: '1'}});
          break;
        case "cnt2":
          //工事未応諾（高額）
          statusID = 'TD00100'
          this.$router.push({ name: statusID , params: {StartupMode: '4'}});
          break;
        case "cnt3":
          //工事未応諾
          statusID = 'TD00100'
          this.$router.push({ name: statusID , params: {StartupMode: '5'}});
          break;
        case "cnt4":
          //見積未提出
          statusID = 'TD00300'
          this.$router.push({ name: statusID , params: {StartupMode: '2'}});
          break;
        case "cnt5":
          //受注済未完了
          statusID = 'TD00100'
          this.$router.push({ name: statusID , params: {StartupMode: '1'}});
          break;
        case "cnt6":
          //作業予定未入力(修繕)
          statusID = 'TD00100'
          this.$router.push({ name: statusID , params: {StartupMode: '2'}});
          break;
        case "cnt7":
          //作業予定未入力(定期委託)
          statusID = 'TD00400'
          this.$router.push({ name: statusID , params: {StartupMode: '1'}});
          break;
        case "cnt8":
          //作業完了未入力(修繕)
          statusID = 'TD00100'
          this.$router.push({ name: statusID , params: {StartupMode: '3'}});
          break;
        case "cnt9":
          //作業完了未入力(定期委託)
          statusID = 'TD00400'
          this.$router.push({ name: statusID , params: {StartupMode: '2'}});
          break;
        case "cnt10":
          //本書類未提出
          statusID = 'TD00400'
          this.$router.push({ name: statusID , params: {StartupMode: '3'}});
          break;
        case "cnt11":
          //請求未処理
          statusID = 'TD00500'
          this.$router.push({ name: statusID , params: {StartupMode: '1'}});  //2022/09/23 引数追加
          break;
        default:
          break;
      }
    },

    getNews(){
      let _this = this
      var data = []
      _this.loading = true
      $thisValue.newsData = [];
      $thisValue.newsData2 = [];
      this.http
        .post("api/Reafs_T/TD00001/getNews")
        .then(function (response) {
            for (let i = 0; i < response.data.length; i++) {
              var obj = {}
              obj.KEIJI_S_YMD= response.data[i].KEIJI_S_YMD
              obj.TOUROKU_KBN = response.data[i].TOUROKU_KBN
              obj.TOUROKU_BUSHO = response.data[i].TOUROKU_BUSHO
              obj.TITLE = response.data[i].TITLE
              obj.KEIJI_ID = response.data[i].KEIJI_ID
              data[i] = obj
            } 

          _this.newsData = data
          $thisValue.newsData2.push(...response.data);
          $thisValue.oshirasePagination.total = response.data.length;
          $thisValue.oshirasePagination.size = response.data.length;
          _this.loading = false
        });
    },
    oshirasePageBack(obj) {
      this.oshirasePagination.page = obj;
    },
    linkViewOshirase(row, column, rowIndex) {
      // お知らせリンク押下時の処理
      this.$router.push({ name: "TD00002", params: {KEIJI_ID: row.KEIJI_ID} });
    },
    cellClassFunctionList(data) {
      let returnStr = "";
      let row = data.row;

      if (data.columnIndex == "0") {
        returnStr += "noPadding";
      }
 
      return returnStr;
    },
    getCnt(){
      this.http
        .post("api/Reafs_T/TD00001/getCnt", this.loginInfo)
        .then(function (response) {
          for (let i = 0; i < response.data.length; i++) {
            var intCnt = response.data[i]    

            document.getElementById("cnt" + (i+1)).innerText = intCnt
            /*if(intCnt == 0){
              document.getElementById("cnt" + (i+1)).innerHTML = '<span id="prevent'+(i+1)+'">0</span>'
              const span = document.getElementById("prevent"+(i+1))
              span.addEventListener("click", function(event) {
                event.preventDefault()
              })
            }else{
              document.getElementById("cnt" + (i+1)).innerHTML = '<span>' + response.data[i] + '</span>'
              
            }*/
          }
        });
    }
  },
  mounted() {
    window.onload = ()=>{
      this.getCnt()
      this.getNews()
    } 
  },
  watch: {
    $route(to, from) {
        this.getCnt()
        this.getNews()
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

.home-container {
  min-height: 726px;
  min-width: 1024px;
  max-width: 1620px;
  max-height: 884px;
  padding: 10px;
}

.cnt-col {
  float: left ;
  padding-left: 20px;
}

.cnt-row {
  padding-bottom: 30px;
  width: 150px;
  font-size: 90%;
}

.card-header{
  width: 100%;
  padding: 10px;
  border-style: solid;
  border-color: #333333;
  border-width: 1px 1px 0px 1px;
  background-color: #808080;
}

.bg-purple-light{
  float: left ;
  width: 100%;
  padding-left: 20px;
  padding-right: 20px;
  margin-bottom:20px;
  border: 1px solid #333333;
}

.card-title{
  color: #FFFFFF;
  font-family: "メイリオ"
}

.cnt-link{
  float: right ;
  padding-right: 40%;
}
</style>

<style scoped>
</style>
