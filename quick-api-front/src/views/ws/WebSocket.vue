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
      {{ tipMessage }}
      <div>
        <div>
          请求方法:<el-input v-model="data" /><br><el-button @click="send">发送</el-button>
        </div>
        <br>
        结果：
        {{ message.data }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WebSocket',
  data() {
    return {
      host: 'ws://localhost',
      port: '8899',
      path: '/ws',
      socket: '',
      tipMessage: '',
      message: '',
      data: ''
    }
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
      const wsUrl = this.host + ':' + this.port + this.path
      this.socket = new WebSocket(wsUrl)
      this.socket.onopen = this.onopen
      this.socket.onerror = this.onerror
      this.socket.onmessage = this.onmessage
      this.socket.onclose = this.onclose
    },
    onopen() {
      this.tipMessage = '连接成功'
      console.log('open>>>>>>>>>>>>')
    },
    onerror() {
      this.tipMessage = '连接失败'
      console.log('error>>>>>>>>>>>>')
    },
    onmessage(msg) {
      this.message = msg
      console.log('message>>>>>>>>>>>>', msg)
    },
    onclose() {
      console.log('close>>>>>>>>>>>>')
    },
    send() {
      const param = {
        requestMethod: this.data,
        requestData: {}
      }
      this.socket.send(JSON.stringify(param))
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
