package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.UUIDUtil;
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
     * @userId yangxiao
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

    /**
     * 保存项目接口
     * @param projectName 项目名
     * @param url 接口路由
     * @param apiData 接口文档数据
     * @param userId 创建者
     * @return void
     * @author yangxiao
     * @date 2021/1/5 20:42
     */
    public void saveMethodApiData(String projectName, String url, String apiData, String userId) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url) 
                || StringUtils.isBlank(apiData) || StringUtils.isBlank(userId)) {
                    throw new BusinessException("saveMethodApiData()参数不完整");
        }

        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setProjectName(projectName);
        apiDoc.setMethodUrl(url);
        apiDoc.setApiJsonData(apiData);
        apiDoc.setUserId(userId);
        apiDoc.setCreateTime(DateTool.getCurrentDate());
        apiDoc.setApiDocId(UUIDUtil.getUUID());
        while (!CollectionUtils.isEmpty(selectById(apiDoc))) {
            apiDoc.setApiDocId(UUIDUtil.getUUID());
        }

        apiDocDao.insert(apiDoc);
    }

    /**
     * 根据ID查询接口文档
     * @param apiDoc 查询条件
     * @return java.util.List<com.quickapi.server.web.dao.entity.ApiDoc>
     * @author yangxiao
     * @date 2021/1/5 20:39
     */
    public List<ApiDoc> selectById(ApiDoc apiDoc) {
        List<ApiDoc> ret = null;
        if (apiDoc != null) {
            QueryWrapper<ApiDoc> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("API_DOC_ID", apiDoc.getUserId());
            ret = apiDocDao.selectList(queryWrapper);
        }

        return ret;
    }
}
