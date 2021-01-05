import MainLayout from '@/layout'
import router from '@/router'
import { endsWith } from './String'

/**
 * 从api信息中获得路由
 * @param {Object} apiInfo 
 * @author yangxiao
 */
export function getRoutes(apiInfo) {
	const groupMap = getGroup(apiInfo)
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
	for (let key in groupMap) {
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
		for (let api of groupMap[key]) {
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
 * @param {Object} apiInfo 
 * @author yangxiao
 */
export function getTeamRoutes(apiInfo) {
	const groupMap = getGroup(apiInfo)
	const routes = []
	let groupIndex = 0
	for (let key in groupMap) {
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
		for (let api of groupMap[key]) {
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
	console.log(routes)
	return routes
}

/**
 * 获得路由的映射数据
 * @param {*} apiInfo 
 */
export function getGroup(apiInfo) {
	const data = {}
	for (let api of apiInfo) {
		if (api.group in data) {
			data[api.group].push(api)
		} else {
			data[api.group] = [api]
		}
	}

	return data
}
/**
 * 设置路由
 * @param {Object} routes 
 */
export function setRoutes(routes) {
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
	for (let api of apiInfo) {
		console.log(api)
		if (api.url === path) {
			api.name = methodName
			api.group = methodGroup

			break
		}
	}
}