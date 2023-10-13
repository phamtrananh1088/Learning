<template>
    <div class="home-container IraiNaiyoSabu">
        <el-form
            :model="iraiNaiyoSabu"
            ref="refIraiForm"
            label-width="120px"
            class="sample-form-class"
            :hide-required-asterisk = "true">
            <el-container class="home-el-container">
                <el-main class="home-el-main">
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="営業店">
                                <div style="display: flex">
                                    <div>
                                        <reafsinputtext
                                        v-model="iraiNaiyoSabu.営業店コード"
                                        class="is-readonly"
                                        maxlength="6"
                                        disabled="disabled"
                                        :width="'95px'">
                                    </reafsinputtext>
                                    </div>
                                    <div class="marginLeft">
                                        <reafsinputtext
                                        class="is-readonly"
                                        :title="iraiNaiyoSabu.営業店名称"
                                        v-model="iraiNaiyoSabu.営業店名称"
                                        maxlength="20"
                                        disabled="disabled"
                                        :width="'100%'">
                                    </reafsinputtext>
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item label="物件">
                                <div style="display: flex">
                                    <div>
                                        <reafsinputtext
                                            class="is-readonly"
                                            v-model="iraiNaiyoSabu.物件コード"
                                            maxlength="8"
                                            disabled="disabled"
                                            :width="'95px'">
                                        </reafsinputtext>
                                    </div>
                                    <div class="marginLeft">
                                        <reafsinputtext
                                            :title="iraiNaiyoSabu.物件名"
                                            class="is-readonly"
                                            v-model="iraiNaiyoSabu.物件名"
                                            maxlength="50"
                                            disabled="disabled"
                                            :width="'100%'">
                                        </reafsinputtext>
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item label="工事件名">
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.工事件名"
                                    :title="iraiNaiyoSabu.工事件名"
                                    class="is-readonly"
                                    maxlength="60"
                                    disabled="disabled"
                                    :width="'343px'">
                                </reafsinputtext>
                            </el-form-item>
                        </el-col>
                        <el-col :span="14" class="marginLeft">
                            <div style="display: flex">
                                <el-form-item label="見積№">                                    
                                    <reafsinputtext
                                        v-model="iraiNaiyoSabu.見積No"
                                        class="is-readonly"
                                        maxlength="11"
                                        disabled="disabled"
                                        :width="'120px'">
                                    </reafsinputtext>                               
                                </el-form-item>
                                <el-form-item label="見積依頼日" class="marginLeft">
                                        <reafsinputtext
                                            v-model="iraiNaiyoSabu.見積依頼日"
                                            class="is-readonly"
                                            maxlength="10"
                                            disabled="disabled"
                                            :width="'120px'">
                                        </reafsinputtext>
                                </el-form-item>
                                <el-form-item label="工事予定日" class="marginLeft">
                                        <reafsinputtext
                                            v-model="iraiNaiyoSabu.工事予定日"
                                            class="is-readonly"
                                            maxlength="21"
                                            disabled="disabled"
                                            :width="'193px'">
                                        </reafsinputtext>
                                </el-form-item>      
                            </div>
                            <el-form-item label="物件住所">
                                <div style="display: flex">
                                    <div>
                                        <reafsinputtext
                                            v-model="iraiNaiyoSabu.物件住所郵便番号"
                                            class="is-readonly"
                                            maxlength="8"
                                            disabled="disabled"
                                            :width="'120px'">
                                        </reafsinputtext>
                                    </div>
                                    <div class="marginLeft">
                                        <reafsinputtext
                                            v-model="iraiNaiyoSabu.物件住所"
                                            class="is-readonly"
                                            maxlength="30"
                                            disabled="disabled"
                                            :width="'281%'">
                                        </reafsinputtext>
                                    </div>
                                </div>
                            </el-form-item>
                            <div style="margin-left:255px">
                                <el-form-item label="作業予定日" prop="作業予定日">
                                <div style="display: flex">
                                <div id="開始日1">
                                    <reafsinputtext
                                        v-model="iraiNaiyoSabu.開始日"
                                        class="is-readonly a"
                                        maxlength="30"
                                        disabled="disabled"
                                        :width="'110px'">
                                    </reafsinputtext>
                                </div>
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.開始時間"
                                    class="is-readonly"
                                    maxlength="30"
                                    disabled="disabled"
                                    :width="'70px'">
                                </reafsinputtext>
                                <span style="margin: 0 2px">～</span>
                                <div id="終了日1">
                                    <reafsinputtext
                                        v-model="iraiNaiyoSabu.終了日"
                                        class="is-readonly"
                                        maxlength="30"
                                        disabled="disabled"
                                        :width="'110px'">
                                    </reafsinputtext>
                                </div>
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.終了時間"
                                    class="is-readonly"
                                    maxlength="30"
                                    disabled="disabled"
                                    :width="'70px'">
                                </reafsinputtext>
                                <!-- <el-date-picker type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                                    v-model="iraiNaiyoSabu.開始日" class="date-picker" disabled/>
                                <el-time-picker type="time" format="HH:mm:ss" value-format="HH:mm:ss"
                                    v-model="iraiNaiyoSabu.開始時間" class="time-picker" disabled/>
                                <span style="margin: 0 2px">～</span>
                                <el-date-picker type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                                    v-model="iraiNaiyoSabu.終了日" class="date-picker" disabled/>
                                <el-time-picker type="time" format="HH:mm:ss" value-format="HH:mm:ss"
                                    v-model="iraiNaiyoSabu.終了時間" class="time-picker" disabled/> -->
                                </div>
                            </el-form-item>
                            </div>
                        </el-col>
                        <el-col :span="14">
                            <el-form-item label="工事区分" class="marginBottom">
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.工事区分"
                                    :title="iraiNaiyoSabu.工事区分"
                                    class="is-readonly"
                                    maxlength="40"
                                    disabled="disabled"
                                    :width="'35%'">
                                </reafsinputtext>
                            </el-form-item>
                            <el-form-item label="大区分" class="marginBottom">
                                <div id="大区分1">
                                    <reafsinputtext                                 
                                        v-model="iraiNaiyoSabu.大区分"
                                        :title="iraiNaiyoSabu.大区分"
                                        class="is-readonly"
                                        maxlength="40"
                                        disabled="disabled"
                                        :width="'35%'">
                                    </reafsinputtext>
                                </div>
                            </el-form-item>
                            <el-form-item label="中区分" class="marginBottom">
                                <div id="中区分1">
                                    <reafsinputtext
                                        v-model="iraiNaiyoSabu.中区分"
                                        :title="iraiNaiyoSabu.中区分"
                                        class="is-readonly"
                                        maxlength="40"
                                        disabled="disabled"
                                        :width="'35%'">
                                    </reafsinputtext>
                                </div>
                            </el-form-item>
                            <el-form-item label="小区分">
                                <div id="小区分1">
                                    <reafsinputtext
                                        v-model="iraiNaiyoSabu.小区分"
                                        :title="iraiNaiyoSabu.小区分"
                                        class="is-readonly"
                                        maxlength="40"
                                        disabled="disabled"
                                        :width="'35%'">
                                    </reafsinputtext>
                                </div>
                            </el-form-item>
                        </el-col>
                        <el-col :span="14">
                            <el-form-item label="不具合場所">
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.不具合場所"
                                    :title="iraiNaiyoSabu.不具合場所"
                                    class="is-readonly"
                                    maxlength="100"
                                    disabled="disabled"
                                    :width="'65%'">
                                </reafsinputtext>
                            </el-form-item>
                            <el-form-item label="メーカー名">
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.メーカー名"
                                    :title="iraiNaiyoSabu.メーカー名"
                                    class="is-readonly"
                                    maxlength="100"
                                    disabled="disabled"
                                    :width="'65%'">
                                </reafsinputtext>
                            </el-form-item>
                            <el-form-item class="is-readonly is-area" prop="依頼内容_状況"
                                label-width="0px"
                                >
                                    <div style="display: flex">
                                        <label
                                            class="el-form-item__label item_label_iraiNaiyo_fuguaiJokyo">
                                            依頼内容／<br />不具合状況</label>
                                        <div id="依頼内容_状況1" style="width:100%">
                                            <el-input
                                            readonly
                                            v-model="iraiNaiyoSabu.依頼内容_状況"
                                            tabindex="27"
                                            class="is-readonly item_input_iraiNaiyo_fuguaiJokyo"
                                            maxlength="400"
                                            type="textarea"
                                            resize="none"
                                            :rows="3"
                                            disabled="disabled"
                                            :width="'100%'"
                                        >
                                        </el-input>
                                        </div>
                                    </div>
                                </el-form-item>
                        </el-col>
                        <el-col>
                            <el-form-item label="作業依頼日" prop="作業依頼日">
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.作業依頼日"
                                    class="is-readonly"
                                    maxlength="30"
                                    disabled="disabled"
                                    :width="'140px'">
                                </reafsinputtext>
                                <!-- <el-date-picker type="date" format="yyyy/MM/dd" value-format="yyyy/MM/dd"
                                    v-model="iraiNaiyoSabu.作業依頼日" class="date-picker" disabled/> -->
                            </el-form-item>
                            <el-form-item label="依頼者">
                                <div style="display: flex">
                                    <div>
                                        <reafsinputtext
                                            v-model="iraiNaiyoSabu.依頼者コード"
                                            class="is-readonly"
                                            maxlength="7"
                                            disabled="disabled"
                                            :width="'100px'">
                                        </reafsinputtext>
                                    </div>
                                    <div class="marginLeft">
                                        <reafsinputtext
                                            :title="iraiNaiyoSabu.依頼者所属役職氏名"
                                            v-model="iraiNaiyoSabu.依頼者所属役職氏名"
                                            class="is-readonly"
                                            maxlength="100"
                                            disabled="disabled"
                                            :width="'250%'">
                                        </reafsinputtext>
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item label="連絡先">
                                <reafsinputtext
                                    v-model="iraiNaiyoSabu.連絡先"
                                    class="is-readonly"
                                    maxlength="20"
                                    disabled="disabled"
                                    :width="'16%'">
                                </reafsinputtext>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-main>
            </el-container>
        </el-form>
    </div>
</template>
<script>
import reafsinputtext from '@/components/basic/ReafsControl/ReafsInputText.vue'

export default{
  components: {
    reafsinputtext
  },
  data () {
    return {
      queryParams: {
        会社コード: '',
        工事依頼No: '',
        工事依頼No枝番: '',
        業者コード: '',
        業者コード枝番: ''
      },
      userInfo: {
        ユーザーＩＤ: '',
        会社コード: ''
      },
      iraiNaiyoSabu: {
        営業店コード: '',
        営業店名称: '',
        見積No: '',
        見積依頼日: '',
        工事予定日: '',
        物件コード: '',
        物件名: '',
        物件住所郵便番号: '',
        物件住所: '',
        工事件名: '',
        開始日: '',
        開始時間: '',
        終了日: '',
        終了時間: '',
        工事区分: '',
        大区分: '',
        中区分: '',
        小区分: '',
        不具合場所: '',
        メーカー名: '',
        依頼内容_状況: '',
        作業依頼日: '',
        依頼者コード: '',
        依頼者所属役職氏名: '',
        連絡先: ''
      }
    }
  },
  watch: {

  },
  computed: {

  },
  inject: ['hideLeftMenu'],
  created () {
    let userLogIn = this.$store.getters.getUserInfo()
    this.userInfo.ユーザーＩＤ = userLogIn.userId
    this.userInfo.会社コード = userLogIn.会社コード
  },
  mounted () {
    this.queryParams = {
      会社コード: this.userInfo.会社コード,
      工事依頼No: this.$route.query.工事依頼No,
      工事依頼No枝番: this.$route.query.工事依頼No枝番,
      業者コード: this.$route.query.業者コード,
      業者コード枝番: this.$route.query.業者コード枝番
    }
    this.hideLeftMenu(false)
    this.getPageData()
  },
  methods: {
    async getPageData () {
      await this.http
        .get('/api/Reafs_T/IraiNaiyoSabu/Get業者別見積マスタ',
          {
            会社コード: this.userInfo.会社コード,
            工事依頼No: this.queryParams.工事依頼No,
            工事依頼No枝番: this.queryParams.工事依頼No枝番,
            業者コード: this.queryParams.業者コード,
            業者コード枝番: this.queryParams.業者コード枝番
          })
        .then((response) => {
          this.iraiNaiyoSabu.営業店コード = response.data.営業店
          this.iraiNaiyoSabu.営業店名称 = response.data.営業店名称
          this.iraiNaiyoSabu.見積No = response.data.見積No
          this.iraiNaiyoSabu.見積依頼日 = response.data.見積依頼日
          this.iraiNaiyoSabu.工事予定日 = response.data.工事予定日
          this.iraiNaiyoSabu.物件コード = response.data.物件コード
          this.iraiNaiyoSabu.物件名 = response.data.物件名
          this.iraiNaiyoSabu.物件住所郵便番号 = response.data.物件住所郵便番号
          this.iraiNaiyoSabu.物件住所 = response.data.物件住所
          this.iraiNaiyoSabu.工事件名 = response.data.工事件名
          this.iraiNaiyoSabu.開始日 = response.data.開始日
          this.iraiNaiyoSabu.開始時間 = response.data.開始時間
          this.iraiNaiyoSabu.終了日 = response.data.終了日
          this.iraiNaiyoSabu.終了時間 = response.data.終了時間
          this.iraiNaiyoSabu.工事区分 = response.data.工事区分
          this.iraiNaiyoSabu.大区分 = response.data.大区分
          this.iraiNaiyoSabu.中区分 = response.data.中区分
          this.iraiNaiyoSabu.小区分 = response.data.小区分
          this.iraiNaiyoSabu.不具合場所 = response.data.不具合場所
          this.iraiNaiyoSabu.メーカー名 = response.data.メーカー名
          this.iraiNaiyoSabu.依頼内容_状況 = response.data.依頼内容_状況
          this.iraiNaiyoSabu.作業依頼日 = response.data.作業依頼日
          this.iraiNaiyoSabu.依頼者コード = response.data.依頼者コード
          this.iraiNaiyoSabu.依頼者所属役職氏名 = response.data.依頼者所属役職氏名
          this.iraiNaiyoSabu.連絡先 = response.data.連絡先
        })
    },
    async onBack () {
      window.parent.postMessage({
        'call': 'closePopup'
      }, '*')
    }
  }
}
document.getElementsByName('大区分')
</script>
<style>
    .IraiNaiyoSabu .marginLeft{
        margin-left: 15px;
    }
    .IraiNaiyoSabu .date-picker{
        width: 120px !important;
    }
    .IraiNaiyoSabu .time-picker{
        width: 95px !important;
    }
    .IraiNaiyoSabu .item_label {
    padding-left: 15px;
    max-width: 407px;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    }
    .IraiNaiyoSabu .item_label_iraiNaiyo_fuguaiJokyo {
    height: 75px !important;
    line-height: 30px;
    width: 145px !important;
    text-align: center;
    margin-top: 1px;
    padding-right: 0;
    vertical-align: middle;
    float: left;
    font-size: 14px;
    color: white;
    padding: 5px 0 0 0;
    box-sizing: border-box;
    }
    .IraiNaiyoSabu .fontSize12{
        font-size: 12px;
    }
    .IraiNaiyoSabu .right{
        text-align: right;
        margin-bottom: 20px;
    }
    .IraiNaiyoSabu .marginBottom{
        margin-bottom: 0px;
    }
    .IraiNaiyoSabu #大区分1 div div input{
        border-top: none !important;
    }
    .IraiNaiyoSabu #中区分1 div div input{
        border-top: none !important;
    }
    .IraiNaiyoSabu #小区分1 div div input{
        border-top: none !important;
    }
    .IraiNaiyoSabu .el-form .el-form-item__content{
        line-height:27px;
    }
    .IraiNaiyoSabu .el-form .el-form-item__label{
        margin-top: 0px !important;
    }
    .IraiNaiyoSabu .el-textarea.is-disabled .el-textarea__inner{
        color:black !important;
    }
    .IraiNaiyoSabu .header-collapse {
        display: none !important;
    }
    .IraiNaiyoSabu #開始日1 div div input {
        border-right: none !important;
    }
    .IraiNaiyoSabu #終了日1 div div input {
        border-right: none !important;
    }
    .IraiNaiyoSabu #依頼内容_状況1 div textarea{
        border-radius: 0;
    }
</style>
