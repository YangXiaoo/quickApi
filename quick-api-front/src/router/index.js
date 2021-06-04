import Vue from 'vue'
import Router from 'vue-router'
import MainLayout from '@/layout'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/localApi',
    redirect: '/localApi/home',
    component: MainLayout,
    children: [
      {
        path: 'home',
        name: 'LocalApiHome',
        component: () => import('@/views/localApi/localApiHome'),
        meta: {
          title: '本地',
          affix: true // 不关闭
        }
      }
    ]
  },
  {
    path: '/userApi',
    redirect: '/userApi/home',
    component: MainLayout,
    children: [
      {
        path: 'home',
        name: 'UserApiHome',
        component: () => import('@/views/userApi/userApiHome'),
        meta: {
          title: '个人',
          affix: true // 不关闭
        }
      }
    ]
  },
  {
    path: '/projectApi',
    redirect: '/projectApi/home',
    component: MainLayout,
    children: [
      {
        path: 'home',
        name: 'ProjectApiHome',
        component: () => import('@/views/projectApi/projectApiHome'),
        meta: {
          title: '项目',
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
