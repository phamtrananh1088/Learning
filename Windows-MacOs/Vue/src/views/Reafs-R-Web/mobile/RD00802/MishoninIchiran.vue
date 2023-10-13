<template>
  <div class="home-contianer">
    <!-- 検索部 -->
    <el-form
      :model="searchForm"
      :rules="rules"
      ref="searchForm"
      :label-position="isMobileSized ? 'top' : 'left'"
    >
      <el-row>
        <el-col :span="21" :xs="24">
          <!-- layout  xs:<768px sm:>=768px md:>=992px lg:>=1200px xl:>=1920px-->
          <el-col :span="24" :xs="24">
            <el-form-item label="承認種類" >
              <el-radio-group
                v-model="searchForm.syouninSyuruiSelectVal"
                size="mini"
              >
                <el-radio
                  v-for="item in syouninSyuruiItems"
                  :key="item.名称コード"
                  :label="item.名称コード"
                >
                  {{ item.名称 }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="対象" >
              <el-col :md="18" :sm="18">
                <el-radio-group
                  v-model="searchForm.taisyouSelectVal"
                  size="mini"
                >
                  <el-radio label="1">承認待ち</el-radio>
                  <el-radio label="2">申請中</el-radio>
                  <el-radio label="3">否認</el-radio>
                  <el-radio label="4">差戻</el-radio>
                </el-radio-group>
                <el-col>
                  <label class="margin_right_60">履歴／ </label>
                  <el-radio-group
                    v-model="searchForm.taisyouSelectVal"
                    size="mini"
                  >
                    <el-radio label="5">承認済</el-radio>
                    <el-radio label="6">否認</el-radio>
                    <el-radio label="7">差戻</el-radio>
                    <el-radio label="8">取下</el-radio>
                  </el-radio-group>
                </el-col>
              </el-col>
            </el-form-item>
          </el-col>
        </el-col>
        <el-col :span="3" :xs="24">
          <el-form-item label-width="0px">
            <el-button type="primary" size="mini" @click="onSearchSubmit">
              &nbsp;検索&nbsp;
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" :xs="24">
          <label for="extension" key="extension" @click="onClickShowExtension">その他絞込み検索</label>
          <i :class="ShowExtensionClass" @click="onClickShowExtension"></i>
        </el-col>
      </el-row>
      <el-row v-show="this.ShowExtensionFlg">
        <el-col :span="24" :xs="24">
          <el-form-item label="依頼日">
            <!-- <nengetsu
              v-model="searchForm.iraiDate"
              type="daterange"
              format="yyyy/MM/dd"
              :width="'260px'"
              range-separator="~"
            ></nengetsu> -->
            <el-input
              v-model="searchForm.iraiDateStr"
              style="width: 140px"
              suffix-icon="el-icon-date"
              placeholder="依頼開始日"
              @click.native="showiraiDateStrPopup"
              readonly
            >
            </el-input>
            ~
            <el-input
              v-model="searchForm.iraiDateEnd"
              style="width: 140px"
              suffix-icon="el-icon-date"
              placeholder="依頼終了日"
              @click.native="showiraiDateEndPopup"
              readonly
            >
            </el-input>
            <van-popup 
              v-model="iraiDateStrshow"
              position="bottom">
              <van-datetime-picker
                v-model="searchForm.iraiDateStrSelect"
                type="date"
                title="依頼開始日"
                @confirm="confirmiraiDateStr"
                @cancel="canceliraiDateStr"
              />
            </van-popup>
            <van-popup 
              v-model="iraiDateEndshow"
              position="bottom">
              <van-datetime-picker
                v-model="searchForm.iraiDateEndSelect"
                type="date"
                title="依頼終了日"
                @confirm="confirmiraiDateEnd"
                @cancel="canceliraiDateEnd"
              />
            </van-popup>
          </el-form-item>

          <el-form-item label="依頼者" prop="iraiTantoiCd">
            <opensearch
              v-model="searchForm.iraiTantoiCd"
              :disabled="false"
              :appendlabelText="iraiTantoiNm"
              :append="'append'"
              :appendlabelWidth="'240px'"
              :maxlength="'7'"
              :path="'reafsmodalSearchShain'"
              :errorMessageCode = "'E040020'"
              :inputClass = "'moblie-input-append'"
              @backData="reafsmodalBack_searchIraiTanto"
            >
            </opensearch>
          </el-form-item>

          <el-form-item label="申請日">
            <!-- <nengetsu
              v-model="searchForm.sinseDate"
              type="daterange"
              format="yyyy/MM/dd"
              :width="'260px'"
              range-separator="~"
            ></nengetsu> -->
            <el-input
              v-model="searchForm.sinseDateStr"
              style="width: 140px"
              suffix-icon="el-icon-date"
              placeholder="申請開始日"
              @click.native="showsinseDateStrPopup"
              readonly
            >
            </el-input>
            ~
            <el-input
              v-model="searchForm.sinseDateEnd"
              style="width: 140px"
              suffix-icon="el-icon-date"
              placeholder="申請終了日"
              @click.native="showsinseDateEndPopup"
              readonly
            >
            </el-input>
            <van-popup 
              v-model="sinseDateStrshow"
              position="bottom">
              <van-datetime-picker
                v-model="searchForm.sinseDateStrSelect"
                type="date"
                title="申請開始日"
                @confirm="confirmsinseDateStr"
                @cancel="cancelsinseDateStr"
              />
            </van-popup>
            <van-popup 
              v-model="sinseDateEndshow"
              position="bottom">
              <van-datetime-picker
                v-model="searchForm.sinseDateEndSelect"
                type="date"
                title="申請終了日"
                @confirm="confirmsinseDateEnd"
                @cancel="cancelsinseDateEnd"
              />
            </van-popup>
          </el-form-item>

          <el-form-item label="申請者" prop="sinseTantoiCd">
            <opensearch
              v-model="searchForm.sinseTantoiCd"
              :disabled="false"
              :appendlabelText="sinseiTantoNm"
              :append="'append'"
              :appendlabelWidth="'240px'"
              :maxlength="'7'"
              :path="'reafsmodalSearchShain'"
              :errorMessageCode = "'E010353'"
              :inputClass = "'moblie-input-append'"
              @backData="reafsmodalBack_searchSinseiTanto"
            >
            </opensearch>
          </el-form-item>

          <el-form-item label="営業店" prop="eigyoten">
            <opensearch
              v-model="searchForm.eigyoten"
              width="100px"
              :disabled="false"
              :appendlabelText="eigyotenNm"
              :append="'append'"
              :appendlabelWidth="'240px'"
              :path="'reafsmodalSearchMise'"
              maxlength="7"
              :errorMessageCode = "'E040009'"
              :inputClass = "'moblie-input-append'"
              @backData="reafsmodalBack"
            >
            </opensearch>
          </el-form-item>

          <el-form-item label="物件">
            <opensearch
              v-model="searchForm.bukken"
              maxlength="8"
              :disabled="false"
              :appendlabelText="bukkenNm"
              :append="'append'"
              :appendlabelWidth="'240px'"
              :path="'reafsmodalSearchBukken'"
              :errorMessageCode = "'E040010'"
              :inputClass = "'moblie-input-append'"
              @backData="reafsmodalBack_searchBukken"
            >
            </opensearch>
          </el-form-item>

          <el-form-item label="依頼内容/状況" prop="iraiContent">
            <reafsinputtext
              v-model="searchForm.iraiContent"
              :width="'350px'"
              :placeholder="'部分一致検索'"
            ></reafsinputtext>
          </el-form-item>

          <el-form-item label="不具合場所" prop="fuguai">
            <reafsinputtext
              v-model="searchForm.fuguai"
              :width="'350px'"
              :placeholder="'部分一致検索'"
            ></reafsinputtext>
          </el-form-item>

          <el-form-item label="メーカー名" prop="mekaNm">
            <reafsinputtext
              v-model="searchForm.mekaNm"
              :width="'350px'"
              :placeholder="'部分一致検索'"
            ></reafsinputtext>
          </el-form-item>

          <el-form-item label="得意先" prop="tokuisakiCd">
            <opensearch
              v-model="searchForm.tokuisakiCd"
              :disabled="false"
              :appendlabelText="tokuisakiNm"
              :maxlength="'10'"
              :append="'append'"
              :width="'120px'"
              :appendlabelWidth="'230px'"
              :path="'reafsmodalSearchTokuisaki'"
              :subParamsObj="this.SetsubParamsObjTokuisaki()"
              :errorMessageCode = "'E040120'"
              :inputClass = "'moblie-input-append'"
              @backData="reafsmodalBack_Tokuisaki"
            >
            </opensearch>
          </el-form-item>

          <el-form-item label="取引先" prop="torihikisakiCd">
            <opensearch
              v-model="searchForm.torihikisakiCd"
              :disabled="false"
              :appendlabelText="torihikisakiNm"
              :maxlength="'10'"
              :append="'append'"
              :width="'120px'"
              :appendlabelWidth="'230px'"
              :path="'reafsmodalSearchTokuisaki'"
              :subParamsObj="this.SetsubParamsObjTorihikisaki()"
              :errorMessageCode = "'E040550'"
              :inputClass = "'moblie-input-append'"
              @backData="reafsmodalBack_Torihikisaki"
            >
            </opensearch>
          </el-form-item>

          <el-form-item label="依頼状況">
            <el-select
              v-model="searchForm.iraiJoukyou"
              style="width: 350px"
            >
              <el-option
                v-for="item in joukyouList"
                :key="item.名称コード"
                :value="item.名称コード"
                :label="item.名称コード + ' ' + item.名称"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="修繕区分大">
            <el-select
              v-model="searchForm.syuuzenKbnB"
              style="width: 350px"
              @change="onSyuuzenKbnBChange"
            >
              <el-option
                v-for="item in syuuzenKbnBList"
                :key="item.名称コード"
                :value="item.名称コード"
                :label="item.名称コード + ' ' + item.名称"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="修繕区分小">
            <el-select
              ref="syuuzenKbn"
              v-model="searchForm.syuuzenKbnS"
              style="width: 350px"
              :default-first-option="true"
            >
              <el-option
                v-for="item in syuuzenKbnSList"
                :key="item.名称コード"
                :value="item.名称コード"
                :label="item.名称コード + ' ' + item.名称"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="負担区分">
            <el-select
              v-model="searchForm.futanKbn"
              style="width: 350px"
            >
              <el-option
                v-for="item in futanKbnList"
                :key="item.名称コード"
                :value="item.名称コード"
                :label="item.名称コード + ' ' + item.名称"
              >
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="承認日">
            <!-- <nengetsu
              v-model="searchForm.syouninDate"
              type="daterange"
              format="yyyy/MM/dd"
              :width="'260px'"
              range-separator="~"
            ></nengetsu> -->
            <el-input
              v-model="searchForm.syouninDateStr"
              style="width: 140px"
              suffix-icon="el-icon-date"
              placeholder="承認開始日"
              @click.native="showsyouninDateStrPopup"
              readonly
            >
            </el-input>
            ~
            <el-input
              v-model="searchForm.syouninDateEnd"
              style="width: 140px"
              suffix-icon="el-icon-date"
              placeholder="承認終了日"
              @click.native="showsyouninDateEndPopup"
              readonly
            >
            </el-input>
            <van-popup 
              v-model="syouninDateStrshow"
              position="bottom">
              <van-datetime-picker
                v-model="searchForm.syouninDateStrSelect"
                type="date"
                title="承認開始日"
                @confirm="confirmsyouninDateStr"
                @cancel="cancelsyouninDateStr"
              />
            </van-popup>
            <van-popup 
              v-model="syouninDateEndshow"
              position="bottom">
              <van-datetime-picker
                v-model="searchForm.syouninDateEndSelect"
                type="date"
                title="承認終了日"
                @confirm="confirmsyouninDateEnd"
                @cancel="cancelsyouninDateEnd"
              />
            </van-popup>
          </el-form-item>

          <el-form-item label="承認者" prop="syouninTantoiCd">
            <opensearch
              v-model="searchForm.syouninTantoiCd"
              :disabled="false"
              :appendlabelText="syouninTantoiNm"
              :append="'append'"
              :appendlabelWidth="'240px'"
              maxlength="7"
              :path="'reafsmodalSearchShain'"
              :inputClass = "'moblie-input-append'"
              @backData="reafsmodalBack_searchSyouninTantoi"
            >
            </opensearch>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-table
          class="v-table"
          :row-class-name="rowClassFunction"
          v-loading="loading2"
          border
          :height="444"
          :data="
            meisaiData
              ? meisaiData.slice(
                  (pagination.page - 1) * pagination.size,
                  pagination.page * pagination.size
                )
              : []
          "
        >
          <!-- style="width: 370px" -->
          <el-table-column type="expand" width="30">
            <template slot-scope="props">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="依頼日">
                    <span>{{ props.row.依頼日 }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="依頼者">
                    <span>{{ props.row.依頼者 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="申請者営業所">
                    <span>{{ props.row.申請者営業所 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="申請日">
                    <span>{{ props.row.申請日 }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="申請者">
                    <span>{{ props.row.申請者 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="物件">
                    <span>{{ props.row.物件名 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="依頼内容／状況">
                    <span>{{ props.row.依頼内容_状況 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="不具合場所">
                    <span>{{ props.row.不具合場所 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="得意先">
                    <span>{{ props.row.得意先名 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="メーカー名">
                    <span>{{ props.row.メーカー名 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="取引先">
                    <span>{{ props.row.取引先名 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="依頼状況">
                    <span>{{ props.row.依頼状況 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="修区大">
                    <span>{{ props.row.修繕区分大名 }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="修区小">
                    <span>{{ props.row.修繕区分小名 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="負担区分">
                    <span>{{ props.row.負担区分 }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="ステータス">
                    <span>{{ props.row.ステータス }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="承認日">
                    <span>{{ props.row.承認日 }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="承認者">
                    <span>{{ props.row.承認者 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="24">
                  <el-form-item label="否認差戻事由">
                    <span>{{ props.row.否認差戻事由 }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </template>
          </el-table-column>
          <!-- <el-table-column label="№" prop="行NO" width="40"> </el-table-column> -->
          <el-table-column label="依頼日" prop="依頼日" align="center" width="90">
          </el-table-column>
           <el-table-column :label="'物件名'" prop="物件名" width="100">
          </el-table-column>
          <!-- <el-table-column label="依頼者" prop="依頼者"> </el-table-column>
          <el-table-column label="申請日" prop="申請日" align="center">
          </el-table-column>
          <el-table-column label="申請者" prop="申請者"> </el-table-column> -->
          <el-table-column :label="'依頼内容／状況'" prop="依頼内容_状況">
            <template slot-scope="scope">
              <div>
                <a
                  href="javascript:void(0)"
                  @click="onHandleClick(scope.row)"
                  v-text="scope.row.依頼内容_状況 == undefined || scope.row.依頼内容_状況 == ''?'未設定':scope.row.依頼内容_状況"
                ></a>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <div class="block pagination">
          <el-pagination
            :class="isMobileSized ? 'blockcss' : 'inlinecss'"
            small
            ref="table2"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="this.pagination.page"
            :page-sizes="this.pagination.sizes"
            :page-size="this.pagination.size"
            layout="total, prev, pager, next, jumper"
            :total="this.pagination.total"
          ></el-pagination>
        </div>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnumber from "@/components/basic/ReafsControl/ReafsInputNumber.vue";
import reafsmodal from "@/components/modal/Modal.vue";
import eigyoten from "@/components/basic/EigyoTen.vue";
import opensearch from "@/components/basic/openSearch.vue";
import nengetsu from "@/components/basic/Nengetsu.vue";

// require('../../mock')
var $thisValue;
const collapseWidth = 768;

export default {
  components: {
    reafsinputtext,
    reafsinputnumber,
    reafsmodal,
    eigyoten,
    opensearch,
    nengetsu,
  },
  props: {
    showNo: {
      type: Boolean,
      default: true,
    },
    showCheck: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      iraiDateStrshow: false,
      iraiDateEndshow: false,
      sinseDateStrshow: false,
      sinseDateEndshow: false,
      syouninDateStrshow: false,
      syouninDateEndshow: false,
      searchForm: {
        syouninSyuruiSelectVal: "11", // 承認種類
        taisyouSelectVal: "1", // 対象
        txtCd: "",
        lblNm: "",
        row: [],
        // 依頼項目
        iraiDateStr: "", // 依頼日 開始日
        iraiDateEnd: "", // 依頼日 終了日
        iraiDateStrSelect: new Date(), // 依頼日 開始日
        iraiDateEndSelect: new Date(), // 依頼日 終了日
        iraiTantoiCd: "", // 依頼者
        iraiContent: "", // 依頼内容
        iraiJoukyou: "", // 依頼状況
        // 申請項目
        sinseDateStr: "", // 申請日 開始日
        sinseDateEnd: "", // 申請日 開始日
        sinseDateStrSelect: new Date(), // 申請日 開始日
        sinseDateEndSelect: new Date(), // 申請日 終了日
        sinseTantoiCd: "", // 申請者
        // 承認項目
        syouninDate: "", // 承認日
        syouninDateStr: "", // 承認日 開始日
        syouninDateEnd: "", // 承認日 終了日
        syouninDateStrSelect: new Date(), // 申請日 開始日
        syouninDateEndSelect: new Date(), // 申請日 終了日
        syouninTantoiCd: "", // 承認者

        tokuisakiCd: "", // 得意先
        torihikisakiCd: "", // 取引先
        bukken: "", // 物件
        bukkenNm: "", // 物件名
        bukkenKBN: "", // 物件区分
        eigyoten: "", // 営業店
        fuguai: "", // 不具合場所
        mekaNm: "", // メーカー名

        syuuzenKbnB: "", // 修繕区分大
        syuuzenKbnS: "", // 修繕区分小
        futanKbn: "", // 負担区分
      },
      // サブ画面の引数
      subParamsObj: {
        taisyou: "1", //承認種類　 取引先検索：対象
        jigyousyoCd: "110", //事業所コード
        torType: "", //null or 0:取引先 1:得意先
      },

      // 組織サブ2
      // 戻り値：「事業所コード」・「事業所名」・「営業所コード」・「営業所名」・
      //        「部コード」・「課コード」・「係コード」・
      // 　　　　「組織名」・「部門名」
      soshiki2Obj: {
        jigyousyoCd: "", //事業所コード
        jigyousyoNm: "", //事業所名
        eigyousyoCd: "", //営業所コード
        eigyousyoNm: "", //営業所名
        buCd: "", //部コード
        keCd: "", //課コード
        xiCd: "", //係コード
        soshikiNm: "", //組織名
        buNm: "", //部門名
        bukexiCd: "", //部課係
      },
      // 得意先
      tokuisakiNm: "", //得意先
      tokuisakiYuubin: "", //郵便番号
      tokuisakiJuusyo: "", //住所
      //  取引先
      torihikisakiNm: "", //取引先名
      torihikisakiYuubin: "", //郵便番号
      torihikisakiJuusyo: "", //住所

      syouninTantoiNm: "", // 依頼者名
      iraiTantoiNm: "", // 依頼者名
      sinseiTantoNm: "", // 申請者
      bukkenNm: "", // 物件名

      syouninSyuruiItems: [], // 承認種類 選択項目
      joukyouList: [], // 依頼状況 選択項目
      syuuzenKbnBList: [], // 修繕区分大 選択項目
      syuuzenKbnSList: [], // 修繕区分小 選択項目
      futanKbnList: [], // 負担区分 選択項目

      ShowExtensionFlg: false, // その他絞込み検索 表示フラグ
      ShowExtensionClass: "el-icon-caret-right", //
      colomsMod: [],
      rules: {
        eigyoten: [
          // { required: true, message: '入力してください', trigger: 'blur' },
        ],
        koujiiraiNo: [
          {
            type: "number",
            message: "数値を入力してください",
            trigger: "blur",
          },
        ],
      },
      meisaiData: [], // 明細データ
      eigyotenNm: "", // 営業店名
      labelText: "",
      pagination: { total: 0, size: 10, sortName: "", page: 1 },

      url: "api/searchSubmit",
      loading2: false,
      isMobileSized: false,
    };
  },
  created() {
    this.init();
  },
  mounted: function () {
    this.isMobile();
  },
  watch: {
    $route(to, from) {
      if (to.name == "RD00802") {
        this.init();
      }
    },
  },
  methods: {
    init() {
      $thisValue = this;
      
      // 「承認種類」ラジオボタン 選択項目取得
      this.http
        .post("api/Reafs_W/Master/selectM018", {
          strParam1: "053",
          strParam2: "11,12",
          intParam1: 0,
        })
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          $thisValue.syouninSyuruiItems = response.data
        });

      // 「依頼状況」ドロップダウンリスト　選択項目取得
      this.http
        .post("api/Reafs_W/Master/selectM018", { strParam1: "007" })
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          $thisValue.joukyouList = response.data;
          this.joukyouList.unshift({
            名称コード: "",
            名称: "－",
          });
        });

      // 「修繕区分大」ドロップダウンリスト 選択項目取得
      this.http
        .post("api/Reafs_W/Master/selectM018", { strParam1: "009" })
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          $thisValue.syuuzenKbnBList = response.data;
          this.syuuzenKbnBList.unshift({
            名称コード: "",
            名称: "－",
          });
        });

      // 「負担区分」ドロップダウンリスト 選択項目取得
      this.http
        .post("api/Reafs_W/Master/selectM018", { strParam1: "011" })
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          $thisValue.futanKbnList = response.data;
          this.futanKbnList.unshift({
            名称コード: "",
            名称: "－",
          });
        });

      // 「RD00801」お知らせ詳細から遷移した場合
      if (this.$route.params.linkType) {
        this.searchForm.syouninSyuruiSelectVal = this.$route.params.linkType;
        this.onSearchSubmit();
      }
    },
    isMobile() {
      let winWidth = window.innerWidth;
      if (winWidth < collapseWidth) {
        this.isMobileSized = true;
      } else {
        this.isMobileSized = false;
      }
    },
    handleSizeChange(val) {
      this.pagination.page = val;
    },
    handleCurrentChange(val) {
      this.pagination.page = val;
    },
    onHandleClick(row) {
      this.$router.push({
        name: "RD00803",
        params: {
          工事依頼NO: row.工事依頼NO,
          工事依頼NO枝番: row.工事依頼NO枝番,
          種類: row.種類,
        },
      });
    },
    // 検索
    onSearchSubmit() {
      $thisValue = this;

      let qs = require("qs");

      $thisValue.$refs.table ? ($thisValue.$refs.table.loading = true) : "";
      $thisValue.loading2 = true;
      $thisValue.pagination.page = 1;
      $thisValue.pagination.total = 0;
      $thisValue.meisaiData = undefined;

      this.http
        .post(
          "api/Reafs_R/Mobile/Shonin/getMeisaiListRD00802",
          qs.stringify(this.searchForm)
        )
        .then((result) => {
          if (!result.status) {
            $thisValue.$refs.table
              ? ($thisValue.$refs.table.loading = false)
              : "";
            $thisValue.loading2 = false;
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: result.message,
            });
          }

          $thisValue.meisaiData = result.data;
          if (result.data) {
            $thisValue.pagination.total = result.data.length;
          }

          $thisValue.$refs.table
            ? ($thisValue.$refs.table.loading = false)
            : "";
          $thisValue.loading2 = false;
        });

      this.isMobile();
    },
    onSyuuzenKbnBChange(obj) {
      // dropdown修繕区分小
      $thisValue.searchForm.syuuzenKbnS = "";
      this.http
        .post("api/Reafs_W/Master/selectM018", {
          strParam1: "010",
          strParam2: obj,
        })
        .then((response) => {
          if (!response.status) {
            return this.MsgInfo({
              title: this.$store.getters.getPageTitle(),
              message: response.message,
            });
          }
          $thisValue.syuuzenKbnSList = response.data;
          this.syuuzenKbnSList.unshift({
            名称コード: "",
            名称: "－",
          });
        });
    },
    // その他絞込み検索
    onClickShowExtension(key) {
      this.ShowExtensionFlg = !this.ShowExtensionFlg;
      if (this.ShowExtensionFlg) {
        this.ShowExtensionClass = "el-icon-caret-bottom";
      } else {
        this.ShowExtensionClass = "el-icon-caret-right";
      }
    },
    rowClassFunction(data) {
      try {
        if (
          data &&
          data.hasOwnProperty("row") &&
          data.row.hasOwnProperty("申請日")
        ) {
          let sinseiDate = Date.parse("20" + data.row.申請日);
          let todate = new Date();
          let day = (todate - sinseiDate) / (24 * 60 * 60 * 1000);
          if (day > 1) {
            return "table-row-class";
          }
        } else {
          return "";
        }
      } catch (error) {
        return "";
      }
    },
    formateDate(dDate) {
      if (!dDate) {
        return "";
      }
      let strTemp = dDate.toLocaleDateString().split("/");
      let mm = ("0" + strTemp[1])
        .split("")
        .reverse()
        .join("")
        .substr(0, 2)
        .split("")
        .reverse()
        .join("");
      let dd = ("0" + strTemp[2])
        .split("")
        .reverse()
        .join("")
        .substr(0, 2)
        .split("")
        .reverse()
        .join("");
      return strTemp[0] + "/" + mm + "/" + dd;
    },
    SetsubParamsObj(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj));
      tempSubParamsObj.torType = "";
      return tempSubParamsObj;
    },
    SetsubParamsObjTorihikisaki(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj))
      tempSubParamsObj.torType = ""
      tempSubParamsObj.taisyou = ""
      return tempSubParamsObj
    },
    SetsubParamsObjTokuisaki(obj) {
      let tempSubParamsObj = JSON.parse(JSON.stringify(this.subParamsObj))
      tempSubParamsObj.torType = "1"
      tempSubParamsObj.taisyou = ""
      return tempSubParamsObj
    },
    pageBack(obj) {
      this.pagination.page = obj;
    },
    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },
    //営業店　戻すデータ
    reafsmodalBack(obj) {
      this.searchForm.eigyoten = obj.txtCd;
      this.eigyotenNm = obj.lblNm;
    },
    // 依頼者　戻すデータ
    reafsmodalBack_searchIraiTanto(obj) {
      this.searchForm.iraiTantoiCd = obj.txtCd;
      this.iraiTantoiNm = obj.lblNm;
    },
    // 申請者　戻すデータ
    reafsmodalBack_searchSinseiTanto(obj) {
      this.searchForm.sinseTantoiCd = obj.txtCd;
      this.sinseiTantoNm = obj.lblNm;
    },
    // 承認者
    reafsmodalBack_searchSyouninTantoi(obj) {
      this.searchForm.syouninTantoiCd = obj.txtCd;
      this.syouninTantoiNm = obj.lblNm;
    },
    reafsmodalBack_searchBukken(obj) {
      this.searchForm.bukken = obj.txtCd;
      this.bukkenNm = obj.lblNm;
    },
    // 取引先　戻すデータ
    reafsmodalBack_Torihikisaki(obj) {
      // 戻り値：「取引先コード」「取引先名」「郵便番号」「住所」
      //  cd: row.取引先コード,
      //       nm: row.取引先名,
      //       yuubin: row.郵便番号,
      //       juusyo: row.住所,
      this.searchForm.torihikisakiCd = obj.txtCd;
      this.torihikisakiNm = obj.lblNm;
      this.torihikisakiYuubin = obj.row.郵便番号;
      this.torihikisakiJuusyo = obj.row.住所;
    },
    reafsmodalBack_Tokuisaki(obj) {
      this.searchForm.tokuisakiCd = obj.txtCd;
      this.tokuisakiNm = obj.lblNm;
      this.tokuisakiYuubin = obj.row.郵便番号;
      this.tokuisakiJuusyo = obj.row.住所;
    },
    //日付選択コントロール
    //依頼日選択
    showiraiDateStrPopup(){
      this.iraiDateStrshow = true;
    },
    showiraiDateEndPopup(){
      this.iraiDateEndshow = true;
    },
    confirmiraiDateStr(val){
      this.searchForm.iraiDateStr = this.formateDate(val);
      this.iraiDateStrshow = false;
    },
    confirmiraiDateEnd(val){
      this.searchForm.iraiDateEnd = this.formateDate(val);
      this.iraiDateEndshow = false;
    },
    canceliraiDateStr(){
      this.searchForm.iraiDateStr = "";
      this.iraiDateStrshow = false;
    },
    canceliraiDateEnd(){
      this.searchForm.iraiDateEnd = "";
      this.iraiDateEndshow = false;
    },
    //申請日選択
    showsinseDateStrPopup(){
      this.sinseDateStrshow = true;
    },
    showsinseDateEndPopup(){
      this.sinseDateEndshow = true;
    },
    confirmsinseDateStr(val){
      this.searchForm.sinseDateStr = this.formateDate(val);
      this.sinseDateStrshow = false;
    },
    confirmsinseDateEnd(val){
      this.searchForm.sinseDateEnd = this.formateDate(val);
      this.sinseDateEndshow = false;
    },
    cancelsinseDateStr(){
      this.searchForm.sinseDateStr = "";
      this.sinseDateStrshow = false;
    },
    cancelsinseDateEnd(){
      this.searchForm.sinseDateEnd = "";
      this.sinseDateEndshow = false;
    },
    //承認日選択
    showsyouninDateStrPopup(){
      this.syouninDateStrshow = true;
    },
    showsyouninDateEndPopup(){
      this.syouninDateEndshow = true;
    },
    confirmsyouninDateStr(val){
      this.searchForm.syouninDateStr = this.formateDate(val);
      this.syouninDateStrshow = false;
    },
    confirmsyouninDateEnd(val){
      this.searchForm.syouninDateEnd = this.formateDate(val);
      this.syouninDateEndshow = false;
    },
    cancelsyouninDateStr(){
      this.searchForm.syouninDateStr = "";
      this.syouninDateStrshow = false;
    },
    cancelsyouninDateEnd(){
      this.searchForm.syouninDateEnd = "";
      this.syouninDateEndshow = false;
    },
  },
};
</script>
<style lang="less" scoped>
.el-row {
  margin-bottom: 20px;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.home-contianer {
  margin-top: 20px;
  padding: 10px;
  // width: 375px;
  // width: 1024px; //830以上を設定するとTable明細は表示エラーがあった、原因は不明でした
}

.el-radio {
  margin-right: 0;
}
</style>
<style scoped>
.el-radio-group {
  height: 30px;
  padding-left: 2px;
  padding-top: 3px;
}
.el-form >>> .el-radio__label {
  padding-left: 0;
  padding-right: 10px;
}
.margin_right_60 {
  margin-right: calc(30px);
}
.v-table >>> .el-form-item__content {
  min-height: 30px;
}

.v-table >>> .el-table__header th {
  padding: 0px !important;
  background-color: #f8f8f9 !important;
  font-size: 13px;
  height: 42px;
  color: #616161;
}

.v-table >>> .el-table__header th.is-sortable {
  padding: 3px !important;
}

.el-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}
.inlinecss >>> .el-pagination__jump{
  display: inline !important;
}
.blockcss >>> .el-pagination__jump{
  display: block !important;
}
.v-table >>> .el-table__expanded-cell .el-form-item__label{
  width: 100%;
  border-right-color: white;
  border-right-style: solid;
  float: none;
  display: inline-block;
}
.v-table >>> .el-table__expanded-cell .el-row{
  margin-bottom: 0px;
}
.v-table >>> .table-row-class {
  background-color: rgb(248, 230, 230) !important;
  min-height: 32px;
}
.v-table >>> .table-row-class:hover td {
  background-color: rgb(248, 230, 230) !important;
}

</style>
