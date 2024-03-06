import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import http from '@/api/http'

// import redirect from '../redirect'
Vue.use(Router)

// ElementUI
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch((err) => err)
}

const re = http.resolve
const router = new Router({
  mode: 'history',
  routes: [
    // duolingo
    {
      path: re('/duolingo/'),
      name: 'Index_Duo',
      component: () => import('@/views/IndexDuo.vue'), // DuoINDEX
      redirect: re('/duolingo/Learn'),
      children: [
        {
          path: re('/duolingo/Learn'),
          name: 'Duolearn',
          component: () => import('@/views/duolingo/home/learn.vue')
        },
        {
          path: re('/Duolingo/MainMenu'),
          name: 'DuoD00001',
          component: () => import('@/views/duolingo/common/TD00001/MainMenu.vue')
        },
        {
          path: re('/Duolingo/OshiraseHyouji'),
          name: 'DuoD00002',
          component: () => import('@/views/duolingo/common/TD00002/OshiraseHyouji.vue')
        },
        {
          path: re('/Duolingo/YuzaJohoHenko'),
          name: 'DuoD00003',
          component: () => import('@/views/duolingo/common/TD00003/YuzaJohoHenko.vue')
        },
        {
          path: re('/Duolingo/GyoshaYuzaToroku'),
          name: 'DuoD00004',
          component: () => import('@/views/duolingo/common/TD00004/GyoshaYuzaToroku.vue')
        },
        {
          path: re('/Duolingo/ShokaiTouroku'),
          name: 'DuoD00005',
          component: () => import('@/views/duolingo/common/TD00005/ShokaiTouroku.vue')
        },
        /* {
          path: '/Duolingo/PasuwadoRimainda',
          name: 'DuoD00006',
          component: () => import('@/views/duolingo/common/TD00006/PasuwadoRimainda.vue')
        }, */
        {
          // 
          path: re('/Duolingo/KojiIchiran'),
          name: 'DuoD00100',
          component: () => import('@/views/duolingo/koji/TD00100/KojiIchiran.vue')
        },
        {
          path: re('/Duolingo/SagyoHokokuNyuryoku'),
          name: 'DuoD00101',
          component: () => import('@/views/duolingo/common/TD00101/SagyoHokokuNyuryoku.vue')
        },
        {
          // 
          path: re('/Duolingo/MitsumoriIraiIchiran'),
          name: 'DuoD00300',
          component: () => import('@/views/duolingo/mitsumori/TD00300/MitsumoriIraiIchiran.vue')
        },
        {
          path: re('/Duolingo/SyuzenMitsumoriNyuryoku'),
          name: 'DuoD00301',
          component: () => import('@/views/duolingo/mitsumori/TD00301/SyuzenMitsumoriNyuryoku.vue')
        },
        {
          // 
          path: re('/Duolingo/TeikiMitsumoriNyuryoku'),
          name: 'DuoD00302',
          component: () => import('@/views/duolingo/mitsumori/TD00302/TeikiMitsumoriNyuryoku.vue')
        },
        {
          // 
          path: re('/Duolingo/SagyoJokyoIchiran'),
          name: 'DuoD00400',
          component: () => import('@/views/duolingo/sagyo/TD00400/SagyoJokyoIchiran.vue')
        },
        {
          // 
          path: re('/Duolingo/SeikyuGoiIchiran'),
          name: 'DuoD00500',
          component: () => import('@/views/duolingo/seikyu/TD00500/SeikyuGoiIchiran.vue')
        },
        {
          path: re('/Duolingo/DdocumentKensaku'),
          name: 'DuoD00600',
          component: () => import('@/views/duolingo/document/TD00600/DdocumentKensaku.vue')
        },
        {
          path: re('/Duolingo/KakoMitsumoriSanshoSabu'),
          name: 'DuoS00300',
          component: () => import('@/views/duolingo/mitsumori/TS00300/KakoMitsumoriSanshoSabu.vue')
        },
        {
          path: re('/Duolingo/IraiNaiyoSabu'),
          name: 'DuoS00301',
          component: () => import('@/views/duolingo/mitsumori/TS00301/IraiNaiyoSabu.vue')
        },
        {
          path: re('/Duolingo/ZumenShiryoTenpuSabu'),
          name: 'DuoS00302',
          component: () => import('@/views/duolingo/mitsumori/TS00302/ZumenShiryoTenpuSabu.vue')
        },
        {
          path: re('/Duolingo/ShashinTenpuSabu'),
          name: 'DuoS00303',
          component: () => import('@/views/duolingo/mitsumori/TS00303/ShashinTenpuSabu.vue')
        },
        {
          path: re('/Duolingo/BukkenDokyumentoSanshoSabu'),
          name: 'DuoS00304',
          component: () => import('@/views/duolingo/mitsumori/TS00304/BukkenDokyumentoSanshoSabu.vue')
        },
        {
          path: re('/Duolingo/MitsumoriDetaTorikomi'),
          name: 'DuoS00305',
          component: () => import('@/views/duolingo/mitsumori/TS00305/MitsumoriDetaTorikomi.vue')
        },
        {
          path: re('/Duolingo/TeikiMitsumoriSyosaiNyuryoku'),
          name: 'DuoS00306',
          component: () => import('@/views/duolingo/mitsumori/TS00306/TeikiMitsumoriNyuryoku_sagyo.vue')
        },
        {
          path: re('/Duolingo/TeikiMitsumoriIraiSansho'),
          name: 'DuoS00308',
          component: () => import('@/views/duolingo/mitsumori/TD00302/TeikiMitsumoriNyuryoku_irai.vue')
        },
        {
          path: re('/Duolingo/TeikiMitsumoriDataTorikomi'),
          name: 'DuoS00315',
          component: () => import('@/views/duolingo/mitsumori/TD00302/TeikiMitsumoriDateTorikomi.vue')
        },
        {
          path: re('/Duolingo/TeikiSagyoYoteiNyuryoku'),
          name: 'DuoS00401',
          component: () => import('@/views/duolingo/sagyo/TS00401/TeikiSagyoYoteiNyuryoku.vue')
        },
        {
          path: re('/Duolingo/TeikiSagyoHokokuNyuryoku'),
          name: 'DuoS00402',
          component: () => import('@/views/duolingo/sagyo/TS00402/TeikiSagyoHokokuNyuryoku.vue')
        }
      ]
    },
    {
      path: re('/Duolingo/login'),
      name: 'DuoD00000',
      component: () => import('@/views/duolingo/common/TD00000/Login.vue'),
      meta: {
        anonymous: true
      }
    },
    {
      path: re('/Duolingo/PasuwadoRimainda'),
      name: 'DuoD00006',
      component: () => import('@/views/duolingo/common/TD00006/PasuwadoRimainda.vue'),
      meta: {
        anonymous: true
      }
    },
    //* 404
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

  // 
  await http
    .post('api/Duolingo/Common/getAllMenu', { user: 'info' })
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
  if (store.getters.isLogin() && to.name === 'DuoD00000' && store.getters.getIsBackToLogin() != '1' && to.query.redirect) {
    return next({ name: 'DuoD00001' })
  }
  store.commit('setIsBackToLogin', '0')
  if ((to.hasOwnProperty('meta') && to.meta.anonymous) || store.getters.isLogin()) {
    if (to.query.guid && to.name !== 'DuoD00000') {
      store.commit('setIsBackToLogin', '1')
      return next({ name: 'DuoD00000', query: { redirect: to.path, guid: to.query.guid } })
    } else {
      return next()
    }
  }
  if (to.matched[0].path === 'learn') {
    next({ path: re('/Duolingo/learn'), query: { redirect: Math.random() } })
  } else {
    next({ path: re('/Duolingo/login'), query: { redirect: to.path, guid: to.query.guid } })
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
