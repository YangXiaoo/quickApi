package quickcore.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import quickcore.annotations.QuickApi;
import quickcore.common.constants.JSON_MODEL_CODE;
import quickcore.common.constants.SERVICE;
import quickcore.common.tools.JsonModel;
import quickcore.common.tools.RestTool;
import quickcore.core.scanner.ApiScanner;
import quickcore.core.utils.StringUtils;
import quickcore.exception.BusinessException;
import quickcore.models.MethodModel;
import quickcore.web.logic.QuickApiLogic;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;

/**
 * 接口处理
 * @author yangxiao
 */
@RestController
@RequestMapping("/quickApi")
public class QuickApiService {
    private static final Logger logger = LoggerFactory.getLogger(QuickApiService.class);

    @Autowired
    private WebApplicationContext applicationContext;

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
    @Value("${quickApi.version:}")
    private String version;
    @Value("${quickApi.author:}")
    private String author;

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
        return apiLogic.callApi(path, contentType, headerJson, queryData, type);
    }

    /**
     * 根据项目名获取该项目的所有接口信息
     * @param map 请求参数
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:16
     */
    @RequestMapping(value = "pullServiceData", method = RequestMethod.POST)
    public JsonModel pullServiceData(@RequestBody Map<String, Object> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            if (StringUtils.isBlank(projectName)) {
                throw new BusinessException("项目名称为空");
            }

            String url = serviceNames + SERVICE.PULL_DATA;
            jsonModel.success(JSON_MODEL_CODE.SUCCESS, apiLogic.callService(url, map));
        } catch (BusinessException be) {
            jsonModel.error(be.getLocalizedMessage());
        } catch (Exception e) {
            jsonModel.error("未知错误!");
        }

        return jsonModel;
    }
    /**
     * 获得接口所有信息
     * @param serverName 服务器名
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author yangxiao
     * @date 2020/11/28 17:59
     */
    @GetMapping("/api")
    public JsonModel loadQApi(String serverName) {
        JsonModel jsonModel = new JsonModel();

        // 不是从当前服务器获取api信息
        if (!StringUtils.isEmpty(serverName)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                jsonModel = restTemplate.getForObject(serverName + "/api", JsonModel.class);
            } catch (BusinessException be) {
                jsonModel.error(be.getMessage());
            } catch (Exception e) {
                jsonModel.error(e.getLocalizedMessage());
            }
        } else {
            String curBasePackages = "";
            Map<String, Object> apiMapInfo = new HashMap<>();
            try {
                // 从配置文件中获取信息
                if (!StringUtils.isEmpty(basePackages)) {
                    curBasePackages = basePackages;
                    apiMapInfo.put("projectName", projectName);
                    apiMapInfo.put("description", description);
                    apiMapInfo.put("enabled", enabled ? "Yes" : "No");
                    apiMapInfo.put("serviceNames", serviceNames);
                    apiMapInfo.put("localServiceName", localServiceName);
                    apiMapInfo.put("version", version);
                    apiMapInfo.put("author", author);
                    if (!enabled) {
                        throw new BusinessException("接口已关闭");
                    }
                } else {
                    // 从QuickApi注解中获取配置信息
                    Map<String, Object> beans = applicationContext.getBeansWithAnnotation(QuickApi.class);
                    boolean returnFlag = false;                                                                 // 是否获得配置信息标志位
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
                            apiMapInfo.put("description", quickApiAnnotation.description());
                            this.projectName = quickApiAnnotation.projectName();
                            apiMapInfo.put("enabled", quickApiAnnotation.enabled() ? "Yes" : "No");
                            apiMapInfo.put("serviceNames", quickApiAnnotation.serverNames());
                            apiMapInfo.put("localServiceName", quickApiAnnotation.localServiceName());
                            apiMapInfo.put("version", quickApiAnnotation.version());
                            apiMapInfo.put("author", quickApiAnnotation.author());
                            this.author = quickApiAnnotation.author();
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

                System.out.println("curBasePackages: " + curBasePackages);
                List<MethodModel>  methodModelList = ApiScanner.scanApi(curBasePackages.split(","), applicationContext);
                System.out.println(projectName);
                List<MethodModel> preMethodModelList;

                if (StringUtils.equals(this.checkServerStatus().getCode(), JSON_MODEL_CODE.SUCCESS)) {
                    // 比较本地与服务器的接口信息看是否需要更新接口信息
                    Map<String, Object> pullParam = new HashMap<>();
                    pullParam.put("projectName", projectName);
                    JsonModel serviceApiData = this.pullServiceData(pullParam);

                    if (serviceApiData != null && StringUtils.equals(serviceApiData.getCode(), JSON_MODEL_CODE.SUCCESS) ) {
                        preMethodModelList = (List<MethodModel>) serviceApiData.getData();

                        Map<String, MethodModel> localMapInfo = this.getMethodMapInfo(methodModelList);
                        Map<String, MethodModel> serverMapInfo = this.getMethodMapInfo(preMethodModelList);

                        List<MethodModel> deleteMethodList = this.compareApiInfo2Delete(localMapInfo, serverMapInfo);
                        this.deleteMethodInfoList(deleteMethodList);                    // 删除失效的方法

                        List<MethodModel> uploadMethodList = this.compareApiInfo2Upload(localMapInfo, serverMapInfo);
                        this.pushLocalData(uploadMethodList);

                        this.syncLocalData(localMapInfo, serverMapInfo);    // 同步数据
                    }
                } else {
                    logger.warn("远程服务器未连接成功, 只支持本地测试!");
                }

                this.cacheMethodInfo(methodModelList);         // 缓存本地
                apiMapInfo.put("apiInfo", methodModelList);
                jsonModel.success("获取接口信息成功", apiMapInfo);
            } catch (BusinessException be) {
                logger.error("错误", be);
                jsonModel.error(be.getMessage());
            } catch (Exception e) {
                logger.error("未知错误", e);
                jsonModel.error(e.getLocalizedMessage());
            }
        }

        return jsonModel;
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
        String url = serviceNames + SERVICE.SAVE_METHOD_DATA;
        Map<String, Object> map = new HashMap<>();
        map.put("projectName", projectName);
        map.put("data", methodModelList);
        apiLogic.callService(url, map);     // TODO 失败时打印错误
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
        // TODO 检查是否修改本地数据
        for (Map.Entry<String, MethodModel> it : serverMapInfo.entrySet()) {
            if (localMapInfo.containsKey(it.getKey())) {
                MethodModel localMethodModel = localMapInfo.get(it.getKey());
                MethodModel remoteMethodModel = it.getValue();
                if (!localMethodModel.equalsValue(remoteMethodModel)) {
                    if (!remoteMethodModel.isDelete()) {
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
     * 删除一组接口
     * @param data 待删除的接口信息
     * @return void
     * @author yangxiao
     * @date 2021/1/3 21:48
     */
    public void deleteMethodInfoList(List<MethodModel> data) {
        String url = serviceNames + SERVICE.DELETE_METHOD_INFO_LIST;
        Map<String, Object> map = new HashMap<>();
        map.put("projectName", projectName);
        map.put("data", data);
        apiLogic.callService(url, map);
    }

    /**
     * 检查服务器连接
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 21:25
     */
    public JsonModel checkServerStatus() {
        String url = serviceNames + SERVICE.CHECK_SERVER_STATUS;
        Map<String, Object> map = new HashMap<>();  // 构造一个参数
        JsonModel jsonModel = new JsonModel();
        try {
            jsonModel = apiLogic.callService(url, map);
        } catch (Exception e) {
            jsonModel.error("服务器连接失败");
        }

        return jsonModel;
    }
}
