package quickcore.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickcore.common.constants.CONSTANT_DEFINE;
import quickcore.models.MethodModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelUtil {
    private static final Logger log = LoggerFactory.getLogger(ModelUtil.class);

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
        methodModel.setDownload((String)map.get("download"));
        methodModel.setToken((String)map.get("token"));
        methodModel.setDeleteFlag((String)map.get("deleteFlag"));

        return methodModel;
    }

    /**
     * 获得方法数据的映射
     * @param methodModelList 方法信息
     * @return {url: MethodModel}
     * @author yangxiao
     * @date 2020/12/27 17:52
     */
    public static Map<String, MethodModel> getMethodUrlMapInfo(List<MethodModel> methodModelList) {
        Map<String, MethodModel> mapInfo = new HashMap<>();
        for (MethodModel methodModel : methodModelList) {
            if (!mapInfo.containsKey(methodModel.getUrl())) {
                mapInfo.put(methodModel.getUrl(), methodModel);
            } else {
                log.warn("[QuickApi] Skip duplicate method: " + methodModel.getUrl());
            }
        }

        return mapInfo;
    }

    /**
     * 将远程数据同步到本地
     * @param localMapInfo 本地api信息
     * @param serverMapInfo 服务端api信息
     * @return
     * @author yangxiao
     * @date 2020/12/27 22:29
     */
    public static void syncServerData2LocalData(Map<String, MethodModel> serverMapInfo, Map<String, MethodModel> localMapInfo) {
        for (Map.Entry<String, MethodModel> it : serverMapInfo.entrySet()) {
            if (localMapInfo.containsKey(it.getKey())) {
                MethodModel localMethodModel = localMapInfo.get(it.getKey());
                MethodModel remoteMethodModel = it.getValue();
                if (!localMethodModel.equalsValue(remoteMethodModel)) {
                    if (!StringUtils.equals(remoteMethodModel.getDeleteFlag(), CONSTANT_DEFINE.IS_DELETE)) {
                        localMethodModel.setName(remoteMethodModel.getName());
                        localMethodModel.setGroup(remoteMethodModel.getGroup());
                        localMethodModel.setContentType(remoteMethodModel.getContentType());
                        localMethodModel.setRequestType(remoteMethodModel.getRequestType());
                        localMethodModel.setMethodName(remoteMethodModel.getMethodName());
                        localMethodModel.setDescription(remoteMethodModel.getDescription());
                    }
                }
            }
        }
    }


    /**
     * 将对象转换为MethodModel
     * @param data Object
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2021/1/8 21:52
     */
    public static List<MethodModel> convertMethodModel(Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> methodModelEntityList = objectMapper.convertValue(data, List.class);
        List<MethodModel> methodModelList = new ArrayList<>();
        for (Map<String, Object> methodModelEntity : methodModelEntityList) {
            methodModelList.add(ModelUtil.convert2MethodModel(methodModelEntity));
        }

        return methodModelList;
    }
}
