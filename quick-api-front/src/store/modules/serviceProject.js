const state = {
  serviceProjectAddress: {},
  serviceProjectTokenSetting: {}
}

const mutations = {
  SET_SERVICE_PROJECT_ADDRESS: (state, { projectName, address }) => {
    state.serviceProjectAddress[projectName] = address
  },
  SET_SERVICE_PROJECT_TOKEN: (state, data) => {
    state.serviceProjectTokenSetting[data.projectName] = data
  }
}

const actions = {
  setServiceProjectAddress({ commit, state }, data) {
    commit('SET_SERVICE_PROJECT_ADDRESS', data)
  },
  /** token设置 */
  setServiceProjectToken({ commit, state }, data) {
    // 待开发
    commit('SET_SERVICE_PROJECT_TOKEN', data)
    console.log('setServiceProjectToken.data', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
