package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("user_project_page_data")
public class UserProjectPageData {
  @TableId
  private String userProjectPageDataId;
  private String projectId;
  private String projectName;
  private String methodUrl;
  private String userId;
  private String userName;
  private Date createTime;
  private String apiJsonData;
  private String deleteFlag;
  private Date updateTime;


  public String getUserProjectPageDataId() {
    return userProjectPageDataId;
  }

  public void setUserProjectPageDataId(String userProjectPageDataId) {
    this.userProjectPageDataId = userProjectPageDataId;
  }


  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
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


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  public String getApiJsonData() {
    return apiJsonData;
  }

  public void setApiJsonData(String apiJsonData) {
    this.apiJsonData = apiJsonData;
  }


  public String getDeleteFlag() {
    return deleteFlag;
  }

  public void setDeleteFlag(String deleteFlag) {
    this.deleteFlag = deleteFlag;
  }


  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
