const state = {
  userPresets: []
}

const mutations = {

}

const actions = {
  getUserPresets({ commit, dispatch, state }, data) {
    return new Promise((resolve, reject) => {
      state.userPresets = []

      resolve(state.userPresets)
    })
  },
  addUserPresets({ commit, dispatch, state }, data) {
    return new Promise((resolve, reject) => {
      state.userPresets = []

      resolve(state.userPresets)
    })
  },
  deleteUserPresets({ commit, dispatch, state }, data) {
    return new Promise((resolve, reject) => {
      state.userPresets = []

      resolve(state.userPresets)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
