package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.UUIDUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.ProjectInfoDao;
import com.quickapi.server.web.dao.UserProjectInfoDao;
import com.quickapi.server.web.dao.entity.ProjectInfo;
import com.quickapi.server.web.dao.entity.UserProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectInfoLogic {
    @Autowired
    private ProjectInfoDao projectInfoDao;
    @Autowired
    private UserProjectInfoDao userProjectInfoDao;

    /**
     * 保存项目信息
     * <p>
     *     已存在根据项目名更新，不存在插入
     * </p>
     * @param projectinfo 项目信息
     * @return void
     * @author yangxiao
     * @date 2021/1/4 22:17
     */
    public void saveProjectInfo(ProjectInfo projectinfo) {
        if (projectinfo != null) {
            QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PROJECT_NAME", projectinfo.getProjectName());
            List<ProjectInfo> projectInfoList = projectInfoDao.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(projectInfoList)) {
                 projectinfo.setCreateTime(DateTool.getCurrentDate());
                projectinfo.setProjectInfoId(UUIDUtil.getUUID());
                while (!CollectionUtils.isEmpty(selectById(projectinfo))) {
                    projectinfo.setProjectInfoId(UUIDUtil.getUUID());
                }
                projectInfoDao.insert(projectinfo);
            } else {
                if (projectInfoList.size() > 1) {
                    throw new BusinessException("saveProjectInfo(), projectName: " + projectinfo.getProjectName()
                            + ", 找到了超过两个项目信息");
                } else {
                    UpdateWrapper<ProjectInfo> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("PROJECT_NAME", projectInfoList.get(0).getProjectName());
                    projectinfo.setCreateTime(DateTool.getCurrentDate());
                    projectInfoDao.update(projectinfo, updateWrapper);
                }
            }
        }
    }

    /**
     * 根据项目ID查找项目
     * @param projectInfo 项目信息
     * @return java.util.List<com.quickapi.server.web.dao.entity.ProjectInfo>
     * @author yangxiao
     * @date 2021/1/5 20:30
     */
    public List<ProjectInfo> selectById(ProjectInfo projectInfo) {
        List<ProjectInfo> ret = null;
        if (projectInfo != null) {
            QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PROJECT_INFO_ID", projectInfo.getProjectInfoId());
            ret = projectInfoDao.selectList(queryWrapper);
        }

        return ret;
    }

    /**
     * 查询项目信息
     * @param projectName 项目名
     * @return com.quickapi.server.web.dao.entity.ProjectInfo
     * @author yangxiao
     * @date 2021/1/25 19:51
     */
    public ProjectInfo getProjectInfo(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("getProjectInfo()参数不完整");
        }
        ProjectInfo projectInfo = null;

        QueryWrapper<ProjectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_MAME", projectName);
        List<ProjectInfo> projectInfoList = projectInfoDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(projectInfoList)) {
            if (projectInfoList.size() > 1) {
                throw new BusinessException("找到超过两个项目信息");
            } else {
                projectInfo = projectInfoList.get(0);
            }
        }

        return projectInfo;
    }

    /**
     * 获得项目开发者信息
     * @param projectName 项目名称
     * @return java.util.List<java.lang.String>
     * @author yangxiao
     * @date 2021/1/25 19:55
     */
    public List<String> getProjectDevelopers(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("getProjectDevelopers()参数不完整");
        }

        List<String> developers = new ArrayList<>();
        QueryWrapper<UserProjectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        List<UserProjectInfo> projectInfoList = userProjectInfoDao.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(projectInfoList)) {
            for (UserProjectInfo projectInfo : projectInfoList) {
                developers.add(projectInfo.getUserName());
            }
        }

        return developers;
    }

    /**
     * 保存
     * <p>
     *     存在不更新，不存在则插入
     * </p>
     * @param userName 用户名
     * @param projectName 项目名
     * @return void
     * @author yangxiao
     * @date 2021/1/26 20:31
     */
    public void saveUserProjectInfo(String userName, String projectName) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(projectName)) {
            throw new BusinessException("saveUserProjectInfo()参数不完整");
        }

        QueryWrapper<UserProjectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("USER_NAME", userName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);
        List<UserProjectInfo> userProjectInfoList = userProjectInfoDao.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(userProjectInfoList)) {
            UserProjectInfo userProjectInfo = new UserProjectInfo();
            userProjectInfo.setProjectName(projectName);
            userProjectInfo.setUserName(userName);
            userProjectInfo.setDeleteFlag(CONSTANT_DEFINE.NOT_DELETE);
            userProjectInfo.setCreateTime(DateTool.getCurrentDate());
            userProjectInfo.setUserProjectInfoId(UUIDUtil.getUUID());
            while (userProjectInfoDao.selectById(userProjectInfo.getUserProjectInfoId()) != null) {
                userProjectInfo.setUserProjectInfoId(UUIDUtil.getUUID());
            }
            userProjectInfoDao.insert(userProjectInfo);
        }
    }
}
