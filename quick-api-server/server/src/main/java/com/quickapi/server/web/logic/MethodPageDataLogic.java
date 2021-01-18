package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.UUIDUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.MethodPageDataDao;
import com.quickapi.server.web.dao.entity.UserProjectPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MethodPageDataLogic {
    @Autowired
    private MethodPageDataDao methodPageDataDao;

    /**
     * 存在更新，不存在删除
     * @param projectName 项目名
     * @param url 路由
     * @param pageJsonData 页面数据，前端pageData
     * @param userName 用户名 TODO 修改为用户ID
     * @return void
     * @author yangxiao
     * @date 2021/1/17 17:49
     */
    public void saveUserProjectMethodPageData(String projectName, String url, String pageJsonData, String userName) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url)
                || StringUtils.isBlank(pageJsonData) || StringUtils.isBlank(userName)) {
            throw new BusinessException("saveUserProjectMethodPageData()参数不完整");
        }
        boolean insertFlag = true;

        QueryWrapper<UserProjectPageData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("METHOD_URL", url);
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
        List<UserProjectPageData> pageDataList = methodPageDataDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(pageDataList)) {
            if (pageDataList.size() > 1) {                                    // 删除
                for (UserProjectPageData tmp : pageDataList) {
                    tmp.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                    methodPageDataDao.updateById(tmp);
                }
            } else {                                                        // 更新
                UserProjectPageData pageData = pageDataList.get(0);
                pageData.setApiJsonData(pageJsonData);
                pageData.setUpdateTime(DateTool.getCurrentDate());
                methodPageDataDao.updateById(pageData);
                insertFlag = false;
            }
        }

        if (insertFlag) {
            UserProjectPageData pageData = new UserProjectPageData();
            pageData.setApiJsonData(pageJsonData);
            pageData.setMethodUrl(url);
            pageData.setUserName(userName);
            pageData.setProjectName(projectName);
            pageData.setCreateTime(DateTool.getCurrentDate());
            pageData.setUserProjectPageDataId(UUIDUtil.getUUID());
            pageData.setDeleteFlag(CONSTANT_DEFINE.NOT_DELETE);
            while (!CollectionUtils.isEmpty(selectUserProjectPageDataById(pageData))) {
                pageData.setUserProjectPageDataId(UUIDUtil.getUUID());
            }

            methodPageDataDao.insert(pageData);
        }
    }

    /**
     * 获取页面数据
     * @param projectName 项目名
     * @param url 路由
     * @param userName 用户名
     * @return com.quickapi.server.web.dao.entity.UserProjectPageData
     * @author yangxiao
     * @date 2021/1/17 17:48
     */
    public UserProjectPageData getUserProjectMethodPageData(String projectName, String url, String userName) {
        if (StringUtils.isBlank(projectName) || StringUtils.isBlank(url) || StringUtils.isBlank(userName)) {
            throw new BusinessException("getUserProjectMethodPageData()参数不完整");
        }
        UserProjectPageData pageData = null;
        
        QueryWrapper<UserProjectPageData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("METHOD_URL", url);
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
        List<UserProjectPageData> pageDataList = methodPageDataDao.selectList(queryWrapper);
            if (!CollectionUtils.isEmpty(pageDataList)) {
            if (pageDataList.size() > 1) {
                throw new BusinessException("getUserProjectMethodPageData(), projectName: " + projectName
                        + "method url: " + url + ", 找到超过两条数据");
            }

            pageData = pageDataList.get(0);
        }

        return pageData;
  
    }

    /**
     * 根据ID查询数据
     * @param pageData 用户页面数据
     * @return java.util.List<com.quickapi.server.web.dao.entity.UserProjectPageData>
     * @author yangxiao
     * @date 2021/1/17 17:47
     */
    public List<UserProjectPageData> selectUserProjectPageDataById(UserProjectPageData pageData) {
        List<UserProjectPageData> ret = null;
        if (pageData != null) {
            QueryWrapper<UserProjectPageData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("USER_PROJECT_PAGE_DATA_ID", pageData.getUserProjectPageDataId());
            ret = methodPageDataDao.selectList(queryWrapper);
        }

        return ret;
    }
}
