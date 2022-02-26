import md5 from 'js-md5'

const state = {
  wsHost: 'localhost',
  wsPort: '8899',
  wsPath: '/ws',
  socket: '',
  wsConnectStatus: false,
  requestMethod: '',
  wsData: {},
  promisePools: {} // 回调池
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
    return new Promise((resolve, reject) => {
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
          resolve()
        }

        state.socket.onerror = (e) => {
          console.log('error>>>>>>>>>>>>')
          reject(e)
        }

        state.socket.onmessage = (msg) => {
          console.log('[ws] onmessage', state.requestMethod, msg)
          const data = JSON.parse(msg.data)
          const token = data.token

          const req = state.promisePools[token]
          if (data.code === '000') {
            req.resolve(data.rsp)
          } else {
            req.reject(data.rsp)
          }

          delete state.promisePools[token]
        }

        state.socket.onclose = () => {
          if (state.wsConnectStatus) {
            // 非主动关闭尝试重连
          }
          console.log('close>>>>>>>>>>>>')
          state.socket = ''
          state.wsConnectStatus = false
        }
      }
    })
  },
  send({ commit, dispatch, state }, param) {
    // const param = {
    //   requestMethod: requestMethod,
    //   requestData: requestData
    // }
    console.log('[ws]send data， before md5', param)
    param.token = md5(JSON.stringify(param)) // 将参数Md5作为请求唯一标识符
    console.log('[ws]send data', param)
    state.requestMethod = param.requestMethod
    if (state.socket !== '') {
      return new Promise((resolve, reject) => {
        state.promisePools[param.token] = {
          resolve,
          reject,
          param
        }

        state.socket.send(JSON.stringify(param))
      })
    } else {
      console.log('[ws] 未连接')
    }
  },
  getData({ commit, dispatch, state }, requestMethod) {
    new Promise((resolve, reject) => {
      console.log('[ws]get data', requestMethod, state[requestMethod])
      resolve(state[requestMethod])
    })
  },
  close({ commit, dispatch, state }) {
    state.wsConnectStatus = false
    state.socket.close()
    state.socket = ''
    state.promisePools = {}
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
