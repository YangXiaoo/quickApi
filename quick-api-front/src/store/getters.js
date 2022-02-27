const getters = {
  constantRoutes: state => state.app.constantRoutes,
  isSettingLocalFlag: state => state.app.isSettingLocalFlag,

  // 用户相关
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  username: state => state.user.username,
  isLogin: state => state.user.isLogin,
  loginName: state => state.user.loginName,

  // 本地项目
  localProjectRoutes: state => state.localProject.localProjectRoutes,
  localMethodDataList: state => state.localProject.methodDataList,
  localProjectGroupList: state => state.localProject.localProjectGroupList,
  localPageData: state => state.localProject.localPageData,
  localServiceName: state => state.localProject.localServiceName,
  localProjectName: state => state.localProject.localProjectName,
  author: state => state.localProject.author, // 使用者
  description: state => state.localProject.description,
  isLocalProject: state => state.localProject.isLocalProject,

  // 服务端搜索出的项目
  projectMethodDataList: state => state.projectMethodData.projectMethodDataList,
  projectRoutes: state => state.projectMethodData.projectRoutes,

  serviceProjectAddress: state => state.serviceProject.serviceProjectAddress,
  serviceProjectTokenSetting: state => state.serviceProject.serviceProjectTokenSetting,

  // 用户接口相关数据
  userGroupList: state => state.userMethodData.userGroupList,
  userMethodDataList: state => state.userMethodData.userMethodDataList,
  userRoutes: state => state.userMethodData.userRoutes,

  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,

  wsData: state => state.websocket.wsData,
  wsConnectStatus: state => state.websocket.wsConnectStatus,
  wsPort: state => state.websocket.wsPort,
  wsPath: state => state.websocket.wsPath,
  wsHost: state => state.websocket.wsHost,

  // presets
  userPresets: state => state.presets.userPresets
}
export default getters
