<template>
  <div>
    <el-card>
      <div style="margin-top: 70px">
        <el-row>
          <el-col :span="4">
            <el-select v-model="pageStatus.requestType" placeholder="请选择" @change="handlerequestTypeClick">
              <el-option label="POST" value="POST" />
              <el-option label="GET" value="GET" />
              <el-option label="PUT" value="PUT" />
              <el-option label="DELETE" value="DELETE" />
            </el-select>
          </el-col>
          <el-col :span="16">
            <el-input v-model="pageStatus.path" placeholder="请输入内容" class="input-with-select" />
          </el-col>
          <el-col :span="2">
            <el-button type="primary" style="margin-left: 15px" @click="handleSendRequest">
              发送
            </el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="handleClickSave">保存<i class="el-icon-upload el-icon--right" /></el-button>
          </el-col>
        </el-row>
      </div>
      <div class="option-bar" style="margin-top: 15px">
        <el-tabs v-model="pageStatus.requestActiveName" @tab-click="handleClickRequest">
          <el-tab-pane label="Param" name="Param">
            <vue-json-editor v-model="pageStatus.getTypeParam" :show-btns="false" :mode="'code'" lang="zh" @json-change="onParamChange" />
          </el-tab-pane>
          <el-tab-pane label="Header" name="Header">
            <vue-json-editor v-model="pageStatus.headerJson" :show-btns="false" :mode="'code'" lang="zh" @json-change="onHeaderChange" />
          </el-tab-pane>
          <el-tab-pane label="Body" name="Body">
            <el-radio-group v-model="pageStatus.contentType" style="margin-bottom: 10px" @change="changeBodyType">
              <el-radio label="none">none</el-radio>
              <el-radio label="application/json">application/json</el-radio>
              <el-radio label="bodyFile">文件</el-radio>
            </el-radio-group>
            <vue-json-editor v-show="pageStatus.bodyNoneShow" v-model="bodyStringData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
            <vue-json-editor v-show="pageStatus.bodyJsonShow" v-model="bodyJsonData" :show-btns="false" :mode="'code'" lang="zh" @json-change="onBodyChange" />
            <el-card v-show="pageStatus.bodyFileShow" class="body-file-box">
              <el-upload class="body-upload-file" action="" multiple :limit="1" :file-list="pageStatus.fileList">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">可上传任意格式文件</div>
              </el-upload>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </div>
      <el-divider>Response</el-divider>
      <div class="request-result" style="margin-top: 15px">
        <el-tabs v-model="pageStatus.responseActiveName" @tab-click="handleClickResponse">
          <el-tab-pane label="Body" name="Body">
            <vue-json-editor v-model="pageStatus.responseBody" :show-btns="false" :mode="'code'" lang="zh" @json-change="onResponseBodyChange" />
          </el-tab-pane>
          <!-- <el-tab-pane label="Cookies" name="Cookies">
            <el-card class="response-cookies-box"> 没有Cookies </el-card>
          </el-tab-pane> -->
          <el-tab-pane label="Headers" name="Headers">
            <vue-json-editor v-model="pageStatus.responseHeader" :show-btns="false" :mode="'code'" lang="zh" @json-change="onResponseHeaderChange" />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>

    <!-- 保存弹框 -->
    <el-dialog title="完善文档" :visible.sync="dialogSavePage.dialogTableVisible">
      <el-divider>文档名</el-divider>
      <el-input v-model="dialogSavePage.docTitle" />
      <div v-show="pageStatus.headerJson">
        <el-divider>请求头说明</el-divider>
        <el-table :data="dialogSavePage.headerJsonValues" border fit highlight-current-row>
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
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="pageStatus.getTypeParam">
        <el-divider>get参数说明</el-divider>
        <el-table :data="dialogSavePage.getTypeParamValues" border fit highlight-current-row>
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
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="pageStatus.bodyJsonData">
        <el-divider>POST参数说明</el-divider>
        <el-table :data="dialogSavePage.bodyJsonDataValues" border fit highlight-current-row>
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
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="pageStatus.responseBody">
        <el-divider>响应值说明</el-divider>
        <el-table :data="dialogSavePage.responseBodyValues" border fit highlight-current-row>
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
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="pageStatus.responseHeader">
        <el-divider>响应头说明</el-divider>
        <el-input v-model="dialogSavePage.responseHeaderDesc" type="textarea" :rows="4" placeholder="请输入说明" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSavePage.dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveData">确 定</el-button>
      </div>
    </el-dialog>
    <!-- <el-dialog title="修改方法名" :visible.sync="dialogEditMethodData.dialogMethodDataVisible">
      <el-form>
        <el-form-item label="所属组">
          <el-select v-model="dialogEditMethodData.methodGroup" filterable allow-create default-first-option placeholder="请选择组别">
            <el-option v-for="item of groupList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="方法名">
          <el-input v-model="dialogEditMethodData.methodName" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEditMethodData.dialogMethodDataVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveMethodName">确 定</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import vueJsonEditor from 'vue-json-editor'
import { callApi } from '@/api/request'
import {
  getMethodApiData,
  savePageData,
  updateMethodData
} from '@/api/apiInfo'
// import { getRoutes } from '@/utils/routerTool'

import RequestTemplate from '@/views/component/requestTemplate'
export default {
  name: 'Home',
  components: {
    vueJsonEditor,
    RequestTemplate
  },
  data() {
    return {
      // 页面数据
      pageStatus: {
        requestActiveName: 'Body',
        responseActiveName: 'Body',
        path: '',
        requestType: 'POST',

        // 请求
        headerJson: null, // 请求头
        getTypeParam: null,
        contentType: 'none', // post请求类型
        bodyNoneShow: true,
        bodyJsonShow: false,
        bodyFileShow: false,
        bodyJsonData: null,
        bodyStringData: null,
        requestServiceName: '',
        fileList: [],

        // 响应值
        responseHeader: null,
        responseBody: null
      },
      // 文档相关
      dialogSavePage: {
        dialogTableVisible: false, // 文档保存弹框
        docTitle: '',
        headerJsonValues: [], // 头参数说明
        getTypeParamValues: [], // 存储所有参数
        bodyJsonDataValues: [], // post请求参数说明
        bodyStringDataValues: [], // String类请求
        responseBodyValues: [],
        responseHeaderDesc: []
      },
      // 方法名修改
      dialogEditMethodData: {
        dialogMethodDataVisible: false,
        methodName: '',
        methodGroup: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'localServiceName',
      'routes',
      'groupList',
      'localProjectName'
    ])
  },
  mounted() {
    this.setRequestUrl()
    this.initMethodApiData()
  },
  created() { },
  methods: { // 按照页面功能顺序定义方法
    // 设置当前输入框的请求路径
    setRequestUrl() {
      if (this.$route.path === '/home') {
        this.pageStatus.path = 'http://localhost'
      } else {
        console.log('this.localServiceName: ' + this.localServiceName)
        this.pageStatus.path =
          this.localServiceName +
          this.$route.path.substring(
            this.$route.path.substring(1).indexOf('/') + 1
          )
      }
    },
    /**
     * 从服务端请求初始化当前页面数据
     */
    initMethodApiData() {
      const curPath = this.$route.path
      const data = {
        projectName: this.localProjectName,
        url: curPath.substring(curPath.substring(1).indexOf('/') + 1)
      }

      getMethodApiData(data).then((res) => {
        if (res.data.code === '000') {
          if (res.data.data) {
            Object.assign(this.$data.pageStatus, JSON.parse(res.data.data.apiJsonData).pageStatus) // 只初始化页面请求数据，不初始化文档
          }
        }
      })
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
      let contentType = this.pageStatus.contentType
      if (this.pageStatus.requestType === 'POST') {
        if (this.pageStatus.contentType === 'none') {
          queryData = this.pageStatus.bodyStringData
          contentType = ''
        } else if (this.pageStatus.contentType === 'application/json') {
          queryData = this.pageStatus.bodyJsonData
        }
      } else if (this.pageStatus.requestType === 'GET') {
        queryData = this.pageStatus.getTypeParam
        contentType = ''
      }

      callApi(
        this.pageStatus.path,
        contentType,
        this.pageStatus.headerJson,
        queryData,
        this.pageStatus.requestType
      )
        .then((res) => {
          console.log(res)
          this.pageStatus.responseBody = res.data
          this.pageStatus.responseHeader = res.headers
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
    /* 点击保存时显示弹框，完善文档 */
    handleClickSave() {
      this.dialogSavePage.dialogTableVisible = true

      // 解析各参数为表单数据
      this.dialogSavePage.headerJsonValues = this.parseParams(this.pageStatus.headerJson)
      this.dialogSavePage.getTypeParamValues = this.parseParams(this.pageStatus.getTypeParam)
      this.dialogSavePage.bodyJsonDataValues = this.parseParams(this.pageStatus.bodyJsonData)
      this.dialogSavePage.responseBodyValues = this.parseParams(this.pageStatus.responseBody)
    },
    /* 保存文档数据 */
    handleSaveData() {
      this.dialogSavePage.dialogTableVisible = false // 关闭对话框

      // 保存页面数据
      const pageData = JSON.stringify(this.$data)
      const curPath = this.$route.path
      const data = {
        projectName: this.localProjectName,
        url: curPath.substring(curPath.substring(1).indexOf('/') + 1),
        apiData: pageData,
        author: 'dummyUser'
      }

      savePageData(data)
        .then((res) => {
          if (res.data.code === '000') {
            this.$message({
              message: '保存成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '保存失败',
              type: 'warning'
            })
          }
        })
        .catch((error) => {
          this.$message({
            message: error || '保存失败',
            type: 'warning'
          })
        })
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
    // 请求参数转换为对象
    parseParams(getTypeParam) {
      const getTypeParamValues = []
      if (getTypeParam) {
        for (const key in getTypeParam) {
          // 定义表数据格式
          const tableItem = {
            name: null, // 参数名
            value: null, // 参数值
            description: null // 说明
          }

          tableItem.name = key
          tableItem.value = getTypeParam[key]
          tableItem.description = ''

          getTypeParamValues.push(tableItem)
        }
      }

      return getTypeParamValues
    },
    // 获得接口信息
    setMethodData() {
      this.dialogEditMethodData.methodName = this.$route.meta.title
      this.dialogEditMethodData.methodGroup = this.$route.meta.group
    },
    // 弹框修改方法名和组别
    handleMethodChangeClick() {
      this.dialogMethodDataVisible = true
    },
    // 修改方法名
    handleSaveMethodName() {
      // 获得菜单，根据菜单path对菜单的title进行修改
      const url = this.$route.path.substring(
        this.$route.path.substring(1).indexOf('/') + 1
      )
      const param = {
        url: url,
        projectName: this.localProjectName,
        name: this.dialogEditMethodData.methodName,
        methodGroup: this.dialogEditMethodData.methodGroup,
        author: 'dummy-x'
      }
      // 修改本地routes信息
      this.$store.dispatch('api/updateMethodData', param)

      console.log('call updateMethodData()')
      // 上传修改信息至服务器, 生成路由时, 从服务器获得api信息解析时进行设置
      updateMethodData(param).then((res) => {
        if (res.data.code === '000') {
          // 成功
          this.$message({
            message: '保存成功',
            type: 'success'
          })
        } else {
          // 失败
          this.$message({
            message: res.data.message || '保存失败',
            type: 'error'
          })
        }
      })
      this.dialogMethodDataVisible = false
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
