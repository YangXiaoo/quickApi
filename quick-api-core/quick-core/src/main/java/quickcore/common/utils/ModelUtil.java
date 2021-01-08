package quickcore.common.utils;

import quickcore.models.MethodModel;

import java.util.Map;

public class ModelUtil {
    /**
     * 将map转为MethodModel
     * @param map MethodModel的字典格式
     * @return quickcore.models.MethodModel
     * @author yangxiao
     * @date 2021/1/8 21:49
     */
    public static MethodModel convert2MethodModel(Map<String, Object> map) {
        MethodModel methodModel = new MethodModel();
        methodModel.setProjectName((String)map.get("projectName"));
        methodModel.setDescription((String)map.get("methodDescription"));
        methodModel.setMethodName((String)map.get("methodName"));
        methodModel.setRequestType((String)map.get("requestType"));
        methodModel.setContentType((String)map.get("contentType"));
        methodModel.setGroup((String)map.get("methodGroup"));
        methodModel.setClassName((String)map.get("className"));
        methodModel.setUrl((String)map.get("url"));
        methodModel.setName((String)map.get("name"));
        methodModel.setVersion((String)map.get("version"));
        methodModel.setAuthor((String)map.get("author"));
        methodModel.setDownload(map.get("download") == "true");
        methodModel.setToken(map.get("token") == "true");

        return methodModel;
    }
}
