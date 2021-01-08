import { getQApiRoutesInfo, getProjectData } from '@/api/apiInfo'
import { getRoutes, getTeamRoutes, getGroup, getRoutesFromGroupMap } from '@/utils/routerTool'

const state = {
  apiInfo: [],
  routes: [],
  groupList: [], // 组别名
  routerSettingFlag: false,
  description: '',
  projectName: '',
  localProjectName: '',
  serviceNames: '',
  localServiceName: '',
  hostServiceName: '',
  version: '',
  pageStatus: {},
  teamRouterSettingFlag: false,
  teamApiInfo: {},
  teamRoutes: {}
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.routes = routes
  },
  SET_ROUTER_FLAG: (state, flag) => {
    state.routerSettingFlag = flag
  },
  SET_API_INFO: (state, apiInfo) => {
    state.apiInfo = apiInfo
  },
  SET_TEAM_API_INFO: (state, projectName, apiInfo) => {
    state.teamApiInfo[projectName] = apiInfo
  },
  SET_TEAM_ROUTES: (state, projectName, routes) => {
    state.teamRoutes[projectName] = routes
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
  SET_PAGE_STATUS: (state, page, status) => {
    state.pageStatus[page] = status
  },
  SET_GROUP_LIST: (state, groupList) => {
    state.groupList = groupList
  },
  SET_LOCAL_PROJECT_INFO: (state, data) => {
    state.apiInfo = data.apiInfo
    state.localServiceName = data.localServiceName
    state.localProjectName = data.projectName
  }
}

const actions = {
  getApiRoutes({ commit }, params) {
    return new Promise((resolve, reject) => {
      getQApiRoutesInfo(params).then(res => {
          if (res.data.code !== '000') {
              reject(res.message || '获取接口数据失败')
          }

          commit('SET_LOCAL_PROJECT_INFO', res.data.data)
          // 设置一级菜单名称
          const groupMap = getGroup(res.data.data.apiInfo)
          commit('SET_GROUP_LIST', Object.keys(groupMap))
          
          const routes = getRoutesFromGroupMap(groupMap)
          commit('SET_ROUTES', routes)

          resolve(routes)
          }).catch(error => {
              reject(error || '异常错误')
          })
      })
  },
  setRouterSettingFlag({ commit }, flag) {
    commit('SET_ROUTER_FLAG', flag)
  },
  setPageStatus({ commit }, page, status) {
    commit('SET_PAGE_STATUS', page, status)
  },
  getTeamRoutes({ commit }, params) {
    console.log('inter - team route')
    return new Promise((resolve, reject) => {
      getProjectData(params).then(res => {
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
  },
  teamRouterSettingFlag({ commit }, flag) {
    commit('SET_ROUTER_FLAG', flag)
  },
    /**
   * 修改接口信息
   * @param {*} url 
   * @param {*} name
   * @param {*} methodGroup 
   */
  updateMethodData({ commit, state }, {url, name, methodGroup}) {
    const apiInfo = state.apiInfo
    for (let api of apiInfo) {
      console.log(api)
      if (api.url === url) {
        console.log('api.url: ' + url)
        
        if (name) {
          api.name = name
        }
        if (methodGroup) {
          api.group = methodGroup
        }
        
        commit('SET_API_INFO', apiInfo)
        const groupMap = getGroup(apiInfo)
        commit('SET_GROUP_LIST', Object.keys(groupMap))

        const routes = getRoutesFromGroupMap(groupMap)
        commit('SET_ROUTES', routes)

        break
      }
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
