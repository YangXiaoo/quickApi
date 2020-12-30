package com.quickapi.web.models;

/**
 * QApiMethod注解
 * @author yangxiao
 */
public class MethodModel implements Comparable<MethodModel>{
    private String name;            // 注解值
    private String group;
    private String description;
    private String methodName;           // 方法名
    private String requestType;
    private String url;
    private String version;
    private String contentType;
    private String author;
    private String createTime;
    private String updateTime;
    private boolean download;
    private boolean token;

    /**
     * 将对象按名称典序升序排序
     * @param o 当前对象
     * @return int
     */
    @Override
    public int compareTo(MethodModel o) {
        return this.methodName.compareTo(o.getMethodName());
    }

    public boolean isToken() {
        return token;
    }

    public void setToken(boolean token) {
        this.token = token;
    }

    public boolean isDownload() {
        return download;
    }
    public void setDownload(boolean download) {
        this.download = download;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
}

