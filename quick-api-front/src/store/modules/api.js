import { getQApiRoutesInfo } from '@/api/apiInfo'
import { getRoutes } from '@/utils/routerTool'

const state = {
  apiInfo: [],
  routes: [],
  description: '',
  projectName: '',
  serviceNames: '',
  localServiceName: '',
  hostServiceName: '',
  version: ''
}

const mutations = {
  SET_ROUTES: (state, routes) => {
      state.routes = routes
  },
  SET_API_INFO: (state, apiInfo) => {
      state.apiInfo = apiInfo
  }
}

const actions = {
    getApiRoutes({ commit }, params) {
      return new Promise((resolve, reject) => {
        getQApiRoutesInfo(params).then(res => {
            if (res.data.code !== '000') {
                reject(res.data.message || '获取接口数据失败')
            }

            commit('SET_API_INFO', res.data.apiInfo)
            
            const routes = getRoutes(res.data.apiInfo)
            commit('SET_ROUTES', routes)

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
