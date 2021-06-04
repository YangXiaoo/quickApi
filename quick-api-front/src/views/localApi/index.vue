<template>
  <div>
    <div class="method-navbar">
      <div class="left-menu">
        <div class="left-menu-item">
          {{ methodName }} <i class="el-icon-edit" @click="handleMethodChangeClick" />
        </div>
        <div class="left-menu-item">
          <i class="el-icon-refresh" @click="handleRefreshUserPageData" />
        </div>
      </div>
      <div class="right-menu">
        <div v-show="curMethodApiData" class="right-menu-item">
          <span class="doc-title-dot">{{ curMethodApiData.docTitle }} </span>
        </div>
        <div v-show="curMethodApiData && curMethodApiData.userId === author" class="right-menu-item" style="color:red;" @click="handleDeleteMethodApiData">
          <i class="el-icon-delete" />
        </div>
        <!-- <div class="right-menu-item">
          {{ curMethodApiData.userName }}
        </div> -->
        <div class="right-menu-item">
          <el-dropdown @command="handleApiClick">
            <span class="el-dropdown-link">
              文档示例<i class="el-icon-arrow-down el-icon--right" />
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="apiData of methodApiDataList" v-show="methodApiDataList.length > 0" :key="apiData.apiDocId" :command="apiData.apiDocId"> {{ apiData.docTitle }} </el-dropdown-item>
              <el-dropdown-item v-show="methodApiDataList.length === 0"> 暂无文档 </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>
    <request-template
      :page-data="pageData"
      :url="url"
      :is-local-project="true"
      :save-item="saveItem"
      @clickSave="handleClickSave"
      @clickSaveAs="handleClickSaveAs"
    />

    <!-- 保存弹框 -->
    <el-dialog title="完善文档" :visible.sync="methodApiData.dialogTableVisible">
      <el-divider>文档名</el-divider>
      <el-input v-model="methodApiData.docTitle" />
      <div v-show="pageData.headerJson">
        <el-divider>请求头说明</el-divider>
        <el-table :data="methodApiData.headerJsonValues" size="small" border fit highlight-current-row>
          <!-- 参数名 -->
          <el-table-column label="参数名">
            <template slot-scope="{ row }">
              <span> {{ row.name }} </span>
            </template>
          </el-table-column>
          <!-- 参数值 -->
          <el-table-column label="必选">
            <template slot-scope="{ row }">
              <el-select v-model="row.required" placeholder="请选择">
                <el-option label="true" value="true" />
                <el-option label="false" value="false" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="类型">
            <template slot-scope="{ row }">
              <span> {{ row.type }} </span>
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
      <div v-show="pageData.requestType === 'GET'">
        <el-divider>get参数说明</el-divider>
        <el-table :data="methodApiData.getTypeParamValues" size="small" border fit highlight-current-row>
          <!-- 参数名 -->
          <el-table-column label="参数名">
            <template slot-scope="{ row }">
              <span> {{ row.name }} </span>
            </template>
          </el-table-column>
          <!-- 参数值 -->
          <el-table-column label="必选">
            <template slot-scope="{ row }">
              <el-select v-model="row.required" placeholder="请选择">
                <el-option label="true" value="true" />
                <el-option label="false" value="false" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="类型">
            <template slot-scope="{ row }">
              <span> {{ row.type }} </span>
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
      <div v-show="pageData.requestType === 'POST' && pageData.contentType !== 'none'">
        <el-divider>POST参数说明</el-divider>
        <el-table :data="methodApiData.bodyJsonDataValues" size="small" border fit highlight-current-row>
          <!-- 参数名 -->
          <el-table-column label="参数名">
            <template slot-scope="{ row }">
              <span> {{ row.name }} </span>
            </template>
          </el-table-column>
          <!-- 参数值 -->
          <el-table-column label="必选">
            <template slot-scope="{ row }">
              <el-select v-model="row.required" placeholder="请选择">
                <el-option label="true" value="true" />
                <el-option label="false" value="false" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="类型">
            <template slot-scope="{ row }">
              <span> {{ row.type }} </span>
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
        <el-table :data="methodApiData.responseBodyValues" size="small" border fit highlight-current-row>
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
        <el-input v-model="methodApiData.responseHeaderDesc" type="textarea" :rows="4" placeholder="请输入说明" />
      </div>
      <div>
        <el-divider>备注(可选)</el-divider>
        <el-input v-model="methodApiData.remark" type="textarea" :rows="4" placeholder="文档备注" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="methodApiData.dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveMethodApiData">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改方信息" :visible.sync="dialogObj.visible" width="30%">
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
import { saveMethodApiData, getMethodApiData, deleteMethodApiData } from '@/api/methodApiData'
import {
  getUserProjectMethodPageData,
  saveUserProjectMethodPageData
} from '@/api/pageData'
import { getLocalProjectPath, parseParams, parseRequestData } from '@/utils/commonHelper'

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
      methodApiData: {
        dialogTableVisible: false, // 文档保存弹框
        docTitle: '',
        headerJsonValues: [], // 头参数说明
        getTypeParamValues: [], // 存储所有参数
        bodyJsonDataValues: [], // post请求参数说明
        bodyStringDataValues: [], // String类请求
        responseBodyValues: [],
        responseHeaderDesc: '',
        remark: '' // 备注
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
      userName: '',
      methodApiDataList: [],
      curMethodApiData: ''
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
    this.initMethodApiData()
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
    /** 获取接口文档 */
    initMethodApiData() {
      const data = {
        projectName: this.localProjectName,
        url: this.url
      }

      /** 可能存在多个文档，默认初始化第一个文档 */
      getMethodApiData(data).then(res => {
        if (res.data.code === '000') {
          this.methodApiDataList = res.data.data.map(item => {
            this.$set(item, 'docTitle', JSON.parse(item.apiJsonData).methodApiData.docTitle)
            return item
          })
        }
      })
    },
    handleApiClick(apiDocId) {
      console.log('handleApiClick', apiDocId)
      for (const apiData of this.methodApiDataList) {
        console.log('handleApiClick v-for', apiData)
        if (apiData.apiDocId === apiDocId) {
          console.log('handleApiClick-update', apiDocId)
          this.curMethodApiData = apiData
          this.setPageFromApiData()
        }
      }
    },
    setPageFromApiData() {
      console.log('setPageFromApiData', this.curMethodApiData)
      this.pageData = JSON.parse(this.curMethodApiData.apiJsonData).pageData
      this.methodApiData = JSON.parse(this.curMethodApiData.apiJsonData).methodApiData
      console.log('setPageFromApiData.pageData', this.pageData)
      console.log('setPageFromApiData.methodApiData', this.methodApiData)
    },
    /* 保存文档数据, 需要保存页面数据和文档数据 */
    handleSaveMethodApiData() {
      this.methodApiData.dialogTableVisible = false // 关闭对话框
      const data = {
        projectName: this.localProjectName,
        url: this.url,
        apiData: JSON.stringify(this.$data),
        author: this.author
      }

      saveMethodApiData(data)
        .then((res) => {
          if (res.data.code === '000') {
            this.initMethodApiData()
            // this.methodApiDataList.push(this.$data) // 将当前保存的数据插入文档缓存中
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
    /**
     * 不使用文档数据
     * 重新请求获得页面数据
     */
    handleRefreshUserPageData() {
      this.curMethodApiData = ''
      this.initLocalProjectMethodPageData()
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
    handleDeleteMethodApiData() {
      if (this.curMethodApiData.userId === this.author) {
        const data = {
          apiDocId: this.curMethodApiData.apiDocId
        }

        deleteMethodApiData(data).then(res => {
          if (res.data.code === '000') {
            // this.methodApiDataList = this.methodApiDataList.forEach(item => {
            //   if (!this.curMethodApiData.apiDocId === item.apiDocId) {
            //     return item
            //   }
            // })
            this.initMethodApiData()
            this.curMethodApiData = ''
            this.$message({
              type: 'success',
              message: '删除成功'
            })
          } else {
            this.$message({
              message: res.data.message || '删除失败',
              type: 'warning'
            })
          }
        }).catch((error) => {
          this.$message({
            message: error || '删除失败',
            type: 'warning'
          })
        })
      }
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
      console.log('saveMethodApiData.bodyJsonData', this.pageData.bodyJsonData)
      // 解析各参数为表单数据
      this.methodApiData.docTitle = ''
      this.methodApiData.headerJsonValues = parseRequestData(this.pageData.headerJson)
      this.methodApiData.getTypeParamValues = parseRequestData(this.pageData.getTypeParam)
      this.methodApiData.bodyJsonDataValues = parseRequestData(this.pageData.bodyJsonData)
      this.methodApiData.responseBodyValues = parseParams(this.pageData.responseBody)
      this.methodApiData.dialogTableVisible = true // 关闭对话框
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
<style lang="scss" scoped>
.method-navbar {
  position: relative;
  height: 40px;
  background: #fff;
  margin: 10px 10px 10px 10px;
  border-radius: 5px;

  .left-menu {
    float: left;
    height: 100%;
    padding-left: 10px;
    line-height: 40px;

    &:focus {
      outline: none;
    }

    .left-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      vertical-align: text-bottom;

      &:hover {
        cursor: pointer;
      }
    }
  }
  .right-menu {
    float: right;
    height: 100%;
    padding-left: 10px;
    line-height: 40px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      vertical-align: text-bottom;

      &:hover {
        cursor: pointer;
      }
    }
  }
}

.doc-title-dot::before {
  content: '';
  position: relative;
  display: inline-block;
  height: 10px;
  width: 10px;
  border-radius: 50%;
  background: rgb(51, 255, 0);
  left: -10px;
  top: 0px;
}
</style>
