<template>
  <div>
    <div class="method-navbar">
      <div class="left-menu">
        <div class="left-menu-item">
          {{ methodName }}
        </div>
      </div>
      <div class="right-menu">
        <div class="right-menu-item">
          当前文档 {{ methodApiData.docTitle }}
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
    <el-card>
      <div>
        <el-tabs :tab-position="'left'">
          <el-tab-pane label="文档">
            <div>
              <el-button type="success" style="width: 10%">{{ pageData.requestType }}</el-button>
              <el-tag type="success" style="width: 80%">
                {{ pageData.path }}
              </el-tag>
            </div>
            <el-divider />
            <div>
              <el-button type="success" style="width: 10%">请求类型</el-button>
              <el-tag style="width: 80%">{{ pageData.contentType }} </el-tag>
            </div>
            <div v-show="pageData.headerJson">
              <el-divider>请求头参数</el-divider>
              <el-table :data="methodApiData.headerJsonValues" border fit highlight-current-row>
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
            <div v-show="pageData.paramData">
              <el-divider>get参数</el-divider>
              <el-table :data="methodApiData.paramValues" border fit highlight-current-row>
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
            <div v-show="pageData.bodyJsonData">
              <el-divider>POST参数</el-divider>
              <el-table :data="methodApiData.bodyJsonDataValues" border fit highlight-current-row>
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
            <div v-show="pageData.responseBody">
              <el-divider>响应值</el-divider>
              <el-table :data="methodApiData.responseBodyValues" border fit highlight-current-row>
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
            <div v-show="methodApiData.responseHeaderDesc">
              <el-divider>响应头</el-divider>
              <el-input v-model="methodApiData.responseHeaderDesc" type="textarea" :rows="4" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="调试">
            <request-template
              :page-data="pageData"
              :url="url"
              :is-local-project="false"
              :is-show-save="false"
            />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getMethodApiData } from '@/api/methodApiData'
import { getLocalProjectPath } from '@/utils/commonHelper'
import RequestTemplate from '../component/requestTemplate.vue'

export default {
  name: 'ServerProject',
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
        responseHeaderDesc: []
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
      'localServiceName',
      'author',
      'localProjectName',
      'localProjectGroupList'
    ])
  },
  created() {
    this.initBaseData()
    this.initMethodApiData()
  },
  methods: {
    initBaseData() {
      this.url = getLocalProjectPath(this.$route.path)
      this.userName = this.author || 'dummyUser' // 测试使用
      this.methodName = this.$route.meta.title
      this.methodGroup = this.$route.meta.group
      this.projectName = this.$route.meta.projectName // 项目名
    },
    /** 设置接口文档 */
    initMethodApiData() {
      const data = {
        projectName: this.projectName,
        url: this.url
      }

      /** 可能存在多个文档，默认初始化第一个文档 */
      getMethodApiData(data).then(res => {
        if (res.data.code === '000') {
          this.methodApiDataList = res.data.data.map(item => {
            this.$set(item, 'docTitle', JSON.parse(item.apiJsonData).methodApiData.docTitle)
            return item
          })
          console.log('getMethodApiData', this.methodApiDataList)
          if (this.methodApiDataList && this.methodApiDataList.length > 0) {
            this.curMethodApiData = this.methodApiDataList[0]
            console.log('getMethodApiData.curMethodApiData', this.curMethodApiData)
            this.setPageFromApiData()
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
    }
  }
}
</script>
<style lang="scss" scoped>
.method-navbar {
  position: relative;
  height: 40px;
  background: #fff;
  margin: 80px 10px 10px 10px;

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
</style>
