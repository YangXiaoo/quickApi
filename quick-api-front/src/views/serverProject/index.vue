<template>
  <div>
    <el-card style="margin-top: 70px">
      <div class="method-title">
        {{ methodName }} + 下拉框选择示列
      </div>
    </el-card>
    <el-card>
      <div>
        <el-tabs :tab-position="'left'">
          <el-tab-pane label="文档">
            <div>
              <el-button type="success" style="width: 10%">{{
                selectType
              }}</el-button>
              <el-tag type="success" style="width: 80%">
                {{ path }}
              </el-tag>
            </div>
            <el-divider />
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
              <el-input v-model="responseHeaderDesc" type="textarea" :rows="4" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="调试">
            <request-template
              :page-data="pageData"
              :url="url"
              :is-local-project="false"
              @clickSave="handleClickSave"
              @clickSaveAs="handleClickSaveAs"
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
    // TODO 初始化文档信息
  },
  methods: {
    initBaseData() {
      this.url = getLocalProjectPath(this.$route.path)
      this.pageData.path = this.localServiceName + this.url
      this.userName = this.author || 'dummyUser' // 测试使用
      this.methodName = this.$route.meta.title
      this.methodGroup = this.$route.meta.group
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
