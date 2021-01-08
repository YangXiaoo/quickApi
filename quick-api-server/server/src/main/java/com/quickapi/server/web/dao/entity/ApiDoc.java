package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("api_doc")

public class ApiDoc {

  private String apiDocId;
  private String projectName;
  private String methodUrl;
  private String apiJsonData;
  private String userName;
  private String userId;
  private Date createTime;
  private Date updateTime;


  public String getApiDocId() {
    return apiDocId;
  }

  public void setApiDocId(String apiDocId) {
    this.apiDocId = apiDocId;
  }


  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }


  public String getMethodUrl() {
    return methodUrl;
  }

  public void setMethodUrl(String methodUrl) {
    this.methodUrl = methodUrl;
  }


  public String getApiJsonData() {
    return apiJsonData;
  }

  public void setApiJsonData(String apiJsonData) {
    this.apiJsonData = apiJsonData;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

}
