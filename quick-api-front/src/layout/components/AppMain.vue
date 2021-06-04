<template>
  <div class="qa-main-app">
    <transition name="fade-transform" mode="out-in">
      <!-- <keep-alive :include="cachedViews"> -->
      <keep-alive :exclude="['_none__']">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.path
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/variables.scss";
.qa-main-app {
  min-height: calc(100% - #{$navbarHeight});
  // width: 100%;
  overflow: auto;
  background-color: #f7f7f7;
}
</style>
