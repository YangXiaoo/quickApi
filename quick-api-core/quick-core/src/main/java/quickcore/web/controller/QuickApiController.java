package quickcore.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import quickcore.annotations.QuickApi;
import quickcore.common.JSON_MODEL_CODE;
import quickcore.common.JsonModel;
import quickcore.core.scanner.ApiScanner;
import quickcore.core.utils.JsonUtils;
import quickcore.core.utils.RequestUtil;
import quickcore.core.utils.StringUtils;
import quickcore.exception.BusinessException;
import quickcore.models.MethodModel;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.List;

/**
 * 接口处理
 * @author yangxiao
 */
@RestController
@RequestMapping("/quickApi")
public class QuickApiController {
    private static final Logger logger = LoggerFactory.getLogger(QuickApiController.class);

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
    @Value("${quickApi.version:}")
    private String version;
    @Value("${quickApi.author:}")
    private String author;

    /**
     * 请求接口
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
        System.out.println(path + ", " + type + ", queryData: " + queryData + ", contentType" + contentType);
        type = type.toLowerCase();
        Map<String, Object> headerMap = JsonUtils.toMap(headerJson);
        Map<String, Object> queryMap = JsonUtils.toMap(queryData);
        RestTemplate restTemplate = new RestTemplate();

        Object object = null;                                                                                           // 设置请求返回内容

        // 设置请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        Set<String> hSet = headerMap.keySet();
        for (String key : hSet) {
            requestHeaders.add(key, headerMap.get(key) == null ? "" : headerMap.get(key).toString());
        }

        // 设置参数
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        Set<String> qSet = queryMap.keySet();
        String getPramData = "";
        int i = 0;
        for (String key : qSet) {
            // post方法参数
            requestBody.add(key, queryMap.get(key) == null ? "" : queryMap.get(key).toString());

            // get方法参数
            if (i == 0) {
                getPramData = "?" + key + "=" + queryMap.get(key);
            } else {
                getPramData = getPramData + "&" + key + "=" + queryMap.get(key);
            }
            i++;
        }
        try {
            if ("application/json".equals(contentType)) {
                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(queryMap, requestHeaders);
                if (StringUtils.equals("get", type)) {
                    return "请求内容格式为application/json时只支持post、put、delete请求!";
                }
                if (StringUtils.equals("post", type)) {
                    object = restTemplate.exchange(path, HttpMethod.POST,requestEntity, Map.class);
                }
                if (StringUtils.equals("put", type)) {
                    object = restTemplate.exchange(path,HttpMethod.PUT,requestEntity, Map.class);
                }
                if (StringUtils.equals("delete", type)) {
                    object = restTemplate.exchange(path,HttpMethod.DELETE,requestEntity, Map.class);
                }

            } else {
                HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

                if (StringUtils.equals("get", type)) {
                    object = restTemplate.exchange(path + getPramData, HttpMethod.GET,null, Map.class);
                }
                if (StringUtils.equals("post", type)) {
                    object = restTemplate.exchange(path, HttpMethod.POST, requestEntity, Map.class);
                }
                if (StringUtils.equals("put", type)) {
                    object = restTemplate.exchange(path, HttpMethod.PUT, requestEntity, Map.class);
                }
                if (StringUtils.equals("delete", type)) {
                    object = restTemplate.exchange(path, HttpMethod.DELETE, requestEntity, Map.class);
                }
            }

            return object;
        } catch (RestClientException e) {
            return e.getLocalizedMessage();
        }
    }

    @RequestMapping(value = "pullServiceData", method = RequestMethod.POST)
    public JsonModel pullServiceData(@RequestBody Map<String, String> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = (String) map.get("projectName");
            if (StringUtils.isBlank(projectName)) {
                throw new BusinessException("项目名称为空");
            }


            jsonModel.success(JSON_MODEL_CODE.SUCCESS, "保存成功");
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

                // TODO 检查网络是否连接
                // 比较本地与服务器的接口信息看是否需要更新接口信息
                JsonModel apiData = null;   // this.loadQApi(serverName); // TODO 测试，未创建服务器
                if (apiData != null && StringUtils.equals(apiData.getCode(), JSON_MODEL_CODE.SUCCESS) ) {
                    preMethodModelList = (List<MethodModel>) apiData.getData();
                    List<MethodModel> deleteMethodList = this.compareApiInfo2Delete(methodModelList, preMethodModelList);
                    this.deleteMethodInfo(deleteMethodList);                    // 删除失效的方法

                    List<MethodModel> uploadMethodList = this.compareApiInfo2Upload(methodModelList, preMethodModelList);
                    this.pushLocalData(uploadMethodList);

                    this.syncLocalData(methodModelList, preMethodModelList);    // 同步数据
                }

                this.updateMethodInfo(methodModelList);         // 更新
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
     * @param newMethodModelList 本地api信息
     * @param preMethodModelList 服务端api信息
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2020/12/27 17:44
     */
    public List<MethodModel> compareApiInfo2Delete(List<MethodModel> newMethodModelList, List<MethodModel> preMethodModelList) {
        Map<String, MethodModel> localMapInfo = this.getMethodMapInfo(newMethodModelList);
        Map<String, MethodModel> serverMapInfo = this.getMethodMapInfo(preMethodModelList);

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
     * @param newMethodModelList 本地
     * @param preMethodModelList 远程
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2020/12/27 22:32
     */
    public List<MethodModel> compareApiInfo2Upload(List<MethodModel> newMethodModelList, List<MethodModel> preMethodModelList) {
        Map<String, MethodModel> localMapInfo = this.getMethodMapInfo(newMethodModelList);
        Map<String, MethodModel> serverMapInfo = this.getMethodMapInfo(preMethodModelList);

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

    }
     
    /**
     * 将远程数据同步到本地
     * @param newMethodModelList 本地
     * @param preMethodModelList 服务端
     * @return
     * @author yangxiao
     * @date 2020/12/27 22:29
     */
    public void syncLocalData(List<MethodModel> newMethodModelList, List<MethodModel> preMethodModelList) {
        Map<String, MethodModel> localMapInfo = this.getMethodMapInfo(newMethodModelList);
        Map<String, MethodModel> serverMapInfo = this.getMethodMapInfo(preMethodModelList);

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

    public void updateMethodInfo(List<MethodModel> data) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        request.getSession().setAttribute(projectName, data);
    }

    public void deleteMethodInfo(List<MethodModel> data) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        request.getSession().setAttribute(projectName, data);
    }

    @RequestMapping(value = "getProjectData", method = RequestMethod.POST)
    public JsonModel getProjectData(@RequestBody Map<String, String> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = map.get("projectName");
            jsonModel.success("获得数据成功", request.getSession().getAttribute(projectName));
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    @RequestMapping(value = "savePageData", method = RequestMethod.POST)
    public void saveData(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String path = (String) map.get("path");
        System.out.println(path);
        request.getSession().setAttribute(path, map);
    }

    @RequestMapping(value = "getPageData")
    public JsonModel getPageData(@RequestBody Map<String, String> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String path = map.get("path");
            jsonModel.success("获得数据成功", request.getSession().getAttribute(path));
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 保存接口信息
     * @param map
     * @param request
     * @return quickcore.common.JsonModel
     * @author yangxiao
     * @date 2020/12/27 22:18
     */
    @RequestMapping(value = "saveApiInfo")
    public JsonModel saveApiInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = map.get("path");
            // TODO
            jsonModel.success("获得数据成功", "");
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 同步本地api数据到服务器
     * @param map
     * @param request
     * @return quickcore.common.JsonModel
     * @author yangxiao
     * @date 2020/12/27 22:23
     */
    @RequestMapping(value = "pushLocalData")
    public JsonModel pushLocalData(@RequestBody Map<String, String> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = map.get("path");
            // TODO
            jsonModel.success("获得数据成功", "");
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
}
