<template>
  <div>
    <el-container>
      <el-header>
        <div class="header-container">
          <div class="header-import">
            <el-button type="primary" plain><i class="el-icon-download" /></el-button>
          </div>
          <div class="header-workspace">
            <a>活动</a> |
            <a>协作</a>
          </div>
          <div class="header-right">
            <el-button type="primary" plain> <i class="el-icon-s-promotion" /></el-button>
            <el-button type="primary" plain><i class="el-icon-s-custom" /></el-button>
          </div>
        </div>
      </el-header>
      <el-container>
        <el-aside :width="asideWidth">
          <el-menu default-active="1" 
            @open="handleOpen" 
            @close="handleClose" 
            :collapse="isCollapse" 
            class="side-menu">
            <el-submenu  v-for="routes in sideMenu" :index="routes.path" :key="routes.path">
              <template slot="title">
                <span>{{ routes.name }}</span>
              </template>

              <template v-for="route in routes.children">
                <template v-if="route.children">
                  <el-submenu :index="route.path" :key="route.path">
                    <template slot="title">{{ route.name }}</template>
                    <el-menu-item v-for="child in route.children" :index="child.path" :key="child.path">
                      {{ child.name }}
                    </el-menu-item>
                  </el-submenu>
                </template>

                <template v-if="!route.children">
                  <el-menu-item :index="route.path" :key="route.path"> {{ route.name }} </el-menu-item>
                </template>
              </template>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <app-main />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {
  AppMain
} from './components'
// import { mapGetters } from 'vuex'
export default {
  name: 'MainLayout',
  components: {
    AppMain
  },
  data () {
    return {
      isCollapse: false,
      activeIndex: '1',
      sideMenu: []
    }
  },
  computed: {
    // 暂不用
    asideWidth () {
      return this.isCollapse ? '64px' : '200px'
    }
  },
  created () {
    this.getApiRoutes()
  },
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
    },
    getApiRoutes () {
      this.$store.dispatch('api/getApiRoutes', '').then(res => {
          this.sideMenu = res
        }).catch(error => {
          this.$message(error || '加载api接口失败')
        })
    }
  }
}
</script>

<style scoped>
  .el-header {
    background-color: #fff;
    line-height: 60px;
    box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
  }

  .header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-decoration: none;
    color: #409eff;
  }

  .header-import {
    float: left;
  }

  .header-workspace {
    opacity: .5;
  }

  .header-workspace a.active:after {
    content: "";
    display: inline-block;
    position: absolute;
    bottom: 0;
    left: calc(50% - 15px);
    width: 30px;
    height: 2px;
    background: #409eff;

  }

  .header-workspace a {
    cursor: pointer;
    font-weight: 60px;
  }

  .header-right {
    float: right;
  }

  .el-aside {
    background-color: #fff;
  }

  .side-menu {
    overflow-y: auto;
    height: calc(100% - 60px);
    border-right: none;
  }

  .side-menu--collapse {
    height: 90% !important;
  }

  .el-main {
    background-color: #f5f5f5e5;
    overflow: hidden;
    height: calc(100% - 60px);
  }

  ::-webkit-scrollbar-thumb {
    background-color: #aaa;
    border-radius: 6px;
  }

  ::-webkit-scrollbar {
    width: 6px;
    height: 6px;
    background-color: #ccc;
  }
</style>
