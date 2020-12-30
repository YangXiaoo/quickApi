package com.quickapi.server.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quickapi.server.web.dao.entity.ApiDataEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiDataDao extends BaseMapper<ApiDataEntity> {
}