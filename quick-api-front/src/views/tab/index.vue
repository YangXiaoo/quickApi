<template>
  <div>
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
          <el-select v-model="dialogObj.group" filterable allow-create default-first-option placeholder="请选择组别">
            <el-option v-for="item of userGroupList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="接口名">
          <el-input v-model="dialogObj.name" />
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
import { updateUserMethodData } from '@/api/methodData'

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
        name: '',
        group: ''
      },

      url: '', // 页面接口的url，一旦创建不会改变
      name: '',
      group: '',
      userName: ''
    }
  },
  computed: {
    ...mapGetters([
      'userGroupList'
    ])
  },
  mounted() {
    this.initBaseData()
    this.initMethodApiData()
  },
  created() { },
  methods: { // 按照页面功能顺序定义方法
    initBaseData() {
      this.userName = this.$store.getters.name
      this.name = this.$route.meta.title
      this.group = this.$route.meta.group
    },
    /**
     * 从服务端请求初始化当前页面数据
     */
    initMethodApiData() {
      const curPath = this.$route.path
      if (curPath === '/home') { // 首页不初始化
        this.url = 'http://localhost'
        return
      } else {
        this.url = curPath.substring(curPath.substring(1).indexOf('/') + 1)
      }

      const data = {
        userName: this.userName,
        url: this.url
      }

      getUserMethodApiData(data).then((res) => {
        if (res.data.code === '000') {
          if (res.data.data) {
            Object.assign(this.$data.pageData, JSON.parse(res.data.data.apiJsonData).pageData) // 只初始化页面请求数据，不初始化文档
          }
        }
      })
    },
    /* 保存当前页面数据 */
    handleClickSave() {
      if (this.title === 'undefined') { // 需要保存页面信息
        this.dialogObj.dialogTitle = '保存方法信息'
        this.dialogObj.visible = true
      } else {
        this.saveMethodApiData()
      }
    },
    handleDialogSave() {
      if (this.dialogObj.dialogTitle === '保存方法信息') { // 保存方法
        this.saveMethodData()
        this.resetDialog()
      } else {
        // 另存为新的页面
      }
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
        url: this.url,
        name: this.name,
        methodGroup: this.methodGroup
      }
      this.$store.dispatch('userApi/updateUserMethodData', data)
      updateUserMethodData(data).then(res => {
        if (res.data.code === '000') {
          this.$message({
            message: '保存成功',
            type: 'success'
          })
        } else {
          this.$message({
            message: res.data.message || '保存失败',
            type: 'error'
          })
        }
      })
    },
    // 弹框修改方法名和组别
    handleMethodChangeClick() {
    },
    resetDialog() {
      this.dialogObj.dialogTitle = ''
      this.dialogObj.visible = false
      this.dialogObj.name = ''
      this.dialogObj.group = ''
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
