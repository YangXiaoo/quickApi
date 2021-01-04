package quickcore.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import quickcore.common.constants.SERVICE;
import quickcore.common.tools.JsonModel;
import quickcore.exception.BusinessException;
import quickcore.web.logic.QuickApiLogic;

import java.util.Map;

/**
 * 前端请求服务器数据转发
 * @author yangxiao
 */
@RestController
@RequestMapping("/service")
public class SendRequestService {
    @Autowired
    private QuickApiLogic apiLogic;

    @Value("${quickApi.basePackages:}")
    private String basePackages;
    @Value("${quickApi.projectName:quickApi接口}")
    private String projectName;
    @Value("${quickApi.description:}")
    private String description;
    @Value("${quickApi.enabled:true}")
    private boolean enabled;
    @Value("${quickApi.serviceNames:}")
    private String serviceNames;
    @Value("${quickApi.localServiceName:}")
    private String localServiceName;
    @Value("${quickApi.hostServiceName:}")
    private String hostServiceName;
    @Value("${quickApi.version:}")
    private String version;
    @Value("${quickApi.author:}")
    private String author;

    /**
     * 获得方法接口信息
     * @param map 查询条件
     *            path 接口方法在项目中的路由
     *            projectName 方法名
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 22:16
     */
    @PostMapping(value = "getMethodApiData")
    public JsonModel getMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = hostServiceName + SERVICE.GET_METHOD_API_DATA;
            jsonModel = apiLogic.callService(url, map);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 获得项目信息
     * @param map 查询条件
     *            projectName 方法名
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 22:18
     */
    @PostMapping(value = "getMethodDataByProjectName")
    public JsonModel getMethodDataByProjectName(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = hostServiceName + SERVICE.GET_METHOD_DATA_BY_PROJECT_NAME;
            jsonModel = apiLogic.callService(url, map);
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }

    /**
     * 保存接口方法信息
     * @param map 条件
     *            methodModel 接口方法信息
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2020/12/27 22:18
     */
    @PostMapping(value = "saveMethodData")
    public JsonModel saveMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = hostServiceName + SERVICE.SAVE_METHOD_DATA;
            jsonModel = apiLogic.callService(url, map);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }


    /**
     * 保存接口文档
     * @param map 请求参数
     *            path 接口路由
     *            data 保存的数据
     * @return void
     * @author yangxiao
     * @date 2021/1/3 22:33
     */
    @PostMapping(value = "saveMethodApiData")
    public JsonModel saveMethodApiData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = hostServiceName + SERVICE.SAVE_METHOD_API_DATA;
            jsonModel = apiLogic.callService(url, map);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 更新接口方法信息
     * @param map 请求参数
     * @return void
     * @author yangxiao
     * @date 2021/1/4 21:11
     */
    @PostMapping(value = "updateMethodData")
    public JsonModel updateMethodData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = hostServiceName + SERVICE.UPDATE_METHOD_DATA;
            jsonModel = apiLogic.callService(url, map);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 检查服务器是否连接
     * @param map 无参数（构造）
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/4 20:52
     */
    @PostMapping(value = "checkServerStatus")
    public JsonModel checkServerStatus(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = hostServiceName + SERVICE.CHECK_SERVER_STATUS;
            jsonModel = apiLogic.callService(url, map);
        } catch (Exception e) {
            jsonModel.error("服务器连接失败");
        }

        return jsonModel;
    }

    /**
     * 检查服务器是否连接
     * @param map 无参数（构造）
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/4 20:52
     */
    @PostMapping(value = "saveProjectInfo")
    public JsonModel saveProjectInfo(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String url = hostServiceName + SERVICE.SAVE_PROJECT_INFO;
            jsonModel = apiLogic.callService(url, map);
        } catch (Exception e) {
            jsonModel.error("服务器连接失败");
        }

        return jsonModel;
    }
}
