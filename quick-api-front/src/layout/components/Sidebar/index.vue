<template>
  <div class="side-container">
    <div class="side-bar-container">
      <div v-if="isLocalProject" name="localApi" class="side-bar-item" @click="openUrl('LocalApiHome')">
        <div class="item-icon">
          <i class="el-icon-monitor" />
        </div>
        <div class="item-desc">
          本地
        </div>
      </div>
      <div name="userApi" class="side-bar-item" @click="openUrl('UserApiHome')">
        <div class="item-icon">
          <i class="el-icon-star-on" />
        </div>
        <div class="item-desc">
          个人
        </div>
      </div>
      <div name="projectApi" class="side-bar-item" @click="openUrl('ProjectApiHome')">
        <div class="item-icon">
          <i class="el-icon-connection" />
        </div>
        <div class="item-desc">
          项目
        </div>
      </div>
      <div name="setting" class="side-bar-item" @click="openUrl('LocalProjectSetting')">
        <div class="item-icon">
          <i class="el-icon-connection" />
        </div>
        <div class="item-desc">
          设置
        </div>
      </div>
    </div>
    <div class="side-content-container">
      <div v-if="isLocalProject" name="localApi" class="side-content-item">
        <!-- <el-menu
          :default-active="activeMenu"
          :unique-opened="false"
          :collapse-transition="false"
        >
          <sidebar-item v-for="route in localProjectMethodMenu" :key="route.path" :item="route" :base-path="route.path" />
        </el-menu> -->
        <qa-sidebar-item v-for="route in localProjectMethodMenu" :key="route.path" :item="route" :base-path="route.path" />
      </div>
      <div name="userApi" class="side-content-item">
        <!-- <el-menu
          v-show="userMethodMenu.length > 0"
          :default-active="activeMenu"
          :collapse="isCollapse"
          :unique-opened="false"
          :collapse-transition="false"
          mode="vertical"
        >
          <sidebar-item v-for="route in userMethodMenu" :key="route.path" :item="route" :base-path="route.path" />
        </el-menu> -->
        <qa-sidebar-item v-for="route in userMethodMenu" :key="route.path" :item="route" :base-path="route.path" />
        <div v-show="!userMethodMenu.length" style="align-item:center;">
          暂无接口
        </div>
      </div>
      <div name="projectApi" class="side-content-item">
        <div style="margin:10px">
          <el-input v-model="projectNameSearchParam" placeholder="搜索项目" class="input-with-select">
            <el-button slot="append" icon="el-icon-search" @click="handleProjectSearch" />
          </el-input>
        </div>
        <el-collapse v-model="collapseActiveNames" style="margin-left:10px;" @change="handleCollapseChange">
          <!-- 搜索项目 -->
          <el-collapse-item v-for="(subMenu, peojectName) in projectMethodMenu" :key="peojectName" :name="peojectName">
            <template slot="title">
              <div class="collapse-title">
                {{ peojectName }}
              </div>
            </template>
            <!-- <el-menu
              :default-active="activeMenu"
              :collapse="isCollapse"
              :unique-opened="false"
              :collapse-transition="false"
              mode="vertical"
            >
              <sidebar-item v-for="route in subMenu" :key="route.path" :item="route" :base-path="route.path" />
            </el-menu> -->
            <qa-sidebar-item v-for="route in subMenu" :key="route.path" :item="route" :base-path="route.path" />
          </el-collapse-item>
        </el-collapse>
      </div>
      <div name="setting" class="side-content-item">
        <!-- <qa-sidebar-item v-for="route in localProjectMethodMenu" :key="route.path" :item="route" :base-path="route.path" /> -->
      </div>
    </div>
    <Login :trigger="loginVisible" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import QaSidebarItem from './QaSidebarItem'
import Login from '@/views/login'
import variables from '@/styles/variables.scss'

export default {
  name: 'SidebarIndex',
  components: { SidebarItem, Login, QaSidebarItem },
  data() {
    return {
      localProjectMethodMenu: [],
      workType: 'local',
      projectMethodMenu: {}, // 多个项目
      userMethodMenu: [],
      projectNameSearchParam: '',
      collapseActiveNames: [],
      loginVisible: false,
      showDialog: false
    }
  },
  computed: {
    ...mapGetters([
      'author', // 测试项目开发者, 即启动本地项目用户名
      'localProjectRoutes',
      'projectRoutes',
      'userRoutes',
      'username',
      'isLocalProject',
      'isLogin',
      'loginName'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    variables() {
      return variables
    },
    isCollapse() {
      return false
    }
  },
  watch: {
    // 监听路由信息并更新菜单
    localProjectRoutes(newVal) {
      this.localProjectMethodMenu = newVal
    },
    // deep: true
    projectRoutes(newVal) {
      this.projectMethodMenu = newVal
    },
    userRoutes(newVal) {
      this.userMethodMenu = newVal
    },
    isLogin(newValue) {
      if (newValue) {
        this.setUserMethodDataRoutes()
      }
    }
  },
  created() {
    if (this.checkLogin()) {
      this.setUserMethodDataRoutes()
    }
    this.setLocalProjectRoutes()
  },
  mounted() {
    this.tabMouseOn()
  },
  methods: {
    openUrl(path) {
      if (path !== 'LocalApiHome') {
        if (this.isLocalProject) {
          this.checkLogin()
        }
      }
      console.log('openUrl', path)
      this.$router.push({ name: path })
    },
    /** 初始化测试项目的路由，设置左侧菜单 */
    setLocalProjectRoutes() {
      if (!this.isLocalProject) {
        return
      }
      this.$store.dispatch('localProject/setLocalProjectRoutes', '').then(res => {
        this.localProjectMethodMenu = res
      }).catch(error => {
        this.$message(error || '加载测试项目api接口失败')
      })
    },
    /** 初始化个人接口路由，并设置左侧菜单 */
    setUserMethodDataRoutes() {
      const data = {
        userName: this.loginName // this.author
      }
      this.$store.dispatch('userMethodData/setUserMethodDataRoutes', data).then(res => {
        console.log('sideBar.setUserMethodDataRoutes', res)
        this.userMethodMenu = res
      }).catch(error => {
        this.$message(error || '初始化个人接口方法信息失败')
      })
    },
    /** 搜索项目，初始化路由，并设置左侧菜单 */
    handleProjectSearch() {
      if (!this.checkLogin()) {
        return
      }
      console.log(this.projectNameSearchParam)
      const data = {
        projectName: this.projectNameSearchParam
      }

      this.$store.dispatch('projectMethodData/setProjectMethodDataRoutes', data).then(res => {
        // 由于可以搜索多个项目
        // 因此此处为增量添加项目菜单信息
        // 使用this.$set强制刷新
        this.$set(this.projectMethodMenu, this.projectNameSearchParam, res)
      }).catch(error => {
        this.$message(error || '没有找到该项目')
      })
    },
    handleCollapseChange(val) {
      // todo
    },
    handleWorkClick(tab, event) {
      // todo
    },
    tabMouseOn() {
      console.log('tabMouseOn()', '>>>>>>>>>>>>>>>>>>>>')
      const sideBarList = document.getElementsByClassName('side-bar-item')
      const sideContentList = document.getElementsByClassName('side-content-item')
      var contentMap = {}
      for (let i = 0; i < sideContentList.length; i++) {
        contentMap[sideContentList[i].getAttribute('name')] = sideContentList[i]
      }

      for (let i = 0; i < sideBarList.length; i++) {
        sideBarList[i].onmousedown = function() {
          for (let k = 0; k < sideContentList.length; k++) {
            sideContentList[k].setAttribute('class', 'hidden')
          }
          for (let j = 0; j < sideBarList.length; j++) {
            sideBarList[j].setAttribute('class', 'side-bar-item')
          }

          this.setAttribute('class', 'current side-bar-item')
          if (this.getAttribute('name') in contentMap) {
            contentMap[this.getAttribute('name')].setAttribute('class', 'side-content-item')
          }
        }
      }

      var settingFlag = false
      for (let i = 0; i < sideBarList.length; i++) {
        console.log('sideBarList[i].getAttribute(name)', sideBarList[i].getAttribute('name'))
        if (!settingFlag) {
          sideBarList[i].setAttribute('class', 'current side-bar-item')
          if (sideBarList[i].getAttribute('name') in contentMap) {
            contentMap[sideBarList[i].getAttribute('name')].setAttribute('class', 'side-content-item')
          }
          settingFlag = true
        } else {
          sideBarList[i].setAttribute('class', 'side-bar-item')
          if (sideBarList[i].getAttribute('name') in contentMap) {
            contentMap[sideBarList[i].getAttribute('name')].setAttribute('class', 'hidden')
          }
        }
      }
    },
    checkLogin() {
      if (!this.isLogin) {
        if (this.isLocalProject) {
          this.loginVisible = true
        } else {
          this.$message('请登录')
          this.loginVisible = true
        }

        return false
      }

      return true
    }
  }
}
</script>
<style lang="scss" scoped>
  @import "~@/styles/variables.scss";
  .side-container {
    display: flex;
    background: orange;
    flex-flow: row nowrap;
  }

  .hidden {
    display: none;
  }

  .show {
    display: inherit;
  }

  .side-bar-container {
    width: $sideBarWidth;
    background: #ffff;
    box-sizing: border-box;
    border-right: solid #f7f7f7;

    .side-bar-item {
      height: 50px;
      margin: 0 5px 5px 5px;
      cursor: pointer;
      font-size: 12px;

      &:first-of-type {
        margin-top: 5px;
      }
      &:hover {
        background: #d8d8d8;
        border-radius: 5px;
      }

      .item-icon {
        margin-left: 3px;
        font-size: 30px;
      }
      .item-desc {
        margin-left: 6px;
        margin-top: -2px;
      }
    }
  }

  .current {
    background: #d8d8d8;
    // margin: 10px 10px 10px 10px;
    border-radius: 5px;
  }
  .side-content-container {
    width: $sideContentWidth;
    background: #fff;

    .side-content-item {
      height: 100%;
      overflow-y: auto;
    }
  }
  .collapse-title {
    font-size: 16px;
    font-weight: 400;
    margin-left: 8px;
  }
</style>
