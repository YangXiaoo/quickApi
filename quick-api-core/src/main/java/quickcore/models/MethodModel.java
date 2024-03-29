package quickcore.models;

import quickcore.common.utils.StringUtils;

/**
 * QApiMethod注解
 * @author yangxiao
 */
public class MethodModel implements Comparable<MethodModel>{
    private String name;                // 方法别名
    private String group;               // 所属组别
    private String projectName;
    private String description;

    private String methodName;          // 方法名
    private String className;           // 所在类名
    private String requestType;
    private String url;
    private String version;
    private String contentType;
    private String author;

    private String createTime;
    private String updateTime;
    private String download;
    private String token;
    private String deleteFlag;

    /**
     * 将对象按名称典序升序排序
     * @param o 当前对象
     * @return int
     */
    @Override
    public int compareTo(MethodModel o) {
        return this.methodName.compareTo(o.getMethodName());
    }

    /**
     * 判断对象的值是否相等
     * @param obj 比较对象
     * @return String
     * @author yangxiao
     * @date 2020/12/27 18:06
     */
    public boolean equalsValue(MethodModel obj) {
        return StringUtils.equals(obj.getName(), name)
                && StringUtils.equals(obj.getGroup(), group)
                && StringUtils.equals(obj.getDescription(), description)
                && StringUtils.equals(obj.getMethodName(), methodName)
                && StringUtils.equals(obj.getClassName(), className)
                && StringUtils.equals(obj.getRequestType(), requestType)
                && StringUtils.equals(obj.getUrl(), url)
                && StringUtils.equals(obj.getVersion(), version)
                && StringUtils.equals(obj.getContentType(), contentType)
                && StringUtils.equals(obj.getAuthor(), author)
                && StringUtils.equals(obj.getCreateTime(), createTime)
                && StringUtils.equals(obj.getUpdateTime(), updateTime)
                && StringUtils.equals(obj.getProjectName(), projectName)
                && StringUtils.equals(obj.isDownload(),download)
                && StringUtils.equals(obj.isToken(), token);
    }

    public String isToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String isDownload() {
        return download;
    }
    public void setDownload(String download) {
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
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}

