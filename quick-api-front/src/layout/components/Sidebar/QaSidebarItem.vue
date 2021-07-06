<template>
  <!-- <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body @contextmenu.prevent.native="openMenu(tag,$event)">
      <template slot="title">
        <div>
          <span>{{ item.meta.title }}</span>
        </div>
      </template>
      <sidebar-item v-for="child in item.children" :key="child.path" :is-nest="true" :item="child" :base-path="resolvePath(child.path)" class="nest-menu" />
    </el-submenu>
    <ul v-show="visible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
      <li @click="closeMenu()">取消</li>
      <li>新建</li>
      <li>编辑</li>
      <li>删除</li>
    </ul>
  </div> -->
  <div>
    <div class="sidebar-menu">
      <div class="sidebar-menu-title">
        <div class="sidebar-menu-title-left">
          "上"
        </div>
        <div class="sidebar-menu-title-name">
          <span>{{ item.meta.title }}</span>
        </div>
        <div class="sidebar-menu-title-right">
          <div class="sidebar-menu-title-right-top">
            you
          </div>
          <div class="sidebar-menu-title-right-bottom">
            ...
          </div>
        </div>
      </div>
      <div class="submenu-container">
        <div v-for="child in item.children" :key="child.path" class="sidebar-submenu">
          <div class="sidebar-submenu-request-type">
            <span class="post">post</span>
          </div>
          <div class="sidebar-submenu-title">
            {{ child.meta.title }}
          </div>
          <div class="sidebar-submenu-right">
            ...
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import path from 'path'
import { isExternal } from '@/utils/validate'
import FixiOSBug from './FixiOSBug'

export default {
  name: 'QaSidebarItem',
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
    // TODO: refactor with render function
    this.onlyOneChild = null
    return {
      visible: false,
      top: 0,
      left: 0
    }
  },
  computed: {
    ...mapGetters([
    ])
  },
  watch: {
    visible(value) {
      if (value) {
        document.body.addEventListener('click', this.closeMenu)
      } else {
        document.body.removeEventListener('click', this.closeMenu)
      }
    }
  },
  mounted() {
    this.sidebarMenuMoseOn()
  },
  methods: {
    openMenu(tag, event) {
      this.closeMenu()
      console.log('tag', tag)
      console.log('event', event)
      const menuMinWidth = 40
      console.log('openMenu()>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>')
      const offsetLeft = this.$el.getBoundingClientRect().left // container margin left
      console.log('offsetLeft', offsetLeft)
      const offsetWidth = this.$el.offsetWidth // container width
      console.log('offsetWidth', offsetWidth)
      const maxLeft = offsetWidth - menuMinWidth // left boundary
      const left = event.clientX - 45 // 20: margin right
      console.log('event.clientX ', event.clientX)

      if (left > maxLeft) {
        this.left = maxLeft
      } else {
        this.left = left
      }

      this.top = event.clientY - 45
      this.visible = true
    },
    closeMenu() {
      this.visible = false
    },
    handleScroll() {
      this.closeMenu()
    },
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // // When there is only one child router, the child router is displayed by default
      // if (showingChildren.length === 1) {
      //   return true
      // }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    },
    sidebarMenuMoseOn() {
      const menuList = document.getElementsByClassName('sidebar-menu-title')

      for (let i = 0; i < menuList.length; ++i) {
        menuList[i].onmousedown = function() {
          if (menuList[i].nextElementSibling.getAttribute('class').indexOf('menu-colse') !== -1) {
            menuList[i].nextElementSibling.setAttribute('class', 'submenu-container')
          } else {
            menuList[i].nextElementSibling.setAttribute('class', 'menu-colse')
          }
        }

        menuList[i].nextElementSibling.setAttribute('class', 'menu-colse')
      }
    },
    handleMenueClick() {

    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";
.contextmenu {
  margin: 0;
  background: #fff;
  z-index: 3000;
  position: absolute;
  list-style-type: none;
  padding: 0px 0;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 400;
  color: #333;
  box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  li {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
    &:hover {
      background: #eee;
    }
  }
}
.sidebar-menu {
  width: 100%;
  background: #ffff;

  .sidebar-menu-title {
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;

    &:hover {
      background: #d8d8d8;
    }
    .sidebar-menu-title-left {
      width: 20%;
    }
    .sidebar-menu-title-name {
      width: 60%;
    }
    .sidebar-menu-title-right {
      width: 20%;
      float: right;
    }
  }
  .menu-colse {
    display: none;
  }

  .submenu-container {
  }

  .sidebar-submenu {
    width: 100%;
    background: #ffff;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;

    &:hover {
      background: #d8d8d8;
    }

    .sidebar-submenu-request-type {
      width: 20%;
      padding: 2px 2px 2px 5px;
      .post {
        color: orange;
      }
      .get {
        color: greenyellow;
      }
    }
    .sidebar-submenu-title {
      width: 60%;
    }
    .sidebar-submenu-right {
      width: 20%;
    }
  }
}
</style>
