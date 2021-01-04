package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.ApiDocDao;
import com.quickapi.server.web.dao.entity.ApiDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MethodApiDataLogic {
    @Autowired
    private ApiDocDao apiDocDao;

    /**
     * 查找接口文档数据
     * @param projectName 项目名
     * @param url 方法URL
     * @return com.quickapi.server.web.dao.entity.ApiDoc
     * @author yangxiao
     * @date 2021/1/4 21:59
     */
    public ApiDoc findMethodApiData(String projectName, String url) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)) {
            throw new BusinessException("findMethodApiData()参数不完整");
        }
        ApiDoc apiDoc = null;
        QueryWrapper<ApiDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("METHOD_URL", url);
        List<ApiDoc> apiDocList = apiDocDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(apiDocList)) {
            if (apiDocList.size() > 1) {
                throw new BusinessException("findMethodApiData(), projectName: " + projectName
                        + "method url: " + url + ", 找到超过两条数据");
            }

            apiDoc = apiDocList.get(0);
        }

        return apiDoc;
    }
}
