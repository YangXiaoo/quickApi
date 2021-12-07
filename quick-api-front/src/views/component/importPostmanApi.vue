<template>
  <div>
    <el-dialog
      title="导入"
      :visible.sync="dialogVisible"
      width="30%"
      center
    >
      <div class="file-import">
        <el-upload
          ref="import"
          class="upload-demo"
          drag
          action="dummy"
          :multiple="false"
          :limit="1"
          :on-change="handleChange"
          :file-list="fileList"
          :auto-upload="false"
        >
          <i class="el-icon-upload" />
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeImportDialog">取 消</el-button>
        <el-button type="primary" @click="submitImport">确定导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { parsePostmanJsonToQuickApi } from '@/utils/commonHelper'
import { saveUserMethodData } from '@/api/methodData'
import { saveUserMethodApiData } from '@/api/methodApiData'
export default {
  name: 'ImportPostmanApi',
  props: {
    trigger: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      dialogVisible: this.trigger,
      fileList: []
    }
  },
  computed: {
    ...mapGetters([
      'isLogin',
      'author',
      'loginName'
    ])
  },
  watch: {
    trigger(newValue) {
      this.dialogVisible = true
    }
  },
  methods: {
    beforeUpload(file) {
      // 检查后缀
    },
    importApi(content) {
      console.log('importApi', content)
      var reader = new FileReader()
      reader.readAsText(content, 'utf-8')
      reader.onload = () => {
        const fileContent = reader.result
        const apiJson = JSON.parse(fileContent)
        const data = {
          data: parsePostmanJsonToQuickApi(this.loginName, apiJson)
        }
        console.log('apiJosn', data)
        data.data.forEach(item => {
          saveUserMethodData(item.api)
          saveUserMethodApiData(item.pageData)
        })
        // saveUserPostmanApiList(data).then(res => {
        //   if (res.data.code === '000') {
        //     this.$$message({
        //       message: '导入成功',
        //       type: 'info'
        //     })
        //   }
        // }).catch(ignore => {
        //   this.$message({
        //     message: '导入失败，格式错误',
        //     type: 'error'
        //   })
        // })
      }
    },
    closeImportDialog() {
      this.dialogVisible = false
    },
    handleChange(file, fileList) {
      this.fileList = fileList
    },
    submitImport() {
      console.log('submitImport', '>>>>>>>>', this.fileList)
      if (this.fileList) {
        this.fileList.forEach(file => {
          this.importApi(file.raw)
        })
      }
    }
  }
}
</script>

<style lang="scss">
.file-import {
  display: flex;
  align-content: center;
  justify-content: center
}
</style>
