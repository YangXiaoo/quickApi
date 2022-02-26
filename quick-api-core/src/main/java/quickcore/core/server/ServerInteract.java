package quickcore.core.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import quickcore.common.constants.MODEL_CODE;
import quickcore.common.constants.SERVICE;
import quickcore.common.tools.JsonModel;
import quickcore.common.utils.ModelUtil;
import quickcore.common.utils.RequestUtil;
import quickcore.common.utils.StringUtils;
import quickcore.exception.BusinessException;
import quickcore.models.ProjectInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 与服务端交互逻辑
 */
@Component
public class ServerInteract {
    /**
     * 查看文档调试时使用的地址，一般为测试环境部署IP，或者本地IP
     */
    @Value("${quickApi.hostServiceName:}")
    private String hostServiceName;

    /**
     * @return true可以连接, 连接失败false
     */
    public boolean checkServerConnected() {
        JsonModel jsonModel = this.getServerStatus();

        return  jsonModel != null
                && StringUtils.equals(jsonModel.getCode(), MODEL_CODE.SUCCESS);
    }

    /**
     * 检查服务器连接
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:25
     */
    public JsonModel getServerStatus() {
        String url = hostServiceName + SERVICE.CHECK_SERVER_STATUS;
        Map<String, Object> map = new HashMap<>();  // 构造一个参数
        JsonModel jsonModel = new JsonModel();
        try {
            jsonModel = RequestUtil.callService(url, map);
        } catch (Exception e) {
            jsonModel.error("服务器连接失败");
        }

        return jsonModel;
    }


    /**
     * 根据项目名获取该项目的所有接口信息
     * @param projectName 项目名
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:16
     */
    public JsonModel pullServerData(String projectName) {
        JsonModel jsonModel = new JsonModel();
        try {
            if (StringUtils.isBlank(projectName)) {
                throw new BusinessException("项目名称为空");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("projectName", projectName);

            String url = hostServiceName + SERVICE.GET_METHOD_DATA_BY_PROJECT_NAME;
            jsonModel = RequestUtil.callService(url, map);
            Object data = jsonModel.getData();
            jsonModel.setData(ModelUtil.convertMethodModel(data));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }


    /**
     * 保存
     * <p>
     *     存在不更新，不存在则插入
     * </p>
     * @param userName 用户名
     * @param projectName 项目名
     * @return void
     * @author yangxiao
     * @date 2021/1/26 20:31
     */
    public void saveUserProjectInfo(String userName, String projectName) {
        String url = hostServiceName + SERVICE.SAVE_USER_PROJECT_INFO;
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("projectName", projectName);
        RequestUtil.callService(url, map);
    }

    /**
     * 保存项目信息
     * <p>
     *     存在更新，不存在插入
     * </p>
     * @param map projectInfo
     * @return void
     * @author yangxiao
     * @date 2021/1/4 22:28
     */
    public void saveLocalProjectInfo(Map<String, Object> map) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName((String) map.get("projectName"));
        projectInfo.setBasePackages((String) map.get("basePackages"));
        projectInfo.setDescription((String) map.get("description"));
        projectInfo.setEnable((String) map.get("enabled"));
        projectInfo.setServerNames((String) map.get("serviceNames"));
        projectInfo.setLocalServiceName((String) map.get("localServiceName"));
        projectInfo.setVersion((String) map.get("version"));
        projectInfo.setAuthor((String) map.get("author"));
        projectInfo.setHostServiceName((String) map.get("hostServiceName"));

        Map<String, Object> data = new HashMap<>();
        data.put("projectInfo", projectInfo);
        String url = hostServiceName + SERVICE.SAVE_PROJECT_INFO;
        RequestUtil.callService(url, data);
    }
}