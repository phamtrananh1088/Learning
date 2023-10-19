<template>
  <div class="home-container IraiSansyoSub">
    <el-form
      :model="IraiSansyoSub"
      ref="IraiSansyoSubForm"
      label-width="120px"
      class="sample-form-class"
      :hide-required-asterisk="true"
    >
      <el-container class="home-el-container">
        <el-main class="home-el-main">
          <el-row>
            <el-col :span="8">
              <div>
                <label> 取引先：{{ queryParams.取引先 }} </label>
              </div>
            </el-col>
          </el-row>

          <el-row>
            <!-- 明細 -->
            <reafstable
              ref="table"
              :maxHeight="420"
              :tableData="tblIraiData"
              :columns="tblIraiColumns"
              :cellStyleOption="cellStyleOptionMeisai"
              rowKeyFieldName="key"
              class="tblMeisai"
            >
            </reafstable>
          </el-row>

          <el-row style="margin: 1% 0 0 0; width:940px;">
            <el-col>
              <div style="display: flex">
                <!-- 位置調整用DIV -->
                <div disabled="disabled" style="width: 333px; height: 25px;"></div>

                <!-- 入力金額合計行 -->
                <div disabled="disabled"><label for="kingaku04" class="el-form-item__label" style="width: 120px;">入力合計額</label></div>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="sansyoSubForm.tax"
                  :required="false"
                  :width="'65px'"
                  ref="入力税率"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="syoukeiForm.kingaku04"
                  :required="false"
                  :width="'140px'"
                  ref="税抜按分金額_入力"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="syoukeiForm.kingaku05"
                  :required="false"
                  :width="'140px'"
                  ref="按分金額消費税_入力"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="syoukeiForm.kingaku06"
                  :required="false"
                  :width="'140px'"
                  ref="税込按分金額_入力"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
              </div>
            </el-col>
          </el-row>

          <el-row style="margin: 1% 0 0 0; width:940px;">
            <el-col>
              <div style="display: flex">
                <!-- 位置調整用DIV -->
                <div disabled="disabled" style="width: 333px; height: 25px;"></div>

                <!-- 按分元金額表示行 -->
                <div disabled="disabled"><label for="kingaku04" class="el-form-item__label" style="width: 120px;">按分元金額</label></div>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="sansyoSubForm.tax"
                  :required="false"
                  :width="'65px'"
                  ref="按分元税率"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="sansyoSubForm.kingakuZeinuki"
                  :required="false"
                  :width="'140px'"
                  ref="税抜按分元"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="sansyoSubForm.kingakuSyohizei"
                  :required="false"
                  :width="'140px'"
                  ref="按分元消費税"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
                <reafsinputnumber
                  :intLength="12"
                  :floatLength="0"
                  v-model="sansyoSubForm.kingakuZeikomi"
                  :required="false"
                  :width="'140px'"
                  ref="税込按分元"
                  :disabled="'true'"
                  class="is-gray-label"
                ></reafsinputnumber>
              </div>
            </el-col>
          </el-row>

          <el-footer>
            <el-row class="footer_row">
              <div class="footer_button">
                <el-col>
                  <el-button 
                    :loading="loading_更新"
                    @click="onIraiKousin"
                    :disabled="queryParams.部門確認 == '部門確認済'"
                    >更新</el-button
                  >
                  <el-button :loading="loading_閉じる" @click="onBack"
                    >閉じる</el-button
                  >
                </el-col>
              </div>
            </el-row>
          </el-footer>
        </el-main>
      </el-container>
    </el-form>
    <reafsmodalSearchHiyoFutanBumonSabu
      :dialogVisible.sync="showDialog"
      :subParamsObj="subParamsObj"
      @backData="dataBack">
    </reafsmodalSearchHiyoFutanBumonSabu>
  </div>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnumber from '@/components/basic/ReafsControl/ReafsInputNumber.vue'
import reafstable from "@/components/table/Grid.vue";
import opensearch from "@/components/basic/openSearch.vue";
import reafsmodal from '@/components/modal/Modal.vue'
import reafsmodalSearchHiyoFutanBumonSabu from '@/views/Reafs-W/search/WS00520/Modal_SearchHiyoFutanBumonSabu.vue'

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafstable,
    opensearch,
    reafsmodal,
    reafsmodalSearchHiyoFutanBumonSabu
  },

  data() {
    return {
      queryParams: {
        会社コード: "",
        契約NO: "",
        契約NO枝番: "",
        履歴NO: "",
        明細NO: "",
        契約年度: "",
        取引先: "",
        修繕区分小: "",
        負担区分: "",
        部門確認: "",
        部門コード: "",
        部門名称: ""
      },

      userInfo: {
        ユーザーＩＤ: "",
        会社コード: ""
      },

      sansyoSubForm: {
        tax: 0,
        kingakuZeinuki: 0,
        kingakuSyohizei: 0,
        kingakuZeikomi: 0
      },

      syoukeiForm: {
        tax: 0,
        kingaku04: 0,
        kingaku05: 0,
        kingaku06: 0
      },

      meisaiDisable: false,
      
      tblIraiColumns: [
        {
          key: '1',
          title: "費用負担部門",
          width: 450,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="hiyoFutanBumon" style="display: inline-flex;">
                <div>
                  <reafsinputtext
                    style="display:flex;"
                    maxlength="8"
                    value={row.tbl費用負担部門コード}
                    ref={'ref費用負担部門' + rowIndex}
                    width="110px"
                    disabled={this.meisaiDisable}
                    on-input={(val) => {
                      row.tbl費用負担部門コード = val
                    }}
                    on-change={(value) => { this.onChangeHiyouHutanBumon(value, rowIndex) }}
                  >
                  </reafsinputtext>
                </div>
                <div>
                  <el-button
                    class="btnHiyoFutanBumon"
                    icon="el-icon-search"
                    on-click={() => { this.onShowAnbunDialog(rowIndex) }}
                    disabled={this.meisaiDisable}
                  ></el-button>
                </div>
                <div>
                  <span style="line-height: 30px;">
                    {row.tbl費用負担部門名称}
                  </span>
                </div>
              </div>
            )
          }
        },

        {
          key: '2',
          title: '税率',
          width: 65,
          field: 'tbl税率',
          align: 'center'
        },

        {
          key: '3',
          title: "税抜按分金額",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl税抜按分金額}
                  width="100%"
                  ref={"ref税込按分金額" + rowIndex}
                  on-input={val => {
                    row["tbl税抜按分金額"] = val;
                  }}
                  on-change={val => {
                    this.txtInputDataChanged(4, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: '4',
          title: "税抜按分消費税",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl按分金額消費税}
                  width="100%"
                  ref={"ref按分金額消費税" + rowIndex}
                  on-input={val => {
                    row["tbl按分金額消費税"] = val;
                  }}
                  on-change={val => {
                    this.txtInputDataChanged(5, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: '5',
          title: "税込按分金額",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl税込按分金額}
                  width="100%"
                  ref={"ref税込按分金額" + rowIndex}
                  on-input={val => {
                    row["tbl税込按分金額"] = val;
                  }}
                  on-change={val => {
                    this.txtInputDataChanged(6, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

      ],
      
      tblIraiData: [{
        tbl費用負担部門コード: '',
        tbl費用負担部門名称: '',
        tbl税率: 0,
        tbl税抜按分金額: 0,
        tbl按分金額消費税: 0,
        tbl税込按分金額: 0
      }],

      putData:{
        会社コード: '',
        契約NO: '',
        履歴NO: '',
        契約NO枝番: '',
        契約年月: '',
        明細NO: 0,
        WS00532_GridData: [{
          tbl按分サブNO: 0,
          tbl費用負担部門コード: '',
          tbl費用負担部門名称: '',
          tbl税率: 0,
          tbl税抜按分金額: 0,
          tbl按分金額消費税: 0,
          tbl税込按分金額: 0
        }]
      },

      subParamsObj: {
        taisyou: '', // 承認種類　 　　　　 取引先検索：対象
        jigyousyoCd: '', // 事業所コード
        torType: ''
      },

      loading_更新: false,
      loading_閉じる: false,
      rowKey: 'rowKey',
      rowIndex: -1,
      showDialog: false
    };
  },

  watch: {},

  computed: {},

  inject: ["hideLeftMenu"],

  created() {
    let userLogIn = this.$store.getters.getUserInfo();
    this.userInfo.ユーザーＩＤ = userLogIn.userId;
    this.userInfo.会社コード = userLogIn.会社コード;
  },

  mounted() {
    this.queryParams = {
      会社コード: this.userInfo.会社コード,
      契約NO: this.$route.query.契約NO,
      契約NO枝番: this.$route.query.契約NO枝番,
      履歴NO: this.$route.query.履歴NO,
      明細NO: this.$route.query.明細NO,
      契約年月: this.$route.query.契約年月,
      取引先: this.$route.query.取引先,
      修繕区分小: this.$route.query.修繕区分小,
      負担区分: this.$route.query.負担区分,
      部門確認: this.$route.query.部門確認,
      部門コード: this.$route.query.部門コード,
      部門名称: this.$route.query.部門名称
    };

    if (this.queryParams.部門確認 == '部門確認済') {
      this.meisaiDisable = true;
    } else {
      this.meisaiDisable = false;
    }

    this.hideLeftMenu(false);
    this.getPageData();
  },

  methods: {

    async getPageData() {
      console.log("startgetPageData");

      // 按分元データ取得処理
      await this.SearchAnbunData();

      // 按分データ取得処理
      await this.SearchMeisai();
    },

    async SearchMeisai() {
      console.log("startSearchMeisai");

      let _this = this
      let mode = 0;

      if (_this.queryParams.修繕区分小 == "08"){
        // 直接なら見るのは原価、F013
        mode = 2;
      } else if(_this.queryParams.修繕区分小 == "09"){
        // 請負なら見るのは売価、F012
        mode = 1;
      }

      await this.http
      .get("/api/Reafs_W/Shonin/WD00530/GetPageData_K004", {
          契約No: this.queryParams.契約NO,
          契約No枝番: this.queryParams.契約NO枝番,
          契約年月: this.queryParams.契約年月,
          履歴no: this.queryParams.履歴NO,
          明細no: this.queryParams.明細NO,
          mode: mode
      })
      
      // 検索実行後処理
      .then(function(response) {
        // 按分データセット
        // 明細部分は9行固定、行追加などは行なわない。
        _this.tblIraiData = response.data;

        if(_this.tblIraiData.length < 9){
          if (_this.tblIraiData.length == 0)
          {
            _this.tblIraiData.push(_this.defaultDataMeisai());

            _this.tblIraiData[0].tbl費用負担部門コード = _this.queryParams.部門コード;
            _this.tblIraiData[0].tbl費用負担部門名称 = _this.queryParams.部門名称;

            _this.tblIraiData[0].tbl税率 = _this.sansyoSubForm.tax;
          }


          for (let i = _this.tblIraiData.length; i < 9; i++) 
          {
            _this.tblIraiData.push(_this.defaultDataMeisai())
          }
        }
        
        _this.sumCalcMeisai();
      })
    },

    async SearchAnbunData() {
      console.log("startSearchAnbunData");

      let _this = this
      let mode = 0;

      if (_this.queryParams.修繕区分小 == "08"){
        // 直接なら見るのは原価、F013
        mode = 2;
      } else if(_this.queryParams.修繕区分小 == "09"){
        // 請負なら見るのは売価、F012
        mode = 1;
      }

      await this.http
      .get("/api/Reafs_W/Shonin/WD00530/GetPageData_AnbunmotoData", {
          契約No: this.queryParams.契約NO,
          契約No枝番: this.queryParams.契約NO枝番,
          契約年月: this.queryParams.契約年月,
          履歴no: this.queryParams.履歴NO,
          明細no: this.queryParams.明細NO,
          mode: mode
      })
      
      // 検索実行後処理
      .then(function(response) {
        // 按分元データセット
        _this.sansyoSubForm.tax = response.data.tax;
        _this.sansyoSubForm.kingakuZeinuki = response.data.kingakuZeinuki;
        _this.sansyoSubForm.kingakuSyohizei = response.data.kingakuSyohizei;
        _this.sansyoSubForm.kingakuZeikomi = response.data.kingakuZeikomi;
      })
    },

    onBack() {
      window.parent.postMessage(
        {
          call: "closePopup"
        },
        "*"
      );
    },

    onIraiKousin() {
      console.log("更新処理実行");

      // エラーチェックおよび格納データ取得
      let checkResult = this.fnc_boolean_setSendData();

      if (checkResult == true) {
        // 登録確認メッセージ
        this.fnc_void_showTourokuMsg();
      }

    },

    fnc_boolean_setSendData() {
      var torokuGridDate = [];
      var subNumber = 0;

      if ((this.syoukeiForm.kingaku04 != this.sansyoSubForm.kingakuZeinuki) ||
          (this.syoukeiForm.kingaku05 != this.sansyoSubForm.kingakuSyohizei) ||
          (this.syoukeiForm.kingaku06 != this.sansyoSubForm.kingakuZeikomi) ) {
        this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致しません。

        return false;
      }

      for (let i = 0; i < this.tblIraiData.length; i++) {

        if (this.tblIraiData[i].tbl費用負担部門コード.length != 0) {
          // 費用負担部門が設定されていたら登録対象データ

          torokuGridDate.push(this.tblIraiData[i]);
          torokuGridDate[i].tbl按分サブNO = subNumber;
          subNumber += 1;

        } else {

          if (this.tblIraiData[i].tbl税抜按分金額 != 0 || this.tblIraiData[i].tbl按分金額消費税 != 0 || this.tblIraiData[i].tbl税込按分金額 != 0)
          {
            this.fnc_void_showInfoMsg("E040777"); // 費用負担部門が未設定の行に金額が入力されています。

            return false;
          }
        }
      }

      if (torokuGridDate.length <= 1)
      {
        this.fnc_void_showInfoMsg("E040787"); // 費用負担部門を複数設定して下さい。
          
        return false;
      }

      console.log(torokuGridDate);
      this.putData.WS00532_GridData = torokuGridDate;
      this.putData.会社コード = this.queryParams.会社コード;
      this.putData.契約NO = this.queryParams.契約NO;
      this.putData.履歴NO = this.queryParams.履歴NO;
      this.putData.契約NO枝番 = this.queryParams.契約NO枝番;
      this.putData.契約年月 = this.$route.query.契約年月;
      this.putData.明細NO = this.queryParams.明細NO;
      console.log(this.putData.WS00532_GridData);

      return true;
    },

    fnc_void_showTourokuMsg() {
      this.fnc_rtn_getMsg('K000004').then(response => {
          this.MsgConf(
            {
              title: "按分入力",
              message: response.data
            },
            () => {
              // OKが選択された場合
              // 登録処理実行
              this.fnc_void_Update();
              return true;
            },
            () => {
              // Cancelが選択された場合
              return false;
            }
          );
        });
    },

    async fnc_void_Update() {
       //登録データ渡し
      let letval_touroku = new Object();
      let _this = this;
      letval_touroku = _this.putData;

      console.log(letval_touroku);

      await _this.http
        .post(
          "/api/Reafs_W/Shonin/WD00530/WD00532_Put_AnbunData",
          letval_touroku,
          "登録しています...."
        )
        .then(function(response) {
          if (response.status = true) {
            _this.fnc_void_showInfoMsg("M000008");
            
            // 再取得
            _this.getPageData();
          }
        });
    },

    onChangeHiyouHutanBumon(value, rowIndex) {
      console.log("費用負担部門変更時処理");

      let _this = this

      if (value == '') 
      {
        _this.tblIraiData[rowIndex].tbl費用負担部門名称 = ''
        _this.tblIraiData[rowIndex].tbl税率 = ''
      }
      else
      {
        this.http
          .get('/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34', { 費用負担部門コード: value })
          .then((response) =>{
            if (response.data !== null) 
            {
              _this.tblIraiData[rowIndex].tbl費用負担部門名称 = response.data.費用負担部門
              _this.tblIraiData[rowIndex].tbl税率 = _this.sansyoSubForm.tax
            } 
            else 
            {
              _this.fnc_rtn_getMsg('E040747').then((response) => {
                _this.fnc_void_showError(response.data, () => {
                  _this.$refs[`ref費用負担部門${Index}`].focus()
                })
              }).finally(() => {
              })
              _this.tblIraiData[rowIndex].tbl費用負担部門名称 = ''
              _this.tblIraiData[rowIndex].tbl税率 = ''
            }
          })
        }
    },

    reafsmodalBack_searchBukken (obj) {
      console.log("費用負担部門サブ画面使用時値セット処理");
      this.tblIraiData.tbl費用負担部門コード = obj.txtCd
      this.tblIraiData.tbl費用負担部門名称 = obj.lblNm
    },

    txtInputDataChanged(type, row) {
      console.log("Start_txtInputDataChanged");

      // 税抜按分金額変更時のみ、入力値に応じて消費税額と税込金額の自動計算を行なう
      if (type == 4) {
        // 税率を百分率から小数点へ変換
        let zeiritsu = row.tbl税率  / 100
        
        // 自動計算の結果を各項目にセット
        row.tbl按分金額消費税 = Number(row.tbl税抜按分金額.toString().replaceAll(',', '')) * zeiritsu
        row.tbl税込按分金額 = Number(row.tbl税抜按分金額.toString().replaceAll(',', '')) + Number(row.tbl税抜按分金額.toString().replaceAll(',', '')) * zeiritsu
      }
      
      // 列合計計算
      let 税抜按分金額sum = 0
      let 按分金額消費税sum = 0
      let 税込按分金額sum = 0
      for (let i = 0; i < this.tblIraiData.length; i++) {
        税抜按分金額sum += Number(this.tblIraiData[i].tbl税抜按分金額.toString().replaceAll(',', ''))
        按分金額消費税sum += Number(this.tblIraiData[i].tbl按分金額消費税.toString().replaceAll(',', ''))
        税込按分金額sum += Number(this.tblIraiData[i].tbl税込按分金額.toString().replaceAll(',', ''))
      }

      // 合計した結果を対象月の合計欄にセット
      console.log("税抜按分金額合計:" + 税抜按分金額sum.toLocaleString());
      this.syoukeiForm.kingaku04 = 税抜按分金額sum.toLocaleString();
      console.log("按分金額消費税合計:" + 按分金額消費税sum.toLocaleString());
      this.syoukeiForm.kingaku05 = 按分金額消費税sum.toLocaleString();
      console.log("税込按分金額合計:" + 税込按分金額sum.toLocaleString());
      this.syoukeiForm.kingaku06 = 税込按分金額sum.toLocaleString();

    },

    onShowAnbunDialog (index) {
      this.rowIndex = index
      this.showDialog = true
    },

    dataBack (obj) {
      console.log("費用負担部門選択サブ画面が閉じられたよ");
      
      const rowIndex = this.rowIndex

      this.tblIraiData[rowIndex].tbl費用負担部門コード = obj.txtCd
      this.tblIraiData[rowIndex].tbl費用負担部門名称 = obj.lblNm
      this.tblIraiData[rowIndex].tbl税率 = this.sansyoSubForm.tax
    },

    sumCalcMeisai () {
      let sum4 = 0;
      let sum5 = 0;
      let sum6 = 0;

      for (let i = 0; i < this.tblIraiData.length; i++) {
        sum4 += Number(this.tblIraiData[i].tbl税抜按分金額.toString().replaceAll(',', ''));
        sum5 += Number(this.tblIraiData[i].tbl按分金額消費税.toString().replaceAll(',', ''));
        sum6 += Number(this.tblIraiData[i].tbl税込按分金額.toString().replaceAll(',', ''));
      }

      this.syoukeiForm.kingaku04 = sum4.toLocaleString();
      this.syoukeiForm.kingaku05 = sum5.toLocaleString();
      this.syoukeiForm.kingaku06 = sum6.toLocaleString();

    },

    defaultDataMeisai () {
      return {
        tbl費用負担部門コード: '',
        tbl費用負担部門名称: '',
        tbl税率: 0,
        tbl税抜按分金額: 0,
        tbl按分金額消費税: 0,
        tbl税込按分金額: 0
      }
    },

    fnc_void_showInfoMsg(MsgCode) {
      this.fnc_rtn_getMsg(MsgCode).then(response => {
        this.MsgInfo({
          title: "按分入力",
          message: response.data
        });
      });
      return 1;
    },

    fnc_void_showError (message, option = null, title = '依頼参照・承認（実施判断）') {
      var self = this
      this.MsgErr({ title,
        message,
        callback: function (a, b) {
          self.hideLoading()
          if (option && typeof (option.callback) === 'function') {
            option.callback()
          }
        }})
    },

    fnc_rtn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    replaceAll (val) {
      return ((val || '') + '').replaceAll(',', '')
    }
  }

};
</script>

<style lang="less" scoped>
.home-container {
  padding: 5px;
  height: 100%;
  width: 100%;
}
.footer_row {
  margin: 3% 0 0 0;
  width: 95%;
}
.footer_button {
  
  margin: 0 0 0 55%;
}

.tblMeisai {
  width: 950px;
}

.btnHiyoFutanBumon {
  width: 40px;
  height: 30px;
  padding: 0 12px 0 12px !important;
  margin-left: -4px;
}
.IraiSansyoSub .hiyoFutanBumon {
  //position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.IraiSansyoSub .hiyoFutanBumon > div:nth-child(1) {
  /* padding-top: 5px;
  padding-bottom: 5px;
  padding-left: 5px; */
}
.IraiSansyoSub .hiyoFutanBumon > div:nth-child(2) {
  border-right: solid 1px #000;
  /* padding-top: 5px;
  padding-bottom: 5px;
  padding-right: 5px; */
}
.IraiSansyoSub .hiyoFutanBumon > div:nth-child(3) {
  padding-left: 5px;
}

</style>

<style scoped>
.IraiSansyoSub .is-gray-label >>> .el-input__inner {
  background-color: lightgray !important;
  color: black !important;
}

</style>
