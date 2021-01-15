import {
  getUserMethodGroupMap,
  getUserRoutesFromMthodGroupMap,
  addUserMethodDataRoutes
} from '@/utils/routerTool'

import {
  getUserMethodDataList,
  updateUserMethodData,
  saveUserMethodData
} from '@/api/methodData'

const state = {
  userMethodDataList: [],
  userGroupList: [],
  userRoutes: []
}

const mutations = {
  ADD_USER_METHOD: (state, methodData) => {
    state.userMethodDataList.push(methodData)
  },
  SET_USER_METHOD_LIST: (state, methodDataList) => {
    state.userMethodDataList = methodDataList
  },
  SET_USER_GROUP_LIST: (state, groupList) => {
    state.userGroupList = groupList
  },
  ADD_USER_GROUP: (state, methodGroup) => {
    state.userGroupList.push(methodGroup)
  },
  SET_USER_ROUTES: (state, routes) => {
    state.userRoutes = routes
  }
}

const actions = {
  /** 保存用户的方法信息 */
  saveUserMethodData({ commit, state }, data) {
    return new Promise((resolve, reject) => {
      saveUserMethodData(data).then(res => {
        commit('ADD_USER_METHOD', data)

        resolve(this.setRoutes(state.userMethodDataList)) // 左侧菜单栏需要监听此数据，完成左侧菜单的更新
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  },
  /** 更新用户方法信息 */
  updateMethodData({ commit, state }, data) {
    return new Promise((resolve, reject) => {
      updateUserMethodData(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '更新失败')
        }

        const userMethodDataList = state.userMethodDataList
        for (const methodData in userMethodDataList) {
          if (methodData.url === data.url) {
            methodData.methodName = data.methodName
            methodData.methodGroup = data.methodGroup
            break
          }
        }

        resolve(this.setRoutes(userMethodDataList))
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  },
  /** 获得用户的接口方法 */
  getUserMethodDataList({ commit }, data) {
    return new Promise((resolve, reject) => {
      getUserMethodDataList(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '获取接口数据失败')
        }
        const methodDataList = res.data.data
        commit('SET_USER_METHOD_LIST', methodDataList)

        resolve(methodDataList)
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  },
  /** 设置用户路由 */
  setUserMethodDataRoutes({ commit, state }, data) {
    return new Promise((resolve, reject) => {
      getUserMethodDataList(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '获取接口数据失败')
        }
        const userMethodDataList = res.data.data
        commit('SET_USER_METHOD_LIST', userMethodDataList)

        const groupMap = getUserMethodGroupMap(data)
        commit('SET_USER_GROUP_LIST', Object.keys(groupMap))

        const routes = this.setRoutes(userMethodDataList)
        addUserMethodDataRoutes(routes) // 将路由挂载到VUE的router中
        resolve(routes)
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  },
  setRoutes({ commit }, data) {
    const groupMap = getUserMethodGroupMap(data)
    commit('SET_USER_GROUP_LIST', Object.keys(groupMap))

    const routes = getUserRoutesFromMthodGroupMap(groupMap)
    commit('SET_USER_ROUTES', routes)

    return routes
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
