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
import com.quickapi.server.web.dao.entity.ApiDoc;
import com.quickapi.server.web.dao.entity.UserApi;
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
     * @return com.quickapi.server.web.dao.entity.ApiDoc
     * @userId yangxiao
     * @date 2021/1/4 21:59
     */
    public List<ApiDoc> findMethodApiData(String projectName, String url) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)) {
            throw new BusinessException("findMethodApiData()参数不完整");
        }
        QueryWrapper<ApiDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("METHOD_URL", url);
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

        ApiDoc apiDoc = new ApiDoc();
        apiDoc.setProjectName(projectName);
        apiDoc.setMethodUrl(url);
        apiDoc.setApiJsonData(apiData);
        apiDoc.setUserId(userId);
        apiDoc.setCreateTime(DateTool.getCurrentDate());
        apiDoc.setDeleteFlag(CONSTANT_DEFINE.NOT_DELETE);
        apiDoc.setApiDocId(UUIDUtil.getUUID());
        while (!CollectionUtils.isEmpty(selectApiById(apiDoc))) {
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
    public List<ApiDoc> selectApiById(ApiDoc apiDoc) {
        List<ApiDoc> ret = null;
        if (apiDoc != null) {
            QueryWrapper<ApiDoc> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("API_DOC_ID", apiDoc.getApiDocId());
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
            ApiDoc apiDoc = apiDocDao.selectById(apiDocId);
            if (apiDoc != null) {
                apiDoc.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                apiDocDao.updateById(apiDoc);
            }
        }
    }

    /**
     * 根据URL查询用户的接口文档数据
     * @param userName 用户姓名
     * @param url url
     * @return com.quickapi.server.web.dao.entity.UserApi
     * @author yangxiao
     * @date 2021/1/14 20:58
     */
    public UserApi findUserMethodApiData(String userName, String url) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(url)) {
            throw new BusinessException("findUserMethodApiData()参数不完整");
        }
        UserApi apiDoc = null;
        QueryWrapper<UserApi> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("METHOD_URL", url);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
        List<UserApi> apiDocList = userApiDao.selectList(queryWrapper);
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

        QueryWrapper<UserApi> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("METHOD_URL", url);
        List<UserApi> apiDocList = userApiDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(apiDocList)) {
            if (apiDocList.size() > 1) {                                    // 删除duo
               for (UserApi tmp : apiDocList) {
                   tmp.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                   UpdateWrapper<UserApi> updateWrapper = new UpdateWrapper<>();
                   updateWrapper.eq("METHOD_URL", url);
                   updateWrapper.eq("USER_NAME", userName);
                   userApiDao.update(tmp, updateWrapper);
               }
            } else {                                                        // 更新
                UserApi apiDoc = apiDocList.get(0);
                apiDoc.setApiJsonData(apiData);
                apiDoc.setUpdateTime(DateTool.getCurrentDate());

                UpdateWrapper<UserApi> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("METHOD_URL", url);
                updateWrapper.eq("USER_NAME", userName);
                userApiDao.update(apiDoc, updateWrapper);
                insertFlag = false;
            }
        }

        if (insertFlag) {
            UserApi apiDoc = new UserApi();
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
     * @param apiDoc UserApi
     * @return java.util.List<com.quickapi.server.web.dao.entity.UserApi>
     * @author yangxiao
     * @date 2021/1/14 21:05
     */
    public List<UserApi> selectUserApiById(UserApi apiDoc) {
        List<UserApi> ret = null;
        if (apiDoc != null) {
            QueryWrapper<UserApi> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("USER_API_ID", apiDoc.getUserApiId());
            ret = userApiDao.selectList(queryWrapper);
        }

        return ret;
    }
}
