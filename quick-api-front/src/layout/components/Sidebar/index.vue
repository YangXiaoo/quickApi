<template>
  <div>
    <!-- <logo v-if="showLogo" :collapse="isCollapse" /> -->
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-tabs v-model="workType" type="card" @tab-click="handleWorkClick" class="side-tabs">
      <el-tab-pane label="本地测试" name="local">
        <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in sideMenu" :key="route.path" :item="route" :base-path="route.path"/>
      </el-menu>
      </el-tab-pane>
      <el-tab-pane label="协作开发" name="team">
        <div style="margin:10px">
            <el-input placeholder="搜索项目" v-model="projectName" class="input-with-select">
          <el-button slot="append" icon="el-icon-search" @click="handleProjectSearch"></el-button>
        </el-input>
        </div>
        <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="false"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in teamMenu" :key="route.path" :item="route" :base-path="route.path"/>
      </el-menu>
      </el-tab-pane>
      </el-tabs>

 
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/styles/variables.scss'
import { getRoutes, setRoutes } from '@/utils/routerTool'

export default {
  components: { SidebarItem, Logo },
  data () {
    return {
      sideMenu: [],
      workType: 'local',
      teamMenu: [],
      projectName: ''
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'routerSettingFlag',
      // 'teamRouterSettingFlag',
      'routes',
      // 'teamRoutes'
      'apiInfo'
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
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  },
  mounted () {
    this.setRoutesFromApi()
  },
  methods: {
    setRoutesFromApi () {
      if (!this.routerSettingFlag) {
        this.$store.dispatch('api/getApiRoutes', '').then(res => {
          this.$store.dispatch('api/setRouterSettingFlag', true)
          this.sideMenu = res
          setRoutes(res)
        }).catch(error => {
          this.$message(error || '加载api接口失败')
        })
      }
    },
    handleWorkClick (tab, event) {
    },
    getTeamRoute () {
      if (!this.teamRouterSettingFlag) {
        this.$store.dispatch('api/getTeamRoutes', '').then(res => {
          this.$store.dispatch('api/teamRouterSettingFlag', true)
          this.teamMenu = res
          setRoutes(res)
        }).catch(error => {
          this.$message(error || '加载api接口失败')
        })
      }
    },
    handleProjectSearch () {
      console.log(this.projectName)
      this.$store.dispatch('api/getTeamRoutes', this.projectName).then(res => {
        this.teamMenu = res
        console.log(res)
        setRoutes(res)
      }).catch(error => {
        this.$message(error || '没有该项目')
      })
    }
  },
  watch: {
    // 监听路由信息
    routes(newVal) {
      console.log('routes watch')
      this.sideMenu = newVal
    }
  }
}
</script>
<style scoped>
</style>