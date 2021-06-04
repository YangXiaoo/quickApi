<template>
  <div>
    <div class="method-navbar">
      <div class="left-menu">
        <div class="left-menu-item">
          {{ methodName }}
        </div>
      </div>
      <div class="right-menu">
        <div v-show="curMethodApiData" class="right-menu-item">
          <span class="doc-title-dot">{{ methodApiData.docTitle }} </span>
        </div>
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
    <div v-show="methodApiDataList && methodApiDataList.length > 0" class="request-main">
      <el-tabs :tab-position="'left'">
        <el-tab-pane label="文档">
          <div class="doc-container">
            <div>
              <el-button type="success" size="small" style="width: 10%">{{ pageData.requestType }}</el-button>
              <el-tag type="success" style="width: 80%; margin-left:10px;">
                {{ pageData.path }}
              </el-tag>
            </div>
            <el-divider />
            <div>
              <el-button type="success" size="small" style="width: 10%">请求类型</el-button>
              <el-tag style="width: 80%; margin-left: 10px;">{{ pageData.contentType }} </el-tag>
            </div>
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
                    <span> {{ row.required }} </span>
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
                    <span> {{ row.description }} </span>
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
                    <span> {{ row.required }} </span>
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
                    <span> {{ row.description }} </span>
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
                    <span> {{ row.required }} </span>
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
                    <span> {{ row.description }} </span>
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
                    <span> {{ row.description }} </span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div v-show="pageData.responseHeader">
              <el-divider>响应头说明</el-divider>
              <el-input v-model="methodApiData.responseHeaderDesc" type="textarea" :rows="4" placeholder="请输入说明" />
            </div>
            <div v-show="methodApiData.remark">
              <el-divider>备注(可选)</el-divider>
              <el-input v-model="methodApiData.remark" type="textarea" :rows="4" placeholder="文档备注" />
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="调试">
          <request-template
            :page-data="pageData"
            :is-local-project="false"
            :is-show-save="false"
          />
        </el-tab-pane>
      </el-tabs>
    </div>
    <div v-show="!methodApiDataList || methodApiDataList.length === 0" class="request-main">
      <div class="no-content" style="width: 300px;margin: 40px auto;">
        没有文档
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getMethodApiData } from '@/api/methodApiData'
import { getLocalProjectPath } from '@/utils/commonHelper'
import RequestTemplate from '../component/requestTemplate.vue'

export default {
  name: 'ProjectApi',
  components: {
    RequestTemplate
  },
  data() {
    return {
      pageData: {
        requestActiveName: 'Body',
        responseActiveName: 'Body',
        path: '', // 完整的请求路径
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
        docTitle: '',
        headerJsonValues: [], // 头参数说明
        getTypeParamValues: [], // 存储所有参数
        bodyJsonDataValues: [], // post请求参数说明
        bodyStringDataValues: [], // String类请求
        responseBodyValues: [],
        responseHeaderDesc: [],
        remark: ''
      },

      url: '', // 页面接口的url，一旦创建不会改变
      methodName: '',
      methodGroup: '',
      userName: '',
      projectName: '',
      methodApiDataList: [],
      curMethodApiData: ''
    }
  },
  computed: {
    ...mapGetters([
      'localProjectName',
      'localProjectGroupList',
      'serviceProjectAddress',
      'username'
    ])
  },
  created() {
    this.initBaseData()
    this.initMethodApiData()
  },
  methods: {
    initBaseData() {
      this.userName = this.username // 测试使用
      this.methodName = this.$route.meta.title
      this.methodGroup = this.$route.meta.group
      this.projectName = this.$route.meta.projectName // 项目名
      this.url = getLocalProjectPath(this.$route.path)
    },
    /** 设置接口文档 */
    initMethodApiData() {
      const data = {
        projectName: this.projectName,
        url: this.url
      }

      /** 可能存在多个文档，默认初始化第一个文档 */
      getMethodApiData(data).then(res => {
        if (res.data.code === '000' && res.data.data && res.data.data.length > 0) {
          this.methodApiDataList = res.data.data.map(item => {
            this.$set(item, 'docTitle', JSON.parse(item.apiJsonData).methodApiData.docTitle)
            return item
          })
          console.log('getMethodApiData', this.methodApiDataList)
          if (this.methodApiDataList && this.methodApiDataList.length > 0) {
            this.curMethodApiData = this.methodApiDataList[0]
            console.log('getMethodApiData.curMethodApiData', this.curMethodApiData)
            this.setPageFromApiData()
            console.log('getMethodApiData.projectName', this.projectName)
            console.log('getMethodApiData.serviceProjectAddress', this.serviceProjectAddress)
            if (this.projectName in this.serviceProjectAddress) {
              const serviceAddress = this.serviceProjectAddress[this.projectName]
              this.pageData.path = 'http://' + serviceAddress + this.url
            }
          }
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
      if (this.projectName in this.serviceProjectAddress) {
        const serviceAddress = this.serviceProjectAddress[this.projectName]
        this.pageData.path = 'http://' + serviceAddress + this.url
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
.doc-container {
  padding: 0px 100px 0px 100px;
}
</style>
