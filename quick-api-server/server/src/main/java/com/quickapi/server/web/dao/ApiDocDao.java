package com.quickapi.server.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quickapi.server.web.dao.entity.ApiDoc;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiDocDao extends BaseMapper<ApiDoc> {
}