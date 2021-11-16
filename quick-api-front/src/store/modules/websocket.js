const state = {
  wsHost: 'localhost',
  wsPort: '8899',
  wsPath: '/ws',
  socket: '',
  wsConnectStatus: false,
  requestMethod: '',
  wsData: {}
}

const mutations = {
  SET_HOST: (state, wsHost) => {
    state.wsHost = wsHost
  },
  SET_PORT: (state, wsPort) => {
    state.wsPort = wsPort
  },
  SET_PATH: (state, wsPath) => {
    state.wsPath = wsPath
  },
  SET_SOCKET: (state, socket) => {
    state.socket = socket
  },
  SET_STATUS: (state, wsConnectStatus) => {
    state.wsConnectStatus = wsConnectStatus
  },
  SET_DATA: (state, wsData) => {
    state.wsData = wsData
  }
}

const actions = {
  connect({ commit, dispatch, state }, parm) {
    if (parm) {
      state.wsHost = parm.host
      state.wsPort = parm.port
    }
    if (state.socket === '') {
      const wsUrl = 'ws://' + state.wsHost + ':' + state.wsPort + state.wsPath
      state.socket = new WebSocket(wsUrl)
      state.socket.onopen = () => {
        state.wsConnectStatus = true
        console.log('open>>>>>>>>>>>>')
      }
      state.socket.onerror = () => {
        state.socket = ''
        state.wsConnectStatus = false
        console.log('error>>>>>>>>>>>>')
      }
      state.socket.onmessage = (msg) => {
        state.wsData[state.requestMethod] = JSON.parse(msg.data)
        if (state.requestMethod === 'getLocalApiMethod') {
          dispatch('localProject/setLocalProjectRoutes', JSON.parse(msg.data), { root: true })
        }
        console.log('[ws] onmessage', state.requestMethod, msg)
      }
      state.socket.onclose = () => {
        console.log('close>>>>>>>>>>>>')
        state.socket = ''
      }
    }
  },
  send({ commit, dispatch, state }, param) {
    // const param = {
    //   requestMethod: requestMethod,
    //   requestData: requestData
    // }
    console.log('[ws]send data', param)
    state.requestMethod = param.requestMethod
    if (state.socket !== '') {
      state.socket.send(JSON.stringify(param))
    } else {
      console.log('[ws] 未连接')
    }
  },
  getData({ commit, dispatch, state }, requestMethod) {
    new Promise((resolve, reject) => {
      console.log('[ws]get data', requestMethod, state[requestMethod])
      resolve(state[requestMethod])
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
