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
    // Reafs-T機能
    {
      path: re('/Reafs_T/'),
      name: 'Index_T',
      component: () => import('@/views/IndexT.vue'), // T用のINDEXを利用する
      redirect: re('/Reafs_T/MainMenu'),
      children: [
        {
          path: re('/Reafs_T/home'),
          name: 'Thome',
          component: () => import('@/views/Reafs-T/common/TD00001/MainMenu.vue')
        },
        {
          path: re('/Reafs_T/MainMenu'),
          name: 'TD00001',
          component: () => import('@/views/Reafs-T/common/TD00001/MainMenu.vue')
        },
        {
          path: re('/Reafs_T/OshiraseHyouji'),
          name: 'TD00002',
          component: () => import('@/views/Reafs-T/common/TD00002/OshiraseHyouji.vue')
        },
        {
          path: re('/Reafs_T/YuzaJohoHenko'),
          name: 'TD00003',
          component: () => import('@/views/Reafs-T/common/TD00003/YuzaJohoHenko.vue')
        },
        {
          path: re('/Reafs_T/GyoshaYuzaToroku'),
          name: 'TD00004',
          component: () => import('@/views/Reafs-T/common/TD00004/GyoshaYuzaToroku.vue')
        },
        {
          path: re('/Reafs_T/ShokaiTouroku'),
          name: 'TD00005',
          component: () => import('@/views/Reafs-T/common/TD00005/ShokaiTouroku.vue')
        },
        /* {
          path: '/Reafs_T/PasuwadoRimainda',
          name: 'TD00006',
          component: () => import('@/views/Reafs-T/common/TD00006/PasuwadoRimainda.vue')
        }, */
        {
          // 工事依頼一覧
          path: re('/Reafs_T/KojiIchiran'),
          name: 'TD00100',
          component: () => import('@/views/Reafs-T/koji/TD00100/KojiIchiran.vue')
        },
        {
          path: re('/Reafs_T/SagyoHokokuNyuryoku'),
          name: 'TD00101',
          component: () => import('@/views/Reafs-T/common/TD00101/SagyoHokokuNyuryoku.vue')
        },
        {
          // 見積依頼一覧
          path: re('/Reafs_T/MitsumoriIraiIchiran'),
          name: 'TD00300',
          component: () => import('@/views/Reafs-T/mitsumori/TD00300/MitsumoriIraiIchiran.vue')
        },
        {
          path: re('/Reafs_T/SyuzenMitsumoriNyuryoku'),
          name: 'TD00301',
          component: () => import('@/views/Reafs-T/mitsumori/TD00301/SyuzenMitsumoriNyuryoku.vue')
        },
        {
          // 定期見積入力
          path: re('/Reafs_T/TeikiMitsumoriNyuryoku'),
          name: 'TD00302',
          component: () => import('@/views/Reafs-T/mitsumori/TD00302/TeikiMitsumoriNyuryoku.vue')
        },
        {
          // 作業状況一覧
          path: re('/Reafs_T/SagyoJokyoIchiran'),
          name: 'TD00400',
          component: () => import('@/views/Reafs-T/sagyo/TD00400/SagyoJokyoIchiran.vue')
        },
        {
          // 請求合意一覧
          path: re('/Reafs_T/SeikyuGoiIchiran'),
          name: 'TD00500',
          component: () => import('@/views/Reafs-T/seikyu/TD00500/SeikyuGoiIchiran.vue')
        },
        {
          path: re('/Reafs_T/DdocumentKensaku'),
          name: 'TD00600',
          component: () => import('@/views/Reafs-T/document/TD00600/DdocumentKensaku.vue')
        },
        {
          path: re('/Reafs_T/KakoMitsumoriSanshoSabu'),
          name: 'TS00300',
          component: () => import('@/views/Reafs-T/mitsumori/TS00300/KakoMitsumoriSanshoSabu.vue')
        },
        {
          path: re('/Reafs_T/IraiNaiyoSabu'),
          name: 'TS00301',
          component: () => import('@/views/Reafs-T/mitsumori/TS00301/IraiNaiyoSabu.vue')
        },
        {
          path: re('/Reafs_T/ZumenShiryoTenpuSabu'),
          name: 'TS00302',
          component: () => import('@/views/Reafs-T/mitsumori/TS00302/ZumenShiryoTenpuSabu.vue')
        },
        {
          path: re('/Reafs_T/ShashinTenpuSabu'),
          name: 'TS00303',
          component: () => import('@/views/Reafs-T/mitsumori/TS00303/ShashinTenpuSabu.vue')
        },
        {
          path: re('/Reafs_T/BukkenDokyumentoSanshoSabu'),
          name: 'TS00304',
          component: () => import('@/views/Reafs-T/mitsumori/TS00304/BukkenDokyumentoSanshoSabu.vue')
        },
        {
          path: re('/Reafs_T/MitsumoriDetaTorikomi'),
          name: 'TS00305',
          component: () => import('@/views/Reafs-T/mitsumori/TS00305/MitsumoriDetaTorikomi.vue')
        },
        {
          path: re('/Reafs_T/TeikiMitsumoriSyosaiNyuryoku'),
          name: 'TS00306',
          component: () => import('@/views/Reafs-T/mitsumori/TS00306/TeikiMitsumoriNyuryoku_sagyo.vue')
        },
        {
          path: re('/Reafs_T/TeikiMitsumoriIraiSansho'),
          name: 'TS00308',
          component: () => import('@/views/Reafs-T/mitsumori/TD00302/TeikiMitsumoriNyuryoku_irai.vue')
        },
        {
          path: re('/Reafs_T/TeikiMitsumoriDataTorikomi'),
          name: 'TS00315',
          component: () => import('@/views/Reafs-T/mitsumori/TD00302/TeikiMitsumoriDateTorikomi.vue')
        },
        {
          path: re('/Reafs_T/TeikiSagyoYoteiNyuryoku'),
          name: 'TS00401',
          component: () => import('@/views/Reafs-T/sagyo/TS00401/TeikiSagyoYoteiNyuryoku.vue')
        },
        {
          path: re('/Reafs_T/TeikiSagyoHokokuNyuryoku'),
          name: 'TS00402',
          component: () => import('@/views/Reafs-T/sagyo/TS00402/TeikiSagyoHokokuNyuryoku.vue')
        }
      ]
    },
    {
      path: re('/Reafs_T/login'),
      name: 'TD00000',
      component: () => import('@/views/Reafs-T/common/TD00000/Login.vue'),
      meta: {
        anonymous: true
      }
    },
    {
      path: re('/Reafs_T/PasuwadoRimainda'),
      name: 'TD00006',
      component: () => import('@/views/Reafs-T/common/TD00006/PasuwadoRimainda.vue'),
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
    .post('api/Reafs_T/Common/getAllMenu', { user: 'info' })
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
  if (store.getters.isLogin() && to.name === 'TD00000' && store.getters.getIsBackToLogin() != '1' && to.query.redirect) {
    return next({ name: 'TD00001' })
  }
  store.commit('setIsBackToLogin', '0')
  if ((to.hasOwnProperty('meta') && to.meta.anonymous) || store.getters.isLogin()) {
    if (to.query.guid && to.name !== 'TD00000') {
      store.commit('setIsBackToLogin', '1')
      return next({ name: 'TD00000', query: { redirect: to.path, guid: to.query.guid } })
    } else {
      return next()
    }
  }
  if (to.matched[0].path === 'home') {
    next({ path: re('/Reafs_T/home'), query: { redirect: Math.random() } })
  } else {
    next({ path: re('/Reafs_T/login'), query: { redirect: to.path, guid: to.query.guid } })
  }
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
