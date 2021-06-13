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
          :before-upload="beforeUpload"
          :limit="1"
          :auto-upload="false"
          :http-request="importApi"
        >
          <i class="el-icon-upload" />
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
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
      dialogVisible: this.trigger
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
      console.log('importPostmanApi.watch.trigger', newValue)
      this.dialogVisible = true
    }
  },
  methods: {
    beforeUpload(file) {
      // 检查后缀
    },
    importApi(content) {
      var reader = new FileReader()
      reader.readAsText(content.file, 'utf-8')
      reader.onload = () => {
        const fileContent = reader.result
        const apiJson = JSON.parse(fileContent)
        const data = {
          api: apiJson,
          username: this.loginName
        }
        this.$store.dispatch('userMethodData/importPostmanApi', data)
      }
    },
    closeImportDialog() {
      this.dialogVisible = false
    },
    submitImport() {
      this.$refs.import.submit()
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
