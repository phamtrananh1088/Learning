<template>
  <div :class="['main-theme-' + theme]">
    <div v-show="headerBar" class="main-header">
      <el-header>
        <el-row :gutter="20">
          <div class="header-logo" :style="{ width: menuWidth - 1 + 'px' }">
            <!-- <img v-show="!isCollapse" v-bind:src="logo" /> -->
            <img v-show="!isMobile()" v-bind:src="logo" />
          </div>
          <div class="header-collapse" :style="{ left: !isMobile() ? '200px' :'0' }">
            <i class="el-icon-s-fold" v-show="!isCollapse" @click="toggleLeft"></i>
            <i class="el-icon-s-unfold" v-show="isCollapse" @click="toggleLeft"></i>
          </div>
          <el-col :span="10" class="header-title">
            <span>{{ pagetitle }}</span>
          </el-col>
          <el-col :span="14" class="header-info">
            <el-col :span="5">
              <div class="envtype" style="color:red">
                <span>{{ envType }}</span>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="user">
                <p>{{ shozokuBushoName }}</p>
                <p>{{ userName }}</p>
              </div>
            </el-col>
            <el-col :span="7">
              <div class="h-link">
                <ul>
                  <li v-show="menuLeftShow || logoutButton" v-for="(item, index) in links" :key="index">
                    <el-button :id="item.linkId" type="primary" plain v-show="!isMobile()" @click="to(item)">{{item.text}}</el-button>
                    <i class="el-icon-switch-button" v-show="isMobile()" @click="to(item)" style="padding-top:10px;"></i>
                  </li>
                  <li v-show="!menuLeftShow && !logoutButton && btnBackShow">
                    <el-button
                      type="primary"
                      plain
                      @click="onBack">
                      閉じる
                    </el-button>
                  </li>
                </ul>
              </div>
            </el-col>
          </el-col>
        </el-row>
      </el-header>
    </div>
    <div v-show="menuLeftShow" class="main-aside" :style="{ width: menuWidth + 'px' }">
      <div class="main-menu">
        <el-scrollbar style="height: 100%">
          <Menu
            :isCollapse="isCollapse"
            :list="menuOptions"
            @menuTitle="onSelect"
          ></Menu>
          <router-link to="form">フォーム</router-link><br/>
          <router-link to="grid">一覧</router-link><br/>
          <br/><br/>
          <i class="el-icon-s-tools" @click="changeTheme('blue')">Reafs-W</i><br/>
          <i class="el-icon-s-tools" @click="changeTheme('green')">Reafs-R</i><br/>
          <i class="el-icon-s-tools" @click="changeTheme('orange')">Reafs-T</i>
        </el-scrollbar>
      </div>
    </div>
    <div class="main-container" :style="{ left: menuLeftShow ? menuWidth - 1 + 'px' : 0 }">
      <div class="main-content">
        <el-scrollbar style="height: 100%;" :wrapClass="isCollapse?'wrap-class-sidehide':'wrap-class'">
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
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>
<script>
import loading from '@/components/basic/RouterLoading'
import Menu from '@/components/basic/ElementMenu.vue'
let imgUrl = require('@/assets/imgs/header_logo.png') // 2022.02.18 Reafsロゴに変更
var $vueIndex
const {body} = document
const collapseWidth = 501// メニュー縮小表示幅

export default {
  data () {
    return {
      menuLeftShow: true,
      logoutButton: false,
      btnBackShow: false,
      headerBar: true,
      menuWidth: 200, // サイドメニュー幅
      isCollapse: false,
      menu_theme: 'light',
      pagetitle: 'メニュー',
      envType: '検証環境',
      userName: '--',
      shozokuBushoName: '資産管理部 資産管理課',
      navigation: [{ name: 'ホームページ', id: 0, path: '/home' }],
      logo: imgUrl,
      theme: 'green',
      links: [
        // { text: "個人設定", path: "/UserInfo", id: -1 },
        { text: 'ログアウト', path: '/login', id: -4 }
      ],
      menuOptions: []
    }
  },
  components: {
    Menu,
    loading
  },
  provide () {
    return {
      hideLeftMenu: this.hideLeftMenu,
      showLogoutButton: this.showLogoutButton,
      showHeaderBar: this.showHeaderBar,
      showBackShowBtn: this.showBackShowBtn
    }
  },
  beforeMount () {
    window.addEventListener('resize', this.resizeScreen)
  },
  created () {
    let theme = localStorage.getItem('main_theme')
    if (theme) {
      this.theme = theme
    }
    this.menu_theme = this.theme == 'white' ? 'dark' : 'light'
    let userInfo = this.$store.getters.getUserInfo()
    this.userName = userInfo.userName
    this.shozokuBushoName = userInfo.shozokuBushoName
    $vueIndex = this

    // メニューを取得
    let treeMenu = $vueIndex.$store.getters.getMenuInfo()
    if (treeMenu) {
      $vueIndex.menuOptions = treeMenu
    } else {
      this.http.ajax({
        url: 'api/menu/getTreeMenu',
        json: true,
        success: function (result) {
          $vueIndex.menuOptions = result.data
          $vueIndex.$store.commit('setMenuInfo', result.data)
        },
        type: 'get',
        async: false
      })
    }
  },
  methods: {
    async onBack () {
      if (window.parent) {
        window.parent.postMessage({
          'call': 'closePopup'
        }, '*')
      }
    },
    hideLeftMenu (show) {
      this.menuLeftShow = show
    },
    showLogoutButton (show) {
      this.logoutButton = show
    },
    showBackShowBtn (show) {
      this.btnBackShow = show
    },
    showHeaderBar (show) {
      this.headerBar = show
    },
    toggleLeft () {
      this.isCollapse = !this.isCollapse
      this.menuWidth = this.isCollapse ? 0 : 200
    },
    changeTheme (name) {
      if (this.theme != name) {
        this.theme = name
      }
      this.menu_theme = this.theme == 'white' ? 'dark' : 'light'
      localStorage.setItem('main_theme', name)
    },
    to (item) {
      if (typeof item === 'string' || item.path == '/login') {
        if (item == '/login' || item.path == '/login') {
          // ログイン情報を削除
          this.$store.commit('setPageData', {})
          this.$store.commit('clearUserInfo', '')
          this.$store.commit('clearMenuInfo', '')
          window.location.href = '/'
          return
        }
        this.$router.push({ path: item })
      }
    },
    onSelect (obj) {
      this.pagetitle = obj
    },
    resizeScreen () {
      if (this.isMobile()) {
        this.isCollapse = true
        this.menuWidth = 0
      } else {
      }
    },
    isMobile () {
      const react = body.getBoundingClientRect()
      return react.width < collapseWidth
    }
  }
}
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
.main-header .header-collapse{
  position: absolute;
  color: white;
  font-size: 18pt;
  padding-top: 10px;
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

.main-aside .main-menu >>> .ivu-menu {
  text-align: left;
  position: unset;
  width: 100% !important;
}
.main-aside .main-menu >>> .is-horizontal {
  display: none !important;
}
.main-aside .main-menu >>> .is-vertical {
  width: 2px;
}
.main-container {
  min-width: 800px;
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
  top: 0px;
  margin: 0;
  overflow: auto;
  background: #f3f3f3;
}

.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  overflow-y: scroll;
}
.sidebar::-webkit-scrollbar {
    width: 0;
}
.sidebar-el-menu:not(.el-menu--collapse) {
    width: 250px;
}
.sidebar > ul {
    height: 100%;
}
.header-title{
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
.user-header {
  background: white;
  height: 52px;
  width: 52px;
  border-radius: 50%;
  margin-right: 0px;
  top: 4px;
  position: relative;
  border: 1px solid #dfdfdf;
}
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
.main-header .pagetitle{
  text-align: center;
  padding: 15px;
  font-size: 14pt;
}
.main-header .envtype{
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
   background: rgb(146, 208, 80);
  }
  .main-header {
    background-color: rgb(146, 208, 80);
  }
}

.main-theme-orange {
  .header {
   background: rgb(255, 152, 0);
  }
  .main-header {
    background-color: rgb(255, 152, 0);
  }
}

.h-link a:hover {
  color: #dfdfdf;
}
.h-link .actived {
  border-bottom: 2px solid white;
}
.h-link a,
.h-link .actived a,
.main-header .user ,
.main-header .header-title span{
  color: black;
}

.main-theme-blue {
  .header {
   background: rgb(59, 73, 159);
  }
  .main-header {
    background-color: rgb(59, 73, 159);
  }

  .h-link a,
  .h-link .actived a,
  .main-header .user ,
  .main-header .header-title span{
    color: white;
  }
}
</style>

<style>
.el-scrollbar__wrap {
  overflow-x: hidden;
}
*::-webkit-scrollbar {
  height: 10px;
}
.wrap-class-sidehide {
  overflow-x:auto;
}
.wrap-class-sidehide .el-scrollbar__view{
  /* width: 1920px; */
  width: 1536px;
  height: 100%;
}
.wrap-class {
  overflow-x:auto;
}
.wrap-class .el-scrollbar__view{
  /* width: 1720px; */
  width: 1336px;
  height: 100%;
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
@-webkit-keyframes fadeInLeftBig {
  0% {
    opacity: 0;
    -webkit-transform: translate3d(-50px, 0, 0);
    transform: translate3d(-50px, 0, 0);
  }
  to {
    opacity: 1;
    -webkit-transform: none;
    transform: none;
  }
}
@keyframes fadeInLeftBig {
  0% {
    opacity: 0;
    -webkit-transform: translate3d(-50px, 0, 0);
    transform: translate3d(-50px, 0, 0);
  }
  to {
    opacity: 1;
    -webkit-transform: none;
    transform: none;
  }
}
.fadeInLeftBig {
  -webkit-animation-name: fadeInLeftBig;
  animation-name: fadeInLeftBig;
}
</style>
