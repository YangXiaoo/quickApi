package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("api_doc")
public class ApiDocEntity {
    private String apiDocId;

    private String projectName;

    private String url;

    private String apiJsonData;

    public String getApiDocId() {
        return apiDocId;
    }

    public void setApiDocId(String apiDocId) {
        this.apiDocId = apiDocId == null ? null : apiDocId.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getApiJsonData() {
        return apiJsonData;
    }

    public void setApiJsonData(String apiJsonData) {
        this.apiJsonData = apiJsonData == null ? null : apiJsonData.trim();
    }
}