<template>
  <div>
    <div class="project-home-container">
      <div>
        端口:<el-input v-model="port" placeholder="80" /> <br>
      </div>
      <div>
        地址:<el-input v-model="path" /><br>
      </div>
      <div>
        <el-button @click="connect">连接</el-button> <br>
      </div>
      {{ wsConnectStatus }}
      <div>
        <div>
          请求方法:<el-input v-model="data" /><br><el-button @click="send">发送</el-button>
        </div>
        <br>
        结果：
        {{ wsData }}
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'WebSocket',
  data() {
    return {
      host: 'ws://localhost',
      port: '8899',
      path: '/ws',
      socket: '',
      message: '',
      data: ''
    }
  },
  computed: {
    ...mapGetters([
      'wsConnectStatus',
      'wsData'
    ])
  },
  mounted() {
    this.initSocket()
  },
  destroyed() {

  },
  methods: {
    initSocket() {
      if (this.socket === '') {
        this.connect()
      }
    },
    connect() {
      this.$store.dispatch('websocket/connect')
    },
    send() {
      const param = {
        requestMethod: this.data,
        requestData: {}
      }
      // this.socket.send(JSON.stringify(param))

      this.$store.dispatch('websocket/send', param)
    }
  }
}
</script>

<style scoped>
.project-home-container {
  display: flex;
  flex-flow: column nowrap;

  align-content: center;
  margin-left:200px;
  margin-right: 200px;
}
</style>
