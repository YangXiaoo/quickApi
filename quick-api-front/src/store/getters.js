const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  routes: state => state.localProject.routes,
  apiInfo: state => state.localProject.apiInfo,
  groupList: state => state.localProject.groupList,
  routerSettingFlag: state => state.localProject.routerSettingFlag,
  pageStatus: state => state.localProject.pageStatus,
  localServiceName: state => state.localProject.localServiceName,
  localProjectName: state => state.localProject.localProjectName,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews
}
export default getters
