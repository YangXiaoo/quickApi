const state = {
  serviceProjectAddress: {}
}

const mutations = {
  SET_SERVICE_PROJECT_ADDRESS: (state, {projectName, address}) => {
    state.serviceProjectAddress[projectName] = address
  }
}

const actions = {
  setServiceProjectAddress({ commit, state }, data) {
    commit('SET_SERVICE_PROJECT_ADDRESS', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
