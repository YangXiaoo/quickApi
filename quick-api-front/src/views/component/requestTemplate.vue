<template>
  <div class="request-main">
    <div>
      <el-row>
        <el-col :span="3">
          <el-select v-model="pageData.requestType" placeholder="请选择" @change="handlerequestTypeClick">
            <el-option label="POST" value="POST" />
            <el-option label="GET" value="GET" />
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
        <el-col v-show="isShowSave" :span="3">
          <!-- <el-button type="primary" @click="handleClickSave">保存<i class="el-icon-upload el-icon--right" /></el-button> -->
          <el-dropdown type="primary" split-button @click="handleClickSave" @command="handleClickSaveAs">
            保存
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="item of saveItem" :key="item.command" :command="item.command">{{ item.label }}</el-dropdown-item>
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
            <el-radio label="form-data">form-data</el-radio>
          </el-radio-group>
          <vue-json-editor v-show="pageData.bodyNoneShow" v-model="pageData.bodyStringData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
          <vue-json-editor v-show="pageData.bodyJsonShow" v-model="pageData.bodyJsonData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
          <el-card v-show="pageData.bodyFormDataShow" class="body-file-box">
            <!-- <el-upload class="body-upload-file" action="" multiple :limit="1" :file-list="pageData.fileList">
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">可上传任意格式文件</div>
            </el-upload> -->
            <div class="form-data-table">
              <div v-for="(row, i) of pageData.formData" :key="i" class="form-data-row">
                <div class="form-key">
                  <div class="form-key-data">
                    <el-input v-if="row.type !== 'title'" v-model="row.key" />
                    <span v-else> {{ row.key }} </span>
                  </div>
                  <div v-if="row.type !== 'title'" class="form-key-type">
                    <el-select v-model="row.type" placeholder="请选择">
                      <el-option label="Text" value="Text" />
                      <el-option label="File" value="File" />
                    </el-select>
                  </div>
                </div>
                <div class="form-value">
                  <el-input v-if="row.type === 'Text'" v-model="row.value" />
                  <el-upload
                    v-else-if="row.type === 'File'"
                    class="upload-demo"
                    action=""
                    :show-file-list="false"
                    :on-change="(file) => handleAddFile(file, row)"
                    multiple
                    :auto-upload="false"
                  >
                    <el-button v-if="!row.fileList.length" slot="trigger" size="small" type="primary">点击上传</el-button>
                    <div v-else slot="tip">
                      已选{{ row.fileList.length }}个文件
                      <el-button style="color:red;" @click="removeFiles(row)">删除</el-button>
                    </div>
                  </el-upload>
                  <!-- <input v-else-if="row.type === 'File'" id="file" type="file" @change="(file) => handleAddFile(file, row)"> -->
                  <span v-else> {{ row.value }} </span>
                </div>
              </div>
              <div class="add-row">
                <span @click="addFormDataRow">增加</span>
                <span @click="removeFormDataRow">移除</span>
              </div>
            </div>
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
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import vueJsonEditor from 'vue-json-editor'
import { requestLocalBlobApi } from '@/api/localProject'
import { callServiceApi } from '@/api/project'
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
    },
    saveItem: {
      type: Array,
      default: () => {
        return [] // [{ command: '', label: ''}]
      }
    },
    isShowSave: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
    }
  },
  computed: {
    ...mapGetters([
      'wsConnectStatus'
    ])
  },
  watch: {
  },
  mounted() {
  },
  created() { },
  methods: { // 按照页面功能顺序定义方法
    /**
     * 点击请求类型Tab
     */
    handleClickRequest(tab, event) {
      // console.log(tab, event)
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
      let queryData = {}
      let contentType = this.pageData.contentType
      if (this.pageData.requestType === 'POST') {
        if (this.pageData.contentType === 'none') {
          queryData = this.pageData.bodyStringData
          contentType = ''
        } else if (this.pageData.contentType === 'application/json') {
          queryData = this.pageData.bodyJsonData
        } else if (this.pageData.contentType === 'form-data') {
          queryData = new FormData()
          this.pageData.formData.forEach(item => {
            if (item) {
              if (item.type === 'Text') {
                if (item.key) {
                  queryData.append(item.key, item.value)
                }
              } else if (item.type === 'File' && item.key) {
                if (item.fileList) {
                  queryData.append(item.key, item.fileList)
                }
              }
            }
          })

          // 无论本地接口还是非本地项目只要是表单请求，都直连
          requestLocalBlobApi(this.pageData.path, queryData).then(res => {
            this.pageData.responseBody = res.body
            this.pageData.responseHeader = res.headers
          })

          return
        }
      } else if (this.pageData.requestType === 'GET') {
        queryData = this.pageData.getTypeParam
        contentType = ''
      }

      const req = {
        requestMethod: 'callApi',
        requestData: {
          path: this.pageData.path,
          contentType: contentType,
          headerJson: this.pageData.headerJson || {},
          queryData: queryData || {},
          type: this.pageData.requestType
        }
      }

      // 只有本地接口才用socket进行请求
      // 个人接口或非本地接口向服务器请求
      if (this.isLocalProject || this.wsConnectStatus) {
        this.$store.dispatch('websocket/send', req).then((res) => {
          this.pageData.responseBody = res.body
          this.pageData.responseHeader = res.headers
          this.$message({
            message: res.statusText || '请求成功',
            type: 'success'
          })
        }).catch((error) => {
          this.$message({
            message: error || '请求失败',
            type: 'warning'
          })
        })
      } else {
        // 没有本地服务则通过服务器调用
        // this.changeLocalHostToLocalIp(req.requestData) // 将localhost转换为当前服务IP地址
        callServiceApi(req.requestData).then((res) => {
          console.log(res)
          this.pageData.responseBody = res.data
          this.pageData.responseHeader = res.headers
          this.$message({
            message: res.statusText || '请求成功',
            type: 'success'
          })
        }).catch((error) => {
          this.$message({
            message: error || '请求失败',
            type: 'warning'
          })
        })
      }
    },
    /* 点击保存事件*/
    handleClickSave() {
      console.log('handleClickSave')
      this.$emit('clickSave')
    },
    handleClickSaveAs(val) {
      console.log('handleClickSaveAs', val)
      this.$emit('clickSaveAs', val)
    },
    changeBodyType(value) {
      console.log('changeBodyType.value', value)
      if (value === 'application/json') {
        this.pageData.bodyNoneShow = false
        this.pageData.bodyJsonShow = true
        this.pageData.bodyFormDataShow = false
        this.pageData.contentType = 'application/json'
      } else if (value === 'form-data') {
        this.pageData.bodyNoneShow = false
        this.pageData.bodyJsonShow = false
        this.pageData.bodyFormDataShow = true
      } else if (value === 'none') {
        this.pageData.bodyNoneShow = true
        this.pageData.bodyJsonShow = false
        this.pageData.bodyFormDataShow = false
        this.pageData.contentType = 'none'
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
        this.pageData.requestActiveName = 'Body'
      } else {
        this.pageData.requestActiveName = 'Param'
        this.pageData.contentType = 'none'
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
    },
    hanldeFormDataKeyType(type, row) {
      console.log('hanldeFormDataKeyType', type, row)
      this.$set(row, 'type', type)
    },
    removeFiles(row) {
      this.$set(row, 'fileList', [])
    },
    handleAddFile(file, row) {
      if (file) {
        row.fileList.push(file.raw)
      }
    },
    addFormDataRow() {
      this.pageData.formData.push({ key: '', value: '', type: 'Text', fileList: [] })
    },
    removeFormDataRow() {
      if (this.pageData.formData && this.pageData.formData.length > 1) {
        this.pageData.formData.pop()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.request-main {
  background: #fff;
  margin: 10px 10px 10px 10px;
  padding: 10px;
  border-radius: 5px;
}

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
  height: 185px;
  overflow-y: auto;
  align-items: center;
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
.form-data-table {
  box-sizing: border-box;
  border-right: 1px solid #ebeef5;
  border-top: 1px solid #ebeef5;
  border-left: 1px solid #ebeef5;
  overflow-y: auto;

  .form-data-row {
    border-bottom: 1px solid #ebeef5;
    display: flex;
    flex-flow: row nowrap;

    .form-key {
      width: 50%;
      display: flex;
      flex-flow: row nowrap;
      border-right: 1px solid #ebeef5;

      .form-key-data {
        width: 85%;
      }

      .form-key-type {
        width: 15%;
        align-items: center;
        align-self: center;
      }
    }

    .form-value {
      width: 50%;
    }
  }
}
</style>
