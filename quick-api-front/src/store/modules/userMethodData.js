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
  saveUserMethodData({ commit, state, dispatch }, data) {
    return new Promise((resolve, reject) => {
      saveUserMethodData(data).then(res => {
        commit('ADD_USER_METHOD', data)

        // 左侧菜单栏需要监听routes，完成左侧菜单的更新
        dispatch('setRoutes', state.userMethodDataList)
        resolve(state.userRoutes)
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  },
  /** 更新用户方法信息 */
  updateUserMethodData({ commit, state, dispatch }, data) {
    return new Promise((resolve, reject) => {
      updateUserMethodData(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '更新失败')
        }
        console.log('updateUserMethodData.url', data.url)
        const methodDataList = state.userMethodDataList
        for (const methodData of methodDataList) {
          console.log('updateUserMethodData.methodData', methodData)
          if (methodData.url === data.url) {
            console.log('updateUserMethodData', data.url, '修改成功')
            methodData.methodName = data.methodName
            methodData.methodGroup = data.methodGroup
            break
          }
        }

        dispatch('setRoutes', methodDataList)
        console.log('updateUserMethodData.userRoutes', state.userRoutes)
        resolve(state.userRoutes)
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
  setUserMethodDataRoutes({ commit, state, dispatch }, data) {
    return new Promise((resolve, reject) => {
      getUserMethodDataList(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '获取接口数据失败')
        }
        const userMethodDataList = res.data.data
        commit('SET_USER_METHOD_LIST', userMethodDataList)

        dispatch('setRoutes', state.userMethodDataList)
        addUserMethodDataRoutes(state.userRoutes) // 将路由挂载到VUE的router中
        resolve(state.userRoutes)
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
