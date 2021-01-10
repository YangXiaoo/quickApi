const state = {
  methodData: [], // 方法数据
  groupList: [] // 一级菜单
}

const mutations = {

}

const actions = {
  /**
   * 1. 保存方信息
   * 2. 保存页面参数信息
   */
  saveMyMethodDataAndApi({ commit }, data) {
    // dummy
  },
  /**
   * 根据用户信息和url获得接口数据
   */
  getMyMethodApi({ commit }, data) {
    // dummy
  },
  /**
   * 根据用户信息获得用户的所有接口方法数据
   */
  getMyMethodData({ commit }, data) {
    // dummy
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
