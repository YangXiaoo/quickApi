import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import api from './modules/api'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    api
  },
  getters
})

export default store
