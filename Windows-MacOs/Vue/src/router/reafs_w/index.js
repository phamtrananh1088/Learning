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
    // Reafs-W機能
    {
      path: re('/Reafs_W/'),
      name: 'Index_W',
      component: () => import('@/views/IndexW.vue'), // W用のINDEXを利用する
      redirect: re('/Reafs_W/home'),
      children: [
        {
          path: re('/Reafs_W/home'),
          name: 'WD00010',
          component: () => import('@/views/Reafs-W/common/WD00010/Top.vue')
        },
        // {
        //   path: '/Reafs_W/common/Top',
        //   name: 'WD00001',
        //   component: () => import('@/views/Reafs-W/common/WD00010/Top.vue')
        // },
        {
          path: re('/Reafs_W/OsiraseIchiran'),
          name: 'WD00011',
          component: () => import('@/views/Reafs-W/common/WD00011/OsiraseIchiran.vue')
        },
        {
          path: re('/Reafs_W/OsiraseTouroku'),
          name: 'WD00012',
          component: () => import('@/views/Reafs-W/common/WD00012/OsiraseTouroku.vue')
        },
        {
          path: re('/Reafs_W/IraiNyuryoku'),
          name: 'WD00110',
          component: () => import('@/views/Reafs-W/irai/WD00110/IraiNyuryoku.vue')
        },
        {
          path: re('/Reafs_W/CalendarSettei'),
          name: 'WM00100',
          component: () => import('@/views/Reafs-W/master/WM00100/CalendarSettei.vue')
        },
        {
          path: re('/Reafs_W/SyuzenIraiSansyoShonin'),
          name: 'WD00111',
          component: () => import('@/views/Reafs-W/irai/WD00111/SyuzenIraiSansyoShonin.vue')
        },
        {
          path: re('/Reafs_W/TyohyoIchiran'),
          name: 'WD00130',
          component: () => import('@/views/Reafs-W/irai/WD00130/TyohyoIchiran.vue')
        },
        {
          path: re('/Reafs_W/IchiranKensaku'),
          name: 'WD00210',
          component: () => import('@/views/Reafs-W/irai/WD00210/IchiranKensaku.vue')
        },
        {
          path: re('/Reafs_W/TeikiIraiSansyoShonin'),
          name: 'WD00311',
          component: () => import('@/views/Reafs-W/irai/WD00311/TeikiIraiSansyoShonin.vue')
        },
        {
          path: re('/Reafs_W/SagyoJoukyoIchiran'),
          name: 'WD00410',
          component: () => import('@/views/Reafs-W/sagyo/WD00410/SagyoJoukyoIchiran.vue')
        },
        {
          path: re('/Reafs_W/SagyoJoukyoTsukibetuIchiran'),
          name: 'WD00420',
          component: () => import('@/views/Reafs-W/sagyo/WD00420/SagyoJoukyoTsukibetuIchiran.vue')
        },
        {
          path: re('/Reafs_W/MishoninShoninIchiran'),
          name: 'WD00510',
          component: () => import('@/views/Reafs-W/shonin/WD00510/MishoninShoninIchiran.vue')
        },
        {
          path: re('/Reafs_W/WD00520/SyuzenIraiSansyoShonin'),
          name: 'WD00520',
          component: () => import('@/views/Reafs-W/shonin/WD00520/SyuzenIraiSansyoShonin.vue')
        },
        {
          path: re('/Reafs_W/WD00530/TeikiIraiSansyoShonin'),
          name: 'WD00530',
          component: () => import('@/views/Reafs-W/shonin/WD00530/TeikiIraiSansyoShoninTeiki.vue')
        },
        {
          path: re('/Reafs_W/Modal_SearchBukken'),
          name: 'WS00020',
          component: () => import('@/views/Reafs-W/search/WS00020/Modal_SearchBukken.vue')
        },
        {
          path: re('/Reafs_W/Modal_SearchTorihikisaki'),
          name: 'WS00030',
          component: () => import('@/views/Reafs-W/search/WS00030/Modal_SearchTorihikisaki.vue')
        },
        {
          path: re('/Reafs_W/Modal_SearchShain'),
          name: 'WS00040',
          component: () => import('@/views/Reafs-W/search/WS00040/Modal_SearchShain.vue')
        },
        {
          path: re('/Reafs_W/Modal_SearchMise'),
          name: 'WS00050',
          component: () => import('@/views/Reafs-W/search/WS00050/Modal_SearchMise.vue')
        },
        {
          path: re('/Reafs_W/Modal_SearchsSoshiki2'),
          name: 'WS00060',
          component: () => import('@/views/Reafs-W/search/WS00060/Modal_SearchsSoshiki2.vue')
        },
        {
          path: re('/Reafs_W/Modal_SearchKubun'),
          name: 'WS00080',
          component: () => import('@/views/Reafs-W/search/WS00080/Modal_SearchKubun.vue')
        },
        {
          path: re('/Reafs_W/IraiTenpuTouroku'),
          name: 'WD00120',
          component: () => import('@/views/Reafs-W/irai/WD00120/IraiTenpuTouroku.vue')
        },
        {
          path: re('/Reafs_W/HatchuShoUkesho'),
          name: 'WP00020',
          component: () => import('@/views/Reafs-W/tanpyo/WP00020/HatchuShoUkesho.vue')
        }
      ]
    },
    // Reafs-W機能ログイン
    {
      path: re('/Reafs_W/login'),
      name: 'WD00000',
      component: () => import('@/views/Reafs-W/common/WD00000/Login.vue'),
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
  if (store.getters.getRouterParams()) {
    const params = store.getters.getRouterParams()
    store.commit('setRouterParams', null)
    router.push({name: to.name, params})
    return
  }
  if (to.matched.length === 0) return next({ path: re('/404') })
  store.dispatch('onLoading', true)

  // 画面名設定
  await http
    .post('/api/Menu/getAllMenu', { user: 'info' })
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

  if (store.getters.isLogin() && to.name === 'WD00000' && store.getters.getIsBackToLogin() != '1' && to.query.redirect) {
    return next({ name: 'WD00010' })
  }
  store.commit('setIsBackToLogin', '0')
  if ((to.hasOwnProperty('meta') && to.meta.anonymous) || store.getters.isLogin()) {
    if (to.query.guid && to.name !== 'WD00000') {
      store.commit('setIsBackToLogin', '1')
      return next({ name: 'WD00000', query: { redirect: to.path, guid: to.query.guid } })
    } else {
      return next()
    }
  }
  next({ path: re('/Reafs_W/login'), query: { redirect: to.path, guid: to.query.guid } })
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
