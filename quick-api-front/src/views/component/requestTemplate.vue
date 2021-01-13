<template>
  <div>
    <el-card>
      <div style="margin-top: 70px">
        <el-row>
          <el-col :span="4">
            <el-select v-model="pageData.requestType" placeholder="请选择" @change="handlerequestTypeClick">
              <el-option label="POST" value="POST" />
              <el-option label="GET" value="GET" />
              <el-option label="PUT" value="PUT" />
              <el-option label="DELETE" value="DELETE" />
            </el-select>
          </el-col>
          <el-col :span="16">
            <el-input v-model="pageData.path" placeholder="请输入内容" class="input-with-select" />
          </el-col>
          <el-col :span="2">
            <el-button type="primary" style="margin-left: 15px" @click="handleSendRequest">
              发送
            </el-button>
          </el-col>
          <el-col :span="2">
            <!-- <el-button type="primary" @click="handleClickSave">保存<i class="el-icon-upload el-icon--right" /></el-button> -->
            <el-dropdown>
              <el-button type="primary" @click="handleClickSave">
                保存<i class="el-icon-upload el-icon--right" />
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click="handleClickSaveAs">另存为</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
      </div>
      <div class="option-bar" style="margin-top: 15px">
        <el-tabs v-model="pageData.requestActiveName" @tab-click="handleClickRequest">
          <el-tab-pane label="Param" name="Param">
            <vue-json-editor v-model="pageData.getTypeParam" :show-btns="false" :mode="'code'" lang="zh" @json-change="onParamChange" />
          </el-tab-pane>
          <el-tab-pane label="Header" name="Header">
            <vue-json-editor v-model="pageData.headerJson" :show-btns="false" :mode="'code'" lang="zh" @json-change="onHeaderChange" />
          </el-tab-pane>
          <el-tab-pane label="Body" name="Body">
            <el-radio-group v-model="pageData.contentType" style="margin-bottom: 10px" @change="changeBodyType">
              <el-radio label="none">none</el-radio>
              <el-radio label="application/json">application/json</el-radio>
              <el-radio label="bodyFile">文件</el-radio>
            </el-radio-group>
            <vue-json-editor v-show="pageData.bodyNoneShow" v-model="pageData.bodyStringData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
            <vue-json-editor v-show="pageData.bodyJsonShow" v-model="pageData.bodyJsonData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
            <el-card v-show="pageData.bodyFileShow" class="body-file-box">
              <el-upload class="body-upload-file" action="" multiple :limit="1" :file-list="pageData.fileList">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">可上传任意格式文件</div>
              </el-upload>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </div>
      <el-divider>Response</el-divider>
      <div class="request-result" style="margin-top: 15px">
        <el-tabs v-model="pageData.responseActiveName" @tab-click="handleClickResponse">
          <el-tab-pane label="Body" name="Body">
            <vue-json-editor v-model="pageData.responseBody" :show-btns="false" :mode="'code'" lang="zh" @json-change="onResponseBodyChange" />
          </el-tab-pane>
          <!-- <el-tab-pane label="Cookies" name="Cookies">
            <el-card class="response-cookies-box"> 没有Cookies </el-card>
          </el-tab-pane> -->
          <el-tab-pane label="Headers" name="Headers">
            <vue-json-editor v-model="pageData.responseHeader" :show-btns="false" :mode="'code'" lang="zh" @json-change="onResponseHeaderChange" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import vueJsonEditor from 'vue-json-editor'
import { callApi } from '@/api/localProject'
import { getMethodApiData } from '@/api/methodApiData'
export default {
  name: 'RequestTemplate',
  components: {
    vueJsonEditor
  },
  props: {
    // 页面数据
    pageData: {
      type: Object,
      required: true
    },
    // 请求路径
    url: {
      type: String,
      default: ''
    },
    // 是否本地项目
    isLocalProject: {
      type: Boolean,
      default: true
    },
    projectName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
    }
  },
  computed: {
    ...mapGetters([
    ])
  },
  mounted() {
    this.initMethodApiData()
  },
  created() { },
  methods: { // 按照页面功能顺序定义方法
    /**
     * 从服务端请求初始化当前页面数据
     */
    initMethodApiData() {
      if (this.isLocalProject) {
        if (!this.projectName) {
          this.$message({
            type: 'warning',
            message: '没有项目名'
          })

          return
        }

        const data = {
          projectName: this.projectName,
          url: this.url
        }

        getMethodApiData(data).then((res) => {
          if (res.data.code === '000') {
            if (res.data.data) {
              Object.assign(this.$data.pageData, JSON.parse(res.data.data.apiJsonData).pageData) // 只初始化页面请求数据，不初始化文档
            }
          }
        })
      } else {
        // 非本地项目时页面初始化接口不一致
      }
    },
    /**
     * 点击请求类型Tab
     */
    handleClickRequest(tab, event) {
      console.log(tab, event)
    },
    /**
     * 点击响应值
     */
    handleClickResponse(tab, event) {
      console.log(tab, event)
    },
    /**
     * 发送请求
     */
    handleSendRequest() {
      let queryData = null
      let contentType = this.pageData.contentType
      if (this.pageData.requestType === 'POST') {
        if (this.pageData.contentType === 'none') {
          queryData = this.pageData.bodyStringData
          contentType = ''
        } else if (this.pageData.contentType === 'application/json') {
          queryData = this.pageData.bodyJsonData
        }
      } else if (this.pageData.requestType === 'GET') {
        queryData = this.pageData.getTypeParam
        contentType = ''
      }

      callApi(
        this.url,
        contentType,
        this.pageData.headerJson,
        queryData,
        this.pageData.requestType
      )
        .then((res) => {
          console.log(res)
          this.pageData.responseBody = res.data
          this.pageData.responseHeader = res.headers
          this.$message({
            message: res.statusText || '请求成功',
            type: 'success'
          })
        })
        .catch((error) => {
          this.$message({
            message: error || '请求失败',
            type: 'warning'
          })
        })
    },
    /* 点击保存事件*/
    handleClickSave() {
      this.$emit('clickSave')
    },
    handleClickSaveAs() {
      this.$emit('clickSaveAs')
    },
    changeBodyType(value) {
      if (value === 'application/json') {
        this.bodyNoneShow = false
        this.bodyJsonShow = true
        this.bodyFileShow = false
        this.contentType = 'application/json'
      } else if (value === 'bodyFile') {
        this.bodyNoneShow = false
        this.bodyJsonShow = false
        this.bodyFileShow = true
      } else if (value === 'none') {
        this.bodyNoneShow = true
        this.bodyJsonShow = false
        this.bodyFileShow = false
        this.contentType = 'none'
      }
    },
    onResponseBodyChange(value) {
      console.log(value)
    },
    onResponseHeaderChange(value) {
      console.log(value)
    },
    handlerequestTypeClick(value) {
      if (value === 'POST') {
        this.requestActiveName = 'Body'
      } else {
        this.requestActiveName = 'Param'
        this.contentType = 'none'
      }
    },
    // json输入框事件处理, 无用
    onParamChange(value) {
      console.log(value)
    },
    onParamSave(value) {
      console.log(value)
    },
    onHeaderChange(value) {
      console.log(value)
    },
    onBodyChange(value) {
      console.log(value)
    },
    onBodySave(value) {
      console.log(value)
    }
  }
}
</script>
<style scoped>
.el-tag {
  margin-left: 10px;
}

.request-type {
  display: inline-flex;
}

.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
.jsoneditor-poweredBy {
  display: none;
}
.body-file-box {
  align-items: center;
  height: 185px;
  text-align: center;
}
.body-upload-file {
  margin-top: 50px;
}
.response-cookies-box {
  align-items: center;
  height: 185px;
  text-align: center;
}
.jsoneditor-menu a {
  display: none !important;
}
</style>
