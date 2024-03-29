package com.quickapi.server.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

/**
 * 方法工具类
 * @author yangxiao
 * @date 2020-11-28
 */
public class RequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

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
    public static Object callApi(String path, String contentType, String headerJson, String queryData, String type) {
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
                    object = restTemplate.exchange(path, HttpMethod.POST, requestEntity, Map.class);
                }
                if (StringUtils.equals("put", type)) {
                    object = restTemplate.exchange(path, HttpMethod.PUT, requestEntity, Map.class);
                }
                if (StringUtils.equals("delete", type)) {
                    object = restTemplate.exchange(path, HttpMethod.DELETE, requestEntity, Map.class);
                }

            } else {
                HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

                if (StringUtils.equals("get", type)) {
                    object = restTemplate.exchange(path + getPramData, HttpMethod.GET, null, Map.class);
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
}
