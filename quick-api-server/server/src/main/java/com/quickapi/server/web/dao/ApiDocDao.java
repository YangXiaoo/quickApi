package com.quickapi.server.web.dao;

import com.quickapi.server.web.dao.entity.ApiDocEntity;

public interface ApiDocDao {
    int deleteByPrimaryKey(String apiDocId);

    int insert(ApiDocEntity record);

    int insertSelective(ApiDocEntity record);

    ApiDocEntity selectByPrimaryKey(String apiDocId);

    int updateByPrimaryKeySelective(ApiDocEntity record);

    int updateByPrimaryKeyWithBLOBs(ApiDocEntity record);

    int updateByPrimaryKey(ApiDocEntity record);
}