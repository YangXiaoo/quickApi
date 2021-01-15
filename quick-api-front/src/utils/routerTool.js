import MainLayout from '@/layout'
import router from '@/router'
import { getUUID32 } from './uuid'
/**
 * 从api信息中获得路由
 * @param {Object} apiInfo
 * @author yangxiao
 */
export function getRoutes(apiInfo) {
  const groupMap = getProjectMethodGroupMap(apiInfo)
  return getRoutesFromGroupMap(groupMap)
}

/**
 * 从api信息中获得路由
 * @param {Object} groupMap
 * @author yangxiao
 */
export function getRoutesFromGroupMap(groupMap) {
  const routes = []
  let groupIndex = 0
  for (const key in groupMap) {
    const curRouter = {
      path: '/local-',
      component: MainLayout,
      children: [],
      meta: {
        title: ''
      }
    }

    // 设置菜单组别名称
    curRouter.path += groupIndex
    curRouter.meta.title = key

    // 设置子菜单
    for (const api of groupMap[key]) {
      const curChildRouter = {
        path: '',
        name: '',
        component: () => import('@/views/request/index'),
        meta: {
          title: '',
          group: ''
        }
      }
      curChildRouter.path = api.url.substring(1)
      curChildRouter.name = api.methodName
      curChildRouter.meta.title = api.name // 设置子菜单名称
      curChildRouter.meta.group = api.group // 设置子菜单名称

      curRouter.children.push(curChildRouter)
    }

    routes.push(curRouter)
    groupIndex += 1
  }

  console.log(routes)
  return routes
}

/**
 * 从api信息中获得路由
 */
export function getProjectRoutesFromMethodDataList(apiInfo) {
  const groupMap = getProjectMethodGroupMap(apiInfo)
  const routes = []
  let groupIndex = 0
  for (const key in groupMap) {
    const curRouter = {
      path: '/team-',
      component: MainLayout,
      children: [],
      meta: {
        title: '',
        group: ''
      }
    }

    // 设置菜单组别名称
    curRouter.path += groupIndex
    curRouter.meta.title = key

    // 设置子菜单
    for (const api of groupMap[key]) {
      const curChildRouter = {
        path: '',
        name: '',
        component: () => import('@/views/team/index'),
        meta: {
          title: ''
        }
      }
      curChildRouter.path = api.url.substring(1)
      curChildRouter.name = api.methodName
      curChildRouter.meta.title = api.name
      curChildRouter.meta.group = api.group

      curRouter.children.push(curChildRouter)
    }

    routes.push(curRouter)
    groupIndex += 1
  }

  return routes
}

/**
 * 获得项目路由的映射数据
 * @param {*} apiInfo
 */
export function getProjectMethodGroupMap(apiInfo) {
  const data = {}
  for (const api of apiInfo) {
    if (api.group in data) {
      data[api.group].push(api)
    } else {
      data[api.group] = [api]
    }
  }

  return data
}

/** 将用户拥有的方法按所属组分类 */
export function getUserMethodGroupMap(methodDataList) {
  const methodGroupMap = {}

  methodDataList.map(item => {
    if (item.methodGroup in methodGroupMap) {
      methodGroupMap[item.methodGroup].push(item)
    } else {
      methodGroupMap[item.methodGroup] = [item]
    }
  })
}
/**
 * 设置路由
 * @param {Object} routes
 */
export function setRoutes(routes) {
  router.addRoutes(routes)
}

/** 添加个人方法路由 */
export function addUserMethodDataRoutes(routes) {
  router.addRoutes(routes)
}

/**
 * 修改接口信息
 * @param {*} apiInfo
 * @param {*} path
 * @param {*} methodName
 * @param {*} methodGroup
 */
export function changeApiInfo(apiInfo, path, methodName, methodGroup) {
  for (const api of apiInfo) {
    if (api.url === path) {
      api.name = methodName
      api.group = methodGroup

      break
    }
  }
}

/**
 * 生成一个页面的路由
 */
export function generatePage() {
  const pageUrl = getUUID32()
  const curRouter = {
    path: pageUrl,
    component: MainLayout,
    children: [],
    meta: {
      title: 'undefined-class',
      group: 'undefined'
    }
  }
  const curChildRouter = {
    path: pageUrl,
    name: pageUrl,
    component: () => import('@/views/tab/index'),
    meta: {
      title: 'undefined'
    }
  }
  curRouter.children.push(curChildRouter)
  setRoutes([curRouter])

  return pageUrl
}

/** 生成用户方法的路由 */
export function getUserRoutesFromMthodGroupMap(methodGroupMap) {
  const routes = []
  let groupIndex = 0
  for (const key in methodGroupMap) {
    const curRouter = {
      path: '/user-',
      component: MainLayout,
      children: [],
      meta: {
        title: ''
      }
    }

    // 设置菜单组别名称
    curRouter.path += groupIndex
    curRouter.meta.title = key

    // 设置子菜单
    for (const methodData of methodGroupMap[key]) {
      const curChildRouter = {
        path: '',
        name: '',
        component: () => import('@/views/tab/index'),
        meta: {
          title: '',
          group: ''
        }
      }
      curChildRouter.path = methodData.url
      curChildRouter.name = methodData.url
      curChildRouter.meta.title = methodData.methodName // 设置子菜单名称
      curChildRouter.meta.group = methodData.methodGroup // 设置子菜单名称

      curRouter.children.push(curChildRouter)
    }

    routes.push(curRouter)
    groupIndex += 1
  }

  return routes
}
