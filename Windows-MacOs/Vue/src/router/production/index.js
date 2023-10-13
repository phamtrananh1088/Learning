import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import http from '@/api/http'

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
          component: () => import('@/views/Home.vue')
        }, {
          path: re('/home2'),
          name: 'home2',
          component: () => import('@/views/Home2.vue')
        }, {
          path: re('/form'),
          name: 'form',
          component: () => import('@/views/Form.vue')
        },{
          path: re('/form2'),
          name: 'form2',
          component: () => import('@/views/Form2.vue')
        },{
          path: re('/Grid'),
          name: 'grid',
          component: () => import('@/views/Grid.vue')
        },{
          path: re('/Grid2'),
          name: 'grid2',
          component: () => import('@/views/Grid2.vue')
        },{
          path: re('/syuzenIraiSansyoShonin'),
          name: 'syuzenIraiSansyoShonin',
          component: () => import('@/views/WD00100/syuzenIraiSansyoShonin.vue')
        },
        {
          path: re('/rd00802'),
          name: 'rd00802',
          component: () => import('@/views/RD00802/rd00802.vue')
        },
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
    // Reafs-W機能
    {
      path: re('/Reafs_W'),
      name: 'WIndex',
      component: () => import('@/views/PcIndex'), // Pc用のINDEXを利用する
      redirect: re('/Reafs_W/home'),
      children: [
        ...redirect,
        {
          path: re('/Reafs_W/home'),
          name: 'Whome',
          component: () => import('@/views/Home.vue')
        },
        {
          path: re('/Reafs_W/common/Top'),
          name: 'WD00001',
          component: () => import('@/views/Reafs-W/common/WD00010/Top.vue')
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
      name: 'RIndex',
      component: () => import('@/views/MbIndex'), // Mobile用のINDEXを利用する
      redirect: re('/Reafs_R_Web/mobile/Top'),
      children: [
        ...redirect,
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
      component: () => import('@/views/Index.vue'),
      redirect: re('/Mobile/home'),
      children: [
        ...redirect,
        {
          path: re('/Mobile/home'),
          name: 'home',
          component: () => import('@/views/RD00801/Home.vue')
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
    },
  ]
})


router.beforeEach((to, from, next) => {
  store.getters.getUserInfo()
  if (to.matched.length == 0) return next({ path: re('/404') });
  store.dispatch("onLoading", true);
  if ((to.hasOwnProperty('meta') && to.meta.anonymous) || store.getters.isLogin()) {
    return next();
  }
  if (to.matched[0].path === re('/Reafs_W')) {
    next({ name: 'WD00000', query: { redirect: Math.random() } });
  } else if(to.matched[0].path === re('/Reafs_R_Web')) {
    next({ name: 'RD00800', query: { redirect: Math.random() } });
  } else if(to.matched[0].path === re('/Reafs_T')) {
    next({ name: 'RD00800', query: { redirect: Math.random() } });
  } else {
    next({ path: re('/login'), query: { redirect: Math.random() } });
  }  
})

router.afterEach((to, from) => {
  store.dispatch("onLoading", false);
})
router.onError((error) => {
  const pattern = /Loading chunk (\d)+ failed/g;
  const isChunkLoadFailed = error.message.match(pattern);
  const targetPath = router.history.pending.fullPath;
  console.log(error.message);
  console.log(targetPath);
  if (isChunkLoadFailed) {
    window.location.replace(window.location.href);
  }
});

export default router;
