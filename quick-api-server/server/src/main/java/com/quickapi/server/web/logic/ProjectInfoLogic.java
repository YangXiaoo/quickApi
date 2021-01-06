package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quickapi.server.common.tools.DateTool;
import com.quickapi.server.common.utils.UUIDUtil;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.ProjectInfoDao;
import com.quickapi.server.web.dao.entity.ProjectInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ProjectInfoLogic {
    @Autowired
    private ProjectInfoDao projectInfoDao;

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
}
