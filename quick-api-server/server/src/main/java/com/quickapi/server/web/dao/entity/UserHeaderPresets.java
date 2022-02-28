package com.quickapi.server.web.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@TableName("user_header_presets")
public class UserHeaderPresets {
  @TableId
  private String presetId;
  private String userName;
  private String name;
  private String value;
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private Date createTime;
  private String deleteFlag;


  public String getPresetId() {
    return presetId;
  }

  public void setPresetId(String presetId) {
    this.presetId = presetId;
  }


  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  public String getDeleteFlag() {
    return deleteFlag;
  }

  public void setDeleteFlag(String deleteFlag) {
    this.deleteFlag = deleteFlag;
  }

}
