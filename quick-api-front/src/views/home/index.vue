<template>
  <div>

    <el-card>
      <div style="margin-top: 70px;">
        <el-row>
          <el-col :span="4">
            <el-select v-model="selectType" placeholder="请选择">
              <el-option label="POST" value="POST"></el-option>
              <!-- <el-option label="GET" value="GET"></el-option> -->
              <el-option label="PUT" value="PUT"></el-option>
              <el-option label="DELETE" value="DELETE"></el-option>
            </el-select>
          </el-col>
          <el-col :span="16">
            <el-input placeholder="请输入内容" v-model="UrlInput" class="input-with-select"></el-input>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" style="margin-left: 15px" @click="handleSendRequest">
              发送
            </el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="success">保存</el-button>
          </el-col>
        </el-row>
      </div>
      <div class="option-bar" style="margin-top: 15px;">
        <el-tabs v-model="requestActiveName" @tab-click="handleClickRequest">
          <el-tab-pane label="Param" name="Param">
            <vue-json-editor
              v-model="paramInput" 
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onParamChange"
              @json-save="onParamSave" />
          </el-tab-pane>
          <el-tab-pane label="Header" name="Header">
            <vue-json-editor
              v-model="headerJson" 
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onHeaderChange"/>
          </el-tab-pane>
          <el-tab-pane label="Body" name="Body">
            <el-radio-group v-model="bodyType" @change="changeBodyType" style="margin-bottom: 10px">
              <el-radio label="application/json">JSON</el-radio>
              <el-radio label="bodyFile">文件</el-radio>
            </el-radio-group>
             <vue-json-editor v-show="bodyJsonShow"
              v-model="bodyInput" 
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onBodyChange"
              @json-save="onBodySave" />
            <el-card class="body-file-box" v-show="bodyFileShow">
              <el-upload
                class="body-upload-file"
                action="https://jsonplaceholder.typicode.com/posts/"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :before-remove="beforeRemove"
                multiple
                :limit="3"
                :on-exceed="handleExceed"
                :file-list="fileList">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">可上传任意格式文件</div>
              </el-upload>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </div>
      <el-divider>Response</el-divider>
      <div class="request-result" style="margin-top: 15px;">
        <el-tabs v-model="responseActiveName" @tab-click="handleClickResponse">
          <el-tab-pane label="Body" name="Body">
            <vue-json-editor
              v-model="responseBody" 
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onResponseBodyChange"/>
          </el-tab-pane>
          <el-tab-pane label="Cookies" name="Cookies">
            <el-card class="response-cookies-box">
              没有Cookies
            </el-card>
          </el-tab-pane>
          <el-tab-pane label="Headers" name="Headers">
            <vue-json-editor
              v-model="responseHeader" 
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onResponseHeaderChange"/>
          </el-tab-pane>
        </el-tabs>
      </div>

    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import vueJsonEditor from 'vue-json-editor'
import { callApi } from '@/api/request'

export default {
  name: 'Home',
  components: {
    vueJsonEditor
  },
  computed: {
  ...mapGetters([
    //   'localServiceName'
    ]),
  },
  data () {
    return {
      requestActiveName: 'Param',
      responseActiveName: 'Body',
      UrlInput: '',
      paramInput: null,
      bodyInput: null,
      headerJson: null,
      responseBody: null,
      responseHeader: null,
      requestServiceName: '',
      selectType: 'POST',
      bodyType: 'application/json',
      bodyJsonShow: true,
      bodyFileShow: false
    }
  },
  mounted () {
    this.setCurRul()
  },
  methods: {
    handleClickRequest (tab, event) {
      console.log(tab, event)
    },
    handleClickResponse (tab, event) {
      console.log(tab, event)
    },
    handleSendRequest () {
      callApi(this.UrlInput, this.bodyType, this.headerJson, this.paramInput, this.selectType || 'post').then(res => {
        this.responseBody = res.data
        this.responseHeader = res.headers
      })
    },
    // 设置当前输入框的请求
    setCurRul () {
      this.UrlInput = 'http://localhost'
    },
    onParamChange (value) {
      console.log(value)
    },
    onParamSave (value) {
      console.log(value)
    },
    onHeaderChange (value) {
      console.log(value)
    },
    onBodyChange (value) {
      console.log(value)
    },
    onBodySave (value) {
      console.log(value)
    },
    changeBodyType (value) {
      if (value === 'bodyJSON') {
        this.bodyFileShow = false
        this.bodyJsonShow = true
      } else if (value === 'bodyFile') {
        this.bodyJsonShow = false
        this.bodyFileShow = true
      }
    }
  },
  onResponseBodyChange (value) {
    console.log(value)
  },
  onResponseHeaderChange (value) {
    console.log(value)
  }
}
</script>
<style scoped>
  .el-tag {
    margin-left: 10px;
  }

  /* .el-select .el-input {
    width: 130px;
  } */
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
</style>
