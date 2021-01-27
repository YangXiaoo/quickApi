const getters = {
  constantRoutes: state => state.app.constantRoutes,

  // 用户相关
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,

  // 本地项目
  localProjectRoutes: state => state.localProject.localProjectRoutes,
  localMethodDataList: state => state.localProject.methodDataList,
  localProjectGroupList: state => state.localProject.localProjectGroupList,
  localPageData: state => state.localProject.localPageData,
  localServiceName: state => state.localProject.localServiceName,
  localProjectName: state => state.localProject.localProjectName,
  author: state => state.localProject.author, // 使用者
  description: state => state.localProject.description,

  // 服务端搜索出的项目
  projectMethodDataList: state => state.projectMethodData.projectMethodDataList,
  projectRoutes: state => state.projectMethodData.projectRoutes,

  serviceProjectAddress: state => state.serviceProject.serviceProjectAddress,

  // 用户接口相关数据
  userGroupList: state => state.userMethodData.userGroupList,
  userMethodDataList: state => state.userMethodData.userMethodDataList,
  userRoutes: state => state.userMethodData.userRoutes,

  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews
}
export default getters
