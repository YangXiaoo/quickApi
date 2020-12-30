package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("project_info")
public class ProjectInfoEntity {
    private String projectInfoId;

    private String basePackages;

    private String projectName;

    private String description;

    private String serverNames;

    private String hostServiceName;

    private String localServiceName;

    private String version;

    private String author;

    private String enable;

    public String getProjectInfoId() {
        return projectInfoId;
    }

    public void setProjectInfoId(String projectInfoId) {
        this.projectInfoId = projectInfoId == null ? null : projectInfoId.trim();
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages == null ? null : basePackages.trim();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getServerNames() {
        return serverNames;
    }

    public void setServerNames(String serverNames) {
        this.serverNames = serverNames == null ? null : serverNames.trim();
    }

    public String getHostServiceName() {
        return hostServiceName;
    }

    public void setHostServiceName(String hostServiceName) {
        this.hostServiceName = hostServiceName == null ? null : hostServiceName.trim();
    }

    public String getLocalServiceName() {
        return localServiceName;
    }

    public void setLocalServiceName(String localServiceName) {
        this.localServiceName = localServiceName == null ? null : localServiceName.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable == null ? null : enable.trim();
    }
}