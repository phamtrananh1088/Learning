<template>
  <el-dialog title="" :visible="dialogVisible" :show-close="false" class="KojiKubunSetsumeiIchiran dialog-class" @close="onClose()" :close-on-click-modal="false">
    <el-row class="img-content" v-loading="!isLoaded">
      <el-col class="img-content-col" :span="24">
        <el-row
          class="img-show border-bot border-right"
          ref="refImgContainer"
        >
          <img
            :src="linkImage"
            @load="onload"
            ref="refImg"
          />
        </el-row>
      </el-col>
    </el-row>
    <el-row style="margin-top:10px;">
      <el-col :span="18">
        <p/>
      </el-col>
      <el-col :span="6">
        <el-button @click="onClose" style="float: right">閉じる</el-button>
      </el-col>
    </el-row>
  </el-dialog>
</template>

<script>
export default {
  components: {
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      isLoaded: false,
      linkImage: ''
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    async init () {
      let result = {}
      await this.http
        .get('/api/Reafs_W/Irai/WD00110/GetQuestionBoxImage', 'アクセスしています....')
        .then(res => {
          result = res
        })
      if (result.status && result.data && result.data.m001Data) {
        result.data.m001Data.map((x) => {
          this.linkImage = this.http.ipAddress + x.linkImage
        })
      }
    },
    onload () {
      this.isLoaded = true
    },
    onClose () {
      this.$emit('update:dialogVisible', false)
    }
  }
}
</script>
<style lang="less" scoped>
.img-content {
  display: flex;
  flex-direction: row;
  height: calc(100% - 60px);
  text-align: center;
}
.img-show {
  // position: relative;
  width: 100%;//100%;
  height: 100%;//calc(100% - 10px);
  img {
    // position: absolute;
    top: 5px;
    left: 5px;
    bottom: 5px;
    right: 5px;
    max-width: 100%;
    max-height: 100%;
    object-fit: cover;
    margin: auto;
    background-color: white;
  }
  background-color: #f3f3f3;
}
</style>
<style scoped>
.el-dialog__wrapper.dialog-class >>> .el-dialog {
  width: 1130px;
  height: 760px;
  overflow: overlay;
  max-width: -webkit-fill-available;
  max-height: -webkit-fill-available;
}
.KojiKubunSetsumeiIchiran.dialog-class >>> .el-dialog__header {
  padding: 0px 20px 10px;
}
.KojiKubunSetsumeiIchiran.dialog-class >>> .el-dialog__body {
  padding: 0px 10px 30px;
  width: 1130px;
  height: 750px;
}
.el-dialog__wrapper.dialog-class >>> .el-dialog::-webkit-scrollbar {
  background-color: rgba(0, 0, 0, 0.1);
  width: 10px;
  height: 10px;
}
.el-dialog__wrapper.dialog-class >>> .el-dialog::-webkit-scrollbar-thumb {
  background-color: rgba(82, 81, 81, 0.5);
  width: 10px;
  border-radius: 5px;
}
.el-dialog__wrapper.dialog-class >>> .el-dialog::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.5);
}
</style>