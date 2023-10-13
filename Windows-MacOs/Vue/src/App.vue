<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import "./assets/css/common.less";
export default {
  name: "App",
  created () {

  },
  mounted () {
    document.getElementById("v-loading-container").style.display = "none";
  }
};
</script>  

<style>
.el-loading {
  z-index: 999999;
}
.el-table th {
  display: table-cell !important;
}
.el-loading .el-loading-spinner {
  padding: 7px;
  background: #ececec;
  width: 200px;
  color: red;
  left: 0;
  right: 0;
  margin: 0 auto;
  border-radius: 5px;
  border: 1px solid #a0a0a0;
}
#app {
  font-family: 'Meiryo UI','メイリオ', Meiryo,'MS PGothic', 'ヒラギノ角ゴ ProN W3' ,"Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  overflow-y: hidden;
}

#app textarea {
  font-family: Arial;
}
#nprogress .bar {
  background: white;
  height: 2px;
}
 .grid-detail .el-table__fixed-body-wrapper{
  top: 41px !important;
} 
</style>
<style>
html,
body,
#app {
  height: 100%;
  min-height: 726px;
  min-width: 1024px;
  max-width: 1620px;
  max-height: 884px;
}
</style>
