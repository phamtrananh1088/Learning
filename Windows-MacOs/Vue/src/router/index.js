import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import http from '../api/http'

import redirect from './redirect'
Vue.use(Router)
const re = http.resolve

const router = new Router({
  mode: 'history',
  routes: [
    // テスト機能
    {
      path: re('/'),
      name: 'Index',
      component: () => import('@/views/Index'),
      redirect: re('/home'),
      children: [
        ...redirect,
        {
          path: re('/home'),
          name: 'home',
          component: () => import('@/views/home.vue')
        }, {
          path: re('/home2'),
          name: 'home2',
          component: () => import('@/views/home2.vue')
        }, {
          path: re('/form'),
          name: 'form',
          component: () => import('@/views/Form.vue')
        }, {
          path: re('/form2'),
          name: 'form2',
          component: () => import('@/views/Form2.vue')
        }, {
          path: re('/Grid'),
          name: 'grid',
          component: () => import('@/views/Grid.vue')
        }, {
          path: re('/Grid2'),
          name: 'grid2',
          component: () => import('@/views/Grid2.vue')
        }, {
          path: re('/syuzenIraiSansyoShonin'),
          name: 'syuzenIraiSansyoShonin',
          component: () => import('@/views/WD00100/syuzenIraiSansyoShonin.vue')
        }
      ]
    },
    // テスト機能ログイン
    {
      path: re('/login'),
      name: 'login',
      component: () => import('@/views/Login.vue'),
      meta: {
        anonymous: true
      }
    },
    // Reafs-T機能
    {
      path: re('/Reafs_T'),
      name: 'Index_T',
      component: () => import('@/views/IndexT.vue'), // T用のINDEXを利用する
      redirect: re('/Reafs_T/home'),
      children: [
        ...redirect,
        {
          path: re('/Reafs_T/home'),
          name: 'Thome',
          component: () => import('@/views/home.vue')
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
        {
          path: re('/Reafs_T/PasuwadoRimainda'),
          name: 'TD00006',
          component: () => import('@/views/Reafs-T/common/TD00006/PasuwadoRimainda.vue')
        },
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
          component:() => import('@/views/Reafs-T/mitsumori/TD00302/TeikiMitsumoriNyuryoku_irai.vue')
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
    {
      path: re('/Reafs_W'),
      name: 'Index_W',
      component: () => import('@/views/IndexW.vue'), // W用のINDEXを利用する
      redirect: re('/Reafs_W/home'),
      children: [
        ...redirect,
        {
          path: re('/Reafs_W/home'),
          name: 'WD00010',
          component: () => import('@/views/Reafs-W/common/WD00010/Top.vue')
        },
        {
          path: re('/Reafs_W/common/Top'),
          name: 'WD00010',
          component: () => import('@/views/Reafs-W/common/WD00010/Top.vue')
        },
        {
          path: re('/Reafs_W/common/WD00011/OsiraseIchiran'),
          name: 'WD00011',
          component: () => import('@/views/Reafs-W/common/WD00011/OsiraseIchiran.vue')
        },
        {
          path: re('/Reafs_W/common/WD00012/OsiraseIchiran'),
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
          component: () => import('@/views/Reafs-W/shonin/WD00530/TeikiIraiSansyoShonin.vue')
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
    // Reafs-R-Web機能
    {
      path: re('/Reafs_R_Web'),
      name: 'Index_R',
      component: () => import('@/views/IndexR.vue'), // Mobile用のINDEXを利用する
      redirect: re('/Reafs_R_Web/mobile/Top'),
      children: [
        ...redirect,
        {
          path: re('/Reafs_R_Web/mobile'),
          name: 'Rhome',
          component: () => import('@/views/home.vue')
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
    // MobileSample
    {
      path: re('/Mobile'),
      name: 'Index',
      component: () => import('@/views/Index'),
      redirect: re('/Mobile/home'),
      children: [
        ...redirect,
        {
          path: re('/Mobile/home'),
          name: 'home',
          component: () => import('@/views/RD00801/home.vue')
        }
      ]
    },
    {
      path: re('/Mobile/login'),
      name: 'login',
      component: () => import('@/views/RD00800/Login.vue'),
      meta: {
        anonymous: true
      }
    },
    //* 404は最後
    {
      path: '*',
      component: () => import('@/views/redirect/404.vue')
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  store.getters.getUserInfo()
  if (to.matched.length === 0) return next({ path: re('/404') })
  store.dispatch('onLoading', true)

  let p = '/api/Menu/getAllMenu'
  if (to.matched[0].path === re('/Reafs_R_Web')) {
    p = '/api/Reafs_R/Mobile/getAllMenu'
  } else if (to.matched[0].path === re('/Reafs_T')) {
    p = '/api/Reafs_T/Common/getAllMenu'
  } else {
    p = '/api/Menu/getAllMenu'
  }
  // 画面名設定
  await http
    .post(p, { user: 'info' })
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

  if ((to.hasOwnProperty('meta') && to.meta.anonymous) || store.getters.isLogin()) {
    // ログイン状態で遷移処理を行う
    return next()
  }
  if (to.matched[0].path === re('/Reafs_W')) {
    return next({ name: 'WD00000', query: { redirect: Math.random() } })
  } else if (to.matched[0].path === re('/Reafs_R_Web')) {
    if (to.matched[1].path === re('/Reafs_R_Web/mobile/KinkyuKougaku')) {
      next({ name: 'RD00800', query: to.query })
    } else {
      next({ name: 'RD00800', query: { redirect: Math.random() } })
    }
  } else if (to.matched[0].path === re('/Reafs_T')) {
    next({ name: 'TD00000', query: { redirect: Math.random() } })
  } else {
    return next({ path: re('/login'), query: { redirect: Math.random() } })
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
  if (isChunkLoadFailed) {
    window.location.replace(window.location.href)
  }
})

export default router
