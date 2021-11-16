<template>
  <div>
    <el-dialog title="连接服务" :visible.sync="dialogVisible" width="25%">
      <el-form label-width="80px">
        <el-form-item label="地址">
          <el-input v-model="host" />
        </el-form-item>
        <el-form-item label="端口">
          <el-input v-model="port" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="loading" type="warning" @click.native.prevent="connectWebsocket">连接</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'WebsocketConnect',
  props: {
    trigger: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      dialogVisible: this.trigger,
      host: 'localhost',
      port: '8080'
    }
  },
  computed: {
    ...mapGetters([
      'wsHost',
      'wsPort'
    ])
  },
  watch: {
    trigger(newValue) {
      console.log('websocketConnect.watch.trigger', newValue)
      this.dialogVisible = true
    }
  },
  mounted() {
    this.host = this.wsHost
    this.port = this.wsPort
  },
  methods: {
    connectWebsocket() {
      const param = {
        host: this.host,
        port: this.port
      }

      this.$store.dispatch('websocket/connect', param).then(() => {
        console.log('连接')
        const req = {
          requestMethod: 'getLocalApiMethod',
          requestData: ''
        }
        this.$store.dispatch('websocket/send', req).then(res => {
          console.log('返回', res)
          this.$store.dispatch('localProject/setLocalProjectRoutes', res)
        })
      })
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss">
.file-import {
  display: flex;
  align-content: center;
  justify-content: center
}
</style>
