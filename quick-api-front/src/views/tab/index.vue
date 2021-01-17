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
      :is-local-project="false"
      @clickSave="handleClickSave"
      @clickSaveAs="handleClickSaveAs"
    />

    <!-- 保存弹框 -->
    <el-dialog :title="dialogObj.dialogTitle" :visible.sync="dialogObj.visible">
      <el-form>
        <el-form-item label="所属组">
          <el-select v-model="dialogObj.methodGroup" filterable allow-create default-first-option placeholder="请选择组别">
            <el-option v-for="item of userGroupList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="接口名">
          <el-input v-model="dialogObj.methodName" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogObj.visible = false">取 消</el-button>
        <el-button type="primary" @click="handleDialogSave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getUserMethodApiData, saveUserMethodApiData } from '@/api/methodApiData'

import RequestTemplate from '../component/requestTemplate.vue'
export default {
  name: 'Tab',
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
      // 弹框
      dialogObj: {
        dialogTitle: '',
        visible: false,
        methodName: '',
        methodGroup: ''
      },

      url: '', // 页面接口的url，一旦创建不会改变
      methodName: '',
      methodGroup: '',
      userName: ''
    }
  },
  computed: {
    ...mapGetters([
      'userGroupList',
      'author'
    ])
  },
  mounted() {
    this.initBaseData()
    this.initMethodApiData()
  },
  created() { },
  methods: { // 按照页面功能顺序定义方法
    initBaseData() {
      this.url = this.$route.path // 页面URL在新建时已确定
      this.userName = this.author || 'dummyUser' // 测试使用
      this.methodName = this.$route.meta.title
      this.methodGroup = this.$route.meta.group
    },
    /**
     * 从服务端请求初始化当前页面数据
     */
    initMethodApiData() {
      console.log('initMethodApiData', this.methodName)
      if (this.methodName === 'undefined') {
        return
      }

      const data = {
        userName: this.userName,
        url: this.url
      }

      getUserMethodApiData(data).then((res) => {
        if (res.data.code === '000') {
          if (res.data.data) {
            Object.assign(this.$data.pageData, JSON.parse(res.data.data.apiJsonData)) // 只初始化页面请求数据，不初始化文档
          }
        }
      })
    },
    /* 保存当前页面数据 */
    handleClickSave() {
      // 对于新建的页面则需要定义方法名等信息
      if (this.methodName === 'undefined') {
        this.dialogObj.dialogTitle = '保存方法信息'
        this.dialogObj.visible = true
      } else {
        // 对已经创建的页面，点击保存只保存页面数据
        this.saveMethodApiData()
      }
    },
    handleDialogSave() {
      if (this.dialogObj.dialogTitle === '保存方法信息') {
        // 第一次保存时，不仅要保存方法信息还要保存接口数据
        this.saveMethodData()
        this.saveMethodApiData()
      } else if (this.dialogObj.dialogTitle === '修改方法信息') {
        // 修改方法信息
        this.updateUserMethodData()
      }
      this.resetDialog()
    },
    saveMethodApiData() {
      // 保存页面数据
      const pageData = JSON.stringify(this.$data.pageData)
      const data = {
        url: this.url,
        apiData: pageData,
        userName: this.userName
      }

      saveUserMethodApiData(data)
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
    /**
     * 另存为页面
     */
    handleClickSaveAs() {

    },
    saveMethodData() {
      const data = {
        userName: this.userName, // 推荐使用邮箱
        url: this.url, // 路由
        methodName: this.dialogObj.methodName, // 方法名
        methodGroup: this.dialogObj.methodGroup
      }
      this.$store.dispatch('userMethodData/saveUserMethodData', data)
      this.methodName = this.dialogObj.methodName
      this.methodGroup = this.dialogObj.methodGroup
    },
    updateUserMethodData() {
      const data = {
        userName: this.userName, // 推荐使用邮箱
        url: this.url, // 路由
        methodName: this.dialogObj.methodName, // 方法名
        methodGroup: this.dialogObj.methodGroup
      }
      this.$store.dispatch('userMethodData/updateUserMethodData', data).then(res => {
        this.methodName = data.methodName
        this.methodGroup = data.methodGroup
      }).catch((error) => {
        this.$message({
          message: error || '更新失败',
          type: 'warning'
        })
      })
    },
    // 弹框修改方法名和组别
    handleMethodChangeClick() {
      this.dialogObj.dialogTitle = '修改方法信息'
      this.dialogObj.methodName = this.methodName
      this.dialogObj.methodGroup = this.methodGroup
      this.dialogObj.visible = true
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
