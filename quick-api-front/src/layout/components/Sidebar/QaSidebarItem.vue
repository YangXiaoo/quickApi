<template>
  <div>
    <div class="sidebar-menu">
      <div class="sidebar-menu-title">
        <div class="sidebar-menu-title-left">
          <!-- <i class="el-icon-arrow-right" /> -->
        </div>
        <div class="sidebar-menu-title-name">
          <span>{{ item.meta.title }}</span>
        </div>
        <div v-if="editMenu" class="sidebar-menu-title-right" @click.stop="handleClickMenuExtend(item, $event)">
          <i class="el-icon-more" />
        </div>
      </div>
      <div class="submenu-item-container">
        <div v-for="child in item.children" :key="basePath + child.path" :path="resolvePath(child.path)" class="submenu-item" @click="handleClickMenuItem(child.path)">
          <div class="submenu-item-request-type">
            <span :class="hanldeRequestTypeStyle(child.meta.requestType || 'GET')">{{ child.meta.requestType || 'GET' }}</span>
          </div>
          <div class="submenu-item-title">
            {{ child.meta.title }}
          </div>
        </div>
      </div>
    </div>
    <ul v-show="submenuExtendVisible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
      <li @click="closeSubmenuExtend()">取消</li>
      <li>新建</li>
      <li>编辑</li>
      <li>删除</li>
    </ul>
    <ul v-show="menuExtendVisible" :style="{left:left+'px',top:top+'px'}" class="contextmenu">
      <li @click="closeMenuExtend()">取消</li>
      <li @click="renameMethodGroup()">重命名</li>
    </ul>

    <!-- 本地项目菜单 -->
    <el-dialog v-if="isLocal" title="修改菜单名" :visible.sync="localDialogObj.visible" width="25%">
      <el-form label-width="80px">
        <el-form-item label="所属组">
          <el-select v-model="localDialogObj.groupItem.meta.title" filterable allow-create default-first-option placeholder="请选择组别">
            <el-option v-for="obj of localProjectGroupList" :key="obj" :label="obj" :value="obj" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="localDialogObj.visible = false">取 消</el-button>
        <el-button type="warning" @click="handleSaveMethodName">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 用户菜单 -->
    <el-dialog v-if="!isLocal" title="修改菜单名" :visible.sync="userDialogObj.visible" width="25%">
      <el-form label-width="80px">
        <el-form-item label="所属组">
          <el-select v-model="userDialogObj.groupItem.meta.title" filterable allow-create default-first-option placeholder="请选择组别">
            <el-option v-for="obj of userGroupList" :key="obj" :label="obj" :value="obj" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userDialogObj.visible = false">取 消</el-button>
        <el-button type="warning" @click="handleSaveUserMethodName">确 定</el-button>
      </div>
    </el-dialog>
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
    },
    isLocal: {
      type: Boolean,
      default: false
    },
    editMenu: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      submenuExtendVisible: false,
      menuExtendVisible: false,
      top: 0,
      left: 0,
      localDialogObj: {
        visible: false,
        groupItem: {
          meta: {
            title: ''
          }
        }
      },
      userDialogObj: {
        visible: false,
        methodName: '',
        methodGroup: '',
        groupItem: {
          meta: {
            title: ''
          }
        }
      }
    }
  },
  computed: {
    ...mapGetters([
      'localProjectGroupList',
      'localProjectName',
      'author',
      'loginName',
      'userGroupList'
    ])
  },
  watch: {
    $route() {
      this.showCurrentMenuItem()
    }
  },
  mounted() {
    this.sidebarMenuMouseOn()
    this.sidebarMenuItemMouseOn()
  },
  methods: {
    handleClickSubmenuExtend(item, event) {
      this.closeSubmenuExtend()
      console.log('event', event)
      const menuMinWidth = 40
      const offsetLeft = this.$el.getBoundingClientRect().left // container margin left
      console.log('offsetLeft', offsetLeft)
      const offsetWidth = this.$el.offsetWidth // container width
      console.log('offsetWidth', offsetWidth)
      const maxLeft = offsetWidth - menuMinWidth // left boundary
      const left = event.clientX + 1000 // 20: margin right
      console.log('event.clientX ', event.clientX)

      if (left > maxLeft) {
        this.left = maxLeft
      } else {
        this.left = left
      }

      this.top = event.clientY

      this.submenuExtendVisible = true
    },
    handleClickMenuExtend(item, event) {
      if (this.isLocal) {
        this.localDialogObj.groupItem = item
      } else {
        this.userDialogObj.groupItem = item
      }
      this.closeMenuExtend()
      const menuMinWidth = 40
      const offsetLeft = this.$el.getBoundingClientRect().left // container margin left
      console.log('offsetLeft', offsetLeft)
      const offsetWidth = this.$el.offsetWidth // container width
      console.log('offsetWidth', offsetWidth)
      const maxLeft = offsetWidth - menuMinWidth // left boundary
      const left = event.clientX - 20 // 20: margin right
      console.log('event.clientX ', event.clientX)

      if (left > maxLeft) {
        this.left = maxLeft
      } else {
        this.left = left
      }

      this.top = event.clientY
      this.menuExtendVisible = true
    },
    closeSubmenuExtend() {
      this.submenuExtendVisible = false
    },
    closeMenuExtend() {
      this.menuExtendVisible = false
    },
    handleScroll() {
      this.closeSubmenuExtend()
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
    sidebarMenuMouseOn() {
      const menuList = document.getElementsByClassName('sidebar-menu-title')

      for (let i = 0; i < menuList.length; ++i) {
        menuList[i].onclick = function() {
          if (menuList[i].nextElementSibling.getAttribute('class').indexOf('menu-colse') !== -1) {
            menuList[i].nextElementSibling.setAttribute('class', 'submenu-item-container')
            menuList[i].setAttribute('class', 'current-menu sidebar-menu-title')

            // 菜单展开标识
            menuList[i].firstElementChild.innerHTML = '<i class="el-icon-arrow-down" />'
          } else {
            menuList[i].nextElementSibling.setAttribute('class', 'menu-colse')
            menuList[i].setAttribute('class', 'sidebar-menu-title')
            menuList[i].firstElementChild.innerHTML = '<i class="el-icon-arrow-right" />'
          }
        }

        menuList[i].nextElementSibling.setAttribute('class', 'menu-colse')
        menuList[i].firstElementChild.innerHTML = '<i class="el-icon-arrow-right" />'
      }
    },
    sidebarMenuItemMouseOn() {
      const menuItemList = document.getElementsByClassName('submenu-item')

      for (let i = 0; i < menuItemList.length; ++i) {
        menuItemList[i].onmousedown = function() {
          if (menuItemList[i].getAttribute('class').indexOf('current-menu-item') !== -1) {
            menuItemList[i].setAttribute('class', 'submenu-item')
          } else {
            menuItemList[i].setAttribute('class', 'submenu-item current-menu-item')
            for (let j = 0; j < menuItemList.length; ++j) {
              if (j !== i) {
                menuItemList[j].setAttribute('class', 'submenu-item')
              }
            }
          }
          // for (let j = 0; j < menuItemList.length; ++j) {
          //   menuItemList[j].setAttribute('class', 'submenu-item')
          // }
        }

        // menuItemList[i].setAttribute('class', 'submenu-item')
      }
    },
    handleClickMenuItem(path) {
      this.$router.push({
        path: this.resolvePath(path)
      })
    },
    showCurrentMenuItem() {
      const curPath = this.$route.path
      console.log('showCurrentMenuItem', curPath)

      const menuItemList = document.getElementsByClassName('submenu-item')

      this.$nextTick(() => {
        for (let i = 0; i < menuItemList.length; ++i) {
          if (menuItemList[i].getAttribute('path') === curPath) {
            menuItemList[i].setAttribute('class', 'submenu-item current-menu-item')
            // 关闭其它菜单current显示
            for (let j = 0; j < menuItemList.length; ++j) {
              if (j !== i) {
                menuItemList[j].setAttribute('class', 'submenu-item')
              }
            }

            // 如果父级菜单关闭，则打开
            const menuItemContainerElement = menuItemList[i].parentNode
            if (menuItemContainerElement) {
              if (menuItemContainerElement.getAttribute('class').indexOf('menu-colse') !== -1) {
                menuItemContainerElement.setAttribute('class', 'submenu-item-container')
              }

              if (menuItemContainerElement.previousElementSibling) {
                if (menuItemContainerElement.previousElementSibling.getAttribute('class').indexOf('current-menu') === -1) {
                  menuItemContainerElement.previousElementSibling.setAttribute('class', 'current-menu sidebar-menu-title')
                  menuItemContainerElement.previousElementSibling.firstElementChild.innerHTML = '<i class="el-icon-arrow-down" />'
                }
              }
            }
          } else {
            menuItemList[i].setAttribute('class', 'submenu-item')
          }
        }
      })
    },
    hanldeRequestTypeStyle(requestType) {
      if (requestType) {
        if (requestType === 'POST') {
          return 'post'
        } else if (requestType === 'GET') {
          return 'get'
        } else if (requestType === '通用') {
          return 'normal'
        }
      } else {
        return 'GET'
      }
    },
    handleSaveMethodName() {
      // 获得菜单，根据菜单path对菜单的title进行修改
      for (const child of this.localDialogObj.groupItem.children) {
        const data = {
          url: '/' + child.path,
          projectName: this.localProjectName,
          name: child.meta.title,
          methodGroup: this.localDialogObj.groupItem.meta.title,
          author: this.author
        }

        this.$store.dispatch('localProject/updateProjectMethodData', data).then(res => {
          // this.updateVisitedView(data.name, data.methodGroup)
        }).catch((error) => {
          this.$message({
            message: error || '更新失败',
            type: 'warning'
          })
        })
      }
      this.resetDialog()
    },
    renameMethodGroup() {
      if (this.isLocal) {
        this.localDialogObj.visible = true
        this.closeMenuExtend()
      } else {
        this.userDialogObj.visible = true
        this.closeMenuExtend()
      }
    },
    resetDialog() {
      this.localDialogObj.visible = false
      this.userDialogObj.visible = false
    },
    handleSaveUserMethodName() {
      for (const child of this.userDialogObj.groupItem.children) {
        const data = {
          userName: this.loginName, // 推荐使用邮箱
          url: child.path, // 路由
          methodName: child.meta.title, // 方法名
          methodGroup: this.userDialogObj.groupItem.meta.title
        }
        this.$store.dispatch('userMethodData/updateUserMethodData', data)

        this.resetDialog()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";
.contextmenu {
    margin: 0;
    background: #FFF;
    border: 1px solid #EBEEF5;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    z-index: 3000;
    min-width: 150px;
    position: absolute;
    list-style-type: none;
    padding: 0px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
  li {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
    &:hover {
      background: #eee;
    }
  }
}
.menu-extend {
    margin: 0;
    list-style-type: none;
    padding: 0px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
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
    height: 58px;
    align-items: center;
    cursor: pointer;
    border-top: 1px solid #d8d8d8;

    &:hover {
      background: #d8d8d8;
    }
    .sidebar-menu-title-left {
      margin-left: 10px;
      width: 10%;
    }
    .sidebar-menu-title-name {
      width: 75%;
      font-size: 16px;
      text-align: left;
    }
    .sidebar-menu-title-right {
      width: 15%;
      padding-right: 10px;
      text-align: right;
      color: #ffff;

      &:hover {
        font-size: 120%;
        color: #424242;
      }

      // .sidebar-menu-title-right-top {
      //   display: flex;
      //   height: 29px;
      //   padding-right: 5px;
      //   text-align: right;
      //   align-items: center;
      //   justify-content: center;
      //   border-bottom: 1px solid #f7f7f7;
      // }
      // .sidebar-menu-title-right-bottom {
      //   display: flex;
      //   padding-right: 5px;
      //   text-align: right;
      //   align-items: center;
      //   justify-content: center;
      //   height: 29px;
      // }
    }
  }
  .menu-colse {
    display: none;
  }

  .current-menu {
    // background: #d8d8d8;
    box-shadow: 0 1px 4px #d8d8d8;
  }

  .submenu-item-container {
    background: #ffff;
  }

  .submenu-item {
    width: 100%;
    background: #ffff;
    display: flex;
    flex-flow: row nowrap;
    justify-content: center;
    height: 50px;
    align-items: center;
    cursor: pointer;

    padding-left: 10px;

    &:hover {
      background: #eae6e6;
      padding-left: -10px;
    }

    .submenu-item-request-type {
      width: 20%;
      font-size: 14px;
      padding: 2px 2px 2px 5px;
      text-align: left;
      .post {
        color: orange;
      }
      .get {
        color: #7ecd71d1;
      }
      .narmal {
        color: #f7f7f7;
      }
    }
    .submenu-item-title {
      width: 80%;
      font-size: 14px;
    }
    // .submenu-item-right {
    //   width: 20%;
    //   padding-right: 10px;
    //   text-align: right;
    //   color: #ffff;

    //   &:hover {
    //     font-size: 120%;
    //     color: #424242;
    //   }
    // }
  }

  .current-menu-item {
    background: #eae6e6;
    padding-left: -10px;
  }
}
</style>
