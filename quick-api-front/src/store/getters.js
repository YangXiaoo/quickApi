const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  routes: state => state.api.routes,
  apiInfo: state => state.api.apiInfo,
  groupList: state => state.api.groupList,
  routerSettingFlag: state => state.api.routerSettingFlag,
  pageStatus: state => state.api.pageStatus,
  localServiceName: state => state.api.localServiceName,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews
}
export default getters
