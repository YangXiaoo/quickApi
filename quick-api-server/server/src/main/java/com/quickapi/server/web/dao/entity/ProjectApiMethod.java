package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("project_api_method")
public class ProjectApiMethod {
  @TableId
  private String dataApiId;
  private String projectName;
  private String name;
  private String methodGroup;
  private String methodDescription;
  private String methodName;
  private String className;
  private String requestType;
  private String contentType;
  private String url;
  private String version;
  private String author;
  private Date createTime;
  private Date updateTime;
  private String download;
  private String token;
  private String deleteFlag;


  public String getDataApiId() {
    return dataApiId;
  }

  public void setDataApiId(String dataApiId) {
    this.dataApiId = dataApiId;
  }


  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getMethodGroup() {
    return methodGroup;
  }

  public void setMethodGroup(String methodGroup) {
    this.methodGroup = methodGroup;
  }


  public String getMethodDescription() {
    return methodDescription;
  }

  public void setMethodDescription(String methodDescription) {
    this.methodDescription = methodDescription;
  }


  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }


  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }


  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }


  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }


  public String getDownload() {
    return download;
  }

  public void setDownload(String download) {
    this.download = download;
  }


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  public String getDeleteFlag() {
    return deleteFlag;
  }

  public void setDeleteFlag(String deleteFlag) {
    this.deleteFlag = deleteFlag;
  }

}
