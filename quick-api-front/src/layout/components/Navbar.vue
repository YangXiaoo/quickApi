<template>
  <div class="navbar">
    <div class="left-menu">
      <div class="left-menu-item" @click="handleHomeClick">
        <i class="el-icon-s-home" />
      </div>
      <div class="left-menu-item" @click="handleImportClick">
        <i class="el-icon-bottom" />
      </div>
      <div class="left-menu-item" @click="handleNewTabClick">
        <i class="el-icon-plus" />
      </div>
    </div>
    <div class="right-menu">
      <el-dropdown class="right-menu-item hover-effect" trigger="click">
        <i class="el-icon-setting" @click="handleProjectSetting" />
        <el-dropdown-menu slot="dropdown">
          <router-link to="/settings/localProjectSetting">
            <el-dropdown-item>本地项目设置</el-dropdown-item>
          </router-link>
          <router-link v-show="serviceProjectFlag" to="/settings/serviceProjectSetting">
            <el-dropdown-item>远端接口调用设置</el-dropdown-item>
          </router-link>
        </el-dropdown-menu>
      </el-dropdown>
      <div class="right-menu-item" @click="handleRecordClick">
        <i class="el-icon-time" />
      </div>
      <div class="right-menu-item" @click="handleLoginClick">
        <i class="el-icon-user" />
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { generateNewTabPage } from '@/utils/routerTool'
export default {
  data() {
    return {
      serviceProjectFlag: false
    }
  },
  computed: {
    ...mapGetters([
      'projectMethodDataList'
    ])
  },
  watch: {
    projectRoutes(newValue) {
      console.log('projectMethodDataList.watch', newValue)
      this.serviceProjectFlag = Object.keys(newValue).length !== 0
    }
  },
  methods: {
    /**
     * 返回首页
     */
    handleHomeClick() {
      this.$router.push({ name: 'Home' })
    },
    /**
     * 新建页面
     */
    handleNewTabClick() {
      const pageName = generateNewTabPage()
      this.$router.replace({ name: pageName })
    },
    handleImportClick() {
      this.$message({
        type: 'info',
        message: '导入功能，暂未开发'
      })
    },
    handleSettingClick() {
      this.$message({
        type: 'info',
        message: '设置功能，暂未开发'
      })
    },
    handleRecordClick() {
      // this.$message({
      //   type: 'info',
      //   message: '提交记录功能，暂未开发'
      // })
      this.$router.push({ name: 'History' })
    },
    handleLoginClick() {
      this.$message({
        type: 'info',
        message: '用户登录功能，暂未开发'
      })
    },
    handleProjectSetting() {
      console.log('handleProjectSetting', this.projectMethodDataList)
      this.serviceProjectFlag = Object.keys(this.projectMethodDataList).length !== 0
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/variables.scss";
.navbar {
  height: 40px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .left-menu {
    float: left;
    height: 100%;
    line-height: 40px;
    padding-left: $sideBarWidth;

    &:focus {
      outline: none;
    }

    .left-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &:hover {
        cursor: pointer;
        transition: background .3s;
        background: rgba(59, 224, 133, 0.025)
      }

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          cursor: pointer;
          background: rgba(0, 0, 0, .025)
        }
      }
    }
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 40px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &:hover {
        cursor: pointer;
        transition: background .3s;
        background: rgba(59, 224, 133, 0.025)
      }

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
