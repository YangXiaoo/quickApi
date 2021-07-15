package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.UUIDUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.ApiDocDao;
import com.quickapi.server.web.dao.UserApiDao;
import com.quickapi.server.web.dao.entity.ProjectApIExample;
import com.quickapi.server.web.dao.entity.UserApiPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MethodApiDataLogic {
    @Autowired
    private ApiDocDao apiDocDao;
    @Autowired
    private UserApiDao userApiDao;

    /**
     * 查找接口文档数据
     * @param projectName 项目名
     * @param url 方法URL
     * @return com.quickapi.server.web.dao.entity.ProjectApIExample
     * @userId yangxiao
     * @date 2021/1/4 21:59
     */
    public List<ProjectApIExample> findMethodApiData(String projectName, String url) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)) {
            throw new BusinessException("findMethodApiData()参数不完整");
        }
        QueryWrapper<ProjectApIExample> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("METHOD_URL", url);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

        return apiDocDao.selectList(queryWrapper);
    }

    /**
     * 查找用户提交记录
     * <p>
     *     用户姓名参数不传时，查看整个项目的提交记录
     * </p>
     * @param projectName 项目名
     * @param userName 用户名
     * @return java.util.List<com.quickapi.server.web.dao.entity.ProjectApIExample>
     * @author yangxiao
     * @date 2021/1/26 21:00
     */
    public List<ProjectApIExample> getProjectMethodApiDataHistory(String projectName, String userName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("getProjectMethodApiDataHistory()参数不完整");
        }
        QueryWrapper<ProjectApIExample> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("CREATE_TIME");
        queryWrapper.eq("PROJECT_NAME", projectName);
        if (!StringUtils.isBlank(userName)) {
            queryWrapper.eq("USER_NAME", userName);
        }
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

        return apiDocDao.selectList(queryWrapper);
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

        ProjectApIExample projectApIExample = new ProjectApIExample();
        projectApIExample.setProjectName(projectName);
        projectApIExample.setMethodUrl(url);
        projectApIExample.setApiJsonData(apiData);
        projectApIExample.setUserId(userId);
        projectApIExample.setCreateTime(DateTool.getCurrentDate());
        projectApIExample.setDeleteFlag(CONSTANT_DEFINE.NOT_DELETE);
        projectApIExample.setApiDocId(UUIDUtil.getUUID());
        while (!CollectionUtils.isEmpty(selectApiById(projectApIExample))) {
            projectApIExample.setApiDocId(UUIDUtil.getUUID());
        }

        apiDocDao.insert(projectApIExample);
    }

    /**
     * 根据ID查询接口文档
     * @param projectApIExample 查询条件
     * @return java.util.List<com.quickapi.server.web.dao.entity.ProjectApIExample>
     * @author yangxiao
     * @date 2021/1/5 20:39
     */
    public List<ProjectApIExample> selectApiById(ProjectApIExample projectApIExample) {
        List<ProjectApIExample> ret = null;
        if (projectApIExample != null) {
            QueryWrapper<ProjectApIExample> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("API_DOC_ID", projectApIExample.getApiDocId());
            ret = apiDocDao.selectList(queryWrapper);
        }

        return ret;
    }

    /**
     * 删除接口
     * @param apiDocId 用户测试项目接口主键ID
     * @return void
     * @author yangxiao
     * @date 2021/1/18 19:46
     */
    public void deleteMethodApiData(String apiDocId) {
        if (StringUtils.isNotBlank(apiDocId)) {
            ProjectApIExample projectApIExample = apiDocDao.selectById(apiDocId);
            if (projectApIExample != null) {
                projectApIExample.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                apiDocDao.updateById(projectApIExample);
            }
        }
    }

    /**
     * 根据URL查询用户的接口文档数据
     * @param userName 用户姓名
     * @param url url
     * @return com.quickapi.server.web.dao.entity.UserApiPageData
     * @author yangxiao
     * @date 2021/1/14 20:58
     */
    public UserApiPageData findUserMethodApiData(String userName, String url) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(url)) {
            throw new BusinessException("findUserMethodApiData()参数不完整");
        }
        UserApiPageData apiDoc = null;
        QueryWrapper<UserApiPageData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("METHOD_URL", url);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
        List<UserApiPageData> apiDocList = userApiDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(apiDocList)) {
            if (apiDocList.size() > 1) {
                throw new BusinessException("findUserMethodApiData(), userName: " + userName
                        + "method url: " + url + ", 找到超过两条数据");
            }

            apiDoc = apiDocList.get(0);
        }

        return apiDoc;
    }

    /**
     * 保存用户接口文档数据
     * <p>
     *     存在更新，不存在则新建
     * </p>
     * @param userName 用户姓名
     * @param url 路由
     * @param apiData 页面接口文档数据
     * @return void
     * @author yangxiao
     * @date 2021/1/14 21:07
     */
    public void saveUserMethodApiData(String userName, String url, String apiData) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(url)
                || StringUtils.isBlank(apiData)) {
            throw new BusinessException("saveUserMethodApiData()参数不完整");
        }
        boolean insertFlag = true;

        QueryWrapper<UserApiPageData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("METHOD_URL", url);
        List<UserApiPageData> apiDocList = userApiDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(apiDocList)) {
            if (apiDocList.size() > 1) {                                    // 删除duo
               for (UserApiPageData tmp : apiDocList) {
                   tmp.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                   UpdateWrapper<UserApiPageData> updateWrapper = new UpdateWrapper<>();
                   updateWrapper.eq("METHOD_URL", url);
                   updateWrapper.eq("USER_NAME", userName);
                   userApiDao.update(tmp, updateWrapper);
               }
            } else {                                                        // 更新
                UserApiPageData apiDoc = apiDocList.get(0);
                apiDoc.setApiJsonData(apiData);
                apiDoc.setUpdateTime(DateTool.getCurrentDate());

                UpdateWrapper<UserApiPageData> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("METHOD_URL", url);
                updateWrapper.eq("USER_NAME", userName);
                userApiDao.update(apiDoc, updateWrapper);
                insertFlag = false;
            }
        }

        if (insertFlag) {
            UserApiPageData apiDoc = new UserApiPageData();
            apiDoc.setApiJsonData(apiData);
            apiDoc.setMethodUrl(url);
            apiDoc.setUserName(userName);
            apiDoc.setCreateTime(DateTool.getCurrentDate());
            apiDoc.setUserApiId(UUIDUtil.getUUID());
            apiDoc.setDeleteFlag(CONSTANT_DEFINE.NOT_DELETE);
            while (!CollectionUtils.isEmpty(selectUserApiById(apiDoc))) {
                apiDoc.setUserApiId(UUIDUtil.getUUID());
            }

            userApiDao.insert(apiDoc);
        }
    }

    /**
     * 根据用户接口ID查询数据
     * @param apiDoc UserApiPageData
     * @return java.util.List<com.quickapi.server.web.dao.entity.UserApiPageData>
     * @author yangxiao
     * @date 2021/1/14 21:05
     */
    public List<UserApiPageData> selectUserApiById(UserApiPageData apiDoc) {
        List<UserApiPageData> ret = null;
        if (apiDoc != null) {
            QueryWrapper<UserApiPageData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("USER_API_ID", apiDoc.getUserApiId());
            ret = userApiDao.selectList(queryWrapper);
        }

        return ret;
    }

    /**
     * 删除个人接口
     * @param userName 用户名
     * @param url 接口路由
     * @return void
     * @author yangxiao
     * @date 2021/1/20 21:17
     */
    public void deleteUserMethodApiData(String userName, String url) {
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(url)) {
            UserApiPageData userApiPageData = this.findUserMethodApiData(userName, url);
            if (userApiPageData != null) {
                userApiPageData.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                UpdateWrapper<UserApiPageData> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("USER_API_ID", userApiPageData.getUserApiId());
                userApiDao.update(userApiPageData, updateWrapper);
            }

        }
    }
}
