package quickcore.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import quickcore.common.constants.CONSTANT_DEFINE;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 方法工具类
 * @author yangxiao
 * @date 2020-11-28
 */
public class RequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 获得所有方法的请求信息
     * <p>
     *     获得方法名，类名，接口名列表，类URL，方法URL,请求类型
     * </p>
     * @param applicationContext WebApplicationContext
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author yangxiao
     * @date 2020/11/28 18:42
     */
    public static List<Map<String, Object>> getRequestMappingInfo(WebApplicationContext applicationContext) {
        List<Map<String, Object>> resultList = new ArrayList<>();   // 定义返回数据

        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> requestMap = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> requestMappingInfoHandlerMethodEntry : requestMap.entrySet()) {
            Map<String, Object> curResultMap = new LinkedHashMap<>();

            // 获得请求映射信息和对应的处理方法
            RequestMappingInfo requestMappingInfo = requestMappingInfoHandlerMethodEntry.getKey();
            HandlerMethod handlerMethod = requestMappingInfoHandlerMethodEntry.getValue();

            // 获得方法名
            curResultMap.put(CONSTANT_DEFINE.KEY_METHOD_NAME, handlerMethod.getMethod().getName());

            // 获得方法所在类名
            curResultMap.put(CONSTANT_DEFINE.KEY_CLASS_NAME, handlerMethod.getMethod().getDeclaringClass().getName());

            // 获得方法所在类声明的接口名
            Class<?>[] interfaces = handlerMethod.getMethod().getDeclaringClass().getInterfaces();
            List<String> interfaceNameList = new ArrayList<>();
            if (interfaces != null) {
                for (Class<?> c : interfaces) {
                    interfaceNameList.add(c.getName());
                }
            }
            curResultMap.put(CONSTANT_DEFINE.KEY_INTERFACE_NAME_LIST, interfaceNameList);

            // 获得方法所在类的@RequestMapping注解
            Annotation[] parentAnnotationArray = handlerMethod.getBeanType().getAnnotations();
            for (Annotation annotation : parentAnnotationArray) {
                if (annotation instanceof RequestMapping) {
                    RequestMapping requestMapping = (RequestMapping) annotation;
                    if (requestMapping.value().length > 0) {
                        curResultMap.put(CONSTANT_DEFINE.KEY_CLASS_URL, requestMapping.value()[0]);
                    }
                }
            }

            // 获得方法的URL
            PatternsRequestCondition patternsRequestCondition = requestMappingInfo.getPatternsCondition();
            if (patternsRequestCondition != null) {
                logger.debug("patternsRequestCondition.getPatterns().size(): " + patternsRequestCondition.getPatterns().size());
                for (String url : patternsRequestCondition.getPatterns()) {
                    curResultMap.put(CONSTANT_DEFINE.KEY_METHOD_URL, url);
                }
            }

            // 获得请求类型
            RequestMethodsRequestCondition methodsRequestCondition = requestMappingInfo.getMethodsCondition();
            for (RequestMethod requestMethod : methodsRequestCondition.getMethods()) {
                curResultMap.put(CONSTANT_DEFINE.KEY_REQUEST_TYPE, requestMethod.toString());
            }

            resultList.add(curResultMap);
        }

        return resultList;
    }
}
