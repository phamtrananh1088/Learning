<template>
  <el-dialog :title="title" :visible="dialogVisible" :show-close="false" class="ShoriGaiyo dialog-class"
    @close="onClose()" @open="onOpen()" :close-on-click-modal="false">
    <el-container class="home-el-container">
      <el-main class="home-el-main">
        <el-row>
          <el-col v-for="(item, index) in data" :key="index" :span="24">
           <el-checkbox-group v-model="selectTemp">
            <el-checkbox :label="item.value" @change="onCheckboxChange($event, item)">{{ item.name }}</el-checkbox>
            </el-checkbox-group>
          </el-col>
        </el-row>
      </el-main>
      <el-footer class="home-el-footer" height="auto">
        <el-row style="margin:10px;">
          <div style="display: flex; justify-content: space-around;">
            <el-button @click="onSubmit" style="float: right">決定</el-button>
            <el-button @click="onClose" style="float: right">閉じる</el-button>
          </div>
        </el-row>
      </el-footer>
    </el-container>
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
    },
    data: {
      type: Array,
      default () { return [] }
    },
    title: {
      type: String,
      default: '進捗選択'
    },
    selection: {
      type: Array,
      default () { return [] }
    }
  },
  data () {
    return {
      selectTemp: []
    }
  },
  methods: {
    onOpen () {
      this.selectTemp = JSON.parse(JSON.stringify(this.selection))
    },
    onCheckboxChange (e, item) {
      if (e) {
        if (!this.selectTemp.find(x => x === item.value)) {
          this.selectTemp.push(item.value)
        }
      } else {
        const index = this.selectTemp.indexOf(item.value)
        if (index > -1) {
          this.selectTemp.splice(index, 1) // 2nd parameter means remove one item only
        }
      }
      return e
    },
    onSubmit () {
      this.$emit('update:selection', this.selectTemp)
      console.log(this.selection)
      this.$emit('update:dialogVisible', false)
      this.data.map((x) => {
        x.isSelect = false
      })
    },
    onClose () {
      this.data.map((x) => {
        x.isSelect = false
      })
      this.$emit('update:dialogVisible', false)
    }
  }
}
</script>
<style>
.ShoriGaiyo .el-dialog {
  width: 280px !important;
}

.ShoriGaiyo .el-dialog__body {
  padding: 0 10px !important;
  height: 550px;
}

.ShoriGaiyo .home-el-container {
  height: 100%;
}

.ShoriGaiyo .home-el-main {
  padding: 10px !important;
}
.ShoriGaiyo main {
  overflow-y: auto;
}

.ShoriGaiyo .el-dialog__header {
  background: rgb(93,90,88);
}

.ShoriGaiyo .el-dialog__header span {
  color: white;
}

/* .ShoriGaiyo main::-webkit-scrollbar {
  width: 1em;
}

.ShoriGaiyo main::-webkit-scrollbar-track {
  background-color: rgba(216, 207, 207, 0.3);
}

.ShoriGaiyo main::-webkit-scrollbar-thumb {
  background-color: darkgrey;
  outline: 1px solid slategrey;
} */

</style>
