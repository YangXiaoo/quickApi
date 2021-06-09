<template>
  <div class="side-container">
    <div class="side-bar-container">
      <div v-if="isLocalProject" name="localApi" class="side-bar-item" @click="openUrl('LocalApiHome')">
        本地
      </div>
      <div name="userApi" class="side-bar-item" @click="openUrl('UserApiHome')">
        个人
      </div>
      <div name="projectApi" class="side-bar-item" @click="openUrl('ProjectApiHome')">
        项目
      </div>

    </div>
    <div class="side-content-container">
      <div v-if="isLocalProject" name="localApi" class="side-content-item">
        <el-menu
          :default-active="activeMenu"
          :unique-opened="false"
          :collapse-transition="false"
        >
          <sidebar-item v-for="route in localProjectMethodMenu" :key="route.path" :item="route" :base-path="route.path" />
        </el-menu>
      </div>
      <div name="userApi" class="side-content-item">
        <el-menu
          v-show="userMethodMenu.length > 0"
          :default-active="activeMenu"
          :collapse="isCollapse"
          :unique-opened="false"
          :collapse-transition="false"
          mode="vertical"
        >
          <sidebar-item v-for="route in userMethodMenu" :key="route.path" :item="route" :base-path="route.path" />
        </el-menu>
        <div v-show="!userMethodMenu.length" style="padding-left: 50px;">
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
            <el-menu
              :default-active="activeMenu"
              :collapse="isCollapse"
              :unique-opened="false"
              :collapse-transition="false"
              mode="vertical"
            >
              <sidebar-item v-for="route in subMenu" :key="route.path" :item="route" :base-path="route.path" />
            </el-menu>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
  name: 'SidebarIndex',
  components: { SidebarItem },
  data() {
    return {
      localProjectMethodMenu: [],
      workType: 'local',
      projectMethodMenu: {}, // 多个项目
      userMethodMenu: [],
      projectNameSearchParam: '',
      collapseActiveNames: []
    }
  },
  computed: {
    ...mapGetters([
      'author', // 测试项目开发者, 即启动本地项目用户名
      'localProjectRoutes',
      'projectRoutes',
      'userRoutes',
      'username',
      'isLocalProject'
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
    }
  },
  created() {
    this.setLocalProjectRoutes()
    this.setUserMethodDataRoutes()
  },
  mounted() {
    window.onload = () => {
      this.tabMouseOn()
    }
  },
  methods: {
    openUrl(path) {
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
        userName: this.username // this.author
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

      console.log('tabMouseOn.variables', variables)
      for (let i = 0; i < sideBarList.length; i++) {
        console.log('sideBarList[i].hasAttribute', sideBarList[i].hasAttribute('no-content'))

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
    background: #42b983;
    box-sizing: border-box;

    .side-bar-item {
      height: 40px;
      padding: 5px 2px 0px 2px;
      vertical-align: middle;
      cursor: pointer;

      &:hover {
        background: #b4b4b4;
      }
    }
  }

  .side-content-container {
    width: $sideContentWidth;
    background: #fff;

    .side-content-item {
      height: 100%;
      overflow-y: auto;
    }

    .current {
      background: #b4b4b4;
    }
  }
</style>
