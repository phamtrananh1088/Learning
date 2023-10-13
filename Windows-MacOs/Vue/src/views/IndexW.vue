<template>
  <div :class="['main-theme-' + theme]">
    <div v-show="headerBar" class="main-header">
      <el-header>
        <el-row :gutter="20">
          <!-- <div class="header-logo" :style="{ width: menuWidth - 1 + 'px' }"  @click="toTop"> -->
          <div class="header-logo" :style="{ width: menuWidth - 1 + 'px' }">
            <!-- <img v-show="!isCollapse" v-bind:src="logo" /> -->
            <!-- <img v-show="!isMobile()" v-bind:src="logo" style="cursor:pointer" /> -->
            <img v-show="!isMobile()" v-bind:src="logo" />
          </div>
          <!-- ハノイ側修正2022/09/26　課題管理表№89：「2022/07/20依頼分」「screenPopupを利用して起動するダイアログ画面を対象に開閉ボタン、ログアウトボタン機能の修正を行う、」 -->
          <div v-show="menuLeftShow" class="header-collapse" :style="{ left: !isMobile() ? '200px' : '0' }">
            <el-button size="mini" v-show="isCollapse" @click="toggleLeft" style="margin-left:0px">メニュー表示</el-button>
            <el-button size="mini" v-show="!isCollapse" @click="toggleLeft" style="margin-left:0px">メニュー非表示</el-button>
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
          <el-col :span="10" class="header-title">
            <span>{{ pagetitle }}</span>
          </el-col>
          <el-col :span="14" class="header-info">
            <el-col :span="5">
              <div class="envtype" style="color: red">
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
                    <el-button :id="item.linkId" type="primary" plain v-show="!isMobile()" @click="to(item)">{{
    item.text
}}</el-button>
                    <i class="el-icon-switch-button" v-show="isMobile()" @click="to(item)"
                      style="padding-top: 10px"></i>
                  </li>
                  <li v-show="!menuLeftShow && !logoutButton && btnBackShow">
                    <el-button type="primary" plain @click="onBack">
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
          <br />
          <i class="el-icon-question" style="cursor: pointer" @click="openManual()">マニュアル</i><br /><br />
          <Menu :isCollapse="isCollapse" :list="menuOptions" @menuTitle="onSelect"></Menu>
          <!-- <router-link to="form">フォーム</router-link><br />
          <router-link to="grid">一覧</router-link><br /> -->
        </el-scrollbar>
      </div>
    </div>
    <div class="main-container" :style="{ left: menuLeftShow ? menuWidth - 1 + 'px' : 0 }">
      <div class="main-content">
        <!-- <div id="notification" v-if="isChanged>
            <el-button @click="cancel">×</el-button>
            <p class="notificationTitle">更新通知</p>
            <p>アプリケーションの更新を確認しました。更新しますか？</p>
            <el-button @click="reload">OK</el-button>
        </div> -->
        <el-scrollbar style="height: 100%" :wrapClass="isCollapse ? 'wrap-class-sidehide' : 'wrap-class'" ref="scroll">
          <loading v-show="$store.getters.isLoading()"></loading>
          <keep-alive>
            <router-view v-if="
  !$route.meta ||
  ($route.meta && !$route.meta.hasOwnProperty('keepAlive'))
"></router-view>
          </keep-alive>
          <router-view v-if="$route.meta && $route.meta.hasOwnProperty('keepAlive')"></router-view>
          <Manual :dialogVisible.sync="showDialogManual" :manualType="manualType"></Manual>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>
<script>
import loading from '@/components/basic/RouterLoading'
import Menu from '@/components/basic/ElementMenu.vue'
import Manual from '@/components/modal/Modal_Manual.vue'

let imgUrl = require('@/assets/imgs/header_logo.png') // 2022.02.18 Reafsロゴに変更
var $vueIndex
const { body } = document
const collapseWidth = 501 // メニュー縮小表示幅

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
      pagetitle: this.$store.getters.getPageTitle(),
      envType: '検証環境W',
      userName: '--',
      shozokuBushoName: '資産管理部 資産管理課',
      navigation: [{ name: 'ホームページ', id: 0, path: this.http.resolve('/Reafs_W/home') }],
      logo: imgUrl,
      theme: 'green',
      links: [
        // { text: "個人設定", path: "/UserInfo", id: -1 },
        { text: 'ログアウト', path: this.http.resolve('/Reafs_W/login'), id: -4, linkId: 'btnLogout' }
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
      hideLeftMenu: this.hideLeftMenu,
      setPageTitle: this.setPageTitle,
      showLogoutButton: this.showLogoutButton,
      showBackShowBtn: this.showBackShowBtn,
      showHeaderBar: this.showHeaderBar
    }
  },
  beforeMount () {
    window.addEventListener('resize', this.resizeScreen)
  },
  created () {
    // let theme = localStorage.getItem("main_theme");
    // if (theme) {
    //   this.theme = theme;
    // }

    // アプリの更新チェック処理用
    // setInterval(this.fetchServer, 1000 * 60 * this.normalizedFrequencyMinutes);

    this.theme = 'blue'
    this.menu_theme = this.theme == 'white' ? 'dark' : 'light'
    let userInfo = this.$store.getters.getUserInfo()
    this.userName = userInfo.userName
    this.shozokuBushoName = userInfo.shozokuBushoName
    $vueIndex = this

    if (userInfo.reafsWRTFlg !== 'WD00000') {
      this.$store.commit('clearUserInfo', '')
      this.$store.commit('clearMenuInfo', '')
      window.location.href = this.http.resolve('/Reafs_W')
    }
    // メニューを取得
    this.http.ajax({
      url: 'api/menu/getTreeMenu',
      json: true,
      success: function (result) {
        $vueIndex.menuOptions = result.data
      },
      type: 'get',
      async: false
    })
  },
  mounted () {
    window.addEventListener('beforeunload', e => this.beforeunloadHandler(e))
    window.addEventListener('unload', e => this.unloadHandler(e))

    // サイドメニューから画面遷移した際のメニュー表示制御
    if ($vueIndex.$store.getters.getOldPath().toString().indexOf(this.http.resolve('/Reafs_W/home')) > -1) {
      this.isCollapse = false
      this.menuWidth = 200
    } else {
      this.isCollapse = true
      this.menuWidth = 0
    }
  },
  destroyed () {
    window.removeEventListener('beforeunload', e => this.beforeunloadHandler(e))
    window.removeEventListener('unload', e => this.unloadHandler(e))
  },
  watch: {
    $route (to, from) {
      let t = setInterval(() => {
        if (
          $vueIndex.pagetitle &&
          $vueIndex.pagetitle != $vueIndex.$store.getters.getPageTitle()
        ) {
          $vueIndex.pagetitle = $vueIndex.$store.getters.getPageTitle()
          clearInterval(t)
        } else if ($vueIndex.pagetitle) {
          clearInterval(t)
        }
      }, 200)

      // 画面内のコントロールから画面遷移した際のメニュー表示制御
      if (to.fullPath.toString().indexOf(this.http.resolve('/Reafs_W/home')) > -1) {
        this.isCollapse = false
        this.menuWidth = 200
      } else {
        this.isCollapse = true
        this.menuWidth = 0
      }

      setTimeout(() => {
        if (this.$refs.scroll) {
          this.$refs.scroll.update()
        }
      }, 500)
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
    beforeunloadHandler () {
      this._beforeUnload_time = new Date().getTime()
    },
    unloadHandler (e) {
      this._gap_time = new Date().getTime() - this._beforeUnload_time
      // debugger
      // ウィンドウが閉じているか更新されているかを判断する
      if (this._gap_time <= 5) {
        // 一旦コメント
        // ログインしている場合は、ウィンドウを閉じる前にログイン情報を削除
        // this.$store.commit('clearUserInfo', '')
        // this.$store.commit('clearMenuInfo', '')
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
    setPageTitle (title) {
      this.pagetitle = title
    },
    toggleLeft () {
      this.isCollapse = !this.isCollapse
      this.menuWidth = this.isCollapse ? 0 : 200
    },
    toTop () {
      window.location.href = this.http.resolve('/Reafs_W/home')
    },
    changeTheme (name) {
      if (this.theme != name) {
        this.theme = name
      }
      this.menu_theme = this.theme == 'white' ? 'dark' : 'light'
      localStorage.setItem('main_theme', name)
    },
    getMsg (key) {
      return this.commonFunctionUI.getMsg(key)
    },
    to (item) {
      if (typeof item === 'string' || item.path == this.http.resolve('/Reafs_W/login')) {
        if (item == '/Reafs_W/login' || item.path == this.http.resolve('/Reafs_W/login')) {
          // 確認メッセージ
          this.getMsg('K000008').then((response) => {
            this.MsgConf(
              {
                title: this.$store.getters.getPageTitle(),
                message: response.data
              },
              () => {
                // OKが選択された場合
                // ログイン情報を削除
                this.$store.commit('clearUserInfo', '')
                this.$store.commit('clearMenuInfo', '')
                window.location.href = this.http.resolve('/Reafs_W')
              },
              () => {
                // Cancelが選択された場合

              }
            )
          })
        } else {
          // this.$router.push({ path: item })
          this.$router.push({ name: item })
        }
      }
    },
    onSelect (obj) {
      this.pagetitle = obj
    },
    resizeScreen () {
      if (this.$refs.scroll) {
        this.$refs.scroll.update()
      }
      if (this.isMobile()) {
        this.isCollapse = true
        this.menuWidth = 0
      } else {
      }
    },
    isMobile () {
      const react = body.getBoundingClientRect()
      return react.width < collapseWidth
    },
    openManual () {
      this.showDialogManual = true
      this.manualType = 'W'
    }
  }
  // fetchServer() {
  //   fetch(this.normalizedUrl)
  //     .then(res => {
  //       if (res.status !== 200) {
  //         throw Error('Cannot access server.');
  //       }
  //       return res.text();
  //     })
  //     .then(html => {
  //       const hash = this.createHash(html);
  //       this.judgeHash(hash);
  //     })
  //     .catch(err => console.log(err));
  // },
  // createHash(str) {
  //   const len = str.length;
  //   let hash = 0;
  //   if (len === 0) return hash;
  //   let i;
  //   for (i = 0; i < len; i++) {
  //     hash = ((hash << 5) - hash) + str.charCodeAt(i);
  //     hash |= 0; // Convert to 32bit integer
  //   }
  //   return hash;
  // },
  // judgeHash(hash) {
  //   if (!this.previousHash) {
  //     this.previousHash = hash;
  //     return;
  //   }
  //   if (this.previousHash !== hash) {
  //     this.isChanged = true;
  //   }
  // },
  // reload() {
  //   location.reload(true);
  // },
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

.main-aside .main-menu>>>.ivu-menu {
  text-align: left;
  position: unset;
  width: 100% !important;
}

.main-aside .main-menu>>>.is-horizontal {
  display: none !important;
}

.main-aside .main-menu>>>.is-vertical {
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
  top: 48px;
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

.sidebar>ul {
  height: 100%;
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

.header-info>div {
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
  width: 1536px;
}

.wrap-class {
  overflow-x: auto;
}

.wrap-class .el-scrollbar__view {
  /* width: 1720px; */
  width: 1336px;
}

.el-scrollbar__bar.is-horizontal {
  height: 11px;
  opacity: 1;
}

.el-scrollbar .el-scrollbar__thumb {
  background: rgba(144, 147, 153);
}

.scrollbarHide::-webkit-scrollbar {
  display: none;
}

.scrollbarShow::-webkit-scrollbar {
  display: block;
}
</style>
