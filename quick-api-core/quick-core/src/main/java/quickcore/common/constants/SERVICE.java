package quickcore.common.constants;

/**
 * 服务器请求接口URL
 * @author yangxiao
 */
public interface SERVICE {
    // 查询
    String GET_METHOD_DATA_BY_PROJECT_NAME = "/api/getMethodDataByProjectName";              // 根据项目名称获得项目所有接口方法信息
    String GET_METHOD_API_DATA = "/api/getMethodApiData";                                 // 获得方法接口文档数据
    String GET_USER_METHOD_API_DATA = "/api/getUserMethodApiData";
    String GET_USER_METHOD_DATA_LIST = "/api/getUserMethodDataList";
    String GET_USER_PROJECT_PAGE_DATA = "/api/getUserProjectMethodPageData";
    String GET_PROJECT_DEVELOPERS = "/api/getProjectDevelopers";
    String GET_PROJECT_FINISHED_METHOD_DATA_COUNT = "/api/getProjectFinishedMethodDataCount";
    String GET_PROJECT_FINISHED_METHOD_DATA_MAP = "/api/getProjectFinishedMethodDataMap";
    String GET_PROJECT_METHOD_API_DATA_HISTORY = "/api/getProjectMethodApiDataHistory";
    String GET_RUNNING_SERVICE = "/api/register/getRunningService";

    // 保存方法
    String SAVE_METHOD_DATA = "/api/saveMethodData";                                         // 保存接口方法信息
    String SAVE_USER_METHOD_DATA = "/api/saveUserMethodData";                                // 保存接口方法信息
    String SAVE_METHOD_API_DATA = "/api/saveMethodApiData";                               // 保存接口文档
    String SAVE_USER_METHOD_API_DATA = "/api/saveUserMethodApiData";                      // 保存接口文档
    String SAVE_PROJECT_INFO = "/api/saveProjectInfo";                                          // 保存项目信息
    String SAVE_USER_PROJECT_PAGE_DATA = "/api/saveUserProjectMethodPageData";
    String SAVE_USER_PROJECT_INFO = "/api/saveUserProjectInfo";

    // 删除方法
    String DELETE_METHOD_DATA_LIST = "/api/deleteMethodDataList";                            // 删除接口方法信息
    String DELETE_USER_METHOD_DATA = "/api/deleteUserMethodData";                            // 删除接口方法信息
    String DELETE_METHOD_API_DATA = "/api/deleteUserMethodApiData";                       // 删除接口文档

    // 更新
    String UPDATE_METHOD_DATA = "/api/updateMethodData";                                     // 更新接口方法信息
    String UPDATE_USER_METHOD_DATA = "/api/updateUserMethodData";                            // 更新接口方法信息

    // 服务器连接
    String CHECK_SERVER_STATUS = "/api/check/checkServerStatus";
    String SEND_LOCALHOST_STATUS = "/api/register/reportStatus";
}
