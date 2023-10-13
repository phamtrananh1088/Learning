<!-- タブレット用のIndex -->
<template>
  <div :class="['main-theme-' + theme]">
    <div class="main-header">
      <el-header>
        <el-row :gutter="0">
          <div class="header-logo" :style="{ width: menuWidth - 1 + 'px' }">
            <img v-show="!isMobile()" v-bind:src="logo" />
          </div>
          <div
            class="header-collapse"
            :style="{ left: !isMobile() ? '200px' : '0' }"
          >
            <el-button size="mini" v-show="isCollapse" @click="toggleLeft"
                      >メニュー表示</el-button
                    >
            <el-button size="mini" v-show="!isCollapse" @click="toggleLeft"
                      >メニュー非表示</el-button
                    >
            <!-- <i
              class="el-icon-s-fold"
              v-show="!isCollapse"
              @click="toggleLeft"
            ></i>
            <i
              class="el-icon-s-unfold"
              v-show="isCollapse"
              @click="toggleLeft"
            ></i> -->
          </div>
          <el-col :span="14" :xs="14" class="header-title">
            <span>{{ pagetitle }}</span>
          </el-col>
          <el-col :span="10" :xs="10" class="header-info">
            <el-col :span="!isMobile() ? 5 : 17">
              <div class="envtype" style="color: red">
                <span>{{ envType }}</span>
              </div>
            </el-col>
            <el-col :span="!isMobile() ? 12 : 0">
              <div v-show="!isMobile()" class="user">
                <p>{{ shozokuBushoName }}</p>
                <p>{{ userName }}</p>
              </div>
            </el-col>
            <el-col :span="7">
              <div class="h-link">
                <ul>
                  <li v-for="(item, index) in links" :key="index">
                    <el-button
                      type="primary"
                      plain
                      v-show="!isMobile()"
                      @click="to(item)"
                      >{{ item.text }}</el-button
                    >
                    <i
                      class="el-icon-switch-button"
                      v-show="isMobile()"
                      @click="to(item)"
                      style="padding-top: 10px"
                    ></i>
                  </li>
                </ul>
              </div>
            </el-col>
          </el-col>
        </el-row>
      </el-header>
    </div>
    <div class="main-aside" :style="{ width: menuWidth + 'px' }">
      <div class="main-menu">
        <el-scrollbar style="height: 100%">
          <br />
          <i
            class="el-icon-question"
            style="cursor: pointer"
            @click="openManual()"
            >マニュアル</i
          ><br /><br />
          <Menu
            :isCollapse="isCollapse"
            :list="menuOptions"
            @menuTitle="onSelect"
          ></Menu>
          <!-- <router-link to="form">フォーム</router-link><br />
          <router-link to="grid">一覧</router-link><br /> -->
          <!-- <br/><br/>
          <i class="el-icon-s-tools" @click="changeTheme('blue')">Reafs-W</i><br/>
          <i class="el-icon-s-tools" @click="changeTheme('green')">Reafs-R</i><br/>
          <i class="el-icon-s-tools" @click="changeTheme('orange')">Reafs-T</i> -->
        </el-scrollbar>
      </div>
    </div>
    <div class="main-container" :style="{ left: menuWidth - 1 + 'px' }">
      <div class="main-content">
        <el-scrollbar
          style="height: 100%"
          :wrapClass="isCollapse ? 'wrap-class-sidehide' : 'wrap-class'"
        >
          <loading v-show="$store.getters.isLoading()"></loading>
          <keep-alive>
            <router-view
              v-if="
                !$route.meta ||
                ($route.meta && !$route.meta.hasOwnProperty('keepAlive'))
              "
            ></router-view>
          </keep-alive>
          <router-view
            v-if="$route.meta && $route.meta.hasOwnProperty('keepAlive')"
          ></router-view>
          <Manual :dialogVisible.sync="showDialogManual" :manualType ="manualType"></Manual>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>
<script>
import loading from "@/components/basic/RouterLoading";
import Menu from "@/components/basic/ElementMenu.vue";
import Manual from "@/components/modal/Modal_Manual.vue";
let imgUrl = require("@/assets/imgs/header_logo.png"); // 2022.02.18 Reafsロゴに変更
import "../assets/css/commonR.less";
var $vueIndex;
const { body } = document;
const collapseWidth = 1180; // メニュー縮小表示幅　ipad Airの幅

export default {
  data() {
    return {
      menuWidth: 0, // サイドメニュー幅。Reafs_Rの場合はスマホデバイスが多いので、デフォルトは「0」非表示になる
      isCollapse: true, //Reafs_Rの場合、デフォルトは非表示になる
      menu_theme: "light",
      pagetitle: this.$store.getters.getPageTitle(),
      envType: "開発環境",
      userName: "--",
      shozokuBushoName: "資産管理部 資産管理課",
      navigation: [
        { name: "ホームページ", id: 0, path: this.http.resolve("/Reafs_R_Web/mobile") },
      ],
      logo: imgUrl,
      theme: "green",
      links: [
        // { text: "個人設定", path: "/UserInfo", id: -1 },
        { text: "ログアウト", path: this.http.resolve("/Reafs_R_Web/login"), id: -4 },
      ],
      menuOptions: [],
      showDialogManual: false,
      manualType: '' // マニュアルの種類
    }
  },
  components: {
    Menu,
    loading,
    Manual
  },
  provide () {
    return {
      setPageTitle: this.setPageTitle
    }
  },
  beforeMount() {
    window.addEventListener("resize", this.resizeScreen);
  },
  created() {
    this.changeTheme("green");
    let theme = localStorage.getItem("main_theme");
    if (theme) {
      this.theme = theme;
    }
    this.menu_theme = this.theme == "white" ? "dark" : "light";
    let userInfo = this.$store.getters.getUserInfo();
    this.userName = userInfo.userName;
    this.shozokuBushoName = userInfo.shozokuBushoName;
    $vueIndex = this;
    
    if (userInfo.reafsWRTFlg !== 'RD00800') {
      this.$store.commit('clearUserInfo', '')
      this.$store.commit('clearMenuInfo', '')
      window.location.href = this.http.resolve("/Reafs_R_Web/login");
    }
    
    // メニューを取得
    this.http.ajax({
      url: 'api/Reafs_R/Mobile/getTreeMenu',
      json: true,
      success: function (result) {
        $vueIndex.menuOptions = result.data
        for (let i = 0; i < $vueIndex.menuOptions.length; i++) {
          if ($vueIndex.menuOptions[i].HyojiFlg != 1) {
            $vueIndex.menuOptions.splice(i, 1)
          }
        }
      },
      type: 'get',
      async: false
    })
  },
  watch: {
    $route(to, from) {
      let t = setInterval(() => {
        if (
          $vueIndex.pagetitle &&
          $vueIndex.pagetitle != $vueIndex.$store.getters.getPageTitle()
        ) {
          $vueIndex.pagetitle = $vueIndex.$store.getters.getPageTitle()
          clearInterval(t)
        } else if ($vueIndex.pagetitle) {
          clearInterval(t)
        } else {
          $vueIndex.pagetitle = $vueIndex.$store.getters.getPageTitle()
          clearInterval(t)
        }
      }, 200)
    },
  },
  methods: {
    toggleLeft() {
      this.isCollapse = !this.isCollapse;
      this.menuWidth = this.isCollapse ? 0 : 200;
    },
    changeTheme(name) {
      if (this.theme != name) {
        this.theme = name;
      }
      this.menu_theme = this.theme == "white" ? "dark" : "light";
      localStorage.setItem("main_theme", name);
    },
    getMsg(key) {
      return this.commonFunctionUI.getMsg(key);
    },
    to(item) {
      if (typeof item === "string" || item.path == this.http.resolve("/Reafs_R_Web/login")) {
        if (item == "/Reafs_R_Web/login" || item.path == this.http.resolve("/Reafs_R_Web/login")) {
          // 確認メッセージ
          this.getMsg("K000008").then((response) => {
            this.MsgConf(
              {
                title: this.$store.getters.getPageTitle(),
                message: response.data,
              },
              () => {
                // OKが選択された場合
                // ログイン情報を削除
                this.$store.commit("clearUserInfo", "");
                this.$store.commit('clearMenuInfo', '')
                window.location.href = this.http.resolve("/Reafs_R_Web/login");
                return;
              },
              () => {
                // Cancelが選択された場合
                return;
              }
            );
          });
        } else {
          // this.$router.push({ path: item })
          this.$router.push({ name: item });
        }
      }
    },
    onSelect(obj) {
      this.pagetitle = obj;
    },
    resizeScreen() {
      if (this.isMobile()) {
        this.isCollapse = true;
        this.menuWidth = 0;
      } else {
      }
    },
    isMobile() {
      const react = body.getBoundingClientRect();
      return react.width < collapseWidth;
    },
    openManual() {
      this.showDialogManual = true
      this.manualType = 'R'
    },
    setPageTitle (title) { // 追加
      this.pagetitle = title
    }
  },
};
</script>

<style scoped>
body {
  height: 100%;
  width: 100%;
}
.main-header {
  height: 47px;
  position: fixed;
  width: 100%;
  left: 0;
  top: 0;
  z-index: 1500;
}
.main-header .header-logo {
  position: absolute;
  height: 47px;
  line-height: 47px;
}
.header-logo img {
  height: 100%;
}
.main-header .header-collapse {
  position: absolute;
  color: rgb(88, 88, 88);
  font-size: 18pt;
  /* padding-top: 10px; */
  padding-top: 3px;
}

.main-aside {
  height: 100%;
  position: absolute;
  float: left;
  overflow: hidden;
}
.main-aside .main-menu {
  position: absolute;
  width: 100%;
  top: 47px;
  bottom: 0;
  background: white;
  border-right: 1px solid #e3e3e3;
}

.main-aside .main-menu >>> .is-horizontal {
  display: none !important;
}
.main-aside .main-menu >>> .is-vertical {
  width: 2px;
}
.main-container {
  /* min-width: 800px; */
  right: 0;
  display: inline-block;
  position: absolute;
  margin: 0;
  box-sizing: border-box;
  height: 100%;
}
.main-content {
  border-left: 1px solid #eee;
  position: absolute;
  width: 100%;
  bottom: 0;
  top: 48px;
  margin: 0;
  overflow: auto;
  background: #f3f3f3;
}

.header-title {
  text-align: right;
  padding-top: 10px;
  font-size: 13pt;
  font-weight: bold;
}
.header-info {
  right: 20px;
  text-align: right;
}
</style>

<style lang="less" scoped>
.main-header .user {
  display: inline-block;
  height: 100%;
  margin-top: 5px;
  text-align: right;

  p {
    margin: 0;
    font-size: 10pt;
  }
}
.main-header .pagetitle {
  text-align: center;
  padding: 15px;
  font-size: 14pt;
}
.main-header .envtype {
  text-align: right;
  padding-top: 15px;
  font-size: 10.25pt;
}
.h-link:hover {
  cursor: pointer;
}
.header-info > div {
  float: left;
}
.main-header .user span:first-child {
  font-size: 15px;
  font-weight: bolder;
}
.h-link ul {
  height: 100%;
  display: inline-block;
  margin-top: 5px;
}
.h-link li {
  height: 100%;
  list-style: none;
  float: left;
  position: relative;
  cursor: pointer;
  z-index: 3;
}

img[src=""],
img:not([src]) {
  opacity: 0;
}
</style>

<style lang="less" scoped>
.main-theme-green {
  .header {
    background: #daffb2;
  }
  .main-header {
    background-color: #daffb2;
  }
}

.main-theme-orange {
  .header {
    background: #ffe4bc;
  }
  .main-header {
    background-color: #ffe4bc;
  }
}

.h-link a:hover {
  color: #dfdfdf;
}
.h-link .actived {
  border-bottom: 2px solid rgb(88, 88, 88);
}
.h-link a,
.h-link .actived a,
.main-header .user,
.main-header .header-title span {
  color: rgb(88, 88, 88);
}

.main-theme-blue {
  .header {
    background: #aec8ff;
  }
  .main-header {
    background-color: #aec8ff;
  }

  .h-link a,
  .h-link .actived a,
  .main-header .user,
  .main-header .header-title span {
    color: rgb(88, 88, 88);
  }
}
</style>

<style>
html,
body,
#app {
  width: 375px; /* iphoneSEの幅 */
  height: 617px; /* 高さ */
  min-width: 375px; /* iphoneSEの幅 */
  min-height: 617px; /* iphoneSEの高さ */
  overflow: hidden;
}
.el-scrollbar__wrap {
  overflow-x: hidden;
}
*::-webkit-scrollbar {
  height: 10px;
}
.wrap-class-sidehide {
  overflow-x: auto;
}
.wrap-class-sidehide .el-scrollbar__view {
  /* width: 1920px; */
  width: 100%;
}
.wrap-class {
  overflow-x: auto;
}
.wrap-class .el-scrollbar__view {
  /* width: 1720px; */
  width: 100%;
}
.scrollbarHide::-webkit-scrollbar {
  display: none;
}
.scrollbarShow::-webkit-scrollbar {
  display: block;
}
.animated {
  -webkit-animation-duration: 1s;
  animation-duration: 1s;
  -webkit-animation-fill-mode: both;
  animation-fill-mode: both;
}
</style>
