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
        <el-col :span="15">
          <el-input v-model="pageData.path" placeholder="请输入内容" class="input-with-select" />
        </el-col>
        <el-col :span="3">
          <el-dropdown type="primary" split-button @click="handleSendRequest" @command="handleSendRequestAndDowload">
            发送
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="sendAndDownlod">发送并下载</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
        <el-col v-show="isShowSave" :span="3">
          <el-dropdown type="primary" split-button @click="handleClickSave" @command="handleClickSaveAs">
            保存
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="item of saveItem" :key="item.command" :command="item.command">{{ item.label }}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>
      </el-row>
    </div>
    <div class="option-bar">
      <el-tabs v-model="pageData.requestActiveName" @tab-click="handleClickRequest">
        <el-tab-pane label="Param" name="Param">
          <div class="request-operate-bar">
            <div class="request-operate-left request-operate-list">
              <div class="request-operate-item">
                查询参数
              </div>
            </div>
            <div class="request-operate-right request-operate-list">
              <div class="request-operate-item">
                <span @click="clearGetParam"><i class="el-icon-delete" /></span>
              </div>
            </div>
          </div>
          <vue-json-editor v-model="pageData.getTypeParam" :show-btns="false" :mode="'code'" lang="zh" @json-change="onParamChange" />
        </el-tab-pane>
        <el-tab-pane label="Header" name="Header">
          <div class="request-operate-bar">
            <div class="request-operate-left request-operate-list">
              <div class="request-operate-item">
                请求头参数
              </div>
            </div>
            <div class="request-operate-right request-operate-list">
              <div class="request-operate-item">
                <el-dropdown class="right-menu-item hover-effect" trigger="click" @command="insertPersetToHeader">
                  <span>预置</span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="preSetsManage">管理预置</el-dropdown-item>
                    <el-dropdown-item
                      v-for="item of presets"
                      :key="item.presetId"
                      :command="item.presetId"
                    >
                      {{ item.name }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
              <div class="request-operate-item">
                <span @click="clearHeaderParam"><i class="el-icon-delete" /></span>
              </div>
            </div>
          </div>
          <vue-json-editor v-model="pageData.headerJson" :show-btns="false" :mode="'code'" lang="zh" @json-change="onHeaderChange" />
        </el-tab-pane>
        <el-tab-pane label="Body" name="Body">
          <div class="request-operate-bar">
            <div class="request-operate-left request-operate-list">
              <div class="request-operate-item">
                <el-radio-group v-model="pageData.contentType" @change="changeBodyType">
                  <el-radio label="none">none</el-radio>
                  <el-radio label="application/json">application/json</el-radio>
                  <el-radio label="form-data">form-data</el-radio>
                </el-radio-group>
              </div>
            </div>
            <div class="request-operate-right request-operate-list">
              <div class="request-operate-item">
                <span @click="clearBodyParam"><i class="el-icon-delete" /></span>
              </div>
            </div>
          </div>
          <vue-json-editor v-show="pageData.bodyNoneShow" v-model="pageData.bodyStringData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
          <vue-json-editor v-show="pageData.bodyJsonShow" v-model="pageData.bodyJsonData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
          <el-card v-show="pageData.bodyFormDataShow" class="body-file-box">
            <div class="form-data-table">
              <div class="form-data-row">
                <div class="form-key-title">
                  KEY
                </div>
                <div class="form-value-title">
                  VALUE
                </div>
              </div>
              <div v-for="(row, i) of pageData.formData" :key="i" class="form-data-row">
                <div class="form-key">
                  <div class="form-key-data">
                    <el-input v-if="row.type !== 'title'" v-model="row.key" />
                    <span v-else> {{ row.key }} </span>
                  </div>
                  <div v-if="row.type !== 'title'" class="form-key-type">
                    <el-select v-model="row.type" placeholder="请选择">
                      <el-option label="text" value="text" />
                      <el-option label="file" value="file" />
                    </el-select>
                  </div>
                </div>
                <div class="form-value">
                  <el-input v-if="row.type === 'text'" v-model="row.value" />
                  <el-upload
                    v-else-if="row.type === 'file'"
                    class="form-upload"
                    action=""
                    :show-file-list="false"
                    :on-change="(file) => handleAddFile(file, row)"
                    multiple
                    :auto-upload="false"
                  >
                    <el-button v-if="!row.fileList.length" slot="trigger" size="small" type="primary">点击上传</el-button>
                    <div v-else slot="tip" class="form-remove-file">
                      已选{{ row.fileList.length }}个文件
                      <span style="color:red;" @click="removeFiles(row)"><i class="el-icon-close" /></span>
                    </div>
                  </el-upload>
                  <span v-else> {{ row.value }} </span>
                </div>
                <div class="form-row-remove">
                  <span @click="removeFormDataRow(i)"><i class="el-icon-delete" /></span>
                </div>
              </div>
            </div>
            <div class="form-add-row">
              <div class="form-add-row-button">
                <span @click="addFormDataRow"><i class="el-icon-plus" /></span>
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

    <HeaderPresets :trigger="presetTrigger" @refresh="updateUserHeaderPresets" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import vueJsonEditor from 'vue-json-editor'
import { requestLocalBlobApi, responseLocalBlobApi } from '@/api/localProject'
import { callServiceApi } from '@/api/project'
import { getUserHeaderPresets } from '@/api/presets'
import { download } from '@/utils/commonHelper'

import HeaderPresets from './headerPresets.vue'
export default {
  name: 'RequestTemplate',
  components: {
    vueJsonEditor,
    HeaderPresets
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
      presetTrigger: false,
      presets: []
    }
  },
  computed: {
    ...mapGetters([
      'wsConnectStatus',
      'loginName'
    ])
  },
  watch: {
  },
  mounted() {
    this.getUserHeaderPresets()
  },
  created() { },
  methods: { // 按照页面功能顺序定义方法
    /**
     * 点击请求类型Tab
     */
    handleClickRequest(tab, event) {
      // console.log(tab, event)
    },
    clearGetParam() {
      this.pageData.getTypeParam = {}
    },
    clearHeaderParam() {
      this.pageData.headerJson = {}
    },
    clearBodyParam() {
      if (this.pageData.contentType === 'none') {
        //
      } else if (this.pageData.contentType === 'application/json') {
        this.pageData.bodyJsonData = {}
      } else if (this.pageData.contentType === 'form-data') {
        this.pageData.formData = []
      }
    },
    insertPersetToHeader(command) {
      if (command === 'preSetsManage') {
        this.openPresetDialog()
      } else {
        this.presets.forEach(preset => {
          if (preset.presetId === command) {
            console.log('cur preset.value', preset.value)
            if (!this.pageData.headerJson ||
              JSON.stringify(this.pageData.headerJson) === '{}') {
              this.pageData.headerJson = JSON.parse(JSON.stringify(preset.value))
            } else {
              Object.keys(preset.value).forEach(key => {
                console.log('cur preset.value[key]', key, preset.value[key])
                this.$set(this.pageData.headerJson, key, preset.value[key])
              })
            }
          }
        })
      }
    },
    updateUserHeaderPresets(presets) {
      this.presets = presets
    },
    openPresetDialog() {
      this.presetTrigger = !this.presetTrigger
    },
    getUserHeaderPresets() {
      const param = {
        userName: this.loginName
      }
      getUserHeaderPresets(param).then(res => {
        if (res.data.code === '000') {
          const data = res.data.data
          if (data) {
            this.presets = data
            this.presets.forEach(item => {
              item.value = JSON.parse(item.value)
            })
          }
        }
      })
    },
    /**
     * 点击响应值
     */
    handleClickResponse(tab, event) {
      console.log(tab, event)
    },
    getRequestData() {
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
              if (item.type === 'text') {
                if (item.key) {
                  queryData.append(item.key, item.value)
                }
              } else if (item.type === 'file' && item.key) {
                if (item.fileList) {
                  queryData.append(item.key, item.fileList)
                }
              }
            }
          })
        }
      } else if (this.pageData.requestType === 'GET') {
        queryData = this.pageData.getTypeParam
        contentType = ''
      }

      return {
        contentType: contentType,
        queryData: queryData
      }
    },
    /**
     * 发送请求
     */
    handleSendRequest() {
      const requestData = this.getRequestData()

      // 因为socket传输大文件麻烦所以暂时无论本地接口
      // 还是非本地项目只要是表单请求，都直连
      if (requestData.contentType === 'form-data') {
        requestLocalBlobApi(this.pageData.path, requestData.queryData).then(res => {
          this.pageData.responseBody = res.body
          this.pageData.responseHeader = res.headers
        })

        return
      }

      const req = {
        requestMethod: 'callApi',
        requestData: {
          path: this.pageData.path,
          contentType: requestData.contentType,
          headerJson: this.pageData.headerJson || {},
          queryData: requestData.queryData || {},
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
    handleSendRequestAndDowload(command) {
      if (command === 'sendAndDownlod') {
        const requestData = this.getRequestData()
        responseLocalBlobApi(this.pageData.path, requestData.queryData)
          .then(res => {
            download(res.data)
          })
      }
    },
    /* 点击保存事件*/
    handleClickSave() {
      this.$emit('clickSave')
    },
    handleClickSaveAs(val) {
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
        this.pageData.contentType = 'form-data'
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
      if (!this.pageData.formData) {
        this.pageData.formData = []
      }
      this.pageData.formData.push({ key: '', value: '', type: 'text', fileList: [] })
    },
    removeFormDataRow(i) {
      this.pageData.formData.splice(i, 1)
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

  .option-bar {
    margin-top: 15px;

    .request-operate-bar {
      display: flex;
      flex-flow: row nowrap;
      align-items: center;
      margin: 0 0 4px 0;

      .request-operate-left {
        margin-right: auto;
        display: flex;
        flex-flow: row;
        align-items: center;
        .request-operate-item {
          margin-right: 8px;

          cursor: pointer;
        }
      }

      .request-operate-right {
        margin-left: auto;
        display: flex;
        flex-flow: row;
        align-items: center;
        padding-right: 8px;
        .request-operate-item {
          margin-left: 8px;

          cursor: pointer;
        }
      }
    }
  }
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
    display: flex;
    flex-flow: row nowrap;
    border-bottom: 1px solid #ebeef5;
    .form-key-title {
      height: 30px;
      width: 45%;
      display: flex;
      justify-content: center;
      align-items: center;
      border-right: 1px solid #ebeef5;
      font-size: 15px;
      font-weight: bold;
    }
    .form-value-title {
      height: 30px;
      width: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 15px;
      font-weight: bold;
    }
    .form-row-remove {
      width: 5%;
      padding-top: 10px;
      border-left: 1px solid #ebeef5;
      text-align: center;

      :hover {
        color: red;
        cursor: pointer;
      }
    }
    .form-key {
      width: 45%;
      display: flex;
      flex-flow: row nowrap;
      justify-items: center;
      border-right: 1px solid #ebeef5;

      .form-key-data {
        width: 85%;
        align-self: center;
      }

      .form-key-type {
        width: 15%;
        align-self: center;
      }
    }

    .form-value {
      width: 50%;
      height: 40px;
      display: flex;
      justify-content: center;
      align-items: center;
      .form-upload {
      }

      .form-remove-file {
        margin-top: -18px;

        :hover {
          cursor: pointer;
        }
      }
    }
  }
}

.form-add-row {
  float: right;
  height: 40px;

  .form-add-row-button {
    width: 55px;
    padding-top: 10px;

    :hover {
      color: #e68c51;
      cursor: pointer;
    }
  }
}
</style>
