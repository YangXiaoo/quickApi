<template>
  <div class="app-wrapper">
    <navbar />
    <div class="qa-main-container">
      <sidebar />
      <div class="qa-main-content-container">
        <tags-view />
        <app-main />
      </div>
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain, TagsView } from './components'
// import ResizeMixin from './mixin/ResizeHandler'

export default {
  name: 'MainLayout',
  components: {
    Navbar,
    Sidebar,
    AppMain,
    TagsView
  },
  // mixins: [ResizeMixin],
  data() {
    return {
      dynamicTags: ['标签一', '标签二', '标签三']
    }
  },
  computed: {
  },
  methods: {
    handleClose(tag) {
      this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
    },
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/mixin.scss";
  @import "~@/styles/variables.scss";

  .app-wrapper {
    width: 100%;
    height: 100%;
    margin:0px 0px -10px 0px;
    background-color: #ffff;
    display: flex;
    flex-flow: column nowrap;
  }

  .qa-main-container {
    display: flex;
    flex-flow: row nowrap;
    height: calc(100% - #{$navbarHeight});
  }
  .qa-main-content-container {
    display: flex;
    flex-flow: column nowrap;
    width: calc(100% - #{$sideBarWidth} - #{$sideContentWidth});

    box-sizing: border-box;
  }
</style>
