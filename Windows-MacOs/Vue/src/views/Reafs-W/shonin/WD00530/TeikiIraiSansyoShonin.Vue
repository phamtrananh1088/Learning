<template>
  <div class="home-container TeikiIraiSansyoShonin">
    <el-form
      ref="refTeikiForm"
      label-width="120px"
      class="sample-form-class">
      <el-container class="home-el-container">
        <el-footer class="home-el-footer border-top">
          <el-row>
            <el-col>
              <el-button tabindex="37" @click="onBack" style="float: right">戻る</el-button>
            </el-col>
          </el-row>
        </el-footer>
      </el-container>
    </el-form>
  </div>
</template>

<script>
export default {
  data () {
    return {
      queryParams: {
        工事依頼No: '',
        発注サブNo: '',
        検収回数: '',
        tabFocus: ''
      },
      activeName: 'irai',
      oldSelectedTab: 'irai',
      sessionPageDataBack: null
    }
  },
  mounted () {
    this.queryParams = {
      工事依頼No: this.$route.query.工事依頼No,
      発注サブNo: this.$route.query.発注サブNo,
      検収回数: this.$route.query.検収回数,
      tabFocus: this.$route.query.tabFocus
    }

    if (this.queryParams.工事依頼No && this.queryParams.発注サブNo && this.queryParams.検収回数) {
      this.activeName = 'sagyo'
      this.oldSelectedTab = 'sagyo'
      // this.syuzenForm.工事依頼No = this.queryParams.工事依頼No
      // this.searchForm.koujiiraiNo = this.queryParams.工事依頼No.substring(0, 2)
      // this.searchForm.koujiiraiNo1 = this.queryParams.工事依頼No.substring(2)
      // this.sagyo発注サブNo = this.queryParams.発注サブNo
      // this.sagyo検収回数 = this.queryParams.検収回数
    } else if (this.queryParams.工事依頼No && this.queryParams.tabFocus === 'mitsu') {
      this.activeName = 'mitsu'
      this.oldSelectedTab = 'mitsu'
      // this.syuzenForm.工事依頼No = this.queryParams.工事依頼No
      // this.searchForm.koujiiraiNo = this.queryParams.工事依頼No.substring(0, 2)
      // this.searchForm.koujiiraiNo1 = this.queryParams.工事依頼No.substring(2)
    } else {
      this.activeName = 'irai'
      this.oldSelectedTab = 'irai'
    }
    // this.onSearch()
    // this.setPageTitle(this.menuOptions.SubMenuName)
  },
  methods: {
    async onBack () {
      if (this.activeName === 'sagyo') {
        if (this.queryParams.tabFocus === 'sagyo') {
          this.$router.push({ name: 'WD00510' })
        } else {
          this.$router.push({ name: 'WD00001' })
        }
      } else if (this.activeName === 'mitsu') {
        if (this.queryParams.tabFocus === 'mitsu') {
          this.$router.push({ name: 'WD00510' })
        } else {
          this.$router.push({ name: 'WD00001' })
        }
      } else {
        this.$router.push({ name: 'WD00510' })
      }
    }
  }
}
</script>
