package quickcore.common.constants;

public interface SERVICE {
    // 根据项目名获取该项目的所有接口信息
    String PULL_DATA = "/api/getApiDataByProjectName";

    // 检查服务器连接
    String CHECK_SERVER_STATUS = "/api/checkServerStatus";

    // 删除方法
    String DELETE_METHOD_INFO_LIST = "/api/deleteMethodInfoList";

    // 保存方法
    String SAVE_METHOD_DATA = "/api/saveMethodData";                // 保存接口方法信息
}
