package com.quickapi.server.web.dao;

import com.quickapi.server.web.dao.entity.ProjectInfoEntity;

public interface ProjectInfoDao {
    int deleteByPrimaryKey(String projectInfoId);

    int insert(ProjectInfoEntity record);

    int insertSelective(ProjectInfoEntity record);

    ProjectInfoEntity selectByPrimaryKey(String projectInfoId);

    int updateByPrimaryKeySelective(ProjectInfoEntity record);

    int updateByPrimaryKey(ProjectInfoEntity record);
}