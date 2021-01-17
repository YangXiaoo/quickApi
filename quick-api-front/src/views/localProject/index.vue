<template>
  <div>
    <el-card style="margin-top: 70px">
      <div class="method-title">
        {{ methodName }} <i class="el-icon-edit" @click="handleMethodChangeClick" />
      </div>
    </el-card>
    <request-template
      :page-data="pageData"
      :url="url"
      :is-local-project="true"
      :save-item="saveItem"
      @clickSave="handleClickSave"
      @clickSaveAs="handleClickSaveAs"
    />

    <!-- 保存弹框 -->
    <el-dialog title="完善文档" :visible.sync="dialogSaveMethodApiData.dialogTableVisible">
      <el-divider>文档名</el-divider>
      <el-input v-model="dialogSaveMethodApiData.docTitle" />
      <div v-show="pageData.headerJson">
        <el-divider>请求头说明</el-divider>
        <el-table :data="dialogSaveMethodApiData.headerJsonValues" border fit highlight-current-row>
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
        <el-table :data="dialogSaveMethodApiData.getTypeParamValues" border fit highlight-current-row>
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
        <el-table :data="dialogSaveMethodApiData.bodyJsonDataValues" border fit highlight-current-row>
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
        <el-table :data="dialogSaveMethodApiData.responseBodyValues" border fit highlight-current-row>
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
        <el-input v-model="dialogSaveMethodApiData.responseHeaderDesc" type="textarea" :rows="4" placeholder="请输入说明" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSaveMethodApiData.dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveMethodApiData">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改方信息" :visible.sync="dialogObj.visible">
      <el-form>
        <el-form-item label="所属组">
          <el-select v-model="dialogObj.methodGroup" filterable allow-create default-first-option placeholder="请选择组别">
            <el-option v-for="item of localProjectGroupList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="方法名">
          <el-input v-model="dialogObj.methodName" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogObj.visible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveMethodName">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { saveMethodApiData } from '@/api/methodApiData'
import {
  getUserProjectMethodPageData,
  saveUserProjectMethodPageData
} from '@/api/pageData'
import { getLocalProjectPath, parseParams } from '@/utils/commonHelper'

import RequestTemplate from '../component/requestTemplate.vue'

export default {
  name: 'LocalProject',
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
      dialogSaveMethodApiData: {
        dialogTableVisible: false, // 文档保存弹框
        docTitle: '',
        headerJsonValues: [], // 头参数说明
        getTypeParamValues: [], // 存储所有参数
        bodyJsonDataValues: [], // post请求参数说明
        bodyStringDataValues: [], // String类请求
        responseBodyValues: [],
        responseHeaderDesc: []
      },

      // 弹框
      dialogObj: {
        dialogTitle: '',
        visible: false,
        methodName: '',
        methodGroup: ''
      },
      saveItem: [
        { command: 'saveDoc', label: '保存文档' }
        // { command: 'saveAsNewTab', label: '另存为我的接口' }
      ],
      url: '', // 页面接口的url，一旦创建不会改变
      methodName: '',
      methodGroup: '',
      userName: ''
    }
  },
  computed: {
    ...mapGetters([
      'localServiceName',
      'author',
      'localProjectName',
      'localProjectGroupList'
    ])
  },
  mounted() {
    this.initBaseData()
    this.initLocalProjectMethodPageData()
  },
  created() { },
  methods: {
    initBaseData() {
      this.url = getLocalProjectPath(this.$route.path)
      this.pageData.path = this.localServiceName + this.url
      this.userName = this.author || 'dummyUser' // 测试使用
      this.methodName = this.$route.meta.title
      this.methodGroup = this.$route.meta.group
    },
    /**
     * 从服务端请求初始化当前页面数据
     */
    initLocalProjectMethodPageData() {
      const data = {
        projectName: this.localProjectName,
        url: this.url,
        author: this.author
      }

      getUserProjectMethodPageData(data).then((res) => {
        if (res.data.code === '000') {
          if (res.data.data) {
            Object.assign(this.$data.pageData, JSON.parse(res.data.data.apiJsonData)) // 只初始化页面请求数据，不初始化文档
          }
        }
      })
    },
    /* 保存文档数据 */
    handleSaveMethodApiData() {
      this.dialogSaveMethodApiData.dialogTableVisible = false // 关闭对话框

      // 保存页面数据
      const data = {
        projectName: this.localProjectName,
        url: this.url,
        apiData: JSON.stringify(this.dialogSaveMethodApiData),
        author: this.author
      }

      saveMethodApiData(data)
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
    // 弹框修改方法名和组别
    handleMethodChangeClick() {
      this.dialogObj.methodName = this.methodName
      this.dialogObj.methodGroup = this.methodGroup
      this.dialogObj.visible = true
    },
    // 修改方法名
    handleSaveMethodName() {
      // 获得菜单，根据菜单path对菜单的title进行修改
      const data = {
        url: this.url,
        projectName: this.localProjectName,
        name: this.dialogObj.methodName,
        methodGroup: this.dialogObj.methodGroup,
        author: this.author
      }

      this.$store.dispatch('localProject/updateProjectMethodData', data).then(res => {
        console.log('handleSaveMethodName', data)
        this.methodName = data.name
        this.methodGroup = data.methodGroup
      }).catch((error) => {
        this.$message({
          message: error || '更新失败',
          type: 'warning'
        })
      })
      this.resetDialog()
    },
    /** 保存页面数据 */
    handleClickSave() {
      // 保存页面数据
      const data = {
        projectName: this.localProjectName,
        url: this.url,
        pageData: JSON.stringify(this.pageData),
        author: this.author
      }
      saveUserProjectMethodPageData(data).then(res => {
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
      }).catch((error) => {
        this.$message({
          message: error || '保存失败',
          type: 'warning'
        })
      })
    },
    /** 另存为 */
    handleClickSaveAs(command) {
      if (command === 'saveDoc') {
        this.saveMethodApiData()
      }
    },
    saveMethodApiData() {
      // 解析各参数为表单数据
      this.dialogSaveMethodApiData.headerJsonValues = parseParams(this.pageData.headerJson)
      this.dialogSaveMethodApiData.getTypeParamValues = parseParams(this.pageData.getTypeParam)
      this.dialogSaveMethodApiData.bodyJsonDataValues = parseParams(this.pageData.bodyJsonData)
      this.dialogSaveMethodApiData.responseBodyValues = parseParams(this.pageData.responseBody)
      this.dialogSaveMethodApiData.dialogTableVisible = true // 关闭对话框
    },
    resetDialog() {
      this.dialogObj.dialogTitle = ''
      this.dialogObj.visible = false
      this.dialogObj.methodName = ''
      this.dialogObj.methodGroup = ''
    }
  }
}
</script>
<style scoped>
.method-title {
  /* background-color: #fff; */
  height: 20px;
  margin: 0px 10px 0px 10px;
  border-radius: 5px;
  font-size: 13px;
  color: #303133;
}
</style>
