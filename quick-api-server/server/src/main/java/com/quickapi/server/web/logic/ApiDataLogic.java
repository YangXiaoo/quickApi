package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.ApiDataDao;
import com.quickapi.server.web.dao.entity.ApiDataEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiDataLogic {
    @Autowired
    private ApiDataDao apiDataDao;

    /**
     * 保存一条接口信息
     * @param apiDataEntity 单个接口信息
     * @return void
     * @author yangxiao
     * @date 2020/12/29 21:09
     */
    public void insertApiData(ApiDataEntity apiDataEntity) {
        if (apiDataEntity != null) {

            apiDataDao.insert(apiDataEntity);
        }
    }

    /**
     * 根据项目名称查找项目所有接口
     * @param projectName 项目名称
     * @return java.util.List<com.quickapi.server.web.dao.entity.ApiDataEntity>
     * @author yangxiao
     * @date 2020/12/29 21:21
     */
    public List<ApiDataEntity> findApiDataByProjectName(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("findByProjectName()参数不完整");
        }
        QueryWrapper<ApiDataEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

        return apiDataDao.selectList(queryWrapper);
    }
}
