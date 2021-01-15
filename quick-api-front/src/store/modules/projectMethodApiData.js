/**
 * 针对项目接口文档
 */
import { getMethodDataByProjectName } from '@/api/methodData'
import { getTeamRoutes } from '@/utils/routerTool'
const state = {

}

const mutations = {

}

const actions = {
  getTeamRoutes({ commit }, params) {
    return new Promise((resolve, reject) => {
      getMethodDataByProjectName(params).then(res => {
        console.log(res)
        if (res.data.code !== '000') {
          reject(res.message || '获取接口数据失败')
        }

        commit('SET_TEAM_API_INFO', params, res.data.data)
        commit('SET_TEAM_LOCAL_SERVICE_NAME', res.data.data.localServiceName)

        const routes = getTeamRoutes(res.data.data)
        commit('SET_TEAM_ROUTES', routes)

        resolve(routes)
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
