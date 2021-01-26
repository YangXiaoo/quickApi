package quickcore.common.constants;

/**
 * 服务器请求接口URL
 * @author yangxiao
 */
public interface SERVICE {
    // 查询
    String GET_METHOD_DATA_BY_PROJECT_NAME = "/api/methodData/getMethodDataByProjectName";              // 根据项目名称获得项目所有接口方法信息
    String GET_METHOD_API_DATA = "/api/methodApiData/getMethodApiData";                                 // 获得方法接口文档数据
    String GET_USER_METHOD_API_DATA = "/api/methodApiData/getUserMethodApiData";
    String GET_USER_METHOD_DATA_LIST = "/api/methodData/getUserMethodDataList";
    String GET_USER_PROJECT_PAGE_DATA = "/api/pageData/getUserProjectMethodPageData";
    String GET_PROJECT_DEVELOPERS = "/api/project/getProjectDevelopers";
    String GET_PROJECT_FINISHED_METHOD_DATA_COUNT = "/api/methodData/getProjectFinishedMethodDataCount";
    String GET_PROJECT_FINISHED_METHOD_DATA_MAP = "/api/methodData/getProjectFinishedMethodDataMap";
    String GET_PROJECT_METHOD_API_DATA_HISTORY = "/api/methodApiData/getProjectMethodApiDataHistory";

    // 保存方法
    String SAVE_METHOD_DATA = "/api/methodData/saveMethodData";                                         // 保存接口方法信息
    String SAVE_USER_METHOD_DATA = "/api/methodData/saveUserMethodData";                                // 保存接口方法信息
    String SAVE_METHOD_API_DATA = "/api/methodApiData/saveMethodApiData";                               // 保存接口文档
    String SAVE_USER_METHOD_API_DATA = "/api/methodApiData/saveUserMethodApiData";                      // 保存接口文档
    String SAVE_PROJECT_INFO = "/api/project/saveProjectInfo";                                          // 保存项目信息
    String SAVE_USER_PROJECT_PAGE_DATA = "/api/pageData/saveUserProjectMethodPageData";
    String SAVE_USER_PROJECT_INFO = "/api/project/saveUserProjectInfo";

    // 删除方法
    String DELETE_METHOD_DATA_LIST = "/api/methodData/deleteMethodDataList";                            // 删除接口方法信息
    String DELETE_USER_METHOD_DATA = "/api/methodData/deleteUserMethodData";                            // 删除接口方法信息
    String DELETE_METHOD_API_DATA = "/api/methodApiData/deleteUserMethodApiData";                       // 删除接口文档

    // 更新
    String UPDATE_METHOD_DATA = "/api/methodData/updateMethodData";                                     // 更新接口方法信息
    String UPDATE_USER_METHOD_DATA = "/api/methodData/updateUserMethodData";                            // 更新接口方法信息

    // 检查服务器连接
    String CHECK_SERVER_STATUS = "/api/check/checkServerStatus";
}
