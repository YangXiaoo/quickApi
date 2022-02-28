package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("user_api_method")
public class UserApiMethod {
  @TableId
  private String userMethodId;
  private String url;
  private String methodName;
  private String methodGroup;
  private String userName;
  private String userId;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private Date createTime;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private Date updateTime;
  private String deleteFlag;
  private String requestType;


  public String getUserMethodId() {
    return userMethodId;
  }

  public void setUserMethodId(String userMethodId) {
    this.userMethodId = userMethodId;
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


  public String getMethodGroup() {
    return methodGroup;
  }

  public void setMethodGroup(String methodGroup) {
    this.methodGroup = methodGroup;
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

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }
}
