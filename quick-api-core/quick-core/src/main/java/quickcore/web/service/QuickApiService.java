package quickcore.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import quickcore.common.constants.CONSTANT_DEFINE;
import quickcore.common.constants.SERVICE;
import quickcore.common.tools.IPTool;
import quickcore.common.tools.JsonModel;
import quickcore.common.utils.ModelUtil;
import quickcore.core.utils.StringUtils;
import quickcore.exception.BusinessException;
import quickcore.models.MethodModel;
import quickcore.web.dao.entity.ProjectInfo;
import quickcore.common.utils.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;

/**
 * 接口处理
 * <p>
 *     放入一个类中
 * </p>
 * @author yangxiao
 */
@RestController
@RequestMapping("/quickApi")
public class QuickApiService {
    private static final Logger logger = LoggerFactory.getLogger(QuickApiService.class);

    @Autowired
    private WebApplicationContext applicationContext;

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

    @Value("${server.port:8080}")
    private String port;

    private Map<String, MethodModel> localMapInfo = new HashMap<>();
    private Map<String, MethodModel> serverMapInfo = new HashMap<>();
    private static boolean SEND_FLAG = false;
    /**
     * 前端请求本地接口
     * @param path 请求路径
     * @param contentType 请求内容类型
     * @param headerJson 请求头
     * @param queryData 请求数据
     * @param type 类型
     * @return java.lang.Object
     * @author yangxiao
     * @date 2020/11/27 18:01
     */
    @GetMapping("/callApi")
    public Object callApi(String path, String contentType, String headerJson, String queryData, String type) {
        return RequestUtil.callApi(path, contentType, headerJson, queryData, type);
    }

    ///**
    // * 获得接口所有信息
    // * <p>
    // *     推荐使用配置方法
    // * </p>
    // * @return java.util.Map<java.lang.String,java.lang.Object>
    // * @author yangxiao
    // * @date 2020/11/28 17:59
    // */
    //@GetMapping("/api")
    //public JsonModel loadQApi() {
    //    JsonModel jsonModel = new JsonModel();
    //    String curBasePackages = "";
    //    Map<String, Object> apiMapInfo = new HashMap<>();
    //    try {
    //        // 从配置文件中获取信息
    //        if (!StringUtils.isEmpty(basePackages)) {
    //            curBasePackages = basePackages;
    //            apiMapInfo.put("projectName", projectName);
    //            apiMapInfo.put("description", description);
    //            apiMapInfo.put("enabled", enabled ? "Yes" : "No");
    //            apiMapInfo.put("serviceNames", serviceNames);
    //            apiMapInfo.put("localServiceName", localServiceName);
    //            apiMapInfo.put("version", version);
    //            apiMapInfo.put("author", author);
    //            apiMapInfo.put("hostServiceName", hostServiceName);
    //            apiMapInfo.put("basePackages", basePackages);
    //            logger.info("curBasePackages: " + curBasePackages);
    //            if (!enabled) {
    //                throw new BusinessException("接口已关闭");
    //            }
    //        } else {
    //            // 从QuickApi注解中获取配置信息
    //            Map<String, Object> beans = applicationContext.getBeansWithAnnotation(QuickApi.class);
    //            boolean returnFlag = false;                                                                 // 是否获得配置信息标志位
    //            if (!beans.isEmpty()) {
    //                for (Map.Entry<String, Object> beanEntry : beans.entrySet()) {
    //                    Object obj = beanEntry.getValue();
    //                    Class<?> bootClass = obj.getClass();
    //                    QuickApi quickApiAnnotation = bootClass.getAnnotation(QuickApi.class);
    //                    curBasePackages = quickApiAnnotation.basePackages();
    //                    if (StringUtils.isEmpty(curBasePackages)) {
    //                        returnFlag = true;
    //                        break;
    //                    }
    //
    //                    apiMapInfo.put("projectName", quickApiAnnotation.projectName());
    //                    apiMapInfo.put("basePackages", quickApiAnnotation.basePackages());
    //                    apiMapInfo.put("description", quickApiAnnotation.description());
    //                    this.projectName = quickApiAnnotation.projectName();
    //                    apiMapInfo.put("enabled", quickApiAnnotation.enabled() ? "Yes" : "No");
    //                    apiMapInfo.put("serviceNames", quickApiAnnotation.serverNames());
    //                    apiMapInfo.put("localServiceName", quickApiAnnotation.localServiceName());
    //                    apiMapInfo.put("version", quickApiAnnotation.version());
    //                    apiMapInfo.put("author", quickApiAnnotation.author());
    //                    apiMapInfo.put("hostServiceName", quickApiAnnotation.hostServiceName());
    //                    this.author = quickApiAnnotation.author();
    //                    this.hostServiceName = quickApiAnnotation.hostServiceName();
    //                    if (!quickApiAnnotation.enabled()) {
    //                        throw new BusinessException("接口已关闭");
    //                    }
    //                    returnFlag = true;
    //                    break;
    //                }
    //            }
    //
    //            if (!returnFlag) {
    //                throw new BusinessException("接口已关闭");
    //            }
    //        }
    //
    //        System.out.println("curBasePackages: " + curBasePackages);
    //        List<MethodModel>  methodModelList = ApiScanner.scanApi(curBasePackages.split(","), applicationContext);
    //        System.out.println(projectName);
    //        List<MethodModel> preMethodModelList;
    //
    //        if (StringUtils.equals(this.checkServerStatus().getCode(), MODEL_CODE.SUCCESS)) {
    //            if (!SEND_FLAG) {
    //                SEND_FLAG = true;
    //                new Thread(reportStatus()).start();
    //            }
    //            this.saveUserProjectInfo((String)apiMapInfo.get("author"), projectName);
    //            this.saveLocalProjectInfo(apiMapInfo);
    //            // 比较本地与服务器的接口信息看是否需要更新接口信息
    //            JsonModel serviceApiData = this.pullServiceData(projectName);
    //            if (serviceApiData != null && StringUtils.equals(serviceApiData.getCode(), MODEL_CODE.SUCCESS) ) {
    //                preMethodModelList = (List<MethodModel>) serviceApiData.getData();
    //
    //                this.localMapInfo = this.getMethodMapInfo(methodModelList);
    //                this.serverMapInfo = this.getMethodMapInfo(preMethodModelList);
    //
    //                // 不自动删除本地不存在的接口
    //                //List<MethodModel> deleteMethodList = this.compareApiInfo2Delete(localMapInfo, serverMapInfo);
    //                //this.deleteMethodDataList(deleteMethodList);                    // 删除失效的方法
    //
    //                //List<MethodModel> uploadMethodList = this.compareApiInfo2Upload(this.localMapInfo, this.serverMapInfo);
    //                //this.pushLocalData(uploadMethodList);
    //
    //                this.syncLocalData(this.localMapInfo, this.serverMapInfo);    // 同步数据
    //            } else {
    //                // 上传本地接口方法数据
    //                //this.pushLocalData(methodModelList);
    //            }
    //        } else {
    //            logger.warn("远程服务器未连接成功, 只支持本地测试!");
    //        }
    //
    //        this.cacheMethodInfo(methodModelList);         // 缓存本地
    //        apiMapInfo.put("methodDataList", methodModelList);
    //        jsonModel.success("获取接口信息成功", apiMapInfo);
    //    } catch (BusinessException be) {
    //        logger.error("错误", be);
    //        jsonModel.error(be.getMessage());
    //    } catch (Exception e) {
    //        logger.error("未知错误", e);
    //        jsonModel.error(e.getLocalizedMessage());
    //    }
    //
    //    return jsonModel;
    //}

    /**
     * 报告测试端服务状态
     * @param
     * @return java.lang.Runnable
     * @author yangxiao
     * @date 2021/1/27 20:40
     */
    public Runnable reportStatus() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(CONSTANT_DEFINE.REPORT_GAP);
                        logger.info("send localhost status");
                        String url = hostServiceName + SERVICE.SEND_LOCALHOST_STATUS;
                        Map<String, Object> map = new HashMap<>();
                        map.put("ip", IPTool.getLocalIp());
                        map.put("port", port);
                        map.put("projectName", projectName);
                        RequestUtil.callService(url, map);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
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
        logger.info("url: " + url);
        RequestUtil.callService(url, data);
    }

    /**
     * 比较本地接口数据与服务端接口数据，比较接口url; 返回需要删除的接口
     * <p>
     *     本地不存在，服务端存且作者为本地开发者名称一致，则删除服务端数据
     * </p>
     * @param localMapInfo 本地api信息
     * @param serverMapInfo 服务端api信息
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2020/12/27 17:44
     */
    public List<MethodModel> compareApiInfo2Delete(Map<String, MethodModel> localMapInfo, Map<String, MethodModel> serverMapInfo) {
        List<MethodModel> deleteMethodList = new ArrayList<>();
        for (Map.Entry<String, MethodModel> it : serverMapInfo.entrySet()) {
            if (!localMapInfo.containsKey(it.getKey())) {
                deleteMethodList.add(it.getValue());
            }
        }

        return deleteMethodList;
    }

    /**
     * 比较本地和服务器的数据，将本地新增的数据同步到服务器中
     * @param localMapInfo 本地api信息
     * @param serverMapInfo 服务端api信息
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2020/12/27 22:32
     */
    public List<MethodModel> compareApiInfo2Upload(Map<String, MethodModel> localMapInfo, Map<String, MethodModel> serverMapInfo) {
        List<MethodModel> uploadMethodList = new ArrayList<>();
        for (Map.Entry<String, MethodModel> it : localMapInfo.entrySet()) {
            if (!serverMapInfo.containsKey(it.getKey())) {
                uploadMethodList.add(it.getValue());
            }
        }

        return uploadMethodList;
    }

    /**
     * 将本地新增数据上传到服务器中
     * @param methodModelList 本地数据
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2020/12/27 22:35
     */
    public void pushLocalData(List<MethodModel> methodModelList) {
        String url = hostServiceName + SERVICE.SAVE_METHOD_DATA;
        Map<String, Object> map = new HashMap<>();
        for (MethodModel methodModel : methodModelList) {
            methodModel.setProjectName(projectName);
            map.put("methodModel", methodModel);
            RequestUtil.callService(url, map);
        }
    }

    /**
     * 根据项目名获取该项目的所有接口信息
     * @param projectName 项目名
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:16
     */
    public JsonModel pullServiceData(String projectName) {
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
            jsonModel.setData(this.convertMethodModel(data));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 将对象转换为MethodModel
     * @param data Object
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2021/1/8 21:52
     */
    public List<MethodModel> convertMethodModel(Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> methodModelEntityList = objectMapper.convertValue(data, List.class);
        List<MethodModel> methodModelList = new ArrayList<>();
        for (Map<String, Object> methodModelEntity : methodModelEntityList) {
            methodModelList.add(ModelUtil.convert2MethodModel(methodModelEntity));
        }

        return methodModelList;
    }
    /**
     * 将远程数据同步到本地
     * @param localMapInfo 本地api信息
     * @param serverMapInfo 服务端api信息
     * @return
     * @author yangxiao
     * @date 2020/12/27 22:29
     */
    public void syncLocalData(Map<String, MethodModel> localMapInfo, Map<String, MethodModel> serverMapInfo) {
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
     * 获得方法数据的映射
     * @param methodModelList 方法信息
     * @return java.util.Map<java.lang.String,quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2020/12/27 17:52
     */
    public Map<String, MethodModel> getMethodMapInfo(List<MethodModel> methodModelList) {
        Map<String, MethodModel> mapInfo = new HashMap<>();
        for (MethodModel methodModel : methodModelList) {
            if (!mapInfo.containsKey(methodModel.getUrl())) {
                mapInfo.put(methodModel.getUrl(), methodModel);
            } else {
                logger.warn("[Quick-Api] skip duplicate method: " + methodModel.getUrl());
            }
        }

        return mapInfo;
    }

    /**
     * 缓存接口数据
     * <p>
     *     当服务器重启时 session会被重置， 此时也会从服务器中拉取最新数据
     * </p>
     * @param data 接口数据
     * @return void
     * @author yangxiao
     * @date 2021/1/3 22:09
     */
    public void cacheMethodInfo(List<MethodModel> data) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        request.getSession().setAttribute(projectName, data);
    }

    /**
     * 从本地缓存获取方法
     * <p>
     *     存在BUG，不是最新数据（已弃用）
     * </p>
     * @param
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2021/1/4 22:07
     */
    public List<MethodModel> reloadMethodInfoFromCache() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        return (List<MethodModel>) request.getSession().getAttribute(projectName);
    }

    /**
     * 检查服务器连接
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:25
     */
    public JsonModel checkServerStatus() {
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
     * 是否开启本地项目
     * @param map
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/6/4 20:49
     */
    @PostMapping("/getLocalConnection")
    public JsonModel getConnection(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            jsonModel.success("get connected!", true);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    @PostMapping("/getLocalMapInfo")
    public JsonModel getLocalMapInfo(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            jsonModel.success("success", this.localMapInfo);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    @PostMapping("/getServerMapInfo")
    public JsonModel getServerMapInfo(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            jsonModel.success("success", this.serverMapInfo);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 删除一组接口方法信息
     * @return void
     * @author yangxiao
     * @date 2021/1/3 21:48
     */
    @PostMapping("/deleteMethodDataListFromLocalDiff")
    public JsonModel deleteMethodDataList(@RequestBody Map<String, Object> dummy) {
        JsonModel jsonModel = new JsonModel();
        try {
            List<MethodModel> deleteDataList = this.compareApiInfo2Delete(this.localMapInfo, this.serverMapInfo);
            String url = hostServiceName + SERVICE.DELETE_METHOD_DATA_LIST;
            Map<String, Object> map = new HashMap<>();
            map.put("data", deleteDataList);
            RequestUtil.callService(url, map);

            jsonModel.success("success", true);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 获得远程服务有而本地服务没有的接口
     * @param
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/7/15 23:19
     */
    @PostMapping("/getDeleteMethodList")
    public JsonModel getDeleteMethodList(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            List<MethodModel> data = this.compareApiInfo2Delete(this.localMapInfo, this.serverMapInfo);
            jsonModel.success("success", data);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 获得远程服务有而本地服务没有的接口
     * @param
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/7/15 23:19
     */
    @PostMapping("/getUploadMethodList")
    public JsonModel getUploadMethodList(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            List<MethodModel> uploadMethodList = this.compareApiInfo2Upload(this.localMapInfo, this.serverMapInfo);
            jsonModel.success("success", uploadMethodList);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    @PostMapping("/syncProjectApiMethod")
    public JsonModel syncProjectApiMethod(@RequestBody List<MethodModel>  methodModelList) {
        JsonModel jsonModel = new JsonModel();
        try {

            List<MethodModel> deleteMethod = new ArrayList<>();
            List<MethodModel> addMethod = new ArrayList<>();
            for (MethodModel methodModel : methodModelList) {
                if (StringUtils.equals(methodModel.getDeleteFlag(), CONSTANT_DEFINE.IS_DELETE)) {
                    deleteMethod.add(methodModel);
                } else {
                    addMethod.add(methodModel);
                }
            }

            String url = hostServiceName + SERVICE.DELETE_METHOD_DATA_LIST;
            Map<String, Object> map = new HashMap<>();
            map.put("data", deleteMethod);
            RequestUtil.callService(url, map);

            this.pushLocalData(addMethod);

            jsonModel.success("success", true);
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
}