package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quickapi.server.common.tools.DateTool;
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
                projectinfo.setCreateTime((Timestamp) DateTool.getCurrentDate());
                projectInfoDao.insert(projectinfo);
            } else {
                if (projectInfoList.size() > 1) {
                    throw new BusinessException("saveProjectInfo(), projectName: " + projectinfo.getProjectName()
                            + ", 找到了超过两个项目信息");
                } else {
                    UpdateWrapper updateWrapper = new UpdateWrapper();
                    updateWrapper.eq("PROJECT_NAME", projectInfoList.get(0).getProjectName());
                    projectInfoDao.update(projectinfo, updateWrapper);
                }
            }
        }
    }
}
