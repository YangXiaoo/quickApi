package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("api_doc")
public class ApiDoc {
  @TableId
  private String apiDocId;
  private String projectId;
  private String methodUrl;
  private String apiJsonData;
  private String userName;
  private String userId;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private Date createTime;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private Date updateTime;
  private String deleteFlag;
  private String projectName;


  public String getApiDocId() {
    return apiDocId;
  }

  public void setApiDocId(String apiDocId) {
    this.apiDocId = apiDocId;
  }


  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
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
