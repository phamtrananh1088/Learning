import http from '@/api/http'
const re = http.resolve
let redirect = [{
    path: re('/404'),
    name: '404',
    component: () => import('@/views/redirect/404.vue'),
    meta:{
        anonymous:true
      }
}, {
    path: re('/401'),
    name: '401',
    component: () => import('@/views/redirect/401.vue')
}, {
    path: re('/coding'),
    name: 'coding',
    component: () => import('@/views/redirect/coding.vue')
}]
export default redirect;