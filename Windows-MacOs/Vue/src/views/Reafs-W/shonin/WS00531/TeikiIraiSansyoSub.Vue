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
                <label> 作業名：{{ queryParams.作業名称 }} </label>
              </div>
            </el-col>
          </el-row>

          <div class="tableMeisai" style="width: 1400px; overflow-y: scroll;">
            <el-row>
              <!-- 明細 -->
              <reafstable
                ref="tableHiyoAnbunShitei"
                :maxHeight="418"
                :height="418"
                :tableData="tblIraiData"
                :columns="tblIraiColumns"
                :cellStyleOption="cellStyleOptionMeisai"
                rowKeyFieldName="key"
                class="tblMeisai"
              >
              </reafstable>
            </el-row>

            <el-row style="margin: 1% 0 0 0; width:2350px;">
              <el-col>
                <div style="display: flex">
                  <!-- 位置調整用DIV -->
                  <div
                    disabled="disabled"
                    style="width: 333px; height: 25px;"
                  ></div>

                  <!-- 入力金額合計行 -->
                  <div disabled="disabled">
                    <label
                      for="kingaku04"
                      class="el-form-item__label"
                      style="width: 120px;"
                      >入力合計額</label
                    >
                  </div>
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
                    ref="4月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku05"
                    :required="false"
                    :width="'140px'"
                    ref="5月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku06"
                    :required="false"
                    :width="'140px'"
                    ref="6月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku07"
                    :required="false"
                    :width="'140px'"
                    ref="7月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku08"
                    :required="false"
                    :width="'140px'"
                    ref="8月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku09"
                    :required="false"
                    :width="'140px'"
                    ref="9月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku10"
                    :required="false"
                    :width="'140px'"
                    ref="10月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku11"
                    :required="false"
                    :width="'140px'"
                    ref="11月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku12"
                    :required="false"
                    :width="'140px'"
                    ref="12月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku01"
                    :required="false"
                    :width="'140px'"
                    ref="1月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku02"
                    :required="false"
                    :width="'140px'"
                    ref="2月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingaku03"
                    :required="false"
                    :width="'140px'"
                    ref="3月合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="syoukeiForm.kingakuSum"
                    :required="false"
                    :width="'150px'"
                    ref="総合計"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                </div>
              </el-col>
            </el-row>

            <el-row style="margin: 1% 0 0 0; width:2350px;">
              <el-col>
                <div style="display: flex">
                  <!-- 位置調整用DIV -->
                  <div
                    disabled="disabled"
                    style="width: 333px; height: 25px;"
                  ></div>

                  <!-- 按分元金額表示行 -->
                  <div disabled="disabled">
                    <label
                      for="kingaku04"
                      class="el-form-item__label"
                      style="width: 120px;"
                      >按分元金額</label
                    >
                  </div>
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
                    v-model="sansyoSubForm.kingaku04"
                    :required="false"
                    :width="'140px'"
                    ref="4月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku05"
                    :required="false"
                    :width="'140px'"
                    ref="5月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku06"
                    :required="false"
                    :width="'140px'"
                    ref="6月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku07"
                    :required="false"
                    :width="'140px'"
                    ref="7月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku08"
                    :required="false"
                    :width="'140px'"
                    ref="8月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku09"
                    :required="false"
                    :width="'140px'"
                    ref="9月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku10"
                    :required="false"
                    :width="'140px'"
                    ref="10月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku11"
                    :required="false"
                    :width="'140px'"
                    ref="11月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku12"
                    :required="false"
                    :width="'140px'"
                    ref="12月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku01"
                    :required="false"
                    :width="'140px'"
                    ref="1月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku02"
                    :required="false"
                    :width="'140px'"
                    ref="2月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingaku03"
                    :required="false"
                    :width="'140px'"
                    ref="3月按分元"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                  <reafsinputnumber
                    :intLength="12"
                    :floatLength="0"
                    v-model="sansyoSubForm.kingakuSum"
                    :required="false"
                    :width="'150px'"
                    ref="按分元年額"
                    :disabled="'true'"
                    class="is-gray-label"
                  ></reafsinputnumber>
                </div>
              </el-col>
            </el-row>
          </div>

          <el-footer>
            <el-row class="footer_row">
              <div class="footer_button">
                <el-col>
                  <el-button
                    :loading="loading_更新"
                    @click="onIraiKousin"
                    :disabled="queryParams.進捗コード_本体 > 400"
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
      @backData="dataBack"
    >
    </reafsmodalSearchHiyoFutanBumonSabu>
  </div>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnumber from "@/components/basic/ReafsControl/ReafsInputNumber.vue";
import reafstable from "@/components/table/Grid.vue";
import opensearch from "@/components/basic/openSearch.vue";
import reafsmodal from "@/components/modal/Modal.vue";
import reafsmodalSearchHiyoFutanBumonSabu from "@/views/Reafs-W/search/WS00520/Modal_SearchHiyoFutanBumonSabu.vue";

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
        契約年度: "",
        履歴NO: 0,
        明細NO: 0,
        相見積番号: 0,
        修繕区分小: "",
        負担区分: "",
        作業名称: "",
        部門コード: "",
        部門名称: ""
      },

      userInfo: {
        ユーザーＩＤ: "",
        会社コード: ""
      },

      meisaiDisable: false,

      sansyoSubForm: {
        tax: 0,
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
        kingakuSum: 0
      },

      syoukeiForm: {
        tax: 0,
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
        kingakuSum: 0
      },

      tblIraiColumns: [
        {
          key: "1",
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
                    ref={"ref費用負担部門" + rowIndex}
                    width="110px"
                    on-input={val => {
                      row.tbl費用負担部門コード = val;
                    }}
                    on-change={value => {
                      this.onChangeHiyouHutanBumon(value, rowIndex);
                    }}
                    disabled={this.meisaiDisable}
                  ></reafsinputtext>
                </div>
                <div>
                  <el-button
                    class="btnHiyoFutanBumon"
                    icon="el-icon-search"
                    on-click={() => {
                      this.onShowAnbunDialog(rowIndex);
                    }}
                    disabled={this.meisaiDisable}
                  ></el-button>
                </div>
                <div>
                  <span style="line-height: 30px;">
                    {row.tbl費用負担部門名称}
                  </span>
                </div>
              </div>
            );
          }
        },

        {
          key: "2",
          title: "税率",
          width: 65,
          field: "tbl税率",
          align: "center"
        },

        {
          key: "3",
          title: "4月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl4月}
                  width="100%"
                  ref={"ref4月" + rowIndex}
                  on-input={val => {
                    row["tbl4月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(4, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "4",
          title: "5月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl5月}
                  width="100%"
                  ref={"ref5月" + rowIndex}
                  on-input={val => {
                    row["tbl5月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(5, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "5",
          title: "6月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl6月}
                  width="100%"
                  ref={"ref6月" + rowIndex}
                  on-input={val => {
                    row["tbl6月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(6, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "6",
          title: "7月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl7月}
                  ref={"ref7月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl7月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(7, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "7",
          title: "8月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl8月}
                  ref={"ref8月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl8月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(8, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "8",
          title: "9月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl9月}
                  ref={"ref9月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl9月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(9, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "9",
          title: "10月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl10月}
                  ref={"ref10月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl10月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(10, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "10",
          title: "11月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl11月}
                  ref={"ref11月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl11月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(11, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "11",
          title: "12月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl12月}
                  ref={"ref12月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl12月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(12, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "12",
          title: "1月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl1月}
                  ref={"ref1月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl1月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(1, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "13",
          title: "2月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl2月}
                  ref={"ref2月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl2月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(2, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "14",
          title: "3月",
          width: 140,
          renderBodyCell: ({ row, column, rowIndex }) => {
            return (
              <div class="cell-body-custome">
                <reafsinputnumber
                  intLength={12}
                  floatLength={0}
                  value={row.tbl3月}
                  ref={"ref3月" + rowIndex}
                  width="100%"
                  on-input={val => {
                    row["tbl3月"] = val;
                  }}
                  on-change={val => {
                    this.txtMonthChanged(3, row);
                  }}
                  disabled={this.meisaiDisable}
                ></reafsinputnumber>
              </div>
            );
          }
        },

        {
          key: "15",
          field: "tbl年額",
          title: "年額",
          width: 150,
          align: "center"
        }
      ],

      tblIraiData: [
        {
          tbl費用負担部門コード: "",
          tbl費用負担部門名称: "",
          tbl税率: "",
          tbl4月: 0,
          tbl5月: 0,
          tbl6月: 0,
          tbl7月: 0,
          tbl8月: 0,
          tbl9月: 0,
          tbl10月: 0,
          tbl11月: 0,
          tbl12月: 0,
          tbl1月: 0,
          tbl2月: 0,
          tbl3月: 0,
          tbl年額: 0
        }
      ],

      putData: {
        会社コード: "",
        契約NO: "",
        履歴NO: "",
        契約NO枝番: "",
        相見積番号: "",
        明細NO: 0,
        WS00531_GridData: [
          {
            tbl按分サブNO: 0,
            tbl費用負担部門コード: "",
            tbl税率: 0,
            tbl4月: 0,
            tbl5月: 0,
            tbl6月: 0,
            tbl7月: 0,
            tbl8月: 0,
            tbl9月: 0,
            tbl10月: 0,
            tbl11月: 0,
            tbl12月: 0,
            tbl1月: 0,
            tbl2月: 0,
            tbl3月: 0,
            tbl年額: 0
          }
        ]
      },

      subParamsObj: {
        taisyou: "", // 承認種類　 　　　　 取引先検索：対象
        jigyousyoCd: "", // 事業所コード
        torType: ""
      },

      loading_更新: false,
      loading_閉じる: false,
      rowKey: "rowKey",
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
    console.log("定期見積部門按分設定画面");
    
    this.queryParams = {
      会社コード: this.userInfo.会社コード,
      契約NO: this.$route.query.契約NO,
      契約NO枝番: this.$route.query.契約NO枝番,
      履歴NO: this.$route.query.履歴NO,
      明細NO: this.$route.query.明細NO,
      相見積番号: this.$route.query.相見積番号,
      契約年度: this.$route.query.契約年度,
      修繕区分小: this.$route.query.修繕区分小,
      負担区分: this.$route.query.負担区分,
      進捗コード_本体: this.$route.query.進捗コード_本体,
      部門コード: this.$route.query.部門コード,
      部門名称: this.$route.query.部門名称
      // 作業名称: this.$route.query.作業名称
    };

    if (this.queryParams.進捗コード_本体 > 400) {
      this.meisaiDisable = true;
    } else {
      this.meisaiDisable = false;
    }
    console.log(this.queryParams.進捗コード_本体 + "meisaiDisable / " + this.meisaiDisable);

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

      let _this = this;

      this.http
        .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F019", {
          契約No: this.queryParams.契約NO,
          契約No枝番: this.queryParams.契約NO枝番,
          契約年度: this.queryParams.契約年度,
          相見積番号: this.queryParams.相見積番号,
          履歴no: this.queryParams.履歴NO,
          明細no: this.queryParams.明細NO
        })

        // 検索実行後処理
        .then(function(response) {
          // 明細部分は9行固定、行追加などは行なわない。
          _this.tblIraiData = response.data;

          _this.queryParams.作業名称 = response.data[0].作業名称;

          if (_this.tblIraiData.length < 9) {
            // 登録済みの按分データが1件も存在しない場合、親画面で設定されている費用負担部門コードをデフォルトの部門コードとして設定する
            if (response.data[0].tbl費用負担部門コード == null) {
              _this.tblIraiData.push(_this.defaultDataMeisai());

              _this.tblIraiData[0].tbl費用負担部門コード = _this.queryParams.部門コード;
              _this.tblIraiData[0].tbl費用負担部門名称 = _this.queryParams.部門名称;

              _this.tblIraiData[0].tbl税率 = _this.sansyoSubForm.tax;
            }

            for (let i = _this.tblIraiData.length; i < 9; i++) {
              _this.tblIraiData.push(_this.defaultDataMeisai());
            }
          }

          _this.sumCalcMeisai();
        });
    },

    async SearchAnbunData() {
      console.log("startSearchAnbunData");

      let _this = this;

      // 修繕区分小が直接か請負かで見るテーブルが変わる
      if (_this.queryParams.修繕区分小 == "08") {
        // 直接なら見るのは原価、F013
        await this.http
          .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F013個別", {
            契約No: this.queryParams.契約NO,
            契約No枝番: this.queryParams.契約NO枝番,
            契約年度: this.queryParams.契約年度,
            履歴no: this.queryParams.履歴NO,
            明細no: this.queryParams.明細NO
          })

          // 検索実行後処理
          .then(function(response) {
            _this.sansyoSubForm.tax = response.data[0].tax;
            _this.sansyoSubForm.kingaku04 = response.data[0].kingaku04;
            _this.sansyoSubForm.kingaku05 = response.data[0].kingaku05;
            _this.sansyoSubForm.kingaku06 = response.data[0].kingaku06;
            _this.sansyoSubForm.kingaku07 = response.data[0].kingaku07;
            _this.sansyoSubForm.kingaku08 = response.data[0].kingaku08;
            _this.sansyoSubForm.kingaku09 = response.data[0].kingaku09;
            _this.sansyoSubForm.kingaku10 = response.data[0].kingaku10;
            _this.sansyoSubForm.kingaku11 = response.data[0].kingaku11;
            _this.sansyoSubForm.kingaku12 = response.data[0].kingaku12;
            _this.sansyoSubForm.kingaku01 = response.data[0].kingaku01;
            _this.sansyoSubForm.kingaku02 = response.data[0].kingaku02;
            _this.sansyoSubForm.kingaku03 = response.data[0].kingaku03;
            _this.sansyoSubForm.kingakuSum = response.data[0].kingakuSum;
          });
      } else if (_this.queryParams.修繕区分小 == "09") {
        // 請負なら見るのは売価、F012
        await this.http
          .get("/api/Reafs_W/Shonin/WD00530/GetPageData_F012個別", {
            契約No: this.queryParams.契約NO,
            契約No枝番: this.queryParams.契約NO枝番,
            契約年度: this.queryParams.契約年度,
            履歴no: this.queryParams.履歴NO,
            明細no: this.queryParams.明細NO
          })

          // 検索実行後処理
          .then(function(response) {
            _this.sansyoSubForm.tax = response.data[0].tax;
            _this.sansyoSubForm.kingaku04 = response.data[0].kingaku04;
            _this.sansyoSubForm.kingaku05 = response.data[0].kingaku05;
            _this.sansyoSubForm.kingaku06 = response.data[0].kingaku06;
            _this.sansyoSubForm.kingaku07 = response.data[0].kingaku07;
            _this.sansyoSubForm.kingaku08 = response.data[0].kingaku08;
            _this.sansyoSubForm.kingaku09 = response.data[0].kingaku09;
            _this.sansyoSubForm.kingaku10 = response.data[0].kingaku10;
            _this.sansyoSubForm.kingaku11 = response.data[0].kingaku11;
            _this.sansyoSubForm.kingaku12 = response.data[0].kingaku12;
            _this.sansyoSubForm.kingaku01 = response.data[0].kingaku01;
            _this.sansyoSubForm.kingaku02 = response.data[0].kingaku02;
            _this.sansyoSubForm.kingaku03 = response.data[0].kingaku03;
            _this.sansyoSubForm.kingakuSum = response.data[0].kingakuSum;
          });
      }
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

      if (this.syoukeiForm.kingakuSum != this.sansyoSubForm.kingakuSum) {
        // 入力金額合計と按分元合計金額が異なる場合はNGとする
        this.fnc_void_showInfoMsg("E040776");

        return false;
      }

      for (let i = 0; i < this.tblIraiData.length; i++) {
        if (this.tblIraiData[i].tbl費用負担部門コード.length != 0) {
          // 費用負担部門が設定されていたら登録対象データ

          torokuGridDate.push(this.tblIraiData[i]);
          torokuGridDate[i].tbl按分サブNO = subNumber;
          subNumber += 1;
        } else {
          if (
            this.tblIraiData[i].tbl4月 != 0 ||
            this.tblIraiData[i].tbl5月 != 0 ||
            this.tblIraiData[i].tbl6月 != 0 ||
            this.tblIraiData[i].tbl7月 != 0 ||
            this.tblIraiData[i].tbl8月 != 0 ||
            this.tblIraiData[i].tbl9月 != 0 ||
            this.tblIraiData[i].tbl10月 != 0 ||
            this.tblIraiData[i].tbl11月 != 0 ||
            this.tblIraiData[i].tbl12月 != 0 ||
            this.tblIraiData[i].tbl1月 != 0 ||
            this.tblIraiData[i].tbl2月 != 0 ||
            this.tblIraiData[i].tbl3月 != 0
          ) {
            // 費用負担部門が未設定で、按分金額が入っていたらNGとする
            this.fnc_void_showInfoMsg("E040777");

            return false;
          }
        }
      }

      if (torokuGridDate.length <= 1) {
        this.fnc_void_showInfoMsg("E040787"); // 費用負担部門を複数設定して下さい。

        return false;
      }

      console.log(torokuGridDate);
      this.putData.WS00531_GridData = torokuGridDate;
      this.putData.会社コード = this.queryParams.会社コード;
      this.putData.契約NO = this.queryParams.契約NO;
      this.putData.履歴NO = this.queryParams.履歴NO;
      this.putData.契約NO枝番 = this.queryParams.契約NO枝番;
      this.putData.相見積番号 = this.queryParams.相見積番号;
      this.putData.明細NO = this.queryParams.明細NO;
      console.log(this.putData.WS00531_GridData);

      return true;
    },

    fnc_void_showTourokuMsg() {
      this.fnc_rtn_getMsg("K000004").then(response => {
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
          "/api/Reafs_W/Shonin/WD00530/WD00531_Put_AnbunData",
          letval_touroku,
          "登録しています...."
        )
        .then(function(response) {
          if ((response.status = true)) {
            _this.fnc_void_showInfoMsg("M000008");
            // 再取得
            _this.getPageData();
          }
        });
    },

    onChangeHiyouHutanBumon(value, rowIndex) {
      console.log("費用負担部門変更時処理");

      let _this = this;

      if (value == "") {
        _this.tblIraiData[rowIndex].tbl費用負担部門名称 = "";
        _this.tblIraiData[rowIndex].tbl税率 = "";
      } else {
        this.http
          .get("/api/Reafs_W/Shonin/WD00520/Get名称取得方法No34", {
            費用負担部門コード: value
          })
          .then(response => {
            if (response.data !== null) {
              _this.tblIraiData[rowIndex].tbl費用負担部門名称 =
                response.data.費用負担部門;
              _this.tblIraiData[rowIndex].tbl税率 = _this.sansyoSubForm.tax;
            } else {
              _this
                .fnc_rtn_getMsg("E040747")
                .then(response => {
                  _this.fnc_void_showError(response.data, () => {
                    _this.$refs[`ref費用負担部門${Index}`].focus();
                  });
                })
                .finally(() => {});
              _this.tblIraiData[rowIndex].tbl費用負担部門名称 = "";
              _this.tblIraiData[rowIndex].tbl税率 = "";
            }
          });
      }
    },

    reafsmodalBack_searchBukken(obj) {
      console.log("費用負担部門サブ画面使用時値セット処理");
      this.tblIraiData.tbl費用負担部門コード = obj.txtCd;
      this.tblIraiData.tbl費用負担部門名称 = obj.lblNm;
    },

    txtMonthChanged(month, row) {
      console.log(month + "月の値が変わりました");

      // 列合計計算
      let sumMonth = 0;
      for (let i = 0; i < this.tblIraiData.length; i++) {
        if (month == 4) {
          sumMonth += Number(
            this.tblIraiData[i].tbl4月.toString().replaceAll(",", "")
          );
        } else if (month == 5) {
          sumMonth += Number(
            this.tblIraiData[i].tbl5月.toString().replaceAll(",", "")
          );
        } else if (month == 6) {
          sumMonth += Number(
            this.tblIraiData[i].tbl6月.toString().replaceAll(",", "")
          );
        } else if (month == 7) {
          sumMonth += Number(
            this.tblIraiData[i].tbl7月.toString().replaceAll(",", "")
          );
        } else if (month == 8) {
          sumMonth += Number(
            this.tblIraiData[i].tbl8月.toString().replaceAll(",", "")
          );
        } else if (month == 9) {
          sumMonth += Number(
            this.tblIraiData[i].tbl9月.toString().replaceAll(",", "")
          );
        } else if (month == 10) {
          sumMonth += Number(
            this.tblIraiData[i].tbl10月.toString().replaceAll(",", "")
          );
        } else if (month == 11) {
          sumMonth += Number(
            this.tblIraiData[i].tbl11月.toString().replaceAll(",", "")
          );
        } else if (month == 12) {
          sumMonth += Number(
            this.tblIraiData[i].tbl12月.toString().replaceAll(",", "")
          );
        } else if (month == 1) {
          sumMonth += Number(
            this.tblIraiData[i].tbl1月.toString().replaceAll(",", "")
          );
        } else if (month == 2) {
          sumMonth += Number(
            this.tblIraiData[i].tbl2月.toString().replaceAll(",", "")
          );
        } else if (month == 3) {
          sumMonth += Number(
            this.tblIraiData[i].tbl3月.toString().replaceAll(",", "")
          );
        }
      }

      // 対象月の合計が按分元金額より大きい場合、エラーとする
      if (month == 4) {
        if (
          Number(this.sansyoSubForm.kingaku04.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl4月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 5) {
        if (
          Number(this.sansyoSubForm.kingaku05.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl5月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 6) {
        if (
          Number(this.sansyoSubForm.kingaku06.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl6月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 7) {
        if (
          Number(this.sansyoSubForm.kingaku07.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl7月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 8) {
        if (
          Number(this.sansyoSubForm.kingaku08.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl8月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 9) {
        if (
          Number(this.sansyoSubForm.kingaku09.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl9月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 10) {
        if (
          Number(this.sansyoSubForm.kingaku10.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl10月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 11) {
        if (
          Number(this.sansyoSubForm.kingaku11.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl11月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 12) {
        if (
          Number(this.sansyoSubForm.kingaku12.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl12月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 1) {
        if (
          Number(this.sansyoSubForm.kingaku01.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl1月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 2) {
        if (
          Number(this.sansyoSubForm.kingaku02.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl2月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      } else if (month == 3) {
        if (
          Number(this.sansyoSubForm.kingaku03.toString().replaceAll(",", "")) <
          sumMonth
        ) {
          row.tbl3月 = 0;
          this.fnc_void_showInfoMsg("E040776"); // 入力合計額と按分元金額が一致していません。

          return;
        }
      }

      // 合計した結果を対象月の合計欄にセット
      if (month == 4) {
        console.log("4月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku04 = sumMonth.toLocaleString();
      } else if (month == 5) {
        console.log("5月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku05 = sumMonth.toLocaleString();
      } else if (month == 6) {
        console.log("6月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku06 = sumMonth.toLocaleString();
      } else if (month == 7) {
        console.log("7月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku07 = sumMonth.toLocaleString();
      } else if (month == 8) {
        console.log("8月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku08 = sumMonth.toLocaleString();
      } else if (month == 9) {
        console.log("9月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku09 = sumMonth.toLocaleString();
      } else if (month == 10) {
        console.log("10月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku10 = sumMonth.toLocaleString();
      } else if (month == 11) {
        console.log("11月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku11 = sumMonth.toLocaleString();
      } else if (month == 12) {
        console.log("12月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku12 = sumMonth.toLocaleString();
      } else if (month == 1) {
        console.log("1月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku01 = sumMonth.toLocaleString();
      } else if (month == 2) {
        console.log("2月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku02 = sumMonth.toLocaleString();
      } else if (month == 3) {
        console.log("3月列合計:" + sumMonth.toLocaleString());
        this.syoukeiForm.kingaku03 = sumMonth.toLocaleString();
      }

      // 対象月の合計が変わったからそれに合わせて総合計も再計算
      this.syoukeiForm.kingakuSum =
        Number(this.syoukeiForm.kingaku04.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku05.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku06.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku07.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku08.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku09.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku10.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku11.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku12.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku01.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku02.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku03.toString().replaceAll(",", ""));

      // 行合計計算
      let sumYear =
        Number(row.tbl4月.toString().replaceAll(",", "")) +
        Number(row.tbl5月.toString().replaceAll(",", "")) +
        Number(row.tbl6月.toString().replaceAll(",", "")) +
        Number(row.tbl7月.toString().replaceAll(",", "")) +
        Number(row.tbl8月.toString().replaceAll(",", "")) +
        Number(row.tbl9月.toString().replaceAll(",", "")) +
        Number(row.tbl10月.toString().replaceAll(",", "")) +
        Number(row.tbl11月.toString().replaceAll(",", "")) +
        Number(row.tbl12月.toString().replaceAll(",", "")) +
        Number(row.tbl1月.toString().replaceAll(",", "")) +
        Number(row.tbl2月.toString().replaceAll(",", "")) +
        Number(row.tbl3月.toString().replaceAll(",", ""));
      row.tbl年額 = sumYear.toLocaleString();
    },

    sumCalcMeisai() {
      console.log("全体再計算実行");

      let sum4 = 0;
      let sum5 = 0;
      let sum6 = 0;
      let sum7 = 0;
      let sum8 = 0;
      let sum9 = 0;
      let sum10 = 0;
      let sum11 = 0;
      let sum12 = 0;
      let sum1 = 0;
      let sum2 = 0;
      let sum3 = 0;

      for (let i = 0; i < this.tblIraiData.length; i++) {
        sum4 += Number(
          this.tblIraiData[i].tbl4月.toString().replaceAll(",", "")
        );
        sum5 += Number(
          this.tblIraiData[i].tbl5月.toString().replaceAll(",", "")
        );
        sum6 += Number(
          this.tblIraiData[i].tbl6月.toString().replaceAll(",", "")
        );
        sum7 += Number(
          this.tblIraiData[i].tbl7月.toString().replaceAll(",", "")
        );
        sum8 += Number(
          this.tblIraiData[i].tbl8月.toString().replaceAll(",", "")
        );
        sum9 += Number(
          this.tblIraiData[i].tbl9月.toString().replaceAll(",", "")
        );
        sum10 += Number(
          this.tblIraiData[i].tbl10月.toString().replaceAll(",", "")
        );
        sum11 += Number(
          this.tblIraiData[i].tbl11月.toString().replaceAll(",", "")
        );
        sum12 += Number(
          this.tblIraiData[i].tbl12月.toString().replaceAll(",", "")
        );
        sum1 += Number(
          this.tblIraiData[i].tbl1月.toString().replaceAll(",", "")
        );
        sum2 += Number(
          this.tblIraiData[i].tbl2月.toString().replaceAll(",", "")
        );
        sum3 += Number(
          this.tblIraiData[i].tbl3月.toString().replaceAll(",", "")
        );
      }

      this.syoukeiForm.kingaku04 = sum4;
      this.syoukeiForm.kingaku05 = sum5;
      this.syoukeiForm.kingaku06 = sum6;
      this.syoukeiForm.kingaku07 = sum7;
      this.syoukeiForm.kingaku08 = sum8;
      this.syoukeiForm.kingaku09 = sum9;
      this.syoukeiForm.kingaku10 = sum10;
      this.syoukeiForm.kingaku11 = sum11;
      this.syoukeiForm.kingaku12 = sum12;
      this.syoukeiForm.kingaku01 = sum1;
      this.syoukeiForm.kingaku02 = sum2;
      this.syoukeiForm.kingaku03 = sum3;

      this.syoukeiForm.kingakuSum =
        Number(this.syoukeiForm.kingaku04.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku05.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku06.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku07.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku08.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku09.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku10.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku11.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku12.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku01.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku02.toString().replaceAll(",", "")) +
        Number(this.syoukeiForm.kingaku03.toString().replaceAll(",", ""));
    },

    defaultDataMeisai() {
      return {
        tbl費用負担部門コード: "",
        tbl費用負担部門名称: "",
        tbl税率: "",
        tbl4月: 0,
        tbl5月: 0,
        tbl6月: 0,
        tbl7月: 0,
        tbl8月: 0,
        tbl9月: 0,
        tbl10月: 0,
        tbl11月: 0,
        tbl12月: 0,
        tbl1月: 0,
        tbl2月: 0,
        tbl3月: 0,
        tbl年額: 0
      };
    },

    onShowAnbunDialog(index) {
      console.log("費用負担部門選択サブ画面が開かれたよ");

      this.rowIndex = index;
      this.showDialog = true;
    },

    dataBack(obj) {
      console.log("費用負担部門選択サブ画面が閉じられたよ");

      const rowIndex = this.rowIndex;

      this.tblIraiData[rowIndex].tbl費用負担部門コード = obj.txtCd;
      this.tblIraiData[rowIndex].tbl費用負担部門名称 = obj.lblNm;
      this.tblIraiData[rowIndex].tbl税率 = this.sansyoSubForm.tax;
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

    fnc_void_showError(
      message,
      option = null,
      title = "依頼参照・承認（実施判断）"
    ) {
      var self = this;
      this.MsgErr({
        title,
        message,
        callback: function(a, b) {
          self.hideLoading();
          if (option && typeof option.callback === "function") {
            option.callback();
          }
        }
      });
    },

    ///////////////////////////////////////////////
    // keyからS016メッセージマスタにアクセスしメッセージ取得
    ///////////////////////////////////////////////
    fnc_rtn_getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },

    replaceAll(val) {
      return ((val || "") + "").replaceAll(",", "");
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
  float: right;
  margin: 0 0 0 80%;
}

.tblMeisai {
  width: 2355px;
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

.IraiSansyoSub >>> ::-webkit-scrollbar {
  background-color: rgba(0, 0, 0, 0.1);
  width: 10px;
}

.IraiSansyoSub >>> ::-webkit-scrollbar-track {
  background-color: rgb(255, 255, 255);
  width: 1px;
}

.IraiSansyoSub >>> ::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.3);
  width: 1px;
}
</style>
