<template>
  <div>
    <!-- <logo v-if="showLogo" :collapse="isCollapse" /> -->
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-tabs v-model="workType" type="card" class="side-tabs" @tab-click="handleWorkClick">
        <el-tab-pane label="测试项目" name="local">
          <el-menu
            :default-active="activeMenu"
            :unique-opened="false"
            :collapse-transition="false"
          >
            <sidebar-item v-for="route in localProjectMethodMenu" :key="route.path" :item="route" :base-path="route.path" />
          </el-menu>

          <!-- <el-collapse-item v-for="(subMenu, peojectName) in projectMethodMenu" :key="peojectName" :name="peojectName">
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
          </el-collapse-item> -->
        </el-tab-pane>
        <el-tab-pane label="接口文档" name="team">
          <div style="margin:10px">
            <el-input v-model="projectNameSearchParam" placeholder="搜索项目" class="input-with-select">
              <el-button slot="append" icon="el-icon-search" @click="handleProjectSearch" />
            </el-input>
          </div>
          <!-- 个人接口 -->
          <el-collapse v-model="collapseActiveNames" @change="handleCollapseChange">
            <el-collapse-item name="userMethodData">
              <template slot="title">
                <div class="collapse-title">
                  <i class="el-icon-user" />用户接口
                </div>
              </template>
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
            </el-collapse-item>

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
        </el-tab-pane>
      </el-tabs>

    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'

export default {
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
      'username'
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
  methods: {
    /** 初始化测试项目的路由，设置左侧菜单 */
    setLocalProjectRoutes() {
      this.$store.dispatch('localProject/setLocalProjectRoutes', '').then(res => {
        console.log('sideBar.setLocalProjectRoutes', res)
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
    }
  }
}
</script>
<style scoped>
.el-collapse-item__header {
  background-color: #f1f4f7;
  /* border-bottom: 1px solid #5ed0ec; */
}
.collapse-title {
  margin-left: 10px !important;
  font-size: 14px;
}
</style>
