import Vue from 'vue'
import Router from 'vue-router'
import MainLayout from '@/layout'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/',
    redirect: '/home',
    component: MainLayout,
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/home/index'),
        meta: {
          title: '首页',
          affix: true // 不关闭
        }
      }
    ]
  },
  {
    path: '/commit',
    component: MainLayout,
    children: [
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/commit/history'),
        meta: {
          title: '提交记录',
          affix: false
        }
      }
    ]
  },
  {
    path: '/settings',
    component: MainLayout,
    children: [
      {
        path: 'localProjectSetting',
        name: 'LocalProjectSetting',
        component: () => import('@/views/settings/localProjectSetting'),
        meta: {
          title: '本地测试项目设置',
          affix: false
        }
      },
      {
        path: 'serviceProjectSetting',
        name: 'ServiceProjectSetting',
        component: () => import('@/views/settings/serviceProjectSetting'),
        meta: {
          title: '项目文档设置',
          affix: false
        }
      }
    ]
  },
  {
    path: '*',
    redirect: '/home'
  }
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

/**
 * 重置路由
 */
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
