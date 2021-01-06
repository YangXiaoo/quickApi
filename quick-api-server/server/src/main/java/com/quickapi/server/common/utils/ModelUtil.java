package com.quickapi.server.common.utils;

import com.quickapi.server.web.dao.entity.MethodModel;

import java.util.Map;

public class ModelUtil {

    /**
     * 将map转换为MethodModel
     * @param map MethodModel的map形式
     * @return com.quickapi.server.web.dao.entity.MethodModel
     * @author yangxiao
     * @date 2021/1/6 22:39
     */
    public static MethodModel mapToMethodModel(Map<String, Object> map) {
        MethodModel methodModel = new MethodModel();
        methodModel.setProjectName((String)map.get("projectName"));
        methodModel.setName((String)map.get("name"));
        methodModel.setMethodGroup((String)map.get("group"));
        methodModel.setMethodDescription((String)map.get("description"));
        methodModel.setMethodName((String)map.get("methodName"));
        methodModel.setClassName((String)map.get("className"));
        methodModel.setRequestType((String)map.get("requestType"));
        methodModel.setContentType((String)map.get("contentType"));
        methodModel.setUrl((String)map.get("url"));
        methodModel.setVersion((String)map.get("version"));
        methodModel.setAuthor((String)map.get("author"));
        methodModel.setDownload((Boolean)map.get("download") ? "true" : "false");
        methodModel.setToken((Boolean)map.get("token") ? "true" : "false");

        return methodModel;
    }
}
