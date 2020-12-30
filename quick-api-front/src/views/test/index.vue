<template>
  <div>
    <div>
      <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
        {{tag}}
      </el-tag>
    </div>

    <el-card>
      <div style="margin-top: 15px;">
        <el-row>
          <el-col :span="4">
            <el-select v-model="selectType" placeholder="请选择">
              <el-option label="POST" value="1"></el-option>
              <el-option label="GET" value="2"></el-option>
              <el-option label="DELETE" value="3"></el-option>
            </el-select>
          </el-col>
          <el-col :span="16">
            <el-input placeholder="请输入内容" v-model="inputURL" class="input-with-select"></el-input>
          </el-col>
          <el-col :span="2">
            <el-button type="primary">
              发送
            </el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="success">保存</el-button>
          </el-col>
        </el-row>
      </div>
      <div class="option-bar" style="margin-top: 15px;">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="Param" name="first">Param</el-tab-pane>
          <el-tab-pane label="Header" name="second">Header</el-tab-pane>
          <el-tab-pane label="Body" name="third">Body</el-tab-pane>
        </el-tabs>
      </div>
      <el-divider>Response</el-divider>
      <div class="request-result" style="margin-top: 15px;">
        <el-tabs v-model="activeName1" @tab-click="handleClick">
          <el-tab-pane label="Body" name="first">Body</el-tab-pane>
          <el-tab-pane label="Cookies" name="second">Cookies</el-tab-pane>
          <el-tab-pane label="Headers" name="third">Headers</el-tab-pane>
        </el-tabs>
      </div>

    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Test',
  data () {
    return {
      dynamicTags: ['标签一', '标签二', '标签三'],
      activeName: 'second',
      activeName1: 'first',
      inputURL: '',
      selectType: '1',
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      value: ''
    }
  },
  mounted () {},
  methods: {
    handleClose (tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
    },
    handleClick (tab, event) {
      console.log(tab, event)
    },
    handleSend () {
      alert('button click')
    }
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
</style>
