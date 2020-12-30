import { apiRouter, subRouter, childRouter } from '@/router'

/**
 * 从api信息中获得路由
 * @param {Object} apiInfo 
 * @author yangxiao
 */
export function getRoutes(apiInfo) {
	const routes = []
	for (let key in apiInfo) {
		const group = apiInfo[key]
		if (group) {
			const curRouter = Object.create(apiRouter)
			curRouter.path = '/' + key
			curRouter.name = key
			
			// 组的大小为一个则只有一个子路由，将子路由作为此组
			if (group.length == 1) {
				curRouter.path = '/' + group.value
				let subGroup = group[0]
				// 子路由的方法加入children中
				if (subGroup.methodModels) {
					for (let child of subGroup) {
						curChildRouter = Object.create(childRouter)
						curChildRouter.path = child.value
						curChildRouter.name = child.name
						curChildRouter.meta.title = child.name

						curRouter.children.push(curChildRouter)
					}

					routes.push(curRouter)
				}
			} else {
				// 组的大小超过一个，则有两级菜单
				for (let subGroup of group) {
					if (subGroup) {
						curSubRouter = Object.create(subRouter)
						curSubRouter.path = subGroup.value
						curSubRouter.name = subGroup.name
						if (subGroup.methodModels) {
							for (let child of subGroup) {
								curChildRouter = Object.create(childRouter)
								curChildRouter.path = child.value
								curChildRouter.name = child.name
								curChildRouter.meta.title = child.name
	
								curSubRouter.children.push(curChildRouter)
							}
						}
	
						curRouter.children.push(curSubRouter)
					}
				}

				routes.push(curRouter)
			}
		}
	}

	return routes
}