<template>
  <div class="container">
    <el-card class="history-table">
      <el-table :data="commitHistory" max-height="500">
        <el-table-column label="方法别名">
          <template slot-scope="scope">
            {{ scope.row.name || scope.row.methodName }}
          </template>
        </el-table-column>
        <el-table-column label="方法名">
          <template slot-scope="scope">
            {{ scope.row.methodName }}
          </template>
        </el-table-column>
        <el-table-column label="路径" min-width="200">
          <template slot-scope="scope">
            {{ scope.row.methodUrl }}
          </template>
        </el-table-column>
        <el-table-column label="类名">
          <template slot-scope="scope">
            {{ scope.row.className }}
          </template>
        </el-table-column>
        <el-table-column label="提交者">
          <template slot-scope="scope">
            {{ scope.row.userId }}
          </template>
        </el-table-column>
        <el-table-column label="提交时间">
          <template slot-scope="scope">
            {{ scope.row.createTime }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="success" @click="handleClickMethod(scope.row.methodName)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getProjectMethodApiDataHistory } from '@/api/methodApiData'
export default {
  name: 'History',
  components: {
  },
  data() {
    return {
      commitHistory: []
    }
  },
  computed: {
    ...mapGetters([
      'localServiceName',
      'groupList',
      'localMethodDataList',
      'localProjectName',
      'description'
    ])
  },
  watch: {
  },
  mounted() {
  },
  created() {
    this.getCommitHistoryTableData()
  },
  methods: {
    getCommitHistoryTableData() {
      const data = {
        projectName: this.localProjectName
      }

      getProjectMethodApiDataHistory(data).then(res => {
        if (res.data.code === '000') {
          this.commitHistory = this.getFullData(res.data.data)
        }
      })
    },
    getFullData(data) {
      if (data) {
        data.forEach(item => {
          for (const method of this.localMethodDataList) {
            if (method.url === item.methodUrl) {
              item.methodName = method.methodName
              item.name = method.name
              item.className = method.className
            }
          }
        })
      }

      return data
    },
    handleClickMethod(name) {
      this.$router.push({ name: name })
    }
  }
}
</script>
<style lang="scss" scoped>
.container {
  position: relative;
  margin: 10px 10px 10px 10px;
  border-radius: 5px;

  .history-table {
    margin-top: 20px;
  }
}
</style>
