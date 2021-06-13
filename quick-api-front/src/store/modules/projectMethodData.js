/**
 * 项目方法信息
 */
import {
  getMethodDataByProjectName
} from '@/api/methodData'
import {
  getProjectRoutesFromMethodDataList,
  addProjectMethodDataRoutes
} from '@/utils/routerTool'

const state = {
  projectMethodDataList: {}, // 项目名: [方法列表]
  projectRoutes: {} // 项目名: [项目路由]
}

const mutations = {
  SET_PROJECT_METHOD_LIST: (state, { projectName, methodDataList }) => {
    state.projectMethodDataList[projectName] = methodDataList
  },
  SET_PROJECT_ROUTE: (state, { projetcName, routes }) => {
    state.projectRoutes[projetcName] = routes
  }
}

const actions = {
  setProjectMethodDataRoutes({ commit, state }, data) {
    return new Promise((resolve, reject) => {
      getMethodDataByProjectName(data).then(res => {
        if (res.data.code !== '000') {
          reject(res.message || '获取接口数据失败')
        }

        const methodDataList = res.data.data
        if (!methodDataList || methodDataList.length === 0) {
          reject('该项目不存在')
        }
        const projectMethodDataListData = {
          projectName: data.projectName,
          methodDataList: methodDataList
        }
        commit('SET_PROJECT_METHOD_LIST', projectMethodDataListData)

        const routes = getProjectRoutesFromMethodDataList(state.projectMethodDataList[data.projectName])
        addProjectMethodDataRoutes(routes)
        const routeData = {
          projectName: data.projectName,
          routes: routes
        }
        commit('SET_PROJECT_ROUTE', routeData)
        resolve(routes)
      }).catch(error => {
        reject(error || '异常错误')
      })
    })
  },
  resetProjectApi({ state }) {
    state.projectMethodDataList = {}
    state.projectRoutes = {}
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
