<template>
  <div>
    <div class="header">
      <div class="left-menu">
        <div class="left-menu-item" @click="handleHomeClick">
          <i class="el-icon-s-home" />
        </div>
        <div class="left-menu-item" @click="handleImportClick">
          <i class="el-icon-bottom" />
        </div>
        <div v-if="isLocalProject" class="left-menu-item" @click="handlePushApiClick">
          <i class="el-icon-upload" />
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
        <div v-if="isLocalProject" class="right-menu-item" @click="handleRecordClick">
          <i class="el-icon-time" />
        </div>
        <div v-if="!isLogin" class="right-menu-item" @click="handleLoginClick">
          <i class="el-icon-user" />
        </div>
        <div v-if="isLogin" class="right-menu-item">
          您好, {{ loginName }}
        </div>
        <div v-if="isLogin" class="right-menu-item" @click="handleLogout">
          <i class="el-icon-switch-button" />
        </div>
      </div>
    </div>
    <Login :trigger="loginVisible" />
    <ImportPostmanApi :trigger="importTrigger" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { generateNewTabPage } from '@/utils/routerTool'

import Login from '@/views/login'
import ImportPostmanApi from '@/views/component/importPostmanApi.vue'

export default {
  components: { Login, ImportPostmanApi },
  data() {
    return {
      serviceProjectFlag: false,
      loading: false,
      loginVisible: false,
      importTrigger: false
    }
  },
  computed: {
    ...mapGetters([
      'projectMethodDataList',
      'isLocalProject',
      'isLogin',
      'loginName'
    ])
  },
  watch: {
    projectRoutes(newValue) {
      this.serviceProjectFlag = Object.keys(newValue).length !== 0
    }
  },
  methods: {
    /**
     * 返回首页
     */
    handleHomeClick() {
      this.$router.push({ name: 'QuickApi' })
    },
    /**
     * 新建页面
     */
    handleNewTabClick() {
      const pageName = generateNewTabPage()
      this.$router.replace({ name: pageName })
    },
    handleImportClick() {
      console.log('Navbar.handleImportClick()', '>>>>>>>>>>>>>>>')
      if (!this.isLogin) {
        this.$message({
          type: 'info',
          message: '请登录'
        })

        return
      }
      this.importTrigger = !this.importTrigger
    },
    // 同步本地接口到远程服务器
    handlePushApiClick() {
      this.$router.push({ name: 'PushLocalApi' })
    },
    handleSettingClick() {
      this.$message({
        type: 'info',
        message: '设置功能，暂未开发'
      })
    },
    handleRecordClick() {
      this.$router.push({ name: 'History' })
    },
    handleLoginClick() {
      console.log('handleLoginClick()', '>>>>>>>>>>>>>>>>>>')
      this.loginVisible = !this.loginVisible
    },
    handleLogout() {
      this.$store.dispatch('user/logout').then(() => {
        console.log('handleLogout.loginout', '>>>>>>>>>>>>>>>>')
        location.reload()
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
.header {
  height: 40px;
  overflow: hidden;
  position: relative;
  background: #424242;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .left-menu {
    float: left;
    height: 100%;
    line-height: 40px;
    // padding-left: $sideBarWidth;

    &:focus {
      outline: none;
    }

    .left-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #f7f7f76b;
      vertical-align: text-bottom;

      &:hover {
        cursor: pointer;
        transition: background .3s;
        background: rgba(171, 190, 180, 0.164)
      }

      &:active {
        cursor: pointer;
        background: rgba(171, 190, 180, 0.164)
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
      color: #f7f7f76b;
      vertical-align: text-bottom;

      &:hover {
        cursor: pointer;
        transition: background .3s;
        background: rgba(171, 190, 180, 0.164)
      }
      &:active {
        cursor: pointer;
        background: rgba(171, 190, 180, 0.164)
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
