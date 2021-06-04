<template>
  <div class="home-container">
    <el-row :gutter="40">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <span class="card-panel-icon">
              <i class="el-icon-menu" />
            </span>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              {{ localProjectName }}
            </div>
            {{ description || '暂无描述' }}
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <span class="card-panel-icon">
              <i class="el-icon-star-on" />
            </span>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              接口总数
            </div>
            <span class="card-panel-num">{{ projectMethodDataCount }} </span>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <span class="card-panel-icon">
              <i class="el-icon-s-claim" />
            </span>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              已完成文档接口
            </div>
            <span class="card-panel-num">{{ projectFinishedMethodDataCount }} </span>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shopping">
            <span class="card-panel-icon">
              <i class="el-icon-user-solid" />
            </span>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              开发者人数
            </div>
            <span class="card-panel-num">{{ projectDeveloperCount }} </span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-card class="unfinished-table">
      <el-table :data="projectUnfinishedMethodDataList" max-height="500">
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
            {{ scope.row.url }}
          </template>
        </el-table-column>
        <el-table-column label="类名">
          <template slot-scope="scope">
            {{ scope.row.className }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="success" @click="handleClickFinishDoc(scope.row.methodName)">完成文档</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getProjectDevelopers } from '@/api/project'
import {
  getProjectFinishedMethodDataCount,
  getProjectFinishedMethodDataMap
} from '@/api/methodData'
export default {
  name: 'Home',
  components: {
  },
  data() {
    return {
      projectFinishedMethodDataCount: 0,
      projectDeveloperCount: 0,
      projectFinishedMethodDataMap: '',
      projectUnfinishedMethodDataList: []
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
    localProjectName(newVal) {
      this.initBaseData(newVal)
    },
    localMethodDataList(newVal) {
      this.initBaseData(this.localProjectName)
    }
  },
  mounted() {
  },
  created() {
  },
  methods: {
    initBaseData(projectName) {
      const data = {
        projectName: projectName
      }
      console.log('initBaseData.data', data)
      getProjectFinishedMethodDataCount(data).then(res => {
        if (res.data.code === '000') {
          this.projectFinishedMethodDataCount = res.data.data
        } else {
          this.projectFinishedMethodDataCount = '0'
        }
      })
      getProjectDevelopers(data).then(res => {
        if (res.data.code === '000') {
          this.projectDeveloperCount = res.data.data.length
        } else {
          this.projectDeveloperCount = '0'
        }
      })
      getProjectFinishedMethodDataMap(data).then(res => {
        if (res.data.code === '000') {
          this.projectFinishedMethodDataMap = res.data.data
          console.log('getProjectFinishedMethodDataMap', res.data.data)
          this.projectUnfinishedMethodDataList = this.getProjectUnfinishedMethodDataList()
        }
      })

      this.projectMethodDataCount = this.localMethodDataList.length || 0
    },
    handleClickFinishDoc(name) {
      console.log('handleClickFinishDoc.name', name)
      this.$router.push({ name: name })
    },
    getProjectUnfinishedMethodDataList() {
      console.log('projectUnfinishedMethodDataList.localMethodDataList', this.localMethodDataList)
      console.log('projectUnfinishedMethodDataList.projectFinishedMethodDataMap', this.projectFinishedMethodDataMap)

      const unfinishedList = []
      const finishedMethodDataList = Object.keys(this.projectFinishedMethodDataMap)
      console.log('projectUnfinishedMethodDataList.finishedMethodDataList', finishedMethodDataList)
      for (const methodData of this.localMethodDataList) {
        let flag = false
        for (const url of finishedMethodDataList) {
          if (url === methodData.url) {
            flag = true
          }
        }
        if (!flag) {
          unfinishedList.push(methodData)
        }
      }
      return unfinishedList
    }
  }
}
</script>
<style lang="scss" scoped>
.home-container {
  // position: relative;
  // background: #fff;
  margin: 5px 20px 10px 20px;
  border-radius: 5px;

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    border-radius: 5px;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
  .unfinished-table {
    margin-top: 20px;
  }
}
</style>
