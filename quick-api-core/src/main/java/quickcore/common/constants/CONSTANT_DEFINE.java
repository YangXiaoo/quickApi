package quickcore.common.constants;

/**
 * 常量
 * @author yangxiao
 */
public interface CONSTANT_DEFINE {
    String UNDEFINE_API_URL = "API未设置请求路径";
    String UNDEFINE_REQUEST_TYPE = "未知的请求类型";
    String COMMON_USE_REQUEST_TYPE = "通用";
    String UNDEFINE_GROUP = "未知分组";
    String UNDEFINE_API_NAME = "未知的api名";

    // 请求工具字典常量
    String KEY_METHOD_NAME = "methodName";
    String KEY_CLASS_NAME = "className";
    String KEY_INTERFACE_NAME_LIST = "interfaceNameList";
    String KEY_CLASS_URL = "classURL";
    String KEY_METHOD_URL = "methodURL";
    String KEY_REQUEST_TYPE = "requestType";

    // 请求类型
    String REQUEST_TYPE_POST = "POST";
    String REQUEST_TYPE_GET = "GET";

    // 向服务端发送请求间隔 毫秒
    long REPORT_GAP = 30 * 1000;

    // 通用常量
    String IS_DELETE = "01";        // 删除
    String NOT_DELETE = "00";       // 未删除

    String IS_DOWNLOAD = "01";      // 是下载方法
    String NOT_DOWNLOAD = "00";     // 不是下载方法
}
