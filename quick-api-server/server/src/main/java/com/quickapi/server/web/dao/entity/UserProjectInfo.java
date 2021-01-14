package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("user_project_info")
public class UserProjectInfo {
  @TableId
  private String userProjectInfoId;
  private String projectInfoId;
  private String projectName;
  private String projectDesc;
  private String userName;
  private Date createTime;
  private Date updateTime;
  private String deleteFlag;
  private String userId;


  public String getUserProjectInfoId() {
    return userProjectInfoId;
  }

  public void setUserProjectInfoId(String userProjectInfoId) {
    this.userProjectInfoId = userProjectInfoId;
  }


  public String getProjectInfoId() {
    return projectInfoId;
  }

  public void setProjectInfoId(String projectInfoId) {
    this.projectInfoId = projectInfoId;
  }


  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }


  public String getProjectDesc() {
    return projectDesc;
  }

  public void setProjectDesc(String projectDesc) {
    this.projectDesc = projectDesc;
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


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

}
