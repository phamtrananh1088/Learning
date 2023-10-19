import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import http from '@/api/http'

// import redirect from '../redirect'
Vue.use(Router)

// ElementUIのメニューで重複クリックエラーを回避する
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch((err) => err)
}
const re = http.resolve

const router = new Router({
  mode: 'history',
  routes: [

    // Reafs-R-Web機能
    {
      path: re('/Reafs_R_Web/'),
      name: 'Index_R',
      component: () => import('@/views/IndexR.vue'), // Mobile用のINDEXを利用する
      redirect: re('/Reafs_R_Web/mobile/Top'),
      children: [
        {
          path: re('/Reafs_R_Web/mobile'),
          name: 'Rhome',
          component: () => import('@/views/Home.vue')
        },
        {
          path: re('/Reafs_R_Web/mobile/Top'),
          name: 'RD00801',
          component: () => import('@/views/Reafs-R-Web/mobile/RD00801/Menu.vue')
        },
        {
          path: re('/Reafs_R_Web/mobile/MishoninIchiran'),
          name: 'RD00802',
          component: () => import('@/views/Reafs-R-Web/mobile/RD00802/MishoninIchiran.vue')
        },
        {
          path: re('/Reafs_R_Web/mobile/KinkyuKougaku'),
          name: 'RD00803',
          component: () => import('@/views/Reafs-R-Web/mobile/RD00803/KinkyuKougaku.vue')
        }
      ]
    },
    // Reafs-R-Web機能ログイン
    {
      path: re('/Reafs_R_Web/login'),
      name: 'RD00800',
      component: () => import('@/views/Reafs-R-Web/mobile/RD00800/Login.vue'),
      meta: {
        anonymous: true
      }
    },
    //* 404は最後
    {
      path: '*',
      name: '404ERROR',
      component: () => import('@/views/redirect/404.vue')
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  store.getters.getUserInfo()
  if (to.matched.length === 0) return next({ path: re('/404') })
  store.dispatch('onLoading', true)

  // 画面名設定
  await http
    .post('/api/Reafs_R/Mobile/getAllMenu', { user: 'info' })
    .then(function (result) {
      store.commit('setMenuInfo', result.data)
      let { 0: menuInfo } = result.data.filter((r) => {
        return r.SubMenuId == to.name
      })

      if (menuInfo) {
        store.commit('setpageTitle', menuInfo.SubMenuName)
      } else {
        store.commit('setpageTitle', '')
      }
    })
  if (store.getters.isLogin() && to.name === 'RD00800' && store.getters.getIsBackToLogin() != '1' && to.query.redirect) {
    return next({ name: 'RD00801' })
  }
  store.commit('setIsBackToLogin', '0')
  if ((to.hasOwnProperty('meta') && to.meta.anonymous) || store.getters.isLogin()) {
    if (to.query.guid && to.name !== 'RD00800') {
      store.commit('setIsBackToLogin', '1')
      return next({ name: 'RD00800', query: { redirect: to.path, guid: to.query.guid } })
    } else {
      return next()
    }
  } else {
    // if (to.name == 'RD00803') {
    //   return next({ name: 'RD00800', query: to.query })
    // }
  }
  next({ path: re('/Reafs_R_Web/login'), query: { redirect: to.path, guid: to.query.guid } })
})

router.afterEach((to, from) => {
  store.dispatch('onLoading', false)
  const oldPath = store.getters.getOldPath()
  if (to.fullPath !== oldPath) {
    store.commit('setPageData', {})
  }
  store.commit('setOldPath', to.fullPath)
})

router.onError((error) => {
  const pattern = /Loading chunk (\d)+ failed/g
  const isChunkLoadFailed = error.message.match(pattern)
  const targetPath = router.history.pending.fullPath
  console.log(error.message)
  console.log(targetPath)
  if (isChunkLoadFailed) {
    window.location.replace(window.location.href)
  }
})

export default router
