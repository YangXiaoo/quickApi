import Vue from 'vue'
import Router from 'vue-router'
import MainLayout from '@/layout'

Vue.use(Router)

export const apiRouter = {
  path: '',
  name: '',
  component: MainLayout,
  children: [],
  meta: {
    title: ''
  }
}

export const childRouter = {
  path: 'index',
  name: 'index',
  component: () => import('@/views/request/index'),
  meta: {
    title: ''
  }
}

export const subRouter = {
  path: '',
  name: '',
  meta: {
    title: ''
  },
  children: []
}

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
    path: '*',
    redirect: '/home'
  }
]

const tmp = JSON.parse(JSON.stringify(childRouter))
constantRoutes[0].children.push(tmp)

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
