<template>
  <div>
    <el-card>
      <div style="margin-top: 70px">
        <el-tabs :tab-position="'left'">
          <el-tab-pane label="文档">
            <div>
              <div style="display: inline">
                {{ methodName }}
              </div>
              <div style="display: inline; margin-left: 20px">
                <el-dropdown>
                  <span class="el-dropdown-link">
                    例子<i class="el-icon-arrow-down el-icon--right"></i>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>成功示例</el-dropdown-item>
                    <el-dropdown-item>失败示例</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
            <div>
              <el-button type="success" style="width: 10%">{{
                selectType
              }}</el-button>
              <el-tag type="success" style="width: 80%">
                {{ path }}
              </el-tag>
            </div>
            <el-divider></el-divider>
            <div>
              <el-button type="success" style="width: 10%">请求类型</el-button>
              <el-tag style="width: 80%">{{ contentType }} </el-tag>
            </div>
            <div v-show="headerJson">
              <el-divider>请求头参数</el-divider>
              <el-table :data="headerJsonValues" border fit highlight-current-row>
                <!-- 参数名 -->
                <el-table-column label="参数名">
                  <template slot-scope="{ row }">
                    <span> {{ row.name }} </span>
                  </template>
                </el-table-column>
                <!-- 参数值 -->
                <el-table-column label="参数值">
                  <template slot-scope="{ row }">
                    <span> {{ row.value }} </span>
                  </template>
                </el-table-column>
                <!-- 参数说明 -->
                <el-table-column label="参数说明">
                  <template slot-scope="{ row }">
                    <span> {{ row.description }} </span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-show="paramData">
              <el-divider>get参数</el-divider>
              <el-table :data="paramValues" border fit highlight-current-row>
                <!-- 参数名 -->
                <el-table-column label="参数名">
                  <template slot-scope="{ row }">
                    <span> {{ row.name }} </span>
                  </template>
                </el-table-column>
                <!-- 参数值 -->
                <el-table-column label="参数值">
                  <template slot-scope="{ row }">
                    <span> {{ row.value }} </span>
                  </template>
                </el-table-column>
                <!-- 参数说明 -->
                <el-table-column label="参数说明">
                  <template slot-scope="{ row }">
                    <span> {{ row.description }} </span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-show="bodyJsonData">
              <el-divider>POST参数</el-divider>
              <el-table :data="bodyJsonDataValues" border fit highlight-current-row>
                <!-- 参数名 -->
                <el-table-column label="参数名">
                  <template slot-scope="{ row }">
                    <span> {{ row.name }} </span>
                  </template>
                </el-table-column>
                <!-- 参数值 -->
                <el-table-column label="参数值">
                  <template slot-scope="{ row }">
                    <span> {{ row.value }} </span>
                  </template>
                </el-table-column>
                <!-- 参数说明 -->
                <el-table-column label="参数说明">
                  <template slot-scope="{ row }">
                    <span> {{ row.description }} </span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-show="responseBody">
              <el-divider>响应值</el-divider>
              <el-table :data="responseBodyValues" border fit highlight-current-row>
                <!-- 参数名 -->
                <el-table-column label="参数名">
                  <template slot-scope="{ row }">
                    <span> {{ row.name }} </span>
                  </template>
                </el-table-column>
                <!-- 参数值 -->
                <el-table-column label="参数值">
                  <template slot-scope="{ row }">
                    <span> {{ row.value }} </span>
                  </template>
                </el-table-column>
                <!-- 参数说明 -->
                <el-table-column label="参数说明">
                  <template slot-scope="{ row }">
                    <span> {{ row.description }} </span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-show="responseHeaderDesc">
              <el-divider>响应头</el-divider>
              <el-input type="textarea" :rows="4" v-model="responseHeaderDesc" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="调试">
            <div>
              <el-row>
                <el-col :span="4">
                  <el-select v-model="selectType" placeholder="请选择" @change="handleSelectTypeClick">
                    <el-option label="POST" value="POST"></el-option>
                    <el-option label="GET" value="GET"></el-option>
                    <el-option label="PUT" value="PUT"></el-option>
                    <el-option label="DELETE" value="DELETE"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="16">
                  <el-input placeholder="请输入内容" v-model="path" class="input-with-select"></el-input>
                </el-col>
                <el-col :span="2">
                  <el-button type="primary" style="margin-left: 15px" @click="handleSendRequest">
                    发送
                  </el-button>
                </el-col>
              </el-row>
            </div>
            <div class="option-bar" style="margin-top: 15px">
              <el-tabs v-model="requestActiveName" @tab-click="handleClickRequest">
                <el-tab-pane label="Param" name="Param">
                  <vue-json-editor v-model="paramData" :showBtns="false" :mode="'code'" lang="zh" @json-change="onParamChange" />
                </el-tab-pane>
                <el-tab-pane label="Header" name="Header">
                  <vue-json-editor v-model="headerJson" :showBtns="false" :mode="'code'" lang="zh" @json-change="onHeaderChange" />
                </el-tab-pane>
                <el-tab-pane label="Body" name="Body">
                  <el-radio-group v-model="contentType" @change="changeBodyType" style="margin-bottom: 10px">
                    <el-radio label="none">none</el-radio>
                    <el-radio label="application/json">application/json</el-radio>
                    <el-radio label="bodyFile">文件</el-radio>
                  </el-radio-group>
                  <vue-json-editor v-show="bodyNoneShow" v-model="bodyStringData" :showBtns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
                  <vue-json-editor v-show="bodyJsonShow" v-model="bodyJsonData" :showBtns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
                  <el-card class="body-file-box" v-show="bodyFileShow">
                    <el-upload class="body-upload-file" action="https://jsonplaceholder.typicode.com/posts/" multiple :limit="1" :file-list="fileList">
                      <el-button size="small" type="primary">点击上传</el-button>
                      <div slot="tip" class="el-upload__tip">
                        可上传任意格式文件
                      </div>
                    </el-upload>
                  </el-card>
                </el-tab-pane>
              </el-tabs>
            </div>
            <el-divider>Response</el-divider>
            <div class="request-result" style="margin-top: 15px">
              <el-tabs v-model="responseActiveName" @tab-click="handleClickResponse">
                <el-tab-pane label="Body" name="Body">
                  <vue-json-editor v-model="responseBody" :showBtns="false" :mode="'code'" lang="zh" @json-change="onResponseBodyChange" />
                </el-tab-pane>
                <!-- <el-tab-pane label="Cookies" name="Cookies">
                  <el-card class="response-cookies-box"> 没有Cookies </el-card>
                </el-tab-pane> -->
                <el-tab-pane label="Headers" name="Headers">
                  <vue-json-editor v-model="responseHeader" :showBtns="false" :mode="'code'" lang="zh" @json-change="onResponseHeaderChange" />
                </el-tab-pane>
              </el-tabs>
            </div>
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
import { getMethodApiData, saveMethodApiData } from '@/api/methodApiData'
export default {
  name: 'Team',
  components: {
    vueJsonEditor,
  },
  computed: {
    ...mapGetters(['localServiceName', 'pageStatus', 'localProjectName']),
  },
  data () {
    return {
      requestActiveName: 'Body',
      responseActiveName: 'Body',
      path: '',
      paramData: null,
      bodyJsonData: null,
      bodyStringData: null,
      fileList: [],
      headerJson: null,
      responseBody: null,
      responseHeader: null,
      requestServiceName: '',
      selectType: 'POST',
      contentType: 'none',
      bodyNoneShow: true,
      bodyJsonShow: false,
      bodyFileShow: false,

      methodName: '',

      dialogTableVisible: false, // 文档保存弹框
      paramValues: [], // 存储所有参数
      bodyJsonDataValues: [], // post请求参数说明
      bodyStringDataValues: [],
      headerJsonValues: [], // 头参数说明
      responseBodyValues: [],
      responseHeaderValues: [],
    }
  },
  mounted () {
    this.setCurRul()
    this.initCurData()
  },
  methods: {
    handleClickRequest (tab, event) {
      console.log(tab, event)
    },
    handleClickResponse (tab, event) {
      console.log(tab, event)
    },
    handleSendRequest () {
      let queryData = null
      let contentType = this.contentType
      if (this.selectType === 'POST') {
        if (this.contentType === 'none') {
          queryData = this.bodyStringData
          contentType = ''
        } else if (this.contentType === 'application/json') {
          queryData = this.bodyJsonData
        }
      } else if (this.selectType === 'GET') {
        queryData = this.paramData
        contentType = ''
      }

      callApi(
        this.path,
        contentType,
        this.headerJson,
        queryData,
        this.selectType
      )
        .then((res) => {
          console.log(res)
          this.responseBody = res.data
          this.responseHeader = res.headers
          this.$message({
            message: res.statusText || '请求成功',
            type: 'success',
          })
        })
        .catch((error) => {
          this.$message({
            message: error || '请求失败',
            type: 'warning',
          })
        })
    },
    handleSaveData () {
      const curData = JSON.parse(JSON.stringify(this.$data))
      const dataKey = this.$route.path
      const data = {
        path: dataKey,
        pageData: curData,
      }

      saveMethodApiData(data)
        .then((res) => {
          this.$message({
            message: '保存成功',
            type: 'success',
          })
        })
        .catch((error) => {
          this.$message({
            message: '保存失败',
            type: 'warning',
          })
        })
    },
    // 设置当前输入框的请求
    setCurRul () {
      if (this.$route.path === '/home') {
        this.path = 'http://localhost'
      } else {
        this.path =
          this.localServiceName +
          this.$route.path.substring(
            this.$route.path.substring(1).indexOf('/') + 1
          )
      }
    },
    initCurData () {
      const curPath = this.$route.path
      const data = {
        projectName: this.localProjectName,
        url: curPath.substring(curPath.substring(1).indexOf('/') + 1),
      }

      const curData = getMethodApiData(data).then((res) => {
        if (res.data.code === '000') {
          if (res.data.data) {
            Object.assign(this.$data, JSON.parse(res.data.data.apiJsonData))

            // 设置菜单名
            this.methodName = this.$route.meta.title
          }
        }
      })
    },
    onParamChange (value) {
      console.log(value)
    },
    onParamSave (value) {
      console.log(value)
    },
    onHeaderChange (value) {
      console.log(value)
    },
    onBodyChange (value) {
      console.log(value)
    },
    onBodySave (value) {
      console.log(value)
    },
    changeBodyType (value) {
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
    onResponseBodyChange (value) {
      console.log(value)
    },
    onResponseHeaderChange (value) {
      console.log(value)
    },
    handleSelectTypeClick (value) {
      if (value === 'POST') {
        this.requestActiveName = 'Body'
      } else {
        this.requestActiveName = 'Param'
        this.contentType = 'none'
      }
    },
  },
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
