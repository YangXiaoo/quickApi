package quickcore.core.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import quickcore.annotations.component.QApi;
import quickcore.annotations.component.QApiMethod;
import quickcore.common.constants.CONSTANT_DEFINE;
import quickcore.core.utils.RequestUtil;
import quickcore.core.utils.StringUtils;
import quickcore.models.ApiModel;
import quickcore.models.MethodModel;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 接口扫描
 * @author yangxiao
 */
public class ApiScanner {
    private static final Logger logger = LoggerFactory.getLogger(ApiScanner.class);
    /**
     * 扫描所有接口，获得对应的方法信息
     * @param basePackageArray 包名
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2020/11/29 0:28
     */
    public static List<MethodModel> scanApi(String[] basePackageArray, WebApplicationContext applicationContext)
            throws Exception {
        List<ApiModel> apiModelList = new ArrayList<>();
        List<MethodModel> finalMethodModelList = new ArrayList<>();
        Map<String, List<MethodModel>> methodGroup = new HashMap<>();
        
        if (basePackageArray != null && basePackageArray.length > 0) {
            // 获得所有方法的请求信息
            List<Map<String, Object>> requestMapList = RequestUtil.getRequestMappingInfo(applicationContext);
            logger.info("requestMapList: " + requestMapList);
            // 获得指定包名下的所有java类型文件
            List<String> packageList = new ArrayList<>();
            for (String basePackage : basePackageArray) {
                PackageScanner packageScanner = new PackageScanner(basePackage);
                packageList.addAll(packageScanner.getFullyQualifiedClassNameList());
            }
            logger.info("packageList: " + StringUtils.listToString(packageList));

            // 对每个类进行处理
            for (String className: packageList) {
                Class<?> clazz = Class.forName(className);                      // 装载实例化的类
                ApiModel apiModel = new ApiModel();
                if (!clazz.isAnnotationPresent(QApi.class)) {                   // 没有@QApi注解时的默认处理方式
                    apiModel.setName(clazz.getSimpleName());
                    apiModel.setGroup(clazz.getSimpleName());
                    apiModel.setDescription(clazz.getName());
                } else {
                    // 获取@QApi注解信息
                    if (clazz.isAnnotationPresent(QApi.class)) {
                        QApi qApi = clazz.getAnnotation(QApi.class);
                        if (qApi.hidden()) continue;
                        apiModel.setName(StringUtils.isEmpty(qApi.value()) ? clazz.getSimpleName() : qApi.value());
                        apiModel.setGroup(StringUtils.isEmpty(qApi.group()) ? clazz.getSimpleName() : qApi.group());
                        apiModel.setDescription(StringUtils.isEmpty(qApi.description()) ? clazz.getSimpleName() : qApi.description());
                    }
                }
                
                // 获取类名
                apiModel.setValue(clazz.getSimpleName());

                // 获得所有方法
                Method[] methodArray = clazz.getMethods();
                if (methodArray.length > 0) {
                    List<MethodModel> methodModelList = new ArrayList<>();
                    for (Method method : methodArray) {
                        if (method == null) continue;
                        MethodModel methodModel = new MethodModel();
                        if (!method.isAnnotationPresent(QApiMethod.class)) {
                            methodModel.setName(method.getName());
                            methodModel.setGroup(apiModel.getGroup());
                            methodModel.setDescription(method.getName());
                            methodModel.setMethodName(method.getName());
                            methodModel.setClassName(clazz.getSimpleName());
                        } else {
                            QApiMethod qApiMethod = method.getAnnotation(QApiMethod.class);
                            if (qApiMethod.hidden()) continue;
                            methodModel.setName(StringUtils.isEmpty(qApiMethod.value()) ? method.getName() : qApiMethod.value());
                            methodModel.setGroup(StringUtils.isEmpty(qApiMethod.group()) ? apiModel.getGroup() : qApiMethod.group());
                            methodModel.setDescription(StringUtils.isEmpty(qApiMethod.description()) ? method.getName() : qApiMethod.description());
                            methodModel.setMethodName(method.getName());
                            methodModel.setClassName(clazz.getSimpleName());
                        }
                        methodModel.setUrl(CONSTANT_DEFINE.UNDEFINE_API_URL);

                        // 根据请求映射找到对应的类
                        for (Map<String, Object> requestMap : requestMapList) {
                            String requestClassName = (String) requestMap.get(CONSTANT_DEFINE.KEY_CLASS_NAME);
                            if (requestClassName != null) {
                                if (method.getDeclaringClass().getName().equals(requestClassName)
                                        && method.getName().equals(requestMap.get(CONSTANT_DEFINE.KEY_METHOD_NAME))) {
                                    Object methodURL = requestMap.get(CONSTANT_DEFINE.KEY_METHOD_URL);
                                    Object classURL = requestMap.get(CONSTANT_DEFINE.KEY_CLASS_URL);
                                    logger.info("classURL: " + classURL);
                                    Object requestType = requestMap.get(CONSTANT_DEFINE.KEY_REQUEST_TYPE);
                                    if (methodURL == null) {
                                        methodURL = CONSTANT_DEFINE.UNDEFINE_API_URL;
                                        requestType = CONSTANT_DEFINE.UNDEFINE_REQUEST_TYPE;
                                    }
                                    if (requestType == null) {
                                        requestType = CONSTANT_DEFINE.COMMON_USE_REQUEST_TYPE;
                                    }

                                    methodModel.setUrl(methodURL.toString());
                                    methodModel.setRequestType(requestType.toString());
                                    if (className != null && !StringUtils.isEmpty(classURL.toString())) {
                                        if (StringUtils.equals(apiModel.getName(), clazz.getSimpleName())) {
                                            apiModel.setName(classURL.toString());
                                        }
                                    }
                                }
                            } else {
                                @SuppressWarnings("unchecked")
                                List<String> interfaceNameList = (List<String>) requestMap.get(CONSTANT_DEFINE.KEY_INTERFACE_NAME_LIST);
                                for (String interfaceName : interfaceNameList) {
                                    if (method.getDeclaringClass().getName().equals(interfaceName)
                                            && method.getName().equals(requestMap.get(CONSTANT_DEFINE.KEY_METHOD_NAME))) {
                                        Object methodURL = requestMap.get(CONSTANT_DEFINE.KEY_METHOD_URL);
                                        Object classURL = requestMap.get(CONSTANT_DEFINE.KEY_CLASS_URL);
                                        Object requestType = requestMap.get(CONSTANT_DEFINE.KEY_REQUEST_TYPE);
                                        if (methodURL == null) {
                                            methodURL = CONSTANT_DEFINE.UNDEFINE_API_URL;
                                            requestType = CONSTANT_DEFINE.UNDEFINE_REQUEST_TYPE;
                                        }
                                        if (requestType == null) {
                                            requestType = CONSTANT_DEFINE.COMMON_USE_REQUEST_TYPE;
                                        }

                                        methodModel.setUrl(methodURL.toString());
                                        methodModel.setRequestType(requestType.toString());
                                        if (className != null && !StringUtils.isEmpty(classURL.toString())) {
                                            if (StringUtils.equals(apiModel.getName(), clazz.getSimpleName())) {
                                                apiModel.setName(classURL.toString());
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        // 不是请求方法则跳过
                        if (methodModel.getUrl().equals(CONSTANT_DEFINE.UNDEFINE_API_URL)) continue;

                        if (!methodModel.getGroup().equals(apiModel.getGroup())) {
                            if (!methodGroup.containsKey(methodModel.getGroup())) {
                                List<MethodModel> tmp = new ArrayList<>();
                                tmp.add(methodModel);
                                methodGroup.put(methodModel.getGroup(), tmp);
                            } else {
                                methodGroup.get(methodModel.getGroup()).add(methodModel);
                            }
                        } else {
                            methodModelList.add(methodModel);
                        }
                    }
                    apiModel.setMethodModels(methodModelList);
                    finalMethodModelList.addAll(methodModelList);
                }
                apiModelList.add(apiModel);
            }
        }

        //groupingMethodModel(apiModelList, methodGroup);
        //Map<String, List<ApiModel>> apiModelGroup = groupingApiModel(apiModelList);
        //emptyGroupFilter(apiModelGroup);

        return finalMethodModelList;
    }

    /**
     * 将不在所属类分组的方法放入分组的类中
     * <p>
     *     有些方法会被分到其它组
     * </p>
     * @param apiModelList ApiModel列表
     * @param methodGroup 方法分组
     * @return void
     * @author yangxiao
     * @date 2020/11/29 10:25
     */
    public static void groupingMethodModel(List<ApiModel> apiModelList, Map<String, List<MethodModel>> methodGroup) {
        if (apiModelList != null && methodGroup != null) {
            for (ApiModel apiModel : apiModelList) {
                if (methodGroup.containsKey(apiModel.getGroup())) {
                    apiModel.getMethodModels().addAll(methodGroup.get(apiModel.getGroup()));
                }
            }
        }
    }

    /**
     * 将ApiModel按group分开
     * @param apiModelList ApiModel列表
     * @return java.util.Map<java.lang.String,java.util.List<quickcore.models.ApiModel>>
     * @author yangxiao
     * @date 2020/11/29 10:27
     */
    public static Map<String, List<ApiModel>> groupingApiModel(List<ApiModel> apiModelList) {
        Map<String, List<ApiModel>> groupItems = new HashMap<>();
        for (ApiModel apiModel : apiModelList) {
            if (!groupItems.containsKey(apiModel.getGroup())) {
                List<ApiModel> tmpList = new ArrayList<>();
                tmpList.add(apiModel);
                groupItems.put(apiModel.getGroup(), tmpList);
            } else {
                groupItems.get(apiModel.getGroup()).add(apiModel);
            }
        }
        return groupItems;
    }

    /**
     * 将无效的分组去除
     * @param apiModelGroup 分组后的请求
     * @return void
     * @author yangxiao
     * @date 2020/11/29 17:13
     */
    public static void emptyGroupFilter(Map<String, List<ApiModel>> apiModelGroup) {
        if (apiModelGroup != null) {
            apiModelGroup.entrySet().removeIf(entry -> entry.getValue().get(0).getMethodModels().isEmpty());
        }
    }
}
