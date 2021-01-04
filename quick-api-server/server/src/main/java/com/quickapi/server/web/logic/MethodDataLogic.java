package com.quickapi.server.web.logic;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.quickapi.server.common.constant.CONSTANT_DEFINE;
import com.quickapi.server.exception.BusinessException;
import com.quickapi.server.web.dao.MethodModelDao;
import com.quickapi.server.web.dao.entity.MethodModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MethodDataLogic {
    @Autowired
    private MethodModelDao methodModelDao;

    /**
     * 保存一条接口信息
     * @param methodModel 单个接口信息
     * @return void
     * @author yangxiao
     * @date 2020/12/29 21:09
     */
    public void saveMethodData(MethodModel methodModel) {
        if (methodModel != null) {
            methodModelDao.insert(methodModel);
        }
    }

    /**
     * 根据项目名称查找项目所有接口
     * @param projectName 项目名称
     * @return java.util.List<com.quickapi.server.web.dao.entity.MethodModel>
     * @author yangxiao
     * @date 2020/12/29 21:21
     */
    public List<MethodModel> findApiDataByProjectName(String projectName) {
        if (StringUtils.isBlank(projectName)) {
            throw new BusinessException("findByProjectName()参数不完整");
        }
        QueryWrapper<MethodModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("PROJECT_NAME", projectName);
        queryWrapper.eq("DELETE_FLAG", CONSTANT_DEFINE.NOT_DELETE);

        return methodModelDao.selectList(queryWrapper);
    }

    /**
     * 删除一组接口方法信息（假删）
     * @param methodModelList 接口方法信息
     * @return void
     * @author yangxiao
     * @date 2021/1/4 21:34
     */
    public void deleteMethodDataList(List<MethodModel> methodModelList) {
        if (!CollectionUtils.isEmpty(methodModelList)) {
            for (MethodModel methodModel : methodModelList) {
                UpdateWrapper<MethodModel> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("PROJECT_NAME", methodModel.getProjectName());
                updateWrapper.eq("URL", methodModel.getUrl());
                methodModel.setDeleteFlag(CONSTANT_DEFINE.IS_DELETE);
                methodModelDao.update(methodModel, updateWrapper);
            }
        }
    }

    /**
     * 更新接口方法信息
     * @param methodModel 接口方法
     * @return void
     * @author yangxiao
     * @date 2021/1/4 21:45
     */
    public void updateMethodData(MethodModel methodModel) {
        if (methodModel != null) {
            UpdateWrapper<MethodModel> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("PROJECT_NAME", methodModel.getProjectName());
            updateWrapper.eq("URL", methodModel.getUrl());
            methodModelDao.update(methodModel, updateWrapper);
        }
    }
}
