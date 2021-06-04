/**
 * 本地测试项目
 */
import { getLocalProjectData } from '@/api/localProject'
import { updateMethodData } from '@/api/methodData'
import {
  getProjectMethodGroupMap,
  getRoutesFromGroupMap,
  addProjectMethodDataRoutes
} from '@/utils/routerTool'

const state = {
  methodDataList: [], // 方法列表
  localProjectRoutes: [], // 用于存储测试项目的路由
  localProjectGroupList: [], // 组别名
  description: '',
  localProjectName: '', // 项目名，用于数据修改，唯一ID
  serviceNames: '', // 项目组其余服务方
  localServiceName: '', // 本地IP
  hostServiceName: '', // QuickApi数据管理服务端
  version: '', // 项目版本
  author: '',
  localPageData: {} // 缓存本地页面数据
}

const mutations = {
  SET_LOCAL_PROJECT_ROUTES: (state, localProjectRoutes) => {
    state.localProjectRoutes = localProjectRoutes
  },
  SET_LOCAL_METHOD_DATA_LIST: (state, methodDataList) => {
    state.methodDataList = methodDataList
  },
  SET_HOST_SERVICE_NAME: (state, hostName) => {
    state.hostServiceName = hostName
  },
  SET_LOCAL_SERVICE_NAME: (state, hostName) => {
    state.localServiceName = hostName
  },
  SET_TEAM_SERVICE_NAME: (state, hostName) => {
    state.teamServiceName = hostName
  },
  SET_LOCAL_PAGE_DATA: (state, page, status) => {
    state.localPageData[page] = status
  },
  SET_LOCAL_PROJECT_GROUP_LIST: (state, localProjectGroupList) => {
    state.localProjectGroupList = localProjectGroupList
  },
  SET_LOCAL_PROJECT_INFO: (state, data) => {
    state.methodDataList = data.methodDataList
    state.localServiceName = data.localServiceName
    state.localProjectName = data.projectName
    state.author = data.author
  }
}

const actions = {
  /** 获得本地项目数据，并将方法路由挂载到Router中 */
  setLocalProjectRoutes({ commit, dispatch }, data) {
    return new Promise((resolve, reject) => {
      getLocalProjectData(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '获取测试项目数据失败')
        }
        // // 是本地项目
        // dispatch('app/setLocalProjectFlag', true, { root: true })

        commit('SET_LOCAL_PROJECT_INFO', res.data.data)
        console.log('setLocalProjectRoutes.data', res.data.data)
        const groupMap = getProjectMethodGroupMap(res.data.data.methodDataList)
        commit('SET_LOCAL_PROJECT_GROUP_LIST', Object.keys(groupMap))

        const localProjectRoutes = getRoutesFromGroupMap(groupMap)
        commit('SET_LOCAL_PROJECT_ROUTES', localProjectRoutes)
        addProjectMethodDataRoutes(localProjectRoutes)

        resolve(localProjectRoutes)
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  },
  /** 修改接口信息 */
  updateProjectMethodData({ commit, state }, data) {
    return new Promise((resolve, reject) => {
      updateMethodData(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '更新失败')
        }
        const methodDataList = state.methodDataList
        for (const api of methodDataList) {
          if (api.url === data.url) {
            api.name = data.name
            api.group = data.methodGroup
            break
          }
        }
        commit('SET_LOCAL_METHOD_DATA_LIST', methodDataList)
        const groupMap = getProjectMethodGroupMap(methodDataList)
        commit('SET_LOCAL_PROJECT_GROUP_LIST', Object.keys(groupMap))

        // 左侧菜单监听localProjectRoutes并更新菜单
        const localProjectRoutes = getRoutesFromGroupMap(groupMap)
        commit('SET_LOCAL_PROJECT_ROUTES', localProjectRoutes)

        resolve(state.localProjectRoutes)
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
