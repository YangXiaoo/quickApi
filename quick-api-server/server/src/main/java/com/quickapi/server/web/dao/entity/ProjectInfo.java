package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("project_info")
public class ProjectInfo {

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
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;


  public String getProjectInfoId() {
    return projectInfoId;
  }

  public void setProjectInfoId(String projectInfoId) {
    this.projectInfoId = projectInfoId;
  }


  public String getBasePackages() {
    return basePackages;
  }

  public void setBasePackages(String basePackages) {
    this.basePackages = basePackages;
  }


  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getServerNames() {
    return serverNames;
  }

  public void setServerNames(String serverNames) {
    this.serverNames = serverNames;
  }


  public String getHostServiceName() {
    return hostServiceName;
  }

  public void setHostServiceName(String hostServiceName) {
    this.hostServiceName = hostServiceName;
  }


  public String getLocalServiceName() {
    return localServiceName;
  }

  public void setLocalServiceName(String localServiceName) {
    this.localServiceName = localServiceName;
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


  public String getEnable() {
    return enable;
  }

  public void setEnable(String enable) {
    this.enable = enable;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

}
