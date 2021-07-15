<template>
  <div>
    <el-table
      ref="multipleTable"
      :data="uploadMethodList"
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
        label="姓名"
        width="120"
      />
      <el-table-column
        prop="methodName"
        label="方法名"
        show-overflow-tooltip
      />
      <el-table-column
        prop="methodUrl"
        label="URL"
        show-overflow-tooltip
      />
    </el-table>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getDeleteMethodList, getUploadMethodList } from '@/api/localProject'
export default {
  name: 'PushLocalApi',
  data() {
    return {
      loading: false,
      dialogVisible: this.trigger,
      uploadMethodList: [],
      deleteMethodList: []
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
      // 获得远程服务有，而本地没有的接口，红色显示，左边有选择框，选择后点击同步会删除远端接口
      // 获得远程服务没有，而本地有的接口，蓝色显示，左边有选择框，选择后点击同步会删除远端接口
      getDeleteMethodList().then((res) => {
        if (res.data.code === '000') {
          this.deleteMethodList = res.data.data || []
        }
      })
      getUploadMethodList().then((res) => {
        if (res.data.code === '000') {
          this.uploadMethodList = res.data.data || []
        }
      })
    },
    handleSelectionChange() {
      
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
</style>
