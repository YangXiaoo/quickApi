import { constantRoutes } from '@/router'
import { getConnection } from '@/api/localProject'
const state = {
  constantRoutes: constantRoutes,
  isLocalProject: false, // 默认不是本地项目启动
  isSettingLocalFlag: false // 是否设置本地接口标识
}

const mutations = {

}

const actions = {
  setLocalProjectFlag({ state }, data = {}) {
    state.isSettingLocalFlag = true

    return new Promise((resolve, reject) => {
      getConnection(data).then(res => {
        state.isLocalProject = true
        resolve(res)
      }).catch(error => {
        console.log('非本地项目!', error)
        state.isLocalProject = false
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
