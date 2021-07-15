package com.quickapi.server.common.utils;

import com.quickapi.server.web.dao.entity.ProjectApiMethod;

import java.util.Map;

public class ModelUtil {

    /**
     * 将map转换为MethodModel
     * @param map MethodModel的map形式
     * @return com.quickapi.server.web.dao.entity.ProjectApiMethod
     * @author yangxiao
     * @date 2021/1/6 22:39
     */
    public static ProjectApiMethod mapToMethodModel(Map<String, Object> map) {
        ProjectApiMethod projectApiMethod = new ProjectApiMethod();
        projectApiMethod.setProjectName((String)map.get("projectName"));
        projectApiMethod.setName((String)map.get("name"));
        projectApiMethod.setMethodGroup((String)map.get("group"));
        projectApiMethod.setMethodDescription((String)map.get("description"));
        projectApiMethod.setMethodName((String)map.get("methodName"));
        projectApiMethod.setClassName((String)map.get("className"));
        projectApiMethod.setRequestType((String)map.get("requestType"));
        projectApiMethod.setContentType((String)map.get("contentType"));
        projectApiMethod.setUrl((String)map.get("url"));
        projectApiMethod.setVersion((String)map.get("version"));
        projectApiMethod.setAuthor((String)map.get("author"));
        projectApiMethod.setDownload((Boolean)map.get("download") ? "true" : "false");
        projectApiMethod.setToken((Boolean)map.get("token") ? "true" : "false");

        return projectApiMethod;
    }
}
