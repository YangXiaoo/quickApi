<template>
  <div>
    <request-template
      :page-data="pageData"
      :url="url"
      :is-local-project="false"
      :project-name="projectName"
      @clickSave="handleClickSave"
      @clickSaveAs="handleClickSaveAs"
    />

    <!-- 保存弹框 -->
    <el-dialog title="完善文档" :visible.sync="dialogSavePage.dialogTableVisible">
      <el-divider>文档名</el-divider>
      <el-input v-model="dialogSavePage.docTitle" />
      <div v-show="pageData.headerJson">
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
      <div v-show="pageData.getTypeParam">
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
      <div v-show="pageData.bodyJsonData">
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
      <div v-show="pageData.responseBody">
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
      <div v-show="pageData.responseHeader">
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
import { callApi } from '@/api/localProject'
import {
  getMethodApiData
} from '@/api/methodApiData'
import { updateMethodData } from '@/api/methodData'

import RequestTemplate from '../component/requestTemplate.vue'
export default {
  name: 'Home',
  components: {
    RequestTemplate
  },
  data() {
    return {
      // 页面数据
      pageData: {
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
        this.pageData.path = 'http://localhost'
      } else {
        console.log('this.localServiceName: ' + this.localServiceName)
        this.pageData.path =
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
            Object.assign(this.$data.pageData, JSON.parse(res.data.data.apiJsonData).pageData) // 只初始化页面请求数据，不初始化文档
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
        this.pageData.path,
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
    /* 点击保存时显示弹框，完善文档 */
    handleClickSave() {
      this.dialogSavePage.dialogTableVisible = true

      // 解析各参数为表单数据
      this.dialogSavePage.headerJsonValues = this.parseParams(this.pageData.headerJson)
      this.dialogSavePage.getTypeParamValues = this.parseParams(this.pageData.getTypeParam)
      this.dialogSavePage.bodyJsonDataValues = this.parseParams(this.pageData.bodyJsonData)
      this.dialogSavePage.responseBodyValues = this.parseParams(this.pageData.responseBody)
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
