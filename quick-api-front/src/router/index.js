import Vue from 'vue'
import Router from 'vue-router'
import MainLayout from '@/layout'

Vue.use(Router)

export const apiRouter = {
  path: '',
  name: '',
  component: MainLayout,
  children: []
}

export const childRouter = {
  path: '',
  name: '',
  component: () => import('@/views/test/index'),
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
    redirect: '/test',
    component: MainLayout,
    children: [
      {
        path: 'test',
        name: 'Test',
        component: () => import('@/views/test/index'),
        meta: {
          title: '测试'
        }
      }
    ]
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter () {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
