package quickcore.core.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import quickcore.annotations.component.QApi;
import quickcore.annotations.component.QApiMethod;
import quickcore.common.constants.CONSTANT_DEFINE;
import quickcore.common.utils.PathUtil;
import quickcore.common.utils.StringUtils;
import quickcore.models.ApiModel;
import quickcore.models.MethodModel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口扫描
 * @author yangxiao
 */
public class ApiScanner {
    private static final Logger logger = LoggerFactory.getLogger(ApiScanner.class);
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

    /**
     * 获取指定包下所有请求方法信息
     *
     * @param basePackageArray
     * @return java.util.List<quickcore.models.MethodModel>
     * @author yangxiao
     * @date 2021/11/10 21:42
     */
    public static List<MethodModel> getPackagesRequestMethodModelList(String[] basePackageArray)
            throws Exception {
        List<ApiModel> apiModelList = new ArrayList<>();
        List<MethodModel> finalMethodModelList = new ArrayList<>();
        Map<String, List<MethodModel>> methodGroup = new HashMap<>();

        if (basePackageArray != null && basePackageArray.length > 0) {
            List<String> rawPackageList = new ArrayList<>();
            for (String basePackage : basePackageArray) {
                PackageScanner packageScanner = new PackageScanner(basePackage);
                rawPackageList.addAll(packageScanner.getFullyQualifiedClassNameList());
            }
            logger.info("rawPackageList: " + StringUtils.listToString(rawPackageList));

            for (String className: rawPackageList) {
                ApiModel apiModel = new ApiModel();
                Class<?> clazz = Class.forName(className);

                // 判断是否为请求类
                boolean isRequestClazz = false;
                String clazzPath = "";
                Annotation[] parentAnnotationArray = clazz.getAnnotations();
                for (Annotation annotation : parentAnnotationArray) {
                    if (annotation instanceof RequestMapping
                            || annotation instanceof PostMapping
                            || annotation instanceof RestController
                            || annotation instanceof Controller
                            || annotation instanceof ResponseBody) {
                        if (annotation instanceof  RequestMapping) {
                            RequestMapping requestMapping = (RequestMapping) annotation;
                            clazzPath = String.join("/", requestMapping.value());
                        } else if (annotation instanceof  GetMapping) {
                            GetMapping requestMapping = (GetMapping) annotation;
                            clazzPath = String.join("/", requestMapping.value());
                        } else if (annotation instanceof PostMapping) {
                            PostMapping requestMapping = (PostMapping) annotation;
                            clazzPath = String.join("/", requestMapping.value());
                        }

                        isRequestClazz = true;
                    }
                }
                if (!isRequestClazz) {
                    continue;
                }

                logger.info("request class: " + className);
                if (!clazz.isAnnotationPresent(QApi.class)) {
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
                if (methodArray.length == 0) {
                    continue;
                }

                List<MethodModel> methodModelList = new ArrayList<>();
                for (Method method : methodArray) {
                    if (method == null) continue;
                    MethodModel methodModel = new MethodModel();
                    if (!method.isAnnotationPresent(QApiMethod.class)) {
                        boolean isRequestMethod = false;
                        Annotation[] methodAnnotations = method.getAnnotations();
                        for (Annotation annotation : methodAnnotations) {
                            if (annotation instanceof  RequestMapping) {
                                RequestMapping requestMapping = (RequestMapping) annotation;
                                methodModel.setUrl(PathUtil.joinPath(clazzPath,
                                        String.join("/", requestMapping.value())));
                                if (requestMapping.method().length == 0) {
                                    methodModel.setRequestType(CONSTANT_DEFINE.REQUEST_TYPE_GET);
                                } else {
                                    methodModel.setRequestType(requestMapping.method()[0].name());
                                }

                                isRequestMethod = true;
                            } else if (annotation instanceof  GetMapping) {
                                GetMapping requestMapping = (GetMapping) annotation;
                                methodModel.setUrl(PathUtil.joinPath(clazzPath,
                                        String.join("/", requestMapping.value())));
                                methodModel.setRequestType(CONSTANT_DEFINE.REQUEST_TYPE_GET);

                                isRequestMethod = true;
                            } else if (annotation instanceof PostMapping) {
                                PostMapping requestMapping = (PostMapping) annotation;
                                methodModel.setUrl(PathUtil.joinPath(clazzPath,
                                        String.join("/", requestMapping.value())));
                                methodModel.setRequestType(CONSTANT_DEFINE.REQUEST_TYPE_POST);

                                isRequestMethod = true;
                            }
                        }
                        if (!isRequestMethod) {
                            continue;
                        }
                        methodModel.setName(method.getName());
                        methodModel.setGroup(apiModel.getGroup());
                        methodModel.setDescription(method.getName());
                        methodModel.setMethodName(method.getName());
                        methodModel.setClassName(clazz.getSimpleName());
                    } else {
                        QApiMethod qApiMethod = method.getAnnotation(QApiMethod.class);
                        if (qApiMethod.hidden()) continue;
                        methodModel.setName(StringUtils.isEmpty(qApiMethod.value()) ?
                                method.getName()
                                : qApiMethod.value());
                        methodModel.setGroup(StringUtils.isEmpty(qApiMethod.group()) ?
                                apiModel.getGroup()
                                : qApiMethod.group());
                        methodModel.setDescription(StringUtils.isEmpty(qApiMethod.description()) ?
                                method.getName()
                                : qApiMethod.description());
                        methodModel.setMethodName(method.getName());
                        methodModel.setClassName(clazz.getSimpleName());
                    }

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
                apiModelList.add(apiModel);
            }
        }

        return finalMethodModelList;
    }
}
