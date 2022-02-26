package quickcore.core.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import quickcore.annotations.QuickApi;
import quickcore.common.constants.CONSTANT_DEFINE;
import quickcore.common.constants.MODEL_CODE;
import quickcore.common.constants.SERVICE;
import quickcore.common.tools.IPTool;
import quickcore.common.tools.JsonModel;
import quickcore.common.utils.ModelUtil;
import quickcore.common.utils.RequestUtil;
import quickcore.common.utils.StringUtils;
import quickcore.core.scanner.ApiScanner;
import quickcore.core.server.ServerInteract;
import quickcore.exception.BusinessException;
import quickcore.models.MethodModel;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuickApiLogic {
    private static final Logger log = LoggerFactory.getLogger(QuickApiLogic.class);

    /**
     * 扫描的包地址，多个包使用英文逗号分割
     */
    @Value("${quickApi.basePackages:}")
    private String basePackages;
    /**
     * 项目名称
     */
    @Value("${quickApi.projectName:quickApi接口}")
    private String projectName;
    /**
     * 项目简介
     */
    @Value("${quickApi.description:}")
    private String description;
    /**
     * 引入quickApi后默认使用quickApi功能
     */
    @Value("${quickApi.enabled:true}")
    private boolean enabled;
    /**
     * quickApi与服务器数据交互地址
     */
    @Value("${quickApi.serviceNames:}")
    private String serviceNames;
    /**
     * 暂无用处
     */
    @Value("${quickApi.localServiceName:}")
    private String localServiceName;
    /**
     * 查看文档调试时使用的地址，一般为测试环境部署IP，或者本地IP
     */
    @Value("${quickApi.hostServiceName:}")
    private String hostServiceName;
    @Value("${quickApi.version:1.0.0}")
    private String version;
    @Value("${quickApi.author:}")
    private String author;
    @Value("${server.port:8080}")
    private String port;
    @Value("${quickApi.reportSeconds:30}")
    private long reportSeconds;

    @Autowired
    ServerInteract serverInteract;

    private Map<String, MethodModel> localMapInfo = new HashMap<>();
    private Map<String, MethodModel> serverMapInfo = new HashMap<>();
    private static boolean SEND_SYNC_DATA_FLAG = false;
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

    /**
     * 获得接口所有信息
     * <p>
     *     推荐使用配置方法
     * </p>
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author yangxiao
     * @date 2020/11/28 17:59
     */
    public JsonModel getLocalApiMethod(WebApplicationContext applicationContext) {
        JsonModel jsonModel = new JsonModel();
        String curBasePackages = "";
        Map<String, Object> apiMapInfo = new HashMap<>();
        try {
            // 从配置文件中获取信息
            if (StringUtils.isNotEmpty(basePackages)) {
                curBasePackages = basePackages;
                apiMapInfo.put("projectName", projectName);
                apiMapInfo.put("description", description);
                apiMapInfo.put("enabled", enabled ? "Yes" : "No");
                apiMapInfo.put("serviceNames", serviceNames);
                apiMapInfo.put("localServiceName", localServiceName);
                apiMapInfo.put("version", version);
                apiMapInfo.put("author", author);
                apiMapInfo.put("hostServiceName", hostServiceName);
                apiMapInfo.put("basePackages", basePackages);
                log.info("curBasePackages: " + curBasePackages);
                if (!enabled) {
                    throw new BusinessException("接口已关闭");
                }
            } else {
                // 从QuickApi注解中获取配置信息
                Map<String, Object> beans = applicationContext.getBeansWithAnnotation(QuickApi.class);
                boolean returnFlag = false;
                if (!beans.isEmpty()) {
                    for (Map.Entry<String, Object> beanEntry : beans.entrySet()) {
                        Object obj = beanEntry.getValue();
                        Class<?> bootClass = obj.getClass();
                        QuickApi quickApiAnnotation = bootClass.getAnnotation(QuickApi.class);
                        curBasePackages = quickApiAnnotation.basePackages();
                        if (StringUtils.isEmpty(curBasePackages)) {
                            returnFlag = true;
                            break;
                        }

                        apiMapInfo.put("projectName", quickApiAnnotation.projectName());
                        apiMapInfo.put("basePackages", quickApiAnnotation.basePackages());
                        apiMapInfo.put("description", quickApiAnnotation.description());
                        this.projectName = quickApiAnnotation.projectName();
                        apiMapInfo.put("enabled", quickApiAnnotation.enabled() ? "Yes" : "No");
                        apiMapInfo.put("serviceNames", quickApiAnnotation.serverNames());
                        apiMapInfo.put("localServiceName", quickApiAnnotation.localServiceName());
                        apiMapInfo.put("version", quickApiAnnotation.version());
                        apiMapInfo.put("author", quickApiAnnotation.author());
                        apiMapInfo.put("hostServiceName", quickApiAnnotation.hostServiceName());
                        this.author = quickApiAnnotation.author();
                        this.hostServiceName = quickApiAnnotation.hostServiceName();
                        if (!quickApiAnnotation.enabled()) {
                            throw new BusinessException("接口已关闭");
                        }
                        returnFlag = true;
                        break;
                    }
                }

                if (!returnFlag) {
                    throw new BusinessException("接口已关闭");
                }
            }

            log.info("[QuickApi] Scanning package: " + curBasePackages);
            //List<MethodModel> methodModelList = ApiScanner.scanApi(curBasePackages.split(","), applicationContext);
            List<MethodModel> methodModelList = ApiScanner.getPackagesRequestMethodModelList(curBasePackages.split(","));
            List<MethodModel> preMethodModelList;

            if (serverInteract.checkServerConnected()) {
                if (!SEND_SYNC_DATA_FLAG) {
                    SEND_SYNC_DATA_FLAG = true;
                    new Thread(reportCurrentServiceStatus()).start();
                }
                serverInteract.saveUserProjectInfo((String)apiMapInfo.get("author"), projectName);
                serverInteract.saveLocalProjectInfo(apiMapInfo);
                // 比较本地与服务器的接口信息看是否需要更新接口信息
                JsonModel serverApiData = serverInteract.pullServerData(projectName);
                if (serverApiData != null && StringUtils.equals(serverApiData.getCode(), MODEL_CODE.SUCCESS) ) {
                    preMethodModelList = (List<MethodModel>) serverApiData.getData();

                    this.localMapInfo = ModelUtil.getMethodUrlMapInfo(methodModelList);
                    this.serverMapInfo = ModelUtil.getMethodUrlMapInfo(preMethodModelList);

                    // 不自动删除本地不存在的接口
                    //List<MethodModel> deleteMethodList = this.compareApiInfo2Delete(localMapInfo, serverMapInfo);
                    //this.deleteMethodDataList(deleteMethodList);                    // 删除失效的方法

                    //List<MethodModel> uploadMethodList = this.compareApiInfo2Upload(this.localMapInfo, this.serverMapInfo);
                    //this.pushLocalData(uploadMethodList);

                    ModelUtil.syncServerData2LocalData(this.serverMapInfo, this.localMapInfo);
                } else {
                    // 上传本地接口方法数据
                    //this.pushLocalData(methodModelList);
                }
            } else {
                log.warn("远程服务器未连接成功, 只支持本地测试!");
            }

            this.cacheMethodInfo(methodModelList);         // 缓存本地
            apiMapInfo.put("methodDataList", methodModelList);
            jsonModel.success("获取接口信息成功", apiMapInfo);
        } catch (BusinessException be) {
            log.error("错误", be);
            jsonModel.error(be.getMessage());
        } catch (Exception e) {
            log.error("未知错误", e);
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
    /**
     * 报告测试端服务状态
     * @param
     * @return java.lang.Runnable
     * @author yangxiao
     * @date 2021/1/27 20:40
     */
    public Runnable reportCurrentServiceStatus() {
        return new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(reportSeconds * 1000);
                        log.info("send localhost status");
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
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            if (request != null) {
                request.getSession().setAttribute(projectName, data);
            } else {
                log.warn("[QuickApi] ServletRequestAttributes.request is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
