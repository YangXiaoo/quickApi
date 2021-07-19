<template>
  <div class="push-container">
    <el-button @click="syncApiDataList">同步接口</el-button>
    <el-table
      ref="multipleTable"
      :data="dataList"
      tooltip-effect="dark"
      style="width: 50%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column
        prop="name"
        label="方法别名"
        width="120"
      />
      <el-table-column
        prop="methodName"
        label="方法名"
        show-overflow-tooltip
      />
      <el-table-column
        prop="url"
        label="URL"
        show-overflow-tooltip
      />
      <el-table-column
        prop="deleteFlag"
        label="删除/新增"
      >
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.deleteFlag === '00' ? 'primary' : 'success'"
            disable-transitions
          >{{ scope.row.deleteFlag === '00' ? '新增' : '删除' }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getDeleteMethodList, getUploadMethodList, syncProjectApiMethod, getLocalProjectData } from '@/api/localProject'
export default {
  name: 'PushLocalApi',
  data() {
    return {
      loading: false,
      dialogVisible: this.trigger,
      uploadMethodList: [],
      deleteMethodList: [],
      dataList: [],
      multiSelectList: []
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
  created() {
    this.getSyncDataList()
  },
  methods: {
    getSyncDataList() {
      this.resetDataList()
      // 获得远程服务有，而本地没有的接口，红色显示，左边有选择框，选择后点击同步会删除远端接口
      // 获得远程服务没有，而本地有的接口，蓝色显示，左边有选择框，选择后点击同步会删除远端接口
      getDeleteMethodList().then((res) => {
        if (res.data.code === '000') {
          this.deleteMethodList = res.data.data || []
          this.deleteMethodList.forEach(item => {
            this.$set(item, 'deleteFlag', '01')
          })
        }
        this.dataList.push(...this.deleteMethodList)
      })
      getUploadMethodList().then((res) => {
        if (res.data.code === '000') {
          this.uploadMethodList = res.data.data || []
          this.dataList.push(...this.uploadMethodList)
          this.uploadMethodList.forEach(item => {
            this.$set(item, 'deleteFlag', '00')
          })
        }
      })
    },
    resetDataList() {
      this.dataList = []
      this.deleteMethodList = []
      this.uploadMethodList = []
    },
    handleSelectionChange(val) {
      this.multiSelectList = val
    },
    syncApiDataList() {
      // 同步接口
      console.log('multiSelectList', this.multiSelectList)
      syncProjectApiMethod(this.multiSelectList).then(res => {
        if (res.data.code === '000') {
          this.$message({
            message: '同步成功',
            type: 'success'
          })
          getLocalProjectData()
          this.getSyncDataList()
        }
      })
    },
    filterTag(value, row) {
      return row.tag === value
    },
    switchDelteFlag(val) {
      return val === '00' ? '删除' : '新增'
    }
  }
}
</script>

<style lang="scss">
.file-import {
  display: flex;
  align-content: center;
  justify-content: center;
}
.push-container {
  margin: 10px 20px;
  width: 100%;
  font-size: 12px;
  position: relative;
  overflow: auto;
  color: #666;
  background: #fff;
  border-radius: 5px;
  border-color: rgba(0, 0, 0, .05);
}
</style>
